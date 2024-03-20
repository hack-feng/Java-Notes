![image-20240318092538298](https://image.xiaoxiaofeng.site/blog/2024/03/18/xxf-20240318092538.png?xxfjava)

## 文件转base64字符串

~~~java
/**
 * file转换为base64
 * 注意：这里转换为base64后，是不包含文件head头的
 */
public static String fileToBase64(File file) {
    Base64.Encoder base64 = Base64.getEncoder();
    String base64Str = null;
    try (FileInputStream fis = new FileInputStream(file);
         ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        byte[] b = new byte[1024];
        int n;
        while ((n = fis.read(b)) != -1) {
            bos.write(b, 0, n);
        }
        base64Str = base64.encodeToString(bos.toByteArray());
    } catch (IOException e) {
        e.printStackTrace();
    }
    return base64Str;
}
~~~

## base64字符串转文件
~~~java

/**
 * base64转化为file，并保存到指定路径下
 */
public static void base64ToFile(String base, String path) {
    if (StringUtils.isBlank(base)) {
        return;
    }
    Base64.Decoder decoder = Base64.getDecoder();
    try (OutputStream out = new FileOutputStream(path)) {
        byte[] bytes = decoder.decode(base);
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }
        out.write(bytes);
        out.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
~~~

## base64转化为file流

~~~java
/**
 * base64转化为file流
 */
public static File base64ToFile(String base64) {
    if (base64 == null || "".equals(base64)) {
        return null;
    }
    byte[] buff = Base64.getDecoder().decode(base64);

    File file;
    try {
        file = File.createTempFile("tmp", null);
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
    try (FileOutputStream out = new FileOutputStream(file)) {
        out.write(buff);
    } catch (IOException e) {
        e.printStackTrace();
    }
    return file;
}
~~~

## base64转MultipartFile

~~~java
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;

/**
 * @author 笑小枫
 */
public class Base64DecodedMultipartFile implements MultipartFile {
    private final byte[] imgContent;
    private final String header;
    private final String fileName;

    public Base64DecodedMultipartFile(byte[] imgContent, String header, String fileName) {
        this.imgContent = imgContent;
        this.header = header.split(";")[0];
        this.fileName = fileName;
    }

    @Override
    public String getName() {
        return fileName + "." + header.split("/")[1];
    }

    @Override
    public String getOriginalFilename() {
        return fileName + "." + header.split("/")[1];
    }

    @Override
    public String getContentType() {
        return header.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(imgContent);
        }
    }

    /**
     * base64转multipartFile
     **/
    public static MultipartFile base64Convert(String base64, String header, String fileName) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] b = decoder.decode(base64);
        //取索引为1的元素进行处理
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        // 处理过后的数据通过Base64DecodeMultipartFile转换为MultipartFile对象
        return new Base64DecodedMultipartFile(b, header, fileName);
    }
}
~~~

## byte数组转MultiPartFile

1. POM导入
~~~pom
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.5</version>
</dependency>
<dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-test</artifactId>
      <version>RELEASE</version>
</dependency>
~~~
2. 代码部分
~~~java
byte[] bytes = message.getPacket();
InputStream inputStream = new ByteArrayInputStream(bytes);
MultipartFile file = new MockMultipartFile(ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
~~~

## MultipartFile转化为byte数组

~~~java
byte[] bytes= file.getBytes();
~~~