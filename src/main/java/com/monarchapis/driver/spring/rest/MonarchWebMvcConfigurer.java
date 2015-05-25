package com.monarchapis.driver.spring.rest;

import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.monarchapis.driver.spring.MonarchConfigurer;

@Import(MonarchConfigurer.class)
public class MonarchWebMvcConfigurer extends WebMvcConfigurerAdapter {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		ApiRequestHandlerInterceptor interceptor = new ApiRequestHandlerInterceptor();
		registry.addInterceptor(interceptor).addPathPatterns("/**");
	}
}
