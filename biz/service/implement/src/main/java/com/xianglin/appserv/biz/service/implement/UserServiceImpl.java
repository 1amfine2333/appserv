package com.xianglin.appserv.biz.service.implement;

import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.UserRelationManager;
import com.xianglin.appserv.biz.shared.util.LoginAttrUtil;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.daointerface.AppUserRelationMapper;
import com.xianglin.appserv.common.dal.daointerface.UserDAO;
import com.xianglin.appserv.common.dal.dataobject.AppUserRelation;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.dal.dataobject.UserInfoWrap;
import com.xianglin.appserv.common.service.facade.UserService;
import com.xianglin.appserv.common.service.facade.model.BindUserDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.UserInfoDTO;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.AccountNodeManagerVo;
import com.xianglin.appserv.common.service.facade.model.vo.AppCommuseMenuDTO;
import com.xianglin.appserv.common.service.facade.model.vo.AppCommuseWordDTO;
import com.xianglin.appserv.common.service.integration.cif.AppLoginServiceClient;
import com.xianglin.appserv.common.util.RyUtil;
import com.xianglin.appserv.common.util.constant.AppservConstants;
import com.xianglin.appserv.core.service.UserCoreService;
import com.xianglin.appserv.core.service.UserRelationCoreService;
import com.xianglin.cif.common.service.facade.AuthService;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.constant.ResponseConstants.FacadeEnums;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.model.CustomersDTO;
import com.xianglin.cif.common.service.facade.req.PersonReq;
import com.xianglin.cif.common.service.facade.vo.PersonVo;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.xlnodecore.common.service.facade.resp.AccountNodeManagerResp;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @dateTime 2016年11月23日 下午4:10:42
 */
