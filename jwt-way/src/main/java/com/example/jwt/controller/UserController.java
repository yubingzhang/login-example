package com.example.jwt.controller;

import com.example.common.protocol.BaseResp;
import com.example.common.request.LoginRequest;
import com.example.common.request.RefreshTokenRequest;
import com.example.common.response.LoginResponse;
import com.example.common.response.RefreshTokenResponse;
import com.example.common.util.BaseRespUtil;
import com.example.jwt.service.IBaseUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户服务
 *
 * @author zhangyubing
 */
@Slf4j
@RestController
public class UserController {

    /**
     * 基础服务
     */
    @Resource
    private IBaseUserService baseUserService;


    /**
     * 登录接口，如果userId为空则根据其他信息创建新用户，不为空根据用户id进行登录
     * 接入方可以自行实现登录逻辑
     *
     * @param req 用户信息
     * @return 登录返回
     */
    @PostMapping("/login/v1")
    public BaseResp<LoginResponse> login(@Validated @RequestBody LoginRequest req) {
        Long userId = req.getUserId();
        if (null == req.getUserId()) {
            // 注册游客账号
            userId = baseUserService.registerUser(req.getDeviceId(), req.getNickname(), req.getGenderEnum());
            log.info("registerUser success, userId:{}", userId);
        }
        // 登录
        LoginResponse loginResponse = baseUserService.login(userId, req.getNickname());
        log.info("login userId:{}, ret:{}", userId, loginResponse);
        return BaseRespUtil.success(req, loginResponse);
    }

    /**
     * 刷新令牌接口
     *
     * @param req 刷新令牌
     * @return 新令牌和新刷新令牌
     */
    @PostMapping("/refresh-token/v1")
    public BaseResp<RefreshTokenResponse> refreshToken(@Validated @RequestBody RefreshTokenRequest req) {
        return BaseRespUtil.success(req, baseUserService.refreshToken(req));
    }

}
