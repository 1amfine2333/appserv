package com.xianglin.appserv.biz.service.implement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.biz.shared.LogManager;
import com.xianglin.appserv.biz.shared.MessageManager;
import com.xianglin.appserv.biz.shared.SysParaManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.AppRequestLog;
import com.xianglin.appserv.common.dal.dataobject.AppRequestLogDetail;
import com.xianglin.appserv.common.dal.dataobject.SystemConfigModel;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.app.ActivityInviteService;
import com.xianglin.appserv.common.service.facade.app.InterestService;
import com.xianglin.appserv.common.service.facade.model.*;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.ActivityInviteDetailVo;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.facade.req.ActivityInviteDetailReq;
import com.xianglin.appserv.common.service.integration.cif.PartyAttrAccountServiceClient;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.core.model.UserInterestInfo;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.model.CustomersDTO;
import com.xianglin.fala.session.Session;
import com.xianglin.finan.common.service.facade.FnTransServiceFacade;
import com.xianglin.finan.common.service.facade.ZyFnBusiReqServiceFacade;
import com.xianglin.finan.common.service.facade.ZyFnChannelSafeServiceFacade;
import com.xianglin.finan.common.service.facade.ZyFnProductServiceFacade;
import com.xianglin.finan.common.service.facade.dto.*;
import com.xianglin.finan.common.service.facade.dto.ZyFnProductQueryReqDTO;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.gateway.common.service.spi.util.GatewayRequestContext;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Describe :
 * Created by xingyali on 2017/10/23 15:01.
 * Update reason :
 */
