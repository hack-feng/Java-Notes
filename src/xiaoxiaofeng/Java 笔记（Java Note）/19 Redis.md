# Redis 简介 #

 *  高性能的 key-value 内存数据库（Redis 能读的速度是 110000 次/s，写的速度是 81000 次/s），用 C 语言编写
 *  Redis 高性能的三个因素：纯内存存储；IO 多路复用技术；单线程架构
 *  支持数据的持久化，可以将内存中的数据保持在磁盘中，重启的时候可以再次加载进行使用
 *  Redis 的所有操作都是**原子性**的，同时 Redis 还支持对几个操作全并后的原子性执行
 *  支持 publish/subscribe、通知、key 过期等等特性
 *  Redis 的键值支持的数据类型：string（字符串），hash（哈希），list（列表），set（集合）及 zset（有序集合）、BitMap（位图）、HyperLogLog、Geo（地理位置）
 *  Redis 内置了复制（Replication），LUA 脚本（Lua scripting），LRU 驱动事件（LRU eviction），事务（Transactions）和不同级别的持久化（Persistence），并通过 Redis 哨兵（Sentinel）和自动分区（Cluster）提供高可用性（High Availability）
 *  Redis 使用场景：缓存、排行榜、计数器、社交网络（赞/踩、粉丝、共同好友/喜好等）、消息队列

> 
> 为什么 Redis 是单线程的？
> 因为 Redis 是基于内存的操作，CPU 不是 Redis 的瓶颈，Redis 的瓶颈最有可能是机器内存的大小或者网络带宽
> 单线程容易实现，而且避免了不必要的上下文切换和竞争条件
> 
> 
> 单线程带来的问题：如果某个命令执行过长， 会造成其它命令的阻塞
> 

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414447583.png) 
图 1 Redis的5种数据结构

# Redis 常用命令 #

## Redis 连接 ##

 *  使用 Redis 客户端连接 Redis（cmd 命令）：`redis-cli -h host -p port -a password`
 *  auth password：验证密码是否正确（需修改配置项：requirepass password）
 *  ping：查看服务是否运行（Redis 的默认监听端口为 6379）
 *  echo message：打印字符串
 *  ping：查看服务是否运行
 *  quit：关闭当前连接
 *  client id：返回当前连接的 ID（每个连接 ID 永不重复且单调递增）
 *  select index：切换到指定的数据库（默认 16 个库，默认使用的数据库是 0 号库）

## Redis 服务器 ##

 *  client kill \[ip:port\] \[id client-id\]：关闭客户端连接
 *  client getname：获取连接的名称
 *  info \[server|clients|memory|persistence|stats|replication|cpu|commandstats|cluster|keyspace\]：返回关于 Redis 服务器的各种信息和统计数值，section 可选值
 *  flushall：删除所有数据库的所有 key
 *  flushdb：删除当前数据库的所有 key
 *  save：异步保存数据到硬盘

## Key（键） ##

 *  keys pattern：查找所有符合给定模式（pattern）的 key
 *  dbsize：返回当前数据库中 key 的总数
 *  exists key：检查给定 key 是否存在，如果存在则返回 1， 不存在则返回 0
 *  del key：在 key 存在时删除 key，返回被删除 key 的数量
 *  expire key seconds：key 在 seconds 秒后过期
 *  expireat key timestamp：key 在秒级时间戳 timestamp 后过期
 *  persist key：将 key 的过期时间清除
 *  ttl key：以秒为单位返回 key 的剩余生存时间（time to live），当 key 不存在时，返回 -2，当 key 存在但没有设置剩余生存时间时，返回 -1
 *  rename key newkey：修改 key 的名称
 *  renamenx key newkey：仅当 newkey 不存在时，将 key 改名为 newkey
 *  type key：返回 key 所储存的值的类型
 *  move key db：将当前数据库的 key 移动到给定的数据库 db 当中
 *  scan cursor \[match pattern\] \[count number\]：采用渐进式遍历当前数据库中的数据库键，cursor 是一个游标，第一次遍历从 0 开始，每次 scan 遍历完都会返回当前游标的值（相关命令：sscan、hscan、zscan）
 *  键名：`业务名:对象名:id:[属性]`
 *  迁移键：migrate 命令

