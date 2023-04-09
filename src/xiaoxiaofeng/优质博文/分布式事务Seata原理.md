>Seata 是一款开源的分布式事务解决方案，致力于提供高性能与简单易用的分布式事务服务，为用户提供了 AT、TCC、SAGA 和 XA 几种不同的事务模式。Seata AT模式是基于XA事务演进而来，需要数据库支持。AT 模式的特点就是对业务无入侵式，用户只需要关注自己的业务SQL，Seata 框架会在第一阶段拦截并解析用户的 SQL，并保存其变更前后的数据镜像，形成undo log，并自动生成事务第二阶段的提交和回滚操作。

# 一、Seata 介绍： #

## 1、Seata 简介： ##

Seata 是一款开源的分布式事务解决方案，致力于提供高性能与简单易用的分布式事务服务，为用户提供了 AT、TCC、SAGA 和 XA 几种不同的事务模式：

 *  AT模式：无侵入式的分布式事务解决方案，适合不希望对业务进行改造的场景，但由于需要添加全局事务锁，对影响高并发系统的性能。该模式主要关注多DB访问的数据一致性，也包括多服务下的多DB数据访问一致性问题
 *  TCC模式：高性能的分布式事务解决方案，适用于对性能要求比较高的场景。该模式主要关注业务拆分，在按照业务横向扩展资源时，解决服务间调用的一致性问题
 *  Saga模式：长事务的分布式事务解决方案，适用于业务流程长且需要保证事务最终一致性的业务系统。Saga 模式一阶段就会提交本地事务，无锁，长流程情况下可以保证性能，多用于渠道层、集成层业务系统，事务参与者可以是其它公司的服务也可以是遗留系统的服务，并且对于无法进行改造和提供 TCC 要求的接口，也可以使用 Saga 模式

## 2、Seata 的核心组件： ##

在 Seata 中主要有以下三种角色，其中 TM 和 RM 是作为 Seata 的客户端与业务系统集成在一起，TC 作为 Seata 的服务端独立部署：

>  *  事务协调器（TC）：维护全局事务的运行状态，负责协调并驱动全局提交或回滚
>  *  事务管理器（TM）：事务发起方，控制全局事务的范围，负责开启一个全局事务，并最终发起全局提交或回滚全局的决议
>  *  资源管理器（RM）：事务参与方，管理本地事务正在处理的资源，负责向 TC 注册本地事务、汇报本地事务状态，接收 TC 的命令来驱动本地事务的提交或回滚

## 3、Seata 的整体执行流程： ##

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550458490.png)

Seata 分布式事务的整体执行机制如上图所示，可以大致分为两阶段提交：

 *  ① 发起方 TM 向 TC 申请开启一个全局事务，全局事务创建成功并生成唯一的全局事务标识 XID，该 XID 在后续事务的服务调用链路的上下文传播（通过Aop实现)）
 *  ② RM 向 TC 注册分支事务，汇报资源准备状况，并与 XID 进行绑定（Branch分支事务指分布式事务中每个独立的本地局部事务）
 *  ③ TM 向 TC 发起 XID 下的所有分支事务的全局提交或回滚请求（事务一阶段结束）
 *  ④ TC 汇总事务信息，决定分布式事务是提交还是回滚；
 *  ⑤ TC 通知所有 RM 提交/回滚 资源，事务二阶段结束；

# 二、Seata 的 AT 模式原理： #

Seata AT模式是基于XA事务（XA是基于数据库实现的分布式事务协议）演进而来，需要数据库支持，如果是 MySQL，则需要5.6以上版本才支持XA协议。AT 模式的特点就是对业务无入侵式，用户只需要关注自己的业务SQL，Seata 框架会在第一阶段拦截并解析用户的 SQL，并保存其变更前后的数据镜像，形成undo log，并自动生成事务第二阶段的提交和回滚操作。

## 1、AT 模式的整体执行流程： ##

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550458848.png)

AT 模式 RM 驱动分支事务的行为分为以下两个阶段：

（1）执行阶段：

 *  （1）代理 JDBC 数据源，拦截并解析业务 SQL，生成更新前后的镜像数据，形成 UNDO LOG。
 *  （2）向 TC 注册分支。
 *  （3）分支注册成功后，把业务数据的更新和 UNDO LOG 放在同一个本地事务中提交。

