## 1. 背景

因为之前docker直接使用centos的`yum -y install docker`安装的，安装源的版本比较老，最近项目计划使用docker的一些新特性，所以需要将docker版本做一下升级。本文详细记录了升级的过程，有需要的小伙伴可以参考一下。

## 2. 升级过程

### 2.1 查看当前版本 

首先使用`docker --version`查看现有的docker版本，可以看到，我们使用的是1.13.1，也是比较老的一个版本的。

输出如下：

~~~bash
Docker version 1.13.1, build 7d71120/1.13.1
~~~

### 2.2 卸载当前版本

首先需要停止我们当前运行的docker服务

~~~bash
sudo systemctl stop docker
~~~

![image-20250821102531489](https://image.xiaoxiaofeng.site/blog/image/image-20250821102531489.png?xiaoxiaofeng)

Docker 使用了 systemd 的 **socket 激活** 机制

~~~bash
# 同时停止 docker.service 和 docker.socket
sudo systemctl stop docker.socket docker.service

# 或者先停止socket再停止服务
sudo systemctl stop docker.socket
sudo systemctl stop docker.service
~~~

 然后卸载当前版本docker

~~~bash
sudo yum remove -y docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
~~~

如果是比较新的ce版本，卸载命令如下：

~~~bash
sudo yum remove -y docker-ce \
                  docker-ce-cli \
                  docker-ce-rootless-extras \
                  containerd.io \
                  docker-buildx-plugin \
                  docker-compose-plugin
~~~



删除旧的 Docker 相关数据（可选）：

⚠️ **注意：**

- `/var/lib/docker` 目录包含所有 Docker 容器和镜像，如果不想丢失数据，请 **备份后再执行删除**，或者不删除。
- 如果docker版本差异过大，会出现原来容器在新版本不可用的情况，如果存在数据存在容器内，请先备份数据。

~~~bash
sudo rm -rf /var/lib/docker
~~~

### 2.3 安装必要的依赖包

安装 `yum-utils` 工具，它们可以帮助你管理 YUM 仓库

~~~bash
sudo yum install -y yum-utils
~~~

### 2.4 设置 Docker 仓库

Docker 的官方仓库在国外，速度可能较慢。建议国内用户使用国内镜像源，例如阿里云：

~~~bash
sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
~~~

若你希望使用官方源，可以使用以下命令

~~~bash
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
~~~

### 2.5 安装最新版本的 Docker

接下来，安装 Docker CE 最新版本：

~~~bash
sudo yum install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
~~~

![image-20250821103147401](https://image.xiaoxiaofeng.site/blog/image/image-20250821103147401.png?xiaoxiaofeng)

若要安装 **特定版本**，可以先列出可用版本：

~~~bash
yum list docker-ce --showduplicates | sort -r
~~~

然后安装指定版本：

~~~bash
sudo yum install -y docker-ce-<VERSION> docker-ce-cli-<VERSION> containerd.io
~~~

### 2.6 启动 Docker 并设置开机自启

安装完成后，启动 Docker 服务并将其设置为开机自动启动：

~~~bash
sudo systemctl start docker
sudo systemctl enable docker
~~~

## 3. 安装验证

### 3.1 验证 Docker 版本

~~~bash
docker --version
~~~

输出如下：

![image-20250821103118175](https://image.xiaoxiaofeng.site/blog/image/image-20250821103118175.png?xiaoxiaofeng)

### 3.2 验证安装

通过运行 `hello-world` 镜像来验证 Docker 是否已正确安装并正常运行：

~~~bash
docker run hello-world
~~~

如果看到 "Hello from Docker!" 的提示信息，说明安装成功。

## 4. 镜像源配置

因为国内无法直接使用官方的镜像源，这里可以使用阿里云的制品中心做下替换

链接前往，需登录：[https://cr.console.aliyun.com/cn-hangzhou/instances/artifact](https://cr.console.aliyun.com/cn-hangzhou/instances/artifact)

![image-20250821103456767](https://image.xiaoxiaofeng.site/blog/image/image-20250821103456767.png?xiaoxiaofeng)

## 5. Docker Compose升级

### 5.1 检查版本兼容性（首要步骤）

首先检查你当前的 Docker 和 Docker Compose 版本：

```
# 检查 Docker 版本
docker --version

# 检查 Docker Compose 版本
docker-compose --version
```

Docker Compose 版本需要与 Docker 引擎版本匹配。常见的兼容性问题：

- **Docker Engine 20.10+** 需要 **Docker Compose v2+**
- 旧版本 Docker 可能无法与新版本 Compose 正常工作

### 2. 升级 Docker Compose

如果版本不匹配，建议升级到最新版本：

```
# 卸载旧版本
sudo rm /usr/local/bin/docker-compose

# 下载最新版本（请查看 GitHub 发布页获取最新版本号）
sudo curl -L "https://github.com/docker/compose/releases/download/v2.23.3/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# 添加执行权限
sudo chmod +x /usr/local/bin/docker-compose

# 验证安装
docker-compose --version
```

### 3. 使用 Docker Compose V2（推荐）

Docker 现在内置了 Compose V2，可以替代传统的 `docker-compose` 命令：

```
# 使用新的 compose 插件（注意没有横杠）
docker compose up -d

# 检查是否可用
docker compose version
```
