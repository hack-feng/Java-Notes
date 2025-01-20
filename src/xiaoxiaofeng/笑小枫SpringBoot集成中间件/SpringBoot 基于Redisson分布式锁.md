
## 1. 分布式锁 

### 1.1 为什么要使用分布式锁 

以下是使用分布式锁的一些主要原因：

1.  **保持数据一致性**： 在分布式系统中，数据一致性是至关重要的。使用分布式锁可以防止并发更新导致的数据不一致问题，确保数据在所有节点之间保持一致
2.  **避免重复执行**： 当多个进程可能执行相同的任务时，分布式锁可以防止任务被重复执行，例如，防止多个节点同时处理同一条数据
3.  **资源同步**： 分布式锁可以用来同步对共享资源的访问，比如数据库、文件系统或者外部服务等

### 1.2 分布式锁的应用场景

以下是分布式锁的一些具体应用场景：

 *  **订单处理**：在电子商务系统中，避免多个服务实例同时处理同一个订单
 *  **库存管理**：确保在更新商品库存时不会因为并发操作导致库存数量错误
 *  **任务调度**：在分布式任务调度系统中，防止同一个任务被多个工作节点同时执行
 *  **缓存更新**：在缓存数据需要更新时，防止多个实例同时写入，导致缓存数据不一致

### 1.3 分布式锁的实现原理

