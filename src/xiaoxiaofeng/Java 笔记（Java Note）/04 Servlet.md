# Servlet 的开发 #

 *  继承 HttpServlet
 *  重写 service(HttpServletRequest req, HttpServletResponse resp) 或者重写 doGet() 和 doPost() 两个方法
 *  注意：不用为 Servlet 类编写构造器如果需要对 Servlet 执行初始化操作，应将初始化操作放在 Servlet 的 init()方法中定义如果重写了 init(ServletConfig config) 方法，则应在重写该方法的第一行调用 super.init(config)，该方法将调用 HttpServlet 的 init 方法

# Servlet 的配置 #

## 在 Servlet 类中使 @WebServlet 注解进行配置 ##

 *  @WebServlet 支持的常用属性：name、urlPatterns、loadOnStartup、initParams
 *  注意：**不要**在 web.xml 文件的**根元素** 中指定 metadata-complete="true" **不要**在 web. xml 文件中配置该 Servlet
    

## 通过在 web.xml 文件中进行配置 ##

 *  根元素 `<web-app>`，属性：metadata-complete：当该属性值为 true 时，该 Web 应用将不会加载注解配置的 Web 组件
     - `<welcome-file-list>`：配置首页，如 index. html、index. htm、index. jsp
     - `<servlet>` 元素，配置 Servlet 的名字
       - `<servlet-name>`，指定 Servlet 的名字，不能使用 default、jsp
       - `<servlet-class>`，指定 Servlet 的实现类（全限定名）
       - `<load-on-startup>`，配置应用启动时，创建 Servlet 实例，这个整型值越小，Servlet 就越优先实例化
       - `<init-param>`，配置 Servlet 参数，子元素：`<param-name>`，指定参数名；`<param-value>`，指定参数值
     - `<servlet-mapping>` 元素，配置 Servlet 的 URL
       - `<servlet-name>`，指定 Servlet 的名字，不能为 default 或 jsp
       - `<url-pattern>`，指定 Servlet 映射的 URL 地址（必须以 "/" 开头），可以使用通配符（ * ），可以配置多个
     - `<context-param>`元素，配置该 Web 应用的参数，子元素：`<param-name>`，配置参数名；`<param-value>`，配置参数值
     - `<error-page>`，设置全局错误页面，子元素：`<exception-type>`、`<error-code>`、`<location>`

# JSP/Servlet 的生命周期 #

 *  由 Web 容器进行控制
 *  客户端第一次请求某个 Servlet 时，系统通过反射创建该 Servlet 的实例（调用公共无参的构造器），或 Web 应用启动时立即创建 Servlet 实例
 *  Web 容器调用 Servlet 的 init 方法，对 Servlet 进行初始化
 *  Servlet 初始化后，将一直存在于容器中，用于响应客户端请求（调用其 service 方法）
 *  Web 容器决定销毁 Servlet 时，先调用 Servlet 的 destroy 方法，通常在关闭 Web 应用时销毁 Servlet

# Servlet 接口 #

 *  用于处理及响应客户端的请求

`void init(ServletConfig config)`：创建 Servlet 实例时，调用该方法初始化 Servlet`void service(ServletRequest req, ServletResponse res)`：对用户请求生成响应`void destroy()`：销毁 Servlet 实例时，自动调用该方法的回收资源`ServletConfig getServletConfig()`：返回 ServletConfig 对象，该对象包含此 servlet 的初始化和启动参数`String getServletInfo()`：返回有关 servlet 的信息，比如作者、版本和版权

# ServletConfig 接口 #

`String getInitParameter(String name)`：获取此 servlet 的指定初始化参数名称的值（获取配置参数）`Enumeration<String> getInitParameterNames()`：获取此 servlet 的初始化参数的所有名称（以 String 对象的 Enumeration 的形式）`ServletContext getServletContext()`：获取应用上下文`String getServletName()`：返回此 servlet 实例的名称

# 继承体系 #

 *  抽象类：GenericServlet，实现了 Servlet、ServletConfig、Serializable 接口
 *  抽象类：HttpServlet，继承了 GenericServlet 抽象类

