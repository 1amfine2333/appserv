package com.xianglin.appserv.common.service.facade.model.vo;

/**
 * Created by wanglei on 2017/7/4.
 */
public class BudgetTotalVo extends BaseVo{
	
	private long partyId;//用户ID
	private String startDay;//开始时间
	private String endDay;//结束时间
	private String budgetSurplus;//预算剩余
    private String isBudget;//是否有预算 Y 有 N没有
	private String inSum;//收入总和
	private String outSum;//支出总和
	
	
	public long getPartyId() {
		return partyId;
	}
	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}
	public String getStartDay() {
		return startDay;
	}
	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	public String getBudgetSurplus() {
		return budgetSurplus;
	}
	public void setBudgetSurplus(String budgetSurplus) {
		this.budgetSurplus = budgetSurplus;
	}
	public String getInSum() {
		return inSum;
	}
	public void setInSum(String inSum) {
		this.inSum = inSum;
	}
	public String getOutSum() {
		return outSum;
	}
	public void setOutSum(String outSum) {
		this.outSum = outSum;
	}

    public String getIsBudget() {
        return isBudget;
    }

    public void setIsBudget(String isBudget) {
        this.isBudget = isBudget;
    }
}
