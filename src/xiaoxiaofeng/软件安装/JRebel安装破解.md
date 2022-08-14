> 标题：人生苦短，我用JRebel_小白码上飞的博客-CSDN博客_jrebel
>
> 描述：使用JRebel让你在开发过程中，无需重启项目即可让代码改动快速生效，极大提高效率，可谓是必备IDEA插件
>
> 作者：小白码上飞
>
> 标签：资源分享,IDEA必备插件,java,IDEA插件
>
> 链接：https://blog.csdn.net/u011291072/article/details/123594219

首先，看一看JRebel和市场上热门的热部署方案比较，如下图：

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660306287580.png)

第一列的JRebel在图中的描述，除了远程debug相关的功能，对其他的功能支持都挺好，如果只是想提升本地开发效率，绝对要试一试JRebel啊！

## 为啥要用热部署插件？ ##

想到了年轻的时候，每次改动代码之后，都要重新启动项目。项目小倒是还好，大一些的项目，加载的东西多，要等上一两分钟。当时就想，如果可以改了代码不重新启动就好了，真是费劲。（真是年少无知，太热爱工作，这重启的时间摸鱼，多是一件美事啊！）后来自己发现，如果只改方法中的几行代码，IDEA重新编译后是可以直接生效的（也就是上图中的IDEA热加载）。如果是增加个类，或者多写个方法，还是得老老实实的重启项目才行。

## JRebel究竟有什么奇效？ ##

### IDEA启动项目 ###

我们写个简单的demo来看看JRebel的效果。

创建一个Spring的web项目，写一个简单的Controller，实现一个post请求：
~~~java
    @RestController
    @Slf4j
    public class TestJRebelController {
        @PostMapping("/test/hello")
        public String hello() {
            return "你好，欢迎常来看看";
        }
    }
~~~
通过IDEA启动项目，本地请求后，正常返回

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660306289329.png)

这时我们增加一个方法`helloWorld()`，如下：
~~~java
    @RestController
    @Slf4j
    public class TestJRebelController {
        @PostMapping("/test/hello")
        public String hello() {
            return "你好，欢迎常来看看";
        }
    
        @PostMapping("/test/helloworld")
        public String helloWorld() {
            return "你好，这个世界欢迎常来看看";
        }
    }
~~~
然后在菜单栏选择Build–>Recompile ‘TestJRebelController.java’，弹出的窗口点击reload，这时左下角会提示新增方法的重新编译对VM是无效的，需要重新启动服务。当然，新的url请求自自然也是失效的。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660306290130.png)

### JRebel启动项目 ###

现在我们安装完JRebel，通过JRebel启动只有`hello()`方法的项目。之后我们再添加`helloWorld()`这个方法，按照刚才的方式重新编译，发现左下角提示“1 class reloaded”。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660306290776.png)

之后请求路径`localhost:8080/test/helloworld`成功返回。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660306291300.png)

完全是实时生效啊！

再试试新增一个类：
~~~java
    @RestController
    @Slf4j
    public class TestController {
        @PostMapping("/test2/helloworld")
        public String hello() {
            return "你好，第二个世界欢迎常来看看";
        }
    }
~~~
这次我没有重新编译，直接请求`localhost:8080/test2/helloworld`，发现直接返回了信息。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660306292195.png)

为啥呢？原来是因为我的IDEA在这里配置了自动编译：

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660306292915.png)

所以当你的代码有变化时，JRebel会自动重新加载最新的代码，所以不用你手动重新编译，就会实时生效啦。同时控制台会输出以下的信息：
~~~java
    2022-03-19 10:18:19 JRebel: Reloading class 'com.example.littleweb.controller.TestController'.
~~~

## 安装JRebel ##

安装插件的过程很简单。

### 1、IDEA插件市场搜索并安装 ###

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660306293651.png)

### 2、激活 ###

重启IDEA后，根据首次安装指引，进入激活步骤。这里感谢薯条大佬搭建的激活服务器，直接访问，[https://jrebel.qekang.com](https://jrebel.qekang.com)，复制界面上展示的激活链接，填充到激活页面，然后随便填写一个邮箱名即可完成激活。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660306294700.png)

然后进入idea的激活JRebel的页面，如下图所示：

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660306295868.png)

### 3、启动项目 ###

之后用这个小绿火箭的图标来启动或者debug项目就可以啦。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/8/12/xxf-1660306297647.png)

好啦，这样，摸鱼的时间就又减少了。


[img]: https://img-blog.csdnimg.cn/img_convert/feabc010fbd0a0467f3bcb155faede3b.png
[img 1]: https://img-blog.csdnimg.cn/img_convert/ec337562dc951570e973e61743025ccb.png
[img 2]: https://img-blog.csdnimg.cn/img_convert/4cb0b06a16339a4857268bc9723259a8.png
[img 3]: https://img-blog.csdnimg.cn/img_convert/7cf97734385cc588b19a05b0a44d73bc.png
[img 4]: https://img-blog.csdnimg.cn/img_convert/d62e0fcb081dcb1d0179907c9b90219c.png
[img 5]: https://img-blog.csdnimg.cn/img_convert/2d05f43a841c8da44f69cdc9ec03d5bc.png
[img 6]: https://img-blog.csdnimg.cn/img_convert/c2dc072b4fb156f0ee8da0c3dd86abb5.png
[img 7]: https://img-blog.csdnimg.cn/img_convert/fe9706c559a682684bde27d119ae3090.png
[img 8]: https://img-blog.csdnimg.cn/img_convert/715c5a15e09cf6bf8bc357073a697464.png
[img 9]: https://img-blog.csdnimg.cn/img_convert/d32fef93a18c57ffca0f7a5c6ee08730.png
[img 10]: https://img-blog.csdnimg.cn/img_convert/6b367d54dc0b2da587efb84652c3351d.png