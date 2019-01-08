/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;

/**
 * 用户及其身份角色唯一标识
 * 
 * @author pengpeng 2016年2月22日下午6:09:42
 */
public class UserFigureIdDTO implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 8152509826245424451L;

	/** 用户唯一标识 */
	private String userId;

	/** 用户身份角色唯一标识 */
	private String figureId;

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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserIdFigureId [userId=" + userId + ", figureId=" + figureId + "]";
	}

}
