
分布式文件存储，浏览器上传的文件，统一存储到一个服务器

本文选择统一存储到云存储

# 阿里云开通云存储 #

 *  先贴计费，很便宜  
    原文：[https://www.aliyun.com/price/product?spm=5176.8465980.help.3.4e701450R42spo\#/oss/detail/ossbag][https_www.aliyun.com_price_product_spm_5176.8465980.help.3.4e701450R42spo_oss_detail_ossbag]  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404811919.png)
 *  aliyun官网 - 产品 - oss对象存储  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404812777.png)
 *  开通

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404813252.png)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404814007.png)

 *  如果未实名认证，需要认证一下，支付宝认证，约一分钟就可以了

# 使用 #

## 术语 ##

 *  存储空间 `Bucket`  
    存储空间是您用于存储对象（Object）的容器，所有的对象都必须隶属于某个存储空间。
 *  对象/文件 `Object`  
    对象是 OSS 存储数据的基本单元，也被称为OSS的文件。对象由元信息（Object Meta）、用户数据（Data）和文件名（Key）组成。对象由存储空间内部唯一的Key来标识。
 *  地域 `Region`  
    地域表示 OSS 的数据中心所在物理位置。您可以根据费用、请求来源等综合选择数据存储的地域。详情请查看OSS已经开通的Region。
 *  访问域名 `Endpoint`  
    Endpoint 表示OSS对外服务的访问域名。OSS以HTTP RESTful API的形式对外提供服务，当访问不同地域的时候，需要不同的域名。通过内网和外网访问同一个地域所需要的域名也是不同的。具体的内容请参见各个Region对应的Endpoint。
 *  访问密钥 `AccessKey`  
    AccessKey，简称 AK，指的是访问身份验证中用到的AccessKeyId 和AccessKeySecret。OSS通过使用AccessKeyId 和AccessKeySecret对称加密的方法来验证某个请求的发送者身份。AccessKeyId用于标识用户，AccessKeySecret是用户用于加密签名字符串和OSS用来验证签名字符串的密钥，其中AccessKeySecret 必须保密。

## 创建 ##

 *  右侧点创建  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404814355.png)
 *  选项（根据实际需要来选）  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404814985.png)  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404815837.png)  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404816565.png)

## 快速测试上传文件 ##

 *  上传，选择文件上传  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404817220.png)
 *  查看图片  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404817763.png)  
    访问如下位置网址：  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404818163.png)

## java上传 ##

java上传需要先把文件上传到后台，再由后台上传给oss对象存储，自己的服务器也存储了一遍文件，不划算。

服务端签名后上传（本文后面）是指从后台获得签名后直接从前端上传，效率更高。

### 1 创建子用户 ###

 *  直接通过自己的用户名密码验证是不安全的，创建子用户，并对其授权
 *  网址 [https://ram.console.aliyun.com/users][https_ram.console.aliyun.com_users]  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404818763.png)
 *  如图所示的两个内容相当于用户名密码,要复制下来，一会要用，一会再进来是看不到`AccessKey Secret`内容的！  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404819482.png)
 *  授权
    
    1.  先返回  
        ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404820074.png)
    2.  添加权限  
        ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404820416.png)
    3.  添加如下权限  
        ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404821019.png)
    4.  成功  
        ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404821703.png)

### 2.1 方法1：（推荐方法2） ###

#### 步骤 ####

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404822016.png)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404822803.png)

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404823382.png)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404824132.png)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404824757.png)

#### 代码 ####

##### pom #####

 *  在Maven项目中加入依赖项  
    在Maven工程中使用OSS Java SDK，只需在pom.xml中加入相应依赖即可。以3.10.2版本为例，在中加入如下内容：

    ~~~xml
    <dependency>
        <groupId>com.aliyun.oss</groupId>
        <artifactId>aliyun-sdk-oss</artifactId>
        <version>3.10.2</version>
    </dependency>
    ~~~
    
 *  如果使用的是Java 9及以上的版本，则需要添加jaxb相关依赖。添加jaxb相关依赖示例代码如下：

    ~~~xml
    <dependency>
        <groupId>javax.xml.bind</groupId>
        <artifactId>jaxb-api</artifactId>
        <version>2.3.1</version>
    </dependency>
    <dependency>
        <groupId>javax.activation</groupId>
        <artifactId>activation</artifactId>
        <version>1.1.1</version>
    </dependency>
    <!-- no more than 2.3.3-->
    <dependency>
        <groupId>org.glassfish.jaxb</groupId>
        <artifactId>jaxb-runtime</artifactId>
        <version>2.3.3</version>
    </dependency>
    ~~~

