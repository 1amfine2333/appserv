/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;

/**
 * 用户身份角色DTO
 * 
 * @author pengpeng 2016年2月22日下午3:01:19
 */
public class UserFigureDTO implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -8219183562838507055L;

	/** 用户唯一标识 */
	private String userId;

	/** 用户身份角色唯一标识 */
	private String figureId;

	/** 昵称或备注名 */
	private String nickName;

	/** 头像图片url */
	private String avatarUrl;

	/** 性别 */
	private String gender;

	/** 性取向 */
	private String sexualOrientation;

	/** 个性签名 */
	private String individualitySignature;

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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserFigureDTO [userId=" + userId + ", figureId=" + figureId + ", nickName=" + nickName + ", avatarUrl="
				+ avatarUrl + ", gender=" + gender + ", sexualOrientation=" + sexualOrientation
				+ ", individualitySignature=" + individualitySignature + "]";
	}

}
