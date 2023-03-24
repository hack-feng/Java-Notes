>基于MQ的分布式事务方案，本质上是对本地消息表的一个封装，整体流程与本地消息表一致，唯一不同的就是将本地消息表存在了MQ内部，而不是业务数据库，事务消息解决的是生产端的消息发送与本地事务执行的原子性问题，确保 MQ 生产端正确无误地将消息发送出来，没有多发，也不会漏发，至于发送后消费端有没有正常的消费消息，这种异常场景将由 MQ 消息消费失败重试机制来保证。

## 一、RocketMQ事务消息原理： ##

RocketMQ 在 4.3 版本之后实现了完整的事务消息，基于MQ的分布式事务方案，本质上是对本地消息表的一个封装，整体流程与本地消息表一致，唯一不同的就是将本地消息表存在了MQ内部，而不是业务数据库，事务消息解决的是生产端的消息发送与本地事务执行的原子性问题，这里的界限一定要清楚，是确保 MQ 生产端正确无误地将消息发送出来，没有多发，也不会漏发，至于发送后消费端有没有正常的消费消息，这种异常场景将由 MQ 消息消费失败重试机制来保证。

RocketMQ 设计中的 broker 与 producer 端的双向通信能力，使得 broker 天生可以作为一个事务协调者；而 RocketMQ 本身提供的存储机制则为事务消息提供了持久化能力；RocketMQ 的高可用机制以及可靠消息设计则为事务消息在系统发生异常时依然能够保证达成事务的最终一致性。

### 1、RocketMQ 实现事务一致性的原理： ###

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550632178.png)

> 备注：本地事务的回滚依赖于本地DB的ACID特性，订阅方的成功消费由 MQ Server 的失败重试机制进行保证。

**（1）正常情况：**在事务主动方服务正常，没有发生故障的情况下，发消息流程如下：

步骤①：MQ 发送方向 MQ Server 发送 half 消息，MQ Server 标记消息状态为 prepared，此时该消息 MQ 订阅方是无法消费到的

步骤②：MQ Server 将消息持久化成功之后，向发送方 ACK 确认消息已经成功接收

步骤③：发送方开始执行本地事务逻辑

步骤④：发送方根据本地事务执行结果向 MQ Server 提交二次确认，commit 或 rollback

最终步骤：MQ Server 如果收到的是 commit 操作，则将半消息标记为可投递，MQ订阅方最终将收到该消息；若收到的是 rollback 操作则删除 half 半消息，订阅方将不会接受该消息；如果本地事务执行结果没响应或者超时，则 MQ Server 回查事务状态，具体见步骤（2）的异常情况说明。

**（2）异常情况：**在断网或者应用重启等异常情况下，图中的步骤④提交的二次确认超时未到达 MQ Server，此时的处理逻辑如下：

步骤⑤：MQ Server 对该消息进行消息回查

步骤⑥：发送方收到消息回查后，检查该消息的本地事务执行结果

步骤⑦：发送方根据检查得到的本地事务的最终状态再次提交二次确认。

最终步骤：MQ Server基于 commit/rollback 对消息进行投递或者删除

### 2、RocketMQ事务消息的实现流程： ###

以 RocketMQ 4.5.2 版本为例，事务消息有专门的一个队列 RMQ\_SYS\_TRANS\_HALF\_TOPIC，所有的 prepare 消息都先往这里放，当消息收到 Commit 请求后，就将消息转移到真实的 Topic 队列里，供 Consumer 消费，同时向 RMQ\_SYS\_TRANS\_OP\_HALF\_TOPIC 塞一条消息。简易流程图如下：

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550632580.png)

当应用模块的事务因为中断或者其他的网络原因导致无法立即响应的，RocketMQ 会当做 UNKNOW 处理，对此 RocketMQ 事务消息提供了一个补救方案：定时回查事务消息的事务执行状态，简易流程图如下：

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550632958.png)

## 二、Springboot 整合 RocketMQ 实现事务消息： ##

该部分将从 "下订单 + 扣减库存"的案例来介绍 SpringBoot 如何整合 RocketMQ 并使用事务消息保证最终一致性。核心思路是订单服务（生产端）向 RocketMQ 发送库存扣减消息，再执行本地订单生成逻辑，最后交由 RocketMQ 通知 库存服务扣减库存并保证库存扣减消息被正常消费。

