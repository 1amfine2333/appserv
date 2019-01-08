package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.dal.dataobject.ActivityInviteRanking;


public interface ActivityInviteRankingDAO extends BaseDAO<ActivityInviteRanking>{
	
	/**
	 * 排行榜查询
	 * 
	 * @param req
	 * @return
	 */
	List<ActivityInviteRanking> selectRanking(Map<String, Object> req);
}