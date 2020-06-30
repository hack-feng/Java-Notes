### Docker 卸载
~~~
sudo apt-get remove docker docker-engine docker.io containerd runc
~~~

### Docker安装

#### 1、Docker 要求 
CentOS 系统的内核版本高于 3.10 ，先验证你的CentOS 版本是否支持 Docker 。
~~~
[root@k8s-n1 ~]# uname -r
3.10.0-693.el7.x86_64
~~~

#### 2、安装docker
~~~
yum -y install docker
~~~

#### 3、启动docker
~~~
systemctl start docker.service
~~~

#### 4、设置docker开机启动
~~~
systemctl enable docker.service
~~~

### Docker安装Mysql

#### 1、下载mysql镜像
~~~
下载最新版本：docker pull mysql
下载指定版本：docker pull mysql:8.0.15
~~~

~~~
[root@k8s-n1 ~]# docker pull mysql:8.0.15
~~~
如下图：<br>
![mysql镜像下载成功图片](../images/dockerpullmysql8.png)

#### 2、查看下载的mysql镜像
~~~
[root@k8s-n1 ~]# docker images
~~~
如下图：<br>
![docker查看mysql镜像图片](../images/msyql-dockerimage.png)

#### 3、创建目录
~~~
[root@k8s-n1 /]# mkdir -p /mnt/mysql/data /mnt/mysql/logs /mnt/mysql/conf
~~~

#### 4、启动docker里面的mysql镜像
~~~
[root@k8s-n1 /]# docker run -p 3308:3306 --name trade_mysql -v /mnt/mysql/conf:/etc/mysql/conf.d -v /mnt/mysql/logs:/logs -v /mnt/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d 7bb2586065cd
~~~

参数说明：<br>
-v 挂载宿主机目录和 docker容器中的目录<br>
-d 后台运行<br>
-p 映射容器端口号和宿主机端口号<br>
-e 环境参数<br>
7bb2586065cd 镜像id（ IMAGE ID ）

查看容器启动情况
~~~
docker ps
~~~

如果mysql启动没有成功，查看mysql启动日志
~~~
[root@k8s-n1 mysql/]#  docker logs -f trade_mysql
ERROR: mysqld failed while attempting to check config
command was: "mysqld --verbose --help"
mysqld: Can't read dir of '/etc/mysql/conf.d/' (OS errno 13 - Permission denied)
mysqld: [ERROR] Fatal error in defaults handling. Program aborted!
~~~
如果出现上图错误：   
~~~
[root@k8s-n1 /]# docker run --privileged=true -p 3308:3306 --name trade_mysql -v /mnt/mysql/conf:/etc/mysql/conf.d -v /mnt/mysql/logs:/logs -v /mnt/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d 7bb2586065cd
~~~
添加 **--privileged=true** 增加权限命令，启动成功。


此时启动已完成，在docker启动镜像时密码加密使用的是caching_sha2_password，
在服务器端启动默认使用mysql_native_password 加密的，
如需要使用外部工具连接，需要进入docker容器重置root密码。
详细操作如下：

#### 5、进入docker容器修改Mysql
~~~
[root@k8s-n1 mysql]# docker exec -it trade_mysql /bin/sh

# mysql -u root -p

mysql> ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
~~~

#### 6、安装成功
~~~
[root@k8s-n1 /]# docker ps
~~~
查看docker容器：<br>
![docker查看图片](../images/mysql-dockerps-a.jpg)
Navicat连接：<br>
![Navicat连接图片](../images/mysql-success.jpg)


### docker安装Redis

#### 1、使用docker查看Redis版本信息
~~~
[root@k8s-n1 /]# docker search redis
~~~

#### 2、下载redis镜像
~~~
[root@k8s-n1 /]# docker pull redis:4.0
~~~
如下图：<br>
![redis镜像下载成功图片](../images/dockerpullredis.jpg)

#### 3、查看下载镜像的镜像id
~~~
[root@k8s-n1 /]# docker images
~~~

#### 4、启动docker里的redis镜像
~~~
[root@k8s-n1 /]# docker run -itd --name redis_test -p 6379:6379 8280a2c45ce5
~~~

参数说明：<br>
-p 6380:6379：映射容器服务的 6379 端口到宿主机的 6380 端口。外部可以直接通过宿主机ip:6380 访问到 Redis 的服务。<br>
8280a2c45ce5 镜像id（ IMAGE ID ）

#### 5、安装成功
~~~
[root@k8s-n1 /]# docker ps
~~~
* 查看docker容器：<br>
![docker查看图片](../images/redis-dockerps-a.jpg)

* RedisDesktopManager连接：<br>
![docker查看图片](../images/redis-success.jpg)


#### 6、测试Redis
* 进入docker容器测试
~~~
$ docker exec -it redis_test /bin/bash

root@1d71ab146d19:/data# redis-cli

