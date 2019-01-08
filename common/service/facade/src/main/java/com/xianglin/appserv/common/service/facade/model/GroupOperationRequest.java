/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;
import java.util.List;

/**
 * 群操作请求，用于（批量）邀请好友加入群或者（批量）踢用户出群
 * 
 * @author pengpeng 2016年2月29日上午11:26:55
 */
public class GroupOperationRequest implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 4395364659031311596L;

	/** 发起操作的用户在该群中的身份角色唯一标识 */
	private String figureId;

	/** 群组唯一标识 */
	private String groupId;

	/** 成员列表 */
	private List<UserFigureIdDTO> members;

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
	 * @return the members
	 */
	public List<UserFigureIdDTO> getMembers() {
		return members;
	}

	/**
	 * @param members
	 *            the members to set
	 */
	public void setMembers(List<UserFigureIdDTO> members) {
		this.members = members;
	}

}
