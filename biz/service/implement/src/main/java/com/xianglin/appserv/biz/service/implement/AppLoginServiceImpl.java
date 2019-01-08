/**
 *
 */
package com.xianglin.appserv.biz.service.implement;

import com.alibaba.fastjson.JSON;
import com.xianglin.appserv.biz.shared.*;
import com.xianglin.appserv.biz.shared.listener.ActivityInviteListner;
import com.xianglin.appserv.biz.shared.listener.UserMonthAchieveEvent;
import com.xianglin.appserv.biz.shared.listener.event.UserRegistrationEvent;
import com.xianglin.appserv.biz.shared.util.ResponseCacheUtils;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.daointerface.UserDAO;
import com.xianglin.appserv.common.dal.dataobject.AppClientLog;
import com.xianglin.appserv.common.dal.dataobject.AppClientLogLogin;
import com.xianglin.appserv.common.dal.dataobject.AppPropExtend;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.AppLoginService;
import com.xianglin.appserv.common.service.facade.app.ActivityInviteService;
import com.xianglin.appserv.common.service.facade.app.GoldService;
import com.xianglin.appserv.common.service.facade.model.*;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.UserStatus;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.UserType;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.enums.YesNo;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.integration.cif.AppLoginServiceClient;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.common.util.constant.AppservConstants;
import com.xianglin.appserv.core.model.exception.AppException;
import com.xianglin.cif.common.service.facade.AuthService;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.constant.ResponseConstants.FacadeEnums;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.model.CustomRealnameauthDTO;
import com.xianglin.cif.common.service.facade.model.CustomersDTO;
import com.xianglin.cif.common.service.facade.model.RoleDTO;
import com.xianglin.fala.session.Session;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.xlnodecore.common.service.facade.AccountNodeManagerService;
import com.xianglin.xlnodecore.common.service.facade.base.CommonResp;
import com.xianglin.xlnodecore.common.service.facade.enums.PosLoginTypeEnum;
import com.xianglin.xlnodecore.common.service.facade.resp.AccountNodeManagerResp;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 乡邻APP专用登录服务实现类
 *
 * @author hebbo 2016年8月15日上午9:59:00
 */
@ServiceInterface
public class AppLoginServiceImpl implements AppLoginService {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(AppLoginServiceImpl.class);

    @Autowired
    private AppLoginServiceClient appLoginServiceClient;

    private SessionHelper sessionHelper;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AccountNodeManagerService accountNodeManagerService;

    @Autowired
    private ActivityInviteListner activityInviteListner;

    @Autowired
    private CustomersInfoService customersInfoService;

    @Autowired
    private AuthService authService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PropExtendManager propExtendManager;

    @Autowired
    private ActivityInviteService activityInviteService;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private LogManager logManager;

    @Autowired
    private ResponseCacheUtils responseCacheUtils;

    @Autowired
    private MessageManager messageManager;

