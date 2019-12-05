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
![mysql镜像下载成功图片事例](https://github.com/hack-feng/Java-Notes/blob/master/src/images/dockerpullmysql8.png)

#### 2、查看下载的mysql镜像
~~~
[root@k8s-n1 ~]# docker images
~~~
如下图：
![docker查看mysql镜像](https://github.com/hack-feng/Java-Notes/blob/master/src/images/dockerimage.png)

#### 3、创建目录
~~~
[root@k8s-n1 /]# mkdir -p /mnt/mysql/data /mnt/mysql/logs /mnt/mysql/conf
~~~

#### 4、启动docker里面的mysql镜像
~~~
[root@k8s-n1 /]# docker run -p 3308:3306 --name trade_mysql -v /mnt/mysql/conf:/etc/mysql/conf.d -v /mnt/mysql/logs:/logs -v /mnt/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql
~~~

此时启动已完成，在docker启动镜像时密码加密使用的是caching_sha2_password，
在服务器端启动默认使用mysql_native_password 加密的，
如需要使用外部工具连接，需要进入docker容器重置root密码。
详细操作如下：

#### 5、查看docker进入docker容器
~~~
[root@k8s-n1 mysql]# docker exec -it trade_mysql /bin/sh

# mysql -u root -p

mysql> ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
~~~

#### 6、安装成功
~~~
[root@k8s-n1 /]# docker ps -a
~~~
查看docker容器：
![docker查看](https://github.com/hack-feng/Java-Notes/blob/master/src/images/dockerimage.png)
Navicat连接：
![docker查看](https://github.com/hack-feng/Java-Notes/blob/master/src/images/success.png)
