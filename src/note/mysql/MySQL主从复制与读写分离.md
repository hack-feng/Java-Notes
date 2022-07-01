[toc]

# MySQL主从复制与读写分离

前言：现在大多企业对于数据库的业务应用，成熟的业务通常数据量都比较大，单台MySQL在安全性、高可用性和高并发方面都无法满足实际的需求；就需要配置多台主从数据库服务以实现读写分离

### 一、MySQL主从复制

MySQL的主从复制和MySQL的读写分离两者有着紧密联系，首先要部署主从复制，只有主从复制完成了，才能在此基础上进行数据的读写分离

#### 1.主从复制原理

##### 1.1mysql支持的复制类型：

①STATEMENT:基于语句的复制，在主服务器上执行sql语句，在从服务器上执行同样的语句，MySQL默认采用基于语句的复制，执行效率高

②ROW:基于行的复制，把改变的内容复制过去，而不是把命令在从服务器上执行一边

③MIXED:混合类型的复制，默认采用基于语句的复制，一旦发现基于语句无法精确复制时，就会采用基于行的复制

#### 2.主从复制的工作过程

主从复制核心部分就是两个日志、三个线程（高版本的mysql以及异步复制、半同步复制、全同步复制

核心重点：两个日志文件：二进制日志、中继日志

 、三个线程：master的dump thread、slave的IO、SQL

主要原理：master将数据保存在二进制日志中，IO向dump发出同步请求，dump把数据发送给IO线程。IO会写入到本地的中继日志，SQL线程会读取本地的日志数据，同步到自己的数据库中，完成同步

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701100840.png)

①Master节点将数据的改变记录成二进制日志(bin log),当Master上的数据发生改变时，则将其改变写入二进制日志中

②Slave节点会在一定时间间隔内对Master的二进制日志进行探测其是否发生改变，如果发生改变，则开始一个I/O线程请求Master的二进制时间

③同时Master节点为每个I/O线程启动一个dump线程，用于向其发生二进制事件，并保存至Slave节点本地的中继日志(Relay log)中，Slave节点将启动SQL线程从中继日志中读取二进制日志，在本地重放，即解析成sql语句逐一执行，使得其数据和Master节点的保持一致，最后I/O线程和SQL线程将进入睡眠状态，等待下一次被唤醒

**注：**中继日志通常会位于OS缓存中，所以中继日志的开销很小

复制过程有一个很重要的限制，即复制在Slave上是串行化的，也就是说Master上的并行更新操作不能在Slave上并行操作

```
主MySQL服务器做的增删改操作，都会写入自己的二进制日志（Binary log）
然后从MySQL从服务器打开自己的I/O线程连接主服务器，进行读取主服务器的二进制日志
I/O去监听二进制日志，一旦由新的数据，会发起请求连接
这时候会触发dump线程，dump thread响应请求，传送数据给I/O(dump线程要么处于等待，要么处于睡眠状态)
I/O接收到数据之后存放在中继日志
SQL thread线程会读取中继日志里的数据，存放到自己的服务器中

dump通过TP也就是网络的方式发送给IO
```

#### 3.MySQL四种同步方式：

##### 3.1异步复制(Async Replication):

主库将更新写入Binlog日志文件，不需要等待数据更新是否已经复制到从库中，就可以继续处理更多的请求，Master将事件写入binlog，但并不知道Slave是否或何时已经接收且已处理，在异步复制的机制的情况下，如果Master宕机，事务在Master上已经提交，但可能这些事务没有传到任何的Slave上，假设有Master–>Salve故障转移的机制，此时Slave也可能会丢失事务，Mysql复制默认是异步复制，异步复制提供了最佳性能

##### 3.2 同步复制（Sync Replication）

主库将更新写入Binlog日志文件后，需要等待数据更新已经复制到从库中，并且已经在从库执行成功，然后才能返回继续处理其它的请求，同步复制提供了最佳安全性，保证数据安全，数据不会丢失，但对性能有一定的影响

##### 3.3半同步复制（Semi-Sync Replication）

