package com.xianglin.appserv.biz.shared;

import com.xianglin.appserv.common.dal.dataobject.ActivityDeposits;

/**
 * Created by wanglei on 2017/1/6.
 */
public interface ActivityDepositsManager {


    /**
     * 初始化（更新）揽储目标
     * @param partyId
     * @return
     */
    public ActivityDeposits queryInitDeposits(Long partyId);

    /**
     * 发放奖励
     * @param partyId
     * @return
     */
    public ActivityDeposits rewardDeposits(Long partyId);
}
