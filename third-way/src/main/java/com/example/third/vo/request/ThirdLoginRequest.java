package com.example.third.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 三方登录请求
 *
 * @author zhangyubing
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdLoginRequest {

    /**
     * 三方类型(1:QQ 2:微信 3：支付宝 4：Apple)
     */
    @NotNull
    Integer thirdType;

    /**
     * 授权码
     */
    @NotNull
    String code;

}