127.0.0.1:6379> set name test
OK
127.0.0.1:6379> get name
"test"
127.0.0.1:6379> 
~~~
* 容器内部测试：<br>
![docker查看图片](../images/redis-test.jpg)


### docker下安装RabbitMQ

#### 1、docker拉取RabbitMQ镜像 
~~~
[root@k8s-n1 /]# docker pull rabbitmq:3.7.7-management
~~~
如下图：<br>
![rabbitmq镜像下载成功图片](../images/rabbitMQ-pull.jpg)


#### 2、创建挂载目录
~~~
[root@k8s-n1 /]# mkdir /mnt/rabbitMQ/data
~~~

#### 3、查看下载镜像的镜像id
~~~
[root@k8s-n1 /]# docker images
~~~

![查看镜像图片](../images/rabbitMQ-dockerimages.jpg)

#### 4、启动docker里的RabbitMQ镜像
~~~
[root@k8s-n1 /]# docker run -d --name rabbitmq3.7.7 -p 5672:5672 -p 15672:15672 -v /mnt/rabbitMQ/data:/var/lib/rabbitmq --hostname myRabbit -e RABBITMQ_DEFAULT_VHOST=my_vhost  -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin 2888deb59dfc
~~~

参数说明：<br>
-d 后台运行容器；<br>
--name 指定容器名；<br>
-p 指定服务运行的端口（5672：应用访问端口；15672：控制台Web端口号）；<br>
-v 映射目录或文件；<br>
--hostname  主机名（RabbitMQ的一个重要注意事项是它根据所谓的 “节点名称” 存储数据，默认为主机名）；<br>
-e 指定环境变量；（RABBITMQ_DEFAULT_VHOST：默认虚拟机名；RABBITMQ_DEFAULT_USER：默认的用户名；RABBITMQ_DEFAULT_PASS：默认用户名的密码）

#### 5、启动成功
~~~
[root@k8s-n1 /]# docker ps
~~~
* 查看docker容器：<br>
![docker查看图片](../images/rabbitMQdockerps-a.jpg)
* 浏览器访问<br>
用浏览器访问``http://192.168.2.21:15672`` 访问成功，表示RabbitMQ安装成功。
![浏览器访问图片](../images/rabbitMQ-success.jpg)


### Docker容器内部无法访问宿主机网络(No Route to host)

* 关闭防火墙(局域网内推荐)
~~~
# centos 7
systemctl stop firewalld
~~~

* 在防火墙上开放指定端口
~~~
# 添加端口
firewall-cmd --zone=public --add-port=7001/tcp --permanent
firewall-cmd --reload
~~~

### Docker启动报错 : iptables failed

#### 错误信息
Error response from daemon: driver failed programming external connectivity on endpoint gloomy_kirch : iptables failed: iptables --wait -t nat -A DOCKER -p tcp -d 0/0 --dport 32810 -j DNAT --to-destination 172.17.0.2:80 ! -i docker0: iptables: No chain/target/match by that name.

#### 原因分析

在Docker Daemon服务启动之后, 修改了防火墙配置(修改/关闭等), 此时启动Docker容器会造成防火墙网络配置有问题。

#### 解决方案
~~~
# 先重启防火墙
systemctl restart firewalld
# 再重启Docker 服务
systemctl restart docker
~~~

### Docker挂载的目录, 在容器内无读写权限(Permission denied)

#### 环境&现象
CentOS 7.4 环境, 启动Docker时volume容器和宿主机的挂载目录, 但是在容器内部无权限对此目录进行操作

~~~
# 错误现象
ls: cannot open directory '.': Permission denied
~~~

#### 解决办法

* 关闭CentOS7中安全模块selinux
~~~
# 临时关闭selinux
setenforce 0

# 永久关闭selinux : SELINUX=disabled , 重启机器生效
vi /etc/selinux/config
~~~

* 运行容器时, 给容器增加特权
~~~
docker run -i -t -v /soft:/soft --privileged=true 637fe9ea94f0 /bin/bash
~~~

### SpringBoot项目打包成docker镜像
#### 创建Dockerfile文件，内容如下：
~~~
FROM openjdk:8-jdk-alpine
VOLUME /temp
EXPOSE 9001
ADD logistics-quote-1.0.0.jar logistics-quote.jar
ENTRYPOINT ["java","-jar","/logistics-quote.jar"]
~~~

#### 打成docker镜像
将jar包和Dockerfile放在服务器的统一目录下，执行命令：
~~~
docker build -t quote:1.0.0 .
~~~

执行docker images 可以查看到生成的镜像

#### docker 启动镜像
~~~
docker run -d --name trade_test -p 9002:9001 imageId
~~~


### docker删除
~~~
# 停止容器运行
docker stop containsId

# 删除容器
docker rm containsId

# 删除镜像
docker rmi imagesId

~~~