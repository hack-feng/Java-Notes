#!/bin/bash

# 重启项目
restart="restart"
# 将现有的项目备份到history，将temp项目移至启动目录，启动项目
start="start"
# 停止运行的项目
stop="stop"
# 项目名称
javaName=sale-manage-1.0.0
# 存放需要更新的包的位置
temp="/app/qhshapp/back/product/temp/"
# 存放历史包位置
history="/app/qhshapp/back/product/history/"
dump_name=`date +%Y%m%d%H%M%S`

if [ "$1" = "$restart" ]; then
  if [ ! -f "$javaName.jar" ]; then
    ehco "$javaName.jar不存在"
    exit 0
  fi
elif [ "$1" == "$start" ]; then
  if [ ! -f "$temp$javaName.jar" ]; then
    echo "$temp$javaName.jar不存在"
    exit 0
  fi
  
  if [ ! -d "$history" ]; then
    mkdir "$history"
	echo "$history 目录不存在，已自动创建"
  fi
elif [ "$1" == "$stop" ]; then
  echo "准备停止项目"
else
  echo "请输入进行的操作参数 start:重新部署启动|restart:重启项目|stop:停止项目"
  exit 0
fi

# 关闭运行中的项目
ps -ef|grep "$javaName" |grep 'java' |awk '{print $2}'| while read pid
do
  if [ -z "$pid" ]; then 
    echo '没有进程正在运行'
  else
    kill -9 $pid
    echo '关闭进程:'$pid
  fi
done

if [ "$1" == "$restart" ]; then
  nohup java -Xms512m -Xmx1024m -jar "$javaName".jar & 
  echo '项目重新启动成功，即将打开运行日志'
  tail -f nohup.out
elif [ "$1" == "$start" ]; then
  if [ -f "$javaName.jar" ]; then
    mv $javaName.jar "$history$javaName$dump_name".jar
  fi
  mv $temp$javaName.jar $javaName.jar
  nohup java -Xms512m -Xmx1024m -jar "$javaName".jar & 
  echo "项目启动成功，即将打开运行日志"
  tail -f nohup.out
elif [ "$1" == "$stop" ]; then
  echo '项目关闭成功'
else
  echo "请输入进行的操作参数 start:重新部署启动|restart:重启项目|stop:停止项目"
fi
