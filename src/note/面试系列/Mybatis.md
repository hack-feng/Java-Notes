### 什么是Mybatis？
1. Mybatis是一个半ORM（对象关系映射）框架，它内部封装了JDBC，开发时只需要关注Sql本身，不需要花费精力其处理加载驱动、创建连接、创建statement等繁杂的过程。
程序员直接编写原生的sql，可以严格控制执行的性能，灵活度较高。

2. Mybatis可以使用XML或注解来配置和映射原生sql，将POJO映射成数据库的记录，避免了几乎所有的JDBC代码和手动设置参数及获取结果集。

3. 通过XML文件或注解的方式将要执行的各种statement配置起来，并通过JAVA对象和statement的sql动态参数进行映射生成最终执行的sql语句，最后由mybatis框架执行sql并将结果映射为JAVA对象并返回。

### #{}和${}的区别是什么？
1. `#{}`是预编译处理；
2. Mybatis在处理#{}时，会将sql中的`#{}`替换为`?`号，调用PreparedStatement的set方法来赋值；
3. 使用`#{}`可以有效的防止SQL注入，提高系统安全性。


1. `${}`是字符串替换；
2. Mybatis在处理`${}`时，就是把`${}`替换为变量的值；
3. 存在sql注入的风险。

### Mybatis除了select|delete|update|insert外 还有哪些标签？
`<resultMap>、<sql>、<selectKey>、<include>、<parameterMap>`
除此之外，还有9个动态sql的标签

### Mybatis动态sql有什么用？执行原理？有哪些动态Sql标签?

Mybatis动态sql可以在Xml映射文件内，以标签的形式编写动态sql。

执行原理：根据表达式的值，完成逻辑判断并动态拼接sql的功能。

动态Sql标签：trim、where、if、choose、when、otherwise、foreach、bind、set

### Mybatis的xml配置文件，不同的Xml映射文件中，id是否可以重复？

如果没有配置namespace，id不可重复；如果配置了，id可以重复。

### Mybatis是否支持延迟加载？

Mybatis 仅支持association关联对象和collection的关联集合对象的延迟加载。可以在Mybatis的配置文件中，配置lazyLoadingEnabled=true|false。

懒加载的原理是：使用CGLIB创建目标对象的代理对象，当调用目标方法时，进入拦截器方法，比如调用a.getB().getName()，拦截器的invoke()方法发现a.getB()是null值，
那么就会单独发送事先保存好的查询关联B对象的sql，把B查询上来，然后调用a.setB(b)，于是a的对象b就有值了，接着完成a.getB().getName()方法的调用。

### Mybatis的一级缓存、二级缓存
* 一级缓存

基于PerpetualCache的HashMap本地缓存，其存储作用域为session，当session flush或clone后，该Session的Cache就将清空。
一级缓存默认是打开的。

* 二级缓存
二级缓存与一级缓存的机制相同，默认也是采用PerpetualCache的HashMap存储。不同的是其存储的作用域为Mapper(namespace)，并且可以自定义存储源，如Ehcache。
二级缓存默认是关闭的，要开启二级缓存，需要实现Serializable序列化接口。可以在映射文件中配置<cache/>启用二级缓存。

对于缓存数据更新机制，当某一个作用域（一级缓存Session/二级缓存Namespaces）进行了C/U/D操作后，默认该作用域下的所有select中的缓存被clear。

###简述一下Mybatis插件的运行原理
Mybatis仅可以编写针对ParameterHandler、ResultSetHandler、StatementHandler、Executor的4中接口插件。
Mybatis使用JDK的动态代理，为需要拦截的接口生成代理对象以实现接口方法的拦截功能，当执行这4种接口对象的方法时，就会进入到拦截方法，
具体就是InvocationHandler的invoke方法。当然，只会拦截那些你指定需要拦截的方法。








