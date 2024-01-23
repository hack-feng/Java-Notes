## 1. 背景
我们在使用SpringBoot的时候，有没有注意过Console的日志呢？你们的是什么样的？有没有花里胡哨，有没有重点突出，有没有突出你们项目的特色？

项目发布生产后，你们还有没有关注过生产日志呢？怎么输出的？怎么保存的，有保存在那里的呢？

带着诸多疑问，本文来给你解密，让我们一起看看SpringBoot怎么使用Logback玩转日志的。

## 2. 什么是Logback

Logback 旨在作为流行的 log4j 项目的继承者，是SpringBoot内置的日志处理框架，spring-boot-starter其中包含了spring-boot-starter-logging，该依赖内容就是 Spring Boot 默认的日志框架 logback。具体如下图所示👇

![image-20231225165645718](https://image.xiaoxiaofeng.site/blog/2023/12/25/xxf-20231225165645.png?xxfjava)

![image-20231225165723351](https://image.xiaoxiaofeng.site/blog/2023/12/25/xxf-20231225165723.png?xxfjava)

官方文档：[http://logback.qos.ch/manual/](http://logback.qos.ch/manual/)

## 3. SpringBoot使用logback介绍

在我们启动SpringBoot，发现我们并没有主动去配置过任何和日志打印的相关配置，但是控制台却打印了相关的启动日志；因为SpringBoot为Logback提供了默认的配置文件base.xml，base.xml文件里定义了默认的root输出级别为INFO。系统打印的日志信息如下：

![image-20231225165907265](https://image.xiaoxiaofeng.site/blog/2023/12/25/xxf-20231225165907.png?xxfjava)

我们可以到SpringBoot源码里看一下base.xml具体是如何配置的，如下图所示👇

![image-20231225170145487](https://image.xiaoxiaofeng.site/blog/2023/12/25/xxf-20231225170145.png?xxfjava)

## 4. 自定义logback配置

可以看到默认的配置是非常简单，那么我们可以自定义配置吗？答案当然是肯定的🙈

在resources目录下创建文件`logback-spring.xml`，详细配置如下👇

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
 
<configuration scan="true" scanPeriod="60 seconds">

	<springProperty scope="context" name="spring.application.name" source="spring.application.name"/>
 
	<!-- 定义参数 -->
	<property name="log.lever" value="debug" />
	<property name="log.maxHistory" value="365" />
 	<property name="log.filePath" value="logs"></property>
<!-- 	如果这里不想写死C盘，那么上面的配置，系统会自动在项目所在的盘符创建文件夹 -->
<!--	<property name="log.filePath" value="C:/{spring.application.name}_log"></property>-->
<!-- 	<property name="log.pattern" value="%-12(%d{yyyy-MM-dd HH:mm:ss.SSS}) |-%-5level [%thread] %c [%L] -| %msg%n" /> -->
	<property name="log.pattern" value="%-12(%d{MM-dd HH:mm:ss}) %c [%L] | %msg%n" />
 
	<!-- 控制台设置 -->
	<appender name="consoleAppender" class="ch.qos.logback.core.ConsoleAppender">
	<!--	<encoder>
			<pattern>${log.pattern}</pattern>
		</encoder>-->
		<encoder>
			<!--<pattern>%d %p (%file:%line\)- %m%n</pattern>-->
			<!--格式化输出：%d:表示日期    %thread:表示线程名     %-5level:级别从左显示5个字符宽度  %msg:日志消息    %n:是换行符-->
			<pattern>%boldMagenta(笑小枫控制台-) %red(%d{yyyy-MM-dd HH:mm:ss}) %green([%thread]) %highlight(%-5level) %yellow(%logger) - %cyan(%msg%n)</pattern>
			<charset>UTF-8</charset>
		</encoder>
	</appender>
 
	<!-- DEBUG -->
	<appender name="debugAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
		<!-- 文件路径 -->
		<file>${log.filePath}/${spring.application.name}_debug.log</file>
		<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<!-- 文件名称 -->
			<fileNamePattern>${log.filePath}/debug/${spring.application.name}_debug.%d{yyyy-MM-dd}.log.gz
			</fileNamePattern>
			<!-- 文件最大保存历史数量 -->
			<MaxHistory>${log.maxHistory}</MaxHistory>
		</rollingPolicy>
		<encoder>
            <pattern>${log.pattern}</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>DEBUG</level>
            <onMatch>ACCEPT</onMatch>  
            <onMismatch>DENY</onMismatch>  
        </filter>
	</appender>
	
	<!-- INFO -->
	<appender name="infoAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
		<!-- 文件路径 -->
		<file>${log.filePath}/${spring.application.name}_info.log</file>
		<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<!-- 文件名称 -->
			<fileNamePattern>${log.filePath}/info/${spring.application.name}_info.%d{yyyy-MM-dd}.log.gz
			</fileNamePattern>
			<!-- 文件最大保存历史数量 -->
			<MaxHistory>${log.maxHistory}</MaxHistory>
		</rollingPolicy>
		<encoder>
            <pattern>${log.pattern}</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>  
            <onMismatch>DENY</onMismatch>  
        </filter>
	</appender>
	
	<!-- ERROR -->
	<appender name="errorAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
		<!-- 文件路径 -->
		<file>${log.filePath}/${spring.application.name}_error.log</file>
		<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<!-- 文件名称 -->
			<fileNamePattern>${log.filePath}/error/${spring.application.name}_error.%d{yyyy-MM-dd}.log.gz
			</fileNamePattern>
			<!-- 文件最大保存历史数量 -->
			<MaxHistory>${log.maxHistory}</MaxHistory>
		</rollingPolicy>
		<encoder>
            <pattern>${log.pattern}</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>  
            <onMismatch>DENY</onMismatch>  
        </filter>
	</appender>
<!-- 	如果要查看错误日志，可以把level=info改为level=debug -->
	<root level="info">
		<appender-ref ref="consoleAppender" />
		<appender-ref ref="debugAppender" />
		<appender-ref ref="infoAppender" />
		<appender-ref ref="errorAppender" />
	</root>
</configuration>
~~~

我们在启动一下程序，看下效果👇

![image-20231225170444479](https://image.xiaoxiaofeng.site/blog/2023/12/25/xxf-20231225170444.png?xxfjava)

可见多了很多我们自定的一些配置，包括控制台前缀和打印日志的颜色等～

同时我们还可以在项目目录下看到我们配置的logs日志归档目录文件。下文图就不更新了，借用之前的图吧，影响不大😅😅😅。

![image-20220716214040740](http://file.xiaoxiaofeng.site/blog/image/2022/07/16/20220716214041.png)

模拟抛出一个系统异常，可以看到错误日志信息，同时可以在logs的error日志中看到错误日志，如下面图所示👇

![image-20220716213625922](http://file.xiaoxiaofeng.site/blog/image/2022/07/16/20220716213626.png)

![image-20220716213959066](http://file.xiaoxiaofeng.site/blog/image/2022/07/16/20220716213959.png)

真心喜欢我的这个配色，总是能让我撸代码的时候保持身心愉悦🐾如下所示，从别的篇章捞的截图，爱了爱了💕

![image-20220721173754295](https://s2.loli.net/2022/07/21/uJhkGxz8RAcdT1e.png)

## 5. 如何把日志同步到ES中

很多大公司为了方便日志的查看和检索，都使用ES来处理日志了吧，这里简单的说一下日志如何存储在ES中。

详细步骤可以查看[Spring Cloud + ELK统一日志系统搭建](https://blog.csdn.net/qq_34988304/article/details/100058049)

这里使用Logstash

在pom文件引用

~~~xml
<!--logback日志-->
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>5.2</version>
</dependency>
~~~

在`resources`添加`logbak`的配置文件 `logback-spring.xml`：

这里简化了logbak的配置文件，没有自定义系列，只是简单的同步

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="60 seconds">
    <!-- 定义参数 -->
    <property name="log.pattern" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n" />
    <!-- 控制台打印设置 -->
    <appender name="consoleAppender" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${log.pattern}</pattern>
        </encoder>
    </appender>
    
    <!-- logstash设置 -->
    <appender name="logstash" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
        <param name="Encoding" value="UTF-8"/>
        <!-- logstash服务器ip -->
        <remoteHost>192.168.0.146</remoteHost>
        <!-- logstash tcp 端口-->
        <port>4569</port>
        <!-- <filter class="com.program.interceptor.ELKFilter"/>-->//引入过滤类
        <!-- encoder is required -->
        <encoder charset="UTF-8" class="net.logstash.logback.encoder.LogstashEncoder" >
            <customFields>{"appname":"ceshi"}</customFields> // 索引名
        </encoder>
    </appender>

    <root level="info">
        <appender-ref ref="consoleAppender" />
        <appender-ref ref="logstash"/>
    </root>
</configuration>
~~~

其中`{"appname":"ceshi"}` 对应`logstash`配置文件中的`appname`，为创建的索引名。
可以在Kibana索引管理中根据名称进行分区搜索。根据自己的需求来，这里只做演示。

## 6. logback配置属性详解

在上面我们已经实现了自定义配置logback的打印，接下来我们详细讲解一下对应的属性，方便大家根据自己实际业务去配置。

### 根节点< configuration>

```xml
<configuration scan="true" scanPeriod="60 seconds" debug="false">  
    <!-- 其他配置省略-->  
</configuration>
```

1. scan : 当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true。
2. scanPeriod : 设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒。当scan为true时，此属性生效。默认的时间间隔为1分钟。
3. debug : 当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。

#### 子节点:< property>

```xml
<property name="LOG_FILE_PATH" value="${LOG_FILE:-${LOG_PATH:-${LOG_TEMP:-${java.io.tmpdir:-/tmp}}}/logs}"/>
```

用来定义变量值的标签，< property> 有两个属性，name和value；其中name的值是变量的名称，value的值时变量定义的值,通过定义的值会被插入到logger上下文中。定义变量后，可以使“${}”来使用变量。

**注：多环境配置下，通过 application.yml 传递参数过来，< property >取不到环境参数，得用< springProperty >。**

```xml
<springProperty scope="context" name="APP_NAME" source="spring.application.name" defaultValue="springBoot"/>
```

#### 子节点:< appender>

appender用来格式化日志输出节点，有两个属性name和class，class用来指定哪种输出策略，常用就是控制台输出策略和文件输出策略。

- **ConsoleAppender：**就想他的名字一样，将日志信息打印到控制台上，更加准确的说：使用System.out或者System.err方式输出，主要子标签有：encoder，target
- **FileAppender：**用于将日志信息输出到文件中，主要子标签有：append，encoder，file
- **RollingFileAppender：**从名字我们就可以得出：FileAppender是RollingFileAppender的父类。即RollingFileAppender继承 FIleAppender类。功能：能够动态的创建一个文件。也就是说：到满足一定的条件，就会创建一个新的文 件，然后将日志写入到新的文件中。有两个重要的标签与rolingFileAppender进行交互：**RollingPolicy**，**TriggeringPolicy**，主要子标签：file，append，encoder，rollingPolicy，triggerPolicy

以下分别介绍这些标签：

##### file

被写入的文件名，可以是相对目录，也可以是绝对目录，如果上级目录不存在会自动创建，没有默认值。

##### target

设置一System.out还是System.err方式输出。默认值为System.out

##### append

如果是 true，日志被追加到文件结尾，如果是 false，清空现存文件，默认是true。

##### prudent

- **FileAppender:**如果是 true，日志会被安全的写入文件，即使其他的FileAppender也在向此文件做写入操作，效率低，默认是 false。
- **RollingFileAppender:**当为true时，不支持FixedWindowRollingPolicy。支持TimeBasedRollingPolicy，但是有两个限制，1不支持也不允许文件压缩，2不能设置file属性，必须留空。

##### layout和encoder

```xml
<!--输出到控制台 ConsoleAppender-->
<appender name="consoleLog1" class="ch.qos.logback.core.ConsoleAppender">
    <layout>
        <pattern>${FILE_LOG_PATTERN}</pattern>
    </layout>
</appender>

<!--输出到控制台 ConsoleAppender-->
<appender name="consoleLog2" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>%d -2 %msg%n</pattern>
    </encoder>
</appender>
<!--设置项目日志输出级别为INFO-->
<root level="INFO">
    <appender-ref ref="consoleLog1"/>
    <appender-ref ref="consoleLog2"/>
</root>
```

可以看到appender的子节点layout和encoder都可以输出，都可以将事件转换为格式化后的日志记录，但是控制台输出使用layout，文件输出使用encoder。自从0.9.19版本之后，Fileappender和他的子类是期望使用encoder，不再使用layout。

###### layout和encoder区别

1. encoder：主要工作有两个：①将一个event事件转换成一组byte数组，②将转换后的字节数据输出到文件中。
2. encoder组件是在0.9.19版本之后才引进来的。在以前的版本中，appender是使用layout（将一个event事件转换成一个字符串），然后使用【java.io.writer】对象将字符串写入到文件中。
3. 自从0.9.19版本之后，Fileappender和他的子类是期望使用encoder，不再使用layout。
4. 其中layout仅仅完成了将一个event事件转换成一个字符串这一个功能。此外，layout不能控制将字符串写出到文件。layout不能整合event事件到一组中。与encoder相比，不仅仅能按照格式进行转化，而且还能将数据写入到文件中。

因为layout已经不再推荐使用了，那么这里重点讲一下encoder。

其中patternLayoutEncoder是最常使用encoder，也就是默认的，默认就是PatternLayoutEncoder类，他包含可patternLayout大部分的工作。

###### 几个重要的encoder

1. LayoutWrappingEncoder:（不怎么用）

```
   1.在0.9.19版本之前，都是使用layout来控制输出的格式。那就存在大量的layout接口（自定义）的代码。在0.9.19就变成了使用encoder来控制，如果我们想使用以前的layout怎么办？这个LayoutWrappingEncoder就是为了encoder能够操作内部layout存在的。即这个类在encoder与layout之间提供一个桥梁。这个类实现了encoder类，又包含了layout将evnet事件装换成字符串的功能。
   
   2.原理：使用layout将输入的evnet事件转换成一个字符串，然后将字符串按照用户指定的编码转换成byte数组。最后将byte数据写入到文件中去。
   
   3.在默认的情况下，输出流是立即刷新的。除非immediateFlush属性值为false，就不会立即刷新，但是为提高logger接入量。
   
```

2. PatternLayoutEncoder：常用。他是LayoutWrappingEncoder的子类

```xml
1.考虑到PatternLayout是layout中最常用的组件，所以logback人员开发出了patternLayoutEncoder类，这个类是LayoutWrappingEncoder的扩展，这个类包含了PatternLayout。

2.immediateFlush标签与LayoutWrappingEncoder是一样的。默认值为【true】。这样的话，在已存在的项目就算没有正常情况下的关闭，也能记录所有的日志信息到磁盘上，不会丢失任何日志信息。因为是立即刷新。如果将【immediateFlush】设置为【false】，可能就是五倍的原来的logger接入量。但是可能会丢失日志信息在没有正常关闭项目的情况下。例如：

<appender name="FILE" class="ch.qos.logback.core.FileAppender"> 
  <file>foo.log</file>
  <encoder><!--默认就是PatternLayoutEncoder类-->
    <pattern>%d %-5level [%thread] %logger{0}: %msg%n</pattern>
    <!-- this quadruples logging throughput -->
    <immediateFlush>false</immediateFlush>
  </encoder> 
</appender>

3.如果想在文件的开头打印出日志的格式信息：即打印日志的模式。使用【outputPatternAsHeader】标签，并设置为【true】.默认值为【false】。例如：
<!--输出到控制台 ConsoleAppender-->
<appender name="consoleLog2" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>%d -2 %msg%n</pattern>
        <outputPatternAsHeader>true</outputPatternAsHeader>
    </encoder>
</appender>
他就会在打印开始之前第一句输出日志格式，如：#logback.classic pattern: %d -2 %msg%n

```

patternLayoutEncoder类既有layout将一个事件转化为字符串，又有将字符创写入到文件中的作用。他是encoder标签的默认类实例。

##### filter

- 简介
  logback具有过滤器支持。logbcak允许给日志记录器appender配置一个或多个Filter(或者给整体配置一个或多个TurboFilter)，来控制:当满足过滤器指定的条件时，才记录日志(或不满足条件时，拒绝记录日志)。logback支持自定义过滤器，当然logback也自带了一些常用的过滤器，在绝大多数时候，自带的过滤器其实就够用了，一般是不需要自定义过滤器的。

- logback提供的过滤器支持主要分两大类：

```xml
  ch.qos.logback.core.filter.Filter
  ch.qos.logback.classic.turbo.TurboFilter
```

- Filter与TurboFilter自带的几种常用过滤器

  | 过滤器                 | 来源        | 说明                                                         | 相对常用 |
  | ---------------------- | ----------- | ------------------------------------------------------------ | -------- |
  | LevelFilter            | Filter      | 对指定level的日志进行记录(或不记录)，对不等于指定level的日志不记录(或进行记录) | 是       |
  | ThresholdFilter        | Filter      | 对大于或等于指定level的日志进行记录(或不记录)，对小于指定level的日志不记录(或进行记录) 提示：info级别是大于debug的 | 是       |
  | EvaluatorFilter        | Filter      | 对满足指定表达式的日志进行记录(或不记录)，对不满足指定表达式的日志不作记录(或进行记录) | 是       |
  | MDCFilter              | TurboFilter | 若MDC域中存在指定的key-value，则进行记录，否者不作记录       | 是       |
  | DuplicateMessageFilter | TurboFilter | 根据配置不记录多余的重复的日志                               | 是       |
  | MarkerFilter           | TurboFilter | 针对带有指定标记的日志，进行记录(或不作记录)                 | 否       |
  | …                      | …           | …                                                            | …        |

`TurboFilter的性能是优于Filter的，这是因为TurboFilter的作用时机是在创建日志事件ILoggingEvent对象之前，而Filter的作用时机是在创建之后`。若一个日志注定是会被过滤掉不记录的，那么创建ILoggingEvent对象(包括后续的参数组装方法调用等)这个步骤无疑是非常消耗性能的。

这里主要介绍两种filter

**ThresholdFilter：**

ThresholdFilter为系统定义的拦截器，例如我们用ThresholdFilter来过滤掉ERROR级别以下的日志不输出到文件中。如果不用记得注释掉，不然你控制台会发现没日志

```xml
<filter class="ch.qos.logback.classic.filter.ThresholdFilter">
    <level>ERROR</level>
</filter>
```

**LevelFilter**

如果只是想要 Info 级别的日志，只是过滤 info 还是会输出 Error 日志，因为 Error 的级别高，所以我们使用下面的策略，可以避免输出 Error 的日志

```xml
<filter class="ch.qos.logback.classic.filter.LevelFilter">
    <!--过滤 Error-->
    <level>ERROR</level>
    <!--匹配到就禁止-->
    <onMatch>DENY</onMatch>
    <!--没有匹配到就允许-->
    <onMismatch>ACCEPT</onMismatch>
</filter>
```

FilterReply有三种枚举值：

- `DENY`：表示不用看后面的过滤器了，这里就给拒绝了，不作记录。
- `NEUTRAL`：表示需不需要记录，还需要看后面的过滤器。若所有过滤器返回的全部都是NEUTRAL，那么需要记录日志。
- `ACCEPT`：表示不用看后面的过滤器了，这里就给直接同意了，需要记录。

##### rollingPolicy

- **TimeBasedRollingPolicy:**它根据时间来制定滚动策略.时间滚动策略可以基于时间滚动按时间生成日志。
  下面是官网给出的示例：

~~~xml
  <configuration>
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
      <file>logFile.log</file>
      <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <!-- daily rollover -->
        <fileNamePattern>logFile.%d{yyyy-MM-dd}.log</fileNamePattern>
        <!-- keep 30 days' worth of history capped at 3GB total size -->
        <maxHistory>30</maxHistory>
        <totalSizeCap>3GB</totalSizeCap>
      </rollingPolicy>
  
      <encoder>
        <pattern>%-4relative [%thread] %-5level %logger{35} - %msg%n</pattern>
      </encoder>
    </appender> 
  
    <root level="DEBUG">
      <appender-ref ref="FILE" />
    </root>
  </configuration>
~~~

 紧跟着又给出的多个JVM写同一个日志文件的配置，主要是加一行开启（节俭）prudent模式

~~~xml
  <configuration>
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
      <!-- Support multiple-JVM writing to the same log file -->
      <prudent>true</prudent>
      <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <fileNamePattern>logFile.%d{yyyy-MM-dd}.log</fileNamePattern>
        <maxHistory>30</maxHistory> 
        <totalSizeCap>3GB</totalSizeCap>
      </rollingPolicy>

      <encoder>
        <pattern>%-4relative [%thread] %-5level %logger{35} - %msg%n</pattern>
      </encoder>
    </appender> 

    <root level="DEBUG">
      <appender-ref ref="FILE" />
    </root>
  </configuration>
~~~

* **SizeAndTimeBasedRollingPolicy:**基于大小和时间的滚动策略
  这个策略出现的原因就是对时间滚动策略的一个补充，使其不仅按时间进行生成而且考虑到文件大小的原因，因为在基于时间的滚动策略生成的日志文件，只是对一段时间总的日志大小做了限定，但是没有对每个日志文件的大小做限定，这就会造成个别日志文件过大，后期传递，阅读困难的问题，所以就有了这第二个策略。
  
  下面是官网给的示例：

```xml
<configuration>
  <appender name="ROLLING" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>mylog.txt</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
      <!-- rollover daily -->
      <fileNamePattern>mylog-%d{yyyy-MM-dd}.%i.txt</fileNamePattern>
       <!-- each file should be at most 100MB, keep 60 days worth of history, but at most 20GB -->
       <maxFileSize>100MB</maxFileSize>    
       <maxHistory>60</maxHistory>
       <totalSizeCap>20GB</totalSizeCap>
    </rollingPolicy>
    <encoder>
      <pattern>%msg%n</pattern>
    </encoder>
  </appender>

  <root level="DEBUG">
    <appender-ref ref="ROLLING" />
  </root>

</configuration>
```

* **FixedWindowRollingPolicy：**基于固定窗口的滚动策略
  这个策略的出现，我个人猜测是因为需要日志文件保持为某个特定的数量，防止滚动测策略导致过多的日志文件出现。这个策略出现得配合triggeringPolicy,给一个什么时候日志滚动一次的控制，这部分是跟上面两种策略所不一样的地方。
  下面是官网给出的示例:

```xml
<configuration>
  <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>test.log</file>

    <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
      <fileNamePattern>tests.%i.log.zip</fileNamePattern>
      <minIndex>1</minIndex>
      <maxIndex>3</maxIndex>
    </rollingPolicy>

    <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
      <maxFileSize>5MB</maxFileSize>
    </triggeringPolicy>
    <encoder>
      <pattern>%-4relative [%thread] %-5level %logger{35} - %msg%n</pattern>
    </encoder>
  </appender>
        
  <root level="DEBUG">
    <appender-ref ref="FILE" />
  </root>
</configuration>
```

注意：在RollingFileAppender中有一个file标签，也是设置文件的名称的。file可以设置 也可以不设置。如果你设置了file标签的话，他就不会转换到新的文件中。所有的日志 信息将会输入到同一个文件中。如果file标签没有设置。文件的名称就会在每一个阶段 由filenamePattern计算得出。

**fileNamePattern:**这是一个强制的标签。他的值可以包含：文件的名称、适当的%d转 换说明符。这个%d说明符可以包含一个【日期和时间】的模式。其中【模式】类似于 【SimpleDateFormat】类。如果这个【模式】没有写的话，默认就是【yyyy-MM-dd】的模式。 转换文件的名称从fileNamePattern中得到

**maxHistory：**这是一个可选的标签。以异步方式删除较旧的文件,例如，如果您指定每月滚动，并将maxHistory设置为6，则将保留6个月的归档文件，并删除6个月以上的文件。

**totalSizeCap：**这是一个可选的标签。这是所有日志文件的总大小空间。当日志文件的空间超过了设置的最大 空间数量，就会删除旧的文件。注意：这个标签必须和maxHistory标签一起使用。

**cleanHistoryOnStart:**如果设置为true，则将在追加程序启动时执行归档删除。默认情况下，此属性设置为false。

#### **子节点:< loger>**

用来设置某一个包或者具体的某一个类的日志打印级别、以及指定< appender >。< loger >仅有一个name属性，一个可选的level和一个可选的addtivity属性。

- name:用来指定受此loger约束的某一个包或者具体的某一个类。
- level:用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL 和 OFF，还有一个特俗值INHERITED或者同义词NULL，代表强制执行上级的级别。如果未设置此属性，那么当前loger将会继承上级的级别。
- addtivity:是否向上级loger传递打印信息。默认是true。

**loger在实际使用的时候有两种情况**

- **第一种：**带有loger的配置，不指定级别，不指定appender

```xml
  <logger name="com.xf.controller.TestLogController"/>
```

  > 将TestLogController类的日志的打印，但是并没用设置打印级别，所以继承他的上级的日志级别“info”；没有设置addtivity，默认为true，将此loger的打印信息向上级传递；没有设置appender，此loger本身不打印任何信息。

  < root level=“info”>将root的打印级别设置为“info”，指定了名字为“console”的appender。当执行com.xf.controller.TestLogController类的testLog方法时，所以首先执行< logger name=“com.xf.controller.TestLogController”/>，将级别为“info”及大于“info”的日志信息传递给root，本身并不打印；root接到下级传递的信息，交给已经配置好的名为“console”的appender处理，“console” appender 将信息打印到控制台；

- **第二种：**带有多个loger的配置，指定级别，指定appender

```xml
  <configuration>
      <logger name="com.xf.controller.TestLogController" level="WARN" additivity="false">
          <appender-ref ref="console"/>
      </logger>
  </configuration>
```

> 控制com.xf.controller.TestLogController类的日志打印，打印级别为“WARN”;additivity属性为false，表示此loger的打印信息不再向上级传递;指定了名字为“console”的appender;

这时候执行com.xf.controller.TestLogController类的login方法时，先执行< logger name=“com.xf.controller.TestLogController” level=“WARN” additivity=“false”>,将级别为“WARN”及大于“WARN”的日志信息交给此loger指定的名为“console”的appender处理，在控制台中打出日志，不再向上级root传递打印信息。

**注：当然如果你把additivity="false"改成additivity="true"的话，就会打印两次，因为打印信息向上级传递，logger本身打印一次，root接到后又打印一次。**

#### **子节点:< root >**

root节点是必选节点，用来指定最基础的日志输出级别，只有一个level属性。level默认是DEBUG。

level:用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL 和 OFF，不能设置为INHERITED或者同义词NULL。

可以包含零个或多个元素，标识这个appender将会添加到这个loger。

```xml
<root level="debug">
  <appender-ref ref="console" />
  <appender-ref ref="file" />
</root>
```

#### 多环境配置

`<springProfile>`标签允许你自由的包含或排除基于激活的Spring profiles的配置的一部分。在`<configuration>`元素的任何地方都支持Profile部分。使用`name`属性来指定哪一个profile接受配置。多个profiles可以用一个逗号分隔的列表来指定。

```xml
<springProfile name="staging">
    <!-- configuration to be enabled when the "staging" profile is active -->
</springProfile>

<springProfile name="dev, staging">
    <!-- configuration to be enabled when the "dev" or "staging" profiles are active -->
</springProfile>

<springProfile name="!production">
    <!-- configuration to be enabled when the "production" profile is not active -->
</springProfile>

```

## 7. 小结

好啦，本文就到这里了，我们简单的总结一下，主要介绍了以下内容👇👇

* 简单介绍了logback
* 自定义logback打印配置
* 详解logback配置信息

## 8. 项目源码

本文到此就结束了，如果帮助到你了，帮忙点个赞👍

本文源码：[https://github.com/hack-feng/maple-product/tree/main/maple-flyway](https://github.com/hack-feng/maple-product/tree/main/maple-flyway)

>  🐾我是笑小枫，全网皆可搜的【[笑小枫](https://www.xiaoxiaofeng.com)】

