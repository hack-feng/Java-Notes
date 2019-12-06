### mysql版本驱动
~~~
url: jdbc:mysql://localhost:3306/cloud-ida?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
~~~

mysql5.7:
~~~xml
<!-- pom -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.41</version>
</dependency>

<!-- properties   driverClassName: com.mysql.jdbc.Driver-->
~~~

mysql8.0:
~~~xml
<!-- pom -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.16</version>
</dependency>

<!-- properties   driverClassName: com.mysql.cj.jdbc.Driver -->
~~~

### 执行sql文件报`Data too long for column`错误
在sql文件的头上添加`/*!40101 SET NAMES utf8 */;

### 解决this is incompatible with sql_mode=only_full_group_by问题

~~~
       一、原理层面

       这个错误发生在mysql 5.7 版本及以上版本会出现的问题：

       mysql 5.7版本默认的sql配置是:sql_mode="ONLY_FULL_GROUP_BY"，这个配置严格执行了"SQL92标准"。

       很多从5.6升级到5.7时，为了语法兼容，大部分都会选择调整sql_mode，使其保持跟5.6一致，为了尽量兼容程序。

        

        二、sql层面

        在sql执行时，出现该原因：

        简单来说就是：输出的结果是叫target list，就是select后面跟着的字段，还有一个地方group by column，就是

        group by后面跟着的字段。由于开启了ONLY_FULL_GROUP_BY的设置，所以如果一个字段没有在target list 

        和group by字段中同时出现，或者是聚合函数的值的话，那么这条sql查询是被mysql认为非法的，会报错误。
~~~

> #### 一、查看sql_mode的语句如下
~~~
select @@GLOBAL.sql_mode;
~~~

> #### 二、解决方案-(推荐解决方案二)

* 解决方案一：sql语句暂时性修改sql_mode
 ~~~
 set @@GLOBAL.sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION
 ~~~
 
>问题：

>重启mysql数据库服务之后，ONLY_FULL_GROUP_BY还会出现。

* 解决方案二：永久解决方案。

>需修改mysql配置文件，通过手动添加sql_mode的方式强制指定不需要ONLY_FULL_GROUP_BY属性，my.cnf位于etc文件夹下，vim下光标移到最后，添加如下：
~~~
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION
~~~

重启mysql服务，顺利解决。