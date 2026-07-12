## 1. 什么是飞算JavaAI

先看一下官方的介绍吧，一天让你成为Java高手，以前是十行八行抄，现在是代码全靠AI，需求分析->软件设计->工程代码生成，一起呵成，主打的就是一个字，**快**！

> 飞算JavaAI是飞算科技推出的一款革命性的智能开发工具，它能够通过自然语言或者语音输入，帮助开发者自动完成需求分析、软件设计、工程代码生成等全过程。它不像传统的代码生成工具只能生成简单的代码片段，而是能够生成完整的工程代码，包括配置类文件、Java源代码目录、资源文件以及测试文件等。这种“全流程”智能化的能力无疑将大大提升开发者的效率，尤其对于那些时间紧迫、任务繁重的项目，飞算JavaAI能够有效减轻开发者的负担。
>
>   记得我第一次接触到飞算JavaAI时，内心充满了兴奋和好奇，尤其是它能够通过语音或文字输入需求，自动生成工程代码这一功能，让我感受到了“未来科技”的魅力。之前，作为开发者，我常常需要花费大量时间在一些重复性的编码工作上，像是配置文件的编写、模块间的代码连接等，这些工作虽然重要，但也极其消耗精力。而飞算JavaAI的出现，就像是一个得力的助手，让我能将更多精力集中在业务逻辑和技术创新上。
>

![image-20250711173405465](https://image.xiaoxiaofeng.site/blog/image/image-20250711173405465.png?xiaoxiaofeng)

让我们看下飞算AI的整体框架和流程吧。

![image-20250711174514172](https://image.xiaoxiaofeng.site/blog/image/image-20250711174514172.png?xiaoxiaofeng)

## 2. 飞算JavaAI安装注册

首先在我们的ide里面安装飞算JavaAI的插件。如下图所示，直接在Plugins里面搜索飞算即可。

![image-20250711173230937](https://image.xiaoxiaofeng.site/blog/image/image-20250711173230937.png?xiaoxiaofeng)

安装完需要登录飞算JavaAI，如果没有注册账号，我们需要使用手机号进行注册一下。

![image-20250711173457602](https://image.xiaoxiaofeng.site/blog/image/image-20250711173457602.png?xiaoxiaofeng)

注册完成后，我们登录一下飞算JavaAI进行授权。

![image-20250711173742554](https://image.xiaoxiaofeng.site/blog/image/image-20250711173742554.png?xiaoxiaofeng)

## 3. 飞算JavaAI初体验

登录之后就可以使用我们的飞算JavaAI工具来辅助我们编码了，下面让我们一起看看效果吧。

最近在和第三方对接，直接来体验吧~

输入命令：

~~~
帮我写一个和第三方接口对接的加密方式，使用Aes加密，同时校验接口3分钟有效期
~~~

![image-20250711174057232](https://image.xiaoxiaofeng.site/blog/image/image-20250711174057232.png?xiaoxiaofeng)

短短的时间示例代码就出来了，如果没有问题我们可以直接接受使用，如果存在问题可以拒绝，重新生成。

当然我们的提示词约精确，生成出来的内容也会更加准确，我们这里只是进行一下简单的体验，效果还是不错的。

![image-20250711174212705](https://image.xiaoxiaofeng.site/blog/image/image-20250711174212705.png?xiaoxiaofeng)

最后附上生成的代码，让我们大伙一起对它进行一个评价吧，纯原生，未修改~

~~~java
package com.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Base64;

public class Aes {
    // AES 加密密钥（需要根据实际情况替换）
    private static final String SECRET_KEY = "your-secret-key-123456"; // 16字节的密钥

    // 加密方法
    public static String encrypt(String plainText) throws Exception {
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    // 解密方法
    public static String decrypt(String encryptedText) throws Exception {
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedData);
    }

    // 校验接口有效期（3分钟）
    public static boolean isValidRequest(String timestampStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date requestTime = sdf.parse(timestampStr);
            Date currentTime = new Date();
            long timeDifference = currentTime.getTime() - requestTime.getTime();
            return timeDifference <= 3 * 60 * 1000; // 3分钟
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
~~~

## 4. 本文小结

飞算JavaAI整体使用下来，效果还是挺不错的，生成的速度也很快，没有体验过的小伙伴速速去体验吧~