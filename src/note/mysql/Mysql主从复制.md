## 一、无聊的理论知识

MySQL的主从复制和MySQL的读写分离两者有着紧密联系，首先要部署主从复制，只有主从复制完成了，才能在此基础上进行数据的读写分离。

**为什么要主从复制** 

> 注意： MySQL是现在普遍使用的数据库，但是如果宕机了必然会造成数据丢失。为了保证MySQL数据库的可靠性。就要会一些提高可靠性的技术。

**如何解决性能问题** 

生活中有很形象的例子，比如你去超市买东西，如果只有一个收银台，只可以排一队一次等待，如果有多个收银台，就可以排多队，大大提升了效率。

### 1. 主从复制原理

**mysql支持的复制类型：**

①STATEMENT:基于语句的复制，在主服务器上执行sql语句，在从服务器上执行同样的语句，MySQL默认采用基于语句的复制，执行效率高

②ROW:基于行的复制，把改变的内容复制过去，而不是把命令在从服务器上执行一边

③MIXED:混合类型的复制，默认采用基于语句的复制，一旦发现基于语句无法精确复制时，就会采用基于行的复制

### 2. 主从复制的工作过程

主从复制核心部分就是两个日志、三个线程（高版本的mysql以及异步复制、半同步复制、全同步复制

核心重点：两个日志文件：二进制日志、中继日志、三个线程：master的dump thread、slave的IO、SQL

主要原理：master将数据保存在二进制日志中，IO向dump发出同步请求，dump把数据发送给IO线程。IO会写入到本地的中继日志，SQL线程会读取本地的日志数据，同步到自己的数据库中，完成同步

