package com.example.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 刷新令牌返回
 *
 * @author zhangyubing
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse {

    /**
     * 令牌
     */
    String token;

    /**
     * 刷新令牌
     */
    String refreshToken;

}
