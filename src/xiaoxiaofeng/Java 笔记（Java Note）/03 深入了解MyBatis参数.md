# 深入了解MyBatis参数 #

相信很多人可能都遇到过下面这些异常：

 *  `"Parameter 'xxx' not found. Available parameters are […]"`
 *  `"Could not get property 'xxx' from xxxClass. Cause:`
 *  `"The expression 'xxx' evaluated to a null value."`
 *  `"Error evaluating expression 'xxx'. Return value (xxxxx) was not iterable."`

不只是上面提到的这几个，我认为有很多的错误都产生在和参数有关的地方。

想要避免参数引起的错误，我们需要深入了解参数。

想了解参数，我们首先看MyBatis处理参数和使用参数的全部过程。

本篇由于为了便于理解和深入，使用了大量的源码，因此篇幅较长，需要一定的耐心看完，本文一定会对你起到很大的帮助。

## 参数处理过程 ##

### 处理接口形式的入参 ###

在使用MyBatis时，有两种使用方法。一种是使用的接口形式，另一种是通过`SqlSession`调用命名空间。这两种方式在传递参数时是不一样的，命名空间的方式更直接，但是多个参数时需要我们自己创建`Map`作为入参。相比而言，使用接口形式更简单。

接口形式的参数是由MyBatis自己处理的。如果使用接口调用，入参需要经过额外的步骤处理入参，之后就和命名空间方式一样了。

在**MapperMethod.java**会首先经过下面方法来转换参数：

``````````java
public Object convertArgsToSqlCommandParam(Object[] args) { 
    final int paramCount = params.size(); 
    if (args == null || paramCount == 0) { 
      return null; 
    } else if (!hasNamedParameters && paramCount == 1) { 
      return args[params.keySet().iterator().next()]; 
    } else { 
      final Map<String, Object> param = new ParamMap<Object>(); 
      int i = 0; 
      for (Map.Entry<Integer, String> entry : params.entrySet()) { 
        param.put(entry.getValue(), args[entry.getKey()]); 
        // issue #71, add param names as param1, param2...but ensure backward compatibility 
        final String genericParamName = "param" + String.valueOf(i + 1); 
        if (!param.containsKey(genericParamName)) { 
          param.put(genericParamName, args[entry.getKey()]); 
        } 
        i++; 
      } 
      return param; 
    } 
  }
``````````

在这里有个很关键的`params`，这个参数类型为`Map<Integer, String>`，他会根据接口方法按顺序记录下接口参数的定义的名字，如果使用`@Param`指定了名字，就会记录这个名字，如果没有记录，那么就会使用它的序号作为名字。

例如有如下接口：

``````````java
List<User> select(@Param('sex')String sex,Integer age);
``````````

那么他对应的`params`如下:

``````````json
{ 
      0:'sex', 
      1:'1' 
  }
``````````

继续看上面的`convertArgsToSqlCommandParam`方法，这里简要说明3种情况：

 *  入参为`null`或没有时，参数转换为`null`
 *  没有使用`@Param`注解并且只有一个参数时，返回这一个参数
 *  使用了`@Param`注解或有多个参数时，将参数转换为`Map`1类型，并且还根据参数顺序存储了key为param1,param2的参数。
    **注意：从第3种情况来看，建议各位有多个入参的时候通过`@Param`指定参数名，方便后面（动态sql）的使用。**

经过上面方法的处理后，在`MapperMethod`中会继续往下调用命名空间方式的方法：

``````````
Object param = method.convertArgsToSqlCommandParam(args); 
  result = sqlSession.<E>selectList(command.getName(), param);
``````````

从这之后开始按照统一的方式继续处理入参。

1:这里的`Map`实际类型为`ParamMap<V>`,和下一步处理集合中的`StrictMap<V>`类是两个功能完全一样的类。

### 处理集合 ###

不管是`selectOne`还是`selectMap`方法，归根结底都是通过`selectList`进行查询的，不管是`delete`还是`insert`方法，都是通过`update`方法操作的。在`selectList`和`update`中所有参数的都进行了统一的处理。

在**DefaultSqlSession.java**中的`wrapCollection`方法：

``````````java
private Object wrapCollection(final Object object) { 
    if (object instanceof Collection) { 
      StrictMap<Object> map = new StrictMap<Object>(); 
      map.put("collection", object); 
      if (object instanceof List) { 
        map.put("list", object); 
      } 
      return map;      
    } else if (object != null && object.getClass().isArray()) { 
      StrictMap<Object> map = new StrictMap<Object>(); 
      map.put("array", object); 
      return map; 
    } 
    return object; 
  }
``````````

