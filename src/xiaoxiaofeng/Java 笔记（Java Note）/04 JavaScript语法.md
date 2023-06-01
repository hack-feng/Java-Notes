+   JavaScript 是基于对象的脚本语言
+   JavaScript 的解释器被称为 JavaScript 引擎，为浏览器的一部分
+   [MDN JavaScript 文档 (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript)
+   由 3 部分组成：ECMAScript、DOM、BOM

![JavaScript对象类型系统](https://image.xiaoxiaofeng.site/blog/2023/04/26/xxf-20230426095846.png?xxfjava)

# JavaScript 用法

1.  在 HTML 页面中嵌入执行 JavaScript 代码
    
    +   使用 `javascript:` 前缀构建执行 JavaScript 代码的 URL，此时调用者为 window 对象
    +   使用 `<script>` 元素来包含 JavaScript 代码
2.  导入 \* .js 文件，`<script>` 与 `<img>` 标签的 src 属性可以是指向当前 HTML 页面**所在域之外**的某个域中的 URL
```html
    <script type="text/javascript" src="index.js"></script>
```


+   JavaScript 严格模式：通过在脚本或函数的头部添加 `"use strict";` 表达式来声明，标准与严格模式的区别： 隐式声明或定义变量、对象重名的属性、arguments.callee、with 语句等

#  数据类型和变量

##  定义变量的方式

1.  隐式定义：直接给变量**赋值**
2.  显式定义：使用 var 关键字创建变量

+   注意：
    +   如果使用 var 定义变量，那么程序会**在代码执行之前**强制**定义**一个新变量，并被添加到最接近的环境中，当运行到为该变量赋值时，才会为该变量赋值
    +   如果没有使用 var 定义变量，系统会在运行到为该变量赋值时，优先在当前上下文中搜索是否存在该变量，只有在该变量不存在的前提下，系统才会**重新定义**一个新变量，并被添加到**全局环境中**（隐式声明或定义变量）
    +   JavaScript 变量均为对象，当声明一个变量时，就创建了一个新的对象
    +   **JavaScript 变量提升**：JavaScript 中，**函数**及**变量**的**声明**都将被提升到**函数的最顶部**，而变量初始化（即变量的初始化）的不会

## 类型转换

+   自动类型转换
    +   对于减号运算符，因为字符串不支持减法运算，所以系统自动将字符串转换成数值
    +   对于加号运算符，因为字符串可用加号作为连接运算符，所以系统自动将数值转换成字符串，并将两个字符串进行连接运算
+   强制类型转换：toString()、parselnt()、parseFloat()

##  变量的作用范围

+   JavaScript 使用静态作用域（词法作用域），由程序定义的位置决定，与代码的执行顺序无关
+   全局变量：在函数外定义的变量
+   局部变量：在**函数里定义**的变量，只能在函数里访问，且**与定义的位置无关**
+   如果局部变量和全局变量的变量名相同，则局部变量将覆盖全局变量
+   没有块级作用域
+   ES5 中使用词法环境管理静态作用域

###  词法环境

+   组成：1. 环境记录（形参、函数、变量等）；2. 对外部词法环境的引用（outer)
+   一段代码开始**执行前**，先初始化词法环境（形参、函数定义、变量定义）

#  基本数据类型（原始类型）（5 种）

+   每当读取一个基本类型值的时候，后台就会自动创建一个对应的基本包装类型的对象，这个对象**只存在于一行代码的执行瞬间**，然后立即被销毁，因此不能在运行时为基本类型值添加属性和方法

## Number 类型

+   整数、浮点数
+   支持用科学计数法表示
+   特殊值：Infinity（正无穷大）、-Infinity（负无穷大）、NaN（不是一个数）
+   JavaScript 中能**精确表示**的最大整数是 `Math.pow(2, 53)`，即 9007199254740992
    +   `Number.MAX_SAFE_INTEGER` 最大安全整数，为 9007199254740991
    +   `Number.MIN_SAFE_INTEGER` 最小安全整数，为 -9007199254740991

##  String 类型

+   由单引号或双引号括起来的字符序列
+   比较两个字符串的字符序列是否相等可以直接使用 ==
+   "" 分别转换为：false、0

## Boolean 类型

+   只有两个字面值：true、false
+   类型转换：true 分别转换为：1、"true"；false 分别转换为：0、"false"
+   0、-0、""、false、NaN、undefined、null 都转换为 false

##   Undefined 类型

+   只有一个值：undefined
+   出现场景：已声明未赋值的变量、获取对象不存在的属性、无返回值的函数的执行结果、函数的参数没有传入、`void(expression)`
+   类型转换：undefined 分别转换为：false、NaN、"undefined"

## Null 类型

+   只有一个值：null，表示对象不存在
+   类型转换：null 分别转换为：false、0、"null"

## 复合类型

+   由多个基本数据类型（也可以包括复合类型）组成的数据体

## Object 对象

+   对象是一组命名变量、函数的集合
+   类型转换：{} 分别转换为：true、NaN、"\[object Object\]"

### 类型识别

1.  typeof：可以识别标准类型（Null 除外），不能识别具体的对象类型（Function 除外）
2.  Object.prototype.toString：可以识别标准类型以及内置（build-in）对象类型，不能识别自定义类型
3.  constructor：可以识别标准类型（Undefined/Null 除外）、内置对象类型、自定义对象类型
4.  instanceof：可以判别内置对象类型、自定义对象类型，不能判别原始类型（标准类型）

```javascript
function type(obj){
    return Object.prototype.toString.call(obj).slice(8, -1).toLowerCase()；
}

// 获取对象构造函数名称*
function getConstructorName(obj){
    return obj&&obj.constructor && obj.constructor.toStringO.match(/function\s*([~(]*)/)[1]；
}
```

## Array 数组

+   数组是一系列的变量

```javascript
var arr1 = [1, 3, 5, 7]; // 使用 JSON 语法创建
var arr2 = [];
var arr3 = new Array(1, 3, 5, 7); // 创建数组对象
var arr3 = new Array(4); // 4 为数组长度
```

+   数组长度允许动态改变，通过 length 属性可得到或改变数组的长度
+   同一个数组中的元素类型可以互不相同
+   访问数组元素时不会产生数组越界，访问并未赋值的数组元素时，该元素的值为undefined

##  Function 函数

+   用于定义函数或者新对象构造器

# 运算符

+   赋值运算符：= += -= 等
+   算术运算符：+ - \* / % ++
+   位运算符：& | ~ ^ << >> >>>
+   比较运算符：> >= < <= != ==
    +   \=== ：严格等于，值和类型均相等，才返回 true
    +   !== ：严格不等于，值和类型均不相等，才返回 true
    +   字符串的比较规则是按字母的 Unicode 值进行比较
+   逻辑运算符：&& || !
    +   在逻辑运算中，0、-0、""、false、NaN、undefined、null 都表示 false，其他所有的数据都表示 true
    +   && 运算符：从左往右依次判断，返回第一个为 false 的值，否则返回最后一个值，即返回第一个能判断整个表达式的**值**
    +   || 运算符：从左往右依次判断，返回第一个为 true 的值，否则返回最后一个值，即返回第一个能判断整个表达式的**值**
+   三目运算符：? :
+   逗号运算符：用于将多个表达式排在一起，整个表达式返回最右边表达式的值
+   对象运算符：new delete . \[\] instanceof
+   void 运算符：用于强行指定表达式不会返回值
+   typeof 和 instanceof 运算符
    +   typeof 用于判断某个变量的**数据类型**，不同类型参数使用 typeof 运算符的返回值类型如下：  
        undefined 值：undefined；布尔型值：boolean；数字型值：number；字符串值：string；对象或 null ：object；函数：function
    +   instanceof 用于判断某个变量是否为指定类的实例（根据它的原型链来识别），使用 instanceof 判断基本数据类型值都返回 false
+   in 操作符，用于判断指定的属性是否存在于实例或原型中

# 语句

## 条件控制语句

+   if 语句、switch 语句

> switch 语句会使用**严格等于**计算符（===）进行比较

## 循环控制语句

+   while 循环、do while 循环、for 循环
    
+   for in 循环（循环遍历对象的属性，包括原型对象上的属性）
    
```javascript
    for (index in object) {
        // 逻辑代码
    }
```

+   当遍历数组时，for in 循环中的 index 是数组元素的**索引**
    
+   当遍历对象时，for in 循环中的 index 是是该对象的**属性名**
    
+   关键字：break 和 continue
    

##  异常处理语句

+   try/catch/finally
+   throw

##  with 语句

+   通常用于缩短特定情形下必须写的代码量
+   暂时改变变量的作用域链，将 with 语句中的对象添加到作用域链的头部
+   with 语句可能导致执行性能下降，应尽可能的避免使用

# 函数

+   函数是由事件驱动的或者当它被调用时执行的可重复使用的代码块

## 定义函数的方式

+   **函数名**就是函数的唯一标识（一个指向该函数的指针），对于两个同名的函数，后面定义的函数覆盖前面定义的函数
+   定义函数时无须声明返回值类型，也无须声明形参类型
+   调用时，JavaScript 会将没有传入实参的参数值自动设置为 undefined 值（支持空参数）
+   如果函数无明确的返回值，或调用了没有参数的 return 语句，那么它真正返回的值是 undefined
+   函数对参数执行逻辑操作之前，应先判断参数类型，并判断参数是否包含了需要访问的属性、方法
+   通过参数类型检查实现函数重载
+   JavaScript 函数既是一个函数，也可作为一个方法，还是一个对象，并可作为一个类使用，而且还是该类唯一的构造器

```javascript
// 方式1：函数声明，在执行代码之前会先读取函数声明，可以先使用，后声明
// 定义命名函数，同时也定义了一个类
function 函数名([参数名称1, 参数名称2, ...]){
    // 函数体
    [return 值;]
}

// 直接调用函数（以函数附加的对象作为调用者），返回值是该函数的返回值
函数名(值1, 值2, ...);
// 将函数作为类的构造器，返回值是该类的一个对象
new 函数名(值1, 值2, ...);

// 方式2：函数表达式
// 定义匿名函数，再赋值给变量 
var f = function([参数名称1, 参数名称2, ...]) {
    // 函数体
};  // 最后要加上分号（;）

// 调用
f(值1, 值2, ...);
```

## 函数中的变量

+   局部变量：以 var 或不加任何前缀声明的变量，只能在函数里访问
+   实例属性：在函数中以 **this** 前缀修饰的变量，必须通过对象来访问
+   类属性：在函数中以**函数名**前缀修饰的变量，必须通过类（也就是函数）来访问

## 函数的参数传递

+   基本类型传递的是值的副本，引用类型传递的是内存地址值的副本
+   复合类型的变量只是一个引用，该引用指向实际的 JavaScript 对象

## 函数内部属性

### arguments 对象

+   类数组对象，包含着传入函数中的所有参数，可以通过 arguments 对象访问传入的参数（第一个参数是 arguments\[0\]），检测参数个数（arguments.length），通过判断传入函数中参数的类型和数量并作出不同的反应，可以**模仿**函数的重载
+   属性 callee，指向拥有 arguments 这个对象的函数，可以使用 `arguments.callee()` 来实现对函数的递归调用

### this 对象

+   this 指向调用该方法的对象
+   通过 `call(thisArg, arg1, arg2, ...)` 或 `apply(thisArg, argArray)` 可以将函数体内 this 对象改为 thisArg

## 函数表达式

+   匿名函数：没有名字的函数表达式（可以用来模仿块级作用域，避免向全局作用域中添加变量或函数）
    
+   闭包（closure）：指有权访问另一个函数作用域中的变量的函数（可以用来私有化变量、让一个变量常驻内存）
    
```javascript
    // 定义一个计数器
    (function () {
        // 这里是块级作用域
        var id = 1;
        // 把获取 id 的方法添加到 window 对象上，变量 id 因为被引用着所以不会被回收
        window.getId = function () {
            return id++;
        }
    })();
```


#  对象

+   对象是无序的属性的集合， 其属性值可以是基本值、对象或者函数

## 使用对象

+   JavaScript 中的对象本质上是一个**关联数组**，类似 Java 里的 Map 数据结构，由一组 key-value 对组成
+   当对象的 value 是基本类型的值或者复合类型的值时，此时的 value 是该对象的属性值
+   当对象的 value 是函数，此时的函数是该对象的方法
+   允许为**对象**动态地增加属性和方法，为对象添加方法时，不要在函数后添加括号（一旦添加了括号，将表示要把函数的返回值赋给对象的属性），`obj.attrName=value;` 或 `obj['attrName']=value;`
+   允许为**类**动态地增加属性和方法，通过将属性或方法增加到**原有类的 prototype 属性**上
+   使用**方括号表示法**，支持通过**变量**来访问属性，或者访问包含特殊字符（如空格等）的属性
+   访问对象属性：`obj.attrName` 或 `obj['attrName']`
+   删除对象属性：`delete obj.attr;` 或 `delete obj['attr'];`
+   对象的克隆：使用 for in 循环来遍历并拷贝属性值

##  创建对象

###  使用 new 关键字调用构造器创建对象

+   JavaScript 中所有的函数都可以作为构造器使用，使用 new 调用函数后返回值是一个对象
+   每创建一个函数，就会同时创建它的 prototype 对象，因此每个构造函数都有一个 prototype 属性，这个属性指向该构造函数的**原型对象**
+   所有函数的默认原型都是 Object 的实例
+   原型对象中除了包含 constructor 属性之外，还包括后来添加的其它属性和方法
+   每个对象都有 constructor 属性，该属性指向其构造函数，如 Person.prototype.constructor 指向 Person
+   Firefox、Safari 和 Chrome 在每个对象上都支持`__proto__` 属性，该属性指向构造函数的原型对象

```javascript
// 创建自定义类型的方式
// 构造函数模式用于定义实例属件，初始化原型对象
function Person(name, gender) {
    this.name = name; // this 代表当前对象
    this.gender = gender;
};
// 原型模式用于定义方法和共享的属性
Person.prototype = {
    constructor : Perosn,
    sayName : function() {
        alter(this.name);
    }
};

// 创建一个 Person 实例
var p = new Person('sdky', 'male');
```

###  使用 Object 直接创建对象

+   `var myObj = new Object();`
+   `Object.create(proto[, propertiesObject])`：使用指定的原型对象和属性创建一个对象

###  使用 JSON 语法创建对象

+   使用花括号，然后将每个属性写成“key:value”对的形式
+   属性值可以是基本数据类型、函数、数组，甚至可以是另外一个 JSON 语法创建的对象

```javascript
var p = {
    name: 'sdky',
    gender: 'male',
    age: 23  // 最后一个属性定义后没有英文逗号
};
```

## 继承

+   通过原型链实现继承：重写原型对象，让原型对象等于另一个类型的实例 `SubType.prototype = new SuperType();`

![原型链](https://image.xiaoxiaofeng.site/blog/2023/04/26/xxf-20230426095846.jpeg?xxfjava)

+   借用构造函数实现继承：`function SubType(){ SuperType.call(this); )`
    
+   组合继承：先通过借用构造函数来实现对实例属性的继承，再使用原型链实现对原型属性和方法的继承
    
```javascript
    function SuperType(name) {
        this.name = this.name;
        this.colors = ["red", "blue", "green"];
    }
    
    SuperType.prototype.sayName = function () {
        alert(this.name);
    };
    
    function SubType(name, age) {
        // 继承属性
        SuperType.call(this, name);
        this.age = age;
    }
    
    // 继承方法
    SubType.prototype = new SuperType();
    
    SubType.prototype.sayAge = function () {
        alert(this.age);
    };
```


#  ECMAScript 对象类型

+   在 ECMAScript 中，所有对象并非同等创建的。
+   一般来说，可以创建并使用的对象有三种：本地对象、内置对象和宿主对象

## 本地对象

+   ECMA-262 把本地对象（native object）定义为“独立于宿主环境的 ECMAScript 实现提供的对象”。简单来说，本地对象就是 ECMA-262 定义的类（引用类型）
+   包括：Object、Function、Array、String、Boolean、Number、Date、RegExp、Error、EvalError、RangeError、ReferenceError、SyntaxError、TypeError、URIError

### Object

+   属性：constructor、prototype
    
+   静态方法 `Object.create(proto[, propertiesObject])`：使用指定的原型对象 proto 和属性创建一个对象
    
+   对象方法 `toString()`：返回一个表示该对象的字符串，如果此方法在自定义对象中未被覆盖，`toString()` 返回 "\[object type\]"，其中 type 是对象的类型  
    `hasOwnProperty(property)`：判断该对象是否有指定的属性（**不包括**原型对象存在的属性）
    

###  Function

+   属性：length（函数的形参个数）、prototype
+   对象方法 `apply(thisArg, [argsArray])`：通过参数指定函数调用者和函数参数并**执行该函数**  
    `bind(thisArg, arg1, arg2, ...)`：通过参数指定函数调用者和函数参数并**返回该函数引用**（不执行）

###  Array

+   属性：index、length
    
+   静态方法 `isArray(obj)`：判断对象 obj 是否为数组
    
+   对象方法 `concat(arrayX, arrayX, ...)`：返回连接两个或更多的数组得到的**新数组**  
    `join(separator)`：把数组的所有元素放入一个字符串，元素通过指定的分隔符进行分隔 `indexOf(item, start)`：返回某个指定的元素在数组中首次出现的位置，如果没找到指定元素则返回 -1  
    `lastIndexOf(item, start)`  
    `pop()`：删除并返回数组的最后一个元素  
    `push(newelement1, newelement2, ...)`：向数组的末尾添加一个或更多元素，并返回新的长度  
    `shift()`：删除并返回数组的第一个元素  
    `unshift(newelement1, newelement2, ...)`：向数组的开头添加一个或更多元素，并返回新的长度  
    `reverse()`：颠倒数组中元素的顺序  
    `sort(sortfunction)`：对数组的元素进行排序，可用 sortfunction 规定排序顺序（注意：会改变原始数组）  
    `slice(start, end)`：从某个已有的数组返回选定的元素组成的**新数组**，\[start, end) ，如果参数是负数，则规定的是从数组尾部开始算起的位置（即 -1 指最后一个元素）  
    `splice(index, howmany, element1, ...)`：从索引 index 开始，删除 howmany 个元素，再在此位置上添加新元素 element1, ...  
    `forEach(function(currentValue, index, arr)[, thisValue])`：遍历数组的每个元素，并将元素传递给回调函数 `filter(function(currentValue, index, arr)[, thisValue])`：创建一个新的数组，新数组中的元素是**通过 function 检查**指定数组中符合条件的所有元素
    
```javascript
    // 参数：value 数组中的当前项，index 当前项的索引，array 原始数组
    // 遍历数组元素，无返回值
    forEach(function(value, index, array) {
        // do something
    })
    
    // 参数：value 数组中的当前项，index 当前项的索引，array 原始数组
    // 遍历数组元素，返回一个对应的新数组，新数组中的元素是 xxx
    map(function(value, index, array) {
        // do something
        return xxx;
    })
```


###  String

+   属性：length
    
+   构造函数 `new String(s)`：返回创建的 String 对象  
    `String(s)`：返回原始字符串
    
+   对象方法 `charAt(index)`：获取字符串特定索引处的字符，从 0 开始  
    `indexOf(searchvalue, fromindex)`：获取某个指定的字符串值在字符串中首次出现的位置，如果没有找到匹配的字符串则返回 -1  
    `lastIndexOf(searchvalue, fromindex)`  
    `fromCharCode(numX, numX, ...)`：类方法，将一系列 Unicode 值转换成字符串  
    `replace(regexp/substr, replacement)`：替换与正则表达式匹配的子串  
    `split(separator, howmany)`：把字符串分割为字符串数组  
    `slice(start, end)`：提取字符串的某个部分，并以**新的字符串**返回被提取的部分，\[start, end)，如果参数是负数，则规定的是从字符串的尾部开始算起的位置（即 -1 指最后一个字符）  
    `substring(start, stop)`：用于提取字符串中介于两个指定下标之间的字符（不接受负的参数）
    

### Boolean

### Number

+   构造函数 `new Number(value)`：返回创建的 Number 对象 `Number(value)`：返回原始数值
    
+   对象方法 `toFixed(num)`：四舍五入为指定小数位数（num）的数字
    

###  Date

+   构造函数 `new Date()`：返回创建的表示当前的日期和时间 Date 对象 `Date()`：返回当前的日期和时间**字符串** `new Date(value)`：value 表示自 1970-1-1 00:00:00 UTC 起经过的毫秒数 `new Date(dateString)`：dateString 的格式："1970-01-01 00:00:00 UTC+8" `new Date(year, month, [day, [hour, [minutes, [seconds, [milliseconds]]]]])`：month 介于 0（一月）~ 11（十二月）之间
    
+   静态方法 `Date.parse(dateString)`：解析一个日期时间字符串，并返回 1970/1/1 午夜距离该日期时间的毫秒数，dateString 的格式可以为："1970-01-01 00:00:00 UTC"
    
+   对象方法 `getFullYear()`：以四位数字返回年份 `getMonth()`：返回月份 (0 ~ 11) `getDate()`：返回一个整数，表示一月中的某一天（1 ~ 31） `getDay()`：返回一个整数，表示一周中的某一天（0 ~ 6，0 表示星期日，6 表示星期六） `getHours()`：返回 Date 对象的小时 (0 ~ 23) `getMinutes()`：返回 Date 对象的分钟 (0 ~ 59) `getSeconds()`：返回 Date 对象的秒数 (0 ~ 59) `getTime()`：返回 1970 年 1 月 1 日至今的毫秒数 `setFullYear(year, month, day)`：设置年份 `setMonth(month, day)`：设置月份，month 介于 0（一月）~ 11（十二月）之间 `setDate(day)`：设置一个月的某一天 `setHours(hour, min, sec, millisec)`：设置指定的时间的小时字段 `setMinutes(min, sec, millisec)` `setSeconds(sec, millisec)` `setTime(millisec)`：以毫秒设置 Date 对象 `toLocaleString()`：根据本地时间格式，把 Date 对象转换为字符串 `toLocaleTimeString()`：根据本地时间格式，把 Date 对象的时间部分转换为字符串 `toLocaleDateString()`：根据本地时间格式，把 Date 对象的日期部分转换为字符串
    
```javascript
    var date = new Date();
    var day = date.getDate();
    day = day < 10 ? "0" + day : day;
    var month = date.getMonth() + 1;
    month = month < 10 ? "0" + month : month;
    var time = date.getFullYear() + "-" + month + "-" + day + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
```


###  RegExp

+   正则表达式，一个 RegExp 对象包含一个正则表达式和关联的标志
+   直接量语法：`/pattern/attributes`
+   创建 RegExp 对象的语法：`new RegExp(pattern, attributes);`
+   参数 attributes 是一个可选的字符串，包含属性 "g"、"i" 和 "m"，分别用于指定全局匹配、区分大小写的匹配和多行匹配
+   对象方法 `test(string)`：检索字符串 string 中是否含有与 RegExp 对象匹配的文本，返回 true 或 false

##  内置对象

+   ECMA-262 把内置对象（built-in object）定义为“由 ECMAScript 实现提供的、独立于宿主环境的所有对象，在 ECMAScript 程序开始执行时出现”。这意味着开发者不必明确实例化内置对象，它已被实例化了
+   ECMA-262 只定义了两个内置对象，即 `Global` 和 `Math`（它们也是本地对象，根据定义，每个内置对象都是本地对象）

###  全局对象（Global 对象）

+   全局变量和函数都是全局对象的属性
+   Web 浏览器将这个全局对象作为 window 对象的一部分，因此在全局作用域中声明的所有变量和函数，都成为了 window 对象的属性

#### 全局变量

+   undefined、Infinity、NaN、Object、Array、Function、Boolean、String、Number、Date、RegExp、Error 等

####  全局函数

+   `decodeURIComponent(URIstring)`：解码一个编码的 URI 组件
+   `encodeURIComponent(URIstring)`：把字符串编码为 URI 组件
+   `decodeURI(URIstring)`：解码某个编码的 URI
+   `encodeURI(URIstring)`：把字符串编码为 URI，对以下在 URI 中具有特殊含义的 ASCII 标点符号，该函数是**不会进行转义**的：, / ? : @ & = + $ #（用于**分隔 URI 组件**的标点符号）
+   `eval(string)`：把字符串作为 JavaScript 代码来执行
+   `isFinite(number)`：判断某个变量是否为有穷大的数
+   `isNaN(x)`：判断某个变量是否为 NaN
+   `parseInt(string, radix)`：解析一个字符串并返回一个整数，radix 可选，要解析的数字的基数
+   `parseFloat(string)`：解析一个字符串并返回一个浮点数

### Math 对象

+   Math 对象是拥有一些属性和方法的单一对象，主要用于数字计算
+   属性：E、PI、SQRT2 等
+   `Math.random()`：返回介于 0 ~ 1 之间的一个随机数，\[0.0, 1.0)
+   `Math.ceil(x)`：向上取整（大于等于 x，并且与它最接近的整数）
+   `Math.floor(x)`：向下取整（小于等于 x，且与 x 最接近的整数）
+   `Math.round(x)`：把数四舍五入为最接近的整数（与 x 最接近的整数）

###  JSON 对象

+   用于存储和交换文本信息
+   `JSON.stringify(对象或数组)`：把 JavaScript 对象序列化为 JSON 字符串
+   `JSON.parse(jsonString)`：把 JSON 字符串解析为原生 JavaScript 值

## 宿主对象

+   所有非本地对象都是宿主对象（host object），即由 ECMAScript 实现的宿主环境提供的对象
+   所有 BOM 和 DOM 对象都是宿主对象