`void service(HttpServletRequest req, HttpServletResponse resp)`：接收来自 public service 方法的标准 HTTP 请求，并将它们分发给此类中定义的 doXXX 方法`void doGet(HttpServletRequest req, HttpServletResponse resp)`：由服务器调用（通过 service 方法），以允许 servlet 处理 GET 请求`void doPost(HttpServletRequest req, HttpServletResponse resp)`：由服务器调用（通过 service 方法），以允许 servlet 处理 POST 请求

# ServletContext 接口 #

 *  让多个 JSP、Servlet 共享数据`setAttribute(String attrName, Object value)`、`getAttribute(String attrName)`
 *  获取 Web 应用配置参数`String getInitParameter(String paramName)``Enumeration<String> getInitParameterNames()`
 *  获取路径`String getRealpath(String path)`：根据给定资源的相对**虚拟**路径获取该资源在服务器文件系统上的绝对文件路径`String getContextpath()`：获取上下文路径，即  元素的 path 属性值（在request 对象中也提供了该方法） `InputStream getResourceAsStream(String path)`：以 InputStream 对象的形式返回位于指定路径上的资源
 *  其它`String getMimeType(String file)`：获取指定文件的 MIME 类型，如果 MIME 类型未知，则返回 null，常见 MIME 类型： "text/html" 和 "image/gif"
    

# HttpServletRequest 接口 #

 *  继承了 ServletRequest 接口
 *  如果 **POST 请求**的请求参数里包含非西欧字符，则必须在获取请求参数之前先调用 `void setCharacterEncoding(String env)` 方法设置编码的字符集
 *  如果 GET 请求的请求参数里包含非西欧字符方法一：在获取请求参数值之后对请求参数值重新编码（先将其转换成字节数组，再将字节数组重新解码成字符串）方法二：修改 Tomcat 的配置文件，在 server.xml 的  元素中加入属性 URIEncoding="utf-8"
 *  获取请求行信息`String getMethod()`：获取请求方式`String getRequestURI()`：获取请求的 URI，从协议名称一直到查询字符串的那一部分，即返回请求行中的资源名，包括上下文路径，如 /test/index.html`StringBuffer getRequestURL()`：获取请求的 URL，包含协议、服务器名、端口号、资源路径信息，不包含查询字符串参数，即浏览器地址栏的内容`public String getQueryString()`：获取包含在请求 URL 中路径后面的查询字符串，即问号后的字符串`String getContextPath()`：获取上下文路径，即  元素的 path 属性值
 *  获取请求头信息`String getHeader(String name)`：获取指定请求头的值`Enumeration<String> getHeaderNames()`：获取所有请求头的名称`Enumeration<String> getHeaders(String name)`：获取指定请求头的多个值`int getIntHeader(String name)`：获取指定请求头的值，并将该值转为整数值
 *  获取请求参数（每个有 name 属性的表单域对应一个请求参数）`String getParameter(String paramName)`：根据参数名称，获取对应请求参数的值`String[] getParameterValues(String paramName)`：根据参数名称，获取对应请求参数的多个所组成的数组`Map getParameterMap()`：获取所有请求参数名和参数值所组成的 Map 对象`Enumeration<String> getParameterNames()`：获取所有请求参数名所组成的 Enumeration 对象
 *  操作 request 范围的属性`setAttribute(String attrName, Object value)`、`getAttribute(String attrName)`
 *  执行请求转发（forward）或请求包含（include）
    
     *  HttpServletRequest 类提供了一个 `getRequestDispatcher (String path)` 方法（获取请求分发器），其中 path 就是希望 forward 或者 include 的目标路径（以斜线开头表示当前 web 应用的根路径，不以斜线开头表示相对路径），该方法返回 **RequestDispatcher**，该对象提供了如下两个方法：
        
         *  `void forward(ServletRequest request, ServletResponse response)`：执行 forward
         *  `void include(ServletRequest request, ServletResponse response)`：执行 include
     *  forward 用户请求时，请求参数和 request 范围的属性都不会丢失
 *  获取网络信息`String getRemoteAddr()`：返回发出请求的客户机的 IP 地址
 *  其它`ServletInputStream getInputStream()`：以二进制数据形式获取请求正文，返回输入流`BufferedReader getReader()`：获取请求正文
    

