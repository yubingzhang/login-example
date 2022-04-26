package com.example.third.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QQ
 *
 * @author moxiaofei
 */
@Data
public class QqModel {

    public static final String URL_USERINFO = "https://openmobile.qq.com/user/get_simple_userinfo";
    public static final String URL_ME = "https://graph.qq.com/oauth2.0/me";

    /**
     * 获取我的信息请求
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeInfoRequest {
        /**
         * 用户凭证
         */
        @JSONField(name = "access_token")
        private String accessToken;

        /**
         * 是否申请unionID
         */
        @JSONField(name = "unionid")
        private int unionId;

        /**
         * 默认是jsonpb格式，如果填写json，则返回json格式
         */
        private String fmt;
    }

    /**
     * 获取我的信息响应
     */
    @Data
    public static class MeInfoResponse extends MeInfo {
        /**
         * 错误码
         */
        private int error;
        /**
         * 错误信息
         */
        @JSONField(name = "error_description")
        private String errorDescription;

        public boolean isSuccess() {
            return this.error == 0;
        }

        public MeInfo getMeInfo() {
            return this;
        }
    }

    /**
     * 我的信息
     */
    @Data
    public static class MeInfo {
        /**
         * 应用唯一标识
         */
        @JSONField(name = "client_id")
        private String clientId;
        /**
         * QQ用户在应用的唯一账号标识，同一个用户在不同应用的openid不一样
         */
        private String openid;
        /**
         * QQ用户在开发者在多个应用间（打通后）的标识，打通后，不同应用的unionid一样
         */
        private String unionid;
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
         * 用户凭证
         */
        @JSONField(name = "access_token")
        private String accessToken;

        /**
         * 应用ID
         */
        @JSONField(name = "oauth_consumer_key")
        private String oauthConsumerKey;

        /**
         * 用户唯一标识
         */
        private String openid;
    }

    /**
     * 获取用户信息响应
     */
    @Data
    public static class UserInfoResponse extends UserInfo {
        /**
         * 返回码
         */
        private int ret;

        /**
         * 错误信息
         */
        private String msg;

        public boolean isSuccess() {
            return this.ret == 0;
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

        private static final String MALE = "男";
        private static final String FEMALE = "女";

        /**
         * 用户在QQ空间的昵称
         */
        private String nickname;

        /**
         * 大小为30×30像素的QQ空间头像URL
         */
        @JSONField(name = "figureurl")
        private String figureUrl;

        /**
         * 大小为50×50像素的QQ空间头像URL
         */
        @JSONField(name = "figureurl_1")
        private String figureUrl1;

        /**
         * 大小为100×100像素的QQ空间头像URL
         */
        @JSONField(name = "figureurl_2")
        private String figureUrl2;

        /**
         * 大小为40×40像素的QQ头像URL
         */
        @JSONField(name = "figureurl_qq_1")
        private String figureUrlQq1;

        /**
         * 大小为100×100像素的QQ头像URL
         */
        @JSONField(name = "figureurl_qq_2")
        private String figureUrlQq2;

        /**
         * 性别。 如果获取不到则默认返回"男"
         */
        private String gender;

        public String getGender() {
            if (MALE.equals(gender)) {
                //return GenderEnum.MALE.getName();
            } else if (FEMALE.equals(gender)) {
                //return GenderEnum.FEMALE.getName();
            }
            return "";
        }
    }

}
