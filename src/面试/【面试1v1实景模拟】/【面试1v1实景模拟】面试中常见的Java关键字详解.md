### 老面👴：Java中有哪些关键字

笑小枫🍁：

1. 用于数据类型
用于数据类型的关键字有 boolean、byte、char、 double、 float、int、long、new、short、void、instanceof。

2. 用于语句
用于语句的关键字有break、case、 catch、 continue、 default 、do、 else、 for、 if、return、switch、try、 while、 finally、 throw、this、 super。

3. 用于修饰
用于修饰的关键字有 abstract、final、native、private、 protected、public、static、synchronized、
transient、 volatile。

4. 用于方法、类、接口、包和异常
用于方法、类、接口、包和异常的关键字有 class、 extends、 implements、interface、 package、import、throws。

5. 其他
还有些关键字,如 future、 generic、 operator、 outer、rest、var等都是Java保留的没有意义的关键字。另外，Java还有3个保留字:goto、const、null。它们不是关键字，而是文字。包含Java定义的值。和关键字一样,它们也不可以作为标识符使用。

---

### 老面👴：简单介绍一下 final 关键字

笑小枫🍁：final关键字可以用来修饰引用、方法和类。

1. 修饰引用
* 如果引用为基本数据类型，则该引用为常量，该值无法修改；
* 如果引用为引用数据类型，比如对象、数组，则该对象、数组本身可以修改，但指向该对象或数组的地址的引用不能修改。
* 如果引用时类的成员变量，则必须当场赋值，否则编译会报错。

2. 修饰方法
当使用final修饰方法时，这个方法无法被子类重写。但是，该方法仍然可以被继承。

3. 修饰类
当用final修改类时，该类成为最终类，无法被继承。

---

### 老面👴：简单介绍一下 this、super 关键字

笑小枫🍁：this:代表当前对象的引用，谁来调用我我就代表谁。super:代表当前对象对父类的引用。

1. 调用成员变量不同；
this：成员变量 调用本类的成员变量，也可以调用父类的成员变量。 
super：成员变量 调用父类的成员变量。

2. 调用构造方法不同。
this：调用本类的构造方法。 
super：调用父类的构造方法。

3. 调用成员方法。
this：成员方法 调用本类的成员方法，也可以调用父类的方法。 
super：成员方法 调用父类的成员方法。

---

### 老面👴：简单介绍一下 static 关键字

笑小枫🍁：

1. static是一个修饰符，用于修饰成员。（成员变量，成员函数）static修饰的成员变量 称之为静态变量或类变量。
2. static修饰的成员被所有的对象共享。
3. static优先于对象存在，因为static的成员随着类的加载就已经存在。
4. static修饰的成员多了一种调用方式，可以直接被类名所调用，（类名.静态成员）。
5. static修饰的数据是共享数据，对象中的存储的是特有的数据。

---

### 老面👴：简单介绍一下 volatile 关键字

笑小枫🍁：volatile是java虚拟机提供的轻量级同步机制，用于表示可以被多个线程异步修改的成员变量，其特点有: 1.保证可见性 2.禁止指令重排 3.不保证原子性

**1、volatile可见性实现原理：**

变量被volatile关键字修饰后，底层**汇编指令**中会出现一个**lock前缀指令**。会导致以下两种事情的发生：

1. 修改volatile变量时，会强制将修改后的值刷新到主内存中。
2. 修改volatile变量后，会导致其他线程工作内存中对应的变量值失效。因此，再读取该变量值的时候就需要重新从读取主内存中的值。

**2、volatile有序性实现原理：**

**指令重排序：**编译器在不改变单线程程序语义的前提下，重新安排语句的执行顺序，指令重排序在单线程下不会有问题，但是在多线程下，可能会出现问题。

volatile有序性的保证就是通过**禁止指令重排序**来实现的。指令重排序包括编译器和处理器重排序，JMM会分别限制这两种指令重排序。禁止指令重排序又是通过加**内存屏障**实现的。

```Java  
JMM:JMM(java 内存模型 Java Memory Model 简称JMM) 本身是一个抽象的概念,并不在内存中真实存在的,它描述的是一组规范或者规则,通过这组规范定义了程序中各个变量(实例字段,静态字段和构成数组对象的元素)的访问方式.
JMM的同步规定:
 1.线程解锁之前,必须把共享变量刷新回主存
 2.线程加锁锁之前,必须读取主存的最新值到自己的工作空间
 3.加锁解锁必须是 同一把锁

内存屏障（memory barriers）：一组处理器指令，用于实现对内存操作的顺序限制。
```