主库提交更新写入二进制日志文件后，等待数据更新写入了从服务器中继日志中，然后才能再继续处理其它请求，该功能确保至少有1个从库接收完主库传递过来的binlog内容已经写入到自己的relay log里面了，才会通知主库上面的等待线程，该操作完毕

半同步复制是最佳安全性于最佳性能 之间的一个折中

MySQL5.5版本之中引入了半同步复制功能，主从服务器必须安装半同步复制插件，才能开启该复制功能，如果等待超时，超过rpl_semi_sync_master_timeout参数设置时间(默认值为10000，表示10秒)，则关闭半同步复制。并自动转换为异步复制模式，当master dump线程发送完一个事务的所有事件之后，如果在rpl_semi_sync_master_timeout内，收到了从库的响应，则主从又重新恢复为增强半同步复制

ACK (Acknowledge character）即是确认字符。

##### 3.4增强半同步复制（lossless Semi-Sync Replication、无损复制）

增强半同步时在MySQL 5.7引入，其实半同步可以看成一个过渡功能，因为默认的配置就是增强半同步，大家一般说的半同步复制其实就是增强的半同步复制，也就是无损复制

增强半同步和半同步不同的是，等待ACK时间不同

rpl_semi_sync_master_wait_point =AFTER_SYNC(默认)

半同步的问题是因为等待ACK的点是Commit之后，此时Master已经完成数据变更，用户已经可以看到最新数据，当Binlog还未同步到Slave时，发生主从切换，那么此时从库是没有这个最新数据的，用户看到的还是老数据，增强半同步将等待ACK的点放在提交Commit之前，此时数据还未被提交，外界看不到数据变更，此时如果发生主从切换，新库依然还是老数据，不存在数据不一致的问题

### 二、读写分离原理

读写分离就是只在主服务器上写，只在从服务器上读，基本的原理是让主数据库处理事务性操作，而从数据库处理select查询，数据库复制被用来把主数据库上事务性操作导致的变更同步到集群中的从数据库

#### 1.什么是读写分离？

读写分离，基本的原理是让主数据库处理事务性增、删、改、删操作(INSERT、UPDATE、DELETE),而从数据库处理SELECT查询操作，数据库复制被用来把事务性操作导致的变更同步到集群中的从数据库

#### 2.读写分离的意义

- 因为数据库的“写”（写10000条数据可能要3分钟）操作是比较耗时的
- 但是数据库的“读”（读10000条数据可能只要5秒钟）
- 所以读写分离，解决的是，数据库的写入，影响了查询的效率

#### 3.什么时候配置读写分离：

数据库不一定要配置读写分离，如果程序使用数据库较多时，而更新少，查询多的情况下会考虑使用，利用数据库主从同步，再通过读写分离可以分担数据库压力，提高性能

#### 4.主从复制与读写分离：

在实际的生产环境中，对数据库的读和写都在同一个数据库服务器中，是不能满足实际需求的，无论实在安全性、高可用性还是高并发等各个方面都是完全不能满足实际需求，因此，通过主从复制的方式来同步数据，再通过读写分离来提升数据库的并发负载能力，有点类似于rsync，但不同的是rsync是对磁盘文件做备份，而MySQL主从复制是对数据库中的数据、语句做备份

#### 5.MySQL读写分离原理

目前较为常见的MySQL读写分离

##### 5.1基于程序代码内部实现

在代码中根据select、insert进行路由分类，这类方法也是目前生产环境应用最广泛的；优点是性能较好，因为在程序代码中实现，不需要增加额外的设备为硬件开支；缺点是需要开发人员来实现，运维人员无从下手，但并不是所有的应用都适合在程序代码中实现读写分离，像一些大型复杂的Java应用，如果在程序代码中实现读写分离对代码改动就较大

##### 5.2基于中间代理层实现

代理一般位于客户端和服务器之间，代理服务器接到客户端请求后通过判断后转发到后端数据库，有以下代表性程序

①、MySQL-Proxy，MySQL-Proxy为MySQL开源项目，通过其自带的lua脚本进行SQL判断

②、Atlas，是由奇虎360的web平台部基础架构团队开发维护的一个基于MySQL协议的数据中间层项目，它是在MySQL-Proxy0.8.2版本的基础上，对其进行了优化，增加了一些新的功能特性，360内部使用Atlas运行的MySQL，每天承载的读写请求数达几十亿条，支持事务以及存储过程

③、Amoeba，由陈思儒开发，作者曾就职于阿里巴巴，该程序由Java语言进行开发，阿里巴巴将其用于生产环境，但是它不支持事务和存储过程

由于使用MySQL Proxy需要写大量的Lua脚本，这些Lua并不是现成的，而是需要自己去写，这对于并不熟悉MySQL Proxy 内置变量和MySQL Protocol 的人理解非常困难。Amoeba是一个非常容易使用、可移植非常强的软件。因此它在生产环境中被广泛应用于数据库的代理层

```
Master服务器：192.168.50.128
slave1服务器：192.168.50.158
slave2服务器：192.168.50.168
```

#### 6.MySQL主从复制延迟

```
1.msater服务器高并发，形成大量事务
2.网络延迟
3.主从硬件设备导致
4.本来就不是同步复制、而是异步复制
从库优化MySQL参数，比如增大innodb、buffer_pool_size,让更多操作在MySQL内存中完成，减少磁盘操作
从库使用使用高性能主机，包括cpu强悍、内存加大，避免使用虚拟云主机，使用物理主机，这样提升了I/O方面性
从库使用SSD磁盘
网络优化，避免跨机房实现同步
```

### 三、搭建MySQL主从复制

#### 1.主服务器配置

##### 1.1下载ntp时间同步软件

```
systemctl stop firewalld.service 
setenforce 0
systemctl disable firewalld.service 
yum install -y ntp
```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701101050.png)

