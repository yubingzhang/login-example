package com.example.mobile.service;

import com.example.mobile.config.ServiceConfigProperties;
import com.example.mobile.constants.CommonConstants;
import com.example.mobile.vo.SessionInfoVO;
import com.example.mobile.vo.response.LoginResponse;
import com.example.mobile.vo.response.SendSmsCodeResponse;
import com.example.mobile.service.helper.BaseSmsHelper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
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
    private SessionService sessionService;

    @Resource
    private ServiceConfigProperties serviceConfigProperties;

    @Resource
    private RedissonClient redissonClient;


    /**
     * 登录
     *
     * @return 登录返回
     */
    public LoginResponse doLogin(String countryCode, String phoneNumber) {
        //UserBaseVO userBaseVO = toUserBaseVO(userBaseMessage);
        // 生成token
        SessionInfoVO sessionInfoVO = sessionService.generateAndSaveToken(123L);

        return LoginResponse.builder()
                .token(sessionInfoVO.getToken())
                .refreshToken(sessionInfoVO.getRefreshToken())
                .tokenExpire(serviceConfigProperties.getTokenExpire())
                //.user(userBaseVO)
                .build();
    }

    /**
     * 发送登录验证码
     *
     * @param countryCode 地区吗
     * @param phoneNumber 手机号
     */
    public SendSmsCodeResponse sendLoginSmsCode(String countryCode, String phoneNumber) {
        // 不发送验证码
        /*if (!serviceConfigProperties.getUseSmsCode()) {
            return SendSmsCodeResponse.builder().reSendInterval(DEFAULT_RE_SEND_INTERVAL).build();
        }*/

        // 短信验证码位数
        int figures = CommonConstants.DEFAULT_SMS_CODE_FIGURES;

        String code = BaseSmsHelper.generate(figures);
        // 发送code
        return SendSmsCodeResponse.builder().build();
    }

    /**
     * 校验验证码然后登录
     *
     * @param countryCode 国家代码
     * @param phoneNumber 手机号
     * @param code        验证码
     * @return 用户信息
     */
    public LoginResponse verifySmsCodeAndLogin(String countryCode, String phoneNumber, String code) {
        // 校验验证码
        if (serviceConfigProperties.getUseSmsCode()) {
            verifySmsCode(countryCode, phoneNumber, code, "");
        }
        // 校验通过，登录
        return doLogin(countryCode, phoneNumber);
    }

    /**
     * 校验验证码
     *
     * @param countryCode 国家代码
     * @param phoneNumber 手机号
     * @param code        验证码
     * @param event       短信事件
     */
    private void verifySmsCode(String countryCode, String phoneNumber, String code, String event) {
        String redisKey = "";
        RBucket<String> redisBucket = redissonClient.getBucket(redisKey);
        if (!redisBucket.isExists()) {
            log.error(">>>>> verifySmsCode failed[sms code record not exists]! [{}]", code);
            return;
        }
        if (!code.equals(redisBucket.get())) {
            log.error(">>>>> verifySmsCode failed[sms code not matching]! [{}]",code);
            return;
        }
        // 验证完后清理对应的缓存
        redisBucket.delete();
    }
}
