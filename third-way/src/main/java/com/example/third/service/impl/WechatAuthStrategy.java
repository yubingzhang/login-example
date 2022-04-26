package com.example.third.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.third.config.WechatCertProperties;
import com.example.third.exception.BizException;
import com.example.third.model.WechatModel;
import com.example.third.protocol.RetCodeEnum;
import com.example.third.service.AbstractThirdPartAuthStrategy;
import com.example.third.util.RetrofitHttpClient;
import com.example.third.util.ThreadLocalUtil;
import com.example.third.vo.request.ThirdLoginRequest;
import com.example.third.vo.response.LoginResponse;
import com.example.third.vo.response.ThirdUserInfoResponse;
import com.google.protobuf.StringValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

import static com.example.third.enums.ThirdTypeEnum.THIRD_TYPE_WECHAT;
import static com.example.third.model.WechatModel.*;

/**
 * 微信授权
 *
 * @author zhangyubing
 */
@Slf4j
@Service("wechatAuthStrategy")
public class WechatAuthStrategy extends AbstractThirdPartAuthStrategy {

    private static final String LOG_TITLE = "[Wechat-Auth]";
    private static final String ACCESS_TOKEN_INFO = "access_token_info";

    @Resource
    private WechatCertProperties wechatCertProperties;

    @Override
    public LoginResponse login(ThirdLoginRequest req) {
        try {
            return super.login(req);
        } finally {
            ThreadLocalUtil.remove(ACCESS_TOKEN_INFO);
        }
    }

    @Override
    public ThirdUserInfoResponse getThirdInfo(ThirdLoginRequest req) {
        WechatModel.AccessTokenInfo accessTokenInfo = ThreadLocalUtil.getAndRemove(ACCESS_TOKEN_INFO);
        if (Objects.isNull(accessTokenInfo)) {
            accessTokenInfo = getAccessToken(req.getCode());
        }
        WechatModel.UserInfo userInfo = getUserInfo(accessTokenInfo.getAccessToken(), accessTokenInfo.getOpenid());
        return ThirdUserInfoResponse.builder().bindType(THIRD_TYPE_WECHAT.getValue())
                .build();
    }

    @Override
    public String getOpenid(ThirdLoginRequest req) {
        WechatModel.AccessTokenInfo accessTokenInfo = getAccessToken(req.getCode());
        ThreadLocalUtil.put(ACCESS_TOKEN_INFO, accessTokenInfo);
        return accessTokenInfo.getOpenid();
    }

    /**
     * 获取 access_token
     *
     * @param code 客户端提供的授权码
     * @return access_token
     */
    private WechatModel.AccessTokenInfo getAccessToken(String code) {
        WechatModel.AccessTokenRequest request = WechatModel.AccessTokenRequest.builder()
                .appId(wechatCertProperties.getAppId())
                .secret(wechatCertProperties.getAppSecret())
                .code(code)
                .grantType(PARAM_VALUE_GRANT_TYPE)
                .build();
        Map<String, Object> params = JSONObject.parseObject(JSONObject.toJSONString(request), JSONObject.class);
        WechatModel.AccessTokenResponse response = RetrofitHttpClient.get(URL_ACCESS_TOKEN, params, WechatModel.AccessTokenResponse.class);
        if (response == null || !response.isSuccess()) {
            log.error("{} getAccessToken failed! [{}], [{}]", LOG_TITLE, request, response);
            throw new BizException(RetCodeEnum.RET_INTERNAL_SERVER_ERROR);
        }
        return response.getAccessTokenInfo();
    }

    /**
     * 获取微信用户信息
     *
     * @param accessToken 接口凭证
     * @param openid      用户标识
     * @return 用户信息
     */
    private WechatModel.UserInfo getUserInfo(String accessToken, String openid) {
        WechatModel.UserInfoRequest request = WechatModel.UserInfoRequest.builder()
                .accessToken(accessToken)
                .openid(openid)
                .build();
        Map<String, Object> params = JSONObject.parseObject(JSONObject.toJSONString(request), JSONObject.class);
        WechatModel.UserInfoResponse response = RetrofitHttpClient.get(URL_USERINFO, params, UserInfoResponse.class);
        if (response == null || !response.isSuccess()) {
            log.error("{} getUserInfo failed! [{}], [{}]", LOG_TITLE, request, response);
            throw new BizException(RetCodeEnum.RET_INTERNAL_SERVER_ERROR);
        }
        return response.getUserInfo();
    }

}

