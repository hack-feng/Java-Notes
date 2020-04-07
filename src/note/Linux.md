
### 查看是否有java程序启动
ps -ef|grep java

### 杀掉某个进程
kill -9 xxx(进程id)

### 批量杀掉进程
ps -ef |grep xxx  |awk '{print $2}'|xargs kill -9

### 启动服务
systemctl start xxx

### 重启服务
systemctl restart xxx

### 禁用服务
systemctl disable xxx

### 停止tomcat:在tomcat /bin目录下执行
sh shutdown.sh

### 启动tomcat:在tomcat /bin目录下执行
sh startup.sh

### 下载工具安装包
wget xxx

### 解压下载的压缩包
tar -zxvf xxx

### 打压缩包:将目录/etc/sysconfig/目录下的文件打包成文件sysconfig.tar文件，并且放在当前目录中
tar -cvf sysconfig.tar /etc/sysconfig

### 将目录/etc/sysconfig/目录下的文件添加到文件sysconfig.tar文件中去
tar -rvf sysconfig.tar /etc/sysconfig/

### 创建文件
touch a.txt

### 删除文件
rm a.txt

### 查看实时日志
tail -f cataline.out

### 退出查看实时日志
ctrl + c

### 安装git
yum -y install git  ### 版本为1.7.1
wget https://github.com/git/git/archive/v2.2.1.tar.gz

### 查看git版本
git --version

### 卸载git
yum remove git

# Docker 命令

### 查看所有的镜像
docker images

### 停止所有的容器
docker stop $(docker ps -a -q)

### 查看所有的容器
docker ps -a

### 获取所有的容器的容器id
docker ps -a -q

### 删除容器
docker rm 容器id

### 删除所有的容器
docker rm $(docker ps -a -q)

### 删除镜像，需要先删除容器，然后才可以删除镜像。（未验证）
docker rmi 镜像id

### 获取所有镜像的id
docker images -q

### 删除所有的镜像
docker rmi -f $(docker images -q)

### 构建镜像(注意镜像名称后面加   "空格.")
docker build -t 镜像名称 .

### 运行docker镜像
docker run 镜像名称

### 映射指定的ip启动
docker run -d -p 宿主ip:虚拟机ip 镜像名称

### 查看docker运行的日志log
docker logs 容器id(CONTAINER ID)

~~~
mysql操作

1.终端启动MySQL：/etc/init.d/mysql start； 
2.登录MySQL：mysql -uroot -p (用root账户登录),然后输入密码； 
3.查看所有的数据库名字：show databases; 
4.选择一个数据库操作： use database_name; 
5.查看当前数据库下所有的表名：show tables; 
6.创建一个数据库：create database database_name; 
7.删除一个数据库：drop database database_name; 
8.创建一个表: create table mytest( uid bigint(20) not null, uname varchar(20) not null); 
9.删除一个表: drop table mytest; 
10.SQL插入语句：insert into table_name(col1,col2) values(value1,value2); 
11.SQL更新语句：update table_name set col1='value1',col2='value2' where where_definition; 
12.SQL查询语句：select * from table_name where.......(最复杂的语句) 1
3.SQL删除语句：delete from table_name where... 
14.增加表结构的字段：alert table table_name add column field1 date ,add column field2 time... 
15.删除表结构的字段：alert table table_name drop field1; 
16.查看表的结构：show columns from table_name; 
17.limit 的使用：select * from table_name limit 3；//每页只显示3行 select * from table_name limit 3,4 //从查询结果的第三个开始，显示四项结果。 此处可很好的用来作分页处理。 
18.对查询结果进行排序: select * from table_name order by field1,orderby field2;多重排序
19.退出MySQL:exit; 
20.删除表中所有数据： truncate table 数据表名称 （不可恢复）


1、根据端口号得到其占用的进程的详细信息
netstat -tlnp|grep 80
tcp        0      0 192.168.33.10:80            0.0.0.0:*                   LISTEN      5014/httpd
tcp        0      0 0.0.0.0:48054               0.0.0.0:*                   LISTEN      5386/java

2、一次性的清除占用80端口的程序
lsof -i :80|grep -v "PID"|awk '{print "kill -9",$2}'|sh

3、手工终止进程的运行
kill 5014
如果终止不了，可以强制终止
kill -9 5014

4、查看已经开放的端口：
firewall-cmd --list-ports

5、开启端口
firewall-cmd --zone=public --add-port=80/tcp --permanent

命令含义：
–zone ###作用域
–add-port=80/tcp ###添加端口，格式为：端口/通讯协议
–permanent ###永久生效，没有此参数重启后失效

6、防火墙设置
firewall-cmd --reload ###重启firewall
systemctl stop firewalld.service ###停止firewall
systemctl disable firewalld.service ###禁止firewall开机启动
~~~

### 建立服务挂载(NFS)
#### 服务器端配置

* 查看是否安装nfs
~~~
[root@bogon ~]# rpm -qa | grep nfs
[root@bogon ~]# rpm -qa | grep rpcbind
~~~

* 安装nfs
~~~ 
[root@bogon ~]# yum -y install nfs-utils rpcbind 
~~~

* 创建需要共享的目录 
~~~
[root@bogon test]# /data/test
~~~

* 编辑nfs配置  
~~~
vi /etc/exports
~~~

* 配置生效
~~~
exportfs -r
~~~

* 在exports添加：`/data/test *(insecure,rw)`
* 查看nfs共享的目录 
~~~
exportfs -av
~~~

* 启动rpcbind、nfs服务
~~~
[root@bogon lys]# service rpcbind start
[root@bogon lys]# service nfs start
~~~

#### 客户端配置

* 配置host文件 `192.168.2.10 maple`
~~~
vi /etc/host
~~~

* 挂载2.10服务器的目录
~~~
mount -t nfs maple:/data/test  /net/maple
~~~

* 创建 `/data/test` 目录

* 建立目录的软链接
~~~
ln -s /net/maple/ /data/test/
~~~

* 卸载已挂在的NFS
~~~
umount /net/maple/
~~~

* 正确的删除方式（删除软链接，但不删除实际数据）
~~~
rm -rf  ./test_chk_ln
~~~

* 错误的删除方式
~~~
rm -rf ./test_chk_ln/ (这样就会把原来test_chk下的内容删除)
~~~

### 删除挖矿程序
