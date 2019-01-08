package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityRewardVo;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityTaskVo;
import com.xianglin.appserv.common.service.facade.model.vo.AppTransactionVo;
import com.xianglin.appserv.common.service.facade.model.vo.WechatShareInfo;

import java.util.List;

/** app3.0b版本运营活动接口，包括转盘和新手奖励
 * Created by wanglei on 2017/5/3.
 */
public interface ActivityTurntableService {

    /**查询当日大转盘任务进度
     * @param partyId
     * @param activityCode 活动编号
     * @param daily
     * @return
     */
    Response<List<ActivityTaskVo>> queryProgess(Long partyId,String activityCode,String daily);

    /**查询大转盘分享明细
     * @param activityCode
     * @return
     */
    Response<WechatShareInfo> queryWechatShareInfo(String activityCode);

    /**更新进度为成功
     * @param req
     * @return
     */
    Response<Boolean> updateProgess(ActivityTaskVo req);

    /**获取奖励
     * @param partyId
     * @return
     */
    Response<AppTransactionVo> rewardTurntable(Long partyId);

    /**查询奖励
     * @param partyId
     * @param activityCode
     * @return
     */
    Response<List<ActivityRewardVo>> queryNewUserGift(Long partyId,String activityCode);

    /**领取新人礼
     * @param partyId
     * @return
     */
    Response<List<AppTransactionVo>> rewardNewUserGift(Long partyId);
}
