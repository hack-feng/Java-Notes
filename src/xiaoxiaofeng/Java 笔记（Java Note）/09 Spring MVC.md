# Spring MVC 和 Struts2 对比 #

 *  Spring MVC 的入口是 Servlet，而 Struts2 是 Filter
 *  Spring MVC 会稍微比 Struts2 快些：Spring MVC 是基于方法设计，而 Sturts2 是基于类，每次发一次请求都会实例一个 Action
 *  Spring MVC 使用更加简洁，开发效率 Spring MVC 比 struts2 高（支持 JSR303，处理 Ajax 请求更方便）
 *  Struts2 的 OGNL 表达式使页面的开发效率相比 Spring MVC 更高些
 *  https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414398982.png) 
    图 1 Spring\_MVC\_Table\_of\_Contents

# Spring MVC 执行流程 #

 *  在 Spring MVC 框架中，控制器实际上由两个部分共同组成，即拦截所有用户请求和处理请求的通用代码都由前端控制器 **DispatcherServlet** 完成，而实际的业务控制（诸如调用后台业务逻辑代码，返回处理结果等）则由 **Controller** 处理
 *  org.springframework.web.servlet.DispatcherServlet\#doDispatch
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414399661.png) 
    图 2 Spring\_MVC请求——响应的完整流程

 *  客户端向服务器发送请求，如果匹配 DispatcherServlet 的请求映射路径（在 web.xml 中指定），则 Web 容器将该请求转交给 DispatcherServlet 处理
 *  DispatcherServlet 根据请求的信息（URL、HTTP 方法等），调用 HandlerMapping 找到处理请求的 Handler 对象以及 Handler 对象对应的拦截器，这些对象会被封装到一个 HandlerExecutionChain（执行链） 对象当中返回给 DispatcherServlet
 *  DispatcherServlet 根据获得的 Handler，选择一个合适的 HandlerAdapter，以统一的接口对 Handler 方法进行调用 完成实际处理请求。在提取请求中的模型数据，填充 Handler 的**入参**过程中，根据配置，Spring 还会完成一些额外的工作：
    
     *  消息转换：将请求消息（如 Json、xml 等数据）转换成一个对象，将对象转换为指定的响应信息
     *  数据转换：对请求消息进行数据转换，如 String 转换成 Integer、Double 等
     *  数据格式化：对请求消息进行数据格式化，如将字符串转换成格式化数字或格式化日期等
     *  数据验证：验证数据的有效性（长度、格式等），验证结果存储到 BindingResult 或 Error 中
 *  Handler 执行完成后，向 DispatcherServlet 返回一个 ModelAndView 对象，ModelAndView 对象包含视图逻辑名和模型数据信息
 *  DispatcherServlet 根据返回的 ModelAndView，选择一个合适的 ViewResolver（视图解析器）完成逻辑视图名到真实视图对象的解析，得到真实的视图对象 View
 *  DispatcherServlet 使用得到的 View 对象对 ModelAndView 中的模型数据进行视图渲染
 *  DispatcherServlet 将视图渲染结果返回给客户端

``````````java
// DispatcherServlet 中的初始化策略方法 
  protected void initStrategies(ApplicationContext context) { 
      // 用于处理上传请求。处理方法是将普通的 request 包装成 MultipartttpservletRequest，后者可以直接调用 getFile 方法获取 
      initMultipartResolver(context); 
      // SpringMVC 主要有两个地方用到了 Locale：一是 ViewResolver 视图解析的时候；二是用到国际化资源或者主题的时候 
      initLocaleResolver(context); 
      // 用于解析主题 
      // SpringMVC 中一个主题对应一个 properties 文件，里面存放着跟当前主题相关的所有资源，如图片、css 样式等。SpringMVC 的主题也支持国际化 
      initThemeResolver(context); 
      // 用来查找 Handler 
      initHandlerMappings(context); 
      // 从名字上看，它就是一个适配器。servlet需要的处理方法的结构却是固定的，都是以 request 和 response 为参数的方法。 
      // 如何让固定的 servlet 处理方法调用灵活的 Handler 来进行处理？这就是 HandlerAdapter 要做的事情 
      initHandlerAdapters(context); 
      // 对异常情况进行处理 
      initHandlerExceptionResolvers(context); 
      // 有的Handler处理完后并没有设置 View 也没有设置 Viewlame，这时就需要从 request 获取ViewName 了， 
      // 如何从 request 中获取 ViewName 就是 RequestToViewNameTranslator 要做的事情了。 
      initRequestToViewNameTranslator(context); 
      // ViewResolver 用来将 String 类型的视图名和 Locale 解析为 View 类型的视图 
      // View 是用来渲染页面的，也就是将程序返回的参数填入模板里，生成 html（也可能是其它类型）文件 
      initViewResolvers(context); 
      // 用来管理 FlashMap 的，FlashMap 主要用在 redirect 重定向中传递参数 
      initFlashMapManager(context); 
  }
