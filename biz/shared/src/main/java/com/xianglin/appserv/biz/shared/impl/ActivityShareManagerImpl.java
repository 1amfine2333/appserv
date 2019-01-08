package com.xianglin.appserv.biz.shared.impl;

/**
 * Created by wanglei on 2017/3/14.
 */

import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.biz.shared.ActivityShareManager;
import com.xianglin.appserv.common.dal.daointerface.ActivityShareAuthDAO;
import com.xianglin.appserv.common.dal.daointerface.ActivityShareDailyDAO;
import com.xianglin.appserv.common.dal.daointerface.ActivityShareRewardDAO;
import com.xianglin.appserv.common.dal.daointerface.UserDAO;
import com.xianglin.appserv.common.dal.dataobject.ActivityShareAuth;
import com.xianglin.appserv.common.dal.dataobject.ActivityShareDaily;
import com.xianglin.appserv.common.dal.dataobject.ActivityShareReward;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.integration.cif.TransferServiceClient;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.core.model.exception.AppException;
import com.xianglin.te.common.service.facade.enums.Constants;
import com.xianglin.te.common.service.facade.req.AccountBalChangeReq;
import com.xianglin.te.common.service.facade.resp.AccountBalChangeResp;
import com.xianglin.xlnodecore.common.service.facade.AccountNodeManagerService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 红包雨活动管理实现
 *
 * @author
 * @create 2017-03-14 16:38
 **/
@Service
public class ActivityShareManagerImpl implements ActivityShareManager {

    private static final Logger logger = LoggerFactory.getLogger(ActivityShareManagerImpl.class);

    @Autowired
    private ActivityShareDailyDAO activityShareDailyDAO;

    @Autowired
    private ActivityShareRewardDAO activityShareRewardDAO;

    @Autowired
    private ActivityShareAuthDAO activityShareAuthDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AccountNodeManagerService accountNodeManagerService;

    @Autowired
    private TransferServiceClient transferServiceClient;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ActivityShareDaily queryInitShareDaily(Long partyId) {
        ActivityShareDaily daily = null;
        try {
            String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
            String key = today+partyId;
            if(redisUtil.isRepeat(key,24*3600)){
                Map<String, Object> paras = DTOUtils.queryMap();
                paras.put("daily", today);
                paras.put("partyId", partyId);
                List<ActivityShareDaily> list = activityShareDailyDAO.selectList(paras);
                if (CollectionUtils.isNotEmpty(list)) {
                    daily = list.get(0);
                }
            }else {
                daily = new ActivityShareDaily();
                daily.setPartyId(partyId);
                daily.setDaily(today);
                daily.setTaskStatus(Constant.YESNO.NO.code);
                daily.setTipAlertStatus(Constant.YESNO.NO.code);
                daily.setProgessAlertStatus(Constant.YESNO.NO.code);
                daily.setShareStatus(Constant.YESNO.NO.code);
                activityShareDailyDAO.insert(daily);
            }
        } catch (Exception e) {
            logger.warn("queryInitShareDaily", e);
        }
        return daily;
    }

    @Override
    public void updateShareDaily(ActivityShareDaily daily) {
        activityShareDailyDAO.updateByPrimaryKeySelective(daily);
    }

    @Override
    public ActivityShareAuth addUpdateShareAuth(ActivityShareAuth auth) {
        // 根据openId查询绑定关系十分存在，存在则更新
        Map<String, Object> paras = DTOUtils.queryMap();
        paras.put("openId", auth.getOpenId());
        List<ActivityShareAuth> list = activityShareAuthDAO.selectList(paras);
        if (CollectionUtils.isNotEmpty(list)) {
            auth.setId(list.get(0).getId());
            activityShareAuthDAO.updateByPrimaryKeySelective(auth);
        } else {
            activityShareAuthDAO.insert(auth);
        }
        return auth;
    }

    @Override
    public ActivityShareAuth queryShareAuth(String openid) {
        ActivityShareAuth activityShareAuth = new ActivityShareAuth();
        Map<String, Object> paras = DTOUtils.queryMap();
        paras.put("openId", openid);
        List<ActivityShareAuth> selectList = activityShareAuthDAO.selectList(paras);
        if (CollectionUtils.isNotEmpty(selectList)) {
            activityShareAuth = selectList.get(0);
        }
        return activityShareAuth;
    }

