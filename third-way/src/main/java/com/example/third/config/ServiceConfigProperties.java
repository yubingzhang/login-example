package com.example.third.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 服务配置
 *
 * @author zhangyubing
 */
@Data
@Component
@ConfigurationProperties(prefix = "service.config")
public class ServiceConfigProperties {

    /**
     * 是否使用短信验证码
     */
    Boolean useSmsCode;

    /**
     * token过期时间（秒）
     */
    Long tokenExpire;

    /**
     * refreshToken过期时间（秒）
     */
    Long refreshTokenExpire;

    /**
     * 用户总数基础数值
     */
    Integer baseTotalUserCount;
}
