

## 使用可视化界面操作

1、进入历史记录

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/9/xxf-1662713341083.png)

2、这个时候我们可以看到提交的所有记录

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/9/xxf-1662713342325.png)

3、进入某一个即将要回退的版本详情页

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/9/xxf-1662713343217.png)

4、 这个单击"revert",进行回退![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/9/xxf-1662713344050.png)

## 使用git命令操作

1、首先先把需要回退的代码拉取到本地(注意：拉取要回退代码的分支哦！！)

git clone XXX 将代码克隆到本地

git clone -b dev/release XXX 将代码的某个分支克隆到本地

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/9/xxf-1662713344853.png)

2、查看该分支下整个代码的提交历史记录

git log

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/9/xxf-1662713345533.png)

3、回退到某个误提交的版本(注：此处需注意只能回退到commit类型的版本哦！merge是不可以的)

git revert -n 版本号

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/9/xxf-1662713346215.png)

4、回退成功，将之前的版本再次新建成一个新版本

git commit -m "XXX"

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/9/xxf-1662713346866.png)

5、此时，需要将代码push到代码库哦！

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/9/xxf-1662713347556.png)

以上，就完成版本的回退工作了，两种方式大家可以自行选择~

## 关于笑小枫💕

> 本章到这里结束了，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
>
> 微信公众号：笑小枫
>
> 笑小枫个人博客：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
>
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)
>
> 本文源码：[https://github.com/hack-feng/maple-demo](https://github.com/hack-feng/maple-demo) 