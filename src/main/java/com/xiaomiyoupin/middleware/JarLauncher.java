package com.xiaomiyoupin.middleware;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuchenyang
 * @date 2020/10/22 15:37
 */
public class JarLauncher extends URLClassLoader {

    private String jarLauncherName;

    private static ConcurrentHashMap<String, Class<?>> CLASS_CACHE =
            new ConcurrentHashMap<String, Class<?>>();

    public JarLauncher(URL[] urls, String jarLauncherName, ClassLoader parent) {
        super(urls, parent);
        this.jarLauncherName = jarLauncherName;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        synchronized (this) {
            Class<?> clazz = CLASS_CACHE.get(name);
            if (clazz == null) {
                clazz = super.loadClass(name);
                CLASS_CACHE.put(name, clazz);
                System.out.println("[" + jarLauncherName + "]-loadClass-" + name);
            }
            return clazz;
        }
    }

    public String getJarLauncherName() {
        return jarLauncherName;
    }

    public void setJarLauncherName(String jarLauncherName) {
        this.jarLauncherName = jarLauncherName;
    }
}
