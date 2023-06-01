 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414658321.jpeg) 
图 1 free\_software\_licenses

# Linux 管理 #

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414658882.png) 
图 2 Linux目录结构

## 常用快捷键 ##

Ctrl + l：清除屏幕信息Ctrl + C：中断正在运行的程序Shift + Insert：粘贴Tab：命令和文件名补全

## 常用的一些命令 ##

 *  pwd：显示当前所在的目录
 *  ls：显示当前目录的文件和目录
 *  ls -l：显示当前目录的文件和目录的详细信息
 *  ls -a：显示当前目录的文件和目录，包括隐藏文件和目录
 *  ls -al：多个选项合并使用
 *  cd /：跳转到根目录
 *  cd ~：跳转到当前用户的家目录
 *  cd -：切换到上次进入的目录
 *  cd abc/test：使用相对路径的方式切换到 abc/text 目录
 *  cd /abc/test：使用绝对路径的方式切换到 abc/text 目录
 *  clear：清除屏幕信息
 *  ifconfig：查看 Linux 操作系统的 IP 地址
 *  df：用于显示磁盘的使用情况
 *  df -h：格式化显示输出磁盘使用情况
 *  du 目录名称：显示每个文件的大小
 *  du -sh 目录名称：汇总显示该目录以及子目录下所有文件的大小
 *  env：查看当前用户的变量
 *  echo：输出
 *  history：显示执行过的历史命令记录
 *  which \[-a\] 指令：指令搜索，-a 将所有指令列出
 *  man：查看指令帮助
 *  init 0：关机
 *  init 6：重启，或 reboot
 *  su 用户名：切换登录用户
 *  passwd：修改密码
 *  setup：启动图形化操作界面
 *  wget -qO- bench.sh | bash：查看系统信息，测试网络带宽及硬盘读写速率

### systemctl 命令 ###

 *  监视和控制 systemd，可用于查看系统状态和管理系统及服务（实际上将 service 和 chkconfig 这两个命令组合到一起）
 *  Unit：表示不同类型的 sytemd 对象，通过配置文件进行标识和配置，文件中主要包含了系统服务，监听 socket、保存的系统快照以及其他与 init 相关的信息（使用 systemctl 控制单元时，通常需要使用单元文件的全名，包括扩展名，如果无扩展名，systemctl 默认把扩展名当做 .service）
 *  配置文件/usr/lib/systemd/system：每个服务最主要的启动脚本设置，类似于之前的/etc/initd.d/run/system/system：系统执行过程中所产生的服务脚本，比上面的目录优先运行/etc/system/system：管理员建立的执行脚本，类似于/etc/rc.d/rcN.d/Sxx 类的功能，比上面目录优先运行，在三者之中，此目录优先级最高
 *  实例systemctl start firewalld.service：启动某服务systemctl stop firewalld.service：停止某服务systemctl restart firewalld.service：重启某服务systemctl try-restart firewalld.service：条件式某服务（已启动才重启，否则不做任何操作）systemctl status firewalld.service：检查服务状态systemctl enable firewalld.service：设置某服务开机自启动systemctl is-enabled firewalld.servcice：查看某服务是否开机自启systemctl disable firewalld.service：停止某服务开机自启动systemctl list-units -t service：查看所有已启动的服务systemctl kill 进程名：杀死进程systemctl halt systemctl poweroff：关机systemctl reboot：重启systemctl suspend：挂起systemctl hibernate：休眠systemctl hybrid-sleep：休眠并挂起

### 权限 ###

 *  权限：定义计算机资源或者服务的访问能力，常见的操作权限有：r 可读取、w 可写入、x 可执行等
 *  定义一个资源的权限：① 用户具有该资源的权限（文件所有者，属主）；② 用户组具有该资源的权限（属组）；③ 其他用户（既不是属主，也不是属组）
 *  chmod：用于修改文件的权限 `chmod [-cfvR] mode file`，如 chmod 777 file、chmod +x xxx.sh
 *  mode：权限设定字串，格式如下
    
     *  4 r—、5 r-x、6 rw-、7 rwx
     *   *  表示增加权限、- 表示取消权限、= 表示唯一设定权限
 *  \-R：递归改变整个目录的所有文件权限

