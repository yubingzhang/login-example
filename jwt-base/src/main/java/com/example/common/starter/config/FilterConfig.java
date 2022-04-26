package com.example.common.starter.config;

import com.example.common.starter.filter.ContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置
 *
 * @author zhangyubing
 */
@Configuration
public class FilterConfig {

    @Autowired
    private ContextFilter contextFilter;

    /**
     * 注册servlet过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<ContextFilter> registerContextFilter() {
        FilterRegistrationBean<ContextFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(contextFilter);
        registrationBean.setName("contextFilter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
