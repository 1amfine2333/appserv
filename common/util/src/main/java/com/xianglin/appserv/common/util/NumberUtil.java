/**
 *
 */
package com.xianglin.appserv.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 
 */
public class NumberUtil {

    /**
     * 金额格式化，取两位小数，小数后直接去除
     *
     * @param amount
     * @return
     */
    public static String amountFormat(String amount) {
        BigDecimal amt = null;
        DecimalFormat fm = new DecimalFormat("###0.00");
        if (amount == null) {
            amt = BigDecimal.ZERO;
        } else {
            amt = new BigDecimal(amount);
        }
        fm.setRoundingMode(RoundingMode.DOWN);
        return fm.format(amt);
    }

    /**
     * 金额格式化，取两位小数，小数后直接去除
     *
     * @param amount
     * @return
     */
    public static String amountFormat(BigDecimal amount) {
        DecimalFormat fm = new DecimalFormat("###0.00");
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
        fm.setRoundingMode(RoundingMode.DOWN);
        return fm.format(amount);
    }

    public static BigDecimal formatBigDecimal(BigDecimal value, int scale, RoundingMode model) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.setScale(scale, model);
    }

    /**
     * 截取操作，保留几位小数
     *
     * @param value
     * @param scale
     * @return
     */
    public static BigDecimal truncateBigDecimal(BigDecimal value, int scale) {
        return formatBigDecimal(value, scale, RoundingMode.DOWN);
    }

    /**
     * 如果小数位>=0.5 则进位。
     *
     * @param value
     * @param scale
     * @return
     */
    public static BigDecimal formatBigDecimal(BigDecimal value, int scale) {
        return formatBigDecimal(value, scale, RoundingMode.HALF_UP);
    }

    /**
     * 如果进位后为偶数则向上进位，如果为奇数向下
     * *<p>Example:
     * <table border>
     * <tr valign=top><th>Input Number</th>
     * <th>Input rounded to one digit<br> with {@code HALF_EVEN} rounding
     * <tr align=right><td>5.5</td>  <td>6</td>
     * <tr align=right><td>2.5</td>  <td>2</td>
     * <tr align=right><td>1.6</td>  <td>2</td>
     * <tr align=right><td>1.1</td>  <td>1</td>
     * <tr align=right><td>1.0</td>  <td>1</td>
     * <tr align=right><td>-1.0</td> <td>-1</td>
     * <tr align=right><td>-1.1</td> <td>-1</td>
     * <tr align=right><td>-1.6</td> <td>-2</td>
     * <tr align=right><td>-2.5</td> <td>-2</td>
     * <tr align=right><td>-5.5</td> <td>-6</td>
     * </table>
     *
     * @param value
     * @param scale
     * @return
     */
    public static BigDecimal formatBigDecimalEven(BigDecimal value, int scale) {
        return formatBigDecimal(value, scale, RoundingMode.HALF_EVEN);
    }

    /**
     * 如果小数位>=0.5 则进位。
     *
     * @param value
     * @param scale
     * @return
     */
    public static String formatToString(BigDecimal value, int scale) {
        if (value == null) return null;
        return formatBigDecimal(value, scale, RoundingMode.HALF_UP).toPlainString();
    }

    public static String toString(BigDecimal value) {
        return formatToString(value, 2);
    }

    public static void main(String[] args) {
        BigDecimal b = new BigDecimal("11.97631");
        System.out.println(amountFormat(b));
        System.out.println(formatBigDecimalEven(b, 2));
        System.out.println(truncateBigDecimal(b, 2));
        System.out.println(formatBigDecimal(b, 2));
        System.out.println(formatToString(b, 0));
        System.out.println(formatBigDecimal(b, 2, RoundingMode.CEILING));
    }

}
