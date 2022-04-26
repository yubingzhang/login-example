package com.example.mobile.sms.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.example.mobile.model.SmsTemplateConfig;
import com.example.mobile.vo.request.SendSmsCodeRequest;
import com.example.mobile.service.helper.BaseSmsHelper;
import com.example.mobile.sms.strategy.ISmsServiceStrategy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;


/**
 * 阿里云短信服务
 *
 * @author moxiaofei
 */
@Slf4j
public class AliyunSmsServiceStrategy implements ISmsServiceStrategy {

    @Resource
    private Client client;

    /**
     * 阿里云使用的是模板短信
     *
     * @param templateConfig 模板配置
     * @param smsMessage     短信消息
     */
    @Override
    @SneakyThrows
    public void sendSms(SmsTemplateConfig templateConfig, SendSmsCodeRequest smsMessage) {
        List<String> paramNames = BaseSmsHelper.queryContextParamNames(templateConfig.getTemplateContent());
        if (paramNames.size() != smsMessage.getTemplateParamsList().size()) {
            log.error(">>>>> [Aliyun-Sms]: param count not matching! {}", templateConfig);
            throw new RuntimeException("[Aliyun-Sms]: param count not matching!");
        }
        JSONObject paramJson = new JSONObject();
        for (int i = 0; i < paramNames.size(); i++) {
            paramJson.put(paramNames.get(i), smsMessage.getTemplateParamsList().get(i));
        }
        // 发短信
        SendSmsRequest request = new SendSmsRequest()
                .setPhoneNumbers(smsMessage.getCountryCode() + smsMessage.getPhoneNumber())
                .setSignName(templateConfig.getSignName())
                .setTemplateCode(templateConfig.getTemplateCode())
                .setTemplateParam(paramJson.toJSONString());
        SendSmsResponse response = client.sendSms(request);
        log.debug("sendSms end! [{}], [{}]", JSON.toJSONString(request), JSON.toJSONString(response));
        SendSmsResponseBody body = response.getBody();
    }
}
