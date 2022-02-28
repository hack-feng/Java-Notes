## Aviator简介

> Aviator是一个高性能、轻量级的java语言实现的表达式求值引擎，主要用于各种表达式的动态求值。现在已经有很多开源可用的java表达式求值引擎，为什么还需要Aviator呢？

> Aviator的实现思路与其他轻量级的求值器很不相同，其他求值器一般都是通过解释的方式运行，而Aviator则是直接将表达式编译成Java字节码，交给JVM去执行。简单来说，Aviator的定位是介于Groovy这样的重量级脚本语言和IKExpression这样的轻量级表达式引擎之间。

## Aviator特性

* 支持大部分运算操作符，包括算术操作符、关系运算符、逻辑操作符、位运算符、正则匹配操作符(=~)、三元表达式?: ，并且支持操作符的优先级和括号强制优先级，具体请看后面的操作符列表。
* 支持函数调用和自定义函数
* 支持正则表达式匹配，类似Ruby、Perl的匹配语法，并且支持类Ruby的$digit指向匹配分组。
* 自动类型转换，当执行操作的时候，会自动判断操作数类型并做相应转换，无法转换即抛异常。
* 支持传入变量，支持类似a.b.c的嵌套变量访问。
* 轻量级、高性能

## Maven依赖

~~~
<dependency>
      <groupId>com.googlecode.aviator</groupId>
      <artifactId>aviator</artifactId>
      <version>5.2.3</version>
 </dependency>
~~~

从 3.2.0 版本开始， Aviator 仅支持 JDK 7 及其以上版本。 JDK 6 请使用 3.1.1 这个稳定版本。

## Aviator的使用场景

### 执行表达式

Aviator的使用都是集中通过`com.googlecode.aviator.AviatorEvaluator`这个入口类来处理。

* Aviator支持常见的算术运算符,包括`+ - * / %`五个二元运算符,和一元运算符`-(负)`。
* 其中`- * / %`和一元的`-`仅能作用于Number类型。
* `+`不仅能用于`Number`类型,还可以用于`String`的相加,或者字符串与其他对象的相加。
* Aviator规定,任何类型与`String`相加,结果为`String`。
* Aviator的数值类型仅支持`Long`和`Double`，任何整数都将转换成`Long`，任何浮点数都将转换为`Double`，包括用户传入的变量数值。

~~~java
import com.googlecode.aviator.AviatorEvaluator;

/**
 * @author 笑小枫
 * @date 2022/2/24
 */
public class AviatorEvaluatorUtil {

    public static void main(String[] args) {
        Long result = (Long) AviatorEvaluator.execute("1+2+3");
        System.out.println(result);
    }
}
~~~

运算结果：
~~~
6
~~~

### 编译表达式

上面提到的例子都是直接执行表达式，事实上Aviator背后都帮你做了编译并执行的工作。你可以自己先编译表达式，返回一个编译的结果，然后传入不同的env来复用编译结果，提高性能，这是更推荐的使用方式

~~~java
import com.googlecode.aviator.AviatorEvaluator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 笑小枫
 * @date 2022/2/24
 */
public class AviatorEvaluatorUtil {

    public static void main(String[] args) {
        Map<String, Object> env2 = new HashMap<>(16);
        env2.put("a2", 10.1);
        env2.put("b2", 0.1);
        env2.put("c2", 0.1);
        String expression2 = "a2+b2-c2";
        Double result2 = (Double) AviatorEvaluator.execute(expression2, env2, true);
        System.out.println(result2);

        Map<String, Object> env3 = new HashMap<>(16);
        env3.put("a3", "xiao");
        env3.put("b3", "xiao");
        env3.put("c3", "feng");
        String expression3 = "a3+b3+c3";
        String result3 = AviatorEvaluator.execute(expression3, env3, true).toString();
        System.out.println(result3);
    }
}
~~~

运算结果：
~~~
10.1
xiaoxiaofeng
~~~

### 调用函数

Aviator支持函数调用，函数调用的风格类似lua

~~~java
import com.googlecode.aviator.AviatorEvaluator;

/**
 * @author 笑小枫
 * @date 2022/2/24
 */
public class AviatorEvaluatorUtil {

