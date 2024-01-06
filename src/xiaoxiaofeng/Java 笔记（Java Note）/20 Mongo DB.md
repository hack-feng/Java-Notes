 *  MongoDB 是一个基于分布式文件存储的数据库
 *  MongoDB 将数据存储为一个文档，数据结构由键值（key=>value）对组成

# NoSQL #

 *  NoSQL，指的是非关系型的数据库（NoSQL 有时也称作 Not Only SQL 的缩写）
 *  特点：没有声明性查询语言；没有预定义的模式；键 - 值对存储，列存储，文档存储，图形数据库；最终一致性，而非 ACID 属性；非结构化和不可预知的数据；CAP 定理；高性能、高可用性和可伸缩性
 *  优点：高可扩展性；分布式计算；低成本；架构的灵活性，半结构化数据；没有复杂的关系
 *  缺点：没有标准化；有限的查询功能；最终一致是不直观的程序
 *  主要 NoSQL 数据库分类

| 类型       | 部分代表               | 特点                                                         |
| ---------- | ---------------------- | ------------------------------------------------------------ |
| 键值存储   | MemcacheDB、Redis      | 可以通过 key 快速查询到其 value                              |
| 文档存储   | MongoDB、CouchDB       | 文档存储一般用类似 JSON 的格式存储，存储的内容是文档型的，可以对某些字段建立索引，实现关系数据库的某些功能 |
| 列存储     | Hbase、Cassandra       | 按列存储数据的，方便存储结构化和半结构化数据，方便做数据压缩，对针对某一列或者某几列的查询有非常大的 IO 优势 |
| 图存储     | Neo4J、FlockDB         | 图形关系的最佳存储                                           |
| 对象存储   | db4o、Versant          | 通过类似面向对象语言的语法操作数据库，通过对象的方式存取数据 |
| XML 数据库 | Berkeley DB XML、BaseX | 高效的存储 XML 数据，并支持 XML 的内部查询语法，比如 XQuery, Xpath |

## CAP 定理 ##

 *  在计算机科学中，CAP 定理（CAP theorem），又被称作布鲁尔定理（Brewer's theorem），它指出对于一个**分布式计算系统**来说，不可能同时满足以下三点：
    
     *  一致性（Consistency）：所有节点在同一时间具有相同的数据
     *  可用性（Availability）：保证每个请求不管成功或者失败都有响应
     *  分区容错（Partition tolerance）：发生网络分区时不会影响系统的继续运作
 *  CAP 理论的核心是：一个**分布式**系统不可能同时很好的满足一致性、可用性和分区容错性这三个需求，**最多只能同时较好的满足两个**
 *  因此，根据 CAP 原理将 NoSQL 数据库分成了满足 CA 原则、满足 CP 原则和满足 AP 原则三 大类：
    
     *  CA - 单点集群，满足一致性，可用性的系统，通常在可扩展性上不太强大
     *  CP - 满足一致性，分区容错性的系统，通常性能不是特别高
     *  AP - 满足可用性，分区容错性的系统，通常可能对一致性要求低一些
       
     
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414461186.png) 
    图 1 CAP定理

## BASE ##

BASE 理论是对 CAP 理论的**延伸**，核心思想是即使无法做到强一致性，但应用可以采用适合的方式达到最终一致性：

 *  Basically Availble（基本可用）：分布式系统在出现故障的时候，允许损失部分可用性，即保证核心可用
 *  Soft-state（软状态/柔性事务）：允许系统存在中间状态，而该中间状态不会影响系统整体可用性，即允许不同节点间副本同步的延时
 *  Eventual Consistency（最终一致性）：系统中的所有数据副本经过一定时间后，最终能够达到一致的状态

# MongoDB 简介 #

 *  一个基于分布式文件存储的数据库，由 C++ 语言编写，旨在为 Web 应用提供可扩展的高性能数据存储解决方案
 *  MongoDB 的优势：JSON 文档模型、动态的数据模式、二级索引强大、查询功能、自动分片、水平扩展、自动复制、高可用、文本搜索、企业级安全、聚合框架 MapReduce、大文件存储 GridFS
 *  MongoDB 现支持 3 种数据库引擎：**WiredTiger**（默认）、MMAPv1 和 In-Memory，前两种为硬盘存储引擎，后一种为内存存储引擎\*\*
 *  并发级别：MMAPv1 为集合级别锁；WiredTiger 通过 MVCC 实现文档级别的并发控制，即**文档级别锁**
 *  MongoDB 的不足：不支持事务操作（4.0 版本后支持多文档 ACID），占用磁盘空间过大

