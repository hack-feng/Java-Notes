@[TOC]

# 笑小枫系列-SpringBoot返回结果包装

## 为什么要返回统一格式后的结果

> 前后端分离的时代，如果没有统一的返回格式，给前端的结果各式各样，估计前端的小伙伴就要骂娘了。  
我们想对自定义异常抛出指定的状态码排查错误，对系统的不可预知的异常抛出友好一点的异常信息。  
我们想让接口统一返回一些额外的数据，例如接口执行的时间等等。  
...  
所以嘛，不管是日常和前端小伙伴对接，还是和其他部门进行接口对接，都应该返回固定的格式。  
本文给出一个简单通用的返回格式，小伙伴们有需求，可以基于此版本根据自己的业务需求丰富返回格式。

返回数据格式如下，有兴趣的小伙伴们可以继续往下看SpringBoot是怎么来实现的。

~~~json
{
  "status": true,
  "code": "0000",
  "msg": "",
  "data": {
    "id": 1,
    "deptId": 103,
    "userName": "admin",
    "nickName": "笑小枫",
    "userType": "00",
    "email": "xxf@163.com",
    "phone": "15888888888",
    "status": "0",
    "remark": "管理员"
  }
}
~~~

## @RestControllerAdvice简单介绍

> 首先了解一下SpringBoot的`@RestControllerAdvice`注解，它是Spring框架3.2新增的的注解  
点进去这个注解源码，可以看到它是由`@ControllerAdvice`和`@ResponseBody`的组合注解  
它通常用来定义`@ExceptionHandler`， `@InitBinder`以及`@ModelAttribute`适用于所有方法`@RequestMapping`的方法。  
直白点说，这就是一个增强Controller的注解。主要实现以下三个方面的功能

1. 全局异常处理
2. 全局数据预处理
3. 全局数据绑定

我们看一下`@RestControllerAdvice`注解的源码

~~~java

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ControllerAdvice
@ResponseBody
public @interface RestControllerAdvice {
    @AliasFor(
            annotation = ControllerAdvice.class
    )
    String[] value() default {};

    @AliasFor(
            annotation = ControllerAdvice.class
    )
    String[] basePackages() default {};

    @AliasFor(
            annotation = ControllerAdvice.class
    )
    Class<?>[] basePackageClasses() default {};

    @AliasFor(
            annotation = ControllerAdvice.class
    )
    Class<?>[] assignableTypes() default {};

    @AliasFor(
            annotation = ControllerAdvice.class
    )
    Class<? extends Annotation>[] annotations() default {};
}
~~~

接下来说一下使用`@RestControllerAdvice`常做的两个功能的实现

1. 返回统一格式的结果
2. 异常统一处理

## SpringBoot怎么返回统一格式的结果

首先创建一个测试的Controller，代码如下：

~~~java
package com.maple.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 笑小枫
 * @date 2021/12/7
 */
@RestController
@RequiredArgsConstructor
public class TestResultController {

    @GetMapping("/testResult")
    public Test testResult() {
        Test test = new Test();
        test.setName("笑小枫");
        test.setAge(18);
        test.setRemark("大家好，我是笑小枫，喜欢我的小伙伴点个赞");
        return test;
    }

    @Data
    static class Test {
        private String name;

        private Integer age;

        private String remark;
    }
}
~~~

我们先看看这是调用返回的结果

~~~json
{
  "name": "笑小枫",
  "age": 18,
  "remark": "大家好，我是笑小枫，喜欢我的小伙伴点个赞"
}
~~~

然后定义一个统一的返回格式类，代码如下：

~~~java
package com.maple.model;

import lombok.Data;

/**
 * 统一返回信息包装类
 *
 * @author 笑小枫
 * @date 2021/12/8
 */
@Data
public class ResultJson {

    private static final String SUCCESS_CODE = "0000";

