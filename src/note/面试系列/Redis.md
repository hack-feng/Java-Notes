### 1、什么时redis？简单介绍一下。

Redis本质上时一个Key-Value类型的内存数据库。
整个数据库统统加载再内存中进行操作，
定期的通过异步操作把数据库数据flush到硬盘上进行保存。

### 2、Redis支持哪些数据类型？

String、List、Set、Sorted Set、hashes

### 3、你在项目中什么场景下使用Redis？

* 会话缓存：用户登录或注册时的验证码存储，用户登录后，token的缓存。
* 热点数据缓存：经常被查询的数据，如热门商品信息、热门话题等。可以直接保存到redis中，加快用户的访问速度。
* 排行榜：投票前十名，榜单前十名等等。
* 计数器：由于redis时单线程的，可以用于生成递增或递减的数据。例如分布式中id递增编号计数，库存信息减少等。
* 分布式锁：主要利用redis的setnx命令。setnx（set if not exists）就是不存在的时候，则成功设置缓存，返回1，否则返回0。当然可以根据具体业务设置一个过期时间，防止程序没用成功解锁而引起死锁。
* 队列：由于redis有list push和list pop命令，所有可以执行队列的操作。
* 发布/订阅：这个简单的场景可以使用，建议使用

### 4 Redis key的过期时间和永久有效怎么设置？
过期时间:expire
永久有效：persist

### 5、什么是缓存穿透、缓存雪崩、缓存击穿？
* 缓存穿透

一般的缓存系统，都是按照key去缓存数据，如果不存在对应的value，则会去后端查找（例如DB），一些恶意请求故意对不存在的key值，发起大量请求，对系统造成很大的压力。

如何避免：
1. 对查询结果为空的情况也进行缓存，缓存时间设置的相对短一些。
2. 布隆过滤器：将所有有可能存在的数据放在一个足够大的bitMap中，一个一定不存在的数据会被这个bitMap拦截掉。
3. 对一定不存在的key进行过滤，比如递增id，如果传递的参数小于0，直接返回失败。

* 缓存雪崩

当服务器重启或者在同一段时间大量缓存失效，这种情况下，会导致大量请求直接请求到后端，导致后端系统压力过大、崩溃。

如何避免：
1. 不同的key，设置不同的过期时间，避免同一时间点，大量缓存失效。
2. 做二级缓存，A1为原始数，A2为拷贝数据，A1失效时，可以访问A2，A1失效时间为短期，A2失效时间为长期。

* 缓存击穿

对于一个设置了过期时间的key，缓存在过期的时候，这个key有大量的请求并发访问，会穿透缓存，直接访问后端程序，导致崩溃。

如何避免：
1. 使用互斥锁，当缓存失效时，不立即load db，先使用redis的setnx设置一个互斥锁，当操作成功返回时执行load db的操作并设置缓存，否则重试get缓存的方法。
2. 永远不过期：设置物理不过期，逻辑上过期（后台异步线程去刷新）

### 6、Redis集群模式如何实现？主从复制如何实现？

## 7、Redis的持久化是怎么实现的?
* RDB(Redis DataBase)：在不同的时间点将Redis的数据生成的快照同步到磁盘等介质上。

  优点：内存到磁盘的快照，定期更新
  
  缺点:耗时、耗性能（fork+io操作），容易丢失数据
  
