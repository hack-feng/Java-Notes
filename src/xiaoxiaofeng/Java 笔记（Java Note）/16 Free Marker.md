 *  FreeMarker 是一种模板引擎，通过合并模板和 Java 对象（数据模型）来生成输出文本（数据模型+模板=输出）
 *  与 JSP 相比，不依赖于 Servlet、网络或 Web 环境，没有更高级的编译发生，数字和日期格式默认本地化敏感
 *  依赖的 jar 包：freemarker.jar

# 模板开发 #

## 数据模型（本身是哈希表 root） ##

 *  scalars：标量，存储单一的值。这种类型的值可以是字符串（单引号或双引号内）、数字、日期/时间、布尔值（true 和 false）
 *  hash：哈希表，存储变量和与其相关且有唯一标识名称变量的容器，如 Map 或自定义对象。访问子变量时使用**点式语法**或**方括号语法**：`someBean.propName`，`someBean["propName"]`；调用对象方法：`someBean.methodName()`
 *  sequence：序列，存储有序变量的容器，如数组、列表、存储数字范围的序列（`start..end`）等可遍历对象。访问子变量时只能使用**方括号语法**，且方括号内的表达式必须是一个数字

## 模板内容 ##

 *  插值：$\{expression\}，输出真实的值来替换花括号内的表达式，只能在文本区段或者字符串文字中使用，插值表达式的结果只能是字符串，数字或日期类型，其他类型的值（如布尔，序列）只能手动转换为字符串类型
 *  FTL 标签：标签以\#开头，<\#指令名 …>
 *  注释：<\#— 注释内容 —>
 *  其它文本内容会被按照原样输出，FTL 忽略表达式中的多余空格

## 指令 ##

 * if, else, elseif 指令：有条件地跳过模板的一部分
   `<#if condition>...</#if>`
   `<#if condition>...<#else>...</#if>`
   `<#if condition>...<#elseif condition2>...<#else>...</#if>`

   > 注意：
   >
   > 1. 测试两个值相等使用 = 或者 ==
   > 2. = 或 != 两边的表达式的结果都必须是标量，而且两个标量都必须是相同类型
   > 3. 对数字和日期类型的比较，也可以使用 <，<=，>= 和 >
   > 4. 在使用 >= 和 > 时，需用 lt 代替<，lte 代替<=，gt 代替>，gte 代替 >=，或者**将表达式放到括号内**

*  switch, case, default, break 指令：选择模板中的一个片段

``````````java
<#switch value> 
      <#case refValue1> 
          ... 
          <#break> 
      <#case refValue2> 
          ... 
          <#break> 
      <#default> 
          ... 
  </#switch>
``````````

 *  list, break 指令：遍历集合的内容`<#list sequence as item>${item.prop}</#list>`在 list 循环中的循环变量：`item_index` 表示当前项在循环中的步进索引的数值，`item_has_next` 表示当前项是否是序列的最后一项的布尔值
 *  include 指令：在当前的模板中插入插入另外一个 FreeMarker 模板文件`<#include path [options]>`path：要包含文件的路径字符串
 *  import 指令：创建一个命名空间，名字由 hash 给定，并用 path 引入的模板中的变量（宏，函数等）填充命名空间`<#import path as namespacehash>`
 *  assign 指令：在模板中定义简单变量`<#assign name1=value1 name2=value2 … nameN=valueN>`
 *  global 指令：创建在所有的命名空间中都可见的变量
 *  local 指令：在宏和方法的内部定义局部变量
 *  macro, nested, return 指令
    
     *  nested 指令：执行**自定义指令**开始和结束标签**中间的模板片段**，即嵌套内容

``````````
<#-- 定义宏（注意：没有默认值的参数必须在有默认值参数之前） --> 
  <#macro macroName param1 param2 ... paramN> 
      ${param1} 
      ... 
      <#nested loopvar1, loopvar2, ..., loopvarN> 
      ... 
      <#return> 
      ... 
  </#macro> 
  
  <#-- 调用宏（注意：循环变量的名称在分号之后给定） --> 
  <@macroName param1=value1 param2=value2 ...; loopparam1, loopparam2, ..., loopparamN> 
      ${loopparam1} 
  </@macroName>
``````````

 *  自定义指令 <@…>，可嵌套内容
 *  function, return 指令
 *  noparse 指令：不在这个指令体中间寻找 FTL 标签，插值和其他特殊的字符序列
 *  compress 指令：移除缩进、空行和重复的空格/制表符

## 处理不存在的变量 ##

 *  当访问一个不存在的变量或属性值为 null 时，FreeMarker 会报错而导致模板执行中断
 *  在变量名后面跟着一个 `!` 和默认值（如果省略默认值，那么结果会是空串，空序列或空哈希表），如：`unsafe_expr!default_expr` 或 `unsafe_expr!` 或 `(unsafe_expr)!default_expr` 或 `(unsafe_expr)!`
 *  在变量名后面加 `??` 来检测变量是否存在，如：`unsafe_expr??` 或 `(unsafe_expr)??`
 *  说明：用**括号**时，表示允许其中表达式的任意部分可以未定义
 *  最高优先级运算符： `exp!exp` `expr!` `expr??`

## 常用的内建函数 ##

 *  内建函数以 `?` 形式提供**变量的不同形式或者其他信息**

