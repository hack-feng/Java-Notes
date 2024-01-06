## 下载

下载地址：http://maven.apache.org/download.cgi。下载maven文件并解压。

![img](https://image.xiaoxiaofeng.site/article/img/2022/08/11/xxf-20220811204018.png)

## 配置环境变量

编辑.bash_profile文件：

```sh
vim ~/.bash_profile
```

配置maven文件地址：

```sh
export M2_HOME=/Users/xxx/Documents/maven/apache-maven-3.6.1
export PATH=$PATH:$M2_HOME/bin
```

保存文件，执行如下命令使配置生效：

```sh
source ~/.bash_profile
```

## 验证

输入：

```sh
mvn -v
```

得到：

![image-20220811204037627](https://image.xiaoxiaofeng.site/article/img/2022/08/11/xxf-20220811204039.png)

证明配置成功。



## 修改Maven镜像仓库

可以修改镜像的位置，默认的镜像下载速度很慢，可以修改成阿里云的镜像

修改conf下的配置文件settings.xml，找到`<mirror></mirror>`标签

![image-20220811204341100](https://image.xiaoxiaofeng.site/article/img/2022/08/11/xxf-20220811204350.png)

~~~xml
    <mirror>
      <!--This sends everything else to /public -->
      <id>nexus</id>
      <mirrorOf>*</mirrorOf> 
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    </mirror>
    <mirror>
      <!--This is used to direct the public snapshots repo in the 
          profile below over to a different nexus group -->
      <id>nexus-public-snapshots</id>
      <mirrorOf>public-snapshots</mirrorOf> 
      <url>http://maven.aliyun.com/nexus/content/repositories/snapshots/</url>
    </mirror>
~~~

具体的镜像下载地址看各自需要。

## 修改本地存储仓库

可以更换仓库位置，仓库是我们通过maven下载jar包存放的地方

找到下图中的标签，修改到自己需要的目录

`<localRepository>/path/to/local/repo</localRepository>`

![image-20220811204615910](https://image.xiaoxiaofeng.site/article/img/2022/08/11/xxf-20220811204623.png)