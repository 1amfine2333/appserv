/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model.vo;

import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author leaf 2016年8月11日下午5:53:55
 */
public class FlowersVo extends PageReq implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;

	private Long fromNodeManagerId;

    private Long toNodePartyId;

    private Date sendDate;

    private String isDeleted;

    private String creator;

    private String updater;

    private Date createDate;

    private Date updateDate;

    private String comments;

	/**
	 * @return the fromNodeManagerId
	 */
	public Long getFromNodeManagerId() {
		return fromNodeManagerId;
	}

	/**
	 * @param fromNodeManagerId the fromNodeManagerId to set
	 */
	public void setFromNodeManagerId(Long fromNodeManagerId) {
		this.fromNodeManagerId = fromNodeManagerId;
	}

	/**
	 * @return the toNodePartyId
	 */
	public Long getToNodePartyId() {
		return toNodePartyId;
	}

	/**
	 * @param toNodePartyId the toNodePartyId to set
	 */
	public void setToNodePartyId(Long toNodePartyId) {
		this.toNodePartyId = toNodePartyId;
	}

	/**
	 * @return the sendDate
	 */
	public Date getSendDate() {
		return sendDate;
	}

	/**
	 * @param sendDate the sendDate to set
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * @return the isDeleted
	 */
	public String getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the updater
	 */
	public String getUpdater() {
		return updater;
	}

	/**
	 * @param updater the updater to set
	 */
	public void setUpdater(String updater) {
		this.updater = updater;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FlowersDTO [fromNodeManagerId=" + fromNodeManagerId + ", toNodePartyId=" + toNodePartyId
				+ ", sendDate=" + sendDate + ", isDeleted=" + isDeleted + ", creator=" + creator + ", updater="
				+ updater + ", createDate=" + createDate + ", updateDate=" + updateDate + ", comments=" + comments
				+ "]";
	}
	
	
    
}
