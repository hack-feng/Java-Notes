解除正在死锁的状态有两种方法：

## 方法一

1.查询是否锁表

show OPEN TABLES where In_use > 0;

2.查询进程(如果您有SUPER权限可以看到所有线程。否则只能看到自己帐号下的线程)

show processlist;

3.杀死进程id(就是上面命令查到的id)

kill id;

## 方法二

1.查看下在锁的事务

SELECT * FROM INFORMATION_SCHEMA.INNODB_TRX;

2.杀死进程id(就是上面命令查到的trx_mysql_thread_id列)

kill 线程ID

查出死锁进程：SHOW PROCESSLIST;

杀掉进程          KILL 3203;

其它关于查看死锁的命令：

1：查看当前的事务

SELECT * FROM INFORMATION_SCHEMA.INNODB_TRX;

2：查看当前锁定的事务

SELECT * FROM INFORMATION_SCHEMA.INNODB_LOCKS;

3：查看当前等锁的事务

SELECT * FROM INFORMATION_SCHEMA.INNODB_LOCK_WAITS;
