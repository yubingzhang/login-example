package com.example.jwt.service.impl;


import com.alibaba.druid.util.StringUtils;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.example.common.response.UploadFileResponse;
import com.example.common.starter.holder.ContextHolder;
import com.example.jwt.constants.CommonConstants;
import com.example.jwt.service.IUploadFileService;
import com.example.jwt.service.helper.FileHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


/**
 * aliyun oss 上传文件
 *
 * @author zhangyubing
 */
@Slf4j
@Service
public class AliyunUploadFileServiceImpl implements IUploadFileService {


    @Override
    public UploadFileResponse uploadHeader(MultipartFile file) throws IOException {
        String fileFormat = FileHelper.checkImage(file);
        if (StringUtils.isEmpty(fileFormat)) {
            //throw new BizException(RET_UPLOAD_IMAGE_FORMAT_UN_SUPPORT);
        }
        String path = FileHelper.createPath(ContextHolder.getUserId(), fileFormat,
                CommonConstants.ImagePrefix.IMAGE_PREFIX_USER_HEADER, "aliyunOssProperties.getUserHeaderParentKey()");
        // 上传
        String fileUrl = doUpload(path,
                "aliyunOssProperties.getUserHeaderBucket()",
                file.getInputStream());
        return UploadFileResponse.builder().fileUrl(fileUrl).build();
    }

    /**
     * 上传文件到oss (TODO: 需要替换参数和返回结果的url拼接)
     *
     * @param objectName  文件目录
     * @param bucket      oss bucket
     * @param inputStream 文件流
     * @return 文件url
     */
    private String doUpload(String objectName, String bucket, InputStream inputStream) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build("aliyunOssProperties.getEndpoint()",
                "aliyunOssProperties.getAccessKeyId()",
                "aliyunOssProperties.getAccessKeySecret()");
        try {
            // 上传文件。
            ossClient.putObject(bucket, objectName, inputStream);
        } catch (OSSException oe) {
            log.error(">>>>> Aliyun OSS Caught an OSSException ", oe);
            //throw new BizException();
        } catch (ClientException ce) {
            log.error(">>>>> Aliyun OSS Caught an ClientException ", ce);
            //throw new BizException();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return "url/" + objectName;
    }
}
