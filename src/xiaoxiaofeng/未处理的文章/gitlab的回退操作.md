

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


[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAWlJSfg_size_20_color_FFFFFF_t_70_g_se_x_16]: https://img-blog.csdnimg.cn/017be99cad7943c6acf02f7870922386.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAWlJSfg==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAWlJSfg_size_20_color_FFFFFF_t_70_g_se_x_16 1]: https://img-blog.csdnimg.cn/fe00675bbea642208fedc771fdb117e2.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAWlJSfg==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAWlJSfg_size_20_color_FFFFFF_t_70_g_se_x_16 2]: https://img-blog.csdnimg.cn/a72e4108727549a2a0fac7c4e21adbbb.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAWlJSfg==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAWlJSfg_size_20_color_FFFFFF_t_70_g_se_x_16 3]: https://img-blog.csdnimg.cn/636ed50f07bb40208909737ad4192057.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAWlJSfg==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAWlJSfg_size_20_color_FFFFFF_t_70_g_se_x_16 4]: https://img-blog.csdnimg.cn/f21369fe1e8a432199cf0aa5e13cd26d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAWlJSfg==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAWlJSfg_size_20_color_FFFFFF_t_70_g_se_x_16 5]: https://img-blog.csdnimg.cn/ca30db96b37747aea358893208355152.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAWlJSfg==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAWlJSfg_size_20_color_FFFFFF_t_70_g_se_x_16 6]: https://img-blog.csdnimg.cn/b27e165676d54842a8230910e06fac6e.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAWlJSfg==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAWlJSfg_size_20_color_FFFFFF_t_70_g_se_x_16 7]: https://img-blog.csdnimg.cn/760876355f9a44e9b618679b8a7109e9.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAWlJSfg==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAWlJSfg_size_20_color_FFFFFF_t_70_g_se_x_16 8]: https://img-blog.csdnimg.cn/5ef553137abc49ccb779550d75b60e92.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAWlJSfg==,size_20,color_FFFFFF,t_70,g_se,x_16