##### 1.2修改ntp配置文件

```
vim /etc/ntp.conf
在文件末尾添加
server 127.128.50.0		设置本地是时钟源
fudge 127.127.50.0 stratum 8		设置时间层级为8(限制在15)
```

![在这里插入图片描述](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701101223.png)

```
service ntpd start			开启ntp服务
```

![在这里插入图片描述](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701100717.png)

#### 2.主服务器MySQL配置

##### 2.1MySQL配置文件添加

```
配置文件修改
vim /etc/my.cnf
log-bin=master-bin
binlog_format = MIXED
log-slave-updates=true

systemcrl restart mysqld.service
```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701101436.png)

##### 2.2.二进制文件授权

```
grant replication slave on *.* to 'myslave'@'192.168.50%' identified by '123456';
show master status;
```

![在这里插入图片描述](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701101452.png)

#### 3.Slave服务器1 配置

###### 3.1下载时间同步

```
yum install -y ntp		下载ntp时间同步软件
```

![在这里插入图片描述](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701101524.png)

##### 3.2启动ntp服务

```
service ntpd start		启动ntpd服务器
/usr/sbin/ntpdate 192.168.50.128	和主服务器时间保持同步
crontab -e		设置周期性计划任务
crontab -l
*/30 * * * * /usr/sbin/ntpdate 192.168.50.128
```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701101556.png)

##### 3.3修改MySQL的配置文件

```
vim /etc/my.cnf
server-id = 2 修改，注意id与Master的不能相同，两个slave的也不能相同
relay-log=relay-log-bin 添加，开启中继日志，从主服务器上同步日志文件记录到本地
relay-log-index=slave-relay-bin.index 添加，定义中继日志文件的位置和名称，一般和relay-log在同一目录
relay_logg_recovery = 1 选配项

#当slave从库宕机后，加入relay-log损坏了，导致一部分中继日志没有处理，则自动放弃所有未执行的relay-log，并且重新从master上获取日志，这样就保证了relay-log的完整性，默认情况下该功能是关闭的，将relay_log_recovery的值设置为1时，可在slave从库上开启该功能，建议开启
```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701101638.png)

##### 3.4配置同步，

```
change master to master_host='192.168.50.128',master_user='myslave',master_password='123456',master_log_file='mysql-bin.000027',master_log_pos=451;
#配置同步，注意master_log+file和master_log_pos的值与master查询的一致

start slave; 启动报错。如果有报错执行rest slave；
show slave status\G; 查看slave状态

\#确保IO和SQL这两个线程都是YES，代表同步正常

Slave_IO_Running: Yes 负责与主机的通信
Slave_SQL_Running:Yes 负责自己的slave mysql进程
```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701101703.png)

