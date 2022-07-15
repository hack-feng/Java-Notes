字符串是在程序开发中最常见的，Apache Commons开源项目在org.apache.commons.lang3包下提供了StringUtils工具类，该类相当于是对jdk自带的String类的增强，主要做了几方面的处理：

1. 核心设计理念就是对于null的进行内部处理，使用时不再需要进行繁琐的null值判定，同时也避免抛出空指针的异常

2. 作为工具类，通过增加额外处理的方式，尽量避免抛出异常，例如截取字符串，如指定长度超过了最大长度，不会抛出异常

3. 通过更多的重载方法，实现了忽略大小写、增加控制范围（左侧、右侧、中间）等常用操作

4. 增加了批量功能，如批量判定、批量比对等（个人认为批量功能适用的场景会比较少）

5. 通过对基本功能的综合应用，增加可直接实现特定开发场景的功能方法

 

整个工具类经过多年不断积累，非常庞大，官方按照功能，分了二十几个大类，方法总数达到了200多个，但其实常用和实用的其实并不没有那么多，因此从使用的角度，进行了重新整理，以便更好地利用起来，转换为开发生产力。

几个概念先明确下：

1. null：String是引用类型而不是基本类型，所以取值可以为null

2. 空串：即不包含任何元素的字符串，表示为""

3. 空格：即" ",对应ascii码是32

4. 空白字符：是一组非可见的字符，对于文本处理来说，除了空格外，通常包括tab（\t）、回车（\r）、换行（\n）、垂直制表符(\v)、换页符(\f)，当然，windows系统常用的回车换行的组合(\r\n)也算在其中。

 

 官方文档地址：http://commons.apache.org/proper/commons-lang/javadocs/api-3.8.1/index.html

以下是个人根据开发经验，站在使用者角度，重新整理和归类。
注：

1. 以下整理方法排除掉了官方标记为过时的方法

