## 1. RabbitMq简介

### 1.1 消息队列中间件简介
消息队列中间件是分布式系统中重要的组件，主要解决应用耦合，异步消息，流量削锋等问题实现高性能，高可用，可伸缩和最终一致性[架构] 使用较多的消息队列有 ActiveMQ（安全），RabbitMQ，ZeroMQ，Kafka（大数据），MetaMQ，RocketMQ
以下介绍消息队列在实际应用中常用的使用场景：异步处理，应用解耦，流量削锋和消息通讯四个场景

### 1.2 什么是RabbitMQ
`RabbitMQ` 是一个由 Erlang 语言开发的 AMQP 的开源实现。
`AMQP`：Advanced Message Queue，高级消息队列协议。它是应用层协议的一个开放标准，为面向消息的中间件设计，基于此协议的客户端与消息中间件可传递消息，并不受产品、开发语言等条件的限制。
`RabbitMQ` 最初起源于金融系统，用于在分布式系统中存储转发消息，在易用性、扩展性、高可用性等方面表现不俗。具体特点包括：

1. 可靠性（Reliability）
   RabbitMQ 使用一些机制来保证可靠性，如持久化、传输确认、发布确认。2.灵活的路由（Flexible Routing）
   在消息进入队列之前，通过 Exchange 来路由消息的。对于典型的路由功能，RabbitMQ已经提供了一些内置的 Exchange 来实现。针对更复杂的路由功能，可以将多个Exchange 绑定在一起，也通过插件机制实现自己的 Exchange 。

2. 消息集群（Clustering）
   多个 RabbitMQ 服务器可以组成一个集群，形成一个逻辑 Broker

3. 高可用（Highly Available Queues）
   队列可以在集群中的机器上进行镜像，使得在部分节点出问题的情况下队列仍然可用。

4. 多种协议（Multi-protocol）
   RabbitMQ 支持多种消息队列协议，比如 STOMP、MQTT 等等。

5. 多语言客户端（Many Clients）
    RabbitMQ 几乎支持所有常用语言，比如 Java、.NET、Ruby 等等。

6. 管理界面（Management UI）
    RabbitMQ 提供了一个易用的用户界面，使得用户可以监控和管理消息 Broker 的许多方面。

7. 跟踪机制（Tracing）
    如果消息异常，RabbitMQ 提供了消息跟踪机制，使用者可以找出发生了什么。

8. 插件机制（Plugin System）
    RabbitMQ 提供了许多插件，来从多方面进行扩展，也可以编写自己的插件。

## 2. 安装准备工具

因为`RabbitMQ`服务器是用`Erlang`语言编写的， 所以，你需要去查看`rabbitMq`适应`Erlang`的版本，因为不同的`rabbitMq`版本对应不同的`Erlang`版本，可以点击如下该链接查看版本匹配度：

https://www.rabbitmq.com/which-erlang.html#compatibility-matrix

