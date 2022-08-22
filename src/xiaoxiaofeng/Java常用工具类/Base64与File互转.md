~~~java
package com.maple.demo.util;


import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Base64;

/**
 * .doc——data:application/msword;base64,
 * .docx——data:application/vnd.openxmlformats-officedocument.wordprocessingml.document;base64,
 * .xls——data:application/vnd.ms-excel;base64,
 * .xlsx——data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,
 * .pdf——data:application/pdf;base64,
 * .ppt——data:application/vnd.ms-powerpoint;base64,
 * .pptx——data:application/vnd.openxmlformats-officedocument.presentationml.presentation;base64,
 * .txt——data:text/plain;base64,
 * <p>
 * 图片类
 * .png——data:image/png;base64,
 * .jpg——data:image/jpeg;base64,
 * .gif——data:image/gif;base64,
 * .svg——data:image/svg+xml;base64,
 * .ico——data:image/x-icon;base64,
 * .bmp——data:image/bmp;base64,
 *
 * @author 笑小枫
 * @date 2022/7/22
 * @see <a href="https://www.xiaoxiaofeng.com">https://www.xiaoxiaofeng.com</a>
 */
public class Base64Util {

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

    public static void main(String[] args) {
        // 处理图片信息，将图片转为base64字符串
        File file = new File("D:\\xiaoxiaofeng.jpg");
        String base64 = fileToBase64(file);
        base64ToFile(base64, "D:\\test\\xiaoxiaofeng.jpg");
        File fileTemp = base64ToFile(base64);
        fileToBase64(fileTemp);
    }
}
~~~