    /**
     * 成功失败的状态值，true：成功；false：失败
     * 这里返回编码为：“0000”，系统就认为接口成功；其他值，代表失败
     */
    private Boolean status;

    /**
     * 状态码 正确为0000
     */
    private String code;

    /**
     * 返回提示信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private Object data;

    public ResultJson() {
        this.status = true;
        this.code = SUCCESS_CODE;
        this.msg = "";
    }

    public ResultJson(Object data) {
        this.status = true;
        this.code = SUCCESS_CODE;
        this.msg = "";
        this.data = data;
    }

    public ResultJson(String code, String msg) {
        this.status = SUCCESS_CODE.equals(code);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 如果返回状态码非0000，且接口状态为成功，请使用这个
     * @param status 接口请求状态
     * @param code 返回code值
     * @param msg 返回消息
     * @param data 返回数据
     */
    public ResultJson(Boolean status, String code, String msg, Object data) {
        this.status = status;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
~~~

最后创建一个配置类，添加`@RestControllerAdvice`注解，代码如下：

~~~java
package com.maple.config;

import com.alibaba.fastjson.JSON;
import com.maple.model.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 对返回结果统一进行处理，包括返回结果格式统一包装，返回异常统一处理
 *
 * @author 笑小枫
 * @date 2021/12/8
 */
@Slf4j
@RestControllerAdvice
public class RestResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 返回结果包装统一返回格式
     *
     * @return 包装后的返回结果
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // 指定返回的结果为application/json格式，不指定，String类型转json后返回Content-Type是text/plain;charset=UTF-8
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ResultJson result = new ResultJson(body);
        // 若返回类型是ResultJson，则不进行修改
        if (body == null) {
            if (returnType.getParameterType().isAssignableFrom(String.class)) {
                return JSON.toJSONString(result);
            }
        } else if (body instanceof ResultJson) {
            return body;
        } else if (body instanceof String) {
            return JSON.toJSONString(result);
        }
        return result;
    }
}
~~~

这样就完成了统一结果的返回，我们再看一下返回的结果

~~~json
{
  "status": true,
  "code": "0000",
  "msg": "",
  "data": {
    "name": "笑小枫",
    "age": 18,
    "remark": "大家好，我是笑小枫，喜欢我的小伙伴点个赞"
  }
}
~~~

这样统一返回格式就完成了，对代码没有的侵入，原来的代码该怎么写还是怎么写，是不是很nice。

## SpringBoot异常统一处理

上面我们我们介绍了统一返回格式，如果程序抛异常了，我们是否也可以返回统一的格式呢？  
答案是，当然可以的，不光可以抛出我们想要的格式，还可以对指定的异常类型进行特殊处理   
例如使用@Validated对入参校验的异常，我们自定义的异常等等

首先我们模拟一个异常的场景，代码如下：

~~~java
package com.maple.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 笑小枫
 * @date 2021/12/7
 */
@RestController
@RequiredArgsConstructor
public class TestResultController {

    @GetMapping("/testResultError")
    public Test testResultError() {
        Test test = null;
        test.setName("简单点，抛个空指针吧");
        return test;
    }

    @Data
    static class Test {
        private String name;

        private Integer age;

        private String remark;
    }
}
~~~

我们先看看这是调用返回的结果，可以看到，由于我们对结果进行了统一封装，将错误信息放到了data里面。 但显然，status显示true，后台都空指针了，这显然不是我们想要的结果。

~~~json
{
  "status": true,
  "code": "0000",
  "msg": "",
  "data": {
    "timestamp": "2021-12-22T06:24:19.332+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "",
    "path": "/testResultError"
  }
}
~~~

贴一下控制台错误信息

~~~
笑小枫控制台- 2021-12-22 14:25:38 [http-nio-6666-exec-1] ERROR org.apache.catalina.core.ContainerBase.[Tomcat].[localhost].[/].[dispatcherServlet] - Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.NullPointerException] with root cause
java.lang.NullPointerException: null
	at com.maple.controller.TestResultController.testResultError(TestResultController.java:19)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
    ...
