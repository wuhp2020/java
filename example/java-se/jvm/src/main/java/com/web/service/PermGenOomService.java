package com.web.service;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/9
 * @ Desc   : 描述
 */
public class PermGenOomService {
    public static void main(String[] args) {
        URL url = null;
        List<ClassLoader> classLoaderList = new ArrayList<>();
        try {
            url = new File("/tmp").toURI().toURL();
            URL[] urls = {url};
            while(true) {
                ClassLoader loader = new URLClassLoader(urls);
                classLoaderList.add(loader);
                loader.loadClass("com.web.service.Test");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}


class Test {}