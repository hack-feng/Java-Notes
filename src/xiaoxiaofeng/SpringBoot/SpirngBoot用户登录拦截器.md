[toc]

## 什么是JWT
什么是JWT,JWT(全称：Json Web Token)是一个开放标准(RFC 7519)，它定义了一种紧凑的、自包含的方式，用于作为JSON对象在各方之间安全地传输信息。 该信息可以被验证和信任，因为它是数字签名的。

上面说法比较文绉绉，简单点说就是一种认证机制，让后台知道该请求是来自于受信的客户端。

### JWT的优点
* json格式的通用性，所以JWT可以跨语言支持，比如Java、JavaScript、PHP、Node等等。
* 可以利用Payload存储一些非敏感的信息。
* 便于传输，JWT结构简单，字节占用小。
* 不需要在服务端保存会话信息，易于应用的扩展。

### JWT的缺点
* 安全性没法保证，所以jwt里不能存储敏感数据。因为jwt的payload并没有加密，只是用Base64编码而已。
* 无法中途废弃。因为一旦签发了一个jwt，在到期之前始终都是有效的，如果用户信息发生更新了，只能等旧的jwt过期后重新签发新的jwt。
* 续签问题。当签发的jwt保存在客户端，客户端一直在操作页面，按道理应该一直为客户端续长有效时间，否则当jwt有效期到了就会导致用户需要重新登录。

### 对于JWT的缺点，应该怎么处理？
* 针对JWT的缺点，我们在使用的过程中，只储存常用的无敏感数据，比如用户ID，用户角色等。
* 中途废弃和续签问题，通过和redis配合使用，将token返回时，同步保存redis，通过控制token在redis的有效期来进行控制。
* 还可以通过统计redis有效数据，对在线用户进行统计或强制下线等操作。

## 使用JWT判断用户登录流程

以用户登录功能为例，程序流程如下：

**用户登录**

