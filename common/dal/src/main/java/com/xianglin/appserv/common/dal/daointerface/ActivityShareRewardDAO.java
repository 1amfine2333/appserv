package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.dal.dataobject.ActivityShareReward;

public interface ActivityShareRewardDAO extends BaseDAO<ActivityShareReward> {

	/**
	 * 根据(条件)分页查询
	 * 
	 * @param paras
	 * @return
	 */
	List<ActivityShareReward> selectList(Map<String, Object> paras);

	/**
	 * 根据(条件)查询每条分享当天红包领取情况
	 * 
	 * @param paras
	 * @return
	 */
	int selectCount(Map<String, Object> paras);
}