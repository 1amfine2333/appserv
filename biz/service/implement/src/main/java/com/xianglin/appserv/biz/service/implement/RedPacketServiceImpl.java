/**
 *
 */
package com.xianglin.appserv.biz.service.implement;

import com.alibaba.dubbo.common.json.JSON;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.daointerface.RedPacketPoolDAO;
import com.xianglin.appserv.common.dal.dataobject.RedPacketPool;
import com.xianglin.appserv.common.service.facade.RedPacketService;
import com.xianglin.appserv.common.service.facade.model.PointRushDTO;
import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.AccountNodeManagerVo;
import com.xianglin.appserv.common.service.facade.model.vo.PointRushVo;
import com.xianglin.appserv.common.service.facade.model.vo.RedPacketVo;
import com.xianglin.appserv.common.service.facade.model.vo.StationVo;
import com.xianglin.appserv.common.service.integration.cif.PartyAttrAccountServiceClient;
import com.xianglin.appserv.common.service.integration.cif.TransferServiceClient;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.core.service.CoreRedPacketService;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.fala.session.Session;
import com.xianglin.gateway.common.service.facade.model.ResultEnum;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.te.common.service.facade.enums.Constants.*;
import com.xianglin.te.common.service.facade.req.AccountBalChangeReq;
import com.xianglin.te.common.service.facade.resp.AccountBalChangeResp;
import com.xianglin.te.common.service.facade.vo.AcctChangeUpTe;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhangyong 2016年10月12日下午4:35:44
 */
@ServiceInterface
public class RedPacketServiceImpl implements RedPacketService {

    private static final String RED_PACKET_URL = "RED_PACKET_URL";
    private static final Logger logger = LoggerFactory.getLogger(RedPacketServiceImpl.class);
    @Autowired
    private CoreRedPacketService coreRedPacketService;
    @Autowired
    private RedPacketPoolDAO poolDAO;

    private SessionHelper sessionHelper;

    @Autowired
    private TransferServiceClient transferServiceClient;

    @Autowired
    private PartyAttrAccountServiceClient partyAttrAccountServiceClient;

    private static String FRESH_RED_PACKET = "fresh.red.packet";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserManager userManager;

