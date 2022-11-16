## 笑小枫💕

> 欢迎来到笑小枫的世界，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
>
> 微信公众号：笑小枫
>
> 笑小枫个人博客：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
>
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)



## 1.下载： ##

> 本文须知：安装maven环境之前要先安装java jdk环境（没有安装java环境的可以先去看安装JAVA环境的教程）Maven 3.3+ require JDK 1.7 及以上。

方式一：可以从官方下载，下载页面：http://maven.apache.org/download.cgi  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478511699.png)

方式二：

博主提供的百度网盘版本下载：[https://pan.baidu.com/s/1QkhJo9b79zQi_6ll2zoVbg?pwd=gro3](https://pan.baidu.com/s/1QkhJo9b79zQi_6ll2zoVbg?pwd=gro3)



下载好后是一个压缩文件  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478512389.jpg)

## 2.安装： ##

maven压缩包解压到一个没有中文，空格或其他特殊字符的文件夹内即可使用。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478512688.png)

## 3.配置MAVEN\_HOME ##

maven 的使用是在jdk的基础上，所以电脑必须有jdk  
第一步：新增环境变量：`MAVEN_HOME`  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478513379.png)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478513984.png)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478514861.png)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478515529.png)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478516247.png)  
第二步：在path环境变量中添加：`%MAVEN_HOME%\bin `
找到环境变量配置界面  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478516756.png)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478517356.png)  
第三步：测试：按住win+R 输入cmd，进入黑窗口控制台。输入命令： `mvn -v`  
如果出现以下maven的版本信息，则说明maven的安装与环境变量的配置均正确；  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478517947.png)

## 4.配置仓库 ##

maven的仓库可以分为3种：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478518742.png)  
### 一：配置本地仓库  
一般情况下，我们需要自己重新设置本地仓库的地址，设置方法如下：

1.  第一步：在maven的安装目录下创建一个repository文件夹，位置可以随便放，这里只是演示。  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478519368.png)  
    第二步：在核心配置文件setting.xml中，将仓库存放的位置设置成：D:\\WorkSpace\\apache-maven-3.6.1-bin\\apache-maven-3.6.1\\repository（以自己的repository创建路径为准)；

本地仓库的位置是通过maven的核心配置文件（settings.xml）来配置的。settings.xml文件位于maven安装目录：…\\apache-maven-3.6.1\\conf\\settings.xml 。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478520217.png)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478520763.png)  
### 二： 配置私服

    打开settins.xml文件，找到<mirrors>标签节点，在这个标签中添加一些配置信息中的任意一个：

【阿里云私服】

```xml
<mirror>
     <id>nexus-aliyun</id>
     <mirrorOf>*</mirrorOf>
     <name>Nexus aliyun</name>
     <url>http://maven.aliyun.com/nexus/content/groups/public</url>
 </mirror>
```

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478521769.png)  
注: 千万注意, 只可配置一个(另一个要注释!) 不然两个可能发生冲突 ,导致jar包下不下来 !!!

## 补充知识点(添加maven到我们的IDEA中)

1.  安装IEAD,无脑安装，我相信你一定会安装。
2.  新建项目，我们选择maven项目，选中本地安装JDK的目录

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478407491.png)

2.点击File的setting会发现我们新建的maven项目没有指向我们本地的maven地址

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/15/xxf-1668478408821.png)

3.设置IDEA的maven指向我们自己的maven地址：

![image-20221115102452877](https://image.xiaoxiaofeng.site/article/img/2022/11/15/xxf-20221115102454.png)

4.点进去找到我们的maven设置，选择我们的自己的maven目录地址、配置setting文件及仓库地址如下图（设置好了记得点击应用及确定哦）：

![image-20221115102255567](https://image.xiaoxiaofeng.site/article/img/2022/11/15/xxf-20221115102258.png)
