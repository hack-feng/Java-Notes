# Mysql数据误删除至数据恢复的生产事故完整复盘
## 生产事故发生场景
场景：随着同事一句“卧槽，我把生产库给清了”，瞬间整个人懵逼了。项目上线第三天，成功完成清库操作。事情是这样子的，同事本地测试打算截断部分业务表重新测试数据，结果一不留神就把生产库给干掉了。

本文章做为本次生产事故的复盘，主要从怎么避免问题的发生和问题发生后怎么处理两个角度展开。

## 生产事故发生主要问题
本次生产事故发生的主要原因：
1. 开发测试阶段一直使用Navicat Premium进行截断表清库的方式
2. 开发环境连接和本地环境连接混在一起，没有进行合理管理
3. 操作时没有仔细检查
4. 没有专业的数据库管理员，生产数据库没有进行专业的处理和用户权限划分

## 数据恢复全过程

* 事故发生时间：2021-03-03 11:42分左右
* 数据库环境：docker下安装的Mysql8.0
* 恢复基础：数据库开启了binlog，每天晚上22点自动备份

介于事故解决过程中没有留下有效截图，这里将进行测试数据库模拟，还原事故发生至解决的整个过程

### docker安装mysql数据库
docker详细安装过程参考文章[【Docker安装软件，这一篇就够了】](https://zhangfz.blog.csdn.net/article/details/103405009)

docker下安装mysql 8.0.15
~~~
docker pull mysql:8.0.15
~~~

~~~
mkdir -p /data/mysql/data /data/mysql/logs /data/mysql/conf
~~~

启动mysql_test
~~~
docker run -p 3322:3306 --name mysql_test -v /data/mysql/conf:/etc/mysql/conf.d -v /data/mysql/logs:/logs -v /data/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d 7bb2586065cd
~~~

进入docker容器修改Mysql
~~~
[root@k8s-n1 mysql]# docker exec -it mysql_test /bin/sh

# mysql -u root -p

mysql> ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'test001';
~~~

### 查看Mysql bin_log的开启状态
Mysql8.0是默认开启的，之前默认是关闭的

查看bin_log是否开启，如果为ON则代表开启

~~~
show variables like 'log_bin';
~~~

~~~
mysql> show variables like 'log_bin';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| log_bin       | ON    |
+---------------+-------+
1 row in set (0.00 sec)
~~~~