# MongoDB 操作 #

## 启动 MongoDB ##

 *  命令行下运行 MongoDB 服务器 或 配置 MongoDB 服务 **任选一个方式**启动
 *  可以通过 `mongod —help` 查看可配置的启动选项
 *  命令行下运行 MongoDB 服务器：mongod —port 27017 —dbpath "D:\\mongodb\\data\\db"
 *  [配置 MongoDB 服务][MongoDB]
    
     *  分别创建文件夹 data\\db 和 data\\log，创建配置文件 mongod.yaml（参考 [Configuration File Options][]）

``````````
systemLog: 
       destination: file 
       path: D:\mongodb\data\log\mongod.log 
       logAppend: true 
   storage.dbPath: D:\mongodb\data\db # 默认值为 \data\db 
   net: 
       bindIp: 127.0.0.1 
       port: 27017 
   security.authorization: enabled # 默认值为 disabled，即无授权可以直接访问数据库 
   #storage.inMemory.engineConfig.inMemorySizeGB: 256MB # 默认情况下将使用较大的值：50% 的 RAM 减去 1 GB 或者 256 MB（仅用于企业版）
``````````

 *  安装 MongoDB 服务：mongod.exe —config "C:\\mongodb\\mongod.yaml" —serviceName MongoDB —install
 *  启动 MongoDB 服务：net start MongoDB
 *  关闭 MongoDB 服务：net stop MongoDB
 *  移除 MongoDB 服务：mongod.exe —remove

## 连接 MongoDB ##

 *  mongodb://\[username:password@\]host1\[:port1\]\[, host2\[:port2\], …\[, hostN\[:portN\]\]\]\[/\[database\]\[?options\]\]，如 `mongodb://127.0.0.1:27017`
 *  连接 MongoDB 后默认切换到 test 数据库
 *  MongoDB Shell 是 MongoDB 自带的交互式 **Javascript shell**

## 数据库备份与恢复 ##

 *  导出数据库的数据到指定目录中：mongodump -h  <:port> -d dbname -o dbdirectory，如 `mongodump -h 127.0.0.1:27017 -d test -o c:\data\dump`
 *  恢复备份的数据：mongorestore -h  <:port> -d dbname  ，如 `mongorestore -h127.0.0.1:27017 -d test c:\data\dump\test`
    

## 管理用户和角色 ##

 *  mongodb 内置角色：
    
     *  数据库用户角色：read、readWrite;
     *  数据库管理角色：dbAdmin、dbOwner、userAdmin；
     *  集群管理角色：clusterAdmin、clusterManager、clusterMonitor、hostManager；
     *  备份恢复角色：backup、restore；
     *  所有数据库角色：readAnyDatabase、readWriteAnyDatabase、userAdminAnyDatabase、dbAdminAnyDatabase
     *  超级用户角色：root
     *  内部角色：\_\_system
 *  具体角色：
    
     *  read：允许用户读取指定数据库
     *  readWrite：允许用户读写指定数据库
     *  dbAdmin：允许用户在指定数据库中执行管理函数，如索引创建、删除，查看统计或访问 system.profile
     *  userAdmin：允许用户向 system.users 集合写入，可以找指定数据库里创建、删除和管理用户
     *  clusterAdmin：只在 admin 数据库中可用，赋予用户所有分片和复制集相关函数的管理权限
     *  readAnyDatabase：只在 admin 数据库中可用，赋予用户所有数据库的读权限
     *  readWriteAnyDatabase：只在 admin 数据库中可用，赋予用户所有数据库的读写权限
     *  userAdminAnyDatabase：只在 admin 数据库中可用，赋予用户所有数据库的 userAdmin 权限
     *  dbAdminAnyDatabase：只在 admin 数据库中可用，赋予用户所有数据库的 dbAdmin 权限
     *  root：只在 admin 数据库中可（超级账号，超级权限）
 *  创建管理员用户

