package com.xianglin.appserv.common.service.integration.cif;

import java.util.List;

import com.xianglin.xlappserver.common.service.facade.base.Md;

public interface MessageSendClient {
	
	/**
	 * 想server推送消息
	 * @param mds
	 */
	public void sendNotice(List<Md> mds);
	
	/**
	 * 向客户端发送即时通知消息
	 * @param md 消息内容
	 */
	public void sendNotice(Md md);
	
	/**
	 * 发送群通知
	 * 
	 * @param groupId 群id
	 * @param noticeType 通知类型
	 * @param updateId 被操作用户id
	 * @param updateFigure 被操作用户
	 * @param operFigrue 操作角色
	 */
	public void sendGroupNotice(String groupId,Integer noticeType,String updateId,String updateFigure,String operFigrue);
}