## Vi 编辑器 ##

 *  3 种基本工作模式编辑模式（命令模式）：所有的机键动作都会理解为编辑整个文档的操作，默认为编辑模式输入模式（插入模式）：大部分机键动作都会理解为输入的字符（在命令模式下按 i 或 o 进入）末行模式：在末行模式，输入很多文件管理命令用于离开或者保存文件（在命令模式下按 : 进入）
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414659460.jpeg) 
    图 3 Vim's\_modes

 *  退出 vi 及保存文件命令模式下保存并退出：输入 ZZ:w \[filename\] 将文章以指定的文件名 filename 保存:wq 存盘并退出 vi:x 执行保存并退出 vi:q! 不存盘强制退出 vi
 *  编辑模式0：跳转到行首$：跳转到行尾G：跳转到最后一行gg：跳转到第一行i：进入插入模式，从光标当前位置开始输入文件o：进入插入模式，当前光标所在行的下方插入新的一行:：进入末行模式Ctrl + F：向下翻一页Ctrl + B：向上翻一页dd：删除光标所在行3dd：从光标行开始删除 3 行yy：复制光标所在行2yy：从光标行开始复制 2 行p：粘贴n：下一个匹配的字符串N：上一个匹配的字符串
 *  末行模式:set nu：显示行号:3：跳转到第 3 行:1, 4d：删除第一行到第四行:/parttern：从前往后查找 parttern:?parttern：从后往前查找 parttern:%s/partter/string/gi：替换 partter 为 string（g 全局替换，i 忽略大小写）

## 文件管理 ##

 *  文件搜索和查看find 命令查找符合条件的文件（用 . 来表示当前目录，用 / 来表示系统根目录），-name 指定目录 'test*'（从指定目录下查找指定文件名，可以用*  通配符），find -mtime +30（更改时间在 30 天之前的文件），find -mmin +1（更改时间在 1 分钟前的文件）whereis 文件名：查找文件所在位置cat：查看文件内容cat > filename：可以快速创建文件并写入内容less 或 more：less 输入 /pattern 可以查找内容，more 翻页比较方便tail -100f：默认显示最后 10 行，带 f 可以实时更新；tail -10 文件：指定显示第几行
 *  创建文件和目录touch 文件名vi 文件名mkdir -p 目录名 1/目录名 2：创建层级目录，可以是相对目录，也可以是绝对目录
 *  文件的压缩和解压
    
     *  tar 命令，常用选项：-z ：使用 gzip，文件后缀名 .tar.gz-j ：使用 bzip2，文件后缀名 .tar.bz2-J ：使用 xz，文件后缀名 .tar.xz-c ：新建打包文件，文件后缀名 .tar-t ：查看打包文件里面有哪些文件-x ：解打包或解压缩的功能-v ：显示执行时的详细信息-f 文件：指定要处理的文件-C 目录：在特定目录解压缩
     *  zip 命令，压缩 .zip 文件，常用选项：-q：不显示指令执行过程-r：递归处理，将指定目录下的所有文件和子目录一并处理
     *  unzip 命令，解压缩 .zip 文件，常用选项：-l：显示压缩文件内所包含的文件，但不解压-p：显示将解压缩的结果-v：显示执行时的详细信息-n：解压缩时不要覆盖原有的文件-d 目录：指定文件解压缩后所要存储的目录

``````````
tar -cvf test.tar test    # 仅打包，不压缩 
  tar -xvf test.tar -C test # 解压 
  tar -tvf test.tar         # 查看，但不解压 
  
  tar -czvf test.tar.gz test    # 打包后，以 gzip 压缩 
  tar -xzvf test.tar.gz -C test # 解压 
  
  zip -r test.zip test 
  unzip test.zip -d test
