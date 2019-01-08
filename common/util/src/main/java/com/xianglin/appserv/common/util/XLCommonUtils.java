package com.xianglin.appserv.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/11/2
 * Time: 14:43
 */
public class XLCommonUtils {
    private static final String rule = "*************************";

    public static String maskMobile(String mobile) {
        if ((StringUtils.isBlank(mobile)) || (mobile.length() != 11)) {
            return mobile;
        }
        return StringUtils.substring(mobile, 0, 3) + StringUtils.substring("*************************", 0, 4)
                + StringUtils.substring(mobile, 7);
    }

    public static String maskIDCardNo(String idcardNo) {
        if ((StringUtils.isBlank(idcardNo)) || (idcardNo.length() < 15) || (idcardNo.length() > 18)) {
            return idcardNo;
        }
        return StringUtils.substring(idcardNo, 0, idcardNo.length() - 6)
                + StringUtils.substring("*************************", 0, 6);
    }

    public static String maskBankCardNo(String cardNo) {
        if ((StringUtils.isBlank(cardNo)) || (cardNo.length() < 5) || (cardNo.length() > 25)) {
            return cardNo;
        }
        int length = cardNo.length();
        return "*************************".substring(0, length - 4) + StringUtils.substring(cardNo, length - 4);
    }
}
