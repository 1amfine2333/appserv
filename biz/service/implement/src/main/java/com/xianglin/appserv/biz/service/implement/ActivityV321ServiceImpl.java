package com.xianglin.appserv.biz.service.implement;

import com.xianglin.appserv.biz.shared.ActivityInviteManager;
import com.xianglin.appserv.biz.shared.ActivityManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.ActivityInviteDetail;
import com.xianglin.appserv.common.dal.dataobject.AppActivityTask;
import com.xianglin.appserv.common.dal.dataobject.AppTransaction;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.app.ActivityV321Service;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.enums.YesNo;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityTaskVo;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by wanglei on 2017/9/22.
 */
@Service("activityV321Service")
public class ActivityV321ServiceImpl implements ActivityV321Service {

    private static final Logger logger = LoggerFactory.getLogger(ActivityV321ServiceImpl.class);

    @Autowired
    private ActivityInviteManager activityInviteManager;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private UserManager userManager;

    /**
     * 查询用户成功邀请数量
     *
     * @param partyId 当前用户partyId
     * @return
     */
    @Override
    public Response<Integer> queryInviteCount(Long partyId) {
        Response<Integer> response = ResponseUtils.successResponse();
        try {
            response.setResult(activityInviteManager.queryDetailCount(ActivityInviteDetail.builder()
                    .activityCode(ActivityV321Service.ACTIVITY_CODE)
                    .recPartyId(partyId)
                    .status(Constant.ActivityInviteStatus.S.name()).build()));
        } catch (Exception e) {
            logger.warn("unReadMsgCount error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * 确认邀请关系，被邀请人输入手机号进行邀请
     *
     * @param partyId
     * @param mobilePhone
     * @return
     */
    @Override
    public Response<Integer> createInvite(Long partyId, String mobilePhone) {
        Response<Integer> response = ResponseUtils.successResponse();
        try {
            response.setResult(activityInviteManager.addInvateDetail(ActivityInviteDetail.builder()
                    .activityCode(ActivityV321Service.ACTIVITY_CODE)
                    .recPartyId(partyId)
                    .loginName(mobilePhone)
                    .status(Constant.ActivityInviteStatus.I.name()).build()));
        } catch (Exception e) {
            logger.warn("unReadMsgCount error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * 查询用户提醒
     * 查询最后一个，兵将所有的数据设置为已处理
     *
     * @param partyId
     * @return
     */
    @Override
    public Response<ActivityTaskVo> queryUpdateAlert(Long partyId) {
        Response<ActivityTaskVo> response = ResponseUtils.successResponse();
        try {
            AppActivityTask task = AppActivityTask.builder()
                    .activityCode(ActivityV321Service.ACTIVITY_CODE)
                    .partyId(partyId)
                    .taskStatus(Constant.YESNO.YES.code)
                    .useStatus(Constant.YESNO.NO.code).build();
            List<AppActivityTask> list = activityManager.queryActivityTask(task, null);
            if (CollectionUtils.isNotEmpty(list)) {
//                task = list.get(0);
//                List<AppTransaction> trans = activityManager.queryTransaction(AppTransaction.builder().activityCode(ACTIVITY_CODE).taskId(task.getId()).transStatus(Constant.TransStatus.S.name()).build(), null);
//                if (CollectionUtils.isNotEmpty(trans)) {
//                    task.setTaskCode(trans.get(0).getAmtType());
//                    task.setTaskResult(trans.get(0).getAmount().toPlainString());
//                }
                response.setResult(DTOUtils.map(list.get(0), ActivityTaskVo.class));
                for (AppActivityTask t : list) {
                    t.setUseStatus(Constant.YESNO.YES.code);
                    activityManager.saveUpdateActivityTask(t);
                }
            }
        } catch (Exception e) {
            logger.warn("unReadMsgCount error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * 每日分型兵获得奖励
     * 每天只能获得一次
     *
     * @param partyId
     * @return
     */
    @Override
    public Response<Boolean> shareReward(Long partyId) {
        Response<Boolean> response = ResponseUtils.successResponse(true);
        try {
            //分享并后去一次分享奖励
            User user = userManager.queryUser(partyId);
            String today = DateUtils.formatDate(new Date(), "yyyyMMdd");
            AppActivityTask task = AppActivityTask.builder()
                    .partyId(partyId).daily(today).taskCode(Constant.ActivityTaskType.SHARE.name())
                    .activityCode(ActivityV321Service.ACTIVITY_CODE).build();
            List<AppActivityTask> list = activityManager.queryActivityTask(task, null);
            if (CollectionUtils.isNotEmpty(list)) {//发现有未发放奖励的，重新发放
                for (AppActivityTask t : list) {
                    if (StringUtils.equals(t.getUseStatus(), YesNo.N.name())) {
                        activityManager.reward(partyId, t.getId(),null);
                    }
                }
            } else {
                task = AppActivityTask.builder().partyId(partyId)
                        .deviceId(user.getDeviceId()).mobilePhone(user.getLoginName()).activityCode(ACTIVITY_CODE).taskStatus(YesNo.Y.name())
                        .taskCode(Constant.ActivityTaskType.SHARE.name()).daily(today).taskDailyId(today + ACTIVITY_CODE + partyId)
                        .useStatus(YesNo.N.name()).taskName(Constant.ActivityTaskType.SHARE.desc).build();
                boolean result = activityManager.saveUpdateActivityTask(task).getId() == null;
                if (result) {
                    activityManager.reward(partyId, task.getId(),null);
                }
            }
        } catch (Exception e) {
            logger.warn("unReadMsgCount error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }
}
