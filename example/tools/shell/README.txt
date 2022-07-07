#!/bin/bash

# grep后取第一个参数
docker images | grep acg | grep -v grep | awk '{print $1}'

#####################################

# 统计文件中指定字符数量
grep 'image' values.yaml | wc -l

#####################################

# mysql客户端同步数据

#!/bin/bash

# 临时数据存放目录
tempdata=/home/setup/scripts/result.data
tempsql=/home/setup/scripts/result.sql
# 记录id的文件不能删除
tempid=/home/setup/scripts/result.id

# 初始化id
if [ ! -f "$tempid" ]
then
touch "$tempid"
fi

if [ -s $tempid ]
then
echo '===== 开始同步 ====='
else
echo '===== 没有id, 初始化为: 1 ====='
echo 1 > $tempid
fi

while true
do

# 需要同步抓拍数据的起始id
id=$(cat $tempid | grep -v grep | awk '{print $1}')

# 先清理导出的数据
rm -rf $tempdata
rm -rf $tempsql

# 从视频人像导出数据
/home/setup/scripts/mysql -h127.0.0.1 -uroot -P3306 -p123 -Dabf -N -e "select feature_code,create_time,HEX(feature),camera_id,bg_image_id,capture_time,capture_place,image_id,image_quality,'11' from face_1_e61e7210b4d74109b7c3656091e8dae6 where id >= $id limit 100" > $tempdata

# 判断是否导出数据, 没有数据则继续导出
line=$(wc -l $tempdata | grep -v grep | awk '{print $1}')
if [ "$line" -eq "0" ]
then
echo '===== 没有导出数据, 继续执行 ====='
else

# 将数据处理为sql
cat $tempdata | while read line
do
  object_id=`echo $line | awk '{print $1}'`
  create_time=`echo $line | awk '{print $2}'`
  feature=`echo $line | awk '{print $3}'`
  cameraId=`echo $line | awk '{print $4}'`
  backgroundImageId=`echo $line | awk '{print $5}'`
  snapshotTime=`echo $line | awk '{print $6}'`
  place=`echo $line | awk '{print $7}'`
  image_id=`echo $line | awk '{print $8}'`
  image_quality=`echo $line | awk '{print $9}'`
  fileType=`echo $line | awk '{print $10}'`
  echo $object_id
  echo "insert into Archive00000020200729Collection (object_id, create_time, feature,cameraId, backgroundImageId, snapshotTime, place, image_id, image_quality, fileType) values ('"$object_id"', "$create_time", 0x"$feature", '"$cameraId"', '"$backgroundImageId"', "$snapshotTime", '"$place"', '"$image_id"', '"$image_quality"', '"$fileType"');" >> $tempsql
done

echo '===== 开始导入 ====='
# 导入到一人一档库
/home/setup/scripts/mysql -h127.0.0.1 -uroot -P3306 -p123 -Dabf <<EOF
source $tempsql
EOF

# 记录导出的数据id
id=$(($id+$line))
echo '===== 当前同步到id: '$id' ====='
echo $id > $tempid

fi

sleep 10
done

#####################################

head -n 10 values.yaml
tail -n 10 values.yaml