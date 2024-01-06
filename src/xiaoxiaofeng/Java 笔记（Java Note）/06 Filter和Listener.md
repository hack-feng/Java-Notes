# Filter #

 *  用于对用户请求进行预处理，也可以对 HttpServletResponse 进行后处理
 *  在服务器启动时创建 Filter 实例并执行初始化，创建或销毁顺序由在 web.xml 中配置的  的先后顺序决定
 *  过滤器链：过滤顺序由在 web.xml 中配置的  的先后顺序决定
    

## 创建 Filter 的步骤 ##

1. 创建 Filter 类，实现 javax. servlet.Filter 接口，该接口中的方法

   - `void init(FilterConfig config)`：用于完成 Filter 的初始化
   - `void destroy()`：用于 Filter 销毁前，完成某些资源的回收
   - `void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)`：实现过滤功能，该方法就是对每个请求及响应增加的额外处理

   > doFilter() 方法可实现对用户请求进行预处理，也可实现对服务器响应进行后处理——它们的分界线为是否调用了 `chain.doFilter(request, response);`，执行该方法之前，即对用户请求进行预处理，行该方法之后，即对服务器响应进行后处理

2. 通过注解或在 web.xml 文件中配置 Filter

   - 在 web.xml 文件中配置 Filter
     - `<filter>`，定义 Filter
       - `<filter-name>`，Filter 的名字
       - `<filter-class>`，Filter 的实现类
       - `<init-param>`，为该 Filter 配置参数，子标签 `<param-name>`、`<param-value>`
     - `<filter-mapping>`，定义 Filter 拦截的 URL 地址
       - `<filter-name>`，Filter 的名字
       - `<url-pattern>`，Filter 负责拦截的 URL，可以有多个
       - `<servlet-name>`，对指定的 Servlet 过滤
       - `<dispatcher>`，指定 Filter 所拦截的资源被 Servlet 容器调用的方式，内容可以为：REQUEST、FORWARD、INCLUDE、ERROR、ASYNC 之一（默认情况下，Filter 只会对新的请求做拦截 REQUEST，如果是请求转发，则不会过滤）
   - 使用 @WebFilter 注解，支持的常用属性
     - filterName，指定该 Filter 的名称
     - initParams，用于为该 Filter 配置参数
     - servletNames，该属性值可指定多个 Servlet 的名称，用于指定该 Filter 仅对这几个 Servlet 执行过滤
     - urIPatterns 或 value，指定该 Filter 所拦截的 URL

## 应用 ##

```java
// 1. 请求编码过滤器 org.springframework.web.filter.CharacterEncodingFilter
String encoding = getEncoding();
if (encoding != null) {
    if (isForceRequestEncoding() || request.getCharacterEncoding() == null) {
        request.setCharacterEncoding(encoding);
    }
    if (isForceResponseEncoding()) {
        response.setCharacterEncoding(encoding);
    }
}
filterChain.doFilter(request, response);

// 2. 请求日志过滤器
// org.springframework.web.filter.AbstractRequestLoggingFilter
// ch.qos.logback.classic.helpers.MDCInsertingServletFilter

// 3. 登录验证过滤器 CheckLoginFilter
// 对于需要过滤的资源，放在统一的目录下，如 @WebFilter("/checklogin/*")
if (user == null) {
    resp.sendRedirect("/login.jsp");
    return; 
}

// 4. 屏蔽敏感字过滤器 ContentFilter
// 自定义 MessageFilterRequest 继承 HttpServletRequestWrapper 覆盖 getParameter 方法
MessageFilterRequest req = new MessageFilterRequest((HttpServletRequest) request);

// 5. 做 MVC 框架的前端控制器（处理请求、进行分发）
```

