## 什么是跨域

CORS全称Cross-Origin Resource Sharing，意为跨域资源共享。当一个资源去访问另一个不同域名或者同域名不同端口的资源时，就会发出跨域请求。如果此时另一个资源不允许其进行跨域资源访问，那么访问就会遇到跨域问题。

跨域指的是由于浏览器的安全性限制，不允许前端页面访问协议不同、域名不同、端口号不同的http接口，例如我本地创建一个html，里面写一个ajax请求访问我服务器SpringBoot应用提供的接口：`192.168.1.11:8080/getUser`
则会出报 `No 'Access-Control-Allow-Origin' header is present on the requested resource. `错误。

## SpringBoot怎么解决跨域

> 在springboot中可以采用多种方式解决跨域问题，例如：可以在类或方法上添加`@CrossOrigin` 注解。还有一种就是全局配置，全局配置需要添加自定义类实现 `WebMvcConfigurer` 接口，然后实现接口中的 `addCorsMappings` 方法。

* addMapping：表示对哪种格式的请求路径进行跨域处理。
* allowedHeaders：表示允许的请求头，默认允许所有的请求头信息。
* allowedMethods：表示允许的请求方法，默认是 GET、POST 和 HEAD。这里配置为 * 表示支持所有的请求方法。
* maxAge：表示探测请求的有效期
* allowedOrigins 表示支持的域

~~~java
package com.maple.rest.aop;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域拦截.
 *
 * @author 笑小枫
 * @date 2021-11-03 13:23:08
 * @since JDK 1.8
 */
@WebFilter(filterName = "corsFilter", urlPatterns = "/*")
@Order(0)
public class CorsFilter implements Filter {

    private static final String HEADER_ORIGIN = "Origin";
    private static final String METHOD_OPTIONS = "OPTIONS";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        if (request.getHeader(HEADER_ORIGIN) != null) {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HEADER_ORIGIN));
        }

        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        // 如果允许所有请求方式，用*
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
         // 如果允许所有header，用*
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Authorization, Content-Type, Accept, X-Requested-With, remember-me");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");

        if (METHOD_OPTIONS.equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        chain.doFilter(req, res);
    }
}
~~~

## 关于笑小枫

> 本章到这里结束了，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
> 老规矩，懂了就点赞收藏；不懂就问，日常在线，我会就会回复哈~
> 后续文章会陆续更新，文档会同步在微信公众号、个人博客、CSDN和GitHub保持同步更新。
> 微信公众号：笑小枫
> 笑小枫个人博客：[https://www.xiaoxiaofeng.site](https://www.xiaoxiaofeng.site)
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)
> GitHub文档：[https://github.com/hack-feng/Java-Notes](https://github.com/hack-feng/Java-Notes) 
