package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityTaskVo;

/**V3.2.1版本运营活动服务
 * Created by wanglei on 2017/9/21.
 */
public interface ActivityV321Service {

    String ACTIVITY_CODE = "103";

    /**
     * 查询用户成功邀请数量
     * @param partyId 当前用户partyId
     * @return
     */
    Response<Integer> queryInviteCount(Long partyId);

    /** 确认邀请关系，被邀请人输入手机号进行邀请
     * @param partyId
     * @param mobilePhone
     * @return
     */
    Response<Integer> createInvite(Long partyId,String mobilePhone);

    /** 查询用户提醒
     * 查询最后一个，兵将所有的数据设置为已处理
     * @param partyId
     * @return
     */
    Response<ActivityTaskVo> queryUpdateAlert(Long partyId);

    /**每日分型兵获得奖励
     * 每天只能获得一次
     * @param partyId
     * @return
     */
    Response<Boolean> shareReward(Long partyId);

}
