> Apollo（阿波罗）是携程框架部门研发的分布式配置中心，能够集中化管理应用不同环境、不同集群的配置，配置修改后能够实时推送到应用端，并且具备规范的权限、流程治理等特性。。。。。
>
> 微服务,微服务,java,spring cloud

# 前言 #

Apollo（阿波罗）是携程框架部门研发的分布式配置中心，能够集中化管理应用不同环境、不同集群的配置，配置修改后能够实时推送到应用端，并且具备规范的权限、流程治理等特性，适用于服务配置管理场景。

> 自己在学习过程中发现Apollo配置中心，相对于SpringConfig等配置中心在部署上特别复杂，但是一旦配置完成，使用起来特别方便，因此在学习中应该特别弄明白他的架构模型。

--------------------

`本文章图片均来自于Apollo官方文档[Apollo官网](https://www.apolloconfig.com/#/zh/design/apollo-design?id=_133-meta-server)。`

# 一、Apollo架构 #

## （一）简介 ##

> Apollo架构模型简单点如下图：他的用户分两类，一类相当于管理员（运维人员）往其中写入配置，一类为客户端（微服务）往配置拉取配置。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728682751.png)

## （二）角色介绍 ##

> 但是光看上图不足以明白Apollo配置中心是如何运行的，更不能自己配置，因此重点依照下图对Apollo架构模型进行介绍。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728683607.png)

<table> 
 <thead> 
  <tr> 
   <th>角色</th> 
   <th>介绍</th> 
  </tr> 
 </thead> 
 <tbody> 
  <tr> 
   <td>Portal</td> 
   <td>portal英文意思为门户，在架构中充当的角色为管理端，用户通过该模块对配置进行修改。</td> 
  </tr> 
  <tr> 
   <td>PortalDB</td> 
   <td>存储一些环境变量，及配置环境等信息的数据库，注意该库不存储配置信息。</td> 
  </tr> 
  <tr> 
   <td>Admin Service</td> 
   <td>负责接收Portal发送过来的配置信息，对配置信息进行修改</td> 
  </tr> 
  <tr> 
   <td>ConfigDB</td> 
   <td>储存配置信息的数据库</td> 
  </tr> 
  <tr> 
   <td>Client</td> 
   <td>配置信息使用者，通常为微服务。</td> 
  </tr> 
  <tr> 
   <td>Config Service</td> 
   <td>负责定期从数据库（ConfigDB）中拉取配置信息，若有变化，推送给客服端。或是客服端定期利用改服务拉取配置信息。</td> 
  </tr> 
  <tr> 
   <td>Eureka</td> 
   <td>注册中心，无论Conifg Service还是Admin Service在实际使用中都是部署在多台服务器上的。因此，对于客服端或者门户管理端而言，建立两个服务的连接都需要通过注册中心发现服务地址而后在连接。</td> 
  </tr> 
  <tr> 
   <td>Meta Service</td> 
   <td>Meta Server从Eureka获取Config Service和Admin Service的服务信息，相当于是一个Eureka Client，增设一个Meta Server的角色主要是为了封装服务发现的细节，对Portal和Client而言，永远通过一个Http接口获取Admin Service和Config Service的服务信息，而不需要关心背后实际的服务注册和发现组件。</td> 
  </tr> 
 </tbody> 
</table>

## （三）服务端实现 ##

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728684044.png)  
管理员利用portal端发布配置，portal首先访问MetaService发现AdminService地址，而后访问AdminService将配置修改到ConfigDB，同时通知ConfigService通知变更，ConfigService推送更新消息给Client。

## （四）客服端实现 ##

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728684489.png)  
客服端支持两种方式更新配置  
1.当配置中心配置变更后，主动推送配置给客服端，而后客服端将配置缓存到本地，并同时将应用相关配置进行热修改。  
2.客服端定时从配置中心拉取配置，若有变化则通知应用程序进行更新。

> 因为有缓存的存在所以，当与配置中心断开时，应用也不会停止，他会从本地缓存读取相应的配置。

# 二、Apollo部署 #

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728684992.png)  
Apollo不同的环境是独立且相互隔离的，而他的使用者，通过自身的配置来确定访问/设置的环境。下面以dev开发环境、pro生产环境为案例进行apollo服务的部署。

## （一）准备数据库 ##