    /**
     * 调用站长端的登录
     */
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.login", description = "移动app登录")
    @Override
    public Response<NodeVo> login(LoginDTO loginDTO, DeviceInfo deviceInfo) {

        Long startTime = System.currentTimeMillis();
        logger.info("login loginDTO:{},DeviceInfo:{}, timeStamp:{}", loginDTO, deviceInfo, startTime);
        Response<NodeVo> resp = ResponseUtils.successResponse();
        com.xianglin.cif.common.service.facade.model.Response<com.xianglin.xlnodecore.common.service.facade.vo.NodeVo> response = new com.xianglin.cif.common.service.facade.model.Response<com.xianglin.xlnodecore.common.service.facade.vo.NodeVo>();
        com.xianglin.cif.common.service.facade.model.LoginDTO ld = new com.xianglin.cif.common.service.facade.model.LoginDTO();
        com.xianglin.cif.common.service.facade.model.DeviceInfo di = new com.xianglin.cif.common.service.facade.model.DeviceInfo();
        NodeVo resultVo = new NodeVo();
        //判断是否网点编号登陆开启
        String supportNodeCode = PropertiesUtil.getProperty("app.ios.support.nodecode");
        if (StringUtils.isNotEmpty(supportNodeCode)) {
            if ("false".equals(supportNodeCode) && StringUtils.isNotEmpty(loginDTO.getNodeCode()) && StringUtils.isEmpty(loginDTO.getMobilePhone())) {
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_S_DATA_PARAM_ERROR);
                resp.setMemo("不支持网点编号登陆");
                resp.setTips("不支持网点编号 登陆");
                return resp;
            }
        }
        /** 获取session */
        Session session = sessionHelper.getSession();
        try {
            if (null != loginDTO) {
                BeanUtils.copyProperties(ld, loginDTO);
            }
            if (null != deviceInfo) {
                BeanUtils.copyProperties(di, deviceInfo);
            }
            String deviceId = session.getAttribute(SessionConstants.DEVICE_ID);
            //校验设备绑定基本信息
            di.setDeviceId(deviceId);
            response = appLoginServiceClient.login(ld, di, deviceId);
            //保存站点的详细信息
            if (FacadeEnums.OK.getCode().equals(response.getCode() + "")) {
                com.xianglin.xlnodecore.common.service.facade.vo.NodeVo result = response.getResult();
                //站点经理保存
                AccountNodeManagerVo managerVo = new AccountNodeManagerVo();
                AccountNodeManagerResp managerVoResp = appLoginServiceClient.queryNodeManagerByPartyId(result.getNodeManagerPartyId());
                if (null != managerVoResp && null != managerVoResp.getVo()) {
                    BeanUtils.copyProperties(managerVo, managerVoResp.getVo());
                }
                //保存session
                saveSession(loginDTO, deviceInfo, result.getNodeManagerPartyId(), loginDTO.getPassword(), loginDTO.getNodeCode(), result, managerVo);
                //保存用户信息
                logger.info("login start1 time{}, spend {} ==============", startTime, System.currentTimeMillis() - startTime);

                saveLoginUser(loginDTO.getNodeCode(), result.getNodeManagerPartyId(), deviceId, session.getId(), managerVo.getUserRole(), loginDTO.getPassword());
                logger.info("login start2 time{}, spend {} ==============", startTime, System.currentTimeMillis() - startTime);

                resultVo = (null == response.getResult() ? null : DTOUtils.map(response.getResult(), NodeVo.class));
                if (null != resultVo) {
                    resultVo.setMobilePhone(managerVo.getMobilePhone());
                }
                resp.setResult(resultVo);
                resp.setCode(FacadeEnums.OK.getCode().equals(response.getCode() + "") ? ResultEnum.ResultSuccess.getCode() : response.getCode());
                resp.setMemo(response.getTips());
                resp.setTips(response.getMemo());
                logger.info("login start3 time{}, spend {} ==============", startTime, System.currentTimeMillis() - startTime);
            } else {
                logger.warn("主动登陆失败，loginDTO：{}：{}", loginDTO, response);
                resp.setCode(response.getCode());
                resp.setMemo("登陆失败,请重新登陆");
                resp.setTips("登陆失败,请重新登陆");
            }


        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            logger.info("移动app主动登录异常" + e.getMessage());
        }
        logger.info("login start4 time{}, spend {}===========", startTime, System.currentTimeMillis() - startTime);
        return resp;
    }

    /**
     * 自动登录首先获取session中的站长partyId和password去校验
     *
     * @see com.xianglin.appserv.common.service.facade.AppLoginService#autoLogin(com.xianglin.appserv.common.service.facade.model.LoginDTO, com.xianglin.appserv.common.service.facade.model.DeviceInfo)
     */
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.autoLogin", description = "移动app自动登录")
    @Override
    public Response<NodeVo> autoLogin(LoginDTO loginDTO, DeviceInfo deviceInfo) {

        Response<NodeVo> resp = ResponseUtils.successResponse();
        com.xianglin.cif.common.service.facade.model.Response<com.xianglin.xlnodecore.common.service.facade.vo.NodeVo> response = new com.xianglin.cif.common.service.facade.model.Response<com.xianglin.xlnodecore.common.service.facade.vo.NodeVo>();
        com.xianglin.cif.common.service.facade.model.LoginDTO ld = new com.xianglin.cif.common.service.facade.model.LoginDTO();
        com.xianglin.cif.common.service.facade.model.DeviceInfo di = new com.xianglin.cif.common.service.facade.model.DeviceInfo();
        /** 获取session */
        Session session = sessionHelper.getSession();
        try {
            if (null != loginDTO) {
                BeanUtils.copyProperties(ld, loginDTO);
            }
            if (null != deviceInfo) {
                BeanUtils.copyProperties(di, deviceInfo);
            }
            String deviceId = session.getAttribute(SessionConstants.DEVICE_ID);

            response = appLoginServiceClient.autoLogin(ld, di, deviceId);
            //保存站点的详细信息
            if (FacadeEnums.OK.getCode().equals(response.getCode() + "")) {
                BeanUtils.copyProperties(loginDTO, ld);
                com.xianglin.xlnodecore.common.service.facade.vo.NodeVo result = response.getResult();
                //站点经理保存
                AccountNodeManagerVo managerVo = new AccountNodeManagerVo();
                AccountNodeManagerResp managerVoResp = appLoginServiceClient.queryNodeManagerByPartyId(result.getNodeManagerPartyId());
                if (null != managerVoResp && null != managerVoResp.getVo()) {
                    BeanUtils.copyProperties(managerVo, managerVoResp.getVo());
                }
                managerVo.setNodePartyId(result.getNodePartyId());
                //保存session
                saveLoginUser(loginDTO.getMobilePhone(), result.getNodeManagerPartyId(), deviceId, session.getId(), managerVo.getUserRole(), loginDTO.getPassword());
                saveSession(loginDTO, deviceInfo, result.getNodeManagerPartyId(), loginDTO.getPassword(), null, result, managerVo);
                resp.setResult(null == response.getResult() ? null : DTOUtils.map(response.getResult(), NodeVo.class));
                resp.setCode(ResultEnum.ResultSuccess.getCode());
                resp.setMemo(response.getTips());
                resp.setTips(response.getMemo());
            } else {
                logger.warn("自动登陆失败，loginDTO：{}：{}", loginDTO, response);
                resp.setCode(response.getCode());
                resp.setMemo("登陆失败,请重新登陆");
                resp.setTips("登陆失败,请重新登陆");
            }


        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            logger.info("移动app自动登录异常" + e.getMessage());
        }
        return resp;
    }

    /**
     * 自动登录首先获取session中的站长partyId和password去校验
     *
     * @see com.xianglin.appserv.common.service.facade.AppLoginService#autoLogin(com.xianglin.appserv.common.service.facade.model.LoginDTO, com.xianglin.appserv.common.service.facade.model.DeviceInfo)
     */
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.autoLoginVOPT", description = "移动app自动登录V1.3+")
    @Override
    public Response<NodeVo> autoLoginVOPT(LoginDTO loginDTO, DeviceInfo deviceInfo) {

        Response<NodeVo> resp = ResponseUtils.successResponse();
        try {
            loginDTO.setDeviceId(deviceInfo.getDeviceId());
            NodeVo vo = loginV3(loginDTO);
            resp.setResult(vo);
            saveLoginRecord("autoLoginVOPT",deviceInfo);
        } catch (Exception e) {
            logger.info("移动app自动登录异常", e);
            resp.setCode(ResultEnum.ResultSuccess.getCode());
            resp.setTips("登陆失败,进入游客模式");
            resp.setMemo("登陆失败,进入游客模式");
        }
        return resp;
    }

    private Response<NodeVo> autoLoginBack(LoginDTO loginDTO, DeviceInfo deviceInfo) {

        Response<NodeVo> resp = ResponseUtils.successResponse();
        com.xianglin.cif.common.service.facade.model.Response<com.xianglin.xlnodecore.common.service.facade.vo.NodeVo> response = new com.xianglin.cif.common.service.facade.model.Response<com.xianglin.xlnodecore.common.service.facade.vo.NodeVo>();
        com.xianglin.cif.common.service.facade.model.LoginDTO ld = new com.xianglin.cif.common.service.facade.model.LoginDTO();
        com.xianglin.cif.common.service.facade.model.DeviceInfo di = new com.xianglin.cif.common.service.facade.model.DeviceInfo();
        com.xianglin.cif.common.service.facade.model.Response<String> cifLoginResp = null;
        NodeVo resultVo = new NodeVo();
        /** 获取session */
        Session session = sessionHelper.getSession();
        AccountNodeManagerVo managerVo = new AccountNodeManagerVo();
        managerVo.setUserRole(UserType.visitor.name());
        try {
            if (null != loginDTO) {
                BeanUtils.copyProperties(ld, loginDTO);
            }
            if (null != deviceInfo) {
                BeanUtils.copyProperties(di, deviceInfo);
            }
            String deviceId = session.getAttribute(SessionConstants.DEVICE_ID);
            String userType = UserType.visitor.name();
            com.xianglin.xlnodecore.common.service.facade.vo.NodeVo isNodeManager = appLoginServiceClient.queryNodeInfoByNodeManagerPartyId(ld.getPartyId());
            if (isNodeManager != null) {
                //是站长，执行站长登录
                response = appLoginServiceClient.autoLogin(ld, di, deviceId);
                userType = UserType.nodeManager.name();
            } else {
                //普通用户登录
                ld.setPartyId(loginDTO.getPartyId());
                cifLoginResp = appLoginServiceClient.cifAutoLogin(ld, di, deviceId);
                userType = UserType.user.name();
            }
            managerVo.setUserRole(userType);
            logger.info("ld == {}", ld);
//            PersonVo personVo = getPersonInfo(ld.getPartyId());
//            logger.info("personVo == {}", personVo);
            if (UserType.nodeManager.name().equals(userType) && FacadeEnums.OK.getCode().equals(response.getCode() + "")) {
                //保存站点的详细信息
                BeanUtils.copyProperties(loginDTO, ld);
                com.xianglin.xlnodecore.common.service.facade.vo.NodeVo result = response.getResult();
                //站点经理保存
                AccountNodeManagerResp managerVoResp = appLoginServiceClient.queryNodeManagerByPartyId(result.getNodeManagerPartyId());
                if (null != managerVoResp && null != managerVoResp.getVo()) {
                    BeanUtils.copyProperties(managerVo, managerVoResp.getVo());
                }
                managerVo.setNodePartyId(result.getNodePartyId());
                User su = saveLoginUser(loginDTO.getMobilePhone(), result.getNodeManagerPartyId(), deviceId, session.getId(), managerVo.getUserRole(), loginDTO.getPassword());
                //查询app_user表中用户信息
                getUserInfo(ld, managerVo);
                //保存session
//                if (personVo == null) {
//                    personVo = wrapPerson(managerVo.getPartyId(), managerVo.getMobilePhone(), managerVo.getTrueName(), BusinessConstants.PartyLevel.V0.code);
//                }
//                saveSession(loginDTO, deviceInfo, loginDTO.getPassword(), resultVo, managerVo, personVo);
                //  saveSession(loginDTO, deviceInfo, result.getNodeManagerPartyId(), loginDTO.getPassword(), null, result, managerVo);
                //保存用户信息
                resp.setResult(null == response.getResult() ? null : DTOUtils.map(response.getResult(), NodeVo.class));
                resp.getResult().setUserType(managerVo.getUserRole());
                resp.getResult().setRyToken(su.getRyToken());
                resp.setCode(ResultEnum.ResultSuccess.getCode());
                resp.setMemo(response.getTips());
                resp.setTips(response.getMemo());

            } else if (UserType.user.name().equals(userType) && cifLoginResp != null && FacadeEnums.OK.code.equals(cifLoginResp.getCode() + "")) {
                managerVo.setPartyId(ld.getPartyId());
                User su = saveLoginUser(ld.getMobilePhone(), ld.getPartyId(), deviceId, session.getId(), managerVo.getUserRole(), loginDTO.getPassword());
                managerVo.setMobilePhone(su.getLoginName());//自动登录设置手机号为登录名
                getUserInfo(ld, managerVo);

//                managerVo.setTrueName(personVo.getTrueName());
//                if (personVo == null) {
//                    personVo = wrapPerson(ld.getPartyId(), ld.getMobilePhone(), ld.getMobilePhone(), BusinessConstants.PartyLevel.V0.code);
//                }
//                managerVo.setGender(personVo.getGender());
//                saveSession(loginDTO, deviceInfo, loginDTO.getPassword(), null, managerVo, personVo);
                /**
                 * 普通用户信息适配开始
                 */
                resultVo.setRyToken(su.getRyToken());
                resultVo.setNodeManagerPartyId(ld.getPartyId());
                resultVo.setUserType(managerVo.getUserRole());
                resultVo.setNodePartyId(-1l);//村民没有网点设置为-1
                resultVo.setMobilePhone(ld.getMobilePhone());
//                resultVo.setNodeManagerName(personVo.getTrueName());

                if (managerVo.getUserInfoDTO() != null) {
                    resultVo.setAvatar(managerVo.getUserInfoDTO().getHeadImg());
                }
                resp.setResult(resultVo);
                resp.setMemo(cifLoginResp.getTips());
                resp.setTips(cifLoginResp.getMemo());
                String comment = ld.getMobilePhone() + "自动登录了移动app";
//                appLoginServiceClient.saveLog(ld.getPartyId(), personVo.getTrueName(), personVo.getTrueName(), OperateTypeEnum.APP_LOGIN, comment);
                /**
                 * 普通用户信息适配结束
                 */
            } else {
                logger.warn("自动登陆失败，loginDTO：{}：{}", loginDTO, response);
                resp.setCode(ResultEnum.ResultSuccess.getCode());
                resp.setMemo("登陆失败,进入游客模式");
                resp.setTips("登陆失败,进入游客模式");
                //session.setAttribute(SessionConstants.XL_QY_USER, managerVo);
                session.setAttribute(SessionConstants.CLIENT_VERSION, loginDTO.getClientVersion());
                session.setAttribute(SessionConstants.USER_TYPE, UserType.visitor.name());
                sessionHelper.saveSession(session);
            }

        } catch (Exception e) {
            resp.setCode(ResultEnum.ResultSuccess.getCode());
            resp.setMemo("登陆失败,进入游客模式");
            resp.setTips("登陆失败,进入游客模式");
            session.setAttribute(SessionConstants.XL_QY_USER, managerVo);
            logger.info("移动app自动登录异常", e);
        }
        return resp;
    }

    /**
     * 帐号登出
     */
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.logout", description = "移动app退出登录")
    @Override
    public Response<Boolean> logout(LoginDTO loginDTO) {
        /** 定义返回对象 */
        Response<Boolean> resp = ResponseUtils.successResponse();
        com.xianglin.cif.common.service.facade.model.Response<Boolean> response;
        /** 获取session */
        Session session = sessionHelper.getSession();
        try {
            if (session == null) {
                logger.info("用户登出，获取SESSION失败。");
                resp.setCode(Integer.valueOf(FacadeEnums.GetSessionFail.code));
                resp.setMemo(FacadeEnums.GetSessionFail.msg);
                resp.setTips(FacadeEnums.GetSessionFail.tip);
                return resp;
            }
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);

//            response = appLoginServiceClient.logout(deviceId, partyId);
            sessionHelper.removeSession(session.getId());
            User user = User.builder().build();
            user.setLoginName(loginDTO.getMobilePhone());
            user.setPartyId(partyId);
            user.setComments("主动退出");
            userManager.logout(user);

            String sid = SysConfigUtil.getStr("article_refuse_users");
            if (StringUtils.isNotEmpty(sid)) {
                logger.info("remove session id {}", sid);
                sessionHelper.removeSession(sid);
            }
//            resp.setCode((ResultEnum.ResultSuccess.getCode() == response.getCode()) ? ResultEnum.ResultSuccess.getCode() : response.getCode());
//            resp.setMemo(response.getTips());
//            resp.setTips(response.getMemo());
//            resp.setResult(response.getResult());
        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            logger.info("移动app登出异常" + e.getMessage());
        }

        return resp;
    }

    /**
     * Description: 短信发送
     *
     * @see com.xianglin.appserv.common.service.facade.AppLoginService#sendSms(com.xianglin.appserv.common.service.facade.model.vo.NodeVo)
     */
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.sendSms", description = "移动app获取验证码")
    @Override
    public Response<Boolean> sendSms(NodeVo nodeVo) {
        /** 定义返回对象 */
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            com.xianglin.cif.common.service.facade.model.Response<Boolean> response = appLoginServiceClient.sendSms(nodeVo.getNodeCode(), nodeVo.getMobilePhone());
            if (null != response && (FacadeEnums.OK.getCode().equals(response.getCode() + "") || AppservConstants.LoginError.NOT_BIND_MOBILE.code == response.getCode())) {
                resp.setCode(ResultEnum.ResultSuccess.getCode());
                resp.setResult(response.getResult());
            } else {
                resp.setCode(response.getCode());
                resp.setMemo(response.getTips());
                resp.setTips(response.getMemo());
            }
        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            resp.setMemo("访问人太多啦，请再试一次");
            resp.setTips("访问人太多啦，请再试一次");
            logger.info(e.getMessage());
        }
        return resp;
    }

    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.smsCodeSend", description = "V1.3后移动app获取验证码")
    @Override
    public Response<Boolean> smsCodeSend(NodeVo nodeVo) {
        /** 定义返回对象 */
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {

            //判断是否是新用户
            String loginAccount = nodeVo.getMobilePhone();//1.3之后之后手机号登录
            boolean isNewUser = false;
            CustomersDTO customersDTO = customersInfoService.selectByMobilePhone(loginAccount).getResult();
            if (customersDTO == null) {
                isNewUser = true;
            }

//            List<Long> partyIds = accountNodeManagerService.getNodeManagerMoreInfoByMobilePhone(nodeVo.getMobilePhone());
//            if (CollectionUtils.isNotEmpty(partyIds) && partyIds.size() > 1) {
//                resp.setCode(AppservConstants.LoginError.MOBILE_BIND_MORE.code);
//                resp.setTips(AppservConstants.LoginError.MOBILE_BIND_MORE.desc);
//                resp.setMemo(AppservConstants.LoginError.MOBILE_BIND_MORE.desc);
//                return resp;
//            }
            com.xianglin.cif.common.service.facade.model.Response<Boolean> response = appLoginServiceClient.sendSmsOnly(loginAccount);
            if (null != response && (FacadeEnums.OK.getCode().equals(response.getCode() + ""))) {
                resp.setCode(ResultEnum.ResultSuccess.getCode());
                if (isNewUser) {
                    resp.setResult(true);
                    resp.setMemo("该号码尚未注册，验证通过将自动注册并登录");
                    resp.setTips("该号码尚未注册，验证通过将自动注册并登录");
                } else {
                    resp.setResult(false);
                }

            } else {
                resp.setCode(response.getCode());
                resp.setMemo(response.getTips());
                resp.setTips(response.getMemo());
            }
        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            resp.setMemo("访问人太多啦，请再试一次");
            resp.setTips("访问人太多啦，请再试一次");
            logger.info(e.getMessage());
        }
        return resp;
    }

    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.smsCodeSendV2", description = "V3.2.1移动app获取验证码")
    @Override
    public Response<Boolean> smsCodeSendV2(String phone) {
        /** 定义返回对象 */
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {

            //判断是否是新用户
            String loginAccount = phone;//1.3之后之后手机号登录
            boolean isNewUser = false;
            CustomersDTO customersDTO = customersInfoService.selectByMobilePhone(phone).getResult();
            if (customersDTO == null) {
                isNewUser = true;
            }

            com.xianglin.cif.common.service.facade.model.Response<Boolean> response = appLoginServiceClient.sendSmsOnly(loginAccount);
            if (null != response && (FacadeEnums.OK.getCode().equals(response.getCode() + ""))) {
                resp.setCode(ResultEnum.ResultSuccess.getCode());
                if (isNewUser) {
                    resp.setResult(true);
                    resp.setMemo("该号码尚未注册，验证通过将自动注册并登录");
                    resp.setTips("该号码尚未注册，验证通过将自动注册并登录");
                } else {
                    resp.setResult(false);
                }
            } else {
                resp.setCode(response.getCode());
                resp.setMemo(response.getTips());
                resp.setTips(response.getMemo());
            }
        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            resp.setMemo("访问人太多啦，请再试一次");
            resp.setTips("访问人太多啦，请再试一次");
            logger.info(e.getMessage());
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.resetPassword", description = "重置登陆密码")
    public Response<Boolean> resetPassword(String password) {

        Response<Boolean> response = ResponseUtils.successResponse(true);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            User user = userManager.queryUser(partyId);
            if (user != null) {
//                if (StringUtils.isEmpty(user.getPasswordSalt())) {
//                    user.setPasswordSalt(UUID.randomUUID().toString());
//                }
//                user.setPassword(Md5Crypt.apr1Crypt(password, user.getPasswordSalt()));
                user.setPassword(password);
                userManager.updateUser(user);
            } else {
                logger.warn("用户不存在");
                response.setResult(false);
            }
        } catch (Exception e) {
            logger.error("resetPassword ", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.loginByPassword", description = "账户密码登陆")
    public Response<NodeVo> loginByPassword(LoginDTO loginDTO) {
        /*
        1,校验密码
        2，判断用户类型
        3，更新用户session信息
        4，保存登陆日志
         */
        Response<NodeVo> response = ResponseUtils.successResponse();
        try {
            User user = userManager.getUserByLoginAccount(loginDTO.getMobilePhone());
            if (user == null) {
                response.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_VALDATE_MANAGER_MOBILE_NOTEXIST);
            } else {
                //判断登陆次数
                int errorCount = appLoginServiceClient.queryLogCcount(user.getPartyId(), PosLoginTypeEnum.AccessAccountByPas.getTips());
                if (errorCount >= 5) {
                    response.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_VALDATE_POS_LOGIN_PWDERROR);
                    response.setTips("您的操作次数过多，请在一个小时后重试！");
                    return response;
                }
                if (!StringUtils.equals(user.getPassword(), loginDTO.getPassword())) {
//                    appLoginServiceClient.saveLog(user.getPartyId(), user.getNikerName(), user.getNikerName(), OperateTypeEnum.APP_LOGIN, "登陆app", PosLoginTypeEnum.AccessAccountByPas.getTips());
                    response.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_VALDATE_POS_LOGIN_PWDERROR);
                } else {
                    String userType = UserType.user.name();
                    NodeVo vo = new NodeVo();
                    //判断是否是站长
                    com.xianglin.xlnodecore.common.service.facade.vo.NodeVo managerVoResp = appLoginServiceClient.queryNodeInfoByNodeManagerPartyId(user.getPartyId());
                    logger.info("partyIdResp :{}", ToStringBuilder.reflectionToString(managerVoResp));
                    if (managerVoResp != null) {//站长
                        userType = UserType.nodeManager.name();
                        logger.info("managerVoResp :{}", ToStringBuilder.reflectionToString(managerVoResp));
                        vo = DTOUtils.map(managerVoResp, NodeVo.class);
                    }

                    AccountNodeManagerVo managerVo = new AccountNodeManagerVo();
                    managerVo.setUserRole(userType);
                    loginDTO.setPartyId(user.getPartyId());
                    getUserInfo(DTOUtils.map(loginDTO, com.xianglin.cif.common.service.facade.model.LoginDTO.class), managerVo);

//                    PersonVo personVo = getPersonInfo(user.getPartyId());
                    user.setUserType(userType);
                    managerVo.setPartyId(user.getPartyId());
//                    appLoginServiceClient.saveLog(user.getPartyId(), user.getNikerName(), user.getNikerName(), OperateTypeEnum.APP_LOGIN, "登陆app");
                    user = saveLoginUser(loginDTO.getMobilePhone(), user.getPartyId(), loginDTO.getDeviceId(), sessionHelper.getSession().getId(), userType, loginDTO.getPassword());
                    saveSession(user);
                    vo.setRyToken(user.getRyToken());
                    vo.setNodeManagerPartyId(user.getPartyId());
                    vo.setUserType(userType);
                    if (vo.getNodePartyId() == null) {
                        vo.setNodePartyId(-1L);
                    }
                    vo.setMobilePhone(user.getLoginName());
                    response.setResult(vo);
                }
            }
            saveLoginRecord("loginByPassword",null);
        } catch (Exception e) {
            logger.error("loginByPassword ", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.hasPassword", description = "判断账号是否设置密码")
    public Response<Boolean> hasPassword(String mobilePhone) {
        /** 定义返回对象 */
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            //判断是否是新用户
            String loginAccount = mobilePhone;
            User u = userManager.getUserByLoginAccount(loginAccount);
            if (u != null && StringUtils.isNotEmpty(u.getPassword())) {
                resp.setResult(true);
            }
        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            logger.info("查询密码异常" + e.getMessage());
        }
        return resp;
    }

    /**
     * 用户验证码登陆
     *
     * @param loginDTO
     * @param deviceInfo
     * @return
     * @since v3.2.1
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.loginMobileV2", description = "用户验证码登陆")
    public Response<String> loginMobileV2(LoginDTO loginDTO, DeviceInfo deviceInfo) {

        Response<String> resp = ResponseUtils.successResponse();
        try {
            Response loginResp = loginByMobileV1_3(loginDTO, deviceInfo);
            resp.setResult(queryLoginStatus());
            if (!loginResp.isSuccess()) {
                resp.setCode(loginResp.getCode());
                resp.setTips(loginResp.getTips());
                resp.setMemo(loginResp.getMemo());
            }
            resp.setResult(queryLoginStatus());
            saveLoginRecord("loginMobileV2",deviceInfo);
        } catch (Exception e) {
            logger.error("loginMobileV2 ", e);
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 用户密码登陆
     *
     * @param loginDTO
     * @return
     * @since v3.2.1
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.loginPasswordV2", description = "用户密码登陆")
    public Response<String> loginPasswordV2(LoginDTO loginDTO) {

        Response<String> resp = ResponseUtils.successResponse();
        try {
            Response loginResp = loginByPassword(loginDTO);
            resp.setResult(queryLoginStatus());
            if (!loginResp.isSuccess()) {
                resp.setCode(loginResp.getCode());
                resp.setTips(loginResp.getTips());
                resp.setMemo(loginResp.getMemo());
            }
            saveLoginRecord("loginPasswordV2",null);
        } catch (Exception e) {
            logger.error("loginPasswordV2 ", e);
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 实名认证信息同步
     *
     * @param partyId
     */
    private void synchroRealName(Long partyId) {

        if (partyId != null) {
            //查询实名认证信息
            com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> resp = customersInfoService.selectCustomsAlready2Auth(partyId);
            if (resp.getResult() != null) {
                if (StringUtils.isNotEmpty(resp.getResult().getCredentialsNumber())) {
                    String trueName = resp.getResult().getCustomerName();
                    //根据partyID查用户
                    User user = userManager.queryUser(partyId);
                    if (StringUtils.isNotEmpty(trueName)) {
                        if (!StringUtils.equals(user.getTrueName(), trueName)) {
                            userManager.updateUser(User.builder().trueName(trueName).id(user.getId()).build());
                        }
                    } else {
                        userManager.updateUser(User.builder().trueName("").id(user.getId()).build());
                    }

                }
            }
        }
    }

    /**
     * 用户密码登陆
     *
     * @param password
     * @return
     * @since v3.2.1
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.setPassword", description = "用户设置密码")
    public Response<String> setPassword(String password) {

        Response<String> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            User user = userManager.queryUser(partyId);
            user.setPassword(password);
            userManager.updateUser(user);
            resp.setResult(queryLoginStatus());
        } catch (Exception e) {
            logger.error("setPassword ", e);
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 用户实名认证
     *
     * @param vo
     * @return
     * @since v3.2.1
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.realNameAuth", description = "用户实名认证")
    public Response<String> realNameAuth(RealNameVo vo) {

        Response<String> resp = ResponseUtils.successResponse();
        try {
            userRealNameAuth(vo, resp);
            resp.setResult(queryLoginStatus());
            if (StringUtils.equals(resp.getResult(), Constant.LoginResult.SS.name())) {//异步发放v321活动奖励
                Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
                activityManager.rewardV321(partyId,Constant.ActivityTaskType.REGISTER,Constant.ActivityTaskType.INVITE);
                activityManager.rewardV321(partyId,Constant.ActivityTaskType.REALNAME_AUTH,Constant.ActivityTaskType.F_REALNAME_AUTH);
                activityManager.rewardV321(partyId,Constant.ActivityTaskType.PERFECT_DATA,Constant.ActivityTaskType.F_PERFECT_DATA);

            }
        } catch (AppException e) {
            logger.warn("realNameAuth", e);
            resp.setFacade(e.getFacade());
        } catch (Exception e) {
            logger.warn("realNameAuth ", e);
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }


    /**
     * 用户信息更新及实名认证
     *
     * @param vo
     * @param resp
     * @throws AppException
     */
    private void userRealNameAuth(RealNameVo vo, Response resp) throws AppException {

        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        User user = userManager.queryUser(partyId);
        AreaVo areaVo = new AreaVo();
        areaVo.setCounty(vo.getCounty());
        areaVo.setProvince(vo.getProvince());
        areaVo.setTown(vo.getTown());
        areaVo.setVillage(vo.getVillage());
        areaVo.setCity(vo.getCity());
        user.setDistrict(areaVo.toString());
        user.setProvince(areaVo.getProvinceName());
        user.setCity(areaVo.getCityName());
        user.setCounty(areaVo.getCountyName());
        user.setTown(areaVo.getTownName());
        user.setVillage(areaVo.getVillageName());
        if (StringUtils.isNotEmpty(vo.getIdNumber())) {
            char c = vo.getIdNumber().charAt(vo.getIdNumber().length() - 1);
            if (c % 2 == 0) {
                user.setGender("女");
            } else {
                user.setGender("男");
            }
        }
        userManager.updateUser(user);
        if (StringUtils.isEmpty(user.getTrueName())) {
            CustomersDTO customers = new CustomersDTO();
            customers.setPartyId(partyId);
            customers.setMobilePhone(user.getLoginName());
            customers.setCustomerName(vo.getUserName());
            customers.setCredentialsType("身份证");
            customers.setCredentialsNumber(vo.getIdNumber());
            customers.setCreator(partyId + "");
            com.xianglin.cif.common.service.facade.model.Response<CustomRealnameauthDTO> resp1 = authService.twoFactorAuth(customers, "app");
            logger.info("cif secondAuth resp :{}", ToStringBuilder.reflectionToString(resp1));
            if (resp1.getCode() == 200000) {
                user.setTrueName(resp1.getResult().getCredentialsName());
                userManager.updateUser(user);
            } else {
                resp.setCode(resp1.getCode());
                resp.setTips(resp1.getTips());
            }
        }
    }

    /**
     * 设置用户信息
     *
     * @param user
     * @return
     * @since v3.2.1
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.submitUserInfo", description = "置用户信息，并完成登陆")
    public Response<String> submitUserInfo(UserVo user) {

        Response<String> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            User updateUser = userManager.queryUser(partyId);
            updateUser.setHeadImg(user.getHeadImg());
            updateUser.setPartyId(partyId);
            updateUser.setGender(user.getGender());
            updateUser.setBirthday(user.getBirthday());
            updateUser.setNikerName(user.getNikerName());
            if (StringUtils.isEmpty(updateUser.getHeadImg())) {
                updateUser.setHeadImg(SysConfigUtil.getStr("default_user_headimg"));
            }
            userManager.updateUser(updateUser);
            resp.setResult(queryLoginStatus());
            if (StringUtils.equals(resp.getResult(), Constant.LoginResult.SS.name())) {//异步发放v321活动奖励
                activityManager.rewardV321(updateUser.getPartyId(),Constant.ActivityTaskType.REGISTER,Constant.ActivityTaskType.INVITE);
                activityManager.rewardV321(updateUser.getPartyId(),Constant.ActivityTaskType.REALNAME_AUTH,Constant.ActivityTaskType.F_REALNAME_AUTH);
                activityManager.rewardV321(updateUser.getPartyId(),Constant.ActivityTaskType.PERFECT_DATA,Constant.ActivityTaskType.F_PERFECT_DATA);

            }
        } catch (Exception e) {
            logger.error("submitUserInfo ", e);
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.autoLoginV2", description = "自动登陆，返回下一步操作")
    public Response<String> autoLoginV2(LoginDTO loginDTO, DeviceInfo deviceInfo) {

        Response<String> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(AppSessionConstants.PARTY_ID, Long.class);
            User user = userManager.queryUser(partyId);
            if (user != null && StringUtils.equals(loginDTO.getDeviceId(), user.getDeviceId())) {
                loginV3(loginDTO);
                sessionHelper.getSession().setAttribute(AppSessionConstants.PARTY_ID, partyId);
                resp.setResult(queryLoginStatus());
            } else {
                sessionHelper.getSession().removeAttribute(AppSessionConstants.PARTY_ID);
                resp.setResult(Constant.LoginResult.NL.name());
            }
            userManager.saveUpdatePush(AppPushVo.builder().deviceId(loginDTO.getDeviceId()).partyId(partyId).version(loginDTO.getClientVersion()).systemType(deviceInfo.getSystemType()).build());
            saveLoginRecord("autoLoginV2",deviceInfo);
        } catch (Exception e) {
            logger.error("autoLoginV2 ", e);
            resp.setResult(Constant.LoginResult.NL.name());
        }
        return resp;
    }

    /**
     * 用户验证码登陆
     *
     * @param loginDTO
     * @param deviceInfo
     * @return
     * @since v3.5
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.loginMobileV3", description = "用户验证码登陆")
    public Response<String> loginMobileV3(LoginDTO loginDTO, DeviceInfo deviceInfo) {

        Response<String> resp = ResponseUtils.successResponse();
        try {
            Response loginResp = loginByMobileV1_3(loginDTO, deviceInfo);
            resp.setResult(queryLoginStatus());
            if (!loginResp.isSuccess()) {
                resp.setCode(loginResp.getCode());
                resp.setTips(loginResp.getTips());
                resp.setMemo(loginResp.getMemo());
            }
            resp.setResult(queryLoginStatusV3());
            saveLoginRecord("loginMobileV3",deviceInfo);
        } catch (Exception e) {
            logger.error("loginMobileV2 ", e);
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 用户密码登陆
     *
     * @param loginDTO
     * @return
     * @since v3.5
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.loginPasswordV3", description = "用户密码登陆")
    public Response<String> loginPasswordV3(LoginDTO loginDTO) {

        Response<String> resp = ResponseUtils.successResponse();
        try {
            Response loginResp = loginByPassword(loginDTO);
            resp.setResult(queryLoginStatusV3());
            if (!loginResp.isSuccess()) {
                resp.setCode(loginResp.getCode());
                resp.setTips(loginResp.getTips());
                resp.setMemo(loginResp.getMemo());
            }
            saveLoginRecord("loginPasswordV3",null);
        } catch (Exception e) {
            logger.error("loginPasswordV2 ", e);
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 自动登陆，返回下一步操作
     *
     * @param loginDTO
     * @param deviceInfo
     * @return
     * @since v3.5
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.autoLoginV3", description = "自动登陆，返回下一步操作")
    public Response<String> autoLoginV3(LoginDTO loginDTO, DeviceInfo deviceInfo) {

        Response<String> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(AppSessionConstants.PARTY_ID, Long.class);
            User user = userManager.queryUser(partyId);
            if (user != null && StringUtils.equals(loginDTO.getDeviceId(), user.getDeviceId())) {
                loginV3(loginDTO);
                sessionHelper.getSession().setAttribute(AppSessionConstants.PARTY_ID, partyId);
                resp.setResult(queryLoginStatusV3());
            } else {
                sessionHelper.getSession().removeAttribute(AppSessionConstants.PARTY_ID);
                resp.setResult(Constant.LoginResult.NL.name());
            }
            AppPushVo vo = AppPushVo.builder().deviceId(loginDTO.getDeviceId()).partyId(partyId).version(loginDTO.getClientVersion()).systemType(deviceInfo.getSystemType()).build();
            userManager.saveUpdatePush(vo);

            saveLoginRecord("autoLoginV3",deviceInfo);
        } catch (Exception e) {
            logger.error("autoLoginV2 ", e);
            resp.setResult(Constant.LoginResult.NL.name());
        }
        return resp;
    }

    /**
     * 用户实名认证
     *
     * @param vo
     * @return
     * @since v3.5
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.realNameAuthV3", description = "用户实名认证")
    public Response<String> realNameAuthV3(RealNameVo vo) {

        Response<String> resp = ResponseUtils.successResponse();
        try {
            userRealNameAuth(vo, resp);
            resp.setResult(queryLoginStatusV3());
            Long partyId = sessionHelper.getSessionProp(AppSessionConstants.PARTY_ID, Long.class);
            if (StringUtils.equals(resp.getResult(), Constant.LoginResult.SS.name())) {//异步发放v321活动奖励
                User currentUser = userManager.queryUser(partyId);
                activityManager.rewardV321(currentUser.getPartyId(),Constant.ActivityTaskType.REGISTER,Constant.ActivityTaskType.INVITE);
                activityManager.rewardV321(currentUser.getPartyId(),Constant.ActivityTaskType.REALNAME_AUTH,Constant.ActivityTaskType.F_REALNAME_AUTH);
                activityManager.rewardV321(currentUser.getPartyId(),Constant.ActivityTaskType.PERFECT_DATA,Constant.ActivityTaskType.F_PERFECT_DATA);

                //家谱分享外链--处理添加
                UserRegistrationEvent registrationEvent = new UserRegistrationEvent(this, userManager.queryUser(partyId));
                applicationContext.publishEvent(registrationEvent);
            }
        } catch (AppException e) {
            logger.warn("realNameAuthV3", e);
            resp.setFacade(e.getFacade());
        } catch (Exception e) {
            logger.error("realNameAuth ", e);
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    public Response<Boolean> cancellation(String mobilePhone) {

        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            User user = userManager.getUserByLoginAccount(mobilePhone);
            if (user != null && StringUtils.isEmpty(user.getTrueName())) {
                userManager.deleteUser(user.getId());
                resp.setResult(true);
            } else {
                logger.warn("用户{}不存在或已经实名，无法被删除", mobilePhone);
            }
        } catch (Exception e) {
            logger.error("cancellation ", e);
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    public Response<Boolean> resetMobile(String mobilePhone, String
            newMobile) {

        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            User user = userManager.getUserByLoginAccount(mobilePhone);
            if (user != null && StringUtils.isEmpty(user.getTrueName())) {
                User updateUser = User.builder().id(user.getId()).newLoginName(newMobile).build();
                userManager.updateUser(updateUser);
                if (StringUtils.isNotEmpty(user.getSessionId())) {
                    sessionHelper.removeSession(user.getSessionId());
                }
            } else {
                logger.warn("用户{}不存在或已经实名，无法被删除", mobilePhone);
            }
        } catch (Exception e) {
            logger.error("cancellation ", e);
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.loginV4", description = "用户注册（登陆）V4")
    public Response<LoginVo> loginV4(LoginDTO loginDTO, DeviceInfo deviceInfo) {
        LoginVo userVo = new LoginVo();
        Response<LoginVo> resp = ResponseUtils.successResponse(userVo);
        try {
            Session session = sessionHelper.getSession();
            if (loginDTO == null || deviceInfo == null || StringUtils.isEmpty(loginDTO.getMobilePhone())) {
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            boolean isNewUser = false;//是否新用户
            boolean sendNewUserReward = false;
            Response<Boolean> checkLogin = checkLogin(loginDTO.getMobilePhone(), loginDTO.getSmsCode(), loginDTO.getPassword(), loginDTO.getType());
            if (checkLogin.isSuccess()) {
                User user = userManager.getUserByLoginAccount(loginDTO.getMobilePhone());
                if (user == null) { //新用户
                    CustomersDTO customers = customersInfoService.selectByMobilePhone(loginDTO.getMobilePhone()).getResult();
                    logger.info("cif open selectByMobilePhone resp:{}", ToStringBuilder.reflectionToString(customers));
                    if (customers == null || StringUtils.equals("app",customers.getCreator())) {//已注册用户
                        sendNewUserReward = true;
                    }
                    CustomersDTO customersDTO = new CustomersDTO();
                    customersDTO.setMobilePhone(loginDTO.getMobilePhone());
                    customersDTO.setCreator(loginDTO.getMobilePhone());
                    Optional.ofNullable(loginDTO.getRecCode()).ifPresent(v -> {
                        Optional.ofNullable(customersInfoService.selectByInvitationCode(v).getResult()).ifPresent(v1 -> {
                            customersDTO.setInvitationPartyId(v1.getPartyId());
                        });
                    });
                    List<RoleDTO> roleDTOs = new ArrayList<>();
                    RoleDTO role = new RoleDTO();
                    role.setRoleCode("APP_USER");
                    role.setRoleName("APP用户");
                    roleDTOs.add(role);
                    customersDTO.setRoleDTOs(roleDTOs);
                    logger.info("cif openAccount req:{}", ToStringBuilder.reflectionToString(customersDTO));
                    com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> cifResp = customersInfoService.openAccount(customersDTO, "app");
                    logger.info("cif openAccount resp:{}", ToStringBuilder.reflectionToString(cifResp));
                    isNewUser = true;
                    if (cifResp.isSuccess()) {
                        Long partyId = cifResp.getResult().getPartyId();
                        user = new User();
                        user.setPartyId(partyId);
                        user.setLoginName(loginDTO.getMobilePhone());
                        //保留推荐记录
                        if (StringUtils.isNotEmpty(loginDTO.getRecCode())) {
                            cifResp = customersInfoService.selectByInvitationCode(loginDTO.getRecCode());
                            logger.info("cif openAccount req:{}, resp:{}", loginDTO.getRecCode(), ToStringBuilder.reflectionToString(cifResp));
                            if (cifResp.getResult() != null) {
                                activityInviteService.invite(ActivityInviteDetailVo.builder().loginName(loginDTO.getMobilePhone()).partyId(user.getPartyId()).recPartyId(cifResp.getResult().getPartyId()).activityCode(GoldService.ACTIVITY_REWARD).deviceId(loginDTO.getDeviceId()).source("邀请码注册").build());
                            }

                        }
                    }
                }
                saveSession(user.getPartyId(),user.getLoginName(),loginDTO.getClientVersion(),deviceInfo.getSystemType());
                userVo = synUserInfo(loginDTO.getMobilePhone(), loginDTO.getDeviceId(), session.getId(), isNewUser);
                if (StringUtils.isNotEmpty(deviceInfo.getChannel())) {
                    propExtendManager.queryAndInit(AppPropExtend.builder().relationId(userVo.getPartyId()).type(User.class.getSimpleName()).deviceId(loginDTO.getDeviceId()).ekey(MapVo.USER_REG_CHANNEL).value(deviceInfo.getChannel()).build());
                }
                //发放注册奖励
                if(sendNewUserReward){
                    activityManager.rewardV321(userVo.getPartyId(),Constant.ActivityTaskType.REGISTER,Constant.ActivityTaskType.INVITE);
                }
                resp.setResult(userVo);
            } else {
                resp.setResonpse(checkLogin.getCode(), checkLogin.getMemo(), checkLogin.getTips());
            }
            //login record
            saveLoginRecord("loginV4",deviceInfo);
        } catch (Exception e) {
            logger.warn("loginV4 ", e);
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_USER_INVALID);
        }
        return resp;
    }

    /**
     * 登录校验
     *
     * @param mobile   手机号
     * @param smsCode  短信验证码
     * @param password 密码
     * @param type     图案密码
     * @return
     */
    private Response<Boolean> checkLogin(String mobile, String smsCode, String password, String type) {
        Response<Boolean> resp = new Response(false);
        if (StringUtils.equals(type, Constant.LoginType.SMS.name())) {
            String adminAccount = SysConfigUtil.getStr(Constant.BusiVisitKey.app_ios_xl_admin_account.code);
            String adminVertifyCode = SysConfigUtil.getStr(Constant.BusiVisitKey.app_ios_xl_admin_vertifyCode.code);
            if (StringUtils.isNotEmpty(smsCode)) {//测试用户
                if (StringUtils.equals(mobile, adminAccount) && StringUtils.equals(smsCode, adminVertifyCode)) {
                    logger.info("test account");
                    resp.setResult(true);
                } else {
                    com.xianglin.cif.common.service.facade.model.Response<Boolean> smsResp = appLoginServiceClient.validateSms(mobile, smsCode);//短信验证码校验
                    if (!FacadeEnums.OK.code.equals(String.valueOf(smsResp.getCode()))) {
                        logger.debug("smsResp:{}", smsResp.getCode());
                        resp.setResonpse(smsResp.getCode(), smsResp.getMemo(), smsResp.getTips());
                    }
                }
            }
        } else if (StringUtils.equals(type, Constant.LoginType.PWD.name())) {
            User user = userManager.getUserByLoginAccount(mobile);
            if (user ==null || !StringUtils.equals(password, user.getPassword())) {//密码错误
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_VALDATE_POS_LOGIN_PWDERROR);
            }
        } else if (StringUtils.equals(type, Constant.LoginType.PATT_PWD.name())) {
            User user = userManager.getUserByLoginAccount(mobile);
            if (user ==null || !StringUtils.equals(user.getPasswordPatternStatus(), YesNo.Y.name()) || !StringUtils.equals(password, user.getPasswordPattern())) {//密码错误
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.ERROR_CHAT_400056);
            }
        } else {
            logger.info("unknow login type {}", type);
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_VALDATE_POS_LOGIN_PWDERROR);
        }
        return resp;
    }

    /**
     * 同步用户状态信息
     *
     * @param mobilePhone
     * @param deviceId
     * @param sessionId
     * @param isNewUser   是否新用户
     * @return
     */
    private LoginVo synUserInfo(String mobilePhone, String deviceId, String sessionId, boolean isNewUser) {
        LoginVo loginVo = null;
        com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> cifResp = customersInfoService.selectByMobilePhone(mobilePhone);
        logger.info("cif selectByMobilePhone resp:{}", ToStringBuilder.reflectionToString(cifResp));
        if (cifResp.isSuccess()) {
            String userType = UserType.user.name();
            boolean isManager = cifResp.getResult().getRoleDTOs().stream().anyMatch(roleDTO -> {
                return StringUtils.equals(roleDTO.getRoleCode(), "NODE_MANAGER");
            });
            if (isManager) {
                userType = UserType.nodeManager.name();
            }
            User su = saveLoginUser(mobilePhone, cifResp.getResult().getPartyId(), deviceId, sessionId, userType, "");
            //判断是否需要弹出图形密码框
            boolean alertPasswordPattern = true;
            AppPropExtend prop = propExtendManager.queryAndInit(AppPropExtend.builder().relationId(su.getPartyId()).type(User.class.getSimpleName())
                    .deviceId(su.getDeviceId()).ekey(MapVo.USER_PASSWORD_PATTERN).value(LocalDateTime.now().toString()).build());
            if (StringUtils.isNotEmpty(prop.getIsDeleted()) || isNewUser) {
                alertPasswordPattern = false;
            }
            //=======================================================================
            //家谱分享外链--处理添加
            UserRegistrationEvent registrationEvent = new UserRegistrationEvent(this, userManager.queryUser(cifResp.getResult().getPartyId()));
            applicationContext.publishEvent(registrationEvent);

            synchroRealName(su.getPartyId());//同步实名信息
            if(StringUtils.isEmpty(su.getShowName())){
                su.setShowName(su.showName());
            }
            loginVo = LoginVo.builder().loginName(su.getLoginName()).partyId(su.getPartyId()).userType(userType)
                    .ryToken(su.getRyToken())
                    .isNewUser(isNewUser).alertPasswordPattern(alertPasswordPattern).showName(su.getShowName()).headImg(StringUtils.isNotEmpty(su.getHeadImg()) ? su.getHeadImg() : SysConfigUtil.getStr("default_user_headimg")).build();
            
        }
        return loginVo;
    }


    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.autoLoginV4", description = "自动登录V4")
    public Response<LoginVo> autoLoginV4(LoginDTO loginDTO, DeviceInfo deviceInfo) {
        Response<LoginVo> resp = ResponseUtils.successResponse(null);
        try {
            Long partyId = sessionHelper.getSessionProp(AppSessionConstants.PARTY_ID, Long.class);
            User user = userManager.queryUser(partyId);
            if (user != null && StringUtils.equals(loginDTO.getDeviceId(), user.getDeviceId())) {
                LoginVo userVo = synUserInfo(user.getLoginName(), user.getDeviceId(), sessionHelper.getSession().getId(), false);
                saveSession(userVo.getPartyId(),userVo.getLoginName(),loginDTO.getClientVersion(),deviceInfo.getSystemType());
                resp.setResult(userVo);
            } else {
                sessionHelper.getSession().removeAttribute(AppSessionConstants.PARTY_ID);//自动登录失败
            }
            saveLoginRecord("autoLoginV4",deviceInfo);
        } catch (Exception e) {
            logger.warn("loginV4 ", e);
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_USER_INVALID);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.setPatternPassword", description = "图形密码")
    public Response<Boolean> setPatternPassword(String patternPasswordStatus, String patternPassword) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_USER_INVALID);
                return resp;
            }
            if (StringUtils.isEmpty(patternPassword) && StringUtils.equals(patternPasswordStatus, Constant.YESNO.YES.name())) {
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_OPERATOR_INVALID);
                return resp;
            }
            User user = userManager.queryUser(partyId);
            if (StringUtils.isNotEmpty(patternPassword)) {
                user.setPasswordPattern(patternPassword);
                user.setPasswordPatternStatus("Y");
            } else {
                user.setPasswordPatternStatus(patternPasswordStatus);
            }
            boolean update = userManager.updateUser(user) == 1;
            resp.setResult(update);
        } catch (Exception e) {
            logger.error("setPatternPassword ", e);
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.hasPasswordV2", description = "用户是否设置密码V2")
    public Response<Boolean> hasPasswordV2(String mobilePhone, String passwordType) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            //判断是否是新用户
            String loginAccount = mobilePhone;
            User u = userManager.getUserByLoginAccount(loginAccount);
            if (u != null) {
                if (StringUtils.equals(passwordType, Constant.LoginType.PWD.name())) {
                    if (StringUtils.isNotEmpty(u.getPassword())) {
                        resp.setResult(true);
                    }
                } else if (StringUtils.equals(passwordType, Constant.LoginType.PATT_PWD.name())) {
                    if (StringUtils.isNotEmpty(u.getPasswordPattern())) {
                        resp.setResult(true);
                    }
                } else if (StringUtils.equals(passwordType, Constant.LoginType.PATT_PWD_STATUS.name())) {
                    if (StringUtils.isNotEmpty(u.getPasswordPattern()) && StringUtils.equals(u.getPasswordPatternStatus(), Constant.YESNO.YES.code)) {
                        resp.setResult(true);
                    }
                }
            }
        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            logger.info("查询密码异常" + e.getMessage());
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.channelRegister", description = "渠道用户注册(同步)")
    public Response<LoginVo> channelRegister(LoginVo userInfo) {
        return responseCacheUtils.execute(() -> {
            User user  = User.builder().loginName(userInfo.getLoginName())
                    .partyId(userInfo.getPartyId()).headImg(userInfo.getHeadImg()).userType(UserType.user.name()).status("NORMAL")
                    .nikerName(userInfo.getShowName()).build();
            user = userManager.channelRegister(user);
            return LoginVo.builder().partyId(user.getPartyId()).loginName(user.getLoginName()).headImg(user.getHeadImg()).showName(user.getShowName()).build();
        });
    }

    @Override
    public Response<Boolean> forceOffline(Long partyId, String message) {
        return responseCacheUtils.execute(() -> {
            User user = userManager.queryUser(partyId);
            Optional.ofNullable(user).ifPresent(v -> {
                String sessionId = v.getSessionId();
                if(StringUtils.isNotEmpty(sessionId)){
                    MsgVo msg = MsgVo.builder().build();
                    msg.setPartyId(user.getPartyId());
                    msg.setMsgType(Constant.MsgType.OFFLINE.name());
                    msg.setMsgTitle(Constant.MsgType.OFFLINE.desc);
                    msg.setMessage(message);
                    msg.setIsDeleted(Constant.YESNO.YES.code);
                    messageManager.sendMsg(msg);
//                    sessionHelper.removeSession(sessionId);
                    Session session = sessionHelper.getSessionFromRedis(sessionId);
                    if(session != null){
                        session.removeAttribute(AppSessionConstants.PARTY_ID);
                        sessionHelper.saveSession(session);
                    }
                }
            });
            return true;
        });
    }

    /**
     * 查询当前用户登陆状态
     *
     * @return
     */
    private String queryLoginStatus() {

        String resp = Constant.LoginResult.NN.name();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            synchroRealName(partyId);
            User user = null;
            if (partyId != null) {
                user = userManager.queryUser(partyId);
            }
            if (user != null) {
                if (StringUtils.isEmpty(user.getPassword())) {
                    resp = Constant.LoginResult.NP.name();
                } else if (StringUtils.isEmpty(user.getTrueName()) || StringUtils.isEmpty(user.getDistrict())) {
                    resp = Constant.LoginResult.NA.name();
                } else if (StringUtils.isEmpty(user.getGender())) {
                    resp = Constant.LoginResult.ND.name();
                } else {
                    resp = Constant.LoginResult.SS.name();
                }
            }
        } catch (Exception e) {
            logger.warn("queryLoginStatus", e);
        }
        return resp;
    }

    /**保存登陆日志
     * @param type
     * @param deviceInfo
     */
    private void saveLoginRecord(String type,DeviceInfo deviceInfo){
        try {
            DeviceInfo nullDevice = new DeviceInfo();
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String clentVersion = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
            String systemType = sessionHelper.getSessionProp(AppSessionConstants.SYSTEM_TYPE,String.class);
            String remoteIp = sessionHelper.getGatewayProp("remoteAddr");
            AppClientLogLogin login = AppClientLogLogin.builder().partyId(partyId).deviceId(deviceId)
                    .type(type).ip(remoteIp).systemType(systemType).systemVersion(Optional.ofNullable(deviceInfo).orElse(nullDevice).getSystemVersion())
                    .version(clentVersion).deviceName(Optional.ofNullable(deviceInfo).orElse(nullDevice).getDeviceName())
                    .apkSource(Optional.ofNullable(deviceInfo).orElse(nullDevice).getChannel()).build();
            logManager.saveLoginRecord(login);
        } catch (Exception e) {
            logger.warn("saveLoginRecord",e);
        }
    }

    /**
     * 查询当前用户登陆状态
     *
     * @return
     * @since V3.5
     */
    private String queryLoginStatusV3() {

        String resp = Constant.LoginResult.NN.name();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            synchroRealName(partyId);
            User user = null;
            if (partyId != null) {
                user = userManager.queryUser(partyId);
            }
            if (user != null) {
                if (StringUtils.isEmpty(user.getTrueName()) || StringUtils.isEmpty(user.getDistrict())) {
                    resp = Constant.LoginResult.NA.name();
                } else {
                    resp = Constant.LoginResult.SS.name();
                }
            }
        } catch (Exception e) {
            logger.warn("queryLoginStatus", e);
        }
        return resp;
    }

    private NodeVo loginV3(LoginDTO loginDTO) throws Exception {

        NodeVo vo = new NodeVo();
        vo.setNodePartyId(-1L);

        if (StringUtils.isEmpty(loginDTO.getMobilePhone())) {
            vo.setUserType(UserType.visitor.name());
            vo = null;
            sessionHelper.getSession().setAttribute(AppSessionConstants.DEVICE_ID, loginDTO.getDeviceId());
        } else {
            User user = userManager.getUserByLoginAccount(loginDTO.getMobilePhone());
            String userType = UserType.user.name();
            //判断是否是站长
//            com.xianglin.cif.common.service.facade.model.Response<Long> partyIdResp = appLoginServiceClient.getNodeManagerPartyId(loginDTO.getMobilePhone());
//            logger.info("loginV3 partyIdResp:{}", org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(partyIdResp));
//            if (partyIdResp.getResult() != null && user.getPartyId().equals(partyIdResp.getResult())) {//站长
            com.xianglin.xlnodecore.common.service.facade.vo.NodeVo managerVoResp = appLoginServiceClient.queryNodeInfoByNodeManagerPartyId(user.getPartyId());
            logger.info("loginV3 partyIdResp:{}", org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(managerVoResp));
            if (managerVoResp != null) {
                userType = UserType.nodeManager.name();
                vo = DTOUtils.map(managerVoResp, NodeVo.class);
            }
//            }
            AccountNodeManagerVo managerVo = new AccountNodeManagerVo();
            managerVo.setUserRole(userType);
            loginDTO.setPartyId(user.getPartyId());
            getUserInfo(DTOUtils.map(loginDTO, com.xianglin.cif.common.service.facade.model.LoginDTO.class), managerVo);
//            PersonVo personVo = getPersonInfo(user.getPartyId());
            user.setUserType(userType);

            managerVo.setPartyId(user.getPartyId());
//            saveSession(loginDTO, null, loginDTO.getPassword(), vo, managerVo, personVo);
//            appLoginServiceClient.saveLog(user.getPartyId(), user.getNikerName(), user.getNikerName(), OperateTypeEnum.APP_LOGIN, "登陆app");
            user = saveLoginUser(loginDTO.getMobilePhone(), user.getPartyId(), loginDTO.getDeviceId(), sessionHelper.getSession().getId(), userType, loginDTO.getPassword());
            saveSession(user);
            vo.setRyToken(user.getRyToken());
            vo.setNodeManagerPartyId(user.getPartyId());
            vo.setUserType(userType);
            vo.setMobilePhone(user.getLoginName());
            vo.setAvatar(user.getHeadImg());
        }
        return vo;
    }

    /**
     * 首次登录
     *
     * @see com.xianglin.appserv.common.service.facade.AppLoginService#userFirstLogin(com.xianglin.appserv.common.service.facade.model.LoginDTO)
     */
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.userFirstLogin", description = "移动app首次登录")
    @Override
    public Response<Long> userFirstLogin(LoginDTO loginDTO) {

        Response<Long> resp = ResponseUtils.successResponse();
        try {
            com.xianglin.xlnodecore.common.service.facade.resp.PosLoginResp response = appLoginServiceClient.userFirstLogin(loginDTO.getNodeCode(), loginDTO.getSmsCode(), loginDTO.getPassword(), loginDTO.getMobilePhone());
            if (null != response) {
                resp.setCode(FacadeEnums.OK.getCode().equals(response.getCode() + "") ? ResultEnum.ResultSuccess.getCode() : response.getCode());
                resp.setMemo(response.getMemo());
                resp.setTips(response.getTips());
                resp.setResult(response.getNodeManagerPartyId());
            }
            resp.setResult(1l);
            return resp;
        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            logger.info("首次登录异常" + e.getMessage());
        }
        return resp;
    }

    /**
     * 忘记密码
     *
     * @see com.xianglin.appserv.common.service.facade.AppLoginService#resetUserLogin(com.xianglin.appserv.common.service.facade.model.LoginDTO)
     */
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.resetUserLogin", description = "移动app忘记密码")
    @Override
    public Response<Long> resetUserLogin(LoginDTO loginDTO) {

        Response<Long> resp = ResponseUtils.successResponse();
        try {
//            com.xianglin.xlnodecore.common.service.facade.resp.PosLoginResp response = appLoginServiceClient.resetUserLogin(loginDTO.getNodeCode(), loginDTO.getSmsCode(), loginDTO.getPassword(), loginDTO.getMobilePhone());
//            if (response.getCode() == 1000) {
//                resp.setCode(ResultEnum.ResultSuccess.getCode());
//                resp.setMemo(response.getMemo());
//                resp.setTips(response.getTips());
//                resp.setResult(response.getNodeManagerPartyId());
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);

            User user = User.builder().build();
            user.setLoginName(loginDTO.getNodeCode());
            user.setPartyId(partyId);
            user.setDeviceId("");
            userManager.addUpdateUser(user);
//            } else {
//                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.UPDATE_FAIL);
//            }
            resp.setResult(1l);
            return resp;
        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            logger.info("忘记密码异常" + e.getMessage());
        }
        return resp;
    }

    /**
     * 通过nodeCode获取预留手机号码
     * chengfanfan
     *
     * @param loginDTO
     * @return 2016年8月23日
     */
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.getMobileByNodeCode", description = "移动app获取手机号")
    @Override
    public Response<String> getMobileByNodeCode(LoginDTO loginDTO) {
        /** 定义返回对象 */
        Response<String> resp = ResponseUtils.successResponse();
        try {
            CommonResp<String> mobileResp = null;
//                    appLoginServiceClient.queryMobileByNodeCode(loginDTO.getNodeCode());
            if (null != mobileResp) {
                resp.setResult(null == mobileResp.getRespVo() ? "" : mobileResp.getRespVo());
                resp.setCode(FacadeEnums.OK.getCode().equals(mobileResp.getCode() + "") ? ResultEnum.ResultSuccess.getCode() : mobileResp.getCode());
                resp.setMemo(mobileResp.getMemo());
                resp.setTips(mobileResp.getTips());
            }

        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            logger.info("忘记密码异常" + e.getMessage());
        }
        return resp;
    }


    /**
     * @param loginDTO   登录信息
     * @param deviceInfo 设备信息
     * @param partyId    partyId
     * @param password
     * @param nodeCode
     * @param node
     * @param managerVo
     */
    @Deprecated
    private void saveSession(LoginDTO loginDTO, DeviceInfo deviceInfo, Long partyId, String password, String nodeCode, com.xianglin.xlnodecore.common.service.facade.vo.NodeVo node, AccountNodeManagerVo managerVo) {
        logger.debug("保存会话信息。SESSIONID:{},loginInfo:{},deviceInfo：{}，figure:{}", loginDTO, deviceInfo);

        Session session = sessionHelper.getSession();
        session.setAttribute(SessionConstants.PARTY_ID, partyId);
        session.setAttribute(SessionConstants.DEVICE_ID, deviceInfo.getDeviceId());
        session.setAttribute(SessionConstants.DEVICE_INFO, deviceInfo);
        session.setAttribute(SessionConstants.CLIENT_ID, loginDTO.getClientId());
        session.setAttribute(SessionConstants.CLIENT_VERSION, loginDTO.getClientVersion());
        session.setAttribute(SessionConstants.PASSWORD, password);
        session.setAttribute(SessionConstants.NODE_CODE, nodeCode);
        session.setAttribute(SessionConstants.NODE_MANAGER_INFO, node);
        session.setAttribute(SessionConstants.NODE_INFO, node);
        session.setAttribute(SessionConstants.XL_QY_USER, managerVo);
        session.setAttribute(SessionConstants.DISTRICT_CODE, node.getDistrictCode());
        session.setAttribute(SessionConstants.node_party_id, node.getNodePartyId());
        sessionHelper.saveSession(session);
        logger.debug("登录成功后，SESSION中获取的partyId:{}", session.getAttribute(SessionConstants.PARTY_ID));
    }

    public SessionHelper getSessionHelper() {

        return sessionHelper;
    }

    public void setSessionHelper(SessionHelper sessionHelper) {

        this.sessionHelper = sessionHelper;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.AppLoginService#loginByMobile(com.xianglin.appserv.common.service.facade.model.LoginDTO, com.xianglin.appserv.common.service.facade.model.DeviceInfo)
     */
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.loginByMobile", description = "移动app手机号登录")
    @Override
    public Response<NodeVo> loginByMobile(LoginDTO loginDTO, DeviceInfo deviceInfo) {

        Long startTime = System.currentTimeMillis();
        logger.info("loginByMobile loginDTO:{},DeviceInfo:{}, timeStamp:{}", loginDTO, deviceInfo, startTime);
        Response<NodeVo> resp = ResponseUtils.successResponse();
        com.xianglin.cif.common.service.facade.model.Response<com.xianglin.xlnodecore.common.service.facade.vo.NodeVo> response = new com.xianglin.cif.common.service.facade.model.Response<com.xianglin.xlnodecore.common.service.facade.vo.NodeVo>();
        com.xianglin.cif.common.service.facade.model.LoginDTO ld = new com.xianglin.cif.common.service.facade.model.LoginDTO();
        com.xianglin.cif.common.service.facade.model.DeviceInfo di = new com.xianglin.cif.common.service.facade.model.DeviceInfo();
        NodeVo resultVo = new NodeVo();
        /** 获取session */
        Session session = sessionHelper.getSession();
        try {
            if (null != loginDTO) {
                BeanUtils.copyProperties(ld, loginDTO);
            }
            if (null != deviceInfo) {
                BeanUtils.copyProperties(di, deviceInfo);
            }
            String deviceId = session.getAttribute(SessionConstants.DEVICE_ID);
            //校验设备绑定基本信息
            di.setDeviceId(deviceId);
            //是否是乡邻测试账号，验证码必须是正确的
            String adminAccount = SysConfigUtil.getStr(Constant.BusiVisitKey.app_ios_xl_admin_account.code);
            String adminVertifyCode = SysConfigUtil.getStr(Constant.BusiVisitKey.app_ios_xl_admin_vertifyCode.code);
            boolean isAdminLogin = false;
            if (StringUtils.isNotEmpty(adminAccount) && StringUtils.isNotEmpty(adminVertifyCode)) {
                if (loginDTO.getMobilePhone().equals(adminAccount) && loginDTO.getSmsCode().equals(adminVertifyCode)) {
                    isAdminLogin = true;
                }
            }
            if (!isAdminLogin) {
                com.xianglin.cif.common.service.facade.model.Response<Boolean> smsResp = appLoginServiceClient.validateSms(loginDTO.getMobilePhone(), loginDTO.getSmsCode());
                if (!FacadeEnums.OK.code.equals(String.valueOf(smsResp.getCode()))) {
                    logger.debug("smsResp:{}", smsResp.getCode());
                    resp.setCode(AppservConstants.LoginError.INVALID_SMS_CODE.code);
                    resp.setTips(AppservConstants.LoginError.INVALID_SMS_CODE.desc);
                    resp.setMemo(AppservConstants.LoginError.INVALID_SMS_CODE.desc);
                    return resp;
                }
            }
            response = appLoginServiceClient.login(ld, di, deviceId);
            //保存站点的详细信息
            if (FacadeEnums.OK.getCode().equals(response.getCode() + "")) {
                com.xianglin.xlnodecore.common.service.facade.vo.NodeVo result = response.getResult();
                //站点经理保存
                AccountNodeManagerVo managerVo = new AccountNodeManagerVo();
                //此接口需要返回当前用户的角色
                AccountNodeManagerResp managerVoResp = appLoginServiceClient.queryNodeManagerByPartyId(result.getNodeManagerPartyId());
                if (null != managerVoResp && null != managerVoResp.getVo()) {
                    BeanUtils.copyProperties(managerVo, managerVoResp.getVo());
                }
                managerVo.setNodePartyId(result.getNodePartyId());
                saveLoginUser(loginDTO.getMobilePhone(), result.getNodeManagerPartyId(), deviceId, session.getId(), managerVo.getUserRole(), loginDTO.getPassword());
                //查询app_user表中用户信息
                getUserInfo(ld, managerVo);
                //保存session
                saveSession(loginDTO, deviceInfo, result.getNodeManagerPartyId(), loginDTO.getPassword(), result.getNodeCode(), result, managerVo);
//					updateAppVersion(deviceId, result.getNodeManagerPartyId(), deviceInfo.getSystemVersion());
                resultVo = (null == response.getResult() ? null : DTOUtils.map(response.getResult(), NodeVo.class));
                if (null != resultVo) {
                    resultVo.setMobilePhone(managerVo.getMobilePhone());
                }
                resp.setResult(resultVo);
                resp.setCode(ResultEnum.ResultSuccess.getCode());
                resp.setMemo(response.getTips());
                resp.setTips(response.getMemo());
            } else {
                logger.warn("主动登陆失败，loginDTO：{}：{}", loginDTO, response);
                resp.setCode(response.getCode());
                resp.setMemo("登陆失败,请重新登陆");
                resp.setTips("登陆失败,请重新登陆");
            }
            saveLoginRecord("loginByMobile",deviceInfo);
        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            logger.info("移动app主动登录异常", e);
        }
        logger.info("loginByMobile start time{}, spend {}", startTime, System.currentTimeMillis() - startTime);
        return resp;
    }

    private User saveLoginUser(String accountName, long partyId, String
            deviceId, String sessionId, String userType, String password) {

        long startTime = System.currentTimeMillis();
        logger.info("login saveLoginUser time{}, spend {} ", startTime);
        //保存用户信息
        User user = User.builder().build();
        user.setLoginName(accountName);
        user.setPartyId(partyId);
        user.setDeviceId(deviceId);
        user.setUserType(StringUtils.isEmpty(userType) ? UserType.user.name() : userType);
        user.setStatus(UserStatus.NORMAL.name());
        user.setSessionId(sessionId);
        if (StringUtils.isNotEmpty(password)) {
            user.setPassword(password);
        }
        User u = userManager.addUpdateUser(user);
        logger.info("login saveLoginUser start11 time{}, spend {} ", startTime, System.currentTimeMillis() - startTime);
        applicationContext.publishEvent(new UserMonthAchieveEvent(user));
        logger.info("login saveLoginUser start22 time{}, spend {} ", startTime, System.currentTimeMillis() - startTime);
        return u;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.sendSmsAndValid", description = "发送短信验证码并返回是否新用户")
    public Response<Map<String, Boolean>> sendSmsAndValid(NodeVo nodeVo) {
        /** 定义返回对象 */
        Response<Map<String, Boolean>> resp = ResponseUtils.successResponse();
        Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
        boolean bsend = false, bnew = false;
        try {
            //查询是否为新注册用户
            User user = userDAO.selectByMobilePhone(nodeVo.getMobilePhone());
            if (user == null) {
                bnew = true;
            }
            //发短信
            com.xianglin.cif.common.service.facade.model.Response<Boolean> response = appLoginServiceClient.sendSmsOnly(nodeVo.getMobilePhone());
            if (null != response) {
                resp.setCode(FacadeEnums.OK.getCode().equals(response.getCode() + "") ? ResultEnum.ResultSuccess.getCode() : response.getCode());
                resp.setMemo(response.getTips());
                resp.setTips(response.getMemo());
                bsend = response.getResult();
            }
            resultMap.put("bsend", bsend);
            resultMap.put("bnew", bnew);
            resp.setResult(resultMap);
        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            resp.setMemo("访问人太多啦，请再试一次");
            resp.setTips("访问人太多啦，请再试一次");
            logger.info(e.getMessage());
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.loginByMobileV1_3", description = "移动app手机号登录v1.3+")
    public Response<NodeVo> loginByMobileV1_3(LoginDTO
                                                      loginDTO, DeviceInfo deviceInfo) {
        Long startTime = System.currentTimeMillis();
        logger.info("loginByMobileV1_3 loginDTO:{},DeviceInfo:{}, timeStamp:{}", loginDTO, deviceInfo, startTime);
        Response<NodeVo> resp = ResponseUtils.successResponse();
        com.xianglin.cif.common.service.facade.model.Response<com.xianglin.xlnodecore.common.service.facade.vo.NodeVo> response = new com.xianglin.cif.common.service.facade.model.Response<com.xianglin.xlnodecore.common.service.facade.vo.NodeVo>();
        //默认失败
        response.setFacade(FacadeEnums.FAIL);

        com.xianglin.cif.common.service.facade.model.LoginDTO ld = new com.xianglin.cif.common.service.facade.model.LoginDTO();
        com.xianglin.cif.common.service.facade.model.DeviceInfo di = new com.xianglin.cif.common.service.facade.model.DeviceInfo();
        NodeVo resultVo = new NodeVo();
        com.xianglin.cif.common.service.facade.model.Response<String> cifLoginResp = null;
        /** 获取session */
        Session session = sessionHelper.getSession();

        try {
            if(1 == 1){
                throw new BusiException("请升级app");
            }
            String deviceId = deviceInfo.getDeviceId();
            session.setAttribute(AppSessionConstants.DEVICE_ID, deviceId);
            sessionHelper.saveSession(session);
            if (null != loginDTO) {
                BeanUtils.copyProperties(ld, loginDTO);
            } else {
                if (loginDTO.getMobilePhone() == null) {
                    resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_INPUT_INVALID);
                    resp.setTips("手机号不能为空");
                    resp.setMemo("手机号不能为空");
                    return resp;
                }
            }
            if (null != deviceInfo) {
                BeanUtils.copyProperties(di, deviceInfo);
            }
            //校验设备绑定基本信息
            di.setDeviceId(deviceId);
            //是否是乡邻测试账号，验证码必须是正确的
            String adminAccount = SysConfigUtil.getStr(Constant.BusiVisitKey.app_ios_xl_admin_account.code);
            String adminVertifyCode = SysConfigUtil.getStr(Constant.BusiVisitKey.app_ios_xl_admin_vertifyCode.code);
            boolean isAdminLogin = false;
            String userType = UserType.visitor.name();
            if (StringUtils.isNotEmpty(adminAccount) && StringUtils.isNotEmpty(adminVertifyCode)) {//测试用户
                if (loginDTO.getMobilePhone().equals(adminAccount) && loginDTO.getSmsCode().equals(adminVertifyCode)) {
                    isAdminLogin = true;
                }
            }
            if (!isAdminLogin) {//非测试账号，设置手机号
                com.xianglin.cif.common.service.facade.model.Response<Boolean> smsResp = appLoginServiceClient.validateSms(loginDTO.getMobilePhone(), loginDTO.getSmsCode());
                if (!FacadeEnums.OK.code.equals(String.valueOf(smsResp.getCode()))) {
                    logger.debug("smsResp:{}", smsResp.getCode());
                    resp.setCode(smsResp.getCode());
                    resp.setTips(smsResp.getTips());
                    resp.setMemo(smsResp.getMemo());
                    return resp;
                }
            }
            User u = userManager.getUserByLoginAccount(loginDTO.getMobilePhone());
            if (u == null) {//用户不存在需要创建
                CustomersDTO customersDTO = new CustomersDTO();
                customersDTO.setMobilePhone(loginDTO.getMobilePhone());
                customersDTO.setCreator(loginDTO.getMobilePhone());
//                customersDTO.setInvitationCode(loginDTO.getRecCode());//推荐关系建立延后
                List<RoleDTO> roleDTOs = new ArrayList<>();
                RoleDTO role = new RoleDTO();
                role.setRoleCode("APP_USER");
                role.setRoleName("APP用户");
                roleDTOs.add(role);
                customersDTO.setRoleDTOs(roleDTOs);
                logger.info("cif openAccount req:{}", ToStringBuilder.reflectionToString(customersDTO));
                com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> cifResp = customersInfoService.openAccount(customersDTO, "app");
                logger.info("cif openAccount resp:{}", ToStringBuilder.reflectionToString(cifResp));
                if (cifResp.isSuccess()) {
                    Long partyId = cifResp.getResult().getPartyId();
                    userType = UserType.user.name();
                    u = new User();
                    u.setPartyId(partyId);
                    u.setLoginName(loginDTO.getMobilePhone());
                    //保留推荐记录
                    if (StringUtils.isNotEmpty(loginDTO.getRecCode())) {
                        cifResp = customersInfoService.selectByInvitationCode(loginDTO.getRecCode());
                        logger.info("cif openAccount req:{}, resp:{}", loginDTO.getRecCode(), ToStringBuilder.reflectionToString(cifResp));
                        if (cifResp.getResult() != null) {
                            activityInviteService.invite(ActivityInviteDetailVo.builder().loginName(loginDTO.getMobilePhone()).recPartyId(cifResp.getResult().getPartyId()).activityCode(GoldService.ACTIVITY_REWARD).deviceId(loginDTO.getDeviceId()).source("邀请码注册").build());
                        }
                    }
                } else {
                    resp.setCode(cifResp.getCode());
                    resp.setTips(cifResp.getTips());
                    resp.setMemo(cifResp.getMemo());
                    return resp;
                }
            } else {
                userType = UserType.user.name();
            }
            com.xianglin.xlnodecore.common.service.facade.vo.NodeVo nodeVo = appLoginServiceClient.queryNodeInfoByNodeManagerPartyId(u.getPartyId());
            if (nodeVo != null) {
                userType = UserType.nodeManager.name();
            }
            u.setUserType(userType);

            User su = saveLoginUser(loginDTO.getMobilePhone(), u.getPartyId(), deviceId, session.getId(), userType, loginDTO.getPassword());
            u.setDeviceId(deviceId);
            saveSession(u);
            if (StringUtils.isNotEmpty(deviceInfo.getChannel())) {
                propExtendManager.queryAndInit(AppPropExtend.builder().relationId(u.getPartyId()).type(User.class.getSimpleName()).deviceId(deviceId).ekey(MapVo.USER_REG_CHANNEL).value(deviceInfo.getChannel()).build());
            }
            resultVo.setRyToken(su.getRyToken());
            resultVo.setNodeManagerPartyId(ld.getPartyId());
            resultVo.setUserType(userType);
            resultVo.setNodePartyId(-1L);//村民没有网点设置为-1
            resultVo.setMobilePhone(ld.getMobilePhone());
            resultVo.setNodeManagerName(u.getTrueName());
            resultVo.setAvatar(su.getHeadImg());
            resp.setResult(resultVo);
            saveLoginRecord("loginByMobileV1_3",deviceInfo);
        } catch (BusiException e){
            resp.setFacade(e.getFacadeEnums());
        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            logger.info("移动app主动登录异常", e);
        }
        logger.info("loginByMobileV1_3 end time{}, spend {}", startTime, System.currentTimeMillis() - startTime);
        return resp;
    }

    /**缓存session中对象
     * @since 20180503
     * @param partyid
     * @param loginName
     * @param clientVersion
     * @param systemType
     */
    private void saveSession(Long partyid,String loginName,String clientVersion,String systemType){
        logger.info("save session partyid:{},loginName:{},clientVersion:{},systemType:{}",partyid,loginName,clientVersion,systemType);
        Session session = sessionHelper.getSession();
        session.setAttribute(AppSessionConstants.PARTY_ID, partyid);
        session.setAttribute(AppSessionConstants.LOGIN_NAME, loginName);
        session.setAttribute(AppSessionConstants.CLIENT_VERSION, clientVersion);
        session.setAttribute(AppSessionConstants.SYSTEM_TYPE, StringUtils.upperCase(systemType));
        sessionHelper.saveSession(session);
    }


    private void saveSession(User u) {

        Session session = sessionHelper.getSession();
        com.xianglin.xlnodecore.common.service.facade.vo.NodeVo node = appLoginServiceClient.queryNodeInfoByNodeManagerPartyId(u.getPartyId());
        //兼容
        if (node != null) {
            session.setAttribute(SessionConstants.NODE_CODE, node.getNodeCode());
            session.setAttribute(SessionConstants.DISTRICT_CODE, node.getDistrictCode());
            session.setAttribute(SessionConstants.node_party_id, node.getNodePartyId());
            session.setAttribute(SessionConstants.NODE_INFO, node);
            session.setAttribute(SessionConstants.NODE_MANAGER_INFO, node);
        }
        session.setAttribute(AppSessionConstants.PARTY_ID, u.getPartyId());
        session.setAttribute(AppSessionConstants.LOGIN_NAME, u.getLoginName());
        session.setAttribute(AppSessionConstants.DEVICE_ID, u.getDeviceId());

        session.setAttribute(SessionConstants.USER_TYPE, u.getUserType());//兼容

        //兼容
        AccountNodeManagerVo managerVo = new AccountNodeManagerVo();
        managerVo.setUserRole(u.getUserType());
        managerVo.setPartyId(u.getPartyId());
        managerVo.setTrueName(u.getTrueName());
        managerVo.setMobilePhone(u.getLoginName());
        session.setAttribute(SessionConstants.XL_QY_USER, managerVo);

        sessionHelper.saveSession(session);
        logger.debug("登录成功后，SESSION中获取的partyId:{}", session.getAttribute(SessionConstants.PARTY_ID));
    }

    private void getUserInfo
            (com.xianglin.cif.common.service.facade.model.LoginDTO
                     loginDTO, AccountNodeManagerVo managerVo) throws
            BusiException, IllegalAccessException, InvocationTargetException {
        //查询app_user表中用户信息
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        User user = User.builder().build();
        user = userDAO.selectByPartyId(loginDTO.getPartyId());
        String idNumber = customersInfoService.selectByPartyId(user.getPartyId()).getResult().getCredentialsNumber();
        userInfoDTO.setIdNumber(idNumber);
        BeanUtils.copyProperties(userInfoDTO, user);//赋值
        managerVo.setUserInfoDTO(userInfoDTO);
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppLoginService.validateSms", description = "校验手机验证码")
    public Response<Boolean> validateSms(String mobilePhone, String
            smsCode) {
        /** 定义返回对象 */
        Response<Boolean> resp = ResponseUtils.successResponse(true);
        try {
            com.xianglin.cif.common.service.facade.model.Response<Boolean> smsResp = appLoginServiceClient.validateSms(mobilePhone, smsCode);
            if (!FacadeEnums.OK.code.equals(String.valueOf(smsResp.getCode()))) {
                logger.debug("validateSms:{}", smsResp.getCode());
                resp.setCode(smsResp.getCode());
                resp.setTips(smsResp.getTips());
                resp.setMemo(smsResp.getMemo());
                resp.setResult(false);
                return resp;
            }
        } catch (Exception e) {
            resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            resp.setMemo("访问人太多啦，请再试一次");
            resp.setTips("访问人太多啦，请再试一次");
            logger.info(e.getMessage());
        }
        return resp;
    }

}
