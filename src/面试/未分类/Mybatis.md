### 什么是Mybatis？

1. **Mybatis是一个半ORM(对象关系映射)框架**，它内部封装了JDBC，开发时只需要关注SQL语句本身，不需要花费精力去处理加载驱动、创建连接、创建statement等繁杂的过程。程序员直接编写原生态sql，可以严格控制sql执行性能，灵活度高。

2. MyBatis可以使用XML或注解来配置和映射原生信息，将POJO映射成数据库中的记录，避免了几乎所有的JDBC代码和手动设置参数以及获取结果集。

3. 通过xml文件或注解的方式将要执行的各种statement配置起来，并通过java对象和statement中sql的动态参数进行映射生成最终执行的sql语句，最后由mybatis框架执行sql并将结果映射为java对象并返回。(从执行sql到返回result的过程)。

### 说说Mybaits的优缺点

**优点**

1. 基于SQL语句编程，相当灵活，不会对应用程序或者数据库的现有设计造成任何影响，SQL写在XML里，解除sql与程序代码的耦合，便于统一管理；提供XML标签，支持编写动态SQL语句，并可重用。

2. 与JDBC相比，减少了50%以上的代码量，消除了JDBC大量冗余的代码，不需要手动开关连接；

3. 很好的与各种数据库兼容(因为MyBatis使用JDBC来连接数据库，所以只要JDBC支持的数据库MyBatis都支持)。

4. 能够与Spring很好的集成；

5. 提供映射标签，支持对象与数据库的ORM字段关系映射；提供对象关系映射标签，支持对象关系组件维护。

**缺点**

1. SQL语句的编写工作量较大，尤其当字段多、关联表多时，对开发人员编写SQL语句的功底有一定要求。单表解决方案：MybatisPlus框架大大简化了单表的增删改查语句。

2. SQL语句依赖于数据库，导致数据库移植性差，不能随意更换数据库。

### MyBatis框架适用哪些场合？

1. MyBatis专注于SQL本身，是一个足够灵活的DAO层解决方案。

2. 对性能的要求很高，或者需求变化较多的项目，如互联网项目，MyBatis将是不错的选择。

### MyBatis与Hibernate有哪些不同？

1. Mybatis和hibernate不同，它不完全是一个ORM框架，因为MyBatis需要程序员自己编写Sql语句。
2. Mybatis直接编写原生态sql，可以严格控制sql执行性能，灵活度高，非常适合对关系数据模型要求不高的软件开发，因为这类软件需求变化频繁，一但需求变化要求迅速输出成果。但是灵活的前提是mybatis无法做到数据库无关性，如果需要实现支持多种数据库的软件，则需要自定义多套sql映射文件，工作量大。
3. Hibernate对象/关系映射能力强，数据库无关性好，对于关系模型要求高的软件，如果用hibernate开发可以节省很多代码，提高效率。

### 为什么说Mybatis是半自动ORM映射工具？

Hibernate属于全自动ORM映射工具，使用Hibernate查询关联对象或者关联集合对象时，可以根据对象关系模型直接获取，所以它是全自动的。

Mybatis在查询关联对象或关联集合对象时，需要手动编写sql来完成，所以，称之为半自动ORM映射工具。

### #{}和${}的区别是什么？

**#{}是预编译处理，${}是字符串替换。**

Mybatis在处理#{}时，会将sql中的#{}替换为?号，调用PreparedStatement的set方法来赋值；

Mybatis在处理${}时，就是把${}替换成变量的值。例如传入表名、SQL片段，排序字段等场景。存在sql注入问题，请谨慎使用！

**使用#{}可以有效的防止SQL注入，提高系统安全性。**

### 什么情况下用${ }、什么情况下用#{ }？

Mybatis 中优先使用 #{}。当需要**动态传入表名或列名**时，使用 ${} 。

### 什么是SQL注入？如何防止？

