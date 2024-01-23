## 1. 项目背景

唉！本文写起来都是泪点。不是刻意写的本文，主要是对日常用到的文件上传做了一个汇总总结，同时希望可以给用到的小伙伴带来一点帮助吧。

* 上传本地，这个就不水了，基本做技术的都用到过吧；

* 阿里云OSS，阿里云是业界巨鳄了吧，用到的人肯定不少吧，不过博主好久不用了，简单记录下；
* 华为云OBS，工作需要，也简单记录下吧；
* 七牛云，个人网站最开始使用的图床，目的是为了白嫖10G文件存储。后来网站了升级了https域名，七牛云免费只支持http，https域名加速是收费的。https域名的网站在谷歌上请求图片时会强制升级为https。
* 又拍云，个人网站目前在用的图床，加入了又拍云联盟，网站底部挂链接，算是推广合作模式吧（对我这种不介意的来说就是白嫖）。

还有腾讯云等等云，暂没用过，就先不整理了，使用都很简单，SDK文档很全，也很简单。

## 2. 上传思路

分为两点来说。本文的精华也都在这里了，统一思想。

### 2.1 前端调用上传文件

前端上传的话，应该是我们常用的吧，通过`@RequestParam(value = "file") MultipartFile file`接收，然后转为`InputStream` or `byte[]` or `File`，然后调用上传就可以了，核心也就在这，很简单的，尤其上传到云服务器，装载好配置后，直接调用SDK接口即可。

### 2.2 通过url地址上传网络文件

通过url上传应该很少用到吧，使用场景呢，例如爬取文章的时候，把网络图片上传到自己的图床；图片库根据url地址迁移。

说到这，突然想起了一个问题，大家写文章的时候，图片上传到图床后在文章内是怎么保存的呢？是全路径还是怎么保存的？如果加速域名换了，或者换图床地址了，需要怎么迁移。希望有经验的大佬可以留言指导！

## 3. 上传到本地

这个比较简单啦，贴下核心代码吧

*  在yml配置下上传路径

~~~yml
file:
  local:
    maxFileSize: 10485760
    imageFilePath: D:/test/image/
    docFilePath: D:/test/file/
~~~

* 创建配置类，读取配置文件的参数

~~~java
package com.maple.upload.properties;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 上传本地配置
 *
 * @author 笑小枫
 * @date 2022/7/22
 * @see <a href="https://www.xiaoxiaofeng.com">https://www.xiaoxiaofeng.com</a>
 */
@Data
@Configuration
public class LocalFileProperties {

    // ---------------本地文件配置 start------------------
    /**
     * 图片存储路径
     */
    @Value("${file.local.imageFilePath}")
    private String imageFilePath;

    /**
     * 文档存储路径
     */
    @Value("${file.local.docFilePath}")
    private String docFilePath;

    /**
     * 文件限制大小
     */
    @Value("${file.local.maxFileSize}")
    private long maxFileSize;
    // --------------本地文件配置 end-------------------

}
~~~

* 创建上传下载工具类

~~~java
package com.maple.upload.util;

import com.maple.upload.properties.LocalFileProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 笑小枫
 * @date 2024/1/10
 * @see <a href="https://www.xiaoxiaofeng.com">https://www.xiaoxiaofeng.com</a>
 */
@Slf4j
@Component
@AllArgsConstructor
public class LocalFileUtil {
    private final LocalFileProperties fileProperties;


    private static final List<String> FILE_TYPE_LIST_IMAGE = Arrays.asList(
            "image/png",
            "image/jpg",
            "image/jpeg",
            "image/bmp");

