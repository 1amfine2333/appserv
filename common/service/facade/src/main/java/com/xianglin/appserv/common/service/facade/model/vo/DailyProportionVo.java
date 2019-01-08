package com.xianglin.appserv.common.service.facade.model.vo;

import java.util.ArrayList;
import java.util.List;

/** 日度明细页面每日占比
 * Created by wanglei on 2017/7/4.
 */
public class DailyProportionVo extends BaseVo{
	
	private long partyId;
	private String day;//日期
	private String amount;//支出总额
	private String proportion;//所占比例总额
	private String percentage;//所占比例总额的比分比
	private String budget;//预算
    private List<DailyDetailTotalVo> dailyDetailTotalVo = new ArrayList<DailyDetailTotalVo>();
	
	public long getPartyId() {
		return partyId;
	}
	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getProportion() {
		return proportion;
	}
	public void setProportion(String proportion) {
		this.proportion = proportion;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}

    public List<DailyDetailTotalVo> getDailyDetailTotalVo() {
        return dailyDetailTotalVo;
    }

    public void setDailyDetailTotalVo(List<DailyDetailTotalVo> dailyDetailTotalVo) {
        this.dailyDetailTotalVo = dailyDetailTotalVo;
    }
}
