![13 Java IO流](https://image.xiaoxiaofeng.site/blog/2023/08/29/xxf-20230829105358.png?xxfjava)
## 1. 流

### 1.1 流的概念

流(stream)的概念源于UNIX中管道(pipe)的概念。在UNIX中，管道是一条不间断的字节流，用来实现程序或进程间的通信，或读写外围设备、外部文件等。

一个流，必有源端和目的端，它们可以是计算机内存的某些区域，也可以是磁盘文件，甚至可以是Internet上的某个URL。

流的方向是重要的，根据流的方向，流可分为两类：输入流和输出流。用户可以从输入流中读取信息，但不能写它。相反，对输出流，只能往输入流写，而不能读它。

实际上，流的源端和目的端可简单地看成是字节的生产者和消费者，对输入流，可不必关心它的源端是什么，只要简单地从流中读数据，而对输出流，也可不知道它的目的端，只是简单地往流中写数据。

形象的比喻——水流 ，文件==程序 ，文件和程序之间连接一个管道，水流就在之间形成了,自然也就出现了方向：可以流进，也可以流出.便于理解，这么定义流： 流就是一个管道里面有流水，这个管道连接了文件和程序。

### 1.2 IO流概述

大多数应用程序都需要实现与设备之间的数据传输，例如键盘可以输入数据，显示器可以显示程序的运行结果等。在Java中，将这种通过不同输入输出设备（键盘，内存，显示器，网络等）之间的数据传输抽象的表述为“流”，程序允许通过流的方式与输入输出设备进行数据传输。Java中的“流”都位于Java.io包中，称之为IO（输入输出）流。 IO流：即Input Output的缩写。

输入流和输出流相对于内存设备而言。将外设中的数据读取到内存中：输入。将内存的数写入到外设中：输出。

IO流的特点：

- IO流用来处理设备间的数据传输。
- Java对数据的操作是通过流的方式。
- Java用于操作流的对象都在IO包中。
- 流按操作数据分为两种：字节流和字符流。
- 流按流向分为：输入流和输出流。

PS：流只能操作数据，而不能操作文件。

### 1.3 流的三种分类方式

- 按流的方向分为：输入流和输出流     
- 按流的数据单位不同分为：字节流和字符流     
- 按流的功能不同分为:节点流和处理流

### 1.4 流的层次结构

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180144.png?xxfjava)

 IO流的常用基类：

- 字节流的抽象基流：InputStream和OutputStream
- 字符流的抽象基流：Reader和Writer

PS：此四个类派生出来的子类名称都是以父类名作为子类名的后缀，以前缀为其功能；如InputStream子类FileInputStream；Reader子类FileReader

字符流的由来：

其实就是：字节流读取文字字节数据后，不直接操作而是先查指定的编码表，获取对应的文字。再对这个文字进行操作。简单说：字节流+编码表。

## 2. 字节流

在计算机中，无论是文本、图片、音频还是视频，所有文件都是以二进制(字节)形式存在的，IO流中针对字节的输入输出提供了一系列的流，统称为字节流。字节流是程序中最常用的流，根据数据的传输方向可将其分为字节输入流和字节输出流。在JDK中，提供了两个抽象类InputStream和OutputStream，它们是字节流的顶级父类，所有的字节输入流都继承自InputStream，所有的字节输出流都继承自OutputStream。为了方便理解，可以把InputStream和OutputStream比作两根“水管”，如图所示。

![1500708330117](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180156.png?xxfjava)

InputStream和OutputStream这两个类虽然提供了一系列和读写数据有关的方法，但是这两个类是抽象类，不能被实例化，因此，针对不同的功能，InputStream和OutputStream提供了不同的子类，这些子类形成了一个体系结构，如图所示。

![1500708378450](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180158.png?xxfjava)

