package com.demo.middleware.decorator;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

public class CglibProxy  {

    public static <T> T create(Object target, Class<T> clz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clz);
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {

                System.out.println("[执行代理方法"+method.getName()+"]...被代理对象的classloader -->" + target.getClass().getClassLoader());
                Method declaredMethod = target.getClass().getDeclaredMethod(method.getName(), method.getReturnType());
                return declaredMethod.invoke(target, objects);
            }
        });
        return (T) enhancer.create();
    }

}
