package com.xianglin.appserv.common.util;

import java.util.HashMap;
import java.util.Map;

public class SysConfigUtil {


    private static final Map<String, String> SYS_CONFIG_MAP = new HashMap<String, String>();

    /**
     * @param key
     * @param value
     * @author gengchaogang
     * @dateTime 2015年12月4日 上午11:05:14
     */
    public static void put(String key, String value) {
        SYS_CONFIG_MAP.put(key, value);
    }

    /**
     * @param maps
     * @author gengchaogang
     * @dateTime 2015年12月4日 上午11:05:54
     */
    public static void putAll(Map<String, String> maps) {
        SYS_CONFIG_MAP.putAll(maps);
    }

    public static String getStr(String key) {
        return getStr(key, null);
    }

    public static String getStr(String key, String defaultValue) {
        return SYS_CONFIG_MAP.get(key) == null ? defaultValue : SYS_CONFIG_MAP.get(key);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int value) {
        return SYS_CONFIG_MAP.get(key) == null ? value : Integer.valueOf(SYS_CONFIG_MAP.get(key));
    }

    public static long getLong(String key) {
        return getLong(key, 0);
    }

    public static long getLong(String key, long value) {
        return SYS_CONFIG_MAP.get(key) == null ? value : Long.valueOf(SYS_CONFIG_MAP.get(key));
    }
}
