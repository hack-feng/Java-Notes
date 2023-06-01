# Spring Cloud 介绍 #

 *  微服务是一种架构风格，它提倡将**单体应用划分成一组小的服务**，每个微服务运行在其**独立的进程**中，服务与服务间采用轻量级的通信机制互相协作（通常是基于 HTTP 协议的 RESTful API），这些服务围绕着具体业务进行构建，并且能够通过自动化部署机制来**独立的部署**
 *  优点：技术异构性、弹性、易扩展、易部署 、与组织结构对齐 、可组合性 、可替代性
 *  Spring Cloud offers a simple and accessible programming model to the most common distributed system patterns, helping developers build resilient, reliable, and coordinated applications. Spring Cloud is built on top of Spring Boot, making it easy for developers to get started and become productive quickly.
 *  Spring Cloud 下的 Spring Cloud Netflix 模块，主要封装了 Netflix 的以下项目：Eureka、Hystrix、Ribbon、Zuul
 *  除了 [Spring Cloud Netflix][] 模块外，Spring Cloud 还包括以下几个重要的模块：[Spring Cloud Config][]、[Spring Cloud OpenFeign][]、[Spring Cloud Sleuth][]、[Spring Cloud Stream][]、[Spring Cloud Bus][]、[Spring Cloud Security][] 等
 *  Spring Cloud 主要功能
    
     *  服务发现
     *  服务熔断
     *  配置服务
     *  服务安全
     *  服务网关
     *  分布式消息
     *  分布式跟踪
     *  各种云平台支持
 *  Spring Cloud 的基础依赖

``````````xml
<parent> 
      <groupId>org.springframework.boot</groupId> 
      <artifactId>spring-boot-starter-parent</artifactId> 
      <version>2.1.7.RELEASE</version> 
      <relativePath/> <!-- lookup parent from repository --> 
  </parent> 
  
  <properties> 
      <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding> 
      <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding> 
      <java.version>1.8</java.version> 
      <spring-cloud.version>Greenwich.RELEASE</spring-cloud.version> 
  </properties> 
  
  <dependencyManagement> 
      <dependencies> 
          <dependency> 
              <groupId>org.springframework.cloud</groupId> 
              <artifactId>spring-cloud-dependencies</artifactId> 
              <version>${spring-cloud.version}</version> 
              <type>pom</type> 
              <scope>import</scope> 
          </dependency> 
      </dependencies> 
  </dependencyManagement> 
  
  <!--<build>--> 
      <!--<plugins>--> 
            <!-- SpringBoot 应用打包插件 --> 
          <!--<plugin>--> 
              <!--<groupId>org.springframework.boot</groupId>--> 
              <!--<artifactId>spring-boot-maven-plugin</artifactId>--> 
          <!--</plugin>--> 
      <!--</plugins>--> 
  <!--</build>-->
``````````

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414538335.png) 
图 1 Spring\_Cloud

# 服务注册与发现 Eureka #

 *  将业务组件注册到 Eureka 服务器中，其他客户端组件可以向服务器获取服务并且进行远程调用
 *  作为 Eureka 客户端存在的服务提供者，主要进行以下工作：
    
     *  向服务器注册服务（发送自己的服务信息，包括 IP 地址、端口、ServiceId 等）
     *  发送心跳给服务器（即服务续约，默认每隔 30 秒发送一次心跳来保持其最新状态）
     *  向服务器获取注册列表（客户端同样在内存中保存了注册表信息，因此每次服务的请求都不必经过服务器端的注册中心）

## Eureka 服务器 ##

 *  添加依赖 spring-cloud-starter-netflix-eureka-server（会自动引入 spring-boot-starter-web，具有 Web 容器的功能）
 *  添加配置

``````````yml
server.port: 8761 # 声明服务器的 HTTP 端口 
  eureka: 
    client: 
      registerWithEureka: false # 是否将自己向自己或别的 Eureka Server 进行注册，默认值为 true 
      fetchRegistry: false # 是否到 Eureka 服务器中获取注册信息，默认值为 true
``````````

 *  在启动类上添加 @EnableEurekaServer，声明该应用是一个 Eureka 服务器
 *  访问 http://localhost:8761

## Eureka 客户端（服务提供者、服务调用者） ##

 *  添加依赖 spring-cloud-starter-netflix-eureka-client
 *  添加配置

``````````yml
server.port: 9000 
  spring.application.name: xxx 
  eureka: 
    # 实例信息配置 
    instance: 
      instanceId: ${spring.cloud.client.ipAddress}:${spring.application.name}:${server.port} # 注册到 Eureka 的实例 id 
      hostname: localhost # 主机名，不配置的时候将根据操作系统的主机名来获取 
      preferIpAddress: true # 优先使用 IP 地址方式进行注册服务 
  #    appname: ${spring.application.name} # 服务名，默认取 spring.application.name 的配置值，如果没有则为 unknown 
    # 客户端信息配置 
    client: 
      serviceUrl.defaultZone: http://localhost:8761/eureka/ # 服务注册地址
``````````

 *  在启动类上添加 @EnableEurekaClient，声明该应用是一个 Eureka 客户端（该注解已经包含 @EnableDiscoveryClient，即具有发现服务的能力）（Greenwich 版不需要）
 *  服务调用者使用 RestTemplate 根据服务提供者的**应用名称**来进行调用其发布的 REST 服务
 *  获得服务地址：注入 EurekaClient、DiscoveryClient

## Eureka 集群搭建 ##

 *  Eureka 服务器之间需要互相注册
 *  Eureka 服务器配置文件

``````````yml
server.port: 8761 
  spring: 
    profiles: discovery1 
    application.name: cloud-server 
  eureka: 
    instance: 
      hostname: discovery1 
      instanceId: ${spring.cloud.client.ipAddress}:${spring.application.name}:${server.port} 
    client: 
      serviceUrl.defaultZone: http://discovery2:8762/eureka/ 
  --- 
  server.port: 8762 
  spring: 
    profiles: discovery2 
    application.name: cloud-server 
  eureka: 
    instance: 
      hostname: discovery2 
      instanceId: ${spring.cloud.client.ipAddress}:${spring.application.name}:${server.port} 
    client: 
      serviceUrl.defaultZone: http://discovery1:8761/eureka/
``````````

 *  修改 Eureka 客户端的配置文件，defaultZone 改为 `http://discovery1:8761/eureka/`，将服务注册到 discovery1 上，discovery1 的注册列表信息会自动同步到 discovery2 节点