2. 涉及到正则表达式的方法，官方从StringUtils中移除，更换到了RegExUtils类中，不再在StringUtils中体现。

 ![笑小枫-www.xiaoxiaofeng.site](http://file.xiaoxiaofeng.site/blog/image/2022/07/05/20220705131339.png)

## 1 判断与验证

根据特定规则判断，不改变字符串自身

### 1.1判断是否包含内容

提供了两类方法，empty类和blank类，区别在对于前者只有为null或空串时，才返回true，而后者则会包含所有空白字符（空格、tab、换行、回车等）的判定，使用时需根据要实现的目的来选择具体用哪一个。

两大类方法完全类似，只列出empty类方法

签名：

~~~java
public static boolean isEmpty(CharSequence cs)
~~~

说明：

只有为null或空串时，才返回true

示例：

~~~java
StringUtils.isEmpty(null); // true

StringUtils.isEmpty("");  // true
~~~

扩展：

~~~java
//判定是否非空串

public static boolean isNotEmpty(CharSequence cs)

//批量判断，不定数量参数或数组

public static boolean isAnyEmpty(CharSequence... css)

public static boolean isAllEmpty(CharSequence... css)

public static boolean isNoneEmpty(CharSequence... css)
~~~

 

### 1.2 比较字符串 

基本用法equals、equalsIgnoreCase、compareTo、compareToIgnoreCase，同jdk.

扩展点：

1）扩展equalsAny，可批量进行匹配判定，几乎不会用到，建议使用集合的contains方法更自然。

2）通过compareTo重载函数，可以指定null对象的优先级

### 1.3 检验字符串
通过一系列实现好的方法，来快速返回是否符合特定规则

~~~java
//判断是否只包含unicode字符（注意：汉字也是unicode字符）
public static boolean isAlpha(CharSequence cs)
//判断是否只包含unicode字符及空格
public static boolean isAlphaSpace(CharSequence cs)
//判断是否只包含unicode字符及数字
public static boolean isAlphanumeric(CharSequence cs)
//判断是否只包含unicode字符、数字及空格
public static boolean isAlphanumericSpace(CharSequence cs)
//判断是否只包含数字及空格
public static boolean isNumericSpace(CharSequence cs)
//判断是否只包含可打印的ascii码字符（注意，空格不属于范围内）
public static boolean isAsciiPrintable(CharSequence cs)
//判断是否为数字（注意：小数点和正负号，都会判定为false）
public static boolean isNumeric(CharSequence cs)
//判定是否只包括空白字符
public static boolean isWhitespace(CharSequence cs)
//判定是否全部为大写
public static boolean isAllUpperCase(CharSequence cs)
//判定是否全部为小写
public static boolean isAllLowerCase(CharSequence cs)
//判定是否混合大小写（注意：包含其他字符，如空格，不影响结果判定）
public static boolean isMixedCase(CharSequence cs)
~~~

### 1.4 包含字符串
contains，同jdk

~~~java
public static boolean contains(CharSequence seq,int searchChar)
public static boolean contains(CharSequence seq,CharSequence searchSeq)
~~~


扩展：

~~~java
//忽略大小写
public static boolean containsIgnoreCase(CharSequence str,CharSequence searchStr)
//是否包含空白字符
public static boolean containsWhitespace(CharSequence seq)
//只包含指定字符
public static boolean containsOnly(CharSequence cs,char... searchChars)
public static boolean containsOnly(CharSequence cs,CharSequence searchChars)
//批量判断包含任意一个
public static boolean containsAny(CharSequence cs,char... searchChars)
public static boolean containsAny(CharSequence cs,CharSequence searchChars)
//批量判断不包含任何一个
public static boolean containsNone(CharSequence cs,char... searchChars)
public static boolean containsNone(CharSequence cs,CharSequence searchChars)
~~~

### 1.5 起止字符判定

~~~java
//startWith
public static boolean startsWith(CharSequence str,CharSequence prefix)
public static boolean startsWithIgnoreCase(CharSequence str,CharSequence prefix)
public static boolean startsWithAny(CharSequence sequence,CharSequence... searchStrings)
//endWith
public static boolean endsWith(CharSequence str,CharSequence suffix)
public static boolean endsWithIgnoreCase(CharSequence str,CharSequence suffix)
public static boolean endsWithAny(CharSequence sequence,CharSequence... searchStrings)
~~~

## 2 处理字符串

不改变字符串实质内容，对首尾以及中间的空白字符进行处理 

### 2.1 移除空白字符

去除首尾的空白字符，提供了两类方法，strip类和trim类,

trim类与jdk差异不大，去除包含控制字符（ascii码<=32）在内的控制字符（底层应用没做过，有可能会用到控制字符吧），主要是增加了对null的处理。

strip类则做了很多增强，通过重载方法实现了很多其他功能，建议在开发中使用strip类。

注意：全角空格并不在处理范围内。

签名：

~~~java
public static String strip(String str)
~~~

扩展：

~~~java
//去除并转化为null或empty

public static String stripToNull(String str)

public static String stripToEmpty(String str)

//去除指定字符串

public static String strip(String str,String stripChars)

//控制去除范围

public static String stripStart(String str,String stripChars)

public static String stripEnd(String str,String stripChars)

//批量操作
public static String[] stripAll(String... strs)
public static String[] stripAll(String[] strs,String stripChars) 
~~~

相关方法：

~~~java
//对字符串基本处理的复合应用，将字符串中所有空白字符去除

public static String deleteWhitespace(String str)

//去除首尾，但中间的空白字符，替换为单个空格

public static String normalizeSpace(String str)

//去除声调音标,官方举例是将 'à' 转换为'a',很生僻，基本不会用到，不确定汉语拼音的音标是否能处理

public static String stripAccents(String input)
~~~

### 2.2 去除换行
去除结尾的一处换行符，包括三种情况 \r \n \r\n

~~~java
public static String chomp(String str)
~~~


示例

~~~java
StringUtils.chomp("\r") = ""
StringUtils.chomp("\n") = ""
StringUtils.chomp("\r\n") = ""
StringUtils.chomp("abc \r") = "abc "
StringUtils.chomp("abc\n") = "abc"
StringUtils.chomp("abc\r\n") = "abc"
StringUtils.chomp("abc\r\n\r\n") = "abc\r\n"
StringUtils.chomp("abc\n\r") = "abc\n"
StringUtils.chomp("abc\n\rabc") = "abc\n\rabc"
~~~

### 2.3 去除间隔符
去除末尾一个字符，常见使用场景是通过循环处理使用间隔符拼装的字符串，去除间隔符
注意：使用时需确保最后一位一定是间隔符，否则有可能破坏正常数据

~~~java
public static String chop(String str)
~~~


示例：

~~~java
StringUtils.chop("1,2,3,") = "1,2,3"
StringUtils.chop("a") = ""
StringUtils.chop("abc") = "ab"
StringUtils.chop("abc\nabc") = "abc\nab"

//此外，末尾的换行符也视为字符，如果结尾是\r\n，则一块去除，建议使用专用的chomp,以免造成非预期的结果
StringUtils.chop("\r") = ""
StringUtils.chop("\n") = ""
*StringUtils.chop("\r\n") = ""*
~~~

### 2.4 去除非数字
去除所有非数字字符，将剩余的数字字符拼接成字符串

~~~java
public static String getDigits(String str)
~~~


示例：

~~~java
StringUtils.getDigits("abc") = ""
StringUtils.getDigits("1000$") = "1000"
StringUtils.getDigits("1123~45") = "112345"
StringUtils.getDigits("(541) 754-3010") = "5417543010"
~~~

## 3.查找字符串

### 3.1查找字符串 

indexOf与lastIndexOf,可搜索字符、字符串以及指定起始搜索位置，同jdk。

~~~java
public static int indexOf(CharSequence seq,CharSequence searchSeq)

public static int indexOf(CharSequence seq,CharSequence searchSeq,int startPos)
~~~

扩展

~~~java
//增加忽略大小写控制
public static int indexOfIgnoreCase(CharSequence str,CharSequence searchStr)
//返回第n次匹配的所在的索引数。
public static int ordinalIndexOf(CharSequence str,CharSequence searchStr,int ordinal)
//同时查找多个字符
public static int indexOfAny(CharSequence cs,char... searchChars)
//返回不在搜索字符范围内的第一个索引位置
public static int indexOfAnyBut(CharSequence cs,char... searchChars)
~~~

## 4.编辑字符串

字符串的分割、合并、截取、替换

###  4.1 分割字符串 

jdk中的split使用正则表达式匹配，而字符串分割最常用场景是如下这种根据间隔符分割

~~~java
String str="he,ll,o";
String [] reuslt=str.split(",");
~~~


虽然split的方式也能实现效果，但是还有有点别扭，而在StringUtils，就是通过字符串匹配，而不是正则表达式

~~~java
// 不设置间隔符，默认使用空白字符分割
public static String[] split(String str)
// 根据间隔符分割 
public static String[] splitByWholeSeparator(String str,String separator)
// 限定返回，贪婪匹配
public static String[] splitByWholeSeparator(String str,String separator,int max),

// 示例：
StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
// 空白字符作为一个数组元素返回（其他方法默认去除空白字符元素）
public static String[] splitPreserveAllTokens(String str) 
// 示例：
StringUtils.splitPreserveAllTokens("abc def") = ["abc", "def"]
StringUtils.splitPreserveAllTokens("abc def") = ["abc", "", "def"]
StringUtils.splitPreserveAllTokens(" abc ") = ["", "abc", ""]
// 特定场景，根据字符类型分割，同一类划为一个数组元素，驼峰命名情况下，最后一个大写字母归属后面元素而不是前面
public static String[] splitByCharacterTypeCamelCase(String str)
// 示例：
StringUtils.splitByCharacterTypeCamelCase("ab de fg") = ["ab", " ", "de", " ", "fg"]
StringUtils.splitByCharacterTypeCamelCase("ab de fg") = ["ab", " ", "de", " ", "fg"]
StringUtils.splitByCharacterTypeCamelCase("ab:cd:ef") = ["ab", ":", "cd", ":", "ef"]
StringUtils.splitByCharacterTypeCamelCase("number5") = ["number", "5"]
StringUtils.splitByCharacterTypeCamelCase("fooBar") = ["foo", "Bar"]
StringUtils.splitByCharacterTypeCamelCase("foo200Bar") = ["foo", "200", "Bar"]
StringUtils.splitByCharacterTypeCamelCase("ASFRules") = ["ASF", "Rules"]
~~~

### 4.2 合并字符串
jdk使用concat方法，StringUtils使用join，这是一个泛型方法，建议实际使用过程中，还是只对String使用，不要对数值类型进行合并，会导致代码可读性降低

~~~java
//默认合并，注意：自动去除空白字符或null元素
public static <T> String join(T... elements)
// 示例:
StringUtils.join(null) = null
StringUtils.join([]) = ""
StringUtils.join([null]) = ""
StringUtils.join(["a", "b", "c"]) = "abc"
StringUtils.join([null, "", "a"]) = "a"
~~~

~~~java
//使用指定间隔符合并，注意：保留空白字符或null元素
public static String join(Object[] array,char separator)
// 示例：
StringUtils.join(["a", "b", "c"], ';') = "a;b;c"
StringUtils.join([null, "", "a"], ';') = ";;a"
~~~

~~~java
//拼接数值
public static String join(long[] array,char separator)
public static String join(int[] array,char separator)
// 示例：
StringUtils.join([1, 2, 3], ';') = "1;2;3"
StringUtils.join([1, 2, 3], null) = "123"
~~~


（注意：实测与官网文档不符，StringUtils.join(new int[]{1, 2, 3}, null);返回结果是一个很奇怪的字符串[I@77a82f1；使用StringUtils.join(new Object[]{1, 2, 3}, null)），才会返回期望的“123”

相关方法： 

~~~java
//joinWith，基本就是把数组参数和间隔符的位置颠倒了一下，意义不大，建议弃用
StringUtils.joinWith(",", {"a", "b"}) = "a,b"
~~~

此外，还有大量重载函数，进一步指定起始元素和结束元素，实用性较低，不建议使用。

### 4.3 截取字符串

相关方法有多个，substring和truncate基本用法同jdk，内部处理异常

~~~java
public static String substring(String str,int start)
public static String substring(String str,int start,int end)

public static String truncate(String str,int maxWidth)；
public static String truncate(String str,int offset,int maxWidth)
~~~

扩展：

~~~java
//直接实现从左侧、右侧或中间截取指定位数，实用性高
public static String left(String str,int len)
public static String right(String str,int len)
public static String mid(String str,int pos,int len) 

//直接实现特定规则，但总体来说适用场景不多
//截取第一个指定字符前/后的字符串返回
public static String substringBefore(String str,String separator)
public static String substringAfter(String str,String separator)
//截取最后一个指定字符前/后的字符串返回
public static String substringBeforeLast(String str,String separator)
public static String substringAfterLast(String str,String separator)
//截取特定字符串中间部分
public static String substringBetween(String str,String tag)
示例：StringUtils.substringBetween("tagabctag", "tag") = "abc"
//返回起止字符串中间的字符串，且只返回第一次匹配结果
public static String substringBetween(String str,String open,String close)
//返回起止字符串中间的字符串，返回所有匹配结果
public static String[] substringBetween(String str,String open,String close)
~~~

### 4.4 替换字符串
jdk中使用replace，StringUtils使用同样名字，默认替换掉所有匹配项，扩展实现了忽略大小写、只替换一次、指定最大替换次数等

~~~java
public static String replace(String text,String searchString,String replacement)
public static String replaceChars(String str,char searchChar,char replaceChar)
//扩展：
//忽略大小写
public static String replaceIgnoreCase(String text,String searchString,String replacement)
//只替换一次
public static String replaceOnce(String text,String searchString,String replacement)
public static String replaceOnceIgnoreCase(String text,String searchString,String replacement)
//最大替换次数
public static String replace(String text,String searchString,String replacement,int max)
//示例：
StringUtils.replace("abaa", "a", "z", 0) = "abaa"
StringUtils.replace("abaa", "a", "z", 1) = "zbaa"
StringUtils.replace("abaa", "a", "z", 2) = "zbza"
StringUtils.replace("abaa", "a", "z", -1) = "zbzz"
~~~

注意：
max是替换次数，0代表不做替换，-1代表替换所有,从代码可读性考虑，建议按照常规思维模式使用，别使用这些0或者-1比较变态的用法

~~~java
//扩展批量，过于复杂，执行结果难以预期，不建议使用
public static String replaceEach(String text,String[] searchList,String[] replacementList)
public static String replaceEachRepeatedly(String text,String[] searchList,String[] replacementList)
public static String replaceChars(String str,String searchChars,String replaceChars)
~~~

### 4.5.移除字符串
remove，移除字符

~~~java
public static String remove(String str,char remove)
public static String remove(String str,String remove)
//示例：
StringUtils.remove("queued", "ue") = "qd"
//（注意，是第二个参数中所有字符，而不是匹配整个字符串）

//扩展：
//忽略大小写
public static String removeIgnoreCase(String str,String remove)
//移除指定位置
public static String removeStart(String str,String remove)
public static String removeEnd(String str,String remove)
//指定位置且忽略大小写
public static String removeStartIgnoreCase(String str,String remove)
public static String removeEndIgnoreCase(String str,String remove)
~~~

 ### 4.6.覆盖部分字符串

~~~java
public static String overlay(String str,String overlay,int start,int end)
~~~

典型应用场景，隐藏字符串如证件号码、地址或手机号码中部分字符
示例：

~~~java
StringUtils.overlay("13712345678","",3,7)=“1375678”
~~~


注意：实现时做了不少防止异常的处理，比如后面两个参数为止可以调换，会自动判断哪个数字小，哪个就是起始值，然后，如果是负数，则表示添加到开始，如果超出字符串自身长度，添加到末尾，但这些奇特的用法尽量避免使用，否则代码可读性会很差。

### 4.7 生成字符串
根据指定信息产生字符串

~~~java
public static String repeat(String str,int repeat)
//示例：
StringUtils.repeat("a", 3) = "aaa"
StringUtils.repeat("ab", 2) = "abab"
//扩展：
//指定间隔符
public static String repeat(String str,String separator,int repeat)
//示例
StringUtils.repeat("?", ", ", 3) = "?, ?, ?" 
~~~

### 4.8 前缀和后缀

~~~java
//追加前缀，如只有两个参数，则是无条件追加，超过两个参数，是在不匹配prefixes任何情况下才追加
public static String prependIfMissing(String str,CharSequence prefix,CharSequence... prefixes)
public static String prependIfMissingIgnoreCase(String str,CharSequence prefix,CharSequence... prefixes)
//追加后缀，如只有两个参数，则是无条件追加，超过两个参数，是在不匹配suffixes任何情况下才追加
public static String appendIfMissing(String str,CharSequence suffix,CharSequence... suffixes)
public static String appendIfMissingIgnoreCase(String str,CharSequence suffix,CharSequence... suffixes)

//无条件同时增加前缀和后缀
public static String wrap(String str,char wrapWith)
public static String wrap(String str,String wrapWith)
//有条件同时增加前缀和后缀
public static String wrapIfMissing(String str,char wrapWith)
public static String wrapIfMissing(String str,String wrapWith)
//去除前缀和后缀
public static String unwrap(String str,char wrapChar)
public static String unwrap(String str,String wrapToken)
~~~

## 5.字符串转换
字符串内容意义不变，形式变化

### 5.1 大小写转换
转换字符串至大写或小写状态 

~~~java
//转换大写
public static String upperCase(String str)
public static String upperCase(String str,Locale locale)
//转换小写
public static String lowerCase(String str)
public static String lowerCase(String str,Locale locale)
//首字母大写
public static String capitalize(String str)
//首字母小写
public static String uncapitalize(String str)
//大小写交换，即大写变小写，小写变大写
public static String swapCase(String str)
~~~

### 5.2 字符串缩略
将字符串缩减为指定宽度

~~~java
public static String abbreviate(String str,int maxWidth)
~~~

注意，maxWidth必须>=4，否则抛异常
如果字符长度小于maxWidth，直接返回该字符串，否则缩减效果为 substring(str, 0, max-3) + "..."

示例：

~~~java
StringUtils.abbreviate("abcdefg", 4) = "a..."
//扩展：
//可指定缩减字符的省略符号

public static String abbreviate(String str,String abbrevMarker,int maxWidth)
// 示例：
StringUtils.abbreviate("abcdefg", "..", 4) = "ab.."
StringUtils.abbreviate("abcdefg", "..", 3) = "a.." 
~~~


另有其他重载函数，可指定起始位置，实现..ab..效果，同样过于复杂，会导致代码可读性变差，不建议使用

### 5.3 补齐字符串
自动补齐至指定宽度，可指定字符，如不指定，默认补空格，有三个，center、leftPad和rightPad
使用场景：
1)显示时补充数据宽度一致使其对齐，更美观
2)单据流水号，宽度固定，左侧补0

