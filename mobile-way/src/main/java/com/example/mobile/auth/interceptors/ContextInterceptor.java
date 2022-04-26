package com.example.mobile.auth.interceptors;

import com.alibaba.druid.util.StringUtils;
import com.example.mobile.auth.holder.ContextHolder;
import com.example.mobile.exception.BizException;
import com.example.mobile.protocol.RetCodeEnum;
import com.example.mobile.service.SessionService;
import com.example.mobile.vo.SessionInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;


/**
 * 上下文拦截器
 *
 * @author zhangyubing
 */
@Component
@Slf4j
public class ContextInterceptor implements HandlerInterceptor {

    private static final String REQUEST_METHOD_OPTION = "OPTIONS";
    private static final Set<String> DEFAULT_WHITE_LIST = new HashSet<String>() {{
        // 发生错误时spring boot会自动发起一次/error请求，因此跳过error请求，否则报无权限
        add("/error");
    }};

    @Resource
    SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 允许跳过鉴权
        boolean permit = REQUEST_METHOD_OPTION.equals(request.getMethod())
                || DEFAULT_WHITE_LIST.contains(request.getRequestURI())
                || ContextHolder.getSkipAuth();

        if (permit) {
            return true;
        }
        authorize(ContextHolder.getToken());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    /**
     * 认证
     *
     * @param token token
     */
    private void authorize(String token) {
        if (StringUtils.isEmpty(token)) {
            log.error(">>>>> Authorized failed(token is empty)!");
            throw new BizException(RetCodeEnum.RET_TOKEN_CANNOT_EMPTY);
        }
        SessionInfoVO sessionInfoVO = sessionService.getSession(token);
        ContextHolder.setUserId(sessionInfoVO.getUserId());
    }
}
