package com.example.third.service;

import com.example.third.enums.ThirdTypeEnum;
import com.example.third.exception.BizException;
import com.example.third.model.UserBaseInfo;
import com.example.third.vo.request.ThirdLoginRequest;
import com.example.third.vo.response.LoginResponse;
import com.example.third.vo.response.ThirdUserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.function.Supplier;

import static com.example.third.protocol.RetCodeEnum.RET_INTERNAL_SERVER_ERROR;


/**
 * 第三方授权Service
 *
 * @author moxiaofei
 */
@Slf4j
@Service
public class ThirdPartAuthService {

    private static final String LOG_TITLE = "[Third-Part-Auth]";

    @Resource
    BaseService baseService;

    @Resource
    private Map<String, IThirdPartAuthStrategy> thirdPartAuthStrategyContainer;

    /**
     * 授权登录
     *
     * @param thirdLoginRequest 第三方授权信息
     * @return 登录响应
     */
    public LoginResponse login(ThirdLoginRequest thirdLoginRequest) {
        return getStrategy(ThirdTypeEnum.getInstance(thirdLoginRequest.getThirdType())).login(thirdLoginRequest);
    }

    /**
     * 获取第三方用户信息
     *
     * @param thirdLoginRequest 第三方授权信息
     * @return 第三方用户信息
     */
    public ThirdUserInfoResponse getThirdUserInfo(ThirdLoginRequest thirdLoginRequest) {
        return getStrategy(ThirdTypeEnum.getInstance(thirdLoginRequest.getThirdType())).getThirdInfo(thirdLoginRequest);
    }

    /**
     * 获取第三方授权策略
     *
     * @param thirdType 第三方类型
     * @return 第三方授权策略
     */
    public IThirdPartAuthStrategy getStrategy(ThirdTypeEnum thirdType) {
        IThirdPartAuthStrategy thirdPartAuthStrategy = thirdPartAuthStrategyContainer.get(thirdType.getImpl());
        if (thirdPartAuthStrategy == null) {
            log.error("{} not found thirdPartAuthStrategy by type: {}", LOG_TITLE, thirdType);
            throw new BizException(RET_INTERNAL_SERVER_ERROR);
        }
        return thirdPartAuthStrategy;
    }

    /**
     * 1. 查询用户是否存在
     * 2. 不存在则创建新用户
     * 3. 判断用户状态，正常则生成对应的session信息
     *
     * @param queryUserBase 查询用户函数
     * @param addUserBase   新增用户函数
     * @return {@link LoginResponse}
     */
    public LoginResponse login(Supplier<UserBaseInfo> queryUserBase,
                               Supplier<UserBaseInfo> addUserBase) {
        UserBaseInfo userBaseMessage = queryUserBase.get();
        if (userBaseMessage == null ) {
            userBaseMessage = addUserBase.get();
            log.debug("add user base!, ret: {}", userBaseMessage);
        }
        return baseService.doLogin(userBaseMessage);
    }

}