``````````

# 常用 API #

 *  Controller 接口`ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)`
 *  Mode 接口，模型数据的存储容器，类似 Map 接口`Model addAttribute(String attributeName, Object attributeValue)`：// 添加模型数据
 *  ModelAndView 类`ModelAndView addObject(String attributeName, Object attributeValue)`： 添加模型数据`void setViewName(String viewName)`：设置逻辑视图名，视图解析器会根据该名字解析到具体的视图页面

``````````java
// 在 Bean 中获取 request、session 
  @Autowired 
  private HttpServletRequest request; // 自动注入request 
  
  // 非表现层获取 request、session 的方法 
  ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes(); 
  HttpSession session = attrs.getRequest().getSession(); 
  
  // interceptor 获取当前被拦截的请求处理方法 
  // 动态的请求：handler -----> HandlerMethod 对象 
  // 静态的请求：handler -----> DefalueMethod 对象 <mvc:default-handler> 
  HandlerMethod hm = (HandlerMethod) handler; 
  Method method = hm.getMethod();
``````````

> 
> 只有无状态的 Bean 才可以在多线程环境下共享，在 Spring 中，绝大部分 Bean 都可以声明为 singleton 作用域
> Spring 对一些 Bean（如 RequestContextHolder、TransactionSynchronizationManager、LocaleContextHolder 等）中非线程安全的“状态性对象”采用 **ThreadLocal** 进行封装，让它们也成为线程安全的“状态性对象”，因此，有状态的 Bean 就能够以 singleton 的方式在多线程中正常工作
> 
> 
> 对于实体 Bean 在多线程中的处理：1. 对于实体 Bean 一般通过方法参数的的形式传递（参数是局部变量，所以多线程之间不会有影响）；2. 有的地方对于有状态的 Bean 直接使用 prototype 原型模式来进行解决；3. 对于使用 Bean 的地方可以通过 new 的方式来创建）
> 

# 请求处理方法 #

## 请求处理方法可返回的类型 ##

 *  ModelAndView、Model、Map、View、String、void、对象类型、StreamingResponseBody
 *  HttpEntity 、 **ResponseEntity** 
 *  若方法的返回值为 String
    
     *  此 String 表示逻辑视图名称（即用于 ViewResolver 解析的视图名），完整物理的视图名是：前缀+逻辑视图名+后缀
     *  若 String 中包含 “forward:” 前缀，表示请求转发，其后的字符作为 URL 处理，相当于 `request.getRequestDispatcher("").forward(request,response);`
     *  若 String 中包含 “redirect:” 前缀，表示重定向，其后的字符作为 URL 处理，相当于 `response.sendRedirect("");`
 *  若没有返回逻辑视图名称，则默认使用被访问的 RequestMapping 的 value 值作为逻辑视图名称
    

## 请求处理方法可出现的参数类型 ##

 *  Spring MVC 会根据请求方法签名不同，将请求消息中的信息以一定的方式转换并绑定到请求方法的参数中
 *  HttpServletRequest、HttpServletResponse、HttpSession、HttpEntity
 *  InputStream、OutputStream
 *  Map、ModelMap、Model、MultipartFile
 *  简单类型：参数为基本数据类型时，必须保证请求参数的值不能为 null 或 ""，否则会出现数据转换的异常；参数为包装类型或 String 类型时，请求参数的值可以为 null 或 ""
 *  复合类型：请求参数为 `形参名.属性`；属性类型为 List 时，需要在请求参数中指定 List 的下标
 *  数组（不支持 List 集合）：有多个**同名**的请求参数时，Spring MVC 会自动封装成数组
 *  Data
 *  Optional
    

# 视图和视图解析器 #

 *  Spring MVC 支持的视图
 *  org.springframework.web.servlet.view
 *  常见的视图：InternalResourceView、JstlView、FreeMarkerView、AbstractXlsView、AbstractXlsxView、AbstractPdfView、MappingJackson2JsonView、MappingJackson2XmlView、RedirectView
 *  常见的视图解析器：InternalResourceViewResolver、FreeMarkerViewResolver、ThymeleafViewResolver、BeanNameViewResolver、ContentNegotiatingViewResolver、HandlerExceptionResolver

# 配置 DispatcherServlet #

