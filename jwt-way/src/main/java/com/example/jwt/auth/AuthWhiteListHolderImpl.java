package com.example.jwt.auth;

import com.example.common.starter.holder.IAuthWhiteListHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.example.jwt.constants.CommonConstants.STATIC_IMAGE_AVATAR_DIR;


/**
 * 登录认证白名单实现类
 *
 * @author zhangyubing
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
            add("/login/v1");
            add("/refresh-token/v1");
            add("/check-upgrade/v1");
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
        return WHITE_LIST.contains(url) || url.startsWith(STATIC_IMAGE_AVATAR_DIR);
    }
}
