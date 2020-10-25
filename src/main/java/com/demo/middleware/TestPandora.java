package com.demo.middleware;

import com.demo.middleware.proxy.HelloWorldProxy;
import com.xiaomiyoupin.HelloWorld;
import com.xiaomiyoupin.IHelloWorld;
import com.demo.middleware.proxy.DynamicProxy;

import java.lang.reflect.Method;

/**
 * @author wuchenyang
 * @date 2020/10/22 16:25
 */
public class TestPandora {

    public static void main(String[] args) throws Exception {
  /*
    IHelloWorld object = PandoraApplicationContext.getObject(HelloWorld.class);
    抛出异常：
    Exception in thread "main" java.lang.ClassCastException:
        com.xiaomiyoupin.HelloWorld cannot be cast to com.xiaomiyoupin.HelloWorld
        at com.xiaomiyoupin.middleware.测试类.main(测试类.java:15)

解答：https://developer.aliyun.com/article/710407

每个ClassLoader都有一个 Dictionary 用来保存它所加载的InstanceKlass信息。
并且，每个 ClassLoader 通过锁，保证了对于同一个Class，它只会注册一份 InstanceKlass 到自己的 Dictionary 。
正式由于上面这些原因，如果所有的 ClassLoader 都由自己去加载 Class 文件
就会导致对于同一个Class文件，存在多份InstanceKlass，所以即使是同一个Class文件，
不同InstanceKlasss 衍生出来的实例类型也是不一样的。
        */
        PandoraApplicationContext.run();

        System.out.println("当前类加载器是：" + Thread.currentThread().getContextClassLoader());
        PandoraApplicationContext.doSetEnvironment(InnerJarsEnum.MIDDLEWARE_DEMO);
        System.out.println("当前类加载器是：" + Thread.currentThread().getContextClassLoader());

        Object object = PandoraApplicationContext.getObject(HelloWorld.class);
        Class mainClass = PandoraApplicationContext.getMainClass(InnerJarsEnum.MIDDLEWARE_DEMO);

        System.out.println("类加载器是：" + mainClass.getClassLoader());
        Method m = mainClass.getDeclaredMethod("echo",String.class);
        System.out.println("成功执行返回值：" + m.invoke(object, new Object[] {"test"}));


        //这里成功了，所以思路还是用代理
        HelloWorldProxy helloWorldProxy = new HelloWorldProxy();
        System.out.println("测试输出："+helloWorldProxy.echo("hello"));

        //TODO  这里调试失败
        Object helloWorldObj = PandoraApplicationContext.getObject(HelloWorld.class);
        System.out.println("类加载器是：" + helloWorldObj.getClass().getClassLoader());
        IHelloWorld helloWorld = (IHelloWorld) helloWorldObj;
        System.out.println(helloWorld.echo("yes"));


        //TODO  调试使用JDK动态代理   失败....
        IHelloWorld proxy = (IHelloWorld) DynamicProxy.getProxy(object);
        System.out.println(proxy.echo("yes"));

        //TODO  使用rpc方式 http://localhost:8080/middleware/HelloWorld/echo?params=hah;  m.invoke(object, new Object[] {s})反射执行。
        //https://www.coder.work/article/6385901

    }
}