##### 注：一般Slave_IO_Running:No的可能性

①:网络不通

②my.cnf配置有问题

③密码、file文件名、pos偏移量

④防火墙没有关闭

#### 4.Slave2服务器修改

![在这里插入图片描述](https://img-blog.csdnimg.cn/b195622672064e6caa3f4ee745a35954.png)

#### 5.测试

```
到master服务器上创建一个库，看看slave服务器有没有同步 

master服务器：
create database ceshizhu;

slave服务器查看
```

![在这里插入图片描述](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701100718.png)

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701101751.png)
![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701101803.png)

### 四、数据库主从延迟的原因与解决方案

**主从延迟是怎样定义的呢？与主从数据同步相关的时间点有三个**

①主库执行完一个事务，写入binlog，我们这个把这个时刻记为T1；

②主库同步数据给从库，从库接受完这个binlog的时刻，记录为T2；

③从库执行完这个事务，这个时刻记录为T3

#### 1.哪些情况会导致主从延迟？

1.如果从库所在的机器比主库的机器性能差，会导致主从延迟，这种情况比较好解决，只需选择主从 库一样规格的机器就好

2.如果从库的压力大，也会导致主从延迟，比如主库直接影响业务，大家可能使用会比较可知，因此一般查询都打到从库了，结果导致从库查询消耗大量CPU，影响同步速度，最后导致主从延迟，这种情况的话，可以搞一主多从架构，即多接几个从库分摊读的压力，另外还可以把binlog接入到Hadoop这类系统，让它们提供查询的能力

3.大事务也会导致主从延迟，如果一个事务执行就要10分钟，那么主库执行完后，给到从库执行，最后这个事务可能就会导致从库延迟10分钟，日常开发中，就会特别强调，不要一次性delete太多SQL，需要分批进行，其实也是为了避免大事务，另外，大表的DDL语句，也会导致大事务

4.网络延迟也会导致主从延迟，这种情况只能优化自己的网络

5.如果从数据库过多也会导致主从延迟，因此要避免复制的从节点数量过多，从库数据一般以3-5个为宜

6.低版本的MySQL只支持单线程复制，如果主库并发高，来不及传送到从库，就会导致延迟，可以换用高版本的MySQL，可以支持多线程复制

```
如果同步失败可使用以下方法尝试解决
#忽略当前的错误执行下一步的同步
先 stop slave;
① slave数据库中：SET GLOBAL SQL_SLAVE_SKIP_COUNTER=1;
START SLAVE;
问题：I/O线程 一直处于connecting
第一件事，看laster error,报错项内容是为支点找不到
先 stop slave;
② slave数据库中：
CHANGE MASTER TO MASTER_LOG_FILE=‘mysql-bin.000001’,MASTER_LOG_POS=0;
首先遇到这个是因为binlog位置索引处的问题，生产环境下不要直接reset slave（删除change master 操作的）；
reset slave会将主从同步的文件以及位置恢复到初始状态，一开始没有数据还好，有数据的话，相当于重新开始同步，可能会出现一些问题；
一般做主从同步，都是要求以后的数据实现主从同步，而对于旧的数据完全可以使用数据库同步工具先将数据库同步，完了再进行主从同步；
```

#### 2.解决方法

```
1.打开主（master)服务器，进入mysql
2.执行flush logs；//这时主服务器会重新创建一个binlog文件；
3.在主服务上执行show master status\G;显示如下：
4.来到从服务器的mysql；
5.stop slave;
6.change master to master_log_file='mysql-bin.000012',master_log_pos=154;//这里的file和pos都是上面主服务器master显示的。
7.start slave; //这时候就应可以了
```

### 五、搭建MySQL读写分离

**环境准备**

```
msater服务器：192.168.50.128		mysql 5.7	
Slave服务器：192.168.50.158			mysql 5.7
Slave服务器：192.168.50.168			jdk1.6、Amoeba
Amoeba服务器：192.168.50.198		mysql
客户端：192.168.50.108				
基于一主两从的基础上
```

