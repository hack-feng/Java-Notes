## 笑小枫💕

> 欢迎来到笑小枫的世界，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
>
> 微信公众号：笑小枫
>
> 笑小枫个人博客：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
>
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)

## 简介

支持全家桶，如idea,pycharm,webstorm,goland......等等，亲测有效，请放心使用~

针对最新 **2021.3.1**版本可以做到永久激活、直接激活99年！首先将电脑上的旧版本 IDEA 卸载干净，如果你的电脑是一台新电脑、没有安装过 IDEA ，那么你就可以跳过这一步。一定要按照下图所示勾选，确保完全卸载干净。

安装这里就不说了，直接官网下载，双击安装就可以了，注意版本 **2021.3.1**，因为我用的是这个版本测试的，其他版本是否有效，暂未亲自测试。

如果软件打开了，把软件关闭后再操作。


## 获取插件

插件下载地址：https://pan.baidu.com/s/1MGRmLL06o0AircXmF466Vg?pwd=aghq 

关注微信公众号【笑小枫】，可以微信搜索，或者直接用微信扫描下方二维码直接进行关注，回复【破解2021】获取插件解压用到的密码呦。

![wxgzh](http://file.xiaoxiaofeng.site/blog/wxgzh.jpg)


## window破解

将ja-netfilter-all.zip补丁压缩到D盘（我是放在 D 盘下面），所以激活补丁路径为：-javaagent:d:/ja-netfilter-all/ja-netfilter.jar

![img](http://file.xiaoxiaofeng.site/blog/3oAS0MuFk451gFgfFjXh1A.png)

在软件的快捷键上  右键 ——》点击属性   

 ![img](http://file.xiaoxiaofeng.site/blog/rWpNShLEre7qlN4ApA6DMg.png)        

点击打开文件所在的位置

 ![img](http://file.xiaoxiaofeng.site/blog/SoKZsgniZEkDDeRZq81rtA.png)        

找到 idea64.exe.vmoptions 文件并且打开。

 ![img](http://file.xiaoxiaofeng.site/blog/NKfU3u5e-PRUYHXXGgCxUw.png)        

右击选择打开方式：文本编辑直接看最后几行的开头，应该能找到连续都是五行D开头的代码，最后一行以D开头的结尾是=off，应该都是这个。在最后面加-javaagent:（路径）。`-javaagent:d:/ja-netfilter-all/ja-netfilter.jar`

如果下面还多了两行代码，我不知道大家的是多少，记得要把这两行代码删除掉，不然会激活不成功。

现在我们打开 IDEA 软件，如下图所示点击 OK。

 ![img](http://file.xiaoxiaofeng.site/blog/Inz9kGyJK0hkrG2UHoMAYg.png)        

回到 IDEA ，点击 Activation code，将激活码粘贴进去，然后点击 Activate 激活。

![img](http://file.xiaoxiaofeng.site/blog/VS3oNbc1dXKVjVADIFYyGw.png)        

如下如所示，就说明你已经激活成功了。

![image-20221128194123310](https://image.xiaoxiaofeng.site/blog/image/image-20221128194123310.png?xiaoxiaofeng)        

切记，后续不要升级 IDEA，否则激活可能会失效！！！。

如果按上面操作完成后仍然激活不了：检查激活补丁的路径 有没有写对。



## MAC破解

将ja-netfilter-all.zip补丁压缩到你想要的文件夹，注意不要有中文，我这里的激活补丁路径为：/Users/maple/maple/idea/ja-netfilter.jar

找到idea.vmoptions文件（/applications/IntelliJ IDEA.app/Contents/bin/idea.vmoptions）

打开访达，应用程序里面找到idea

![image-20220520112640638](http://file.xiaoxiaofeng.site/blog/image-20220520112640638.png)

右击选择显示包内容，就可以找到对应的idea.vmoptions文件了，路径为（/Contents/bin/idea.vmoptions）

![image-20220520112956839](http://file.xiaoxiaofeng.site/blog/image-20220520112956839.png)

右击选择打开方式：文本编辑直接看最后几行的开头，应该能找到连续都是五行D开头的代码，最后一行以D开头的结尾是=off，应该都是这个。在最后面加-javaagent:（路径）。

如果下面还多了两行代码，我不知道大家的是多少，记得要把这两行代码删除掉，不然会激活不成功。

打开idea.vmoptions文件，在最后添加`-javaagent:/Users/maple/maple/idea/ja-netfilter.jar`，注意需要对应上文插件的位置哈

![image-20220520113408568](http://file.xiaoxiaofeng.site/blog/image-20220520113408568.png)

然后打开idea，配合文件夹中的code进行激活，操作如window一致。然后就可以看到成功激活到了2099年。

![image-20221128194153062](https://image.xiaoxiaofeng.site/blog/image/image-20221128194153062.png?xiaoxiaofeng)