![1500708385651](https://image.xiaoxiaofeng.site/blog/2023/08/10/xxf-20230810100639.png?xxfjava)

### 2.1 字节流写数据

字节输出流：抽象类OutputStream，实现类FileOutputStream

字节输出流操作步骤：

- 创建字节输出流对象
- 写数据
- 释放资源

字节流写数据的方式

| 方法                              | 说明            |
| :------------------------------ | :------------ |
| write(int b)                    | 一次写一个字节       |
| write(byte[] b)                 | 一次写一个字节数组     |
| write(byte[] b,int off,int len) | 一次写一个字节数组的一部分 |
| flush()                         | 刷新缓冲区         |
| close()                         | 释放资源          |

如何实现数据的换行?

- 为什么没有换行呢?因为只是写了字节数据，并没有写入换行符号。
- 如何实现呢?写入换行符号即可

PS：不同的系统针对不同的换行符号识别是不一样的?

- windows：\r\n
- linux：\n
- Mac：\r
- 而一些常见的个高级记事本，是可以识别任意换行符号的。

如何实现数据的追加写入?

用构造方法带第二个参数是true的情况即可

```java
package cn.xiaoxiaofeng;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/*
 * 需求：我要往一个文本文件中输入一句话："hello,io"
 *
 * 分析：
 * 	A:这个操作最好是采用字符流来做，但是呢，字符流是在字节流之后才出现的，所以，今天我先讲解字节流如何操作。
 * 	B:由于我是要往文件中写一句话，所以我们要采用字节输出流。
 */
public class FileOutputStreamDemo {
	public static void main(String[] args) throws IOException {
		// 创建字节输出流对象
		// FileOutputStream(File file)
		// File file = new File("fos.txt");
		// FileOutputStream fos = new FileOutputStream(file);
		// FileOutputStream(String name)
		FileOutputStream fos = new FileOutputStream("fos.txt");
		/*
		 * 创建字节输出流对象了做了几件事情：
		 * A:调用系统功能去创建文件
		 * B:创建fos对象
		 * C:把fos对象指向这个文件
		 */

		//写数据
		fos.write("hello,IO".getBytes());
		fos.write("java".getBytes());

		//释放资源
		//关闭此文件输出流并释放与此流有关的所有系统资源。
		fos.close();
		/*
		 * 为什么一定要close()呢?
		 * A:让流对象变成垃圾，这样就可以被垃圾回收器回收了
		 * B:通知系统去释放跟该文件相关的资源
		 */
		//java.io.IOException: Stream Closed
		//fos.write("java".getBytes());
	}
}
```
### 2.2 字节流写数据的方式

```java
package cn.xiaoxiaofeng;
import java.io.FileOutputStream;
import java.io.IOException;
/*
 * 字节输出流操作步骤：
 * A:创建字节输出流对象
 * B:调用write()方法
 * C:释放资源
 *
 * public void write(int b):写一个字节
 * public void write(byte[] b):写一个字节数组
 * public void write(byte[] b,int off,int len):写一个字节数组的一部分
 */
public class FileOutputStreamDemo2 {
	public static void main(String[] args) throws IOException {
		// 创建字节输出流对象
		// OutputStream os = new FileOutputStream("fos2.txt"); // 多态
		FileOutputStream fos = new FileOutputStream("fos2.txt");

		// 调用write()方法
		//fos.write(97); //97 -- 底层二进制数据	-- 通过记事本打开 -- 找97对应的字符值 -- a
		// fos.write(57);
		// fos.write(55);

		//public void write(byte[] b):写一个字节数组
		byte[] bys={97,98,99,100,101};
		fos.write(bys);

		//public void write(byte[] b,int off,int len):写一个字节数组的一部分
		fos.write(bys,1,3);

		//释放资源
		fos.close();
	}
}
```
字节流写数据加入异常处理

```java
package cn.xiaoxiaofeng;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/*
 * 加入异常处理的字节输出流操作
 */
public class FileOutputStreamDemo4 {
	public static void main(String[] args) {
		// 分开做异常处理
		// FileOutputStream fos = null;
		// try {
		// fos = new FileOutputStream("fos4.txt");
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		//
		// try {
		// fos.write("java".getBytes());
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// try {
		// fos.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// 一起做异常处理
		// try {
		// FileOutputStream fos = new FileOutputStream("fos4.txt");
		// fos.write("java".getBytes());
		// fos.close();
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// 改进版
		// 为了在finally里面能够看到该对象就必须定义到外面，为了访问不出问题，还必须给初始化值
		FileOutputStream fos = null;
		try {
			// fos = new FileOutputStream("z:\\fos4.txt");
			fos = new FileOutputStream("fos4.txt");
			fos.write("java".getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 如果fos不是null，才需要close()
			if (fos != null) {
				// 为了保证close()一定会执行，就放到这里了
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
```
### 2.3 字节流读取数据

字节输入流：抽象类InputStream，实现类FileInputStream

字节输入流操作步骤：

- A：创建字节输入流对象
- B：调用read()方法读取数据，并把数据显示在控制台
- C：释放资源

字节流读数据的方式

| 方法                                 | 功能描述          |
| :--------------------------------- | :------------ |
| int read()                         | 一次读取一个字节      |
| int read(byte[] b)                 | 一次读取一个字节数组    |
| int read(byte[] b,int off,int len) | 一次读一个字节数组的一部分 |
| void close()                       | 释放资源          |

```java
package cn.xiaoxiaofeng;
import java.io.FileInputStream;
import java.io.IOException;
/*
 * 字节输入流操作步骤：
 * A:创建字节输入流对象
 * B:调用read()方法读取数据，并把数据显示在控制台
 * C:释放资源
 *
 * 读取数据的方式：
 * A:int read():一次读取一个字节
 * B:int read(byte[] b):一次读取一个字节数组
 * C:int read(byte[] b):一次读取一个字节数组
 *   返回值其实是实际读取的字节个数。
 */
public class FileInputStreamDemo {
	public static void main(String[] args) throws IOException {
		// FileInputStream(String name)
		// FileInputStream fis = new FileInputStream("fis.txt");
		FileInputStream fis = new FileInputStream("FileOutputStreamDemo.java");

		// // 调用read()方法读取数据，并把数据显示在控制台
		// // 第一次读取
		// int by = fis.read();
		// System.out.println(by);
		// System.out.println((char) by);
		//
		// // 第二次读取
		// by = fis.read();
		// System.out.println(by);
		// System.out.println((char) by);
		//
		// // 第三次读取
		// by = fis.read();
		// System.out.println(by);
		// System.out.println((char) by);
		// // 我们发现代码的重复度很高，所以我们要用循环改进
		// // 而用循环，最麻烦的事情是如何控制循环判断条件呢?
		// // 第四次读取
		// by = fis.read();
		// System.out.println(by);
		// // 第五次读取
		// by = fis.read();
		// System.out.println(by);
		// //通过测试，我们知道如果你读取的数据是-1，就说明已经读取到文件的末尾了

		// 用循环改进
		// int by = fis.read();
		// while (by != -1) {
		// System.out.print((char) by);
		// by = fis.read();
		// }

		// 最终版代码
		int by = 0;
		// 读取，赋值，判断
		while ((by = fis.read()) != -1) {
			System.out.print((char) by);
		}
                // 数组的长度一般是1024或者1024的整数倍
		byte[] bys = new byte[1024];
		int len = 0;
		while ((len = fis.read(bys)) != -1) {
			System.out.print(new String(bys, 0, len));
		}

		// 释放资源
		fis.close();
	}
}
```

### 2.4 字节流复制数据练习

代码示例：把c:\\a.txt内容复制到d:\\b.txt中

```java
package cn.xiaoxiaofeng;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/*
 * 需求：把c盘下的a.txt的内容复制到d盘下的b.txt中
 *
 * 数据源：
 * 		c:\\a.txt	--	读取数据--	FileInputStream
 * 目的地：
 * 		d:\\b.txt	--	写出数据	--	FileOutputStream
 */
public class CopyFileDemo2 {
	public static void main(String[] args) throws IOException {
		// 封装数据源
		FileInputStream fis = new FileInputStream("c:\\a.txt");
		// 封装目的地
		FileOutputStream fos = new FileOutputStream("d:\\b.txt");

		// 复制数据
		int by = 0;
		while ((by = fis.read()) != -1) {
			fos.write(by);
		}

		// 释放资源
		fos.close();
		fis.close();
	}
}
```
### 2.5字节缓冲流

一个字节一个字节的读写，需要频繁的操作文件，效率非常低。这就好比从北京运送烤鸭到上海，如果有一万只烤鸭，每次运送一只，就必须运输一万次，这样的效率显然非常低。为了减少运输次数，可以先把一批烤鸭装在车厢中，这样就可以成批的运送烤鸭，这时的车厢就相当于一个临时缓冲区。当通过流的方式拷贝文件时，为了提高效率也可以定义一个字节数组作为缓冲区。在拷贝文件时，可以一次性读取多个字节的数据，并保存在字节数组中，然后将字节数组中的数据一次性写入文件。

在IO包中提供两个带缓冲的字节流，分别是BufferedInputStream和BufferedOutputStream，它们的构造方法中分别接收InputStream和OutputStream类型的参数作为对象，在读写数据时提供缓冲功能。应用程序、缓冲流和底层字节流之间的关系如图所示。

![image-20230810163603278](https://image.xiaoxiaofeng.site/blog/2023/08/10/xxf-20230810163603.png?xxfjava)

字节流一次读写一个数组的速度明显比一次读写一个字节的速度快很多，这是加入了数组这样的缓冲区效果，java本身在设计的时候，也考虑到了这样的设计思想(装饰设计模式后面讲解)，所以提供了字节缓冲区流。

字节缓冲输出流：BufferedOutputStream，字节缓冲输入流：BufferedInputStream。

```java
package cn.xiaoxiaofeng;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/*
 * 通过定义数组的方式确实比以前一次读取一个字节的方式快很多，所以，看来有一个缓冲区还是非常好的。
 * 既然是这样的话，那么，java开始在设计的时候，它也考虑到了这个问题，就专门提供了带缓冲区的字节类。
 * 这种类被称为：缓冲区类(高效类)
 * 写数据：BufferedOutputStream
 * 读数据：BufferedInputStream
 *
 * 构造方法可以指定缓冲区的大小，但是我们一般用不上，因为默认缓冲区大小就足够了。
 *
 * 为什么不传递一个具体的文件或者文件路径，而是传递一个OutputStream对象呢?
 * 原因很简单，字节缓冲区流仅仅提供缓冲区，为高效而设计的。但是呢，真正的读写操作还得靠基本的流对象实现。
 */
public class BufferedOutputStreamDemo {
	public static void main(String[] args) throws IOException {
		// BufferedOutputStream(OutputStream out)
		// FileOutputStream fos = new FileOutputStream("bos.txt");
		// BufferedOutputStream bos = new BufferedOutputStream(fos);
		// 简单写法
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream("bos.txt"));

		// 写数据
		bos.write("hello".getBytes());

		// 释放资源
		bos.close();
	}
}
```
### 2.6 字节缓冲流复制数据练习

```java
package cn.xiaoxiaofeng;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/*
 * 需求：把e:\\a.mp4复制到当前项目目录下的b.mp4中
 *
 * 字节流四种方式复制文件：
 * 基本字节流一次读写一个字节：	共耗时：117235毫秒
 * 基本字节流一次读写一个字节数组： 共耗时：156毫秒
 * 高效字节流一次读写一个字节： 共耗时：1141毫秒
 * 高效字节流一次读写一个字节数组： 共耗时：47毫秒
 */
public class CopyMp4Demo {
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		// method1("e:\\a.mp4", "copy1.mp4");
		// method2("e:\\a.mp4", "copy2.mp4");
		// method3("e:\\a.mp4", "copy3.mp4");
		method4("e:\\a.mp4", "copy4.mp4");
		long end = System.currentTimeMillis();
		System.out.println("共耗时：" + (end - start) + "毫秒");
	}

	// 高效字节流一次读写一个字节数组：
	public static void method4(String srcString, String destString)
			throws IOException {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				srcString));
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(destString));

		byte[] bys = new byte[1024];
		int len = 0;
		while ((len = bis.read(bys)) != -1) {
			bos.write(bys, 0, len);
		}

		bos.close();
		bis.close();
	}

	// 高效字节流一次读写一个字节：
	public static void method3(String srcString, String destString)
			throws IOException {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				srcString));
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(destString));

		int by = 0;
		while ((by = bis.read()) != -1) {
			bos.write(by);

		}

		bos.close();
		bis.close();
	}

	// 基本字节流一次读写一个字节数组
	public static void method2(String srcString, String destString)
			throws IOException {
		FileInputStream fis = new FileInputStream(srcString);
		FileOutputStream fos = new FileOutputStream(destString);

		byte[] bys = new byte[1024];
		int len = 0;
		while ((len = fis.read(bys)) != -1) {
			fos.write(bys, 0, len);
		}

		fos.close();
		fis.close();
	}

	// 基本字节流一次读写一个字节
	public static void method1(String srcString, String destString)
			throws IOException {
		FileInputStream fis = new FileInputStream(srcString);
		FileOutputStream fos = new FileOutputStream(destString);

		int by = 0;
		while ((by = fis.read()) != -1) {
			fos.write(by);
		}

		fos.close();
		fis.close();
	}
}
```

## 3. 字符流

### 3.1 字符流

InputStream类和OutputStream类在读写文件时操作的都是字节，如果希望在程序中操作字符，使用这两个类就不太方便，为此JDK提供了字符流。同字节流一样，字符流也有两个抽象的顶级父类，分别是Reader和Writer。其中Reader是字符输入流，用于从某个源设备读取字符，Writer是字符输出流，用于向某个目标设备写入字符。Reader和Writer作为字符流的顶级父类，也有许多子类，接下来通过继承关系图来列出Reader和Writer的一些常用子类，如图所示。

![字符流](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180221.png?xxfjava)

#### 3.1.1 转换流出现的原因及思想
由于字节流操作中文不是特别方便，所以，java就提供了转换流。字符流=字节流+编码表。

编码表：由字符及其对应的数值组成的一张表

常见编码表

计算机只能识别二进制数据，早期由来是电信号。为了方便应用计算机，让它可以识别各个国家的文字。就将各个国家的文字用数字来表示，并一一对应，形成一张表。

字符串中的编码问题

编码：把看得懂的变成看不懂的
解码：把看不懂的变成看得懂

```java
package cn.xiaoxiaofeng;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
/*
 * String(byte[] bytes, String charsetName):通过指定的字符集解码字节数组
 * byte[] getBytes(String charsetName):使用指定的字符集合把字符串编码为字节数组
 * 
 * 编码:把看得懂的变成看不懂的
 * String -- byte[]
 * 
 * 解码:把看不懂的变成看得懂的
 * byte[] -- String
 * 
 * 举例：谍战片(发电报，接电报)
 * 
 * 码表：小本子
 * 		字符	数值
 * 
 * 要发送一段文字：
 * 		今天晚上在老地方见
 * 
 * 		发送端：今 -- 数值 -- 二进制 -- 发出去
 * 		接收端：接收 -- 二进制 -- 十进制 -- 数值 -- 字符 -- 今
 * 
 * 		今天晚上在老地方见
 * 
 * 编码问题简单，只要编码解码的格式是一致的。
 */
public class StringDemo {
	public static void main(String[] args) throws UnsupportedEncodingException {
		String s = "你好";

		// String -- byte[]
		byte[] bys = s.getBytes(); // [-60, -29, -70, -61]
		// byte[] bys = s.getBytes("GBK");// [-60, -29, -70, -61]
		// byte[] bys = s.getBytes("UTF-8");// [-28, -67, -96, -27, -91, -67]
		System.out.println(Arrays.toString(bys));

		// byte[] -- String
		String ss = new String(bys); // 你好
		// String ss = new String(bys, "GBK"); // 你好
		// String ss = new String(bys, "UTF-8"); // ???
		System.out.println(ss);
	}
}
```
#### 3.1.2 转换流概述

IO流可分为字节流和字符流，有时字节流和字符流之间也需要进行转换。在JDK中提供了两个类可以将字节流转换为字符流，它们分别是InputStreamReader和OutputStreamWriter。

OutputStreamWriter是Writer的子类，它可以将一个字节输出流转换成字符输出流，方便直接写入字符，而InputStreamReader是Reader的子类，它可以将一个字节输入流转换成字符输入流，方便直接读取字符。通过转换流进行数据读写的过程如图所示。

![字符流](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180230.png?xxfjava)

OutputStreamWriter写数据

| 方法                                 | 功能描述         |
| ---------------------------------- | ------------ |
| write(int c)                       | 写入一个字符       |
| write(char[] cbuf)                 | 写入一个字符数组     |
| write(char[] cbuf,int off,int len) | 写入一个字符数组的一部分 |
| write(String str)                  | 写入一个字符串      |
| write(String str,int off,int len)  | 写入一个字符串的一部分  |

字符流操作要注意的问题：

字符流数据没有直接进文件而是到缓冲区，所以要刷新缓冲区。

flush()和close()的区别：

- A：close()关闭流对象，但是先刷新一次缓冲区。关闭之后，流对象不可以继续再使用了。
- B：flush()仅仅刷新缓冲区,刷新之后，流对象还可以继续使用。

```java
package cn.xiaoxiaofeng;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
/*
 * OutputStreamWriter的方法：
 * public void write(int c):写一个字符
 * public void write(char[] cbuf):写一个字符数组
 * public void write(char[] cbuf,int off,int len):写一个字符数组的一部分
 * public void write(String str):写一个字符串
 * public void write(String str,int off,int len):写一个字符串的一部分
 */
public class OutputStreamWriterDemo {
	public static void main(String[] args) throws IOException {
		// 创建对象
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
				"osw2.txt"));

		// 写数据
		// public void write(int c):写一个字符
		// osw.write('a');
		// osw.write(97);
		// 为什么数据没有进去呢?
		// 原因是：字符 = 2字节
		// 文件中数据存储的基本单位是字节。
		// void flush()

		// public void write(char[] cbuf):写一个字符数组
		// char[] chs = {'a','b','c','d','e'};
		// osw.write(chs);

		// public void write(char[] cbuf,int off,int len):写一个字符数组的一部分
		// osw.write(chs,1,3);

		// public void write(String str):写一个字符串
		// osw.write("我爱林青霞");

		// public void write(String str,int off,int len):写一个字符串的一部分
		osw.write("我爱林青霞", 2, 3);

		// 刷新缓冲区
		osw.flush();
		// osw.write("我爱林青霞", 2, 3);

		// 释放资源
		osw.close();
		// java.io.IOException: Stream closed
		// osw.write("我爱林青霞", 2, 3);
	}
}
```
#### 3.1.3 InputStreamReader读数据
- public int read()：一次读一个字符 
- public int read(char[] cbuf)：一次读一个字符数组

```java
package cn.xiaoxiaofeng;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
/*
 * InputStreamReader的方法：
 * int read():一次读取一个字符
 * int read(char[] chs):一次读取一个字符数组
 */
public class InputStreamReaderDemo {
	public static void main(String[] args) throws IOException {
		// 创建对象
		InputStreamReader isr = new InputStreamReader(new FileInputStream(
				"StringDemo.java"));

		// 一次读取一个字符
		// int ch = 0;
		// while ((ch = isr.read()) != -1) {
		// System.out.print((char) ch);
		// }

		// 一次读取一个字符数组
		char[] chs = new char[1024];
		int len = 0;
		while ((len = isr.read(chs)) != -1) {
			System.out.print(new String(chs, 0, len));
		}

		// 释放资源
		isr.close();
	}
}
```
#### 3.1.4 字符流复制文本文件

```java
package cn.xiaoxiaofeng;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
/*
 * 需求：把当前项目目录下的a.txt内容复制到当前项目目录下的b.txt中
 * 
 * 数据源：
 * 		a.txt -- 读取数据 -- 字符转换流 -- InputStreamReader
 * 目的地：
 * 		b.txt -- 写出数据 -- 字符转换流 -- OutputStreamWriter
 */
public class CopyFileDemo {
	public static void main(String[] args) throws IOException {
		// 封装数据源
		InputStreamReader isr = new InputStreamReader(new FileInputStream(
				"a.txt"));
		// 封装目的地
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
				"b.txt"));

		// 读写数据
		// 方式1
		// int ch = 0;
		// while ((ch = isr.read()) != -1) {
		// osw.write(ch);
		// }

		// 方式2
		char[] chs = new char[1024];
		int len = 0;
		while ((len = isr.read(chs)) != -1) {
			osw.write(chs, 0, len);
			// osw.flush();
		}

		// 释放资源
		osw.close();
		isr.close();
	}
}
```
#### 3.1.5 转换流的简化写法
转换流的名字比较长，而我们常见的操作都是按照本地默认编码实现的，所以，为了简化我们的书写，转换流提供了对应的子类。

FileWriter

代码示例：把当前项目目录下的a.txt内容复制到当前项目目录下的b.txt中

```java
package cn.xiaoxiaofeng;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/*
 * 由于我们常见的操作都是使用本地默认编码，所以，不用指定编码。
 * 而转换流的名称有点长，所以，Java就提供了其子类供我们使用。
 * OutputStreamWriter = FileOutputStream + 编码表(GBK)
 * FileWriter = FileOutputStream + 编码表(GBK)
 * 
 * InputStreamReader = FileInputStream + 编码表(GBK)
 * FileReader = FileInputStream + 编码表(GBK)
 * 
 /*
 * 需求：把当前项目目录下的a.txt内容复制到当前项目目录下的b.txt中
 * 
 * 数据源：
 * 		a.txt -- 读取数据 -- 字符转换流 -- InputStreamReader -- FileReader
 * 目的地：
 * 		b.txt -- 写出数据 -- 字符转换流 -- OutputStreamWriter -- FileWriter
 */
public class CopyFileDemo2 {
	public static void main(String[] args) throws IOException {
		// 封装数据源
		FileReader fr = new FileReader("a.txt");
		// 封装目的地
		FileWriter fw = new FileWriter("b.txt");

		// 一次一个字符
		// int ch = 0;
		// while ((ch = fr.read()) != -1) {
		// fw.write(ch);
		// }

		// 一次一个字符数组
		char[] chs = new char[1024];
		int len = 0;
		while ((len = fr.read(chs)) != -1) {
			fw.write(chs, 0, len);
			fw.flush();
		}

		// 释放资源
		fw.close();
		fr.close();
	}
}
```
#### 3.1.6 FileReader

代码示例：把c:\\a.txt内容复制到d:\\b.txt中

```java
package cn.xiaoxiaofeng;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/*
 * 需求：把c:\\a.txt内容复制到d:\\b.txt中
 * 
 * 数据源：
 * 		c:\\a.txt -- FileReader
 * 目的地:
 * 		d:\\b.txt -- FileWriter
 */
public class CopyFileDemo3 {
	public static void main(String[] args) throws IOException {
		// 封装数据源
		FileReader fr = new FileReader("c:\\a.txt");
		// 封装目的地
		FileWriter fw = new FileWriter("d:\\b.txt");

		// 读写数据
		// int ch = 0;
		int ch;
		while ((ch = fr.read()) != -1) {
			fw.write(ch);
		}
		
		//释放资源
		fw.close();
		fr.close();
	}
}
```
### 3.2 字符缓冲流
字符流为了高效读写，也提供了对应的字符缓冲流。BufferedWriter：字符缓冲输出流，BufferedReader：字符缓冲输入流。

#### 3.2.1 BufferedWriter基本用法
将文本写入字符输出流，缓冲各个字符，从而提供单个字符、数组和字符串的高效写入。 可以指定缓冲区的大小，或者接受默认的大小。在大多数情况下，默认值就足够大了。

代码示例：BufferedWriter基本用法

```java
package cn.xiaoxiaofeng;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
/**
 * 字符流为了高效读写，也提供了对应的字符缓冲流。
 * BufferedWriter:字符缓冲输出流
 * BufferedReader:字符缓冲输入流
 * 
 * BufferedWriter:字符缓冲输出流
 * 将文本写入字符输出流，缓冲各个字符，从而提供单个字符、数组和字符串的高效写入。 
 * 可以指定缓冲区的大小，或者接受默认的大小。在大多数情况下，默认值就足够大了。 
 */
public class BufferedWriterDemo {
	public static void main(String[] args) throws IOException {
		// BufferedWriter(Writer out)
		// BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
		// new FileOutputStream("bw.txt")));

		BufferedWriter bw = new BufferedWriter(new FileWriter("bw.txt"));

		bw.write("hello");
		bw.write("world");
		bw.write("java");
		bw.flush();

		bw.close();
	}
}
```
#### 3.2.2 BufferedReader基本用法
从字符输入流中读取文本，缓冲各个字符，从而实现字符、数组和行的高效读取。  可以指定缓冲区的大小，或者可使用默认的大小。大多数情况下，默认值就足够大了。 

代码示例：  BufferedReader基本用法

```java
package cn.xiaoxiaofeng;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 * BufferedReader
 * 从字符输入流中读取文本，缓冲各个字符，从而实现字符、数组和行的高效读取。 
 * 可以指定缓冲区的大小，或者可使用默认的大小。大多数情况下，默认值就足够大了。 
 * 
 * BufferedReader(Reader in)
 */
public class BufferedReaderDemo {
	public static void main(String[] args) throws IOException {
		// 创建字符缓冲输入流对象
		BufferedReader br = new BufferedReader(new FileReader("bw.txt"));

		// 方式1
		// int ch = 0;
		// while ((ch = br.read()) != -1) {
		// System.out.print((char) ch);
		// }

		// 方式2
		char[] chs = new char[1024];
		int len = 0;
		while ((len = br.read(chs)) != -1) {
			System.out.print(new String(chs, 0, len));
		}

		// 释放资源
		br.close();
	}
}
```
#### 3.2.3 特殊功能

- BufferedWriter，newLine()：根据系统来决定换行符
- BufferedReader，String readLine()：一次读取一行数据

代码示例：字符缓冲流的特殊方法

```java
package cn.xiaoxiaofeng;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/*
 * 字符缓冲流的特殊方法：
 * BufferedWriter:
 * 		public void newLine():根据系统来决定换行符
 * BufferedReader:
 * 		public String readLine()：一次读取一行数据
 * 		包含该行内容的字符串，不包含任何行终止符，如果已到达流末尾，则返回 null
 */
public class BufferedDemo {
	public static void main(String[] args) throws IOException {
		// write();
		read();
	}

	private static void read() throws IOException {
		// 创建字符缓冲输入流对象
		BufferedReader br = new BufferedReader(new FileReader("bw2.txt"));

		// public String readLine()：一次读取一行数据
		// String line = br.readLine();
		// System.out.println(line);
		// line = br.readLine();
		// System.out.println(line);

		// 最终版代码
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		
		//释放资源
		br.close();
	}

	private static void write() throws IOException {
		// 创建字符缓冲输出流对象
		BufferedWriter bw = new BufferedWriter(new FileWriter("bw2.txt"));
		for (int x = 0; x < 10; x++) {
			bw.write("hello" + x);
			// bw.write("\r\n");
			bw.newLine();
			bw.flush();
		}
		bw.close();
	}

}
```
代码示例：字符缓冲流复制文本文件

```java
package cn.xiaoxiaofeng;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/*
 * 需求：把当前项目目录下的a.txt内容复制到当前项目目录下的b.txt中
 * 
 * 数据源：
 * 		a.txt -- 读取数据 -- 字符转换流 -- InputStreamReader -- FileReader -- BufferedReader
 * 目的地：
 * 		b.txt -- 写出数据 -- 字符转换流 -- OutputStreamWriter -- FileWriter -- BufferedWriter
 */
public class CopyFileDemo2 {
	public static void main(String[] args) throws IOException {
		// 封装数据源
		BufferedReader br = new BufferedReader(new FileReader("a.txt"));
		// 封装目的地
		BufferedWriter bw = new BufferedWriter(new FileWriter("b.txt"));

		// 读写数据
		String line = null;
		while ((line = br.readLine()) != null) {
			bw.write(line);
			bw.newLine();
			bw.flush();
		}

		// 释放资源
		bw.close();
		br.close();
	}
}
```

### 3.3 模拟记事本

```java
package cn.itcast.chapter07.task02;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
/**
 * 模拟记事本程序
 */
public class Notepad {
	private static String filePath;
	private static String message = "";
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("--1:新建文件 2:打开文件  3:修改文件  4:保存 5:退出--");
		while (true) {
			System.out.print("请输入操作指令：");
			int command = sc.nextInt();
			switch (command) {
			case 1:
				createFile();// 1:新建文件
				break;
			case 2:
				openFile();// 2:打开文件
				break;
			case 3:
				editFile();// 3:修改文件
				break;
			case 4:
				saveFile();// 4:保存
				break;
			case 5:
				exit();// 5:退出
				break;
			default:
				System.out.println("您输入的指令错误！");
				break;
			}
		}
	}
	/**
	 * 新建文件 从控制台获取内容
	 */
	private static void createFile() {
		message = "";// 新建文件时，暂存文件内容清空
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入内容，停止编写请输入\"stop\":");// 提示
		StringBuffer stb = new StringBuffer();// 用于后期输入内容的拼接
		String inputMessage = "";
		while (!inputMessage.equals("stop")) {// 当输入“stop”时，停止输入
			if (stb.length() > 0) {
				stb.append("\r\n");// 追加换行符
			}
			stb.append(inputMessage);// 拼接输入信息
			inputMessage = sc.nextLine();// 获取输入信息
		}
		message = stb.toString();// 将输入内容暂存
	}
	/**
	 * 打开文件
	 */
	private static void openFile() throws Exception {
		message = "";// 打开文件时，将暂存内容清空
		Scanner sc = new Scanner(System.in);
		System.out.print("请输入打开文件的位置：");
		filePath = sc.next();// 获取打开文件的路径
		// 控制只能输入txt格式的文件路径
		if (filePath != null && !filePath.endsWith(".txt")) {
			System.out.print("请选择文本文件！");
			return;
		}
		FileReader in = new FileReader(filePath);// 实例化一个FileReader对象
		char[] charArray = new char[1024];// 缓冲数组
		int len = 0;
		StringBuffer sb = new StringBuffer();
		// 循环读取，一次读取一个字符数组
		while ((len = in.read(charArray)) != -1) {
			sb.append(charArray);
		}
		message = sb.toString();// 将打开文件内容暂存
		System.out.println("打开文件内容：" + "\r\n" + message);
		in.close();// 释放资源
	}
	/**
	 * 修改文件内容 通过字符串替换的形式
	 */
	private static void editFile() {
		if (message == "" && filePath == null) {
			System.out.println("请先新建文件或者打开文件");
			return;
		}
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入要修改的内容（以 \"修改的目标文字:修改之后的文字\" 格式）,"
				+ "停止修改请输入\"stop\":");
		String inputMessage = "";
		while (!inputMessage.equals("stop")) {// 当输入stop时,停止修改
			inputMessage = sc.nextLine();
			if (inputMessage != null && inputMessage.length() > 0) {
				// 将输入的文字根据“：”拆分成数组
				String[] editMessage = inputMessage.split(":");
				if (editMessage != null && editMessage.length > 1) {
					// 根据输入的信息将文件中内容替换
					message = message.replace(editMessage[0], editMessage[1]);
				}
			}
		}
		System.out.println("修改后的内容:" + "\r\n" + message);
	}
	/**
	 * 保存 新建文件存在用户输入的路径 打开的文件将原文件覆盖
	 */
	private static void saveFile() throws IOException {
		Scanner sc = new Scanner(System.in);
		FileWriter out = null;
		if (filePath != null) {// 文件是由“打开”载入的
			out = new FileWriter(filePath);// 将原文件覆盖
		} else {// 新建的文件
			System.out.print("请输入文件保存的绝对路径：");
			String path = sc.next();// 获取文件保存的路径
			filePath = path;
			// 将输入路径中大写字母替换成小写字母后判断是不是文本格式
			if (!filePath.toLowerCase().endsWith(".txt")) {
				filePath += ".txt";
			}
			out = new FileWriter(filePath);// 构造输出流
		}
		out.write(message);// 写入暂存的内容
		out.close();// 关闭输出流
		message = "";// 修改文件前现将写入内容置空
		filePath = null;// 将文件路径至null
	}
	/**
	 * 退出
	 */
	private static void exit() {
		System.out.println("您已退出系统，谢谢使用！");
		System.exit(0);
	}
}
```
## 4. File类

### 4.1 File类概述

File类用于封装一个路径，这个路径可以是从系统盘符开始的绝对路径，如：“D:\file\a.txt”，也可以是相对于当前目录而言的相对路径，如：“src\Hello.java”。File类内部封装的路径可以指向一个文件，也可以指向一个目录，在File类中提供了针对这些文件或目录的一些常规操作。

文件和目录路径名的抽象表示形式，表示一个文件或文件夹，并提供了一系列操作文件或文件夹的方法

File类中提供了一系列方法，用于操作其内部封装的路径指向的文件或者目录，例如判断文件/目录是否存在、创建、删除文件/目录等。

![1500708828607](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180113.png?xxfjava)

### 4.2 构造方法

| 方法                               | 功能描述                 |
| :------------------------------- | :------------------- |
| File(String pathname)            | 根据路径得到File对象         |
| File(String parent,String child) | 根据目录和子文件/目录得到对象      |
| File(File parent,String child)   | 根据父File对象和子文件/目录得到对象 |

示例代码

```java
package cn.xiaoxiaofeng;
import java.io.File;
/*
 * 我们要想实现IO的操作，就必须知道硬盘上文件的表现形式。
 * 而Java就提供了一个类File供我们使用。
 * 
 * File:文件和目录(文件夹)路径名的抽象表示形式
 * 构造方法：
 * 		File(String pathname)：根据一个路径得到File对象
 * 		File(String parent, String child):根据一个目录和一个子文件/目录得到File对象
 * 		File(File parent, String child):根据一个父File对象和一个子文件/目录得到File对象
 */
public class FileDemo {
	public static void main(String[] args) {
		// File(String pathname)：根据一个路径得到File对象
		// 把e:\\demo\\a.txt封装成一个File对象
		File file = new File("E:\\demo\\a.txt");

		// File(String parent, String child):根据一个目录和一个子文件/目录得到File对象
		File file2 = new File("E:\\demo", "a.txt");

		// File(File parent, String child):根据一个父File对象和一个子文件/目录得到File对象
		File file3 = new File("e:\\demo");
		File file4 = new File(file3, "a.txt");

		// 以上三种方式其实效果一样
	}
}
```

### 4.3 创建功能

| 返回值     | 方法               | 功能描述                     |
| :------ | :--------------- | :----------------------- |
| boolean | createNewFile()  | 创建文件                     |
| File    | createTempFile() | 创建一个用于缓存的临时文件            |
| boolean | mkdir()          | 创建文件夹                    |
| boolean | mkdirs()         | 创建多级文件夹，如果父级文件夹不存在，会自动创建 |

代码示例

```java
package cn.xiaoxiaofeng;
import java.io.File;
import java.io.IOException;
/*
 *创建功能：
 *public boolean createNewFile():创建文件 如果存在这样的文件，就不创建了
 *public boolean mkdir():创建文件夹 如果存在这样的文件夹，就不创建了
 *public boolean mkdirs():创建文件夹,如果父文件夹不存在，会帮你创建出来
 */
public class FileDemo {
	public static void main(String[] args) throws IOException {
		// 需求：我要在e盘目录下创建一个文件夹demo
		File file = new File("e:\\demo");
		System.out.println("mkdir:" + file.mkdir());

		// 需求:我要在e盘目录demo下创建一个文件a.txt
		File file2 = new File("e:\\demo\\a.txt");
		System.out.println("createNewFile:" + file2.createNewFile());

		// 需求：我要在e盘目录test下创建一个文件b.txt
		// Exception in thread "main" java.io.IOException: 系统找不到指定的路径。
		// 注意：要想在某个目录下创建内容，该目录首先必须存在。
		// File file3 = new File("e:\\test\\b.txt");
		// System.out.println("createNewFile:" + file3.createNewFile());

		// 需求:我要在e盘目录test下创建aaa目录
		// File file4 = new File("e:\\test\\aaa");
		// System.out.println("mkdir:" + file4.mkdir());

		// File file5 = new File("e:\\test");
		// File file6 = new File("e:\\test\\aaa");
		// System.out.println("mkdir:" + file5.mkdir());
		// System.out.println("mkdir:" + file6.mkdir());

		// 其实我们有更简单的方法
		File file7 = new File("e:\\aaa\\bbb\\ccc\\ddd");
		System.out.println("mkdirs:" + file7.mkdirs());

		// 看下面的这个东西：
		File file8 = new File("e:\\liuyi\\a.txt");
		System.out.println("mkdirs:" + file8.mkdirs());
	}
}
```
### 4.4 删除功能

| 返回值     | 方法             | 功能描述                   |
| :------ | :------------- | :--------------------- |
| boolean | delete()       | 删除文件或文件夹               |
| void    | deleteOnExit() | JVM退出时删除File对象对应的文件和目录 |

代码示例

```java
package cn.xiaoxiaofeng;
import java.io.File;
import java.io.IOException;
/*
 * 删除功能:public boolean delete()
 * 
 * 注意：
 * 		A:如果你创建文件或者文件夹忘了写盘符路径，那么，默认在项目路径下。
 * 		B:Java中的删除不走回收站。
 * 		C:要删除一个文件夹，请注意该文件夹内不能包含文件或者文件夹
 */
public class FileDemo {
	public static void main(String[] args) throws IOException {
		// 创建文件
		// File file = new File("e:\\a.txt");
		// System.out.println("createNewFile:" + file.createNewFile());

		// 我不小心写成这个样子了
		File file = new File("a.txt");
		System.out.println("createNewFile:" + file.createNewFile());

		// 继续玩几个
		File file2 = new File("aaa\\bbb\\ccc");
		System.out.println("mkdirs:" + file2.mkdirs());

		// 删除功能：我要删除a.txt这个文件
		File file3 = new File("a.txt");
		System.out.println("delete:" + file3.delete());

		// 删除功能：我要删除ccc这个文件夹
		File file4 = new File("aaa\\bbb\\ccc");
		System.out.println("delete:" + file4.delete());

		// 删除功能：我要删除aaa文件夹
		// File file5 = new File("aaa");
		// System.out.println("delete:" + file5.delete());

		File file6 = new File("aaa\\bbb");
		File file7 = new File("aaa");
		System.out.println("delete:" + file6.delete());
		System.out.println("delete:" + file7.delete());
	}
}
```

### 4.5 重命名功能

| 方法                   | 功能描述                  |
| :------------------- | :-------------------- |
| renamneTo(File dest) | 路径名相同就是重命名，不一样就是改名加剪切 |

代码示例

```java
package cn.xiaoxiaofeng;
import java.io.File;
/*
 * 重命名功能:public boolean renameTo(File dest)
 * 		如果路径名相同，就是改名。
 * 		如果路径名不同，就是改名并剪切。
 * 
 * 路径以盘符开始：绝对路径	c:\\a.txt
 * 路径不以盘符开始：相对路径	a.txt
 */
public class FileDemo {
	public static void main(String[] args) {
		// 创建一个文件对象
		// File file = new File("林青霞.jpg");
		// // 需求：我要修改这个文件的名称为"东方不败.jpg"
		// File newFile = new File("东方不败.jpg");
		// System.out.println("renameTo:" + file.renameTo(newFile));

		File file2 = new File("东方不败.jpg");
		File newFile2 = new File("e:\\林青霞.jpg");
		System.out.println("renameTo:" + file2.renameTo(newFile2));
	}
}
```
### 4.6 判断功能

| 方法            | 功能描述    |
| :------------ | :------ |
| isDirectory() | 判断是否是目录 |
| isFile()      | 判断是否是文件 |
| exists()      | 判断是否是存在 |
| canRead()     | 判断是否是可读 |
| canWrite()    | 判断是否是可写 |
| isHidden()    | 判断是否是隐藏 |
| isAbsolute()  | 是否是绝对路径 |

示例代码

```java
import java.io.File;  
  
/* 
 * 判断功能: 
 * public boolean isDirectory():判断是否是目录 
 * public boolean isFile():判断是否是文件 
 * public boolean exists():判断是否存在 
 * public boolean canRead():判断是否可读 
 * public boolean canWrite():判断是否可写 
 * public boolean isHidden():判断是否隐藏 
 */  
public class FileDemo {  
    public static void main(String[] args) {  
        // 创建文件对象  
        File file = new File("a.txt");  
  
        System.out.println("isDirectory:" + file.isDirectory());// false  
        System.out.println("isFile:" + file.isFile());// true  
        System.out.println("exists:" + file.exists());// true  
        System.out.println("canRead:" + file.canRead());// true  
        System.out.println("canWrite:" + file.canWrite());// true  
        System.out.println("isHidden:" + file.isHidden());// false  
    }  
}
```
### 4.7 获取功能

| 返回值      | 方法                | 功能描述                    |
| :------- | :---------------- | :---------------------- |
| String   | getAbsolutePath() | 获取绝对路径                  |
| String   | getPath()         | 获取相对路径                  |
| String   | getParent()       | 获取父目录                   |
| String   | getName()         | 获取名称                    |
| long     | getFreeSpace()    | 获取剩余可用空间                |
| long     | getTotalSpace()   | 获取总大小                   |
| long     | length()          | 获取长度，字节数                |
| long     | lastModified()    | 获取最后一次修改时间，毫秒值          |
| String[] | list()            | 获取指定目录下的所有文件或文件夹的名称数组   |
| File[]   | listFiles()       | 获取指定目录下的所有文件或文件夹的File数组 |

代码示例

```java
package cn.xiaoxiaofeng;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
/*
 * 获取功能：
 * public String getAbsolutePath()：获取绝对路径
 * public String getPath():获取相对路径
 * public String getName():获取名称
 * public long length():获取长度。字节数
 * public long lastModified():获取最后一次的修改时间，毫秒值
 */
public class FileDemo {
	public static void main(String[] args) {
		// 创建文件对象
		File file = new File("demo\\test.txt");

		System.out.println("getAbsolutePath:" + file.getAbsolutePath());
		System.out.println("getPath:" + file.getPath());
		System.out.println("getName:" + file.getName());
		System.out.println("length:" + file.length());
		System.out.println("lastModified:" + file.lastModified());

		// 1416471971031
		Date d = new Date(1416471971031L);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = sdf.format(d);
		System.out.println(s);
	}
}
```
运行结果：

```
getAbsolutePath:D:\workspace\Test\demo\test.txt
getPath:demo\test.txt
getName:test.txt
length:0
lastModified:0
2014-11-20 16:26:11
```

判断缓存文件是否过期

```java
File file = ...;
long time = System.currentTimeMillis() - file.lastModified();
if (time < cachetime){
    // 缓存时间小于指定的时间，缓存有效，否则缓存过期
}else {
    
}
```

### 4.8 高级获取功能

| 返回值      | 方法                               | 功能描述         |
| :------- | :------------------------------- | :----------- |
| String[] | list(FilenameFilter filter)      | 返回满足条件的文件名数组 |
| File[]   | listFiles(FilenameFilter filter) | 返回满足条件的文件数组  |
| File[]   | listRoots()                      | 列出系统所有的根路径   |

```java
package cn.xiaoxiaofeng;
import java.io.File;
/*
 * 高级获取功能：
 * public String[] list():获取指定目录下的所有文件或者文件夹的名称数组
 * public File[] listFiles():获取指定目录下的所有文件或者文件夹的File数组
 */
public class FileDemo {
	public static void main(String[] args) {
		// 指定一个目录
		File file = new File("e:\\");

		// public String[] list():获取指定目录下的所有文件或者文件夹的名称数组
		String[] strArray = file.list();
		for (String s : strArray) {
			System.out.println(s);
		}
		System.out.println("------------");

		// public File[] listFiles():获取指定目录下的所有文件或者文件夹的File数组
		File[] fileArray = file.listFiles();
		for (File f : fileArray) {
			System.out.println(f.getName());
		}
	}
}
```
### 4.9 文件过滤器

- list(FilenameFilter filter)
- listFiles(FilenameFilter filter)

FilenameFilter 接口

- accept(File dir, String name)

### 4.10 File练习

文件名称过滤器的实现思想及代码

- public String[] list(FilenameFilter filter)
- public File[] listFiles(FilenameFilter filter)

#### 4.10.1 文件名称过滤器的实现

```java
package cn.xiaoxiaofeng;
import java.io.File;
import java.io.FilenameFilter;
/*
 * 判断E盘目录下是否有后缀名为.jpg的文件，如果有，就输出此文件名称
 * A:先获取所有的，然后遍历的时候，依次判断，如果满足条件就输出。
 * B:获取的时候就已经是满足条件的了，然后输出即可。
 * 
 * 要想实现这个效果，就必须学习一个接口：文件名称过滤器
 * public String[] list(FilenameFilter filter)
 * public File[] listFiles(FilenameFilter filter)
 */
public class FileDemo2 {
	public static void main(String[] args) {
		// 封装e判断目录
		File file = new File("e:\\");

		// 获取该目录下所有文件或者文件夹的String数组
		// public String[] list(FilenameFilter filter)
		String[] strArray = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// return false;
				// return true;
				// 通过这个测试，我们就知道了，到底把这个文件或者文件夹的名称加不加到数组中，取决于这里的返回值是true还是false
				// 所以，这个的true或者false应该是我们通过某种判断得到的
				// System.out.println(dir + "---" + name);
				// File file = new File(dir, name);
				// // System.out.println(file);
				// boolean flag = file.isFile();
				// boolean flag2 = name.endsWith(".jpg");
				// return flag && flag2;
				return new File(dir, name).isFile() && name.endsWith(".jpg");
			}
		});

		// 遍历
		for (String s : strArray) {
			System.out.println(s);
		}
	}
}
```
#### 4.10.2 递归遍历目录下指定后缀名结尾的文件名称

```java
package cn.xiaoxiaofeng;
import java.io.File;
/*
 * 需求：把E:\JavaSE目录下所有的java结尾的文件的绝对路径给输出在控制台。
 * 
 * 分析：
 * 		A:封装目录
 * 		B:获取该目录下所有的文件或者文件夹的File数组
 * 		C:遍历该File数组，得到每一个File对象
 * 		D:判断该File对象是否是文件夹
 * 			是：回到B
 * 			否：继续判断是否以.java结尾
 * 				是：就输出该文件的绝对路径
 * 				否：不搭理它
 */
public class FilePathDemo {
	public static void main(String[] args) {
		// 封装目录
		File srcFolder = new File("E:\\JavaSE");

		// 递归功能实现
		getAllJavaFilePaths(srcFolder);
	}

	private static void getAllJavaFilePaths(File srcFolder) {
		// 获取该目录下所有的文件或者文件夹的File数组
		File[] fileArray = srcFolder.listFiles();

		// 遍历该File数组，得到每一个File对象
		for (File file : fileArray) {
			// 判断该File对象是否是文件夹
			if (file.isDirectory()) {
				getAllJavaFilePaths(file);
			} else {
				// 继续判断是否以.java结尾
				if (file.getName().endsWith(".java")) {
					// 就输出该文件的绝对路径
					System.out.println(file.getAbsolutePath());
				}
			}
		}
	}
}
```
#### 4.10.3 递归删除带内容的目录

```java
package cn.xiaoxiaofeng;
import java.io.File;
/*
 * 需求：递归删除带内容的目录
 * 
 * 分析：
 * 		A:封装目录
 * 		B:获取该目录下的所有文件或者文件夹的File数组
 * 		C:遍历该File数组，得到每一个File对象
 * 		D:判断该File对象是否是文件夹
 * 			是：回到B
 * 			否：就删除
 */
public class FileDeleteDemo {
	public static void main(String[] args) {
		// 封装目录
		File srcFolder = new File("demo");
		// 递归实现
		deleteFolder(srcFolder);
	}

	private static void deleteFolder(File srcFolder) {
		// 获取该目录下的所有文件或者文件夹的File数组
		File[] fileArray = srcFolder.listFiles();

		if (fileArray != null) {
			// 遍历该File数组，得到每一个File对象
			for (File file : fileArray) {
				// 判断该File对象是否是文件夹
				if (file.isDirectory()) {
					deleteFolder(file);
				} else {
					System.out.println(file.getName() + "---" + file.delete());
				}
			}

			System.out.println(srcFolder.getName() + "---" + srcFolder.delete());
		}
	}
}
```

#### 4.10.4 模拟文件管理器

DocumentManager

```java
package cn.itcast.chapter07.task03;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class DocumentManager {
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("--1:指定关键字检索文件  2:指定后缀名检索文件  " + "3:复制文件/目录  4:退出--");
		while (true) {
			System.out.print("请输入指令：");
			int command = sc.nextInt();
			switch (command) {
			case 1:
				searchByKeyWorld();// 指定关键字检索文件
				break;
			case 2:
				searchBySuffix();// 指定后缀名检索文件
				break;
			case 3:
				copyDirectory();// 复制文件/目录
				break;
			case 4:
				exit();// 退出
				break;
			default:
				System.out.println("您输入的指令错误！");
				break;
			}
		}
	}

	// *********1.指定关键字检索文件*********
	private static void searchByKeyWorld() {
		Scanner sc = new Scanner(System.in);
		System.out.print("请输入要检索的目录位置：");
		String path = sc.next();// 从控制台获取路径
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {// 判断目录是否存在，是否是目录
			System.out.println(path + " (不是有效目录)");
			return;
		}
		System.out.print("请输入搜索关键字：");
		String key = sc.next();// 获取关键字
		// 在输入目录下获取所有包含关键字的文件路径
		ArrayList<String> list = FileUtils.listFiles(file, key);
		for (Object obj : list) {
			System.out.println(obj);// 将路径打印到控制台
		}
	}

	// *********2.指定后缀名检索文件********//
	private static void searchBySuffix() {
		Scanner sc = new Scanner(System.in);
		System.out.print("请输入要检索的目录位置：");
		String path = sc.next();// 从控制台获取路径
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {// 判断目录是否存在，是否是目录
			System.out.println(path + " (不是有效目录)");
			return;
		}
		System.out.print("请输入搜索后缀：");
		String suffix = sc.next();
		String[] suffixArray = suffix.split(",");// 获取后缀字符串
		// 在输入目录下获取所有指定后缀名的文件路径
		ArrayList<String> list = FileUtils.listFiles(file, suffixArray);
		for (Object obj : list) {
			System.out.println(obj);// 将路径打印到控制台
		}
	}

	// *********3.复制文件/目录**********//
	private static void copyDirectory() throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.print("请输入源目录：");
		String srcDirectory = sc.next();// 从控制台获取源路径
		File srcFile = new File(srcDirectory);
		if (!srcFile.exists() || !srcFile.isDirectory()) {// 判断目录是否存在，是否是目录
			System.out.println("无效目录！");
			return;
		}
		System.out.print("请输入目标位置：");
		String destDirectory = sc.next();// 从控制台获取目标路径
		File destFile = new File(destDirectory);
		if (!destFile.exists() || !destFile.isDirectory()) {// 判断目录是否存在，是否是目录
			System.out.println("无效位置！");
			return;
		}
		// 将源路径中的内容复制到目标路径下
		FileUtils.copySrcPathToDestPath(srcFile, destFile);
	}

	// *********4.退出**********//
	private static void exit() {
		System.out.println("您已退出系统，谢谢使用！");
		System.exit(0);
	}
}
```

FileUtils

```java
package cn.itcast.chapter07.task03;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class FileUtils {
	/**
	 * 指定关键字检索文件
	 * @param file   File对象
	 * @param key    关键字
	 * @return 包含关键字的文件路径
	 */
	public static ArrayList<String> listFiles(File file, final String key) {
		FilenameFilter filter = new FilenameFilter() { // 创建过滤器对象
			public boolean accept(File dir, String name) {// 实现accept()方法
				File currFile = new File(dir, name);
				// 如果文件名包含关键字返回true，否则返回false
				if (currFile.isFile() && name.contains(key)) {
					return true;
				}
				return false;
			}
		};
		// 递归方式获取规定的路径
		ArrayList<String> arraylist = fileDir(file, filter);
		return arraylist;
	}

	/**
	 * 指定后缀名检索文件
	 * @param file   File对象
	 * @param suffixArray   后缀名数组
	 * @return 指定后缀名的文件路径
	 */
	public static ArrayList<String> listFiles(File file,
			final String[] suffixArray) {
		FilenameFilter filter = new FilenameFilter() { // 创建过滤器对象
			public boolean accept(File dir, String name) {// 实现accept()方法
				File currFile = new File(dir, name);
				if (currFile.isFile()) {// 如果文件名以指定后缀名结尾返回true，否则返回false
					for (String suffix : suffixArray) {
						if (name.endsWith("." + suffix)) {
							return true;
						}
					}
				}
				return false;
			}
		};
		// 递归方式获取规定的路径
		ArrayList<String> arraylist = fileDir(file, filter);
		return arraylist;
	}

	/**
	 * 递归方式获取规定的路径
	 * @param dir   File对象
	 * @param filter   过滤器
	 * @return 过滤器过滤后的文件路径
	 */
	public static ArrayList<String> fileDir(File dir, FilenameFilter filter) {
		ArrayList<String> arraylist = new ArrayList<String>();
		File[] lists = dir.listFiles(filter); // 获得过滤后的所有文件数组
		for (File list : lists) {
			// 将文件的绝对路径放到集合中
			arraylist.add(list.getAbsolutePath());
		}
		File[] files = dir.listFiles(); // 获得当前目录下所有文件的数组
		for (File file : files) { // 遍历所有的子目录和文件
			if (file.isDirectory()) {
				// 如果是目录，递归调用fileDir()
				ArrayList<String> every = fileDir(file, filter);
				arraylist.addAll(every);// 将文件夹下的文件路径添加到集合中
			}
		}// 此时的集合中有当前目录下的文件路径，和当前目录的子目录下的文件路径
		return arraylist;
	}

	/**
	 * 复制文件/目录
	 * @param srcFile   源目录
	 * @param destFile  目标目录
	 */
	public static void copySrcPathToDestPath(File srcDir, File destDir)
			throws Exception {
		File[] files = srcDir.listFiles();// 子文件目录
		for (int i = 0; i < files.length; i++) {
			File copiedFile = new File(destDir, files[i].getName());// 创建指定目录的文件
			if (files[i].isDirectory()) {// 如果是目录
				if (!copiedFile.mkdirs()) {// 创建文件夹
					System.out.println("无法创建：" + copiedFile);
					return;
				}
				// 调用递归，获取子文件夹下的文件路径
				copySrcPathToDestPath(files[i], copiedFile);
			} else {// 复制文件
				FileInputStream input = new FileInputStream(files[i]);// 获取输入流
				FileOutputStream output = new FileOutputStream(copiedFile);// 获取输出流
				byte[] buffer = new byte[1024];// 创建缓冲区
				int n = 0;
				// 循环读取字节
				while ((n = input.read(buffer)) != -1) {
					output.write(buffer, 0, n);
				}
				input.close();// 关闭输入流
				output.close();// 关闭输出流
			}
		}
	}
}
```

## 5. NIO

Java NIO（New IO）是从Java 1.4版本开始引入的一个新的IO API，可以替代标准的Java IO API。本系列教程将有助于你学习和理解Java NIO。

Java NIO提供了与标准IO不同的IO工作方式： 

- Channels and Buffers（通道和缓冲区）：标准的IO基于字节流和字符流进行操作的，而NIO是基于通道（Channel）和缓冲区（Buffer）进行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。
- Asynchronous IO（异步IO）：Java NIO可以让你异步的使用IO，例如：当线程从通道读取数据到缓冲区时，线程还是可以进行其他事情。当数据被写入到缓冲区时，线程可以继续处理它。从缓冲区写入通道也类似。
- Selectors（选择器）：Java NIO引入了选择器的概念，选择器用于监听多个通道的事件（比如：连接打开，数据到达）。因此，单个的线程可以监听多个数据通道。

下面就来详细介绍Java NIO的相关知识。 

### 5.1 Java NIO 概述

Java NIO 由以下几个核心部分组成： 

- Channels 通道
- Buffers 缓冲区
- Selectors 选择器

虽然Java NIO 中除此之外还有很多类和组件，但在我看来，Channel，Buffer 和 Selector 构成了核心的API。其它组件，如Pipe和FileLock，只不过是与三个核心组件共同使用的工具类。因此，在概述中我将集中在这三个组件上。其它组件会在单独的章节中讲到。 

#### 5.1.1 Channel 和 Buffer 

基本上，所有的 IO 在NIO 中都从一个Channel 开始。Channel 有点象流。 数据可以从Channel读到Buffer中，也可以从Buffer 写到Channel中。这里有个图示： 

![channel和buffer](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180416.png?xxfjava)


Channel和Buffer有好几种类型。下面是JAVA NIO中的一些主要Channel的实现： 

- FileChannel 文件通道
- DatagramChannel 用于UDP通信
- SocketChannel 用于TCP通信（客户端）
- ServerSocketChannel 用于TCP通信（服务端）
- Pipe.SinkChannel 用于线程间通信的管道
- Pipe.SourceChannel 用于线程间通信的管道

正如你所看到的，这些通道涵盖了UDP 和 TCP 网络IO，以及文件IO。 

与这些类一起的有一些有趣的接口，但为简单起见，我尽量在概述中不提到它们。本教程其它章节与它们相关的地方我会进行解释。 

以下是Java NIO里关键的Buffer实现： 

- ByteBuffer
- CharBuffer
- DoubleBuffer
- FloatBuffer
- IntBuffer
- LongBuffer
- ShortBuffer

这些Buffer覆盖了你能通过IO发送的基本数据类型：byte，short，int，long，float，double 和 char 

Java NIO 还有个 MappedByteBuffer，用于表示内存映射文件， 我也不打算在概述中说明。 

#### 5.1.2 Selector 

Selector允许单线程处理多个 Channel。如果你的应用打开了多个连接（通道），但每个连接的流量都很低，使用Selector就会很方便。例如，在一个聊天服务器中。 

这是在一个单线程中使用一个Selector处理3个Channel的图示： 

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180420.png?xxfjava)


要使用Selector，得向Selector注册Channel，然后调用它的select()方法。这个方法会一直阻塞到某个注册的通道有事件就绪。一旦这个方法返回，线程就可以处理这些事件，事件的例子有如新连接进来，数据接收等。 


### 5.2 Java NIO vs IO


当学习了Java NIO和IO的API后，一个问题马上涌入脑海： 

> 我应该何时使用IO，何时使用NIO呢？在本文中，我会尽量清晰地解析Java NIO和IO的差异、它们的使用场景，以及它们如何影响您的代码设计。


#### 5.2.1 Java NIO和IO的主要区别 

下表总结了Java NIO和IO之间的主要差别，我会更详细地描述表中每部分的差异。 

| IO                  | NIO                   |
| :------------------ | :-------------------- |
| Stream oriented 面向流 | Buffer oriented 面向缓冲区 |
| Blocking IO 阻塞      | Non blocking IO 非阻塞   |
| 无                   | Selectors 选择器         |


#### 5.2.2 面向流与面向缓冲 

Java NIO和IO之间第一个最大的区别是，IO是面向流的，NIO是面向缓冲区的。 Java IO面向流意味着每次从流中读一个或多个字节，直至读取所有字节，它们没有被缓存在任何地方。此外，它不能前后移动流中的数据。如果需要前后移动从流中读取的数据，需要先将它缓存到一个缓冲区。 Java NIO的缓冲导向方法略有不同。数据读取到一个它稍后处理的缓冲区，需要时可在缓冲区中前后移动。这就增加了处理过程中的灵活性。但是，还需要检查是否该缓冲区中包含所有您需要处理的数据。而且，需确保当更多的数据读入缓冲区时，不要覆盖缓冲区里尚未处理的数据。 

#### 5.2.3 阻塞与非阻塞IO 

Java IO的各种流是阻塞的。这意味着，当一个线程调用read() 或 write()时，该线程被阻塞，直到有一些数据被读取，或数据完全写入。该线程在此期间不能再干任何事情了。 Java NIO的非阻塞模式，使一个线程从某通道发送请求读取数据，但是它仅能得到目前可用的数据，如果目前没有数据可用时，就什么都不会获取。而不是保持线程阻塞，所以直至数据变的可以读取之前，该线程可以继续做其他的事情。 非阻塞写也是如此。一个线程请求写入一些数据到某通道，但不需要等待它完全写入，这个线程同时可以去做别的事情。 线程通常将非阻塞IO的空闲时间用于在其它通道上执行IO操作，所以一个单独的线程现在可以管理多个输入和输出通道（channel）。 

#### 5.2.4 选择器（Selectors） 

Java NIO的选择器允许一个单独的线程来监视多个输入通道，你可以注册多个通道使用一个选择器，然后使用一个单独的线程来“选择”通道：这些通道里已经有可以处理的输入，或者选择已准备写入的通道。这种选择机制，使得一个单独的线程很容易来管理多个通道。 

#### 5.2.5 NIO和IO如何影响应用程序的设计 

无论您选择IO或NIO工具箱，可能会影响您应用程序设计的以下几个方面： 

- 对NIO或IO类的API调用
- 数据处理
- 用来处理数据的线程数

##### 5.2.5.1 API调用 

当然，使用NIO的API调用时看起来与使用IO时有所不同，但这并不意外，因为并不是仅从一个InputStream逐字节读取，而是数据必须先读入缓冲区再处理。 

##### 5.2.5.2 数据处理 

使用纯粹的NIO设计相较IO设计，数据处理也受到影响。 

在IO设计中，我们从 InputStream 或 Reader 逐字节读取数据。假设你正在处理一基于行的文本数据流，例如： 


该文本行的流可以这样处理： 

```java
InputStream input = … ; // get the InputStream from the client socket  
BufferedReader reader = new BufferedReader(new InputStreamReader(input));  
  
String nameLine   = reader.readLine();  
String ageLine    = reader.readLine();  
String emailLine  = reader.readLine();  
String phoneLine  = reader.readLine();  
```

请注意处理状态由程序执行多久决定。换句话说，一旦reader.readLine()方法返回，你就知道肯定文本行就已读完， readline()阻塞直到整行读完，这就是原因。你也知道此行包含名称；同样，第二个readline()调用返回的时候，你知道这行包含年龄等。 正如你可以看到，该处理程序仅在有新数据读入时运行，并知道每步的数据是什么。一旦正在运行的线程已处理过读入的某些数据，该线程不会再回退数据（大多如此）。下图也说明了这条原则： 

![从一个阻塞的流中读数据](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180426.png?xxfjava)


而一个NIO的实现会有所不同，下面是一个简单的例子： 

```java
ByteBuffer buffer = ByteBuffer.allocate(48);  
  
int bytesRead = inChannel.read(buffer);  
```

注意第二行，从通道读取字节到ByteBuffer。当这个方法调用返回时，你不知道你所需的所有数据是否在缓冲区内。你所知道的是，该缓冲区包含一些字节，这使得处理有点困难。 

假设第一次 read(buffer)调用后，读入缓冲区的数据只有半行，例如，“Name:An”，你能处理数据吗？显然不能，需要等待，直到整行数据读入缓存，在此之前，对数据的任何处理毫无意义。 

所以，你怎么知道是否该缓冲区包含足够的数据可以处理呢？好了，你不知道。发现的方法只能查看缓冲区中的数据。其结果是，在你知道所有数据都在缓冲区里之前，你必须检查几次缓冲区的数据。这不仅效率低下，而且可以使程序设计方案杂乱不堪。例如： 

```java
ByteBuffer buffer = ByteBuffer.allocate(48);  
int bytesRead = inChannel.read(buffer);  
while(! bufferFull(bytesRead) ) {  
	bytesRead = inChannel.read(buffer);  
}  
```

bufferFull()方法必须跟踪有多少数据读入缓冲区，并返回真或假，这取决于缓冲区是否已满。换句话说，如果缓冲区准备好被处理，那么表示缓冲区满了。 

bufferFull()方法扫描缓冲区，但必须保持在bufferFull()方法被调用之前状态相同。如果没有，下一个读入缓冲区的数据可能无法读到正确的位置。这是不可能的，但却是需要注意的又一问题。 

如果缓冲区已满，它可以被处理。如果它不满，并且在你的实际案例中有意义，你或许能处理其中的部分数据。但是许多情况下并非如此。下图展示了“缓冲区数据循环就绪”： 


![从一个通道里读数据，直到所有的数据都读到缓冲区里](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180428.png?xxfjava)


#### 5.2.6 总结 

NIO可让您只使用一个（或几个）单线程管理多个通道（网络连接或文件），但付出的代价是解析数据可能会比从一个阻塞流中读取数据更复杂。 

如果需要管理同时打开的成千上万个连接，这些连接每次只是发送少量的数据，例如聊天服务器，实现NIO的服务器可能是一个优势。同样，如果你需要维持许多打开的连接到其他计算机上，如P2P网络中，使用一个单独的线程来管理你所有出站连接，可能是一个优势。一个线程多个连接的设计方案如下图所示： 


![单线程管理多个连接](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180431.png?xxfjava)


如果你有少量的连接使用非常高的带宽，一次发送大量的数据，也许典型的IO服务器实现可能非常契合。下图说明了一个典型的IO服务器设计： 


![一个典型的IO服务器设计：一个连接通过一个线程处理](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180432.png?xxfjava)


### 5.3 通道（Channel）


Java NIO的通道类似流，但又有些不同： 

- 既可以从通道中读取数据，又可以写数据到通道。但流的读写通常是单向的。
- 通道可以异步地读写。
- 通道中的数据总是要先读到一个Buffer，或者总是要从一个Buffer中写入。

正如上面所说，从通道读取数据到缓冲区，从缓冲区写入数据到通道。如下图所示： 

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180434.png?xxfjava)


