package com.xianglin.appserv.biz.service.implement;

import com.xianglin.appserv.biz.shared.util.LoginAttrUtil;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.service.facade.app.PersonInfoService;
import com.xianglin.appserv.common.service.facade.model.MyInfoRowDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.AccountNodeManagerVo;
import com.xianglin.appserv.common.service.integration.cif.PartyAttrAccountServiceClient;
import com.xianglin.appserv.common.service.integration.cif.TransferServiceClient;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.appserv.common.util.XLCommonUtils;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.constant.ResponseConstants;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.req.PersonReq;
import com.xianglin.cif.common.service.facade.resp.PartyAttrAccountResp;
import com.xianglin.cif.common.service.facade.resp.PartyAttrPasswordResp;
import com.xianglin.cif.common.service.facade.vo.PersonVo;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.xlnodecore.common.service.facade.enums.DeleteEnum;
import com.xianglin.xlnodecore.common.service.facade.enums.DeviceEnum;
import com.xianglin.xlnodecore.common.service.facade.enums.OperationLogEnum;
import com.xianglin.xlnodecore.common.service.facade.req.OperationLogReq;
import com.xianglin.xlnodecore.common.service.facade.resp.AccountNodeManagerResp;
import com.xianglin.xlnodecore.common.service.facade.resp.OperateLogResp;
import com.xianglin.xlnodecore.common.service.facade.vo.OperationLogVo;
import com.xianglin.xlnodecore.common.service.integration.AccountNodeManagerClient;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/11/1
 * Time: 14:54
 */
@Service("personInfoService")
@ServiceInterface
public class PersonInfoServiceImpl implements PersonInfoService {

    private static final Logger logger = LoggerFactory.getLogger(PersonInfoServiceImpl.class);

    @Autowired
    private AccountNodeManagerClient accountNodeManagerClient;

    @Autowired
    private PartyAttrAccountServiceClient partyAttrAccountServiceClient;

    @Autowired
    private LoginAttrUtil loginAttrUtil;

    @Autowired
    private TransferServiceClient transferServiceClient;

