/**
 * 
 */
package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityDepositsVo;

/**
 * 揽储活动接口
 */
public interface ActivityDepositsService {

	/**
	 * 查询（初始化）个人揽储进度
	 * 
	 * @param parytId
	 * @return
	 */
	Response<ActivityDepositsVo> queryDeposits(Long parytId);

	/**
	 * 领取揽储活动奖励
	 * 
	 * @param parytId
	 * @return
	 */
	Response<ActivityDepositsVo> rewardDeposits(Long parytId);

}