``````````

 *  文件的删除rm 文件名rm -f 文件名rm -fr 目录：删除目录必须使用 -r 递归删除
 *  文件的拷贝和移动cp 文件名 目标路径：拷贝mv 文件名 目标路径：移动scp local\_file remote\_ip:remote\_dir\_file：Linux 之间复制文件和目录，-r 递归复制整个目录
 *  文本处理
    
     *  grep 命令：`grep [-abcEFGhHilLnqrsvVwxy] [-A <显示列数>] [-B <显示列数>] [-C <显示列数>] [-d <进行动作>] [-e <模式>] [-f <范本文件>] [—help] [模式] [文件或目录…]`查找文件中包含指定内容的所有行，-n（显示行数），-C 2（显示查询出来的内容的上下 2 行），-A 2（显示下 2 行），-B 2（显示上 2 行），-v（显示不包含匹配文本的所有行）
     *  sed 命令：`sed [-hnV] [-e <script>] [-f <script文件>] [文本文件]`利用脚本来处理、编辑文本文件主要用来自动编辑一个或多个文件、简化对文件的反复操作、编写转换程序等一种流编辑器，处理时，把当前处理的行存储在临时缓冲区中，称为“模式空间”（pattern space），接着用 sed 命令处理缓冲区中的内容，处理完成后，把缓冲区的内容送往屏幕。接着处理下一行，这样不断重复，直到文件末尾。文件内容并没有改变，除非你使用重定向存储输出。
     *  awk 命令：`awk [选项参数] 'script' var=value file(s)` 或 `awk [选项参数] -f scriptfile var=value file(s)`AWK 是一种处理文本文件的语言，也是一个强大的文本分析工具，用于对文本和数据进行处理数据可以来自标准输入、一个或多个文件，或其它命令的输出支持用户自定义函数和动态正则表达式等先进功能

## 网络通讯 ##

ifconfig：ip 查看service network restart：重启网络服务chkconfig iptables on：开启防火墙（重启后生效）chkconfig iptables off：关闭防火墙（重启后生效）service iptables restart：重启防火墙netstat -anp | grep 80：查看占用 80 端口的进程netstat -tulp：查看正在使用 Socket 的监控中的程序（包括 TCP、UDP 传输协议）ssh -i ~/.ssh/id\_rsa -p 23 root@192.168.1.237：使用 SSH 远程登录服务器

 *  永久放行 8080 端口：firewall-cmd —zone=public —add-port=8080/tcp —permanent
 *  运行、停止、禁用 firewalld启动：systemctl start firewalld查看状态：systemctl status firewalld 或者 firewall-cmd —state重启防火墙：firewall-cmd —reload禁用：systemctl disable firewalld停止：systemctl stop firewalld

## 系统管理 ##

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414659957.png) 
图 4 top命令

top：实时显示系统资源使用情况及进程信息（输入 M 按照内存排序，输入 P 按照 CPU 排序，输入T 按照占用 CPU 的时间排序）free -h：查看内存使用情况ps -ef | grep java：进程状态查看killall java：杀死名字为 java 的进程

 *  ps 命令，当前系统的进程状态，如 `ps -ef | grep java`、`ps aux | grep java`，常用选项：a：显示现行终端机下的所有程序，包括其他用户的程序u：以用户为主的格式来显示程序状况x：显示所有程序，不以终端机来区分-e：显示所有程序-f：显示 UID, PPIP, C 与 STIME 栏位

## 常用的配置文件 ##

 *  /etc/profile：环境变量配置文件
 *  /etc/sysconfig/iptables：防火墙的相关配置
 *  /etc/sysconfig/network-scripts/ifcfg-eth0：网卡配置文件
 *  /etc/inittab：系统运行级别配置文件
 *  /etc/rc.d/rc.local：配置系统开机启动相关命令

## bash shell ##

 *  bash 是 Bourne Again Shell 简称，是从 unx 系统中的 sh 发展而来，是用户和 Linux 內核交互的工具，用户通过 bash 操作内核完成系统的使用和管理
 *  多个指令执行的判断方法bash1&&bash2：前者执行成功才会执行后者bash1||bash2：前者执行完毕且失败才执行后者

