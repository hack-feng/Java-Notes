## 一、项目背景

不知道小伙伴们有没有遇到过这样的需求，就是一个项目启动时不知道数据源，需要项目无数据源启动后，用户在画面自定义录入数据源信息，然后项目再初始化数据库链接，初始化管理员用户。最后项目进入正常使用。

正常情况下，应该不会遇到这种需求吧，我们都是把数据库链接放在配置文件，然后启动项目，简简单单，轻轻松松。但是当整个项目交给用户使用时，谁使用都不知道情况下，算了，只能让他们自己输入数据源了。。。

本文就是针对这个问题，简单的介绍一下我实现的思路吧，本文只放核心代码，源码放在文章最后了。

## 二、涉及到技术栈

由于前端技术受限（画页面影响我输出的速度），这里就不画页面了，通过接口方式来展示。

* Spring Boot      version: 2.7.12
* Mysql                version: 8.0.29
* Mybatis-plus    version: 3.3.2
* Mybatis动态数据源

demo中引入的依赖

~~~xml
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

        <!-- mybatis-plus的依赖 -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.3.2</version>
        </dependency>

        <!--动态数据源-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
            <version>3.3.2</version>
        </dependency>

        <!--Lombok管理Getter/Setter/log等-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
            <version>1.18.24</version>
        </dependency>
~~~



## 三、功能实现

本文只是为了演示实现思想，源码只是一个实现的小demo，具体使用还是需要结合自己项目。

### 实现思想

1. 首先，项目启动后不加载数据源
2. 然后通过拦截器检验，是否连接数据库
3. 如果没有连接数据库，则去配置文件的地址找配置文件，如果存在，则加载数据库配置
4. 如果不存在数据库文件，则抛出异常，让用户输入数据库链接
5. 用户输入数据库链接后，进行校验连接
6. 如果连接通过，生成配置文件，保存在指定目录，供后续重启加载
7. 同时，装载Hikari连接池，利用Mybatis plus动态切换数据源的功能，将此连接池切为master，至此，数据库启动成功。

### 数据库表

