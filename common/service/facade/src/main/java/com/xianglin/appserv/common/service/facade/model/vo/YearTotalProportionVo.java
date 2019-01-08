package com.xianglin.appserv.common.service.facade.model.vo;

/**
 * Created by wanglei on 2017/7/4.
 */
public class YearTotalProportionVo extends BaseVo{
	
	private long partyId;
	
	private String month;
	
	private String inSum;
	
	private String outSum;
	
	private String monthSurplus;
	
	private String totalSurplus;
	
	public long getPartyId() {
		return partyId;
	}
	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
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
	public String getMonthSurplus() {
		return monthSurplus;
	}
	public void setMonthSurplus(String monthSurplus) {
		this.monthSurplus = monthSurplus;
	}
	public String getTotalSurplus() {
		return totalSurplus;
	}
	public void setTotalSurplus(String totalSurplus) {
		this.totalSurplus = totalSurplus;
	}
	
	
}
