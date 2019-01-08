package com.xianglin.appserv.biz.service.implement;

/**
 * Created by wanglei on 2017/3/13.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xianglin.appserv.biz.shared.ActivityShareManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.ActivityShareAuth;
import com.xianglin.appserv.common.dal.dataobject.ActivityShareDaily;
import com.xianglin.appserv.common.dal.dataobject.ActivityShareReward;
import com.xianglin.appserv.common.service.facade.app.ActivityShareService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityShareAuthVo;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityShareDailyVo;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityShareRewardVo;
import com.xianglin.appserv.common.service.facade.model.vo.WechatShareInfo;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.appserv.core.model.exception.AppException;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;

/**
 * 红包雨活动接口实现类
 *
 * @author
 * @create 2017-03-13 17:53
 **/
@ServiceInterface
@Service("activityShareService")
public class ActivityShareServiceImpl implements ActivityShareService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityShareServiceImpl.class);

    @Autowired
    private ActivityShareManager activityShareManager;

    @Autowired
    private SessionHelper sessionHelper;

    private final String appid = "wx0c1a1664441c4dd7";

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ActivityShareService.queryShareAlert", description = "显示是否弹出红包雨提示")
    public Response<String> queryShareAlert () {
        Response<String> response = ResponseUtils.successResponse();
        try {
            Boolean showTip = Boolean.parseBoolean(SysConfigUtil.getStr("activity.share.showTip", "false"));
            if (showTip) {
                Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
                if (partyId != null) {
                    ActivityShareDaily daily = activityShareManager.queryInitShareDaily(partyId);
                    if (StringUtils.equals(daily.getTipAlertStatus(), Constant.YESNO.YES.code)) {
                        // 已登录用户并提示过，未提示过
                        showTip = false;
                    }
                }
            }
            if (showTip) {
                response.setResult(SysConfigUtil.getStr("activity.share.showTip.url"));
            }
        } catch (Exception e) {
            logger.error("showShareTip error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ActivityShareService.confirmShareAlert", description = "确认每日任务提示框")
    public Response<Boolean> confirmShareAlert () {
        Response<Boolean> response = ResponseUtils.successResponse(true);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            ActivityShareDaily daily = activityShareManager.queryInitShareDaily(partyId);
            daily.setTipAlertStatus(Constant.YESNO.YES.code);
            activityShareManager.updateShareDaily(daily);
        } catch (Exception e) {
            logger.error("confirmShareAlert error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ActivityShareService.queryShareProgessAlert", description = "查询个人红包雨活动进度")
    public Response<WechatShareInfo> queryShareProgessAlert () {
        Response<WechatShareInfo> response = ResponseUtils.successResponse();
        try {
            Boolean showTip = Boolean.parseBoolean(SysConfigUtil.getStr("activity.share.showTip", "false"));
            if (showTip) {
                Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
                ActivityShareDaily daily = activityShareManager.queryInitShareDaily(partyId);
                if (StringUtils.equals(daily.getTaskStatus(), Constant.YESNO.YES.code) && StringUtils.equals(daily.getProgessAlertStatus(), Constant.YESNO.NO.code)) {
                    // 任务已经完成，提示框没有出过国的时候才会发出提示
                    WechatShareInfo info = new WechatShareInfo();
                    info.setTitle(SysConfigUtil.getStr("activity.share.wechat.title"));
                    info.setTitieImg(SysConfigUtil.getStr("activity.share.wechat.titleImg"));
                    info.setContent(SysConfigUtil.getStr("activity.share.wechat.content"));

//                    String url = " https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=URL&response_type=code&scope=snsapi_userinfo&state=dailyId#wechat_redirect";
//                    info.setUrl(url.replace("APPID",appid).replace("URL", URLEncoder.encode(SysConfigUtil.getStr("activity.share.wechat.url"),"utf-8")).replace("dailyId",daily.getId().toString()));
                    info.setUrl(SysConfigUtil.getStr("activity.share.wechat.url") + "?dailyId=" + daily.getId());
                    logger.info("WechatShareInfo :{}",info);
                    response.setResult(info);
                }
            }
        } catch (Exception e) {
            logger.error("queryShareProgessAlert error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ActivityShareService.confirmShareProgessAlert", description = "确认任务进度提示框")
    public Response<Boolean> confirmShareProgessAlert () {
        Response<Boolean> response = ResponseUtils.successResponse(true);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            ActivityShareDaily daily = activityShareManager.queryInitShareDaily(partyId);
            daily.setProgessAlertStatus(Constant.YESNO.YES.code);
            activityShareManager.updateShareDaily(daily);
        } catch (Exception e) {
            logger.error("confirmShareProgessAlert error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    public Response<ActivityShareDailyVo> queryShareDaily (Long partyId) {
        Response<ActivityShareDailyVo> resp = new Response<>(null);
        try {
            ActivityShareDaily activityShareDaily = activityShareManager.queryInitShareDaily(partyId);
            ActivityShareDailyVo vo = DTOUtils.map(activityShareDaily, ActivityShareDailyVo.class);
            resp.setResult(vo);
            resp.setCode(FacadeEnums.OK.code);
        } catch (Exception e) {
            logger.error("queryShareDaily error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    public Response<ActivityShareDailyVo> updateShareDaily (ActivityShareDailyVo vo) {
        Response<ActivityShareDailyVo> resp = new Response<>(vo);
        try {
            ActivityShareDaily activityShareDaily = activityShareManager.queryInitShareDaily(vo.getPartyId());
            activityShareDaily.setShareStatus("Y");
            activityShareManager.updateShareDaily(activityShareDaily);
            vo = DTOUtils.map(activityShareDaily, ActivityShareDailyVo.class);
            resp.setResult(vo);
            resp.setFacade(FacadeEnums.OK);
        } catch (Exception e) {
            logger.error("queryShareDaily error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    public Response<ActivityShareAuthVo> bindShareAuth (ActivityShareAuthVo vo) {
        Response<ActivityShareAuthVo> resp = new Response<ActivityShareAuthVo>(vo);
        try {
            ActivityShareAuth activityShareAuth = DTOUtils.map(vo, ActivityShareAuth.class);
            activityShareManager.addUpdateShareAuth(activityShareAuth);
            resp.setResult(vo);
            resp.setFacade(FacadeEnums.OK);
        } catch (Exception e) {
            logger.error("bindShareAuth error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    // 领取奖励
    public Response<ActivityShareRewardVo> receiveShareReward (Long mobilePhone, Long dailyId) {
        Response<ActivityShareRewardVo> resp = new Response<ActivityShareRewardVo>(null);
        try {
            ActivityShareReward asr = new ActivityShareReward();
            asr.setMobilePhone(String.valueOf(mobilePhone));
            asr.setDailyId(dailyId);
            ActivityShareReward shareeward = activityShareManager.shareeward(asr);
            ActivityShareRewardVo vo = DTOUtils.map(shareeward, ActivityShareRewardVo.class);
            resp.setResult(vo);
            resp.setFacade(FacadeEnums.OK);
        } catch (AppException e) {
            logger.info("receiveShareReward error", e);
            resp.setFacade(e.getFacade());
        } catch (Exception e) {
            logger.error("receiveShareReward error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 对象转换工具方法
     *
     * @param reward
     * @return
     * @throws Exception
     */
    private ActivityShareRewardVo convertShareReward (ActivityShareReward reward) throws Exception {
        ActivityShareRewardVo vo = DTOUtils.map(reward, ActivityShareRewardVo.class);
        if (reward != null && reward.getActivityShareAuth() != null) {
            vo.setNickName(reward.getActivityShareAuth().getNickName());
            vo.setHeadImgUrl(reward.getActivityShareAuth().getHeadImgUrl());
        }
        return vo;
    }

    // 查是否已授权
    @Override
    public Response<ActivityShareAuthVo> queryShareAuth (String openid) {
        Response<ActivityShareAuthVo> resp = new Response<>(null);
        try {
            ActivityShareAuth activityShareAuth = activityShareManager.queryShareAuth(openid);
            ActivityShareAuthVo vo = DTOUtils.map(activityShareAuth, ActivityShareAuthVo.class);
            resp.setResult(vo);
            resp.setFacade(FacadeEnums.OK);
        } catch (Exception e) {
            logger.error("queryShareAuth error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    // 查看奖励列表
    @Override
    public Response<List<ActivityShareRewardVo>> queryShareReward (Long dailyId) {
        Response<List<ActivityShareRewardVo>> resp = new Response<List<ActivityShareRewardVo>>(null);
        try {
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("dailyId", dailyId);
            List<ActivityShareReward> shareRewards = activityShareManager.queryShareRewards(paras);
            List<ActivityShareRewardVo> list = new ArrayList<>(shareRewards.size());
            if (CollectionUtils.isNotEmpty(shareRewards)) {
                for (ActivityShareReward reward : shareRewards) {
                    list.add(convertShareReward(reward));
                }
            }
            resp.setResult(list);
            resp.setFacade(FacadeEnums.OK);
        } catch (Exception e) {
            logger.error("queryShareReward error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }
}
