/**
 *
 */
package com.xianglin.appserv.biz.shared.listener;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.xianglin.appserv.biz.shared.ActivityManager;
import com.xianglin.appserv.biz.shared.MessageManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.common.dal.daointerface.*;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.app.ActivityV321Service;
import com.xianglin.appserv.common.service.facade.app.GoldService;
import com.xianglin.appserv.common.service.facade.app.PersonalService;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.ActivityInviteStatus;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.NativeActivity;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.UserType;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.YESNO;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.facade.model.vo.PointRushVo;
import com.xianglin.appserv.common.service.facade.model.vo.UserVo;
import com.xianglin.appserv.common.service.integration.cif.TransferServiceClient;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.core.model.exception.AppException;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.model.CustomersDTO;
import com.xianglin.te.common.service.facade.enums.Constants.*;
import com.xianglin.te.common.service.facade.req.AccountBalChangeReq;
import com.xianglin.te.common.service.facade.resp.AccountBalChangeResp;
import jodd.util.ArraysUtil;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.assertj.core.util.ArrayWrapperList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 邀请用户上线提醒
 *
 * @author wanglei 2016年8月16日上午10:53:27
 */
@Component
public class ActivityInviteListner {

    private static final Logger logger = LoggerFactory.getLogger(ActivityInviteListner.class);

    @Autowired
    private ActivityInviteDAO activityInviteDAO;

    @Autowired
    private ActivityInviteDetailDAO activityInviteDetailDAO;

    @Autowired
    private TransferServiceClient transferServiceClient;

    @Autowired
    private AppActivityTaskDAO appActivityTaskDAO;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AppActivityRewardDAO appActivityRewardDAO;

    @Autowired
    private UserManager userManager;

    @Autowired
    private CustomersInfoService customersInfoService;

    @Autowired
    private GoldService goldService;

    private Set<Long> partyIds = new HashSet<>();

    /**
     * 发放v3.2.1活动奖励
     *
     * @param user
     */
    @Async
    @Deprecated
    public void rewardV321(User user) {
        /*
        1，发放新手奖励兵发送消息
		2，发放推荐人奖励兵发送消息
		3，发放推荐人累计奖励
		 */
        try {
            List<ActivityInviteDetail> details = activityInviteDetailDAO.select(ActivityInviteDetail.builder()
                    .loginName(user.getLoginName()).status(ActivityInviteStatus.I.name()).build());
            //int deviceCount = activityInviteDetailDAO.selectCount(ActivityInviteDetail.builder().activityCode(GoldService.ACTIVITY_REWARD).deviceId(user.getDeviceId()).build());
            logger.info("rewardV321 userId:{},deviceCount:{}", user);
            if (CollectionUtils.isNotEmpty(details)) {
                for (int i = 0; i < details.size(); i++) {
                    if (i == 0) {
                        ActivityInviteDetail detail = details.get(0);
                        detail.setPartyId(user.getPartyId());
                        detail.setDeviceId(user.getDeviceId());
                        detail.setComments("新用户登陆");
                        detail.setStatus(ActivityInviteStatus.S.name());
                        detail.setActivityCode(details.get(0).getActivityCode());
                        detail.setCommentName(user.getTrueName());
                        activityInviteDetailDAO.updateByPrimaryKeySelective(detail);//更新状态为成功  
                    } else {
                        ActivityInviteDetail detail = details.get(i);
                        detail.setRecPartyId(null);
                        detail.setPartyId(user.getPartyId());
                        detail.setDeviceId(user.getDeviceId());
                        detail.setComments("已被邀请");
                        detail.setStatus(ActivityInviteStatus.U.name());
                        detail.setActivityCode(details.get(i).getActivityCode());
                        detail.setCommentName(user.getTrueName());
                        activityInviteDetailDAO.updateByPrimaryKeySelective(detail);//更新状态为成功
                    }
                }
                CustomersDTO customersDTO = new CustomersDTO();
                customersDTO.setPartyId(user.getPartyId());
                if (details.get(0).getActivityCode().equals(GoldService.ACTIVITY_REWARD)) {   //活动106
                    rewardInviteV321(user, Constant.ActivityTaskType.REGISTER, details.get(0).getActivityCode());//发送新手奖励
                    //发实名认证奖励
                    goldService.award("REALNAME_AUTH");
                    //发完善资料奖励
                    goldService.award("PERFECT_DATA");
                    //boolean flag = rewardInvite20(userDAO.selectByPartyId(detail.getRecPartyId()));  //发放20人额外奖励
                    rewardInviteV321(userDAO.selectByPartyId(details.get(0).getRecPartyId()), Constant.ActivityTaskType.INVITE, details.get(0).getActivityCode());//发送推荐奖励
                    //发推荐人实名认证奖励
                    goldService.award("F_REALNAME_AUTH");
                    //发推荐人完善资料奖励
                    goldService.award("F_PERFECT_DATA");
                    
                    customersDTO.setInvitationPartyId(details.get(0).getRecPartyId());
                } else { //活动109
                    rewardInviteV321(user, Constant.ActivityTaskType.REGISTER351, details.get(0).getActivityCode());//发送新手奖励
                    //boolean flag = rewardInvite20(userDAO.selectByPartyId(detail.getRecPartyId()));  //发放20人额外奖励
                    rewardInviteV321(userDAO.selectByPartyId(details.get(0).getRecPartyId()), Constant.ActivityTaskType.INVITE351, details.get(0).getActivityCode());//发送推荐奖励
                    customersDTO.setInvitationPartyId(details.get(0).getRecPartyId());
                }
                customersInfoService.syncInvitationCustomer(customersDTO);
            } else {
                rewardInviteV321(user, Constant.ActivityTaskType.REGISTER, GoldService.ACTIVITY_REWARD);//发送新
                //发实名认证奖励
                goldService.award("REALNAME_AUTH");
                //发完善资料奖励
                goldService.award("PERFECT_DATA");
            }
//            syncRewardInvite();
        } catch (Exception e) {
            logger.warn("rewardV321", e);
        }
    }

