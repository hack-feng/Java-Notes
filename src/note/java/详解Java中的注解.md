在Java中，注解(Annotation)引入始于Java5，用来描述Java代码的元信息，通常情况下注解不会直接影响代码的执行，尽管有些注解可以用来做到影响代码执行。

## 注解可以做什么

Java中的注解通常扮演以下角色

- 编译器指令
- 构建时指令
- 运行时指令

其中

- Java内置了三种编译器指令，本文后面部分会重点介绍
- Java注解可以应用在构建时，即当你构建你的项目时。构建过程包括生成源码，编译源码，生成xml文件，打包编译的源码和文件到JAR包等。软件的构建通常使用诸如Apache Ant和Maven这种工具自动完成。这些构建工具会依照特定的注解扫描Java代码，然后根据这些注解生成源码或文件。
- 通常情况下，注解并不会出现在编译后的代码中，但是如果想要出现也是可以的。Java支持运行时的注解，使用Java的反射我们可以访问到这些注解，运行时的注解的目的通常是提供给程序和第三方API一些指令。

## 注解基础

一个简单的Java注解类似与`@Entity`。其中`@`的意思是告诉编译器这是一个注解。而`Entity`则是注解的名字。通常在文件中，写法如下

```java
public @interface Entity {  } 
```

### 注解元素

Java注解可以使用元素来进行设置一些值，注解中的元素类似于属性或者参数。定义包含元素的注解示例代码

```java
public @interface Entity {   String tableName(); } 
```

使用包含元素的注解示例代码

```java
@Entity(tableName = "vehicles") 
```

上述注解的元素名称为`tableName`，设置的值为`vehicles`。没有元素的注解不需要使用括号。

如果注解包含多个元素，使用方法如下

```java
@Entity(tableName = "vehicles", primaryKey = "id") 
```

如果注解只有一个元素，通常我们的写法是这样的

```java
@InsertNew(value = "yes") 
```

但是这种情况下，当且仅当元素名为value,我们也可以简写，即不需要填写元素名`value`，效果如下

```java
@InsertNew("yes") 
```

## 注解使用

注解可以用来修饰代码中的这些元素

- 类
- 接口
- 方法
- 方法参数
- 属性
- 局部变量

一个完整的使用示例如下

```java
@Entity
public class Vehicle {

    @Persistent
    protected String vehicleName = null;


    @Getter
    public String getVehicleName() {
        return this.vehicleName;
    }

    public void setVehicleName(@Optional vehicleName) {
        this.vehicleName = vehicleName;
    }

    public List addVehicleNameToList(List names) {

        @Optional
        List localNames = names;

        if(localNames == null) {
            localNames = new ArrayList();
        }
        localNames.add(getVehicleName());

        return localNames;
    }
}
```

## 内置的Java注解

Java中有三种内置注解，这些注解用来为编译器提供指令。它们是

- @Deprecated
- @Override
- @SuppressWarnings

### @Deprecated

- 可以用来标记类，方法，属性。
- 如果上述三种元素不再使用，使用@Deprecated注解
- 如果代码使用了@Deprecated注解的类，方法或属性，编译器会进行警告。

@Deprecated使用很简单，如下为注解一个弃用的类。

```java
@Deprecated 
public class MyComponent {

} 
```

当我们使用@Deprecated注解后，建议配合使用对应的@deprecated JavaDoc符号，并解释说明为什么这个类，方法或属性被弃用，已经替代方案是什么。

```java
@Deprecated
/**
  @deprecated This class is full of bugs. Use MyNewComponent instead.
*/
public class MyComponent {

}
```

## @Override

@Override注解用来修饰对父类进行重写的方法。如果一个并非重写父类的方法使用这个注解，编译器将提示错误。

实际上在子类中重写父类或接口的方法，@Overide并不是必须的。但是还是建议使用这个注解，在某些情况下，假设你修改了父类的方法的名字，那么之前重写的子类方法将不再属于重写，如果没有@Overide，你将不会察觉到这个子类的方法。有了这个注解修饰，编译器则会提示你这些信息。

使用Override注解的例子

```java
public class MySuperClass {

    public void doTheThing() {
        System.out.println("Do the thing");
    }
}


public class MySubClass extends MySuperClass{

    @Override
    public void doTheThing() {
        System.out.println("Do it differently");
    }
}
```

