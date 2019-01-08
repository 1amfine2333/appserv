package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppActivityReward;
import com.xianglin.appserv.common.dal.dataobject.AppActivityTask;

import java.util.List;
import java.util.Map;

public interface AppActivityRewardDAO extends BaseDAO<AppActivityReward> {

	/** 根据条件查询
	 * @param paras
	 * @return
	 */
	List<AppActivityReward> selectList(Map<String, Object> paras);

	/** 根据条件查询数量
	 * @param paras
	 * @return
	 */
	Integer selectCount(Map<String, Object> paras);

}