## 使用 web.xml 配置 ##

 *  配置 DispatcherServlet 作为前端控制器来拦截用户请求，修改 contextConfigLocation 的参数值为 Spring MVC 的配置文件路径（classpath:mvc.xml），并设置为 load-on-startup（启动时创建创建所有处理器）
 *  url-pattem 元素的值使用 "\*.do"（若使用"/"，还需在 mvc.xml 中配置 `<mvc:default-servlet-handler />`，对进入 DispatcherServlet 的 URL 进行检查，如果发现是静态资源的请求，就将该请求转由 Web 应用服务器默认的 Servlet 处理；如果不是静态资源的请求，则由 DispatcherServlet 继续处理）
 *  配置字符编码过滤器：使用 CharacterEncodingFilter 作为过滤器，修改 encoding 的参数值为 UTF-8，forceEncoding 的参数值为 true

``````````xml
<web-app> 
  
      <listener> 
          <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class> 
      </listener> 
  
      <context-param> 
          <param-name>contextConfigLocation</param-name> 
          <param-value>/WEB-INF/root-context.xml</param-value> 
      </context-param> 
  
      <servlet> 
          <servlet-name>app1</servlet-name> 
          <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class> 
          <init-param> 
              <param-name>contextConfigLocation</param-name> 
              <param-value>/WEB-INF/app1-context.xml</param-value> 
          </init-param> 
          <load-on-startup>1</load-on-startup> 
      </servlet> 
  
      <servlet-mapping> 
          <servlet-name>app1</servlet-name> 
          <url-pattern>/app1/*</url-pattern> 
      </servlet-mapping> 
  
  </web-app>
``````````

## 使用 Java Config 配置 ##

 *  方式 1：实现 WebApplicationInitializer

``````````java
public class MyWebApplicationInitializer implements WebApplicationInitializer { 
  
      @Override 
      public void onStartup(ServletContext servletCxt) { 
          // Load Spring web application configuration 
          AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext(); 
          ac.register(AppConfig.class); 
          ac.refresh(); 
  
          // Create and register the DispatcherServlet 
          DispatcherServlet servlet = new DispatcherServlet(ac); 
          ServletRegistration.Dynamic registration = servletCxt.addServlet("app", servlet); 
          registration.setLoadOnStartup(1); 
          registration.addMapping("/app/*"); 
      } 
  }
``````````

``````````java
public class MyWebApplicationInitializer implements WebApplicationInitializer { 
  
      @Override 
      public void onStartup(ServletContext container) { 
          XmlWebApplicationContext appContext = new XmlWebApplicationContext(); 
          appContext.setConfigLocation("/WEB-INF/spring/dispatcher-config.xml"); 
  
          ServletRegistration.Dynamic registration = container.addServlet("dispatcher", new DispatcherServlet(appContext)); 
          registration.setLoadOnStartup(1); 
          registration.addMapping("/"); 
      } 
  }
``````````

 *  方式 2：继承 AbstractAnnotationConfigDispatcherServletInitializer 或 AbstractDispatcherServletInitializer

``````````java
// 配置 WebApplicationContext 层级 
  // 如果不需要应用上下文分层，可以通过 getRootConfigClasses() 方法返回所有配置，而 getServletConfigClasses() 方法返回 null 
  public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer { 
  
      @Override 
      protected Class<?>[] getRootConfigClasses() { 
          return new Class<?>[] { RootConfig.class }; 
      } 
  
      @Override 
      protected Class<?>[] getServletConfigClasses() { 
          return new Class<?>[] { App1Config.class }; 
      } 
  
      @Override 
      protected String[] getServletMappings() { 
          return new String[] { "/app1/*" }; 
      } 
  }
``````````

``````````java
public class MyWebAppInitializer extends AbstractDispatcherServletInitializer { 
  
      @Override 
      protected WebApplicationContext createRootApplicationContext() { 
          return null; 
      } 
  
      @Override 
      protected WebApplicationContext createServletApplicationContext() { 
          XmlWebApplicationContext cxt = new XmlWebApplicationContext(); 
          cxt.setConfigLocation("/WEB-INF/spring/dispatcher-config.xml"); 
          return cxt; 
      } 
  
      @Override 
      protected String[] getServletMappings() { 
          return new String[] { "/" }; 
      } 
  }
``````````

# 配置 Spring MVC #

## 使用 XML 配置 —— mvc.xml ##

 *  配置 3 个 Spring MVC 核心组件 Bean（Spring4.0 之后可以不配置，使用默认的），包括 BeanNameUrlHandlerMapping（处理器转换器）、SimpleControllerHandlerAdapter（处理器适配器）、InternalResourceViewResolver（视图解析器，主要通过设置前缀、后缀，以及控制器中方法来返回视图名的字符串，以得到实际页面）
 *  定义处理用户请求的处理器 Bean：id 或 name（要有斜杠）、class
 *  修改视图解析器在 mvc.xml 中重新配置视图解析器 InternalResourceViewResolver，修改属性 prefix、suffix（默认为空字符串）

## 使用 Java Config 配置 ##

# Spring MVC 的常用注解 #

 *  在 mvc.xml 中开启 Spring MVC 注解的解析器 `<mvc:annotation-driven/>`，使用该标签后，会自动注册核心 Bean

## @Controller ##

 *  用于指示该类的实例是一个控制器

## @RequestMapping ##

 *  可用于类或方法，用来转换 Web 请求（访问路径和参数）
 *  常用属性：
    
     *  value、path：用于将指定请求的实际地址转换到方法上，value 的属性值可以不带斜杠
     *  method：用来指定该方法仅仅处理哪些 HTTP 请求方式，包括 GET、POST、HEAD、OPTIONS、PUT、PATCH、DELETE、TRACE，如果没有指定 method 属性值，则请求处理方法可以处理任意的 HTTP 请求方式
     *  consumes：指定处理**请求的提交内容类型**（Content-Type），如 "application/json"、"text/html"、"application/x-www-form-urlencoded"、"multipart/form-data"（MediaType 提供了常用的媒体类型）
     *  produces：指定**返回的内容类型**，返回的内容类型必须是 request 请求头（Accept）中所包含的类型，如 "application/json;charset=UTF-8"、"application/json"
     *  headers：指定请求中必须包含某些指定的 header 值，才能让该方法处理，如 "Accept=application/json"
     *  params：指定请求中必须包含某些参数值时，才让该方法处理，如 params="myParam=myValue”，方法仅处理其中名为“myParam”、值为“myValue”的请求
 *  组合注解：@GetMapping、@PostMapping、@PutMapping、@DeleteMapping、PatchMapping
 *  后缀匹配：Spring MVC 中默认将 `.` *作为匹配后缀，即映射到 `/person` 的方法也隐式映射到 `/person.`*。通过重写 WebMvcConfigurerAdapter 类中的 configurePathMatch 方法可设置不忽略“.”后面的参数，`configurer.setUseSuffixPatternMatch(false)`（Spring Boot 默认设置为 `false`）
 *  URL Path 匹配原则：
    
     *  `?` 匹配任何单个字符
     *  `*` 匹配 0 或者任意数量的字符
     *  `**` 匹配 0 或者更多的目录
     *  最长匹配原则：存在多个路径匹配模式时，Spring MVC 会以最长符合路径模式来匹配一个路径

## @CrossOrigin ##

 *  可用于类或方法，**设置跨域行为**，常用属性：origins（允许域名）、methods、allowedHeaders、exposedHeaders、allowCredentials（是否允许发送 Cookie，启用后允许域名不能设置为 '\*'）、maxAge（本次预检请求的有效期，单位为秒）
 *  默认情况下 @CrossOrigin 设置的默认值：允许所有源，允许所有请求头，允许 GET、POST、HEAD 方法，不启用 allowedCredentials，maxAge 被设置为 30 分钟，详见 `CorsConfiguration#applyPermitDefaultValues()`

## @ResponseStatus ##

## 参数绑定注解 ##

> 
> 如果目标方法参数类型不是字符串，则自动应用类型转换
> 
> 
> 处理方法入参最多只能使用一个 Spring MVC 的注解，否则将抛出异常
> 

### @RequestParam ###

 *  用于将指定的请求参数设置到方法参数
 *  属性：name、required（默认 true）、defaultValue

### @PathVariable ###

 *  用于将 REST 风格的请求 URL 中的动态参数设置到方法参数，属性 value 省略则默认绑定同名参数，默认情况下参数支持简单类型（由 BeanUtils\#isSimpleProperty 决定，如 int、long、Date 等）

``````````java
// 访问 http://localhost/departments/3 
  @RequestMapping(value = "/departments/{deptId}", method = RequestMethod.DELETE) 
  @ResponseBody 
  public void deleteDept(@PathVariable("deptId") Long deptId, HttpServletResponse response) { 
      response.setStatus(HttpServletResponse.SC_NO_CONTENT); 
  }
``````````

### @RequestHeader ###

 *  用于将请求头中的指定参数值设置到方法参数

### @RequestPart ###

 *  用于将 multipart/form-data 请求中上传的文件设置到方法参数

### @CookieValue ###

 *  用于将请求的 Cookie 数据中的指定参数值设置到方法参数

### @ModelAttribute ###

 *  添加属性到数据模型 Model 中，默认的属性名是**首字母小写的属性值的类名**，属性名可用 @ModelAttribute 中的 value 属性值指定
 *  用法
    
     *  修饰方法：Spring MVC 在调用目标处理方法前，会先逐个调用在方法级上标注了 @ModdAttribUte 注解的方法，并将这些方法的返回值添加到数据模型中
     *  修饰方法的参数：将数据模型中的值设置到方法参数，再用请求消息填充该方法参数对象，如果不存在则实例化，并将方法参数绑定的值添加到数据模型中

### @SessionAttribute ###

### @RequestAttribute ###

## 信息转换 ##

 *  @RestController：修饰类，组合了 @Controller 和 @ResponseBody
 *  @RequestBody：修饰参数，用于读取 Request 请求的 body 部分数据，根据 Content-Type 查找匹配的 HttpMessageConverter 进行解析，转换成对应的 Object，并设置到被修饰的方法参数上（application/json、application/xml 等格式的数据必须使用 @RequestBody 来处理）
 *  @ResponseBody：可修饰类、方法，将方法返回的对象或集合数据通过适当的消息转换器 HttpMessageConverter 转换为指定格式后，写入到 Response 对象的 body 数据区，并将其返回客户端（此时配置的视图解析器失效）

### HttpMessageConverter<T> 接口 ###

 *  HttpMessageConverter 接口负责将请求信息转换为一个对象（类型为 T），并将对象（类型为 T）绑定到请求方法的参数中或输出为响应信息
    

### 转换 Form 提交方式来提交数据 ###

 *  FormHttpMessageConverter
 *  将 application/x-www-form-urlencoded 内容读入到 MultiValueMap 中，也会将 MultiValueMap 写入到 application/x-www-form-urlencoded 中；或将 MultiValueMap写入到 multipart/form-data 中

### 转换 JSON 数据 ###

 *  JSON 序列化和反序列化转换器，用于**转换 Post 请求体中的 JSON** 以及**将对象序列化为返回响应的 JSON**
 *  Spring 默认使用 Jackson 处理 json 数据（可通过 HttpMessageConverter 进行自定义配置）
 *  Spring MVC 默认使用 MappingJackson2HttpMessageConverter 转换 JSON 格式的数据
 *  在 JSON 和类型化的对象或非类型化的 HashMap 间互相读取和写入
 *  在 Spring Boot 中，可以配置 `spring.jackson.date-format=yyyy-MM-dd HH:mm:ss` `spring.jackson.time-zone=GMT+8`，但**不支持** Java 8 中新的日期和时间 API

``````````java
/** 
   * 注意：自定义 ObjectMapper Bean 时，会导致配置文件中的 spring.jackson.**** 失效 
   */ 
  @Configuration 
  public class JacksonConfig { 
  
      private final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"; 
      private final static String DATE_PATTERN = "yyyy-MM-dd"; 
  
      @Bean 
      public ObjectMapper objectMapper() { 
          DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN); 
          DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN); 
          return new Jackson2ObjectMapperBuilder() 
              .findModulesViaServiceLoader(true) 
              .failOnUnknownProperties(false) 
              // .serializationInclusion(JsonInclude.Include.NON_NULL) 
              .timeZone(TimeZone.getTimeZone("GMT+8")) 
              .simpleDateFormat(DATE_TIME_PATTERN) 
              .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter)) 
              .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter)) 
              .serializerByType(LocalDate.class, new LocalDateSerializer(dateFormatter)) 
              .deserializerByType(LocalDate.class, new LocalDateDeserializer(dateFormatter)) 
              .build(); 
      } 
  }