sql注入是一种**注入攻击**，它通过将任意代码插入数据库查询，使得攻击者完全控制数据库服务器。  攻击者可以使用SQL注入漏洞绕过应用程序安全措施；可以绕过网页或Web应用程序的身份验证和授权，并检索整个SQL数据库的内容；还可以使用SQL注入来添加，修改和删除数据库中的记录。

1. **检查变量数据类型和格式**

2. **过滤特殊符号**

3. **绑定变量，使用预编译语句**

${}是字符串替换，相当于直接显示数据，**#{}是预编译处理**，相当于对数据加上双引号。即#{}是将传入的值当做字符串的形式，先替换为?号，然后调用`PreparedStatement`的set方法来赋值，而$是将传入的数据直接显示生成`sql`语句。

**使用#{}可以有效的防止SQL注入**，`MyBatis`启用了预编译功能，在SQL执行前，会先将上面的SQL发送给数据库进行编译；执行时，直接使用编译好的SQL，替换占位符“?”就可以了。因为SQL注入只能对编译过程起作用，所以这样的方式就很好地避免了SQL注入的问题。

**原理：**

在框架底层，是JDBC中的`PreparedStatement`类在起作用，`PreparedStatement`是我们很熟悉的Statement的子类，**它的对象包含了编译好的SQL语句**。这种“准备好”的方式不仅能提高安全性，而且在多次执行同一个SQL时，能够提高效率。原因是SQL已编译好，再次执行时无需再编译。

```mysql
--Mybatis在处理#{}时
select id,name,age from student where id =#{id}
当前端把id值1传入到后台的时候，就相当于:
select id,name,age from student where id ='1'

--Mybatis在处理${}时
select id,name,age from student where id =${id}
当前端把id值1传入到后台的时候，就相当于：
select id,name,age from student where id = 1
```

### Mybatis中如何实现一对一的查询？

有**联合查询**和**嵌套查询**

联合查询是几个表联合查询,只查询一次,通过在resultMap里面配置**association**节点配置一对一的类就可以完成；

嵌套查询是先查一个表，根据这个表里面的结果的外键id，去再另外一个表里面查询数据,也是通过**association**配置，但另外一个表的查询通过select属性配置。

### Mybatis中如何实现一对多的查询？

有**联合查询**和**嵌套查询**。

联合查询是几个表联合查询,只查询一次,通过在resultMap里面的**collection**节点配置一对多的类就可以完成；

嵌套查询是先查一个表,根据这个表里面的结果的外键id,去再另外一个表里面查询数据,也是通过配置**collection**,但另外一个表的查询通过select节点配置。

### Mybatis是否支持延迟加载？实现原理是什么？

Mybatis仅支持association关联对象和collection关联集合对象的延迟加载，association指的就是一对一，collection指的就是一对多查询。在Mybatis配置文件中，可以配置是否启用延迟加载lazyLoadingEnabled=true|false。

它的原理是，使用CGLIB创建目标对象的代理对象，当调用目标方法时，进入拦截器方法，比如调用a.getB().getName()，拦截器invoke()方法发现a.getB()是null值，那么就会单独发送事先保存好的查询关联B对象的sql，把B查询上来，然后调用a.setB(b)，于是a的对象b属性就有值了，接着完成a.getB().getName()方法的调用。这就是延迟加载的基本原理。

当然了，不光是Mybatis，几乎所有的包括Hibernate，支持延迟加载的原理都是一样的。

### 如何获取自动生成的(主)键值?

insert方法总是返回一个int值，这个值代表的是插入的行数。

如果采用自增长策略，自动生成的键值在insert方法执行完后可以被设置到传入的参数对象中。

