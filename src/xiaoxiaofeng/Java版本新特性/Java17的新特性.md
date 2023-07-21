## 序

本文主要讲述一下Java17的新特性

## 版本号

```apache
java -version
openjdk version "17" 2021-09-14
OpenJDK Runtime Environment (build 17+35-2724)
OpenJDK 64-Bit Server VM (build 17+35-2724, mixed mode, sharing)
```

> 从version信息可以看出是build 17+35

## 特性列表

### [JEP 306: Restore Always-Strict Floating-Point Semantics](https://link.segmentfault.com/?enc=til3jwZtZXl1OQe8uxOW9Q%3D%3D.44YII8S8yu1iPUoWxVsqTRTKDO3qKvYqq78wprrsFs8cx%2B3GpajHvOXkN4FIYkNl)

> 恢复始终执行严格模式的浮点定义，修复25年前英特尔的浮点指令存在的一些问题

### [JEP 356: Enhanced Pseudo-Random Number Generators](https://link.segmentfault.com/?enc=7ZSzzFnEGR3iuo6EIbvtlQ%3D%3D.nCDWh99C3Y8x7kQPKqlRVqiOdp6oc4J9aoSCtnKwc%2FH2EW%2FqGYftVEzwX5BwW6WS)

> 引入RandomGenerator及RandomGeneratorFactory提供更好的随机数生成

```jboss-cli
RandomGenerator generator = RandomGeneratorFactory.all()
    .filter(RandomGeneratorFactory::isJumpable)
    .filter(factory -> factory.stateBits() > 128)
    .findAny()
    .map(RandomGeneratorFactory::create)
//  if you need a `JumpableGenerator`:
//  .map(JumpableGenerator.class::cast)
    .orElseThrow();
```

### [JEP 382: New macOS Rendering Pipeline](https://link.segmentfault.com/?enc=%2BoiZNx3m3%2BdY%2Feipo%2Fa03Q%3D%3D.makW3kxH%2FikohTsd%2BwlQVnVTxcoIEbxGJ%2FKGH9ihG3histKvb%2BxMRHn3FDB8IeF8)

> 使用Apple Metal API为macOS新增了Java 2D internal rendering pipeline

### [JEP 391: macOS/AArch64 Port](https://link.segmentfault.com/?enc=6xuOglB%2FXEZWLy8oCPs8WQ%3D%3D.0yY3gDrrOXLM%2BEaJHnXnUzqk3nZvMmcRc%2BTBJh%2BR%2FHeKkh6N3zyNASvyXWyRHsy1)

> 迁移JDK到macOS/AArch64

### [JEP 398: Deprecate the Applet API for Removal](https://link.segmentfault.com/?enc=KWQeWMe8JQ7DvXfiGbgGvA%3D%3D.MIoAfJJwBFGq7Lzu7NSOkPJZ%2BzePJXU8VrbPGQ4pYhNaDJLOSnmkPN2HYgNwO1Wa)

> 标记Applet API为废弃方便后续移除，具体如下

```stylus
java.applet.Applet
java.applet.AppletStub
java.applet.AppletContext
java.applet.AudioClip
javax.swing.JApplet
java.beans.AppletInitializer
```

### [JEP 403: Strongly Encapsulate JDK Internals](https://link.segmentfault.com/?enc=yRrOkdIhx0gcOc%2B77G4Cjw%3D%3D.FJdVsGd9ZZwbetHwYIwEQeFMuZWNMlOnQxOPefLt4%2ByaVAfDgeChh0fULNTomUtC)

