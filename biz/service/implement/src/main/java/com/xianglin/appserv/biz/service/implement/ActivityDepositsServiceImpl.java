package com.xianglin.appserv.biz.service.implement;

import com.xianglin.appserv.biz.shared.ActivityDepositsManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.ActivityDeposits;
import com.xianglin.appserv.common.service.facade.app.ActivityDepositsService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityDepositsVo;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.common.util.NumberUtil;
import com.xianglin.appserv.common.util.SysConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wanglei on 2017/1/6.
 */
@Service("activityDepositsService")
public class ActivityDepositsServiceImpl implements ActivityDepositsService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityDepositsServiceImpl.class);

    @Autowired
    private ActivityDepositsManager activityDepositsManager;

    @Override
    public Response<ActivityDepositsVo> queryDeposits(Long parytId) {
        Response<ActivityDepositsVo> response = ResponseUtils.successResponse();
        try {
            //判断活动是否已经过期
            ActivityDepositsVo vo = null;
            Date endDate = DateUtils.parse(DateUtils.DATETIME_FMT, SysConfigUtil.getStr("activity.deposits.endTime"));
            if ((new Date()).compareTo(endDate) > 0) {
                response = ResponseUtils.toResponse(ResponseEnum.APPSERV_ERROR_002);//活动已经过期
            } else {
                ActivityDeposits deposits = activityDepositsManager.queryInitDeposits(parytId);
                if (deposits != null) {
                    vo = encapDeposits(deposits,endDate);
                    response.setResult(vo);
                } else {
                    response = ResponseUtils.toResponse(ResponseEnum.APPSERV_ERROR_002);//活动已经过期
                }
            }
        } catch (Exception e) {
            logger.error("inviteInfo error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    public Response<ActivityDepositsVo> rewardDeposits(Long parytId) {
        Response<ActivityDepositsVo> response = ResponseUtils.successResponse();
        try {
            //判断活动是否已经过期
            ActivityDepositsVo vo = null;
            Date endDate = DateUtils.parse(DateUtils.DATETIME_FMT, SysConfigUtil.getStr("activity.deposits.endTime"));
            if ((new Date()).compareTo(endDate) > 0) {
                response = ResponseUtils.toResponse(ResponseEnum.APPSERV_ERROR_002);//活动已经过期
            } else {
                ActivityDeposits deposits = activityDepositsManager.rewardDeposits(parytId);
                if (deposits != null) {
                    vo = encapDeposits(deposits,endDate);
                    response.setResult(vo);
                } else {
                    response = ResponseUtils.toResponse(ResponseEnum.APPSERV_ERROR_002);//活动已经过期
                }
            }
        } catch (Exception e) {
            logger.error("inviteInfo error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }


    /**封装返回信息
     * @param deposits
     * @param endDate
     * @return
     */
    private ActivityDepositsVo encapDeposits(ActivityDeposits deposits,Date endDate) {
        ActivityDepositsVo vo = new ActivityDepositsVo();
        vo.setCurrentAchieve(NumberUtil.formatToString(deposits.getCurrentAchieve(),0));
        vo.setGoalAchieve(NumberUtil.formatToString(deposits.getGoalAchieve(),0));
        vo.setId(deposits.getId());
        vo.setPartyId(deposits.getPartyId());
        if (deposits.getGoalAchieve().compareTo(deposits.getCurrentAchieve()) >= 0) {
            vo.setRemainAchieve(NumberUtil.formatToString(deposits.getGoalAchieve().subtract(deposits.getCurrentAchieve()),0));
        } else {
            vo.setRemainAchieve("0");
        }
        if(StringUtils.equals(deposits.getRewardSign(), Constant.ActivityDepositsStatus.F.name())){
            vo.setProgress(((deposits.getCurrentAchieve().subtract(deposits.getStartAchieve()).divide(deposits.getGoalAchieve().subtract(deposits.getStartAchieve()),3))).multiply(new BigDecimal("100")).intValue());
        }else {
            vo.setProgress(100);
        }
        if(vo.getProgress() < 0){
            vo.setProgress(0);
        }else if(vo.getProgress() > 100){
            vo.setProgress(100);
        }
        vo.setRewardSign(deposits.getRewardSign());
        vo.setRewardResult(deposits.getRewardResult());
        vo.setEndDate(endDate);
        vo.setEndTime(vo.getEndDate().getTime());
        return vo;
    }
}

