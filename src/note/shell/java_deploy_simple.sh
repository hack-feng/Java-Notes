#!/bin/bash

javaName=sale-manage-1.0.0


ps -ef|grep "$javaName" |grep 'java' |awk '{print $2}'| while read pid
do
  kill -9 $pid
  echo '关闭进程:'$pid
done
nohup java -Xms512m -Xmx1024m -jar "$javaName".jar & 

echo '启动成功'

