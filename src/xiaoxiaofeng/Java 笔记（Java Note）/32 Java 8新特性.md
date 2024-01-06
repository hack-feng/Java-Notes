# Lambda 表达式 #

 *  Lambda 表达式：带有参数变量的表达式
 *  Lambda 表达式由参数列表、箭头（`->`）和 Lambda 主体组成
 *  基本语法：`(parameters) -> expression` 或 `(parameters) -> { statements; }`（注意表达式与语句的区别）
 *  Java 编译器可以从**上下文**（目标类型）推断出用什么函数式接口来配合 Lambda 表达式，因此可以在 Lambda 语法中省去标注参数类型，当 Lambda 仅有一个类型需要推断的参数时，参数名称两边的括号也可以省略
 *  在 Lambda 主体中引用的局部变量必须使用 `final` 修饰或事实上最终的（类似匿名内部类）

## 函数式接口 ##

 *  有且仅有一个抽象方法的接口（@FunctionalInterface 注解用于表示该接口会设计成一个函数式接口），如 Comparator 、Runnable、Callable
    

## 函数描述符 ##

 *  函数式接口的抽象方法的签名（Lambda 表达式的签名）
 *  特殊的 void 兼容规则：如果一个 Lambda 的主体是**一个语句表达式**，它就和一个**返回 void**的函数描述符兼容

## 使用 Lambda ##

 *  Lambda 表达式以内联的形式为函数式接口的抽象方法提供实现，并把整个表达式作为函数式接口的一个**实例**（即 Lambda 表达式是函数式接口一个具体实现的实例）
 *  作用：传递代码片段
 *  注意：只有在**接受函数式接口**的地方才可以使用 Lambda 表达式

## 常见的函数式接口 ##

| 函数式接口          | 抽象方法                  | 函数描述符       | 默认方法                | 静态方法                                                     |
| ------------------- | ------------------------- | ---------------- | ----------------------- | ------------------------------------------------------------ |
| Comparator<T>       | `int compare(T o1, T o2)` | (T,T) -> int     | reversed, thenComparing | naturalOrder, comparing, comparingInt, comparingLong, comparingDouble, reverseOrder, nullsFirst, nullsLast |
| Runnable            | `void run()`              | () -> void       |                         |                                                              |
| Callable<V>         | `V call()`                | () -> V          |                         |                                                              |
| Predicate<T>        | `boolean test(T t)`       | T -> boolean     | and, or, negate         |                                                              |
| BiPredicate<T, U>   | `boolean test(T t, U u)`  | (T,U) -> boolean |                         |                                                              |
| Consumer<T>         | `void accept(T t)`        | T -> void        | andThen                 |                                                              |
| BiConsumer<T, U>    | `void accept(T t, U u)`   | (T,U) -> void    |                         |                                                              |
| Supplier<T>         | `T get()`                 | () -> T          |                         |                                                              |
| Function<T,R>       | `R apply(T t)`            | T -> R           | andThen, compose        | identity                                                     |
| BiFunction<T, U, R> | `R apply(T t, U u)`       | (T,U) -> R       |                         |                                                              |
| UnaryOperator<T>    | `T apply(T t)`            | T -> T           |                         |                                                              |
| BinaryOperator<T>   | `Tt apply(T t1, T t2)`    | (T,T) -> T       |                         | minBy, maxBy                                                 |

# 方法引用 #

 *  基本语法：`目标引用 :: 方法名`
 *  作用：直接传递现有的方法实现
 *  方法引用主要有四类（仅有一个方法调用的 Lambda 方法体）
    
     *  指向静态方法的方法引用，`args -> ClassName.staticMethod(args)` 等价 `ClassName::staticMethod`
     *  指向任意类型的实例方法的方法引用，`(arg0, rest) -> arg0.instanceMethod(rest)` 等价 `ClassName::instanceMethod`（arg0 的类型是 ClassName）
     *  指向现有对象的实例方法的方法引用，`args -> expr.instanceMethos(args)` 等价 `expr::instanceMethod`
     *  构造函数引用，`ClassName::new`（需要有无参构造器）

