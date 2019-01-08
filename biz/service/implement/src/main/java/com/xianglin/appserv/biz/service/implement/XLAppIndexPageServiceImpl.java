/**
 *
 */
package com.xianglin.appserv.biz.service.implement;

import com.alibaba.fastjson.JSON;
import com.xianglin.appserv.biz.shared.*;
import com.xianglin.appserv.biz.shared.util.LoginAttrUtil;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.service.facade.RedPacketService;
import com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService;
import com.xianglin.appserv.common.service.facade.model.*;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.DeviceType;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.UserType;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.MonthAchieveType;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;
import com.xianglin.appserv.common.service.integration.cif.PartyAttrAccountServiceClient;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.core.model.BaseUrl;
import com.xianglin.appserv.core.service.CoreRedPacketService;
import com.xianglin.cif.common.service.facade.constant.ResponseConstants;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.resp.PartyAttrAccountResp;
import com.xianglin.fala.session.Session;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.xlnodecore.common.service.facade.BankImportService;
import com.xianglin.xlnodecore.common.service.facade.model.AgentDetailDTO;
import com.xianglin.xlnodecore.common.service.facade.req.BankImportReq;
import com.xianglin.xlnodecore.common.service.facade.resp.BankImportListResp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyong 2016年8月12日上午11:19:09
 */
@ServiceInterface
public class XLAppIndexPageServiceImpl implements XLAppIndexPageService {

    private final static Logger logger = LoggerFactory.getLogger(XLAppIndexPageServiceImpl.class);

    private static final String BANER_URL = "BANER_URL";
    private static final String BANER_IMG_URL = "BANER_IMG_URL";
    private static final String DEFAULT_BANER_IMG_URL = "DEFAULT_BANER_IMG_URL";
    private static final String DEFAULT_BANER_URL = "DEFAULT_BANER_URL";

    private static final String TRUE = "true";
    private static final String FALSE = "false";
    /**
     * banner 缓存key
     */
    private static final String BANNER_CACHE_KEY = "app_banner_cache_key";
    private static final String ALL_BANNER_CACHE_KEY = "all_app_banner_cache_key";
    private static final int CACHE_TIMEOUT = 60 * 60;//超时时间

    /**
     * @see com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService#getXLAppHomeData(java.lang.Long)
     */
    /**
     * sessionHelper
     */
    private SessionHelper sessionHelper;
    @Autowired
    private ProfitManager profitManager;

    @Autowired
    private BusinessManager businessManager;
    @Autowired
    private MessageManager appMessageManager;
    @Autowired
    private PartyAttrAccountServiceClient partyAttrAccountServiceClient;
    @Autowired
    private BannerManager bannerManager;
    @Autowired
    private BankImportService bankImportService;

    @Autowired
    private PropertiesFactoryBean config;

    @Autowired
    private UserManager userManager;

    @Autowired
    private CoreRedPacketService coreRedPacketService;

    @Autowired
    private RedPacketService redPacketService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LoginAttrUtil loginAttrUtil;