    @Autowired
    private CustomersInfoService customersInfoService;
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonInfoService.isExistPartyAttrAccount", description = "资金账户是否开通")
    public Response<Boolean> isExistPartyAttrAccount(Long partyId) {
        logger.info("====是否开通资金账户===,{}", partyId);
        Response<Boolean> response = ResponseUtils.successResponse();

        Long userId = partyId;// loginAttrUtil.getPartyId();
        if(userId == null || userId == 0){
            userId = loginAttrUtil.getPartyId();
        }
        if (null == userId) {
            response.setResonpse(ResponseEnum.SESSION_INVALD);
            return response;
        }
        PartyAttrAccountResp resp = partyAttrAccountServiceClient.getPartyAttrAccount(partyId);
        if (ResponseConstants.FacadeEnums.ACCOUNT_IS_EXISTS.code.equals(resp.getCode())) {
            response.setResult(true);
        } else {
            response.setResult(false);
        }


        return response;
    }
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonInfoService.isShowAccountTips", description = "资金账户是否显示tips")
    public Response<String> isShowAccountTips(Long partyId){
        logger.info("====资金帐户提示是否显示===,{}", partyId);
        Response<String> response = ResponseUtils.successResponse();
        Long userId = partyId;// loginAttrUtil.getPartyId();
        if(userId == null || userId == 0){
            userId = loginAttrUtil.getPartyId();
        }
        if (null == userId) {
            response.setResonpse(ResponseEnum.SESSION_INVALD);
            return response;
        }
        PartyAttrAccountResp resp = partyAttrAccountServiceClient.getPartyAttrAccount(partyId);
        if (ResponseConstants.FacadeEnums.ACCOUNT_IS_EXISTS.code.equals(resp.getCode())) {
            com.xianglin.te.common.service.facade.resp.Response<String> balance = transferServiceClient.abQuery(partyId);
            if(FacadeEnums.OK.code == balance.getCode() ){
                BigDecimal b= new BigDecimal(balance.getResult());
                if(BigDecimal.ZERO.compareTo(b)<0) {
                    response.setResult("去提现");
                }
            }
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonInfoService.isSetTradePassword", description = "资金账户是否开通")
    public Response<Boolean> isSetTradePassword(Long partyId) {

        logger.info("====是否设置交易密码===，{}", partyId);
        Response<Boolean> response = ResponseUtils.successResponse();
        Long userId = partyId;
        if(userId == null || userId == 0){
            userId = loginAttrUtil.getPartyId();
        }
        if (null == userId || userId == 0) {
            logger.warn("====未从session取得登陆信息====");
            response.setResonpse(ResponseEnum.SESSION_INVALD);
            return response;
        }

        PartyAttrPasswordResp resp = partyAttrAccountServiceClient.selectTradePwd(userId);
        if (ResponseConstants.FacadeEnums.OK.code.equals(resp.getCode())) {
            response.setResult(true);
        } else {
            response.setResult(false);
        }
        return response;

    }
    /**
     * 在app 1.2时使用该接口，1.3后@see com.xianglin.appserv.common.service.facade.UserService
     * @param partyId
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonInfoService.getPersonInfo", description = "资金账户是否开通")
    public Response<AccountNodeManagerVo> getPersonInfo(Long partyId) {

        Response<AccountNodeManagerVo> response = ResponseUtils.successResponse();
        //Long userId = partyId;
        Long userId = loginAttrUtil.getPartyId();
        if (null == userId) {
            logger.warn("====未从session取得登陆信息====");
            response.setResonpse(ResponseEnum.SESSION_INVALD);
            return response;
        }
        AccountNodeManagerVo accountNodeManagerVo = null ;
        try {
            AccountNodeManagerResp resp = accountNodeManagerClient.queryNodeManagerByPartyId(userId);
            if (FacadeEnums.OK.code == resp.getCode()) {
                com.xianglin.xlnodecore.common.service.facade.vo.AccountNodeManagerVo vo = resp.getVo();
                accountNodeManagerVo = DTOUtils.map(vo, AccountNodeManagerVo.class);
                accountNodeManagerVo.setCredentialsNumber(XLCommonUtils.maskIDCardNo(accountNodeManagerVo.getCredentialsNumber()));
                accountNodeManagerVo.setMobilePhone(XLCommonUtils.maskMobile(accountNodeManagerVo.getMobilePhone()));
                response.setResult(accountNodeManagerVo);
            }
            //查询操作日志
//            OperationLogReq req = new OperationLogReq();
//            req.setObjectId(userId);
//            req.setAccountType(OperationLogEnum.NODE_MANAGER.getValue());
//            req.setClientDevice(DeviceEnum.WECHAT.toString());
//            req.setTableName("XLC_ACCOUNT_NODEMANAGER");
//            OperateLogResp logResp = logServiceClient.getOperationLog(req);
//            if (FacadeEnums.OK.code == logResp.getCode()) {
//                List<OperationLogVo> logVoList = logResp.getVoListOpLog();
//                if (CollectionUtils.isNotEmpty(logVoList)) {
//                    List<String> logStrList = new ArrayList<>(logVoList.size());
//                    for (OperationLogVo logvo : logVoList
//                            ) {
//                        logStrList.add(logvo.getComments());
//                    }
//
//                    if(null == accountNodeManagerVo){
//                        accountNodeManagerVo = new AccountNodeManagerVo();
//                    }
//                    accountNodeManagerVo.setLogComments(logStrList);
//                }
//            }
        } catch (Exception e) {
            response.setResonpse(ResponseEnum.DATA_ERROR);
            logger.error("", e);
            // e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response<String> getVertifyLevel() {
        Response<String> response = ResponseUtils.successResponse();
        //获取实名认证等级
        PersonReq req = new PersonReq();
        PersonVo personVo = new PersonVo();
        personVo.setIsDeleted(DeleteEnum.NOT_DEL.getCode());
        Long userId = loginAttrUtil.getPartyId();
        String level = customersInfoService.selectCustomsAlready2Auth(userId).getResult().getAuthLevel();
        String vertifyLevel = level;
        response.setResult(vertifyLevel);
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonInfoService.getRowInfo", description = "我的页面每行信息")
    public Response<MyInfoRowDTO> getRowInfo(){

        Response<MyInfoRowDTO> response =ResponseUtils.successResponse();
        MyInfoRowDTO info = new MyInfoRowDTO();
        info.setAboutAppRow("true");
        info.setInviteRow(SysConfigUtil.getStr("INVITE_ACTIVITY_SHOW","false"));
        Long userId = loginAttrUtil.getPartyId();
        if(null != userId){
            info.setAccountRow("true");
            if(accountTips(userId)){
                info.setAccountRowTip("去提现");
            }
        }
        String userType = loginAttrUtil.getSessionStr(SessionConstants.USER_TYPE);
        if(Constant.UserType.nodeManager.name().equals(userType)) {
            info.setContractManagerRow("true");
        }
        info.setResetPwRow("true");
        info.setUserInfoRow("true");
        response.setResult(info);
        return response;

    }

    private boolean accountTips(Long partyId){
        PartyAttrAccountResp resp = partyAttrAccountServiceClient.getPartyAttrAccount(partyId);
        if (ResponseConstants.FacadeEnums.ACCOUNT_IS_EXISTS.code.equals(resp.getCode())) {
            com.xianglin.te.common.service.facade.resp.Response<String> balance = transferServiceClient.abQuery(partyId);
            if(FacadeEnums.OK.code == balance.getCode() ){
                BigDecimal b= new BigDecimal(balance.getResult());
                if(BigDecimal.ZERO.compareTo(b)<0) {
                    return true;
                }
            }
        }
        return false;
    }
}
