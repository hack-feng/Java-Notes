### 什么是ZooKeeper？

ZooKeeper是一个开放源码的分布式协调服务，它是集群的管理者，监视着集群中各个节点的状态根据节点提交的反馈进行下一步合理操作。最终，将简单易用的接口和性能高效、功能稳定的系统提供给用户。

分布式应用程序可以基于Zookeeper实现诸如数据发布/订阅、负载均衡、命名
服务、分布式协调/通知、集群管理、Master选举、分布式锁和分布式队列等功能。Zookeeper保证了如下分布式一致性特性：

1. 顺序一致性
2. 原子性
3. 单一视图
4. 可靠性
5. 实时性(最终一致性)

客户端的读请求可以被集群中的任意一台机器处理，如果读请求在节点上注册了监听器，这个监听器也是由所连接的zookeeper机器来处理。对于写请求，这些请求会同时发给其他zookeeper机器并且达成一致后，请求才会返回成功。因此，随着zookeeper的集群机器增多，读请求的吞吐会提高但是写请求的吞吐会下降。

有序性是zookeeper中非常重要的一个特性，所有的更新都是全局有序的，每个更新都有一个唯一的时间戳，这个时间戳称为zxid(ZookeeperTransactionId)。而读请求只会相对于更新有序，也就是读请求的返回结果中会带有这个zookeeper最新的zxid。

### ZooKeeper提供了什么？


1. 文件系统
2. 通知机制


### Zookeeper文件系统

Zookeeper提供一个多层级的节点命名空间(节点称为znode)。与文件系统不同的是，这些节点都可以设置关联的数据，而文件系统中只有文件节点可以存放数据而目录节点不行。

Zookeeper为了保证高吞吐和低延迟，在内存中维护了这个树状的目录结构，这种特性使得Zookeeper不能用于存放大量的数据，每个节点的存放数据上限为1M。


### ZAB协议？

ZAB协议是为分布式协调服务Zookeeper专门设计的一种支持崩溃恢复的原子广播协议。

ZAB协议包括两种基本的模式：崩溃恢复和消息广播。

当整个zookeeper集群刚刚启动或者Leader服务器宕机、重启或者网络故障导致不存在过半的服务器与Leader服务器保持正常通信时，所有进程(服务器)进入崩溃恢复模式，首先选举产生新的Leader服务器，然后集群中Follower服务器开始与新的Leader服务器进行数据同步，当集群中超过半数机器与该Leader服务器完成数据同步之后，退出恢复模式进入消息广播模式，Leader服务器开始接收客户端的事务请求生成事物提案来进行事务请求处理。

### 四种类型的数据节点Znode

1. PERSISTENT-持久节点
除非手动删除，否则节点一直存在于Zookeeper上

2. EPHEMERAL-临时节点
临时节点的生命周期与客户端会话绑定，一旦客户端会话失效(客户端与zookeeper连接断开不一定会话失效)，那么这个客户端创建的所有临时节点都会被移除。

3. PERSISTENT_SEQUENTIAL-持久顺序节点
基本特性同持久节点，只是增加了顺序属性，节点名后边会追加一个由父节点维护的自增整型数字。

4. EPHEMERAL_SEQUENTIAL-临时顺序节点
基本特性同临时节点，增加了顺序属性，节点名后边会追加一个由父节点维护的自增整型数字。

### ZookeeperWatcher机制--数据变更通知

Zookeeper允许客户端向服务端的某个Znode注册一个Watcher监听，当服务端的一些指定事件触发了这个Watcher，服务端会向指定客户端发送一个事件通知来实现分布式的通知功能，然后客户端根据Watcher通知状态和事件类型做出业务上的改变。

**工作机制**：
1. 客户端注册watcher
2. 服务端处理watcher
3. 客户端回调watcher

**Watcher特性总结**：

1. 一次性
无论是服务端还是客户端，一旦一个Watcher被触发，Zookeeper都会将其从相应的存储中移除。这样的设计有效的减轻了服务端的压力，不然对于更新非常频繁的节点，服务端会不断的向客户端发送事件通知，无论对于网络还是服务端的压力都非常大。

2. 客户端串行执行
客户端Watcher回调的过程是一个串行同步的过程。

3. 轻量

* Watcher通知非常简单，只会告诉客户端发生了事件，而不会说明事件的具体内容。
* 客户端向服务端注册Watcher的时候，并不会把客户端真实的Watcher对象实体传递到服务端，仅仅是在客户端请求中使用boolean类型属性进行了标记。

4. watcherevent异步发送watcher的通知事件从server发送到client是异步的，这就存在一个问题，不同的客户端和服务器之间通过socket进行通信，由于网络延迟或其他因素导致客户端在不通的时刻监听到事件，由于Zookeeper本身提供了orderingguarantee，即客户端监听事件后，才会感知它所监视znode发生了变化。所以我们使用Zookeeper不能期望能够监控到节点每次的变化。Zookeeper只能保证最终的一致性，而无法保证强一致性。