    @ServiceMethod(alias = "com.xianglin.appserv.biz.service.XLAppIndexPageService.getXLAppHomeData", needLogin = true, description = "移动app首页")
    @Override
    public Response<XLAppHomeDataDTO> getXLAppHomeData(Long userPartyId, Long nodePartyId) {
        Long startTime = System.currentTimeMillis();
        logger.info("getXLAppHomeData userPartyId:{},nodePartyId:{}, timeStamp:{}", userPartyId, nodePartyId, startTime);
        Response<XLAppHomeDataDTO> response = ResponseUtils.successResponse();
        XLAppHomeDataDTO dto = new XLAppHomeDataDTO();
        // 查询baner
	/*	Response<List<BanerDTO>> banerResp = getBanerList();

		if (banerResp.isSuccess()) {
			dto.setBanerDtoList(banerResp.getResult());
		}*/
        dto.setBanerDtoList(getBaners());
        // 查询收益详情
        try {
            ProfitDTO profitDto = profitManager.queryLocalProfit(userPartyId, null, MonthAchieveType.INCOME.name());

            BigDecimal totalAmount = profitManager.queryTotalProfit(userPartyId, null, MonthAchieveType.INCOME.name());
            if (profitDto != null) {
                profitDto.setTotal(NumberUtil.truncateBigDecimal(totalAmount, 2));
            }

            dto.setProfitDto(profitDto);
        } catch (Exception e1) {
            logger.error("查询收益出错", e1);
            // e1.printStackTrace();
        }


        // 查询开通业务
		/*Response<List<BusinessDTO>> busiResp = getBusinessList(nodePartyId);
		if (busiResp.isSuccess()) {
			dto.setBusinessDtoList(busiResp.getResult());
		}*/


        // 查询和该用户相关的公告和消息
        MsgQuery msgQ = new MsgQuery();
        msgQ.setPageSize(2);
        msgQ.setStartPage(1);
        List<MsgVo> msgList;
        boolean activityFlag = false;
        try {
            String business = loginAttrUtil.getSessionStr(SessionConstants.BUSINESS_INFO);
            List<BusinessDTO> list = JSON.parseArray(business, BusinessDTO.class);
            if (!CollectionUtils.isEmpty(list)) {
                dto.setBusinessDtoList(list);
            }
            //Session session = sessionHelper.getSession();
            Long partyId = loginAttrUtil.getPartyId();
            //Long partyId = Long.valueOf(session.getAttribute(SessionConstants.PARTY_ID));
            msgList = appMessageManager.list(msgQ, partyId);
            dto.setMsgVoList(msgList);

            String userType = loginAttrUtil.getSessionStr(SessionConstants.USER_TYPE);
            if (UserType.nodeManager.name().equals(userType)) {//站长才有银行业务
                BankImportReq req = new BankImportReq();
                req.setNodePartyId(Long.valueOf(loginAttrUtil.getNodePartyId()));
                req.setDistrictCode(loginAttrUtil.getSessionStr(SessionConstants.DISTRICT_CODE));
                com.xianglin.xlnodecore.common.service.facade.base.Response<AgentDetailDTO> bankResp = bankImportService
                        .getAgentDetail(req);
                if (bankResp.isSucc()) {
                    dto.setBankBalance(bankResp.getResult().getBalance());
                    dto.setCardCount(bankResp.getResult().getCardCount());
                }

                req.setDistrictCode(loginAttrUtil.getSessionStr(SessionConstants.node_party_id).substring(0, 2));
                BankImportListResp bankImportListResp = bankImportService.queryBankAndEcommerceByNodePartyId(req);
                if (bankImportListResp != null && bankImportListResp.getMap() != null
                        && bankImportListResp.getMap().get("bankBusiness") != null) {
                    if (bankImportListResp.getMap().get("bankBusiness") instanceof Map) {
                        dto.setBankSumDate(
                                (String) ((Map) bankImportListResp.getMap().get("bankBusiness")).get("importDate"));
                    }
                }
            }
            // 红包查询
            Request<RedPacketVo> redreq = new Request<>();
            RedPacketVo reqVo = new RedPacketVo();
            reqVo.setPartyId(partyId);
            reqVo.setType(Constant.RedPacketType.FRESH_RED_PACKET.name());
            redreq.setReq(reqVo);

            if ("true".equals(SysConfigUtil.getStr(Constant.BusiVisitKey.activity_fresh_red_packet_switch.code, "false"))) {
                //1. 查询是否领取过红包
                Response<List<RedPacketVo>> redPacketVo = redPacketService.getRedPacketList(redreq);
                if (FacadeEnums.OK.getCode() == redPacketVo.getCode()) {
                    dto.setIsFlag(TRUE);
                } else if (FacadeEnums.RETURN_EMPTY.getCode() == redPacketVo.getCode()) {
                    dto.setIsFlag(FALSE);
                }
            } else {
                dto.setIsFlag(TRUE);
            }
            //dto.setIsFlag(TRUE);
            // 判断整点抢活动是否开启
            activityFlag = coreRedPacketService.isActivityStart(DateUtils.formatDate(DateUtils.getNow(), DateUtils.DATE_TPT_TWO));
        } catch (Exception e) {
            logger.error("主页数据初始化失败，partyid:{}", nodePartyId, e);
        }

        response.setResult(dto);
        logger.info("getXLAppHomeData start time{}, spend {}", startTime, System.currentTimeMillis() - startTime);
        return response;
    }


