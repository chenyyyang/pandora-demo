package com.demo.middleware.rpc.http;

import com.demo.middleware.core.PandoraApplicationContext;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PandoraRpcServer extends HttpServlet {

    private static volatile ConcurrentHashMap<String, ObjectDefinition> mainClassMap = new ConcurrentHashMap<>();

    /* 注册这个 servlet  @Bean
    public ServletRegistrationBean middleTierServiceServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new RpcServer(), "/middleware/*");
        servletRegistrationBean.setName("MiddleTierService");
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("servletName", "mts");
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }*/

    @Override
    public void init() {
        ConcurrentHashMap<String, Class> mainClassHolder = PandoraApplicationContext.getMainClassHolder();
        if (mainClassHolder.isEmpty()) {
            System.err.println("未加载class");
            return;
        }
        for (Map.Entry<String, Class> stringClassEntry : mainClassHolder.entrySet()) {
            ObjectDefinition objectDefinition= covert2ObjectDefinition(stringClassEntry.getValue());
            String className= getClassNameWithOutPackage(stringClassEntry.getKey());

            mainClassMap.put(className,objectDefinition);
        }
        System.out.println("servlet初始化完成！");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        doGet(request, response);
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        // 函数名
        String pathInfo = request.getPathInfo();
        String[] path = pathInfo.split("/");
        String className = path[1];
        String methodName = path[2];

        ObjectDefinition objectDefinition = mainClassMap.get(className);
        Method method = objectDefinition.getMethodMap().get(methodName);

        String result = "";
        try {
            Object object = PandoraApplicationContext.getObject(objectDefinition.getMainClass());
            result= (String) method.invoke(object,request.getParameter("parmas"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        mainClassMap.clear();
    }

    private ObjectDefinition covert2ObjectDefinition(Class value) {
        Map<String, Method> methodMap = new HashMap<>();
        Method[] methods = value.getDeclaredMethods();
        for (Method method : methods) {
            methodMap.put(method.getName(),method);
        }
        return new ObjectDefinition(value,methodMap);
    }

    private String getClassNameWithOutPackage(String key) {
        String[] split = key.split(".");
        return split[split.length-1];
    }

}