（2）完成阶段：

 *  全局提交，收到 TC 的分支提交请求，异步删除相应分支的 UNDO LOG。
 *  全局回滚，收到 TC 的分支回滚请求，查询分支对应的 UNDO LOG 记录，生成补偿回滚的 SQL 语句，执行分支回滚并返回结果给 TC

## 2、AT 模式两阶段详细流程： ##

### **2.1、第一阶段的详细执行流程：** ###

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550459219.png)

在第一阶段，RM 写表时，Seata 通过代理数据源（从而达到对业务无侵入的效果）拦截业务 SQL 并 解析 SQL 语义，找到该 SQL 要更新的业务数据保存成 before image（前置镜像），然后执行业务SQL，在业务数据更新后，再将其保存成 after image（后置镜像），最后生成行锁。通过把更新前后的业务数据数据镜像组织成回滚日志 undo log，并利用本地事务的 ACID 特性，将业务数据的更新和回滚日志 undo log 的写入在同一个本地事务中提交，保证任何提交的业务数据的更新一定有相应的回滚日志存在（即操作的原子性）。

基于这样的机制，分支的本地事务就可以在全局事务的第一阶段提交，并马上释放本地事务锁定的资源，这也是 Seata 的 AT 模式和 XA 事务的不同之处，两阶段提交往往对资源的锁定需要持续到第二阶段实际的提交或者回滚操作，而有了回滚日志之后，可以在第一阶段释放对资源的锁定，降低了锁范围，提高效率，即使第二阶段发生异常需要回滚，只需找对 undo log 中对应数据镜像并反解析成 SQL 来达到回滚目的

### **2.2、第二阶段提交的详细执行流程：** ###

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550459545.png)

如果二阶段的全局表决结果是提交的话，说明所有分支事务的业务SQL已经在第一阶段生效，此时 Seata 框架只需异步删除所有分支第一阶段保存的镜像数据、回滚日志和行锁，完成数据清理即可，这个过程是非常快速的

### **2.3、第二阶段回滚的详细执行流程：** ###

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550460070.png)

如果第二阶段是回滚的话，Seata 就需要回滚第一阶段已执行的 SQL 进行还原业务数据。由 TC 通知所有 RM 进行根据第一阶段的回滚日志 undo log （即before image）进行反向补偿，RM 收到 TC 发来的回滚请求后，通过 XID 和 Branch ID 找到相应的回滚日志记录，通过undo log 生成反向的更新 SQL 并执行，以完成分支的业务数据还原，最后删除 undo log、redo log 和行锁。但在还原前需要校验脏写，对比数据库”当前业务数据”和 “after image”，如果两份数据完全一致就说明没有脏写，可以还原业务数据，如果不一致就说明有脏写，出现脏写就需要转人工处理。

# 三、Seata 的 TCC、Saga、XA模式原理： #

文章第二部分介绍了 Seata 的 AT 模式，接下来我们就介绍下 Seata 的其实几种事务模式：

## 1、TCC 模式： ##

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550460416.png)

TCC 模式 RM 驱动分支事务的行为分为以下两个阶段：

（1）执行阶段：

 *  ① 向 TC 注册分支。
 *  ② 执行业务定义的 Try 方法。
 *  ③ 向 TC 上报 Try 方法执行情况：成功或失败。

（2）完成阶段：

 *  全局提交，收到 TC 的分支提交请求，执行业务定义的 Confirm 方法。
 *  全局回滚，收到 TC 的分支回滚请求，执行业务定义的 Cancel 方法。

## 2、Saga 模式： ##

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550460772.png)

Saga 模式 RM 驱动分支事务的行为包含以下两个阶段：

（1）执行阶段：

 *  ① 向 TC 注册分支。
 *  ② 执行业务方法。
 *  ③ 向 TC 上报业务方法执行情况：成功或失败。

（2）完成阶段：

 *  全局提交，RM 不需要处理。
 *  全局回滚，收到 TC 的分支回滚请求，执行业务定义的补偿回滚方法。

## 3、XA模式： ##

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550461309.png)

XA 模式 RM 驱动分支事务的行为包含以下两个阶段：

（1）执行阶段：

 *  ① 向 TC 注册分支
 *  ② XA Start，执行业务 SQL，XA End
 *  ③ XA prepare，并向 TC 上报 XA 分支的执行情况：成功或失败

