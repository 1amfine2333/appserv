/**
 *
 */
package com.xianglin.appserv.common.service.integration.cif.impl;

import com.xianglin.appserv.common.service.integration.cif.AppLoginServiceClient;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.common.util.EncryptUtil;
import com.xianglin.appserv.common.util.RedisUtil;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.appserv.common.util.constant.AppservConstants;
import com.xianglin.cif.common.service.facade.AppLoginService;
import com.xianglin.cif.common.service.facade.constant.BusinessConstants;
import com.xianglin.cif.common.service.facade.constant.ResponseConstants.FacadeEnums;
import com.xianglin.cif.common.service.facade.model.DeviceInfo;
import com.xianglin.cif.common.service.facade.model.LoginDTO;
import com.xianglin.cif.common.service.facade.model.Response;
import com.xianglin.cif.common.service.facade.req.PersonReq;
import com.xianglin.cif.common.service.facade.resp.PersonResp;
import com.xianglin.cif.common.service.facade.vo.PartyAttrContactVo;
import com.xianglin.cif.common.service.facade.vo.PersonVo;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.xlStation.base.model.SmsResponse;
import com.xianglin.xlStation.common.service.facade.MessageService;
import com.xianglin.xlnodecore.common.service.facade.AccountNodeManagerService;
import com.xianglin.xlnodecore.common.service.facade.NodeService;
import com.xianglin.xlnodecore.common.service.facade.NodeWorkImageService;
import com.xianglin.xlnodecore.common.service.facade.base.CommonReq;
import com.xianglin.xlnodecore.common.service.facade.base.CommonResp;
import com.xianglin.xlnodecore.common.service.facade.enums.DeleteEnum;
import com.xianglin.xlnodecore.common.service.facade.enums.NodeStatusEnums;
import com.xianglin.xlnodecore.common.service.facade.enums.OperateTypeEnum;
import com.xianglin.xlnodecore.common.service.facade.req.AccountLogReq;
import com.xianglin.xlnodecore.common.service.facade.req.NodeReq;
import com.xianglin.xlnodecore.common.service.facade.req.PosLoginReq;
import com.xianglin.xlnodecore.common.service.facade.resp.AccountNodeManagerResp;
import com.xianglin.xlnodecore.common.service.facade.resp.NodeResp;
import com.xianglin.xlnodecore.common.service.facade.resp.PosLoginResp;
import com.xianglin.xlnodecore.common.service.facade.vo.NodeVo;
import com.xianglin.xlnodecore.common.service.facade.vo.NodeWorkImageVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * app登录
 *
 * @author cf 2016年8月15日上午10:37:20
 */
public class AppLoginServiceClientImpl implements AppLoginServiceClient {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(AppLoginServiceClientImpl.class);

    /**
     * figureService
     */
    private AppLoginService appLoginService;

    /**
     * accountNodeManagerService
     */
    private AccountNodeManagerService accountNodeManagerService;

    /**
     * nodeService
     */
    private NodeService nodeService;


    /**
     * messageService
     */
    private MessageService messageService;

    /**
     * nodeWorkImageService
     */
    private NodeWorkImageService nodeWorkImageService;

    @Autowired
    private RedisUtil redisUtil;


    private String smsContentTemplate = "亲爱的用户，您本次手机验证码是$，请输入以完成身份认证。该验证码30分钟内有效。";

