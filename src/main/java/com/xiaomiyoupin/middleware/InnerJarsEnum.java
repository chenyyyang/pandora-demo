package com.xiaomiyoupin.middleware;

/**
 * @author wuchenyang
 * @date 2020/10/22 15:52
 */
public enum InnerJarsEnum {

    MIDDLEWARE_DEMO
            (
                    "demoJar",
                    "com.xiaomiyoupin.HelloWorld",
                    new String[] {
                            "http://res.youpin.mi-img.com/test_upload/middleware-1.0-SNAPSHOT.jar",
                            "http://res.youpin.mi-img.com/test_upload/gson-2.8.6.jar"
                    }
            );

    private String jarName;

    private String mainClass;

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
