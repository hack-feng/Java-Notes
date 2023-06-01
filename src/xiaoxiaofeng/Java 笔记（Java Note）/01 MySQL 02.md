# 数据库中的事务控制 #

## 事务相关知识 ##

 *  事务（Transaction，简写为 tx）：由一步或几步数据库操作（DML 语句）序列组成的逻辑执行单元，这系列操作要么全部执行，要么全部放弃执行（一组原子性的 SQL 査询）
 *  事务的 4 个特性（ACID 性）：原子性、一致性、隔离性、持续性
    
     *  原子性（Atomicity）：事务是数据库的逻辑工作单位，事务中包含的所有操作要么全部提交成功，要么全部失败回滚
     *  一致性（Consistency）：事务执行的结果必须是使数据库从一个一致性状态变到另一个一致性状态
     *  隔离性（Isolation）：一个事务所做的修改在最终提交以前，对其它事务是不可见的
     *  持久性（Durability）：也称永久性，一个事务一旦提交，则其所做的修改就会永久保存到数据库中，接下来的其它操作或故障不会对其执行结果有任何影响
 *  事务的 ACID 是通过 InnoDB 日志和锁来保证：事务的隔离性是通过数据库锁的机制实现的，原子性和持久性通过 redo log（重做日志）来实现，一致性通过 undo log 来实现
 *  在修改表的数据时，先需要修改其**内存拷贝**，再把该修改行为记录到重做日志 Buffer（redo log buffer）中，在事务结束后将重做日志写入磁盘，并通知文件系统刷新缓存中的数据到磁盘文件
 *  事务控制的命令：begin 或 start transaction、commit、rollback
 *  事务自动提交：
    
     *  MySQL 默认采用自动提交（autocommit = 1）模式，即如果不是显式地开始一个事务，则毎个查询都被当作一个事务执行提交操作
     *  在执行 DDL、DCL 操作之前会强制执行 commit 提交**当前**的活动事务
 *  事务并发可能会导致的问题：
    
     *  脏读：一个事务读到另一个事务未提交的更新数据
     *  不可重复读：一个事务两次读**同一行**数据，期间有另一个事务提交了更新，导致这两次读到的数据不一样
     *  幻读：一个事务在读取**某个范围内**的记录时，另外一个事务又在该范围内插入了新的记录，当再次读取该范围的记录时，会产生幻行
     *  第一类丢失更新（回滚丢失）：撤消一个事务时，把其它事务已提交的更新的数据回滚掉了
     *  第二类丢失更新（覆盖丢失）：提交一个事务时，把其它事务已提交的更新的数据覆盖了
 *  事务设置隔离级别：
    
     *  读未提交：事务中的修改，即使没有提交，对其它事务也都是可见的
     *  读已提交：一个事务开始时，只能“看见”其它已经提交的事务所做的修改
     *  可重复读：同一个事务中多次读取同样记录的结果是一致的，当 A 事务修改了一条记录但未提交时，B 事务将**不允许修改**这条记录（会被阻塞，innodb\_lock\_wait\_timeout，默认是 50s）
     *  可串行化：事务顺序执行，事务在读取的毎一行数据上都加锁
 *  InnoDB 默认的事务隔离级别是 Repeatable Read（可重复读），并且通过**间隙锁**（next-key locking）策略防止幻读的出现
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414339263.png) 
    图 1 事务的隔离级别

## 数据库锁 ##

 *  根据加锁范围，MySQL 里面的锁大致可以分成全局锁、表级锁和行锁三类

### 全局锁 ###

 *  主要用在全库备份过程中，对所有表进行全局读锁定 `flush tables with read lock` (FTWRL)
 *  在备份过程中整个库完全处于只读状态，客户端断开连接后，MySQL 会自动释放这个全局锁

### 表级锁 ###

 *  MySQL 里面表级别的锁有两种：一种是表锁，一种是元数据锁（meta data lock，MDL)

