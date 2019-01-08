/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;

/**
 * 联系人DTO
 * 
 * @author pengpeng 2016年2月22日下午2:40:14
 */
public class ContactsDTO implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -4875853104443257997L;

	/** 当前用户身份角色唯一标识 */
	private String figureId;

	/** 联系人用户唯一标识 */
	private String contactsUserId;

	/** 联系人身份角色唯一标识 */
	private String contactsFigureId;

	/** 联系人备注名 */
	private String remarkName;

	/** 昵称 */
	private String nickName;

	/** 头像图片url */
	private String avatarUrl;

	/** 性别 */
	private String gender;

	/** 性取向 */
	private String sexualOrientation;

	/** 个性签名 */
	private String individualitySignature;

	/** 联系人类型，分组 */
	private String contactsType;

	/** 关系建立方式 */
	private String relationEstablishType;

	/** 关系建立时间 */
	private long relationEstablishTime;

	/** 联系人排序分值 */
	private int score;

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
	 * @return the remarkName
	 */
	public String getRemarkName() {
		return remarkName;
	}

	/**
	 * @param remarkName
	 *            the remarkName to set
	 */
	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
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
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName
	 *            the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the sexualOrientation
	 */
	public String getSexualOrientation() {
		return sexualOrientation;
	}

	/**
	 * @param sexualOrientation
	 *            the sexualOrientation to set
	 */
	public void setSexualOrientation(String sexualOrientation) {
		this.sexualOrientation = sexualOrientation;
	}

	/**
	 * @return the individualitySignature
	 */
	public String getIndividualitySignature() {
		return individualitySignature;
	}

	/**
	 * @param individualitySignature
	 *            the individualitySignature to set
	 */
	public void setIndividualitySignature(String individualitySignature) {
		this.individualitySignature = individualitySignature;
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

	/**
	 * @param contactsType
	 *            the contactsType to set
	 */
	public void setContactsType(String contactsType) {
		this.contactsType = contactsType;
	}

	/**
	 * @return the relationEstablishTime
	 */
	public long getRelationEstablishTime() {
		return relationEstablishTime;
	}

	/**
	 * @param relationEstablishTime
	 *            the relationEstablishTime to set
	 */
	public void setRelationEstablishTime(long relationEstablishTime) {
		this.relationEstablishTime = relationEstablishTime;
	}

	/**
	 * @return the contactsType
	 */
	public String getContactsType() {
		return contactsType;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ContactsDTO [figureId=" + figureId + ", contactsUserId=" + contactsUserId + ", contactsFigureId="
				+ contactsFigureId + ", remarkName=" + remarkName + ", nickName=" + nickName + ", avatarUrl="
				+ avatarUrl + ", gender=" + gender + ", sexualOrientation=" + sexualOrientation
				+ ", individualitySignature=" + individualitySignature + ", contactsType=" + contactsType
				+ ", relationEstablishType=" + relationEstablishType + ", relationEstablishTime="
				+ relationEstablishTime + ", score=" + score + "]";
	}

}
