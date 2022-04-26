package com.example.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 上传文件结果
 *
 * @author zhangyubing
 */
@Data
@Builder
@AllArgsConstructor
public class UploadFileResponse {
    /**
     * 文件访问路径
     */
    private String fileUrl;
}
