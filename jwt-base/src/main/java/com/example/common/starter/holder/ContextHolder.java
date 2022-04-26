package com.example.common.starter.holder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.common.exception.AuthorizedException;
import com.example.common.exception.BizException;
import com.example.common.protocol.RetCodeEnum;
import com.example.common.starter.constants.CommonConstant;
import com.example.common.starter.model.HeaderMeta;
import com.example.common.util.ThreadLocalUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;

import static com.example.common.protocol.RetCodeEnum.RET_UNAUTHORIZED;


/**
 * 上下文持有者
 *
 * @author zhangyubing
 */
@Slf4j
public class ContextHolder {

    /**
     * 用户id标识
     */
    private static final String USER_ID = "userId";

    /**
     * 是否是刷新令牌
     */
    private static final String IS_REFRESH_TOKEN = "isRefreshToken";

    /**
     * 跳过登录认证标识
     */
    private static final String SKIP_AUTH = "skip_auth";

    /**
     * refreshToken有效时间，默认90天
     */
    public static final int REFRESH_TOKEN_EXPIRE = 60 * 60 * 24 * 90;

    /**
     * accessToken有效时间，默认7天
     */
    public static final int ACCESS_TOKEN_EXPIRE = 60 * 60 * 24 * 7;

    /**
     * token密钥
     */
    @Getter
    @Setter
    private static String appSecret = "123456";

    public static String getToken() {
        return get(CommonConstant.AUTHORIZATION);
    }

    /**
     * 设置令牌
     *
     * @param token 令牌
     */
    public static void setToken(String token) {
        put(CommonConstant.AUTHORIZATION, token);
    }

    /**
     * 设置请求头sud元数据
     *
     * @param headerSudMeta header里的sud源信息
     */
    public static void setHeaderSudMeta(HeaderMeta headerSudMeta) {
        put(CommonConstant.HEADER_META, headerSudMeta);
    }

    public static HeaderMeta getHeaderSudMeta() {
        return get(CommonConstant.HEADER_META);
    }

    /**
     * 创建token
     *
     * @param userId         用户id
     * @param isRefreshToken true是刷新令牌,false不是刷新令牌
     * @return token
     */
    public static String createToken(long userId, boolean isRefreshToken) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, isRefreshToken ? REFRESH_TOKEN_EXPIRE : ACCESS_TOKEN_EXPIRE);
        String token = JWT.create()
                .withClaim(USER_ID, userId)
                .withClaim(IS_REFRESH_TOKEN, isRefreshToken)
                // 设置过期时间
                .withExpiresAt(instance.getTime())
                // 设置签名
                .sign(Algorithm.HMAC256(appSecret));
        log.debug("create token success, token:{}, appSecret:{}", token, appSecret);
        return token;
    }

    /**
     * 验证accessToken
     *
     * @param accessToken 令牌
     */
    public static void verifyAccessToken(String accessToken) {
        JWTVerifier build = JWT.require(Algorithm.HMAC256(appSecret)).build();
        build.verify(accessToken);
        if (isRefreshToken(build, accessToken)) {
            log.error(">>>>> get accessToken is refreshToken, token:{}", accessToken);
            throw new BizException(RET_UNAUTHORIZED);
        }
    }

    /**
     * 验证refreshToken
     *
     * @param refreshToken 刷新令牌
     */
    public static void verifyRefreshToken(String refreshToken) {
        try {
            JWTVerifier build = JWT.require(Algorithm.HMAC256(appSecret)).build();
            build.verify(refreshToken);
            if (!isRefreshToken(build, refreshToken)) {
                log.error(">>>>> get refreshToken is accessToken, token:{}", refreshToken);
                throw new BizException(RET_UNAUTHORIZED);
            }
        } catch (TokenExpiredException e) {
            log.error(">>>>> Authorized failed({})!", RetCodeEnum.RET_TOKEN_HAS_EXPIRED);
            throw new AuthorizedException(RetCodeEnum.RET_TOKEN_HAS_EXPIRED);
        } catch (Exception e) {
            log.error(">>>>> token authentication failed.", e);
            throw new AuthorizedException(RetCodeEnum.RET_UNAUTHORIZED);
        }
    }

    /**
     * 是否是刷新令牌
     *
     * @param token 令牌
     * @return true 是刷新令牌, false不是刷新令牌
     */
    public static boolean isRefreshToken(JWTVerifier jwtVerifier, String token) {
        return jwtVerifier.verify(token).getClaim(IS_REFRESH_TOKEN).asBoolean();
    }

    private static Long getUserIdFromToken(String token) {
        return JWT.require(Algorithm.HMAC256(appSecret)).build().verify(token).getClaim(USER_ID).asLong();
    }

    /**
     * 从refreshToken中获取userId
     *
     * @param refreshToken 刷新令牌
     * @return userId
     */
    public static Long getUserIdFromRefreshToken(String refreshToken) {
        return JWT.require(Algorithm.HMAC256(appSecret)).build().verify(refreshToken).getClaim(USER_ID).asLong();
    }

    public static long getUserId() {
        return getUserIdFromToken(getToken());
    }

    public static void setUserId(long userId) {
        put(USER_ID, userId);
    }

    public static void setSkipAuth(boolean flag) {
        put(SKIP_AUTH, flag);
    }

    public static boolean getSkipAuth() {
        return get(SKIP_AUTH);
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
