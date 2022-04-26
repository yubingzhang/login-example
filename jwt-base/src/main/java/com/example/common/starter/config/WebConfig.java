package com.example.common.starter.config;

import com.example.common.starter.interceptors.ContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置类
 *
 * @author moxiaofei
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ContextInterceptor contextInterceptor;

    /**
     * web配置, 添加请求上下文拦截器
     *
     * @return web配置类
     */
    @Bean
    public WebConfig getWebConfig() {
        return new WebConfig() {
            @Override
            public void addInterceptors(InterceptorRegistry interceptorRegistry) {
                interceptorRegistry.addInterceptor(contextInterceptor);
            }
        };
    }

}