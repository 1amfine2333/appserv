package com.xianglin.appserv.biz.shared.impl;

import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.biz.shared.ActivityManager;
import com.xianglin.appserv.common.dal.daointerface.*;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.app.ActivityV321Service;
import com.xianglin.appserv.common.service.facade.app.GoldService;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.YesNo;
import com.xianglin.appserv.common.service.integration.cif.TransferServiceClient;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.core.model.exception.AppException;
import com.xianglin.cif.common.service.facade.GoldcoinService;
import com.xianglin.cif.common.service.facade.model.Response;
import com.xianglin.cif.common.service.facade.vo.GoldcoinAccountVo;
import com.xianglin.cif.common.service.facade.vo.GoldcoinExchangeVo;
import com.xianglin.cif.common.service.facade.vo.GoldcoinRecordVo;
import com.xianglin.te.common.service.facade.enums.Constants;
import com.xianglin.te.common.service.facade.req.AccountBalChangeReq;
import com.xianglin.te.common.service.facade.resp.AccountBalChangeResp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by wanglei on 2017/5/4.
 */
@Service
public class ActivityManagerImpl implements ActivityManager {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ActivityManagerImpl.class);

    @Autowired
    private AppActivityDAO appActivityDAO;

    @Autowired
    private AppActivityTaskDAO appActivityTaskDAO;

    @Autowired
    private AppActivityRewardDAO appActivityRewardDAO;

    @Autowired
    private AppTransactionDAO appTransactionDAO;

    @Autowired
    private TransferServiceClient transferServiceClient;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private GoldcoinService goldcoinService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ActivityInviteDetailDAO activityInviteDetailDAO;

    /**
     * 查询活动列表
     *
     * @param paras
     * @return
     */
    @Override
    public List<AppActivity> queryActivitys(Map<String, Object> paras) {
        return appActivityDAO.selectList(paras);
    }

    /**
     * 查询各个人物进度
     *
     * @param para
     * @param page
     * @return
     */
    @Override
    public List<AppActivityTask> queryActivityTask(AppActivityTask para, Page page) {
        return appActivityTaskDAO.selectList(para, page);
    }

    @Override
    public AppActivityTask saveUpdateActivityTask(AppActivityTask task) {
        if (task.getId() != null) {
            appActivityTaskDAO.updateByPrimaryKeySelective(task);
        } else {
            //需要判断活动是否进行中
            AppActivity activity = appActivityDAO.selectByCode(task.getActivityCode());
            if (activity != null && StringUtils.equals(activity.getDataStatus(), Constant.YESNO.YES.code)) {
                if (!redisUtil.isRepeat(task.getTaskDailyId(), 3600)) {
                    appActivityTaskDAO.insert(task);
                } else {
                    logger.warn("TASKDAILY ID ERROR ", task.getTaskDailyId());
                }
            }
        }
        return task;
    }

    @Override
    public List<AppActivityReward> queryActivityReward(Map<String, Object> paras) {
        return appActivityRewardDAO.selectList(paras);
    }

    @Override
    @Transactional
    public List<AppTransaction> reward(Long partyId, String activityCode) throws AppException {
        /*
        1，查询用户
        2，查询任务关联的task，没有活动则不发奖
        3，查询奖励
        4，发放奖励
        6，更新任务状态为已完成
         */
        User user = User.builder().build();
        user = userDAO.selectByPartyId(partyId);

        String categary = "";
        AppActivityTask task;

        String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
        AppActivityTask.AppActivityTaskBuilder builder = AppActivityTask.builder().activityCode("activityCode")
                .taskStatus(Constant.YESNO.YES.code)
                .useStatus(Constant.YESNO.NO.code)
                .partyId(partyId);
        if (StringUtils.equals(activityCode, Constant.ActivityType.NEW_GIFT.code)) {
            builder.taskCode(Constant.ActivityTaskType.NEWgIFT.name());
        } else {
            categary = user.getUserType();
            builder.daily(today);
        }
        List<AppActivityTask> tasks = appActivityTaskDAO.selectList(builder.build(), new Page());
        if (CollectionUtils.isEmpty(tasks)) {
            throw new AppException(FacadeEnums.ERROR_CHAT_400011);
        }
        task = tasks.get(0);//取第一个活动

        List<AppActivityReward> rws = randomReward(activityCode, categary, null, null);
        if (CollectionUtils.isEmpty(rws)) {
            for (int i = 0; i < 10; i++) {
                rws = randomReward(activityCode, categary, null, null);
                if (CollectionUtils.isNotEmpty(rws)) {
                    break;
                }
            }
        }
        if (CollectionUtils.isEmpty(rws)) {
            throw new AppException(FacadeEnums.ERROR_CHAT_400011);//奖励已经发放完了，明天再来吧
        }
        List<AppTransaction> trans = doReward(rws, task);
        return trans;
    }

    @Override
    public List<AppTransaction> reward(Long partyId, Long taskId, String subCategory) throws AppException {
        List<AppTransaction> trans = new ArrayList<>();
        logger.info("begin to reward.....");
        /**
         * 1,找到关联task，判断使用状态是否已经被使用
         * 2，根据活动，奖励类别查询奖励，
         * 3，发放奖励
         * 4，判断是否需要发送消息，若是，则发送消息
         */
        AppActivityTask task = appActivityTaskDAO.selectByPrimaryKey(taskId);
        if (task == null || !StringUtils.equals(task.getTaskStatus(), YesNo.Y.name())) {
            throw new AppException(FacadeEnums.ERROR_CHAT_400010);
        }
        List<AppActivityReward> rws = randomReward(task.getActivityCode(), task.getTaskCode(), subCategory, partyId);
        if (CollectionUtils.isEmpty(rws)) {
            throw new AppException(FacadeEnums.ERROR_CHAT_400011);//奖励已经发放完了，明天再来吧
        }
        checkActivityStatus(task.getActivityCode());//校验活动状态
        trans = doReward(rws, task);
        updateActivityStatus(task.getActivityCode());
        return trans;
    }

    @Override
    public List<AppTransaction> queryTransaction(AppTransaction tran, Page page) {
        return appTransactionDAO.selectTranList(tran, page);
    }

    @Override
    public int queryActivityTaskCount(Map<String, Object> paras) {
        return appActivityTaskDAO.queryActivityTaskCount(paras);
    }

    @Override
    public List<AppActivityTask> queryActivityTaskByParas(Map<String, Object> paras) {
        return appActivityTaskDAO.query(paras);
    }

    @Override
    public List<AppActivity> selecActivitytList(AppActivity build) {
        return appActivityDAO.selecActivitytList(build, null);
    }

    @Override
    public AppActivity queryActivityById(Long id) {
        return appActivityDAO.selectByPrimaryKey(id);
    }

    @Override
    public Boolean updateActivity(AppActivity appActivity) {
        return appActivityDAO.updateByPrimaryKeySelective(appActivity) == 1;
    }

    /**
     * 批量执行金币兑换
     *
     * @param batchId
     */
    @Override
    @Async
    public void batchExchangeGold(String batchId) {
        try {
            logger.info("start to batchExchangeGold batchId:{}", batchId);
            redisUtil.add(GoldService.DO_EXCHANGE_TAG, "true", -1);
            Response<GoldcoinExchangeVo> response = goldcoinService.queryExchangeBatch(batchId);
            logger.info(" batchInfo:{}", response);
            if (response.getResult() != null) {
                GoldcoinExchangeVo batch = response.getResult();
                if (StringUtils.equals(batch.getStatus(), GoldcoinExchangeVo.STATUS_I)) {
                    int startPage = 1;
                    int pageSize = 100;
                    Integer exchangeScale = batch.getExchangeScale();//兑换比例
                    int totalAccount = 0;//总账户数
                    int totalAmount = 0;//总金币数
                    BigDecimal totalBalance = BigDecimal.ZERO;//兑换成的总金额
                    List<GoldcoinAccountVo> list = goldcoinService.queryAccountList(GoldcoinAccountVo.TYPE_USER, startPage, pageSize).getResult();
                    while (list.size() > 0) {
                        for (GoldcoinAccountVo account : list) {
                            if (account.getAmount() > 0) {
                                BigDecimal exhangeBalance = NumberUtil.truncateBigDecimal(new BigDecimal(account.getAmount().doubleValue() / exchangeScale, MathContext.DECIMAL32), 2);
                                if (exhangeBalance.compareTo(BigDecimal.ZERO) > 0) {
                                    doExchange(account, exchangeScale, exhangeBalance, batchId);
                                }
                            }
                        }
                        startPage++;
                        list = goldcoinService.queryAccountList(GoldcoinAccountVo.TYPE_USER, startPage, pageSize).getResult();
                    }
                    goldcoinService.finishExchange(batchId);
                }
            }
            redisUtil.delete(GoldService.DO_EXCHANGE_TAG);
            logger.info("over batchExchangeGold batchId:{}", batchId);
        } catch (Exception e) {
            logger.warn("batchExchangeGold", e);
        }
    }

    @Override
    public void rewardV321(Long partyId, Constant.ActivityTaskType rewardType, Constant.ActivityTaskType recRewardType) throws AppException {
        /*
        0，发放个人奖励
        1，查询推荐记录
        2，判断并发放个人奖励
        3，判断并发放推荐人奖励
        4，判断推荐记录状态，如果不是成功则更新为成功
         */
        try {
            User user = userDAO.selectByPartyId(partyId);
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            if (rewardType != null) {
                logger.info("send rewardV321 to user:{}", user);
                Map<String, Object> paras = DTOUtils.queryMap();
                paras.put("activityCode", GoldService.ACTIVITY_REWARD);
                paras.put("rewardCategary", rewardType.name());
                paras.put("rewardStatus", Constant.YESNO.YES.code);
                List<AppActivityReward> rewards = appActivityRewardDAO.selectList(paras);
                String amount = "0";
                if (rewards.size() > 0) {
                    amount = String.valueOf(rewards.get(0).getStartAmt().intValue());
                }
                AppActivityTask task = AppActivityTask.builder().partyId(user.getPartyId()).deviceId(user.getDeviceId())
                        .mobilePhone(user.getLoginName()).activityCode(GoldService.ACTIVITY_REWARD)
                        .taskCode(rewardType.name()).daily(today).taskDailyId(today + rewardType.name() + user.getLoginName()).taskResult(amount)
                        .taskName(rewardType.desc).taskStatus(Constant.YESNO.YES.code).useStatus(Constant.YESNO.NO.code).alertStatus(Constant.YESNO.NO.code).build();
                if (!redisUtil.isRepeat(task.getTaskDailyId(), 3600)) {
                    int result = appActivityTaskDAO.insertWithSelect(task);
                    if (result > 0) {
                        List<AppTransaction> trans = reward(user.getPartyId(), task.getId(), null);
                    }
                } else {
                    logger.warn("TASKDAILY ID ERROR ", task.getTaskDailyId());
                }
            }
            if (recRewardType != null) {
                List<ActivityInviteDetail> details = activityInviteDetailDAO.select(ActivityInviteDetail.builder()
                        .loginName(user.getLoginName()).activityCode(GoldService.ACTIVITY_REWARD).build());
                if (details.size() == 1) {
                    ActivityInviteDetail detail = details.get(0);
                    User recUser = userDAO.selectByPartyId(detail.getRecPartyId());
                    if (recUser != null) {
                        if (checkActInvite(recUser.getPartyId())) {
                            AppActivityTask task = AppActivityTask.builder().partyId(recUser.getPartyId()).deviceId(recUser.getDeviceId())
                                    .mobilePhone(recUser.getLoginName()).activityCode(GoldService.ACTIVITY_REWARD)
                                    .taskCode(recRewardType.name()).daily(today).taskDailyId(today + recRewardType.name() + user.getLoginName() + recUser.getLoginName())
                                    .taskName(recRewardType.desc).taskStatus(Constant.YESNO.YES.code).useStatus(Constant.YESNO.NO.code).alertStatus(Constant.YESNO.NO.code).build();
                            if (!redisUtil.isRepeat(task.getTaskDailyId(), 3600)) {
                                int result = appActivityTaskDAO.insertWithSelect(task);
                                if (result > 0) {
                                    List<AppTransaction> trans = reward(recUser.getPartyId(), task.getId(), null);
                                    //更新推荐状态
                                    if (StringUtils.isEmpty(user.getTrueName())) {
                                        detail.setCommentName(user.getTrueName());
                                    }
                                    detail.setStatus(Constant.ActivityInviteStatus.S.name());
                                    activityInviteDetailDAO.updateByPrimaryKeySelective(detail);
                                }
                            } else {
                                logger.warn("TASKDAILY ID ERROR ", task.getTaskDailyId());
                            }
                        } else {
                            detail.setStatus(Constant.ActivityInviteStatus.S.name());
                            activityInviteDetailDAO.updateByPrimaryKeySelective(detail);
                        }
                    }
                } else {
                    logger.info("data error rewardV321 to user has more than one or no rec user:{}", user);
                }
            }
        } catch (Exception e) {
            logger.warn("rewardV321 exception", e);
        }
    }

    /**
     * 增加过滤参加好友争霸赛活动用户
     *
     * @param partyId
     * @return
     */
    private boolean checkActInvite(Long partyId) {
        try {
            LocalDate endDate = LocalDate.parse(SysConfigUtil.getStr("ACT_INVITE_ENDDATE"), DateTimeFormatter.BASIC_ISO_DATE);
            String invietUser = SysConfigUtil.getStr("ACT_INVITE_USER");
            if (LocalDate.now().isBefore(endDate) && StringUtils.contains(invietUser, partyId.toString())) {
                return false;
            }
        } catch (Exception e) {
            logger.warn("", e);
        }
        return true;
    }

    /**
     * 执行兑换
     *
     * @param account
     * @param exchangeScale 兑换比例
     * @param balance       兑换金额
     * @param batchId       批次号
     * @return
     */
    private boolean doExchange(GoldcoinAccountVo account, int exchangeScale, BigDecimal balance, String batchId) {
        try {
            logger.info("begin to account exchange account:{},exchangeScale:{},balance:{}", account, exchangeScale, balance);
            User user = userDAO.selectByPartyId(account.getPartyId());
            String transNo = createTransNo(account.getPartyId() + "");
            AppTransaction tran = new AppTransaction();
            tran.setTransNo(transNo);
            tran.setDaily(DateTime.now().toString("yyyyMMdd"));
            tran.setSubTransNo(transNo + 1);
            tran.setPartyId(user.getPartyId());
            tran.setMobilePhone(user.getLoginName());
            tran.setActivityCode(GoldService.ACTIVITY_REWARD);
            tran.setActivityCategary("EXCHANGE");//update
            tran.setTransType(Constant.TransType.ACTIVITY.name());
            tran.setAmtType(Constant.ActivityShareRewardType.CASH.name());
            tran.setAmount(balance);
            tran.setTransStatus(Constant.TransStatus.I.name());
            tran.setComments("金币兑换");
            doTransaction(tran);

            if (StringUtils.equals(tran.getTransStatus(), Constant.TransStatus.S.name())) {
                GoldcoinRecordVo req = GoldcoinRecordVo.builder().requestId(tran.getTransNo()).batchId(batchId).system("app").fronPartyId(10001L).toPartyId(tran.getPartyId())
                        .amount(-account.getAmount()).balance(balance).type(tran.getActivityCategary()).remark(tran.getComments()).build();
                logger.info("do request gold coin {}:", req);
                Response<GoldcoinRecordVo> resp = goldcoinService.doRecord(req);
                String accountRespStr = ToStringBuilder.reflectionToString(resp);
                return true;
            }
        } catch (Exception e) {
            logger.warn("doExchange fail", e);
        }
        return false;
    }

    /**
     * 更新业务状态
     *
     * @param activityCode
     */
    private void updateActivityStatus(String activityCode) {
        try {
            if (StringUtils.equals(activityCode, ActivityV321Service.ACTIVITY_CODE)) {
                //总金额超过100000
                BigDecimal amount = appTransactionDAO.selectTotalAmount(AppTransaction.builder().activityCode(ActivityV321Service.ACTIVITY_CODE).build(), null);
                BigDecimal amountLimit = new BigDecimal(SysConfigUtil.getStr("activity_v321_amout_limit"));

                String spareUrl = systemConfigMapper.getSysConfigValue("activity_v321_url_spare");//备用地址
                if (amountLimit.compareTo(amount) <= 0) {
                    AppActivity activity = appActivityDAO.selectByCode(activityCode);
                    activity.setDataStatus(YesNo.N.name());
                    activity.setActivityUrl(spareUrl);
                    activity.setComments("金额超限，活动自动下线");
                    appActivityDAO.updateByPrimaryKeySelective(activity);
                    //活动地址改为备用地址
                    SystemConfigModel config = systemConfigMapper.getSysConfigByKey("activity_v321_url");
                    config.setValue(spareUrl);
                    systemConfigMapper.updateByPrimaryKeySelective(config);
                }
            }
        } catch (Exception e) {
            logger.warn("updateActivityStatus", e);
        }
    }

    /**
     * 校验活动的状态
     *
     * @param activityCode
     */
    private void checkActivityStatus(String activityCode) throws AppException {
        List<AppActivity> list = appActivityDAO.selecActivitytList(AppActivity.builder().activeCode(activityCode).dataStatus(YesNo.Y.name())
                .date(DateUtils.formatDate(new Date(), "yyyyMMdd")).build(), null);
        if (CollectionUtils.isEmpty(list)) {
            throw new AppException(FacadeEnums.ERROR_CHAT_400029);
        }
    }

    /**
     * 执行发放奖励
     *
     * @param rws
     * @param task
     * @return
     * @throws AppException
     */
    private List<AppTransaction> doReward(List<AppActivityReward> rws, AppActivityTask task) throws AppException {
        List<AppTransaction> trans = new ArrayList<>();
        User user = userDAO.selectByPartyId(task.getPartyId());
        String transNo = createTransNo(user.getPartyId() + "");
        int i = 1;
        for (AppActivityReward rw : rws) {
            //发放奖励
            AppTransaction tran = new AppTransaction();
            tran.setTransNo(transNo);
            tran.setSubTransNo(transNo + i++);
            tran.setPartyId(user.getPartyId());
            tran.setMobilePhone(user.getLoginName());
            tran.setActivityCode(rw.getActivityCode());
            tran.setActivityCategary(task.getTaskCode());//update
            tran.setTaskId(task.getId());
            tran.setTransType(Constant.TransType.ACTIVITY.name());
            tran.setAmtType(rw.getRewardType());
            if (rw.getStartAmt().compareTo(rw.getEndAmt()) == 0) {
                tran.setAmount(rw.getStartAmt());
            } else {
                tran.setAmount(NumberUtil.formatBigDecimal(new BigDecimal(RandomUtils.nextDouble(rw.getStartAmt().doubleValue(), rw.getEndAmt().doubleValue())), 2));
            }
            tran.setTransStatus(Constant.TransStatus.I.name());
            tran.setDaily(task.getDaily());
            tran.setComments(rw.getRewardName());
            doTransaction(tran);
            trans.add(tran);
        }
        task.setUseStatus(Constant.YESNO.YES.code);
        appActivityTaskDAO.updateByPrimaryKeySelective(task);
        return trans;
    }


    private void doTransaction(AppTransaction tran) throws AppException {
        //执行交易
        try {
            appTransactionDAO.insert(tran);

            if (StringUtils.equals(tran.getAmtType(), Constant.ActivityShareRewardType.CASH.name())) {
                // 调用te接口给用户发放奖励
                AccountBalChangeReq accountReq = new AccountBalChangeReq();
                String requestNum = UUID.randomUUID().toString();
                accountReq.setAccountBalType(Constants.AccountBalTypeEnums.ACCOUNT_BAL_PRINCIPAL.code);
                accountReq.setOrderRequest(requestNum);
                accountReq.setPartyId(tran.getPartyId() + "");
                accountReq.setOrderNumber(tran.getTransNo());
                accountReq.setOrderSubNumber(tran.getSubTransNo());
                accountReq.setTransactionAmount(NumberUtil.toString(tran.getAmount()));
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
                accountReq.setCreator(tran.getPartyId() + "");
                com.xianglin.te.common.service.facade.resp.Response<AccountBalChangeResp> accountResp = transferServiceClient.accountBalChange(accountReq);
                String accountRespStr = ToStringBuilder.reflectionToString(accountResp);
                logger.info("accountResp {}:", accountRespStr);
                tran.setTransResult(accountRespStr);
                if (FacadeEnums.OK.code == accountResp.getCode()) {
                    tran.setTransStatus(Constant.TransStatus.S.name());
                } else {
                    tran.setTransStatus(Constant.TransStatus.F.name());
                }
            } else if (StringUtils.equals(tran.getAmtType(), Constant.ActivityShareRewardType.GOLDCOIN.name())) {//金币奖励
                GoldcoinRecordVo req = GoldcoinRecordVo.builder().requestId(tran.getTransNo()).system("app").fronPartyId(10000L).toPartyId(tran.getPartyId())
                        .amount(tran.getAmount().intValue()).type(tran.getActivityCategary()).remark(tran.getComments()).build();
                logger.info("do request gold coin {}:", req);
                Response<GoldcoinRecordVo> resp = goldcoinService.doRecord(req);
                String accountRespStr = ToStringBuilder.reflectionToString(resp);
                logger.info("doRecord {}:", accountRespStr);
                tran.setTransResult(accountRespStr);
                if (resp.isSuccess()) {
                    tran.setTransStatus(Constant.TransStatus.S.name());
                }
            } else {// 话费券和优惠券
                Map<String, String> param = new HashMap<>();
                String app_key = "xianglin".concat(DateUtils.formatDate(DateUtils.getNow(), DateUtils.DATE_TPT_TWO)).concat("@#_$&");
                param.put("party_id", tran.getPartyId() + "");
                if (StringUtils.startsWith(tran.getAmtType(), Constant.ActivityShareRewardType.CALL_BILL.name())) {
                    param.put("type", "1");// 话费券
                } else {
                    param.put("type", "2");// 优惠券
                    param.put("key", URLEncoder.encode(tran.getComments(), "utf-8"));
                }
                param.put("amount", NumberUtil.toString(tran.getAmount()));
                app_key = app_key.concat(SHAUtil.getSortString(param));
                param.put("app_key", app_key);
                param.put("sign", SHAUtil.shaEncode(MD5.encode(app_key)));
                String json = HttpUtils.executePost(PropertiesUtil.getProperty("luckwheel.coupon.url"), param, 2000);
                logger.info("reward:{}", json);
                tran.setTransResult(json);
                if (StringUtils.isNotEmpty(json)) {
                    JSONObject object = JSONObject.parseObject(json);
                    if ("1".equals(object.getString("error_code"))) {
                        tran.setTransRemark(object.getJSONObject("data").getString("memc_code"));
                        tran.setTransStatus(Constant.TransStatus.S.name());
                    } else {
                        tran.setTransStatus(Constant.TransStatus.F.name());
                    }
                }
            }
            appTransactionDAO.updateByPrimaryKeySelective(tran);
        } catch (Exception e) {
            logger.warn("doTransaction", e);
            throw new AppException(FacadeEnums.ERROR_CHAT_400013);
        }
    }

    /**
     * 生成订单号
     *
     * @param partyId
     * @return
     */
    private String createTransNo(String partyId) {
        StringBuilder sb = new StringBuilder();
        sb.append(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        for (int i = 0; i < 16; i++) {
            if (partyId.length() > i) {
                sb.append(partyId.charAt(i));
            } else {
                sb.append(RandomUtils.nextInt(0, 10));
            }
        }
        sb.append(System.currentTimeMillis());
        return sb.substring(0, 30);
    }

    /**
     * 随机奖励
     *
     * @param activityCode 活动号
     * @param category     奖励类别
     * @return
     * @throws AppException
     */
    private List<AppActivityReward> randomReward(String activityCode, String category, String subCategory, Long partyId) throws AppException {
        Map<String, Object> paras = DTOUtils.queryMap();
        paras.put("activityCode", activityCode);
        if (StringUtils.isNotEmpty(category)) {
            paras.put("rewardCategary", category);
        }
        int random = RandomUtils.nextInt(0, 100);
        paras.put("random", random);
        paras.put("subCategary", subCategory);
        paras.put("rewardStatus", Constant.YESNO.YES.code);
        List<AppActivityReward> rewards = appActivityRewardDAO.selectList(paras);
        if (CollectionUtils.isEmpty(rewards)) {
            throw new AppException(FacadeEnums.ERROR_CHAT_400009);
        }
        List<AppActivityReward> rewardList = new ArrayList<>();
        for (AppActivityReward rw : rewards) {//判断奖励是否有效
            if (rw.getLimit() > 0) {
                BigDecimal total = appTransactionDAO.selectTotalAmount(AppTransaction.builder().activityCode(activityCode).amtType(rw.getRewardType()).activityCategary(rw.getRewardCategary()).partyId(partyId).build(), null);
                if (rw.getLimit() <= total.intValue()) {//超过总上线
                    logger.info("exceed limit total:{},limit:{}", total, rw.getLimit());
                    break;
                }
            }
            if (rw.getDailyLimit() >= 0) {
                String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
                BigDecimal total = appTransactionDAO.selectTotalAmount(AppTransaction.builder().activityCode(activityCode).amtType(rw.getRewardType()).daily(today).activityCategary(rw.getRewardCategary()).partyId(partyId).build(), null);
                if (rw.getDailyLimit() <= total.intValue()) {
                    logger.info("exceed dayLimit total:{},dayLimit:{}", total, rw.getDailyLimit());
                    break;
                }
            }
            rewardList.add(rw);
        }
        return rewardList;
    }

    public static void main(String[] args) throws Exception {
//        String partyId = "666666671988";
//        Map<String, String> param = new HashMap<>();
//        String app_key = "xianglin".concat(DateUtils.formatDate(DateUtils.getNow(), DateUtils.DATE_TPT_TWO)).concat("@#_$&");
//        param.put("party_id", partyId);
//        if (StringUtils.startsWith("CALL_BILL_1", Constant.ActivityShareRewardType.CALL_BILL.name())) {
//            param.put("type", "1");// 话费券
//        } else {
//            param.put("type", "2");// 优惠券
//        }
//        param.put("amount", "10.00");
//        app_key = app_key.concat(SHAUtil.getSortString(param));
//        param.put("app_key", app_key);
//        param.put("sign", SHAUtil.shaEncode(MD5.encode(app_key)));
//        String json = HttpUtils.executePost("https://mai-test1.xianglin.cn/index.php/wap/lottery-luckyCoupons.html", param, 20000);
//        logger.info("reward:{}", json);
//
//        System.out.println(DateFormatUtils.format(new Date(), "yyyyMMddhhmmss"));
//        System.out.println(System.currentTimeMillis());
        BigDecimal big = new BigDecimal(30D / 1000D, MathContext.DECIMAL32);
        System.out.println(big);
        System.out.println(NumberUtil.truncateBigDecimal(new BigDecimal(30D / 1000D, MathContext.DECIMAL32), 2));
        System.out.println(30D / 1000);
    }
}