##### 上传文件 #####

```java
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import java.io.File;

public class Demo {

    public static void main(String[] args) throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "yourAccessKeyId";
        String accessKeySecret = "yourAccessKeySecret";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "examplebucket";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "exampledir/exampleobject.txt";
        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String filePath= "D:\\localpath\\examplefile.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 创建PutObjectRequest对象。            
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(filePath));
            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 上传文件。
            ossClient.putObject(putObjectRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
```

### 2.2 方法2： ###

`Alibaba Cloud OSS`: 阿里云对象存储服务（Object Storage Service，简称 OSS），是阿里云提供的海量、安全、低成本、高可靠的云存储服务。您可以在任何应用、任何时间、任何地点存储和访问任意类型的数据。  
原文：[https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-oss-spring-boot-sample][https_github.com_alibaba_aliyun-spring-boot_tree_master_aliyun-spring-boot-samples_aliyun-oss-spring-boot-sample]

#### pom 引入依赖 ####

```xml
<!-- https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alicloud-oss -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alicloud-oss</artifactId>
    <version>2.2.0.RELEASE</version>
</dependency>
```

>  *  附上官方文档的依赖，导入失败

~~~xml
<dependency>
   <groupId>com.alibaba.cloud</groupId>
   <artifactId>aliyun-oss-spring-boot-starter</artifactId>
</dependency>
~~~

#### yaml ####

 *  配置accessKeyId, secretAccessKey 和region，获取方法原文有

    ~~~yml
    spring:
      cloud:
        alicloud:
          access-key: xx
          secret-key: xx
          oss:
            endpoint: xx
    ~~~

#### 文件上传代码 ####

 *  文件上传

    
    
    ```java
    @Resource
    private OSSClient ossClient;
        
    @Test
    public void saveFile() {
        // 上传
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "examplebucket";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "exampledir/exampleobject.txt";
        ossClient.putObject(new PutObjectRequest(bucketName, objectName, new File("C:\\Users\\AikeTech\\Pictures\\Saved Pictures\\20220223214433.jpg")));
        System.out.println("上传成功。。。");
    }
    ```

## 服务端签名后直传 ##

