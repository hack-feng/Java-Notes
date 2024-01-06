## 序

本文主要讲Java7的新特性，相对于Java6而言，Java7增加了一些重要的特性，比如NIO2，不像Java6那么鸡肋，也算是一个重要的版本。

## 特性列表

- suppress异常(`新语法`)

- 捕获多个异常(新语法)

- try-with-resources(新语法)

- JSR341-Expression Language Specification(新规范)

- JSR203-More New I/O APIs for the Java Platform(新规范)

- JSR292与InvokeDynamic

- 支持JDBC4.1规范

- Path接口、DirectoryStream、Files、WatchService

- jcmd

- fork/join framework

- Java Mission Control

  ### 1、suppress异常(`新语法`)

  ```java
  /**
   * 记录异常，不被淹没
   * addSuppressed
   */
  class ReadFile {
    public void read(String filename) throws BaseException {
        FileInputStream input = null;
        IOException readException = null;
        try {
            input = new FileInputStream(filename);
        } catch (IOException ex) {
            readException = ex;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    if (readException == null) {
                        readException = ex;
                    }else{
                        //使用java7的
                        readException.addSuppressed(ex);
                    }
                }
            }
            if (readException != null) {
                throw new BaseException(readException);
            }
        }
    }
  }
  ```

  ### 2、捕获多个异常(`新语法`)

  ```java
  public void handle() {
        ExceptionThrower thrower = new ExceptionThrower();
        try {
            thrower.manyExceptions();
        } catch (ExceptionA | ExceptionB ab) {
            System.out.println(ab.getClass());
        } catch (ExceptionC c) {
        }
    }
  ```

  ### 3、try-with-resources(`新语法`)

  ```java
  /**
   * try-with-resource
   * 不需要使用finally来保证打开的流被正确关闭
   * 这是自动完成的。
   * @created 2014-07-21
   */
  public class ResourceBasicUsage {
    public String readFile(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(String.format("%n"));
            }
            return builder.toString();
        }
    }
  }
  ```

  实现AutoCloseable

  ```java
  /**
   * @created 2014-07-21
   */
  public class CustomResource  implements AutoCloseable {
    public void close() throws Exception {
        System.out.println("进行资源释放。");
    }
    public void useCustomResource() throws Exception {
        try (CustomResource resource = new CustomResource())  {
            System.out.println("使用资源。");
        }
    }
    public static void main(String[] args) {
        try {
            new CustomResource().useCustomResource();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
  }
  ```

  ### 4、JSR341-Expression Language Specification(`新规范`)

  ```java
  public static void main(String[] args){
        ELProcessor el = new ELProcessor();
        assert (el.eval("Math.random()") instanceof Double);
    }
  ```

  ### 5、JSR203-More New I/O APIs for the Java Platform(`新规范`)

- bytebuffer

  ```java
  public class ByteBufferUsage {
    public void useByteBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.put((byte)1);
        buffer.put(new byte[3]);
        buffer.putChar('A');
        buffer.putFloat(0.0f);
        buffer.putLong(10, 100L);
        System.out.println(buffer.getChar(4));
        System.out.println(buffer.remaining());
    }
    public void byteOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(1);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.getInt(0); //值为16777216
    }
    public void compact() {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.put(new byte[16]);
        buffer.flip();
        buffer.getInt();
        buffer.compact();
        int pos = buffer.position();
    }
    public void viewBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.putInt(1);
        IntBuffer intBuffer = buffer.asIntBuffer();
        intBuffer.put(2);
        int value = buffer.getInt(); //值为2
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ByteBufferUsage bbu = new ByteBufferUsage();
        bbu.useByteBuffer();
        bbu.byteOrder();
        bbu.compact();
        bbu.viewBuffer();
    }
  }
  ```

