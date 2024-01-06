 *  对于 Spring 框架而言，一切 Java 对象都是 Bean
 *  Spring 的 IoC 容器降低了业务对象替换的复杂性，提高了组件之间的解耦
 *  Spring 的 AOP 支持允许将一些通用任务如安全、事务、日志等进行集中式处理，从而提供了更好的复用
 *  Spring 的 ORM 和 DAO 提供了与第三方持久层框架的良好整合，并简化了底层的数据库访问
 *  注意：Spring 的核心容器必须依赖于 common-logging.jar
 *  https://docs.spring.io/spring/docs/current/spring-framework-reference/index.html
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414391733.png) 
    图 1 spring-overview

# Spring 整合 JUnit 测试 #

``````````java
// 让 JUnit 直接启动 Spring 容器，测试类运行在容器中 
  @RunWith(SpringJUnit4ClassRunner.class) 
  // 启动 Spring 容器时去哪加载配置文件，默认加载当前包下的 "当前类名-context.xml" 
  @ContextConfiguration("classpath:applicationContext.xml")
``````````

# Spring 容器 #

## Resource 接口，资源访问 ##

 *  常见的前缀及对应的访问策略
    
     *  `classpath:` ——以 ClassPathResource 实例访问类加载路径下的资源
     *  `file:` ——以 UrlResource 实例访问本地文件系统的资源，以斜杠开头表示绝对路径，否则表示相对路径
     *  `http:` ——以 UrlResource 实例访问基于 HTTP 协议的网络资源
     *  ``（无前缀）——由 ApplicationContext 的实现类来决定访问策略

## Spring 容器的创建方式 ##

### BeanFactory 接口 ###

 *  负责配置、创建、管理 Bean
 *  在程序请求 Bean 时才开始创建 Bean 及其所依赖的 Bean
 *  常用方法
    
     *  `boolean containsBean(String name)`：判断 Spring 容器是否包含 id 为 name 的 Bean 实例
     *  `T getBean(Class<T> requiredType)`：获取 Spring 容器中属于 requiredType 类型的、唯一的 Bean 实例
     *  `Object getBean(String name)`：返回容器 id 为 name 的 Bean 实例
     *  `T getBean(String name, Class requiredType)`：返回容器中 id 为 name，并且类型为 requiredType 的 Bean
     *  `Class<?> getType(String name)`：返回容器中 id 为 name 的 Bean 实例的类型

``````````java
Resource resource = new ClassPathResource("applicationContext.xml"); 
  BeanFactory factory = new XmlBeanFactory(resource);
``````````

### ApplicationContext 接口 ###

 *  Spring 上下文，BeanFactory 的子接口
 *  其常用实现类：FileSystemXmlApplicationContext、ClassPathXmlApplicationContext 和 AnnotationConfigApplicationContext
 *  在创建 Spring 后默认预初始化所有的 singleton Bean 及其所依赖的 Bean
 *  系统前期创建 ApplicationContext 时将有较大的系统开销，但一旦 ApplicationContext 初始化完成，程序后面获取 singleton Bean 实例时将拥有较好的性能

``````````java
// 使用 XML 配置文件提供 Bean 定义信息启动容器 
  ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml"); 
  // 使用 Java 配置类提供 Bean 定义信息启动容器 
  ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
``````````

 *  AbstractApplicationContext 抽象类中的方法：`Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType)`：获取所有带有指定注解的 Beans 集合`T getBean(String name, Class<T> requiredType)`：根据容器中 Bean 的 id 来获取指定 Bean

### WebApplicationContext 接口 ###

 *  Spring Web 上下文，ApplicationContext 的子接口
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414392403.jpeg) 
    图 2 mvc-context-hierarchy

## 获取 Spring 容器 ##

 *  自定义 Bean 实现 BeanFactoryAware 接口，重写该接口中的 `setBeanFactory(BeanFactory beanFactory)` 方法，当 Spring 调用该方法时会将 Spring 容器作为参数传入该方法
 *  自定义 Bean 实现 ApplicationContextAware 接口，重写该接口中的 `setApplicationContext(ApplicationContext applicationContext)` 方法，当 Spring 容器调用该方法时会把自身作为参数传入该方法