#### 表锁 ####

 *  lock tables tb1\_name \[as alias\] \{read \[local\] | \[low\_priority\] write\} \[, tb1\_name \[as alias\] \{read \[local\] | \[low\_priority\] write\}\] …：锁定用于当前线程的表，如果一个线程获得对一个表的 read 锁定，该线程（和所有其它线程）只能从该表中读取；如果一个线程获得对一个表的 write 锁定，只有保持锁定的线程可以对表进行写入，其它的线程被阻止，直到锁定被释放时为止
    
     *  unlock tables：释放被当前线程保持的任何锁定

#### 元数据锁（MDL） ####

 *  当对一个表做增删改查操作的时候，加 MDL 读锁；当要对表做结构变更操作的时候，加 MDL 写锁
 *  MDL 不需要显式使用，在访问一个表的时候会被自动加上

### 行锁 ###

 *  行锁是在引擎层由各个引擎自己实现的

#### InnoDB 的行锁模式及加锁方法 ####

 *  InnoDB 实现了以下两种类型的**行锁**：
    
     *  **共享锁**（S Lock）：允许一个事务去读一行数据，阻止其它事务获得相同数据集的排他锁
     *  **排他锁**（X Lock）：允许获得排他锁的事务更新数据，阻止其它事务取得相同数据集的共享读锁和排他写锁
 *  另外，为了允许行锁和表锁共存，实现多粒度锁机制，InnoDB 还有两种**内部使用**的**意向锁**（Intention Locks），这两种意向锁都是**表锁**
    
     *  意向共享锁（IS Lock）：事务打算给数据行加行共享锁，事务在给一个数据行加共享锁前必须先取得该表的 IS 锁
     *  意向排他锁（IX Lock）：事务打算给数据行加行排他锁，事务在给一个数据行加排他锁前必须先取得该表的 IX 锁
       
     
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414339873.png) 
    图 2 InnoDB行锁模式兼容性列表
    
 *  如果一个事务请求的锁模式与当前的锁兼容，InnoDB 就将请求的锁授予该事务；反之，如果两者不兼容，该事务就要等待锁释放
 *  **两阶段锁协议**：在 InnoDB 事务中，行锁是在需要的时候才加上，要等到事务结束时才释放，因此如果事务中需要锁多个行，应把最可能造成锁冲突、最可能影响并发度的锁的申请时机尽量往后放
 *  加锁方法
    
     *  意向锁是 InnoDB 自动加的，不需用户干预
     *  对于 update、delete 和 insert 语句，InnoDB 会自动给涉及数据集加排他锁（X）
     *  对于普通 select 语句，InnoDB 不会加任何锁
     *  事务可以通过以下语句**显式给记录集加共享锁或排他锁**
        
         *  共享锁（S）：`select * from table_name where … lock in share mode`
         *  排他锁（X）：`select * from table _name where … for update`

#### InnoDB 行锁实现方式 ####

 *  InnoDB 的行锁是通过给**索引上的索引项**加锁来实现的，如果没有索引，InnoDB 将通过隐藏的聚簇索引来对记录加锁
 *  如果**不通过索引条件检索数据**，那么 InnoDB 将对表中的**所有记录**加锁，实际效果跟表锁一样
 *  InnoDB 行锁分为 3 种情形：
    
     *  Record Lock：对**索引项**加锁
     *  Gap Lock：对索引项之间的“间隙”、第一条记录前的“间隙”或最后一条记录后的“间隙”加锁
     *  Next-key Lock：前两种的组合，对记录及其前面的间隙加锁
 *  当使用**范围条件**而不是相等条件检索数据，并请求共享或排他锁时，InnoDB 会给符合条件的已有数据记录的索引项加锁；对于键值在条件范围内但并不存在的记录，叫做间隙（GAP），**为了防止幻读以及保证恢复和复制的正确性**，InnoDB 也会对这个间隙加锁，这种锁机制就是所谓的 Next-Key 锁

#### 死锁和死锁检测 ####

 *  死锁：两个事务都需要获得对方持有的排他锁才能继续完成事务，即循环锁等待
 *  发生死锁后，有两种方式解除：
    
     *  一种是锁等待超时（默认 innodb\_lock\_wait\_timeout = 50）
     *  另一种是死锁检测，InnoDB 自动检测到死锁后（默认 innodb\_deadlock\_detect = on），主动回滚死锁链条中的某一个事务，让其它事务得以继续执行

