/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;

/**
 * 联系人关系请求
 * 
 * @author pengpeng 2016年2月18日下午9:17:44
 */
public class ContactsRelationRequest implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 7213793903403827240L;

	/** 发起操作的用户的唯一标识 */
	private String userId;

	/** 发起操作的用户的身份角色唯一标识 */
	private String figureId;

	/** 被操作的用户的唯一标识 */
	private String contactsUserId;

	/** 被操作的用户的身份角色唯一标识 */
	private String contactsFigureId;

	/** 关系建立方式 */
	private String relationEstablishType;

	/**
	 * 联系人类型，分组： HIGH--高频联系人; NORMAL--普通联系人; LOW--低频联系人; UMKNOWN--陌生人;
	 * BLACK--黑名单.
	 */
	private String contactsType;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	 * @return the contactsUserId
	 */
	public String getContactsUserId() {
		return contactsUserId;
	}

	/**
	 * @param contactsUserId
	 *            the contactsUserId to set
	 */
	public void setContactsUserId(String contactsUserId) {
		this.contactsUserId = contactsUserId;
	}

	/**
	 * @return the contactsFigureId
	 */
	public String getContactsFigureId() {
		return contactsFigureId;
	}

	/**
	 * @param contactsFigureId
	 *            the contactsFigureId to set
	 */
	public void setContactsFigureId(String contactsFigureId) {
		this.contactsFigureId = contactsFigureId;
	}

	/**
	 * @return the relationEstablishType
	 */
	public String getRelationEstablishType() {
		return relationEstablishType;
	}

	/**
	 * @param relationEstablishType
	 *            the relationEstablishType to set
	 */
	public void setRelationEstablishType(String relationEstablishType) {
		this.relationEstablishType = relationEstablishType;
	}

	public String getContactsType() {
		return contactsType;
	}

	public void setContactsType(String contactsType) {
		this.contactsType = contactsType;
	}

	@Override
	public String toString() {
		return "ContactsReationRequest [userId=" + userId + ", figureId=" + figureId + ", contactsUserId="
				+ contactsUserId + ", contactsFigureId=" + contactsFigureId + ", relationEstablishType="
				+ relationEstablishType + ", contactsType=" + contactsType + "]";
	}

}