- filechannel

  ```java
  public class FileChannelUsage {
    public void openAndWrite() throws IOException {
        FileChannel channel = FileChannel.open(Paths.get("my.txt"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.putChar('A').flip();
        channel.write(buffer);
    }
    public void readWriteAbsolute() throws IOException {
        FileChannel channel = FileChannel.open(Paths.get("absolute.txt"), StandardOpenOption.READ, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        ByteBuffer writeBuffer = ByteBuffer.allocate(4).putChar('A').putChar('B');
        writeBuffer.flip();
        channel.write(writeBuffer, 1024);
        ByteBuffer readBuffer = ByteBuffer.allocate(2);
        channel.read(readBuffer, 1026);
        readBuffer.flip();
        char result = readBuffer.getChar(); //值为'B'
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        FileChannelUsage fcu = new FileChannelUsage();
        fcu.openAndWrite();
        fcu.readWriteAbsolute();
    }
  }
  ```

  ### 6、JSR292与InvokeDynamic

  JSR 292: Supporting Dynamically Typed Languages on the JavaTM Platform，支持在JVM上运行动态类型语言。在字节码层面支持了InvokeDynamic。

- 方法句柄MethodHandle

  ```java
  public class ThreadPoolManager {
    private final ScheduledExecutorService stpe = Executors
            .newScheduledThreadPool(2);
    private final BlockingQueue<WorkUnit<String>> lbq;
    public ThreadPoolManager(BlockingQueue<WorkUnit<String>> lbq_) {
        lbq = lbq_;
    }
    public ScheduledFuture<?> run(QueueReaderTask msgReader) {
        msgReader.setQueue(lbq);
        return stpe.scheduleAtFixedRate(msgReader, 10, 10, TimeUnit.MILLISECONDS);
    }
    private void cancel(final ScheduledFuture<?> hndl) {
        stpe.schedule(new Runnable() {
            public void run() {
                hndl.cancel(true);
            }
        }, 10, TimeUnit.MILLISECONDS);
    }
    /**
     * 使用传统的反射api
     */
    public Method makeReflective() {
        Method meth = null;
        try {
            Class<?>[] argTypes = new Class[]{ScheduledFuture.class};
            meth = ThreadPoolManager.class.getDeclaredMethod("cancel", argTypes);
            meth.setAccessible(true);
        } catch (IllegalArgumentException | NoSuchMethodException
                | SecurityException e) {
            e.printStackTrace();
        }
        return meth;
    }
    /**
     * 使用代理类
     * @return
     */
    public CancelProxy makeProxy() {
        return new CancelProxy();
    }
    /**
     * 使用Java7的新api，MethodHandle
     * invoke virtual 动态绑定后调用 obj.xxx
     * invoke special 静态绑定后调用 super.xxx
     * @return
     */
    public MethodHandle makeMh() {
        MethodHandle mh;
        MethodType desc = MethodType.methodType(void.class, ScheduledFuture.class);
        try {
            mh = MethodHandles.lookup().findVirtual(ThreadPoolManager.class,
                    "cancel", desc);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw (AssertionError) new AssertionError().initCause(e);
        }
        return mh;
    }
    public static class CancelProxy {
        private CancelProxy() {
        }
        public void invoke(ThreadPoolManager mae_, ScheduledFuture<?> hndl_) {
            mae_.cancel(hndl_);
        }
    }
  }
  ```

- 调用

  ```java
  public class ThreadPoolMain {
    /**
     * 如果被继承，还能在静态上下文寻找正确的class
     */
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ThreadPoolManager manager;
    public static void main(String[] args) {
        ThreadPoolMain main = new ThreadPoolMain();
        main.run();
    }
    private void cancelUsingReflection(ScheduledFuture<?> hndl) {
        Method meth = manager.makeReflective();
        try {
            System.out.println("With Reflection");
            meth.invoke(hndl);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    private void cancelUsingProxy(ScheduledFuture<?> hndl) {
        CancelProxy proxy = manager.makeProxy();
        System.out.println("With Proxy");
        proxy.invoke(manager, hndl);
    }
    private void cancelUsingMH(ScheduledFuture<?> hndl) {
        MethodHandle mh = manager.makeMh();
        try {
            System.out.println("With Method Handle");
            mh.invokeExact(manager, hndl);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    private void run() {
        BlockingQueue<WorkUnit<String>> lbq = new LinkedBlockingQueue<>();
        manager = new ThreadPoolManager(lbq);
        final QueueReaderTask msgReader = new QueueReaderTask(100) {
            @Override
            public void doAction(String msg_) {
                if (msg_ != null)
                    System.out.println("Msg recvd: " + msg_);
            }
        };
        ScheduledFuture<?> hndl = manager.run(msgReader);
        cancelUsingMH(hndl);
        // cancelUsingProxy(hndl);
        // cancelUsingReflection(hndl);
    }
  }
  ```

  ### 7、支持JDBC4.1规范

