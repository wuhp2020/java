1. 编译
javac SearchJNI.java
javac -h . SearchJNI.java

2. 生成
com_web_service_SearchJNI.h

3. 创建
SearchJNI.c

4. 实现
# 追加
#include "com_web_service_SearchJNI.h"
JNIEXPORT void JNICALL Java_com_web_service_SearchJNI_searchByLinux(JNIEnv *env, jobject thisObj){
    printf("Hello, Linux JNI!");
    return "Linux JNI";
}

5.编译
gcc -I/Library/Java/JavaVirtualMachines/jdk1.8.0_311.jdk/Contents/Home/include/ -I/Library/Java/JavaVirtualMachines/jdk1.8.0_311.jdk/Contents/Home/include/darwin/ -dynamiclib SearchJNI.c -o SearchJNI.so

