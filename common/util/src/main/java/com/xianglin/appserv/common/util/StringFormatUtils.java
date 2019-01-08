package com.xianglin.appserv.common.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串格式化工具
 */
public class StringFormatUtils {

    private static Pattern pattern = Pattern.compile("((?<=\\{)([a-zA-Z_]{1,})(?=\\}))");

    /**
     * 字符串格式
     * 支持类型 "hao are you #{name},here is you #{value}",匹配格式map ("name":"test","value":"book")
     *
     * @param source
     * @param paras
     * @return
     */
    private static String format(String source, Map<String, String> paras) {
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            String key = matcher.group();
            source = source.replaceAll("#\\{" + key + "\\}", paras.get(key) + "");
        }
        return source;
    }

}
