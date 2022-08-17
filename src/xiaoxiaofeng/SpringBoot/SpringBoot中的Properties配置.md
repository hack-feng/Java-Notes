随着我们的演示功能越来越多，里面的配置也越来越多，我们怎么更好的来管理这些配置呢？

实际项目中，我们肯定会有开发环境、测试环境、生产环境，我们又该怎么便捷的配置这些环境呢？

我们应该怎么自定义自己的一些配置呢？该怎么使用呢？

带着这些问题，我们一起来探讨一下🧐🧐🧐

## 配置分模块管理

在使用过程中，我们有很多配置，比如mysql配置、redis配置、mybatis-plus、调用第三方的接口配置等等...

我们现在都是放在一个大而全的配置里面的，如果我们想根据功能分为不同的配置文件管理，让配置更加清晰，应该怎么做呢？

我们可以使用`spring.profiles.include: *,*,*`来包含其他的配置

> 注意：创建的配置要用application-开始，例如mysql的，我们可以配置成application-mysql这样。
>
> 包含多个配置，用英文逗号（“,”）分割即可

我们在`resources`目录下创建`application-xxf.yml`文件，代码如下👇

~~~yml
xxf:
  name: 笑小枫-xxf
  site: https://www.xiaoxiaofeng.com
~~~

然后在`application.yml`配置以下代码即可

~~~yml
spring:
  profiles:
    include: xxf
~~~

因为功能单一，且相互依赖，最后统一写测试代码吧

## 配置多个环境

像mysql、redis不同环境的配置肯定不一样，不想每次打包发布前都修改一次配置，应该怎么快速的切换环境呢？

我们可以使用`spring.profiles.active: *`来指定项目启动使用的环境。

我们来创建两个测试的配置

`application-dev.yml`

~~~yml
xxf:
  desc: 这是一个开发的环境
~~~

`application-prod.yml`

~~~yml
xxf:
  desc: 这是一个生产的环境
~~~

然后在`application.yml`配置中使用`spring.profiles.active: *`来指定环境即可。

~~~yml
spring:
  profiles:
    active: dev
~~~

因为功能单一，且相互依赖，最后统一写测试代码吧

## 自定义配置使用

像上文中，`xxf.name``xxf.desc`都是我们自定义的配置，那么我们在项目中如何快速的使用呢？

最简单的使用注解`@Value("${xxf.name}")`这样就可以了。

如果多个配置可能会经常使用，我们想统一管理怎么办，可以创建一个类，把它们统一定义在类中，然后在类上使用`@Configuration`注解就可以了。

对于同一组的配置，我们还可以使用`@ConfigurationProperties(prefix = "xxf")`指定前缀。像下面这样👇

~~~java
package com.maple.demo.config.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 笑小枫
 * @date 2022/7/22
 * @see <a href="https://www.xiaoxiaofeng.site">https://www.xiaoxiaofeng.site</a>
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "xxf")
public class XxfProperties {

    private String name;

    private String site;
}

~~~

## 实例测试

上面说了那么多，让我们来测试一下吧。

在`controller`包下编写测试的类`TestConfigController.java`

~~~java
package com.maple.demo.controller;

import com.maple.demo.config.bean.XxfProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 笑小枫
 * @date 2022/7/22
 * @see <a href="https://www.xiaoxiaofeng.site">https://www.xiaoxiaofeng.site</a>
 */
@Slf4j
@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
@Api(tags = "实例演示-application文件配置")
public class TestConfigController {

    /**
     * 通用-不区分环境
     */
    @Value("${xxf.name}")
    private String name;
    @Value("${xxf.site}")
    private String site;

    /**
     * 区分环境
     */
    @Value("${xxf.desc}")
    private String desc;

    /**
     * 同组配置，可以使用类的形式注入
     */
    private final XxfProperties xxfProperties;

    @ApiOperation(value = "不同环境的配置测试用例")
    @GetMapping("/configEnv")
    public String chooseEnv() {
        return desc;
    }

    @ApiOperation(value = "包含配置的测试用例")
    @GetMapping("/commonEnv")
    public String commonEnv() {
        return name + site;
    }

    @ApiOperation(value = "用类统一使用配置的测试用例")
    @GetMapping("/configClass")
    public String configClass() {
        return xxfProperties.getName();
    }

}

~~~

分为三种场景

* 不同环境的配置测试用例

​	配置dev环境，浏览器访问`http://localhost:6666/example/configEnv`

![image-20220817153538206](https://image.xiaoxiaofeng.site/article/img/2022/08/17/xxf-20220817153547.png)

​	切换成prod环境，重启项目，浏览器再次访问`http://localhost:6666/example/configEnv`

![image-20220817153714594](https://image.xiaoxiaofeng.site/article/img/2022/08/17/xxf-20220817153716.png)

* 包含配置的测试用例

  浏览器访问`http://localhost:6666/example/commonEnv`

![image-20220817153802762](https://image.xiaoxiaofeng.site/article/img/2022/08/17/xxf-20220817153804.png)

* 用类统一使用配置的测试用例

  浏览器访问`http://localhost:6666/example/configClass`

![image-20220817154030857](https://image.xiaoxiaofeng.site/article/img/2022/08/17/xxf-20220817154032.png)

## 更多配置管理工具

如果我们是单一项目，在项目中管理配置还是可以的，但如果我们有十个，一百个项目，，如果我们有专业的运维人员，或者生产的配置想要隔离开，这样我们再在项目中管理配置就不合适了。

我们项目可以整合一些其他的工具进行配置管理，例如阿里的`nocas`，携程的`apollo`等等都是很好的开源的配置管理系统，我们可以直接使用，这里不展开介绍，感兴趣的小伙伴可以阅读下面的文章。

[]()

[Apollo微服务配置中心详解]()