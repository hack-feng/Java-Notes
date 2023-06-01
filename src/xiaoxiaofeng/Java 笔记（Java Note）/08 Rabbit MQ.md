 *  AMQP 协议（Advanced Message Queuing Protocol），一个提供统一消息服务的应用层标准高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计
 *  RabbitMQ 是采用 Erlang 语言实现的 AMQP 协议的消息中间件
 *  RabbitMQ 的特点：可靠性、灵活的路由、扩展性、髙可用性、多种协议、多语言客户端、管理界面、插件机制

# 相关概念 #

 *  Producer：生产者创建消息，然后发布到 RabbitMQ 中
 *  Message：消息一般可以包含 2 个部分：消息体和标签。消息体（payload）一般是一个带有业务逻辑结构的数据；消息的标签（label）用来表述这条消息，比如一个**交换器的名称**和一个**路由键**
 *  Consumer：消费者连接到 RabbitMQ 服务器，并订阅到队列上
 *  Broker：消息中间件的服务节点
 *  Queue：队列，是 RabbitMQ 的内部对象，用于存储消息，多个消费者订阅同一个队列时，队列中的消息将以**轮询**（round-robin）的分发方式发送给消费者
 *  Exchange：交换器，生产者将消息发送到 Exchange，由交换器将消息路由到一个或者多个队列中。如果路由不到，或许会返回给生产者（需要设置 mandatory 参数），或许直接丢弃
 *  RoutingKey：路由键，生产者将消息发给交换器时，用来指定这个消息的路由规则
 *  Binding：绑定，RabbitMQ 中通过绑定将交换器与队列关联起来，在绑定时一般会指定一个绑定键（BindingKey）
 *  一个 Connection 可以用来创建多个 Channel 实例，但是 Channel 实例不能在线程间共享，应用程序应该为每一个线程开辟一个 Channel
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414526258.jpeg) 
    图 1 RabbitMQ模型架构

# 交换器类型 #

 *  fanout：把所有发送到该交换器的消息路由到所有与该交换器绑定的队列中（广播）
 *  direct：把消息路由到 BindingKey 和 RoutingKey 完全匹配的队列中（单播）
 *  topic：将消息路由到 BindingKey 和 RoutingKey 相匹配的队列中。BindingKey 和 RoutingKey 是一个点号“.”分隔的字符串，**BindingKey** 中可以存在两种特殊字符“*”（匹配一个单词）、“\#”（匹配零或多个单词），用于做\*模糊匹配*
 *  headers：不依赖于路由键的匹配规则来路由消息，而是根据发送的消息内容中的 headers 属性进行匹配

# RabbitMQ 运转流程 #

## 生产者发送消息 ##

 *  生产者连接到 RabbitMQ Broker，建立一个连接（Connection），开启一个信道（Channel）
 *  生产者声明一个交换器，并设置相关属性，比如交换机类型、是否持久化等
 *  生产者声明一个队列，并设置相关属性，比如是否排他、是否持久化、是否自动删除等
 *  生产者通过路由键将交换器和队列绑定起来
 *  生产者发送消息至 RabbitMQ Broker，其中包含路由键、交换器等信息
 *  相应的交换器根据接收到的路由键查找相匹配的队列
 *  如果找到，则将从生产者发送过来的消息存入相应的队列中；如果没有找到，则根据生产者配置的属性选择丢弃还是回退给生产者
 *  关闭信道，关闭连接

> 
> 如果尝试声明一个已经存在的交换器或者队列，只要声明的参数完全匹配现存的交换器或者队列，RabbitMQ 就可以什么都不做，并成功返回；如果声明的参数不匹配则会抛出异常
> 

``````````java
public class RabbitProducer { 
      private static String exchangeName = "exchange_demo"; 
      private static String routingKey = "routingkey_demo"; 
      private static String queueName = "queue_demo"; 
      private static SortedSet<Long> unconfirmedSet = Collections.synchronizedSortedSet(new TreeSet<Long>()); 
  
