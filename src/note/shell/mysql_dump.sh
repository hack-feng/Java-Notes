#! /bin/bash
dump_name=sale_dump_`date +%Y%m%d%H%M%S`
dump_path="/test/dump"

if [ ! -d $dump_path  ];then
  mkdir $dump_path
fi

echo $dump_name'备份开始'
/usr/local/mysql/bin/mysqldump -h 127.0.0.1 -P 3306 -u root -p123456 sale_21 > $dump_path/$dump_name
echo $dump_name'备份结束'