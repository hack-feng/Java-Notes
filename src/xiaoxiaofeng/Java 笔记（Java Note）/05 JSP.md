# 基本原理 #

 *  在标准的 HTML 页面中嵌入 Java 代码，或使用各种 JSP 标签， 从而可以动态地提供页面内容
 *  JSP（Java Server Page）的本质是 Servlet，JSP 页面由 Servlet 容器生成对应 Servlet 的 Java 文件和 class 文件
 *  JSP 页面里的所有静态内容和 JSP 脚本都会转换成 Servlet 里 \_JspService() 方法 的可执行性代码
 *  Servlet 和 JSP 的区别：
    
     *  Servlet 中没有内置对象，原来 JSP 中的内置对象都必须由程序显式创建
     *  对于静态的 HTML 标签，Servlet 都必须使用页面输出流逐行输出

# JSP 的 4 种基本语法 #

 *  JSP 注释，不会出现在翻译成的 xxx\_jsp.java 文件<%— 注释内容 —%>
 *  JSP 声明，成员变量和成员方法<%! 声明部分 %>
 *  JSP 输出表达式，输出表达式语法后不能有分号实际上就是调用输出流打印到页面上，`out.print(表达式);`<%=表达式%>
 *  JSP 脚本，声明的变量是局部变量，里面不能定义方法<% 可执行性语句 %>

# JSP 的 3 个编译指令 #

 *  使用编译指令的语法格式：<%@ 指令名 属性名="属性值"…%>
 *  page：定义当前 JSP 页面的各种属性

``````````jsp
<%@page 
   language="Java" 
   contentType="mimeType;charset=characterSet" | "text/html;charSet=ISO-8859-1" // 设定生成网页的文件格式和编码字符集，即 MIME 类型和页面字符集类型（指定响应数据的类型） 
   pageEncoding="ISO-8859-1" 
   extends="package.class" 
   import="package.class | package.*,..." 
   session="true | false’’ // 设定这个 JSP 页面是否需要 HTTP Session，默认是 true 
   buffer="none|8KB| size Kb" // 指定输出缓冲区的大小 
   autoFlush="true| false" // 当输出缓冲区即将溢出时，是否需要强制输出缓冲区的内容 
   isThreadSafe="true | false" 
   info="text" // 设置该 JSP 程序的信息 
   errorPage="relativeURL" // 指定错误处理页面 
   isErrorPage="true| false" // 设置本 JSP 页面是否为错误处理程序 
   %>
``````````

 *  如果 **JSP 脚本、输出表达式**在运行中抛出未处理的异常，系统将自动跳转到 errorPage 属性指定的页面；如果 errorPage 没有指定错误页面，系统则直接把异常信息呈现给客户端浏览器
 *  include：用于指定包含另一个页面（**静态包含**）
    
     *  `<%@include file="relativeURLSpec"%>`
     *  在翻译阶段将一个外部文件嵌入到当前 JSP 文件中，同时解析整个页面中的 JSP 语句（如果有的话），包括被包含页面的编译指令
 *  taglib：用于引入标签
    
     *  属性：uri、prefix

# JSP 的 7 个动作指令 #

 *  jsp:forward：用于将页面响应转发到另外的页面，请求的参数和请求属性都不会丢失

``````````jsp
<jsp:forward page="{relativeURL|<%=expression%>}"/> 
   <%-- 可在转发时增加额外的请求参数（将参数值传入被转向的页面） %--> 
   <jsp:forward page="{relativeURL|<%=expression%>}"> 
       {<jsp:param name="参数名" value="参数值"/>} 
   </jsp:forward>
``````````

 *  jsp:param：用于传递参数，必须与其他支持参数的标签一起使用
 *  jsp:include：用于**动态引入**一个 JSP 页面（在运行时期引入）不会导入被 include 页面的编译指令，仅仅将被导入页面的 **body 内容**插入本页面

