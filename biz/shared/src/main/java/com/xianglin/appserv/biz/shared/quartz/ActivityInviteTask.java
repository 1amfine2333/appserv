/**
 *
 */
package com.xianglin.appserv.biz.shared.quartz;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.xianglin.appserv.biz.shared.ActivityManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.common.dal.daointerface.*;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.app.GoldService;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.YesNo;
import com.xianglin.cif.common.service.facade.GoldcoinService;
import com.xianglin.cif.common.service.facade.model.Response;
import com.xianglin.cif.common.service.facade.vo.GoldcoinRecordVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.xianglin.appserv.biz.shared.MessageManager;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.ActivityInviteStatus;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgStatus;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgType;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.UserType;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.YESNO;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.integration.cif.TransferServiceClient;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.common.util.NumberUtil;
import com.xianglin.appserv.common.util.SerialNumberUtil;
import com.xianglin.appserv.common.util.constant.AppservConstants;
import com.xianglin.te.common.service.facade.enums.Constants.AccountBalTypeEnums;
import com.xianglin.te.common.service.facade.enums.Constants.AccountBizTypeEnums;
import com.xianglin.te.common.service.facade.enums.Constants.AccountFeeFromEnums;
import com.xianglin.te.common.service.facade.enums.Constants.AccountFeeType;
import com.xianglin.te.common.service.facade.enums.Constants.ProductCode;
import com.xianglin.te.common.service.facade.enums.Constants.TradeTypeEnums;
import com.xianglin.te.common.service.facade.enums.Constants.TransactionAmountSignEnums;
import com.xianglin.te.common.service.facade.enums.Constants.TransactionTypeEnums;
import com.xianglin.te.common.service.facade.req.AccountBalChangeReq;
import com.xianglin.te.common.service.facade.resp.AccountBalChangeResp;
import com.xianglin.xlnodecore.common.service.facade.DistrictCodeFullService;
import com.xianglin.xlnodecore.common.service.facade.NodeService;
import com.xianglin.xlnodecore.common.service.facade.req.NodeReq;
import com.xianglin.xlnodecore.common.service.facade.resp.DistrictCodeFullResp;
import com.xianglin.xlnodecore.common.service.facade.resp.NodeResp;
import com.xianglin.xlnodecore.common.service.facade.vo.NodeVo;

/**
 * 邀请活动相关定时任务
 *
 * @author wanglei 2016年12月14日下午6:13:14
 */
