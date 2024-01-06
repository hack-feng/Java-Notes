# JVM 内存结构 #

虚拟机在执行 Java 程序的过程中会把所管理的内存划分为若干个不同的数据区域：

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414311066.jpeg) 
图 1 JVM内存结构

 *  **方法区**：与堆一样，是**各个线程共享**的内存区域，用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据
    
     *  HotSpot 虚拟机把方法区叫做永久代（Permanent Generation）
     *  在 jdk1.7 中，符号引用放到 native heap，字符串常量、类的静态变量放到 java heap
     *  在 jdk1.8 中，永久代被移除，**类的元数据**放到本地堆内存（native heap）中，这一块区域叫 **Metaspace**（元空间）
 *  运行时常量池：方法区的一部分，用于存放编译器生成的各种字面量和符号引用，这部分内容将在类加载后存放到方法区的运行时常量池中
 *  **堆**：被**所有线程共享**的一块内存区域，在虚拟机启动时创建。所有的**对象实例**以及**数组**都要在堆上分配内存
    
     *  使用 new 关键字，就表示在堆中开辟一块新的存储空间
     *  堆内存由年轻代/新生代和老年代组成，而年轻代又被分成三部分，Eden 空间、From Survivor 空间、To Survivor 空间，默认情况下年轻代按照 8:1:1 的比例来分配
     *  堆内存又被划分成不同的部分：伊甸区（Eden）、幸存者区域（Survivor Sapce）、老年代（Old Generation Space）
 *  **Java 虚拟机栈**：每个线程在创建时都会创建一个虚拟机栈，其内部保存一个个的栈帧，每个**方法**被执行时都会同时创建一个**栈帧**用于存储局部变量表、操作栈、动态链接、方法出口等信息。当方法调用完毕，该方法的栈帧就被销毁了
 *  本地方法栈：每个线程在调用本地方法时都会创建一个栈帧，为虚拟机使用到的 native 方法服务
 *  程序计数器：每个线程都有自己的程序计数器，用来记录当前线程正在执行的字节码指令的地址
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414311976.png) 
    图 2 Java Memory Model

# JVM 常见参数 #

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414312527.png) 
图 3 JVM内存各区域内存大小的控制参数

 *  \-Xms 设置堆的最小空间大小，默认为物理内存的 1/64
 *  \-Xmx 设置堆的最大空间大小，默认为物理内存的 1/4
 *  \-Xss 设置每个线程的堆栈大小，jdk1.5 以后默认为 1M
 *  \-Xdebug 在调试模式下运行
 *  \-XX:NewSize 设置新生代最小空间大小
 *  \-XX:MaxNewSize 设置新生代最大空间大小
 *  \-XX:PermSize 设置永久代最小空间大小
 *  \-XX:MaxPermSize 设置永久代最大空间大小
 *  \-XX:NewRatio 设置老年代与新生代的比值，默认值为 3
 *  \-XX:SurvivorRatio 设置年轻代中 Eden 区与 2 个 Survivor 区的比值
 *  \-XX:MaxTenuringThreshold 表示一个对象如果在 Survivor 区移动的次数达到设置值还没有被垃圾回收就进入老年代（如果设置为 0 的话，则年轻代对象不经过 Survivor 区，直接进入老年代）
 *  \-XX:PretenureSizeThreshold 直接晋升到老年代的对象大小
 *  \-XX:+PrintGCDetails 输出详细的 GC 处理日志
 *  \-XX:+HeapDumpOnOutOfMemoryError 让 JVM 碰到 OOM 场景时输出 dump 信息

# GC 算法 #

## 对象存活判断 ##

判断对象是否存活一般有两种方式：

 *  引用计数：每个对象有一个引用计数属性，新增一个引用时计数加 1，引用释放时计数减 1，计数为 0 时可以回收（Java 并没有选择引用计数，因为存在对象相互循环引用的问题）
 *  可达性分析（Reachability Analysis）：从 GC Roots 开始向下搜索，搜索所走过的路径称为引用链，当一个对象到 GC Roots 没有任何引用链相连时，则认为此对象是不可达的（JVM 会把虚拟机栈和本地方法栈中正在引用的对象、静态属性引用的对象和常量，作为 GC Roots）

