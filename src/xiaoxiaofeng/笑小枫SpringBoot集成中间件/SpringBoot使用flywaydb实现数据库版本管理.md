## 1. 项目背景

本文主要是配合[SpringBoot使用用户输入的自定义数据源启动]()一文附带产出。前文主要介绍了SpringBoot无数据源启动，然后通过用户录入自定义数据库配置后，连接数据库的操作。但是当整个项目交给用户使用时，谁使用都不知道情况下，数据源都自己定义的情况下，我们项目升级版本，免不了有数据库文件变更的情况，这个时候，就靠本文介绍的`flyway`来控制数据库版本了。

Flyway是一款开源的数据库版本管理工具，可以实现管理并跟踪数据库变更，支持数据库版本自动升级，而且不需要复杂的配置，能够帮助团队更加方便、合理的管理数据库变更。

例：创建两个sql变更文件，项目启动后会将两个文件中的sql语句全部执行。

## 2. 涉及到技术栈

本文演示涉及到大技术如下：

* Spring Boot      version: 2.7.12
* Mysql                version: 8.0.29
* Mybatis-plus    version: 3.3.2
* flywaydb           version: 5.2.1

demo中引入的依赖

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.12</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.maple</groupId>
    <artifactId>maple-flyway</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>maple-flyway</name>
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
        </dependency>

        <!-- 引入web相关 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--使用Mysql数据库-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.29</version>
        </dependency>

        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
            <version>5.2.1</version>
        </dependency>
        
        <!-- mybatis-plus的依赖 -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.3.2</version>
        </dependency>

        <!--Lombok管理Getter/Setter/log等-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
            <version>1.18.24</version>
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

## 3. 功能实现

本文只是为了演示实现思想，源码只是一个实现的小demo，具体使用还是需要结合自己项目。

### 3.1 实现思想

直接在java工程中嵌入sql脚本。工程重新部署时，会自动更新数据库，保证数据库与代码同步，避免了手动更新数据库带来的弊病。

flywaydb：主要是再第一次启动的时候创建flyway_schema_history表，然后去加载classpath下的文件进行执行，并且再表中记录加载的版本号记录。

### 3.2 具体代码

首先创建一个SpringBoot项目，这里就不展开赘述

添加我们的yml配置文件

~~~yml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/maple?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B8
    driverClassName: com.mysql.cj.jdbc.Driver
    username: root
    password: 123456
  # flyway配置
  flyway:
    # 是否启用flyway
    enabled: true
    # 编码格式，默认UTF-8
    encoding: UTF-8
    # 迁移sql脚本文件存放路径，官方默认db/migration 
    locations: classpath:db/migration
    # 迁移sql脚本文件名称的前缀，默认V
    sql-migration-prefix: V
    # 迁移sql脚本文件名称的分隔符，默认2个下划线__ 
    sql-migration-separator: __
    # 迁移sql脚本文件名称的后缀
    sql-migration-suffixes: .sql
    # 迁移时是否进行校验，默认true  
    validate-on-migrate: true
    # 当迁移发现数据库非空且存在没有元数据的表时，自动执行基准迁移，新建schema_version表
    baseline-on-migrate: true
    # 是否关闭要清除已有库下的表功能,生产环境必须为true,否则会删库
    clean-disabled: true
~~~

具体的配置规则我们会在后面讲到

在resources目录下创建我们存放sql的目录`db/migration`,这个是官方默认的路径,我们可以自己进行修改调整

创建sql文件`V20230328_01__user.sql`