### 变量 ###

 *  变量是代表一些值的符号
 *  Linux 有自定义变量(local) 和环境变量(environment)，自定义变量和环境变量作用范围不同：自定义变量只能在当前的 shell 环境中有效；环境变量会在整个主机下的 shell 环境中生效
 *  可以使用以下指令查看所有变量：set 可以查看所有变量；env 只能查看环境变量

#### 变量设值方式 ####

 *  通过自定义变量设置
    
     *  `VARIABLE=vaue` 来设置（变量内容如果有空格，必须使用双引号或者单引号）
     *  `echo $VARIABLE` 获取变量值
     *  变量名不能以数字或者特殊字符开头
 *  通过别名（Aliases）设置
    
     *  `alias Iss='ls -la'`
     *  使用 alias 自身查看所有的 alias
     *  使用 alias 和 alias 名称查看 alias 值
     *  unalias 删除别名定义
 *  通过环境变量设置：`export [variable name]`

### 输入/输出重定向 ###

 *  标准输入（stdin）是指令数据的输入，文件描述符为 0，使用 `<` 或者 `<<`，默认是键盘
 *  标准输出（stdout）是指令执行成功返回的结果，文件描述符为 `1，使用`>`或者`>\`，默认由屏幕显示
 *  标准错误输出（stderr）是指令执行失败返回的错误信息，文件描述符为 2，使用 `2>` 或者 `2>>`，默认是屏幕

#### 重定向输入/输出 ####

 *  <：指定输入的数据媒介来源
 *  1>：将 stdout 覆盖输出到指定的媒介
 *  1>>：将 stdout 追加到指定的媒介
 *  2>：将 stderr 覆盖输出到指定的媒介
 *  2>>：将 stderr 追加到指定的媒介
 *  2>&1：将 stderr 重定向到 stdout（`>`后面的 `&`，表示重定向的目标不是一个文件，而是一个文件描述符）
 *  command < file：将 stdin 重定向到 file
 *  command > file：将 stdout 重定向到 file
 *  command 2 > file：将 stderr 重定向到 file
 *  command >> file 2>&1：将 stdout 和 stderr 合并后以追加的方式重定向到 file

#### 管道命令 ####

 *  命令通过管道符 `|` 连接，用于将当前命令的输出结果作为下一个命令的参数
 *  能够接收标准输入（stdin），如 tail/more/grep 等
 *  能够接收来自于前一个指令的数据成为 stdin 进行处理
 *  grep 指令：`grep [-cinv] 'key' filename`，查找文件里符合条件的字符串，支持正则（-c 计算字符出现的个数；-i 忽略大小写进行查找；-n 输出行号；-v 显示不包含匹配文本的所有行）
 *  sort 指令：`sort [-fbknrtu] filename`，将文本文件内容加以排序，文本文件以行为单位，（-f 忽略大小写；-b 忽略最前面的空格；-M 以月份英文名字排序；-n 使用数值的大小排序；-r 逆向排序；-t 分隔符，默认是 tab 分割；-k 以第几列来排序）
 *  wc 指令：`wc [-lwm] filename`，统计（-l 统计行；-w 统计词；-m 统计字符）

## 其它常用命令 ##

 *  [curl 命令][curl]，利用 URL 规则在命令行下工作的文件传输工具，常用选项：-A：指定客户端的用户代理标头-v：显示详细信息，包含请求和响应的首部-o xxx：把输出写到 xxx 文件中-O：把输出写到文件中，保留远程文件的文件名-C：断点续转-x：ip:port 使用 HTTP 代理-c cookiec.txt：操作结束后把 cookie 写入到文件中-b cookiec.txt：cookie 字符串或文件读取位置-I：只显示请求头信息-X ：指定请求方式，如 `-X POST`\-G：以 get 方式来发送数据-d ：用于发送 post 请求的数据体-H  ：自定义头信息传递给服务器-F  ：模拟 http 表单提交数据-k：允许不使用证书到 SSL 站点-s：静默模式，不输出任何东西-S：显示错误-L：跟随重定向

``````````
curl myip.ipip.net 
  curl -H "Content-Type:application/json" -d '{"id":"123"}' http://localhost:8080/search -v 
  # GET 请求（省略 -G，会发出一个 POST 请求） 
  curl -G -d "name=value&name2=value2" http://www.baidu.com 
  # POST 请求 
  curl -d "name=value&name2=value2" http://www.baidu.com
``````````

 *  wget 命令：用来从指定的 URL 下载文件，常用选项：-O 文件名：以不同的文件名保存，`wget -O bench.sh http://bench.sh`\-c：继续执行上次终端的任务-q：不显示指令执行过程-b：进行后台的方式运行-v：显示详细执行过程-i filelist.txt：从指定文件获取要下载的 URL 地址—spider：测试下载链接
 *  xargs 命令：`somecommand | xargs -item command`xargs 是**给命令传递参数**的一个过滤器（捕获一个命令的输出，然后传递给另外一个命令），也**是组合多个命令**的一个工具xargs 可以将管道或标准输入（stdin）数据转换成命令行参数，也能够从文件的输出中读取数据xargs 也可以将单行或多行文本输入转换为其他格式，例如多行变单行，单行变多行xargs 默认的命令是 echo，这意味着通过管道传递给 xargs 的输入将会包含换行和空白，不过通过 xargs 的处理，换行和空白将被空格取代

 *  ps、grep、kill 联合使用杀掉进程：`ps -ef | grep spring-boot-demo.jar | grep -v grep | awk '{print $2}'| xargs kill -9`