      public static void main(String[] args) throws IOException, TimeoutException, InterruptedException { 
          ConnectionFactory factory = new ConnectionFactory(); 
          factory.setHost("192.168.0.2"); factory.setPort(5672); // RabbitMQ 服务端默认端口号为 5672 
          factory.setUsername("root"); factory.setPassword("123456"); 
          // factory.setUri("amqp://userName:password@host:port/virtualHost"); // 或者使用 URI 的方式 
          Connection connection = factory.newConnection(); // 创建连接 
  
          Channel channel = connection.createChannel(); // 创建信道 
          channel.confirmSelect(); // 将信道置为 publisher confirm 模式 
          // 声明一个type="direct"、持久化的、非自动删除的交换器 
          // 自动删除的前提是所有与这个交换器绑定的队列或者交换器都与此解绑 
          channel.exchangeDeclare(exchangeName, "direct", true, false, null); 
          // 声明一个持久化、非排他的、非自动删除的队列 
          // 排他队列仅对首次声明它的连接可见，并在连接断开时自动删除 
          // 自动删除的前提是所有与这个队列连接的消费者都断开时 
          channel.queueDeclare(queueName, true, false, false, null); 
          // 将交换器与队列通过路由键绑定 
          channel.queueBind(queueName, exchangeName, routingKey); 
  
          // 添加 ReturnListener 监听器，获取到没有被正确路由到合适队列的消息 
          channel.addReturnListener(new ReturnListener() { 
              @Override 
              public void handleReturn(int replyCode, String replyText, 
                                       String exchange, String routingKey, 
                                       AMQP.BasicProperties basicProperties, 
                                       byte[] body) { 
                  System.out.println("Basic.Return 返回的结果：" + new String(body)); 
              } 
          }); 
  
          // 异步 confirm 
          // 添加 ConfirmListener 监听器设置回调方法 
          channel.addConfirmListener(new ConfirmListener() { 
              @Override 
              public void handleAck(long deliveryTag, boolean multiple) { 
                  System.out.println("Nack, SeqNo: " + deliveryTag + ", multiple: " + multiple); 
                  if (multiple) { 
                      unconfirmedSet.headSet(deliveryTag + 1).clear(); 
                  } else { 
                      unconfirmedSet.remove(deliveryTag); 
                  } 
              } 
  
              @Override 
              public void handleNack(long deliveryTag, boolean multiple) { 
                  if (multiple) { 
                      unconfirmedSet.headSet(deliveryTag + 1).clear(); 
                  } else { 
                      unconfirmedSet.remove(deliveryTag); 
                  } 
                  // 注意这里需要添加处理消息重发的场景 
              } 
          }); 
          // 普通 confirm 
          /*if (!channel.waitForConfirms()) { 
              System.out.println("send message failed"); 
              // do something else.... 
          }*/ 
  
          // 发送一条持久化的消息：hello world! 
          // mandatory 设置为 true，消息不可达时，返回给生产者 
          // 消息的基本属性集包含 14 个属性成员 
          // AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().contentType("text/plain").deliveryMode(2).priority(0).build(); 
          long nextSeqNo = channel.getNextPublishSeqNo(); 
          channel.basicPublish(exchangeName, routingKey, true, MessageProperties.PERSISTENT_TEXT_PLAIN, 
                  "Hello World!".getBytes()); 
          unconfirmedSet.add(nextSeqNo); 
  
          // 关闭资源 
          channel.close(); 
          connection.close(); 
      } 
  }
``````````

## 消费者接收消息 ##

 *  消费者连接到 RabbitMQ Broker，建立一个连接（Connection），开启一个信道（Channel）
 *  消费者向 RabbitMQ Broker **请求消费**相应队列中的消息，可能会设置相应的回调函数，以及做一些准备工作
 *  消费者等待 RabbitMQ Broker 回应并投递相应队列中的消息，消费者接收消息
 *  消费者确认（ack）接收到的消息
 *  RabbitMQ 从队列中删除相应已经被确认的消息
 *  关闭信道，关闭连接

``````````java
public class RabbitConsumer { 
      private static String queueName = "queue_demo"; 
  