# HttpServletResponse 接口 #

 *  继承了 ServletResponse 接口
 *  常用方法`void setCharacterEncoding(String charset)`：设置将发送到客户端的响应的字符编码`void setContentType(String type)`：设置将发送到客户端的响应的内容类型（MIME 类型、编码方式），如 "text/html; charset=UTF-8"、"application/x-msdownload"`void setHeader(String name, String value)`：用给定名称和值设置响应头`void setStatus(int sc)`：设置此响应的状态代码
 *  response 响应生成非字符响应`ServletOutputStream getOutputStream()`：获取响应输出字节流，可用于文件下载`PrintWriter getWriter()`：获取可将字符文本发送到客户端的字符输出流
 *  URL 重定向（redirect）
    
     *  `void sendRedirect(String path)`：重新向 path 资源发送请求，path 需加上上下文路径（当以 http 开头时，表示重定向到外部的一个资源）
     *  URL 重定向是第一次请求的响应码为 302 并且响应头中有 Location，那么**浏览器**发送将根据 Location 的地址发出第二次请求
     *  与 forward 不同的是，重定向会丢失所有的请求参数和 request 范围的属性，因为重定向将生成第二次请求
 *  增加 Cookie
    
     *  Cookie 与 session 的不同之处在于：session 会随浏览器的关闭而失效，但 Cookie 会一直存放在**客户端**机器上，除非超出 Cookie 的生命期限
     *  大部分 session 机制都**使用会话 cookie 来保存 session id**
     *  cookie 的内容主要包括：名字、值、过期时间、路径和域
     *  cookie 的使用是由浏览器按照一定的原则在后台自动发送给服务器的：浏览器检查所有存储的 cookie，如果某个 cookie 所声明的作用范围大于等于将要请求的资源所在的位置，则把该 cookie **附在请求资源的 http 请求头上**发送给服务器
     *  `String encodeURL(String url)`：通过将会话 ID 包含在指定 URL 中对该 URL 进行编码，如果不需要编码，则返回未更改的 URL（URL 重写，用于不支持 cookie 的浏览器）