案例中使用到的服务分为两个，订单服务和库存服务；涉及到的数据库表主要有三个，订单表、存储表，本地事务状态表。由于这几个表都比较简单，这里就不将对应的建表语句粘贴出来了，同样对应的 Pojo对象、Dao层、Service层 代码也不粘贴出来了，下面只展示核心逻辑的代码。

### 1、启动 RocketMQ 服务端： ###

RocketMQ的安装与部署请参考这篇文章：[https://blog.csdn.net/a745233700/article/details/122531859][https_blog.csdn.net_a745233700_article_details_122531859]

### 2、在父pom文件中引入依赖： ###

    <!-- rocketmq 事务消息 -->
        <dependency>
            <groupId>org.apache.rocketmq</groupId>
            <artifactId>rocketmq-spring-boot-starter</artifactId>
            <version>2.2.1</version>
        </dependency>

### 3、生产端代码： ###

生产端的核心逻辑就是向 RocketMQ 投递事务消息，并执行本地事务，最后将本地事务的执行结果通知到 RocketMQ

**（1）RocketMQ相关配置：**

在 application.properties 配置文件中添加以下配置：

    rocketmq.name-server=172.28.190.101:9876
    rocketmq.producer.group=order_shop

**（2）创建一个监听类：**

实现 TransactionListener 接口，在实现的数据库事务提交方法executeLocalTransaction() 和回查事务状态方法checkLocalTransaction() 中模拟结果

    /**
     * rocketmq 事务消息回调类
     */
    @Slf4j
    @Component
    public class OrderTransactionListener implements TransactionListener
    {
        @Resource
        private ShopOrderMapper shopOrderMapper;
    
        /**
         * half消息发送成功后回调此方法，执行本地事务
         *
         * @param message 回传的消息，利用transactionId即可获取到该消息的唯一Id
         * @param arg 调用send方法时传递的参数，当send时候若有额外的参数可以传递到send方法中，这里能获取到
         * @return 返回事务状态，COMMIT：提交  ROLLBACK：回滚  UNKNOW：回调
         */
        @Override
        @Transactional
        public LocalTransactionState executeLocalTransaction(Message message, Object arg)
        {
            log.info("开始执行本地事务：订单信息：" + new String(message.getBody()));
            String msgKey = new String(message.getBody());
            ShopOrderPojo shopOrder = JSONObject.parseObject(msgKey, ShopOrderPojo.class);
    
            int saveResult;
            LocalTransactionState state;
            try
            {
                //修改为true时，模拟本地事务异常
                boolean imitateException = true;
                if(imitateException)
                {
                    throw new RuntimeException("更新本地事务时抛出异常");
                }
    
                // 生成订单，本地事务的回滚依赖于DB的ACID特性，所以需要添加Transactional注解。当本地事务提交失败时，返回ROLLBACK_MESSAGE，则会回滚rocketMQ中的half message，保证分布式事务的一致性。
                saveResult = shopOrderMapper.insert(shopOrder);
                state = saveResult == 1 ? LocalTransactionState.COMMIT_MESSAGE : LocalTransactionState.ROLLBACK_MESSAGE;
    
                // 更新本地事务并将事务号持久化，为后续的幂等做准备
                // TransactionDao.add(transactionId)
            }
            catch (Exception e)
            {
                log.error("本地事务执行异常，异常信息：", e);
                state = LocalTransactionState.ROLLBACK_MESSAGE;
            }
    
            //修改为true时，模拟本地事务超时，对于超时的消息，rocketmq会调用checkLocalTransaction方法回查本地事务执行状况
            boolean imitateTimeout = false;
            if(imitateTimeout)
            {
                state = LocalTransactionState.UNKNOW;
            }
    
            log.info("本地事务执行结果：msgKey=" + msgKey + ",execute state:" + state);
            return state;
        }


​    
        /**
         * 回查本地事务接口
         *
         * @param messageExt 通过获取transactionId来判断这条消息的本地事务执行状态
         * @return 返回事务状态，COMMIT：提交  ROLLBACK：回滚  UNKNOW：回调
         */
        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt messageExt)
        {
            log.info("调用回查本地事务接口：msgKey=" +  new String(messageExt.getBody()));
    
            String msgKey = new String(messageExt.getBody());
            ShopOrderPojo shopOrder = JSONObject.parseObject(msgKey, ShopOrderPojo.class);
    
            // 备注：此处应使用唯一ID查询本地事务是否执行成功，唯一ID可以使用事务的transactionId。但为了验证方便，只查询DB的订单表是否存在对应的记录
            // TransactionDao.isExistTx(transactionId)
            List<ShopOrderPojo> list = shopOrderMapper.selectList(new QueryWrapper<ShopOrderPojo>()
                    .eq("shop_id", shopOrder.getShopId())
                    .eq("user_id", shopOrder.getUserId()));
    
            LocalTransactionState state = list.size() > 0 ? LocalTransactionState.COMMIT_MESSAGE : LocalTransactionState.ROLLBACK_MESSAGE;
            log.info("调用回查本地事务接口的执行结果：" +  state);
    
            return state;
        }
    }

