# Maven #

 *  **项目管理工具**，可以对 Java 项目进行构建、依赖管理

### 安装 ###

 *  配置环境变量：JAVA\_HOME、MAVEN\_HOME
 *  将 %JAVA\_HOME%\\bin、%MAVEN\_HOME%\\bin 路径添加到操作系统的 PATH 环境变量中

### 设置 settings. xml ###

 * 通过 ~/.m2/ 目录下（ ~ 表示用户目录）的 settings.xml 文件进行设置

*  <localRepository>：用于设置本地仓库的路径，如 E:\repository

*  配置远程仓库的镜像 [Using Mirrors for Repositories](http://maven.apache.org/guides/mini/guide-mirror-settings.html)，在 `<mirrors>` 标签中添加 mirror 子节点:

 ```xml
     <mirror>
         <id>aliyun-maven</id> <!-- 该镜像的唯一标识符，用来区分不同的镜像 -->
         <name>Nexus aliyun</name>
         <url>https://maven.aliyun.com/repository/public</url>
         <mirrorOf>central</mirrorOf> <!-- 被镜像的远程仓库的 id，任何对于该远程仓库的请求都会转至该镜像 -->
     </mirror>
     <mirror>
         <id>central-repository</id>
         <name>Central Repository</name>
         <url>https://repo.maven.apache.org/maven2</url>
         <mirrorOf>central</mirrorOf> <!-- 根据 id 值的字母排序，当前一个 mirror 无法连接时，才会去找后一个 -->
     </mirror>
 ```

> Note that there can be at most one mirror for a given repository. In other words, you cannot map a single repository to a group of mirrors that all define the same `<mirrorOf>` value. Maven will not aggregate the mirrors but simply picks the first match. If you want to provide a combined view of several repositories, use a [repository manager (opens new window)](http://maven.apache.org/repository-management.html)instead.

   - 如果想使用其它代理仓库，可在 ` 节点中加入对应的仓库使用地址。以使用 spring 代理仓为例：

```xml
     <repository>
         <id>spring</id>
         <url>https://maven.aliyun.com/repository/spring</url>
         <releases>
             <enabled>true</enabled>
         </releases>
         <snapshots>
             <enabled>true</enabled>
         </snapshots>
     </repository>
```

### pom.xm 文件 ###

 *  pom.xm 文件被称为项目对象模型（Project Object Model）描述文件
*  超级 POM 位置：$M2_HOME/lib/maven-model-builder-x.x.x.jar 中的 org/apache/maven/model/pom-4.0.0.xml，任何一个 Maven 项目都隐式地继承该 POM
*  <modelVersion>：声明项目描述符遵循哪一个 POM 模型版本
*  <parent>：指定继承的父模块的坐标，子标签 <groupId>、<artifactId>、<version>、<relativePath>（父 pom 的相对路径，设定一个空值将始终从仓库中获取，不从本地路径获取）
*  <groupId>、<artifactId>、<version>、<packaging> 定义了该项目的唯一标识，这个唯一标识被称为 Maven 坐标
*  <name>、<url> 是 pom.xml 提供的描述性元素
*  <modules> <module>：定义被**聚合**的模块
*  <properties>：自定义 Maven 属性，如 `<java.version>1.8</java.version>` 定义后， Maven 运行时会将 POM 中的所有的 `${java.version}` 替换成实际值 1.8
*  <dependencyManagement> <dependencies>：定义依赖管理

>   说明：<dependencyManagement> 里**只是声明版本，并不实现引入**，因此子项目需要显式的声明依赖，version 和 scope 都读取自父 pom；而 <dependencies> 所有声明在主 pom 的 <dependencies> 里的依赖都会**自动引入**，并默认被所有的子项目**继承**
> 

 *  <dependencies> ：定义依赖关系
    - <dependency>：依赖管理
       - <groupId>：指定依赖库所属的组织 ID
       - <artifactId>：指定依赖库的项目名
       - <version>：指定依赖库的版本号
       - <scope>：指定依赖库起作用的范围，内容可以为：
         - compile：编译依赖范围，对于编译、测试，运行三种 classpath 都有效（无指定时**默认**使用该依赖范围）
         - test：测试依赖范围，只对于测试 classpath 有效
         - provided：已提供依赖范围，对于编译和测试 classpath 有效，但在运行时无效
         - runtime：运行时依赖范围，对于测试和运行 classpath 有效，但在编译主代码时无效
         - system：系统依赖范围，对于编译和测试 classpath 有效，但在运行时无效，必须通过 systemPath 元素显式地指定依赖文件的路径
         - import：导入依赖范围，继承父 POM 文件中用 dependencyManagement 配置的依赖
       - <optional>：指定该依赖库是否为可选的，true 表示可选依赖**不会被传递**，不会被其它项目继承，默认 false
       - <classifier>：JDK 版本号
       - <exclusions>：用于排除依赖，子标签 <exclusion>
*  <build>：定义构建信息
     - <pluginMariagenient> <plugins>：定义插件管理
     - <plugins>
       - <plugin>，子标签 <groupId>、<artifactId>、<version>、<configuration>
*  <repositories>：定义一个或者多个依赖的远程仓库
     - <repository>：子标签 <id>、<name>、<url>、<releases>、<snapshots>
*  <pluginRepositories>：定义一个或者多个插件的远程仓库
     - <pluginRepository>：子标签 <id>、<name>、<url>、<releases>、<snapshots>
*  <distributionManagement>：定义部署的远程仓库
     - <repository>：定义发布版本构件的部署仓库，子标签：
       - <id>：该远程仓库的唯一标识，需要与 setting.xml 中 server 元素的 id 匹配
       - <name>：为了方便阅读
       - <url>
     - <snapshotRepository>：定义快照版本构件的部署仓库

### Maven 的主要约定 ###

 *  源代码应该位于 $\{basedir\}/src/main/java 路径下
 *  资源文件应该位于 $\{basedir\}/src/main/resources 路径下
 *  测试代码应该位于 $\{basedir\}/src/test 路径下
 *  编译生成的 class 文件应该位于 $\{basedir\}/target/classes 路径下
 *  项目打包后会产生一个 jar文件，并将生成的 JAR 包放在 $\{basedir\}/target 路径下  
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/blog/2023/04/25/xxf-20230425173324.jpeg?xxfjava)   
    图 1 Maven项目的文件结构

### Maven 默认的生命周期（lifecycle）的核心阶段 ###

 *  compile：编译项目
 *  test：单元测试
 *  package：项目打包
 *  install：安装到本地仓库
 *  deploy：部署到远程仓库

### Maven 的坐标（coordinate） ###

 *  groupld：该项目的开发者的域名
 *  artifactld：指定项目名
 *  version：指定项目的版本
 *  packaging：指定项目打包的类型，可用值：jar（默认）、war、pom

### Maven 的仓库（repository） ###

 * id、name、url

*  仓库分类：本地仓库、远程仓库（中央仓库 central、私服、其它公共库）

   > 中央仓库是 Maven 核心自带的远程仓库，在默认配置下，当本地仓库没有 Maven 需要的构件的时候，就会尝试从中央仓库下载
   >
   > Maven 自带的中央仓库的 id 为 **central**（见超级 POM），如果其他的仓库声明也使用该 id，会覆盖中央仓库的配置

*  镜像：由于镜像仓库**完全屏蔽了被镜像仓库**，当镜像仓库无法连接时，Maven 将无法下载构件

*  配罝认证信息（必须配置在 settings.xml 文件中）

   ```xml
   <settings>
       <servers>
           <!-- 配置 Nexus 的认证信息 -->
           <server>
               <!-- server 元素的 id 必须与需要 repository 元素的 id 一致，或与 mirror 元素的 id 一致-->
               <id>nexus</id>
               <username>admin</username>
               <password>admin123</password>
           </server>
       </servers>
   </settings>
   ```

*  配置仓库信息：

   1. 方式一，在项目的 pom.xml 中配置 <repositories>、<pluginRepositories>
   2. 方式二，在 settings.xml 中配置 Nexus 仓库

   ```xml
   <!-- 只使用私服，即 Maven 对任何仓库的构件下载请求都转到私服 -->
   <settings>
       <mirrors>
           <!-- 创建一个匹配任何仓库的镜像，镜像的地址为私服 -->
           <!-- <mirrorOf> 值为 *，表示该配置是所有仓库的镜像，任何对于远程仓库的请求都会被转至该镜像的 url -->
           <mirror>
               <id>nexus</id>
               <url>http://localhost:8081/nexus/content/groups/public</url>
               <mirrorOf>*</mirrorOf>
           </mirror>
       </mirrors>
   
       <profiles>
           <!-- settings 中激活的 profile 的值会覆盖任何其它定义在 POM 中或者 profile.xml 中的带有相同 id 的 profile -->
           <profile>
               <id>nexus</id>
               <!-- 使用 repositories 声明一个或者多个远程库-->
               <repositories>
                   <repository>
                       <!-- id 为 central，覆盖超级 POM 中央仓库的配置 -->
                       <id>central</id>
                       <!-- 此时，url 已无关紧要 -->
                       <url>http://central</url>
                       <releases><enabled>true</enabled></releases>
                       <snapshots>
                           <!-- 开启快照版本的下载支持 -->
                           <enabled>true</enabled>
                           <!-- 配置从远程仓库检查更新的频率 -->
                           <!-- 选项是：always（每次构建），daily（默认，每日），interval: X（这里 X 是以分钟为单位的时间间隔），或者 never（从不） -->
                           <updatePolicy>always</updatePolicy>
                       </snapshots>
                   </repository>
               </repositories>
               <!--<pluginRepositories>
                   <pluginRepository>
                       <id>central</id>
                       <url>http://central</url>
                       <releases><enabled>true</enabled></releases>
                       <snapshots><enabled>true</enabled></snapshots>
                   </pluginRepository>
               </pluginRepositories>-->
           </profile>
   
       </profiles>
   
       <activeProfiles>
           <activeProfile>nexus</activeProfile>
       </activeProfiles>
   </settings>
   ```

   ```xml
   <!-- 同时使用中央仓库、私服 -->
   <settings>
       <profiles>
           <profile>
               <id>nexus</id>
               <repositories>
                   <repository>
                       <id>nexus</id>
                       <name>Nexus</name>
                       <url>http://localhost:8081/nexus/content/groups/public</url>
                       <releases><enabled>true</enabled></releases>
                       <snapshots>
                           <enabled>true</enabled>
                           <updatePolicy>always</updatePolicy>
                       </snapshots>
                   </repository>
               </repositories>
               <!--<pluginRepositories>
                   <pluginRepository>
                       <id>nexus</id>
                       <name>Nexus</name>
                       <url>http://localhost:8081/nexus/content/groups/public</url>
                       <releases><enabled>true</enabled></releases>
                       <snapshots><enabled>true</enabled></snapshots>
                   </pluginRepository>
               </pluginRepositories>-->
           </profile>
   
       </profiles>
   
       <activeProfiles>
           <activeProfile>nexus</activeProfile>
       </activeProfiles>
   </settings>
   ```

### Maven 依赖调解 ###

Maven 依赖调解（Dependency Mediation）的原则：

 *  第一原则：路径最近者优先
 *  第二原则：第一声明者优先。在依赖路径长度相等的前提下，在 POM 中依赖声明的顺序决定了谁会被解析使用，顺序最靠前的那个依赖优胜

### Maven 常用命令 ###

 *  mvn 命令的基本用法：`mvn [options] [<goal(s)>] [<phase(s)>]`，options 表示可用的选项，goal 指插件目标，phase 指生命周期阶段
 *  `mvn –v` // 显示版本信息
 *  `mvn compile` // 编译
 *  `mvn clean` // 清空生成的文件
 *  `mvn clean compile`
 *  `mvn clean test` // 编译并测试
 *  `mvn clean package -Dmaven.test.skip=true` // 打包
 *  `mvn clean site` // 生成项目相关信息的网站
 *  `mvn install` // 在本地 Repository 中安装 jar
 *  `mvn install -Dmaven.test.skip=true -Pdev`// 跳过测试，激活 id 为 dev 的 profile
 *  `mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc14 -Dversion=10.2.0.2.0 -Dpackaging=jar -Dfile=E:\ojdbc14-10.2.0.2.0.jar` // 安装 jar 包到本地仓库
 *  `mvn dependency:list` // 査看当前项目的已解析依赖
 *  `mvn dependency:tree` // 査看当前项目的依赖树
 *  `mvn dependency:analyze` // 分析当前项目的依赖，分析结果包含 `Used undeclared dependencies found`（项目中使用到但是没有显式声明的依赖）和 `Unused declared dependencies found`（项目中未使用但显式声明的依赖）
 *  `mvn idea:idea` // Maven 项目生成 idea 项目文件
 *  `mvn idea:clean` // 清除 idea 的项目工程文件
 *  `mvn eclipse:eclipse` // Maven 项目生成 eclipse 项目文件

### Maven 属性 ###

 *  内置属性：`${basedir}` 表示项目根目录（即包含 pom.xml 文件的目录）；`${version}` 表示项目版本
 *  POM 属性：引用 POM 文件中对应元素的值，如 `${project.groupId}`、`${project.artifactId}`、`${project.version}` 分别对应项目的 groupId、artifactId、version
 *  自定义属性：在 POM 的  元素下自定义 Maven 属性
 *  Setting 属性：使用以 settings. 开头的属性引用 settings.xml 文件中 XML 元素的值，如 `${settings.localRepository}` 指向用户本地仓库的地址
 *  Java 系统属性：引用 Java 系统属性，如 `${user.home}`
 *  环境变量属性：使用以 env. 开头的属性引用环境变量，如 `${env.JAVA_HOME}`  
    

### 资源过滤 ###

 *  针对不同环境的 profile
~~~xml
    <profiles> 
           <profile> 
               <id>dev</id> 
               <!-- 定义 Maven 属性 --> 
               <properties> 
                   <activatedProperties>dev</activatedProperties> 
               </properties> 
               <activation> 
                   <activeByDefault>true</activeByDefault> 
               </activation> 
           </profile> 
           <profile> 
               <id>prod</id> 
               <properties> 
                   <activatedProperties>prod</activatedProperties> 
               </properties> 
           </profile> 
       </profiles> 
    
       <build> 
           <!-- 主资源文件由 maven-resources-plugin 处理 --> 
           <resources> 
               <!-- 定义资源 --> 
               <resource> 
                   <!-- 指定资源所在目录 --> 
                   <directory>src/main/resources</directory> 
                   <!-- 指定资源文件：所有 properties 文件 --> 
                   <includes> 
                       <include>**/*.properties</include> 
                   </includes> 
                   <!-- 对资源文件开启过滤，解析资源文件中的 Maven 属性 --> 
                   <filtering>true</filtering> 
               </resource> 
               <!-- 定义资源二 --> 
               <resource> 
                   <directory>src/main/resources</directory> 
                   <excludes> 
                       <exclude>**/*.properties</exclude> 
                   </excludes> 
                   <filtering>false</filtering> 
               </resource> 
           </resources> 
       </build>
~~~
 *  在资源文件中使用 Maven 属性 `${activateProperties}`如果项目的 pom 已经继承 spring-boot-starter-parent，则占位符 `${..}` 被替换为 `@..@`，即使用方式为 `@activateProperties@`（可通过 [maven-resources-plugin][] 插件的  属性自定义）
 *  激活 profile
    
     *  命令行激活，多个 id 之间以逗号分隔 `mvn install -Pdev`
     *  settings 文件显式激活，配置 settings xml 文件的  元素
     *  系统属性激活：当某系统属性存在的时候，自动激活 profile，定义  中的  属性
     *  操作系统环境激活：自动根据操作系统环境激活，定义  中的  属性
     *  文件存在与否激活：根据项目中某个文件存在与否来决定是否激活，定义  中的  属性
     *  默认激活，定义  中的  属性

### Maven 多模块 ###

#### maven 多个子模块项目管理 ####

```xml
<groupId>com.example.account</groupId>
<artifactId>account-parent</artifactId>
<version>1.0.0-SNAPSHOT</version>
<packaging>pom</packaging>
<name>Account Parent</name>

<modules>
    <module>account-email</module>
    <module>account-persist</module>
</modules>
```



```xml
<parent>
    <groupId>com.example.account</groupId>
    <artifactId>account-parent</artifactId>
    <!-- 版本升级需要修改每个子模块 parent.version 的值 -->
    <version>1.0.0-SNAPSHOT</version>
</parent>
<artifactId>account-email</artifactId>
<name>Account Email</name>
```

 *  主项目 parent 包
    
     *  指定整个应用的 dependencyManagement
     *  定义项目的发布的仓库地址 distributionManagement
     *  所有第三方依赖的版本号全部定义在 properties 下
     *  所有内部模块依赖版本号统一使用 `${project.version}`
     *  指定所有的子模块 modules
 *  项目子模块 pom.xml
    
     *  明确定义 parent 模块的 groupId、artifactId、version
     *  **不要定义子模块的 version**（同 parent 保持一致）
     *  子模块无需定义 groupId
     *  子模块所有的依赖包版本全部集成 parent 模块，即：子模块不得定义依赖包版本号
     *  子模块需定义是否需要 deploy 到私服 `<maven.deploy.skip>true</maven.deploy.skip>`
     *  对于需要 depoly 的子模块（对外发布的，比如 dubbo 提供的 api 包）不应该依赖重量级 jar 包（比如 spring, mybatis 等）
 *  子模块 packaging 为 pom
    
     *  明确定义 parent 模块的 groupId、artifactId、version
     *  指定所有的子模块 modules
     *  无需定义 groupId

#### 多个子模块项目版本号修改 ####

[versions-maven-plugin](https://www.mojohaus.org/versions-maven-plugin/usage.html)

1. 在项目顶层 pom 中添加插件 org.codehaus.mojo:versions-maven-plugin

   ```xml
   <!-- 项目版本号修改插件 -->
   <!-- 执行命令：mvn versions:set -DnewVersion=1.0.0-SNAPSHOT -->
   <plugin>
       <groupId>org.codehaus.mojo</groupId>
       <artifactId>versions-maven-plugin</artifactId>
       <version>2.11.0</version>
       <configuration>
           <!-- 是否生成 .versionsBackup 备份文件 -->
           <generateBackupPoms>false</generateBackupPoms>
       </configuration>
   </plugin>
   ```

2. 在项目根目录下执行以下命令修改版本号 `mvn versions:set -DnewVersion=1.1.0`

- 在一个多模块的 Maven 项目中，反应堆（Reactor）是指所有模块组成的一个构建结构
- 对于单模块的项目，反应堆就是该模块本身，但对于多模块项目来说，反应堆就包含了各模块之间继承与依赖的关系，从而能够自动计算出合理的模块构建顺序
- 反应堆的构建顺序：Maven 按序读取 POM，如果该 POM 没有依赖模块，那么就构建该模块，否则就先构建其依赖模块，如果该依赖还依赖于其它模块，则进一步先构建依赖的依赖
- 用于裁剪反应堆的命令选项：
  - -am，--aIso-make，同时构建所列模块的依赖模块
  - -amd，---also-make-dependents，同时构建依赖于所列模块的模块
  - -pl，--projects <arg>，构建指定的模块，模块间用逗号分隔
  - -rf，--resume-from <arg>，在完整的反应堆构建顺序基础上指定从哪个模块开始构建（从指定的模块恢复反应堆）
  - 如 `mvn clean package -pl account-email,account-persist -am -Dmaven.test.skip=true`

- mvnw，全名是 Maven Wrapper，通过在 maven-wrapper.properties 文件中记录要使用的 Maven 版本，当用户执行 `mvnw clean` 命令时，发现当前用户的 Maven 版本和期望的版本不一致，那么就下载期望的版本，然后用期望的版本来执行 mvn 命令，比如 `mvn clean`
- 为项目添加 mvnw 支持：`mvn -N io.takari:maven:wrapper -Dmaven=3.3.3`
- 添加 Maven Wrapper 后，项目内会生成 .mvn 目录和 mvnw、mvnw.cmd 文件

# Gradle #

 *  基于 Apache Ant 和 Apache Maven 概念的**项目自动化构建**开源工具
 *  使用一种基于 Groovy 的特定领域语言（DSL）来声明项目设置

### 安装 ###

 *  配置环境变量：JAVA\_HOME、GRADLE\_HOME、GRADLE\_USER\_HOME
 *  将 %JAVA\_HOME%\\bin、%GRADLE\_HOME%\\bin 路径添加到操作系统的 PATH 环境变量中
 *  配置远程仓库：在 GRADLE\_USER\_HOME 目录下添加 init.gradle 文件
~~~xml
    allprojects {
        repositories {
            mavenLocal()
            // maven { url 'file:///E:/repository/'}
            maven { name "AliyunNexus"; url "https://maven.aliyun.com/repository/public" }
            mavenCentral()
        }
    
        buildscript {
            repositories {
                maven { name "AliyunNexus"; url 'https://maven.aliyun.com/repository/public' }
                maven { name "M2"; url 'https://plugins.gradle.org/m2' }
            }
        }
        
        task showRepositories {
            doLast {
                repositories.each {
                    println "repository: ${it.name} ('${it.url}')"
                }
            }
        }
    }
~~~
### 依赖管理 ###

### 常用插件 ###


[Using Mirrors for Repositories]: http://maven.apache.org/guides/mini/guide-mirror-settings.html
[Maven]: https://static.sitestack.cn/projects/sdky-java-note/1e2d07e01ddcf25fa67bb92f8cbeefd7.jpeg
[maven-resources-plugin]: https://maven.apache.org/plugins/maven-resources-plugin/resources-mojo.html#delimiters