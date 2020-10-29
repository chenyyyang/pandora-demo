package com.demo.middleware;

import com.demo.middleware.core.PandoraApplicationContext;
import com.demo.middleware.decorator.HelloWorldWrapper;
import com.xiaomiyoupin.HelloWorld;
import com.xiaomiyoupin.IHelloWorld;
import com.demo.middleware.decorator.DynamicProxy;

import java.lang.reflect.Method;


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

        //TODO 测试Thread.currentThread().setContextClassLoader(cacheClass.getClassLoader());
//        System.out.println("当前类加载器是：" + Thread.currentThread().getContextClassLoader());
//        PandoraApplicationContext.doSetEnvironment(InnerJarsEnum.MIDDLEWARE_DEMO);
//        System.out.println("当前类加载器是：" + Thread.currentThread().getContextClassLoader());

        Class mainClass = PandoraApplicationContext.getMainClass(InnerJarsEnum.MIDDLEWARE_DEMO);
        System.out.println("类加载器是：" + mainClass.getClassLoader());

        //泛化调用
        Object object = PandoraApplicationContext.getObject(HelloWorld.class);
        Method m = object.getClass().getDeclaredMethod("echo",String.class);
        System.out.println("成功执行返回值：" + m.invoke(object, new Object[] {"test"}));

        //这里成功了，打算用装饰模式，来避免每个方法都用反射
        HelloWorldWrapper helloWorldWrapper = new HelloWorldWrapper(mainClass);
        System.out.println("测试输出："+ helloWorldWrapper.echo("hello"));

        //TODO  尝试通过接口来强引用 对象。。调试失败
        Object helloWorldObj = PandoraApplicationContext.getObject(HelloWorld.class);
        System.out.println("类加载器是：" + helloWorldObj.getClass().getClassLoader());
        HelloWorld helloWorld = (HelloWorld) helloWorldObj;

        //TODO  尝试使用JDK动态代理   失败....
        IHelloWorld proxy = (IHelloWorld) DynamicProxy.getProxy(object);
        System.out.println(proxy.echo("yes"));

        //TODO  使用rpc方式 http://localhost:8080/middleware/HelloWorld/echo?params=hah;  m.invoke(object, new Object[] {s})反射执行。
        //https://www.coder.work/article/6385901
    }

}
