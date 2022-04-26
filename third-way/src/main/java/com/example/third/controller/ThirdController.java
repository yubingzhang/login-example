package com.example.third.controller;

import com.example.third.protocol.BaseResp;
import com.example.third.service.ThirdPartAuthService;
import com.example.third.util.BaseRespUtil;
import com.example.third.vo.request.ThirdLoginRequest;
import com.example.third.vo.response.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 第三方相关服务
 *
 * @author zhangyubing
 */
@RestController
@RequestMapping("/third")
@Slf4j
public class ThirdController {

    @Resource
    private ThirdPartAuthService thirdPartAuthService;

    /**
     * 第三方登录
     *
     * @param req 第三方类型，授权码
     * @return 登录结果
     */
    @PostMapping("/login/v1")
    public BaseResp<LoginResponse> thirdLogin(@RequestBody ThirdLoginRequest req) {
        return BaseRespUtil.success(thirdPartAuthService.login(req));
    }
}