每套环境需要依赖数据库，ApolloPortalDB和ApolloConfigDB（[数据库文件下载地址][Link 1]）。这里对两个系统准备数据库环境如下

<table> 
 <thead> 
  <tr> 
   <th>环境</th> 
   <th>portal数据库（共用）</th> 
   <th>config数据库（独立）</th> 
  </tr> 
 </thead> 
 <tbody> 
  <tr> 
   <td>dev</td> 
   <td>ApolloPortal</td> 
   <td>ApolloConfig-dev</td> 
  </tr> 
  <tr> 
   <td>pro</td> 
   <td>ApolloPortal</td> 
   <td>ApolloConfig-pro</td> 
  </tr> 
 </tbody> 
</table>

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728685456.png)

## （二）配置服务 ##

环境准备

<table> 
 <thead> 
  <tr> 
   <th>环境</th> 
   <th>主机</th> 
   <th>服务</th> 
  </tr> 
 </thead> 
 <tbody> 
  <tr> 
   <td>dev</td> 
   <td>192.168.17.101</td> 
   <td>configService 、adminService、 portal</td> 
  </tr> 
  <tr> 
   <td>pro</td> 
   <td>192.168.17.102</td> 
   <td>configService 、adminService</td> 
  </tr> 
 </tbody> 
</table>

### 1. 手动部署 ###

自己下载adminservice、configservice、portal三个服务的压缩包，上传至服务器解压。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728685850.png)

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728686283.png)

#### （1）ConfigService ####

进入configService中的config目录  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728686592.png)

修改application-github.properties文件中的数据源，注意在数据库连接最好加上时区，否则会可能在运行时会出错。其中数据库选择，根据相应的环境做选择。如为dev环境就选择configdb-dev ， pro环境就选择configdb-pro。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728686923.png)

#### （2）AdminService ####

同configService进入conf目录修改application-github.properties中的数据源。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728687466.png)

#### （3）Portal ####

修改config目录下的application-github.properties 文件，指定portal数据库。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728687878.png)  
修改config目录下的apollo-env.properties文件，配置相应环境的地址。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728688357.png)

#### （4）启动服务 ####

因为configService中自带eureka 和 meta服务的包，所以启动顺序应该为:  
confiService -> adminService ->portal  
启动configService中的scripts包下的startup.sh文件

> 编辑startup.sh文件更改Service的启动端口一般使用默认8080，这个端口为metaService端口，其他服务或应该通过该端口访问注册中心，从而进行其他操作。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728688768.png)

启动configService中的scripts包下的startup.sh文件  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728689129.png)

启动portal中的scripts包下的startup.sh文件  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728689490.png)


![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728689803.png)  
访问portal主机（http://192.168.17.101:8070/)即可看到服务配置页面，输入用户名apollo，密码admin后登录。

### 2.脚本自动部署（Quick Start） ###

#### （1）下载Quick Start ####

1.从GitHub下载：[下载地址][Link 2]  
2.从百度网盘下载  
通过网盘链接下载，提取码: 9wwe  
下载到本地后，在本地解压apollo-quick-start.zip

#### （2）配置文件 ####

Apollo服务端需要知道如何连接到你前面创建的数据库，所以需要编辑demo.sh，修改ApolloPortalDB和ApolloConfigDB相关的数据库连接串信息。

```properties
#apollo config db info
apollo_config_db_url="jdbc:mysql://localhost:3306/ApolloConfigDB?characterEncoding=utf8&serverTimezone=Asia/Shanghai"
apollo_config_db_username=用户名
apollo_config_db_password=密码（如果没有密码，留空即可）

# apollo portal db info
apollo_portal_db_url="jdbc:mysql://localhost:3306/ApolloPortalDB?characterEncoding=utf8&serverTimezone=Asia/Shanghai"
apollo_portal_db_username=用户名
apollo_portal_db_password=密码（如果没有密码，留空即可）
```

#### （3）启动脚本 ####

```properties
./demo.sh start

#看到下面提示表示成功
==== starting service ====
Service logging file is ./service/apollo-service.log
Started [10768]
Waiting for config service startup.......
Config service started. You may visit http://localhost:8080 for service status now!
Waiting for admin service startup....
Admin service started
==== starting portal ====
Portal logging file is ./portal/apollo-portal.log
Started [10846]
Waiting for portal startup......
Portal started. You can visit http://localhost:8070 now!
```

