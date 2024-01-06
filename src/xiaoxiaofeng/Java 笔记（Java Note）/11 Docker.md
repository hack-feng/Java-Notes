# Docker 简介 #

 *  Docker 官方文档：https://docs.docker.com/
 *  Docker 架构遵循 C/S 架构，分为客户端、Docker 主机、Docker 镜像仓库三部分
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414666850.jpeg) 
    图 1 Docker\_Architecture

 *  Docker 镜像地址配置
    
     *  网易镜像地址：http://hub-mirror.c.163.com

``````````
sudo mkdir -p /etc/docker 
  sudo tee /etc/docker/daemon.json <<-'EOF' 
  { 
    "registry-mirrors": ["https://wi2behga.mirror.aliyuncs.com"] 
  } 
  EOF 
  sudo systemctl daemon-reload 
  sudo systemctl restart docker
``````````

# Docker 常用命令 #

## 镜像相关 ##

 *  docker search <镜像>：搜索镜像
 *  docker pull <镜像>：获取镜像
 *  docker rmi <镜像>：删除本地镜像

## 容器相关 ##

 *  docker run \[OPTIONS\] IMAGE \[COMMAND\] \[ARG…\]，常用选项：-d：后台运行容器-e：设置环境变量—expose / -p 宿主端口:容器端口—name：指定容器名称—link：链接不同容器-v 宿主目录:容器目录：挂载磁盘卷

``````````
# 使用 Portainer 管理 Docker 
  docker pull portainer/portainer && docker run --name portainer --restart always -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock -v ~/docker-data/portainer/data:/data -v ~/docker-data/portainer/cn:/public -d portainer/portainer 
  
  # 通过 Docker 运行 MySQL 
  docker run --name mysql --restart always -p 3306:3306 -v ~/docker-data/mysql/lib:/var/lib/mysql -v ~/docker-data/mysql/etc:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=admin -d mysql:5.7 
  
  # 通过 Docker 运行 MongoDB 
  docker run --name mongo -p 27017:27017 -v ~/docker-data/mongo:/data/db -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=admin -d mongo 
  
  # 通过 Docker 运行 Redis 
  docker run --name redis -p 6379:6379 -d redis 
  
  docker run --name zookeeper -p 2181:2181 -d zookeeper:3.5 
  
  docker run --name consul -p 8500:8500 -p 8600:8600/udp -d consul 
  
  docker run --name nacos -p 8848:8848 -e MODE=standalone -d nacos/nacos-server 
  
  docker run --name rabbitmq -d -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=root -e ABBITMQ_DEFAULT_PASS=admin rabbitmq:management 
  
  docker run --name zipkin -d -p 9411:9411 openzipkin/zipkin 
  docker run --name rabbit-zipkin -d -p 9411:9411--link rabbitmq -e RABBIT_ADDRESSES=rabbitmq:5672 -e RABBIT_USER=root -e RABBIT_PASSWORD=admin openzipkin/zipkin
``````````

 *  docker start/stop/restart <容器名>
 *  docker rm <容器名>：-v 删除与容器关联的卷，-f 通过 SIGKILL 信号强制删除一个运行中的容器
 *  docker ps <容器名>
 *  docker exec \[OPTIONS\] CONTAINER COMMAND \[ARG…\]：在运行的容器中执行命令，如 `docker exec -it mongo bash`，退出容器 `exit`
 *  docker logs <容器名>

# Docker 镜像 #

 *  镜像是静态的只读模板
 *  镜像中包含构建 Docker 容器的指令
 *  镜像是分层的（联合文件系统）
 *  通过 Dockerfile 来创建镜像

## Dockerfile ##

 *  Dockerfile 是一个文本文件，里面包含了构建 Docker 镜像所需要用到的命令（Instruction）

### 常用指令 ###

| 指令         | 作用        | 格式举例                                            |
| ---------- | --------- | ----------------------------------------------- |
| FROM       | 基于哪个镜像    | `FROM <image>[:<tag>] [AS <name>]`              |
| LABEL      | 设置标签      | `LABEL maintainer="Sdky"`                       |
| RUN        | 运行安装命令    | `RUN ["executable", "param1", "param2"]`        |
| CMD        | 容器启动时的命令  | `CMD ["executable","param1","param2"]`          |
| ENTRYPOINT | 容器启动后的命令  | `ENTRYPOINT ["executable", "param1", "param2"]` |
| VOLUME     | 挂载目录      | `VOLUME ["/data"]`                              |
| EXPOSE     | 容器要监听的端口  | `EXPOSE <port> [<port>/<protocol>…]`            |
| ENV        | 设置环境变量    | `ENV <key> <value>`                             |
| ADD        | 添加文件      | `ADD [—chown=<user>:<group>] <src>… <dest>`     |
| WORKDIR    | 设置运行的工作目录 | `WORKDIR /path/to/workdir`                      |
| USER       | 设置运行的用户   | `USER <user>[:<group>]`                         |

### 构建 Docker 镜像 ###

 *  手动构建：进入 Dockerfile 所在目录，`docker build -t spring-boot-demo-docker .`
 *  通过 Maven 插件构建：[dockerfile-maven-plugin][]

# Docker-Compose #

 *  Docker-Compose 项目是 Docker 官方的开源项目，负责实现对 Docker 容器集群的快速编排

## Docker-Compose 模板文件 ##

 *  Compose 模板文件是一个定义服务、网络和卷的 YAML 文件
 *  Compose 通过一个 docker-compose.yml 模板文件来定义一组相关联的应用容器为一个项目（project）

## 常用命令 ##

 *  docker-compose -f docker-compose.yml up -d \[SERVICE…\]：使用的 Compose 模板文件（默认为 docker-compose.yml），在后台启动服务
 *  docker-compose ps：列出项目中目前的所有容器
 *  docker-compose logs \[SERVICE…\]：查看服务容器的输出，-f 实时输出
 *  docker-compose start \[SERVICE…\]：启动已经存在的服务容器
 *  docker-compose stop \[SERVICE…\]：停止正在运行的容器
 *  docker-compose rm \[SERVICE…\]：删除（停止状态的）服务容器
 *  docker-compose down：停止和删除容器、网络、卷、镜像
 *  docker-compose exec \[options\] SERVICE COMMAND
 *  docker-compose pause/unpause \[SERVICE…\]：暂停/恢复


[Docker_Architecture]: https://static.sitestack.cn/projects/sdky-java-note/8bb9efb0272b081060708e0bea045c65.jpeg
[dockerfile-maven-plugin]: https://github.com/spotify/dockerfile-maven