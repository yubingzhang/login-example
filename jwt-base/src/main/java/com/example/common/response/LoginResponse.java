package com.example.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录返回
 *
 * @author zhangyubing
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    /**
     * 头像
     */
    String avatar;

    /**
     * 用户ID
     */
    Long userId;

    /**
     * 昵称
     */
    String nickname;

    /**
     * 登录令牌
     */
    String token;

    /**
     * token过期时间
     */
    Long tokenExpire;

    /**
     * 刷新令牌
     */
    String refreshToken;
}
