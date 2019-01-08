/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import java.util.Date;

/**
 * 
 * 用户收到的公告或者通知消息
 * @author zhangyong 2016年8月11日下午5:35:23
 */
public class MessageDTO {

	private Long id;
	private String msgTitle;
	private String msgType;
	private String titleImg;
	private String message;
	private String status;
	
	private String isDeleted;
	
	private String creator;
	private String updater;
	private Date createTime;
	private Date updateTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMsgTitle() {
		return msgTitle;
	}
	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getTitleImg() {
		return titleImg;
	}
	public void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
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
		return "MessageDTO [id=" + id + ", msgTitle=" + msgTitle + ", msgType=" + msgType + ", titleImg=" + titleImg
				+ ", message=" + message + ", status=" + status + ", isDeleted=" + isDeleted + ", creator=" + creator
				+ ", updater=" + updater + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	
	
	
}