``````````
#!/bin/bash 
  pid=`ps -ef | grep spring-boot-demo.jar | grep -v grep | awk '{print $2}'` 
  if [ -n "$pid" ] 
  then 
     kill -9 $pid 
  fi 
  echo "==== starting spring-boot-demo-helloworld application ====" 
  cd /home/springboot 
  nohup java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Dspring.profiles.active=prod spring-boot-demo.jar >> app.log 2>&1 & 
  echo "spring-boot-demo-helloworld application started." 
  
  # nohup COMMAND &：nohup 不挂断地运行，& 在后台运行 
  # -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005：远程调试
``````````

## 软件包管理器 ##

 *  安装程序的方式：
    
     *  通用二进制格式：直接解压压缩文件就可以使用
     *  软件包管理器：如 rpm
     *  软件包管理器的前端工具：如 yum
     *  源代码编译
 *  软件包的组成部分：
    
     *  二进制程序，位于 /bin, /sbin, /usr/bin, /usr/sbin, /usr/local/bin, /usr/local/sbin 等目录中
     *  库文件，位于 /lib, /usr/lib, /usr/local/lib 等目录中，Linux 中库文件以 .so（动态链接库）或 .a（静态链接库）作为文件后缀名
     *  配置文件，位于 /etc 目录中
     *  帮助文件：手册，README, INSTALL (/usr/share/doc/)

### 后端工具（rpm、dpt） ###

 *  rpm（RPM Package Manager）主要用于安装、升级、卸载、查询、校验和数据库管理
 *  dpt（Debian Packet Tool）
 *  rpm：安装，-ivh（显示安装进度和相关信息），—nodeps（安装时不检查依赖关系），—force（强制安装）
 *  rpm -q 包名（不要有后缀名）：查询
 *  rpm -qa：查询所有的安装的包，| grep pattern（搜索）
 *  rpm -Uvh 包名：更新
 *  rpm -e：卸载，-nodeps（忽略依赖关系）

### 前端管理工具：yum ###

 *  Yum（全称 Yellow Dog Updater）是一个在 Fedora 和 RedHat 以及 CentOS 中的 Shell 前端软件包管理器
 *  基于 RPM 包管理，能够从指定的服务器自动下载 RPM 包并且安装，可以自动处理依赖性关系，并且一次安装所有依赖的软件包
 *  本地配置：/etc/yum.repos.d/ 目录下的 \*.repo 文件

