![06 Java 面向对象](https://image.xiaoxiaofeng.site/blog/2023/08/07/xxf-20230807104020.png?xxfjava)

## 1. 面向对象思想

面向对象是一种符合人类思维习惯的编程思想。现实生活中存在各种形态不同的事物，这些事物之间存在着各种各样的联系。在程序中使用对象来映射现实中的事物，使用对象的关系来描述事物之间的联系，这种思想就是面向对象。

提到面向对象，自然会想到面向过程，面向过程就是分析出解决问题所需要的步骤，然后用函数把这些步骤一一实现，使用的时候依次调用就可以了。面向对象则是把构成问题的事务按照一定规则划分为多个独立的对象，然后通过调用对象的方法来解决问题。当然，一个应用程序会包含多个对象，通过多个对象的相互配合来实现应用程序的功能，这样当应用程序功能发生变动时，只需要修改个别的对象就可以了，从而使代码更容易得到维护。面向对象的特点主要可以概括为封装性、继承性和多态性。

**1、封装性**

封装是面向对象的核心思想，将对象的属性和行为封装起来，不需要让外界知道具体实现细节，这就是封装思想。例如，用户使用电脑，只需要使用手指敲键盘就可以了，无需知道电脑内部是如何工作的，即使用户可能碰巧知道电脑的工作原理，但在使用时，并不完全依赖电脑工作原理这些细节。

**2、继承性**

继承性主要描述的是类与类之间的关系，通过继承，可以在无需重新编写原有类的情况下，对原有类的功能进行扩展。例如，有一个汽车的类，该类中描述了汽车的普通特性和功能，而轿车的类中不仅应该包含汽车的特性和功能，还应该增加轿车特有的功能，这时，可以让轿车类继承汽车类，在轿车类中单独添加轿车特性的方法就可以了。继承不仅增强了代码的复用性、提高开发效率，还为程序的维护补充提供了便利。 

**3、多态性**

多态性指的是在程序中允许出现重名现象，它指在一个类中定义的属性和方法被其它类继承后，它们可以具有不同的数据类型或表现出不同的行为，这使得同一个属性和方法在不同的类中具有不同的语义。例如，当听到“Cut” 这个单词时，理发师的行为是剪发，演员的行为表现是停止表演，不同的对象，所表现的行为是不一样的。

### 1.1 面向对象思想引入

面向对象的编程思想，力图让程序中对事物的描述与该事物在现实中的形态保持一致。为了做到这一点，面向对象的思想中提出了两个概念，即类和对象。其中，类是对某一类事物的抽象描述，而对象用于表示现实中该类事物的个体。

前面我们讲过数组，当有多个数组都需要遍历时，我们可以将遍历的代码封装到方法中，需要遍历时，就调用相应的方法即可，提高代码的复用性。在对数组遍历的基础上继续增加需求，比如获取最值，数值逆序等，同样需要将这些功能封装到相应的方法中。这样继续封装会发现方法越来越多，于是就想能不能将这些方法继续进行封装呢？通过前面的讲解我们知道类是可以存放方法的，所以，我们就考虑使用类封装来这多个方法，将来再做数组的操作时，不用去找具体的方法，先找到这个类，然后使用这个类中的方法。这就是面向对象思想的编程方式。
### 1.2 面向过程思想概述

我们来回想一下，这几天我们完成一个需求的步骤：首先是搞清楚我们要做什么，然后在分析怎么做，最后我们再代码体现。一步一步去实现，而具体的每一步都需要我们去实现和操作。这些步骤相互调用和协作，完成我们的需求。

在上面的每一个具体步骤中我们都是参与者，并且需要面对具体的每一个步骤和过程，这就是面向过程最直接的体现。

那么什么是面向过程开发呢? 面向过程开发，其实就是面向着具体的每一个步骤和过程，把每一个步骤和过程完成，然后由这些功能方法相互调用，完成需求。

面向过程的代表语言：C语言，强调的是每一个功能的步骤

### 1.3 面向对象思想概述

当需求单一，或者简单时，我们一步一步去操作没问题，并且效率也挺高。可随着需求的更改，功能的增多，发现需要面对每一个步骤很麻烦了，这时就开始思索，能不能把这些步骤和功能在进行封装，封装时根据不同的功能，进行不同的封装，功能类似的封装在一起。这样结构就清晰了很多。用的时候，找到对应的类就可以了。这就是面向对象的思想。接下来我们看看面向对象到底是什么?

面向对象思想概述：面向对象是基于面向过程的编程思想，强调的是对象，然后由对象去调用功能。

面向对象思想特点

- 是一种更符合我们思想习惯的思想
- 可以将复杂的事情简单化
- 将我们从执行者变成了指挥者，角色发生了转换

**举例：**

**(1) 买电脑：**​

面向过程：我的了解电脑--了解我自己的需求--找对应的参数信息--去中关村买电脑--讨价还价--买回电脑

面向对象：我知道我要买电脑 -- 班长去给我买 -- 班长就买回来了

**(2)  洗衣服：**

面向过程：把衣服脱下--找一个盆--放点洗衣粉--加点水--把衣服扔进去--搓一搓--清洗衣服--拧干--晾起来

面向对象：把衣服脱下--打开全自动洗衣机--扔进去--一键即可--晾起来

**(3) 吃饭：**

面向过程：去超市买菜--摘菜--洗菜--切菜--炒菜--盛起来--吃

面向对象：上饭店吃饭，你--服务员(点菜)--厨师(做菜)--服务员(端菜)--吃

### 1.4 面向对象开发、设计、特征

面向对象开发：就是不断的创建对象，使用对象，指挥对象做事情。
面向对象设计：其实就是在管理和维护对象之间的关系。

面向对象特征

- 封装(encapsulation)
- 继承(inheritance)
- 多态(polymorphism)

## 2. 类与对象及其使用

### 2.1 类与对象的关系

我们学习编程语言，就是为了模拟现实世界的事物，实现信息化。比如：去超市买东西的计费系统，去银行办业务的系统。

我们如何表示一个现实世界事物呢：

- 属性 就是该事物的描述信息
- 行为 就是该事物能够做什么
- 举例：学生事物

我们学习的Java语言最基本单位是类，所以，我们就应该把事物用一个类来体现。

类：是一组相关的属性和行为的集合
对象：是该类事物的具体体现
举例：类 学生，对象班长就是一个对象

**PS**
类：可以理解为构造对象的一个蓝图或者模版，是抽象的概念
对象：是以类为模型创建的具体实例，是对类的一种具体化。

### 2.2 类的定义

现实世界的事物有属性（人的身高，体重等）和行为（人可以学习，吃饭等）

Java中用class描述事物也是如此，成员变量 就是事物的属性，成员方法 就是事物的行为

定义类其实就是定义类的成员(成员变量和成员方法)

```java
/**
	事物：
		属性	事物的信息描述
		行为	事物的功能

	类：
		成员变量	事物的属性
		成员方法	事物的行为

	定义一个类，其实就是定义该类的成员变量和成员方法。

	案例：我们来完成一个学生类的定义。

	学生事物：
		属性：姓名，年龄，地址...
		行为：学习，吃饭，睡觉...

	把事物要转换为对应的类：

	学生类：
		成员变量：姓名，年龄，地址...
		成员方法：学习，吃饭，睡觉...

	成员变量：和以前变量的定义是一样的格式，但是位置不同，在类中方法外。
	成员方法：和以前的方法定义是一样的格式，但是今天把static先去掉。

	首先我们应该定义一个类，然后完成类的成员。
*/
//这是我的学生类
class Student {
	//定义变量
	String name;//姓名
	int age;//年龄
	String address;//地址

	//定义方法
	//学习的方法
	public void study() {
		System.out.println("学生爱学习");
	}

	//吃饭的方法
	public void eat() {
		System.out.println("学习饿了,要吃饭");
	}

	//睡觉的方法
	public void sleep() {
		System.out.println("学习累了,要睡觉");
	}
}
```

### 2.3 对象内存图

只要是用new操作符定义的实体就会在堆内存中开辟一个新的空间，并且每一个对象中都有一份属于自己的属性。

通过对象.对象成员的方式操作对象中的成员，对其中一个对象的成员进行了修改，和另一个对象没有任何关系。

1个对象的内存图：一个对象的基本初始化过程

![java对象内存](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140621.png?xxfjava)

2个对象的内存图：方法的共用

![java对象内存](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140624.png?xxfjava)

3个对象的内存图：其中有两个引用指向同一个对象

![java对象内存](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140627.png?xxfjava)

**垃圾回收**

在Java中，当一个对象成为垃圾后仍会占用内存空间，时间一长，就会导致内存空间的不足。针对这种情况，Java中引入了垃圾回收机制。有了这种机制，程序员不需要过多关心垃圾对象回收的问题，Java虚拟机会自动回收垃圾对象所占用的内存空间。

一个对象在成为垃圾后会暂时地保留在内存中，当这样的垃圾堆积到一定程度时，Java虚拟机就会启动垃圾回收器将这些垃圾对象从内存中释放，从而使程序获得更多可用的内存空间。除了等待Java虚拟机进行自动垃圾回收外，还可以通过调用System.gc()方法来通知Java虚拟机立即进行垃圾回收。当一个对象在内存中被释放时，它的finalize()方法会被自动调用，因此可以在类中通过定义finalize()方法来观察对象何时被释放。

### 2.4 成员变量和局部变量的区别

![成员变量和局部变量的区别](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140630.png?xxfjava)

注意事项：局部变量名称可以和成员变量名称一样，在方法中使用的时候，采用的是就近原则。

```java
class Varialbe {
	//成员变量
	//int num = 10;
	int num; //0

	public void show() {
		//int num2 = 20; //局部变量
		//可能尚未初始化变量num2
		//int num2; //没有默认值
		int num2 = 20;
		System.out.println(num2);

		//int num = 100;
		System.out.println(num);
	}
}

class VariableDemo {
	public static void main(String[] args) {
		Varialbe v = new Varialbe();

		System.out.println(v.num); //访问成员变量

		v.show();

	}
}
```

### 2.5 形式参数问题

基本类型作为形式参数：形式参数的改变不影响实际参数
引用类型作为形式参数：形式参数的改变直接影响实际参数

```java
/**
	形式参数的问题：
		基本类型：形式参数的改变不影响实际参数
		引用类型：形式参数的改变直接影响实际参数
*/
//形式参数是基本类型
class Demo {
	public int sum(int a,int b) {
		return a + b;
	}
}

//形式参数是引用类型
class Student {
	public void show() {
		System.out.println("我爱学习");
	}
}

class StudentDemo {
	//如果你看到了一个方法的形式参数是一个类类型(引用类型)，这里其实需要的是该类的对象。
	public void method(Student s) { //调用的时候，把main方法中的s的地址传递到了这里 Student s = new Student();
		s.show();
	}
}

class ArgsTest {
	public static void main(String[] args) {
		//形式参数是基本类型的调用
		Demo d = new Demo();
		int result = d.sum(10,20);
		System.out.println("result:"+result);
		System.out.println("--------------");

		//形式参数是引用类型的调用
		//需求：我要调用StudentDemo类中的method()方法
		StudentDemo sd = new StudentDemo();
		//创建学生对象
		Student s = new Student();
		sd.method(s); //把s的地址给到了这里
	}
}
```
运行结果：

![形参](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140633.png?xxfjava)

在java中，方法参数的传递永远都是传值，而这个值，对于基本数据类型，值就是你赋给变量的那个值。而对于引用数据类型，这个值是对象的引用，而不是这个对象本身。

### 2.6 匿名对象

匿名对象：就是没有名字的对象。是对象的一种简化表示形式
匿名对象的两种使用情况：对象调用方法仅仅一次的时候；作为实际参数传递。

```java
/**
	匿名对象：就是没有名字的对象。

	匿名对象的应用场景：
		A:调用方法，仅仅只调用一次的时候。
			注意：调用多次的时候，不适合。
			那么，这种匿名调用有什么好处吗?
				有，匿名对象调用完毕就是垃圾。可以被垃圾回收器回收。
		B:匿名对象可以作为实际参数传递
*/
class Student {
	public void show() {
		System.out.println("我爱学习");
	}
}

class StudentDemo {
	public void method(Student s) {
		s.show();
	}
}

class NoNameDemo {
	public static void main(String[] args) {
		//带名字的调用
		Student s = new Student();
		s.show();
		s.show();
		System.out.println("--------------");

		//匿名对象
		//new Student();
		//匿名对象调用方法
		new Student().show();
		new Student().show(); //这里其实是重新创建了一个新的对象
		System.out.println("--------------");


		//匿名对象作为实际参数传递
		StudentDemo sd = new StudentDemo();
		//Student ss = new Student();
		//sd.method(ss); //这里的s是一个实际参数
		//匿名对象
		sd.method(new Student());

		//在来一个
		new StudentDemo().method(new Student());
 	}
}
```
运行结果：

![匿名对象](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140636.png?xxfjava)

### 2.7 this关键字

this代表其所在函数所属对象的引用。换言之，this代本类对象的引用。

当成员变量和局部变量重名，可以用关键字this来区分，this就是所在函数所属对象的引用。

简单说，哪个对象调用了this所在的函数，this就代表哪个对象。一般方法调用默认加this。（方法被哪个对象调用，this就代表那个对象）

什么时候使用this呢?

- 局部变量隐藏成员变量
- 其他用法后面和super一起讲解

```java
/**
	我们曾经曰：起名字要做到见名知意。

	this:是当前类的对象引用。简单的记，它就代表当前类的一个对象。

		注意：谁调用这个方法，在该方法内部的this就代表谁。

	this的场景：
		解决局部变量隐藏成员变量

	this:哪个对象调用那个方法，this就代表那个对象
*/
class Student {
	private String name;
	private int age;

	public String getName() {
		return name; //这里其实是隐含了this
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}

class StudentTest2 {
	public static void main(String[] args) {
		//创建一个对象
		Student s1 = new Student();
		s1.setName("林青霞");
		s1.setAge(27);
		System.out.println(s1.getName()+"---"+s1.getAge());

		//创建第二个对象
		Student s2 = new Student();
		s2.setName("刘意");
		s2.setAge(30);
		System.out.println(s2.getName()+"---"+s2.getAge());
	}
}
```

运行结果：

![this关键字](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140642.png?xxfjava)

this关键字的内存图解：

![this关键字](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140644.png?xxfjava)

### 2.8 构造方法

构造方法作用概述：给对象的数据进行初始化

**构造方法格式**

- 方法名与类名相同
- 没有返回值类型，连void都没有
- 没有具体的返回值

**构造方法注意事项**

- 如果你不提供构造方法，系统会给出默认构造方法
- 如果你提供了构造方法，系统将不再提供
- 构造方法也是可以重载的

**给成员变量赋值有两种方式**
- setXxx()
- 构造方法

**PS：**

1. 一般函数和构造函数什么区别呢？

构造函数：对象创建时，就会调用与之对应的构造函数，对对象进行初始化。
一般函数：对象创建后，需要函数功能时才调用。

构造函数：对象创建时，会调用并且只调用一次。
一般函数：对象创建后，可以被调用多次。

2. 创建对象都必须要通过构造函数初始化。

一个类中如果没有定义过构造函数，那么该类中会有一个默认的空参数构造函数。

如果在类中定义了指定的构造函数，那么类中的默认构造函数就没有了。

3. 多个构造函数是以重载的形式存在的。

```java
/**
	我们一直在使用构造方法，但是，我们确没有定义构造方法，用的是哪里来的呢?

	构造方法的注意事项:
		A:如果我们没有给出构造方法，系统将自动提供一个无参构造方法。
		B:如果我们给出了构造方法，系统将不再提供默认的无参构造方法。
			注意：这个时候，如果我们还想使用无参构造方法，就必须自己给出。建议永远自己给出无参构造方法

	给成员变量赋值有两种方式：
		A:setXxx()
		B:构造方法
*/

class Student {
	private String name;
	private int age;

	public Student() {
		//System.out.println("我给了，你还给不");
		System.out.println("这是无参构造方法");
	}

	//构造方法的重载格式
	public Student(String name) {
		System.out.println("这是带一个String类型的构造方法");
		this.name = name;
	}

	public Student(int age) {
		System.out.println("这是带一个int类型的构造方法");
		this.age = age;
	}

	public Student(String name,int age) {
		System.out.println("这是一个带多个参数的构造方法");
		this.name = name;
		this.age = age;
	}

	public void show() {
		System.out.println(name+"---"+age);
	}
}

class ConstructDemo2 {
	public static void main(String[] args) {
		//创建对象
		Student s = new Student();
		s.show();
		System.out.println("-------------");

		//创建对象2
		Student s2 = new Student("林青霞");
		s2.show();
		System.out.println("-------------");

		//创建对象3
		Student s3 = new Student(27);
		s3.show();
		System.out.println("-------------");

		//创建对象4
		Student s4 = new Student("林青霞",27);
		s4.show();
	}
}
```

#### 构造方法和成员变量初始化顺序

执行父类静态代码 执行子类静态代码
初始化父类成员变量（我们常说的赋值语句）
初始化父类构造函数
初始化子类成员变量
初始化子类构造函数

### 2.9 类的成员方法

成员方法其实就是我们前面讲过的方法

方法具体划分：
- 根据返回值：有明确返回值方法；返回void类型的方法
- 根据形式参数：无参方法；带参方法

一个基本类的标准代码写法

```java
/**
	一个标准代码的最终版。

	学生类：
		成员变量：
			name，age
		构造方法：
			无参，带两个参
		成员方法：
			getXxx()/setXxx()
			show()：输出该类的所有成员变量值

	给成员变量赋值：
		A:setXxx()方法
		B:构造方法

	输出成员变量值的方式：
		A:通过getXxx()分别获取然后拼接
		B:通过调用show()方法搞定
*/
class Student {
	//姓名
	private String name;
	//年龄
	private int age;

	//构造方法
	public Student() {
	}

	public Student(String name,int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	//输出所有的成员变量值
	public void show() {
		System.out.println(name+"---"+age);
	}
}

//测试类
class StudentTest {
	public static void main(String[] args) {
		//方式1给成员变量赋值
		//无参构造+setXxx()
		Student s1 = new Student();
		s1.setName("林青霞");
		s1.setAge(27);
		//输出值
		System.out.println(s1.getName()+"---"+s1.getAge());
		s1.show();
		System.out.println("----------------------------");

		//方式2给成员变量赋值
		Student s2 = new Student("刘意",30);
		System.out.println(s2.getName()+"---"+s2.getAge());
		s2.show();
	}
}
```

运行结果：

![类的成员方法](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140651.png?xxfjava)

### 2.10 类的初始化过程

Student s = new Student();在内存中做了哪些事情?

- 加载Student.class文件进内存
- 在栈内存为s开辟空间
- 在堆内存为学生对象开辟空间
- 对学生对象的成员变量进行默认初始化
- 对学生对象的成员变量进行显示初始化
- 通过构造方法对学生对象的成员变量赋值
- 学生对象初始化完毕，把对象地址赋值给s变量

![类的初始化过程](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140654.png?xxfjava)

### 2.11 static关键字

static关键字可以修饰成员变量和成员方法

#### static关键字特点

- 随着类的加载而加载
- 优先于对象存在
- 被类的所有对象共享
- 这也是我们判断是否使用静态关键字的条件
- 可以通过类名调用

#### static关键字注意事项

- 在静态方法中是没有this关键字的
- 静态方法只能访问静态的成员变量和静态的成员方法

静态的内存图

![静态的内存图](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140656.png?xxfjava)

静态变量和成员变量的区别

![静态变量和成员变量的区别](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140658.png?xxfjava)

#### main方法是静态的

public static void main(String[] args) {}

- public 被jvm调用，访问权限足够大。
- static 被jvm调用，不用创建对象，直接类名访问
- void被jvm调用，不需要给jvm返回值
- main 一个通用的名称，虽然不是关键字，但是被jvm识别
- String[] args 以前用于接收键盘录入的
- 静态什么时候用？

#### 静态变量

当分析对象中所具备的成员变量的值都是相同时，这时这个成员就可以被静态修饰。

只要数据在对象中都是不同的，就是对象的特有数据，必须存储在对象中，是非静态的。

如果是相同的数据，对象不需要做修改，只需要使用即可，不需要存储在对象中，定义成静态的。

#### 静态函数

函数是否用静态修饰，就参考一点，就是该函数功能是否需要访问到对象中的特有数据。

简单点说，从源代码看，该功能是否需要访问非静态的成员变量，如果需要，该功能就是非静态的。

如果不需要，就可以将该功能定义成静态的。当然，也可以定义成非静态，但是非静态需要被对象调用。

如果没有访问特有数据的方法，该对象的创建是没有意义。

```java
/*
	main方法的格式讲解：
		public static void main(String[] args) {...}

		public:公共的，访问权限是最大的。由于main方法是被jvm调用，所以权限要够大。
		static:静态的，不需要创建对象，通过类名就可以。方便jvm的调用。
		void:因为我们曾经说过，方法的返回值是返回给调用者，而main方法是被jvm调用。你返回内容给jvm没有意义。
		main:是一个常见的方法入口。我见过的语言都是以main作为入口。
		String[] args:这是一个字符串数组。值去哪里了?
			这个东西到底有什么用啊?怎么给值啊?
				这个东西早期是为了接收键盘录入的数据的。
				格式是：
					java MainDemo hello world java
*/
class MainDemo {
	public static void main(String[] args) {
		//System.out.println(args); //[Ljava.lang.String;@175078b
		//System.out.println(args.length); //0
		//System.out.println(args[0]); //ArrayIndexOutOfBoundsException

		//接收数据后
		System.out.println(args);
		System.out.println(args.length);
		//System.out.println(args[0]);
		for(int x=0; x<args.length; x++) {
			System.out.println(args[x]);
		}
	}
}
```

运行结果：

![静态函数](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140702.png?xxfjava)

## 3. 封装

封装概述：是指隐藏对象的属性和实现细节，仅对外提供公共访问方式。

### 3.1 封装的好处

- 隐藏实现细节，提供公共的访问方式
- 提高了代码的复用性
- 提高安全性。

### 3.2 封装原则

- 将不需要对外提供的内容都隐藏起来。
- 把属性隐藏，提供公共方法对其访问。

### 3.3 private关键字

- 是一个权限修饰符。
- 可以修饰成员(成员变量和成员方法)
- 被private修饰的成员只在本类中才能访问。

### 3.4 private最常见的应用

- 把成员变量用private修饰
- 提供对应的getXxx()和setXxx()方法

一个标准的案例的使用

```java
    /**
     人：
     属性：年龄
     行为：说话
     */
    class Person{
        //private：私有，是一个权限修饰符，用于修饰
        //不希望别人直接访问赋值，需要通过私有化把属性进行隐藏
        private int age ;

        //通过提供set、get公共方法对其访问
        public void setAge( int a){
            //在set方法内可以对属性的赋值进行限制
            if (a > 0 && a < 130){
                age = a;
            } else
                System.out .println("错误的数据" );
        }

        public int getAge(){
            return age ;
        }

        void speak(){
            System.out .println("age = " + age);
        }
    }

    class PersonDemo{
        public static void main(String[] args){
            Person p = new Person();
            //通过其他方式访问
            p.setAge(20);
            p.speak();
            //赋值不合法，set方法就不允许成功赋值
            p.setAge(-20);
        }
    }
```

运行结果：

![1491310549430](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140757.png?xxfjava)

注意事项：

- 私有仅仅是封装的一种体现而已。
- private关键字：是一个权限修饰符，用于修饰成员(成员变量和成员函数)，被私有化的成员只在本类中有效。
- 常用场景之一：将成员变量私有化，对外提供对应的set、get方法对其进行访问，提高对数据访问的安全性。

## 4. 继承


### 4.1 继承的描述

在现实生活中，继承一般指的是子女继承父辈的财产。在程序中，继承描述的是事物之间的所属关系，通过继承可以使多种事物之间形成一种关系体系。例如猫和狗都属于动物，程序中便可以描述为猫和狗继承自动物，同理，波斯猫和巴厘猫继承自猫，而沙皮狗和斑点狗继承自狗。这些动物之间会形成一个继承体系，具体如下图所示。

![1500704359335](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140811.png?xxfjava)

在Java中，类的继承是指在一个现有类的基础上去构建一个新的类，构建出来的新类被称作子类，现有类被称作父类，子类会自动拥有父类所有可继承的属性和方法。在程序中，如果想声明一个类继承另一个类，需要使用extends关键字。

通过 extends 关键字让类与类之间产生继承关系。

多个类中存在相同属性和行为时，将这些内容抽取到单独一个类中，那么多个类无需再定义这些属性和行为，只要继承那个类即可。多个类可以称为子类，单独这个类称为父类或者超类。

注意事项：

- 子类可以直接访问父类中的非私有的属性和行为。
- 子类无法继承父类中私有的内容。
- 父类怎么来的？共性不断向上抽取而来的。

示例：

```java
class Person{
    String name;
    int age ;
}
class Student extends Person{
    void study(){
        System.out.println("student study..." + age);
    }
}
class Worker extends Person{
    void work(){
        System.out.println("worker work..." + age);
    }
}
class ExtendDemo{
    public static void main(String[] args){
        Student s = new Student();
        s. name = "zhangsan" ;
        s. age = 20;
        s.study();
        Worker w = new Worker();
        w. name = "lisi" ;
        w. age = 30;
        w.work();
    }
}
```
运行结果：

![1491308047866](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140814.png?xxfjava)

好处：

- 继承的出现提高了代码的复用性。
- 继承的出现让类与类之间产生了关系，提供了多态的前提。

### 4.2 继承的特点

在类的继承中，需要注意一些问题，具体如下：

1．在Java中，类只支持单继承，不允许多重继承，也就是说一个类只能有一个直接父类，例如下面这种情况是不合法的。

![1500704447701](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140822.png?xxfjava)

2．多个类可以继承一个父类，例如下面这种情况是允许的。

![1500704467099](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140824.png?xxfjava)

3．在Java中，多层继承是可以的，即一个类的父类可以再去继承另外的父类，例如C类继承自B类，而B类又可以去继承A类，这时，C类也可称作A类的子类。例如下面这种情况是允许的。

![1500704487847](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140827.png?xxfjava)

4．在Java中，子类和父类是一种相对概念，也就是说一个类是某个类父类的同时，也可以是另一个类的子类。例如上面的示例中，B类是A类的子类，同时又是C类的父类。

Java只支持单继承，不支持多继承。一个类只能有一个父类，不可以有多个父类。
原因：因为多继承容易出现问题。两个父类中有相同的方法，子类到底要执行哪一个是不确定的。

示例：

```java
class A{
    void show(){
        System.out.println("a" );
    }
}
class B{
    void show(){
        System.out.println("b" );
    }
}
class C extends B,A{
}
```

那么创建类C的对象，调用show方法就不知道调用类A中的show方法还是类B中的show方法。所以java不支持多继承，但将这种机制换了另一个安全的方式来体现，也就是多实现（后面会详细说明）。

Java支持多层继承(继承体系)：

C继承B，B继承A，就会出现继承体系。多层继承出现的继承体系中，通常看父类中的功能，了解该体系的基本功能，建立子类对象，即可使用该体系功能。

定义继承需要注意：不要仅为了获取其他类中某个功能而去继承，类与类之间要有所属( "is a")关系。

### 4.3 super关键字&函数覆盖

在继承关系中，子类会自动继承父类中定义的方法，但有时在子类中需要对继承的方法进行一些修改，即对父类的方法进行重写。需要注意的是，在子类中重写的方法需要和父类被重写的方法具有相同的方法名、参数列表以及返回值类型。

当子类重写父类的方法后，子类对象将无法访问父类被重写的方法，为了解决这个问题，在Java中专门提供了一个super关键字用于访问父类的成员。例如访问父类的成员变量、成员方法和构造方法。

在子父类中，成员的特点体现：

成员变量

- this和super的用法很相似
- this代表本类对象的引用
- super代表父类的内存空间的标识
- 当本类的成员和局部变量同名用this区分
- 当子父类中的成员变量同名用super区分父类

示例：

```java
class Fu{
   private int num = 4;

   public int getNum(){
       return num ;
   }
}

class Zi extends Fu{
   private int num = 5;

   void show(){
       System.out.println(this.num + "..." + super.getNum());
   }
}

class ExtendDemo{
   public static void main(String[] args){
       Zi z = new Zi();
       z.show();
   }
}
```
运行结果

![1491308096802](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140831.png?xxfjava)

成员函数

当子父类中出现成员函数一模一样的情况，会运行子类的函数。这种现象，称为覆盖操作，这是函数在子父类中的特性。

在子类覆盖方法中，继续使用被覆盖的方法可以通过super.函数名获取。

函数两个特性：

1. 重载，同一个类中。

2. 覆盖，子类中，覆盖也称为重写，覆写，override。

示例：

```java
class Fu{
   public void show(){
       System.out.println("fu show run" );
   }
}

class Zi extends Fu{
   public void show(){
       System.out.println("zi show run" );
   }
}

class ExtendDemo{
   public static void main(String[] args){
       Zi z = new Zi();
       z.show();
   }
}
```
运行结果：

![1491308113418](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140834.png?xxfjava)

**什么时候使用覆盖操作？**

当子类需要父类的功能，而功能主体子类有自己特有内容时，可以复写父类中的方法，这样，即沿袭了父类的功能，又定义了子类特有的内容。

示例：

```java
class Phone{
   void call(){}
   void show(){
       System.out.println("number" );
   }
}

class NewPhone extends Phone{
   void show(){
       System.out.println("name" );
       System.out.println("pic" );
       super.show();
   }
}

class ExtendDemo{
   public static void main(String[] args){
       NewPhone p = new NewPhone();
       p.show();
   }
}
```
运行结果：

![1491308129771](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140836.png?xxfjava)

注意事项：

- 父类中的私有方法不可以被覆盖
- 父类为static的方法无法覆盖
- 覆盖时，子类方法权限一定要大于等于父类方法权限

示例：

```java
class Fu{
    public void show(){
        System.out.println("fu show run" );
    }
}

class Zi extends Fu{
    private void show(){
        System.out.println("zi show run" );
    }
}

class ExtendDemo{
    public static void main(String[] args){
        Zi z = new Zi();
        z.show();
    }
}
```
运行结果：

![1491308142717](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140839.png?xxfjava)

### 4.4 构造函数

子父类中构造函数的特点：在子类构造函数执行时，发现父类构造函数也运行了。
原因：在子类的构造函数中，第一行有一个默认的隐式语句：super();。
注意：如果使用super(4);语句调用父类的其他构造函数，那么默认的父类构造函数将不会再被调用。

示例：

```java
class Fu{
    int num ;
    Fu(){
        num = 10;
        System.out.println("A fu run" );
    }
    Fu(int x){
        System.out.println("B fu run..." + x);
    }
}

class Zi extends Fu{
    Zi(){
        //super();//默认调用的就是父类中的空参数的构造函数
        System.out.println("C zi run " + num);
    }
    Zi(int x){
        super(4);
        System.out.println("D zi run " + x);
    }
}

class ExtendDemo{
    public static void main(String[] args){
        new Zi();
        System.out.println("-------------------" );
        new Zi(6);
    }
}
```
运行结果：

![1491308168245](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140841.png?xxfjava)

### 4.5 子类的实例化过程

子类中所有的构造函数默认都会访问父类中空参数的构造函数。因为每一个构造函数的第一行都有一条默认的语句super();。

为什么子类实例化的时候要访问父类中的构造函数呢？

那是因为子类继承了父类，获取到了父类中内容（属性），所以在使用父类内容之前，要先看父类是如何对自己的内容进行初始化的。

注意事项：

- 当父类中没有空参数的构造函数时，子类的构造函数必须通过this或者super语句指定要访问的构造函数。
- 子类构造函数中如果使用this调用了本类构造函数，那么默认的super();就没有了，因为super和this都只能定义在第一行，所以只能有一个。但是可以保证的是，子类中肯定会有其他的构造函数访问父类的构造函数。
- super语句必须要定义在子类构造函数的第一行！因为父类的初始化动作要先完成。

示例：
```java
class Fu{
    Fu(){
        super();
        //调用的是子类的show方法，此时其成员变量num还未进行显示初始化
        show();
        return;
    }
    void show(){
        System.out.println("fu show" );
    }
}
class Zi extends Fu{
    int num = 8;
    Zi(){
        super();
        //通过super初始化父类内容时，子类的成员变量并未显示初始化，等super()父类初始化完毕后，才进行子类的成员变量显示初始化
        return;
    }
    void show(){
        System.out.println("zi show..." + num);
    }
}
class ExtendDemo{
    public static void main(String[] args){
        Zi z = new Zi();
        z.show();
    }
}
```
运行结果：

![1491308191041](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140844.png?xxfjava)

总结：一个对象实例化过程，以`Person p = new Person();`为例

1. JVM会读取指定的路径下的Person.class文件，并加载进内存，并会先加载Person的父类（如果有直接的父类的情况下）
2. 在内存中开辟空间，并分配地址
3. 并在对象空间中，对对象的属性进行默认初始化
4. 调用对应的构造函数进行初始化
5. 在构造函数中，第一行会先到调用父类中构造函数进行初始化
6. 父类初始化完毕后，再对子类的属性进行显示初始化
7. 再进行子类构造函数的特定初始化
8. 初始化完毕后，将地址值赋值给引用变量

### 4.6 final关键字

final关键字可用于修饰类、变量和方法，它有“无法改变”或者“最终”的含义，因此被final修饰的类、变量和方法将具有以下特性：

- final可以修饰类，方法，变量
- final修饰的类不可以被继承
- final修饰的方法不可以被覆盖
- final修饰的变量是一个常量，只能被赋值一次
- 为什么要用final修饰变量，其实，在程序中如果一个数据是固定的。那么直接使用这个数据就可以了，但是这种阅读性差，所以应该给数据起个名称。而且这个变量名称的值不能变化，所以加上final固定
- 写法规范：常量所有字母都大写，多个单词，中间用_连接

示例1：

```java
 //继承弊端：打破了封装性
 class Fu{
        void method(){
        }
 }

 class Zi extends Fu{
        public static final double PI = 3.14;
        void method(){
             System.out.println(PI);
        }
 }

 class FinalDemo{
        public static void main(String[] args){
             Zi zi = new Zi();
             zi.method();
        }
 }
```
运行结果：

![1491308214198](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140847.png?xxfjava)

示例2：
```java
class FinalDemo{
    public static void main(String[] args){
        final int x = 4;
        x = 5;
    }
}
```
运行结果：

![1491308228930](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518140849.png?xxfjava)

### 4.7 内存结构

静态绑定，当方法被 static private final三个关键字其中一个修饰，执行的静态绑定

动态绑定，方法执行的动态绑定

属性看变量的类型，Person.this.salary

```java
public class TestChunApp
{

	public static void main(String[] args) {
		Person p = new Manager();
      	p.sing();
		//System.out.println(p);	//Person.this.salary
	}
}

class Person{
	private int salary = 10000;
	
	public static void sing(){
		System.out.println("忘情水");
	}
	int getSalary(){
		return salary;
	}
	public void printSalary(){
		System.out.println(salary);
	}
}

class Manager extends Person{
	int salary = 30000;
	
	public static void sing(){
		System.out.println("中国人");
	}
	public void printSalary(){
		System.out.println(getSalary());
	}
}
```

## 5. 多态

在设计一个方法时，通常希望该方法具备一定的通用性。例如要实现一个动物叫的方法，由于每种动物的叫声是不同的，因此可以在方法中接收一个动物类型的参数，当传入猫类对象时就发出猫类的叫声，传入犬类对象时就发出犬类的叫声。在同一个方法中，这种由于参数类型不同而导致执行效果各异的现象就是多态。继承是多态得以实现的基础。

在Java中为了实现多态，允许使用一个父类类型的变量来引用一个子类类型的对象，根据被引用子类对象特征的不同，得到不同的运行结果。

定义：某一类事物的多种存在形态。

例：动物中猫，狗。
猫这个对象对应的类型是猫类型：猫 x = new 猫();
同时猫也是动物中的一种，也可以把猫称为动物：动物  y = new 猫();
动物是猫和狗具体事物中抽取出来的父类型。
父类型引用指向了子类对象。

多态性简单说就是一个对象对应着不同类型。

体现：父类或者接口的引用指向或者接收自己的子类对象。
作用：多态的存在提高了程序的扩展性和后期可维护性。

前提：

1. 需要存在继承或者实现关系。
2. 需要有覆盖操作。

好处：提高了代码的扩展性，前期定义的代码可以使用后期的内容。
弊端：前期定义的内容不能使用（调用）后期子类的特有内容。

示例1：
```java
 abstract class Animal{
        abstract void eat();
 }

 class Dog extends Animal{
        void eat(){
             System.out.println("啃骨头");
        }
        void lookHome(){
             System.out.println("看家");
        }
 }

 class Cat extends Animal{
        void eat(){
             System.out.println("吃鱼");
        }
        void catchMouse(){
             System.out.println("抓老鼠");
        }
 }

 class Pig extends Animal{
        void eat(){
             System.out.println("饲料");
        }
        void gongdi(){
             System.out.println("拱地");
        }
 }

 class DuoTaiDemo{
        public static void main(String[] args){
             Cat c = new Cat();
             Dog d = new Dog();

              method(c);
              method(d);
              method(new Pig());
        }

        public static void method(Animal a){
             a.eat();
        }
 }
```
运行结果：

![1491308350957](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141149.png?xxfjava)

示例2：

```java
class DuoTaiDemo{
    public static void main(String[] args){
        //自动类型提升，猫对象提升到了动物类型。但是特有功能无法访问，作用就是限制对特有功能
        的访问。
        //专业讲：向上转型，将子类型隐藏。就不能使用子类的特有方法了。
        Animal a = new Cat();
        a.eat();
        //a.catchMouse();//报错

        //如果还想用具体动物猫的特有功能。
        //你可以将该对象进行向下转型。
        Cat c = (Cat)a; //向下转型的目的是为了能够使用子类中的特有方法。
        c.eat();
        c.catchMouse();

        //注意：对于转型，自始至终都是子类对象在做类型的变化。
        //Animal a = new Dog();
        //Cat c = (Cat)a;//但是类型不能随意转换，否则可能会报出ClassCastException的异常
    }

    public static void method(Animal a){
        a.eat();
    }
}
```
运行结果：

![1491308364716](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141154.png?xxfjava)

示例3：

```java
/*
 毕老师和毕姥爷的故事
 */
class 毕姥爷{
    void 讲课(){
        System.out.println("管理");
    }
    void 钓鱼(){
        System.out.println("钓鱼");
    }
}

class 毕老师 extends 毕姥爷{
    void 讲课(){
        System.out.println("Java");
    }
    void 看电影(){
        System.out.println("看电影");
    }
}

class DuoTaiDemo{
    public static void main(String[] args){
        毕老师 x = new 毕老师();
        x.讲课(); //Java
        x.看电影(); //看电影

        毕姥爷 y = new 毕老师();
        y.讲课(); //Java
        y.钓鱼(); //钓鱼

        毕老师 z = (毕老师)y;
        z.看电影(); //看电影
    }
}
```
运行结果：

![1491308380018](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141201.png?xxfjava)

instanceof ：用于判断对象的具体类型，只能用于引用数据类型判断，通常在向下转型前用于健壮性的判断。

示例4：

```java
class DuoTaiDemo{
    public static void main(String[] args){
    }

    public static void method(Animal a){
        a.eat();

        if(a instanceof Cat){
            Cat c = (Cat )a;
            c.catchMouse();
        }
        else if (a instanceof Dog){
            Dog d = (Dog )a;
            d.lookHome();
        }
    }
}
```

多态时，成员的特点：

1. 成员变量


编译时：参考引用型变量所属的类中是否有调用的成员变量。有，编译通过，没有，编译失败。

运行时：参考引用型变量所属的类中是否有调用的成员变量，并运行该所属类中的成员变量。

简单说：编译和运行都参考等号的左边。

示例：

```java
class Fu{
    int num = 3;
}

class Zi extends Fu{
    int num = 4;
}

class DuoTaiDemo{
    public static void main(String[] args){
        Zi f1 = new Zi();
        System.out.println(f1.num);
        Fu f2 = new Zi();
        System.out.println(f2.num);
    }
}
```
运行结果：

![1491308397512](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141206.png?xxfjava)

2. 成员函数（非静态）

编译时：参考引用型变量所属的类中是否有调用的函数。有，编译通过。没有，编译失败。

运行时：参考的是对象所属的类中是否有调用的函数。

简单说：编译看左边，运行看右边。

示例：
```java
class Fu{
    void show(){
        System.out.println("fu show");
    }
}

class Zi extends Fu{
    void show(){
        System.out.println("zi show");
    }
}

class DuoTaiDemo{
    public static void main(String[] args){
        Fu f = new Zi();
        f.show();
    }
}
```
运行结果：

![1491308420570](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141209.png?xxfjava)

3. 静态函数

编译时：参考的是对象所属的类中是否有调用的函数。

运行时：参考的是对象所属的类中是否有调用的函数。

简单说：编译和运行看左边。

示例：
```java
class Fu{
    static void method(){
        System.out.println("fu static method");
    }
}

class Zi extends Fu{
    static void method(){
        System.out.println("zi static method");
    }
}

class DuoTaiDemo{
    public static void main(String[] args){
        Fu f = new Zi();
        f.method();// fu static method
        Fu.method();
    }
}
```
运行结果：

![1491308433699](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141212.png?xxfjava)

### 5.1 java中实现多态的机制是什么？

靠的是父类或接口定义的引用变量可以指向子类或具体实现类的实例对象，而程序调用的方法在运行期才动态绑定，就是引用变量所指向的具体实例对象的方法，也就是内存里正在运行的那个对象的方法，而不是引用变量的类型中定义的方法。重写、重载是多态性的不同表现

父类A有一个方法function(),子类B,C分别继承A并且重写function()，当创建一个对象A b = new B();b.function()就调用B的funciotn,假如你new C(),那调用的就是C重写的function。怎么判断使用那个类的function就是动态绑定，这个现象就是多态...

动态绑定机制

## 6. 抽象类

当定义一个类时，常常需要定义一些方法来描述该类的行为特征，但有时这些方法的实现方式是无法确定的。例如前面在定义Animal类时，shout()方法用于表示动物的叫声，但是针对不同的动物，叫声也是不同的，因此在shout()方法中无法准确描述动物的叫声。

针对上面描述的情况，Java允许在定义方法时不写方法体，不包含方法体的方法为抽象方法，抽象方法必须使用abstract关键字来修饰。

### 6.1 抽象类概述

抽象定义：抽象就是从多个事物中将共性的、本质的内容抽取出来。例如：狼和狗共性都是犬科，犬科就是抽象出来的概念。

抽象类：Java中可以定义没有方法体的方法，该方法的具体实现由子类完成，该方法称为抽象方法，包含抽象方法的类就是抽象类。

抽象方法的由来：多个对象都具备相同的功能，但是功能具体内容有所不同，那么在抽取过程中，只抽取了功能定义，并未抽取功能主体，那么只有功能声明，没有功能主体的方法称为抽象方法。

例如：狼和狗都有吼叫的方法，可是吼叫内容是不一样的。所以抽象出来的犬科虽然有吼叫功能，但是并不明确吼叫的细节。

抽象类实际上是定义了一个标准和规范，等着他的子类们去实现。

### 6.2 抽象类的特点

抽象类不能创建实例，只能当成父类来被继承。抽象类是从多个具体类中抽象出来的父类，它具有更高层次的抽象。

抽象类体现的就是一种模板模式的设计，抽象类作为多个子类的通用模板。

当一个类中包含了抽象方法，该类必须使用abstract关键字来修饰，使用abstract关键字修饰的类为抽象类。

在定义抽象类时需要注意，包含抽象方法的类必须声明为抽象类，但抽象类可以不包含任何抽象方法，只需使用abstract关键字来修饰即可。另外，抽象类是不可以被实例化的，因为抽象类中有可能包含抽象方法，抽象方法是没有方法体的，不可以被调用。如果想调用抽象类中定义的方法，则需要创建一个子类，在子类中将抽象类中的抽象方法进行实现。

抽象类和抽象方法必须用abstract关键字来修饰。
抽象方法只有方法声明，没有方法体，定义在抽象类中。
格式：修饰符 abstract 返回值类型 函数名(参数列表) ;
抽象类不可以被实例化，也就是不可以用new创建对象。

原因如下：

1. 抽象类是具体事物抽取出来的，本身是不具体的，没有对应的实例。例如：犬科是一个抽象的概念，真正存在的是狼和狗。

2. 而且抽象类即使创建了对象，调用抽象方法也没有意义。

3. 抽象类通过其子类实例化，而子类需要覆盖掉抽象类中所有的抽象方法后才可以创建对象，否则该子类也是抽象类。

示例：

```java
 abstract class Demo{
        abstract /*抽象*/ void show();
 }

 class DemoA extends Demo{
        void show(){
             System.out.println("demoa show" );
        }
 }

 class DemoB extends Demo{
        void show(){
             System.out.println("demob show" );
        }
 }

 class AbstractDemo{
        public static void main(String[] args){
             DemoA demoA = new DemoA();
             demoA.show();

             DemoB demoB = new DemoB();
             demoB.show();     
        }
 }
```
运行结果：

![1491308244992](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141223.png?xxfjava)

### 6.3 抽象类举例代码讲解

需求：公司中程序员有姓名，工号，薪水，工作内容。项目经理除了有姓名，工号，薪水，还有奖金，工作内容。

分析：在这个问题领域中，通过名词提炼法：

程序员：
属性：姓名，工号，薪水。
行为：工作。

经理：
属性：姓名，工号，薪水，奖金。
行为：工作。

程序员和经理不存在着直接继承关系。但是，程序员和经理却具有共性内容，可以进行抽取，因为他们都是公司的雇员。可以将程序员和经理进行抽取，建立体系。

代码：

```java
 //描述雇员。
 abstract class Employee{
        private String name ;
        private String id ;
        private double pay ;

       Employee(String name,String id, double pay){
              this.name = name;
              this.id = id;
              this.pay = pay;
       }

        public abstract void work();
 }

 //描述程序员
 class Programmer extends Employee{
       Programmer(String name,String id, double pay){
              super(name,id,pay);
       }

        public void work(){
             System.out.println("code..." );
        }
 }

 //描述经理
 class Manager extends Employee{
       private int bonus ;

       Manager(String name,String id, double pay,int bonus){
              super(name,id,pay);
              this.bonus = bonus;
       }

       public void work(){
             System.out.println("manage" );
       }
 }
```
### 6.4 抽象类相关问题

抽象类中是否有构造函数？
答：有，用于给子类对象进行初始化。

抽象关键字abstract不可以和哪些关键字共存？
答：private、static、final。

抽象类中可不可以没有抽象方法？

答：可以，但是很少见。目的就是不让该类创建对象，AWT的适配器对象就是这种类。通常这个类中的方法有方法体，但是却没有内容。

示例：

```java
abstract class Demo{
     void show1(){}
     void show2(){}
}
```
抽象类和一般类的区别？

相同点：抽象类和一般类都是用来描述事物的，都在内部定义了成员。

不同点：
1. 一般类有足够的信息描述事物。抽象类描述事物的信息有可能不足。
2. 一般类中不能定义抽象方法，只能定义非抽象方法。抽象类中可定义抽象方法，同时也可以定义非抽象方法。
3. 一般类可以被实例化。抽象类不可以被实例化。

抽象类一定是个父类吗？
答：是的，因为需要子类覆盖其方法后才可以对子类实例化。

## 7. 接口
当一个抽象类中的方法都是抽象的时候，这时可以将该抽象类用另一种形式定义和表示，就是接口。

格式：interface {}
接口中的成员修饰符是固定的：
成员常量：public static final
成员函数：public abstract
由此得出结论，接口中的成员都是公共的权限
接口是对外暴露的规则
接口是程序的功能扩展

注意事项：

- 虽然抽象类中的全局变量和抽象方法的修饰符都可以不用写，但是这样阅读性很差。所以，最好写上
- 类与类之间是继承关系，类与接口直接是实现关系
- 接口不可以实例化，能由实现了接口并覆盖了接口中所有的抽象方法的子类实例化。否则，这个子类就是一个抽象类。

通常所说的接口

- 接口还可以是对外提供的服务，你封装好的类库供人调用，这也可以理解为接口
- 程序接口应该是你公布出来供别人调用使用的类和方法
- 接口一般指的是HTTP接口，也可以说是HTTP API
- 接口由后端提供，前端调用后端接口以获取后端数据
- 接口由URL和HTTP方法构成，URL为接口的地址，HTTP方法指的是GET, PUT, DELETE等

示例：
```java
interface Demo{
    public static final int NUM = 4;
    public abstract void show1();
    public abstract void show2();
}

class DemoImpl implements /*实现*/Demo{
    public void show1(){}
    public void show2(){}
}

class InterfaceDemo{
    public static void main(String[] args){
        DemoImpl d = new DemoImpl();
        System.out.println(d.NUM);
        System.out.println(DemoImpl.NUM);
        System.out.println(Demo.NUM);
    }
}
```
运行结果：

![1491308269877](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141232.png?xxfjava)

接口的出现将“多继承”通过另一种形式体现出来，即“多实现”。
在java中不直接支持多继承，因为会出现调用的不确定性。
所以，java将多继承机制进行改良，在java中变成了多实现，一个类可以实现多个接口。
接口的出现避免了单继承的局限性。
示例：

```java
interface A{
    public void show();
}

interface Z{
    public void show();
}

//多实现
class Test implements A,Z{
    public void show(){
        System.out.println("Test");
    }
}

class InterfaceDemo{
    public static void main(String[] args){
        Test t = new Test();
        t.show();
    }
}
```
运行结果：

![1491308285140](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141235.png?xxfjava)

一个类在继承另一个类的同时，还可以实现多个接口。
示例1：

```java
 interface A{
        public void show();
 }

 interface Z{
        public void show();
 }

 class Q{
     public void method(){
     }
 }

 abstract class Test2 extends Q implements A,Z{

 }
```
示例2：
```java
 interface CC{
        void show();
 }

 interface MM{
        void method();
 }

 //接口与接口之间是继承关系，而且接口可以多继承
 interface QQ extends CC,MM{
        public void function();
 }

 class WW implements QQ{
        //覆盖3个方法
        public void show(){}
        public void method(){}
        public void function(){}
 }
```
抽象类和接口的异同点？

相同点：都是不断向上抽取而来的。

不同点：

1. 抽象类需要被继承，而且只能单继承。接口需要被实现，而且可以多实现。
2. 抽象类中可以定义抽象方法和非抽象方法，子类继承后，可以直接使用非抽象方法。接口中只能定义抽象方法，必须由子类去实现。
3. 抽象类的继承，是is a关系，定义该体系的基本共性内容。接口的实现是like a关系

### 7.1 接口应用综合案例

代码：

```java
/*
笔记本电脑使用。
为了扩展笔记本的功能，但日后出现什么功能设备不知道。
因此需要定义一个规则，只要日后出现的设备都符合这个规则就可以了。
规则在java中就是接口。
*/
interface USB{//暴露的原则
    public void open();
    public void close();
}

//实现原则
//这些设备和电脑的耦合性降低了
class UPan implements USB{
    public void open(){
        System.out.println("upan open");
    }
    public void close(){
        System.out.println("upan close");
    }
}

class UsbMouse implements USB{
    public void open(){
        System.out.println("usbMouse open");
    }
    public void close(){
        System.out.println("usbMouse close");
    }
}

class BookPC{
    public static void main(String[] args){
        //功能扩展了
        useUSB(new UPan());
    }
    //使用原则
    public static void useUSB(USB u){//接口类型的引用，用于接收（指向）接口的子类对象
        if(u != null ){
            u.open();
            u.close();
        }
    }
}
```
运行结果：

![1491308317919](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141239.png?xxfjava)

### 7.2 Java8改进的接口

Java 8 对接口进行了改进，允许在接口中定义默认方法，默认方法可以提供方法实现。

### 7.3抽象类和接口的区别

1.语法层面上的区别

- 抽象类可以提供成员方法的实现细节，而接口中只能存在public abstract 方法；

- 抽象类中的成员变量可以是各种类型的，而接口中的成员变量只能是public static final类型的；

- 接口中不能含有静态代码块以及静态方法，而抽象类可以有静态代码块和静态方法；

- 一个类只能继承一个抽象类，而一个类却可以实现多个接口。

2.设计层面上的区别

接口是事物的能力，直接理解就是约定；抽象类是事物的本质。

抽象类是对一种事物的抽象，即对类抽象，而接口是对行为的抽象。抽象类是对整个类整体进行抽象，包括属性、行为，但是接口却是对类局部（行为）进行抽象。

继承是 `is a`的关系，而 接口实现则是 `has a` 的关系。如果一个类继承了某个抽象类，则子类必定是抽象类的种类，而接口实现就不需要有这层类型关系。

设计层面不同，抽象类作为很多子类的父类，它是一种模板式设计。而接口是一种行为规范，它是一种辐射式设计。也就是说：

- 对于抽象类，如果需要添加新的方法，可以直接在抽象类中添加具体的实现，子类可以不进行变更；
- 而对于接口则不行，如果接口进行了变更，则所有实现这个接口的类都必须进行相应的改动。

抽象类是对一种事物的抽象，即对类抽象，而接口是对行为的抽象。抽象类是对整个类整体进行抽象，包括属性、行为，但是接口却是对类局部（行为）进行抽象。举个简单的例子，飞机和鸟是不同类的事物，但是它们都有一个共性，就是都会飞。那么在设计的时候，可以将飞机设计为一个类Airplane，将鸟设计为一个类Bird，但是不能将 飞行 这个特性也设计为类，因此它只是一个行为特性，并不是对一类事物的抽象描述。此时可以将 飞行 设计为一个接口Fly，包含方法fly( )，然后Airplane和Bird分别根据自己的需要实现Fly这个接口。然后至于有不同种类的飞机，比如战斗机、民用飞机等直接继承Airplane即可，对于鸟也是类似的，不同种类的鸟直接继承Bird类即可。从这里可以看出，继承是一个 “是不是”的关系，而 接口 实现则是 “有没有”的关系。如果一个类继承了某个抽象类，则子类必定是抽象类的种类，而接口实现则是有没有、具备不具备的关系，比如鸟是否能飞（或者是否具备飞行这个特点），能飞行则可以实现这个接口，不能飞行就不实现这个接口。

设计层面不同，抽象类作为很多子类的父类，它是一种模板式设计。而接口是一种行为规范，它是一种辐射式设计。什么是模板式设计？最简单例子，大家都用过ppt里面的模板，如果用模板A设计了ppt B和ppt C，ppt B和ppt C公共的部分就是模板A了，如果它们的公共部分需要改动，则只需要改动模板A就可以了，不需要重新对ppt B和ppt C进行改动。而辐射式设计，比如某个电梯都装了某种报警器，一旦要更新报警器，就必须全部更新。也就是说对于抽象类，如果需要添加新的方法，可以直接在抽象类中添加具体的实现，子类可以不进行变更；而对于接口则不行，如果接口进行了变更，则所有实现这个接口的类都必须进行相应的改动。

下面看一个网上流传最广泛的例子：门和警报的例子：门都有open( )和close( )两个动作，此时我们可以定义通过抽象类和接口来定义这个抽象概念：

```java
abstract class Door {
    public abstract void open();
    public abstract void close();
}
```

或者

```java
interface Door {
    public abstract void open();
    public abstract void close();
}
```

但是现在如果我们需要门具有报警alarm( )的功能，那么该如何实现？下面提供两种思路：

- 将这三个功能都放在抽象类里面，但是这样一来所有继承于这个抽象类的子类都具备了报警功能，但是有的门并不一定具备报警功能；

- 将这三个功能都放在接口里面，需要用到报警功能的类就需要实现这个接口中的open( )和close( )，也许这个类根本就不具备open( )和close( )这两个功能，比如火灾报警器。

从这里可以看出， Door的open() 、close()和alarm()根本就属于两个不同范畴内的行为，open()和close()属于门本身固有的行为特性，而alarm()属于延伸的附加行为。因此最好的解决办法是单独将报警设计为一个接口，包含alarm()行为,Door设计为单独的一个抽象类，包含open和close两种行为。再设计一个报警门继承Door类和实现Alarm接口。

```java
interface Alram {
    void alarm();
}
 
abstract class Door {
    void open();
    void close();
}
 
class AlarmDoor extends Door implements Alarm {
    void oepn() {
      //....
    }
    void close() {
      //....
    }
    void alarm() {
      //....
    }
}
```

3.实际应用上的差异

在实际使用中，使用抽象类(也就是继承)，是一种强耦合的设计，用来描述`A is a B` 的关系，即如果说A继承于B，那么在代码中将A当做B去使用应该完全没有问题。比如在Android中，各种控件都可以被当做View去处理。

如果在你设计中有两个类型的关系并不是`is a`，而是`is like a`，那就必须慎重考虑继承。因为一旦我们使用了继承，就要小心处理好子类跟父类的耦合依赖关系。组合优于继承。

继承和实现接口的区别就是：继承描述的是这个类『是什么』的问题，而实现的接口则描述的是这个类『能做什么』的问题。

抽象类

- 体现的是 is a 关系，即继承关系，抽象类是多个具体类的抽象，是用来被继承的。
- 体现的是一种模板模式的设计

接口

- 定义的是一种规范，体现的是一种规范和实现分离的哲学
- 对Java单继承的补充，是一种功能增强
- 接口回调，一种消息通信机制
- 面向接口编程，解耦
- 接口无法保存状态

## 8. 内部类

定义：将一个类定义在另一个类的里面，里面那个类就称为内部类（内置类，嵌套类）。

访问特点：内部类可以直接访问外部类中的成员，包括私有成员。而外部类要访问内部类中的成员必须要建立内部类的对象。

示例1：

```java
 /*
 内部类的设计：
 分析事物时，发现该事物描述中还有事物，而且这个事物还在访问被描述事物的内容，这时候就定义
内部类。
 */
 class Outer{
        private int num = 3;

        class Inner //内部类
        {
              void show(){
                   System.out.println("show run..." + num);
              }
        }

        public void method(){
             Inner in = new Inner();
             in.show();
        }
 }

 class InnerClassDemo{
        public static void main(String[] args){
             Outer out = new Outer();
             out.method();
        }
 }
```
运行结果：

![1491308448323](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141338.png?xxfjava)

示例2：

```java
class Outer{
    private int num = 3;

    class Inner
    {
        void show(){
            System.out.println("show run..." + num);
        }
    }
}

class InnerClassDemo{
    public static void main(String[] args){
        //直接访问外部类中的内部类中的成员
        Outer.Inner in = new Outer().new Inner();
        in.show();
    }
}
```
运行结果：

![1491308458976](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141340.png?xxfjava)

### 8.1 内部类的位置

内部类定义在成员位置上，可以被private、static成员修饰符修饰。被static修饰的内部类只能访问外部类中
的静态成员。

示例1：

```java
class Outer{
    private static int num = 3;

    static class Inner
    {
        void show(){
            System.out.println("show run..." + num);
        }
    }
}

class InnerClassDemo{
    public static void main(String[] args){
        //如果内部类是静态的，相当于一个外部类
        Outer.Inner in = new Outer.Inner();
        in.show();
    }
}
```
运行结果：

![1491308470712](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141343.png?xxfjava)

示例2：如果内部类是静态的，内部类成员也是静态的，可以不用创建内部类对象，直接调用。
```java
class Outer{
    private static int num = 3;

    static class Inner
    {
        static void show(){
            System.out.println("show run..." + num);
        }
    }
}

class InnerClassDemo{
    public static void main(String[] args){
        Outer.Inner.show();
    }
}
```
运行结果：

![1491308482425](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141345.png?xxfjava)

PS：如果内部类中定义了静态成员，该内部类也必须是静态的！

示例：
```java
class Outer{
    private static int num = 3;

    static class Inner
    {
        static void show(){
            System.out.println("show run..." + num);
        }
    }
}
```
2、为什么内部类能直接访问外部类中的成员呢？
那是因为内部类持有了外部类的引用，外部类名.this。

示例：

```java
class Outer{
    int num = 3;
    class Inner{
        int num = 4;
        void show(){
            int num = 5;
            System.out.println(num);
            System.out.println(this.num);
            System.out.println(Outer.this.num);
        }
    }
    void method(){
        new Inner().show();
    }
}

class InnerClassDemo{
    public static void main(String[] args){
        new Outer().method();
    }
}
```
运行结果：

![1491308497028](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141348.png?xxfjava)

3、内部类定义在局部位置上，也可以直接访问外部类中的成员。
同时可以访问所在局部中的局部变量，但必须是被final修饰的。

示例：

```java
class Outer{
    int num = 3;
    void method(final int y){
        final int x = 9;
        class Inner{
            void show(){
                System.out.println("show..." + x + "," + y);
            }
        }
        Inner in = new Inner();
        in.show();
    }
}

class InnerClassDemo{
    public static void main(String[] args){
        new Outer().method(4);
    }
}
```
运行结果：

![1491308507996](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141350.png?xxfjava)

### 8.2 匿名内部类

定义：就是内部类的简化写法。
前提：内部类可以继承或实现一个外部类或者接口。
格式：new 外部类名或者接口名(){覆盖类或者接口中的代码，(也可以自定义内容。)}
简单理解：就是建立一个带内容的外部类或者接口的子类匿名对象。

什么时候使用匿名内部类呢？
通常使用方法是接口类型参数，并且该接口中的方法不超过三个，可以将匿名内部类作为参数传递。

好处：增强阅读性。

示例1：

```java
abstract class Demo{
    abstract void show();
}

class Outer{
    int num = 4;

    void method(){
        new Demo(){//匿名内部类
            void show(){
                System.out.println("show......" + num);
            }
        }.show();
    }
}

class InnerClassDemo{
    public static void main(String[] args){
        new Outer().method();
    }
}
```
运行结果：

![1491308522362](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141353.png?xxfjava)

示例2：

```java
interface Inter{
    void show1();
    void show2();
}

class Outer{
    public void method(){
        Inter in = new Inter(){
            public void show1(){
                System.out.println("...show1...." );
            }
            public void show2(){
                System.out.println("...show2...." );
            }
        };
        in.show1();
        in.show2();
    }
}

class InnerClassDemo{
    public static void main(String[] args){
        new Outer().method();
    }
}
```
运行结果：

![1491308534469](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141356.png?xxfjava)

示例3：

```java
interface Inter{
    void show1();
    void show2();
}

/*
通常的使用场景之一：
当函数参数是接口类型时，而且接口中的方法不超过三个。
可以用匿名内部类作为实际参数进行传递。
*/
class InnerClassDemo{
    public static void main(String[] args){
        show(new Inter(){
            public void show1(){
                System.out.println("...show1..." );
            }
            public void show2(){
                System.out.println("...show2..." );
            }
        });
    }
    public static void show(Inter in){
        in.show1();
        in.show2();
    }
}
```
运行结果：

![1491308546397](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141358.png?xxfjava)

对象的初始化过程

示例：

```java
class Fu{
    int num = 9;

    {
        System.out.println("Fu" );
    }

    Fu(){
        super();//Object
        //显示初始化
        //构造代码块初始化
        show();
    }
    void show(){
        System.out.println("fu show " + num);//被覆盖，运行子类的
    }
}

class Zi extends Fu{
    int num = 8;

    {
        System.out.println("Zi" );
    }

    Zi(){
        super();
        //显示初始化
        //构造代码块初始化
        show();
    }

    void show(){
        System.out.println("zi show " + num);
    }
}

public class Demo{
    public static void main(String[] args){
        new Zi();
    }
}
```
运行结果

![1491308560153](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141402.png?xxfjava)

## 9. 接口回调

> 原文链接：http://blog.csdn.net/aigestudio/article/details/40869893

废话不多说，像许多网上介绍回调机制的文章一样，我这里也以一个现实的例子开头：假设你公司的总经理出差前需要你帮他办件事情，这件事情你需要花些时间去做，这时候总经理肯定不能守着你做完再出差吧，于是就他告诉你他的手机号码叫你如果事情办完了你就打电话告诉他一声；这是一个现实生活中常能碰到的例子，我们用呢就用代码的方式来实现一个这个过程，看一下这个过程究竟是怎样的。

首先在Eclipse中新建一个Java项目：CallBackDemoInJava

然后再新建三个类：Manager（该类用来模拟总经理），Personnel（该类用来模拟员工），Main（主类）

![接口回调](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141249.jpg?xxfjava)

Manager的代码如下：

```java
package com.aige.test;  

/** 
 * @description 该类用来模拟总经理
 */  
public class Manager {  
    /** 
     * @param personnel 传入一个员工类的对象 
     */  
    public Manager(Personnel personnel) {  
        // 想让该让员工做什么  
        personnel.doSomething(this, "整理公司文件");  
    }  

    /** 
     * @description 当员工做完总经理让他做的事后就通过该方法通知总经理 
     * @param result 事情结果 
     */  
    public void phoneCall(String result) {  
        System.out.println("事情" + result);  
    }  
}  
```

Manager类是个模拟总经理的类，当该类的对象被造出来后就会通过Personnel的对象去执行Personnel中的doSomething方法告诉员工做些什么

Personnel的代码如下：

```java
package com.aige.test;  

/** 
 * @description 该类用来模拟员工
 */  
public class Personnel {  
    public void doSomething(Manager manager, String task) { 
        // 总经理通过doSomething方法告诉员工要做什么  
        System.out.println("总经理要你做" + task);  
        String result = "做完了";  
        // 当事情做完了我们就通过总经理公布的phoneCall方法通知总经理结果  
        manager.phoneCall(result);  
    }  
}  
```

总经理通过调用Personnel中的doSomething方法告诉员工该做些什么，当员工做完后就通过Manager中的phoneCall方法通知总经理结果

那么好的！万事俱备，我们在Main中测试运行下代码看看结果：

```java
package com.aige.test;  

public class Main {  
    public static void main(String[] args) {  
        // 首先我们需要一个员工  
        Personnel personnel = new Personnel();  
        // 其次把这个员工指派给总经理  
        new Manager(personnel);  
    }  
}  
```

代码执行结果如下：

![接口回调](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141254.jpg?xxfjava)

回到我们刚才举的那个现实中的例子：总经理出差前要你去办件事情，这件事情通过doSomething告诉你了，事情要是办完了就打总经理的电话phoneCall通知他一声。这里的phoneCall我们就称为回调方法，为什么称之为回调呢？你问我我也不清楚哈，这你得问Sun公司了，不过我们从代码的执行过程可以看出数据的流向大致是Manager → Personnel → Manager，这不就是一个“回调”的过程么？现在我们来总结下满足回调的两个基本条件：

- Class A调用Class B中的X方法
- ClassB中X方法执行的过程中调用Class A中的Y方法完成回调

一切看上去都很完美，以上例子代码简单通俗地描述了回调，但是这里我就会有这样一个疑问：假设总经理出差前交了件事情给我去办，不巧……副总经理也要给我件事去办，更无耻的是……主管也发任务过来了，都要求说做完就打电话通知他们……这时我们就要定义更多类，什么总经理类啦、经理类啦、主管类啦、杂七杂八的类，但是这些杂七杂八的大爷们都要求做完事情就电话通知，每个类都会有一个类似phoneCall的方法作为回调方法，这时，我们利用面向对象的思想来看，是不是可以把这个回调方法抽象出来作为一个独立的抽象类或接口呢？多态的思想油然而生，鉴于JAVA接口的好处，我们就定义一个名为CallBack的接口作为回调接口，再在该接口下定义一个名为backResult的抽象方法作为回调方法，让那些总经理类啦、经理类啦、主管类啦、什么的都去实现该接口，这时候我们就来改造下我们的项目：

![接口回调](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518141257.jpg?xxfjava)

CallBack代码如下：

```java
package com.aige.test;  

/** 
 * @description 回调接口
 */  
public interface CallBack {  
    // 回调方法  
    public void backResult(String result);  
} 
```
Manager代码改造后如下：实现CallBack接口重写backResult方法
```java
package com.aige.test;  

/** 
 * @description 该类用来模拟总经理
 */  
public class Manager implements CallBack {  
    /** 
     * @param personnel  传入一个员工类的对象 
     */  
    public Manager(Personnel personnel) {  
        // 想让该让员工做什么  
        personnel.doSomething(this, "整理公司文件");  
    }  

    /** 
     * @description 当员工做完总经理让他做的事后就通过该方法通知总经理 
     * @param result 事情结果 
     */  
    public void backResult(String result) {  
        System.out.println("事情" + result);  
    }  
}  
```

Personnel代码改造后如下：doSomething方法不再传入一个Manager对象而是一个CallBack接口

```java
package com.aige.test;  

/** 
 * @description 该类用来模拟员工
 */  
public class Personnel {  
    public void doSomething(CallBack callBack, String task) {  
        // 总经理通过doSomething方法告诉员工要做什么  
        System.out.println("总经理要你做" + task);  
        String result = "做完了"; 
        // 当事情做完了我们就通过总经理公布的phoneCall方法通知总经理结果  
        callBack.backResult(result);  
    }  
}  
```

Main代码不变，执行结果也是一样的。

至此，回调的基本概念差不多就是这样了~其实回调真心不难理解，但是回调在Android中相当重要，几乎处处可见回调机制，如果你能理解到回调的奥秘~我相信对你的Android技术是一个不小的提升

### 9.1 接口回调机制

接口回调就是一个通知机制，作用:

1. 单纯的通知
2. 通知+传值

步骤:

1. 定义接口以及接口方法
2. 定义接口对象
3. 在某一个地方，接口对象调用接口方法
4. 暴露接口对象(构造方法，setter方法)

代码示例

```java
// 1.定义接口,以及接口方法
public interface OnTeacherComeListener {
	void onTeachCome(String teacherName);
	void onTeachCome();
}
```

```java
public class ClassMate {
	// 2.定义接口对象
	OnTeacherComeListener	mOnTeacherComeListener;

	// 方式1:通过构造方法赋值
	public ClassMate(OnTeacherComeListener onTeacherComeListener) {
		super();
		mOnTeacherComeListener = onTeacherComeListener;
	}

	public ClassMate() {
		super();
	}

	// 模拟老师来了
	public void doTeacherCome(String teacherName) {
		// 3.在某一个地方.接口对象调用接口方法-->老师来了的时候
		mOnTeacherComeListener.onTeachCome(teacherName);
	}

	// 4.暴露接口对象(构造方法,setter方法)

	// 方式2:通过setter赋值
	public void setOnTeacherComeListener(OnTeacherComeListener onTeacherComeListener) {
		mOnTeacherComeListener = onTeacherComeListener;
	}
}
```

```java
public class Me {
	private Timer	mTimer;

	/**想睡觉*/
	public void wantSleep() {
		System.out.println("昨晚敲代码敲到4点钟,突然想睡觉...");
	}

	/**开始睡觉*/
	public void startSleep() {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				System.out.println("开始呼呼大睡.........");
			}
		}, 0, 1000);
	}

	/**停止睡觉*/
	public void stopSleep() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
			System.out.println("停止了睡觉");
		}
	}
}
```
我想要睡觉，但是睡觉前我跟同桌说，伍老师来了把我叫醒，要是李老师来了不用管，继续睡
```java
public class Test {
	public static void main(String[] args) {
		final Me me = new Me();

		// 我想睡觉
		me.wantSleep();
		// 找到一个同桌
		ClassMate classMate = new ClassMate();
		System.out.println("我去和同桌协商......");
		// 和他商量好-->如果老师来了.你拍醒我.让我停止睡觉
		classMate.setOnTeacherComeListener(new OnTeacherComeListener() {

			@Override
			public void onTeachCome(String teacherName) {// 通知+传值的效果
				if ("伍老师".equals(teacherName)) {// 回调过程中.有传值的效果
					System.out.println("伍老师不是班主任,我继续睡觉");
				} else if ("李老师".equals(teacherName)) {
					me.stopSleep();//
				}
			}

			@Override
			public void onTeachCome() {// 通知
			}
		});
		// 模拟商量了2s钟
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 开始睡觉
		me.startSleep();

		// 模拟伍老师来了.
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		classMate.doTeacherCome("伍老师");
		// 模拟伍老师来了.
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		classMate.doTeacherCome("李老师");

	}
}
```
### 9.2 同步、异步、回调、轮询

- 同步

好比你去麦当劳点餐，你说“来个汉堡”，服务员告诉你，对不起，汉堡要现做，需要等5分钟，于是你站在收银台前面等了5分钟，拿到汉堡再去逛商场，这是同步。

- 异步

你说“来个汉堡”，服务员告诉你，汉堡需要等5分钟，你可以先去逛商场，等做好了，我们再通知你，这样你可以立刻去干别的事情（逛商场），这是异步

- 回调、轮询

很明显，使用异步来编写程序性能会远远高于同步，但是异步的缺点是编程模型复杂。想想看，你得知道什么时候通知你“汉堡做好了”，而通知你的方法也各不相同。如果是服务员跑过来找到你，这是回调模式，如果服务员发短信通知你，你就得不停地检查手机，这是轮询模式。总之，异步的复杂度远远高于同步

### 9.3 回调函数

你到一个商店买东西，刚好你要的东西没有货，于是你在店员那里留下了你的电话，过了几天店里有货了，店员就打了你的电话，然后你接到电话后就到店里去取了货。在这个例子里，你的电话号码就叫回调函数，你把电话留给店员就叫登记回调函数，店里后来有货了叫做触发了回调关联的事件，店员给你打电话叫做调用回调函数，你到店里去取货叫做响应回调事件。

回调函数是你写一个函数，让预先写好的系统来调用。你去调用系统的函数，是直调。让系统调用你的函数，就是回调。但假如满足于这种一句话结论，是不会真正明白的。

回调函数可以看成，让别人做事，传进去的额外信息。

比如 A 让 B 做事，根据粒度不同，可以理解成 A 函数调用 B 函数，或者 A 类使用 B 类，或者 A 组件使用 B 组件等等。反正就是 A 叫 B 做事。

当 B 做这件事情的时候，自身的需要的信息不够，而 A 又有。就需要 A 从外面传进来，或者 B 做着做着再向外面申请。对于 B 来说，一种被动得到信息，一种是主动去得到信息，有人给这两种方式术语，叫信息的 push，和信息的 pull。

A 调用 B，A 需要向 B 传参数。如简单的函数：

```c
int max(int a, int b); 
```

要使用这函数，得到两者最大的值, 外面就要传进来 a, b。这个很好理解。

```c
void qsort(void *, size_t, size_t, int (*)(const void *, const void *));
```

而这个函数用于排序，最后一个参数就是回调函数，似乎就比较难以理解了。这是因为人为割裂了代码和数据。

我们暂停一下，看看计算机中比较诡异的地方，也就是代码(code)和数据(data)的统一。这是一个槛，如果不跨过这槛，很多概念就不清楚。我们常常说计算机程序分成 code 和 data 两部分。很多人会理解成，code 是会运行的，是动态的，data 是给 code 使用，是静态的，这是两种完全不同的东西。

其实 code 只是对行为的一种描述，比如有个机器人可以开灯，关灯，扫地。如果跟机器人约定好，0 表示开灯，1 表示关灯，2 表示扫地。我发出指令串，0 1 2，就可以控制机器人开灯，关灯，扫地。再约定用二进制表示，两位一个指令，就有一个数字串，000111，这个时候 000111 这串数字就描述了机器人的一系列动作，这个就是从一方面理解是 code，它可以控制机器人的行为。但另一方面，它可以传递，可以记录，可以修改，也就是数据。只要大家都协商好，code 就可以编码成 data, 将 data 解释运行的时候，也变成了 code。

code 和 data 可以不用区分，统一称为信息。既然 int max(int a, int b) 中 int，double 等表示普通 data 的东西可以传递进去，自然表示 code 的函数也可以传进去了。有些语言确实是不区分的，它的 function(表示code)跟 int, double 的地位是一样的。这种语言就为函数是第一类值。

而有些语言是不能存储函数，不能动态创建函数，不能动态销毁函数。只能存储一个指向函数的指针，这种语言称为函数是第二类值。

另外有些语言不单可以传递函数，函数里面又用到一些外部信息(包括code, data)。那些语言可以将函数跟函数所用到的信息一起传递存储。这种将函数和它所用的信息作为一个整体，就为闭包。

过了这个槛，将代码和数据统一起来，很多难以理解的概念就会清晰很多。

现在我们再回头看看回调函数。回调函数也就是是 A 让 B 做事，B 做着做着，信息不够，不知道怎么做了，就再让外面处理。

比如上述排序例子，A 让 B 排序，B 会做排序，但排序需要知道哪个比哪个大，这点 B 自己不知道，就需要 A 告诉它。而这种判断大小本身是一种动作，既然 C 语言中不可以传进第一值的函数，就设计成传递第二值的函数指针，这个函数指针就是 A 传向 B 的信息，用来表示一个行为。这里本来 A 调用 B 的，结果 B 又调用了 A 告诉它的信息，也就叫 callback。

再比如 A 让 B 监听系统的某个消息，比如敲了哪个键。跟着 B 监听到了，但它不知道怎么去处理这个消息，就给外面关心这个消息，又知道怎么去处理这个消息的人去处理，这个处理过程本身是个行为，既然这个语言不可以传递函数，又只能传一个函数指针了。假如我将函数指针存储下来，以后就可以随时调用。代码和数据都是信息，数据可以存储下来，用来表示行为的函数自然也可以存储下来。

跟着有些人有会引申成，什么注册啊，通知啊等等等。假如 B 做监听，C, D, E, F, G, H 告诉 B 自己有兴趣知道这消息，那 B 监听到了就去告诉 C,D,E,F,G等人了，这样通知多人了，就叫广播。

理解后进行思考，根本不用关心术语。术语只是为了沟通，别人要告诉你，或者你去告诉人，使用的一套约定的词语。同一个东西往往有不同术语。

再将回调的概念泛化，比如某人同时关心 A, B, C, D, E, F 事件，并且这些事件是一组的，比如敲键盘，鼠标移动，鼠标点击等一组。将一组事件结合起来。在有些语言就映射成接口，接口有 N 个函数。有些语言就映射成一个结构，里面放着 N 个函数指针。跟着就不是将单个函数指针传进去，而是将接口，或者函数指针的结构传进去。根据不同的用途，有些人叫它为代理，监听者，观察者等等。

实际上也是将某种行为存储下来，以后有需要再进行调用。跟回调函数在更深层次是没有区别的。