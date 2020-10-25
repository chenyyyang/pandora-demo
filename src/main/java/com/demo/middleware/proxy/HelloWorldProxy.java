package com.demo.middleware.proxy;

import com.xiaomiyoupin.HelloWorld;
import com.xiaomiyoupin.IHelloWorld;
import com.demo.middleware.InnerJarsEnum;
import com.demo.middleware.PandoraApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wuchenyang
 * @date 2020/10/22 21:35
 */
public class HelloWorldProxy implements IHelloWorld {

    Class mainClass = PandoraApplicationContext.getMainClass(InnerJarsEnum.MIDDLEWARE_DEMO);

    @Override
    public String echo(String s) {
        Method m = null;
        try {
            //获取当前方法名称
            String method = Thread.currentThread() .getStackTrace()[1].getMethodName();
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