      public static void main(String[] args) throws IOException, TimeoutException, InterruptedException { 
          ConnectionFactory factory = new ConnectionFactory(); 
          factory.setUsername("root"); 
          factory.setPassword("123456"); 
          // 这里的连接方式与生产者的略有不同，注意辨别区别 
          Address[] addresses = new Address[]{new Address("192.168.0.2", 5672)}; 
          Connection connection = factory.newConnection(addresses); // 创建连接 
          final Channel channel = connection.createChannel(); // 创建信道 
          channel.basicQos(64); // 设置客户端最多接收未被 ack 的消息的个数 
          // 消费消息（推模式） 
          // 自动确认（autoAck）设置为 false，即不自动确认 
          // 消费者标签，用来区分多个消费者 
          channel.basicConsume(queueName, false, "myConsumerTag", new DefaultConsumer(channel) { 
              // 设置消费者的回调函数 
              @Override 
              public void handleDelivery(String consumerTag, 
                                         Envelope envelope, 
                                         AMQP.BasicProperties properties, 
                                         byte[] body) 
                      throws IOException { 
                  System.out.println("recv message: " + new String(body)); 
                  // 在接收到消息之后进行显式 ack 操作 
                  channel.basicAck(envelope.getDeliveryTag(), false); 
              } 
          }); 
          // 消费消息（拉模式） 
          // GetResponse response = channel.basicGet(queueName, false); 
          // System.out.println(new String(response.getBody())); 
          // channel.basicAck(response.getEnvelope().getDeliveryTag(), false); 
          channel.close(); 
          connection.close(); 
      } 
  }
``````````

### 消费模式 ###

 *  消费模式分两种：推（Push）模式和拉（Pull）模式
 *  推模式采用 Basic.Consume 进行消费，在推模式中，通过持续订阅的方式来消费消息，**在接收模式期间，RabbitMQ 会不断地推送消息给消费者**，但推送消息的个数会受到 Basic.Qos 的限制（channel.basicQos 设置为 0 则表示没有上限）
 *  拉模式则是调用 Basic.Get 进行消费，获得**单条消息**，Basic.Qos 的使用对于拉模式的消费方式无效

### 消息顺序性 ###

 *  指消费者消费到的消息和发送者发布的消息的顺序是一致
 *  在消息队列**只有一个生产者、一个消费者**的情况下可以保证消息的顺序性
 *  在多个消费者的情况下，如果要保证消息的顺序性，需要业务方使用 RabbitMQ 之后做进一步的处理，比如**在消息体内添加全局有序标识**（类似 Sequence ID）来实现

# 消费端的确认与拒绝 #

## 消费端的确认 ##

 *  当 autoAck 等于 false 时，RabbitMQ 会等待消费者显式地回复确认信号（调用 channel.basicAck 方法）后才从内存（或者磁盘）中移去消息
 *  当 autoAck 等于 true 时，RabbitMQ 会自动把发送出去的消息置为确认，然后从内存（或者磁盘）中删除
 *  如果 RabbitMQ 一直没有收到消费者的确认信号，并且消费此消息的消费者**已经断开连接**，RabbitMQ 才会将该消息重新加入到队列，等待投递

## 消费端的拒绝 ##

 *  调用与其对应的 channel.basicReject 或 channel.basicNack 方法来告诉 RabbitMQ 拒绝这个消息
 *  被拒绝的消息可以重新加入到队列（设置 requeue 参数为 true），或变成“死信”（设置 requeue 参数为 false）

# 生产者确认 #

 *  默认情况下生产者是**不知道**消息有没有正确地到达服务器

## 事务机制 ##

 *  channel.txSelect 用于将当前的信道设置成事务模式
 *  channel.txCommit 用于提交事务
 *  channel.txRollback 用于事务回滚，补偿发送
 *  采用事务机制实现会严重降低 RabbitMQ 的消息吞吐量：在一条消息发送之后会使发送端**阻塞**，以等待 RabbitMQ 的回应，之后才能继续发送下一条消息

