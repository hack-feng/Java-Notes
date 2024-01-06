# Spring最新全家桶面试题

一、Spring Framework
1.谈谈你对Spring的理解
2.Spring的优缺点是什么？
二、Spring IOC
3.什么是Spring IOC 容器？有什么作用？
4.Spring IoC 的实现机制是什么？
5.什么是Spring的依赖注入(DI)？IOC和DI的区别是什么
6.紧耦合和松耦合有什么区别？
7.BeanFactory的作用

8. BeanDefinition的作用
9. BeanFactory 和 ApplicationContext有什么区别？
11. IOC容器的加载过程：
12.你知道Spring的哪些扩展点，在什么时候调用？
三、Spring Beans
13.什么是Spring beans？
14.配置Bean有哪几种方式？
15.解释Spring支持的几种bean的作用域
16、单例bean的优势
17.Spring实例化bean方式的几种方式
18.Spring框架中的单例bean是线程安全的吗？（阿里一面）
19.Spring如何处理线程并发问题？
20.什么是bean装配？
21.什么是bean的自动装配？
22. 自动装配有哪些限制(需要注意）？
23.解释不同方式的自动装配，spring 自动装配 bean 有哪些方式？
20.Spring 在加载过程中Bean有哪几种形态：
25. 解释Spring框架中bean的生命周期
26、Spring是如何解决Bean的循环依赖？
27.Spring如何避免在并发下获取不完整的Bean?
28.BeanDefinition的加载过程：
29. 如何在Spring所有BeanDefinition注册完后做扩展？
30.如何在Spring所有Bean创建完后做扩展？
31、Spring容器启动时，为什么先加载BeanFactoryPostProcess

```
图灵课堂
```

32. Bean的创建顺序是什么样的？
四、Spring注解
33.Spring有哪几种配置方式：
34.用过JavaConfig方式的spring配置吗？它是如何替代xml的？
35.@Component, @Controller, @Repository, @Service 有何区别？
36.@Import可以有几种用法？
37.如何让自动注入没有找到依赖Bean时不报错
38.如何让自动注入找到多个依赖Bean时不报错
40.@Autowired和@Resource之间的区别
41.使用@Autowired注解自动装配的过程是怎样的？
42.配置类@Configuration的作用解析原理:
43.@Bean之间的方法调用是怎么保证单例的？
44.要将一个第三方的类配成为Bean有哪些方式？
45、为什么@ComponentScan 不设置basePackage也会扫描？
五、Spring  AOP
46.什么是AOP、能做什么
47.解释一下Spring AOP里面的几个名词
48.Spring通知有哪些类型？
49.Spring AOP and AspectJ AOP 有什么区别？
50.JDK动态代理和CGLIB动态代理的区别
51.JavaConfig方式如何启用AOP?如何强制使用cglib?
52.介绍AOP有几种实现方式
53.什么情况下AOP会失效,怎么解决？
54.Spring的AOP是在哪里创建的动态代理？
55.Spring的 Aop的完整实现流程？
六、Spring事务
56.事务四大特性
57.Spring支持的事务管理类型， spring 事务实现方式有哪些？
59.说一下 spring 的事务隔离？
60.Spring事务实现基本原理
61. Spring事务传播行为实现原理：
62.Spring多线程事务 能否保证事务的一致性（同时提交、同时回滚）？
63.Spring事务的失效原因？
七、Spring其他
64.Spring事件监听的核心机制是什么？
65.Spring 框架中都用到了哪些设计模式？

```
图灵课堂
```

66.Spring是如何整合MyBatis将Mapper接口注册为Bean的原理？
八、SpringMVC
67.说说你是如何解决 get 和 post 乱码问题？
68.Spring MVC的控制器是不是单例模式,如果是,有什么问题,怎么解决？
69.请描述Spring MVC的工作流程？描述一下 DispatcherServlet 的工作流程？
70.SpringMvc怎么和AJAX相互调用的？
71.Spring和SpringMVC为什么需要父子容器？
72.是否可以把所有Bean都通过Spring容器来管理？（Spring的applicationContext.xml中配置全局扫描)
73.是否可以把我们所需的Bean都放入Spring-mvc子容器里面来管理（springmvc的spring-servlet.xml中配置全局扫描）?
74.如何实现无XML零配置的SpringMVC
75.SpringMVC的拦截器和过滤器有什么区别？执行顺序？
九、Spring Boot
76.谈谈你对SpringBoot的理解，它有哪些特性（优点）？
77.Spring和SpringBoot的关系和区别？
78.SpringBoot的核心注解
79.springboot的自动配置原理？
80.为什么SpringBoot的jar可以直接运行？
81.SpringBoot的启动原理？
82.SpringBoot内置Tomcat启动原理？
83.SpringBoot外置Tomcat启动原理？
84.会不会SpringBoot自定义Starter？大概实现过程？
85.SpringBoot读取配置文件的原理是什么？加载顺序是怎样的?
86.SpringBoot的默认日志实现框架是什么？怎么切换成别的？
87.说说你在开发的时候怎么在SpringBoot的基础上做扩展？
十、微服务
88、微服务架构的优缺点
89.SOA、分布式、微服务之间有什么关系和区别？
90.怎么拆分微服务、拆分时机是什么？
91.Spring Cloud有哪些常用组件，作用是什么？
92.注册中心的原理是什么？
93.谈谈配置中心？
94.说说服务网关可以做什么？
95.什么是服务雪崩？什么是服务限流？
96.什么是服务熔断？什么是服务降级？区别是什么？
97.说说Seata的实现原理？
98.你的微服务项目出了异常怎样更快速的定位？

```
图灵课堂
```

```
99.Ribbon说说有哪些负载均衡策略
100.你项目哪些场景用到了限流、降级？怎么配的？
```
#### 一、Spring Framework

###### 1.谈谈你对Spring的理解

什么是spring
Spring是一个生态：可以构建java应用所需的一切基础设施
通常Spring指的就是Spring Framework 

核心解释
spring是一个轻量级的开源容器框架。
spring是为了解决企业级应用开发的业务逻辑层和其他各层对象和对象直接的耦合问题
spring是一个IOC和AOP的容器框架。
IOC：控制反转
AOP：面向切面编程
容器：包含并管理应用对象的生命周期

###### 2.Spring的优缺点是什么？

```
特点：
1.方便解耦，简化开发
通过Spring提供的IoC容器，我们可以将对象之间的依赖关系交由Spring进行控制，避免硬编码所造成的过度程序耦
合。
有了Spring，用户不必再为单实例模式类、属性文件解析等这些很底层的需求编写代码，可以更专注于上层的应用。
人话：集中管理对象，对象和对象之间的耦合度减低，方便维护对象。
2.AOP编程的支持
通过Spring提供的AOP功能，方便进行面向切面的编程，许多不容易用传统OOP实现的功能可以通过AOP轻松应
付。
Spring的AOP支持允许将一些通用任务如安全、事务、日志等进行集中式管理，从而提供了更好的复用.
人话： 在不修改代码的情况下可以对业务代码进行增强   减少重复代码 提高开发效率 维护方便
3.声明事物的支持
在Spring中，我们可以从单调烦闷的事务管理代码中解脱出来，通过声明式方式灵活地进行事务的管理，提高开发效
率和质量。
人话：提高开发效率，只需要一个简单注解@Transactional
4.方便程序的测试
可以用非容器依赖的编程方式进行几乎所有的测试工作，在Spring里，测试不再是昂贵的操作，而是随手可做的事
情。例如：Spring对Junit4支持，可以通过注解方便的测试Spring程序。
```
```
图灵课堂
```

```
人话： Spring实现测试  使我们 可以结合junit非常方便测试Spring Bean  SpringMVC
5.方便集成各种优秀框架
Spring不排斥各种优秀的开源框架，相反，Spring可以降低各种框架的使用难度，Spring提供了对各种优秀框架
（如Struts,Hibernate、Hessian、Quartz）等的直接支持。
人话：  拥有非常强大粘合度、集成能力非常，只需要简单配置就可以集成第三方框架  
6.降低Java EE API的使用难度
Spring对很多难用的Java EE API（如JDBC，JavaMail，远程调用等）提供了一个薄薄的封装层，通过Spring的简易
封装，这些Java EE API的使用难度大为降低。
人话：简化开发， 帮我封装很多功能性代码
7.Java 源码是经典学习范例
Spring的源码设计精妙、结构清晰、匠心独用，处处体现着大师对Java设计模式灵活运用以及对Java技术的高深造
诣。Spring框架源码无疑是Java技术的最佳实践范例。如果想在短时间内迅速提高自己的Java技术水平和应用开发水
平，学习和研究Spring源码将会使你收到意想不到的效果。 
人话：学习到了Spring底层的实现、反射..设计模式 都是我们值得学习，  提供非常多的扩展接口供外部进行扩展
缺点
从应用层面来说是没有缺点的  
简化开发， 如果想深入到底层去了解就非常困难（上层使用越简单、底层封装得就越复杂）
源码缺点：由于spring 大而全（要集成这么多框架、提供非常非常多的扩展点，经过十多年的代码迭代） 代码量非常庞大 
,一百多万  对于去深入学习源码带来了一定困难。
```
## 二、Spring IOC

##### 3.什么是Spring IOC 容器？有什么作用？

控制反转即IoC (Inversion of Control)，它把传统上由程序代码直接操控的对象的调用权交给容器，通过容器来实现对
象组件的装配和管理。所谓的“控制反转”概念就是对组件对象控制权的转移，从程序代码本身转移到了外部容器。
Spring IOC 负责创建对象，管理对象（通过依赖注入（DI），装配对象，配置对象，并且管理这些对象的整个生命周
期。

对于 IoC 来说，最重要的就是容器。容器管理着 Bean 的生命周期，控制着 Bean 的依赖注入。
控制反转(IoC)有什么作用
管理对象的创建和依赖关系的维护。对象的创建并不是一件简单的事，在对象关系比较复杂时，如果依赖关系
需要程序猿来维护的话，那是相当头疼的
解耦，由容器去维护具体的对象
托管了类的产生过程，比如我们需要在类的产生过程中做一些处理，最直接的例子就是代理，如果有容器程序
可以把这部分处理交给容器，应用程序则无需去关心类是如何完成代理的

人话：
作用：
 控制反转    控制了什么？
UserService service=new UserService();   // 耦合度太高 、维护不方便
引入Ioc   就将创建对象的控制权交给Spring的Ioc.   以前由程序员自己控制对象创建， 现在交给Spring的Ioc去创建， 
如果要去使用对象需要通过DI（依赖注入）@Autowired 自动注入 就可以使用对象 ;    

```
图灵课堂
```

优点：  1.集中管理对象、方便维护 。2.降低耦合度

IOC的优点是什么？ 
最小的代价和最小的侵入性使松散耦合得以实现。
IOC容器支持加载服务时的饿汉式初始化和懒加载。

##### 4.Spring IoC 的实现机制是什么？

Spring 中的 IoC 的实现原理就是工厂模式加反射机制。
示例：
1
2 interface fruit{
3      public abstract void eat();
4 }  
5 class Apple implements fruit{
6 public void eat(){
7          System.out.println("Apple");
8      }
9 }  
10 class Orange implements fruit{
11 public void eat(){
12         System.out.println("Orange");
13     }  
14 }  
15 class Factory{  
16     public static fruit getInstance(String ClassName){
17         fruit f=null;  
18         try{  
19             f=(fruit)Class.forName(ClassName). newInstance();
20         }catch (Exception e) {  
21             e.printStackTrace();
22         }
23         return f;
24     }  
25 }  
26 class hello{
27     public static void main(String[]  a){
28         fruit f=Factory.getInstance("Reflect.Apple");
29         if(f!=null){
30             f.eat();
31         }
32     }  
33 }  

###### 5.什么是Spring的依赖注入(DI)？IOC和DI的区别是什么

很多人把IOC和DI说成一个东西，笼统来说的话是没有问题的，但是本质上还是有所区别的,希望大家能够严谨一点，IOC和DI是从不同的
角度描述的同一件事，IOC是从容器的角度描述，而DI是从应用程序的角度来描述，也可以这样说，IOC是依赖倒置原则的设计思想，而
DI是具体的实现方式

在面向对象设计的软件系统中，底层的实现都是由N个对象组成的，所有的对象通过彼此的合作，最终实现系统的业务逻
辑。

```
图灵课堂
```

有一个对象出了问题，就可能会影响到整个流程的正常运转。现在，伴随着工业级应用的规模越来越庞大，对象之间的依赖关系也越来越
复杂，经常会出现对象之间的多重依赖性关系，因此，架构师和设计师对于系统的分析和设计，将面临更大的挑战。对象之间耦合度过高
的系统，必然会出现牵一发而动全身的情形。

大家看到了吧，由于引进了中间位置的“第三方”，也就是IOC容器，对象和对象之间没有了耦合关系， 它起到了一种
类似“粘合剂”的作用，把系统中的所有对象粘合在一起发挥作用，如果没有这个“粘合剂”，对象与对象之间会彼此
失去联系，这就是有人把IOC容器比喻成“粘合剂”的由来。

###### 6.紧耦合和松耦合有什么区别？

紧耦合：

##### 紧密耦合是指类之间高度依赖。

松耦合：

##### 松耦合是通过促进单一职责和关注点分离、依赖倒置的设计原则来实现的。

###### 7.BeanFactory的作用

```
BeanFactory是Spring中非常核心的一个顶层接口；
它是Bean的“工厂”、它的主要职责就是生产Bean；
它实现了简单工厂的设计模式，通过调用getBean传入标识生产一个Bean；
它有非常多的实现类、每个工厂都有不同的职责（单一职责）功能，最强大的工厂是：DefaultListableBeanFactory  
Spring底层就是使用的该实现工厂进行生产Bean的
BeanFactory它也是容器         Spring容器（管理着Bean的生命周期）    
```
```
图灵课堂
```

###### 8. BeanDefinition的作用 

它主要负责存储Bean的定义信息:决定Bean的生产方式。

 如：spring.xml 