**这里特别需要注意的一个地方是`map.put("collection", object)`，这个设计是为了支持`Set`类型，需要等到MyBatis 3.3.0版本才能使用。**

`wrapCollection`处理的是只有一个参数时，集合和数组的类型转换成`Map`2类型，并且有默认的Key，从这里你能大概看到为什么`<foreach>`中默认情况下写的**array**和**list**（`Map`类型**没有**默认值**map**）。

2:这里的`Map`实际类型为`StrictMap<V>`,和接口处理中的`ParamMap<V>`类是两个功能完全一样的类。

## 参数的使用 ##

参数的使用分为两部分：

 *  第一种就是常见`#{username}`或者`${username}`。
 *  第二种就是在动态SQL中作为条件，例如`<if test="username!=null and username !=''">`。

下面对这两种进行详细讲解，为了方便理解，先讲解第二种情况。

### 在动态SQL条件中使用参数 ###

关于动态SQL的基础内容可以查看[官方文档][Link 1]。

动态SQL为什么会处理参数呢？

主要是因为动态SQL中的`<if>,<bind>,<foreache>`都会用到表达式，表达式中会用到属性名，属性名对应的属性值如何获取呢？获取方式就在这关键的一步。不知道多少人遇到**Could not get property xxx from xxxClass**或**: Parameter 'xxx' not found. Available parameters are\[…\]**，都是不懂这里引起的。

在**DynamicContext.java**中，从构造方法看起：

``````````java
public DynamicContext(Configuration configuration, Object parameterObject) { 
    if (parameterObject != null && !(parameterObject instanceof Map)) { 
      MetaObject metaObject = configuration.newMetaObject(parameterObject); 
      bindings = new ContextMap(metaObject); 
    } else { 
      bindings = new ContextMap(null); 
    } 
    bindings.put(PARAMETER_OBJECT_KEY, parameterObject); 
    bindings.put(DATABASE_ID_KEY, configuration.getDatabaseId()); 
  }
``````````

这里的`Object parameterObject`就是我们经过前面两步处理后的参数。这个参数经过前面两步处理后，到这里的时候，他只有下面三种情况：

 *  `null`，如果没有入参或者入参是`null`，到这里也是`null`。
 *  `Map`类型，除了`null`之外，前面两步主要是封装成`Map`类型。
 *  数组、集合和`Map`**以外的**`Object`类型，可以是基本类型或者实体类。
    看上面构造方法，如果参数是**1,2**情况时，执行代码`bindings = new ContextMap(null);`参数是**3**情况时执行`if`中的代码。我们看看`ContextMap`类，这是一个内部静态类，代码如下：

``````````java
static class ContextMap extends HashMap<String, Object> { 
    private MetaObject parameterMetaObject; 
    public ContextMap(MetaObject parameterMetaObject) { 
      this.parameterMetaObject = parameterMetaObject; 
    } 
    public Object get(Object key) { 
      String strKey = (String) key; 
      if (super.containsKey(strKey)) { 
        return super.get(strKey); 
      } 
      if (parameterMetaObject != null) { 
        // issue #61 do not modify the context when reading 
        return parameterMetaObject.getValue(strKey); 
      } 
      return null; 
    } 
  }
``````````

我们先继续看`DynamicContext`的构造方法，在`if/else`之后还有两行：

``````````java
bindings.put(PARAMETER_OBJECT_KEY, parameterObject); 
  bindings.put(DATABASE_ID_KEY, configuration.getDatabaseId());
``````````

其中两个Key分别为：

``````````java
public static final String PARAMETER_OBJECT_KEY = "_parameter"; 
  public static final String DATABASE_ID_KEY = "_databaseId";
``````````

也就是说**1,2**两种情况的时候，参数值只存在于`"_parameter"`的键值中。**3**情况的时候，参数值存在于`"_parameter"`的键值中，也存在于`bindings`本身。

当动态SQL取值的时候会通过OGNL从`bindings`中获取值。MyBatis在OGNL中注册了`ContextMap`:

``````````java
static { 
    OgnlRuntime.setPropertyAccessor(ContextMap.class, new ContextAccessor()); 
  }
``````````

当从`ContextMap`取值的时候，会执行`ContextAccessor`中的如下方法：

``````````java
@Override 
  public Object getProperty(Map context, Object target, Object name) 
      throws OgnlException { 
    Map map = (Map) target; 
  
    Object result = map.get(name); 
    if (map.containsKey(name) || result != null) { 
      return result; 
    } 
  
    Object parameterObject = map.get(PARAMETER_OBJECT_KEY); 
    if (parameterObject instanceof Map) { 
      return ((Map)parameterObject).get(name); 
    } 
  
    return null; 
  }
