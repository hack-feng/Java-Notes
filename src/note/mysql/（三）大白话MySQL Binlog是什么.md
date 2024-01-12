Buffer Pool、Undo日志、Redo日志和binlog



## 1、前情回顾 #

前一节我们借助于一条更新SQL讲解了MySQL的常用存储引擎Innodb是怎么去完成这条数据更新，明白了Innodb存储引擎有几个重要的组件：**Buffer Pool、Undo日志和Redo日志**。

    update users set name='lisi' where id=2

数据更新过程如下图：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354263072.jpg)  
那么MySQL除了以上的两种主要日志文件外，还有一种重要的日志文件：**binlog**。

## 2、MySQL binlog到底是什么东西？ #

 *  实际上我们之前说的redo log，是一种偏向物理性质的重做日志，因为他里面记录的是类似这样的东西，**“对哪个数据页中的什么记录，做了个什么修改”**。而且**redo log本身是属于InnoDB存储引擎独有的一个东西**。
 *  而binlog叫做归档日志，里面记录的是偏向于逻辑性的日志，类似于“对users表中的id=2的这一行数据做了更新操作，更新以后的值是什么”。binlog不是InnoDB存储引擎特有的日志文件，是**属于mysql server自己的日志文件**。

## 3、MySQL binlog的作用 #

binlog是一个二进制格式的文件，主要有以下几个作用：

 *  利用binlog日志恢复数据库数据。比如我们经常听说的删库跑路，那么有什么办法将删除的数据再恢复进来，通过binlog日志就可以做到。
 *  主从架构通过binlog同步数据。生产上我们MySQL架构一般都是主从架构，那么怎么将主库更改的数据同步到从库去呢？最常用的办法就是使用binlog来同步数据到从库。
 *  可以用binlog中的信息进行审计，比如判断是否有对数据库进行注入攻击。

## 4、提交事务的时候，同时会写入binlog #

接着上一讲讲到，在我们提交事务的时候，会把redo log日志写入磁盘文件中去。但是其实在提交事务的时候，我们同时还会把这次更新对应的binlog日志写入到磁盘文件中去，如下图所示：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354264843.jpg)  
大家可以在这个图里看到一些变动，就是把跟InnoDB存储引擎进行交互的组件加了进来，这个组件就是之前提过的**执行器**，他会负责跟InnoDB进行交互，包括从磁盘里加载数据到Buffer Pool中进行缓存，包括写入undo日志，包括更新Buffer Pool里的数据，以及写入redo log buffer，redo log刷入磁盘，写binlog，等等。  
实际上，**执行器是非常核心的一个组件，负责跟存储引擎配合完成一个SQL语句在磁盘与内存层面的数据全部更新操作**。

而且我们在上图可以看到，每一次更新语句的执行，拆分为了两个阶段：

 *  上图中的1、2、3、4几个步骤，其实本质是你执行这个更新语句的时候干的事。
 *  然后上图中的5和6两个步骤，是从你提交事务开始的，属于提交事务的阶段了。

## 5、binlog日志的刷盘策略分析 #

对于binlog日志，其实和redo日志一样也有不同的刷盘策略，有一个sync\_binlog参数可以控制binlog的刷盘策略。

 *  sync\_binlog=0，此时你把binlog写入磁盘的时候，其实不是直接进入磁盘文件，**而是进入文件系统的os cache内存缓存**。而让文件系统自行决定什么时候来做同步，或者cache满了之后才同步到磁盘。

所以跟之前分析的一样，如果此时机器宕机，那么你在os cache里的binlog日志是会丢失的，我们看下图的示意：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354265673.jpg)

 *  sync\_binlog=1，那么此时会强制在提交事务的时候，把binlog直接写入OS CACHE，并立即刷入到磁盘文件里去，相当于直接写入磁盘了。

那么这样提交事务之后，哪怕机器宕机，磁盘上的binlog是不会丢失的，如下图所示  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354266645.jpg)

 *  sync\_binlog=n，**当每进行n次事务提交之后**，MySQL进行一次将os cache中的binlog数据强制写入磁盘。

**总结：**

1.  前面讲到在MySQL中系统默认的设置是sync\_binlog=0，也就是不做任何强制性的磁盘刷新指令，这时候的性能是最好的，但是风险也是最大的。因为一旦系统Crash，在os cache中的所有binlog信息都会被丢失。
2.  而当sync\_binlog=1的时候，是最安全但是性能损耗最大的设置。因为当设置为1的时候，即使系统Crash，也最多丢失os cache中未完成的一个事务，对实际数据没有任何实质性影响。但是就是对写入性能影响太大，binlog虽然是顺序IO，多个事务同时提交，同样很大的影响MySQL和IO性能。
3.  对于高并发事务的系统说，“sync\_binlog”设置为0和设置为1的系统写入性能差距可能高达5倍甚至更多。
4.  所以此参数设置要结合自己的实际业务场景来设置合理值。