    @Override
    public ActivityShareReward shareeward(ActivityShareReward reward) throws AppException {
        /*
         * 1，判断是否已经领奖励 2，判断当日领奖数是否已到上限 3，判断该分享下是否还有奖励 4，生成奖励信息对象 5，分配奖励
         * 判断用户类型，app用户，根据类型发放奖励 非app用户，新站长，生成新站长奖励 普通未登录用户，生成奖励 6，针对已登录用户，发放奖励
         */
        ActivityShareReward shareReward = null;
        String today = DateFormatUtils.format(new Date(), "yyyyMMdd");

        ActivityShareDaily daily = activityShareDailyDAO.selectByPrimaryKey(reward.getDailyId());
        if (daily == null || StringUtils.equals(daily.getTaskStatus(), Constant.YESNO.NO.code)) {
            throw new AppException(FacadeEnums.ERROR_CHAT_400007, "该分享已经失效或被删除，请重新再试");
        }
        Map<String, Object> paras = DTOUtils.queryMap();
        paras.put("mobilePhone", reward.getMobilePhone());
        List<ActivityShareAuth> auths = activityShareAuthDAO.selectList(paras);
        if (CollectionUtils.isEmpty(auths)) {
            throw new AppException(FacadeEnums.ERROR_CHAT_400009, "请先授权");
        }

        paras = DTOUtils.queryMap();
        paras.put("mobilePhone", reward.getMobilePhone());
        paras.put("dailyId", reward.getDailyId());
        List<ActivityShareReward> list = activityShareRewardDAO.selectList(paras);
        if (CollectionUtils.isNotEmpty(list)) {
            shareReward = list.get(0);
            shareReward.setComments("这是一个已经拆过的红包~");
            return shareReward;
        }

        paras.remove("dailyId");
        paras.put("daily", today);
        int count = activityShareRewardDAO.selectCount(paras);
        if (count >= SysConfigUtil.getInt("activity.share.limit.user")) {
            throw new AppException(FacadeEnums.ERROR_CHAT_400007, "今天已经拆了3个红包啦，明天再来吧~");
        }

        paras.clear();
        paras.put("dailyId", reward.getDailyId());
        count = activityShareRewardDAO.selectCount(paras);
        if (count >= SysConfigUtil.getInt("activity.share.limit.daily")) {
            throw new AppException(FacadeEnums.ERROR_CHAT_400008);
        }

        shareReward = new ActivityShareReward();
        shareReward.setDailyId(reward.getDailyId());
        shareReward.setMobilePhone(reward.getMobilePhone());
        shareReward.setDaily(today);

        // 查询用户类型
        String userTYpe = "user";// 1,
        User user = User.builder().build();
        user = userDAO.selectByMobilePhone(reward.getMobilePhone());
        userTYpe = user.getUserType();
        if (StringUtils.equals(userTYpe, Constant.UserType.user.name())) {
            List<Long> ids = accountNodeManagerService.getNodeManagerMoreInfoByMobilePhone(reward.getMobilePhone());
            if (CollectionUtils.isNotEmpty(ids)) {
                userTYpe = "newNodeManager";
            }
        }
        shareReward.setPartyId(user.getPartyId());
        initReward(shareReward, userTYpe);
        activityShareRewardDAO.insert(shareReward);

        reward(reward.getMobilePhone());// 异步发放奖励
        return shareReward;
    }

    @Override
    public List<ActivityShareReward> queryShareRewards(Map<String, Object> paras) {
        return activityShareRewardDAO.selectList(paras);
    }

    /**
     * 计算奖励类型
     *
     * @param reward
     * @param userType
     * @return
     */
    private ActivityShareReward initReward(ActivityShareReward reward, String userType) throws AppException {
        try {
            String ward = SysConfigUtil.getStr("activity.share.rw." + userType);
            JSONObject jsonObj = JSONObject.parseObject(ward);
            int start = SysConfigUtil.getInt("activity.share.rw.start");
            int end = SysConfigUtil.getInt("activity.share.rw.end");
            int key = RandomUtils.nextInt(start, end + 1);
            JSONObject rewardObj = jsonObj.getJSONObject(key + "");
            reward.setRewardType(rewardObj.getString("type"));
            if (rewardObj.containsKey("rewardAmt")) {
                reward.setRewardAmt(new BigDecimal(rewardObj.getInteger("rewardAmt")));
            } else {
                reward.setRewardAmt(new BigDecimal(RandomUtils.nextDouble(rewardObj.getDouble("min"), rewardObj.getDouble("max"))).setScale(2, RoundingMode.FLOOR));
            }
            return reward;
        } catch (Exception e) {
            logger.error("queryReward", e);
            throw new AppException(FacadeEnums.ERROR_CHAT_400009);
        }
    }

    /**
     * 发放奖励
     *
     * @param mobilePhone
     */
    @Async("asyncExecutor")
    @Override
    public void reward(String mobilePhone) {
        // 2017/3/23 查询是否有未收取的奖励，按照奖励类型领取奖励，更新奖励状态
        try {
            logger.info("begin to reward mobilePhone:{}", mobilePhone);
            User user = User.builder().build();
            user = userDAO.selectByMobilePhone(mobilePhone);
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("mobilePhone", mobilePhone);
            paras.put("rewardStatus", Constant.YESNO.NO.code);
            List<ActivityShareReward> list = activityShareRewardDAO.selectList(paras);
            for (ActivityShareReward reward : list) {
                reward.setPartyId(user.getPartyId());
                dealReward(reward);
            }
        } catch (Exception e) {
            logger.error("reward", e);
        }
    }

