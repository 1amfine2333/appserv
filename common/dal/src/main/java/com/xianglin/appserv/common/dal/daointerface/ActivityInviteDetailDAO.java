package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.dal.dataobject.ActivityDeposits;
import com.xianglin.appserv.common.dal.dataobject.ActivityInviteDetail;


public interface ActivityInviteDetailDAO extends BaseDAO<ActivityInviteDetail>{
	
	/**
	 * 全量查询
	 * 
	 * @param req
	 * @return
	 */
	List<ActivityInviteDetail> select(ActivityInviteDetail req);
	
	/**
	 * 分页查询
	 * 
	 * @param paras
	 * @return
	 */
	List<ActivityInviteDetail> selectMap(Map<String, Object> paras);
	
	/**
	 * 查询有推荐成功的用户
	 * 
	 * @return
	 */
	List<Long> selectAlertPartyIds();

	/**更新detail撞他 根据loginName,activityCode
	 * @param detail
	 * @return
	 */
	int updateSDetail(ActivityInviteDetail detail);

	/**
	 * 查询数量
	 * @param detail
	 * @return
	 */
	int selectCount(ActivityInviteDetail detail);

	/**带条件插入 根据activityCode,loginName排重
	 * @param req
	 * @return
	 */
	int insertWithSelect(ActivityInviteDetail req);

    /**
     * 查询未发奖励的
     * @return
     */
    List<ActivityInviteDetail> selectNoRewardInvite();

    int updateByPrimaryKeySelective(ActivityInviteDetail activityInviteDetail);
}
