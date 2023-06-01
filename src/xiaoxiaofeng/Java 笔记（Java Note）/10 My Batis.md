# ORM #

 *  ORM（Object/Relation Mapping）：对象/关系数据库映射
 *  作用：把对持久化对象的保存、修改、删除等操作，转换成对数据库的操作
 *  映射关系：数据表映射类数据表的行映射对象（即实例）数据表的列（字段）映射对象的属性
 *  注意：实体类需符合 JavaBean 规范

# MyBatis 的常用 API #

## SqlSessionFactory ##

 *  建议使用单例模式

``````````java
// 读取 mybatis-config.xml 文件 
  InputStream in = Resources.getResourceAsStream("mybatis-config.xml"); 
  // 初始化 mybatis，创建 SqlSessionFactory 类的实例 
  SqlSessionFactory SqlSessionFactory = new SqlSessionFactoryBuilder().build(in); 
  // 创建 SqlSession 实例 
  SqlSession session = SqlSessionFactory.openSession();
``````````

## SqlSession ##

 *  每个线程都应该有它自己的 SqlSession 实例
 *  常用方法`<T> T getMapper(Class<T> type)`：获取 mapper 接口的**代理对象**`insert`、`update`、`delete`、`selectOne`、`selectList` 方法`void commit()`：提交事务`void rollback()`：回滚事务`void close()`：关闭 SqlSession 对象

# mybatis-config.xml 配置文件 #

`<configuration>`：配置

- `<properties>`：引入配置文件，resource
- `<settings>`：设置，lazyLoadingEnabled（延时加载，当开启时，所有**关联对象**都会延迟加载，特定关联关系中可通过设置 fetchType 属性来覆盖该项的开关状态）、aggressiveLazyLoading（默认为 false，每个属性按需加载 (true in ≤3.4.1)）、lazyLoadTriggerMethods、cacheEnabled（默认为 true）、mapUnderscoreToCamelCase（默认 false）、defaultFetchSize（默认返回的结果集的大小）
- `<typeAliases>`：类型别名，typeAlias、package
- `<typeHandlers>`：类型处理器
- `<plugins>`：插件
- `<environments>`：环境配置，default
  - `<environment>`：环境变量，id
    - `<transactionManager>`：事务管理器，type="JDBC"
    - `<dataSource>`：数据源，type="POOLED"，子标签 `<property>`，配置 driver、url、username、password
- `<mappers>`：引入映射文件，resource

# Mapper XML 映射文件 #

## 常用的标签 ##

- `<cache>`：开启当前 mapper 的 namespace 下的**二级缓存**，type（使用自定义缓存方案，其指定的类必须实现 org.mybatis.cache.Cache 接口）
- `<insert>`：useGeneratedKeys="true"、keyPraperty、keyColumn、flushCache="false"（当前 insert 语句被调用后不清空本地缓存和二级缓存）
- `<update>`
- `<delete>`
- `<select>`：resuItType、resultMap、useCache="false"（禁用当前 select 语句的二级缓存）、statementType（可选 STATEMENT，PREPARED 或 CALLABLE，默认值：PREPARED）、fetchSize（尝试让驱动程序每次批量返回的结果行数等于这个设置值，默认值为 unset（依赖驱动））
- `<sql>`、`<include>`
- `<resultMap>`

## 参数类型（parameterType） ##

 *  如果传入的参数是原生的类型或简单数据类型（比如整型和字符串），因为没有相关属性，会使用参数值来替代
 *  如果传入的参数是一个复杂的对象，\#\{xxx\} 语句则会查找参数对象的 xxx 属性，然后将属性的值传入预处理语句的参数中
 *  如果传入的参数是一个 Map，则会以 xxx 作为 key 查找 Map 当中的值并将其设置到 SQL 语句中

## 高级结果映射 `<resultMap>` ##

 *  使用  映射返回类型，将从结果集中取出的数据转换成所需要的对象，解决列名不匹配
 *  常用属性：id、type、extends
 *  fetchType="eager"
    

### 普通映射 `<id>` `<result>` ###

 *  column：从数据库中得到的列名，或者是列名的重命名标签
 *  property：表示要映射到列结果的字段或属性名