## Eureka 的自我保护模式 ##

 *  默认情况下，如果 Eureka Server 在一定时间内（默认 90 秒）没有接收到某个微服务实例的心跳，Eureka Server 将会移除该实例（失效剔除）
 *  如果在 15 分钟内 Eureka Serve 接收到的心跳低于 85%，那么 Eureka 就认为客户端与注册中心出现了**网络故障**，而微服务本身是正常运行的，此时进入自我保护机制：1、Eureka Server 不再从注册列表中移除因为长时间没收到心跳而应该过期的服务2、Eureka Server 仍然能够接受新服务的注册和查询请求，但是不会被同步到其它节点上，保证当前节点依然可用3、当网络稳定时当前 Eureka Server 新的注册信息会被同步到其它节点中

 *  Renews threshold：Eureka Server 期望在每分钟中收到的心跳次数：threshold 初始值为 1，client 个数为 n，threshold = 1+2\*n（此为禁止自注册的情况，这里的乘以 2 是因为默认每分钟发两次心跳）
 *  Renews (last min)：上一分钟内收到的心跳次数：renews = 2\*n
 *  当 renews/threshold < 0.85 `eureka.server.renewalPercentThreshold=0.85` 时，就会进入自我保护机制`eureka.server.enableSelfPreservation=true`

## Eureka Rest 接口 ##

 *  获取所有注册信息：GET /eureka/apps
 *  获取应用信息：GET /eureka/apps/\{appname\}
 *  服务注册：POST /eureka/\{appname\}
 *  心跳续约： PUT /eureka/apps/\{appname\}/\{instanceId\}?status=UP
 *  服务下线：DELETE /eureka/apps/\{appname\}/\{instanceId\}
 *  强制修改健康状态（overriddenstatus）：PUT /eureka/apps/\{appname\}/\{instanceId\}?status=\{status\} （status 的值：UP; DOWN; STARTING; **OUT\_OF\_SERVICE**; UNKNOWN）（通过手动或使用 Asgard 等管理工具设置 OUT\_OF\_SERVICE 暂时禁止某些实例的流量，可实现红黑部署）
 *  删除修改的健康状态（让 Eureka 服务器开始遵守实例本身发布的状态）：DELETE /eureka/apps/\{appname\}/\{instanceId\}/status

## 信息监控与健康检查 ##

 *  在 Eureka 客户端的项目中添加依赖 spring-boot-starter-actuator
 *  信息监控：修改配置文件，添加 info 信息；访问 Eureka客户端的 /info 端点

``````````yml
info: 
    app.name: ${spring.application.name} 
    company.name: www.example.com 
    build.artifactId: @project.artifactId@ 
    build.version: @project.version@
``````````

 *  健康检查：修改配置文件，添加健康状态检查配置；访问 Eureka客户端的 /health 端点（可通过 EurekaClient\#registerHealthCheck() API 插入自定义的 HealthCheckHandlers）

``````````yml
eureka.client.healthcheck.enabled: true
``````````

## 服务注册与发现机制 ##

 *  spring-cloud-commons
 *  服务注册抽象：ServiceRegistry
 *  客户发现抽象：DiscoveryClient、LoadBalancerClient
    

## 其它服务注册中心 ##

 *  [ZooKeeper][]：spring-cloud-starter-zookeeper-discovery
 *  [Consul][]：spring-cloud-starter-consul-discovery
 *  [Nacos][]

# 负载均衡器 Ribbon #

 *  常见的负载均衡方式：
    
     *  集中式 LB，**独立进程单元**，通过负载均衡策略，将请求转发到不同的执行单元上，如 F5、Nginx
     *  进程内 LB，将负载均衡逻辑以代码的形式封装到**服务消费者的客户端**上（服务消费者客户端维护了一份服务提供者的信息列表），如 Ribbon

## Ribbon 介绍 ##

 *  Ribbon 是 Netflix 下的负载均衡项目，提供以下特性：
    
     *  负载均衡器，可支持插拔式的负载均衡规则
     *  对多种协议提供支持，例如 HTTP、TCP、UDP
     *  集成了负载均衡功能的客户端
 *  Ribbon 的主要子模块：
    
     *  ribbon-core：Ribbon 的核心 API
     *  ribbon-loadbalancer：可以独立使用或与其他模块一起使用的负载均衡器 API
     *  ribbon-eureka: Ribbon 结合 Eureka 客户端的 API，为负载均衡器提供动态服务注册列表信息
 *  Ribbon 的负载均衡器的三大子模块
    
     *  Rule：负载均衡策略，决定从 server 列表中返回哪个 server 实例（由 IRule 接口的 choose 方法来决定）
     *  Ping：心跳检测，该组件主要使用定时器每隔一段时间会去 Ping server ，判断 server 是否存活（IPing 接口）
     *  ServerList：服务列表，可以通过静态的配置确定负载的 server ，也可以动态指定 server 列表（如果动态指定 server 列表，则会有后台的线程来刷新该列表）
 *  Ribbon 自带的负载规则
    
     *  RoundRobinRule：默认的规则，通过简单的轮询服务列表来选择 server
     *  AvailabilityFilteringRule：过滤掉由于多次访问故障而处于“短路”状态的 server 以及并发的连接数量超过阈值的 server ，再对剩余的 server 列表按照轮询规则选择
     *  WeightedResponseTimeRule：根据响应时间为每个 server 分配一个权重值，响应时间越短，权重值越大，被选中的可能性越高（当刚开始运行权重没有初始化时使用轮询规则选择）
     *  ZoneAvoidanceRule：使用 Zone 对 server 进行分类，复合判断 server 所在区域的运行性能和 server 的可用性选择 server
     *  BestAvailableRule：忽略“短路”的 server ，再选择并发数较低的 server
     *  RandomRule：随机选择可用的 server
     *  RetryRule：按照轮询规则选择 server ，如果选择的 server 无法连接则在指定时间内进行重试，再重新选择 server

## 在 Spring Cloud 中使用 Ribbon ##

 *  在 Spring 容器启动时，会为被修饰过的 RestTemplate 添加拦截器 LoadBalancerInterceptor，在拦截器的方法中，将远程调用方法交给了 Ribbon 的负载均衡器 LoadBalancerClient 去处理（选择 server，**将原来请求的 URI 进行改写**）
 *  在**服务调用者**的项目中添加依赖 spring-cloud-starter-netflix-ribbon
 *  配置 Ribbon，指定使用的负载规则、Ping 类或者 server 列表
    
     *  使用代码配置 Ribbon