![img](https://image.xiaoxiaofeng.site/blog/2025/01/16/xxf-20250116132140.png?xxfjava)

## 2. 使用分布式锁的注意事项

### 2.1 单机服务

单机服务虽然可以使用分布式锁，也可以直接使用`synchronized`直接加锁解决问题。

### 2.2 锁释放问题

Redisson支持多种加锁方式，下面简单介绍下。

首先先获取锁，可以使用`工程名:方法名:数据唯一标志`等命名，

`RLock lock = redissonUtil.getLock("锁名称");`

**java.util.concurrent.locks.Lock**类里的加锁接口


```java
/**
 * 获取锁。如果锁不可用，那么当前线程将进入休眠状态等待，直到获得锁为止。
 * 默认锁30s，通过看门狗调度，每10s检查业务是否执行完，如果没执行完，自动续期为30s
 */
void lock();
```

```java
/**
 * 尝试获取锁。如果锁可用，返回true，否则返回false。
 * 默认锁30s，通过看门狗调度，每10s检查业务是否执行完，如果没执行完，自动续期为30s
 */
boolean tryLock();
```

```java
/**
 * 尝试获取锁。如果锁可用，返回true，否则进入等待时间，如果等待时间内，锁仍不可用，则返回false。
 * 默认锁30s，通过看门狗调度，每10s检查业务是否执行完，如果没执行完，自动续期为30s
 * 
 * @param time 等待锁的最大时间
 * @param unit 时间单位
 */
boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
```
**org.redisson.api.RLock**类里的加锁接口

```java
/**
 * 获取锁。如果锁不可用，那么当前线程将进入休眠状态等待，直到获得锁为止。
 * 锁不会通过看门狗调度进行自动续期，达到leaseTime的时间后，不论业务是否执行完，锁都会释放掉
 * 
 * @param leaseTime 锁的自动释放时间
 * @param unit 时间单位
 */
void lock(long leaseTime, TimeUnit unit);
```

```java
/**
 * 尝试获取锁。如果锁可用，返回true，否则进入等待时间，如果等待时间内，锁仍不可用，则返回false。
 * 锁不会通过看门狗调度进行自动续期，达到leaseTime的时间后，不论业务是否执行完，锁都会释放掉
 * 
 * @param waitTime 等待锁的最大时间
 * @param leaseTime 锁的自动释放时间
 * @param unit 时间单位
 */
boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;
```

**注意事项：**

针对上述的几种方法，有几点需要我们实际使用的过程中权衡。

* 情景一：如果请求必须等待上一个请求执行完成，如果上个请求未完成，则无脑等待；无脑使用`void lock();`即可。
* 情景二：如果不想等待，获取不到锁，立马返回，使用`boolean tryLock();`。
* 情景三：如果可以等待一段时间，一段时间内获取不到锁，返回的可以使用`boolean tryLock(long time, TimeUnit unit)`。
* 情景四：如果不想等待，获取不到锁，立马返回，且业务执行到指定时间不管是否完成，必须返回的，可以使用`void lock(long leaseTime, TimeUnit unit);`。
* 情景五：如果可以等待一段时间，且业务执行到指定时间不管是否完成，必须返回的，可以使用`boolean tryLock(long waitTime, long leaseTime, TimeUnit unit)`。

注意点：如果设置了`leaseTime`，实际情况中，业务某个节点执行超过了设置的`leaseTime`时间。这种情况业务数据如何处理一定要考虑清楚。下文示例中有简单的情景处理方法供参考。


## 3. Redisson实现分布式锁

### 3.1 引入依赖

在项目的 `pom.xml` 文件中添加以下依赖

```java
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-boot-starter</artifactId>
    <version>3.16.8</version>
</dependency>
```

### 3.2 编写配置文件

在 `application.yml` 文件中配置 Redis 的连接信息

```java
spring:
  data:
    redis:
      host: 127.0.0.1 # Redis服务器的主机地址
      port: 6379 # Redis服务器的端口号
      password: 123456 # 访问Redis服务器的密码
      database: 0 # Redis数据库的索引
```

### 3.3 初始化 RedissonClient

如果项目已经使用redis，存在redis的config配置类。则无需配置，可以直接使用Redisson。

否则需要配置Redisson。Redisson 提供了多种配置方式，包括单节点、集群、哨兵等。在实际开发中，我们常常使用 Redis 的单节点配置。

以下是一个基本的 Redisson 配置示例：

~~~java
package com.maple.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson 配置
 * redisson 支持单点、主从、哨兵、集群等部署方式
 */
@Slf4j
@Configuration
public class RedissonConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private Integer database;
    /**
     * redisson 客户端
     * @return
     */
    @Bean
    public RedissonClient redissonClient() {
        //单点
        Config config = new Config();
        //地址及密码
        String redisUrl = String.format("redis://%s:%s", host + "", port + "");
        config.useSingleServer()
                .setAddress(redisUrl)
                .setPassword(password)
                // 重试间隔
                .setRetryInterval(3000)
                // 重试次数
                .setRetryAttempts(5)
                // 命令等待超时时间
                .setTimeout(10000)
                // 配置连接池大小
                .setConnectionPoolSize(64)
                // 配置最小空闲连接数
                .setConnectionMinimumIdleSize(10)
                // 选择数据库0
                .setDatabase(database);
        log.info("redisson 客户端初始化成功");
        return Redisson.create(config);

        //主从
//        Config config = new Config();
//        config.useMasterSlaveServers()
//            .setMasterAddress("redis://127.0.0.1:6379").setPassword("123456")
//            .addSlaveAddress("redis://127.0.0.1:6389")
//            .addSlaveAddress("redis://127.0.0.1:6399");
//        return Redisson.create(config);

        //哨兵
//        Config config = new Config();
//        config.useSentinelServers()
//            .setMasterName("myMaster")
//            .addSentinelAddress("redis://127.0.0.1:6379", "redis://127.0.0.1:6389")
//            .addSentinelAddress("redis://127.0.0.1:6399");
//        return Redisson.create(config);

        //集群
//        Config config = new Config();
//        config.useClusterServers()
//                //cluster state scan interval in milliseconds
//            .setScanInterval(2000)
//            .addNodeAddress("redis://127.0.0.1:6379", "redis://127.0.0.1:6389")
//            .addNodeAddress("redis://127.0.0.1:6399");
//        return Redisson.create(config);
    }
}

~~~

定义一个Redisson的工具类，也可以不定义。下面代码中有用到，懒的处理代码了，又怕有小伙伴找不到。

~~~java
package com.maple.redis.util;

import org.redisson.api.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author 笑小枫  https://xiaoxiaofeng.com/
 * @date 2023/8/28
 */
@Component
public class RedissonUtil {

    private final RedissonClient client;

    public RedissonUtil(RedissonClient client) {
        this.client = client;
    }

    /**
     * 写入缓存
     */
    public <T> void set(String key, T value) {
        RBucket<T> rBucket = client.getBucket(key);
        rBucket.set(value);
    }

