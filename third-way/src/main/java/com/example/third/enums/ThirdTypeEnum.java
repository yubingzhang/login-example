package com.example.third.enums;

import lombok.Getter;

/**
 * 三方类型枚举
 *
 * @author zhangyubing
 */
public enum ThirdTypeEnum {

    /**
     * QQ
     */
    THIRD_TYPE_QQ(1,"qqAuthStrategy"),

    /**
     * 微信
     */
    THIRD_TYPE_WECHAT(2,"wechatAuthStrategy"),

    /**
     * 支付宝
     */
    THIRD_TYPE_ALIPAY(3,"alipayAuthStrategy"),

    /**
     * apple
     */
    THIRD_TYPE_APPLE(4,"appleAuthStrategy");

    @Getter
    private final int value;

    @Getter
    private final String impl;


    ThirdTypeEnum(int value,String impl) {
        this.value = value;
        this.impl = impl;
    }

    public static ThirdTypeEnum getInstance(int value) {
        for (ThirdTypeEnum thirdTypeEnum: ThirdTypeEnum.values()) {
            if (thirdTypeEnum.getValue() == value) {
                return thirdTypeEnum;
            }
        }
        return null;
    }
}
