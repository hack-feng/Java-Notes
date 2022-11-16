## 笑小枫💕

> 欢迎来到笑小枫的世界，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
>
> 微信公众号：笑小枫
>
> 笑小枫个人博客：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
>
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)

## RedisInsight简介 ##

RedisInsight是Redis官方出品的可视化管理工具，可用于设计、开发、优化你的Redis应用。支持深色和浅色两种主题，界面非常炫酷！可支持String、Hash、Set、List、JSON等多种数据类型的管理，同时支持远程使用CLI功能，功能非常强大！

下面是RedisInsight的一张使用效果图，颜值不错！

![image-20221116113342463](https://image.xiaoxiaofeng.site/article/img/2022/11/16/xxf-20221116113345.png)

## 使用 ##

> Redis服务安装完毕，接下来我们就使用RedisInsight来管理下它试试！

### 基本使用 ###

 *  首先下载RedisInsight的安装包，下载地址：https://redis.com/redis-enterprise/redis-insight/

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668568693481.png)

 *  下载完成后直接安装即可，安装完成后在主界面选择`添加Redis数据库`；

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668568694193.png)

 *  选择`手动添加数据库`，输入Redis服务连接信息即可；

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668568694759.png)

 *  打开连接后即可管理Redis，右上角会显示已经安装的Redis增强模块；

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668568695265.png)

 *  接下来我们就可以通过RedisInsight在Redis中添加键值对数据了，比如添加`String`类型键值对；

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668568695793.png)

 *  添加Hash类型，编辑的时候可以单个属性编辑，还是挺方便的；

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668568696414.png)

 *  添加List类型，编辑的时候可以直接Push元素进去；

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668568696947.png)

 *  添加JSON类型，安装RedisJSON模块后可支持；

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668568697736.png)

 *  对原生JSON类型，不仅支持高亮预览，还能支持新增、编辑和删除单个属性，够方便！

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668568698283.png)

 *  另外RedisInsight还支持深色和浅色两种主题切换，在设置中即可更改。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668568698856.png)

### CLI ###

 *  如果RedisInsight的图形化界面功能满足不了你的话，还可以试试它的`CLI`功能，点击左下角CLI标签即可打开；

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668568699648.png)

 *  贴心的Redis官方怕你记不住命令，还添加了`Command Helper`这个查找命令文档的功能，比如我们可以搜索下`hget`这个命令的用法。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668568700239.png)

### Profiler ###

通过Profiler功能，我们可以查看Redis的命令执行日志，比如我们使用RedisInsight添加一个叫`testKey`的键值对，Profiler将显示如下日志。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668568700819.png)

## 总结 ##

RedisInsight不愧是官方出品的可视化工具，感觉是目前用起来体验最好的Redis工具了！特别是对Redis新特性的支持，其他工具是无法比拟的！

## 参考资料 ##

> 感觉Redis的官方文档做的特别良心，强烈建议大家看下！

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/16/xxf-1668568705024.png)

官方文档：https://developer.redis.com/explore/redisinsightv2
