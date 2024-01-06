## 笑小枫💕

> 欢迎来到笑小枫的世界，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
>
> 微信公众号：笑小枫
>
> 笑小枫个人博客：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
>
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)



--------------------

## 一、安装 

1、方式一：进入官网[https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html](https_www.oracle.com_java_technologies_javase_javase-jdk8-downloads.html)下载JDK

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660293003850.jpg)

方式二：博主提供的百度网盘版本下载：[https://pan.baidu.com/s/1liNHej2hGXJU_L8yQNRZmQ?pwd=9w3f ](https://pan.baidu.com/s/1liNHej2hGXJU_L8yQNRZmQ?pwd=9w3f)



**需要变更安装目录的看这里**

>  本文单纯为了演示，都是用的默认的安装路径。不想改文章了。简单补充说说我常用的安装目录吧。
>
> 我一般喜欢在D盘。在D盘创建目录：`D:\workpath\java8`，然后在`java8`目录下创建`jdk`和`jre`两个目录。
>
> 下文中步骤3，选择`jdk`目录，步骤4选择`jre`目录即可。
>
> 配置中步骤3的环境变量路径也要对应的选择`jdk`目录：`D:\workpath\java8\jdk`



2、下载后双击即可安装，选择下一步

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660293004453.jpg)  
3、选择开发工具，更改jdk安装路径（默认安装在C盘)，然后下一步

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660293004814.jpg)  
4、更改jre安装路径（采用默认安装位置），下一步

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660293005161.jpg)  
5、安装完成  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660293005523.jpg)

## 二、配置 #

1、右击计算机图标，接着点击菜单最下方的属性菜单项，选择高级系统设置

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660293005862.jpg)  
2、选择”高级“选项卡，然后点击下方的环境变量：

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660293006559.jpg)

3、在系统变量中新建`JAVA_HOME`，变量名输入：`JAVA_HOME`，变量值输入JDK的安装目录： `C:\Program Files\Java\jdk1.8.0_291`，点击确定

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660293006914.jpg)

4、在系统变量中查看是否有`classpath`变量，如果没有，则新建这个变量，变量名`classpath` 变量值 `.;%JAVA_HOME%\lib;` 注意，此变量值以英文句点符号开始，以分好结束。然后点击确定  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660293007259.jpg)  
5、最后，在系统变量里面双击编辑`Path`变量，在弹出的对话框后，新建两个变量值：`%JAVA_HOME%\bin`和`%JAVA_HOME%\jre\bin`。Windows7则是在变量值的最后，添加如下字符串：`;%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin`注意，前面第一个是分号。若没有Path变量，则添加Path变量

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660293007600.jpg)  
6、检查JDK是否安装成功。在cmd窗口下（快捷键：Win+R 输入cmd），输入`java –version`，如下图所示，则说明jdk安装成功。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660293007971.jpg)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660293008324.jpg)

7、在cmd窗口下（快捷键：Win+R 输入cmd），输入`javac`，如下图所示,则说明配置环境变量完成。

![image-20221112153717365](https://image.xiaoxiaofeng.site/blog/image/image-20221112153717365.png?xiaoxiaofeng)