# struts.xml 中的常见标签 #

*  `<constant>`，配置常量，常见：struts.devMode、struts.ui.theme
*  `<include>`，导入其它配置文件，属性：file
*  `<package>`，定义一个包配置，属性：name、extends（一般继承 "struts-default"）、namespace（不指定时为默认命名空间）、abstract（缺省值："false"）
     -` <interceptors>`
       - `<interceptor>`，定义拦截器，子标签 `<param>`，在定义拦截器时指定拦截器参数的默认值
       - `<interceptor-stack>`，定义拦截器栈
         - `<interceptor-ref>`，引用拦截器，子标签` <param>`，在使用拦截器时重新指定拦截器参数（覆盖）
     - `<default-interceptor-ref >`
     - `<global-result>`，子标签 `<result>`
     - `<action>`，属性：name、class（缺省值："ActionSupport"）、method（缺省值："execute"）
       - `<interceptor-ref>`
       - `<result>`，配置逻辑视图和物理视图之间的映射关系，属性：name（缺省值："success"）、type（缺省值：dispatcher，常见值：dispatcher、redirect、chain、redirectAction、stream）

# Action 类 #

 *  Action 接口中的全局静态常量：SUCCESS、NONE、ERROR、INPUT、LOGIN
 *  Action 类的编写方式：继承于 ActionSupport 类
 *  Action 类中的实例变量/属性，不仅可用于封装 HTTP 请求参数，还可用于封装 Action 的处理结果
 *  Action 的属性可以通过在 Struts2 配置文件中为该属性动态指定值
 *  每次请求都会创建一个 Action 实例

## Action 中多方法调用 ##

 *  使用通配符name="*\_*" class="xxx.xxx.\{1\}Action" method="\{2\}"浏览器访问：http://ip:端口/contextPath/namespace/actionName

## Action 获取请求参数 ##

 *  对于简单类型（基本类型值或 String 类型值），将 Action 类本身作为 Model 对象，在 Action 中提供与请求参数对应的、可设值的属性（setter 方法）
 *  对于复合类型，创建独立 model 对象，在 Action 类中提供获取该对象的方法（getter 方法），并且把对象 new 出来，HTTP 请求参数的参数名需使用 OGNL 表达式：name="对象名.属性名"

## Action 访问 ServletAPI ##

``````````java
// 1. 通过 ServletActionContext 工具类中的静态方法 
  ServletContext context = ServletActionContext.getServletContext(); 
  String realPath = context.getRealPath("/upload"); 
  // 2. 通过 ActionContext 类，获取的是 request、session、application 三大作用域对应的 Map 对象 
  ActionContext ctx = ActionContext.getContext();
``````````

## Action 给页面（JSP）传递数据的方式 ##

 *  `ValueStack vs = ActionContext.getContext().getValueStack();` // 获取 ValueStack 对象
 *  Action 往 ActionContext 中存储数据
    
     *  把数据放入值栈中：在 Action 中提供可访问的属性（getter方法）
     *  把对象放入 ActionContext 中：`ActionContext.getContext.put(String key, Object value);`
 *  JSP 从 ActionContext 中取出数据
    
     *  获取值栈中数据
        
         *  若放入值栈中的数据没有属性名：`<s:property value="[0].top"/>`
         *  若放入值栈中的数据有属性名：`<s:property value="属性名"/>`
     *  通过 key 获取其它非根对象中数据：`<s:property value="#key"/>`

# OGNL 表达式语言 #

 *  OGNL（Object-Graph Navigation Language，对象图导航语言） 上下文实际上是一个 Map 对象，里面可以存放很多个 JavaBean 对象
 *  Struts2 将 ActionContext（Map 结构）设置为 OGNL 上下文，并将值栈（ValueStack）作为 OGNL 的根对象，还包括 parameters、request、session、application、attr 等普通命名对象
 *  Struts2 总是把当前 Action 实例放置在值栈的栈顶
 *  访问根对象（值栈）中的属性时，**不用加 \# 前缀，并且可以省略对象名**，即 `<s:property value="属性名"/>`
 *  访问其它非根对象时，需要在对象名之前添加 \# 前缀，即 `<s:property value="#对象名.属性名"/>`

