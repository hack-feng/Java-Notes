## 本文简介

相信大家在日常工作中都遇到过加密的场景吧，像登录密码加密保存、无token接口验签、数据加密传输等等。

本文将详细的介绍一下加密的方式，并分析使用场景，并会以详细的代码完整的介绍如何使用加密，让小伙伴们遇到加密时，有更好的选择。

![img](https://image.xiaoxiaofeng.site/blog/2023/06/13/xxf-20230613093240.jpeg?xxfjava)

## 加密方式有哪些？🧐

![img](https://image.xiaoxiaofeng.site/blog/2023/06/13/xxf-20230613093706.jpeg?xxfjava)

### 双向加密

双向加密是可逆的，多用于数据传输，主要分为`对称秘钥加密`、`非对称秘钥加密`

1. 对称秘钥加密

   对称密钥加密，就是采用这种加密方法的双方使用方式用同样的密钥进行加密和解密。密钥是控制加密及解密过程的指令。

   常用的对称加密有：DES、IDEA、RC2、RC4、SKIPJACK、RC5、AES算法等

2. 非对称秘钥加密

   与对称加密算法不同，非对称加密算法需要两个密钥：公开密钥（publickey）和私有密钥 （privatekey）。公开密钥与私有密钥是一对，如果用公开密钥对数据进行加密，只有用对应的私有密钥才能解密；如果用私有密钥对数据进行加密，那么只有用对应的公开密钥才能解密。因为加密和解密使用的是两个不同的密钥，所以这种算法叫作非对称加密算法。

   常见的非对称加密有：RSA、DSA算法等


### 单向加密

单向加密是不可逆的，主要用作密码保存，数据验签等，我们无需知道加密前的数据是什么？只要我们用目标数据加密后和密文比较是否一致。

常见的单向加密有：MD5、SHA算法

本文主要介绍了常见的需要加密处理场景下，如何选择加密方式，主要从以下几个方面介绍：

* 数据加密储存（MD5+盐值；不可逆，永久有效）
* 接口验签（SHA+盐值；不可逆，有时效性）
* 数据加密传输（RSA；数据可逆，公钥加密，私钥解密）

接下来，我们一起看一下各种加密的使用场景和实现方式；

本文涉及到的机密主要用`org.springframework.util.DigestUtils`类下的方法。

## 数据加密储存

说到数据加密，最先想到的就是MD5了吧。

~~~java
    /**
     * MD5加密处理
     *
     * @param password 密码明文
     * @return 加密后密文
     */
    public static String md5(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
~~~

`123456`加密后的密码为：`e10adc3949ba59abbe56e057f20f883e`

是不是很熟悉的样子，之前接触过一个小项目，放完望去，数据库密密麻麻的全是这个值😂

![img](https://image.xiaoxiaofeng.site/blog/2023/06/13/xxf-20230613093258.png?xxfjava)

同一个密码加密后，加密后的结果也一致，这样存储数据岂不是也不安全，显然只是单纯的MD5加密并不能满足我们的需求。那我们怎么处理呢？

我们可以尝试添加一个盐值，然后再对数据进行加密，每个用户随机生成一个盐值，然后根据`数据+盐`的形式加密，然后加密后的数据和盐值都落库，这样就解决了同一个密码加密后的结果也不一样了，我们看一下怎么实现，下面提供了一个实现工具类，供大家参考👇

~~~java
package com.maple.demo.util.common;

import com.maple.demo.config.bean.ErrorCode;
import com.maple.demo.config.exception.MapleCommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;


/**
 * MD5撒盐加密 及MD5加密
 *
 * @author 笑小枫
 * @date 2022/7/20
 * @see <a href="https://www.xiaoxiaofeng.com">https://www.xiaoxiaofeng.com</a>
 */
@Slf4j
public class Md5Util {

    private Md5Util() {

    }

    public static void main(String[] args) {
        String password = "123456";
        log.info("普通MD5加密：" + md5(password));
        String salt = "xxx";
        String cipherText = encrypt(password, salt);
        log.info("MD5加盐加密后的密文：" + cipherText);
        boolean isOk = verifyPassword(password, cipherText, salt);
        log.info("校验密码结果：" + isOk);
    }

    /**
     * MD5加密处理
     *
     * @param password 密码明文
     * @return 加密后密文
     */
    public static String md5(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    /**
     * 密码加密处理
     *
     * @param password 密码明文
     * @param salt     盐
     * @return 加密后密文
     */
    public static String encrypt(String password, String salt) {
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(salt)) {
            log.error("密码加密失败原因： password and salt cannot be empty");
            throw new MapleCommonException(ErrorCode.PARAM_ERROR);
        }
        return DigestUtils.md5DigestAsHex((salt + password).getBytes());
    }

    /**
     * 校验密码
     *
     * @param target 待校验密码
     * @param source 原密码
     * @param salt   加密原密码的盐
     */
    public static boolean verifyPassword(String target, String source, String salt) {
        if (StringUtils.isEmpty(target) || StringUtils.isEmpty(source) || StringUtils.isEmpty(salt)) {
            log.info("校验密码失败，原因 target ={}， source ={}， salt ={}", target, source, salt);
            return false;
        }
        String targetEncryptPwd = encrypt(target, salt);
        return targetEncryptPwd.equals(source);
    }
}

~~~

这只是一种简单的利用MD5的实现方式，还有很多实现方式，像使用SHA或者自己写加密算法等等，下面我们来看看使用SHA方式加密。

## 接口验签

接口验签主要用于和第三方接口交互的时候，使用的一种方式，其实就是用不可逆加密的形式加了一个时间的有效期。这里用SHA跟大家演示一下，当然也是可以用上文的MD5的形式。

数据传输格式如下：

 * 签名处理工具类

 * 格式sign_yyyyMMddHHmmss

 * sign：加密后的数据

 * _：用于区分数据，方便后端分割处理，当然也可以使用两个参数传输

 * yyyyMMddHHmmss：当前时间，用于验证签名有效时间

 * <p>

 * 其中sign=SHA256(data&yyyyMMddHHmmss&salt)

 * data：建议放唯一的业务数据

 * yyyyMMddHHmmss：和上文的时间要一致

 * salt：盐值，可随机生产，妥善保管

工具类中用到了自己封装的时间处理工具类，详情见[https://www.xiaoxiaofeng.com/archives/javadate](https://www.xiaoxiaofeng.com/archives/javadate)

自定义异常可以自己替换成RuntimeException，或者见[https://www.xiaoxiaofeng.com/archives/springboot05](https://www.xiaoxiaofeng.com/archives/springboot05)中自定义异常部分

~~~java
package com.maple.demo.util.common;

import com.maple.demo.config.bean.ErrorCode;
import com.maple.demo.config.exception.MapleCheckException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 签名处理工具类
 * 格式sign_yyyyMMddHHmmss
 * sign：加密后的数据
 * _：用于区分数据，方便后端分割处理，当然也可以使用两个参数传输
 * yyyyMMddHHmmss：当前时间，用于验证签名有效时间
 * <p>
 * 其中sign=SHA256(data&yyyyMMddHHmmss&salt)
 * data：建议放唯一的业务数据
 * yyyyMMddHHmmss：和上文的时间要一致
 * salt：盐值，可随机生产，妥善保管
 *
 * @author 笑小枫
 * @date 2022/7/20
 * @see <a href="https://www.xiaoxiaofeng.com">https://www.xiaoxiaofeng.com</a>
 */
@Slf4j
public class SignUtil {

    private SignUtil() {
    }

    public static void main(String[] args) {
        String data = "XXF001";
        String date = DateUtil.dateToStr(new Date(), DateUtil.YYYYMMDDHHMMSS);
        String salt = "d84d9e1fe49d427da66fd78724dcfc29";
        String ciphertext = DigestUtils.sha256Hex(data + "&" + date + "&" + salt);
        log.info("加密后的密文：" + ciphertext);
        checkCangoSign(data, ciphertext + "_" + date, salt, 300L);
    }

    /**
     * 灿谷签名验证
     * 格式sign_yyyyMMddHHmmss
     * 其中sign=sha256Hex(unionId&yyyyMMddHHmmss&salt)
     *
     * @param businessData 业务数据
     * @param sign         签名信息
     * @param salt         加密盐值
     * @param expireSecond 有效期(秒)
     */
    public static void checkCangoSign(String businessData, String sign, String salt, Long expireSecond) {
        if (StringUtils.isBlank(businessData) || StringUtils.isBlank(sign)) {
            log.error(String.format("验签失败，businessData或sign为空，businessData：%s；sign：%s", businessData, sign));
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "参数有误，请检查后重试");
        }
        String[] signArray = sign.split("_");
        // 签名拆分后为两部分
        int singNum = 2;

        Date reqDate = DateUtil.strToDate(signArray[1], DateUtil.YYYYMMDDHHMMSS);
        Date nowDate = new Date();

        if (reqDate == null || (signArray.length != singNum || secondBetween(reqDate, nowDate) > expireSecond)) {
            log.error(String.format("验签失败，sign无效，businessData：%s；sign：%s", businessData, sign));
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "sign无效");
        }

        if (secondBetween(nowDate, reqDate) > expireSecond) {
            log.error(String.format("验签失败，sign已过期，businessData：%s；sign：%s", businessData, sign));
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "sign已过期，请重试");
        }
        String checkDataSha256 = DigestUtils.sha256Hex(businessData + "&" + signArray[1] + "&" + salt);
        if (!signArray[0].equalsIgnoreCase(checkDataSha256)) {
            log.error(String.format("验签失败，签名验证失败，businessData：%s；sign：%s", businessData, sign));
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "签名验证失败");
        }
    }

    /**
     * 获取两个时间之间相差的秒数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 相差的时间（秒）
     */
    private static long secondBetween(Date startDate, Date endDate) {
        return (startDate.getTime() - endDate.getTime()) / 1000;
    }
}

~~~

## 数据加密传输

什么时候需要数据加密传输呢，比如A系统有一批数据要给B系统，A系统和B系统中没有任何的限制，谁都可以调用，这种场景就需要对接口进行验签操作，对传输的数据进行数据加密，保证数据的安全。

这个时候数据加密就不能使用单向加密了，因为B系统拿到数据后要进行数据解密，所以要用双向加密，这里演示一下非对称的双向加密RSA。

通过生成一对密钥，根据密钥进行数据加密，一般公钥加密，私钥解密，像上文的例子，A系统应该持有公钥加密，B系统持有私钥解密。

![img](https://image.xiaoxiaofeng.site/blog/2023/06/13/xxf-20230613093135.png?xxfjava)

详细实现见代码👇

~~~java
package com.maple.demo.util.common;

import com.alibaba.fastjson.JSON;
import com.maple.demo.config.bean.ErrorCode;
import com.maple.demo.config.exception.MapleCommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加解密处理工具类
 *
 * @author 笑小枫
 * @date 2022/7/20
 * @see <a href="https://www.xiaoxiaofeng.com">https://www.xiaoxiaofeng.com</a>
 */
@Slf4j
public class RsaUtil {

    /**
     * 指定加密算法RSA非对称加密
     */
    private static final String RSA = "RSA";

    /**
     * 指定RSA非对称算法/ECB模式/填充方式
     * 默认填充方式：RSA/ECB/PKCS1Padding
     */
    private static final String RSA_CIPHER = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";

    public static void main(String[] args) {
        Map<String, String> keys = createKeys();
        log.info("密钥对：" + JSON.toJSONString(keys));
        try {
            String ciphertext = publicEncrypt("123abcABC", keys.get("publicKey"));
            log.info("加密后的数据：" + ciphertext);
            String original = privateDecrypt(ciphertext, keys.get("privateKey"));
            log.info("解密后的原文：" + original);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Map<String, String> createKeys() {
        //为RSA算法创建一个KeyPairGenerator对象（KeyPairGenerator，密钥对生成器，用于生成公钥和私钥对）
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA);
        } catch (NoSuchAlgorithmException e) {
            log.error("获取公钥私钥发生错误", e);
            throw new IllegalArgumentException("No such algorithm-->[" + RSA + "]");
        }

        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(2048);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        //返回一个publicKey经过二次加密后的字符串
        String publicKeyStr = Base64Utils.encodeToString(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        //返回一个privateKey经过二次加密后的字符串
        String privateKeyStr = Base64Utils.encodeToString(privateKey.getEncoded());

        Map<String, String> keyPairMap = new HashMap<>(16);
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        return keyPairMap;
    }

    /**
     * 得到公钥
     *
     * @param publicKey 密钥字符串（经过base64编码）
     */
    private static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64Utils.decodeFromString(publicKey));
        return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
    }

    /**
     * 得到私钥
     *
     * @param privateKey 密钥字符串（经过base64编码）
     */
    private static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64Utils.decodeFromString(privateKey));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * 公钥加密
     */
    public static String publicEncrypt(String data, String publicKey) {
        try {
            byte[] dataByte = data.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance(RSA_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
            return Base64Utils.encodeToString(cipher.doFinal(dataByte));
        } catch (Exception e) {
            log.error("加密字符串遇到异常", e);
            throw new MapleCommonException(ErrorCode.COMMON_ERROR, "加密字符串遇到异常");
        }
    }

    /**
     * 私钥解密
     */
    public static String privateDecrypt(String data, String privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
            return new String(cipher.doFinal(Base64Utils.decodeFromString(data)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("解密字符串遇到异常", e);
            throw new MapleCommonException(ErrorCode.COMMON_ERROR, "解密字符串遇到异常");
        }
    }
}

~~~

## 关于笑小枫💕

> 本章到这里结束了，喜欢的朋友关注一下我呦😘😘，大伙的支持，就是我坚持写下去的动力。
> 老规矩，懂了就点赞收藏；不懂就问，日常在线，我会就会回复哈~🤪
> 微信公众号：笑小枫
> 笑小枫个人博客：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)
> 本文源码：[https://github.com/hack-feng/maple-demo](https://github.com/hack-feng/maple-demo) 

