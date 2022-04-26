package com.example.common.protocol;

import lombok.Getter;

/**
 * 返回码枚举
 *
 * @author zhangyubing
 */
public enum RetCodeEnum {
    /**
     * 成功
     */
    SUCCESS(0, "成功"),

    // 公共错误码【1000-1999】
    RET_INTERNAL_SERVER_ERROR(1000, "内部服务异常"),
    RET_UNAUTHORIZED(1001, "未登录"),
    RET_TOKEN_HAS_EXPIRED(1002, "登录已过期，请重新登录"),
    RET_TOKEN_CANNOT_EMPTY(1003, "token不能为空"),
    RET_HTTP_MESSAGE_NOT_READABLE(1004, "http参数不可读"),
    RET_CONFIG_DEFECT(1005, "配置缺失"),
    RET_JSON_PARSE_ERROR(1006, "json解析异常"),
    RET_UPLOAD_IMAGE_FORMAT_UN_SUPPORT(1007, "图片格式不支持"),
    RET_UPLOAD_IMAGE_ERROR(1008, "上传图片失败"),
    RET_TOKEN_HAS_NOT_EXIST(1009, "token不存在"),
    RET_PARAMS_IS_INVALID(1010, "参数非法"),
    RET_HEADER_INCORRECT_FORMAT(1011, "HEADER格式不正确"),
    RET_TOKEN_HAS_KICKED_OUT(1012, "你的账号在另一设备登录,你被迫下线,如果不是你本人操作,建议修改密码");

    @Getter
    private final String desc;

    @Getter
    private final int code;

    RetCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
