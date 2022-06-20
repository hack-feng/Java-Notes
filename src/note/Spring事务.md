[TOC]

## Spring中事务

> Spring提供了两种事务管理机制：编程式事务、声明式事务

### 编程式事务

在代码中手动的管理事务的提交、回滚等操作，代码侵入性比较强

```java
try {
    //TODO something
     transactionManager.commit(status);
} catch (Exception e) {
    transactionManager.rollback(status);
    throw new InvoiceApplyException("异常失败");
}
```

### 声明式事务(推荐)

基于AOP面向切面的，它将具体业务与事务处理部分解耦，代码侵入性很低，所以在实际开发中声明式事务用的比较多。

```java
@Transactional
@GetMapping("/test")
public String test() {
    int insert = cityInfoDictMapper.insert(cityInfoDict);
}
```

## @Transactional

### 作用域

- **作用于类**
  当把@Transactional 注解放在类上时，表示所有该类的public方法都配置相同的事务属性信息。
- **作用于方法**
  当类配置了@Transactional，方法也配置了@Transactional，方法的事务会覆盖类的事务配置信息。
- **作用于接口**
  不推荐这种使用方法，因为一旦标注在Interface上并且配置了Spring AOP 使用CGLib动态代理，将会导致@Transactional注解失效

### 属性

| 属性                       | 功能描述                                                     |
| -------------------------- | ------------------------------------------------------------ |
| **readOnly**               | 指定事务是否为只读事务，默认值为 false； 为了忽略那些不需要事务的方法，比如读取数据，可以设置 read-only 为 true。 【例如】：@Transactional(readOnly=true) |
| **rollbackFor**            | 用于指定能够触发事务回滚的异常类型，可以指定多个异常类型。 【例如】 指定单一异常类：@Transactional(rollbackFor=RuntimeException.class) 指定多个异常类：@Transactional(rollbackFor={RuntimeException.class, Exception.class}) |
| **rollbackForClassName**   | 用于指定能够触发事务回滚的异常类型 名称，可以指定多个异常类型 名称。 【例如】 指定单一异常类名称：@Transactional(rollbackForClassName=“RuntimeException”) 指定多个异常类名称：@Transactional(rollbackForClassName={“RuntimeException”,“Exception”}) |
| **noRollbackFor**          | 用于设置不需要进行回滚的异常类，可以指定多个异常类型 名称。 【例如】 指定单一异常类：@Transactional(noRollbackFor=RuntimeException.class) 指定多个异常类：@Transactional(noRollbackFor={RuntimeException.class, Exception.class}) |
| **noRollbackForClassName** | 用于设置不需要进行回滚的异常类 名称，可以指定多个异常类型 名称。 【例如】 指定单一异常类名称：@Transactional(noRollbackForClassName=“RuntimeException”) 指定多个异常类名称：@Transactional(noRollbackForClassName={“RuntimeException”,“Exception”}) |
| **isolation**              | 事务的隔离级别，默认值为 Isolation.DEFAULT。 （1）Isolation.DEFAULT：使用底层数据库默认的隔离级别。 （2）Isolation.READ_UNCOMMITTED：读取未提交数据(会出现脏读, 不可重复读) 基本不使用 （3）Isolation.READ_COMMITTED：读取已提交数据(会出现不可重复读和幻读) （4）Isolation.REPEATABLE_READ：可重复读(会出现幻读) Isolation.SERIALIZABLE：串行化 【例如】 @Transactional(isolation = Isolation.READ_UNCOMMITTED) |
| **timeout**                | 该属性用于设置事务的超时秒数，超过该时间限制但事务还没有完成，则自动回滚事务。默认值为-1表示永不超时 【例如】 @Transactional(timeout=30) |
| **propagation**            | 该属性用于设置事务的传播行为。默认是Propagation.REQUIRED     |

* propagation的事务传播机制：

（1）Propagation.REQUIRED:如果当前存在事务，则加入该事务，如果当前不存在事务，则创建一个新的事务。

（2）Propagation.SUPPORTS：如果当前存在事务，则加入该事务；如果当前不存在事务，则以非事务的方式继续运行。

（3）Propagation.MANDATORY：如果当前存在事务，则加入该事务；如果当前不存在事务，则抛出异常。

（4）Propagation.REQUIRES_NEW：重新创建一个新的事务，如果当前存在事务，暂停当前的事务。 

（5）Propagation.NOT_SUPPORTED：以非事务的方式运行，如果当前存在事务，暂停当前的事务。 

（6）Propagation.NEVER：以非事务的方式运行，如果当前存在事务，则抛出异常。

（7）Propagation.NESTED ：表示如果当前已经存在一个事务，那么该方法将会在嵌套事务中运行。嵌套的事务可以独立于当前事务进行单独地提交或回滚。如果当前事务不存在，那么其行为和 Propagation.REQUIRED 效果一样。 

### 关于rollbackFor回滚的异常类型