![image-20231113175747700](https://image.xiaoxiaofeng.site/blog/2023/11/15/xxf-20231115165032.png?xxfjava)

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

### 3. MySQL四种同步方式

**异步复制(Async Replication)**

主库将更新写入Binlog日志文件，不需要等待数据更新是否已经复制到从库中，就可以继续处理更多的请求，Master将事件写入binlog，但并不知道Slave是否或何时已经接收且已处理，在异步复制的机制的情况下，如果Master宕机，事务在Master上已经提交，但可能这些事务没有传到任何的Slave上，假设有Master–>Salve故障转移的机制，此时Slave也可能会丢失事务，Mysql复制默认是异步复制，异步复制提供了最佳性能

**同步复制（Sync Replication）**

主库将更新写入Binlog日志文件后，需要等待数据更新已经复制到从库中，并且已经在从库执行成功，然后才能返回继续处理其它的请求，同步复制提供了最佳安全性，保证数据安全，数据不会丢失，但对性能有一定的影响

**半同步复制（Semi-Sync Replication）**

主库提交更新写入二进制日志文件后，等待数据更新写入了从服务器中继日志中，然后才能再继续处理其它请求，该功能确保至少有1个从库接收完主库传递过来的binlog内容已经写入到自己的relay log里面了，才会通知主库上面的等待线程，该操作完毕

半同步复制是最佳安全性于最佳性能 之间的一个折中

MySQL5.5版本之中引入了半同步复制功能，主从服务器必须安装半同步复制插件，才能开启该复制功能，如果等待超时，超过rpl_semi_sync_master_timeout参数设置时间(默认值为10000，表示10秒)，则关闭半同步复制。并自动转换为异步复制模式，当master dump线程发送完一个事务的所有事件之后，如果在rpl_semi_sync_master_timeout内，收到了从库的响应，则主从又重新恢复为增强半同步复制

ACK (Acknowledge character）即是确认字符。

**增强半同步复制（lossless Semi-Sync Replication、无损复制）**

增强半同步时在MySQL 5.7引入，其实半同步可以看成一个过渡功能，因为默认的配置就是增强半同步，大家一般说的半同步复制其实就是增强的半同步复制，也就是无损复制

增强半同步和半同步不同的是，等待ACK时间不同

rpl_semi_sync_master_wait_point =AFTER_SYNC(默认)

半同步的问题是因为等待ACK的点是Commit之后，此时Master已经完成数据变更，用户已经可以看到最新数据，当Binlog还未同步到Slave时，发生主从切换，那么此时从库是没有这个最新数据的，用户看到的还是老数据，增强半同步将等待ACK的点放在提交Commit之前，此时数据还未被提交，外界看不到数据变更，此时如果发生主从切换，新库依然还是老数据，不存在数据不一致的问题

## 二、docker下安装、启动mysql
docker下安装mysql 8.0.15
~~~shell
docker pull mysql:8.0.15
~~~

### 1. 安装主库

启动mysql-master，当作主库

~~~shell
docker run -p 3306:3306 --name mysql_master -e MYSQL_ROOT_PASSWORD=123456 -d 7bb2586065cd
~~~

> 此时启动已完成，在docker启动镜像时密码加密使用的是caching_sha2_password，
> 在服务器端启动默认使用mysql_native_password 加密的，
> 如需要使用外部工具连接，需要进入docker容器重置root密码。

进入docker容器修改Mysql远程连接的密码
~~~shell
docker exec -it mysql_master /bin/sh

# mysql -u root -p

# 输入登录密码（设置的MYSQL_ROOT_PASSWORD）：123456

mysql> ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'test001';
~~~

> 退出数据库设置和docker容器，使用exit命令

![image-20231110135242436](https://image.xiaoxiaofeng.site/blog/2023/11/10/xxf-20231110135348.png?xxfjava)


### 2. 安装从库

启动mysql-slave，当作从库
~~~shell
docker run  -p 3307:3306 --name mysql_slave -e MYSQL_ROOT_PASSWORD=123456 -d 7bb2586065cd
~~~

进入docker容器修改Mysql远程连接的密码
~~~shell
docker exec -it mysql_slave /bin/sh

# mysql -u root -p

# 输入登录密码（设置的MYSQL_ROOT_PASSWORD）：123456

mysql> ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'test001';
~~~

安装完主库和从库后如下所示：

![image-20231110150836045](https://image.xiaoxiaofeng.site/blog/2023/11/10/xxf-20231110150836.png?xxfjava)

## 三、配置Master(主)

~~~shell
docker exec -it mysql_master /bin/sh
~~~

`cd /etc/mysql`切换到/etc/mysql目录下，然后`vi my.cnf`对my.cnf进行编辑。
此时会报出bash: `/bin/sh: 3: vi: not found`，需要我们在docker容器内部自行安装vim。
使用`apt-get install vim`命令安装vim

如果出现如下问题：
~~~shell
Reading package lists... Done
Building dependency tree       
Reading state information... Done
E: Unable to locate package vim
~~~

执行`apt-get update`，然后再次执行`apt-get install vim`即可成功安装vim。

> 如果docker内部安装不了Vim，可以将文件拷贝出来，修改完之后在拷贝进去，或者启动时使用docker -v的方式挂载
>
> ```shell
> #将容器中的文件拷贝出来
> sudo docker cp 容器ID:/etc/mysql/my.cnf /data/mysql/master
> #将容器中的文件拷贝回去
> sudo docker cp /data/mysql/master/my.cnf  容器ID:/etc/mysql/
> ```

然后我们就可以使用vim编辑my.cnf，在my.cnf中添加

~~~shell
[mysqld]
## 同一局域网内，注意要唯一
server-id=99  
## 开启二进制日志功能，可以随便取（关键）
log-bin=mysql-bin
~~~

> **需要重启mysql服务**，`docker restart 容器ID`

下一步在Master数据库创建数据同步用户

~~~shell
CREATE USER 'slave'@'%' IDENTIFIED BY '123456';
ALTER USER 'slave'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
~~~

授予用户 slave REPLICATION SLAVE权限和REPLICATION CLIENT权限，用于在主从库之间同步数据。
~~~shell
GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'slave'@'%';
~~~
![创建用户图片](https://image.xiaoxiaofeng.site/article/img/2022/11/07/xxf-20221107144222.jpg)

## 四、配置Slave(从)
和配置Master(主)一样，在Slave配置文件my.cnf中添加如下配置：
~~~shell
[mysqld]
## 设置server_id,注意要唯一
server-id=101  
## 开启二进制日志功能，以备Slave作为其它Slave的Master时使用
log-bin=mysql-slave-bin   
## relay_log配置中继日志
relay_log=edu-mysql-relay-bin
~~~

**配置完成后也需要重启mysql服务和docker容器**，操作和配置Master(主)一致。

## 五、链接Master(主)和Slave(从)

在Master进入mysql，执行`show master status;`

![笑小枫专属图片](https://image.xiaoxiaofeng.site/article/img/2022/11/07/xxf-20221107144256.jpg)

File和Position字段的值后面将会用到，在后面的操作完成之前，需要保证Master库不能做任何操作，否则将会引起状态变化，File和Position字段的值变化。

在Slave 中进入 mysql，执行
~~~shell
change master to master_host='172.17.0.8', master_user='slave', master_password='test001', master_port=3306, master_log_file='mysql-bin.000002', master_log_pos=994, master_connect_retry=30;
~~~

* 命令说明

![笑小枫专属图片](https://image.xiaoxiaofeng.site/article/img/2022/11/07/xxf-20221107144301.jpg)

```shell
master_host ：Master的地址，指的是容器的独立ip,可以通过docker inspect --format='{{.NetworkSettings.IPAddress}}' 容器名称|容器id查询容器的ip。如果不是docker直接放服务器的IP地址就行，保证两个服务器互通即可

master_port：Master的端口号，指的是容器的端口号

master_user：用于数据同步的用户

master_password：用于同步的用户的密码

master_log_file：指定 Slave 从哪个日志文件开始复制数据，即上文中提到的 File 字段的值

master_log_pos：从哪个 Position 开始读，即上文中提到的 Position 字段的值

master_connect_retry：如果连接失败，重试的时间间隔，单位是秒，默认是60秒
```

在Slave 中的mysql终端执行`show slave status;`用于查看主从同步状态。

![笑小枫专属图片](https://image.xiaoxiaofeng.site/article/img/2022/11/07/xxf-20221107144310.png)

* Slave_IO_Running：负责从库去主库读取二进制日志，并写入到从库的中继日志
* Slave_SQL_Running：负责将中继日志转换成SQL语句后执行

正常情况下，SlaveIORunning 和 SlaveSQLRunning 都是No，因为我们还没有开启主从复制过程。
使用`start slave;`开启主从复制过程，然后再次查询主从同步状态`show slave status;`。

![笑小枫专属图片](https://image.xiaoxiaofeng.site/article/img/2022/11/07/xxf-20221107144315.png)

SlaveIORunning 和 SlaveSQLRunning 都是Yes，说明主从复制已经开启。此时可以测试数据同步是否成功。

## 六、主从复制排错

###  1. 错误：error connecting to master 'xxxx' - retry-time: 30 retries: xx

![笑小枫专属图片](https://image.xiaoxiaofeng.site/article/img/2022/11/07/xxf-20221107144320.png)

使用start slave开启主从复制过程后，如果SlaveIORunning一直是Connecting，则说明主从复制一直处于连接状态，这种情况一般是下面几种原因造成的，我们可以根据 Last_IO_Error提示予以排除。

1、网络不通

    检查ip,端口,这里docker虽然映射出去的mysql端口是3320，但通过内网访问仍然是3306

2、密码不对

    检查是否创建用于同步的用户和用户密码是否正确

3、pos不对

    检查Master的 Position



### 2. 错误：ERROR 1872 (HY000): Slave failed to initialize relay log info structure from the repository

检查my.cnf，是否指定relay_log，指定的relay_log是否被其他server占用。

解决方法：修改my.cnf指定relay_log，然后重启服务。

先执行`reset slave`命令，在执行以下命令：

~~~shell
change master to master_host='172.17.0.8', master_user='slave', master_password='test001', master_port=3306, master_log_file='mysql-bin.000002', master_log_pos=994, master_connect_retry=30;
~~~
最后启动`start slave;`

### 3. 操作从库配置后，主库发生变更

在master`show master status;`查询主库最新的状态

然后执行`reset slave`命令，再执行更新后的链接命令：

~~~shell
change master to master_host='172.17.0.8', master_user='slave', master_password='test001', master_port=3306, master_log_file='mysql-bin.000002', master_log_pos=994, master_connect_retry=30;
~~~

最后启动`start slave;`

## 七、测试主从复制测试

在主库（master）创建test数据库，并添加test表，然后可以看到从库中也出现了对应的数据库和表

![笑小枫专属图片](https://image.xiaoxiaofeng.site/article/img/2022/11/07/xxf-20221107144324.png)

## 八、给运行中的数据库添加从库

给运行中的数据库添加从库操作和上述添加从库操作一致。

需要注意几点

1. 主库是否开启binlog日志。如果没有需要开启。
2. 主库运行中master_log_pos是不断变化的，要注意节点。
3. 需要先将主库的数据备份到从库，保证数据的一致性。

## 九、总结

本文主要讲解了Mysql主从复制的操作，如果有多个从库，重复执行从库的操作即可。

**切记：对数据的增删改操作一定要在主库上操作，不要在从库上操作。**



本文到此就结束了，有问题就找**笑小枫**。

点个关注和收藏吧，不然就👇👇👇👇👇👇👇👇👇

![image-20231110154837139](https://image.xiaoxiaofeng.site/blog/2023/11/10/xxf-20231110170138.png?xxfjava)