``````````java
// 增加 Cookie 
  // 获取请求参数 
  String name = request.getParameter("name"); 
  // 以获取到的请求参数为值，创建一个 Cookie 对象 
  Cookie c = new Cookie("username", URLEncoder.encode(name, "utf-8")); 
  // 设置 Cookie 对象的生存期限（本 Cookie 设置为 24 小时） 
  //  如果没有设置其生存期限，Cookie 将会随浏览器的关闭而自动消失 
  c.setMaxAge(24 * 3600); 
  // 向客户端増加 Cookie 对象 
  response.addCookie(c); 
  
  // 读取 Cookie 
  // 获取本站在客户端上保留的所有 Cookie 
  Cookie[] cookies = request.getCookies(); 
  // 遍历客户端上的每个 Cookie 
  if(cookies != null) { 
     for (Cookie c : cookies) { 
         // 如果 Cookie 的名为 username，表明该 Cookie 是需要访问的 Cookie 
         if(c.getName().equals("username") { 
             out.println(URLDecoder.decode(c.getValue(), "utf-8")); 
         } 
     } 
  }
``````````

 *  默认情况下，Cookie 值不允许出现中文字符
 *  如果需要值为中文内容的 Cookie，可以借助于 URLEncoder 先对中文字符串进行编码，将编码后的结果设为 Cookie 值；当程序要读取 Cookie 时，则应该先读取，然后使用 URLDecoder 对其进行解码
 *  Cookie 的 value、domain、path 校验规则详见 `Rfc6265CookieProcessor`

# HttpSession 接口 #

 *  一次用户会话的含义是：从客户端浏览器连接服务器开始，到客户端浏览器与服务器断开为止，这个过程就是一次会话。
 *  session 的属性值可以是任何**可序列化的 Java 对象**
 *  获取方法：`HttpSession session = request.getSession();`：获取与此请求关联的当前会话，如果该请求没有会话，则创建一个会话（只有在获取时才开始创建）
 *  实例方法`void setAttribute(String attName, Object attValue)`：使用指定名称将对象绑定到此会话`Object getAttribute(String attName)`：返回与此会话中的指定名称绑定在一起的对象`String getId()`：返回包含分配给此会话的唯一标识符的字符串`void removeAttribute(String name)`：从此会话中移除与指定名称绑定在一起的对象`void invalidate()`：使此会话无效`void setMaxInactiveInterval(int interval)`：指定在 servlet 容器使此会话失效之前客户端请求之间的时间间隔，以秒为单位（负数时间指示会话永远不会超时）
 *  应用
    
     *  登录和注销（跟踪用户的 Session）登录检查：登录成功后将 user 对象设置到 HttpSession 作用域；在拦截器中检查 session 中是否存在 user 对象，若不存在并拦截，否则放行
     *  基于 Session 的购物车
     *  验证码的使用（比较用户填写的验证码和 session 中保存的验证码是否相同）
     *  防止表单重复提交（验证表单隐藏域中的标识和 session 中的标识是否相同）

# Servlet 线程安全问题 #

 *  让 Servlet 类实现 SingleThreadModel 接口
 *  在 Servlet 中不使用成员变量

# Servlet 的三大作用域 #

``````````java
// 1. HttpServeltRequest 对象 
  
  // 2. HttpSession 对象 
  HttpSession session = request.getSession(); 
  
  // 3. ServeltContext 对象 
  ServletContext sc = super.getServletContext(); 
  ServletContext sc = config.getServletContext(); 
  ServletContext sc = request.getServletContext(); // Tomcat 7 才有 
  ServletContext sc = session.getServletContext();
``````````

# JSP、Servlet 之间交换数据 #

 *  Web 服务器提供 4 个类似 Map 的结构，分别是 application、session、request、page，允许 JSP、Servlet 将数据放入这 4 个类似 Map 的结构中，或从这 4 个 Map 结构中取出数据，这 4 个 Map 结构的范围（scope）：
    
     *  page：仅对当前页面有效（Servlet 中没有）
     *  request：仅对本次请求有效
     *  session：仅对一次会话有效
     *  application：对于整个 Web 应用有效
 *  多个 JSP、Servlet 共享数据的方法： `void setAttribute(String name, Object value)`：往作用域对象中设置属性 `Object getAttribute(String name)`：从作用域对象中获取指定属性名的属性值，如果不存在给定名称的属性，则返回 null `void removeAttribute(String name)`：从作用域对象中去删除指定名称的属性

# Servlet 的职责 #

 *  获取请求参数，封装成对象
 *  调用业务方法处理请求
 *  控制页面跳转，跳转到某一个 JSP 负责做界面输出操作

# MVC 模式 #

 *  Model2 是 MVC 设计思想下的架构
 *  MVC：Model + View + Controller（数据模型+视图+控制器）
 *  Servlet 则仅充当控制器（Controller）角色，它的作用类似于调度员：所有用户请求都发送给 Servlet，Servlet 调用 Model 来处理用户请求，并调用 JSP 来呈现处理结果；或者 Servlet 直接调用 JSP 将应用的状态数据呈现给用户
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414367092.png) 
    图 1 MVC组件类型的关系和功能

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414367596.png) 
图 2 Model1的流程

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414368047.png) 
图 3 Model2的流程

# 文件上传 #

## 文件上传表单 ##

 *  表单提交方式 method="post"
 *  表单的编码类型 enctype="multipart/form-data" （以二进制的形式进行数据的传输）
 *  文件上传控件 type="file"

``````````xml
<form action="/upload" method="post" enctype="multipart/form-data"> 
      <input type="file" name="headImg" accept="image/*" /> 
  </form>
``````````

