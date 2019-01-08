package com.xianglin.appserv.biz.shared;

import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.core.model.exception.AppException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/** 通用活动管理及3.0活动
 * Created by wanglei on 2017/5/4.
 */
public interface ActivityManager {

    /**查询活动列表
     * @param paras
     * @return
     */
    List<AppActivity> queryActivitys(Map<String,Object> paras);

    /**查询各个人物进度
     * @param paras
     * @param page
     * @return
     */
    List<AppActivityTask> queryActivityTask(AppActivityTask paras, Page page);

    /**新增/更新活动任务
     * @param task
     * @return
     */
    AppActivityTask saveUpdateActivityTask(AppActivityTask task);

    /**查询活动奖励
     * @param paras
     * @return
     */
    List<AppActivityReward> queryActivityReward(Map<String,Object> paras);

    /**发放奖励
     * @param partyId 受奖励人partyId
     * @param activityCode 活动号
     * @return
     * @throws AppException
     */
    @Deprecated
    List<AppTransaction> reward(Long partyId, String activityCode)throws AppException;

    /**根据用户任务发放奖励 发放奖励
     * @param partyId 用户
     * @param taskId 根据用户完成的
     * @param  subCategory 奖励子类别
     * @return
     * @throws AppException
     */
    List<AppTransaction> reward(Long partyId,Long taskId,String subCategory)throws AppException;

    /**根据条件查询
     * @param tran
     * @param page
     * @return
     */
    List<AppTransaction> queryTransaction(AppTransaction tran,Page page);

    /**根据条件查询个数
     * @param paras
     * @param paras
     * @return
     */
    int queryActivityTaskCount(Map<String, Object> paras);

    List<AppActivityTask> queryActivityTaskByParas(Map<String, Object> paras);

    List<AppActivity> selecActivitytList(AppActivity build);

    /**
     * 查询游戏列表
     * @param id
     * @return
     */
    AppActivity queryActivityById(Long id);

    /**
     * 修改游戏点击量
     * @param appActivity
     * @return
     */
    Boolean updateActivity(AppActivity appActivity);

    /**执行批次兑换金币
     * @param batchId
     */
    void batchExchangeGold(String batchId);

    /**发放金币奖励，仅真滴V321活动
     * 奖励类型为空则不发放对应奖励
     * @param partyId 奖励用户
     * @param rewardType 奖励类型
     * @param recRewardType 推荐人奖励类型
     * @throws AppException
     */
    void rewardV321(Long partyId, Constant.ActivityTaskType rewardType, Constant.ActivityTaskType recRewardType)throws AppException;
}
