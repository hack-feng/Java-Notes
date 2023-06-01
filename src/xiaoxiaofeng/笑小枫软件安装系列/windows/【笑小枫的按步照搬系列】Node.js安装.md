## 下载node.js

中文官网地址：[https://nodejs.org/zh-cn/](https://nodejs.org/zh-cn/)

英文官网地址：[https://nodejs.org/en/](https://nodejs.org/en/)

访问 Node.js 官方网站地址，如下图：

![image-20221211141633974](https://image.xiaoxiaofeng.site/blog/image/image-20221211141633974.png?xiaoxiaofeng)

我们下载长期维护版本就行了，这里就不做吃螃蟹的人了，喜欢体验最新版本的可以下载最新尝鲜版哈~

想下载其他版本的，可以自己寻找[全版本node.js](https://nodejs.org/dist/)

<img src="https://image.xiaoxiaofeng.site/blog/image/image-20221211142356775.png?xiaoxiaofeng" alt="image-20221211142356775" style="zoom:50%;" />

## 安装 Node.js 的环境

双击 Node.js 的安装文件，进入 Node.js 的安装界面。如下图：

<img src="https://image.xiaoxiaofeng.site/blog/image/image-20221211142715111.png?xiaoxiaofeng" alt="image-20221211142715111" style="zoom:50%;" />

然后一路向下就可以了，直至安装成功~

<img src="https://image.xiaoxiaofeng.site/blog/image/image-20221211214008032.png?xiaoxiaofeng" alt="image-20221211214008032" style="zoom:50%;" />

如上图所示，就已经成功完成了Node.js 环境的安装步骤。接下来，我们验证一下~

## 验证 Node.js 是否安装成功

打开我们的小黑面板（如果已经打开了，请关闭重新打开，或者打开一个新的），输入如下命令，验证 Node.js 的环境是否安装成功。

~~~sh
node -v
~~~

如果在程序中出现如下图的提示内容，则表示 Node.js 的环境安装成功。如下图：

<img src="https://image.xiaoxiaofeng.site/blog/image/image-20221211214456584.png?xiaoxiaofeng" alt="image-20221211214456584" style="zoom:50%;" />

> **需要说明的是：** `node -v` 命令表示查看当前 Node.js 环境的版本。 

## 验证 npm 包管理器是否可用

在小黑面板中输入如下命令，验证 npm 包管理器是否可用。

~~~sh
npm -v
~~~

如果出现如下图的提示内容，则表示 npm 包管理器可用。如下图：

<img src="https://image.xiaoxiaofeng.site/blog/image/image-20221211214630690.png?xiaoxiaofeng" alt="image-20221211214630690" style="zoom:50%;" />

> **需要说明的是：** `npm -v` 命令表示查看当前 npm 包管理器的版本。

## npm使用国内淘宝镜像的方法

### 通过命令配置

1. 命令

```
npm config set registry https://registry.npm.taobao.org
```

2. 验证命令

```
npm config get registry
```

如果返回https://registry.npm.taobao.org，说明镜像配置成功。

<img src="https://image.xiaoxiaofeng.site/blog/image/image-20221211215155256.png?xiaoxiaofeng" alt="image-20221211215155256" style="zoom:50%;" />

### 通过使用cnpm安装
1. 安装cnpm

```
npm install -g cnpm --registry=https://registry.npm.taobao.org
```

2. 使用cnpm

```
cnpm install xxx
```

至此，我们的node环境已经配置好了，接下来尽情的去享用吧~