``````````java
@Configuration 
  public class MyConfig { 
      @Bean 
      public IRule getRule() { 
          // return new RandomRule(); 
          return new MyRule(); 
      } 
      @Bean 
      public IPing getPing() { 
          return new MyPing(); 
      } 
  } 
  
  @RibbonClient(name="cloud-provider", configuration=MyConfig.class) 
  public class CloudProvider { 
  }
``````````

 *  使用配置文件设置 Ribbon，修改服务调用者配置文件

``````````yml
ribbon: 
  #  ConnectionTimeout: 1000ms # 连接超时时间 
  #  ReadTimeout: 1000ms # 读取超时时间 
  #  ServerListRefreshInterval: 30s # 刷新服务列表源的间隔时间 
  #  NFLoadBalancerRuleClassName: com.example.cloud.MyRule 
  #  NFLoadBalancerPingClassName: com.example.cloud.MyPing 
  #  listOfServers: http://localhost:8080/, http://localhost:8081/
``````````

 *  在程序的 IoC 容器中注入一个 restTemplate 的 Bean，并在这个 Bean 上加上 @LoadBalanced 注解

``````````java
@Configuration 
  public class RibbonConfig { 
      @Bean 
      @LoadBalanced 
      RestTemplate restTemplate() { 
          return new RestTemplate(); 
      } 
  }
``````````

# 声明式调用 Feign #

 *  在使用 Feign 时，可以使用注解来修饰接口，被注解修饰的接口具有访问 Web Service 的能力
 *  Feign 使用的是 JDK 的动态代理，生成的代理类会将请求的信息封装，交给 feign.Client 接口发送请求，该接口的默认实现类 Client.Default 最终会使用 java.net.HttpURLConnection 来发送 HTTP 请求（也可以通过添加依赖使用其它 HttpClient）
 *  通过将 `feign.okhttp.enabled` 或 `feign.httpclient.enabled` 分别设置为 true，并将它们放在类路径上来使用 OkHttpClient 和 ApacheHttpClient feign 客户端
 *  通过在使用 Apache 时提供 ClosableHttpClient 或在使用 OK HTTP 时提供 OkHttpClient 的 bean 来定制使用的 HTTP 客户端
 *  使用 SpringEncoder、ResponseEntityDecoder 序列化和反序列化（FeignClientsConfiguration）

## 在 Spring Cloud 中使用 Feign ##

### 整合 Feign ###

 *  在服务调用者的项目中添加依赖 spring-cloud-starter-openfeign
 *  在服务调用者的启动类上添加 @EnableFeignClients，打开 Feign 开关
 *  编写客户端**接口**，由于 Spring Cloud 提供了注解翻译器（Contract），使得 Feign 可以解析 @RequestMapping、@RequestParam、@RequestHeader、@PathVariable 注解的含义，因此在服务接口上可以直接使用这些注解

``````````java
@FeignClient(name = "spring-server-provider", contextId = "store", path = "store") // 声明需要调用的服务名称 
  public interface StoreClient { 
      @RequestMapping(method = RequestMethod.GET, value = "/stores") 
      List<Store> getStores(); 
  
      @RequestMapping(method = RequestMethod.POST, value = "/stores/{storeId}", consumes = "application/json") 
      Store update(@PathVariable("storeId") Long storeId, Store store); 
  }
``````````

 *  注入编写的客户端接口调用方法

``````````java
@Autowired 
  private StoreClient storeClient; 
  
  storeClient.getStores();
``````````

## 定制 Feign ##

 *  通过配置定制 Feign

``````````yml
feign: 
    client: 
      config: 
        feignName: 
          connectTimeout: 5000 
          readTimeout: 5000 
          loggerLevel: full 
          errorDecoder: com.example.SimpleErrorDecoder 
          retryer: com.example.SimpleRetryer 
          requestInterceptors: 
            - com.example.FooRequestInterceptor 
            - com.example.BarRequestInterceptor 
          decode404: false 
          encoder: com.example.SimpleEncoder 
          decoder: com.example.SimpleDecoder 
          contract: com.example.SimpleContract
``````````

### Feign 负载均衡 ###

 *  Feign 的 stater 依赖已包含 Ribbon，所提供的 feign.Client 的实现类为 **LoadBalancerFeignClient**，在该类中，维护着与 SpringClientFactory 相关的实例，通过 SpringClientFactory 可以获取负载均衡器，负载均衡器会根据一定的规则来选取处理请求的服务器，最终实现负载均衡的功能

# 熔断器 Hystrix #

 *  在分布式环境中，总会有一些被依赖的服务会失效，Hystrix 通过隔离服务间的访问点、停止它们之间的级联故障、提供可回退操作来实现容错，使得不会因为单点故障而降低整个集群的可用性
 *  当某基础服务模块不可用时，服务调用者将对其进行“熔断”，在一定的时间内，服务调用者都不会再调用该基础服务，以维持本身的稳定
 *  如果客户端没有容错机制，客户端将一直等待服务端返回，直到网络超时或者服务有响应，而外界会一直不停地发送请求给客户端，最终导致**客户端因请求过多而瘫痪**
 *  雪崩效应：如果一个服务出现了故障或者是网络延迟，在高并发的情况下，会导致线程阻塞，在很短的时间内该服务的线程资源会消耗殆尽，使得该服务不可用。在分布式系统中，由于服务之间的相互依赖，最终可能会导致整个系统的不可用。
 *  基本的容错模式
    
     *  超时：主动超时
     *  限流：限制最大并发数（RateLimiter）
     *  熔断：调用失败触发阈值后，后续调用直接由断路器返回错误，不再执行实际调用（**断路器模式**，CircuitBreaker）
     *  隔离：隔离不同的依赖调用（**舱壁隔离模式**，Bulkhead）
     *  降级：服务降级

