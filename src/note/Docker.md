### Docker安装

#### 1、Docker 要求 
CentOS 系统的内核版本高于 3.10 ，先验证你的CentOS 版本是否支持 Docker 。
~~~
[root@k8s-n1 ~]# uname -r
3.10.0-693.el7.x86_64
~~~

#### 2、安装docker
~~~
yum update -y
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
如下图：
![mysql镜像下载成功图片](https://github.com/hack-feng/Java-Notes/blob/master/src/images/dockerpullmysql8.png)

#### 2、查看下载的mysql镜像
~~~
[root@k8s-n1 ~]# docker images
~~~
如下图：
![docker查看mysql镜像图片](https://github.com/hack-feng/Java-Notes/blob/master/src/images/msyql-dockerimage.png)

#### 3、创建目录
~~~
[root@k8s-n1 /]# mkdir -p /mnt/mysql/data /mnt/mysql/logs /mnt/mysql/conf
~~~

#### 4、启动docker里面的mysql镜像
~~~
[root@k8s-n1 /]# docker run -p 3308:3306 --name trade_mysql -v /mnt/mysql/conf:/etc/mysql/conf.d -v /mnt/mysql/logs:/logs -v /mnt/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d 7bb2586065cd
~~~

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
查看docker容器：
![docker查看图片](https://github.com/hack-feng/Java-Notes/blob/master/src/images/mysql-dockerps-a.jpg)
Navicat连接：
![Navicat连接图片](https://github.com/hack-feng/Java-Notes/blob/master/src/images/mysql-success.jpg)


### docker安装Redis

#### 1、使用docker查看Redis版本信息
~~~
[root@k8s-n1 /]# docker search redis
~~~

#### 2、下载redis镜像
~~~
[root@k8s-n1 /]# docker pull redis:4.0
~~~
如下图：
![redis镜像下载成功图片](https://github.com/hack-feng/Java-Notes/blob/master/src/images/dockerpullredis.jpg)

#### 3、查看下载镜像的镜像id
~~~
[root@k8s-n1 /]# docker images
~~~

#### 4、启动docker里的redis镜像
~~~
[root@k8s-n1 /]# docker run -itd --name trade_redis -p 6380:6379 8280a2c45ce5
~~~

#### 5、安装成功
~~~
[root@k8s-n1 /]# docker ps
~~~
* 查看docker容器：
![docker查看图片](https://github.com/hack-feng/Java-Notes/blob/master/src/images/redis-dockerps-a.jpg)

* RedisDesktopManager连接：
![docker查看图片](https://github.com/hack-feng/Java-Notes/blob/master/src/images/redis-success.jpg)


#### 6、测试Redis
* 进入docker容器测试
~~~
$ docker exec -it redis-test /bin/bash

root@1d71ab146d19:/data# redis-cli

127.0.0.1:6379> set name test
OK
127.0.0.1:6379> get name
"test"
127.0.0.1:6379> 
~~~
* 容器内部测试：
![docker查看图片](https://github.com/hack-feng/Java-Notes/blob/master/src/images/redis-test.jpg)


### docker下安装RabbitMQ

#### 1、docker拉取RabbitMQ镜像 
~~~
[root@k8s-n1 /]# docker pull rabbitmq:3.7.7-management
~~~
如下图：
![rabbitmq镜像下载成功图片](https://github.com/hack-feng/Java-Notes/blob/master/src/images/rabbitMQ-pull.jpg)


#### 2、创建挂载目录
~~~
[root@k8s-n1 /]# mkdir /mnt/rabbitMQ/data
~~~

#### 3、查看下载镜像的镜像id
~~~
[root@k8s-n1 /]# docker images
~~~
![查看镜像图片](https://github.com/hack-feng/Java-Notes/blob/master/src/images/rabbitMQ-dockerimages.jpg)

#### 4、启动docker里的RabbitMQ镜像
~~~
[root@k8s-n1 /]# docker run -d --name rabbitmq3.7.7 -p 5672:5672 -p 15672:15672 -v /mnt/rabbitMQ/data:/var/lib/rabbitmq --hostname myRabbit -e RABBITMQ_DEFAULT_VHOST=my_vhost  -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin 2888deb59dfc
~~~

#### 5、启动成功
~~~
[root@k8s-n1 /]# docker ps
~~~
* 查看docker容器：
![docker查看图片](https://github.com/hack-feng/Java-Notes/blob/master/src/images/rabbitMQdockerps-a.jpg)
* 浏览器访问
用浏览器访问``http://192.168.2.21:15672`` 访问成功，表示RabbitMQ安装成功。
![浏览器访问图片](https://github.com/hack-feng/Java-Notes/blob/master/src/images/rabbitMQ-success.jpg)
