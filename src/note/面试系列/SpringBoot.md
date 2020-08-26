### 什么时SpringBoot
SpringBoot建立在Spring框架之上。使用Spring启动，减少了样板代码和配置，如pom依赖引入、spring与其他中间件集成等，SpringBoot可以帮助我们以最少的代码量，更加健壮的使用Spring的功能。

### SpringBoot的优点
1. 减少开发，测试时间。
2. 使用JavaConfig有利于避免xml
3. 避免大量的Maven导入和各种版本冲突
4. 内置Tomcat容器，方便启动部署
5. 需要更少的配置，因为没有web.xml，使用时只需添加用@Configuration注解的类，然后添加用@Bean注释的方法，Spring将自动加载对象并向以前一样对其进行管理。

### 如何自定义端口运行SpringBoot应用程序？

在application.properties中指定端口 server.port=8888