![img](https://image.xiaoxiaofeng.site/article/img/2023/03/08/xxf-20230308145053.png)

**3、 volatile为什么不保证原子性？**

java中只有对变量的赋值和读取是原子性的，其他的操作都不是原子性的。所以即使volatile即使能保证被修饰的变量具有可见性，但是不能保证原子性。

---

### 老面👴：简单介绍一下 synchronized 关键字

笑小枫🍁：可用来给对象和方法或者代码块加锁，当它锁定一个方法或者一个代码块的时候，同一时刻最多只有一个线程执行这段代码。

在早期的java版本，synchronized 关键字属于重量级锁，效率低下。jdk1.6之后有了优化。

synchronized在java内存模型中主要有3种作用，分别是：
* 原子性：通过monitorenter和monitorexit指令,保证被synchronized修饰的代码在同一时间只能被一个线程访问，在锁未释放之前，无法被其他线程访问到
* 可见性：保证共享变量的修改能够及时可见,对一个变量的unlock操作之前，必须把此变量同步回主内存中(store和write操作)
* 有序性：一个变量在同一时刻只允许一条线程对其执行lock操作，这条规则决定了持有同一个锁的两个同步块只能串行执行

**synchronized的底层实现原理**

* 原子性实现原理
synchronized实现原子性底层是通过JVM来实现的，同一时间只能有一个线程去执行synchronized中的代码块； 
每一个对象都有一个监视器monitor来关联，监视器被占用时会被锁住，其他线程无法获取该monitor,当JVM执行某个线程的的内部方法的monitorenter,它会尝试去获取该对象的monitor的所有权，过程如下:
1. 若monitor的进入数为0，线程可以进入monitor,并将该monitor的进入数置为1，那么该线程就成为monitor的所有者
2. 若线程已拥有monitor的所有权，允许它重入monitor，则进入monitor的进入数加1（recursions：记录线程拥有锁的次数）
3. 若其他线程已经占有monitor的所有权，那么当前尝试获取monitor的所有权的线程会被阻塞，直到monitor的进入数变为0，才能重新尝试获取monitor的所有权。

  monitorexit指令
  1. 能执行monitorexit指令的线程一定是拥有当前对象的monitor的所有权的线程。
  2. 当执行monitorexit时会将monitor的进入数减1。当monitor的进入数减为0时，当前线程退出monitor，不再拥有monitor的所有权，此时这个monitor阻塞的线程可以尝试去获取这个monitor的所有权。

* 可见性实现原理
synchronized通过内存屏障保证可见性，同样的我们知道volatile是通过内存屏障来保证可见性的，
1. monitorenter指令之后，synchronized内部的共享变量，每次读取数据的时候被强制从主内存读取最新的数据。
2. monitorexit指令也具有Store屏障的作用，也就是让synchronized代码块内的共享变量，如果数据有变更的，强制刷新回主内存。
数据修改之后立即刷新回主内存，其他线程进入synchronized代码块后，使用共享变量的时候强制读取主内存的数据。

* 有序性实现原理
同样的，synchronized也是通过monitorenter、monitorexit指令嵌入上面的内存屏障,既具有加锁、释放锁的功能，同时也具有内存屏障的功能

---

### 老面👴：简单介绍一下 break、continue 关键字

笑小枫🍁：

**break**用于完全结束一个循环，跳出循环体，执行循环后面的语句。

**continue**是跳过当次循环中剩下的语句，执行下一次循环。

---

### 老面👴：简单介绍一下 trabsient 关键字

笑小枫🍁：被transient修饰的变量不参与序列化和反序列化。当一个对象被序列化的时候，transient型变量的值不包括在序列化的表示中，然而非transient型的变量是被包括进去的。

---

### 老面👴：简单介绍一下 instanceof 关键字

笑小枫🍁：在 Java 中可以使用 instanceof 关键字判断一个对象是否为一个类（或接口、抽象类、父类）的实例

---

### 老面👴：简单介绍一下 extends、implements 关键字

笑小枫🍁：

extends 是继承某个类, 继承之后可以使用父类的方法, 也可以重写父类的方法

implements 是实现多个接口, 接口的方法一般为空的, 必须实现才能使用

---

### 老面👴：简单介绍一下 native 关键字

笑小枫🍁：native主要用于方法上，native方法是指该方法的实现由非Java语言实现，比如用C或C++实现。

主要是因为JAVA无法对操作系统底层进行操作，但是可以通过jni(java native interface)调用其他语言来实现底层的访问。

>PS：JNI是Java本机接口（Java Native Interface），是一个本机编程接口，它是Java软件开发工具箱（java Software Development Kit，SDK）的一部分。JNI允许Java代码使用以其他语言编写的代码和代码库。Invocation API（JNI的一部分）可以用来将Java虚拟机（JVM）嵌入到本机应用程序中，从而允许程序员从本机代码内部调用Java代码。