笑小枫💕

> 欢迎来到笑小枫的世界，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
>
> 微信公众号：笑小枫
>
> 笑小枫个人博客：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
>
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)



## 1. Windows 下安装Redis

### 1.1下载Redis

方式一：[https://github.com/tporadowski/redis/releases](https://github.com/tporadowski/redis/releases)

方式二：博主提供的百度网盘版本下载：[https://pan.baidu.com/s/14_RxyUw_3B4mMP5tZGe3Bg?pwd=44wq](https://pan.baidu.com/s/14_RxyUw_3B4mMP5tZGe3Bg?pwd=44wq)

Redis 支持 32 位和 64 位。这个需要根据你系统平台的实际情况选择，这里我们下载 **Redis-x64-xxx.zip**压缩包，将文件解压到指定的目录下，可以自定义，这里演示使用的`5.0.14.1`版本，演示路径为`D:\workpath\redis5.0`。

![image-20230828101359274](https://image.xiaoxiaofeng.site/blog/2023/08/28/xxf-20230828101359.png?xxfjava)

打开文件夹，内容如下：

![image-20230828101601451](https://image.xiaoxiaofeng.site/blog/2023/08/28/xxf-20230828101601.png?xxfjava)

### 1.2 启动Redis服务端

打开一个 **cmd** 窗口，切换目录到 **D:\workpath\redis5.0**

![image-20230828102324972](https://image.xiaoxiaofeng.site/blog/2023/08/28/xxf-20230828102325.png?xxfjava)

 启动redis的服务端，运行命令：

```
redis-server.exe redis.windows.conf
```

如果想方便的话，可以把 redis 的路径加到系统的环境变量里，这样就省得再输路径了，后面的那个 redis.windows.conf 可以省略，如果省略，会启用默认的。输入之后，会显示如下界面：

![image-20230828102457822](https://image.xiaoxiaofeng.site/blog/2023/08/28/xxf-20230828102457.png?xxfjava)

### 1.3 启动Redis客户端

这时候另启一个 cmd 窗口，原来的不要关闭，不然就无法访问服务端了。

切换到 redis 目录下运行，启动redis的客户端:

```
redis-cli.exe -h 127.0.0.1 -p 6379
```

### 1.4 测试Redis

设置键值对:

```
set mykey xiaoxiaofeng
```

取出键值对:

```
get myKey
```

![https://image.xiaoxiaofeng.site/blog/2023/08/28/xxf-20230828102755.png?xxfjava](https://image.xiaoxiaofeng.site/blog/2023/08/28/xxf-20230828102755.png?xxfjava)

------

这样redis在window下的安装已经完成了。

使用redis可以直接到目录下双击执行`redis-server.exe`即可启动服务端。

![image-20230828103302319](https://image.xiaoxiaofeng.site/blog/2023/08/28/xxf-20230828103302.png?xxfjava)

## 2. Linux 源码安装

### 2.1下载Redis

**下载地址：**http://redis.io/download，下载最新稳定版本。

### 2.2 安装Redis

本教程使用的最新文档版本为 2.8.17，下载并安装：

```
# wget http://download.redis.io/releases/redis-6.0.8.tar.gz
# tar xzf redis-6.0.8.tar.gz
# cd redis-6.0.8
# make
```

执行完 **make** 命令后，redis-6.0.8 的 **src** 目录下会出现编译后的 redis 服务程序 redis-server，还有用于测试的客户端程序 redis-cli：

### 2.3 启动 redis 服务

下面启动 redis 服务：

```
# cd src
# ./redis-server
```

注意这种方式启动 redis 使用的是默认配置。也可以通过启动参数告诉 redis 使用指定配置文件使用下面命令启动。

```
# cd src
# ./redis-server ../redis.conf
```

**redis.conf** 是一个默认的配置文件。我们可以根据需要使用自己的配置文件。

### 2.4 启动 redis 客户端

启动 redis 服务进程后，就可以使用测试客户端程序 redis-cli 和 redis 服务交互了。 比如：

```
# cd src
# ./redis-cli
redis> set foo bar
OK
redis> get foo
"bar"
```

------

## 3. Docker安装Redis

### 3.1 使用docker查看Redis版本信息

~~~
[root@k8s-n1 /]# docker search redis
~~~

### 3.2 下载redis镜像

~~~
[root@k8s-n1 /]# docker pull redis:4.0
~~~

如下图：<br>
![redis镜像下载成功图片](D:\zhangfuzeng\code\Java-Notes\src\images\dockerpullredis.jpg)

### 3.3 查看下载镜像的镜像id

~~~
[root@k8s-n1 /]# docker images
~~~

### 3.4 启动docker里的redis镜像

~~~
[root@k8s-n1 /]# docker run -itd --name redis_test -p 6379:6379 191c4017dcdd
~~~

参数说明：<br>
-p 6380:6379：映射容器服务的 6379 端口到宿主机的 6380 端口。外部可以直接通过宿主机ip:6380 访问到 Redis 的服务。<br>
8280a2c45ce5 镜像id（ IMAGE ID ）


如果启动redis后面需要加参数 使用以下命令：

~~~
[root@k8s-n1 /]# docker run -itd --name redis4.0 -p 6379:6379 191c4017dcdd redis-server --bind 0.0.0.0 --requirepass test001 --protected-mode no --daemonize no --appendonly yes
~~~

docker run -d --name trade_test -p 8997:8997 -it --network msnetwork --network-alias mstrade 
redis-server后面的代表使用以下参数配置，支持映射配置文件<br>
redis-server --bind 0.0.0.0 --protected-mode no --daemonize no --appendonly yes

使用映射的配置文件

~~~
[root@k8s-n1 /]# docker run -itd --name redis_test -p 6379:6379 -v /data/redis/redis.conf:/etc/redis/redis.conf 8280a2c45ce5 redis-server /etc/redis/redis.conf --appendonly yes
~~~

* -v 挂载外部文件/data/redis/redis.conf 到 /etc/redis/redis.conf
* redis-server /etc/redis/redis.conf 启动时使用能够该配置文件

### 3.5 安装成功

~~~
[root@k8s-n1 /]# docker ps
~~~

* 查看docker容器：<br>
  ![docker查看图片](D:\zhangfuzeng\code\Java-Notes\src\images\redis-dockerps-a.jpg)

* RedisDesktopManager连接：<br>
  ![docker查看图片](D:\zhangfuzeng\code\Java-Notes\src\images\redis-success.jpg)


### 3.6 测试Redis

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

* 容器内部测试：
  ![docker查看图片](https://image.xiaoxiaofeng.site/blog/2023/08/28/xxf-20230828103436.jpg?xxfjava)

---

## 4. Ubuntu apt 命令安装

在 Ubuntu 系统安装 Redis 可以使用以下命令:

```
# sudo apt update
# sudo apt install redis-server
```

### 4.1 启动 Redis

```
# redis-server
```

### 4.2 查看 redis 是否启动？

```
# redis-cli
```

以上命令将打开以下终端：

```
redis 127.0.0.1:6379>
```

127.0.0.1 是本机 IP ，6379 是 redis 服务端口。现在我们输入 PING 命令。

```
redis 127.0.0.1:6379> ping
PONG
```

以上说明我们已经成功安装了redis。