package com.example.third.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * QQ授权
 *
 * @author zhanguubing
 */

@Data
@Component
@ConfigurationProperties(prefix = "qq.cert")
public class QqCertProperties {

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用密钥
     */
    private String appSecret;
}
