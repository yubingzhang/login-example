package com.example.mobile.constants;

/**
 * redis常量
 *
 * @author zhangybuing
 */
public final class RedisConstant {

    /**
     * session key 前缀
     */
    public static final String SESSION_KEY_PREFIX = "gs:session:s:";

    /**
     * user key 前缀
     */
    public static final String USER_KEY_PREFIX = "gs:session:u:";

    /**
     * login lock 前缀
     */
    public static final String LOGIN_LOCK_KEY_PREFIX = "gs:session:login:lock:";

    /**
     * session指向的对象
     *
     * @param token token
     * @return redis key
     */
    public static String getSessionKey(String token) {
        return SESSION_KEY_PREFIX + token;
    }

    /**
     *
     * @param userId 用户id
     * @return redis key
     */
    public static String getUserKey(Long userId) {
        return USER_KEY_PREFIX + userId;
    }

    /**
     *
     * @param userId 用户id
     * @return redis key
     */
    public static String getLoginLockKey(Long userId) {
        return LOGIN_LOCK_KEY_PREFIX + userId;
    }


}
