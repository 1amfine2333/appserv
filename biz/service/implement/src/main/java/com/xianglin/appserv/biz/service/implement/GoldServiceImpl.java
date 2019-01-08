package com.xianglin.appserv.biz.service.implement;

import com.alibaba.fastjson.JSON;
import com.xianglin.appserv.biz.shared.ActivityManager;
import com.xianglin.appserv.biz.shared.PropExtendManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.app.ActivityV321Service;
import com.xianglin.appserv.common.service.facade.app.GoldService;
import com.xianglin.appserv.common.service.facade.model.AppSessionConstants;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.enums.YesNo;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.core.model.exception.AppException;
import com.xianglin.cif.common.service.facade.GoldcoinService;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.vo.GoldcoinAccountVo;
import com.xianglin.cif.common.service.facade.vo.GoldcoinRecordVo;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Describe :
 * Created by xingyali on 2017/12/11 15:41.
 * Update reason :
 */
@Service("goldService")
@ServiceInterface
public class GoldServiceImpl implements GoldService {
    private static final Logger logger = LoggerFactory.getLogger(PersonalServiceImpl.class);
    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private GoldcoinService goldcoinService;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private PropExtendManager propExtendManager;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GoldService.querySignGold", description = "查询当天是否弹窗")
    public Response<Integer> querySignGold() {
        Response<Integer> resp = ResponseUtils.successResponse(0);
        try {
            Long partyId = sessionHelper.getSessionProp(AppSessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(AppSessionConstants.DEVICE_ID, String.class);
            String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
            queryYesterdaySign(partyId, deviceId);
            AppActivityTask task = AppActivityTask.builder()
                    .partyId(partyId).daily(today).taskCode(Constant.ActivityTaskType.SIGN.name()).alertStatus(YesNo.Y.name())
                    .activityCode(GoldService.ACTIVITY_SIGN).build();
            List<AppActivityTask> list = activityManager.queryActivityTask(task, null);
            if (CollectionUtils.isEmpty(list)) {
                task.setTaskStatus(YesNo.N.name());
                task.setDeviceId(deviceId);
                task.setUseStatus(YesNo.N.name());
                task.setAlertStatus(YesNo.Y.name());
                task.setTaskDailyId(today + task.getPartyId() + task.getActivityCode());
                getContinuitySign(partyId, deviceId);
                task.setTaskResult(String.valueOf(getGold(getContinuitySign(partyId, deviceId) + 1)));
                activityManager.saveUpdateActivityTask(task);
                AppPropExtend prop = propExtendManager.queryAndInit(AppPropExtend.builder().relationId(partyId).type(User.class.getSimpleName()).deviceId(deviceId).ekey(MapVo.USER_SIGN_DAYS).value("0").build());
                resp.setResult(getGold(Integer.valueOf(prop.getValue()) + 1));
            }
        } catch (Exception e) {
            logger.warn("querySignGold error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GoldService.queryRegisterGold", description = "查询注册金币奖励")
    public Response<Integer> queryRegisterGold() {
        Response<Integer> resp = ResponseUtils.successResponse(0);
        try {
            Long partyId = sessionHelper.getSessionProp(AppSessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(AppSessionConstants.DEVICE_ID, String.class);
            List<AppActivityTask> tasks = activityManager.queryActivityTask(AppActivityTask.builder().partyId(partyId).taskCode(Constant.ActivityTaskType.REGISTER.name()).activityCode(GoldService.ACTIVITY_REWARD).alertStatus(YesNo.N.name()).build(), null);
            if (tasks.size() == 1) {
                AppActivityTask task = tasks.get(0);
                task.setAlertStatus(YesNo.Y.name());
                activityManager.saveUpdateActivityTask(task);
                if(StringUtils.isNotEmpty(task.getTaskResult())){
                    resp.setResult(Integer.valueOf(task.getTaskResult()));   
                }
            }
        } catch (Exception e) {
            logger.warn("queryRegisterGold error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GoldService.queryRecord", description = "金币交易记录查询")
    public Response<List<GoldcoinRecordV1>> queryRecord(int startPage, int pageSize) {
        Response<List<GoldcoinRecordV1>> resp = ResponseUtils.successResponse();
        List<GoldcoinRecordV1> list = new ArrayList<>();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            GoldcoinRecordVo req = GoldcoinRecordVo.builder().toPartyId(partyId).build();
            req.setStartPage(startPage);
            req.setPageSize(pageSize);
            com.xianglin.cif.common.service.facade.model.Response<List<GoldcoinRecordVo>> response = goldcoinService.queryRecord(req);
            if (response.getResult() != null) {
                list = DTOUtils.map(response.getResult(), GoldcoinRecordV1.class);
                if (list.size() > 0) {
                    for (GoldcoinRecordV1 goldcoinRecordV1 : list) {
                        if (goldcoinRecordV1.getAmount() > 0) {
                            goldcoinRecordV1.setSymbol(Constant.FilofaxMode.IN.name());
                        } else {
                            goldcoinRecordV1.setSymbol(Constant.FilofaxMode.OUT.name());
                            goldcoinRecordV1.setAmount(Math.abs(goldcoinRecordV1.getAmount()));
                        }
                    }
                }
            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.warn("queryRecord error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GoldService.queryRecordByLastTime", description = "查询上一次的金币兑换记录")
    public Response<GoldcoinRecordV1> queryRecordByLastTime() {
        Response<GoldcoinRecordV1> resp = ResponseUtils.successResponse();
        GoldcoinRecordV1 goldcoinRecordV1 = new GoldcoinRecordV1();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }

            //查上一次的金币和钱
            com.xianglin.cif.common.service.facade.model.Response<List<GoldcoinRecordVo>> response = goldcoinService.queryRecord(GoldcoinRecordVo.builder().toPartyId(partyId).type("EXCHANGE").pageSize(1).startPage(1).build());
            if (response.getResult() != null && response.getResult().size() > 0) {
                int amount = 0;
                if (response.getResult().get(0).getAmount() < 0) {
                    amount = Math.abs(response.getResult().get(0).getAmount());
                    //amount = Integer.valueOf(String.valueOf(response.getResult().get(0).getAmount()).replace("-",""));
                } else {
                    amount = response.getResult().get(0).getAmount();
                }
                goldcoinRecordV1.setAmount(amount);
                goldcoinRecordV1.setBalance(response.getResult().get(0).getBalance());
            } else {
                goldcoinRecordV1.setAmount(0);
                goldcoinRecordV1.setBalance(BigDecimal.ZERO);
            }
            //查当前汇率
            int exchange = Integer.valueOf(SysConfigUtil.getStr("gold_exchange"));
            goldcoinRecordV1.setExchange(exchange);
            //查询晒收入金额
            com.xianglin.cif.common.service.facade.model.Response<GoldcoinAccountVo> res = goldcoinService.queryAccount(partyId);
            BigDecimal bigDecimal = BigDecimal.ZERO;
            if (res.getResult() != null) {
                BigDecimal gold = new BigDecimal(res.getResult().getAmount());
                goldcoinRecordV1.setGold(gold.intValue());
                if (gold.compareTo(BigDecimal.ZERO) == 1) {
                    bigDecimal = gold.divide(new BigDecimal(SysConfigUtil.getStr("gold_exchange"))).setScale(2, BigDecimal.ROUND_DOWN);
                }
                com.xianglin.cif.common.service.facade.model.Response<BigDecimal> iResponse = goldcoinService.queryIncome(partyId);
                if (iResponse.getResult() != null) {
                    bigDecimal = bigDecimal.add(iResponse.getResult());
                }
                goldcoinRecordV1.setInComeAmount(bigDecimal.abs());

            }
            resp.setResult(goldcoinRecordV1);
        } catch (Exception e) {
            logger.warn("queryRecordByLastTime error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GoldService.querySignDay", description = "查询签到天数")
    public Response<List<GoldSignDateVo>> querySignDay() {
        Response<List<GoldSignDateVo>> resp = ResponseUtils.successResponse();
        try {
            List<GoldSignDateVo> voList = new ArrayList<>();
            List<AppActivityTask> list = new ArrayList<>();
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //根据连续签到天数查询联系签到记录
            int day = getContinuitySign(partyId, deviceId);
            if (day > 0) {
                if (day > 7) {
                    day = 7;
                }
                Map<String, Object> paras = new HashMap<>();
                paras.put("partyId", partyId);
                paras.put("taskStatus", YesNo.Y.name());
                paras.put("number", day);
                paras.put("activityCode", GoldService.ACTIVITY_SIGN);
                paras.put("taskCode", Constant.ActivityTaskType.SIGN.name());
                list = activityManager.queryActivityTaskByParas(paras);
                Collections.reverse(list);
                voList = getGoldSignDateVoList(list);
            } else {
                voList = getGoldSignDateVoList(list);
            }
            resp.setResult(voList);
        } catch (Exception e) {
            logger.warn("querySignDay error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private List<GoldSignDateVo> getGoldSignDateVoList(List<AppActivityTask> list) {
        List<GoldSignDateVo> voList = new ArrayList<>();
        //Collections.reverse(list);
        int day = 0;
        if (list.size() > 0) {
            for (AppActivityTask appActivityTask : list) {
                day = day + 1;
                GoldSignDateVo goldSignDateVo = new GoldSignDateVo();
                if (appActivityTask.getUseStatus().equals(YesNo.Y)) {
                    goldSignDateVo.setDay("已领");
                } else {
                    goldSignDateVo.setDay(day + "天");
                }
                goldSignDateVo.setTime(appActivityTask.getDaily());
                if (StringUtils.isNotEmpty(appActivityTask.getTaskResult())) {
                    goldSignDateVo.setAmount(Integer.valueOf(appActivityTask.getTaskResult()));
                }
                if (StringUtils.isNotEmpty(appActivityTask.getUseStatus())) {
                    goldSignDateVo.setGetGole(appActivityTask.getUseStatus());
                }
                if (StringUtils.isNotEmpty(appActivityTask.getTaskStatus())) {
                    goldSignDateVo.setSignType(appActivityTask.getTaskStatus());
                }
                voList.add(goldSignDateVo);
            }
        }
        if (voList.size() < 7) {
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            Date startDay = null;
            if (list.size() > 0) {
                startDay = DateUtils.parse("yyyyMMdd", list.get(voList.size() - 1).getDaily());
                startDate.setTime(startDay);
                startDate.add(Calendar.DATE, 1);
            }
            endDate.add(Calendar.DATE, 7 - voList.size());
            for (; startDate.before(endDate); ) {
                day = day + 1;
                String time = DateUtils.formatDate(startDate.getTime(), "yyyyMMdd");
                if (voList.size() < 7) {
                    voList.add(GoldSignDateVo.builder().amount(getGold(voList.size() + 1)).day(day + "天").getGole("N").signType("N").time(time).build());
                }
                startDate.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
        return voList;
    }

    private int getGold(int count) {
        if (count > 7) {
            count = 7;
        }
        int amount = 0;
        String signReward = SysConfigUtil.getStr("gold_sign_reward");
        if (StringUtils.isNotEmpty(signReward)) {
            JSONArray jsonArray = JSONArray.fromObject(signReward);
            List<Map<String, Object>> list2 = (List<Map<String, Object>>) JSONArray.toCollection(jsonArray, Map.class);
            for (int i = 0; i < list2.size(); i++) {
                if (Integer.valueOf(list2.get(i).get("key").toString()) <= (count)) {
                    amount = Integer.valueOf(list2.get(i).get("value").toString());
                } else {
                    break;
                }
            }
        }
        return amount;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GoldService.sign", description = "签到")
    public Response<Boolean> sign() {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            queryYesterdaySign(partyId, deviceId);
            String today = DateUtils.formatDate(new Date(), "yyyyMMdd");
            //查询当天是否签到
            List<AppActivityTask> appActivityTasks = activityManager.queryActivityTask(AppActivityTask.builder().partyId(partyId).daily(today).taskCode(Constant.ActivityTaskType.SIGN.name()).useStatus(YesNo.Y.name()).activityCode(GoldService.ACTIVITY_SIGN).taskStatus(YesNo.Y.name())
                    .build(), null);
            if (appActivityTasks.size() > 0) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400045);
                return resp;
            } else {
                Response<GoldSignDayVo> response = this.queryContinuitySign();
                int count = 1;
                if (response.getResult() != null) {
                    count = count + response.getResult().getDay();
                }
                int amount = getGold(count);
                User user = userManager.queryUser(partyId);

                AppActivityTask task = AppActivityTask.builder()
                        .partyId(partyId).daily(today).taskCode(Constant.ActivityTaskType.SIGN.name()).useStatus(YesNo.N.name())
                        .activityCode(GoldService.ACTIVITY_SIGN).build();
                List<AppActivityTask> list = activityManager.queryActivityTask(task, null);
                //查询结果应该只有一条
                if (CollectionUtils.isEmpty(list)) {
                    task = AppActivityTask.builder().partyId(partyId).deviceId(deviceId).mobilePhone(user.getLoginName()).activityCode(ACTIVITY_SIGN).taskStatus(YesNo.Y.name()).taskCode(Constant.ActivityTaskType.SIGN.name())
                            .daily(today).taskDailyId(today + task.getPartyId() + task.getActivityCode()).taskResult(String.valueOf(amount)).useStatus(YesNo.Y.name()).alertStatus(YesNo.Y.name()).taskName(YesNo.Y.name()).taskName(Constant.ActivityTaskType.SIGN.desc).build();
                    task = activityManager.saveUpdateActivityTask(task);
                } else {
                    task.setId(list.get(0).getId());
                    task.setTaskStatus(YesNo.Y.name());
                    task.setUseStatus(YesNo.Y.name());
                    task.setAlertStatus(YesNo.Y.name());
                    task = activityManager.saveUpdateActivityTask(task);
                }
                AppPropExtend prop = propExtendManager.queryAndInit(AppPropExtend.builder().relationId(partyId).type(User.class.getSimpleName()).deviceId(deviceId).ekey(MapVo.USER_SIGN_DAYS).value("0").build());
                prop.setValue(String.valueOf(Integer.valueOf(prop.getValue()) + 1));
                propExtendManager.update(prop);
                if (count > 7) {
                    count = 7;
                }
                activityManager.reward(partyId, task.getId(), String.valueOf(count));
                resp.setResult(true);
            }
        } catch (Exception e) {
            logger.warn("sign error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GoldService.queryContinuitySign", description = "查询连续签到")
    public Response<GoldSignDayVo> queryContinuitySign() {
        Response<GoldSignDayVo> resp = ResponseUtils.successResponse();
        try {
            GoldSignDayVo vo = new GoldSignDayVo();
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(AppSessionConstants.DEVICE_ID, String.class);
            vo.setDay(getContinuitySign(partyId, deviceId));
            //查询明天签到的金币
            vo.setAmount(getGold(getContinuitySign(partyId, deviceId) + 1));
            String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
            AppActivityTask task = AppActivityTask.builder()
                    .partyId(partyId).daily(today).taskCode(Constant.ActivityTaskType.SIGN.name())
                    .activityCode(GoldService.ACTIVITY_SIGN).taskStatus("Y").useStatus("Y").build();
            List<AppActivityTask> list = activityManager.queryActivityTask(task, null);
            if (list.size() > 0) {
                vo.setIsSign(true);
            } else {
                vo.setIsSign(false);
            }
            User user = userManager.queryUser(partyId);
            UserVo userVo = new UserVo();
            userVo.setTrueName(user.getShowName());
            userVo.setLoginName(user.getLoginName());
            userVo.setShowName(user.getShowName());
            userVo.setNikerName(user.getNikerName());
            userVo.setPartyId(user.getPartyId());
            if (StringUtils.isNotEmpty(user.getHeadImg())) {
                userVo.setHeadImg(user.getHeadImg());
            } else {
                userVo.setHeadImg(SysConfigUtil.getStr("default_user_headimg"));
            }
            vo.setUserVo(userVo);
            resp.setResult(vo);
        } catch (Exception e) {
            logger.warn("queryContinuitySign error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 查连续签到天数
     *
     * @param partyId
     * @param deviceId
     * @return
     */
    private Integer getContinuitySign(Long partyId, String deviceId) {
        int amount = 0;
        if (partyId != null) {
            AppPropExtend prop = propExtendManager.queryAndInit(AppPropExtend.builder().relationId(partyId).type(User.class.getSimpleName()).deviceId(deviceId).ekey(MapVo.USER_SIGN_DAYS).value("0").build());
            amount = Integer.valueOf(prop.getValue());
        }

        return amount;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GoldService.getGoldcoin", description = "领取金币")
    public Response<Boolean> getGoldcoin(String date) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            AppActivityTask task = AppActivityTask.builder()
                    .partyId(partyId).daily(date).taskCode(Constant.ActivityTaskType.SIGN.name())
                    .activityCode(GoldService.ACTIVITY_SIGN).build();
            List<AppActivityTask> list = activityManager.queryActivityTask(task, null);
            if (list.size() > 0) {
                task = list.get(0);
            }
            activityManager.reward(partyId, task.getId(), null);
            resp.setResult(true);
        } catch (Exception e) {
            logger.warn("queryContinuitySign error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GoldService.querySignAndPunish", description = "查询日常奖励与惩罚")
    public Response<List<ActivityRewardVo>> querySignAndPunish() {
        Response<List<ActivityRewardVo>> resp = ResponseUtils.successResponse();
        try {
            Map<String, Object> map = new HashMap<>();
            //map.put("activityCodes",ACTIVITY_REWARD+","+ACTIVITY_PUBNISH);
            map.put("expectActivityCode", ACTIVITY_SIGN);
            map.put("rewardType", "GOLDCOIN");
            map.put("showStatus", YesNo.Y.name());
            map.put("orderBy", "PRIORITY_LEVEL asc");
            List<AppActivityReward> list = activityManager.queryActivityReward(map);
            List<ActivityRewardVo> voList = DTOUtils.map(list, ActivityRewardVo.class);
            if (voList.size() > 0) {
                for (ActivityRewardVo activityRewardVo : voList) {
                    if (activityRewardVo.getStartAmt().compareTo(BigDecimal.ZERO) == 1) {
                        activityRewardVo.setActivityCode(GoldService.ACTIVITY_REWARD);
                    }
                }
            }
            resp.setResult(voList);
        } catch (Exception e) {
            logger.warn("querySignAndPunish error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GoldService.award", description = "发奖励")
    public Response<Integer> award(String type) {
        Response<Integer> resp = ResponseUtils.successResponse(0);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            String md5 = MD5.md5(partyId + JSON.toJSONString(type));
            if (redisUtil.isRepeat(md5, 1)) {
                resp.setFacade(FacadeEnums.ERROR_REPEAT);
                return resp;
            }
            if (StringUtils.isNotEmpty(type)) {
                //根据type查询活动code
                Map<String, Object> paras = new HashMap<>();
                paras.put("rewardCategary", type);
                List<AppActivityReward> rewards = activityManager.queryActivityReward(paras);
                if (rewards.size() > 0) {
                    String code = rewards.get(0).getActivityCode();
                    User user = userManager.queryUser(partyId);
                    String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
                    AppActivityTask task = AppActivityTask.builder().partyId(partyId).deviceId(deviceId).mobilePhone(user.getLoginName()).activityCode(code).taskStatus(YesNo.Y.name()).taskCode(type)
                            .daily(today).taskDailyId(today + System.currentTimeMillis()).useStatus(YesNo.Y.name()).taskName(YesNo.Y.name()).build();
                    task = activityManager.saveUpdateActivityTask(task);
                    List<AppTransaction> list = activityManager.reward(partyId, task.getId(), null);
                    if (list.size() == 1) {
                        int amount = list.get(0).getAmount().intValue();
                        resp.setResult(amount);
                    }
                }
            }
        } catch (AppException e) {
            logger.warn("award error", e);
        } catch (Exception e) {
            logger.warn("award error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GoldService.awardByPartyId", description = "发奖励")
    public Response<Integer> awardByPartyId(String type, Long partyId) {
        Response<Integer> resp = ResponseUtils.successResponse(0);
        try {
            if (StringUtils.isNotEmpty(type) && partyId != null) {
                //根据type查询活动code
                Map<String, Object> paras = new HashMap<>();
                paras.put("REWARD_CATEGARY", type);
                List<AppActivityReward> rewards = activityManager.queryActivityReward(paras);
                if (rewards.size() > 0) {
                    String code = rewards.get(0).getActivityCode();
                    User user = userManager.queryUser(partyId);
                    String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
                    AppActivityTask task = AppActivityTask.builder().partyId(partyId).mobilePhone(user.getLoginName()).activityCode(code).taskStatus(YesNo.Y.name()).taskCode(type)
                            .daily(today).taskDailyId(today + System.currentTimeMillis()).useStatus(YesNo.Y.name()).taskName(YesNo.Y.name()).build();
                    task = activityManager.saveUpdateActivityTask(task);
                    List<AppTransaction> list = activityManager.reward(partyId, task.getId(), null);
                    if (list.size() == 1) {
                        int amount = list.get(0).getAmount().intValue();
                        resp.setResult(amount);
                    }
                }
            }
        } catch (AppException e) {
            logger.warn("awardByPartyId error", e);
        } catch (Exception e) {
            logger.warn("awardByPartyId error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GoldService.queryRewardAmountByCategary", description = "查询奖励金额")
    public Response<Integer> queryRewardAmountByCategary(String categary) {
        Response<Integer> resp = ResponseUtils.successResponse();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("activityCode", ACTIVITY_REWARD);
            map.put("rewardCategary", categary);
            List<AppActivityReward> list = activityManager.queryActivityReward(map);
            int amount = 0;
            if (list.size() > 0) {
                amount = list.get(0).getStartAmt().intValue();
            }
            resp.setResult(amount);
        } catch (Exception e) {
            logger.warn("querySignAndPunish error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.GoldService.doExchange", description = "执行批量兑换")
    public Response<Boolean> doExchange(String batchId) {
        Response<Boolean> resp = ResponseUtils.successResponse(true);
        try {
            boolean flag = redisUtil.get(GoldService.DO_EXCHANGE_TAG) == null;
            if (flag) {
                activityManager.batchExchangeGold(batchId);
            } else {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400046);
            }
        } catch (Exception e) {
            logger.warn("doExchange error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 初始化连续签到天数
     *
     * @return
     */
    private void queryYesterdaySign(Long partyId, String deviceId) {
        String yesterday = DateFormatUtils.format(DateUtils.addDate(new Date(), -1), "yyyyMMdd");
        //先查昨天是否签到
        int signDay = 0;
        Boolean yesterdaySign = querySignDay(partyId, yesterday);
        if (!yesterdaySign) { //昨天没有签到
            //查今天是否签到
            String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
            Boolean todaySign = querySignDay(partyId, today);
            if (todaySign) {
                signDay = signDay + 1;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("relationId", partyId);
            map.put("ekey", MapVo.USER_SIGN_DAYS);
            map.put("type", User.class.getSimpleName());
            List<AppPropExtend> appPropExtends = propExtendManager.queryChannel(map);
            if (appPropExtends.size() > 0) {
                propExtendManager.update(AppPropExtend.builder().relationId(partyId).type(User.class.getSimpleName()).id(appPropExtends.get(0).getId()).deviceId(deviceId).ekey(MapVo.USER_SIGN_DAYS).value(String.valueOf(signDay)).build());
            } else {
                propExtendManager.insertExceptPropExtend(AppPropExtend.builder().relationId(partyId).type(User.class.getSimpleName()).deviceId(deviceId).ekey(MapVo.USER_SIGN_DAYS).value(String.valueOf(signDay)).build());
            }
        }
    }

    //查询某天是否签到
    private Boolean querySignDay(Long partyId, String today) {
        Boolean flag = false;
        AppActivityTask task = AppActivityTask.builder().partyId(partyId).daily(today).taskCode(Constant.ActivityTaskType.SIGN.name()).taskStatus("Y").useStatus("Y").activityCode(GoldService.ACTIVITY_SIGN).build();
        List<AppActivityTask> list = activityManager.queryActivityTask(task, null);
        if (list.size() > 0) {
            flag = true;
        }
        return flag;
    }


}