``````````
use admin 
  db.createUser( 
    { 
      user: "myUserAdmin", 
      pwd: passwordPrompt(), // or cleartext password 
      roles: [ { role: "userAdminAnyDatabase", db: "admin" }, "readWriteAnyDatabase" ] 
    } 
  )
``````````

 *  创建其它用户

``````````
use test 
  db.createUser( 
    { 
      user: "myTester", 
      pwd:  passwordPrompt(),   // or cleartext password 
      roles: [ { role: "readWrite", db: "test" }, 
               { role: "read", db: "reporting" } ] 
    } 
  )
``````````

 *  在连接时认证

``````````
mongo --port 27017  --authenticationDatabase "admin" -u "myUserAdmin" -p
``````````

 *  在连接后认证

``````````
use admin 
  db.auth("myUserAdmin", passwordPrompt()) // or cleartext password
``````````

# MongoDB 术语 #

### 数据库（database） ###

 *  特殊作用的数据库：admin、local、config

### 集合（collection） ###

 *  集合存在于数据库中，集合没有固定的结构
 *  当第一个文档插入时，集合就会被创建

### 文档（document） ###

 *  文档是**一组键值（key-value）对**，所有存储在集合中的数据都是 BSON 格式（类 JSON 的一种二进制形式的存储格式）
 *  文档中的键/值对是**有序的**，即相同的键值对如果有不同顺序的话，也是不同的文档
 *  MongoDB 区分类型和大小写
 *  MongoDB 的文档不能有重复的键
 *  文档的键是字符串，值不仅可以是在双引号里面的字符串，还可以是其他几种数据类型（甚至可以是整个嵌入的文档）
 *  文档间可以通过**嵌入**和**引用**来建立联系

### 文档值数据类型 ###

 *  ObjectId：对象 ID，文档 "\_id" 键的默认类型，12 字节（Unix 时间戳、机器标识码、PID、随机数），`new ObjectId().getTimestamp()` `new ObjectId().str`
 *  String：字符串，在 MongoDB 中，UTF-8 编码的字符串才是合法的
 *  Integer：整型数值
 *  Double：双精度浮点值
 *  Boolean：布尔值
 *  Timestamp：时间戳，记录文档修改或添加的具体时间
 *  Date：日期时间，用 Unix 时间格式来存储当前日期或时间
 *  Array：用于将数组、列表或多个值存储为一个键
 *  Object：用于内嵌文档
 *  Null：用于创建空值
 *  二进制数据，用于存储二进制数据
 *  代码类型，用于在文档中存储 JavaScript 代码，如 `{"nodeprocess": function(){}}`
 *  正则表达式类型，用于存储正则表达式，如 `{"foo": /foobar/i}`
 *  Min/Max keys：将一个值与 BSON 元素的最低值和最高值相对比

# 操作命令 #

 *  在 Shell 平台用 db 代表当前数据库

## 数据库操作命令 ##

 *  查看当前操作的数据库：db
 *  查看所有数据库：show dbs
 *  创建数据库/切换到指定数据库：use db\_name（在 MongoDB 中，集合只有在文档插入后才会创建）
 *  统计某数据库信息：db.stats()
 *  删除数据库：db.dropDatabase()
 *  开启慢命令检测功能：db.setProfilingLevel(level, slowms)，参数说明：level：指定慢命令分析级别，0 为不执行该命令；1 为记录慢命令，当指定时间限制时（默认为超过 100 毫秒），超过该时间限制范围的执行命令都将记入 system.profile 文件中；2 为记录所有执行命令的情况slowms：可选，指定时间限制范围（单位：毫秒）

## 集合操作命令 ##

 *  查看已有集合：show collections 或 show tables
 *  创建集合：db.createCollection(collection\_name\[, options\])，options 可以是如下参数：capped：如果为 true，则创建固定集合（指有着固定大小的集合，当达到最大值时，会**自动覆盖最早的文档**，当该值为 true 时，必须指定 size 参数）autoIndexId：如为 true，自动在 \_id 字段创建索引，默认为 falsesize：为固定集合指定一个最大值（单位为 Byte）max：指定固定集合中包含文档的最大数量
 *  删除集合：db.collection\_name.drop()

## 文档操作命令 ##

 *  MongoDB 不支持事务，但是 MongoDB 提供了许多原子操作，比如文档的保存，修改，删除等，都是原子操作
 *  MongoDB 的数据存储过程：先把存放于内存的数据记录到数据操作记录日志里，然后以 60 秒为时间间隔，把数据持久性地保存到硬盘的数据库文件上