    @Override
    public Response<Long> getNodeManagerPartyId(String mobilePhone) {
        Response<Long> resp = new Response<>();
        List<Long> partyIds = accountNodeManagerService.getNodeManagerMoreInfoByMobilePhone(mobilePhone);
        if (CollectionUtils.isEmpty(partyIds)) {
            resp.setCode(AppservConstants.LoginError.NOT_BIND_MOBILE.code);
            resp.setTips(AppservConstants.LoginError.NOT_BIND_MOBILE.desc);
            resp.setMemo(AppservConstants.LoginError.NOT_BIND_MOBILE.desc);
            return resp;
        } else if (partyIds.size() > 1) {
            resp.setCode(AppservConstants.LoginError.MOBILE_BIND_MORE.code);
            resp.setTips(AppservConstants.LoginError.MOBILE_BIND_MORE.desc);
            resp.setMemo(AppservConstants.LoginError.MOBILE_BIND_MORE.desc);
            return  resp;
        }
        resp.setResult(partyIds.get(0));
        return resp;
    }
    @Override
    public Response<String> cifAutoLogin(LoginDTO loginDTO,DeviceInfo deviceInfo,String deviceId){
        Response<String> resp = new Response<>();
        resp.setFacade(FacadeEnums.OK);
        //校验设备信息
//        Response<String> deviceResp = appLoginService.autoLogin(loginDTO, deviceInfo, deviceId);
//        if (null == deviceResp || !(ResultEnum.ResultSuccess.getCode() == deviceResp.getCode())) {
//            resp.setCode(deviceResp.getCode());
//            resp.setMemo(deviceResp.getMemo());
//            resp.setTips(deviceResp.getTips());
//        }
        return resp;
    }
    @Override
    public Response<String> cifLogin(LoginDTO loginDTO, DeviceInfo deviceInfo, String deviceId){

        Response<String> resp = new Response<>();
        resp.setFacade(FacadeEnums.OK);
        //校验设备信息
//        Response<String> deviceResp = appLoginService.login(loginDTO, deviceInfo, deviceId);
//        if (null == deviceResp || !(ResultEnum.ResultSuccess.getCode() == deviceResp.getCode())) {
//            resp.setCode(deviceResp.getCode());
//            resp.setMemo(deviceResp.getMemo());
//            resp.setTips(deviceResp.getTips());
//        }
        return resp;
    }
    /**
     * @see com.xianglin.appserv.common.service.integration.cif.AppLoginServiceClient#login(com.xianglin.cif.common.service.facade.model.LoginDTO, com.xianglin.cif.common.service.facade.model.DeviceInfo, java.lang.String)
     */
    @Override
    public Response<NodeVo> login(LoginDTO loginDTO, DeviceInfo deviceInfo, String deviceId) {
        Response<NodeVo> resp = new Response<>();
        resp.setFacade(FacadeEnums.OK);
        try {
            Long partyId ;
            boolean isCheckPassword = false;
            if (!StringUtils.isEmpty(loginDTO.getNodeCode())) {
                //获取站长partyId并校验非空
                isCheckPassword = true;
                partyId = accountNodeManagerService.getNodeManagerMoreInfoByNodeCode(loginDTO.getNodeCode());
                if (null == partyId || null == loginDTO.getPassword()) {
                    resp.setFacade(FacadeEnums.LOGIN_USERID_OR_PASSWORD_INVALID);
                    return resp;
                }
            } else if (!StringUtils.isEmpty(loginDTO.getMobilePhone())) {
          /*      List<Long> partyIds = accountNodeManagerService.getNodeManagerMoreInfoByMobilePhone(loginDTO.getMobilePhone());
                if (CollectionUtils.isEmpty(partyIds)) {
                    resp.setCode(AppservConstants.LoginError.NOT_BIND_MOBILE.code);
                    resp.setTips(AppservConstants.LoginError.NOT_BIND_MOBILE.desc);
                    resp.setMemo(AppservConstants.LoginError.NOT_BIND_MOBILE.desc);
                    return resp;
                } else if (partyIds.size() > 1) {
                    resp.setCode(AppservConstants.LoginError.MOBILE_BIND_MORE.code);
                    resp.setTips(AppservConstants.LoginError.MOBILE_BIND_MORE.desc);
                    resp.setMemo(AppservConstants.LoginError.MOBILE_BIND_MORE.desc);
                    return resp;
                }
                partyId = partyIds.get(0);*/

                Response<Long> partyResp = getNodeManagerPartyId(loginDTO.getMobilePhone());
                if(FacadeEnums.OK.code.equals(partyResp.getCode()+"")){
                    partyId = partyResp.getResult();
                }else{
                    resp.setFacade(FacadeEnums.RETURN_EMPTY);
                    return resp;
                }
            } else {
                resp.setCode(AppservConstants.LoginError.INVALID_INPUT_ACCOUNT.code);
                resp.setTips(AppservConstants.LoginError.INVALID_INPUT_ACCOUNT.desc);
                resp.setMemo(AppservConstants.LoginError.INVALID_INPUT_ACCOUNT.desc);
                return resp;
            }


            loginDTO.setPartyId(partyId);//绑定partyId后面校验绑定
            //校验设备信息
//            Response<String> deviceResp  = cifLogin (loginDTO,deviceInfo,deviceId);
//           if(!FacadeEnums.OK.code.equals(deviceResp.getCode()+"")){
//               resp.setCode(deviceResp.getCode());
//               resp.setMemo(deviceResp.getMemo());
//               resp.setTips(deviceResp.getTips());
//               return resp;
//           }
            //校验站长partyId和密码

//            if (isCheckPassword) {
//                Response<Boolean> nodeResp = posRegisterLoginService.login(partyId, loginDTO.getPassword());
//                if (null != nodeResp && !nodeResp.getResult()) {
//                    resp.setCode(nodeResp.getCode());
//                    resp.setTips(nodeResp.getTips());
//                    resp.setMemo(nodeResp.getMemo());
//                    return resp;
//                }
//            }
            //通过网点经理partyId获取站点详细信息
            NodeReq req = new NodeReq();
            NodeVo vo = new NodeVo();
            vo.setNodeManagerPartyId(partyId);
            req.setVo(vo);
            NodeVo nodeVo = getNodeVoByManagerPartyId(req);
            resp.setResult(nodeVo);
            //记录日志
            String comment =nodeVo.getNodeManagerName()+"主动登录了移动app";
            saveLog(nodeVo.getNodeManagerPartyId(),nodeVo.getNodeManagerName(),nodeVo.getNodeManagerName(),OperateTypeEnum.APP_LOGIN,comment);
            return resp;
        } catch (Exception e) {
            logger.error("", e);
            resp.setFacade(FacadeEnums.LOGIN_EXCEPTION);
            return resp;
        }

    }
    /**
     * @see com.xianglin.appserv.common.service.integration.cif.AppLoginServiceClient#autoLogin(com.xianglin.cif.common.service.facade.model.LoginDTO, com.xianglin.cif.common.service.facade.model.DeviceInfo, java.lang.String)
     */
    @Override
    public Response<NodeVo> autoLogin(LoginDTO loginDTO, DeviceInfo deviceInfo, String deviceId) {
        Response<NodeVo> resp = new Response<NodeVo>();
        resp.setFacade(FacadeEnums.OK);
        try {
            //校验设备信息
//            Response<String> deviceResp = appLoginService.autoLogin(loginDTO, deviceInfo, deviceId);
//            if (null == deviceResp || !(ResultEnum.ResultSuccess.getCode() == deviceResp.getCode())) {
//                resp.setCode(deviceResp.getCode());
//                resp.setMemo(deviceResp.getMemo());
//                resp.setTips(deviceResp.getTips());
//                return resp;
//            }
            //获取站长partyId
//			if(null==loginDTO.getPartyId()||null==loginDTO.getPassword()){
//				resp.setFacade(FacadeEnums.LOGIN_USERID_OR_PASSWORD_INVALID);
//				return resp;
//			}
            //校验站长partyId和密码
//			Response<Boolean> nodeResp = pOSRegisterLoginService.login(loginDTO.getPartyId(), EncryptUtil.sha512(loginDTO.getPassword()));
//			if(null==nodeResp||!nodeResp.getResult()){
//				resp.setCode(nodeResp.getCode());
//				resp.setTips(nodeResp.getTips());
//				resp.setMemo(nodeResp.getMemo());
//				return resp;
//			}

            //通过网点经理partyId获取站点详细信息
            NodeReq req = new NodeReq();
            NodeVo vo = new NodeVo();
            vo.setNodeManagerPartyId(loginDTO.getPartyId());
            req.setVo(vo);
            NodeVo nodeVo = getNodeVoByManagerPartyId(req);
            resp.setResult(nodeVo);
            //记录日志
            String comment =nodeVo.getNodeManagerName()+"自动登录了移动app";
            saveLog(nodeVo.getNodeManagerPartyId(),nodeVo.getNodeManagerName(),nodeVo.getNodeManagerName(),OperateTypeEnum.APP_LOGIN,comment);
            return resp;
        } catch (Exception e) {
            logger.info(e.getMessage());
            resp.setFacade(FacadeEnums.LOGIN_EXCEPTION);
            return resp;
        }

    }

