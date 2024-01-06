## 序

本文主要讲述一下Java12的新特性

## 版本号

```apache
java -version
openjdk version "12" 2019-03-19
OpenJDK Runtime Environment (build 12+33)
OpenJDK 64-Bit Server VM (build 12+33, mixed mode)
```

> 从version信息可以看出是build 12+33

## 特性列表

- [189: Shenandoah: A Low-Pause-Time Garbage Collector (Experimental)](https://link.segmentfault.com/?enc=PgrSrCrOtrrT1lMfpNlFMg%3D%3D.%2FSusTyLurXeJIwBPLB6Rlf1bexoXtdgMcHUF7MfoW1%2B0OB3MDifBmA%2BmNn5RSphI)

  > Shenandoah GC是一个面向low-pause-time的垃圾收集器，它最初由Red Hat实现，支持aarch64及amd64 architecture；ZGC也是面向low-pause-time的垃圾收集器，不过ZGC是基于colored pointers来实现，而Shenandoah GC是基于brooks pointers来实现；如果要使用Shenandoah GC需要编译时--with-jvm-features选项带有shenandoahgc，然后启动时使用-XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC

- [230: Microbenchmark Suite](https://link.segmentfault.com/?enc=ZrP%2FQD5al4rql4Gm%2By6HFQ%3D%3D.ymkjlS9ZsKgIiL0h8zo5L%2By4lZGtQw3CeDoUDI%2BJqxMVz2Jk4uGHvq2aORWo9qfP)

  > 在jdk源码里头新增了一套基础的microbenchmarks suite

- [325: Switch Expressions (Preview)](https://link.segmentfault.com/?enc=LUo0886oeQiWBGq8lbCUFg%3D%3D.2nxIZ%2B9zXp3ITIlqX%2BPZfYmzGQi6AZuSaGIYdvEnQGlewTDd2gphwZ1PaoQRDPZK)

  > 对switch进行了增强，除了使用statement还可以使用expression，比如原来的写法如下：

  ```angelscript
  switch (day) {
    case MONDAY:
    case FRIDAY:
    case SUNDAY:
        System.out.println(6);
        break;
    case TUESDAY:
        System.out.println(7);
        break;
    case THURSDAY:
    case SATURDAY:
        System.out.println(8);
        break;
    case WEDNESDAY:
        System.out.println(9);
        break;
  }
  ```

  现在可以改为如下写法：

  ```pgsql
  switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> System.out.println(6);
    case TUESDAY                -> System.out.println(7);
    case THURSDAY, SATURDAY     -> System.out.println(8);
    case WEDNESDAY              -> System.out.println(9);
  }
  ```

  以及在表达式返回值

  ```livescript
  int numLetters = switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> 6;
    case TUESDAY                -> 7;
    case THURSDAY, SATURDAY     -> 8;
    case WEDNESDAY              -> 9;
  };
  ```

  对于需要返回值的switch expression要么正常返回值要么抛出异常，以下这两种写法都是错误的

  ```csharp
  int i = switch (day) {
    case MONDAY -> {
        System.out.println("Monday"); 
        // ERROR! Block doesn't contain a break with value
    }
    default -> 1;
  };
  i = switch (day) {
    case MONDAY, TUESDAY, WEDNESDAY: 
        break 0;
    default: 
        System.out.println("Second half of the week");
        // ERROR! Group doesn't contain a break with value
  };
  ```

- [334: JVM Constants API](https://link.segmentfault.com/?enc=ifBoXLTNvAbc2cJZDC%2FNGA%3D%3D.gWldShL2uwVDEjjYx3XhjaTyGhrjkTjkb%2FhvxzpZsvvmMXUgcHszPQ%2BUFeBJKMtb)

  > 新增了JVM Constants API，具体来说就是java.base模块新增了java.lang.constant包，引入了ConstantDesc接口(`ClassDesc、MethodTypeDesc、MethodHandleDesc这几个接口直接继承了ConstantDesc接口`)以及Constable接口；ConstantDesc接口定义了resolveConstantDesc方法，Constable接口定义了describeConstable方法；String、Integer、Long、Float、Double均实现了这两个接口，而EnumDesc实现了ConstantDesc接口

- [340: One AArch64 Port, Not Two](https://link.segmentfault.com/?enc=IO4cpk866GXQ%2FONDkKbPig%3D%3D.WWax%2BALzIvTqyadXpaW81zwi40nslsrgS4N6r%2Blc3eC6k6g7i5OVTK%2FdONPVP%2F%2Bi)

  > 64-bit Arm platform (arm64)，也可以称之为aarch64；之前JDK有两个关于aarch64的实现，分别是src/hotspot/cpu/arm以及open/src/hotspot/cpu/aarch64，它们的实现重复了，为了集中精力更好地实现aarch64，该特性在源码中删除了open/src/hotspot/cpu/arm中关于64-bit的实现，保留其中32-bit的实现，于是open/src/hotspot/cpu/aarch64部分就成了64-bit ARM architecture的默认实现

- [341: Default CDS Archives](https://link.segmentfault.com/?enc=6TULDpX4%2BMSmE0vTwy%2BvdA%3D%3D.%2FISn3OVw93W5ZJgdZW8SaiM61nOG0Yk8KGuLQTkhp9sKwmgTKx2CWb4lVv0gAslP)

  > java10的新特性[JEP 310: Application Class-Data Sharing](https://link.segmentfault.com/?enc=9WP34ACQq1yy%2FfYNe0HaAw%3D%3D.9jKcpQRc9n%2B%2FXy6kxTP9bLQ9vZ%2Ba9D0HKHWNZQ7m6WEZYW9OSKL2KY%2FpcE4YiLbg)扩展了JDK5引入的Class-Data Sharing，支持application的Class-Data Sharing；Class-Data Sharing可以用于多个JVM共享class，提升启动速度，最早只支持system classes及serial GC，JDK9对其进行扩展以支持application classes及其他GC算法，并在JDK10中开源出来(`以前是commercial feature`)；JDK11将-Xshare:off改为默认-Xshare:auto，以更加方便使用CDS特性；JDK12的这个特性即在64-bit平台上编译jdk的时候就默认在${JAVA_HOME}/lib/server目录下生成一份名为classes.jsa的默认archive文件(`大概有18M`)方便大家使用

- [344: Abortable Mixed Collections for G1](https://link.segmentfault.com/?enc=ymOMI%2B5HibOdcpFuEknqUQ%3D%3D.nOk4j7ehoQl%2FmBnNfi1wytkNKGW0Xa0b2GN%2FxyUmKRpsNVIGYSkdHYBjVIWea%2Fgr)

  > G1在garbage collection的时候，一旦确定了collection set(`CSet`)开始垃圾收集这个过程是without stopping的，当collection set过大的时候，此时的STW时间会过长超出目标pause time，这种情况在mixed collections时候比较明显。这个特性启动了一个机制，当选择了一个比较大的collection set，允许将其分为mandatory及optional两部分(`当完成mandatory的部分，如果还有剩余时间则会去处理optional部分`)来将mixed collections从without stopping变为abortable，以更好满足指定pause time的目标

- [346: Promptly Return Unused Committed Memory from G1](https://link.segmentfault.com/?enc=FPibxUzYVkM6IMMuSlD4FQ%3D%3D.Af4pMm%2FKOTBahE6Xyp5oZHaHkyyfHmM%2BJqqN2bAt%2BOlriP8YObUBGxjIOvSYf1rC)

  > G1目前只有在full GC或者concurrent cycle的时候才会归还内存，由于这两个场景都是G1极力避免的，因此在大多数场景下可能不会及时会还committed Java heap memory给操作系统。JDK12的这个特性新增了两个参数分别是G1PeriodicGCInterval及G1PeriodicGCSystemLoadThreshold，设置为0的话，表示禁用。当上一次garbage collection pause过去G1PeriodicGCInterval(`milliseconds`)时间之后，如果getloadavg()(`one-minute`)低于G1PeriodicGCSystemLoadThreshold指定的阈值，则触发full GC或者concurrent GC(`如果开启G1PeriodicGCInvokesConcurrent`)，GC之后Java heap size会被重写调整，然后多余的内存将会归还给操作系统

## 细项解读

上面列出的是大方面的特性，除此之外还有一些api的更新及废弃，主要见[JDK 12 Release Notes](https://link.segmentfault.com/?enc=7q3BQlnkK2j4P8JUHcZG5A%3D%3D.cziQIdwwp%2BqkF%2Fzg30khFv4hwbRpY%2F1TFnJp2XXvA8%2BqnkGGTIPhFlVHTFeVd4I7)，这里举几个例子。

### 添加项

- 支持unicode 11

- 支持Compact Number Formatting

  > 使用实例如下

  ```pgsql
    @Test
    public void testCompactNumberFormat(){
        var cnf = NumberFormat.getCompactNumberInstance(Locale.CHINA, NumberFormat.Style.SHORT);
        System.out.println(cnf.format(1_0000));
        System.out.println(cnf.format(1_9200));
        System.out.println(cnf.format(1_000_000));
        System.out.println(cnf.format(1L << 30));
        System.out.println(cnf.format(1L << 40));
        System.out.println(cnf.format(1L << 50));
    }
  ```

  输出

  ```yaml
  1万
  2万
  100万
  11亿
  1兆
  1126兆
  ```

- String支持transform、indent操作

  ```typescript
    @Test
    public void testStringTransform(){
        System.out.println("hello".transform(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.hashCode();
            }
        }));
    }
  
    @Test
    public void testStringIndent(){
        System.out.println("hello".indent(3));
    }
  ```

- Files新增mismatch方法

  ```pgsql
    @Test
    public void testFilesMismatch() throws IOException {
        FileWriter fileWriter = new FileWriter("/tmp/a.txt");
        fileWriter.write("a");
        fileWriter.write("b");
        fileWriter.write("c");
        fileWriter.close();
  
        FileWriter fileWriterB = new FileWriter("/tmp/b.txt");
        fileWriterB.write("a");
        fileWriterB.write("1");
        fileWriterB.write("c");
        fileWriterB.close();
  
        System.out.println(Files.mismatch(Path.of("/tmp/a.txt"),Path.of("/tmp/b.txt")));
    }
  ```

- Collectors新增teeing方法用于聚合两个downstream的结果

  ```reasonml
    @Test
    public void testCollectorTeeing(){
        var result = Stream.of("Devoxx","Voxxed Days","Code One","Basel One")
                .collect(Collectors.teeing(Collectors.filtering(n -> n.contains("xx"),Collectors.toList()),
                                            Collectors.filtering(n -> n.endsWith("One"),Collectors.toList()),
                        (List<String> list1, List<String> list2) -> List.of(list1,list2)
                                            ));
  
        System.out.println(result.get(0));
        System.out.println(result.get(1));
    }
  ```

- CompletionStage新增exceptionallyAsync、exceptionallyCompose、exceptionallyComposeAsync方法

  ```livescript
    @Test
    public void testExceptionallyAsync() throws ExecutionException, InterruptedException {
        LOGGER.info("begin");
        int result = CompletableFuture.supplyAsync(() -> {
            LOGGER.info("calculate");
            int i = 1/0;
            return 100;
        }).exceptionallyAsync((t) -> {
            LOGGER.info("error error:{}",t.getMessage());
            return 0;
        }).get();
  
        LOGGER.info("result:{}",result);
    }
  ```

- JDK12之前CompletionStage只有一个exceptionally，该方法体在主线程执行，JDK12新增了exceptionallyAsync、exceptionallyComposeAsync方法允许方法体在异步线程执行，同时新增了exceptionallyCompose方法支持在exceptionally的时候构建新的CompletionStage

- Allocation of Old Generation of Java Heap on Alternate Memory Devices

  > G1及Parallel GC引入experimental特性，允许将old generation分配在诸如NV-DIMM memory的alternative memory device

- ZGC: Concurrent Class Unloading

  > ZGC在JDK11的时候还不支持class unloading，JDK12对ZGC支持了Concurrent Class Unloading，默认是开启，使用-XX:-ClassUnloading可以禁用

- 新增-XX:+ExtensiveErrorReports

  > -XX:+ExtensiveErrorReports可以用于在jvm crash的时候收集更多的报告信息到hs_err<pid>.log文件中，product builds中默认是关闭的，要开启的话，需要自己添加-XX:+ExtensiveErrorReports参数

- 新增安全相关的改进

  > 支持java.security.manager系统属性，当设置为disallow的时候，则不使用SecurityManager以提升性能，如果此时调用System.setSecurityManager则会抛出UnsupportedOperationException
  > keytool新增-groupname选项允许在生成key pair的时候指定一个named group
  > 新增PKCS12 KeyStore配置属性用于自定义PKCS12 keystores的生成
  > Java Flight Recorder新增了security-related的event
  > 支持ChaCha20 and Poly1305 TLS Cipher Suites

- jdeps Reports Transitive Dependences

  > jdeps的--print-module-deps, --list-deps, 以及--list-reduce-deps选项得到增强，新增--no-recursive用于non-transitive的依赖分析，--ignore-missing-deps用于suppress missing dependence errors

### 移除项

- 移除com.sun.awt.SecurityWarnin
- 移除FileInputStream、FileOutputStream、Java.util.ZipFile/Inflator/Deflator的finalize方法
- 移除GTE CyberTrust Global Root
- 移除javac的-source, -target对6及1.6的支持，同时移除--release选项

### 废弃项

- 废弃的API列表见[deprecated-list](https://link.segmentfault.com/?enc=H7NWSHRlIrplEtZJWe9wjQ%3D%3D.2LtTjpPt%2FEsWw9PdQ3V0T5P2LzJV2s3K5PnqaKqMvKjzgKlvITKaFrRig7siIhra62dIpP1v1tI8XPbnz%2FQX9%2BLPbvTGkHV9clgSwwOR4lg%3D)
- 废弃-XX:+/-MonitorInUseLists选项
- 废弃Default Keytool的-keyalg值

### 已知问题

- Swing不支持GTK+ 3.20及以后的版本
- 在使用JVMCI Compiler(`比如Graal`)的时候，JVMTI的can_pop_frame及can_force_early_return的capabilities是被禁用的

### 其他事项

- 如果用户没有指定user.timezone且从操作系统获取的为空，那么user.timezone属性的初始值为空变为null
- java.net.URLPermission的行为发生轻微变化，以前它会忽略url中的query及fragment部分，这次改动新增query及fragment部分，即`scheme : // authority [ / path ]`变动为`scheme : // authority [ / path ] [ ignored-query-or-fragment ]`
- javax.net.ssl.SSLContext API及Java Security Standard Algorithm Names规范移除了必须实现TLSv1及TLSv1.1的规定

## 小结

- java12不是LTS(`Long-Term Support`)版本(`oracle版本才有LTS`)，oracle对该版本的support周期为6个月。这个版本主要有几个更新点，一个是语法层更新，一个是API层面的更新，另外主要是GC方面的更新。
- 语法层面引入了preview版本的Switch Expressions；API层面引入了JVM Constants API，引入CompactNumberFormat，让NumberFormat支持COMPACTSTYLE，对String、Files、Collectors、CompletionStage等新增方法；GC方面引入了experimental版本的Shenandoah GC，不过oracle build的openjdk没有enable Shenandoah GC support；另外主要对ZGC及G1 GC进行了改进
- 其中JDK12对ZGC支持了Concurrent Class Unloading，默认是开启，使用-XX:-ClassUnloading可以禁用；对于G1 GC则新增支持Abortable Mixed Collections以及Promptly Return Unused Committed Memory特性

## doc

- [openjdk 12](https://link.segmentfault.com/?enc=eAEGovZ0kj%2BNo3QAH8gByg%3D%3D.oian5uUnStfkgKOgNbMLuPSGBY85Vl2e5CsKlZmtSCt%2FfFSAjdGIz%2FnA%2Ftkwvo%2BK)
- [JDK 12 Release Notes](https://link.segmentfault.com/?enc=csjHMbqenCRO5ETqk83ESw%3D%3D.TN3pe44nHc8mj%2B24aPRDU%2BhvpUnRGLDGkkySZLHkvFngL9WSFk9mCadMUENDyPzp)