    public static void main(String[] args) {
        System.out.println(AviatorEvaluator.execute("string.length('xiaoxiaofeng')"));
        System.out.println(AviatorEvaluator.execute("string.substring('xiaoxiaofeng',0,4)"));
        System.out.println(AviatorEvaluator.execute("string.contains(\"xiaoxiaofeng\",\"feng\")"));
        System.out.println(AviatorEvaluator.execute("string.contains(\"xiaoxiaofeng\",string.substring('xiaoxiaofeng',1,3))"));
    }
}
~~~

运算结果：
~~~
12
xiao
true
true
~~~

### 访问数组和集合

可以通过中括号去访问数组和`java.util.List`对象，可以通过`map.key`访问`java.util.Map`中`key`对应的`value`

~~~java
import com.googlecode.aviator.AviatorEvaluator;

import java.util.*;


/**
 * @author 笑小枫
 * @date 2022/2/24
 */
public class AviatorEvaluatorUtil {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        list.add("xiao");
        list.add(" feng");

        int[] array = new int[3];
        array[0] = 1;
        array[1] = 2;
        array[2] = 3;

        final Map<String, Date> map = new HashMap<>();
        map.put("date", new Date());

        Map<String, Object> env = new HashMap<>();
        env.put("list", list);
        env.put("array", array);
        env.put("dateMap", map);

        System.out.println(AviatorEvaluator.execute(
                "list[0]+list[1]+';array[0]+array[1]+array[2]='+(array[0]+array[1]+array[2]) +';today is '+dateMap.date ", env));
    }
}
~~~

运算结果：
~~~
xiao feng;array[0]+array[1]+array[2]=6;today is Fri Feb 25 15:24:10 CST 2022
~~~

### 三元操作符

Aviator 没有提供`if else`语句,但是提供了三元运算符`?:`,形式为`bool ? exp1: exp2`。 其中bool必须为Boolean类型的表达式, 而exp1和exp2可以为任何合法的Aviator表达式,并且不要求exp1和exp2返回的结果类型一致。

~~~java
import com.googlecode.aviator.AviatorEvaluator;

/**
 * @author 笑小枫
 * @date 2022/2/24
 */
public class AviatorEvaluatorUtil {

    public static void main(String[] args) {
        Map<String, Object> env4 = new HashMap<>();
        env4.put("a", 1);
        String result = (String) AviatorEvaluator.execute("a>0? 'yes':'no'", env4);
        System.out.println(result);

    }
}
~~~

运算结果：
~~~
yes
~~~

### 逻辑运算符和关系运算符

Aviator的支持的逻辑运算符包括,一元否定运算符!,以及逻辑与的&&,逻辑或的||。逻辑运算符的操作数只能为Boolean。

Aviator支持的关系运算符包括`<, <=, >, >=, ==, !=`。

关系运算符可以作用于Number之间、String之间、Pattern之间、Boolean之间、变量之间以及其他类型与nil之间的关系比较, 不同类型除了nil之外不能相互比较。

~~~java
import com.googlecode.aviator.AviatorEvaluator;


/**
 * @author 笑小枫
 * @date 2022/2/24
 */
public class AviatorEvaluatorUtil {

    public static void main(String[] args) {
        System.out.println(AviatorEvaluator.execute("(1>0||0<1)&&1!=0"));
    }
}
~~~

运算结果：
~~~
true
~~~

### 位运算符

Aviator 支持所有的 Java 位运算符,包括`&, |, ^, ~, >>, <<, >>>`。

~~~java
import com.googlecode.aviator.AviatorEvaluator;

/**
 * @author 笑小枫
 * @date 2022/2/24
 */
public class AviatorEvaluatorUtil {

    public static void main(String[] args) {
        System.out.println(AviatorEvaluator.execute("10<<2"));
        System.out.println(AviatorEvaluator.execute("100>>>2"));

    }
}
~~~

运算结果：
~~~
40
25
~~~

### 操作符列表

Aviator支持操作符的优先级，并且允许通过括号来强制优先级，下面是完整的操作符列表，按照优先级从高到低的顺序排列