- abort方法

  ```java
  public class AbortConnection {
    public void abortConnection() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:derby://localhost/java7book");
        ThreadPoolExecutor executor = new DebugExecutorService(2, 10, 60,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        connection.abort(executor);
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES);
            System.out.println(executor.getCompletedTaskCount());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static class DebugExecutorService extends ThreadPoolExecutor {
        public DebugExecutorService(int corePoolSize, int maximumPoolSize,
                long keepAliveTime, TimeUnit unit,
                BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }
        public void beforeExecute(Thread t, Runnable r) {
            System.out.println("清理任务：" + r.getClass());
            super.beforeExecute(t, r);
        }
    }
    public static void main(String[] args) {
        AbortConnection ca = new AbortConnection();
        try {
            ca.abortConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  }
  ```

- 自动关闭

  ```java
  public class SetSchema {
    public void setSchema() throws SQLException {
        try (Connection connection = DriverManager
                .getConnection("jdbc:derby://localhost/java7book")) {
            connection.setSchema("DEMO_SCHEMA");
            try (Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM author")) {
                while (rs.next()) {
                    System.out.println(rs.getString("name"));
                }
            }
        }
    }
    public static void main(String[] args) {
        SetSchema ss = new SetSchema();
        try {
            ss.setSchema();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  }
  ```

- 自动映射

  ```java
  public class UseSQLData {
    
    public void useSQLData() throws SQLException {
        try (Connection connection = DriverManager
                .getConnection("jdbc:derby://localhost/java7book")) {
            Map<String,Class<?>> typeMap = new HashMap<String,Class<?>>();
            typeMap.put("java7book.Book", Book.class);
            try (Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM book")) {
                while (rs.next()) {
                    System.out.println(rs.getObject(1, Book.class));
                }
            }
        }
    }
    
    public static void main(String[] args) {
        UseSQLData usd = new UseSQLData();
        try {
            usd.useSQLData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  }
  ```

  ### 8、Path接口(`重要接口更新`)

  ```java
  public class PathUsage {
    public void usePath() {
        Path path1 = Paths.get("folder1", "sub1");
        Path path2 = Paths.get("folder2", "sub2");
        path1.resolve(path2); //folder1\sub1\folder2\sub2
        path1.resolveSibling(path2); //folder1\folder2\sub2
        path1.relativize(path2); //..\..\folder2\sub2
        path1.subpath(0, 1); //folder1
        path1.startsWith(path2); //false
        path1.endsWith(path2); //false
        Paths.get("folder1/./../folder2/my.text").normalize(); //folder2\my.text
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PathUsage usage = new PathUsage();
        usage.usePath();
    }
  }
  ```

  ### 9、DirectoryStream

  ```java
  public class ListFile {
    public void listFiles() throws IOException {
        Path path = Paths.get("");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.*")) {
            for (Path entry: stream) {
                //使用entry
                System.out.println(entry);
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ListFile listFile = new ListFile();
        listFile.listFiles();
    }
  }
  ```

### 10、Files

```java
public class FilesUtils {
    public void manipulateFiles() throws IOException {
        Path newFile = Files.createFile(Paths.get("new.txt").toAbsolutePath());
        List<String> content = new ArrayList<String>();
        content.add("Hello");
        content.add("World");
        Files.write(newFile, content, Charset.forName("UTF-8"));
        Files.size(newFile);
        byte[] bytes = Files.readAllBytes(newFile);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Files.copy(newFile, output);
        Files.delete(newFile);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        FilesUtils fu = new FilesUtils();
        fu.manipulateFiles();
    }
}
```

### 11、WatchService