> 注意：Quick Start只针对本地测试使用

## （三）问题排除 ##

### 1.日志文件位置 ###

在服务根目录中xx.conf文件记录了日志文件存储位置，通过日志文件可以排除错误。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728690787.png)

### 2.通过数据库修改配置 ###

#### （1）eureka位置 ####

在configDB库中的serverconfig表中eureka.service.url配置eureka位置，若使用自带eureka可以不修改，若使用自己搭建eureka修改为自己的eureka就行。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728691242.png)

#### （2）portal环境设置 ####

自己在portal使用中遇到一个问题：虽然在配置文件中配置了dev和pro两个环境，但是启动时页面只有一个环境，这里可通过数据库配置，来手动给portal添加环境，然后重启portal添加完毕。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728692105.png)

# 三、Apollo使用（SpringCloud） #

## （一）添加配置 ##

### 1.创建应用 ###

配置中心利用应用来识别区分配置  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728693030.png)

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728693564.png)  
AppId：应用的唯一识别符  
应用名称：仅在页面上显示的名称

### 2.键值对添加 ###

点击添加配置，来对配置键值对进行添加。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728694077.png)  
添加完后需要点击发布，才会生效。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728694550.png)

### 3.yml文件添加 ###

由于Apollo私有命名空间只接受.properties键值对形式，所以对于yml文件需要利用[yml在线转化工具][yml]转为键值对形式，再利用文本形式添加。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728695026.png)

## （二）项目读取配置（快速入门） ##

这里直接上手快速读取配置

### 1.导入依赖 ###

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.dbh123</groupId>
    <artifactId>springclout-apollo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <parent>
        <artifactId>spring-boot-starter-parent</artifactId>
        <groupId>org.springframework.boot</groupId>
        <version>2.2.4.RELEASE</version>
    </parent>
    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Hoxton.SR1</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--openfeign  由于bootstrap配置文件需要导入相应springcloud依赖才会生效且优先级高于application-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
        <!--apollo客服端依赖-->
        <dependency>
            <groupId>com.ctrip.framework.apollo</groupId>
            <artifactId>apollo-client</artifactId>
            <version>1.9.2</version>
        </dependency>

    </dependencies>

</project>
```

### 2.编写配置 ###

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728695556.png)

#### （1）设置应用id ####

在classpath下的META-INF文件夹中新建app.properties文件写入需要读取的应用id

> 注意这里的appID要和之前apollo配置中心应用id一致。

    app.id=dbh123-test-server

#### （2）配置环境列表 ####

在classpath下新建apollo-env.properties写入环境列表

    dev.meta=http://192.168.17.101:8080
    pro.meta=http://192.168.17.102:8080

#### （3）选择环境 ####

更改window/linux下C:\\opt\\settings\\server.properties文件来选择需要读取的环境。这里演示快速入门，详细在下个目录介绍。

    env=PRO

#### （4）配置命名空间 ####

bootstrap.yml文件中设置命名空间这里采用默认命名空间。

```yml
#注入默认application namespace
apollo:
  bootstrap:
    enabled: true
```

### 3.测试 ###

启动项目访问，查看结果。  
controller代码：

```java
package com.dbh123.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: DBH123
 * @date: 2022/6/30 17:19
 */
@RestController
public class TestController {
    @Value("${name}")
    private String name;
    @Value("${age}")
    private String age;

