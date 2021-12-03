# IOC容器

## Bean实例化

* 通过构造函数实例化（默认）
* 通过静态工厂方法实例化 factory-method
* 通过实例工厂方法实例化 factory-bean、factory-method

## 依赖注入

* 构造函数注入(推荐)
* 基于Setter方法注入

自动装配模式

* no（默认）没有自动装配，Bean 引用必须由ref元素定义
* byName：按属性名称自动装配。Spring 查找与需要自动装配的属性同名的 bean。
* byType：如果容器中只存在一个属性类型的 bean，则让属性自动装配。如果存在多个，会报错。
* constructor：类似于byType但适用于构造函数参数。