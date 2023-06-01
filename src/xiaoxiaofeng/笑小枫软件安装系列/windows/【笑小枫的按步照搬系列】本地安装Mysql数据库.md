## 笑小枫💕

> 欢迎来到笑小枫的世界，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
>
> 微信公众号：笑小枫
>
> 笑小枫个人博客：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
>
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)



## 一、官网下载MySQL ##

1.下载地址

博主提供的百度网盘版本下载：[https://pan.baidu.com/s/14Dre4Sfr7XdxIYWHPiVSlw?pwd=yubo](https://pan.baidu.com/s/14Dre4Sfr7XdxIYWHPiVSlw?pwd=yubo)

官网地址： [https://www.mysql.com/](https_www.mysql.com)

2.在官网首页拉到最下方，点击MySQL Community Server： 
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566526678.png)  
3.根据个人电脑的操作系统选择，此处以Windows x64为例，选择第一个，点击“Download”：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566527912.png)  
4.选择“No thanks, just start my download.”：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566528845.png)

## 二、安装MySQL ##

1.将下载成功的压缩包解压到相应目录下，此处以D:MySQL为例，务必记住此路径，将在之后的配置中使用：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566530230.png)  
2.打开该文件夹，在该文件夹下创建my.ini配置文件：  
①在该文件夹下鼠标右击创建一个文本文件：![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566531089.png)②命名为my：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566533046.png)③双击打开输入如下内容：  
注意：  
1.并不是完全按照如下内容输入，mysql的安装目录一项需要根据本机的安装目录填写  
2.datadir在MySQL版本为8及以上时不需要设置（例如本篇安装的MySQL）

```ini
[client]
# 设置mysql客户端默认字符集
default-character-set=utf8
 
[mysqld]
# 设置3306端口
port = 3306
# 设置mysql的安装目录
basedir=D:\\MySQL\\mysql-8.0.19-winx64
# 设置 mysql数据库的数据的存放目录，MySQL 8+ 不需要以下配置，系统自己生成即可，否则有可能报错
# datadir=D:\\MySQL\\sqldata
# 允许最大连接数
max_connections=20
# 服务端使用的字符集默认为8比特编码的latin1字符集
character-set-server=utf8
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB
```

④点击“另存为”，存为.ini文件：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566533748.png)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566534888.png)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566531089.png)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566536818.png)

## 三、启动MySQL ##

1.以管理员身份打开 cmd 命令行工具：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566537781.png)  
2.把目录切换为解压后文件中的bin文件的路径：

    cd D:MySQLmysql-8.0.19-winx64/bin

若此语句无法切换成功，使用：

    cd /d D:MySQLmysql-8.0.19-winx64/bin

切换成功应显示为：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566540431.png)  
3.初始化数据库：

    mysqld --initialize --console

4.执行完成后，会输出 root 用户的初始默认密码，如下：  
9ACfMu2y9a\*R就是初始密码，后续登录需要用到，登录后可修改密码。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566541163.png)  
5.输入以下安装命令：

    mysqld install

6.输入以下启动命令：

    net start mysql

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566541808.png)

## 四、登录本机的MySQL ##

1.本机 MySQL 数据库登录命令：

```mysql
mysql -u root -p
```

2.按回车确认, 如果安装正确且 MySQL 正在运行, 会得到以下响应：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566542632.png)  
3.输入三-4.中的密码，登录成功：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566543507.png)4.修改密码命令（以新密码为123456为例)：

```mysql
ALTER USER 'root'@'localhost' IDENTIFIED BY '123456';
```

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668566544230.png)

注意上述修改密码，如果使用远程工具连接会报1251错误解决方案见下文，如果需要避免，使用下面的方式~😂😂😂

> 为什么把正确的方式放下面呢，因为我遇到过几次，带大家把能蹚的坑都过一遍吧，加深下印象~~~ 

~~~sql
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';
~~~

## 五、使用Navicat连接mysql

关于Navicat的安装方法见本系列的另一篇文章[【笑小枫的按步照搬系列】数据库可视化工具Navicat安装及破解.md](https://www.xiaoxiaofeng.com/archives/navicat)

然后我们可以使用navicat数据库可视化工具进行连接我们的mysql，但是在连接的过程中报错如下

![image-20221128131849596](https://image.xiaoxiaofeng.site/blog/image/image-20221128131849596.png?xiaoxiaofeng)

出现这个原因是mysql8 之前的版本中[加密](https://so.csdn.net/so/search?q=加密&spm=1001.2101.3001.7020)规则是mysql_native_password,而在mysql8之后,加密规则是caching_sha2_password
解决办法：把mysql用户登录密码加密规则还原成mysql_native_password.

### 修改加密规则

步骤： 
1.打开cmd命令窗口，输入命令 mysql -uroot -p 打开MySQL数据库，然后输入密码进行登录。 
![image-20221128132733866](https://image.xiaoxiaofeng.site/blog/image/image-20221128132733866.png?xiaoxiaofeng)

2.修改加密规则
将加密方式改为mysql\_native\_password  
~~~sql
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';
~~~
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669613129548.png)

3.更新用户的密码
这里将密码更改为password，如果想要更改其他密码，把password替换掉即可  
~~~sql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'password' PASSWORD EXPIRE NEVER;`  
~~~
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669613130074.png)

4.输入命令
`FLUSH PRIVILEGES;` 进行刷新。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669613130834.png)  
5. 这个时候便可以使用Navicat连接我们的数据库，点击【连接测试】则可以看到连接成功的提示哟。

![image-20221128132348907](https://image.xiaoxiaofeng.site/blog/image/image-20221128132348907.png?xiaoxiaofeng)

## 五、注意事项 ##

my.ini文件中的内容一定先根据本地电脑的解压路径进行修改。

mysql8 之前的版本中加密规则是mysql_native_password，而在mysql8之后,加密规则是caching_sha2_password。

