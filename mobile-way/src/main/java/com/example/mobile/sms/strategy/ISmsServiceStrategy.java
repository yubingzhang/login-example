package com.example.mobile.sms.strategy;


import com.example.mobile.model.SmsTemplateConfig;
import com.example.mobile.vo.request.SendSmsCodeRequest;

/**
 * 短信服务策略接口
 *
 * @author zhangybing
 */
public interface ISmsServiceStrategy {

    /**
     * 发送短信验证码
     *
     * @param templateConfig 模板配置
     * @param smsMessage     短信消息
     */
    void sendSms(SmsTemplateConfig templateConfig, SendSmsCodeRequest smsMessage);
}
