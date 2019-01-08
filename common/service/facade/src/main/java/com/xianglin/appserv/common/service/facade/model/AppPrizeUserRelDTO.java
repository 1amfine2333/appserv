package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;

public class AppPrizeUserRelDTO extends PageReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5889100777310306091L;

	private Long relId;

	private String partyid;

	private String nikerName;

	private String loginName;

	private String userType;

	private Long activeid;

	private Long prizeid;

	private String pname;// 奖品名称

	private BigDecimal pmoney;// 奖品金额

	private Date prizetime;

	private String dataStatus;

	private Date createTime;

	private Date updateTime;

	private String tipMsg;

	public Long getRelId() {
		return relId;
	}

	public void setRelId(Long relId) {
		this.relId = relId;
	}

	public String getNikerName() {
		return nikerName;
	}

	public void setNikerName(String nikerName) {
		this.nikerName = nikerName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPartyid() {
		return partyid;
	}

	public void setPartyid(String partyid) {
		this.partyid = partyid == null ? null : partyid.trim();
	}

	public Long getActiveid() {
		return activeid;
	}

	public void setActiveid(Long activeid) {
		this.activeid = activeid;
	}

	public Long getPrizeid() {
		return prizeid;
	}

	public void setPrizeid(Long prizeid) {
		this.prizeid = prizeid;
	}

	public Date getPrizetime() {
		return prizetime;
	}

	public void setPrizetime(Date prizetime) {
		this.prizetime = prizetime;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus == null ? null : dataStatus.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public BigDecimal getPmoney() {
		return pmoney;
	}

	public void setPmoney(BigDecimal pmoney) {
		this.pmoney = pmoney;
	}

	public String getTipMsg() {
		return tipMsg;
	}

	public void setTipMsg(String tipMsg) {
		this.tipMsg = tipMsg;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AppPrizeUserRelDTO [relId=");
		builder.append(relId);
		builder.append(", partyid=");
		builder.append(partyid);
		builder.append(", nikerName=");
		builder.append(nikerName);
		builder.append(", loginName=");
		builder.append(loginName);
		builder.append(", userType=");
		builder.append(userType);
		builder.append(", activeid=");
		builder.append(activeid);
		builder.append(", prizeid=");
		builder.append(prizeid);
		builder.append(", pname=");
		builder.append(pname);
		builder.append(", pmoney=");
		builder.append(pmoney);
		builder.append(", prizetime=");
		builder.append(prizetime);
		builder.append(", dataStatus=");
		builder.append(dataStatus);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", updateTime=");
		builder.append(updateTime);
		builder.append(", tipMsg=");
		builder.append(tipMsg);
		builder.append("]");
		return builder.toString();
	}

}