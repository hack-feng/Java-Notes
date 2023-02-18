### 什么是SpringBoot？

多年来，随着新功能的增加，spring变得越来越复杂。只需访问https://spring.io/projects页面，我们就会看到可以在我们的应用程序中使用的所有Spring项目的不同功能。如果必须启动一个新的Spring项目，我们必须添加构建路径或添加Maven依赖关系，配置应用程序服务器，添加spring配置。因此，开始一个新的spring项目需要很多努力，因为我们现在必须从头开始做所有事情。

SpringBoot是解决这个问题的方法。SpringBoot已经建立在现有spring框架之上。使用spring启动，我们避免了之前我们必须做的所有样板代码和配置。因此，SpringBoot可以帮助我们以最少的工作量，更加健壮地使用现有的Spring功能。


### SpringBoot有哪些优点？

1. 减少开发，测试时间和努力。
2. 使用JavaConfig有助于避免使用XML。
3. 避免大量的Maven导入和各种版本冲突。
4. 提供意见发展方法。
5. 通过提供默认值快速开始开发。
6. 没有单独的Web服务器需要。这意味着你不再需要启动Tomcat，Glassfish或其他任何东西。
7. 需要更少的配置因为没有web.xml文件。只需添加用@Configuration注释的类，然后添加用@Bean注释的方法，Spring将自动加载对象并像以前一样对其进行管理。您甚至可以将@Autowired添加到bean方法中，以使Spring自动装入需要的依赖关系中。
8. 基于环境的配置使用这些属性，您可以将您正在使用的环境传递到应用程序：-Dspring.profiles.active={enviornment}。在加载主应用程序属性文件后，Spring将在(application{environment}.properties)中加载后续的应用程序属性文件。

### 什么是JavaConfig？

SpringJavaConfig是Spring社区的产品，它提供了配置SpringIoC容器的纯Java方法。因此它有助于避免使用XML配置。使用JavaConfig的优点在于：

1. **面向对象的配置**。由于配置被定义为JavaConfig中的类，因此用户可以充分利用Java中的面向对象功能。一个配置类可以继承另一个，重写它的@Bean方法等。

2. **减少或消除XML配置**。基于依赖注入原则的外化配置的好处已被证明。但是，许多开发人员不希望在XML和Java之间来回切换。JavaConfig为开发人员提供了一种纯Java方法来配置与XML配置概念相似的Spring容器。从技术角度来讲，只使用JavaConfig配置类来配置容器是可行的，但实际上很多人认为将JavaConfig与XML混合匹配是理想的。

3. **类型安全和重构友好**。JavaConfig提供了一种类型安全的方法来配置Spring容器。由于Java5.0对泛型的支持，现在可以按类型而不是按名称检索bean，不需要任何强制转换或基于字符串的查找。

### 如何重新加载SpringBoot上的更改，而无需重新启动服务器？

这可以使用DEV工具来实现。通过这种依赖关系，您可以节省任何更改，嵌入式tomcat将重新启动。SpringBoot有一个开发工具(DevTools)模块，它有助于提高开发人员的生产力。Java开发人员面临的一个主要挑战是将文件更改自动部署到服务器并自动重启服务器。开发人员可以重新加载SpringBoot上的更改，而无需重新启动服务器。这将消除每次手动部署更改的需要。SpringBoot在发布它的第一个版本时没有这个功能。这是开发人员最需要的功能。DevTools模块完全满足开发人员的需求。该模块将在生产环境中被禁用。它还提供H2数据库控制台以更好地测试应用程序。

~~~xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-devtools</artifactId>
	<optional>true</optional>
</dependency>
~~~


### SpringBoot中的监视器是什么？

Springbootactuator是spring启动框架中的重要功能之一。Springboot监视器可帮助您访问生产环境中正在运行的应用程序的当前状态。有几个指标必须在生产环境中进行检查和监控。即使一些外部应用程序可能正在使用这些服务来向相关人员触发警报消息。监视器模块公开了一组可直接作为HTTPURL访问的REST端点来检查状态。

### 如何在SpringBoot中禁用Actuator端点安全性？

