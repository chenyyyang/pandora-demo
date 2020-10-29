package com.demo.middleware.rpc.http;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author wuchenyang
 * @date 2020/10/26 20:35
 */
public class ObjectDefinition {

    private Class mainClass;

    private Map<String, Method> methodMap;

    public ObjectDefinition(Class mainClass, Map<String, Method> methodMap) {
        this.mainClass = mainClass;
        this.methodMap = methodMap;
    }

    public Class getMainClass() {
        return mainClass;
    }

    public void setMainClass(Class mainClass) {
        this.mainClass = mainClass;
    }

    public Map<String, Method> getMethodMap() {
        return methodMap;
    }

    public void setMethodMap(Map<String, Method> methodMap) {
        this.methodMap = methodMap;
    }
}