> 对JDK内部的api进行更强的封装，是[JEP 396: Strongly Encapsulate JDK Internals by Default](https://link.segmentfault.com/?enc=CM2FWIZ7TMCyw%2Fc2fvKewQ%3D%3D.3kbOEWSMaxzEX1hGmFjAxq%2BYHz3xtIR%2FiRH2zIrNeWMGt46OP6nXtDJL1lhuPC21)的后续

### [JEP 406: Pattern Matching for switch (Preview)](https://link.segmentfault.com/?enc=ymRm%2F%2F76bVWxNvq8V8H6Gg%3D%3D.wNL7VXWvw4MAUtFxj%2B25glMkF6BHd3wApgfzcMRGIWjVI4pWTRIlx%2FEdHNSqY0go)

> 引入switch模式匹配的preview版本，instanceof的模式匹配在JDK14作为preview，在JDK15作为第二轮的preview，在JDK16转正

```livescript
static String formatterPatternSwitch(Object o) {
    return switch (o) {
        case Integer i -> String.format("int %d", i);
        case Long l    -> String.format("long %d", l);
        case Double d  -> String.format("double %f", d);
        case String s  -> String.format("String %s", s);
        default        -> o.toString();
    };
}
```

### [JEP 407: Remove RMI Activation](https://link.segmentfault.com/?enc=cCHSMT2%2BkLGITXNZ1gUXIQ%3D%3D.GxMKUluK%2FZNy%2F%2F9uTuQM8c6aK1as%2F%2BRvVxh4qssC3yuGeYlPwEazUmnJk0Y4fRO9)

> 移除Remote Method Invocation (RMI)，它在JDK15的[JEP 385](https://link.segmentfault.com/?enc=%2BhFtzWOdD5AKLUWxxMM6dw%3D%3D.P7WwVRbus7KTp61Sv6662EjI%2B6ab%2By9ANIDpL7sBcm6E7BLJV4TJqEKgO03wQ3qC)被废弃

### [JEP 409: Sealed Classes](https://link.segmentfault.com/?enc=2dkU0%2FC8FKSIMtJuGT%2BouA%3D%3D.kSsODJG4JRnSLFHoicroKpA2tNMc2R9eWROVLzfNOn2DQyUsMU1bZVDUGt5UQZBO)

> Sealed Classes在JDK15作为preview引入，在JDK16作为第二轮preview，在JDK17转正

```scala
package com.example.geometry;

public abstract sealed class Shape
    permits Circle, Rectangle, Square, WeirdShape { ... }

public final class Circle extends Shape { ... }

public sealed class Rectangle extends Shape 
    permits TransparentRectangle, FilledRectangle { ... }
public final class TransparentRectangle extends Rectangle { ... }
public final class FilledRectangle extends Rectangle { ... }

public final class Square extends Shape { ... }

public non-sealed class WeirdShape extends Shape { ... }
```

### [JEP 410: Remove the Experimental AOT and JIT Compiler](https://link.segmentfault.com/?enc=Ugo8R3dpcVa5u%2Fp0wqLKxw%3D%3D.OnasILgBTqk%2F%2B91qrGFcL3OSzgPvMRLGoUTYeJ%2BS%2BPr2yjpj3yVNOvlE8HNsHgRd)

> 移除实验性的java版本的AOT及JIT Compiler，具体移除

```stylus
jdk.aot — the jaotc tool
jdk.internal.vm.compiler — the Graal compiler
jdk.internal.vm.compiler.management — Graal's MBean
```

后续要使用可以使用GraalVM

### [JEP 411: Deprecate the Security Manager for Removal](https://link.segmentfault.com/?enc=sBiE88nyWM8YjbhQGeRLjg%3D%3D.YQYjwJdjE5evlbzfhaA4vOj31tkLyIk3HcqHnMg4GWKWPhYZqbg6Uru27h%2BQWthz)

> 废弃java1.0引入的Security Manager方便后续移除

### [JEP 412: Foreign Function & Memory API (Incubator)](https://link.segmentfault.com/?enc=RbUX%2B6EFN7kUJaGKIfAIcg%3D%3D.9jZpeYHCsxgtdMU0sG9V8xwxCGchNA2hsQ2U8RdKgIcJOVmKV%2BxSemhXHDufvaMa)

> JDK14的[JEP 370: Foreign-Memory Access API (Incubator)](https://link.segmentfault.com/?enc=t%2BIZ51rW2XtDT2BVZNlLdA%3D%3D.kjKLhTryjVAQai1ylaq9zrxi%2B0ghRdlig%2B2ErtKQbCWV6SvxnQcj2iDj5lXBfiqw)引入了Foreign-Memory Access API作为incubator，JDK15的[JEP 383: Foreign-Memory Access API (Second Incubator)](https://link.segmentfault.com/?enc=ZSPsK17Y6h%2F0iaulJJ%2FlfA%3D%3D.HKIkKStUkPRx7KKOMuMdMKq0sM%2BAbBAgQTWARIZGsK42s2xkTabGfqN2klAJx8xU)Foreign-Memory Access API作为第二轮incubator，JDK16的[JEP 393: Foreign-Memory Access API (Third Incubator)](https://link.segmentfault.com/?enc=FgJgYvjzAYVgtOoUT565bw%3D%3D.qj1VyHzHq%2FUG39D8vPOMsjSFy27vPrM0i%2ByO3xcrIJ1bQmrGNrhD605IOO1mg0YT)作为第三轮，它引入了Foreign Linker API，JDK17引入Foreign Function & Memory API

### [JEP 414: Vector API (Second Incubator)](https://link.segmentfault.com/?enc=%2F1ZZAbZ4lrDsjgSUf99lOA%3D%3D.m3suXen%2BkPtViTkO2mf5WK5UzrlWLr2lQDbj2j18NURp418STADa7FOXCUHLA0hq)

> JDK16引入了[JEP 338: Vector API (Incubator)](https://link.segmentfault.com/?enc=VOoVWmAAp9%2BMFRL5%2FCtVDg%3D%3D.KWtJ1ngIFwnY9sV0bBGyBmYhq9wuSMIcOv1ry4Rj2tVRwAVuQxePy%2BpDfynbkFA1)提供了jdk.incubator.vector来用于矢量计算，JDK17进行改进并作为第二轮的incubator

### [JEP 415: Context-Specific Deserialization Filters](https://link.segmentfault.com/?enc=0hN2dKNM0gYPMJ16qo%2BRaQ%3D%3D.5grt8Y61CzUfm2NzbqS1glfUi9%2B8HjZLQHc9e4vmkuWOrv9uoPHDk1%2BP2O1y65v6)

> 允许应用去配置指定上下文及动态选择的deserialization filters，示例

```axapta
public class FilterInThread implements BinaryOperator<ObjectInputFilter> {

    // ThreadLocal to hold the serial filter to be applied
    private final ThreadLocal<ObjectInputFilter> filterThreadLocal = new ThreadLocal<>();

    // Construct a FilterInThread deserialization filter factory.
    public FilterInThread() {}

    /**
     * The filter factory, which is invoked every time a new ObjectInputStream
     * is created.  If a per-stream filter is already set then it returns a
     * filter that combines the results of invoking each filter.
     *
     * @param curr the current filter on the stream
     * @param next a per stream filter
     * @return the selected filter
     */
    public ObjectInputFilter apply(ObjectInputFilter curr, ObjectInputFilter next) {
        if (curr == null) {
            // Called from the OIS constructor or perhaps OIS.setObjectInputFilter with no current filter
            var filter = filterThreadLocal.get();
            if (filter != null) {
                // Prepend a filter to assert that all classes have been Allowed or Rejected
                filter = ObjectInputFilter.Config.rejectUndecidedClass(filter);
            }
            if (next != null) {
                // Prepend the next filter to the thread filter, if any
                // Initially this is the static JVM-wide filter passed from the OIS constructor
                // Append the filter to reject all UNDECIDED results
                filter = ObjectInputFilter.Config.merge(next, filter);
                filter = ObjectInputFilter.Config.rejectUndecidedClass(filter);
            }
            return filter;
        } else {
            // Called from OIS.setObjectInputFilter with a current filter and a stream-specific filter.
            // The curr filter already incorporates the thread filter and static JVM-wide filter
            // and rejection of undecided classes
            // If there is a stream-specific filter prepend it and a filter to recheck for undecided
            if (next != null) {
                next = ObjectInputFilter.Config.merge(next, curr);
                next = ObjectInputFilter.Config.rejectUndecidedClass(next);
                return next;
            }
            return curr;
        }
    }

    /**
     * Apply the filter and invoke the runnable.
     *
     * @param filter the serial filter to apply to every deserialization in the thread
     * @param runnable a Runnable to invoke
     */
    public void doWithSerialFilter(ObjectInputFilter filter, Runnable runnable) {
        var prevFilter = filterThreadLocal.get();
        try {
            filterThreadLocal.set(filter);
            runnable.run();
        } finally {
            filterThreadLocal.set(prevFilter);
        }
    }
}

// Create a FilterInThread filter factory and set
    var filterInThread = new FilterInThread();
    ObjectInputFilter.Config.setSerialFilterFactory(filterInThread);

    // Create a filter to allow example.* classes and reject all others
    var filter = ObjectInputFilter.Config.createFilter("example.*;java.base/*;!*");
    filterInThread.doWithSerialFilter(filter, () -> {
          byte[] bytes = ...;
          var o = deserializeObject(bytes);
    });
```

## 细项解读

上面列出的是大方面的特性，除此之外还有一些api的更新及废弃，主要见[JDK 17 Release Notes](https://link.segmentfault.com/?enc=6Nw9seV%2B04NW3%2BDYpj8JSw%3D%3D.Ao%2B6LP9bZ9MhprOICrjKdDCSRfV9JK1uXW75dtFWXAhaHkSXqJD9IcYH1sboHqpv)，这里举几个例子。

### 添加项

- DatagramSocket Can Be Used Directly to Join Multicast Groups ([JDK-8237352](https://link.segmentfault.com/?enc=fsna6xmllESTu1WIxiEz4Q%3D%3D.eP25PVMigYrdqukzKbNukRr6Ut2vgksLMMp59HY4PZuXhXOZHAYAI%2FCTjmq86WkXPeA382ixXB0oHx562umdNg%3D%3D))

  > 更新了java.net.DatagramSocket用于支持joining multicast groups

- Console Charset API ([JDK-8264208](https://link.segmentfault.com/?enc=PA0s7zQO4qexuOUqe8Zi1w%3D%3D.u0eeuP29%2FaebCHBBh3LTRBv50lKHlgfWiG5ZpQfJU8KPvac9Zwbq8ZZyOq%2F%2FnWZx4uY6Oo7UO16SiWbiRsmNEA%3D%3D))

  > java.io.Console新增了方法用于返回console的charset

- JDK Flight Recorder Event for Deserialization ([JDK-8261160](https://link.segmentfault.com/?enc=0Vqhi1yQk23HuApzPrBs9w%3D%3D.syFVpNtYMACJz07cfgfuFFY5OMmXz7pBRG8X1XAF53HUuSFXc1hQjz1%2FC%2FPgwYYS4hFUHd8ptB5uqgExDR4IIg%3D%3D))

  > JDK Flight Recorder新增了jfr.Deserialization实现

- Unified Logging Supports Asynchronous Log Flushing ([JDK-8229517](https://link.segmentfault.com/?enc=%2FnMTrG5%2Ft9AbJzsn4v%2FGow%3D%3D.baPS1fCR3hVq6an3o9HJuoqhYdIYHHfXJP6WrMkH9TgbkVwfHYuVftBr2BpLgdjvBbMgDf3vW%2F988scTodz%2BHw%3D%3D))

  > 引入了`-Xlog:async`参数用于异步日志，以及`-XX:AsyncLogBufferSize=<bytes>`用于控制buffer的大小

### 移除项

- Removal of sun.misc.Unsafe::defineAnonymousClass ([JDK-8243287](https://link.segmentfault.com/?enc=i96BxoJSZZCiPVUsCTWa8g%3D%3D.BY2Md%2F4km0Mgg9fPAz3y1m1RbWRMGqCDCtB%2BQ91qlANmV%2BRjo6UFvb4IXlXLKG1xys1Fv%2FWEDS8C1ElOIs%2BAmA%3D%3D))

  > 移除sun.misc.Unsafe::defineAnonymousClass方法

### 废弃项

- Deprecate 3DES and RC4 in Kerberos ([JDK-8139348](https://link.segmentfault.com/?enc=CUpszAvS1rF11BqmL0VczQ%3D%3D.ULDtIy%2BIk0WRSBumkZUTzmy64qkS%2FDv3g6xxOPml49kacek4%2B4Y3%2FqqBXOszCGdGGcEiNG2bdpxhKyhgaCx8nw%3D%3D))

  > 废弃了Kerberos的des3-hmac-sha1及rc4-hmac这两个encryption types

- Deprecate the Socket Implementation Factory Mechanism ([JDK-8235139](https://link.segmentfault.com/?enc=3BnCf6GhNJDcgN45M6VXCQ%3D%3D.wk9x3sJcv8Hkh%2F%2FVmf6%2FQ2GtBEv1gSFOdzhp0IFilHbaX3vNhIoGa3q%2F4U4H9siPcVLOadXeWxet2TqXfpMGbg%3D%3D))

  > 废弃了如下几个工厂

  ```actionscript
  static void ServerSocket.setSocketFactory(SocketImplFactory fac)
  static void Socket.setSocketImplFactory(SocketImplFactory fac)
  static void DatagramSocket.setDatagramSocketImplFactory(DatagramSocketImplFactory fac)
  ```

### 已知问题

- TreeMap.computeIfAbsent Mishandles Existing Entries Whose Values Are null ([JDK-8259622](https://link.segmentfault.com/?enc=WWUxX9iyJ0aPqG3UO%2BWeGw%3D%3D.USgYE0g4M%2FUyO2U4YKkCi6zZfG0S4ke8tIKIwoVQEvM8uDMf%2FbuVGs8HYij74xDqZS21EJ5cFUPdSrduR3viPA%3D%3D))

  > TreeMap.computeIfAbsent方法针对null的处理与规范有偏差

- Segmentation Fault Error on 9th and 10th Generation Intel® Core™ Processors ([JDK-8263710](https://link.segmentfault.com/?enc=crXNeKeNZgZJloGYNnXdkg%3D%3D.G2idnaddG%2BdMbKANQNeoq908NbxkYREie0vICkccAJS%2BgX7%2BkacWLaVZI17Tg%2Fxcw8XYDHnRBk9AbyFdS%2BSmgQ%3D%3D))

  > 当运行在9th and 10th Generation Intel® Core™ Processors时会有Segmentation Fault Error

### 其他事项

- Updated List of Capabilities Provided by JDK RPMs ([JDK-8263575](https://link.segmentfault.com/?enc=TnuLnH6eEHc98po2102EYA%3D%3D.SbIuVnvjqBe36WW1f5aQD9D6voOSSPouJZM%2B%2F5MrZNBoWNtawrna1LmLoDG%2BrfSx5FStFB4uE%2FH8WIStLdJclw%3D%3D))

  > xml-commons-api, jaxp_parser_impl, 以及java-fonts已经从OracleJDK/OracleJRE RPMs移除

- New Implementation of java.nio.channels.Selector on Microsoft Windows ([JDK-8266369](https://link.segmentfault.com/?enc=lXCTMfvV6wf4yQzK6YHdAQ%3D%3D.7VkvX29xtWsFno4Zh0i6uM3g1LRClgfPhMMujNVv%2B4cbzAtZNujXRQddIih5mRGsEDX8IF0iqtG%2FkxlU9lbDRw%3D%3D))

  > 对windows的java.nio.channels.Selector API采用了更具可扩展的方式实现，原来的实现没有被移除，可以使用`-Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.WindowsSelectorProvider`来继续使用

- Parallel GC Enables Adaptive Parallel Reference Processing by Default ([JDK-8204686](https://link.segmentfault.com/?enc=%2Bo7kXrExlxGSHvaf04U1nw%3D%3D.rxjf3OYLoAZWqkDzxPK1uuYijLkwFDbxXpcRRIbklx%2FJW39WLwoZ2%2BO%2Foh4Q4iR3j%2FAtFAvX0IGXWZ2dL765PA%3D%3D))

  > 针对Parallel GC默认开启了`-XX:ParallelRefProcEnabled`

- URLClassLoader No Longer Throws Undocumented IllegalArgumentException From getResources and findResources ([JDK-8262277](https://link.segmentfault.com/?enc=lD0RF2d9wD5FFpI2xswVAg%3D%3D.pVgnNfPBa4KRgClMmfBF9Gjm3DtvPjY3uVCy50dXTX14VLaMDeQ0YauapyCJ7hEUEgyc%2BmKkvHgpMmJUYBa81Q%3D%3D))

  > URLClassLoader的getResources、findResources不再抛出文档未定义的IllegalArgumentException

## 小结

Java17主要有如下几个特性

- [JEP 306: Restore Always-Strict Floating-Point Semantics](https://link.segmentfault.com/?enc=XQylMHrnYrX%2FC3CQQ6xJbQ%3D%3D.F31buMYQJUWO%2FVnARgNYv0Qmw5jr7xVCMt3t%2BIQpXHTugScVq9S%2BfEl9joE2fWxB)
- [JEP 356: Enhanced Pseudo-Random Number Generators](https://link.segmentfault.com/?enc=meXO71C5KAdgMbaTgus5og%3D%3D.WnUZTRvUcfdc3FGQe%2FwXoGeKhXJnRj4LOh8EZ9%2BLM7ffn2QVpE9VLpUHzigEUBVC)
- [JEP 382: New macOS Rendering Pipeline](https://link.segmentfault.com/?enc=ObvSXM4Y4%2FWFujWLVFLYnQ%3D%3D.60WPlqOWVyuG0ZijEzrJ%2B%2B%2BQU80LWFT%2BpqLb1kNi%2BpvqFquTMK8xmiAZQSJWtQx3)
- [JEP 391: macOS/AArch64 Port](https://link.segmentfault.com/?enc=2Hoo92yGMcD5ToocCpWcYA%3D%3D.A9ZXVaMfSYcAfmTNqdzGGRbcmomjS98sUVSTXkA3FztWWIl1Xzv4xt0MwRIejdkR)
- [JEP 398: Deprecate the Applet API for Removal](https://link.segmentfault.com/?enc=pLDLXHWZsEymNP3A8llbnQ%3D%3D.XhKY%2FKHoYjXAeRNNCXybcN2v8CPj6%2Fm4CvsGq4zZSGqGLbCN4gwD9wdyNkzDgsd7)
- [JEP 403: Strongly Encapsulate JDK Internals](https://link.segmentfault.com/?enc=ve10CRwtsX%2FvXCxrnG9qKg%3D%3D.34XmHEzN1XmqRpeGIUeU7mbp0pRO%2BU%2Bd0FL5ClkAhheKL2GdlQ7a2cQOE1PgwzHd)
- [JEP 406: Pattern Matching for switch (Preview)](https://link.segmentfault.com/?enc=C%2BgUlVkl79giBEEhS8Lnaw%3D%3D.CVMZCGP30m%2FeHSTqPbQoKtWEN1sF74P2QzTduyD%2Bdf3ydJ5NCy8lW7Pg2Z1CHyBn)
- [JEP 407: Remove RMI Activation](https://link.segmentfault.com/?enc=1%2F%2Fc646WhXPKykGVSXSSXQ%3D%3D.ysPJ%2FdNowRQB4SMyKaJ4yWH25qjGRIRTZwg4SN5u7HwKpDfkkZzJQZrHf31SnlyD)
- [JEP 409: Sealed Classes](https://link.segmentfault.com/?enc=aicWvSEMLkLUVQWmHJFl2w%3D%3D.JOwpz3dQvQKPmgT64sQC%2BFpq4C65Gn%2Bfr6ydIxVSubu8OP8DG2yG3yjC95Yv0Z%2Fj)
- [JEP 410: Remove the Experimental AOT and JIT Compiler](https://link.segmentfault.com/?enc=exqqie%2FapFKVYkiESN2BSA%3D%3D.dQ9hoyRPFOReCDHe4UVw0TU8V%2FV%2B6qtI1EBazO6rwNSdfDUpHTu4Uh%2BsPD7jFTre)
- [JEP 411: Deprecate the Security Manager for Removal](https://link.segmentfault.com/?enc=Ad56vTmm1jiaWk3QJjIGgA%3D%3D.C8LGmV%2Bjh%2BXRol%2BONi1XncFBFPFHNxhxcIgR4bjSN77CIJiMgfAlLXC22lnIpuON)
- [JEP 412: Foreign Function & Memory API (Incubator)](https://link.segmentfault.com/?enc=s0IqgBGN2K74xZyYsdcoGA%3D%3D.mPIvYSGTn%2FJz586ZPIsaYPnb0BT7OurVANWAByq38YUxnlchSx3KOqtGtKJIXlQ1)
- [JEP 414: Vector API (Second Incubator)](https://link.segmentfault.com/?enc=DSO1HqbCXY3h1USEFaqvNA%3D%3D.kms8GqLqVAD1PRqa2OVIbK395%2BBryTkIC26U3dY6VzQ0xbPNC%2BstAN9KqvU2TV43)
- [JEP 415: Context-Specific Deserialization Filters](https://link.segmentfault.com/?enc=wNYykdpkCm8s4EAHoK%2Fq8g%3D%3D.a8xzYNeRfYwZOb4oINlSHKuH1yYx57IvVukftFxz56cwpCClzRUIJ2nt48Um6CtQ)

## doc

- [JDK 17 Features](https://link.segmentfault.com/?enc=GhHY74pDQi7w4D7YxYPZsg%3D%3D.I55QIQkZcTqLSZiae%2BiQHobxSXIVI03uvvdJ69m0OglM0uynA4KkparuvtP6cqGy)
- [JDK 17 Release Notes](https://link.segmentfault.com/?enc=SjRADAHtJA6RlkloiQWqtA%3D%3D.HMOcUXOyZT%2F5c0bWv1MehAo7SmpJ3nBVJZjuysUW%2FR49BHwG2r9ahIS64vXVWCjI)
- [JDK 17 Release Notes](https://link.segmentfault.com/?enc=MjrGNYGX7IpsDw0IumOc2A%3D%3D.pZMxw9wneaZW%2Fkp29oS8HMa1EnOkOBB28F2%2BRZiO388vAFbrBfRWBu0A%2FYigHnb21ricdy6Zv4dFOoQEwnYVY6eSJKasXmWpluwsuO4GZ%2Bs%3D)
- [Java SE deprecated-list](https://link.segmentfault.com/?enc=cyeBMdCn0DSMyXTv4FUQcg%3D%3D.z3Fm6ZE8%2BAKD75fFhUeCYEJIoQcbChqK3ekKDDJvc0C9ZQiGjofVWXTpJiQpj2CibugUn2vc6jmS8FTr1Xyl3huqwSbJg%2Bw9yETnAxpXqqQ%3D)
- [The arrival of Java 17!](https://link.segmentfault.com/?enc=ewKnlHiV5IMWsvNi%2FnGNRA%3D%3D.0fFZSIJlpnGovNnbEYg6rP2SOfKIKLhtnYwkeHFYK7WB5hupYGS5z5b8eNYLR5zrwNol3UKSpwsGZ1sy5vKWFw%3D%3D)
- [Java 17 and IntelliJ IDEA](https://link.segmentfault.com/?enc=IYOmw2%2FUoWYTfcFEWMFkRQ%3D%3D.HbiJLRgUs1Hu37pbrHThFyJFM6KvLDHmqwlR4aVKRK1mB%2FYdxbJGh4ynUqex5OwbixzfAygMTdazujsDC81rpXXuXpNWrpq3zqoFR9o1Kp0%3D)
- [Better Random Number Generation in Java 17](https://link.segmentfault.com/?enc=RFLNXHDTytt6xEhIIGFDqw%3D%3D.JAM%2FoS%2FEyMrRSu%2FKvuQ8ALSZskkT2y1eUzgebBTXcIuD9FwauufdP25HS%2BMsxe98)