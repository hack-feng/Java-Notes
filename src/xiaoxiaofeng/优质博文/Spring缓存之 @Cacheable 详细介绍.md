## 一、简介
### 1、缓存介绍
Spring 从 3.1 开始就引入了对 Cache 的支持。定义了 org.springframework.cache.Cache 和 org.springframework.cache.CacheManager 接口来统一不同的缓存技术。并支持使用 JCache（JSR-107）注解简化我们的开发。

其使用方法和原理都类似于 Spring 对事务管理的支持。Spring Cache 是作用在方法上的，其核心思想是，当我们在调用一个缓存方法时会把该方法参数和返回结果作为一个键值对存在缓存中。

### 2、Cache 和 CacheManager 接口说明

Cache 接口包含缓存的各种操作集合，你操作缓存就是通过这个接口来操作的。

Cache 接口下 Spring 提供了各种 xxxCache 的实现，比如：RedisCache、EhCache、ConcurrentMapCache

CacheManager 定义了创建、配置、获取、管理和控制多个唯一命名的 Cache。这些 Cache 存在于 CacheManager 的上下文中。

### 小结：

每次调用需要缓存功能的方法时，Spring 会检查指定参数的指定目标方法是否已经被调用过，如果有就直接从缓存中获取方法调用后的结果，如果没有就调用方法并缓存结果后返回给用户。下次调用直接从缓存中获取。

## 二、@Cacheable 注解使用详细介绍

### 1、缓存使用步骤
@Cacheable 这个注解，用它就是为了使用缓存的。所以我们可以先说一下缓存的使用步骤：

1、开启基于注解的缓存，使用 @EnableCaching 标识在 SpringBoot 的主启动类上。

2、标注缓存注解即可

① 第一步：开启基于注解的缓存，使用 @EnableCaching 标注在 springboot 主启动类上
![image.png](https://image.xiaoxiaofeng.site/blog/2023/08/15/xxf-20230815103554.png?xxfjava)

② 第二步：标注缓存注解
![image.png](https://image.xiaoxiaofeng.site/blog/2023/08/15/xxf-20230815103558.png?xxfjava)

注：这里使用 @Cacheable 注解就可以将运行结果缓存，以后查询相同的数据，直接从缓存中取，不需要调用方法。

### 2、常用属性说明
下面介绍一下 @Cacheable 这个注解常用的几个属性：

cacheNames/value ：用来指定缓存组件的名字

key ：缓存数据时使用的 key，可以用它来指定。默认是使用方法参数的值。（这个 key 你可以使用 spEL 表达式来编写）

keyGenerator ：key 的生成器。 key 和 keyGenerator 二选一使用

cacheManager ：可以用来指定缓存管理器。从哪个缓存管理器里面获取缓存。

condition ：可以用来指定符合条件的情况下才缓存

unless ：否定缓存。当 unless 指定的条件为 true ，方法的返回值就不会被缓存。当然你也可以获取到结果进行判断。（通过 #result 获取方法结果）

sync ：是否使用异步模式。

① cacheNames

用来指定缓存组件的名字，将方法的返回结果放在哪个缓存中，可以是数组的方式，支持指定多个缓存。

![image.png](https://image.xiaoxiaofeng.site/blog/2023/08/15/xxf-20230815103610.png?xxfjava)

② key

缓存数据时使用的 key。默认使用的是方法参数的值。可以使用 spEL 表达式去编写。
![image.png](https://image.xiaoxiaofeng.site/blog/2023/08/15/xxf-20230815103613.png?xxfjava)

③ keyGenerator
key 的生成器，可以自己指定 key 的生成器，通过这个生成器来生成 key。
![image.png](https://image.xiaoxiaofeng.site/blog/2023/08/15/xxf-20230815103457.png?xxfjava)

![image.png](https://image.xiaoxiaofeng.site/blog/2023/08/15/xxf-20230815103618.png?xxfjava)
这样放入缓存中的 key 的生成规则就按照你自定义的 keyGenerator 来生成。不过需要注意的是：

@Cacheable 的属性，key 和 keyGenerator 使用的时候，一般二选一。

④ condition
符合条件的情况下才缓存。方法返回的数据要不要缓存，可以做一个动态判断。
![image.png](https://image.xiaoxiaofeng.site/blog/2023/08/15/xxf-20230815103457.png?xxfjava)

⑤ unless

否定缓存。当 unless 指定的条件为 true ，方法的返回值就不会被缓存。
![image.png](https://image.xiaoxiaofeng.site/blog/2023/08/15/xxf-20230815103623.png?xxfjava)

⑥ sync

是否使用异步模式。默认是方法执行完，以同步的方式将方法返回的结果存在缓存中。

### 3、spEL 编写 key
前面说过，缓存的 key 支持使用 spEL 表达式去编写，下面总结一下使用 spEL 去编写 key 可以用的一些元数据：

![image.png](https://image.xiaoxiaofeng.site/blog/2023/08/15/xxf-20230815103626.png?xxfjava)