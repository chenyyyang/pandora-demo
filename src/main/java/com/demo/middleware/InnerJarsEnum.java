package com.demo.middleware;


public enum InnerJarsEnum {

    MIDDLEWARE_DEMO(
            "demoJar",
            "com.xiaomiyoupin.HelloWorld",
            new String[] {
                    "http://res.youpin.mi-img.com/test_upload/middleware-1.0-SNAPSHOT.jar",
                    "http://res.youpin.mi-img.com/test_upload/gson-2.8.6.jar"
            }
    );

    /*UPLOAD_SDK(
            "uploadSDK",
            "com.xiaomiyoupin.HelloWorld",
            new String[] {
                    "http://res.youpin.mi-img.com/test_upload/middleware-1.0-SNAPSHOT.jar",
                    "http://res.youpin.mi-img.com/test_upload/gson-2.8.6.jar"
            }
    );*/

    /*Jar包名称*/
    private String jarName;

    /*Jar包的入口类，类加载器加载jar包 加载的第一个类*/
    private String mainClass;

    /*Jar包自己 的路径  和 依赖的路径*/
    private String[] dependcyUrls;

    InnerJarsEnum(String jarName, String mainClass, String[] dependcyUrls) {
        this.jarName = jarName;
        this.mainClass = mainClass;
        this.dependcyUrls = dependcyUrls;
    }

    public String getJarName() {
        return jarName;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String[] getDependcyUrls() {
        return dependcyUrls;
    }
}
