![05 Java 数组](https://image.xiaoxiaofeng.site/blog/2023/07/04/xxf-20230704132535.png?xxfjava)
## 1. 数组概述

需求：现在需要统计某公司员工的工资情况，例如计算平均工资、找到最高工资等。假设该公司有80名员工，用前面所学的知识，程序首先需要声明80个变量来分别记住每位员工的工资，然后在进行操作，这样做会显得很麻烦。为了解决这种问题，Java就提供了数组供我们使用

那么数组到底是什么呢?有什么特点呢?通过上面的分析：我们可以得到如下两句话：

- 数组是存储多个变量(元素)的东西(容器)
- 这多个变量的数据类型要一致

## 2. 数组概念

- 数组是存储同一种数据类型多个元素的集合。也可以看成是一个容器
- 数组既可以存储基本数据类型，也可以存储引用数据类型

## 3. 数组的定义格式

格式1：数据类型[] 数组名;
格式2：数据类型 数组名[];
注意：这两种定义做完了，数组中是没有元素值的

## 4. 数组初始化概述：

- Java中的数组必须先初始化,然后才能使用
- 所谓初始化：就是为数组中的数组元素分配内存空间，并为每个数组元素赋值
- 数组的初始化方式
  - 动态初始化：初始化时只指定数组长度，由系统为数组分配初始值。
  - 静态初始化：初始化时指定每个数组元素的初始值，由系统决定数组长度
- 动态初始化：初始化时只指定数组长度，由系统为数组分配初始值

格式：数据类型[] 数组名 = new 数据类型[数组长度];
数组长度其实就是数组中元素的个数。
举例：int[] arr = new int[3];
解释：定义了一个int类型的数组，这个数组中可以存放3个int类型的值。

## 5. Java中的内存分配

Java 程序在运行时，需要在内存中的分配空间。为了提高运算效率，就对空间进行了不同区域的划分，因为每一片区域都有特定的处理数据方式和内存管理方式。

- 栈 存储局部变量
- 堆 存储new出来的东西
- 方法区 (面向对象部分讲)
- 本地方法区 (和系统相关)
- 寄存器 (给CPU使用)

![](https://image.xiaoxiaofeng.site/blog/2023/06/29/xxf-20230629172251.png?xxfjava)

## 6. Java中数组的内存图解

图解1：定义一个数组，输出数组名及元素。然后给数组中的元素赋值，再次输出数组名及元素。

![](https://image.xiaoxiaofeng.site/blog/2023/06/29/xxf-20230629172252.png?xxfjava)

图解2：定义两个数组，分别输出数组名及元素。然后分别给数组中的元素赋值，分别再次输出数组名及元素。

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518135823.png?xxfjava)

图解3：定义两个数组，先定义一个数组，赋值，输出。然后定义第二个数组的时候把第一个数组的地址赋值给第二个数组。然后给第二个数组赋值，再次输出两个数组的名及元素。

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518135825.png?xxfjava)

## 7. 数组的初始化
静态初始化：初始化时指定每个数组元素的初始值，由系统决定数组长度。
格式：数据类型[] 数组名 = new 数据类型[]{元素1,元素2,…};
举例：int[] arr = new int[]{1,2,3};
解释：定义了一个int类型的数组，这个数组中可以存放3个int类型的值，并且值分别是1,2,3。
其实这种写法还有一个简化的写法：int[] arr = {1,2,3};

## 8. 数组操作常见的两个小问题

- ArrayIndexOutOfBoundsException 数组索引越界
  访问到了数组中的不存在的索引时发生
- NullPointerException 空指针异常
  数组引用没有指向实体，却在操作实体中的元素时。

## 9. 二维数组概述

二维数组概述

我们传智播客的Java基础班每个班有很多个学生，所以，可以用数组来存储，而我们又同时有很多个Java基础班。这个也应该用一个数组来存储。如何来表示这样的数据呢?Java就提供了二维数组供我们使用。由此可见：其实二维数组其实就是一个元素为一维数组的数组。

二维数组定义格式
（1）格式1
```java
数据类型[][] 变量名 = new 数据类型[m][n];
// m表示这个二维数组有多少个一维数组
// n表示每一个一维数组的元素个数
```
举例：
```java
int[][] arr = new int[3][2];
// 定义了一个二维数组arr
// 这个二维数组有3个一维数组，名称是arr[0],arr[1],arr[2]
// 每个一维数组有2个元素，可以通过arr[m][n]来获取
// 表示获取第m+1个一维数组的第n+1个元素
```

（2）格式2
```java
数据类型[][] 变量名 = new 数据类型[m][];
// m表示这个二维数组有多少个一维数组
// 这一次没有直接给出一维数组的元素个数，可以动态的给出。
```
举例：
```java
int[][] arr = new int[3][];
arr[0] = new int[2];
arr[1] = new int[3]
arr[2] = new int[1];
```
（3）格式3
```java
数据类型[][] 变量名 = new 数据类型[][]{ {元素…},{元素…},{元素…} };
简化版格式：数据类型[][] 变量名 = { {元素…},{元素…},{元素…} };
举例：int[][] arr =  { {1,2,3},{4,6},{6} };
```
## 10. 数组常见操作

### 10.1 数组遍历

```java
/*
    数组遍历：就是依次输出数组中的每一个元素。

    注意：数组提供了一个属性length，用于获取数组的长度。
          格式：数组名.length
*/  
class ArrayTest {  
    public static void main(String[] args) {  
        //定义数组  
        int[] arr = {11,22,33,44,55};  

        //获取每一个元素  
        //如何获取呢?我们知道数组名结合编号(索引)就可以找到数据  
        for(int x=0; x<5; x++) {  
            //x=0,1,2,3,4  
            System.out.println(arr[x]);  
        }  
        System.out.println("--------------------");  

        //从0开始我们是明确的，但是为什么到5呢，我们是数了一下数组的个数  
        //继续看下个数组如何遍历  
        int[] arr2 = {1,2,3,4,5,6,7,8,9,10,11,2,2,3,4,5,7,8,5,3,5,6,8,7,8,5,3,5,6,8,7,8,5,3,5,6,8,7,8,5,3,5,6,8,7,8,5,3,5,6,8};  
        //而我们在很多时候，数组的元素不能靠数  
        //这个时候，数组就给我们提供了一个属性：length专门用于获取数组的长度  
        //格式：数组名.length 返回数组的长度  
        System.out.println(arr.length);  
        System.out.println(arr2.length);  
        System.out.println("--------------------");  

        //改进第一个程序  
        for(int x=0; x<arr.length; x++) {  
            System.out.println(arr[x]);  
        }  
        System.out.println("--------------------");  

        //我们如果想要对多个数组进行遍历，每个数组的遍历我们都把代码写一遍，麻烦不  
        //麻烦，所以，我们准备用方法改进。  
        //用方法改进后，请调用  
        printArray(arr);  
        System.out.println("--------------------");  
        printArray(arr2);  
        System.out.println("--------------------");  
        printArray2(arr);  
    }  

    /*
        遍历数组的方法

        两个明确：
            返回值类型：void
            参数列表：int[] arr
    */  
    public static void printArray(int[] arr) {  
        for(int x=0; x<arr.length; x++) {  
            System.out.println(arr[x]);  
        }  
    }  

    //请看改进版本  
    public static void printArray2(int[] arr) {  
        System.out.print("[");  
        for(int x=0; x<arr.length; x++) {  
            if(x == arr.length-1) { //这是最后一个元素  
                System.out.println(arr[x]+"]");  
            }else {  
                System.out.print(arr[x]+", ");  
            }  
        }  
    }  
}
```

### 10.2 获取数组最值

```java
/*
    数组获取最值(获取数组中的最大值最小值)

    分析：
        A:定义一个数组，并对数组的元素进行静态初始化。
        B:从数组中任意的找一个元素作为参照物(一般取第一个),默认它就是最大值。
        C:然后遍历其他的元素，依次获取和参照物进行比较，如果大就留下来，如果小，就离开。
        D:最后参照物里面保存的就是最大值。
*/  
class ArrayTest2 {  
    public static void main(String[] args) {  
        //定义一个数组  
        int[] arr = {34,98,10,25,67};  

        //请获取数组中的最大值  
        /*
        //从数组中任意的找一个元素作为参照物
        int max = arr[0];
        //然后遍历其他的元素
        for(int x=1; x<arr.length; x++) {
            //依次获取和参照物进行比较，如果大就留下来，如果小，就离开。
            if(arr[x] > max) {
                max = arr[x];
            }
        }
        //最后参照物里面保存的就是最大值。
        System.out.println("max:"+max);
        */  

        //把这个代码用方法改进  
        //调用方法  
        int max = getMax(arr);  
        System.out.println("max:"+max);  

        //请获取数组中的最小值  
        int min = getMin(arr);  
        System.out.println("min:"+min);  
    }  

    /*
        需求：获取数组中的最大值
        两个明确：
            返回值类型：int
            参数列表：int[] arr
    */  
    public static int getMax(int[] arr) {  
        //从数组中任意的找一个元素作为参照物  
        int max = arr[0];  
        //然后遍历其他的元素  
        for(int x=1; x<arr.length; x++) {  
            //依次获取和参照物进行比较，如果大就留下来，如果小，就离开。  
            if(arr[x] > max) {  
                max = arr[x];  
            }  
        }  
        //最后参照物里面保存的就是最大值。  
        return max;  
    }  

    public static int getMin(int[] arr) {  
        //从数组中任意的找一个元素作为参照物  
        int min = arr[0];  
        //然后遍历其他的元素  
        for(int x=1; x<arr.length; x++) {  
            //依次获取和参照物进行比较，如果小就留下来，如果大，就离开。  
            if(arr[x] < min) {  
                min = arr[x];  
            }  
        }  
        //最后参照物里面保存的就是最小值。  
        return min;  
    }  
}
```
运行结果：

![](https://image.xiaoxiaofeng.site/blog/2023/05/18/xxf-20230518135832.png?xxfjava)

### 10.3 数组元素逆序

```java
/*
    数组元素逆序 (就是把元素对调)

    分析：
        A:定义一个数组，并进行静态初始化。
        B:思路
            把0索引和arr.length-1的数据交换
            把1索引和arr.length-2的数据交换
            ...
            只要做到arr.length/2的时候即可。
*/  
class ArrayTest3 {  
    public static void main(String[] args) {  
        //定义一个数组，并进行静态初始化。  
        int[] arr = {12,98,50,34,76};  

        //逆序前  
        System.out.println("逆序前：");  
        printArray(arr);  

        //逆序后  
        System.out.println("逆序后：");  
        //reverse(arr);  
        reverse2(arr);  
        printArray(arr);  
    }  

    /*
        需求：数组逆序
        两个明确：
            返回值类型：void (有人会想到应该返回的是逆序后的数组，但是没必要，因为这两个数组其实是同一个数组)
            参数列表：int[] arr
    */  
    public static void reverse(int[] arr) {  
        /*
        //第一次交换
        int temp = arr[0];
        arr[0] = arr[arr.length-1-0];
        arr[arr.length-1-0] = temp;

        //第二次交换
        int temp = arr[1];
        arr[1] = arr[arr.length-1-1];
        arr[arr.length-1-1] = temp;

        //第三次交换
        int temp = arr[2];
        arr[2] = arr[arr.length-1-2];
        arr[arr.length-1-2] = temp;
        */  
        //用循环改进  
        for(int x=0; x<arr.length/2; x++) {  
            int temp = arr[x];  
            arr[x] = arr[arr.length-1-x];  
            arr[arr.length-1-x] = temp;  
        }  
    }  

    public static void reverse2(int[] arr) {  
        for(int start=0,end=arr.length-1; start<=end; start++,end--) {  
            int temp = arr[start];  
            arr[start] = arr[end];  
            arr[end] = temp;  
        }  
    }  

    //遍历数组  
    public static void printArray(int[] arr) {  
        System.out.print("[");  
        for(int x=0; x<arr.length; x++) {  
            if(x == arr.length-1) { //这是最后一个元素  
                System.out.println(arr[x]+"]");  
            }else {  
                System.out.print(arr[x]+", ");  
            }  
        }  
    }  
}  
```
运行结果：

![](https://image.xiaoxiaofeng.site/blog/2023/07/04/xxf-20230704103355.png?xxfjava)

### 10.4 数组基本查找

```java
/*
    需求：数组元素查找(查找指定元素第一次在数组中出现的索引)

    分析：
        A:定义一个数组，并静态初始化。
        B:写一个功能实现
            遍历数组，依次获取数组中的每一个元素，和已知的数据进行比较
            如果相等，就返回当前的索引值。
*/  
class ArrayTest5 {  
    public static void main(String[] args) {  
        //定义一个数组，并静态初始化  
        int[] arr = {200,250,38,888,444};  

        //需求：我要查找250在这个数组中第一次出现的索引  
        int index = getIndex(arr,250);  
        System.out.println("250在数组中第一次出现的索引是："+index);  

        int index2 = getIndex2(arr,250);  
        System.out.println("250在数组中第一次出现的索引是："+index2);  

        int index3 = getIndex2(arr,2500);  
        System.out.println("2500在数组中第一次出现的索引是："+index3);  
    }  

    /*
        需求：查找指定数据在数组中第一次出现的索引
        两个明确：
            返回值类型：int
            参数列表：int[] arr,int value
    */  
    public static int getIndex(int[] arr,int value) {  
        //遍历数组，依次获取数组中的每一个元素，和已知的数据进行比较  
        for(int x=0; x<arr.length; x++) {  
            if(arr[x] == value) {  
                //如果相等，就返回当前的索引值。  
                return x;  
            }  
        }  

        //目前的代码有一个小问题  
        //就是假如我要查找的数据在数组中不存在，那就找不到，找不到，你就对应的返回吗?  
        //所以报错。  

        //只要是判断，就可能是false，所以大家要细心。  


        //如果找不到数据，我们一般返回一个负数即可，而且是返回-1  
        return -1;  
    }  

    public static int getIndex2(int[] arr,int value) {  
        //定义一个索引  
        int index = -1;  

        //有就修改索引值  
        for(int x=0; x<arr.length; x++) {  
            if(arr[x] == value) {  
                index = x;  
                break;  
            }  
        }  

        //返回index  
        return index;  
    }  
}  
```
运行结果：

![](https://image.xiaoxiaofeng.site/blog/2023/06/29/xxf-20230629172252.png?xxfjava)

## 11. 随机点名器

```java
package cn.itcast.chapter02.task03;
import java.util.Random;
import java.util.Scanner;
/**
 * 随机点名器
 */
public class CallName {
	/**
	 * 1.存储全班同学姓名 创建一个存储多个同学姓名的容器（数组） 键盘输入每个同学的姓名，存储到容器中（数组）
	 */
	public static void addStudentName(String[] students) {
		// 键盘输入多个同学姓名存储到容器中
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < students.length; i++) {
			System.out.println("存储第" + (i + 1) + "个姓名：");
			// 接收控制台录入的姓名字符串
			students[i] = sc.next();
		}
	}

	/**
	 * 2.总览全班同学姓名
	 */
	public static void printStudentName(String[] students) {
		// 遍历数组，得到每个同学姓名
		for (int i = 0; i < students.length; i++) {
			String name = students[i];
			// 打印同学姓名
			System.out.println("第" + (i + 1) + "个学生姓名：" + name);
		}
	}

	/**
	 * 3.随机点名其中一人
	 */
	public static String randomStudentName(String[] students) {
		// 根据数组长度，获取随机索引
		int index = new Random().nextInt(students.length);
		// 通过随机索引从数组中获取姓名
		String name = students[index];
		// 返回随机点到的姓名
		return name;
	}

	public static void main(String[] args) {
		System.out.println("--------随机点名器--------");
		// 创建一个可以存储多个同学姓名的容器（数组）
		String[] students = new String[3];
		/*
		 * 1.存储全班同学姓名
		 */
		addStudentName(students);
		/*
		 * 2.总览全班同学姓名
		 */
		printStudentName(students);
		/*
		 * 3.随机点名其中一人
		 */
		String randomName = randomStudentName(students);
		System.out.println("被点到名的同学是 :" + randomName);
	}
}
```

## 12. Arrays 类

java.util.Arrays 类能方便地操作数组，它提供的所有方法都是静态的。

具有以下功能：

- 给数组赋值：通过 fill 方法。
- 对数组排序：通过 sort 方法,按升序。
- 比较数组：通过 equals 方法比较数组中元素值是否相等。
- 查找数组元素：通过 binarySearch 方法能对排序好的数组进行二分查找法操作。

具体说明请查看下表：

| 序号 | 方法和说明                                                   |
| :--- | :----------------------------------------------------------- |
| 1    | **public static int binarySearch(Object[] a, Object key)** 用二分查找算法在给定数组中搜索给定值的对象(Byte,Int,double等)。数组在调用前必须排序好的。如果查找值包含在数组中，则返回搜索键的索引；否则返回 (-(*插入点*) - 1)。 |
| 2    | **public static boolean equals(long[] a, long[] a2)** 如果两个指定的 long 型数组彼此*相等*，则返回 true。如果两个数组包含相同数量的元素，并且两个数组中的所有相应元素对都是相等的，则认为这两个数组是相等的。换句话说，如果两个数组以相同顺序包含相同的元素，则两个数组是相等的。同样的方法适用于所有的其他基本数据类型（Byte，short，Int等）。 |
| 3    | **public static void fill(int[] a, int val)** 将指定的 int 值分配给指定 int 型数组指定范围中的每个元素。同样的方法适用于所有的其他基本数据类型（Byte，short，Int等）。 |
| 4    | **public static void sort(Object[] a)** 对指定对象数组根据其元素的自然顺序进行升序排列。同样的方法适用于所有的其他基本数据类型（Byte，short，Int等）。 |