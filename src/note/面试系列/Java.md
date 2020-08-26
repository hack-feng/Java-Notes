### 面向对象的特征有哪几方面
* 抽象：抽象是将一类对象的共同特征总结出来构造类的过程，包括数据抽象和行为抽象两方面。抽象只关注对象有哪些属性和行为，并不关系这些行为的细节是什么。
* 继承：继承是从已有的类得到继承信息创建类的过程。提供继承信息的类被称为父类；得到继承信息的类为子类。
* 封装：封装就是把数据和操作数据的方法绑定起来，对数据的访问只能通过已定义的接口。
* 多态：多态性指允许不同子类型的对象对同一消息做出不同的响应。简单说就是用同样的对象引用调用同样的方法，但是做了不同的事。

### 访问修饰符public、private、protected、default（不写时默认）的区别
|  修饰符 |当前类|同 包|子 类|其他包|
|:-------:|:---:|:---:|:---:|:---:|
| public  |   √ |  √  |  √  |  √  |
|protected|   √ |  √  |  √  |  X  |
| default |   √ |  √  |  X  |  X  |
| private |   √ |  X  |  X  |  X  |

### String是基本数据类型吗？
不是。java中的基本数据类型有8个：byte、short、int、long、float、double、char、boolean。除了基本类型，剩下的都是引用类型。

### 内存中的栈（stack）、堆（heap）、方法区（method area）的用法
通常我们定义一个基本数据类型的变量，一个对象的引用，还有就是函数调用的现场保存都是在使用的VM的栈空间；

通过new关键字和构造器创建的对象则放在堆空间，堆时垃圾收集器管理的主要区域，由于现在垃圾收集器采用分代收集算法，所有堆空间还可以细分为新生代和老年代，再具体一点可以细分为Eden、2个Survivor、Tenured；

方法区和堆都是再各个线程共享的内存区域，用于存储已经被JVM加载的类信息、常量、静态变量、JIT编译器编译后的代码等数据；

~~~
String str = new String("hello");
~~~
上面的语句中变量str存放在栈上，new出来的字符串对象放在堆上，而"hello这个字面量是放在方法区。

### switch是否可以作用再byte、long、String上？

* Java5 之前，switch(expr)中，expr只能是byte、short、char、int；
* Java5，引入了枚举类型；
* Java7，expr可以是String类型；
* 但是长整型long在目前的版本中是不可以的。

### 数组有没有length方法，String有没有length方法
~~~java
class Maple{
    public static void main(String[] args){
        int [] array = {1, 2, 3};
        System.out.println("数组的长度：" + array.length);
        
        String str = "abc";
        System.out.println("字符串的长度：" + str.length());
    
        Map map = new HashMap();
        map.put("a", 1);
        System.out.println("Map的长度：" + map.size());
        
        List list = new ArrayList();
        list.add("a");
        System.out.println("List的长度：" + list.length());
    
        Set set = new HashSet();
        set.put("a");
        System.out.println("Set的长度：" + set.size());
    }
}
~~~

### String、StringBuilder、StringBuffer的区别？
* String:

* StringBuilder

* StringBuffer