## 垃圾回收机制 ##

 *  自动垃圾回收机制；只能回收堆内存中不再被程序引用的对象所占的内存
 *  回收内容：堆中的可回收对象；方法区无用的元数据（卸载不再使用的类型，默认 -XX:+ClassUnloadingWithConcurrentMark）

## 垃圾收集算法 ##

 *  垃圾收集算法主要有：复制、标记-清除、标记-整理
 *  复制算法（Copying）
    
     *  将内存分为大小相等的两块，每次只用其中一块，一块内存用完之后，将存活对象复制到另一块内存区域，然后将原来的半块内存区城全部回收
     *  只需移动堆顶指针，按顺序分配内存即可，实现简单，运行高效，且内存回收后不会产生内存碎片
     *  缺点：可用内存缩小为原来的一半，代价高
 *  标记-清除算法（Mark-Sweep）
    
     *  首先标记出所有需要回收的对象，然后进行清除
     *  缺点：标记和清除过程的效率都不高；内存回收后会产生大量不连续的内存碎片
 *  标记-整理算法（Mark-Compact）
    
     *  标记需要回收的对象，将存活对象移动到一端，然后将端边界以外的内存回收
 *  分代收集算法（Generational Collection）
    
     *  GC 分代的基本假设：绝大部分对象的生命周期都非常短暂，存活时间短
     *  把 Java 堆分为新生代和老年代，根据各个年代的特点采用最适当的收集算法
     *  在新生代中，每次垃圾收集时都发现有大批对象死去，只有少量存活，选用复制算法，只需要付出少量存活对象的复制成本就可以完成收集；而老年代中因为对象存活率高、没有额外空间对它进行分配担保，必须使用“标记-清理”或“标记-整理”算法来进行回收

## 垃圾收集过程 ##

 *  当 Eden 区的空间占用达到一定阈值时，触发 Minor GCEden 区中所有存活的对象都会被复制到 To 区域，而在 From 区中仍存活的对象会根据它们的年龄值来决定去向，年龄达到一定值（年龄阈值）的对象会被移动到老年代中，没有达到阈值的对象会被复制到 To 区域
 *  当老年代的剩余空间不足的时，触发 Major GC
 *  老年代 GC 叫作 Major GC，将对整个堆进行的清理叫作 Full GC

# 垃圾收集器 Garbage Collector #

 *  Serial 收集器：一个采用单个线程并基于复制算法工作在新生代的收集器，进行垃圾收集时，必须暂停其它所有的工作线程（Stop The World），是 Client 模式下 JVM 的默认选项，-XX:+UseSerialGC
 *  Serial Old 收集器：一个采用单线程基于标记-整理算法并工作在老年代的收集器
 *  ParNew 收集器：Serial 收集器的多线程版本（使用多个线程进行垃圾收集），-XX:+UseParNewGC
 *  CMS 收集器（Concurrent Mark Sweep）：一种以尽量减少停顿时间为目标的收集器，工作在老年代，基于标记-清除算法实现，-XX:+UseConcMarkSweepGC
 *  Parallel Scavenge 收集器：一个采用多线程基于复制算法并工作在新生代的收集器，也被称作是吞吐量优先的 GC，是早期 jdk1.8 等版本中 Server 模式 JVM 的默认 GC 选择，-XX:+UseParallelGC，使用 Parallel Scavenge（年轻代）+ Serial Old（老年代）的组合进行 GC
 *  Parallel Old 收集器：一个采用多线程基于标记-整理算法并工作在老年代的收集器，适用于注重于吞吐量及 CPU 资源敏感的场合，-XX:+UseParallelOldGC，使用 Parallel Scavenge（年轻代）+ Parallel Old（老年代）的组合进行 GC
 *  G1 收集器：jdk1.7 提供的一个工作在新生代和老年代的收集器，基于标记-整理算法实现，在收集结束后可以避免内存碎片问题，一种兼顾吞吐量和停顿时间的 GC，是 Oracle jdk1.9 以后的默认 GC 选项
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414313138.png) 
    图 4 常用的垃圾收集器组合

 *  jdk1.7 默认垃圾收集器 Parallel Scavenge（新生代）+ Serial Old（老年代）
 *  jdk1.8 默认垃圾收集器 Parallel Scavenge（新生代）+ Serial Old（老年代）
 *  jdk1.9 默认垃圾收集器 G1

