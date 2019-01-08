package com.xianglin.appserv.common.service.facade.model.vo;

import java.util.Date;

/**

 import java.util.Date;
 * Created by wanglei on 2017/3/14.
 */

/**
 * @author
 * @create 2017-03-14 14:02
 **/
public class ActivityShareDailyVo extends BaseVo {
	private Long id;

	private Long partyId;

	private String daily;

	private String taskStatus;

	private String shareStatus;

	private String tipAlertStatus;

	private String progessAlertStatus;

	private String isDeleted;

	private Date createTime;

	private Date updateTime;

	private String comments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPartyId() {
		return partyId;
	}

	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus == null ? null : taskStatus.trim();
	}

	public String getShareStatus() {
		return shareStatus;
	}

	public void setShareStatus(String shareStatus) {
		this.shareStatus = shareStatus == null ? null : shareStatus.trim();
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted == null ? null : isDeleted.trim();
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments == null ? null : comments.trim();
	}

	public String getTipAlertStatus() {
		return tipAlertStatus;
	}

	public void setTipAlertStatus(String tipAlertStatus) {
		this.tipAlertStatus = tipAlertStatus;
	}

	public String getProgessAlertStatus() {
		return progessAlertStatus;
	}

	public void setProgessAlertStatus(String progessAlertStatus) {
		this.progessAlertStatus = progessAlertStatus;
	}

	public String getDaily() {
		return daily;
	}

	public void setDaily(String daily) {
		this.daily = daily;
	}
}
