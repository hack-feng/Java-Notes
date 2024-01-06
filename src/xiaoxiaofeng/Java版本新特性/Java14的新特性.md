## 序

本文主要讲述一下Java14的新特性

## 版本号

```apache
java -version
openjdk version "14" 2020-03-17
OpenJDK Runtime Environment (build 14+36-1461)
OpenJDK 64-Bit Server VM (build 14+36-1461, mixed mode, sharing)
```

> 从version信息可以看出是build 14+36

## 特性列表

### [305:Pattern Matching for instanceof (Preview)](https://link.segmentfault.com/?enc=%2B0vQKwI4iNkDVg9B6a%2BvsA%3D%3D.akwabh4oEnQZxAE1cQIAFkW%2F8HZxJ%2BaMAvuMwa1Fbkw7%2BnaC39bIIilegIEBpsWA)

> JDK14引入了preview版本的针对instanceof的模式匹配，其用法示例如下

```typescript
    public boolean isBadRequestError(Exception ex) {
        return (ex instanceof HttpClientErrorException rce) &&
                HttpStatus.BAD_REQUEST == rce.getStatusCode();
    }
```

> 无需在自己进行类型强转

### [343:Packaging Tool (Incubator)](https://link.segmentfault.com/?enc=B2jTAmJpI3duh4jhkfYuWA%3D%3D.SEjrbJkoBoDisdfzrXyTc6gitkJS1%2BaKN8QxHeWoJ%2FQm6tVT9Vhy9tN8dbJQ7r7n)

> JDK14引入了jdk.incubator.jpackage.jmod，它基于JavaFX javapackager tool构建，目的在于创建一个简单的打包工具，可以用于构建exe、pkg、dmg、deb、rpm格式的安装文件；非模块化的app的构建示例如下

```css
jpackage --name myapp --input lib --main-jar main.jar
```

### [345:NUMA-Aware Memory Allocation for G1](https://link.segmentfault.com/?enc=7U2e5EoLNeY8Dhn2QTIong%3D%3D.UnC3xGGlBGcqGAjXavoXjX0yM6Oqn6W7o%2FxXczf%2BpWx5oWOsdunw0JHF0zbh%2FzhR)

> 实现了NUMA-aware的内存分配，以提升G1在大型机器上的性能

### [349:JFR Event Streaming](https://link.segmentfault.com/?enc=Nwj%2FQeppKLt9ETcgcjs0dg%3D%3D.ZY0%2BBuO3dZx7OOHEZn5GwPHM5qT0M94%2F1ZgjjfskykK0WVfaj0FVb9u7ElGm1Hly)

> JDK11引入了JFR，使用的时候先dump到磁盘上然后再分析；而在JDK14则支持stream方式来进行持续性的监控，示例如下

```reasonml
public class AgentMain implements Runnable {

    public static void premain(String agentArgs, Instrumentation inst) {
        try {
            Logger.getLogger("AgentMain").log(
                    Level.INFO, "Attaching JFR Monitor");
            new Thread(new AgentMain()).start();
        } catch (Throwable t) {
            Logger.getLogger("AgentMain").log(
                    Level.SEVERE,"Unable to attach JFR Monitor", t);
        }
    }

    @Override
    public void run() {
        var sender = new JfrStreamEventSender();
        try (var rs = new RecordingStream()) {
            rs.enable("jdk.CPULoad")
                    .withPeriod(Duration.ofSeconds(1));
            rs.enable("jdk.JavaMonitorEnter")
                    .withThreshold(Duration.ofMillis(10));
            rs.onEvent("jdk.CPULoad", sender);
            rs.onEvent("jdk.JavaMonitorEnter", sender);
            rs.start();
        }
    }
}
```

### [352:Non-Volatile Mapped Byte Buffers](https://link.segmentfault.com/?enc=1wfG64WFQmq6fcnpd5TRWQ%3D%3D.lQLRDMK29qGT6HdoIAWdRj9v%2FOsQmX4ndkdxBvWCsn8eHWZgXQZQiGxaYoRGB0VC)