    @Override
    public void saveLog(Long objectId,String accountName,String creator,OperateTypeEnum operateTypeEnum,String comment) {
        saveLog(objectId,accountName,creator,operateTypeEnum,comment,"");
    }

    @Override
    public void saveLog(Long objectId, String accountName, String creator, OperateTypeEnum operateTypeEnum, String comment, String operateResult) {
        AccountLogReq acReq = new AccountLogReq();
        acReq.setAccountName(accountName);//网点经理名称
        acReq.setCreateDate(new Date());
        acReq.setObjectId(objectId);
        acReq.setObjectType(operateTypeEnum.name());
        acReq.setObjectName(operateTypeEnum.getMsg());
        acReq.setClientDevice("APP");
        acReq.setClientName("APP");
        acReq.setCreator(creator);
        acReq.setComments(comment);
        acReq.setOperateResult(operateResult);
//        logServiceClient.accountLogSave(acReq);
    }

    @Override
    public int queryLogCcount(Long partyId, String type) {
//        return logServiceClient.queryPosLoginErrorCount(partyId, type);
        return 0;
    }

    private NodeVo getNodeVoByManagerPartyId(NodeReq req) throws Exception {
        NodeVo nodeVo = new NodeVo();
        try {
            NodeResp nodeResponse = nodeService.queryNodeInfoByNodeManagerPartyId(req);
            if (FacadeEnums.OK.code.equals(nodeResponse.getCode()+"")){
                logger.info("nodeService.queryNodeInfoByNodeManagerPartyId(req):" + nodeResponse);
                nodeVo = nodeResponse.getVo();

                //获取站长头像
                List<NodeWorkImageVo> avatars = nodeWorkImageService.selectLatest(nodeVo.getNodePartyId(), null);
                if (!CollectionUtils.isEmpty(avatars)) {
                    nodeVo.setAvatar(null == avatars.get(0).getImageUrl() ? "" : (SysConfigUtil.getStr("H5WECHAT_URL") + avatars.get(0).getImageUrl()));
                } else {
                    nodeVo.setAvatar("");
                }
            }
        } catch (Exception e) {
            logger.error("查询网点经理信息出现问题", e);
            throw new Exception("查询网点经理信息出现问题");
        }
        return nodeVo;
    }
    /**
     * @see com.xianglin.appserv.common.service.integration.cif.AppLoginServiceClient#logout(java.lang.String, java.lang.Long)
     */
    @Override
    public Response<Boolean> logout(String deviceId, Long partyId) {
        return appLoginService.logout(deviceId, partyId);
    }