（2）完成阶段：

 *  收到 TC 的分支提交请求，XA Commit
 *  收到 TC 的分支回滚请求，XA Rollback

> 参考文章：
> 
> [https://help.aliyun.com/document\_detail/157850.html][https_help.aliyun.com_document_detail_157850.html]
> 
> [https://blog.csdn.net/k6T9Q8XKs6iIkZPPIFq/article/details/107273472][https_blog.csdn.net_k6T9Q8XKs6iIkZPPIFq_article_details_107273472]

# 四、SpringCloud Alibaba 整合 Seata AT 模式： #

## 1、搭建 Seata TC 协调者： ##

### 1.1、下载 Seata TC 协调者： ###

Seata 的协调者其实就是阿里开源的一个服务，我们只需要下载（下载地址：[https://github.com/seata/seata/releases][https_github.com_seata_seata_releases]）并启动它，下载完成后，直接解压即可，但是此时还不能直接运行，还需要做一些配置

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550461720.png)

### 1.2、创建TC所需要的表： ###

> [https://github.com/seata/seata/tree/1.4.2/script/server/db][https_github.com_seata_seata_tree_1.4.2_script_server_db]

TC 运行需要将事务信息保存在数据库，因此需要创建一些表，找到 seata-1.4.2 源码的 script\\server\\db 这个目录，将会看到以下SQL文件：

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550462037.png)

以 MySQL 数据库为例，创建数据库 seata，并执行 mysql.sql 文件中的sql语句：

```sql
CREATE TABLE IF NOT EXISTS `global_table`
(
    `xid`                       VARCHAR(128) NOT NULL,
    `transaction_id`            BIGINT,
    `status`                    TINYINT      NOT NULL,
    `application_id`            VARCHAR(32),
    `transaction_service_group` VARCHAR(32),
    `transaction_name`          VARCHAR(128),
    `timeout`                   INT,
    `begin_time`                BIGINT,
    `application_data`          VARCHAR(2000),
    `gmt_create`                DATETIME,
    `gmt_modified`              DATETIME,
    PRIMARY KEY (`xid`),
    KEY `idx_gmt_modified_status` (`gmt_modified`, `status`),
    KEY `idx_transaction_id` (`transaction_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- the table to store BranchSession data
CREATE TABLE IF NOT EXISTS `branch_table`
(
    `branch_id`         BIGINT       NOT NULL,
    `xid`               VARCHAR(128) NOT NULL,
    `transaction_id`    BIGINT,
    `resource_group_id` VARCHAR(32),
    `resource_id`       VARCHAR(256),
    `branch_type`       VARCHAR(8),
    `status`            TINYINT,
    `client_id`         VARCHAR(64),
    `application_data`  VARCHAR(2000),
    `gmt_create`        DATETIME(6),
    `gmt_modified`      DATETIME(6),
    PRIMARY KEY (`branch_id`),
    KEY `idx_xid` (`xid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- the table to store lock data
CREATE TABLE IF NOT EXISTS `lock_table`
(
    `row_key`        VARCHAR(128) NOT NULL,
    `xid`            VARCHAR(128),
    `transaction_id` BIGINT,
    `branch_id`      BIGINT       NOT NULL,
    `resource_id`    VARCHAR(256),
    `table_name`     VARCHAR(32),
    `pk`             VARCHAR(36),
    `gmt_create`     DATETIME,
    `gmt_modified`   DATETIME,
    PRIMARY KEY (`row_key`),
    KEY `idx_branch_id` (`branch_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
```

创建的三张表如下图：

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550462341.png)

 *  global\_table：全局事务表，每当有一个全局事务发起后，就会在该表中记录全局事务的ID
 *  branch\_table：分支事务表，记录每一个分支事务的ID，分支事务操作的哪个数据库等信息
 *  lock\_table：全局锁

### 1.3、修改TC的注册中心和配置中心： ###

找到 seata-server-1.4.2\\seata\\conf 目录，其中有一个 registry.conf 文件，其中配置了TC的注册中心和配置中心。默认的注册中心是 file 形式，实际使用中肯定不能使用，需要改成Nacos形式；同样 Seate 的 TC 的配置中心默认也是使用 file 形式，需要修改为 nacos 作为配置中心：

```
registry {
  # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
  type = "nacos"
  nacos {
    application = "seata-server"
    serverAddr = "localhost:8848"
    namespace = "XXXXXXXXXX"
    cluster = "default"
    username = "nacos"
    password = "nacos"
  }
}

config {
  # file、nacos 、apollo、zk、consul、etcd3
  type = "nacos"
  nacos {
    serverAddr = "localhost:8848"
    namespace = "XXXXXXXXXX"
    group = "SEATA_GROUP"
    username = "nacos"
    password = "nacos"
  }
}
```

需要改动的地方如下：

 *  type：改成nacos，表示使用nacos作为注册中心或者配置中心
 *  application：服务的名称
 *  serverAddr：nacos的地址
 *  group：分组
 *  namespace：命名空间
 *  username：用户名
 *  password：密码

上述配置修改好之后，在TC启动的时候将会自动读取 nacos 的配置，最后这份 registry.conf 文件的配置需要与下文每个seata客户端项目中的配置一致

### 1.4、导入 seata Server 配置： ###

> 建议将 seata 项目下载到本地并阅读 [https://github.com/seata/seata/tree/1.4.2/script/config-center][https_github.com_seata_seata_tree_1.4.2_script_config-center] 路径下的README.md内容

在上面我们已经配置好 seata TC 的配置中心，那么 TC 需要存储到 Nacos 的配置都有哪些，如何推送过去呢？在 [https://github.com/seata/seata/tree/1.4.2/script/config-center][https_github.com_seata_seata_tree_1.4.2_script_config-center] 中有一个 confIg.txt 文件，其中就是 TC 需要的全部配置，而 [https://github.com/seata/seata/tree/1.4.2/script/config-center/nacos][https_github.com_seata_seata_tree_1.4.2_script_config-center_nacos] 中有一个 nacos-config.sh 脚本能够将 config.txt 中的全部配置自动推送到nacos中。

我们先启动nacos服务，然后在 nacos-config.sh 目录下执行下面命令，将 config.txt 中的配置信息推送到 nacos 中：

> \# -h 代表nacos服务的IP；-p 代表nacos的端口号；-g 分组信息；-t 命名空间ID；-u 用户名，-p 密码
> 
> $ sh nacos-config.sh -h 127.0.0.1 -p 8080 -g SEATA\_GROUP -t 7a7581ef-433d-46f3-93f9-5fdc18239c65 -u nacos -w nacos

命令执行成功后，就可以在nacos中看到如下配置：

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550462653.png)

### 1.5、修改TC的事务信息存储方式： ###

seata 的 TC 端的事务信息存储模式（store.mode）现有file、db、redis三种，file模式无需改动，直接启动即可，下面专门讲下db和redis启动步骤。

> 注： file模式为单机模式，全局事务会话信息内存中读写并持久化本地文件root.data，性能较高；

（1）以DB模式存储：

db模式为高可用模式，全局事务会话信息通过db共享，相应性能差些；上一节的内容已经将所有的配置信息都推送到了Nacos中，TC启动时会从Nacos中读取，因此我们修改也需要在Nacos中修改。需要修改的配置如下：

    ## 采用db的存储形式
    store.mode=db
    ## druid数据源
    store.db.datasource=druid
    ## mysql数据库
    store.db.dbType=mysql
    ## mysql驱动
    store.db.driverClassName=com.mysql.jdbc.Driver
    ## TC的数据库url
    store.db.url=jdbc:mysql://127.0.0.1:3306/seata_server?useUnicode=true
    ## 用户名
    store.db.user=root
    ## 密码
    store.db.password=Nov2014

在nacos中搜索上述的配置，直接修改其中的值，比如修改 store.mode，如下图：

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550463056.png)

（2）以Redis模式存储：

redis模式 Seata-Server 1.3 及以上版本支持，性能较高，但存在事务信息丢失风险，所以需要提前配置合适当前场景的redis持久化配置，该模式需改动以下配置：

    store.mode=redis
    store.redis.host=127.0.0.1
    store.redis.port=6379
    store.redis.password=123456

### 1.6、启动TC： ###

按照上述步骤全部配置成功后，则可以启动TC，在 seata-server-1.4.2\\bin 目录下直接点击 seata-server.bat（windows）运行，启动成功后，启动成功后，在Nacos的服务列表中则可以看到TC已经注册进入，如下图：

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550463368.png)

至此，Seata 的 TC 就启动完成了............

## 2、搭建Seata 客户端（RM）： ##

### 2.1、maven 添加 seata 依赖： ###

```xml
<dependency>
   <groupId>com.alibaba.cloud</groupId>
   <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
   <!-- 排除依赖，指定版本和服务端一致 -->
    <exclusions>
       <exclusion>
           <groupId>io.seata</groupId>
           <artifactId>seata-spring-boot-starter</artifactId>
       </exclusion>
       <exclusion>
           <groupId>io.seata</groupId>
           <artifactId>seata-all</artifactId>
       </exclusion>
   </exclusions>
</dependency>
<dependency>
    <groupId>io.seata</groupId>
    <artifactId>seata-spring-boot-starter</artifactId>
    <version>1.4.2</version>
</dependency>
<dependency>
   <groupId>io.seata</groupId>
   <artifactId>seata-all</artifactId>
   <version>1.4.2</version>
</dependency>
```

> 注意：seata客户端的依赖版本必须要和服务端一致。

### 2.2、application.yml 添加 seate 配置： ###

```yml
seata:
  # 这里要特别注意和nacos中配置的要保持一致，建议配置成 ${spring.application.name}-tx-group
  tx-service-group: my_test_tx_group
  registry:
    type: nacos
    nacos:
      # 配置所在命名空间ID，如未配置默认public空间
      server-addr: 127.0.0.1:8848
      namespace: XXXXXXXXXX
      group: SEATA_GROUP
      application: seata-server
      userName: nacos
      password: nacos
  config:
    type: nacos
    nacos:
      server-addr: 127.0.0.1:8848
      namespace: XXXXXXXXXX
      group: SEATA_GROUP
      userName: nacos
      password: nacos
```

tx-service-group 配置的值可以自定义，但是定义后需要在 nacos 配置中心新增 service.vgroupMapping.xxx=default 的配置，该属性一定一定要和 seata 服务端的配置一致，否则不生效；比如上述配置中的，就需要在 nacos 配置中心新增一个配置项 service.vgroupMapping.my\_test-tx-group=default，并且设置分组为SEATA\_GROUP，如下图：

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/3/23/xxf-1679550463722.png)

> 注意：my\_test-tx-group 仅仅是后缀，在nacos中添加配置时记得要加上前缀 service.vgroupMapping.

### 2.3、数据库中添加回滚日志表： ###

> [https://github.com/seata/seata/tree/1.4.2/script/client/at/db][https_github.com_seata_seata_tree_1.4.2_script_client_at_db]

回滚日志表：undo\_log，这是Seata要求必须有的，每个业务库都应该创建一个，SQL如下：

```sql
CREATE TABLE IF NOT EXISTS `undo_log`
(
    `branch_id`     BIGINT       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11)      NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6)  NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6)  NOT NULL COMMENT 'modify datetime',
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';
```

### 2.4、使用 seata 作为全局事务控制： ###

在分布式业务入口增加全局事务注解 @GlobalTransactional，其他 service 接口无需配置；假设A服务调用B服务，那么就在A服务的方法上面加入，B服务上面不用加

```java
/**
     * seata 的 GlobalTransactional 注解只需要加载分布式业务入口处，其他service接口无需配置，从而实现分布式事务
     */
    @GlobalTransactional
    @PostMapping (value = "addOrder",produces = {"application/json;charset=utf-8"})
    public String addOrder(@RequestParam String title, @RequestParam int price, @RequestParam int shopId, @RequestParam int userId)
    {
        shopOrderService.save(ShopOrderPojo.builder().price(price).title(title).shopId(shopId).userId(userId).build());
        storageFeignService.reduceStorage(shopId, 1);
        return "success";
    }
```

备注：如果你进行异常捕捉，seata 将认为你已进行异常处理，就不会回滚数据了

 * （1）比如如果你配置了@ControllerAdvice将可能导致数据不回滚

 * （2）如果使用 Feign 调用分布式服务并配置了fallback，后面服务抛出异常会直接执行fallback导致无法回滚(rollbackFor = Exception.class)；这时可以在fallback的实现方法内手动调用seata全局回滚，如下所示：

~~~java
    @Override
        public BizResponse insertAge(Integer age) {
            //feign调用接口fallback后需要手动调用全局事务回滚
            try {
                String xid = RootContext.getXID();
                GlobalTransactionContext.reload(xid).rollback();
            } catch (TransactionException e) {
                e.printStackTrace();
            }
            return BizResponse.fail("客户端降级处理insertAge，" + Thread.currentThread().getName());
}
~~~

### 2.5、undo log 表介绍： ###

上面介绍过 Seata 是根据 undo\_log 中的记录来回滚的，但是异常回滚后 undo\_log 表却为空？怎么回事，这是因为 undo\_log 日志被删除了，想要看到undo\_log 表中记录，我们需要打断点来看，在异常还没抛出时打断点，然后看下数据库 undo\_log 表中数据情况：

> undo log 表简单介绍：[https://blog.csdn.net/hosaos/article/details/89136666][https_blog.csdn.net_hosaos_article_details_89136666]

--------------------

> 相关阅读：
> 
> [常见的服务器架构入门：从单体架构、EAI 到 SOA 再到微服务和 ServiceMesh][EAI _ SOA _ ServiceMesh]
> 
> [常见分布式理论（CAP、BASE）和一致性协议（Gosssip协议、Raft一致性算法）][CAP_BASE_Gosssip_Raft]
> 
> [一致性哈希算法原理详解][Link 1]
> 
> [Nacos注册中心的部署与用法详细介绍][Nacos]
> 
> [Nacos配置中心用法详细介绍][Nacos 1]
> 
> [SpringCloud OpenFeign 远程HTTP服务调用用法与原理][SpringCloud OpenFeign _HTTP]
> 
> [什么是RPC？RPC框架dubbo的核心流程][RPC_RPC_dubbo]
> 
> [服务容错设计：流量控制、服务熔断、服务降级][Link 2]
> 
> [sentinel 限流熔断神器详细介绍][sentinel]
> 
> [Sentinel 规则持久化到 apollo 配置中心][Sentinel _ apollo]
> 
> [Sentinel-Dashboard 与 apollo 规则的相互同步][Sentinel-Dashboard _ apollo]
> 
> [Spring Cloud Gateway 服务网关的部署与使用详细介绍][Spring Cloud Gateway]
> 
> [Spring Cloud Gateway 整合 sentinel 实现流控熔断][Spring Cloud Gateway _ sentinel]
> 
> [Spring Cloud Gateway 整合 knife4j 聚合接口文档][Spring Cloud Gateway _ knife4j]
> 
> [常见分布式事务详解（2PC、3PC、TCC、Saga、本地事务表、MQ事务消息、最大努力通知）][2PC_3PC_TCC_Saga_MQ]
> 
> [分布式事务Seata原理][Seata]
> 
> [RocketMQ事务消息原理][RocketMQ]

--------------------

参考文章：

[Seata 官方文档：https://seata.io/zh-cn/docs/overview/what-is-seata.html][Seata _https_seata.io_zh-cn_docs_overview_what-is-seata.html]

[https://blog.csdn.net/qq\_34162294/article/details/120984951][https_blog.csdn.net_qq_34162294_article_details_120984951]


[abf4eb52020743868df0adf7a9748c3a.png]: https://img-blog.csdnimg.cn/abf4eb52020743868df0adf7a9748c3a.png
[7180cce8ce7e4231a7592efcebb314d4.png]: https://img-blog.csdnimg.cn/7180cce8ce7e4231a7592efcebb314d4.png
[cc92328f23cc4c5b8416c8db052fe569.png]: https://img-blog.csdnimg.cn/cc92328f23cc4c5b8416c8db052fe569.png
[2b450261c8fb447d98fc4ea3a7c2b36a.png]: https://img-blog.csdnimg.cn/2b450261c8fb447d98fc4ea3a7c2b36a.png
[6fbf21bc075a4a34abc01b5b36446e19.png]: https://img-blog.csdnimg.cn/6fbf21bc075a4a34abc01b5b36446e19.png
[581c3248ac5b4f93846c2c7661e2796c.png]: https://img-blog.csdnimg.cn/581c3248ac5b4f93846c2c7661e2796c.png
[5ea641b60e6340019dbde42ca9e18fd4.png]: https://img-blog.csdnimg.cn/5ea641b60e6340019dbde42ca9e18fd4.png
[613ad4ab0d5847ccb8262cf869d661b3.png]: https://img-blog.csdnimg.cn/613ad4ab0d5847ccb8262cf869d661b3.png
[https_help.aliyun.com_document_detail_157850.html]: https://help.aliyun.com/document_detail/157850.html
[https_blog.csdn.net_k6T9Q8XKs6iIkZPPIFq_article_details_107273472]: https://blog.csdn.net/k6T9Q8XKs6iIkZPPIFq/article/details/107273472
[https_github.com_seata_seata_releases]: https://github.com/seata/seata/releases
[7b360315ee154226925ca6a9e3baa422.png]: https://img-blog.csdnimg.cn/7b360315ee154226925ca6a9e3baa422.png
[https_github.com_seata_seata_tree_1.4.2_script_server_db]: https://github.com/seata/seata/tree/1.4.2/script/server/db
[3d1ff59207224a33b8e30fede55885a1.png]: https://img-blog.csdnimg.cn/3d1ff59207224a33b8e30fede55885a1.png
[08371b25e1e1497cad999be4eed9200a.png]: https://img-blog.csdnimg.cn/08371b25e1e1497cad999be4eed9200a.png
[https_github.com_seata_seata_tree_1.4.2_script_config-center]: https://github.com/seata/seata/tree/1.4.2/script/config-center
[https_github.com_seata_seata_tree_1.4.2_script_config-center_nacos]: https://github.com/seata/seata/tree/1.4.2/script/config-center/nacos
[4635be8a287d4ea88cc778d622587740.png]: https://img-blog.csdnimg.cn/4635be8a287d4ea88cc778d622587740.png
[3e0d76d35f5f4a34a8aefa0a01581715.png]: https://img-blog.csdnimg.cn/3e0d76d35f5f4a34a8aefa0a01581715.png
[68d5b687150d4376b07ec79b454d74c9.png]: https://img-blog.csdnimg.cn/68d5b687150d4376b07ec79b454d74c9.png
[9be5ce5a22c64cf7820aa055b3a02e9e.png]: https://img-blog.csdnimg.cn/9be5ce5a22c64cf7820aa055b3a02e9e.png
[https_github.com_seata_seata_tree_1.4.2_script_client_at_db]: https://github.com/seata/seata/tree/1.4.2/script/client/at/db
[https_blog.csdn.net_hosaos_article_details_89136666]: https://blog.csdn.net/hosaos/article/details/89136666
[EAI _ SOA _ ServiceMesh]: https://blog.csdn.net/a745233700/article/details/117448077
[CAP_BASE_Gosssip_Raft]: https://blog.csdn.net/a745233700/article/details/122401700
[Link 1]: https://blog.csdn.net/a745233700/article/details/120814088
[Nacos]: https://blog.csdn.net/a745233700/article/details/122915663
[Nacos 1]: https://blog.csdn.net/a745233700/article/details/122916208
[SpringCloud OpenFeign _HTTP]: https://blog.csdn.net/a745233700/article/details/122916856
[RPC_RPC_dubbo]: https://blog.csdn.net/a745233700/article/details/122445199
[Link 2]: https://blog.csdn.net/a745233700/article/details/120819219
[sentinel]: https://blog.csdn.net/a745233700/article/details/122733366
[Sentinel _ apollo]: https://blog.csdn.net/a745233700/article/details/122725604
[Sentinel-Dashboard _ apollo]: https://blog.csdn.net/a745233700/article/details/122659459
[Spring Cloud Gateway]: https://blog.csdn.net/a745233700/article/details/122917167
[Spring Cloud Gateway _ sentinel]: https://blog.csdn.net/a745233700/article/details/122917160
[Spring Cloud Gateway _ knife4j]: https://blog.csdn.net/a745233700/article/details/122917137
[2PC_3PC_TCC_Saga_MQ]: https://blog.csdn.net/a745233700/article/details/122402303
[Seata]: https://blog.csdn.net/a745233700/article/details/122402795
[RocketMQ]: https://blog.csdn.net/a745233700/article/details/122656108
[Seata _https_seata.io_zh-cn_docs_overview_what-is-seata.html]: https://seata.io/zh-cn/docs/overview/what-is-seata.html
[https_blog.csdn.net_qq_34162294_article_details_120984951]: https://blog.csdn.net/qq_34162294/article/details/120984951