    /**
     * 上传图片
     */
    public String uploadImage(MultipartFile file) {
        // 检查图片类型
        String contentType = file.getContentType();
        if (!FILE_TYPE_LIST_IMAGE.contains(contentType)) {
            throw new RuntimeException("上传失败，不允许的文件类型");
        }
        int size = (int) file.getSize();
        if (size > fileProperties.getMaxFileSize()) {
            throw new RuntimeException("文件过大");
        }
        String fileName = file.getOriginalFilename();
        //获取文件后缀
        String afterName = StringUtils.substringAfterLast(fileName, ".");
        //获取文件前缀
        String prefName = StringUtils.substringBeforeLast(fileName, ".");
        //获取一个时间毫秒值作为文件名
        fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + prefName + "." + afterName;
        File filePath = new File(fileProperties.getImageFilePath(), fileName);

        //判断文件是否已经存在
        if (filePath.exists()) {
            throw new RuntimeException("文件已经存在");
        }
        //判断文件父目录是否存在
        if (!filePath.getParentFile().exists()) {
            filePath.getParentFile().mkdirs();
        }
        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            log.error("图片上传失败", e);
            throw new RuntimeException("图片上传失败");
        }
        return fileName;
    }

    /**
     * 批量上传文件
     */
    public List<Map<String, Object>> uploadFiles(MultipartFile[] files) {
        int size = 0;
        for (MultipartFile file : files) {
            size = (int) file.getSize() + size;
        }
        if (size > fileProperties.getMaxFileSize()) {
            throw new RuntimeException("文件过大");
        }
        List<Map<String, Object>> fileInfoList = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            Map<String, Object> map = new HashMap<>();
            String fileName = files[i].getOriginalFilename();
            //获取文件后缀
            String afterName = StringUtils.substringAfterLast(fileName, ".");
            //获取文件前缀
            String prefName = StringUtils.substringBeforeLast(fileName, ".");

            String fileServiceName = new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(new Date()) + i + "_" + prefName + "." + afterName;
            File filePath = new File(fileProperties.getDocFilePath(), fileServiceName);
            // 判断文件父目录是否存在
            if (!filePath.getParentFile().exists()) {
                filePath.getParentFile().mkdirs();
            }
            try {
                files[i].transferTo(filePath);
            } catch (IOException e) {
                log.error("文件上传失败", e);
                throw new RuntimeException("文件上传失败");
            }
            map.put("fileName", fileName);
            map.put("filePath", filePath);
            map.put("fileServiceName", fileServiceName);
            fileInfoList.add(map);
        }
        return fileInfoList;
    }

    /**
     * 批量删除文件
     *
     * @param fileNameArr 服务端保存的文件的名数组
     */
    public void deleteFile(String[] fileNameArr) {
        for (String fileName : fileNameArr) {
            String filePath = fileProperties.getDocFilePath() + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    Files.delete(file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                    log.warn("文件删除失败", e);
                }
            } else {
                log.warn("文件: {} 删除失败，该文件不存在", fileName);
            }
        }
    }

    /**
     * 下载文件
     */
    public void downLoadFile(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        String encodeFileName = URLDecoder.decode(fileName, "UTF-8");
        File file = new File(fileProperties.getDocFilePath() + encodeFileName);
        // 下载文件
        if (!file.exists()) {
            throw new RuntimeException("文件不存在！");
        }
        try (FileInputStream inputStream = new FileInputStream(file);
             ServletOutputStream outputStream = response.getOutputStream()) {
            response.reset();
            //设置响应类型	PDF文件为"application/pdf"，WORD文件为："application/msword"， EXCEL文件为："application/vnd.ms-excel"。
            response.setContentType("application/octet-stream;charset=utf-8");
            //设置响应的文件名称,并转换成中文编码
            String afterName = StringUtils.substringAfterLast(fileName, "_");
            //保存的文件名,必须和页面编码一致,否则乱码
            afterName = response.encodeURL(new String(afterName.getBytes(), StandardCharsets.ISO_8859_1.displayName()));
            response.setHeader("Content-type", "application-download");
            //attachment作为附件下载；inline客户端机器有安装匹配程序，则直接打开；注意改变配置，清除缓存，否则可能不能看到效果
            response.addHeader("Content-Disposition", "attachment;filename=" + afterName);
            response.addHeader("filename", afterName);
            //将文件读入响应流
            int length = 1024;
            byte[] buf = new byte[1024];
            int readLength = inputStream.read(buf, 0, length);
            while (readLength != -1) {
                outputStream.write(buf, 0, readLength);
                readLength = inputStream.read(buf, 0, length);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
~~~

访问图片的话，可以通过重写`WebMvcConfigurer`的`addResourceHandlers`方法来实现。

通过请求`/local/images/**`将链接虚拟映射到我们配置的`localFileProperties.getImageFilePath()`下，文件访问同理。

详细代码如下

~~~java
package com.maple.upload.config;

import com.maple.upload.properties.LocalFileProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 笑小枫 <https://www.xiaoxiaofeng.com/>
 * @date 2024/1/10
 */
@Configuration
@AllArgsConstructor
public class LocalFileConfig implements WebMvcConfigurer {

    private final LocalFileProperties localFileProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 重写方法
        // 修改tomcat 虚拟映射
        // 定义图片存放路径
        registry.addResourceHandler("/local/images/**").
                addResourceLocations("file:" + localFileProperties.getImageFilePath());
        //定义文档存放路径
        registry.addResourceHandler("/local/doc/**").
                addResourceLocations("file:" + localFileProperties.getDocFilePath());
    }
}
~~~

* controller调用代码

~~~java
package com.maple.upload.controller;

import com.maple.upload.util.LocalFileUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 文件相关操作接口
 *
 * @author 笑小枫
 * @date 2024/1/10
 * @see <a href="https://www.xiaoxiaofeng.com">https://www.xiaoxiaofeng.com</a>
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/local")
public class LocalFileController {

    private final LocalFileUtil fileUtil;

    /**
     * 图片上传
     */
    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("图片内容为空，上传失败!");
        }
        return fileUtil.uploadImage(file);
    }

    /**
     * 文件批量上传
     */
    @PostMapping("/uploadFiles")
    public List<Map<String, Object>> uploadFiles(@RequestParam(value = "file") MultipartFile[] files) {
        return fileUtil.uploadFiles(files);
    }

    /**
     * 批量删除文件
     */
    @PostMapping("/deleteFiles")
    public void deleteFiles(@RequestParam(value = "files") String[] files) {
        fileUtil.deleteFile(files);
    }

    /**
     * 文件下载功能
     */
    @GetMapping(value = "/download/{fileName:.*}")
    public void download(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        try {
            fileUtil.downLoadFile(response, fileName);
        } catch (Exception e) {
            log.error("文件下载失败", e);
        }
    }
}
~~~