为了方便验证，上面 Demo 使用了两个 boolean 变量 imitateException、imitateTimeout 分别模拟了事务执行异常和超时的情况，只需要将布尔值设置为 true 即可。

**（3）投递事务消息：**

    import com.alibaba.fastjson.JSONObject;
    import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
    import lombok.extern.slf4j.Slf4j;
    import org.apache.rocketmq.client.producer.SendStatus;
    import org.apache.rocketmq.client.producer.TransactionMQProducer;
    import org.apache.rocketmq.client.producer.TransactionSendResult;
    import org.apache.rocketmq.spring.core.RocketMQTemplate;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.messaging.support.MessageBuilder;
    import org.springframework.stereotype.Service;
    
    @Slf4j
    @Service
    public class ShopOrderServiceImpl extends ServiceImpl<ShopOrderMapper, ShopOrderPojo> implements ShopOrderService
    {
        @Resource
        private RocketMQTemplate rocketMQTemplate;
        @Autowired
        private OrderTransactionListener orderTransactionListener;
    
        /**
         * 发送事务消息
         */
        @Override
        public boolean sendOrderRocketMqMsg(ShopOrderPojo shopOrderPojo)
        {
            String topic = "storage";
            String tag = "reduce";
    
            // 设置监听器，此处如果使用MQ其他版本，可能导致强转异常
            ((TransactionMQProducer) rocketMQTemplate.getProducer()).setTransactionListener(orderTransactionListener);
    
            //构建消息体
            String msg = JSONObject.toJSONString(shopOrderPojo);
            org.springframework.messaging.Message<String> message = MessageBuilder.withPayload(msg).build();
            //发送事务消息，由消费者进行进行减少库存
            TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(topic + ":" + tag , message, null);
    
            log.info("Send transaction msg result: " + sendResult);
            return sendResult.getSendStatus() == SendStatus.SEND_OK;
        }
    }

### 4、消费端代码： ###

消费端的核心逻辑就是监听 MQ，接收消息；接收到消息之后扣减库存

**（1）RocketMQ相关配置：**

在 application.properties 配置文件中添加以下配置：

    rocketmq.name-server=172.28.190.101:9876
    rocketmq.consumer.group=order_shop

**（2）消费监听类：** 

    import com.alibaba.fastjson.JSONObject;
    import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
    import org.apache.rocketmq.spring.core.RocketMQListener;
    import org.springframework.stereotype.Component;
    import javax.annotation.Resource;
    
    /**
     * 库存管理消费者类
     **/
    @Component
    @RocketMQMessageListener (consumerGroup = "order_storage", topic = "storage")
    public class StorageConsumerListener implements RocketMQListener<String>
    {
        @Resource
        private TStorageService tStorageService;
    
        /**
         * rocketMQ消费者
         */
        @Override
        public void onMessage(String message)
        {
            System.out.println("消费者开始消费：从MQ中获取的消息是：" + message);
            ShopOrderPojo shopOrder = JSONObject.parseObject(message, ShopOrderPojo.class);
    
            // 1、幂等校验，防止消息重复消费--此处省略相关的代码逻辑：
            // TransactionDao.isExistTx(transactionId)
    
            // 2、执行消息消费操作--减少商品库存：
            TStoragePojo shop = tStorageService.getById(shopOrder.getShopId());
            shop.setNum(shop.getNum() - 1);
            boolean updateResult = tStorageService.updateById(shop);
    
            // 3、添加事务操作记录--此次省略代码：
            // TransactionDao.add(transactionId)
    
            System.out.println("消费者完成消费：操作结果：" + updateResult);
        }
    }

至此，一个完整的基于 RocketMQ 事务消息实现的分布式事务的最终一致性就完成了。

--------------------