## OGNL 中的集合操作 ##

 *  直接创建 List 类型集合的语法为：\{e1, e2, e3, …\}
 *  直接生成 Map 类型集合的语法为：\#\{key1:value1, key2:value2, ..\}
 *  对于集合：OGNL 提供了两个运算符：in 和 not in，用于判断某个元素是否在指定集合中；还允许通过某个规则取得集合的子集：?、^、$

## 访问静态成员 ##

 *  OGNL 表达式还提供了一种访问静态成员（ 包括调用静态方法、访问静态成员变量） 的方式
 *  语法为：@className@staticField、@className@staticMethod(val…)

# Struts2 的标签 #

 *  引用标签库：<%@ taglib uri="/struts-tags" prefix="s"%>
 *  分类：非 UI 标签（流程控制标签、数据访问标签）、UI 标签（表单标签、非表单标签）、Ajax 标签

## 流程控制标签 ##

 *  控制选择输出：if、elseif、else
 *  将集合迭代输出：iterator，属性如果迭代的元素是对象，可以不设置 var 属性，底层会把迭代的每一个对象存放在 root 区域的栈顶

## 数据访问标签 ##

 *  Struts2 的很多标签都可以指定 var（以前是 id）属性，一旦指定了 var 属性，则会将新生成、新设置的值放入 Stack Context 中（必须通过 \#name 形式访问）
 *  如果不指定 var 属性，则新生成、新设置的值不会放入 Stack Context 中，而是放入 ValueStack 中，因此只能在该标签内部访问新生成、新设置的值（可以直接访问）
 *  常用数据标签
    
     *  debg：用于在页面上生成一个调试链接
     *  property：用于**输出** ActionContext 中的值，常用属性：value、default
     *  param：用于设置一个参数，通常是用做其它标签的子标签，常用属性：name、value
     *  date：用于格式化**输出**一个日期，常用属性：name、format
     *  url：用于生成一个 URL 地址

## 表单标签 ##

 *  对于表单标签而言，name 和 vaiue 属性之间存在一个特殊的关系： 因为每个表单元素（请求参数）会被映射成 Action 属性，所以如果某个表单对应的 Action 已经被实例化（该表单被提交过）、且其属性有值时，则该 Action 对应表单里的表单元素会显示出该属性的值，这个值将作为表单标签的 value 值（表单数据回显）

``````````
<!-- 将下面文本框的值绑定到 Action 的 person 属性的 firstName 属性 --> 
  <s:textfield name="person.firstName"/>
``````````

 *  checkboxlist 标签，根据 list 属性指定的集合来生成多个复选框常用属性：listKey（指定集合元素中的某个属性作为复选框的 value）、listValue（指定集合元素中的某个属性作为复选框的标签）
 *  radio 标签，根据 list 属性指定的集合来生成多个单选钮label、 list、 listKey 和 listValue 等属性
 *  select 标签，根据 list 属性指定的集合来生成下拉列表框的选项listKey、HstValue、multiple

# Struts2 的拦截器 #

 *  Struts2 的内置拦截器既可以在 Action 的 execute 方法之前插入执行代码，也可以在 execute 方法之后插入执行代码，这种方式的实质是 AOP（面向切面编程） 的思想
 *  作用：解析请求参数，将请求参数赋值给 Action 属性，执行数据校验，文件上传等等
 *  拦截器的拦截行为会在 Action 的 execute 方法执行之前被执行
 *  常见的拦截器：servletConfig、prepare、modelDriven、fileUpload、params、conversionError、validation、workflow
 *  struts-default 包及其子包使用的默认拦截器栈是 defaultStack，`<default-interceptor-ref name="defaultStack" />`

