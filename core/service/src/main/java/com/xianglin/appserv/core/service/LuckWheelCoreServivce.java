package com.xianglin.appserv.core.service;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.AppActiveShareDTO;
import com.xianglin.appserv.common.service.facade.model.AppPrizeUserRelDTO;
import com.xianglin.appserv.common.service.facade.model.AppPrizesActivityRuleDTO;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.CommonResp;

public interface LuckWheelCoreServivce {
	/**
	 * 获取活动信息列表
	 * @author gengchaogang
	 * @dateTime 2016年12月23日 上午11:27:37
	 * @param appPrizesActivityRuleDTO
	 * @return
	 */
	public List<AppPrizesActivityRuleDTO> getAppPrizesActivityRule(AppPrizesActivityRuleDTO appPrizesActivityRuleDTO) throws BusiException;
	/**
	 * 获取活动奖品
	 * @author gengchaogang
	 * @dateTime 2016年12月30日 下午2:09:49
	 * @param appPrizeUserRelDTO
	 * @return
	 */
	public AppPrizeUserRelDTO makeLuckWheel(AppPrizeUserRelDTO appPrizeUserRelDTO) throws BusiException;
	/**
	 * 插入分享活动记录
	 * @author gengchaogang
	 * @dateTime 2016年12月30日 下午5:57:58
	 * @param appActiveShareDTO
	 * @return
	 * @throws BusiException
	 */
	public AppActiveShareDTO insertAppActiveShare(AppActiveShareDTO appActiveShareDTO) throws BusiException;
	/**
	 * 获取获奖列表
	 * @author gengchaogang
	 * @dateTime 2017年1月3日 上午11:02:27
	 * @param appPrizeUserRelDTO
	 * @return
	 * @throws BusiException
	 */
	public CommonResp<List<AppPrizeUserRelDTO>> getUserLuckWheelPrizes(AppPrizeUserRelDTO appPrizeUserRelDTO) throws BusiException;
	/**
	 * 获取剩余抽奖次数
	 * @author gengchaogang
	 * @dateTime 2017年1月10日 上午11:38:42
	 * @param appPrizeUserRelDTO
	 * @return
	 * @throws BusiException
	 */
	public int getLastCount(AppPrizeUserRelDTO appPrizeUserRelDTO) throws BusiException;
}
