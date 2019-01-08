package com.xianglin.appserv.common.service.facade.model.enums;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.xianglin.appserv.common.service.facade.base.DefaultKeyValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/4/17 11:05.
 */
public enum CurrencyNameEnum {

    CNY("人民币", 0),

    USD("美元", 1),

    JPY("日元", 2),

    EUR("欧元", 3),

    GBP("英镑", 4),

    KRW("韩元", 5),

    HKD("港元", 6),

    AUD("澳元", 7),

    CAD("加元", 8);

    public static final String PREFFIX = "CURRENCY_RATE_";

    private String name;

    private int code;

    CurrencyNameEnum() {

    }

    CurrencyNameEnum(String name, int code) {

        this.name = name;
        this.code = code;
    }

    /**
     * 根据两个枚举返回参数key
     *
     * @param from
     * @param to
     * @return
     */
    public static String ofEnum(CurrencyNameEnum from, CurrencyNameEnum to) {

        return Joiner.on("").join(Arrays.asList(PREFFIX, from.name(), to.name()));
    }

    /**
     * 根据汇率查询的key，拿到两个枚举
     *
     * @param key
     * @return
     */
    public static Map.Entry<CurrencyNameEnum, CurrencyNameEnum> ofKey(String key) {

        checkArgument(!Strings.isNullOrEmpty(key) && key.startsWith(PREFFIX), "参数错误:查询汇率的key不合法");
        String keys = key.substring(PREFFIX.length());
        List<String> keyList = Splitter.fixedLength(3).splitToList(keys);
        checkState(keyList.size() == 2, "参数错误：汇率的key中只能包含两个货币枚举");
        String formKey = keyList.get(0);
        String toKey = keyList.get(1);
        return new DefaultKeyValue<>(CurrencyNameEnum.valueOf(formKey), CurrencyNameEnum.valueOf(toKey));
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public int getCode() {

        return code;
    }

    public void setCode(int code) {

        this.code = code;
    }


}