### 关联映射 `<association>` ###

 *  property：表示要映射到列结果的字段或属性名
 *  javaType：表示该属性对应的 Java 类型或类型别名（如果映射到的是一个 JavaBean，javaType 属性是不需要的，MyBatis 通常可以断定类型；如果映射到的是 HashMap，则应该明确地指定 javaType 来保证 MyBatis 使用正确类型处理器）
 *  column：表示使用哪一列作为参数进行之后的 select 语句查询
 *  select：用于加载复杂类型属性的映射语句的 ID，从 column 中检索出来的数据将作为此 select 语句的参数，查询到的数据封装到 property 所代表的类型对象中
 *  columnPrefix：连接多表时，所映射列别名的前缀
 *  fetchType：属性的取值有 eager（立即加载）、lazy（懒加载）

### 集合映射 `<collection>` ###

 *  property：表示要映射到列结果的字段或属性名（集合类型）
 *  ofType：表示该集合中的**元素**类型
 *  column：表示使用哪一列作为参数进行之后的 select 语句查询
 *  select：用于加载复杂类型属性的映射语句的 ID，从 column 中检索出来的数据将作为此 select 语句的参数，查询到的数据封装到 property 所表示的类型对象中
 *  fetchType：属性的取值有 eager（立即加载）、lazy（懒加载）

### MyBatis 加载关联或集合的两种方式 ###

 *  嵌套查询（额外 SQL）：通过执行另外一个 SQL 映射语句来返回预期的复杂类型
    
     *  对于大型数据集合和列表，会引起“N+1 查询问题”：执行一个单独的 SQL 语句来获取结果列表（就是“+1”）执行一个查询语句来为每条记录加载细节（就是“N”）
     *  解决方法：使用内联查询；在表中添加冗余字段；加缓存；把 N+1 次查询变成 2 次查询（先查询结果列表，然后在应用层中遍历结果列表，取出所有的 \*\_id，去掉重复项，再执行一次查询细节）
 *  嵌套结果（内联查询）：使用嵌套结果映射来处理重复的联合结果的子集

``````````
private Author author; 
  private List<Post> posts;
``````````

``````````xml
<association property="author" javaType="Author" columnPrefix="blog_"> 
      <id property="id" column="author_id"/> 
      <result property="username" column="author_username"/> 
  </association> 
  
  <association property="author" javaType="Author" column="author_id" select="selectAuthor"/> 
  
  <collection property="posts" ofType="Post" columnPrefix="post_"> 
      <id property="id" column="id"/> 
      <result property="subject" column="subject"/> 
      <result property="body" column="body"/> 
  </collection> 
  
  <collection property="posts" ofType="Post" column="id" select="selectPostsForBlog"/>
``````````

## 动态 SQL ##

MyBatis 基于 **OGNL 表达式**来完成动态 SQL，OGNL 的表达式可以被用在任意的 SQL 映射语句中

### 常用动态 SQL 标签 ###

 *  if，注意：
    
     *  test 的值会使用 OGNL 计算结果
     *  在 test 判断条件中 null 与 0 等同
     *  如果传入的是单参数且参数类型为基本数据类型或基本数据类型对应的包装类型时，**在 test 判断条件中**需使用 `_parameter` 代表该参数，或使用 @Param 注解给该参数取一个名字
 *  choose（when、otherwise）
 *  where：where 标签知道只有在一个以上的 if 条件有值的情况下才去插入 WHERE 子句，并且若最后的内容是 “AND” 或 “OR” **开头**，则 where 标签也知道如何将它们去除
 *  set：set 标签会动态前置 SET 关键字，同时也会消除无关的逗号
 *  foreach：对一个集合进行遍历，主要用在构建 in 条件中，属性：collection、item、index、open、separator、close
    
     *  当使用可迭代对象（如列表、集合等）或者数组时，index 是当前迭代的次数，item 的值是本次迭代获取的元素
     *  当使用字典（或者 Map.Entry 对象的集合）时，index 是键，item 是值
     *  collection 属性值
        
         *  如果传入的是单参数且参数类型是一个 List 的时候，collection 属性值为 **list**
         *  如果传入的是单参数且参数类型是一个 array 数组的时候，collection 的属性值为 **array**
         *  如果传入的参数有多个，此时需要把它们封装成一个 Map，当然单参数也可以封装成 Map，Map 的 key 就是参数名，所以这个时候 collection 属性值就是传入的 List 或 Array 对象在自己封装的 Map 里面的 key
 *  bind：可以从 OGNL 表达式中创建一个变量并将其绑定到上下文