调用上传图片的接口，可以看到图片已经上传成功。

![image-20240116102717345](https://image.xiaoxiaofeng.site/blog/2024/01/16/xxf-20240116102725.png?xxfjava)

通过请求`/local/images/**`将链接虚拟映射我们图片上。

![image-20240116103037587](https://image.xiaoxiaofeng.site/blog/2024/01/16/xxf-20240116103037.png?xxfjava)

批量上传，删除等操作就不一一演示截图了，代码已贴，因为是先写的demo，后写的文章，获取代码片贴的有遗漏，如有遗漏，可以去文章底部查看源码地址。

## 4. 上传阿里云OSS

> 阿里云OSS官方sdk使用文档：https://help.aliyun.com/zh/oss/developer-reference/java
>
> 阿里云OSS操作指南：https://help.aliyun.com/zh/oss/user-guide
>
> 公共云下OSS Region和Endpoint对照表：https://help.aliyun.com/zh/oss/user-guide/regions-and-endpoints

更多公共云下OSS Region和Endpoint对照，参考上面链接

![image-20240116103832968](https://image.xiaoxiaofeng.site/blog/2024/01/16/xxf-20240116103833.png?xxfjava)

* 引入oss sdk依赖

~~~xml
        <!-- 阿里云OSS -->
        <dependency>
            <groupId>com.aliyun.oss</groupId>
            <artifactId>aliyun-sdk-oss</artifactId>
            <version>3.8.1</version>
        </dependency>
~~~

* 配置上传配置信息

~~~yml
file:
  oss:
    bucketName: mapleBucket
    accessKeyId: your ak
    secretAccessKey: your sk
    endpoint: oss-cn-shanghai.aliyuncs.com
    showUrl: cdn地址-file.xiaoxiaofeng.com
~~~

* 创建配置类，读取配置文件的参数

~~~java
/*
 * Copyright (c) 2018-2999 上海合齐软件科技科技有限公司 All rights reserved.
 *
 *
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.maple.upload.properties;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS配置
 *
 * @author 笑小枫 <https://www.xiaoxiaofeng.com/>
 * @date 2024/1/10
 */
@Data
@Configuration
public class AliOssProperties {

    @Value("${file.oss.bucketName}")
    private String bucketName;

    @Value("${file.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${file.oss.secretAccessKey}")
    private String secretAccessKey;

    @Value("${file.oss.endpoint}")
    private String endpoint;

    @Value("${file.oss.showUrl}")
    private String showUrl;
}

~~~

* 上传工具类

~~~java
package com.maple.upload.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.maple.upload.properties.AliOssProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 阿里云OSS 对象存储工具类
 * 阿里云OSS官方sdk使用文档：https://help.aliyun.com/zh/oss/developer-reference/java
 * 阿里云OSS操作指南：https://help.aliyun.com/zh/oss/user-guide
 * 
 * @author 笑小枫 <https://www.xiaoxiaofeng.com/>
 * @date 2024/1/15
 */
@Slf4j
@Component
@AllArgsConstructor
public class AliOssUtil {

    private final AliOssProperties aliOssProperties;

    public String uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new RuntimeException("获取文件信息失败");
        }
        // 组建上传的文件名称，命名规则可自定义更改
        String objectKey = FileCommonUtil.setFilePath("xiaoxiaofeng") + FileCommonUtil.setFileName("xxf", fileName.substring(fileName.lastIndexOf(".")));

        //构造一个OSS对象的配置类
        OSS ossClient = new OSSClientBuilder().build(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKeyId(), aliOssProperties.getSecretAccessKey());
        try (InputStream inputStream = file.getInputStream()) {
            log.info(String.format("阿里云OSS上传开始，原文件名：%s，上传后的文件名：%s", fileName, objectKey));
            PutObjectResult result = ossClient.putObject(aliOssProperties.getBucketName(), objectKey, inputStream);
            log.info(String.format("阿里云OSS上传结束，文件名：%s，返回结果：%s", objectKey, result.toString()));
            return aliOssProperties.getShowUrl() + objectKey;
        } catch (Exception e) {
            log.error("调用阿里云OSS失败", e);
            throw new RuntimeException("调用阿里云OSS失败");
        }
    }
}
~~~

因为篇幅问题，controller就不贴了。暂时没有阿里云oss的资源了，这里就不做测试，小伙伴在使用过程中如有问题，麻烦留言告诉我下！

## 5. 上传华为云OBS

> 华为云OBS官方sdk使用文档：https://support.huaweicloud.com/sdk-java-devg-obs/obs_21_0101.html
> 
> 华为云OBS操作指南：https://support.huaweicloud.com/ugobs-obs/obs_41_0002.html
> 
> 华为云各服务应用区域和各服务的终端节点：https://developer.huaweicloud.com/endpoint?OBS

* 引入sdk依赖

~~~xml
        <!-- 华为云OBS -->
        <dependency>
            <groupId>com.huaweicloud</groupId>
            <artifactId>esdk-obs-java-bundle</artifactId>
            <version>3.21.8</version>
        </dependency>
~~~

* 配置上传配置信息

~~~yml
file:
  obs:
    bucketName: mapleBucket
    accessKey: your ak
    secretKey: your sk
    endPoint: obs.cn-east-2.myhuaweicloud.com
    showUrl: cdn地址-file.xiaoxiaofeng.com
~~~

* 创建配置类，读取配置文件的参数

~~~java
package com.maple.upload.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 华为云上传配置
 *
 * @author 笑小枫 <https://www.xiaoxiaofeng.com/>
 * @date 2024/1/10
 */
@Data
@Configuration
public class HwyObsProperties {

    @Value("${file.obs.bucketName}")
    private String bucketName;

    @Value("${file.obs.accessKey}")
    private String accessKey;

    @Value("${file.obs.secretKey}")
    private String secretKey;

    @Value("${file.obs.endPoint}")
    private String endPoint;

    @Value("${file.obs.showUrl}")
    private String showUrl;
}
~~~

* 上传工具类

~~~java
package com.maple.upload.util;

import com.alibaba.fastjson.JSON;
import com.maple.upload.bean.HwyObsModel;
import com.maple.upload.properties.HwyObsProperties;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.PostSignatureRequest;
import com.obs.services.model.PostSignatureResponse;
import com.obs.services.model.PutObjectResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 华为云OBS 对象存储工具类
 * 华为云OBS官方sdk使用文档：https://support.huaweicloud.com/sdk-java-devg-obs/obs_21_0101.html
 * 华为云OBS操作指南：https://support.huaweicloud.com/ugobs-obs/obs_41_0002.html
 * 华为云各服务应用区域和各服务的终端节点：https://developer.huaweicloud.com/endpoint?OBS
 *
 * @author 笑小枫
 * @date 2022/7/22
 * @see <a href="https://www.xiaoxiaofeng.com">https://www.xiaoxiaofeng.com</a>
 */
@Slf4j
@Component
@AllArgsConstructor
public class HwyObsUtil {

    private final HwyObsProperties fileProperties;

    /**
     * 上传华为云obs文件存储
     *
     * @param file 文件
     * @return 文件访问路径， 如果配置CDN，这里直接返回CDN+文件名（objectKey）
     */
    public String uploadFileToObs(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new RuntimeException("获取文件信息失败");
        }
        // 文件类型
        String fileType = fileName.substring(file.getOriginalFilename().lastIndexOf("."));
        // 组建上传的文件名称，命名规则可自定义更改
        String objectKey = FileCommonUtil.setFilePath("") + FileCommonUtil.setFileName(null, fileType);
        PutObjectResult putObjectResult;
        try (InputStream inputStream = file.getInputStream();
             ObsClient obsClient = new ObsClient(fileProperties.getAccessKey(), fileProperties.getSecretKey(), fileProperties.getEndPoint())) {
            log.info(String.format("华为云obs上传开始，原文件名：%s，上传后的文件名：%s", fileName, objectKey));
            putObjectResult = obsClient.putObject(fileProperties.getBucketName(), objectKey, inputStream);
            log.info(String.format("华为云obs上传结束，文件名：%s，返回结果：%s", objectKey, JSON.toJSONString(putObjectResult)));
        } catch (ObsException | IOException e) {
            log.error("华为云obs上传文件失败", e);
            throw new RuntimeException("华为云obs上传文件失败，请重试");
        }
        if (putObjectResult.getStatusCode() == 200) {
            return putObjectResult.getObjectUrl();
        } else {
            throw new RuntimeException("华为云obs上传文件失败，请重试");
        }
    }

    /**
     * 获取华为云上传token，将token返回给前端，然后由前端上传，这样文件不占服务器端带宽
     *
     * @param fileName 文件名称
     * @return
     */
    public HwyObsModel getObsConfig(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new RuntimeException("The fileName cannot be empty.");
        }
        String obsToken;
        String objectKey = null;
        try (ObsClient obsClient = new ObsClient(fileProperties.getAccessKey(), fileProperties.getSecretKey(), fileProperties.getEndPoint())) {

            String fileType = fileName.substring(fileName.lastIndexOf("."));
            // 组建上传的文件名称，命名规则可自定义更改
            objectKey = FileCommonUtil.setFilePath("") + FileCommonUtil.setFileName("", fileType);

            PostSignatureRequest request = new PostSignatureRequest();
            // 设置表单上传请求有效期，单位：秒
            request.setExpires(3600);
            request.setBucketName(fileProperties.getBucketName());
            if (StringUtils.isNotBlank(objectKey)) {
                request.setObjectKey(objectKey);
            }
            PostSignatureResponse response = obsClient.createPostSignature(request);
            obsToken = response.getToken();
        } catch (ObsException | IOException e) {
            log.error("华为云obs上传文件失败", e);
            throw new RuntimeException("华为云obs上传文件失败，请重试");
        }
        HwyObsModel obsModel = new HwyObsModel();
        obsModel.setBucketName(fileProperties.getBucketName());
        obsModel.setEndPoint(fileProperties.getEndPoint());
        obsModel.setToken(obsToken);
        obsModel.setObjectKey(objectKey);
        obsModel.setShowUrl(fileProperties.getShowUrl());
        return obsModel;
    }
}
~~~

