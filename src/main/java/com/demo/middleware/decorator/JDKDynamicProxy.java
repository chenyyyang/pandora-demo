package com.demo.middleware.decorator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wuchenyang
 * @date 2020/11/9 20:59
 */
public class JDKDynamicProxy {

    public static <T> T create(Object target, Class<T> clz) {

        Object obj = Proxy.newProxyInstance(clz.getClassLoader(), new Class[] {clz}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("[执行代理方法" + method.getName() + "]...被代理对象的classloader -->" + target.getClass().getClassLoader());
                Method declaredMethod = target.getClass().getDeclaredMethod(method.getName(), method.getReturnType());
                return declaredMethod.invoke(target, args);
            }
        });
        return (T) obj;
    }

}