> 该特性新增了java.base/jdk/internal/misc/ExtendedMapMode.java以支持MappedByteBuffer访问non-volatile memory (NVM)

### [358:Helpful NullPointerExceptions](https://link.segmentfault.com/?enc=4eJBlnbqRPZ8E0qhwwD15A%3D%3D.RmyAwbVR%2BG9HqNWbTl%2BRgJ1YQqdjGywYIE9PCMRYfv%2F7FsAfhAOkRBhGIDYI9QwO)

> 该特性可以更好地提示哪个地方出现的空指针，需要通过-XX:+ShowCodeDetailsInExceptionMessages开启，示例如下

```pgsql
public class NullPointerExample {

    public record City(String name){

    }

    public record Location(City city) {

    }

    public record User(String name, Location location) {

    }

    public static void main(String[] args){
        User user = new User("hello", new Location(null));
        System.out.println(user.location().city().name());
    }
}
```

> 输出如下

```smali
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "com.example.NullPointerExample$City.name()" because the return value of "com.example.NullPointerExample$Location.city()" is null
    at com.example.NullPointerExample.main(NullPointerExample.java:25)
```

### [359:Records (Preview)](https://link.segmentfault.com/?enc=V%2FgIXfbIKO%2BPcACkwVwtSA%3D%3D.8ISQOM1rRXT%2FYL6rwnVFa3HlsO6s7MIFn%2FCrSCMAfLvEgd6a11r5JkBt6sJRD8aK)

> JDK14引入了preview版本的record类型，示例如下

```pgsql
public record Point(int x, int y) {

    public Point {
        System.out.println(String.format("(%d,%d)", x, y));
    }

    public Point(int value) {
        this(value, value);
    }

    public static Point of(int value) {
        return new Point(value, value);
    }
}
```

> javap反编译如下

```pgsql
javac --enable-preview -source 14 Point.java
javap -verbose Point.class
Compiled from "Point.java"
public final class com.example.domain.Point extends java.lang.Record {
  public com.example.domain.Point(int, int);
  public static com.example.domain.Point of(int);
  public java.lang.String toString();
  public final int hashCode();
  public final boolean equals(java.lang.Object);
  public int x();
  public int y();
}
```

> 可以看见Point继承了java.lang.Record，而且通过invokedynamic提供了final的hashCode及equals

### [361:Switch Expressions (Standard)](https://link.segmentfault.com/?enc=sW8c63XMkDJvVjOQlJOYrw%3D%3D.4xK6Fvq0M333rgyyTqFl%2Fmx6JgpPH9jSvFlx2EIls2USXsstLCoJOkyn7W8id2cn)

> JDK12引入的switch在JDK14变为正式版本，正式版主要是用`->`来替代以前的`:`+`break`；另外就是提供了yield来在block中返回值，示例如下

```csharp
    public void testSwitchWithArrowBlockAndYield() {
        int n = 3;
        String quantityString = switch (n) {
            case 1 -> "one";
            case 2 -> "two";
            default -> {
                System.out.println("default");
                yield "many";
            }
        };
        System.out.println(quantityString);
    }
```

### [362:Deprecate the Solaris and SPARC Ports](https://link.segmentfault.com/?enc=MKjdmGLH%2Fl%2FyBvAo1EkTFA%3D%3D.q2yoDH42m18IyTUq2QRxeDx10DT7rMjlJzP1wRZhkKBKX6e5UyiwtDjGiunkF8ow)

> 废弃了 Solaris/SPARC, Solaris/x64, and Linux/SPARC ports，以在未来的版本中移除

