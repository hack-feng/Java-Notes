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

