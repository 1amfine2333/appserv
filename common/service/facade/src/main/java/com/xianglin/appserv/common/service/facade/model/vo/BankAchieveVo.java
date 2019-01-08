package com.xianglin.appserv.common.service.facade.model.vo;/**
 * Created by wanglei on 2017/4/7.
 */

import java.math.BigDecimal;
import java.util.Date;

/**站长银行业绩（和银行同步）
 * @author
 * @create 2017-04-07 14:50
 **/
public class BankAchieveVo extends BaseVo{

    private Long id;

    /**
     * 银行卡数
     */
    private Integer cardCount;

    /**
     * 存款总数
     */
    private String balance;
    /**
     * 月日均
     */
    private String monthAverage;
    /**
     * 上个月月日均
     */
    private String lastMonthAverage;
    /**
     * 今年年日均
     */
    private String yearAverage;
    /**
     * 去年年日均
     */
    private String lastYearAverage;
    /**
     * 导入日期
     */
    private String importDate;

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public Integer getCardCount () {
        return cardCount;
    }

    public void setCardCount (Integer cardCount) {
        this.cardCount = cardCount;
    }

    public String getBalance () {
        return balance;
    }

    public void setBalance (String balance) {
        this.balance = balance;
    }

    public String getMonthAverage () {
        return monthAverage;
    }

    public void setMonthAverage (String monthAverage) {
        this.monthAverage = monthAverage;
    }

    public String getLastMonthAverage () {
        return lastMonthAverage;
    }

    public void setLastMonthAverage (String lastMonthAverage) {
        this.lastMonthAverage = lastMonthAverage;
    }

    public String getYearAverage () {
        return yearAverage;
    }

    public void setYearAverage (String yearAverage) {
        this.yearAverage = yearAverage;
    }

    public String getLastYearAverage () {
        return lastYearAverage;
    }

    public void setLastYearAverage (String lastYearAverage) {
        this.lastYearAverage = lastYearAverage;
    }

    public String getImportDate () {
        return importDate;
    }

    public void setImportDate (String importDate) {
        this.importDate = importDate;
    }
}
