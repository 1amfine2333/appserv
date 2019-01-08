package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;

import com.xianglin.appserv.common.dal.dataobject.AppPrizeUserRelDO;
import com.xianglin.appserv.common.dal.dataobject.AppPrizesActivityRuleDO;

public interface AppPrizesActivityRuleDAO {
    /**
     * 查询活动奖品规则
     * @author gengchaogang
     * @dateTime 2016年12月23日 上午11:10:41
     * @param appPrizesActivityRuleDO
     * @return
     */
	List<AppPrizesActivityRuleDO> queryActivePrizeRule(AppPrizesActivityRuleDO appPrizesActivityRuleDO);
	/**
	 * 查询获奖记录
	 * @author gengchaogang
	 * @dateTime 2017年1月3日 上午11:23:53
	 * @param appPrizeUserRelDTO
	 * @return
	 */
	List<AppPrizeUserRelDO> getUserLuckWheelPrizes(AppPrizeUserRelDO appPrizeUserRelDO);
}