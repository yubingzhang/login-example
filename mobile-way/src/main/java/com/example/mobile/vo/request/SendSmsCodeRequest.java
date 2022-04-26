package com.example.mobile.vo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 发送短信验证码请求参数
 *
 * @author zhangyubing
 */
@Data
public class SendSmsCodeRequest {

    /**
     * 电话号码
     */
    @NotNull
    private String phoneNumber;

    /**
     * 地区码
     */
    @NotNull
    private String countryCode;

    /**
     * 短信参数
     */
    private List<String> templateParamsList;

}
