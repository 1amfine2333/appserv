package com.xianglin.appserv.common.service.facade.model.vo;/**
 * Created by wanglei on 2017/4/11.
 */

import java.math.BigDecimal;

/**站点银行业绩明细
 * @author wanglei
 * @create 2017-04-11 14:35
 **/
public class NodeAchieveDetailVo extends BaseVo{

    private BigDecimal value;

    private Long dateTime;

    public NodeAchieveDetailVo () {
    }

    public NodeAchieveDetailVo (Long dateTime, BigDecimal value) {
        this.dateTime = dateTime;
        this.value = value;
    }
    public BigDecimal getValue () {
        return value;
    }

    public void setValue (BigDecimal value) {
        this.value = value;
    }

    public Long getDateTime () {
        return dateTime;
    }

    public void setDateTime (Long dateTime) {
        this.dateTime = dateTime;
    }
}
