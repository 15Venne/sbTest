package com.fspringboot.SpringBootDemo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fspringboot.SpringBootDemo.filter.AuthorizationFilter;
import com.google.common.collect.Maps;
/**
 * Hello world!
 *
 */
@Configuration
@SpringBootApplication
@ComponentScan({"com.fspringboot.SpringBootDemo"})
public class App 
{
    public static void main( String[] args )
    {
        //System.out.println( "Hello World!" );
    	SpringApplication.run(App.class, args);
    }
    
    @Bean
	public FilterRegistrationBean filterRegistrationBean() {
		AuthorizationFilter filter = new AuthorizationFilter();
		Map<String, String> initParameters = Maps.newHashMap();
		initParameters.put("enable", "true");
		List<String> urlPatterns = Arrays.asList("/*");

		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(filter);
		
		registrationBean.setInitParameters(initParameters);
		registrationBean.setUrlPatterns(urlPatterns);
		return registrationBean;
	}
}