``````````

 *  自定义 JSON 序列化或反序列化转换器
    
     *  继承 StdSerializer 或 StdDeserializer，重写相应的抽象方法 serialize() 或 deserialize()
     *  在相应的字段上添加 @JsonSerialize(using) 或 @JsonDeserialize(using)，或者在自定义的序列化或反序列化转换器上添加 @JsonComponent
 *  自定义 Jackson：Jackson2ObjectMapperBuilderCustomizer

### 转换 XML 数据 ###

 *  Spring MVC 默认使用 Jaxb2RootElementHttpMessageConverter 转换 XML 格式的数据
 *  在 XML（text/xml 或 application/xml）和使用 JAXB2 注解的对象间互相读取和写入
 *  JAXB（Java Architecture for XML Binding）是一个业界的标准，是一项可以根据 XML Schema 产生 Java 类的技术。该过程中，JAXB 也提供了将 XML 实例文档反向生成 Java 对象树的方法，并能将 Java 对象树的内容重新写到 XML 实例文档。

#### JAXB 常用的注解 ####

 *  javax.xml.bind.annotation 中
 *  @XmlRootElement：修饰类，将 Java 类或枚举类型转换为 xml 文件中的根节点
 *  @XmlAccessorType：修饰类，用于指定由 Java 对象生成 xml 文件时对 Java 对象属性的访问方式，其 value 属性的属性值有 4 个：
    
     *  XmlAccessType.FIELD：Java 对象中的所有成员变量
     *  XmlAccessType.PROPERTY：Java 对象中所有通过 getter/setter 方式访问的成员变量
     *  XmlAccessType.PUBLIC\_MEMBER：Java 对象中所有的 public 访问权限的成员变量和通过 getter/setter 方式访问的成员变量（默认值）
     *  XmlAccessType.NONE：Java 对象的所有属性都不转换为 xml 的元素
 *  @XmlAccessorOrder：修饰类，用于对 Java 对象生成的 xml 元素进行排序，其 value 属性的属性值有 2 个：
    
     *  XmlAccessOrder.UNDEFINED：不排序（默认值）
     *  AccessorOrder.ALPHABETICAL：对生成的 xml 节点按字母顺序排序
 *  @XmlElement：用于把 Java 对象的属性转换为 xml 的子节点，可通过设置其 name 属性值来改变该 java 属性在 xml 文件中的名称
 *  @XmlAttribute：用于把 Java 对象的属性转换为 xml 的属性，可通过设置其 name 属性值为生成的 xml 属性指定别名
 *  @XmlTransient：用于标示在由 Java 对象转换 xml 时，忽略此属性，即在生成的 xml 文件中不出现此元素
 *  @XmlJavaTypeAdapter：常用在转换比较复杂的对象时，如 map 类型或者格式化日期等。使用此注解时，需要写一个 adapter 类继承 XmlAdapter 抽象类，并实现里面的方法，如 `@XmlJavaTypeAdapter(value=xxx.class)`，value 为自己定义的 adapter 类
 *  @XmlElementWrapper：对于数组或集合（即包含多个元素的成员变量），生成一个包装该数组或集合的 XML 元素（称为包装器）

