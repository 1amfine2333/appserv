package com.xianglin.appserv.biz.service.implement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xianglin.appserv.biz.shared.*;
import com.xianglin.appserv.biz.shared.util.ResponseCacheUtils;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.daointerface.AppUserRelationMapper;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.app.GoldService;
import com.xianglin.appserv.common.service.facade.app.IndexService;
import com.xianglin.appserv.common.service.facade.app.PersonalService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.*;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;
import com.xianglin.appserv.common.service.facade.req.UserFeedbackReq;
import com.xianglin.appserv.common.service.integration.cif.PartyAttrAccountServiceClient;
import com.xianglin.appserv.common.service.integration.cif.TransferServiceClient;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.core.service.UserRelationCoreService;
import com.xianglin.cif.common.service.facade.AuthService;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.GoldcoinService;
import com.xianglin.cif.common.service.facade.PartyAttrAccountService;
import com.xianglin.cif.common.service.facade.constant.ResponseConstants;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.model.CustomRealnameauthDTO;
import com.xianglin.cif.common.service.facade.model.CustomersDTO;
import com.xianglin.cif.common.service.facade.resp.PartyAttrAccountResp;
import com.xianglin.cif.common.service.facade.resp.PartyAttrPasswordResp;
import com.xianglin.cif.common.service.facade.vo.GoldcoinAccountVo;
import com.xianglin.cif.common.service.facade.vo.PartyAttrAccountVo;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.xlStation.common.service.facade.exception.BizException;
import com.xianglin.xlnodecore.common.service.facade.DistrictCodeFullService;
import com.xianglin.xlnodecore.common.service.facade.NodeService;
import com.xianglin.xlnodecore.common.service.facade.req.NodeReq;
import com.xianglin.xlnodecore.common.service.facade.resp.DistrictCodeFullListResp;
import com.xianglin.xlnodecore.common.service.facade.resp.DistrictCodeFullResp;
import com.xianglin.xlnodecore.common.service.facade.resp.NodeResp;
import com.xianglin.xlnodecore.common.service.facade.vo.DistrictCodeFullVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wanglei on 2017/5/2.
 */
@Service("personalService")
@ServiceInterface
public class PersonalServiceImpl implements PersonalService {

    public static final String INVITATION_CODE = "推荐码: %s";

    private static final Logger logger = LoggerFactory.getLogger(PersonalServiceImpl.class);

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private PartyAttrAccountServiceClient partyAttrAccountServiceClient;

    @Autowired
    private TransferServiceClient transferServiceClient;

    @Autowired
    private ArticleManager articleManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private DistrictCodeFullService districtCodeFullService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private UserRelationManager userRelationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomersInfoService customersInfoService;

    @Autowired
    private GoldcoinService goldcoinService;

    @Autowired
    private PropExtendManager propExtendManager;

