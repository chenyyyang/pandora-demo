package com.xiaomiyoupin.middleware.proxy;

import com.xiaomiyoupin.HelloWorld;
import com.xiaomiyoupin.IHelloWorld;
import com.xiaomiyoupin.middleware.InnerJarsEnum;
import com.xiaomiyoupin.middleware.PandoraApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wuchenyang
 * @date 2020/10/22 21:35
 */
public class HelloWorldProxy implements IHelloWorld {

    Object object = PandoraApplicationContext.getObject(HelloWorld.class);
    Class mainClass = PandoraApplicationContext.getMainClass(InnerJarsEnum.MIDDLEWARE_DEMO);

    @Override
    public String echo(String s) {
        Method m = null;
        try {
            //获取当前方法名称
            String method = Thread.currentThread() .getStackTrace()[1].getMethodName();
            m = mainClass.getDeclaredMethod(method, String.class);
            return (String) m.invoke(object, new Object[] {s});
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