~~~

接下来我们开始表演，创建一个配置类，配置一下对异常拦截处理，代码如下：

~~~java
package com.maple.config;

import com.maple.config.exception.ErrorCode;
import com.maple.model.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常信息统一处理
 * 
 * @author 笑小枫
 * @date 2021/12/20
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * 系统异常.
     *
     * @param e 异常信息
     * @return R
     */
    @ExceptionHandler(Exception.class)
    public ResultJson exception(Exception e) {
        log.error("系统异常信息 ex={}", e.getMessage(), e);
        // 未知异常统一抛出9999
        return new ResultJson(ErrorCode.OTHER_ERROR.getCode(), ErrorCode.OTHER_ERROR.getMsg());
    }
}
~~~

这里使用了一个异常编码的枚举类`ErrorCode.java`，代码如下：

~~~java
package com.maple.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一异常信息枚举类
 *
 * @author 笑小枫
 * @date 2021/12/9
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {

    /**
     * 异常信息
     */
    PARAM_ERROR("9001", "请求参数有误，请重试"),

    /**
     * 抛出此异常码，请重新在ErrorMsg定义msg
     */
    COMMON_ERROR("9998", "该作者太懒，居然没有定义异常原因"),

    OTHER_ERROR("9999", "未知异常");

    private final String code;

    private final String msg;
}
~~~

我们再看一下这个时候返回的结果，显然，这个结果我们就可以接受了。程序中尽量避免出现空指针哈，这里只是模拟>_<....

~~~json
{
  "status": false,
  "code": "9999",
  "msg": "未知异常，请联系笑小枫",
  "data": null
}
~~~

### 自定义异常的处理

刚刚对统一异常做了处理，那像我们自定义的异常怎么处理呢

首先我们创建一个自定义异常，这里建议先创建一个BaseException的自定义异常，然后不同的小类的异常再继承BaseException异常。 这样我们可以直接拦截BaseException异常就可以了。
BaseException异常这里取名为`MapleBaseException.java`，代码如下：

~~~java
package com.maple.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常-Base父类，细化的自定义异常，应该继承此类
 * 统一异常处理时，会根据此异常类型做判断，返回结果时，如果是自定义异常自动解析code和errorMsg返回
 *
 * @author 笑小枫
 * @date 2021/12/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MapleBaseException extends RuntimeException {

    private final String code;

    private final String errorMsg;

    public MapleBaseException(String code, String errorMsg) {
        super(errorMsg);
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public MapleBaseException(ErrorCode code) {
        super(code.getMsg());
        this.code = code.getCode();
        this.errorMsg = code.getMsg();
    }

    public MapleBaseException(ErrorCode code, String errorMsg) {
        super(errorMsg);
        this.code = code.getCode();
        this.errorMsg = errorMsg;
    }
}
~~~

然后再定义一个校验的自定义异常，代码如下：

~~~java
package com.maple.config.exception;

/**
 * 检测结果不一致时，抛出此异常
 *
 * @author 笑小枫
 * @date 2021/12/8
 */
public class MapleCheckException extends MapleBaseException {

    public MapleCheckException(String code, String errorMsg) {
        super(code, errorMsg);
    }

    public MapleCheckException(ErrorCode code) {
        super(code);
    }

    public MapleCheckException(ErrorCode code, String errorMsg) {
        super(code, errorMsg);
    }
}
~~~

调整一下我们的异常拦截配置类，代码如下：

注意Exception异常拦截上面的`@Order(99)`，因为我们自定义异常也属于Exception异常，所以使用Order执行时往后排。

~~~java
package com.maple.config;

import com.maple.config.exception.ErrorCode;
import com.maple.config.exception.MapleBaseException;
import com.maple.model.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常信息统一处理
 * 
 * @author 笑小枫
 * @date 2021/12/20
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * 自定义异常处理
     *
     * @param e 异常信息
     * @return 返回结果
     */
    @ExceptionHandler(MapleBaseException.class)
    public ResultJson mapleBaseException(MapleBaseException e) {
        log.error("自定义异常信息 ex={}", e.getMessage(), e);
        return new ResultJson(e.getCode(), e.getErrorMsg());
    }

    /**
     * 系统异常.
     *
     * @param e 异常信息
     * @return R
     */
    @ExceptionHandler(Exception.class)
    @Order(99)
    public ResultJson exception(Exception e) {
        log.error("系统异常信息 ex={}", e.getMessage(), e);
        // 未知异常统一抛出9999
        return new ResultJson(ErrorCode.OTHER_ERROR.getCode(), ErrorCode.OTHER_ERROR.getMsg());
    }
}
~~~

