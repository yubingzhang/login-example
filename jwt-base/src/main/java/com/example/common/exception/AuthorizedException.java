package com.example.common.exception;

import com.example.common.protocol.RetCodeEnum;
import lombok.Getter;

/**
 * 登录态认证异常
 *
 * @author zhangyubing
 */
public class AuthorizedException extends RuntimeException {

    /**
     * 返回码枚举
     */
    @Getter
    private final RetCodeEnum retCodeEnum;

    public AuthorizedException(RetCodeEnum retCodeEnum) {
        super(retCodeEnum.name());
        this.retCodeEnum = retCodeEnum;
    }


}
