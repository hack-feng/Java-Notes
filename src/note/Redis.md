### 设置密码
~~~
# 设置密码
CONFIG set requirepass "runoob"
# 查询密码
CONFIG get requirepass
# 密码登录
AUTH password
# 访问其他服务上redis。需要在本地装redis客户端
redis-cli -h ip地址 -p 6379
~~~

### Redis常用的操作
* hash的操作
~~~
> hset hash-key sub-key1 value1
(integer) 1

> hset hash-key sub-key2 value2
(integer) 1

> hset hash-key sub-key1 value1
(integer) 0

> hgetall hash-key
1) "sub-key1"
2) "value1"
3) "sub-key2"
4) "value2"

> hdel hash-key sub-key2
(integer) 1

> hdel hash-key sub-key2
(integer) 0

> hget hash-key sub-key1
"value1"

> hgetall hash-key
1) "sub-key1"
2) "value1"
~~~