~~~sql
CREATE TABLE `maple_user`
(
    `id`        BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_name` VARCHAR(64) NOT NULL COMMENT '登录账号',
    `password`  VARCHAR(64) NOT NULL COMMENT '登录密码',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT='用户信息' COLLATE='utf8_general_ci' ENGINE=InnoDB;
~~~

此时，整个配置就完成了，如下图所示

![image-20230601163312630](https://image.xiaoxiaofeng.site/blog/2023/06/01/xxf-20230601163320.png?xxfjava)

## 4. 系统测试

接下来，我们对系统进行一下测试，直接启动项目即可.

![image-20230601164416719](https://image.xiaoxiaofeng.site/blog/2023/06/01/xxf-20230601164416.png?xxfjava)

接下来，我们去看一下我们的数据库，可以看到生成的表,其中`flyway_schema_history`是flyway自动生成的表，用于标记sql是否执行了，执行结果等信息,`maple_user`是我们需要生成的业务表

![image-20230601165225685](https://image.xiaoxiaofeng.site/blog/2023/06/01/xxf-20230601165225.png?xxfjava)

到这里本文就结束了。

## 5. flyway配置讲解

### 5.1 sql脚本命名规则

1. 仅需要执行一次的，以大写“V”开头，V+版本后(版本号间的数字以“.” 或者“ _ ”分隔开，“ _ ”会自动编译
成` “ . ” )+" __"+文件描述+后缀名`
2. 需要执行多次的，以大写“R”开头，命名如R__clean.sql ，R的脚本只要改变了就会执行,R不带版本号
3. V开头的比R开头的优先级要高。


~~~
前缀：用于版本控制（可配置）、撤消（可配置）和可重复迁移（可配置）VUR)
版本：带有点或下划线的版本可根据需要分隔任意数量的部分（不适用于可重复的迁移）
分隔符：（两个下划线）（可配置）__)
说明：下划线或空格分隔单词
后缀：（可配置.sql)
（可选）版本控制 SQL 迁移还可以省略分隔符和说明
~~~

### 5.2 flyway配置详解
flyway.baseline-description对执行迁移时基准版本的描述.
flyway.baseline-on-migrate当迁移时发现目标schema非空，而且带有没有元数据的表时，是否自动执
行基准迁移，默认false.
flyway.baseline-version开始执行基准迁移时对现有的schema的版本打标签，默认值为1.
flyway.check-location检查迁移脚本的位置是否存在，默认false.
flyway.clean-on-validation-error当发现校验错误时是否自动调用clean，默认false.
flyway.enabled是否开启flywary，默认true.
flyway.encoding设置迁移时的编码，默认UTF-8.
flyway.ignore-failed-future-migration当读取元数据表时是否忽略错误的迁移，默认false.
flyway.init-sqls当初始化好连接时要执行的SQL.
flyway.locations迁移脚本的位置，默认db/migration.
flyway.out-of-order是否允许无序的迁移，默认false.
flyway.password目标数据库的密码.
flyway.placeholder-prefix设置每个placeholder的前缀，默认${.
flyway.placeholder-replacementplaceholders是否要被替换，默认true.
flyway.placeholder-suffix设置每个placeholder的后缀，默认}.
flyway.placeholders.[placeholder name]设置placeholder的value
flyway.schemas设定需要flywary迁移的schema，大小写敏感，默认为连接默认的schema.
flyway.sql-migration-prefix迁移文件的前缀，默认为V.
flyway.sql-migration-separator迁移脚本的文件名分隔符，默认`__`
flyway.sql-migration-suffix迁移脚本的后缀，默认为.sql
flyway.tableflyway使用的元数据表名，默认为schema_version
flyway.target迁移时使用的目标版本，默认为latest version
flyway.url迁移时使用的JDBC URL，如果没有指定的话，将使用配置的主数据源
flyway.user迁移数据库的用户名
flyway.validate-on-migrate迁移时是否校验，默认为true

## 6. 项目源码

本文到此就结束了，如果帮助到你了，帮忙点个赞👍

本文源码：[https://github.com/hack-feng/maple-product/tree/main/maple-flyway](https://github.com/hack-feng/maple-product/tree/main/maple-flyway)

>  🐾我是笑小枫，全网皆可搜的【笑小枫】
