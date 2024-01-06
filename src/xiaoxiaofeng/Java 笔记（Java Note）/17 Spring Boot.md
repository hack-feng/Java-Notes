# Java Config 常用注解 #

## 修饰配置类 ##

 *  @Configuration：修饰类，用于声明当前类是一个配置类（**本身自带 @Component 注解**，启动时 Spring 会创建该类的代理对象）
 *  @ImportResource：修饰配置类，用于导入指定的 XML 配置文件
 *  @Import：修饰 Java 配置类，用于向当前配置类中导入其它 Java 配置类
 *  @ComponentScan：修饰配置类，相当于 `<context:component-scan base-package="包1, 包2, …"/>`，默认扫描当前包以及子包下所有使用 @Service、©Components @Repository @Controller 的类，并注册为 Bean
 *  @PropertySource：修饰配置类，用于加载指定的资源配置文件（可同时使用多个）
 *  @PropertySources：修饰配置类，用于同时加载多个的资源配置文件
 *  @ConfigurationProperties：修饰配置类，用于将配置文件的参数值赋给配置类的属性（该配置类还需使用 @Component 修饰，或者在启动类上添加 @EnableConfigurationProperties）
 *  @AutoConfigureAfter：在指定的配置类初始化后再加载
 *  @AutoConfigureBefore：在指定的配置类初始化前加载
 *  @AutoConfigureOrder：数越小越先初始化

## 修饰属性或方法 ##

 *  @Value：修饰属性、方法及构造器函数，通过使用**属性占位符**从**资源配置文件**中加载一个参数值，如 @Value("$\{db.username\}")，此时要配置一个 PropertySourcesPlaceholderConfigurer 的 Bean 用于解析属性占位符（创建该 Bean 的 方法用 static 修饰，Spring Boot 中不需要配置该 Bean），或者**使用 Environment 获取配置文件中的参数值** `environment.getProperty("app.name")`
 *  @Bean：修饰**方法**，将该**方法的返回值**定义成容器中的一个 Bean，Bean 的类型由方法返回值的类型决定，**名称默认和方法名相同**（属性值：name、initMethod、destroyMethod）
 *  @Scope：修饰方法，指定该方法对应的 Bean 的生命域
 *  @Lazy：修饰方法，指定该方法对应的 Bean 是否需要延迟初始化
 *  @DependsOn：修饰方法，指定在初始化该方法对应的 Bean 之前初始化指定的 Bean
 *  @Profile：修饰配置类或方法，设定当前 context 需要使用的配置环境，可达到**在不同情况下**选择实例化不同的 Bean
 *  @Conditional：满足某个特定的条件才创建该一个特定的 Bean，其属性 value 的类型是 Class\[\]
 *  @Scheduled：修饰方法，用于声明该方法是一个计划任务，属性：
    
     *  cron：指定 cron 表达式：`秒 分 时 日 月 周 [年]`，按照指定时间执行
     *  fixedRate：上一次**开始执行**时间点向后延迟多少时间执行，单位是毫秒
     *  fixedDelay：上一次**执行完毕**时间点向后延迟多少时间执行，单位是毫秒
     *  initialDelay：初次执行任务之前需要等待的时间，和 fixedRate、fixedDelay 组合使用

## @Enable\* 注解 ##

 *  @EnableWebMvc：开启 Web MVC 的配置支持，相当于 `<mvc:annotation-driven/>`（在 Spring Boot 中**无须**使用，否则由于该注解导入了 DelegatingWebMvcConfiguration 配置类，该类继承 [WebMvcConfigurationSupport][]，从而导致 WebMvcAutoConfiguration 不被自动装配）
 *  @EnableTransactionManagement：开启注解式事务的支持，Spring 容器会自动扫描注解 @Transactional 的方法和类，相当于 `<tx:annotation-driven/>`（在 Spring Boot 中无须显式开启使用）
 *  @EnableAspectJAutoProxy：开启对 AspectJ 自动代理的支持，相当于 `<aop:aspectj-autoproxy/>`
 *  @EnableCaching：开启注解式的缓存支持
 *  @EnableScheduling：开启计划任务的支持，再在执行计划任务的 Bean 的方法上使用 @Scheduled 声明这是一个计划任务
 *  @EnableAsync：开启对异步任务的支持，再通过在实际执行的 Bean 的方法中使用 @Async 注解来声明其是一个异步任务（通过 AsyncConfigurer 配置 Executor、AsyncUncaughtExceptionHandler；通过 TaskExecutionAutoConfiguration\#applicationTaskExecutor 创建 ThreadPoolTaskExecutor；异步方法返回值可以为 void 或者 Future，调用方法和异步方法要**写在不同的类中**）

## 条件注解 ##

 *  @Conditional
 *  @ConditionalOnBean：当容器里有指定的 Bean 的条件下
 *  @ConditionalOnMissingBean：当容器里没有指定 Bean 的情况下
 *  @ConditionalOnClass：当类路径下有指定的类的条件下
 *  @ConditionalOnMissingClass：当类路径下没有指定的类的条件下
 *  @ConditionalOnProperty：基于属性作为判断条件
 *  @ConditionalOnResource
 *  @ConditionalOnExpression：基于 SpEL 表达式作为判断条件
 *  @ConditionalOnWebApplication：当前项目是 Web 项目的条件下
 *  @ConditionalOnNotWebApplication：当前项目不是 Web 项目的条件下

## 单元测试相关注解 ##

 *  @RunWith(SpringRunner.class)
 *  @SpringBootTest
 *  @ActiveProfiles("test")：声明生效的 profile
 *  @Transactional：修饰测试类或测试类中的方法，在测试中使用事务管理时，最终**不会进行 commit 操作**
 *  @Rollback：修饰测试类或测试类中的方法，事务执行完后是否进行回滚，默认为 true

``````````java
// 先启动 Spring Boot 程序，然后运行测试代码 
  
  @RunWith(SpringRunner.class) 
  // 创建的测试环境为 Web 测试环境，端口设置为随机，相当于配置 server.port=0 
  @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 
  public class HelloControllerTest { 
      // 注解注入当前的端口号，相当于 @Value("${local.server.port}") 
      @LocalServerPort 
      private int port; 
      private URL base; 
      @Autowired 
      private TestRestTemplate template; 
  
      @Before 
      public void setup() throws Exception { 
          this.base = new URL("http://localhost:" + port + "/hello"); 
      } 
  
      @Test 
      public void getHello() throws Exception { 
          ResponseEntity<String> response = template.getForEntity(base.toString(), String.class); 
          assertThat(response.getBody(), equalTo("Hello SpringBoot")); 
      } 
  }
``````````

 *  使用 MockMvc 模拟 Http 请求

``````````java
@RunWith(SpringRunner.class) 
  @SpringBootTest 
  @AutoConfigureMockMvc 
  @WebMvcTest(GreetingController.class) // 可设置只需要实例化的 controller 
  public class WebMockTest { 
  
      @Autowired 
      private MockMvc mockMvc; 
  
      @MockBean 
      private GreetingService service; 
  
      @Test 
      public void greetingShouldReturnMessageFromService() throws Exception { 
          Mockito.when(service.greet()).thenReturn("Hello Mock"); // 模拟数据的返回 
          mockMvc.perform(get("/greeting")).andDo(print()).andExpect(status().isOk()) 
                  .andExpect(content().string(containsString("Hello Mock"))); 
      } 
  }
``````````

# Spring Boot 简介 #

 *  **官方文档**：[https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle][https_docs.spring.io_spring-boot_docs_current_reference_htmlsingle]
 *  Spring Boot 核心功能
    
     *  独立运行的 Spring 项目。可以以 jar 包的形式独立运行 `java -jar xx.jar`
     *  内嵌 Servlet 容器
     *  提供 starter 简化 Maven 配置。spring-boot-starter-parent
     *  自动配置 Spring。根据在 classpath 路径中的 jar 包、类，为 jar 包里的类自动配置 Bean
     *  准生产的应用监控
 *  无代码生成和 xml 配置。通过条件注解来实现
 *  主要功能
    
     *  自动配置（auto-configuration）
     *  简化依赖（starters）：Core、Web、Template Engines、SQL、NoSQL、Integration、Cloud Core、I/O、Ops
     *  命令行界面（CLI 或 command-line interface）：Spring Boot CLI
     *  执行器（Actuator）：对应用系统的自省和监控
 *  Spring Boot 快速搭建
    
     *  [https://start.spring.io][https_start.spring.io]
     *  Spring Tool Suite，新建 Spring Starter Project
     *  IntellIJ IDEA，新建 Spring Initializr 项目
     *  Spring IO Platform has reached the end of its supported life on 9 April 2019.
     *  Maven 手工构建：使用 spring-boot-starter-parent 作为项目的 parent（using `spring-boot-starter-parent` as their Maven project’s parent），在 添加 Web 支持的 starter pom，添加 Spring Boot 的编译插件

``````````xml
<parent> 
     <groupId>org.springframework.boot</groupId> 
     <artifactId>spring-boot-starter-parent</artifactId> 
     <version>2.1.1.RELEASE</version> 
     <relativePath/> <!-- lookup parent from repository --> 
  </parent> 
  
  <properties> 
     <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding> 
     <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding> 
     <java.version>1.8</java.version> 
  </properties> 
  
  <dependencies> 
     <dependency> 
         <groupId>org.springframework.boot</groupId> 
         <artifactId>spring-boot-starter-web</artifactId> 
     </dependency> 
  </dependencies> 
  
  <!--<build>--> 
     <!--<plugins>--> 
         <!-- SpringBoot 应用打包插件 --> 
         <!--<plugin>--> 
             <!--<groupId>org.springframework.boot</groupId>--> 
             <!--<artifactId>spring-boot-maven-plugin</artifactId>--> 
         <!--</plugin>--> 
     <!--</plugins>--> 
  <!--</build>-->
``````````

 *  Maven 手工构建：引入 spring-boot-dependencies 进行依赖管理（importing the `spring-boot-dependencies` bom），在 添加 Web 支持的 starter pom，添加 Spring Boot 的编译插件

``````````xml
<properties> 
     <java.version>1.8</java.version> 
  </properties> 
  
  <dependencyManagement> 
     <dependencies> 
         <dependency> 
             <groupId>org.springframework.boot</groupId> 
             <artifactId>spring-boot-dependencies</artifactId> 
             <version>2.1.1.RELEASE</version> 
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
  </dependencies> 
  
  <build> 
     <plugins> 
         <plugin> 
             <groupId>org.springframework.boot</groupId> 
             <artifactId>spring-boot-maven-plugin</artifactId> 
             <version>2.1.1.RELEASE</version> 
             <executions> 
                 <execution> 
                     <goals> 
                         <goal>repackage</goal> 
                     </goals> 
                 </execution> 
             </executions> 
         </plugin> 
     </plugins> 
  </build>
``````````

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414439471.png) 
图 1 Spring\_Boot

# 安装 Spring Boot 应用 #

 *  可执行 Jar  ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414441014.png) 
 *  [可直接运行的 Jar][Jar 1]
    
     *  打包后的 Jar 可直接运行，无需 java 命令
     *  [定制启动脚本][Link 1]

# 基本配置 #

## 启动类和 @SpringBootApplication ##

``````````java
@SpringBootApplication
public class AppConfig {
    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class, args);
        // new SpringApplicationBuilder().sources(AppConfig.class).run(args);
    }
}
``````````

 *  @SpringBootApplication：修饰启动类，组合了以下注解：
    
     *  @Configuration：用于声明当前类是一个配置类
     *  @EnableAutoConfiguration：让 Spring Boot 根据类路径中的 jar 包依赖为当前项目进行自动配置，可以通过使用 exclude 属性关闭特定的自动配置
     *  @ComponentScan：组件扫描，可自动发现和装配 Bean

## 定制 Banner ##

 *  关闭 banner：spring.main.banner-mode=off
 *  自定义 Banner，banner.txt

## ApplicationRunner 或 CommandLineRunner 接口 ##

 *  通过重写其 run 方法，该方法在 `SpringApplication.run(…)` 完成之前调用

## 常用 starter pom ##

### 官方 starter pom ###

 *  spring-boot-starter：核心 starter，包含自动配置、日志和 YAML 配置文件的支持
 *  spring-boot-starter-web：用于使用 Spring MVC 构建 web 应用，包括 RESTful（默认的内嵌容器是 Tomcat）
 *  spring-boot-starter-test：用于测试 Spring Boot 应用，支持常用测试类库，包括 JUnit, Hamcrest 和 Mockito
 *  spring-boot-starter-cache：用于使用 Spring 框架的缓存支持
 *  spring-boot-starter-aop：用于使用 Spring AOP 和 AspectJ 实现面向切面编程
 *  spring-boot-starter-jdbc：对 JDBC 的支持（使用 Tomcat JDBC 连接池）
 *  spring-boot-starter-data-mongodb：用于使用基于文档的数据库 MongoDB 和 Spring Data MongoDB
 *  spring-boot-starter-data-redis：用于使用 Spring Data Redis 和 Jedis 客户端操作键-值存储的 Redis
 *  spring-boot-starter-data-solr：通过 Spring Data Solr 使用 Apache Solr 搜索平台
 *  spring-boot-starter-data-elasticsearch：用于使用 Elasticsearch 搜索，分析引擎和 Spring Data Elasticsearch
 *  spring-boot-starter-freemarker：用于使用 FreeMarker 模板引擎构建 MVC web 应用
 *  spring-boot-starter-mail：用于使用 Java Mail 和 Spring 框架 email 发送支持
 *  spring-boot-starter-activemq：用于使用 Apache ActiveMQ 实现 JMS 消息

