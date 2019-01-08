/**
 * 
 */
package com.xianglin.appserv.common.service.facade;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.*;

import java.util.List;
import java.util.Map;



/**
 * 
 * 
 * @author leaf 2016年8月11日下午5:56:52
 */
public interface PresentedFlowersService {
	
	/**
	 * 
	 * 查询区域内余额排名，银行楷书排名 余额 银行卡数 以及上已导余额 银行卡数 
	 * @param req
	 * @return
	 */
	Response<AgentDetailVo> getAgentDetail(BankImportVo req);
	
	/**
	 * 
	 * 查询区域内榜单
	 * @param param
	 * @return
	 */
	Response<CommonResp<List<Map<String,Object>>>> queryRankList(Map<String, Object> param);
	
	
	/**
	 * 插入送花记录
	 * 
	 * @param flowersVo
	 * @return
	 */
	Response<FlowersVo> insertFlowersLog(FlowersVo flowersVo);
	
	/**
     * 查询今日对 nodePartyId 献过花
     * 并且nodePartyId没有对他回赠的 站点 
     * 
     * @param nodePartyId
     * @param nodeManagerPartyId
     * @return
     */
	Response<List<Map<String, Object>>> queryTodayRecordNode(Long nodePartyId, Long nodeManagerPartyId);
	/**
	 * 查询历史的对某个站点献花的记录
	 * @param  flowersVo
	 * @return
	 */
	Response<CommonResp<List<Map<String, Object>>>> queryHistoryRecordNode(FlowersVo flowersVo);
}
