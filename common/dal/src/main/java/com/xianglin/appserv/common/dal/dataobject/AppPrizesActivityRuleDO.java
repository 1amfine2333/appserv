package com.xianglin.appserv.common.dal.dataobject;

import java.math.BigDecimal;

public class AppPrizesActivityRuleDO {
	private BigDecimal ruleID;
	private BigDecimal activeId;
	private BigDecimal prizeId;
	private String ptype;
	private String pname;
	private String ruleCode;
	private String ruleValue;

	public BigDecimal getRuleID() {
		return ruleID;
	}

	public void setRuleID(BigDecimal ruleID) {
		this.ruleID = ruleID;
	}

	public BigDecimal getActiveId() {
		return activeId;
	}

	public void setActiveId(BigDecimal activeId) {
		this.activeId = activeId;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public BigDecimal getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(BigDecimal prizeId) {
		this.prizeId = prizeId;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public String getRuleValue() {
		return ruleValue;
	}

	public void setRuleValue(String ruleValue) {
		this.ruleValue = ruleValue;
	}

}
