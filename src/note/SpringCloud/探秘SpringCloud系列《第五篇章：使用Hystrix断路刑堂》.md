    欢迎来到SpringCloud的江湖，在本章中，我们将向大家传授如何创建SpringCloud的父子项目架构。
    知识无止境，故事有好坏，文章纯属虚构，欢迎大家吐槽。
    行走江湖，没点伎俩傍身怎么能行。本章牵扯到的技术以及工具如下：
    Intellij Idea 2018.1
    JDK 8
    MAVEN 3.2.2
    SpringBoot 1.5.13.RELEASE
    Spring-Cloud Edgware.SR3
    Fegin声明式调用
    Hystrix熔断器

>本文基于[探秘SpringCloud系列《第三篇章：使用Fegin声明式服务调用》](https://blog.csdn.net/qq_34988304/article/details/103984893)一文继续延伸。

>本文中部分代码会有删减，详情请参考[GitHub源码](https://github.com/hack-feng/Spring-Cloud-Edgware.git)
### 配置Hystrix
1. 修改Spring-Cloud-Edgware的pom.xml文件，添加Hystrix熔断器的依赖，修改后如下：
~~~xml
    <!--熔断机制-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-hystrix</artifactId>
    </dependency>
~~~

2. 配置user-service的配置文件，开启hystrix断路器，并配置超时时间
~~~
server:
  port: 8000
spring:
  application:
    name: user-service

#注册服务到eureka-server
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka/

####配置请求超时时间
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 10000
            
###开启Hystrix断路器
feign:
  hystrix:
    enabled: true
~~~

### Hystrix的使用

#### 修改pub-service
编写pub-service的测试类：TestHystrixController.java
~~~java
package com.maple.pub.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/testHystrix")
public class TestHystrixController {

    @GetMapping("/busyPub")
    public String busyPub(String speak){
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        System.out.println(sdf.format(new Date()) + "【小二收到招呼】：" + speak);
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "不好意思客官，让您久等了...";
    }
}
~~~

#### 修改user-service

修改UserGoPubController.java
~~~java
package com.maple.user.controller;

import com.maple.user.fegin.PubServiceFegin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户去酒馆消费Controller
 */
@RestController
@RequestMapping("/userGoPub")
public class UserGoPubController {

    private final PubServiceFegin pubServiceFegin;

    @Autowired
    public UserGoPubController(PubServiceFegin pubServiceFegin) {
        this.pubServiceFegin = pubServiceFegin;
    }

    /**
     * 张三去了一个火爆的酒馆，演示Hystrix熔断器
     */
    @GetMapping("/goBusyPub")
    public String goBusyPub(){
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        System.out.println("张三来到了一个非常火爆的酒馆，人多的地方，就肯定好喝");
        String speak = "小二，来两坛上等的二锅头，两斤牛肉，一只烤羊腿";
        System.out.println(sdf.format(new Date()) + "【发送消息】：" + speak);
        String result = pubServiceFegin.busyPub(speak);
        System.out.println(sdf.format(new Date()) + "【收到小二回话】：" + result);
        System.out.println("张三进入了漫长的等待期...");
        return result;
    }
}
~~~

添加PubServiceError.java的熔断处理类
~~~java
package com.maple.user.fegin.fallback;

import com.maple.user.fegin.PubServiceFegin;
import org.springframework.stereotype.Component;

@Component
public class PubServiceError implements PubServiceFegin {

    @Override
    public String busyPub(String speak) {
        return "啊哦，小二太忙了，程序进入到断路器...";
    }
}
~~~

修改PubServiceFegin.java
~~~java
package com.maple.user.fegin;

import com.maple.user.fegin.fallback.PubServiceError;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "pub-service", fallback = PubServiceError.class)
public interface PubServiceFegin {

    /**
     * fegin调用pub-service，酒馆比较繁忙，休眠15s后应答
     * @param speak 招呼小二
     * @return 小二返回的话语
     */
    @GetMapping("/testHystrix/busyPub")
    String busyPub(@RequestParam("speak") String speak);
}
~~~

### 测试Hystrix
我们依次启动eureka-server、user-service、pub-service

浏览器Get请求：http://127.0.0.1:8000/userGoPub/goBusyPub

查看结果：

![SpringCloud江湖](./images/06-hystrix/01.jpg)

> 本章到此结束。后续文章会陆续更新，文档会同步在CSDN和GitHub保持同步更新。<br>
> CSDN：https://blog.csdn.net/qq_34988304/category_8820134.html <br>
> Github文档：https://github.com/hack-feng/Java-Notes/tree/master/src/note/SpringCloud <br>
> GitHub源码：https://github.com/hack-feng/Spring-Cloud-Edgware.git <br>