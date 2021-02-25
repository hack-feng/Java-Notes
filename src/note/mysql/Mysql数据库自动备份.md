# Linux数据库备份脚本

前提：保存备份文件的服务器需要安装mysql，可以执行mysqldump命令

## 脚本编写（mysql_dump.sh）
 脚本内容：注意一定要在unix环境下编写，如果在windows环境下重定向后文件名会出现问题。
 
 注意替换mysql安装的路径（/usr/local/mysql/bin/mysqldump）和文件保存的路径（/test/dump/）
~~~bash
#! /bin/bash
dump_name=sale_dump_`date +%Y%m%d%H%M%S`
dump_path="/test/dump"
# 判断存放文件目录是否存在
if [ ! -d $dump_path  ];then
  mkdir $dump_path
fi
# 数据库备份
echo $dump_name'备份开始'
/usr/local/mysql/bin/mysqldump -h 127.0.0.1 -P 3306 -u root -p123456 sale_21 > $dump_path/$dump_name
echo $dump_name'备份结束'
~~~

执行 sh mysql_dump.sh，便可以在/test/dump目录下发现备份的数据库文件了

## 定时任务执行

使用linux的crontab命令定时执行备份数据的脚本
~~~
crontab -e
~~~

编辑定时任务，这里每分钟执行一次，编辑完保存即可
~~~
* * * * * /bin/sh /test/mysql_dump.sh
~~~

定时任务的执行最小单位为分钟，详情代表如下：
~~~
*    *    *    *    *
-    -    -    -    -
|    |    |    |    |
|    |    |    |    +----- 星期中星期几 (0 - 6) (星期天 为0)
|    |    |    +---------- 月份 (1 - 12) 
|    |    +--------------- 一个月中的第几天 (1 - 31)
|    +-------------------- 小时 (0 - 23)
+------------------------- 分钟 (0 - 59)
~~~

查看定时任务执行的日志：
~~~
tail -f /var/log/cron
~~~