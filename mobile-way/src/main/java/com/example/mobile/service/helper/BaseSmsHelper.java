package com.example.mobile.service.helper;

import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信基础服务
 *
 * @author moxiaofei
 */
@Slf4j
@Component
public class BaseSmsHelper {

    /**
     * 占位符格式
     */
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(\\$\\{.*?})");

    /**
     * 验证码字符
     */
    private static final char[] CODE_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 随机类
     */
    private static final Random random = new Random();

    /**
     * 生成验证码
     *
     * @param figures 验证码位数
     * @return 验证码
     */
    public static String generate(int figures) {
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < figures; i++) {
            codeBuilder.append(CODE_CHARS[random.nextInt(CODE_CHARS.length)]);
        }
        return codeBuilder.toString();
    }

    /**
     * 查询短信内容参数名称
     *
     * @param content 短信内容
     * @return 参数名称列表
     */
    public static List<String> queryContextParamNames(String content) {
        List<String> paramNames = new ArrayList<>();
        if (StringUtils.isEmpty(content)) {
            return paramNames;
        }
        // 匹配参数
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(content);
        while (matcher.find()) {
            String str = matcher.group();
            paramNames.add(str.substring(2, str.length() - 1));
        }
        return paramNames;
    }
}