    /**
     * Description: 短信发送
     *
     * @author xiezhouqi
     * @date 2016年5月17日
     */
    public Response<Boolean> sendSms(String nodeCode, String mobilePhone) {
        Response<Boolean> resp = new Response<Boolean>();
        resp.setResult(true);
        try {
            if (StringUtils.isEmpty(mobilePhone)) {
                resp.setCode(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_INPUT_INVALID.code);
                resp.setTips("请输入手机号");
                resp.setMemo("请输入手机号");
                return resp;
            }
            boolean isRepeat = redisUtil.isRepeat("send_msg_code"+mobilePhone,10);
            if(isRepeat){
                resp.setFacade(FacadeEnums.E_C_REPEAT_SUBMIT);
                return resp;
            }
            List<Long> partyIds = accountNodeManagerService.getNodeManagerMoreInfoByMobilePhone(mobilePhone);

            if (CollectionUtils.isEmpty(partyIds)) {
                resp.setCode(AppservConstants.LoginError.NOT_BIND_MOBILE.code);
                resp.setTips(AppservConstants.LoginError.NOT_BIND_MOBILE.desc);
                resp.setMemo(AppservConstants.LoginError.NOT_BIND_MOBILE.desc);
                return resp;
            }
            if (partyIds.size() > 1) {
                resp.setCode(AppservConstants.LoginError.MOBILE_BIND_MORE.code);
                resp.setTips(AppservConstants.LoginError.MOBILE_BIND_MORE.desc);
                resp.setMemo(AppservConstants.LoginError.MOBILE_BIND_MORE.desc);
                return resp;
            }
//            int errorSmsAndPwdCount = logServiceClient.queryPosLoginErrorCount(Long.valueOf(mobilePhone), PosLoginTypeEnum.AccessSMSCode.getTips());
            //超过3次，冻结1个小时
//            if (errorSmsAndPwdCount > 3) {
//                resp.setCode(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_VALDATE_POS_LOGIN_SMSERROR_COUNT.code);
//                resp.setTips(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_VALDATE_POS_LOGIN_SMSERROR_COUNT.tip);
//                resp.setMemo("验证错误次过多，请一小时后重试");
//                logger.debug("----一个小时内的短信验证码错误次数超过3次，账户冻结1小时----");
//                return resp;
//            }
            /*	CommonReq<Long> comReq = new CommonReq<>();
				comReq.setReqVo(Long.parseLong(nodeCode));
				CommonResp<String> commonResp = posLoginService.getNodeManagerMobileByNodeCode(comReq);
				
				
				if(commonResp.getCode() == 400000){
					resp.setCode(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_INPUT_INVALID.code);
					resp.setMemo(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_INPUT_INVALID.msg);
					resp.setTips(commonResp.getTips());
					resp.setResult(false);
					return resp;
				}
				if(commonResp.getCode() != com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.OK.code && commonResp.getRespVo() == null){    //根据NodePartyId未查询到手机号码
					resp.setCode(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_INPUT_INVALID.code);
					resp.setMemo(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_INPUT_INVALID.msg);
					resp.setTips("站点编号输入有误请重新输入");
					resp.setResult(false);
					return resp;
				}*/
            com.xianglin.xlStation.base.model.Response smsresp = messageService.sendSmsCode(mobilePhone, smsContentTemplate, "1800");
            if (smsresp.getBussinessCode() != 2000) {
                resp.setCode(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_INPUT_INVALID.code);
                resp.setMemo("验证码发送失败");
                resp.setTips("验证码发送失败");
                resp.setResult(false);
                return resp;
            }
        } catch (Exception e) {
            logger.info("发送短信异常" + e.getMessage());
            resp.setResult(false);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }

        return resp;
    }