    /**
     * 写入缓存设置时效时间
     */
    public <T> void set(String key, T value, long seconds) {
        RBucket<T> rBucket = client.getBucket(key);
        rBucket.set(value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 写入缓存设置时效时间，并指定时间类型
     */
    public <T> void set(String key, T value, long timeToLive, TimeUnit timeUnit) {
        RBucket<T> rBucket = client.getBucket(key);
        rBucket.set(value, timeToLive, timeUnit);
    }

    /**
     * 获取缓存内容
     */
    public <T> T get(String key) {
        RBucket<T> rBucket = client.getBucket(key);
        return rBucket.get();
    }

    /**
     * 删除缓存
     */
    public <T> boolean delete(String key) {
        RBucket<T> rBucket = client.getBucket(key);
        return rBucket.delete();
    }

    /**
     * 删除缓存，并返回缓存的值
     */
    public <T> T getAndDelete(String key) {
        RBucket<T> rBucket = client.getBucket(key);
        return rBucket.getAndDelete();
    }

    /**
     * 获取原子递增Long类型值
     */
    public RAtomicLong getAtomicLong(String key) {
        return client.getAtomicLong(key);
    }

    /**
     * 获取一个普通的RMap实例
     */
    public <K, V> RMap<K, V> getMap(String key) {
        return client.getMap(key);
    }

    /**
     * 获取一个带缓存自动过期功能的RMapCache实例
     */
    public <K, V> RMapCache<K, V> getMapCache(String key) {
        return client.getMapCache(key);
    }

    /**
     * 获取RSet实例
     */
    public <T> RSet<T> getSet(String key) {
        return client.getSet(key);
    }

    /**
     * 获取RList实例
     */
    public <T> RList<T> getList(String key) {
        return client.getList(key);
    }

    /**
     * 获取RQueue实例
     */
    public <T> RQueue<T> getQueue(String key) {
        return client.getQueue(key);
    }

    /**
     * 获取Redis锁
     * 基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。
     */
    public RLock getLock(String key) {
        return client.getLock(key);
    }

    /**
     * 获取Redis公平锁
     * 它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。
     */
    public RLock getFairLock(String key) {
        return client.getFairLock(key);
    }

    /**
     * Redis事务
     * Redisson事务通过分布式锁保证了连续写入的原子性，同时在内部通过操作指令队列实现了Redis原本没有的提交与滚回功能。
     */
    public RTransaction getTransaction() {
        TransactionOptions options = TransactionOptions.defaults()
                // 设置参与本次事务的主节点与其从节点同步的超时时间, 默认值是5秒。
                .syncSlavesTimeout(5, TimeUnit.SECONDS)
                // 处理结果超时, 默认值是3秒。
                .responseTimeout(3, TimeUnit.SECONDS)
                // 命令重试等待间隔时间。仅适用于未发送成功的命令,  默认值是1.5秒。
                .retryInterval(2, TimeUnit.SECONDS)
                // 命令重试次数。仅适用于未发送成功的命令, 默认值是3次。
                .retryAttempts(3)
                // 事务超时时间。如果规定时间内没有提交该事务则自动滚回, 默认值是5秒。
                .timeout(5, TimeUnit.SECONDS);
        return client.createTransaction(options);
    }
}
~~~





### 3.4 分布式锁的使用

首先，定义一个get请求的方法，方法中多个线程模拟并发请求

启动10个线程，每个业务休眠50ms，模拟处理业务

~~~java
    @GetMapping("/handleStockRedisLock")
    public void handleStockRedisLock() {
        ExecutorService threadPool = new ThreadPoolExecutor(
                5,
                10,
                2L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(5),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        Random random = new Random();
        redisUtil.set("maple:stock", 100);
        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            threadPool.execute(() -> testLockService.handleStockRedisLock(finalI, random.nextInt(10) + 1, 50L));
        }
    }
~~~

看下实现

~~~java
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleStockRedisLock(Integer index, Integer buyNum, Long sleepTime) {
        RLock lock = redissonUtil.getLock("handleStockRedisLock");
        boolean isOk = false;
        try {
            // 尝试获取锁，等待5s，5s内没有获取到锁，isOk=false;
            // 业务执行30s，30后未执行完会自动释放锁，lock.unlock()再次执行释放锁时会抛出异常
            isOk = lock.tryLock(5, 30, TimeUnit.SECONDS);
            if (!isOk) {
                log.error("第" + index + "次，获取分布式锁失败");
                return;
            }
            // 业务伪代码
            int stockNum = redissonUtil.get("maple:stock");
            log.info("第" + index + "次，购买商品" + buyNum + "，获取库存，库存数：" + stockNum);
            Thread.sleep(sleepTime);
            redissonUtil.set("maple:stock", stockNum - buyNum);
            log.info("第" + index + "次，处理后的库存数：" + redissonUtil.get("maple:stock"));
        } catch (InterruptedException e) {
            log.error("线程中断");
            Thread.currentThread().interrupt();
        } finally {
            if (isOk) {
                log.info("第" + index + "次，释放锁");
                lock.unlock();
            }
        }
    }
~~~

#### 3.4.1 正常执行结果

先看下正常的执行，线程依次执行。

![image-20250115111307945](https://image.xiaoxiaofeng.site/blog/2025/01/15/xxf-20250115111308.png?xxfjava)

#### 3.4.2 获取锁失败执行结果

再看下获取锁失败的情景，因为锁的等待时间是5s，调整业务处理休眠时间sleepTime为3s，就会有部分线程获取不到锁

![image-20250115111235107](https://image.xiaoxiaofeng.site/blog/2025/01/15/xxf-20250115111235.png?xxfjava)

#### 3.4.3 业务执行超时执行结果

再看下业务执行超时的情景，调整锁的离开时间（leaseTime）为5s，调整业务处理休眠时间sleepTime为6s，即可模拟

因为5s的时候，RLock会自动释放锁，业务6s执行完后，仍然会执行finally代码块的`lock.unlock()`，因为锁已释放，所以抛出异常。

如果数据处理都在一个事务里面，超时需要回滚数据，可以直接使用`@Transactional(rollbackFor = Exception.class)`事务回滚。如果有其他异步数据处理，可以捕捉异常进行处理。

**建议：**可以把非必要业务逻辑移除锁的代码块，尽量减少锁的代码块逻辑，合理的安排leaseTime的时间。

![image-20250115144249513](https://image.xiaoxiaofeng.site/blog/2025/01/15/xxf-20250115144249.png?xxfjava)

#### 3.4.4 加锁时，redis的数据情况

加锁过程中，可以看到redis中，出现了一个HASH类型的数据，key对用我们的锁名称，有效期是我们设置的leaseTime时间或未设置时默认的30s

![image-20250116110647210](https://image.xiaoxiaofeng.site/blog/2025/01/16/xxf-20250116110647.png?xxfjava)

## 4. 基于注解的实现

为了方便分布式锁的日常使用，我们可以使用注解的形式，然后通过Aop切面，直接锁整个方法，使用时业务无侵入，方便快捷。但是，如果方法里的业务处理比较多，不建议直接使用注解，可以将业务拆分，或者针对需要加锁的业务针对性的使用分布式锁。

### 4.1 定义注解

~~~java
package com.maple.redis.util;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 使用redis进行分布式锁
 * @author 笑小枫
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

    /**
     * redis锁 名字
     */
    String lockName() default "";

    /**
     * redis锁 key 支持spel表达式
     */
    String key() default "";

    /**
     * 等待毫秒数,默认为5000毫秒
     *
     * @return 最大等待毫秒数
     */
    int waitTime() default 5000;

    /**
     * 过期毫秒数,默认为-1 不自动解锁
     *
     * @return 轮询锁的时间
     */
    int leaseTime() default -1;

    /**
     * 超时时间单位
     *
     * @return 秒
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
~~~

### 4.2 定义切面

~~~java
/*
 * Copyright (c) 2018-2999 上海合齐软件科技科技有限公司 All rights reserved.
 *
 *
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.maple.redis.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author lgh
 */
@Aspect
@Component
public class RedisLockAspect {

    @Autowired
    private RedissonUtil redissonUtil;

    private static final String REDISSON_LOCK_PREFIX = "redisson_lock:";

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        String spel = redisLock.key();
        String lockName = redisLock.lockName();
        RLock rLock = redissonUtil.getLock(getRedisKey(joinPoint, lockName, spel));
        Object result;
        boolean isLock = false;
        try {
            isLock = rLock.tryLock(redisLock.waitTime(), redisLock.leaseTime(), redisLock.timeUnit());
            if (!isLock) {
                throw new RuntimeException("系统繁忙，请重试");
            }
            //执行方法
            result = joinPoint.proceed();
        } finally {
            if (isLock) {
                rLock.unlock();
            }
        }
        return result;
    }

    /**
     * 将spel表达式转换为字符串
     *
     * @param joinPoint 切点
     * @return redisKey
     */
    private String getRedisKey(ProceedingJoinPoint joinPoint, String lockName, String spel) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object target = joinPoint.getTarget();
        Object[] arguments = joinPoint.getArgs();
        return REDISSON_LOCK_PREFIX + lockName + ":" + parse(target, spel, targetMethod, arguments);
    }


    /**
     * 支持 #p0 参数索引的表达式解析
     *
     * @param rootObject 根对象,method 所在的对象
     * @param spel       表达式
     * @param method     ，目标方法
     * @param args       方法入参
     * @return 解析后的字符串
     */
    public static String parse(Object rootObject, String spel, Method method, Object[] args) {
        if (spel == null || "".equals(spel)) {
            return "";
        }
        //获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u =
                new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);
        if (paraNameArr == null || paraNameArr.length == 0) {
            return spel;
        }
        //使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        //SPEL上下文
        StandardEvaluationContext context = new MethodBasedEvaluationContext(rootObject, method, args, u);
        //把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(spel).getValue(context, String.class);
    }
}
~~~

### 4.3 定义一个测试方法

~~~java
    @GetMapping("/handleStockRedisLockAnnotation")
    public void handleStockRedisLockAnnotation() {
        ExecutorService threadPool = new ThreadPoolExecutor(
                5,
                10,
                2L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(5),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        Random random = new Random();
        redisUtil.set("maple:stock", 100);
        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            threadPool.execute(() -> testLockService.handleStockRedisLockAnnotation(finalI, random.nextInt(10) + 1, 600L));
        }
    }
~~~

### 4.4 定义一个实现接口

~~~java
    @SneakyThrows
    @Override
    @RedisLock(lockName = "handleStockRedisLockAnnotation")
    public void handleStockRedisLockAnnotation(Integer index, Integer buyNum, Long sleepTime) {
        handle(index, buyNum, sleepTime);
    }

    private void handle(Integer index, Integer buyNum, Long sleepTime) throws InterruptedException {
        int stockNum = redissonUtil.get("maple:stock");
        log.info("第" + index + "次，购买商品" + buyNum + "，获取库存，库存数：" + stockNum);
        Thread.sleep(sleepTime);
        redissonUtil.set("maple:stock", stockNum - buyNum);
        log.info("第" + index + "次，处理后的库存数：" + redissonUtil.get("maple:stock"));
    }
~~~

### 4.5 测试结果

跑10个线程，业务处理600ms，锁等待定义5s，所以有一个线程获取锁失败，抛出异常。

![image-20250116160023011](https://image.xiaoxiaofeng.site/blog/2025/01/16/xxf-20250116160023.png?xxfjava)

## 5. 本文源码

使用Redis的过程中还会有很多问题，比如缓存数据一致性，缓存数据持久化，内存淘汰机制，缓存雪崩等等等，在面试的时候也经常会用到，博主整理了一份Redis常见的面试，感兴趣的朋友可以看下：

[【面试1v1实景模拟】Redis面试官会怎么提问？](https://www.xiaoxiaofeng.com/archives/interviewredis)

本文源码：[https://github.com/hack-feng/maple-product/](https://github.com/hack-feng/maple-product/)

其中`maple-redis`模块即为本文的Demo源码。需要的朋友可以看下。

感兴趣的朋友可以帮忙点个star⭐⭐⭐⭐⭐后续会有更多Java相关的集成Demo，让我来做你的百宝袋吧。

> 🐾我是笑小枫，全网皆可搜的【[笑小枫](https://www.xiaoxiaofeng.com)】