原文：[https://help.aliyun.com/document\_detail/31926.html][https_help.aliyun.com_document_detail_31926.html]  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404825478.png)

### 签名主要内容 ###

```json
{
"accessid":"LTAI5tBDFVar1hoq****",
"host":"http://post-test.oss-cn-hangzhou.aliyuncs.com",
"policy":"eyJleHBpcmF0aW9uIjoiMjAxNS0xMS0wNVQyMDoyMzoyM1oiLCJjxb25kaXRpb25zIjpbWyJjcb250ZW50LWxlbmd0aC1yYW5nZSIsMCwxMDQ4NTc2MDAwXSxbInN0YXJ0cy13aXRoIiwiJGtleSIsInVzZXItZGlyXC8i****",
"signature":"VsxOcOudx******z93CLaXPz+4s=",
"expire":1446727949,
"dir":"user-dirs/"
}
```

Body中的各字段说明如下：

`accessid` 用户请求的AccessKey ID。

`host` 用户发送上传请求的域名。

`policy`用户表单上传的策略（Policy），Policy为经过Base64编码过的字符串。详情请参见Post Policy。

`signature` 对Policy签名后的字符串。详情请参见Post Signature。

`expire`由服务器端指定的Policy过期时间，格式为Unix时间戳（自UTC时间1970年01月01号开始的秒数）。

`dir`限制上传的文件前缀。

### yaml主要内容： ###

```yml
spring: 
    cloud:
      alicloud:
        access-key: xx
        secret-key: xx
        oss:
          endpoint: xx
          # 自定义属性bucket
          bucket: xx
server: 
  port: 30000
```

### 编写controller，返回签名信息 ###

```java
@RestController
@Slf4j
public class OssController {
    @Resource
    OSS ossClient;

    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    @Value("${spring.cloud.alicloud.access-key}")
    String accessId ;

    @Value("${spring.cloud.alicloud.secret-key}")
    String accessKey ;

    // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    @Value("${spring.cloud.alicloud.oss.endpoint}")
    String endpoint ;

    // 填写Bucket名称，例如examplebucket。
    @Value("${spring.cloud.alicloud.oss.bucket}")
    String bucket ;
    // 设置上传回调URL，即回调服务器地址，用于处理应用服务器与OSS之间的通信。OSS会在文件上传完成后，把文件上传信息通过此回调URL发送给应用服务器。
    //String callbackUrl = "https://192.168.0.0:8888";

    @RequestMapping("/oss/policy")
    protected R generatePostPolicy() {

        // 填写Host名称，格式为https://bucketname.endpoint。
        String host = "https://" + bucket + "." + endpoint;

        // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dir = format.format(new Date())  + "/";//注意，有/

        Map<String, String> respMap = new LinkedHashMap<String, String>();
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));

//            JSONObject jasonCallback = new JSONObject();
//            jasonCallback.put("callbackUrl", callbackUrl);
//            jasonCallback.put("callbackBody",
//                    "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
//            jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
//            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
//            respMap.put("callback", base64CallbackBody);
        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            log.info(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return R.ok().put("data", respMap);
    }
}
```

 *  其中R是人人开源的类：
    
    
    ​    	
     ```java
     /**
      * 返回数据
      *
      * @author Mark sunlightcs@gmail.com
      */
     public class R extends HashMap<String, Object> {
         private static final long serialVersionUID = 1L;
         
         public R() {
             put("code", 0);
             put("msg", "success");
         }
     
         public static R error() {
             return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
         }
     
         public static R error(String msg) {
             return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
         }
     
         public static R error(int code, String msg) {
             R r = new R();
             r.put("code", code);
             r.put("msg", msg);
             return r;
         }
     
         public static R ok(String msg) {
             R r = new R();
             r.put("msg", msg);
             return r;
         }
     
         public static R ok(Map<String, Object> map) {
             R r = new R();
             r.putAll(map);
             return r;
         }
     
         public static R ok() {
             return new R();
         }
     
         public R put(String key, Object value) {
             super.put(key, value);
             return this;
         }
     }
     ```
    
 *  附上原文签名上传代码供参考：（**可忽略**）  
    [https://help.aliyun.com/document\_detail/91868.htm?spm=a2c4g.11186623.0.0.1607c9277SiG3G\#concept-ahk-rfz-2fb][https_help.aliyun.com_document_detail_91868.htm_spm_a2c4g.11186623.0.0.1607c9277SiG3G_concept-ahk-rfz-2fb]
    
    ```java
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
       String accessId = "yourAccessKeyId";      
       String accessKey = "yourAccessKeySecret"; 
       // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
       String endpoint = "oss-cn-hangzhou.aliyuncs.com"; 
       // 填写Bucket名称，例如examplebucket。
       String bucket = "examplebucket"; 
       // 填写Host名称，格式为https://bucketname.endpoint。                   
       String host = "https://examplebucket.oss-cn-hangzhou.aliyuncs.com"; 
       // 设置上传回调URL，即回调服务器地址，用于处理应用服务器与OSS之间的通信。OSS会在文件上传完成后，把文件上传信息通过此回调URL发送给应用服务器。
       String callbackUrl = "https://192.168.0.0:8888";
       // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
       String dir = "exampledir/"; 
    
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
    
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
    
            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));
    
            JSONObject jasonCallback = new JSONObject();
            jasonCallback.put("callbackUrl", callbackUrl);
            jasonCallback.put("callbackBody",
                    "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
            jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
            respMap.put("callback", base64CallbackBody);
    
            JSONObject ja1 = JSONObject.fromObject(respMap);
            // System.out.println(ja1.toString());
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST");
            response(request, response, ja1.toString());
    
        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally { 
            ossClient.shutdown();
        }
    }
    ```

### 前端 ###

#### 文件上传的组件： ####

##### 单文件： #####

`singleUpload.vue`

```vue
<template> 
  <div>
    <el-upload
      action="http://gulimall.oss-cn-shanghai.aliyuncs.com"
      :data="dataObj"
      list-type="picture"
      :multiple="false" :show-file-list="showFileList"
      :file-list="fileList"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-preview="handlePreview">
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="fileList[0].url" alt="">
    </el-dialog>
  </div>
</template>
<script>
   import {policy} from './policy'
   import { getUUID } from '@/utils'

  export default {
    name: 'singleUpload',
    props: {
      value: String
    },
    computed: {
      imageUrl() {
        return this.value;
      },
      imageName() {
        if (this.value != null && this.value !== '') {
          return this.value.substr(this.value.lastIndexOf("/") + 1);
        } else {
          return null;
        }
      },
      fileList() {
        return [{
          name: this.imageName,
          url: this.imageUrl
        }]
      },
      showFileList: {
        get: function () {
          return this.value !== null && this.value !== ''&& this.value!==undefined;
        },
        set: function (newValue) {
        }
      }
    },
    data() {
      return {
        dataObj: {
          policy: '',
          signature: '',
          key: '',
          ossaccessKeyId: '',
          dir: '',
          host: '',
          // callback:'',
        },
        dialogVisible: false
      };
    },
    methods: {
      emitInput(val) {
        this.$emit('input', val)
      },
      handleRemove(file, fileList) {
        this.emitInput('');
      },
      handlePreview(file) {
        this.dialogVisible = true;
      },
      beforeUpload(file) {
        let _self = this;
        return new Promise((resolve, reject) => {
          policy().then(response => {
            _self.dataObj.policy = response.data.policy;
            _self.dataObj.signature = response.data.signature;
            _self.dataObj.ossaccessKeyId = response.data.accessid;
            _self.dataObj.key = response.data.dir +getUUID()+'_${filename}';
            _self.dataObj.dir = response.data.dir;
            _self.dataObj.host = response.data.host;
            resolve(true)
          }).catch(err => {
            reject(false)
          })
        })
      },
      handleUploadSuccess(res, file) {
        console.log("上传成功...")
        this.showFileList = true;
        this.fileList.pop();
        this.fileList.push({name: file.name, url: this.dataObj.host + '/' + this.dataObj.key.replace("${filename}",file.name) });
        this.emitInput(this.fileList[0].url);
      }
    }
  }
</script>
<style>

</style>
```

`action为你的oss对象存储的外网地址，一定要修改！！！`  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404826180.png)  
`请确认你的响应体有如下内容，否则修改成自己的`  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404826516.png)