    /**
     * 首次登录
     * Description:
     *
     * @author xiezhouqi
     * @date 2016年5月12日
     */
    public PosLoginResp userFirstLogin(String nodeCode, String smsCode, String password, String mobilePhone) {
        PosLoginReq posLoginReq = new PosLoginReq();
        PosLoginResp posLoginResp = new PosLoginResp();
        try {
            posLoginReq.setPwd(password);
            if (!StringUtils.isEmpty(nodeCode)) {
                posLoginReq.setNodeCode(Long.parseLong(nodeCode));
            }
            posLoginReq.setSmsCode(smsCode);
            posLoginReq.setMobilePhone(mobilePhone);
//            posLoginResp = posLoginService.appFirstLogin(posLoginReq);
        } catch (Exception e) {
            logger.info("首次登录异常" + e.getMessage());
            posLoginResp.setBaseResp(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.RETURN_EMPTY);
        }
        return posLoginResp;
    }

    /**
     * 忘记密码
     */
    public PosLoginResp resetUserLogin(String nodeCode, String smsCode, String password, String mobilePhone) {
        PosLoginReq posLoginReq = new PosLoginReq();
        PosLoginResp posLoginResp = new PosLoginResp();
//        try {
//            posLoginReq.setSmsCode(smsCode);//手机校验码
//            if (!StringUtils.isEmpty(nodeCode)) {
//                posLoginReq.setNodeCode(Long.parseLong(nodeCode));
//            }
//            posLoginReq.setPwd(password);//密码
//            posLoginReq.setMobilePhone(mobilePhone);
//            posLoginResp = posLoginService.appChangePWDlogin(posLoginReq);
//        } catch (Exception e) {
//            logger.info("忘记密码异常" + e.getMessage());
//            posLoginResp.setBaseResp(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.RETURN_EMPTY);
//        }
        return posLoginResp;
    }

    /**
     * 获取站点经理的详细信息
     * cf
     */
    public AccountNodeManagerResp queryNodeManagerByPartyId(Long partyId) {
        return accountNodeManagerService.queryNodeManagerByPartyId(partyId);
    }

    @Override
    public NodeVo queryNodeInfoByNodeManagerPartyId(Long partyId){

        NodeReq req = new NodeReq();
        NodeVo nodeVo = new NodeVo();
        nodeVo.setNodeManagerPartyId(partyId);
        req.setVo(nodeVo);
        NodeResp resp = nodeService.queryNodeInfoByNodeManagerPartyId(req);

        if(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.OK.getCode() == resp.getCode()){
            NodeVo vo = resp.getVo();
            if(NodeStatusEnums.JOINED.getMsg().equals(vo.getOperationStatus())){
                return vo;
            }
        }
        return null;
    }

    /**
     * 获取站点经理手机号码
     *
     * @see com.xianglin.appserv.common.service.integration.cif.AppLoginServiceClient#queryMobileByNodeCode(java.lang.String)
     */
    @Override
    public CommonResp<String> queryMobileByNodeCode(String nodeCode) {
        CommonReq<Long> comReq = new CommonReq<>();
        comReq.setReqVo(Long.parseLong(nodeCode));
//        return posLoginService.getNodeManagerMobileByNodeCode(comReq);
        return null;
    }