```java
public class WatchAndCalculate {
    public void calculate() throws IOException, InterruptedException {
        WatchService service = FileSystems.getDefault().newWatchService();
        Path path = Paths.get("").toAbsolutePath();
        path.register(service, StandardWatchEventKinds.ENTRY_CREATE);
        while (true) {
            WatchKey key = service.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                Path createdPath = (Path) event.context();
                createdPath = path.resolve(createdPath);
                long size = Files.size(createdPath);
                System.out.println(createdPath + " ==> " + size);
            }
            key.reset();
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Throwable {
        WatchAndCalculate wc = new WatchAndCalculate();
        wc.calculate();
    }
}
```

### 12、jcmd utility

jcmd是为了替代jps出现了，包含了jps的大部分功能并新增了一些新的功能。

- jcmd -l
  列出所有的Java虚拟机，针对每一个虚拟机可以使用help列出它们支持的命令。

- org.eclipse.jetty.xml.XmlConfiguration /tmp/start4070493346048555702.properties /opt/educat/apps/conf/jetty8.xml

- sun.tools.jcmd.JCmd -l

- org.apache.flume.node.Application --no-reload-conf -f /opt/educat/flume_agent/conf/flume.conf -n log_agent

- jcmd pid help

  ```shell
  jcmd 15308 help
  15308:
  The following commands are available:
  VM.commercial_features
  ManagementAgent.stop
  ManagementAgent.start_local
  ManagementAgent.start
  Thread.print
  GC.class_histogram
  GC.heap_dump
  GC.run_finalization
  GC.run
  VM.uptime
  VM.flags
  VM.system_properties
  VM.command_line
  VM.version
  help
  For more information about a specific command use 'help <command>'.
  ```

- jcmd pid VM.flags查看启动参数

  ```shell
  jcmd 15308 VM.flags
  15308:
  -XX:+DisableExplicitGC 
  -XX:ErrorFile=/var/codecraft/logs/catapp.vmerr.log.201505071655 
  -XX:+HeapDumpOnOutOfMemoryError 
  -XX:HeapDumpPath=/var/codecraft/logs/catapp.heaperr.log.201505071655 -XX:InitialHeapSize=5368709120 
  -XX:+ManagementServer 
  -XX:MaxGCPauseMillis=100 
  -XX:MaxHeapSize=5368709120 
  -XX:MaxPermSize=268435456 
  -XX:+PrintAdaptiveSizePolicy 
  -XX:+PrintCommandLineFlags 
  -XX:+PrintGC 
  -XX:+PrintGCApplicationStoppedTime 
  -XX:+PrintGCDateStamps 
  -XX:+PrintGCDetails 
  -XX:+PrintGCTimeStamps 
  -XX:+PrintHeapAtGC 
  -XX:+PrintTenuringDistribution 
  -XX:StringTableSize=49999 
  -XX:+UnlockExperimentalVMOptions 
  -XX:+UseCompressedOops 
  -XX:+UseG1GC
  ```

- jcmd pid GC.heap_dump D:\d.dump 导出堆信息

- jcmd pid GC.class_histogram查看系统中类的统计信息

- jcmd pid VM.system_properties查看系统属性内容

- jcmd pid Thread.print 打印线程栈

- jcmd pid VM.uptime 查看虚拟机启动时间