> 
> 死锁检测要耗费大量的 CPU 资源，因为每个新来的**被堵住的**线程，都需要判断会不会由于自己的加入导致了死锁
> 热点行更新导致的性能问题的解决方法：控制并发度，对于相同行的更新，在进入引擎之前排队，或者将该热点行改成逻辑上的多行
> 

 *  避免死锁：不同的程序尽量约定以相同的顺序来访问表

## 并发控制机制 ##

 *  悲观锁：假定会发生并发冲突，屏蔽一切可能违反数据完整性的操作，即**在查询的时候给这个数据上锁**，这个锁排斥其他的修改锁，等到这个线程提交了或者回滚了，其他线程要查这个数据才能往下查使用数据库自身的排它锁机制（写锁）：DML 操作自动会加上排它锁，DQL 操作需要手动加上排它锁：`select * from 表名 for update`
 *  乐观锁：假设不会发生并发冲突，**只在提交更新操作时检查是否违反数据完整性**（乐观锁不能解决脏读的问题）在表中额外增加一个列（整数类型），用来表示修改的版本号，修改一次就把版本增加 1，且在提交更新操作时检查版本号使用与之前查询出来的版本号一致（通过判断执行更新操作后的影响行数是否为 0）

``````````sql
select id, name, version from person where id = 10; 
  update person set name  = 'java', version =  version + 1 
  where id = 10 and version = #{version};
``````````

 *  并发量不大且不允许脏读，可以使用悲观锁解决并发问题
 *  并发量非常大，悲观锁会带来非常大的性能问题，应选择使用乐观锁
 *  如果每次访问冲突概率小于 20%，推荐使用乐观锁，否则使用悲观锁

### 数据库实现事务隔离的两种方式 ###

 *  一种是在读取数据前，对其加锁，阻止其他事务对数据进行修改
 *  另一种是不用加任何锁，通过**一定机制**生成一个数据请求时间点的**一致性数据快照**（Snapshot），并用这个快照来提供一定级别（语句级或事务级）的一致性读取，从用户的角度来看，好像是数据库可以提供同一数据的多个版本，因此，这种技术叫做数据多版本并发控制（MultiVersion Concurrency Control，简称 MVCC 或 MCC）

### 多版本并发控制 ###

 *  InnoDB 的 MVCC 是通过在每行记录后面保存**两个隐藏的列**来实现的，一个保存了行创建时的**系统版本号**（行版本号），一个保存行删除时的**系统版本号**（行删除标识）
    
     *  select 时，InnoDB 只査找符合条件的数据行：行版本号小于或等于当前事务版本，并且行删除标识为未定义或大于当前事务版本号，且两个版本号对应的事务都已经提交
     *  update 时，InnoDB 为插入一行新记录，保存当前系统版本号作为行版本号，同时保存当前系统版本号到**原来的行**作为行删除标识
 *  注意：MVCC 只在 RC 和 RC 两个隔离级别下工作
 *  InnoDB 中，每个事务或者语句有自己的一致性读视图（consistent read view），**普通查询语句**是一致性读（consistent read）
 *  在 RR 隔离级别下，
    
     *  使用 `begin/start transaction` 启动事务，一致性视图是在执行第一个**快照读**语句时创建的，之后事务里的其它查询都共用这个一致性视图
     *  使用 `start transaction with consistent snapshot` 启动事务，一致性视图是在事务启动时创建的，之后事务里的其它查询都共用这个一致性视图
 *  在 RC 隔离级别下，无论以哪种方式启动事务，**每一个语句执行前**都会重新算出一个新的视图
 *  **更新数据**都需要先读后写，而这个读是**当前读**（current read），即总是读取已经提交完成的最新版本
 *  更新数据时如果当前的记录的行锁被其他事务占用的话，就需要进入锁等待

# InnoDB 存储引擎索引 #

 *  索引是对数据库表中一列或多列的**值**进行排序的一种结构
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414340435.png) 
    图 3 InnoDB存储引擎索引特性