# 流 #

 *  java.util.stream.Stream
 *  流：从支持**数据处理操作**的**源**生成的**元素序列**
 *  集合是一个内存中的数据结构，包含数据结构中目前所有的值，其主要目的是以特定的时间/空间复杂度存储和访问元素
 *  流是在概念上固定的数据结构，其元素是按需计算的（即延迟计算），流的目的在于表达计算

> 
> 注意：流只能遍历一次
> 

## 流操作 ##

 *  可分为两类操作
    
     *  中间操作（intermediate operation）：可以连接起来的流操作，中间操作会**返回另一个流**
     *  终端操作（terminal operation）：关闭流的操作，终端操作会从流的流水线生成结果（任何不是流的值）
 *  除非流水线上触发一个终端操作，否则中间操作不会执行任何处理（延迟执行）
 *  流的使用一般包括三件事：
    
     *  一个数据源（如集合）来执行一个查询
     *  一个中间操作链，形成一条流的流水线
     *  一个终端操作，执行流水线，并能生成结果

## 使用流 ##

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414295502.png) 
图 1 中间操作和终端操作

### 筛选 ###

 *  `Stream<T> filter(Predicate<T> predicate)`：用谓词筛选，返回一个包括所有符合谓词的元素的流（用谓词筛选）
 *  `Stream<T> distinct()`：返回一个元素各异（根据流所生成元素的 **hashCode 和 equals 方法**实现）的流（筛选各不相同的元素）

``````````java
// distinct elements by property or key 
  static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) { 
      Map<Object, Boolean> seen = new ConcurrentHashMap<>(); 
      return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null; 
  } 
  list.stream().filter(distinctByKey(b -> b.getName()));
``````````

### 切片 ###

 *  `Stream<T> limit(long maxSize)`：返回一个不超过给定长度的流（截短流）
 *  `Stream<T> skip(long n)`：返回一个扔掉了前 n 个元素的流（跳过元素）

### 映射 ###

 *  提取或转换流中的元素
 *  `Stream<R> map(Function<T, R> mapper)`：对流中每一个元素应用函数（元素映射为新元素）
 *  `Stream<R> flatMap(Function<T, Stream<R>> mapper)`：把一个流中的每个元素都换成另一个**流**，然后把所有转换的流**连接**起来成为一个流（流的扁平化）（元素映射为流）
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414296174.jpeg) 
    图 2 Java\_8\_flatMap\_example

### 查找和匹配 ###

 *  `boolean allMatch(Predicate<T> predicate)`：流中的元素是否都能匹配给定的谓词
 *  `boolean anyMatch(Predicate<T> predicate)`：流中是否有一个元素能匹配给定的谓词
 *  `boolean noneMatch(Predicate<T> predicate)`：流中是否没有任何元素与给定的谓词匹配
 *  `Optional<T> findAny()`：返回当前流中的任意元素
 *  `Optional<T> findFirst()`：返回当前流中的第一个元素

### 归约 ###

 *  将流中所有的元素迭代合并成一个结果
 *  `Optional<T> reduce(BinaryOperator<T> accumulator)`
 *  `T reduce(T identity, BinaryOperator<T> accumulator)`：初始值 identity，accumulator 将两个流元素结合起来并产生一个新值（适用于不可变的归约）
 *  `U reduce(U identity, BiFunction<U, T, U> accumulator, BinaryOperator<U> combiner)`：
 *  `Optional<T> min(Comparator<T> comparator)`：根据提供的 Comparator 返回此流的最小元素
 *  `Optional<T> max(Comparator<T> comparator)`：根据提供的 Comparator 返回此流的最大元素
 *  `R collect(Collector<T, A, R> collector)`
 *  `R collect(Supplier<R> supplier, BiConsumer<R, T> accumulator, BiConsumer<R, R> combiner)`：自定义收集器的供应源 supplier、累加器 accumulator、组合器 combiner

#### Collector 接口 ####