## String（字符串） ##

 *  可以存储字符串、整数、浮点数、二进制，值最大不能超过 512MB
 *  set key value \[ex seconds\] \[px milliseconds\] \[nx|xx\]：设置指定 key 的值
 *  setnx key value：**只有在 key 不存在时**设置 key 的值，设置成功返回 1，设置失败返回 0
 *  setex key seconds value：为指定的 key 设置值及其过期时间，如果 key 已经存在，将会替换已有的值
 *  mset key value \[key value …\]：批量设置值
 *  get key：获取指定 key 的值，如果要获取的键不存在， 则返回 nil
 *  mget key \[key …\]：批量获取值
 *  getset key value：将给定 key 的值设为 value，并返回 key 的旧值
 *  getrange key start end：返回 key 中字符串值的子字符
 *  incr key：将 key 中储存的数字值增一
 *  incrby key increment：将 key 所储存的值加上给定的增量值（increment）
 *  incrbyfloat key increment：将 key 所储存的值加上给定的浮点增量值（increment）
 *  decr key：将 key 中储存的数字值减一
 *  decrby key decrement：key 所储存的值减去给定的减量值（decrement）
 *  strlen key：返回 key 所储存的字符串值的长度
 *  append key value：如果 key 已经存在并且是一个字符串，append 命令将 value 追加到 key 原来的值的末尾
 *  使用场景：缓存（存储 JSON 格式的对象）、计数（优酷视频点赞）、分布式锁、共享 Session、限速（短信验证码限速）

## Hash（哈希、字典） ##

 *  键值对集合，一个 String 类型的 field 和 value 的映射表
 *  hset key field value：将哈希表 key 中的 field 的值设为 value
 *  hget key field：获取存储在哈希表中指定 field 的值
 *  hmset key field1 value1 \[field2 value2\]：同时将多个 field-value 对设置到哈希表 key 中
 *  hmget key field1 \[field2\]：获取所有给定 field 的值
 *  hdel key field2 \[field2\]：删除哈希表中一个或多个 field
 *  hexists key field：查看哈希表 key 中指定的 field 是否存在
 *  hincrby key field increment：为哈希表 key 中的指定 field 的整数值加上增量 increment
 *  hlen key：获取哈希表中 field 的个数
 *  hkeys key：获取哈希表中所有的 field
 *  hvals key：获取哈希表中所有的 value
 *  hgetall key：获取哈希表中所有的 field 和 value
 *  hscan key cursor \[match pattern\] \[count count\]：迭代哈希键中的键值对，第一次遍历 cursor 从 0 开始
 *  使用场景：存储、读取、修改对象属性

## List（列表） ##

 *  **双向链表**结构，按照插入顺序排序，可以添加一个元素到列表的头部（左边）或者尾部（右边），一个列表最多可以存储 232-1 个元素
 *  对列表两端插入（push）和弹出（pop），获取指定范围的元素列表、获取指定索引下标的元素等
 *  rpush key value1 \[value2\]：从右边插入元素
 *  lpush key value1 \[value2\]： 从左边插入元素
 *  linsert key before|after pivot value：向某个元素前或者后插入元素
 *  lpop key：从列表左侧弹出元素
 *  rpop key：从列表右侧弹出
 *  lindex key index：获取列表指定索引下标的元素（ 索引从 0 开始）
 *  lrange key start stop：获取列表指定范围内的元素
 *  llen key：获取列表长度
 *  lrem key count value：删除列表元素
 *  ltrim key start stop：对一个列表进行修剪，让列表只保留指定区间内的元素
 *  lset key index newValue：修改指定索引下标的元素
 *  blpop key \[key …\] timeout：阻塞式左侧弹出，当给定列表内没有任何元素可供弹出的时候，连接将被阻塞，直到等待超时或发现可弹出元素为止，timeout 为阻塞时间（单位：秒），如果 timeout=0，那么客户端一直阻塞等下去
 *  brpop key \[key …\] timeout：阻塞式右侧弹出
 *  使用场景：时间轴（微博 TimeLine）、消息队列（回帖、聊天记录、评论）、朋友圈点赞

