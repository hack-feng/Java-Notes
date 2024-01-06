 *  高性能的 HTTP 和**反向代理**服务器，同时也是 IMAP/POP3/SMTP 代理服务器
 *  Nginx 分为主进程（master process）和工作进程（worker process），每个进程中只有一个线程（也可以配置线程池），通过 IO 多路复用（底层使用 epoll/kqueue 等技术）和事件循环达到高并发。主进程负责总体协调工作，比如在配置文件更新后重新应用配置、协调哪个 worker process 应该退役等等。工作进程的个数一般设置为 CPU 的个数。

## Nginx 的启动和停止 ##

 *  start nginx.exe：启动
 *  nginx \[-c /etc/nginx/nginx.conf\]：启动，可同时指定主配置文件
 *  nginx -h
 *  nginx -s 信号：向正在运行的 nginx 进程发送信号，信号包括：
    
     *  stop：立即关闭
     *  quit：安全关闭（处理完请求后在停止服务）
     *  reload：重载配置文件
     *  reopen：重新打开日志文件，实现日志分隔的效果
 *  nginx -t：检查配置文件是否存在语法错误
 *  nginx -v：查看当前 nginx 的版本信息
 *  nginx -V：查看当前 nginx 的编译信息，如安装的模块、安装目录、各种文件的目录、编译器选项等

## Nginx 的配置文件 nginx.conf ##

``````````nginx
#user  nobody; #指定使用的用户和组 
  worker_processes  1; #启动进程，通常设置成和cpu的数量相等 
  
  #全局错误日志 
  error_log  logs/error.log; 
  #error_log  logs/error.log  notice; 
  #error_log  logs/error.log  info; 
  
  #pid        logs/nginx.pid; #PID文件--存放进程号的文件 
  
  #创建全局配置中nginx需要的运行用户 
  #groupadd nginx 
  #useradd -g nginx nginx 
  
  #工作模式及连接数上限 
  events { 
      worker_connections  1024; #单个后台worker process进程的最大并发链接数 
      #并发总数是 worker_processes 和 worker_connections 的乘积 
  } 
  
  #Nginx对HTTP服务器相关属性的配置 
  http { 
      include       mime.types; 
      default_type  application/octet-stream; #设定默认类型为二进制流 
  
      #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" ' 
      # '$status $body_bytes_sent "$http_referer" ' 
      # '"$http_user_agent" "$http_x_forwarded_for"'; 
  
      #access_log  logs/access.log  main; 
  
      sendfile        on; 
      #tcp_nopush     on; 
  
      #keepalive_timeout  0; 
      keepalive_timeout  65; 
  
      #gzip  on; 
  
      include vhost/*.conf; 
  
      #设定 localhost 对应的虚拟主机 
      server { 
          listen       80; #监听80端口 
          server_name  localhost; #定义使用 www.example.cn 访问 
  
          #root D:\code; #定义服务器的默认网站根目录位置 
  
          #charset koi8-r; #设置编码 
  
          #access_log  logs/host.access.log  main; #设定本虚拟主机的访问日志 
  
          #主要用于匹配网页位置，设置不同的功能特征 
          location / { 
              root   html; #定义当前 location 的文档根目录为 html 目录 
              index  index.html index.htm; #定义首页索引文件的名称 
          } 
  
          #error_page  404  /404.html; 
  
          # redirect server error pages to the static page /50x.html 
          error_page   500 502 503 504  /50x.html; 
          location = /50x.html { 
              root   html; 
          } 
      include vhost/*.conf; 
  }
``````````

## Nginx 的虚拟主机 ##

 *  虚拟主机是用来**映射**网站目录和网站代码文件夹的关系
 *  可以通过 server 配置，每个 server 表示一个虚拟机主机

## 动静分离 ##

 *  当用户请求 js、css 等静态资源时有当前 Nginx 服务器处理，请求 php 动态脚本代码时由其他的服务器处理（反向代理）

``````````nginx
# 配置 Nginx 动静分离，定义的静态页面直接从 Nginx 发布目录读取 
      location ~ .*\.(gif|jpg|jpeg|bmp|png|ico|txt|js|css)$ { 
          root /data/www/wugk; 
          #expires 定义用户浏览器缓存的时间为 3 天 
          expires      3d; 
      } 
      # 动态页面交给其他服务器处理 
      location ~ .*\.(php|jsp|cgi)?$ { 
         # 发送给其他服务器处理 
      }
``````````

## Nginx 的反向代理 ##

 *  反向代理（Reverse Proxy）方式是指以代理服务器来接受 internet 上的连接请求，然将请求转发给内部网络上的服务器，并将从服务器上得到的结果返回给 internet 上请求连的客户端，此时代理服务器对外就表现为一个服务器
 *  代理服务器可以作为前端服务器处理静态资源

``````````nginx
server { 
      # 侦听 192.168.8.x 的 80 端口 
      listen       80; 
      server_name    www.itsource.cn; 
  
      # 反向代理时获取客户端真实 IP、域名、协议、端口 
      proxy_set_header Host $http_host; 
      proxy_set_header X-Real-IP $remote_addr; 
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for; 
      proxy_set_header X-Forwarded-Proto $scheme; 
  
      # 对 php 后缀的进行请求 
      location ~ .*\.php$ { 
          # 定义服务器的默认网站根目录位置 
          root     /root; 
          # 定义首页索引文件的名称 
          index index.php index.html index.htm; 
  
          # 请求转向 apache 服务器，apache 服务器运行在 81 端口上 
          proxy_pass    http://localhost:81; 
      } 
  }
``````````

## Nginx 的负载均衡 ##

 *  优点：分散后台服务器的压力；自动去掉不可用的后台服务器；缓存后台服务器响应内容
 *  负载均衡的分配策略：轮询（默认）、weight、ip\_hash、fair（第三方，按后端服务器的响应时间来分配请求，响应时间短的优先分配）、url\_hash（第三方）
 *  负载均衡**配置**

``````````nginx
# 在 http 节点中定义负载均衡设备的 ip 及设备状态 
  upstream myServer { 
      # ip_hash; 
      # down 表示当前的 server 暂时不参与负载 
      server 127.0.0.1:9090 down; 
      # weight  默认为 1，weight 越大，负载的权重就越大 
      server 127.0.0.1:8080 weight=2; 
      server 127.0.0.1:6060; 
      # 其它所有的非 backup 机器 down 或者忙的时候，请求 backup 机器 
      server 127.0.0.1:7070 backup; 
      [ip_hash | fair | url_hash] 
  } 
  
  # 在需要使用负载的 server 节点下添加 
  proxy_pass http://myServer;
``````````