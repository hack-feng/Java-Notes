    欢迎来到SpringCloud的江湖，在本章中，我们将向大家传授如何创建SpringCloud的父子项目架构。
    知识无止境，故事有好坏，文章纯属虚构，欢迎大家吐槽。
    行走江湖，没点伎俩傍身怎么能行。本章牵扯到的技术以及工具如下：
    Intellij Idea 2018.1
    JDK 8
    MAVEN 3.2.2
    SpringBoot 1.5.13.RELEASE
    Spring-Cloud Edgware.SR3
    Fegin声明式调用
    Ribbon负载均衡
    Hystrix熔断器
    
> 前面我们分别介绍了[Fegin服务间调用](https://blog.csdn.net/qq_34988304/article/details/103984893)、[Ribbon负载均衡](https://blog.csdn.net/qq_34988304/article/details/104014277)、[Hystrix断路器](https://blog.csdn.net/qq_34988304/article/details/104033842)。这里我们说一下超时和重试机制

### 各组件超时
### RestTemplate超时时间控制
~~~
/**
 * RestTemplate超时设置
 * 注释：@LoadBalanced配置启用Ribbon负载均衡,有无此注解不影响超时配置
 */
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    //单位为ms
    factory.setReadTimeout(5000);
    //单位为ms
    factory.setConnectTimeout(5000);
    return new RestTemplate(factory);
}
~~~

### Ribbon的超时配置
* 全局配置
~~~
# 设置ribbon超时时间
# SpringCloud feign 默认开启ribbon支持
ribbon:
  ### 指的是建立连接所用的时间，适用于网络状况正常的情况下，两端连接所用的时间。
  ReadTimeout: 3000
  ### 指的是建立连接后从服务器读取到可用资源所用的时间。
  ConnectTimeout: 3000
~~~

* 局部设置
~~~
# 设置ribbon客户端超时时间
# service-id是Ribbon所使用的虚拟主机名，一般和注册到Eureka Server的服务名一致，即与spring.application.name一致
service-id:
  ribbon:
    ### 指的是建立连接所用的时间，适用于网络状况正常的情况下，两端连接所用的时间。
    ReadTimeout: 3000
    ### 指的是建立连接后从服务器读取到可用资源所用的时间。
    ConnectTimeout: 3000
~~~

### Fegin的超时配置
~~~
### 设置fegin的超时
feign:
  client:
    config:
      # 如果使用通用配置，携程default
      feginName:
        connectTimeout: 5000
        readTimeout: 5000 # 单位ms
~~~

### Hystrix的超时配置
~~~
# 配置hystrix超时时间
hystrix:
  command:
    default:
      execution:
        timeout:
          # 默认开启，如需关闭，将enabled改为false即可
          enabled: true
        isolation:
          thread:
            timeoutInMilliseconds: 10000
~~~

## 各组件重试机制
### Ribbon重试
~~~
# 注意Feign和Ribbon不要重复重试，否则会产生笛卡尔积次数
# <client>为Ribbon Client名称，表示对指定的Ribbon Client进行重试。省略代表全局配置
<client>:
  ribbon:
    MaxAutoRetries: 1 # 同一台实例最大重试次数,不包括首次调用
    MaxAutoRetriesNextServer: 2 # 重试负载均衡其他的实例最大重试次数,不包括首次调用
    OkToRetryOnAllOperations: false  # 是否所有操作都重试
~~~

### 基于HTTP请求响应码进行重试
~~~
<clickName>:
  ribbon:
    retryableStatusCodes: 404,502
~~~


