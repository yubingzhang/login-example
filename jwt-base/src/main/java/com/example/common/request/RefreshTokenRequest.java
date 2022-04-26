package com.example.common.request;

import lombok.Data;


/**
 * @author zhangyubing
 */
@Data
public class RefreshTokenRequest {

    /**
     * 刷新令牌
     */
    String refreshToken;
}