@Service
@ServiceInterface
public class UserServiceImpl implements UserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("#{config['APP_FILESERVER_URL']}")
    private String appFileURL;

    @Autowired
    AppLoginServiceClient appLoginServiceClient;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginAttrUtil loginAttrUtil;

    @Autowired
    private UserCoreService userCoreService;
    @Autowired
    private UserManager userManager;

    @Autowired
    private UserRelationManager userRelationCoreService;

    @Autowired
    private AppUserRelationMapper appUserRelationMapper;

    @Autowired
    private CustomersInfoService customersInfoService;

    @Autowired
    private AuthService authService;

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserService.uploadImg", description = "图片上传")
    public Response<String> uploadImg(UserInfoDTO userInfoDTO) {
        Response<String> response = ResponseUtils.successResponse();
        try {
            userInfoDTO.setPartyId(loginAttrUtil.getPartyId());
//            ImgBusiType imgBusiType = ImgBusiType.getEnumByCode(userInfoDTO.getImgBusiType());
//            if (imgBusiType == null) {
//                throw new BusiException(ResponseEnum.PARAM_INVALD, "图片业务类型非法");
//            }
//            ImgInfo imgInfo = ImgBase64Util.getImgName(userInfoDTO.getBase64Img());
//            CommonReq<FileReqVo> commonReq = new CommonReq<>();
//            FileReqVo vo = new FileReqVo();
//            vo.setData(imgInfo.getContent());
//            vo.setFileSize(Long.valueOf(imgInfo.getLength()).intValue());
//            vo.setFileName(imgInfo.getFileName());
//            vo.setFileType("1");
//            commonReq.setBody(vo);
//            CommonResp<FileRespVo> fileResp = appFileService.uploadImgFile(commonReq);
//            if (fileResp.getBody() != null) {
//                response.setResult(appFileURL + fileResp.getBody().getId());
//            }
//            User user = User.builder().build();
//            user.setHeadImg(response.getResult());
//            user.setPartyId(loginAttrUtil.getPartyId());
//            if(user != null && user.getPartyId() != null){
//                User u = userDAO.selectByPartyId(user.getPartyId());
//                if(u != null){
//                    user.setId(u.getId());
//                    userDAO.updateByPrimaryKeySelective(user);
//                }
//            }
//            user = userDAO.selectByPartyId(loginAttrUtil.getPartyId());
//            RyUtil.updateUserInfo(user.getPartyId(), user.getTrueName(), user.getNikerName(), user.getHeadImg());//同步融云头像信息
//            UserInfoDTO userInfoDTOSession = new UserInfoDTO();
//            BeanUtils.copyProperties(userInfoDTOSession, user);//赋值
//
//            AccountNodeManagerVo accountNodeManagerVo = loginAttrUtil.getAccountNodeManager();
//            accountNodeManagerVo.setUserInfoDTO(userInfoDTOSession);// 更新session
//            loginAttrUtil.setSessionVo(SessionConstants.XL_QY_USER, accountNodeManagerVo);
//        } catch (BusiException e) {
//            LOGGER.error("图片上传失败", e);
//            response = ResponseUtils.toResponse(e.getResponseEnum());
        } catch (Exception e) {
            LOGGER.warn("图片上传失败", e);
            response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
        }
        return response;
    }

    /* (non-Javadoc)
     * @see com.xianglin.appserv.common.service.facade.UserService#getUserInfo(com.xianglin.appserv.common.service.facade.model.UserInfoDTO)
     */
    /* (non-Javadoc)
	 * @see com.xianglin.appserv.common.service.facade.UserService#getUserInfo(com.xianglin.appserv.common.service.facade.model.UserInfoDTO)
	 */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserService.getUserInfo", description = "获取用户信息")
    public Response<UserInfoDTO> getUserInfo(UserInfoDTO userInfoDTO) {
        Response<UserInfoDTO> response = ResponseUtils.successResponse();
        try {
            userInfoDTO.setPartyId(loginAttrUtil.getPartyId());
            User user = userDAO.selectByPartyId(userInfoDTO.getPartyId());
            BeanUtils.copyProperties(userInfoDTO, user);//赋值

            //查询站长信息
            AccountNodeManagerResp accountNodeManagerResp = appLoginServiceClient.queryNodeManagerByPartyId(loginAttrUtil.getPartyId());
            if (com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.OK.code != accountNodeManagerResp.getCode()) {
                LOGGER.debug("基业响应码,code{}", accountNodeManagerResp.getCode());
//				throw new BusiException(ResponseEnum.BUSI_INVALD,"查询个人信息失败");
            }
            com.xianglin.xlnodecore.common.service.facade.vo.AccountNodeManagerVo accountNodeManagerVo = accountNodeManagerResp.getVo();
//            if (accountNodeManagerVo != null) {
//                userInfoDTO.setUserType(UserType.nodeManager.name());
//                userInfoDTO.setName(accountNodeManagerVo.getTrueName());
//                userInfoDTO.setSex(accountNodeManagerVo.getGender());
//            } else {
//                PersonReq personReq = new PersonReq();
//                PersonVo personVo = new PersonVo();
//                personVo.setPartyId(loginAttrUtil.getPartyId());
//                personReq.setVo(personVo);
//                PersonResp personResp = personServiceClient.getPersonByPartyId(personReq);
//                if (!ResponseConstants.FacadeEnums.OK.code.equals(personResp.getCode())) {
//                    LOGGER.error("CIF查询个人信息失败,partyId:{} code:{}", personVo.getPartyId(), personResp.getCode());
//                } else {
//                    userInfoDTO.setName(personResp.getVo() != null ? personResp.getVo().getTrueName() : null);
//                    userInfoDTO.setSex(personResp.getVo() != null ? personResp.getVo().getGender() : null);
//                }
//                userInfoDTO.setUserType(UserType.user.name());
//            }

//            //调用CIF获取身份证号
//            PartyAttrCredentialsVo partyAttrCredentialsVo = new PartyAttrCredentialsVo();
//            partyAttrCredentialsVo.setPartyId(loginAttrUtil.getPartyId());
//            partyAttrCredentialsVo.setCredentialsType(CredentialsTypeEnums.ID.msg);
//            PartyAttrCredentialsReq partyAttrCredentialsReq = new PartyAttrCredentialsReq(partyAttrCredentialsVo);
//            PartyAttrCredentialsResp partyAttrCredentialsResp = partyAttrCredentialsService.selectCredentials(partyAttrCredentialsReq);
//            if (!ResponseConstants.FacadeEnums.OK.code.equals(partyAttrCredentialsResp.getCode())) {
//                LOGGER.info("CIF查询联系信息失败,partyId:{},code{}", partyAttrCredentialsVo.getPartyId(), partyAttrCredentialsResp.getCode());
////				throw new BusiException(ResponseEnum.BUSI_INVALD,"CIF查询联系信息失败");
//            } else {
//                if (!CollectionUtils.isEmpty(partyAttrCredentialsResp.getVoList()) && partyAttrCredentialsResp.getVoList().get(0) != null) {
//                    userInfoDTO.setIdNumber(partyAttrCredentialsResp.getVoList().get(0).getCredentialsNumber());
//                    userInfoDTO.setName(partyAttrCredentialsResp.getVoList().get(0).getCredentialsName());
//                }
//            }
            /*PrincipalDTO dto = new PrincipalDTO();
            dto.setPartyId(loginAttrUtil.getPartyId());
            com.xianglin.cif.common.service.facade.model.Response<PartyAttrRealnameauthVo> resp = authenticationService.queryAuthLevelByPartyId(dto);*/
            com.xianglin.cif.common.service.facade.model.Response<CustomersDTO>  resp =customersInfoService.selectCustomsAlready2Auth(loginAttrUtil.getPartyId());
            if (resp.getResult() != null) {
                if (resp.getResult().getAuthLevel().contains("R")) {
                    userInfoDTO.setIsAuth(true);
                } else {
                    userInfoDTO.setIsAuth(false);
                }
            }
            //查询粉丝和关注列表数量
            UserInfoWrap wrap = userRelationCoreService.selectFansAndFollows(userInfoDTO.getPartyId());
            if (wrap != null) {
                userInfoDTO.setFansNumber(wrap.getFansNumbers());
                userInfoDTO.setFollowsNumber(wrap.getFollowNumbers());
            }
            response.setResult(userInfoDTO);
        } catch (Exception e) {
            LOGGER.warn("用户信息查询失败", e);
            response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserService.getUserByPartyId", description = "根据partyId 查询用户信息")
    public Response<UserInfoDTO> getUserByPartyId(UserInfoDTO userInfoDTO) {
        Response<UserInfoDTO> response = ResponseUtils.successResponse();
        if (null == userInfoDTO || userInfoDTO.getPartyId() == null) {
            response.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
        } else {
            User user = User.builder().build();
            try {
                BeanUtils.copyProperties(user, userInfoDTO);
                user = userDAO.selectByPartyId(userInfoDTO.getPartyId());

                if (StringUtils.isEmpty(user.getNikerName())) {
                    user.setNikerName("xl".concat(String.valueOf(user.getPartyId())));
                }
                BeanUtils.copyProperties(userInfoDTO, user);//赋值
                //查询person表中信息
                PersonReq personReq = new PersonReq();
                PersonVo personVo = new PersonVo();
                personVo.setPartyId(userInfoDTO.getPartyId());
                personReq.setVo(personVo);
//                PersonResp personResp = personServiceClient.getPersonByPartyId(personReq);
//                if (!ResponseConstants.FacadeEnums.OK.code.equals(personResp.getCode())) {
//                    LOGGER.error("CIF查询个人信息失败,partyId:{} code:{}", personVo.getPartyId(), personResp.getCode());
//                } else {
//                    userInfoDTO.setName(personResp.getVo() != null ? personResp.getVo().getTrueName() : null);
//                    userInfoDTO.setSex(personResp.getVo() != null ? personResp.getVo().getGender() : null);
//                }
                //查询粉丝和关注列表数量
                UserInfoWrap wrap = userRelationCoreService.selectFansAndFollows(userInfoDTO.getPartyId());
                if (wrap != null) {
                    userInfoDTO.setFansNumber(wrap.getFansNumbers());
                    userInfoDTO.setFollowsNumber(wrap.getFollowNumbers());
                }
                response.setResult(userInfoDTO);
            } catch (Exception e) {
                LOGGER.warn("查询用户信息失败，", e);
                response.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY);
            }

        }
        return response;
    }

    /* (non-Javadoc)
     * @see com.xianglin.appserv.common.service.facade.UserService#updateUserInfo(com.xianglin.appserv.common.service.facade.model.UserInfoDTO)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserService.updateUserInfo", description = "更新用户信息")
    public Response<UserInfoDTO> updateUserInfo(UserInfoDTO userInfoDTO) {
        Response<UserInfoDTO> response = ResponseUtils.successResponse(userInfoDTO);
        try {
            userInfoDTO.setPartyId(loginAttrUtil.getPartyId());
            userCoreService.updateUserInfo(userInfoDTO);

            User user = userDAO.selectByPartyId(userInfoDTO.getPartyId());
            RyUtil.updateUserInfo(user.getPartyId(), user.getTrueName(), user.getNikerName(), user.getHeadImg());//更新用户昵称信息

            BeanUtils.copyProperties(userInfoDTO, user);//赋值
//            userInfoDTO.setIdNumber(personServiceClient.getIdNumber(loginAttrUtil.getPartyId()));
            AccountNodeManagerVo accountNodeManagerVo = loginAttrUtil.getAccountNodeManager();
            accountNodeManagerVo.setUserInfoDTO(userInfoDTO);// 更新session
            accountNodeManagerVo.setGender(userInfoDTO.getSex());
            loginAttrUtil.setSessionVo(SessionConstants.XL_QY_USER, accountNodeManagerVo);

//			PersonVo personVo = loginAttrUtil.getPersonVo();
//			personVo.setGender(userInfoDTO.getSex());
//			loginAttrUtil.setSessionVo(SessionConstants.CIF_PERSON_INFO, personVo);
        } catch (BusiException e) {
            LOGGER.warn("用户信息更新失败", e);
            response = ResponseUtils.toResponse(e.getResponseEnum());
        } catch (Exception e) {
            LOGGER.warn("用户信息更新失败", e);
            response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserService.bindNewPhone", description = "更换绑定手机号码")
    public Response<Boolean> bindNewPhone(BindUserDTO bindUserDTO) {
        /** 定义返回对象 */
        Response<Boolean> response = ResponseUtils.successResponse(false);
        try {
            //step 1 valid
            com.xianglin.cif.common.service.facade.model.Response<Boolean> smsResp = appLoginServiceClient.validateSms(bindUserDTO.getMobilePhone(), bindUserDTO.getSmsCode());
            LOGGER.debug("smsResp:{}", smsResp.getCode());
            if (com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_VALDATE_POS_LOGIN_SMSERROR_COUNT.code == smsResp.getCode()) {
                throw new BusiException(ResponseEnum.BUSI_INVALD, com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_VALDATE_POS_LOGIN_SMSERROR_COUNT.tip);
            }
            if (!FacadeEnums.OK.code.equals(String.valueOf(smsResp.getCode()))) {
                throw new BusiException(ResponseEnum.BUSI_INVALD, AppservConstants.LoginError.INVALID_SMS_CODE.desc);
            }
            //step 2 valid
            com.xianglin.cif.common.service.facade.model.Response<Boolean> bindSmsResp = appLoginServiceClient.validateSms(bindUserDTO.getNewMobilePhone(), bindUserDTO.getNewSmsCode());
            LOGGER.debug("bindSmsResp:{}", bindSmsResp.getCode());
            if (com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_VALDATE_POS_LOGIN_SMSERROR_COUNT.code == bindSmsResp.getCode()) {
                throw new BusiException(ResponseEnum.BUSI_INVALD, com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.E_C_VALDATE_POS_LOGIN_SMSERROR_COUNT.tip);
            }
            if (!FacadeEnums.OK.code.equals(String.valueOf(bindSmsResp.getCode()))) {
                throw new BusiException(ResponseEnum.BUSI_INVALD, AppservConstants.LoginError.INVALID_SMS_CODE.desc);
            }

            User user = userDAO.selectByMobilePhone(bindUserDTO.getMobilePhone());
            //更新CIF信息
            /*PartyAttrContactReq partyAttrContactReq = new PartyAttrContactReq();
            PartyAttrContactVo partyAttrContactVo = new PartyAttrContactVo();
            partyAttrContactVo.setPartyId(loginAttrUtil.getPartyId());
            partyAttrContactVo.setContactType(ContactTypeEnums.MOBILE.msg);
            partyAttrContactVo.setContactInfo(bindUserDTO.getNewMobilePhone());
            partyAttrContactVo.setUpdateDate(new Date());

            partyAttrContactReq.setVo(partyAttrContactVo);
            PartyAttrContactResp partyAttrContactResp = partyAttrContactService.updatePartyAttrContact(partyAttrContactReq);
            if (!ResponseConstants.FacadeEnums.OK.code.equals(partyAttrContactResp.getCode())) {
                throw new BusiException(ResponseEnum.BUSI_INVALD, "CIF更新联系信息失败");
            }*/

            /*PersonReq personReq = new PersonReq();
            PersonVo personVo = new PersonVo();
            personVo.setPartyId(loginAttrUtil.getPartyId());
            personVo.setRegMobilePhone(bindUserDTO.getNewMobilePhone());
            personReq.setVo(personVo);
            PersonResp personResp = personServiceClient.updatePerson(personReq);
            if (!ResponseConstants.FacadeEnums.OK.code.equals(personResp.getCode())) {
                LOGGER.error("CIF更新个人手机号失败,code{}", personResp.getCode());
            }*/
            User user1 =userManager.getUserByLoginAccount(bindUserDTO.getNewMobilePhone());
            if(user1 == null){
                CustomersDTO customers = new CustomersDTO();
                customers.setPartyId(loginAttrUtil.getPartyId());
                customers.setMobilePhone(bindUserDTO.getNewMobilePhone());
                customers.setUpdater(loginAttrUtil.getPartyId()+"");
                LOGGER.info("cif updateBasicInfo reequest:{}",ToStringBuilder.reflectionToString(customers));
                com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> response1 = customersInfoService.updateBasicInfo(customers, "app");
                if(response1.isSuccess()){
                    user = User.builder().build();
                    user.setLoginName(bindUserDTO.getMobilePhone());
                    user.setNewLoginName(bindUserDTO.getNewMobilePhone());
                    user.setPartyId(loginAttrUtil.getPartyId());
                    user.setUpdateTime(new Date());
                    if(user != null && user.getPartyId() != null){
                        User u = userDAO.selectByPartyId(user.getPartyId());
                        if(u != null){
                            user.setId(u.getId());
                            userDAO.updateByPrimaryKeySelective(user);
                            response.setResult(true);
                        }
                    }
                    //userManager.updateUser(user);
                    String deviceId = loginAttrUtil.getSessionStr(SessionConstants.DEVICE_ID);
                    user.setLoginName(bindUserDTO.getNewMobilePhone());
                    userManager.logout(user);
                } else {
                    LOGGER.error("CIF更新个人手机号失败", ToStringBuilder.reflectionToString(response1));
                    response.setCode(response1.getCode());
                    response.setTips(response1.getTips());
                    response.setMemo(response1.getMemo());
//                    response.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.ERROR_CHAT_400049);
                    return response;
                }  
            }else{
                LOGGER.warn("手机号已在app内存在,code{}", bindUserDTO.getNewMobilePhone());
                response.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.ERROR_CHAT_400048);
                return response;
            }
            
            //删除登陆细信息session
            ///loginAttrUtil.getSessionHelper().removeSession(loginAttrUtil.getSessionHelper().getSession().getId());
        } catch (BusiException e) {
            LOGGER.warn("绑定新手机号码失败", e);
            response.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.ERROR_CHAT_400049);
        } catch (Exception e) {
            LOGGER.warn("绑定新手机号码失败", e);
            response.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.ERROR_CHAT_400049);
        }
        return response;
    }

    @Override