## Hystrix 介绍 ##

 *  Hystrix 的主要功能：
    
     *  防止单个服务的故障耗尽整个服务的 Servlet 容器的线程资源
     *  **快速失败**机制，如果某个服务出现了故障，则调用该服务的请求快速失败，而不是线程等待
     *  提供回退（fallback）方案，在请求发生故障时，提供设定好的回退方案
     *  使用熔断机制，防止故障扩散到其他服务
     *  提供熔断器的监控组件 Hystrix Dashboard，可以实时监控熔断器的状态
 *  Hystrix 的运作流程
    
     *  在命令开始执行时，会做一些准备工作，例如**为命令创建相应的线程池**等
     *  判断是否打开了缓存，打开了缓存就直接查找缓存并返回结果
     *  判断断路器是否打开，如果打开了，就表示链路不可用，直接执行回退方法
     *  判断线程池、信号量（计数器）等条件，例如像线程池超负荷，则执行回退方法，否则，就去执行命令的内容
     *  执行命令，计算是否要对断路器进行处理，执行完成后，如果满足一定条件，则需要开启断路器。如果执行成功，则返回结果，反之则执行回退
         ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414539038.png) 
        图 2 hystrix-command-flow-chart

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414539953.png) 
图 3 Hystrix的运作流程图

 *  触发回退的情况：1. 断路器被打开；2. 线程池、队列或信号量满载；3. 实际执行命令失败
 *  回退的模式：在 A 命令的回退方法中执行 B 命令，如果 B 命令也执行失败，同样也会触发 B 命令的回退，形成一种**链式**的命令执行
 *  断路器开启要满足的两个条件：
    
     *  整个链路达到一定阈值，默认情况下，10 秒内产生超过 20 次请求，则符合第一个条件
     *  满足第一个条件的情况下，如果请求的错误百分比大于阈值，则会打开断路器，默认为 50%
 *  断路器关闭
    
     *  断路器打开后，在一段时间内，命令不会再执行（一直触发回退），这段时间称作“休眠期”
     *  休眠期的默认值为 5 秒，休眠期结束后，Hystrix 会尝试性地执行一次命令，此时断路器的状态不是开启，也不是关闭，而是一个半开的状态，如果这一次命令执行成功，则会关闭断路器并清空链路的健康信息；如果执行失败，断路器会继续保持打开的状态
 *  隔离策略
    
     *  THREAD：默认值，由**线程池**来决定命令的执行，如线程池满载，则不会执行命令（Hystrix 使用了 ThreadPoolExecutor 来控制线程池行为，线程池的默认大小为 10）；适用场景：不受信服务、有限扇出
     *  SEMAPHORE：由**信号量**来决定命令的执行，当请求的并发数高于阈值时，就不再执行命令（相对于线程策略，信号量策略开销更小，但是该策略不支持超时以及异步，除非对调用的服务有足够的信任，否则不建议使用该策略进行隔离）；适用场景：受信服务、高扇出（网关）、高频高速调用（cache）
 *  合并请求：在一次请求的过程中，可以将一个时间段内的相同请求（URL 相同，参数不同），收集到同一个命令中执行，这样就节省了线程的开销，减少了网络连接，从而提升执行的性能
 *  请求缓存：如果在一次请求的过程中，多个地方调用同一个接口，可以使用缓存

## Hystrix 主要配置项 ##

| 配置项（前缀 hystrix.command.*.）                    | 含义                                                |
| ---------------------------------------------------- | --------------------------------------------------- |
| execution.isolation.strategy                         | 线程“THREAD”或信号量“SEMAPHORE”隔离（默认：THREAD） |
| **execution.isolation.thread.timeoutInMilliseconds** | run() 方法执行超时时间（默认：**1000**）            |
| execution.timeout.enabled                            | run() 方法执行是否开启超时（默认：true）            |
| execution.isolation.semaphore.maxConcurrentRequests  | 信号量隔离最大并发数（默认：10）                    |
| circuitBreaker.errorThresholdPercentage              | 熔断的错误百分比阀值（默认：50）                    |
| circuitBreaker.requestVolumeThreshold                | 断路器生效必须满足的流量阀值（默认：20）            |
| circuitBreaker.sleepWindowInMilliseconds             | 熔断后重置断路器的时间间隔（默认：5000）            |
| circuitBreaker.forceOpen                             | 设 true 表示强制熔断器进入打开状态（默认：false）   |
| circuitBreaker.forceClosed                           | 设 true 表示强制熔断器进入关闭状态（默认：false）   |

| 配置项（前缀 hystrix.threadpool.*.） | 含义                                                         |
| ------------------------------------ | ------------------------------------------------------------ |
| coreSize                             | 使用线程池时的最大并发请求（默认：10），建议：QPS * TP99 + 冗余线程 |
| maxQueueSize                         | 最大 LinkedBlockingQueue 大小，-1 表示用 SynchronousQueue（默认： -1) |
| default.queueSizeRejectionThreshold  | 队列大小阀值，超过则拒绝（默认：5）                          |

## 在 Spring Cloud 中使用 Hystrix ##

### 在需受保护的方法上使用 Hystrix ###

 *  在服务调用者的项目中添加依赖 spring-cloud-starter-netflix-hystrix
 *  在服务调用者的启动类上添加 @EnableHystrix 或 @EnableCircuitBreaker，启用断路器
 *  在服务**方法**上添加 @HystrixConanand(fallbackMethod = "xxxx")，并配置回退方法等属性
    
     *  被 @HystrixCommand 修饰的方法，Hystrix(javanica) 会使用 AspectJ 对其进行**代理**，Spring 会将相关的代理类转换为 Bean 放到容器中
     *  @HystrixConanand 属性 fallbackMethod、groupKey、commandKey、threadPoolKey

``````````java
@Component 
  public class StoreIntegration { 
  
    @HystrixCommand(fallbackMethod = "defaultStores") 
    public Object getStores(Map<String, Object> parameters) { 
        //do stuff that might fail 
    } 
  
    public Object defaultStores(Map<String, Object> parameters) { 
        return /* something useful */; 
    } 
  }
``````````

### 在 Feign 上使用 Hystrix ###

 *  在服务调用者的项目中添加依赖 spring-cloud-starter-netflix-hystrix（已包含 Hystrix）
 *  在配置文件中打开 Feign 的 Hystrix 开关 `feign.hystrix.enabled=true`
 *  在 Feign 客户端接口的 @FeignClient 注解中添加 fallback 属性，设置处理回退的类（回退类需要实现该接口，并且需要以 Bean 的方式注入 IoC 容器，即添加 @Component）
 *  hystrix.command.@FeignClient注解的name属性值.circuitBreaker.requestVolumeThreshold：针对全局设置默认时间段内发生的请求数
 *  hystrix.command.Feign客户端接口名\#方法名().circuitBreaker.requestVolumeThreshold：针对某个客户端默认时间段内发生的请求数
 *  hystrix.command.@FeignClient注解的name属性值.execution.isolation.thread.timeoutlnMilliseconds：针对全局设置超时时间
 *  hystrix.command.Feign客户端接口名\#方法名().execution.isolation.thread.timeoutlnMilliseconds：针对某个客户端设置超时时间

### 缓存注解、合并请求注解 ###

 *  合并请求、请求缓存，在一次请求的过程中才能实现，因此需要**先初始化请求上下文**

