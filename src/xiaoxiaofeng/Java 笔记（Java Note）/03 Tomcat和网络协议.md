# Tomcat #

 *  Servlet 容器为 JavaWeb 应用提供运行时环境，负责管理 Servlet 和 JSP 的生命周期，以及它们的共享数据
 *  Tomcat 是一个基于组件形式的的 Servlet 容器，由 Server（服务器）、Service（服务）、Connector（连接器）、Engine（引擎）、Host（主机）、Context（应用服务）组成，它们在 server.xml 里配置：

``````````xml
<Server port="8005" shutdown="SHUTDOWN"> 
      <Service name="Catalina"> 
          <Connector port="8080" protocol="HTTP/1.1" 
                     connectionTimeout="20000" 
                     redirectPort="8443" 
                     URIEncoding="UTF-8"/> 
          <Engine name="Catalina" defaultHost="localhost"> 
              <Host name="localhost" appBase="webapps" unpackWARs="true" 
                    autoDeploy="true"> 
                  <!-- Context：表示上下文，当前项目的环境 
                      docBase：需要被部署的 Web 项目的根路径 
                      path：表示上下文路径，其属性值可以是空或以'/'开始，多个<Context /> 的 path 值不能相同 
                      访问： http://ip:port/contextPath/资源名（包括后缀名） --> 
                  <Context docBase="D:\JavaApps\webapp" path="/app"/> 
              </Host> 
          </Engine> 
      </Service> 
  </Server>
``````````

 *  在 XML 配置文件中使用了中文，此时 XML 文件必须使用 UTF-8 编码
 *  Tomcat 7 默认的 HTTP 实现是采用**阻塞式**的 Socket 通信，每个请求都需要创建一个线程处理，Tomcat 请求处理线程的最大数量 maxThreads 默认是 200
 *  可以通过  元素中的 protocol 属性指定 Connector 使用的协议，默认值是 HTTP/1.1，其含义如下：在 Tomcat 7 中，自动选取使用 BIO 或 APR（如果找到 APR 需要的本地库，则使用 APR，否则使用 BIO）；在 Tomcat 8 中，自动选取使用 NIO 或 APR（如果找到 APR 需要的本地库，则使用 APR，否则使用 NIO）
 *  NIO 模式与 BIO 模式的最主要区别：“读取 socket 并交给 Worker 中的线程这个过程”是否是阻塞的，如果是阻塞的，意味着在 socket 等待下一个请求或等待释放的过程中，处理这个 socket 的工作线程会一直被占用，无法释放（HTTP/1.1 默认使用 TCP 长连接）
 *  对于每次客户端请求而言，Web 服务器大致需要完成如下几个步骤：
    
     *  启动单独的线程
     *  使用 I/O 流读取用户请求的二进制流数据
     *  从请求数据中解析参数，处理用户请求，生成响应数据
     *  使用 IO 流向客户端发送请求数据
        

## Tomcat 根路径下的目录 ##

 *  bin：存放了启动/关闭 Tomcat 的等工具
 *  conf：存放了 Tomcat 软件的一些配置文件
 *  lib：存放了Tomcat 软件启动运行的依赖 jar 文件
 *  logs：存放 Tomcat 日志记录
 *  temp：临时目录，比如把上传的大文件存放于临时目录
 *  webapps：里面存放需要部署的 javaweb 项目
 *  work：工作目录，存放了jsp 翻译成 Servlet 的 java 文件以及字节码文件

## 进入控制台 ##

 *  三个控制台：
    
     *  Status 控制台用于监控服务器的状态
     *  Manager 控制台可以部署、监控 Web 应用
     *  Host Manager 控制台
 *  添加管理用户：修改 conf 路径下的 tomcat-users.xml 文件

``````````xml
<tomcat-users> 
      <!-- 增加角色，指定角色名即可 --> 
      <role rolename="manager-gui"/> 
      <role rolename="admin-gui"/> 
      <!-- 增加用户，指定用户名、密码和角色即可 --> 
      <user username="root" password="admin" roles="manager-gui, admin-gui"/> 
  </tomcat-users>
``````````