1 <bean class="com.tuling.User" id="user" scope="singleton" lazy="false" abstract="false" autowire="none" .... >
2 <property name="username" value="xushu">
3 </bean
4
后续BeanFactory根据这些信息就行生产Bean： 比如实例化  可以通过class进行反射进而得到实例对象 ， 比如lazy  则不会在ioc加载时
创建Bean

##### 9. BeanFactory 和 ApplicationContext有什么区别？

BeanFactory和ApplicationContext是Spring的两大核心接口，都可以当做Spring的容器。其中ApplicationContext是
BeanFactory的子接口。
依赖关系
BeanFactory：是Spring里面最顶层的接口，包含了各种Bean的定义，读取bean配置文档，管理bean的加载、实例
化，控制bean的生命周期，维护bean之间的依赖关系。BeanFactory 简单粗暴，可以理解为就是个 HashMap，Key 是 
BeanName，Value 是 Bean 实例。通常只提供注册（put），获取（get）这两个功能。我们可以称之为 “低级容
器”。

```
图灵课堂
```

ApplicationContext 可以称之为 “高级容器”。因为他比 BeanFactory 多了更多的功能。他继承了多个接口。因此具
备了更多的功能。例如资源的获取，支持多种消息（例如 JSP tag 的支持），对 BeanFactory 多了工具级别的支持等
待。所以你看他的名字，已经不是 BeanFactory 之类的工厂了，而是 “应用上下文”， 代表着整个大容器的所有功
能。该接口定义了一个 refresh 方法，此方法是所有阅读 Spring 源码的人的最熟悉的方法，用于刷新整个容器，即重新
加载/刷新所有的 bean。
ApplicationContext接口作为BeanFactory的派生，除了提供BeanFactory所具有的功能外，还提供了更完整的框架功
能： 

官方：

```
图灵课堂
```

#### 10.BeanFactory 和FactoryBean有什么区别？

BeanFactory是一个工厂，也就是一个容器，是来管理和生产bean的；

FactoryBean是一个bean，但是它是一个特殊的bean，所以也是由BeanFactory来管理的，
它是一个接口，他必须被一个bean去实现。
　　不过FactoryBean不是一个普通的Bean，它会表现出工厂模式的样子,是一个能产生或者修饰对象生成的工厂Bean，
　　里面的getObject()就是用来获取FactoryBean产生的对象。所以在BeanFactory中使用“&”来得到FactoryBean本身，
　　用来区分通过容器获取FactoryBean产生的对象还是获取FactoryBean本身。

```
图灵课堂
```

###### 11. IOC容器的加载过程：

从概念态--->定义态的过程
1、实例化一个ApplicationContext的对象； 
2：调用bean工厂后置处理器完成扫描； 
3：循环解析扫描出来的类信息； 
4、实例化一个BeanDefinition对象来存储解析出来的信息； 
5、把实例化好的beanDefinition对象put到beanDefinitionMap当中缓存起来， 
以便后面实例化bean； 
6、再次调用其他bean工厂后置处理器；
从定义态到纯净态
7：当然spring还会干很多事情，比如国际化，比如注册BeanPostProcessor等 
等，如果我们只关心如何实例化一个bean的话那么这一步就是spring调用 
finishBeanFactoryInitialization方法来实例化单例的bean，实例化之前spring要做验证， 
需要遍历所有扫描出来的类，依次判断这个bean是否Lazy，是否prototype，是否 
abstract等等； 
8：如果验证完成spring在实例化一个bean之前需要推断构造方法，因为spring实 
例化对象是通过构造方法反射，故而需要知道用哪个构造方法； 
9：推断完构造方法之后spring调用构造方法反射实例化一个对象；注意我这里说 
的是对象、对象、对象；这个时候对象已经实例化出来了，但是并不是一个完整的bean， 
最简单的体现是这个时候实例化出来的对象属性是没有注入，所以不是一个完整的bean；
从纯净态到成熟态
10：spring处理合并后的beanDefinition 
11：判断是否需要完成属性注入
12：如果需要完成属性注入，则开始注入属性
初始化
13、判断bean的类型回调Aware接口 
14、调用生命周期回调方法 
15、如果需要代理则完成代理
创建完成  
16、put到单例池——bean完成——存在spring容器当中

```
图灵课堂
```

###### 12.你知道Spring的哪些扩展点，在什么时候调用？

Spring中非常非常多的扩展接口，当然你也不需要全部回答，可以挑重点回答： 

1. 执行BeanFactoryPostProcessor的postProcessBeanFactory方法
1 /***
2 * 作用： 在注册BeanDefinition的可以对beanFactory进行扩展 后
3 * 调用时机： Ioc加载时注册BeanDefinition 的时候会调用
4 */
5 public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
6  @Override
7  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
8
9  }
10 }
2. 执行BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry方法：
1

```
图灵课堂
```

```
2 /***
3 * 作用：动态注册BeanDefinition
4 * 调用时机： Ioc加载时注册BeanDefinition 的时候会调用
5 */
6 @Component
7 public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
8  @Override
9  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
10
11
12  RootBeanDefinition beanDefinition = new RootBeanDefinition(Car.class);
13  registry.registerBeanDefinition("car",beanDefinition);
14  }
```
3. 加载BeanPostProcessor实现类 : 在Bean的生命周期会调用9次Bean的后置处理器
4. 创建所有单例bean

初始化阶段:

5. 初始化阶段调用XXXAware接口的SetXXXAware方法 ：

###### 生命周期回调： 初始化、销毁

6. 执行BeanPostProcessor实现类的postProcessBeforeInitialization方法 
7. 执行InitializingBean实现类的afterPropertiesSet方法
8. 执行bean的init-method属性指定的初始化方法
9. 执行BeanPostProcessor实现类的postProcessAfterInitialization方法
10. 初始化完成
11. 关闭容器，执行DiposibleBean实现类的destory
12. 执行bean的destroy-method属性指定的初始化方法

#### 三、Spring Beans 

##### 13.什么是Spring beans？

Spring 官方文档对 bean 的解释是：
In Spring, the objects that form the backbone of your application and that are managed by the Spring IoC container are called beans. A bean is an object that is instantiated, assembled, and otherwise managed by a Spring IoC
container.
翻译过来就是：

```
图灵课堂
```

在 Spring 中，构成应用程序主干并由Spring IoC容器管理的对象称为bean。bean是一个由Spring IoC容器实例
化、组装和管理的对象。
概念简单明了，我们提取处关键的信息：

1. bean是对象，一个或者多个不限定
2. bean由Spring中一个叫IoC的东西管理

###### 14.配置Bean有哪几种方式？

1.xml: <bean class="com.tuling.UserService" id="">
2.注解：@Component(@Controller 、@Service、@Repostory)   前提：需要配置扫描包<component-scan>   反射调用构造方法
3.javaConfig: @Bean   可以自己控制实例化过程
4.@Import    3种方式

###### 15.解释Spring支持的几种bean的作用域

Spring框架支持以下五种bean的作用域：
singleton : bean在每个Spring ioc 容器中只有一个实例。
prototype：一个bean的定义可以有多个实例。
request：每次http请求都会创建一个bean，该作用域仅在基于web的Spring ApplicationContext情形下有效。
session：在一个HTTP Session中，一个bean定义对应一个实例。该作用域仅在基于web的Spring ApplicationContext情
形下有效。
application：全局 Web 应用程序范围的范围标识符。
注意： 缺省的Spring bean 的作用域是Singleton。使用 prototype 作用域需要慎重的思考，因为频繁创建和销毁 bean 
会带来很大的性能开销。

###### 16、单例bean的优势

由于不会每次都新创建新对象所以有一下几个性能上的优势：
1.减少了新生成实例的消耗新生成实例消耗包括两方面，第一，spring会通过反射或者cglib来生成bean实例这都是耗性能的操作，其次给
对象分配内存也会涉及复杂算法。 提供服务器内存的利用率 ，减少服务器内存消耗  
2.减少jvm垃圾回收由于不会给每个请求都新生成bean实例，所以自然回收的对象少了。
3.可以快速获取到bean因为单例的获取bean操作除了第一次生成之外其余的都是从缓存里获取的所以很快。 

###### 17.Spring实例化bean方式的几种方式

1. 构造器方式（反射）；  
2. 静态工厂方式； factory-method 
3. 实例工厂方式(@Bean)；  factory-bean+factory-method
4. FactoryBean方式 

###### 18.Spring框架中的单例bean是线程安全的吗？（阿里一面）

不是，Spring框架中的单例bean不是线程安全的。
spring 中的 bean 默认是单例模式，spring 框架并没有对单例 bean 进行多线程的封装处理。
实际上大部分时候 spring bean 无状态的（比如 dao 类），所以某种程度上来说 bean 也是安全的，但如果 bean 有状
态的话（比如 view model 对象），那就要开发者自己去保证线程安全了，最简单的就是改变 bean 的作用域，
把“singleton”变更为“prototype”，这样请求 bean 相当于 new Bean()了，所以就可以保证线程安全了。
有状态就是有数据存储功能（比如成员变量读写）。
无状态就是不会保存数据。

###### 19.Spring如何处理线程并发问题？

```
图灵课堂
```

