package com.demo.middleware.decorator;

import com.xiaomiyoupin.IHelloWorld;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloWorldWrapper implements IHelloWorld {

    private Class mainClass = null;

    public HelloWorldWrapper(Class mainClass) {
        this.mainClass = mainClass;
    }

    @Override
    public String echo(String s) {
        Method m = null;
        try {
            //获取当前方法名称
            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            m = mainClass.getDeclaredMethod(method, String.class);
            return (String) m.invoke(mainClass.newInstance(), new Object[] {s});

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
