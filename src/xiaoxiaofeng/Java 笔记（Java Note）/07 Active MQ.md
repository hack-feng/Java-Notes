# 消息中间件 #

 *  消息中间件 MOM（Message Oriented Middleware）是在分布式环境中，两个或多个独立运行的系统之间，提供消息通讯作用的中介
 *  作用：把分布式应用中的各个子系统之间服务的调用以**消息通讯**的方式交互
 *  应用场景：存在并发访问的业务（请求排队，流量削峰）；耗时比较久的业务（异步处理）；需要解耦的业务（应用解耦）；海量数据同步（日志）；任务调度；分布式事务

# JMS #

 *  JMS 即 Java 消息服务（Java Message Service），Java 访问消息中间件的一组应用程序接口，提供消息的创建、发送、接收、读取等一系列服务

## JMS 相关概念 ##

 *  JMS Provider：实现 JMS 接口和规范的消息中间件
 *  JMS Producer：消息生产者，创建和发送 JMS 消息的客户端应用
 *  JMS Consumer：消息消费者，接收和处理 JMS 消息的客户端应用
 *  JMS Message：JMS 消息
    
     *  分 3 个部分：消息头、消息属性、消息体（封装具体的消息数据）
     *  JMS 支持的消息类型包括：简单文本（TextMessage)、可序列化的对象（ObjectMessage )、键值对（MapMessage)、字节流（BytesMessage)、流（StreamMessage)，以及无有效负载的消息（Message）等
 *  JMS Destination：消息目的地，包含 queue 和 topic
 *  JMS Domain：消息传递域，JMS 规范中定义了两种消息传递域，分别是点对点和发布/订阅消息传递域：
    
     *  点对点（Point-to-Point，简写 PTP 或 P2P）消息传递域，该消息传递域发送的消息目的地称为**队列**（queue）
        
         *  每个消息只能有一个消费者：当消息的生产者发送一条消息到队列之后，只有注册到同一个消息队列中**一个**消息消费者会接收到该消息
         *  消息的生产者和消费者之间没有时间上的相关性，无论消息消费者在提取消息的时候，消息生产者是否处于运行状态，消息消费者还是可以提取消息
     *  发布/订阅（Publish/Subscribe，简写 Pub/Sub）消息传递域，该消息传递域发送的消息目的地称为**主题**（topic）
        
         *  每个消息可以有多个消费者：消息的发布者需将消息投递给 topic 后，**所有**订阅了该 topic 的消息订阅者都会接收到该消息
         *  生产者和消费者之间有时间上的相关性，订阅一个主题的消费者只能接收自它订阅之后发布的消息
 *  JMS Session：与 JMS Provider 所建立的会话，可设置是否开启事务，消息消费确认模式

# ActiveMQ #

 *  采用 Java 语言编写的完全基于 JMS1.1 规范的面向消息的中间件

## ActiveMQ 的安装运行 ##

 *  运行：bin 目录下的 activemq.bat
 *  配置：conf 目录下的 activemq.xml 文件
    
     *  设置 ActiveMQ 的消息监听端口，默认为 61616
     *  设置 ActiveMQ 的 web 管理界面的端口，默认为 8161。ActiveMQ 使用的是内嵌的 jetty 服务器来运行它提供的管理界面的，该管理界面的访问地址为 localhost:8161
     *  设置 ActiveMQ 的访问用户名和密码，用户名和密码默认都为 admin。设置方法，添加如下配置：

``````````xml
<!-- 添加访问 ActiveMQ 的账号密码 --> 
  <plugins> 
      <simpleAuthenticationPlugin> 
          <users> 
          <authenticationUser username="sdky" password="123456" groups="users, admins"/> 
          </users> 
      </simpleAuthenticationPlugin> 
  </plugins>
``````````

