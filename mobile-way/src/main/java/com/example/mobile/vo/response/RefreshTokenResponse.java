package com.example.mobile.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 刷新token返回
 *
 * @author zhangyubing
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RefreshTokenResponse {

    /**
     * 登录令牌
     */
    String token;

    /**
     * 过期时间（秒）
     */
    Long tokenExpire;

    /**
     * 刷新令牌
     */
    String refreshToken;
}