![image-20231215100206096](https://image.xiaoxiaofeng.site/blog/2023/12/15/xxf-20231215100206.png?xxfjava)

### 2.1 笑小枫下载 

如果你的网络访问`GitHub`或者`erlang官网`比较卡，可以直接通过博主分享的站点进行下载：

`erlang`版本：`otp_win64_25.2.1.exe`

`RabbitMQ`版本：`rabbitmq-server-windows-3.11.0.zip`

笑小枫下载地址：[https://www.xiaoxiaofeng.com/resource/11](https://www.xiaoxiaofeng.com/resource/11)

如果其它版本，继续参考下文：

### 2.2 官网下载erlang

`erlang`官方下载地址：[https://erlang.org/download/otp_versions_tree.html](https://erlang.org/download/otp_versions_tree.html)

可以根据`RabbitMQ`的版本下载对应的`erlang`版本。

我这里下载的是`25.2.1`，`win64`的版本哟

![image-20231218131036165](https://image.xiaoxiaofeng.site/blog/2023/12/18/xxf-20231218131043.png?xxfjava)

### 2.3 GitHub下载RabbitMQ

`RabbitMQ`下载地址：[https://github.com/rabbitmq/rabbitmq-server/releases/tag/v3.11.0](https://github.com/rabbitmq/rabbitmq-server/releases/tag/v3.11.0)

可以直接从`GitHub`上面下载，如果是其他版本，可以直接替换上面链接的版本号即可。

我们这里以`3.11.0`版本为例，讲解具体的安装步骤。

> 注意：不要使用exe执行文件进行安装，使用时会出现错误（例如：Error: {:unable_to_load_rabbit, {‘no such file or directory’, ‘rabbit.app’}}），暂未找到原因。
> 所以，我们下载压缩包进行解压安装（最新版本一般没有压缩包，上一个版本会有）

![image-20231218100657291](https://image.xiaoxiaofeng.site/blog/2023/12/18/xxf-20231218100657.png?xxfjava)

## 3. 安装步骤

### 3.1  erlang安装

#### 3.1.1 安装步骤图文讲解

* 双击执行`otp_win64_25.2.1.exe`

![image-20231215104456077](https://image.xiaoxiaofeng.site/blog/2023/12/15/xxf-20231215104456.png?xxfjava)

* 选择自己需要安装目录，然后继续下一步

![image-20231215104634166](https://image.xiaoxiaofeng.site/blog/2023/12/15/xxf-20231215104634.png?xxfjava)

* 下一步，安装即可，静静等待安装完成即可。

![image-20231215104731837](https://image.xiaoxiaofeng.site/blog/2023/12/15/xxf-20231215104731.png?xxfjava)

#### 3.1.2 环境变量配置图文讲解

别问我在哪里配置，问我就让你百度😂😂😂

> 右击我的电脑 -> 属性 -> 高级系统设置 -> 环境变量

新建系统变量名为：`ERLANG_HOME` 变量值为`erlang`安装地址

![image-20231215105420333](https://image.xiaoxiaofeng.site/blog/2023/12/15/xxf-20231215105420.png?xxfjava)

双击系统变量`path`，点击“新建”，将`%ERLANG_HOME%\bin`加入到`path`中。

![image-20231215105605596](https://image.xiaoxiaofeng.site/blog/2023/12/15/xxf-20231215105605.png?xxfjava)

验证`erlang`是否安装成功

配置完环境变量后，`cmd`一定要重启

`win+R`键，输入`cmd`，再输入`erl`，看到`erlang`版本号就说明`erlang`安装成功了。

![image-20231218140358957](https://image.xiaoxiaofeng.site/blog/2023/12/18/xxf-20231218140406.png?xxfjava)

### 3.2 RabbitMq安装

#### 3.2.1 解压zip文件到执行目录

这里解压到`D:\workpath\rabbit\`目录下

![image-20231218101800110](https://image.xiaoxiaofeng.site/blog/2023/12/18/xxf-20231218101800.png?xxfjava)

#### 3.2.2  启动RabbitMQ

打开`cmd`命令窗口，切换到解压目录下，我这里是`D:\workpath\rabbit\rabbitmq_server-3.11.0\sbin`，运行下面命令，回车运行。 

~~~cmd
# 切换到D盘
d:

# 切换到指定目录下
cd D:\workpath\rabbit\rabbitmq_server-3.11.0\sbin

# 下载可视化管理界面插件
rabbitmq-plugins enable rabbitmq_management
~~~

安装成功的图片

![image-20231218101056051](https://image.xiaoxiaofeng.site/blog/2023/12/18/xxf-20231218101056.png?xxfjava)

启动rabbitMQ，进入到`\rabbitmq_server-3.11.0\sbin`目录下，双击执行`rabbitmq-server.bat`文件。

![image-20231218142628402](https://image.xiaoxiaofeng.site/blog/2023/12/18/xxf-20231218142628.png?xxfjava)

启动成功界面如下：

![image-20231218141137530](https://image.xiaoxiaofeng.site/blog/2023/12/18/xxf-20231218141137.png?xxfjava)

在浏览器输入`http://127.0.0.1:15672/`，默认账号密码`guest`/`guest`

![image-20231218142923184](https://image.xiaoxiaofeng.site/blog/2023/12/18/xxf-20231218142923.png?xxfjava)

登录成功后，可以看到rabbitMQ的管理页面

![image-20231218142951053](https://image.xiaoxiaofeng.site/blog/2023/12/18/xxf-20231218142951.png?xxfjava)

## 4. 稀奇古怪的问题

### 4.1 `rabbitmq-service.bat start exited with code 1`

解决办法：

打开注册表，搜索计算机`\HKEY_LOCAL_MACHINE\SOFTWARE\Ericsson\Erlang`

删除该选项后，以管理员方式打开`cmd`，在`rabbitmq`安装目录`sbin`下运行`rabbitmq-service.bat install`

### 4.2 运行rabbitMQ后，出现` error:{case_clause,version_not_available}`或者`start_error, failed_to_start_child`

解决办法：

rabbitMQ和erlang的版本不兼容，参考对应的匹配关系表

### 4.3 出现 Authentication failed (rejected by the remote node), please check the Erlang cookie 

解决办法：

比较下面两个文件夹下的`.erlang.cookie`文件内容，后来修改其中一个文件的内容，使两个文件内容一样。再次执行命令`rabbitmqctl status`，成功

~~~
C:\Windows\System32\config\systemprofile\.erlang.cookie
C:\User\{{电脑用户名}}\.erlang.cookie
~~~

### 4.4 Status of node rabbit@ … ** (ArgumentError) argument error (stdlib) 

解决办法：

检查rabbitmq 服务的日志db、log 等文件夹的路径是否含有中文，检查本机的用户名是否为中文（存在中文会出现下面的报错）

### 4.5 乱七八糟，网上也找不到的问题

卸载`rabbitMQ`，卸载`erlang`，找准匹配关系，重新安装。

一定要删除干净，参考下文，不然又来一堆乱七八糟的问题

## 5. 完全卸载rabbitMQ

1. 打开控制面板，找到`RabbitMQ server`,右键单击`RabbitMQ Server` 卸载。
2. 在控制面板的当前安装程序列表中找到`Erlang OTP` 右键单击 卸载。
3. 在任务管理器中，找到`epmd.exe`，看进程是否还在运行，右键结束进程。
4. 删除所有`RabbitMQ`和`Erlang`的文件夹与安装目录。
5. 删除`C:\Windows\System32\config\systemprofile\.erlang.cookie`文件。
6. 删除`C:\User\{{电脑用户名}}\.erlang.cookie`文件。
7. 删除`C:\Users\{{电脑用户名}}\AppData\Roaming\RabbitMQ`文件。
8. 打开注册表，找到`HKEY_LOCAL_MACHINE`下的`Ericsson`下的`Erlang`文件夹进行删除。
9. 打开注册表，找到`\HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services`下的`RabbitMQ`文件夹进行删除。
10. 删除配置的环境变量

**如果有的就删除，没有的就跳过就行了，重新安装时，一定要删除干净。**

## 6. 关于笑小枫

本文到此就结束了，如果帮助到你了，帮忙点个赞👍在安装的过程中有什么问题可以留言或者私信我哟

>  🐾我是笑小枫，全网皆可搜的【笑小枫】