~~~java
public static String center(String str,int size)
public static String center(String str,int size,char padChar)
public static String center(String str,int size,String padStr)

public static String leftPad(String str,int size)
public static String leftPad(String str,int size,char padChar)
public static String leftPad(String str,int size,String padStr)

public static String rightPad(String str,int size)
public static String rightPad(String str,int size,char padChar)
public static String rightPad(String str,int size,String padStr) 
~~~

### 5.4 旋转字符串

~~~java
//shift大于0则右旋，小于0则左旋
public static String rotate(String str,int shift)
//示例：
StringUtils.rotate("abcdefg", 2) = "fgabcde"
StringUtils.rotate("abcdefg", -2) = "cdefgab"
//完全颠倒字符串顺序
public static String reverse(String str)
//颠倒字符串顺序,以间隔符为单位进行，单个元素内部不颠倒位置
public static String reverseDelimited(String str,char separatorChar) 
//示例：
StringUtils.reverseDelimited("a.bc.d",'.')=“d.bc.a”
~~~

### 5.5 编码转换

~~~java
//将字节数组转换为指定编码的字符串
public static String toEncodedString(byte[] bytes,Charset charset)
//应用场景：系统间交互时，字符编码不一致，如对方传递的参数编码为GB2312,我方编码为UTF-8，可通过该方法进行转换