``````````

参数中的`target`就是`ContextMap`类型的，所以可以直接强转为`Map`类型。参数中的`name`就是我们写在动态SQL中的属性名。

下面举例说明这三种情况：

 *  null的时候： 不管`name`是什么（`name="_databaseId"`除外，可能会有值），此时`Object result = map.get(name);`得到的`result=null`。 在`Object parameterObject = map.get(PARAMETER_OBJECT_KEY);`中`parameterObject=null`，因此最后返回的结果是`null`。 在这种情况下，不管写什么样的属性，值都会是null，并且不管属性是否存在，都不会出错。
 *  `Map`类型： 此时`Object result = map.get(name);`一般也不会有值，因为参数值只存在于`"_parameter"`的键值中。 然后到`Object parameterObject = map.get(PARAMETER_OBJECT_KEY);`，此时获取到我们的参数值。 在从参数值`((Map)parameterObject).get(name)`根据`name`来获取属性值。 在这一步的时候，如果`name`属性不存在，就会报错：

``````````java
throw new BindingException("Parameter '" + key + "' not found. Available parameters are " + keySet());
``````````

`name`属性是什么呢，有什么可选值呢？这就是**处理接口形式的入参**和**处理集合**处理后所拥有的Key。 如果你遇到过类似异常，相信看到这儿就明白原因了。

 *  数组、集合和`Map`**以外的**`Object`类型： 这种类型经过了下面的处理：

``````````java
MetaObject metaObject = configuration.newMetaObject(parameterObject); 
   bindings = new ContextMap(metaObject);
``````````

`MetaObject`是MyBatis的一个反射类，可以很方便的通过`getValue`方法获取对象的各种属性（支持集合数组和Map，可以多级属性点`.`访问，如`user.username`,`user.roles[1].rolename`）。 现在分析这种情况。 首先通过`name`获取属性时`Object result = map.get(name);`，根据上面`ContextMap`类中的`get`方法：

``````````java
public Object get(Object key) { 
    String strKey = (String) key; 
    if (super.containsKey(strKey)) { 
      return super.get(strKey); 
    } 
    if (parameterMetaObject != null) { 
      return parameterMetaObject.getValue(strKey); 
    } 
    return null; 
  }
``````````

可以看到这里会优先从`Map`中取该属性的值，如果不存在，那么一定会执行到下面这行代码：

``````````java
return parameterMetaObject.getValue(strKey)
``````````

如果`name`刚好是对象的一个属性值，那么通过`MetaObject`反射可以获取该属性值。如果该对象不包含`name`属性的值，就会报错：

``````````java
throw new ReflectionException("Could not get property '" + prop.getName() + "' from " + object.getClass() + ".  Cause: " + t.toString(), t);
``````````

理解这三种情况后，使用动态SQL应该不会有参数名方面的问题了。

### 在SQL语句中使用参数 ###

SQL中的两种形式`#{username}`或者`${username}`，虽然看着差不多，但是实际处理过程差别很大，而且很容易出现莫名其妙的错误。

`${username}`的使用方式为OGNL方式获取值，和上面的动态SQL一样，这里先说这种情况。

#### $\{propertyName\}参数 ####

在**TextSqlNode.java**中有一个内部的静态类`BindingTokenParser`，现在只看其中的`handleToken`方法：

``````````java
@Override 
  public String handleToken(String content) { 
    Object parameter = context.getBindings().get("_parameter"); 
    if (parameter == null) { 
      context.getBindings().put("value", null); 
    } else if (SimpleTypeRegistry.isSimpleType(parameter.getClass())) { 
      context.getBindings().put("value", parameter); 
    } 
    Object value = OgnlCache.getValue(content, context.getBindings()); 
    String srtValue = (value == null ? "" : String.valueOf(value)); // issue #274 return "" instead of "null" 
    checkInjection(srtValue); 
    return srtValue; 
  }
``````````

从`put("value"`这个地方可以看出来，MyBatis会创建一个默认为`"value"`的值，也就是说，在xml中的SQL中可以直接使用`${value}`，从`else if`可以看出来，只有是简单类型的时候，才会有值。

关于这点，举个简单例子，如果接口为`List<User> selectOrderby(String column)`，如果xml内容为：

``````````xml
<select id="selectOrderby" resultType="User"> 
  select * from user order by ${value} 
  </select>
``````````

这种情况下，虽然没有指定一个value属性，但是MyBatis会自动把参数column赋值进去。

再往下的代码：

