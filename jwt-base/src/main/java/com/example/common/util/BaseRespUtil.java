package com.example.common.util;

import com.example.common.protocol.BaseResp;
import com.example.common.protocol.RetCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 统一返回工具类
 *
 * @author zhangyubing
 */
@Slf4j
public class BaseRespUtil {

    /**
     * 返回成功
     *
     * @param data 数据
     * @param <T>  业务参数类型
     * @return 统一返回
     */
    public static <T> BaseResp<T> success(T data) {
        BaseResp<T> resp = new BaseResp<>();
        resp.setRetCode(RetCodeEnum.SUCCESS);
        resp.setData(data);
        log.info("{}, {}", data, dumpRequest());
        return resp;
    }

    /**
     * 返回成功，并打印请求参数
     *
     * @param data 数据
     * @param <T>  业务参数类型
     * @return 统一返回
     */
    public static <K, T> BaseResp<T> success(K req, T data) {
        BaseResp<T> resp = new BaseResp<>();
        resp.setRetCode(RetCodeEnum.SUCCESS);
        resp.setData(data);
        log.info("{}, {}, {}", req, data, dumpRequest());
        return resp;
    }

    /**
     * 返回失败
     *
     * @param retCode 返回码
     * @param <T>     业务参数类型
     * @return 统一返回
     */
    public static <T> BaseResp<T> error(RetCodeEnum retCode) {
        BaseResp<T> resp = new BaseResp<>();
        resp.setRetCode(retCode);
        log.error(">>>>> {}, {}", retCode, dumpRequest());
        return resp;
    }

    /**
     * 返回成功,数据为空
     *
     * @return 统一返回
     */
    public static <T> BaseResp<T> success() {
        BaseResp<T> resp = new BaseResp<>();
        resp.setRetCode(RetCodeEnum.SUCCESS);
        log.info("{}", dumpRequest());
        return resp;
    }

    /**
     * 返回失败
     *
     * @param retCodeEnum 返回码
     * @return 统一返回
     */
    public static <T> BaseResp<T> error(RetCodeEnum retCodeEnum, String errorMsg) {
        BaseResp<T> resp = new BaseResp<>();
        resp.setErrorResp(retCodeEnum.getCode(), errorMsg);
        log.error(">>>>> {}, {}, {}", retCodeEnum, errorMsg, dumpRequest());
        return resp;
    }

    /**
     * 打印请求
     *
     * @return 请求参数字符串
     */
    private static String dumpRequest() {
        // 获取http请求参数
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[method:")
                .append(request.getMethod())
                .append(", uri:")
                .append(request.getRequestURI())
                .append(", HEADERS:(");

        Enumeration<String> headerNames = request.getHeaderNames();
        StringJoiner stringJoiner = new StringJoiner(",");
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            String element = key + "=" + value;
            stringJoiner.add(element);
        }
        stringBuilder.append(stringJoiner);
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

}