``````````java
// T - 流中元素的类型，A - 累加器的类型，R - 收集操作的结果对象的类型 
  public interface Collector<T, A, R> { 
      Supplier<A> supplier(); // 建立新的结果容器 
      BiConsumer<A, T> accumulator(); // 将元素添加到结果容器（累加器是原位更新） 
      BinaryOperator<A> combiner(); // 合并两个结果容器 
      Function<A, R> finisher(); // 对结果容器应用最终转换 
      Set<Collector.Characteristics> characteristics(); // 定义收集器的行为：UNORDERED、CONCURRENT、IDENTITY_FINISH 
      // UNORDERED——归约结果不受流中元素的遍历和累积顺序的影响 
      // CONCURRENT——accumulator 函数可以从多个线程同时调用，且该收集器可以并行归约流（如果收集器没有标为 UNORDERED，则仅在用于无序数据源时才可以并行归约） 
      // IDENTITY_FINISH——完成器方法返回的函数是一个恒等函数，可以跳过，即累加器对象 A 将会直接用作归约过程的最终结果 R 
  }
``````````

#### 自定义收集 ####

``````````java
// 方法 1：自定义 Collector 
  public class ToListCollector<T> implements Collector<T, List<T>, List<T>> { 
      @Override 
      public Supplier<List<T>> supplier() { 
          return ArrayList::new; // 创建集合操作的起始点 
      } 
  
      @Override 
      public BiConsumer<List<T>, T> accumulator() { 
          return List::add; // 累积遍历过的项目，原位修改累加器 
      } 
  
      @Override 
      public BinaryOperator<List<T>> combiner() { 
          return (list1, list2) -> { 
              list1.addAll(list2); // 修改第一个累加器，将其与第二个累加器的内容合并 
              return list1; // 返回修改后的第一个累加器 
          }; 
      } 
  
      @Override 
      public Function<List<T>, List<T>> finisher() { 
          return Function.identity(); // 恒等函数 
      } 
  
      @Override 
      public Set<Characteristics> characteristics() { 
          // 为收集器添加 IDENTITY_FINISH和 CONCURRENT标志 
          return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH, CONCURRENT)); 
      } 
  } 
  
  List<Dish> dishes = menuStream.collect(new ToListCollector<Dish>()); 
  
  // 方法 2：对于 CONCURRENT 和 IDENTITY_FINISH 但并非 UNORDERED 的收集操作 
  List<Dish> dishes = menuStream.collect(ArrayList::new, List::add, List::addAll);
``````````

#### 预定义收集器 ####

 *  Collectors 实用类中提供了很多静态工厂方法用于创建常见的收集器 collector 的实例，这些收集器主要提供三大功能
 *  将流元素归约和汇总为一个值
    
     *  计算流中元素的个数：Collectors.counting
     *  最大值和最小值：Collectors.maxBy、Collectors.minBy，返回类型是 Optional
     *  汇总：求和 Collectors.summingInt、Collectors.summingLong、Collectors.summingDouble；求平均数 Collectors.averagingInt、Collectors.averagingLong、Collectors.averagingDouble
     *  收集关于流中元素 Integer 属性的统计值（元素个数、总和、平均值、最大值、最小值等）：Collectors.summarizing
     *  连接对流中每个元素调用 toString 方法所生成的字符串：Collectors.joining
     *  **广义的归约汇总**：`Collector<T, ?, Optional<T>> reducing(BinaryOperator<T> op)``Collector<T, ?, T> reducing(T identity, BinaryOperator<T> op)``Collector<T, ?, U> reducing(U identity, Function<T, U> mapper, BinaryOperator<U> op)`：归约操作的起始值 identity（也是流中没有元素时的返回值），转换函数 mapper，累积函数 op 将两个转换得到的结果累积成一个同类型的值（使用于可变的归约）
 *  元素分组
    
     *  `Collector<T, ?, Map<K, List<T>>> groupingBy(Function<T, K> classifier)`：分组函数 classifier 把流中的元素分成不同的组，把分组函数**返回的值**作为映射的键，把流中所有具有这个分类值的元素的**列表**作为对应的映射值，`groupingBy(f)` 实际上是 `groupingBy(f, toList())` 的简便写法（注意：分组函数的返回值即映射的键**不能为 null**）
     *  `Collector<T, ?, Map<K, D>> groupingBy(Function<T, K> classifier, Collector<T, A, D> downstream)`：**下游的收集器 downstream** 对**分到同一组中的所有流元素**执行进一步归约操作，可实现**多级分组**、**按子组收集数据**等

