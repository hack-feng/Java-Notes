## 序

本文主要讲述一下Java20的新特性

## 版本号

```apache
java -version
openjdk version "20" 2023-03-21
OpenJDK Runtime Environment (build 20+36-2344)
OpenJDK 64-Bit Server VM (build 20+36-2344, mixed mode, sharing)
```

> 从version信息可以看出是build 20+36

## 特性列表

### [JEP 429: Scoped Values (Incubator)](https://link.segmentfault.com/?enc=q6wBatqOH8SDEWcz6z0F0g%3D%3D.U58Y6%2BQLMjtj0yyuz5TQIONK6gD02J1dIdh87eW2KjU%3D)

ScopedValue是一种类似ThreadLocal的线程内/父子线程传递变量的更优方案。ThreadLocal提供了一种无需在方法参数上传递通用变量的方法，InheritableThreadLocal使得子线程可以拷贝继承父线程的变量。但是ThreadLocal提供了set方法，变量是可变的，另外remove方法很容易被忽略，导致在线程池场景下很容易造成内存泄露。ScopedValue则提供了一种不可变、不拷贝的方案，即不提供set方法，子线程不需要拷贝就可以访问父线程的变量。具体使用如下：

```reasonml
class Server {
  public final static ScopedValue<User> LOGGED_IN_USER = ScopedValue.newInstance();
 
  private void serve(Request request) {
    // ...
    User loggedInUser = authenticateUser(request);
    ScopedValue.where(LOGGED_IN_USER, loggedInUser)
               .run(() -> restAdapter.processRequest(request));
    // ...
  }
}
```

> 通过ScopedValue.where可以绑定ScopedValue的值，然后在run方法里可以使用，方法执行完毕自行释放，可以被垃圾收集器回收

### [JEP 432: Record Patterns (Second Preview)](https://link.segmentfault.com/?enc=yLB%2BHKm%2FHcfYo1JGv7ukUA%3D%3D.GDfK%2Bo3%2Bf5WrU422RHlZxoLdiZ1nZNCaOMvWDrfrM%2Fw%3D)