#### 5.3.1 Channel的实现 

这些是Java NIO中最重要的通道的实现： 

- FileChannel：从文件中读写数据
- DatagramChannel：能通过UDP读写网络中的数据
- SocketChannel：能通过TCP读写网络中的数据
- ServerSocketChannel：可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel

获取Channel：InputStream/OutputStream.getChannel()

#### 5.3.2 基本的 Channel 示例 

下面是一个使用FileChannel读取数据到Buffer中的示例： 

```java
RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");  
FileChannel inChannel = aFile.getChannel();  
  
ByteBuffer buf = ByteBuffer.allocate(48);  
  
int bytesRead = inChannel.read(buf);  
while (bytesRead != -1) {  
  
	System.out.println("Read " + bytesRead);  
	buf.flip();  
  
	while(buf.hasRemaining()){  
		System.out.print((char) buf.get());  
	}  
  
	buf.clear();  
	bytesRead = inChannel.read(buf);  
}  
aFile.close();
```

注意 buf.flip() 的调用，首先读取数据到Buffer，然后反转Buffer,接着再从Buffer中读取数据。下一节会深入讲解Buffer的更多细节。 

### 5.4 缓冲区（Buffer）


Java NIO中的Buffer用于和NIO通道进行交互。如你所知，数据是从通道读入缓冲区，从缓冲区写入到通道中的。 

缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存。这块内存被包装成NIO Buffer对象，并提供了一组方法，用来方便的访问该块内存。 

#### 5.4.1 Buffer的基本用法 

使用Buffer读写数据一般遵循以下四个步骤： 

- 写入数据到Buffer
- 调用flip()方法
- 从Buffer中读取数据
- 调用clear()方法或者compact()方法

当向buffer写入数据时，buffer会记录下写了多少数据。一旦要读取数据，需要通过flip()方法将Buffer从写模式切换到读模式。在读模式下，可以读取之前写入到buffer的所有数据。 

一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。有两种方式能清空缓冲区：调用clear()或compact()方法。clear()方法会清空整个缓冲区。compact()方法只会清除已经读过的数据。任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面。 

| 方法声明                 | 功能描述                                 |
| -------------------- | ------------------------------------ |
| allocate()           | 分配空间，创建Buffer对象                      |
| allocateDirect()     | 直接创建Buffer对象，创建成本高，但读取效率高            |
| flip()               | 为读数据做准备, limit=position; position=0; |
| clear()              | 为写数据做准备, limit=capacity; position=0; |
| hasRemaining()       | pos和limit间是否还有元素可处理                  |
| remaining()          | 获取当前位置和limit间的元素个数                   |
| get()                | 从Buffer中读取数据                         |
| put()                | 写入数据到Buffer                          |
| capacity()           | 获取容量                                 |
| limit()              | 获取界限的位置                              |
| position()           | 获取pos位置                              |
| position(int newPos) | 设置pos位置                              |
| mark()               | 设置mark位置                             |
| reset()              | 将pos转到mark的位置                        |
| rewind()             | 将pos设置为0，取消mark                      |

