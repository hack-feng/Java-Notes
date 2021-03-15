
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

### 查看端口号
netstat -anp |grep 端口号
lsof -i:端口号

~~~
lsof -i:8080：查看8080端口占用
lsof abc.txt：显示开启文件abc.txt的进程
lsof -c abc：显示abc进程现在打开的文件
lsof -c -p 1234：列出进程号为1234的进程所打开的文件
lsof -g gid：显示归属gid的进程情况
lsof +d /usr/local/：显示目录下被进程开启的文件
lsof +D /usr/local/：同上，但是会搜索目录下的目录，时间较长
lsof -d 4：显示使用fd为4的进程
lsof -i -U：显示所有打开的端口和UNIX domain文件
~~~

### 查看所有端口号
netstat -ntlp

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

### Linux文件传输
~~~
scp local_file user@ip:remote_folder
~~~

### 添加、修改、删除用户

### Linux安装JDK1.8
1、首先在/usr/local目录下新建一个文件夹jdk，然后将下载好的jdk压缩文件放入这个文件夹
~~~
mkdir /usr/local/jdk

cd /usr/local/jdk

tar -zxvf jdk-8u181-linux-x64.tar.gz
~~~

2、环境变量配置

修改/etc目录下的profile文件,在profile文件末尾添加如下内容：
~~~
export JAVA_HOME=/usr/local/jdk/jdk1.8.0_181
export CLASSPATH=$:CLASSPATH:$JAVA_HOME/lib/ 
export PATH=$PATH:$JAVA_HOME/bin
~~~

其中 JAVA_HOME需要根据实际安装路径和JDK版本进行修改

修改完成后，执行如下命令使修改生效
~~~
source /etc/profile
~~~

3、验证安装
~~~
java -version
~~~

![CSDN-笑小枫](images/linux/jdk-version.jpg)



### 查看命令
uname -a   # 查看linux版本的命令

java -version  # 查看java版本的命令

mysql -V    # 查看mysql版本的命令

redis-server -v  # 查看redis版本的命令


### linux 创建用户
~~~
groupadd test
useradd -g test test1
passwd test1
~~~

~~~
linux 中更改用户权限和用户组的命令实例;
增加权限给当前用户 chmod +wx filename
chmod -R 777 /upload

用户组 chgrp -R foldname zdz
chown -R  所有者用户名.组名 文件夹名称
例如：chown -R  zdz.nginx KooBox

查看文件权限的语句：
　　在终端输入:
ls -l xxx.xxx （xxx.xxx是文件名）
　　那么就会出现相类似的信息，主要都是这些：
-rw-rw-r--
　　一共有10位数
　　其中： 最前面那个 - 代表的是类型
　　中间那三个 rw- 代表的是所有者（user）
　　然后那三个 rw- 代表的是组群（group）
　　最后那三个 r-- 代表的是其他人（other）
　　然后我再解释一下后面那9位数：
　　r 表示文件可以被读（read）
　　w 表示文件可以被写（write）
　　x 表示文件可以被执行（如果它是程序的话）
　　- 表示相应的权限还没有被授予
　　现在该说说修改文件权限了
　　在终端输入：
　　chmod o w xxx.xxx
　　表示给其他人授予写xxx.xxx这个文件的权限
　　chmod go-rw xxx.xxx
　　表示删除xxx.xxx中组群和其他人的读和写的权限
　　其中：
　　u 代表所有者（user）
　　g 代表所有者所在的组群（group）
　　o 代表其他人，但不是u和g （other）
　　a 代表全部的人，也就是包括u，g和o
　　r 表示文件可以被读（read）
　　w 表示文件可以被写（write）
　　x 表示文件可以被执行（如果它是程序的话）
　　其中：rwx也可以用数字来代替
　　r ------------4
　　w -----------2
　　x ------------1
　　- ------------0
　　行动：
　　 表示添加权限
　　- 表示删除权限
　　= 表示使之成为唯一的权限
　　当大家都明白了上面的东西之后，那么我们常见的以下的一些权限就很容易都明白了：
　　-rw------- (600) 只有所有者才有读和写的权限
　　-rw-r--r-- (644) 只有所有者才有读和写的权限，组群和其他人只有读的权限
　　-rwx------ (700) 只有所有者才有读，写，执行的权限
　　-rwxr-xr-x (755) 只有所有者才有读，写，执行的权限，组群和其他人只有读和执行的权限
　　-rwx--x--x (711) 只有所有者才有读，写，执行的权限，组群和其他人只有执行的权限
　　-rw-rw-rw- (666) 每个人都有读写的权限
　　-rwxrwxrwx (777) 每个人都有读写和执行的权限
~~~

