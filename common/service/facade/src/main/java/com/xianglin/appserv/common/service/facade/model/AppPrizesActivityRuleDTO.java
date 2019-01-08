package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class AppPrizesActivityRuleDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 418474003422526665L;
	private BigDecimal activeId;// 活动ID
	private BigDecimal prizeId;// 奖品id
	private String pname;// 奖品名称
	private String ptype;// 奖品大类
	private BigDecimal ruleID;// 规则ID
	private BigDecimal pamount;// 奖品（剩余）数量
	private BigDecimal pweight;// 奖品权重
	private BigDecimal pmoney;// 奖品金额

	public BigDecimal getActiveId() {
		return activeId;
	}

	public void setActiveId(BigDecimal activeId) {
		this.activeId = activeId;
	}

	public BigDecimal getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(BigDecimal prizeId) {
		this.prizeId = prizeId;
	}

	public BigDecimal getRuleID() {
		return ruleID;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public void setRuleID(BigDecimal ruleID) {
		this.ruleID = ruleID;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public BigDecimal getPamount() {
		return pamount;
	}

	public void setPamount(BigDecimal pamount) {
		this.pamount = pamount;
	}

	public BigDecimal getPweight() {
		return pweight;
	}

	public void setPweight(BigDecimal pweight) {
		this.pweight = pweight;
	}

	public BigDecimal getPmoney() {
		return pmoney;
	}

	public void setPmoney(BigDecimal pmoney) {
		this.pmoney = pmoney;
	}

}
