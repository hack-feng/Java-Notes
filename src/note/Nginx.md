### 使用Nginx做负载均衡
upstream 配置多台服务器
upstream 的配置策略
* 轮询（默认） 
~~~
# 每个请求按时间顺序逐一分配到不同的后端服务器，如果后端服务器down掉，能自动剔除。
upstream backserver { 
    server 192.168.0.14; 
    server 192.168.0.15; 
} 
~~~

* 指定权重（weight）
~~~
# 指定轮询几率，weight和访问比率成正比，用于后端服务器性能不均的情况。 
upstream backserver { 
    server 192.168.0.14 weight=10; 
    server 192.168.0.15 weight=10; 
}
~~~

* IP绑定 ip_hash
~~~
# 每个请求按访问ip的hash结果分配，这样每个访客固定访问一个后端服务器，可以解决session的问题。 
upstream backserver { 
    ip_hash; 
    server 192.168.0.14:88; 
    server 192.168.0.15:80; 
} 
~~~

* fair（第三方）
~~~
按后端服务器的响应时间来分配请求，响应时间短的优先分配。 
upstream backserver { 
    server server1; 
    server server2; 
    fair; 
} 
~~~

* url_hash（第三方）
~~~
按访问url的hash结果来分配请求，使每个url定向到同一个后端服务器，后端服务器为缓存时比较有效。 
upstream backserver { 
    server squid1:3128; 
    server squid2:3128; 
    hash $request_uri; 
    hash_method crc32; 
} 
~~~

具体使用：
~~~
proxy_pass http://backserver/; 
upstream backserver{ 
    ip_hash; 
    server 127.0.0.1:9090 down; (down 表示单前的server暂时不参与负载) 
    server 127.0.0.1:8080 weight=2; (weight 默认为1.weight越大，负载的权重就越大) 
    server 127.0.0.1:6060; 
    server 127.0.0.1:7070 backup; (其它所有的非backup机器down或者忙的时候，请求backup机器) 
} 
max_fails ：允许请求失败的次数默认为1.当超过最大次数时，返回proxy_next_upstream 模块定义的错误 
fail_timeout:max_fails次失败后，暂停的时间
~~~

### Nginx的基础配置
~~~
########### 每个指令必须有分号结束。#################
#user administrator administrators;  #配置用户或者组，默认为nobody nobody。
#worker_processes 2;  #允许生成的进程数，默认为1
#pid /nginx/pid/nginx.pid;   #指定nginx进程运行文件存放地址
error_log log/error.log debug;  #制定日志路径，级别。这个设置可以放入全局块，http块，server块，级别以此为：debug|info|notice|warn|error|crit|alert|emerg
events {
    accept_mutex on;   #设置网路连接序列化，防止惊群现象发生，默认为on
    multi_accept on;  #设置一个进程是否同时接受多个网络连接，默认为off
    #use epoll;      #事件驱动模型，select|poll|kqueue|epoll|resig|/dev/poll|eventport
    worker_connections  1024;    #最大连接数，默认为512
}
http {
    include       mime.types;   #文件扩展名与文件类型映射表
    default_type  application/octet-stream; #默认文件类型，默认为text/plain
    #access_log off; #取消服务日志    
    log_format myFormat '$remote_addr–$remote_user [$time_local] $request $status $body_bytes_sent $http_referer $http_user_agent $http_x_forwarded_for'; #自定义格式
    access_log log/access.log myFormat;  #combined为日志格式的默认值
    sendfile on;   #允许sendfile方式传输文件，默认为off，可以在http块，server块，location块。
    sendfile_max_chunk 100k;  #每个进程每次调用传输数量不能大于设定的值，默认为0，即不设上限。
    keepalive_timeout 65;  #连接超时时间，默认为75s，可以在http，server，location块。

    upstream mysvr {   
      server 127.0.0.1:7878;
      server 192.168.10.121:3333 backup;  #热备
    }
    error_page 404 https://www.baidu.com; #错误页
    server {
        keepalive_requests 120; #单连接请求上限次数。
        listen       4545;   #监听端口
        server_name  127.0.0.1;   #监听地址
        #location [ = | ~ | ~* | ^~ ] URI { ... }
		#设置一个 URI 匹配路径
		#=：精确匹配
		#~：正则表达式匹配，区分字符大小写
		#~*：正则表达式匹配，不区分字符大小写
		#^~：URI 的前半部分匹配，且不实用正则表达式
		 #优先级：
		#= > location 完整路径 > ^~ > ~ > ~* > location 起始路径 > location /       
        location  ~*^.+$ {       #请求的url过滤，正则匹配，~为区分大小写，~*为不区分大小写。
           #root path;  #根目录    
           #index vv.txt;  #设置默认页
           proxy_pass  http://mysvr;  #请求转向mysvr 定义的服务器列表
           deny 127.0.0.1;  #拒绝的ip
           allow 172.18.5.54; #允许的ip           
        } 
    }
} 
~~~

