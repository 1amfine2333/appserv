/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;

/**
 * 群组DTO
 * 
 * @author pengpeng 2016年2月18日下午4:49:52
 */
public class GroupDTO implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -282019769210660074L;

	/** 群主唯一标识 */
	private String ownerUserId;

	/** 群主身份角色唯一标识 */
	private String ownerFigureId;

	/** 当前用户在该群中的身份角色唯一标识 */
	private String figureId;

	/** 对与当前用户的当前身份角色来说，该群组的类型 */
	private String groupType;

	/** 群组唯一标识 */
	private String groupId;

	/** 群组名称 */
	private String groupName;

	/** 群组头像图片url */
	private String avatarUrl;

	/** 群组描述信息 */
	private String description;

	/** 群组状态 */
	private String status;

	/** 创建时间 */
	private long createTime;

	/** 最后更新时间 */
	private long updateTime;

	/**
	 * @return the ownerUserId
	 */
	public String getOwnerUserId() {
		return ownerUserId;
	}

	/**
	 * @param ownerUserId
	 *            the ownerUserId to set
	 */
	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	/**
	 * @return the ownerFigureId
	 */
	public String getOwnerFigureId() {
		return ownerFigureId;
	}

	/**
	 * @param ownerFigureId
	 *            the ownerFigureId to set
	 */
	public void setOwnerFigureId(String ownerFigureId) {
		this.ownerFigureId = ownerFigureId;
	}

	/**
	 * @return the figureId
	 */
	public String getFigureId() {
		return figureId;
	}

	/**
	 * @param figureId
	 *            the figureId to set
	 */
	public void setFigureId(String figureId) {
		this.figureId = figureId;
	}

	/**
	 * @return the groupType
	 */
	public String getGroupType() {
		return groupType;
	}

	/**
	 * @param groupType
	 *            the groupType to set
	 */
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName
	 *            the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the avatarUrl
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 * @param avatarUrl
	 *            the avatarUrl to set
	 */
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	public long getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GroupDTO [ownerUserId=" + ownerUserId + ", ownerFigureId=" + ownerFigureId + ", figureId=" + figureId
				+ ", groupType=" + groupType + ", groupId=" + groupId + ", groupName=" + groupName + ", avatarUrl="
				+ avatarUrl + ", description=" + description + ", status=" + status + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}

}
