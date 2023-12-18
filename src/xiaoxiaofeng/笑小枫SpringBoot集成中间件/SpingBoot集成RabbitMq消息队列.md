## 1. 项目背景



## 2. Rabbit MQ安装

#### 2.1 docker拉取RabbitMQ镜像 

~~~
[root@k8s-n1 /]# docker pull rabbitmq:3.7.7-management
~~~

如下图：
![rabbitmq镜像下载成功图片](https://image.xiaoxiaofeng.site/blog/2023/12/15/xxf-20231215092401.jpg?xxfjava)


#### 2.2 创建挂载目录

~~~
[root@k8s-n1 /]# mkdir /mnt/rabbitMQ/data
~~~

#### 2.3 查看下载镜像的镜像id

~~~
[root@k8s-n1 /]# docker images
~~~

![查看镜像图片](https://image.xiaoxiaofeng.site/blog/2023/12/15/xxf-20231215092413.jpg?xxfjava)

#### 2.4 启动docker里的RabbitMQ镜像

~~~
[root@k8s-n1 /]# docker run -d --name rabbitmq3.7.7 -p 5672:5672 -p 15672:15672 -v /mnt/rabbitMQ/data:/var/lib/rabbitmq --hostname myRabbit -e RABBITMQ_DEFAULT_VHOST=my_vhost  -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin 2888deb59dfc
~~~

> 参数说明：
> -d 后台运行容器；
> --name 指定容器名；
> -p 指定服务运行的端口（5672：应用访问端口；15672：控制台Web端口号）；
> -v 映射目录或文件；
> --hostname  主机名（RabbitMQ的一个重要注意事项是它根据所谓的 “节点名称” 存储数据，默认为主机名）；
> -e 指定环境变量；（RABBITMQ_DEFAULT_VHOST：默认虚拟机名；RABBITMQ_DEFAULT_USER：默认的用户名；RABBITMQ_DEFAULT_PASS：默认用户名的密码）

#### 2.5 启动成功

~~~
[root@k8s-n1 /]# docker ps
~~~

* 查看docker容器：
  ![docker查看图片](https://image.xiaoxiaofeng.site/blog/2023/12/15/xxf-20231215092417.jpg?xxfjava)
* 浏览器访问
  用浏览器访问`http://192.168.2.21:15672` 访问成功，表示RabbitMQ安装成功。
* ![rabbitMQ-success](https://image.xiaoxiaofeng.site/blog/2023/12/15/xxf-20231215093357.jpeg?xxfjava)

## 3. SpringBoot使用Rabbit MQ