``````````java
Object value = OgnlCache.getValue(content, context.getBindings()); 
  String srtValue = (value == null ? "" : String.valueOf(value));
``````````

这里和动态SQL就一样了，通过OGNL方式来获取值。

**看到这里使用OGNL这种方式时，你有没有别的想法？\*\***特殊用法：\*\*你是否在SQL查询中使用过某些固定的码值？一旦码值改变的时候需要改动很多地方，但是你又不想把码值作为参数传进来，怎么解决呢？你可能已经明白了。就是通过OGNL的方式，例如有如下一个码值类：

``````````java
package com.abel533.mybatis; 
  public interface Code{ 
      public static final String ENABLE = "1"; 
      public static final String DISABLE = "0"; 
  }
``````````

如果在xml，可以这么使用：

``````````xml
<select id="selectUser" resultType="User"> 
      select * from user where enable = ${@com.abel533.mybatis.Code@ENABLE} 
  </select>
``````````

除了码值之外，你可以使用OGNL支持的各种方法，如调用静态方法。

#### \#\{propertyName\}参数 ####

这种方式比较简单，复杂属性的时候使用的MyBatis的MetaObject。

在**DefaultParameterHandler.java**中：

``````````java
public void setParameters(PreparedStatement ps) throws SQLException { 
    ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId()); 
    List<ParameterMapping> parameterMappings = boundSql.getParameterMappings(); 
    if (parameterMappings != null) { 
      for (int i = 0; i < parameterMappings.size(); i++) { 
        ParameterMapping parameterMapping = parameterMappings.get(i); 
        if (parameterMapping.getMode() != ParameterMode.OUT) { 
          Object value; 
          String propertyName = parameterMapping.getProperty(); 
          if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for additional params 
            value = boundSql.getAdditionalParameter(propertyName); 
          } else if (parameterObject == null) { 
            value = null; 
          } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) { 
            value = parameterObject; 
          } else { 
            MetaObject metaObject = configuration.newMetaObject(parameterObject); 
            value = metaObject.getValue(propertyName); 
          } 
          TypeHandler typeHandler = parameterMapping.getTypeHandler(); 
          JdbcType jdbcType = parameterMapping.getJdbcType(); 
          if (value == null && jdbcType == null) { 
            jdbcType = configuration.getJdbcTypeForNull(); 
          } 
          typeHandler.setParameter(ps, i + 1, value, jdbcType); 
        } 
      } 
    } 
  }
``````````

上面这段代码就是从参数中取`#{propertyName}`值的方法，这段代码的主要逻辑就是`if/else`判断的地方，单独拿出来分析：

``````````java
if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for additional params 
    value = boundSql.getAdditionalParameter(propertyName); 
  } else if (parameterObject == null) { 
    value = null; 
  } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) { 
    value = parameterObject; 
  } else { 
    MetaObject metaObject = configuration.newMetaObject(parameterObject); 
    value = metaObject.getValue(propertyName); 
  }
``````````

 *  首先看第一个`if`，当使用`<foreach>`的时候，MyBatis会自动生成额外的动态参数，如果`propertyName`是动态参数，就会从动态参数中取值。
 *  第二个`if`，如果参数是`null`，不管属性名是什么，都会返回`null`。
 *  第三个`if`，如果参数是一个简单类型，或者是一个注册了`typeHandler`的对象类型，就会直接使用该参数作为返回值，和属性名无关。
 *  最后一个`else`，这种情况下是复杂对象或者`Map`类型，通过反射方便的取值。

下面我们说明上面四种情况下的参数名注意事项。

1. 动态参数，这里的参数名和值都由MyBatis动态生成的，因此我们没法直接接触，也不需要管这儿的命名。但是我们可以了解一下这儿的命名规则，当以后错误信息看到的时候，我们可以确定出错的地方。 在ForEachSqlNode.java中：

```java
private static String itemizeItem(String item, int i) {
    return new StringBuilder(ITEM_PREFIX).append(item).append("_").append(i).toString();
}
```

其中`ITEM_PRFIX`为`public static final String ITEM_PREFIX = "__frch_";`。 如果在`<foreach>`中的`collection="userList" item="user"`,那么对`userList`循环产生的动态参数名就是:

> __frch_user_0,__frch_user_1,__frch_user_2...

如果访问动态参数的属性，如`user.username`会被处理成`__frch_user_0.username`，这种参数值的处理过程在更早之前解析SQL的时候就已经获取了对应的参数值。具体内容看下面有关`<foreach>`的详细内容。

