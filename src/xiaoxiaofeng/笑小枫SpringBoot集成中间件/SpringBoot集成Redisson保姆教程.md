## 1. SpringBoot集成Redis

关于Redis的安装，这里就不重复介绍了，需要的朋友可以看我之前的博文[Redis多系统安装（Windows、Linux、Ubuntu）](https://www.xiaoxiaofeng.com/archives/redis)

Redis原生命令大全，作者整理的很详细，大部分命令转化为java命令基本也是关键词

[Redis 命令参考](http://redisdoc.com/index.html)

接下来开始我们的正题，一起学习下，SpringBoot整合Redis

### 1.1 引入依赖

pom文件不贴全部代码了，依赖有些多了，占据的篇幅过大，查看全部可以去看本文的源码

* pom.xml
~~~xml
<!-- 引入redis依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!-- 引入redis连接池的依赖 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
~~~

### 1.2 Redis配置

因为我这里redis没有配置密码，所以password注释掉了，你们配置可以直接放开注释即可🐾

* 添加application.yml 配置

~~~yml
server:
  port: 8080
  
spring:
  redis:
    database: 0
    host: 127.0.0.1
    port: 6379
    # password:
    timeout: 5000
    lettuce:
      pool:
        max-active: 32
        max-wait: -1
        max-idle: 16
        min-idle: 8
~~~

* 在config包下添加`RedisConfig.java` 配置类👇
~~~java
package com.maple.redis.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author 笑小枫
 * @date 2022/07/19
 **/
@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        //使用fastjson序列化
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        // value值的序列化采用fastJsonRedisSerializer
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
~~~

### 1.3 Redis工具类

配置完配置，其实我们的Redis就已经集成了，SpringBoot的starter是真的香，后面我们会讲解一下如何制作我们自己的starter。

下面写一个redis常用的工具类，在util包下创建`RedisUtil.java`类👇

~~~java
package com.maple.redis.util;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis常用的一些操作
 *
 * @author 笑小枫
 * @date 2022/07/19
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 写入缓存
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更新缓存
     */
    public boolean getAndSet(final String key, String value) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().getAndSet(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 批量删除对应的value
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     */
    public void removePattern(final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     */
    public void remove(final String key) {
        if (exists(key) != null && exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     */
    public Boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     */
    public Object get(final String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 哈希 添加
     */
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 哈希获取数据
     */
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 列表添加
     */
    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    /**
     * 列表获取
     */
    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    /**
     * 集合添加
     */
    public void addSet(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key, value);
    }

    /**
     * 删除集合下的所有值
     */
    public void removeSetAll(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        Set<Object> objectSet = set.members(key);
        if (objectSet != null && !objectSet.isEmpty()) {
            for (Object o : objectSet) {
                set.remove(key, o);
            }
        }
    }

    /**
     * 判断set集合里面是否包含某个元素
     */
    public Boolean isMember(String key, Object member) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.isMember(key, member);
    }

    /**
     * 集合获取
     */
    public Set<Object> setMembers(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 有序集合添加
     */
    public void zAdd(String key, Object value, double source) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, source);
    }

    /**
     * 有序集合获取指定范围的数据
     */
    public Set<Object> rangeByScore(String key, double source, double source1) {
        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
        return zSet.rangeByScore(key, source, source1);
    }

    /**
     * 有序集合升序获取
     */
    public Set<Object> range(String key, Long source, Long source1) {
        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
        return zSet.range(key, source, source1);
    }

    /**
     * 有序集合降序获取
     */
    public Set<Object> reverseRange(String key, Long source, Long source1) {
        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
        return zSet.reverseRange(key, source, source1);
    }
}
~~~

### 1.4 测试一下吧

编写我们的测试类

~~~java
package com.maple.redis.controller;

import com.alibaba.fastjson.JSON;
import com.maple.redis.util.RedisUtil;
import org.springframework.web.bind.annotation.*;

/**
 * @author 笑小枫
 * @date 2022/7/20
 */
@RestController
@RequestMapping("/redis")
public class TestRedisController {

    private final RedisUtil redisUtil;

    public TestRedisController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 插入String类型的数据到redis
     */
    @PutMapping("/insertStr")
    public void insertStr(String key, String value) {
        redisUtil.set(key, value);
    }

    /**
     * 根据key获取redis的数据
     */
    @PostMapping("/getStr")
    public String getStr(String key) {
        return String.valueOf(redisUtil.get(key));
    }

    /**
     * 根据key删除redis的数据
     */
    @DeleteMapping("/deleteStr")
    public Boolean deleteStr(String key) {
        redisUtil.remove(key);
        return redisUtil.exists(key);
    }

    /**
     * 模拟操作Map集合的数据
     */
    @PostMapping("/operateMap")
    public Object operateMap() {
        redisUtil.hmSet("maple:map", "xiaofeng", "笑小枫");
        return redisUtil.hmGet("maple:map", "xiaofeng");
    }

    /**
     * 模拟操作List集合的数据
     */
    @PostMapping("/operateList")
    public String operateList() {
        String listKey = "maple:list";
        redisUtil.lPush(listKey, "小枫");
        redisUtil.lPush(listKey, "小明");
        redisUtil.lPush(listKey, "小枫");
        return JSON.toJSONString(redisUtil.lRange(listKey, 0, 2));
    }

    /**
     * 模拟操作Set集合的数据
     */
    @PostMapping("/operateSet")
    public String operateSet() {
        String listKey = "maple:set";
        redisUtil.addSet(listKey, "小枫");
        redisUtil.addSet(listKey, "小明");
        redisUtil.addSet(listKey, "小枫");
        System.out.println("集合中是否包含小枫" + redisUtil.isMember(listKey, "小枫"));
        System.out.println("集合中是否包含小红" + redisUtil.isMember(listKey, "小红"));
        return JSON.toJSONString(redisUtil.setMembers(listKey));
    }

    /**
     * 模拟操作ZSet有序集合的数据
     */
    @PostMapping("/operateZSet")
    public String operateZSet() {
        String listKey = "maple:zSet";
        redisUtil.zAdd(listKey, "小枫", 8);
        redisUtil.zAdd(listKey, "小明", 1);
        redisUtil.zAdd(listKey, "小红", 12);
        redisUtil.zAdd(listKey, "大明", 5);
        redisUtil.zAdd(listKey, "唐三", 10);
        redisUtil.zAdd(listKey, "小舞", 9);
        // 降序获取source最高的5条数据
        return JSON.toJSONString(redisUtil.reverseRange(listKey, 0L, 4L));
    }
}
~~~

具体的返回结果我就不一一贴图了，自己建站，流量和网速永远都是一大诟病（哭穷🙈）

简单贴两张吧，怕你们说我敷衍😂😂

* 先调用接口插入一条数据`PUT http://localhost:8080/redis/insertStr?key=blog&value=xiaoxiaofeng`

* 调用接口查询刚刚插入的数据`POST http://localhost:8080/redis/getStr?key=blog`

可以看到返回的数据如下图所示：

![image-20230828164010737](https://image.xiaoxiaofeng.site/blog/2023/08/28/xxf-20230828164010.png?xxfjava)

## 2. SpringBoot集成Redisson

Redisson是一个在Redis的基础上实现的Java驻内存数据网格（In-Memory Data Grid）。它不仅提供了一系列的分布式的Java常用对象，还提供了许多分布式服务。

下文所有的内容基于上文的Redis配置。

### 2.1引入依赖

老规矩，pom文件只贴新增的

~~~xml
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-boot-starter</artifactId>
    <version>3.16.8</version>
</dependency>
~~~

### 2.2 Redisson配置

* 添加application.yml 配置，这里贴个全的yml吧

【注意】如果redis设置密码了的话，redisson的密码同步也改一下

~~~yml
server:
  port: 8080

spring:
  redis:
    database: 0
    host: 127.0.0.1
    port: 6379
    # password:
    timeout: 5000
    lettuce:
      pool:
        max-active: 32
        max-wait: -1
        max-idle: 16
        min-idle: 8
    # Redisson配置 spring.redisson.singleServerConfig
    redisson:
      config:
        singleServerConfig:
          # 连接空闲超时，单位：毫秒
          idleConnectionTimeout: 100000
          # 连接超时，单位：毫秒
          connectTimeout: 10000
          # 命令等待超时，单位：毫秒
          timeout: 3000
          # 命令失败重试次数
          retryAttempts: 3
          # 命令重试发送时间间隔，单位：毫秒
          retryInterval: 1500
          # 密码
          password: null
          # 单个连接最大订阅数量
          subscriptionsPerConnection: 5
          # 客户端名称
          clientName: null
          # 节点地址
          address: redis://${spring.redis.host}:${spring.redis.port}
          # 发布和订阅连接的最小空闲连接数
          subscriptionConnectionMinimumIdleSize: 1
          # 发布和订阅连接池大小
          subscriptionConnectionPoolSize: 50
          # 最小空闲连接数
          connectionMinimumIdleSize: 32
          # 连接池大小
          connectionPoolSize: 64
          # redis数据库编号
          database: 0
          # DNS监测时间间隔，单位：毫秒
          dnsMonitoringInterval: 5000
        # 线程池数量
        threads: 0
        # Netty线程池数量
        nettyThreads: 0
        # 编码
        codec:
          class: "org.redisson.codec.JsonJacksonCodec"
        # 传输模式
        transportMode: "NIO"
        # 配置看门狗的默认超时时间为30s，这里改为10s
        lockWatchdogTimeout: 10000
~~~

### 2.3 Redisson工具类

下面写一个redis常用的工具类，在util包下创建`RedissonUtil.java`类👇

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

### 2.4 测试一下吧

编写一个测试的controller，这里只简单的写几个了，不一一罗列了

~~~java
package com.maple.redis.controller;

import com.maple.redis.util.RedissonUtil;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RTransaction;
import org.redisson.transaction.TransactionException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 笑小枫  https://xiaoxiaofeng.com/
 * @date 2023/8/28
 */
@RestController
@RequestMapping("/redisson")
public class TestRedissonController {

    private final RedissonUtil redissonUtil;

    public TestRedissonController(RedissonUtil redissonUtil) {
        this.redissonUtil = redissonUtil;
    }

    /**
     * 插入String类型的数据到redis
     */
    @PutMapping("/insertStr")
    public void insertStr(String key, String value) {
        redissonUtil.set(key, value);
    }

    /**
     * 根据key获取redis的数据
     */
    @PostMapping("/getStr")
    public String getStr(String key) {
        return redissonUtil.get(key);
    }

    /**
     * 插入map
     */
    @PutMapping("/insertMap")
    public void insertMap(String key) {
        RMap<String, String> rMap = redissonUtil.getMap(key);
        rMap.put("key1", "xiaoxiaofeng");
        rMap.put("key2", "xiaoxiaofeng2");
        rMap.put("key3", "xiaoxiaofeng3");
    }

    /**
     * 获取map
     */
    @PostMapping("/getMap")
    public Map<String, String> getMap(String key) {
        RMap<String, String> rMap = redissonUtil.getMap(key);
        Set<String> set = new HashSet<>();
        set.add("key1");
        set.add("key3");
        return rMap.getAll(set);
    }

    /**
     * 分布式锁伪代码
     */
    @PostMapping("/getLock")
    public void getLock(String key) {
        RLock lock = redissonUtil.getLock(key);
        try {

            // 尝试加锁，最多等待5秒，上锁以后10秒自动解锁
            boolean isOk = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!isOk) {
                // 获取分布式锁失败，可以进行抛出获取锁失败的提示等，根据业务来
                System.out.println("获取分布式锁失败");
                return;
            }
            // 获取锁成功后，开始处理业务逻辑
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // 如果10s没有执行完，锁会自动释放，这里抛出异常
            Thread.currentThread().interrupt();
        } finally {
            // 如果10s没有执行完，锁会自动释放，这里抛出异常，操作失败，需要对数据回滚
            lock.unlock();
        }
    }

    /**
     * Reids事务演示
     */
    @PostMapping("/getTransaction")
    public void getTransaction() {
        RTransaction transaction = redissonUtil.getTransaction();
        RMap<String, String> map = transaction.getMap("myMap");
        map.put("1", "2");
        String value = map.get("3");
        RSet<String> set = transaction.getSet("mySet");
        set.add(value);
        try {
            transaction.commit();
        } catch (TransactionException e) {
            transaction.rollback();
        }
    }

}
~~~

启动项目，可以看到成功使用Redisson：

![image-20230828170354245](https://image.xiaoxiaofeng.site/blog/2023/08/28/xxf-20230828170354.png?xxfjava)

* 先调用接口插入一条数据`PUT http://localhost:8080/redisson/insertStr?key=redisson-blog&value=xiaoxiaofeng-redisson`

* 调用接口查询刚刚插入的数据`POST http://localhost:8080/redisson/getStr?key=redisson-blog`

可以看到返回的数据如下图所示：

![image-20230828170425875](https://image.xiaoxiaofeng.site/blog/2023/08/28/xxf-20230828170425.png?xxfjava)

在测试一下操作map的吧

* 先调用接口插入一条数据`PUT http://localhost:8080/redisson/insertMap?key=redisson-map`

* 调用接口查询刚刚插入的数据`POST http://localhost:8080/redisson/getMap?key=redisson-map`

可以看到返回的数据如下图所示：

![image-20230828170822792](https://image.xiaoxiaofeng.site/blog/2023/08/28/xxf-20230828170822.png?xxfjava)

### 2.5 水一个小标题

常用的还有获取递增的数据，在redisson的工具类中已经给出了，就不一一展开说了

还有Redisson实现分布式锁，在controller中给出了伪代码

同时插入多个redis缓存如何保证数据一致性等等。

更多的Redisson操作文档可以查看[https://www.bookstack.cn/books/redisson-wiki-zh](https://www.bookstack.cn/books/redisson-wiki-zh)

## 3. 笑小枫留言

使用Redis的过程中还会有很多问题，比如缓存数据一致性，缓存数据持久化，内存淘汰机制，缓存雪崩等等等，在面试的时候也经常会用到，博主整理了一份Redis常见的面试，感兴趣的朋友可以看下：

[【面试1v1实景模拟】Redis面试官会怎么提问？](https://www.xiaoxiaofeng.com/archives/interviewredis)

本文源码：[https://github.com/hack-feng/maple-product/](https://github.com/hack-feng/maple-product/)

其中`maple-redis`模块即为本文的Demo源码。需要的朋友可以看下。

感兴趣的朋友可以帮忙点个star⭐⭐⭐⭐⭐后续会有更多Java相关的集成Demo，让我来做你的百宝袋吧。



