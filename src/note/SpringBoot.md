### 解决前端调用跨域问题

~~~java
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE");
    }
}
~~~

### 发送邮件
* pom.xml添加引用
~~~xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<!-- 支持发送邮件 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
    <version>1.5.9.RELEASE</version>
</dependency>
~~~

* 配置发送邮件的application
~~~yaml
spring
  mail:
    default-encoding: UTF-8
    host: smtp.qq.com
    username: 1150640979@qq.com
    password: 1234567890
    port: 22
    protocol: smtp
    properties:
      mail:
        smtp:
          ssl:
            enable: true
          socketFactory:
            port: 465
            class: javax.net.ssl.SSLSocketFactory
~~~

* 编写邮件的工具类
~~~java
@Service
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;
    /**
     * 用来发送模版邮件
     */
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendEmail(Context context, String templateName, String to, String [] cc,
                          String subject, String text, List<String> attachmentList){
        try {
            // 定义邮件信息
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            if(cc != null && cc.length > 0){
                helper.setCc(cc);
            }

            // 如果存在模板，定义邮件模板中的内容
            if(context != null && StringUtils.isNotBlank(templateName)){
                String emailContent = templateEngine.process(templateName, context);
                helper.setText(emailContent, true);
            }else{
                helper.setText(text);
            }

            // 如果存在附件，定义邮件的附件
            if(attachmentList != null && attachmentList.size() > 0){
                for (String attachment : attachmentList) {
                    FileSystemResource file = new FileSystemResource(attachment);
                    helper.addAttachment(file.getFilename(), file);
                }
            }
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
~~~

* 配置发送邮件的模板
在resources目录下创建templates目录。创建email.html模板。
~~~html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>yimcarson</title>
    <style>
        body {
            text-align: center;
            margin-left: auto;
            margin-right: auto;
        }
        #main {
            text-align: center;
        }
    </style>
</head>
<body>
<div id="main">
    <h3>Welcome <span th:text="${project}"></span> -By <span th:text=" ${author}"></span></h3>
    Your Verification Code is
    <h2><span th:text="${code}"></span></h2>
</div>
</body>
</html>
~~~