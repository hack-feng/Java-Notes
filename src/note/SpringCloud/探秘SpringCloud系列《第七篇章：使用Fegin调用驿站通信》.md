> 上一篇讲到了Eureka注册中心，我们将服务都注册到Eureka Server上。那么问题来了，我们有多个服务，都注册到Eureka Server上了，那么它们之间怎么项目调用呢？比如张三想去酒馆喝酒，张三怎么找到酒馆呢？带着问题我们往下分析。

> 有的同学想到了，我们可以使用RestTemplate，让用户去调用酒馆的服务，这样固然可以，但有的同学又说了，这样每次都要拼长长的url，有没有什么更简单的方法呢。Spring Cloud里面已经集成了解决方案，那么有请我们的Fegin（声明式调用）闪亮登场。

### 什么是Fegin?
Feign是一个声明式的Web Service客户端，它的目的就是让Web Service调用更加简单。Feign提供了HTTP请求的模板，通过编写简单的接口和插入注解，就可以定义好HTTP请求的参数、格式、地址等信息。

而Feign则会完全代理HTTP请求，我们只需要像调用方法一样调用它就可以完成服务请求及相关处理。Feign整合了Ribbon和Hystrix(关于Hystrix我们后面再讲)，可以让我们不再需要显式地使用这两个组件。

### Fegin有什么特性?
* 可插拔的注解支持，包括Feign注解和JAX-RS注解;
* 支持可插拔的HTTP编码器和解码器;
* 支持Hystrix和它的Fallback;
* 支持Ribbon的负载均衡;
* 支持HTTP请求和响应的压缩。

### 怎么使用Fegin?
接下来，我们便以张三去酒馆喝酒的例子来介绍Fegin的使用。

首先我们先要创建用户和酒馆两个服务。先来回顾一下这两个服务如何创建。

[探秘SpringCloud系列《第一篇章：创建Spring Cloud的Maven江湖》](https://blog.csdn.net/qq_34988304/article/details/103886568)

准备好pub-service和user-service两个服务。然后把它们注册到Eureka Server里面。因为我们新创建的服务都需要引用Eureka Click的依赖，因为pom文件支持继承的特性，所以我们便把它放在顶级的pom.xml中引用。既然要使用fegin，我们需要在pom.xml添加Fegin的依赖。同样也可以放在顶级pom.xml中。

修改Spring-Cloud-Edgware的pom.xml文件，添加Eureka Click和Fegin的依赖，修改后如下：
~~~

~~~


