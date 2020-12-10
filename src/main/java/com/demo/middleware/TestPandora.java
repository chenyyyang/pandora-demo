package com.demo.middleware;

import com.demo.middleware.core.JarLauncher;
import com.demo.middleware.core.JarLauncherFactory;
import com.demo.middleware.core.PandoraApplicationContext;
import com.demo.middleware.decorator.CglibProxy;
import com.demo.middleware.decorator.JDKDynamicProxy;
import com.xiaomiyoupin.HelloWorld;
import com.xiaomiyoupin.IHelloWorld;

import java.lang.reflect.Method;

public class TestPandora {

    public static void main(String[] args) throws Exception {
//        case1(false);
        case2();
    }

    private static void case1(boolean flag) throws Exception {

        PandoraApplicationContext.run();

        Class mainClass = PandoraApplicationContext.getMainClass(InnerJarsEnum.MIDDLEWARE_DEMO);
        System.out.println(mainClass.getName() + "类加载器是：" + mainClass.getClassLoader());

        //泛化调用
        Object object = PandoraApplicationContext.getObject(HelloWorld.class);
        Method m = object.getClass().getDeclaredMethod("echo", String.class);
        System.out.println("成功执行返回值：" + m.invoke(object, new Object[] {"test"}));

        //cglib生成代理类，执行成功
        HelloWorld proxyObject = CglibProxy.create(object, HelloWorld.class);
        System.out.println("CglibProxy代理对象执行：" + proxyObject.echo("Hello CglibProxy"));

        IHelloWorld wrapper = JDKDynamicProxy.create(object, IHelloWorld.class);
        System.out.println("JDKDynamicProxy代理对象执行：" + wrapper.echo("Hello JDK-DynamicProxy"));

        //TODO  尝试通过接口来强引用 对象。发现类型不同
        Object helloWorldObj = PandoraApplicationContext.getObject(HelloWorld.class);
        System.out.println("类加载器是：" + helloWorldObj.getClass().getClassLoader());
        HelloWorld helloWorld = (HelloWorld) helloWorldObj;

//         如果时appClassloader加载的HelloWorld对象，会因为缺少gson依赖而报错
//        HelloWorld helloWorld = new HelloWorld();
//        helloWorld.echo("throw e");
         /*
    IHelloWorld object = PandoraApplicationContext.getObject(HelloWorld.class);
    抛出异常：
    Exception in thread "main" java.lang.ClassCastException:
        com.xiaomiyoupin.HelloWorld cannot be cast to com.xiaomiyoupin.HelloWorld
        at com.xiaomiyoupin.middleware.测试类.main(测试类.java:15)

解答：https://developer.aliyun.com/article/710407

对于任何一个类  必须由加载它的类加载器和类本身  共同确立其在虚拟机中的唯一性

每个类加载器都拥有一个独立的类名称空间。比较两个类是否相等(equal()  instanceof)只有当
两个类是同一个类加载器加载的时候才有意义。肯定也无法转化，就会抛出ClassCastException
        */

        //TODO  使用rpc方式 http://localhost:8080/middleware/HelloWorld/echo?params=hah;  m.invoke(object, new Object[] {s})反射执行。
        //https://www.coder.work/article/6385901

        /*if (flag == false) {
            //TODO 测试Thread.currentThread().setContextClassLoader(cacheClass.getClassLoader());
            System.out.println("当前类加载器是：" + Thread.currentThread().getContextClassLoader());
            PandoraApplicationContext.doSetEnvironment(InnerJarsEnum.MIDDLEWARE_DEMO);
            System.out.println("当前类加载器是：" + Thread.currentThread().getContextClassLoader());

            Thread thread = new Thread(() -> {
                try {
                    cases(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            thread.setContextClassLoader(Thread.currentThread().getContextClassLoader());
            thread.start();
            new CountDownLatch(1).await();
        }*/

        //TODO java SPI (Service Provider Interface) ，是JDK内置的一种服务提供发现机制

    }

    private static void case2() throws Exception {

        JarLauncher jarLauncher = JarLauncherFactory.create(InnerJarsEnum.MIDDLEWARE_DEMO);

        Class<?> mainClass = jarLauncher.loadClass(InnerJarsEnum.MIDDLEWARE_DEMO.getMainClass());

        HelloWorld proxyObject = CglibProxy.create(mainClass.newInstance(), HelloWorld.class);

        System.out.println("case2 代理对象执行：" + proxyObject.echo("Hello case2"));
    }
}
