package com.fspringboot.SpringBootDemo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * @ClassName: FilterDemo01
 * @Description:filter的三种典型应用： <br/>
 *                             1、可以在filter中根据条件决定是否调用chain.doFilter(request, response)方法， 即是否让目标资源执行<br/>
 *                             2、在让目标资源执行之前，可以对request\response作预处理，再让目标资源执行 <br/>
 *                             3、在目标资源执行之后，可以捕获目标资源的执行结果，从而实现一些特殊的功能 <br/>
 */
@WebFilter(filterName = "authorizationfilter", urlPatterns = { "/*" }, initParams = {
		@WebInitParam(name = "enable", value = "true") })
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("----FilterDemo01过滤器初始化----");
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {

    	
    	HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
        // 对request和response进行一些预处理
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String requestUri = request.getRequestURI();
        System.out.println("***************************************************************");
        System.out.println("method: " + request.getMethod());
        System.out.println("requestURI: " + requestUri);
        
        StringBuffer s = new StringBuffer();
        System.out.println("Content-Type：" + request.getContentType());
        System.out.println("User-Agent：" + request.getHeader("User-Agent"));
        
        String token = request.getParameter("token");
        System.out.println("token: " + token);
        
        if(token == null) {
        	{
        		try {
        			HttpServletResponse t = (HttpServletResponse) response;
        			t.setHeader("Access-Control-Allow-Origin", "*");
        			t.setHeader("Access-Control-Allow-Methods",
        					"POST, GET, OPTIONS, DELETE");
        			t.setHeader("Access-Control-Max-Age", "3600");
        			t.setHeader("Access-Control-Allow-Headers", "x-requested-with");

        			response.setContentType("application/json; charset=UTF-8");
        			response.setCharacterEncoding("UTF-8");
        			response.getWriter().write("授权认证失败");
        			response.getWriter().flush();
        			response.getWriter().close();
        		} catch (Exception e) {
        			throw new RuntimeException(e);
        		}
        	}
        	return ;
        }else {
        	
        	System.out.println("FilterDemo01执行前！！！");
        	chain.doFilter(arg0, arg1); // 让目标资源执行，放行
        	System.out.println("放行后！！！");
        }
    }

    @Override
    public void destroy() {
        System.out.println("----过滤器销毁----");
    }
}