//转换unicode位码
public static int[] toCodePoints(CharSequence str)
~~~

## 6 其他

难以归类的一些功能性方法

### 6.1 取字符串长度

~~~java
public static int length(CharSequence cs)
~~~

### 6.2.计算匹配次数

~~~java
public static int countMatches(CharSequence str,CharSequence sub)
public static int countMatches(CharSequence str,char ch)
~~~

### 6.3 默认值处理

~~~java
//获取默认字符串，null及空格将会返回“”，其他情况返回原始字符串
public static String defaultString(String str)
//获取默认字符串，第一个参数为null及空格将会返回第二个参数指定值，其他情况返回原始字符串
public static String defaultString(String str,String defaultStr)
//其他处理，如果为空白或空，返回指定值
public static <T extends CharSequence> T defaultIfBlank(T str,T defaultStr)
public static <T extends CharSequence> T defaultIfEmpty(T str,T defaultStr)
//其他处理，返回数组中第一个不为空白或不为空的元素
public static <T extends CharSequence> T firstNonBlank(T... values)
public static <T extends CharSequence> T firstNonEmpty(T... values) 
~~~

### 6.4.字符串差异

~~~java
//返回字符串差异部分，实用性差，不建议使用
public static String difference(String str1,String str2)
//返回字符串差异的索引位置
public static int indexOfDifference(CharSequence cs1,CharSequence cs2)
public static int indexOfDifference(CharSequence... css)
~~~

### 6.5 取字符串相同前缀

~~~java
public static String getCommonPrefix(String... strs)
// 示例：
StringUtils.getCommonPrefix(new String[] {"abcde", "abxyz"}) = "ab" 
~~~