## 类型转换 ##

 *  用于转换 **RequestParam** 和 **PathVariable** 参数

### 使用 Converter 接口 ###

 *  自定义 Converter 类型 Bean

``````````java
@Configuration 
  public class DateFormatConfig { 
      @Bean 
      public Converter<String, LocalDateTime> localDateTimeConverter() { 
          // 不能使用 Lambda 表达式 
          // 当使用 Lambda 表达式而不是匿名内部类时，Spring 无法确定泛型类型 
          // https://stackoverflow.com/questions/25711858/spring-cant-determine-generic-types-when-lambda-expression-is-used-instead-of-a 
          return new Converter<String, LocalDateTime>() { 
              @Override 
              public LocalDateTime convert(String source) { 
                  return LocalDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); 
              } 
          }; 
      } 
  }
``````````

### 使用 Formatter 接口 ###

 *  自定义 Formatter 类型 Bean
 *  WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter\#addFormatters

### 使用 @ControllerAdvice 配合 @lnitBinder ###

 *  @InitBinde 定义控制器参数绑定规则，如转换规则、格式化等，会在控制器初始化时注册属性编辑器，并在参数转换之前执行
 *  WebDataBinder 对象用于处理请求消息和处理方法的绑定工作

``````````java
@ControllerAdvice 
  public class TemporalFormatControllerAdvice { 
      @InitBinder 
      protected void initBinder(WebDataBinder binder) { 
          // 注册属性编辑器 
          binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() { 
              @Override 
              public void setAsText(String text) { 
                  setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))); 
              } 
          }); 
      } 
  }