## 发送方确认（publisher confirm）机制 ##

 *  生产者将信道设置成 confirm（确认）模式
 *  一旦信道进入 confirm 模式，所有在该信道上面发布的消息都会被指派一个唯一的 ID（从 1 开始）
 *  消息被投递到所有匹配的队列之后，RabbitMQ 就会发送一个确认（Basic. Ack）给生产者（包含消息的唯一 ID）
 *  如果消息和队列是可持久化的，那么确认消息会**在消息写入磁盘之后**发出
 *  RabbitMQ 因为自身内部错误导致消息丢失就会发送一条 nack（Basic.Nack）命令
 *  当消息最终得到确认之后，生产者应用程序可以通过**回调**方法来处理该确认消息
 *  在一条消息发送之后不会使发送端阻塞，可以继续发送下一条消息，无需等待 RabbitMQ 的回应
 *  生产者通过调用 channel.confirmSelect 方法，然后进行同步 confirm 或者异步 confirm：
    
     *  同步 confirm：在每次发送一条消息或者一批消息后，通过调用 channel.waitForConfirms 方法判断被发送的消息被 ack 还是 nack
     *  异步 confirm：通过调用 channel.addConfirmListener 来添加 ConfirmListener 监听器设置回调方法，服务端确认了一条或者多条消息后客户端会回调这个方法进行处理，具体步骤：
        
         *  在每次发送一条消息后，把消息保存到一个 kv 存储里
         *  收到消息 ack，就从 kv 存储中删除该消息；收到消息 nack，就从 kv 存储取出该消息然后重新投递
         *  也可以对 kv 存储里的消息做监控，如果超过一定时长没收到 ack，就主动重发消息

# 不可达消息何去何从 #

## mandatory 参数 ##

 *  当 mandatory 参数设置为 true 时，交换器无法根据自身的类型和路由键找到一个符合条件的队列，那么 RabbitMQ 会调用 Basic.Return 命令将消息返回给生产者
 *  当 mandatory 参数设置为 false 时，出现上述情形，则消息直接被丢弃
 *  生产者可以通过调用 channel.addReturnListener 来添加 ReturnListener 监听器，获取到没有被正确路由到合适队列的消息

## 备份交换器 ##

 *  将未被路由的消息存储在 RabbitMQ 中的备份交换器（Alternate Exchange）绑定的队列中，备份交换器建议设置为 fanout 类型
 *  在声明交换器（调用 channel.exchangeDeclare 方法）的时添加 alternate-exchange 参数
 *  如果备份交换器和 mandatory 参数一起使用，那么 mandatory 参数无效

# 过期时间（TTL） #

## 设置消息的 TTL ##

 *  消息在队列中的生存时间一旦超过设置的 TTL 值时，就会变成“死信”（Dead Message），消费者将无法再收到该消息
 *  设置方式：
    
     *  通过队列属性设置，队列中所有消息都有相同的过期时间，在 channel.queueDeclare 方法中加入 x-message-ttl 参数，单位为毫秒
     *  对消息本身进行单独设置，每条消息的 TTL 可以不同，在 channel.basicPublish 方法中加入 expiration 的属性参数，单位为毫秒

## 设置队列的 TTL ##

 *  队列处于**未使用**状态的时间一旦超过设置的 TTL 值时，就会被自动删除
 *  设置方式：在 channel.queueDeclare 方法中设置 x-expires 参数，单位为毫秒

# 死信队列 #

 *  消息变成死信（dead message）的情况：消息被拒绝，并且设置 requeue 参数为 false；消息过期；队列达到最大长度
 *  当消息在一个队列中变成死信之后，可以被转存到与死信交换器（DLX，Dead-Letter-Exchange）绑定的死信队列中
 *  设置方式：在 channel.queueDeclare 方法中设置 x-dead-letter-exchange 参数来为这个队列添加 DLX

# 延迟队列 #

 *  当消息被发送以后，等待特定时间后，消费者才能拿到这个消息进行消费
 *  通过 DLX 和 TTL 实现：消费者订阅死信队列

# 优先级队列 #

 *  在优先级队列中，优先级髙的消息优先被消费
 *  通过设置队列的 x-max-priority 参数来实现优先级队列，然后在发送时在消息中设置消息的优先级

