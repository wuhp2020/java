# maven每次install拉去最新的jar, 并且跳过单测
install -U -Dmaven.test.skip=true -f pom.xml