## Spring Environment ##

 *  Environment 是一种在容器内以配置（Profile）和属性（Properties）为模型的应用环境抽象整合
 *  Spring Framework 提供了两种 Environment 的实现（ConfigurableEnvironment 子接口的实现类）
    
     *  StandardEnvironment（一般应用），包含的属性源名称：systemProperties（JVM 系统属性）、systemEnvironment（操作系统环境变量）
     *  StandardServletEnvironment（Web 应用），包含的属性源名称： servletContextInitParams、servletConfigInitParams、jndiProperties

# applicationContext.xml #

 *  `<import resource="classpath:其它 xml 配置文件"/>`，引入其它分支配置文件

# IoC 和 DI #

 *  IoC：Inverse of Control（控制反转）：将原本在程序中手动创建对象的控制权，交由 Spring 框架来管理，即由 Spring 容器负责创建 Bean（即 Java 对象）
 *  DI：Dependency Injection（依赖注入）：Spring 容器管理容器中 Bean 之间依赖关系的方法

## 使用 XML 文件配置 IoC ##

 *  ，定义一个 Bean，需指定的属性
    
     *  调用构造器：id、class（该 Bean 的实现类）、parent（所继承 Bean 的 id）
     *  调用静态工厂方法：id、class（静态工厂类）、factory-method
     *  调用实例工厂方法：id、factory-bean（实例工厂 Bean 的 id）、factory-method
     *  调用实例工厂方法：id、class（实现了 FactoryBean 的实例工厂类型）
 *  其它属性：
    
     *  scope：定义 Bean 的作用域，属性值：singleton（缺省）、prototype（每次调用新建一个 Bean 的实例）、session、request
     *  init-method：初始化后执行的方法
     *  destroy-method：销毁前执行的方法（只适用于 singleton Bean）
     *  abstract="true"：定义成抽象 Bean
     *  lazy-init="true”：阻止 Spring 容器预初始化容器中的 singleton Bean
        

## 使用 XML 文件配置 DI ##

### 设值注入，需提供对应的 setter 方法 ###

`<property>` 子标签

 *  属性：name、value（普通的属性值或属性占位符）、ref（所引用 Bean 的 id）
 *  子标签：
    
     *  `<bean>`，嵌套 Bean，容器不能获取，无须指定 id 属性
     *  `<list>`，每个子标签 `<value>`、`<ref>` 或 `<bean>` 配置一个 List 元素
     *  `<set>`，每个子标签 `<value>`、`<ref>` 或 `<bean>`配置一个 Set 元素
     *  `<array>`，每个子标签 `<value>`、`<ref>` 或 `<bean>` 配置一个数组元素
     *  `<map>`，子标签 `<entry>`，属性：key、key-ref、value、value-ref
     *  `<props>`，子标签 `<prop>`，属性 key 指定 key 的值，内容指定 value 的值
     *  `<value>`，"key"="value"
 *  value 的属性值可以使用**属性占位符**
    
     *  PropertyPlaceholderConfigurer 是一个容器后处理器，负责加载 Properties 属性文件里的属性值，并将这些属性值设置成 Spring 配置文件的数据

``````````xml
<!-- 引入 属性占位符 --> 
  <context:property-placeholder location="classpath:db.properties" system-properties-mode="NEVER"/> 
  <!-- 定义数据源 Bean，使用 Druid 数据源实现 --> 
  <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" 
      init-method="init" destroy-method="close"> 
      <property name="driverClassName" value="${driverClassName}"/> 
      <property name="url" value="${url}"/> 
      <property name="username" value="${username}"/> 
      <property name="password" value="${password}"/> 
  </bean>
``````````

### 构造注入，需提供对应的、带参数的构造器 ###

 *  子标签，代表一个构造器参数，属性：name、value、ref
    

## 使用注解配置 IoC 和 DI ##

 *  Spring 通过使用 Bean 后处理器（BeanPostProcessor）增加对注解的支持
 *  启动包扫描功能，以注册在指定包下带有 IoC 和 DI 相关注解的 Bean：`<context:component-scan base-package="包 1，包 2, …"/>`
 *  启用 4 个 Bean 后处理器（当启动了包扫描功能后，在 Spring 测试环境中可以省略）：`<context:annotation-config />`

### IoC 相关注解 ###

 *  标注 Bean 类，指定该 Java 类作为 Spring Bean，Bean 实例的名称默认是 Bean 类的首字母小写，其它部分不变@Component("bean 的 id") // 缺省：首字母小写的 Bean 类名@Repository：在数据访问层（dao 层）使用@Service：在业务逻辑层（service 层）使用@Controller：在展现层（MVC→Spring MVC）使用@Primary：@Autowired 自动装配找到多个匹配的 Bean时，首选该 Bean
 *  指定 Bean 的作用域，如 @Scope("prototype")
 *  指定 Bean 的加载顺序，如 @Order(1)
 *  标注方法@PostConstruct，指定 Bean 的初始化方法@PreDestroy，指定 Bean 销毁之前的方法