## B+ 树索引 ##

 *  分为主键索引/聚集索引（clustered index）和非主键索引/辅助索引/二级索引（secondary index）
 *  联合索引/复合索引：对表上的多个列进行索引，联合索引的**最左前缀匹配原则**：
    
     *  MySQL 会一直向右匹配直到遇到**范围查询**（>、<、between、like、in）就**停止匹配**，比如 `a = 1 and b = 2 and c > 3 and d = 4`，如果建立 (a, b, c, d) 顺序的索引，d 是用不到索引的，如果建立 (a, b, d, c) 的索引则都可以用到，a, b, d 的顺序可以任意调整
     *  = 和 in 可以乱序，比如 `a = 1 and b = 2 and c = 3` 建立 (a, b, c) 索引可以任意顺序，mysql 的查询优化器会优化成索引可以识别的形式
 *  适用于全键值、键值范围或键前缀査找
 *  前缀索引：对于列的值较长，比如 blob、text、varchar，将值的**前一部分**作为索引
 *  覆盖索引：所需要的数据只需要在索引即可全部获得，不需要再到表中取数据
 *  索引下推（index condition pushdown）：在 MySQL 5.6 引入，在索引遍历过程中，**对索引中包含的字段先做判断**，直接过滤掉不满足条件的记录，减少回表次数

### 聚集索引 ###

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414341624.png) 
图 4 InnoDB聚集索引示意图

 *  InnoDB 存储引擎表是索引组织表，即表中数据按照主键顺序存放
 *  聚集索引：按照表的**主键**构造一棵 B+ 树，同时叶子节点中存放整张表的**行记录数据**，也将聚集索引的叶子节点称为**数据页**
 *  每张表只能拥有一个聚集索引

### 辅助索引 / 非聚集索引 ###

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414342067.png) 
图 5 InnoDB辅助索引示意图

 *  叶子节点不包含行记录的全部数据
 *  叶子节点除了包含键值以外，还包含了主键值
 *  当通过辅助索引来寻找数据时，InnoDB 存储引擎会遍历辅助索引并通过叶级别的**指针**获得指向**主键索引的主键**，然后再通过主键索引来找到一个完整的行记录（回表）

## 哈希索引 ##

 *  InnoDB 存储引擎支持的哈希索引是**自适应的**，InnoDB 存储引擎会根据表的使用情况**自动**为表生成哈希索引，不能人为干预是否在一张表中生成哈希索引
 *  InnoDB utilizes hash indexes internally for its Adaptive Hash Index feature. [InnoDB Storage Engine Features][]

## 全文检索（Full-Text Search） ##

 *  全文检索通常使用倒排索引（inverted index）来实现
 *  在辅助表（auxiliary table）中存储单词与单词自身在一个或多个文档中所在位置之间的映射
 *  通常利用关联数组，其表现形式：\{word, Documentld\}（inverted file index）或者 \{word, (Documentld, Position) \}（full inverted index）
 *  InnoDB 存储引擎采用 full inverted index 的方式：索引表中的每一项都包括一个属性值和具有该属性值的各记录的**地址**

## 索引的利弊 ##

 *  索引的好处
    
     *  提高表数据的检索效率
     *  如果排序的列是索引列，大大降低排序成本
     *  在分组操作中如果分组条件是索引列，也会提高效率
 *  索引需要额外的维护成本：当数据做 update、insert、delete 时，也需对相关索引数据进行处理，因此会降低 update、insert、delete 效率

## 建立与使用索引 ##

 *  在经常用作过滤条件或进行 order by、group by 的字段上建立索引
 *  在用于连接的列（主键/外键）或排序的列上建立索引
 *  不要在选择性非常差的字段上建立索引
 *  对于经常更新的列避免建立索引
 *  在经常存取的多个列上建立复合索引，但要注意复合索引的建立顺序要按照使用的频度来确定
 *  建立的索引支持多种过滤条件：在索引中加入更多的列，并通过 IN( ) 的方式覆盖那些不在 WHERE 子句中的列
 *  避免多个范围条件：对于范围条件查询，**MySQL 无法再使用范围列后面的其他索引列**

 *  重建索引
    
     *  重建主键索引：`alter table t engine=InnoDB`
     *  重建普通索引：`alter table t drop index k;` `alter table t add index(k);`
 *  使用 force index 强行选择一个索引，如 `select * from t force index(k)`

