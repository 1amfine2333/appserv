/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import java.math.BigDecimal;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

/**
 * 
 * 收益明细
 * @author zhangyong 2016年8月15日下午6:53:29
 */
public class ProfitDetailDTO  extends BaseVo{
	/**  */
	private static final long serialVersionUID = 8054977680220021462L;
	private String year;
	private String month;
	/**
	 * 银行收益
	 */
	private BigDecimal bankProfit;
	/**
	 * 电商收益
	 */
	private BigDecimal eshopProfit;
	/**
	 * 贷款收益
	 */
	private BigDecimal loanProfit;
	/**
	 * 手机冲值收益
	 */
	private BigDecimal mobileEchargeProfit;
	/**
	 * 生活缴费收益
	 */
	private BigDecimal liveEchargeProfit;
	
	private BigDecimal totalProfit;
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public BigDecimal getBankProfit() {
		return bankProfit;
	}
	public void setBankProfit(BigDecimal bankProfit) {
		this.bankProfit = bankProfit;
	}
	public BigDecimal getEshopProfit() {
		return eshopProfit;
	}
	public void setEshopProfit(BigDecimal eshopProfit) {
		this.eshopProfit = eshopProfit;
	}
	public BigDecimal getLoanProfit() {
		return loanProfit;
	}
	public void setLoanProfit(BigDecimal loanProfit) {
		this.loanProfit = loanProfit;
	}
	public BigDecimal getMobileEchargeProfit() {
		return mobileEchargeProfit;
	}
	public void setMobileEchargeProfit(BigDecimal mobileEchargeProfit) {
		this.mobileEchargeProfit = mobileEchargeProfit;
	}
	public BigDecimal getLiveEchargeProfit() {
		return liveEchargeProfit;
	}
	public void setLiveEchargeProfit(BigDecimal liveEchargeProfit) {
		this.liveEchargeProfit = liveEchargeProfit;
	}
	public BigDecimal getTotalProfit() {
		
		return totalProfit;
	}
	public BigDecimal getTotal(){
		BigDecimal total = BigDecimal.ZERO;
		if(eshopProfit != null ){
			total =	total.add(eshopProfit);
		}
		if(loanProfit != null){
			total = total.add(loanProfit);
		}
		if(bankProfit != null){
			total = total.add(bankProfit);
		}
		if(mobileEchargeProfit != null){
			total = total.add(mobileEchargeProfit);
		}
		if(liveEchargeProfit != null){
			total = total.add(liveEchargeProfit);
		}
		
		return total;
	}
	public void setTotalProfit(BigDecimal totalProfit) {
		this.totalProfit = totalProfit;
	}
	
}
