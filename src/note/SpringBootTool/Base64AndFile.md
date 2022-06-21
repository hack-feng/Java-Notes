### 文件转base64字符串
~~~java
    /**
     * 文件转base64字符串
     * @param file
     * @return
     */
    public static String fileToBase64(File file) {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            int length = in.read(bytes);
            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return base64;
    }
~~~

### base64字符串转文件
~~~java
    /**
     * base64字符串转文件
     * @param base64
     * @return
     */
    public static File base64ToFile(String base64) {
        File file = null;
        String fileName = "/Petssions/record/testFile.amr";
        FileOutputStream out = null;
        try {
            // 解码，然后将字节转换为文件
            file = new File(Environment.getExternalStorageDirectory(), fileName);
            if (!file.exists())
                file.createNewFile();
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);// 将字符串转换为byte数组
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            byte[] buffer = new byte[1024];
            out = new FileOutputStream(file);
            int bytesum = 0;
            int byteread = 0;
            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread); // 文件写操作
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (out!= null) {
                    out.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return file;
    }
~~~

### base64转MultipartFile
~~~java
package org.cango.user.center.util;

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
