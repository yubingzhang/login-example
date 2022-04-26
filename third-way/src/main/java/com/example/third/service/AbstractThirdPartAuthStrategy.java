package com.example.third.service;

import com.example.third.vo.request.ThirdLoginRequest;
import com.example.third.vo.response.LoginResponse;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * 第三方授权 抽象实现
 *
 * @author zhnagyubing
 */
@Slf4j
public abstract class AbstractThirdPartAuthStrategy implements IThirdPartAuthStrategy {

    @Resource
    private ThirdPartAuthService thirdPartAuthService;


    /**
     * 授权登录逻辑
     * 1. 获取用户对应的第三方标识
     * 2. 查询用户是否存在
     * 2.1 用户不存在: 获取用户的第三方信息，创建新用户并返回用户信息
     * 2.2 用户存在: 直接返回用户信息
     *
     * @param req 第三方授权请求信息
     * @return {@link LoginResponse}
     */
    @Override
    public LoginResponse login(ThirdLoginRequest req) {
        String openid = getOpenid(req);
        return thirdPartAuthService.login(
                () -> {
                    /*RpcUserBindProto.RpcQueryBaseUserByOpenIdResp rpcQueryBaseUserByOpenIdResp = userBindService.queryBaseUserByOpenid(RpcUserBindProto.RpcQueryBaseUserByOpenIdReq.newBuilder()
                            .setBindTypeValue(req.getThirdType())
                            .setOpenId(openid)
                            .build());
                    log.debug("queryBaseUserByOpenid! ret: {}", PbDumpHelper.printPb(rpcQueryBaseUserByOpenIdResp, true));
                    return rpcQueryBaseUserByOpenIdResp.getUser();*/
                    return null;
                },
                () -> {
                    /*RpcBaseProto.RpcThirdUserInfoMessage thirdInfo = getThirdInfo(req);
                    RpcUserBindProto.RpcAddUserByThirdResp rpcAddUserByThirdResp = userBindService.addUserByThird(RpcUserBindProto.RpcAddUserByThirdReq.newBuilder()
                            .setThirdUserInfo(thirdInfo)
                            .build());
                    log.debug("addUserByThird! ret: {}", PbDumpHelper.printPb(rpcAddUserByThirdResp, true));
                    return rpcAddUserByThirdResp.getUser();*/
                    return null;
                }
        );
    }

}