    @GetMapping("/test")
    public String test() {
        System.out.println("name:" + name);
        System.out.println("age:" + age);
        return "读取apollo参数  name："+name+" , age: "+age+"";
    }

}
```

访问页面结果：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728695965.png)  
读取成功！！！

### 4.其他操作 ###

#### （1）本地缓存 ####

Apollo客户端会把从服务端获取到的配置在本地文件系统缓存一份，用于在遇到服务不可用，或网络不通的时候，依然能从本地恢复配置，不影响应用正常运行。  
Mac/Linux: /opt/data/\{appId\}/config-cache  
Windows: C:\\opt\\data\{appId\}\\config-cache

> 更改方式：  
> 配置文件：在Spring Boot的application.properties或bootstrap.properties中指定apollo.cache-dir=/opt/data/some-cache-dir(1.9.0+) 或者 apollo.cacheDir=/opt/data/some-cache-dir(1.9.0之前)  
> 启动jar包设置：java -Dapollo.cache-dir=/opt/data/some-cache-dir -jar xxx.jar(1.9.0+) 或者 java -Dapollo.cacheDir=/opt/data/some-cache-dir -jar xxx.jar(1.9.0之前)

#### （2）动态改变配置环境 ####

在服务运行中，可以自己设定需要读取的配置文件的环境，Environment可以通过以下3种方式的任意一个配置：

1.通过Java System Property：可以通过Java的System Property env来指定环境  
在Java程序启动脚本中，可以指定\*\*-Denv=YOUR-ENVIRONMENT\*\*  
如果是运行jar文件，需要注意格式是**java -Denv=YOUR-ENVIRONMENT -jar xxx.jar**  
注意key为全小写  
通过操作系统的System Environment

2.还可以通过操作系统的System Environment ENV来指定  
注意key为全大写

3.最后一个推荐的方式是通过配置文件来指定env=YOUR-ENVIRONMENT  
对于Mac/Linux，默认文件位置为/opt/settings/server.properties  
对于Windows，默认文件位置为C:\\opt\\settings\\server.properties  
文件内容格式：  
env=DEV

## （三）命名空间 ##

apollo 中的所有配置都有从属的 namespace ，而 namespace 有两种类型： public 和 private ，区别  
如下：

> public:  
> 公共的Namespace的配置能被任何项目读取  
> 通过创建公共Namespace可以实现公共组件的配置，或多个应用共享同一份配置的需求  
> 如果其它应用需要覆盖公共部分的配置，可以在其它应用那里关联公共Namespace，然后在关联的  
> Namespace里面配置需要覆盖的配置即可  
> 如果其它应用不需要覆盖公共部分的配置，那么就不需要在其它应用那里关联公共Namespace  
> private:  
> 私有Namespace的配置只能被所属的应用获取到  
> 通过创建一个私有的Namespace可以实现分组管理配置  
> 私有Namespace的格式可以是xml、yml、yaml、json、txt. 您可以通过apollo-client中ConfigFile  
> 接口来获取非properties格式Namespace的内容  
> 1.3.0及以上版本的apollo-client针对yaml/yml提供了更好的支持，可以通过  
> ConfigService.getConfig(“someNamespace.yml”)直接获取Config对象，也可以通过  
> @EnableApolloConfig(“someNamespace.yml”)或  
> apollo.bootstrap.namespaces=someNamespace.yml注入yml配置到Spring/SpringBoot中去

1.在应用1中新建一个公共命名空间test-pubic，添加配置public.name = 张三  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728696363.png)  
2.在应用2中关联应用1中的公共命名空间test-public,并覆盖配置修改名字为李四，这时可以看到修改是隔离的只在应用2中生效。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/17/xxf-1660728696803.png)

### （四）Springboot集成配置 ###

可以在springboot配置文件中将以上配置集成：

```yml
#顶用apollo配置中心
app:
  id: dbh123-test-server
apollo:
  bootstrap:
    enabled: true #是否开始apollo配置预加载功能
    namespaces: application , test1-private , test2-private #指定命名空间
    eagerLoad:
      enabled: true #是否开始apollo支持日志级别的加载时机
  meta: http://192.168.17.102:8080 # apollo meta server地址 这里必须确定环境
