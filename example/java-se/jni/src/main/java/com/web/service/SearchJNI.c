#include <jni.h>
#include <stdio.h>
#include "com_web_service_SearchJNI.h"

JNIEXPORT void JNICALL Java_com_web_service_SearchJNI_searchByLinux(JNIEnv *env, jobject thisObj){
    printf("Hello, Linux JNI!");
    return;
}

JNIEXPORT void JNICALL Java_com_web_service_SearchJNI_searchByMac(JNIEnv *env, jobject thisObj){
    printf("Hello, Mac JNI!");
    return;
}

JNIEXPORT void JNICALL Java_com_web_service_SearchJNI_searchByWindows(JNIEnv *env, jobject thisObj){
    printf("Hello, Windows JNI!");
    return;
}