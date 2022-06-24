[TOC]

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
        test.setRemark("大家好，我是笑小枫，喜欢我的小伙伴点个赞呗");
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

### 普通返回版，代码侵入性强，但相对灵活

这样我们可以把结果统一放在ResultJson里面返回，代码如下：

~~~java
package com.maple.rest.controller.example;

import com.maple.common.model.ResultJson;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangfuzeng
 * @date 2021/12/7
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/example")
public class TestResultController {

    @GetMapping("/testResultJson")
    public ResultJson testResultJson() {
        Test test = new Test();
        test.setName("笑小枫");
        test.setAge(18);
        test.setRemark("大家好，我是笑小枫，喜欢我的小伙伴点个赞呗");
        return new ResultJson(test);
    }

    @Data
    static class Test {
        private String name;

        private Integer age;

        private String remark;
    }
}
~~~

我们看一下返回的结果

~~~json
{
  "status": true,
  "code": "0000",
  "msg": "",
  "data": {
    "name": "笑小枫",
    "age": 18,
    "remark": "大家好，我是笑小枫，喜欢我的小伙伴点个赞呗"
  }
}
~~~

## @RestControllerAdvice简单介绍

> 首先了解一下SpringBoot的`@RestControllerAdvice`注解，它是Spring框架3.2新增的的注解  
> 点进去这个注解源码，可以看到它是由`@ControllerAdvice`和`@ResponseBody`的组合注解  
> 它通常用来定义`@ExceptionHandler`， `@InitBinder`以及`@ModelAttribute`适用于所有方法`@RequestMapping`的方法。  
> 直白点说，这就是一个增强Controller的注解。主要实现以下三个方面的功能

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

### RestControllerAdvice切面处理，代码无侵入，全局统一

上述代码虽然实现了功能，但所有的返回结果都要处理，对代码有比较强的侵入，Spring拥有各种切面的支持，让我们看看如何代码无侵入的实现这个功能。

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
    "remark": "大家好，我是笑小枫，喜欢我的小伙伴点个赞呗"
  }
}
~~~

这样统一返回格式就完成了，对代码没有的侵入，原来的代码该怎么写还是怎么写，是不是很nice。

## 关于笑小枫

> 本章到这里结束了，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
> 老规矩，懂了就点赞收藏；不懂就问，日常在线，我会就会回复哈~
> 后续文章会陆续更新，文档会同步在CSDN和GitHub保持同步更新。
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)
> GitHub文档：[https://github.com/hack-feng/Java-Notes](https://github.com/hack-feng/Java-Notes) 