### 常用命令 ###

 *  yum repolist：查看当前能够使用的 yum 仓库列表，https://opsx.alibaba.com/mirror
 *  yum clean \[ packages | metadata | expire-cache | rpmdb | plugins | all \]：清空本地 yum 的缓存
 *  yum info 包名：显示软件包的摘要信息
 *  yum install 包名或rpm包路径 -y：安装
 *  yum list | grep 包名：查看仓库中的软件包
 *  yum list installed | grep 包名：查看已安装的软件
 *  yum list updates：查看所有可更新的包
 *  yum update 包1 包2 … 包n -y：更新
 *  yum remove 包1 包2 … 包n -y：卸载

``````````
# 安装 Git 
  yum -y install git-core 
  
  # 使用 nvm 安装 Node.js 
  curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | sh 
  # wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | sh 
  nvm install node 
  
  # 使用 yum 安装 Node.js 
  curl -sL https://rpm.nodesource.com/setup_10.x | bash - 
  yum install -y nodejs
``````````

### yum 的卸载与安装 ###

 *  卸载 yum：`rpm -aq|grep yum|xargs rpm -e —nodeps`
 *  下载 yum 相关安装包

``````````
wget http://mirrors.aliyun.com/centos/7/os/x86_64/Packages/yum-3.4.3-163.el7.centos.noarch.rpm 
  wget http://mirrors.aliyun.com/centos/7/os/x86_64/Packages/yum-metadata-parser-1.1.4-10.el7.x86_64.rpm 
  wget http://mirrors.aliyun.com/centos/7/os/x86_64/Packages/yum-plugin-fastestmirror-1.1.31-52.el7.noarch.rpm 
  # wget http://mirrors.aliyun.com/centos/7/os/x86_64/Packages/python-iniparse-0.4-9.el7.noarch.rpm
``````````

 *  安装 yum 包：`rpm -ivh *.rpm`
 *  配置 yum
    
     *  备份：`mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup`
     *  下载新的 CentOS-Base.repo 到 /etc/yum.repos.d/`curl -o /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo``curl -o /etc/yum.repos.d/CentOS-Base.repo https://qst123.oss-cn-beijing.aliyuncs.com/SETUP/repo/CentOS-Base.repo`（阿里云内网）
 *  安装 epel
    
     *  EPEL（Extra Packages for Enterprise Linux）是由 Fedora 社区打造，为 RHEL 及衍生发行版如 CentOS 等提供高质量软件包的项目

``````````
yum install epel-release 
  curl -o /etc/yum.repos.d/epel.repo http://mirrors.aliyun.com/repo/epel-7.repo 
  curl -o /etc/yum.repos.d/epel.repo https://qst123.oss-cn-beijing.aliyuncs.com/SETUP/repo/epel.repo # 阿里云内网
``````````

# Linux 应用 #

## JDK 的安装 ##

 *  检查是否安装 jdk：rpm -qa | grep jdk 或 yum list installed | grep jdk如果有就先卸载：rpm -e —nodeps xxxx
 *  上传 tar.gz 压缩文件到 soft
 *  解压文件：tar -zxvf xxxxx
 *  移动到推荐的目录 /usr/local/：mv jdk1.8.0\_151 /usr/local/opensource/jdk1.8
 *  配置环境变量 /etc/profile
    
     *  先备份文件：cp /etc/profile /etc/profile.bak
     *  编辑 profile 文件，在最后插入内容 JAVA\_HOME=/usr/local/opensource/jdk1.8 PATH=$JAVA\_HOME/bin:$PATH export JAVA\_HOME PATH
     *  重启系统或者重新加载配置文件（只对当前的终端有效）：source /etc/profile

## Tomcat 的安装 ##

 *  解压文件：tar -zxvf xxxxx
 *  移动到推荐的目录 /usr/local/：mv tomcatxxx /usr/local/opensource/tomcat8
 *  启动 Tomcat：进入 bin 目录，运行 bash startup.sh 或者 ./startup.sh
 *  检查是否启动成功，查看 log 日志：tail -100f catalina.out 或 grep Exception -n -C 5 catalina.out
 *  本地浏览器访问：http://localhost:8080 外面 ip 地址访问：http://192.168.xx.xx:8080
 *  如果外面访问不了，则需要配置防火墙文件，添加 8080 端口