``````````java
Map<String, Map<String, Summary>> sumMap = stuList.stream() 
      .collect(Collectors.groupingBy(Student::getClassName, 
          Collectors.groupingBy(Student::getGender, 
              Collectors.collectingAndThen(Collectors.toList(), stus -> { 
                  int score1Total = 0, score2Total = 0; 
                  for (Student student : stus) { 
                      score1Total += student.getScore1(); 
                      score2Total += student.getScore2(); 
                  } 
                  // int score1Total = stus.stream().mapToInt(Student::getScore1).sum(); 
                  // int score2Total = stus.stream().mapToInt(Student::getScore2).sum(); 
                  Student stu = stus.get(0); 
                  return new Summary(stu.getClassName(), stu.getGender(), score1Total, score2Total); 
              })) 
      ));
``````````

 *  元素分区
    
     *  `Collector<T, ?, Map<Boolean, List<T>>> partitioningBy(Predicate<T> predicate)`：分区函数 predicate，得到的分组 Map 的键类型是 Boolean，最多可以分为两组
     *  `Collector<T, ?, Map<Boolean, D>> partitioningBy(Predicate<T> predicate, Collector<T, A, D> downstream)`
 *  其它
    
     *  `Collector<T, ?, List<T>> toList()`：把流中所有元素收集到一个 List
     *  `Collector<T, ?, Set<T>> toSet()`：把流中所有元素收集到一个 Set，删除重复项
     *  `Collector<T, ?, Map<K, U>> toMap(Function<T, K> keyMapper, Function<T, U> valueMapper)`
     *  `Collector<T, ?, Map<K, U>> toMap(Function<T, K> keyMapper, Function<T, U> valueMapper, BinaryOperator<U> mergeFunction)`
     *  `Collector<T, ?, M> toMap(Function<T, K> keyMapper, Function<T, U> valueMapper, BinaryOperator<U> mergeFunction, Supplier<M> mapSupplier)`：keyMapper - 产生 key 的映射函数；valueMapper - 产生 value 的映射函数（产生的 **value 不能为 null**）；mergeFunction - 一个合并函数，用于解决与**相同 key** 相关联的 value 之间的冲突，提供给 `Map.merge(Object, Object, BiFunction)`；mapSupplier - 返回一个新的空的 Map 的函数，其中将插入结果
     *  `Collector<T, ?, C> toCollection(Supplier<C> collectionFactory)`：把流中所有项目收集到给定的供应源创建的集合
     *  `Collector<T, A, RR> collectingAndThen(Collector<T, A, R> downstream, Function<R, RR> finisher)`：**包裹另一个收集器**，对其结果应用转换函数 finisher **把收集器的结果转换为另一种类型**
     *  `Collector<T, ?, R> mapping(Function<T, U> mapper, Collector<U, A, R> downstream)`：mapper 对流中的元素做变换，downstream 将变换的结果对象收集起来
         ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414296693.png) 
        图 3 Collectors类中的静态工厂方法

### 其它 ###

 *  `long count()`：返回此流中的元素数
 *  `void forEach(Consumer<T> action)`：对此流的每个元素执行操作
 *  `void forEachOrdered(Consumer<T> action)`：按流中元素的顺序对此流的每个元素执行操作
 *  `Stream<T> sorted()`：返回由此流的元素组成的流，根据自然顺序排序（要求流中元素的类型必须实现 Comparable 接口）
 *  `Stream<T> sorted(Comparator<T> comparator)`：返回由此流的元素组成的流，根据提供的 Comparator 进行排序
 *  `S unordered()`：把有序流变成无序流，S 是 BaseStream 的实现类型
 *  `Stream<T> peek(Consumer<T> action)`：返回由该流的元素组成的流，另外在从生成的流中**消耗元素**时对每个元素执行提供的操作（拆分）
 *  `Object[] toArray()`：返回一个包含此流的元素的数组

### 静态方法 ###

 *  `Stream<T> concat(Stream<T> a, Stream<T> b)`：合并两个流