``````````java
@WebFilter(urlPatterns = "/*", filterName = "hystrixFilter") // 需要在启动类上添加 @ServletComponentScan 
  public class HystrixFilter implements Filter { 
      public void init(FilterConfig filterConfig) throws ServletException { 
      } 
  
      public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException { 
          // 创建 Hystrix 的请求上下文 
          HystrixRequestContext context = HystrixRequestContext.initializeContext(); 
          try { 
              chain.doFilter(request, response); 
          } finally { 
              // 销毁 Hystrix 的请求上下文 
              context.shutdown(); 
          } 
      } 
  
      public void destroy() { 
      } 
  }
``````````

 *  请求缓存相关注解
    
     *  @CacheResult：用于修饰方法，表示被修饰的方法返回的结果将会被缓存，需要与 @HystrixCommand 一起使用
     *  @CacheRemove：用于修饰方法让缓存失效，需要与 @CacheResult 的缓存 key 关联
     *  @CacheKey：用于修饰方法参数，表示该参数作为缓存的 
~~~java
     @CacheResult@HystrixCommand(commandKey = "cacheKey")
     public String cacheMethod(String name) { 
     	return "hello";
     }
     @CacheRemove(commandKey = "cacheKey")
     @HystrixCommand
     public String updateMethod(String name) {
		return "update";
	}
~~~


 *  合并请求相关注解：@HystrixCollapser

``````````java
@Component 
  public class CollapseService { 
      // 配置收集 1 秒内的请求 
      @HystrixCollapser(batchMethod = "getPersons", collapserProperties ={@HystrixProperty(name = "timerDelayInMilliseconds", value = "1000")}) 
      public Future<Person> getSinglePerson(Integer id) { 
          System.out.println("执行单个获取的方法"); 
          return null; 
      } 
  
      @HystrixCommand 
      public List<Person> getPersons(List<Integer> ids) { 
          System.out.println("收集请求，参数数量：" + ids.size()); 
          List<Person> ps = new ArrayList<Person>(); 
          for (Integer id : ids) { 
              Person p = new Person(); 
              p.setId(id); 
              p.setName("crazyit"); 
              ps.add(p); 
          } 
          return ps; 
      } 
  }
``````````

## 监控熔断器状态 ##

### 使用 Hystrix Dashboard 监控熔断器状态 ###

 *  在服务调用者的项目中添加依赖 spring-boot-starter-actuator
 *  添加配置 `management.endpoints.web.exposure.include: hystrix.stream`
 *  访问 /actuator/hystrix.stream 端点，可以看到 Hystrix 输出的 stream 数据
 *  新建监控的 Web 项目 hystrix-dashboard
    
     *  添加依赖 spring-cloud-starter-netflix-hystrix-dashboard
     *  在启动类中添加 @EnableHystrixDashboard，开启 Hystrix 控制台
     *  访问 http://hostname:port/hystrix，在文本框中输入需要监控的 Hystrix 流
         ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414540507.png) 
        图 4 Hystrix\_Dashboard中各种数据指标的含义

### 使用 Turbine 集群监控 ###

 *  Turbine 用于聚合多个 Hystrix 流，将多个 Hystrix Dashboard 组件的数据放在一个页面上展示，进行集中监控
 *  在服务调用者的项目中添加依赖 spring-boot-starter-actuator
 *  新建监控的 Web 项目 turbine-monitor
    
     *  添加依赖 spring-cloud-starter-netflix-turbine（已包含 Eureka Client）
     *  添加配置

``````````
spring: 
    application.name: turbine-monitor 
  server.port: 8769 
  security.basic.enabled: false 
  turbine: 
    aggregator.clusterConfig: default   # 指定聚合哪些集群，多个使用“,”分割，默认为 default，可使用 http://.../turbine.stream?cluster={clusterConfig 之一} 访问 
    appConfig: eureka-ribbon-client, eureka-feign-client  # 配置 Eureka 中的 serviceId 列表，表明监控哪些服务 
    clusterNameExpression: new String("default") 
    # 1. clusterNameExpression 指定集群名称，默认表达式 appName；此时：turbine.aggregator.clusterConfig 需要配置想要监控的应用名称 
    # 2. 当 clusterNameExpression: default 时，turbine.aggregator.clusterConfig 可以不写，因为默认就是 default 
    # 3. 当 clusterNameExpression: metadata['cluster']时，假设想要监控的应用配置了 eureka.instance.metadata-map.cluster: ABC，则需要配置，同时 turbine.aggregator.clusterConfig: ABC 
  eureka.client.serviceUrl.defaultZone: http://localhost:8761/eureka/
``````````

 *  在启动类中添加 @EnableTurbine
 *  访问 http://localhost:8769/hystrix，在文本框中输入需要要监控 Turbine 流 http://localhost:8769/turbine.stream

## Resilience4j ##

# 路由网关 Zuul #

## Zuul 介绍 ##

 *  为微服务集群提供代理、过滤、路由等功能
 *  Zuul 的核心是一系列过滤器，可以在 Http 请求的发起和响应返回期间执行一系列的过滤器
 *  Zuul 包括以下 4 种过滤器：
    
     *  pre 过滤器：在请求路由到具体的服务之前执行（可以做安全验证，例如身份验证、参数验证等）
     *  routing 过滤器：用于将请求路由到具体的微服务实例（默认使用 http client 进行网络请求）
     *  post 过滤器：在请求已被路由到微服务后执行的（一般用作收集统计信息、指标，以及将响应传输回客户端）
     *  error 过滤器：在其它过滤器发生错误时执行
 *  每个请求都会创建一个 RequestContext 对象，过滤器之间过 **RequestContext** 对象来共享数据
 *  注意：Spring Cloud Zuul 去掉了过滤器动态加载
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414541166.png) 
    图 5 Zuul网关架构

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414541790.jpeg) 
图 6 Zuul\_1.x请求的生命周期

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414542334.png) 
图 7 Zuul\_2请求的生命周期

 *  Zuul 1.x 采用的是同步阻塞模型，适用计算密集型（CPU bound）场景（可使用异步 AsyncServlet 优化连接数）
 *  Zuul 2.x 采用的是异步非阻塞模型，基于 Netty 处理请求，适用 IO 密集型（IO bound）场景
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414542870.png) 
    图 8 Zuul编程模型

## 在 Spring Cloud 中使用 Zuul ##

 *  在 zuul-gateway 项目中添加依赖 spring-cloud-starter-netflix-zuul
 *  添加配置