> 相关阅读：
> 
> [常见的服务器架构入门：从单体架构、EAI 到 SOA 再到微服务和 ServiceMesh][EAI _ SOA _ ServiceMesh]
> 
> [常见分布式理论（CAP、BASE）和一致性协议（Gosssip协议、Raft一致性算法）][CAP_BASE_Gosssip_Raft]
> 
> [一致性哈希算法原理详解][Link 1]
> 
> [Nacos注册中心的部署与用法详细介绍][Nacos]
> 
> [Nacos配置中心用法详细介绍][Nacos 1]
> 
> [SpringCloud OpenFeign 远程HTTP服务调用用法与原理][SpringCloud OpenFeign _HTTP]
> 
> [什么是RPC？RPC框架dubbo的核心流程][RPC_RPC_dubbo]
> 
> [服务容错设计：流量控制、服务熔断、服务降级][Link 2]
> 
> [sentinel 限流熔断神器详细介绍][sentinel]
> 
> [Sentinel 规则持久化到 apollo 配置中心][Sentinel _ apollo]
> 
> [Sentinel-Dashboard 与 apollo 规则的相互同步][Sentinel-Dashboard _ apollo]
> 
> [Spring Cloud Gateway 服务网关的部署与使用详细介绍][Spring Cloud Gateway]
> 
> [Spring Cloud Gateway 整合 sentinel 实现流控熔断][Spring Cloud Gateway _ sentinel]
> 
> [Spring Cloud Gateway 整合 knife4j 聚合接口文档][Spring Cloud Gateway _ knife4j]
> 
> [常见分布式事务详解（2PC、3PC、TCC、Saga、本地事务表、MQ事务消息、最大努力通知）][2PC_3PC_TCC_Saga_MQ]
> 
> [分布式事务Seata原理][Seata]
> 
> [RocketMQ事务消息原理][RocketMQ]

--------------------

参考文章：[https://www.cnblogs.com/huangying2124/p/11702761.html][https_www.cnblogs.com_huangying2124_p_11702761.html]


[1e5600de658e4fa8818a66f43d8a586b.png]: https://img-blog.csdnimg.cn/1e5600de658e4fa8818a66f43d8a586b.png
[40e2cda476744f8892717d3dda54f334.png]: https://img-blog.csdnimg.cn/40e2cda476744f8892717d3dda54f334.png
[65a9e3468dcc4e2a8070c8db242beb98.png]: https://img-blog.csdnimg.cn/65a9e3468dcc4e2a8070c8db242beb98.png
[https_blog.csdn.net_a745233700_article_details_122531859]: https://blog.csdn.net/a745233700/article/details/122531859
[EAI _ SOA _ ServiceMesh]: https://blog.csdn.net/a745233700/article/details/117448077
[CAP_BASE_Gosssip_Raft]: https://blog.csdn.net/a745233700/article/details/122401700
[Link 1]: https://blog.csdn.net/a745233700/article/details/120814088
[Nacos]: https://blog.csdn.net/a745233700/article/details/122915663
[Nacos 1]: https://blog.csdn.net/a745233700/article/details/122916208
[SpringCloud OpenFeign _HTTP]: https://blog.csdn.net/a745233700/article/details/122916856
[RPC_RPC_dubbo]: https://blog.csdn.net/a745233700/article/details/122445199
[Link 2]: https://blog.csdn.net/a745233700/article/details/120819219
[sentinel]: https://blog.csdn.net/a745233700/article/details/122733366
[Sentinel _ apollo]: https://blog.csdn.net/a745233700/article/details/122725604
[Sentinel-Dashboard _ apollo]: https://blog.csdn.net/a745233700/article/details/122659459
[Spring Cloud Gateway]: https://blog.csdn.net/a745233700/article/details/122917167
[Spring Cloud Gateway _ sentinel]: https://blog.csdn.net/a745233700/article/details/122917160
[Spring Cloud Gateway _ knife4j]: https://blog.csdn.net/a745233700/article/details/122917137
[2PC_3PC_TCC_Saga_MQ]: https://blog.csdn.net/a745233700/article/details/122402303
[Seata]: https://blog.csdn.net/a745233700/article/details/122402795
[RocketMQ]: https://blog.csdn.net/a745233700/article/details/122656108
[https_www.cnblogs.com_huangying2124_p_11702761.html]: https://www.cnblogs.com/huangying2124/p/11702761.html