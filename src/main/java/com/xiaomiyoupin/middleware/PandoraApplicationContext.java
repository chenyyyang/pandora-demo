package com.xiaomiyoupin.middleware;

import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuchenyang
 * @date 2020/10/22 15:43
 */
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
        //获取所有的内嵌jar
        for (InnerJarsEnum innerJarsEnum : innerJarsEnums) {
            //初始化一个ClassLoader来加载,实现组件与组件之前的隔离
            JarLauncher jarLauncher = JarLauncherFactory.getJarLauncher(innerJarsEnum);
            classLoaderHolder.put(innerJarsEnum.getJarName(), jarLauncher);
        }
        //初始化对象容器
        for (InnerJarsEnum innerJarsEnum : innerJarsEnums) {
            String mainClass = innerJarsEnum.getMainClass();
            URLClassLoader classLoader = classLoaderHolder.get(innerJarsEnum.getJarName());
            try {
                Class<?> clazz = classLoader.loadClass(mainClass);
                Object obj = clazz.newInstance();
                {
                    singletonObjetcs.put(clazz, obj);
                    mainClassLoaderHolder.put(mainClass, clazz);
                    classConverter.put(ClassLoader.getSystemClassLoader().loadClass(mainClass), clazz);
                    //添加方法

                }
            } catch (ClassNotFoundException e) {
                System.err.println("[mainClass_initError]-" + innerJarsEnum.getJarName());
            } catch (IllegalAccessException e) {
                System.err.println("[Object_initError]-" + innerJarsEnum.getJarName());
            } catch (InstantiationException e) {
                System.err.println("[Object_initError]-" + innerJarsEnum.getJarName());
            }
        }
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

    public static <T> T getObject(Class<T> clz) {
        //clz默认是 systemClassloader来加载的 ，所以正常获取不到
        Object obj = singletonObjetcs.get(clz);
        if (null == obj) {
            //classConverter中key是 systemClassloader加载的类， vlaue是JarLauncher加载的Class
            Class _fake = classConverter.get(clz);
            //这里拿到自定义的class的对象
            return (T) singletonObjetcs.get(_fake);
        }
        return (T) obj;
    }

}