//	@ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserService.queryTopUserCommMenu", description = "获取个人常用服务")
    public Response<List<AppCommuseMenuDTO>> queryTopUserCommMenu(AppCommuseMenuDTO appCommuseMenuDTO) {
        Response<List<AppCommuseMenuDTO>> response = ResponseUtils.successResponse();
        try {
//			appCommuseMenuDTO.setPartyid(String.valueOf(loginAttrUtil.getPartyId()));
            response.setResult(userCoreService.queryTopUserCommMenu(appCommuseMenuDTO));
        } catch (BusiException e) {
            LOGGER.warn("获取前100条个人常用服务失败", e);
            response = ResponseUtils.toResponse(e.getResponseEnum());
        } catch (Exception e) {
            LOGGER.warn("获取前100条个人常用服务失败", e);
            response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
        }
        return response;
    }

    @Override
//	@ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserService.queryTopCommMenu", description = "获取常用服务")
    public Response<List<AppCommuseMenuDTO>> queryTopCommMenu(AppCommuseMenuDTO appCommuseMenuDTO) {
        Response<List<AppCommuseMenuDTO>> response = ResponseUtils.successResponse();
        try {
            response.setResult(userCoreService.queryTopCommMenu(appCommuseMenuDTO));
        } catch (BusiException e) {
            LOGGER.warn("获取前100条常用服务失败", e);
            response = ResponseUtils.toResponse(e.getResponseEnum());
        } catch (Exception e) {
            LOGGER.warn("获取前100条常用服务失败", e);
            response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
        }
        return response;
    }

    @Override
