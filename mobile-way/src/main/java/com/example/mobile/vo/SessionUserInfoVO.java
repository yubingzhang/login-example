package com.example.mobile.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会话的用户信息对象
 *
 * @author zhangyubing
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SessionUserInfoVO {

    /**
     * 登录令牌
     */
    String token;
}
