package com.example.mobile.auth.holder;

import com.example.mobile.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;

import static com.example.mobile.constants.CommonConstants.HEADER_REFRESH_TOKEN;
import static com.example.mobile.constants.CommonConstants.HEADER_TOKEN;


/**
 * 上下文持有者
 *
 * @author moxiaofei
 */
@Slf4j
public class ContextHolder {

    public static final String USER_ID = "userId";

    public static final String SKIP_AUTH = "skip_auth";
    public static final String SKIP_CRYPT = "skip_crypt";

    public static String getToken() {
        return get(HEADER_TOKEN);
    }

    public static void setToken(String token) {
        put(HEADER_TOKEN, token);
    }

    public static String getRefreshToken() {
        return get(HEADER_REFRESH_TOKEN);
    }

    public static void setRefreshToken(String refreshToken) {
        put(HEADER_REFRESH_TOKEN, refreshToken);
    }

    public static Long getUserId() {
        return get(USER_ID);
    }

    public static void setUserId(Long userId) {
        put(USER_ID, userId);
    }

    public static void setSkipAuth(boolean flag) {
        put(SKIP_AUTH, flag);
    }

    public static boolean getSkipAuth() {
        return get(SKIP_AUTH);
    }

    public static void setSkipCrypt(boolean flag) {
        put(SKIP_CRYPT, flag);
    }

    public static boolean getSkipCrypt() {
        return get(SKIP_CRYPT);
    }


    public static <T> T get(String key) {
        return ThreadLocalUtil.get(key);
    }

    public static <T> void put(String key, T value) {
        ThreadLocalUtil.put(key, value);
    }

    public static void clear() {
        ThreadLocalUtil.clear();
    }

}