### DI 相关注解（配置依赖） ###

 *  @Autowired 自动装配
    
     *  可修饰实例变量、setter 方法、普通方法（方法的参数可以有多个）和构造器等
     *  自动搜索容器中**类型匹配**的 Bean 实例
     *  匹配顺序（**byType** 自动装配策略）：依赖对象的类型或该类型的实现类、依赖对象的名称
     *  若找不到或找到多个 Bean 则抛出异常，可设置 @Autowired(required=false)
 *  @Qualifier("指定 Bean 的 id") **精确装配**
    
     *  可修饰实例变量、方法的形参
     *  若找不到则不注入
 *  @Resource(name="指定 Bean 的 id")
    
     *  JavaEE 的注解
     *  可修饰实例变量或 setter 方法
     *  默认匹配顺序：对象的名称、对象的类型（如果 name 属性一旦指定，就只会按照名称进行装配）
 *  @Value：属性占位符需要放到“$\{ … \}”之中，SpEL 表达式要放到“\#\{ … \}”之中

# SpEL #

 *  [Spring Expression Language (SpEL)][Spring Expression Language _SpEL]

# Spring 的事件 #

 *  ApplicationContext 的事件机制：每当 ApplicationContext 发布 ApplicationEvent 时，容器中的 ApplicationListener Bean 将自动被触发
 *  在 Spring 中的事件，默认是**同步**处理的（executor == null）
 *  自定义事件，继承 ApplicationEvent，并在其构造函数中指定事件源 source 以及事件关联的对象ApplicationEvent 两个子类：ApplicationContextEvent（容器事件，拥有 4 个子类，分别表示容器启动 ContextStartedEvent、刷新 ContextRefreshedEvent、停止 ContextStoppedEvent 及关闭 ContextClosedEvent 的事件）、RequestHandleEvent（与 Web 应用相关的事件）
 *  定义事件监听器

方式 1：实现 ApplicationListener 接口，并指定监听的事件类型，重写该接口中的 `void onApplicationEvent(E event)` 方法（判断该事件对象是否是想要监听的事件，并对消息进行接收处理）

方式 2：在处理事件的 Bean 的方法上添加 @EventListener，并在该方法参数上指定要监听的事件

 *  使用容器发布事件

方式 1：注入 ApplicationContext，使用 ApplicationContext.publishEvent() 方法来发布事件

方式 2：实现 ApplicationEventPublisherAware 接口，使用 ApplicationEventPublisher.publishEvent() 方法来发布事件

# AOP 思想 #

 *  AOP（Aspect Orient Programming），面向切面编程，将程序运行过程分解成各个切面
 *  AOP 的作用：为系统中业务组件的多个业务方法添加某种通用功能（在执行目标方法之前、之后插入一些通用处理）
 *  AOP 的过程：把业务方法中与业务无关的、却为业务模块所共同调用的操作抽离到不同的对象的方法中，最后使用代理的方式组合起来
 *  使用场景：日志、用户鉴权、全局性异常处理、性能监控、事务处理等
 *  术语：
    
     *  切面（Aspect）：用于组织多个 Advice，Advice 放在切面中定义，在实际应用中通常是一个存放通用功能实现的普通 Java 类，如日志切面、权限切面、事务切面等
     *  增强处理（Advice）：AOP 框架在特定的切入点执行的增强处理，处理有 around、before 和 after 等类型，在实际应用中通常是切面类中的一个方法
     *  连接点（Joinpoint）：程序在运行过程中能够插入切面的点，如方法的调用、异常的抛出或成员变量的访问和更新等（Spring AOP 只支持将**方法调用**作为连接点）
     *  切入点（Pointcut）：可以插入增强处理的连接点，当某个连接点满足指定要求（由切入点的正则表达式来定义）时，该连接点将被添加增强处理，该连接点也就变成了切入点
     *  织入（Weaving）：将增强添加到目标对象（Target）中，并创建一个被增强的对象——AOP 代理（Proxy）的过程（Spring AOP 在运行时完成织入）