##### 多文件： #####

`multiUpload.vue`

```vue
<template>
  <div>
    <el-upload
      action="http://gulimall.oss-cn-shanghai.aliyuncs.com"
      :data="dataObj"
      list-type="picture-card"
      :file-list="fileList"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-preview="handlePreview"
      :limit="maxCount"
      :on-exceed="handleExceed"
    >
      <i class="el-icon-plus"></i>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt />
    </el-dialog>
  </div>
</template>
<script>
import { policy } from "./policy";
import { getUUID } from '@/utils'
export default {
  name: "multiUpload",
  props: {
    //图片属性数组
    value: Array,
    //最大上传图片数量
    maxCount: {
      type: Number,
      default: 30
    }
  },
  data() {
    return {
      dataObj: {
        policy: "",
        signature: "",
        key: "",
        ossaccessKeyId: "",
        dir: "",
        host: "",
        uuid: ""
      },
      dialogVisible: false,
      dialogImageUrl: null
    };
  },
  computed: {
    fileList() {
      let fileList = [];
      for (let i = 0; i < this.value.length; i++) {
        fileList.push({ url: this.value[i] });
      }

      return fileList;
    }
  },
  mounted() {},
  methods: {
    emitInput(fileList) {
      let value = [];
      for (let i = 0; i < fileList.length; i++) {
        value.push(fileList[i].url);
      }
      this.$emit("input", value);
    },
    handleRemove(file, fileList) {
      this.emitInput(fileList);
    },
    handlePreview(file) {
      this.dialogVisible = true;
      this.dialogImageUrl = file.url;
    },
    beforeUpload(file) {
      let _self = this;
      return new Promise((resolve, reject) => {
        policy()
          .then(response => {
            console.log("这是什么${filename}");
            _self.dataObj.policy = response.data.policy;
            _self.dataObj.signature = response.data.signature;
            _self.dataObj.ossaccessKeyId = response.data.accessid;
            _self.dataObj.key = response.data.dir + getUUID()+"_${filename}";
            _self.dataObj.dir = response.data.dir;
            _self.dataObj.host = response.data.host;
            resolve(true);
          })
          .catch(err => {
            console.log("出错了...",err)
            reject(false);
          });
      });
    },
    handleUploadSuccess(res, file) {
      this.fileList.push({
        name: file.name,
        // url: this.dataObj.host + "/" + this.dataObj.dir + "/" + file.name； 替换${filename}为真正的文件名
        url: this.dataObj.host + "/" + this.dataObj.key.replace("${filename}",file.name)
      });
      this.emitInput(this.fileList);
    },
    handleExceed(files, fileList) {
      this.$message({
        message: "最多只能上传" + this.maxCount + "张图片",
        type: "warning",
        duration: 1000
      });
    }
  }
};
</script>
<style>
</style>
```

