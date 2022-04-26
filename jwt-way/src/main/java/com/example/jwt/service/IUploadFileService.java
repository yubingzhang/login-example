package com.example.jwt.service;

import com.example.common.response.UploadFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传接口
 *
 * @author zhangyubing
 */
public interface IUploadFileService {

    /**
     * 上传用户头像文件
     *
     * @param file 文件流
     * @return 文件路径
     * @throws IOException
     */
    UploadFileResponse uploadHeader(MultipartFile file) throws IOException;

}
