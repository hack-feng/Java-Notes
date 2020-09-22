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

### Mybatis-plus 自动添加创建和修改时间
~~~java
@Component
public class MybatisPlusConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
~~~

bean:
~~~
    /**
     * 创建时间
     */
    @TableField(fill =FieldFill.INSERT)
    private Date createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill =FieldFill.UPDATE)
    private Date updateTime;
~~~

