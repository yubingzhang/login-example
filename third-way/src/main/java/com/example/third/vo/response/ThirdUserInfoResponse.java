package com.example.third.vo.response;

import lombok.Builder;
import lombok.Data;

/**
 * 第三方用户信息
 *
 * @author zhangyubing
 */
@Builder
@Data
public class ThirdUserInfoResponse {
    /**
     * 用户id
     */
    private Long userId;

    private Integer bindType;

    /**
     * openid
     */
    private String openId;

    /**
     * 昵称
     */
    private String nickname;
}