``````````jsp
<jsp:include page="{relativeURL|<%=expression%>}"/>
 <%-- 可在被导入页面中加入额外的请求参数（将参数值传入被导入的页面） --%>
 <jsp:include page="{relativeURL|<%=expression%>}">
     {<jsp:param name="参数名" value="参数值"/>}
 </jsp:include>
``````````

 *  jsp:plugin：用于下载 JavaBean 或 Applet 到客户端执行
 *  jsp:useBean：创建一个 JavaBean 的实例
 *  jsp:setProperty：设置 JavaBean 实例的属性值
 *  jsp:getProperty：输出 JavaBean 实例的属性值
 *  forward 拿目标页面代替原有页面，而 include（动态包含）则拿目标页面插入原有页面

# JSP 脚本中的 9 个内置对象 #

 *  只能在 JSP 脚本、JSP 输出表达式中使用这些内置对象

    | 内置对象        | 类型                  | 说明                                      |
    | ----------- | ------------------- | --------------------------------------- |
    | pageContext | PageContext         | 代表该 JSP 页面上下文                           |
    | request     | HttpServletRequest  | 该对象封装了客户端的一次请求，代表本次请求                   |
    | session     | HttpSession         | 代表一次会话                                  |
    | application | ServletContext      | 代表 JSP 所属的 Web 应用本身                     |
    | config      | ServletConfig       | 代表该 JSP 的配置信息                           |
    | response    | HttpServletResponse | 代表服务器对客户端的响应                            |
    | out         | JspWriter           | 代表 JSP 页面的字符输出流                         |
    | page        | Object              | 代表该页面本身                                 |
    | exception   | Throwable           | 代表其它页面中的异常和错误，isErrorPage="true" 时才可以使用 |

 *  JSP 的四大作用域对象：pageContext、request、session、application

### pageContext 对象，PageContext 抽象类 ###

 *  共享数据`setAttribute(String attrName, Object value)`、`getAttribute(String attrName)``public Object findAttribute(String name)`：按顺序在页面、请求、会话（如果有效）和应用程序范围中搜索指定属性，并返回指定属性名的值或 null`Object getAttribute(String name, int scope)`：取得指定范围内的 name 属性`public void setAttribute(String name, Object value, int scope)`：将指定变量放入指定范围内
    
     *  其中 scope 可以是如下 4 个值：PageContext.PAGE SCOPE、PageContext.REQUEST SCOPE、PageContext.SESSION SCOPE、PageContext.APPLICATION\_SCOPE
 *  可用于获取其他内置对象`ServletRequest getRequest()`：获取 request 对象`ServletResponse getResponse()`：获取 response 对象`ServletConfig getServletConfig()`：获取 config 对象`ServletContext getServletContext()` : 获取 application 对象`HttpSession getSession()`：获取 session 对象

# EL（表达式语言） #

 *  Expression Language，使用 EL 可以方便地访问 JSP 的隐含对象和 JavaBeans 组件
 *  语法格式：$\{expression\}
 *  EL的特点：从 page、request、session、application 作用域中按顺序搜索，如果不存在这个属性，就输出空字符串
 *  如果需要在支持 EL 的页面中正常输出”$“符号，则 在符号前加转义字符 ”\\“

## 从作用域中获取指定属性名的共享数据 ##

 *  获取 JavaBean 对象中的数据（普通类型、数组、集合、Map）方式一：`${对象.属性名}`，如 `${p.username}`、`${user.map.address}`方式二：`${对象.getter 方法}`，如 `${p.getUsername()}`（Tomcat 7 以上才支持）方式三：`${对象["属性名"]}`，如 `${p["username"]}`
 *  注意：`${对象.属性名}`，要求属性必须提供 getter 方法若操作的是 Map：`${Map 对象.key}`，可获取 Map 中该 key 对应的 value当要存取的属性名称中包含一些特殊字符，如 . 或 – 等并非字母或数字的符号，必须使用方括号语法，例如：`${header["User-Agent"]}`