    /**
     * @see com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService#getBanerList()
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.biz.service.XLAppIndexPageService.getBanerList", needLogin = false, description = "移动app首页baner")
    public Response<List<BanerDTO>> getBanerList() {

        Response<List<BanerDTO>> response = ResponseUtils.successResponse();
        Session session = sessionHelper.getSession();
        String clientersion = session.getAttribute(SessionConstants.CLIENT_VERSION);
        List<BanerDTO> baners = new ArrayList<>();

        String image_url = SysConfigUtil.getStr(BANER_IMG_URL);
        String[] imageUrls = new String[]{};
        if (StringUtils.isNotEmpty(image_url)) {
            imageUrls = image_url.split(",");
        }

        String baner_url = PropertiesUtil.getProperty(BANER_URL);
        String[] banerUrls = new String[]{};
        if (StringUtils.isNotEmpty(baner_url)) {
            banerUrls = baner_url.split(",");
        }
        for (int i = 0; i < imageUrls.length; i++) {
            BanerDTO banerDto = new BanerDTO();
            String[] baner = banerUrls[i].split(":");
            if ("1".equals(baner[0])) {
                banerDto.setUrl(BaseUrl.H5WECHAT_URL + baner[1]);
            }
            if ("2".equals(baner[0])) {
                banerDto.setUrl(BaseUrl.H5CAU_URL + baner[1]);
            }
            banerDto.setImageUrl(BaseUrl.H5WECHAT_URL + imageUrls[i]);
            if (baner[2].contains("ALL") || (clientersion != null && baner[2].contains(clientersion))) {
                baners.add(banerDto);
            }
        }
        response.setResult(baners);
        return response;
    }

    /**
     * 从数据库获取数据
     *
     * @return
     */
    private List<BanerDTO> getBaners() {

        List<BanerDTO> list;

        Map<String, Object> param = new HashMap<>();
        Session session = sessionHelper.getSession();
        String districtCode = session.getAttribute(SessionConstants.DISTRICT_CODE);
        String clientersion = session.getAttribute(SessionConstants.CLIENT_VERSION);
        if (StringUtils.length(districtCode) >= 4) {
            districtCode = districtCode.substring(0, 4);
        }
        param.put("districtCode", districtCode);
        param.put("supportVersion", clientersion);

        list = bannerManager.getBannerList(param);
        String key = BANNER_CACHE_KEY + clientersion + districtCode;
        param.put("districtCode", districtCode);
        param.put("supportVersion", clientersion);
        String cacheBanners = redisUtil.get(key);
		/*if(StringUtils.isNotEmpty(cacheBanners)){
			return JSON.parseArray(cacheBanners,BanerDTO.class);
		}*/
        list = bannerManager.getBannerList(param);
        if (CollectionUtils.isNotEmpty(list)) {
            redisUtil.add(BANNER_CACHE_KEY + districtCode, JSON.toJSONString(list), CACHE_TIMEOUT);
        } else {
            //如果缓存和数据库都为空，显示这个
            list = new ArrayList<>();
            BanerDTO dto = new BanerDTO();
            dto.setImageUrl(PropertiesUtil.getProperty(DEFAULT_BANER_IMG_URL));
            dto.setUrl(PropertiesUtil.getProperty(DEFAULT_BANER_URL));
            list.add(dto);
        }
        return list;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService#getBusinessList(java.lang.Long)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.biz.service.XLAppIndexPageService.getBusinessList", needLogin = true, description = "移动app首页获取业务开通状态")
    public Response<List<BusinessDTO>> getBusinessList(Long nodePartyId) {
        Response<List<BusinessDTO>> response = ResponseUtils.successResponse();
        List<BusinessDTO> list = businessManager.getBusinessList(nodePartyId);
        response.setResult(list);
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService.getMsgList", needLogin = true, description = "移动app首页获取消息列表")
    public Response<List<MsgVo>> getMsgList(Integer startPage, Integer pageSize) {
        Response<List<MsgVo>> resp = ResponseUtils.successResponse();
        MsgQuery msgQ = new MsgQuery();
        if (null != startPage) {
            msgQ.setStartPage(startPage);
        }
        if (null != pageSize) {
            msgQ.setPageSize(pageSize);
        }

        try {
            Session session = sessionHelper.getSession();
            Long partyId = Long.valueOf(session.getAttribute(SessionConstants.PARTY_ID));

            List<MsgVo> msgList = appMessageManager.list(msgQ, partyId);
            resp.setResult(msgList);
        } catch (Exception e) {
            logger.error("查询消息失败", e);
            // resp.setCode(ResponseEnum.DATA_ERROR.getCode());
            // resp.setTips(ResponseEnum.DATA_ERROR.getTips());
            resp.setResult(new ArrayList<MsgVo>());
        }
        return resp;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService#getProfitList(java.lang.Long,
     * java.lang.Integer, java.lang.Integer)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService.getProfitList", needLogin = true, description = "移动app首页收益更多")
    public Response<List<ProfitDTO>> getProfitList(Long partyId, Integer startPage, Integer pageSize) {
        Response<List<ProfitDTO>> resp = ResponseUtils.successResponse();
        List<ProfitDTO> list = profitManager.queryProfitList(partyId, startPage, pageSize);
        if (!CollectionUtils.isEmpty(list)) {
            resp.setResult(list);
        } else {
            logger.debug("收益列表查询为空");
        }
        return resp;
    }

    public static void main(String[] args) {
        String s = "2015";
        String s1 = "201601";
        String a = "false";
        BigDecimal aa = new BigDecimal("0.00");
        BigDecimal bb = new BigDecimal("89.32");

        System.out.println(aa.multiply(bb));
    }

    public SessionHelper getSessionHelper() {
        return sessionHelper;
    }

    public void setSessionHelper(SessionHelper sessionHelper) {
        this.sessionHelper = sessionHelper;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService#getBusiVisitUrlInfo(java.lang.String)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService.getBusiVisitUrlInfo", needLogin = false, description = "业绩与收益排名url")
    public Response<BusiVisitDTO> getBusiVisitUrlInfo(String busiTypeKey) {

        Response<BusiVisitDTO> response = ResponseUtils.successResponse();
        BusiVisitDTO dto = new BusiVisitDTO();
        if (StringUtils.isEmpty(busiTypeKey)) {
            dto.setBankProfitTopUrl(UrlPendingUtil.concatUrl(BaseUrl.H5WECHAT_URL_KEY,
                    Constant.BusiVisitKey.BANK_PROFIT_TOP_URL.name()));
            dto.setBankBalanceUrl(
                    UrlPendingUtil.concatUrl(BaseUrl.H5WECHAT_URL_KEY, Constant.BusiVisitKey.BANK_BALANCE_URL.name()));
            dto.setBankCardNumUrl(
                    UrlPendingUtil.concatUrl(BaseUrl.H5WECHAT_URL_KEY, Constant.BusiVisitKey.BANKCARD_NUM_URL.name()));
            dto.setBankIncrBalanceUrl(UrlPendingUtil.concatUrl(BaseUrl.H5WECHAT_URL_KEY,
                    Constant.BusiVisitKey.BANK_INCRBALANCE_URL.name()));
            dto.setBankPerformanceUrl(UrlPendingUtil.concatUrl(BaseUrl.H5WECHAT_URL_KEY,
                    Constant.BusiVisitKey.BANK_PERFORMANCE_URL.name()));
            dto.setLifePayProfitUrl(UrlPendingUtil.concatUrl(BaseUrl.H5WECHAT_URL_KEY,
                    Constant.BusiVisitKey.LIFE_PAY_PROFIT_URL.name()));
            dto.setTotalProfitUrl(
                    UrlPendingUtil.concatUrl(BaseUrl.H5WECHAT_URL_KEY, Constant.BusiVisitKey.TOTAL_PROFIT_URL.name()));
        } else {
            dto.setDefaultUrl(UrlPendingUtil.concatUrl(BaseUrl.H5WECHAT_URL_KEY, busiTypeKey));
        }
        response.setResult(dto);
        return response;

    }

    /**
     * @see com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService#isExistPartyAttrAccount(java.lang.Long)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService.isExistPartyAttrAccount", description = "资金账户是否开通")
    public Response<Boolean> isExistPartyAttrAccount(Long partyId) {
        Response<Boolean> response = ResponseUtils.successResponse();
        Session session = sessionHelper.getSession();
        Long partyIdSession = null;
        if (partyId == null) {
            partyIdSession = session.getAttribute(SessionConstants.PARTY_ID, Long.class);
            if (partyIdSession == null) {
                logger.warn("get partyId from session is empty !sessionId:{} , nodePartyId :{} ", session.getId(),
                        partyIdSession);
                response.setCode(ResultEnum.SessionStatus.getCode());
                return response;
            }
            partyId = partyIdSession;
        }
        PartyAttrAccountResp resp = partyAttrAccountServiceClient.getPartyAttrAccount(partyId);

        if (ResponseConstants.FacadeEnums.ACCOUNT_IS_EXISTS.code.equals(resp.getCode())) {
            response.setResult(true);
        } else {
            response.setResult(false);
        }

        return response;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService#version(java.lang.String)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService.version", description = "查看app最新版本号")
    public Response<AppVersionVo> version(String deviceType) {
        Response<AppVersionVo> response = ResponseUtils.successResponse();
        try {
            AppVersionVo vo = new AppVersionVo();
            if (StringUtils.equals(deviceType, DeviceType.ANDROID.name())) {
                vo.setVersion(SysConfigUtil.getStr("androidVersion"));
                vo.setDownloadURL(SysConfigUtil.getStr("androidURL"));
                vo.setDesc(SysConfigUtil.getStr("androidDesc"));
                vo.setUpdateLevel(SysConfigUtil.getStr("androidUpdate"));
                vo.setVersionCode(Integer.parseInt(SysConfigUtil.getStr("androidVersionCode")));
            } else {
                vo.setVersion(SysConfigUtil.getStr("iosVersion"));
                vo.setDownloadURL(SysConfigUtil.getStr("iosURL"));
                vo.setDesc(SysConfigUtil.getStr("iosDesc"));
                vo.setUpdateLevel(SysConfigUtil.getStr("iosUpdate"));
                vo.setVersionCode(Integer.parseInt(SysConfigUtil.getStr("iosVersionCode")));
            }
            response.setResult(vo);
        } catch (Exception e) {
            logger.error("version", e);
        }
        return response;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService#submitPushInfo(com.xianglin.appserv.common.service.facade.model.vo.AppPushVo)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService.submitPushInfo", description = "提交推送信息", needLogin = false)
    public Response<Boolean> submitPushInfo(AppPushVo vo) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            if (StringUtils.isNotEmpty(vo.getDeviceId())) {
                Long partyId = null;
                try {
                    Session session = sessionHelper.getSession();
                    session.setAttribute(SessionConstants.CLIENT_VERSION, vo.getVersion());
                    sessionHelper.saveSession(session);
                    partyId = Long.valueOf(session.getAttribute(SessionConstants.PARTY_ID));
                } catch (Exception e) {
                }
                vo.setPartyId(partyId);
                userManager.saveUpdatePush(vo);
                resp.setResult(true);
            }
        } catch (Exception e) {
            resp.setFacade(FacadeEnums.UPDATE_FAIL);
            resp.setResult(false);
            logger.error("submitPushInfo", e);
        }
        return resp;
    }


    /**
     * @see com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService#iosSupportNodeCode()
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService.iosSupportNodeCode", needLogin = false, description = "ios登陆是否开启网点编号登陆")
    public Response<Boolean> iosSupportNodeCode() {
        Response<Boolean> resp = new Response<>(false);

        String isSupportNodeCode = SysConfigUtil.getStr(Constant.BusiVisitKey.app_ios_support_nodecode.code, "false");

        if (StringUtils.isEmpty(isSupportNodeCode)) {
            resp.setResult(false);
        }
        try {
            Boolean support = Boolean.valueOf(isSupportNodeCode);
            resp.setResult(support);
        } catch (Exception e) {
            resp.setResult(false);
        }

        return resp;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService#getTotalSolarData()
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService.getTotalSolarData", needLogin = false, description = "查询光伏总收益")
    public Response<TotalSolarDataVo> getTotalSolarData() {
        Response<TotalSolarDataVo> resp = ResponseUtils.successResponse();
        try {
            resp.setResult(new TotalSolarDataVo());
        } catch (Exception e) {
            resp.setFacade(FacadeEnums.UPDATE_FAIL);
            logger.error("getTotalSolarData", e);
        }
        return resp;
    }

}
