package com.example.third.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信授权
 *
 * @author zhangyubing
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "wechat.cert")
public class WechatCertProperties {

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用密钥
     */
    private String appSecret;
}