### 插入文档 ###

 *  db.collection\_name.insert(document)，如果该集合不在该数据库中，MongoDB 会自动创建该集合并插入文档
 *  从指定集合中插入一条文档数据：db.collection\_name.insertOne()
 *  从指定集合中插入多条文档数据：db.collection\_name.insertMany()
 *  存储的文档必须有一个 "\_id" 键，这个键的值可以是任何类型的，默认是 ObjectId 类型，并且自动将"\_id" 字段设置为主键

### 更新文档 ###

 *  db.collection\_name.update( ,  , \{upsert:  , multi:  , writeConcern:  \})
 *  `db.people.updateMany({age: {$gt: 25 }}, {$set: {status: "C"}})``update people set status = "C" where age > 25`
 *  参数说明：query：update 的查询条件对象update：更新操作符（如 $set, $unset, $inc …）和 update 的对象upsert：可选，这个参数的意思是，如果不存在 update 的记录，是否插入 objNew，true 为插入，默认是 false **不插入**multi：可选，mongodb 默认是 false，**只更新找到的第一条记录**，如果这个参数为 true，就把按条件查出来多条记录全部更新writeConcern：可选，抛出异常的级别
 *  从指定集合更新单个文档：db.collection\_name.updateOne()
 *  从指定集合更新多个文档：db.collection\_name.updateMany()
    

#### 更新操作符 ####

 *  字段更新操作符
    
     *  $set：用来指定一个键并更新键值，若键不存在并创建，`{$set: {a: 5}}`
     *  $unset：用来删除一个键，`{$unset: {a: 1}}`
     *  $rename：修改字段名称，`{$rename: { a:"b"}}`
     *  $inc：对文档的某个值为数字型的键进行增减的操作，`{$inc: {a: -2}}`
     *  $bit：位操作，integer 类型，`{ { $bit: {a: {and: 7 }}}`
 *  $currentDate：设置文档的某个值为当前时间，`{$currentDate: {a: {$type: "date"}}}`
 *  数组更新操作符
    
     *  $push：把 value 追加到**数组类型**的 field 里面去，如果 field 不存在，会新增一个数组类型加进去，`{$push: {a: 1}}`
     *  $pushAll：同 $push，可以追加多个值到一个数组字段内，`{$pullAll: {a: [5, 6]}}`
     *  $addToSet：增加一个值到数组内，**只有当这个值不在数组内才增加**，`{$addToSet: {a: 1}}` `{$addToSet: {a: {$each: [1, 2]}}}`
     *  $pull：从数组 field 内删除一个等于 value 值，`{$pull: {a: 5}}`
     *  $pullAll：从数组 field 内删除多个等于 value 值，`{$pullAll: {a: [5, 6]}}`
     *  $pop：删除数组的第一个或最后一个元素，`{$pop: {a: -1}}` `{$pop: {a: 1}}`

### 删除文档 ###

 *  db.collection\_name.remove( ,  )
 *  参数说明：query：可选，删除的文档的条件对象justOne：可选，如果设为 true 或 1，则只删除一个文档，如果不设置该参数，或使用默认值 false，则**删除所有匹配条件的文档**
 *  从指定集合删除单个文档：db.collection\_name.deleteOne()
 *  从指定集合删除多个文档：db.collection\_name.deleteMany()
 *  清空集合的数据：db.col.remove(\{\})
    

### 查询文档 ###

 *  db.collection\_name.find(query, projection)
 *  参数说明：query：可选，使用查询操作符指定查询条件对象projection：可选，使用投影操作符指定返回的键，默认省略（返回文档中所有键值），如 `{title: 1, by: 1}`（inclusion 模式，指定返回的键）或者 `{title: 0, by: 0}`（exclusion 模式，指定不返回的键），两种模式不可混用（\_id 键默认返回，需要主动指定 \_id:0 才会不返回）
 *  find() 方法的查询条件对象可以传入多个 key，每个 key 以**逗号**隔开，即常规 SQL 的 **AND 条件**
 *  注意：find() 方法返回的是**文档数组**，findOne() 方法只返回一个文档
 *  以格式化的方式来显示：db.collection\_name.find().pretty()
 *  查询分析，提供了查询信息，使用索引及查询统计等：explain()
 *  强制使用一个指定的索引：hint(index)

