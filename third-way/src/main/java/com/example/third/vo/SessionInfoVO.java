package com.example.third.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * session信息
 *
 * @author zhangyubing
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SessionInfoVO {
    /**
     * 用户ID
     */
    Long userId;

    /**
     * token
     */
    String token;

    /**
     * 刷新令牌
     */
    String refreshToken;

    /**
     * token过期时间
     */
    Long tokenExpire;

    /**
     * true:踢出,false未踢出
     */
    Boolean kickedOut;
}
