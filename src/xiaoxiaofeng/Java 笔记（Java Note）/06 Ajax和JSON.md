##  Ajax

+   Ajax（Asynchronous JavaScript and XML），异步 JavaScript 和 XML
+   作用：增强用户体验——可以在用户浏览网页的同时与服务器进行异步交互和实现网页内容的局部更新
+   Ajax 应用的工作过程
    1.  JavaScript 使用 XMLHttpRequest 对象（简称 XHR）向服务器发送请求
    2.  JavaScript 使用 XMLHttpRequest 对象解析服务器响应数据
    3.  JavaScript 通过操作 DOM 动态更新 HTML 页面

### XMLHttpRequest 对象

+   属性  
    responseType: 响应信息为二进制数据时需设置 `xhr.responseType = "blob";`  
    onreadystatechange：指定当 readyState 属性**改变**时的事件处理句柄  
    onload: 请求成功完成时调用的函数  
    readyState：返回 XMLHTTP 请求的当前状态（4 表示数据接收完毕）  
    status：返回当前请求的 http 状态码（200 表示 OK）  
    response: 响应信息  
    responseText：将响应信息作为字符串返回  
    responseXML：将响应信息格式化为 Xml Document 对象并返回
    
+   构造函数：new XMLHttpRequest()
    
+   实例方法  
    `open(bstrMethod, bstrUrl, varAsync)`：创建一个 http 请求，并指定此请求的发送方法（GET 或 POST），请求的 URL，是否为异步请求（默认为 true）  
    `setRequestHeader(bstrHeader, bstrValue)`：设置请求的某个 http 头，POST 请求需设置 "Content-Type" 为 "application/x-www-form-urlencoded"  
    `send(data)`：发送请求到 http 服务器并接收回应，data 为表单参数
    

```javascript
// 创建 XHR 对象
var xhr = new XMLHttpRequest();
// xhr.withCredentials = true; // 设置跨域请求时是否携带 cookie
// 监听 readyState 状态，处理响应
xhr.onreadystatechange = function(callback) {
    if (xhr.readyState == 4) {
        if(xhr.status == 200) {
              callback(xhr.responseText);
        } else {
            ...
        }
    }
};
// 创建请求
xhr.open("get", "/gettime.do?" + new Date().getTime(), true);
// 发送请求
xhr.send();

// POST 请求
xhr.open("post", "http://localhost/user");
xhr.send(formdata);
```

###  FormData 对象

+   formData 是 Ajax2.0（XMLHttpRequest Level 2）新提出的接口，利用 FormData 对象可以将 form 表单元素的 name 与 value 进行组合，实现**表单数据的序列化**，从而减少表单元素的拼接，提高工作效率
    
+   构造函数  
    `new FormData()`：创建一个空对象实例  
    `new FormData(document.getElementById("myForm"))`：用表单来初始化
    
+   对象方法  
    `append()：向 FormData 中添加新的属性值，FormData 对应的属性值存在也不会覆盖原值，而是新增一个值，如果属性不存在则新增一项属性值`set()`：给 FormData 设置属性值，如果 FormData 对应的属性值存在则覆盖原值，否则新增一项属性值`delete()`：从 FormData 对象里面删除一个键值对`get()`：返回在 FormData 对象中与给定键关联的第一个值`getAll()：返回一个包含 FormData 对象中与给定键关联的所有值的数组  
    `has()`：返回一个布尔值表明 FormData 对象是否包含某些键  
    `keys()`：返回一个包含所有键的 iterator 对象  
    `values()`：返回一个包含所有值的 iterator 对象
    

## 跨域资源访问

+   同源策略：限制从一个源加载的文档或脚本与来自另一个源的资源进行交互（同源是指协议、域名、端口都相同）
+   注意：同源策略只是浏览器的安全策略，不是 HTTP 协议的一部分

###  CORS