@Service("interestService")
@ServiceInterface
public class InterestServiceImpl implements InterestService {

    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);
    private static final String successCode = "000000";//成功码
    private static final String errorCode = "100014,100019,100021,100008,100035"; //授权码已过期，请重新授权,重授权
    private static final String errorCode2 = "100021";//Token已过期，请通过code查token
    private static final String errorCode3 = "999999";//对方服务器在重启，直接返回客户端
    private static final String tranCode400105 = "400105"; //申购
    private static final String tranCode400106 = "400106"; //赎回


    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private SysParaManager sysParaManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private PartyAttrAccountServiceClient partyAttrAccountServiceClient;

    @Autowired
    private LogManager logManager;


    @Autowired
    private CustomersInfoService customersInfoService;

    @Autowired
    private ActivityInviteService activityInviteService;

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private ZyFnChannelSafeServiceFacade zyFnChannelSafeServiceFacade;

    @Autowired
    private ZyFnBusiReqServiceFacade zyFnBusiReqServiceFacade;

    @Autowired
    private ZyFnProductServiceFacade zyFnProductServiceFacade;

    @Autowired
    private FnTransServiceFacade fnTransServiceFacade;


    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.InterestService.currencyIntegerestLogin", description = "通用秒息宝接口，需要登录",timeout = 50000)
    public Response<String> currencyIntegerestLogin(String tranCode, String content) {
        Response<String> resp = ResponseUtils.successResponse();
        try {
            if(1 == 1){
                throw new BusiException("暂停服务");
            }
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String sessionId = sessionHelper.getSession().getId();
            if (partyId == null ){
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            String respones = null;
            User user = userManager.queryUser(partyId);
            //String content="{\"phone\":\"13402160951\",\"verCodeType\":\"A\"}";
            //先到session里面获取token和key
            if (user != null) {
                UserInterestInfo userInterestInfo = sessionHelper.getSessionProp("USER_INTEREST", UserInterestInfo.class);
                if(userInterestInfo != null){
                    if(!userInterestInfo.getOpenId().equals(partyId)){
                        accessToken(user.getLoginName(), user.getPartyId() + "");
                        userInterestInfo = sessionHelper.getSessionProp("USER_INTEREST", UserInterestInfo.class);
                    }
                }
                AppRequestLogDetail appRequestLogDetail = AppRequestLogDetail.builder().remark(tranCode).content(content+","+ ToStringBuilder.reflectionToString(userInterestInfo)).type(Constant.Log.REQ.name()).build();
                logManager.insertRequestLogDetail(appRequestLogDetail);
                AppRequestLog appRequestLog = AppRequestLog.builder().deviceId(deviceId).partyId(partyId).requestId(Thread.currentThread().getName()).reqId(appRequestLogDetail.getId()).operationType("com.xianglin.appserv.common.service.facade.app.InterestService.currencyIntegerestLogin").sessionId(sessionId).reqTimestamp(System.currentTimeMillis()).build();
                logManager.insertRequestLog(appRequestLog);
                String code = SysConfigUtil.getStr("interest_authorizeCode");
                for (int i = 0; i < 3; i++) {
                    if(StringUtils.isEmpty(code)){
                        accessToken(user.getLoginName(), user.getPartyId() + "");
                        userInterestInfo = sessionHelper.getSessionProp("USER_INTEREST", UserInterestInfo.class);
                    }
                    if (userInterestInfo == null) {
                        //获取token，将token放到session中
                        String respGetToken = TokenUtils.accessToken(code, user.getLoginName(), partyId + "");
                        if (StringUtils.isNotEmpty(respGetToken)) {
                            JSONObject objectToken = JSON.parseObject(respGetToken);
                            if (objectToken.get("errorCode").toString().equals(successCode)) {
                                userInterestInfo = getUserInterestInfo(objectToken, user.getLoginName(), user.getPartyId() + "");
                            } else if (StringUtils.contains(errorCode,objectToken.get("errorCode").toString())) { //授权码已过期，请重新授权,重新查询
                                accessToken(user.getLoginName(), user.getPartyId() + "");
                                userInterestInfo = sessionHelper.getSessionProp("USER_INTEREST", UserInterestInfo.class);
                            } else if (StringUtils.contains(errorCode2,objectToken.get("errorCode").toString())) { getToken(code, user.getLoginName(), user.getPartyId()+"");
                            }else if (StringUtils.contains(errorCode3,objectToken.get("errorCode").toString())) {
                                resp.setResult(respones);
                                break;
                            }else{
                                accessToken(user.getLoginName(), user.getPartyId() + "");
                                userInterestInfo = sessionHelper.getSessionProp("USER_INTEREST", UserInterestInfo.class);
                            }
                        }
                    }
                    logger.info("userInterestInfo: " + userInterestInfo);
                    if(userInterestInfo != null){
                        respones = TokenUtils.requestCurrency(tranCode, content, userInterestInfo.getAccessToken(), userInterestInfo.getKey());
                        if(respones != null){
                            if(StringUtils.contains(respones,"errorCode")){ //当返回的结果不是加密的字符串时，进行处理
                                logger.info("currencyIntegerestLogin respones: " + respones);
                                JSONObject object = JSON.parseObject(respones);
                                if (object.get("errorCode").toString().equals(successCode)) {
                                    //判断是否是申购或赎回，如果是查询是否有推荐关系，如果有发送推送给推荐人
                                    if(StringUtils.contains(tranCode,tranCode400105) || StringUtils.contains(tranCode,tranCode400106)){
                                        logger.info("sendInviteMsg tranCode :");
                                        sendMsg(partyId,tranCode,content);
                                    }
                                    resp.setResult(respones);
                                    break;
                                } else if (StringUtils.contains(errorCode,object.get("errorCode").toString())) { //授权码已过期，请重新授权,重授权
                                    accessToken(user.getLoginName(), user.getPartyId() + "");
                                } else if (StringUtils.contains(errorCode2,object.get("errorCode").toString())) {  //accessToken已过期，请通过根据code获取token
                                    getToken(code, user.getLoginName(), partyId + "");
                                }else {
                                    resp.setResult(respones);
                                    break;
                                }
                            } else {
                                respones = AESUtils.decrypt(respones, userInterestInfo.getKey());
                                logger.info("currencyIntegerestLogin respones: " + respones);
                                if (StringUtils.isNotEmpty(respones)) {
                                    JSONObject object = JSON.parseObject(respones);
                                    if (object.get("errorCode").toString().equals(successCode)) {
                                        //判断是否是申购或赎回，如果是查询是否有推荐关系，如果有发送推送给推荐人
                                        if(StringUtils.contains(tranCode,tranCode400105) || StringUtils.contains(tranCode,tranCode400106)){ //申购
                                            sendMsg(partyId,tranCode,content);
                                        }
                                        resp.setResult(respones);
                                        break;
                                    } else if (StringUtils.contains(errorCode,object.get("errorCode").toString())) { //授权码已过期，请重新授权,重授权
                                        accessToken(user.getLoginName(), user.getPartyId() + "");
                                    } else if (StringUtils.contains(errorCode2,object.get("errorCode").toString())) {  //accessToken已过期，请通过根据code获取token
                                        getToken(code, user.getLoginName(), partyId + "");
                                    } else {
                                        resp.setResult(respones);
                                        break;
                                    }
                                }
                            }
                        }

                    }
                }
                AppRequestLogDetail appRequestLogDetailResp = AppRequestLogDetail.builder().type(Constant.Log.RESP.name()).remark(tranCode).content(respones+","+ToStringBuilder.reflectionToString(userInterestInfo)).build();
                logManager.insertRequestLogDetail(appRequestLogDetailResp);
                logManager.updateRequestLog(AppRequestLog.builder().id(appRequestLog.getId()).respId(appRequestLogDetailResp.getId()).respTimestamp(System.currentTimeMillis()).build());
            } else {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400041);
                return resp;
            }

        } catch (Exception e) {
            logger.error("currencyIntegerestLogin error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 先查用户是否有推荐人，如果有，发推送
     * @param partyId
     * @param tranCode
     */
    private void sendMsg(Long partyId, String tranCode,String content) {
        try {
            ActivityInviteDetailReq activityInviteDetailReq=new ActivityInviteDetailReq();
            activityInviteDetailReq.setPartyId(partyId);
            activityInviteDetailReq.setStatus("S");
            Response<List<ActivityInviteDetailVo>> list = activityInviteService.inviteDetailByParas(activityInviteDetailReq);
            if(list.getResult() != null && list.getResult().size()>0){    //有推荐人发送推送
                User user =userManager.queryUser(partyId);
                if(user != null){
                    String type="";
                    String amount ="";
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
                    String today = formatter.format(new Date());
                    if(tranCode.equals(tranCode400105)){
                        if(StringUtils.isNotEmpty(content)){
                            JSONObject jsonObject =JSON.parseObject(content);
                            amount = jsonObject.get("amount").toString();
                        }
                        type="好友"+user.getTrueName()+"于"+today+"申购了一笔"+amount+"元秒息宝，您的秒息宝时点余额增加了，请注意您的秒息宝金币奖励。";
                    }
                    if(tranCode.equals(tranCode400106)){
                        if(StringUtils.isNotEmpty(content)){
                            JSONObject jsonObject =JSON.parseObject(content);
                            amount = jsonObject.get("applicationVol").toString();
                        }
                        type="好友"+user.getTrueName()+"于"+today+"赎回了一笔"+amount+"元秒息宝，您的秒息宝时点余额减少了，请注意您的秒息宝金币奖励。";
                    }
                    List<Long> partyIds = new ArrayList<>(1);
                    partyIds.add(list.getResult().get(0).getRecPartyId());
                    messageManager.sendMsg(MsgVo.builder().partyId(list.getResult().get(0).getRecPartyId()).msgTitle("金币奖励").isSave(Constant.YESNO.YES)
                            .message(type).msgType(Constant.MsgType.INTEREST_TIP.name()).loginCheck(Constant.YESNO.NO.code).passCheck(Constant.YESNO.NO.code).expiryTime(0).isDeleted("N").msgSource(Constant.MsgType.INTEREST_TIP.name()).build(), partyIds);
                }
            }
        } catch (Exception e) {
            logger.error("sendMsg error", e);
        }
    }

    @Override
    public Response<String> hCurrencyIntegerestLogin(String tranCode, String content ,String sessionId) {
        Response<String> resp = ResponseUtils.successResponse();
        try {
            if(1 == 1){
                throw new BusiException("暂停服务");
            }
            if (sessionId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            Session session = sessionHelper.getSessionById(sessionId);
            sessionHelper.saveSession(session);
            Response<String> response = this.currencyIntegerestLogin(tranCode,content);
            resp.setResult(response.getResult());
        } catch (Exception e) {
            logger.error("hCurrencyIntegerestLogin error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }



    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.InterestService.currencyIntegerest", description = "通用秒息宝接口，不需要登录")
    public Response<String> currencyIntegerest(String tranCode, String content) {
        Response<String> resp = null;
        try {
            if(1 == 1){
                throw new BusiException("暂停服务");
            }
            if(tranCode.equals("400101")){
                String partyIds = SysConfigUtil.getStr("interest_default_partyId");
                if(partyIds != null){
                    long partyId = Long.valueOf(partyIds);
                    String sessionId="123344gfbljnlklkl";
                    String deviceId="jfaisefnksdikkkkkk";
                    Session session = sessionHelper.getSessionFromRedis(sessionId);
                    if(session == null){
                        session = sessionHelper.createRedisSession(sessionId);
                    }
                    session.setAttribute(SessionConstants.PARTY_ID,partyId);
                    session.setAttribute(SessionConstants.DEVICE_ID,deviceId);
                    session.setMaxInactiveIntervalInSeconds(7*24*3600);
                    sessionHelper.saveSession(session);
                    GatewayRequestContext.setSessionId(sessionId);
                    resp = this.currencyIntegerestLogin(tranCode, content);
                }
            }
        } catch (Exception e) {
            logger.error("currencyIntegerest error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.InterestService.confirmPassword", description = "交易密码确认")
    public Response<Boolean> confirmPassword(String password) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID,Long.class);
            if(partyId == null){
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            Session session = sessionHelper.getSession();
            com.xianglin.cif.common.service.facade.model.Response<Boolean> pwdResp = partyAttrAccountServiceClient.checkTradePwd(password,session.getId());
            if(pwdResp != null && pwdResp.getCode()==200000){
                resp.setResult(pwdResp.getResult());
                resp.setTips(pwdResp.getTips());
            } else {
                resp.setResult(pwdResp.getResult());
                resp.setTips(pwdResp.getTips());
                resp.setCode(pwdResp.getCode());
            }
        } catch (Exception e) {
            logger.error("confirmPassword error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.InterestService.confirmUserNameAndIdNumber", description = "身份证姓名确认")
    public Response<Boolean> confirmUserNameAndIdNumber(String userName, String idNumber) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID,Long.class);
            if(partyId == null){
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            if(userName == null || idNumber == null){
                resp.setResonpse(ResponseEnum.PARAM_INVALD);
                return resp;
            }
            /*PrincipalDTO dto = new PrincipalDTO();
            dto.setPartyId(partyId);
            com.xianglin.cif.common.service.facade.model.Response<PartyAttrRealnameauthVo> response = authenticationService.queryAuthLevelByPartyId(dto);*/
            com.xianglin.cif.common.service.facade.model.Response<CustomersDTO>  response =customersInfoService.selectCustomsAlready2Auth(partyId);
            logger.info("confirmUserNameAndIdNumber:"+response.toString());
            String credentialsNumber = null;
            String credentialsName = null;
            if (response.getResult() != null) {
                if (StringUtils.isNotEmpty(response.getResult().getCredentialsNumber())) {
                    credentialsNumber = response.getResult().getCredentialsNumber();
                    credentialsName = response.getResult().getCustomerName();
                }
            }
            if (userName.equals(credentialsName) && idNumber.equals(credentialsNumber)){
                resp.setResult(true);
            }
        } catch (Exception e) {
            logger.error("confirmUserNameAndIdNumber error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.InterestService.busiRequest", description = "中原银行业务通用接口")
    public Response<String> busiRequestStr(String trxnCode,String content) {
        Response<String> resp = ResponseUtils.successResponse();
        try {
            if(1 == 1){
                throw new BusiException("暂停服务");
            }
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            User user = userManager.queryUser(partyId);
            if(user == null){
                resp.setFacade(FacadeEnums.ERROR_CHAT_400041);
                return resp;
            }
            //将请求参数保存在applog
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String sessionId = sessionHelper.getSession().getId();
            AppRequestLogDetail appRequestLogDetail = AppRequestLogDetail.builder().remark(trxnCode).content(content+","+ ToStringBuilder.reflectionToString("")).type(Constant.Log.REQ.name()).build();
            logManager.insertRequestLogDetail(appRequestLogDetail);
            AppRequestLog appRequestLog = AppRequestLog.builder().deviceId(deviceId).partyId(partyId).requestId(Thread.currentThread().getName()).reqId(appRequestLogDetail.getId()).operationType("com.xianglin.appserv.common.service.facade.app.InterestService.busiRequest").sessionId(sessionId).reqTimestamp(System.currentTimeMillis()).build();
            logManager.insertRequestLog(appRequestLog);
            JSONObject jsonObj = JSON.parseObject(content);
            jsonObj.put("userIp",sessionHelper.getGatewayProp("remoteAddr"));
            ZyFnBusiReqDTO zyFnBusiReqDTO=new ZyFnBusiReqDTO();
            zyFnBusiReqDTO.setTrxnCode(trxnCode);
            zyFnBusiReqDTO.setContent(jsonObj.toJSONString());
            zyFnBusiReqDTO.setPartyId(partyId+"");
            zyFnBusiReqDTO.setMobilePhone(user.getLoginName());
            logger.info("busiRequestStr para:{}", zyFnBusiReqDTO.toString());
            ResponseDTO<String> mapResponseDTO = zyFnBusiReqServiceFacade.busiRequestStr(zyFnBusiReqDTO);
            logger.info("busiRequestStr result:{}", StringUtils.substring(mapResponseDTO.toString(),0,200));
            if(mapResponseDTO.isSuccess()){
                resp.setResult(mapResponseDTO.getResult());
                //将返回结果保存在applog
                AppRequestLogDetail appRequestLogDetailResp = AppRequestLogDetail.builder().type(Constant.Log.RESP.name()).remark(trxnCode).content(mapResponseDTO.getResult()+","+ToStringBuilder.reflectionToString("")).build();
                logManager.insertRequestLogDetail(appRequestLogDetailResp);
                logManager.updateRequestLog(AppRequestLog.builder().id(appRequestLog.getId()).respId(appRequestLogDetailResp.getId()).respTimestamp(System.currentTimeMillis()).build());
            }else {
                resp.setCode(Integer.valueOf(mapResponseDTO.getCode()));
                resp.setTips(mapResponseDTO.getTip());
            }
        } catch (Exception e) {
            logger.error("busiRequest error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.InterestService.preApplyChannelAccessToken", description = "预申请用户访问令牌信息")
    public Response<Object> preApplyChannelAccessToken() {
        Response<Object> resp = ResponseUtils.successResponse();
        try {
            if(1 == 1){
                throw new BusiException("暂停服务");
            }
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            User user = userManager.queryUser(partyId);
            if(user == null){
                resp.setFacade(FacadeEnums.ERROR_CHAT_400041);
                return resp;
            }
            //将请求参数保存在applog
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String sessionId = sessionHelper.getSession().getId();
            AppRequestLogDetail appRequestLogDetail = AppRequestLogDetail.builder().remark("预申请用户访问令牌信息").content("").type(Constant.Log.REQ.name()).build();
            logManager.insertRequestLogDetail(appRequestLogDetail);
            AppRequestLog appRequestLog = AppRequestLog.builder().deviceId(deviceId).partyId(partyId).requestId(Thread.currentThread().getName()).reqId(appRequestLogDetail.getId()).operationType("com.xianglin.appserv.common.service.facade.app.InterestService.preApplyChannelAccessToken").sessionId(sessionId).reqTimestamp(System.currentTimeMillis()).build();
            logManager.insertRequestLog(appRequestLog);

            ZyFnChannelAccessTokenReqDTO zyFnChannelAccessTokenReqDTO = new ZyFnChannelAccessTokenReqDTO();
            zyFnChannelAccessTokenReqDTO.setMobileNo(user.getLoginName());
            zyFnChannelAccessTokenReqDTO.setPartyId(partyId+"");
            logger.info("preApplyChannelAccessToken para:{}", zyFnChannelAccessTokenReqDTO.toString());
            ResponseDTO<Void> voidResponseDTO = zyFnChannelSafeServiceFacade.preApplyChannelAccessToken(zyFnChannelAccessTokenReqDTO);
            logger.info("preApplyChannelAccessToken result:{}", StringUtils.substring(voidResponseDTO.toString(),0,200));
            if(voidResponseDTO.isSuccess()){
                logger.info("预申请用户访问令牌信息 respones: " + voidResponseDTO.getResult());
                resp.setResult(voidResponseDTO.getResult());
                //将返回结果保存在applog
                AppRequestLogDetail appRequestLogDetailResp = AppRequestLogDetail.builder().type(Constant.Log.RESP.name()).remark("预申请用户访问令牌信息").content(voidResponseDTO.getResult()+","+ToStringBuilder.reflectionToString("")).build();
                logManager.insertRequestLogDetail(appRequestLogDetailResp);
                logManager.updateRequestLog(AppRequestLog.builder().id(appRequestLog.getId()).respId(appRequestLogDetailResp.getId()).respTimestamp(System.currentTimeMillis()).build());
            }else {
                resp.setCode(Integer.valueOf(voidResponseDTO.getCode()));
                resp.setTips(voidResponseDTO.getTip());
            }
        } catch (Exception e) {
            logger.error("preApplyChannelAccessToken error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.InterestService.getFnZyProductInfo", description = "理财基金产品信息查询")
    public Response<IZyFnProductInfoRespDTO> getFnZyProductInfo(IZyFnProductQueryReqDTO iZyFnProductQueryReqDTO) {
        Response<IZyFnProductInfoRespDTO> resp = ResponseUtils.successResponse();
        try {
            if(1 == 1){
                throw new BusiException("暂停服务");
            }
            IZyFnProductInfoRespDTO iZyFnProductInfoRespDTO = null;
            //将请求参数保存在applog
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String sessionId = sessionHelper.getSession().getId();
            AppRequestLogDetail appRequestLogDetail = AppRequestLogDetail.builder().remark("").content("理财基金产品信息查询").type(Constant.Log.REQ.name()).build();
            logManager.insertRequestLogDetail(appRequestLogDetail);
            AppRequestLog appRequestLog = AppRequestLog.builder().deviceId(deviceId).partyId(0L).requestId(Thread.currentThread().getName()).reqId(appRequestLogDetail.getId()).operationType("com.xianglin.appserv.common.service.facade.app.InterestService.getFnProductInfo").sessionId(sessionId).reqTimestamp(System.currentTimeMillis()).build();
            logManager.insertRequestLog(appRequestLog);
            ZyFnBusiReqDTO zyFnProductQueryReqDTOZyFnBusiReqDTO =new ZyFnBusiReqDTO();
            ZyFnProductQueryReqDTO ZY= DTOUtils.map(iZyFnProductQueryReqDTO,ZyFnProductQueryReqDTO.class);
            zyFnProductQueryReqDTOZyFnBusiReqDTO.setContent(ZY);
            logger.info("getFnZyProductInfo para:{}", zyFnProductQueryReqDTOZyFnBusiReqDTO.toString());
            ResponseDTO<ZyFnProductInfoRespDTO> fnProductInfo = zyFnProductServiceFacade.getFnZyProductInfo(zyFnProductQueryReqDTOZyFnBusiReqDTO);
            logger.info("getFnZyProductInfo result:{}", StringUtils.substring(fnProductInfo.toString(),0,200));
            if(fnProductInfo.isSuccess()){
                iZyFnProductInfoRespDTO= DTOUtils.map(fnProductInfo.getResult(),IZyFnProductInfoRespDTO.class);
                resp.setResult(iZyFnProductInfoRespDTO);
                //将返回结果保存在applog
                AppRequestLogDetail appRequestLogDetailResp = AppRequestLogDetail.builder().type(Constant.Log.RESP.name()).remark("").content(fnProductInfo.getResult()+","+ToStringBuilder.reflectionToString("")).build();
                logManager.insertRequestLogDetail(appRequestLogDetailResp);
                logManager.updateRequestLog(AppRequestLog.builder().id(appRequestLog.getId()).respId(appRequestLogDetailResp.getId()).respTimestamp(System.currentTimeMillis()).build());
            }else {
                resp.setCode(Integer.valueOf(fnProductInfo.getCode()));
                resp.setTips(fnProductInfo.getTip());
            }
        } catch (Exception e) {
            logger.error("getFnProductInfo error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.InterestService.rechargeMoney", description = "中原银行理财-用户充值")
    public Response<IBalanceOrderDTO> rechargeMoney(IFnRechargeAndWithdrawalsReqDTO iFnRechargeAndWithdrawalsReqDTO) {
        Response<IBalanceOrderDTO> resp = ResponseUtils.successResponse();
        try {
            if(1 == 1){
                throw new BusiException("暂停服务");
            }
            IBalanceOrderDTO iBalanceOrderDTO = null;
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null ){
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            User user = userManager.queryUser(partyId);
            if(user == null){
                resp.setFacade(FacadeEnums.ERROR_CHAT_400041);
                return resp;
            }
            //将请求参数保存在applog
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String sessionId = sessionHelper.getSession().getId();
            AppRequestLogDetail appRequestLogDetail = AppRequestLogDetail.builder().remark("用户充值").content(iFnRechargeAndWithdrawalsReqDTO.toString()).type(Constant.Log.REQ.name()).build();
            logManager.insertRequestLogDetail(appRequestLogDetail);
            AppRequestLog appRequestLog = AppRequestLog.builder().deviceId(deviceId).partyId(partyId).requestId(Thread.currentThread().getName()).reqId(appRequestLogDetail.getId()).operationType("com.xianglin.appserv.common.service.facade.app.InterestService.rechargeMoney").sessionId(sessionId).reqTimestamp(System.currentTimeMillis()).build();
            logManager.insertRequestLog(appRequestLog);
            FnBusiReqDTO fnBusiReqDTO = new FnBusiReqDTO();
            fnBusiReqDTO.setPartyId(partyId+"");
            fnBusiReqDTO.setMobilePhone(user.getLoginName());
            FnRechargeAndWithdrawalsReqDTO map = DTOUtils.map(iFnRechargeAndWithdrawalsReqDTO, FnRechargeAndWithdrawalsReqDTO.class);
            fnBusiReqDTO.setContent(map);
            logger.info("rechargeMoney para:{}", fnBusiReqDTO.toString());
            ResponseDTO<BalanceOrderDTO> balanceOrderDTOResponseDTO = fnTransServiceFacade.rechargeMoney(fnBusiReqDTO);
            logger.info("rechargeMoney result:{}", StringUtils.substring(balanceOrderDTOResponseDTO.toString(),0,200));
            if(balanceOrderDTOResponseDTO.isSuccess()){
                iBalanceOrderDTO = DTOUtils.map(balanceOrderDTOResponseDTO.getResult(),IBalanceOrderDTO.class);
                resp.setResult(iBalanceOrderDTO);
                //将返回结果保存在applog
                AppRequestLogDetail appRequestLogDetailResp = AppRequestLogDetail.builder().type(Constant.Log.RESP.name()).remark("用户充值").content(balanceOrderDTOResponseDTO.getResult()+","+ToStringBuilder.reflectionToString("")).build();
                logManager.insertRequestLogDetail(appRequestLogDetailResp);
                logManager.updateRequestLog(AppRequestLog.builder().id(appRequestLog.getId()).respId(appRequestLogDetailResp.getId()).respTimestamp(System.currentTimeMillis()).build());
            }else {
                resp.setCode(Integer.valueOf(balanceOrderDTOResponseDTO.getCode()));
                resp.setTips(balanceOrderDTOResponseDTO.getTip());
            }
        } catch (Exception e) {
            logger.error("rechargeMoney error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.InterestService.withdrawCash", description = "中原银行理财-用户提现")
    public Response<IBalanceOrderDTO> withdrawCash(IFnRechargeAndWithdrawalsReqDTO iFnRechargeAndWithdrawalsReqDTO) {
        Response<IBalanceOrderDTO> resp = ResponseUtils.successResponse();
        try {
            if(1 == 1){
                throw new BusiException("暂停服务");
            }
            IBalanceOrderDTO iBalanceOrderDTO = null;
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null ){
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            User user = userManager.queryUser(partyId);
            if(user == null){
                resp.setFacade(FacadeEnums.ERROR_CHAT_400041);
                return resp;
            }
            //将请求参数保存在applog
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String sessionId = sessionHelper.getSession().getId();
            AppRequestLogDetail appRequestLogDetail = AppRequestLogDetail.builder().remark("用户提现").content(iFnRechargeAndWithdrawalsReqDTO.toString()).type(Constant.Log.REQ.name()).build();
            logManager.insertRequestLogDetail(appRequestLogDetail);
            AppRequestLog appRequestLog = AppRequestLog.builder().deviceId(deviceId).partyId(partyId).requestId(Thread.currentThread().getName()).reqId(appRequestLogDetail.getId()).operationType("com.xianglin.appserv.common.service.facade.app.InterestService.withdrawCash").sessionId(sessionId).reqTimestamp(System.currentTimeMillis()).build();
            logManager.insertRequestLog(appRequestLog);
            FnBusiReqDTO fnBusiReqDTO = new FnBusiReqDTO();
            FnRechargeAndWithdrawalsReqDTO map = DTOUtils.map(iFnRechargeAndWithdrawalsReqDTO, FnRechargeAndWithdrawalsReqDTO.class);
            fnBusiReqDTO.setContent(map);
            fnBusiReqDTO.setMobilePhone(user.getLoginName());
            fnBusiReqDTO.setPartyId(partyId+"");
            logger.info("withdrawCash para:{}", fnBusiReqDTO.toString());
            ResponseDTO<BalanceOrderDTO> balanceOrderDTOResponseDTO = fnTransServiceFacade.withdrawCash(fnBusiReqDTO);
            logger.info("withdrawCash result:{}", StringUtils.substring(balanceOrderDTOResponseDTO.toString(),0,200));
            if(balanceOrderDTOResponseDTO.isSuccess()){
                iBalanceOrderDTO = DTOUtils.map(balanceOrderDTOResponseDTO.getResult(),IBalanceOrderDTO.class);
                resp.setResult(iBalanceOrderDTO);
                //将返回结果保存在applog
                AppRequestLogDetail appRequestLogDetailResp = AppRequestLogDetail.builder().type(Constant.Log.RESP.name()).remark("用户提现").content(balanceOrderDTOResponseDTO.getResult()+","+ToStringBuilder.reflectionToString("")).build();
                logManager.insertRequestLogDetail(appRequestLogDetailResp);
                logManager.updateRequestLog(AppRequestLog.builder().id(appRequestLog.getId()).respId(appRequestLogDetailResp.getId()).respTimestamp(System.currentTimeMillis()).build());
            }else {
                resp.setCode(Integer.valueOf(balanceOrderDTOResponseDTO.getCode()));
                resp.setTips(balanceOrderDTOResponseDTO.getTip());
            }
        } catch (Exception e) {
            logger.error("withdrawCash error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.InterestService.fundPurchase", description = "中原银行理财-基金申购")
    public Response<IProductOrderDTO> fundPurchase(IFnFundPurchaseReqDTO iFnFundPurchaseReqDTO) {
        Response<IProductOrderDTO> resp = ResponseUtils.successResponse();
        try {
            if(1 == 1){
                throw new BusiException("暂停服务");
            }
            IProductOrderDTO iProductOrderDTO = null;
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null ){
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            User user = userManager.queryUser(partyId);
            if(user == null){
                resp.setFacade(FacadeEnums.ERROR_CHAT_400041);
                return resp;
            }
            //将请求参数保存在applog
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String sessionId = sessionHelper.getSession().getId();
            AppRequestLogDetail appRequestLogDetail = AppRequestLogDetail.builder().remark("基金申购").content(iFnFundPurchaseReqDTO.toString()).type(Constant.Log.REQ.name()).build();
            logManager.insertRequestLogDetail(appRequestLogDetail);
            AppRequestLog appRequestLog = AppRequestLog.builder().deviceId(deviceId).partyId(partyId).requestId(Thread.currentThread().getName()).reqId(appRequestLogDetail.getId()).operationType("com.xianglin.appserv.common.service.facade.app.InterestService.fundPurchase").sessionId(sessionId).reqTimestamp(System.currentTimeMillis()).build();
            logManager.insertRequestLog(appRequestLog);
            FnBusiReqDTO fnBusiReqDTO = new FnBusiReqDTO();
            FnFundPurchaseReqDTO map = DTOUtils.map(iFnFundPurchaseReqDTO, FnFundPurchaseReqDTO.class);
            fnBusiReqDTO.setContent(map);
            fnBusiReqDTO.setMobilePhone(user.getLoginName());
            fnBusiReqDTO.setPartyId(partyId+"");
            logger.info("fundPurchase para:{}", fnBusiReqDTO.toString());
            ResponseDTO<ProductOrderDTO> productOrderDTOResponseDTO = fnTransServiceFacade.fundPurchase(fnBusiReqDTO);
            logger.info("fundPurchase result:{}", StringUtils.substring(productOrderDTOResponseDTO.toString(),0,200));
            if(productOrderDTOResponseDTO.isSuccess()){
                iProductOrderDTO = DTOUtils.map(productOrderDTOResponseDTO.getResult(),IProductOrderDTO.class);
                resp.setResult(iProductOrderDTO);
                //将返回结果保存在applog
                AppRequestLogDetail appRequestLogDetailResp = AppRequestLogDetail.builder().type(Constant.Log.RESP.name()).remark("基金申购").content(productOrderDTOResponseDTO.getResult()+","+ToStringBuilder.reflectionToString("")).build();
                logManager.insertRequestLogDetail(appRequestLogDetailResp);
                logManager.updateRequestLog(AppRequestLog.builder().id(appRequestLog.getId()).respId(appRequestLogDetailResp.getId()).respTimestamp(System.currentTimeMillis()).build());
            }else {
                resp.setCode(Integer.valueOf(productOrderDTOResponseDTO.getCode()));
                resp.setTips(productOrderDTOResponseDTO.getTip());
            }
        } catch (Exception e) {
            logger.error("withdrawCash error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.InterestService.fundRedeem", description = "中原银行理财-基金赎回")
    public Response<IProductOrderDTO> fundRedeem(IFnFundRedeemReqDTO iFnFundRedeemReqDTO) {
        Response<IProductOrderDTO> resp = ResponseUtils.successResponse();
        try {
            if(1 == 1){
                throw new BusiException("暂停服务");
            }
            IProductOrderDTO iProductOrderDTO = null;
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null ){
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            User user = userManager.queryUser(partyId);
            if(user == null){
                resp.setFacade(FacadeEnums.ERROR_CHAT_400041);
                return resp;
            }
            //将请求参数保存在applog
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String sessionId = sessionHelper.getSession().getId();
            AppRequestLogDetail appRequestLogDetail = AppRequestLogDetail.builder().remark("content").content(iFnFundRedeemReqDTO.toString()).type(Constant.Log.REQ.name()).build();
            logManager.insertRequestLogDetail(appRequestLogDetail);
            AppRequestLog appRequestLog = AppRequestLog.builder().deviceId(deviceId).partyId(partyId).requestId(Thread.currentThread().getName()).reqId(appRequestLogDetail.getId()).operationType("com.xianglin.appserv.common.service.facade.app.InterestService.fundRedeem").sessionId(sessionId).reqTimestamp(System.currentTimeMillis()).build();
            logManager.insertRequestLog(appRequestLog);
            FnBusiReqDTO fnBusiReqDTO = new FnBusiReqDTO();
            FnFundRedeemReqDTO map = DTOUtils.map(iFnFundRedeemReqDTO, FnFundRedeemReqDTO.class);
            fnBusiReqDTO.setContent(map);
            fnBusiReqDTO.setMobilePhone(user.getLoginName());
            fnBusiReqDTO.setPartyId(partyId+"");
            logger.info("fundRedeem para:{}", fnBusiReqDTO.toString());
            ResponseDTO<ProductOrderDTO> productOrderDTOResponseDTO = fnTransServiceFacade.fundRedeem(fnBusiReqDTO);
            logger.info("fundRedeem result:{}", StringUtils.substring(productOrderDTOResponseDTO.toString(),0,200));
            if(productOrderDTOResponseDTO.isSuccess()){
                iProductOrderDTO = DTOUtils.map(productOrderDTOResponseDTO.getResult(),IProductOrderDTO.class);
                resp.setResult(iProductOrderDTO);
                //将返回结果保存在applog
                AppRequestLogDetail appRequestLogDetailResp = AppRequestLogDetail.builder().type(Constant.Log.RESP.name()).remark("content").content(productOrderDTOResponseDTO.getResult()+","+ToStringBuilder.reflectionToString("")).build();
                logManager.insertRequestLogDetail(appRequestLogDetailResp);
                logManager.updateRequestLog(AppRequestLog.builder().id(appRequestLog.getId()).respId(appRequestLogDetailResp.getId()).respTimestamp(System.currentTimeMillis()).build());
            }else {
                resp.setCode(Integer.valueOf(productOrderDTOResponseDTO.getCode()));
                resp.setTips(productOrderDTOResponseDTO.getTip());
            }
        } catch (Exception e) {
            logger.error("withdrawCash error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }


    private UserInterestInfo getUserInterestInfo(JSONObject object, String phone, String partyId) {

        UserInterestInfo userInterestInfo = new UserInterestInfo();
        try {
            if (object != null) {
                String scope = object.getString("scope");
                JSONArray jsonArray = JSONArray.parseArray(scope);
                Set<String> set = new HashSet<String>();
                if (jsonArray.size() > 0) {
                    // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                    for (int i = 0; i < jsonArray.size(); i++) {
                        String number = jsonArray.get(i).toString();
                        set.add(number);
                        userInterestInfo.setScope(set);
                    }
                }
                userInterestInfo.setAccessToken(object.get("accessToken").toString());
                userInterestInfo.setKey(TokenUtils.decryptCode(object.get("key").toString()));
                userInterestInfo.setRefreshToken(object.get("refreshToken").toString());
                userInterestInfo.setOpenId(partyId);
                userInterestInfo.setMobileNo(phone);
                Session session = sessionHelper.getSession();
                session.setAttribute("USER_INTEREST", userInterestInfo);
                sessionHelper.saveSession(session);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInterestInfo;
    }

    private String getCode(String resp) {
        String code = null;
        JSONObject object = JSON.parseObject(resp);
        if (object.get("errorCode").toString().equals(successCode)) {
            code = TokenUtils.decryptCode(object.get("code").toString());
            SystemConfigModel systemConfigModel = new SystemConfigModel();
            systemConfigModel.setCode("interest_authorizeCode");
            systemConfigModel.setValue(code);
            sysParaManager.updateByName(systemConfigModel);
        }
        return code;
    }

    //获取code和token并将token放到session中
    private void accessToken(String phone, String openId) {
        String respCode = TokenUtils.authorize();
        if (StringUtils.isNotEmpty(respCode)) {
            String againCode = getCode(respCode);
            String respToken = TokenUtils.accessToken(againCode, phone, openId);//根据code获取token
            if (StringUtils.isNotEmpty(respToken)) {
                JSONObject objectToken = JSON.parseObject(respToken);
                if (objectToken.get("errorCode").toString().equals(successCode)) {
                    getUserInterestInfo(objectToken, phone, openId);
                }
            }
        }
    }



    //获取token,并判断token是否失效，如果失效，则调用accessToken方法重新获取code
    private void getToken(String code, String phone, String openId) {
        String respToken = TokenUtils.accessToken(code, phone, openId);
        if (StringUtils.isNotEmpty(respToken)) {
            JSONObject objectToken = JSON.parseObject(respToken);
            if (StringUtils.contains(errorCode,objectToken.get("errorCode").toString()) || StringUtils.contains(errorCode2,objectToken.get("errorCode").toString())) {
                accessToken(phone, openId + "");
            } else if(objectToken.get("errorCode").toString().equals(successCode)){
                getUserInterestInfo(objectToken, phone, openId + "");
            }
        }else{
            accessToken(phone,openId);
        }
    }
}