### 第三方 starter pom ###

 *  druid-spring-boot-starter
 *  mysql-connector-java
 *  mybatis-spring-boot-starter
 *  pagehelper-spring-boot-starter
 *  com.gitee.reger:spring-boot-starter-dubbo

### 开发自己的 starter ###

 *  准备第三方的 jar
 *  制作 starter
    
     *  建 Maven 工程，xxx-spring-boot-starter
     *  引入 spring-boot-start、spring-boot-autoconfigure、第三方 jar
     *  如需要生成配置元信息，加入 spring-boot-configuration-processor 依赖
     *  编写自动配置类
     *  配置发现配置文件：META-INF/spring.factories
     *  打包发布

## 外部配置 ##

 *  可以使用 properties 文件、YAML 文件、环境变量和命令行参数来外部化配置
 *  属性会以如下的顺序进行设值：
    
     *  命令行参数（优先级最高）
     *  jar 包外部的 Profile-specific 应用属性（application-\{profile\}.properties 和 YAML 变量）
     *  jar 包内部的 Profile-specific 应用属性（application-\{profile\}.properties 和 YAML 变量）
     *  jar 包外部的应用配置（application.properties 和 YAML 变量）
     *  jar 包内部的应用配置（application.properties 和 YAML 变量）
     *  @Configuration 类上的 @PropertySource 注解
     *  默认属性（使用 SpringApplication.setDefaultProperties 指定）

### 命令行参数配置 ###

 *  Spring Boot 会将所有命令行配置参数（以 '—' 开头，比如 `—server.port=9000 —app.name="MyApp"`）转化成一个 property，并将其添加到 Spring Environment 中

### 使用 properties 文件配置参数 ###

 *  SpringApplication 默认从以下位置加载 application.properties 文件，并把它们添加到 Spring Environment 中：
    
     *  项目根目录下的 /config 子目录 `file:config/`（优先级最高）
     *  项目根目录 `file:`
     *  项目 classpath 下的 /config 包 `classpath:/config`
     *  项目 classpath 根路径 `classpath:`
 *  指定其它的配置文件名：spring.config.name
 *  指定配置文件的加载路径（目录位置或文件路径列表以逗号分割，目录应以 / 结尾）：spring.config.location，`ConfigurableApplicationContext context = new SpringApplicationBuilder(TestDefaultFile.class).properties("spring.config.location=classpath:/test-folder/my-config.properties").run(args);`
 *  在 properties 文件中可以使用 `${属性名:默认值}` 引用对应属性的值（当在 properties 文件中找不到引用的属性时默认使用的属性），如 `port=9090` `server.port=${port:8080}`
 *  通过 `@..@` 占位符引用 Maven 项目的属性，通过 `${..}` 占位符引用 Gradle 项目的属性

### 属性绑定 ###

 *  使用 `@Value("${app.name}")` 直接将**非静态属性**值注入到 **Bean** 中（注意：为静态变量赋值时，需在其 setter 方法上使用）
 *  通过 @ConfigurationProperties 将 properties 属性和一个 Bean 及其属性关联（可为不受控的第三方组件绑定属性）
    
     *  松绑定：使用 @ConfigurationProperties 将 Environment 属性**绑定到 Bean** 时会使用一些宽松的规则
     *  `@ConfigurationProperties(prefix="db") private String userName;` 允许匹配方式`db.userName=root` `db.user_name=root` `db.user-name=root` `db_user_name=root` `DB_USER_NAME=root`

``````````java
# application.properties 文件
db.username=root
db.password=admin
db.url=jdbc:mysql:///test
``````````

``````````java
// 绑定到自定义 Bean 属性
@Component // 或者在启动类上添加 @EnableConfigurationProperties(DataSourceConfigProperties.class)
@ConfigurationProperties(prefix = "db")
public class DataSourceConfigProperties {
   private String username;
   private String password;
   private String url;
}

// 绑定到第三方组件属性
@Bean
@ConfigurationProperties(prefix = "db")
public DriverManagerDataSource getDataSource() {
   return new DriverManagerDataSource();
}
``````````

 *  注入一个 ApplicationArguments 类型的 Bean，ApplicationArguments 接口既提供对原始 String\[\] 参数的访问，也提供对解析成 option 和 non-option 参数的访问，如 `args.getNonOptionArgs().toString()`

### 自定义 Environment ###

 *  实现 EnvironmentPostProcessor 接口，重写 postProcessEnvironment 方法

``````````java
PropertiesPropertySource propertySource = new PropertiesPropertySource("mine", properties);
// PropertySource propertySource = new MapPropertySource("mine", map);
// Resource path = new ClassPathResource("com/example/myapp/config.yml");
// PropertySource propertySource = new PropertiesPropertySourceLoader().load("mine", path).get(0);
environment.getPropertySources().addLast(propertySource);
``````````

 *  将实现类**注册**到 META-INF/spring.factories，即在该配置文件中添加 `org.springframework.boot.env.EnvironmentPostProcessor=com.example.YourEnvironmentPostProcessor`

> 
> Spring Boot prepares the `Environment` before the `ApplicationContext` is refreshed. Any key defined with `@PropertySource` is loaded too late to have any effect on auto-configuration.
> 

## Profile 配置 ##

 *  针对不同的环境使用不同的配置
 *  配置文件的命名格式为 application-\{profile\}.properties，如 application-prod.properties
 *  通过在 application.properties 中设置 spring.profiles.active=prod 来指定生效的 Profile 为 application-prod.properties
 *  通过调用 ConfigurableEnvironment 接口控制 Profile 的激活：setActiveProfiles、addActiveProfile、setDefaultProfiles 方法

``````````yml
# 定义 dev 与 prod 两个 profiles，profiels 间使用“---”进行分隔
server:
  address: 192.168.1.100
---
spring:
  profiles: dev
server:
  address: 127.0.0.1
---
spring:
  profiles: prod
server:
  address: 192.168.1.120
``````````

## 日志配置 ##

 *  默认情况下，Spring Boot 使用 Logback 作为日志框架
 *  默认的日志级别是 INFO 级别
 *  默认的日志格式为：时间 级别 PID —- \[线程名\] 日志类:日志内容
 *  配置日志级别（TRACE、DEBUG、INFO、WARN、ERROR、OFF），默认是 INFO 级别，格式为 logging.level.包名=级别，如：logging.level.root=DEBUG、logging.level.org.springframework=DEBUG
 *  输出日志到文件，默认 10M 自动分割文件：logging.file=log.log、logging.path=d:/log.log

### 外部日志框架 LogBack ###

 *  自动加载根据配置文件 logback.xml 和 logback-spring.xml（推荐）
 *  或者指定日志配置文件：logging.config=classpath:mylogback.xml
 *  [Logback configuration][]
 *  常用标签： ：子标签  （负责写日志的组件）、  、
    

``````````xml
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml" />
    <include resource="org/springframework/boot/logging/logback/console-appender.xml" />
    <include resource="org/springframework/boot/logging/logback/file-appender.xml" />

    <!-- 引入外部配置文件 -->
    <property resource="application.properties"/>
    <!--<property file="system.properties"/>-->
    <!--<property file="e:\\system.properties"/>-->
    <!--<property file="/home/webadminconfig/system.properties"/>-->

    <!-- 从 Spring Environment 读取属性 -->
    <springProperty scope="context" name="APPLICATION_NAME" source="spring.application.name" defaultValue="myapp"/>

    <!-- 定义属性，在配置文件中可使用 ${} 取值 -->
    <property name="LOG_DIR" value="./logs"/>

    <!-- 输出到控制台 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        <!--输出的日志级别是大于或等于此级别-->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>info</level>
        </filter>
    </appender>
    <!-- 输出到文件 -->
    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>${LOG_DIR}/${APPLICATION_NAME}.log</file>
        <encoder>
            <pattern>%date %level [%thread] %logger{10} [%file:%line] %msg%n</pattern>
        </encoder>
    </appender>
    <!-- 滚动输出到文件 -->
    <appender name="ROLLFILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <encoder>
            <pattern>%clr(%d{yyyy-MM-dd HH:mm:ss}) %clr(%level) [%thread]-%class:%line>>%msg%n</pattern>
        </encoder>　　　　　　
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_DIR}/${APPLICATION_NAME}_%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory><!-- 日志文件保留份数 -->
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter"><!-- 只打印INFO日志 -->
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- 设置某一个包或具体的某一个类的日志打印级别以及指定 appender -->
    <logger name="org.springframework.web" level="INFO">
        <appender-ref ref="FILE" />
    </logger>

    <!-- 特殊的 logger，代表根配置，如果没有单独指定日志包层级，都默认使用 root 定义的日志输出级别 -->
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <!-- <appender-ref ref="FILE" />-->
    </root>

    <springProfile name="dev,test">
        <!-- 开发或测试环境时激活 -->
    </springProfile>
    <springProfile name="!prod">
        <!-- 生产环境时不激活 -->
    </springProfile>
</configuration>
``````````

## 自动配置原理 ##

 *  spring-boot-autoconfiguration
 *  查看当前项目中已启用和未启用的自动配置的报告（ConditionEvaluationReportLoggingListener）：debug=true 或者在启动命令添加 `—debug`
 *  Springboot 的启动，主要创建了配置环境（environment）、事件监听（listeners）、应用上下文（applicationContext），并基于以上条件，在容器中开始创建需要的 Bean
 *  `SpringApplication.run(AppConfig.class,args);` 执行流程中有 `refreshContext(context);` 语句，该方法内部会解析在配置类上的注解，其中包括 @EnableAutoConfiguration（开启 Spring 应用程序上下文的自动配置），该注解使用 @Import 导入了 EnableAutoConfigurationImportSelector 配置类，而这个类会调用 `SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(), getBeanClassLoader());`方法去读取 jar 包中或项目中的 META-INF/spring.factories 文件中 key 为 EnableAutoConfiguration 对应的全限定类名（配置类）的值（类似 SPI 机制），主要作用是告诉 Spring Boot 该 stareter 需要加载的配置类，然后 Spring Boot 根据配置类使用的**条件注解**自动装配 Bean
 *  自动配置失败分析器：FailureAnalyzer

## 实现热部署 ##

 *  使用 [Spring Loaded][]
 *  使用 [spring-boot-devtools][]

# Spring Boot 的 Web 开发 #

 *  创建 no-web 应用：`SpringApplication.setWebEnvironment(false);` 或 `spring.main.web-environment=false` 或 `spring.main.web-application-type=none` 或 `setWebApplicationType(WebApplicationType.NONE)`
 *  [定制 MVC 配置][MVC]（如拦截器、格式化处理器、视图控制器等）：自定义一个配置类实现 WebMvcConfigurer 接口或继承 WebMvcConfigurerAdapter 抽象类（已过时）

``````````java
@Configuration
public class WebConfig implements WebMvcConfigurer  {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://domain2.com")
                .allowedMethods("PUT", "DELETE")
                .allowedHeaders("header1", "header2", "header3")
                .exposedHeaders("header1", "header2")
                .allowCredentials(false).maxAge(3600);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
    }

    @Override
    public Validator getValidator(); {
    }
}
``````````

 *  @EnableWebMvc + extends WebMvcConfigurerAdapter，在扩展类中重写父类的方法即可，这种方式会导致 WebMvcAutoConfiguration 不被自动装配
 *  extends WebMvcConfigurationSupport，在扩展类中重写父类的方法即可，这种方式会导致 WebMvcAutoConfiguration（`@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)`） 不被自动装配
 *  extends WebMvcConfigurerAdapter / implements WebMvcConfigurer，在扩展类中重写父类的方法即可，使用这种方式时 WebMvcAutoConfiguration 可以被自动装配

> 
> If you want to keep Spring Boot MVC features and you want to add additional [MVC configuration][] (interceptors, formatters, view controllers, and other features), you can add your own `@Configuration` class of type `WebMvcConfigurer` but **without** `@EnableWebMvc`. If you wish to provide custom instances of `RequestMappingHandlerMapping`, `RequestMappingHandlerAdapter`, or `ExceptionHandlerExceptionResolver`, you can declare a `WebMvcRegistrationsAdapter` instance to provide such components.
> 
> 
> If you want to take complete control of Spring MVC, you can add your own `@Configuration` annotated with `@EnableWebMvc`.
> 
> 
> [Spring Boot Reference Guide][]
> 

## 静态资源配置 ##

 *  WebMvcConfigurer\#addResourceHandlers
 *  默认情况下，Spring Boot 会从 classpath 下的 /static、/public、/resources、/META-INF/resources 下加载静态资源
 *  自定义静态资源**加载路径**：spring.resources.staticLocations，默认值为`classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/`（注意以 / 结尾）
 *  自定义静态资源**映射**：spring.mvc.static-path-pattern，默认值为`/**`（表示所有的访问都经过静态资源路径）
 *  可以把静态资源打成 jar 包，Spring Boot 会自动加载 /webjars/\*\* 下的所有 jar 包中的静态资源