## AspectJ 切入点语法 ##

 *  [Supported Pointcut Designators][]
 *  Spring AOP 常用的切入点指示符（pointcut designators，PCD）
    
     *  execution：用于匹配执行方法的连接点
        
         *  `execution(<修饰符>? <返回值类型> <所属类>?<方法名>(形参类型列表) <声明抛出的异常>?)`
         *  通配符：`?` 代表该部分可省略，`*` 代表一个任意类型的参数，`..` 代表零个或多个任意类型的参数
         *  如 `execution( org.crazyit.app.service.impl..*(..))`，匹配 org.crazyit.app.service.impl 包中任意类的任意方法的执行
     *  args：`args(参数类型列表)`，用于匹配执行方法传入的参数为指定类型的连接点
     *  @annotation：`@annotation(注解类型)`，用于匹配当前执行方法持有指定注解的方法
 *  Spring 支持使用如下三个逻辑运算符来组合切入点表达式
    
     *  `&&`：要求连接点同时匹配两个切入点表达式
     *  `||`：只要连接点匹配任意一个切入点表达式
     *  `!`：要求连接点不匹配指定的切入点表达式

## AOP 的实现 ##

 *  静态 AOP 实现：在编译阶段对程序进行修改（编译时增强），以 AspectJ 为代表，需要使用特殊的编译器 AspectJ 定义了如何表达、定义 AOP 编程中的语法规范，以及提供了编译、运行 AspectJ 的一些工具命令
 *  动态 AOP 实现：在内存中以 JDK 动态代理或 cglib 动态地生成 AOP 代理类（运行时增强），以 Spring AOP 为代表

## Spring 的 AOP ##

 *  依赖的 jar 包：aopalliance.jar、aspectjweaver.jar
 *  Spring 使用 AspectJ 方式来定义切入点和增强处理
 *  如果目标类有实现的接口，Spring 会使用 JDK 动态代理生成代理类，该代理类与目标类实现相同的接口
 *  如果目标类没有实现接口，Spring 会使用 cglib 代理生成代理类，该代理类是目标类的子类
 *  相关类：MethodInterceptor

### 使用 XML 配置 AOP ###

<aop:config >

- <aop:aspect >：配置切面，属性：id 该切面的标识名，ref 引用的切面 Bean，order 该切面 Bean 的优先级，子标签：<aop:pointcut >、<aop:before >、<aop:after >、<aop:after-retuming >、<aop:after-throwing >、<aop:around >，子标签属性：method、pointcut、pointcut-ref、throwing、returning
- <aop:pointcut >：配置切入点，属性：id 定该切入点的标识名，expression 该切入点关联的切入点表达式
- <aop:advisor >：将**单独配置的增强处理**和**切入点**绑定在一起，属性：advice-ref、pointcut-ref、pointcut、order、id

``````````xml
<aop:config> 
      <!-- what: 定义切面 --> 
      <aop:aspect ref="txManager"> 
          <!-- where: 定义切入点 --> 
          <aop:pointcut expression="execution(* com.example.tx.service.*Service.*(..))" id="pc"/> 
          <!-- when: 定义在什么时机做增强处理，以及具体做什么增强处理 --> 
          <aop:before method="begin" pointcut-ref="pc"/>  <!-- 前置增强 --> 
          <aop:after-returning method="commit" pointcut-ref="pc"/>  <!-- 后置增强 --> 
          <aop:after-throwing method="rollback" pointcut-ref="pc" throwing="ex"/>  <!-- 异常增强 --> 
          <aop:after method="close" pointcut-ref="pc"/>  <!-- 最终增强 --> 
          <aop:around method="allInOne" pointcut-ref="pc"/>  <!-- 环绕增强 --> 
      </aop:aspect> 
  <aop:config>
``````````

### 使用注解配置 AOP ###

 *  需在 XML 文件中开启 AOP 注解解析器 `<aop:aspectj-autoproxy/>`，启动 @AspectJ 支持；或者在启动类上添加 @EnableAspectJAutoProxy
*  定义切面类 Bean @Aspect
*  定义切入点 @Pointcut
   `@Pointcut("execution(* com.example.tx.service.*Service.*(..))")`
   使用一个返回值为 void、方法体为空的方法来命名切入点，`public void pointCut() {}`
*  定义增强处理，指定切入点，即 value 属性值
   @Around("pointCut()")
   @Before("pointCut()")
   @After("pointCut()")：不管目标方法如何结束（包括成功完成和遇到异常中止两种情况）都会被织入
   @AfterReturning("pointCut()") ：在目标方法正常完成后被织入
   @AfterThrowing(value="pointCut()", throwing="ex")
