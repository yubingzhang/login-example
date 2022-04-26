package com.example.common.starter.filter;

import com.example.common.starter.constants.CommonConstant;
import com.example.common.starter.holder.ContextHolder;
import com.example.common.starter.holder.IAuthWhiteListHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 上下文过滤器
 *
 * @author zhangyubing
 */
@Component
@Slf4j
public class ContextFilter implements Filter {

    /**
     * 登录态认证白名单配置
     */
    private final IAuthWhiteListHolder authWhiteListHolder;

    public ContextFilter(IAuthWhiteListHolder authWhiteListHolder) {
        this.authWhiteListHolder = authWhiteListHolder;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ContextHolder.setSkipAuth(false);

        if (null != authWhiteListHolder && authWhiteListHolder.isInWhiteList(request.getRequestURI())) {
            ContextHolder.setSkipAuth(true);
        }
        String token = request.getHeader(CommonConstant.AUTHORIZATION);

        ContextHolder.setToken(token);

        filterChain.doFilter(servletRequest, servletResponse);
        // 清楚ThreadLocal的数据，否则会有内存泄漏
        ContextHolder.clear();
    }
}
