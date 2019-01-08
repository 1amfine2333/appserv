package com.xianglin.appserv.biz.service.implement;

import com.xianglin.appserv.biz.shared.ActivityManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.AppActivityReward;
import com.xianglin.appserv.common.dal.dataobject.AppActivityTask;
import com.xianglin.appserv.common.dal.dataobject.AppTransaction;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.app.ActivityTurntableService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityRewardVo;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityTaskVo;
import com.xianglin.appserv.common.service.facade.model.vo.AppTransactionVo;
import com.xianglin.appserv.common.service.facade.model.vo.WechatShareInfo;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.appserv.core.model.exception.AppException;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglei on 2017/5/3.
 */
@ServiceInterface
@Service("activityTurntableService")
public class ActivityTurntableServiceImpl implements ActivityTurntableService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityTurntableServiceImpl.class);

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private UserManager userManager;

    /**
     * 查询当日大转盘任务进度
     *
     * @param partyId
     * @param activityCode
     * @param daily
     * @return
     */
    @Override
    public Response<List<ActivityTaskVo>> queryProgess(Long partyId, String activityCode, String daily) {
        Response<List<ActivityTaskVo>> response = ResponseUtils.successResponse();
        try {
            response.setResult(DTOUtils.map(activityManager.queryActivityTask(AppActivityTask.builder().daily(daily).partyId(partyId).activityCode(activityCode).build(), null), ActivityTaskVo.class));
        } catch (Exception e) {
            logger.error("queryProgess error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * 查询大转盘分享明细
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.ActivityTurntableService.queryWechatShareInfo", description = "查询大转盘分享明细")
    public Response<WechatShareInfo> queryWechatShareInfo(String activityCode) {
        Response<WechatShareInfo> response = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            User user = userManager.queryUser(partyId);
            if (StringUtils.equals(activityCode, "001")) {
                if (user != null) {
                    if(StringUtils.isEmpty(user.getShowName())){
                        user.setShowName(queryShowName(user));
                    }
                    WechatShareInfo info = WechatShareInfo.builder().title("@"+user.getShowName() +" 编写了自己的家谱，是不是没见过，赶紧来看看吧！")
                            .titieImg(user.getHeadImg()).content("乡邻App - 上乡邻，啥都有，发家致富不用愁！")
                            .url(SysConfigUtil.getStr("usergenealogy_share_url") + "?partyId=" + user.getPartyId()).build();
                    response.setResult(info);
                }
            }
        } catch (Exception e) {
            logger.error("queryWechatShareInfo error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    private String queryShowName(User user) {
        if(user.getPartyId() != null){
            user.setShowName("xl"+user.getPartyId());
        }
        if(StringUtils.isNotEmpty(user.getTrueName())){
            user.setShowName(user.getTrueName());
        }
        if(StringUtils.isNotEmpty(user.getNikerName())){
            user.setShowName(user.getNikerName());
        }
        return user.getShowName();
    }

    @Override
    public Response<Boolean> updateProgess(ActivityTaskVo req) {
        Response<Boolean> response = ResponseUtils.successResponse();
        try {
            response.setResult(activityManager.saveUpdateActivityTask(DTOUtils.map(req, AppActivityTask.class)).getId() == null);
        } catch (Exception e) {
            logger.error("updateProgess error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * 获取幸运go奖励
     *
     * @param partyId
     * @return
     */
    @Override
    public Response<AppTransactionVo> rewardTurntable(Long partyId) {
        Response<AppTransactionVo> response = ResponseUtils.successResponse();
        try {
            List<AppTransaction> list = activityManager.reward(partyId, Constant.ActivityType.LUCKY.code);
            if (CollectionUtils.isNotEmpty(list)) {
                response.setResult(DTOUtils.map(list.get(0), AppTransactionVo.class));
            } else {
                response.setFacade(FacadeEnums.ERROR_CHAT_400011);
            }
        } catch (AppException e) {
            logger.error("rewardTurntable error", e);
            response.setFacade(e.getFacade());
        } catch (Exception e) {
            logger.error("rewardTurntable error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    public Response<List<ActivityRewardVo>> queryNewUserGift(Long partyId, String activityCode) {
        Response<List<ActivityRewardVo>> response = ResponseUtils.successResponse();
        try {
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("activityCode", activityCode);
            paras.put("rewardStatus", Constant.YESNO.YES.code);
            if (StringUtils.equals(activityCode, Constant.ActivityType.LUCKY.code)) {//幸运go活动，需要区分用户类型
                User u = userManager.queryUser(partyId);
                if (u != null) {
                    paras.put("rewardCategary", u.getUserType());
                } else {
                    //提示没有当前用户
                }
            } else if (StringUtils.equals(activityCode, Constant.ActivityType.NEW_GIFT.code)) {//新人有礼，需要判断该用户是否有新人礼包
                List<AppActivityTask> tasks = activityManager.queryActivityTask(AppActivityTask.builder().partyId(partyId).taskCode(Constant.ActivityTaskType.NEWgIFT.name()).build(), null);
                if (CollectionUtils.isNotEmpty(tasks) && tasks.size() == 1) {
                    AppActivityTask task = tasks.get(0);
                } else {
                    //不是新人没有奖励
                }
            } else {
                //没有找到对应的礼物
            }

            response.setResult(DTOUtils.map(activityManager.queryActivityReward(paras), ActivityRewardVo.class));
        } catch (Exception e) {
            logger.error("updateProgess error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * 领取新人礼
     *
     * @param partyId
     * @return
     */
    @Override
    public Response<List<AppTransactionVo>> rewardNewUserGift(Long partyId) {
        Response<List<AppTransactionVo>> response = ResponseUtils.successResponse();
        try {
            List<AppTransaction> list = activityManager.reward(partyId, Constant.ActivityType.NEW_GIFT.code);
            if (CollectionUtils.isNotEmpty(list)) {
                response.setResult(DTOUtils.map(list, AppTransactionVo.class));
            } else {
                response.setFacade(FacadeEnums.ERROR_CHAT_400011);
            }
        } catch (AppException e) {
            logger.error("rewardNewUserGift error", e);
            response.setFacade(e.getFacade());
        } catch (Exception e) {
            logger.error("rewardNewUserGift error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }
}
