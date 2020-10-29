package com.demo.middleware.core;

import com.demo.middleware.InnerJarsEnum;

import java.net.MalformedURLException;
import java.net.URL;


public class JarLauncherFactory {

    public static JarLauncher getJarLauncher(InnerJarsEnum innerJarsEnum) {
        String JarName = innerJarsEnum.getJarName();
        String[] dependcyUrls = innerJarsEnum.getDependcyUrls();

        URL[] urls = new URL[dependcyUrls.length];
        for (int i = 0; i < dependcyUrls.length; i++) {
            try {
                urls[i] = new URL(dependcyUrls[i]);
            } catch (MalformedURLException e) {
                System.err.println("[URL_ERROR]-" + dependcyUrls[i]);
                continue;
            }
        }
        //  ClassLoader parent == null 所有的class都是不委托给sysytem classloader加载，都由jarLauncher加载
        JarLauncher jarLauncher = new JarLauncher(urls ,"LoaderFor-"+JarName);
        return jarLauncher;
    }
}
