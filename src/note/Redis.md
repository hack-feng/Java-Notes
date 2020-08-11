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

### windows 下设置开机自启动

~~~
redis-server --service-install redis.windows-service.conf --loglevel verbose
~~~

### 过期时间操作

* 设置过期时间（单位：秒）
~~~
expire key seconds
~~~

* 设置过期时间（时间戳）
~~~
expireat key unixTime
~~~

* 设置过期时间（单位：豪秒） 
~~~
pexpire key milliseconds
~~~

* 返回剩余过期时间（单位：秒）
~~~
TTL key
~~~

* 返回剩余过期时间（单位：豪秒）
~~~
PTTL key
~~~

* 移除过期时间的设置
~~~
persist key
~~~

### 设置ID递增递减

* key的value每次递增1
~~~
incr key
~~~

* key的value每次递增指定数
~~~
incrby key num
~~~

* key的value每次减1
~~~
encr key
~~~

* key的value每次递减指定数
~~~
encrby key num
~~~

### Redis使用ip地址访问

1. (必须)修改redis服务器的配置文件redis.windows.conf

注释绑定的主机地址
~~~
# bind 127.0.0.1
~~~
2. 修改redis的守护进程为no，不启用
~~~
daemonize "no"
~~~

3. (必须)修改redis的保护模式为no，不启用
~~~
protected-mode"no"
~~~

4. 在Redis根目录下，以管理员身份打开CMD命令窗口
输入redis-server redis.windows.conf，启动服务

5. 在Redis根目录下尝试远程连接并查看
redis-cli -h ip地址 -p 6379 能够连接
info 能够输出信息

### java使用redis递增
如果key在redis中不存在，则会自动初始化生成key，并返回0。
~~~
public Long getIncr(String key){
    RedisAtomicLong redisAtomicLong = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
    return redisAtomicLong.getAndIncrement();
}
~~~