    @Autowired
    private PartyAttrAccountService partyAttrAccountService;

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private ActivityInviteManager activityInviteManager;

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private IndexService indexService;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private ResponseCacheUtils responseCacheUtils;

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.personInfo", description = "查询个人在我的页面显示信息")
    public Response<PersonalVo> personInfo() {
        //各个数据意义对照原型图，优惠券数量需要向电商要接口，优惠券地址需要电商提供，
        Response<PersonalVo> response = ResponseUtils.successResponse();
        PersonalVo personalVo = null;
        try {
            // 获取登录用户的partyID
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                response.setResonpse(ResponseEnum.SESSION_INVALD);
                return response;
            }
            personalVo = new PersonalVo();
            // 查询用户基本信息
            User user = new User();
            user.setPartyId(partyId);
            User u = userManager.queryUser(partyId);
            if (u == null) {
                logger.info("get user is null");
                return response;
            }
            personalVo.setPartyId(partyId);
            if (StringUtils.isNotEmpty(u.getNikerName())) {
                personalVo.setNickName(u.getNikerName());
            }
            personalVo.setHeadImg(u.getHeadImg());
            personalVo.setTrueName(u.getTrueName());
            if (StringUtils.isNotEmpty(u.getShowName())) {
                personalVo.setShowName(u.getShowName());
            } else {
                personalVo.setShowName(getShowName(user));
            }
            if (StringUtils.isNotEmpty(u.getIntroduce())) {
                personalVo.setIntroduce(u.getIntroduce());
            }
            personalVo.setAccountUrl(
                    SysConfigUtil.getStr("account_balance_url") + "?markFlag=true&mobilePhone=" + u.getLoginName());
            // 查询用户余额
            //PartyAttrAccountResp resp = partyAttrAccountServiceClient.getPartyAttrAccount(partyId);
            //if (ResponseConstants.FacadeEnums.ACCOUNT_IS_EXISTS.code.equals(resp.getCode())) {

            PartyAttrAccountResp cifResp = partyAttrAccountService.getPartyAttrAccount(partyId);
            logger.info("query getPartyAttrAccount partyID:{},resp:{}", partyId, ToStringBuilder.reflectionToString(cifResp));
            if (cifResp.getPartyAttrAccountVo() == null) {
                PartyAttrAccountVo vo = new PartyAttrAccountVo();
                vo.setPartyId(partyId);
                vo.setAppId("app");
                vo.setCreator("" + partyId);
                partyAttrAccountService.addPartyAttrAccount(vo);
            }
            com.xianglin.te.common.service.facade.resp.Response<String> balance = transferServiceClient
                    .abQuery(partyId);
            if (FacadeEnums.OK.code == balance.getCode()) {
                if (NumberUtil.amountFormat(balance.getResult()) == null) {
                    personalVo.setAccountBalance("0.00");
                } else {
                    personalVo.setAccountBalance(NumberUtil.amountFormat(balance.getResult()));
                }
            } else {
                personalVo.setAccountBalance("0.00");
            }
            //}
            // 查询动态数
            Map<String, Object> paras = new HashMap<String, Object>();
            paras.put("partyId", partyId);
            paras.put("articleType", Constant.ArticleType.SUBJECT.name());
            Integer queryArticleCount = articleManager.queryArticleCount(paras);
            if (queryArticleCount == null) {
                queryArticleCount = 0;
            }
            //查询短视频数
            paras.put("articleType", Constant.ArticleType.SHORT_VIDEO.name());
            Integer shortVideoCount = articleManager.queryArticleCount(paras);
            if (shortVideoCount == null) {
                shortVideoCount = 0;
            }
            personalVo.setArticleCount(queryArticleCount);
            personalVo.setShortVideoCount(shortVideoCount);
            // 查询我的关注数
            personalVo.setFansCount(userRelationManager.queryRelationCount(partyId,Constant.RelationStatus.FANS.name()));
            personalVo.setFollowCount(userRelationManager.queryRelationCount(partyId,Constant.RelationStatus.FOLLOW.name()));

            //查询收藏数
            Map<String, Object> map = DTOUtils.queryMap();
            map.put("partyId", partyId);
            map.put("tipType", Constant.ArticleTipType.COLLET.name());
            // 根据partyId查当前用户的动态和评论id
            Integer collet = articleManager.queryArticleTipCount(map);
            personalVo.setCollectionCount(collet);
            //查询用户是否实名认证
            com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> resp2 = customersInfoService.selectCustomsAlready2Auth(partyId);
            logger.info("selectCustomsAlready2Auth info", resp2.toString());
            Boolean isAuth = false;
            if (resp2.getResult() != null) {
                if (StringUtils.isNotEmpty(resp2.getResult().getAuthLevel())) {
                    isAuth = true;
                    personalVo.setIdNumber(resp2.getResult().getCredentialsNumber());

                }
            }
            personalVo.setIsAuth(isAuth);
            //查金币
            try {
                com.xianglin.cif.common.service.facade.model.Response<GoldcoinAccountVo> goldcoinAccountVoResponse = goldcoinService.queryAccount(partyId);
                if (goldcoinAccountVoResponse.getResult() != null) {
                    personalVo.setGold(goldcoinAccountVoResponse.getResult().getAmount());
                }
            } catch (Exception e) {
                logger.warn("goldcoinService.queryAccount error", e);
            }

            // 查询用户的优惠卡数和地址
            Map<String, String> param = new HashMap<>();
            String app_key = "xianglin".concat(DateUtils.formatDate(DateUtils.getNow(), DateUtils.DATE_TPT_TWO))
                    .concat("@#_$&");
            param.put("party_id", partyId + "");
            app_key = app_key.concat(SHAUtil.getSortString(param));
            param.put("app_key", app_key);
            param.put("sign", SHAUtil.shaEncode(MD5.encode(app_key)));
            try {
                String json = HttpUtils
                        .executePost(SysConfigUtil.getStr("couponCountUrl"), param);
                logger.info("Coupon:{}", json);
                JSONObject object = JSONObject.parseObject(json);
                if (object != null) {
                    if ("".equals(object.getString("error"))) {
                        String coupontotal = object.getJSONObject("data").getString("total");
                        personalVo.setCouponCount(Integer.valueOf(coupontotal));
                        personalVo.setCouponUrl(SysConfigUtil.getStr("coupon_count_url"));
                    } else {
                        String error = object.getString("error");
                        logger.info("get couponCount" + error);
                        personalVo.setCouponCount(0);
                        personalVo.setCouponUrl(SysConfigUtil.getStr("coupon_count_url"));
                    }
                } else {
                    logger.info("get json null");
                    personalVo.setCouponCount(0);
                    personalVo.setCouponUrl(SysConfigUtil.getStr("coupon_count_url"));
                }
            } catch (Exception e) {
                logger.warn("getJSON error", e);
            }
            response.setResult(personalVo);
        } catch (Exception e) {
            logger.warn("personInfo error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    private String getShowName(User user) {

        String showName = null;
        if (StringUtils.isEmpty(user.getShowName())) {
            if (user.getPartyId() != null) {
                showName = "xl" + user.getPartyId();
            }
            if (StringUtils.isNotEmpty(user.getTrueName())) {
                showName = user.getTrueName();
            }
            if (StringUtils.isNotEmpty(user.getNikerName())) {
                showName = user.getNikerName();
            }
        }
        return showName;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.unRead  MsgCount", description = "未读消息数")
    public Response<Integer> unReadMsgCount() {

        Response<Integer> response = ResponseUtils.successResponse();
        Integer count = 0;
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                response.setResonpse(ResponseEnum.SESSION_INVALD);
                return response;
            }
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            paras.put("status", 1);
            paras.put("msgTypes", Constant.MsgType.toList(Constant.MsgType.NEWS.name(), Constant.MsgType.ESHOP.name()));

            count = userManager.queryMsgCount(paras);
        } catch (Exception e) {
            logger.warn("unReadMsgCount error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        response.setResult(count);
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.submitFeedback", description = "用户提交反馈")
    public Response<Boolean> submitFeedback(String content) {

        Response<Boolean> response = ResponseUtils.successResponse();
        try {
            //获取当前登录用户的partyID
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                response.setResonpse(ResponseEnum.SESSION_INVALD);
                return response;
            }
            //判断countent是否为空或是否超过150
            if (StringUtils.isEmpty(content) || content.length() > 150) {
                response.setResult(false);
                return response;
            }
            logger.info("content===============" + content);
            //查询当天的日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String today = sdf.format(new Date());
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            paras.put("date", today);
            //查询当前登录用户今天的提交数
            Integer count = userManager.queryFeddbackCount(paras);
            //判断提交数是否小于3条，小于3条就提交
            if (count < 3) {
                //根据用户的partyID查询用户的昵称
                User queryUser = userManager.queryUser(partyId);
                UserFeedback userFeedback = new UserFeedback();
                userFeedback.setPartyId(partyId);
                userFeedback.setContent(content);
                userFeedback.setDate(today);
                userFeedback.setCreater(queryUser.getTrueName());
                Boolean insertFeedback = userManager.addUserFeedback(userFeedback);
                response.setResult(insertFeedback);
            } else {
                response.setResult(false);
                response.setFacade(FacadeEnums.ERROR_CHAT_400012);
                return response;
            }
        } catch (Exception e) {
            logger.warn("submitFeedback error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.personSetInfo", description = "查询个人用户设置")
    public Response<PersonalSetVo> personSetInfo() {

        Response<PersonalSetVo> response = ResponseUtils.successResponse();
        Integer count = 0;
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                response.setResonpse(ResponseEnum.SESSION_INVALD);
                return response;
            }
            User user = userManager.queryUser(partyId);
            PersonalSetVo vo = new PersonalSetVo();
            vo.setHasLoginPassword(StringUtils.isNotEmpty(user.getPassword()));
            vo.setTranPasswordUrl(SysConfigUtil.getStr("trans_password_url"));

            PartyAttrPasswordResp resp = partyAttrAccountServiceClient.selectTradePwd(partyId);
            if (ResponseConstants.FacadeEnums.OK.code.equals(resp.getCode())) {
                vo.setHasTranPassword(true);
            } else {
                vo.setHasTranPassword(false);
            }
            response.setResult(vo);
        } catch (Exception e) {
            logger.warn("personSetInfo error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.deleteUserMsg", description = "删除个人消息")
    public Response<Integer> deleteUserMsg(String[] msgIds) {

        Response<Integer> response = ResponseUtils.successResponse();
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        try {
            if (partyId == null) {
                response.setResonpse(ResponseEnum.SESSION_INVALD);
                return response;
            }
            if (StringUtils.isEmpty(msgIds[0])) {
                response.setResult(0);
                return response;
            }
            String msgId = StringUtils.join(msgIds, ",");
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            paras.put("msgId", msgId);
            Integer updateUserMsg = userManager.updateUserMsg(paras);
            response.setResult(updateUserMsg);
        } catch (Exception e) {
            logger.warn("deleteUserMsg error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.queryDistrictList", description = "级联区域查询")
    public Response<List<DistrictVo>> queryDistrictList(String paraCode) {

        Response<List<DistrictVo>> response = ResponseUtils.successResponse();
        try {
            DistrictCodeFullListResp districtResp = null;
            int length = StringUtils.length(paraCode);
            switch (length) {
                case 0:
                    districtResp = districtCodeFullService.queryProvince();
                    break;
                case 2:
                    districtResp = districtCodeFullService.queryCity(paraCode);
                    break;
                case 4:
                    districtResp = districtCodeFullService.queryCounty(paraCode);
                    break;
                case 6:
                    districtResp = districtCodeFullService.queryTown(paraCode);
                    break;
                default:
                    districtResp = districtCodeFullService.queryVillage(paraCode);
            }
            List<DistrictVo> voList = new ArrayList<>();
            if (districtResp != null && CollectionUtils.isNotEmpty(districtResp.getListVo())) {
                DistrictVo vo = null;
                for (DistrictCodeFullVo district : districtResp.getListVo()) {
                    vo = new DistrictVo();
                    vo.setCode(district.getDistrictCode());
                    vo.setName(StringUtils.isNotEmpty(district.getDistrictName()) ? district.getProvinceName() : StringUtils.isNotEmpty(district.getCityName()) ? district.getCityName() : StringUtils.isNotEmpty(district.getCountyName()) ? district.getCountyName() : StringUtils.isNotEmpty(district.getTownName()) ? district.getTownName() : district.getVillageName());
                    voList.add(vo);
                }
            }
            response.setResult(voList);
        } catch (Exception e) {
            logger.warn("queryDistrictList error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.addRealName", description = "提交实名认证信息")
    public Response<Boolean> addRealName(RealNameVo vo) {

        Response<Boolean> response = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                response.setResonpse(ResponseEnum.SESSION_INVALD);
                return response;
            }
            User user = userManager.queryUser(partyId);
            logger.info("cif selectCustomsAlready2Auth info :{}", ToStringBuilder.reflectionToString(partyId));
            com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> resp = customersInfoService.selectCustomsAlready2Auth(partyId);
            logger.info("cif selectCustomsAlready2Auth resp :{}", ToStringBuilder.reflectionToString(resp));
            if (null == resp.getResult()) {
                CustomersDTO customers = new CustomersDTO();
                customers.setPartyId(partyId);
                customers.setMobilePhone(user.getLoginName());
                customers.setCustomerName(vo.getUserName());
                customers.setCredentialsType("身份证");
                customers.setCredentialsNumber(vo.getIdNumber());
                customers.setCreator(partyId + "");
                logger.info("cif secondAuth info :{}", ToStringBuilder.reflectionToString(customers));
                com.xianglin.cif.common.service.facade.model.Response<CustomRealnameauthDTO> resp1 = authService.twoFactorAuth(customers, "app");
                logger.info("cif secondAuth resp :{}", ToStringBuilder.reflectionToString(resp1));
                if (resp1.isSuccess()) {
                    response.setResult(true);
                    //同步appuser数据
                    if (resp1.getResult() != null && StringUtils.isNotEmpty(resp1.getResult().getCredentialsName())) {
                        user.setTrueName(resp1.getResult().getCredentialsName());
                        userManager.updateUser(user);
                    }
                    //实名认证金币奖励
                    activityManager.rewardV321(user.getPartyId(), Constant.ActivityTaskType.REALNAME_AUTH, Constant.ActivityTaskType.F_REALNAME_AUTH);
                } else {
                    response.setCode(resp1.getCode());
                    response.setTips(resp1.getTips());
                }
            }
        } catch (Exception e) {
            logger.warn("addRealName error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * 查询实名认证信息
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.queryRealName", description = "查询实名认证信息")
    public Response<RealNameVo> queryRealName() {

        Response<RealNameVo> response = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                response.setResonpse(ResponseEnum.SESSION_INVALD);
                return response;
            }
            com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> resp = customersInfoService.selectCustomsAlready2Auth(partyId);
            /*PrincipalDTO dto = new PrincipalDTO();
            dto.setPartyId(partyId);
            com.xianglin.cif.common.service.facade.model.Response<PartyAttrRealnameauthVo> resp = authenticationService.queryAuthLevelByPartyId(dto);*/
            if (resp.getResult() != null) {
                RealNameVo realNameVo = new RealNameVo();
                User u = userManager.queryUser(partyId);
                if (StringUtils.isNotEmpty(u.getDistrict())) {
                    AreaVo area = JSON.parseObject(u.getDistrict(), AreaVo.class);
                    realNameVo.setCity(area.getCity());
                    realNameVo.setProvince(area.getProvince());
                    realNameVo.setVillage(area.getVillage());
                    realNameVo.setCounty(area.getCounty());
                    realNameVo.setTown(area.getTown());
                    realNameVo.setDistrict(area.toString());
                }
                if (StringUtils.isNotEmpty(resp.getResult().getCredentialsNumber())) {
                    String idNumber = resp.getResult().getCredentialsNumber();
                    idNumber = idNumber.replace(idNumber.substring(idNumber.length() - 6, idNumber.length()), "******");
                    realNameVo.setIdNumber(idNumber);
                    realNameVo.setIdCard(resp.getResult().getCredentialsNumber());
                }
                realNameVo.setUserName(resp.getResult().getCustomerName());
                if (resp.getResult().getAuthLevel() != null && resp.getResult().getAuthLevel().contains("R")) {
                    realNameVo.setRealNameStatus("V0");
                }
                response.setResult(realNameVo);
            }
        } catch (Exception e) {
            logger.warn("queryRealName error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }


    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.queryUser", description = "查询用户信息")
    public Response<UserVo> queryUser(Long partyId) {

        Response<UserVo> response = ResponseUtils.successResponse();
        try {
            // 查询用户基本信息
            User user = new User();
            user.setPartyId(partyId);
            User u = userManager.queryUser(partyId);
            if (u == null) {
                logger.info("get user is null");
                return response;
            }
            if (StringUtils.isEmpty(u.getHeadImg())) {
                u.setHeadImg(SysConfigUtil.getStr("default_user_headimg"));
            }
            UserVo userVo = convertUserVo(u);
            userVo.setFansNumber(userRelationManager.queryRelationCount(partyId,Constant.RelationStatus.FANS.name()));
            userVo.setFollowsNumber(userRelationManager.queryRelationCount(partyId,Constant.RelationStatus.FOLLOW.name()));

            Long loginUser = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (loginUser != null) {
                AppUserRelation relation = userRelationManager.queryRelation(loginUser, partyId);
                if (relation != null && StringUtils.equals(relation.getFollowStatus(), YesNo.Y.name())) {
                    userVo.setBothStatus(Constant.RelationStatus.FOLLOW.name());
                } else {
                    userVo.setBothStatus(Constant.RelationStatus.UNFOLLOW.name());
                }
            }
            response.setResult(userVo);
        } catch (Exception e) {
            logger.warn("queryUser error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.queryPersonal", description = "查询当前登陆用户")
    public Response<UserVo> queryPersonal() {

        Response<UserVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            User u = userManager.queryUser(partyId);
            if (u == null) {
                logger.info("get user is null");
                return resp;
            }

            if (StringUtils.isEmpty(u.getHeadImg())) {
                u.setHeadImg(SysConfigUtil.getStr("default_user_headimg"));
            }

            UserVo userVo = convertUserVo(u);

            //=========推荐人信息============
            String introducerName = "";
            com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> customersDTOResponse1 = customersInfoService.selectByPartyId(sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class));
            logger.info("===========cif 推荐人信息：[[ {} ]]===========", JSON.toJSONString(customersDTOResponse1, true));
            if (customersDTOResponse1.isCifSuccess() && customersDTOResponse1.getResult() != null) {
                CustomersDTO invitationCustom = customersDTOResponse1.getResult().getInvitationCustom();
                Long recPartyId = null;
                if (invitationCustom != null) {
                    recPartyId = invitationCustom.getPartyId();
                } else {
                    HashMap<String, Object> params = Maps.newHashMap();
                    params.put("partyId", partyId);
                    params.put("status", "S");
                    params.put("startPage", 1);
                    params.put("pageSize", 100);
                    List<ActivityInviteDetail> inviteDetails = activityInviteManager.queryInvateDetail(params);

                    if (!inviteDetails.isEmpty()) {
                        CustomersDTO customersDTO = new CustomersDTO();
                        customersDTO.setPartyId(partyId);
                        recPartyId = inviteDetails.get(0).getRecPartyId();
                        if (recPartyId != null) {
                            customersDTO.setInvitationPartyId(recPartyId);
                            com.xianglin.cif.common.service.facade.model.Response<Boolean> booleanResponse = customersInfoService.syncInvitationCustomer(customersDTO);
                            logger.info("===========同步推荐人[[ {} ]]===========", booleanResponse);
                        }
                    }
                }
                User recUser = userManager.queryUser(recPartyId);
                if (recUser != null && recPartyId > 1) {
                    if (!Strings.isNullOrEmpty(recUser.getTrueName())) {
                        introducerName = recUser.getTrueName();
                    } else if (!Strings.isNullOrEmpty(recUser.getNikerName())) {
                        introducerName = recUser.getNikerName();
                    } else {
                        introducerName = "xl" + recUser.getPartyId();
                    }
                }
            }
            userVo.setIntroducerName(introducerName);
            //查询用户是否实名认证
            com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> resp2 = customersInfoService.selectCustomsAlready2Auth(partyId);
            logger.info("selectCustomsAlready2Auth info", resp2.toString());
            if (resp2.getResult() != null) {
                if (StringUtils.isNotEmpty(resp2.getResult().getAuthLevel())) {
                    userVo.setCertificatesNumber(resp2.getResult().getCredentialsNumber());
                }
            }

            resp.setResult(userVo);
        } catch (Exception e) {
            logger.warn("queryPersonal error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private UserVo convertUserVo(User u) {

        UserVo userVo = new UserVo();
        if (u != null) {
            userVo.setPartyId(u.getPartyId());
            userVo.setCreateTime(u.getCreateTime());
            userVo.setSessionId(u.getSessionId());
            userVo.setLoginName(u.getLoginName());
            userVo.setIsDeleted(u.getIsDeleted());
            userVo.setDeviceId(u.getDeviceId());
            userVo.setUserType(u.getUserType());
            userVo.setUpdateTime(u.getUpdateTime());
            if (StringUtils.isNotEmpty(u.getBirthday())) {
                userVo.setBirthday(u.getBirthday());
            }
            userVo.setGender(u.getGender());
            if (StringUtils.isNotEmpty(u.getHeadImg())) {
                userVo.setHeadImg(u.getHeadImg());
            }
            userVo.setId(u.getId());
            if (StringUtils.isNotEmpty(u.getIntroduce())) {
                userVo.setIntroduce(u.getIntroduce());
            }
            if (StringUtils.isNotEmpty(u.getComments())) {
                userVo.setComments(u.getComments());
            }
            if (StringUtils.isNotEmpty(u.getCity())) {
                userVo.setCity(u.getCity());
            }
            if (StringUtils.isNotEmpty(u.getVillage())) {
                userVo.setVillage(u.getVillage());
            }
            if (StringUtils.isNotEmpty(u.getLocation())) {
                userVo.setLocation(u.getLocation());
            }
            if (StringUtils.isNotEmpty(u.getTown())) {
                userVo.setTown(u.getTown());
            }
            if (StringUtils.isNotEmpty(u.getTrueName())) {
                userVo.setTrueName(u.getTrueName());
            }
            if (StringUtils.isNotEmpty(u.getStatus())) {
                userVo.setStatus(u.getStatus());
            }
            userVo.setRyToken(u.getRyToken());
            if (StringUtils.isNotEmpty(u.getProvince())) {
                userVo.setProvince(u.getProvince());
            }
            AreaVo locationArea = JSON.parseObject(u.getLocation(), AreaVo.class);
            if (StringUtils.isNotEmpty(u.getNikerName())) {
                userVo.setNikerName(u.getNikerName());
            }
            if (locationArea != null) {
                userVo.setLocationArea(locationArea);
            }
            if (StringUtils.isNotEmpty(u.getCounty())) {
                userVo.setCounty(u.getCounty());
            }
            if (StringUtils.isNotEmpty(u.getDistrict())) {
                AreaVo areaVo = JSON.parseObject(u.getDistrict(), AreaVo.class);
                userVo.setDistrict(areaVo);
            }
            if (StringUtils.isNotEmpty(u.getDescs())) {
                userVo.setDescs(u.getDescs());
            }
            if (StringUtils.isNotEmpty(u.getShowName())) {
                userVo.setShowName(u.getShowName());
            } else {
                if (u.getPartyId() != null) {
                    userVo.setShowName("xl" + u.getPartyId());
                }
                if (StringUtils.isNotEmpty(u.getTrueName())) {
                    userVo.setShowName(u.getTrueName());
                }
                if (StringUtils.isNotEmpty(u.getNikerName())) {
                    userVo.setShowName(u.getNikerName());
                }
            }
            if (u.getPartyId() != null) {
                String name = queryRealName(u.getPartyId());
                if (name != null) {
                    userVo.setIsAuth(true);
                }
            }
        }
        return userVo;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.updateUser", description = "修改用户信息")
    public Response<UserVo> updateUser(UserVo vo) {

        Response<UserVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            User user = new User();
            UserVo userVo = null;
            BeanUtils.copyProperties(user, vo);
            if (vo != null) {

                if (vo.getLocationArea() != null) {
                    user.setLocation(vo.getLocationArea().toString());
                }

                if (vo.getDistrict() != null) {
                    //判断是否需要修改家乡地址
                    User currentUser = userManager.queryUser(partyId);
                    if (currentUser == null) {
                        resp.setFacade(FacadeEnums.RETURN_EMPTY);
                        return resp;
                    }
                    boolean equals = false;
                    AreaVo updateAreavo = vo.getDistrict();
                    AreaVo currentArea = null;

                    String currentUserDistrict = currentUser.getDistrict();
                    if (!Strings.isNullOrEmpty(currentUserDistrict)) {
                        currentArea = JSON.parseObject(currentUserDistrict, AreaVo.class);
                    }
                    if (currentArea == null) {
                        currentArea = new AreaVo();
                        equals = false;
                    } else {
                        String currentLowCode = currentArea.selectCode(currentArea);
                        String updateLowCode = updateAreavo.selectCode(updateAreavo);
                        if (StringUtils.equals(currentLowCode, updateLowCode)) {
                            equals = true;
                        }
                    }
                    if (equals) {
                        logger.info("===========家乡地址未修改，无需进行修改逻辑[[ {} ]]===========", JSON.toJSONString(vo, true));
                    }
                    if (!equals) {
                        //多级异常处理
                        try {
                            //版本控制
                            String versionStr = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
                            Integer version = Integer.valueOf(versionStr.replace(".", ""));
                            if (version - 353 < 0) {
                                resp.setFacade(FacadeEnums.ERROR_CHAT_400055);
                                return resp;
                            }
                            boolean updateFlag = updateDistrict(updateAreavo);

                            //完善资料奖励
                            if (updateFlag) {
                                String currentCode = currentArea.selectCode();
                                if (Strings.isNullOrEmpty(currentCode)) {

                                    activityManager.rewardV321(partyId, Constant.ActivityTaskType.PERFECT_DATA, Constant.ActivityTaskType.F_PERFECT_DATA);

//                                    HashMap<String, Object> queryParams = Maps.newHashMap();
//                                    queryParams.put("partyId", partyId);
//                                    queryParams.put("taskCode", "PERFECT_DATA");
//                                    queryParams.put("taskStatus", Constant.YESNO.YES.code);
//                                    queryParams.put("useStatus", Constant.YESNO.YES.code);
//                                    queryParams.put("taskResult", 2000);
//                                    List<AppActivityTask> appActivityTasks = activityManager.queryActivityTaskByParas(queryParams);
//                                    if (appActivityTasks.isEmpty()) {
//
//
//
//                                        goldService.award("PERFECT_DATA");
//                                        com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> customersDTOResponse1 = customersInfoService.selectByPartyId(partyId);
//                                        if (customersDTOResponse1.isCifSuccess() && customersDTOResponse1.getResult() != null) {
//                                            CustomersDTO invitationCustom = customersDTOResponse1.getResult().getInvitationCustom();
//                                            if (invitationCustom != null) {  //是否有推荐人
//                                                queryParams.put("partyId", invitationCustom.getPartyId());
//                                                queryParams.put("taskCode", "F_PERFECT_DATA");
//                                                List<AppActivityTask> fAppActivityTasks = activityManager.queryActivityTaskByParas(queryParams); //是否已经发放奖励
//                                                if (fAppActivityTasks.isEmpty()) {
//                                                    goldService.award("F_PERFECT_DATA");
//                                                }
//                                            }
//                                        }
//                                    }
                                }
                            }

                        } catch (IllegalStateException e) {
                            logger.warn("===========地区修改失败-数据库数据不一致：[[ {} ]]===========", e);
                            resp.setFacade(FacadeEnums.UPDATE_INVALID);
                            resp.setTips(e.getMessage());
                            return resp;
                        } catch (UnsupportedOperationException | IllegalArgumentException e) {
                            logger.info("===========地区修改失败-错误的请求：[[ {} ]]===========", e);
                            resp.setFacade(FacadeEnums.UPDATE_INVALID);
                            resp.setTips(e.getMessage());
                            return resp;
                        } catch (Exception e) {
                            logger.warn("===========地区修改失败-未知异常：[[ {} ]]===========", e);
                            resp.setFacade(FacadeEnums.UPDATE_INVALID);
                            return resp;
                        }

                    }
                }
            }
            //前面已经更新过，这里置空不再重复更新
            user.setProvince(null);
            user.setCity(null);
            user.setCounty(null);
            user.setTown(null);
            user.setVillage(null);
            user.setDistrict(null);
            
            int o = userManager.updateUser(user);
            if (o == 1) {
                userVo = convertUserVo(user);
                //更新用户的二维码
                if(StringUtils.isNotEmpty(user.getHeadImg())){
                    queryUserQRCode();
                }
            }
            afterHeadImgSet(partyId, user);
            resp.setResult(userVo);

        } catch (Exception e) {
            logger.warn("updateUser error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 查询实名认证姓名
     *
     * @param partyId
     * @return
     */
    private String queryRealName(long partyId) {

        String name = null;
        com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> resp = customersInfoService.selectCustomsAlready2Auth(partyId);
        if (resp.getResult() != null) {
            if (StringUtils.isNotEmpty(resp.getResult().getAuthLevel())) {
                name = resp.getResult().getCustomerName();
            }
        }
        return name;
    }

    private void afterHeadImgSet(Long partyId, User user) {

        //删除与头像相关的验证码
        if (!Strings.isNullOrEmpty(user.getHeadImg())) {

            HashMap<String, Object> params = Maps.newHashMap();

            params.put("relationId", partyId);
            params.put("type", User.class.getSimpleName());
            params.put("ekey", MapVo.USER_QRCODE);
            List<AppPropExtend> appPropExtends = propExtendManager.queryChannel(params);

            for (AppPropExtend input : appPropExtends) {
                AppPropExtend tempPojo = AppPropExtend
                        .builder().id(input.getId()).isDeleted("Y")
                        .build();
                propExtendManager.update(tempPojo);
            }

        }
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.queryNodeInfo", description = "查询站长站点信息")
    public Response<NodeVo> queryNodeInfo() {

        Response<NodeVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            NodeReq req = new NodeReq();
            com.xianglin.xlnodecore.common.service.facade.vo.NodeVo vo = new com.xianglin.xlnodecore.common.service.facade.vo.NodeVo();
            vo.setNodeManagerPartyId(partyId);
            req.setVo(vo);
            NodeResp nodeResp = nodeService.queryNodeInfoByNodeManagerPartyId(req);
            logger.info("node info {},{}", partyId, nodeResp);
            if (nodeResp.getVo() != null) {
                resp.setResult(DTOUtils.map(nodeResp.getVo(), NodeVo.class));
            }
        } catch (Exception e) {
            logger.warn("queryNodeInfo error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 根据条件分页查询所有的反馈
     *
     * @param req
     * @return
     */
    @Override
    public Response<List<UserFeedbackVo>> queryUserFeedBackByParas(UserFeedbackReq req) {

        Response<List<UserFeedbackVo>> resp = ResponseUtils.successResponse();
        try {
            Map<String, Object> paras = DTOUtils.queryMap();
            if (StringUtils.isNotEmpty(req.getContent())) {
                paras.put("likeContent", req.getContent());
            }
            if (StringUtils.isNotEmpty(req.getStartDate()) && StringUtils.isNotEmpty(req.getEndDate())) {
                paras.put("startDate", req.getStartDate());
                paras.put("endDate", req.getEndDate());
            }
            if (StringUtils.isNotEmpty(req.getStatus())) {
                paras.put("status", req.getStatus());
            }
            paras.put("startPage", req.getStartPage());
            paras.put("pageSize", req.getPageSize());

            List<UserFeedback> list = userManager.selectList(paras);
            List<UserFeedbackVo> uList = DTOUtils.map(list, UserFeedbackVo.class);
            if (uList.size() > 0) {
                for (UserFeedbackVo userFeedbackVo : uList) {
                    if (StringUtils.isNotEmpty(userFeedbackVo.getStatus())) {
                        userFeedbackVo.setStatus(userFeedbackVo.getStatus());
                    } else {
                        userFeedbackVo.setStatus("N");
                    }
                }
            }
            resp.setResult(uList);
            resp.setCode(1000);
        } catch (Exception e) {
            logger.warn("queryUserFeedBackByParas error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.queryQrCode", description = "查询分享二维码地址")
    public Response<String> queryQrCode() {

        Response<String> resp = ResponseUtils.successResponse();
        try {
//            resp.setResult(SysConfigUtil.getStr("app_download_url"));
//            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
//            if (partyId != null && partyId != 0) {
//                User u = userManager.queryUser(partyId);
//                if (u != null) {
//                    if (StringUtils.isNotEmpty(u.getDescs())) {
//                        JSONObject json = JSON.parseObject(u.getDescs());
//                        if (StringUtils.equals(u.getHeadImg(), json.getString("headImg"))) {
//                            resp.setResult(json.getString("qrCodeImg"));
//                        } else {
//                            String qrCode = createQrCode(resp.getResult(), u.getHeadImg());
//                            resp.setResult(qrCode);
//                            updateUserQrCode(u, qrCode);
//                        }
//                    } else {
//                        String qrCode = createQrCode(resp.getResult(), u.getHeadImg());
//                        resp.setResult(qrCode);
//                        updateUserQrCode(u, qrCode);
//                    }
//                }
//            }
            resp = queryGoldQRCode();
        } catch (Exception e) {
            logger.warn("queryQrCode error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 修改用户反馈
     *
     * @param userFeedbackVo
     * @return
     */
    @Override
    public Response<Boolean> updateUserFeedback(UserFeedbackVo userFeedbackVo) {

        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            UserFeedback userFeedback = userManager.queryFeedback(userFeedbackVo.getId());
            if (userFeedback != null) {
                Boolean update = userManager.updateUserFeedback(DTOUtils.map(userFeedbackVo, UserFeedback.class));
                //如果有回复则向用户发送回复内容
                if (StringUtils.isNotEmpty(userFeedbackVo.getRemark())) {
                    List<Long> partyIds = Lists.newArrayList();
                    partyIds.add(userFeedbackVo.getPartyId());
                    if (partyIds.size()>0){
                        messageManager.sendMsg(MsgVo.builder().partyId(userFeedback.getPartyId()).msgTitle("反馈信息回复").isSave(Constant.YESNO.YES)
                                .message(userFeedbackVo.getRemark()).msgType(Constant.MsgType.FEEDBACK.name()).loginCheck(Constant.YESNO.NO.code).passCheck(Constant.YESNO.NO.code).expiryTime(0).isDeleted("N").msgSource(Constant.MsgType.FEEDBACK.name()).build());
                    }
                }
                resp.setResult(update);
                resp.setCode(1000);
            }
        } catch (Exception e) {
            logger.warn("queryUserFeedBackByParas error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 根据条件查询反馈数
     *
     * @param req
     * @return
     */
    @Override
    public Response<Integer> queryUserFeedBackCountByParas(UserFeedbackReq req) {

        Response<Integer> resp = ResponseUtils.successResponse();
        try {
            Map<String, Object> paras = DTOUtils.queryMap();
            if (StringUtils.isNotEmpty(req.getContent())) {
                paras.put("likeContent", req.getContent());
            }
            if (StringUtils.isNotEmpty(req.getStartDate()) && StringUtils.isNotEmpty(req.getEndDate())) {
                paras.put("startDate", req.getStartDate());
                paras.put("endDate", req.getEndDate());
            }
            if (StringUtils.isNotEmpty(req.getStatus())) {
                paras.put("status", req.getStatus());
            }
            int count = userManager.queryFeddbackCount(paras);
            resp.setResult(count);
            resp.setCode(1000);
        } catch (Exception e) {
            logger.warn("queryUserFeedBackByParas error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.queryGoldQRCode", description = "金币系统生成二维码地址")
    public Response<String> queryGoldQRCode() {

        Response<String> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            if (partyId != null && partyId != 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("relationId", partyId);
                map.put("type", User.class.getSimpleName());
                map.put("ekey", MapVo.RECOMMEND_USER_QR_URL);
                List<AppPropExtend> list = propExtendManager.queryChannel(map);

                User user = userManager.queryUser(partyId);
                String img = "";
                if (user != null && StringUtils.isNotEmpty(user.getHeadImg())) {
                    img = user.getHeadImg();
                } else {
                    img = SysConfigUtil.getStr("gold_qr_code_img");
                }

                if (list.size() > 0) {
                    if (StringUtils.isNotEmpty(list.get(0).getValue())) {
                        resp.setResult(list.get(0).getValue());
                    } else {
                        String qrCode = createQrCode(SysConfigUtil.getStr("user_share_url2") + "?partyId=" + partyId, img, true);
                        propExtendManager.update(AppPropExtend.builder().ekey(MapVo.RECOMMEND_USER_QR_URL).value(qrCode).deviceId(deviceId).type(User.class.getSimpleName()).relationId(partyId).build());
                        resp.setResult(qrCode);
                    }
                } else {
                    String aa = SysConfigUtil.getStr("user_share_url2") + "?partyId=" + partyId;
                    String qrCode = createQrCode(aa, img, true);
                    propExtendManager.insert(AppPropExtend.builder().ekey(MapVo.RECOMMEND_USER_QR_URL).value(qrCode).deviceId(deviceId).type(User.class.getSimpleName()).relationId(partyId).build());
                    resp.setResult(qrCode);
                }
            }
        } catch (Exception e) {
            logger.warn("queryGoldQRCode error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.unReadMsgCountV2", description = "根据类型查询未读消息数")
    public Response<Integer> unReadMsgCountV2(String msgType) {

        Response<Integer> response = ResponseUtils.successResponse();
        Integer count = 0;
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            paras.put("status", 1);
            paras.put("msgTypes", Arrays.asList(msgType));
            count = userManager.queryMsgCount(paras);
        } catch (Exception e) {
            logger.warn("unReadMsgCount error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        response.setResult(count);
        return response;
    }


    /**
     * @param req
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.listUserMsg", description = "根据类型查询消息")
    public Response<List<MsgVo>> listUserMsg(MsgQuery req) {

        Response<List<MsgVo>> response = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            List<MsgVo> msgList = messageManager.list(req, partyId);
            messageManager.read(partyId, req.getMsgType());
            response.setResult(msgList);
        } catch (Exception e) {
            logger.warn("unReadMsgCount error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.queryFirstMsg", description = "查询最新一套消息")
    public Response<MsgVo> queryFirstMsg(MsgQuery req) {

        Response<MsgVo> response = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            MsgQuery paras = MsgQuery.builder().msgType(req.getMsgType()).pageSize(2).startPage(1).build();
            List<MsgVo> msgList = messageManager.list(paras, partyId);
            if (CollectionUtils.isNotEmpty(msgList)) {
                response.setResult(msgList.get(0));
            }
        } catch (Exception e) {
            logger.warn("unReadMsgCount error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.queryAnnualReport", description = "年报数据查询")
    public Response<Map> queryAnnualReport() {

        Response<Map> response = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            response.setResult(userManager.queryAnnualReport(partyId));
        } catch (Exception e) {
            logger.warn("unReadMsgCount error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.queryDistrictByIdNumber", description = "根据身份证号查询3级地址")
    public Response<AreaVo> queryDistrictByIdNumber(String idNumber) {

        Response<AreaVo> response = ResponseUtils.successResponse();

        try {
            if (StringUtils.isNotEmpty(idNumber)) {
                AreaVo areaVo = new AreaVo();
                //截取身份证号的前6位
                DistrictCodeFullResp districtResp = districtCodeFullService.queryDistrictCodeFullByDistrictCode(StringUtils.substring(idNumber, 0, 6));
                if (districtResp != null && districtResp.getDistrictCodeFullVo() != null) {
                    DistrictVo provinceVo = new DistrictVo();
                    provinceVo.setName(districtResp.getDistrictCodeFullVo().getProvinceName());
                    provinceVo.setCode(districtResp.getDistrictCodeFullVo().getProvinceCode());
                    DistrictVo cityVo = new DistrictVo();
                    cityVo.setCode(String.valueOf(districtResp.getDistrictCodeFullVo().getProvinceCode() + districtResp.getDistrictCodeFullVo().getCityCode()));
                    cityVo.setName(districtResp.getDistrictCodeFullVo().getCityName());
                    DistrictVo countyVo = new DistrictVo();
                    countyVo.setName(districtResp.getDistrictCodeFullVo().getDistrictName());
                    countyVo.setCode(districtResp.getDistrictCodeFullVo().getDistrictCode());
                    areaVo.setProvince(provinceVo);
                    areaVo.setCity(cityVo);
                    areaVo.setCounty(countyVo);
                }
                response.setResult(areaVo);
            }
        } catch (Exception e) {
            logger.warn("queryDistrictByIdCard error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.createUserQRCode", description = "根据跳转地址、key、头像生成二维码")
    public Response<String> createUserQRCode(String url, String key, String img) {

        Response<String> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
                resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            Map<String, Object> map = new HashMap<>();
            map.put("relationId", partyId);
            map.put("type", User.class.getSimpleName());
            map.put("ekey", key);
            List<AppPropExtend> list = propExtendManager.queryChannel(map);
            if (StringUtils.isEmpty(img)) { //如果img为空，获取用户的头像，用户头像不存在就取默认的图片
                User user = userManager.queryUser(partyId);
                if (user != null && StringUtils.isNotEmpty(user.getHeadImg())) {
                    img = user.getHeadImg();
                } else {  //默认的二维码图片
                    img = SysConfigUtil.getStr("gold_qr_code_img");
                }
            } else {
                img = SysConfigUtil.getStr(img);
            }
            if (list.size() > 0) {
                if (StringUtils.isNotEmpty(list.get(0).getValue())) {
                    resp.setResult(list.get(0).getValue());
                } else {
                    String qrCode = createQrCode(SysConfigUtil.getStr(url) + "?partyId=" + partyId, img, true);
                    propExtendManager.update(AppPropExtend.builder().ekey(key).value(qrCode).deviceId(deviceId).type(User.class.getSimpleName()).relationId(partyId).build());
                    resp.setResult(qrCode);
                }
            } else {
                String aa = SysConfigUtil.getStr(url) + "?partyId=" + partyId;
                String qrCode = createQrCode(aa, img, true);
                propExtendManager.insertExceptPropExtend(AppPropExtend.builder().ekey(key).value(qrCode).deviceId(deviceId).type(User.class.getSimpleName()).relationId(partyId).build());
                resp.setResult(qrCode);
            }
        } catch (Exception e) {
            logger.warn("createUserQRCode error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.queryUserByPhone", description = "根据手机号查询用户信息")
    public Response<UserVo> queryUserByPhone(String phone) {

        Response<UserVo> resp = ResponseUtils.successResponse();
        try {
            if (StringUtils.isEmpty(phone)) {
                resp.setFacade(FacadeEnums.E_C_VALDATE_PROPERTY_VAL);
                return resp;
            }
            User u = userManager.getUserByLoginAccount(phone);
            if (u == null) {
                logger.info("get user null");
                return resp;
            }
            if (StringUtils.contains(u.getHeadImg(), "(null)") || StringUtils.isEmpty(u.getHeadImg())) {
                u.setHeadImg(SysConfigUtil.getStr("default_user_headimg"));
            }
            UserVo userVo = convertUserVo(u);
            resp.setResult(userVo);
        } catch (Exception e) {
            logger.warn("queryPersonal error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 查用户的关注数是否超过10人、是否发过微博、是否签到、是否晒收入
     *
     * @return
     */
    @Override
    public Response<Map<String, Object>> queryUserSignAndSubjectAndFollow(Long partyId) {

        Response<Map<String, Object>> resp = ResponseUtils.successResponse();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("isFollow", false);
            map.put("isPublishArticle", false);
            map.put("isSign", false);
            map.put("isShareIncome", false);
            User user = userManager.queryUser(partyId);
            if (user != null) {
                //查关注数
                // 查询我的关注数
                Integer followCount = userRelationManager.queryRelationCount(partyId,Constant.RelationStatus.FOLLOW.name());
                if (followCount > 10) {
                    map.put("isFollow", true);
                }
                //查微博数
                // 查询动态数
                Map<String, Object> paras = new HashMap<String, Object>();
                paras.put("partyId", partyId);
                paras.put("articleType", Constant.ArticleType.SUBJECT.name());
                Integer queryArticleCount = articleManager.queryArticleCount(paras);
                if (queryArticleCount != null && queryArticleCount > 0) {
                    map.put("isPublishArticle", true);
                }
                //查今天是否签到
                String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
                AppActivityTask task = AppActivityTask.builder()
                        .partyId(partyId).daily(today).taskCode(Constant.ActivityTaskType.SIGN.name())
                        .activityCode(GoldService.ACTIVITY_SIGN).taskStatus("Y").useStatus("Y").build();
                List<AppActivityTask> list = activityManager.queryActivityTask(task, null);
                if (list.size() > 0) {
                    map.put("isSign", true);
                }
                //查是否晒收入
                Map<String, Object> param = new HashMap<>();
                param.put("partyId", partyId);
                param.put("activityCode", GoldService.ACTIVITY_REWARD);
                param.put("taskCode", Constant.ActivityTaskType.SHARE_INCOME.name());
                param.put("daily", today);
                List<AppActivityTask> taskList = activityManager.queryActivityTaskByParas(param);
                if (taskList.size() > 0) {
                    map.put("isShareIncome", true);
                }
            }
            resp.setResult(map);
        } catch (Exception e) {
            logger.warn("queryUserSignAndSubjectAndFollow error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.queryNewArticleUser", description = "查询最新发布动态的三个用户")
    public Response<List<UserVo>> queryNewArticleUser() {
        Response<List<UserVo>> resp = ResponseUtils.successResponse();
        List<UserVo> userVos = new ArrayList<>();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            User user = null;
            if (partyId != null) {
                user = userManager.queryUser(partyId);
            }
            //根据用户的家乡地址查询最新发布微博的用户，并且是用户没有删除过微博
            Map<String, Object> param = new HashMap<>();
            if (user != null && StringUtils.isNotEmpty(user.getCity())) {
                param.put("city", user.getCity());
                param.put("province", user.getProvince());
            }
            int startPage = 1;
            param.put("startPage", startPage);
            param.put("pageSize", 10);
            param.put("excludePartyId", partyId);
            param.put("orderBy", "CREATE_TIME DESC");
            Set<Long> partyIds = queryThreeUserArticle(param);
            List<User> users = null;
            if (partyIds.size() > 0) {
                users = userManager.selectUser(new ArrayList<>(partyIds));
            }
            if (users == null) {  //当家乡地址没有发微博的用户取全站的
                param.remove("city");
                param.remove("province");
                partyIds = queryThreeUserArticle(param);
                users = userManager.selectUser(new ArrayList<>(partyIds));
            }
            userVos = users.stream()
                    .map(input -> {
                        if (StringUtils.isEmpty(input.getHeadImg())) {
                            input.setHeadImg(SysConfigUtil.getStr("default_user_headimg"));
                        }
                        return convertUserVo(input);
                    })
                    .collect(Collectors.toList());
            resp.setResult(userVos);
        } catch (Exception e) {
            logger.warn("queryNewArticleUser error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.queryUserQRCode", description = "查询当前用户的二维码")
    public Response<String> queryUserQRCode() {
        Response<String> resp = ResponseUtils.successResponse();
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
        User user = userManager.queryUser(partyId);
        String img = "";
        if (user != null && StringUtils.isNotEmpty(user.getHeadImg())) {
            img = user.getHeadImg();
        } else {
            img = SysConfigUtil.getStr("gold_qr_code_img");
        }
        String url = "APP:USER:" + partyId;
        url = Base64.getEncoder().encodeToString(url.getBytes());
        String code = queryUserQRCode(partyId, img, User.class.getSimpleName(), MapVo.USER_QRCODE, "XL:" + url, deviceId);
        resp.setResult(code);
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.queryPersonalConfig", description = "查询推送配置")
    public Response<String> queryPersonalConfig(String config) {
        return responseCacheUtils.execute(() -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            return propExtendManager.queryAndInit(AppPropExtend.builder().relationId(partyId).deviceId(deviceId)
                    .type(EnumKeyValue.User.class.getSimpleName())
                    .ekey(EnumKeyValue.User.PUSH_SWITCH.name())
                    .value(UserEnum.pushType.OPEN.name()).build()).getValue();
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.PersonalService.updatePersonalConfig", description = "设置推送配置")
    public Response<String> updatePersonalConfig(String config, String value) {
        return responseCacheUtils.execute(() -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            AppPropExtend prop = propExtendManager.queryAndInit(AppPropExtend.builder().relationId(partyId).deviceId(deviceId)
                    .type(EnumKeyValue.User.class.getSimpleName())
                    .ekey(EnumKeyValue.User.PUSH_SWITCH.name())
                    .value(value).build());
            if (!StringUtils.equals(prop.getValue(), value)) {
                propExtendManager.update(AppPropExtend.builder().id(prop.getId()).value(value).build());
            }
            return value;
        });
    }

    /**
     * 二维码查询或创建，不包含推荐码
     *
     * @param partyId
     * @param img
     * @param type
     * @param key
     * @param url
     * @param deviceId
     * @return
     */
    private String queryUserQRCode(Long partyId, String img, String type, String key, String url, String deviceId) {
        Map<String, Object> map = new HashMap<>();
        map.put("relationId", partyId);
        map.put("type", type);
        map.put("ekey", key);
        List<AppPropExtend> list = propExtendManager.queryChannel(map);
        if (list.size() > 0) {
            if (StringUtils.isNotEmpty(list.get(0).getValue())) {
                return list.get(0).getValue();
            } else {
                String qrCode = createQrCode(url, img, false);
                propExtendManager.update(AppPropExtend.builder().ekey(key).value(qrCode).deviceId(deviceId).type(type).relationId(partyId).build());
                return qrCode;
            }
        } else {
            String qrCode = createQrCode(url, img, false);
            propExtendManager.insert(AppPropExtend.builder().ekey(key).value(qrCode).deviceId(deviceId).type(type).relationId(partyId).build());
            return qrCode;
        }
    }

    /**
     * 查询最新发布微博三个用户
     *
     * @param param
     * @return
     */
    private Set<Long> queryThreeUserArticle(Map<String, Object> param) {
        final Set<Long> partyIds = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            if (partyIds.size() >= 3) {
                break;
            }
            param.put("startPage", i + 1);
            List<AppArticle> appArticles = articleManager.queryArticleList(param);
            appArticles.stream().forEach(vo -> {
                partyIds.add(vo.getPartyId());
            });
        }
        return partyIds.stream().limit(3).collect(Collectors.toSet());
    }


    /**
     * 生辰带logo的二维码兵传到服务端
     *
     * @param content
     * @param logoImg
     * @return
     */
    private String createQrCode(String content, String logoImg, Boolean isInvitationCode) {

        if (StringUtils.isEmpty(logoImg)) {
            logoImg = SysConfigUtil.getStr("default_user_headimg");
        }
        File file = null;
        if (isInvitationCode) {
            String invitationCode = "";
            com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> customersDTOResponse = customersInfoService.selectByPartyId(sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class));
            logger.info("===========cif推荐码：[[ {} ]]===========", JSON.toJSONString(customersDTOResponse, true));
            if (customersDTOResponse.isCifSuccess()
                    && customersDTOResponse.getResult() != null
                    && customersDTOResponse.getResult().getInvitationCode() != null) {
                invitationCode = customersDTOResponse.getResult().getInvitationCode();
            }
            file = QRUtils.createQRCode(content, logoImg, String.format(INVITATION_CODE, invitationCode));
        } else {
            file = QRUtils.createQRCode(content, logoImg);
        }
        String jsonBody = HttpUtils.httpImgUpload("https://appfile.xianglin.cn/file/upload", file);
        JSONObject jsonObj = JSON.parseObject(jsonBody);
        String qrUrl = jsonObj.getString(file.getName());
        file.delete();
        return qrUrl;
    }

    /**
     * 更新用户推荐二维码信息
     *
     * @param user
     * @param QrCode
     */
    private void updateUserQrCode(User user, String QrCode) {

        if (StringUtils.isEmpty(user.getHeadImg())) {
            user.setHeadImg(SysConfigUtil.getStr("default_user_headimg"));
        }
        JSONObject json = new JSONObject();
        json.put("headImg", user.getHeadImg());
        json.put("qrCodeImg", QrCode);
        user.setDescs(json.toJSONString());
        userManager.updateUser(user);
    }

    /**
     * 修改家乡
     *
     * @param district
     * @return
     */
    private boolean updateDistrict(AreaVo district) {

        final Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        //参数校验
        checkArgument(district);
        //原始数据
        User user = userManager.queryUser(partyId);
        //更新前数据
        String currentUserDistrict = user.getDistrict();
        AreaVo oldDistrict = null;
        if (!Strings.isNullOrEmpty(currentUserDistrict)) {
            oldDistrict = JSON.parseObject(currentUserDistrict, AreaVo.class);
        }

        //老地址为空，则无需进行退群操作
        if (oldDistrict == null || Strings.isNullOrEmpty(oldDistrict.selectCode(oldDistrict))) {
            //更新用户
            updateUser(district, partyId);
            return true;
        }
        //最低一级区域编码
        String lowestCode = district.selectCode(district);
        String oldLowestCode = oldDistrict.selectCode(oldDistrict);
        //是否需要修改
        if (oldLowestCode.equals(lowestCode)) {
            logger.info("===========前后数据一致，无需修改[[ {} ]]===========", district);
            return false;
        }
        //老地址为空，则无需进行退群操作
        if (Strings.isNullOrEmpty(oldLowestCode)) {
            //更新用户
            updateUser(district, partyId);
            return true;
        }

        final Map<String, Object> groupQueryParams = Maps.newHashMap();
        //处理村务
        AppGroup villageAppGroup = processVillageGroup(groupQueryParams, partyId, oldLowestCode);
        //是否是村群管理员
        isGroupManager(partyId, oldLowestCode);
        //更新用户
        updateUser(district, partyId);

        //同步用户默认的村
        indexService.queryDefaultVillage();

        //村群id
        groupQueryParams.clear();
        groupQueryParams.put("districtCode", oldLowestCode);
        groupQueryParams.put("groupType", Constant.GroupType.G.name());
        groupQueryParams.put("createType", Constant.CreateType.SYSTEM.name());
        List<AppGroup> villageGroups = this.groupManager.queryGroup(groupQueryParams);

        Preconditions.checkState(villageGroups.size() < 2, String.format("一个区域编码存在多个村群：%s", oldLowestCode));

        if (villageGroups.isEmpty()) {
            return true;
        }

        if (villageAppGroup != null) {
            villageGroups.add(villageAppGroup);
        }

        for (AppGroup input : villageGroups) {
            groupQueryParams.clear();
            groupQueryParams.put("groupId", input.getId());
            groupQueryParams.put("partyId", partyId);
            List<AppGroupMember> appGroupMembers = this.groupManager.queryMembers(groupQueryParams);
            //Preconditions.checkState(appGroupMembers.size() == 1, "同一个群有多个相同partyId的成员");
            if (appGroupMembers.size() > 0) {
                RyUtil.quit(partyId, input.getRyGroupId()); //删除融云群成员
                this.groupManager.deleteMember(appGroupMembers.get(0).getId()); //删除群成员
            }
        }

        if (!villageGroups.isEmpty()) {
            //村群成员只有一个时，解散群
            groupQueryParams.clear();
            groupQueryParams.put("groupId", villageGroups.get(0).getId());
            List<AppGroupMember> appGroupMembers = this.groupManager.queryMembers(groupQueryParams);
            //删除成员 解散群
            if (appGroupMembers.size() <= 1) {
                if (appGroupMembers.size() == 1) {
                    this.groupManager.deleteMember(appGroupMembers.get(0).getId());  //删除群成员
                }
                RyUtil.dismiss(partyId, villageGroups.get(0).getRyGroupId()); //解散融云群
                AppGroup delGroup = AppGroup.builder()
                        .id(villageGroups.get(0).getId())
                        .isDeleted("Y")
                        .build();
                this.groupManager.updateGroup(delGroup); //删除群
            }
        }
        return true;
    }

    /**
     * 更新用户家乡
     *
     * @param district
     * @param partyId
     */
    private void updateUser(AreaVo district, Long partyId) {

        //更新用户家乡
        User user = userManager.queryUser(partyId);
        User updateUserInfo = new User();
        updateUserInfo.setId(user.getId());

        updateUserInfo.setDistrict(district.toString());
        updateUserInfo.setProvince(district.getProvinceName());
        updateUserInfo.setCity(district.getCityName());
        updateUserInfo.setCounty(district.getCountyName());
        updateUserInfo.setTown(district.getTownName());
        updateUserInfo.setVillage(district.getVillageName());

        updateUserInfo.setUpdateTime(new Date());
        userManager.updateUser(updateUserInfo);
    }

    /**
     * 是否是村群管理员
     *
     * @param partyId
     * @param oldLowestCode
     */
    private void isGroupManager(Long partyId, String oldLowestCode) {

        HashMap<String, Object> groupQueryParams = Maps.newHashMap();
        //是否是村群主
        groupQueryParams.clear();
        groupQueryParams.put("districtCode", oldLowestCode);
        groupQueryParams.put("managePartyId", partyId);
        groupQueryParams.put("groupType", Constant.GroupType.G.name());
        groupQueryParams.put("createType", Constant.CreateType.SYSTEM.name());
        List<AppGroup> oldGroups = groupManager.queryGroup(groupQueryParams);


        //群主无法修改
        if (!oldGroups.isEmpty()) {
            AppGroup appGroup = oldGroups.get(0);

            //村群只剩一个人时，可以修改家乡地址
            groupQueryParams.clear();
            groupQueryParams.put("groupId", appGroup.getId());
            List<AppGroupMember> appGroupMembers = groupManager.queryMembers(groupQueryParams);

            if (appGroupMembers.size() > 2) {
                logger.info("===========村群管理员，无法修改家乡：[[ {} ]]===========", partyId);
                throw new UnsupportedOperationException("村群管理员，无法修改家乡");
            }

        }
    }

    private AppGroup processVillageGroup(Map<String, Object> groupQueryParams, Long partyId, String oldLowestCode) {

        //是否为开通村务的村管理员
        groupQueryParams.clear();
        groupQueryParams.put("districtCode", oldLowestCode);
        groupQueryParams.put("groupType", Constant.GroupType.V.name());
        groupQueryParams.put("createType", Constant.CreateType.SYSTEM.name());
        List<AppGroup> oldVillageGroups = groupManager.queryGroup(groupQueryParams); //当前村的村务群

        Preconditions.checkState(oldVillageGroups.size() < 2, "存在多个相同districtCode的村务");

        if (oldVillageGroups.isEmpty()) {
            return null;
        }

        groupQueryParams.clear();
        groupQueryParams.put("groupId", oldVillageGroups.get(0).getId());
        groupQueryParams.put("type", Constant.MemberType.MANAGER.name());
        groupQueryParams.put("partyId", partyId);
        List<AppGroupMember> appManagerMembers = groupManager.queryMembers(groupQueryParams); //当前用户是否是管理员

        groupQueryParams.put("partyId", null);
        groupQueryParams.put("type", null);
        List<AppGroupMember> allMembers = groupManager.queryMembers(groupQueryParams);  //村务群总人数

        //管理员无法修改家乡
        if (!appManagerMembers.isEmpty() && allMembers.size() > 1) {
            logger.info("===========村务管理员，无法修改家乡：[[ {} ]]===========", partyId);
            throw new UnsupportedOperationException("管理员无法修改自己的家乡");
        }

        //村务只有管理员一个人,且
        if (!appManagerMembers.isEmpty() && allMembers.size() == 1) {
            logger.info("===========村务中只有村管理员一人：[[ {} ]]===========", partyId);
            //解散融云群
            //RyUtil.dismiss(partyId, oldVillageGroups.get(0).getRyGroupId());
            //删除群成员
            groupManager.deleteMember(appManagerMembers.get(0).getId());
            //更新群
            groupManager.updateGroup(
                    AppGroup
                            .builder()
                            .id(oldVillageGroups.get(0).getId())
                            .managePartyId(0L)
                            .isDeleted("Y")
                            .build()
            );
            return null;
        }
        return oldVillageGroups.get(0);
    }

    private void checkArgument(AreaVo district) {

        Preconditions.checkArgument(district != null, "AreaVo不能为空");
        Preconditions.checkArgument(district.getProvince() != null, "省级区域不能为空");
        Preconditions.checkArgument(district.getCity() != null, "市级区域不能为空");
        Preconditions.checkArgument(district.getCounty() != null, "县级区域不能为空");

    }
}