在一般情况下，只有无状态的Bean才可以在多线程环境下共享，在Spring中，绝大部分Bean都可以声明为singleton作
用域，因为Spring对一些Bean中非线程安全状态采用ThreadLocal进行处理，解决线程安全问题。
ThreadLocal和线程同步机制都是为了解决多线程中相同变量的访问冲突问题。同步机制采用了“时间换空间”的方式，
仅提供一份变量，不同的线程在访问前需要获取锁，没获得锁的线程则需要排队。而ThreadLocal采用了“空间换时
间”的方式。
ThreadLocal会为每一个线程提供一个独立的变量副本，从而隔离了多个线程对数据的访问冲突。因为每一个线程都拥有
自己的变量副本，从而也就没有必要对该变量进行同步了。ThreadLocal提供了线程安全的共享对象，在编写多线程代码
时，可以把不安全的变量封装进ThreadLocal。
1
2 /***
3 * @Author 徐庶 QQ:
4 * @Slogan 致敬大师，致敬未来的你
5 *
6 * 单例Bean的情况
7 * 如果在类中声明成员变量 并且有读写操作（有状态），就是线程不安全
8 * 解决：
9 * 1.设置为多例
10 * 2.将成员变量放在ThreadLocal
11 * 3.同步锁 会影响服务器吞吐量
12 * 但是!
13 * 只需要把成员变量声明在方法中（无状态）， 单例Bean是线程安全的
14 */
15 public class Run {
16  public static void main(String[]  args) {
17
18
19  AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
20
21  // 线程一
22  UserService bean = applicationContext.getBean(UserService.class);
23  new Thread(()  ‐>  {
24  System.out.println(bean.welcome("张三"));
25  }). start();
26
27  // 线程二
28  UserService bean2 = applicationContext.getBean(UserService.class);
29  new Thread(()  ‐>  {
30  System.out.println(bean2.welcome("李四"));
31  }). start();
32
33  }

###### 20.什么是bean装配？

装配，或bean 装配是指在Spring 容器中把bean组装到一起，前提是容器需要知道bean的依赖关系，如何通过依赖注入
来把它们装配到一起。

###### 21.什么是bean的自动装配？

在Spring框架中，在配置文件中设定bean的依赖关系是一个很好的机制，Spring 容器能够自动装配相互合作的bean，
这意味着容器不需要和配置，能通过Bean工厂自动处理bean之间的协作。这意味着 Spring可以通过向Bean Factory中
注入的方式自动搞定bean之间的依赖关系。自动装配可以设置在每个bean上，也可以设定在特定的bean上。

###### 22. 自动装配有哪些限制(需要注意）？ 

```
一定要声明set方法   
覆盖： 你仍可以用 < constructor-arg >和 < property > 配置来定义依赖，这些配置将始终覆盖自动注入。
```
```
图灵课堂
```

```
基本数据类型：不能自动装配简单的属性，如基本数据类型、字符串和类。    (手动注入还是可以注入基本数
据类型的 <property  value=""   @Value)
模糊特性：自动装配不如显式装配精确，如果有可能尽量使用显示装配。    
```
所以更推荐使用手动装配(@Autowired（根据类型、再根据名字）    ref="" 这种方式 更加灵活更加清晰 )  

###### 23.解释不同方式的自动装配，spring 自动装配 bean 有哪些方式？

在spring中，对象无需自己查找或创建与其关联的其他对象，由容器负责把需要相互协作的对象引用赋予各个对象，使
用autowire来配置自动装载模式。
在Spring框架xml配置中共有5种自动装配：
no：默认的方式是不进行自动装配的，通过手工设置ref属性来进行装配bean。@Autowired 来进行手动指定
需要自动注入的属性
byName：通过bean的名称进行自动装配，如果一个bean的 property 与另一bean 的name 相同，就进行自
动装配。
byType：通过参数的数据类型进行自动装配。
constructor：利用构造函数进行装配，并且构造函数的参数通过byType进行装配。
autodetect：自动探测，如果有构造方法，通过 construct的方式自动装配，否则使用 byType的方式自动装
配。 （在spring3.0+弃用）

### 24.有哪些生命周期回调方法？有哪几种实现方式？

有两个重要的bean 生命周期方法，第一个是init ， 它是在容器加载bean的时候被调用。第二个方法是 destroy 它是在
容器卸载类的时候被调用。
bean 标签有两个重要的属性（init-method和destroy-method）。用它们你可以自己定制初始化和注销方法。它们也
有相应的注解（@PostConstruct和@PreDestroy）。

###### 20.Spring 在加载过程中Bean有哪几种形态：

```
图灵课堂
```

###### 25. 解释Spring框架中bean的生命周期 

Bean生命周期：指定的就是Bean从创建到销毁的整个过程:     分4大不：

1. 实例化
    a. 通过反射去推断构造函数进行实例化
    b. 实例工厂、 静态工厂
2. 属性赋值
    a. 解析自动装配（byname bytype constractor  none  @Autowired）   DI的体现
    b. 循环依赖
3. 初始化
    a. 调用XXXAware回调方法
    b. 调用初始化生命周期回调（三种）
    c. 如果bean实现aop  创建动态代理
4. 销毁
    a. 在spring容器关闭的时候进行调用
    b. 调用销毁生命周期回调

下图展示了bean装载到Spring应用上下文中的一个典型的生命周期过程。

bean在Spring容器中从创建到销毁经历了若干阶段，每一阶段都可以针对Spring如何管理bean进行个性化定制。
正如你所见，在bean准备就绪之前，bean工厂执行了若干启动步骤。
我们对上图进行详细描述：
Spring对bean进行实例化；
Spring将值和bean的引用注入到bean对应的属性中；
如果bean实现了BeanNameAware接口，Spring将bean的ID传递给setBean-Name()方法；
如果bean实现了BeanFactoryAware接口，Spring将调用setBeanFactory()方法，将BeanFactory容器实例传入；
如果bean实现了ApplicationContextAware接口，Spring将调用setApplicationContext()方法，将bean所在的应用上
下文的引用传入进来；
如果bean实现了BeanPostProcessor接口，Spring将调用它们的post-ProcessBeforeInitialization()方法；
如果bean实现了InitializingBean接口，Spring将调用它们的after-PropertiesSet()方法。类似地，如果bean使用
initmethod声明了初始化方法，该方法也会被调用；
如果bean实现了BeanPostProcessor接口，Spring将调用它们的post-ProcessAfterInitialization()方法；
此时，bean已经准备就绪，可以被应用程序使用了，它们将一直驻留在应用上下文中，直到该应用上下文被销毁；
如果bean实现了DisposableBean接口，Spring将调用它的destroy()接口方法。同样，如果bean使用destroy-method
声明了销毁方法，该方法也会被调用。
现在你已经了解了如何创建和加载一个Spring容器。但是一个空的容器并没有太大的价值，在你把东西放进去之前，它
里面什么都没有。为了从Spring的DI(依赖注入)中受益，我们必须将应用对象装配进Spring容器中。

```
图灵课堂
```

###### 26、Spring是如何解决Bean的循环依赖？

Spring是如何解决的循环依赖： 采用三级缓存解决的  就是三个Map    ；  关键： 一定要有一个缓存保存它的早期对象作为死循环的出口

1. 一级缓存：存储完整的Bean
2. 二级缓存： 避免多重循环依赖的情况 重复创建动态代理。
3. 三级缓存： 
    a. 缓存是函数接口：通过lambda  把方法传进去（ 把Bean的实例和Bean名字传进去（aop创建） ）   
    b. 不会立即调：（如果在实例化后立即调用的话：所有的aop 不管bean是否循环依赖都会在 实例化后创建
    proxy,   正常Bean 其实spring还是希望遵循生命周期在初始化创建动态代理， 只能循环依赖才创建)
    c. 会在 ABA  (第二次getBean(A) 才会去调用三级缓存（如果实现了aop才会创建动态代理，如果没有实现
    依然返回的Bean的实例））   
    d. 放入二级缓存（避免重复创建）

夺命连环问：

1. 二级缓存能不能解决循环依赖？
    a.  如果只是死循环的问题：  一级缓存就可以解决 ：无法避免在并发下获取不完整的Bean?

```
图灵课堂
```

b. 二级缓存也可以解决循环依赖：  只不过如果出现重复循环依赖   会多次创建aop的动态代理

2. Spring有没有解决多例Bean的循环依赖？
    a. 多例不会使用缓存进行存储（多例Bean每次使用都需要重新创建）
    b. 不缓存早期对象就无法解决循环
3. Spring有没有解决构造函数参数Bean的循环依赖？
    a. 构造函数的循环依赖也是会报错
    b. 可以通过人工进行解决：@Lazy 

##### i. 就不会立即创建依赖的bean了

##### ii. 而是等到用到才通过动态代理进行创建

###### 27.Spring如何避免在并发下获取不完整的Bean?

双重检查锁   
为什么一级缓存不加到锁里面：
性能：避免已经创建好的Bean阻塞等待

###### 28.BeanDefinition的加载过程：

BeanDefinition的加载过程就是将 概念态的Bean注册为定义态的Bean
不同的Spring上下文会有不同的注册过程，但是会用共同的api步骤：

1. 通过BeanDefinitionReader 将配置类(AnnotatedBeanDefinitionReader)（xml文件:XmlBeanDefinitionReader) 注
册为BeanDefinition
2. 解析配置类ConfigurationClassParser(xml文件:BeanDefinitionDocumentReader）
    3. 不同的注解（xml节点）有不同的解析器

##### a. 比如ComponentScan 需要通过ClassPathBeanDefinitionScanner扫描所有类找到类上面有

@Import的类

4. 将读取到的Bean定义信息通过BeanDefinitionRegistry注册为一个BeanDefinition

```
图灵课堂
```

###### 29. 如何在Spring所有BeanDefinition注册完后做扩展？

通常可以使用beanFactoryPostProcessor 对已注册的BeanDefinition进行修改、
或者通过它的子接口BeanDefinitionRegistryPostProcessor 再进行注册

###### 30.如何在Spring所有Bean创建完后做扩展？

哪里才算所有的Bean创建完：  new ApplicationContext()---->refresh()---->finishBeanFactoryInitialization（循环所有的
BeanDefinition ,通过BeanFactory.getBean()生成所有的Bean）    这个循环结束之后所有的bean也就创建完了

###### 31、Spring容器启动时，为什么先加载BeanFactoryPostProcess

1.因为BeanDefinition会在ioc容器加载的先注册， 而BeanFactoryPostProcess就是在所有的BeanDefinition注册完后做扩展的，所以要
先加载BeanFactoryPostProcess

2. 解析配置类的组件  它就实现BeanFactoryPostProcess， 所以要先去加载BeanFactoryPostProcess

### 1.方式一 基于SmartInitializingSingleton接口

### Source

### 在创建所有单例Bean的方法中： 

```
1  finishBeanFactoryInitialization(beanFactory);
```
### SmartInitializingSingleton接口是在所有的Bean实例化完成以后，Spring回调的方法, 

### 所以这里也是一个扩展点，可以在单例bean全部完成实例化以后做处理。

```
图灵课堂
```

### Code

### 【配置类】

```
1 package com.artisan.beanLoadedExtend.smartinit;
2
3 import org.springframework.context.annotation.ComponentScan;
4 import org.springframework.context.annotation.Configuration;
5
6 @Configuration
7 @ComponentScan("com.artisan.beanLoadedExtend")
8 public class SmartInitConfig {
```
### 【扩展类 implements SmartInitializingSingleton 】

```
1 package com.artisan.beanLoadedExtend.smartinit;
2
3
4 import org.springframework.beans.factory.SmartInitializingSingleton;
5 import org.springframework.stereotype.Component;
6
7
8 @Component
9 public class SmartInitExtend implements SmartInitializingSingleton {
10
11  @Override
12  public void afterSingletonsInstantiated()  {
13  System.out.println("all singleton beans loaded , 自定义扩展here ");
```
```
图灵课堂
```

```
14  }
15 }
```
### 【测试】

```
1 package com.artisan.beanLoadedExtend.smartinit;
2
3
4 import org.springframework.context.annotation.AnnotationConfigApplicationContext;
5
6 public class Test {
7
8  public static void main(String[]  args) {
9  AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SmartInitConfig.class);
10
11
12  }
13 }
```
### 2.方式二 基于Spring事件监听

#### Source

生命周期的最后一步是finishRefresh();，这里面中有一个方法是publishEvent

所以这里也可以进行扩展，监听ContextRefreshedEvent事件 。

###### 32. Bean的创建顺序是什么样的？

Bean的创建顺序是由BeanDefinition的注册顺序来决定的,   当然依赖关系也会影响Bean创建顺序  （A-B)。

BeanDefinition的注册顺序由什么来决定的？
主要是由注解（配置）的解析顺序来决定：

1. @Configuration   
2. @Component 
3. @Import—类
4. @Bean
5. @Import—ImportBeanDefinitionRegistrar

```
图灵课堂
```

```
6、BeanDefinitionRegistryPostProcessor
```
#### 四、Spring注解 

###### 33.Spring有哪几种配置方式：

 这里有三种重要的方法给Spring 容器提供配置元数据。

1. XML配置文件。  spring诞生
    a. spring.xml     <bean>
2. 基于注解的配置。  Spring2.5+
    a. spring.xml  <component-scan base-package=" "/>    @Component  @Autowired
3. 基于java的配置。 JavaConfig  Spring3.0+
    a. @Configuration   @Bean   ....

##### 34.用过JavaConfig方式的spring配置吗？它是如何替代xml的？

基于Java的配置，允许你在少量的Java注解的帮助下，进行你的大部分Spring配置而非通过XML文件。
以@Configuration 注解为例，它用来标记类可以当做一个bean的定义，被Spring IOC容器使用。
另一个例子是@Bean注解，它表示此方法将要返回一个对象，作为一个bean注册进Spring应用上下文。
1 @Configuration
2 public class StudentConfig{
3  @Bean
4  public StudentBean myStudent(){
5  returnnewStudentBean();
6  }
7 }
应用：

1. 以前Xml 
    a. Spring容器 ClassPathXmlApplicationContext("xml")
    b. Spring.xml
    c. <bean scope lazy>
    d. 扫描包:   <component-scan>
    e. 引入外部属性配置文件   <property-placeHodeler  resource="xxx.properties">
    f. <property name="password" value="${mysql.password}"></property>

##### g. 指定其他配置文件：<import resource=""

2. javaconfig
    a. Spring容器：AnnotationConfigApplicationContext(javaconfig.class)
    b. 配置类  @Configuration
    c. @Bean @Scope @Lazy
    d. 扫描包: @ComponentScan
    e. 引入外部属性配置文件 @PropertySource("classpath:db.properties")

```
图灵课堂
```

```
f. @Value("${mysql.password}")
g. @Import    @Import({配置类})  使用比较灵活
```
源码：

##### 35.@Component, @Controller, @Repository, @Service 有何区别？

@Component：这将 java 类标记为 bean。它是任何 Spring 管理组件的通用构造型。spring 的组件扫描机制现在可以
将其拾取并将其拉入应用程序环境中。
@Controller：这将一个类标记为 Spring Web MVC 控制器。标有它的 Bean 会自动导入到 IoC 容器中。
@Service：此注解是组件注解的特化。它不会对 @Component 注解提供任何其他行为。您可以在服务层类中使用 
@Service 而不是 @Component，因为它以更好的方式指定了意图。
@Repository：这个注解是具有类似用途和功能的 @Component 注解的特化。它为 DAO 提供了额外的好处。它将 
DAO 导入 IoC 容器，并使未经检查的异常有资格转换为 Spring DataAccessException。

###### 36.@Import可以有几种用法？

4种：

1. 直接指定类 （如果配置类会按配置类正常解析、  如果是个普通类就会解析成Bean)
2. 通过ImportSelector 可以一次性注册多个，返回一个string[]  每一个值就是类的完整类路径
    a. 通过DeferredImportSelector可以一次性注册多个，返回一个string[]  每一个值就是类的完整类路径

##### i. 区别：DeferredImportSelector 顺序靠后

3. 通过ImportBeanDefinitionRegistrar 可以一次性注册多个，通过BeanDefinitionRegistry来动态注册BeanDefintion

##### 37.如何让自动注入没有找到依赖Bean时不报错

这个注解表明bean的属性必须在配置的时候设置，通过一个bean定义的显式的属性值或通过自动装配，若@Required
注解的bean属性未被设置，容器将抛出BeanInitializationException。示例：
1 @Autowired(required = false)
2 private Role role;

##### 38.如何让自动注入找到多个依赖Bean时不报错

```
图灵课堂
```

### 39.@Autowired 注解有什么作用

@Autowired默认是按照类型装配注入的，默认情况下它要求依赖对象必须存在（可以设置它required属性为false）。
@Autowired 注解提供了更细粒度的控制，包括在何处以及如何完成自动装配。

###### 40.@Autowired和@Resource之间的区别

@Autowired可用于：构造函数、成员变量、Setter方法
@Autowired和@Resource之间的区别
@Autowired默认是按照类型装配注入的，默认情况下它要求依赖对象必须存在（可以设置它required属性为
false）。
@Resource默认是按照名称来装配注入的，只有当找不到与名称匹配的bean才会按照类型来装配注入。

###### 41.使用@Autowired注解自动装配的过程是怎样的？

记住：@Autowired 通过Bean的后置处理器进行解析的

#### 1. 在创建一个Spring上下文的时候再构造函数中进行注册AutowiredAnnotationBeanPostProcessor

2. 在Bean的创建过程中进行解析
    1. 在实例化后预解析（解析@Autowired标注的属性、方法   比如：把属性的类型、名称、属性所在的类..... 元数据缓存起）
    2. 在属性注入真正的解析（拿到上一步缓存的元数据 去ioc容器帮进行查找，并且返回注入）
       a. 首先根据预解析的元数据拿到 类型去容器中进行查找 
       如果查询结果刚好为一个，就将该bean装配给@Autowired指定的数据；
       如果查询的结果不止一个，那么@Autowired会根据名称来查找；
       如果上述查找的结果为空，那么会抛出异常。解决方法时，使用required=false。

###### 42.配置类@Configuration的作用解析原理:

```
图灵课堂
```

1.@Configuration用来代替xml配置方式spring.xml配置文件 <bean>

2.  没有@Configuration也是可以配置@Bean
3. @Configuration加与不加有什么区别
4. 加了@Configuration会为配置类创建cglib动态代理（保证配置类@Bean方法调用Bean的单例），@Bean方法的调用就会通过容
   器.getBean进行获取
   原理：
    1.创建Spring上下文的时候会注册一个解析配置的处理器ConfigurationClassPostProcessor（实现BeanFactoryPostProcessor和
   BeanDefinitionRegistryPostProcessor)
    2.在调用invokeBeanFactoryPostProcessor，就会去调用
   ConfigurationClassPostProcessor.postProcessBeanDefinitionRegistry进行解析配置（解析配置类说白就是去解析各种注解
   (@Bean @Configuration@Import @Component ...  就是注册BeanDefinition)
    3. ConfigurationClassPostProcessor.postProcessBeanFactory去创建cglib动态代理