默认情况下，所有敏感的HTTP端点都是安全的，只有具有ACTUATOR角色的用户才能访问它们。安全性是使用标准的HttpServletRequest.isUserInRole方法实施的。我们可以使用来禁用安全性。只有在执行机构端点在防火墙后访问时，才建议禁用安全性。


### 如何在自定义端口上运行SpringBoot应用程序？

为了在自定义端口上运行SpringBoot应用程序，您可以在
application.properties中指定端口。

server.port=8090

### 什么是YAML？

YAML是一种人类可读的数据序列化语言。它通常用于配置文件。

与属性文件相比，如果我们想要在配置文件中添加复杂的属性，YAML文件就更加结构化，而且更少混淆。可以看出YAML具有分层配置数据。

### 如何实现SpringBoot应用程序的安全性？

为了实现SpringBoot的安全性，我们使用spring-boot-starter-security依赖项，并且必须添加安全配置。它只需要很少的代码。配置类将必须扩展WebSecurityConfigurerAdapter并覆盖其方法。

### 如何集成SpringBoot和ActiveMQ？

对于集成SpringBoot和ActiveMQ，我们使用依赖关系。它只需要很少的配置，并且不需要样板代码。


### 如何使用SpringBoot实现分页和排序？

使用SpringBoot实现分页非常简单。使用SpringData-JPA可以实现将可分页的传递给存储库方法。

### 什么是Swagger？你用SpringBoot实现了它吗？


Swagger广泛用于可视化API，使用SwaggerUI为前端开发人员提供在线沙箱。Swagger是用于生成RESTfulWeb服务的可视化表示的工具，规范和完整框架实现。它使文档能够以与服务器相同的速度更新。当通过Swagger正确定义时，消费者可以使用最少量的实现逻辑来理解远程服务并与其进行交互。因此，Swagger消除了调用服务时的猜测。

### 什么是SpringProfiles？

SpringProfiles允许用户根据配置文件(dev，test，prod等)来注册bean。因此，当应用程序在开发中运行时，只有某些bean可以加载，而在PRODUCTION中，某些其他bean可以加载。假设我们的要求是Swagger文档仅适用于QA环境，并且禁用所有其他文档。这可以使用配置文件来完成。SpringBoot使得使用配置文件非常简单。

### 什么是SpringBatch？

SpringBootBatch提供可重用的函数，这些函数在处理大量记录时非常重要，包括日志/跟踪，事务管理，作业处理统计信息，作业重新启动，跳过和资源管理。它还提供了更先进的技术服务和功能，通过优化和分区技术，可以实现极高批量和高性能批处理作业。简单以及复杂的大批量批处理作业可以高度可扩展的方式利用框架处理重要大量的信息。

### 什么是FreeMarker模板？

FreeMarker是一个基于Java的模板引擎，最初专注于使用MVC软件架构进行动态网页生成。使用Freemarker的主要优点是表示层和业务层的完全分离。程序员可以处理应用程序代码，而设计人员可以处理html页面设计。最后使用freemarker可以将这些结合起来，给出最终的输出页面。

### 如何使用SpringBoot实现异常处理？

Spring提供了一种使用ControllerAdvice处理异常的非常有用的方法。我们通
过实现一个ControlerAdvice类，来处理控制器类抛出的所有异常。

### 您使用了哪些starter maven依赖项？

使用了下面的一些依赖项

spring-boot-starter-activemq
spring-boot-starter-security

这有助于增加更少的依赖关系，并减少版本的冲突。


### 我们如何监视所有SpringBoot微服务？

SpringBoot提供监视器端点以监控各个微服务的度量。这些端点对于获取有关应用程序的信息(如它们是否已启动)以及它们的组件(如数据库等)是否正常运行很有帮助。但是，使用监视器的一个主要缺点或困难是，我们必须单独打开应用程序的知识点以了解其状态或健康状况。想象一下涉及50个应用程序的微服务，管理员将不得不击中所有50个应用程序的执行终端。

为了帮助我们处理这种情况，我们将使用位于的开源项目。它建立在SpringBootActuator之上，它提供了一个WebUI，使我们能够可视化多个应用程序的度量。
