package com.demo.middleware.decorator;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author wuchenyang
 * @date 2020/10/29 15:09
 */
public class CglibProxy implements MethodInterceptor {

    Object target;

    public Object getProxyObject(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(this);
        enhancer.setSuperclass(target.getClass());
        return enhancer.create();
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("invoke start");
        System.out.println(methodProxy.getSuperName());
        Object result = methodProxy.invoke(target, args);
        System.out.println("invoke end");
        return result;
    }
}
