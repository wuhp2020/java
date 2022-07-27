# maven每次install拉去最新的jar, 并且跳过单测
install -U -Dmaven.test.skip=true -f pom.xml

#####################################

mvn install:install-file -Dfile=crudapi-core-1.6.1.jar -DgroupId=cn.crudapi -DartifactId=crudapi-core -Dversion=1.6.1 -Dpackaging=jar
mvn install:install-file -Dfile=crudapi-api-1.6.1.jar -DgroupId=cn.crudapi -DartifactId=crudapi-api -Dversion=1.6.1 -Dpackaging=jar
mvn install:install-file -Dfile=crudapi-rest-1.6.1.jar -DgroupId=cn.crudapi -DartifactId=crudapi-rest -Dversion=1.6.1 -Dpackaging=jar
mvn install:install-file -Dfile=crudapi-security-1.6.1.jar -DgroupId=cn.crudapi -DartifactId=crudapi-security -Dversion=1.6.1 -Dpackaging=jar
mvn install:install-file -Dfile=crudapi-weixin-1.6.1.jar -DgroupId=cn.crudapi -DartifactId=crudapi-weixin -Dversion=1.6.1 -Dpackaging=jar