篇幅问题，不贴controller和测试截图了，完整代码参考文章底部源码吧，思想明白了，别的都大差不差。

## 6. 上传七牛云


> 七牛云官方sdk：https://developer.qiniu.com/kodo/1239/java
> 
> 七牛云存储区域表链接：https://developer.qiniu.com/kodo/1671/region-endpoint-fq

* 引入sdk依赖

~~~xml
        <!-- 七牛云 -->
        <dependency>
            <groupId>com.qiniu</groupId>
            <artifactId>qiniu-java-sdk</artifactId>
            <version>7.2.29</version>
        </dependency>
~~~

* 配置上传配置信息

~~~yml
file:
  qiniuyun:
    bucket: mapleBucket
    accessKey: your ak
    secretKey: your sk
    regionId: z1
    showUrl: cdn地址-file.xiaoxiaofeng.com
~~~

* 创建配置类，读取配置文件的参数

~~~java
package com.maple.upload.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛云配置
 *
 * @author 笑小枫 <https://www.xiaoxiaofeng.com/>
 * @date 2024/1/10
 */
@Data
@Configuration
public class QiNiuProperties {

    @Value("${file.qiniuyun.accessKey}")
    private String accessKey;

    @Value("${file.qiniuyun.secretKey}")
    private String secretKey;

    @Value("${file.qiniuyun.bucket}")
    private String bucket;
    
    @Value("${file.qiniuyun.regionId}")
    private String regionId;

    @Value("${file.qiniuyun.showUrl}")
    private String showUrl;
}
~~~

* 上传工具类，**注意有一个区域转换，如后续有新增，`qiNiuConfig`这里需要调整一下**

~~~java
package com.maple.upload.util;

import com.maple.upload.properties.QiNiuProperties;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;

/**
 * 
 * 七牛云 对象存储工具类
 * 七牛云官方sdk：https://developer.qiniu.com/kodo/1239/java
 * 七牛云存储区域表链接：https://developer.qiniu.com/kodo/1671/region-endpoint-fq
 * 
 * @author 笑小枫 <https://www.xiaoxiaofeng.com/>
 * @date 2022/3/24
 */
@Slf4j
@Component
@AllArgsConstructor
public class QiNiuYunUtil {

    private final QiNiuProperties qiNiuProperties;

    public String uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new RuntimeException("获取文件信息失败");
        }
        // 组建上传的文件名称，命名规则可自定义更改
        String objectKey = FileCommonUtil.setFilePath("xiaoxiaofeng") + FileCommonUtil.setFileName("xxf", fileName.substring(fileName.lastIndexOf(".")));

        //构造一个带指定 Region 对象的配置类
        Configuration cfg = qiNiuConfig(qiNiuProperties.getRegionId());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        try (InputStream inputStream = file.getInputStream()) {
            Auth auth = Auth.create(qiNiuProperties.getAccessKey(), qiNiuProperties.getSecretKey());
            String upToken = auth.uploadToken(qiNiuProperties.getBucket());
            log.info(String.format("七牛云上传开始，原文件名：%s，上传后的文件名：%s", fileName, objectKey));
            Response response = uploadManager.put(inputStream, objectKey, upToken, null, null);
            log.info(String.format("七牛云上传结束，文件名：%s，返回结果：%s", objectKey, response.toString()));
            return qiNiuProperties.getShowUrl() + objectKey;
        } catch (Exception e) {
            log.error("调用七牛云失败", e);
            throw new RuntimeException("调用七牛云失败");
        }
    }

    private static Configuration qiNiuConfig(String zone) {
        Region region = null;
        if (Objects.equals(zone, "z1")) {
            region = Region.huabei();
        } else if (Objects.equals(zone, "z0")) {
            region = Region.huadong();
        } else if (Objects.equals(zone, "z2")) {
            region = Region.huanan();
        } else if (Objects.equals(zone, "na0")) {
            region = Region.beimei();
        } else if (Objects.equals(zone, "as0")) {
            region = Region.xinjiapo();
        }
        return new Configuration(region);
    }
}
~~~

