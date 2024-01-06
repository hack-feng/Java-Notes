# SSM 整合 #

## 准备所需的 jar 包 ##

 *  数据库驱动 jar 包及数据源 druid 所需 jar 包
 *  MyBatis 框架 jar 包
 *  MyBatis 整合 Spring 中间件 jar 包
 *  Spring 框架 jar 包及 common-logging.jar、aopalliance.jar、aspectjweaver.jar
 *  JSTL 标签库 jar 包

## 资源文件 resources ##

 *  log4j.properties
 *  db.properties
 *  mybatis-config.xml
 *  applicationContext.xml
 *  mvc.xml

### applicationContext.xml ###

 *  引入 db 配置文件、启用 Bean 后处理器、启用自动扫描包组件
 *  配置 druid 数据源
 *  配置 JDBC 事务管理器
 *  配置 AOP、配置事务增强处理
 *  配置 sqlSessionFactory（使用 SqlSessionFactoryBean 实例工厂）
 *  配置 mapper 接口扫描器 MapperScannerConfigurer
 *  引入 mvc.xml

``````````xml
<beans> 
      <!-- 启用 Bean 后处理器 --> 
      <context:annotation-config/> 
  
      <!-- 启用自动扫描包组件 --> 
      <context:component-scan base-package="com.example.ssm"/> 
  
      <!-- 引入 db 配置文件 --> 
      <context:property-placeholder location="classpath:db.properties" system-properties-mode="NEVER"/> 
  
      <!-- 配置 druid 数据源 --> 
      <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" 
   init-method="init" destroy-method="close"> 
          <property name="driverClassName" value="${driverClassName}"/> 
          <property name="url" value="${url}"/> 
          <property name="username" value="${username}"/> 
          <property name="password" value="${password}"/> 
      </bean> 
  
      <!-- 配置 sqlSessionFactory --> 
      <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean"> 
          <property name="configLocation" value="classpath:mybatis-config.xml"/> 
          <property name="dataSource" ref="dataSource"/> 
          <property name="typeAliasesPackage" value="com.example.ssm.domain"/> 
      </bean> 
  
      <!-- 配置 mapper 接口扫描器 --> 
      <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer"> 
          <property name="basePackage" value="com.example.ssm.mapper"/> 
          <!-- optional unless there are multiple session factories defined --> 
          <!-- <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/> --> 
      </bean> 
  
      <!-- 配置 JDBC 事务管理器 --> 
      <bean id="txManager" 
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager"> 
          <property name="dataSource" ref="dataSource"/> 
      </bean> 
  
      <!-- 配置 AOP --> 
      <aop:config> 
          <aop:pointcut id="txPoint" expression="execution(* com.example.ssm.service.*Servcie.*(..))"/> 
          <aop:advisor advice-ref="txAdvice" pointcut-ref="txPoint"/> 
      </aop:config> 
  
      <!-- 配置事务增强处理 --> 
      <tx:advice id="txAdvice" transaction-manager="txManager"> 
          <tx:attributes> 
              <tx:method name="get*" read-only="true"/> 
              <tx:method name="list*" read-only="true"/> 
              <tx:method name="select*" read-only="true"/> 
              <tx:method name="query*" read-only="true"/> 
              <tx:method name="*" propagation="REQUIRED"/> 
          </tx:attributes> 
      </tx:advice> 
  
      <import resource="classpath:mvc.xml"/> 
  </beans>
``````````

### mvc.xml ###

 *  启用 Spring MVC 默认配置方案
 *  配置视图解析器 InternalResourceViewResolver

``````````xml
<beans> 
      <!-- 启用 Spring MVC 默认配置方案 --> 
      <mvc:annotation-driven/>    
  
      <!-- 配置视图解析器 --> 
      <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"> 
          <property name="prefix" value="/WEB-INF/views/"/> 
          <property name="suffix" value=".jsp"/> 
      </bean> 
  </beans>
``````````

## web.xml ##

 *  定义 Spring MVC 的前端控制器 DispatcherServlet
 *  定义字符编码过滤器 CharacterEncodingFilter

``````````xml
<web-app> 
      <servlet> 
          <servlet-name>app</servlet-name> 
          <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class> 
          <init-param> 
              <param-name>contextConfigLocation</param-name> 
              <param-value>classpath:applicationContext.xml</param-value> 
          </init-param> 
          <load-on-startup>1</load-on-startup> 
      </servlet> 
      <servlet-mapping> 
          <servlet-name>app</servlet-name> 
          <url-pattern>*.do</url-pattern> 
      </servlet-mapping> 
  
      <filter> 
          <filter-name>CharacterEncodingFilter</filter-name> 
          <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class> 
          <init-param> 
              <param-name>encoding</param-name> 
              <param-value>UTF-8</param-value> 
          </init-param> 
          <init-param> 
              <param-name>forceEncoding</param-name> 
              <param-value>true</param-value> 
          </init-param> 
      </filter> 
      <filter-mapping> 
          <filter-name>CharacterEncodingFilter</filter-name> 
          <url-pattern>/*</url-pattern> 
      </filter-mapping> 
  </web-app>
``````````

# RBAC #

 *  RBAC（Role-Based Access Control），基于角色的访问控制
 *  把许可权（Permission）与角色（Role）联系在一起，用户（User）通过充当合适角色的成员而获得该角色的许可权
 *  通过设计 @RequiredPermission 注解，贴在 Controller 中需要权限才能访问的请求方法上

# 应用分层 #

## 应用分层 ##

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414414639.jpeg) 
图 1 用分

 *  开放接口层：可直接封装 Service 接口暴露成 RPC 接口；通过 Web 封装成 http 接口；网关控制层等
 *  终端显示层：各个端的模板渲染并执行显示层。当前主要是 velocity 渲染，JS 渲染，JSP 渲染，移动端展示层等
 *  Web 层：主要是对访问控制进行转发，各类基本参数校验，或者不复用的业务简单处理等
 *  Service 层：相对具体的业务逻辑服务层
 *  Manager 层：通用业务处理层，它有如下特征：
    
     *  对第三方平台封装的层，预处理返回结果及转化异常信息
     *  对 Service 层通用能力的下沉，如缓存方案、中间件通用处理
     *  与 DAO 层交互，对 DAO 的业务通用能力的封装
 *  DAO 层：数据访问层，与底层 MySQL、Oracle、Hbase 进行数据交互
 *  外部接口或第三方平台：包括其它部门 RPC 开放接口，基础平台，其它公司的 HTTP 接口

## 分层异常处理 ##

 *  DAO 层：`catch(Exception e)` `throw new DAOException(e)`，不需要打印日志
 *  Service 层：记录日志信息到磁盘
 *  Web 层：直接跳转到友好错误页面
 *  开放接口层：将异常处理成错误码和错误信息方式返回

## 分层领域模型 ##

 *  DO（Data Object）：与数据库表结构一一对应，通过 DAO 层向上传输数据源对象
 *  DTO（Data Transfer Object）：数据传输对象，Service 和 Manager 向外传输的对象
 *  BO（Business Object）：业务对象，可以由 Service 层输出的封装业务逻辑的对象
 *  QUERY：数据查询对象，各层接收上层的查询请求
 *  VO（View Object）：显示层对象，通常是 Web 向模板渲染引擎层传输的对象


[f959d9681b588d0b0cf221ffd2023e37.jpeg]: https://static.sitestack.cn/projects/sdky-java-note/f959d9681b588d0b0cf221ffd2023e37.jpeg