## 索引的限制 ##

 *  blob 和 text 类型的列只能创建前缀索引
 *  过滤字段使用了函数运算后（如 abs(column)），MySQL 无法使用索引
 *  使用不等于（!= 或者 <>）的时候 MySQL 无法使用索引
 *  使用 like 操作的时候如果条件以通配符开始（'%abc…'），MySQL 无法使用索引
 *  使用非等值查询的时候 MySQL 无法使用 Hash 索引
 *  如果 MySQL 估计使用全表扫描要比使用索引快，则不使用索引

# 査询性能优化 #

 *  遵循一些原则让**优化器**能够按照预想的合理的方式运行
 *  不做、少做、快速地做

## MySQL 执行 DQL 的流程 ##

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414342606.jpeg) 

 *  客户端发送一条査询给服务器
 *  服务器先检査査询缓存，如果命中了缓存，则立刻返回存储在缓存中的结果，否则进入下一阶段（如果査询缓存是打开的）
 *  解析器、预处理器对 SQL 语句进行解析、预处理，再由**优化器**生成对应的**执行计划**（一棵指令树）
 *  **执行器**根据优化器生成的执行计划，调用存储引擎的 API 来执行査询
 *  将结果返回给客户端，并存放到**査询缓存**中

> 
> MySQL 8.0 版本直接将查询缓存的整块功能删掉了，也就是说 8.0 开始彻底没有这个功能了
> 

## 性能优化相关命令 ##