#### 1.安装Java环境

```
因为 Amoeba 基于是 jdk1.5 开发的，所以官方推荐使用 jdk1.5 或 1.6 版本，高版本不建议使用。
将jdk-6u14-linux-x64.bin 和 amoeba-mysql-binary-2.2.0.tar.gz.0 上传到/opt目录下

将要用的软件包上传到opt目录下
cd /opt
cp jdk-6u14-linux-x64.bin /usr/local/
cd /usr/local/
chmod +x jdk-6u14-linux-x64.bin
./jdk-6u14-linux-x64.bin 按空格到最后一行，输入yes回车
```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701101843.png)

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701101850.png)

```
mv jdk1.6.0_14/ /usr/local/jdk1.6

添加环境变量
vim /etc/profile
末尾行添加
export JAVA_HOME=/usr/local/jdk1.6
export CLASSPATH=$CLASSPATH:$JAVA_HOME/lib:$JAVA_HOME/jre/lib
export PATH=$JAVA_HOME/lib:$JAVA_HOME/jre/bin/:$PATH:$HOME/bin
export AMOEBA_HOME=/usr/local/amoeba
export PATH=$PATH:$AMOEBA_HOME/bin

source /etc/profile		刷新配置文件
java -version			查看版本号
```

![在这里插入图片描述](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701100719.png)

![在这里插入图片描述](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701100720.png)

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701101943.png)

```
##安装Amoeba软件##

mkdir /usr/local/amoeba
tar zxf amoeba-mysql-binary-2.2.0.tar.gz -C /usr/local/amoeba/
chmod -R 755 /usr/local/amoeba/
/usr/local/amoeba/bin/amoeba
//如显示amoeba start|stop 说明安装成功
```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102016.png)

#### 2.配置Amoeba读写分离，两个Slave服务器读负载均衡

先在Master、Slave1、Slave2的MySQL上开放权限给Amoeba访问

```
grant all on *.* to test@'192.168.50.%' identified by '123456';
```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102035.png)

**amoeba服务器配置amoeba服务**

```
cd /usr/local/amoeba/conf/
cp amoeba.xml amoeba.xml.bak
cp dbServers.xml dbServers.xml.bak

vim amoeba.xml 修改amoeba配置文件
30修改
<property name="user">amoeba</property>
32修改
<property name="password">123456</property>
115修改
<property name="defaultPool">master</property>
117去掉注释–
<property name="writePool">master</property>
<property name="readPool">slaves</property>
```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102114.png)

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102126.png)

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102137.png)

**修改数据库配置文件**

```
vim dbServers.xml               #修改数据库配置文件

23注释掉
作用：默认进入test库 以防mysql中没有test库时，会报错
<!-- mysql schema 
<property name="schema">test</property>
-->
26修改
<!-- mysql user -->
<property name="user">test</property>
28-30去掉注释
<property name="password">123456</property>
45修改，设置主服务器的名Master
<dbServer name="master"  parent="abstractServer">
48修改，设置主服务器的地址
<property name="ipAddress">192.168.50.128</property>
52修改，设置从服务器的名slave1
<dbServer name="slave1"  parent="abstractServer">
55修改，设置从服务器1的地址
<property name="ipAddress">192.168.50.158</property>
58复制上面6行粘贴，设置从服务器2的名slave2和地址
<dbServer name="slave2"  parent="abstractServer">
<property name="ipAddress">192.168.50.168</property>
65修改
<dbServer name="slaves" virtual="true">
71修改
<property name="poolNames">slave1,slave2</property>

/usr/local/amoeba/bin/amoeba start&         #启动Amoeba软件，按ctrl+c 返回
netstat -anpt | grep java             #查看8066端口是否开启，默认端口为TCP 8066

```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102316.png)

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102328.png)

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102335.png)

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102341.png)



#### 3.客户端测试(192.168.50.108)读写分离

在192.168.50.108客户端服务器上进行测试，使用yum安装MySQL虚拟客户端

```
yum install -y mariadb mariadb-server

mysql -u amoeba -p123456 -h 192.168.223.12 -P8066

通过amoeba服务器代理访问MySQL，在通过客户端连接MySQL后写入的数据只有主服务器会记录，然后同步给从服务器
```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102557.png)

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102619.png)


