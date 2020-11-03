## 介绍
pandora-demo提供了动态加载jar包的功能，
并且InnerJarsEnum中每个对象都生成一个自定义的classloader来加载相关的jar包。
通过类加载器隔离实现中间件依赖与业务代码依赖隔离，中间件与中间件依赖隔离

* 1.把需要的jar放在云端，在InnerJarsEnum配置好中间件源码包的路径，以及中间件依赖（以gson为例）的路径
* 2.新建URLclassloader对象（例：JarLauncher）去加载云端的jar包，首先加载的时mainClass
* 3.反射new 出mainClass对象(例：HelloWorld对象)，泛化调用对象的方法。
* 4.泛化调用有所局限性，所以可以使用cglib生成代理对象，调用代理对象
的方法时，会去反射执行目标对象的方法，但是这样业务代码又必须要依赖cglib和中间件
源码（比如需要直接引入middleware1.0 但是不直接依赖gson）
* 5.cglib生成的代理对象可以手动加入spring单例池等，和spring结合，使依赖隔离对程序员无感


### 各个包的解释

| 包名               |     介绍                                                                          |
| -------------------|---------------------------------------------------------------------------------- |
| core         |    启动过程方法以及所需要的 classloader                                              |
| decorater |     通过代理来调用对象的方法                                                |
|rpc.http      |     通过rpc以及泛化调用来调用对象的方法                                                                     |                                                |
| InnerJarsEnum | 存放一些示例jar包，以及jar包对应的依赖，mainClass是jar包中一个被加载的类


### 编译安装

把HelloWorld.class相关的报错都注释调，直接在TestPandora例子中反射执行HelloWorld的echo方法。
如果要使用代理，参照以下步骤：
```
1.见pom.xml
2.从云端 下载demo需要的jar包 http://res.youpin.mi-img.com/test_upload/middleware-1.0-SNAPSHOT.jar
3.通过<scope>system</scope> 引入jar包

```
然后就可以使用TestPandora跑起来了，可以debug看到，jar包加载和运行过程。

```
不建议尝试：
通过rpc泛化调用
1.注册这个 PandoraRpcServer  @Bean
    public ServletRegistrationBean middleTierServiceServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new RpcServer(), "/middleware/*");
        servletRegistrationBean.setName("MiddleTierService");
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("servletName", "mts");
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }

2.http调用
servlet初始化时会把，class和method加载到内存中，等待http调用
```

### 资料和引用
- [类容器隔离组件源码浅析](https://bingoex.github.io/2018/01/01/pandora/)
- [深入理解Java虚拟机读书笔记](https://bingoex.github.io/2015/09/17/jvm-book-3-classloader/#%E6%A6%82%E8%BF%B0)
- [Pandora Boot和spring Boot](https://blog.csdn.net/alex_xfboy/article/details/89531580)
- [springboot启动时如何加载jar](https://cloud.tencent.com/developer/article/1619027)