### nginx反向代理路径问题
~~~
http://abc.com:8080 写法和 http://abc.com:8080/ 写法的区别如下:

不带/
location /NginxTest/ {
            proxy_pass  http://abc.com:8080;
}

带/
location /NginxTest/ {
            proxy_pass  http://abc.com:8080/;
}
上面两种配置，区别只在于proxy_pass转发的路径后是否带 “/”。
针对情况1:
        (带参数)如果访问url =http://localhost:90/NginxTest/servlet/MyServlet?name=123333333，
        则被nginx代理后，请求路径便会访问http://abc.com:8080/NginxTest/servlet/MyServlet?name=123333333。
        
       （不带参数）如果访问url =http://localhost:90/NginxTest/servlet/MyServlet，
       则被nginx代理后，请求路径便会访问http://abc.com:8080/NginxTest/servlet/MyServlet。

针对情况2：
          如果访问url = http://server/NginxTest/test.jsp，
          则被nginx代理后，请求路径会变为 http://proxy_pass/test.jsp，直接访问server的根资源。
          
          访问http://localhost:90/NginxTest/NginxTest/NginxTest/servlet/MyServlet，
          被nginx代理后，请求路径才会访问http://abc.com:8080/NginxTest/servlet/MyServlet。
          
**注意：上面两种访问路径的差别。
修改配置后重启nginx代理就成功了。**
~~~

### nginx地址重写
~~~
location /NginxTest/ {
    rewrite  ^/NginxTest/(.*)$  /$1  break;
    proxy_pass  http://abc.com:8080;
}

1. ^~/NginxTest/ 是一个匹配规则，用于拦截请求，匹配任何以 /NginxTest/ 开头的地址，匹配符合以后就停止往下搜索正则。
2.rewrite  ^/NginxTest/(.*)$  /$1  break;
代表重写拦截进来的请求，并且只能对域名后边的除去传递的参数外的字符串起作用，
例如http://localhost:90/NginxTest/NginxTest/servlet/MyServlet?name=lovleovlove重写,
只对/NginxTest/NginxTest/servlet/MyServlet重写。

例如访问地址：http://localhost:90/NginxTest/NginxTest/servlet/MyServlet?name=lovleovlove，
实际访问的地址（重写地址）为http://ita-1312-0059.synacast.local:8080/NginxTest/servlet/MyServlet?name=lovleovlove

例如访问地址：http://localhost:90/NginxTest/NginxTest/servlet/MyServlet，
实际访问的地址（重写地址）为http://ita-1312-0059.synacast.local:8080/NginxTest/servlet/MyServlet

3.rewrite后面的参数是一个简单的正则^/NginxTest/(.*)$  /$1 ,$1代表正则中的第一个(),$2代表第二个()的值,以此类推。
break代表匹配一个之后停止匹配。
rewrite的语法：
rewrite regex URL [flag];
rewrite是关键字，regex是正则表达式，URL是要替代的内容，[flag]是标记位的意思，它有以下几种值：
last: 相当于Apache的[L]标记，表示完成rewrite
break: 停止执行当前虚拟主机的后续rewrite指令集
redirect: 返回302临时重定向，地址栏会显示跳转后的地址
permanent: 返回301永久重定向，地址栏会显示跳转后的地址
~~~

### nginx启动重启
~~~
命令: nginx -c /usr/local/nginx/conf/nginx.conf

重启服务： service nginx restart

2. 快速停止或关闭Nginx：nginx -s stop

3. 正常停止或关闭Nginx：nginx -s quit

4. 配置文件修改重装载命令：nginx -s reload
~~~

### docker启动nginx
~~~
docker run -d -p 9000:80 -v /data/deploy_yjwl/web:/usr/share/nginx/html --name nginxyjwl --restart always nginx
~~~

### nginx 配置vue使用history模式
在location里面添加

`try_files $uri $uri/ /index.html;`

详细如下所示
~~~
location / {
    root   /usr/share/nginx/html;
    index  index.html index.htm;
    # 配置vue的history模式
    try_files $uri $uri/ /index.html;
}
~~~