### EXPLAIN 命令 ###

 *  使用方式：`explain 待执行的 SQL`
 *  id：执行查询的序列号
 *  select\_type：使用的查询类型
    
     *  **SIMPLE**：除子查询或者 union 之外的其它查询
     *  **PRIMARY**：子查询中的**最外层查询**
     *  **SUBQUERY**：子查询内层查询的第一个 select，结果不依赖于外部查询结果集
     *  **DEPENDENT SUBQUERY**：子查询中内层的第一个 select，**依赖于外部查询的结果集**
     *  **DERIVED**：在 from 子句的子查询中的 select
     *  UNION：union 语句中第二个 select 开始的后面所有 select，第一个 select 为 PRIMARY
     *  DEPENDENT UNION：子查询中的 union，且为 union 中从第二个 select 开始的后面所有 select，同样依赖于外部查询的结果集
     *  UNCACHEABLE SUBQUERY：结果集无法缓存的子查询
     *  UNION RESULT：union 中的合并结果
 *  table：这次查询访问的数据表
 *  **type**：对表所使用的访问方式：all | index | range | ref | eq\_ref | const, system | null，从左至右，性能由最差到最好
    
     *  all：全表扫描
     *  **index**：全索引扫描（遍历整个索引来査询匹配的行）
     *  **rang**：对索引进行范围检索
     *  **ref**：使用非唯一索引扫描或唯一索引的前缀扫描
     *  eq\_ref：最多只会有一条匹配结果，一般是通过主键或者唯一键索引来访问
     *  ref\_or\_null：与 ref 的唯一区别就是在使用索引引用查询之外再增加一个空值的查询
     *  index\_merge：查询中同时使用两个（或更多）索引，然后对索引结果进行 merge 之后再读取表数据
     *  index\_subquery：子查询中的返回结果字段组合是一个索引（或索引组合），但不是一个主键或者唯一索引
     *  unique\_subquery：子查询中的返回结果字段组合是主键或者唯一约束
     *  const：读常量，且最多只会有一条记录匹配，由于是常量，所以实际上只需要读一次
     *  system：系统表，表中只有一行数据
 *  **possible\_keys**：查询时可能使用的索引，如果没有使用索引，为 null
 *  **key**：实际使用的索引
 *  key\_len：使用到索引字段的长度
 *  ref：过滤的方式，比如 const（常量），column（join），func（某个函数）
 *  **rows**：扫描行的数量（通过收集到的统计信息估算，重新统计索引信息 `analyze table t`）
 *  filtered：针对表里符合某个条件（where 子句或联接条件）的记录数的百分比所做的一个悲观估算，把 rows 列和这个百分比相乘可以得到 MySQL 估算它将和查询计划里前一个表关联的行数
 *  Extra：查询中每一步实现的额外细节信息
    
     *  **Using index**：所需要的数据只需要在 Index 即可全部获得，而不需要再到表中取数据
     *  **Using temporary**：使用临时表，主要常见于 group by 和 order by 等操作中
     *  **Using where**：如果不是读取表的所有数据，或者不是仅仅通过索引就可以获取所有需要的数据，则会出现 Using where 信息
     *  **Using filesort**：当查询中包含 order by 操作，而且无法利用索引完成排序操作的时候，MySQL 查询优化器不得不选择相应的排序算法来实现
     *  Distinct：查找 distinct 值，所以当 mysql 找到了第一条匹配的结果后，将停止该值的查询而转为后面其他值的查询
     *  Full scan on NULL key：子查询中的一种优化方式，主要在遇到无法通过索引访问 null 值的使用使用
     *  Not exists：在某些左连接中 MySQL 查询优化器所通过改变原有查询的组成而使用的优化方法，可以部分减少数据访问次数
     *  No tables：查询语句中使用 from dual 或者不包含任何 from 子句
     *  Impossible WHERE noticed after reading const tables：MySQL 查询优化器通过收集到的统计信息判断出不可能存在结果
     *  Select tables optimized away：当我们使用某些聚合函数来访问存在索引的某个字段的时候，MySQL 查询优化器会通过索引而直接一次定位到所需的数据行完成整个查询。当然，前提是在查询中不能有 group by 操作。如使用 min() 或者 max() 的时候
     *  Using index for group-by：数据访问和 Using index 一样，所需数据只需要读取索引即可，而当查询中使用了 group by 或者 DISTINCT 子句的时候，如果分组字段也在索引中，Extra 中的信息就会是 Using index for group-by
     *  Using where with pushed condition：这是一个仅仅在 NDBCluster 存储引擎中才会出现的信息，而且还需要通过打开 Condition Pushdown 优化功能才可能会被使用，控制参数为 engine\_condition\_pushdown

### 使用 profiling 分析 SQL 语句 ###

 *  在 Session 级别开启 profiling：`set profiling = 1;`
 *  执行查询，在 profiling 过程中所有的 query 都可以记录下来
 *  查看记录的 query：`show profiles;`
 *  选择要查看的 profile：`show profile for query 1;`
 *  选择要查看的 profile 的 cpu、block io 明细：`show profile cpu, block io for query 1;`

## 慢查询分析 ##

 *  慢査询日志相关配置选项：
    
     *  slow\_query\_log：设置是否打开慢査询日志的开关，默认值为 off
     *  long\_query\_time：定义慢查询的时间，单位为秒，默认值为 10
     *  slow\_query\_log\_file: 设置慢查询日志文件的路径

## 常见的查询优化 ##

 *  优化表结构：适当使用冗余数据；大表拆小表，有大数据的列（如类型为 text）单独拆成一张表；把常用属性分离成小表
 *  字段尽可能地设置为 NOT NULL
 *  切分查询：将大査询切分成小査询
 *  分解关联查询：对每一个表进行一次单表查询，然后将结果在应用层中进行关联优点：让缓存的效率更高，执行单表查询可以减少锁的竞争，可以对数据库进行拆分，可以使用 in( ) 代替关联查询
 *  优化关联查询：考虑到关联的顺序，确保 on 或者 using 子句中的列上有索引；确保任何的 group by 和 order by 中的表达式只涉及到一个表中的列；**小结果集驱动大结果集**；在 on 或者 using 子句后加上过滤条件对右边表做过滤
 *  优化子查询：尽可能使用**关联査询**代替
 *  优化 count() 查询：使用 count(\*) 统计行数；使用近似值；增加汇总表
 *  优化 limit 分页：利用延迟关联或者子查询优化超多分页场景：当 offset 特别大时，先快速定位需要获取的 id 段，然后再关联，即 select a.\* from table\_1 a, (select id from table\_1 where 条件 limit 100000, 20 ) b where a.id = b.id
 *  其它尽量避免使用负向查询：not、!=、<>、!<、!>、not exists、not in、not like 等尽量避免在 where 子句中使用 or 来连接条件，**同一字段改用 in，不同一字段改用 union**

