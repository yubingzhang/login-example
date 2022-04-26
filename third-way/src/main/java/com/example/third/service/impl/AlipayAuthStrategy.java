package com.example.third.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.example.third.service.AbstractThirdPartAuthStrategy;
import com.example.third.util.ThreadLocalUtil;
import com.example.third.vo.request.ThirdLoginRequest;
import com.example.third.vo.response.LoginResponse;
import com.example.third.vo.response.ThirdUserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

import static com.example.third.enums.ThirdTypeEnum.THIRD_TYPE_ALIPAY;


/**
 * 支付宝授权服务
 *
 * @author zhangubing
 */
@Slf4j
@Service("alipayAuthStrategy")
public class AlipayAuthStrategy extends AbstractThirdPartAuthStrategy {

    private static final String ALIPAY_OAUTH_TOKEN = "alipay_oauth_token";

    @Resource
    private AlipayClient alipayClient;

    /**
     * 支付宝登录
     *
     * @param req 第三方授权请求信息
     * @return 登录结果
     */
    @Override
    public LoginResponse login(ThirdLoginRequest req) {
        try {
            return super.login(req);
        } finally {
            ThreadLocalUtil.remove(ALIPAY_OAUTH_TOKEN);
        }
    }

    /**
     * 获取支付宝openId
     *
     * @param req 请求参数
     * @return openId
     */
    @Override
    public String getOpenid(ThirdLoginRequest req) {
        AlipaySystemOauthTokenResponse alipaySystemOauthTokenResponse = getToken(req.getCode());
        ThreadLocalUtil.put(ALIPAY_OAUTH_TOKEN, alipaySystemOauthTokenResponse);
        return alipaySystemOauthTokenResponse.getUserId();
    }

    /**
     * 获取第三方登录信息
     *
     * @param req 请求参数
     * @return 用户信息
     */
    @Override
    public ThirdUserInfoResponse getThirdInfo(ThirdLoginRequest req) {
        AlipaySystemOauthTokenResponse alipaySystemOauthTokenResponse = ThreadLocalUtil.getAndRemove(ALIPAY_OAUTH_TOKEN);
        if (Objects.isNull(alipaySystemOauthTokenResponse)) {
            alipaySystemOauthTokenResponse = getToken(req.getCode());
        }
        return ThirdUserInfoResponse.builder().bindType(THIRD_TYPE_ALIPAY.getValue())
                .openId(alipaySystemOauthTokenResponse.getUserId())
                .build();
    }

    /**
     * 获取支付宝accessToken
     *
     * @param code 授权码
     * @return token信息
     */
    private AlipaySystemOauthTokenResponse getToken(String code) {
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(code);
        AlipaySystemOauthTokenResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (Exception e) {
            log.error(">>>>> 支付宝登录异常 ", e);
            //throw new BizException(RetCodeEnum.RET_BASE_THIRD_LOGIN_ERROR);
        }
        if (!response.isSuccess()) {
            log.error(">>>>> 支付宝登录失败 ", response);
            //throw new BizException(RetCodeEnum.RET_BASE_THIRD_LOGIN_ERROR);
        }
        return response;
    }
}
