package com.example.jwt.controller;

import com.example.common.protocol.BaseResp;
import com.example.common.response.UploadFileResponse;
import com.example.common.util.BaseRespUtil;
import com.example.jwt.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 文件上传 controller
 *
 * @author zhangyubing
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Resource
    private IUploadFileService uploadFileService;

    /**
     * 上传用户头像
     *
     * @param file 请求参数
     * @return 图片访问url
     * @throws IOException
     */
    @PostMapping("/header/v1")
    public BaseResp<UploadFileResponse> uploadHeader(@RequestParam(value = "file") MultipartFile file) throws IOException {
        return BaseRespUtil.success(uploadFileService.uploadHeader(file));
    }
}
