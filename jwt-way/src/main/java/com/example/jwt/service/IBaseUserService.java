package com.example.jwt.service;


import com.example.common.enums.GenderEnum;
import com.example.common.request.RefreshTokenRequest;
import com.example.common.response.LoginResponse;
import com.example.common.response.RefreshTokenResponse;

/**
 * <p>
 * 用户基础信息表 服务类
 * </p>
 *
 * @author zhangybuing
 */
public interface IBaseUserService {
    /**
     * 注册用户
     *
     * @param deviceId   设备号
     * @param nickname   昵称
     * @param genderEnum 性别
     * @return 用户ID
     */
    long registerUser(String deviceId, String nickname, GenderEnum genderEnum);

    /**
     * 登录
     *
     * @param userId   用户ID
     * @param nickname 昵称
     * @return 登录返回
     */
    LoginResponse login(Long userId, String nickname);

    /**
     * 刷新token
     *
     * @param req refreshToken
     * @return 新的token和新的refreshToken
     */
    RefreshTokenResponse refreshToken(RefreshTokenRequest req);
}
