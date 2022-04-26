package com.example.mobile.service;

import com.alibaba.fastjson.JSONObject;
import com.example.mobile.auth.holder.ContextHolder;
import com.example.mobile.config.ServiceConfigProperties;
import com.example.mobile.constants.RedisConstant;
import com.example.mobile.exception.BizException;
import com.example.mobile.vo.SessionInfoVO;
import com.example.mobile.vo.SessionUserInfoVO;
import com.example.mobile.vo.response.RefreshTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

import static com.example.mobile.protocol.RetCodeEnum.*;

/**
 * 会话服务
 *
 * @author zhangyubing
 */
@Slf4j
@Component
public class SessionService {

    @Resource
    RedissonClient redissonClient;

    @Resource
    ServiceConfigProperties serviceConfigProperties;

    /**
     * 创建session信息
     *
     * @param userId 用户ID
     * @return session信息
     */
    public SessionInfoVO createSessionInfo(Long userId) {
        JSONObject payload = new JSONObject();
        payload.put("user_id", userId);
        payload.put("timestamp", System.currentTimeMillis());
        String token = DigestUtils.md5Hex(String.valueOf(payload.toJSONString()));
        payload.put("grant_type", "refresh");
        String refreshToken = DigestUtils.md5Hex(String.valueOf(payload.toJSONString()));
        return SessionInfoVO.builder()
                .userId(userId)
                .token(token)
                .refreshToken(refreshToken)
                .tokenExpire(System.currentTimeMillis() + serviceConfigProperties.getTokenExpire() * 1000)
                .kickedOut(false)
                .build();
    }

    /**
     * 生成并保持session
     *
     * @param userId 用户id
     * @return session信息
     */
    public SessionInfoVO generateAndSaveToken(long userId) {
        RLock lock = redissonClient.getLock(RedisConstant.getLoginLockKey(userId));
        lock.lock();
        try {
            log.debug("get the login lock success! key: {}, userid:{}", lock.getName(), userId);
            // 判断用户是否已经登陆, 已登陆则踢掉原先账号
            RBucket<String> userInfoBucket = redissonClient.getBucket(RedisConstant.getUserKey(userId));
            if (userInfoBucket.isExists()) {
                SessionUserInfoVO sessionUserInfoVO = JSONObject.parseObject(userInfoBucket.get(), SessionUserInfoVO.class);
                // 设置sessionId指向的对象为被踢出
                RBucket<String> sessionBucket = redissonClient.getBucket(RedisConstant.getSessionKey(sessionUserInfoVO.getToken()));
                if (sessionBucket.isExists()) {
                    SessionInfoVO sessionInfoVO = JSONObject.parseObject(sessionBucket.get(), SessionInfoVO.class);
                    sessionInfoVO.setKickedOut(true);
                    // 踢掉的token保存10分钟
                    sessionBucket.set(JSONObject.toJSONString(sessionInfoVO), 60 * 10, TimeUnit.SECONDS);
                    log.debug("kicked out token:{}", sessionUserInfoVO.getToken());
                }
            }

            // 构建SessionInfoVO, 用于sessionId指向的对象
            SessionInfoVO sessionInfo = createSessionInfo(userId);
            // 保存sessionId指向的对象
            redissonClient.getBucket(RedisConstant.getSessionKey(sessionInfo.getToken()))
                    .set(JSONObject.toJSONString(sessionInfo), serviceConfigProperties.getRefreshTokenExpire(), TimeUnit.SECONDS);

            // 构建SessionUserInfoVO, 用于userId指向的对象
            SessionUserInfoVO sessionUserInfoVO = SessionUserInfoVO.builder()
                    .token(sessionInfo.getToken())
                    .build();
            userInfoBucket.set(JSONObject.toJSONString(sessionUserInfoVO),
                    serviceConfigProperties.getRefreshTokenExpire(), TimeUnit.SECONDS);

            return sessionInfo;
        } finally {
            log.debug("login lock unlock! key:{}", lock.getName());
            lock.unlock();
        }
    }

    /**
     * 获取session
     *
     * @param token token
     * @return session信息
     */
    public SessionInfoVO getSession(@NotNull String token) {
        RBucket<String> bucket = redissonClient.getBucket(RedisConstant.getSessionKey(token));
        if (!bucket.isExists()) {
            log.error(">>>>> get session error[session not exist]! {}", token);
            throw new BizException(RET_UNAUTHORIZED);
        }
        SessionInfoVO sessionInfoVO = JSONObject.parseObject(bucket.get(), SessionInfoVO.class);
        // 校验token是否有效
        validatedToken(sessionInfoVO);
        return sessionInfoVO;
    }

    /**
     * 校验token是否有效
     *
     * @param sessionInfoVO session信息
     */
    private void validatedToken(SessionInfoVO sessionInfoVO) {
        if (sessionInfoVO.getTokenExpire() < System.currentTimeMillis()) {
            log.error(">>>>> validatedToken [token is expire]! {}", sessionInfoVO.getToken());
            throw new BizException(RET_TOKEN_HAS_EXPIRED);
        }
        if (sessionInfoVO.getKickedOut()) {
            log.warn(">>>>> validatedToken [token has being kicked out]! {}", sessionInfoVO.getToken());
            throw new BizException(RET_TOKEN_HAS_KICKED_OUT);
        }
    }

    /**
     * 校验refreshToken是否有效
     *
     * @param sessionInfoVO session信息
     * @param refreshToken  refreshToken
     */
    private void validatedRefreshToken(SessionInfoVO sessionInfoVO, String refreshToken) {
        if (sessionInfoVO.getKickedOut()) {
            log.warn(">>>>> validatedRefreshToken [token has being kicked out]! {}", sessionInfoVO.getToken());
            throw new BizException(RET_TOKEN_HAS_KICKED_OUT);
        }
        if (!refreshToken.equals(sessionInfoVO.getRefreshToken())) {
            log.error(">>>>> validatedRefreshToken [refresh token not matching]! {}", refreshToken);
            throw new BizException(RET_TOKEN_HAS_EXPIRED);
        }
    }

    /**
     * 刷新token
     *
     * @param refreshToken 刷新令牌
     * @return 新token, refreshToken
     */
    public RefreshTokenResponse refreshToken(String refreshToken) {
        String token = ContextHolder.getToken();
        RBucket<String> bucket = redissonClient.getBucket(
                RedisConstant.getSessionKey(token));
        if (!bucket.isExists()) {
            log.error(">>>>> refresh token error[session not exists]! {}", refreshToken);
            throw new BizException(RET_TOKEN_HAS_EXPIRED);
        }
        SessionInfoVO sessionInfoVO = JSONObject.parseObject(bucket.get(), SessionInfoVO.class);
        // 校验refreshToken是否有效
        validatedRefreshToken(sessionInfoVO, refreshToken);
        // 重新生成并保存回话
        SessionInfoVO newSessionInfoVO = generateAndSaveToken(sessionInfoVO.getUserId());

        return RefreshTokenResponse.builder()
                .token(newSessionInfoVO.getToken())
                .tokenExpire(newSessionInfoVO.getTokenExpire())
                .refreshToken(newSessionInfoVO.getRefreshToken())
                .build();
    }
}