*  当定义 Around 增强处理方法时，该方法的第一个形参必须是 ProceedingJoinPoint 类型（JoinPoint 类型的子类，代表了织入增强处理的连接点），在增强处理方法体内调用 ProceedingJoinPoint 参数的 `proceed()` 方法才会执行目标方法
*  JoinPoint 接口中常用的方法
   `Object[] getArgs()`：返回执行目标方法时的参数
   `Signature getSignature()`：返回被增强的方法的相关信息
   `Object getTarget()`：返回被织入增强处理的目标对象
   `Object getThis()`：返回 AOP 框架为目标对象生成的代理对象
*  ProceedingJoinPoint 接口（JoinPoint 的子接口）中常用的方法
   `Object proceed()`：执行目标方法
   `Object proceed(Object[] args)`：args 中的值被传入目标方法作为执行方法的实参
*  @DeclareParents：引入新的类来增强功能

``````````java
// 定义一个切面 Bean 
  @Aspect 
  @Component 
  public class FourAdviceTest { 
  
      // 定义 Around 增强处理 
      @Around("execution(* com.example.app.service.impl.*.*(..))") 
      public Object processTx(ProceedingJoinPoint jp) throws Throwable { 
          System.out.println("Around 增强：执行目标方法之前，模拟开始事务..."); 
          // 访问执行目标方法的参数 
          Object[] args = jp.getArgs(); 
          // 当执行目标方法的参数存在，且第一个参数是字符串时 
          if (args != null && args.length > 0 && args[0].getClass() == String.class) { 
              // 修改目标方法调用参数的第一个参数 
              args[0] = "【增加的前缀】" + args[0]; 
          } 
          // 执行目标方法，并保存目标方法执行后的返回值 
          Object rvt = jp.proceed(args); 
          System.out.println("Around 增强：执行目标方法之后，模拟结束事务..."); 
          // 如果 rvt 的类型是 Integer，将 rvt 改为它的平方 
          if (rvt != null && rvt instanceof Integer) 
              rvt = (Integer) rvt * (Integer) rvt; 
          return rvt; 
      } 
  
      // 定义 Before 增强处理 
      @Before("execution(* com.example.app.service.impl.*.*(..))") 
      public void authority(JoinPoint jp) { 
          System.out.println("Before 增强：模拟执行权限检查..."); 
          // 返回被织入增强处理的目标方法 
          System.out.println("Before 增强：被织入增强处理的目标方法为：" 
                  + jp.getSignature().getName()); 
          // 访问执行目标方法的参数 
          System.out.println("Before 增强：目标方法的参数为：" 
                  + Arrays.toString(jp.getArgs())); 
          // 访问被增强处理的目标对象 
          System.out.println("Before 增强：被织入增强处理的目标对象为：" 
                  + jp.getTarget()); 
      } 
  
      //定义 AfterReturning 增强处理 
      @AfterReturning(pointcut = "execution(* com.example.app.service.impl.*.*(..))", 
              returning = "rvt") 
      public void log(JoinPoint jp, Object rvt) { 
          System.out.println("AfterReturning 增强：获取目标方法返回值：" + rvt); 
          System.out.println("AfterReturning 增强：模拟记录日志功能..."); 
          // 返回被织入增强处理的目标方法 
          System.out.println("AfterReturning 增强：被织入增强处理的目标方法为：" 
                  + jp.getSignature().getName()); 
          // 访问执行目标方法的参数 
          System.out.println("AfterReturning 增强：目标方法的参数为：" 
                  + Arrays.toString(jp.getArgs())); 
          // 访问被增强处理的目标对象 
          System.out.println("AfterReturning 增强：被织入增强处理的目标对象为：" 
                  + jp.getTarget()); 
      } 
  
      // 定义 After 增强处理 
      @After("execution(* com.example.app.service.impl.*.*(..))") 
      public void release(JoinPoint jp) { 
          System.out.println("After 增强：模拟方法结束后的释放资源"); 
          // 返回被织入增强处理的目标方法 
          System.out.println("After 增强：被织入增强处理的目标方法为：" 
                  + jp.getSignature().getName()); 
          // 访问执行目标方法的参数 
          System.out.println("After 增强：目标方法的参数为：" 
                  + Arrays.toString(jp.getArgs())); 
          // 访问被增强处理的目标对象 
          System.out.println("After 增强：被织入增强处理的目标对象为：" 
                  + jp.getTarget()); 
      } 
  }
