## 1. 背景

最近要开始搞模型训练，决定使用conda来对python进行版本隔离管理，本文为最基础的anaconda软件的安装教程。

## 2. 下载安装

可以去官网下载即，下载地址：https://www.anaconda.com/download

如果下载速度很慢，可以去清华镜像源下载：

https://mirrors.tuna.tsinghua.edu.cn/anaconda/archive/ 

官网下载流程如下：

![image-20250623163536216](https://image.xiaoxiaofeng.site/blog/image/image-20250623163536216.png?xiaoxiaofeng)

选择对应的系统进行下载

![image-20250623163631302](https://image.xiaoxiaofeng.site/blog/image/image-20250623163631302.png?xiaoxiaofeng)

然后双击下载的安装文件，无脑的一路向下即可。**下面图片省略部分步骤。**

![image-20250623163753673](https://image.xiaoxiaofeng.site/blog/image/image-20250623163753673.png?xiaoxiaofeng)

![image-20250623163805162](https://image.xiaoxiaofeng.site/blog/image/image-20250623163805162.png?xiaoxiaofeng)

选择对应的安装路径

![image-20250623163855667](https://image.xiaoxiaofeng.site/blog/image/image-20250623163855667.png?xiaoxiaofeng)

## 3. 安装验证

安装完成后搜索`Anaconda Prompt` 并打开，输入查看版本命令`conda --version`，结果如下图：

![image-20250623172345152](https://image.xiaoxiaofeng.site/blog/image/image-20250623172345152.png?xiaoxiaofeng)

也可以在开始菜单中搜索`Anaconda Navigator`并打开，第一次打开会有些慢。

![image-20250623172453391](https://image.xiaoxiaofeng.site/blog/image/image-20250623172453391.png?xiaoxiaofeng)

## 4. Python环境配置

### 4.1 切换镜像源

* 清华源

【方法一】搜索`Anaconda Prompt` 并打开，依次输入以下命令行，可以将下载的镜像源切换到清华上。

```text
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/free/
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/conda-forge 
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/msys2/
conda config --set show_channel_urls yes
```

【方法二】在c盘用户目录下修改`.condarc`文件，加上是清华源。粘贴上述代码会直接在`.condarc`文件生成内容。

![image-20250624102852385](https://image.xiaoxiaofeng.site/blog/image/image-20250624102852385.png?xiaoxiaofeng)

* 阿里源

方法同上

~~~
conda config --add channels https://mirrors.aliyun.com/anaconda/pkgs/main/
conda config --set show_channel_urls yes
~~~

添加完可以使用`conda config --show channels`命令查看配置的镜像源。

### 4.2 切换依赖包位置

要将 Conda 下载的依赖包保存到指定目录，你可以使用以下几种方法：

#### 方法1：临时更改下载目录（单次有效）

```bash
conda install --download-only -p D:\your\target\directory package_name
```

#### 方法2：永久更改 Conda 包缓存目录

1. 首先找到你的 Conda 配置文件位置（通常是 `C:\Users\你的用户名\.condarc`）
2. 编辑或创建 `.condarc` 文件，添加以下内容：

```yaml
pkgs_dirs:
  - D:\your\conda_pkgs
```

3. 保存后，所有后续通过 Conda 安装的包都会默认下载到这个目录

#### 方法3：创建新环境时指定目录

```bash
conda create --prefix D:\your\envs\my_env python=3.8 package_name
```

#### 方法4：使用 `--offline` 模式安装已下载的包

如果你已经下载了包到指定目录，可以这样安装：

```bash
conda install --offline --file D:\your\packages\package.tar.bz2
```

#### 注意事项

1. 确保目标目录有足够的空间（通常 Conda 包会占用几百MB到几GB空间）
2. 路径中使用正斜杠 `/` 或双反斜杠 `\\` 避免转义问题
3. 更改缓存目录后，可能需要运行 `conda clean -a` 清理旧缓存

#### 查看当前 Conda 配置

```bash
conda info
```

这会显示当前的包缓存目录等信息。

希望这些方法能帮助你成功将 Conda 下载的依赖保存到 D 盘指定目录！

### 4.3 使用命令创建环境

输入命令`conda create -n py310 python=3.10`，如下图所示

![image-20250624093725595](https://image.xiaoxiaofeng.site/blog/image/image-20250624093725595.png?xiaoxiaofeng)

输入命令`conda env list`可以看到已经安装的python环境

输入命令`conda activate py310 `，可以切换到对应的环境上去

![image-20250624094123808](https://image.xiaoxiaofeng.site/blog/image/image-20250624094123808.png?xiaoxiaofeng)

### 4.4 使用Anaconda Navigator创建环境

搜索`Anaconda Navigator`并打开，选择Environments

![image-20250624094325337](https://image.xiaoxiaofeng.site/blog/image/image-20250624094325337.png?xiaoxiaofeng)

点击Create按钮，可以添加一个新的python环境。

![image-20250624094440008](https://image.xiaoxiaofeng.site/blog/image/image-20250624094440008.png?xiaoxiaofeng)

等待一会可以看到python 3.11版本的环境创建成功

![image-20250624094620358](https://image.xiaoxiaofeng.site/blog/image/image-20250624094620358.png?xiaoxiaofeng)

### 4.5 在VS Code选择环境

打卡VS Code，创建一个py文件或者打开你的python项目中的文件，可以看到右下角有个环境版本号，点击就可以切换到对应的版本上。

![image-20250624094846413](https://image.xiaoxiaofeng.site/blog/image/image-20250624094846413.png?xiaoxiaofeng)

## 5. 总结

本文介绍了anaconda的安装过程，以及使用anaconda创建python不同的环境，以及环境的切换使用。

后续的文章将陆续为大家介绍如何整理训练集，以及如何去训练使用模型，感兴趣的老铁们可以点个关注。

博主也是边学习，边输出博客，有什么不对的地方欢迎大家指出，也欢迎大佬们加个好友互相交流。

欢迎添加我的微信交流：q1150640979

我的个人网站：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
