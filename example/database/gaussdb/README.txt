部署:
docker run -itd --name gaussdb --privileged=true -e GS_PASSWORD=Secretpassword@123 -p 15432:5432 enmotech/opengauss:latest

登录:
su - omm
gsql -d postgres -U gaussdb -W'Secretpassword@123' -h 127.0.0.1 -p5432

#####################################

创建表:
