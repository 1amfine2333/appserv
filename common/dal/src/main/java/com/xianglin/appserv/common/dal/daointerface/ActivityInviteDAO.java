package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.dal.dataobject.ActivityInvite;
import org.apache.ibatis.annotations.Param;


public interface ActivityInviteDAO extends BaseDAO<ActivityInvite>{
	
	/**
	 * 根据partyId查询
	 * 
	 * @param partyId
	 * @return
	 */
	@Deprecated
	ActivityInvite selectByPartyId(@Param("partyId") Long partyId);

	/** 根据parityId和活动号查询
	 * @param partyId
	 * @param activityCode
	 * @return
	 */
	ActivityInvite selectActivity(@Param("partyId") Long partyId, @Param("activityCode") String activityCode);
	/**
	 * 排行榜查询
	 * 
	 * @param req
	 * @return
	 */
	List<ActivityInvite> selectRanking(Map<String, Object> req);
}