package com.xianglin.appserv.common.service.facade;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.AppActiveShareDTO;
import com.xianglin.appserv.common.service.facade.model.AppPrizeUserRelDTO;
import com.xianglin.appserv.common.service.facade.model.AppPrizesActivityRuleDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.CommonResp;

public interface LuckWheelServivce {
	
	/**
	 * 获取活动列表
	 * @author gengchaogang
	 * @dateTime 2016年12月30日 上午11:38:33
	 * @return
	 */
	public Response<List<AppPrizesActivityRuleDTO>> getAppPrizesActivity(AppPrizesActivityRuleDTO appPrizesActivityRuleDTO);
	/**
	 * 抽奖
	 * @author gengchaogang
	 * @dateTime 2017年1月3日 上午10:01:12
	 * @param appPrizeUserRelDTO
	 * @return
	 */
	public Response<AppPrizeUserRelDTO> makeLuckWheel(AppPrizeUserRelDTO appPrizeUserRelDTO);
	/**
	 * 分享
	 * @author gengchaogang
	 * @dateTime 2017年1月3日 上午10:01:03
	 * @param appActiveShareDTO
	 * @return
	 */
	public Response<AppActiveShareDTO> insertAppActiveShare(AppActiveShareDTO appActiveShareDTO);
	/**
	 * 中奖记录查询
	 * @author gengchaogang
	 * @dateTime 2017年1月4日 上午10:26:37
	 * @param appPrizeUserRelDTO
	 * @return
	 * @throws BusiException
	 */
	public Response<CommonResp<List<AppPrizeUserRelDTO>>> getUserLuckWheelPrizes(AppPrizeUserRelDTO appPrizeUserRelDTO);
	
	/**
	 * 查询剩余抽奖次数
	 * @author gengchaogang
	 * @dateTime 2017年1月10日 上午11:47:33
	 * @param appPrizeUserRelDTO
	 * @return
	 */
	public Response<Integer> getLastCount(AppPrizeUserRelDTO appPrizeUserRelDTO);
	
}
