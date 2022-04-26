package com.example.third.protocol;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回
 *
 * @param <T> 业务返回类型
 * @author zhangyubing
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class BaseResp<T> {

    /**
     * 返回码
     */
    private int retCode;

    /**
     * 返回信息
     */
    private String retMsg;

    /**
     * 数据
     */
    private T data;

    public void setRetCode(RetCodeEnum retCode) {
        this.retCode = retCode.getCode();
        this.retMsg = retCode.getDesc();
    }

    public void setErrorResp(int code, String msg) {
        this.retCode = code;
        this.retMsg = msg;
    }

}
