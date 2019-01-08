package com.xianglin.appserv.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词过滤工具
 *
 * @author wanglei
 */
public class SensitiveWordUtil {

    private static Map<Character, Map> sensitiveMap = new HashMap<>();

    private static final Character ENDTAG = '#';

    private static final Character DEFAULTcHAR = '*';

    private static long currentTime = System.currentTimeMillis();

    /**
     * 关键字初始化
     */
    private static void initSensitiveMap() {
        long sysTime = System.currentTimeMillis();
        //5分钟缓存
        if (sysTime - currentTime > 200_000 || sensitiveMap.isEmpty()) {
            currentTime = sysTime;
            String words = SysConfigUtil.getStr("SENSITIVE_WORDS");
            if (StringUtils.isNotEmpty(words)) {
                Map<Character, Map> map = new HashMap<>();
                Arrays.stream(words.split("，")).forEach(word -> {
                    char[] chars = word.toCharArray();
                    int lensth = chars.length;
                    int i = 0;
                    Map paraMap = map;
                    while (i < lensth + 1) {
                        if (i == lensth) {
                            paraMap.put(ENDTAG, ENDTAG);
                            break;
                        }
                        char w = chars[i];
                        if (paraMap.containsKey(w)) {
                            paraMap = (Map) paraMap.get(w);
                        } else {
                            Map subMap = new HashMap();
                            paraMap.put(w, subMap);
                            paraMap = subMap;
                        }
                        i++;
                    }
                });
                sensitiveMap = map;
            }
        }
    }


    /**
     * 敏感词过滤
     *
     * @param input
     * @return
     */
    public static String filterSensitiveWord(String input) {
        initSensitiveMap();
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(input) || sensitiveMap.isEmpty()) {
            return input;
        } else {
            char[] words = input.toCharArray();
            int i = 0;
            int length = words.length;
            while (i < length) {
                char w = words[i];
                Map paraMap = sensitiveMap;
                int j = i;
                boolean isSensitive = false;
                while (paraMap.containsKey(w)) {
                    paraMap = (Map) paraMap.get(w);
                    if (paraMap.containsKey(ENDTAG)) {
                        isSensitive = true;
                        j ++;
                        break;
                    }
                    if (++j == length) {
                        break;
                    }
                    w = words[j];
                }
                if (j > i) {
                    for (; i < j; i++) {
                        if(isSensitive){
                            sb.append(DEFAULTcHAR);
                        }else {
                            sb.append(words[i]);
                        }
                    }
                } else {
                    sb.append(w);
                    i++;
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(filterSensitiveWord("银行卡复假钱发缥发货对对对代办发票公司师傅的说法三代理开票公司台湾独立交援交银行卡复"));
    }

}