``````````

# Spring 的 JDBC #

## JdbcTemplate 类 ##

 *  构造器：JdbcTemplate(DataSource dataSource)（调用该构造器时，dataSource 不能为 null，否则抛出异常）
 *  实例方法：query、queryForObject、queryForList、update、batchUpdate、execute`int update(String sql, Object… args)``<T> T queryForObject(String sql, RowMapper<T> rowMapper, Object… args)``<T> List<T> query(String sql, RowMapper<T> rowMapper, Object… args)`
 *  SimpleJdbcInsert 类、NamedParameterJdbcTemplate 类

## JdbcDaoSupport 抽象类 ##

 *  实例方法：`void setDataSource(DataSource dataSource)``JdbcTemplate getJdbcTemplate()`

## JDBC 异常抽象 ##

 *  Spring JDBC 会将数据操作的异常转换为 DataAccessException
 *  通过 SQLErrorCodeSQLEXceptionTranslator 解析错误码
 *  ErrorCode 定义：
    
     *  org/springframework/jdbc/support/sql-error-codes.xml
     *  classpath 下的 sql-error-codes.xml（可定制错误码）

# Spring 的事务管理 #

## 相关接口/类 ##

 *  **TransactionDefinition**，定义了一个事务规则：事务隔离、事务传播、事务超时、只读状态
    
     *  事务的传播方式：当一个方法已经在一个开启的事务当中了，应该怎么处理自身的事务  ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414392900.png) 
     *  事务隔离：默认值为 ISOLATION\_DEFAULT（-1），使用数据库的默认隔离级别
 *  **PlatformTransactionManager** Spring 具体的事务管理由 PlatformTransactionManager 的不同实现类来完成方法包括：开始事务 getTransaction()、提交事务 commit() 和回滚事务 rollback()常用实现类：DataSourceTransactionManager
 *  **TransactionStatus**，表示一个事务
 *  ProxyTransactionManagementConfiguration、TransactionInterceptor

## 配置事务管理器 ##

``````````xml
<!-- 配置 JDBC 数据源的事务管理器，使用 DataSourceTransactionManager 实现类 --> 
  <!-- 配置 DataSourceTransactionManager 时需要依赖注入 DataSource 的引用 --> 
  <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager"> 
      <property name="dataSource" ref="dataSource"/> 
  </bean>
``````````

## 声明式事务 ##

### 使用 XML 配置事务 ###

 *  子标签的常用属性
    
     *  name：指定对哪些方法起作用
     *  propagation：事务的传播方式，属性值：REQUIRED（默认值，要求在事务环境中执行该方法，如果当前执行线程己处于事务环境中，则直接调用，如果当前执行线程不处于事务环境中，则启动新的事务）、SUPPORTS（如果当前执行线程处于事务环境中，则使用当前事务，否则不使用事务）
     *  read-only：是否是只读事务，属性值：false（默认值）、true
     *  rollback-for：触发事务回滚的 Exception，默认是所有 **Runtime 异常**回滚
     *  no-rollback-for：不触发事务回滚的 Exception，默认是所有 Checked 异常不回滚
        

``````````xml
<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager"> 
      <property name="dataSource" ref="dataSource"/> 
  </bean> 
  
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
  
  <!-- 配置 AOP --> 
  <aop:config> 
      <!-- 定义一个切入点，通过 expression 指定对应的切入点表达式 --> 
      <aop:pointcut expression="execution(* com.example.ssm.service.*Servcie.*(..))" id="pc"/> 
      <!-- 将增强处理和切入点绑定在一起：指定在 pc 切入点应用 txAdvice 事务增强处理 --> 
      <aop:advisor advice-ref="txAdvice" pointcut-ref="pc"/> 
  </aop:config>
``````````

