package com.example.common.starter.holder;


/**
 * 认证白名单配置
 *
 * @author zhangyubing
 */
public interface IAuthWhiteListHolder {

    /**
     * 传入的url是否在白名单中
     *
     * @param url 请求url
     * @return true:跳过登录态鉴权，false不跳过
     */
    boolean isInWhiteList(String url);
}
