    欢迎来到SpringCloud的江湖，在本章中，我们将向大家传授如何创建SpringCloud的父子项目架构。
    知识无止境，故事有好坏，文章纯属虚构，欢迎大家吐槽。
    行走江湖，没点伎俩傍身怎么能行。本章牵扯到的技术以及工具如下：
    Intellij Idea 2018.1
    JDK 8
    MAVEN 3.2.2
    SpringBoot 1.5.13.RELEASE
    Spring-Cloud Edgware.SR3
    Spring Cloud Config声明式调用

> 随着微服务的不断增多，微服务的管理便成了难题，那么怎么解决这个问题，于是便有了Spring Cloud Config.

### 一、Spring Cloud Config 是什么
Spring Cloud Config 是用来为分布式系统中的基础设施和微服务应用提供的集中化的外部配置支持。分为服务端和客户端两个部分。其中服务端也称为分布式配置中心，它是一个独立的微服务应用，用来连接配置仓库并为客户端提供获取配置信息。实现了对服务器端和客户端中环境变量和属性配置的抽象映射。

说直白一点：就是把springboot项目中的application.yml配置文件抽取出来，放在一起统一管理。

可以将配置信息放在git，svn或本地化的文件系统。

### 二、构建Spring Cloud Config配置中心

 - 创建一个springboot工程，命名为cloud-config，并在pom.xml文件引入下面依赖：

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <artifactId>cloud-eureka</artifactId>
        <groupId>com.maple</groupId>
        <version>1.0.0</version>
    </parent>
    <artifactId>cloud-config</artifactId>
    <version>1.0.0</version>
    <name>cloud-config</name>
    <description>Demo project for Spring Boot</description>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

 - 在springboot项目程序主类添加@EnableConfigServer注解，开启Spring Cloud Config的服务功能。
 

```
@EnableDiscoveryClient
@EnableConfigServer
@SpringBootApplication
public class CloudConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudConfigApplication.class, args);
    }

}
```

 - 在application.yml中添加配置服务的基本信息以及Git仓库的相关信息。
 

```
server:
  port: 6666

#注册服务到eureka-server
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka/

spring:
  application:
    name: cloud-config
  cloud:
    config:
      server:
        git:
          uri: https://github.com/hack-feng/Spring-Cloud-Edgware.git
          search-paths: /config
          # 这里用的是公有库，无需密码，如果是私有库，需设置密码
          username:
          password:
      label: master
```
Git的配置分别对应以下内容
 - spring.cloud.config.profile:对应的是开发版本，对应配置规则中的profile，见下面介绍
 - spring.cloud.config.label:Git代码的分支信息，默认是master
 - spring.cloud.config.server.uri:配置Git仓库位置
 - spring.cloud.config.server.search-paths:配置Git仓库路径下的相对搜索位置，可以配置多个
 - spring.cloud.config.server.username:配置Git仓库用户名，共有库可以不设置
 - spring.cloud.config.server.password:配置Git仓库密码

### 配置规则详解
在Git仓库中https://github.com/hack-feng/Spring-Cloud-Edgware/config下创建以下配置文件
 - application-dev.yml
 ~~~
 logging:
  level:
    org.springframework.security: INFO

eureka:
  client:
    serviceUrl:
      defaultZone: http://127.0.0.1:8761/eureka/

 ~~~
 
 - pay-service-dev.yml
~~~
server:
  port: 8004

name: zhangsan
~~~

内容对应：

***注意使用yml格式 :后面跟一个空格***
端点与配置文件的映射规则如下：

 -  /{application}/{profile}[/{label}]
 -  /{application}-{profile}.yml
 -  /{label}/{application}-{profile}.yml
 -  /{application}-{profile}.properties
 -  /{label}/{application}-{profile}.properties

{application}表示微服务的名字，即：spring.application.name
{profile}表示开发版本，即：spring.cloud.config.profile
{label}表示Git仓库的分支，即：spring.cloud.config.label

 - 启动项目，访问：http://127.0.0.1:6666/pay-service/dev如下图
 ![SpringCloud江湖](./images/config/01.jpg)
 
 - 访问http://127.0.0.1:6666/pay-service-dev.yml得到
 ![SpringCloud江湖](./images/config/02.jpg)
 
 由以上例子可见：application-dev.yml为父类，其他的yml都可以获得application-dev.yml里面的内容，因此，我们可以在application-dev.yml定义一些通用的东西。
 
 如果application-dev.yml和pay-service-dev.yml均定义了同一个配置。子类的会覆盖掉父类的配置。

 至此，已成功构建了Config Server，并通过构造URL方式，获取了Git仓库的配置信息。


### 三、编写config Client
上文已经构造了cloud-config，并使用Config Server端点获取到了配置信息。接下来讨论SpringCloud微服务如何获取配置信息。

 - 在cloud-service下，创建一个springBoot模块，命名为：pay-service ，pom.xml文件如下：
 

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.maple</groupId>
        <artifactId>cloud-service</artifactId>
        <version>1.0.0</version>
    </parent>
    <artifactId>pay-service</artifactId>
    <version>1.0.0</version>
    <name>pay-service</name>
    <description>Demo project for Spring Boot</description>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

在祖节点的pom.xml添加config的配置：
~~~
<!--添加config统一配置中心依赖-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
~~~

 -  springboot项目程序主类程序。
 

```java
package com.maple.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayServiceApplication.class, args);
    }
}
```

 - 创建bootstarp.yml配置，指定congfig-server的位置，如下
 
~~~
spring:
  cloud:
    config:
      profile: dev
      uri: http://127.0.0.1:6666/
  application:
    name: pay-service
~~~

 - 创建测试ConfigTestController.java 内容如下

```java
package com.maple.pay.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 笑小枫
 * @date 2020/2/7 10:50
 **/
@Scope
@RestController
public class TestConfigController {

    /**
     * 从配置文件中取出该值
     */
    @Value("${name}")
    private String name;

    @GetMapping("/test")
    private String test(){
        return "配置文件中获取到的内容为：" + name;
    }
}
```
启动项目，访问http://127.0.0.1:8004/test，得到以下结果

 ![SpringCloud江湖](./images/config/03.jpg)

到这里spring Cloud Config基于Git的统一配置中心就搭建好了。

> 本章到此结束。后续文章会陆续更新，文档会同步在CSDN和GitHub保持同步更新。<br>
> CSDN：https://blog.csdn.net/qq_34988304/category_8820134.html <br>
> Github文档：https://github.com/hack-feng/Java-Notes/tree/master/src/note/SpringCloud <br>
> GitHub源码：https://github.com/hack-feng/Spring-Cloud-Edgware.git <br>
