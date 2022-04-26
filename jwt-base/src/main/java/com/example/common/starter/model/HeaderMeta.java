package com.example.common.starter.model;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.common.exception.BizException;
import com.example.common.starter.constants.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

import static com.example.common.protocol.RetCodeEnum.RET_HEADER_INCORRECT_FORMAT;

/**
 * header里的源信息
 *
 * @author zhangyubing
 */
@Slf4j
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeaderMeta {

    /**
     * 地区码,index=1
     */
    private String locale;

    /**
     * 客户端渠道,index=2
     */
    private String clientChannel;

    /**
     * 客户端版本,index=3
     */
    private String clientVersion;

    /**
     * 构建编号
     */
    private String buildNumber;

    /**
     * 设备ID,index=4
     */
    private String deviceId;

    /**
     * 系统类型,index=5
     */
    private String systemType;

    /**
     * 系统版本,index=6
     */
    private String systemVersion;

    /**
     * 客户端时间戳,index=7
     */
    private String clientTimestamp;

    /**
     * 从请求里解析出sud源信息
     *
     * @param request 请求
     * @return sud源信息
     */
    public static HeaderMeta parseFromRequest(HttpServletRequest request) {
        HeaderMeta headerSudMeta = new HeaderMeta();
        // 约定: 以逗号分隔,第一个字段是locale,
        String sudMeta = request.getHeader(CommonConstant.HEADER_META);
        if (StringUtils.isBlank(sudMeta)) {
            log.warn(">>>>> not exist request header: Sud-Meta");
            // 为了兼容老版本未传Sud-Meta字段，默认值
            headerSudMeta.setLocale("zh-CN");
            return headerSudMeta;
        }
        String[] strings = sudMeta.split(",");
        for (int i = 0; i < strings.length; i++) {
            try {
                // 利用反射设置属性
                Field[] declaredFields = HeaderMeta.class.getDeclaredFields();
                if (declaredFields.length > (i + 1)) {
                    // 顺序一定要一致
                    declaredFields[i + 1].set(headerSudMeta, strings[i]);
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
                throw new BizException(RET_HEADER_INCORRECT_FORMAT);
            }
        }
        return headerSudMeta;
    }
}
