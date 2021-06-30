#Spring环环相扣

最近面试经常被问到Spring的一些内容，经过一系列的学习，发现Spring底层真的是环环相扣，面试的时候可以根据某一个点延伸出N个面，下面就面试常问问的一些技术点进行总结汇总，了解他们是怎么环环相扣的。
首先就本文涉及到的几个知识点先罗列一下：
* 什么是IOC
* @Autowired和@Resource的区别
* Spring Bean的生命周期
* Spring的循环依赖
* 简单的介绍一下AOP
* JDK动态代理和Cglib的区别
* JDK动态代理实现的原理

## Spring Bean的生命周期
实例化 -> 属性填充 -> 实现aware接口
-> if(实现BeanPostProcessor接口) -> postProcessBeforeInitialization -> init(初始化) -> postProcessAfterInitialization
-> 放入Bean的单例池 -> 使用 -> 销毁

## @Autowired和@Resource的区别

### @Autowired
@Autowired默认按照类型(by-type)装配，默认情况下要求依赖对象必须存在。

* 如果允许依赖对象为null，需设置required属性为false，即
~~~
@Autowired(required=false)
private InjectionBean beanName;
~~~

* 如果使用按照名称(by-name)装配，需结合@Qualifier注解使用，即
~~~
@Autowired
@Qualifier("beanName")
private InjectionBean beanName;
~~~

### @Resource
@Resource默认按照名称(by-name)装配，名称可以通过name属性指定。

* 如果没有指定name
1. 当注解在字段上时，默认取name=字段名称装配。
2. 当注解在setter方法上时，默认取name=属性名称装配。

* 当按照名称(by-name）装配未匹配时，按照类型(by-type)装配。
1. 当显示指定name属性后，只能按照名称(by-name)装配。

@Resource装配顺序

1. 如果同时指定name和type属性，则找到唯一匹配的bean装配，未找到则抛异常；
2. 如果指定name属性，则按照名称(by-name)装配，未找到则抛异常；
3. 如果指定type属性，则按照类型(by-type)装配，未找到或者找到多个则抛异常；
4. 既未指定name属性，又未指定type属性，则按照名称(by-name)装配；如果未找到，则按照类型(by-type)装配。

### 对比

|  对比项   | @Autowired             |  @Resource                         |
|  ----    | ----                   |  ----                              |
| 注解来源  | Spring注解              | JDK注解(JSR-250标准注解，属于J2EE)  |
| 装配方式  | 优先按类型              | 优先按名称                          |
| 属性      | required               | name、type                         |
| 作用范围  | 字段、setter方法、构造器 | 字段、setter方法                    |