### [363:Remove the Concurrent Mark Sweep (CMS) Garbage Collector](https://link.segmentfault.com/?enc=r9GbMp4pNlleBYgVYFY8cg%3D%3D.Qk%2FYmzIKJfkfihiI%2BDOY7itwZUZvD8iqW1E1q06cW4%2F%2BaNhJKW%2Fe09nfd75Djjlh)

> 移除了CMS垃圾收集器，如果在JDK14中使用-XX:+UseConcMarkSweepGC的话，会出现warning，但是不会exit而是以默认的垃圾收集器运行，如下

```pgsql
OpenJDK 64-Bit Server VM warning: Ignoring option UseConcMarkSweepGC; support was removed in 14.0
```

### [364:ZGC on macOS](https://link.segmentfault.com/?enc=ZV6pww1lQYrQhqo0hinFYQ%3D%3D.Kx0ptXKmXC%2BroZaxYbuTRkvgr25YRW5HDO7V%2BGvx7LDHtki%2BfjMM%2F6B1ER%2BEQ%2FJx)

> 之前的ZGC只能在linux上使用，现在mac上也能使用ZGC了，示例如下

```ruby
-XX:+UnlockExperimentalVMOptions -XX:+UseZGC
```

### [365:ZGC on Windows](https://link.segmentfault.com/?enc=mHWE9JhAAapib7m6CntLiA%3D%3D.gK5QMrcYpnNzxozjJPDMWqnqWYbgg%2FGQEKXeBvtPKe1EEL%2B8XxuQxIBQr1FAblFW)

> 之前的ZGC只能在linux上使用，现在windows(`不能低于1803版本`)上也能使用ZGC了，示例如下

```ruby
-XX:+UnlockExperimentalVMOptions -XX:+UseZGC
```

### [366:Deprecate the ParallelScavenge + SerialOld GC Combination](https://link.segmentfault.com/?enc=SdirLTiYWclxSHZOQbb3Vg%3D%3D.7o1IKvxQr8tnJvEXch055yc9hWRBF%2Bd8D03p3Xi6c78%2BUTONCXclBUZ9eT0V6Auv)

> 废弃了parallel young generation GC与SerialOld GC的组合(` -XX:+UseParallelGC与-XX:-UseParallelOldGC配合开启`)，现在使用-XX:+UseParallelGC -XX:-UseParallelOldGC或者-XX:-UseParallelOldGC都会出现告警如下

```pgsql
OpenJDK 64-Bit Server VM warning: Option UseParallelOldGC was deprecated in version 14.0 and will likely be removed in a future release.
```

> 单独使用-XX:+UseParallelGC则开启parallel young and old generation GC algorithms

### [367:Remove the Pack200 Tools and API](https://link.segmentfault.com/?enc=VcgHgsTXpgpIsbUsDxnXWw%3D%3D.qaqBrFfaDMs22IARiVm1vfx6OAYnJLxyec%2FGpDPAWk89uku3pW7DIK9%2F1iS%2F%2Fcm3)

> 移除了Pack200 API

### [368:Text Blocks (Second Preview)](https://link.segmentfault.com/?enc=0YmOk4wb56ZWNqpOl4TRfA%3D%3D.AiB3j%2Fwf2XKL0TyIOa8Q8itXdZNxxcTsPHHch0J9ypxKKFiYrfeDAACL6reQK2oZ)

> JDK13引入的text blocks进行第二轮preview，JDK14的版本主要增加了两个escape sequences，分别是` \<line-terminator>`与`\s escape sequence`，示例如下

```ceylon
    @Test
    public void testTextBlockWithTwoNewEscapeSequences() {
        String inOneLine = """
                Lorem ipsum dolor sit amet, consectetur adipiscing \
                elit, sed do eiusmod tempor incididunt ut labore \
                et dolore magna aliqua.\
                """;
        System.out.println(inOneLine);

        String singleSpace = """
                red  \s
                green\s
                blue \s
                """;
        System.out.println(singleSpace);
    }
```

