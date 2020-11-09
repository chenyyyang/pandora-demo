package com.demo.middleware.core;

import java.net.URL;
import java.net.URLClassLoader;


public class JarLauncher extends URLClassLoader {

    private String jarLauncherName;

    public JarLauncher(URL[] urls, String jarLauncherName) {
        //设置为null
        super(urls, null);
        this.jarLauncherName = jarLauncherName;
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        System.out.println("[" + jarLauncherName + "]-loadClass-" + name);
        return super.findClass(name);

    }

    public String getJarLauncherName() {
        return jarLauncherName;
    }

    public void setJarLauncherName(String jarLauncherName) {
        this.jarLauncherName = jarLauncherName;
    }


   /* private ConcurrentHashMap<String, Class<?>> CLASS_CACHE =
            new ConcurrentHashMap<String, Class<?>>();

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        synchronized (this) {
            Class<?> clazz = CLASS_CACHE.get(name);
            if (clazz == null) {
                clazz = super.loadClass(name,false);
                CLASS_CACHE.put(name, clazz);
                System.out.println("[" + jarLauncherName + "]-loadClass-" + name);
            }
            return clazz;
        }
    }*/
}