**主服务器**

```
create database bangde;
use bangde;

create table ces(id int(10),name varchar(10));
show tables;
```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102659.png)

**客户机操作**

```
use bangde
show tables;
desc ces;
insert into ces values(1,'xiaowang');
```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102728.png)

**三台MySQL服务器查看数据**

```
select * from bangde.ces;
```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102805.png)

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102813.png)

**关闭两台从服务器的同步服务测试读写分离**

```
stop slave;		关闭服务，无法同步，主服务器或客户端写入数据时将不再同步到数据库
```

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701102830.png)

**三台服务器都插入数据**

```
master服务器
insert into ces values(2,'master');

slave1服务器
insert into ces values(3,‘slave1’);

slave2服务器
insert into ces values(4,‘slave2’);
```

![在这里插入图片描述](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701100722.png)

**各台服务器查看表内刚刚插入的数据**

在客户端上只能看到slave2或slave1插入的数据，这是由于我两台从服务器的同步都关闭了，不能再获取到主服务器和客户端写入的数据，而客户端写入的数据只是写入主服务器中，但从服务器关闭同步后获取不到，所以也看不到数据。

主服务器可以看到自己插入的数据和客户端插入的数据，而从服务器插入的数据是看不到的，

两台从服务器因为同步服务关闭，所以只能看到自己插入的数据，其他数据看不到

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701103040.png)

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701103049.png)

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701103053.png)

![img](http://file.xiaoxiaofeng.site/blog/image/2022/07/01/20220701103117.png)

### 总结

通过上述内容以及案例对MySQL主从同步原理、读写分离原理简单叙述了一下，下面通过两个实验对于新手小白而言，如果没有理解主从和读写的原理，可以尝试做个实验，可以更好的理解其中的原理

其中对于主从同步原理一定要熟知，对于我们排错有很大的帮助

**主从同步原理**

1. 首先client端（tomcat）将数据写入到master节点的数据库中，master节点会通知存储引擎提交事务，同时会将数据以（基于行、基于sql、基于混合）的方式保存在二进制日志中
2. SLAVE节点会开启I/O线程，用于监听master的二进制日志的更新，一旦发生更新内容，则向master的dump线程发出同步请求
3. master的dump线程在接收到SLAVE的I/O请求后，会读取二进制文件中更新的数据，并发送给SLAVE的I/O线程
4. SLAVE的I/O线程接收到数据后，会保存在SLAVE节点的中继日志中
5. 同时，SLAVE节点钟的SQL线程，会读取中继日志钟的熟，更新在本地的mysql数据库中
6. 最终，完成slave——>复制master数据，达到主从同步的效果

**如何查看主从同步状态是否成功**

在从服务器输入命令show slave status\G,查看主从信息进行查看，里面有IO线程的状态信息，有master服务器的IP地址、端口、事务开始号

当 slave_io_running 和 slave_sql_running 都显示为yes时，表示主从同步状态成功

在主服务器中写入数据，去从服务器中查看数据是否写入或数据是否存在，如果存在说明主从同步成功

**I/O和SQL不是yes的情况，排除方法**

1.查看网络问题，

2.查看防火墙规则和增强机制

3.查看从服务器内的slave是否开启

4.查看两台从服务器的server-id是否由于相同导致只能连接上一台

5.master_log_file 和 master_log_pos 的值是否与master查询一致，也就是偏移量和文件名是否一致

6.查看配置文件是否配置正确

**show slave status能看到哪些信息**

IO线程的状态信息；

master服务器的IP地址、端口、事务开始位置

最近一次的报错信息和报错位置等

**主从复制（延迟）有哪些可能**

IO堵塞导致复制延迟，读写数据量过大

主从复制单线程，如果主库写并发太大，来不及传送到从库，就会导致延迟

主服务器的负载过大，被多个睡眠或者僵尸线程占用，导致系统负载过大

从库硬件比主库差，导致复制延迟

网络延迟





转载：https://blog.csdn.net/Hanxuhanhan/article/details/125543828