``````````

### @DateTimeFormat ###

 *  可以修饰 Date、Calendar 等时间类型的参数、字段
 *  如 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")，该注解**支持** Java 8 中新的日期和时间 API
 *  在 Spring Boot 中，可以配置 spring.mvc.date-format=yyyy-MM-dd，但**不支持** Java 8 中新的日期和时间 API

### @NumberFormat ###

## 数据校验 ##

 *  通过在 Bean 属性上标注注解指定校验规则，并通过标注的验证接口对 Bean 进行验证
 *  对 Controller 的入参对象进行数据校验，校验结果保存在被校验入参对象**之后**的 BindingResult 或 Errors 对象中
    
     *  @Valid：可修饰方法、参数、Bean 属性，对参数进行**嵌套验证**时须在相应 Bean 属性（字段）加上 @Valid
     *  @Validated：可修饰类、方法、参数，不可修饰 Bean 属性，但支持**分组校验**（如果要开启对 Bean 中方法参数或返回值验证，@Validated 应该加在类上，而不是方法参数上）
 *  默认情况下，校验错误导致 MethodArgumentNotValidException 被转换为 400（`BAD_REQUEST`）响应
 *  通过实现 javax.validation.ConstraintValidator 完成自定义校验注解
 *  JSR 303 注解
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414400232.png) 
    图 3 JSR 303注解

 *  Hibernate Validator 扩展的注解
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414401113.png) 
    图 4 HibernateValidator 扩展的注解

# URI 链接 #

 *  构造 URI：UriComponentsBuilder