``````````xml
<bind name="pattern" value="'%' + _parameter.getName() + '%'" /> 
  <bind name="username_bind" value='@java.util.UUID@randomUUID().toString().replace("-", "")' /> 
  <!-- 对变量的调用可以通过 #{} 或 ${} 获取 -->
``````````

 *  trim

``````````xml
<!-- 等价 where 标签 -->
<trim prefix="WHERE" prefixOverrides="AND | OR">
    ... 
</trim>
<!-- 等价 set 标签 -->
<trim prefix="SET" suffixOverrides=",">
    ...
</trim>
``````````

## 参数的使用 ##

### 在 SQL 语句中使用 ###

 *  #{ } 和 ${} 传参的区别：MyBatis 在对 SQL 语句进行预编译之前，会对 SQL 进行动态解析，解析为一个 BoundSql 对象 #
    *  `#{ }` ：在动态 SQL 解析阶段，一个 \#\{ \} 被解析为一个 JDBC 预编译语句（PreparedStatement ）的参数占位符 ?，而变量的替换是在 DBMS 中（注意：使用占位符替换字符串时会带上单引号 ''）
     *  `${ }` ：仅仅为一个字符串替换，在动态 SQL 解析阶段就已经进行变量替换，（$\{ \} 通过 **OGNL** 方式来获取值）
 *  用法：能使用 \#\{ \} 的地方就用 \#\{ \}，变量作为表名、列名或 SQL 关键字时，使用 $\{ \}

### 在动态 SQL 条件中使用 ###

#### MyBatis 动态 SQL 中的内置参数 ####

 *  `_parameter`：代表整个参数，单个参数时 `_parameter` 就是代表这个参数；多个参数时参数会被封装为一个 map，`_parameter`就是代表这个 map，通过 `_parameter.get("arg0")` 或 `_parameter.get("param1")` 可以得到第一个参数
 *  `_databaseId`：如果在 mybatis-config.xml 配置文件中配置了 databaseIdProvider 标签，则 `_databaseId` 就是代表当前数据库类型的别名
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414407693.png) 
    图 1 Mybatis中OGNL表达式

### Mybatis 常用的 OGNL 表达式 ###

 *  `e1 or e2`：或
 *  `e1 and e2`：且
 *  `e1 == e2` 或 `e1 eq e2`：相等
 *  `e1 != e2` 或 `e1 neq e2`：不等
 *  `e1 lt e2`：小于
 *  `e1 lte e2`：小于等于
 *  `e1 gt e2`：大于
 *  `e1 gte e2`：大于等于
 *  `e1 + e2`（加），`e1 - e2`（减），`e1 * e2`（乘），`e1/e2`（除），`e1%e2`（余）
 *  `!e` 或 `not e`：非，取反
 *  `e.property`：对象属性值
 *  `e.method(args)`：调用对象方法
 *  `e1[e2]`：按索引取值（List、数组和 map）
 *  `@class@field`：调用类的静态字段值
 *  `@class@method(args)`：调用类的静态方法

# 常用注解 #

 *  @Param 注解，用于给映射器方法参数取一个名字，当映射器方法参数有多个时，必需要把参数封装成一个 Map
 *  @Alias 注解，定义类的别名

# 关联关系 #

 *  实例之间的互相访问

## 一对一 ##

 *  使用外键关联关系，并给外键列增加 unique 唯一约束

## 多对一、一对多 ##

 *  使用主外键关联，外键列应该在多方，即多方维护关系
 *  查询操作
    
     *  额外 SQL 查询：子查询，存在 N+1 问题，适用于查询单个对象
     *  内联查询/嵌套查询：多表连接查询，适用于查询多个对象
 *  在实际开发中，一对多关系通常映射为集合对象，而由于多方的数据量可能很大，所以通常使用懒加载；而多对一只是关联到一个对象，所以通常使用多表连接直接把数据提取出来