## Set（集合） ##

 *  **哈希表**结构，无序集合，且成员不允许重复，一个集合最多可以存储 232-1 个元素
 *  支持集合内的增删改查，同时还支持多个集合取交集、并集、差集
 *  sadd key member1 \[member2\]：向集合添加一个或多个成员
 *  srem key member1 \[member2\]：移除集合中一个或多个成员
 *  scard key：获取集合的成员数
 *  sismember key member：判断 member 元素是否是集合 key 的成员
 *  smembers key：返回集合中的所有成员
 *  spop key：移除并返回集合中的一个随机元素
 *  srandmember key \[count\]：返回集合中一个或多个随机数
 *  sunion key1 \[key2\]：返回所有给定集合的并集
 *  sunionstore destination key1 \[key2\]：所有给定集合的并集存储在 destination 集合中
 *  sinter key1 \[key2\]：返回给定所有集合的交集
 *  sinterstore destination key1 \[key2\]：返回给定所有集合的交集并存储在 destination 中
 *  sdiff key1 \[key2\]：返回给定所有集合的差集
 *  sdiffstore destination key1 \[key2\]：返回给定所有集合的差集并存储在 destination 中
 *  sscan key cursor \[match pattern\] \[count count\]：迭代集合键中的元素，第一次遍历 cursor 从 0 开始
 *  使用场景：去重（统计访问网站的所有独立 IP）、生成随机数（抽奖）、共同好友（求交集）、好友推荐（求并集后再求差集）

## Sorted Set（有序集合） ##

 *  **跳跃表**结构，有序集合，且不允许重复的成员
 *  每个元素都关联一个 double 类型的**分数**，通过分数来为集合中的成员进行**从小到大**的排序（在分数相同的情况下，对成员按照二进制大小进行顺序）
 *  有序集合的成员是唯一的，但分数（score）可以重复
 *  zadd key score1 member1 \[score2 member2\]：向有序集合添加一个或多个成员，或者更新已存在成员的分数
 *  zrem key member \[member …\]：移除有序集合中的一个或多个成员
 *  zcard key：获取有序集合的成员数
 *  zcount key min max：计算在有序集合中指定区间分数的成员数
 *  zrange key start stop \[withscores\]：通过索引区间返回有序集合成指定区间内的成员
 *  zrevrange key start stop \[withscores\]：返回有序集中指定区间内的成员，通过索引，分数从高到底
 *  zscore key member：返回有序集合中指定成员的分数
 *  zrank key member：返回有序集合中指定成员的索引
 *  zrevrank key member：返回有序集合中指定成员的排名，有序集成员按分数值递减（从大到小）排序
 *  zincrby key increment member：有序集合中对指定成员的分数加上增量 increment
 *  zinterstore destination numkeys key \[key …\]：计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中
 *  zscan key cursor \[match pattern\] \[count count\]：迭代有序集合中的元素（包括元素成员和元素分值），第一次遍历 cursor 从 0 开始
 *  zpopmin key \[count\]：Remove and return members with the lowest scores in a sorted set
 *  zpopmax key \[count\]：Remove and return members with the highest scores in a sorted set
 *  bzpopmin key \[key …\] timeout：Remove and return the member with the lowest score from one or more sorted sets, or block until one is available
 *  bzpopmax key \[key …\] timeout：Remove and return the member with the highest score from one or more sorted sets, or block until one is available
 *  使用场景带有权重的元素，比如一个游戏的用户得分**排行榜**缓存淘汰算法：LRU（Least recently used 最近最少使用）、LFU（Least Frequently Used 最不经常使用）

## Bitmaps（位图） ##

 *  一个由二进制位组成的数组，数组的下标在 Bitmaps 中叫做偏移量
 *  可以对字符串的位进行操作（Bitmaps 实际类型为字符串类型）
 *  setbit key offset 1|0：设置或清除指定偏移量上的 bit 值（offset 从 0 开始）
 *  getbit key offset：获取指定偏移量上的 bit 值
 *  bitcount key \[start end\]：统计位图指定起止位置的值为“1”比特（bit）的位数
 *  bitop and|or|not|xor destkey key \[key … \]：对一个或多个位图进行比特位运算操作，并将结果保存在 destkey 中
 *  bitpos key 1|0 \[start\] \[end\]：获取位图中第一个 bit 值为 1或 0 的位置，可指定要检测的起止位置
 *  使用场景：统计网站独立访问用户（偏移量作为用户的 id）

## HyperLogLog ##

 *  HyperLogLog 是一种基数算法（ 实际类型为字符串类型），可以对**集合的基数**进行估算（一个带有 0.81% 标准错误的近似值）
 *  HyperLogLog 只能根据输入元素来计算基数，而不能存储输入元素本身
 *  每个 HyperLogLog 只占 12 KB 内存
 *  pfadd key element \[element …\]：向 HyperLogLog 添加元素
 *  pfcount key \[key …\]：返回 HyperLogLog（或多个 HyperLogLog 的并集）的近似基数
 *  pfmerge destkey sourcekey \[sourcekey .. . \]：将多个 HyperLogLog 合并为一个 HyperLogLog，并赋值给destkey

