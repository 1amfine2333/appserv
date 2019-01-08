package com.xianglin.appserv.biz.shared;

import com.xianglin.appserv.common.dal.dataobject.ActivityShareAuth;
import com.xianglin.appserv.common.dal.dataobject.ActivityShareDaily;
import com.xianglin.appserv.common.dal.dataobject.ActivityShareReward;
import com.xianglin.appserv.core.model.exception.AppException;

import java.util.List;
import java.util.Map;

/**
 * 红包雨活动管理
 */
public interface ActivityShareManager {

    /**
     * 查询并初始化daily
     * 
     * @param partyId
     * @return
     */
    ActivityShareDaily queryInitShareDaily(Long partyId);

    /**
     * 更新每日任务进度
     * 
     * @param daily
     */
    void updateShareDaily(ActivityShareDaily daily);

    /**
     * 新增或更新用户微信授权信息
     * 
     * @param auth
     * @return
     */
    ActivityShareAuth addUpdateShareAuth(ActivityShareAuth auth);

    /**
     * 查询微信授权信息
     * 
     * @param openId
     * @return
     */
    ActivityShareAuth queryShareAuth(String openId);

    /**
     * 领取奖励
     * 
     * @param reward
     * @return
     * @throws AppException
     */
    ActivityShareReward shareeward(ActivityShareReward reward) throws AppException;

    /**
     * 查询领取奖励列表
     * 
     * @param paras
     * @return
     */
    List<ActivityShareReward> queryShareRewards(Map<String, Object> paras);

    /**
     * 发放红包雨奖励， 异步执行
     * 
     * @param mobile
     */
    void reward(String mobile);
}
