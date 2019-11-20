#Spring Cloud 
场景：

我的这个异常出现场景是这样的（我采用的是Spring Cloud中Feign组件来实现服务间调用的）：

有两个服务A和B,A调用B(A->B)。

场景一：先启动了A，再启动了B。但是这时候，怎么都调用不到B。

场景二：先启动B，再启动A，这是可以正常调用。然后挂掉B，再重启。之后就怎么也调用不到B了。

解决方案：

~~~
--设置property ribbon.eureka.enable=false将会明确的让Eureka的ribbon失效。
--我们这里需要手动开启它

ribbon:
  eureka:
    enabled: true
~~~

### 抽取config的通用配置，例如：eureka注册中心配置，zipkin，database等配置信息
在bootstrap.yml添加以下配置，主要是name，后面的你要获取的配置目录，以","分隔。<br>
以下例子将请求application-dev.yml,eureka-server-dev.yml,base-dev.yml三个配置文件的内容。<br>
默认会加载application-dev.yml，故也可以在name后面不加application。
~~~
spring:
  cloud:
    config:
      label: master
      profile: dev
      uri: http://localhost:8888/
      name: application,eureka-server,base
~~~