5. 注册watchergetData、exists、getChildren

6. 触发watchercreate、delete、setData

7. 当一个客户端连接到一个新的服务器上时，watch将会被以任意会话事件触发。当与一个服务器失去连接的时候，是无法接收到watch的。而当client重新连接时，如果需要的话，所有先前注册过的watch，都会被重新注册。通常这是完全透明的。只有在一个特殊情况下，watch可能会丢失：对于一个未创建的znode的existwatch，如果在客户端断开连接期间被创建了，并且随后在客户端连接上之前又删除了，这种情况下，这个watch事件可能会被丢失。

### 客户端注册Watcher实现

1. 调用getData()/getChildren()/exist()三个API，传入Watcher对象
2. 标记请求request，封装Watcher到WatchRegistration
3. 封装成Packet对象，发服务端发送request
4. 收到服务端响应后，将Watcher注册到ZKWatcherManager中进行管理
5. 请求返回，完成注册。


### 服务端处理Watcher实现


* **服务端接收Watcher并存储**
接收到客户端请求，处理请求判断是否需要注册Watcher，需要的话将数据节点的节点路径和ServerCnxn(ServerCnxn代表一个客户端和服务端的连接，实现了Watcher的process接口，此时可以看成一个Watcher对象)存储在WatcherManager的WatchTable和watch2Paths中去。

* **Watcher触发**
以服务端接收到setData()事务请求触发NodeDataChanged事件为例：

1. 封装WatchedEvent
将通知状态(SyncConnected)、事件类型(NodeDataChanged)以及节点路径封装成一个WatchedEvent对象

2. 查询Watcher
从WatchTable中根据节点路径查找Watcher

3. 没找到；说明没有客户端在该数据节点上注册过Watcher

4. 找到；提取并从WatchTable和Watch2Paths中删除对应Watcher(从这里可以看出Watcher在服务端是一次性的，触发一次就失效了)

* **调用process方法来触发Watcher**
这里process主要就是通过ServerCnxn对应的TCP连接发送Watcher事件通知。


### 客户端回调Watcher

客户端SendThread线程接收事件通知，交由EventThread线程回调Watcher。客户端的Watcher机制同样是一次性的，一旦被触发后，该Watcher就失效了。

### ACL权限控制机制

UGO(User/Group/Others)

目前在Linux/Unix文件系统中使用，也是使用最广泛的权限控制方式。是一种粗粒度的文件系统权限控制模式。

ACL(AccessControlList)访问控制列表

包括三个方面：

1. 权限模式(Scheme)

* IP：从IP地址粒度进行权限控制
* Digest：最常用，用类似于username:password的权限标识来进行权限配置，便于区分不同应用来进行权限控制
* World：最开放的权限控制方式，是一种特殊的digest模式，只有一个权限标识“world:anyone”
* Super：超级用户

2. 授权对象

授权对象指的是权限赋予的用户或一个指定实体，例如IP地址或是机器灯。

3. 权限Permission

* CREATE：数据节点创建权限，允许授权对象在该Znode下创建子节点
* DELETE：子节点删除权限，允许授权对象删除该数据节点的子节点
* READ：数据节点的读取权限，允许授权对象访问该数据节点并读取其数据内容或子节点列表等
* WRITE：数据节点更新权限，允许授权对象对该数据节点进行更新操作
* ADMIN：数据节点管理权限，允许授权对象对该数据节点进行ACL相关设置操作

### 描述下Chroot特性

3.2.0版本后，添加了Chroot特性，该特性允许每个客户端为自己设置一个命名空间。如果一个客户端设置了Chroot，那么该客户端对服务器的任何操作，都将会被限制在其自己的命名空间下。

通过设置Chroot，能够将一个客户端应用于Zookeeper服务端的一颗子树相对应，在那些多个应用公用一个Zookeeper进群的场景下，对实现不同应用间的相互隔离非常有帮助。

### 描述下会话管理

分桶策略：将类似的会话放在同一区块中进行管理，以便于Zookeeper对会话进行不同区块的隔离处理以及同一区块的统一处理。

分配原则：每个会话的“下次超时时间点”(ExpirationTime)

### 描述下服务器角色

* **Leader**

1. 事务请求的唯一调度和处理者，保证集群事务处理的顺序性
2. 集群内部各服务的调度者

* **Follower**

1. 处理客户端的非事务请求，转发事务请求给Leader服务器
2. 参与事务请求Proposal的投票
3. 参与Leader选举投票

* **Observer**

1. 3.0版本以后引入的一个服务器角色，在不影响集群事务处理能力的基础上提升集群的非事务处理能力
2. 处理客户端的非事务请求，转发事务请求给Leader服务器
3. 不参与任何形式的投票


### 描述下Zookeeper下Server工作状态


服务器具有四种状态，分别是LOOKING、FOLLOWING、LEADING、OBSERVING。

