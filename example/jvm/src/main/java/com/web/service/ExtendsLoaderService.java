package com.web.service;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/7
 * @ Desc   : 描述
 */
public class ExtendsLoaderService extends ClassLoader {

    public static void main(String[] args) throws Exception {
        ExtendsLoaderService loaderService = new ExtendsLoaderService();
        Class<?> cls = loaderService.loadClass(ExtendsServiceA.class.getName());
        System.out.println(cls.getClassLoader());
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                try {
                    //修改classloader的原双亲委派逻辑, 从而打破双亲委派
                    if (name.startsWith("com.web.service")){
                        c = findClass(name);
                    }
                    else {
                        c = this.getParent().loadClass(name);
                    }
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found
                    // from the non-null parent class loader
                }

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();
                    c = findClass(name);

                    // this is the defining class loader; record the stats
                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    sun.misc.PerfCounter.getFindClasses().increment();
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }
}

class ExtendsServiceA {

}