## Geo（地理位置） ##

 *  Geo 的数据类型为 zset，Redis 将所有地理位置信息的 geohash 存放在 zset 中
 *  geoadd key longitude latitude member \[longitude latitude member … \]：将指定的空间元素添加到指定的 key 里
 *  geopos key member \[member … \]：返回地理位置的经纬度
 *  geohash key member \[member … \]：返回地理位置的 geohash 字符串（geohash 将二维经纬度转换为一维字符串）
 *  geodist key member1 member2 \[unit\]：返回两个地理位置之间的距离，unit 为返回位置之间距离的单位，可以是 m（米）、km（千米）、mi（英里）、ft（英尺），默认为 m
 *  georadius key longitude latitude radius m|km|mi|ft：以给定的经纬度为中心，查询询指定半径内所有的地理位置元素的集合
 *  georadiusbymember key member radius m|km|mi|ft：以给定的位置元素为中心，查询指定半径内所有的地理位置元素的集合
 *  zrem key member：删除地理位置元素

## PUB/SUB（发布订阅） ##

 *  由发布者发布信息，存储到指定的频道上，然后订阅者根据自己订阅的频道接收信息
 *  目前 Redis 的发布订阅功能采取的是**发送即忘**（fire and forget）策略，不会对发布的消息进行持久化，因此新加入的订阅者，无法收到该频道之前的消息；当订阅事件的客户端断线时，会丢失所有在断线期间分发给它的事件
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414449201.png) 
    图 2 Redis发布订阅模型

 *  publish channel message：发布信息到指定的频道，返回订阅者个数
 *  subscribe channel \[channel . . . \]：订阅指定频道
 *  unsubscribe \[channel \[channel … \]\]：取消对指定频道的订阅
 *  psubscribe pattern \[pattern …\]：订阅符合给定模式的信息
 *  punsubscribe \[pattern \[pattern …\]\]：取消对所有给定模式信息的订阅
 *  pubsub channels \[pattern\]：查看当前的活跃频道（至少有一个订阅者的频道） ，订阅模式的客户端不计算在内
 *  pubsub numsub \[channel …\]：查看频道订阅数
 *  pubsub numpat：查看模式订阅数
 *  [键空间通知][Link 1]（keyspace notification）：使客户端可以通过订阅频道或模式， 来接收那些以某种方式改动了 Redis 数据集的事件；在默认配置下， 该功能处于关闭状态；对于每个修改数据库的操作，键空间通知都会发送两种不同类型的事件：键空间通知（以 `keyspace@<db>` 为前缀）、键事件通知（以 `keyevent@<db>` 为前缀）

## Streams ##

 *  xadd
 *  xrange
 *  xread

## 事务 ##

 *  通常的命令组合：watch … multi … exec
 *  将一组需要一起执行的命令放到 multi 和 exec 两个命令之间（multi 命令代表事务开始，exec 命令代表事务结束）
 *  事务中的命令出现**语法错误**，会造成整个事务无法执行
 *  事务中的命令在运行时，遇到某条命令**执行错误**，则在其它命令可以执行成功，因为 **Redis 不支持回滚**功能
 *  multi：标记一个事务块的开始，开始事务后，该客户端的命令不会马上被执行，而是**存放在一个队列**里，如果在这时执行一些返回数据的命令，结果都是返回 null
 *  exec：执行所有事务块内的命令，事务块内所有的命令都被执行时，返回所有命令执行的返回值；若事务被打断，返回 nil
 *  discard：取消事务，放弃执行事务块内的所有命令（在取消事务的同时，也会取消对 key 的监视）
 *  watch key \[key …\]：监视一个或多个 key，如果在事务执行之前，这个（或这些）key 被其它命令所改动，那么事务将被打断（类似乐观锁）
 *  unwatch：取消 watch 命令对所有 key 的监视
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414450518.png) 
    图 3 Redis事务执行过程

# Redis 内存 #

