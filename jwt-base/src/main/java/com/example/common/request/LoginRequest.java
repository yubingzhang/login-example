package com.example.common.request;

import com.example.common.enums.GenderEnum;
import lombok.Data;

/**
 * 登录请求
 *
 * @author moxiaofei
 */
@Data
public class LoginRequest {

    /**
     * 用户ID,为null则注册新用户，不为null则登录
     */
    Long userId;

    /**
     * 设备ID
     */
    String deviceId;

    /**
     * 昵称
     */
    String nickname;

    /**
     * 性别(默认男)
     */
    GenderEnum genderEnum = GenderEnum.MALE;

}
