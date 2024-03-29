# RPC #

 *  RPC（Remote Procedure Call），远程过程调用，通过网络从远程计算机程序上请求服务
 *  服务调用方发送 RPC 请求到服务提供方，服务提供方根据调用方提供的参数执行请求方法，将执行结果返回给调用方，一次 RPC 调用完成
 *  通过**动态代理**实现透明化远程服务调用：代理类的 invoke 方法中封装了**与远端服务通信**的细节
 *  网络传输协议：TCP、HTTP
 *  常见的 RPC 框架：Dubbo、Thrift、gRPC、Finagle

## RPC 调用的流程 ##

 *  服务消费方（client）以本地调用方式调用服务
 *  client stub 接收到调用后负责将接口名称、方法名称、参数等组装成能够进行网络传输的消息体
 *  client stub 找到服务地址，并将序列化后的消息发送到服务端
 *  server stub 收到消息后进行反序列化
 *  server stub 根据反序列化结果调用本地的服务
 *  本地服务执行并将结果返回给 server stub
 *  server stub 将返回结果打包成消息并发送至消费方
 *  client stub 接收到消息，并进行反序列化
 *  服务消费方得到最终结果

## 服务注册中心 ##

 *  服务注册中心的作用：服务端服务的注册和客户端服务的获取；提高系统的可用性；提高系统的可伸缩性；统一管理服务
 *  常用的服务注册中心：Zookeeper、Redis

# Dubbo #

 *  一个分布式服务框架，致力于提供高性能和透明化的 RPC 远程**服务调用**方案，以及 SOA **服务治理**方案
 *  透明化的远程方法调用、软负载均衡及容错机制、服务自动注册与发现
 *  缺省的协议为 **Dubbo 协议**：采用**单一长连接**和 **NIO** 异步通讯，使用 **Hessian** 进行二进制序列化，适合于小数据量大并发的服务调用，以及服务消费者机器数远大于服务提供者机器数的情况
 *  缺省的集群容错模式为 **failover**：失败自动切换，当出现失败，重试其它服务器
 *  默认使用 **Javassist** 动态字节码生成，创建代理类

## Dubbo 的核心内容 ##

 *  RPC 远程调用：封装了长连接 NIO 框架，如 Netty、Mina 等，采用的是多线程模式
 *  集群容错：提供了基于接口方法的远程调用的功能，并实现了负载均衡策略、失败容错等功能
 *  服务发现：集成了 Apache 的 ZooKeeper 组件，用于服务的注册和发现

## Dubbo 的调用关系 ##

 *  服务容器负责启动，加载，运行服务提供者
 *  服务提供者在启动时，向注册中心注册自己提供的服务
 *  服务消费者在启动时，向注册中心订阅自己所需的服务
 *  注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者
 *  服务消费者，从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用
 *  服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414506960.jpeg) 
    图 1 Dubbo的调用关系

## Dubbo 的架构原理 ##

 *  服务消费者程序通过服务接口使用服务，而服务接口通过**代理**加载具体服务，具体服务可以是本地的代码模块，也可以是远程的服务
 *  服务框架**客户端模块**通过服务注册中心加载服务提供者列表（服务提供者启动后自动向服务注册中心注册自己可提供的服务接口列表），查找需要的服务接口，并根据配置的负载均衡策略（默认为**随机调用**）将服务调用请求发送到某台服务提供者服务器
 *  如果服务调用失败，客户端模块会自动从服务提供者列表选择一个可提供同样服务的另一台服务器重新请求服务，实现服务的自动失效转移，保证服务高可用
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414507727.jpeg) 
    图 2 Dubbo的架构原理

## 负载均衡 ##

 *  随机（Random），按权重设置随机概率（缺省）
 *  轮循（RoundRobin），按公约后的权重设置轮循比率（存在慢的提供者累积请求的问题）
 *  最少活跃调用数（LeastActive），相同活跃数的随机，活跃数指调用前后计数差（使慢的提供者收到更少请求）
 *  一致性 Hash（ConsistentHash），相同参数的请求总是发到同一提供者


[Dubbo]: https://static.sitestack.cn/projects/sdky-java-note/946ca3c94cc42473d9fd7319c1be3a38.jpeg
[Dubbo 1]: https://static.sitestack.cn/projects/sdky-java-note/49bf3a2edf3155101868e8432b10f671.jpeg