``````````
// 相当于 `where qty > 100 and qty < 200 and (key1=value1 or key2=value2)` 
  db.col.find({ 
      qty: { 
          $gt: 100, 
          $lt: 200 
      }, 
      $or: [{ 
          key1: { 
              $lt: value1 
          } 
      }, { 
          key1: value1 
      }] 
  })
``````````

#### 查询操作符 ####

 *  $lt（<）、$lte（<=）、$gt（>）、$gte（>=）、$ne（!=），用法：`{$lt: 10}`
 *  `{$or: [{a: 1}, {b: 2}]}`（a is 1 or b is 2）
 *  `{$in: [10,"hello"]}`（值为 10 或 "hello"）、`{$nin: [10,"hello"]}`
 *  `{$not: {$type: 2}}`
 *  `{$size: 3}`（is an array with exactly 3 elements）
 *  `{$exists: true}`（包含该字段）
 *  `{$all: [10,"hello"]}`（值的类型为数组，且包含 10 和 "hello"）
 *  `{$elemMatch: {b: 1, c: 2}`（is an array that contains an element with both b equal to 1 and c equal to 2）
 *  `{"a.b": 10}`（查询内嵌文档的属性时，属性名必须加上引号）
 *  `{a: /^m/}`（相当于 `{a: {$regex: "^m"}`、`{a: /foo.*bar/}`（模糊查询）
 *  `{$mod: [10, 1]}`（a mod 10 is 1）
 *  `{$type: 2}`（a 的值的类型为 String）
 *  分页
    
     *  跳过指定数量的文档：db.collection\_name.find().skip(number)
     *  读取指定数量的文档：db.collection\_name.find().limit(number)
 *  通过参数指定排序的字段对数据进行排序：db.collection\_name.find().sort(\{key 名：1 或 - 1\})，其中 1 为升序排列，而 -1 是用于降序排列
 *  查询总数：db.collection\_name.find().count() 或 db.collection name.count(query, options)
 *  查询集合里指定键的不同值： db.collection\_name.distinct(, query, option)

### 聚合 ###

 *  主要用于处理数据（如统计平均值，求和等），并返回计算后的数据结果：db.collection\_name.aggregate(aggregate\_operation)
 *  常用的聚合表达式
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414461840.png) 
    图 2 MongoDB聚合的表达式

 *  MongoDB 的常用的管道操作
    
     *  $project：修改输入文档的结构，可以用来重命名、增加或删除域，也可以用于创建计算结果以及嵌套文档
     *  $match：用于过滤数据，只输出符合条件的文档，$match 使用 MongoDB 的标准查询操作
     *  $limit：用来限制 MongoDB 聚合管道返回的文档数
     *  $skip：在聚合管道中跳过指定数量的文档，并返回余下的文档
     *  $unwind：将文档中的某一个数组类型字段拆分成多条，每条包含数组中的一个值
     *  $group：将集合中的文档分组，可用于统计结果
     *  $sort：将输入文档排序后输出
     *  $geoNear：输出接近某一地理位置的有序文档

# MongoDB 索引 #

 *  索引在具体使用时，是驻内存中持续运行的
 *  默认情况下，在建立集合的同时，MongoDB 数据库自动为集合\_id 建立唯一索引
 *  MongoDB 支持在索引下使用 $ 函数
 *  创建索引：db.collection\_name.createIndex(keys\[, options\])，如 `db.col.createIndex({_id: "hashed", title: -1, description: "text"}, {background: true})`
 *  keys 中的 key 的值1：按升序创建索引-1：按降序来创建索引"text"：创建文本索引"hashed"：创建哈希索引，如 `db.stores.createIndex({location: "2dsphere"})`"2dsphere"：创建地理空间索引，字段中的地理数据必须符合 GeoJSON 格式
 *  常用的可选参数 optionsbackground：是否以后台方式创建索引，默认值为 false（建索引过程会阻塞其它数据库操作）unique：建立的索引是否唯一，默认值为 falsename：索引的名称，如果未指定，MongoDB 的通过连接索引的字段名和排序顺序生成一个索引名称sparse：对文档中不存在的字段数据是否不启用索引，如果设置为 true 的话，在索引字段中不会查询出不包含对应字段的文档，默认值为 falseexpireAfterSeconds：指定一个以秒为单位的数值，完成 TTL 设定，设定文档在指定的时间（索引字段的值加上一个特定的秒数）达到后**自动失效**（**TTL 索引**）
 *  查看集合索引：db.collection\_name.getIndexes()
 *  查看集合索引大小：db.collection\_name.totalIndexSize()
 *  删除集合所有索引：db.collection\_name.dropIndexes()
 *  删除集合指定索引：db.collection\_name.dropIndex("索引名")

