## 序

本文主要讲述一下Java19的新特性

## 版本号

```apache
java -version
openjdk version "19" 2022-09-20
OpenJDK Runtime Environment (build 19+36-2238)
OpenJDK 64-Bit Server VM (build 19+36-2238, mixed mode, sharing)
```

> 从version信息可以看出是build 19+36

## 特性列表

### [JEP 405: Record Patterns (Preview)](https://link.segmentfault.com/?enc=wPkxKgkVZZpXjH%2B%2F7LomjA%3D%3D.Y%2BgFWLKrSgqiNmrD5QCqGXoPSddgg8VSIuD6m%2B7c31Q%3D)

> instanceof的模式匹配在JDK14作为preview，在JDK15作为第二轮的preview，在JDK16[JEP 394: Pattern Matching for instanceof](https://link.segmentfault.com/?enc=QQkxm4%2FwTjpUQU4U5BpuFw%3D%3D.XcUPIXs%2FpfszcHDrJL2W8%2F3Zr%2F1G7%2Bs0RfhVU4Ck8o0pQu8ioePU7in4iOlxUNyp)转正
> switch模式匹配在JDK17的[JEP 406: Pattern Matching for switch (Preview)](https://link.segmentfault.com/?enc=Qxz5fnlNUqQ9bOYk9jDpUA%3D%3D.tLUblB9fCaI%2FRFy7wFmb1an8fImhbmyRMBMzj6McyWA%3D)引入作为preview版本，在JDK18的[JEP 420: Pattern Matching for switch (Second Preview)](https://link.segmentfault.com/?enc=ZJZ2viL%2Bsh8c9raFfbedmw%3D%3D.rEARj09qYG0bbHpj2pn9I61RFnWqJEAu9Qn5kv6FkuE%3D)作为第二轮的preview

针对record类型，引入instance of可以这么写

```pgsql
record Point(int x, int y) {}

static void printSum(Object o) {
    if (o instanceof Point p) {
        int x = p.x();
        int y = p.y();
        System.out.println(x+y);
    }
}
```

现在可以这么写

```reasonml
record Point(int x, int y) {}

void printSum(Object o) {
    if (o instanceof Point(int x, int y)) {
        System.out.println(x+y);
    }
}
```

比较复杂的例子：

```reasonml
record Point(int x, int y) {}
enum Color { RED, GREEN, BLUE }
record ColoredPoint(Point p, Color c) {}
record Rectangle(ColoredPoint upperLeft, ColoredPoint lowerRight) {}

Rectangle r = new Rectangle(new ColoredPoint(new Point(x1, y1), c1), 
                            new ColoredPoint(new Point(x2, y2), c2));

static void printXCoordOfUpperLeftPointWithPatterns(Rectangle r) {
    if (r instanceof Rectangle(ColoredPoint(Point(var x, var y), var c),
                               var lr)) {
        System.out.println("Upper-left corner: " + x);
    }
}
```

如果是泛型record的话：

```wren
record Box<T>(T t) {}

static void test1(Box<Object> bo) {
    if (bo instanceof Box<Object>(String s)) {
        System.out.println("String " + s);
    }
}
static void test2(Box<Object> bo) {
    if (bo instanceof Box<String>(var s)) {
        System.out.println("String " + s);
    }
}
```

### [JEP 422: Linux/RISC-V Port](https://link.segmentfault.com/?enc=8MLrSoP%2FirS7r%2B7Ihm0cYQ%3D%3D.Hyr7upOZejFu5xEE0pNPjV6%2FtvsHG8bGH8hZlTFCQG8%3D)

> [RISC-V](https://link.segmentfault.com/?enc=xUfsakT1cB9pJVcCywZdNA%3D%3D.71uuXVNasyQbx%2B5NkH%2F6PvR2Lgqv%2BvyTGpQ95ESb8rpRNOaoVIJHgE2%2BfoW4vZ%2FM)是一个基于精简指令集（RISC）原则的开源指令集架构（ISA）,这个JEP则移植JDK到RISC-V上

### [JEP 424: Foreign Function & Memory API (Preview)](https://link.segmentfault.com/?enc=NyqeHQwaYjbRvS6yUvYPLw%3D%3D.AFpqN5gWR%2Fl28KyNaVTtz4Il2hx%2F437djyPMx%2Feu9mM%3D)

> Foreign Function & Memory (FFM) API包含了两个incubating API
> JDK14的[JEP 370: Foreign-Memory Access API (Incubator)](https://link.segmentfault.com/?enc=ZSYtb9qwz7By7qj9vGG1wg%3D%3D.7gcbgy%2F1ffN3a%2B2lIt8agMLpniHN87HIjeVOdQ0UYuUilh1aApAKmvhPLoIr5FtM)引入了Foreign-Memory Access API作为incubator
> JDK15的[JEP 383: Foreign-Memory Access API (Second Incubator)](https://link.segmentfault.com/?enc=5spzynPE0MUKlH%2BXxwDtkQ%3D%3D.WRqbltRNlMSPQzHIHj6mS3L%2BR1iNeICdd8zydmJ0SZjkZkXKwSOgrYcscIYUV0BI)Foreign-Memory Access API作为第二轮incubator
> JDK16的[JEP 393: Foreign-Memory Access API (Third Incubator)](https://link.segmentfault.com/?enc=kCPLHB3EQxgH6Ydz0fIEsw%3D%3D.uZsWGQYMojtWxIH7ieHSJO7xn745JBh%2BjJnim4llWYevSTCkcM3G64V3WK1JBbjt)作为第三轮，它引入了Foreign Linker API (JEP [389](https://link.segmentfault.com/?enc=SJMGA4HYVRl2m52MiE7QEw%3D%3D.%2FpvCN05FMT28t5l4Ins%2FufiqlLie7u4NR1NxRb84Lscp9C5zstlUftwYkLQae534))
> FFM API在JDK 17的[JEP 412: Foreign Function & Memory API (Incubator)](https://link.segmentfault.com/?enc=jxpjoJHNYD5n6Q%2FTd4Tk9w%3D%3D.snTKJl6RUoEQ4b4YnFoMUgWz0VGLyz5nhIx6ir4U8Rg%3D)作为incubator引入
> FFM API在JDK 18的[JEP 419: Foreign Function & Memory API (Second Incubator)](https://link.segmentfault.com/?enc=9EPyVk70XObKsV57fp%2FjqA%3D%3D.A20hWx40IlsAUdjZsxTnC1cKGPApT5YQDVIWe8b5%2FPE%3D)作为第二轮incubator
> JDK19的这个JEP则将FFM API作为preview API

使用示例

```java
// 1. Find foreign function on the C library path
Linker linker = Linker.nativeLinker();
SymbolLookup stdlib = linker.defaultLookup();
MethodHandle radixSort = linker.downcallHandle(
                             stdlib.lookup("radixsort"), ...);
// 2. Allocate on-heap memory to store four strings
String[] javaStrings   = { "mouse", "cat", "dog", "car" };
// 3. Allocate off-heap memory to store four pointers
SegmentAllocator allocator = SegmentAllocator.implicitAllocator();
MemorySegment offHeap  = allocator.allocateArray(ValueLayout.ADDRESS, javaStrings.length);
// 4. Copy the strings from on-heap to off-heap
for (int i = 0; i < javaStrings.length; i++) {
    // Allocate a string off-heap, then store a pointer to it
    MemorySegment cString = allocator.allocateUtf8String(javaStrings[i]);
    offHeap.setAtIndex(ValueLayout.ADDRESS, i, cString);
}
// 5. Sort the off-heap data by calling the foreign function
radixSort.invoke(offHeap, javaStrings.length, MemoryAddress.NULL, '\0');
// 6. Copy the (reordered) strings from off-heap to on-heap
for (int i = 0; i < javaStrings.length; i++) {
    MemoryAddress cStringPtr = offHeap.getAtIndex(ValueLayout.ADDRESS, i);
    javaStrings[i] = cStringPtr.getUtf8String(0);
}
assert Arrays.equals(javaStrings, new String[] {"car", "cat", "dog", "mouse"});  // true
```

### [JEP 425: Virtual Threads (Preview)](https://link.segmentfault.com/?enc=CzMTsmfhbZuPtaGZaeAN%2FQ%3D%3D.6LgZn9LrVp7o%2Fh%2BHntZP%2BHCGTX6i7iwPU73lEHAnqS0%3D)

> 虚拟线程应该是JDK19最重磅的特性，首次集成进来即为preview的API
> 虚拟线程是由JDK提供的用户态线程，类似golang的goroutine，erlang的processes
> 虚拟线程采用的是M:N的调度模式，即M数量的虚拟线程运行在N个Thread上，使用的是ForkJoinPool以FIFO模式来进行调度，N默认是为Runtime.availableProcessors()，可以通过`jdk.virtualThreadScheduler.parallelism`来修改

使用示例

```livescript
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 10_000).forEach(i -> {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));
            return i;
        });
    });
}  // executor.close() is called implicitly, and waits
```

> 如上使用了少数几个OS线程来运行10000个虚拟线程
> 虚拟线程在超过上千个非CPU密集并发任务场景可以显著提升系统的吞吐率

```livescript
void handle(Request request, Response response) {
    var url1 = ...
    var url2 = ...
 
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        var future1 = executor.submit(() -> fetchURL(url1));
        var future2 = executor.submit(() -> fetchURL(url2));
        response.send(future1.get() + future2.get());
    } catch (ExecutionException | InterruptedException e) {
        response.fail(e);
    }
}
 
String fetchURL(URL url) throws IOException {
    try (var in = url.openStream()) {
        return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }
}
```

> 像这种场景虽然是block的代码，但是因为引入的是虚拟线程，系统可以很好地伸缩；当虚拟线程block在IO或者其他操作(`BlockingQueue.take()`)时，虚拟线程会从Thread unmount，当操作完成才重新mount上继续执行。不过有些操作不会unmount虚拟线程，会一同thread和底层的OS线程一起block住(比如进入synchronized代码块/方法，比如执行一个native方法或者foreign function)。
> 虚拟线程开销不大，因而不需要使用池化技术
> 使用`jcmd <pid> Thread.dump_to_file -format=json <file>`可以以json格式来dump虚拟线程，实例如下
> ![dump 虚拟线程](https://image.xiaoxiaofeng.site/blog/2023/06/29/xxf-20230629145627.png?xxfjava)
> Thread.Builder, Thread.ofVirtual(), Thread.ofPlatform() 可以用来创建虚拟线程或者是平台线程，比如

```autohotkey
Thread thread = Thread.ofVirtual().name("duke").unstarted(runnable);
```

> `Thread.startVirtualThread(Runnable)`等同于创建和启动虚拟线程
> Thread.threadId() 作为final方法会返回线程标识，而非final的Thread.getId()则被废弃
> Thread.getAllStackTraces()现在返回的是平台线程而非所有线程

### [JEP 426:Vector API (Fourth Incubator)](https://link.segmentfault.com/?enc=6zwTycVSvRc2vjJEIdOI4w%3D%3D.B1gFIgfKcwF8igUaSJ6zevd7G8taI%2BoTo%2B%2BgUX2CIY4%3D)

> JDK16引入了[JEP 338: Vector API (Incubator)](https://link.segmentfault.com/?enc=nCbgq1%2BkLxuQvWy4PyaiVw%3D%3D.N%2Fn4j%2BDwV0q60sHsEHJNMrw3rJ0R1khilmaN0c7GnTzwifZJNbBCQuphpn%2FDrkFb)提供了jdk.incubator.vector来用于矢量计算
> JDK17进行改进并作为第二轮的incubator[JEP 414: Vector API (Second Incubator)](https://link.segmentfault.com/?enc=PTaVd2qjbK5ummxc%2BjfvDg%3D%3D.ej2CJe2Jj4gFglo2kSptzh4V%2Be%2BI8GbkNj8Ixt9gv07kyoztOjdCuJcmZz%2BxwTmh)
> JDK18的[JEP 417: Vector API (Third Incubator)](https://link.segmentfault.com/?enc=6qu2%2BxsIuSQvsS9XPPCQUA%3D%3D.Sjecu4cRMDS0r3UBpHkjBMlVeRDweuarYJvoWCvaX00%3D)进行改进并作为第三轮的incubator，而JDK19则作为第四轮的incubator

### [JEP 427: Pattern Matching for switch (Third Preview)](https://link.segmentfault.com/?enc=ulggJ6NlxBuakRPEDLqv%2FA%3D%3D.dc0u2tQLCIHMSCkA%2BGoPASnykuHqlkpbSuODGJvhagk%3D)

> instanceof的模式匹配在JDK14作为preview，在JDK15作为第二轮的preview，在JDK16转正
> JDK17引入[JEP 406: Pattern Matching for switch (Preview)](https://link.segmentfault.com/?enc=%2B8pJcVpsPNtefGuW5Efxaw%3D%3D.OKHvFVrSJTBtwQKn1spvN8FSZs9eL5YXVT1roFllsy1foSMje0hUdnd7EQJK60Ws)
> JDK18的[JEP 420: Pattern Matching for switch (Second Preview)](https://link.segmentfault.com/?enc=almcHPdeEqaZAMglz69iXw%3D%3D.RgXFQrVXhbBNzC3%2BMGUA2mLvb3AO1dUpfMWOBeOjwYM%3D)则作为第二轮的preview，JDK19作为第三轮preview

### [JEP 428: Structured Concurrency (Incubator)](https://link.segmentfault.com/?enc=4nRmCfaj7UTNOdW4uKmQ2A%3D%3D.LauT5u6M9QQq2AVWW1bWdvOgXHiYULgiffsUXuA2BPY%3D)

> 结构化并发也是JDK19的一个重要特性。JDK5引入的ExecutorService可以用于并行处理任务，比如

```livescript
Response handle() throws ExecutionException, InterruptedException {
    Future<String>  user  = esvc.submit(() -> findUser());
    Future<Integer> order = esvc.submit(() -> fetchOrder());
    String theUser  = user.get();   // Join findUser
    int    theOrder = order.get();  // Join fetchOrder
    return new Response(theUser, theOrder);
}
```

> 但是当findUser抛出异常时，fetchOrder还是会在自己的线程继续运行，或者findUser需要运行很长时间，而当fetchOrder异常时，整个handle方法还是需要浪费时间等待findUser执行完，它阻塞在user.get()。为了更好地处理这种在异常场景下取消其他子任务，引入结构化并发来解决此问题，其主要是StructuredTaskScope这个类，它可以fork子任务，然后一起join或者一起cancel。

```livescript
Response handle() throws ExecutionException, InterruptedException {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        Future<String>  user  = scope.fork(() -> findUser());
        Future<Integer> order = scope.fork(() -> fetchOrder());

        scope.join();           // Join both forks
        scope.throwIfFailed();  // ... and propagate errors

        // Here, both forks have succeeded, so compose their results
        return new Response(user.resultNow(), order.resultNow());
    }
}
```

> 如果其中一个子任务失败了，则会取消另外一个在运行的任务。在scope.join()之后，可以使用resultNow()或者exceptionNow()来获取结果

## 细项解读

上面列出的是大方面的特性，除此之外还有一些api的更新及废弃，主要见[JDK 19 Release Notes](https://link.segmentfault.com/?enc=xGDmjlnxLht%2FW%2FUJW4wrdg%3D%3D.s1UNBwOHCOvyZalSu%2BAcuMJMapkRq4vuTpmDV6yv5NfyDFS%2Fjaqhl0gwx%2FKUD1L1)，这里举几个例子。

### 添加项

- System Properties for System.out and System.err ([JDK-8283620](https://link.segmentfault.com/?enc=nnx2o5rXmX2EoO3UvJhT%2FA%3D%3D.U2%2F%2FdKIrPlznNX4OFjz0XrAkSYwtCod5baShQ50suiCz9j7O3YXKpF2UHtAi2ItK))

  > 新增stdout.encoding这两个系统属性stderr.encoding

- Support Unicode 14.0 ([JDK-8268081](https://link.segmentfault.com/?enc=rvCa5vxE29zHvK7AkjwIMA%3D%3D.b%2BfZBnOVVoINNza%2FNFE%2FRNFCF5X%2FMFtO%2F%2BkSL6D3DwrjtjCmWBCYIZjvoKMVPsJY))

  > 新增支持unicode 14.0版本

- Additional Date-Time Formats ([JDK-8176706](https://link.segmentfault.com/?enc=wrFaoOc5fcS22HRFmO7LMQ%3D%3D.yaWe26rwAv9wvROk43qP8TTUG3fLoKGwxhTxxwsGQFHQzDKJMVfYdBuTQr0HycsL))

  > 以前java.time.format.DateTimeFormatter/DateTimeFormatterBuilder只支持FormatStyle.FULL/LONG/MEDIUM/SHORT这四种，现在可以自定义，比如`DateTimeFormatter.ofLocalizedPattern("yMMM")`

- Automatic Generation of the CDS Archive ([JDK-8261455](https://link.segmentfault.com/?enc=tLF1cZT11kbDfztUq8PgIA%3D%3D.pCBYbNEobp4xFNm2edyhBrS6kPCa5gHNJZXMuo2M%2BUU9dr4q3GttSzD7cOSDM3lP))

  > 新增`-XX:+AutoCreateSharedArchive`参数可以自动创建CDS archive，比如`java -XX:+AutoCreateSharedArchive -XX:SharedArchiveFile=app.jsa -cp app.jar App`

### 移除项

- Remove Finalizer Implementation in SSLSocketImpl ([JDK-8212136](https://link.segmentfault.com/?enc=jfTtG1ZPQ9uFMJlBQz4fzA%3D%3D.t5nAbPi8M%2BXJ9DeY9RvvzkwReszdqBWsqj9sEo0kpC64uiARnHXnD3Xfo%2FQ3Rdmt))

  > 移除SSLSocket的finalizer实现

- Removal of Diagnostic Flag GCParallelVerificationEnabled ([JDK-8286304](https://link.segmentfault.com/?enc=x6s8UTB9NIv3vFcRHOGBVQ%3D%3D.gOYmEwOnxoUYZPpfevWpTRWMRASTrbo%2BlVBFdTZP%2FI3v6JI1ubQ18KHHxF2ZE3u1))

  > 移除GCParallelVerificationEnabled参数

### 废弃项

完整列表见[Java SE 19 deprecated-list](https://link.segmentfault.com/?enc=RRVgMgzVwAG6GeN9l0YvCQ%3D%3D.NjsLlr73m8qq%2BQRqsJRTG80EHn8fiLLfr2n4jYH8TbVCbbVuilPZvbgmti6VTYxoONs0FxPhVwhwfVifG%2B0PTfCo3Jvn16q902UjjHmIjmM%3D)

- java.lang.ThreadGroup Is degraded ([JDK-8284161](https://link.segmentfault.com/?enc=7Q7OIfVf3UhDPL4Zs91iyg%3D%3D.KRfbSRIcQtPdAtHa1xcTaw6qvTi8RUGdq6u9HM9H6YJa97Gmjsg9zAQ55VuK4yNO))

  > ThreadGroup不能再被显示destroy了，它现在不再与其subgroup保持强引用

- Deprecation of Locale Class Constructors ([JDK-8282819](https://link.segmentfault.com/?enc=fdzcv36E0MUPcd54lekkKw%3D%3D.%2Bbj%2FIVhU6Tp6yVtqBkIdyQYfv583vZr%2BqeJ0QhEjjxvSwPt6u0zwZVn5HoOXhWHm))

  > Locale的构造器被废弃了，可用Locale.of()这个工厂方法替代

### 已知问题

- ForkJoinPool and ThreadPoolExecutor do not use Thread::start to Start Worker Threads ([JDK-8284161](https://link.segmentfault.com/?enc=alyeOrSrH3LAlgzTW7qoEw%3D%3D.BLfY4QMbCV9DcTqLInMnQWmU0erLxfUbrT4Cm%2Fh4Hzd0C4Rk6a7mP3B04cfjJTHv))

  > ForkJoinPool及ThreadPoolExecutor在这个版本不再使用`Thread::start来启动线程了，因而那些override无参start的工作线程可能会受影响，不过ForkJoinWorkerThread.onStart()不受影响

- InflaterInputStream.read Throws EOFException ([JDK-8292327](https://link.segmentfault.com/?enc=zRdswFuwMUzI25SjqUPCwg%3D%3D.BtugSfpG1jCrdQDBDapdFF2gADvcL38xtznZLd%2BFC07GS6RiaF4Bd6nyzSrsdgFj))

  > InflaterInputStream在这个版本可能抛出EOFException

- G1 Remembered set memory footprint regression after [JDK-8286115](https://link.segmentfault.com/?enc=h5ASunmAMAZ%2Fsb%2B4IRy2Ng%3D%3D.Rgwk7ILO3oMS5V76dZBg8v%2FcLVeeYvNDNK2tA8CUOjMsM1gMbGmubkW9D2VoEKFD) ([JDK-8292654](https://link.segmentfault.com/?enc=i1c9%2BfH0g6WrPgzM9MH70A%3D%3D.L7H4NUYNZ0ahEk5jYvOZKUluMjv%2FAVwd2VeWl6J5GsLeRI9G0iFy3vEUnTP68nat))

  > [JDK-8286115](https://link.segmentfault.com/?enc=Wj8Zk9dPq2MYoLwMPZFVwg%3D%3D.tiY0uryR%2B2dWsIM81ZJi3FxDQ82PRmuuke7XBHVY5oNS7dBD52u8mTjhlHSICTsj)这个变动了G1的RSet的ergonomic size，会造成本地内存使用增加，可以通过增加G1RemSetArrayOfCardsEntries值来缓解，比如

  ```ruby
  -XX:+UnlockExperimentalVMOptions -XX:G1RemSetArrayOfCardsEntries=128
  ```

### 其他事项

- JNI GetVersion Returns JNI_VERSION_19 ([JDK-8286176](https://link.segmentfault.com/?enc=O2loYHht23F%2BRytVGB9gyQ%3D%3D.IABXGHOIfe1%2BIptjtB%2F%2BLJDZLCjCq%2B6LfF8%2B%2BcS%2BeeBnVWU4opURG6k8tW3T4S42))

  > GetVersion这个jni方法返回JNI_VERSION_19

- Double.toString(double) and Float.toString(float) may Return Slightly Different Results ([JDK-4511638](https://link.segmentfault.com/?enc=rsI0imEZqKCsYgL%2Bq1OfQg%3D%3D.N%2BONqV0XzyjBhXeDk0%2F%2FbnJqVk3acacZ%2FQ2fpVaFQYjbDpGT2MuWACOrBg5cCMfs))

  > Double.toString(2e23), 现在会返回"2.0E23", 而之前的版本会返回"1.9999999999999998E23"

- Make HttpURLConnection Default Keep Alive Timeout Configurable ([JDK-8278067](https://link.segmentfault.com/?enc=jsaVE3oX%2FlAE14P9rJ4Sgw%3D%3D.yffIo8GT5YFqudizddiGsCaXHoYxgrW2GqUbFLHUXrZN4axrRG5kj%2F616mz3nQ5q))

  > 新增了http.keepAlive.time.server及http.keepAlive.time.proxy系统属性可以用于修改默认的Keep Alive Timeout

- JVM TI Changes to Support Virtual Threads ([JDK-8284161](https://link.segmentfault.com/?enc=fM9lJNDN2jfHvhmmeDaS3A%3D%3D.ZN0UdgMpGN6GzLDcIhSMqnI5ptjc4ad%2BVmsVZ55k9Zp2p%2FE6kyVRnrPm73fHjACV))

  > The JVM Tool Interface (JVM TI)已经更新，现可支持虚拟线程

- -Xss may be Rounded up to a Multiple of the System Page Size ([JDK-8236569](https://link.segmentfault.com/?enc=8cY3%2Biuo%2B3pmzATylmWRFQ%3D%3D.cNXqvQ68XBLaE9YA1koPpOBgmMyLLtu%2BEIj9676%2FYPgilWARYfeDpi1i6wsHeIS0))

  > 实际的java线程堆栈大小可能与-Xss命令行选项指定的值不同；它可能四舍五入为系统所需的页面大小的倍数。

## 小结

Java19主要有如下几个特性

- [JEP 405: Record Patterns (Preview)](https://link.segmentfault.com/?enc=7oqtSnPV18BnekpKjNFJ6g%3D%3D.3aqrco1Agie0pSRAPzdXCm%2BCgUHgvpD5GdOIxbJnQb8%3D)
- [JEP 422: Linux/RISC-V Port](https://link.segmentfault.com/?enc=3kcYIm4ABctNWQ47vU62fw%3D%3D.Azpm4Qx%2F4Z3gj8SPi3GXZuOltT%2BCVkoXOC%2FGulJGqZI%3D)
- [JEP 424: Foreign Function & Memory API (Preview)](https://link.segmentfault.com/?enc=3dfQi3Me3ceyzU%2B9S0fjKQ%3D%3D.GdnSa53krauXiP%2FB%2Buat2NKKQZNL%2BImvgAhccUhXmU4%3D)
- [JEP 425: Virtual Threads (Preview)](https://link.segmentfault.com/?enc=Qnj8U6%2FMFRK6z3hTc1FFTg%3D%3D.ASXRWUqYwpXCbeydlfaGaFQgOXoIKWj99lFRLRBuiqk%3D)
- [JEP 426: Vector API (Fourth Incubator)](https://link.segmentfault.com/?enc=RGFMlvNEtWpw0Ri2WDaY2w%3D%3D.cTZSLsncpUVgwSZAaDC13tgwNwGFcag8a26qhVCObzA%3D)
- [JEP 427: Pattern Matching for switch (Third Preview)](https://link.segmentfault.com/?enc=%2B5QmW0DDNtTIBEH2XuCTBA%3D%3D.%2BMOFGOYzBr3qHSX75D7Cug%2F%2F5TeCbmpIY0mlFraBLQQ%3D)
- [JEP 428: Structured Concurrency (Incubator)](https://link.segmentfault.com/?enc=j2yp%2F1%2FEDGlGZNZywPtWow%3D%3D.oTEjHHKGaDzOTdJKgrpRR6655ADm6KoeFa4B8e%2BPpvs%3D)

## doc

- [JDK 19 Features](https://link.segmentfault.com/?enc=7fuMehogvK5XixlXXRlmZA%3D%3D.1%2FGrGesdK7u6vle1DlV3VdBOPCZ3NcrziuaP47dU%2FAKtcQumxk2ab4l98NMvfuxC)
- [JDK 19 Release Notes](https://link.segmentfault.com/?enc=t8wtC8iuYJ2YHAouOcgEvg%3D%3D.LEf3gI%2FXOJGJr%2F4aZARjlGsPljur%2BPcTrO%2FJicYvJoqHKzdqIKqbXpNJq98Fp6W4)
- [Consolidated JDK 19 Release Notes](https://link.segmentfault.com/?enc=fCuW5veTTi3qTEYT0MWAmA%3D%3D.RzO%2BPB76Y5W%2Bz6HUsVs4E5bl58Kby6bw87%2BnHOId3P5LuMBZmUO4MIm3I8ws%2BHXk3XFKua%2B4n6YuAv9BSxkKDdvZ4HGDeJRXFw3YOya0RwA%3D)
- [Java SE 19 deprecated-list](https://link.segmentfault.com/?enc=80ov5lu8wSXv1DtOw0dRjA%3D%3D.48xhjBlAcfEKC7D0uJtzE0efdablLcM0sU4PO%2ByI0CNqE0TCxIid3ZlSXFHv5bQ1SQy01g0ykESuaECGpXpqwUjsJ%2FNRRfZ44bHigLoX0XA%3D)
- [The Arrival of Java 19](https://link.segmentfault.com/?enc=zi%2BKDJ8KwCd14mNwkz4JyQ%3D%3D.em%2BOmTcQvSRgfbOqft5a0KlGhe8pwsjC9b030dbSkfMgwvOo5WayspxsuA9zouQjaqa0JEvzp%2FyQ9of4HMEvjw%3D%3D)
- [JDK 19 G1/Parallel/Serial GC changes](https://link.segmentfault.com/?enc=mFoqG0naCVQz31TILsb9XA%3D%3D.MqwYzifBWUFPWsMk%2BcbII6OrMFiNe8LbMB3v2bJi9u2L1soX3HqxKhUmjHIcrksZFf2kS05o%2B%2BfAUUl5wdQjjluwdoFjuhIAF7TuzP81NxA%3D)
- [Java 19 Delivers Features for Projects Loom, Panama and Amber](https://link.segmentfault.com/?enc=FVqrR2VWth4BosGI0B5YhQ%3D%3D.w839W4B5T0corakyrGMa4u%2FrzdgBd7CYKUZvVvHH74sLcyCbYC8ByZEaDW2eQZe8Ex8zeHIO8F7BX9Vp%2FVIOHw%3D%3D)
- [JDK 19 and JDK 20: What We Know So Far](https://link.segmentfault.com/?enc=405beYIGlBc1lvuiuiEXlw%3D%3D.B2HWtMMw5zfYtb2%2FJTZv9ShuqjSmAzFVH3rOnJs9so8Dr1%2BQy0ru8In5TRs8uf5greD8YDLmi4YK9ayu3srEKA%3D%3D)