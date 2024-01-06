## 序

本文主要讲述一下Java11的新特性

## 版本号

```apache
java -version
openjdk version "11" 2018-09-25
OpenJDK Runtime Environment 18.9 (build 11+28)
OpenJDK 64-Bit Server VM 18.9 (build 11+28, mixed mode)
```

- General-Availability Release版本是基于tag为jdk-11+28的版本编译
- 从version信息可以看出是build 11+28

## 特性列表

- [181: Nest-Based Access Control](https://link.segmentfault.com/?enc=pZSWcvmbdRGSH%2FuS8s8IRA%3D%3D.HSHyrfFbnYWh4JRsEDlzYKAFflM5xxP7yYQm9ofZUYgNwQk%2BOH2z5S6JOafKBdWf)

  > 相关解读[Java Nestmate稳步推进](https://link.segmentfault.com/?enc=MoWmpFo8s6FW0dz0fpb3yw%3D%3D.81%2B0kO%2Fs1FRen2xD8wDRMGp8Zb9XFDtcl0v5eBSTcokqrJIWxFlxf7rWPCOKQ0H5)，[Specification for JEP 181: Nest-based Access Control](https://link.segmentfault.com/?enc=DdXqqHkivTDB4tM31heNmg%3D%3D.byAxJWzG8MYTf1xPR%2BpS%2FpBGe0Sk5V2zy6D52m6WgI8ZCUng%2FgHe%2Bzul5ABwb5PG%2Bd%2BhNvhrAPHo8%2B%2FlCfDJVQ%3D%3D)
  > 简单的理解就是Class类新增了getNestHost，getNestMembers方法

- [309: Dynamic Class-File Constants](https://link.segmentfault.com/?enc=WKXjZvJdGnNpWOCVZuwLlg%3D%3D.XSkzLivDz6Vl%2FKrNxxOPIlLW4LRHywsF%2B6vLJQUnH8%2B46Z%2FdPvzZb%2FA%2BRkckNxVa)

  > 相关解读[Specification for JEP 309: Dynamic Class-File Constants (JROSE EDITS)](https://link.segmentfault.com/?enc=nwdz3f%2Fl1lOV%2FBDUb%2F%2F9Cg%3D%3D.rJa%2F8T3ZXSTzP1vLUZ2me8Ro%2Fy0C%2BBbsdFX79UX8Qj873Qk08tpn%2FtZMskwWSCqE42p0mZ3vr%2BnHjNYIMjptMsennhJx343bJZpT3SdBuVc%3D)
  > jvm规范里头对Constant pool新增一类CONSTANT_Dynamic

- [315: Improve Aarch64 Intrinsics](https://link.segmentfault.com/?enc=6Ums81JdrQ42jkPB1j%2FGGA%3D%3D.yERGosEv8B7fe5xM1slZfLuxSk34oHuQ12Q2wTeOf43I%2F%2Ba3sQhKvYEyknA5eIzl)

  > 对于AArch64处理器改进现有的string、array相关函数，并新实现java.lang.Math的sin、cos、log方法

- [318: Epsilon: A No-Op Garbage Collector](https://link.segmentfault.com/?enc=8NikHLbyLDcM40ZCDKFQbg%3D%3D.o3Tzporld9S7k3RiZ8yhd%2FWvREmNq1QOE%2BdL5tXyH%2BmPL8HVKRJCSA%2FAq3LLUS7X)

  > 引入名为Epsilon的垃圾收集器，该收集器不做任何垃圾回收，可用于性能测试、短生命周期的任务等，使用-XX:+UseEpsilonGC开启

- [320: Remove the Java EE and CORBA Modules](https://link.segmentfault.com/?enc=5EQLwkIb40FlVTh7Y%2FigKQ%3D%3D.DqJgVGb2jYeu9ombzq9jM194GJ1zh7dWSjEVNWE%2FxVYVQKtCx9IbFXQCU2J9E5G2)(`重磅`)

  > 将java9标记废弃的Java EE及CORBA模块移除掉，具体如下：（1）xml相关的，java.xml.ws, java.xml.bind，java.xml.ws，java.xml.ws.annotation，jdk.xml.bind，jdk.xml.ws被移除，只剩下java.xml，java.xml.crypto,jdk.xml.dom这几个模块；（2）java.corba，java.se.ee，java.activation，java.transaction被移除，但是java11新增一个java.transaction.xa模块

- [321: HTTP Client (Standard)](https://link.segmentfault.com/?enc=likGr4mhlKGjUZw0yWSDWg%3D%3D.ZL8vBPNiqTP4dy%2BdxSSqfl8t8UsAVEf9VXiX7SySWMhZQryngsvqTwm01YGF%2B6xZ)(`重磅`)

  > 相关解读[java9系列(六)HTTP/2 Client (Incubator)](https://segmentfault.com/a/1190000013518969)，[HTTP Client Examples and Recipes](https://link.segmentfault.com/?enc=bY%2F94hJ1k7J%2FFcyGyJcdJw%3D%3D.NZfAqfO%2F%2BW72qeotTqeItDb%2BpNtumRRg0z6CaRkyrXlUC1wROj2zH0B%2BTwIcrg4grS6WcsAasW4Ma2GPC2%2FOPQ%3D%3D)，在java9及10被标记incubator的模块jdk.incubator.httpclient，在java11被标记为正式，改为java.net.http模块。

- [323: Local-Variable Syntax for Lambda Parameters](https://link.segmentfault.com/?enc=HRVPxy4%2FgLWnoxpfHWXhhw%3D%3D.5HCf3PzosUMv1vwadvcLNB%2F0uwfbpxTDqLAABmtWb9Al%2Fi4kZ9SBR%2Fhu6Fzm35cV)

  > 相关解读[New Java 11 Language Feature: Local-Variable Type Inference (var) extended to Lambda Expression Parameters](https://link.segmentfault.com/?enc=V7A3brVcGSDXyWxs%2FOCTTQ%3D%3D.h23gyeMT48mrFSgfPPUeLvw9xeNv55MBN0wSX3Cj5dP4ers7SkhbLyZ4EY1tc9twPMhrstKjCHKxwk%2FRyFGbC8zs3hJuQMrbUanndYdzS%2FTXwrunN0gUdUDJExjnAJwdNXCsKY4jCUxlNONHj%2Bw0piRvRNqhGobHKkB%2F%2BMRzh4XuqD9Ijrb20kPNqKOckHF4)
  > 允许lambda表达式使用var变量，比如(var x, var y) -> x.process(y)，如果仅仅是这样写，倒是无法看出写var有什么优势而且反而觉得有点多此一举，但是如果要给lambda表达式变量标注注解的话，那么这个时候var的作用就突显出来了(@Nonnull var x, @Nullable var y) -> x.process(y)

- [324: Key Agreement with Curve25519 and Curve448](https://link.segmentfault.com/?enc=uXjfB9tfpuACh6eZTiWgdg%3D%3D.sia46vZfdm8qwrqA7gwO9fi%2BQMHC8DmIIAzqJRiBYQOB8LY9SAX4BxJrBnkJxlca)

  > 使用RFC 7748中描述的Curve25519和Curve448实现key agreement

- [327: Unicode 10](https://link.segmentfault.com/?enc=i%2BNkS%2FkyJOZ2QKFezvU5Mg%3D%3D.ZoHmrbkRVtTzfMylj%2BWySvQLooxcYTYVWTS8G%2B2a2iYCdeR4rcsD9Q8AeX0VAmGb)

  > 升级现有的API，支持Unicode10.0.0

- [328: Flight Recorder](https://link.segmentfault.com/?enc=hi5UMfpWebwIc16u1zHOaQ%3D%3D.veEQ6UNqxbYZjNlp%2Fta7%2BiFq4BEog4BtaWfxEzz0IuH4tCTKQZWKJUImsRViERwL)

  > 相关解读[Java 11 Features: Java Flight Recorder](https://link.segmentfault.com/?enc=7NMULQzsuZCmYFYlbotu8Q%3D%3D.T8fUaq2Dx%2FyCd%2BunCUD59B0Nmt2SOeBg5jVlJYVzHyx3jnDpT9XFA3pvujmupDep6aqVF9%2Bc0oj%2FoEUeWmF8z8zZn%2BH1eJ%2FtX54%2BIUyAKao%3D)
  > Flight Recorder以前是商业版的特性，在java11当中开源出来，它可以导出事件到文件中，之后可以用Java Mission Control来分析。可以在应用启动时配置java -XX:StartFlightRecording，或者在应用启动之后，使用jcmd来录制，比如

  ```mipsasm
  $ jcmd <pid> JFR.start
  $ jcmd <pid> JFR.dump filename=recording.jfr
  $ jcmd <pid> JFR.stop
  ```

- [329: ChaCha20 and Poly1305 Cryptographic Algorithms](https://link.segmentfault.com/?enc=q11E43FALaEC3A7dzKXcNQ%3D%3D.hqpdICZeCJjU2ZcHfdgXjR%2BSc1HG4q3534T3AFIG5SrCl41WFkSG3TQrFBbFQqGJ)

  > 实现 RFC 7539的ChaCha20 and ChaCha20-Poly1305加密算法

- [330: Launch Single-File Source-Code Programs](https://link.segmentfault.com/?enc=X6NnqB4o%2BsnJsIZ9Mt5MGw%3D%3D.m66082hDhqgRhUzGwbPhUTB%2BbpzYDtw93ANPipwh0GjhLVU1OqpA4C5DOigs8VdU)(`重磅`)

  > 相关解读[Launch Single-File Source-Code Programs in JDK 11](https://link.segmentfault.com/?enc=TyqnWKnzL%2BkqIYWiIyAxfg%3D%3D.lCBULHUdIi%2BXv7da745ml8Qv%2BwVYnG%2FsKlo1%2Fdb1ZvPGKDvKs4yyXDcMjpJ%2BNMHUN6u877QIR%2FbvrFr8EzSb4rz7L3NQ%2BNsiyluAvmR29Ec%3D)
  > 有了这个特性，可以直接java HelloWorld.java来执行java文件了，无需先javac编译为class文件然后再java执行class文件，两步合成一步

- [331: Low-Overhead Heap Profiling](https://link.segmentfault.com/?enc=%2BhAyRqvRUV29%2Fsu5EDj4lA%3D%3D.nhXRIBpcyhlB7GNsbbiB9otMMf27FHcHkF1s32a2hYoiyXwDeAy%2BDiWNFok%2BPdl%2F)

  > 通过JVMTI的SampledObjectAlloc回调提供了一个开销低的heap分析方式

- [332: Transport Layer Security (TLS) 1.3](https://link.segmentfault.com/?enc=qtNUYV8RbU0DECzKxERELw%3D%3D.xN%2BV63F40oUJcm6ZQ32rfg9Y3zAMo3tQDy8NJ%2BRFEjv62VwnCF1tUZ0jBqOc%2BCw%2F)(`重磅`)

  > 支持RFC 8446中的TLS 1.3版本

- [333: ZGC: A Scalable Low-Latency Garbage Collector(Experimental)](https://link.segmentfault.com/?enc=hZ%2BhBkaUH9hbsYmASRPpcw%3D%3D.bif9A59fUERjuYcRmcOCpjhu9SvynfbqL1JvP887ZIhnHxRMdQlz%2F5o37vsM1mgs)(`重磅`)

  > 相关解读[JDK11的ZGC小试牛刀](https://segmentfault.com/a/1190000015725327)，[一文读懂Java 11的ZGC为何如此高效](https://link.segmentfault.com/?enc=Ul%2BnPan14weXYC%2FoEQ6sEQ%3D%3D.ZARiFcE3M%2BgGm66WfC%2BFkGk6DCrUsv5tM6ltsdC0kMqADTPQmn5ifnUYg056tW47oUaSOUjr4Rm%2BZ3ly4aHCaw%3D%3D)

- [335: Deprecate the Nashorn JavaScript Engine](https://link.segmentfault.com/?enc=1PcxUo3FXlSmPYQH2tet%2Bg%3D%3D.wPLD0uwzY67csHqGc0x06h7u3YMq2FQuygTKDbGd0sYfxGjXP6Ozkotho9rWot96)

  > 相关解读[Oracle弃用Nashorn JavaScript引擎](https://link.segmentfault.com/?enc=CMLJhZCMV9SX8%2BvB52gtKw%3D%3D.f%2FprYrBI4wLV4RTMf0fXUZ6GvB0EKCOaKfhJkUbnd1vjNTlU0OdtZ6nkltW%2BHD9C4y4JRDU1jpnprZ2O30%2BRAg%3D%3D)，[Oracle GraalVM announces support for Nashorn migration](https://link.segmentfault.com/?enc=8nYOG8yk%2FiwbeiGA76UNog%3D%3D.XQ27XUeyn1cHdm%2BBYIW64twE%2FlcndCuuY5GvzOVlDRj1LpTMAWl4JngNqkRa2%2FqEY4NNYAfZH5gsIny9%2BPK2XqEmMyTG%2BbbmaG58QR8yLWQhfZ0ISap1s%2F%2FTeXtx8IJO)
  > 废除Nashorn javascript引擎，在后续版本准备移除掉，有需要的可以考虑使用GraalVM

- [336: Deprecate the Pack200 Tools and API](https://link.segmentfault.com/?enc=7X5iltALetA35mgrbeKvbA%3D%3D.rfpHgsnmjMZV9VlkORVXsFm%2F8Cw8rVjuTDg4PIUazwcUl44QrHx%2BFnVn30sx1g%2Bq)

  > 废除了pack200以及unpack200工具以及java.util.jar中的Pack200 API。Pack200主要是用来压缩jar包的工具，不过由于网络下载速度的提升以及java9引入模块化系统之后不再依赖Pack200，因此这个版本将其移除掉。

## 细项解读

上面列出的是大方面的特性，除此之外还有一些api的更新及废弃，主要见[What's New in JDK 11 - New Features and Enhancements](https://link.segmentfault.com/?enc=T7spL%2BcYywMWRAVdK4cFUA%3D%3D.rnHAKvc6A9PvFY9KXH9Kb6S%2FNZS6Z2KsGA5bIfGnmPdKKfRWjDte9zWa%2FZEGm6uqWWwUj%2F%2BG4jPLavmf3j7RML0cF3hrjhWl%2BBsPtZNG6MEn2q%2BYOiejA6DJ7%2FTg7wBu)以及[90 New Features (and APIs) in JDK 11](https://link.segmentfault.com/?enc=JJeCVY9JtturET9rHDFPCw%3D%3D.Km3ammjEp%2FFLtTzx6%2BM8qE4vT0JzWp2YVVUsTohTbruz08B1azPIfdg5n0pHA1siLnOTKU1M1VSCKWBCMDfoVA%3D%3D)，这里举几个例子。

### 添加项

- Collection.toArray(IntFunction)

  ```reasonml
    @Test
    public void testCollectionToArray(){
        Set<String> names = Set.of("Fred", "Wilma", "Barney", "Betty");
        String[] copy = new String[names.size()];
        names.toArray(copy);
        System.out.println(Arrays.toString(copy));
        System.out.println(Arrays.toString(names.toArray(String[]::new)));
    }
  ```

  > Collection类新增toArray(IntFunction)的default方法，可以直接通过传入IntFunction告知要转换的目标类型

- String.strip

  ```reasonml
    @Test
    public void testStrip(){
        String text = "  \u2000a  b  ";
        Assert.assertEquals("a  b",text.strip());
        Assert.assertEquals("\u2000a  b",text.trim());
        Assert.assertEquals("a  b  ",text.stripLeading());
        Assert.assertEquals("  \u2000a  b",text.stripTrailing());
    }
  ```

  > java11对String类新增了strip，stripLeading以及stripTrailing方法，除了strip相关的方法还新增了isBlank、lines、repeat(int)等方法

- 添加了Google Trust Services GlobalSign Root Certificates

- 添加了GoDaddy Root Certificates

- 添加了T-Systems, GlobalSign and Starfield Services Root Certificates

- 添加了Entrust Root Certificates

### 移除项

- 移除了com.sun.awt.AWTUtilities
- 移除了sun.misc.Unsafe.defineClass，使用java.lang.invoke.MethodHandles.Lookup.defineClass来替代
- 移除了Thread.destroy()以及 Thread.stop(Throwable)方法
- 移除了sun.nio.ch.disableSystemWideOverlappingFileLockCheck、sun.locale.formatasdefault属性
- 移除了jdk.snmp模块
- 移除了javafx，openjdk估计是从java10版本就移除了，oracle jdk10还尚未移除javafx，而java11版本则oracle的jdk版本也移除了javafx
- 移除了Java Mission Control，从JDK中移除之后，需要自己单独下载
- 移除了这些Root Certificates ：Baltimore Cybertrust Code Signing CA，SECOM ，AOL and Swisscom

### 废弃项

- 废弃了Nashorn JavaScript Engine
- 废弃了-XX+AggressiveOpts选项
- -XX:+UnlockCommercialFeatures以及-XX:+LogCommercialFeatures选项也不再需要
- 废弃了Pack200工具及其API

## 小结

- java11是java改为6个月发布一版的策略之后的第一个LTS(`Long-Term Support`)版本(`oracle版本才有LTS`)，这个版本最主要的特性是：在模块方面移除Java EE以及CORBA模块，在JVM方面引入了实验性的ZGC，在API方面正式提供了HttpClient类。
- 从java11版本开始，不再单独发布JRE或者Server JRE版本了，有需要的可以自己通过jlink去定制runtime image

## doc

- [JDK11](https://link.segmentfault.com/?enc=ByG8XPHiAhXFT%2BDEk%2BpoAA%3D%3D.nv2KXyOf2TgS8mdRjIv8pa5IR8BAkE%2F2KvjmTJWKycg%3D)
- [JDK11 Features](https://link.segmentfault.com/?enc=TDQo3zb7eeqoyPqSv5NZgQ%3D%3D.LFnGQkFmoBm63v26D5KLbDlFSMFiwh3p%2F%2BpFFiWdI6%2Fezha1URfQQxyQ91v8jV6a)
- [Introducing Java SE 11](https://link.segmentfault.com/?enc=5iv4i19XZ2VM7jQlxVuk0w%3D%3D.E1JJQo1I0S6zPAQSrXjydvypfQAowhxfOY7NAP6GShT5Nr5fwYC%2BZT96sq2KJ%2FOflPBsZ%2FWl76C1%2B5Jag%2BG0ORQ%2FxzN4e%2BE2Y3CqTtf1eBQ%3D)(`官方解读`)
- [JDK 11 Release Notes](https://link.segmentfault.com/?enc=jUpzaNFULy6WR6DLaSncZg%3D%3D.F0BLSdyyuxTKy8biSng70og2mPGA5zQxKidZK3v1rEGh3ql3%2BX7oXoFdJspXJhgaT6FGvC%2FuL0QQaROXStxv17c5%2FoLKEsbxYpYfhksRdTk%3D)(`官方细项解读`)