`action为你的oss对象存储的外网地址，一定要修改！！！`  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404826180.png)  
`请确认你的响应体有如下内容，否则修改成自己的`  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404826516.png)

##### 从后端获取签名 #####

`policy.js`

```vue
import http from '@/utils/httpRequest.js'
export function policy() {
   return  new Promise((resolve,reject)=>{
        http({
            url: http.adornUrl("/thirdparty/oss/policy"),
            method: "get",
            params: http.adornParams({})
        }).then(({ data }) => {
            resolve(data);
        })
    });
}
```

`url为你从前端获取签名信息的路径，如果不同，请修改`  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404827206.png)

##### 跨域问题 #####

客户端进行表单直传到OSS时，会从浏览器向OSS发送带有Origin的请求消息。OSS对带有Origin头的请求消息会进行跨域规则（CORS）的验证。因此需要为Bucket设置跨域规则以支持Post方法。

1.  登录OSS管理控制台。
2.  单击Bucket列表，然后单击目标Bucket名称。
3.  在左侧导航栏，选择权限管理 > 跨域设置，然后在跨域设置区域，单击设置。  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404827815.png)  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404828420.png)  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404828907.png)
4.  规则如下  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404829367.png)
5.  成功  
    ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/9/17/xxf-1663404830074.png)

## 关于笑小枫💕

