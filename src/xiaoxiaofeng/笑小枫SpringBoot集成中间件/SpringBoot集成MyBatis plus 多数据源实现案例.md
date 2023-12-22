## 1. 项目背景

最近写文章发布到【笑小枫】小程序和[我的个人网站](https://www.xiaoxiaofeng.com/)上，因为个人网站用的是halo框架搭建，两边数据结构不一致，导致我每次维护文章都需要两边维护，这就很烦~

于是，本文就诞生了。通过项目链接这两个数据库，我在维护文章的时候，同时同步下个人网站的博客。

PS：果然，程序员就是懒🤪

> 深度PS：在七夕发布这一篇文章，感觉不对劲呀，还是要专一，不能多...【手动狗头，不是单身的那种】

## 2. 项目准备

项目使用的是Mybatis plus。这里简单点，直接使用Mybatis plus的多数据源，简单方便。

这里已经脱离了原有代码。单独抽出了一个小Demo，小枫我还是很贴心的。不让你们看我那乱七八糟的逻辑了~

>  这里只是为了演示，实际按你们项目来就行了

### 2.1 准备数据库

首先创建两个数据库吧，这里演示就叫`test`和`test2`了。

然后简单点，每个表创建一张表好了。

test创建user表：

~~~mysql
CREATE TABLE `user` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`login_name` VARCHAR(64) NOT NULL COMMENT '登录名',
	`password` VARCHAR(128) NOT NULL COMMENT '密码',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`update_time` DATETIME NOT NULL COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)COMMENT='用户表' COLLATE='utf8_general_ci' ENGINE=InnoDB;
~~~

test2创建product表：

~~~mysql
CREATE TABLE `product` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`name` VARCHAR(64) NOT NULL COMMENT '名称',
	`remark` VARCHAR(128) NOT NULL COMMENT '备注',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`update_time` DATETIME NOT NULL COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)COMMENT='商品表' COLLATE='utf8_general_ci' ENGINE=InnoDB;
~~~

创建完之后就是这样：

![image-20230822104220517](https://image.xiaoxiaofeng.site/blog/2023/08/22/xxf-20230822104220.png?xxfjava)

## 3. 功能实现

简单点吧，接下来坐飞机🛫，直接飞核心内容，算了还是火箭吧🚀。

### 3.1 引入依赖

这里的版本和mybatis-plus版本一致就可以了，这里用`<mybatis-plus-version>3.5.2</mybatis-plus-version>`

~~~xml
        <!-- 多数据源 -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
            <version>${mybatis-plus-version}</version>
        </dependency>
~~~

### 3.2 生成基础对象

然后使用Mybatis plus代码生成工具生成基础对象，具体代码就不贴了，需要的可以看Github源码，源码地址放在文章最后。

这里controller用了一个，其它的分包了，实际根据项目需求来就行，影响不大，自己看着舒服就行。

在Application.java上添加`@MapperScan`的注解。

~~~java
package com.maple.dynamic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.maple.dynamic.test.mapper", "com.maple.dynamic.test2.mapper"})
public class MapleDynamicDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapleDynamicDatasourceApplication.class, args);
    }
}
~~~

整个项目的结构如下：

![image-20230822111227672](https://image.xiaoxiaofeng.site/blog/2023/08/22/xxf-20230822111227.png?xxfjava)

### 3.3 配置数据库链接

配置application.yml文件，这里使用yml格式哈。

~~~yml
server:
  port: 8080
  
spring:
  datasource:
    dynamic:
      primary: master #设置默认的数据源或者数据源组,默认值即为master
      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源
      datasource:
        master:
          url: jdbc:mysql://127.0.0.1:3306/test?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B8
          username: root
          password: Zhang123
          driver-class-name: com.mysql.cj.jdbc.Driver
        test2:
          url: jdbc:mysql://127.0.0.1:3306/test2?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B8
          username: root
          password: Zhang123
          driver-class-name: com.mysql.cj.jdbc.Driver
~~~

默认数据库是master，test2配置了一个test2的数据源，这里的master和test2可以根据自己需求命名。

### 3.4 使用演示

默认的是master数据源，这里就不做配置了。

如果想使用test2数据源，需要在service上添加@DS("test2")注解切换数据源。

**@DS** 可以注解在方法上或类上，**同时存在就近原则 方法上注解 优先于 类上注解**。

|     注解      |                   结果                   |
| :-----------: | :--------------------------------------: |
|    没有@DS    |                默认数据源                |
| @DS("dsName") | dsName可以为组名也可以为具体某个库的名称 |

我们给`ProductServiceImpl.java`添加上`@DS("test2")`注解。

![image-20230822133533034](https://image.xiaoxiaofeng.site/blog/2023/08/22/xxf-20230822133533.png?xxfjava)

创建`TestController.java`，添加测试方法，如下图所示：

![image-20230822133727369](https://image.xiaoxiaofeng.site/blog/2023/08/22/xxf-20230822133727.png?xxfjava)

## 4. 功能测试

好了，项目的整体配置都OK了，我们一起来看一下效果吧。

启动项目

![image-20230822133901848](https://image.xiaoxiaofeng.site/blog/2023/08/22/xxf-20230822133901.png?xxfjava)

可以看到[test2]、[master]添加成功。其中[master]为主数据源。

我们调用下`GET http://localhost:8080/test`试下，可以看到成功取到了数据。

![image-20230822134100660](https://image.xiaoxiaofeng.site/blog/2023/08/22/xxf-20230822134100.png?xxfjava)

好了，本文就到这里了。也懒得总结了，总结内容看标题吧😅

## 5. 项目源码

[https://github.com/hack-feng/maple-product/](https://github.com/hack-feng/maple-product/)

其中`maple-dynamic-datasource`模块即为本文的Demo源码。需要的朋友可以看下。

感兴趣的朋友可以帮忙点个star⭐⭐⭐⭐⭐后续会有更多Java相关的集成Demo，让我来做你的百宝袋吧。