## 多对多 ##

 *  使用一个中间表来维护关系
 *  保存记录后，再通过中间表建立关系 saveRelation()
 *  更新记录前，先删除关系 deleteRelation()，再重新建立关系
 *  删除时，先删除关系，再删除记录

# MyBatis 的缓存机制（查询缓存） #

 *  默认情况下，如果 SqlSession 执行了 **DML 操作**，并提交到数据库，MyBatis 中的一级缓存、二级缓存都会被清空

## 一级缓存（session 级别） ##

 *  作用域是 SqlSession 范围
 *  缓存数据存储在 SqlSession 对象的 HashMap 中，key 为对象的 id，value 为对象
 *  默认开启

## 二级缓存（mapper 级别） ##

 *  作用域是 mapper 的同一个 namespace，多个 SqlSession 共享
 *  相同的 namespace 下的 sql 语句，且向 sql 中传递的**参数**也相同，即 key 为 namespace + sqlId + 参数
 *  使用二级缓存时，与查询结果映射的 Java 对象必须实现 Serializable 接口
 *  开启二级缓存：在 mapper 文件中添加 `<cache/>`
    
     *  mapper 文件中的所有 select 语句将会被缓存
     *  mapper 文件中的所有 insert, update 和 delete 语句会刷新缓存
     *  缓存会使用 Least Recently Used（LRU，最近最少使用的）算法来收回

### EhCache 二级缓存 ###

 *  导入 ehcache-core.jar、mybatis-ehcache.jar
 *  添加配置文件 ehcache.xml
 *  在 mapper 文件中添加 `<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>`

### Redis 二级缓存 ###

 *  导入 mybatis-redis:1.0.0-beta2.jar（所用客户端为 Jedis）
 *  添加配置文件 redis.properties
 *  在 mapper 文件中添加 `<cache type="org.mybatis.caches.redis.RedisCache"/>`

### EhCache 与 Redis 的区别 ###

 *  Redis 的数据结构比较丰富，有 key-value、hash、set 等；EhCache 比较简单，只有 key-value
 *  EhCache 直接在 JVM 虚拟机中缓存，速度快，效率高，但是缓存共享麻烦，集群分布式应用不方便；Redis 是通过 Socket 访问到缓存服务，效率 EhCache 低，比数据库要快很多，处理集群和分布式缓存方便，有成熟的方案
 *  如果是单个应用或者对缓存访问要求很高的应用，用 EhCache；如果是大型系统，存在缓存共享、分布式部署、缓存内容很大的，建议用 Redis

# MyBatis 的注解配置 #

## 常用 Annotation 注解 ##

 *  @Select、@Insert、@Update、@Delete
 *  动态 SQL 映射：@SelectProvider、@InsertProvider、@UpdateProvider、@DeleteProvider允许指定一个类名和一个方法在执行时返回运行的语句，属性：type（类的完全限定名）、method（该类中的那个方法）
 *  @Result：在列和属性之间的单独结果映射，属性：id（是否被用于主键映射）、column、property、javaType、jdbcType、typeHandler、one（与 XML 配置中的  相似）、many（与 XML 配置的  相似）
 *  @Results：多个结果映射（Result）列表
 *  @Options：提供配置选项的附加值，属性：useGeneratedKeys、keyProperty 等
 *  @One：复杂类型的单独属性值映射，必须指定 select 属性（映射的 SQL 语句的完全限定名）
 *  @Many：复杂类型的集合属性映射，必须指定 select 属性（映射的 SQL 语句的完全限定名）
 *  @Mapper：Marker interface for MyBatis mappers
    

## 使用 ##

``````````java
@Insert("insert into TB_USER(name, sex, age) values(#{name}, #{sex}, #{age})")
@Options(useGeneratedKeys = true, keyProperty = "id")
int saveUser(User user);

@Delete("delete from TB USER where id= #{id}")
int removeUser(@Param("id") Integer id);

@Update("update TB USER set name= #{name}, sex = #{sex}, age = #{age} where id = #{id}")
void modifyUser(User user);

@Select("select * from TB_USER where id = #{id}")
@Results({
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "name", property = "name"),
        @Result(column = "age", property = "age")
})
User selectUserByld(Integer id);