## 部署 Web 应用 ##

 *  利用 Tomcat 的自动部署：将一个 Web 应用复制到 Tomcat 的 webapps 目录下
 *  利用控制台部署（实质依然是利用 Tomcat 的自动部署）
 *  增加自定义的 Web 部署文件：在 conf 目录下新建 Catalina 目录，再在 Catalina 目录下新建 localhost 目录，最后在该目录下新建一个名字任意的 XML 文件——该文件就是部署 Web 应用的配置文件，该文件的主文件名将作为 Web 应用的虚拟路径
 *  修改 server.xml 文件部署 Web 应用：修改 conf 目录下的 server.xml 文件在  中增加一个标签  ，path 代表当前项目的 **虚拟路径**，其属性值可以是空或者以 / 开头
    

## 配置 Tomcat 的数据源 ##

 *  将 MySQL 的 roBC 驱动程序复制到 Tomcat 的 lib 路径下
 *  配置局部数据源：在 Web 应用对应的部署文件中为 Context 元素增加一个 Resource 子元素
 *  配置全局数据源：修改 Tomcat 的 server.xml 文件

## 构建 Web 应用 ##

 *  新建一个 webapp 文件夹
 *  在第 1 步所建的文件夹内建一个 WEB-INF 文件夹
 *  在第 2 步所建的 WEB-INF 路径下，新建两个文件夹：classes 和 lib（classes 保存单个 \* . class 文件，lib 保存打包后的 JAR 文件）
 *  进入 Tomcat 或任何其他 Web 容器内，找到任何一个 Web 应用，将 Web 应用的 WEB-INF 下的 web. xml 文件复制到第 2 步所建的 WEB-INF 文件夹下，并修改复制后的 web.xml 文件，将该文件修改成只有一个根元素的 XML 文件
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414358543.png) 
    图 1 JavaWeb项目结构

# HTTP 协议 #

 *  Hypertext Transfer Protocol（超文本传输协议）的缩写
 *  属于应用层协议，构建在 TCP 和 IP 协议之上，用来规定服务器和客户浏览器端信息交互的格式
 *  特点：无状态，默认端口是 80
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414359245.jpeg) 
    图 2 HTTP网络协议栈

### TCP 长连接 ###

 *  从 HTTP/1.1 起，默认使用 TCP 长连接，用以保持连接特性（在响应头加入 Connection: keep-alive）
 *  在使用长连接的情况下，当一个请求完成后，客户端和服务器之间用于传输 HTTP 数据的 TCP 连接不会关闭，客户端再次访问这个服务器时，会继续使用这一条已经建立的连接发送数据包
 *  Keep-Alive 不会永久保持连接，它有一个保持时间，可以在不同的服务器软件中设定这个时间
 *  实现长连接需要客户端和服务端都支持长连接

### 请求报文 ###

 *  由一个请求行、若干消息头、一个空行、请求数据
 *  请求行包括请求方法、资源路径、HTTP 版本
 *  常见的 Request Headers
    
     *  Accept：用户代理期望的MIME 类型列表
     *  Accept-Encoding：列出用户代理支持的压缩方法
     *  Accept-Language：列出用户代理期望的页面语言
     *  Cache-Control: no-cache
     *  Connection: keep-alive
     *  Content-Type：数据编码格式
     *  Cookie：先前由服务器通过 Set-Cookie 首部投放并存储到客户端的 HTTP cookies
     *  Host：指明服务器的域名以及服务器监听的 TCP 端口号
     *  Origin：指示请求来自于哪个站点，用于 CORS 请求或者 POST 请求
     *  Pragma: no-cache，与 Cache-Control: no-cache 效果一致，强制要求缓存服务器在返回缓存的版本之前将请求提交到源头服务器进行验证
     *  Referer：包含了当前请求页面的来源页面的地址，即表示当前页面是通过此来源页面里的链接进入的
     *  User-Agent
 *  当前台页面使用 GET 或 POST 方式提交数据时，数据编码格式由请求头的 Content-Type 指定，可以分为：
    
     *  application/x-www-form-urlencoded
     *  multipart/form-data
     *  application/json、application/xml 等格式的数据