### [370:Foreign-Memory Access API (Incubator)](https://link.segmentfault.com/?enc=zYVOmOjrlzB4H1HtSu864w%3D%3D.o9GYEoLxGV3LQuPpIfjjLR5YvGUHsv8YgDKEexaSVcogdFXdzch6ZBg2kxRhRG5G)

> 提供了incubator版本的API用于操纵堆外内存，使用示例如下

```csharp
    @Test
    public void testForeignMemoryAccessAPI() {
        SequenceLayout intArrayLayout
                = MemoryLayout.ofSequence(25,
                MemoryLayout.ofValueBits(32,
                        ByteOrder.nativeOrder()));

        VarHandle intElemHandle
                = intArrayLayout.varHandle(int.class,
                MemoryLayout.PathElement.sequenceElement());

        try (MemorySegment segment = MemorySegment.allocateNative(intArrayLayout)) {
            MemoryAddress base = segment.baseAddress();
            for (int i = 0; i < intArrayLayout.elementCount().getAsLong(); i++) {
                intElemHandle.set(base, (long) i, i);
            }
        }
    }
```

## 细项解读

上面列出的是大方面的特性，除此之外还有一些api的更新及废弃，主要见[JDK 14 Release Notes](https://link.segmentfault.com/?enc=IWuxwQ%2FDxCEjJNPlLk4kcg%3D%3D.eAd8ZbcZd91K29qXrKk492Pb9UzZYzzYdgt3BlfcHZaZ%2BPA%2BjBPSXy9nWpbFZa9s)，这里举几个例子。

### 添加项

- New Method to SAX ContentHandler for Handling XML Declaration ([JDK-8230814](https://link.segmentfault.com/?enc=jQ%2Ba1jezqBlAp6ho7VMzZg%3D%3D.zTGxF7kb62V5nRkMHHVC83UwHLYf9vsWIBDqehYWC%2FTpgulyQ6VenTFQOyNP3ypkkgCZuSELdP%2BJYk3yAiye6A%3D%3D))

  > 给SAX ContentHandler新增了方法，如下

  ```arduino
    default void declaration(String version, String encoding, String standalone)
        throws SAXException
    {
        //no op
    }
  ```

### 移除项

- Removal of sun.nio.cs.map System Property ([JDK-8229960](https://link.segmentfault.com/?enc=b2lDDEApFIq1bk0bZOJDGw%3D%3D.DiI74XJcDMZDxLBljimsDXy41eUOMikhV4CH4On2ZhZODqbLSiJNzxB2R3C2dZIQuLBbA5BLQeYYeu0VtQ3%2B6g%3D%3D))

  > 移除了sun.nio.cs.map属性

- Removed Deprecated java.security.acl APIs ([JDK-8191138](https://link.segmentfault.com/?enc=S2Ij6pVkD5P0S9ZAJOxrhQ%3D%3D.f7%2B985qRekaNixmCCbfqInDQpj7YddXo6wFrqHjiNHfiBBRDZoe4M2pDJxvk17iODhGihBwxzAQTa7Tn%2Blw0mA%3D%3D))

  > 移除了java.security.acl

- Removal of the Default keytool -keyalg Value ([JDK-8214024](https://link.segmentfault.com/?enc=4ngCTA5gJ%2BZLKfWrQ2jiUg%3D%3D.9alWeWy6hNA2PA2Ed6KEVs1fLEdQXYV8tYRNDikEe0%2BuGT3eQ5E5CrcX%2BG%2FwVAeX357olUrJWuIJeQFQM3Bw1w%3D%3D))

  > 移除了默认的keytool -keyalg

### 废弃项

- Thread Suspend/Resume Are Deprecated for Removal ([JDK-8231602](https://link.segmentfault.com/?enc=y%2BEvhz37%2BX4c0BnQasE4rw%3D%3D.oKCvlRKUqyLkjezoOTuBFiKHQsFTOLa7%2BJ06uYorn9c7RAtXouNruvrCNMDwLaquvH6sGEF4AGUxR12uBgx46A%3D%3D))

  > 废弃了Thread的如下方法

  ```reasonml
  Thread.suspend()
  Thread.resume()
  ThreadGroup.suspend()
  ThreadGroup.resume()
  ThreadGroup.allowThreadSuspension(boolean)
  ```

### 已知问题

- Text Visibility Issues in macOS Dark Mode ([JDK-8228555](https://link.segmentfault.com/?enc=KtApqwKOaXzVeLedDZw5Uw%3D%3D.bxvhzl1qxQahBFgmVKnMtpzLBcq8h5vrOXazS0%2FF4dKTXweUzzr47tWQQ0SZ6lU40JZgr7ARi5uGXfTdNKf54Q%3D%3D))

  > 已知在macOS上的Dark Mode有Text Visibility Issues

### 其他事项

- Thread.countStackFrames Changed to Unconditionally Throw UnsupportedOperationException ([JDK-8205132](https://link.segmentfault.com/?enc=t9pcvROABSiosGmRLoq9Jg%3D%3D.Uk1ag%2BJ8AUTMqUzLtFAMBnro8V%2FJCCche3CO6CTMGTlI0KAvNtVU6m9sZQLEYPjA8axgCW919ILUo7lWS5Cz9g%3D%3D))

  > Thread.countStackFrames开启转为无条件地抛出UnsupportedOperationException

## 小结

Java14主要有如下几个特性

- [305:Pattern Matching for instanceof (Preview)](https://link.segmentfault.com/?enc=oPE%2BsjoCJGH%2FGuwGNiGwXQ%3D%3D.sCipHBmyWu3x%2FP%2BAt8e1I6nfBza5ViAi95LkuySkFwOn5vn9T1f5V1gXo37SIhln)
- [349:JFR Event Streaming](https://link.segmentfault.com/?enc=95ksrsN%2B6fAdoLg8P5dYFA%3D%3D.NYY5jPVmB%2BBwUa4Z%2Buz04DRBTuMH1D7Djjk9KgzmJdtCnhqxr2FOBHC5%2B%2FP9v65w)
- [352:Non-Volatile Mapped Byte Buffers](https://link.segmentfault.com/?enc=mhHue0ti6XcYeq2KGbSPwg%3D%3D.fpUAG50QkOv7cB7BBGfkdjxXPSA24XRReqBgjoyPfrabAoghYFYVriRHTkwiyC3R)
- [358:Helpful NullPointerExceptions](https://link.segmentfault.com/?enc=N25KxCghGifMYVnGMeqDcA%3D%3D.TdM9p9C9wx8cu8Y4aBwAfeiocHyGRHevhh1wp7CtoXbXAtZpOIKp1kCYd6hiOF8m)
- [359:Records (Preview)](https://link.segmentfault.com/?enc=VScoyhvzmUsZd4K%2B9H0Lfg%3D%3D.RfZlNKUzWKw81lQAjTVh%2B0zUDRJ%2FJrvskgEQEA6aHL%2F9fZKpFJRJUFJSVL58Nr%2Br)
- [361:Switch Expressions (Standard)](https://link.segmentfault.com/?enc=rexA8N9ToueMFMfwnz6f%2Bw%3D%3D.gnCdOQsxjb7x4dX3%2BOFrnwKNFUbxyNarFGqygCY86sZtWgxcG0lpMFFY%2FiFpt3zv)
- [363:Remove the Concurrent Mark Sweep (CMS) Garbage Collector](https://link.segmentfault.com/?enc=CfyrzWmB5WzrJOZGalATlw%3D%3D.mWTJ%2BVkTD7rhrI3jv2HOWG59GUBR%2BJ8SQuPP1bu5i9m1%2BxXN%2F5R7bauDNYtfIDbz)
- [364:ZGC on macOS](https://link.segmentfault.com/?enc=lL%2BBl3%2F%2FjWiLIW0ixxnnSw%3D%3D.lTi5DDVkq56dyQH56wGABkkYPEnv6PMTCI7zAvKEqulhk%2FdtGlADKmHj35tk81M0)
- [366:Deprecate the ParallelScavenge + SerialOld GC Combination](https://link.segmentfault.com/?enc=l1gMdJyV1DLBMXnwQL3UmQ%3D%3D.5RCyQXCKmuTiUJ3%2BLSH%2F1Hu%2Ft9t6MHn589P4iaUKRvfPLcGtwRbrxiIUk5lxS3Kt)
- [368:Text Blocks (Second Preview)](https://link.segmentfault.com/?enc=wMLrXkmTd7lkwBXuRNPgjg%3D%3D.Abw2OeK90fGWr%2Bflwql2Q%2Bosce9gg8EXeTcRrKQuOZwZ3H2UkFyXMewo6MUCfAJ3)
- [370:Foreign-Memory Access API (Incubator)](https://link.segmentfault.com/?enc=v7i%2FFIuyY40YIm08TLkRBg%3D%3D.3uAsFLvWmHoDFWxX3CDshpR2gWcZgeJnyojXXcTygH6RsTQbJdqp8LMI47VqtuCz)

## doc

- [JDK 14 Features](https://link.segmentfault.com/?enc=sn0I7WRYsZhykbjYX4lchg%3D%3D.GKW8Evikx7kM9r3llqUrESZgD3oie9Zyvo%2F%2Fp5JhCI9TzB%2BS4oU%2BdLtYwq8FGnyO)
- [JDK 14 Release Notes](https://link.segmentfault.com/?enc=J2hSs1gLmTMGuntGB1hQQQ%3D%3D.eMw%2FpcCI52AnbK3GpkUFFIEwRAaCPIzQPexBevTx8H%2BKrJNrQ0fvjBv%2FywxwSkx1)
- [Oracle JDK 14 Release Notes](https://link.segmentfault.com/?enc=L%2F283jrZ%2BOGV1nl4kSRimw%3D%3D.sUYzmm%2BW8L%2Bj6CogqJQu7aFs9s9kRg34Ewqh7E%2Bk11nkbpfp2nc3qUXvCd18uAmJO64kHbMH7i91YwTiC5A%2FXRwGiiBQtf0fqxvXPcBi1LA%3D)
- [Java SE deprecated-list](https://link.segmentfault.com/?enc=Qtn6A6V3w4A4w9xiWnAXeg%3D%3D.GK%2BNIIYfrNBKVYvnMgMP9d%2FOQWkRpBWD9YgNg3R7J%2Fg9am8A30u4Zp4MpKN4rk5h43mtEjWp7U16d2ff%2FiOm75Jwz5LYN3Cc5jp1gobyWmI%3D)
- [The Arrival of Java 14!](https://link.segmentfault.com/?enc=p6VawYsJY7sknXwka%2FVQrg%3D%3D.CZI4Gsw1kcH0Z1ZjGI%2FdAi9E3il6JPqTh5RWF9L0MT5kVBA%2FwppTSI3JvYt68UiQG3vamlbAE%2BV2ns%2BLMCt%2BeHvxsrZ3nAVRYRw5%2Ffn7IMQ%3D)
- [Java Flight Recorder and JFR Event Streaming in Java 14](https://link.segmentfault.com/?enc=hGsmV14mOCD%2Fa48l9tSFCQ%3D%3D.oHNb%2FbmJx74cxqDiY%2BSNHsT7ny4yYphXgRYCNbsS2SQLWO5pDgXAzAxVHh5sxYuwBO1vmZigHCJbFG9KiISKcSKiB%2FlVzXyQ9tEJz%2FpppoCULVCZNJXJ2voc1wtS45Xw)