###### 43.@Bean之间的方法调用是怎么保证单例的？

######      （ @Configuration加与不加的区别是什么？）

1.如果希望@bean的方法返回是对象是单例  需要在类上面加上@Configuration,
2.Spring 会在invokeBeanFactoryPostProcessor  通过内置BeanFactoryPostProcessor中会CGLib生成动态代理代理
3.当@Bean方法进行互调时， 则会通过CGLIB进行增强，通过调用的方法名作为bean的名称去ioc容器中获取，进而保
证了@Bean方法的单例 

###### 44.要将一个第三方的类配成为Bean有哪些方式？

1. @Bean
2. @Import
3.通过Spring的扩展接口：BeanDefinitionRegistryPostProcessor 

###### 45、为什么@ComponentScan 不设置basePackage也会扫描？

因为Spring在解析@ComponentScan的时候 拿到basePackage  如果没有设置会将你的类所在的包的地址作为扫描包的地址

#### 五、Spring  AOP

##### 46.什么是AOP、能做什么

```
图灵课堂
```

AOP(Aspect-Oriented Programming)，一般称为面向切面编程，用于将那些与业务无关，但却对多个对象产生影响的公共行为和逻
辑，抽取并封装为一个可重用的模块，这个模块被命名为“切面”（Aspect），减少系统中的重复代码，降低了模块间的耦合度，同时提
高了系统的可维护性。
可用于权限认证、日志、事务处理等。

  AOP、OOP在字面上虽然非常类似，但却是面向不同领域的两种设计思想。OOP（面向对象编程）针对业务处理过程的实体及其属性和
行为进行抽象封装，以获得更加清晰高效的逻辑单元划分。 而AOP作为面向对象的一种补充，则是针对业务处理过程中的切面进行提取， 
已达到业务代码和公共行为代码之间低耦合性的隔离效果。这两种设计思想在目标上有着本质的差异。

##### 47.解释一下Spring AOP里面的几个名词

（1）切面（Aspect）：  在Spring Aop指定就是“切面类” ，切面类会管理着切点、通知。
（2）连接点（Join point）： 指定就是被增强的业务方法
（3）通知（Advice）：     就是需要增加到业务方法中的公共代码， 通知有很多种类型分别可以在需要增加的业务方法
不同位置进行执行（前置通知、后置通知、异常通知、返回通知、环绕通知）
（4）切点（Pointcut）：  由他决定哪些方法需要增强、哪些不需要增强，  结合切点表达式进行实现
（5）目标对象（Target Object）：  指定是增强的对象
（6）织入（Weaving） ：  spring aop用的织入方式：动态代理。  就是为目标对象创建动态代理的过程就叫织入。

##### 48.Spring通知有哪些类型？

在AOP术语中，在的某个特定的连接点上执行的动作——官方
Spring切面可以应用5种类型的通知：

1. 前置通知（Before）：在目标方法被调用之前调用通知功能；
2. 后置通知（After）：在目标方法完成之后调用通知，此时不会关心方法的输出是什么；
3. 返回通知（After-returning ）：在目标方法成功执行之后调用通知；

```
图灵课堂
```

4. 异常通知（After-throwing）：在目标方法抛出异常后调用通知；
5. 环绕通知（Around）：通知包裹了被通知的方法，在被通知的方法调用之前和调用之后执行自定义的行为。

执行顺序：

###### Spring在5.2.7之后就改变的advice 的执行顺序。 在github官网版本更新说明中有说明：如图

######  1 、正常执行：@Before­­­>方法­­­­>@AfterReturning­­­>@After

2 、异常执行：@Before­­­>方法­­­­>@AfterThrowing­­­>@After

更新说明：https://github.com/spring­projects/spring­framewor...
#25186链接：https://github.com/spring­projects/spring­framewor...

##### 49.Spring AOP and AspectJ AOP 有什么区别？

###### 关系：

```
当在Spring中要使用@Aspect、@Before.等这些注解的时候， 就需要添加AspectJ相关依赖
1 <dependency>
2  <groupId>org.aspectj</groupId>
3  <artifactId>aspectjweaver</artifactId>
4  <version>1.9.5</version>
5 </dependency>
Spring Aop提供了 AspectJ 的支持，但只用到的AspectJ的切点解析和匹配。 @Aspect、@Before.等这些注解
都是由AspectJ 发明的
```
AOP实现的关键在于 代理模式，AOP代理主要分为静态代理和动态代理。静态代理的代表为AspectJ；动态代理则以
Spring AOP为代表。

###### 区别：

（2）Spring AOP使用的动态代理，它基于动态代理来实现。默认地，如果使用接口的，用 JDK 提供的动态代理实现，
如果没有接口，使用 CGLIB 实现。 

```
图灵课堂
```

（1）AspectJ是静态代理的增强，所谓静态代理，就是AOP框架会在编译阶段生成AOP代理类，因此也称为编译时增
强，他会在编译阶段将AspectJ(切面)织入到Java字节码中，运行的时候就是增强之后的AOP对象。
属于静态织入，它是通过修改代码来实现的，它的织入时机可以是：

##### Compile-time weaving：编译期织入，如类 A 使用 AspectJ 添加了一个属性，类 B 引

##### 用了它，这个场景就需要编译期的时候就进行织入，否则没法编译类 B。

##### Post-compile weaving：编译后织入，也就是已经生成了 .class 文件，或已经打成 jar 包

##### 了，这种情况我们需要增强处理的话，就要用到编译后织入。

##### Load-time weaving：指的是在加载类的时候进行织入，要实现这个时期的织入，有几

##### 种常见的方法。1、自定义类加载器来干这个，这个应该是最容易想到的办法，在被织入类加

##### 载到 JVM 前去对它进行加载，这样就可以在加载的时候定义行为了。2、在 JVM 启动的时候

##### 指定 AspectJ 提供的 agent：-javaagent:xxx/xxx/aspectjweaver.jar。

```
AspectJ 出身也是名门，来自于 Eclipse 基金会，link：https://www.eclipse.org/aspectj
AspectJ 能干很多 Spring AOP 干不了的事情，它是 AOP 编程的完全解决方案。Spring AOP 致力于解决的
是企业级开发中最普遍的 AOP 需求（方法织入），而不是力求成为一个像 AspectJ 一样的 AOP 编程完全解决方
案。
因为 AspectJ 在实际代码运行前完成了织入，所以大家会说它生成的类是没有额外运行时开销的。
很多人会对比 Spring AOP 和 AspectJ 的性能，Spring AOP 是基于代理实现的，在容器启动的时候需要生成
代理实例，在方法调用上也会增加栈的深度，使得 Spring AOP 的性能不如 AspectJ 那么好。
```
##### 50.JDK动态代理和CGLIB动态代理的区别

Spring AOP中的动态代理主要有两种方式，JDK动态代理和CGLIB动态代理：
JDK动态代理只提供接口的代理，不支持类的代理。

##### JDK会在运行时为目标类生成一个 动态代理类$proxy*.class  . 

#####  该代理类是实现了接目标类接口， 并且代理类会实现接口所有的方法增强代码。 

##### 调用时 通过代理类先去调用处理类进行增强，再通过反射的方式进行调用目标方法。从而

##### 实现AOP

如果代理类没有实现 接口，那么Spring AOP会选择使用CGLIB来动态代理目标类。

##### CGLIB的底层是通过ASM在运行时动态的生成目标类的一个子类。（还有其他相关类，主

##### 要是为增强调用时效率） 会生成多个 ， 

##### 并且会重写父类所有的方法增强代码，

##### 调用时先通过代理类进行增强，再直接调用父类对应的方法进行调用目标方法。从而实现

##### AOP。

#### CGLIB是通过继承的方式做的动态代理，因此如果某个类被标记为

#### final，那么它是无法使用CGLIB做动态代理的。

#### CGLIB 除了生成目标子类代理类，还有一个FastClass(路由类)，可以

#### （但不是必须）让本类方法调用进行增强，而不会像jdk代理那样本类方法调

#### 用增强会失效 

```
很多人会对比  JDK和Cglib的性能，jdk动态代理生成类速度快，调用慢，cglib生成类速度慢，但后续调用
快，在老版本CGLIB的速度是JDK速度的10倍左右, 但是实际上JDK的速度在版本升级的时候每次都提高很多性能,而
CGLIB仍止步不前.
```
```
图灵课堂
```

在对JDK动态代理与CGlib动态代理的代码实验中看，1W次执行下，JDK7及8的动态代理性能比CGlib要好20%左右。

###### 51.JavaConfig方式如何启用AOP?如何强制使用cglib?

```
1
2 @EnableAspectJAutoProxy
3 //(proxyTargetClass = true) //强制CGLIB
4 //(exposeProxy = true) 在线程中暴露代理对象@EnableAspectJAutoProxy
```
###### 52.介绍AOP有几种实现方式 

```
Spring 1.2 基于接口的配置：最早的 Spring AOP 是完全基于几个接口的，想看源码的同学可以从这里起步。
Spring 2.0 schema-based 配置：Spring 2.0 以后使用 XML 的方式来配置，使用 命名空间 <aop ></aop>
Spring 2.0 @AspectJ 配置：使用注解的方式来配置，这种方式感觉是最方便的，还有，这里虽然叫
做 @AspectJ，但是这个和 AspectJ 其实没啥关系。
AspectJ  方式，这种方式其实和Spring没有关系，采用AspectJ 进行动态织入的方式实现AOP，需要用
AspectJ 单独编译。
```
###### 53.什么情况下AOP会失效,怎么解决？

失效原因： 

1. 方法是private 也会失效，解决：改成public
2. 目标类没有配置为Bean也会失效， 解决：配置为Bean
3. 切点表达式没有配置正确 
4.  ... 
内部调用不会触发AoP.

解决方式：必须走代理， 重新拿到代理对象再次执行方法才能进行增强

1. 在本类中自动注入当前的bean
2. 设置暴露当前代理对象到本地线程， 可以通过AopContext.currentProxy() 拿到当前正在调用的动态代理对象
1 @EnableAspectJAutoProxy(exposeProxy = true)

###### 54.Spring的AOP是在哪里创建的动态代理？

1. 正常的Bean会在Bean的生命周期的‘初始化’后， 通过BeanPostProcessor.postProcessAfterInitialization创建aop的动态代理
2. 还有一种特殊情况： 循环依赖的Bean会在Bean的生命周期‘属性注入’时存在的循环依赖的情况下， 也会为循环依赖的Bean
通过MergedBeanDefinitionPostProcessor.postProcessMergedBeanDefinition创建aop

###### 55.Spring的 Aop的完整实现流程？

Aop的实现大致分为三大步：JavaConfig
当@EnableAspectJAutoProxy 会通过@Import注册一个BeanPostProcessor处理AOP
1.解析切面： 在Bean创建之前的第一个Bean后置处理器会去解析切面（解析切面中通知、切点，一个通知就会解析成一
个advisor(通知、切点)） 

```
图灵课堂
```

2.创建动态代理 正常的Bean初始化后调用BeanPostProcessor  拿到之前缓存的advisor ，再通过advisor中pointcut  
判断当前Bean是否被切点表达式匹配，如果匹配，就会为Bean创建动态代理（创建方式1.jdk动态代理2.cglib)。
3.调用：拿到动态代理对象， 调用方法 就会判断当前方法是否增强的方法， 就会通过调用链的方式依次去执行通知.

##  六、Spring事务

###### 56.事务四大特性 

(1) 原子性（Atomicity）
原子性是指事务包含的所有操作要么全部成功，要么全部失败回滚， 因此事务的操作如果成功就必须要完全应用到数据库，如果操作失败则不能对数据库有
任何影响。
(2) 一致性（Consistency）
一致性是指事务必须使数据库从一个一致性状态变换到另一个一致性状态，也就是说一个事务执行之前和执行之后都必须处于一致性状态。
拿转账来说，假设用户A和用户B两者的钱加起来一共是 5000 ，那么不管A和B之间如何转账，转几次账，事务结束后两个用户的钱相加起来应该还得是
5000 ，这就是事务的一致性。
(3) 隔离性（Isolation）
隔离性是当多个用户并发访问数据库时，比如操作同一张表时，数据库为每一个用户开启的事务，不能被其他事务的操作所干扰，多个并发事务之间要相互
隔离。
即要达到这么一种效果：对于任意两个并发的事务T1和T2，在事务T1看来，T2要么在T1开始之前就已经结束，要么在T1结束之后才开始，这样每个事务都
感觉不到有其他事务在并发地执行。
关于事务的隔离性数据库提供了多种隔离级别，稍后会介绍到。
(4) 持久性（Durability）
持久性是指一个事务一旦被提交了，那么对数据库中的数据的改变就是永久性的，即便是在数据库系统遇到故障的情况下也不会丢失提交事务的操作。
例如我们在使用JDBC操作数据库时，在提交事务方法后，提示用户事务操作完成，当我们程序执行完成直到看到提示后，就可以认定事务以及正确提交，即
使这时候数据库出现了问题，也必须要将我们的事务完全执行完成，否则就会造成我们看到提示事务处理完毕，但是数据库因为故障而没有执行事务的重大错
误。

##### 57.Spring支持的事务管理类型， spring 事务实现方式有哪些？

Spring支持两种类型的事务管理：
编程式事务管理：这意味你通过编程的方式管理事务，给你带来极大的灵活性，但是难维护。
声明式事务管理：这意味着你可以将业务代码和事务管理分离，你只需用注解和XML配置来管理事务。

实现声明式事务的三种方式：

1. 基于接口
    a. 基于 TransactionInterceptor 的声明式事务: Spring 声明式事务的基础，通常也不建议使用这种方式，但是与aop
    一样，了解这种方式对理解 Spring 声明式事务有很大作用。
    b. 基于 TransactionProxyFactoryBean 的声明式事务: 第一种方式的改进版本，简化的配置文件的书写，这是
    Spring 早期推荐的声明式事务管理方式，但是在 Spring 2.0 中已经不推荐了。