@Select("select * from TB_USER")
List<User> selectAHUser();

@Select("select * from TB_ORDER where id = #{id}")
@Results({
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "code", property = "code"),
        @Result(column = "total", property = "total"),
        @Result(column = "user_id", property = "user",
                one = @One(select = "com.example.mapper.UserMapper.selectByld",
                        fetchType = FetchType.EAGER)),
        @Result(column = "id", property = "articles",
                many = @Many(select = "com.example.mapper.ArticleMapper.selectByOrderld",
                        fetchType = FetchType.LAZY))
})
Order selectByld(Integer id);
``````````

# MyBatis 分页插件 PageHelper #

 *  添加依赖：

``````````xml
<dependency>
     <groupId>com.github.pagehelper</groupId>
     <artifactId>pagehelper-spring-boot-starter</artifactId>
     <version>最新版本</version>
 </dependency>
``````````

 *  使用 PageInfo 的用法：在 Service 中使用 PageInfo 替换 PageResult

``````````java
public PageInfo queryPage(QueryObject qo) {
     // 开始分页
     PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
     // 查询 page 结果
     List list = Mapper.queryList(qo);
     // 用 PageInfo 对结果进行包装
     return new PageInfo(list);
 }
``````````

 *  PageInfo 中常用的分页属性：pageNum 当前页，pageSize 每页的数量，size 当前页的数量，total 总记录数，pages 总页数，结果集 list，prePage 前一页，nextPage 下一页
 *  注意此时在 Mapper.xml 中的 queryList 方法中不需要写分页语句

## MyBatis 插件机制 ##

 *  MyBatis 釆用**责任链模式**，通过**动态代理**组织多个插件（拦截器），通过这些插件可以改变 MyBatis 的默认行为（如 SQL 重写）
 *  可拦截的类型：Executor、StatementHandler、ParameterHandler、ResultSetHandler
 *  分页拦截器，实现接口 Interceptor，并添加 @Intercepts 注解

``````````java
// com.github.pagehelper.PageInterceptor
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
// com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
``````````

# MyBatis 通用 Mapper #

## 基础接口 ##

### Select ###

 *  `List<T> select(T record)`：根据实体中的属性值进行查询，查询条件使用等号
 *  `T selectByPrimaryKey(Object key)`：根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
 *  `List<T> selectAll()`：查询全部结果，select(null) 方法能达到同样的效果
 *  `T selectOne(T record)`：根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
 *  `int selectCount(T record)`：根据实体中的属性查询总数，查询条件使用等号

### Insert ###

 *  `int insert(T record)`：保存一个实体，null 的属性也会保存，不会使用数据库默认值
 *  `int insertSelective(T record)`：保存一个实体，null 的属性不会保存，会使用数据库默认值

### Update ###

 *  `int updateByPrimaryKey(T record)`：根据主键更新实体全部字段，null 值会被更新
 *  `int updateByPrimaryKeySelective(T record)`：根据主键更新属性不为 null 的值

### Delete ###

 *  `int delete(T record)`：根据实体属性作为条件进行删除，查询条件使用等号
 *  `int deleteByPrimaryKey(Object key)`：根据主键字段进行删除，方法参数必须包含完整的主键属性

## Example 方法 ##

 *  `List<T> selectByExample(Object example)`：根据 Example 条件进行查询（该查询支持通过`Example`类指定查询列，通过`selectProperties`方法指定查询列）
 *  `int selectCountByExample(Object example)`：根据 Example 条件进行查询总数
 *  `int updateByExample(T record, Object example)`：根据 Example 条件更新实体`record`包含的全部属性，null 值会被更新
 *  `int updateByExampleSelective(T record, Object example)`：根据 Example 条件更新实体`record`包含的不是 null 的属性值
 *  `int deleteByExample(Object example)`：根据 Example 条件删除数据

## Condition 方法 ##

 *  Condition 方法和 Example 方法作用完全一样，只是为了避免 Example 带来的歧义，提供的 Condition 方法
 *  `List<T> selectByCondition(Object condition)`：根据 Condition 条件进行查询
 *  `int selectCountByCondition(Object condition)`：根据 Condition 条件进行查询总数
 *  `int updateByCondition(T record, Object condition)`：根据 Condition 条件更新实体`record`包含的全部属性，null 值会被更新
 *  `int updateByConditionSelective(T record, Object condition)`：根据 Condition 条件更新实体`record`包含的不是 null 的属性值
 *  `int deleteByCondition(Object condition)`：根据 Condition 条件删除数据