~~~sql
CREATE TABLE `maple_user`
(
    `id`        BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_name` VARCHAR(64) NOT NULL COMMENT '登录账号',
    `password`  VARCHAR(64) NOT NULL COMMENT '登录密码',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT='用户信息' COLLATE='utf8_general_ci' ENGINE=InnoDB;
~~~

### 创建项目

这个很简单，就不多说了（说多了就是废话😂）

![image-20230525143334803](https://image.xiaoxiaofeng.site/blog/2023/05/25/xxf-20230525143342.png?xxfjava)

### 配置文件

这里配置文件简单配置了，mybatis-plus的配置和文件存储的路径（init.config）

~~~yml
server:
  port: 8080

mybatis-plus:
  mapper-locations: classpath*:mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

init:
  config:
    filePath: /srv/apps/config/
    dbFileName: db.properfies
    userFileName: user.properfies
~~~

###  调用接口

这里提供了四个接口，分别是

* 初始化数据库配置
* 校验数据库是否链接
* 重置数据库配置，并断开链接
* 获取用户信息，如果不存在，初始化

接口简单的controller贴上，非核心实现就不贴了，需要的朋友可以去看源码

~~~java
package com.maple.inputdb.controller;

import com.maple.inputdb.bean.InitModel;
import com.maple.inputdb.config.InitDataConfig;
import com.maple.inputdb.entity.MapleUser;
import com.maple.inputdb.service.IMapleUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 笑小枫 <https://xiaoxiaofeng.com/>
 * @date 2023/3/9
 */
@RestController
@RequestMapping("/init")
@AllArgsConstructor
public class InitController {

    private final InitDataConfig initDataConfig;

    private final IMapleUserService userService;

    /**
     * 初始化数据库
     *
     * @param initModel 数据库配置
     */
    @PostMapping("/initData")
    public void initData(@RequestBody InitModel initModel) {
        initDataConfig.initData(initModel);
    }

    /**
     * 校验数据库是否链接
     *
     * @return 配置完成
     */
    @PostMapping("/check")
    public String check() {
        return "系统配置完成";
    }

    /**
     * 重置连接数据
     */
    @PostMapping("/resetData")
    public void resetData() {
        initDataConfig.deleteFile();
    }

    /**
     * 获取用户信息，如果不存在，初始化
     *
     * @param userName 用户账号
     * @return 用户信息
     */
    @PostMapping("/getUser")
    public MapleUser getUser(String userName) {
        return userService.getUser(userName);
    }
}
~~~

### 核心工具类

* 数据库是否链接全部变量，使用单例模式，初始化是否连接数据库的状态，放全局变量

~~~java
package com.maple.inputdb.config;

/**
 * 数据库是否链接全部变量
 *
 * @author 笑小枫 <https://xiaoxiaofeng.com/>
 * @date 2023/3/23
 */
public class DbStatusSingleton {
    
    /**
     * false：未连接数据库  true：已连接数据库
     */
    private boolean dbStatus = false;

    private static final DbStatusSingleton DB_STATUS_SINGLETON = new DbStatusSingleton();

    private DbStatusSingleton() {
    }

    public static DbStatusSingleton getInstance() {
        return DB_STATUS_SINGLETON;
    }

    public boolean getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(boolean dbStatus) {
        this.dbStatus = dbStatus;
    }

}
~~~

* 创建拦截器，请求进来之前先判断是否初始化配置，如果没有则报错，这里可以指定错误码，前端可以统一拦截错误码，然后跳转初始化配置页面。注意：需要在启动类上添加`@ServletComponentScan`注解

~~~java
package com.maple.inputdb.filter;

import com.maple.inputdb.config.DbStatusSingleton;
import com.maple.inputdb.config.DynamicDatasourceConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author 笑小枫 <https://xiaoxiaofeng.com/>
 * @date 2023/3/23
 */
@WebFilter(filterName = "dbFilter", urlPatterns = "/*")
@Order(1)
@Slf4j
@AllArgsConstructor
public class DbFilter implements Filter {

    private final DynamicDatasourceConfig datasourceConfig;

    private final List<String> excludedUrlList;

    @Override
    public void init(FilterConfig filterConfig) {
        excludedUrlList.addAll(Collections.singletonList(
                "/init/initData"
        ));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String url = ((HttpServletRequest) request).getRequestURI();
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        boolean isMatch = false;
        for (String excludedUrl : excludedUrlList) {
            if (Pattern.matches(excludedUrl.replace("*", ".*"), url)) {
                isMatch = true;
                break;
            }
        }
        if (isMatch) {
            chain.doFilter(request, response);
        } else {
            boolean isOk = DbStatusSingleton.getInstance().getDbStatus() || datasourceConfig.checkDataSource();
            if (isOk) {
                chain.doFilter(request, response);
            } else {
                log.error("初始化系统失败，请先进行系统配置");
                writeRsp(httpServletResponse);
            }
        }
    }

    private void writeRsp(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setHeader("content-type", "application/json;charset=UTF-8");
        try {
            response.getWriter().println("初始化系统失败，请先进行系统配置");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
~~~

* 校验数据库配置，储存初始化配置

~~~java
package com.maple.inputdb.config;

import com.maple.inputdb.bean.InitModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author 笑小枫 <https://xiaoxiaofeng.com/>
 * @date 2023/3/10
 */
@Slf4j
@Component
@AllArgsConstructor
public class InitDataConfig {

    private final DynamicDatasourceConfig dynamicDatasourceConfig;

    private final InitConfigProperties initConfigProperties;

    public void initData(InitModel initModel) {

        if (DbStatusSingleton.getInstance().getDbStatus()
                || Boolean.TRUE.equals(dynamicDatasourceConfig.checkDataSource())) {
            throw new RuntimeException("数据已完成初始化，请勿重复操作");
        }

        // 检查数据库连接是否正确
        checkConnection(initModel);

        if (!new File(initConfigProperties.getInitFilePath() + initConfigProperties.getInitUserName()).exists()) {
            File file = createFile(initConfigProperties.getInitFilePath(), initConfigProperties.getInitUserName());
            try (FileWriter out = new FileWriter(file);
                 BufferedWriter bw = new BufferedWriter(out)) {
                bw.write("userName=" + initModel.getSysUserName());
                bw.newLine();
                bw.write("password=" + initModel.getSysPassword());
                bw.flush();
            } catch (IOException e) {
                log.info("写入管理员信息文件失败", e);
                throw new RuntimeException("写入管理员信息文件失败，请重试");
            }
        }

        if (!new File(initConfigProperties.getInitFilePath() + initConfigProperties.getInitDbName()).exists()) {
            File file = createFile(initConfigProperties.getInitFilePath(), initConfigProperties.getInitDbName());
            try (FileWriter out = new FileWriter(file);
                 BufferedWriter bw = new BufferedWriter(out)) {

                bw.write(String.format("jdbcUrl=jdbc:mysql://%s:%s/%s?autoReconnect=true&autoReconnectForPools=true&useUnicode=true&characterEncoding=UTF-8",
                        initModel.getDatabaseHost(), initModel.getDatabasePort(), initModel.getDatabaseName()));
                bw.newLine();
                bw.write("username=" + initModel.getUser());
                bw.newLine();
                bw.write("password=" + initModel.getPassword());
                bw.newLine();
                bw.write("driverClassName=com.mysql.cj.jdbc.Driver");
                bw.flush();

            } catch (IOException e) {
                log.info("写入数据库文件失败", e);
                throw new RuntimeException("写入数据库文件失败，请重试");
            }
        }

        boolean isOk = dynamicDatasourceConfig.checkDataSource();
        if (!isOk) {
            throw new RuntimeException("初始化数据库信息失败，请检查配置是否正确");
        }
    }

    /**
     * 检查数据库连接是否正确
     *
     * @param initModel 连接信息
     */
    private void checkConnection(InitModel initModel) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s",
                    initModel.getDatabaseHost(), initModel.getDatabasePort(), initModel.getDatabaseName()), initModel.getUser(), initModel.getPassword());
            log.info("校验数据库连接成功，开始进行数据库配置" + conn.getCatalog());
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            log.info("校验数据库连接失败", e);
            throw new RuntimeException("初始化数据库信息失败，请检查配置是否正确：" + e.getMessage());
        }
    }

    private File createFile(String path, String fileName) {
        File pathFile = new File(path);
        if (pathFile.mkdirs()) {
            log.info(path + " is not exist, this is auto created.");
        }
        File file = new File(path + File.separator + fileName);
        try {
            if (!file.createNewFile()) {
                throw new RuntimeException(String.format("创建%s文件失败，请重试", fileName));
            }
        } catch (IOException e) {
            log.error(String.format("创建%s文件失败", fileName), e);
            throw new RuntimeException(String.format("创建%s文件失败，请重试", fileName));
        }
        return file;
    }

    public void deleteFile() {
        File sqlFile = new File(initConfigProperties.getInitFilePath() + initConfigProperties.getInitDbName());
        if (sqlFile.exists()) {
            log.info(sqlFile.getName() + " --- delete sql file result:" + sqlFile.delete());
        }

        File userFile = new File(initConfigProperties.getInitFilePath() + initConfigProperties.getInitUserName());
        if (userFile.exists()) {
            log.info(userFile.getName() + " --- delete user file result:" + userFile.delete());
        }

        dynamicDatasourceConfig.stopDataSource();

        // 数据初始化状态设为false
        DbStatusSingleton.getInstance().setDbStatus(false);
        log.info("初始化数据重置完成");
    }
}
~~~

* 使用Mybatis plus动态数据源功能，进行切换数据源，完成数据库连接启动和关闭功能

~~~java
package com.maple.inputdb.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author 笑小枫 <https://xiaoxiaofeng.com/>
 * @date 2023/3/9
 */
@Slf4j
@Component
public class DynamicDatasourceConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private InitConfigProperties initConfigProperties;

    public Boolean checkDataSource() {
        try {
            Connection connection = dataSource.getConnection();
            connection.close();
            DbStatusSingleton.getInstance().setDbStatus(true);
            return true;
        } catch (Exception e) {
            log.info("获取数据库连接失败，即将重新连接数据库...");
            return addDataSource();
        }
    }

    public Boolean addDataSource() {
        File file = new File(initConfigProperties.getInitFilePath() + initConfigProperties.getInitDbName());
        if (!file.exists()) {
            log.error("连接数据库失败：没有找到db.properties文件");
            return false;
        }
        try (InputStream rs = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(rs);
            HikariConfig config = new HikariConfig(properties);
            config.setPassword(config.getPassword());
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
            ds.addDataSource("master", new HikariDataSource(config));
            ds.setPrimary("master");
            DbStatusSingleton.getInstance().setDbStatus(true);
            log.info("连接数据库成功");
            return true;
        } catch (Exception e) {
            log.error("连接数据库失败：" + e);
            return false;
        }
    }

    /**
     * 关闭数据库连接
     */
    public void stopDataSource() {
        try {
            Connection connection = dataSource.getConnection();
            connection.close();
        } catch (Exception e) {
            log.info("数据库连接已关闭，无需重复关闭...");
            return;
        }
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        HikariDataSource hds = (HikariDataSource) ds.getDataSource("master");
        try {
            if (hds.isRunning()) {
                hds.close();
                log.info("数据库连接已关闭");
            }
            ds.setPrimary("null");
            ds.removeDataSource("master");
        } catch (Exception e) {
            log.error("关闭数据库连接失败:", e);
            e.printStackTrace();
        }
    }
}
~~~

* 初始化数据的Model类

~~~java
package com.maple.inputdb.bean;

import lombok.Data;

/**
 * @author 笑小枫 <https://xiaoxiaofeng.com/>
 * @date 2023/3/10
 */
@Data
public class InitModel {
    /**
     * 数据库相关字段
     */
    private String databaseHost;
    private String databasePort;
    private String databaseName;
    private String user;
    private String password;


    /**
     * 初始化用户
     */
    private String sysUserName;
    private String sysPassword;

}
~~~

* 初始化数据的配置文件类

~~~java
package com.maple.inputdb.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author 笑小枫 <https://xiaoxiaofeng.com/>
 * @date 2023/3/10
 */
@Data
@Configuration
public class InitConfigProperties {

    @Value("${init.config.filePath}")
    private String initFilePath;

    @Value("${init.config.dbFileName}")
    private String initDbName;

    @Value("${init.config.userFileName}")
    private String initUserName;

}
~~~

还有一些getUser接口牵扯到的文件crud类，这里就不一一去贴了。

### 四、功能测试

* check连接：首先启动项目，调用`/init/check`接口，这是还没有初始化数据库配置，在拦截器拦截校验时报错了，如下图所示：

![image-20230526111351407](https://image.xiaoxiaofeng.site/blog/2023/05/26/xxf-20230526111351.png?xxfjava)

* 初始化连接：调用`/init/initData`接口初始化数据

~~~json
{
  "sysUserName": "admin",
  "sysPassword": "123456",
  "databaseHost": "127.0.0.1",
  "databasePort": "3306",
  "databaseName": "maple",
  "user": "root",
  "password": "123456"
}
~~~

初始化时，会先校验是否初始化过配置，如果没有才会进行，保存配置，并连接数据库，如下图所示：

![image-20230526111702732](https://image.xiaoxiaofeng.site/blog/2023/05/26/xxf-20230526111702.png?xxfjava)

这是看我们存放配置的文件路径里面已经出现了我们的文件。

![image-20230526112003126](https://image.xiaoxiaofeng.site/blog/2023/05/26/xxf-20230526112003.png?xxfjava)

打开`db.properfies`文件，可以看到下面内容，后续项目重新启动，会先来此目录判断文件是否存在，如果存在，则会自动加载文件内容，去连接数据库。![image-20230526112028170](https://image.xiaoxiaofeng.site/blog/2023/05/26/xxf-20230526112028.png?xxfjava)

* check连接：可以看到此时数据库已经连接成功

![image-20230526111853516](https://image.xiaoxiaofeng.site/blog/2023/05/26/xxf-20230526111853.png?xxfjava)

* 重置数据库：调用`/init/resetData`接口，这里会报一个错，可以忽略，想了解详情的，可以去看源码，然后会无了一个大语...

![image-20230526112301509](https://image.xiaoxiaofeng.site/blog/2023/05/26/xxf-20230526112301.png?xxfjava)

* 再次check连接：可以看到，在拦截器拦截校验时，数据库已经断开，配置文件也已经删除，需要重新配置了

![image-20230526112454084](https://image.xiaoxiaofeng.site/blog/2023/05/26/xxf-20230526112454.png?xxfjava)

## 五、功能总结

本文主要利用Mybatis Plus的动态切换数据源的功能，间接实现了无数据源启动，用户自定义数据源的功能。只是一种实现思路，肯定还会有更优的实现方案，暂时还没有找到，如找到，会继续出文介绍。

配合本文的还有数据库版本管理，连接数据库后，可以初始化数据库表结构，然后再初始化管理员信息，后续迭代升级时，sql变更，在项目启动时自动加载，维护数据库表版本，可以去看后续的文章，通过`flywaydb`实现。

本文到此就结束了，如果帮助到你了，帮忙点个赞👍

本文源码：[https://github.com/hack-feng/maple-product/tree/main/maple-input-db](https://github.com/hack-feng/maple-product/tree/main/maple-input-db)



>  我是笑小枫，全网皆可搜的【笑小枫】