    /**
     * 同步发奖励
     */
    private void syncRewardInvite() {
        logger.info("syncRewardInvite :{} ");
        List<ActivityInviteDetail> details = activityInviteDetailDAO.selectNoRewardInvite();
        logger.info("syncRewardInvite :{} ", details.size());
        if (details.size() > 0) {
            for (ActivityInviteDetail d : details) {
                ActivityInviteDetail detail = d;
                User user = userManager.getUserByLoginAccount(d.getLoginName());
                detail.setPartyId(user.getPartyId());
                detail.setDeviceId(user.getDeviceId());
                detail.setComments("同步发奖励");
                detail.setStatus(ActivityInviteStatus.S.name());
                detail.setActivityCode(d.getActivityCode());
                detail.setCommentName(user.getTrueName());
                activityInviteDetailDAO.updateByPrimaryKeySelective(detail);//更新状态为成功
                if (d.getActivityCode().equals(GoldService.ACTIVITY_REWARD)) {   //活动106
                    rewardInviteV321(userDAO.selectByPartyId(d.getRecPartyId()), Constant.ActivityTaskType.INVITE, details.get(0).getActivityCode());//发送推荐奖励
                    logger.info("syncRewardInvite :{} ", d.getRecPartyId() + ":同步发邀请奖励结束");
                    User u = userManager.getUserByLoginAccount(d.getLoginName());
                    if (u != null && u.getPartyId() != null) {
                        logger.info("syncRewardREGISTER begin:{} ", u.getPartyId());
                        //查询注册用户是否发了奖励，如果没有发，就补发
                        Map<String, Object> paras = new HashMap<>();
                        paras.put("partyId", u.getPartyId());
                        paras.put("activityCode", GoldService.ACTIVITY_REWARD);
                        paras.put("taskCode", Constant.ActivityTaskType.REGISTER.name());
                        List<AppActivityTask> taskList = appActivityTaskDAO.query(paras);
                        logger.info("syncRewardREGISTER begin:{} ", taskList.size());
                        if (taskList.size() == 0) { //没有发注册奖励
                            rewardInviteV321(userDAO.selectByPartyId(d.getPartyId()), Constant.ActivityTaskType.REGISTER, details.get(0).getActivityCode());//发送推荐奖励
                            logger.info("syncRewardREGISTER :{} ", d.getPartyId() + ":同步发注册奖励结束");
                        }
                    }
                } else { //活动109
                    rewardInviteV321(userDAO.selectByPartyId(d.getRecPartyId()), Constant.ActivityTaskType.INVITE351, details.get(0).getActivityCode());//发送推荐奖励
                    logger.info("syncRewardInvite :{} ", d.getRecPartyId() + ":同步发邀请奖励结束");
                    User u = userManager.getUserByLoginAccount(d.getLoginName());
                    if (u != null && u.getPartyId() != null) {
                        logger.info("syncRewardREGISTER begin:{} ", u.getPartyId());
                        //查询注册用户是否发了奖励，如果没有发，就补发
                        Map<String, Object> paras = new HashMap<>();
                        paras.put("partyId", u.getPartyId());
                        paras.put("activityCode", GoldService.COUNTDOWN_POPUP);
                        paras.put("taskCode", Constant.ActivityTaskType.REGISTER351.name());
                        List<AppActivityTask> taskList = appActivityTaskDAO.query(paras);
                        logger.info("syncRewardREGISTER begin:{} ", taskList.size());
                        if (taskList.size() == 0) { //没有发注册奖励
                            rewardInviteV321(userDAO.selectByPartyId(d.getPartyId()), Constant.ActivityTaskType.REGISTER351, details.get(0).getActivityCode());//发送推荐奖励
                            logger.info("syncRewardREGISTER :{} ", d.getPartyId() + ":同步发注册奖励结束");
                        }
                    }
                }
            }
        }
    }