下面是一个使用Buffer的例子： 

```java
RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");  
FileChannel inChannel = aFile.getChannel();  
  
//create buffer with capacity of 48 bytes  
ByteBuffer buf = ByteBuffer.allocate(48);  
  
int bytesRead = inChannel.read(buf); //read into buffer.  
while (bytesRead != -1) {  
  
  buf.flip();  //make buffer ready for read  
  
  while(buf.hasRemaining()){  
      System.out.print((char) buf.get()); // read 1 byte at a time  
  }  
  
  buf.clear(); //make buffer ready for writing  
  bytesRead = inChannel.read(buf);  
}  
aFile.close();  
```

#### 5.4.2 Buffer的capacity、position、limit 

缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存。这块内存被包装成NIO Buffer对象，并提供了一组方法，用来方便的访问该块内存。 

为了理解Buffer的工作原理，需要熟悉它的三个属性： 

- capacity 容量
- position 位置
- limit 界限

position和limit的含义取决于Buffer处在读模式还是写模式。不管Buffer处在什么模式，capacity的含义总是一样的。 

这里有一个关于capacity，position和limit在读写模式中的说明，详细的解释在插图后面。 

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180439.png?xxfjava)

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180441.png?xxfjava)

##### 5.4.2.1 capacity 

作为一个内存块，Buffer有一个固定的大小值，也叫“capacity”.你只能往里写capacity个byte、long，char等类型。一旦Buffer满了，需要将其清空（通过读数据或者清除数据）才能继续写数据往里写数据。 

##### 5.4.2.2 position 

当你写数据到Buffer中时，position表示当前的位置。初始的position值为0.当一个byte、long等数据写到Buffer后， position会向前移动到下一个可插入数据的Buffer单元。position最大可为capacity – 1。 

当读取数据时，也是从某个特定位置读。当将Buffer从写模式切换到读模式，position会被重置为0。当从Buffer的position处读取数据时，position向前移动到下一个可读的位置。 

##### 5.4.2.3 limit 

在写模式下，Buffer的limit表示你最多能往Buffer里写多少数据。 写模式下，limit等于Buffer的capacity。 

当切换Buffer到读模式时， limit表示你最多能读到多少数据。因此，当切换Buffer到读模式时，limit会被设置成写模式下的position值。换句话说，你能读到之前写入的所有数据（limit被设置成已写数据的数量，这个值在写模式下就是position） 

新分配的 CharBuffer 对象

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180444.png?xxfjava)

向 Buffer 中放入3个对象后的示意图

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180445.png?xxfjava)

执行 Buffer 的 flip() 方法后的示意图

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180447.png?xxfjava)

执行 clear() 后的 Buffer示意图

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180449.png?xxfjava)

#### 5.4.3 Buffer的类型 

Java NIO 有以下Buffer类型： 

- ByteBuffer
- MappedByteBuffer
- CharBuffer
- DoubleBuffer
- FloatBuffer
- IntBuffer
- LongBuffer
- ShortBuffer

如你所见，这些Buffer类型代表了不同的数据类型。换句话说，就是可以通过char，short，int，long，float 或 double类型来操作缓冲区中的字节。 

MappedByteBuffer 有些特别，在涉及它的专门章节中再讲。 

#### 5.4.4 Buffer的分配 

要想获得一个Buffer对象首先要进行分配。 每一个Buffer类都有一个allocate方法。下面是一个分配48字节capacity的ByteBuffer的例子。 

```java
ByteBuffer buf = ByteBuffer.allocate(48);  
```

这是分配一个可存储1024个字符的CharBuffer： 

```java
CharBuffer buf = CharBuffer.allocate(1024);  
```

#### 5.4.5 向Buffer中写数据 

写数据到Buffer有两种方式： 

- 从Channel写到Buffer。
- 通过Buffer的put()方法写到Buffer里。

从Channel写到Buffer的例子 

```java
int bytesRead = inChannel.read(buf); //read into buffer.  
```

通过put方法写Buffer的例子： 

