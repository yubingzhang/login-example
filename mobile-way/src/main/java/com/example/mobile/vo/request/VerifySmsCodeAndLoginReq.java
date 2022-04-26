package com.example.mobile.vo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 校验验证码并登录请求参数
 *
 * @author zhangubing
 */
@Data
public class VerifySmsCodeAndLoginReq {
    /**
     * 验证码
     */
    @NotNull
    private String code;

    /**
     * 手机号码
     */
    @NotNull
    private String phone;

    /**
     * 国家代码，如中国：+86
     */
    @NotNull
    private String countryCode;
}
