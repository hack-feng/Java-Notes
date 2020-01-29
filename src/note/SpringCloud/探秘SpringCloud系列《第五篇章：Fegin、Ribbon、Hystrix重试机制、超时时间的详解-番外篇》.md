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

### OpenFegin Click的Retryer重试
~~~
/**
 * 开启Fegin重试机制
 */
@Bean
Retryer feignRetryer() {
    return  new Retryer.Default(1L, 5L, 3);
}
~~~

* Retryer继承了Cloneable接口，它定义了continueOrPropagate、clone方法；它内置了一个名为Default以及名为NEVER_RETRY的实现
* Default有period、maxPeriod、maxAttempts参数可以设置，默认构造器使用的period为100，maxPeriod为1000，maxAttempts为5；continueOrPropagate方法首先判断attempt是否达到阈值，达到则抛出异常，否则进一步计算interval，然后进行sleep
* NEVER_RETRY的continueOrPropagate直接抛出异常，而clone方法直接返回当前实例

### 注意事项
* 建议讲Hystrix的超时时间设置为大于多次重试所用时间的和。
* 一般不建议将ribbon.OkToRetryOnAllOperations设为true。一旦启用，post请求时，由于缓存了请求题，此时可能会影响服务器的资源。
* Ribbon重试不要和Retryer重试同时启用，会产生笛卡尔积的重试结果。

> 本章到此结束。后续文章会陆续更新，文档会同步在CSDN和GitHub保持同步更新。<br>
> CSDN：https://blog.csdn.net/qq_34988304/category_8820134.html <br>
> Github文档：https://github.com/hack-feng/Java-Notes/tree/master/src/note/SpringCloud <br>
> GitHub源码：https://github.com/hack-feng/Spring-Cloud-Edgware.git <br>