## Redis 内存回收策略 ##

 *  删除过期键对象
    
     *  惰性删除：主节点每次处理读取命令时，都会检查键是否超时，如果超时则执行 del 命令删除键对象，之后 del 命令也会异步发送给从节点
     *  定时任务删除：Redis 主节点在内部定时任务会**循环采样**一定数量的键，当发现采样的键过期时执行 del 命令，之后再同步给从节点
 *  内存溢出控制策略当 Redis 所用内存达到 maxmemory 上限时会触发相应的溢出控制策略（maxmemory-policy），Redis 支持 6 种策略：
    
     *  noeviction：**默认策略**，不会删除任何数据，拒绝所有写入操作并返回客户端错误信息
     *  volatile-lru：根据 LRU 算法删除设置了超时属性（expire）的键，直到腾出足够空间为止。如果没有可删除的键对象，回退到 noeviction 策略
     *  allkeys-lru：根据 LRU 算法删除键，不管数据有没有设置超时属性，直到腾出足够空间为止。
     *  volatile-random：随机删除过期键，直到腾出足够空间为止
     *  allkeys-random：随机删除所有键，直到腾出足够空间为止
     *  volatile-ttl：根据键值对象的 ttl 属性，删除最近将要过期数据。如果没有，回退到 noeviction 策略

## 内存配置优化 ##

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414451779.png) 
图 4 hash、list、set、zset内部编码配置

 *  ziplist 压缩编码的性能表现跟值长度和元素个数密切相关
 *  使用 ziplist 编码时，建议长度不要超过 1000，每个元素大小控制在 512 字节以内
 *  使用 intset 编码的集合时，尽量保持整数范围一致，如都在 int-16 范围内

# Redis 相关问题及解决方案 #

## 缓存雪崩 ##

 *  发生场景：设置缓存时采用了相同的过期时间，导致缓存在某一时刻同时失效，请求全部转发到 DB，DB 瞬时压力过重雪崩
 *  解决方案：
    
     *  为 key 设置不同的过期时间，如在原有的失效时间基础上增加一个 1-5 分钟随机值
     *  缓存预热：在上线时先将需要缓存的数据放到缓存中去

## 缓存穿透 ##

 *  发生场景：要查询的数据不存在，缓存无法命中所以需要查询数据库，如果从数据库查不到数据则不写入缓存，这将导致每次对该数据的查询都会去数据库查询
 *  解决方案：
    
     *  缓存空值：即使这条数据不存在也将其存储到缓存中去，设置一个较短的过期时间，并且可以做日志记录，寻找问题原因
     *  布隆过滤器拦截：预先将所有的 key 用布隆过滤器保存起来，然后过滤掉那些不存在的 key

### 布隆过滤器（Bloom Filter） ###

 *  用于检索一个元素是否在一个集合中，优点是空间效率和查询时间都比一般的算法要好的多，缺点是有一定的误识别率和删除困难
 *  布隆过滤器实际上是一个 bit 向量或者说 bit 数组，使用多个不同的哈希函数生成多个哈希值，并对每个生成的哈希值对应的 bit 位置 1
 *  核心思想
    
     *  使用多个哈希函数，增大随机性，减少 hash 碰撞的概率
     *  扩大数组范围，使 hash 值均匀分布，进一步减少 hash 碰撞的概率

## 缓存击穿 ##

 *  发生场景：缓存在某个时间点过期的时候，恰好在这个时间点对这个 key 有大量的并发请求过来，这些请求发现缓存过期都从 DB 加载数据并回设到缓存，这个时候大并发的请求可能会瞬间把 DB 压垮
 *  解决方案：
    
     *  让缓存永不过期
     *  定时去更新快过期的缓存
     *  使用互斥锁（mutex key）从 DB 加载数据

## 缓存预热 ##

 *  系统上线后，将相关的缓存数据直接加载到缓存系统，避免在用户请求的时候，先查询数据库，然后再将数据缓存的问题
 *  实现方法：在项目启动的时候自动进行加载；写一个隐秘的接口用于加载缓存

## 缓存更新 ##

 *  缓存更新策略：
    
     *  定时去更新快过期的缓存
     *  每次从缓存获取数据时获取不到时，再重新查询存放到缓存中
     *  主动更新：在真实数据更新之前以及更新之后，删除对应的缓存（双删）；利用**消息系统**或者其它方式通知缓存更新

## 处理 bigkey ##

 *  bigkey 是指 key 对应的 value 所占的内存空间比较大，如字符串类型单个 value 值超过 10KB，非字符串类型的元素个数超过 5000
 *  bigkey 的危害
    
     *  在 Redis Cluster 中，bigkey 会造成节点的内存空间使用不均匀
     *  操作 bigkey 比较耗时，容易阻塞 Redis
     *  每次获取 bigkey 产生的网络流量较大，容易造成网络拥塞
 *  如何发现：redis-cli —bigkeys
 *  解决方案：拆分数据结构，取值时不要把所有元素都取出来（hmget），将该 key 迁移到一个新的 Redis 节点上

