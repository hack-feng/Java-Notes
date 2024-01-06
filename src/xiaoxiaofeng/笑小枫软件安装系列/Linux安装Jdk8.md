本文以centos7为例，一步一步进行jdk1.8的安装。

## 1. 下载安装

官网下载链接：
https://www.oracle.com/cn/java/technologies/downloads/#java8

![image-20231113162131625](https://image.xiaoxiaofeng.site/blog/2023/11/13/xxf-20231113162131.png?xxfjava)

上传jdk的压缩包到服务器的`/usr/local`目录下

在当前目录解压jdk压缩包，如果是其它版本，注意替换后面压缩包名称

~~~shell
tar -zxvf jdk-8u391-linux-x64.tar.gz 
~~~

解压完，文件夹如下图所示，多了`jdk1.8.0_391`目录

![image-20231113163606489](https://image.xiaoxiaofeng.site/blog/2023/11/13/xxf-20231113163606.png?xxfjava)

## 2. 配置

使用命令：`vim /etc/profile`
进入编辑状态，加入下边这段配置，并保存退出

**注意配置中JAVA_HOME的路径，要与上文的一致**

~~~shell
export JAVA_HOME=/usr/local/jdk1.8.0_391
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
~~~

![image-20231113164058233](https://image.xiaoxiaofeng.site/blog/2023/11/13/xxf-20231113164058.png?xxfjava)

重新加载配置，输入命令：`source /etc/profile`

## 3. 验证

输入`java -version`

![image-20231113164231075](https://image.xiaoxiaofeng.site/blog/2023/11/13/xxf-20231113164231.png?xxfjava)

输入`javac`

![image-20231113164438423](https://image.xiaoxiaofeng.site/blog/2023/11/13/xxf-20231113164438.png?xxfjava)

大功告成，JDK安装完成了。