## 6、基于binlog和redo log完成事务的提交 #

当我们把binlog写入磁盘文件之后，接着就会完成最终的事务提交，此时会把本次更新对应的binlog文件名称和这次更新的binlog日志在文件里的位置，都写入到redo log日志文件里去，同时在redo log日志文件里写入一个commit标记。

在完成这个事情之后，才算最终完成了事务的提交，我们看下图的示意。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354267578.jpg)

## 7、最后一步在redo日志中写入commit标记的意义是什么？ #

这时候肯定有童鞋会问了，最后在redo日志中写入commit标记有什么意义呢？  
说白了，**这个commit其实是用来保持redo log日志与binlog日志一致性的**。

我们来举个例子，假设我们在提交事务的时候，一共有上图中的5、6、7三个步骤，必须是三个步骤都执行完毕，才算是提交了事务。那么在我们刚完成步骤5的时候，也就是redo log刚刷入磁盘文件的时候，mysql宕机了，此时怎么办？

这个时候因为没有最终的事务commit标记在redo日志里，所以此次事务可以判定为不成功。不会说redo日志文件里有这次更新的日志，但是binlog日志文件里没有这次更新的日志，不会出现数据不一致的问题。

如果要是完成步骤6的时候，也就是binlog写入磁盘了，此时mysql宕机了，怎么办？  
同理，因为没有redo log中的最终commit标记，因此此时事务提交也是失败的。

必须是在redo log中写入最终的事务commit标记了，然后此时事务提交成功，而且redo log里有本次更新对应的日志，binlog里也有本次更新对应的日志 ，redo log和binlog完全是一致的。

## 8、后台IO线程随机将内存更新后的脏数据刷回磁盘 #

现在我们假设已经提交事务了，此时一次更新“update users set name=‘lisi’ where id=2”，他已经把内存里的buffer pool中的缓存数据更新了，同时磁盘里有redo日志和binlog日志，都记录了把我们指定的“id=2”这行数据修改了“name=‘lisi’”。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354269069.jpg)

此时我们会思考一个问题了，但是这个时候磁盘上的数据文件里的“id=2”这行数据的name字段还是等于"zhangsan"这个旧的值！那么什么时候会把磁盘的数据刷成"zhangsan"呢？

其实MySQL有一个后台的IO线程，会在之后某个时间里，随机的把内存buffer pool中的修改后的脏数据给刷回到磁盘上的数据文件里去，我们看下图：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/2/xxf-1667354270973.jpg)  
当上图中的IO线程把buffer pool里的修改后的脏数据刷回磁盘的之后，磁盘上的数据才会跟内存里一样，都是"name=lisi"这个修改以后的值了！

在你IO线程把脏数据刷回磁盘之前，哪怕mysql宕机崩溃也没关系，因为重启之后，会根据redo日志恢复之前提交事务做过的修改到内存里去，就是id=10的数据的name修改为了"lisi"。然后等适当时机，IO线程自然还是会把这个修改后的数据刷到磁盘上的数据文件里去的。

## 9、binlog日志和redo日志的区别是什么？ #

好多人可能说了，binlog日志和redo日志都是还原数据库数据用的，这两个功能很类似，是不是存在功能重复的问题？相信很多人都混淆过，其实两者的本质不同。

 *  作用不同：binlog可以作为恢复数据使用，比如误删数据；redo log作为异常宕机或者介质故障后的数据恢复使用。
 *  内容不同：redo log是物理日志，是数据页面的修改之后的物理记录，binlog是逻辑日志，可以简单认为记录的就是sql语句。
 *  恢复数据时候的效率不同，基于物理日志的redo log恢复数据的效率要高于语句逻辑日志的binlog。
 *  binlog不是循环使用，在写满或者重启之后，会生成新的binlog文件。redo log是循环使用。

## 10、基于更新数据的流程，总结一下InnoDB存储引擎的架构原理 #

大家通过一次更新数据的流程，就可以清晰地看到，InnoDB存储引擎主要就是包含了一些buffer pool、redo log buffer等内存里的缓存数据，同时还包含了一些undo日志文件、redo日志文件等东西，同时mysql server自己还有binlog日志文件。

在你执行更新的时候，每条SQL语句，都会对应修改buffer pool里的缓存数据、写undo日志、写redo log buffer几个步骤。但是当你提交事务的时候，一定会把redo log刷入磁盘，binlog刷入磁盘，完成redo log中的事务commit标记。最后后台的IO线程会随机的把buffer pool里的脏数据刷入磁盘里去。

其实大家可以看到MySQL通过两阶段提交过程来完成事务的一致性的，也即redo log和binlog的一致性的。理论上是先写redo log，再写binlog，两个日志都提交成功(刷入磁盘)，事务才算真正的完成。

## 关于笑小枫💕

> 本章到这里结束了，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
>
> 微信公众号：笑小枫
>
> 笑小枫个人博客：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
>
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)
>