> 本章到这里结束了，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
>
> 微信公众号：笑小枫
>
> 笑小枫个人博客：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
>
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)
>
> 本文源码：[https://github.com/hack-feng/maple-demo](https://github.com/hack-feng/maple-demo)


[https_www.aliyun.com_price_product_spm_5176.8465980.help.3.4e701450R42spo_oss_detail_ossbag]: https://www.aliyun.com/price/product?spm=5176.8465980.help.3.4e701450R42spo#/oss/detail/ossbag
[c9d0d66165bc45279c3c9a4ae376efba.png]: https://img-blog.csdnimg.cn/c9d0d66165bc45279c3c9a4ae376efba.png
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16]: https://img-blog.csdnimg.cn/e7c962c33d70440dbee7de3a5024d8cc.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 1]: https://img-blog.csdnimg.cn/11421d9db94049fa9c29b6ba7b046794.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[e98dec8900a94c97ad62acfe957c198b.png]: https://img-blog.csdnimg.cn/e98dec8900a94c97ad62acfe957c198b.png
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_17_color_FFFFFF_t_70_g_se_x_16]: https://img-blog.csdnimg.cn/2431186a5285409994e71ea811d3fa8c.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_17,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_18_color_FFFFFF_t_70_g_se_x_16]: https://img-blog.csdnimg.cn/3c5123cc89a8453b8427775f45b52f2e.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_18,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 2]: https://img-blog.csdnimg.cn/56c300a1ddd24ef0b2d19635807680a9.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 3]: https://img-blog.csdnimg.cn/e86daf05f3eb417b9b9e0b0b869255dd.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 4]: https://img-blog.csdnimg.cn/65fc53a872804e3b9088e2bb662e1815.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[528b3bb5f649481180f477a3a7023cd4.png]: https://img-blog.csdnimg.cn/528b3bb5f649481180f477a3a7023cd4.png
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 5]: https://img-blog.csdnimg.cn/1105007ff4d24e4fa53326e5662e4c56.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[https_ram.console.aliyun.com_users]: https://ram.console.aliyun.com/users
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 6]: https://img-blog.csdnimg.cn/67cd1cfe5c344dd092fa508822ab70bb.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 7]: https://img-blog.csdnimg.cn/ae5ecd9214bc4d4bb220047c15f6c581.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[aa04e0b2b22a41f9819eaff7c30b0e46.png]: https://img-blog.csdnimg.cn/aa04e0b2b22a41f9819eaff7c30b0e46.png
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 8]: https://img-blog.csdnimg.cn/15085f05376a49ec96f5bbbe9c961898.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_17_color_FFFFFF_t_70_g_se_x_16 1]: https://img-blog.csdnimg.cn/5d2c9d9de37a4d05918e1ac3bf49b7ea.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_17,color_FFFFFF,t_70,g_se,x_16
[8a1ba0d41644446b90b2179534b00e19.png]: https://img-blog.csdnimg.cn/8a1ba0d41644446b90b2179534b00e19.png
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 9]: https://img-blog.csdnimg.cn/1cbaf137b104410db744a76a092ce109.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 10]: https://img-blog.csdnimg.cn/38c26e4cafb1451b9b74206f6516cade.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 11]: https://img-blog.csdnimg.cn/b5e879f9224f48ceae8f151e397b60b7.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 12]: https://img-blog.csdnimg.cn/351255927c93489593012d3b740666d3.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 13]: https://img-blog.csdnimg.cn/d833ed93eb034ca9813da1fd0be1da18.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[https_github.com_alibaba_aliyun-spring-boot_tree_master_aliyun-spring-boot-samples_aliyun-oss-spring-boot-sample]: https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-oss-spring-boot-sample
[https_help.aliyun.com_document_detail_31926.html]: https://help.aliyun.com/document_detail/31926.html
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_18_color_FFFFFF_t_70_g_se_x_16 1]: https://img-blog.csdnimg.cn/bd923b7bdee5421fac1186241a418733.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_18,color_FFFFFF,t_70,g_se,x_16
[https_help.aliyun.com_document_detail_91868.htm_spm_a2c4g.11186623.0.0.1607c9277SiG3G_concept-ahk-rfz-2fb]: https://help.aliyun.com/document_detail/91868.htm?spm=a2c4g.11186623.0.0.1607c9277SiG3G#concept-ahk-rfz-2fb
[9d28c80c1ab248eba7bd1091b90ed9af.png]: https://img-blog.csdnimg.cn/9d28c80c1ab248eba7bd1091b90ed9af.png
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 14]: https://img-blog.csdnimg.cn/ffea4a58354f49fdbdf5c30c24da7e14.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_19_color_FFFFFF_t_70_g_se_x_16]: https://img-blog.csdnimg.cn/891d9a94379646a5a80c7e64deb583c3.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_19,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_14_color_FFFFFF_t_70_g_se_x_16]: https://img-blog.csdnimg.cn/e2105c1686d44383a28fe44a25d90469.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_14,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 15]: https://img-blog.csdnimg.cn/d6861c8b461d4ec49f54aaf0e74eb482.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_14_color_FFFFFF_t_70_g_se_x_16 1]: https://img-blog.csdnimg.cn/f6ee89b1a8b74b0790ed1a4f91ad5849.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_14,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 16]: https://img-blog.csdnimg.cn/8b89c974770b4f06a1b2c1e093cec2f0.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16
[watermark_type_d3F5LXplbmhlaQ_shadow_50_text_Q1NETiBAbGlhbmdqaWF5eQ_size_20_color_FFFFFF_t_70_g_se_x_16 17]: https://img-blog.csdnimg.cn/2552ba1a24e2444fb4d18a4b3d5e0708.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAbGlhbmdqaWF5eQ==,size_20,color_FFFFFF,t_70,g_se,x_16