```java
@Getter
@Slf4j
@WebFilter
@Order(OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER - 10000)
public class AccessLoggingFilter extends OncePerRequestFilter {

    private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 1024; // 1 mb
    private int maxPayloadLength = DEFAULT_MAX_PAYLOAD_LENGTH;

    /**
     * 是否不过滤异步请求
     */
    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isFirstRequest = !isAsyncDispatch(request);

        HttpServletRequest requestToUse = request;
        if (isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request, getMaxPayloadLength());
        }

        HttpServletResponse responseToUse = response;
        if (isFirstRequest && !(response instanceof ContentCachingResponseWrapper)) {
            responseToUse = new ContentCachingResponseWrapper(response);
        }

        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        Throwable throwable = null;
        try {
            filterChain.doFilter(requestToUse, responseToUse);
            status = responseToUse.getStatus();
        } catch (Throwable ex) {
            throwable = ex;
            throw ex;
        } finally {
            if (!isAsyncStarted(requestToUse)) {
                Throwable ex = (Throwable) requestToUse.getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);
                if (ex != null) {
                    throwable = ex;
                } else {
                    ex = (Throwable) requestToUse.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE);
                    if (ex != null) {
                        throwable = ex;
                    }
                }
                log.info(createRequestMsg(requestToUse));
                log.info(createResponseMsg(responseToUse, status));
                if (throwable != null) {
                    log.warn(throwable.getMessage(), throwable);
                }
            }
        }
    }

    private String createRequestMsg(HttpServletRequest request) {
        StringBuilder msg = new StringBuilder();
        msg.append("Request [");

        msg.append("uri=").append(request.getRequestURI());

        String queryString = request.getQueryString();
        if (queryString != null) {
            msg.append('?').append(queryString);
        }

        String method = request.getMethod();
        msg.append("; method=").append(method);

        String client = request.getRemoteAddr();
        if (StringUtils.hasLength(client)) {
            msg.append("; client=").append(client);
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            msg.append("; session=").append(session.getId());
        }
        String user = request.getRemoteUser();
        if (user != null) {
            msg.append("; user=").append(user);
        }

        List<String> values = Collections.list(request.getHeaderNames());
        if (values.size() > 0) {
            // msg.append("; headers=").append(new ServletServerHttpRequest(request).getHeaders());
            String headers = values.stream().map(name -> name + ":" + Collections.list(request.getHeaders(name)))
                .collect(Collectors.joining(", "));
            msg.append("; headers={").append(headers).append("}");
        }

        String params = request.getParameterMap().entrySet().stream()
            .map(entry -> entry.getKey() + ":" + Arrays.toString(entry.getValue()))
            .collect(Collectors.joining(", "));
        if (StringUtils.hasLength(params)) {
            msg.append("; params={").append(params).append("}");
        }

        if (HttpMethod.POST.matches(method) || HttpMethod.PATCH.matches(method) || HttpMethod.PUT.matches(method)) {
            ContentCachingRequestWrapper wrapper =
                WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
            if (wrapper != null) {
                byte[] buf = wrapper.getContentAsByteArray();
                String payload = getPayload(buf, wrapper.getCharacterEncoding());
                msg.append("; body=").append(payload);
            }
        }

        msg.append("]");
        return msg.toString();
    }

    private String createResponseMsg(HttpServletResponse response, int status) {
        StringBuilder msg = new StringBuilder();
        msg.append("Response [");

        msg.append("status=").append(status);

        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        String payload = null;
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            try {
                wrapper.copyBodyToResponse();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            payload = getPayload(buf, wrapper.getCharacterEncoding());
        }

        Collection<String> values = response.getHeaderNames();
        if (values.size() > 0) {
            // msg.append("; headers=").append(new ServletServerHttpResponse(response).getHeaders());
            String headers = values.stream().map(name -> name + ":" + response.getHeaders(name))
                    .collect(Collectors.joining(", "));
            msg.append("; headers={").append(headers).append("}");
        }

        msg.append("; body=").append(payload);

        msg.append("]");
        return msg.toString();
    }

    private String getPayload(byte[] buf, String characterEncoding) {
        if (buf.length > 0 && buf.length < getMaxPayloadLength()) {
            try {
                return new String(buf, 0, buf.length, characterEncoding);
            } catch (UnsupportedEncodingException ex) {
                return "[unknown]";
            }
        }
        return null;
    }
}
```



# Listener

- 作用：监听 Web 应用的内部事件

## 使用 Listener 的步骤

1. 定义 Listener 实现类，常用的 Listener 接口有：
   ServletContextListener：用于监听 Web 应用的启动和关闭
   ServletContextAttributeListener：用于监听 application 内属性的改变（被添加、删除、替换）
   ServletRequestListener：用于监听用户请求的初始化和销毁
   ServletRequestAttributeListener：用于监听 request 内属性的改变（被添加、删除、替换）
   HttpSessionListener：用于监听 session 的创建和销毁（可以通过该监听器监听系统的在线用户）
   HttpSessionAttributeListener：用于监听 session 内属性的改变（被添加、删除、替换）
2. 通过注解或在 web.xml 文件中配置 Listemer
   - 使用 @WebListener 修饰 Listener 实现类，无须指定任何属性
   - 在 <listener> 标签中增加子标签 <listener-class>，指定 Listener 的实现类