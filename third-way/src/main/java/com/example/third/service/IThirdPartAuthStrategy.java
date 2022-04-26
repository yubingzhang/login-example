package com.example.third.service;


import com.example.third.vo.request.ThirdLoginRequest;
import com.example.third.vo.response.LoginResponse;
import com.example.third.vo.response.ThirdUserInfoResponse;

/**
 * 第三方授权 策略接口
 *
 * @author zhangyubing
 */
public interface IThirdPartAuthStrategy {
    /**
     * 登录
     *
     * @param req 第三方授权请求信息
     * @return 登录响应
     */
    LoginResponse login(ThirdLoginRequest req);

    /**
     * 获取第三方信息
     *
     * @param req 第三方授权请求信息
     * @return 第三方用户信息
     */
    ThirdUserInfoResponse getThirdInfo(ThirdLoginRequest req);

    /**
     * 获取用户第三方唯一标识
     *
     * @param req 第三方授权请求信息
     * @return 用户唯一标识
     */
    String getOpenid(ThirdLoginRequest req);
}