![image.png](http://file.xiaoxiaofeng.site/blog/image/20211105-bdd1a94e-2682-4057-91ce-7b6a1dece494.png)

**（1）默认什么异常会回滚？**

> rollbackFor默认是error和RuntimeException会触发回滚，其他异常或自定义异常不会回滚

**（2）怎么看是否是运行时异常？**

> 看其父类（或者“爷爷类”）是否是RuntimeException

## SpringBoot配置全局异常处理

- **自定义Controller注解**

```java
@RestController
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public @interface MyController {
    String value() default "";
}
```

在项目的Controller层，直接使用该注解即可（所有通过该Controller层的方法都会有事务）

- **自定义@Service**

```java
@Service
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public @interface MyService {
    String value() default "";
}
```

一般不用，因为Controller层已经加了事务，再用这个可能因为`事务嵌套`

## 失效场景

**1、@Transactional 应用在非 public 修饰的方法上**

> 如果Transactional注解应用在非public 修饰的方法上，Transactional将会失效。
> 原因是：TransactionInterceptor （事务拦截器）使用AOP，在目标方法执行前后进行拦截，会检查目标方法的修饰符是否为 public，不是 public则不会获取@Transactional 的属性配置信息。（spring要求被代理方法必须是public的。）

**2、@Transactional 注解属性 propagation 设置错误**

> 这种是因为propagation的参数设置错误，根据需求选择合适的@Transactional的propagation属性的值

**3、@Transactional 注解属性 rollbackFor 设置错误**

> rollbackFor默认是error和RuntimeException会触发回滚，其他异常或自定义异常不会回滚，需要回滚则需要指定或者直接指定@Transactional(rollbackFor=Exception.class)

**4、同一个类中方法调用，导致@Transactional失效**

> 这个和redis缓存失效场景之一一样，如果在一个类中，B方法加了事务，A方法没加事务调用B方法，另外一个类调用A方法，则B方法的事务失效。这种情况可以加一层Mamager层对Service层通用能力的下沉。

**5、异常被捕获，导致@Transactional失效**

> 当我们是用catch把异常捕获了，那么该方法就不会抛出异常了，那么出现该异常就不会回滚了

**6、数据库引擎不支持事务**

> 这种情况出现的概率并不高，事务能否生效数据库引擎是否支持事务是关键。常用的MySQL数据库默认使用支持事务的innodb引擎。一旦数据库引擎切换成不支持事务的myisam，那事务就从根本上失效了。

**7、没有被 Spring 管理**

如下面例子所示：

```java
@Service
public class OrderService {
    @Transactional
    public void updateOrder(Order order) {
        // update order
    }
}
```

如果此时把 @Service 注解注释掉，这个类就不会被加载成一个 Bean，那这个类就不会被 Spring 管理了，事务自然就失效了。

**8、方法被定义为finall**

> 方法被定义成了final的，这样会导致spring aop生成的代理对象不能复写该方法，而让事务失效。

## 事务异常记录

### Transaction rolled back because it has been marked as rollback-only

#### 问题描述

事务的注解使用起来很简单，但是如果只了解皮毛就会出现事务失效、事务异常等问题。

本次主要讲，在事务嵌套（加了事务的方法，调用加了事务的方法）时，报错

```
Transaction rolled back because it has been marked as rollback-only, 
org.springframework.transaction.UnexpectedRollbackException: 
Transaction rolled back because it has been marked as rollback-only
```

#### 问题产生原因

##### 问题复现

如下，在ClassA类中有个加了事务的A方法，调用了ClassB中的加了事务的B方法

```java
public Class ClassA {
	private ClassB classB;
	@Transactional
	public void A() {
		try {
			B();
		} catch (Exception e) {
			log.error("啥也不干");
		}
	}
}
```
```java
public Class ClassB {
	@Transactional
	public void B() {
		throw new Exception();
	}
}
```

这种情况下就会报错：Transaction rolled back because it has been marked as rollback-only

##### 报错原因

当A方法的事物(REQUIRED),B方法的事物(REQUIRED),A调用B方法，在spring中，spring将会把这些事务合二为一。

当整个方法中每个子方法没报错时，整个方法执行完才提交事务。

如果某个子方法有异常，spring将该事务标志为rollback only。如果这个子方法没有将异常往上抛，或者主父方法将子方法抛出的异常捕获了，那么，该异常就不会触发事务进行回滚，事务就会在整个方法执行完后就会提交，这时就会造成Transaction rolled back because it has been marked as rollback-only的异常。（`由于异常被标记了rollback only，但是又执行了commit，此时就会报这个错`）

#### 解决方法

- **方法1：父方法不要捕获异常**

  在2.1的举例中，A方法去掉try…catch即可

- **方法2：子方法的事务propagation属性换为NESTED**

  在2.1的举例中，将B方法的事务注解的属性改为NESTED

  ```java
  public Class ClassB {
  	@Transactional(propagation = Propagation.NESTED)
  	public void B() {
  		throw new Exception();
  	}
  }
  ```

| 属性               | 功能描述                                                     |
| ------------------ | ------------------------------------------------------------ |
| Propagation.NESTED | 表示如果当前已经存在一个事务，那么该方法将会在嵌套事务中运行。嵌套的事务可以独立于当前事务进行单独地提交或回滚。如果当前事务不存在，那么其行为和 Propagation.REQUIRED 效果一样。 |

## 写在最后

本文到此就结束了，博主会持续输出日常工作中常用的技术总结和面试的高频点。

老规矩，懂了就点赞收藏；不懂就问，日常在线，我会就会回复哈~