public class ActivityInviteTask {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ActivityInviteTask.class);

    @Autowired
    private ActivityInviteDetailDAO activityInviteDetailDAO;

    @Autowired
    private ActivityInviteDAO activityInviteDAO;

    @Autowired
    private ActivityInviteRankingDAO activityInviteRankingDAO;

    @Autowired
    private TaskDAO taskDAO;

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private TransferServiceClient transferServiceClient;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private DistrictCodeFullService dsitricodeService;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private GoldcoinService goldcoinService;

    @Autowired
    private AppActivityInviteRewardDAO appActivityInviteRewardDAO;

    /**
     * 每天7,11,19三个时间点发送推送成功提醒
     */
    void inviteAlert() {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("taskId", "30011");
            Task task = taskDAO.getTaskByTaskId(map);
            if (task == null) {
                return;
            }
            task.setUpdateTime(new Date());
            task.setTimeStamp(System.currentTimeMillis());
            task.setStatus(AppservConstants.STATUS_EXECUTING);
            int result = taskDAO.updateEntity(task);// 任务更新为进行中

            alert();

            synchroCommentNameExecute();//同步用户昵称信息

            // 更新任务为已经完成
            task.setVersion(task.getVersion() + 1);
            task.setTimeStamp(System.currentTimeMillis());
            task.setStatus(AppservConstants.STATUS_UNEXECUTED);
            result = taskDAO.updateEntity(task);// 任务更新为结束
        } catch (Exception e) {
            logger.error("InviteAlertTask", e);
        }
    }

    /**
     * 每周一0点开始统计排行榜并确定发放奖励金额
     */
    void inviteRanking() {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("taskId", "30012");
            Task task = taskDAO.getTaskByTaskId(map);
            if (task == null) {
                return;
            }
            task.setUpdateTime(new Date());
            task.setTimeStamp(System.currentTimeMillis());
            task.setStatus(AppservConstants.STATUS_EXECUTING);
            int result = taskDAO.updateEntity(task);// 任务更新为进行中

            inviteRankingInit();

            // 更新任务为已经完成
            task.setVersion(task.getVersion() + 1);
            task.setTimeStamp(System.currentTimeMillis());
            task.setStatus(AppservConstants.STATUS_UNEXECUTED);
            result = taskDAO.updateEntity(task);// 任务更新为结束
        } catch (Exception e) {
            logger.error("InviteAlertTask", e);
        }
    }

    /**
     * 每周一中午12点发放排行榜奖励
     */
    void inviteRankingReward() {
        try {
            logger.info("inviteRankingReward ");
            Map<String, String> map = new HashMap<>();
            map.put("taskId", "30013");
            Task task = taskDAO.getTaskByTaskId(map);
            if (task == null) {
                return;
            }
            task.setUpdateTime(new Date());
            task.setTimeStamp(System.currentTimeMillis());
            task.setStatus(AppservConstants.STATUS_EXECUTING);
            int result = taskDAO.updateEntity(task);// 任务更新为进行中

            rewardRanking();

            // 更新任务为已经完成
            task.setVersion(task.getVersion() + 1);
            task.setTimeStamp(System.currentTimeMillis());
            task.setStatus(AppservConstants.STATUS_UNEXECUTED);
            result = taskDAO.updateEntity(task);// 任务更新为结束
        } catch (Exception e) {
            logger.error("InviteAlertTask", e);
        }
    }

    /**
     * 发放推荐奖励
     */
    public void rewardInvite() {
        SystemConfigModel model = null;
        try {
            logger.info("begin to rewardInvite");
            int result = systemConfigMapper.updateSyn("invite_reward_task","0","1");
            if (result == 0) {
                return;
            }
            //1，计算每个用户前一天收益并进入数据库
            Set<Long> recSet = new HashSet<>();//推荐人
            String day = DateTime.now().minusDays(1).toString("yyyyMMdd");
            DateTime startTime = DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(1).minusDays(1);
            DateTime endTime = startTime.plusDays(1);
            int scale = Integer.valueOf(systemConfigMapper.getSysConfigValue("sign_tribute_ratio").replace("%",""));
            List<ActivityInviteDetail> details = activityInviteDetailDAO.select(ActivityInviteDetail.builder().activityCode(GoldService.ACTIVITY_REWARD).status(ActivityInviteStatus.S.name()).build());
            for (ActivityInviteDetail detail : details) {
                if (detail.getPartyId() != null && detail.getRecPartyId() != null && detail.getRecPartyId() != 1) {
                    int totalAmount = 0;
                    recSet.add(detail.getRecPartyId());
                    Response<List<GoldcoinRecordVo>> resp = goldcoinService.queryRecord(GoldcoinRecordVo.builder().toPartyId(detail.getPartyId()).startDate(startTime.toDate()).endDate(endTime.toDate()).startPage(1).pageSize(Short.MAX_VALUE).build());
                    if (CollectionUtils.isNotEmpty(resp.getResult())) {
                       for(GoldcoinRecordVo record:resp.getResult()){
                           if(record.getAmount() > 0 && !StringUtils.equals(record.getType(),"PAY_TRIBUTE")){
                               totalAmount += record.getAmount();
                           }
                       }
                       if(totalAmount > 0){
                           int rewuard = totalAmount*scale/100;
                           if(rewuard > 0){
                                appActivityInviteRewardDAO.insert(AppActivityInviteReward.builder().partyId(detail.getPartyId()).activityCode(GoldService.ACTIVITY_REWARD).day(day).amount(totalAmount).scale(scale).reward(rewuard).recPartyId(detail.getRecPartyId()).status(ActivityInviteStatus.I.name()).build());
                           }
                       }
                    }
                }
            }
            //2，查询邀请表，发放奖励
            for(Long recPartyId:recSet){
                List<AppActivityInviteReward> list = appActivityInviteRewardDAO.select(AppActivityInviteReward.builder().recPartyId(recPartyId).status(ActivityInviteStatus.I.name()).activityCode(GoldService.ACTIVITY_REWARD).build());
                int totalReward = 0;
                String transId = StringUtils.substring(GoldService.ACTIVITY_REWARD+DateTime.now().toString("yyyyMMdd")+recPartyId+System.currentTimeMillis(),0,32);
                if(CollectionUtils.isNotEmpty(list)){
                    for(AppActivityInviteReward reward:list){
                        totalReward += reward.getReward();
                    }
                    goldcoinService.doRecord(GoldcoinRecordVo.builder().system("app").requestId(transId).fronPartyId(10000L).toPartyId(recPartyId).amount(totalReward).type("PAY_TRIBUTE").remark("好友进贡").build());
                    for(AppActivityInviteReward reward:list){
                        reward.setRewardId(transId);
                        reward.setStatus(ActivityInviteStatus.S.name());
                        appActivityInviteRewardDAO.updateByPrimaryKeySelective(reward);
                    }
                }
            }
            logger.info("finish rewardInvite");
        } catch (Exception e) {
            logger.warn("rewardInvite", e);
        } finally {
            systemConfigMapper.updateSyn("invite_reward_task","1","0");
        }
    }

    /**
     * 每天8点给未签到的用户发送推送
     */
    void signTip() {
        SystemConfigModel model = null;
        try {
            TimeUnit.SECONDS.sleep(RandomUtils.nextInt(5));//任务执行前先暂停
            logger.info("begin to signTip");
            int result = systemConfigMapper.updateSyn("sign_tip","0","1");
            if (result == 0) {
                return;
            }
            //执行发提醒任务
            publishSignTip();
        } catch (Exception e) {
            logger.error("signTip", e);
        } finally {
            systemConfigMapper.updateSyn("sign_tip","1","0");
        }
    }


    /**
     * 执行发提醒任务
     */
    @Async
    private void publishSignTip() {
        try {
            logger.info("publishSignTip begin");
            int size = 100;
            //查询所有已经签到的用户的partyID
            String today = DateUtils.formatDate(new Date(), "yyyyMMdd");
            List<AppActivityTask> taskList = activityManager.queryActivityTask(AppActivityTask.builder().activityCode(GoldService.ACTIVITY_SIGN).taskCode(Constant.ActivityTaskType.SIGN.name()).daily(today).taskStatus(YesNo.Y.name()).useStatus(YesNo.Y.name()).build(), null);
            List<Long> signPartyIds = new ArrayList<>(1);
            for (AppActivityTask task : taskList) {
                signPartyIds.add(task.getPartyId());
            }
            Map<String, Object> queryMap = new HashMap<>();
            int startPage = 1;
            queryMap.put("startPage", startPage);
            queryMap.put("pageSize", size);
            boolean run = true;
            while (run) {
                List<User> list = userManager.getUsersByParam(queryMap);
                startPage = startPage + 1;
                queryMap.remove("startPage");
                queryMap.put("startPage", startPage);
                if (CollectionUtils.isEmpty(list) || list.size() == 0) {
                    run = false;
                } else {
                    List<Long> partyIds = new ArrayList<>(1);
                    for (User u : list) {
                        if (!signPartyIds.contains(u.getPartyId())) {
                            partyIds.add(u.getPartyId());
                        }
                    }
                    //发推送
                    if (partyIds.size() > 0) {
                        messageManager.sendMsg(MsgVo.builder().msgTitle("乡邻app ").isSave(YESNO.YES)
                                .message("你今天的签到奖励还没有领取，赶快领取吧").msgType(MsgType.SIGN.name()).loginCheck(YESNO.NO.code).passCheck(YESNO.NO.code).expiryTime(0).isDeleted("Y").msgSource(MsgType.SIGN.name()).build(), partyIds);
                        logger.info("publishSignTip end:" + partyIds.size());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("publishSignTip", e);
        }
    }

    /**
     * 同步推荐人和被推荐人昵称
     */
    @Async
    private void synchroCommentNameExecute() {
        try {
            logger.info("synchroCommentNameExecute begin");
            //同步推荐人昵称
            String commentName = null;
            int startPage = 1;
            Map<String, Object> paras = DTOUtils.queryMap();
            List<ActivityInvite> list = activityInviteDAO.selectRanking(paras);
            while (CollectionUtils.isNotEmpty(list)) {
                for (ActivityInvite ai : list) {
                    //修改用户名
                    commentName = queryCommentName(ai.getPartyId());
                    if (StringUtils.isNotEmpty(commentName) && !StringUtils.equals(ai.getCommentName(), commentName)) {
                        ai.setCommentName(commentName);
                        activityInviteDAO.updateByPrimaryKeySelective(ai);
                    }
                }
                paras.put("startPage", ++startPage);
                list = activityInviteDAO.selectRanking(paras);
            }

            //同步被推荐人昵称
            int page = 0;
            paras.put("startPage", ++startPage);
            paras.put("status", ActivityInviteStatus.S.name());
            List<ActivityInviteDetail> details = activityInviteDetailDAO.selectMap(paras);
            while (CollectionUtils.isNotEmpty(details)) {
                for (ActivityInviteDetail ai : details) {
                    //修改用户名
                    commentName = queryCommentName(ai.getPartyId());
                    if (!StringUtils.equals(ai.getCommentName(), commentName)) {
                        ai.setCommentName(commentName);
                        activityInviteDetailDAO.updateByPrimaryKeySelective(ai);
                    }
                }
                paras.put("startPage", ++startPage);
                details = activityInviteDetailDAO.selectMap(paras);
            }
        } catch (Exception e) {
            logger.error("synchroCommentNameExecute", e);
        }
    }

    private String queryCommentName(Long partyId) {
        String commentName = null;
        try {
            User user = userDAO.selectByPartyId(partyId);
            logger.info("queryCommentName user info {} ", user);
            if (StringUtils.equals(user.getUserType(), UserType.nodeManager.name())) {//针对站长
                NodeReq req = new NodeReq();
                NodeVo vo = new NodeVo();
                vo.setNodeManagerPartyId(partyId);
                req.setVo(vo);
                NodeResp resp = nodeService.queryNodeInfoByNodeManagerPartyId(req);
                logger.info("queryCommentName node manager {},node info:{}", partyId, resp);
                if (resp.getVo() != null) {
                    String managerName = resp.getVo().getNodeManagerName();
                    logger.info("queryCommentName node managervo {},node info:{}", resp.getVo());
                    DistrictCodeFullResp dresp = dsitricodeService.queryDistrictCodeFullByDistrictCode(resp.getVo().getDistrictCode());
                    if (dresp.getDistrictCodeFullVo() != null) {
                        commentName = dresp.getDistrictCodeFullVo().getProvinceName() + dresp.getDistrictCodeFullVo().getCityName() + StringUtils.substring(managerName, 0, 1) + "站长";
                    }
                }
            }
            if (StringUtils.isEmpty(commentName)) {
                commentName = user.getNikerName();
            }
            if (StringUtils.isEmpty(commentName)) {
                commentName = SerialNumberUtil.phoneNumberEncrypt(user.getLoginName());
            }
        } catch (Exception e) {
            logger.error("queryCommentName", e);
        }
        return commentName;
    }

    private void alert() {
        try {
            logger.info("invite alert begin");
            List<Long> list = activityInviteDetailDAO.selectAlertPartyIds();
            if (CollectionUtils.isNotEmpty(list)) {
                List<ActivityInviteDetail> reminds = null;
                BigDecimal total = BigDecimal.ZERO;
                for (Long partyId : list) {
                    ActivityInviteDetail detail = new ActivityInviteDetail();
                    detail.setRecPartyId(partyId);
                    detail.setMsgStatus(YESNO.NO.code);
                    detail.setStatus(ActivityInviteStatus.S.name());
                    reminds = activityInviteDetailDAO.select(detail);
                    if (CollectionUtils.isNotEmpty(reminds)) {
                        for (ActivityInviteDetail d : reminds) {
                            d.setMsgStatus(YESNO.YES.code);
                            total = total.add(d.getAmt());
                            activityInviteDetailDAO.updateByPrimaryKeySelective(d);
                        }

                        // 发送提醒消息
                        String msg = "您又成功邀请了" + reminds.size() + "位用户了哦，" + NumberUtil.amountFormat(total) + "元奖励已到账，快来看看吧";
                        sendNoticeMsg(partyId, msg);
                        // 统计金额初始化
                        total = BigDecimal.ZERO;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("InviteAlertTask ", e);
        }
    }

    /**
     * 保存排行榜
     */
    private void inviteRankingInit() {
        try {
            Map<String, Object> paras = DTOUtils.queryMap();
            List<ActivityInvite> list = activityInviteDAO.selectRanking(paras);// 只取前十名
            if (CollectionUtils.isNotEmpty(list)) {
                String rankDate = DateUtils.formatDate(new Date(), DateUtils.DATE_TPT_TWO);
                for (ActivityInvite ai : list) {
                    ActivityInviteRanking rank = new ActivityInviteRanking();
                    rank.setRankingDate(rankDate);
                    rank.setPartyId(ai.getPartyId());
                    rank.setRanking(ai.getRanking());
                    rank.setInviteCount(ai.getRecCount());
                    rank.setInviteAmt(ai.getRecAmt());
                    rank.setRewardAmt(getRewardAmt(rank.getInviteCount()));
                    if (rank.getRewardAmt().compareTo(BigDecimal.ZERO) == 0) {
                        rank.setComments("不满足领取奖励条件");
                        rank.setStatus(ActivityInviteStatus.F.name());
                    } else {
                        rank.setStatus(ActivityInviteStatus.I.name());
                    }
                    activityInviteRankingDAO.insert(rank);
                }
            }
        } catch (Exception e) {
            logger.error("inviteRankingInit", e);
        }
    }

    /**
     * 计算奖励金额
     *
     * @param count
     * @return
     */
    private BigDecimal getRewardAmt(int count) {
        BigDecimal amt = null;
        if (count >= 1000) {
            amt = new BigDecimal("100");
        } else if (count >= 300) {
            amt = new BigDecimal("50");
        } else if (count >= 100) {
            amt = new BigDecimal("10");
        } else {
            amt = BigDecimal.ZERO;
        }
        return amt;
    }

    /**
     * 发放排行榜奖励
     */
    private void rewardRanking() {
        try {
            String rankDate = DateUtils.formatDate(new Date(), DateUtils.DATE_TPT_TWO);
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("rankingDate", rankDate);
            List<ActivityInviteRanking> ranks = activityInviteRankingDAO.selectRanking(paras);
            for (ActivityInviteRanking rank : ranks) {
                logger.info("rewardRanking {}", rank);
                if (StringUtils.equals(rank.getStatus(), ActivityInviteStatus.I.name())) {
                    // 发放奖励
                    // 调用te接口给用户发放奖励
                    AccountBalChangeReq accountReq = new AccountBalChangeReq();
                    String requestNum = UUID.randomUUID().toString();
                    accountReq.setAccountBalType(AccountBalTypeEnums.ACCOUNT_BAL_PRINCIPAL.code);
                    accountReq.setOrderRequest(requestNum);
                    accountReq.setPartyId(rank.getPartyId() + "");
                    accountReq.setOrderNumber(createOrderNumber(rank.getPartyId()));
                    accountReq.setOrderSubNumber(createOrderNumber(rank.getPartyId()) + rank.getId());
                    accountReq.setTransactionAmount(NumberUtil.toString(rank.getRewardAmt()));
                    accountReq.setUserType(AccountBizTypeEnums.VILLAGER_TYPE.code);
                    accountReq.setTransactionType(TransactionTypeEnums.TRANS_TYPE_100035.code);
                    accountReq.setMemo(TransactionTypeEnums.TRANS_TYPE_100035.msg);
                    accountReq.setTradeCategory(TradeTypeEnums.RECHARGE.name());
                    accountReq.setProductCode(ProductCode.APP.code);
                    accountReq.setOrderTime(DateUtils.getDateStr(DateUtils.getNow()));
                    accountReq.setRequestTime(String.valueOf(System.currentTimeMillis()));
                    accountReq.setAccountBalType(AccountBalTypeEnums.ACCOUNT_BAL_PRINCIPAL.code);
                    accountReq.setTransactionAmountSign(TransactionAmountSignEnums.POSITIVE.code);// 充值or消费
                    accountReq.setFee("0");
                    accountReq.setFeeType(AccountFeeType.FEE_IN.code);
                    accountReq.setFeePayer(AccountFeeFromEnums.FEE_COMPANY.code);
                    accountReq.setTransactionSource("app");
                    accountReq.setCreator(rank.getPartyId() + "");
                    com.xianglin.te.common.service.facade.resp.Response<AccountBalChangeResp> accountResp = transferServiceClient
                            .accountBalChange(accountReq);
                    logger.info("rewardRanking accountResp {}:", accountResp);
                    if (FacadeEnums.OK.code == accountResp.getCode()) {
                        ActivityInvite ai = activityInviteDAO.selectByPartyId(rank.getPartyId());
                        ai.setRecAmt(ai.getRecAmt().add(rank.getRewardAmt()));
                        activityInviteDAO.updateByPrimaryKeySelective(ai);

                        rank.setStatus(ActivityInviteStatus.S.name());
                        activityInviteRankingDAO.updateByPrimaryKeySelective(rank);

                        // 给该用户推送一条消息
                        String msg = "恭喜您成功获得" + NumberUtil.amountFormat(rank.getRewardAmt()) + "元排名奖励！";
                        sendNoticeMsg(rank.getPartyId(), msg);
                    } else {
                        logger.error("资金账户发送推荐奖励失败，{}", accountReq);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("rewardRanking", e);
        }
    }

    /**
     * 发送提醒消息
     *
     * @param partyId
     * @param msg
     */
    private void sendNoticeMsg(Long partyId, String msg) {
        try {
            MsgVo vo = new MsgVo();
            vo.setPartyId(partyId);
            vo.setIsDeleted(YESNO.YES.code);
            vo.setMsgStatus(MsgStatus.READED.name());
            vo.setIsSave(YESNO.NO);
            vo.setMsgType(MsgType.NOTIFY.name());
            vo.setLoginCheck(YESNO.YES.code);
            vo.setMsgTitle("邀请提醒");
            vo.setMessage(msg);
            vo.setExpiryTime(20 * 60);// 8：55分开始，9：15结束
            vo.setLoginCheck(YESNO.YES.code);
            messageManager.sendMsg(vo);
        } catch (Exception e) {
            logger.error("sendNoticeMsg", e);
        }
    }

    /**
     * 创建订单号
     *
     * @param partyId
     * @return
     */
    public static String createOrderNumber(Long partyId) {
        StringBuffer sb = new StringBuffer();
        sb.append(DateUtils.formatDate(new Date(), DateUtils.DATE_TPT_TWO)).append(partyId)
                .append(System.currentTimeMillis());
        if (sb.length() > 24) {
            return sb.substring(0, 24).toString();
        }
        return sb.toString();
    }
}
