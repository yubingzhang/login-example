package com.example.mobile.controller;

import com.example.mobile.protocol.BaseResp;
import com.example.mobile.service.SessionService;
import com.example.mobile.util.BaseRespUtil;
import com.example.mobile.vo.request.RefreshTokenRequest;
import com.example.mobile.vo.request.SendSmsCodeRequest;
import com.example.mobile.vo.request.VerifySmsCodeAndLoginReq;
import com.example.mobile.vo.response.LoginResponse;
import com.example.mobile.vo.response.RefreshTokenResponse;
import com.example.mobile.vo.response.SendSmsCodeResponse;
import com.example.mobile.service.BaseService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

/**
 * 用户登录
 *
 * @author zhangyubing
 */
public class UserLoginController {
    @Resource
    private BaseService baseService;

    @Resource
    private SessionService sessionService;
    /**
     * 发送验证码
     *
     * @return 返回码
     */
    @PostMapping("/login/send-sms-code/v1")
    public BaseResp<SendSmsCodeResponse> sendSmsCode(@Validated @RequestBody SendSmsCodeRequest req) {
        SendSmsCodeResponse sendSmsCodeResponse = baseService.sendLoginSmsCode(req.getCountryCode(), req.getPhoneNumber());
        return BaseRespUtil.success(req, sendSmsCodeResponse);
    }

    /**
     * 校验验证码并登录
     *
     * @param req 验证码、手机号
     * @return token和用户信息
     */
    @PostMapping("/login/verify-sms-code/v1")
    public BaseResp<LoginResponse> verifySmsCodeAndLogin(@Validated @RequestBody VerifySmsCodeAndLoginReq req) {
        return BaseRespUtil.success(req, baseService.verifySmsCodeAndLogin(req.getCountryCode(), req.getPhone(), req.getCode()));
    }

    /**
     * 刷新token
     *
     * @return 新token
     */
    @PostMapping("/refresh-token/v1")
    public BaseResp<RefreshTokenResponse> refreshToken(@Validated @RequestBody RefreshTokenRequest req) {
        return BaseRespUtil.success(req, sessionService.refreshToken(req.getRefreshToken()));
    }
}