### 使用注解配置事务 ###

 *  开启事务注解解析器，并指定使用的事务管理器 Bean 的 id（默认为 transactionManager）：`<tx:annotation-driven transaction-manager="txManager"/>`
 *  使用 @Transactional 标注类或方法，可用属性
    
     *  transactionManager：指定使用的事务管理器 Bean 的 id
     *  isolation：用于指定事务的隔离级别，默认为底层事务的隔离级别 Isolation.DEFAULT
     *  propagation：指定事务传播行为，默认 Propagation.REQUIRED
     *  readOnly：指定事务是否只读，默认 false
     *  rollbackFor：指定遇到特定异常时强制回滚事务，默认 `ex instanceof RuntimeException || ex instanceof Error` 时才回滚
     *  rollbackForClassName：指定遇到特定的多个异常时强制回滚事务。该属性值可以指定多个异常类名
     *  noRollbackFor：指定遇到特定异常时强制不回滚事务
     *  noRollbackForClassName：指定遇到特定的多个异常时强制不回滚事务。该属性值可以指定多个异常类名
     *  timeout：指定事务的超时时长
 *  自调用失效问题
    
     *  一个类**自身方法之间的调用**，称为自调用
     *  在自调用过程中，是类自身的调用，而**不是**代理对象去调用，**不会产生 AOP**
     *  解决方法：访问**增强后的代理类**的方法，而非直接访问自身的方法，如：用一个 service 去调用另一个 service；从 Spring IoC 容器中获取 service 代理对象去启用 AOP

## 编程式事务 ##

 *  使用 TransactionTemplate 类手动实现事务
 *  `T execute(TransactionCallback<T> action)` 重写 TransactionCallback、TransactionCallbackWithoutResult 中的 doInTransaction 或 doInTransactionWithoutResult 方法

# Spring 的缓存抽象 #

 *  Spring 可以支持多种缓存管理机制，，如 ConcurrentMap、EhCache、Redis、Caffeine、JCache 等，并提供了缓存处理器的接口 CacheManager 和与之相关的类

# 工具类 #

## StringUtils ##

 *  org.springframework.util.StringUtils
 *  `boolean isEmpty(Object str)`：字符串是否为空或者空字符串
 *  `boolean hasLength(CharSequence str)`：字符串是否为空，或者长度为 0
 *  `boolean hasText(String str)`：字符串是否有内容（不为空，且不全为空格）
 *  `boolean containsWhitespace(String str)`：字符串是否包含空格
 *  `String trimWhitespace(String str)`：去掉字符串前后的空格
 *  `String trimAllWhitespace(String str)`：去掉字符串中所有的空格
 *  `String unqualify(String qualifiedName)`：得到以 . 分割的最后一个值
 *  `String unqualify(String qualifiedName, char separator)`：得到以给定字符分割的最后一个值
 *  `String getFilename(String path)`：获取文件名
 *  `String getFilenameExtension(String path)`：获取文件后缀名
 *  `String capitalize(String str)`：首字母大写
 *  `String uncapitalize(String str)`：取消首字母大写
 *  `boolean substringMatch(CharSequence str, int index, CharSequence substring)`：判断从指定索引开始，是否匹配子字符串
 *  `int countOccurrencesOf(String str, String sub)`：判断子字符串在字符串中出现的次数
 *  `String replace(String inString, String oldPattern, String newPattern)`：在字符串中使用子字符串替换
 *  `String delete(String inString, String pattern)`：删除所有匹配的子字符串
 *  `String deleteAny(String inString, String charsToDelete)`：删除子字符串中任意出现的字符
 *  `String quote(String str)`：在字符串前后增加单引号，比较适合在日志时候使用
 *  `String[] addStringToArray(String[] array, String str)`：把一个字符串添加到一个字符串数组中
 *  `String[] concatenateStringArrays(String[] array1, String[]array2)`：连接两个字符串数组
 *  `String[] mergeStringArrays(String[] array1, String[] array2)`：连接两个字符串数组，去掉重复元素
 *  `String[] sortStringArray(String[] array)`：字符串数组排序
 *  `String[] tokenizeToStringArray(String str, String delimiters)`：对每一个元素执行 trim 操作，并去掉空字符串
 *  `String[] delimitedListToStringArray(String str, String delimiter)`：分割字符串，以 delimiter 作为整体分隔符
 *  `Set<String> commaDelimitedListToSet(String str)`：使用逗号分割字符串，并放到 set 中去重
 *  `String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix)`：将集合中的每个元素使用前缀、后缀、分隔符连接
 *  `String collectionToDelimitedString(Collection<?> coll, String delim)`：将集合中的每个元素使用指定字符串连接
 *  `String arrayToDelimitedString(Object[] arr, String delim)`：数组使用指定字符串连接

## StopWatch ##

 *  秒表，可用于查看多个任务的耗时情况，线程不安全
 *  构造器：StopWatch(String id)
 *  `void start(String taskName)`
 *  `void stop()`
 *  `long getTotalTimeMillis()`、`long getLastTaskTimeMillis()`
 *  `TaskInfo[] getTaskInfo()`、`TaskInfo getLastTaskInfo()`
 *  `String shortSummary()`