    /**
     * @see com.xianglin.appserv.common.service.facade.RedPacketService#checkRemains(java.lang.String)
     */
    @Override
    public Response<Boolean> checkRemains(String date) {
        Response<Boolean> resp = new Response<>(null);
        resp.setFacade(FacadeEnums.OK);
        if (!coreRedPacketService.checkRemains(date)) {
            resp.setResonpse(ResponseEnum.RED_PACKET_EMPTY);
        } else {
            resp.setResult(Boolean.TRUE);
        }
        return resp;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.RedPacketService#checkHasParticipate(java.lang.String, java.lang.Long)
     */
    @Override
    public Response<Map<String, Object>> checkHasParticipate(String date, Long partyId) {
        Response<Map<String, Object>> resp = new Response<>(null);
        resp.setFacade(FacadeEnums.OK);
        Map<String, Object> result = coreRedPacketService.checkHasParticipate(date, String.valueOf(partyId));
        resp.setResult(result);
    /*	if(coreRedPacketService.checkHasParticipate(date, String.valueOf(partyId))){
			resp.setResult(Boolean.TRUE);
		}else{
			resp.setResult(Boolean.FALSE);
		}*/
        return resp;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.RedPacketService#killRedPacket(com.xianglin.appserv.common.service.facade.model.Request)
     */
    @Override
    public Response<RedPacketVo> killRedPacket(Request<RedPacketVo> req) {
        Response<RedPacketVo> resp = new Response<>(null);
        if (req.getReq() == null) {
            resp.setCode(FacadeEnums.E_C_INPUT_INVALID.code);
            resp.setMemo("抢购失败");
            resp.setTips("抢购失败");
            return resp;
        }

        RedPacketVo vo = req.getReq();
        vo.setOrderNumber(coreRedPacketService.getSeqNumber());

        vo.setTransactionStatus(Constant.TransactionStatus.SUCCESS.name());
        Boolean flag = coreRedPacketService.consumeRedPacket(vo);
        //如果抢到，更细数据成功，并且时现金券，调用资金账户
        resp.setResult(vo);
        if (flag) {
            resp.setFacade(FacadeEnums.OK);
            if (Constant.RedPacketType.CASH.name().equals(vo.getType())) {
                try {
                    cashRedPacketInAccount(req);
                } catch (Exception e) {
                    logger.error("入账失败，req:{}", e, req);
                }
            }
        } else {
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
            resp.setTips("红包已抢完");
            resp.setMemo("红包已抢完");
        }

        return resp;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.RedPacketService#remainsNum(String)
     */
    @Override
    public Response<Integer> remainsNum(String date) {
        Response<Integer> resp = new Response<>(null);
        if (date == null) {
            date = DateUtils.formatDate(DateUtils.getNow(), DateUtils.DATE_TPT_TWO);
        }
        int num = coreRedPacketService.remaindNum(date);

        if (num < 0) {
            num = 0;
        }
        resp.setResult(Integer.valueOf(num));
        return resp;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.RedPacketService#isActivityStatus(String)
     */
    @Override
    public Response<Map<String, Object>> isActivityStatus(String date) {
        Response<Map<String, Object>> resp = new Response<>(null);
        resp.setFacade(FacadeEnums.OK);
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(date)) {
            date = DateUtils.formatDate(DateUtils.getNow(), DateUtils.DATE_TPT_TWO);
        }
        String value = coreRedPacketService.get(RedPacketService.RED_ACTIVITY_FLAG, date);
        if (Constant.ActivityStatus.READY.name().equals(value)) {
            resp.setResonpse(ResponseEnum.RED_PACKET_NOT_START);
        } else if (Constant.ActivityStatus.END.name().equals(value)) {
            resp.setResonpse(ResponseEnum.RED_PACKET_EMPTY);
        } else if (StringUtils.isEmpty(value)) {
            Date now = DateUtils.getNow();
            int seconds = DateUtils.getIntervalSeconds(now, DateUtils.getLastestTimeOfDay(now));
//			coreRedPacketService.add(RedPacketService.RED_ACTIVITY_DESC, date, Constant.ActivityResultDesc.TOMORROR.getDesc(), seconds);
            coreRedPacketService.add(RedPacketService.RED_ACTIVITY_DESC, date, SysConfigUtil.getStr(Constant.BusiVisitKey.activity_tomorror_desc.code, PropertiesUtil.getProperty("activity.tomorror.desc")), seconds);
            coreRedPacketService.add(RedPacketService.RED_ACTIVITY_FLAG, date, Constant.ActivityStatus.END.name(), seconds);
        }
        boolean status = coreRedPacketService.isActivityStart(date);
        map.put("activityDesc", coreRedPacketService.get(RedPacketService.RED_ACTIVITY_DESC, date));
        String number = coreRedPacketService.get(RedPacketService.RED_PACKET_TOTAL_NUM, date);
        map.put("number", ((number != null && Integer.valueOf(number) > 0) && status) ? number : SysConfigUtil.getStr(Constant.BusiVisitKey.red_packet_total_num.code, PropertiesUtil.getProperty(RedPacketService.RED_PACKET_TOTAL_NUM)));//活动正常显示缓存中的
        map.put("status", status);
        resp.setResult(map);
        return resp;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.RedPacketService#getParticipateUser(com.xianglin.appserv.common.service.facade.model.Request)
     */
    @Override
    public Response<List<RedPacketVo>> getParticipateUser(Request<RedPacketVo> req) {

        Response<List<RedPacketVo>> resp = new Response<>(null);
        resp.setFacade(FacadeEnums.OK);

        List<RedPacketVo> list = coreRedPacketService.getParticipateUser(req.getReq());
        if (CollectionUtils.isNotEmpty(list)) {
            resp.setResult(list);
        } else {
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.RedPacketService#nextAvailable(java.lang.String)
     */
    @Override
    public Response<RedPacketVo> nextAvailable(String date) {
        Response<RedPacketVo> resp = new Response<>(null);
        resp.setFacade(FacadeEnums.OK);
        if (date == null) {
            resp.setResonpse(ResponseEnum.RED_PACKET_EMPTY);
            return resp;
        }
        Long id = coreRedPacketService.nextAvailable(date);

        RedPacketVo vo = coreRedPacketService.detailInfo(id, date);

        if (vo == null) {
            resp.setResonpse(ResponseEnum.RED_PACKET_EMPTY);
            return resp;
        }
        resp.setResult(vo);
        return resp;
    }

    @Override
    public Response<RedPacketVo> addRedPacket(Request<RedPacketVo> req) {
        Response<RedPacketVo> resp = new Response<RedPacketVo>(null);
        resp.setFacade(FacadeEnums.OK);
        if (req == null || req.getReq() == null) {
            resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
        } else {

            RedPacketVo vo = req.getReq();
            try {
                RedPacketPool pool = DTOUtils.map(vo, RedPacketPool.class);
                poolDAO.insert(pool);
                vo.setId(pool.getId());
                resp.setResult(vo);
            } catch (Exception e) {
                logger.error("插入红包/优惠券失败", e);
                resp.setResonpse(ResponseEnum.RED_PACKET_ERROR);
            }
        }
        return resp;

    }

    @Override
    public Response<Map<String, Double>> getRate(String key) {
        Response<Map<String, Double>> resp = new Response<Map<String, Double>>(null);
        resp.setFacade(FacadeEnums.OK);
        Map<String, Double> map = new HashMap<>();
        try {
            if (null == key) {
                String cash_scale = SysConfigUtil.getStr(Constant.BusiVisitKey.cash_red_packet_scale.code, PropertiesUtil.getProperty(RedPacketService.CASH_RED_PACKET_SCALE));
                String coupon_scale = SysConfigUtil.getStr(Constant.BusiVisitKey.cash_coupon_packet_scale.code, PropertiesUtil.getProperty(RedPacketService.COUPON_RED_PACKET_SCALE));
                if (StringUtils.isNoneEmpty(cash_scale)) {
                    map.put(RedPacketService.CASH_RED_PACKET_SCALE, Double.valueOf(cash_scale));
                }
                if (StringUtils.isNoneEmpty(coupon_scale)) {
                    map.put(RedPacketService.COUPON_RED_PACKET_SCALE, Double.valueOf(coupon_scale));
                }
            } else {
                map.put(key, Double.valueOf(SysConfigUtil.getStr(key, key)));
            }
        } catch (Exception e) {
            resp.setFacade(FacadeEnums.E_S_DATA_ERROR);
        }
        resp.setResult(map);
        return resp;
    }

    public SessionHelper getSessionHelper() {
        return sessionHelper;
    }

    public void setSessionHelper(SessionHelper sessionHelper) {
        this.sessionHelper = sessionHelper;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.RedPacketService#getRedPacketList(com.xianglin.appserv.common.service.facade.model.Request)
     */
    @Override
    public Response<List<RedPacketVo>> getRedPacketList(Request<RedPacketVo> req) {
        Response<List<RedPacketVo>> resp = new Response<>(null);

        resp.setFacade(FacadeEnums.OK);
        try {
            List<RedPacketVo> list = coreRedPacketService.getRedPacketList(req.getReq());
            if (CollectionUtils.isNotEmpty(list)) {
                resp.setResult(list);
            } else {
                resp.setFacade(FacadeEnums.RETURN_EMPTY);
            }
        } catch (Exception e) {
            logger.error("查询红包时出现问题，", e);
            resp.setFacade(FacadeEnums.E_S_DATA_ERROR);
        }

        return resp;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.RedPacketService#cashRedPacketInAccount(com.xianglin.appserv.common.service.facade.model.Request)
     */
    @Override
    public Response<Boolean> cashRedPacketInAccount(Request<RedPacketVo> req) {
        Response<Boolean> resp = new Response<>(null);
        if (req == null || req.getReq() == null) {
            resp.setResult(false);
        } else {
            RedPacketVo vo = req.getReq();
            AccountBalChangeReq accountReq = new AccountBalChangeReq();
            String requestNum = coreRedPacketService.getSeqNumber();
            accountReq.setAccountBalType(AccountBalTypeEnums.ACCOUNT_BAL_PRINCIPAL.code);
            accountReq.setOrderRequest(requestNum);
            accountReq.setPartyId(vo.getPartyId() + "");
            accountReq.setOrderNumber(vo.getOrderNumber());
            accountReq.setOrderSubNumber(vo.getOrderNumber());
            accountReq.setTransactionAmount(NumberUtil.toString(vo.getAmount()));
            accountReq.setUserType(AccountBizTypeEnums.NODE_MANAGER_TYPE.code);
            if (Constant.RedPacketType.CASH.name().equals(vo.getType()) || Constant.RedPacketType.LUCKWHEEL_RED_PACKET.name().equals(vo.getType())) {
                accountReq.setTransactionType(TransactionTypeEnums.TRANS_TYPE_100003.code);
                accountReq.setMemo(TransactionTypeEnums.TRANS_TYPE_100003.msg);
            } else if (Constant.RedPacketType.FRESH_RED_PACKET.name().equals(vo.getType())) {
                accountReq.setTransactionType(TransactionTypeEnums.TRANS_TYPE_100004.code);
                accountReq.setMemo(TransactionTypeEnums.TRANS_TYPE_100004.msg);
            }
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
            accountReq.setCreator(vo.getPartyId() + "");
            com.xianglin.te.common.service.facade.resp.Response<AccountBalChangeResp> accountResp = transferServiceClient.accountBalChange(accountReq);
            logger.debug("accountResp {}:", accountResp);

            if (FacadeEnums.OK.code == accountResp.getCode()) {
                resp.setResult(true);
            } else {
                resp.setResult(false);
            }
        }
        return resp;
    }

    /**
     * 红包领取
     */
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.RedPacketService.cashRedPacket", needLogin = true, description = "红包领取")
    @Override
    public Response<RedPacketVo> cashRedPacket(Long partyId) {

        logger.info("新人红包领取开始{}", partyId);
        Session session = sessionHelper.getSession();
        String session_partyId = session.getAttribute(SessionConstants.PARTY_ID);
        Response<RedPacketVo> resp = ResponseUtils.successResponse();
        logger.info("当前登陆用户partyId:{} 新人红包领取开始传入{}", session_partyId, partyId);

        if (StringUtils.isEmpty(session_partyId)) {
            resp.setFacade(FacadeEnums.E_S_INVALID_SESSION);
            resp.setMemo("用户未登录");
            resp.setTips("用户未登录");
            return resp;
        }
        if (!StringUtils.equals(partyId.toString(), session_partyId)) {
            resp.setFacade(FacadeEnums.E_S_INVALID_SESSION);
            resp.setMemo("用户操作非法");
            resp.setTips("用户操作非法");
            return resp;
        }
        partyId = Long.valueOf(session_partyId);
        boolean b = coreRedPacketService.isRepeat(FRESH_RED_PACKET + partyId, 10);
        if (b) {
            logger.info("{} 重复请求", partyId);
            resp.setResonpse(ResponseEnum.RED_PACKET_PARTICIPATE);
            resp.setTips("操作频繁，请稍后");
            resp.setMemo("操作频繁，请稍后");
            return resp;
        }
        Request<RedPacketVo> req = new Request<>();
        RedPacketVo vo = new RedPacketVo();
        RedPacketVo redVo = new RedPacketVo();

        redVo.setPartyId(partyId);
        redVo.setType(Constant.RedPacketType.FRESH_RED_PACKET.name());


        if (partyId == null) {
            resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
        } else {
            List<RedPacketVo> list = coreRedPacketService.getRedPacketList(redVo);
            if (!CollectionUtils.isEmpty(list)) {

                logger.info("{}已经领过", partyId);
                resp.setResonpse(ResponseEnum.RED_PACKET_PARTICIPATE);
                resp.setTips("您已经领过了");
                resp.setMemo("您已经领过了");
            } else {
                String orderNumber = coreRedPacketService.getSeqNumber();
                // 调用资金账户系统
                // vo.setAmount(new BigDecimal("2.00"));
                vo.setOrderNumber(orderNumber);
                vo.setType(Constant.RedPacketType.FRESH_RED_PACKET.name());
                AccountNodeManagerVo managerVo =
                        sessionHelper.getSession().getAttribute(SessionConstants.XL_QY_USER,
                                AccountNodeManagerVo.class);
                vo.setAmount(new BigDecimal(SysConfigUtil.getStr(Constant.BusiVisitKey.fresh_redpacket_amount.code, "2")));
                vo.setAccountName(managerVo.getMobilePhone());
                vo.setUserName(managerVo.getTrueName());
                vo.setPartyId(partyId);
                vo.setDescription("新用户现金红包");
                vo.setEffectiveDate(new Date());
                vo.setCreateDate(new Date());
                vo.setUpdateDate(new Date());
                vo.setStatus(Constant.RedPacketStatus.USED.name());
                vo.setIsDeleted("0");
                vo.setTransactionStatus(Constant.TransactionStatus.SUCCESS.name());
                req.setReq(vo);
                Response<RedPacketVo> response = this.addRedPacket(req);
                if (FacadeEnums.OK.code == response.getCode()) {
                    RedPacketVo result = response.getResult();
                    Response<Boolean> core = this.cashRedPacketInAccount(req);
                    if (core.getResult() != null || core.getResult()) {
                        result.setIsDeleted("0");
                        result.setStatus(Constant.RedPacketStatus.USED.name());// 已使用
                    } else {
                        result.setIsDeleted("1");
                        result.setStatus(Constant.RedPacketStatus.EXPIRED.name());// 失效
                    }

                    if ("1".equals(result.getIsDeleted())) {
                        RedPacketPool pool = new RedPacketPool();
                        pool.setId(result.getId());
                        pool.setIsDeleted(result.getIsDeleted());
                        pool.setStatus(result.getStatus());
                        poolDAO.updateByPrimaryKeySelective(pool);
                    }

                }
				/*Response<Boolean> core = this.cashRedPacketInAccount(req);
				if (core.getResult() != null || core.getResult()) {
					vo.setIsDeleted("0");
					vo.setStatus(Constant.RedPacketStatus.USED.name());// 已使用
				} else {
					vo.setIsDeleted("1");
					vo.setStatus(Constant.RedPacketStatus.EXPIRED.name());// 失效
				}*/


            }
        }
        resp.setResult(vo);
        return resp;
    }

    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.RedPacketService.getPointRushList", needLogin = true, description = "获取整点抢红包用户列表")
    @Override
    public Response<PointRushDTO> getPointRushList(Request<RedPacketVo> req) {
        Response<PointRushDTO> resp = ResponseUtils.successResponse();
        try {
            PointRushDTO dto = new PointRushDTO();
            List<PointRushVo> pVos = new ArrayList<>();

            boolean activityFlag = coreRedPacketService.isActivityStart(DateUtils.formatDate(DateUtils.getNow(), DateUtils.DATE_TPT_TWO));//判断活动是否开启
            dto.setActivityFlag(activityFlag);
            dto.setRedPacketUrl(SysConfigUtil.getStr(RED_PACKET_URL));
            if (!activityFlag) {
                List<StationVo> stationVos = new ArrayList<>();
                try {
                    PointRushVo vo = null;

                    //查询推荐成功推送
                    Set<String> set = new HashSet();
                    //Set<String> set = redisUtil.smembers(redisUtil.ACTIVITY_INVITE_POINT_ALERT);
                    if (CollectionUtils.isNotEmpty(set)) {
                        Set<String> expireSet = new HashSet<>();
                        for (String s : set) {
                            vo = JSON.parse(s, PointRushVo.class);
                            if (vo.getExpireTime() != 0 && vo.getExpireTime() < System.currentTimeMillis()) {//过期消息
                                expireSet.add(s);
                            } else {
                                pVos.add(vo);
                            }
                        }
                        //删除过期消息
                        if (expireSet.size() > 0) {
                            redisUtil.srem(redisUtil.ACTIVITY_INVITE_POINT_ALERT, expireSet.toArray(new String[0]));
                        }
                    }

                    if (CollectionUtils.isNotEmpty(dto.getStations())) {
                        for (StationVo s : dto.getStations()) {
                            vo = new PointRushVo();
                            vo.setPointRush(s.getName());
                            vo.setPushActive("https://h5.xianglin.cn/home/nodeManager/guangfu.html");//目前固定
                            pVos.add(vo);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(dto.getNewStatiosList())) {
                        for (StationVo s : dto.getStations()) {
                            vo = new PointRushVo();
                            vo.setPointRush(s.getName());
                            vo.setPushActive("https://h5.xianglin.cn/home/nodeManager/guangfu.html");//目前固定
                            pVos.add(vo);
                        }
                    }

                } catch (Exception e) {
                    logger.error("调用光伏接口失败,{}", e);
                }
            } else {
                RedPacketVo vo = new RedPacketVo();

                vo.setEffectiveDate(new Date());
                vo.setStatus(Constant.RedPacketStatus.USED.name());
                vo.setTransactionStatus(Constant.TransactionStatus.SUCCESS.name());
                try {
                    List<RedPacketVo> redPackList = coreRedPacketService.getParticipateUser(vo);
                    for (RedPacketVo rvo : redPackList) {
                        PointRushVo pv = new PointRushVo();
                        if (rvo.getUserName() != null) {
                            if (Constant.RedPacketType.COUPON.name().equals(rvo.getType())) {
                                pv.setPointRush(rvo.getUserName().substring(0, 1) + "*抢到" + rvo.getAmount() + "元电商优惠券");
                            } else {
                                pv.setPointRush(rvo.getUserName().substring(0, 1) + "*抢到" + rvo.getAmount() + "元红包");
                            }
                            pv.setPushActive(SysConfigUtil.getStr(RED_PACKET_URL));
                            pVos.add(pv);
                        }
                    }
                } catch (Exception e) {
                    logger.info("查询整点抢红包列表异常,{}", e);
                }
            }

            dto.setpVosList(pVos);
            resp.setResult(dto);
        } catch (Exception e) {
            logger.error("getPointRushList", e);
        }
        return resp;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.RedPacketService#decreaseTotal(java.lang.String, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public Response<Boolean> decreaseTotal(String dateStr, Integer step, Integer assertNum) {
        Response<Boolean> response = new Response<>(null);
        response.setFacade(FacadeEnums.OK);
        logger.debug("before consume remain num {}", coreRedPacketService.get(RedPacketService.RED_PACKET_TOTAL_NUM, dateStr));
        boolean flag = coreRedPacketService.isReady(RED_PACKET_TOTAL_NUM, dateStr, step, assertNum, 15 * 60);
        logger.debug("after consume remain num {}, flag {}", coreRedPacketService.get(RedPacketService.RED_PACKET_TOTAL_NUM, dateStr), flag);

        if (!flag) {//此方法在并发大出现重复调用，seconds会延迟一会
            Date now = DateUtils.getNow();
            int seconds = DateUtils.getIntervalSeconds(now, DateUtils.getLastestTimeOfDay(now));
            coreRedPacketService.deleteCache(dateStr);
            coreRedPacketService.add(RedPacketService.RED_ACTIVITY_FLAG, dateStr, Constant.ActivityStatus.END.name(), seconds);
//			coreRedPacketService.add(RedPacketService.RED_ACTIVITY_DESC, dateStr,  Constant.ActivityResultDesc.TOMORROR.getDesc(), seconds);
            //活动进行中的时候，使用缓存中的，如果是初始化等操作使用实时查询数据库
            coreRedPacketService.add(RedPacketService.RED_ACTIVITY_DESC, dateStr, SysConfigUtil.getStr(Constant.BusiVisitKey.activity_tomorror_desc.code, PropertiesUtil.getProperty("activity.tomorror.desc")), seconds);
            response.setResonpse(ResponseEnum.RED_PACKET_EMPTY);
        }
        response.setResult(flag);
        return response;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.RedPacketService#isCashRedPacket()
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.RedPacketService.isCashRedPacket", needLogin = true, description = "查询是否开启新手红包")
    public Response<Boolean> isCashRedPacket() {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            if ("true".equals(SysConfigUtil.getStr(Constant.BusiVisitKey.activity_fresh_red_packet_switch.code, "false"))) {
                //1. 查询是否领取过红包
                RedPacketVo reqVo = new RedPacketVo();
                reqVo.setPartyId(sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class));
                reqVo.setType(Constant.RedPacketType.FRESH_RED_PACKET.name());
                List<RedPacketVo> list = coreRedPacketService.getRedPacketList(reqVo);
                if (CollectionUtils.isEmpty(list)) {
                    resp.setResult(true);
                }
            }
        } catch (Exception e) {
            logger.error("isCashRedPacket，", e);
            resp.setFacade(FacadeEnums.E_S_DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.RedPacketService.sendRedPacket", needLogin = true, description = "发送聊天红包")
    public Response<Long> sendRedPacket(RedPacketVo redPacket, String password) {
        Response<Long> resp = ResponseUtils.successResponse();
        try {
            logger.info("redPacket {},password:{}", redPacket, password);
            /*1,校验密码，
             *2，进行账户处理
             */
            if (StringUtils.isEmpty(password)) {
                resp.setFacade(FacadeEnums.PWD_EMPTY);
            } else {
                Session session = sessionHelper.getSession();
                logger.info("check passwd sessionId {}", session.getId());
                Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
                boolean isRealName = userManager.queryAndSynRealName(partyId);
                if (!isRealName) {
                    resp.setResultEnum(ResultEnum.UnAuth);
                    return resp;
                }
                com.xianglin.cif.common.service.facade.model.Response<Boolean> pwdResp = partyAttrAccountServiceClient.checkTradePwd(password, session.getId());
                if (!pwdResp.getResult()) {
                    resp.setCode(pwdResp.getCode());
                    resp.setTips(pwdResp.getTips());
                    return resp;
                } else {
                    //初始化红包
                    redPacket.setSendPartyId(partyId);
                    redPacket.setType(Constant.RedPacketType.CHAT_USER_REDPACKET.name());
                    redPacket.setOrderNumber(coreRedPacketService.getSeqNumber());
                    redPacket.setAccountName(sessionHelper.getSessionProp(SessionConstants.LOGIN_NAME, String.class));
                    redPacket.setEffectiveDate(new Date());
                    redPacket.setCreateDate(new Date());
                    redPacket.setUpdateDate(new Date());
                    redPacket.setStatus(Constant.RedPacketStatus.UNUSED.name());
                    redPacket.setIsDeleted("0");
                    redPacket.setTransactionStatus(Constant.TransactionStatus.PROCESS.name());
                    redPacket = coreRedPacketService.addRedPacket(redPacket);
                    //调用接口转账
                    AcctChangeUpTe te = new AcctChangeUpTe();
                    te.setMemberCode(redPacket.getSendPartyId() + "");
                    te.setToMemberCode(redPacket.getPartyId() + "");
                    te.setTransactionType(TransactionTypeEnums.TRANS_TYPE_100013.code);//乡邻红包
                    te.setToTransactionType(TransactionTypeEnums.TRANS_TYPE_100012.code);
                    te.setOrderSeqId(redPacket.getOrderNumber());
                    te.setAmount(redPacket.getAmount());
                    com.xianglin.te.common.service.facade.resp.Response<Boolean> teResp = transferServiceClient.accountBalTransfer(te, redPacket.getOrderNumber(), redPacket.getOrderNumber());
                    logger.info("sendRedPacket result:{}", StringUtils.substring(teResp.toString(), 0, 200));
                    if (teResp.getResult()) {//转账成功
                        redPacket.setTransactionStatus(Constant.TransactionStatus.SUCCESS.name());
                        resp.setResult(redPacket.getId());
                    } else {//转账失败
                        redPacket.setTransactionStatus(Constant.TransactionStatus.FAIL.name());
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400003);
                    }
                    redPacket.setStatus(Constant.RedPacketStatus.USED.name());
                    coreRedPacketService.updatedRedPacket(redPacket);
                }
            }
        } catch (Exception e) {
            logger.error("sendRedPacket，", e);
            resp.setFacade(FacadeEnums.E_S_DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.RedPacketService.queryAcctBalance", needLogin = true, description = "查询资金账户余额")
    public Response<String> queryAcctBalance() {
        Response<String> resp = ResponseUtils.successResponse(null);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            boolean isRealName = userManager.queryAndSynRealName(partyId);
            if (!isRealName) {
                resp.setResultEnum(ResultEnum.UnAuth);
                return resp;
            }
            String amt = transferServiceClient.abQuery(partyId).getResult();
            resp.setResult(amt);
            if (StringUtils.isEmpty(amt)) {
                resp.setFacade(FacadeEnums.RETURN_EMPTY);
            }
        } catch (Exception e) {
            logger.error("queryAcctBalance，", e);
            resp.setFacade(FacadeEnums.E_S_DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.RedPacketService.queryRedPacketDetail", needLogin = true, description = "查询红包明细")
    public Response<RedPacketVo> queryRedPacketDetail(Long packetId) {
        Response<RedPacketVo> resp = ResponseUtils.successResponse(null);
        try {
            RedPacketVo vo = coreRedPacketService.queryById(packetId);
            resp.setResult(vo);
            if (vo == null) {
                resp.setFacade(FacadeEnums.RETURN_EMPTY);
            }
        } catch (Exception e) {
            logger.error("queryRedPacketDetail，", e);
            resp.setFacade(FacadeEnums.E_S_DATA_ERROR);
        }
        return resp;
    }
}