# MongoDB 复制（副本集） #

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414462434.jpeg) 
图 3 MongoDB复制

 *  把一台主节点服务器的数据完整地复制到从节点服务器上，起数据备份作用
 *  MongoDB 的复制至少需要两个节点：其中一个是主节点，负责处理客户端请求，其余的都是从节点，负责复制主节点上的数据
 *  各个节点常见的搭配方式为：一主一从、一主多从
 *  复制原理：主节点数据的修改操作会被记录到主节点的 oplog 上，从节点定期轮询（心跳）主节点获取这些操作，然后对自己的数据副本执行这些操作，从而保证从节点的数据与主节点一致
 *  特征：N 个节点的集群、任何节点可作为主节点、所有写入操作都在主节点上、**自动故障转移**、自动恢复
 *  副本集内的机器通过**心跳机制**通信，当检测到主节点宕机时，副本集内的服务器从剩余的服务器中选举一台新的服务器作为主节点继续提供服务

# MongoDB 分片（Sharding） #

 *  **将大型集合分割成块并存储到不同服务器**（或者集群）上，以解决大数据存放问题
 *  MongoDB 中数据的分片是以**集合**为基本单位的，集合中的数据通过**分片键**（Shard Key）被分割成多块（Chunk）
 *  片键就是在集合中选一个键，用该键的值作为数据分割的依据
 *  分片集群架构中的主要组件
    
     *  Mongos：充当查询路由器，接受用户查询请求后，通过跟踪 config 服务器上的分片元数据来确定用户需要访问哪个分片服务器，为不同访问用户的读、写操作**提供了数据访问统一接口**
     *  Config Server：存储集群的**元数据**（含数据块相关信息）和**配置信息**，配置服务必须部署为副本集
     *  Shard：每个分片包含被分片数据集合的子集，以块（Chunk）为单位存数据
         ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414462981.jpeg) 
        图 4 MongoDB分片集群结构

 *  分片策略
    
     *  [范围分片][Link 1]（Ranged Sharding）：sh.shardCollection("database.collection", \{ \})（默认的分片方法，shard key 应为具有连续键值的字段）
     *  [哈希分片][Link 2]（Hashed Sharding）：sh.shardCollection("database.collection", \{ : "hashed"\})（节点取余分区，无法范围查询，需先创建哈希索引）
     *  [分区分片][Link 3]（Zone Sharding）：根据文档字段的不同键值进行分片，需先创建单一或复合索引
        

# MongoTemplate 常用操作 #

 *  MongoTemplate 实现了 interface MongoOperations
 *  MongoDB documents 和 domain classes 之间的映射关系是通过实现了 MongoConverter 这个 interface 的类来实现的
 *  MongoTemplate 提供了非常多的操作 MongoDB 的方法。它是线程安全的，可以在多线程的情况下使用
 *  MongoTemplate 实现了 MongoOperations 接口，此接口定义了众多的操作方法如"find", "findAndModify", "findOne", "insert", "remove", "save", "update" and "updateMulti"等
 *  MongoTemplate 转换 domain object 为 DBObject，缺省转换类为 MongoMappingConverter，并提供了 Query, Criteria, and Update 等流式 API
 *  MongoTemplate 核心操作类：Criteria 和 Query
    
     *  Criteria 类：
     *  Query 类：将语句进行封装或者添加排序之类的操作

