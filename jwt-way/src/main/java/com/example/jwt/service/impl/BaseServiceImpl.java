package com.example.jwt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.enums.GenderEnum;
import com.example.common.exception.BizException;
import com.example.common.request.RefreshTokenRequest;
import com.example.common.response.LoginResponse;
import com.example.common.response.RefreshTokenResponse;
import com.example.common.starter.holder.ContextHolder;
import com.example.jwt.service.IBaseUserService;
import com.example.jwt.dao.entity.BaseUser;
import com.example.jwt.dao.mapper.BaseUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.common.protocol.RetCodeEnum.RET_INTERNAL_SERVER_ERROR;


/**
 * 用户基础信息表 服务实现类
 *
 * @author zhangyubing
 */
@Slf4j
@Service
public class BaseServiceImpl extends ServiceImpl<BaseUserMapper, BaseUser> implements IBaseUserService {

    /**
     * 注册用户
     *
     * @param deviceId   设备号
     * @param nickname   昵称
     * @param genderEnum 性别
     * @return 用户ID
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public long registerUser(String deviceId, String nickname, GenderEnum genderEnum) {
        BaseUser user = createUser(deviceId, nickname, genderEnum);
        boolean save = this.save(user);
        long userId = Optional.ofNullable(user.getUserId())
                // 如果创建用户失败抛出异常
                .orElseThrow(() -> new BizException(RET_INTERNAL_SERVER_ERROR));
        boolean updateById = this.updateById(user);
        log.debug("registerUser userId: {}, updateById:{}", userId, updateById);
        return userId;
    }

    /**
     * 登录
     *
     * @param userId   用户ID
     * @param nickname 昵称
     * @return 用户信息和令牌
     */
    @Override
    public LoginResponse login(Long userId, String nickname) {
        BaseUser baseUser = this.getById(userId);
        if (null == baseUser) {
            throw new BizException(RET_INTERNAL_SERVER_ERROR);
        }
        baseUser.setNickname(nickname);
        // 更新昵称
        this.updateById(baseUser);
        return LoginResponse.builder()
                .userId(userId)
                .nickname(baseUser.getNickname())
                .avatar(baseUser.getAvatar())
                .token(ContextHolder.createToken(userId, false))
                .refreshToken(ContextHolder.createToken(userId, true))
                .build();
    }

    /**
     * 刷新token
     *
     * @param req refreshToken
     * @return 新的token和新的refreshToken
     */
    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest req) {
        // 验证刷新令牌
        ContextHolder.verifyRefreshToken(req.getRefreshToken());
        long userId = ContextHolder.getUserIdFromRefreshToken(req.getRefreshToken());
        return RefreshTokenResponse.builder()
                .token(ContextHolder.createToken(userId, false))
                .refreshToken(ContextHolder.createToken(userId, true))
                .build();
    }

    /**
     * 创建用户
     *
     * @param deviceId   设备ID
     * @param nickname   昵称
     * @param genderEnum 性别
     * @return 用户对象
     */
    private BaseUser createUser(String deviceId, String nickname, GenderEnum genderEnum) {
        BaseUser baseUser = new BaseUser();
        baseUser.setDeviceId(deviceId);
        baseUser.setNickname(nickname);
        baseUser.setGender(genderEnum.getName());

        return baseUser;
    }

}