``````````yml
spring.application.name: zuul-gateway 
  eureka: 
    instance.hostname: localhost 
    client.serviceUrl.defaultZone: http://localhost:8761/eureka/ # 网关作为 Eureka 客户端注册到 Eureka 服务器 
  zuul: 
    routes: 
      xxx: # routeld 
        path: /xxx/** # 默认情况下使用 routeld 作为 path 
        serviceId: xxx-service # Ribbon 路由，声明所有的 /xxx/** 请求将会被转发到 Id 为 xxx-service 的服务进行处理，默认情况下使用 routeld 作为 serviceld 
  #      url: https://www.baidu.com # 简单路由（以 http: 或 https: 开头） 
  #      url: forward:/yyy/hello # 跳转路由（以 forward: 开头），当外部访问网关的 A 地址时，会跳转到 B 地址 
    ribbon.eager-load.enabled: true # 预加载 Ribbon，默认为 false（在第一次调用集群服务时才初始化 Ribbon 的客户端） 
  #  prefix: /v1 # 给每一个服务的 API 接口加前缀 /v1
``````````

 *  在启动类中添加 @EnableZuulProxy 注解，开启对 Zuul 的支持

## 路由配置 ##

 *  简单路由：将 HTTP 请求全部转发到“源服务”（HTTP 服务），处理跳转路由的过滤器为 SimpleHostRoutingFilter
 *  跳转路由：当外部访问网关的 A 地址时，会跳转到 B 地址，处理跳转路由的过滤器为 SendForwardFilter
 *  Ribbon 路由：当网关作为 Eureka 客户端注册到 Eureka 服务器时，可以通过配置 serviceld 将请求转发到集群的服务中
    
     *  对应的过滤器为 RibbonRoutingFilter 的过滤器，该过滤器会调用 Ribbon 的 API 来实现负载均衡，默认情况下用 HttpClient 来转发请求
     *  为了保证转发的性能，使用了 HttpClient 的连接池功能，可以修改 HttpClient 连接池的属性
        
         *  zuul.host.maxTotalConnections：目标主机的最大连接数，默认值为 200
         *  zuul.host.maxPerRouteConnections：每个主机的初始连接数，默认值为 20
 *  自定义的路由规则
 *  忽略路由：使用 zuul.ignoredServices 设置排除的服务，使用 zuul.ignoredPattems 设置不进行路由的 URL

## 请求头配置 ##

``````````yml
zuul: 
    sensitiveHeaders: cookie # 在默认情况下 HTTP 请求头的 Cookie、Set-Cookie、Authorization 属性不会传递到”源服务“ 
    ignoredHeaders: accept-language # 忽略请求与响应中该头信息
``````````

## 路由端点 ##

 *  在网关项目中提供了一个 /routes 服务，可以访问该端点査看路由映射信息
 *  需要手动开启该服务
    
     *  在 zuul-gateway 项目中添加依赖 spring-boot-starter-actuator
     *  在配置文件中将 management. security.enabled 属性值设置为 false，关闭安全认证
     *  在启动类中添加 @EnableZuulProxy 注解
     *  访问 http://localhost:8080/routes

## 在 Zuul 上配置熔断器 ##

 *  RibbonRoutingFilter 在进行转发时会封装为一个 Hystrix 命令予以执行，即具有容错的功能
 *  在 zuul-gateway 项目中建立一个网关处理类实现 ZuulFallbackProvider 的接口，并使用 @Component 注入 IoC 容器
 *  在网关处理类的 getRoute() 方法指定熔断功能应用于哪些路由的服务（返回“\*”表示所有的路由服务都加熔断功能）
 *  在网关处理类的 fallbackResponse() 方法处理回退逻辑

## 自定义的过滤器 ##

 *  类型 Type：定义在路由流程中，过滤器被应用的阶段
 *  执行顺序 Execution Order：在同一个 Type 中，定义过滤器执行的顺序
 *  条件 Criteria：过滤器被执行必须满足的条件
 *  动作 Action：如果条件满足，过滤器中将被执行的动作

``````````java
@Component 
  public class MyFilter extends ZuulFilter { 
      private static Logger log = LoggerFactory.getLogger(MyFilter.class); 
  
      @Override 
      public String filterType() { 
          // 过滤器的类型，可以为 "pre", "post", "routing", "error" 
          return PRE_TYPE; 
      } 
  
      @Override 
      public int filterOrder() { 
          // 过滤顺序，值越小，越早执行该过滤器 
          return 0; 
      } 
  
      @Override 
      public boolean shouldFilter() { 
          // 是否过滤逻辑，如果为 true，则执行 run() 方法；如果为 false，则不执行 run() 方法 
          return true; 
      } 
  
      @Override 
      public Object run() { 
          RequestContext ctx = RequestContext.getCurrentContext(); 
          HttpServletRequest request = ctx.getRequest(); 
          log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString())); 
          Object accessToken = request.getParameter("token"); 
          // 检查请求的参数中是否传了 token 这个参数 
          // 如果没有传，则请求不被路由到具体的服务实例，直接返回响应，状态码为 401 
          if (accessToken == null) { 
              log.warn("token is empty"); 
              ctx.setSendZuulResponse(false); 
              ctx.setResponseStatusCode(401); 
              try { 
                  ctx.getResponse().getWriter().write("token is empty"); 
              } catch (Exception e) { 
              } 
              return null; 
          } 
          log.info("ok"); 
          return null; 
      } 
  }
``````````

# 配置中心 Spring Cloud Config #

 *  支持的后端存储 ：Git（默认）、SVN、本地文件系统、Vault、JDBC 等

## 引导程序 ##

 *  Spring Boot 程序在进行容器初始化时会先建立一个“引导上下文”（BootstrapContext），再创建主应用的上下文
 *  引导上下文会读取 bootstrap.yml（或 .properties）文件，主应用程序上下文通常读取的是 application.yml（或 .properties）文件
 *  因为 application.yml 的配置会在 bootstrap. yml 后加载，所以如果两份配置文件同时存在，且存在 key 相同的配置，则 application.yml 的配置会**覆盖** bootstrap. yml 的配置
 *  **配置客户端**（ConfigClient）的引导程序在进行引导上下文创建时，会去**读取外部的属性**，并且会进行属性解密等工作

## Spring Cloud Config 的使用 ##

### 配置服务器 ###

 *  在 config-server 项目中添加依赖 spring-cloud-config-server（可同时添加服务发现依赖及相关配置）
 *  添加配置
    
     *  spring-cloud-config-server 项目提供了 4 种配置，可以通过设置 spring.profiles.active 不同的值来激活
        
         *  git：默认值，表示去 Git 仓库读取配置文件
         *  subversion：表示去 SVN 仓库读取配置文件
         *  native：将去本地的文件系统中读取配置文件
         *  vault：去 Vault 中读取配置文件，Vault 是一款资源控制工具，可对资源实现安全访问

``````````yml
server.port: 8888 
  spring: 
   application.name: config-server 
   # 从 Git 仓库读取配置文件 
   profiles.active: git 
   cloud.config.server.git: 
     uri: https://github.com/abc/efg/ # 配置 Git 仓库的地址 
     search-paths: config-repo # 搜索仓库的文件夹地址，可以配置多个，用“,”分割 
     username:    # Git 仓库的账号（公开的 Git 仓库不需要） 
     password:    # Git 仓库的密码（公开的 Git 仓库不需要）
``````````

``````````yml
server.port: 8888 
  spring: 
   application.name: config-server 
   # 从本地读取配置文件 
   profiles.active: native 
   # 读取配置的路径为 classpath 下的 config 目录 
   cloud.config.server.native.search-locations: classpath:/config
``````````

 *  在启动类中添加 @EnableConfigServer 注解，开启 Config Server
 *  访问 http://localhost:8888/文件名，访问规则/\{application\}/\{profile\}\[/\{label\}\]/\{application\}-\{profile\}.yml/\{label\}/\{application\}-\{profile\}.yml/\{application\}-\{profile\}.properties/\{label\}/\{application\}-\{profile\}.properties

### 配置客户端 ###

 *  在客户端项目中添加依赖 spring-cloud-starter-config
 *  由于客户端要在引导程序中读取配置服务器的配置，配置文件名为 **bootstrap**.yml（或 .properties）

``````````yml
spring.cloud.config: 
    uri: http://localhost:8888/ 
    # 读取的是 master 分支  config-repo  目录下的 neo-config-dev.yml（或. properties） 
    label: master 
    name: neo-config # 默认值为 spring.application.name 的值 
    profile: dev 
    fail-fast: true|retry
``````````

 *  可同时添加服务发现依赖及相关配置，发现配置中心，再添加配置

``````````yml
spring.cloud.config.discovery: 
    enabled: true 
    service-id: config-server
``````````

## 刷新配置 ##

 *  版本控制系统的配置文件修改后，需要通知客户端来改变配置
 *  使用 HTTP 的 POST 方法访问**客户端**的 **/refresh 端点**进行刷新
 *  客户端的 refresh 服务在接收到请求后，会重新到配置服务器获取最新的配置
 *  此时，容器中使用了 **@RefreshScope** 注解进行修饰的 Bean 都会在缓存中进行**销毁** ContextRefresher\#refresh，当这些 Bean 被再次引用时，就会创建新的实例，以此达到一个”刷新“的效果
 *  可以与 Spring Cloud Bus 整合实现集群全部配置的刷新（向消息中间件（RabbitMQ 等）发送一个 Post 请求，通过消息组件通知所有的节点更新配置）—— RefreshRemoteApplicationEvent