    private void dealReward(ActivityShareReward reward) {
        try {
            logger.info("deal reward:{}", ToStringBuilder.reflectionToString(reward));
            if (StringUtils.equals(reward.getRewardType(), Constant.ActivityShareRewardType.CASH.name())) {
                // 调用te接口给用户发放奖励
                AccountBalChangeReq accountReq = new AccountBalChangeReq();
                String requestNum = UUID.randomUUID().toString();
                accountReq.setAccountBalType(Constants.AccountBalTypeEnums.ACCOUNT_BAL_PRINCIPAL.code);
                accountReq.setOrderRequest(requestNum);
                accountReq.setPartyId(reward.getPartyId() + "");
                accountReq.setOrderNumber(createOrderNumber(reward.getId() + ""));
                accountReq.setOrderSubNumber(createOrderNumber(reward.getId() + reward.getDailyId() + ""));
                accountReq.setTransactionAmount(NumberUtil.toString(reward.getRewardAmt()));
                accountReq.setUserType(Constants.AccountBizTypeEnums.VILLAGER_TYPE.code);
                // 暂时使用红包充值类型
                accountReq.setTransactionType(Constants.TransactionTypeEnums.TRANS_TYPE_100035.code);
                accountReq.setMemo("红包");
                accountReq.setTradeCategory(Constants.TradeTypeEnums.RECHARGE.name());
                accountReq.setProductCode(Constants.ProductCode.APP.code);
                accountReq.setOrderTime(DateUtils.getDateStr(DateUtils.getNow()));
                accountReq.setRequestTime(String.valueOf(System.currentTimeMillis()));
                accountReq.setAccountBalType(Constants.AccountBalTypeEnums.ACCOUNT_BAL_PRINCIPAL.code);
                accountReq.setTransactionAmountSign(Constants.TransactionAmountSignEnums.POSITIVE.code);// 充值or消费
                accountReq.setFee("0");
                accountReq.setFeeType(Constants.AccountFeeType.FEE_IN.code);
                accountReq.setFeePayer(Constants.AccountFeeFromEnums.FEE_COMPANY.code);
                accountReq.setTransactionSource("app");
                accountReq.setCreator(reward.getPartyId() + "");
                com.xianglin.te.common.service.facade.resp.Response<AccountBalChangeResp> accountResp = transferServiceClient.accountBalChange(accountReq);
                logger.info("accountResp {}:", ToStringBuilder.reflectionToString(accountResp));
                if (FacadeEnums.OK.code == accountResp.getCode()) {
                    reward.setRewardStatus(Constant.YESNO.YES.code);
                    activityShareRewardDAO.updateByPrimaryKeySelective(reward);
                }
            } else {// 话费券和优惠券
                Map<String, String> param = new HashMap<>();
                String app_key = "xianglin".concat(DateUtils.formatDate(DateUtils.getNow(), DateUtils.DATE_TPT_TWO)).concat("@#_$&");
                param.put("party_id", reward.getPartyId() + "");
                if (StringUtils.equals(reward.getRewardType(), Constant.ActivityShareRewardType.CALL_BILL.name())) {
                    param.put("type", "1");// 话费券
                } else {
                    param.put("type", "2");// 优惠券
                }
                param.put("amount", NumberUtil.toString(reward.getRewardAmt()));
                app_key = app_key.concat(SHAUtil.getSortString(param));
                param.put("app_key", app_key);
                param.put("sign", SHAUtil.shaEncode(MD5.encode(app_key)));
                String json = HttpUtils.executePost(PropertiesUtil.getProperty("luckwheel.coupon.url"), param, 2000);
                logger.info("reward:{}", json);
                reward.setRemark(json);
                JSONObject object = JSONObject.parseObject(json);
                if ("1".equals(object.getString("error_code"))) {
                    reward.setRelationId(object.getJSONObject("data").getString("memc_code"));
                    reward.setRewardStatus(Constant.YESNO.YES.code);
                }
                activityShareRewardDAO.updateByPrimaryKeySelective(reward);
            }
        } catch (Exception e) {
            logger.error("reward", e);
        }
    }

    /**
     * 创建订单号
     *
     * @param arg
     * @return
     */
    private String createOrderNumber(String arg) {
        StringBuffer sb = new StringBuffer();
        sb.append(DateUtils.formatDate(new Date(), DateUtils.DATE_TPT_TWO)).append(System.currentTimeMillis()).append(arg);
        if (sb.length() > 24) {
            return sb.substring(0, 24).toString();
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        int count = 0;
        for (int i = 0; i < 100; i++) {
            int re = RandomUtils.nextInt(1, 11);
            System.out.println(re);
            if (re == 1) {
                count++;
            }
        }
        System.out.println("0 数为:" + count);

        System.out.println(new BigDecimal("2.23211241312").setScale(2, RoundingMode.FLOOR));
    }

}
