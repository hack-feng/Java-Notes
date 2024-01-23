## 1. 背景

作为SpringBoot集成中间件其中的一篇文章吧，既然打算出这么一个系列了，争取做到虽小却全，又精又美的一个系列吧。

Mybatis Plus作为我入行以来，一直接触的一个中间件，也必须集成一下。同时也为初学者带来一些帮吧。

>  本文拆分自[笑小枫-SpringBoot系列](https://blog.csdn.net/qq_34988304/category_11604043.html)，更为精简的介绍了SpringBoot如何集成中间件。如果想系统的使用SpringBoot，可以参考[笑小枫-SpringBoot系列](https://blog.csdn.net/qq_34988304/category_11604043.html)

## 2. Mybatis Plus简介

`MyBatis-Plus (opens new window)`（简称 MP）是一个`MyBatis (opens new window)`的增强工具，在 `MyBatis` 的基础上只做增强不做改变，为简化开发、提高效率而生。

### 2.1 特性

- **无侵入**：只做增强不做改变，引入它不会对现有工程产生影响，如丝般顺滑
- **损耗小**：启动即会自动注入基本 CURD，性能基本无损耗，直接面向对象操作
- **强大的 CRUD 操作**：内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分 CRUD 操作，更有强大的条件构造器，满足各类使用需求
- **支持 Lambda 形式调用**：通过 Lambda 表达式，方便的编写各类查询条件，无需再担心字段写错
- **支持主键自动生成**：支持多达 4 种主键策略（内含分布式唯一 ID 生成器 - Sequence），可自由配置，完美解决主键问题
- **支持 ActiveRecord 模式**：支持 ActiveRecord 形式调用，实体类只需继承 Model 类即可进行强大的 CRUD 操作
- **支持自定义全局通用操作**：支持全局通用方法注入（ Write once, use anywhere ）
- **内置代码生成器**：采用代码或者 Maven 插件可快速生成 Mapper 、 Model 、 Service 、 Controller 层代码，支持模板引擎，更有超多自定义配置等您来使用
- **内置分页插件**：基于 MyBatis 物理分页，开发者无需关心具体操作，配置好插件之后，写分页等同于普通 List 查询
- **分页插件支持多种数据库**：支持 MySQL、MariaDB、Oracle、DB2、H2、HSQL、SQLite、Postgre、SQLServer 等多种数据库
- **内置性能分析插件**：可输出 SQL 语句以及其执行时间，建议开发测试时启用该功能，能快速揪出慢查询
- **内置全局拦截插件**：提供全表 delete 、 update 操作智能分析阻断，也可自定义拦截规则，预防误操作

### 2.2 支持数据库

> 任何能使用 `MyBatis` 进行 CRUD, 并且支持标准 SQL 的数据库，具体支持情况如下，如果不在下列表查看分页部分教程 PR 您的支持。

- MySQL，Oracle，DB2，H2，HSQL，SQLite，PostgreSQL，SQLServer，Phoenix，Gauss ，ClickHouse，Sybase，OceanBase，Firebird，Cubrid，Goldilocks，csiidb，informix，TDengine，redshift
- 达梦数据库，虚谷数据库，人大金仓数据库，南大通用(华库)数据库，南大通用数据库，神通数据库，瀚高数据库，优炫数据库，星瑞格数据库

### 2.3 框架结构

![img](https://image.xiaoxiaofeng.site/blog/2023/12/21/xxf-20231221132753.jpeg?xxfjava)


## 3. 集成Mybatis Plus

### 3.1 配置基础依赖⚙️

👉首先，项目中会使用到mysql、mybatis-plus等等功能，接下来我们就一一讲解一下。

| 依赖名称                                  | 依赖描述                                  | 版本    |
| ----------------------------------------- | ----------------------------------------- | ------- |
| SpringBoot                                | SpringBoot版本                            | 2.7.12  |
| mysql-connector-java:mysql-connector-java | mysql驱动                                 | 8.0.29  |
| com.baomidou:mybatis-plus-boot-starter    | mybatis-plus的依赖                        | 3.5.2   |
| com.baomidou:mybatis-plus-generator       | mybatis-plus的自动生成代码插件            | 3.5.2   |
| org.apache.velocity:velocity-engine-core  | Java 的模板引擎框架，用于代码自动生成     | 2.3     |
| org.projectlombok:lombok                  | 代码简化，getter/setter、构造器编译时生成 | 1.18.24 |

完整的pom.xml文件如下：👇

~~~xml
 		<!--使用Mysql数据库-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql-connector-version}</version>
        </dependency>
        
        <!-- mybatis-plus的依赖 -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatis-plus-version}</version>
        </dependency>
        
        <!-- mybatis-plus的自动生成代码插件 -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>${mybatis-plus-version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.velocity</groupId>
            <artifactId>velocity-engine-core</artifactId>
            <version>2.3</version>
        </dependency>

        <!--Lombok管理Getter/Setter/log等-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
            <version>1.18.24</version>
        </dependency>
~~~

### 3.2 引入Mysql配置

👉首先我们需要创建一个Mysql数据库maple，字符集选择utf8。这里我们使用的工具是Navicat Premium 11。当然其他工具都是一样，没有特使要求。

![image-20220710141353991](http://file.xiaoxiaofeng.site/blog/image/2022/07/10/20220710141354.png) 

👉创建一张usc_user表，当然有很多字段我们可能暂且不需要，暂且保留，sql如下：👇

~~~mysql
CREATE TABLE `usc_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `account` varchar(30) DEFAULT NULL COMMENT '用户账号',
  `user_name` varchar(30) DEFAULT NULL COMMENT '用户姓名',
  `nick_name` varchar(30) DEFAULT NULL COMMENT '用户昵称',
  `email` varchar(50) DEFAULT NULL COMMENT '用户邮箱',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `sex` char(1) DEFAULT NULL COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT NULL COMMENT '头像地址',
  `salt` varchar(32) DEFAULT NULL COMMENT '用户加密盐值',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户中心-用户信息表';
~~~

👉创建好数据库后，配置我们项目的application.yml文件

~~~yml
server:
  port: 8080

spring:
  # 配置数据库连接
  datasource:
    # 数据库连接地址及常用配置
    url: jdbc:mysql://127.0.0.1:3306/maple?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC
    # 数据库连接驱动
    driverClassName: com.mysql.cj.jdbc.Driver
    # 数据库用户
    username: root
    # 数据库用户密码
    password: 123456

# 配置mybatis-plus的xml和bean的目录
mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  configuration:
    # 开启驼峰功能
    map-underscore-to-camel-case: true
    # 日志打印功能
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  #逻辑删除配置
  global-config:
    db-config:
      # 逻辑已删除值(默认为 1)
      logic-delete-value: 1
      # 逻辑未删除值(默认为 0)
      logic-not-delete-value: 0
~~~

至此，我们的数据库配置已经完成✌。接下来我们配置一下Mybatis-plus。

### 3.3 引入MyBatis-Plus配置

**配置MyBatis-Plus**

* MyBatis 是一款优秀的持久层框架，它支持自定义 SQL、存储过程以及高级映射。MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。
* MyBatis-Plus是基于MyBatis框架进一步的封装，增强了MyBatis的功能，只需简单配置，即可快速进行单表 CRUD 操作，从而节省大量时间，以及一些常用工具的封装。

* MyBatis中文文档地址：https://mybatis.org/mybatis-3/zh/index.html
* MyBatis-Plus官方地址：https://baomidou.com/

接下来我们就一起配置一下MyBatis-Plus：

> 这里我们会使用Mybatis-plus生成代码、分页、逻辑删除、常用字段自动注入数据等插件。

👉首先是application.yml

~~~yml
# 配置mybatis-plus的xml和bean的目录
mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true    # 开启驼峰功能
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl    # 日志打印功能
  global-config:  #逻辑删除配置
    db-config:
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
~~~

然后我们在config目录创建MybatisPlusConfig.java文件

这里配置下Mybaits的自动填充机制

~~~java
package com.maple.mybatisplus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Date;

/**
 * Mybatis plus配置
 *
 * @author 笑小枫 <https://www.xiaoxiaofeng.com/>
 * @date 2022/7/10
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.maple.mybatisplus.mapper")
public class MybatisPlusConfig implements MetaObjectHandler {

    /**
     * 新增时,自动填充数据
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("isDeleted", false, metaObject);
        this.setFieldValByName("createId", 1L, metaObject);
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateId", 1L, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    /**
     * 修改时，自动填充数据
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateId", 1L, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
~~~

其中，createId暂时使用占位符，后期结合登录后从session里面获取。

**配置自动生成代码插件**

👉首先创建`BaseEntity.java`，所有的实体类都继承该对象，用于存放系统字段，代码如下👇

如果不需要`BaseEntity.java`或者字段不一致，直接删除或修改即可。

```java
package com.maple.mybatisplus.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 笑小枫 <https://www.xiaoxiaofeng.com/>
 * @date 2022/7/11
 */
@Data
public class BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    protected Long id;

    /**
     * 创建人id
     */
    @TableField(value = "create_id", fill = FieldFill.INSERT)
    private Long createId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人id
     */
    @TableField(value = "update_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateId;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
```

👉配置`MyBatis-Plus`代码生成工具，创建`util`目录，用于存放工具类。

然后创建`Generator.java`，代码如下：👇

~~~java
package com.maple.mybatisplus.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.maple.mybatisplus.model.BaseEntity;


/**
 * MyBatis-Plus代码生成工具
 *
 * @author 笑小枫 <https://www.xiaoxiaofeng.com/>
 * @date 2019/4/25
 */
public class Generator {

    public static void main(String[] args) {

        // 设置作者
        String auth = "笑小枫 <https://www.xiaoxiaofeng.com/>";
        // 设置父包名
        String packageName = "com.maple.mybatisplus";
        // 设置父包模块名
        String moduleName = "";
        // 指定输出目录
        String path = "D:";
        String url = "jdbc:mysql://127.0.0.1:3306/maple?useUnicode=true&useSSL=false&characterEncoding=utf8";
        String username = "root";
        String password = "Zhang123";
        // 设置需要生成的表名,多个
        String table = "usc_user";
        // 设置过滤表前缀
        String[] tablePrefix = {"usc_"};
        generateTest(auth, packageName, path, moduleName, url, username, password, table, tablePrefix);
    }

    private static void generateTest(String auth, String packageName, String path, String moduleName,
                                     String url, String username, String password, String table, String[] tablePrefix) {
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> builder.author(auth)
                        // 开启 swagger 模式
//                        .enableSwagger()
                        .outputDir(path))
                .packageConfig(builder -> builder.parent(packageName)
                                .moduleName(moduleName)
                        // 设置mapperXml生成路径
//                        .pathInfo(Collections.singletonMap(OutputFile.xml, path))
                )
                .strategyConfig(builder -> builder.addInclude(table)
                        .addTablePrefix(tablePrefix)
                        .entityBuilder()
                        // entity父类，这里把系系统字段抽取出来了，不需要删掉即可
                        .superClass(BaseEntity.class)
                        // 是否开启Lombok
                        .enableLombok()
                        // 设置逻辑删除标志
                        .logicDeleteColumnName("delete_flag")
                        .controllerBuilder()
                        .enableRestStyle()
                )
                .execute();
    }
}
~~~

> 注意：适用版本：mybatis-plus-generator 3.5.1 及其以上版本，对历史版本不兼容！3.5.1 以下的请参考[代码生成器旧](https://baomidou.com/pages/d357af/)

* superClass(BaseEntity.class)：entity使用父类BaseEntity.class，减少id、创建人、修改人等系统字段
* .enableSwagger()：这里没有配置swagger，所以我们关闭swagger模式
* logicDeleteColumnName("delete_flag")：开启逻辑删除

* enableLombok()：项目使用lombok，减少getter/setter代码的编写，使用插件编译时自动生成
* controllerBuilder().enableRestStyle()：controller使用Rest代码风格

👉然后我们执行该Main方法

![image-20220711163747350](http://file.xiaoxiaofeng.site/blog/image/2022/07/11/20220711163747.png)

 然后会在配置的目录下生成com文件夹，本文的在D：盘下。

我们将代码复制到我们的项目中，此时的项目代码结构如下如：👇

注意下mapper.xml存放的位置

![image-20231221141344181](https://image.xiaoxiaofeng.site/blog/2023/12/21/xxf-20231221141344.png?xxfjava)

### 3.3 功能测试👌

编写一个测试用例测试一下吧

在UserController.java文件中添加新增用户和根据ID查询用户信息的接口👇

~~~java
package com.maple.mybatisplus.controller;

import com.maple.mybatisplus.entity.User;
import com.maple.mybatisplus.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户中心-用户信息表 前端控制器
 * </p>
 *
 * @author 笑小枫 <https://www.xiaoxiaofeng.com/>
 * @since 2023-12-21
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/insert")
    public User insert(@RequestBody User user) {
        userService.save(user);
        return user;
    }

    @GetMapping("/selectById/{id}")
    public User selectById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

}
~~~

我们先调用一下插入方法。

参数我们只保留简单的几个，如下👇

~~~json
###
POST http://localhost:8080/user/insert
Content-Type: application/json

{
  "account": "xiaoxiaofeng",
  "email": "1150640979@qq.com",
  "nickName": "笑小枫",
  "password": "123456",
  "phone": "18300000000",
  "userName": "笑小枫"
}
~~~

可以看到数据里面，我们的数据已经插入进去了，这里直接用[笑小枫-SpringBoot系列](https://blog.csdn.net/qq_34988304/category_11604043.html)里面的图了，不重新截取了。。。👇

![image-20220711220037702](http://file.xiaoxiaofeng.site/blog/image/2022/07/11/20220711220038.png)

接下来我们调用查询接口看一下👇

~~~
GET http://localhost:8080/user/selectById/1
~~~

可以看到数据成功返回

![image-20231221141928075](https://image.xiaoxiaofeng.site/blog/2023/12/21/xxf-20231221141928.png?xxfjava)

OK，大功告成✌️

## 4. 贴几个常用的单表操作吧

简单的贴几个注意点吧，更多的操作可以参考Mybatis Plus的官网，或者给我留言哈，精通各种单表不想写sql的骚操作😂😂😂

~~~java
package com.maple.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.mybatisplus.entity.User;
import com.maple.mybatisplus.mapper.UserMapper;
import com.maple.mybatisplus.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户中心-用户信息表 服务实现类
 * </p>
 *
 * @author 笑小枫 <https://www.xiaoxiaofeng.com/>
 * @since 2023-12-21
 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;

    private void selectTest(User user) {
        // List查询
        List<User> userList = userMapper.selectList(Wrappers.lambdaQuery(User.class)
                // 精确查询
                .eq(User::getAccount, user.getAccount())
                // 存在即查询，不存在即不查询
                .eq(Objects.nonNull(user.getSex()), User::getSex, user.getSex())
                // 模糊查询
                .like(User::getNickName, user.getNickName())
                // 不在数组中
                .notIn(User::getId, 1, 2, 3)
                // 排序
                .orderByDesc(User::getCreateTime)
        );

        // update user对象中，如果为null不更新，如果需要强制更新为null，用set
        int updateNum = userMapper.update(user, Wrappers.lambdaUpdate(User.class)
                .set(User::getPhone, null)
                .eq(User::getId, user.getId()));

        // 删除这里是逻辑删除，修改的deleteFlag=true，如果不配置逻辑删除，则是物理删除
        // 如果配置了逻辑删除，使用mybatis plus提供的查询，查询不出删除的数据；如果直接在xml里面，可以查询出删除的数据
        int deleteNum = userMapper.deleteById(user.getId());
    }
}
~~~

## 5. 小结

好啦，本文就到这里了，我们简单的总结一下，主要介绍了以下内容👇👇

* SpringBoot集成mysql数据库
* 集成Mybatis Plus框架
* 通过一个简单的例子演示了一下使用Mybatis Plus进行数据插入和查询

## 6. 项目源码💕

本文到此就结束了，如果帮助到你了，帮忙点个赞👍

本文源码：[https://github.com/hack-feng/maple-product/tree/main/maple-mybatis-plus](https://github.com/hack-feng/maple-product/tree/main/maple-mybatis-plus)

>  🐾我是笑小枫，全网皆可搜的【[笑小枫](https://www.xiaoxiaofeng.com)】
