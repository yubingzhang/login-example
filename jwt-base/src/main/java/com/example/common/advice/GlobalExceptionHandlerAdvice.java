package com.example.common.advice;

import com.example.common.exception.AuthorizedException;
import com.example.common.exception.BizException;
import com.example.common.protocol.BaseResp;
import com.example.common.protocol.RetCodeEnum;
import com.example.common.util.BaseRespUtil;
import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;


/**
 * 全局统一异常处理器
 *
 * @author zhahgyubing
 */
@Slf4j
@Order(1)
@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    /**
     * 异常处理器
     *
     * @param e   异常
     * @param <T> 返回类型
     * @return 统一返回
     */
    @ExceptionHandler({AuthorizedException.class, MethodArgumentNotValidException.class, Exception.class})
    public <T> BaseResp<T> handleException(Exception e) {
        log.error(e.getMessage(), e);
        if (e instanceof AuthorizedException) {
            AuthorizedException authorizedException = (AuthorizedException) e;
            return BaseRespUtil.error(authorizedException.getRetCodeEnum());
        }
        if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                String msg = String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage());
                return BaseRespUtil.error(RetCodeEnum.RET_PARAMS_IS_INVALID, msg);
            }
        }
        return BaseRespUtil.error(RetCodeEnum.RET_INTERNAL_SERVER_ERROR);
    }

    /**
     * 业务异常处理器
     *
     * @param e   异常
     * @param <T> 返回类型
     * @return 统一返回
     */
    @ExceptionHandler({BizException.class})
    public <T> BaseResp<T> handleException(BizException e) {
        log.error(e.getMessage(), e);
        return BaseRespUtil.error(e.getRetCode());
    }

    /**
     * 参数解析异常
     *
     * @param e   异常
     * @param <T> 返回类型
     * @return 统一返回
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public <T> BaseResp<T> handleException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return BaseRespUtil.error(RetCodeEnum.RET_HTTP_MESSAGE_NOT_READABLE);
    }

    /**
     * json解析异常
     *
     * @param e   异常
     * @param <T> 返回类型
     * @return 统一返回
     */
    @ExceptionHandler({JsonParseException.class})
    public <T> BaseResp<T> handleException(JsonParseException e) {
        log.error(e.getMessage(), e);
        return BaseRespUtil.error(RetCodeEnum.RET_JSON_PARSE_ERROR);
    }

    /**
     * Servlet异常
     *
     * @param e   异常
     * @param <T> 返回类型
     * @return 统一返回
     */
    @ExceptionHandler(ServletException.class)
    public <T> BaseResp<T> handleException(ServletException e) {
        log.error(e.getMessage(), e);
        return BaseRespUtil.error(RetCodeEnum.RET_INTERNAL_SERVER_ERROR);
    }

}
