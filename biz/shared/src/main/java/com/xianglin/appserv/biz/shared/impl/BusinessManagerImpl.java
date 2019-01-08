/**
 *
 */
package com.xianglin.appserv.biz.shared.impl;

import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.biz.shared.BusinessManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.util.LoginAttrUtil;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.AppMobileService;
import com.xianglin.appserv.common.service.facade.model.BusinessDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.vo.AppMobileVo;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.appserv.core.model.BaseUrl;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.model.CustomersDTO;
import com.xianglin.cif.common.service.facade.model.RoleDTO;
import com.xianglin.fala.session.Session;
import com.xianglin.juhe.common.service.integration.JuheServiceClient;
import com.xianglin.merchant.common.service.facade.MerchantServiceFacade;
import com.xianglin.merchant.common.service.facade.dto.MerchantPartyIdBindDTO;
import com.xianglin.merchant.common.service.facade.dto.ResponseDTO;
import com.xianglin.merchant.common.service.facade.dto.SysUserRoleInfoDTO;
import com.xianglin.te.common.service.facade.enums.Constants.FacadeEnums;
import com.xianglin.xlnodecore.common.service.facade.NodeBusinessService;
import com.xianglin.xlnodecore.common.service.facade.enums.BusinessStatusEnums;
import com.xianglin.xlnodecore.common.service.facade.enums.BusinessTypeEnums;
import com.xianglin.xlnodecore.common.service.facade.req.NodeBusinessListReq;
import com.xianglin.xlnodecore.common.service.facade.resp.NodeBusinessListResp;
import com.xianglin.xlnodecore.common.service.facade.vo.NodeBusinessExtVo;
import com.xianglin.xlnodecore.common.service.integration.VendorServiceClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author zhangyong 2016年8月30日下午2:15:44
 */
public class BusinessManagerImpl implements BusinessManager {

    private final static Logger logger = LoggerFactory.getLogger(BusinessManagerImpl.class);

    private SessionHelper sessionHelper;
    @Autowired
    private NodeBusinessService nodeBusinessService;

    @Autowired
    private AppMobileService appMobileService;
    @Autowired
    private LoginAttrUtil loginAttrUtil;

    @Autowired
    private JuheServiceClient juheServiceClient;
    @Autowired
    private VendorServiceClient vendorServiceClient;

    @Autowired
    private MerchantServiceFacade merchantServiceFacade;

    @Autowired
    private CustomersInfoService customersInfoService;

    @Autowired
    private UserManager userManager;

