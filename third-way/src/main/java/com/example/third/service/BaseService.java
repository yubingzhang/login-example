package com.example.third.service;

import com.example.third.config.ServiceConfigProperties;
import com.example.third.model.UserBaseInfo;
import com.example.third.vo.SessionInfoVO;
import com.example.third.vo.response.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 基础服务
 *
 * @author zhangyubing
 */
@Slf4j
@Component
public class BaseService {

    @Resource
    private ServiceConfigProperties serviceConfigProperties;


    /**
     * 登录
     *
     * @return 登录返回
     */
    public LoginResponse doLogin(UserBaseInfo userBaseMessage) {
        //UserBaseVO userBaseVO = toUserBaseVO(userBaseMessage);
        // 生成token
        SessionInfoVO sessionInfoVO = null;

        return LoginResponse.builder()
                .token(sessionInfoVO.getToken())
                .refreshToken(sessionInfoVO.getRefreshToken())
                .tokenExpire(serviceConfigProperties.getTokenExpire())
                //.user(userBaseVO)
                .build();
    }


}