### Criteria 类 ###

 *  用于添加查询条件
 *  类方法`Criteria where(String key)``Criteria byExample(Object example)``Criteria byExample(Example<?> example)`
 *  常用实例方法`Criteria and (String key)``Criteria andOperator( Criteria… criteria)``Criteria orOperator( Criteria… criteria)``Criteria gt(object o)``Criteria gte(Object o``Criteria in(Object… o)``Criteria is(Object o)``Criteria It(object o)``Criteria Ite(Object o)`\`Criteria nin(Object… o)

### Query 类 ###

 *  将查询条件进行封装或者添加排序、投影等操作
 *  常用实例方法`Query skip(long skip)``Query limit(int limit)``Query with(Pageable pageable)``Query with(Sort sort)`

### MongoTemplate ###

 *  当在实体类上标注 `@Document(collection="MongoDB 对应 collection 名")` 时，方法中的 collectionName 可省略

#### 查询 ####

 *  `T findById(Object id, Class<T> entityClass)`：根据 id 查某一条数据
 *  `T findById(Object id, Class<T> entityClass, String collectionName)`：根据 id 查某一条数据，根据集合名
 *  `T findOne(Query query, Class<T> entityClass)`：根据查询条件，获取第一条数据
 *  `T findOne(Query query, Class<T> entityClass, String collectionName)`：根据查询条件和集合名，获取第一条数据
 *  `List<T> find(Query query, Class<T> entityClass)`：根据条件查询，获取符合条件的集合
 *  `List<T> find(Query query, Class<T> entityClass, String collectionName)`：根据条件查询和集合名，获取符合条件的集合
 *  `List<T> findAll(Class<T> entityClass)`：根据类型查询所有
 *  `List<T> findAll(Class<T> entityClass, String collectionName)`：根据类型和集合名查询所有

#### 新增 ####

 *  `T insert(Object objectToSave)`
 *  `T insert(Object objectToSave, String collectionName)`
 *  `T save(Object objectToSave)`：如果存在相同 id，那么就更新属性，如果没有，则新增
 *  `T save(Object objectToSave, String collectionName)`
 *  `Collection<T> insert(Collection<? extends T> batchToSave, Class<?> entityClass)`：批量新增
 *  `Collection<T> insert(Collection<? extends T> batchToSave, String collectionName)`：批量新增
 *  `Collection<T> insertAll(Collection<? extends T> objectsToSave)`：批量新增

#### 更新 ####

 *  `upsert(Query query, Update update, Class<?> entityClass)`：更新指定类型的第一条
 *  `upsert(Query query, Update update, String collectionName)`：更新指定类型指定集合名的第一条
 *  `upsert(Query query, Update update, Class<?> entityClass, String collectionName)`：更新指定集合名的的第一条
 *  `updateMulti(Query query, Update update, Class<?> entityClass)`：更新指定类型的多条数据
 *  `updateMulti(Query query, Update update, String collectionName)`：更新指定集合名的多条数据
 *  `updateMulti(Query query, Update update, Class<?> entityClass, String collectionName)`：更新指定类型指定集合名的多条数据

#### 删除 ####

 *  `remove(Object object)`：根据 id 删除
 *  `remove(Query query, Class<?> entityClass)`：根据查询条件指定删除
 *  `remove(Query query, String collectionName)`：根据集合名指定删除
 *  `remove(Query query, Class<?> entityClass, String collectionName)`：根据集合名和查询条件删除

#### 其他 ####

 *  `count()`
 *  `findAndModify()`：查找并修改
 *  `exists()`：查看是否存在

# MongoDB Repository #

 *  @EnableMongoRepositories
 *  对应接⼝
    
     *  MongoRepository
     *  PagingAndSortingRepository
     *  CrudRepository


[CAP]: https://static.sitestack.cn/projects/sdky-java-note/b455d299c9fbc73676fb22424e85fac6.png
[MongoDB]: https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows-unattended/
[Configuration File Options]: https://docs.mongodb.com/manual/reference/configuration-options/
[MongoDB 1]: https://static.sitestack.cn/projects/sdky-java-note/bc4014e140cc75a9f234ce1f063c6938.png
[MongoDB 2]: https://static.sitestack.cn/projects/sdky-java-note/2c2f0e13d1bad33e9ad1f35d796b21cd.jpeg
[MongoDB 3]: https://static.sitestack.cn/projects/sdky-java-note/507a4977f8a037c0389bb211401538d3.jpeg
[Link 1]: https://docs.mongodb.com/manual/core/ranged-sharding/
[Link 2]: https://docs.mongodb.com/manual/core/hashed-sharding/
[Link 3]: https://docs.mongodb.com/manual/core/zone-sharding/