> 
> \-XX:+PrintCommandLineFlags 查看 JVM 设置的 XX 选项的值
> \-XX:+PrintGCDetails 查看 GC 日志的新生代、老年代名称判断
> 

# Java 内存模型 #

Java 内存模型（Java Memory Model，JMM）是一个抽象的概念，描述了一组规则或规范，定义了程序中各个共享变量的访问规则

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414313735.png) 
图 5 Java内存模型

 *  JMM 规定了所有的变量都存储在主内存（Main Memory）中
 *  每个线程还有自己的工作内存（Working Memory），线程的工作内存中保存了该线程使用到的变量的主内存的副本拷贝，线程对变量的所有操作（读取、赋值等）都必须在工作内存中进行，而不能直接读写主内存中的变量（volatile 变量仍然有工作内存的拷贝，但是由于它特殊的操作顺序性规定，所以看起来如同直接在主内存中读写访问一般）
 *  不同的线程之间也无法直接访问对方工作内存中的变量，线程之间值的传递都需要通过主内存来完成

# Java 对象模型 #

 *  Java 对象在 JVM 中的存储结构
 *  HotSpot 虚拟机中，设计了一个 OOP-Klass Model，OOP（Ordinary Object Pointer）指的是普通对象指针，而 Klass 用来描述对象实例的具体类型
 *  每一个 Java 类，在被 JVM 加载的时候，JVM 会给这个类创建一个 instanceKlass，保存在方法区，用来在 JVM 层表示该 Java 类。当我们在 Java 代码中，使用 new 创建一个对象的时候，JVM 会创建一个 instanceOopDesc 对象，这个对象中包含了对象头以及实例数据
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414314328.jpeg) 
    图 6 Java对象模型

# JVM 性能监控与故障处理工具 #

## JDK 监控和故障处理命令行工具 ##

 *  jps：JVM Process Status Tool，显示指定系统内所有的 HotSpot 虚拟机进程
 *  jcmd：发送诊断命令请求到正在运行的 JVM
 *  jstat：JVM Statistics Monitoring Tool，用于收集 HotSpot 虚拟机各方面的运行数据
 *  jstack：Stack Trace for Java，显示虚拟机的线程快照
 *  jinfo：Configuration Info for Java，显示虚拟机配置信息
 *  jmap：Memory Map for Java，生成虚拟机的内存转储快照（heapdump 文件）
 *  jhat：JVM Heap Dump Browser，用于分析 heapdump 文件，它会建立一个 HTTP/HTML 服务器，让用户可以在浏览器上査看分析结果

## JDK 的可视化工具 ##

 *  JConsole：Java 监视和管理控制台
 *  JMC（Java Mission Control）
 *  Visual VM：多合一故障处理工具


[JVM]: https://static.sitestack.cn/projects/sdky-java-note/190d5c837dc230f4f3eba93f9ea8d0bc.jpeg
[Java Memory Model]: https://static.sitestack.cn/projects/sdky-java-note/b90e69db76c706cc308be9af176bd2a4.png
[JVM 1]: https://static.sitestack.cn/projects/sdky-java-note/4fdd77c5fe604afb09f411495aee8517.png
[8004ec22905ad92a2bc59cf726bb1e47.png]: https://static.sitestack.cn/projects/sdky-java-note/8004ec22905ad92a2bc59cf726bb1e47.png
[Java]: https://static.sitestack.cn/projects/sdky-java-note/edc67a822ae1a18ff7a2a443258dd749.png
[Java 1]: https://static.sitestack.cn/projects/sdky-java-note/be75f15a0c34eafc46129bdf9573a4bb.jpeg