### 响应报文 ###

 *  由一个状态行、若干消息头、一个空行、响应正文
 *  常见的响应状态码：200（请求成功）、301/302/307（重定向）、400（请求参数有误）、500（服务器遇到错误）
    
     *  1xx：指示信息—表示请求已接收，继续处理
     *  2xx：成功—表示请求已被成功接收、理解、接受
     *  3xx：重定向—要完成请求必须进行更进一步的操作
     *  4xx：客户端错误—请求有语法错误或请求无法实现
     *  5xx：服务器错误—服务器未能实现合法的请求
 *  常见的 Response Headers
    
     *  Connection: keep-alive，当前的事务完成后不关闭网络连接（HTTP/1.1 的请求默认使用一个持久连接）
     *  Keep-Alive: timeout=5, max=1000，设置一个空闲连接需要保持打开状态的最小时长（以秒为单位），以及在连接关闭之前在此连接可以发送的请求的最大值
     *  Content-Type：指示服务器文档的 MIME 类型，帮助浏览器去处理接收到的数据
     *  Date：报文创建的日期和时间
     *  Server：处理请求的源头服务器所用到的软件相关信息
     *  Set-Cookie：服务器端向客户端发送的 cookie
 *  GET 和 POST 请求的区别
    
     *  GET 方式的请求会将请求参数的名和值转换成字符串，并附加在原 URL 之后，URL 和参数之间以”?“分隔，而多个参数之间以“&”分隔；POST 方式发送的请求参数以及对应的值放在请求实体中传输，安全性相对较高
     *  GET 请求传送的数据量一般不能大于 2 KB，而 POST 方式没有上限

### Cookie ###

 *  服务器在响应头里面添加一个 `Set-Cookie` 选项，通过该头部告知客户端保存 Cookie 信息
 *  `Set-Cookie: BIDUPSID=BC125A34B8B725424FEA411934D58D04; max-age=31536000; expires=Thu, 31-Dec-20 13:34:42 GMT; domain=.baidu.com; path=/; secure; HttpOnly`
 *  当 Cookie 满足请求 URL 的作用域、作用路径等条件时，浏览器在发送请求时就会在请求头的 `Cookie` 中设置该 Cookie，即 `Cookie: name1=value1; name2=value2; …`
 *  目前 cookie 规范有两个不同的版本：cookies 版本 0（有时被称为 Netscape cookies）和 cookies 版本 1（RFC 2965）
 *  属性
    
     *  =  ：cookie 的名称可以是除了控制字符、空格或制表符之外的任何 US-ASCII 字符，同时不能包含以下分隔字符： `( ) < > @ , ; : \ " / [ ] ? = { }`；cookie 的值如果存在，需要包含在双引号里面，支持除了控制字符、空格、双引号、逗号、分号以及反斜线之外的任意 US-ASCII 字符。
     *  domain：所属作用域，默认值为创建 cookie 的服务器的主机名（**不包含子域名**）。**如果显式指定了 domain，则一般包含子域名，即不论 domain 前面是有 `.`，浏览器存储的 domain 前面会包含 `.`**，所有向该域或其子域发送的请求中都会包含这个 cookie 信息。
     *  path：所属作用路径，默认值为创建 cookie 的 URL 的路径。以 "/" 为路径分隔符，子路径也会被匹配。
     *  expires：cookie 的最长有效时间。
     *  max-age：cookie 失效的时间，单位秒。如果为正数，则该 cookie 在 max-age 秒后失效；如果为负数，该 cookie 为临时 cookie，关闭浏览器即失效，浏览器也不会以任何形式保存该 cookie；如果为 0，表示删除该 cookie。默认为 -1。如果 expires 和 max-age 均存在，则 max-age 优先级更高。
     *  secure：只在使用 HTTPS 协议时才发送 cookie，默认值为 false。
     *  HttpOnly：告知浏览器不允许通过脚本 document.cookie 去修改这个值，同样这个值在 document.cookie 中也不可见，但在 HTTP 请求中仍会携带这个 cookie。
     *  SameSite：允许服务器要求某个 cookie 在跨站请求时不会被发送，从而可以阻止跨站请求伪造攻击（CSRF）。
        

# HTTPS 协议 #

 *  HTTPS 的全称是 Hypertext Transfer Protocol over Secure Socket Layer，即基于 SSL 的 HTTP 协议
 *  SSL 的全称为 Secure Sockets Layer，即安全套接层，一种网络安全协议
 *  SSL 的继任者是 TLS 协议，全称为 Transport Layer Security，即传输层安全协议
 *  HTTPS 的默认端口为 443
 *  HTTPS 既支持单向认证，也支持双向认证
 *  使用 JSSE（Java Security Socket Extension）进行 SSL/TSL 协议数据传输
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414360181.jpeg) 
    图 3 HTTPS协议栈

