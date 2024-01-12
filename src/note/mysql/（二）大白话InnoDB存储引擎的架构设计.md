mysql,数据库,sql


## 1、概述 #

上一节我们已经分析了MySQL架构上的整体设计原理，大部分人应该对一条SQL语句的一步步执行流程有了初步的整体了解，对于其中的细节我们会慢慢的分节去讲。

相信大部分人使用MySQL，选择的存储引擎应该就是**InnoDB存储引擎**，所以后面的章节主要基于InnoDB存储引擎来讲。那么今天我们基于一条**更新语句**的执行，来初步的了解一下InnoDB存储引擎的架构设计。

假如我们需要在业务系统需要执行一条如下的sql来更新数据：

    update users set name='lisi' where id=2

首先让我们回想一下这个sql一步步是如何执行的？如下图所示：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354102127.jpg)  
前一节我们已经讲过这个sql执行过程如下，首先业务系统通过连接池获取一个连接，连接到mysql数据库上，然后会经过**SQL接口、解析器、优化器、执行器几个环节**，解析SQL语句，生成执行计划，然后由执行器负责按照这个计划，去调用InnoDB存储引擎的接口去执行SQL。

今天我们来一起探索下这个存储引擎里的架构设计，以及如何基于存储引擎完成一条更新语句的执行。

## 2、InnoDB的重要内存结构：缓冲池 #

今天第一个闪亮登场的重要组件是：**缓冲池（Buffer Pool）**。缓冲池是InnoDB存储引擎中放在内存里的一个组件，这里面会缓存很多的数据，以便于以后在查询的时候，万一你要是内存缓冲池里有数据，就可以不用去查磁盘了，我们看下图：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354103416.jpg)

所以当我们的InnoDB存储引擎要执行更新语句的时候 ，比如对“id=2”这一行数据进行更新，其实会先看看“id=2”这一行数据是否在内存缓冲池里，如果不在的话，那么**会直接从磁盘里加载到内存缓冲池里来**，而且接着还会**对这行记录加独占锁**。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354104265.jpg)

## 3、undo日志文件：如何让你更新的数据可以回滚？ #

其实我们都应该知道，我们现在执行的是一个更新语句，一般都是在一个事务里进行的。那么事务提交之前，如果业务执行失败，我们需要对数据进行回滚的，也就是把“id=2”这行数据的name，更新为“lisi”的新值回滚到之前的“zhangsan”旧值。

大家此时想下要怎么进行回滚呢？最简单的办法就是把这条数据的旧值“zhangsan”在更新之前找个地方先记录下来，以便于以后的回滚。

没错！mysql就是这样做的，**记录这个旧值的组件叫做uodo log**。

此时，对我们来说，“id=2”这行数据的name原来是“zhangsan”，现在我们将要更新为“lisi”。那么此时我们在更新前得先把旧值“zhangsan”和“id=2”这些信息，**写入到undo日志文件中去**。

所以考虑到我们可能要回滚数据到旧值，这里会把你更新前的值写入undo日志文件，我们看下图：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354105074.jpg)

## 4、更新buffer pool中的缓存数据 #

当我们做完之前两步：

1.  把要更新的那行记录从磁盘文件加载到缓冲池，同时对这行记录加锁之后
2.  更新前的旧值写入undo日志文件之后

此时我们就可以正式开始更新这行记录了，那么更新的时候，存储引擎是怎么做的呢？大多数人都应该能想到：

 *  先更新内存缓冲池中的这条记录，此时这个数据就是脏数据了。  
    ps：这里所谓的更新内存缓冲池里的数据，意思就是把内存里的“id=2”这行数据的name字段修改为“lisi”。

那么为什么说此时这行数据就是脏数据了呢？

因为这个时候仅仅只是更新了内存缓冲池中数据，磁盘上“id=2”这行数据的name字段还是“zhangsan”，所以就会叫他是脏数据。

如下图，注意认真看执行的步骤的序号：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354105913.jpg)

## 5、Redo Log Buffer：万一系统宕机，如何避免数据丢失？ #

接着我们再来思考一个问题，按照上图的说明，现在已经把内存里的数据进行了修改，但是磁盘上的数据还没修改  
那么此时万一MySQL所在的机器宕机了，必然会导致内存里修改过的数据丢失，这可怎么办呢？

这个时候，就必须要把对内存所做的修改写入到一个地方里去，这也是内存里的一个缓冲区，组件名字叫**redo日志**。  
**所谓的redo日志，就是记录下来你对数据做了哪些修改**，比如对“id=2这行记录修改了name字段的值为lisi”，这就是一个redo日志。  
我们先看下图的示意：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354106875.jpg)  
这个redo日志其实是**用来在MySQL突然宕机的时候，用来恢复你更新过的新数据**，后面会有专门的章节来讲解redo日志，现在我们仅需要知道redo日志里记录的是什么和主要作用即可。

## 6、如果还没提交事务，MySQL宕机了怎么办？ #

我们都知道，InnoDB存储引擎是支持事务的，其实在数据库中，哪怕执行一条SQL语句，其实也可以是一个独立的事务，**只有当我们提交事务之后，SQL语句才算执行结束**。