## 数值流 ##

 *  流有三种基本的原始类型特化：IntStream、DoubleStream 和 LongStream，流中的元素类型分别为 int、long 和 double
 *  原始类型流映射到数值流
    
     *  `IntStream mapToInt(ToIntFunction<T> mapper)`
     *  `LongStream mapToLong(ToLongFunction<T> mapper)`
     *  `DoubleStream mapToDouble(ToDoubleFunction<T> mapper)`
 *  数值流转换回对象流
    
     *  `Stream<Integer> boxed()`、`Stream<Long> boxed()`、`Stream<Double> boxed()`：装箱
     *  `Stream<U> mapToObj(IntFunction<U> mapper)`、`Stream<U> mapToObj(LongFunction<U> mapper)`、`Stream<U> mapToObj(DoubleFunction<U> mapper)`
 *  数值流之间的相互转换
    
     *  `LongStream mapToLong(IntToLongFunction mapper)`、`IntStream mapToInt(LongToIntFunction mapper)`
     *  `DoubleStream mapToDouble(IntToDoubleFunction mapper)`、`DoubleStream mapToDouble(LongToDoubleFunction mapper)``IntStream mapToInt(DoubleToIntFunction mapper)`、`LongStream mapToLong(DoubleToLongFunction mapper)`

### 类方法 ###

 *  `IntStream range(int startInclusive, int endExclusive)`、`LongStream range(long startInclusive, long endExclusive)`：生成某个范围内的数字，不包含结束值
 *  `IntStream rangeClosed(int startInclusive, int endInclusive)`、`LongStream rangeClosed(long startInclusive, long endInclusive)`：生成某个范围内的数字，包含结束值

### 实例方法 ###

 *  `OptionalInt max()`、`OptionalLong max()`、`OptionalDouble max()`：求最大值
 *  `OptionalInt min()`、`OptionalLong min()`、`OptionalDouble min()`：求最大值
 *  `int sum()`、`long sum()`、`double sum()`：求和，默认返回 0

## 构建流 ##

 *  从集合创建
    
     *  使用 Collection 接口中的抽象方法 `Stream<E> stream()`创建一个流
 *  由值创建流
    
     *  使用 Stream 类中静态方法 `Stream.of(T… values)` 通过显式值创建一个流
     *  使用 Stream 类中静态方法`Stream.empty()`，创建一个空流
 *  由数组创建流
    
     *  使用 Arrays 类中静态方法 `Arrays.stream(T[] array)` 从数组创建一个流
 *  由文件生成流
    
     *  使用 Files 类中静态方法`Files.lines(Path path, Charset cs)`，返回一个由指定文件中的**各行**构成的字符串流
 *  由函数生成流：创建无限流
    
     *  迭代：使用 Stream 类中静态方法 `Stream.iterate(T seed, UnaryOperator<T> f)`，依次对每个新生成的值应用函数 f，流的第一个元素是初始值 seed
     *  生成：使用 Stream 类中静态方法 `Stream.generate(Supplier<T> s)`：流的每个元素由 s 提供

## 并行流 ##

 *  `parallelStream()`
 *  `parallel()`：把顺序流转成并行流
 *  `sequential()`：把并行流转成顺序流
 *  并行流背后使用的基础架构是 Java 7 中引入的 Fork/Join 分支/合并框架（以递归方式将可以并行的任务拆分成更小的任务，在不同的线程上执行，然后将每个子任务的结果合并起来生成整体结果）
 *  默认的线程池是 ForkJoinPool，默认的线程数量是处理器数量（`Runtime.getRuntime().availableProcessors()`）

# Optional<T> #

 *  Optional 类（java.util.Optional）是一个容器类，代表一个值存在或不存在
 *  基础类型的 Optional 对象：OptionalInt、OptionalLong、OptionalDouble
    

### 静态方法 ###

 *  `Optional<T> empty()`：返回一个空的 Optional 实例
 *  `Optional<T> of(T value)`：返回具有指定非空值的 Optional 实例，如果该值为 null，则抛出一个 NullPointerException 异常
 *  `Optional<T> ofNullable(T value)`：返回一个指定值的 Optional，如果值为 null，则返回一个空的 Optional