## HTTPS 通信的大致过程 ##

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414360682.png) 
图 4 HTTPS工作原理

 *  客户端将自己支持的加密算法发送给服务器，请求服务器证书
 *  服务器选取一组加密算法，并将证书（公钥）返回给客户端
 *  客户端校验证书合法性，生成**随机对称密钥**，用公钥加密后发送给服务器
 *  服务器用私钥解密出对称密钥，返回一个响应，HTTPS 连接建立完成
 *  随后双方通过这个**对称密钥**进行安全的数据通信

## SSL 协议的握手过程 ##

 *  客户端发送一个 client hello 消息，消息包含协议的版本信息、sessionid、客户端支持的加密算法、压缩算法等信息，以及客户端产生的随机数（client random）
 *  服务端响应一个 server hello 消息，消息包含协议版本信息、sessionid、压缩算法等信息，以及服务端数字证书、服务端产生的随机数（server random），如果是双向认证，则请求客户端证书
 *  客户端通过**证书链**验证服务端证书的有效性，如果证书验证通过，客户端将向服务端发送客户端证书以及经过服务端公钥加密的预主密钥 premaster secret（PMS）
 *  服务端验证服务客户端的有效性，并使用自己的私钥对 PMS 进行解密，使用客户端随机数、服务端随机数和解密后的 PMS 构键主密钥（Master Secret，MS），通过 MS 生成加密密钥
 *  客户端也使用客户端随机数、服务端随机数和 PMS 构键 MS，通过 MS 生成加密密钥
 *  通知服务端未来信息使用加密密钥加密
 *  给服务端发送加密密钥来加密信息，终止握手
 *  通知客户端使用加密密钥加密
 *  给客户端发送加密密钥加密信息，终止握手

## 部署 HTTPS Web ##

 *  使用 java/bin 下的 keytool（Java 的数字证书管理工具）生成一个数字证书（注意：生成的证书是**不被客户端信任的**），如果想得到**证书认证机构**（CA）的认证，需要导出数字证书并签发申请（CSR），经证书认证机构认证并颁发后，再将认证后的证书导入**本地密钥库**与**信任库**
    
     *  证书生成命令：`keytool -genkey -alias 别名 -storetype 仓库类型 -keyalg 算法 -keysize 长度 -keystore 文件名 -validity 有效期`
     *  说明：仓库类型，JKS、JCEKS、PKCS12 等；算法，RSA、DSA 等；长度，例如 2048
 *  Tomcat 服务器证书安装：修改 Tomcat 配置 server.xml

``````````xml
<Connector port="443" protocol="HTTP/1.1" SSLEnabled="true" 
      maxThreads="150" scheme="https" secure="true" 
      # 证书保存的路径，默认情况下，Tomcat 将从当前操作系统用户的用户目录下读取名为 “.keystore” 的文件 
      keystoreFile="/usr/*/conf/www.domain.com.jks" 
      # 密钥库密码，指定 keystore 的密码 
      keystorePass="******" 
      # 如果设为 true，表示 Tomcat 要求所有的 SSL 客户出示安全证书，对 SSL 客户进行身份验证 
      clientAuth="false"/>
``````````

## Linux 安装证书 ##

在线生成 SSL 配置：[SSL Configuration Generator][]

 *  开启防火墙的 443 端口：`iptables -A INPUT -p tcp —dport 443 -j ACCEPT`
 *  Nginx 服务器证书安装：修改 Nginx 配置（可以对每个虚拟主机进行单独配置，写在 server 块中）