2. 基于< tx> 和< aop>命名空间的声明式事务管理： 目前推荐的方式，其最大特点是与 Spring AOP 结合紧密，可以充分利用切点表达式的强大支
持，使得管理事务更加灵活。

```
图灵课堂
```

3. 基于 @Transactional 的全注解方式： 将声明式事务管理简化到了极致。开发人员只需在配置文件中加上一行启用相关后处理 Bean 的配置，然后
在需要实施事务管理的方法或者类上使用 @Transactional 指定事务规则即可实现事务管理，而且功能也不必其他方式逊色。

### 58、说一下Spring的事务传播行为

事务的传播特性指的是当一个事务方法被另一个事务方法调用时，这个事务方法应该如何进行？

```
1 @Transactional
2 public void trans(){
3  sub();
4  log();  // 记录流水 数据库操作
5  query();
6 }
7
8 @Transactional(SUPPORTS)
9 public info query(){
10  ....
11 }
12 @Transactional REQUIRES_NEW
13 public void log(){
14  // todo: 记录日志
15 }
```
```
事务传播行为类型 外部不存在事务 外部存在事务 使用方式
REQUIRED（默认） 开启新的事务 融合到外部事务中 @Transactional(propagation = Propagation.REQUIRED)适用增删改查
SUPPORTS 不开启新的事务 融合到外部事务中 @Transactional(propagation = Propagation.SUPPORTS)适用查询
REQUIRES_NEW 开启新的事务 不用外部事务，创建新的事务 @Transactional(propagation = Propagation.REQUIRES_NEW)适用内部事务和外部事务不存在业务关联情况，如日志
NOT_SUPPORTED 不开启新的事务 不用外部事务 @Transactional(propagation = Propagation.NOT_SUPPORTED)不常用
NEVER  不开启新的事务 抛出异常 @Transactional(propagation = Propagation.NEVER )不常用
MANDATORY 抛出异常 融合到外部事务中 @Transactional(propagation = Propagation.MANDATORY)不常用
NESTED 开启新的事务 融合到外部事务中,SavePoint机制，外层影响内层， 内层不会影响外层 @Transactional(propagation = Propagation.NESTED)不常用
```
##### 59.说一下 spring 的事务隔离？

用来解决并发事务所产生一些问题： 
并发会产生什么问题？
1.脏读
2.不可重复度
3.幻影读

概念： 通过设置隔离级别可解决在并发过程中产生的那些问题：
 1.脏读
事务1  begin 事务2  begin
update t_user set balance=800
where id=1;

```
图灵课堂
```

```
#balance=800
select * from  t_user where id=1
commit#balance=800; 
rollback;  #回滚#balance=1000
```
1. 一个事务，读取了另一个事务中没有提交的数据，会在本事务中产生的数据不一致的问题
解决方式：@Transactional(isolation = Isolation.READ_COMMITTED)
读已提交：READ COMMITTED
要求Transaction01只能读取Transaction02已提交的修改。
    2.不可重复度
事务1  begin 事务2  begin
select * #balance=1000from  t_user where id=1
updateset balance=800 t_user 
where id=1;
commit#balance=800; 
select * #balance=800from t_user where id=1
commit; 
一个事务中，多次读取相同的数据， 但是读取的结果不一样，  会在本事务中产生数据不一致的问题。
解决方式：@Transactional(isolation = Isolation.REPEATABLE_READ)
可重复读：REPEATABLE READ
确保Transaction01可以多次从一个字段中读取到相同的值，即Transaction01执行期间禁止其它事务对这个字段进行更
新。(行锁）
    3.幻影读
事务1  begin 事务2  begin
select sum(balance) from  t_user where id=1
#balance=3000 INSERT INTO  t_user
VALUES (
'4','赵六',
'123456784','1000'

(^) commit); ; 
select sum(balance) from  t_user where id=1
#balance=4000
commit; 
一个事务中，多次对数据进行整表数据读取（统计），但是结果不一样， 会在本事务中产生数据不一致的问题。
解决方式：@Transactional(isolation = Isolation.SERIALIZABLE)
串行化：SERIALIZABLE
确保Transaction01可以多次从一个表中读取到相同的行，在Transaction01执行期间，禁止其它事务对这个表进行添加、更
新、删除操作。可以避免任何并发问题，但性能十分低下。（表锁）
很多人容易搞混不可重复读和幻读，确实这两者有些相似：
 对于前者,   只需要锁行
 对于后者,   需要锁表 
图灵课堂


```
1 并发安全：SERIALIZABLE>REPEATABLE_READ>READ_COMMITTED
2 运行效率：READ_COMMITTED>REPEATABLE_READ>SERIALIZABLE
```
 当不设置事务隔离级别将使用数据库的默认事务隔离级别：
1 #MYSQL：REPEATABLE‐READ
2 SELECT @@tx_isolation;
3 #ORACLE: READ_COMMITTED
4 SELECT s.sid, s.serial#,
5 CASE BITAND(t.flag, POWER(2,  28))
6 WHEN 0 THEN 'READ COMMITTED'
7 ELSE 'SERIALIZABLE'
8 END AS isolation_level
9 FROM v$transaction t
10 JOIN v$session s ON t.addr = s.taddr AND s.sid = sys_context('USERENV',  'SID');

###### 60.Spring事务实现基本原理   

使用： 
1 @EnableTransactionManagement
原理：
1.解析切面 ——>   bean的创建前第一个bean的后置处理器进行解析advisor(pointcut(通过@Transacational解析的切点) ， advise)  
(这个advisor 是通过@EnableTransactionManagement注册了一个配置类，该配置类就配置了adivsor)
2.创建动态代理——>  bean的初始化后调用bean的后置处理器进行创建动态代理(有接口使用jdk，没接口使用cglib)，  创建动态代理之
前会先根据advisor中pointCut 匹配@Transacational( 方法里面是不是有、类上面是不是有、接口或父类上面是不是有 )   ， 匹配到就创
建动态代理。
3.调用：  动态代理
try{
4.创建一个数据库连接Connection, 并且修改数据库连接的autoCommit属性为false，禁止此连接的自动提交，这是
实现Spring事务非常重要的一步
5.然后执行目标方法方法，方法中会执行数据库操作sql 
}
catch{
6.如果出现了异常，并且这个异常是需要回滚的就会回滚事务，否则仍然提交事务
}
7.执行完当前方法后，如果没有出现异常就直接提交事务

###### 61. Spring事务传播行为实现原理：

```
图灵课堂
```

2.Spring的事务信息是存在ThreadLocal中的， 所以一个线程永远只能有一个事务，  
融入：当传播行为是融入外部事务则拿到ThreadLocal中的Connection、共享一个数据库连接共同提交、回
滚； 
创建新事务：当传播行为是创建新事务，会将嵌套新事务存入ThreadLocal、再将外部事务暂存起来；  当嵌套
事务提交、回滚后，会将暂存的事务信息恢复到ThreadLocal中
 调用：融入
1
2 try{
3  3.内嵌：判断ThreadLocal是否已经有Connection，有的话就说明是一个内嵌事务， 判断当前事务的传播行为
4 融入：不会创建Connection，返回事务状态信息(TransactionInfo.newTransaction=false)

(^5) nInfo. 1 外部newTransaction.创建一个数据库连接=true)Connection存入ThreadLocal, 并且修改数据库连接的autoCommit属性为false ，返回事务状态信息(Transactio
6 2 外部.然后执行目标方法方法（调用了内部事务方法）方法中会执行数据库操作sql
7  4.内嵌：然后执行目标方法方法方法中会执行数据库操作sql
8 }
9 catch{
10 如果出现了异常，并且这个异常是需要回滚的就会回滚事务，否则仍然提交事务
11 }
12  5 内嵌：判断newTransaction==true 就提交事务
13 6.外部： 判断newTransaction==true 拿到ThreadLocal中的Connection进行提交
14
15
调用：创建新事务
1
2 try{
3  3.内嵌：判断ThreadLocal是否已经有Connection，有的话就说明是一个内嵌事务， 判断当前事务的传播行为
(^4) 置空 创建新事务：会把外层事务相关的事务信息（) Connection、隔离级别、是否只读....暂存同时会把外层事务的ThreadLocal存储的事务信息都
5 创建Connection放入ThreadLocal，返回事务状态信息(TransactionInfo.newTransaction=ture,TransactionInfo.外部事务的信息暂存)
(^6) nInfo. 1 外部newTransaction.创建一个数据库连接=true)Connection存入ThreadLocal, 并且修改数据库连接的autoCommit属性为false ，返回事务状态信息(Transactio
7 2 外部.然后执行目标方法方法（调用了内部事务方法）方法中会执行数据库操作sql
8  4.内嵌：然后执行目标方法方法方法中会执行数据库操作sql
9 }
10 catch{
11 如果出现了异常，并且这个异常是需要回滚的就会回滚事务，否则仍然提交事务
12 }
13  5 内嵌：判断newTransaction==true 就提交事务， 判断是否暂存事务， 把暂存的事务信息回归ThreadLocal中
14 6.外部： 判断newTransaction==true 拿到ThreadLocal中的Connection进行提交
15
16

###### 62.Spring多线程事务 能否保证事务的一致性（同时提交、同时回滚）？

1.Spring的事务信息是存在ThreadLocal中的Connection， 所以一个线程永远只能有一个事务

2. 所以Spring 的事务是无法实现事务一致性的
3. 可以通过编程式事务，或者通过分布式事务的思路:二阶段提交方式

```
图灵课堂
```

###### 63.Spring事务的失效原因？

 失效原因： 

1. 方法是private 也会失效，解决：改成public
2. 目标类没有配置为Bean也会失效  解决：配置为Bean
3. 自己捕获了异常  解决：不要捕获处理
4. 使用cglib动态代理，但是@Transactional声明在接口上面
5.  ... 
内部调用导致事务传播失效.

解决方式：必须走代理， 重新拿到代理对象再次执行方法才能进行增强

3. 在本类中自动注入当前的bean
4. 设置暴露当前代理对象到本地线程， 可以通过AopContext.currentProxy() 拿到当前正在调用的动态代理对象
1 @EnableAspectJAutoProxy(exposeProxy = true)

### 七、Spring其他

###### 64.Spring事件监听的核心机制是什么？

##### 原理：观察者模式

##### 支持异步：

##### 异步发布事件的核心机制？   多线程

spring的事件监听有三个部分组成：

##### 事件（ApplicationEvent)  负责对应相应监听器 事件源发生某事件是特定事件监听器

##### 被触发的原因。

##### 监听器(ApplicationListener) 对应于观察者模式中的观察者。监听器监听特定事件,并

##### 在内部定义了事件发生后的响应逻辑。

##### 事件发布器（ApplicationEventMulticaster ）对应于观察者模式中的被观察者/主

##### 题，  负责通知观察者 对外提供发布事件和增删事件监听器的接口,维护事件和事件监听

##### 器之间的映射关系,并在事件发生时负责通知相关监听器。

```
图灵课堂
```

Spring事件机制是观察者模式的一种实现，但是除了发布者和监听者者两个角色之外，还有一个EventMultiCaster的角
色负责把事件转发给监听者，工作流程如下：

```
图灵课堂
```

```
Spring事件机制
```
也就是说上面代码中发布者调用applicationEventPublisher.publishEvent(msg); 是会将事件发送给了EventMultiCaster， 而后由
EventMultiCaster注册着所有的Listener，然后根据事件类型决定转发给那个Listener。

######  65.Spring 框架中都用到了哪些设计模式？

###### 66.Spring是如何整合MyBatis将Mapper接口注册为Bean的原理？

1. 首先MyBatis的Mapper接口核心是JDK动态代理
2. Spring会排除接口，无法注册到IOC容器中
3. MyBatis 实现了BeanDefinitionRegistryPostProcessor 可以动态注册BeanDefinition
4. 需要自定义扫描器（继承Spring内部扫描器ClassPathBeanDefinitionScanner )  重写排除接口的方法
（isCandidateComponent）
5. 但是接口虽然注册成了BeanDefinition但是无法实例化Bean  因为接口无法实例化

```
图灵课堂
```

6. 需要将BeanDefinition的BeanClass  替换成JDK动态代理的实例（偷天换日）
7. Mybatis 通过FactoryBean的工厂方法设计模式可以自由控制Bean的实例化过程，可以在getObject方法中创建JDK动态代理

## 八、SpringMVC 

###### 67.说说你是如何解决 get 和 post 乱码问题？

（ 1 ）解决post请求乱码问题：在web.xml中配置一个CharacterEncodingFilter过滤器，设置成utf­8；

