## 为啥要记录呀
今天在修改Docker内部文件的时候，安装vim居然失败了，在执行`apt-get update`时一直有几个404，解决无果，最后放弃安装vim，将文件拷贝出来修改，然后再拷贝到docker内部。记录一下如何修改Docker内部文件的方法吧。

![image-20231110153456506](https://image.xiaoxiaofeng.site/blog/2023/11/10/xxf-20231110153525.png?xxfjava)

## 记录了个啥呀

Docker修改容器内部文件的方法一共有三种，下面进行一一介绍。

### 方法一、安装Vim的方式修改 

进入容器内部修改 使用下面的命令以命令行的形式可以进入容器的内部对文件进行修改。

```bash
docker exec -it 容器ID /bin/bash
```

不过里面没有vim，需要自行安装，安装代码如下所示。不过不推荐这种形式，因为里边的文件是临时的，容器被删除之后，配置就失效了，需要从新配置。

```sql
apt-get update
apt-get install vim
```
### 方法二、将文件拷贝出来修改
通过docker cp拷贝进行修改 可以通过下面的代码将需要修改的文件拷贝出来，修改完成之后再拷贝回去。这种方式其实和第一种差不多，只是不用安装vim，但是容器被删除之后，修改过的内容也会失效。而且需要重启容器才能生效（好像）

```bash
#将容器中的文件拷贝出来
sudo docker cp 容器ID:/etc/mysql/my.cnf /home/mysql/
#将容器中的文件拷贝回去
sudo docker cp /home/mysql/my.cnf  容器ID:/etc/mysql/
```
### 方法三、通过-v挂载文件夹
3、使用-v挂载文件夹(推荐) 最后一种方法是在启动的时候使用-v将容器内部的文件夹挂载（映射）到本地的某个路径下，以后以后可以直接在本地修改，不需要进入容器内部.

```shell
#冒号前是本地路径（需要绝对路径），冒号后是容器中的路径
$ docker run -p 3306:3306 --name mysql_8 -v /home/mysql/conf:/etc/mysql/conf.d -d 7bb2586065cd
```

## 不想总结的总结
总结个啥子来，记住我是**笑小枫**就行啦。点个收藏吧，不然就如下图了哈~

![image-20231110154837139](https://image.xiaoxiaofeng.site/blog/2023/11/10/xxf-20231110154837.png?xxfjava)