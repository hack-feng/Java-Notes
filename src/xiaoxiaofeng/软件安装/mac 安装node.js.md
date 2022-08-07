## 下载node.js

中文官网地址：[https://nodejs.org/zh-cn/](https://nodejs.org/zh-cn/)

英文官网地址：[https://nodejs.org/en/](https://nodejs.org/en/)

访问 Node.js 官方网站地址，如下图：

![笑小枫-20220807225518967](https://image.xiaoxiaofeng.site/article/img/2022/08/07/xxf-20220807225530.png)

> Node.js 官方提供两个版本：一个是长期支持版本，一个是最新版本。**这里建议下载长期支持版本，因为这个版本更稳定。** 

<img src="http://image.xiaoxiaofeng.site/image-20220807220653455.png" alt="image-20220807220653455" style="zoom:50%;" />

这里我们根据自己的系统选择对应的安装包，这里选择的是mac系统的

## 安装 Node.js 的环境

双击 Node.js 的安装文件，进入 Node.js 的安装界面。如下图：

![image-20220807221435238](http://image.xiaoxiaofeng.site/image-20220807221435238.png)

然后一路向下就可以了，直到安装成功～

![笑小枫](http://image.xiaoxiaofeng.site/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTM1NzU0NDE=,size_16,color_FFFFFF,t_70.png)

如上图所示，提供“安装成功”信息后，点击【关闭】按钮，结束 Node.js 环境的安装步骤。

## 验证 Node.js 是否安装成功

运行 Mac 系统下的 “终端” 程序，如下图：

![image-20220807222814020](http://image.xiaoxiaofeng.site/image-20220807222814020.png)

在 “终端” 程序中输入如下命令，验证 Node.js 的环境是否安装成功。

~~~sh
node -v
~~~

如果在 “终端” 程序中出现如下图的提示内容，则表示 Node.js 的环境安装成功。如下图：

![笑小枫-20220807222710097](http://image.xiaoxiaofeng.site/image-20220807222710097.png)

> **需要说明的是：** `node -v` 命令表示查看当前 Node.js 环境的版本。

## 验证 npm 包管理器是否可用

在 “终端” 程序中输入如下命令，验证 npm 包管理器是否可用。

~~~sh
npm -v
~~~

如果在 “终端” 程序中出现如下图的提示内容，则表示 npm 包管理器可用。如下图：

![笑小枫-20220807222921537](http://image.xiaoxiaofeng.site/image-20220807222921537.png)

> **需要说明的是：** `npm -v` 命令表示查看当前 npm 包管理器的版本。