# MySQL 复制 #

## 复制的工作原理 ##

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414343472.png) 
图 6 MySQL复制过程

 *  主库（master）把数据更改记录到**二进制日志**（bin log）中
 *  从库（slave）**I/O 线程**跟主库建立一个普通的客户端连接，将主库上的二进制日志内容**复制**到自己的**中继日志**（relay log）中（主库**发送信号量**通知其有新的事件产生）
 *  从库 **SQL 线程**将中继日志中新增加的日志内容解析成 SQL 语句，并在自身从数据库上按顺序执行这些 SQL 语句

## 主要功能 ##

 *  由于从库复制是异步的，所以从库上可能会存在脏数据
 *  读 / 写分离：将不能容忍脏数据的读査询和写分配到分配到主库，其它的读（如评论、报表、日志等）査询分配到从库
 *  数据库备份、数据分布、读取的负载平衡、高可用性和故障转移

## 使用 Keepalived 实现主从自动切换 ##

## 代码实现 ##

 *  使用 Spring 的 **AbstractRoutingDataSource** 实现多数据源切换
 *  DataSource 以 key-value 形式存储，**根据传入的 key 值**切换到对应的 DataSource 上

# 数据库分库分表中间件 #

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414343997.png) 
图 7 云数据库MySQL\_5.7性能

## Mycat ##

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414344484.png) 
图 8 Mycat架构

 *  Mycat 核心配置文件
    
     *  server. xml：配置连接 Mycat 的用户名、密码、数据库名
     *  schema xml：配置 schema、datanode、datahost
     *  rule. xml：分片规则
 *  Mycat 常用的分片规则：取模范围分片、一致性 hash 分片、范围分片、枚举分片、取模分片、日期分片、按月分片、冷热数据分片

## Sharding-JDBC ##

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414345060.jpeg) 
图 9 MySQL优化


[b550bdbf5a33c79511fa11393d0f78ad.png]: https://static.sitestack.cn/projects/sdky-java-note/b550bdbf5a33c79511fa11393d0f78ad.png
[InnoDB]: https://static.sitestack.cn/projects/sdky-java-note/f0f36a0b2b691a0f213bae6808556690.png
[InnoDB 1]: https://static.sitestack.cn/projects/sdky-java-note/24b7cf175b053ddab57f6921a51e9707.png
[InnoDB 2]: https://static.sitestack.cn/projects/sdky-java-note/cc2d5039f59a19b367423891cddb64d1.png
[InnoDB 3]: https://static.sitestack.cn/projects/sdky-java-note/404429b9a7f80c6ad17d1cd6e0499148.png
[InnoDB Storage Engine Features]: https://dev.mysql.com/doc/refman/5.7/en/innodb-introduction.html
[01 MySQL 02 - _6]: https://static.sitestack.cn/projects/sdky-java-note/ca784b6fe377ec2c3cca974f092d5dbc.jpeg
[MySQL]: https://static.sitestack.cn/projects/sdky-java-note/56ad7e16034291090c8e2b4b8c4a20dd.png
[MySQL_5.7]: https://static.sitestack.cn/projects/sdky-java-note/329ff00abf3b218e54634d78dd4e4a1d.png
[Mycat]: https://static.sitestack.cn/projects/sdky-java-note/1f878fb3d5602d302b03a78369bd701c.png
[MySQL 1]: https://static.sitestack.cn/projects/sdky-java-note/2c8cb2422d5ce5a6d2a0da1c7ee9fba1.jpeg