    @Override
    public List<BusinessDTO> getBusiList(String userType) {
        List<BusinessDTO> list = new ArrayList<>();
        try {
            Session session = sessionHelper.getSession();
            list = new ArrayList<>();
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Set<String> set = new HashSet<>();
            if (partyId != null && partyId != 0) {
                set.add(Constant.BusinessType.LOGIN.name());
//                Map<String, String> map = new HashMap<>();
//                if (personVo != null && !Constant.UserType.visitor.name().equals(userType)) {
//                    if (hasLoanRecord(personVo.getRegMobilePhone(), personVo.getPartyId(), vlevel)) {
//                        list.add(getLiveBusinessDto(nodePartyId, Constant.BusinessType.LOAN.name(), true));
//                        set.add(Constant.BusinessType.LOAN.name());
//                    }
//                }
                userType = userManager.queryUser(partyId).getUserType();
                if (Constant.UserType.nodeManager.name().equals(userType)) {
//                    // 2016/12/7 查询是否是开通借款页面，如果没有开通，查询是否有过借款记录
//                    NodeBusinessListReq req = new NodeBusinessListReq();
//                    req.setNodePartyId(nodePartyId);
//                    NodeBusinessListResp resp = nodeBusinessService.queryNodeBusinessList(req);
//                    logger.info("【查询基业开通业务，nodePartyId:{}返回code：{}】", nodePartyId, resp.getCode());
//                    if (FacadeEnums.OK.code.equals(String.valueOf(resp.getCode()))) {
//                        List<NodeBusinessExtVo> businessVos = resp.getVoList();
//                        list = new ArrayList<>(businessVos.size());
//                        for (NodeBusinessExtVo nodeBusinessExtVo : businessVos) {
//                            logger.debug("【已有的业务:{},业务状态:{}】", nodeBusinessExtVo.getBusinessType(), nodeBusinessExtVo.getBusinessStatus());
//                            String busiType = convertBusiTypeToAppServ(nodeBusinessExtVo.getBusinessType());
//                            String busiStatus = convertBusiStatusToAppServ(nodeBusinessExtVo.getBusinessStatus());
//                            map.put(busiType, busiStatus);
//                        }
//                        String busiStatus = map.get(Constant.BusinessType.BANK.name());
//                        if (busiStatus != null) {
//                            if (Constant.BusinessStatus.OPENING.name().equals(busiStatus)
//                                    || Constant.BusinessStatus.SIGNED.name().equals(busiStatus)) {
//                                list.add(getLiveBusinessDto(nodePartyId, Constant.BusinessType.BANK.name(), true));
//                            }
//                        }
//                        busiStatus = map.get(Constant.BusinessType.LOAN.name());
//                        if (busiStatus != null) {
//                            if (Constant.BusinessStatus.OPENING.name().equals(busiStatus)
//                                    || Constant.BusinessStatus.SIGNED.name().equals(busiStatus) && !set.contains(Constant.BusinessType.LOAN.name())) {
//                                list.add(getLiveBusinessDto(nodePartyId, Constant.BusinessType.LOAN.name(), true));
//                                set.add(Constant.BusinessType.LOAN.name());
//                            }
//
//                        }
//                    }
                    set.add(Constant.BusinessType.BANK.name());
                    set.add(Constant.BusinessType.LOAN.name());
                    //业务开通和有过业务是两回事，如果业务未开通有过业务操作记录，则显示
                    set.add(Constant.BusinessType.NODEMANAGER.name());
                } else {
                    set.add(Constant.BusinessType.USER.name());
                }
                set.addAll(queryMerchantRoles(sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class)));
                set.add(queryXlLoanRoles(sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class)));
                set.add(queryLoan(sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class)));
                set.addAll(queryContract(sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class)));
                set.add(queryInrest(sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class)));

                if (isOpenjiaofei()) {
                    list.add(getLiveBusinessDto(partyId, Constant.BusinessType.LIVE.name(), true));
                    set.add(Constant.BusinessType.LIVE.name());
                }
//                list.add(getLiveBusinessDto(partyId, Constant.BusinessType.ESHOP.name(), true));
//                list.add(getLiveBusinessDto(partyId, Constant.BusinessType.MOBILERECHARGE.name(), true));

