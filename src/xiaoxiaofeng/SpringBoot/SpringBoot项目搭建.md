## 1 搭建SpringBoot基础框架

前置依赖：

1. 装有java开发环境
2. 装有maven环境

### 1.1 创建SpringBoot项目

首先打开IDEA, 创建一个最基础的SpringBoot项目，详细步骤如下：

 (1) 选择File->New->Project...，如下图：
 ![CSDN-笑小枫](http://file.xiaoxiaofeng.site/blog/image/1.1-1.jpg)

 (2) 选择Spring Initializr，选择对应的JDK 1.8的版本，默认地址Default(https://start.spring.io/)创建
 ![CSDN-笑小枫](http://file.xiaoxiaofeng.site/blog/image/1.1-2.jpg)

 当然我们也可以直接在网页上创建，打开https://start.spring.io/网址对应的信息如下：
 ![CSDN-笑小枫](http://file.xiaoxiaofeng.site/blog/image/1.1-6.jpg)

 (3) 填写项目对应的信息

 * Group：Maven坐标-项目组织唯一标识符
 * Artifact：Maven坐标-项目唯一标识符
 * Type：这里使用Maven管理项目
 * Language：语言选择Java
 * Packaging：打包方式，可以是jar或war形式
 * Java Version：Java版本，选择8
 * Version：项目版本号，根据自己喜好来，初始可以使用默认
 * Name：项目名称，通常和Artifact一致
 * Dependencies：项目描述
 * package：项目包结构，这里可以根据自己喜好定义
   ![CSDN-笑小枫](http://file.xiaoxiaofeng.site/blog/image/1.1-3.jpg)

 (4) 选择Spring Boot的版本和需要引入的依赖，这里可以不做修改，等项目创建成功后，统一在pom.xml中配置
 ![CSDN-笑小枫](http://file.xiaoxiaofeng.site/blog/image/1.1-4.jpg)

 (5) 选择项目保存的目录，点击Finish,项目创建完成
 ![CSDN-笑小枫](http://file.xiaoxiaofeng.site/blog/image/1.1-5.jpg)

 (6) 修改项目的pom.xml文件中的<parent>为我们对应的版本信息

 ~~~
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.0.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
 ~~~

完整pom.xml文件：

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
 <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
     <modelVersion>4.0.0</modelVersion>
     <parent>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-parent</artifactId>
         <version>2.1.0.RELEASE</version>
         <relativePath/> <!-- lookup parent from repository -->
     </parent>
     <groupId>com.maple</groupId>
     <artifactId>maple-demo</artifactId>
     <version>0.0.1-SNAPSHOT</version>
     <name>maple-demo</name>
     <description>Demo project for Spring Boot</description>
 
     <properties>
         <java.version>1.8</java.version>
     </properties>
 
     <dependencies>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter</artifactId>
         </dependency>
 
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-test</artifactId>
             <scope>test</scope>
             <exclusions>
                 <exclusion>
                     <groupId>org.junit.vintage</groupId>
                     <artifactId>junit-vintage-engine</artifactId>
                 </exclusion>
             </exclusions>
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
~~~

 (7)等待项目下载依赖，此时SpringBoot项目差创建完成，对应的结构如下：
 ![CSDN-笑小枫](http://file.xiaoxiaofeng.site/blog/image/1.1-7.jpg)

### 1.2 配置SpringBoot项目

(1) 接下来，我们配置一下我们创建的SpringBoot项目，这是一个web项目，首先我们需要引入web对应的jar包。

在pom.xml文件中添加对应的依赖：

~~~
<!-- 引入web相关 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
~~~

完整pom.xml如下

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.0.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.maple</groupId>
    <artifactId>maple-demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>maple-demo</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!-- 引入web相关 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
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
~~~

(2) 修改application.properties后缀为application.yml，添加启动的端口号：6666

~~~
server:
  port: 6666
~~~

(3) 修改MapeDemoApplicationTests.java文件，因为创建是高版本的SpringBoot，所以重新引入一下依赖。

### 1.3 启动SpringBoot项目

(1) 在com.maple.demo下创建TestController.java文件，用于测试项目启动
代码信息:

~~~java
package com.maple.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Maple
 * @date 2020/10/13 14:09
 **/
@RestController
public class TestController {

    @GetMapping(value = "test")
    public String test(){
        return "项目启动成功";
    }
}
~~~

(2) 启动项目

可以直接在启动类上进行启动，启动成功后，详情如下：
 ![CSDN-笑小枫](http://file.xiaoxiaofeng.site/blog/image/1.3-1.jpg)

(3) 项目启动成功，在浏览器输入http://127.0.0.1:6666/test地址进行测试，返回结果如下：
 ![CSDN-笑小枫](http://file.xiaoxiaofeng.site/blog/image/1.3-2.jpg)

### 1.4 常用工具类

在我们日常开发中，我们肯定会用到各式各样的插件、工具。例如：链接数据库、使用Redis、导出Excel、生成Word等等，这些我们在后续会一一提到。

### 1.5 小结

本章主要介绍了如果搭建一个简单的Spring Boot项目，并启动项目。

