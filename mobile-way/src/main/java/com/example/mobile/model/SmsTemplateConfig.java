package com.example.mobile.model;

import lombok.Data;

/**
 * 短信模板配置
 *
 * @author zhangyubing
 */
@Data
public class SmsTemplateConfig {

    /**
     * 短信服务提供商名称pb枚举的number
     */
    private int smsProvider;

    /**
     * 模板Code
     */
    private String templateCode;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 模板内容
     */
    private String templateContent;
}