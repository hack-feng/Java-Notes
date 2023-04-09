## 背景

最近在开发【Java面试 | 笑小枫】小程序，便发现老是有人半夜刷题，如下图所示：

<img src="https://image.xiaoxiaofeng.site/blog/image/image-20230321205840646.png?xiaoxiaofeng" alt="image-20230321205840646" style="zoom:50%;" />

现在都这么卷了吗？大半夜的都不睡觉了吗？还在撸题~越想越不对，赶紧看了一下，发现自己录入题目的时间也好多都在凌晨。

好家伙，秒懂，时区错了。错就错了吧，影响也不大。

直到现在出现了每日签到的功能，好吧顺手改一下，反正也不难。都改了，顺手整理篇博客吧



## 知识点

> UTC：**C**oordinated **U**niversal **T**ime 协调世界时。
>
> GMT：**G**reenwich **M**ean **T**ime 格林尼治标准时间。（在协调世界时意义上的0时区，即GMT = UTC+0）
>
> 中国的时间是【东八区】，比GMT多八个小时，即 GMT+8（或UTC+8，但习惯上还是用GMT+8）



## 代码中常见的三种时间差错问题

### 【我遇到的】本地获取的时间没有错，存入数据库的时候时间相差8小时

mybatis将本地的数据传入到mysql数据库服务器的时候，服务器会对数据进行检测，会把date类型的数据自动转换为mysql服务器所对应的时区，即0时区，所以会相差8小时。

解决方案：

* 在数据库链接上添加`serverTimezone=GMT%2B8`

![image-20230321214129614](https://image.xiaoxiaofeng.site/blog/image/image-20230321214129614.png?xiaoxiaofeng)

### java下使用 `new date()`获取的时间会和真实的本地时间相差8小时

new date（）调用的是jvm时间，而jvm使用的时间默认是0时区的时间，即：和北京时间将会相差8小时。

解决方案：

* 手动设置jvm时间：将时间改为第8时区的时间：
* 如果是springboot项目，可以面向切面加上这个，或者启动main类上加上如下代码：
~~~java
TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
~~~


### 数据库时间没有错，获取到了后端，之后返回给前端相差8小时

springboot中对加了@RestController或者@Controller+@ResponseBody注解的方法的返回值默认是Json格式，所以，对date类型的数据，在返回浏览器端时，会被springboot默认的Jackson框架转换，而Jackson框架默认的时区GMT（相对于中国是少了8小时）。所以最终返回到前端结果是相差8小时。

解决方案：

* 将spring的json构造器的时区改正即可，在application.yml文件中添加：

~~~yml
spring:
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
~~~
* 可以使用注解，在entity实体类的date数据上添加注解，那么数据库传回的data数据要转换为json格式的时候就是北京时间了，再次传回到前端的时候，也不会出现时区问题。
~~~java
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
private Date updateDate;
~~~



## 数据库代码时区的问题

以上说的都是代码中时间的问题，还有一种情况，就是sql使用`NOW()`获取时间，这种写法太可恶了。**强烈不推荐**

这种情况使用的是数据库的时间，首先我们看一下数据库时间

~~~sql
select NOW();
~~~

<img src="https://image.xiaoxiaofeng.site/blog/image/image-20230321215559765.png?xiaoxiaofeng" alt="image-20230321215559765" style="zoom: 50%;" />

如果和当前时间一致，那么恭喜你，没问题。

如果比当前时间少8小时，那么依旧恭喜你，你穿越了。

![image-20230321215226932](https://image.xiaoxiaofeng.site/blog/image/image-20230321215226932.png?xiaoxiaofeng)

言归正传，如果比当前时间少8小时，该怎么处理呢？



### 通过Sql命令修改，临时生效

本方法的优点是，生效快，不需要重启数据库；缺点是重启数据库后配置失效。

1. **首先检查下Mysql系统时区**

~~~sql
show variables like '%time_zone%';
~~~

<img src="https://image.xiaoxiaofeng.site/blog/image/image-20230321215721287.png?xiaoxiaofeng" alt="image-20230321215721287" style="zoom:50%;" />

2. **设置时区**

~~~SQL
-- 修改mysql全局时区为北京时间，即我们所在的东8区
set global time_zone = '+08:00'; 

-- 修改当前会话时区，不然需要重新打开会话才会生效
set time_zone = '+08:00';
~~~

3. **立即刷新生效**

~~~sql
flush privileges;
~~~

然后再执行一下我们的`select NOW();`查看一下时间，OK，时间一致

<img src="https://image.xiaoxiaofeng.site/blog/image/image-20230321220030720.png?xiaoxiaofeng" alt="image-20230321220030720" style="zoom:50%;" />

### 通过配置文件来进行修改，永久性生效

本方法的优点是永久性生效，缺点是需要重启数据库

修改mysql的配置文件。linux系统上是my.cnf文件，window系统是my.ini

在[mysqld]区域中加上 default-time_zone = '+8:00'

重启mysql使新时区生效



## 总结

本文到这里就结束了。总结一下吧

1. 代码中常见的数据问题是，程序中正常，保存到数据库中差8小时，这种情况用在数据库连接中添加`serverTimezone=GMT%2B8`
2. Java下使用 `new date()`获取的时间会和真实的本地时间相差8小时，这个需要修改JVM时区，正常很少见
3. 数据库时间没有错，获取到了后端，之后返回给前端相差8小时，可以通过设置json转换的时区来进行调整
4. 修改数据库的时区，可以通过命令临时修改和通过配置文件永久性修改

![image-20230321205540732](https://image.xiaoxiaofeng.site/blog/image/image-20230321205540732.png?xiaoxiaofeng)