```java
buf.put(127);  
```

put方法有很多版本，允许你以不同的方式把数据写入到Buffer中。例如， 写到一个指定的位置，或者把一个字节数组写入到Buffer。 更多Buffer实现的细节参考JavaDoc。 

##### flip()方法 

flip方法将Buffer从写模式切换到读模式。调用flip()方法会将position设回0，并将limit设置成之前position的值。 

换句话说，position现在用于标记读的位置，limit表示之前写进了多少个byte、char等 —— 现在能读取多少个byte、char等。 

#### 5.4.6 从Buffer中读取数据 

从Buffer中读取数据有两种方式： 

- 从Buffer读取数据到Channel。
- 使用get()方法从Buffer中读取数据。

从Buffer读取数据到Channel的例子： 

```java
//read from buffer into channel.  
int bytesWritten = inChannel.write(buf);  
```

使用get()方法从Buffer中读取数据的例子 

```java
byte aByte = buf.get();  
```

get方法有很多版本，允许你以不同的方式从Buffer中读取数据。例如，从指定position读取，或者从Buffer中读取数据到字节数组。更多Buffer实现的细节参考JavaDoc。 

##### rewind()方法 

Buffer.rewind()将position设回0，所以你可以重读Buffer中的所有数据。limit保持不变，仍然表示能从Buffer中读取多少个元素（byte、char等）。 

##### clear()与compact()方法 

一旦读完Buffer中的数据，需要让Buffer准备好再次被写入。可以通过clear()或compact()方法来完成。 

如果调用的是clear()方法，position将被设回0，limit被设置成 capacity的值。换句话说，Buffer 被清空了。Buffer中的数据并未清除，只是这些标记告诉我们可以从哪里开始往Buffer里写数据。 

如果Buffer中有一些未读的数据，调用clear()方法，数据将“被遗忘”，意味着不再有任何标记会告诉你哪些数据被读过，哪些还没有。 

如果Buffer中仍有未读的数据，且后续还需要这些数据，但是此时想要先先写些数据，那么使用compact()方法。

compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。limit属性依然像clear()方法一样，设置成capacity。现在Buffer准备好写数据了，但是不会覆盖未读的数据。 

##### mark()与reset()方法 

通过调用Buffer.mark()方法，可以标记Buffer中的一个特定position。之后可以通过调用Buffer.reset()方法恢复到这个position。例如： 

```java
buffer.mark();  
  
//call buffer.get() a couple of times, e.g. during parsing.  
  
buffer.reset();  //set position back to mark.  
```

##### equals()与compareTo()方法

可以使用equals()和compareTo()方法两个Buffer。 

equals()

当满足下列条件时，表示两个Buffer相等： 

- 有相同的类型（byte、char、int等）。
- Buffer中剩余的byte、char等的个数相等。
- Buffer中所有剩余的byte、char等都相同。

如你所见，equals只是比较Buffer的一部分，不是每一个在它里面的元素都比较。实际上，它只比较Buffer中的剩余元素。 

compareTo()方法

compareTo()方法比较两个Buffer的剩余元素(byte、char等)， 如果满足下列条件，则认为一个Buffer“小于”另一个Buffer： 

- 第一个不相等的元素小于另一个Buffer中对应的元素。
- 所有元素都相等，但第一个Buffer比另一个先耗尽(第一个Buffer的元素个数比另一个少)。

（译注：剩余元素是从 position到limit之间的元素） 

### 5.5 分散Scatter和聚集Gather

Java NIO开始支持scatter/gather，scatter/gather用于描述从Channel（译者注：Channel在中文经常翻译为通道）中读取或者写入到Channel的操作。 

分散（scatter）从Channel中读取是指在读操作时将读取的数据写入多个buffer中。因此，Channel将从Channel中读取的数据“分散（scatter）”到多个Buffer中。 

聚集（gather）写入Channel是指在写操作时将多个buffer的数据写入同一个Channel，因此，Channel 将多个Buffer中的数据“聚集（gather）”后发送到Channel。 

scatter / gather经常用于需要将传输的数据分开处理的场合，例如传输一个由消息头和消息体组成的消息，你可能会将消息体和消息头分散到不同的buffer中，这样你可以方便的处理消息头和消息体。 

#### 5.5.1 Scattering Reads 

Scattering Reads是指数据从一个channel读取到多个buffer中。如下图描述： 

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180453.png?xxfjava)


代码示例如下： 

```java
ByteBuffer header = ByteBuffer.allocate(128);  
ByteBuffer body   = ByteBuffer.allocate(1024);  
  
ByteBuffer[] bufferArray = { header, body };  
  
channel.read(bufferArray);  
```

注意buffer首先被插入到数组，然后再将数组作为channel.read() 的输入参数。read()方法按照buffer在数组中的顺序将从channel中读取的数据写入到buffer，当一个buffer被写满后，channel紧接着向另一个buffer中写。 

Scattering Reads在移动下一个buffer前，必须填满当前的buffer，这也意味着它不适用于动态消息(译者注：消息大小不固定)。换句话说，如果存在消息头和消息体，消息头必须完成填充（例如 128byte），Scattering Reads才能正常工作。 

#### 5.5.2 Gathering Writes 

Gathering Writes是指数据从多个buffer写入到同一个channel。如下图描述： 

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180456.png?xxfjava)


代码示例如下： 

```java
ByteBuffer header = ByteBuffer.allocate(128);  
ByteBuffer body   = ByteBuffer.allocate(1024);  
  
//write data into buffers  
  
ByteBuffer[] bufferArray = { header, body };  
  
channel.write(bufferArray);  
```

buffers数组是write()方法的入参，write()方法会按照buffer在数组中的顺序，将数据写入到channel，注意只有position和limit之间的数据才会被写入。因此，如果一个buffer的容量为128byte，但是仅仅包含58byte的数据，那么这58byte的数据将被写入到channel中。因此与Scattering Reads相反，Gathering Writes能较好的处理动态消息。 

### 5.6 通道之间的数据传输 

在Java NIO中，如果两个通道中有一个是FileChannel，那你可以直接将数据从一个channel（译者注：channel中文常译作通道）传输到另外一个channel。 

#### 5.6.1 transferFrom() 

FileChannel的transferFrom()方法可以将数据从源通道传输到FileChannel中（译者注：这个方法在JDK文档中的解释为将字节从给定的可读取字节通道传输到此通道的文件中）。下面是一个简单的例子： 

```java
RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");  
FileChannel      fromChannel = fromFile.getChannel();  
  
RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");  
FileChannel      toChannel = toFile.getChannel();  
  
long position = 0;  
long count = fromChannel.size();  
  
toChannel.transferFrom(position, count, fromChannel);  
```

方法的输入参数position表示从position处开始向目标文件写入数据，count表示最多传输的字节数。如果源通道的剩余空间小于 count 个字节，则所传输的字节数要小于请求的字节数。 

此外要注意，在SoketChannel的实现中，SocketChannel只会传输此刻准备好的数据（可能不足count字节）。因此，SocketChannel可能不会将请求的所有数据(count个字节)全部传输到FileChannel中。 

#### 5.6.2 transferTo() 

transferTo()方法将数据从FileChannel传输到其他的channel中。下面是一个简单的例子： 

```java
RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");  
FileChannel      fromChannel = fromFile.getChannel();  
  
RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");  
FileChannel      toChannel = toFile.getChannel();  
  
long position = 0;  
long count = fromChannel.size();  
  
fromChannel.transferTo(position, count, toChannel);  
```

是不是发现这个例子和前面那个例子特别相似？除了调用方法的FileChannel对象不一样外，其他的都一样。 

上面所说的关于SocketChannel的问题在transferTo()方法中同样存在。SocketChannel会一直传输数据直到目标buffer被填满。 

### 5.7 选择器（Selector）


Selector（选择器）是Java NIO中能够检测一到多个NIO通道，并能够知晓通道是否为诸如读写事件做好准备的组件。这样，一个单独的线程可以管理多个channel，从而管理多个网络连接。 

#### 5.7.1 为什么使用Selector? 

仅用单个线程来处理多个Channels的好处是，只需要更少的线程来处理通道。事实上，可以只用一个线程处理所有的通道。对于操作系统来说，线程之间上下文切换的开销很大，而且每个线程都要占用系统的一些资源（如内存）。因此，使用的线程越少越好。 

但是，需要记住，现代的操作系统和CPU在多任务方面表现的越来越好，所以多线程的开销随着时间的推移，变得越来越小了。实际上，如果一个CPU有多个内核，不使用多任务可能是在浪费CPU能力。不管怎么说，关于那种设计的讨论应该放在另一篇不同的文章中。在这里，只要知道使用Selector能够处理多个通道就足够了。 

下面是单线程使用一个Selector处理3个channel的示例图： 

#### 5.7.2 Selector的创建 

通过调用Selector.open()方法创建一个Selector，如下： 

```java
Selector selector = Selector.open();  
```

#### 5.7.3 向Selector注册通道 

为了将Channel和Selector配合使用，必须将channel注册到selector上。通过SelectableChannel.register()方法来实现，如下： 

```java
channel.configureBlocking(false);  
SelectionKey key = channel.register(selector,  Selectionkey.OP_READ);  
```

与Selector一起使用时，Channel必须处于非阻塞模式下。这意味着不能将FileChannel与Selector一起使用，因为FileChannel不能切换到非阻塞模式。而套接字通道都可以。 

注意register()方法的第二个参数。这是一个“interest集合”，意思是在通过Selector监听Channel时对什么事件感兴趣。可以监听四种不同类型的事件： 

- Connect
- Accept
- Read
- Write

通道触发了一个事件意思是该事件已经就绪。所以，某个channel成功连接到另一个服务器称为“连接就绪”。一个server socket channel准备好接收新进入的连接称为“接收就绪”。一个有数据可读的通道可以说是“读就绪”。等待写数据的通道可以说是“写就绪”。 

这四种事件用SelectionKey的四个常量来表示： 

- SelectionKey.OP_CONNECT
- SelectionKey.OP_ACCEPT
- SelectionKey.OP_READ
- SelectionKey.OP_WRITE

如果你对不止一种事件感兴趣，那么可以用“位或”操作符将常量连接起来，如下： 

```java
int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;  
```

在下面还会继续提到interest集合。 

#### 5.7.4 SelectionKey 

在上一小节中，当向Selector注册Channel时，register()方法会返回一个SelectionKey对象。这个对象包含了一些你感兴趣的属性： 

- interest集合
- ready集合
- Channel
- Selector
- 附加的对象（可选）

下面我会描述这些属性。 

##### interest集合 

就像向Selector注册通道一节中所描述的，interest集合是你所选择的感兴趣的事件集合。可以通过SelectionKey读写interest集合，像这样： 

```java
int interestSet = selectionKey.interes();  
  
boolean isInterestedInAccept  = (interestSet & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT；  
boolean isInterestedInConnect = interestSet & SelectionKey.OP_CONNECT;  
boolean isInterestedInRead    = interestSet & SelectionKey.OP_READ;  
boolean isInterestedInWrite   = interestSet & SelectionKey.OP_WRITE;  
```

可以看到，用“位与”操作interest 集合和给定的SelectionKey常量，可以确定某个确定的事件是否在interest 集合中。 

##### ready集合 

ready 集合是通道已经准备就绪的操作的集合。在一次选择(Selection)之后，你会首先访问这个ready set。Selection将在下一小节进行解释。可以这样访问ready集合： 

```java
int readySet = selectionKey.readyOps(); 
```

可以用像检测interest集合那样的方法，来检测channel中什么事件或操作已经就绪。但是，也可以使用以下四个方法，它们都会返回一个布尔类型： 

```java
selectionKey.isAcceptable();  
selectionKey.isConnectable();  
selectionKey.isReadable();  
selectionKey.isWritable();  
```

##### Channel + Selector 

从SelectionKey访问Channel和Selector很简单。如下： 

```java
Channel  channel  = selectionKey.channel();  
Selector selector = selectionKey.selector();  
```

##### 附加的对象 

可以将一个对象或者更多信息附着到SelectionKey上，这样就能方便的识别某个给定的通道。例如，可以附加 与通道一起使用的Buffer，或是包含聚集数据的某个对象。使用方法如下： 

```java
selectionKey.attach(theObject);  
Object attachedObj = selectionKey.attachment();  
```

还可以在用register()方法向Selector注册Channel的时候附加对象。如： 

```java
SelectionKey key = channel.register(selector, SelectionKey.OP_READ, theObject);  
```

#### 5.7.5 通过Selector选择通道 

一旦向Selector注册了一或多个通道，就可以调用几个重载的select()方法。这些方法返回你所感兴趣的事件（如连接、接受、读或写）已经准备就绪的那些通道。换句话说，如果你对“读就绪”的通道感兴趣，select()方法会返回读事件已经就绪的那些通道。 

下面是select()方法： 

```java
int select()
int select(long timeout)
int selectNow()
```

select()阻塞到至少有一个通道在你注册的事件上就绪了。 

select(long timeout)和select()一样，除了最长会阻塞timeout毫秒(参数)。 

selectNow()不会阻塞，不管什么通道就绪都立刻返回（译者注：此方法执行非阻塞的选择操作。如果自从前一次选择操作后，没有通道变成可选择的，则此方法直接返回零。）。 

select()方法返回的int值表示有多少通道已经就绪。亦即，自上次调用select()方法后有多少通道变成就绪状态。如果调用select()方法，因为有一个通道变成就绪状态，返回了1，若再次调用select()方法，如果另一个通道就绪了，它会再次返回1。如果对第一个就绪的channel没有做任何操作，现在就有两个就绪的通道，但在每次select()方法调用之间，只有一个通道就绪了。 

##### selectedKeys() 

一旦调用了select()方法，并且返回值表明有一个或更多个通道就绪了，然后可以通过调用selector的selectedKeys()方法，访问“已选择键集（selected key set）”中的就绪通道。如下所示： 

```java
Set selectedKeys = selector.selectedKeys();  
```

当像Selector注册Channel时，Channel.register()方法会返回一个SelectionKey 对象。这个对象代表了注册到该Selector的通道。可以通过SelectionKey的selectedKeySet()方法访问这些对象。 

可以遍历这个已选择的键集合来访问就绪的通道。如下： 

```java
Set selectedKeys = selector.selectedKeys();  
Iterator keyIterator = selectedKeys.iterator();  
while(keyIterator.hasNext()) {  
    SelectionKey key = keyIterator.next();  
    if(key.isAcceptable()) {  
        // a connection was accepted by a ServerSocketChannel.  
    } else if (key.isConnectable()) {  
        // a connection was established with a remote server.  
    } else if (key.isReadable()) {  
        // a channel is ready for reading  
    } else if (key.isWritable()) {  
        // a channel is ready for writing  
    }  
    keyIterator.remove();  
}  
```

这个循环遍历已选择键集中的每个键，并检测各个键所对应的通道的就绪事件。 

注意每次迭代末尾的keyIterator.remove()调用。Selector不会自己从已选择键集中移除SelectionKey实例。必须在处理完通道时自己移除。下次该通道变成就绪时，Selector会再次将其放入已选择键集中。 

SelectionKey.channel()方法返回的通道需要转型成你要处理的类型，如ServerSocketChannel或SocketChannel等。 

#### 5.7.6 wakeUp() 

某个线程调用select()方法后阻塞了，即使没有通道已经就绪，也有办法让其从select()方法返回。只要让其它线程在第一个线程调用select()方法的那个对象上调用Selector.wakeup()方法即可。阻塞在select()方法上的线程会立马返回。 

