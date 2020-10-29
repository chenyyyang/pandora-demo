## 简介
pandora-demo 主要提供了jar包隔离机制

1.把需要的jar放在云端
2.URLclassloader加载云端的jar包
3.反射new 出对象，调用方法。


## 包含组件
一个Java基础工具类，对文件、流、加密解密、转码、正则、线程、XML等JDK方法进行封装，组成各种Util工具类，同时提供以下组件：

| 模块                |     介绍                                                                          |
| -------------------|---------------------------------------------------------------------------------- |
| hutool-aop         |     JDK动态代理封装，提供非IOC下的切面支持                                              |
| hutool-bloomFilter |     布隆过滤，提供一些Hash算法的布隆过滤                                                |
| hutool-cache       |     简单缓存实现                                                                     |
| hutool-core        |     核心，包括Bean操作、日期、各种Util等                                               |
| hutool-cron        |     定时任务模块，提供类Crontab表达式的定时任务                                          |
| hutool-crypto      |     加密解密模块，提供对称、非对称和摘要算法封装                                          |
| hutool-db          |     JDBC封装后的数据操作，基于ActiveRecord思想                                         |
| hutool-dfa         |     基于DFA模型的多关键字查找                                                         |
| hutool-extra       |     扩展模块，对第三方封装（模板引擎、邮件、Servlet、二维码、Emoji、FTP、分词等）            |
| hutool-http        |     基于HttpUrlConnection的Http客户端封装                                            |
| hutool-log         |     自动识别日志实现的日志门面                                                         |
| hutool-script      |     脚本执行封装，例如Javascript                                                     |
| hutool-setting     |     功能更强大的Setting配置文件和Properties封装                                        |
| hutool-system      |     系统参数调用封装（JVM信息等）                                                      |
| hutool-json        |     JSON实现                                                                       |
| hutool-captcha     |     图片验证码实现                                                                   |
| hutool-poi         |     针对POI中Excel和Word的封装                                                       |
| hutool-socket      |     基于Java的NIO和AIO的Socket封装                                                   |

-------------------------------------------------------------------------------

## 安装

### Maven
在项目的pom.xml的dependencies中加入以下内容:

```xml
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.3.5</version>
</dependency>
```

### 编译安装

访问Hutool的码云主页：[https://gitee.com/loolly/hutool](https://gitee.com/loolly/hutool) 下载整个项目源码（v5-master或v5-dev分支都可）然后进入Hutool项目目录执行：

```sh
./hutool.sh install
```

然后就可以使用Maven引入了。