//                if (guangfugShow()) {
//                    list.add(getLiveBusinessDto(partyId, Constant.BusinessType.SOLAR.name(), true));
//                    set.add(Constant.BusinessType.SOLAR.name());
//                }
                session.setAttribute(SessionConstants.BUSINESS_INFO, list);
            } else {
                set.add(Constant.BusinessType.VISITOR.name());
            }
            session.setAttribute(SessionConstants.BUSINESS_OPEN_INFO, set);
            sessionHelper.saveSession(session);
        } catch (Exception e) {
            logger.warn("getBusiList", e);
        }

        return list;
    }

    private Set<String> queryContract(Long partyId) {
        Set<String> roles = new HashSet<>();
        try {
            logger.info("queryContract resp {}", ToStringBuilder.reflectionToString(partyId));
            com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> customersDTOResponse = customersInfoService.selectByPartyId(partyId);
            logger.info("queryContract customersDTOResponse {}", customersDTOResponse);
            if (customersDTOResponse.getResult() != null && customersDTOResponse.getResult().getBusinessDTOes() != null) {
                for (com.xianglin.cif.common.service.facade.model.BusinessDTO businessDTO : customersDTOResponse.getResult().getBusinessDTOes()) {
                    roles.add(businessDTO.getBusinessCode());
                }
                for(RoleDTO role: customersDTOResponse.getResult().getRoleDTOs()){
                    roles.add(role.getRoleCode());
                }
            }
        } catch (Exception e) {
            logger.warn("queryContract", e);
        }
        return roles;
    }


    /**
     * 查询用户乡邻助贷权限
     *
     * @param partyId
     * @return
     */
    private String queryXlLoanRoles(Long partyId) {
        String result = "";
        try {
            if (partyId != null && partyId != 0) {
                User u = userManager.queryUser(partyId);
                String whiteList = SysConfigUtil.getStr("xlloan_white_list");//判断手机号匹配
                if (u != null && whiteList != null) {
                    boolean flag = StringUtils.contains(whiteList, u.getLoginName());
                    if (flag) {
                        result = Constant.BusinessType.XL_LOAN.name();
                    }
                }
                if (StringUtils.isEmpty(result)) {//判断身份证匹配
                    com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> cifResp = customersInfoService.selectByPartyId(partyId);
                    String idList = SysConfigUtil.getStr("xlloan_white_list_ID");
                    String ID = cifResp.getResult().getCredentialsNumber();
                    if (StringUtils.isNotEmpty(idList) && StringUtils.isNotEmpty(ID)) {
                        String[] ids = idList.split(",");
                        for (String reg : ids) {
                            if (StringUtils.startsWith(ID, reg)) {
                                result = Constant.BusinessType.XL_LOAN.name();
                                break;
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            logger.warn("queryXlLoanRoles", e);
        }
        return result;
    }

    /**
     * 查询用户秒息宝权限
     *
     * @param partyId
     * @return
     */
    private String queryInrest(Long partyId) {
        String result = "";
        try {
            if (partyId != null && partyId != 0) {
                User u = userManager.queryUser(partyId);
                String whiteList = SysConfigUtil.getStr("interest_white_list");//判断手机号匹配
                if (u != null) {
                    if (StringUtils.isNotEmpty(whiteList)) {
                        boolean flag = StringUtils.contains(whiteList, u.getLoginName());
                        if (flag) {
                            result = Constant.BusinessType.INTEREST.name();
                        }
                    } else {
                        result = Constant.BusinessType.INTEREST.name();
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("queryInrest", e);
        }
        return result;
    }

    /**
     * 查询是否有我的接口入口
     *
     * @param partyId
     * @return
     */
    private String queryLoan(Long partyId) {
        String result = "";
        try {
            if (partyId != null && partyId != 0) {
                String whiteList = SysConfigUtil.getStr("cash.packet.rank1");
                if (StringUtils.isNotEmpty(whiteList)) {
                    boolean flag = StringUtils.contains(whiteList, partyId + "");
                    if (flag) {
                        result = Constant.BusinessType.LOAN.name();
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("queryXlLoanRoles", e);
        }
        return result;
    }


    /**
     * 查询乡邻账房角色
     *
     * @param partyId
     * @return
     */
    private Set<String> queryMerchantRoles(Long partyId) {
        //查询乡邻账房角色
        Set<String> roles = new HashSet<>(2);
        try {
            if (partyId == null || partyId == 0) {
                return roles;
            }
            MerchantPartyIdBindDTO dto = new MerchantPartyIdBindDTO();
            dto.setPartyId(partyId + "");
            dto.setPhone(userManager.queryUser(partyId).getLoginName());
            ResponseDTO<Object> resp = merchantServiceFacade.bindMerchantPartyId(dto);
            logger.info("bind merchant resp {}", ToStringBuilder.reflectionToString(resp));

            dto = new MerchantPartyIdBindDTO();
            dto.setPartyId(sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class) + "");
            ResponseDTO<List<SysUserRoleInfoDTO>> mResp = merchantServiceFacade.listRoleByPartyId(dto);
            logger.info("merchantServiceFacade.listRoleByPartyId mResp {}", mResp);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(mResp.getResult())) {
                for (SysUserRoleInfoDTO roleInfo : mResp.getResult()) {
                    switch (roleInfo.getRoName()) {
                        case "SHOPKEEPER":
                            roles.add(Constant.BusinessType.MERCHANT.name());
                            break;
                        case "SALESMAN":
                            roles.add(Constant.BusinessType.MERCHANT.name());
                            break;
                        case "EXHIBITION":
                            roles.add(Constant.BusinessType.MERCHANT_BUSI.name());
                            break;
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("queryMerchantRoles", e);
        }
        return roles;
    }

    private boolean guangfugShow() {
        try {
            Long partyId = loginAttrUtil.getPartyId();
            String mobile = loginAttrUtil.getSessionStr(SessionConstants.LOGIN_NAME);
            logger.info("======party:{},mobile:{}======", partyId, mobile);
            if (mobile == null) {//Person表中手机号为空时，false
                return false;
            }
            String areaCode = getAreaCodeByMobile(mobile, "" + partyId);
            return vendorServiceClient.queryVendorIsExistByAreaCode(areaCode);
        } catch (Exception e) {
            logger.warn("查询光伏开通信息错误：", e);
            return false;
        }
    }


    private String getAreaCodeByMobile(String mobile, String creator) {
        String areaCode = null;
        AppMobileVo vo = new AppMobileVo();
        vo.setMobile(mobile);
        Response<AppMobileVo> response = appMobileService.getAppMobile(vo);
        Date now = DateUtils.getNow();
        if (com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.OK.code == response.getCode()) {
            AppMobileVo result = response.getResult();
            if (DateUtils.getIntervalDays(result.getUpdateTime(), now) > 365) {//每年更新一次
                JSONObject js = juheServiceClient.getMobileInfo(mobile);
                result.setUpdater(creator);
                result.setUpdateTime(now);
                wrapMobileInfo(js, result);
                appMobileService.updateAppMobile(result);
            }
            areaCode = result.getAreacode();
        } else {
            AppMobileVo newVo = new AppMobileVo();
            newVo.setCreator(creator);
            newVo.setCreateTime(now);
            newVo.setMobile(mobile);
            newVo.setUpdateTime(now);
            newVo.setUpdater(creator);
            JSONObject js = juheServiceClient.getMobileInfo(mobile);
            wrapMobileInfo(js, newVo);
            areaCode = newVo.getAreacode();

            appMobileService.insertAppMobile(newVo);
        }
        return areaCode;
    }

    private void wrapMobileInfo(JSONObject js, AppMobileVo vo) {
        if (js != null) {
            vo.setIsDeleted("0");
            vo.setAreacode(js.getString("areacode"));
            vo.setProvince(js.getString("province"));
            vo.setCard(js.getString("card"));
            vo.setCity(js.getString("city"));
            vo.setCompany(js.getString("company"));
        }

    }

    private boolean hasLoanRecord(String mobilePhone, Long partyId, String vlevel) {

        try {
//            if (BusinessConstants.PartyLevel.V0.name().equals(vlevel) || StringUtils.isEmpty(vlevel)) {
//                //根据手机号查询贷款信息
//                return loanServiceClient.getCustApplyList(mobilePhone, null);
//            } else {
//                String idNumber = getIdNumber(partyId);
//                //根据身份证号查询
//                return loanServiceClient.getCustApplyList(null, idNumber);
//            }
            return false;
        } catch (Exception e) {
            logger.warn("查询借款信息错误", e);
            return false;
        }

    }

    private String getIdNumber(Long partyId) {
        return customersInfoService.selectCustomsAlready2Auth(partyId).getResult().getCustomerName();
    }

    /**
     * @see com.xianglin.appserv.biz.shared.BusinessManager#getBusinessList(java.lang.Long)
     */
    @Override
    public List<BusinessDTO> getBusinessList(Long nodePartyId) {
        Session session = sessionHelper.getSession();
        List<BusinessDTO> list = new ArrayList<>();

        nodePartyId = loginAttrUtil.getNodePartyId();
        if (nodePartyId == null) {
            return list;
        }
        NodeBusinessListReq req = new NodeBusinessListReq();
        req.setNodePartyId(nodePartyId);
        NodeBusinessListResp resp = nodeBusinessService.queryNodeBusinessList(req);

        logger.info("【查询基业开通业务，nodePartyId:{}返回code：{}】", nodePartyId, resp.getCode());

        if (FacadeEnums.OK.code.equals(String.valueOf(resp.getCode()))) {
            List<NodeBusinessExtVo> businessVos = resp.getVoList();
            list = new ArrayList<>(businessVos.size());
            for (NodeBusinessExtVo nodeBusinessExtVo : businessVos) {
                logger.debug("【已有的业务:{},业务状态:{}】", nodeBusinessExtVo.getBusinessType(), nodeBusinessExtVo.getBusinessStatus());
                setBusinessDTO(nodeBusinessExtVo, list);
            }

            list.add(getLiveBusinessDto(nodePartyId, Constant.BusinessType.LIVE.name(), true));
            list.add(getLiveBusinessDto(nodePartyId, Constant.BusinessType.MOBILERECHARGE.name(), true));
        }


        session.setAttribute(SessionConstants.BUSINESS_INFO, list);
        sessionHelper.saveSession(session);
        return list;
    }

    private BusinessDTO convertBusinessDTO(NodeBusinessExtVo extVo) {
        BusinessDTO dto = new BusinessDTO();

        dto.setBusinessStatus(convertBusiStatusToAppServ(extVo.getBusinessStatus()));
        dto.setBusinessType(convertBusiTypeToAppServ(extVo.getBusinessType()));
        dto.setId(extVo.getId());
        dto.setNodePartyId(extVo.getNodePartyId().toString());
        if (Constant.BusinessStatus.OPENING.name().equals(dto.getBusinessStatus())
                || Constant.BusinessStatus.SIGNED.name().equals(dto.getBusinessStatus())) {
            String key = dto.getBusinessType().concat(BaseUrl.BUSINESS_STUFFIX);
            dto.setH5url(SysConfigUtil.getStr(key, null));
        } else {
            dto.setH5url(SysConfigUtil.getStr(dto.getBusinessType().concat(BaseUrl.BUSINESS_CLOSE_STUFFIX), null));
        }
        return dto;
    }

    private void setBusinessDTO(NodeBusinessExtVo extVo, List<BusinessDTO> list) {
        BusinessDTO dto = new BusinessDTO();

        dto.setBusinessStatus(convertBusiStatusToAppServ(extVo.getBusinessStatus()));
        dto.setBusinessType(convertBusiTypeToAppServ(extVo.getBusinessType()));
        dto.setId(extVo.getId());
        dto.setNodePartyId(extVo.getNodePartyId().toString());
        if (Constant.BusinessStatus.OPENING.name().equals(dto.getBusinessStatus())
                || Constant.BusinessStatus.SIGNED.name().equals(dto.getBusinessStatus())) {
            // dto.setH5url(SysConfigUtil.getStr());
            String key = dto.getBusinessType()+BaseUrl.BUSINESS_STUFFIX;
            dto.setH5url(SysConfigUtil.getStr(key, null));
            //  end = PropertiesUtil.getProperty(dto.getBusinessType().concat(BaseUrl.BUSINESS_STUFFIX));
          /*  if (!StringUtils.isEmpty(end)) {
                if (Constant.BusinessType.ESHOP.name().equals(dto.getBusinessType())
                        || Constant.BusinessType.MOBILERECHARGE.name().equals(dto.getBusinessType())) {
                    dto.setH5url(end);
                } else {
                    dto.setH5url(BaseUrl.H5WECHAT_URL + end);
                }
            }*/
        } else {
            dto.setH5url(SysConfigUtil.getStr(dto.getBusinessType()+BaseUrl.BUSINESS_CLOSE_STUFFIX, null));
        }
        list.add(dto);
    }

    private String convertBusiTypeToAppServ(String businessType) {
        if (BusinessTypeEnums.BANK.msg.equals(businessType)) {
            return Constant.BusinessType.BANK.name();
        } else if (BusinessTypeEnums.ESHOP.msg.equals(businessType)) {
            return Constant.BusinessType.ESHOP.name();
        } else if (BusinessTypeEnums.LOAN.msg.equals(businessType)) {
            return Constant.BusinessType.LOAN.name();
        } else {
            return businessType;
        }
    }

    private String convertBusiStatusToAppServ(String businessStatus) {
        if (BusinessStatusEnums.NONE.msg.equals(businessStatus)) {
            return Constant.BusinessStatus.NONE.name();
        } else if (BusinessStatusEnums.OPENING.msg.equals(businessStatus)) {
            return Constant.BusinessStatus.OPENING.name();
        } else if (BusinessStatusEnums.SIGNED.msg.equals(businessStatus)) {
            return Constant.BusinessStatus.SIGNED.name();
        } else if (BusinessStatusEnums.INVOKED.msg.equals(businessStatus)) {
            return Constant.BusinessStatus.INVOKED.name();
        } else {
            return null;
        }
    }

    private BusinessDTO getLiveBusinessDto(Long nodePartyId, String busiType, Boolean open) {
        BusinessDTO dto = new BusinessDTO();
        dto.setBusinessType(busiType);
        if (open == null || !open) {
            dto.setBusinessStatus(Constant.BusinessStatus.NONE.name());
        } else {
            dto.setBusinessStatus(Constant.BusinessStatus.OPENING.name());
            if (Constant.BusinessType.LIVE.name().equals(busiType)) {
                if (isOpenjiaofei()) {
                    dto.setH5url(SysConfigUtil.getStr(dto.getBusinessType().concat(BaseUrl.BUSINESS_STUFFIX), null));
                } else {
                    dto.setBusinessStatus(Constant.BusinessStatus.NONE.name());
                }
            } else {
                dto.setH5url(SysConfigUtil.getStr(dto.getBusinessType().concat(BaseUrl.BUSINESS_STUFFIX), null));

            }

        }
        if (nodePartyId != null) {
            dto.setNodePartyId(nodePartyId.toString());
        }
        return dto;
    }

    @Override
    public boolean isOpenjiaofei() {
        String partyIds = SysConfigUtil.getStr(Constant.BusiVisitKey.live_pay_party_list.code);//水电煤白名单
        Long partyId = loginAttrUtil.getPartyId();
        if ("ALL".equalsIgnoreCase(partyIds) || StringUtils.isEmpty(partyIds)) {
            return true;
        } else {
            String[] party_list = partyIds.split(",");
            for (String party : party_list) {
                if (party.equals(String.valueOf(partyId))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Set<String> queryUserBusiness() {
        Set<String> busiSet = new HashSet<>();
        try {
            busiSet = sessionHelper.getSessionProp(SessionConstants.BUSINESS_OPEN_INFO, Set.class);
            if (CollectionUtils.isEmpty(busiSet)) {
                getBusiList("");
                busiSet = sessionHelper.getSessionProp(SessionConstants.BUSINESS_OPEN_INFO, Set.class);
                logger.info("user busi info", busiSet);
            }
        } catch (Exception e) {
            logger.warn("queryUserBusiness  ", e);
        }
        return busiSet;
    }

    public SessionHelper getSessionHelper() {
        return sessionHelper;
    }

    public void setSessionHelper(SessionHelper sessionHelper) {
        this.sessionHelper = sessionHelper;
    }

}