1. 参数为`null`，由于这里的判断和参数名无关，因此入参`null`的时候，在xml中写的`#{name}`不管`name`写什么，都不会出错，值都是`null`。
2. 可以直接使用`typeHandler`处理的类型。最常见的就是基本类型，例如有这样一个接口方法`User selectById(@Param("id")Integer id)`，在xml中使用`id`的时候，我们可以随便使用属性名，不管用什么样的属性名，值都是`id`。
3. 复杂对象或者`Map`类型一般都是我们需要注意的地方，这种情况下，就必须保证入参包含这些属性，如果没有就会报错。这一点和可以参考上面有关`MetaObject`的地方。

#### `<foreach>`详解

所有动态SQL类型中，`<foreach>`似乎是遇到问题最多的一个。

例如有下面的方法：

```xml
<insert id="insertUserList">
  INSERT INTO user(username,password)
  VALUES
  <foreach collection="userList" item="user" separator=",">
    (#{user.username},#{user.password})
  </foreach>
</insert>
```

对应的接口：

```java
int insertUserList(@Param("userList")List<User> list);
```

我们通过`foreach`源码，看看MyBatis如何处理上面这个例子。

在**ForEachSqlNode.java**中的`apply`方法中的前两行：

```java
Map<String, Object> bindings = context.getBindings();
final Iterable<?> iterable = evaluator.evaluateIterable(collectionExpression, bindings);
```

这里的`bindings`参数熟悉吗？上面提到过很多。经过一系列的参数处理后，这儿的bindings如下：

```javascript
{
  "_parameter":{
    "param1":list,
    "userList":list
  },
  "_databaseId":null,
}
```

`collectionExpression`就是`collection="userList"`的值`userList`。

我们看看`evaluator.evaluateIterable`如何处理这个参数，在**ExpressionEvaluator.java**中的`evaluateIterable`方法：

```java
public Iterable<?> evaluateIterable(String expression, Object parameterObject) {
    Object value = OgnlCache.getValue(expression, parameterObject);
    if (value == null) {
      throw new BuilderException("The expression '" + expression + "' evaluated to a null value.");
    }
    if (value instanceof Iterable) {
      return (Iterable<?>) value;
    }
    if (value.getClass().isArray()) {
        int size = Array.getLength(value);
        List<Object> answer = new ArrayList<Object>();
        for (int i = 0; i < size; i++) {
            Object o = Array.get(value, i);
            answer.add(o);
        }
        return answer;
    }
    if (value instanceof Map) {
      return ((Map) value).entrySet();
    }
    throw new BuilderException("Error evaluating expression '" + expression + "'.  Return value (" + value + ") was not iterable.");
}
```

首先通过看第一行代码：

```java
Object value = OgnlCache.getValue(expression, parameterObject);
```

这里通过OGNL获取到了`userList`的值。获取`userList`值的时候可能出现异常，具体可以参考上面动态SQL部分的内容。

`userList`的值分四种情况。

1. `value == null`，这种情况直接抛出异常`BuilderException`。
2. `value instanceof Iterable`，实现`Iterable`接口的直接返回，如`Collection`的所有子类，通常是`List`。
3. `value.getClass().isArray()`数组的情况，这种情况会转换为`List`返回。
4. `value instanceof Map`如果是`Map`，通过`((Map) value).entrySet()`返回一个`Set`类型的参数。

通过上面处理后，返回的值，是一个`Iterable`类型的值，这个值可以使用`for (Object o : iterable)`这种形式循环。

在`ForEachSqlNode`中对`iterable`循环的时候，有一段需要关注的代码：

```java
if (o instanceof Map.Entry) {
	@SuppressWarnings("unchecked") 
	Map.Entry<Object, Object> mapEntry = (Map.Entry<Object, Object>) o;
	applyIndex(context, mapEntry.getKey(), uniqueNumber);
	applyItem(context, mapEntry.getValue(), uniqueNumber);
} else {
	applyIndex(context, i, uniqueNumber);
	applyItem(context, o, uniqueNumber);
}
```

如果是通过`((Map) value).entrySet()`返回的`Set`，那么循环取得的子元素都是`Map.Entry`类型，这个时候会将`mapEntry.getKey()`存储到`index`中，`mapEntry.getValue()`存储到`item`中。

如果是`List`，那么会将序号`i`存到`index`中，`mapEntry.getValue()`存储到`item`中。

# 最后

这篇文章很长，写这篇文章耗费的时间也很长，超过10小时，写到半夜两点都没写完。

这篇文章真的非常有用，如果你对Mybatis有一定的了解，这篇文章几乎是必读的一篇。

如果各位发现文中错误或者其他问题欢迎留言或加群详谈。


[Link 1]: http://mybatis.github.io/mybatis-3/zh/dynamic-sql.html