# 持久化 #

 *  RDB 方式（默认）：通过快照（snapshotting）完成，当符合一定条件（在指定的时间内被更改的键的个数大于指定的数值）时 Redis 会自动将**内存中的所有数据**生成**快照**并存储在 dump.rdb 文件（默认）中，也可以使用 bgsave 命令手动触发
 *  AOF（append only file）方式：每执行一条会更改 Redis 中的数据的命令，Redis 就会将该命令以追加的方式写入硬盘中的 AOF 文件；定期对 AOF 文件进行重写（把 Redis 进程内的数据转化为写命令同步到新 AOF 文件）
 *  比较：RDB 方式无法做到数据实时持久化/秒级持久化；Redis 加载 RDB 恢复数据远远快于 AOF 方式
 *  AOF 持久化开启且存在 AOF 文件时，优先加载 AOF 文件，否则加载 RDB 文件

# 主从复制 + 哨兵（Sentinel） #

## 主从复制 ##

 *  主节点负责写数据，从节点负责读数据（slave 支持只读模式且默认开启）
 *  复制过程
    
     *  从节点与主节点建立 socket 连接，从节点发送 ping 命令，主节点权限验证（如果主节点设置了 requirepass 参数）
     *  从节点向主节点发送 psync 命令，执行同步操作：
        
         *  **全量复制**（full resynchronization）：如果是第一次进行复制，主节点执行 bgsave 保存 RDB 文件到本地，**发送 RDB 文件给从节点**，从节点把接收的 RDB 文件保存在本地，然后清空自身旧数据开始**加载** RDB 文件；对于从节点开始接收 RDB 快照到接收完成期间，主节点会把这期间写命令数据保存在**复制客户端缓冲区**内，当从节点加载完 RDB 文件后，主节点再把缓冲区内的数据发送给从节点，保证主从之间数据一致性
         *  **部分复制**（partial resynchronization）：当主从节点之间网络出现中断，从节点再次连上主节点后，从节点会向主节点要求补发丢失的命令数据，主节点根据**偏移量**把**复制积压缓冲区**里的数据发送给从节点
     *  异步复制：主节点持续地把写命令发送给从节点，保证主从数据一致性

## 哨兵架构 ##

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414453640.png) 
图 5 Redis\_Sentinel架构

 *  Redis Sentinel 是一个分布式架构，其中包含若干个（2n+1，至少 3 个）哨兵节点和 Redis 数据节点
 *  主从复制节点是数据节点，哨兵机制部署的节点是监控节点，它们都是 Redis 实例，但是哨兵节点不存储数据
 *  每个哨兵节点会对数据节点和其余哨兵节点进行**监控**，当它发现某个节点不可达时，会对节点做**下线标识**，如果被标识的是主节点，它还会和其它哨兵节点进行“**协商**”
 *  当**超过半数**哨兵节点都认为主节点不可达时，它们会**选举**出一个哨兵节点来完成自动故障转移的工作，同时会将这个变化实时通知给 Redis 客户端

# 集群（Cluster） #

 *  可以使用 redis-trib.rb 工具搭建集群
 *  节点通信：集群内部节点通信采用 Gossip（流言）协议彼此发送消息，节点定期不断发送和接受 ping/pong 消息来维护更新集群的状态（集群中的每个节点都会单独开辟一个 TCP 通道，用于节点之间彼此通信，通信端口号在基础端口上加 10000）
 *  Redis 集群采用的**数据分区**规则是 **Hash Slots**（哈希虚拟槽分区）：Redis 集群把所有的数据映射到 16384 个槽中，每个节点负责一定数量的槽，即先对每个 key 计算 CRC16 值，再对 16384 取模，将 key 映射到一个编号在 0~16383 之间的槽内（slot = CRC16(key) % 16384），然后把键值对存放到对应范围的节点上
 *  集群功能限制：
    
     *  **不支持**在多个节点上执行 mset、mget 等批量操作
     *  在不同的节点上无法多个 key 使用事务功能
     *  数据库只能使用 db0
 *  集群伸缩通过**在节点之间移动槽和相关数据**实现
 *  请求路由
    
     *  节点接收到键命令时会判断相关的槽是否由自身节点负责，如果不是则**返回重定向信息**（重定向信息包含了键所对应的槽以及负责该槽的节点地址）
     *  使用 Smart 客户端操作集群时，客户端**内部负责计算维护** 键→槽→节点 的映射，用于快速定位键命令到目标节点
     *  重定向分为 MOVED 和 ASK：ASK 说明集群正在进行槽数据迁移，客户端只在本次请求中做临时重定向，不会更新本地槽缓存；MOVED 重定向说明槽已经明确分派到另一个节点，客户端需要更新槽节点缓存
 *  集群自动故障转移
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414454491.png) 
    图 6 6节点Redis集群连接示意图