//	@ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserService.queryTopUserCommWord", description = "获取个人常用关键词")
    public Response<List<AppCommuseWordDTO>> queryTopUserCommWord(AppCommuseWordDTO appCommuseWordDTO) {
        Response<List<AppCommuseWordDTO>> response = ResponseUtils.successResponse();
        try {
//			appCommuseWordDTO.setPartyid(String.valueOf(loginAttrUtil.getPartyId()));
            response.setResult(userCoreService.queryTopUserCommWord(appCommuseWordDTO));
        } catch (BusiException e) {
            LOGGER.warn("获取前100条个人常用关键词失败", e);
            response = ResponseUtils.toResponse(e.getResponseEnum());
        } catch (Exception e) {
            LOGGER.warn("获取前100条个人常用关键词失败", e);
            response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
        }
        return response;
    }

    @Override
//	@ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserService.queryTopCommWord", description = "获取常用关键词")
    public Response<List<AppCommuseWordDTO>> queryTopCommWord(AppCommuseWordDTO appCommuseWordDTO) {
        Response<List<AppCommuseWordDTO>> response = ResponseUtils.successResponse();
        try {
            response.setResult(userCoreService.queryTopCommWord(appCommuseWordDTO));
        } catch (BusiException e) {
            LOGGER.warn("获取前100条常用关键词失败", e);
            response = ResponseUtils.toResponse(e.getResponseEnum());
        } catch (Exception e) {
            LOGGER.warn("获取前100条常用关键词失败", e);
            response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserService.saveOrUpdateUserCommMenu", description = "更新用户常用服务")
    public Response<AppCommuseMenuDTO> saveOrUpdateUserCommMenu(AppCommuseMenuDTO appCommuseMenuDTO) {
        Response<AppCommuseMenuDTO> response = ResponseUtils.successResponse();
        try {
            String partyId = String.valueOf(loginAttrUtil.getPartyId());
            appCommuseMenuDTO.setPartyid(StringUtils.isEmpty(partyId) ? Constant.COUNT_SERVICE_YOUKE_PARTYID : partyId);
            userCoreService.saveOrUpdateUserCommMenu(appCommuseMenuDTO);
        } catch (BusiException e) {
            LOGGER.warn("用户常用服务更新失败", e);
            response = ResponseUtils.toResponse(e.getResponseEnum());
        } catch (Exception e) {
            LOGGER.warn("用户常用服务更新失败", e);
            response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.UserService.saveOrUpdateUserCommWord", description = "更新用户常用关键字")
    public Response<AppCommuseWordDTO> saveOrUpdateUserCommWord(AppCommuseWordDTO appCommuseWordDTO) {
        Response<AppCommuseWordDTO> response = ResponseUtils.successResponse();
        try {
            String partyId = StringUtils.isEmpty(appCommuseWordDTO.getPartyid()) ? String.valueOf(loginAttrUtil.getPartyId()) : appCommuseWordDTO.getPartyid();
            appCommuseWordDTO.setPartyid(StringUtils.isEmpty(partyId) ? Constant.COUNT_SERVICE_YOUKE_PARTYID : partyId);
            userCoreService.saveOrUpdateUserCommWord(appCommuseWordDTO);
        } catch (BusiException e) {
            LOGGER.warn("用户常用词更新失败", e);
            response = ResponseUtils.toResponse(e.getResponseEnum());
        } catch (Exception e) {
            LOGGER.warn("用户常用词更新失败", e);
            response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
        }
        return response;
    }

}
