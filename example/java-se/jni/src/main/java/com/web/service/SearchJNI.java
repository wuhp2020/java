package com.web.service;

public class SearchJNI {

    static {
        System.load("/Users/mac/java/example/jni/src/main/java/com/web/service/SearchJNI.so");
    }

    public static native void searchByLinux();
    public static native void searchByMac();
    public static native void searchByWindows();
}
