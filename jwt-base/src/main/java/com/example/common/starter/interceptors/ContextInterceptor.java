package com.example.common.starter.interceptors;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.common.exception.AuthorizedException;
import com.example.common.protocol.RetCodeEnum;
import com.example.common.starter.holder.ContextHolder;
import com.example.common.starter.model.HeaderMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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
        // 发生错误时spring boot会自动发起一次/error请求,因此跳过error请求，否则报无权限
        add("/error");
    }};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 设置sud-meta
        HeaderMeta headerSudMeta = HeaderMeta.parseFromRequest(request);
        ContextHolder.setHeaderSudMeta(headerSudMeta);

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
        try {
            ContextHolder.verifyAccessToken(token);
        } catch (TokenExpiredException e) {
            log.error(">>>>> Authorized failed({})!", RetCodeEnum.RET_TOKEN_HAS_EXPIRED);
            throw new AuthorizedException(RetCodeEnum.RET_TOKEN_HAS_EXPIRED);
        } catch (Exception e) {
            log.error(">>>>> token authentication failed.");
            throw new AuthorizedException(RetCodeEnum.RET_UNAUTHORIZED);
        }
    }
}
