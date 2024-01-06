# JavaBean #

 *  **JavaBean 规范**
    
     *  类使用 `public` 修饰
     *  有 `public` 修饰的**无参构造器**
     *  包含属性的操作方法，即有 `public` 修饰的 getter/setter 方法
 *  boolean 类型的字段没有对应的 getter 方法，取而代之是 **is 方法**，如 isMan
 *  JavaBean：符合 JavaBean 规范的 Java 类，是可重复使用组件
 *  成员：属性（property）、方法（method）、事件（event）
 *  JavaBean 的属性由 getter/setter 方法所决定，说某个类有 age 属性，意味着该类包含 setAge() 或 getAge() 方法
 *  通常作为 DTO（数据传输对象），用来封装值对象，在各层之间传递数据

# 内省机制 #

 *  用于获取和操作 JavaBean 中的成员信息
 *  java.beans 包下

### Introspector 类 ###

 *  通过分别分析 bean 的类和父类，寻找显式或隐式信息，使用这些信息构建一个**全面描述目标 bean** 的 BeanInfo 对象
 *  类方法`BeanInfo getBeanInfo(Class<?> beanClass)`：在 JavaBean 上进行内省，获取其所有成员信息（包括继承的）`BeanInfo getBeanInfo(Class<?> beanClass, Class<?> stopClass)`：在给定的“断”点之下，在 JavaBean 上进行内省，获取其所有成员信息

### BeanInfo 接口 ###

 *  `PropertyDescriptor[] getPropertyDescriptors()`：获取其中的属性描述器
 *  `MethodDescriptor[] getMethodDescriptors()`：获取其中的方法描述器

### PropertyDescriptor 类 ###

 *  `Class<?> getPropertyType()`：获得属性的类型的 Class 对象
 *  `String getName()`：获取此属性名
 *  `Method getReadMethod()`：获取属性值的 get 方法
 *  `Method getWriteMethod()`：获取属性值的 set 方法

``````````java
// JavaBean 和 Map 之间的相互转换 
  public class BeanMapUtils { 
      // 把一个 Map 对象转换为一个 JavaBean 对象 
      public static <T> T map2bean(Map<String, Object> map, Class<T> beanType) throws Exception { 
          T obj = beanType.newInstance(); 
          BeanInfo beanInfo = Introspector.getBeanInfo(beanType, Object.class); 
          PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors(); 
          for (PropertyDescriptor pd : pds) { 
              Object value = map.get(pd.getName()); 
              pd.getWriteMethod().invoke(obj, value); 
          }    
          return obj; 
      } 
  
      // 把一个 JavaBean 对象转换为以一个 Map 对象 
      public static Map<String, Object> bean2map(Object bean) throws Exception { 
          Map<String, Object> map = new HashMap<>(); 
          BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), Object.class); 
          PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors(); 
          for (PropertyDescriptor pd : pds) { 
              String name = pd.getName(); 
              Object value = pd.getReadMethod().invoke(bean); 
              map.put(name, value); 
          } 
          return map; 
      } 
  }
``````````

# BeansUtils 工具类 #

 *  需要的 jar 包：commons-beanutils.jar、commons-logging.jar
 *  类方法`String getProperty(Object bean, String name)`：获取 bean 对象中属性名为 name 的值`void setProperty(Object bean, String name, Object value)`：设置 bean 对象中属性名为 name 的值为 value`void copyProperties(Object dest, Object orig)`：JavaBean 对象之间属性值的拷贝；或 Map 对象转 JavaBean 对象

# Lombok 工具 #

 *  通过命令行安装 jar 包，然后在项目中导入 lombok.jar

``````````
<dependency> 
      <groupId>org.projectlombok</groupId> 
      <artifactId>lombok</artifactId> 
      <optional>true</optional> 
  </dependency>
``````````

## Lombok 中的常用注解 ##

 *  val：用在局部变量前，相当于将变量声明为 final
 *  @NonNull：在方法参数前增加这个注解会自动在方法内对该参数进行是否为空的校验，如果为空，则抛出 NullPointerException
 *  @Cleanup：自动管理资源，用在局部变量前，在当前变量范围内即将执行完毕退出之前会自动清理资源，自动生成 try-finally 这样的代码来关闭流
 *  @Getter、@Setter：用在属性或类上，为属性生成 getter 和 setter 方法
 *  @EqualsAndHashCode：生成 `equals()` 方法 和 `hashCode()` 方法
 *  @ToString：实现 `toString()` 方法，属性：exclude、callSuper
 *  @Data：相当于同时使用了 @Getter、@Setter、@ToString、@EqualsAndHashCode、@RequiredArgsConstructor
 *  @Value：用在类上，是 @Data 的不可变形式，相当于为属性添加 final 声明，只提供 getter 方法，而不提供 setter 方法
 *  @Accessors：用在类、字段上（A more fluent API for getters and setters），属性：fluent、chain、prefix，`Widget testWidget = new Widget().id(1).name("foo");`
 *  @Builder：用在类、构造器、方法上，使用构造器创建对象，`Widget testWidget = Widget.builder().id(1).name("foo").build();`，属性：toBuilder = true，`Widget newWidget = testWidget.toBuilder().id(2).build();`；@Builder.Default（用在字段上，表示有默认值）
 *  @NoArgsConstructor：生成无参数的构造器
 *  @AllArgsConstructor
 *  @RequiredArgsConstructor(staticName = "of")
 *  @Synchronized：用在方法上，将方法声明为同步的，并自动加锁，锁对象是一个私有的属性 $lock 或 $LOCK
 *  @SneakyThrows：自动抛受检异常，而无需显式在方法上使用 throws 语句
 *  @Slf4j：为类提供一个 属性名为 log 的 SLF4J 日志对象（推荐）`private static final Logger log = LoggerFactory.getLogger(Test.class);` `log.error(各类参数或者对象toString() + "_" + e.getMessage(), e);`
 *  @Log4j：为类提供一个 属性名为 log 的 Log4j 日志对象
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414283402.png) 
    图 1 Java日志工具


[Java]: https://static.sitestack.cn/projects/sdky-java-note/8908845b093a2c31bd8eb4bd96b64f30.png