- jcmd pid PerfCounter.print 查看性能统计

  ```
  jcmd 15308 PerfCounter.print
  15308:
  java.ci.totalTime=79326405
  java.cls.loadedClasses=19977
  java.cls.sharedLoadedClasses=0
  java.cls.sharedUnloadedClasses=0
  java.cls.unloadedClasses=1443
  java.property.java.class.path="/opt/educat/apps/server/jetty-distribution-8.1.9.v20130131/lib/jetty-xml-8.1.9.v20130131.jar:/opt/educat/apps/server/jetty-distribution-8.1.9.v20130131/lib/servlet-api-3.0.jar:/opt/educat/apps/server/jetty-distribution-8.1.9.v20130131/lib/jetty-http-8.1.9.v20130131.jar:/opt/educat/apps/server/jetty-distribution-8.1.9.v20130131/lib/jetty-continuation-8.1.9.v20130131.jar:/opt/educat/apps/server/jetty-distribution-8.1.9.v20130131/lib/jetty-server-8.1.9.v20130131.jar:/opt/educat/apps/server/jetty-distribution-8.1.9.v20130131/lib/jetty-security-8.1.9.v20130131.jar:/opt/educat/apps/server/jetty-distribution-8.1.9.v20130131/lib/jetty-servlet-8.1.9.v20130131.jar:/opt/educat/apps/server/jetty-distribution-8.1.9.v20130131/lib/jetty-webapp-8.1.9.v20130131.jar:/opt/educat/apps/server/jetty-distribution-8.1.9.v20130131/lib/jetty-deploy-8.1.9.v20130131.jar:/opt/educat/apps/server/jetty-distribution-8.1.9.v20130131/lib/jetty-servlets-8.1.9.v20130131.jar:/opt/educat/apps/server/jetty-distribution-8.1.9.v20130131/l"
  java.property.java.endorsed.dirs="/usr/local/jdk1.7.0_21/jre/lib/endorsed"
  java.property.java.ext.dirs="/usr/local/jdk1.7.0_21/jre/lib/ext:/usr/java/packages/lib/ext"
  java.property.java.home="/usr/local/jdk1.7.0_21/jre"
  java.property.java.library.path="/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib"
  java.property.java.version="1.7.0_21"
  java.property.java.vm.info="mixed mode"
  java.property.java.vm.name="Java HotSpot(TM) 64-Bit Server VM"
  java.property.java.vm.specification.name="Java Virtual Machine Specification"
  java.property.java.vm.specification.vendor="Oracle Corporation"
  java.property.java.vm.specification.version="1.7"
  java.property.java.vm.vendor="Oracle Corporation"
  java.property.java.vm.version="23.21-b01"
  java.rt.vmArgs="-javaagent:/opt/educat/apps/lib/jolokia-jvm-1.1.0-agent.jar=port=23061 -Xloggc:/var/codecraft/logs/catapp.gc.log.201505071655 -XX:ErrorFile=/var/codecraft/logs/catapp.vmerr.log.201505071655 -XX:HeapDumpPath=/var/codecraft/logs/catapp.heaperr.log.201505071655 -Xmx5g -Xms5g -XX:MaxPermSize=256m -XX:+HeapDumpOnOutOfMemoryError -XX:+DisableExplicitGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintHeapAtGC -XX:+PrintTenuringDistribution -XX:+PrintCommandLineFlags -XX:+PrintAdaptiveSizePolicy -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:StringTableSize=49999 -Djetty.home=/opt/educat/apps/server/jetty-distribution-8.1.9.v20130131 -Dapp.port=8061 -Dmedis_environment=online -Dcore.step=app -DSTOP.PORT=38061 -Djetty.port=8061 -Dcom.sun.management.jmxremote.authenticate=false -Dapp.logdir=/var/codecraft/logs -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector -Dapp.ip=10.64.28.207 -Dapp.cont"
  java.rt.vmFlags=""
  java.threads.daemon=72
  java.threads.live=128
  java.threads.livePeak=129
  java.threads.started=1444
  sun.ci.compilerThread.0.compiles=2595
  sun.ci.compilerThread.0.method=""
  sun.ci.compilerThread.0.time=1290
  sun.ci.compilerThread.0.type=1
  sun.ci.compilerThread.1.compiles=2802
  sun.ci.compilerThread.1.method=""
  sun.ci.compilerThread.1.time=1413
  sun.ci.compilerThread.1.type=2
  sun.ci.lastFailedMethod=""
  sun.ci.lastFailedType=0
  sun.ci.lastInvalidatedMethod=""
  sun.ci.lastInvalidatedType=0
  sun.ci.lastMethod="org/codehaus/groovy/classgen/VariableScopeVisitor checkVariableNameForDeclaration"
  sun.ci.lastSize=2184
  sun.ci.lastType=1
  sun.ci.nmethodCodeSize=12188576
  sun.ci.nmethodSize=24492688
  sun.ci.osrBytes=196694
  sun.ci.osrCompiles=156
  sun.ci.osrTime=8521713
  sun.ci.standardBytes=2072839
  sun.ci.standardCompiles=5241
  sun.ci.standardTime=70804692
  sun.ci.threads=2
  sun.ci.totalBailouts=0
  sun.ci.totalCompiles=5397
  sun.ci.totalInvalidates=0
  sun.classloader.findClassTime=358334873
  sun.classloader.findClasses=507
  sun.classloader.parentDelegationTime=30062667
  sun.cls.appClassBytes=63743816
  sun.cls.appClassLoadCount=58098
  sun.cls.appClassLoadTime=9843833
  sun.cls.appClassLoadTime.self=5288490
  sun.cls.classInitTime=2617049
  sun.cls.classInitTime.self=1088905
  sun.cls.classLinkedTime=4605704
  sun.cls.classLinkedTime.self=541928
  sun.cls.classVerifyTime=4055324
  sun.cls.classVerifyTime.self=2423448
  sun.cls.defineAppClassTime=3206202
  sun.cls.defineAppClassTime.self=386302
  sun.cls.defineAppClasses=16465
  sun.cls.initializedClasses=14546
  sun.cls.isUnsyncloadClassSet=0
  sun.cls.jniDefineClassNoLockCalls=94
  sun.cls.jvmDefineClassNoLockCalls=4405
  sun.cls.jvmFindLoadedClassNoLockCalls=32671
  sun.cls.linkedClasses=16465
  sun.cls.loadInstanceClassFailRate=0
  sun.cls.loadedBytes=43314456
  sun.cls.lookupSysClassTime=87247
  sun.cls.methodBytes=34262690
  sun.cls.nonSystemLoaderLockContentionRate=133
  sun.cls.parseClassTime=3099390
  sun.cls.parseClassTime.self=2670584
  sun.cls.sharedClassLoadTime=9647
  sun.cls.sharedLoadedBytes=0
  sun.cls.sharedUnloadedBytes=0
  sun.cls.sysClassBytes=12986737
  sun.cls.sysClassLoadTime=503885
  sun.cls.systemLoaderLockContentionRate=0
  sun.cls.time=15382336
  sun.cls.unloadedBytes=5087120
  sun.cls.unsafeDefineClassCalls=1555
  sun.cls.verifiedClasses=16383
  sun.gc.cause="No GC"
  sun.gc.collector.0.invocations=85
  sun.gc.collector.0.lastEntryTime=24164511065
  sun.gc.collector.0.lastExitTime=24164628388
  sun.gc.collector.0.name="G1 incremental collections"
  sun.gc.collector.0.time=7628099
  sun.gc.collector.1.invocations=1
  sun.gc.collector.1.lastEntryTime=24543200515
  sun.gc.collector.1.lastExitTime=24544107869
  sun.gc.collector.1.name="G1 stop-the-world full collections"
  sun.gc.collector.1.time=907355
  sun.gc.generation.0.agetable.bytes.00=0
  sun.gc.generation.0.agetable.bytes.01=4294976
  sun.gc.generation.0.agetable.bytes.02=2014880
  sun.gc.generation.0.agetable.bytes.03=5406352
  sun.gc.generation.0.agetable.bytes.04=4875176
  sun.gc.generation.0.agetable.bytes.05=2865952
  sun.gc.generation.0.agetable.bytes.06=4374048
  sun.gc.generation.0.agetable.bytes.07=2058664
  sun.gc.generation.0.agetable.bytes.08=3574376
  sun.gc.generation.0.agetable.bytes.09=6923448
  sun.gc.generation.0.agetable.bytes.10=1541088
  sun.gc.generation.0.agetable.bytes.11=1347376
  sun.gc.generation.0.agetable.bytes.12=735888
  sun.gc.generation.0.agetable.bytes.13=402632
  sun.gc.generation.0.agetable.bytes.14=713272
  sun.gc.generation.0.agetable.bytes.15=728688
  sun.gc.generation.0.agetable.size=16
  sun.gc.generation.0.capacity=4510973976
  sun.gc.generation.0.maxCapacity=5368709144
  sun.gc.generation.0.minCapacity=24
  sun.gc.generation.0.name="young"
  sun.gc.generation.0.space.0.capacity=4510973960
  sun.gc.generation.0.space.0.initCapacity=1128267784
  sun.gc.generation.0.space.0.maxCapacity=5368709128
  sun.gc.generation.0.space.0.name="eden"
  sun.gc.generation.0.space.0.used=580911104
  sun.gc.generation.0.space.1.capacity=8
  sun.gc.generation.0.space.1.initCapacity=8
  sun.gc.generation.0.space.1.maxCapacity=8
  sun.gc.generation.0.space.1.name="s0"
  sun.gc.generation.0.space.1.used=0
  sun.gc.generation.0.space.2.capacity=8
  sun.gc.generation.0.space.2.initCapacity=8
  sun.gc.generation.0.space.2.maxCapacity=5368709128
  sun.gc.generation.0.space.2.name="s1"
  sun.gc.generation.0.space.2.used=0
  sun.gc.generation.0.spaces=3
  sun.gc.generation.1.capacity=857735176
  sun.gc.generation.1.maxCapacity=5368709128
  sun.gc.generation.1.minCapacity=8
  sun.gc.generation.1.name="old"
  sun.gc.generation.1.space.0.capacity=857735176
  sun.gc.generation.1.space.0.initCapacity=4240441352
  sun.gc.generation.1.space.0.maxCapacity=5368709128
  sun.gc.generation.1.space.0.name="space"
  sun.gc.generation.1.space.0.used=155730608
  sun.gc.generation.1.spaces=1
  sun.gc.generation.2.capacity=138412032
  sun.gc.generation.2.maxCapacity=268435456
  sun.gc.generation.2.minCapacity=20971520
  sun.gc.generation.2.name="perm"
  sun.gc.generation.2.space.0.capacity=138412032
  sun.gc.generation.2.space.0.initCapacity=20971520
  sun.gc.generation.2.space.0.maxCapacity=268435456
  sun.gc.generation.2.space.0.name="perm"
  sun.gc.generation.2.space.0.used=138212560
  sun.gc.generation.2.spaces=1
  sun.gc.lastCause="Heap Inspection Initiated GC"
  sun.gc.policy.collectors=1
  sun.gc.policy.desiredSurvivorSize=264241152
  sun.gc.policy.generations=3
  sun.gc.policy.maxTenuringThreshold=15
  sun.gc.policy.name="GarbageFirst"
  sun.gc.policy.tenuringThreshold=15
  sun.gc.tlab.alloc=0
  sun.gc.tlab.allocThreads=0
  sun.gc.tlab.fastWaste=0
  sun.gc.tlab.fills=0
  sun.gc.tlab.gcWaste=0
  sun.gc.tlab.maxFastWaste=0
  sun.gc.tlab.maxFills=0
  sun.gc.tlab.maxGcWaste=0
  sun.gc.tlab.maxSlowAlloc=0
  sun.gc.tlab.maxSlowWaste=0
  sun.gc.tlab.slowAlloc=0
  sun.gc.tlab.slowWaste=0
  sun.management.JMXConnectorServer.0.authenticate="false"
  sun.management.JMXConnectorServer.0.remoteAddress="service:jmx:rmi:///jndi/rmi://edu-cat02.lf.codecraft.com:8199/jmxrmi"
  sun.management.JMXConnectorServer.0.ssl="false"
  sun.management.JMXConnectorServer.0.sslNeedClientAuth="false"
  sun.management.JMXConnectorServer.0.sslRegistry="false"
  sun.management.JMXConnectorServer.address="service:jmx:rmi://127.0.0.1/stub/rO0ABXNyAC5qYXZheC5tYW5hZ2VtZW50LnJlbW90ZS5ybWkuUk1JU2VydmVySW1wbF9TdHViAAAAAAAAAAICAAB4cgAaamF2YS5ybWkuc2VydmVyLlJlbW90ZVN0dWLp/tzJi+FlGgIAAHhyABxqYXZhLnJtaS5zZXJ2ZXIuUmVtb3RlT2JqZWN002G0kQxhMx4DAAB4cHc3AAtVbmljYXN0UmVmMgAADDEwLjY0LjI4LjIwNwAAhbjWmVwaDwiNg3l3YeUAAAFNLZX68oACAHg="
  sun.os.hrt.frequency=1000000
  sun.os.hrt.ticks=24580753795
  sun.perfdata.majorVersion=2
  sun.perfdata.minorVersion=0
  sun.perfdata.overflow=0
  sun.perfdata.size=32768
  sun.perfdata.timestamp=259316
  sun.perfdata.used=17792
  sun.property.sun.boot.class.path="/usr/local/jdk1.7.0_21/jre/lib/resources.jar:/usr/local/jdk1.7.0_21/jre/lib/rt.jar:/usr/local/jdk1.7.0_21/jre/lib/sunrsasign.jar:/usr/local/jdk1.7.0_21/jre/lib/jsse.jar:/usr/local/jdk1.7.0_21/jre/lib/jce.jar:/usr/local/jdk1.7.0_21/jre/lib/charsets.jar:/usr/local/jdk1.7.0_21/jre/lib/jfr.jar:/usr/local/jdk1.7.0_21/jre/classes"
  sun.property.sun.boot.library.path="/usr/local/jdk1.7.0_21/jre/lib/amd64"
  sun.rt._sync_ContendedLockAttempts=297851
  sun.rt._sync_Deflations=438863
  sun.rt._sync_EmptyNotifications=0
  sun.rt._sync_FailedSpins=0
  sun.rt._sync_FutileWakeups=349651
  sun.rt._sync_Inflations=438971
  sun.rt._sync_MonExtant=16256
  sun.rt._sync_MonInCirculation=0
  sun.rt._sync_MonScavenged=0
  sun.rt._sync_Notifications=1580811
  sun.rt._sync_Parks=1935145
  sun.rt._sync_PrivateA=0
  sun.rt._sync_PrivateB=0
  sun.rt._sync_SlowEnter=0
  sun.rt._sync_SlowExit=0
  sun.rt._sync_SlowNotify=0
  sun.rt._sync_SlowNotifyAll=0
  sun.rt._sync_SuccessfulSpins=0
  sun.rt.applicationTime=24559855809
  sun.rt.createVmBeginTime=1430988913170
  sun.rt.createVmEndTime=1430988913429
  sun.rt.internalVersion="Java HotSpot(TM) 64-Bit Server VM (23.21-b01) for linux-amd64 JRE (1.7.0_21-b11), built on Apr  4 2013 04:03:29 by "java_re" with gcc 4.3.0 20080428 (Red Hat 4.3.0-8)"
  sun.rt.interruptedBeforeIO=0
  sun.rt.interruptedDuringIO=0
  sun.rt.javaCommand="org.eclipse.jetty.xml.XmlConfiguration /tmp/start4070493346048555702.properties /opt/educat/apps/conf/jetty8.xml"
  sun.rt.jvmCapabilities="1000000000000000000000000000000000000000000000000000000000000000"
  sun.rt.jvmVersion=387252225
  sun.rt.safepointSyncTime=2333795
  sun.rt.safepointTime=15955181
  sun.rt.safepoints=18365
  sun.rt.threadInterruptSignaled=0
  sun.rt.vmInitDoneTime=1430988913232
  sun.threads.vmOperationTime=9516621
  sun.urlClassLoader.readClassBytesTime=958824201
  sun.zip.zipFile.openTime=72163038
  sun.zip.zipFiles=3838
  ```

  ### 13、fork/join

  Java7提供的一个用于并行执行任务的框架，是一个把大任务分割成若干个小任务，最终汇总每个小任务结果后得到大任务结果的框架。

  ### 14、Java Mission Control

  在JDK7u40里头提供了Java Mission Control，这个是从JRockit虚拟机里头迁移过来的类似JVisualVm的东东。

  ### 15、其他

- Binary Literals支持

- Numeric Literals的下划线支持

- Strings in switch Statements

  ## 参考

- [Java SE 7 Features and Enhancements](https://link.segmentfault.com/?enc=5gYxqUEmVkNqCWhQDc6NEQ%3D%3D.zQVsVUi7ltLghkvXE4qdTPTBKVO5L%2FSoxXiKGFn2TrSrKeKqBNP3CQeHQpNRARuJoG8t%2BwDZvB4YlCAY%2BiNu25x5FEF9GsVKPaEtqopsmKc%3D)