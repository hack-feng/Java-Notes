## 序

本文主要讲述一下Java13的新特性

## 版本号

```apache
java -version
openjdk version "13" 2019-09-17
OpenJDK Runtime Environment (build 13+33)
OpenJDK 64-Bit Server VM (build 13+33, mixed mode, sharing)
```

> 从version信息可以看出是build 13+33

## 特性列表

### [350: Dynamic CDS Archives](https://link.segmentfault.com/?enc=IS3AE9A9IoE6Qq2oLYtXOg%3D%3D.8155JIG%2Btlwgv0JkK4Drx918QHEqQGUhIA6ngNvrsEBo4f8MgDcFsZWS4FIi%2B%2Bx5)

> JDK5引入了Class-Data Sharing可以用于多个JVM共享class，提升启动速度，最早只支持system classes及serial GC
> JDK9对其进行扩展以支持application classes及其他GC算法
> java10的新特性[JEP 310: Application Class-Data Sharing](https://link.segmentfault.com/?enc=v2V9nl1RWI%2FDTAtk4XQZlQ%3D%3D.4Ennuqsc6wgGPnsPoCBCw7GQXoKEoCXmFnUAo4bWicgwgCDIX7PEb6VzGpHj6d4b)扩展了JDK5引入的Class-Data Sharing，支持application的Class-Data Sharing并开源出来(以前是commercial feature)
> JDK11将-Xshare:off改为默认-Xshare:auto，以更加方便使用CDS特性
> JDK12的[341: Default CDS Archives](https://link.segmentfault.com/?enc=VFYog%2FQmPdqSNxZVXJccBA%3D%3D.ICgjQY51I7MeXzaaLYIOs0nu4Hff3LGMDGCyJEfn4RE5vepbtfKj4IjAuKKpdgJC)即在64-bit平台上编译jdk的时候就默认在${JAVA_HOME}/lib/server目录下生成一份名为classes.jsa的默认archive文件(大概有18M)方便大家使用
> JDK13的这个特性支持在Java application执行之后进行动态archive

- 导出jsa

  ```mipsasm
  java -XX:ArchiveClassesAtExit=hello.jsa -cp hello.jar Hello
  ```

- 使用jsa

  ```mipsasm
  java -XX:SharedArchiveFile=hello.jsa -cp hello.jar Hello
  ```

### [351: ZGC: Uncommit Unused Memory](https://link.segmentfault.com/?enc=WwjX1kTjLLveZPAd4h9xqA%3D%3D.wDvQiIi9mY90uY6G8A1rf6Eu5V9zKSsPmkVEK0HplrSa4U0qAusIpZv%2FdgnkB6OU)

> Java12的[346: Promptly Return Unused Committed Memory from G1](https://link.segmentfault.com/?enc=xpJKGIetWoGNQkUkV0BgHw%3D%3D.ZQJQLQOpf20x1fL91AIR0jBjgA%2Fi5%2BlJnUgRpCkKF0w5sWFccF6QqLM5kOu90osl)新增了两个参数分别是G1PeriodicGCInterval及G1PeriodicGCSystemLoadThreshold用于GC之后重新调整Java heap size，然后将多余的内存归还给操作系统
> Java12的[189: Shenandoah: A Low-Pause-Time Garbage Collector (Experimental)](https://link.segmentfault.com/?enc=lCIQC3A4H%2FrATT5W0Lwaig%3D%3D.Cm6yBf3X5dl3465IdwAsGHnfZmTP8%2BMVS7aIWaCW7qDcETfzZZc2tpWN4yTAkEHM)拥有参数-XX:ShenandoahUncommitDelay=<milliseconds>来指定ZPage的page cache的失效时间，然后归还内存
> Java13则给ZGC新增归还unused heap memory给操作系统的特性；它新增了几个参数，-XX:ZUncommitDelay=<seconds>用于指定ZPage的page cache的失效时间；ZGC的归还内存默认是开启的，可以使用-XX:-ZUncommit来显式禁用

### [353: Reimplement the Legacy Socket API](https://link.segmentfault.com/?enc=3CCUbmpAlZBZQOJ43et7PQ%3D%3D.GCkW4AikHysFwK%2FiBsaHmoFmxxmtnJ1c9ysqC%2Fa6ZE1Z3WmfMkrL416fGetQSzVj)

> 本特性替换了java.net.Socket以及java.net.ServerSocket API的底层实现；它使用NioSocketImpl来替换JDK1.0的PlainSocketImpl；如果要继续使用旧版的Socket实现，可以使用-Djdk.net.usePlainSocketImpl参数来切换到旧版本

/Library/Java/JavaVirtualMachines/jdk-13.jdk/Contents/Home/lib/src.zip!/java.base/java/net/SocketImpl.java

```typescript
public abstract class SocketImpl implements SocketOptions {
    private static final boolean USE_PLAINSOCKETIMPL = usePlainSocketImpl();

    private static boolean usePlainSocketImpl() {
        PrivilegedAction<String> pa = () -> NetProperties.get("jdk.net.usePlainSocketImpl");
        String s = AccessController.doPrivileged(pa);
        return (s != null) && !s.equalsIgnoreCase("false");
    }

    /**
     * Creates an instance of platform's SocketImpl
     */
    @SuppressWarnings("unchecked")
    static <S extends SocketImpl & PlatformSocketImpl> S createPlatformSocketImpl(boolean server) {
        if (USE_PLAINSOCKETIMPL) {
            return (S) new PlainSocketImpl(server);
        } else {
            return (S) new NioSocketImpl(server);
        }
    }

    //......
}
```

> SocketImpl的USE_PLAINSOCKETIMPL取决于usePlainSocketImpl方法，而它会从NetProperties读取dk.net.usePlainSocketImpl配置，如果不为null且不为false，则usePlainSocketImpl方法返回true；createPlatformSocketImpl会根据USE_PLAINSOCKETIMPL来创建PlainSocketImpl或者NioSocketImpl

### [354: Switch Expressions (Preview)](https://link.segmentfault.com/?enc=qi%2B0iIuEfF6Fk3VUfOtaZA%3D%3D.fGnMwv%2BDXxn8W9ts22uKx6hIDWPYEzFpvyGXtCxVQ6GygNB3muTnukzH4K1gdMSW)

> 本特性主要是使用yield替换了break来避免歧义，因为break可以用来进行跳转执行类似goto的操作

```livescript
    @Test
    public void testSwitchYield(){
        String dayOfWeek = switch(1){
            case 1 -> {
                String day = "Monday";
                yield day;
            }
            case 2 -> {
                String day = "Tuesday";
                yield day;
            }
            default -> "Unknown";
        };
        System.out.println(dayOfWeek);
    }
```

### [355: Text Blocks (Preview)](https://link.segmentfault.com/?enc=7wJZ3DHM7o8M35MZHkcczg%3D%3D.gKUh%2BKe6Imw4nSOfahFUtFrqgE6T3ER3%2FZhoTZyw0Ejv%2BKfUUClcEwgZGWaE8rm5)

> 本特性主要引入了Text Blocks，使用"""来包围一段text block，可以内置占位符最后使用String.format来填充

```ceylon
    @Test
    public void testTextBlock(){
        // Without Text Blocks
        String html = "<html>\n" +
                "   <body>\n" +
                "      <p>Hello, Escapes</p>\n" +
                "   </body>\n" +
                "</html>\n";
        System.out.println(html);

        // With Text Blocks
        String html2 = """
            <html>
                <body>
                <p>Hello, %s</p>
                </body>
            </html>""";

        System.out.println(String.format(html2, "World"));

        String htmlWithNewLine = """
            <html>
                <body>
                <p>Hello World</p>
                </body>
            </html>
            """;
        System.out.print(htmlWithNewLine);
        System.out.println("a new line");
    }
```

> 需要注意text blocks不能在一行，另外如果结尾的"""在新的一行则会输出新的一行

## 细项解读

上面列出的是大方面的特性，除此之外还有一些api的更新及废弃，主要见[JDK 13 Release Notes](https://link.segmentfault.com/?enc=ewjdtDFlzcSoyM%2FeeX9AzA%3D%3D.5bhNlD9BY0Ob%2FVAQotyNzKwltHuHylWOjiWWT2hutJzV9E0tyfQb71YMGmkdejGD)，这里举几个例子。

### 添加项

- 添加FileSystems.newFileSystem(Path, Map<String, ?>) Method
- 新的java.nio.ByteBuffer Bulk get/put Methods Transfer Bytes Without Regard to Buffer Position
- 支持Unicode 12.1
- 添加-XX:SoftMaxHeapSize Flag，目前仅仅对ZGC起作用
- ZGC的最大heap大小增大到16TB

### 移除项

- 移除awt.toolkit System Property
- 移除Runtime Trace Methods
- 移除-XX:+AggressiveOpts
- 移除Two Comodo Root CA Certificates、Two DocuSign Root CA Certificates
- 移除内部的com.sun.net.ssl包

### 废弃项

- 废弃-Xverify:none及-noverify
- 废弃rmic Tool并准备移除
- 废弃javax.security.cert并准备移除

### 已知问题

- 不再支持Windows 2019 Core Server
- 使用ZIP File System (zipfs) Provider来更新包含Uncompressed Entries的ZIP或JAR可能造成文件损坏

### 其他事项

- GraphicsEnvironment.getCenterPoint()及getMaximumWindowBounds()已跨平台统一

- 增强了JAR Manifest的Class-Path属性处理

  > jdk.net.URLClassPath.showIgnoredClassPathEntries属性设置为true可以用来帮助查看非法的Class-Path entries

- 针对Negatively Sized Argument，StringBuffer(CharSequence)及StringBuilder(CharSequence)会抛出NegativeArraySizeException

- linux的默认进程启动机制已经使用posix_spawn

- Lookup.unreflectSetter(Field)针对static final fields会抛出IllegalAccessException

- 使用了java.net.Socket.setSocketImplFactory及java.net.ServerSocket.setSocketFactory方法的要注意，要求客户端及服务端要一致，不能一端使用自定义的factory一端使用默认的factory

- SocketImpl的supportedOptions, getOption及setOption方法的默认实现发生了变化，默认的supportedOptions返回空，而默认的getOption,及setOption方法抛出UnsupportedOperationException

- JNI NewDirectByteBuffer创建的Direct Buffer为java.nio.ByteOrder.BIG_ENDIAN

- Base64.Encoder及Base64.Decoder可能抛出OutOfMemoryError

- 改进了Serial GC Young pause time report

- 改进了MaxRAM及UseCompressedOops参数的行为

## 小结

- Java13主要新增了如下特性
  - 350: Dynamic CDS Archives
  - 351: ZGC: Uncommit Unused Memory
  - 353: Reimplement the Legacy Socket API
  - 354: Switch Expressions (Preview)
  - 355: Text Blocks (Preview)
- 语法层面，改进了Switch Expressions，新增了Text Blocks，二者皆处于Preview状态；API层面主要使用NioSocketImpl来替换JDK1.0的PlainSocketImpl
- GC层面则改进了ZGC，以支持Uncommit Unused Memory

## doc

- [JDK 13 Features](https://link.segmentfault.com/?enc=8bxjSAXUCIFQ96UcUs%2Fi%2Bw%3D%3D.kdK2H2d5a8%2BfzE816aL4NEj6E63Yaiap5e6ipRGF4YCyBIXDCcWn1qAVHDXcnWtF)
- [JDK 13 Release Notes](https://link.segmentfault.com/?enc=GoNtzdWP8Tr2u6oxvwa7Og%3D%3D.lKAZh68kO9xcq2cyX2Wu3rHuvy%2BR2%2F7eYT%2FFD97rWVqXG2aoJqLVqZItSFuZp0Y1)
- [Oracle JDK 13 Release Notes](https://link.segmentfault.com/?enc=NR%2BeKjjpKDzou5sVoXsfgQ%3D%3D.0QVyEtaKywRKTTzytTnhjXz2XKknMoJEqAf5Ap8ulxt%2Fo24KHdvxjozdmuVzjuMprC1TlztPu%2FCbaCWQsaoLMIp%2F2UAoW2exev16xD3Sujc%3D)
- [Java SE deprecated-list](https://link.segmentfault.com/?enc=sy1UDi4N7jgI5uO0PV7gcQ%3D%3D.tOvhUSLQtdIM5mAC7W4xWVixmdGUbcka7mKaflV9N4XZkeiaxx1Sz6x00yBGcZ2iyxmcn9Rj5WDLjPV%2BW%2FlQ4yhnh1JWrp2fqjA1vFK3gtc%3D)
- [The arrival of Java 13!](https://link.segmentfault.com/?enc=hnwTZNRJpb5VyLz%2BC6JEgw%3D%3D.IU1X0Pm%2FMw4UCpUQJaXHjalWWhcn7OznqO2jmavZAFphABZOXdhYpqvwFrxquZLroHCRzg0HdyR2gp9xPXoWCSLMSzXm4q0PcdSsLzjtEkU%3D)
- [Improve Launch Times On Java 13 With Application Class-Data Sharing](https://link.segmentfault.com/?enc=ulsRBAM53B7jqJ55YEzLgg%3D%3D.JvUy1bF9aRcMeljLKTEGKi50w1YvZYIWsYM1p762cY%2BsotoEzs7CZfyaPfVk%2FuRL8Xv42F8nTkPQVbxRdPPfZQ%3D%3D)