## 功能介绍

在日常数据使用过程中，经常需要对一些隐私数据进行脱敏处理，如姓名、手机号、身份证号、邮箱等

这里写了两个通用的脱敏方法，可以灵活方便的对数据进行脱敏处理

两种模式：

* 保留前面几位，后面几位 `vagueHandle`；例如手机号保留前3后4等
* 从第几位开始到第几位结束进行掩码`exactHandle`;例如姓名第2位掩码处理等

## 详细代码实现

直接复制就可以测试使用，应该可以覆盖大多数场景，如不满足，可以联系我继续补充。或者基于代码调整。

```java
package com.maple.rest.controller.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 笑小枫
 * @date 2022/7/4
 */
@Slf4j
public class TestSensitiveController {

    public static void main(String[] args) {
        log.info(exactHandle("小枫", 2, 2));
        log.info(exactHandle("笑小枫", 2, 2));
        log.info(exactHandle("笑小枫枫枫", 4, 5));
        log.info(exactHandle("笑小枫", 1, 3));

        log.info(vagueHandle("18300008888", 1, 8));
        log.info(vagueHandle("18300008888", 3, 4));
        log.info(vagueHandle("371324199009101110", 6, 4));

    }

    /**
     * 保留左边left位和右边right位，其他掩码（'*'）处理
     * eg:value:18300008888  left:3  right:4  return:183****8888
     *
     * @param value 需要处理的值
     * @param left  左边保留几位
     * @param right 右边保留几位
     * @return 处理后的值
     */
    private static String vagueHandle(String value, int left, int right) {
        int length = StringUtils.length(value);
        if (left + right > length) {
            return value;
        }
        return StringUtils.left(value, left).concat(
                StringUtils.leftPad(StringUtils.right(value, right), length - left, "*"));
    }

    /**
     * 第left位至第right位做掩码处理
     * 包含begin和end
     * eg:value:笑小枫  begin:2  end:2  return:笑*枫
     * eg:value:笑小枫  begin:1  end:2  return:**枫
     *
     * @param value 需要处理的值
     * @param begin 第几位开始
     * @param end   第几位结束
     * @return 处理后的值
     */
    private static String exactHandle(String value, int begin, int end) {
        begin = begin - 1;
        int length = StringUtils.length(value);
        if (end <= begin || begin > length) {
            return value;
        }
        return StringUtils.left(value, begin).concat(
                StringUtils.leftPad(StringUtils.right(value, length - end), length - begin, "*"));
    }
}

```

## 测试结果

执行结果如下：

![笑小枫-www.xiaoxiaofeng.site](http://file.xiaoxiaofeng.site/blog/image/2022/07/05/20220705110151.png)



## 涉及到的StringUtils方法介绍

```
// 获取字符串左侧指定长度的字符串，第⼀个参数：原字符串，第⼆个参数：取左侧字符数量
StringUtils.left(String str, int len)

// 获取字符串右侧指定长度的字符串，第⼀个参数：原字符串，第⼆个参数：取右侧字符数量
StringUtils.right(String str, int len)

// 左侧补齐：第⼀个参数：原始字符串，第⼆个参数：字符串的长度，第三个是补充的字符串
StringUtils.leftPad(String str, int size, String padStr)

// 右侧补齐：第⼀个参数：原始字符串，第⼆个参数：字符串的长度，第三个是补充的字符串
StringUtils.rightPad(String str, int size, String padStr)
```

## 关于笑小枫

> 本章到这里结束了，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
> 老规矩，懂了就点赞收藏；不懂就问，日常在线，我会就会回复哈~
> 后续文章会陆续更新，文档会同步在微信公众号、个人博客、CSDN和GitHub保持同步更新。
> 微信公众号：笑小枫
> 笑小枫个人博客：[http://www.xiaoxiaofeng.site](http://www.xiaoxiaofeng.site)
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)
> GitHub文档：[https://github.com/hack-feng/Java-Notes](https://github.com/hack-feng/Java-Notes) 