``````````java
// https://example.com/hotel%20list/New%20York?q=foo%2Bbar 
  URI uri = UriComponentsBuilder.fromHttpUrl("https://example.com/hotel list/{hotel}") 
      .queryParam("q", "{q}") 
      .encode() // 编码，UriComponentsBuilder#encode() 
      .buildAndExpand("New York", "foo+bar") // 扩展 URI 变量，返回 UriComponents 
      .toUri(); // 获取 URI 
  
  URI uri = UriComponentsBuilder.fromHttpUrl("https://example.com/hotel list/{hotel}") 
      .queryParam("q", "{q}") 
      .build("New York", "foo+bar"); 
  
  URI uri = UriComponentsBuilder.fromHttpUrl("https://example.com/hotel list/{hotel}?q={q}") 
      .build("New York", "foo+bar");
``````````

 *  构造相对 Servlet 请求的 URI：ServletUriComponentsBuilder

``````````java
// 构造相对于当前请求的 URI 
  URI uri = ServletUriComponentsBuilder.fromRequest(request) 
          .replaceQueryParam("accountId", "{id}").build() 
          .expand("123") 
          .encode() // UriComponents#encode() 
          .toUri(); 
  // 构造相对于上下文路径的 URI 
  URI uri = ServletUriComponentsBuilder.fromContextPath(request) 
          .path("/accounts").build().toUri(); 
  // 构造相对于 Servlet 的 URI 
  URI uri = ServletUriComponentsBuilder.fromServletMapping(request) 
      .path("/accounts").build().toUri();
``````````

 *  构造指向 Controller 的 URI ：MvcUriComponentsBuilder

``````````java
URI uri = MvcUriComponentsBuilder 
      .fromMethodName(BookingController.class, "getBooking", 21).buildAndExpand(42) 
      .encode().toUri(); 
  URI uri = MvcUriComponentsBuilder.fromMethodCall(on(BookingController.class) 
          .getBooking(21)).buildAndExpand(42).encode().toUri(); 
  
  @Controller 
  @RequestMapping("/hotels/{hotel}") 
  public class BookingController { 
  
      @GetMapping("/bookings/{booking}") 
      public String getBooking(@PathVariable Long booking) { 
          // ... 
      } 
  }
``````````

 *  URI 编码
    
     *  `UriComponentsBuilder#encode()`：首先对 URI 模板进行预编码，然后在扩展时**严格**对 URI 变量进行编码，即还会**替换出现在 URI 变量中的具有保留含义的字符**，如 URI 变量中的 ":" 替换为 "%3A"
     *  `UriComponents#encode()`：扩展 URI 变量后，对 URI 组件进行编码（不会替换出现在 URI 变量中的具有保留含义的字符）
     *  编码规则如下：在 URI 组件中，将**百分比编码**应用到所有非法字符，包括 non-US-ASCII 字符，以及在 RFC 3986 中定义的 URI 组件内的特殊字符，即将需要转换的内容使用 UTF-8 编码后，再使用十六进制表示法转换，并在之前加上 `%` 开头

# 文件上传 #

 *  依赖的 jar 包：commons-fileupload.jar
 *  在 XML 中配置文件上传解析器 MultipartResolver（默认没有装配）

``````````xml
<!-- 配置文件上传解析器，其 id 固定 --> 
  <bean id="multipartResolver" 
      class="org.springframework.web.multipart.commons.CommonsMultipartResolver"> 
      <!-- 上传文件大小上限，单位为字节（3 MB）--> 
      <property name="maxUploadSize" value="#{3*1024*1024}"/> 
  </bean>
``````````

 *  在 Controller 方法中（可在 @PostMapping 中指定 consumes = "multipart/form-data"），通过 MultipartFile file 来接收上传的文件，通过 MultipartFile\[\] files 接收多个文件上传
 *  MultipartFile 接口`boolean isEmpty()`：是否有上传的文件`String getName()`：获取表单中文件上传组件的名字`String getContentType()`：获取文件 MIME 类型，如 image/jpeg 等`String getOriginalFilename()`：获取上传文件的原名`long getSize()`：获取文件的字节大小，单位为 byte`InputStream getInputStream()`：获取文件流`void transferTo(File dest)`：将上传文件保存到一个目标文件中

# 文件下载 #

 *  `new ResponseEntity(T body, HttpHeaders headers, HttpStatus status)`

``````````java
// http://localhost/files/first.txt
@GetMapping("/files/{filename:.+}")
@ResponseBody
public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
    Resource file = ...;
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
}
``````````

# 拦截器 #

 *  自定义拦截器继承 HandlerInterceptorAdapter 抽象类
 *  重写拦截方法
    
     *  `boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)`：该方法将在请求处理之前被调用，当返回值为 false 时，表示表示拦截，请求结束；当返回值为 true 时，表示放行
     *  `void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)`
     *  `void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)`
 *  在 mvc.xml 文件中配置拦截器