## ActiveMQ 的使用 ##

 *  session 事务
    
     *  消息生产者如果设置了事务，那么需要提交事务才会发送消息，如果没有设置事务，则自动发送消息
     *  消息消费者如果设置了事务，那么需要提交事务才能确认消息，如果事务回滚，那么消息会再次传送
 *  消息确认模式
    
     *  如果 session 开启事务，那么消息确认模式只能是 `Session.SESSION_TRANSACTED`，就算设置其它模式也会被忽略
        
         *  如果 session 不开启事务，那么消息确认模式有以下三种：
         *  `Session.AUTO_ACKNOWLEDGE` 表示 Session 会自动确认所接收到的消息
         *  `Session.CLIENT_ACKNOWLEDGE` 表示由客户端调用消息的 acknowledge() 方法来确认所收到的消息
         *  `Session.DUPS_OK_ACKNOWLEDGE` 使得 Session 将“懒惰”地确认消息，即不会立即确认消息，这样有可能导致消息重复投递。在第二次重新传递消息的时候，消息头的 JmsDelivered 会被置为 true 标示当前消息已经传送过一次，客户端需要进行消息的重复处理控制
 *  消息传送模式
    
     *  默认使用持久化消息 `DeliveryMode.PERSISTENT`，特点：发送的消息会持久化到磁盘，消息不会丢失
     *  非持久化消息 `DeliveryMode.NON_PERSISTENT`，特点：发送的消息只存在内存
 *  消息监听
    
     *  使用 receive() 方法，只能收到一条消息
     *  使用 MessageListener，监听消息中心是否还有消息，如果有的话，实时通知消息消费者获取消息

## 通过 JMS 访问 ActiveMQ ##

``````````java
// 创建连接工厂，参数：user（默认为 null）、password（默认为 null）、url（默认为 "failover://tcp://localhost:61616"） 
  ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(); 
  // 利用连接工厂创建连接 
  Connection connection = factory.createConnection(); 
  // 启动连接 
  connection.start(); 
  
  // 创建 session 
  // 第一个参数表示是否采用事务消息，第二个参数表示消息确认模式 
  // Session session = connection.createSession(true, Session.SESSION_TRANSACTED); 
  Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
``````````

``````````java
// 创建消息发送的目的地 
  // Destination destination = session.createQueue("MessageQueue"); 
  Destination destination = session.createTopic("MessageTopic"); 
  
  // 根据目的地创建消息生产者 
  MessageProducer producer = session.createProducer(destination); 
  // producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); // 设置消息传送模式为非持久 
  
  // 创建消息对象 
  TextMessage message = (TextMessage) session.createTextMessage(); 
  message.setText("hello everyone!"); 
  
  // 通过消息生产者发送消息 
  producer.send(message); 
  
  // 提交事务 
  // session.commit(); 
  // 关闭会话 
  session.close(); 
  // 关闭连接 
  connection.close();
``````````

``````````java
// 创建消息接收的目的地 
  // Destination destination = session.createQueue("MessageQueue"); 
  Destination destination = session.createTopic("MessageTopic"); 
  
  // 根据目的地创建消息消费者 
  MessageConsumer consumer = session.createConsumer(destination); 
  
  while (true) { 
      // 从该消息队列中接收消息 
      // TextMessage textMessage = (TextMessage) consumer.receive(); 
  
      // 或者在该 topic 上注册一个 listener，订阅该 topic 
      consumer.setMessageListener(new MessageListener() { 
          public void onMessage(Message message) { 
              // 获取消息内容 
              TextMessage textMessage = (TextMessage) message; 
              System.out.println(textMessage.getText()); 
              try { 
                  // 提交事务 
                  // session.commit(); 
                  // 手动确认消息 
                  // textMessage.acknowledge(); 
              } catch (JMSException e) { 
                  e.printStackTrace(); 
              } 
          } 
      }); 
  }
``````````

## ActiveMQ 集群 ##

 *  基于 Master-Slave 模式实现的冷备方案：当 Master 启动时，它会获得共享文件系统（或共享数据库某个表）的**排他锁**，而其他 Slave 则 stand-by，不对外提供服务，同时等待获取 Master 的排他锁；假如 Master 连接中断或者发生异常，那么它的排他锁则会立即释放，此时便会有另外一个 Slave 能够争夺到 Master 的排他锁，从而成为 Master，对外提供服务
 *  在 ActiveMQ 的客户端连接的配置中使用 failover 的方式：failover:(tcp://master:61616, tcp://slavel:61616, tcp://slave2:61616)