> JDK19的[JEP 405: Record Patterns (Preview)](https://link.segmentfault.com/?enc=SCgSWmT0jslk%2BRJVAY0TkQ%3D%3D.zcnoP1z8DXfffrfh7yd4kqHUxFbJIt12oGmQEeL4Xio%3D)将Record的模式匹配作为第一次preview
> JDK20则作为第二次preview

- 针对嵌套record的推断，可以这样

  ```reasonml
  record Point(int x, int y) {}
  enum Color { RED, GREEN, BLUE }
  record ColoredPoint(Point p, Color c) {}
  record Rectangle(ColoredPoint upperLeft, ColoredPoint lowerRight) {}
  
  static void printColorOfUpperLeftPoint(Rectangle r) {
    if (r instanceof Rectangle(ColoredPoint(Point p, Color c),
                               ColoredPoint lr)) {
        System.out.println(c);
    }
  }
  ```

- 整体而言，模式匹配有如下几种：

  ```nestedtext
  Pattern:
  TypePattern
  ParenthesizedPattern
  RecordPattern
  
  TypePattern:
  LocalVariableDeclaration
  
  ParenthesizedPattern:
  ( Pattern )
  
  RecordPattern:
  ReferenceType RecordStructurePattern
  
  RecordStructurePattern:
  ( [ RecordComponentPatternList ] )
  
  RecordComponentPatternList : 
  Pattern { , Pattern }
  ```

- 针对泛型推断

  ```wren
  record Box<T>(T t) {}
  
  static void test1(Box<String> bo) {
    if (bo instanceof Box<String>(var s)) {
        System.out.println("String " + s);
    }
  }
  ```

  也支持嵌套

  ```javascript
  static void test3(Box<Box<String>> bo) {
    if (bo instanceof Box<Box<String>>(Box(var s))) {    
        System.out.println("String " + s);
    }
  }
  ```

### [JEP 433: Pattern Matching for switch (Fourth Preview)](https://link.segmentfault.com/?enc=fOKdFRyskvByZY%2BZ0O6nRw%3D%3D.idhjj%2B%2Bq6Kl3%2FGYcKtERRHQkN7PIYJ59%2BaLbIeSUkU8%3D)

> 在JDK14[JEP 305: Pattern Matching for instanceof (Preview)](https://link.segmentfault.com/?enc=M5Y3KsxQF8pp1307P2VroA%3D%3D.jpMsmcEGP7bYESHYrhzCQat4T76qK%2B%2FWGJ61iSeE69g%3D)作为preview
> 在JDK15[JEP 375: Pattern Matching for instanceof (Second Preview)](https://link.segmentfault.com/?enc=qlKt7BSZj74DNFVyVUBPiQ%3D%3D.10fPxFfea%2FJPOw9jI3DA6aicGk2%2B4w9rS3AbqbjE1a4%3D)作为第二轮的preview
> 在JDK16[JEP 394: Pattern Matching for instanceof](https://link.segmentfault.com/?enc=m6hXGWe9o1yfl0e7AfCa7Q%3D%3D.WU24AN0NMThqmSGcXIWenbR4jhXvi%2BExSU8sdzrgLII%3D)转正
> JDK17引入[JEP 406: Pattern Matching for switch (Preview)](https://link.segmentfault.com/?enc=1rEv2%2B3lNK7kTSQkxoN5CQ%3D%3D.1yXOl3Argj193yt6ilh277IxP0JG8g5c11HwRK7QflC23UP7%2B%2BSaIbS5Kv1cN7aX)
> JDK18的[JEP 420: Pattern Matching for switch (Second Preview)](https://link.segmentfault.com/?enc=x6NXKrvS5VU1SjexmQFCRA%3D%3D.bYAEuvmhyf078CAt0645VhIy4UdA70dqQ0FJQsXDTbk%3D)则作为第二轮preview
> JDK19的[JEP 427: Pattern Matching for switch (Third Preview)](https://link.segmentfault.com/?enc=0KMQhHl4NtHkI3m%2BXbdZ4A%3D%3D.VTkN9r%2BqwcsQKHbhcv2TMhuM%2Bsz1g4atJ76gkFpaRHM%3D)作为第三轮preview
> JDK20作为第四轮preview
> 自第三次预览以来的主要变化是：

- 针对枚举类型出现无法匹配的时候抛出MatchException而不是IncompatibleClassChangeError
- 开关标签的语法更简单
- switch现在支持record泛型的推断

以前针对null值switch会抛出异常，需要特殊处理

```csharp
static void testFooBar(String s) {
    if (s == null) {
        System.out.println("Oops!");
        return;
    }
    switch (s) {
        case "Foo", "Bar" -> System.out.println("Great");
        default           -> System.out.println("Ok");
    }
}
```

现在可以直接switch

```csharp
static void testFooBar(String s) {
    switch (s) {
        case null         -> System.out.println("Oops");
        case "Foo", "Bar" -> System.out.println("Great");
        default           -> System.out.println("Ok");
    }
}
```

case when的支持，以前这么写

```scala
class Shape {}
class Rectangle extends Shape {}
class Triangle  extends Shape { int calculateArea() { ... } }

static void testTriangle(Shape s) {
    switch (s) {
        case null:
            break;
        case Triangle t:
            if (t.calculateArea() > 100) {
                System.out.println("Large triangle");
                break;
            }
        default:
            System.out.println("A shape, possibly a small triangle");
    }
}
```

现在可以这么写

```csharp
static void testTriangle(Shape s) {
    switch (s) {
        case null -> 
            { break; }
        case Triangle t
        when t.calculateArea() > 100 ->
            System.out.println("Large triangle");
        case Triangle t ->
            System.out.println("Small triangle");
        default ->
            System.out.println("Non-triangle");
    }
}
```

针对record泛型的类型推断：

```wren
record MyPair<S,T>(S fst, T snd){};

static void recordInference(MyPair<String, Integer> pair){
    switch (pair) {
        case MyPair(var f, var s) -> 
            ... // Inferred record Pattern MyPair<String,Integer>(var f, var s)
        ...
    }
}
```

### [JEP 434: Foreign Function & Memory API (Second Preview)](https://link.segmentfault.com/?enc=6CQwCKY9Ja9U5IKpC5ffbw%3D%3D.CAn5XWv5Jl4u5EHlqaDRk7VBu4upLtco8HLYpLIl%2FAA%3D)

> Foreign Function & Memory (FFM) API包含了两个incubating API
> JDK14的[JEP 370: Foreign-Memory Access API (Incubator)](https://link.segmentfault.com/?enc=0eIeWp0bWxgh%2BAF2J4iNWQ%3D%3D.2u29qB8oL%2FNXksHtV9gN8h6whSmw5xdgOuLiZbmNkQZJCMM%2BNoIYafO4TipHKL8O)引入了Foreign-Memory Access API作为incubator
> JDK15的[JEP 383: Foreign-Memory Access API (Second Incubator)](https://link.segmentfault.com/?enc=IujxUw7Eiq9iXPBCjHHp4w%3D%3D.fJrR7RcuDeSIliwAVRCWfP%2BZXCMeJ%2BLhjSoUFp44hVGNdE7o332Q4eCtyHqW1JlD)Foreign-Memory Access API作为第二轮incubator
> JDK16的[JEP 393: Foreign-Memory Access API (Third Incubator)](https://link.segmentfault.com/?enc=BV6fepJAja1i3A8zbvyQhg%3D%3D.7j5reRA%2BNxnVl2pHB09TNaZ04Rq9x6MRVGEJu5Ga0EsoZhQ3bluNrmHxirwT66VF)作为第三轮，它引入了Foreign Linker API (JEP [389](https://link.segmentfault.com/?enc=W1tjtGZrbvbyLi3%2FPpGZOQ%3D%3D.TxSTBP5GvS7sHPZQpfLPXQ28GI2yAODE1JdmBGP%2BY8g%2F5FzUw1e5FHuSf4Z54zHs))
> FFM API在JDK 17的[JEP 412: Foreign Function & Memory API (Incubator)](https://link.segmentfault.com/?enc=HCAneExPm1qkL4J8GJGQjg%3D%3D.wOXagzyV%2Bn44JloZaSFvHH0PeTnLZDoNVNLlpAvgGkY%3D)作为incubator引入
> FFM API在JDK 18的[JEP 419: Foreign Function & Memory API (Second Incubator)](https://link.segmentfault.com/?enc=ycpSDJO7TsEeXcIH2hrR0w%3D%3D.GMxEj1e0hRCT1SRgEamHLbMre9Kk%2FxFgt1EdI5W8P3Q%3D)作为第二轮incubator
> JDK19的[JEP 424: Foreign Function & Memory API (Preview)](https://link.segmentfault.com/?enc=Sz%2BKqFWjLeifwYhXm7UoaA%3D%3D.jUO%2FIAvqt%2FqEiBgZkIzc61%2BD%2BmjI78ccMnobjpw6BW8%3D)则将FFM API作为preview API
> JDK20作为第二轮preview

使用示例

```reasonml
 javac --release 20 --enable-preview ... and java --enable-preview ....

// 1. Find foreign function on the C library path
Linker linker          = Linker.nativeLinker();
SymbolLookup stdlib    = linker.defaultLookup();
MethodHandle radixsort = linker.downcallHandle(stdlib.find("radixsort"), ...);
// 2. Allocate on-heap memory to store four strings
String[] javaStrings = { "mouse", "cat", "dog", "car" };
// 3. Use try-with-resources to manage the lifetime of off-heap memory
try (Arena offHeap = Arena.openConfined()) {
    // 4. Allocate a region of off-heap memory to store four pointers
    MemorySegment pointers = offHeap.allocateArray(ValueLayout.ADDRESS, javaStrings.length);
    // 5. Copy the strings from on-heap to off-heap
    for (int i = 0; i < javaStrings.length; i++) {
        MemorySegment cString = offHeap.allocateUtf8String(javaStrings[i]);
        pointers.setAtIndex(ValueLayout.ADDRESS, i, cString);
    }
    // 6. Sort the off-heap data by calling the foreign function
    radixsort.invoke(pointers, javaStrings.length, MemorySegment.NULL, '\0');
    // 7. Copy the (reordered) strings from off-heap to on-heap
    for (int i = 0; i < javaStrings.length; i++) {
        MemorySegment cString = pointers.getAtIndex(ValueLayout.ADDRESS, i);
        javaStrings[i] = cString.getUtf8String(0);
    }
} // 8. All off-heap memory is deallocated here
assert Arrays.equals(javaStrings, new String[] {"car", "cat", "dog", "mouse"});  // true
```

### [JEP 436: Virtual Threads (Second Preview)](https://link.segmentfault.com/?enc=r5ByV%2BTPx6M6jccOysl%2FgQ%3D%3D.psLMFLeWgAtOGfOPyGodiqPledhU8gVF6nt2fSJJOgs%3D)

> 在JDK19[https://openjdk.org/jeps/425](https://segmentfault.com/a/JEP))作为第一次preview
> 在JDK20作为第二次preview，此版本java.lang.ThreadGroup被永久废弃

使用示例

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

### [JEP 437: Structured Concurrency (Second Incubator)](https://link.segmentfault.com/?enc=6WV4T20zBq1pnE183YJGyA%3D%3D.mjQAx7OCjsC6wkvSy0bt6I8DaWslT7eRw%2BwzyYKi1uE%3D)

> 在JDK19[JEP 428: Structured Concurrency (Incubator)](https://link.segmentfault.com/?enc=e0p18JklCRdHFlweutPhwQ%3D%3D.lkc6KT5DTksHvb1dqWf5nd844zAZTpot%2FCe59P8G8ug%3D)作为第一次incubator
> 在JDK20作为第二次incubator

### [JEP 438: Vector API (Fifth Incubator)](https://link.segmentfault.com/?enc=%2FdPk62%2B7RrumV267iuqDxw%3D%3D.rv5j4pvw%2BhP%2BQo28Y4wWTA77aZHjLXuYKgpCVm1mP1k%3D)

> JDK16引入了[JEP 338: Vector API (Incubator)](https://link.segmentfault.com/?enc=R89WduoyzKZU0KLhFj3MjA%3D%3D.NeMBLcnDyzf6nuIrT5dKiDz1bSEY57XdyzGu6%2FqBoY9z5S8nuRHdXvChfzr3%2FqaD)提供了jdk.incubator.vector来用于矢量计算
> JDK17进行改进并作为第二轮的incubator[JEP 414: Vector API (Second Incubator)](https://link.segmentfault.com/?enc=p5Uv1SHLM2r%2FJlayLk7i%2FA%3D%3D.sFaYFirnlGLnHZYv7%2BZnxDe4pmK430FIQ9gFZmulCCJY4i2XlryP%2F%2BUSQBKHQFZb)
> JDK18的[JEP 417: Vector API (Third Incubator)](https://link.segmentfault.com/?enc=062uUqWG4YpFrZI1YC7ZWg%3D%3D.vuDoN0INTNDsCg%2B8LdceTQU4lQS8BvGGgqdtOTouXd8%3D)进行改进并作为第三轮的incubator
> JDK19[JEP 426:Vector API (Fourth Incubator)](https://link.segmentfault.com/?enc=jGZmdbyxLH646wliO3Yy0g%3D%3D.ohdms0mcZaudM7v8wJQabkgqqpPcanGtGBUQDplZALE%3D)作为第四轮的incubator
> JDK20作为第五轮的incubator

## 细项解读

上面列出的是大方面的特性，除此之外还有一些api的更新及废弃，主要见[JDK 20 Release Notes](https://link.segmentfault.com/?enc=SejuTXk31Rq%2BaHAcpuAvag%3D%3D.xfQ6T7kZpOu%2BAt1vTtsBmNQU7XVsq6UcuFqCZ8xyMW%2BPuf%2FSOTFuka4WcfEsSC08)，这里举几个例子。

### 添加项

- Support Unicode 15.0 [Update Unicode Data Files to 15.0.0](https://link.segmentfault.com/?enc=NtQlygVGUJjcCq%2FASFM%2Bvg%3D%3D.kx2cMyUE5H99q%2BRG9WaIVLi%2Fc5E0OC%2Bov29lqrNQAQl%2F%2BKmmPEzZVEcrtEAlBTN5)
- Add GarbageCollectorMXBean for Remark and Cleanup Pause Time in G1 [JDK-8297247](https://link.segmentfault.com/?enc=QkRjSQz3BG95l794h7buKQ%3D%3D.cPDPGfLLZzF9X1QJ3gbqLv9fepodl%2BcTwmOx8KMoLhrhwop%2Frg26kGVtPUpmfeRb)

### 移除项

- Thread.suspend/resume Changed to Throw UnsupportedOperationException [JDK-8249627](https://link.segmentfault.com/?enc=AO8JzgR1FDXi8O4hJcl1%2Bw%3D%3D.8N6CmNbkIEjWzkvLpUJN%2Bovoh2s7tb5xlzj0yJiLGQlLQDEYCGKkUB087yZGILPM)

- Thread.Stop Changed to Throw UnsupportedOperationException [JDK-8289610](https://link.segmentfault.com/?enc=o7MT9rKHF%2FGNCkNlDVowyw%3D%3D.O78P3CE%2BKgnIlapaqcH00G0zDjtmyLS9bDamZV5XA8xPns75g9xtmLMyNvXXdQkq)

- Improved Control of G1 Concurrent Refinement Threads [JDK-8137022](https://link.segmentfault.com/?enc=8%2Bdpt%2FsPT1x3ek7MVCRx7Q%3D%3D.BRfxq3xFLaW71yvSt7ZfoTiC%2FW%2F5uV28RTI55dh5qLeB7rYIalI3YNtx7byeVQcD)

  > 以下这些参数未来版本移除

  ```ruby
  -XX:-G1UseAdaptiveConcRefinement
  
  -XX:G1ConcRefinementGreenZone=buffer-count
  
  -XX:G1ConcRefinementYellowZone=buffer-count
  
  -XX:G1ConcRefinementRedZone=buffer-count
  
  -XX:G1ConcRefinementThresholdStep=buffer-count
  
  -XX:G1ConcRefinementServiceIntervalMillis=msec
  ```

### 废弃项

完整列表见[Java SE 20 deprecated-list](https://link.segmentfault.com/?enc=KIhkWMtj0cUK%2FyoBDqAOIQ%3D%3D.Aa81kq34P2sSB%2FlU3Gj0iKEM3qC%2BqtNam1NxSx39atZMiUQPBuqmA2GTCeO51MzqDVPaj9MGCYvxZel7zAeQKkrjTMLf%2FK4%2FWI8GGhrLslE%3D)

- java.net.URL Constructors Are Deprecated [JDK-8294241](https://link.segmentfault.com/?enc=ZcpS6x9U6Ac2NI1G7re5Sw%3D%3D.kubrp568NNRcL4CPiWuDfpBk5Bk7jLcx%2FShEORHgQ8Gfx0ZfmRFp3drUlryBVslb)

  > URL的构造器被废弃，可以使用URL::of(URI, URLStreamHandler)工厂方法替代

### 已知问题

- java.lang.Float.floatToFloat16 and java.lang.Float.float16ToFloat May Return Different NaN Results when Optimized by the JIT Compiler ([JDK-8302976](https://link.segmentfault.com/?enc=QMY0TTRA%2BM07YUMwujhRPg%3D%3D.5CCIlRSWFQTEesGhgnP0Qkq7%2FzEZhdlDVBw%2FFJzLDXJFoUBHQeNLQ8UfUeMKXXFr), [JDK-8289551](https://link.segmentfault.com/?enc=BpTf3c%2FWRNgaPDaaSoKw4g%3D%3D.eRDTaR7t2rl72bRpo8h%2Bclm0oXc%2FKPJl1AnjAt6qP%2BcaH5ud3Ky9c3s6up637ry0), [JDK-8289552](https://link.segmentfault.com/?enc=XtKOheTF8KJTpTz1VUfRqg%3D%3D.cupxspslZIwho3w2CuSwPdNEgd%2FcaNGHJv4QdLDMhLeGBbczjV%2F0BV2oQPZI22Jh))

  > JDK20引入了java.lang.Float.floatToFloat16及java.lang.Float.float16ToFloat方法，在JIT编译优化时可能会返回不同的Nan结果，可以使用如下参数禁用JIT对此的优化

  ```ruby
  -XX:+UnlockDiagnosticVMOptions -XX:DisableIntrinsic=_floatToFloat16,_float16ToFloat
  ```

### 其他事项

- Disabled TLS_ECDH_* Cipher Suites ([JDK-8279164](https://link.segmentfault.com/?enc=MLWBdVzDDEBPOOfLLnH0Cg%3D%3D.z%2B9DhkL12qUbqkKp1pNIYHb%2BaoIPLCecsEwzrcC2PF5kEFK%2BDaL26gHTn5kYZXe%2F))

  > TLS_ECDH_* cipher suites默认被禁用了

- HTTP Response Input Streams Will Throw an IOException on Interrupt ([JDK-8294047](https://link.segmentfault.com/?enc=1lhG%2BNnx0al4bAbDctpCNw%3D%3D.r2W0xAFy7QFoeHrKxdr94DCNmkr36laIaCxGzGI5g6BBc6fBn20NpkrKxf1T6Qrl))

- HttpClient Default Keep Alive Time is 30 Seconds ([JDK-8297030](https://link.segmentfault.com/?enc=nBYdkbG1C1A5EbjTv7e1YA%3D%3D.FkKpvOtPgRM6Ugn2ZaMDyGCDnZNlXbAsbmSHQRusWqdRmKLajfX4uaMdTieIVqZA))

  > http1.1及http2的空闲连接的超时时间从1200秒改为30秒

## 小结

Java20主要有如下几个特性

- [JEP 429: Scoped Values (Incubator)](https://link.segmentfault.com/?enc=1SNW5sc7TQqhWDVO17DFrA%3D%3D.JbDm7EhE63wXO3Xn7gljuVOtIM%2BcnxE2eZpem%2FVlJao%3D)
- [JEP 432: Record Patterns (Second Preview)](https://link.segmentfault.com/?enc=G50Tazdx1QO9rZd32563WA%3D%3D.cGP39UI9GBOX1cxgtQC7lYZBFqF1zj1wzZm2AXv83Bs%3D)
- [JEP 433: Pattern Matching for switch (Fourth Preview)](https://link.segmentfault.com/?enc=ainAXav%2BmpYOWzzefol6%2Fw%3D%3D.GeEV%2BTxaSrkL21emj8jL6uQnUrThq9r5EKn4W6A8P7M%3D)
- [JEP 434: Foreign Function & Memory API (Second Preview)](https://link.segmentfault.com/?enc=WvUUiNSJW3VrFF3HfJJgfg%3D%3D.mvrbycycHiOebzFaib9EXAhMx5oGDJlSUgSa7SPHD0k%3D)
- [JEP 436: Virtual Threads (Second Preview)](https://link.segmentfault.com/?enc=8Bl0XU6L2nTUJ9jfhVVIhA%3D%3D.%2FEpcc76xtAQzPHKZc4VekPW1mQSYYQAdnpydb2vglOA%3D)
- [JEP 437: Structured Concurrency (Second Incubator)](https://link.segmentfault.com/?enc=RYFgN5m4e%2BIkF%2FQXKXArvg%3D%3D.SQb8oK30RCWTg26vonKaeQbFRTSWIWgu5wbDA%2FDzuog%3D)
- [JEP 438: Vector API (Fifth Incubator)](https://link.segmentfault.com/?enc=D6J77jYv0kwFrR42bp6EqQ%3D%3D.5qyhHmYSLKdSF7UDabzniFnoQKmR7eLgqMvGcwgSlmg%3D)

## doc

- [JDK 20 Features](https://link.segmentfault.com/?enc=mRL%2FaGEkj5AGKEfPhZkc0Q%3D%3D.yB3Xq3ehUEvPIMY8bO7QO%2FpEL6whxeSH7QyPTj1Vffx3BwKA7lOkDdRMLPtjIoFc)
- [JDK 20 Release Notes](https://link.segmentfault.com/?enc=AfvskrpU9xys2JHiXehDUg%3D%3D.21HY6NOhNN%2B6zhNb%2B4iDOIgZHxD8H1uCSuOvfoL6MuWS1jPWTiFvBMYkLJAFfiv5)
- [Consolidated JDK 20 Release Notes](https://link.segmentfault.com/?enc=3EFuILa3591rBwnUVfuvFw%3D%3D.Az%2BxovUZ9N%2FawyOC2cKV3FpINvDO4lFUwin%2Fjb5NXdnkWdyB59ciC0EPiqgHvhikXdCb0NQf4CnVbUcQZau9ooK2eG77MxfXiFaMGi1doxg%3D)
- [Java SE 20 deprecated-list](https://link.segmentfault.com/?enc=c2auksnnbWMGq7lorFvfVA%3D%3D.o6JY7G%2BXLfGHP6EuRuwZ%2BVnC6UGQb1GL2it1B4aUnXe1Jro%2Fv48rFT%2BYQAPyAMtq0yXziJupTB%2BEMX1Jdwn6nrvUqMCbUENVM1y9WOWW1r4%3D)
- [The Arrival of Java 20](https://link.segmentfault.com/?enc=bvC9s5r5PcH07jIAQFKfZA%3D%3D.dQXhWZ9uclUjReYQi4jGNoE0oL8L6l24RRDGCgb9mGBMF%2Bw4RWslQDNyzW%2BqR0YthWOOflk2PFuz63IHZnkF8A%3D%3D)
- [JDK 20 G1/Parallel/Serial GC changes](https://link.segmentfault.com/?enc=2cMELWS3A4%2FCcECSxTCbaA%3D%3D.zcMejRjEhMxNnINxOQy%2FvTgQKcizrJ74g90jo3SrRCW1Zl6YJOMox%2BlXRLfH%2BN3wjDVLrMhPHcitMYNuIH489AiZ4OiAoxTHYXK5Li3%2F7Mk%3D)
- [Java 20 Delivers Features for Projects Amber, Loom and Panama](https://link.segmentfault.com/?enc=Qinx4z94J8JWqqVuSuz9DQ%3D%3D.n5T4dPrjXv78QoWK6INVex4Sti25aSK%2BdWeWLM8qH06oI7w6xYan9NPmTsHk%2BULLY6DDj4zfFZH8ZnChFDV%2BoA%3D%3D)
- [Java 20: a faster future is Looming](https://link.segmentfault.com/?enc=jI3wd3y7C%2FzFns0fflqExQ%3D%3D.I9xfhKEfToj1BX2cJIbPU1gQGpwtgHhOI%2FhaIqBcU85L9kiWMIhCAvE5qxzxZmuRMq54%2FpVX2z8D7fzDuYRpKg%3D%3D)
- [JDK 20 Security Enhancements](https://link.segmentfault.com/?enc=FgxEkBM9ppmF%2B80tnaDeKA%3D%3D.EvCrFYXsrXjdm8UgXKSWcCu%2BUid7XHTG0V9pBk11FE91%2BkJewgeen1Pkt%2F0U5Fbq)
- [Project Loom’s Scoped Values: The Most Interesting New Java 20 Feature](https://link.segmentfault.com/?enc=5Az7vef6mGXemhAvj2bI7w%3D%3D.ASN%2FLh%2B79CdsE7GgMPPfOGdOd%2FRLCFPR4UaeaBMKLjfFJUR1VnxpMe2TyvPtQNVw)