## 配置的加密和解密 ##

 *  Spring Cloud Config 为配置文件中的敏感数据提供了加密和解密的功能，加密后的密文在传输给客户端**前**会进行解密
 *  配置服务器支持对称加密和非对称加密，对称加密使用 AES 算法，非对称加密使用 RSA 算法
 *  为配置服务器安装 JCE（Java Cryptography Extension）
 *  配置加密
    
     *  对称加密，修改配置服务器（spring-config-server）的 application. yml 文件

``````````
encrypt.key: myKey # 设置加密和解密的 key
``````````

 *  非对称加密
    
     *  使用 keytool 工具生成一对密钥用于加密和解密：`keytool -genkeypair -alias "testKey" -keyalg "RSA" -keystore "D:\myTest. keystore"`（创建一个别名为 testKey 的证书，该证书存放在 D:\\myTest. keystore 密钥库中）
     *  将生成的 myTest. keystore 复制到配置服务器 spring-configserver 的 classpath 下
     *  修改配置服务器（spring-config-server）的 application. yml 文件

``````````yml
encrypt.keyStore: 
   location: classpath:/myTest.keystore # keystore 位置 
   alias: testKey # 密钥对的别名 
   password: root # 密钥库的密码 
   secret: admin # 密钥口令
``````````

 *  加密和解密端点
    
     *  访问的/encrypt 端点会使用配置的 key 对明文进行加密并返回密文
     *  使用 HTTP 的 POST 方法访问配置服务器 /encrypt 端点，该端点会使用配置的 key 对明文进行加密并返回密文
     *  使用 HTTP 的 POST 方法访问配置服务器 /decrypt 端点，该端点会使用配置的 key 对密文进行解密并返回明文
 *  存储加密数据
    
     *  在版本控制系统的配置文件中，使用“\{cipher\}密文”的格式来保存加密后的数据
     *  yml 文件在配置时需要**加单引号**，properties 文件在配置时需要**把单引号去掉**，如