## MySQL 的安装 ##

 *  检查是否安装 MySQL 相关：rpm -qa | grep mysql 或者 yum list installed |grep mysql如果有就先卸载：rpm -e —nodeps xxx 或者 yum -y remove xxxxxCentOS 7 需先卸载 mariadb
 *  把安装文件 common、libs、client、server 上传到 /soft 目录安装 MySQL：rpm -ivh MySQL\*.rpm
 *  检查安装结果：rpm -qa | grep mysql
 *  启动 MySQL 服务：service mysql start
 *  连接 MySQL，设置远程登录用户

``````````
mysql> grant all privileges on \*.* to 'root'@'%' identified by 'admin' with grant option; -- 远程用户授权  
  mysql> flush privileges; -- 刷新缓存，重载授权表
``````````

 *  重启 MySQL 服务器：service mysql restart
 *  设置防火墙开放 3306 端口
 *  通过客户端连接，删除多余的客户（只保留 root@%）cp /usr/share/mysql/my-medium.cnf /etc/my.cnf
 *  注意：在 Linux 系统中，MySQL 对于表是默认区分大小写的，`show variables like "%case%";`可在配置文件中 vi /etc/my.cnf 添加参数\[mysqld\]lower\_case\_table\_names=1

## 定时任务 Crontab ##

 *  检查是否安装 crontab：yum list installed | grep crontab
 *  查看 yum 仓库是否有该安装包：yum list | grep crontab
 *  安装 crontab：yum -y install crontabs.noarch
 *  编辑定时任务：crontab -e\*/1 /home/dbbackup/dbbackup.sh
 *  查看定时任务：crontab -l
 *  重启 Linux 或者处理服务
 *  服务操作说明：/sbin/service crond start：启动服务/sbin/service crond stop：关闭服务/sbin/service crond restart：重启服务/sbin/service crond reload：重新载入配置
 *  查看 crontab 服务状态：service crond status
 *  注意 sh 脚本要有 x 执行的权限：chmod +x filename
 *  数据库定时备份每天晚上凌晨 2 点 30 分开始对数据库进行数据备份到 /backup/ 目录下，并且只保留 30 天的备份数据

``````````
1. 编写 wmsbackup.sh 脚本文件 
  #!/bin/bash 
  find /root/backup/ -mtime +30 -name '*.sql' -exec rm -fr '{}' \; 
  DATE=$(date +%Y%m%d-%H:%M) 
  mysqldump -uroot -padmin wms>/root/backup/wms$DATE.sql 
  
  2. 定时运行脚本文件 
  crontab -e 
  30 2 * * * /root/backup/wmsbackup.sh
``````````

## 进程守护监控 Supervisor ##

## lrzsz ##

 *  安装：yum -y install lrzsz
 *  将选定的文件发送（send）到本地：sz filename
 *  从本地选择文件上传到服务器（receive）：rz，或者直接拖拽要上传的文件
 *  需使用支持 Zmodem 协议 的 telnet/ssh 客户端；lrzsz 只适合传输小文件

## ab ##

 *  ApacheBench，Apache 的 We b服务器的性能测试工具
 *  安装：yum -y install httpd-tools
 *  常用参数：-c：指定一次向服务器发出请求数-n：指定测试会话使用的请求数
 *  `ab -c 10 -n 1 http://www.baidu.com/`


[free_software_licenses]: https://static.sitestack.cn/projects/sdky-java-note/b20e36d6a1822dbdffc323c537cc14f6.jpeg
[Linux]: https://static.sitestack.cn/projects/sdky-java-note/86b140bd2497549f938c78c6e0612bd7.png
[Vim_s_modes]: https://static.sitestack.cn/projects/sdky-java-note/79a37634d540e855d92c33b6f708bcc1.jpeg
[top]: https://static.sitestack.cn/projects/sdky-java-note/7c25eb91551289c4ab65101e64f1e097.png
[curl]: https://curl.haxx.se/docs/manpage.html