如果有其它线程调用了wakeup()方法，但当前没有线程阻塞在select()方法上，下个调用select()方法的线程会立即“醒来（wake up）”。 

#### 5.7.7 close() 

用完Selector后调用其close()方法会关闭该Selector，且使注册到该Selector上的所有SelectionKey实例无效。通道本身并不会关闭。 

#### 5.7.7 完整的示例 

这里有一个完整的示例，打开一个Selector，注册一个通道注册到这个Selector上(通道的初始化过程略去),然后持续监控这个Selector的四种事件（接受，连接，读，写）是否就绪。 

```java
Selector selector = Selector.open();  
channel.configureBlocking(false);  
SelectionKey key = channel.register(selector, SelectionKey.OP_READ);  
while(true) {  
  int readyChannels = selector.select();  
  if(readyChannels == 0) continue;  
  Set selectedKeys = selector.selectedKeys();  
  Iterator keyIterator = selectedKeys.iterator();  
  while(keyIterator.hasNext()) {  
    SelectionKey key = keyIterator.next();  
    if(key.isAcceptable()) {  
        // a connection was accepted by a ServerSocketChannel.  
    } else if (key.isConnectable()) {  
        // a connection was established with a remote server.  
    } else if (key.isReadable()) {  
        // a channel is ready for reading  
    } else if (key.isWritable()) {  
        // a channel is ready for writing  
    }  
    keyIterator.remove();  
  }  
}  
```

### 5.8 文件通道

Java NIO中的FileChannel是一个连接到文件的通道。可以通过文件通道读写文件。 

FileChannel无法设置为非阻塞模式，它总是运行在阻塞模式下。 

| 方法说明       | 功能描述                         |
| ---------- | ---------------------------- |
| read()     | 将数据从Channel读到Buffer，返回读取的字节数 |
| write()    | 将Buffer数据写入Channel           |
| map()      | 将数据映射成ByteBuffer，面向块的处理      |
| position() | 获取通道当前位置                     |
| size()     | 获取通道文件的大小                    |
| truncate() | 截取文件                         |
| force()    | 将通道里尚未写入磁盘的数据强制写到磁盘上         |
| lock()     | 获取文件锁，如果无法获取，程序将一直阻塞         |
| tryLock()  | 尝试获取文件锁                      |
| close()    | 关闭通道                         |

#### 5.8.1 打开FileChannel 

在使用FileChannel之前，必须先打开它。但是，我们无法直接打开一个FileChannel，需要通过使用一个InputStream、OutputStream或RandomAccessFile来获取一个FileChannel实例。下面是通过RandomAccessFile打开FileChannel的示例： 

```java
RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");  
FileChannel inChannel = aFile.getChannel();  
```

#### 5.8.2 从FileChannel读取数据 

调用多个read()方法之一从FileChannel中读取数据。如： 

```java
ByteBuffer buf = ByteBuffer.allocate(48);  
int bytesRead = inChannel.read(buf);  
```

首先，分配一个Buffer。从FileChannel中读取的数据将被读到Buffer中。 

然后，调用FileChannel.read()方法。该方法将数据从FileChannel读取到Buffer中。read()方法返回的int值表示了有多少字节被读到了Buffer中。如果返回-1，表示到了文件末尾。 

#### 5.8.3 向FileChannel写数据 

使用FileChannel.write()方法向FileChannel写数据，该方法的参数是一个Buffer。如： 

```java
String newData = "New String to write to file..." + System.currentTimeMillis();  
  
ByteBuffer buf = ByteBuffer.allocate(48);  
buf.clear();  
buf.put(newData.getBytes());  
  
buf.flip();  
  
while(buf.hasRemaining()) {  
    channel.write(buf);  
}  
```

注意FileChannel.write()是在while循环中调用的。因为无法保证write()方法一次能向FileChannel写入多少字节，因此需要重复调用write()方法，直到Buffer中已经没有尚未写入通道的字节。 

#### 5.8.4 关闭FileChannel 

用完FileChannel后必须将其关闭。如： 

```java
channel.close();  
```

FileChannel的position方法 

有时可能需要在FileChannel的某个特定位置进行数据的读/写操作。可以通过调用position()方法获取FileChannel的当前位置。 

也可以通过调用position(long pos)方法设置FileChannel的当前位置。 

这里有两个例子： 

```java
long pos = channel.position();  
channel.position(pos +123);  
```

如果将位置设置在文件结束符之后，然后试图从文件通道中读取数据，读方法将返回-1 —— 文件结束标志。 

如果将位置设置在文件结束符之后，然后向通道中写数据，文件将撑大到当前位置并写入数据。这可能导致“文件空洞”，磁盘上物理文件中写入的数据间有空隙。 

#### 5.8.5 FileChannel的size方法 

FileChannel实例的size()方法将返回该实例所关联文件的大小。如： 

```java
long fileSize = channel.size();  
```

#### 5.8.6 FileChannel的truncate方法 

可以使用FileChannel.truncate()方法截取一个文件。截取文件时，文件将中指定长度后面的部分将被删除。如：

```java
channel.truncate(1024);  
```

这个例子截取文件的前1024个字节。 

#### 5.8.7 FileChannel的force方法 

FileChannel.force()方法将通道里尚未写入磁盘的数据强制写到磁盘上。出于性能方面的考虑，操作系统会将数据缓存在内存中，所以无法保证写入到FileChannel里的数据一定会即时写到磁盘上。要保证这一点，需要调用force()方法。 

force()方法有一个boolean类型的参数，指明是否同时将文件元数据（权限信息等）写到磁盘上。 

下面的例子同时将文件数据和元数据强制写到磁盘上： 

```java
channel.force(true);  
```

### 5.9 Socket 通道


Java NIO中的SocketChannel是一个连接到TCP网络套接字的通道。可以通过以下2种方式创建SocketChannel： 

- 打开一个SocketChannel并连接到互联网上的某台服务器。
- 一个新连接到达ServerSocketChannel时，会创建一个SocketChannel。

#### 5.9.1 打开 SocketChannel 

下面是SocketChannel的打开方式： 

```java
SocketChannel socketChannel = SocketChannel.open();  
socketChannel.connect(new InetSocketAddress("http://jenkov.com", 80));  
```

#### 5.9.2 关闭 SocketChannel 

当用完SocketChannel之后调用SocketChannel.close()关闭SocketChannel： 

```java
socketChannel.close();  
```

#### 5.9.3 从 SocketChannel 读取数据 

要从SocketChannel中读取数据，调用一个read()的方法之一。以下是例子： 

```java
ByteBuffer buf = ByteBuffer.allocate(48);  
int bytesRead = socketChannel.read(buf);  
```

首先，分配一个Buffer。从SocketChannel读取到的数据将会放到这个Buffer中。 

然后，调用SocketChannel.read()。该方法将数据从SocketChannel 读到Buffer中。read()方法返回的int值表示读了多少字节进Buffer里。如果返回的是-1，表示已经读到了流的末尾（连接关闭了）。 

#### 5.9.4 写入 SocketChannel 

写数据到SocketChannel用的是SocketChannel.write()方法，该方法以一个Buffer作为参数。示例如下： 

```java
String newData = "New String to write to file..." + System.currentTimeMillis();  
  
ByteBuffer buf = ByteBuffer.allocate(48);  
buf.clear();  
buf.put(newData.getBytes());  
  
buf.flip();  
  
while(buf.hasRemaining()) {  
    channel.write(buf);  
}  
```

注意SocketChannel.write()方法的调用是在一个while循环中的。Write()方法无法保证能写多少字节到SocketChannel。所以，我们重复调用write()直到Buffer没有要写的字节为止。 

#### 5.9.5 非阻塞模式 

可以设置 SocketChannel 为非阻塞模式（non-blocking mode）.设置之后，就可以在异步模式下调用connect(), read() 和write()了。 

**connect()** 

如果SocketChannel在非阻塞模式下，此时调用connect()，该方法可能在连接建立之前就返回了。为了确定连接是否建立，可以调用finishConnect()的方法。像这样： 

```java
socketChannel.configureBlocking(false);  
socketChannel.connect(new InetSocketAddress("http://jenkov.com", 80));  
  
while(! socketChannel.finishConnect() ){  
    //wait, or do something else...  
}  
```

 **write()** 

非阻塞模式下，write()方法在尚未写出任何内容时可能就返回了。所以需要在循环中调用write()。前面已经有例子了，这里就不赘述了。 

**read()** 

非阻塞模式下,read()方法在尚未读取到任何数据时可能就返回了。所以需要关注它的int返回值，它会告诉你读取了多少字节。 

#### 5.9.6 非阻塞模式与选择器 

非阻塞模式与选择器搭配会工作的更好，通过将一或多个SocketChannel注册到Selector，可以询问选择器哪个通道已经准备好了读取，写入等。Selector与SocketChannel的搭配使用会在后面详讲。 

### 5.10 ServerSocket 通道

Java NIO中的 ServerSocketChannel 是一个可以监听新进来的TCP连接的通道，就像标准IO中的ServerSocket一样。ServerSocketChannel类在 java.nio.channels包中。 

这里有个例子： 

```java
ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();  
  
serverSocketChannel.socket().bind(new InetSocketAddress(9999));  
  
while(true){  
    SocketChannel socketChannel =  
            serverSocketChannel.accept();  
  
    //do something with socketChannel...  
}  
```

#### 5.10.1 打开 ServerSocketChannel 

通过调用 ServerSocketChannel.open() 方法来打开ServerSocketChannel.如： 

```java
ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();  
```

#### 5.10.2 关闭 ServerSocketChannel 

通过调用ServerSocketChannel.close() 方法来关闭ServerSocketChannel. 如： 

```java
serverSocketChannel.close();  
```

#### 5.10.3 监听新进来的连接 

通过 ServerSocketChannel.accept() 方法监听新进来的连接。当 accept()方法返回的时候，它返回一个包含新进来的连接的 SocketChannel。因此，accept()方法会一直阻塞到有新连接到达。 

通常不会仅仅只监听一个连接，在while循环中调用 accept()方法. 如下面的例子： 

```java
while(true){  
    SocketChannel socketChannel =  serverSocketChannel.accept();
    //do something with socketChannel...  
}  
```

当然，也可以在while循环中使用除了true以外的其它退出准则。 

#### 5.10.4 非阻塞模式 

ServerSocketChannel可以设置成非阻塞模式。在非阻塞模式下，accept() 方法会立刻返回，如果还没有新进来的连接，返回的将是null。 因此，需要检查返回的SocketChannel是否是null。如： 

```java
ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();  
serverSocketChannel.socket().bind(new InetSocketAddress(9999));  
serverSocketChannel.configureBlocking(false);  
  
while(true){  
    SocketChannel socketChannel = serverSocketChannel.accept();  
    if(socketChannel != null){  
        //do something with socketChannel...  
    }  
}  
```

### 5.11 Datagram 通道


Java NIO中的DatagramChannel是一个能收发UDP包的通道。因为UDP是无连接的网络协议，所以不能像其它通道那样读取和写入。它发送和接收的是数据包。 

#### 5.11.1 打开 DatagramChannel 

下面是 DatagramChannel 的打开方式： 

```java
DatagramChannel channel = DatagramChannel.open();  
channel.socket().bind(new InetSocketAddress(9999));  
```

这个例子打开的 DatagramChannel可以在UDP端口9999上接收数据包。 

#### 5.11.2 接收数据 

通过receive()方法从DatagramChannel接收数据，如： 

```java
ByteBuffer buf = ByteBuffer.allocate(48);  
buf.clear();  
channel.receive(buf);  
```

receive()方法会将接收到的数据包内容复制到指定的Buffer. 如果Buffer容不下收到的数据，多出的数据将被丢弃。 

#### 5.11.3 发送数据 

通过send()方法从DatagramChannel发送数据，如: 

```java
String newData = "New String to write to file..." + System.currentTimeMillis();  
  
ByteBuffer buf = ByteBuffer.allocate(48);  
buf.clear();  
buf.put(newData.getBytes());  
buf.flip();  
  
int bytesSent = channel.send(buf, new InetSocketAddress("jenkov.com", 80));  
```

这个例子发送一串字符到”jenkov.com”服务器的UDP端口80。 因为服务端并没有监控这个端口，所以什么也不会发生。也不会通知你发出的数据包是否已收到，因为UDP在数据传送方面没有任何保证。 

#### 5.11.4 连接到特定的地址 

可以将DatagramChannel“连接”到网络中的特定地址的。由于UDP是无连接的，连接到特定地址并不会像TCP通道那样创建一个真正的连接。而是锁住DatagramChannel ，让其只能从特定地址收发数据。 

这里有个例子: 

```java
channel.connect(new InetSocketAddress("jenkov.com", 80));  
```

当连接后，也可以使用read()和write()方法，就像在用传统的通道一样。只是在数据传送方面没有任何保证。这里有几个例子： 

```java
int bytesRead = channel.read(buf);  
int bytesWritten = channel.write(but);  
```

### 5.12 管道（Pipe）


Java NIO 管道是2个线程之间的单向数据连接。Pipe有一个source通道和一个sink通道。数据会被写到sink通道，从source通道读取。 

这里是Pipe原理的图示： 

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518180507.png?xxfjava)

#### 5.12.1 创建管道 

通过Pipe.open()方法打开管道。例如： 

```java
Pipe pipe = Pipe.open();  
```

#### 5.12.2 向管道写数据 

要向管道写数据，需要访问sink通道。像这样： 

```java
Pipe.SinkChannel sinkChannel = pipe.sink();  
```

通过调用SinkChannel的write()方法，将数据写入SinkChannel,像这样： 

```java
String newData = "New String to write to file..." + System.currentTimeMillis();  
ByteBuffer buf = ByteBuffer.allocate(48);  
buf.clear();  
buf.put(newData.getBytes());  
  
buf.flip();  
  
while(buf.hasRemaining()) {  
    sinkChannel.write(buf);  
}  
```

#### 5.12.3 从管道读取数据 

从读取管道的数据，需要访问source通道，像这样： 

```java
Pipe.SourceChannel sourceChannel = pipe.source();  
```

调用source通道的read()方法来读取数据，像这样： 

```java
ByteBuffer buf = ByteBuffer.allocate(48);  
  
int bytesRead = inChannel.read(buf);  
```

read()方法返回的int值会告诉我们多少字节被读进了缓冲区

### 5.13 总结

本文中说了最重要的3个概念

- Channel 通道
- Buffer 缓冲区
- Selector 选择器

其中Channel对应以前的流，Buffer不是什么新东西，Selector是因为nio可以使用异步的非堵塞模式才加入的东西。

以前的流总是堵塞的，一个线程只要对它进行操作，其它操作就会被堵塞，也就相当于水管没有阀门，你伸手接水的时候，不管水到了没有，你就都只能耗在接水（流）上。

nio的Channel的加入，相当于增加了水龙头（有阀门），虽然一个时刻也只能接一个水管的水，但依赖轮换策略，在水量不大的时候，各个水管里流出来的水，都可以得到妥善接纳，这个关键之处就是增加了一个接水工，也就是Selector，他负责协调，也就是看哪根水管有水了的话，在当前水管的水接到一定程度的时候，就切换一下：临时关上当前水龙头，试着打开另一个水龙头（看看有没有水）。

