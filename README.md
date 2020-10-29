## 简介
pandora-demo提供了动态加载jar包的功能，
并且InnerJarsEnum中每个对象都生成一个自定义的classloader来加载相关的jar包。
实现类加载器隔离

* 1.把需要的jar放在云端，在InnerJarsEnum配置好jar包的路径
* 2.新建URLclassloader对象去加载云端的jar包
* 3.反射new 出对象，泛化调用对象的方法。


## 各个包的解释

| 包名               |     介绍                                                                          |
| -------------------|---------------------------------------------------------------------------------- |
| core         |    启动过程方法以及所需要的 classloader                                              |
| decorater |     通过代理来调用对象的方法                                                |
|rpc.http      |     通过rpc以及泛化调用来调用对象的方法                                                                     |                                                |
| InnerJarsEnum | 存在一些示例jar包，以及jar包对应的依赖，mainClass是jar包中一个被加载的类

## 安装
### 编译安装

```
见pom.xml
从云端 下载demo需要的jar包 http://res.youpin.mi-img.com/test_upload/middleware-1.0-SNAPSHOT.jar
通过<scope>system</scope> 引入jar包
```

然后就可以使用TestPandora跑起来了，可以debug看到，jar包加载和运行过程。