### 实例方法 ###

 *  `boolean isPresent()`：在值存在时就返回 true，否则返回 false
 *  `void ifPresent(Consumer<T> block)`：在值存在的时候执行给定的代码块，否则什么也不做
 *  `Optional<U> map(Function<T, U> mapper)`：如果值存在，就对该值执行提供的 mapping 函数调用，否则返回一个空的 Optional 对象
 *  `Optional<U> flatMap(Function<T, Optional<U>> mapper)`：如果值存在，就对该值执行提供的 mapping 函数调用，返回一个 Optional 类型的值，否则就返回一个空的 Optional 对象（将两层的 Optional 对象转换为单一 Optional 对象）
 *  `Optional<T> filter(Predicate<T> predicate)`：如果值存在并且满足提供的谓词，就返回**包含该值**的 Optional 对象；否则返回一个空的 Optional 对象
 *  `T get()`：在值存在时返回值，否则抛出一个 NoSuchElementException 异常
 *  `T orElse(T other)`：返回值（如果存在），否则返回 other
 *  `T orElseGet(Supplier<T> other)`：返回值（如果存在），否则调用 other 并返回该调用的结果
 *  `T orElseThrow(Supplier<X> exceptionSupplier)`：返回包含的值（如果存在），否则抛出由 exceptionSupplier 创建的异常

# 新的日期和时间 API #

 *  java.time 包中，日期-时间对象是不可变的
 *  Temporal 的实现类：Instant（代表一个具体的时刻）、LocalDate（代表不带时区的日期）、LocalTime（代表不带时区的时间）、LocalDateTime（代表不带时区的日期、时间）、Year（代表年）、YearMonth（代表年月）、ZonedDateTime（代表带相对于指定时区的日期、时间）
 *  TemporalAmount 的实现类：Duration（代表以秒和纳秒衡量的时长）、Period（代表以年、月、日衡量的时长）
 *  ZoneId（代表一个时区）、ZoneOffset（ZoneId 的子类，代表与 UTC/格林尼治时间的绝对偏差）
 *  Clock（用于获取指定时区的当前日期、时间）
 *  DayOfWeek（星期枚举类）、Month（月份枚举类）
 *  ChronoUnit（时间单位枚举类）：YEARS、MONTHS、WEEKS、DAYS、HOURS、MINUTES、SECONDS、NANOS 等
 *  格式器类：DateTimeFormatter，其所有实例都是线程安全的
 *  根据模式字符串来创建 DateTimeFormatter 格式器，类方法：`DateTimeFormatter ofPattern(String pattern)`
 *  使用 DateTimeFormatter 格式化为字符串
    
     *  调用 DateTimeFormatter 对象的 `format(TemporalAccessor temporal)` 将日期、时间（LocalDate、LocalDateTime、LocalTime 等实例）格式化为字符串
     *  调用 LocalDate、LocalDateTime、LocalTime 等日期、时间对象的 `format(DateTimeFormatter formatter)` 方法执行格式化
 *  使用 DateTimeFormatter 解析字符串通过日期、时间对象提供的 `parse(CharSequence text)`或`parse(CharSequence text, DateTimeFormatter formatter)` 方法解析日期、时间字符串（默认的格式是 DateTimeFormatter.ISO\_LOCAL\_DATE\_TIME、DateTimeFormatter.ISO\_LOCAL\_DATE、DateTimeFormatter.ISO\_LOCAL\_TIME，如 "2011-12-03T10:15:30.1234"，"T" 不区分大小写，秒和纳秒可选）