示例：
~~~sql
<insert id="insertname" usegeneratedkeys="true" keyproperty="id">
insert into names(name) values(#{name})
</insert>
~~~

~~~java
Name name = new Name();
name.setName("fred");

int rows= mapper.insertname(name);
// 完成后,id已经被设置到对象中
system.out.println("rowsinserted=" + rows);
system.out.println("generatedkeyvalue=" + name.getid());
~~~

### Mybatis的一级、二级缓存

1. 一级缓存:基于PerpetualCache的HashMap本地缓存，其存储作用域为Session，当Sessionflush或close之后，该Session中的所有Cache就将清空，默认打开一级缓存。

2. 二级缓存与一级缓存其机制相同，默认也是采用PerpetualCache，HashMap存储，不同在于其存储作用域为Mapper(Namespace)，并且可自定义存储源，如Ehcache。默认不打开二级缓存，要开启二级缓存，使用二级缓存属性类需要实现Serializable序列化接口(可用来保存对象的状态),可在它的映射文件中配置<cache/>；

3. 对于缓存数据更新机制，当某一个作用域(一级缓存Session/二级缓存Namespaces)的进行了C/U/D操作后，默认该作用域下所有select中的缓存将被clear。

### 什么是MyBatis的接口绑定？

接口绑定，就是在MyBatis中任意定义接口,然后把接口里面的方法和SQL语句绑定,我们直接调用接口方法就可以,这样比起原来了SqlSession提供的方法我们可以有更加灵活的选择和设置。

接口绑定有两种实现方式,一种是通过注解绑定，就是在接口的方法上面加上@Select、@Update等注解，里面包含Sql语句来绑定；另外一种就是通过xml里面写SQL来绑定,在这种情况下,要指定xml映射文件里面的namespace必须为接口的全路径名。当Sql语句比较简单时候,用注解绑定,当SQL语句比较复杂时候,用xml绑定,一般用xml绑定的比较多。

### mapper.xml映射文件中，有哪些标签？

除了常见的`<select>`、`<insert>`、`<update>`、`<delete>`标签以外，还有`<resultMap>`、`<parameterMap>`、`<sql>`、`<include>`、`<selectKey>`

加上动态sql的9个标签，trim、where、set、foreach、if、choose、when、otherwise、bind等

其中`<sql>`为sql片段标签，通过`<include>`标签引入sql片段，`<selectKey>`为不支持自增的主键生成策略标签。

### Mybatis动态sql有什么用？有哪些动态sql？


Mybatis动态sql可以在Xml映射文件内，以标签的形式编写动态sql，执行原理是根据表达式的值完成逻辑判断并动态拼接sql的功能。

Mybatis提供了9种动态sql标签：trim|where|set|foreach|if|choose|when|otherwise|bind。

### 使用MyBatis的mapper接口调用时有哪些要求？


1. Mapper接口方法名和mapper.xml中定义的每个sql的id相同；
2. Mapper接口方法的输入参数类型和mapper.xml中定义的每个sql的parameterType的类型相同；
3. Mapper接口方法的输出参数类型和mapper.xml中定义的每个sql的resultType的类型相同；
4. Mapper.xml文件中的namespace即是mapper接口的类路径。

### 通常一个Xml映射文件，都会写一个Dao接口与之对应，这个Dao接口的工作原理是什么？

Dao接口即Mapper接口。接口的全限名，就是映射文件中的namespace的值；接口的方法名，就是映射文件中Mapper的Statement的id值；接口方法内的参数，就是传递给sql的参数。

Mapper接口是没有实现类的，当调用接口方法时，接口全限名+方法名拼接字符串作为key值，可唯一定位一个MapperStatement。在Mybatis中，每一个<select>、<insert>、<update>、<delete>标签，都会被解析为一个MapperStatement对象。

举例：com.xxf.mappers.StudentDao.findStudentById，可以唯一找到namespace为com.xxf.mappers.StudentDao下面id为findStudentById的MapperStatement。

Mapper接口里的方法，是不能重载的，因为是使用全限名+方法名的保存和寻找策略。Mapper接口的工作原理是JDK动态代理，Mybatis运行时会使用JDK动态代理为Mapper接口生成代理对象proxy，代理对象会拦截接口方法，转而执行MapperStatement所代表的sql，然后将sql执行结果返回。

### Mybatis是如何将sql执行结果封装为目标对象并返回的？

- 方式一：<select> 标签使用 resultType 参数，传递 Java 类，sql 中 select 的字段名保持与 Java 类属性名称一致
- 方式二：使用 <resultMap> 标签，定义数据库列名和对象属性名之间的映射关系
- 方式三：使用注解 select 的字段名保持与接口方法返回的 Java 类或集合的元素类的属性名称一致

~~~sql
方式一
<select id="selectUser" resultType="com.xxf.po.User">
    select * from user where id = #{id}
</select>

方式二
<select id="selectUserByResultMap" resultMap="userMap">
    select * from user where id = #{id}
</select>
<resultMap id="userMap" type="com.xxf.po.User">
    <id property="id" column="id" />
    <result property="mc" column="name"/>
</resultMap>

方式三
@Select("select * from user")
List<User> selectAllUsers();
~~~

### 当实体类中的属性名和表中的字段名不一样怎么办？

第1种：通过在查询的sql语句中定义字段名的别名，让字段名的别名和实体类的属性名一致。

~~~sql
<select id="selectorder" parametertype="int" resultetype="com.xxf.domain.order">
	select 
	order_id as id,
	order_no as orderNo,
	order_price as price 
	form orders 
	where order_id = #{id}
</select>
~~~

第2种：通过<resultMap>来映射字段名和实体类属性名的一一对应的关系。

~~~sql
<select id="getOrder" parameterType="int"resultMap="orderresultmap">
	select * from orders where order_id = #{id}
</select>

<resultMap type="me.gacl.domain.order"  id="orderresultmap">
    <!-– 用id属性来映射主键字段 ––>
    <id property="id" column="order_id">
    <!–- 用 result属性来映射非主键字段 ，property为实体类属性名，column为数据表中的属性––>
    <result property="orderNo" column="order_no"/>
    <result property="price" column="order_price" /> 
</reslutMap>
~~~

### MyBatis 的 Xml 映射文件中，不同的 Xml 映射文件，id 是否可以重复？

不同的 Xml 映射文件，如果配置了 namespace，那么 id 可以重复；如果没有配置 namespace，那么 id 不能重复；毕竟 namespace 不是必须的，只是最佳实践而已。

原因就是 namespace+id 是作为 `Map<String, MappedStatement>` 的 key 使用的，如果没有 namespace，就剩下 id，那么，id 重复会导致数据互相覆盖。**有了 namespace，自然 id 就可以重复，namespace 不同，namespace+id 自然也就不同。**

### 在mapper中如何传递多个参数?

**第一种：DAO层的函数**

~~~java
public User selectUser(String name, String area);
~~~

对应的xml,#{0}代表接收的是dao层中的第一个参数，#{1}代表dao层中第二参数，更多参数一致往后加即可。

~~~sql
<select id="selectUser" resultMap="BaseResultMap">
	select * from user where user_name=#{0} and user_area=#{1}
</select>
~~~

**第二种：使用@param注解**

如果传递参数是对象，可以用对象.参数的形式，如下user.password

~~~java
public interface userMapper{
	User selectUser(@param("username")String username, @param("user")User user);
}
~~~

然后,就可以在xml像下面这样使用(推荐封装为一个map,作为单个参数传递给mapper:

~~~sql
<select id="selectUser" resultType="user">
	select id, username, password
	from some_table
	where username=#{username} and password=#{user.password}
</select>
~~~

**第三种：多个参数封装成map**


~~~java
try {
	// 映射文件的命名空间.SQL片段的ID，就可以调用对应的映射文件中的
	// 由于我们的参数超过了两个，而方法中只有一个Object参数收集，因此我们使用Map集合来装载我们的参数
	Map<String, Object> map = new HashMap();
	map.put("start", start);
	map.put("end", end);
	return sqlSession.selectList("StudentID.pagination", map);
} catch (Exceptione) {
	e.printStackTrace();
	sqlSession.rollback();
	throwe;
} finally {
	MybatisUtil.closeSqlSession();
}
~~~

### Mapper编写有哪几种方式？

**第一种：接口实现类继承SqlSessionDaoSupport**
使用此种方法需要编写mapper接口，mapper接口实现类、mapper.xml文件。

1. 在sqlMapConfig.xml中配置mapper.xml的位置

~~~xml
<mappers>
	<mapper resource="mapper.xml文件的地址"/>
	<mapper resource="mapper.xml文件的地址"/>
</mappers>
~~~

2. 定义mapper接口
3. 实现类集成SqlSessionDaoSupport
   mapper方法中可以this.getSqlSession()进行数据增删改查。
4. spring配置

~~~xml
<bean id="" class="mapper接口的实现">
	<property name="sqlSessionFactory" ref="sqlSessionFactory"></property>
</bean>
~~~

**第二种：使用org.mybatis.spring.mapper.MapperFactoryBean**

1. 在sqlMapConfig.xml中配置mapper.xml的位置，如果mapper.xml和mappre接口的名称相同且在同一个目录，这里可以不用配置

~~~xml
<mappers>
	<mapper resource="mapper.xml文件的地址"/>
	<mapper resource="mapper.xml文件的地址"/>
</mappers>
~~~

2. 定义mapper接口：

  * mapper.xml中的namespace为mapper接口的地址
  * mapper接口中的方法名和mapper.xml中的定义的statement的id保持一致
  * Spring中定义

~~~xml
<bean id="" class="org.mybatis.spring.mapper.MapperFactoryBean">
	<property name="mapperInterface" value="mapper接口地址"/>
	<property name="sqlSessionFactory" ref="sqlSessionFactory"/>
</bean>
~~~

**第三种：使用mapper扫描器**

1. mapper.xml文件编写：

mapper.xml中的namespace为mapper接口的地址；
mapper接口中的方法名和mapper.xml中的定义的statement的id保持一致；如果将mapper.xml和mapper接口的名称保持一致则不用在sqlMapConfig.xml中进行配置。

2. 定义mapper接口：

注意mapper.xml的文件名和mapper的接口名称保持一致，且放在同一个目录

3. 配置mapper扫描器：

~~~xml
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
	<property name="basePackage" value="mapper接口包地址"/>
	<property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
</bean>
~~~


4. 使用扫描器后从spring容器中获取mapper的实现对象。

### Mybatis是如何进行分页的？分页插件的原理是什么？

Mybatis使用RowBounds对象进行分页，它是针对ResultSet结果集执行的内存分页，而非物理分页。可以在sql内直接书写带有物理分页的参数来完成物理分页功能，也可以使用分页插件来完成物理分页。

分页插件的基本原理是使用Mybatis提供的插件接口，实现自定义插件，在插件的拦截方法内拦截待执行的sql，然后重写sql，根据dialect方言，添加对应的物理分页语句和物理分页参数。

### 简述Mybatis的插件运行原理，以及如何编写一个插件。

Mybatis仅可以编写针对ParameterHandler、ResultSetHandler、StatementHandler、Executor这4种接口的插件，Mybatis使用JDK的动态代理，为需要拦截的接口生成代理对象以实现接口方法拦截功能，每当执行这4种接口对象的方法时，就会进入拦截方法，具体就是InvocationHandler的invoke()方法，当然，只会拦截那些你指定需要拦截的方法。

编写插件：实现Mybatis的Interceptor接口并复写intercept()方法，然后在给插件编写注解，指定要拦截哪一个接口的哪些方法即可，记住，别忘了在配置文件中配置你编写的插件。