## 自定义拦截器 ##

 *  步骤
    
     *  继承 AbstractInterceptor 类，重写 intercept(ActionInvocation invocation) 方法`ActionContext ctx = invocation.getInvocationContext();` // 取得请求相关的 ActionContext 实例`String result = invocation.invoke();` // 执行该拦截器的后一个拦截器或者执行指定 Action 中的被拦截方法
     *  在 struts.xml 中注册拦截器：通过 来定义拦截器，再通过 < interceptor-ref> 来引用拦截器
 *  注意：一旦在  中显式引用了某个拦截器，则该 Action 所在包的默认拦截器不再起作用对于那些不需要使用权限控制的 Action，可以将它们定义在另外的包中

## 拦截指定方法方法的拦截器 ##

 *  继承 MethodFilterlnterceptor 重写 doIntercept(ActionInvocation invocation) 方法
 *  通过  来定义拦截器后，在  中引用了自定义的拦截器时，配置参数：excludeMethods、includeMethods

## 类型转换 ##

 *  Struts2 内建的类型转换器，可以自动将 HTTP 请求参数（字符串类型）转换成以下类型：boolean 和 Boolean、char 和 Character、int 和 Integer、long 和 Long、 float 和 Float、double 和 Double、Date（SHORT 格式）、String\[\]、ArrayList\[\]
 *  基于 OGNL 的类型转换：把 HTTP 请求参数（表单元素和其它 GET/POST 的参数）命名为合法的 OGNL 表达式
 *  ActionSupport 负责收集类型转换错误、输入校验错误，并将它们封装成 FieldError 对象，添加到 ActionContext 中
 *  当类型转换失败或输入校验失败时，系统将转入 input 逻辑视图所指定的视图资源

## 手动完成输入校验 ##

 *  继承 ActionSupport 类，重写 Validateable 接口中的 validate() 方法，在 Validate() 方法中，当校验错误时通过 `super.addFieldError("fieldName", "erroeMsg");` 添加错误信息到 FieldError 集合中
 *  方法不需要被校验：@SkipValidation
 *  只校验 xxx() 方法：只需定义 validateXxx() 方法
 *  指定校验失败跳转的结果视图：@InputConfig(resultName="xxx")

## 数据预处理 ##

 *  **修改默认的拦截器栈**为 "paramsPrepareParamsStack"，该拦截器栈包含 2个 params 拦截器，拦截顺序：params、prepare、params
 *  实现 PreParable 接口，重写 prepare() 方法
 *  只对 xxx() 方法预处理：只需定义 prepareXxx() 方法
 *  可以用来解决缺失某些请求参数导致封装成的对象数据不完整

## 文件上传 ##

 *  在 Action 类中定义 xxx、xxxFileName、xxxContentType 成员变量，并提供 setter 方法

``````````java
// 表单中上传控件的 name 属性值为 "xxx" 
  private File xxx; // 封装上传文件内容 
  private String xxxContentType; // 封装上传文件类型 
  private String xxxFileName; // 封装上传文件名 
  // 三个成员变量的 setter 方法 
  
  // 使用 UUID 工具类来生成唯一的文件基名 
  // 使用 FilenameUtils 获取文件的拓展名 
  // 通过 FileUtils 工具类的 copyFile() 方法保存文件到服务器的指定位置
``````````

 *  拦截器实现文件过滤配置 fileUpload 拦截器时，可以为其指定两个参数：allowedTypes、maximumSize

# 文件下载 #

 *  文件下载的 Action 需要提供一个返回 InputStream 流的业务方法，该输入流代表了被下载文件的入口
 *  配置该 Action 时， 中需指定4 个参数：contentType、inputName、contentDisposition、bufferSize
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414385561.png) 


[Struts2]: https://static.sitestack.cn/projects/sdky-java-note/27141ff76ba9ff3bcdd80d46ee8c72cb.png