``````````java
<mvc:interceptors>
    <mvc:interceptor>
        <!-- /* 只能拦截一级路径；/** 可以拦截一级或多级路径 -->
        <mvc:mapping path="/**"/>
        <mvc:exclude-mapping path="/login.do"/>
        <bean class="自定义拦截器的类名"/>
    </mvc:interceptor>
</mvc:interceptors>
``````````

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414401719.png) 
图 5 过滤器和拦截器的执行顺序

# 请求前后增强处理 #

 *  RequestBodyAdvice，用于对带有 @RequestBody 注解的 Controller 方法，在**读取请求 body 之前**或者**在 body 转换成对象之前**做相应的增强
 *  ResponseBodyAdvice，用于对使用 @ResponseBody 修饰的 Controller 方法，在**响应体写出之前**做相应的增强
 *  需加上 @ControllerAdvice 注解

``````````java
@Slf4j
@ControllerAdvice
public class LogRequestBodyAdvice implements RequestBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        Method method = methodParameter.getMethod();
        log.info("{}.{}:{}", method.getDeclaringClass().getSimpleName(), method.getName(), JSON.toJSONString(body));
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        Method method = methodParameter.getMethod();
        log.info("{}.{}", method.getDeclaringClass().getSimpleName(), method.getName());
        return body;
    }
}
``````````

``````````java
@Slf4j
@ControllerAdvice
public class LogResponseBodyAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Method method = methodParameter.getMethod();
        String url = serverHttpRequest.getURI().toASCIIString();
        log.info("{}.{}, url={}, result={}", method.getDeclaringClass().getSimpleName(), method.getName(), url, JSON.toJSONString(body));
        return body;
    }
}
``````````

# 异常统一处理 #

WebApplicationContext 中声明的 HandlerExceptionResolver bean 用来解析处理请求时抛出的异常，其实现类：SimpleMappingExceptionResolver、DefaultHandlerExceptionResolver、ResponseStatusExceptionResolver、ExceptionHandlerExceptionResolver

## 使用 XML 文件配置 ##

``````````xml
<!-- 配置简单异常处理器 -->
<bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
    <!-- 定义出现异常时默认跳转的视图 -->
    <property name="defaultErrorView" value="common/error"/>
    <!-- 定义异常的变量名，默认名为 exception -->
    <property name="exceptionAttribute" value="ex"/>
    <!-- 定义需要特殊处理的异常，用类名或完全路径名作为 key，异常跳转视图作为值 -->
    <property name="exceptionMappings">
        <value>
            com.example.wms.exception.SecurityException=common/nopermission
            <!-- 这里还可以继续扩展不同异常类型的异常处理 -->
        </value>
    </property>
</bean>
``````````

## 使用统一的异常处理类 ##

 *  使用 @ControllerAdvice 或 @RestControllerAdvice 定义统一的异常处理类，用来拦截 Controller 的方法
 *  使用 @ExceptionHandler(value = Exception.class) 指定该方法处理的异常类型
 *  使用 @ResponseStatus(HttpStatus.xxx) 指定该方法返回的状态码

``````````java
/**
 * SpringMVC 自定义异常对应的 status code
 *            Exception                       HTTP Status Code
 * ConversionNotSupportedException         500 (Internal Server Error)
 * HttpMessageNotWritableException         500 (Internal Server Error)
 * HttpMediaTypeNotSupportedException      415 (Unsupported Media Type)
 * HttpMediaTypeNotAcceptableException     406 (Not Acceptable)
 * HttpRequestMethodNotSupportedException  405 (Method Not Allowed)
 * NoSuchRequestHandlingMethodException    404 (Not Found)
 * TypeMismatchException                   400 (Bad Request)
 * HttpMessageNotReadableException         400 (Bad Request)
 * MissingServletRequestParameterException 400 (Bad Request)
*/

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handlerAuthorizationException(HandlerMethod method, HttpServletResponse response, AuthorizationException exception) throws Exception {
        if (method.getMethod().isAnnotationPresent(ResponseBody.class)) {
            // ajax 请求
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(new ObjectMapper().writeValueAsString(new JsonResult().mark(("没有权限！"))));
        } else {
            // 资源访问
            response.sendRedirect("/nopermission.jsp");
        }
    }
}
``````````


[Spring_MVC_Table_of_Contents]: https://static.sitestack.cn/projects/sdky-java-note/eebff17b17cfcf1133d39fc9256b6ba8.png
[Spring_MVC]: https://static.sitestack.cn/projects/sdky-java-note/24b81a1b06b6564f8753b8473baeb5a3.png
[JSR 303]: https://static.sitestack.cn/projects/sdky-java-note/d50ac49f4047fc9f19ae6fa3545ed7b0.png
[HibernateValidator]: https://static.sitestack.cn/projects/sdky-java-note/4f10675b74a0b40771d695c69dabf2a7.png
[49e43ead3755428a15da7019dd0485b4.png]: https://static.sitestack.cn/projects/sdky-java-note/49e43ead3755428a15da7019dd0485b4.png