# Redis 的 Java client #

 *  Jedis：A blazingly small and sane redis java client
 *  Lettuce：Advanced Redis client for thread-safe sync, async, and reactive usage. Supports Cluster, Sentinel, Pipelining, and codecs.
 *  Redisson：distributed and scalable Java data structures on top of Redis server

## Jedis 客户端的使用 ##

 *  Jedis 不是线程安全的
 *  通过 JedisPoolConfig 配置 JedisPool，通过 JedisPool 获得 Jedis 实例
 *  直接使用 Jedis 中的用法
 *  JedisSentinelPool、JedisCluster

``````````java
JedisPoolConfig jedisPoolConfig = new JedisPoolConfig(); 
  JedisPool jedisPool = new JedisPool(jedisPoolConfig(), host); 
  try (Jedis jedis = jedisPool.getResource()) { 
      // Jedis API 
  }
``````````

# RedisTemplate 常用操作 #

``````````java
// 获取字符串操作接口 
  redisTemplate.opsForValue(); 
  // 获取散列操作接口 
  redisTemplate.opsForHash(); 
  // 获取列表操作接口 
  redisTemplate.opsForList(); 
  // 获取集合操作接口 
  redisTemplate.opsForSet(); 
  // 获取有序集合操作接口 
  redisTemplate.opsForZSet(); 
  // 获取基数操作接口 
  redisTemplate.opsForHyperLogLog(); 
  // 获取地理位置操作接口 
  redisTemplate.opsForGeo(); 
  
  // 获取绑定键的操作类，可以对某个键的数据进行多次操作 
  // 获取字符串绑定键操作接口 
  redisTemplate.boundValueOps("string"); 
  // 获取散列绑定键操作接口 
  redisTemplate.boundHashOps("hash"); 
  // 获取列表（链表）绑定键操作接口 
  redisTemplate.boundListOps("list"}; 
  // 获取集合绑定键操作接口 
  redisTemplate.boundSetOps("set"); 
  // 获取有序集合绑定键操作接口 
  redisTemplate.boundZSetOps("zset"); 
  // 获取地理位置绑定键操作接口 
  redisTemplate.boundGeoOps("geo"); 
  
  
  // 向 redis 里存入数据并设置缓存时间 
  stringRedisTemplate.opsForValue().set("test", "100", 60 * 10, TimeUnit.SECONDS); 
  
  // 根据 key 获取缓存中的 val 
  stringRedisTemplate.opsForValue().get("test"); 
  
  // val 做 -1 操作 
  stringRedisTemplate.boundValueOps("test").increment(-1); 
  stringRedisTemplate.opsForValue().increment("test", -1); 
  
  // val 做 +1 操作 
  stringRedisTemplate.boundValueOps("test").increment(1); 
  
  // 向指定 key 中存放 set 集合 
  stringRedisTemplate.opsForSet().add("red_123", "1", "2", "3"); 
  
  // 根据 key 查看集合中是否存在指定数据 
  stringRedisTemplate.opsForSet().isMember("red_123", "1"); 
  
  // 根据 key 获取 set 集合 
  stringRedisTemplate.opsForSet().members("red_123"); 
  
  // 检查 key 是否存在，返回 boolean 值 
  stringRedisTemplate.hasKey("546545"); 
  
  // 根据 key 删除缓存 
  stringRedisTemplate.delete("test"); 
  
  // 设置过期时间 
  stringRedisTemplate.expire("red_123", 1000, TimeUnit.MILLISECONDS); 
  
  // 根据 key 获取过期时间 
  stringRedisTemplate.getExpire("test"); 
  
  // 根据 key 获取过期时间并换算成指定单位 
  stringRedisTemplate.getExpire("test", TimeUnit.SECONDS); 
  
  // 使用 RedisCallback 回调接口，在同一条连接下执行多个 Redis 命令 
  stringRedisTemplate.execute(new RedisCallback() { 
      @Override 
      public Object doInRedis(RedisConnection connection) 
              throws DataAccessException { 
          connection.set("key1".getBytes(), "value1".getBytes()); 
          connection.hSet("hash".getBytes(), "field".getBytes(), "hvalue".getBytes()); 
          return null; 
      } 
  }); 
  
  // 使用 SessionCallback 回调接口，在同一条连接下执行多个 Redis 命令（推荐），可用于实现事务 
  stringRedisTemplate.execute(new SessionCallback() { 
      @Override 
      public Object execute(RedisOperations operations) { 
          // 设置要监控 key1 
          operations.watch("key1"); 
          // 开启事务，在 exec 命令执行前，全部都只是进入队列 
          operations.multi(); 
          operations.opsForValue().set("key2", "value2"); 
          // 获取值将为 null，因为 Redis 只是把命令放入队列 
          Object value2 = operations.opsForValue().get("key2"); 
          // 执行 exec 命令，将先判别 key1 是否在监控后被修改过，如果是不执行事务，否则执行事务 
          return operations.exec(); 
      } 
  }); 
  
  // 开启事务支持，默认关闭，关闭时每个操作使用的是不同的连接 
  stringRedisTemplate.setEnableTransactionSupport(true);
