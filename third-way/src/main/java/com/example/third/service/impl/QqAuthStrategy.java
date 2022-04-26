package com.example.third.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.third.config.QqCertProperties;
import com.example.third.exception.BizException;
import com.example.third.model.QqModel;
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

import static com.example.third.enums.ThirdTypeEnum.THIRD_TYPE_QQ;
import static com.example.third.model.QqModel.URL_ME;
import static com.example.third.model.WechatModel.URL_USERINFO;
import static com.example.third.protocol.RetCodeEnum.RET_INTERNAL_SERVER_ERROR;


/**
 * QQ授权
 *
 * @author moxiaofei
 */
@Slf4j
@Service("qqAuthStrategy")
public class QqAuthStrategy extends AbstractThirdPartAuthStrategy {

    private static final String LOG_TITLE = "[QQ-Auth]";
    private static final String ME_INFO = "me_info";

    @Resource
    private QqCertProperties certProperties;

    @Override
    public LoginResponse login(ThirdLoginRequest req) {
        try {
            return super.login(req);
        } finally {
            ThreadLocalUtil.remove(ME_INFO);
        }
    }

    @Override
    public ThirdUserInfoResponse getThirdInfo(ThirdLoginRequest req) {
        QqModel.MeInfo meInfo = ThreadLocalUtil.getAndRemove(ME_INFO);
        if (Objects.isNull(meInfo)) {
            meInfo = getMeInfo(req.getCode());
        }
        QqModel.UserInfo userInfo = getUserInfo(req.getCode(), meInfo.getOpenid());

        return ThirdUserInfoResponse.builder()
                .openId(meInfo.getOpenid())
                //.(StringValue.of(meInfo.getUnionid()))
                .bindType(THIRD_TYPE_QQ.getValue())
                .build();
    }

    @Override
    public String getOpenid(ThirdLoginRequest req) {
        QqModel.MeInfo meInfo = getMeInfo(req.getCode());
        ThreadLocalUtil.put(ME_INFO, meInfo);
        return meInfo.getOpenid();
    }

    /**
     * 获取我的信息
     *
     * @param accessToken 用户凭证
     * @return 我的信息
     */
    private QqModel.MeInfo getMeInfo(String accessToken) {
        QqModel.MeInfoRequest request = QqModel.MeInfoRequest.builder()
                .accessToken(accessToken)
                .unionId(1)
                .fmt("json")
                .build();
        Map<String, Object> params = (JSONObject) JSONObject.toJSON(request);
        QqModel.MeInfoResponse response = RetrofitHttpClient.get(URL_ME, params, QqModel.MeInfoResponse.class);
        if (response == null || !response.isSuccess()) {
            log.error("{} getMeInfo failed! [{}], [{}]", LOG_TITLE, request, response);
            throw new BizException(RET_INTERNAL_SERVER_ERROR);
        }
        return response.getMeInfo();
    }

    /**
     * 获取用户信息
     *
     * @param accessToken 用户凭证
     * @param openid      用户标识
     * @return 用户信息
     */
    public QqModel.UserInfo getUserInfo(String accessToken, String openid) {
        QqModel.UserInfoRequest request = QqModel.UserInfoRequest.builder()
                .accessToken(accessToken)
                .openid(openid)
                .oauthConsumerKey(certProperties.getAppId())
                .build();
        Map<String, Object> params = (JSONObject) JSONObject.toJSON(request);
        QqModel.UserInfoResponse response = RetrofitHttpClient.get(URL_USERINFO, params, QqModel.UserInfoResponse.class);
        if (response == null || !response.isSuccess()) {
            log.error("{} getUserInfo failed! [{}], [{}]", LOG_TITLE, request, response);
            throw new BizException(RET_INTERNAL_SERVER_ERROR);
        }
        return response.getUserInfo();
    }

}
