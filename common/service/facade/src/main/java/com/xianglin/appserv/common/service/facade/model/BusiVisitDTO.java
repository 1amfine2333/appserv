/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

/**
 * 业务访问具体的url，后台动态配置
 * 
 * @author zhangyong 2016年8月23日上午11:32:40
 */
public class BusiVisitDTO extends BaseVo {

	/**  */
	private static final long serialVersionUID = 1906596075804752054L;
	
	private String totalProfitUrl;
	private String totalProfitRank;
	/**
	 *便民服务收益排名url
	 */
	private String lifePayProfitUrl;
	/**
	 * 便民服务排名
	 */
	private String lifePayProfitRank;
	/**
	 * 银行收益排行榜单url
	 */
	private String bankProfitTopUrl;
	/**
	 * 银行收益排行榜排名
	 */
	private String bankProfitTopRank;
	/**
	 * 业绩排名url
	 */
	private String bankPerformanceUrl;
	private String bankPerformanceRank;
	
	/**
	 * 余额排名url
	 */
	private String bankBalanceUrl;
	
	private String bankBalanceRank;
	/**
	 * 增量排名url
	 */
	private String bankIncrBalanceUrl;
	private String bankIncrBalanceRank;
	/**
	 * 银行卡排名url
	 */
	private String bankCardNumUrl;
	
	private String bankCardNumRank;
	
	
	
	
	/***********************赚钱页面业务url******************/
	
	private String bankManagerUrl;
	private String eshopManagerUrl;
	private String loanManagerUrl;
	private String lifeMangerUrl;
	private String mobileChargeUrl;

	/**
	 * 默认url
	 */
	private String defaultUrl;
	public String getTotalProfitUrl() {
		return totalProfitUrl;
	}

	public void setTotalProfitUrl(String totalProfitUrl) {
		this.totalProfitUrl = totalProfitUrl;
	}

	public String getTotalProfitRank() {
		return totalProfitRank;
	}

	public void setTotalProfitRank(String totalProfitRank) {
		this.totalProfitRank = totalProfitRank;
	}

	public String getLifePayProfitUrl() {
		return lifePayProfitUrl;
	}

	public void setLifePayProfitUrl(String lifePayProfitUrl) {
		this.lifePayProfitUrl = lifePayProfitUrl;
	}

	public String getLifePayProfitRank() {
		return lifePayProfitRank;
	}

	public void setLifePayProfitRank(String lifePayProfitRank) {
		this.lifePayProfitRank = lifePayProfitRank;
	}

	public String getBankProfitTopUrl() {
		return bankProfitTopUrl;
	}

	public void setBankProfitTopUrl(String bankProfitTopUrl) {
		this.bankProfitTopUrl = bankProfitTopUrl;
	}

	public String getBankProfitTopRank() {
		return bankProfitTopRank;
	}

	public void setBankProfitTopRank(String bankProfitTopRank) {
		this.bankProfitTopRank = bankProfitTopRank;
	}

	public String getBankPerformanceUrl() {
		return bankPerformanceUrl;
	}

	public void setBankPerformanceUrl(String bankPerformanceUrl) {
		this.bankPerformanceUrl = bankPerformanceUrl;
	}

	public String getBankPerformanceRank() {
		return bankPerformanceRank;
	}

	public void setBankPerformanceRank(String bankPerformanceRank) {
		this.bankPerformanceRank = bankPerformanceRank;
	}

	public String getBankBalanceUrl() {
		return bankBalanceUrl;
	}

	public void setBankBalanceUrl(String bankBalanceUrl) {
		this.bankBalanceUrl = bankBalanceUrl;
	}

	public String getBankIncrBalanceRank() {
		return bankIncrBalanceRank;
	}

	public void setBankIncrBalanceRank(String bankIncrBalanceRank) {
		this.bankIncrBalanceRank = bankIncrBalanceRank;
	}

	public String getBankCardNumUrl() {
		return bankCardNumUrl;
	}

	public void setBankCardNumUrl(String bankCardNumUrl) {
		this.bankCardNumUrl = bankCardNumUrl;
	}

	public String getBankCardNumRank() {
		return bankCardNumRank;
	}

	public void setBankCardNumRank(String bankCardNumRank) {
		this.bankCardNumRank = bankCardNumRank;
	}

	public String getBankBalanceRank() {
		return bankBalanceRank;
	}

	public void setBankBalanceRank(String bankBalanceRank) {
		this.bankBalanceRank = bankBalanceRank;
	}

	public String getBankIncrBalanceUrl() {
		return bankIncrBalanceUrl;
	}

	public void setBankIncrBalanceUrl(String bankIncrBalanceUrl) {
		this.bankIncrBalanceUrl = bankIncrBalanceUrl;
	}

	public String getBankManagerUrl() {
		return bankManagerUrl;
	}

	public void setBankManagerUrl(String bankManagerUrl) {
		this.bankManagerUrl = bankManagerUrl;
	}

	public String getEshopManagerUrl() {
		return eshopManagerUrl;
	}

	public void setEshopManagerUrl(String eshopManagerUrl) {
		this.eshopManagerUrl = eshopManagerUrl;
	}

	public String getLoanManagerUrl() {
		return loanManagerUrl;
	}

	public void setLoanManagerUrl(String loanManagerUrl) {
		this.loanManagerUrl = loanManagerUrl;
	}

	public String getLifeMangerUrl() {
		return lifeMangerUrl;
	}

	public void setLifeMangerUrl(String lifeMangerUrl) {
		this.lifeMangerUrl = lifeMangerUrl;
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}

	public String getMobileChargeUrl() {
		return mobileChargeUrl;
	}

	public void setMobileChargeUrl(String mobileChargeUrl) {
		this.mobileChargeUrl = mobileChargeUrl;
	}
}