    /**
     * @param appLoginService the appLoginService to set
     */
    public void setAppLoginService(AppLoginService appLoginService) {
        this.appLoginService = appLoginService;
    }

//    /**
//     * @param posRegisterLoginService the appLoginService to set
//     */
//    public void setPosRegisterLoginService(POSRegisterLoginService posRegisterLoginService) {
//        this.posRegisterLoginService = posRegisterLoginService;
//    }

    /**
     * @param accountNodeManagerService the appLoginService to set
     */
    public void setAccountNodeManagerService(AccountNodeManagerService accountNodeManagerService) {
        this.accountNodeManagerService = accountNodeManagerService;
    }

    /**
     * @param nodeService the appLoginService to set
     */
    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }


    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

//    public void setPosLoginService(PosLoginService posLoginService) {
//        this.posLoginService = posLoginService;
//    }
//
//    public void setLogServiceClient(LogServiceClient logServiceClient) {
//        this.logServiceClient = logServiceClient;
//    }

    public void setNodeWorkImageService(NodeWorkImageService nodeWorkImageService) {
        this.nodeWorkImageService = nodeWorkImageService;
    }

    public static void main(String[] args) {
        String str = "[{\"platform\":\"iPhone\",\"systemType\":\"ios\",\"systemVersion\":\"1.0.0\"}]";
        System.out.println(EncryptUtil.sha512(str));
//		System.out.println(new Sha512Hash(str));
        //System.out.println(null==null?"":commonResp.getRespVo());
    }

    /**
     * 有疑问？  干嘛不是在发送短信的时候查询短信发送参数，在进行判断是否发送短信
     * xlstation提供查询接口，查询发送次数
     *
     * @see com.xianglin.appserv.common.service.integration.cif.AppLoginServiceClient#validateSms(java.lang.String, java.lang.String)
     */
    @Override
    public Response<Boolean> validateSms(String mobilePhone, String smsCode) {
        Response<Boolean> resp = new Response<>();

        boolean errorSmsAndPwdCount = redisUtil.isReady(mobilePhone,0,6,3600);

//       int errorSmsAndPwdCount = logServiceClient.queryPosLoginErrorCount(Long.valueOf(mobilePhone), PosLoginTypeEnum.AccessSMSCode.getTips());
        //超过3次，冻结1个小时
        if (errorSmsAndPwdCount) {
            resp.setCode(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_VALDATE_POS_LOGIN_SMSERROR_COUNT.code);
            resp.setTips("您的操作次数过多，请在一个小时后重试！");
            resp.setMemo("您的操作次数过多，请在一个小时后重试！");
            logger.debug("----一个小时内的短信验证码错误次数超过3次，账户冻结1小时----");
            return resp;
        }
        SmsResponse smsResponse = messageService.getSmsCode(mobilePhone);
        logger.info("validateSms smsCode:{},smsResponse:{}",smsCode, ToStringBuilder.reflectionToString(smsResponse));
        if (!(smsResponse.getBussinessCode() == 2000)) {//这里临时先写死，xlStion的成功响应码
            resp.setCode(smsResponse.getBussinessCode());
            resp.setCode(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_VALDATE_MOBILEPHONE_ERROR.code);
            resp.setTips(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_VALDATE_MOBILEPHONE_ERROR.tip);
            logger.warn("----请求xlStation, 获取短信验证码异常----");
            return resp;
        }
        if (!smsCode.equals(smsResponse.getSmsCode())) {
            resp.setCode(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_VALDATE_MOBILEPHONE_ERROR.code);
            resp.setTips(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_VALDATE_MOBILEPHONE_ERROR.tip);
            resp.setResult(false);
//            AccountLogReq req = new AccountLogReq();
//            req.setObjectId(Long.valueOf(mobilePhone));
//            req.setObjectType(OperateTypeEnum.INVALID_SMSCODE.name());
//            req.setObjectName("APP");
//            req.setOperateResult(PosLoginTypeEnum.AccessSMSCode.getTips());
//            logServiceClient.accountLogSave(req);
            redisUtil.isReady(mobilePhone,1,6,3600);
            logger.debug("----短信验证码输入有误----");
            return resp;
        }
        redisUtil.isReady(mobilePhone,1,6,1);
        return resp;
    }

    private PersonResp registerCif(LoginDTO loginDTO,DeviceInfo deviceInfo){

        Date now = DateUtils.getNow();
        PersonReq req = new PersonReq();
        PartyAttrContactVo contactVo = new PartyAttrContactVo();
        contactVo.setContactType(BusinessConstants.ContactTypeEnums.MOBILE.name());
        contactVo.setCreator(loginDTO.getMobilePhone());
        contactVo.setContactStatus(BusinessConstants.ProfileStatus.VALID.name());
        contactVo.setCreateDate(now);
        contactVo.setUpdateDate(now);
        contactVo.setIsDeleted(DeleteEnum.NOT_DEL.getCode());
        contactVo.setContactInfo(loginDTO.getMobilePhone());
        req.setContactVo(contactVo);
        PersonVo personVo = new PersonVo();
        personVo.setIsDeleted(DeleteEnum.NOT_DEL.getCode());
        personVo.setCreateDate(now);
        personVo.setCreator(loginDTO.getMobilePhone());
        personVo.setRegMobilePhone(loginDTO.getMobilePhone());
        personVo.setUpdateDate(now);
        personVo.setLastLoginDeviceId(deviceInfo.getPlatform());
        personVo.setBirthday(now);
        req.setVo(personVo);
//        PersonResp  resp = personService.addPersonWithOutCredential(req);
//        return resp;
        return null;
    }

    private PersonResp getPersonVo(LoginDTO loginDTO){
        PersonReq req = new PersonReq();
        PersonVo personVo = new PersonVo();
        personVo.setIsDeleted(DeleteEnum.NOT_DEL.getCode());
        personVo.setRegMobilePhone(loginDTO.getMobilePhone());
        req.setVo(personVo);
//       return personService.getPersonInfo(req);
        return null;
    }

    public Response<PersonVo> registerUser(LoginDTO loginDTO,DeviceInfo deviceInfo,String deviceId){
        Response<PersonVo> resp = new Response<>();
        resp.setFacade(FacadeEnums.OK);
        PersonResp personInfo = getPersonVo(loginDTO);
        if(FacadeEnums.OK.equals(personInfo.getCode())){
            resp.setResult(personInfo.getVo());
            return resp;
        }else if(FacadeEnums.E_C_REPEAT_SUBMIT.code.equals(personInfo.getCode())){
            logger.warn("=====手机号：{}存在重复=====",loginDTO.getMobilePhone());
            resp.setCode(Integer.valueOf(personInfo.getCode()));
            resp.setTips("手机号状态异常");
            resp.setMemo("手机号状态异常");
            return resp;
        }
        PersonResp personResp = registerCif(loginDTO,deviceInfo);
        if(FacadeEnums.OK.code.equals(personResp.getCode())){
            resp.setResult(personResp.getVo());
            String comments = loginDTO.getMobilePhone()+"注册完成";
            Long partyId = personResp.getVo().getPartyId();
            saveLog(partyId,personResp.getVo().getTrueName(),loginDTO.getMobilePhone(),OperateTypeEnum.APP_REGISTER,comments);
        }else{
            resp.setFacade(FacadeEnums.INSERT_FAIL);
            resp.setTips("亲，请稍后重试");
            resp.setMemo("亲，请稍后重试");
            logger.warn("====注册app用户失败====，手机号：{},设备id:{}",loginDTO.getMobilePhone(),deviceId);
        }
        return resp;
    }

	@Override
    @Deprecated
	public Response<NodeVo> loginAndRegister(LoginDTO loginDTO, DeviceInfo deviceInfo, String deviceId) {
		Response<NodeVo> resp = new Response<>();
        resp.setFacade(FacadeEnums.OK);
        try {
            boolean isRegisterLogin  = false;
            boolean isNodeManager = true;
            Long partyId = null;
            if(!StringUtils.isEmpty(loginDTO.getMobilePhone())) {
                List<Long> partyIds = accountNodeManagerService.getNodeManagerMoreInfoByMobilePhone(loginDTO.getMobilePhone());
                if (CollectionUtils.isEmpty(partyIds)) {
                    PersonResp personResp = registerCif(loginDTO,deviceInfo);
                    if(FacadeEnums.OK.code.equals(personResp.getCode())){
                        partyId = personResp.getVo().getPartyId();
                        isRegisterLogin  = true;
                        isNodeManager = false;
                    }
                } else if (partyIds.size() > 1) {
                    resp.setCode(AppservConstants.LoginError.MOBILE_BIND_MORE.code);
                    resp.setTips(AppservConstants.LoginError.MOBILE_BIND_MORE.desc);
                    resp.setMemo(AppservConstants.LoginError.MOBILE_BIND_MORE.desc);
                    return resp;
                }else{
                	partyId = partyIds.get(0);
                }
            } else {
                resp.setCode(AppservConstants.LoginError.INVALID_INPUT_ACCOUNT.code);
                resp.setTips(AppservConstants.LoginError.INVALID_INPUT_ACCOUNT.desc);
                resp.setMemo(AppservConstants.LoginError.INVALID_INPUT_ACCOUNT.desc);
                return resp;
            }
            loginDTO.setPartyId(partyId);//绑定partyId后面校验绑定
            //校验设备信息
            Response<String> deviceResp = appLoginService.login(loginDTO, deviceInfo, deviceId);
            if (null == deviceResp || !(ResultEnum.ResultSuccess.getCode() == deviceResp.getCode())) {
                resp.setCode(deviceResp.getCode());
                resp.setMemo(deviceResp.getMemo());
                resp.setTips(deviceResp.getTips());
                return resp;
            }
            //记录日志
            AccountLogReq acReq = new AccountLogReq();
            String comment ="" ;
            //通过网点经理partyId获取站点详细信息
            if(isNodeManager) {
                NodeReq req = new NodeReq();
                NodeVo vo = new NodeVo();
                vo.setNodeManagerPartyId(partyId);
                req.setVo(vo);
                NodeVo nodeVo = getNodeVoByManagerPartyId(req);
                resp.setResult(nodeVo);
                acReq.setAccountName(nodeVo.getNodeManagerName());//网点经理名称
                acReq.setCreator(nodeVo.getNodeManagerName());
                comment = nodeVo.getNodeManagerName() + "主动登录了移动app";
            }else{
                //NodeVo
            }
            acReq.setCreateDate(new Date());
            acReq.setObjectId(partyId);
            acReq.setObjectType("APP登录");
            acReq.setObjectName("APP");
            acReq.setClientDevice("APP");
            acReq.setClientName("APP");
            if(isRegisterLogin){
                comment = loginDTO.getMobilePhone() + "主动注册登录了移动app";
            }
            acReq.setComments(comment);
//            logServiceClient.accountLogSave(acReq);
            return resp;
        } catch (Exception e) {
            logger.error("", e);
            resp.setFacade(FacadeEnums.LOGIN_EXCEPTION);
            return resp;
        }
	}

	@Override
	public Response<Boolean> sendSmsOnly(String mobilePhone) {
		Response<Boolean> resp = new Response<Boolean>();
        resp.setResult(true);
        try {
            if (StringUtils.isEmpty(mobilePhone)) {
                resp.setCode(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_INPUT_INVALID.code);
                resp.setTips("请输入手机号");
                resp.setMemo("请输入手机号");
                return resp;
            }
            boolean isRepeat = redisUtil.isRepeat("send_msg_code"+mobilePhone,10);
            if(isRepeat){
                resp.setFacade(FacadeEnums.E_C_REPEAT_SUBMIT);
                return resp;
            }
//            int errorSmsAndPwdCount = logServiceClient.queryPosLoginErrorCount(Long.valueOf(mobilePhone), OperateTypeEnum.SMSCODE_APP.name());
//            //超过3次，冻结1个小时
//            if (errorSmsAndPwdCount > 3) {
//                resp.setCode(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_VALDATE_POS_LOGIN_SMSERROR_COUNT.code);
//                resp.setTips(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_VALDATE_POS_LOGIN_SMSERROR_COUNT.tip);
//                resp.setMemo("验证码发送次数过多，请一小时后重试");
//                logger.debug("----一个小时内的短信验证码发送次数过多超过3次，账户冻结1小时----");
//                return resp;
//            }
            com.xianglin.xlStation.base.model.Response smsresp = messageService.sendSmsCode(mobilePhone, smsContentTemplate, "1800");
            if (smsresp.getBussinessCode() != 2000) {
                resp.setCode(com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_INPUT_INVALID.code);
                resp.setMemo("验证码发送失败");
                resp.setTips("验证码发送失败");
                resp.setResult(false);
                return resp;
            }else{
                saveLog(Long.valueOf(mobilePhone),mobilePhone,mobilePhone,OperateTypeEnum.SMSCODE_APP,"发送成功");
            }

        } catch (Exception e) {
            logger.info("发送短信异常" + e.getMessage());
            resp.setResult(false);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }

        return resp;
	}


}