# RPC 实现 #

 *  客户端发送请求消息，服务端回复响应的消息
 *  为了接收响应的消息，需要在请求消息中用 replyTo 设置一个**回调队列**（RPC 服务端回复请求时的目的队列），以及用 correlationld 用来标记一个请求

# 持久化 #

 *  交换器的持久化：在声明交换器是将 durable 参数置为 true
 *  队列的持久化：在声明队列是将 durable 参数置为 true
 *  消息的持久化：将消息的投递模式（BasicProperties 中的 deliveryMode 属性）设置为 2

# 镜像队列（Mirror Queue） #

 *  将队列镜像到**集群中的其它 Broker 节点**上
 *  每一个**镜像队列**都包含一个主节点（master）和若干个从节点（slave）
 *  发送到镜像队列的**所有消息**会被同时发往 master 和所有的 slave 上
 *  除发送消息（Basic.Publish）外的所有动作都只会向 master 发送，然后再由 master 将命令执行的结果广播给各个 slave。比如消费者与 slave 建立了 TCP 连接之后执行一个 Basic .Get 的操作，那么首先是由 slave 将 Basic.Get 请求发往 master，再由 master 准备好数据返回给 slave，最后由 slave 投递给消费者
 *  如果 master 由于某种原因失效，那么“资历最老”（加入的时间最长）的 slave 会被提升为新的 master
 *  镜像队列的配置主要是通过添加相应的 **Policy**（策略）来完成的
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414526864.png) 
    图 2 RabbitMQ镜像集群结构

# 消息传输保障 #

 *  RabbitMQ 支持“最少一次”投递，即消息绝不会丢失，但可能会重复传输，需要：
    
     *  消息生产者开启事务机制或者 publisher confirm 机制，以确保消息可以可靠地传输到 RabbitMQ 中
     *  消息生产者使用 mandatory 参数或者备份交换器，来确保不可达消息不会被丢弃
     *  交换器、队列和消息都进行持久化处理，以确保 RabbitMQ 服务器在遇到异常情况时不会造成消息丢失
     *  消费者在消费消息时将 autoAck 设置为 false，在消费完消息之后再进行手动确认，以避免在消费端引起不必要的消息丢失
 *  RabbitMQ 无法保证“恰好一次”投递，即每条消息只会被传输一次且仅传输一次
 *  由于网络断开或者其他原因造成 RabbitMQ 没有收到消费者的确认命令，生产者没有收到 RabbitMQ 返回确认通知，从而会出现**重复消费**的问题
 *  解决方法：
    
     *  可以引入 GUID (Globally Unique Identifier)，保证每条消息都有唯一编号，借助 Redis 等集中式缓存或者日志表进行去重处理；
     *  消费端处理消息的业务逻辑保持幂等性

# 多用户与权限 #

 *  虚拟主机（vhost，virtual host），每一个 vhost 本质上是一个独立的小型 RabbitMQ 服务器，拥有自己独立的队列、交换器及绑定关系等，并且它拥有自己独立的权限，RabbitMQ 默认创建的 vhost 为“/”
 *  在 RabbitMQ 中，权限控制则是以 vhost 为单位的
 *  默认用户 guest 的默认密码为 guest，注意：默认的 guest 用户只能通过本地网络访问 Broker（可修改 loopback\_users 配置项）
 *  添加新用户 `rabbitmqctl add_user root 123456`
 *  设置 root 用户为管理员角色：`rabbitmqctl set_user_tags root administrator`

# RabbitMQ 管理 #

 *  rabbitmqctl 是 RabbitMQ 中的 CLI 管理工具
 *  rabbitmq\_management 插件提供用户图形化的管理功能、监控功能以及 HTTP API 接口，启用 `rabbitmq-plugins enable rabbitmq_management`，管理界面端口是 15672


[RabbitMQ]: https://static.sitestack.cn/projects/sdky-java-note/abab1e76830c4875b0788beeffdb20be.jpeg
[RabbitMQ 1]: https://static.sitestack.cn/projects/sdky-java-note/b016765a802e01aa5cd7322c7225bc4e.png