    /**
     * 发放邀请慢20额外奖励
     *
     * @param user
     */
    private boolean rewardInvite20(User user) {
        boolean flag = false;
        try {
            int inviteCount = activityInviteDetailDAO.selectCount(ActivityInviteDetail.builder().activityCode(ActivityV321Service.ACTIVITY_CODE)
                    .recPartyId(user.getPartyId()).status(ActivityInviteStatus.S.name()).build());//查询总邀请数
            if (inviteCount >= 20) {//超过20处理
                int top = inviteCount / 20;
                String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
                AppActivityTask task = AppActivityTask.builder().partyId(user.getPartyId()).deviceId(user.getDeviceId())
                        .mobilePhone(user.getLoginName()).activityCode(ActivityV321Service.ACTIVITY_CODE)
                        .taskCode(Constant.ActivityTaskType.INVITE20.name()).daily(today).taskDailyId(user.getPartyId() + ActivityV321Service.ACTIVITY_CODE + top)
                        .taskName(Constant.ActivityTaskType.INVITE20.desc).taskStatus(YESNO.YES.code).useStatus(YESNO.NO.code).build();
                int result = appActivityTaskDAO.insertWithSelect(task);
                if (result > 0) {
                    List<AppTransaction> trans = activityManager.reward(user.getPartyId(), task.getId(), null);
                    flag = true;
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(trans)) {
                        //发送提示消息
                        task.setUseStatus(YESNO.YES.code);
                        appActivityTaskDAO.updateByPrimaryKeySelective(task);
                        /*try {
                            List<Long> partyIds = new ArrayList<>(1);
                            partyIds.add(user.getPartyId());
                            messageManager.sendMsg(MsgVo.builder().partyId(user.getPartyId()).msgTitle("领取乡邻大礼包").isSave(YESNO.YES)
                                    .message("恭喜你成功邀请20名好友，乡邻大礼包已发放，请至“我的-余额”和“我的-优惠券”查看~\n" +
                                            "再接再厉，下一个乡邻大礼包在向你召唤~").msgType(Constant.MsgType.ARTICLE.name()).loginCheck(YESNO.NO.code).passCheck(YESNO.NO.code).expiryTime(0).build(), partyIds);
                        } catch (Exception e) {
                            logger.warn("", e);
                        }*/
                    }
                    task = AppActivityTask.builder().partyId(user.getPartyId()).deviceId(user.getDeviceId())
                            .mobilePhone(user.getLoginName()).activityCode(ActivityV321Service.ACTIVITY_CODE)
                            .taskCode(Constant.ActivityTaskType.INVITE20.name()).daily(today).taskDailyId(user.getPartyId() + ActivityV321Service.ACTIVITY_CODE + top + 1)
                            .taskName(Constant.ActivityTaskType.INVITE20.desc).taskStatus(YESNO.YES.code).useStatus(YESNO.NO.code).build();
                    appActivityTaskDAO.insertWithSelect(task);
                }
            }
        } catch (AppException e) {
            logger.warn("rewardInviteV321 partyId:{},taskType:{} ", user);
        }
        return flag;
    }

    /**
     * 发放邀请奖励
     *
     * @param user
     * @param taskType
     */
    private void rewardInviteV321(User user, Constant.ActivityTaskType taskType, String code) {
        try {
            String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("activityCode", code);
            paras.put("rewardCategary", taskType.name());
            paras.put("rewardStatus", Constant.YESNO.YES.code);
            List<AppActivityReward> rewards = appActivityRewardDAO.selectList(paras);
            String amount = "0";
            if (rewards.size() > 0) {
                amount = String.valueOf(rewards.get(0).getStartAmt().intValue());
            }
            AppActivityTask task = AppActivityTask.builder().partyId(user.getPartyId()).deviceId(user.getDeviceId())
                    .mobilePhone(user.getLoginName()).activityCode(code)
                    .taskCode(taskType.name()).daily(today).taskDailyId(today + code + System.currentTimeMillis())
                    .taskName(taskType.desc).taskStatus(YESNO.YES.code).useStatus(YESNO.NO.code).alertStatus(YESNO.NO.code).taskResult(amount).build();
            int result = appActivityTaskDAO.insertWithSelect(task);
            if (result > 0) {
                List<AppTransaction> trans = activityManager.reward(user.getPartyId(), task.getId(), null);
                /*if (org.apache.commons.collections.CollectionUtils.isNotEmpty(trans)) {
                    //发送提示消息
                    task.setUseStatus(YESNO.YES.code);
                    appActivityTaskDAO.updateByPrimaryKeySelective(task);
                    try {
                        List<Long> partyIds = new ArrayList<>(1);
                        partyIds.add(user.getPartyId());
                        messageManager.sendMsg(MsgVo.builder().partyId(user.getPartyId()).msgTitle("领取现金红包").isSave(YESNO.YES)
                                .message("恭喜你成功领取现金红包，请至“我的-余额”查看。分享即送金秋好礼，请至“我的-邀请好友”点击“发出邀请”一键领取现金红包！\n" +
                                        "红包不等人，多邀多得哟~").msgType(Constant.MsgType.ARTICLE.name()).loginCheck(YESNO.NO.code).passCheck(YESNO.NO.code).expiryTime(0).build(), partyIds);
                    } catch (Exception e) {
                        logger.warn("", e);
                    }
                }*/
                //增加一个弹出提示框
                /*task = AppActivityTask.builder().partyId(user.getPartyId()).deviceId(user.getDeviceId())
                        .mobilePhone(user.getLoginName()).activityCode(ActivityV321Service.ACTIVITY_CODE)
                        .taskCode(taskType.name()).daily(today).taskDailyId(today + ActivityV321Service.ACTIVITY_CODE + System.currentTimeMillis() + 1)
                        .taskName(taskType.desc).taskStatus(YESNO.YES.code).useStatus(YESNO.NO.code).build();
                appActivityTaskDAO.insertWithSelect(task);*/
            }
        } catch (AppException e) {
            logger.warn("rewardInviteV321 partyId:{},taskType:{} ", user.getPartyId(), taskType, e);
        }
    }

    /**
     * 用户登陆，触发事件给推荐人奖励
     *
     * @param user
     */
    @Async
    public void inviteAlert(User user) {
        /*
         * 1,根据partyId判断该用户是否有未处理的推荐关系
         * 2，根据deviceId判断该用户是否有未处理的推荐关系
         * 3，查询推荐人奖励金额
         * 4，给推荐人发送金额
         * 5，将消息提醒放到缓存中
         */
        logger.info("init ActivityInviteListner user {}", user);
        try {
            ActivityInviteDetail ad ;
            logger.info("发送邀请奖励，{}", user);
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("loginName", user.getLoginName());
            List<ActivityInviteDetail> list = activityInviteDetailDAO.selectMap(paras);
            if (CollectionUtils.isNotEmpty(list)) {
                ad = list.get(0);
                if (!StringUtils.equals(ad.getStatus(), ActivityInviteStatus.I.name())) {
                    logger.info("用户已经处理奖励，{}", ad);
                }
                ad.setPartyId(user.getPartyId());
                ad.setDeviceId(user.getDeviceId());
                ad.setCommentName(initCommantName(user));

                paras = DTOUtils.queryMap();
                paras.put("partyId", user.getPartyId());
                list = activityInviteDetailDAO.selectMap(paras);
                if (CollectionUtils.isNotEmpty(list)) {
                    ad.setAmt(BigDecimal.ZERO);
                    ad.setStatus(ActivityInviteStatus.F.name());
                    ad.setMsgStatus(YESNO.YES.code);
                    ad.setComments("该partyId已经领取过奖励");
                    activityInviteDetailDAO.updateByPrimaryKeySelective(ad);
                    logger.info(ad.getComments());
                    return;
                }

                paras = DTOUtils.queryMap();
                paras.put("deviceId", user.getDeviceId());
                list = activityInviteDetailDAO.selectMap(paras);
                if (CollectionUtils.isNotEmpty(list)) {
                    ad.setAmt(BigDecimal.ZERO);
                    ad.setStatus(ActivityInviteStatus.F.name());
                    ad.setMsgStatus(YESNO.YES.code);
                    ad.setComments("该deviceId已经领取过奖励");
                    activityInviteDetailDAO.updateByPrimaryKeySelective(ad);
                    logger.info(ad.getComments());
                    return;
                }

                //状态变更为进行中
                ad.setStatus(ActivityInviteStatus.D.name());//更新状态为处理中
                activityInviteDetailDAO.updateByPrimaryKeySelective(ad);

                BigDecimal amt = computeInviteAmt(ad.getRecPartyId());
                logger.info("计算推荐奖励,amt:{}", amt);

                if (amt != null) {
                    ad.setAmt(amt);

                    //调用te接口给用户发放奖励
                    AccountBalChangeReq accountReq = new AccountBalChangeReq();
                    String requestNum = UUID.randomUUID().toString();
                    accountReq.setAccountBalType(AccountBalTypeEnums.ACCOUNT_BAL_PRINCIPAL.code);
                    accountReq.setOrderRequest(requestNum);
                    accountReq.setPartyId(ad.getRecPartyId() + "");
                    accountReq.setOrderNumber(createOrderNumber(ad.getPartyId()));
                    accountReq.setOrderSubNumber(createOrderNumber(ad.getPartyId()) + ad.getRecPartyId());
                    accountReq.setTransactionAmount(NumberUtil.toString(amt));
                    accountReq.setUserType(getUserType(user.getUserType()));
                    // 暂时使用红包充值类型
                    accountReq.setTransactionType(TransactionTypeEnums.TRANS_TYPE_100035.code);
                    accountReq.setMemo(TransactionTypeEnums.TRANS_TYPE_100035.msg);
                    accountReq.setTradeCategory(TradeTypeEnums.RECHARGE.name());
                    accountReq.setProductCode(ProductCode.APP.code);
                    accountReq.setOrderTime(DateUtils.getDateStr(DateUtils.getNow()));
                    accountReq.setRequestTime(String.valueOf(System.currentTimeMillis()));
                    accountReq.setAccountBalType(AccountBalTypeEnums.ACCOUNT_BAL_PRINCIPAL.code);
                    accountReq.setTransactionAmountSign(TransactionAmountSignEnums.POSITIVE.code);//充值or消费
                    accountReq.setFee("0");
                    accountReq.setFeeType(AccountFeeType.FEE_IN.code);
                    accountReq.setFeePayer(AccountFeeFromEnums.FEE_COMPANY.code);
                    accountReq.setTransactionSource("app");
                    accountReq.setCreator(ad.getPartyId() + "");
                    com.xianglin.te.common.service.facade.resp.Response<AccountBalChangeResp> accountResp = transferServiceClient.accountBalChange(accountReq);
                    logger.info("accountResp {}:", accountResp);

                    if (FacadeEnums.OK.code == accountResp.getCode()) {
                        ActivityInvite rec = activityInviteDAO.selectByPartyId(ad.getRecPartyId());
                        rec.setRecAmt(rec.getRecAmt().add(amt));
                        rec.setRecCount(rec.getRecCount() + 1);
                        activityInviteDAO.updateByPrimaryKeySelective(rec);

                        ad.setStatus(ActivityInviteStatus.S.name());//更新状态为处理成功
                        activityInviteDetailDAO.updateByPrimaryKeySelective(ad);


                        String msg = "恭喜" + rec.getCommentName() + "用户获得" + NumberUtil.amountFormat(amt) + "元邀请奖励";
                        PointRushVo push = new PointRushVo();
                        push.setPointRush(msg);
                        push.setPushActive(NativeActivity.ACTIVITY_INVITE.code);
                        push.setExpireTime(System.currentTimeMillis() + 60 * 60 * 1000);
                        logger.info("添加消息到首页滚动提示 {}:", push);
                        redisUtil.sadd(redisUtil.ACTIVITY_INVITE_POINT_ALERT, 0, JSON.json(push));
                    } else {
                        logger.error("资金账户发送推荐奖励失败，{}", accountReq);
                    }
                }

            }
        } catch (Exception e) {
            logger.error("", e);
        }

    }

    /**
     * 给新闻类型
     *
     * @param type
     * @return
     */
    private String getUserType(String type) {
        if (StringUtils.equals(type, UserType.nodeManager.name())) {
            return AccountBizTypeEnums.NODE_MANAGER_TYPE.code;
        } else {
            return AccountBizTypeEnums.VILLAGER_TYPE.code;
        }
    }

    /**
     * 计算奖励金额
     *
     * @param partyId
     * @return
     */
    private BigDecimal computeInviteAmt(Long partyId) {
        if (partyId == null || partyId == 0) {
            return null;
        }
        ActivityInviteDetail req = new ActivityInviteDetail();
        req.setRecPartyId(partyId);
        req.setStatus(ActivityInviteStatus.S.name());
        List<ActivityInviteDetail> list = activityInviteDetailDAO.select(req);
        int count = 0;
        if (CollectionUtils.isNotEmpty(list)) {
            count = list.size();
        }
        BigDecimal amt = null;
        if (count == 0) {
            amt = new BigDecimal("2");
        } else if (count >= 1 && count < 30) {
            amt = new BigDecimal("1");
        } else if (count >= 30 && count < 100) {
            amt = new BigDecimal("1.5");
        } else if (count >= 100) {
            amt = new BigDecimal("2");
        }
        return amt;
    }

    /**
     * 创建订单号
     *
     * @param partyId
     * @return
     */
    public static String createOrderNumber(Long partyId) {
        StringBuffer sb = new StringBuffer();
        sb.append(DateUtils.formatDate(new Date(), DateUtils.DATE_TPT_TWO)).append(System.currentTimeMillis()).append(partyId);
        if (sb.length() > 24) {
            return sb.substring(0, 24).toString();
        }
        return sb.toString();
    }

    /**
     * 设置显示名，针对站长需要额外处理
     *
     * @param user
     * @return
     */
    private String initCommantName(User user) {
        if (StringUtils.isNotEmpty(user.getNikerName())) {
            return user.getNikerName();
        } else {
            return SerialNumberUtil.phoneNumberEncrypt(user.getLoginName());
        }
    }

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        System.out.println(cal.get(Calendar.YEAR));
        System.out.println(cal.get(Calendar.MONTH) + 1);
        System.out.println(createOrderNumber(100000112L));
    }

}