1 <filter>
2 <filter‐name>CharacterEncodingFilter</filter‐name>
3 <filter‐class>org.springframework.web.filter.CharacterEncodingFilter</filter‐class>
4 <init‐param>
5 <param‐name>encoding</param‐name>
6 <param‐value>utf‐8</param‐value>
7 </init‐param>
8 </filter>
9
10 <filter‐mapping>
11 <filter‐name>CharacterEncodingFilter</filter‐name>
12 <url‐pattern>/*</url‐pattern>
13 </filter‐mapping>
（ 2 ）get请求中文参数出现乱码解决方法有两个：

①修改tomcat配置文件添加编码与工程编码一致，如下：
1 <ConnectorURIEncoding="utf‐8" connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443"/>
②另外一种方法对参数进行重新编码：
1 String userName = new String(request.getParamter("userName").getBytes("ISO8859‐1"), "utf‐8")
ISO8859­1是tomcat默认编码，需要将tomcat编码后的内容按utf­8编码。

###### 68.Spring MVC的控制器是不是单例模式,如果是,有什么问题,怎么解决？

```
图灵课堂
```

答：是单例模式,所以在多线程访问的时候有线程安全问题,不要用同步,会影响性能的,解决方案是在控制器里面不能写字
段。 

###### 69.请描述Spring MVC的工作流程？描述一下 DispatcherServlet 的工作流程？

（1）用户发送请求至前端控制器DispatcherServlet；
（2） DispatcherServlet收到请求后，调用HandlerMapping处理器映射器，请求获取Handle；
（3）处理器映射器根据请求url找到具体的处理器，生成处理器对象及处理器拦截器(如果有则生成)一并返回给
DispatcherServlet；
（4）DispatcherServlet 调用 HandlerAdapter处理器适配器；
（5）HandlerAdapter 经过适配调用 具体处理器(Handler，也叫后端控制器)；
（6）Handler执行完成返回ModelAndView；
（7）HandlerAdapter将Handler执行结果ModelAndView返回给DispatcherServlet；
（8）DispatcherServlet将ModelAndView传给ViewResolver视图解析器进行解析；
（9）ViewResolver解析后返回具体View；
（10）DispatcherServlet对View进行渲染视图（即将模型数据填充至视图中）
（11）DispatcherServlet响应用户。

###### 70.SpringMvc怎么和AJAX相互调用的？

（1）加入Jackson.jar
（2）在配置文件中配置json的消息转换器.(jackson不需要该配置HttpMessageConverter）
1 <!‐‐它就帮我们配置了默认json映射‐‐>
2 <mvc:annotation‐driven conversion‐service="conversionService" ></mvc:annotation‐driven>
3
（3）在接受Ajax方法里面可以直接返回Object,List等,但方法前面要加上@ResponseBody注解。

```
图灵课堂
```

springMVC对数据Message的处理操作提供了一个接口HttpMessageConverter，用来对参数值和返回值的转换处理。
在请求和返回过程中可以进行转换json

###### 71.Spring和SpringMVC为什么需要父子容器？

就功能性来说不用子父容器也可以完成（参考：SpringBoot就没用子父容器）

1. 所以父子容器的主要作用应该是划分框架边界。有点单一职责的味道。service、dao层我们一般使用spring框架
来管理、controller层交给springmvc管理
2. 规范整体架构 使 父容器service无法访问子容器controller、子容器controller可以访问父容器 service
3. 方便子容器的切换。如果现在我们想把web层从spring mvc替换成struts，那么只需要将spring­mvc.xml替换成
Struts的配置文件struts.xml即可，而spring­core.xml不需要改变。
4. 为了节省重复bean创建

###### 72.是否可以把所有Bean都通过Spring容器来管理？（Spring的applicationContext.xml中配置全局扫

###### 描)

不可以，这样会导致我们请求接口的时候产生 404 。 如果所有的Bean都交给父容器，SpringMVC在初始化HandlerMethods的时
候（initHandlerMethods）无法根据Controller的handler方法注册HandlerMethod，并没有去查找父容器的bean；
也就无法根据请求URI 获取到 HandlerMethod来进行匹配.

###### 73.是否可以把我们所需的Bean都放入Spring­mvc子容器里面来管理（springmvc的spring­

###### servlet.xml中配置全局扫描）?

可以 ， 因为父容器的体现无非是为了获取子容器不包含的bean,  如果全部包含在子容器完全用不到父容器了，  所以是可以全部放在
springmvc子容器来管理的。
虽然可以这么做不过一般应该是不推荐这么去做的，一般人也不会这么干的。如果你的项目里有用到事物、或者aop记得也需要把
这部分配置需要放到Spring-mvc子容器的配置文件来，不然一部分内容在子容器和一部分内容在父容器,可能就会导致
你的事物或者AOP不生效。     所以如果aop或事物如果不生效也有可能是通过父容器(spring)去增强子容器
(Springmvc)，也就无法增强。

###### 74.如何实现无XML零配置的SpringMVC

1. 省略web.xml
    a. servlet3.0之后规范中提供了SPI扩展:META-INF/services/javax.servlet.ServletContainerInitializer

```
图灵课堂
```

```
b. SpringMVC通过实现ServletContainerInitializer接口
c. 动态注册ContextLoaderListener 和DispatcherServlet并创建子父容器(Application)
```
2. 省略spring.xml和spring-mvc.xml(只是sprinmvc方式 ，springboot在自动配置类完成)   配置类--xml
    a. 实现一个继承AbstractAnnotationConfigDispatcherServletInitializer的类
    b. 该类就实现了ServletContainerInitializer，它会创建ContextLoaderListener 和DispatcherServlet
    c. 还会创建父子容器， 创建容器时传入父子容器配置类则可以替代spring.xml和spring-mvc.xml

###### 75.SpringMVC的拦截器和过滤器有什么区别？执行顺序？

拦截器不依赖与servlet容器，过滤器依赖与servlet容器。
拦截器只能对action请求(DispatcherServlet 映射的请求)起作用，而过滤器则可以对几乎所有的请求起作用。
拦截器可以访问容器中的Bean(DI)，而过滤器不能访问（基于spring注册的过滤器也可以访问容器中的bean）。

执行顺序：

多个过滤器的执行顺序跟xml文件中定义的先后关系有关
当然，对于多个拦截器它们之间的执行顺序跟在SpringMVC的配置文件中定义的先后顺序有关。

## 九、Spring Boot

```
图灵课堂
```

###### 76.谈谈你对SpringBoot的理解，它有哪些特性（优点）？

SpringBoot的用来快速开发Spring应用的一个脚手架、其设计目的是用来简新Spring应用的初始搭建以及开发过程。
1.SpringBoot提供了很多内置的Starter结合自动配置，对主流框架无配置集成、开箱即用。
2.SpringBoot简化了开发，采用JavaConfig的方式可以使用零xml的方式进行开发；
2.SpringBoot内置Web容器无需依赖外部Web服务器，省略了Web.xml，直接运行jar文件就可以启动web应用；
4.SpringBoot帮我管理了常用的第三方依赖的版本，减少出现版本冲突的问题；
5.SpringBoot自带了监控功能，可以监控应用程序的运行状况，或者内存、线程池、Http 请求统计等，同时还提供了优雅关闭应用程
序等功能。

###### 77.Spring和SpringBoot的关系和区别？

 SpringBoot是Spring生态的产品。
Spring Framework是一个容器框架
SpringBoot 它不是一个框架、它是一个可以快速构建基于Spring的脚手架(里面包含了Spring和各种框架），为开发Spring生态其他框架
铺平道路
2个不是一个层面的东西， 没有可比性。

###### 78.SpringBoot的核心注解

1. @SpringBootApplication注解：这个注解标识了一个SpringBoot工程，它实际上是另外三个注解的组合，这三个注解是：
2. @SpringBootConfiguration：这个注解实际就是一个@Configuration，表示启动类也是一个配置类
3. @EnableAutoConfiguration：向Spring容器中导入了一个Selector，用来加载ClassPath下SpringFactories中所定义的自动配
置类，将这些自动加载为配置Bean  
4.  @Conditional 也很关键， 如果没有它我们无法在自定义应用中进行定制开发

###### @ConditionalOnBean、 

###### @ConditionalOnClass、

###### @ConditionalOnExpression、

###### @ConditionalOnMissingBean等。

###### 79.springboot的自动配置原理？

1.通过@SpringBootConfiguration 引入了@EnableAutoConfiguration (负责启动自动配置功能）
2.@EnableAutoConfiguration 引入了@Import
3.Spring容器启动时：加载Ioc容器时会解析@Import 注解
4.@Import导入了一个deferredImportSelector(它会使SpringBoot的自动配置类的顺序在最后，这样方便我们扩展和覆盖？)
5.然后读取所有的/META-INF/spring.factories文件（SPI)
6.过滤出所有AutoConfigurtionClass类型的类
7.最后通过@ConditioOnXXX排除无效的自动配置类

```
图灵课堂
```

###### 80.为什么SpringBoot的jar可以直接运行？

1.SpringBoot提供了一个插件spring-boot-maven-plugin用于把程序打包成一个可执行的jar包。
2.Spring Boot应用打包之后，生成一个Fat jar(jar包中包含jar)，包含了应用依赖的jar包和Spring Boot loader相关的
类。
3.java -jar会去找jar中的manifest文件，在那里面找到真正的启动类（Main-Class）；
4.Fat jar的启动Main函数是JarLauncher，它负责创建一个LaunchedURLClassLoader来加载boot-lib下面的jar，并以
一个新线程启动应用的启动类的Main函数（找到manifest中的Start-Class）。

###### 81.SpringBoot的启动原理？

1.运行main方法： 初始化new SpringApplication  从spring.factories  读取 listener  ApplicationContextInitializer   。

2.运行run方法
3.读取 环境变量   配置信息.....

4. 创建springApplication上下文:ServletWebServerApplicationContext
5. 预初始化上下文 ： 将启动类作为配置类进行读取-->将配置注册为BeanDefinition
6.调用refresh 加载ioc容器 
    invokeBeanFactoryPostProcessor -- 解析@Import:  加载所有的自动配置类
    onRefresh  创建(内置)servlet容器
7.在这个过程中springboot会调用很多监听器对外进行扩展

```
图灵课堂
```

###### 82.SpringBoot内置Tomcat启动原理？

```
 当依赖Spring-boot-starter-web依赖时会在SpringBoot中添加：ServletWebServerFactoryAutoConfiguration
servlet容器自动配置类
该自动配置类通过@Import导入了可用(通过@ConditionalOnClass判断决定使用哪一个)的一个Web容器工厂（默认Tomcat)
在内嵌Tomcat类中配置了一个TomcatServletWebServerFactory的Bean（Web容器工厂）
它会在SpringBoot启动时 加载ioc容器(refresh)   OnRefersh  创建内嵌的Tomcat并启动 
```
```
图灵课堂
```

###### 83.SpringBoot外置Tomcat启动原理？

```
1 public class TomcatStartSpringBoot extends SpringBootServletInitializer {
2  @Override
3  protected SpringApplicationBuilder (SpringApplicationBuilder builder) {
4  return builder.sources(Application.class);
5  }
6 }
```
```
图灵课堂
```

servlet3.0 规范官方文档： 8.2.4

大概：  当servlet容器启动时候  就会去META-INF/services  文件夹中找到javax.servlet.ServletContainerInitializer,      这个文件里面肯
定绑定一个ServletContainerInitializer.   当servlet容器启动时候就会去该文件中找到ServletContainerInitializer的实现类，从而创建它
的实例调用onstartUp
@HandlesTypes(WebApplicationInitializer.class).
@HandlesTypes传入的类为ServletContainerInitializer感兴趣的
容器会自动在classpath中找到 WebApplicationInitializer   会传入到onStartup方法的
webAppInitializerClasses中
Set<Class<?>> webAppInitializerClasses 这里面也包括之前定义的TomcatStartSpringBoot 
1
2 @HandlesTypes(WebApplicationInitializer.class)
3 public class SpringServletContainerInitializer implements ServletContainerInitializer {
1 @Override
2 public void onStartup(@Nullable Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
3  throws ServletException {
4
5  List<WebApplicationInitializer> initializers = new LinkedList<>();
6
7  if (webAppInitializerClasses != null) {
8  for (Class<?> waiClass : webAppInitializerClasses) {
9  // 如果不是接口 不是抽象 跟WebApplicationInitializer有关系 就会实例化
10  if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) &&
11  WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
12  try {
13  initializers.add((WebApplicationInitializer)
14  ReflectionUtils.accessibleConstructor(waiClass).newInstance());
15  }
16  catch (Throwable ex) {
17  throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
18  }
19  }
20  }
21  }
22 
23  if (initializers.isEmpty()) {
24  servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
25  return;

```
图灵课堂
```

26  }
27 

(^28) classpath"); servletContext.log(initializers.size() + " Spring WebApplicationInitializers detected on
29  // 排序
30  AnnotationAwareOrderComparator.sort(initializers);
31  for (WebApplicationInitializer initializer : initializers) {
32  initializer.onStartup(servletContext);
33  }
34 }
1 @Override
2 public void onStartup(ServletContext servletContext) throws ServletException {
3  // Logger initialization is deferred in case an ordered
4  // LogServletContextInitializer is being used
5  this.logger = LogFactory.getLog(getClass());
6  WebApplicationContext rootApplicationContext = createRootApplicationContext(servletContext);
7  if (rootApplicationContext != null) {
(^8) servletContext)); servletContext.addListener(new SpringBootContextLoaderListener(rootApplicationContext,
9  }
10  else {
11  this.logger.debug("No ContextLoaderListener registered, as createRootApplicationContext() did not "
12  + "return an application context");
13  }
14 }
SpringBootServletInitializer
之前定义的TomcatStartSpringBoot 就是继承它
1 protected WebApplicationContext createRootApplicationContext(ServletContext servletContext) {
2  SpringApplicationBuilder builder = createSpringApplicationBuilder();
3  builder.main(getClass());
4  ApplicationContext parent = getExistingRootWebApplicationContext(servletContext);
5  if (parent != null) {
6  this.logger.info("Root context already created (using as parent).");
7  servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, null);
8  builder.initializers(new ParentContextApplicationContextInitializer(parent));
9  }
10  builder.initializers(new ServletContextApplicationContextInitializer(servletContext));
11  builder.contextClass(AnnotationConfigServletWebServerApplicationContext.class);
12  // 调用configure
13  builder = configure(builder);
图灵课堂


```
14  builder.listeners(new WebEnvironmentPropertySourceInitializer(servletContext));
15  SpringApplication application = builder.build();
16  if (application.getAllSources().isEmpty()
```
(^17) {  && MergedAnnotations.from(getClass(), SearchStrategy.TYPE_HIERARCHY).isPresent(Configuration.class))
18  application.addPrimarySources(Collections.singleton(getClass()));
19  }
20  Assert.state(!application.getAllSources().isEmpty(),
21  "No SpringApplication sources have been defined. Either override the "
22  + "configure method or add an @Configuration annotation");
23  // Ensure error pages are registered
24  if (this.registerErrorPageFilter) {
25  application.addPrimarySources(Collections.singleton(ErrorPageFilterConfiguration.class));
26  }
27  application.setRegisterShutdownHook(false);
28  return run(application);
29 }
当调用configure就会来到TomcatStartSpringBoot .configure
将Springboot启动类传入到builder.source
1 @Override
2 protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
3  return builder.sources(Application.class);
4 }
// 调用SpringApplication application = builder.build();  就会根据传入的Springboot启动类来构建一个SpringApplication 
1 public SpringApplication build(String... args) {
2  configureAsChildIfNecessary(args);
3  this.application.addPrimarySources(this.sources);
4  return this.application;
5 }
//   调用 return run(application);   就会帮我启动springboot应用
1 protected WebApplicationContext run(SpringApplication application) {
2  return (WebApplicationContext) application.run();
3 }
它就相当于我们的
1 public static void main(String[] args) {
2  SpringApplication.run(Application.class, args);
3 }

###### 84.会不会SpringBoot自定义Starter？大概实现过程？

#### 2. HelloProperties

```
1 package com.starter.tulingxueyuan;
2
3 import org.springframework.boot.context.properties.ConfigurationProperties;
4
5 /***
6  * @Author 徐庶 QQ:1092002729
7  * @Slogan 致敬大师，致敬未来的你
8  */
```
```
图灵课堂
```

```
9 @ConfigurationProperties("tuling.hello")
10 public class HelloProperties {
11  private String name;
12 
13  public String getName() {
14  return name;
15  }
16 
17  public void setName(String name) {
18  this.name = name;
19  }
20 }
21 
```
#### 3. IndexController 

```
1 package com.starter.tulingxueyuan;
2
3 import org.springframework.beans.factory.annotation.Autowired;
4 import org.springframework.web.bind.annotation.RequestMapping;
5 import org.springframework.web.bind.annotation.RestController;
6
7 /***
8  * @Author 徐庶 QQ:1092002729
9  * @Slogan 致敬大师，致敬未来的你
10  */
11 @RestController
12 public class IndexController {
13 
14  HelloProperties helloProperties;
15 
16  public IndexController(HelloProperties helloProperties) {
17  this.helloProperties=helloProperties;
18  }
19 
20  @RequestMapping("/")
21  public String index(){
22  return helloProperties.getName()+"欢迎您";
23  }
24 
25 }
26 
```
#### 4. HelloAutoConfitguration

```
1 package com.starter.tulingxueyuan;
2
3 import org.springframework.beans.factory.annotation.Autowired;
4 import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
5 import org.springframework.boot.context.properties.EnableConfigurationProperties;
6 import org.springframework.context.annotation.Bean;
7 import org.springframework.context.annotation.Configuration;
8
9 /***
10  * @Author 徐庶 QQ:1092002729
```
```
图灵课堂
```

```
11  * @Slogan 致敬大师，致敬未来的你
12  *
13  * 给web应用自动添加一个首页
14  */
15 @Configuration
16 @ConditionalOnProperty(value = "tuling.hello.name")
17 @EnableConfigurationProperties(HelloProperties.class)
18 public class HelloAutoConfitguration {
19 
20  @Autowired
21  HelloProperties helloProperties;
22 
23  @Bean
24  public IndexController indexController(){
25  return new IndexController(helloProperties);
26  }
27 }
28 
```
#### 5. spring.factories

在 resources 下创建文件夹 META-INF 并在 META-INF 下创建文件 spring.factories ，内容如下：

1
2 org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
3  com.starter.tulingxueyuan.HelloAutoConfitguration
到这儿，我们的配置自定义的starter就写完了 ，我们hello-spring-boot-starter-autoconfigurer、hello-spring-boot-
starter 安装成本地jar包。

###### 85.SpringBoot读取配置文件的原理是什么？加载顺序是怎样的?

#### 通过事件监听的方式读取的配置文件：ConfigFileApplicationListener

优先级从高到低，高优先级的配置覆盖低优先级的配置，所有配置会形成互补配置。
1 * <ul>
2 * <li>file:./config/</li>
3 * <li>file:./config/xxx/application.properties</li>
4 * <li>file:./application.properties</li>
5 * <li>classpath:config/</li>
6 * <li>classpath:</li>
7 * </ul>

```
图灵课堂
```

###### 86.SpringBoot的默认日志实现框架是什么？怎么切换成别的？

总结：

1. SpringBoot底层也是使用slf4j+logback的方式进行日志记录
    a. logback桥接：logback-classic
2. SpringBoot也把其他的日志都替换成了slf4j；
    a. log4j 适配： log4j-over-slf4j   
    b. jul适配：jul-to-slf4j 
    c. 这两个适配器都是为了适配Spring的默认日志：jc

###### 切换日志框架

```
将 logback切换成log4j2 
```
1. 将logback的场景启动器排除（slf4j只能运行有 1 个桥接器）
2. 添加log4j2的场景启动器
3. 添加log4j2的配置文件
1 <dependencies>
2  <dependency>
3  <!‐‐starter‐web里面自动添加starter‐logging 也就是logback的依赖‐‐>
4  <groupId>org.springframework.boot</groupId>
5  <artifactId>spring‐boot‐starter‐web</artifactId>
6  <exclusions>
7  <!‐‐排除starter‐logging 也就是logback的依赖‐‐>
8  <exclusion>
9  <artifactId>spring‐boot‐starter‐logging</artifactId>
10  <groupId>org.springframework.boot</groupId>
11  </exclusion>
12  </exclusions>
13  </dependency>
14 
15  <!‐‐Log4j2的场景启动器‐‐>
16  <dependency>
17  <groupId>org.springframework.boot</groupId>
18  <artifactId>spring‐boot‐starter‐log4j2</artifactId>
19  </dependency>
20 </dependencies>
将 logback切换成log4j  
1. 要将logback的桥接器排除

```
图灵课堂
```

```
1 <dependency>
2  <!‐‐starter‐web里面自动添加starter‐logging 也就是logback的依赖‐‐>
3  <groupId>org.springframework.boot</groupId>
4  <artifactId>spring‐boot‐starter‐web</artifactId>
5  <exclusions>
6  <exclusion>
7  <artifactId>logback‐classic</artifactId>
8  <groupId>ch.qos.logback</groupId>
9  </exclusion>
10  </exclusions>
11 </dependency>
```
2. 添加log4j的桥接器
1 <dependency>
2  <groupId>org.slf4j</groupId>
3  <artifactId>slf4j‐log4j12</artifactId>
4 </dependency>
3. 添加log4j的配置文件
log4j.properties
1 #trace<debug<info<warn<error<fatal
2 log4j.rootLogger=trace, stdout
3 log4j.appender.stdout=org.apache.log4j.ConsoleAppender
4 log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
5 log4j.appender.stdout.layout.ConversionPattern=%d %p [%c] ‐ %m%n

###### 87.说说你在开发的时候怎么在SpringBoot的基础上做扩展？

首先肯定要确认你扩展的技术点（比如扩展的是aop)   
打开aop自动配置类:   

#### 重点关注@ConditionalOnXXX 它可以帮助开启或关闭 某些功能

```
深入看源码  有些自动配置类 提供对外的扩展接口、实现接口也可以进行扩展
```
## 十、微服务

###### 88、微服务架构的优缺点

1. 演变而来（从单体应用演变过来）
2. 初期评估起手就上微服务

###### 分工协作

```
单体：影响开发效率,发布和迭代性差；项目启动慢，    每个人对整体的项目都要有所把握；  业务缩减后如果
语言不一致开发人员面临流失。
拆分：提高开发效率和敏捷性ꞏ；单个服务启动快， 专人处理专事专注自己的服务；  充分利用项目开发人员
（哪怕是不同的语言不同框架，不同存储技术，也可以）
```
###### 并发能力

```
单体：整体集群，易造成系统资源浪费；   之前下单功能要去集群无法准确评测最大并发量， 因为所有的
功能都在一起，无法准确预估扩容的服务器。
```
```
图灵课堂
```

```
拆分：服务集群，充分利用服务器资源；   现在只需要针对下单服务进行压测就可以得到，下单功能具体
能承受的并发量最高水位，从而更准确的进行扩容。
```
###### 维护能力（维护困难）

```
单体：随着业务量增加，应用慢慢膨胀，后续可能会变得牵一发而动全身，难以维护。
拆分：根据功能垂直拆分，责任更加分明，维护更加精准。
```
###### 容错

###### 单体：单点故障，一个功能OOM导致整个应用都不可用

###### 拆分：弱依赖的服务出现故障，可以进行熔断（隔离） 依然不影响主业务正常使用

###### 扩展 

###### 单体：难以技术升级

###### 拆分：新的服务采用任意新技术（技术多样性）

###### 缺点

分布式
分布式系统较难编程，因为远程调用速度很慢，并且总是面临失败的风险。对于开发人员的技术要求更高
最终一致性
对于分布式系统而言，保持强一致性非常困难，这意味着每个人都必须管理最终一致性。
运维复杂性 
微服务必定带来开发、上线、运维的复杂度的提高，如果说单体应用复杂度为  10 ，实施了微服务后的复杂度将是  100 ，
配备了相应的工具和平台后，可以将复杂度降低到  50 ，但仍然比单体复杂的多。
隐式接口
服务和服务之间通过接口来“联系”，当某一个服务更改接口格式时，可能涉及到此接口的所有服务都需要做调整。
重复劳动
在很多服务中可能都会使用到同一个功能，而这一功能点没有足够大到提供一个服务的程度，这个时候可能不同的服务
团队都会单独开发这一功能，重复的业务逻辑，这违背了良好的软件工程中的很多原则。

###### 89.SOA、分布式、微服务之间有什么关系和区别？

1分布式架构是指将单体架构中的各个部分拆分，然后部署不同的机器或进程中去，SOA和微服务基本上都是分布式架构的
2SOA是一种面向服务的架构，系统的所有服务都注册在总线上，当调用服务时，从总线上查找服务信息，然后调用
3微服务是一种更彻底的面向服务的架构，将系统中各个功能个体抽成一个个小的应用程序，基本保持一个应用对应的一个服务的架构

###### 90.怎么拆分微服务、拆分时机是什么？

```
图灵课堂
```

#### 怎么拆：

1、高内聚低耦合，职责单一，服务粒度适中，服务不要太细（有的团队甚至一个接口一个服务，一个表一个服务）
2、以业务模型切入：比如产品，用户，订单为一个模型来切入）
3、演进式拆分：刚开始不要划分太细，可以随着迭代过程来逐步优化。  

微服务 1.0，仅使用注册发现，基于 SpringCloud 或者 Dubbo 进行开发，目前意图实施微服务的传统企业大部分处于这个阶段，或者正
从单体应用，向这个阶段过渡，处于 0.5 的阶段；

微服务 2.0，使用了熔断，限流，降级等服务治理策略，并配备完整微服务工具和平台，目前大部分互联网企业处于这个阶段。传统企业
中的领头羊，在做互联网转型的过程中，正在向这个阶段过渡，处于 1.5 的阶段；

微服务 3.0，Service Mesh 将服务治理作为通用组件，下沉到平台层实现，使得应用层仅仅关注业务逻辑，平台层可以根据业务监控自动
调度和参数调整，实现 AIOps 和智能调度。目前一线互联网公司在进行这方面的尝试。

拆分时机： 

业务量足够大，足够成本（投入成本、时间成本）

首先，如果是预估到业务在飞速增长，那就别犹豫，一定要提前考虑微服务的拆分。

其次，如果在设计架构的时候，发现需要很多异构的技术栈，那也要考虑下微服务。

最后，如果公司技术基础设施非常完备，对应的业务起初就设计的非常复杂，那么也别犹豫，起手就上微服务。

###### 91.Spring Cloud有哪些常用组件，作用是什么？

```
图灵课堂
```

```
注册中心：管理服务
负载均衡：客户端的负载均衡器
服务调用：使远程服务调用更加优雅
配置中心：管理服务的配置
服务熔断：保证应用高可用 防止出现服务雪崩，防止激增流量打垮冷系统.....
分布式事务：Seata
网关：为客户端提供统一的服务，一些跟业务本身功能无光的公共逻辑都可以放在网关实现：鉴权、日志、限流、跨域、路由
转发....
链路追踪：实时追踪服务的监控状况，协助快速恢复
```
###### 92.注册中心的原理是什么？ 

服务注册： 当服务启动 通过Rest请求的方式向Nacos Server注册自己的服务
服务心跳：Nacose Client 会维护一个定时心跳持续通知Nacos Server , 默认5s一次， 如果nacos  Client超过了15秒没
有接收心跳，会将服务健康状态设置false（拉取的时候会忽略）,    如果nacos  Client超过了30 秒没有接收心跳 剔除服
务。
服务发现：Nacose Client  会有一个定时任务，实时去Nacos Server 拉取健康服务
服务停止： Nacose Client 会主动通过Rest请求Nacos Server 发送一个注销的请求

###### 93.谈谈配置中心？

```
图灵课堂
```

集中管理服务的配置、提高维护性、时效性、安全性
有哪些东西可以作为配置？
比方说，数据库连接Url，缓存连接url字符串，数据库的用户名，密码都可以作为配置的字符串，除此之外，还有一些可以动态调整的参
数，比方说，客户端的超时设置限流规则和降级开关，流量的动态调度，比方说某个功能只是针对某个地区用户，还有某个功能只在大促
的时段开放，如果这种需要通过静态的方式去配置或者发布的方式去配置，那么响应速度是非常慢，可能对业务存在风险，如果有一套集
中式的配置中心，只需要相关人员在配置中心动态去调整参数，就基本上可以实时或准实时去调整相关对应的业务。所以配置中心在微服
务中算是一个举足轻重的组件。

###### 推还是拉

现在我们了解了 Nacos 的配置管理的功能了，但是有一个问题我们需要弄明白，那就是 Nacos 客户端是怎么实时获取到 Nacos 服务端
的最新数据的。
其实客户端和服务端之间的数据交互，无外乎两种情况：
服务端推数据给客户端
客户端从服务端拉数据
那到底是推还是拉呢，从 Nacos 客户端通过 Listener 来接收最新数据进行分析

原理：
 Nacos 服务端创建了相关的配置项后，客户端就可以进行监听了。
客户端是通过一个定时任务来检查自己监听的配置项的数据的，一旦服务端的数据发生变化时，客户端将会获取到最新的数据，并将最新
的数据保存在一个 CacheData 对象中，然后会重新计算 CacheData 的 md5 属性的值，此时就会对该 CacheData 所绑定的 Listener 触
发 receiveConfigInfo（接收配置信息） 回调。

###### 拉的优势

```
图灵课堂
```

客户端拉取服务端的数据与服务端推送数据给客户端相比，优势在哪呢，为什么 Nacos 不设计成主动推送数据，而是要客户端去拉取呢？
如果用推的方式，服务端需要维持与客户端的长连接，这样的话需要耗费大量的资源，并且还需要考虑连接的有效性，例如需要通过心跳
来维持两者之间的连接。而用拉的方式，客户端只需要通过一个无状态的 http 请求即可获取到服务端的数据。

###### 94.说说服务网关可以做什么？

```
面对互联网复杂的业务系统，基本可以将服务网关分成两类：流量网关和业务网关。
流量网关：跟具体的后端业务系统和服务完全无关的部分，比如安全策略、全局性流控策略、流量分发策略等。
业务网关：针对具体的后端业务系统，或者是服务和业务有一定关联性的部分，并且一般被直接部署在业务服务的前面。业务网关一
般部署在流量网关之后，业务系统之前，比流量网关更靠近系统。我们大部分情况下说的 API 网关，狭义上指的是业务网关。并且如
果系统的规模不大，我们也会将两者合二为一，使用一个网关来处理所有的工作
```
###### 95.什么是服务雪崩？什么是服务限流？

服务雪崩效应： 因服务提供者的不可用导致服务调用者的不可用,并将不可用逐渐放大的过程，就叫服务雪崩效应
解决方式：
通过熔断机制，当一个服务挂了，被影响的服务能够及时熔断，使用 Fallback 数据保证流程在非关键服务不可
用的情况下，仍然可以进行。

```
图灵课堂
```

```
通过线程池和消息队列机制实现异步化，允许服务快速失败，当一个服务因为过慢而阻塞，被影响服务可以在
超时后快速失败，不会影响整个调用链路。
```
###### 服务限流

是指在高并发请求下，为了保护系统，可以对访问服务的请求进行数量上的限制，从而防止系统不被大量请求压垮，在秒杀中，限流是非
常重要的。

###### 96.什么是服务熔断？什么是服务降级？区别是什么？

1服务熔断（终止交易），当服务A调用的某个服务B不可用时，上游服务A为了保证自己不受影响，及时切断与服务B的通讯。以防服务雪
崩。   防止服务雪崩一种措施

2.服务降级（执行B计划）：提前预想好另外一种兜底措施，  可以进行后期补救。 直到服务B恢复，再恢复和B服务的正常通讯。    当被
调用服务不可用时的一种兜底措施。

###### 97.说说Seata的实现原理？

在应用中Seata整体事务逻辑基于两阶段提交的模型，核心概念包含三个角色：
TC：事务协调者，即独立运行的seata-server，用于接收事务注册，提交和回滚。
TM：事务发起者。用来告诉TC全局事务的开始，提交，回滚。
RM：事务资源，每一个RM都会作为一个分支事务注册在TC。
AT(Auto Transaction)模式

第一阶段 
过程：
 TM 向 TC 申请开启一个全局事务，全局事务创建并生成一个全局唯一的XID。
 XID 在微服务调用链路的上下文中传播。
假设运行：update product set name = 'GTS' where name = 'TXC';    // id=1

1. 解析 SQL：得到 SQL 的类型（UPDATE），表（product），条件（where name = 'TXC'）等相关的信
息。
2. 查询前镜像：根据解析得到的条件信息，生成查询语句，定位数据。 select * from product where name 
= 'TXC'  镜像前数据

```
图灵课堂
```

3. 执行业务 SQL：更新这条记录的 name 为 'GTS'。
4. 查询后镜像：根据前镜像的结果，通过 主键 定位数据。select * from produc where name = 'TXC' 镜像后
数据
5. 插入回滚日志：把前后镜像数据以及业务 SQL 相关的信息组成一条回滚日志记录，插入到 UNDO_LOG 表中。
    提交前，RM 向 TC 注册分支：申请 product 表中，主键值等于 1 的记录的 全局锁 。
6. 本地事务提交：业务数据的更新和前面步骤中生成的 UNDO LOG 一并提交。
    TM 向 TC 发起针对 XID 的全局提交或回滚决议。将本地事务提交的结果上报给 TC

#### 二阶段-提交

```
TC 调度 XID 下管辖的全部分支事务完成提交或回滚请求。
```
1. 收到 TC 的分支提交请求，把请求放入一个异步任务的队列中，马上返回提交成功的结果给 TC。
2. 异步任务阶段的分支:提交请求,将异步和批量地删除相应 UNDO LOG 记录。

#### 二阶段-回滚

```
TC 调度 XID 下管辖的全部分支事务完成提交或回滚请求。
```
1. 收到 TC 的分支回滚请求，开启一个本地事务，执行如下操作。

```
图灵课堂
```

2. 通过 XID 和 Branch ID 查找到相应的 UNDO LOG 记录。
3.数据校验：拿 UNDO LOG 中的后镜与当前数据进行比较，如果有不同，说明数据被当前全局事务之外的动作做
了修改（出现脏写），转人工处理（Seata 因为无法感知这个脏写如何发生，此时只能打印日志和触发异常通知，
告知用户需要人工介入）
4.人工没有脏写就简单了：根据 UNDO LOG 中的前镜像和业务 SQL 的相关信息生成并执行回滚的语句：
5 .提交本地事务。并把本地事务的执行结果（即分支事务回滚的结果）上报给 TC。

###### 98.你的微服务项目出了异常怎样更快速的定位？

#### 介绍

```
1.页面
```
```
图灵课堂
```

1. 报错通知
    a. DB

##### i. 

1. Hystrix

```
图灵课堂
```

#### 问题排查

1. hystrix
1. 日志

```
图灵课堂
```

a. 

#### 动态日志级别调整

1. 使用场景
    a. 线上问题排查。
2. 使用方式
    a. 直接调整单个运行的节点的日志级别获取更详细的日志信息。
3. 使用效果
    a. 无需重启服务即可打印详细日志。
    b. 单节点调整，不会影响其他节点。

#### 历史状态日志

1. 查看一定时间内的微服务状态信息
    a. 上线下情况
    b. 访问情况
    c. 异常情况及其详细信息

```
图灵课堂
```

d. 状态变化时间点

#### 实际案例

1. 微服务执行对应数据库测试语句失败。
2. 监控页面微服务对应颜色从绿色变成红色。
3. 触发企业微信通知
4. 相关人员获知微服务出现的异常情况与原因
5. 解决问题
    a. 可以通过异常信息直接判断异常原因

##### i. 直接进行相关处理。

b. 通过异常信息无法判断异常原因

##### i. 直接访问该微服务的异常节点查看相关日志。

```
图灵课堂
```

#### 低版本问题

```
图灵课堂
```

1. 低版本的配置中心会每次检查都会访问一次配置中心数据
    a. 解决方案

##### i. 升级为部门标准版本

b. 高版本优化

##### i. 加入缓存

###### 99.Ribbon说说有哪些负载均衡策略

IRule
这是所有负载均衡策略的父接口，里边的核心方法就是choose方法，用来选择一个服务实例。
AbstractLoadBalancerRule
AbstractLoadBalancerRule是一个抽象类，里边主要定义了一个ILoadBalancer，这里定义它的目的主要是辅助负责均衡策略选取合适的服务端实
例。
RandomRule
看名字就知道，这种负载均衡策略就是随机选择一个服务实例，看源码我们知道，在RandomRule的无参构造方法中初始化了一个Random对象，
然后在它重写的choose方法又调用了choose(ILoadBalancer lb, Object key)这个重载的choose方法，在这个重载的choose方法中，每次利用
random对象生成一个不大于服务实例总数的随机数，并将该数作为下标所以获取一个服务实例。
RoundRobinRule
RoundRobinRule这种负载均衡策略叫做线性轮询负载均衡策略。这个类的choose(ILoadBalancer lb, Object key)函数整体逻辑是这样的：开启
一个计数器count，在while循环中遍历服务清单，获取清单之前先通过incrementAndGetModulo方法获取一个下标，这个下标是一个不断自增长

```
图灵课堂
```

的数先加 1 然后和服务清单总数取模之后获取到的（所以这个下标从来不会越界），拿着下标再去服务清单列表中取服务，每次循环计数器都会加
1 ，如果连续 10 次都没有取到服务，则会报一个警告No available alive servers after 10 tries from load balancer: XXXX。
RetryRule（在轮询的基础上进行重试）
看名字就知道这种负载均衡策略带有重试功能。首先RetryRule中又定义了一个subRule，它的实现类是RoundRobinRule，然后在RetryRule的
choose(ILoadBalancer lb, Object key)方法中，每次还是采用RoundRobinRule中的choose规则来选择一个服务实例，如果选到的实例正常就返
回，如果选择的服务实例为null或者已经失效，则在失效时间deadline之前不断的进行重试（重试时获取服务的策略还是RoundRobinRule中定义的
策略），如果超过了deadline还是没取到则会返回一个null。
WeightedResponseTimeRule（权重 —nacos的NacosRule ，Nacos还扩展了一个自己的基于配置的权重扩展）
WeightedResponseTimeRule是RoundRobinRule的一个子类，在WeightedResponseTimeRule中对RoundRobinRule的功能进行了扩展，
WeightedResponseTimeRule中会根据每一个实例的运行情况来给计算出该实例的一个权重，然后在挑选实例的时候则根据权重进行挑选，这样能
够实现更优的实例调用。WeightedResponseTimeRule中有一个名叫DynamicServerWeightTask的定时任务，默认情况下每隔 30 秒会计算一次
各个服务实例的权重，权重的计算规则也很简单，如果一个服务的平均响应时间越短则权重越大，那么该服务实例被选中执行任务的概率也就越大。
ClientConfigEnabledRoundRobinRule
ClientConfigEnabledRoundRobinRule选择策略的实现很简单，内部定义了RoundRobinRule，choose方法还是采用了RoundRobinRule的
choose方法，所以它的选择策略和RoundRobinRule的选择策略一致，不赘述。
BestAvailableRule
BestAvailableRule继承自ClientConfigEnabledRoundRobinRule，它在ClientConfigEnabledRoundRobinRule的基础上主要增加了根据
loadBalancerStats中保存的服务实例的状态信息来过滤掉失效的服务实例的功能，然后顺便找出并发请求最小的服务实例来使用。然而
loadBalancerStats有可能为null，如果loadBalancerStats为null，则BestAvailableRule将采用它的父类即
ClientConfigEnabledRoundRobinRule的服务选取策略（线性轮询）。
ZoneAvoidanceRule （默认规则，复合判断server所在区域的性能和server的可用性选择服务器。）
ZoneAvoidanceRule是PredicateBasedRule的一个实现类，只不过这里多一个过滤条件，ZoneAvoidanceRule中的过滤条件是以
ZoneAvoidancePredicate为主过滤条件和以
AvailabilityPredicate为次过滤条件组成的一个叫做CompositePredicate的组合过滤条件，过滤成功之后，继续采用线性轮询
(RoundRobinRule)的方式从过滤结果中选择一个出来。
AvailabilityFilteringRule（先过滤掉故障实例，再选择并发较小的实例）
 过滤掉一直连接失败的被标记为circuit tripped的后端Server，并过滤掉那些高并发的后端Server或者使用一个AvailabilityPredicate来
包含过滤server的逻辑，其实就是检查status里记录的各个Server的运行状态。

###### 100.你项目哪些场景用到了限流、降级？怎么配的？

服务降级的预案
在进行降级之前要对系统进行梳理，提前将一些 不重要 或 不紧急 的服务（弱依赖）或任务进行服务的 延迟使用 或 暂停使用。 （积分）
看看哪些服务是必须誓死保护，哪些系统是能够丢卒保帅；具体可以参考日志级别设置预案：

一般：有些服务偶尔因为网络抖动或者服务正在上线而超时，可以自动降级；
警告：有些服务在一段时间内成功率有波动（如在95~100%之间），可以自动降级或人工降级，并发送告警；
错误：可用率低于90%，或者连接池被占用满了，或者访问量突然猛增到系统能承受的最大阀值，此时可以根据情况自动降级或者人工降
级；
严重错误：因为特殊原因数据错误了，此时需要紧急人工降级   

测试会提供准确的数据；
自己算： 二八法则

```
计算关系：
QPS = 并发量 / 平均响应时间
并发量 = QPS * 平均响应时间
根据以上计算关系，我们来预估下单日访问量在 1000W 需要多大的QPS来支持：
通常情况下，80% 的访问量集中在 20%的时间，算一下这 1000w pv实际需要机器达到多少qps才能满足，
qps = (1000w * 0.8) / (24 * 3600 * 0.2)
qps = 462.9
根据压力测试的反馈，单台机子的QPS是多少，利用以上结果就可以算出需要几台机器 或 大致推算出需不需要使用缓存配置
方案一： 使用集群服务器 不使用缓存服务器
方案二： 使用集群服务器 同时使用缓存服务器 (推荐)
例子：
```
```
图灵课堂
```

    每秒可以处理的请求数 QPS（TPS）：每秒钟可以处理的请求或者事务的数量。
    并发数： 系统同一时候处理的请求数量（事务数）
    响应时间：  一般取平均响应时间
QPS（TPS）= 并发数/平均响应时间
并发数 = QPS*平均响应时间
例子:
 一个典型的上班签到系统，早上8点上班。7点半到8点这30分钟的时间里用户会登录签到系统进行签到。公司员工为1000
人，平均每一个员上登录签到系统的时长为5分钟。能够用以下的方法计算。
（1）QPS = 1000/(30*60) 事务/秒
（2）平均响应时间为 = 5*60  秒
（3）并发数= QPS*平均响应时间 = 1000/(30*60) *(5*60)=166.7
再看如果老板要求实现多少并发数，在反推出机器需要多少QPS，看是否集群配置。

```
图灵课堂
```