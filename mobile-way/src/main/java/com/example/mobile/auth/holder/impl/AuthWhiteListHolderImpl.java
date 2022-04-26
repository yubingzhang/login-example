package com.example.mobile.auth.holder.impl;

import com.example.mobile.auth.holder.IAuthWhiteListHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 登录认证白名单实现类
 *
 * @author moxiaofei
 */
@Component
public class AuthWhiteListHolderImpl implements IAuthWhiteListHolder {

    /**
     * 鉴权访问url白名单
     * 此白名单内部url无需登录态鉴权
     */
    public static final Set<String> WHITE_LIST = new HashSet<String>() {
        {
            // 登录接口跳过登录认证
            add("/base/login/send-sms-code/v1");
            add("/base/login/verify-sms-code/v1");
            add("/base/refresh-token/v1");

            add("/callback/login/v1");
            add("/callback/exit/v1");
        }
    };

    /**
     * 传入的url是否在白名单中
     *
     * @param url 请求url
     * @return true: 跳过登录态鉴权，false：不跳过登录态鉴权
     */
    @Override
    public boolean isInWhiteList(String url) {
        return WHITE_LIST.contains(url);
    }
}