![img](http://file.xiaoxiaofeng.site/blog/image/2022/06/30/20220630143121.jpeg)

**token认证访问**

![img](http://file.xiaoxiaofeng.site/blog/image/2022/06/30/20220630143121.jpeg)

注：系统中采用JWT对用户登录授权验证。

**基于Token的身份验证流程**

使用基于Token的身份验证，在服务端不需要存储用户的登录记录。大概的流程是这样的:

1、客户端使用用户名或密码请求登录；

2、服务端收到请求，去验证用户名与密码；

3、验证成功后，服务端会使用JWT签发一个Token，保存到Redis中，同时再把这个Token发送给客户端；

4、客户端收到Token以后可以把它存储起来，比如放在Cookie里或者Local Storage里；

5、客户端每次向服务端请求资源的时候需要在请求Header里面带着服务端签发的Token；

6、服务端收到请求，然后去验证客户端请求里面带着的Token，如果验证成功，就向客户端返回请求的数据。验证失败，返回失败原因。


## 功能实现

自动生成的`User.java`、`UserRole.java`、`UserMapper.java`、`UserRoleMapper.java`、`UserMapper.xml`...就不贴代码了，没有业务代码，且占的篇幅过大。

Redis相关参考[SpringBoot集成Redis]()一文

### 涉及到的表sql

~~~sql
CREATE TABLE `usc_user`
(
    `id`          BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `open_id`     VARCHAR(255) NULL DEFAULT NULL COMMENT '小程序的openId',
    `dept_id`     BIGINT(20) NULL DEFAULT NULL COMMENT '部门ID',
    `account`     VARCHAR(30) NULL DEFAULT NULL COMMENT '用户账号',
    `user_name`   VARCHAR(30) NULL DEFAULT NULL COMMENT '用户姓名',
    `nick_name`   VARCHAR(30) NULL DEFAULT NULL COMMENT '用户昵称',
    `user_type`   VARCHAR(2) NULL DEFAULT '00' COMMENT '用户类型（00系统用户,01小程序用户）',
    `email`       VARCHAR(50) NULL DEFAULT '' COMMENT '用户邮箱',
    `phone`       VARCHAR(11) NULL DEFAULT '' COMMENT '手机号码',
    `sex`         CHAR(1) NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
    `avatar`      VARCHAR(100) NULL DEFAULT '' COMMENT '头像地址',
    `salt`        VARCHAR(32) NULL DEFAULT NULL COMMENT '用户加密盐值',
    `password`    VARCHAR(100) NULL DEFAULT '' COMMENT '密码',
    `status`      CHAR(1) NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
    `login_ip`    VARCHAR(128) NULL DEFAULT '' COMMENT '最后登录IP',
    `login_date`  DATETIME NULL DEFAULT NULL COMMENT '最后登录时间',
    `create_id`   BIGINT(20) NULL DEFAULT NULL COMMENT '创建人id',
    `create_name` VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
    `update_id`   BIGINT(20) NULL DEFAULT NULL COMMENT '修改人id',
    `update_name` VARCHAR(64) NULL DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
    `is_deleted`  TINYINT(1) NULL DEFAULT '0' COMMENT '删除标志',
    `remark`      VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UNI_USER_NAME` (`user_name`) USING BTREE
) COMMENT='用户中心-用户信息表' ENGINE=InnoDB ROW_FORMAT=DYNAMIC AUTO_INCREMENT=1;
~~~

~~~sql
CREATE TABLE `usc_user_role`
(
    `id`      BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UNI_USER_ROLE` (`user_id`, `role_id`) USING BTREE
) COMMENT='用户中心-用户和角色关联表' ENGINE=InnoDB ROW_FORMAT=DYNAMIC AUTO_INCREMENT=1;
~~~

### JWT工具类

```java
package com.maple.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.maple.common.config.GlobalConfig;
import com.maple.common.config.exception.ErrorCode;
import com.maple.common.config.exception.MapleCheckException;
import com.maple.common.model.TokenBean;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Jwt常用操作
 *
 * @author 笑小枫
 * @date 2021/12/23
 */
public class JwtUtil {

    private static final String ACCOUNT = "account";
    private static final String USER_ID = "userId";
    private static final String USER_TYPE = "userType";
    private static final String ROLE_LIST = "roleList";
    private static final String DEP_ID = "depId";

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token, String account) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(GlobalConfig.SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withClaim(ACCOUNT, account).build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户登录帐号
     */
    public static String getAccount() {
        try {
            DecodedJWT jwt = getJwt();
            if (jwt == null) {
                return null;
            }
            return jwt.getClaim(ACCOUNT).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户登录帐号
     */
    public static String getAccount(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(ACCOUNT).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static Long getUserId() {
        try {
            DecodedJWT jwt = getJwt();
            if (jwt == null) {
                return null;
            }
            return jwt.getClaim(USER_ID).asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户ID
     */
    public static Long getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(USER_ID).asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static TokenBean getTokenMsg() {
        TokenBean tokenBean = new TokenBean();
        try {
            DecodedJWT jwt = getJwt();
            if (jwt == null) {
                return tokenBean;
            }
            tokenBean.setUserId(jwt.getClaim(USER_ID).asLong());
            tokenBean.setAccount(jwt.getClaim(ACCOUNT).asString());
            return tokenBean;

        } catch (JWTDecodeException e) {
            return tokenBean;
        }
    }

    private static DecodedJWT getJwt() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(servletRequestAttributes)) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR);
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();

        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            return null;
        }
        return JWT.decode(authorization);
    }

    /**
     * 通过Token解析出roles
     *
     * @param token token
     * @return 角色信息
     */
    public static String getRolesByToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(ROLE_LIST).asString();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取token中的role list
     *
     * @return 角色信息
     */
    public static List<String> getRoleList() {

        DecodedJWT jwt = getJwt();
        if (jwt == null) {
            return new ArrayList<>();
        }
        String roleString = jwt.getClaim(ROLE_LIST).asString();
        if (!StringUtils.isEmpty(roleString)) {
            return Arrays.asList(roleString.split(","));
        }

        return new ArrayList<>();
    }

    /**
     * 校验token是否有效
     *
     * @param token token信息
     * @return 返回结果
     */
    public static boolean verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(GlobalConfig.SECRET)).build();
            DecodedJWT jwt = verifier.verify(token);
            jwt.getClaims();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建token
     *
     * @param tokenBean token保存的信息
     * @return token
     */
    public static String createToken(TokenBean tokenBean) {
        Algorithm algorithm = Algorithm.HMAC256(GlobalConfig.SECRET);
        return JWT.create().withClaim(USER_ID, tokenBean.getUserId()).withClaim(ACCOUNT, tokenBean.getAccount()).withClaim(USER_TYPE, tokenBean.getUserType()).withClaim(ROLE_LIST, tokenBean.getRoleList()).withClaim(DEP_ID, tokenBean.getDeptId()).sign(algorithm);
    }
}
```

### Filter登录拦截器

~~~java
package com.maple.rest.aop;

import com.alibaba.fastjson.JSON;
import com.maple.common.config.GlobalConfig;
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
 * @author 笑小枫
 * @date 2022/06/29
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
            String authorization = httpServletRequest.getHeader("Authorization");
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

### 用户登录接口

#### model和param

登录请求对象`LoginQuery.java`

```java
package com.maple.user.vo.query;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author 笑小枫
 * @date 2021/12/9
 */
@Data
@ApiModel(value = "用户登录请求对象", description = "用户中心-用户登录请求对象")
public class LoginQuery {

    @ApiModelProperty(value = "登录账号")
    private String account;
    
    @ApiModelProperty(value = "登录密码")
    private String password;
}
```

返回的用户信息对象`UserModel.java`

```java
package com.maple.user.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 用户中心-用户信息
 *
 * @author 笑小枫
 * @date 2021/12/23
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户视图对象", description = "用户中心-用户信息")
public class UserModel {

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户类型")
    private String userType;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "用户性别")
    private String sex;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "帐号状态")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "用户验证Token")
    private String token;

    @ApiModelProperty(value = "用户角色Id")
    private List<Long> roleIds;
}
```

通用类`GlobalConfig.java`

```java
package com.maple.common.config;

/**
 * 全局配置
 *
 * @author 笑小枫
 */
public class GlobalConfig {

    private GlobalConfig() {

    }

    /**
     * 用户储存在redis中的过期时间
     */
    public static final long EXPIRE_TIME = 60 * 60 * 12L;

    /**
     * 生成token的私钥
     */
    public static final String SECRET = "maple1223";

    /**
     * 用户登录token保存在redis的key值
     *
     * @param account 用户登录帐号
     * @return token保存在redis的key
     */
    public static String getRedisUserKey(String account) {
        return "MAPLE_ADMIN:" + account;
    }
}
```

#### controller

```java
package com.maple.rest.controller.manage;

import com.maple.user.vo.query.LoginQuery;
import com.maple.user.vo.model.UserInfo;
import com.maple.common.util.JwtUtil;
import com.maple.user.vo.model.UserModel;
import com.maple.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

/**
 * 系统登录
 *
 * @author 笑小枫
 * @date 2021/12/8
 */
@Api(tags = "管理系统-系统登录操作")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/sso")
public class LoginController {

    private final IUserService userService;

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public UserModel login(@RequestBody LoginQuery req) {
        return userService.login(req);
    }

    @ApiOperation(value = "用户退出登录")
    @GetMapping("/logout")
    public void logout() {
        userService.logout();
    }
}
```

#### service

```java
package com.maple.user.service;

import com.maple.user.vo.model.UserModel;
import com.maple.user.vo.query.LoginQuery;

/**
 * <p>
 * 用户中心-用户信息表 服务类
 * </p>
 *
 * @author Maple
 * @since 2021-12-07
 */
public interface IUserService {

    /**
     * 用户登录
     *
     * @param req 用户信息
     * @return 用户登录信息
     */
    UserModel login(LoginQuery req);

    /**
     * 退出系统，清除用户token
     */
    void logout();
}
```

#### serviceImpl

```java
package com.maple.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.config.GlobalConfig;
import com.maple.common.config.exception.ErrorCode;
import com.maple.common.config.exception.MapleCheckException;
import com.maple.common.config.exception.MapleCommonException;
import com.maple.common.model.TokenBean;
import com.maple.common.util.JwtUtil;
import com.maple.common.util.Md5Util;
import com.maple.common.util.RedisUtil;
import com.maple.user.bean.User;
import com.maple.user.bean.UserRole;
import com.maple.user.mapper.UserMapper;
import com.maple.user.mapper.UserRoleMapper;
import com.maple.user.service.IUserService;
import com.maple.user.vo.model.UserModel;
import com.maple.user.vo.query.LoginQuery;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户中心-用户信息表 服务实现类
 * </p>
 *
 * @author Maple
 * @since 2021-12-07
 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

    private final RedisUtil redisUtil;

    @Override
    public UserModel login(LoginQuery req) {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getAccount, req.getAccount())
                .eq(User::getUserType, "00")
                .last("LIMIT 1"));
        if (Objects.isNull(user)) {
            throw new MapleCheckException(ErrorCode.USER_LOGIN_ERROR);
        }
        if ("1".equals(user.getStatus())) {
            throw new MapleCheckException(ErrorCode.USER_STATUS_ERROR);
        }
        if (!Md5Util.verifyPassword(req.getPassword(), user.getPassword(), user.getUserName())) {
            throw new MapleCheckException(ErrorCode.USER_LOGIN_ERROR);
        }

        List<Long> userRoles = userRoleMapper.selectList(Wrappers.lambdaQuery(UserRole.class)
                .eq(UserRole::getUserId, user.getId()))
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());
        TokenBean tokenBean = TokenBean.builder()
                .userId(user.getId())
                .userType(user.getUserType())
                .account(user.getUserName())
                .deptId(user.getDeptId())
                .roleList(StringUtils.join(userRoles, ","))
                .build();

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user, userModel);
        String token;
        try {
            token = JwtUtil.createToken(tokenBean);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MapleCommonException(ErrorCode.COMMON_ERROR);
        }
        userModel.setToken(token);
        redisUtil.set(GlobalConfig.getRedisUserKey(user.getAccount()), token);
        return userModel;
    }

    @Override
    public void logout() {
        redisUtil.remove(GlobalConfig.getRedisUserKey(JwtUtil.getAccount()));
    }
}
```

### 使用JWT的用户信息

直接使用`JwtUtil.class`工具类里的方法，即可拿到对应的数据

~~~
JwtUtil.getUserId();
JwtUtil.getAccount();
~~~

## 功能测试

直接在`LoginController.java`里面添加`getUserId`方法进行测试，详细代码如下：

~~~java
package com.maple.rest.controller.manage;

import com.maple.common.util.JwtUtil;
import com.maple.user.service.IMenuService;
import com.maple.user.service.IUserService;
import com.maple.user.vo.model.UserModel;
import com.maple.user.vo.query.LoginQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 系统登录
 *
 * @author 笑小枫
 * @date 2021/12/8
 */
@Api(tags = "管理系统-系统登录操作")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/sso")
public class LoginController {

    private final IUserService userService;

    private final IMenuService menuService;

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public UserModel login(@RequestBody LoginQuery req) {
        return userService.login(req);
    }

    @ApiOperation(value = "用户退出登录")
    @GetMapping("/logout")
    public void logout() {
        userService.logout();
    }

    @ApiOperation(value = "获取登录用户信息")
    @GetMapping("/getUserId")
    public String getUserId() {
        return "当前登录用户的ID为" + JwtUtil.getUserId();
    }
}

~~~
在未登录状态请求接口，返回信息如下：
![image-20220630155943091](http://file.xiaoxiaofeng.site/blog/image/2022/06/30/20220630155943.png)

调用登录接口，进行用户登录
![image-20220630160212694](http://file.xiaoxiaofeng.site/blog/image/2022/06/30/20220630160213.png)

登录后，拿到token，在请求头设置`Authorization`参数后，再次调用，返回信息如下：
![image-20220630160319904](http://file.xiaoxiaofeng.site/blog/image/2022/06/30/20220630160320.png)

本文代码如果直接复制，可能会缺失一些类，因为小编直接摘选的了项目中在用的功能，没有精力再次重头整理，但整体逻辑思想没有影响。

后续整个SpringBoot系列完善后，会把整个系列的代码进行开源，大家可以下载进行参考。

后续的文章更加精彩，使用SpringBoot从0搭建整站，我会一直更新下去的。

## 关于笑小枫

> 本章到这里结束了，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
> 老规矩，懂了就点赞收藏；不懂就问，日常在线，我会就会回复哈~
> 后续文章会陆续更新，文档会同步在微信公众号、个人博客、CSDN和GitHub保持同步更新。
> 微信公众号：笑小枫
> 笑小枫个人博客：[http://www.xiaoxiaofeng.site](http://www.xiaoxiaofeng.site)
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)
> GitHub文档：[https://github.com/hack-feng/Java-Notes](https://github.com/hack-feng/Java-Notes) 