1. LOOKING：寻找Leader状态。当服务器处于该状态时，它会认为当前集群中没有Leader，因此需要进入Leader选举状态。
2. FOLLOWING：跟随者状态。表明当前服务器角色是Follower。
3. LEADING：领导者状态。表明当前服务器角色是Leader。
4. OBSERVING：观察者状态。表明当前服务器角色是Observer。


### zookeeper是如何保证事务的顺序一致性的？

zookeeper采用了全局递增的事务Id来标识，所有的proposal(提议)都在被提出的时候加上了zxid，zxid实际上是一个64位的数字，高32位是epoch(时期;纪元;世;新时代)用来标识leader周期，如果有新的leader产生出来，epoch会自增，低32位用来递增计数。当新产生proposal的时候，会依据数据库的两阶段过程，首先会向其他的server发出事务执行请求，如果超过半数的机器都能执行并且能够成功，那么就会开始执行。

### 分布式集群中为什么会有Master？

在分布式环境中，有些业务逻辑只需要集群中的某一台机器进行执行，其他的机器可以共享这个结果，这样可以大大减少重复计算，提高性能，于是就需要进行leader选举。

### zk节点宕机如何处理？

Zookeeper本身也是集群，推荐配置不少于3个服务器。Zookeeper自身也要保证当一个节点宕机时，其他节点会继续提供服务。

如果是一个Follower宕机，还有2台服务器提供访问，因为Zookeeper上的数据是有多个副本的，数据并不会丢失；如果是一个Leader宕机，Zookeeper会选举出新的Leader。

ZK集群的机制是只要超过半数的节点正常，集群就能正常提供服务。只有在ZK节点挂得太多，只剩一半或不到一半节点能工作，集群才失效。

* 3个节点的cluster可以挂掉1个节点(leader可以得到2票>1.5)
* 2个节点的cluster就不能挂掉任何1个节点了(leader可以得到1票<=1)


### zookeeper负载均衡和nginx负载均衡区别

zk的负载均衡是可以调控，nginx只是能调权重，其他需要可控的都需要自己写插件；但是nginx的吞吐量比zk大很多，应该说按业务选择用哪种方式。

### Zookeeper有哪几种几种部署模式？

部署模式：单机模式、伪集群模式、集群模式。


### 集群最少要几台机器，集群规则是怎样的?

集群规则为2N+1台，N>0，即3台。


### 集群支持动态添加机器吗？

其实就是水平扩容了，Zookeeper在这方面不太好。两种方式：

全部重启：关闭所有Zookeeper服务，修改配置之后启动。不影响之前客户端的会话。

逐个重启：在过半存活即可用的原则下，一台机器重启不影响整个集群对外提供服务。这是比较常用的方式。3.5版本开始支持动态扩容。

### Zookeeper对节点的watch监听通知是永久的吗？为什么不是永久的?

不是。官方声明：一个Watch事件是一个一次性的触发器，当被设置了Watch的数据发生了改变的时候，则服务器将这个改变发送给设置了Watch的客户端，以便通知它们。

为什么不是永久的，举个例子，如果服务端变动频繁，而监听的客户端很多情况
下，每次变动都要通知到所有的客户端，给网络和服务器造成很大压力。

一般是客户端执行getData(“/节点A”,true)，如果节点A发生了变更或删除，客户端会得到它的watch事件，但是在之后节点A又发生了变更，而客户端又没有设置watch事件，就不再给客户端发送。

在实际应用中，很多情况下，我们的客户端不需要知道服务端的每一次变动，我只要最新的数据即可。

### Zookeeper的java客户端都有哪些？

java客户端：zk自带的zkclient及Apache开源的Curator。

### chubby是什么，和zookeeper比你怎么看？

chubby是google的，完全实现paxos算法，不开源。zookeeper是chubby的开源实现，使用zab协议，paxos算法的变种。

### 说几个zookeeper常用的命令。

常用命令：ls、get、set、create、delete等。


### ZAB和Paxos算法的联系与区别？

相同点：
1. 两者都存在一个类似于Leader进程的角色，由其负责协调多个Follower进程的运行
2. Leader进程都会等待超过半数的Follower做出正确的反馈后，才会将一个提案进行提交
3. ZAB协议中，每个Proposal中都包含一个epoch值来代表当前的Leader周期，Paxos中名字为Ballot

不同点：

ZAB用来构建高可用的分布式数据主备系统(Zookeeper)，Paxos是用来构建分布式一致性状态机系统。

### Zookeeper的典型应用场景


Zookeeper是一个典型的发布/订阅模式的分布式数据管理与协调框架，开发人员可以使用它来进行分布式数据的发布和订阅。

通过对Zookeeper中丰富的数据节点进行交叉使用，配合Watcher事件通知机制，可以非常方便的构建一系列分布式应用中年都会涉及的核心功能，如：

1. 数据发布/订阅
2. 负载均衡
3. 命名服务
4. 分布式协调/通知
5. 集群管理
6. Master选举
7. 分布式锁
8. 分布式队列