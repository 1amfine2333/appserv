/**
 *
 */
package com.xianglin.appserv.biz.service.implement;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.xianglin.appserv.biz.shared.*;
import com.xianglin.appserv.biz.shared.util.ResponseCacheUtils;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.daointerface.AppPushDAO;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.app.ArticleService;
import com.xianglin.appserv.common.service.facade.app.GoldService;
import com.xianglin.appserv.common.service.facade.app.IndexService;
import com.xianglin.appserv.common.service.facade.model.AppSessionConstants;
import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.UserType;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.enums.YesNo;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;
import com.xianglin.appserv.common.service.facade.req.ArticleReq;
import com.xianglin.appserv.common.util.*;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.model.CustomersDTO;
import com.xianglin.fala.session.Session;
import com.xianglin.gateway.common.service.facade.model.ResultEnum;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author wanglei 2016年8月16日下午5:43:50
 */
@ServiceInterface
@Service
public class IndexServiceImpl implements IndexService {

    private static final Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);

    /**  */
    @Autowired
    private MessageManager messageManager;

    @Autowired
    private BannerManager bannerManager;

    /**
     * sessionHelper
     */
    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private BusinessManager businessManager;

    @Autowired
    private ArticleManager articleManager;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private AppPushDAO appPushDAO;

    @Autowired
    private CustomersInfoService customersInfoService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LogManager logManager;

    @Autowired
    private ResponseCacheUtils responseCacheUtils;

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.indexBanners", description = "查询首页banners列表")
    public Response<List<BannerVo>> indexBanners() {
        return indexBannersV2("1.3.0", null);
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.indexBannersV2", description = "查询首页banners列表")
    public Response<List<BannerVo>> indexBannersV2(String clientVersion, String type) {
        Response<List<BannerVo>> response = ResponseUtils.successResponse();
        try {
            Map<String, Object> paras = DTOUtils.queryMap();
            String supportVersion = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
            if (StringUtils.isEmpty(supportVersion)) {
                supportVersion = clientVersion;
            }
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            User user = userManager.queryUser(partyId);
            paras.put("pageSize", 9);
            paras.put("type", type);
            paras.put("orderBy", "PRIORITY_LEVEL ASC");
            List<BannerVo> list = bannerManager.indexBannerList(paras);
            //用户类型
            String userType = sessionHelper.getSessionProp(SessionConstants.USER_TYPE, String.class);
            if (StringUtils.isBlank(userType)) {
                userType = UserType.visitor.name();
            }
            String userType2 = userType;
            //查客户端系统
            String clientType = queryClientType();
            //查用户的第一级code
            /*String code=null;
            if(user != null && StringUtils.isNotEmpty(user.getDistrict())){
                AreaVo area = JSON.parseObject(user.getDistrict(), AreaVo.class);
                code =  area.getProvince().getCode();
            }*/
            String provinceName = null;
            if (user != null && StringUtils.isNotEmpty(user.getDistrict())) {
                AreaVo area = JSON.parseObject(user.getDistrict(), AreaVo.class);
                provinceName = area.getProvince().getName();
            }
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
                Iterator<BannerVo> iter = list.iterator();
                while (iter.hasNext()) {
                    BannerVo vo = iter.next();
                    userType = userType2;
                    //状态为Y删除或开始时间和结束时间为空的删除
                    if (StringUtils.isNotEmpty(vo.getBannerStatus()) && vo.getBannerStatus().equals(Constant.YESNO.NO.code)) {
                        iter.remove();
                        continue;
                    }
                    if ((vo.getStartDate() == null && vo.getEndDate() == null) || StringUtils.isEmpty(vo.getSupportUserType())) {
                        iter.remove();
                        continue;
                    }
                    //用户类型为all
                    if (vo.getSupportUserType().equals("all")) {
                        userType = "all";
                    }

                    //用户可见用户类型
                    if (StringUtils.isNotEmpty(vo.getSupportUserType()) && !StringUtils.contains(vo.getSupportUserType(), userType)) {
                        iter.remove();
                        continue;
                    }
                    //支持系统
                    if (StringUtils.isNotEmpty(vo.getSupportOs()) && !StringUtils.contains(vo.getSupportOs(), clientType)) {
                        iter.remove();
                        continue;
                    }
                    //支持版本
                    if (StringUtils.isNotEmpty(vo.getSupportVersion()) && !StringUtils.contains(vo.getSupportVersion(), supportVersion)) {
                        iter.remove();
                        continue;
                    }
                    //可见地域
                    if (StringUtils.isNotEmpty(vo.getSupportArea()) && user != null && StringUtils.isNotEmpty(provinceName) && !StringUtils.contains(vo.getSupportArea(), provinceName)) {
                        iter.remove();
                        continue;
                    }
                    //如果有用户预览或有地区 并且 用户状态为游客
                    if ((StringUtils.isNotEmpty(vo.getSupportUser()) || StringUtils.isNotEmpty(vo.getSupportArea())) && (StringUtils.equals(userType, UserType.visitor.name()) || StringUtils.equals(userType2, UserType.visitor.name()))) {
                        iter.remove();
                        continue;
                    }
                    //支持用户的手机号
                    if (StringUtils.isNotEmpty(vo.getSupportUser()) && user != null && StringUtils.isNotEmpty(user.getLoginName()) && !StringUtils.contains(vo.getSupportUser(), user.getLoginName())) {
                        iter.remove();
                        continue;
                    }
                    /*if(StringUtils.isNotEmpty(vo.getSupportUser()) && StringUtils.equals(userType, UserType.visitor.name())){
                        iter.remove();
                        continue;
                    }*/
                    //判断上线时间是否大于当前时间，大于当前时间就remove
                    if (vo.getStartDate() != null && vo.getStartDate().compareTo(new Date()) == 1) {
                        iter.remove();
                        continue;
                    }
                    //判断下线时间是否小于当前时间，小于就remove
                    if (vo.getEndDate() != null && new Date().compareTo(vo.getEndDate()) == 1) {
                        iter.remove();
                        continue;
                    }
                    //查询有banner图片，仅首页
                    if (StringUtils.equals(type, Constant.BannerType.INDEX.name()) && StringUtils.isEmpty(vo.getBannerImage())) {
                        vo.setBannerImage(SysConfigUtil.getStr("BANNER_DEFAULT_IMG"));
                    }
                    String content = HtmlUtils.htmlUnescape(vo.getContent());
                    vo.setContent(content);
                }
            }
            response.setResult(list);
        } catch (Exception e) {
            logger.error("indexBanners error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.indexBusiness", description = "查询首页开通业务列表")
    public Response<List<BusinessVo>> indexBusiness() {
        return indexBusinessV2("1.3.0");
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.indexBusinessV2", description = "查询首页开通业务列表")
    public Response<List<BusinessVo>> indexBusinessV2(String clientVersion) {
        Response<List<BusinessVo>> response = ResponseUtils.successResponse();
        try {
            Session session = sessionHelper.getSession();
            session.setAttribute(SessionConstants.CLIENT_VERSION, clientVersion);
            sessionHelper.saveSession(session);
            sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
            Set<String> busiSet = businessManager.queryUserBusiness();
            List<BusinessVo> list = getBusiness(null, clientVersion, busiSet);
            // 2,删除多余的业务，保证最终结果最多7个
            if (StringUtils.startsWith(clientVersion, "3.4")) {
                list = CollectionUtils.remove(list, 9, list.size());
            } else {
                list = CollectionUtils.remove(list, 7, list.size());
            }
            BusinessVo businessVo = new BusinessVo();
            businessVo.setBusiName("全部");
            businessVo.setId(0L);
            businessVo.setBusiImage("https://appfile.xianglin.cn/file/1038617");
            list.add(businessVo);
            response.setResult(list);
        } catch (Exception e) {
            logger.warn("indexBusiness error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.app.IndexService#indexNewsMsg(com.xianglin.appserv.common.service.facade.model.Request)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.indexNewsMsg", description = "首页消息查询")
    public Response<List<MsgVo>> indexNewsMsg(Request<MsgQuery> req) {
        Response<List<MsgVo>> response = ResponseUtils.successResponse();
        try {
            List<MsgVo> list = messageManager.listNews(req.getReq());
            for (MsgVo vo : list) {
                vo.setUrl(SysConfigUtil.getStr("H5WECHAT_URL") + "/home/nodeManager/topNews.html?id=" + vo.getId());
                vo.setMessage(StringUtils.substring(StringUtils.replacePattern(vo.getMessage(), "<.*?>", ""), 0, 200));
            }
            response.setResult(list);
        } catch (Exception e) {
            logger.error("indexNewsMsg error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.themeSwitch", description = "主题更换")
    public Response<String> themeSwitch() {
        Response<String> response = ResponseUtils.successResponse();
        String theme = SysConfigUtil.getStr("THEME_TEMPLATE", Constant.ThemeType.DEFAULT.name());
        response.setResult(theme);
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.indexBusinessAll", description = "全部业务入口")
    public Response<BusinessAllVo> indexBusinessAll(String clientVersion) {
        Response<BusinessAllVo> resp = ResponseUtils.successResponse();
        BusinessAllVo businessAllVo = new BusinessAllVo();
        try {
            businessAllVo.setXianglinLifeBusiness(queryXlLife(clientVersion));//村情村貌
            Set<String> busiSet = businessManager.queryUserBusiness();
            if (org.apache.commons.collections.CollectionUtils.isEmpty(busiSet) && compareVersion(clientVersion, "3.5.3")) {
                busiSet = new HashSet<>();
                busiSet.add("VISITOR");
                busiSet.add("MERCHANT");
                busiSet.add("LOGIN");
                busiSet.add("LIVE");
            }
            businessAllVo.setUserBusiness(getBusiness(Constant.ConvenientToPersonType.MYAPPS.name(), clientVersion, busiSet));//乡邻财富
            businessAllVo.setQueryBusiness(getBusiness(Constant.ConvenientToPersonType.PERSONQUERY.name(), clientVersion, busiSet));//便民生活
            businessAllVo.setServicesBusiness(getBusiness(Constant.ConvenientToPersonType.PERSONSERVICE.name(), clientVersion, busiSet));//购物娱乐
            businessAllVo.setLifeBusiness(getBusiness(Constant.ConvenientToPersonType.PERSONLIFE.name(), clientVersion, busiSet));//乡邻工具
            resp.setResult(businessAllVo);
        } catch (Exception e) {
            logger.info("indexBusinessAll error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }


    /**
     * 版本号比较
     *
     * @param version1
     * @param version2
     * @return
     */
    private boolean compareVersion(String version1, String version2) {
        try {
            return Integer.valueOf(StringUtils.replace(version1, ".", "")) >= Integer.valueOf(StringUtils.replace(version2, ".", ""));
        } catch (Exception e) {
            logger.info("info error", e);
        }
        return false;
    }

    /**
     * 判断并查询村务
     *
     * @param clientVersion
     * @return
     */
    private List<BusinessVo> queryXlLife(String clientVersion) throws Exception {
        List<BusinessVo> list = null;
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        if (partyId != null) { //查询是不是试点村，是的话就展示
            String code = null;
            //查询用户的信息
            User user = userManager.queryUser(partyId);
            if (StringUtils.isNotEmpty(user.getDistrict())) {
                AreaVo area = JSON.parseObject(user.getDistrict(), AreaVo.class);
                if (area.getVillage() != null) {
                    code = area.getVillage().getCode();
                }
            }
            if (StringUtils.isNotEmpty(code)) {
                //根据用户的code查询群的code
                Map<String, Object> paras = new HashMap<>();
                paras.put("districtCode", code);
                paras.put("groupType", Constant.GroupType.V.name());
                paras.put("createType", Constant.CreateType.SYSTEM.name());
                List<AppGroup> appGroupList = groupManager.queryGroup(paras);
                if (appGroupList.size() > 0) {
                    paras.clear();
                    paras.put("groupId", appGroupList.get(0).getId());
                    paras.put("type", Constant.MemberType.MANAGER);
                    List<AppGroupMember> appGroupMemberList = groupManager.queryMembers(paras);
                    if (appGroupMemberList.size() > 0) {
                        Set<String> busiSet = businessManager.queryUserBusiness();
                        if (busiSet == null) {
                            busiSet = new HashSet<>();
                        }
                        busiSet.add("LIFE");
                        list = getBusiness(Constant.ConvenientToPersonType.LIFE.name(), clientVersion, busiSet);
                    }
                }
            }
        }
        return list;
    }

    private List<BusinessVo> getBusiness(String busiCategory, String clientVersion, Set<String> busiSet) {
        return getBusiness(busiCategory,clientVersion,busiSet,Constant.BusiSupportUser.COMMON);
    }


    private List<BusinessVo> getBusiness(String busiCategory, String clientVersion, Set<String> busiSet, Constant.BusiSupportUser supportUserType) {
        List<BusinessVo> list = new ArrayList<BusinessVo>();
        List<AppBusiness> businessList;
        try {
            AppBusiness paras = AppBusiness.builder()
                    .supportUserType(supportUserType.name())
                    .busiCategory(busiCategory).supportVersion(clientVersion).build();
            businessList = bannerManager.queryBusiness(paras);
            if (busiSet == null) {
                busiSet = new HashSet<>();
            }
            list = checkBusiness(businessList, busiSet);
        } catch (Exception e) {
            logger.warn("getBusiness error", e);
        }
        return list;
    }

    /**
     * 检验用户业务权限
     *
     * @param businessList
     * @return
     * @throws Exception
     */
    private List<BusinessVo> checkBusiness(List<AppBusiness> businessList, final Set<String> busiSet) throws Exception {
        List<BusinessVo> list = new ArrayList<BusinessVo>();
        logger.info("用户开通业务，busiSet:{}", busiSet);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(businessList)) {
            String clientType = queryClientType();
            Iterator<AppBusiness> iter = businessList.iterator();
            while (iter.hasNext()) {
                AppBusiness vo = iter.next();
                if (StringUtils.isNotEmpty(vo.getAuthSign()) && !checkAuth(busiSet, Arrays.asList(vo.getAuthSign().split(",")))) {
                    iter.remove();
                } else if (StringUtils.isNotEmpty(vo.getSupportOS()) && !StringUtils.equalsIgnoreCase(vo.getSupportOS(), clientType)) {
                    iter.remove();
                }
            }
            list = DTOUtils.map(businessList, BusinessVo.class);
        }
        return list;
    }

    /**
     * 业务权限判断
     *
     * @param busiSet   用户当前包含权限
     * @param checkAuth 业务需要的权限
     * @return
     */
    private boolean checkAuth(final Set<String> busiSet, Collection<String> checkAuth) {
        boolean result = true;
        if (org.apache.commons.collections.CollectionUtils.isEmpty(checkAuth)) {
            return true;
        }
        result = !checkAuth.stream().map(auth -> {
            if (StringUtils.startsWith(auth, "!")) {
                return !busiSet.contains(StringUtils.substring(auth, 1));
            } else if (StringUtils.contains(auth, "|")) {
                return org.apache.commons.collections.CollectionUtils.containsAny(busiSet, Arrays.asList(StringUtils.split(auth, "|")));
            } else {
                return busiSet.contains(auth);
            }
        }).anyMatch(i -> i == false);
        return result;
    }

    /**
     * 查询客户端类型
     *
     * @return
     */
    private String queryClientType() {
        String clientType = "IOS";
        try {
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            AppPush push = appPushDAO.selectByDeviceId(deviceId);
            logger.info("systype device:{},push:{}", deviceId, push);
            if (push != null && StringUtils.isNotEmpty(push.getSystemType())) {
                clientType = push.getSystemType();
            }
        } catch (Exception e) {
            logger.warn("queryClientType", e);
        }
        return clientType;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.indexAlert", description = "活动弹窗以及乡邻介绍")
    public Response<IndexAlertVo> indexAlert(String clientVersion) {
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
        Response<IndexAlertVo> response = ResponseUtils.successResponse();
        IndexAlertVo indexAlertVo = new IndexAlertVo();
        try {
            // 先设置乡邻公司介绍
            indexAlertVo.setCompanyIntroduce(SysConfigUtil.getStr("xl_company_introduce"));
            // 先判断弹框是否被禁止
            List<AppActivityTask> list1 = new ArrayList<AppActivityTask>();
            Boolean showTip = false;
            // 登录用户是否禁止弹
            if (partyId != null) {
                list1 = activityManager.queryActivityTask(AppActivityTask.builder().taskCode(Constant.ActivityTaskType.NEVER.name()).activityCode(Constant.ActivityType.LUCKY.code).partyId(partyId).build(), null);
            } else {
                // 设备是否禁止弹
                list1 = activityManager.queryActivityTask(AppActivityTask.builder().taskCode(Constant.ActivityTaskType.NEVER.name()).activityCode(Constant.ActivityType.LUCKY.code).deviceId(deviceId).build(), null);
            }
            // 如果设备和用户都没被禁止弹
            if (list1.size() == 0) {
                // 活动弹框是否弹
                Map<String, Object> queryMap = DTOUtils.queryMap();
                queryMap.put("dataStatus", Constant.YESNO.YES.code);
                queryMap.put("activeCode", Constant.ActivityType.LUCKY.code);
                List<AppActivity> queryActivitys = activityManager.queryActivitys(queryMap);
                showTip = (queryActivitys.size() != 0 && queryActivitys.get(0).getDataStatus().equals("Y")) ? true
                        : false;
                if (showTip) {
                    if (partyId != null) {
                        Boolean flag = saveUpdateActivity();
                        showTip = flag;
                    } else {
                        // 用户未登录,一直弹,客户端控制弹一次
                        saveUpdateActivity();
                        showTip = true;
                    }
                }
            }
            if (showTip) {
                // 设置活动地址 是否弹框根据这个值是否为空
                indexAlertVo.setShareAlert(SysConfigUtil.getStr("activity.share.showActivity.url"));
            }
            indexAlertVo.setRegisterGoldcoin(200);//暂定

            indexAlertVo.setSignGoldcoin(30);
            response.setResult(indexAlertVo);
        } catch (Exception e) {
            logger.warn("indexAlert error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * 创建是否弹框记录
     *
     * @return
     */
    private Boolean saveUpdateActivity() {
        boolean showTip = false;
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);

        String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
        List<AppActivityTask> activityTaskList = activityManager.queryActivityTask(AppActivityTask.builder().daily(today).deviceId(deviceId).taskCode(Constant.ActivityTaskType.POPUP.name()).build(), null);
        // 如果集合为空说明没有弹过
        if (org.apache.commons.collections.CollectionUtils.isEmpty(activityTaskList)) {
            AppActivityTask appActivityTask = new AppActivityTask();
            if (partyId != null) {
                appActivityTask.setPartyId(partyId);
                appActivityTask.setTaskDailyId(partyId + "" + System.currentTimeMillis());
            } else {
                appActivityTask.setTaskDailyId(System.currentTimeMillis() + "");
            }
            appActivityTask.setActivityCode(Constant.ActivityType.LUCKY.code);
            appActivityTask.setDeviceId(deviceId);
            appActivityTask.setTaskCode(Constant.ActivityTaskType.POPUP.name());
            appActivityTask.setDaily(today);
            appActivityTask.setTaskName(Constant.ActivityType.LUCKY.desc);
            appActivityTask.setUseStatus(Constant.YESNO.YES.code);
            appActivityTask.setTaskStatus(Constant.YESNO.YES.code);
            boolean flag = activityManager.saveUpdateActivityTask(appActivityTask).getId() == null;
            showTip = flag;
        }
        return showTip;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.indexAlertConfirm", description = "活动弹窗以后都不再弹了")
    public Response<Boolean> indexAlertConfirm(String activityCode) {
        Response<Boolean> response = ResponseUtils.successResponse();
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
        AppActivityTask appActivityTask = new AppActivityTask();
        boolean flag = false;
        if (partyId != null) {// 登录状态
            String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
            appActivityTask.setTaskCode(Constant.ActivityTaskType.NEVER.name());
            appActivityTask.setTaskDailyId(partyId + "" + System.currentTimeMillis());
            appActivityTask.setTaskStatus(Constant.YESNO.YES.code);
            appActivityTask.setActivityCode(activityCode);
            appActivityTask.setDeviceId(deviceId);
            appActivityTask.setPartyId(partyId);
            appActivityTask.setDaily(today);
            appActivityTask.setUseStatus(Constant.YESNO.YES.code);
            flag = activityManager.saveUpdateActivityTask(appActivityTask).getId() == null;
        } else {// 未登录状态
            appActivityTask.setTaskCode(activityCode);
            appActivityTask.setTaskCode(Constant.ActivityTaskType.NEVER.name());
            appActivityTask.setActivityCode(activityCode);
            appActivityTask.setTaskStatus(Constant.YESNO.YES.code);
            appActivityTask.setDeviceId(deviceId);
            flag = activityManager.saveUpdateActivityTask(appActivityTask).getId() == null;
        }
        response.setResult(flag);
        if (flag == false) {
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.indexActivity", description = "活动推荐")
    public Response<List<ActivityVo>> indexActivity(String clientVersion) {
        Response<List<ActivityVo>> resp = ResponseUtils.successResponse();
        List<ActivityVo> list = new ArrayList<ActivityVo>();
        try {
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("isPraise", YesNo.Y.name());
            List<AppActivity> queryActivitys = activityManager.queryActivitys(paras);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(queryActivitys)) {
                //list = DTOUtils.map(queryActivitys, ActivityVo.class);
                Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
                //根据partyID查询年报数据
                Map map = null;
                if (partyId != null) {
                    map = userManager.queryAnnualReport(partyId);
                }
                for (AppActivity appActivity : queryActivitys) {
                    ActivityVo activityVo = null;
                    if (appActivity.getActiveCode().equals("205")) {
                        if (partyId != null && map != null) {
                            activityVo = DTOUtils.map(appActivity, ActivityVo.class);
                            list.add(activityVo);
                        }
                    } else {
                        if (StringUtils.isNotEmpty(appActivity.getSupportVersion())) {
                            if (StringUtils.contains(appActivity.getSupportVersion(), clientVersion)) {
                                activityVo = DTOUtils.map(appActivity, ActivityVo.class);
                                list.add(activityVo);
                            }
                        } else {
                            activityVo = DTOUtils.map(appActivity, ActivityVo.class);
                            list.add(activityVo);
                        }
                    }
                }

            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.warn("indexActivity error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.indexPointRush", description = "乡邻头条50条滚动")
    public Response<List<PointRushVo>> indexPointRush(String clientVersion) {
        // 本期查询新闻50条封装成滚动对象 乡邻头条 本期版本号不使用
        Response<List<PointRushVo>> response = ResponseUtils.successResponse();
        MsgQuery msgQuery = new MsgQuery();
        List<PointRushVo> listPointRushVo = new ArrayList<PointRushVo>();
        try {
            msgQuery.setPageSize(50);
            msgQuery.setStartPage(1);
            msgQuery.setMsgType(Constant.MsgNewsTag.XLXW.name());
            List<MsgVo> list = messageManager.listNews(msgQuery);
            for (MsgVo vo : list) {
                PointRushVo pointRushVo = new PointRushVo();
                vo.setUrl(SysConfigUtil.getStr("H5WECHAT_URL") + "/home/nodeManager/topNews.html?id=" + vo.getId());
                vo.setMessage(StringUtils.substring(StringUtils.replacePattern(vo.getMessage(), "<.*?>", ""), 0, 50));
                pointRushVo.setPointRush(vo.getMsgTitle());
                pointRushVo.setPushActive(vo.getUrl());
                listPointRushVo.add(pointRushVo);
            }
            response.setResult(listPointRushVo);
        } catch (Exception e) {
            logger.warn("indexPointRush error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.indexArticle", description = "首页动态推荐")
    public Response<List<ArticleVo>> indexArticle(String clientVersion) {
        Response<List<ArticleVo>> resp = new Response<List<ArticleVo>>(null);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String userType = sessionHelper.getSessionProp(SessionConstants.USER_TYPE, String.class);
            if (StringUtils.isEmpty(userType)) {
                userType = Constant.UserType.visitor.name();
            }
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("pageSize", 5);
            paras.put("orderBy", "FOLLOWCOUNT2");// 点赞和评论总和字段
            paras.put("articleType", Constant.ArticleType.SUBJECT.name());
            List<AppArticle> list = new ArrayList<AppArticle>();
            List<AppArticle> list3 = new ArrayList<AppArticle>();
            // 设置查询时间;
            int chooseDay = 0;
            while (list.size() == 0) {
//                Date startDate = org.apache.commons.lang3.time.DateUtils.getStartDate(chooseDay);
//                Date endDate = DateUtils.getEndDate(chooseDay);
//                paras.put("startDate", startDate);
//                paras.put("endDate", endDate);
                List<AppArticle> list2 = articleManager.queryArticleList(paras);
                chooseDay -= 1;
                list.addAll(list2);
            }
            if (list.size() > 5) {
                list = list.subList(0, 5);
            }
            for (AppArticle appArticle : list) {
                // 先做是否点赞判断
                Map<String, Object> paras2 = DTOUtils.queryMap();
                paras2.put("tipType", Constant.ArticleTipType.PRAISE.name());
                paras2.put("articleId", appArticle.getId());
                if (partyId != null) {
                    paras2.put("partyId", partyId);
                    List<AppArticleTip> queryArticleTipList2 = articleManager.queryArticleTipList(paras2);
                    if (org.apache.commons.collections.CollectionUtils.isEmpty(queryArticleTipList2)) {
                        appArticle.setArticleStatus(Constant.YESNO.NO.code);
                    } else {
                        appArticle.setArticleStatus(queryArticleTipList2.get(0).getTipStatus());
                    }
                } else {
                    appArticle.setArticleStatus(Constant.YESNO.NO.code);
                }
                list3.add(appArticle);
            }
            List<ArticleVo> result = convertPojo(list3, partyId);
            resp.setResult(result);
            resp.setCode(1000);
        } catch (Exception e) {
            logger.warn("indexArticle error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.queryDefaultVillage", description = "查询默认的村")
    public Response<VillageVo> queryDefaultVillage() {
        Response<VillageVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            String code = null; //村code
            String district = null;
            String groupName = null;
            //查询用户的信息
            User user = userManager.queryUser(partyId);
            if (user != null) {
                if (StringUtils.isNotEmpty(user.getDistrict())) {
                    AreaVo area = JSON.parseObject(user.getDistrict(), AreaVo.class);
                    if (area.getVillage() != null && StringUtils.isNotEmpty(area.getVillage().getCode())) {
                        code = area.getVillage().getCode();
                        groupName = area.getVillage().getName();
                    }
                    //将不存在当前地址的默认村删除
                    //deleteVillage(partyId, code);
                    //只有第5级可以有村,将不是第5级地址的村务删除
                    if (StringUtils.isEmpty(code) && StringUtils.isEmpty(groupName)) {
                        //如果村为空，将该地址最低一级的村务删除
                        AreaVo areaVo = new AreaVo();
                        String lawcode = areaVo.selectCode(area); //用户最低一级的code
                        //根据用户的code查询群的code
                        if (StringUtils.isEmpty(lawcode)) {
                            return resp;
                        }
                        Map<String, Object> paras = new HashMap<>();
                        paras.put("districtCode", lawcode);
                        paras.put("groupType", Constant.GroupType.V.name());
                        paras.put("createType", Constant.CreateType.SYSTEM.name());
                        List<AppGroup> appGroupList = groupManager.queryGroup(paras);
                        if (appGroupList.size() > 0) {
                            groupManager.updateGroup(AppGroup.builder().id(appGroupList.get(0).getId()).isDeleted("Y").build());
                        }
                        paras.remove("groupType");
                        paras.put("groupType", Constant.GroupType.G.name());
                        List<AppGroup> villGroupList = groupManager.queryGroup(paras);
                        if (villGroupList.size() > 0) {
                            groupManager.updateGroup(AppGroup.builder().id(villGroupList.get(0).getId()).isDeleted("Y").build());
                        }
                        return resp;
                    }
                }
                
                /*code = areaVo.selectCode(area);
                district = areaVo.selectName(area);
                groupName = areaVo.selectLowName(area);*/
            }
            VillageVo villageVo = new VillageVo();
            GroupVo groupVo = null;   //村信息
            ArticleVo articleVo = null; //财务公开
            ArticleVo broadcast = null; //村广播

            //查村，如果存不存在就创建存，并将当前登录用户添加到这个村里面
            if (StringUtils.isNotEmpty(code)) {
                //根据用户的code查询群的code
                Map<String, Object> paras = new HashMap<>();
                paras.put("districtCode", code);
                paras.put("groupType", Constant.GroupType.V.name());
                paras.put("createType", Constant.CreateType.SYSTEM.name());
                List<AppGroup> appGroupList = groupManager.queryGroup(paras);
                if (appGroupList.size() > 0) {//如果村存在，将当前用户添加进去
                    AppGroup appGroup = appGroupList.get(0);
                    Boolean isManager = false;
                    AppGroupMember groupMember = null;

                    //查询该用户是否在这个村
                    paras.clear();
                    paras.put("partyId", partyId);
                    paras.put("groupId", appGroupList.get(0).getId());
                    List<AppGroupMember> appGroupMemberList = groupManager.queryMembers(paras);
                    if (appGroupMemberList.size() > 0) {
                        groupMember = appGroupMemberList.get(0);
                    } else {
                        //返回该群
                        AppGroupMember appGroupMember = AppGroupMember.builder().groupId(appGroupList.get(0).getId()).partyId(partyId).tempName(getUserName(user.getPartyId(), user.getNikerName(), user.getTrueName())).tempMobilePhone(user.getLoginName()).type(Constant.MemberType.MEMBER.name()).operator(partyId).build();
                        groupManager.insertGroupMeber(appGroupMember);
                        if (appGroupMember.getId() != null) {
                            groupMember = groupManager.groupMember(appGroupMember.getId());
                        }
                        appGroup = appGroupList.get(0);
                    }
                    if (groupMember != null && groupMember.getType() != null) {
                        if (groupMember.getType().equals(Constant.MemberType.MANAGER.name())) {
                            isManager = true;
                        }
                    }
                    groupVo = GroupVo.builder().name(appGroup.getName()).id(appGroup.getId()).isManager(isManager).build();
                    //如果当前村的成员>=2创建群，并将村成员初始化到群里面
                    createDefaultGroup(appGroup, user);
                } else {
                    //创建群
                    AppGroup appGroup = null;
                    appGroup = AppGroup.builder().managePartyId(partyId).district(district).districtCode(code).createType(Constant.CreateType.SYSTEM.name()).groupType(Constant.GroupType.V.name()).name(groupName).imageUrl(SysConfigUtil.getStr("group_default_url")).operator(partyId).build();
                    Boolean insertGroup = groupManager.create(appGroup);
                    if (insertGroup) {
                        AppGroupMember appGroupMember = AppGroupMember.builder().groupId(appGroup.getId()).partyId(partyId).tempName(getUserName(user.getPartyId(), user.getNikerName(), user.getTrueName())).type(Constant.MemberType.MEMBER.name()).tempMobilePhone(user.getLoginName()).operator(partyId).build();
                        groupManager.insertGroupMeber(appGroupMember);
                        appGroupMember = groupManager.groupMember(appGroupMember.getId());
                        List<AppGroup> list = groupManager.queryGroup(paras);
                        if (list.size() > 0) {
                            Boolean isManager = false;
                            if (appGroupMember.getType() != null) {
                                if (appGroupMember.getType().equals(Constant.MemberType.MANAGER.name())) {
                                    isManager = true;
                                }
                            }
                            groupVo = GroupVo.builder().name(list.get(0).getName()).id(list.get(0).getId()).isManager(isManager).build();
                        }
                    }
                    //如果当前村的成员>=2创建群，并将村成员初始化到群里面
                    createDefaultGroup(appGroup, user);
                }
            }
            //查询村的政务公开
            if (groupVo != null) {
                //查询村的广播
                Map<String, Object> paras = new HashMap<>();
                paras.put("groupId", groupVo.getId());
                paras.put("articleType", Constant.ArticleType.BROADCAST.name());
                List<AppArticle> bList = articleManager.queryArticleList(paras);
                if (bList.size() > 0) {
                    AppArticle bArticle = bList.get(0);
                    broadcast = DTOUtils.map(bArticle, ArticleVo.class);
                }
                //查询有没有管理员
                paras.clear();
                paras.put("type", Constant.MemberType.MANAGER.name());
                paras.put("groupId", groupVo.getId());
                int count = groupManager.queryGroupMemberCount(paras);
                if (count > 0) {
                    groupVo.setStatus("Y");
                } else {
                    groupVo.setStatus("N");
                }
            }
            if (articleVo != null) {
                if (StringUtils.isNotEmpty(articleVo.getNikeName())) {
                    articleVo.setReplyNickName(articleVo.getNikeName());
                } else {
                    articleVo.setReplyNickName("xl" + articleVo.getReplyPartyId());
                }
                //实名认证信息
                String name = queryRealName(articleVo.getPartyId());
                if (name != null) {
                    articleVo.setReplyNickName(name);
                }
                articleVo.setDateTime(indecateTime(articleVo.getCreateTime()));
            }
            villageVo.setGroupVo(groupVo);
            villageVo.setArticleVo(articleVo);
            villageVo.setBroadcast(broadcast);
            resp.setResult(villageVo);
        } catch (Exception e) {
            logger.warn("queryDefaultVillage error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    private void createDefaultGroup(AppGroup appGroup, User user) {
        //先判断村的成员是否>=2，如果是
        Map<String, Object> paras = new HashMap<>();
        paras.put("groupId", appGroup.getId());
        List<AppGroupMember> list = groupManager.queryMembers(paras);
        if (list.size() >= 2) {  //根据村code查询群是否存在
            paras.clear();
            paras.put("districtCode", appGroup.getDistrictCode());
            paras.put("groupType", Constant.GroupType.G.name());
            paras.put("createType", Constant.CreateType.SYSTEM.name());
            List<AppGroup> listGroup = groupManager.queryGroup(paras);
            if (listGroup.size() > 0) {   //存在
                //判断当前用户是否在群里面，不在就添加进去
                paras.put("partyId", user.getPartyId());
                paras.put("groupId", listGroup.get(0).getId());
                List<AppGroupMember> appGroupMembers = groupManager.queryMembers(paras);
                if (appGroupMembers.size() == 0) {
                    if (RyUtil.join(user.getPartyId(), listGroup.get(0).getRyGroupId(), listGroup.get(0).getName())) {
                        AppGroupMember appGroupMember = AppGroupMember.builder().partyId(user.getPartyId()).tempMobilePhone(user.getLoginName()).tempName(user.getTrueName()).groupId(listGroup.get(0).getId()).operator(user.getPartyId()).build();
                        groupManager.insertExceptMember(appGroupMember);
                        //判断群是否有管理员
                        if (listGroup.get(0).getManagePartyId().equals(0)) {
                            groupManager.updateGroup(AppGroup.builder().id(listGroup.get(0).getId()).managePartyId(user.getPartyId()).build());
                        }
                    }
                }
            } else { //不存在
                //创建群，并将村的用户同步到群里面
                AppGroup group = AppGroup.builder().managePartyId(user.getPartyId()).groupType(Constant.GroupType.G.name()).name(appGroup.getName()).imageUrl(SysConfigUtil.getStr("group_default_url")).createType(Constant.CreateType.SYSTEM.name()).operator(user.getPartyId()).ryGroupId(UUID.randomUUID().toString()).build();
                Boolean create = groupManager.create(group);
                if (create) {
                    if (RyUtil.create(user.getPartyId(), group.getRyGroupId(), appGroup.getName())) {
                        //查询村里面所有的人员
                        for (AppGroupMember groupMember : list) {
                            if (RyUtil.join(groupMember.getPartyId(), group.getRyGroupId(), groupMember.getTempName())) {
                                AppGroupMember appGroupMember = AppGroupMember.builder().partyId(groupMember.getPartyId()).tempMobilePhone(groupMember.getTempMobilePhone()).tempName(groupMember.getTempName()).groupId(group.getId()).operator(user.getPartyId()).build();
                                groupManager.insertExceptMember(appGroupMember);
                            }
                        }
                    }
                }
            }
        } else { //村务的人数<2，如果村群存在，将村群解散
            paras.put("districtCode", appGroup.getDistrictCode());
            paras.put("groupType", Constant.GroupType.G.name());
            paras.put("createType", Constant.CreateType.SYSTEM.name());
            List<AppGroup> listGroup = groupManager.queryGroup(paras);
            if (listGroup.size() > 0) {
                if (RyUtil.dismiss(user.getPartyId(), appGroup.getRyGroupId())) {
                    groupManager.updateGroup(AppGroup.builder().id(listGroup.get(0).getId()).isDeleted("Y").build());
                }
            }
        }
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.indexBusinessV3", description = "查询首页开通业务列表")
    public Response<List<BusinessVo>> indexBusinessV3(String clientVersion) {
        Response<List<BusinessVo>> response = ResponseUtils.successResponse();
        try {
            Session session = sessionHelper.getSession();
            session.setAttribute(SessionConstants.CLIENT_VERSION, clientVersion);
            sessionHelper.saveSession(session);
            Set<String> busiSet = businessManager.queryUserBusiness();
            List<BusinessVo> lifeList = queryXlLife(clientVersion);//村务2
            List<BusinessVo> allList = new ArrayList<>();//结果集
            int maxLength = 9;
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            List<BusinessVo> userBusiness;
            if (partyId != null && partyId != -1) {//已登录用户
                List<BusinessVo> defaultBusiness = checkBusiness(bannerManager.queryUserBusiness(AppBusiness.builder().partyId(1L).supportVersion(clientVersion).build()), busiSet);
                if (org.apache.commons.collections.CollectionUtils.isEmpty(lifeList)) {
                    defaultBusiness.remove(0);//删除招工
                } else {
                    maxLength = 14;
                    allList.addAll(lifeList);
                }
                userBusiness = checkBusiness(bannerManager.queryUserBusiness(AppBusiness.builder().partyId(partyId).supportVersion(clientVersion).build()), busiSet);
                userBusiness.addAll(defaultBusiness);
            } else {//未登录用户
                busiSet = new HashSet<>();
                busiSet.add("LOGIN");
                busiSet.add("VISITOR");
                userBusiness = checkBusiness(bannerManager.queryUserBusiness(AppBusiness.builder().partyId(0L).supportVersion(clientVersion).build()), busiSet);
            }
            allList.addAll(userBusiness);

            //排重
            List<BusinessVo> resultList = new ArrayList<>();
            String blackList = SysConfigUtil.getStr("INDEX_BUSINESS_BLACK_LIST");
            Set<String> busenessSet = new HashSet<>();
            if (StringUtils.isNotEmpty(blackList)) {
                busenessSet = Arrays.stream(blackList.split(",")).collect(Collectors.toSet());
            }
            for (BusinessVo vo : allList) {
                if (!busenessSet.contains(vo.getBusiName())) {
                    resultList.add(vo);
                    busenessSet.add(vo.getBusiName());
                    if (resultList.size() >= maxLength) {
                        break;
                    }
                }
            }

            BusinessVo businessVo = new BusinessVo();
            businessVo.setBusiName("全部");
            businessVo.setId(0L);
            businessVo.setBusiImage("https://appfile.xianglin.cn/file/1038617");
            businessVo.setBusiIcon("https://cdn02.xianglin.cn/619bcf4e240e20eabd9857403905e9b9-2087.png");
            resultList.add(businessVo);
            response.setResult(resultList);
        } catch (Exception e) {
            logger.warn("indexBusiness error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.clickBusiness", description = "点击业务")
    public Response<Boolean> clickBusiness(Long businessId) {
        Response<Boolean> resp = ResponseUtils.successResponse(true);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId != null) {
                AppBusiness business = bannerManager.queryBusiness(businessId);
                if (business != null) {
                    boolean isCheck = Arrays.stream(SysConfigUtil.getStr("business_realName_check").split(","))
                            .anyMatch(s -> s.equals(business.getBusiCode()));
                    if (isCheck) {
                        boolean isAuth = userManager.queryAndSynRealName(partyId);
                        if (!isAuth) {
                            resp.setResultEnum(ResultEnum.UnAuth);
                            return resp;
                        }
                    }
                    bannerManager.updateUserBusiness(partyId, business);
                }
            }
        } catch (Exception e) {
            logger.warn("queryGame error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.queryGame", description = "查询游戏列表")
    public Response<List<ActivityVo>> queryGame() {
        Response<List<ActivityVo>> resp = ResponseUtils.successResponse();
        try {
            List<AppActivity> list = activityManager.selecActivitytList(AppActivity.builder().category(Constant.ActivityCategory.GAME.name()).dataStatus(YesNo.Y.name()).build());
            List<ActivityVo> voList = DTOUtils.map(list, ActivityVo.class);
            if (voList.size() > 0) {
                for (ActivityVo vo : voList) {
                    vo.setClickNum((vo.getClickNum() * 10) + new DateTime(vo.getUpdateTime()).getSecondOfMinute() % 9 + 1);
                    if (StringUtils.isNotEmpty(vo.getActiveCode()) && Integer.parseInt(vo.getActiveCode()) % 2 == 0) {
                        vo.setActiveCode(YesNo.Y.name());
                    } else {
                        vo.setActiveCode(YesNo.N.name());
                    }
                }
            }
            resp.setResult(voList);
        } catch (Exception e) {
            logger.warn("queryGame error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.updateClickGame", description = "修改游戏点击量")
    public Response<Boolean> updateClickGame(Long id) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        if (id != null) {
            AppActivity appActivity = activityManager.queryActivityById(id);
            if (appActivity != null) {
                Boolean update = activityManager.updateActivity(AppActivity.builder().id(appActivity.getId()).clickNum((appActivity.getClickNum() + 1)).build());
                resp.setResult(update);
            }
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.queryUserAnnualReportPopup", description = "查询用户的年报账单是否弹窗")
    public Response<Boolean> queryUserAnnualReportPopup() {
        Response<Boolean> response = ResponseUtils.successResponse(false);
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        String deviceId = sessionHelper.getSessionProp(AppSessionConstants.DEVICE_ID, String.class);
        if (partyId == null) {
            response.setResonpse(ResponseEnum.SESSION_INVALD);
            return response;
        }
        Map map = userManager.queryAnnualReport(partyId);
        if (map != null) {
            //查询是否已弹窗，如果
            AppActivityTask task = AppActivityTask.builder().partyId(partyId).taskCode(Constant.ActivityTaskType.ANNUAL_POPUP.name()).taskStatus("Y").useStatus("Y").alertStatus("Y").activityCode(GoldService.ANNUALREPORT_POPUP).build();
            List<AppActivityTask> list = activityManager.queryActivityTask(task, null);
            if (org.apache.commons.collections.CollectionUtils.isEmpty(list)) {//没有弹窗，返回TRUE

                String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
                task.setTaskDailyId(partyId + Constant.ActivityTaskType.ANNUAL_POPUP.name());
                task.setDaily(today);
                task.setTaskName(Constant.ActivityTaskType.ANNUAL_POPUP.desc);
                task.setDeviceId(deviceId);
                activityManager.saveUpdateActivityTask(task);
//                response.setResult(true);
            }
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.queryCycleDays", description = "查询距离大转盘活动天数")
    public Response<Integer> queryCycleDays() {
        Response<Integer> resp = ResponseUtils.successResponse(-1);
        try {
            Long partyId = sessionHelper.getSessionProp(AppSessionConstants.PARTY_ID, Long.class);
            if (partyId != null) {
                String firstDay = SysConfigUtil.getStr("activity_cycle_start_date");
                if (StringUtils.isNotEmpty(firstDay)) {
//                    int days = (int) TimeUnit.MILLISECONDS.toDays(org.apache.commons.lang3.time.DateUtils.parseDate(firstDay, "yyyyMMdd").getTime() - org.apache.commons.lang3.time.DateUtils.parseDate(.convertDate(new Date(), "yyyyMMdd"), "yyyyMMdd").getTime());
                    int days = 1000;
                    ;
                    if (days <= 3 && days > 0) {
                        //查询当前用户当天是否弹窗

                        String deviceId = sessionHelper.getSessionProp(AppSessionConstants.DEVICE_ID, String.class);
                        String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
                        AppActivityTask task = AppActivityTask.builder()
                                .partyId(partyId).daily(today).taskCode(Constant.ActivityTaskType.COUNTDOWN_POPUP.name()).alertStatus(YesNo.Y.name())
                                .activityCode(GoldService.COUNTDOWN_POPUP).taskStatus(YesNo.Y.name()).useStatus(YesNo.Y.name()).build();
                        List<AppActivityTask> list = activityManager.queryActivityTask(task, null);
                        if (org.apache.commons.collections.CollectionUtils.isEmpty(list)) {//没有弹窗，返回TRUE
                            task.setTaskDailyId(today + System.currentTimeMillis());
                            task.setTaskName(Constant.ActivityTaskType.COUNTDOWN_POPUP.desc);
                            task.setDeviceId(deviceId);
                            activityManager.saveUpdateActivityTask(task);
                            resp.setResult(days);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("queryCycleDays error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 根据类型查Banner top
     * 查首页和发现页
     *
     * @return
     */
    @Override
    public Response<List<BannerVo>> queryTopBanner(List<String> types) {
        Response<List<BannerVo>> resp = ResponseUtils.successResponse();
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("orderBy", "TYPE DESC,PRIORITY_LEVEL ASC");
            //Constant.BannerType.toList(Constant.BannerType.INDEX.name(),Constant.BannerType.DISCOVER.name());
            param.put("bannerTypes", types);
            List<BannerVo> bannerVos = bannerManager.indexBannerList(param);
            if (bannerVos != null && bannerVos.size() > 0) {
                for (BannerVo bannerVo : bannerVos) {
                    if (StringUtils.isNotEmpty(bannerVo.getBannerStatus()) && bannerVo.getBannerStatus().equals(Constant.YESNO.NO.code)) {
                        bannerVo.setEndDate(null);
                        bannerVo.setStartDate(null);
                    }
                    bannerVo.setContent(HtmlUtils.htmlUnescape(bannerVo.getContent()));
                }
            }
            resp.setResult(bannerVos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    /**
     * 批量修改Banner top
     *
     * @param bannerVos
     * @return
     */
    @Override
    public Response<Boolean> updateBannerVo(List<BannerVo> bannerVos) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            if (bannerVos != null && bannerVos.size() == 1) {
                AppBanner appBanner = bannerManager.selectByPrimaryKey(bannerVos.get(0).getId());
                if (appBanner == null) {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                    return resp;
                }
                Map<String, Object> param = new HashMap<>();
                param.put("type", bannerVos.get(0).getType());
                List<BannerVo> bannerVoList = bannerManager.indexBannerList(param);
                //查询要修改的记录
                if (bannerVoList != null && bannerVoList.size() >= 9 && !appBanner.getType().equals(bannerVoList.get(0).getType())) {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400057);
                    return resp;
                }
            }
            if (bannerVos.size() > 0) {
                for (int i = 0; i < bannerVos.size(); i++) {
                    AppBanner appBanner = DTOUtils.map(bannerVos.get(i), AppBanner.class);
                    appBanner.setUpdateDate(new Date());
                    String url = SysConfigUtil.getStr("BANNER_URL");
                    if (StringUtils.contains(appBanner.getHrefUrl(), url)) {
                        String hrefUrl = url + "?id=" + appBanner.getId();
                        appBanner.setHrefUrl(hrefUrl);
                    }
                    if (StringUtils.isNotEmpty(appBanner.getBannerStatus()) && appBanner.getBannerStatus().equals(Constant.YESNO.NO.code)) {
                        appBanner.setStartDate(null);
                        appBanner.setEndDate(null);
                    }
                    bannerManager.updateBanner(appBanner);
                }
                //修改排序规则
                updateBannerpriorityLevel();

                resp.setResult(true);
            }
        } catch (Exception e) {
            logger.info("updateBannerVo error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    private void updateBannerpriorityLevel() {
        try {
            /*//发现
            Map<String, Object> param = new HashMap<>();
            param.put("orderBy", "PRIORITY_LEVEL ASC");
            param.put("type", Constant.BannerType.DISCOVER.name());
            List<BannerVo> bannerVos = bannerManager.indexBannerList(param);
            if (bannerVos != null && bannerVos.size() > 0) {
                for (int i = 0; i < bannerVos.size(); i++) {
                    if (bannerVos.get(i).getType().equals(Constant.BannerType.DISCOVER.name())) {
                        bannerVos.get(i).setPriorityLevel((i + 1) * 10);
                        bannerVos.get(i).setUpdateDate(null);
                        bannerManager.updateBanner(DTOUtils.map(bannerVos.get(i), AppBanner.class));
                    }
                }

            }
            //首页
            param.put("type", Constant.BannerType.INDEX.name());
            List<BannerVo> indexBannerList = bannerManager.indexBannerList(param);
            if (indexBannerList != null && indexBannerList.size() > 0) {
                for (int i = 0; i < indexBannerList.size(); i++) {
                    if (indexBannerList.get(i).getType().equals(Constant.BannerType.INDEX.name())) {
                        indexBannerList.get(i).setPriorityLevel((i + 1));
                        indexBannerList.get(i).setUpdateDate(null);
                        bannerManager.updateBanner(DTOUtils.map(indexBannerList.get(i), AppBanner.class));
                    }
                }

            }*/

            Set<String> strings = new HashSet<>();
            strings.add(Constant.BannerType.XL_SQ.name());
            strings.add(Constant.BannerType.XL_SHOP.name());
            strings.add(Constant.BannerType.XL_LIFE.name());
            strings.add(Constant.BannerType.XL_TRAVE.name());
            strings.add(Constant.BannerType.XL_OPE_L.name());
            strings.add(Constant.BannerType.XL_OPE_U.name());
            strings.stream().forEach(vo -> {
                Map<String, Object> params = new HashMap<>();
                params.put("orderBy", "PRIORITY_LEVEL ASC");
                params.put("type", vo);
                List<BannerVo> bannerVos = null;
                try {
                    bannerVos = bannerManager.indexBannerList(params);
                } catch (Exception e) {
                    logger.warn("query bannerVos:", e);
                }
                AtomicInteger atomicInteger = new AtomicInteger(0);
                bannerVos.stream().forEach(v -> {
                    if (v.getType().equals(Constant.BannerType.DISCOVER.name())) {
                        v.setPriorityLevel((atomicInteger.get() + 1) * 10);
                    } else {
                        v.setPriorityLevel((atomicInteger.get() + 1));
                    }
                    v.setUpdateDate(null);
                    bannerManager.updateBanner(DTOUtils.map(v, AppBanner.class));
                    atomicInteger.getAndAdd(1);
                });
            });
        } catch (Exception e) {
            logger.info("updateBannerpriorityLevel error", e);
        }
    }

    /**
     * 新增Banner top
     *
     * @param bannerVo
     * @return
     */
    @Override
    public Response<Boolean> insertBannerVo(BannerVo bannerVo) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            AppBanner appBanner = DTOUtils.map(bannerVo, AppBanner.class);
            if (bannerVo != null && StringUtils.isNotEmpty(bannerVo.getType()) && (appBanner.getType().equals(Constant.BannerType.DISCOVER.name()) || appBanner.getType().equals(Constant.BannerType.INDEX.name()) || appBanner.getType().equals(Constant.BannerType.XL_SQ.name()) || appBanner.getType().equals(Constant.BannerType.XL_SHOP.name()) || appBanner.getType().equals(Constant.BannerType.XL_LIFE.name()) || appBanner.getType().equals(Constant.BannerType.XL_TRAVE.name()) || appBanner.getType().equals(Constant.BannerType.XL_OPE_U.name()) || appBanner.getType().equals(Constant.BannerType.XL_OPE_L.name()) || appBanner.getType().equals(Constant.BannerType.RECRUIT.name()))) { //根据类型查个数
                Map<String, Object> param = new HashMap<>();
                param.put("orderBy", "PRIORITY_LEVEL ASC");
                param.put("type", appBanner.getType());
                List<BannerVo> bannerVos = bannerManager.indexBannerList(param);
                if (bannerVos != null && bannerVos.size() >= 9) {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400057);
                    return resp;
                }
                if (bannerVos != null && bannerVos.size() > 0) {
                    if (appBanner.getType().equals(Constant.BannerType.DISCOVER.name())) {
                        appBanner.setPriorityLevel((bannerVos.size() + 1) * 10);
                    } else if (appBanner.getType().equals(Constant.BannerType.INDEX.name()) || appBanner.getType().equals(Constant.BannerType.XL_SQ.name()) || appBanner.getType().equals(Constant.BannerType.XL_SHOP.name()) || appBanner.getType().equals(Constant.BannerType.XL_LIFE.name()) || appBanner.getType().equals(Constant.BannerType.XL_TRAVE.name()) || appBanner.getType().equals(Constant.BannerType.XL_OPE_U.name()) || appBanner.getType().equals(Constant.BannerType.XL_OPE_L.name())) {
                        appBanner.setPriorityLevel(bannerVos.size() + 1);
                    }
                } else {
                    if (appBanner.getType().equals(Constant.BannerType.INDEX.name()) || appBanner.getType().equals(Constant.BannerType.XL_SQ.name()) || appBanner.getType().equals(Constant.BannerType.XL_SHOP.name()) || appBanner.getType().equals(Constant.BannerType.XL_LIFE.name()) || appBanner.getType().equals(Constant.BannerType.XL_TRAVE.name()) || appBanner.getType().equals(Constant.BannerType.XL_OPE_U.name()) || appBanner.getType().equals(Constant.BannerType.XL_OPE_L.name())) {
                        appBanner.setPriorityLevel(1);
                    } else if (appBanner.getType().equals(Constant.BannerType.DISCOVER.name())) {
                        appBanner.setPriorityLevel(10);
                    }
                }
            }
            Boolean update = bannerManager.insertBannerVo(appBanner);
            String url = SysConfigUtil.getStr("BANNER_URL");
            if (StringUtils.contains(appBanner.getHrefUrl(), url)) {
                String hrefUrl = url + "?id=" + appBanner.getId();
                appBanner.setHrefUrl(hrefUrl);
                bannerManager.updateBanner(appBanner);
            }
            resp.setResult(update);
        } catch (Exception e) {
            logger.info("insertBannerVo error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    public Response<Boolean> deleteBannerVo(Long id) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            if (id != null && id > 0L) {
                AppBanner appBanner = bannerManager.selectByPrimaryKey(id);
                if (appBanner != null) {
                    Boolean update = bannerManager.updateBanner(AppBanner.builder().id(id).isDeleted("Y").build());
                    resp.setResult(update);
                    Map<String, Object> param = new HashMap<>();
                    param.put("type", appBanner.getType());
                    param.put("orderBy", "PRIORITY_LEVEL ASC");
                    List<BannerVo> bannerVoLists = bannerManager.indexBannerList(param);
                    if (bannerVoLists.size() > 0) {
                        for (int i = 0; i < bannerVoLists.size(); i++) {
                            if (appBanner.getType().equals(Constant.BannerType.DISCOVER.name())) {
                                bannerVoLists.get(i).setPriorityLevel((i + 1) * 10);
                            } else if (appBanner.getType().equals(Constant.BannerType.INDEX.name())) {
                                bannerVoLists.get(i).setPriorityLevel(i + 1);
                            }
                            bannerVoLists.get(i).setUpdateDate(null);
                            bannerManager.updateBanner(DTOUtils.map(bannerVoLists.get(i), AppBanner.class));
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.info("deleteBannerVo error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.indexBusinessAllV2", description = "业务全部入口")
    public Response<List<BusinessAllV2>> indexBusinessAllV2(String clientVersion) {
        Response<List<BusinessAllV2>> resp = ResponseUtils.successResponse();
        List<BusinessAllV2> businessAllV2List = new ArrayList<>();
        try {
            String life = "";
            String myapps = null;
            String personService = null;
            String personQuery = null;
            String personLife = null;
            String business_name = SysConfigUtil.getStr("BUSINESS_NAME");


            final Set<String> busiSet = businessManager.queryUserBusiness();
            if (org.apache.commons.collections.CollectionUtils.isEmpty(busiSet) && compareVersion(clientVersion, "3.5.3")) {
                busiSet.clear();
                busiSet.add("VISITOR");
                busiSet.add("MERCHANT");
                busiSet.add("LOGIN");
                busiSet.add("LIVE");
            }

            //村情村貌
            BusinessAllV2 lifeBusiness = new BusinessAllV2();
            lifeBusiness.setBusiness(queryXlLife(clientVersion));
            if (lifeBusiness.getBusiness() != null && lifeBusiness.getBusiness().size() > 0) {
                if (StringUtils.isNotEmpty(business_name)) {
                    LinkedMap object = JSON.parseObject(business_name, LinkedMap.class);
                    lifeBusiness.setBusinessName(object.get("LIFE").toString());
                } else {
                    lifeBusiness.setBusinessName("村情村貌");
                }
                businessAllV2List.add(lifeBusiness);
            }
            if (StringUtils.isNotEmpty(business_name)) {
                LinkedMap object = JSON.parseObject(business_name, LinkedMap.class);
                Set<String> strings = object.keySet();
                businessAllV2List.addAll(strings.stream().map((vo) -> {
                    BusinessAllV2 myappsBusiness = new BusinessAllV2();
                    myappsBusiness.setBusiness(getBusiness(vo, clientVersion, busiSet));
                    if (myappsBusiness.getBusiness() != null && myappsBusiness.getBusiness().size() > 0) {
                        myappsBusiness.setBusinessName(object.get(vo).toString());
                    }
                    return myappsBusiness;
                }).collect(Collectors.toList()));
            }
            resp.setResult(businessAllV2List.stream().filter(vo -> vo.getBusiness().size() > 0).collect(Collectors.toList()));
        } catch (Exception e) {
            logger.info("indexBusinessAllV2 error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 根据id查询banner
     *
     * @param id
     * @return
     */
    @Override
    public Response<BannerVo> queryBannerById(Long id) {
        Response<BannerVo> resp = ResponseUtils.successResponse();
        try {
            AppBanner appBanner = bannerManager.selectByPrimaryKey(id);
            BannerVo bannerVo = DTOUtils.map(appBanner, BannerVo.class);
            bannerVo.setContent(HtmlUtils.htmlUnescape(bannerVo.getContent()));
            resp.setResult(bannerVo);
        } catch (Exception e) {
            logger.info("queryBannerById error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 根据code查询business
     *
     * @param code
     * @return
     */
    @Override
    public Response<BusinessVo> queryBusinessByCode(String code) {
        Response<BusinessVo> resp = ResponseUtils.successResponse();
        try {
            AppBusiness appBusiness = bannerManager.selectBusinessByCode(code);
            BusinessVo businessVo = DTOUtils.map(appBusiness, BusinessVo.class);
            resp.setResult(businessVo);
        } catch (Exception e) {
            logger.info("queryBannerById error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.queryStartPage", description = "查询启动页")
    public Response<BannerVo> queryStartPage() {
        Response<BannerVo> resp = ResponseUtils.successResponse();
        try {
            String supportVersion = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
            if (StringUtils.isEmpty(supportVersion)) {
                String did = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
                AppPush appPush = appPushDAO.selectByDeviceId(did);
                if (appPush != null && StringUtils.isNotEmpty(appPush.getVersion())) {
                    supportVersion = appPush.getVersion();
                }
            }
            final String version = supportVersion;
            Map<String, Object> paras = new HashMap<>();
            paras.put("type", Constant.BannerType.START.name());
            paras.put("startDate", new Date());
            paras.put("endDate", new Date());
            paras.put("bannerStatus", Constant.YESNO.YES.code);
            List<BannerVo> list = bannerManager.indexBannerList(paras);
            Optional<BannerVo> first = list.stream().filter(vo -> StringUtils.isEmpty(vo.getSupportVersion()) || (StringUtils.isNotEmpty(vo.getSupportVersion()) && StringUtils.contains(vo.getSupportVersion(), version))).findFirst();
            resp.setResult(first.orElse(null));
        } catch (Exception e) {
            logger.warn("queryStartPage error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.queryOpeartePosition", description = "查运营位")
    public Response<List<BannerVo>> queryOpeartePosition() {
        Response<List<BannerVo>> response = ResponseUtils.successResponse();
        try {
            List<BannerVo> list = new ArrayList<>();
            Map<String, Object> param = new HashMap<>();
            //查运营位上
            param.put("bannerTypes", Constant.BannerType.toList(Constant.BannerType.OPER_U.name(), Constant.BannerType.OPER_UD.name()));
            List<BannerVo> bannerVoListUP = bannerManager.indexBannerList(param);
            list.add(getOneBannerVo(bannerVoListUP));
            //查运营位下
            param.put("bannerTypes", Constant.BannerType.toList(Constant.BannerType.OPER_L.name(), Constant.BannerType.OPER_LD.name()));
            List<BannerVo> bannerVoListL = bannerManager.indexBannerList(param);
            list.add(getOneBannerVo(bannerVoListL));
            response.setResult(list);
        } catch (Exception e) {
            logger.warn("queryOpeartePosition error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    private BannerVo getOneBannerVo(List<BannerVo> bannerVoList) {
        try {
            String supportVersion = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
            bannerVoList = bannerVoList.stream().map(vo -> {
                if (StringUtils.isEmpty(vo.getNeedLogin())) {
                    vo.setNeedLogin("N");
                }
                return vo;
            }).filter(vo -> StringUtils.isEmpty(vo.getSupportVersion()) || StringUtils.isNotEmpty(vo.getSupportVersion()) && StringUtils.contains(vo.getSupportVersion(), supportVersion)).collect(Collectors.toList());
            if (bannerVoList.size() > 0) {
                return bannerVoList.get(0);
            }
        } catch (Exception e) {
            logger.warn("getOneBannerVo error", e);
        }
        return null;
    }

    @Override
    public Response<Boolean> updateBanner(BannerVo bannerVo) {
        Response<Boolean> response = ResponseUtils.successResponse(false);
        try {
            AppBanner appBanner = DTOUtils.map(bannerVo, AppBanner.class);
            Boolean flag = bannerManager.updateBanner(appBanner);
            response.setResult(flag);
        } catch (Exception e) {
            logger.warn("updateBanner error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.queryIndexMsg", description = "查首页5条新闻")
    public Response<List<MsgVo>> queryIndexMsg() {
        return responseCacheUtils.executeCacheResponse(ResponseCacheUtils.ResponseCacheKey.INDEX_NEWS, "queryIndexMsg", () -> {
            Request<MsgQuery> req = new Request<MsgQuery>();
            MsgQuery msgQuery = new MsgQuery();
            msgQuery.setPageSize(5);
            msgQuery.setStartPage(1);
            req.setReq(msgQuery);
            List<MsgVo> list = messageManager.listNews(req.getReq());
            for (MsgVo vo : list) {
                vo.setUrl(SysConfigUtil.getStr("H5WECHAT_URL") + "/home/nodeManager/topNews.html?id=" + vo.getId());
                vo.setMessage(StringUtils.substring(StringUtils.replacePattern(vo.getMessage(), "<.*?>", ""), 0, 200));
            }
            return list;
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.queryBusinessArticleByqueryKey", description = "搜索应用名")
    public Response<BusinessArticleVo> queryBusinessArticleByqueryKey(ArticleReq articleReq) {
        Response<BusinessArticleVo> resp = ResponseUtils.successResponse();
        BusinessArticleVo businessVos = new BusinessArticleVo();
        List<BusinessVo> businessVoList = new ArrayList<>();
        List<ArticleVo> articleVoList = new ArrayList<>();
        try {
            if (StringUtils.isEmpty(articleReq.getType()) && StringUtils.isEmpty(articleReq.getQueryKey())) {
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            doArticleRecord(articleReq.getType(), articleReq.getQueryKey());
            articleReq.setArticleType(Constant.ArticleType.SUBJECT.name());
            articleReq.setOrderBy("CREATE_TIME DESC");
            articleReq.setGroupId(0L);
            if (articleReq.getType().equals(Constant.BusinessOrArticle.ARTICLE.name())) {  //查微博
                articleVoList = queryArticleVo(articleReq);
            } else if (articleReq.getType().equals(Constant.BusinessOrArticle.ICON.name())) { //查应用
                businessVoList = queryBusinessVo(articleReq.getQueryKey());
            } else if (articleReq.getType().equals(Constant.BusinessOrArticle.ALL.name())) { //查应用和微博
                articleReq.setPageSize(7);
                articleVoList = queryArticleVo(articleReq);
                businessVoList = queryBusinessVo(articleReq.getQueryKey()).stream().limit(5).collect(Collectors.toList());
            }
            businessVos.setBusinessVo(businessVoList);
            businessVos.setArticleVo(articleVoList);
            resp.setResult(businessVos);
        } catch (Exception e) {
            logger.warn("queryBusinessArticleByqueryKey error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private void doArticleRecord(String type, String queryKey) {
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
        String clentVersion = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
        String systemType = sessionHelper.getSessionProp(AppSessionConstants.SYSTEM_TYPE, String.class);
        logManager.saveSearchRecord(AppClientLogSearch.builder().partyId(partyId).deviceId(deviceId).systemType(systemType).version(clentVersion).type(type).content(queryKey).build());
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.indexBusinessV4", description = "首页业务入口查询V4")
    public Response<BusinessV2> indexBusinessV4(String clientVersion) {
        Response<BusinessV2> response = ResponseUtils.successResponse();
        BusinessV2 businessV2 = new BusinessV2();
        try {
            Session session = sessionHelper.getSession();
            session.setAttribute(SessionConstants.CLIENT_VERSION, clientVersion);
            sessionHelper.saveSession(session);
            Set<String> busiSet = businessManager.queryUserBusiness();
            List<BusinessVo> lifeList = queryXlLife(clientVersion);//村务2
            List<BusinessVo> allList = new ArrayList<>();//结果集
            int maxLength = 9;
            int maxFirstLength = 4;
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            List<BusinessVo> userBusiness;
            List<BusinessVo> firstBusiness;
            if (partyId != null && partyId != -1) {//已登录用户
                List<BusinessVo> defaultBusiness = checkBusiness(bannerManager.queryUserBusiness(AppBusiness.builder().partyId(3L).supportVersion(clientVersion).build()), busiSet);
                userBusiness = checkBusiness(bannerManager.queryUserBusiness(AppBusiness.builder().partyId(partyId).supportVersion(clientVersion).build()), busiSet);

                userBusiness.addAll(defaultBusiness);
            } else {//未登录用户
                busiSet = new HashSet<>();
                busiSet.add("LOGIN");
                busiSet.add("VISITOR");
                userBusiness = checkBusiness(bannerManager.queryUserBusiness(AppBusiness.builder().partyId(3L).supportVersion(clientVersion).build()), busiSet);
            }
            allList.addAll(userBusiness);

            //按照优先级查出4个业绩
            firstBusiness = checkBusiness(bannerManager.queryBusiness(AppBusiness.builder().busiCategory(Constant.ConvenientToPersonType.FIRSTMODULES.name()).partyId(0L).supportVersion(clientVersion).build()), busiSet);

            //保证优先级业务只有4个
            firstBusiness = firstBusiness.stream().limit(4).collect(Collectors.toList());
            List<String> codes = firstBusiness.stream().map(vo -> vo.getBusiName()).collect(Collectors.toList());
            //去除4个业绩已经存在的应用
            allList = allList.stream().filter((vo) -> !codes.contains(vo.getBusiName())).collect(Collectors.toList());

            //排重
            List<BusinessVo> resultList = new ArrayList<>();
            String blackList = SysConfigUtil.getStr("INDEX_BUSINESS_BLACK_LIST");
            Set<String> busenessSet = new HashSet<>();
            if (StringUtils.isNotEmpty(blackList)) {
                busenessSet = Arrays.stream(blackList.split(",")).collect(Collectors.toSet());
            }
            for (BusinessVo vo : allList) {
                if (!busenessSet.contains(vo.getBusiName())) {
                    resultList.add(vo);
                    busenessSet.add(vo.getBusiName());
                    if (resultList.size() >= maxLength) {
                        break;
                    }
                }
            }

            BusinessVo businessVo = new BusinessVo();
            businessVo.setBusiName("全部");
            businessVo.setId(0L);
            businessVo.setBusiImage("https://appfile.xianglin.cn/file/1038617");
            businessVo.setBusiIcon("https://cdn02.xianglin.cn/619bcf4e240e20eabd9857403905e9b9-2087.png");
            resultList.add(businessVo);
            businessV2.setBusinessVos(resultList);
            businessV2.setFirstBusinessVos(firstBusiness);
            response.setResult(businessV2);
        } catch (Exception e) {
            logger.warn("indexBusinessV4 error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    public Response<List<BannerVo>> queryOpeartePositionTop() {
        Response<List<BannerVo>> response = ResponseUtils.successResponse();
        try {
            Map<String, Object> param = new HashMap<>();
            //查默认运营位
            param.put("bannerTypes", Constant.BannerType.toList(Constant.BannerType.OPER_UD.name(), Constant.BannerType.OPER_LD.name()));
            List<BannerVo> bannerVoList = bannerManager.indexBannerList(param);
            //查非默认运营位
            param.put("bannerTypes", Constant.BannerType.toList(Constant.BannerType.OPER_U.name(), Constant.BannerType.OPER_L.name()));
            List<BannerVo> bannerVos = bannerManager.indexBannerList(param);
            if (bannerVos != null && bannerVos.size() > 0) {
                bannerVoList.addAll(bannerVos);
            }
            bannerVoList = bannerVoList.stream().map(vo -> {
                if (StringUtils.isEmpty(vo.getNeedLogin())) {
                    vo.setNeedLogin("N");
                }
                return vo;
            }).collect(Collectors.toList());
            response.setResult(bannerVoList);
        } catch (Exception e) {
            logger.warn("queryOpeartePositionTop error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.queryXlQuare", description = "查询乡邻广场")
    public Response<List<BannerVo>> queryXlQuare(String types) {
        return responseCacheUtils.execute(() -> {
            List<BannerVo> bannerV2s = new ArrayList<>();
            String supportVersion = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
            List<String> typesList = Arrays.asList(types.split(","));
            typesList.stream().forEach((vo) -> {
                Response<List<BannerVo>> response = indexBannersV2(supportVersion, vo);
                if (com.alibaba.dubbo.common.utils.CollectionUtils.isNotEmpty(response.getResult())) {
                    if (types.contains(Constant.BannerType.XL_OPE_L.name()) || types.contains(Constant.BannerType.XL_OPE_U.name())) { //乡邻广场运营位上和运营位下只返回一条
                        bannerV2s.add(response.getResult().get(0));
                    } else {
                        bannerV2s.addAll(response.getResult());
                    }
                }
            });
            return bannerV2s;
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.IndexService.queryVoiceBusiness", description = "应用语音搜索")
    public Response<List<BusinessVo>> queryVoiceBusiness(String voiceMsg) {
        return responseCacheUtils.execute(() -> {
            String clientVersion = sessionHelper.getSessionProp(AppSessionConstants.CLIENT_VERSION, String.class);
            Set<String> busiSet = businessManager.queryUserBusiness();
            doArticleRecord(Constant.BusiSupportUser.VOICE.name(),voiceMsg);
            return getBusiness(null,clientVersion,busiSet,Constant.BusiSupportUser.VOICE).stream()
                    .filter(v -> StringUtils.contains(v.getKeyWords(), voiceMsg))
                    .map(v -> BusinessVo.builder().busiName(v.getBusiName()).busiIcon(v.getBusiIcon()).htmlTitle(v.getHtmlTitle())
                            .hrefUrl(v.getHrefUrl()).busiActive(v.getBusiActive()).build()).collect(Collectors.toList());
        });
    }

    /**
     * 根据key查询应用
     *
     * @param queryKey
     * @return
     */
    private List<BusinessVo> queryBusinessVo(String queryKey) {
        final List<BusinessVo> businessVoList = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String supportVersion = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
            Response<List<BusinessAllV2>> listResponse = this.indexBusinessAllV2(supportVersion);
            if (listResponse.getResult().size() > 0) {
                listResponse.getResult().forEach((vo) -> {
                    vo.getBusiness().forEach((in) -> {
                        if (in.getBusiName().contains(queryKey)) {
                            if (partyId == null) {
                                businessVoList.add(in);
                            } else {
                                ids.add(in.getId());
                            }
                        }
                    });
                });
                //登录用户根据id查询后按照点击量排序
                if (partyId != null && ids.size() > 0) {
                    List<AppBusiness> appBusinesses = bannerManager.selectBusiListByIds(AppBusiness.builder().partyId(partyId).ids(ids).build());
                    List<BusinessVo> map = DTOUtils.map(appBusinesses, BusinessVo.class);
                    businessVoList.addAll(map);
                }
            }
        } catch (Exception e) {
            logger.warn("queryBusinessVo error", e);
        }
        return businessVoList;
    }

    /**
     * 根据key分页模糊查询微博内容查6个
     *
     * @param articleReq
     * @return
     */
    private List<ArticleVo> queryArticleVo(ArticleReq articleReq) {
        try {
            articleReq.setIsExcludeShareUrl(false);
            Response<List<ArticleVo>> listResponse = articleService.queryArticleListV4(articleReq);
            if (listResponse.getResult() != null && listResponse.getResult().size() > 0) {
                return listResponse.getResult();
            }
        } catch (Exception e) {
            logger.warn("queryArticleVo error", e);
        }
        return null;
    }


    //将不存在当前地址的默认村删除
    private void deleteVillage(Long partyId, String code) {
        if (partyId != null && partyId > 0) {
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            paras.put("expectDistrictCode", code);
            paras.put("groupType", Constant.GroupType.V.name());
            List<AppGroupMember> members = groupManager.queryAppMemberByParas(paras);
            if (members.size() > 0) {
                for (AppGroupMember member : members) {
                    groupManager.updateGroupMember(AppGroupMember.builder().id(member.getId()).isDeleted("Y").build());
                }
            }
        }
    }


    /**
     * 查询实名认证姓名
     *
     * @param partyId
     * @return
     */
    private String queryRealName(long partyId) {
        String name = null;
        com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> resp2 = customersInfoService.selectCustomsAlready2Auth(partyId);
        if (resp2.getResult() != null) {
            if (StringUtils.isNotEmpty(resp2.getResult().getCustomerName())) {
                name = resp2.getResult().getCustomerName();
            }
        }
        return name;
    }

    private AppArticle getArticle(List<AppArticle> fList, List<AppArticle> gList) {
        AppArticle appArticle = null;
        if (fList.size() == 0 && gList.size() > 0) {
            appArticle = gList.get(0);
        }
        if (gList.size() == 0 && fList.size() > 0) {
            appArticle = fList.get(0);
        }
        if (fList.size() > 0 && gList.size() > 0) {
            //比较两条的时间
            if (fList.get(0).getCreateTime().getTime() > gList.get(0).getCreateTime().getTime()) {
                appArticle = fList.get(0);
            } else {
                appArticle = gList.get(0);
            }
        }
        return appArticle;
    }

    private String getUserName(Long partyId, String nikerName, String trueName) {
        String name = null;
        if (partyId != null) {
            name = "xl" + partyId;
        }
        if (StringUtils.isNotEmpty(nikerName)) {
            name = nikerName;
        }
        if (StringUtils.isNotEmpty(trueName)) {
            name = trueName;
        }
        return name;
    }

    /**
     * 实体类List转换方法
     *
     * @param list
     * @return
     */
    private List<ArticleVo> convertPojo(List<AppArticle> list) {
        List<ArticleVo> result = new ArrayList<>(list.size());
        for (AppArticle article : list) {
            try {

                ArticleVo vo;
                vo = DTOUtils.map(article, ArticleVo.class);
                if (article.getCreateTime() != null) {
                    vo.setDateTime(indecateTime(article.getCreateTime()));
                }
                if (article.getUser() != null) {
                    if (StringUtils.isNotEmpty(article.getUser().getNikerName())) {
                        vo.setNikeName(article.getUser().getNikerName());
                    } else {
                        vo.setNikeName("xl" + article.getPartyId());
                    }
                    if (StringUtils.isNotEmpty(article.getUser().getHeadImg())) {
                        vo.setHeadImg(article.getUser().getHeadImg());
                    } else {
                        vo.setHeadImg(SysConfigUtil.getStr("default_user_headimg"));
                    }
                    vo.setGender(article.getUser().getGender());
                }
                if (article.getReplyUser() != null) {
                    if (StringUtils.isNotEmpty(article.getReplyUser().getNikerName())) {
                        vo.setReplyNickName(article.getReplyUser().getNikerName());
                    } else {
                        vo.setReplyNickName("xl" + article.getReplyPartyId());
                    }
                    if (StringUtils.isNotEmpty(article.getReplyUser().getHeadImg())) {
                        vo.setReplyHeadImg(article.getReplyUser().getHeadImg());
                    } else {
                        vo.setReplyHeadImg(SysConfigUtil.getStr("default_user_headimg"));
                    }
                }
//                // 查询性别
//                PersonReq personReq = new PersonReq();
//                PersonVo pVo = new PersonVo();
//                pVo.setPartyId(article.getPartyId());
//                pVo.setIsDeleted(DeleteEnum.NOT_DEL.getCode());
//                personReq.setVo(pVo);
//                PersonResp personResp = personServiceClient.getPersonByPartyId(personReq);
//                if (ResponseConstants.FacadeEnums.OK.code.equals(personResp.getCode())) {
//                    pVo = personResp.getVo();
//                    vo.setGender(pVo.getGender());
//                }
                result.add(vo);
            } catch (Exception e) {
                logger.error("convertPojo error", e);
            }
        }
        return result;
    }

    /**
     * ArticleVo 对象转换
     *
     * @param list
     * @param userPartyId 当前登陆用户
     * @return
     */
    private List<ArticleVo> convertPojo(List<AppArticle> list, Long userPartyId) {
        List<ArticleVo> result = new ArrayList<>(list.size());
        for (AppArticle article : list) {
            try {
                ArticleVo vo;
                vo = DTOUtils.map(article, ArticleVo.class);
                if (article.getCreateTime() != null) {
                    vo.setDateTime(indecateTime(article.getCreateTime()));
                }
                if (article.getUser() != null) {
                    if (StringUtils.isNotEmpty(article.getUser().getNikerName())) {
                        vo.setNikeName(article.getUser().getNikerName());
                    } else {
                        vo.setNikeName("xl" + article.getPartyId());
                    }
                    if (StringUtils.isNotEmpty(article.getUser().getHeadImg())) {
                        vo.setHeadImg(article.getUser().getHeadImg());
                    } else {
                        vo.setHeadImg(SysConfigUtil.getStr("default_user_headimg"));
                    }
                    //取发表用户的类型
                    vo.setGender(article.getUser().getGender());
                    vo.setUserType(article.getUser().getUserType());
//					if(StringUtils.equals(article.getUser().getUserType(), Constant.UserType.nodeManager.name())){
//						vo.setUserType("村长");
//					}else {
//						vo.setUserType("村户");
//					}
                }
                if (article.getReplyUser() != null) {
                    if (StringUtils.isNotEmpty(article.getReplyUser().getNikerName())) {
                        vo.setReplyNickName(article.getReplyUser().getNikerName());
                    } else {
                        vo.setReplyNickName("xl" + article.getReplyPartyId());
                    }
                    if (StringUtils.isNotEmpty(article.getReplyUser().getHeadImg())) {
                        vo.setReplyHeadImg(article.getReplyUser().getHeadImg());
                    } else {
                        vo.setReplyHeadImg(SysConfigUtil.getStr("default_user_headimg"));
                    }
                }
//                //初始化性别
//                PersonReq personReq = new PersonReq();
//                PersonVo pVo = new PersonVo();
//                pVo.setPartyId(article.getPartyId());
//                pVo.setIsDeleted(DeleteEnum.NOT_DEL.getCode());
//                personReq.setVo(pVo);
//                PersonResp personResp = personServiceClient.getPersonByPartyId(personReq);
//                if (ResponseConstants.FacadeEnums.OK.code.equals(personResp.getCode())) {
//                    pVo = personResp.getVo();
//                    vo.setGender(pVo.getGender());
//                }

                //实名认证信息
                com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> resp2 = customersInfoService.selectCustomsAlready2Auth(article.getPartyId());
                Boolean isAuth = false;
                if (resp2.getResult() != null) {
                    if (StringUtils.isNotEmpty(resp2.getResult().getCustomerName())) {
                        vo.setNikeName(resp2.getResult().getCustomerName());
                    }
                }
                // 查询性别
                if (userPartyId != null) {
                    Map<String, Object> paras = DTOUtils.queryMap();
                    paras.put("pageSize", 100);
                    paras.put("partyId", userPartyId);
                    paras.put("articleId", article.getId());
                    List<AppArticleTip> tips = articleManager.queryArticleTipList(paras);
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(tips)) {
                        for (AppArticleTip tip : tips) {
                            if (StringUtils.equals(tip.getTipType(), Constant.ArticleTipType.SHARE.name())) {
                                vo.setIsShare(Constant.YESNO.YES.code);
                            }
                            if (StringUtils.equals(tip.getTipType(), Constant.ArticleTipType.COLLET.name()) && StringUtils.equals(tip.getTipStatus(), Constant.YESNO.YES.code)) {
                                vo.setIsCollect(Constant.YESNO.YES.code);
                            }
                            if (StringUtils.equals(tip.getTipType(), Constant.ArticleTipType.PRAISE.name()) && StringUtils.equals(tip.getTipStatus(), Constant.YESNO.YES.code)) {
                                vo.setIsPraise(Constant.YESNO.YES.code);
                                vo.setArticleStatus(Constant.YESNO.YES.code);
                            }
                        }
                    }
                }
                result.add(vo);
            } catch (Exception e) {
                logger.error("convertPojo error", e);
            }
        }
        return result;
    }

    /**
     * 转换显示时间类型
     *
     * @param createTime
     * @return
     */
    private String indecateTime(Date createTime) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
        Date yesterday = c.getTime();
        if (fmt.format(createTime).toString().equals(fmt.format(new Date()).toString())) {// 判断如果是今天
            int intervalMinutes = DateUtils.getIntervalMinutes(createTime, new Date());
            if (intervalMinutes < 60) {
                String showTime = intervalMinutes + "分钟前";
                return showTime;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String showTime = sdf.format(createTime);
                return showTime;
            }
        } else if (fmt.format(yesterday).equals(fmt.format(createTime))) {// 判断如果是昨天
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String showTime = "昨天" + sdf.format(createTime);
            return showTime;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String showTime = sdf.format(createTime);
            return showTime;
        }
    }

    public static void main(String[] args) {
        Set<String> busi = new HashSet<>();
        busi.add("LOGIN");
        busi.add("CONTRACT");
//        busi.add("NODE_MANAGER");
        busi.add("NODEMANAGER");
        busi.add("MERCHANT");
        busi.add("EMPLOYEE");
        busi.add("ISC_HK_YLB");


        Set<String> auth = new HashSet<>();
        auth.add("LOGIN");
//        auth.add("CONTRACT");
        auth.add("NODE_MANAGER|EMPLOYEE");
        auth.add("!ISC_HK_YLB");
//        System.out.println(checkAuth(busi, auth));

    }


}