## HTTP 响应缓存 ##

 *  常用配置（默认时间单位都是秒），ResourceProperties.Cache
    
     *  spring.resources.cache.cachecontrol.max-age=时间
     *  spring.resources.cache.cachecontrol.no-cache=true/false
     *  spring.resources.cache.cachecontrol.s-max-age=时间
 *  Controller 中手动设置缓存

``````````java
@GetMapping("/book/{id}")
public ResponseEntity<Book> showBook(@PathVariable Long id) {

    Book book = findBook(id);
    String version = book.getVersion();

    return ResponseEntity
            .ok()
            .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
            .eTag(version) // lastModified is also available
            .body(book);
}
``````````

## 拦截器配置 ##

 *  自定义拦截器：自定义一个类实现 Hanlderlnterceptor 接口或者继承 HandlerlnterceptorAdapter 抽象类，重写 preHandle 方法（在请求发生前执行），或重写 postHandle 方法（在请求完成后执行）
 *  注册拦截器：自定义一个配置类实现 WebMvcConfigurer 接口，在配置类中配置该拦截器的 Bean，重写 addInterceptors 方法（Spring Boot 2.x 对静态资源也进行了拦截）

## Freemarker 集成 ##

 *  添加依赖 spring-boot-starter-freemarker 后，Spring Boot 会自动添加 FreeMarkerViewResolver（Bean id 为 freeMarkerViewResolver）
 *  通过加了前缀和后缀（默认值为空和 '.ftl'）的视图名从默认的加载路径 "classpath:/templates/" 下加载模板文件
 *  spring.freemarker.expose-session-attributes：设定在 merge 到模板之前，是否将所有 HttpSession 的属性都添加到 model 中，默认 false
 *  前台访问的资源名，去掉后缀，找 controller 方法：
    
     *  找到 controller 方法：
        
         *  如果返回是的页面，返回的字符串加上 .ftl 再去 templates 下找资源，找不到报 404 错误
         *  如果返回的是 JSON 数据，此时如果资源名的后缀为 .html 或 .htm，则报 406 错误（当请求的后缀为 .html 或 .htm 时，会欺骗浏览器当做一个静态网页来解析，是一个简单的 SEO 优化，但返回的是 json 字符串，浏览器收到数据后不知该以哪种类型数据来进行解析，所以就会报 406 状态码）
     *  找不到 controller 方法，不去掉后缀，直接去 static 下找该文件，找不到报 404 错误

## Servlet 相关 ##

 *  可选内嵌 Web 容器：Tomcat、Jetty、Undertow、Reactor Netty

### 常见的服务器配置 ###

 *  监听 HTTP 请求的端口：server.port
 *  上下文路径：server.servlet.context-path
 *  session 是否持久化：server.servlet.session.persistent=false
 *  session 超时时间：server.servlet.session.timeout=30m
 *  session 数据存放位置：server.servlet.session.store-dir
 *  session-cookie 配置：server.servlet.session.cookie.*\*\**
 *  错误页面的位置：server.error.path=/error
 *  HTTP 响应压缩：server.compression.enabled=true
 *  Tomcat 特定配置
    
     *  server.tomcat.max-connections=10000
     *  server.tomcat.max-http-post-size=2MB
     *  server.tomcat.max-swallow-size=2MB
     *  server.tomcat.max-threads=200
     *  server.tomcat.min-spare-threads=10
 *  可通过编程方式修改容器器配置，自定义 Bean 实现 WebServerFactoryCustomizer 接口
 *  SSL 相关配置
    
     *  server.ssl.key-store
     *  server.ssl.key-store-type，JKS 或者 PKCS12
     *  server.ssl.key-store-password=
        

### Favicon 配置 ###

 *  关闭 Favicon：pring.mvc.favicon.enabled=false
 *  自定义 Favicon，favicon.ico

### 添加 Servlet 组件 ###

 *  方式 1：在配置类上添加 @ServletComponentScan，会自动扫描使用 @WebServlet、@WebFilter 和 @WebListener 的类，并自动注册到内嵌 servlet 容器（默认情况下从被注解类的 package 开始扫描）
 *  方式 2：使用 @Bean 创建 ServletRegistrationBean、FilterRegistrationBean 和 ServletListenerRegistrationBean 并添加转换和初始化参数来完成注册

## 文件上传 ##

 *  Spring Boot 采用 Servlet 3 javax.servlet.http.Part API 来支持文件上传（MultipartAutoConfiguration ）
 *  支持类型 multipart/form-data，使用 MultipartFile 接收上传的文件
 *  相关配置（Spring Boot 1.4 版本和 1.5 版本）
    
     *  spring.http.multipart.enabled=true：是否允许处理上传
     *  spring.http.multipart.maxFileSize=1MB：允许最大的单文件上传大小，单位可以是 kb、mb
     *  spring.http.multipart.maxRequestSize=10MB：允许的最大请求大小
 *  也可以通过 @Bean 创建一个 MultipartConfigElement 对象对上传进行配置
 *  上传文件的处理：由于应用是打成 jar 包，所以一般会把上传的文件放到其他位置，并通过设置 spring.resources.static-locations 来完成资源位置转换`file.path=D:/IdeaProjects/springbootdemo/uploads``spring.resources.static-locations=classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/,file:${file.path}`

## 错误处理 ##

 *  默认情况下，Spring Boot 把所有错误都重新定位到 /error 这个处理路径上，由 BasicErrorController 类完成处理
 *  自定义错误页面：将错误页面（可以是静态 HTML 文件，也可以是使用模板文件）添加到 /error 文件夹下（DefaultErrorViewResolver），文件名必须是明确的状态码或一系列标签，如 resources/public/error/404.html、resources/templates/error/5xx.ftl

``````````java
@Configuration
public class ContainerConfig implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage[] errorPages = new ErrorPage[2];
        errorPages[0] = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500");
        errorPages[1] = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404");
        registry.addErrorPages(errorPages);
    }
}
``````````

 *  对于复杂的转换，添加实现 ErrorViewResolver 接口的 Bean
 *  使用统一的异常处理类

``````````java
@ControllerAdvice
public class GlobalErrorAdvice {
    @ExceptionHandler(value = Exception.class)
    public String error(Model model, Exception e) {
        model.addAttribute("ex", e);
        return "err";
    }
}
``````````

## Spring Session ##

 *  支持的存储：JDBC、Redis、Hazelcast、MongoDB
 *  实现原理：通过定制的 HttpServletRequest 返回定制的 HttpSession
 *  相关类：SessionRepositoryRequestWrapper、SessionRepositoryFilter、DelegatingFilterProxy
 *  基于 Redis 的 HttpSession：添加依赖 spring-session-data-redis；在启动类上添加 @EnableRedisHttpSession
 *  相关配置
    
     *  spring.session.store-type=redis
     *  spring.session.timeout=
     *  spring.session.redis.flush-mode=on-save
     *  spring.session.redis.namespace=spring:session

# Spring Boot 的 RDBMS 访问 #

## 配置数据源 ##

 *  Spring Boot 使用 DataSourceConfiguration 类来完成 datasource 的自动创建
 *  Spring Boot 2.x 默认使用 HikariCP 作为数据库连接池（Spring Boot 2.x 默认使用 Tomcat 连接池）
 *  如果需要使用其它数据库连接池，需在 spring-boot-starter-jdbc 依赖中排除 HikariCP
 *  添加配置（注意：HikariCP 使用 jdbc-url 属性，[配置项][Link 2]）spring.datasource.url=jdbc:mysql://localhost/testspring.datasource.username=dbuserspring.datasource.password=dbpassspring.datasource.driver-class-name=com.mysql.jdbc.Driver
 *  方式 1：使用 @Bean 创建一个 DataSource 对象，并设置相关属性
 *  方式 2：添加依赖 spring-boot-starter-jdbc

## 配置多个数据源 ##

 *  配置多个数据源时，必须将其中一个 DataSource 实例例标记为 @Primary，否则需要在启动时排除相关的自动配置类（DataSourceAutoConfiguration、DataSourceTransactionManagerAutoConfiguration、JdbcTemplateAutoConfiguration），再手动配置相关 Bean（PlatformTransactionManager、JdbcTemplate）

``````````
app.datasource.first.url=jdbc:mysql://localhost/first
app.datasource.first.username=dbuser
app.datasource.first.password=dbpass
app.datasource.first.configuration.maximum-pool-size=30

app.datasource.second.url=jdbc:mysql://localhost/second
app.datasource.second.username=dbuser
app.datasource.second.password=dbpass
app.datasource.second.max-total=30
``````````

``````````java
// 方式 1：使用 DataSourceProperties 来构造数据源
  @Bean
  @Primary
  @ConfigurationProperties("app.datasource.first")
  public DataSourceProperties firstDataSourceProperties() {
      return new DataSourceProperties();
  }

  @Bean
  @Primary
  @ConfigurationProperties("app.datasource.first.configuration")
  public HikariDataSource firstDataSource() {
      return firstDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
  }

  // 方式 2：使用 DataSourceBuilder 来构造数据源
  @Bean
  @ConfigurationProperties("app.datasource.second")
  public HikariDataSource secondDataSource() {
      return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }
``````````

## 操作数据库 ##

### 使用 JdbcTemplate 操作数据库 ###

### 使用 JPA（Hibernate）操作数据 ###

 *  添加依赖 spring-boot-starter-data-jpa

### 集成 MyBatis ###

 *  添加依赖 mybatis-spring-boot-starter，[官网][Link 3]
 *  mapper 接口、mapper.xml、mybatis.xml
 *  添加配置

