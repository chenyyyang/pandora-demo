package com.demo.middleware.core;

import com.demo.middleware.InnerJarsEnum;

import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;


public class PandoraApplicationContext {

    /*mainClass 表示一个Jar包的启动类*/
    /*全局持有所有classloader   key 是 mainClass名字  value是对应的classloader*/
    private static volatile ConcurrentHashMap<String, URLClassLoader> classLoaderHolder = new ConcurrentHashMap<String, URLClassLoader>();

    /*全局持有所有Class和 对应的对象*/
    private static volatile ConcurrentHashMap<Class, Object> singletonObjetcs = new ConcurrentHashMap<Class, Object>();

    /*key是 类被 system classloader 和 JarLauncher分别加载的class*/
    private static volatile ConcurrentHashMap<Class, Class> classConverter = new ConcurrentHashMap();

    /*key 是 mainClass名字  value是mainClass的Class*/
    private static volatile ConcurrentHashMap<String, Class> mainClassLoaderHolder = new ConcurrentHashMap<String, Class>();

    public static void run() {

        InnerJarsEnum[] innerJarsEnums = InnerJarsEnum.class.getEnumConstants();
        //获取所有的待加载的jar
        for (InnerJarsEnum innerJarsEnum : innerJarsEnums) {
            //初始化一个ClassLoader来加载,实现组件与组件之前的隔离
            JarLauncher jarLauncher = JarLauncherFactory.getJarLauncher(innerJarsEnum);
            classLoaderHolder.put(innerJarsEnum.getJarName(), jarLauncher);
        }
        //加载Class
        for (InnerJarsEnum innerJarsEnum : innerJarsEnums) {
            //TODO  自定义注解   扫描被注解的类
            String mainClass = innerJarsEnum.getMainClass();
            //拿到对应的classloader
            URLClassLoader classLoader = classLoaderHolder.get(innerJarsEnum.getJarName());
            try {
                Class<?> clazz = classLoader.loadClass(mainClass);
                {
                    mainClassLoaderHolder.put(mainClass, clazz);
                    classConverter.put(ClassLoader.getSystemClassLoader().loadClass(mainClass), clazz);
                }
            } catch (ClassNotFoundException e) {
                System.err.println("[mainClass_loadError]-" + innerJarsEnum.getJarName());
            }
        }
        System.out.println("[类加载完毕]...");
    }

    public static Class doSetEnvironment(InnerJarsEnum _enum) {
        Class cacheClass = mainClassLoaderHolder.get(_enum.getMainClass());
        if (cacheClass != null) {
            Thread.currentThread().setContextClassLoader(cacheClass.getClassLoader());
        }
        return cacheClass;
    }

    public static Class getMainClass(InnerJarsEnum _enum) {

        return mainClassLoaderHolder.get(_enum.getMainClass());
    }

    public static synchronized <T> T getObject(Class<T> clz) throws Exception {
        Object o;
        //是自定义加载器加载的class
        if (clz.getClassLoader() instanceof JarLauncher) {
            //从单例池中取出对象
            o = getSingletonObject(clz);
        } else {
            //classConverter中key是 systemClassloader加载的类， vlaue是JarLauncher加载的Class
            //这里拿到自定义的class的对象
            Class orginClass = classConverter.get(clz);
            if (orginClass == null) {
                return null;
            }
            o = getSingletonObject(orginClass);
        }
        return (T) o;
    }

    private static Object getSingletonObject(Class clz) throws InstantiationException, IllegalAccessException {
        Object cachedObject = null;
        Object obj = singletonObjetcs.get(clz);

        if (obj != null) {
            cachedObject = obj;
        } else {
            cachedObject = clz.newInstance();
            singletonObjetcs.put(clz, cachedObject);
        }
        return cachedObject;
    }

    public static ConcurrentHashMap<String, Class> getMainClassHolder() {
        return mainClassLoaderHolder;
    }
}