|序号|操作符|结合性|操作数限制|
|---|---|---|---|
|0|() [ ]|从左到右|()用于函数调用，[ ]用于数组和java.util.List的元素访问，要求[indx]中的index必须为整型|
|1|! -|从右到左|! 能用于Boolean,- 仅能用于Number|
|2|* / %|从左到右|Number之间|
|3|+ -|从左到右|+ - 都能用于Number之间, + 还能用于String之间，或者String和其他对象|
|4|< <= > >=|从左到右|Number之间、String之间、Pattern之间、变量之间、其他类型与nil之间|
|5|== != =~|从左到右|==和!=作用于Number之间、String之间、Pattern之间、变量之间、其他类型与nil之间以及String和java.util.Date之间，=~仅能作用于String和Pattern之间|
|6|&&|从左到右|Boolean之间，短路|
|7| &#124;&#124; |从左到右|Boolean之间，短路|
|8|? :|从右到左|第一个操作数的结果必须为Boolean，第二和第三操作数结果无限制|

### 内置函数

|函数名称|说明|
|--------|--- |
|sysdate()|返回当前日期对象 java.util.Date|
|rand()|返回一个介于 0-1 的随机数,double 类型|
|print([out],obj)|打印对象,如果指定 out,向 out 打印, 否则输出到控制台|
|println([out],obj)|与 print 类似,但是在输出后换行|
|now()|返回 System.currentTimeMillis|
|long(v)|将值的类型转为 long|
|double(v)|将值的类型转为 double|
|str(v)|将值的类型转为 string|
|date_to_string(date,format)|将 Date 对象转化化特定格式的字符串,2.1.1 新增|
|string_to_date(source,format)|将特定格式的字符串转化为 Date 对 象,2.1.1 新增|
|string.contains(s1,s2)|判断 s1 是否包含 s2,返回 Boolean|
|string.length(s)|求字符串长度,返回 Long|
|string.startsWith(s1,s2)|s1 是否以 s2 开始,返回 Boolean|
|string.endsWith(s1,s2)|s1 是否以 s2 结尾,返回 Boolean|
|string.substring(s,begin[,end])|截取字符串 s,从 begin 到 end,如果忽略 end 的话,将从 begin 到结尾,与 java.util.String.substring 一样。|
|string.indexOf(s1,s2)|java 中的 s1.indexOf(s2),求 s2 在 s1 中 的起始索引位置,如果不存在为-1|
|string.split(target,regex,[limit])|Java 里的 String.split 方法一致,2.1.1 新增函数|
|string.join(seq,seperator)|将集合 seq 里的元素以 seperator 为间隔 连接起来形成字符串,2.1.1 新增函数|
|string.replace_first(s,regex,replacement)|Java 里的 String.replaceFirst 方法, 2.1.1 新增|
|string.replace_all(s,regex,replacement)|Java 里的 String.replaceAll 方法 , 2.1.1 新增|
|math.abs(d)|求 d 的绝对值|
|math.sqrt(d)|求 d 的平方根|
|math.pow(d1,d2)|求 d1 的 d2 次方|
|math.log(d)|求 d 的自然对数|
|math.log10(d)|求 d 以 10 为底的对数|
|math.sin(d)|正弦函数|
|math.cos(d)|余弦函数|
|math.tan(d)|正切函数|
|map(seq,fun)|将函数 fun 作用到集合 seq 每个元素上, 返回新元素组成的集合|
|filter(seq,predicate)|将谓词 predicate 作用在集合的每个元素 上,返回谓词为 true 的元素组成的集合|
|count(seq)|返回集合大小|
|include(seq,element)|判断 element 是否在集合 seq 中,返回 boolean 值|
|sort(seq)|排序集合,仅对数组和 List 有效,返回排 序后的新集合|
|reduce(seq,fun,init)|fun 接收两个参数,第一个是集合元素, 第二个是累积的函数,本函数用于将 fun 作用在集合每个元素和初始值上面,返回 最终的 init 值|
|seq.eq(value)|返回一个谓词,用来判断传入的参数是否跟 value 相等,用于 filter 函数,如filter(seq,seq.eq(3)) 过滤返回等于3 的元素组成的集合|
|seq.neq(value)|与 seq.eq 类似,返回判断不等于的谓词|
|seq.gt(value)|返回判断大于 value 的谓词|
|seq.ge(value)|返回判断大于等于 value 的谓词|
|seq.lt(value)|返回判断小于 value 的谓词|
|seq.le(value)|返回判断小于等于 value 的谓词|
|seq.nil()|返回判断是否为 nil 的谓词|
|seq.exists()|返回判断不为 nil 的谓词|