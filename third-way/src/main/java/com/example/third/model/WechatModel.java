package com.example.third.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信
 *
 * @author moxiaofei
 */
@Data
public class WechatModel {

    /**
     * url路径
     */
    public static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static final String URL_USERINFO = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * 参数值
     */
    public static final String PARAM_VALUE_GRANT_TYPE = "authorization_code";

    /**
     * 获取access_token请求
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccessTokenRequest {
        /**
         * 应用唯一标识
         */
        @JSONField(name = "appid")
        private String appId;

        /**
         * 应用密钥
         */
        private String secret;

        /**
         * 授权码
         */
        private String code;

        /**
         * 授权类型
         */
        @JSONField(name = "grant_type")
        private String grantType;
    }

    /**
     * 获取access_token 响应
     */
    @Data
    public static class AccessTokenResponse extends AccessTokenInfo {
        /**
         * 错误码
         */
        @JSONField(name = "errcode")
        private int errCode;

        /**
         * 错误消息
         */
        @JSONField(name = "errmsg")
        private String errmsg;

        public boolean isSuccess() {
            return this.errCode == 0;
        }

        public AccessTokenInfo getAccessTokenInfo() {
            return this;
        }
    }

    /**
     * access_token 信息
     */
    @Data
    public static class AccessTokenInfo {
        /**
         * 接口调用凭证
         */
        @JSONField(name = "access_token")
        private String accessToken;

        /**
         * access_token 接口调用凭证超时时间，单位（秒）
         */
        @JSONField(name = "expires_in")
        private long expiresIn;

        /**
         * 用户刷新 access_token
         */
        @JSONField(name = "refresh_token")
        private String refreshToken;

        /**
         * 授权用户唯一标识
         */
        private String openid;

        /**
         * 用户授权的作用域，使用逗号（,）分隔
         */
        private String scope;
    }

    /**
     * 获取用户信息请求
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoRequest {
        /**
         * 调用凭证
         */
        @JSONField(name = "access_token")
        private String accessToken;

        /**
         * 普通用户的标识，对当前开发者帐号唯一
         */
        private String openid;

        /**
         * 国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语，默认为 en
         */
        private String lang;
    }

    /**
     * 获取用户信息响应
     */
    @Data
    public static class UserInfoResponse extends UserInfo {
        /**
         * 错误码
         */
        @JSONField(name = "errcode")
        private int errCode;

        /**
         * 错误消息
         */
        @JSONField(name = "errmsg")
        private String errmsg;

        public boolean isSuccess() {
            return this.errCode == 0;
        }

        public UserInfo getUserInfo() {
            return this;
        }
    }

    /**
     * 用户信息
     */
    @Data
    public static class UserInfo {
        /**
         * 普通用户的标识，对当前开发者帐号唯一
         */
        private String openid;

        /**
         * 普通用户昵称
         */
        private String nickname;

        /**
         * 普通用户性别，1 为男性，2 为女性
         */
        private int sex;

        /**
         * 普通用户个人资料填写的省份
         */
        private String province;

        /**
         * 普通用户个人资料填写的城市
         */
        private String city;

        /**
         * 国家，如中国为 CN
         */
        private String country;

        /**
         * 用户头像，最后一个数值代表正方形头像大小（有 0、46、64、96、132 数值可选，0 代表 640*640 正方形头像），用户没有头像时该项为空
         */
        @JSONField(name = "headimgurl")
        private String headImgUrl;

        /**
         * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的 unionid 是唯一的
         */
        @JSONField(name = "unionid")
        private String unionId;

        /**
         * 获取性别字符串
         *
         * @return
         */
        public String getGender() {
            String gender = "";
            if (this.sex == 1) {
                //gender = GenderEnum.MALE.getName();
            } else if (this.sex == 2) {
               // gender = GenderEnum.FEMALE.getName();
            }
            return gender;
        }
    }

}
