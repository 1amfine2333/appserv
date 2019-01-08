package com.xianglin.appserv.common.service.facade;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.CommonResp;
import com.xianglin.appserv.common.service.facade.model.vo.RewardVo;

import java.util.List;
import java.util.Map;

/**
 * 打赏
 * @dateTime 2016年11月3日 下午2:03:38
 *
 */
public interface RewardService {
	
	/**
     * 查询今日对 nodePartyId 献过花
     * 并且nodePartyId没有对他回赠的 站点 
     * 
     * @param rewardVo
     * @return
     */
	Response<CommonResp<List<Map<String, Object>>>> queryTodayRecordNode(RewardVo rewardVo);
	/**
	 * 查询历史的对某个站点打赏的记录
	 *
	 * @param rewardVo
	 * @return
	 */
	Response<CommonResp<List<Map<String, Object>>>> queryHistoryRecordNode(RewardVo rewardVo);

}