## @SuppressWarnings

- @SuppressWarnings用来抑制编译器生成警告信息。
- 可以修饰的元素为类，方法，方法参数，属性，局部变量

使用场景：当我们一个方法调用了弃用的方法或者进行不安全的类型转换，编译器会生成警告。我们可以为这个方法增加@SuppressWarnings注解，来抑制编译器生成警告。

注意：使用@SuppressWarnings注解，采用就近原则，比如一个方法出现警告，我们尽量使用@SuppressWarnings注解这个方法，而不是注解方法所在的类。虽然两个都能抑制编译器生成警告，但是范围越小越好，因为范围大了，不利于我们发现该类下其他方法的警告信息。

使用示例

```java
@SuppressWarnings public void methodWithWarning() { 

} 
```

## 创建自己的注解

在Java中，我们可以创建自己的注解，注解和类，接口文件一样定义在自己的文件里面。如下

```java
@interface MyAnnotation {
    String   value();
    String   name();
    int      age();
    String[] newNames();
}
```

上述代码定义了一个叫做MyAnnotation的注解，它有4个元素。再次强调一下，`@interface`这个关键字用来告诉java编译器这是一个注解。

仔细一看，你会发现，注解元素的定义很类似于接口的方法。这些元素有类型和名称。这些类型可以是

- 原始数据类型
- String
- Class
- annotation
- 枚举
- 一维数组

如下为应用自定义的注解

```java
@MyAnnotation(
    value="123",
    name="Jakob",
    age=37,
    newNames={"Jenkov", "Peterson"}
)
public class MyClass {

}
```

注意，我们需要为所有的注解元素设置值，一个都不能少。

## 注解元素默认值

对于注解中的元素，我们可以为其设置默认值，使用方法为

```java
@interface MyAnnotation {
    String   value() default "";
    String   name();
    int      age();
    String[] newNames();
}
```

上述代码，我们设置了value元素的默认值为空字符串。当我们在使用时，可以不设置value的值，即让value使用空字符串默认值。 使用示例代码



```java
@MyAnnotation(
    name="Jakob",
    age=37,
    newNames={"Jenkov", "Peterson"}
)
public class MyClass {

}
```

## @Retention

@Retention是用来修饰注解的注解，使用这个注解，我们可以做到

- 控制注解是否写入class文件
- 控制class文件中的注解是否在运行时可见

控制很简单，使用使用以下三种策略之一即可。

- RetentionPolicy.SOURCE 表明注解仅存在源码之中，不存在.class文件，更不能运行时可见。常见的注解为@Override, @SuppressWarnings。
- RetentionPolicy.CLASS 这是默认的注解保留策略。这种策略下，注解将存在与.class文件，但是不能被运行时访问。通常这种注解策略用来处于一些字节码级别的操作。
- RetentionPolicy.RUNTIME 这种策略下可以被运行时访问到。通常情况下，我们都会结合反射来做一些事情。

@Retention的使用示例

```java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {
    String   value() default "";
}
```

## @Target

使用@Target注解，我们可以设定自定义注解可以修饰哪些java元素。简单示例

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
public @interface MyAnnotation {
    String   value();
}
```

上述的代码说明MyAnnotation注解只能修饰方法。

@Target可以选择的参数值有如下这些

- ElementType.ANNOTATION_TYPE（注：修饰注解）
- ElementType.CONSTRUCTOR
- ElementType.FIELD
- ElementType.LOCAL_VARIABLE
- ElementType.METHOD
- ElementType.PACKAGE
- ElementType.PARAMETER
- ElementType.TYPE（注：任何类型，即上面的的类型都可以修饰）

## @Inherited

如果你想让一个类和它的子类都包含某个注解，就可以使用@Inherited来修饰这个注解。

```java
java.lang.annotation.Inherited

@Inherited
public @interface MyAnnotation {

}
```

```java
@MyAnnotation 
public class MySuperClass { ... } 
```

```java
public class MySubClass extends MySuperClass { ... } 
```

上述代码的大致意思是

1. 使用@Inherited修饰注解MyAnnotation
2. 使用MyAnnotation注解MySuperClass
3. 实现一个类MySubclass继承自MySuperClass

通过以上几步，MySubClass也拥有了MyAnnotation注解。

关于Java中的注解，一些基本的概念就是这些。