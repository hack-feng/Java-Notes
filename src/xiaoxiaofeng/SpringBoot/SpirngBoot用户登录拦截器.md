~~~java
package com.maple.rest.aop;

import com.alibaba.fastjson.JSON;
import com.maple.common.config.GlobalConfig;
import com.maple.common.config.MapleConstants;
import com.maple.common.config.exception.ErrorCode;
import com.maple.common.model.ResultJson;
import com.maple.common.util.JwtUtil;
import com.maple.common.util.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 判断用户登录token
 *
 * @author zhangfuzeng
 * @date 2021/12/29
 */
@WebFilter(filterName = "jwtFilter", urlPatterns = {"/*"})
@AllArgsConstructor
public class JwtFilter implements Filter {

    private final List<String> excludedUrlList;

    @Override
    public void init(FilterConfig filterConfig) {
        excludedUrlList.addAll(Arrays.asList(
                "/sso/login",
                "/sso/logout",
                "/blog/*"
        ));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url = ((HttpServletRequest) request).getRequestURI();
        boolean isMatch = false;
        for (String excludedUrl : excludedUrlList) {
            if (Pattern.matches(excludedUrl.replace("*", ".*"), url)) {
                isMatch = true;
                break;
            }
        }
        if (isMatch) {
            chain.doFilter(request, response);
        } else {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            //处理跨域问题，跨域的请求首先会发一个options类型的请求
            if (httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
                chain.doFilter(request, response);
            }
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            RedisUtil redisService = (RedisUtil) factory.getBean("redisUtil");
            String account;
            String authorization = httpServletRequest.getHeader(MapleConstants.TOKEN_NAME);
            // 判断token是否存在，不存在代表未登录
            if (StringUtils.isEmpty(authorization)) {
                writeRsp(httpServletResponse, ErrorCode.NO_TOKEN);
                return;
            } else {
                account = JwtUtil.getAccount(authorization);
                String token = (String) redisService.get(GlobalConfig.getRedisUserKey(account));
                // 判断token是否存在，不存在代表登陆超时
                if (StringUtils.isEmpty(token)) {
                    writeRsp(httpServletResponse, ErrorCode.TOKEN_EXPIRE);
                    return;
                } else {
                    // 判断token是否相等，不相等代表在其他地方登录
                    if (!token.equalsIgnoreCase(authorization)) {
                        writeRsp(httpServletResponse, ErrorCode.TOKEN_EXCHANGE);
                        return;
                    }
                }
            }
            redisService.set(GlobalConfig.getRedisUserKey(account), authorization, GlobalConfig.EXPIRE_TIME);
            chain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private void writeRsp(HttpServletResponse response, ErrorCode errorCode) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setHeader("content-type", "application/json;charset=UTF-8");
        try {
            response.getWriter().println(JSON.toJSON(new ResultJson(errorCode.getCode(), errorCode.getMsg())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

~~~