``````````java
// 获取当前日期 
  LocalDate today = LocalDate.now(); 
  today.lengthOfMonth(); // today.get(ChronoField.DAY_OF_MONTH); 
  // 判断是否时闰年 
  today.isLeapYear(); 
  
  // 获取当前时间 
  LocalDateTime now = LocalDateTime.now(); 
  // 设置时间 
  LocalDateTime dateTime = LocalDateTime.of(2018, 10, 1, 23, 59, 59); 
  
  // 获取年、月、日、时、分、秒 
  dateTime.getYear(); // dateTime.get(ChronoField.YEAR); 
  dateTime.getMonth().getValue(); // dateTime.get(ChronoField.MONTH_OF_YEAR); 
  dateTime.getDayOfMonth(); // dateTime.get(ChronoField.DAY_OF_MONTH); 
  dateTime.getHour(); 
  dateTime.getMinute(); 
  dateTime.getSecond(); 
  
  // 调整日期/时间，返回修改了属性的新对象 
  dateTime.withDayOfMonth(25); // dateTime.with(ChronoField.DAY_OF_MONTH, 25) 
  // 当月的最后一天 
  dateTime.with(TemporalAdjusters.lastDayOfMonth()); 
  
  // 比较先后 
  dateTime.isAfter(now); 
  dateTime.isBefore(now); 
  
  // 加减时间 
  dateTime.plusDays(1); // dateTime.plus(1, ChronoUnit.DAYS); 
  dateTime.minusDays(1); 
  
  // 格式化 
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
  // LocalDateTime 转 String 
  formatter.format(dateTime); // dateTime.format(formatter); 
  // String 转 LocalDateTime 
  LocalDateTime.parse("2018-10-01 23:59:59", formatter); 
  
  // LocalDateTime 转 LocalDate、LocalTime 
  LocalDate localDate = dateTime.toLocalDate(); 
  LocalTime localTime = dateTime.toLocalTime(); 
  
  // LocalDate、LocalTime 转 LocalDateTime 
  LocalDateTime localDateTime = localDate.atStartOfDay(); 
  LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime); 
  
  // 获取当前时间戳 
  long millisecond = Instant.now().toEpochMilli(); // 转换当前时间的毫秒值 
  long second = Instant.now().getEpochSecond(); // 获取当前时间的秒数 
  Instant.ofEpochMilli(millisecond); // 毫秒值转 Instant 
  
  // 将此日期转换为从 1970-01-01 开始的天数 
  dateTime.toEpochDay(); 
  // 将此日期时间转换为从 1970-01-01T00：00：00Z 开始的秒数 
  dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli(); 
  
  // LocalDateTime 转换为 ZonedDateTime，再转换为 Instant，再转换为 Date 
  Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()); 
  // Date 转换为 Instant，再转换为 LocalDateTime 
  LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()); 
  
  // 获取相差时间间隔 
  amount = start.until(end, ChronoUnit.DAYS); // end.toEpochDay() - start.toEpochDay() 
  amount = ChronoUnit.DAYS.between(start, end); 
  
  // 时间长度：Duration、Period 
  Duration d1 = Duration.between(time1, time2); 
  Duration d1 = Duration.between(dateTime1, dateTime2); 
  Duration d2 = Duration.between(instant1, instant2); 
  Period tenDays = Period.between(date1, date2); 
  
  Period sixMonths = Period.ofMonths(6); 
  Duration sixSeconds = Duration.ofSeconds(6); 
  dateTime.plus(sixMonths); 
  dateTime.plus(sixSeconds); 
  
  // 时区 ID 
  ZoneId zoneId = ZoneId.systemDefault(); 
  // ZoneId zoneId = TimeZone.getDefault().toZoneId(); 
  // ZoneId romeZone = ZoneId.of("Asia/Shanghai"); 
  zoneId.getId(); // 时区 ID：Asia/Shanghai 
  zoneId.getRules(); // 时区规则：ZoneRules[currentStandardOffset=+08:00] 
  
  // ZonedDateTime 有 LocalDateTime 几乎相同的方法，不同的是它可以设置时区 
  ZoneId zoneId = ZoneId.of("UTC+8"); 
  ZonedDateTime zonedDateTime = ZonedDateTime.of(2018, 10, 1, 23, 59, 59, 1234, zoneId); 
  
  // 为时间点添加时区信息 
  ZonedDateTime zdt1 = date.atStartOfDay(zoneId); // 时间为 00:00:00 
  ZonedDateTime zdt2 = dateTime.atZone(zoneId); 
  ZonedDateTime zdt3 = instant.atZone(zoneId);
``````````

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414297597.png) 
图 4 表示时间点的日期-时间类的通用方法

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414298650.png) 
图 5 日期-时间类中表示时间间隔的通用方法

 *  TemporalAdjusters 工厂类中返回 TemporalAdjuster（调整器）实例的静态方法
    
     *  `firstDayOfMonth()`：当月的第一天
     *  `lastDayOfMonth()`：当月的最后一天
     *  `firstDayOfNextMonth()`：下月的第一天
     *  `lastInMonth(DayOfWeek dayOfWeek)`：下月的最后一天
     *  `firstDayOfNextYear()`：明年的第一天
     *  `firstDayOfYear()`：当年的第一天
     *  `lastDayOfYear()`：今年的最后一天
     *  `dayOfWeekInMonth(int ordinal, DayOfWeek dayOfWeek)`：同一个月中，第几个符合星期几要求的值
     *  `firstInMonth(DayOfWeek dayOfWeek)`：同一个月中，第一个符合星期几要求的值
     *  `lastInMonth(DayOfWeek dayOfWeek)`：同一个月中，最后一个符合星期几要求的值
     *  `previous(DayOfWeek dayOfWeek)`：在当前日期之前第一个符合指定星期几要求的日期
     *  `next(DayOfWeek dayOfWeek)`：在当前日期之后第一个符合指定星期几要求的日期
     *  `previousOrSame(DayOfWeek dayOfWeek)`：在当前日期之后第一个符合指定星期几要求的日期，如果当前日期已经符合要求，直接返回该对象
     *  `nextOrSame(DayOfWeek dayOfWeek)`：在当前日期之后第一个符合指定星期几要求的日期，如果当前日期已经符合要求，直接返回该对象
       
     
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414299461.png) 
    图 6 DateTimeFormatter\_Predefined\_Formatters