## AopUtils ##

 *  org.springframework.aop.support.AopUtils
 *  `boolean isAopProxy(Object object)`：是否是代理对象
 *  `boolean isJdkDynamicProxy(Object object)`：判断是否是 JDK 代理对象
 *  `boolean isCglibProxy(Object object)`：判断是否是 cglib 代理对象
 *  `Class<?> getTargetClass(Object candidate)`：获取对象的真实类型
 *  `Method getMostSpecificMethod(Method method, Class<?> targetClass)`：获取真实对象上对应的方法
 *  `Object invokeJoinpointUsingReflection(Object target, Method method, Object[] args)`：在 target 对象上，使用 args 参数列表执行 method

## ClassUtils ##

## PropertiesLoaderUtils ##

 *  org.springframework.core.io.support.PropertiesLoaderUtils，主要用于加载 Properties 文件
 *  `Properties loadProperties(Resource resource)`：从一个资源文件加载 Properties
 *  `Properties loadProperties(EncodedResource resource)`：加载资源文件，传入的是提供了编码的资源类（EncodedResource）
 *  `void fillProperties(Properties props, Resource resource)`：从一个资源类中加载资源，并填充到指定的 Properties 对象中
 *  `void fillProperties(Properties props, EncodedResource resource)`：从一个编码资源类中加载资源，并填充到指定的 Properties 对象中
 *  `Properties loadAllProperties(String resourceName)`：根据资源文件名称，加载并合并 classpath 中的所有资源文件
 *  `Properties loadAllProperties(String resourceName, ClassLoader classLoader)`：从指定的 ClassLoader 中，根据资源文件名称，加载并合并 classpath 中的所有资源文件

## BeanUtils ##

 *  `void copyProperties(Object source, Object target, String… ignoreProperties)`：浅克隆

## NumberUtils ##

## CollectionUtils ##

## FileCopyUtils ##

 *  `int copy(File in, File out)`
 *  `void copy(byte[] in, File out)`
 *  `int copy(InputStream in, OutputStream out)`
 *  `void copy(byte[] in, OutputStream out)`
 *  `int copy(Reader in, Writer out)`
 *  `void copy(String in, Writer out)`
 *  `String copyToString(Reader in)`
 *  `byte[] copyToByteArray(File in)`
 *  `byte[] copyToByteArray(InputStream in)`

## UUID 生成器 ##

 *  `new AlternativeJdkIdGenerator().generateId()`

## Assert 断言工具类 ##

 *  `Assert.notNull(Object object, "object is required")`：对象非空
 *  `Assert.isTrue(Object object, "object must be true")`：对象必须为 true
 *  `Assert.notEmpty(Collection collection, "collection must not be empty")`：集合非空
 *  `Assert.hasLength(String text, "text must be specified")`：字符不为 null 且字符长度不为 0
 *  `Assert.hasText(String text, "text must not be empty")`：text 不为 null 且必须至少包含一个非空格的字符
 *  `Assert.isInstanceOf(Class clazz, Object obj, "clazz must be of type [clazz]")`：obj 必须能被正确造型成为 clazz 指定的类

## UriUtils ##

## MultiValueMap ##

 *  MultiValueMap extends Map  >，一个 Key 对应多个 Value
 *  常用实现类：LinkedMultiValueMap、HttpHeaders
 *  `V getFirst(K key)`
 *  `void add(K key, V value)`
 *  `void addAll(K key, List<? extends V> values)`
 *  `void addAll(MultiValueMap<K, V> values)`
 *  `void set(K key, V value)`
 *  `void setAll(Map<K, V> values)`
 *  `Map<K, V> toSingleValueMap()`：Return a Map with the first values contained in this MultiValueMap
    


[spring-overview]: https://static.sitestack.cn/projects/sdky-java-note/1fc8208bff09a13a1216b3f13df32203.png
[mvc-context-hierarchy]: https://static.sitestack.cn/projects/sdky-java-note/481c79fcb3cd34ca1b7175518b43fe6a.jpeg
[Spring Expression Language _SpEL]: https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions
[Supported Pointcut Designators]: https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#aop-pointcuts-designators
[237ffeffcedeca21cb7db717af833efd.png]: https://static.sitestack.cn/projects/sdky-java-note/237ffeffcedeca21cb7db717af833efd.png