* AOF(Append Only File)：将redis所执行过的命令都保存下来，在下次redis重启时，只需要执行指令就可以了

  优点：写日志
  
  缺点：体积大，恢复速度慢
 
 * bgsave 做镜像全量持久化。AOF做增量持久化。因为bgsave会消耗比较长的时间，不够实时，在停机的时候会丢失大量数据，需要AOF来配合。
 
 Redis实例重启时优先使用AOF来恢复内存状态，如果没有AOF日志，就会使用RDB文件来恢复。Redis会定期做AOF重写。
  
 ### 8、Redis的过期键策略有哪些？
 
 * 定时过期：一个key一个定时器
 * 惰性过期：只用使用key时才判断key是否过期，过期则清除。
 * 定期过期：定时过期和惰性过期折中。
 
 ### 9、Redis的回收策略（淘汰策略）
 
 * volatile-lru:从已设置过期时间的数据集（server.db[i].expires）中挑选最近最少使用的数据淘汰
 * volatile-ttl:从已设置过期时间的数据集（server.db[i].expires）中挑选将要过期的数据淘汰
 * volatile-random:从已设置过期时间的数据集（server.db[i].expires）中随机挑选数据淘汰
 * allKeys-lru:从所有数据集（server.db[i].dict）中挑选最近最少使用的数据淘汰
 * allKeys-random:从已设置过期时间的数据集（server.db[i].dict）中随机挑选数据淘汰
 * no-enviction:禁止驱逐数据
 
 使用策略：
 1. 如果数据呈现幂律分布，也就是一部分数据访问高，一部分访问少，则使用allKeys-lru策略
 2. 如果数据呈现平等分布，也就是所有的数据访问机会均等，则使用allKeys-random策略
 
 ### 10、知道LRU算法吗？
 
 LRU: new LinkedHashMap<K, V>(capacity, DEFAULT_LOAD_FACTORY, true);
 
 第三个参数为true，代表linkedList按访问顺序排序，可做LRU缓存，设为false则代表按插入顺序排序，可做FIFO缓存。
 
 LRU算法实现：
 
 1. 通过双向链表来实现，新数据插入到链表的头部；
 2. 每当缓存命中（即缓存中的数据被访问），则将数据移至链表头部；
 3. 当链表满了之后，遗弃尾部的数据。
 
 ### 11、假如Redis里面有一亿个key，其中10W个key以已知固定的前缀开头，如何将他们全部查出来？
 
 使用key指令可以找出指定模式的key列表；
 
 但是redis是单线程的，如果是线上的服务器，keys命令会导致服务器堵塞，直至取出全部的数据。
 这种情况可以使用scan指令，但是scan指令会有一定的概率取出重复的数据，在程序中进行一次去重就可以了。
 scan整体的时间会大于keys执行的时间但是不会造成redis堵塞。
 
 ### 12、Redis常见性能优化和解决方案
 1. Master最好不要写内存快照，如果Master写内存快照，save命令调度rdbSave函数，会堵塞主线程的工作，当快照比较大的时对性能影响比较大。
 2. 如果数据比较重要，某个Slave开启AOF备份，策略设置为每秒同步一次；
 3. 为了增加主从复制的速度和连接的稳定性，Master和Slave尽量在同一个局域网内；
 4. 尽量避免在压很大的主库上增加从库
 5. 主从复制尽量用单向链表的结构更为稳定。Master <- slave1 <- slave2 <- slave3...这样的结构方便解决单点故障的问题，实现slave对master的替换。
 
 ### 13、使用过redis做过队列吗？
 
 一般使用list的数据结构，使用rpush生产消息，使用lpop消费消息，如果lPop时消息为空，要适当的sleep一段时间。
 
 > 如果对方问，可不可不使用sleep呢?
 
 redis里面有一个blPop命令，如果队列中的消息为空时，队列将会发生堵塞，直到rPush进新的消息。
 
 > 可不可生产一个消息，多个消费者消费呢？
 
 可以使用redis的pub/sub 主题订阅者模式，可以实现1:N的消息队列
 
 > pub/sub模式有什么缺点？
 
 在消费者下线的情况下，生产的消息会发生丢失。可以使用专业的消息队列，如RabbitMq等
 
 > redis如何实现延时队列？
 
 可以使用sortedSet，使用时间戳作为scope，消息内容作为key调用zAdd来上产消息，消费者使用zRangeByScope指定来获取N秒之前的数据进行轮询进行处理。
 
 
 ### 14、使用过redis做分布式锁吗？
 
 使用redis的setNx来争抢锁，抢到之后，使用expire设置一个过期时间防止没有释放，出现死锁
 
 > 如果在setNx拿到锁之后，设置expire过期时间之前，进程crash或者重启怎么办？
 
 可以将setNx和expire当作一条指令进行执行。
 
 加锁：
 ~~~
 SET lock_key random_value NX PX 5000
 ~~~
 
 * lock_key：分布式锁的名称
 * random_value：客户端生成的唯一字符，保证锁的一致性
 * NX：代表只有键存在时，才能对键进行操作
 * PX 5000：设置键的过期时间为5s
 
 解锁(Lua脚本)：
 ~~~
 if redis.call("get", KEYS[1]) == ARGV[1] then
    return redis.call("del", KEYS[1])
 else
    return 0
 end
 ~~~
 
 ### java中实现分布式锁
 ~~~java
 class Redis{
    /**
      * setNX
      * @param lockKey  锁
      * @param value   请求标识
      * @param exptime 过期时间
      * @return 是否获取到锁
      */
     public boolean tryGetDistributedRedisLock( String lockKey, Serializable value, final long exptime) {
         return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
             @Override
             public Boolean doInRedis(RedisConnection connection) {
                 RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
                 RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                 Object obj = connection.execute("set", keySerializer.serialize(lockKey),
                         valueSerializer.serialize(value),
                         SafeEncoder.encode("NX"),
                         SafeEncoder.encode("EX"),
                         Protocol.toByteArray(exptime));
                 return obj != null;
             }
         });
     }
 
     /**
      * 释放锁
      */
     private  final Long RELEASE_SUCCESS = 1L;
 
     /**
      * 释放分布式锁
      * @param lockKey 锁
      * @param requestId 请求标识
      * @return 是否释放成功
      */
     public  boolean releaseDistributedRedisLock(String lockKey, String requestId) {
         String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
         Object result = redisTemplate.execute(new DefaultRedisScript(script,Long.class), Collections.singletonList(lockKey), requestId);
         return RELEASE_SUCCESS.equals(result);
     }
 }
 ~~~