### 处理字符串的内建函数 ###

 *  `substring(from, toExclusive)`：取子串
 *  `trim`：去掉字符串首尾的空格
 *  `lower_case`：字符串的小写形式
 *  `upper_case`：字符串的大写形式
 *  `cap_first`：字符串的第一个字母变为大写形式
 *  `uncap_first`：字符串的第一个字母变为小写形式
 *  `capitalize`：字符串的所有单词的首字母都大写
 *  `html`：字符串中所有的特殊 HTML 字符都用实体引用来代替（比如 < 代替 `<`）
 *  字符串转换成日期值
    
     *  `"10/25/1995"?date("MM/dd/yyyy")`
     *  `"15:05:30"?time("HH:mm:ss")`
     *  `"1995-10-25 03:05 PM"?datetime("yyyy-MM-dd hh:mm a")`

### 处理数字的内建函数 ###

 *  `c`：数字转字符（最多在小数点后打印 16 位）
 *  `string("#.00")`：使用 Java 中的数字格式语法将数字转字符串，（0 表示阿拉伯数字，如果不存在则显示为 0；\# 表示阿拉伯数字，如果不存在则不显示），如 `12.1?string("000.00")` 结果是 012.10，`${12345678?string(",##0.00")}` 结果是 12,345,678.00
 *  `int`：数字的整数部分（比如 `-1.9?int` 结果是 -1）
 *  `round`,`floor`,`ceiling`：使用指定的舍入法则，转换一个数字到整数

### 处理日期的内建函数 ###

 *  `string("yyyy-MM-dd HH:mm:ss")`：以指定的格式将日期类型转字符串类型

### 处理布尔值的内建函数 ###

 *  `string`：使用代表 true 和 false 值的默认字符串 "true" 和 "false" 来转换布尔值为字符串
 *  `string("yes", "no")`：如果布尔值是 true，这会返回第一个参数，否则就返回第二个参数（可用作三元运算符）

### 处理序列的内建函数 ###

 *  `first`：序列的第一个子变量
 *  `last`：序列的最后一个子变量
 *  `reverse`：反转序列
 *  `size`：序列中元素的个数
 *  `sort`：以升序方式返回
 *  `sort_by`：返回由给定的哈希表子变量来升序排序的序列

### 处理哈希表的内建函数 ###

 *  `keys`：返回包含哈希表中查找到的键的序列
 *  `values`：返回包含哈希表中查找到的值的序列

# 程序代码 #

``````````java
// 创建配置对象
// 一个应用程序通常只使用一个共享的 Configuration 实例
Configuration cfg = new Configuration();
Configuration cfg = config.getConfiguration();

// 指定模板文件从何处加载的数据源，这里设置成一个文件目录
// cfg.setDirectoryForTemplateLoading(new File("/where/you/store/templates"));
// 获取模板
Template temp = cfg.getTemplate("test.ftl");

// 创建数据模型
Map root = new HashMap(); // 创建根哈希表
root.put("user", "Big Joe"); // 在根中放入字符串"user"

// 将模板和数据模型合并
Writer out = new OutputStreamWriter(System.out);
temp.process(root, out);
out.flush();
``````````

## 在 Java 代码中实现自定义指令 ##

 *  定义 Bean 实现 TemplateDirectiveModel 接口中的 execute 方法

``````````java
@Component
 public class UserListDirective implements TemplateDirectiveModel {

     @Autowired
     private UserMapper userMapper;

     @Override
     public void execute(Environment env, Map params, TemplateModel[] loopVars,
             TemplateDirectiveBody body) throws TemplateException, IOException {
         String name = params.get("name").toString();
         List<User> userlist = userMapper.listByProperty("name", name);
         // 获取包装器
         BeansWrapperBuilder builder = new BeansWrapperBuilder(Configuration.VERSION_2_3_25);
         BeansWrapper beansWrapper = builder.build();
         // 对象包装后，再添加到容器中
         env.setVariable("userList", beansWrapper.wrap(userlist));
         body.render(env.getOut());
     }
 }
``````````

 *  将指令作为共享变量放到 Configuration 中`configuration.setSharedVariable("userListTag", userListDirective);`
 *  模板文件中的使用自定义指令

``````````java
<@userListTag name="zhangsan">
     <#if userList?? && userList?size gt 0>
         <#list userList as user>
             ${user.name}
         </#list>
     </#if>
 </@userListTag>
``````````

# 在 Spring MVC 中集成 FreeMarker #

``````````xml
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
    <version>2.3.23</version>
</dependency>
``````````

``````````xml
<!-- 配置 freeMarker 的模板路径 -->
<bean class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
    <!-- 配置 freemarker 的文件编码 -->
    <property name="defaultEncoding" value="UTF-8"/>
    <!-- 配置 freemarker 寻找模板的路径 -->
    <property name="templateLoaderPath" value="/WEB-INF/views/"/>
    <property name="freemarkerSettings">
        <value>
            number_format = 0.##
            datetime_format = yyyy-MM-dd HH:mm:ss
        </value>
    </property>
</bean>

<!--freemarker 视图解析器 -->
<bean id="viewResolver"
      class="org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver">
    <!-- 是否在 model 中自动把 session 中的 attribute 导入进去 -->
    <property name="exposeSessionAttributes" value="true"/>
    <!-- 配置逻辑视图自动添加的后缀名 -->
    <property name="suffix" value=".ftl"/>
    <!-- 配置视图的输出 HTML 的 contentType -->
    <property name="contentType" value="text/html;charset=UTF-8"/>
</bean>
``````````