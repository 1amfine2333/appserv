/**
 * 
 */
package com.xianglin.appserv.biz.shared;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.ProfitDetailDTO;
import com.xianglin.appserv.common.service.facade.model.vo.req.MonthAchieveQuery;

/**
 * 
 * 
 * @author zhangyong 2016年8月17日上午10:39:19
 */
public interface ProfitDetailManager {

	/**
	 * 
	 * 
	 * 查询收益|业绩
	 * @param monthQuery
	 * @param partyId
	 * @return
	 */
	List<ProfitDetailDTO> getProfitDetailList(MonthAchieveQuery monthQuery,Long partyId);
}