当其他人需要用水的时候，不是直接去接水，而是事前提了一个水桶给接水工，这个水桶就是Buffer。也就是，其他人虽然也可能要等，但不会在现场等，而是回家等，可以做其它事去，水接满了，接水工会通知他们。

这其实也是非常接近当前社会分工细化的现实，也是统分利用现有资源达到并发效果的一种很经济的手段，而不是动不动就来个并行处理，虽然那样是最简单的，但也是最浪费资源的方式。

## 6. AIO

### 6.1 JDK7 AIO初体验

JDK7已经release一段时间了，有个重要的新特性是AIO。今天趁闲暇，简单体验了下，简单分享如下：

### 6.2 关于AIO的概念理解
关于AIO的概念，仅谈谈个人的一点理解。可能不到位，请大家指出。
Io的两个重要步骤：发起IO请求，和实际的IO操作。在unix网络编程的定义里异步和非异步概念的区别就是实际的IO操作是否阻塞。如果不是就是异步，如果是就是同步。

而阻塞和非阻塞的区别在于发起IO请求的时候是否会阻塞，如果会就是阻塞，不会就是非阻塞。

本人理解能力有限，想了个例子来辅助自己理解：
小明想要买一本<深入java虚拟机>的书，以下几个场景可以来理解这几种io模式：

1.    如果小明每天都去书店问售货员说有没有这本书，如果没有就回去继续等待，等下次再过来文。(阻塞)
2.    如果小明告诉售货员想买一本<深入java虚拟机>的书，那么就在家里等着做其他事情去了，如果书到了售货员就通知小明，小明再自己过去取。
3.    如果小明告售货员想买一本<深入java虚拟机>的书，然后告诉售货员到了帮他送到某某地方去，就做其他事情去了。小明就不管了，等书到了，售货员就帮他送到那个地方了。
      售货员可以认为是操作系统的一个服务，而小明是一个用户进程。不知道是否有误，如果有误请大家拍砖指出，谢谢。
      可以看出2,3的效率明显要比1高。但是1最简单，而2,3需要一些协作。充分证明了团队合作的力量。

### 6.3 JDK7 AIO初体验
AsynchronousChannel：支持异步通道，包括服务端AsynchronousServerSocketChannel和普通AsynchronousSocketChannel等实现。

CompletionHandler：用户处理器。定义了一个用户处理就绪事件的接口，由用户自己实现，异步io的数据就绪后回调该处理器消费或处理数据。

AsynchronousChannelGroup：一个用于资源共享的异步通道集合。处理IO事件和分配给CompletionHandler。(具体这块还没细看代码，后续再分析这块)

以一个简单监听服务端为例，基本过程是：

1.    启动一个服务端通道
2.    定义一个事件处理器，用户事件完成的时候处理，如消费数据。
3.    向系统注册一个感兴趣的事件，如接受数据，并把事件完成的处理器传递给系统。
4.    都已经交待完毕，可以只管继续做自己的事情了，操作系统在完成事件后通过其他的线程会自动调用处理器完成事件处理

以下用一个例子来简单实现，一个服务端和客户端。服务端监听客户端的消息，并打印出来

AIOServer.java

```java
package io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * @author noname
 */
public class AIOServer {
	public final static int PORT = 9888;
	private AsynchronousServerSocketChannel server;

	public AIOServer() throws IOException {
		server = AsynchronousServerSocketChannel.open().bind(
				new InetSocketAddress(PORT));
	}

	public void startWithFuture() throws InterruptedException,
			ExecutionException, TimeoutException {
		System.out.println("Server listen on " + PORT);
		Future<AsynchronousSocketChannel> future = server.accept();
		AsynchronousSocketChannel socket = future.get();
		ByteBuffer readBuf = ByteBuffer.allocate(1024);
		readBuf.clear();
		socket.read(readBuf).get(100, TimeUnit.SECONDS);
		readBuf.flip();
		System.out.printf("received message:" + new String(readBuf.array()));
		System.out.println(Thread.currentThread().getName());

	}

	public void startWithCompletionHandler() throws InterruptedException,
			ExecutionException, TimeoutException {
		System.out.println("Server listen on " + PORT);
		//注册事件和事件完成后的处理器
		server.accept(null,
				new CompletionHandler<AsynchronousSocketChannel, Object>() {
					final ByteBuffer buffer = ByteBuffer.allocate(1024);

					public void completed(AsynchronousSocketChannel result,
							Object attachment) {
						System.out.println(Thread.currentThread().getName());
						System.out.println("start");
						try {
							buffer.clear();
							result.read(buffer).get(100, TimeUnit.SECONDS);
							buffer.flip();
							System.out.println("received message: "
									+ new String(buffer.array()));
						} catch (InterruptedException | ExecutionException e) {
							System.out.println(e.toString());
						} catch (TimeoutException e) {
							e.printStackTrace();
						} finally {

							try {
								result.close();
								server.accept(null, this);
							} catch (Exception e) {
								System.out.println(e.toString());
							}
						}

						System.out.println("end");
					}

					@Override
					public void failed(Throwable exc, Object attachment) {
						System.out.println("failed: " + exc);
					}
				});
		// 主线程继续自己的行为
		while (true) {
			System.out.println("main thread");
			Thread.sleep(1000);
		}

	}

	public static void main(String args[]) throws Exception {
		new AIOServer().startWithCompletionHandler();
	}
}
```

AIOClient.java

```java
package io.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class AIOClient {

	public static void main(String... args) throws Exception {
		AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
		client.connect(new InetSocketAddress("localhost", 9888));
		client.write(ByteBuffer.wrap("test".getBytes())).get();
	}
}
```

服务端写了两种处理实现方式，startWithCompletionHandler是通过Handler来处理，startWithFuture是通过Future方式来处理。startWithCompletionHandler方法里可以看到调用accepte()完成异步注册后，线程就可以继续自己的处理了，完全不被这个io所中断。

从以上来看AIO的代码简单了很多，至少比NIO的代码实现简单很多。

### 6.4 AIO和NIO性能哪个好

Java NIO ： 同步非阻塞，服务器实现模式为一个请求一个线程，即客户端发送的连接请求都会注册到多路复用器上，多路复用器轮询到连接有I/O请求时才启动一个线程进行处理。
Java AIO(NIO.2) ： 异步非阻塞，服务器实现模式为一个有效请求一个线程，客户端的I/O请求都是由OS先完成了再通知服务器应用去启动线程进行处理，

NIO方式适用于连接数目多且连接比较短（轻操作）的架构，比如聊天服务器，并发局限于应用中，编程比较复杂，JDK1.4开始支持。
AIO方式使用于连接数目多且连接比较长（重操作）的架构，比如相册服务器，充分调用OS参与并发操作，编程比较复杂，JDK7开始支持

I/O属于底层操作，需要操作系统支持，并发也需要操作系统的支持，所以性能方面不同操作系统差异会比较明显。另外NIO的非阻塞，需要一直轮询，也是一个比较耗资源的。所以出现AIO

### 6.5 使用J2SE进行服务器架构技术选型的变迁

虽然现在对大多数程序员来讲，基本不会再有使用Java开发一个服务器这样的任务，但是，这方面的技术研究一下，对自己的技术提高还是非常有帮助的。说不定啥时候能派上用场。

使用Java（J2SE）来设计服务器产品（不使用开源或其他已有产品）的架构，随着Java的不断发展，这几年也发生了很大变化。在JDK1.4之前，使用Java构建服务器应用本身就很少，所以这里也就不提了，我们从JDK1.4开始说。

#### 阶段1：一个连接一个线程

#### 阶段2：服务器端采用了线程池

阶段1和阶段2虽然简单，但是很实用，在很多场景下仍然是第一选择。而且编程模型业内非常简单。

#### 阶段3：采用非阻塞IO，多路复用技术，又有两种不同的方式

这种方式很重要的一点就是在IO事件发生时得到通知，由程序进行处理。

NIO给编程带来了很大的复杂度，使用NIO开发非常不容易，也很容易犯错误，所以，采用别人的框架是一个简单而自然的选择，采用grizzly和mina都很不错，对通用的场景都能满足要求。这里提醒一下，不管mina和grizzly，都有一些你不想用的特性，干扰你想用的功能，需要小心对待，最好自己也能处理mina和grizzly的bug，改进这些框架的功能。

再有，给予NIO来开发SSL也很复杂。

#### 阶段4：使用AIO技术

AIO最大的特性就是事前先设置好事件的回调函数，事件发生时自动调用回调。而且，得到的通知是“IO操作完成”，而不是NIO的“IO操作即将开始”。

使用AIO，在上层开发SSL也也很麻烦。

## 7. 序列化流


### 7.1 什么是java序列化，如何实现java序列化？

我们有时候将一个java对象变成字节流的形式传出去或者从一个字节流中恢复成一个java对象，例如，要将java对象存储到硬盘或者传送给网络上的其他计算机，这个过程我们可以自己写代码去把一个java对象变成某个格式的字节流再传输，但是，JRE本身就提供了这种支持，我们可以调用OutputStream的writeObject()方法来做，如果要让java 帮我们做，要被传输的对象必须实现Serializable接口，这样，javac编译时就会进行特殊处理，编译的类才可以被writeObject()方法操作，这就是所谓的序列化。需要被序列化的类必须实现Serializable接口，该接口是一个mini接口，其中没有需要实现的方法，implements Serializable只是为了标注该对象是可被序列化的

例如，在web开发中，如果对象被保存在了Session中，tomcat在重启时要把Session对象序列化到硬盘，这个对象就必须实现Serializable接口。如果对象要经过分布式系统进行网络传输或通过rmi等远程调用，这就需要在网络上传输对象，被传输的对象就必须实现Serializable接口

### 7.2 Serializable

Serializable是一个javase标记接口，会产生一个序列化值，该值跟bean的成员相关，所以实现Serilizable接口的时候，必须给一个uid，否则，当成员变化的时候，标记值也会变化，再次读取的时候也出现exception(要先重新write再read，但是write可能会让之前的数据丢失)

** 注意事项 **

- 使用transient关键字声明不需要序列化的成员变量
- 序列化数据后，再次修改类文件，读取数据会出问题，如何解决呢?
```
private static final long serialVersionUID = -2071565876962058344L;
```

### 7.3 序列化流ObjectOutputStream

ObjectOutputStream 将 Java 对象的基本数据类型和图形写入 OutputStream。可以使用 ObjectInputStream 读取（重构）对象。通过在流中使用文件可以实现对象的持久存储。如果流是网络套接字流，则可以在另一台主机上或另一个进程中重构对象。

只能将支持 java.io.Serializable 接口的对象写入流中。每个 Serializable 对象的类都被编码，编码内容包括类名和类签名、对象的字段值和数组值，以及从初始对象中引用的其他所有对象的闭包。 

writeObject() 方法用于将对象写入流中。所有对象（包括 String 和数组）都可以通过 writeObject ()写入。可将多个对象或基元写入流中。必须使用与写入对象时相同的类型和顺序从相应 ObjectInputstream 中读回对象。 

还可以使用 DataOutput 中的适当方法将基本数据类型写入流中。还可以使用 writeUTF() 方法写入字符串。 

对象的默认序列化机制写入的内容是：对象的类，类签名，以及非瞬态和非静态字段的值。其他对象的引用（瞬态和静态字段除外）也会导致写入那些对象。可使用引用共享机制对单个对象的多个引用进行编码，这样即可将对象的图形恢复为最初写入它们时的形状。 

** 构造方法 ** 

- ObjectOutputStream() 

为完全重新实现 ObjectOutputStream 的子类提供一种方法，让它不必分配仅由 ObjectOutputStream 的实现使用的私有数据。 

- ObjectOutputStream(OutputStream out) ：

创建写入指定 OutputStream 的 ObjectOutputStream

### 7.4 反序列化流ObjectInputStream

1、ObjectInputStream 对以前使用 ObjectOutputStream 写入的基本数据和对象进行反序列化。 

2、ObjectOutputStream 和 ObjectInputStream 分别与 FileOutputStream 和 FileInputStream 一起使用时，可以为应用程序提供对对象图形的持久存储。ObjectInputStream 用于恢复那些以前序列化的对象。其他用途包括使用套接字流在主机之间传递对象，或者用于编组和解组远程通信系统中的实参和形参。 

3、ObjectInputStream 确保从流创建的图形中所有对象的类型与 Java 虚拟机中显示的类相匹配。使用标准机制按需加载类。 

4、只有支持 java.io.Serializable 或 java.io.Externalizable 接口的对象才能从流读取。 

5、readObject 方法用于从流读取对象。应该使用 Java 的安全强制转换来获取所需的类型。在 Java 中，字符串和数组都是对象，所以在序列化期间将其视为对象。读取时，需要将其强制转换为期望的类型。 

6、可以使用 DataInput 上的适当方法从流读取基本数据类型。 

7、默认情况下，对象的反序列化机制会将每个字段的内容恢复为写入时它所具有的值和类型。反序列化进程将忽略声明为瞬态或静态的字段。对其他对象的引用使得根据需要从流中读取这些对象。使用引用共享机制能够正确地恢复对象的图形。反序列化时始终分配新对象，这样可以避免现有对象被重写。 

8、序列化操作问题：NotSerializableException:未序列化异常

9、为什么要实现序列化?如何实现序列化?

类通过实现 java.io.Serializable 接口以启用其序列化功能。未实现此接口的类将无法使其任何状态序列化或反序列化。

该接口居然没有任何方法，类似于这种没有方法的接口被称为标记接口。

10、序列化数据后，再次修改类文件，读取数据会出问题，如何解决呢?

每次修改java文件的内容的时候,class文件的id值都会发生改变。而读取文件的时候，会和class文件中的id值进行匹配。所以，就会出问题。让这个id值在java文件中是一个固定的值，这样，你修改文件的时候，这个id值就不会发生改变。

我们要知道的是：看到类实现了序列化接口的时候，要想解决黄色警告线问题，就可以自动产生一个序列化id值。而且产生这个值以后，我们对类进行任何改动，它读取以前的数据是没有问题的。

11、我一个类中可能有很多的成员变量，有些我不想进行序列化。请问该怎么办呢?

使用transient关键字声明不需要序列化的成员变量
代码示例：
```java
package cn.xiaoxiaofeng;  
  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;  
  
/* 
 * 序列化流：把对象按照流一样的方式存入文本文件或者在网络中传输。对象 -- 流数据(ObjectOutputStream) 
 * 反序列化流:把文本文件中的流对象数据或者网络中的流对象数据还原成对象。流数据 -- 对象(ObjectInputStream) 
 */  
public class ObjectStreamDemo {  
    public static void main(String[] args) throws IOException,  
            ClassNotFoundException {  
        // 由于我们要对对象进行序列化，所以我们先自定义一个类  
        // 序列化数据其实就是把对象写到文本文件  
        // write();  
  
        read();  
    }  
  
    private static void read() throws IOException, ClassNotFoundException {  
        // 创建反序列化对象  
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(  
                "oos.txt"));  
  
        // 还原对象  
        Object obj = ois.readObject();  
  
        // 释放资源  
        ois.close();  
  
        // 输出对象  
        System.out.println(obj);  
    }  
  
    private static void write() throws IOException {  
        // 创建序列化流对象  
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(  
                "oos.txt"));  
  
        // 创建对象  
        Person p = new Person("林青霞", 27);  
  
        // public final void writeObject(Object obj)  
        oos.writeObject(p);  
  
        // 释放资源  
        oos.close();  
    }  
}  
```

