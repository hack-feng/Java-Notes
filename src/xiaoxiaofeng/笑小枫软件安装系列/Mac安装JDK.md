软件安装,macos,java,mac,系统安装,开发工具



最新版Mac安装JDK并配置系统环境变量教程，1.双击运行下载好的JDK安装文件。



## 一、JDK获取 ##

1.可以直接去oracle官网获取JDK：[Java Downloads | Oracle][Java Downloads _ Oracle]

JDK8下载直达链接[https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html](https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html)

> 注意是下载jdk，不要下载成jre了。。。

![image-20220810231616778](https://image.xiaoxiaofeng.site/article/img/2022/08/10/xxf-20220810231623.png)



## 二、安装教程 ##

1.双击运行下载好的JDK安装文件。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/10/xxf-1660143649343.png)

2.弹出以下界面后，双击.pkg结尾的文件进入安装引导界面

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/10/xxf-1660143649941.png)

3.弹出安装引导界面后一直点继续安装JDK.

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/10/xxf-1660143650242.png)

4.安装中 ...

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/10/xxf-1660143650785.png)

5.弹出以下界面后表示安装成功

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/10/xxf-1660143651151.png)

6.打开电脑上的命令行界面

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/10/xxf-1660143651848.png)

7.输入命令 java -version 检测JDK是否安装成功，如果出现版本号表示安装成功了（见下图）

    java -version  //查看JDK版本

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/10/xxf-1660143652720.png)

提示：有些同学可能会有疑问，怎么没像windows那样配环境变量也能运行，对于这点我也不是很清楚。。。。

对于其他版本的电脑作者不是很清楚，但是我的这台是不用配的（作者这台电脑是20版的 MacBook Pro）

--------------------

**如果是需要配环境变量的请继续阅读**

8.在命令行页面输入命令 " /usr/libexec/java\_home -V " 查看JDK的安装目录（把目录复制下来，后面有用）

    /usr/libexec/java_home -V

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/10/xxf-1660143653472.png)

9.输入命令 sudo vi ~/.bash\_profile

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/10/xxf-1660143654040.png)

10.在打开的文件中输入下面的命令，JAVA\_HOME 指的是你JDK的安装目录，既步骤八复制的内容

    export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_321.jdk/Contents/Home"

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/10/xxf-1660143654463.png)

**提示：**

1. 在输入法英文模式下 按 " i键 " 进入编辑模式

2. 编写完成后 按 " esc键 " 后输入 " :wq " 保存退出  


11.命令行输入 " source .bash\_profile "使刚才写入的配置生效,然后再次验证JDK是否安装成功。




[Java Downloads _ Oracle]: https://www.oracle.com/technetwork/java/javase/downloads/index.html
[8cd23a7f140b90adf9f50c7615c0d5e0.png]: https://img-blog.csdnimg.cn/img_convert/8cd23a7f140b90adf9f50c7615c0d5e0.png
[62314b44859f5bee27ad8b22cf7db1dc.png]: https://img-blog.csdnimg.cn/img_convert/62314b44859f5bee27ad8b22cf7db1dc.png
[644eb08ce3155797296438e5178825a6.png]: https://img-blog.csdnimg.cn/img_convert/644eb08ce3155797296438e5178825a6.png
[072de50d7e39678dccc01d357b62dc17.png]: https://img-blog.csdnimg.cn/img_convert/072de50d7e39678dccc01d357b62dc17.png
[d3d9c5558bed1e797b12c8604e0f6f7d.png]: https://img-blog.csdnimg.cn/img_convert/d3d9c5558bed1e797b12c8604e0f6f7d.png
[eccc1246cd3ece46d2b7647f1c62718f.png]: https://img-blog.csdnimg.cn/img_convert/eccc1246cd3ece46d2b7647f1c62718f.png
[1910661d0c553ba9c04c5a3db9756403.png]: https://img-blog.csdnimg.cn/img_convert/1910661d0c553ba9c04c5a3db9756403.png
[2a543ae97695115b320611a683f14662.png]: https://img-blog.csdnimg.cn/img_convert/2a543ae97695115b320611a683f14662.png
[a2d5467e3ef5d04888b4586a52b88d94.png]: https://img-blog.csdnimg.cn/img_convert/a2d5467e3ef5d04888b4586a52b88d94.png
[4da7c264421787028ef14c8b516fd87e.png]: https://img-blog.csdnimg.cn/img_convert/4da7c264421787028ef14c8b516fd87e.png