篇幅问题，不贴controller和测试截图了，完整代码参考文章底部源码吧，思想明白了，别的都大差不差。

## 7. 上传又拍云

> 又拍云客户端配置：https://help.upyun.com/knowledge-base/quick_start/
> 
> 又拍云官方sdk：https://github.com/upyun/java-sdk

* 引入sdk依赖

~~~xml
        <!-- 又拍云OSS -->
        <dependency>
            <groupId>com.upyun</groupId>
            <artifactId>java-sdk</artifactId>
            <version>4.2.3</version>
        </dependency>
~~~

* 配置上传配置信息

~~~yml
file:
  upy:
    bucketName: mapleBucket
    userName: 操作用户名称
    password: 操作用户密码
    showUrl: cdn地址-file.xiaoxiaofeng.com
~~~

* 创建配置类，读取配置文件的参数

~~~java
package com.maple.upload.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 又拍云上传配置
 *
 * @author 笑小枫 <https://www.xiaoxiaofeng.com/>
 * @date 2024/1/10
 */
@Data
@Configuration
public class UpyOssProperties {

    @Value("${file.upy.bucketName}")
    private String bucketName;

    @Value("${file.upy.userName}")
    private String userName;

    @Value("${file.upy.password}")
    private String password;

    /**
     * 加速域名
     */
    @Value("${file.upy.showUrl}")
    private String showUrl;
}
~~~