到目前为止，我们仅是将新数据写入redo日志中，其实还没有提交事务，那么如果此时MySQL崩溃，必然导致内存里Buffer Pool中的修改过的数据都丢失，同时你写入Redo Log Buffer中的redo日志也会丢失。如下图所示：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354108467.jpg)  
那么这个时候数据丢失要紧吗？

其实是不要紧的，因为你一条更新语句，没提交事务，就代表没执行成功，仍然是保持着数据的一致性。因为此时MySQL宕机虽然导致内存里的数据都丢失了，但是你会发现，磁盘上的数据依然还停留在原样子。

也就是说，“id=2”的那行数据的name字段的值还是老的值，“zhangsan”，所以此时你的这个事务就是执行失败了，没能成功完成更新，你会收到一个数据库的异常。然后当mysql重启之后，你会发现你的数据并没有任何变化。

所以此时如果mysql服务器宕机，不会有任何的问题。

## 7、提交事务的时候将redo日志写入磁盘中 #

上面是因为没有提交事务，所以mysql服务器宕机影响不大。但是接着我们准备提交事务了，此时大家可能想到的是我们是不是应该将redo日志从内存中写入磁盘去？从而来避免msyql宕机造成的数据不一致。

没错，接着我们要提交事务了，此时存储引擎是要把redo日志从redo log buffer里刷入到磁盘文件里去。在此额外补充一点，内存数据写入磁盘中一般并不是直接写入磁盘，而是**先写入磁盘的文件系统缓存中（os cache），再刷入（flush）磁盘中**。

InnoDB提供了几种刷盘策略，此时这个策略是通过**innodb\_flush\_log\_at\_trx\_commit**来配置的，它有几个选项如下：

 *  **innodb\_flush\_log\_at\_trx\_commit=0**，表示每隔一秒把redo log buffer刷到磁盘文件系统中(os cache)去，并且调用文件系统的“flush”操作将缓存刷新到磁盘上去。  
    也就是说一秒之前的日志都保存在内存缓冲区，也就是说，如果mysql机器宕掉，最极端的情况是可能丢失1秒的数据变更。  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354109437.jpg)
 *  **innodb\_flush\_log\_at\_trx\_commit=1**，表示在每次事务提交的时候，都会把redo log buffer刷到磁盘文件系统中(os cache)去，并立刻调用文件系统的“flush”操作将缓存刷新到磁盘上去。  
    **这个设置是最安全的设置，能够保证不论是MySQL Crash 还是OS Crash或者是主机断电都不会丢失任何已经提交的数据**。但是这样的话，数据库对磁盘IO的要求就非常高了，如果底层的硬件比较差，那么MySQL数据库的并发很快就会由于硬件IO的问题而无法提升，会大大降低mysql的性能。  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354111255.jpg)
 *  **innodb\_flush\_log\_at\_trx\_commit=2**，表示在每次事务提交的时候会把redo log buffer刷到磁盘文件系统中(os cache)去，但并不会立即刷写到磁盘，每隔1s 将os cache中的数据刷新到磁盘。  
    如果只是MySQL数据库挂掉了（MySQL Crash），**由于文件系统没有问题，那么对应的事务数据并没有丢失**。只有在数据库所在的主机操作系统损坏或者突然掉电的情况下，数据库的事务数据可能丢失1秒之类的事务数据。这样的好处，减少了事务数据丢失的概率，而对底层硬件的IO要求也没有那么高(log buffer写到文件系统中，一般只是从log buffer的内存转移的文件系统的内存缓存(os cache)中，对底层IO没有压力)。  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354113396.jpg)

**总结如下：**

    首先需要大致了解一下mysql日志操作步骤：
    log_buff ---> os cache --->OS刷新 (flush)---> disk
    
    innodb_flush_log_at_trx_commit 参数解释：
    0（延迟写）：        log_buff  —> 每隔1秒   —>  os cache  —>实时---—>  disk
    1（实时写，实时刷）： log_buff  —>   实时    —>  os cache  —>实时---—>  disk
    2（实时写，延迟刷）： log_buff  —>   实时    —>  os cache  —>每隔1秒—>  disk

假设此时提交事务成功之后，redo日志也写在磁盘文件里，此时你肯定会有一条redo日志记录如下内容：“我此时对哪个数据做了一个什么修改，比如name字段修改为lisi了”。

然后哪怕此时buffer pool中更新过的数据还没刷新到磁盘里去，此时内存里的数据是已经更新过的“name=lisi”，然后磁盘上的数据还是没更新过的“name=zhangsan”。

我们看下图，提交事务之后，可能处于的一个状态，**Buffer Pool和redo日志中都是更新后的新值，而磁盘中则是旧值**。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354115305.jpg)

**此时如果说提交事务后处于上图的状态，然后mysql系统突然崩溃了，此时会如何？会丢失数据吗？**  
答案是**不会丢失**。因此等mysql重启之后，innoDB可以根据redo日志去恢复之前做过的修改，如下图：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354116197.jpg)  
那么磁盘中数据什么时候会更新会新值，并且怎么更新会新值，请看下一章节。  

## 关于笑小枫💕

> 本章到这里结束了，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
>
> 微信公众号：笑小枫
>
> 笑小枫个人博客：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
>
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)



