### 问题引入
我们平时的开发中经常要引入各种sdk，现在我希望在代码中引入[middleware-demo](http://res.youpin.mi-img.com/test_upload/middleware-demo-1.0-SNAPSHOT.jar)
,你可以把这个demo 看作是MQ 的sdk,echo方法看成是MQ的send方法，功能就是输出序列化好的字符串...
```
public class HelloWorld implements IHelloWorld {
   
    public String echo(String param) {
        HashMap map = new HashMap();
        map.put("echoSuccess", param);
        return (new Gson()).toJson(map);
    }
```
<br>
然后发现middleware-demo 居然要依赖gson-2.8.6(不然也没法执行echo方法呀)，而我的项目pom中另一个中间件
也依赖gson，版本是5.0.0...版本差的有点多...<br>
尝试使用<exclude>排除掉middleware-demo中的gson依赖,直接用gson-5.0.0，发现middleware-demo就抛异常了...NoSuchMethodError（此处假设gson-5.0.0中toJson方法名字改掉了）。
怎么办呢，又不想去排除掉现在稳定的gson-5.0.0。

### 问题解决
- 1.还是在业务项目pom中排除掉middleware-demo中的gson-2.8.6依赖，现在middleware-demo肯定是用不起来了,toJson报错NoSuchMethodError
- 2.把gson-2.8.6上传到金山云对象存储上,得到 [url]:http://res.youpin.mi-img.com/test_upload/gson-2.8.6.jar ,当然也可以放在本地磁盘或者resources下
- 3.在业务项目中加入本项目（pandora-demo）源码（因为还在demo阶段...没有打包成jar包,pandora-demo遵循最少依赖原则
可选依赖cglib和asm，无其他依赖）
- 4.增加middleware-demo中间件的配置,依赖的gson的云端地址（第2步得到的...）、启动类的全名（com.xiaomiyoupin.HelloWorld）
```
MIDDLEWARE_DEMO(
            "demoJar",
            "com.xiaomiyoupin.HelloWorld",
            new String[] {
                    "http://res.youpin.mi-img.com/test_upload/middleware-demo-1.0-SNAPSHOT.jar",
                    "http://res.youpin.mi-img.com/test_upload/gson-2.8.6.jar"
            }
    );
```
- 5.在spring容器启动前执行。PandoraApplicationContext.run();
- 6.PandoraApplicationContext.run()的时候动态的去金山云 加载了gson-2.8.6，完全脱离maven的束缚
- 7.按照下面的三行代码直接调用middleware-demo中的类 HelloWorld.echo()方法，可以看到成功使用了gson-2.8.6...
也就是说gson冲突的问题就没有了，项目的gson-5.0.0不会受到影响...
```$java
Object object = PandoraApplicationContext.getObject(HelloWorld.class);
HelloWorld proxyObject = CglibProxy.getProxyObject(object,HelloWorld.class);
//输出：代理对象执行：{"echoSuccess":"Hello cglib"}
System.out.println("代理对象执行："+proxyObject.echo("Hello cglib"));
```
- 8.由于是demo阶段，还没有把对象直接交给spring容器。直接把代理对象交给spring容器就更方便了。
且pandra-demo中可以引入更多的middleware，中间件与中间件之间也是互不影响的。

### 原理介绍
pandora-demo提供了动态加载jar包的功能，
并且为InnerJarsEnum中每个枚举对象都生成一个自定义的classloader（例：JarLauncher）来加载相关的jar包。
通过类加载器隔离实现中间件依赖与业务代码依赖隔离，中间件与中间件依赖隔离

* 1.把需要的jar放在云端，在InnerJarsEnum配置好中间件源码包的路径，以及中间件依赖（以gson为例）的路径
* 2.新建URLclassloader对象（例：JarLauncher）去加载云端的jar包，首先加载的是mainClass
* 3.反射new 出mainClass对象(例：HelloWorld对象)，泛化调用对象的方法。
* 4.泛化调用有所局限性，所以可以使用cglib生成代理对象，调用代理对象
的方法时，会去反射执行目标对象的方法，但是这样业务代码又必须要依赖cglib和中间件
源码（比如需要直接引入middleware1.0 但是不需要引入gson依赖）
* 5.cglib生成的代理对象可以交给spring管理，和spring结合，使依赖隔离对程序员无感


### 各个包的解释

| 包名               |     介绍                                                                          |
| -------------------|---------------------------------------------------------------------------------- |
| core         |    启动过程方法以及所需要的 classloader                                              |
| decorater |     通过代理来调用对象的方法，不用可以注释掉...                                               |
|rpc.http      |     通过rpc以及泛化调用来调用对象的方法，不用可以注释掉...                                                                     |                                                |
| InnerJarsEnum | 存放一些示例jar包，以及jar包对应的依赖，mainClass是jar包中一个被加载的类|
| TestPandora | main方法入口，测试用的，不用可以注释掉...      |

### 编译安装

* 1.把HelloWorld.class相关的报错都注释调，直接在TestPandora例子中反射执行com.xiaomiyoupin.HelloWorld的echo方法。
* 2.如果要使用代理来调用，参照以下步骤：
```
1.打开pom.xml
2.从云端 下载demo需要的jar包 http://res.youpin.mi-img.com/test_upload/middleware-1.0-SNAPSHOT.jar
3.通过<scope>system</scope> 引入jar包
4.执行TestPandora中的例子

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