+   [CORS (opens new window)](https://developer.mozilla.org/zh-CN/docs/web/http/cors) 是一个 W3C 标准，全称是“跨域资源共享”（Cross-Origin Resource Sharing）
    
+   跨域资源共享（CORS）标准新增了一组 HTTP 首部字段，允许服务器声明哪些源站有权限访问哪些资源。另外，规范要求，对那些可能对服务器数据产生副作用的 HTTP 请求方法（特别是 GET 以外的 HTTP 请求，或者搭配某些 MIME 类型的 POST 请求），浏览器必须首先使用 OPTIONS 方法发起一个预检请求（preflight request），从而获知服务端是否允许该跨域请求。服务器确认允许之后，才发起实际的 HTTP 请求。在预检请求的返回中，服务器端也可以通知客户端，是否需要携带身份凭证（包括 Cookies 和 HTTP 认证相关数据）。
    
+   普通跨域请求，只需服务端设置 Access-Control-Allow-Origin 即可，前端无须设置；**若要请求时携带 cookie，则前后端都需要设置**（所读取的 cookie 为跨域请求接口所在域的 cookie，而非当前页）
    
+   OPTIONS 预检请求的 Response Headers
```shell
    Access-Control-Allow-Credentials: true
    Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Auth-Key, Accept, Accept-Encoding, Accept-Language, Host, Referer, User-Agent
    Access-Control-Allow-Methods: GET, POST, OPTIONS
    Access-Control-Allow-Origin: http://app1.example.com
    Access-Control-Max-Age: 1728000
    Connection: keep-alive
    Content-Length: 0
    Date: Wed, 02 Sep 2020 16:26:35 GMT
    Server: nginx/1.16.1
```

###  Frame 代理

### Nginx 代理

```nginx
location / {
    #add_header Access-Control-Allow-Origin *; # 设置为 * 时，浏览器将不会发送 cookie，即使在 XHR 设置了 withCredentials（当前端跨域访问不带 cookie 时，可为 *）
    add_header Access-Control-Allow-Origin $scheme://$host;
    add_header Access-Control-Allow-Credentials true; # 该值只能是 true，否则不返回，为 true 时，Access-Control-Allow-Origin 不能为 *
    add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
    add_header Access-Control-Allow-Headers 'Origin, X-Requested-With, Content-Type, Auth-Key, Accept, Accept-Encoding, Accept-Language, Host, Referer, User-Agent';

    if ($request_method = 'OPTIONS') {
        return 204;
    }
}
```

### JSONP

+   JSONP（JSON with padding），填充式 JSON 或参数式 JSON，一种跨域读取数据的技术
+   JSONP 由两部分组成：**回调函数**是当响应到来时应该在页面中调用的函数，回调函数的名字一般是在请求中指定；**数据**是传入回调函数中的 JSON 数据
+   可通过使用动态`<script >` 标签来实现：为 src 属性指定一个跨域 URL，返回的数据会**调用 js 解释器进行解析**

##  JSON 数据格式

+   JSON（JavaScript Object Notation），JS 对象标记，数据交换格式
    
+   JSON 有两种结构
    
    1.  对象（object），表示一组无序的键值对，一个对象由 { } 括起来的，“名称/值”对之间使用 , 分隔，每个“名称”后跟一个 : ，格式例如 `{"key1":value1, "key2":value2, "key3":value3, ...}`（注意：**对象的属性名必须加双引号**）
    2.  数组（array），表示一组有序的值，一个数组由 \[ \] 括起来，值之间使用 , 分隔，格式例如 `[{"key1":value1,"key2":value2,"key3":value3}, {"key1":value1,"key2":value2,"key3":value3}, ...]`
+   JSON 值可以是：数字（整数或浮点数）、字符串（在**双引号**中）、逻辑值（true 或 false）、数组（在方括号中）、对象（在花括号中）、null
    
```json
    {
        "Number": 123,
        "String": "Hello World",
        "Boolean": true,
        "Array": [1, 2, 3],
        "Object": {"a": "b", "c": "d"},
        "Null": null
    }
```

+   Firefox 浏览器中将对象或数组转换为 JSON 字符串的方法：`对象或数组.toSource()`
    
+   JSON 格式字符串转对象或数组的方法：`eval("(" + jsonString + ")")`，因为 JavaScript 规定，如果行首是花括号，一律解释为语句（即代码块），如果要解释为表达式（即对象），必须在花括号前加上圆括号
    

## Java 中操作 JSON 的库

### jackson

+   所需 jar 包：jackson-core.jar、jackson-annotations.jar、[jackson-databind.jar (opens new window)](https://github.com/FasterXML/jackson-databind)
+   JSON 与 Java 对象之间的转换：com.fasterxml.jackson.databind.ObjectMapper、com.fasterxml.jackson.databind.json.JsonMapper（jackson-databind.jar）
+   XML 与 Java 对象之间的转换：com.fasterxml.jackson.dataformat.xml.XmlMapper（jackson-dataformat-xml.jar）
+   YAML 与 Java 对象之间的转换：com.fasterxml.jackson.dataformat.yaml.YAMLMapper（jackson-dataformat-yaml.jar）

### jackson 中的处理 JSON 的三种方式

1.  数据绑定：JSON 和 POJO 相互转换，基于属性访问器规约或注解（最常用）
    
2.  树模型：提供一个 JSON 文档可变内存树的表示形式（最灵活）
    
    +   容器节点抽象类 ContainerNode（extends BaseJsonNode（extends JsonNode））
    +   子类：ObjectNode、ArrayNode
    
```java
    JsonNode jsonNode = mapper.readTree(jsonStr);
    ObjectNode objNode = mapper.createObjectNode(); // 创建对象节点  
    ArrayNode arrNode = mapper.createArrayNode(); // 创建数组节点
    ArrayNode arrNode = objNode.withArray("propertyName")
```

3.  流式 API：读取和写入 JSON 内容作为离散事件（性能最佳：开销最低、速度最快的读/写；其它二者基于它实现），相关类：JsonParser、JsonGenerator
    

### JSON 与 Java 对象之间的转换

+   ObjectMapper 是线程安全的
    
+   在默认情况下，ObjectMapper 依赖于 Java 对象的默认的**无参构造器**进行反序列化，并且**严格地**通过 **getter 和 setter 的命名规约**进行序列化和反序列化
    
    > jackson 反序列化时对象的属性类型不能为实例内部类，但可以为静态内部类（创建实例内部类对象前必须先创建外部类对象）
    >  在序列化时，要求 field 可以被访问到，先通过 getter，如果没有再去找 field，如果还没有，就跳过这个 field
    >  在反序列化时，通过反射，调用构造器，根据 json 里的 key-value 中的 key，去找对应变量的 setter，找不到就直接找对应变量，如果还找不到且没有设置 ignore unknown，就抛出异常
    
>  `MapperFeature#USE_STD_BEAN_NAMING(false)`: Specific difference is that Jackson always lower cases leading upper-case letters, so "getURL()" becomes "url" property; whereas standard Bean naming **only** lower-cases the first letter if it is NOT followed by another upper-case letter (so "getURL()" would result in "URL" property). `BeanUtil#legacyManglePropertyName` 与 `BeanUtil#stdManglePropertyName`

+   ObjectMapper 默认将 json 格式字符串中的**对象结构**转换为 **LinkedHashMap 对象**，**数组结构**转换为 **ArrayList 对象**
    
+   com.fasterxml.jackson.databind.ObjectMapper 类中的实例方法
    
    +   `String writeValueAsString(Object value)`：对象或集合转 json 格式字符串
        
    +   `<T> T readValue(String content, Class<T> valueType)`：json 格式字符串转简单类型对象
        
    +   `<T> T readValue(String content, TypeReference valueTypeRef)`：json 格式字符串转复杂类型对象（如有泛型类型字段的对象），如 `mapper.readValue(jsonStr, new TypeReference<Result<XXXX>>() {});`
        
    +   `<T> T readValue(String content, JavaType valueType)`：json 格式字符串转复杂类型对象（如集合类型）
        
    +   `<T> T convertValue(Object fromValue, Class<T> toValueType)`：将给定值转换为指定类型的对象
        
    +   通过 ObjectMapper 获取类型工厂 TypeFactory `mapper.getTypeFactory()`，再通过 TypeFactory 构造 JavaType（或其子类 ArrayType、CollectionType、MapType）
        
        +   `JavaType constructType(TypeReference<?> typeRef)`
        +   `JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses)`
        +   `JavaType constructParametricType(Class<?> rawType, JavaType... parameterTypes)`
        +   `ArrayType constructArrayType(Class<?> elementType)`
        +   `ArrayType constructArrayType(JavaType elementType)`
        +   `CollectionType constructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass)`
        +   `CollectionType constructCollectionType(Class<? extends Collection> collectionClass, JavaType elementType)`
        +   `MapType constructMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass)`
        +   `MapType constructMapType(Class<? extends Map> mapClass, JavaType keyType, JavaType valueType)`
        
```java
        /**
         Concrete Java types that Jackson will use for simple data binding are:
         JSON Type    Java Type
         object     LinkedHashMap<String,Object>
         array      ArrayList<Object>
         string     String
         number(no fraction) Integer, Long or BigInteger (smallest applicable)
         number(fraction)  Double(configurable to use BigDecimal)
         true|false   Boolean
         null      null
         */
        JavaType javaType = mapper.getTypeFactory().constructParametricType(Result.class, User.class);
        Result<User> result = mapper.readValue(jsonStr, javaType);
        // List<User> userList = mapper.readValue(jsonStr, new TypeReference<Result<User>>() {});
        
        ArrayType arrayType = mapper.getTypeFactory().constructArrayType(User.class);
        User[] users = mapper.readValue(jsonStr, arrayType);
        // User[] o = mapper.readValue(jsonStr, new TypeReference<User[]>() {});
        
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, User.class);
        List<User> userList = mapper.readValue(jsonStr, listType);
        // List<User> userList = mapper.readValue(jsonStr, new TypeReference<List<User>>() {});
```

+   `ObjectMapper setVisibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility)`：设置可见性
    
+   `ObjectMapper enableDefaultTyping(DefaultTyping dti)`：指定序列化时包含的属性类型信息（默认不开启，即不包含类型信息）
    
+   `ObjectMapper enableDefaultTyping(DefaultTyping applicability, JsonTypeInfo.As includeAs)`
    
+   `ObjectMapper configure(DeserializationFeature f, boolean state)`：打开/关闭某反序列化特性，如浮点数反序列化为 Double、忽略空属性等
    
```java
    // 通过反射机制（而非 getter 和 setter 方法）直接操作对象上的字段
    mapper.findAndRegisterModules()
        // 屏蔽所有 accessor
        .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        // 任何字段可见
        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true); // 格式化输出
    // mapper.configure(MapperFeature.USE_STD_BEAN_NAMING, true); // 默认 false
    // mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true); // 属性序列化顺序
    mapper.disable(DeserializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 禁用序列化日期为 timestamps
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // 禁用遇到未知属性抛出异常
    
    mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
    // {"@class":"org.apache.commons.lang3.tuple.MutableTriple","left":"1234","middle":["java.lang.Long",4562],"right":"triple"}
```

### jackson 的多态类型处理

方式一：设置全局的 DefaultTyping；方式二：使用 @JsonTypeInfo 注解

1.  全局 DefaultTyping，ObjectMapper.DefaultTyping 枚举类中的枚举值：
    
    +   JAVA\_LANG\_OBJECT：序列化时包含属性类型为 Object 的类型信息
    +   OBJECT\_AND\_NON\_CONCRETE：序列化时包含属性类型为 Object、非具体类型（抽象类和接口）的类型信息
    +   NON\_CONCRETE\_AND\_ARRAYS：序列化时包含属性类型为 Object、非具体类型（抽象类和接口）以及数组元素类型的类型信息
    +   **NON\_FINAL**：序列化时包含非 final 对象类型信息、以及属性中所有非 final 类型或者非 final 类型数组元素的类型信息
2.  使用注解处理多态
    
    +   @JsonTypeInfo：修饰类、字段、方法、参数，用于指出序列化包含的类型信息细节
        
        +   属性：
            +   use：（必选）指定序列化**类型信息**时使用的类型识别码，属性值为 JsonTypeInfo.Id 中的枚举值：
                +   CLASS：使用完全限定类名做识别，此时默认的识别码属性名称 "@class"
                +   MINIMAL\_CLASS：若基类和子类在同一包类，使用类名（忽略包名）作为识别码，此时默认的识别码属性名称 "@c"
                +   NAME：使用 @JsonSubTypes 或 @JsonTypeName 指定的逻辑类型名称，此时默认的识别码属性名称 "@type"
                +   CUSTOM：自定义识别码，需结合 property 属性和 @JsonTypeIdResolver
                +   NONE：不使用识别码，即序列化是不包含类型信息
            +   include：指定识别码是如何被包含进去的，属性值为 JsonTypeInfo.As 中的枚举值：
                +   **PROPERTY**：作为数据的属性（默认）
                +   EXISTING\_PROPERTY：作为 POJO 中已经存在的属性
                +   EXTERNAL\_PROPERTY：作为扩展属性
                +   WRAPPER\_OBJECT：作为一个包装的对象
                +   WRAPPER\_ARRAY：作为一个包装的数组
            +   property：指定识别码的属性名称
            +   defaultImpl：如果类型识别码不存在或者无效，指定反序列化时使用的默认类型
            +   visible：定义识别码在反序列化时是否保留，默认 false
    +   @JsonSubTypes：用来指示该类的子类型以及逻辑类型名称
        
    +   @JsonTypeName：用于定义类的逻辑类型名称
        
    
    > 只有当 @JsonTypeInfo 的 use 属性值为 JsonTypeInfo.Id.NAME 时，才会使用逻辑类型名称
    
```java
    @Data
    public class Zoo {
        private Animal animal;
    }
```

```java
    @Data
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Dog.class, name = "dog"),
            @JsonSubTypes.Type(value = Cat.class, name = "cat")
    })
    public abstract class Animal {
        protected String name;
        protected String type;
    }
    
    @Data
    public class Dog extends Animal {
        private Double barkVolume;
    }
    
    @Data
    public class Cat extends Animal {
        private Boolean likesCream;
        private Integer lives;
    }
```

```json
    {
        "animals": {
            "name": "lacy",
            "type": "cat",
            "likesCream": true,
            "lives": 5
        }
    }
```

```java
    // @JsonSubTypes 可以用其它方式代替
    @Data
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
    public abstract class Animal {
        protected String name;
        protected String type;
    }
    
    @Data
    @JsonTypeName("dog")
    public class Dog extends Animal {
        private Double barkVolume;
    }
    
    @Data
    @JsonTypeName("cat")
    public class Cat extends Animal {
        private Boolean likesCream;
        private Integer lives;
    }
    
    mapper.registerSubtypes(Animal.class, Dog.class, Cat.class); // 注册子类或逻辑类型名称
```

### Jackson 的常用注解

+   @JsonInclude(JsonInclude.Include.NON\_NULL)，修饰类，序列化时忽略 null 属性，可配置 `spring.jackson.default-property-inclusion=non_null`
+   @JsonIgnoreProperties(value = {"internalId", "secretKey"}, ignoreUnknown = true)，修饰类，指定时需要忽略的字段，且在反序列化时忽略 json 中存在的未知字段
+   @JsonIgnore，序列化或反序列化时忽略该属性
+   @JsonCreator，修饰构造器，指定反序列化时调用的构造器，**默认调用无参构造器**
+   @JsonProperty("uname")，属性值：value（指定反序列化和序列化时该属性对应的名称）、required（该属性是否必须存在 json 中）
+   @JsonAlias：指定反序列化时该属性对应的一个或多个别名
+   @JsonValue，修饰字段、方法，一个类中最多只能存在一个该注解，表示以该字段值或方法的返回值作为序列化结果
+   @JsonEnumDefaultValue，设置默认枚举值（需开启 read\_unknown\_enum\_values\_using\_default\_value=true）
+   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")，属性：pattern、timezone、shap（指定序列化后类型），该注解**支持** Java 8 中新的日期和时间 API
+   @JsonSerialize，修饰字段或者 getter 方法上，用于在序列化时附加自定义的代码，与 @JsonFormat 类似，但是功能更丰富，支持自定义
+   @JsonDeserialize，修饰字段或者 setter 方法上，用于在反序列化时附加自定义的代码
+   @JsonNaming，修饰类，用于指定命名的策略，内置的命名策略（PropertyNamingStrategy）：SNAKE\_CASE、UPPER\_CAMEL\_CASE、LOWER\_CAMEL\_CASE（默认）、LOWER\_CASE、KEBAB\_CASE

> 在 **int 类型枚举字段**上标记 @JsonValue **只能用于序列化**，**对反序列化无效**，反序列化时使用的还是枚举的 ordinal() 索引值（[相关 Issue (opens new window)](https://github.com/FasterXML/jackson-databind/issues/1850)）

```java
// 时间戳序列化、反序列化为 LocalDateTime
public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        if (value != null) {
            long timestamp = value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            gen.writeNumber(timestamp);
        }
    }
}

public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
        long timestamp = p.getValueAsLong();
        if (timestamp > 0) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        } else {
            return null;
        }
    }
}

@JsonSerialize(using = LocalDateTimeSerializer.class)
@JsonDeserialize(using = LocalDateTimeDeserializer.class)
private LocalDateTime timestamp;
```

```java
// IdWorker 生成主键太大导致 js 精度丢失
@ApiModelProperty(value = "数据 ID", dataType = "java.lang.String")
@JsonSerialize(using = ToStringSerializer.class)
private Long id;
```

### fastjson

+   所需 jar 包：fastjson.jar
    
+   注意：转对象时，该对象的类需提供 setter 方法，转字符串时，根据对象的类提供的 getter 方法
    
+   fastjson 默认将 json 格式字符串中的**对象结构**转换为 **JSONObject 对象**（implements Map<String, Object>），**数组结构**转换为 **JSONArray对象**（`implements List<Object>`）
    
+   JSON 类中的类方法
    
    +   `String toJSONString(Object object)`：对象或集合序列化为 json 格式字符串
    +   `<T> T parseObject(String text, Class<T> clazz)`：json 格式字符串反序列化为简单类型对象
    +   `<T> T parseObject(String text, TypeReference<T> type, Feature... features)`：json 格式字符串反序列化为复杂类型对象，如 `List<Model> models = JSON.parseObject(jsonStr, new TypeReference<List<Model>>() {});`
    +   `JSONObject parseObject(String text)`：json 格式字符串转为 JSONObject（JSONObject 是 json 字符串与 POJO 对象转换过程中的中间表达类型，实现了 Map 接口，`Xxx getXxx(String key)`、`xxx getXxxValue(String key)`）
    +   `<T> List<T> parseArray(String text, Class<T> clazz)`：json 格式字符串反序列化为 List 集合类型对象
+   `JSONArray parseArray(String text)`：json 格式字符串转为 JSONArray（JSONObject 是 JSON 字符串与 List 集合类型对象转换过程中的中间表达类型，实现了 List 接口，`Xxx getXxx(int index)`、`xxx getXxxValue(int index)`）
    
+   fastjson 的常用注解
    
    +   @JSONField，修饰字段、getter 或 setter 方法，属性：ordinal、name（转 json 格式时的属性名）、alternateNames（反序列化时字段的替代名称）、format、serialize（转 json 格式时是否忽略）、deserialize、serialzeFeatures（指定序列化特性）、parseFeatures（指定反序列化特性）
    +   @JSONType，修饰类，属性：includes、ignores、naming
+   SerializerFeature 序列化特性
    
    +   QuoteFieldNames，默认开启
    +   SkipTransientField，默认开启
    +   WriteEnumUsingName，默认开启
    +   SortField，默认开启
    +   **WriteMapNullValue**，输出时保留 value 为 null 的键值对
    +   **WriteBigDecimalAsPlain**
    +   MapSortField
    +   **DisableCircularReferenceDetect**，输出时禁用循环/重复引用检查，不开启时会输出 `$ref` `$[0]` `@` `$` 等引用标识，开启时如果存在循环引用会导致 StackOverflowError
    +   WriteClassName
    +   NotWriteDefaultValue
+   Feature 反序列化特性
    
    +   AutoCloseSource，默认开启
    +   InternFieldNames，默认开启
    +   UseBigDecimal，默认开启
    +   AllowUnQuotedFieldNames，默认开启
    +   AllowSingleQuotes，默认开启
    +   AllowArbitraryCommas，默认开启
    +   SortFeidFastMatch，默认开启
    +   IgnoreNotMatch，默认开启
    +   **OrderedField**
    +   TrimStringFieldValue
    +   UseNativeJavaObject，use HashMap instead of JSONObject, ArrayList instead of JSONArray
    +   DisableCircularReferenceDetect
+   JSONPath
    
    +   [fastjson JSONPath (opens new window)](https://github.com/alibaba/fastjson/wiki/JSONPath)
    +   [Jayway JsonPath (opens new window)](https://github.com/json-path/JsonPath)
    +   [JSONPath Online Evaluator (opens new window)](http://jsonpath.com/)

### Gson

## JSON 的最佳实践

+   在实体类中提供 `Map<String, Object> toJson()` 方法，Map 中放置需要转换成 json 格式的属性及属性值，在 Controller 中的请求处理方法返回该 Map 对象

