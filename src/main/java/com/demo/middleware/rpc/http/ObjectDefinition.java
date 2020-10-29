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
}