``````````
mybatis.config-location=classpath:mybatis.xml
mybatis.mapper-locations=classpath:mapper/*Mapper.xml
mybatis.type-aliases-package=com.example.springbootdemo.domain
logging.level.com.example.springbootdemo=debug
``````````

 *  扫描 mapper 接口
    
     *  方式 1：在配置类使用 `@MapperScan(basePackages={"com.example1..mapper", "com.example.mapper."})` 修饰
     *  方式 2：使用 @Bean 创建一个 MapperScannerConfigurer 对象，并设置相关属性，所在的配置类需添加 `@AutoConfigureAfter(MybatisAutoConfiguration.class)`
     *  方式 3：在 每个 Mapper 接口类上增加 @Mapper
 *  自定义类型转换
    
     *  自定义类型处理器继承 BaseTypeHandler
     *  添加配置 `mybatis.type-handlers-package=com.example.springbootdemo.handler`
 *  [集成 MyBatis, 分页插件 PageHelper, 通用 Mapper][MyBatis_ _ PageHelper_ _ Mapper]
    

## Spring Boot 的事务支持 ##

 *  添加依赖 spring-boot-starter-aop
 *  在 Spring Boot 中，无须显式开启使用 @EnableTransactionManagement 注解，直接在 service 上使用 @Transactional 标注类或方法
 *  手动配置事务管理器

``````````java
@Bean
@Resource
public PlatformTransactionManager firstTxManager(DataSource firstDataSource) {
    return new DataSourceTransactionManager(firstDataSource);
}
``````````

## 记录 SQL 日志 ##

 *  P6SQL， https://github.com/p6spy/p6spy

## 通过 Reactive 的方式访问 ##

 *  R2DBC，Reactive Relational Database Connectivity
 *  [Spring Data R2DBC][]，目前支持的数据库：Postgres、H2、Microsoft SQL Server
 *  相关的类
    
     *  ConnectionFactory
     *  R2dbcCustomConversions
     *  DatabaseClient：`execute().sql(SQL)`、`inTransaction(db -> {})`
     *  R2dbcExceptionTranslator、SqlErrorCodeR2dbcExceptionTranslator

# 集成 Redis #

 *  添加依赖 spring-boot-starter-data-redis（2. x 版本的 starter 在默认的情况下使用 Lettuce 作为 Redis 连接池）
 *  添加配置

``````````
spring.redis.database=0
spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.password=
``````````

 *  注入 RedisTemplate 或 StringRedisTemplate 对象，调用其方法
    
     *  RedisTemplate 默认使用的序列化器是 JdkSerializationRedisSerializer，序列化成 byte\[\]
     *  StringRedisTemplate 使用的序列化器是 StringRedisSerializer，key、value、hashKey、hashValue 序列化成 String

``````````java
@Bean
public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Serializable> template = new RedisTemplate<>();
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setConnectionFactory(redisConnectionFactory);
    return template;
}
``````````

 *  配置连接工厂 LettuceConnectionFactory、JedisConnectionFactory：RedisStandaloneConfiguration、RedisSentinelConfiguration、RedisClusterConfiguration
 *  Lettuce 对读写分离的支持：LettuceClientConfiguration、LettucePoolingClientConfiguration、LettuceClientConfigurationBuilderCustomizer
 *  Lettuce 对 Reactive 的支持：ReactiveRedisConnection、ReactiveRedisConnectionFactory、ReactiveRedisTemplate
 *  自定义类型转换：使用 @Bean 手动创建 RedisCustomConversions

## 使用 Spring 缓存注解操作 Redis ##

 *  添加依赖 spring-boot-starter-cache
 *  在启动类上添加 @EnableCaching，开启注解式的缓存支持
 *  配置 Redis 缓存管理器 RedisCacheManager

``````````java
# spring.cache.type= REDIS # 缓存类型，在默认的情况下，Spring 会自动根据上下文检测
# spring.cache.redis.key-prefix= # Redis 的键前缀
# spring.cache.redis.use-key-prefix=true # 是否启用 Redis 的键前缀，默认是 true
spring.cache.redis.cache-null-values=true # 是否允许 Redis 缓存空值，默认是 true
spring.cache.redis.time-to-live=0ms # 缓存超时时间戳，配置为 0 则不设置超时时间，默认是永不过期
``````````

 *  Spring 缓存相关注解
    
     *  @CachePut 表示将方法结果返回存放到缓存中，如 `@CachePut(value = "redisCache", condition = "#result != 'null'", key = "'redisuser'+#result.id")`
     *  @Cacheable 表示先从缓存中通过定义的键查询，如果可以查询到数据，则返回，否则执行该方法，返回数据，并且将返回结果保存到缓存中，如 `@Cacheable(value = "redisCache", key = "'redisuser'+#id")`
     *  @CacheEvict 通过定义的键移除缓存，属性 beforelnvocation 表示在方法之前或者之后移除缓存，其默认值为 false，在方法成功返回后将缓存移除，如`@CacheEvict(value =”redisCachen, key = "'redisuser'+#id" , beforeinvocation = false)`
     *  @Caching，属性 cacheable、CachePut、CacheEvict
     *  @CacheConfig，修饰类，属性 cacheNames、cacheManager
 *  属性 key 的值是一个 SpEL 表达式，如 `#参数名` 代表对应的实参，`#result` 代表返回的结果对象
 *  默认 key 的生成策略：如果方法没有参数，则使用 0 作为 key；如果只有一个参数的话则使用该参数作为 key；如果参数多余一个的话则使用所有参数的 hashCode 作为 key
 *  Redis 缓存管理器默认会使用 `<cacheName>::<key>`的形式作为键保存数据
 *  **缓存注解自调用失效**：在添加有缓存注解的方法 A 中调用添加有缓存注解的另一方法 B 时，方法 B 上的缓存注解将会失效
 *  自定义缓存管理器：使用 @Bean 手动创建 RedisCacheManager

# 集成 MongoDB #

 *  添加依赖 spring-boot-starter-data-mongodb
 *  添加配置

``````````
spring.data.mongodb.uri=mongodb://用户名:密码@127.0.0.1:27017/数据库
``````````

 *  注入 MongoTemplate 对象，调用其方法，或者使用 JPA（MongoRepository 接口）
 *  自定义类型转换：使用 @Bean 手动创建 MongoCustomConversions
 *  对 Reactive 的支持
    
     *  依赖 spring-boot-starter-data-mongodb-reactive
     *  ReactiveMongoClientFactoryBean、ReactiveMongoDatabaseFactory、ReactiveMongoTemplate

# 集成 Dubbo #

 *  添加依赖 [org.apache.dubbo:dubbo-spring-boot-starter][org.apache.dubbo_dubbo-spring-boot-starter]、org.apache.dubbo:dubbo
 *  添加配置

``````````properties
# 服务提供者的配置文件
 spring.application.name=xxx # 当前应用名称
 # dubbo.application.name=${spring.application.name} # Dubbo 应用名默认值为 ${spring.application.name}
 embedded.zookeeper.port = 2181
 dubbo.registry.address=zookeeper://127.0.0.1:${embedded.zookeeper.port} # 注册中心的地址
 dubbo.scan.base-packages=xxx # 扫描 Dubbo 注解的包路径
 dubbo.protocol.name=dubbo # 服务提供者使用的协议
 dubbo.protocol.port=20880 # 服务提供者所使用协议的端口
 dubbo.provider.timeout=5000 #（可选）远程服务超时时间

 demo.service.version=1.0.0 # DemoService version
``````````

``````````properties
# 服务消费者的配置文件
 spring.application.name=xxx # 当前应用名称
 embedded.zookeeper.port=2181
 dubbo.registry.address=zookeeper://127.0.0.1:${embedded.zookeeper.port} # 注册中心的地址
 dubbo.consumer.timeout=5000 #（可选）远程服务调用超时（默认为 1000）
 dubbo.consumer.check=false #（可选）启动时检查提供者是否存在，true 报错，false 忽略（默认 true）

 demo.service.version=1.0.0 # DemoService version
``````````

 *  发布服务，在接口的实现类上添加 `@Service(version = "${demo.service.version}")`调用服务，通过 `@Reference(version = "${demo.service.version}")` 注入代理的接口实现类

# 集成 ActiveMQ #

 *  添加依赖 spring-boot-starter-activemq
 *  添加配置

``````````properties
spring.activemq.broker-url=tcp://127.0.0.1:61616
spring.activemq.user=admin
spring.activemq.password=admin
``````````

 *  使用 @Bean 创建 Destination 消息地点对象（ActiveMQTopic 及 ActiveMQQueue）；使用 @Bean 创建消息监听容器 JmsListenerContainerFactory 对象（DefaultJmsListenerContainerFactory），并设置属性（pubSubDomain、connectionFactory、sessionAcknowledgeMode 等）
 *  消息生产者 Bean：注入 JmsTemplate 和 Destination 对象，通过调用 `jmsTemplate.convertAndSend(destination, content);` 发布消息消息消费者 Bean：在消息处理的方法上使用 @JmsListener 注解，并通过属性 destination、containerFactory 指要监听的消息地点和使用的消息监听容器

# 集成 RabbitMQ #

# Spring Boot Actuator #

 *  目的：监控并管理应用程序
 *  访问方式：HTTP、JMX
 *  依赖：spring-boot-starter-actuator

## 端点 ##

 * 常用 Endpoint

   | ID             | 说明                                      | 默认启用 | 默认 HTTP | 默认 JMX |
   | -------------- | ----------------------------------------- | -------- | --------- | -------- |
   | beans          | 显示容器中的 Bean 列表                    | Y        | N         | Y        |
   | caches         | 显示应用中的缓存                          | Y        | N         | Y        |
   | conditions     | 显示配置条件的计算情况                    | Y        | N         | Y        |
   | configprops    | 显示 @ConfigurationProperties 的信息      | Y        | N         | Y        |
   | env            | 显示 ConfigurableEnvironment 中的属性     | Y        | N         | Y        |
   | health         | 显示健康检查信息                          | Y        | **Y**     | Y        |
   | httptrace      | 显示 HTTP Trace 信息（默认为最新 100 次） | Y        | N         | Y        |
   | info           | 显示当前应用信息                          | Y        | **Y**     | Y        |
   | loggers        | 显示并更新日志配置                        | Y        | N         | Y        |
   | metrics        | 显示应用的度量信息                        | Y        | N         | Y        |
   | mappings       | 显示所有的 @RequestMapping 信息           | Y        | N         | Y        |
   | scheduledtasks | 显示应用的调度任务信息                    | Y        | N         | Y        |
   | shutdown       | 优雅地关闭应用程序                        | **N**    | N         | Y        |
   | threaddump     | 执行 Thread Dump                          | Y        | N         | Y        |
   | heapdump       | 返回 Heap Dump 文件，格式为 HPROF         | Y        | N         | N/A      |
   | prometheus     | 返回可供 Prometheus 抓取的信息            | Y        | N         | N/A      |

 *  端口与路径
    
     *  management.server.address=
     *  management.server.port=
     *  management.endpoints.web.base-path=/actuator
     *  management.endpoints.web.path-mapping.=路径
 *  启用 Endpoint
    
     *  management.endpoint..enabled=true
     *  management.endpoints.enabled-by-default=false
 *  暴露 Endpoint
    
     *  management.endpoints.jmx.exposure.exclude=
     *  management.endpoints.jmx.exposure.include=\*
     *  management.endpoints.web.exposure.exclude=
     *  management.endpoints.web.exposure.include=info, health
 *  访问 Actuator Endpoint
    
     *  HTTP 访问：/actuator/
     *  使用 Jconsle 或 Visual VM 通过 JMX 查看

## 应用信息 ##

 *  访问 info 端点
 *  相关配置项
    
     *  info.app.author=Sdky
     *  info.app.encoding=@project.build.sourceEncoding@
     *  info.app.java.source=@java.version@
     *  info.app.java.target=@java.version@

## 健康信息 ##

 *  目的：检查应用程序的运行状态
 *  访问 health 端点
 *  内置状态：DOWN - 503、OUT\_OF\_SERVICE - 503、UP - 200、UNKNOWN - 200
 *  机制：通过 HealthIndicatorRegistry 收集信息，HealthIndicator 实现具体检查逻辑
 *  相关配置项
    
     *  management.health.defaults.enabled=true|false
     *  management.health..enabled=true
     *  management.endpoint.health.show-details=never|whenauthorized|always
 *  内置 HealthIndicator：DiskSpaceHealthIndicator、DataSourceHealthIndicator、RedisHealthIndicator、MongoHealthIndicator、JmsHealthIndicator、MailHealthIndicator、RabbitHealthIndicator、InfluxDbHealthIndicator、CassandraHealthIndicator、Neo4jHealthIndicator、ElasticsearchHealthIndicator、SolrHealthIndicator
 *  自定义 HealthIndicator：自定义 Bean 实现 HealthIndicator 接口

## 度量 ##

 *  [Micrometer][], an application metrics facade that supports numerous monitoring systems.
 *  目的：获取运行数据
 *  访问 metrics 或 prometheus 端点
 *  相关配置项
    
     *  management.metrics.export.\*
     *  management.metrics.tags.\*
     *  management.metrics.enable.\*
     *  management.metrics.distribution.\*
     *  management.metrics.web.server.auto-time-requests
 *  自定义度量指标

# 常见应用属性 #

[Common application properties][]

META-INF/spring-configuration-metadata.json 文件

``````````
# ===================================================================
# COMMON SPRING BOOT PROPERTIES
#
# This sample file is provided as a guideline. Do NOT copy it in its
# entirety to your own application.               ^^^
# ===================================================================
``````````

## Core Properties ##

``````````
debug=false # Enable debug logs.
trace=false # Enable trace logs.
``````````

### Logging ###

``````````
# LOGGING
logging.config= # Location of the logging configuration file. For instance, `classpath:logback.xml` for Logback.
logging.exception-conversion-word=%wEx # Conversion word used when logging exceptions.
logging.file= # Log file name (for instance, `myapp.log`). Names can be an exact location or relative to the current directory.
logging.file.max-history=0 # Maximum of archive log files to keep. Only supported with the default logback setup.
logging.file.max-size=10MB # Maximum log file size. Only supported with the default logback setup.
logging.group.*= # Log groups to quickly change multiple loggers at the same time. For instance, `logging.level.db=org.hibernate,org.springframework.jdbc`.
logging.level.*= # Log levels severity mapping. For instance, `logging.level.org.springframework=DEBUG`.
logging.path= # Location of the log file. For instance, `/var/log`.
logging.pattern.console= # Appender pattern for output to the console. Supported only with the default Logback setup.
logging.pattern.dateformat=yyyy-MM-dd HH:mm:ss.SSS # Appender pattern for log date format. Supported only with the default Logback setup.
logging.pattern.file= # Appender pattern for output to a file. Supported only with the default Logback setup.
logging.pattern.level=%5p # Appender pattern for log level. Supported only with the default Logback setup.
logging.register-shutdown-hook=false # Register a shutdown hook for the logging system when it is initialized.
``````````

### AOP ###

``````````
# AOP
spring.aop.auto=true # Add @EnableAspectJAutoProxy.
spring.aop.proxy-target-class=true # Whether subclass-based (CGLIB) proxies are to be created (true), as opposed to standard Java interface-based proxies (false).
``````````

### Identity ###

``````````
# IDENTITY (ContextIdApplicationContextInitializer)
spring.application.name= # Application name.
``````````

### Admin ###

``````````
# ADMIN (SpringApplicationAdminJmxAutoConfiguration)
spring.application.admin.enabled=false # Whether to enable admin features for the application.
spring.application.admin.jmx-name=org.springframework.boot:type=Admin,name=SpringApplication # JMX name of the application admin MBean.
``````````

### Auto-configuration ###

``````````
# AUTO-CONFIGURATION
spring.autoconfigure.exclude= # Auto-configuration classes to exclude.
``````````

### Banner ###

``````````
# BANNER
spring.banner.charset=UTF-8 # Banner file encoding.
spring.banner.location=classpath:banner.txt # Banner text resource location.
spring.banner.image.location=classpath:banner.gif # Banner image file location (jpg or png can also be used).
spring.banner.image.width=76 # Width of the banner image in chars.
spring.banner.image.height= # Height of the banner image in chars (default based on image height).
spring.banner.image.margin=2 # Left hand image margin in chars.
spring.banner.image.invert=false # Whether images should be inverted for dark terminal themes.
``````````

### Spring Core ###

``````````
# SPRING CORE
spring.beaninfo.ignore=true # Whether to skip search of BeanInfo classes.
``````````

### Spring Cache ###

``````````
# SPRING CACHE (CacheProperties)
spring.cache.cache-names= # Comma-separated list of cache names to create if supported by the underlying cache manager.
spring.cache.caffeine.spec= # The spec to use to create caches. See CaffeineSpec for more details on the spec format.
spring.cache.couchbase.expiration= # Entry expiration. By default the entries never expire. Note that this value is ultimately converted to seconds.
spring.cache.ehcache.config= # The location of the configuration file to use to initialize EhCache.
spring.cache.infinispan.config= # The location of the configuration file to use to initialize Infinispan.
spring.cache.jcache.config= # The location of the configuration file to use to initialize the cache manager.
spring.cache.jcache.provider= # Fully qualified name of the CachingProvider implementation to use to retrieve the JSR-107 compliant cache manager. Needed only if more than one JSR-107 implementation is available on the classpath.
spring.cache.redis.cache-null-values=true # Allow caching null values.
spring.cache.redis.key-prefix= # Key prefix.
spring.cache.redis.time-to-live= # Entry expiration. By default the entries never expire.
spring.cache.redis.use-key-prefix=true # Whether to use the key prefix when writing to Redis.
spring.cache.type= # Cache type. By default, auto-detected according to the environment.
``````````

### Spring Config ###

``````````
# SPRING CONFIG - using environment property only (ConfigFileApplicationListener)
spring.config.additional-location= # Config file locations used in addition to the defaults.
spring.config.location= # Config file locations that replace the defaults.
spring.config.name=application # Config file name.
``````````

### Hazelcast ###

``````````
# HAZELCAST (HazelcastProperties)
spring.hazelcast.config= # The location of the configuration file to use to initialize Hazelcast.
``````````

### Project Information ###

``````````
# PROJECT INFORMATION (ProjectInfoProperties)
spring.info.build.encoding=UTF-8 # File encoding.
spring.info.build.location=classpath:META-INF/build-info.properties # Location of the generated build-info.properties file.
spring.info.git.encoding=UTF-8 # File encoding.
spring.info.git.location=classpath:git.properties # Location of the generated git.properties file.
``````````

### Email ###

``````````
# ### Email (MailProperties)
spring.mail.default-encoding=UTF-8 # Default MimeMessage encoding.
spring.mail.host= # SMTP server host. For instance, `smtp.example.com`.
spring.mail.jndi-name= # Session JNDI name. When set, takes precedence over other Session settings.
spring.mail.password= # Login password of the SMTP server.
spring.mail.port= # SMTP server port.
spring.mail.properties.*= # Additional JavaMail Session properties.
spring.mail.protocol=smtp # Protocol used by the SMTP server.
spring.mail.test-connection=false # Whether to test that the mail server is available on startup.
spring.mail.username= # Login user of the SMTP server.
``````````

### Application Settings ###

``````````
# APPLICATION SETTINGS (SpringApplication)
spring.main.allow-bean-definition-overriding=false # Whether bean definition overriding, by registering a definition with the same name as an existing definition, is allowed.
spring.main.banner-mode=console # Mode used to display the banner when the application runs.
spring.main.sources= # Sources (class names, package names, or XML resource locations) to include in the ApplicationContext.
spring.main.web-application-type= # Flag to explicitly request a specific type of web application. If not set, auto-detected based on the classpath.
``````````

### File Encoding ###

``````````
# FILE ENCODING (FileEncodingApplicationListener)
spring.mandatory-file-encoding= # Expected character encoding the application must use.
``````````

### Profiles ###

``````````
# PROFILES
spring.profiles.active= # Comma-separated list of active profiles. Can be overridden by a command line switch.
spring.profiles.include= # Unconditionally activate the specified comma-separated list of profiles (or list of profiles if using YAML).
``````````

### Quartz Scheduler ###

``````````
# QUARTZ SCHEDULER (QuartzProperties)
spring.quartz.auto-startup=true # Whether to automatically start the scheduler after initialization.
spring.quartz.jdbc.comment-prefix=-- # Prefix for single-line comments in SQL initialization scripts.
spring.quartz.jdbc.initialize-schema=embedded # Database schema initialization mode.
spring.quartz.jdbc.schema=classpath:org/quartz/impl/jdbcjobstore/tables_@@platform@@.sql # Path to the SQL file to use to initialize the database schema.
spring.quartz.job-store-type=memory # Quartz job store type.
spring.quartz.overwrite-existing-jobs=false # Whether configured jobs should overwrite existing job definitions.
spring.quartz.properties.*= # Additional Quartz Scheduler properties.
spring.quartz.scheduler-name=quartzScheduler # Name of the scheduler.
spring.quartz.startup-delay=0s # Delay after which the scheduler is started once initialization completes.
spring.quartz.wait-for-jobs-to-complete-on-shutdown=false # Whether to wait for running jobs to complete on shutdown.
``````````

### Task Execution ###

``````````
# TASK EXECUTION  (TaskExecutionProperties)
spring.task.execution.pool.allow-core-thread-timeout=true # Whether core threads are allowed to time out. This enables dynamic growing and shrinking of the pool.
spring.task.execution.pool.core-size=8 # Core number of threads.
spring.task.execution.pool.keep-alive=60s # Time limit for which threads may remain idle before being terminated.
spring.task.execution.pool.max-size= # Maximum allowed number of threads. If tasks are filling up the queue, the pool can expand up to that size to accommodate the load. Ignored if the queue is unbounded.
spring.task.execution.pool.queue-capacity= # Queue capacity. An unbounded capacity does not increase the pool and therefore ignores the "max-size" property.
spring.task.execution.thread-name-prefix=task- # Prefix to use for the names of newly created threads.
``````````

### Task Scheduling ###

``````````
# TASK SCHEDULING  (TaskSchedulingProperties)
spring.task.scheduling.pool.size=1 # Maximum allowed number of threads.
spring.task.scheduling.thread-name-prefix=scheduling- # Prefix to use for the names of newly created threads.
``````````

## Web Properties ##

### Embedded Server Configuration ###

``````````
# EMBEDDED SERVER CONFIGURATION (ServerProperties)
server.address= # Network address to which the server should bind.
server.compression.enabled=false # Whether response compression is enabled.
server.compression.excluded-user-agents= # Comma-separated list of user agents for which responses should not be compressed.
server.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json,application/xml # Comma-separated list of MIME types that should be compressed.
server.compression.min-response-size=2KB # Minimum "Content-Length" value that is required for compression to be performed.
server.connection-timeout= # Time that connectors wait for another HTTP request before closing the connection. When not set, the connector's container-specific default is used. Use a value of -1 to indicate no (that is, an infinite) timeout.
server.error.include-exception=false # Include the "exception" attribute.
server.error.include-stacktrace=never # When to include a "stacktrace" attribute.
server.error.path=/error # Path of the error controller.
server.error.whitelabel.enabled=true # Whether to enable the default error page displayed in browsers in case of a server error.
server.http2.enabled=false # Whether to enable HTTP/2 support, if the current environment supports it.
server.max-http-header-size=8KB # Maximum size of the HTTP message header.
server.port=8080 # Server HTTP port.
server.server-header= # Value to use for the Server response header (if empty, no header is sent).
server.use-forward-headers= # Whether X-Forwarded-* headers should be applied to the HttpRequest.
server.servlet.context-parameters.*= # Servlet context init parameters.
server.servlet.context-path= # Context path of the application.
server.servlet.application-display-name=application # Display name of the application.
server.servlet.jsp.class-name=org.apache.jasper.servlet.JspServlet # Class name of the servlet to use for JSPs.
server.servlet.jsp.init-parameters.*= # Init parameters used to configure the JSP servlet.
server.servlet.jsp.registered=true # Whether the JSP servlet is registered.
server.servlet.session.cookie.comment= # Comment for the session cookie.
server.servlet.session.cookie.domain= # Domain for the session cookie.
server.servlet.session.cookie.http-only= # Whether to use "HttpOnly" cookies for session cookies.
server.servlet.session.cookie.max-age= # Maximum age of the session cookie. If a duration suffix is not specified, seconds will be used.
server.servlet.session.cookie.name= # Session cookie name.
server.servlet.session.cookie.path= # Path of the session cookie.
server.servlet.session.cookie.secure= # Whether to always mark the session cookie as secure.
server.servlet.session.persistent=false # Whether to persist session data between restarts.
server.servlet.session.store-dir= # Directory used to store session data.
server.servlet.session.timeout=30m # Session timeout. If a duration suffix is not specified, seconds will be used.
server.servlet.session.tracking-modes= # Session tracking modes.
server.ssl.ciphers= # Supported SSL ciphers.
server.ssl.client-auth= # Client authentication mode.
server.ssl.enabled=true # Whether to enable SSL support.
server.ssl.enabled-protocols= # Enabled SSL protocols.
server.ssl.key-alias= # Alias that identifies the key in the key store.
server.ssl.key-password= # Password used to access the key in the key store.
server.ssl.key-store= # Path to the key store that holds the SSL certificate (typically a jks file).
server.ssl.key-store-password= # Password used to access the key store.
server.ssl.key-store-provider= # Provider for the key store.
server.ssl.key-store-type= # Type of the key store.
server.ssl.protocol=TLS # SSL protocol to use.
server.ssl.trust-store= # Trust store that holds SSL certificates.
server.ssl.trust-store-password= # Password used to access the trust store.
server.ssl.trust-store-provider= # Provider for the trust store.
server.ssl.trust-store-type= # Type of the trust store.
server.tomcat.accept-count=100 # Maximum queue length for incoming connection requests when all possible request processing threads are in use.
server.tomcat.accesslog.buffered=true # Whether to buffer output such that it is flushed only periodically.
server.tomcat.accesslog.directory=logs # Directory in which log files are created. Can be absolute or relative to the Tomcat base dir.
server.tomcat.accesslog.enabled=false # Enable access log.
server.tomcat.accesslog.file-date-format=.yyyy-MM-dd # Date format to place in the log file name.
server.tomcat.accesslog.pattern=common # Format pattern for access logs.
server.tomcat.accesslog.prefix=access_log # Log file name prefix.
server.tomcat.accesslog.rename-on-rotate=false # Whether to defer inclusion of the date stamp in the file name until rotate time.
server.tomcat.accesslog.request-attributes-enabled=false # Set request attributes for the IP address, Hostname, protocol, and port used for the request.
server.tomcat.accesslog.rotate=true # Whether to enable access log rotation.
server.tomcat.accesslog.suffix=.log # Log file name suffix.
server.tomcat.additional-tld-skip-patterns= # Comma-separated list of additional patterns that match jars to ignore for TLD scanning.
server.tomcat.background-processor-delay=10s # Delay between the invocation of backgroundProcess methods. If a duration suffix is not specified, seconds will be used.
server.tomcat.basedir= # Tomcat base directory. If not specified, a temporary directory is used.
server.tomcat.internal-proxies=10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|\\
        192\\.168\\.\\d{1,3}\\.\\d{1,3}|\\
        169\\.254\\.\\d{1,3}\\.\\d{1,3}|\\
        127\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|\\
        172\\.1[6-9]{1}\\.\\d{1,3}\\.\\d{1,3}|\\
        172\\.2[0-9]{1}\\.\\d{1,3}\\.\\d{1,3}|\\
        172\\.3[0-1]{1}\\.\\d{1,3}\\.\\d{1,3}\\
        0:0:0:0:0:0:0:1\\
        ::1 # Regular expression that matches proxies that are to be trusted.
server.tomcat.max-connections=10000 # Maximum number of connections that the server accepts and processes at any given time.
server.tomcat.max-http-post-size=2MB # Maximum size of the HTTP post content.
server.tomcat.max-swallow-size=2MB # Maximum amount of request body to swallow.
server.tomcat.max-threads=200 # Maximum amount of worker threads.
server.tomcat.min-spare-threads=10 # Minimum amount of worker threads.
server.tomcat.port-header=X-Forwarded-Port # Name of the HTTP header used to override the original port value.
server.tomcat.protocol-header= # Header that holds the incoming protocol, usually named "X-Forwarded-Proto".
server.tomcat.protocol-header-https-value=https # Value of the protocol header indicating whether the incoming request uses SSL.
server.tomcat.redirect-context-root=true # Whether requests to the context root should be redirected by appending a / to the path.
server.tomcat.remote-ip-header= # Name of the HTTP header from which the remote IP is extracted. For instance, `X-FORWARDED-FOR`.
server.tomcat.resource.allow-caching=true # Whether static resource caching is permitted for this web application.
server.tomcat.resource.cache-ttl= # Time-to-live of the static resource cache.
server.tomcat.uri-encoding=UTF-8 # Character encoding to use to decode the URI.
server.tomcat.use-relative-redirects= # Whether HTTP 1.1 and later location headers generated by a call to sendRedirect will use relative or absolute redirects.
``````````

### FreeMarker ###

``````````
# FREEMARKER (FreeMarkerProperties)
spring.freemarker.allow-request-override=false # Whether HttpServletRequest attributes are allowed to override (hide) controller generated model attributes of the same name.
spring.freemarker.allow-session-override=false # Whether HttpSession attributes are allowed to override (hide) controller generated model attributes of the same name.
spring.freemarker.cache=false # Whether to enable template caching.
spring.freemarker.charset=UTF-8 # Template encoding.
spring.freemarker.check-template-location=true # Whether to check that the templates location exists.
spring.freemarker.content-type=text/html # Content-Type value.
spring.freemarker.enabled=true # Whether to enable MVC view resolution for this technology.
spring.freemarker.expose-request-attributes=false # Whether all request attributes should be added to the model prior to merging with the template.
spring.freemarker.expose-session-attributes=false # Whether all HttpSession attributes should be added to the model prior to merging with the template.
spring.freemarker.expose-spring-macro-helpers=true # Whether to expose a RequestContext for use by Spring's macro library, under the name "springMacroRequestContext".
spring.freemarker.prefer-file-system-access=true # Whether to prefer file system access for template loading. File system access enables hot detection of template changes.
spring.freemarker.prefix= # Prefix that gets prepended to view names when building a URL.
spring.freemarker.request-context-attribute= # Name of the RequestContext attribute for all views.
spring.freemarker.settings.*= # Well-known FreeMarker keys which are passed to FreeMarker's Configuration.
spring.freemarker.suffix=.ftl # Suffix that gets appended to view names when building a URL.
spring.freemarker.template-loader-path=classpath:/templates/ # Comma-separated list of template paths.
spring.freemarker.view-names= # White list of view names that can be resolved.
``````````

### Groovy Templates ###

``````````
# GROOVY TEMPLATES (GroovyTemplateProperties)
spring.groovy.template.allow-request-override=false # Whether HttpServletRequest attributes are allowed to override (hide) controller generated model attributes of the same name.
spring.groovy.template.allow-session-override=false # Whether HttpSession attributes are allowed to override (hide) controller generated model attributes of the same name.
spring.groovy.template.cache=false # Whether to enable template caching.
spring.groovy.template.charset=UTF-8 # Template encoding.
spring.groovy.template.check-template-location=true # Whether to check that the templates location exists.
spring.groovy.template.configuration.*= # See GroovyMarkupConfigurer
spring.groovy.template.content-type=text/html # Content-Type value.
spring.groovy.template.enabled=true # Whether to enable MVC view resolution for this technology.
spring.groovy.template.expose-request-attributes=false # Whether all request attributes should be added to the model prior to merging with the template.
spring.groovy.template.expose-session-attributes=false # Whether all HttpSession attributes should be added to the model prior to merging with the template.
spring.groovy.template.expose-spring-macro-helpers=true # Whether to expose a RequestContext for use by Spring's macro library, under the name "springMacroRequestContext".
spring.groovy.template.prefix= # Prefix that gets prepended to view names when building a URL.
spring.groovy.template.request-context-attribute= # Name of the RequestContext attribute for all views.
spring.groovy.template.resource-loader-path=classpath:/templates/ # Template path.
spring.groovy.template.suffix=.tpl # Suffix that gets appended to view names when building a URL.
spring.groovy.template.view-names= # White list of view names that can be resolved.
``````````

### HTTP ###

``````````
# HTTP (HttpProperties)
spring.http.converters.preferred-json-mapper= # Preferred JSON mapper to use for HTTP message conversion. By default, auto-detected according to the environment.
spring.http.encoding.charset=UTF-8 # Charset of HTTP requests and responses. Added to the "Content-Type" header if not set explicitly.
spring.http.encoding.enabled=true # Whether to enable http encoding support.
spring.http.encoding.force= # Whether to force the encoding to the configured charset on HTTP requests and responses.
spring.http.encoding.force-request= # Whether to force the encoding to the configured charset on HTTP requests. Defaults to true when "force" has not been specified.
spring.http.encoding.force-response= # Whether to force the encoding to the configured charset on HTTP responses.
spring.http.encoding.mapping= # Locale in which to encode mapping.
spring.http.log-request-details=false # Whether logging of (potentially sensitive) request details at DEBUG and TRACE level is allowed.
``````````

### Multipart ###

``````````
# MULTIPART (MultipartProperties)
spring.servlet.multipart.enabled=true # Whether to enable support of multipart uploads.
spring.servlet.multipart.file-size-threshold=0B # Threshold after which files are written to disk.
spring.servlet.multipart.location= # Intermediate location of uploaded files.
spring.servlet.multipart.max-file-size=1MB # Max file size.
spring.servlet.multipart.max-request-size=10MB # Max request size.
spring.servlet.multipart.resolve-lazily=false # Whether to resolve the multipart request lazily at the time of file or parameter access.
``````````

### Jackson ###

``````````
# JACKSON (JacksonProperties)
spring.jackson.date-format= # Date format string or a fully-qualified date format class name. For instance, `yyyy-MM-dd HH:mm:ss`.
spring.jackson.default-property-inclusion= # Controls the inclusion of properties during serialization. Configured with one of the values in Jackson's JsonInclude.Include enumeration.
spring.jackson.deserialization.*= # Jackson on/off features that affect the way Java objects are deserialized.
spring.jackson.generator.*= # Jackson on/off features for generators.
spring.jackson.joda-date-time-format= # Joda date time format string. If not configured, "date-format" is used as a fallback if it is configured with a format string.
spring.jackson.locale= # Locale used for formatting.
spring.jackson.mapper.*= # Jackson general purpose on/off features.
spring.jackson.parser.*= # Jackson on/off features for parsers.
spring.jackson.property-naming-strategy= # One of the constants on Jackson's PropertyNamingStrategy. Can also be a fully-qualified class name of a PropertyNamingStrategy subclass.
spring.jackson.serialization.*= # Jackson on/off features that affect the way Java objects are serialized.
spring.jackson.time-zone= #  Time zone used when formatting dates. For instance, "America/Los_Angeles" or "GMT+10".
spring.jackson.visibility.*= # Jackson visibility thresholds that can be used to limit which methods (and fields) are auto-detected.
``````````

### GSON ###

``````````
# GSON (GsonProperties)
spring.gson.date-format= # Format to use when serializing Date objects.
spring.gson.disable-html-escaping= # Whether to disable the escaping of HTML characters such as '<', '>', etc.
spring.gson.disable-inner-class-serialization= # Whether to exclude inner classes during serialization.
spring.gson.enable-complex-map-key-serialization= # Whether to enable serialization of complex map keys (i.e. non-primitives).
spring.gson.exclude-fields-without-expose-annotation= # Whether to exclude all fields from consideration for serialization or deserialization that do not have the "Expose" annotation.
spring.gson.field-naming-policy= # Naming policy that should be applied to an object's field during serialization and deserialization.
spring.gson.generate-non-executable-json= # Whether to generate non executable JSON by prefixing the output with some special text.
spring.gson.lenient= # Whether to be lenient about parsing JSON that doesn't conform to RFC 4627.
spring.gson.long-serialization-policy= # Serialization policy for Long and long types.
spring.gson.pretty-printing= # Whether to output serialized JSON that fits in a page for pretty printing.
spring.gson.serialize-nulls= # Whether to serialize null fields.
``````````

### Spring MVC ###

``````````
# SPRING MVC
spring.mvc.async.request-timeout= # Amount of time before asynchronous request handling times out.
spring.mvc.contentnegotiation.favor-parameter=false # Whether a request parameter ("format" by default) should be used to determine the requested media type.
spring.mvc.contentnegotiation.favor-path-extension=false # Whether the path extension in the URL path should be used to determine the requested media type.
spring.mvc.contentnegotiation.media-types.*= # Map file extensions to media types for content negotiation. For instance, yml to text/yaml.
spring.mvc.contentnegotiation.parameter-name= # Query parameter name to use when "favor-parameter" is enabled.
spring.mvc.date-format= # Date format to use. For instance, `dd/MM/yyyy`.
spring.mvc.dispatch-trace-request=false # Whether to dispatch TRACE requests to the FrameworkServlet doService method.
spring.mvc.dispatch-options-request=true # Whether to dispatch OPTIONS requests to the FrameworkServlet doService method.
spring.mvc.favicon.enabled=true # Whether to enable resolution of favicon.ico.
spring.mvc.formcontent.filter.enabled=true # Whether to enable Spring's FormContentFilter.
spring.mvc.hiddenmethod.filter.enabled=true # Whether to enable Spring's HiddenHttpMethodFilter.
spring.mvc.ignore-default-model-on-redirect=true # Whether the content of the "default" model should be ignored during redirect scenarios.
spring.mvc.locale= # Locale to use. By default, this locale is overridden by the "Accept-Language" header.
spring.mvc.locale-resolver=accept-header # Define how the locale should be resolved.
spring.mvc.log-resolved-exception=false # Whether to enable warn logging of exceptions resolved by a "HandlerExceptionResolver", except for "DefaultHandlerExceptionResolver".
spring.mvc.message-codes-resolver-format= # Formatting strategy for message codes. For instance, `PREFIX_ERROR_CODE`.
spring.mvc.pathmatch.use-registered-suffix-pattern=false # Whether suffix pattern matching should work only against extensions registered with "spring.mvc.contentnegotiation.media-types.*".
spring.mvc.pathmatch.use-suffix-pattern=false # Whether to use suffix pattern match (".*") when matching patterns to requests.
spring.mvc.servlet.load-on-startup=-1 # Load on startup priority of the dispatcher servlet.
spring.mvc.servlet.path=/ # Path of the dispatcher servlet.
spring.mvc.static-path-pattern=/** # Path pattern used for static resources.
spring.mvc.throw-exception-if-no-handler-found=false # Whether a "NoHandlerFoundException" should be thrown if no Handler was found to process a request.
spring.mvc.view.prefix= # Spring MVC view prefix.
spring.mvc.view.suffix= # Spring MVC view suffix.
``````````

### Spring Resources Handling ###

``````````
# SPRING RESOURCES HANDLING (ResourceProperties)
spring.resources.add-mappings=true # Whether to enable default resource handling.
spring.resources.cache.cachecontrol.cache-private= # Indicate that the response message is intended for a single user and must not be stored by a shared cache.
spring.resources.cache.cachecontrol.cache-public= # Indicate that any cache may store the response.
spring.resources.cache.cachecontrol.max-age= # Maximum time the response should be cached, in seconds if no duration suffix is not specified.
spring.resources.cache.cachecontrol.must-revalidate= # Indicate that once it has become stale, a cache must not use the response without re-validating it with the server.
spring.resources.cache.cachecontrol.no-cache= # Indicate that the cached response can be reused only if re-validated with the server.
spring.resources.cache.cachecontrol.no-store= # Indicate to not cache the response in any case.
spring.resources.cache.cachecontrol.no-transform= # Indicate intermediaries (caches and others) that they should not transform the response content.
spring.resources.cache.cachecontrol.proxy-revalidate= # Same meaning as the "must-revalidate" directive, except that it does not apply to private caches.
spring.resources.cache.cachecontrol.s-max-age= # Maximum time the response should be cached by shared caches, in seconds if no duration suffix is not specified.
spring.resources.cache.cachecontrol.stale-if-error= # Maximum time the response may be used when errors are encountered, in seconds if no duration suffix is not specified.
spring.resources.cache.cachecontrol.stale-while-revalidate= # Maximum time the response can be served after it becomes stale, in seconds if no duration suffix is not specified.
spring.resources.cache.period= # Cache period for the resources served by the resource handler. If a duration suffix is not specified, seconds will be used.
spring.resources.chain.cache=true # Whether to enable caching in the Resource chain.
spring.resources.chain.compressed=false # Whether to enable resolution of already compressed resources (gzip, brotli).
spring.resources.chain.enabled= # Whether to enable the Spring Resource Handling chain. By default, disabled unless at least one strategy has been enabled.
spring.resources.chain.html-application-cache=false # Whether to enable HTML5 application cache manifest rewriting.
spring.resources.chain.strategy.content.enabled=false # Whether to enable the content Version Strategy.
spring.resources.chain.strategy.content.paths=/** # Comma-separated list of patterns to apply to the content Version Strategy.
spring.resources.chain.strategy.fixed.enabled=false # Whether to enable the fixed Version Strategy.
spring.resources.chain.strategy.fixed.paths=/** # Comma-separated list of patterns to apply to the fixed Version Strategy.
spring.resources.chain.strategy.fixed.version= # Version string to use for the fixed Version Strategy.
spring.resources.static-locations=classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/ # Locations of static resources.
``````````

### Spring Session ###

``````````
# SPRING SESSION (SessionProperties)
spring.session.store-type= # Session store type.
spring.session.timeout= # Session timeout. If a duration suffix is not specified, seconds will be used.
spring.session.servlet.filter-order=-2147483598 # Session repository filter order.
spring.session.servlet.filter-dispatcher-types=async,error,request # Session repository filter dispatcher types.
``````````

### Spring Webflux ###

``````````
# SPRING WEBFLUX (WebFluxProperties)
spring.webflux.date-format= # Date format to use. For instance, `dd/MM/yyyy`.
spring.webflux.hiddenmethod.filter.enabled=true # Whether to enable Spring's HiddenHttpMethodFilter.
spring.webflux.static-path-pattern=/** # Path pattern used for static resources.
``````````

### Spring Web Services ###

``````````
# SPRING WEB SERVICES (WebServicesProperties)
spring.webservices.path=/services # Path that serves as the base URI for the services.
spring.webservices.servlet.init= # Servlet init parameters to pass to Spring Web Services.
spring.webservices.servlet.load-on-startup=-1 # Load on startup priority of the Spring Web Services servlet.
spring.webservices.wsdl-locations= # Comma-separated list of locations of WSDLs and accompanying XSDs to be exposed as beans.
``````````

## Security Properties ##

### Security ###

``````````
# SECURITY (SecurityProperties)
spring.security.filter.order=-100 # Security filter chain order.
spring.security.filter.dispatcher-types=async,error,request # Security filter chain dispatcher types.
spring.security.user.name=user # Default user name.
spring.security.user.password= # Password for the default user name.
spring.security.user.roles= # Granted roles for the default user name.
``````````

### Security Oauth2 Client ###

``````````
# SECURITY OAUTH2 CLIENT (OAuth2ClientProperties)
spring.security.oauth2.client.provider.*= # OAuth provider details.
spring.security.oauth2.client.registration.*= # OAuth client registrations.
``````````

### Security Oauth2 Resource Server ###

``````````
# SECURITY OAUTH2 RESOURCE SERVER (OAuth2ResourceServerProperties)
spring.security.oauth2.resourceserver.jwt.jwk-set-uri= # JSON Web Key URI to use to verify the JWT token.
spring.security.oauth2.resourceserver.jwt.issuer-uri= # URI that an OpenID Connect Provider asserts as its Issuer Identifier.
``````````

## Data Properties ##

### Data JDBC ###

``````````
# DATA JDBC
spring.data.jdbc.repositories.enabled=true # Whether to enable JDBC repositories.
``````````

### MongoDB ###

``````````
# MONGODB (MongoProperties)
spring.data.mongodb.authentication-database= # Authentication database name.
spring.data.mongodb.database= # Database name.
spring.data.mongodb.field-naming-strategy= # Fully qualified name of the FieldNamingStrategy to use.
spring.data.mongodb.grid-fs-database= # GridFS database name.
spring.data.mongodb.host= # Mongo server host. Cannot be set with URI.
spring.data.mongodb.password= # Login password of the mongo server. Cannot be set with URI.
spring.data.mongodb.port= # Mongo server port. Cannot be set with URI.
spring.data.mongodb.repositories.type=auto # Type of Mongo repositories to enable.
spring.data.mongodb.uri=mongodb://localhost/test # Mongo database URI. Cannot be set with host, port and credentials.
spring.data.mongodb.username= # Login user of the mongo server. Cannot be set with URI.
``````````

### Data Redis ###

``````````
# DATA REDIS
spring.data.redis.repositories.enabled=true # Whether to enable Redis repositories.
``````````

### Solr ###

``````````
# SOLR (SolrProperties)
spring.data.solr.host=http://127.0.0.1:8983/solr # Solr host. Ignored if "zk-host" is set.
spring.data.solr.repositories.enabled=true # Whether to enable Solr repositories.
spring.data.solr.zk-host= # ZooKeeper host address in the form HOST:PORT.
``````````

### Datasource ###

``````````
# DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
spring.datasource.continue-on-error=false # Whether to stop if an error occurs while initializing the database.
spring.datasource.data= # Data (DML) script resource references.
spring.datasource.data-username= # Username of the database to execute DML scripts (if different).
spring.datasource.data-password= # Password of the database to execute DML scripts (if different).
spring.datasource.dbcp2.*= # Commons DBCP2 specific settings
spring.datasource.driver-class-name= # Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
spring.datasource.generate-unique-name=false # Whether to generate a random datasource name.
spring.datasource.hikari.*= # Hikari specific settings
spring.datasource.initialization-mode=embedded # Initialize the datasource with available DDL and DML scripts.
spring.datasource.jmx-enabled=false # Whether to enable JMX support (if provided by the underlying pool).
spring.datasource.jndi-name= # JNDI location of the datasource. Class, url, username & password are ignored when set.
spring.datasource.name= # Name of the datasource. Default to "testdb" when using an embedded database.
spring.datasource.password= # Login password of the database.
spring.datasource.platform=all # Platform to use in the DDL or DML scripts (such as schema-${platform}.sql or data-${platform}.sql).
spring.datasource.schema= # Schema (DDL) script resource references.
spring.datasource.schema-username= # Username of the database to execute DDL scripts (if different).
spring.datasource.schema-password= # Password of the database to execute DDL scripts (if different).
spring.datasource.separator=; # Statement separator in SQL initialization scripts.
spring.datasource.sql-script-encoding= # SQL scripts encoding.
spring.datasource.tomcat.*= # Tomcat datasource specific settings
spring.datasource.type= # Fully qualified name of the connection pool implementation to use. By default, it is auto-detected from the classpath.
spring.datasource.url= # JDBC URL of the database.
spring.datasource.username= # Login username of the database.
spring.datasource.xa.data-source-class-name= # XA datasource fully qualified name.
spring.datasource.xa.properties= # Properties to pass to the XA data source.
``````````

### JDBC ###

``````````
# JDBC (JdbcProperties)
spring.jdbc.template.fetch-size=-1 # Number of rows that should be fetched from the database when more rows are needed.
spring.jdbc.template.max-rows=-1 # Maximum number of rows.
spring.jdbc.template.query-timeout= # Query timeout. Default is to use the JDBC driver's default configuration. If a duration suffix is not specified, seconds will be used.
``````````

### Embedded MongoDB ###

``````````
# EMBEDDED MONGODB (EmbeddedMongoProperties)
spring.mongodb.embedded.features=sync_delay # Comma-separated list of features to enable.
spring.mongodb.embedded.storage.database-dir= # Directory used for data storage.
spring.mongodb.embedded.storage.oplog-size= # Maximum size of the oplog.
spring.mongodb.embedded.storage.repl-set-name= # Name of the replica set.
spring.mongodb.embedded.version=3.5.5 # Version of Mongo to use.
``````````

### Redis ###

``````````
# REDIS (RedisProperties)
spring.redis.cluster.max-redirects= # Maximum number of redirects to follow when executing commands across the cluster.
spring.redis.cluster.nodes= # Comma-separated list of "host:port" pairs to bootstrap from.
spring.redis.database=0 # Database index used by the connection factory.
spring.redis.url= # Connection URL. Overrides host, port, and password. User is ignored. Example: redis://user:password@example.com:6379
spring.redis.host=localhost # Redis server host.
spring.redis.jedis.pool.max-active=8 # Maximum number of connections that can be allocated by the pool at a given time. Use a negative value for no limit.
spring.redis.jedis.pool.max-idle=8 # Maximum number of "idle" connections in the pool. Use a negative value to indicate an unlimited number of idle connections.
spring.redis.jedis.pool.max-wait=-1ms # Maximum amount of time a connection allocation should block before throwing an exception when the pool is exhausted. Use a negative value to block indefinitely.
spring.redis.jedis.pool.min-idle=0 # Target for the minimum number of idle connections to maintain in the pool. This setting only has an effect if it is positive.
spring.redis.lettuce.pool.max-active=8 # Maximum number of connections that can be allocated by the pool at a given time. Use a negative value for no limit.
spring.redis.lettuce.pool.max-idle=8 # Maximum number of "idle" connections in the pool. Use a negative value to indicate an unlimited number of idle connections.
spring.redis.lettuce.pool.max-wait=-1ms # Maximum amount of time a connection allocation should block before throwing an exception when the pool is exhausted. Use a negative value to block indefinitely.
spring.redis.lettuce.pool.min-idle=0 # Target for the minimum number of idle connections to maintain in the pool. This setting only has an effect if it is positive.
spring.redis.lettuce.shutdown-timeout=100ms # Shutdown timeout.
spring.redis.password= # Login password of the redis server.
spring.redis.port=6379 # Redis server port.
spring.redis.sentinel.master= # Name of the Redis server.
spring.redis.sentinel.nodes= # Comma-separated list of "host:port" pairs.
spring.redis.ssl=false # Whether to enable SSL support.
spring.redis.timeout= # Connection timeout.
``````````

### Transaction ###

``````````
# TRANSACTION (TransactionProperties)
spring.transaction.default-timeout= # Default transaction timeout. If a duration suffix is not specified, seconds will be used.
spring.transaction.rollback-on-commit-failure= # Whether to roll back on commit failures.
``````````

## Integration Properties ##

### ActiveMQ ###

``````````
# ACTIVEMQ (ActiveMQProperties)
spring.activemq.broker-url= # URL of the ActiveMQ broker. Auto-generated by default.
spring.activemq.close-timeout=15s # Time to wait before considering a close complete.
spring.activemq.in-memory=true # Whether the default broker URL should be in memory. Ignored if an explicit broker has been specified.
spring.activemq.non-blocking-redelivery=false # Whether to stop message delivery before re-delivering messages from a rolled back transaction. This implies that message order is not preserved when this is enabled.
spring.activemq.password= # Login password of the broker.
spring.activemq.send-timeout=0ms # Time to wait on message sends for a response. Set it to 0 to wait forever.
spring.activemq.user= # Login user of the broker.
spring.activemq.packages.trust-all= # Whether to trust all packages.
spring.activemq.packages.trusted= # Comma-separated list of specific packages to trust (when not trusting all packages).
spring.activemq.pool.block-if-full=true # Whether to block when a connection is requested and the pool is full. Set it to false to throw a "JMSException" instead.
spring.activemq.pool.block-if-full-timeout=-1ms # Blocking period before throwing an exception if the pool is still full.
spring.activemq.pool.enabled=false # Whether a JmsPoolConnectionFactory should be created, instead of a regular ConnectionFactory.
spring.activemq.pool.idle-timeout=30s # Connection idle timeout.
spring.activemq.pool.max-connections=1 # Maximum number of pooled connections.
spring.activemq.pool.max-sessions-per-connection=500 # Maximum number of pooled sessions per connection in the pool.
spring.activemq.pool.time-between-expiration-check=-1ms # Time to sleep between runs of the idle connection eviction thread. When negative, no idle connection eviction thread runs.
spring.activemq.pool.use-anonymous-producers=true # Whether to use only one anonymous "MessageProducer" instance. Set it to false to create one "MessageProducer" every time one is required.
``````````

### RabbitMQ ###

``````````
# RABBIT (RabbitProperties)
spring.rabbitmq.addresses= # Comma-separated list of addresses to which the client should connect.
spring.rabbitmq.cache.channel.checkout-timeout= # Duration to wait to obtain a channel if the cache size has been reached.
spring.rabbitmq.cache.channel.size= # Number of channels to retain in the cache.
spring.rabbitmq.cache.connection.mode=channel # Connection factory cache mode.
spring.rabbitmq.cache.connection.size= # Number of connections to cache.
spring.rabbitmq.connection-timeout= # Connection timeout. Set it to zero to wait forever.
spring.rabbitmq.dynamic=true # Whether to create an AmqpAdmin bean.
spring.rabbitmq.host=localhost # RabbitMQ host.
spring.rabbitmq.listener.direct.acknowledge-mode= # Acknowledge mode of container.
spring.rabbitmq.listener.direct.auto-startup=true # Whether to start the container automatically on startup.
spring.rabbitmq.listener.direct.consumers-per-queue= # Number of consumers per queue.
spring.rabbitmq.listener.direct.default-requeue-rejected= # Whether rejected deliveries are re-queued by default.
spring.rabbitmq.listener.direct.idle-event-interval= # How often idle container events should be published.
spring.rabbitmq.listener.direct.missing-queues-fatal=false # Whether to fail if the queues declared by the container are not available on the broker.
spring.rabbitmq.listener.direct.prefetch= # Maximum number of unacknowledged messages that can be outstanding at each consumer.
spring.rabbitmq.listener.direct.retry.enabled=false # Whether publishing retries are enabled.
spring.rabbitmq.listener.direct.retry.initial-interval=1000ms # Duration between the first and second attempt to deliver a message.
spring.rabbitmq.listener.direct.retry.max-attempts=3 # Maximum number of attempts to deliver a message.
spring.rabbitmq.listener.direct.retry.max-interval=10000ms # Maximum duration between attempts.
spring.rabbitmq.listener.direct.retry.multiplier=1 # Multiplier to apply to the previous retry interval.
spring.rabbitmq.listener.direct.retry.stateless=true # Whether retries are stateless or stateful.
spring.rabbitmq.listener.simple.acknowledge-mode= # Acknowledge mode of container.
spring.rabbitmq.listener.simple.auto-startup=true # Whether to start the container automatically on startup.
spring.rabbitmq.listener.simple.concurrency= # Minimum number of listener invoker threads.
spring.rabbitmq.listener.simple.default-requeue-rejected= # Whether rejected deliveries are re-queued by default.
spring.rabbitmq.listener.simple.idle-event-interval= # How often idle container events should be published.
spring.rabbitmq.listener.simple.max-concurrency= # Maximum number of listener invoker threads.
spring.rabbitmq.listener.simple.missing-queues-fatal=true # Whether to fail if the queues declared by the container are not available on the broker and/or whether to stop the container if one or more queues are deleted at runtime.
spring.rabbitmq.listener.simple.prefetch= # Maximum number of unacknowledged messages that can be outstanding at each consumer.
spring.rabbitmq.listener.simple.retry.enabled=false # Whether publishing retries are enabled.
spring.rabbitmq.listener.simple.retry.initial-interval=1000ms # Duration between the first and second attempt to deliver a message.
spring.rabbitmq.listener.simple.retry.max-attempts=3 # Maximum number of attempts to deliver a message.
spring.rabbitmq.listener.simple.retry.max-interval=10000ms # Maximum duration between attempts.
spring.rabbitmq.listener.simple.retry.multiplier=1 # Multiplier to apply to the previous retry interval.
spring.rabbitmq.listener.simple.retry.stateless=true # Whether retries are stateless or stateful.
spring.rabbitmq.listener.simple.transaction-size= # Number of messages to be processed between acks when the acknowledge mode is AUTO. If larger than prefetch, prefetch will be increased to this value.
spring.rabbitmq.listener.type=simple # Listener container type.
spring.rabbitmq.password=guest # Login to authenticate against the broker.
spring.rabbitmq.port=5672 # RabbitMQ port.
spring.rabbitmq.publisher-confirms=false # Whether to enable publisher confirms.
spring.rabbitmq.publisher-returns=false # Whether to enable publisher returns.
spring.rabbitmq.requested-heartbeat= # Requested heartbeat timeout; zero for none. If a duration suffix is not specified, seconds will be used.
spring.rabbitmq.ssl.algorithm= # SSL algorithm to use. By default, configured by the Rabbit client library.
spring.rabbitmq.ssl.enabled=false # Whether to enable SSL support.
spring.rabbitmq.ssl.key-store= # Path to the key store that holds the SSL certificate.
spring.rabbitmq.ssl.key-store-password= # Password used to access the key store.
spring.rabbitmq.ssl.key-store-type=PKCS12 # Key store type.
spring.rabbitmq.ssl.trust-store= # Trust store that holds SSL certificates.
spring.rabbitmq.ssl.trust-store-password= # Password used to access the trust store.
spring.rabbitmq.ssl.trust-store-type=JKS # Trust store type.
spring.rabbitmq.ssl.validate-server-certificate=true # Whether to enable server side certificate validation.
spring.rabbitmq.ssl.verify-hostname=true # Whether to enable hostname verification.
spring.rabbitmq.template.default-receive-queue= # Name of the default queue to receive messages from when none is specified explicitly.
spring.rabbitmq.template.exchange= # Name of the default exchange to use for send operations.
spring.rabbitmq.template.mandatory= # Whether to enable mandatory messages.
spring.rabbitmq.template.receive-timeout= # Timeout for `receive()` operations.
spring.rabbitmq.template.reply-timeout= # Timeout for `sendAndReceive()` operations.
spring.rabbitmq.template.retry.enabled=false # Whether publishing retries are enabled.
spring.rabbitmq.template.retry.initial-interval=1000ms # Duration between the first and second attempt to deliver a message.
spring.rabbitmq.template.retry.max-attempts=3 # Maximum number of attempts to deliver a message.
spring.rabbitmq.template.retry.max-interval=10000ms # Maximum duration between attempts.
spring.rabbitmq.template.retry.multiplier=1 # Multiplier to apply to the previous retry interval.
spring.rabbitmq.template.routing-key= # Value of a default routing key to use for send operations.
spring.rabbitmq.username=guest # Login user to authenticate to the broker.
spring.rabbitmq.virtual-host= # Virtual host to use when connecting to the broker.
``````````

### Apache Kafka ###

``````````
# APACHE KAFKA (KafkaProperties)
spring.kafka.admin.client-id= # ID to pass to the server when making requests. Used for server-side logging.
spring.kafka.admin.fail-fast=false # Whether to fail fast if the broker is not available on startup.
spring.kafka.admin.properties.*= # Additional admin-specific properties used to configure the client.
spring.kafka.admin.ssl.key-password= # Password of the private key in the key store file.
spring.kafka.admin.ssl.key-store-location= # Location of the key store file.
spring.kafka.admin.ssl.key-store-password= # Store password for the key store file.
spring.kafka.admin.ssl.key-store-type= # Type of the key store.
spring.kafka.admin.ssl.protocol= # SSL protocol to use.
spring.kafka.admin.ssl.trust-store-location= # Location of the trust store file.
spring.kafka.admin.ssl.trust-store-password= # Store password for the trust store file.
spring.kafka.admin.ssl.trust-store-type= # Type of the trust store.
spring.kafka.bootstrap-servers= # Comma-delimited list of host:port pairs to use for establishing the initial connections to the Kafka cluster. Applies to all components unless overridden.
spring.kafka.client-id= # ID to pass to the server when making requests. Used for server-side logging.
spring.kafka.consumer.auto-commit-interval= # Frequency with which the consumer offsets are auto-committed to Kafka if 'enable.auto.commit' is set to true.
spring.kafka.consumer.auto-offset-reset= # What to do when there is no initial offset in Kafka or if the current offset no longer exists on the server.
spring.kafka.consumer.bootstrap-servers= # Comma-delimited list of host:port pairs to use for establishing the initial connections to the Kafka cluster. Overrides the global property, for consumers.
spring.kafka.consumer.client-id= # ID to pass to the server when making requests. Used for server-side logging.
spring.kafka.consumer.enable-auto-commit= # Whether the consumer's offset is periodically committed in the background.
spring.kafka.consumer.fetch-max-wait= # Maximum amount of time the server blocks before answering the fetch request if there isn't sufficient data to immediately satisfy the requirement given by "fetch-min-size".
spring.kafka.consumer.fetch-min-size= # Minimum amount of data the server should return for a fetch request.
spring.kafka.consumer.group-id= # Unique string that identifies the consumer group to which this consumer belongs.
spring.kafka.consumer.heartbeat-interval= # Expected time between heartbeats to the consumer coordinator.
spring.kafka.consumer.key-deserializer= # Deserializer class for keys.
spring.kafka.consumer.max-poll-records= # Maximum number of records returned in a single call to poll().
spring.kafka.consumer.properties.*= # Additional consumer-specific properties used to configure the client.
spring.kafka.consumer.ssl.key-password= # Password of the private key in the key store file.
spring.kafka.consumer.ssl.key-store-location= # Location of the key store file.
spring.kafka.consumer.ssl.key-store-password= # Store password for the key store file.
spring.kafka.consumer.ssl.key-store-type= # Type of the key store.
spring.kafka.consumer.ssl.protocol= # SSL protocol to use.
spring.kafka.consumer.ssl.trust-store-location= # Location of the trust store file.
spring.kafka.consumer.ssl.trust-store-password= # Store password for the trust store file.
spring.kafka.consumer.ssl.trust-store-type= # Type of the trust store.
spring.kafka.consumer.value-deserializer= # Deserializer class for values.
spring.kafka.jaas.control-flag=required # Control flag for login configuration.
spring.kafka.jaas.enabled=false # Whether to enable JAAS configuration.
spring.kafka.jaas.login-module=com.sun.security.auth.module.Krb5LoginModule # Login module.
spring.kafka.jaas.options= # Additional JAAS options.
spring.kafka.listener.ack-count= # Number of records between offset commits when ackMode is "COUNT" or "COUNT_TIME".
spring.kafka.listener.ack-mode= # Listener AckMode. See the spring-kafka documentation.
spring.kafka.listener.ack-time= # Time between offset commits when ackMode is "TIME" or "COUNT_TIME".
spring.kafka.listener.client-id= # Prefix for the listener's consumer client.id property.
spring.kafka.listener.concurrency= # Number of threads to run in the listener containers.
spring.kafka.listener.idle-event-interval= # Time between publishing idle consumer events (no data received).
spring.kafka.listener.log-container-config= # Whether to log the container configuration during initialization (INFO level).
spring.kafka.listener.monitor-interval= # Time between checks for non-responsive consumers. If a duration suffix is not specified, seconds will be used.
spring.kafka.listener.no-poll-threshold= # Multiplier applied to "pollTimeout" to determine if a consumer is non-responsive.
spring.kafka.listener.poll-timeout= # Timeout to use when polling the consumer.
spring.kafka.listener.type=single # Listener type.
spring.kafka.producer.acks= # Number of acknowledgments the producer requires the leader to have received before considering a request complete.
spring.kafka.producer.batch-size= # Default batch size.
spring.kafka.producer.bootstrap-servers= # Comma-delimited list of host:port pairs to use for establishing the initial connections to the Kafka cluster. Overrides the global property, for producers.
spring.kafka.producer.buffer-memory= # Total memory size the producer can use to buffer records waiting to be sent to the server.
spring.kafka.producer.client-id= # ID to pass to the server when making requests. Used for server-side logging.
spring.kafka.producer.compression-type= # Compression type for all data generated by the producer.
spring.kafka.producer.key-serializer= # Serializer class for keys.
spring.kafka.producer.properties.*= # Additional producer-specific properties used to configure the client.
spring.kafka.producer.retries= # When greater than zero, enables retrying of failed sends.
spring.kafka.producer.ssl.key-password= # Password of the private key in the key store file.
spring.kafka.producer.ssl.key-store-location= # Location of the key store file.
spring.kafka.producer.ssl.key-store-password= # Store password for the key store file.
spring.kafka.producer.ssl.key-store-type= # Type of the key store.
spring.kafka.producer.ssl.protocol= # SSL protocol to use.
spring.kafka.producer.ssl.trust-store-location= # Location of the trust store file.
spring.kafka.producer.ssl.trust-store-password= # Store password for the trust store file.
spring.kafka.producer.ssl.trust-store-type= # Type of the trust store.
spring.kafka.producer.transaction-id-prefix= # When non empty, enables transaction support for producer.
spring.kafka.producer.value-serializer= # Serializer class for values.
spring.kafka.properties.*= # Additional properties, common to producers and consumers, used to configure the client.
spring.kafka.ssl.key-password= # Password of the private key in the key store file.
spring.kafka.ssl.key-store-location= # Location of the key store file.
spring.kafka.ssl.key-store-password= # Store password for the key store file.
spring.kafka.ssl.key-store-type= # Type of the key store.
spring.kafka.ssl.protocol= # SSL protocol to use.
spring.kafka.ssl.trust-store-location= # Location of the trust store file.
spring.kafka.ssl.trust-store-password= # Store password for the trust store file.
spring.kafka.ssl.trust-store-type= # Type of the trust store.
spring.kafka.streams.application-id= # Kafka streams application.id property; default spring.application.name.
spring.kafka.streams.auto-startup=true # Whether or not to auto-start the streams factory bean.
spring.kafka.streams.bootstrap-servers= # Comma-delimited list of host:port pairs to use for establishing the initial connections to the Kafka cluster. Overrides the global property, for streams.
spring.kafka.streams.cache-max-size-buffering= # Maximum memory size to be used for buffering across all threads.
spring.kafka.streams.client-id= # ID to pass to the server when making requests. Used for server-side logging.
spring.kafka.streams.properties.*= # Additional Kafka properties used to configure the streams.
spring.kafka.streams.replication-factor= # The replication factor for change log topics and repartition topics created by the stream processing application.
spring.kafka.streams.ssl.key-password= # Password of the private key in the key store file.
spring.kafka.streams.ssl.key-store-location= # Location of the key store file.
spring.kafka.streams.ssl.key-store-password= # Store password for the key store file.
spring.kafka.streams.ssl.key-store-type= # Type of the key store.
spring.kafka.streams.ssl.protocol= # SSL protocol to use.
spring.kafka.streams.ssl.trust-store-location= # Location of the trust store file.
spring.kafka.streams.ssl.trust-store-password= # Store password for the trust store file.
spring.kafka.streams.ssl.trust-store-type= # Type of the trust store.
spring.kafka.streams.state-dir= # Directory location for the state store.
spring.kafka.template.default-topic= # Default topic to which messages are sent.
``````````


[WebMvcConfigurationSupport]: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/WebMvcConfigurationSupport.html
[https_docs.spring.io_spring-boot_docs_current_reference_htmlsingle]: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/
[https_start.spring.io]: https://start.spring.io/
[Spring_Boot]: https://static.sitestack.cn/projects/sdky-java-note/41bd84aea4dbd970af7731c42966895c.png
[Jar]: https://static.sitestack.cn/projects/sdky-java-note/46c23e7b73f04c261c312406bde39a97.png
[Jar 1]: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#deployment-install
[Link 1]: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#deployment-script-customization
[Logback configuration]: https://logback.qos.ch/manual/configuration.html
[Spring Loaded]: https://github.com/spring-projects/spring-loaded
[spring-boot-devtools]: https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples/spring-boot-sample-devtools
[MVC]: https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-config
[MVC configuration]: https://docs.spring.io/spring/docs/5.1.9.RELEASE/spring-framework-reference/web.html#mvc
[Spring Boot Reference Guide]: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-spring-mvc-auto-configuration
[Link 2]: https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby
[Link 3]: http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/
[MyBatis_ _ PageHelper_ _ Mapper]: https://github.com/abel533/MyBatis-Spring-Boot
[Spring Data R2DBC]: https://spring.io/projects/spring-data-r2dbc
[org.apache.dubbo_dubbo-spring-boot-starter]: https://github.com/apache/incubator-dubbo-spring-boot-project/blob/master/README_CN.md
[Micrometer]: https://micrometer.io/
[Common application properties]: https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html