```


[0ef72a0afe014200a6e727bbdbd1e6d0.png]: https://img-blog.csdnimg.cn/0ef72a0afe014200a6e727bbdbd1e6d0.png
[e464a4b01a4247d0a8a9e880075e4a98.png]: https://img-blog.csdnimg.cn/e464a4b01a4247d0a8a9e880075e4a98.png
[a31a5dece2924285bacf492461b19a07.png]: https://img-blog.csdnimg.cn/a31a5dece2924285bacf492461b19a07.png
[5f3830edf53a4eadb4fc7bd2053c31e6.png]: https://img-blog.csdnimg.cn/5f3830edf53a4eadb4fc7bd2053c31e6.png
[a72c136570e04f8680986a42362f9fcd.png]: https://img-blog.csdnimg.cn/a72c136570e04f8680986a42362f9fcd.png
[Link 1]: https://github.com/apolloconfig/apollo-build-scripts/tree/master/sql
[465dcbf24dfd40d095c6bb73dd87f0bb.png]: https://img-blog.csdnimg.cn/465dcbf24dfd40d095c6bb73dd87f0bb.png
[4711f3c6b751475ea98fc5b15c78287e.png]: https://img-blog.csdnimg.cn/4711f3c6b751475ea98fc5b15c78287e.png
[eb9c1bf4525444d7adb5c367a7f5efba.png]: https://img-blog.csdnimg.cn/eb9c1bf4525444d7adb5c367a7f5efba.png
[8a29be01d9e54337b74d11ee4a14ced0.png]: https://img-blog.csdnimg.cn/8a29be01d9e54337b74d11ee4a14ced0.png
[51d3736ca10a404bbcb778532839cb37.png]: https://img-blog.csdnimg.cn/51d3736ca10a404bbcb778532839cb37.png
[7c40027275044f48adde0b12364bdc83.png]: https://img-blog.csdnimg.cn/7c40027275044f48adde0b12364bdc83.png
[f87dcf0bd43f43329fc572cc9c94fb5d.png]: https://img-blog.csdnimg.cn/f87dcf0bd43f43329fc572cc9c94fb5d.png
[c8448e27db0d45dba27bb56f1f8b8ab8.png]: https://img-blog.csdnimg.cn/c8448e27db0d45dba27bb56f1f8b8ab8.png
[cb72d02ed5774191a6c6a0b2958dfb90.png]: https://img-blog.csdnimg.cn/cb72d02ed5774191a6c6a0b2958dfb90.png
[be1fc3613a6a49518772d1bee8a4d081.png]: https://img-blog.csdnimg.cn/be1fc3613a6a49518772d1bee8a4d081.png
[d232c531b3704343ac9682fa572e8315.png]: https://img-blog.csdnimg.cn/d232c531b3704343ac9682fa572e8315.png
[72d98dee5c8c4f61a4cb3702876ba7ab.png]: https://img-blog.csdnimg.cn/72d98dee5c8c4f61a4cb3702876ba7ab.png
[Link 2]: https://github.com/nobodyiam/apollo-build-scripts
[0b4bb621dfef46ffb0b5bf6fb89d10b2.png]: https://img-blog.csdnimg.cn/0b4bb621dfef46ffb0b5bf6fb89d10b2.png
[a36f38274acf438385160c59631c9950.png]: https://img-blog.csdnimg.cn/a36f38274acf438385160c59631c9950.png
[dbab55fb28604f40b6175ee228e95e64.png]: https://img-blog.csdnimg.cn/dbab55fb28604f40b6175ee228e95e64.png
[513c9cf776ba42f68a8d6ff6119e7d58.png]: https://img-blog.csdnimg.cn/513c9cf776ba42f68a8d6ff6119e7d58.png
[56efa2d8841749f68d6e63eb525961a1.png]: https://img-blog.csdnimg.cn/56efa2d8841749f68d6e63eb525961a1.png
[1243a522dcea46289de3e1ef10bd15a5.png]: https://img-blog.csdnimg.cn/1243a522dcea46289de3e1ef10bd15a5.png
[a700f78c04ba40b6bca97f5cf23accc5.png]: https://img-blog.csdnimg.cn/a700f78c04ba40b6bca97f5cf23accc5.png
[yml]: https://www.toyaml.com/index.html
[e34acfe617dd4ecd8635bd9a68d3ac83.png]: https://img-blog.csdnimg.cn/e34acfe617dd4ecd8635bd9a68d3ac83.png
[b7dc4a9d4ab54a5688269c25d2c96253.png]: https://img-blog.csdnimg.cn/b7dc4a9d4ab54a5688269c25d2c96253.png
[cf152c81ce484bf098640c94670b4110.png]: https://img-blog.csdnimg.cn/cf152c81ce484bf098640c94670b4110.png
[fa97d17c3a3c4240a0032d3a7c5db326.png]: https://img-blog.csdnimg.cn/fa97d17c3a3c4240a0032d3a7c5db326.png
[e9b5285575a143beafef17592271de58.png]: https://img-blog.csdnimg.cn/e9b5285575a143beafef17592271de58.png

更多内容参考https://www.apolloconfig.com/#/zh/

# 关于笑小枫💕

> 本章到这里结束了，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
>
> 微信公众号：笑小枫
>
> 笑小枫个人博客：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
>
> CSDN：[https://zhangfz.blog.csdn.net
>
> 本文转载自：https://blog.csdn.net/Dbh321/article/details/125533024，如有侵权，请联系删除