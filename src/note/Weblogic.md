### Tomcat迁移weblogic的填坑之旅

在web.xml同级目录添加weblogic.xml文件配置
* 解决jar包冲突
~~~
<container-descriptor>
        <!-- 设置为true会优先加载项目配置，但启动报错，暂未找到原因，故设为false，手动添加优先加载的jar包 -->
        <prefer-web-inf-classes>false</prefer-web-inf-classes>
        <!--<prefer-application-packages>-->
            <!--<package-name>org.springframework.ws.*</package-name>-->
            <!--<package-name>org.joda.time.*</package-name>-->
            <!--<package-name>javassist</package-name>-->
            <!--<package-name>org.hibernate.*</package-name>-->
            <!--<package-name>javax.persistence.*</package-name>-->
            <!--<package-name>antlr.*</package-name>-->
        <!--</prefer-application-packages>-->

        <prefer-application-packages>

            <package-name>org.apache.commons.io.*</package-name>
            <package-name>org.springframework.*</package-name>
            <package-name>javax.validation.*</package-name>
            <package-name>javax.validation.bootstrap.*</package-name>
            <package-name>javax.validation.constraints.*</package-name>
            <package-name>javax.validation.groups.*</package-name>
            <package-name>javax.validation.metadata.*</package-name>
            <package-name>javax.validation.spi.*</package-name>
            <package-name>javax.validation.spi.*</package-name>
            <package-name>javassist</package-name>
            <package-name>javax.persistence.*</package-name>
            <package-name>org.hibernate.*</package-name>
            <package-name>hibernate-jpa-2.1-api</package-name>
            <package-name>org.joda.time.*</package-name>
        </prefer-application-packages>

        <prefer-application-resources>
            <resource-name>javax.faces.*</resource-name>
            <resource-name>org.apache.taglibs.*</resource-name>
            <resource-name>org.springframework.*</resource-name>
            <resource-name>javax.persistence.*</resource-name>
            <resource-name>javax.validation.*</resource-name>
            <resource-name>org.hibernate.*</resource-name>
            <resource-name>org.joda.time.*</resource-name>
            <resource-name>org.apache.commons.io.*</resource-name>
        </prefer-application-resources>
        <show-archived-real-path-enabled>true</show-archived-real-path-enabled>
    </container-descriptor>
~~~

* 配置虚拟路径，映射
~~~
<virtual-directory-mapping>
    <local-path>/weblogic/application/railwayWebsite/</local-path>
    <url-pattern>/upload/*</url-pattern>
</virtual-directory-mapping>
~~~

* 对应tomcat的配置为：
~~~
<Content docBase="/data/railwaywebsite_tomcat7/webapps/railwayWebsite/" path="/" reloadable="true" />
~~~

* 百度富文本（ueditor）上传图片附件失效
~~~
1、weblogic与jsp发生冲突，导致controller.jsp报错
<%@ page trimDirectiveWhitespaces="true" %>
2、后台ConfigManager配置在weblogic服务器上找不到路径，修改ConfigManager.java的文件目录配置
private ConfigManager ( String rootPath, String contextPath, String uri ) throws FileNotFoundException, IOException {
    rootPath = rootPath.replace( "\\", "/" );
    this.contextPath = contextPath;
    if ( contextPath.length() > 0 ) {
        if((rootPath.endsWith(contextPath)) || (rootPath.endsWith(contextPath + "/"))){
            this.rootPath = rootPath.substring(0, rootPath.length() - contextPath.length());
            this.originalPath = (this.rootPath + uri);
        }else {
            this.rootPath = rootPath;
            this.originalPath = (this.rootPath + uri.replace(contextPath, ""));
        }
    } else {
        this.rootPath = rootPath;
        this.originalPath = this.rootPath + uri;
    }
    this.initEnv();
}
~~~