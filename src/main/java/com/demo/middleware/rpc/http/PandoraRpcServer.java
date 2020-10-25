package com.demo.middleware.rpc.http;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PandoraRpcServer extends HttpServlet {

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



    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        doGet(request, response);
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        doGet(request, response);
    }

    @Override
    public void destroy() {

    }
}