``````````
mysql.passwd: '{cipher}797e316ce5cldc9ce02d6eca929ea48c577efd2e379d79171d454a9b816818cd'`
``````````

## Spring Cloud 的配置抽象 ##

 *  在上下文中增加 Spring Cloud Config 的 PropertySource
 *  通过 PropertySourceLocator 提供 PropertySource
 *  配置中心会以如下的顺序给属性设值（优先级从高到低）
    
     *  应用名-profile.\{profile\}.properties 和 YAML 变量
     *  应用名.\{profile\}.properties 和 YAML 变量
     *  application-profile.\{profile\}.properties 和 YAML 变量
     *  application.\{profile\}.properties 和 YAML 变量

## 其它配置中心 ##

 *  [ZooKeeper][ZooKeeper 1]：spring-cloud-starter-zookeeper-config
 *  Consul：spring-cloud-starter-consul-config
 *  Nacos
 *  [Apollo][]

# 消息微服务 #

## Spring Cloud Stream ##

 *  一个用于构建消息驱动微服务的框架
 *  Spring Cloud Stream 在消息生产者和消息消费者之间加入了一个类似代理的角色，它直接与消息代理中间件进行交互，消息生产者与消息消费者不再需要直接调用各个消息代理框架的 API
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414543553.png) 
    图 9 SCSt-with-binder

## 开发消息微服务 ##

 *  在生产者、消费者项目中添加依赖 spring-cloud-starter-stream-rabbit 或 spring-cloud-starter-stream-kafka
 *  在配置文件中添加 RabbitMQ 的连接信息

``````````yml
rabbitmq: 
    host: localhost 
    port: 5672 
    username: guest 
    password: guest
``````````

 *  在生产者、消费者的启动类上添加 @EnableBinding(通道接口名.class)，绑定通道
    
     *  由于 Spring Cloud Stream 内置了 3 个接口 Source、Sink 与 Processor（Processor 接口继承于 Source 与 Sink），可以使用内置的 output 与 input 两个通道发送消息、接受消息\`\`\` java// 生产者@AutowiredSource source;
        / / 创建消息Message msg = MessageBuilder.withPayload("Hello World".getBytes()).build();/ / 发送消息，调用 output 方法得到 SubscribableChannel（通道）实例，再调用 send 方法source.output().send(msg);

``````````java
  # 消费者 
  @StreamListener(Sink.INPUT) // 声明订阅 Sink.INPUT 通道的消息 
  public void receivelnput(byte[] msg) { 
      System.out.println("receivelnput 方法接收到的消息：" + new String(msg)); 
  }
``````````

# 服务链路追踪 Spring Cloud Sleuth #

 *  用于跟踪微服务的调用过程
 *  Google Dapper 术语
    
     *  Span - 基本的工作单元
     *  Trace - 由一组 Span 构成的树形结构
     *  Annotation - 用于及时记录事件
 *  Sleuth 可以与 Zipkin、Apache HTrace、Brave 和 ELK 等整合进行服务跟踪、数据分析
 *  日志输出：\[appname,traceId,spanId,exportable\]

## Sleuth 整合服务跟踪系统 Zipkin ##

 *  启动 Zipkin 服务器，下载地址 https://zipkin.io/
 *  配置微服务，让各个微服务向 Zipkin 服务器报告过程数据
    
     *  添加依赖 spring-cloud-starter-zipkin（已包含 spring-cloud-starter-sleuth），可同时添加服务发现依赖及相关配置，并设置 `spring.zipkin.discovery-client-enabled=true`
     *  默认使用 HTTP 埋点，如需通过 MQ 埋点，需增加 RabbitMQ 或 Kafka 依赖，并设置 `spring.zipkin.sender.type=web | rabbit | kafka`
     *  添加配置

``````````yml
# 配置 Zipkin 服务器 
  spring: 
    zipkin.base-url: http://localhost:9411 
    sleuth.sample.percentage: 0.1 # 跨度数据的采样百分比，默认值为 0.1 
  # 配置日志级别（可选） 
  logging.level: 
    root: INFO 
    org.springframework.cloud.sleuth: DEBUG
``````````

 *  访问 http://localhost:9411

## Sleuth 整合数据分析平台 ELK ##

 *  ELK 包括 Elasticsearch、Logstash、Kibana 这 3 个项目：
    
     *  Elasticsearch：是一个分布式数据仓库，提供了 RESTful 服务，可用于数据存储、分析
     *  Logstash：主要用于数据收集、转换，可将数据保存到指定的数据仓库中
     *  Kibana：是可视化的数据管理平台，主要用于操作 Elasticsearch 的数据，它提供了多种图表展示数据，支持动态报表
 *  微服务所产生的日志数据会被 Logstash 读取，最终保存到 Elasticsearch 仓库进行保存
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414544101.png) 
    图 10 ELK与微服务集群的关系

 *  下载 ELK
 *  运行 Elasticsearch
 *  使用 Logstash 读取 JSON，分为 3 个阶段：输入（input）、过滤（filter）和输出（output）
 *  使用 Kibana 展示数据
 *  让微服务产生相应的 JSON 日志（使用 Logback 转换 JSON）

## 服务链路追踪 ##

 *  使用 Sleuth 收集的链路数据
 *  使用 RabbitMQ 传输链路数据
 *  使用 ElasticSearch 存储链路数据
 *  使用 Kibana 展示链路数据

## 其它链路追踪 ##

 *  Skywalking
 *  Pinpoint
 *  Cat


[Spring Cloud Netflix]: https://cloud.spring.io/spring-cloud-netflix
[Spring Cloud Config]: https://cloud.spring.io/spring-cloud-config
[Spring Cloud OpenFeign]: https://cloud.spring.io/spring-cloud-openfeign
[Spring Cloud Sleuth]: https://cloud.spring.io/spring-cloud-sleuth
[Spring Cloud Stream]: https://cloud.spring.io/spring-cloud-stream
[Spring Cloud Bus]: https://cloud.spring.io/spring-cloud-bus
[Spring Cloud Security]: https://cloud.spring.io/spring-cloud-security
[Spring_Cloud]: https://static.sitestack.cn/projects/sdky-java-note/e4bd107726aef66b1670f5503c74bb30.png
[ZooKeeper]: https://cloud.spring.io/spring-cloud-zookeeper/reference/html/#spring-cloud-zookeeper-discovery
[Consul]: https://cloud.spring.io/spring-cloud-consul/reference/html/
[Nacos]: https://nacos.io/zh-cn/index.html
[hystrix-command-flow-chart]: https://static.sitestack.cn/projects/sdky-java-note/e2a2ec720717194b4e51999e2199ce84.png
[Hystrix]: https://static.sitestack.cn/projects/sdky-java-note/11883019c8c4cb7e0b72b1806c76ce29.png
[Hystrix_Dashboard]: https://static.sitestack.cn/projects/sdky-java-note/4f6ac6708f29cc4140bb351b3da2b9f5.png
[Zuul]: https://static.sitestack.cn/projects/sdky-java-note/4716c77e4e29ab1874146a5b7694f13e.png
[Zuul_1.x]: https://static.sitestack.cn/projects/sdky-java-note/ccd27c874dfc5851f64fcb0eb6721412.jpeg
[Zuul_2]: https://static.sitestack.cn/projects/sdky-java-note/9cf609400d9cba0872adad241962f68c.png
[Zuul 1]: https://static.sitestack.cn/projects/sdky-java-note/77994816c7088bb4c9500053dc575070.png
[ZooKeeper 1]: https://cloud.spring.io/spring-cloud-zookeeper/reference/html/#spring-cloud-zookeeper-config
[Apollo]: https://github.com/ctripcorp/apollo
[SCSt-with-binder]: https://static.sitestack.cn/projects/sdky-java-note/e78766d022b561b1810f856c4d6d3b3b.png
[ELK]: https://static.sitestack.cn/projects/sdky-java-note/a3ea7ce8ec4e2948af0424fbd3286401.png