## 获取当前 Web 应用的上下文路径 ##

 *  `${pageContext.request.contextPath}`
 *  `${pageContext.getRequest().getContextPath()}`

## EL 支持的运算符 ##

 *  算术运算符：+ - \* / 或 div % 或 mod （表达式语言把所有数值都当成浮点数处理）支持自动转变类型：`${param.count + 20}`
 *  比较运算符：== 或 eq、!= 或 ne、< 或 lt、> 或 gt、<= 或 le、>= 或 geEL 不仅可在数字与数字之间比较，还可在字符与字符之间比较（根据其对应 Unicode 值来比较字符串的大小）
 *  逻辑运算符：&& 或 and、|| 或 or、! 或 not
 *  empty 运算符，主要用来判断值是否为空（null、空字符串、空集合），如，判断集合中有元素：`${empty list}`、`${not empty list}`
 *  条件运算符：`${ A ? B : C}`

## EL 的内置对象 ##

 *  EL包含 11 个内置对象，其中，只有PageContext 对象的类型是 ServletContext，其它 10 个对象的类型是 **Map**
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414373911.png) 
    图 1 EL隐含对象

# JSTL（JSP 标准标签库） #

 *  JSTL 是 Sun 提供的一套标签库

## 使用标签库 ##

 *  引入入 jar 包：taglibs-standard-spec-1.2.5.jar、taglibs-standard-impl-1.2.5.jar
 *  在对应的 JSP 页面中引入要使用的标签库，如引入核心标签库、国际化标签库`<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>``<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>`
 *  使用标签

``````````jsp
<tagPrefix:tagName tagAttribute="tagValue" ...>
     <tagBody/>
 </tagPrefix:tagName>

 <%-- 如果该标签没有标签体 --%>
 <tagPrefix:tagName tagAttribute="tagValue" .../>
``````````

## JSTL 中常用的标签 ##

### 逻辑判断标签（if、choose） ###

 *  需要引入核心标签库
 *  if：单条件判断

``````````jsp
<c:if test="${checkCondition}" var="varName" scope="page|request|session|application">
     body content
 </c:if>
``````````

 *  属性
    
     *  test：判断的条件表达式
     *  var：用来存放表达式的结果，即 true 或 false
     *  scope：var变量的作用域范围
     *  body content：标签体，若表达式的结果为 true，则显示（可以没有）
 *  choose-when-otherwise：多条件判断

### 循环迭代标签（forEach） ###

 *  需要引入核心标签库
 *  底层原理：每次迭代会把每一个被迭代的元素，存放到 pageContext 作用域中

``````````jsp
<c:forEach items="${collection}" var="varName" varstatue="varStatusName" begin="begin" end="end" step="step">
    // 循环体
</c:forEach>
``````````

 *  属性
    
     *  items：要迭代的集合类名
     *  var：指定保存在 collection 集合类中的对象名称，默认放在 pageScope 作用域中
     *  varStatus：显示循环状态的变量，其属性包括：
        
         *  index：返回当前迭代元素的索引
         *  count：返回当前迭代了几个元素
         *  first：如果当前被迭代元素是否是第一个元素则显示 true
         *  last：如当前被迭代元素是否是最后一个元素则显示 true
     *  begin：循环的初始值
     *  end：循环结束
     *  step：步长，循环间隔的数值

### 时间格式化标签（formatDate） ###

 *  需要引入国际化标签库

``````````jsp
<%
    pageContext.setAttribute("date", new java.util.Date());
%>
<fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss" var="time"/>
${time}
``````````

 *  属性
    
     *  value：要显示的日期
     *  pattern：自定义格式模式
     *  var：存储格式化日期的变量名


[EL]: https://static.sitestack.cn/projects/sdky-java-note/1db7d725fe61945b629ad9a75326d4aa.png