# Base64 #

 *  Base64 是一种用 64 个字符来表示任意二进制数据的方法
 *  Base64 编码会把 3 字节的二进制数据编码为 4 字节的文本数据，，长度比原来增加 1/3
 *  java.util.Base64 工具类提供的静态方法用于获取以下三种 Base64 编码方案的编解码器（Base64.Encoder、Base64.Decoder）
    
     *  Basic（RFC4648）：输出被映射到一组字符 `A-Za-z0-9+/`，编码不添加任何行标，输出的解码仅支持 `A-Za-z0-9+/`
     *  URL and Filename safe（RFC4648*URLSAFE）：输出映射到一组字符 \`A-Za-z0-9+*\`，输出是 URL 和文件
     *  MIME（RFC2045）：输出映射到 MIME 友好格式，即输出每行不超过 76 个字符，并且使用 '\\r' 并跟随 '\\n' 作为分割，编码输出最后没有行分割

### 内部类 ###

 *  Base64.Encoder，实例方法：`String encodeToString(byte[] src)`：使用 Base64 编码方案将指定的字节数组编码为字符串 `int encode(byte[] src, byte[] dst)`：使用 Base64 编码方案对来自指定字节数组的所有字节进行编码，将生成的字节写入给定的输出字节数组 `byte[] encode(byte[] src)`：使用 Base64 编码方案将指定字节数组中的所有字节编码为新分配的字节数组 `OutputStream wrap(OutputStream os)`：使用 Base64 编码方案包装用于编码字节数据的输出流
 *  Base64.Decoder，实例方法`byte[] decode(String src)`：使用 Base64 编码方案将 Base64 编码的字符串解码为新分配的字节数组 `byte[] decode(byte[] src)`：使用 Base64 编码方案从输入字节数组中解码所有字节，将结果写入新分配的输出字节数组 `int decode(byte[] src, byte[] dst)`：使用 Base64 编码方案从输入字节数组中解码所有字节，将结果写入给定的输出字节数组 `InputStream wrap(InputStream is)`：返回一个输入流，用于解码 Base64 编码字节流

# 其它语言新特性 #

## 注解 ##

#### 重复注解 ####

 *  要求：将注解标记为 @Repeatable，提供一个注解的容器

#### 类型注解 ####

 *  从 Java 8 开始，注解已经能应用于任何类型，包括 new 操作符、类型转换、instanceof 检查、泛型类型参数，以及 implements 和 throws 子句

## 通用目标类型推断 ##


[c9c7b6916647fa6f6d80f1b59230886f.png]: https://static.sitestack.cn/projects/sdky-java-note/c9c7b6916647fa6f6d80f1b59230886f.png
[Java_8_flatMap_example]: https://static.sitestack.cn/projects/sdky-java-note/21a832b07b34bfece792116ff0cfc805.jpeg
[Collectors]: https://static.sitestack.cn/projects/sdky-java-note/2c404902b2d051ab0fa778a5c356ec0b.png
[-]: https://static.sitestack.cn/projects/sdky-java-note/2807f9a7719a34fe94f6df47511e1e97.png
[- 1]: https://static.sitestack.cn/projects/sdky-java-note/77e8e3624ab918b5e87a2f74c5c6942d.png
[DateTimeFormatter_Predefined_Formatters]: https://static.sitestack.cn/projects/sdky-java-note/9c6ecd2698fcfb5bd6428598b3c23e98.png