## RowBounds ##

 *  默认为**内存分页**，可以配合 PageHelper 实现物理分页
 *  `List<T> selectByRowBounds(T record, RowBounds rowBounds)`：根据实体属性和 RowBounds 进行分页查询
 *  `List<T> selectByExampleAndRowBounds(Object example, RowBounds rowBounds)`：根据 example 条件和 RowBounds 进行分页查询
 *  `List<T> selectByConditionAndRowBounds(Object condition, RowBounds rowBounds)`：根据 example 条件和 RowBounds 进行分页查询，该方法和 selectByExampleAndRowBounds 完全一样，只是名字改成了 Condition

## special 特殊接口 ##

 *  针对部分数据库设计，不是所有数据库都支持
 *  `int insertList(List<T> recordList)`：批量插入，支持批量插入的数据库可以使用，例如 MySQL, H2 等，另外该接口限制实体包含`id`属性并且必须为自增列
 *  `int insertUseGeneratedKeys(T record)`：插入数据，限制为实体包含`id`属性并且必须为自增列，实体配置的主键策略无效

## Ids 接口 ##

 *  通过操作 ids 字符串进行操作，ids 如”1, 2, 3”这种形式的字符串，这个方法要求实体类中有且只有一个带有`@Id`注解的字段，否则会抛出异常
 *  `List<T> selectByIds(String ids)`：根据主键字符串进行查询，类中只有存在一个带有 @Id 注解的字段
 *  `int deleteByIds(String ids)`：根据主键字符串进行删除，类中只有存在一个带有 @Id 注解的字段

## 通用的 Example 查询对象 ##

 *  常用字段：
    
     *  String orderByClause：order by 的子句
     *  boolean distinct：是否去重
     *  boolean exists：字段是否必须存在，true 时，如果字段不存在就抛出异常，false 时，如果不存在就不使用该字段的条件
     *  boolean notNull：值是否不能为空，true 时，如果值为空，就会抛出异常，false 时，如果为空就不使用该字段的条件
     *  boolean forUpdate：是否加上排它锁
     *  Set selectColumns：查询字段
     *  Set excludeColumns：排除的查询字段
     *  String countColumn
     *  List oredCriteria
     *  Map propertyMap：属性和列对应
     *  String tableName：动态表名
 *  构造器：Example(Class entityClass)（默认 exists 为 true，notNull 为 false）
 *  实例方法：`Criteria createCriteria()`

### 内部类 Criteria ###

 *  用于添加条件，相当 where 子句
 *  字段：exists、notNull、andOr（连接条件）、propertyMap
 *  使用 and 连接条件的方法、使用 or 连接条件的方法
 *  `Criteria andIsNull(String property)`
 *  `Criteria andIsNotNull(String property)`
 *  `Criteria andEqualTo(String property, Object value)`
 *  `Criteria andNotEqualTo(String property, Object value)`
 *  `Criteria andGreaterThan(String property, Object value)`
 *  `Criteria andGreaterThanOrEqualTo(String property, Object value)`
 *  `Criteria andLessThan(String property, Object value)`
 *  `Criteria andLessThanOrEqualTo(String property, Object value)`
 *  `Criteria andIn(String property, Iterable values)`
 *  `Criteria andNotIn(String property, Iterable values)`
 *  `Criteria andBetween(String property, Object value1, Object value2)`
 *  `Criteria andNotBetween(String property, Object value1, Object value2)`
 *  `Criteria andLike(String property, String value)`
 *  `Criteria andNotLike(String property, String value)`
 *  `Criteria andCondition(String condition)`：手写条件，例如 "length(countryname)<5"
 *  `Criteria andCondition(String condition, Object value)`：手写左边条件，右边用 value 值


[Mybatis_OGNL]: https://static.sitestack.cn/projects/sdky-java-note/d028687ad368c7d1a810a7a970d42fe0.png