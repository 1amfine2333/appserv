package com.xianglin.appserv.common.util;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * emoji 字符转义工具类
 *
 * @author Yungyu
 * @description Created by Yungyu on 2018/4/8 11:44.
 */

public final class EmojiEscapeUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmojiEscapeUtil.class);

    /**
     * emoji 表情匹配模式
     */
    private static final String EMOJI_REGEX_STRING = "(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]\uFE0F?|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?|[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|[\u00A9\u00AE]\uFE0F?|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)";

    /**
     * 转义后替换字符串的模板
     */
    private static final String ESCAPE_MAKER = "${%s}";

    /**
     * 还原表情时的匹配模式
     */
    private static final String AFTER_ESCAPE_PATTERN = "\\$\\{[a-zA-Z0-9/+=]+?}";

    /**
     * 线程安全的模式类
     */
    private static final Pattern EMOJI_PATTERN = Pattern.compile(EMOJI_REGEX_STRING);

    /**
     * 线程安全的模式类
     */
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile(AFTER_ESCAPE_PATTERN);

    /**
     * 缓存字节码动态生成的字段stub类
     */
    //private static final ConcurrentMap<Class<?>, FieldAccess> FIELD_ACCESS_CACHE = Maps.newConcurrentMap();

    /**
     * 不允许继承，不允许实例化
     */
    private EmojiEscapeUtil() {

    }

    /**
     * 表情字符串 转义成 base64
     *
     * @param emojiString
     * @return
     */
    public static String escapeEmoji2Base64String(String emojiString) {

        Matcher matcher = EMOJI_PATTERN.matcher(emojiString);
        while (matcher.find()) {
            String emojiStr = matcher.group();
            String encodeStr = BaseEncoding.base64().encode(emojiStr.getBytes(Charsets.UTF_8));
            emojiString = emojiString.replace(emojiStr, String.format(ESCAPE_MAKER, encodeStr));
        }
        return emojiString;
    }

    /**
     * 将base64 转义成 表情字符串
     *
     * @param base64String
     * @return
     */
    public static String escapeBase642EmojiString(String base64String) {

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(base64String);
        while (matcher.find()) {
            String escapeStr = matcher.group();
            String base64 = escapeStr.substring(2, escapeStr.length() - 1);
            String decodeStr = new String(BaseEncoding.base64().decode(base64), Charsets.UTF_8);
            base64String = base64String.replace(escapeStr, decodeStr);
        }
        return base64String;
    }

    /**
     * 转义对象中的字段
     *
     * @param obj
     * @return
     */
    public static void escapeEmojiString(Object obj) {

        processFields(obj, EmojiEscapeUtil::escapeEmoji2Base64String);
    }

    /**
     * 反转义对象中的字段
     *
     * @param obj
     * @return
     */
    public static void deEscapeEmojiString(Object obj) {

        processFields(obj, EmojiEscapeUtil::escapeBase642EmojiString);

    }

    private static Object processFields(Object obj, Function<String, String> fun) {

        if (obj == null) {
            return obj;
        }
        Class<?> clazz = obj.getClass();
        //只处理自己声明的属性，不处理继承的属性。
        Field[] fields = clazz.getDeclaredFields();
        Lists.newArrayList(fields)
                .stream()
                .filter(input -> input.isAnnotationPresent(EscapeField.class))
                .forEach(input -> {
                    input.setAccessible(true);
                    try {
                        Object fieldVal = input.get(obj);
                        if (fieldVal != null) {
                            if (fieldVal instanceof String) {
                                String escapeEmoji2Base64String = fun.apply((String) fieldVal);
                                input.set(obj, escapeEmoji2Base64String);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        logger.info("===========处理emoji字符错误，字段：[[ {}.{} ]]===========", input.getDeclaringClass().getName(), input.getName(), e);
                        throw new RuntimeException("处理emoji字符错误");
                    }

                });

        return obj;
    }


    /**
     * mapper方法上的转义标记
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface EscapeMehthod {

    }

    /**
     * DO对象字段上的转义标记
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface EscapeField {

    }
}
