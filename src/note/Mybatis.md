### Mybatis Plus 配置

~~~
<!-- Application文件 -->
@MapperScan(value="xxx.xxx.mapper")

<!-- properties配置 -->
# 配置mybatis-plus的xml和bean的目录
mybatis-plus.type-aliases-package=com.hege.bean
mybatis-plus.mapper-locations=classpath:mapper/*.xml
~~~

### MyBatisPlus忽略映射字段注解
@TableField(exist = false)：表示该属性不为数据库表字段，但又是必须使用的。

@TableField(exist = true)：表示该属性为数据库表字段。
