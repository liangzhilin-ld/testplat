package com.techstar.testplat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.techstar.testplat.common.AuthInterceptor;

@Configuration
public class CORSConfiguration implements WebMvcConfigurer {
//	@Resource
//    AuthInterceptor authInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//    	// addPathPatterns拦截所有请求,excludePathPatterns排除的请求
//    	InterceptorRegistration registration = registry.addInterceptor(authInterceptor());
//    	registration.addPathPatterns("/**");
//    	registration.excludePathPatterns("/auth/**","/");
//    	//排除swagger2页面请求
//    	registration.excludePathPatterns("/swagger-resources/**");
//    	registration.excludePathPatterns("/webjars/**");
//    	registration.excludePathPatterns("/v2/**");
//    	registration.excludePathPatterns("/swagger-ui.html/**");
//    	registration.excludePathPatterns("/error");
//    	registration.excludePathPatterns("/csrf");
//    }
//    @Bean
//    public AuthInterceptor authInterceptor() {
//    	return new AuthInterceptor();
//    } 
    
}
