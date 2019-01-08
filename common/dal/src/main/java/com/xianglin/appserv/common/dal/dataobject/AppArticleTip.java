package com.xianglin.appserv.common.dal.dataobject;

import java.util.Date;

public class AppArticleTip {
	private Long id;

	private Long partyId;

	private String tipType;

	private Long articleId;

	private String content;

	private Long toPartyId;

	private String tipStatus;

	private String dealStatus;

	private String isDeleted;

	private Date createTime;

	private Date updateTime;

	private String comments;

	private String readStatus;

	private String operator;

	private User user;

	private User toUser;

	private String articleType;

	public String getArticleType() {
		return articleType;
	}

	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}

	public Long getPartyId() {
		return partyId;
	}

	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}

	public String getTipType() {
		return tipType;
	}

	public void setTipType(String tipType) {
		this.tipType = tipType == null ? null : tipType.trim();
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public Long getToPartyId() {
		return toPartyId;
	}

	public void setToPartyId(Long toPartyId) {
		this.toPartyId = toPartyId;
	}

	public String getTipStatus() {
		return tipStatus;
	}

	public void setTipStatus(String tipStatus) {
		this.tipStatus = tipStatus == null ? null : tipStatus.trim();
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus == null ? null : dealStatus.trim();
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return "AppArticleTip [id=" + id + ", partyId=" + partyId + ", tipType=" + tipType + ", articleId=" + articleId
				+ ", content=" + content + ", toPartyId=" + toPartyId + ", tipStatus=" + tipStatus + ", dealStatus="
				+ dealStatus + ", isDeleted=" + isDeleted + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", comments=" + comments + ", readStatus=" + readStatus + ", user=" + user + ", toUser=" + toUser + "operator=" + operator
				+ "]";
	}

}
