# docker 下配置Mysql主从复制

## docker下安装、启动mysql
docker下安装mysql 8.0.15
~~~
docker pull mysql:8.0.15
~~~

启动mysql-master，当作主库
~~~
docker run -p 3320:3306 --name mysql_master -e MYSQL_ROOT_PASSWORD=123456 -d 7bb2586065cd
~~~

进入docker容器修改Mysql
~~~
[root@k8s-n1 mysql]# docker exec -it mysql_master /bin/sh

# mysql -u root -p

mysql> ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'test001';
~~~

启动mysql-slave，当作从库
~~~
docker run  -p 3321:3306 --name mysql_slave -e MYSQL_ROOT_PASSWORD=123456 -d 7bb2586065cd
~~~

进入docker容器修改Mysql
~~~
[root@k8s-n1 mysql]# docker exec -it mysql_slave /bin/sh

# mysql -u root -p

mysql> ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'test001';
~~~

## 配置Master(主)
~~~
[root@k8s-n1 mysql]# docker exec -it mysql_master /bin/sh
~~~

`cd /etc/mysql`切换到/etc/mysql目录下，然后`vi my.cnf`对my.cnf进行编辑。
此时会报出bash: `/bin/sh: 3: vi: not found`，需要我们在docker容器内部自行安装vim。
使用`apt-get install` vim命令安装vim

会出现如下问题：
~~~
Reading package lists... Done
Building dependency tree       
Reading state information... Done
E: Unable to locate package vim
~~~

执行`apt-get update`，然后再次执行`apt-get install vim`即可成功安装vim。
然后我们就可以使用vim编辑my.cnf，在my.cnf中添加

~~~
[mysqld]
## 同一局域网内注意要唯一
server-id=99  
## 开启二进制日志功能，可以随便取（关键）
log-bin=mysql-bin
~~~

下一步在Master数据库创建数据同步用户
~~~
CREATE USER 'slave'@'%' IDENTIFIED BY '123456';

ALTER USER 'slave'@'%' IDENTIFIED WITH mysql_native_password BY 'test001';
~~~

授予用户 slave REPLICATION SLAVE权限和REPLICATION CLIENT权限，用于在主从库之间同步数据。
~~~
GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'slave'@'%';
~~~
![创建用户图片](https://image.xiaoxiaofeng.site/article/img/2022/11/07/xxf-20221107144222.jpg)

## 配置Slave(从)
和配置Master(主)一样，在Slave配置文件my.cnf中添加如下配置：
~~~
[mysqld]
## 设置server_id,注意要唯一
server-id=101  
## 开启二进制日志功能，以备Slave作为其它Slave的Master时使用
log-bin=mysql-slave-bin   
## relay_log配置中继日志
relay_log=edu-mysql-relay-bin  
~~~

配置完成后也需要重启mysql服务和docker容器，操作和配置Master(主)一致。

## 链接Master(主)和Slave(从)

在Master进入mysql，执行`show master status;`

![笑小枫专属图片](https://image.xiaoxiaofeng.site/article/img/2022/11/07/xxf-20221107144256.jpg)

File和Position字段的值后面将会用到，在后面的操作完成之前，需要保证Master库不能做任何操作，否则将会引起状态变化，File和Position字段的值变化。

在Slave 中进入 mysql，执行
~~~
change master to master_host='172.17.0.8', master_user='slave', master_password='test001', master_port=3306, master_log_file='mysql-bin.000002', master_log_pos=994, master_connect_retry=30;
~~~

* 命令说明

![笑小枫专属图片](https://image.xiaoxiaofeng.site/article/img/2022/11/07/xxf-20221107144301.jpg)

    master_host ：Master的地址，指的是容器的独立ip,可以通过docker inspect --format='{{.NetworkSettings.IPAddress}}' 容器名称|容器id查询容器的ip
    
    master_port：Master的端口号，指的是容器的端口号
    
    master_user：用于数据同步的用户
    
    master_password：用于同步的用户的密码
    
    master_log_file：指定 Slave 从哪个日志文件开始复制数据，即上文中提到的 File 字段的值
    
    master_log_pos：从哪个 Position 开始读，即上文中提到的 Position 字段的值
    
    master_connect_retry：如果连接失败，重试的时间间隔，单位是秒，默认是60秒

在Slave 中的mysql终端执行`show slave status \G;`用于查看主从同步状态。

![笑小枫专属图片](https://image.xiaoxiaofeng.site/article/img/2022/11/07/xxf-20221107144310.png)

* Slave_IO_Running：负责从库去主库读取二进制日志，并写入到从库的中继日志
* Slave_SQL_Running：负责将中继日志转换成SQL语句后执行

正常情况下，SlaveIORunning 和 SlaveSQLRunning 都是No，因为我们还没有开启主从复制过程。
使用`start slave;`开启主从复制过程，然后再次查询主从同步状态`show slave status \G;`。

![笑小枫专属图片](https://image.xiaoxiaofeng.site/article/img/2022/11/07/xxf-20221107144315.png)

SlaveIORunning 和 SlaveSQLRunning 都是Yes，说明主从复制已经开启。此时可以测试数据同步是否成功。

## 主从复制排错

![笑小枫专属图片](https://image.xiaoxiaofeng.site/article/img/2022/11/07/xxf-20221107144320.png)

使用start slave开启主从复制过程后，如果SlaveIORunning一直是Connecting，则说明主从复制一直处于连接状态，这种情况一般是下面几种原因造成的，我们可以根据 Last_IO_Error提示予以排除。

1、网络不通

    检查ip,端口,这里docker虽然映射出去的mysql端口是3320，但通过内网访问仍然是3306

2、密码不对

    检查是否创建用于同步的用户和用户密码是否正确

3、pos不对

    检查Master的 Position

## 测试主从复制测试

在主库（master）创建test数据库，并添加test表，然后可以看到从库中也出现了对应的数据库和表

![笑小枫专属图片](https://image.xiaoxiaofeng.site/article/img/2022/11/07/xxf-20221107144324.png)