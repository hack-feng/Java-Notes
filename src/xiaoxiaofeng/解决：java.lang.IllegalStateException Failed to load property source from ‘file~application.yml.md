## 问题描述

新建了一个项目，然后把之前的数据库连接池配置cv过来了，项目启动居然报错，报错如下：

![image-20230817110952241](https://image.xiaoxiaofeng.site/blog/2023/08/17/xxf-20230817110952.png?xxfjava)

![image-20230817111030989](https://image.xiaoxiaofeng.site/blog/2023/08/17/xxf-20230817111031.png?xxfjava)

## 问题分析

检查了一下，配置格式没有什么问题，因为内容是cv过来的，所以也排除配置的问题。

那就简单了，看着密密麻麻的中文注释，大概率就是编码问题了。

下面简单说说解决方法吧~

## 解决方法

### 方法一

把注释去掉。。。简单粗暴，但不适合呀~，水一波，下面说正事

### 方法二

修改下文件的编码，改成utf-8就可以了，下面一起看一下。

选中项目名称，点击file->settings->Editor->File Encodings,将字符集设置成 UTF-8，然后apply

**然后打开maven,清除掉之前的编译文件**

![image-20230817111410857](https://image.xiaoxiaofeng.site/blog/2023/08/17/xxf-20230817111410.png?xxfjava)

修改完之后，我们再启动看一下，Ok，完美解决问题。

![image-20230817111534204](https://image.xiaoxiaofeng.site/blog/2023/08/17/xxf-20230817111534.png?xxfjava)

本文就到这里了，客官，点个赞再走呗~