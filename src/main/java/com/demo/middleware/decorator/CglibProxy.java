package com.demo.middleware.decorator;

import com.xiaomiyoupin.HelloWorld;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

public class CglibProxy  {

    public static Object getProxyObject(Object target) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(HelloWorld.class);
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {

                System.out.println("被代理对象的classloader -->"+target.getClass().getClassLoader());
                Method declaredMethod = target.getClass().getDeclaredMethod(method.getName(),method.getReturnType());
                return declaredMethod.invoke(target, objects);
            }
        });
        return enhancer.create();
    }

}
