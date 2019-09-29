### Mybatis Plus 配置

~~~
<!-- Application文件 -->
@MapperScan(value="xxx.xxx.mapper")

<!-- properties配置 -->
# 配置mybatis-plus的xml和bean的目录
mybatis-plus.type-aliases-package=com.hege.bean
mybatis-plus.mapper-locations=classpath:mapper/*.xml
~~~