### 安装
需要先安装erlang，然后在安装rabbitMq

### 安装erlang
~~~
cd /opt
wget https://packages.erlang-solutions.com/erlang-solutions-1.0-1.noarch.rpm
rpm -Uvh erlang-solutions-1.0-1.noarch.rpm
yum install epel-release
yum install erlang
~~~


### 安装完erlang

### 启动
前端页面：http://localhost:15672
程序连接：
~~~yml
spring:
  rabbitmq:
      host: localhost
      port: 5672
      username: guest
      password: guest
~~~