* 上传工具类

~~~java
package com.maple.upload.util;

import com.maple.upload.properties.UpyOssProperties;
import com.upyun.RestManager;
import com.upyun.UpException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * 又拍云 对象存储工具类
 * 又拍云客户端配置：https://help.upyun.com/knowledge-base/quick_start/
 * 又拍云官方sdk：https://github.com/upyun/java-sdk
 *
 * @author 笑小枫
 * @date 2022/7/22
 * @see <a href="https://www.xiaoxiaofeng.com">https://www.xiaoxiaofeng.com</a>
 */
@Slf4j
@Component
@AllArgsConstructor
public class UpyOssUtil {

    private final UpyOssProperties fileProperties;

    /**
     * 根据url上传文件到又拍云
     */
    public String uploadUpy(String url) {
        // 组建上传的文件名称，命名规则可自定义更改
        String fileName = FileCommonUtil.setFilePath("xiaoxiaofeng") + FileCommonUtil.setFileName("xxf", url.substring(url.lastIndexOf(".")));
        RestManager restManager = new RestManager(fileProperties.getBucketName(), fileProperties.getUserName(), fileProperties.getPassword());

        URI u = URI.create(url);
        try (InputStream inputStream = u.toURL().openStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            Response response = restManager.writeFile(fileName, bytes, null);
            if (response.isSuccessful()) {
                return fileProperties.getShowUrl() + fileName;
            }
        } catch (IOException | UpException e) {
            log.error("又拍云oss上传文件失败", e);
        }
        throw new RuntimeException("又拍云oss上传文件失败，请重试");
    }

    /**
     * MultipartFile上传文件到又拍云
     */
    public String uploadUpy(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new RuntimeException("获取文件信息失败");
        }
        // 组建上传的文件名称，命名规则可自定义更改
        String objectKey = FileCommonUtil.setFilePath("xiaoxiaofeng") + FileCommonUtil.setFileName("xxf", fileName.substring(fileName.lastIndexOf(".")));
        RestManager restManager = new RestManager(fileProperties.getBucketName(), fileProperties.getUserName(), fileProperties.getPassword());

        try (InputStream inputStream = file.getInputStream()) {
            log.info(String.format("又拍云上传开始，原文件名：%s，上传后的文件名：%s", fileName, objectKey));
            Response response = restManager.writeFile(objectKey, inputStream, null);
            log.info(String.format("又拍云上传结束，文件名：%s，返回结果：%s", objectKey, response.isSuccessful()));
            if (response.isSuccessful()) {
                return fileProperties.getShowUrl() + objectKey;
            }
        } catch (IOException | UpException e) {
            log.error("又拍云oss上传文件失败", e);
        }
        throw new RuntimeException("又拍云oss上传文件失败，请重试");
    }
}
~~~

篇幅问题，不贴controller和测试截图了，完整代码参考文章底部源码吧，思想明白了，别的都大差不差。

## 8. 项目源码

本文到此就结束了，如果帮助到你了，帮忙点个赞👍

本文源码：[https://github.com/hack-feng/maple-product/tree/main/maple-file-upload](https://github.com/hack-feng/maple-product/tree/main/maple-file-upload)

>  🐾我是笑小枫，全网皆可搜的【[笑小枫](https://www.xiaoxiaofeng.com)】