## 文件上传相关类 ##

 *  基于Apache FileUpload组件
 *  需要的 jar 包：commons-fileupload.jar、commons-io.jar
 *  步骤：
    
     *  判断是否有上传的文件
     *  获取保存路径（判断路径是否存在，如果不存在就创建一个）
     *  将上传文件保存到目标文件中，可使用 `commons.io.FileUtils.writeByteArrayToFile()` 快速写文件到磁盘

### DiskFileItemFactory 类，实现的接口：FileItemFactory ###

 *  构造器：DiskFileItemFactory()、DiskFileItemFactory(int sizeThreshold, File repository)
 *  实例方法：`void setSizeThreshold(int sizeThreshold)`：设置缓存大小，小于则直接存在内存中，大于则写到临时目录中（单位：字节）`void setRepository(File repository)`：设置临时目录的路径

### ServletFileUpload 类，文件上传处理器 ###

 *  构造器：ServletFileUpload(FileItemFactory fileItemFactory)
 *  实例方法：`boolean isMultipartContent(HttpServletRequest request)`：判断请求是否为处理上传表单的请求`List<FileItem> parseRequest(HttpServletRequest request)`：解析请求，将 request 对象中的每一个表单元素封装到 FileItem 对象中，返回 FileItem 列表`void setFileSizeMax(long fileSizeMax)`：设置单个上传文件的允许大小（单位：字节），超过时抛出 FileSizeLimitExceededException`void setSizeMax(long sizeMax)`：设置整个上传表单的允许大小（单位：字节），超过时抛出 SizeLimitExceededException

### FileItem 接口 ###

 *  实例方法：`boolean isFormField()`：判断是否是普通表单元素`long getSize()`：获取表单元素的内容大小`String getFieldName()`：获取表单元素的 name 属性值（参数名）`String getString(String encoding)`：获取表单元素的 value 属性值（参数值）`String getName(String filename)`：获取文件上传控件的文件名（IE 带路径名）`void write(File file)`：把二进制数据写到文件中

### FilenameUtils 类，文件名工具类 ###

 *  类方法：`String getName(String filename)`：获取文件的名称（不含路径名）`String getExtension(String filename)`：获取文件的拓展名`String getBaseName(String filename)`：获取文件的基名（不含拓展名）`String getFullPath(String filename)`：获取文件所在的绝对路径

# 文件下载 #

``````````java
// 处理 GET 请求参数中文乱码 
  filename = new String(filename.getBytes("ISO-8859-1"), "UTF-8"); 
  
  // 获取下载文件所在的路径 
  String dir = request.getServletContext().getRealPath("/WEB-INF/upload"); 
  File file = new File(dir, filename); 
  // 判断文件是否真实存在 
  if (file.exists()) { 
      // 下载显示的文件名，解决中文名称乱码问题 
      String downloadFielName = ""; 
      String userAgent = request.getHeader("User-Agent"); 
      if (userAgent.toUpperCase().contains("MSIE") || userAgent.toUpperCase().contains("TRIDENT") || userAgent.toUpperCase().contains("EDGE")) { 
          // 对于 IE 或 Edge 浏览器 
          downloadFielName = URLEncoder.encode(filename, "UTF-8"); 
      } else { 
          // 对于 W3C 浏览器 
          downloadFielName = new String(filename.getBytes("UTF-8"), "ISO8859-1"); 
      } 
  
      // 设置响应报文中对象的媒体类型 
      response.setContentType("application/octet-stream"); 
  
      // 设置响应报头，以 attachment（下载方式） 打开文件 
      response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadFielName + "\""); 
  
      // 通过流的方式将文件输出到浏览器 
      OutputStream out = response.getOutputStream(); 
      Files.copy(Paths.get(dir, filename), out); 
  }
``````````


[MVC]: https://static.sitestack.cn/projects/sdky-java-note/2a431b7da8f76bd17c6f9831682e9d6d.png
[Model1]: https://static.sitestack.cn/projects/sdky-java-note/ed4291614bf10b3442935f5c2de66ebb.png
[Model2]: https://static.sitestack.cn/projects/sdky-java-note/abfdff0ac279c26c83569704e5158667.png