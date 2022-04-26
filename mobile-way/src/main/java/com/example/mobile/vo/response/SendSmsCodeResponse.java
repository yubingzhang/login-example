package com.example.mobile.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送短信验证码返回
 *
 * @author zhangyubing
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SendSmsCodeResponse {

    /**
     * 重复发送间隔（秒）
     */
    Long reSendInterval;
}