``````````xml
server { 
     listen 80 default_server; 
     listen [::]:80 default_server; 
     # 在没有显式定义 default_server 时，nginx 会将配置的第一个 server 作为 default_server，即当请求没有匹配任何 server_name 或直接使用 ip 访问时，此 server 会处理此请求 
  
     # redirect all HTTP requests to HTTPS with a 301 Moved Permanently response. 
     return 301 https://$host$request_uri; 
     # rewrite ^(.*)$ https://$host$1 permanent; # 把http的域名请求转成https 
  } 
  
  server { 
     #listen 80; 
     #listen [::]:80; 
     listen 443 ssl http2; 
     listen [::]:443 ssl http2; # 监听443端口，并开启ssl功能，同时开启HTTP/2 
     server_name www.example.com; # 填写绑定证书的域名 
  
     ssl_certificate cert.crt; # 证书文件 
     ssl_certificate_key cert.key; # 私钥文件 
     ssl_session_timeout 1d; 
  
     ssl_protocols TLSv1.2 TLSv1.3; # 配置协议（可选） 
     #ssl_ciphers ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-CHACHA20-POLY1305:ECDHE-RSA-CHACHA20-POLY1305:DHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384; # 配置ECC证书加密套件 
     ssl_ciphers TLS13-AES-256-GCM-SHA384:TLS13-CHACHA20-POLY1305-SHA256:TLS13-AES-128-GCM-SHA256:TLS13-AES-128-CCM-8-SHA256:TLS13-AES-128-CCM-SHA256:EECDH+CHACHA20:EECDH+CHACHA20-draft:EECDH+ECDSA+AES128:EECDH+aRSA+AES128:RSA+AES128:EECDH+ECDSA+AES256:EECDH+aRSA+AES256:RSA+AES256:EECDH+ECDSA+3DES:EECDH+aRSA+3DES:RSA+3DES:!MD5; # 配置RSA证书加密套件，写法遵循 openssl 标准（可选） 
     ssl_prefer_server_ciphers on; # 由服务器协商最佳的加密算法（可选） 
  
     #开启HSTS（可选），并设置有效期为“6307200秒”（6个月），包括子域名（根据情况可删掉），预加载到浏览器缓存（根据情况可删掉） 
     add_header Strict-Transport-Security "max-age=63072000; includeSubdomains; preload"; 
  
     location / { 
         root /var/www/www.domain.com; 
         index  index.html index.htm; 
     } 
  }
``````````

## PKIX path building failed 问题 ##

 *  当 Java 客户端通过 HTTPS 访问的服务器的证书不被信任时，Java 客户端程序会抛出异常：`javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target`
 *  解决方法：方法 1. 禁用客户端的证书检查；方法 2. 将证书导入客户端使用的 JRE 的 cacerts 证书库
 *  使用 keytool 将证书导入到 JRE 的 cacerts 证书库

``````````xml
cd ${JAVA_HOME}/jre/lib/security 
  keytool -import -alias abc -keystore cacerts -file abc.cer
``````````

其中：-alias 指定别名（建议与证书同名），-keystore 指定存储文件（即证书库），-file 指定要导入的证书

> 
> 当切换到 cacerts 文件所在的目录时，才可指定 `-keystore cacerts`，否则需要指定全路径
> 当提示输入 cacerts 证书库的密码时，输入 `changeit`（即 JRE 中 cacerts 证书库的默认密码）
> 

 *  查看证书信息：`keytool -list -keystore cacerts -alias abc`
 *  如需更新证书，应先删除原证书，再导入新证书

``````````
cd ${JAVA_HOME}/jre/lib/security 
  keytool -delete -alias abc -keystore cacerts 
  keytool -import -alias abc -keystore cacerts -file ${JAVA_HOME}/jre/lib/security/abc.cer 
  keytool -list -keystore cacerts -alias abc
``````````

# OAuth 协议 #

 *  OAuth（开放授权）是一个开放标准，允许用户授权第三方应用访问他们存储在另外的服务提供者上的信息，而不需要将用户名和密码提供给第三方应用或分享他们数据的所有内容

## OAuth 授权过程 ##

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414361101.jpeg) 
图 5 OAuth协议授权数据访问过程

 *  用户先对第三方软件厂商（ISV）的应用进行访问，发起请求
 *  ISV 接收到用户请求后，再向平台商请求 request token，并带上其申请的 **appld**
 *  平台将返回给第三方应用 **request token**
 *  ISV 应用将用户引导到平台的授权页面，并带上自己的 **appld**、**request token** 和**回调地址**
 *  用户在平台的页面上进行登录，并且完成授权
 *  平台通过 ISV 提供的回调链接，返回给 ISV 应用 **access token**
 *  ISV 应用通过 **access token** 取到用户授权的数据，进行加工后返回给用户，授权数据访问完成


[JavaWeb]: https://static.sitestack.cn/projects/sdky-java-note/91bd6fb0144adfe6adb5cdb2cf6426b0.png
[HTTP]: https://static.sitestack.cn/projects/sdky-java-note/0932e7c6491565a6e2b0c4a53e839b90.jpeg
[HTTPS]: https://static.sitestack.cn/projects/sdky-java-note/99ef4daa70ee10b93059b12cac689705.jpeg
[HTTPS 1]: https://static.sitestack.cn/projects/sdky-java-note/ebd4900f1c8d460e2a417ed7e9c940b3.png
[SSL Configuration Generator]: https://ssl-config.mozilla.org/
[OAuth]: https://static.sitestack.cn/projects/sdky-java-note/8cec20f9bc3a82c7033efee37ebdb14c.jpeg