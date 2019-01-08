package com.xianglin.appserv.common.service.facade.model;

import java.util.Date;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

public class AppActiveShareDTO extends BaseVo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5951326272656171535L;

	private Long shareId;

	private String partyid;

	private Long activeid;

	private Date sharetime;

	private String dataStatus;

	private Date createTime;

	private Date updateTime;

	public Long getShareId() {
		return shareId;
	}

	public void setShareId(Long shareId) {
		this.shareId = shareId;
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

	public Date getSharetime() {
		return sharetime;
	}

	public void setSharetime(Date sharetime) {
		this.sharetime = sharetime;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AppActiveShareDTO [shareId=");
		builder.append(shareId);
		builder.append(", partyid=");
		builder.append(partyid);
		builder.append(", activeid=");
		builder.append(activeid);
		builder.append(", sharetime=");
		builder.append(sharetime);
		builder.append(", dataStatus=");
		builder.append(dataStatus);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", updateTime=");
		builder.append(updateTime);
		builder.append("]");
		return builder.toString();
	}

}