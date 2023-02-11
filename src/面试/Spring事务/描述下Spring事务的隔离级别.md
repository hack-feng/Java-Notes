### 描述下事务的隔离级别

> Isolation有5个值，有4种隔离级别，其中DEFAULT为默认值，使用底层数据库默认的隔离级别。

1. DEFAULT：使用底层数据库默认的隔离级别。 MySQL的默认隔离级别：REPEATABLE_READ（可重复读）；Oracle默认的隔离级别：READ_COMMITTED（读已提交）
2. READ_UNCOMMITTED：读未提交(存在脏读、不可重复读、幻读) 基本不使用
3. READ_COMMITTED：读已提交(解决度脏数据，存在不可重复读与幻读) 
4. REPEATABLE_READ：可重复读(解决脏读、不可重复读，存在幻读) 
5. SERIALIZABLE：串行化 。由于事务是串行执行，所以效率会大大下降，应用程序的性能会急剧降低。如果没有特别重要的情景，一般都不会使用 Serializable 隔离级别。

使用方法：@Transactional(isolation = Isolation.READ_UNCOMMITTED) 

**名词解释：**

“脏读”（dirty reads），事务可以看到其他事务“尚未提交”的修改。如果另一个事务回滚，那么当前事务读到的数据就是脏数据。

”不可重复读“（Non Repeatable Read），在一个事务内，多次读同一数据，在这个事务还没有结束时，如果另一个事务恰好修改了这个数据，那么，在第一个事务中，两次读取的数据就可能不一致。

”幻读“（Phantom Read）“，幻读是指，在一个事务中，第一次查询某条记录，发现没有，但是，当试图更新这条不存在的记录时，竟然能成功，并且，再次读取同一条记录，它就神奇地出现了。