``````````

> 
> By default, transaction Support is disabled and has to be explicitly enabled for each `RedisTemplate` in use by setting `setEnableTransactionSupport(true)`. Doing so forces binding the current `RedisConnection` to the current `Thread` that is triggering `MULTI`. If the transaction finishes without errors, `EXEC` is called. Otherwise `DISCARD` is called. Once in `MULTI`, `RedisConnection` queues write operations. All `readonly` operations, such as `KEYS`, are piped to a fresh (non-thread-bound) `RedisConnection`.
> 
> 
> https://docs.spring.io/spring-data/redis/docs/current/reference/html/
> 

# Redis Repository #

 *  @EnableRedisRepositories
 *  实体注解：@RedisHash、@Id、@Indexed

# 基于 Redis 实现的分布式锁 #

``````````java
// 加锁 
  // 当 key 不存在时进行 set 操作，若 key 已经存在则不做任何操作 
  // value 为 requestId，表示加锁的客户端 
  // 设置锁过期时间，防止死锁的产生 
  // 返回 true 表示加锁成功，返回 false 表示加锁失败 
  // SET resource_name my_random_value NX EX 30 
  Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, requestId, 30L, TimeUnit.SECONDS); 
  
  // 释放锁 
  // 获取 key 的值，判断加锁的是不是当前客户端，如果是再释放锁，即删除该 key 
  // Lua 脚本在 Redis 中是原子执行的 
  // 通过 execute 方法执行脚本：首先直接传该 Lua 脚本的 SHA1 值，如果在 Redis 中找不到缓存的 Lua 脚本导致报错，则在程序 catch 该错误，把整个脚本序列化后传入 Redis 进行执行 
  String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end"; 
  RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class); 
  // 返回 1 表示释放锁成功，返回 0 表示释放锁失败 
  Long result = stringRedisTemplate.execute(redisScript, Collections.singletonList(lockKey), Collections.singletonList(requestId));
``````````

 *  使用 [Redisson][] 实现分布式锁：可重入锁、联锁、红锁、读写锁、信号量、闭锁


[Redis_5]: https://static.sitestack.cn/projects/sdky-java-note/e24d1d5399e6f8a368e7d9160b691cc9.png
[Redis]: https://static.sitestack.cn/projects/sdky-java-note/153396b953912da3e3b537845584011c.png
[Link 1]: http://redisdoc.com/topic/notification.html
[Redis 1]: https://static.sitestack.cn/projects/sdky-java-note/1bc37ce0618f6cd0a170500a15b4a4dd.png
[hash_list_set_zset]: https://static.sitestack.cn/projects/sdky-java-note/0026092ffbda3321f2e8b5da31f1ff42.png
[Redis_Sentinel]: https://static.sitestack.cn/projects/sdky-java-note/748cab2d23fbab3b0f30459626e3063c.png
[6_Redis]: https://static.sitestack.cn/projects/sdky-java-note/7391b78d0322b142fdb12c008ccee1e3.png
[Redisson]: https://github.com/redisson/redisson/wiki/8.-分布式锁和同步器