我们再模拟一下抛出自定义异常，代码如下：

~~~java
package com.maple.controller;

import com.maple.config.exception.ErrorCode;
import com.maple.config.exception.MapleCheckException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 笑小枫
 * @date 2021/12/7
 */
@RestController
@RequiredArgsConstructor
public class TestResultController {

    @GetMapping("/testResultError")
    public Test testResultError() {
        Test test = new Test();
        test.setName("笑小枫");
        if (test.getAge() == null) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR);
        }
        return test;
    }

    @Data
    static class Test {
        private String name;

        private Integer age;

        private String remark;
    }
}
~~~

最后，我们来看一下结果

~~~json
{
    "status": false,
    "code": "9998",
    "msg": "笑小枫太懒，居然没有定义异常原因",
    "data": null
}
~~~

贴一下控制台异常信息
~~~
笑小枫控制台- 2021-12-22 14:38:26 [http-nio-6666-exec-2] ERROR com.maple.config.ExceptionAdvice - 自定义异常信息 ex=笑小枫太懒，居然没有定义异常原因
com.maple.config.exception.MapleCheckException: 笑小枫太懒，居然没有定义异常原因
	at com.maple.controller.TestResultController.testResultError(TestResultController.java:23)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	...
~~~

### @Validated对入参校验的异常处理
上面的说的@Validated对入参校验的异常处理，这里简单贴一下代码，不做详细概述。系统抛出异常格式太丑，这里做了简单优化.

~~~java
package com.maple.config;

import com.maple.config.exception.ErrorCode;
import com.maple.config.exception.MapleBaseException;
import com.maple.model.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 异常信息统一处理
 *
 * @author 笑小枫
 * @date 2021/12/20
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * 自定义异常处理
     *
     * @param e 异常信息
     * @return 返回结果
     */
    @ExceptionHandler(MapleBaseException.class)
    public ResultJson mapleBaseException(MapleBaseException e) {
        log.error("自定义异常信息 ex={}", e.getMessage(), e);
        return new ResultJson(e.getCode(), e.getErrorMsg());
    }

    /**
     * 参数校验异常处理
     *
     * @param e 异常信息
     * @return 返回结果
     */
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public ResultJson validException(MethodArgumentNotValidException e) {
        log.error("参数校验异常信息 ex={}", e.getMessage(), e);
        BindingResult result = e.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p -> {
                FieldError fieldError = (FieldError) p;
                stringBuilder.append(fieldError.getDefaultMessage());
            });
        }
        return new ResultJson(ErrorCode.PARAM_ERROR.getCode(), stringBuilder.toString());
    }

    /**
     * 系统异常.
     *
     * @param e 异常信息
     * @return R
     */
    @ExceptionHandler(Exception.class)
    @Order(99)
    public ResultJson exception(Exception e) {
        log.error("系统异常信息 ex={}", e.getMessage(), e);
        // 未知异常统一抛出9999
        return new ResultJson(ErrorCode.OTHER_ERROR.getCode(), ErrorCode.OTHER_ERROR.getMsg());
    }
}
~~~

## 关于笑小枫


