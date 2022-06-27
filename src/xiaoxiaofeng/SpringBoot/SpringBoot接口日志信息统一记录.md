## 为什么要记录接口日志？

至于为什么，详细看到这里的小伙伴心里都有一个答案吧，我这里简单列一下常用的场景吧🙈

* 用户登录记录统计
* 重要增删改操作留痕

* 需要统计用户的访问次数
* 接口调用情况统计
* 线上问题排查
* 等等等...

既然有这么多使用场景，那我们该怎么处理，总不能一条一条的去记录吧🥶

面试是不是老是被问Spring的Aop的使用场景，那这个典型的场景就来了，我们可以使用Spring的Aop，完美的实现这个功能，接下来上代码😁

## 先定义一下日志存储的对象吧

本文涉及到依赖：

* lombok
* swagger
* mybatisplus

简单如下，可以根据自己的需求进行修改

贴一下建表sql吧

~~~sql
CREATE TABLE `sys_operate_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) DEFAULT '4' COMMENT '业务类型（0查询 1新增 2修改 3删除 4其他）',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `resp_time` bigint(20) DEFAULT NULL COMMENT '响应时间',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `browser` varchar(255) DEFAULT NULL COMMENT '浏览器类型',
  `operate_type` int(1) DEFAULT '3' COMMENT '操作类别（0网站用户 1后台用户 2小程序 3其他）',
  `operate_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `operate_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `operate_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `operate_param` text COMMENT '请求参数',
  `json_result` text COMMENT '返回参数',
  `status` int(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` text COMMENT '错误消息',
  `create_id` bigint(20) DEFAULT NULL COMMENT '操作人id',
  `create_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统管理-操作日志记录';
~~~

使用的mybatis plus的自动生成代码功能生成的对象，真香🤪

~~~java
package com.maple.system.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统管理-操作日志记录
 * </p>
 *
 * @author Maple
 * @since 2021-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_operate_log")
@ApiModel(value="OperateLog对象", description="系统管理-操作日志记录")
public class OperateLog extends Model<OperateLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日志主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "模块标题")
    private String title;

    @ApiModelProperty(value = "业务类型（0查询 1新增 2修改 3删除 4其他）")
    private Integer businessType;

    @ApiModelProperty(value = "方法名称")
    private String method;
    
    @ApiModelProperty(value = "响应时间")
    private Long respTime;

    @ApiModelProperty(value = "请求方式")
    private String requestMethod;

    @ApiModelProperty(value = "操作类别（0网站用户 1后台用户 2小程序 3其他）")
    private Integer operateType;

    @ApiModelProperty(value = "浏览器类型")
    private String browser;

    @ApiModelProperty(value = "请求URL")
    private String operateUrl;

    @ApiModelProperty(value = "主机地址")
    private String operateIp;

    @ApiModelProperty(value = "操作地点")
    private String operateLocation;

    @ApiModelProperty(value = "请求参数")
    private String operateParam;

    @ApiModelProperty(value = "返回参数")
    private String jsonResult;

    @ApiModelProperty(value = "操作状态（0正常 1异常）")
    private Integer status;

    @ApiModelProperty(value = "错误消息")
    private String errorMsg;

    @ApiModelProperty(value = "操作人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createId;

    @ApiModelProperty(value = "操作人员")
    @TableField(fill = FieldFill.INSERT)
    private String createName;

    @ApiModelProperty(value = "操作时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
~~~

mapper代码就不贴了，都是生成的，只用到了mybatis plus的insert方法，下面别再问我为什么少个类了😂

## 定义切点、Aop实现功能

### 定义涉及到枚举类

`业务类型`BusinessTypeEnum枚举类：

~~~java
package com.maple.common.enums;

/**
 * @author zhangfuzeng
 * @date 2022/6/27
 */
public enum BusinessTypeEnum {
    // 0查询 1新增 2修改 3删除 4其他
    SELECT,
    INSERT,
    UPDATE,
    DELETE,
    OTHER
}
~~~

`操作类别OperateTypeEnum`枚举类：

~~~java
package com.maple.common.enums;

/**
 * @author zhangfuzeng
 * @date 2022/6/27
 */
public enum OperateTypeEnum {
    // 0网站用户 1后台用户 2小程序 3其他
    BLOG,
    ADMIN,
    APP,
    OTHER
}
~~~

### 定义切点的注解

定义一个自定义注解`MapleLog.java`，哪些接口需要记录日志就靠它了，命名根据自己的调整哈，我的maple，谁叫我是[笑小枫](http://www.xiaoxiaofeng.site)呢，不要好奇的点这个链接，不然你会发现惊喜😎

~~~java
package com.maple.common.model;

import com.maple.common.enums.BusinessTypeEnum;
import com.maple.common.enums.OperateTypeEnum;

import java.lang.annotation.*;

/**
 * @author Maple
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MapleLog {

    // 0网站用户 1后台用户 2小程序 3其他
    OperateTypeEnum operateType() default OperateTypeEnum.OTHER;

    // 0查询 1新增 2修改 3删除 4其他
    BusinessTypeEnum businessType() default BusinessTypeEnum.SELECT;
    
    // 返回保存结果是否落库，没用的大结果可以不记录，比如分页查询等等，设为false即可
    boolean saveResult() default true;
}
~~~

### Aop实现功能

使用了Aop的环绕通知，其中`JwtUtil`是我系统中存储登录用户用的，根据自己的系统来，没有去掉就OK

`OperateLogMapper`是mybatis plus生成的保存到数据的，根据自己的业务来，不需要入库，可以直接打印log，忽略它🙈

参数和返回结果的值，数据库类型是text，长度不能超过65535，这里截取了65000

~~~java
package com.maple.rest.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.maple.common.model.MapleLog;
import com.maple.common.util.JwtUtil;
import com.maple.system.bean.OperateLog;
import com.maple.system.mapper.OperateLogMapper;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;


/**
 * @author ZhangFZ
 * 配置切面类，@Component 注解把切面类放入Ioc容器中
 */
@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class SystemLogAspect {

    private final OperateLogMapper operateLogMapper;

    @Pointcut(value = "@annotation(com.maple.common.model.MapleLog)")
    public void systemLog() {
    }

    @Around(value = "systemLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = new Object();
        // 定义执行开始时间
        long startTime;
        // 定义执行结束时间
        long endTime;
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 取swagger的描述信息
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        MapleLog mapleLog = method.getAnnotation(MapleLog.class);
        OperateLog operateLog = new OperateLog();

        try {
            operateLog.setBrowser(request.getHeader("USER-AGENT"));
            operateLog.setOperateUrl(request.getRequestURI());
            operateLog.setRequestMethod(request.getMethod());
            operateLog.setMethod(String.valueOf(joinPoint.getSignature()));
            operateLog.setCreateTime(new Date());
            operateLog.setOperateIp(getIpAddress(request));
            // 取JWT的登录信息，无需登录可以忽略
            if (request.getHeader("Authorization") != null) {
                operateLog.setCreateName(JwtUtil.getAccount());
                operateLog.setCreateId(JwtUtil.getUserId());
            }
            String operateParam = JSON.toJSONStringWithDateFormat(joinPoint.getArgs(), "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteMapNullValue);
            if (operateParam.length() > 65000) {
                operateParam = operateParam.substring(0, 65000);
            }
            operateLog.setOperateParam(operateParam);

            if (apiOperation != null) {
                operateLog.setTitle(apiOperation.value() + "");
            }

            if (mapleLog != null) {
                operateLog.setBusinessType(mapleLog.businessType().ordinal());
                operateLog.setOperateType(mapleLog.operateType().ordinal());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        startTime = System.currentTimeMillis();
        try {
            obj = joinPoint.proceed();
            endTime = System.currentTimeMillis();
            operateLog.setRespTime(endTime - startTime);
            operateLog.setStatus(0);
            // 判断是否保存返回结果，列表页可以设为false
            if (mapleLog.saveResult()) {
                String result = JSON.toJSONString(obj);
                if (result.length() > 65000) {
                    result = result.substring(0, 65000);
                }
                operateLog.setJsonResult(result);
            }
        } catch (Exception e) {
            // 记录异常信息
            operateLog.setStatus(1);
            operateLog.setErrorMsg(e.toString());
            throw e;
        } finally {
            endTime = System.currentTimeMillis();
            operateLog.setRespTime(endTime - startTime);
            operateLogMapper.insert(operateLog);
        }
        return obj;
    }

    /**
     * 获取Ip地址
     */
    private static String getIpAddress(HttpServletRequest request) {
        String xip = request.getHeader("X-Real-IP");
        String xFor = request.getHeader("X-Forwarded-For");
        String unknown = "unknown";
        if (StringUtils.isNotEmpty(xFor) && !unknown.equalsIgnoreCase(xFor)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = xFor.indexOf(",");
            if (index != -1) {
                return xFor.substring(0, index);
            } else {
                return xFor;
            }
        }
        xFor = xip;
        if (StringUtils.isNotEmpty(xFor) && !unknown.equalsIgnoreCase(xFor)) {
            return xFor;
        }
        if (StringUtils.isBlank(xFor) || unknown.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xFor) || unknown.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xFor) || unknown.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(xFor) || unknown.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(xFor) || unknown.equalsIgnoreCase(xFor)) {
            xFor = request.getRemoteAddr();
        }
        return xFor;
    }
}
~~~

就这样，简单吧，拿去用吧

## 写个测试类吧

~~~java
package com.maple.rest.controller.example;

import com.maple.common.config.exception.ErrorCode;
import com.maple.common.config.exception.MapleCommonException;
import com.maple.common.enums.BusinessTypeEnum;
import com.maple.common.enums.OperateTypeEnum;
import com.maple.common.model.MapleLog;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangfuzeng
 * @date 2022/6/27
 */
@RestController
@RequestMapping("/example")
public class TestSystemLogController {

    @ApiOperation(value = "测试带参数、有返回结果的get请求")
    @GetMapping("/testGetLog/{id}")
    @MapleLog(businessType = BusinessTypeEnum.OTHER, operateType = OperateTypeEnum.OTHER)
    public Test testGetLog(@PathVariable Integer id) {
        Test test = new Test();
        test.setName("笑小枫");
        test.setAge(18);
        test.setRemark("大家好，我是笑小枫，喜欢我的小伙伴点个赞呗");
        return test;
    }

    @ApiOperation(value = "测试json参数、抛出异常的post请求")
    @PostMapping("/testPostLog")
    @MapleLog(businessType = BusinessTypeEnum.OTHER, operateType = OperateTypeEnum.OTHER, saveResult = false)
    public Test testPostLog(@RequestBody Test param) {
        Test test = new Test();
        test.setName("笑小枫");
        if (test.getAge() == null) {
            // 这里使用了自定义异常，测试可以直接抛出RuntimeException
            throw new MapleCommonException(ErrorCode.COMMON_ERROR);
        }
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

浏览器请求http://localhost:6666/example/testGetLog/1

![image-20220627172120224](http://file.xiaoxiaofeng.site/blog/image/2022/06/27/20220627172120.png)

再模拟一下post异常请求吧：POST http://localhost:6666/example/testPostLog

![image-20220627172936529](http://file.xiaoxiaofeng.site/blog/image/2022/06/27/20220627172936.png)

## 关于笑小枫

> 本章到这里结束了，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
> 老规矩，懂了就点赞收藏；不懂就问，日常在线，我会就会回复哈~
> 后续文章会陆续更新，文档会同步在个人博客、CSDN和GitHub保持同步更新。
> 笑小枫个人博客：[http://www.xiaoxiaofeng.site](http://www.xiaoxiaofeng.site)
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)
> GitHub文档：[https://github.com/hack-feng/Java-Notes](https://github.com/hack-feng/Java-Notes) 