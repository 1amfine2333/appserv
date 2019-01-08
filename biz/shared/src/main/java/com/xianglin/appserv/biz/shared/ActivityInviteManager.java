/**
 * 
 */
package com.xianglin.appserv.biz.shared;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.dal.dataobject.ActivityInvite;
import com.xianglin.appserv.common.dal.dataobject.ActivityInviteDetail;
import com.xianglin.appserv.common.service.facade.model.Response;

/**
 * 
 * 
 * @author wanglei 2016年12月13日下午4:32:29
 */
public interface ActivityInviteManager {

	/**
	 * 查询或初始化个人邀请记录
	 * 
	 * @param partyId
	 * @return
	 */
	public ActivityInvite queryInit(Long partyId);
	
	/**
	 * 根据条件查询排名
	 * 
	 * @param req
	 * @return
	 */
	public List<ActivityInvite> queryRanking(Map<String, Object> req);
	
	/**
	 * 查询并更新待提醒推荐(推荐成功)列表
	 * 需要更新提醒状态为已经提醒
	 * @param partyId
	 * @return
	 */
	public List<ActivityInviteDetail> queryUpdateRemind(Long partyId);
	
	/**
	 * 查询个人推荐成功用户记录
	 * 
	 * @param req
	 * @return
	 */
	public List<ActivityInviteDetail> queryInvateDetail(Map<String, Object> req);
	
	/**
	 * 新增邀请记录
	 * 
	 * @param req
	 */
	int addInvateDetail(ActivityInviteDetail req);

	/**查询数量
	 * @param detail
	 * @return
	 */
	int queryDetailCount(ActivityInviteDetail detail);

    int updateInviteDetail(ActivityInviteDetail activityInviteDetail);
}
