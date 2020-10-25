package com.demo.middleware.decorator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wuchenyang
 * @date 2020/10/22 20:25
 */
public class DynamicProxy implements InvocationHandler {

    private final Object subject;

    public DynamicProxy(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return method.invoke(subject, args);
    }

    /**
     * 获取动态代理
     *
     * @param realSubject 代理对象
     */
    public static Object getProxy(Object realSubject) {
        InvocationHandler handler = new DynamicProxy(realSubject);
        Object o = Proxy.newProxyInstance(realSubject.getClass().getClassLoader(),
                realSubject.getClass().getInterfaces(), handler);
        System.out.println(o.getClass());
        System.out.println(o.getClass().getClassLoader());
        return o;
    }

}