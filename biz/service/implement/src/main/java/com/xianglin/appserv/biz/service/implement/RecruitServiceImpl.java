package com.xianglin.appserv.biz.service.implement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.xianglin.appserv.biz.shared.*;
import com.xianglin.appserv.biz.shared.util.ResponseCacheUtils;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.app.RecruitService;
import com.xianglin.appserv.common.service.facade.model.AppSessionConstants;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.*;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.RecruitJobReq;
import com.xianglin.appserv.common.service.facade.req.RecruitJobResumeReq;
import com.xianglin.appserv.common.service.facade.req.RecruitResumeReq;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.core.model.exception.AppException;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.model.CustomersDTO;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Describe :
 * Created by xingyali on 2018/4/19 10:58.
 * Update reason :
 */
@Service("recruitService")
@ServiceInterface
public class RecruitServiceImpl implements RecruitService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private RecruitManager recruitManager;

    @Autowired
    private ArticleManager articleManager;

    @Autowired
    private PropExtendManager propExtendManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CustomersInfoService customersInfoService;

    @Autowired
    private ResponseCacheUtils responseCacheUtils;

    @Autowired
    private SysParaManager sysParaManager;

    @Autowired
    private LogManager logManager;
    /**
     * 缩略图
     */
    private final String smallImg = "@!200_200";


    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.queryRecruitJobList", description = "招工列表查询")
    public Response<List<RecruitJobVo>> queryRecruitJobList(RecruitJobReq req) {
        String s = MD5.md5(req.toString());
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        //保存搜索记录
        if (StringUtils.isNotEmpty(req.getKey())) {
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String clentVersion = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
            String systemType = sessionHelper.getSessionProp(AppSessionConstants.SYSTEM_TYPE, String.class);
            logManager.saveSearchRecord(AppClientLogSearch.builder().partyId(partyId).deviceId(deviceId).systemType(systemType).version(clentVersion).type(Constant.SearchType.SEARCH.name()).content(req.getKey()).build());
        }
        return responseCacheUtils.executeCacheResponse(ResponseCacheUtils.ResponseCacheKey.RECRUIT_LIST, s, () -> {
            List<AppRecruitJob> list = new LinkedList<>();
            AppRecruitJob recruitJob = DTOUtils.map(req, AppRecruitJob.class);
            AppDateSectionReq appDateSectionReq = new AppDateSectionReq();
            appDateSectionReq.setEndDay(req.getEndDay());
            appDateSectionReq.setStartDay(req.getStartDay());
            appDateSectionReq.setPageSize(req.getPageSize());
            appDateSectionReq.setStartPage(req.getStartPage());
            String orderBy = null;
            /*/*if (StringUtils.isEmpty(req.getOrderBy())) {
                orderBy = "a.RECOMMEND_SIGN desc,a.OFFICIAL_SIGN desc,a.RECOMMEND_TIME desc";
            } else {
                orderBy = req.getOrderBy();
            }*/
            if(StringUtils.isNotEmpty(req.getOrderBy())){
                orderBy = req.getOrderBy();
            }
            if (req.getPartyId() == null && StringUtils.isEmpty(req.getComments()) && req.getStartPage() <= 1 && (req.getLastId() == null || req.getLastId() <= 0) && !StringUtils.contains(req.getOrderBy(), "RAND()")) {//第一页 //查第一页的数据，查随机条数时不优先查置顶和官方
                recruitJob.setComments("(a.RECOMMEND_SIGN=1 or a.OFFICIAL_SIGN=1)");
                if (StringUtils.isEmpty(req.getOrderBy())) {
                    orderBy = "a.RECOMMEND_SIGN desc,a.RECOMMEND_TIME desc";
                }
                list.addAll(recruitManager.queryRecruitJobList(recruitJob, null, orderBy, req.getJobCategorys(), req.getExpectCitys(), req.getIsCommission()));
                orderBy = null;
                recruitJob.setComments(null);
                recruitJob.setOfficialSign(0);
                recruitJob.setRecommendSign(0);
            }
            //查第二页以上的数据，不包含置顶、官方和查随机条数包含置顶、官方
            if (req.getPartyId() == null && req.getLastId() != null && req.getLastId() > 0 && !StringUtils.contains(req.getOrderBy(), "RAND()")) {
                recruitJob.setOfficialSign(0);
                recruitJob.setRecommendSign(0);
            }
            list.addAll(recruitManager.queryRecruitJobList(recruitJob, appDateSectionReq, orderBy, req.getJobCategorys(), req.getExpectCitys(), req.getIsCommission()));
            List<RecruitJobVo> recruitJobVoList = convertRecruitPojoList(list);
            return recruitJobVoList;
        });
    }

    /**
     * 招工列表转换
     * @param list
     * @return
     */
    private List<RecruitJobVo> convertRecruitPojoList(List<AppRecruitJob> list) {
        List<RecruitJobVo> recruitJobVoList = list.stream().map((vo) -> {
            RecruitJobVo recruitJobVo = convertRecruitPojo(vo);
            return recruitJobVo;
        }).collect(Collectors.toList());
        return recruitJobVoList;
    }


    /**
     * 同步招聘待处理数
     *
     * @param id
     * @return
     */
    private int syncStayApplyNum(Long id) {
        return recruitManager.syncStayApplyNum(id);
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.queryRecruitJobList2", description = "招工列表查询")
    public Response<List<RecruitJobV2>> queryRecruitJobList2(RecruitJobReq recruitJobReq) {

        Response<List<RecruitJobV2>> resp = ResponseUtils.successResponse();
        try {
            Response<List<RecruitJobVo>> listResponse = this.queryRecruitJobList(recruitJobReq);
            if (listResponse.getResult() != null && listResponse.getResult().size() > 0) {
                List<RecruitJobV2> map = DTOUtils.map(listResponse.getResult(), RecruitJobV2.class);
                resp.setResult(map);
            }
        } catch (Exception e) {
            logger.warn("queryRecruitJobList2 error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private RecruitJobVo syncRecruitJob(RecruitJobVo recruitJobVo, Long partyId) {

        try {
            //是否是当前用户发布
            if (partyId != null && partyId.equals(recruitJobVo.getPartyId())) {
                recruitJobVo.setIsPublish(true);
            }
            if (partyId != null) {
                //查个人当前用户的简历数个推荐简历数
                Map<String, Object> map = new HashMap<>();
                map.put("partyId", partyId);
                Map<String, BigDecimal> queryResumeCount = recruitManager.queryPersonalAndRecommedResumeCount(map);
                if (queryResumeCount == null) {
                    recruitJobVo.setIsResume(false);
                    recruitJobVo.setIsRecommendResume(false);
                } else {
                    if (queryResumeCount.get("isResume").compareTo(BigDecimal.ZERO) == 1) {
                        recruitJobVo.setIsResume(true);
                    }
                    if (queryResumeCount.get("isRecommendResume").compareTo(BigDecimal.ZERO) == 1) {
                        recruitJobVo.setIsRecommendResume(true);
                    }
                }
            }
            //招工详情页面如果福利是急招就就为空
            if (StringUtils.equals(recruitJobVo.getWalfare(), "急招")) {
                recruitJobVo.setWalfare(null);
                recruitJobVo.setWalfares(null);
            }
            recruitJobVo.setTitle("来自乡邻app的分享");
            recruitJobVo.setUrl(SysConfigUtil.getStr("RECRUIT_SHARE_URL") + "?id=" + recruitJobVo.getId() + "&articleId=" + recruitJobVo.getArticleId() + "&partyId=" + partyId);
            String imgs = recruitJobVo.getImages();
            if (StringUtils.isNotEmpty(imgs)) {
                String[] imgsarr = imgs.split(";");
                recruitJobVo.setTitieImg(imgsarr[0] + "@!200_200");
            } else {
                recruitJobVo.setTitieImg(SysConfigUtil.getStr("recruitment_default_img") + "@!200_200");
            }
            String contentStr = "@你，快来看看我分享了什么！";
            recruitJobVo.setContent(shareContent(recruitJobVo, contentStr));
            String contentStrPYQ = "你有一条新分享未查看 ";
            recruitJobVo.setContentPYQ(shareContent(recruitJobVo, contentStrPYQ));
        } catch (Exception e) {
            logger.warn("syncRecruitJob error", e);
        }
        return recruitJobVo;
    }

    /**
     * 招工列表总数
     *
     * @param req
     * @return
     */
    @Override
    public Response<Integer> queryRecruitJobCount(RecruitJobReq req) {
        return responseCacheUtils.execute(() -> {
            AppRecruitJob recruitJob = DTOUtils.map(req, AppRecruitJob.class);
            AppDateSectionReq appDateSectionReq = new AppDateSectionReq();
            appDateSectionReq.setEndDay(req.getEndDay());
            appDateSectionReq.setStartDay(req.getStartDay());
            if (StringUtils.isNotEmpty(req.getComments()) && StringUtils.equals("TOP", req.getComments())) {
                recruitJob.setComments(null);
            }
            int count = recruitManager.queryRecruitJobCount(recruitJob, appDateSectionReq, req.getIsCommission());
            return count;
        });
    }

    private String queryRequirement(RecruitJobVo recruitJobVo) {

        StringBuffer stringBuffer = new StringBuffer();
        if (StringUtils.isNotEmpty(recruitJobVo.getGender())) {
            if (stringBuffer.length() > 0) {
                stringBuffer.append("/" + recruitJobVo.getGender());
            } else {
                stringBuffer.append(recruitJobVo.getGender());
            }
        } else {
            stringBuffer.append("男女不限");
        }
        if (StringUtils.isNotEmpty(recruitJobVo.getEducation())) {
            if (stringBuffer.length() > 0) {
                stringBuffer.append("/" + recruitJobVo.getEducation());
            } else {
                stringBuffer.append(recruitJobVo.getEducation());
            }
        } else {
            stringBuffer.append("/学历不限");
        }
        if (StringUtils.isNotEmpty(recruitJobVo.getAgeType())) {
            if (StringUtils.equals(recruitJobVo.getAgeType(), YesNo.N.name())) { //年龄不限
                stringBuffer.append("/年龄不限");
            } else {
                if (recruitJobVo.getAgeStart() != null && recruitJobVo.getAgeEnd() != null) {
                    stringBuffer.append("/"+recruitJobVo.getAgeStart() + "-" + recruitJobVo.getAgeEnd() + "岁");
                }
                if (recruitJobVo.getAgeStart() != null && recruitJobVo.getAgeEnd() == null) {
                    stringBuffer.append("/"+recruitJobVo.getAgeStart() + "岁以上");
                }
            }
        } else {
            stringBuffer.append("/年龄不限");
        }
        return stringBuffer.toString();
    }

    /**
     * 查招工详情
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.queryRecruitJobById", description = "查招工详情")
    public Response<RecruitJobVo> queryRecruitJobById(String type, Long id) {

        Response<RecruitJobVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (id == null || type == null) {
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            AppRecruitJob appRecruitJob = null;
            if (type.equals(Constant.RAType.ARTICLE.name())) {
                appRecruitJob = recruitManager.queryRecruitJobByArticleId(id);
            } else {
                appRecruitJob = recruitManager.queryRecruitJobById(id);
            }
            if (appRecruitJob == null || (appRecruitJob != null && appRecruitJob.getIsDeleted().equals("Y"))) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400071);
                return resp;
            }
            RecruitJobVo recruitJobVo = convertRecruitPojo(appRecruitJob);
            recruitJobVo = syncRecruitJob(recruitJobVo, partyId);
            resp.setResult(recruitJobVo);
        } catch (Exception e) {
            logger.warn("queryRecruitJobById error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private String getAddress(RecruitJobVo recruiJobVo) {

        StringBuffer stringBuffer = new StringBuffer();
        if (StringUtils.isNotEmpty(recruiJobVo.getProvince())) {
            stringBuffer.append(recruiJobVo.getProvince());
        }
        if (StringUtils.isNotEmpty(recruiJobVo.getCity())) {
            stringBuffer.append(recruiJobVo.getCity());
        }
        if (StringUtils.isNotEmpty(recruiJobVo.getCounty())) {
            stringBuffer.append(recruiJobVo.getCounty());
        }
        return stringBuffer.toString();
    }

    /**
     * 发布招工
     *
     * @param recruitJobVo
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.publishRecruitJob", description = "发布招工")
    public Response<Boolean> publishRecruitJob(RecruitJobVo recruitJobVo) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            //String loginName = sessionHelper.getSessionProp(SessionConstants.LOGIN_NAME, String.class);
            if(partyId == null && recruitJobVo.getPartyId() == null){
                 throw new BusiException("用户未登录");
            }else{
                if(partyId == null){
                    partyId = recruitJobVo.getPartyId();
                }
            }
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String md5 = MD5.md5(partyId + JSON.toJSONString(recruitJobVo));
            if (redisUtil.isRepeat(md5, 2)) {
                resp.setFacade(FacadeEnums.ERROR_REPEAT);
                return resp;
            }
            if (recruitJobVo == null) {
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            if (StringUtils.isNotEmpty(recruitJobVo.getContactPhone()) && recruitJobVo.getContactPhone().length() > 11) {
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.ERROR_CHAT_400064);
                return resp;
            }
            recruitJobVo.setPartyId(partyId);
            //发布article
            Long id = insertArticleVo(recruitJobVo);
            if (id == null || id <= 0) {
                resp.setFacade(FacadeEnums.INSERT_FAIL);
                return resp;
            }
            recruitJobVo.setArticleId(id);
            //对象转换

            AreaVo area = recruitJobVo.getArea();

            recruitJobVo.setArea(null);
            AppRecruitJob appRecruitJob = DTOUtils.map(recruitJobVo, AppRecruitJob.class);
            //AppRecruitJob appRecruitJob = convertRecruitJobVo(recruitJobVo);
            if (area != null) {
                appRecruitJob.setArea(area.toString());
            }
            //查询为官方的手机号码
            User user =userManager.queryUser(partyId);
            if(user == null){
                throw new BusiException("用户不存在");              
            }
            String recruitPublishjobUserMobile = SysConfigUtil.getStr("RECRUIT_PUBLISHJOB_USER_MOBILE");
            if(StringUtils.isNotEmpty(recruitPublishjobUserMobile) && StringUtils.contains(recruitPublishjobUserMobile,user.getLoginName())){
                appRecruitJob.setOfficialSign(1);
            }else{
                appRecruitJob.setOfficialSign(0);
            }
            Boolean flag = recruitManager.insertRecruitJob(appRecruitJob);
            resp.setResult(flag);
            //同步用户的家乡地址
            //将发布的地址同步到扩展表
            createUserAddress(appRecruitJob, deviceId);
            if (recruitJobVo.getIsSubject().equals(YesNo.Y.name())) {  //判断是否发布到微博
                AppArticle article = new AppArticle();
                article.setArticleType(Constant.ArticleType.SUBJECT.name());
                if (StringUtils.isEmpty(recruitJobVo.getImages())) {
                    article.setShareImg(SysConfigUtil.getStr("recruitment_default_img") + "?200_200");
                } else {
                    String[] imgs = recruitJobVo.getImages().split(";");
                    article.setShareImg(imgs[0]);
                }
                article.setContactsPhone(recruitJobVo.getContactPhone());
                article.setContacts(recruitJobVo.getCompanyName());
                article.setPartyId(partyId);
                article.setShareUrl("NATIVE:RECRUITMENT:" + id);
                article.setShareTitle(StringUtils.substring(recruitJobVo.getJobName(), 0, 100));
                article.setArticle("");
                article = queryUserAddress(article);
                articleManager.addArticle(article);
            }
            //查询用户的招工草稿，然后清除
            AppPropExtend appPropExtend = propExtendManager.selectProp(partyId, EnumKeyValue.User.class.getSimpleName(), EnumKeyValue.User.USER_RECRUITJOB_DRAFT.name());
            if (appPropExtend != null) {
                propExtendManager.update(AppPropExtend.builder().isDeleted("Y").relationId(partyId).type(EnumKeyValue.User.class.getSimpleName()).id(appPropExtend.getId()).ekey(EnumKeyValue.User.USER_RECRUITJOB_DRAFT.name()).build());
            }
            responseCacheUtils.refreshCache(ResponseCacheUtils.ResponseCacheKey.RECRUIT_LIST);
        } catch (Exception e) {
            logger.warn("publishRecruitJob error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private AppArticle queryUserAddress(AppArticle article) {

        try {
            User user = userManager.queryUser(article.getPartyId());
            if (StringUtils.isNotEmpty(user.getDistrict())) {
                AreaVo areaVo = JSON.parseObject(user.getDistrict(), AreaVo.class);
                if (StringUtils.isNotEmpty(areaVo.getVillage().getName())) {
                    article.setVillage(areaVo.getVillage().getName());
                }
                if (StringUtils.isNotEmpty(areaVo.getProvince().getName())) {
                    article.setProvince(areaVo.getProvince().getName());
                }
                if (StringUtils.isNotEmpty(areaVo.getCounty().getName())) {
                    article.setCounty(areaVo.getCounty().getName());
                }
                if (StringUtils.isNotEmpty(areaVo.getCity().getName())) {
                    article.setCity(areaVo.getCity().getName());
                }
                if (StringUtils.isNotEmpty(areaVo.getTown().getName())) {
                    article.setTown(areaVo.getTown().getName());
                }
            }
        } catch (Exception e) {
            logger.error("queryUserAddress error", e);
        }
        return article;
    }

    private void createUserAddress(AppRecruitJob appRecruitJob, String deviceId) {

        if (appRecruitJob != null) {
            List<MapVo> mapVoList = new ArrayList<>();
            MapVo mapVo = new MapVo();
            mapVo.setKey("province");
            MapVo mapVo2 = new MapVo();
            mapVo2.setKey("city");
            MapVo mapVo3 = new MapVo();
            mapVo3.setKey("county");
            MapVo mapVo4 = new MapVo();
            mapVo4.setKey("town");
            MapVo mapVo5 = new MapVo();
            mapVo5.setKey("village");
            if (StringUtils.isNotEmpty(appRecruitJob.getProvince())) {
                mapVo.setValue(appRecruitJob.getProvince());
            } else {
                mapVo.setValue("");
            }
            if (StringUtils.isNotEmpty(appRecruitJob.getCity())) {
                mapVo2.setValue(appRecruitJob.getCity());
            } else {
                mapVo2.setValue("");
            }
            if (StringUtils.isNotEmpty(appRecruitJob.getCounty())) {
                mapVo3.setValue(appRecruitJob.getCounty());
            } else {
                mapVo3.setValue("");
            }
            mapVoList.add(mapVo);
            mapVoList.add(mapVo2);
            mapVoList.add(mapVo3);
            mapVoList.add(mapVo4);
            mapVoList.add(mapVo5);
            if (mapVoList.size() > 0) { //将List转成json字符串存到扩展表
                String locationJson = JSONArray.fromObject(mapVoList).toString();
                //查询该地址是否在扩展表里面存。存在就修改一下时间，不存在就新增一条
                Map<String, Object> paras = new HashMap<>();
                paras.put("relationId", appRecruitJob.getPartyId());
                paras.put("type", EnumKeyValue.User.class.getSimpleName());
                paras.put("ekey", EnumKeyValue.User.USER_USED_ADDRESS.name());
                paras.put("value", locationJson);
                List<AppPropExtend> appPropExtends = propExtendManager.queryChannel(paras);
                if (appPropExtends.size() > 0) {     //修改时间
                    propExtendManager.update(AppPropExtend.builder().id(appPropExtends.get(0).getId()).build());
                } else {   //新增一条
                    propExtendManager.insert(AppPropExtend.builder().relationId(appRecruitJob.getPartyId()).ekey(EnumKeyValue.User.USER_USED_ADDRESS.name()).deviceId(deviceId).value(locationJson).type(EnumKeyValue.User.class.getSimpleName()).build());
                }

            }
        }
    }

    private Long insertArticleVo(RecruitJobVo recruitJobVo) {

        AppArticle appArticle = AppArticle.builder().partyId(recruitJobVo.getPartyId()).groupId(0L).articleType(Constant.ArticleType.RECRUITMENT.name()).article(recruitJobVo.getDesc()).articleImgs(recruitJobVo.getImages()).contacts(recruitJobVo.getContactName()).contactsPhone(recruitJobVo.getContactPhone()).visibleLevel(7).province(recruitJobVo.getProvince()).city(recruitJobVo.getCity()).county(recruitJobVo.getCounty()).build();
        articleManager.addArticle(appArticle);
        return appArticle.getId();
    }


    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.publishRecruitJobResume", description = "投递简历")
    public Response<Boolean> publishRecruitJobResume(Long jobId, Long resumeId) {

        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String md5 = MD5.md5(partyId + JSON.toJSONString(jobId + resumeId));
            if (redisUtil.isRepeat(md5, 1)) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400068);
                return resp;
            }
            if (jobId == null || resumeId == null) {
                resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }

            //查简历或职位是否存在
            AppRecruitJob appRecruitJob = recruitManager.queryRecruitJobById(jobId);
            AppRecruitResume appRecruitResume = recruitManager.queryRecruitResumeById(resumeId);
            if (appRecruitJob == null || appRecruitResume == null || (appRecruitJob != null && appRecruitJob.getIsDeleted().equals("Y"))) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                return resp;
            }

            //查询简历是否投递过这个职位
            List<AppRecruitJobResume> appRecruitJobResumes = recruitManager.queryJobResume(AppRecruitJobResume.builder().recruitId(resumeId).jobId(jobId).build(), null);
            if (appRecruitJobResumes != null && appRecruitJobResumes.size() > 0) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400060);
                return resp;
            }

            //判断简历是否有姓名或性别，如果没有简历就不存在
            if (appRecruitResume != null && (StringUtils.isEmpty(appRecruitResume.getName()) || StringUtils.isEmpty(appRecruitResume.getGender()))) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400062);
                return resp;
            }
            //判断发布的职位的用户是否是自己
            if (appRecruitJob.getPartyId().equals(partyId)) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400059);
                return resp;
            }
            AppRecruitJobResume appRecruitJobResume = AppRecruitJobResume.builder().jobId(jobId).partyId(partyId).recruitId(resumeId).status("H").build();
            if (appRecruitResume.getType().equals(RecruitEnum.ResumeType.RECOMMEND.name())) {
                if (appRecruitJob.getCommission() == null){
                    appRecruitJobResume.setCommission(0);
                }else {
                    appRecruitJobResume.setCommission(appRecruitJob.getCommission());
                }
            } else {
                appRecruitJobResume.setCommission(0);
            }
            Boolean flag = recruitManager.insertRecruitJobResume(appRecruitJobResume);
            resp.setResult(flag);
            //更新简历最近一次投递的工作、最近一次投递简历时间、投递次数
            appRecruitResume.setRecentJob(appRecruitJob.getJobName());
            appRecruitResume.setRecentJobTime(new Date());
            appRecruitResume.setAge(appRecruitResume.getAge());
            recruitManager.updateRecruitResume(appRecruitResume);
            //更新招聘投递数量
            recruitManager.updateRecruitJobApplyCount(jobId);
            //保存岗位申请进度
            recruitManager.insertAppRecruitJobResumeTip(AppRecruitJobResumeTip.builder().tip(RecruitEnum.EmploymentType.getTip(appRecruitJobResume.getStatus())).jobResumeId(appRecruitJobResume.getId()).build());

            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            //工作存入扩展表
            propExtendManager.insert(AppPropExtend.builder().relationId(appRecruitJobResume.getId()).deviceId(deviceId).type(EnumKeyValue.Recruit.class.getSimpleName()).ekey(EnumKeyValue.Recruit.JOB_PROP_EXTEND.name()).value(JSONObject.toJSONString(appRecruitJob)).build());
            //简历存入扩展表
            propExtendManager.insert(AppPropExtend.builder().relationId(appRecruitJobResume.getId()).deviceId(deviceId).type(EnumKeyValue.Recruit.class.getSimpleName()).ekey(EnumKeyValue.Recruit.RESUME_PROP_EXTEND.name()).value(JSONObject.toJSONString(appRecruitResume)).build());
        } catch (Exception e) {
            logger.error("publishRecruitJobResume error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 发布个人简历、推荐简历、求职意向
     *
     * @param recruitResumeVo
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.publishRecruitResume", description = "发布个人简历、推荐简历、求职意向")
    public Response<RecruitResumeVo> publishRecruitResume(RecruitResumeVo recruitResumeVo) {

        Response<RecruitResumeVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String md5 = MD5.md5(partyId + JSON.toJSONString(recruitResumeVo));
            if (redisUtil.isRepeat(md5, 5)) {
                resp.setFacade(FacadeEnums.ERROR_REPEAT);
                return resp;
            }
            if (recruitResumeVo == null) {
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            //判断手机号或推荐码是否存在
            if (StringUtils.isNotEmpty(recruitResumeVo.getRecommendPhone())) {
                if (!isExistPhoneorInvitationCode(recruitResumeVo.getRecommendPhone())) {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400063);
                    return resp;
                }
            }
            AppRecruitResume appRecruitResume = DTOUtils.map(recruitResumeVo, AppRecruitResume.class);
            String job = getJobAndAdress(recruitResumeVo).get("job").toString();
            String city = getJobAndAdress(recruitResumeVo).get("city").toString();
            if (StringUtils.isNotEmpty(job)) {
                appRecruitResume.setExpectJob(job);
            }
            if (StringUtils.isNotEmpty(city)) {
                appRecruitResume.setExpectCity(city);
            }
            //根据id直接修改数据
            if (appRecruitResume.getId() != null && !appRecruitResume.getId().equals(0L)) {
                AppRecruitResume recruitResume = recruitManager.queryRecruitResumeById(appRecruitResume.getId());
                //查数据是否存在
                if (recruitResume == null) {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                    return resp;
                }
                appRecruitResume.setAge(recruitResume.getAge());
                if (recruitResume.getDeliveryCount() != null) {
                    appRecruitResume.setDeliveryCount(appRecruitResume.getDeliveryCount());
                }
                if (StringUtils.isNotEmpty(appRecruitResume.getPayType())) {
                    recruitResumeVo.setPayType(appRecruitResume.getPayType());
                    if (StringUtils.equals(appRecruitResume.getPayType(),RecruitEnum.ResumePayType.F.name()) || StringUtils.equals(appRecruitResume.getPayType(),RecruitEnum.ResumePayType.N.name())){
                        appRecruitResume.setPayStart(0);
                        appRecruitResume.setPayEnd(0);
                    }else {
                        appRecruitResume.setPayStart(recruitResumeVo.getPayStart());
                        appRecruitResume.setPayEnd(recruitResumeVo.getPayEnd());
                    }
                }
                recruitManager.updateRecruitResume(appRecruitResume);
                resp.setResult(recruitResumeVo);
                return resp;
            }
            Map<String, Object> param = new HashMap<>();
            param.put("type", recruitResumeVo.getType());
            param.put("partyId", partyId);
            //查个人简历是否存在，个人简历存在就修改，不存在就新增
            List<AppRecruitResume> appRecruitResumes = recruitManager.queryRecruitResume(param);

            if (appRecruitResumes != null && appRecruitResumes.size() > 0 && recruitResumeVo.getType().equals(RecruitEnum.ResumeType.PSERSONAL.name())) {//个人简历
                //更新简历
                appRecruitResume.setId(appRecruitResumes.get(0).getId());
                appRecruitResume.setPartyId(partyId);
                if (StringUtils.isNotEmpty(recruitResumeVo.getCertificatesNumber())) {
                    appRecruitResume.setAge(queryAge(recruitResumeVo.getCertificatesNumber()));
                } else {
                    appRecruitResume.setAge(appRecruitResumes.get(0).getAge());
                }
                if (appRecruitResumes.get(0).getDeliveryCount() != null) {
                    appRecruitResume.setDeliveryCount(appRecruitResume.getDeliveryCount());
                }
                Boolean flag = recruitManager.updateRecruitResume(appRecruitResume);
                if (flag) {
                    recruitResumeVo.setId(appRecruitResumes.get(0).getId());
                }
            } else {
                appRecruitResume.setPartyId(partyId);
                if (StringUtils.isNotEmpty(recruitResumeVo.getCertificatesNumber())) {
                    appRecruitResume.setAge(queryAge(recruitResumeVo.getCertificatesNumber()));
                }
                appRecruitResume.setManageStatus("N");
                Boolean flag = recruitManager.insertRecruitResume(appRecruitResume);
                if (flag) {
                    recruitResumeVo.setId(appRecruitResume.getId());
                }
            }
            resp.setResult(recruitResumeVo);
        } catch (Exception e) {
            logger.error("publishRecruitResume error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    //判断手机号或验证码是否存在
    private Boolean isExistPhoneorInvitationCode(String recommendPhone) {

        if (StringUtils.isNotEmpty(recommendPhone)) {
            if (recommendPhone.length() == 11) {   //手机号
                User user = userManager.getUserByLoginAccount(recommendPhone);
                if (user == null) {
                    return false;
                }
            } else if (recommendPhone.length() == 6) {  //推荐码
                logger.info("查推荐码 info", recommendPhone);
                com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> customersDTOResponse = customersInfoService.selectByInvitationCode(recommendPhone);
                logger.info("customersDTOResponse info", customersDTOResponse.toString());
                if (customersDTOResponse == null || customersDTOResponse.getResult() == null) {
                    return false;
                }
            } else { //推荐码或手机号不存在
                return false;
            }
        }
        return true;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.queryRecruitResumeById", description = "根据id查询个人简历/推荐简历/求职意向详情")
    public Response<RecruitResumeVo> queryRecruitResumeById(Long id) {

        Response<RecruitResumeVo> resp = ResponseUtils.successResponse();
        try {
            if (id == null) {
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            AppRecruitResume appRecruitResume = recruitManager.queryRecruitResumeById(id);
            if(appRecruitResume == null){
                resp.setFacade(FacadeEnums.ERROR_CHAT_400072);
                return resp;
            }
            RecruitResumeVo recruitResumeVo = DTOUtils.map(appRecruitResume, RecruitResumeVo.class);
            //地址转换
            recruitResumeVo = queryRecruitResumeVo(recruitResumeVo);
            resp.setResult(recruitResumeVo);
        } catch (Exception e) {
            logger.error("queryRecruitResumeById error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.queryRecruitResumeListByType", description = "根据类型查当前用户的个人简历/推荐简历/求职意向")
    public Response<List<RecruitResumeVo>> queryRecruitResumeListByType(String type, PageReq req) {

        Response<List<RecruitResumeVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (type == null) {
                resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            String resumeType = null;
            if (type.equals(RecruitEnum.ResumeType.INTENTION.name())) {
                resumeType = RecruitEnum.ResumeType.PSERSONAL.name();
            } else {
                resumeType = type;
            }
            Map<String, Object> param = new HashMap<>();
            param.put("partyId", partyId);
            param.put("type", resumeType);
            param.put("startPage", req.getStartPage());
            param.put("pageSize", req.getPageSize());
            List<AppRecruitResume> appRecruitResumes = recruitManager.queryRecruitResume(param);
            List<RecruitResumeVo> recruitResumeVos = new ArrayList<>();
            for (AppRecruitResume appRecruitResume : appRecruitResumes) {
                RecruitResumeVo recruitResumeVo = DTOUtils.map(appRecruitResume, RecruitResumeVo.class);
                recruitResumeVo = queryRecruitResumeVo(recruitResumeVo);
                if (StringUtils.isNotEmpty(recruitResumeVo.getExpectJob()) || StringUtils.isNotEmpty(recruitResumeVo.getPayType())) {
                    recruitResumeVo.setIsIntention(true);
                }
                //查个人简历或推荐简历时判断是否有简历，如果有就返回
                if ((type.equals(RecruitEnum.ResumeType.PSERSONAL.name()) || type.equals(RecruitEnum.ResumeType.RECOMMEND.name())) && StringUtils.isNotEmpty(appRecruitResume.getName())) {
                    recruitResumeVos.add(recruitResumeVo);
                }
                //如果是求职意向就直接返回
                if (type.equals(RecruitEnum.ResumeType.INTENTION.name())) {
                    recruitResumeVos.add(recruitResumeVo);
                    break;
                }
            }
            resp.setResult(recruitResumeVos);
        } catch (Exception e) {
            logger.error("queryRecruitResumeListByType error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private RecruitResumeVo queryRecruitResumeVo(RecruitResumeVo recruitResumeVo) {

        if (recruitResumeVo == null) {
            return recruitResumeVo;
        }
        if (StringUtils.isNotEmpty(recruitResumeVo.getLocation())) {
            try {
                if (StringUtils.isNotEmpty(recruitResumeVo.getLocation())) {
                    recruitResumeVo.setLocationStitch(recruitResumeVo.getLocation());
                }
                AreaVo areaVo = JSON.parseObject(recruitResumeVo.getLocation(), AreaVo.class);
                recruitResumeVo.setLocationStitch(areaVo.selectName().replace("·", ""));
            } catch (Exception e) {
                logger.error("areaVo error", e);
            }
        }
        if (StringUtils.isNotEmpty(recruitResumeVo.getDistrict())) {
            try {
                if (StringUtils.isNotEmpty(recruitResumeVo.getDistrict())) {
                    recruitResumeVo.setDistrictStitch(recruitResumeVo.getDistrict());
                }
                AreaVo areaVo = JSON.parseObject(recruitResumeVo.getDistrict(), AreaVo.class);
                recruitResumeVo.setDistrictStitch(areaVo.selectName().replace("·", ""));
            } catch (Exception e) {
                logger.error("areaVo error", e);
            }
        }
        User user = userManager.queryUser(recruitResumeVo.getPartyId());
        if (user != null) {
            if (StringUtils.isNotEmpty(user.getShowName())) {
                recruitResumeVo.setShowName(user.getShowName());
            } else {
                recruitResumeVo.setShowName(user.showName());
            }
        } else {
            recruitResumeVo.setShowName("xl" + recruitResumeVo.getPartyId());
        }
        return recruitResumeVo;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.updateRecruitResume", description = "修改简历和被推荐人简历")
    public Response<Boolean> updateRecruitResume(RecruitResumeVo recruitResumeVo) {

        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (recruitResumeVo == null) {
                resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            //判断手机号或推荐码是否存在
            if (StringUtils.isNotEmpty(recruitResumeVo.getRecommendPhone())) {
                if (!isExistPhoneorInvitationCode(recruitResumeVo.getRecommendPhone())) {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400063);
                    return resp;
                }
            }
            //查数据是否存在
            AppRecruitResume appRecruitResume = recruitManager.queryRecruitResumeById(recruitResumeVo.getId());
            if (appRecruitResume == null) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                return resp;
            }
            //查当前登录的partyID是否和发布人的partyid一致
            if (!partyId.equals(appRecruitResume.getPartyId())) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400065);
                return resp;
            }
            //查年龄
            if (StringUtils.isNotEmpty(recruitResumeVo.getCertificatesNumber())) {
                recruitResumeVo.setAge(queryAge(recruitResumeVo.getCertificatesNumber()));
            } else {
                recruitResumeVo.setAge(appRecruitResume.getAge());
            }
            if (appRecruitResume.getDeliveryCount() != null) {
                recruitResumeVo.setDeliveryCount(appRecruitResume.getDeliveryCount());
            }
            if (StringUtils.isNotEmpty(appRecruitResume.getPayType())) {
                recruitResumeVo.setPayType(appRecruitResume.getPayType());
            }
            if (appRecruitResume.getPayStart() != null) {
                recruitResumeVo.setPayStart(appRecruitResume.getPayStart());
            }
            if (appRecruitResume.getPayEnd() != null) {
                recruitResumeVo.setPayEnd(appRecruitResume.getPayEnd());
            }

            Boolean flag = recruitManager.updateRecruitResume(DTOUtils.map(recruitResumeVo, AppRecruitResume.class));
            resp.setResult(flag);
        } catch (Exception e) {
            logger.error("updateRecruitResume error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.isShowRecruitJob", description = "返回是否显示我的招聘、我的求职、我的推荐")
    public Response<Map<String, Object>> isShowRecruitJob() {

        Response<Map<String, Object>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> param = new HashMap<>();
            //查招聘
            int recruitJobCount = recruitManager.queryRecruitJobCount(AppRecruitJob.builder().partyId(partyId).build(), null, null);
            param.put("partyId", partyId);
            //查求职
            param.put("type", RecruitEnum.ResumeType.PSERSONAL.name());
            List<AppRecruitJob> jobList = recruitManager.queryJobRecommendList(param);
            //查推荐
            param.put("type", RecruitEnum.ResumeType.RECOMMEND.name());
            List<AppRecruitJob> recommendList = recruitManager.queryJobRecommendList(param);
            param.clear();
            //查是否有求职意向
            List<AppRecruitResume> appRecruitResumes = recruitManager.queryResumeByParam(AppRecruitResume.builder().partyId(partyId).type(RecruitEnum.ResumeType.PSERSONAL.name()).build(), null, null, null);
            if (appRecruitResumes != null && appRecruitResumes.size() > 0 && (StringUtils.isNotEmpty(appRecruitResumes.get(0).getPayType()))) {
                param.put("isIntention", true);
            } else {
                param.put("isIntention", false);
            }
            if (recruitJobCount > 0) {
                param.put("isShowRecruit", true);
            } else {
                param.put("isShowRecruit", false);
            }
            if (jobList != null && jobList.size() > 0) {
                param.put("isShowJob", true);
            } else {
                param.put("isShowJob", false);
            }
            if (recommendList != null && recommendList.size() > 0) {
                param.put("isShowRecommend", true);
            } else {
                param.put("isShowRecommend", false);
            }
            resp.setResult(param);
        } catch (Exception e) {
            logger.error("isShowRecruitJob error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.queryJobRecommend", description = "查询当前用户的求职或推荐")
    public Response<List<RecruitJobVo>> queryJobRecommend(String type, PageReq req) {

        Response<List<RecruitJobVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (type == null) {
                resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            Map<String, Object> param = new HashMap<>();
            param.put("partyId", partyId);
            param.put("type", type);
            param.put("startPage", req.getStartPage());
            param.put("pageSize", req.getPageSize());
            List<AppRecruitJob> jobList = recruitManager.queryJobRecommendList(param);
            List<RecruitJobVo> recruitJobVoList = new ArrayList<>();
            if (jobList != null && jobList.size() > 0) {
                for (AppRecruitJob appRecruitJob : jobList) {
                    //RecruitJobVo recruitJobVo = DTOUtils.map(appRecruitJob, RecruitJobVo.class);
                    RecruitJobVo recruitJobVo = convertRecruitPojo(appRecruitJob);
                    recruitJobVoList.add(recruitJobVo);
                }
            }
            resp.setResult(recruitJobVoList);
        } catch (Exception e) {
            logger.error("queryJobRecommend error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private RecruitJobVo getWelfareArray(RecruitJobVo recruitJobVo) {

        try {
            if (recruitJobVo != null) {
                //福利转数组
                if (StringUtils.isNotEmpty(recruitJobVo.getWalfare())) {
                    String[] walfare = recruitJobVo.getWalfare().split(",");
                    recruitJobVo.setWalfares(walfare);
                }
                //图片转数组
                if (StringUtils.isNotEmpty(recruitJobVo.getImages())) {
                    String[] image = recruitJobVo.getImages().split(";");
                    recruitJobVo.setImage(image);
                    String[] smallImage = new String[image.length];
                    for (int i = 0; i < image.length; i++) {
                        smallImage[i] = image[i] + smallImg;
                    }
                    recruitJobVo.setSmallImage(smallImage);
                }
            }
        } catch (Exception e) {
            logger.error("getWelfareArray error", e);
        }
        return recruitJobVo;
    }

    /**
     * 删除招工
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.deleteRecruitJob", description = "删除招工")
    public Response<Boolean> deleteRecruitJob(Long id) {

        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (id == null) {
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            AppRecruitJob appRecruitJob = recruitManager.queryRecruitJobById(id);
            if (appRecruitJob == null || (appRecruitJob != null && appRecruitJob.getIsDeleted().equals("Y"))) {
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.ERROR_CHAT_400071);
                return resp;
            }
            if (partyId != null && !partyId.equals(appRecruitJob.getPartyId())) {
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.ERROR_CHAT_400066);
                return resp;
            }

            Boolean flag = recruitManager.updateRecruitJob(AppRecruitJob.builder().id(id).isDeleted("Y").build());
            // 动态逻辑删除处理
            AppArticle appArticle = articleManager.queryArticle(appRecruitJob.getArticleId());
            if (appArticle != null) {
                appArticle.setIsDeleted("Y");
                articleManager.updateArticle(appArticle);
            }
            resp.setResult(flag);
            responseCacheUtils.refreshCache(ResponseCacheUtils.ResponseCacheKey.RECRUIT_LIST);
        } catch (Exception e) {
            logger.error("deleteRecruitJob error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.deleteResruitResume", description = "删除简历")
    public Response<Boolean> deleteResruitResume(Long id) {

        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (id == null) {
                resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            AppRecruitResume appRecruitResume = recruitManager.queryRecruitResumeById(id);
            if (appRecruitResume == null) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                return resp;
            }
            if (!partyId.equals(appRecruitResume.getPartyId())) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400067);
                return resp;
            }
            if (appRecruitResume.getType().equals(RecruitEnum.ResumeType.PSERSONAL.name())) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400061);
                return resp;
            }
            Boolean flag = recruitManager.updateRecruitResume(AppRecruitResume.builder().id(id).isDeleted("Y").build());
            resp.setResult(flag);
        } catch (Exception e) {
            logger.error("deleteResruitResume error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 修改招聘
     *
     * @param recruitJobVo
     * @return
     */
    @Override
    public Response<Boolean> updateRecruitJob(RecruitJobVo recruitJobVo) {

        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            if (recruitJobVo == null) {
                resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            AppRecruitJob appRecruitJob1 = recruitManager.queryRecruitJobById(recruitJobVo.getId());
            if (appRecruitJob1 == null) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400071);
                return resp;
            }
            AppRecruitJob appRecruitJob = DTOUtils.map(recruitJobVo, AppRecruitJob.class);
            if (appRecruitJob != null && appRecruitJob.getRecommendSign() != null) {
                appRecruitJob.setRecommendTime(new Date());
            }
            Boolean flag = recruitManager.updateRecruitJob(appRecruitJob);
            resp.setResult(flag);
            responseCacheUtils.refreshCache(ResponseCacheUtils.ResponseCacheKey.RECRUIT_LIST);
        } catch (Exception e) {
            logger.error("deleteRecruitJob error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }

        return resp;
    }


    /**
     * 根据招聘id查询投递的简历
     *
     * @param recruitJobResumeReq
     * @return
     */
    @Override
    public Response<List<RecruitResumeVo>> queryRecruitResumeListByJobResume(RecruitJobResumeReq recruitJobResumeReq) {
        Response<List<RecruitResumeVo>> resp = ResponseUtils.successResponse();
        try {
            if (recruitJobResumeReq == null) {
                resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            AppRecruitJob appRecruitJob = recruitManager.queryRecruitJobById(recruitJobResumeReq.getJobId());
            if (appRecruitJob == null) { //岗位已经被删除
                resp.setFacade(FacadeEnums.ERROR_CHAT_400071);
                return resp;
            }
            Map<String, Object> param = new HashMap<>();
            param.put("jobId", recruitJobResumeReq.getJobId());
            param.put("startPage", recruitJobResumeReq.getStartPage());
            param.put("pageSize", recruitJobResumeReq.getPageSize());
            List<AppRecruitJobResume> appRecruitJobResumes = recruitManager.queryRecruitJobResumeList(param);
            List<RecruitResumeVo> recruitResumes = appRecruitJobResumes.stream().map((vo) -> {
                AppRecruitResume appRecruitResume = queryResumePropExtend(vo.getId(), vo.getRecruitId());
                RecruitResumeVo recruitResumeVo = new RecruitResumeVo();
                recruitResumeVo.setStatus(vo.getStatus());
                if (appRecruitResume == null) {
                    appRecruitResume = recruitManager.queryRecruitResumeById(vo.getRecruitId());
                    if (appRecruitResume == null) {
                        recruitResumeVo.setId(vo.getRecruitId());
                        return recruitResumeVo;
                    }
                }
                recruitResumeVo = DTOUtils.map(appRecruitResume, RecruitResumeVo.class);
                //地址转换
                recruitResumeVo = queryRecruitResumeVo(recruitResumeVo);
                //转换期望工作
                if(StringUtils.isNotEmpty(recruitResumeVo.getExpectCity())){
                    recruitResumeVo.setExpectCity(convertExpectCity(recruitResumeVo.getExpectCity()));  
                }

                //查询年龄
                if (StringUtils.isNotEmpty(recruitResumeVo.getCertificatesNumber())) {
                    recruitResumeVo.setAge(queryAge(recruitResumeVo.getCertificatesNumber()));
                }
                //查询录用状态
                List<AppRecruitJobResume> recruitJobResumes = recruitManager.queryJobResume(AppRecruitJobResume.builder().jobId(recruitJobResumeReq.getJobId()).recruitId(appRecruitResume.getId()).build(), null);
                if (recruitJobResumes != null && recruitJobResumes.size() > 0) {
                    recruitResumeVo.setStatus(recruitJobResumes.get(0).getStatus());
                }
                //查询用户是否投递过改职位的简历
                List<AppRecruitJobResume> jobResumes = recruitManager.queryJobResume(AppRecruitJobResume.builder().jobId(recruitJobResumeReq.getJobId()).partyId(appRecruitResume.getPartyId()).build(), null);
                if (jobResumes != null && jobResumes.size() > 0) {
                    recruitResumeVo.setIsApply(true);
                }
                recruitResumeVo.setCreateTime(vo.getCreateTime());
                return recruitResumeVo;
            }).collect(Collectors.toList());
            resp.setResult(recruitResumes);
        } catch (Exception e) {
            logger.warn("queryRecruitResumeListByJobResume error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 根据招聘id查询投递的简历总数
     *
     * @param recruitJobResumeReq
     * @return
     */
    @Override
    public Response<Integer> queryRecruitResumeCountByJobResume(RecruitJobResumeReq recruitJobResumeReq) {

        Response<Integer> resp = ResponseUtils.successResponse();
        try {
            AppRecruitJob appRecruitJob = recruitManager.queryRecruitJobById(recruitJobResumeReq.getJobId());
            if (appRecruitJob == null) { //岗位已经被删除
                resp.setFacade(FacadeEnums.ERROR_CHAT_400071);
                return resp;
            }
            int count = recruitManager.queryRecruitJobResumeCount(AppRecruitJobResume.builder().jobId(recruitJobResumeReq.getJobId()).build());
            resp.setResult(count);
        } catch (Exception e) {
            logger.warn("queryRecruitResumeCountByJobResume error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private int queryAge(String certificatesNumber) {

        int age = 0;
        if (StringUtils.isNotEmpty(certificatesNumber)) {
            int leh = certificatesNumber.length();
            String dates = "";
            //int se = Integer.valueOf(IdNO.substring(leh - 1)) % 2;
            if (leh == 18) {
                dates = certificatesNumber.substring(6, 10);
            } else {
                dates = "19" + certificatesNumber.substring(6, 8);
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year = df.format(new Date());
            age = Integer.parseInt(year) - Integer.parseInt(dates);
        }
        if (age < 0 || age > 100) {
            age = 0;
        }
        return age;
    }

    /**
     * 修改简历投递状态
     *
     * @param recruitJobResumeVo
     * @return
     */
    @Override
    public Response<Boolean> updateJobResumeStatus(RecruitJobResumeVo recruitJobResumeVo) {

        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            if (recruitJobResumeVo == null) {
                resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            if (recruitJobResumeVo.getId() != null && StringUtils.equals(recruitJobResumeVo.getType(), RecruitEnum.JobResumeStatusType.COMMISSION.name())) { //有奖推荐
                AppRecruitJobResume appRecruitJobResume = recruitManager.queryjobResumeById(recruitJobResumeVo.getId());
                /*if (appRecruitJobResume == null) { //投递记录已经被删除
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400073);
                    return resp;
                }
                AppRecruitJob appRecruitJob = recruitManager.queryRecruitJobById(appRecruitJobResume.getJobId());
                if (appRecruitJob == null) { //岗位已经被删除
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400071);
                    return resp;
                }*/
                List<AppRecruitJobResume> recruitJobResumes = recruitManager.queryJobResume(AppRecruitJobResume.builder().jobId(recruitJobResumeVo.getJobId()).recruitId(recruitJobResumeVo.getRecruitId()).build(), null);
                if (recruitJobResumes == null && recruitJobResumes.size() == 0) {//简历已经被删除
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                    return resp;
                }
                appRecruitJobResume = DTOUtils.map(recruitJobResumeVo, AppRecruitJobResume.class);
                Boolean flag = recruitManager.updateRecruitJobResume(appRecruitJobResume);
                //保存岗位申请进度提示信息
                if (StringUtils.isNotEmpty(recruitJobResumeVo.getStatus())) {
                    String str = RecruitEnum.EmploymentType.getTip(recruitJobResumeVo.getStatus());
                    recruitManager.insertAppRecruitJobResumeTip(AppRecruitJobResumeTip.builder().jobResumeId(recruitJobResumeVo.getId()).tip(str).build());
                }
                resp.setResult(flag);
                return resp;
            }
            if (StringUtils.isNotEmpty(recruitJobResumeVo.getManageStatus())) {
                AppRecruitResume appRecruitResume = recruitManager.queryRecruitResumeById(recruitJobResumeVo.getRecruitId());
                if (appRecruitResume == null) {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400072);
                    return resp;
                }
                Boolean aBoolean = recruitManager.updateRecruitResume(AppRecruitResume.builder().id(appRecruitResume.getId()).manageStatus(recruitJobResumeVo.getManageStatus()).age(appRecruitResume.getAge()).build());
                resp.setResult(aBoolean);
                return resp;
            }
            if (StringUtils.equals(recruitJobResumeVo.getType(), RecruitEnum.JobResumeStatusType.RESUME.name())) {//修改求职人员管理的备注
                AppRecruitResume appRecruitResume = recruitManager.queryRecruitResumeById(recruitJobResumeVo.getId());
                if (appRecruitResume == null) {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400072);
                    return resp;
                }
                String remark = null;
                if (recruitJobResumeVo.getRemark() != null) {
                    remark = recruitJobResumeVo.getRemark();
                }
                Boolean aBoolean = recruitManager.updateRecruitResume(AppRecruitResume.builder().id(appRecruitResume.getId()).remark(remark).build());
                resp.setResult(aBoolean);
                return resp;
            }
            if (StringUtils.equals(recruitJobResumeVo.getType(), RecruitEnum.JobResumeStatusType.JOBRESUME.name())) {//修改录用状态
                AppRecruitResume appRecruitResume = recruitManager.queryRecruitResumeById(recruitJobResumeVo.getRecruitId());
                if (appRecruitResume == null) { //该简历已经被删除
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400072);
                    return resp;
                }
                List<AppRecruitJobResume> recruitJobResumes = recruitManager.queryJobResume(AppRecruitJobResume.builder().jobId(recruitJobResumeVo.getJobId()).recruitId(recruitJobResumeVo.getRecruitId()).build(), null);
                if (recruitJobResumes == null && recruitJobResumes.size() == 0) { //关联表数据已经被删除
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                    return resp;
                }
                //查询简历的状态
                queryResumeStatus(recruitJobResumeVo.getRecruitId());
                recruitJobResumeVo.setId(recruitJobResumes.get(0).getId());
                AppRecruitJobResume appRecruitJobResume = DTOUtils.map(recruitJobResumeVo, AppRecruitJobResume.class);
                AppRecruitJob appRecruitJob = recruitManager.queryRecruitJobById(recruitJobResumeVo.getJobId());
                if (appRecruitJob == null) { //岗位已经被删除
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400071);
                    return resp;
                }
                Boolean flag = recruitManager.updateRecruitJobResume(appRecruitJobResume);
                resp.setResult(flag);
                //同步简历的状态
                queryResumeStatus(recruitJobResumeVo.getRecruitId());
                //保存岗位申请进度提示信息
                String str = RecruitEnum.EmploymentType.getTip(recruitJobResumeVo.getStatus());
                recruitManager.insertAppRecruitJobResumeTip(AppRecruitJobResumeTip.builder().jobResumeId(recruitJobResumeVo.getId()).tip(str).build());
                return resp;
            }
        } catch (Exception e) {
            logger.warn("updateJobResumeStatus error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 查询求职人员
     *
     * @param recruitResumeReq
     * @return
     */
    @Override
    public Response<List<RecruitResumeVo>> queryRecruitResume(RecruitResumeReq recruitResumeReq) {

        Response<List<RecruitResumeVo>> resp = ResponseUtils.successResponse();
        try {
            List<RecruitResumeVo> recruitResumeVos = new ArrayList<>();
            AppRecruitResume appRecruitResume = DTOUtils.map(recruitResumeReq, AppRecruitResume.class);
            Page page = new Page();
            page.setStartPage(recruitResumeReq.getStartPage());
            page.setPageSize(recruitResumeReq.getPageSize());
            String orderBy = null;
            if (StringUtils.isNotEmpty(recruitResumeReq.getOrderBy())) {
                orderBy = recruitResumeReq.getOrderBy();
            }
            List<AppRecruitResume> appRecruitResumes = recruitManager.queryResumeByParam(appRecruitResume, page, orderBy, "Y");
            if (appRecruitResumes != null && appRecruitResumes.size() > 0) {
                for (AppRecruitResume appRecruitResume1 : appRecruitResumes) {
                    RecruitResumeVo recruitResumeVo = convertRecruitResume(appRecruitResume1);
                    recruitResumeVos.add(recruitResumeVo);
                }
            }
            resp.setResult(recruitResumeVos);
        } catch (Exception e) {
            logger.warn("queryRecruitResume error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private RecruitResumeVo convertRecruitResume(AppRecruitResume appRecruitResume1) {
        RecruitResumeVo recruitResumeVo = null;
        try {
            recruitResumeVo = DTOUtils.map(appRecruitResume1, RecruitResumeVo.class);
            //地址转换
            recruitResumeVo = queryRecruitResumeVo(recruitResumeVo);
            String status = queryResumeStatus(appRecruitResume1.getId());
            recruitResumeVo.setStatus(status);
            if (StringUtils.isEmpty(recruitResumeVo.getStatus())) {
                recruitResumeVo.setStatus("N");
            }
            if(StringUtils.isNotEmpty(recruitResumeVo.getExpectCity())){
                recruitResumeVo.setExpectCity(convertExpectCity(recruitResumeVo.getExpectCity()));
            }
            //更新求职人员的投递数量
            int count = recruitManager.queryRecruitJobResumeCount(AppRecruitJobResume.builder().recruitId(recruitResumeVo.getId()).build());
            recruitResumeVo.setDeliveryCount(count);
            recruitManager.updateRecruitResume(AppRecruitResume.builder().id(recruitResumeVo.getId()).deliveryCount(count).build());
        } catch (Exception e) {
            logger.warn("convertRecruitResume:",e);
        }
        return recruitResumeVo;
    }

    /**
     * 地址转换
     * @param expectCity
     * @return
     */
    private String convertExpectCity(String expectCity) {
        String[] city = expectCity.split(",");
        String[] cityArray = new String[city.length];
        for (int i = 0; i < city.length; i++) {
            if(city[i].equals("-")){
                cityArray[i] = "不限";   
            }else{
                cityArray[i] = city[i]; 
            }
        }
        String expentCity =  StringUtils.join(cityArray,",");
        return expentCity;
    }

    /**
     * 查询求职人员总数
     *
     * @param recruitResumeReq
     * @return
     */
    @Override
    public Response<Integer> queryRecruitResumeCount(RecruitResumeReq recruitResumeReq) {

        Response<Integer> resp = ResponseUtils.successResponse();
        try {
            AppRecruitResume appRecruitResume = DTOUtils.map(recruitResumeReq, AppRecruitResume.class);
            int count = recruitManager.queryResumeCountByParam(appRecruitResume, "Y");
            resp.setResult(count);
        } catch (Exception e) {
            logger.warn("queryRecruitResumeCount error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 同步录用状态
     *
     * @param recruitId
     */
    private String queryResumeStatus(Long recruitId) {

        try {
            AppRecruitResume appRecruitResume = recruitManager.queryRecruitResumeById(recruitId);
            int age = 0;
            if (appRecruitResume != null && appRecruitResume.getAge() != null && appRecruitResume.getAge() > 0) {
                age = appRecruitResume.getAge();
            }
            int countN = recruitManager.queryRecruitJobResumeCount(AppRecruitJobResume.builder().recruitId(recruitId).build());

            if (countN == 0) {
                appRecruitResume.setStatus(null);
                recruitManager.updateAllRecruitResumeById(appRecruitResume);
                //recruitManager.updateRecruitResume(AppRecruitResume.builder().id(recruitId).age(age).status("").build());
                return "N";
            }
            //查询有没有已录取
            int countU = recruitManager.queryRecruitJobResumeCount(AppRecruitJobResume.builder().status(RecruitEnum.EmploymentType.U.name()).recruitId(recruitId).build());//已录用
            int countUC = recruitManager.queryRecruitJobResumeCount(AppRecruitJobResume.builder().status(RecruitEnum.EmploymentType.UC.name()).recruitId(recruitId).build());//佣金已发放
            int countUNC = recruitManager.queryRecruitJobResumeCount(AppRecruitJobResume.builder().status(RecruitEnum.EmploymentType.UNC.name()).recruitId(recruitId).build()); //佣金不发放
            //投递记录中存在已录用或佣金不发放或佣金已发放
            if (countU > 0 || countUC > 0 || countUNC > 0) {
                recruitManager.updateRecruitResume(AppRecruitResume.builder().id(recruitId).age(age).status(RecruitEnum.EmploymentType.U.name()).build());
                return RecruitEnum.EmploymentType.U.name(); //已录取
            }

            // 投递记录中只存在等待录用 H
            int countH = recruitManager.queryRecruitJobResumeCount(AppRecruitJobResume.builder().status(RecruitEnum.EmploymentType.H.name()).recruitId(recruitId).build());
            //等待录取：投递记录中没有已录取U和未录取F
            if (countN == countH) {
                recruitManager.updateRecruitResume(AppRecruitResume.builder().id(recruitId).status(RecruitEnum.EmploymentType.H.name()).age(age).build());
                return RecruitEnum.EmploymentType.H.name();//等待录取
            }

            //未录取：投递记录中1、同时存在未录用和等待录用  2、只存在未录用
            //存在未录用
            int countF = recruitManager.queryRecruitJobResumeCount(AppRecruitJobResume.builder().status(RecruitEnum.EmploymentType.F.name()).recruitId(recruitId).build());

            if ((countF > 0 && countH > 0) || (countN == countF)) {
                recruitManager.updateRecruitResume(AppRecruitResume.builder().id(recruitId).status(RecruitEnum.EmploymentType.F.name()).age(age).build());
                return RecruitEnum.EmploymentType.F.name();  //未录取
            }
        } catch (Exception e) {
            logger.info("queryResumeStatus :", e);
        }
        return null;
    }

    /**
     * 根据简历的id查询投递记录
     *
     * @param recruitJobResumeReq
     * @return
     */
    @Override
    public Response<List<RecruitJobVo>> queryRecruitJobByJobResume(RecruitJobResumeReq recruitJobResumeReq) {
        Response<List<RecruitJobVo>> resp = ResponseUtils.successResponse();
        try {
            if (recruitJobResumeReq == null) {
                resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            AppRecruitResume appRecruitResume = recruitManager.queryRecruitResumeById(recruitJobResumeReq.getRecruitId());
            if (appRecruitResume == null) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400072);
                return resp;
            }
            Map<String, Object> param = new HashMap<>();
            param.put("recruitId", recruitJobResumeReq.getRecruitId());
            param.put("startPage", recruitJobResumeReq.getStartPage());
            param.put("pageSize", recruitJobResumeReq.getPageSize());
            //根据简历的id查询投递记录
            List<AppRecruitJobResume> recruitJobResumes = recruitManager.queryRecruitJobResumeList(param);
            List<RecruitJobVo> recruitJobVoList = recruitJobResumes.stream().map((vo) -> {
                RecruitJobVo recruitJobVo = new RecruitJobVo();
                AppRecruitJob appRecruitJob = queryJobPropExtend(vo.getId(), vo.getJobId());
                if (appRecruitJob == null) { //缓存里面没有数据、在数据库查询
                    appRecruitJob = recruitManager.queryRecruitJobById(vo.getJobId());
                    if (appRecruitJob == null) {//数据库查询不到 只显示招聘id
                        recruitJobVo.setId(vo.getJobId());
                        recruitJobVo.setCreateTime(vo.getCreateTime());
                        return recruitJobVo;
                    }
                }
                appRecruitJob.setCreateTime(vo.getCreateTime());
                return convertRecruitPojo(appRecruitJob);
            }).collect(Collectors.toList());
            resp.setResult(recruitJobVoList);
        } catch (Exception e) {
            logger.warn("queryRecruitJobByJobResume error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 根据简历的id查询投递记录总数
     *
     * @param recruitJobResumeReq
     * @return
     */
    @Override
    public Response<Integer> queryRecruitJobCountByJobResume(RecruitJobResumeReq recruitJobResumeReq) {

        Response<Integer> resp = ResponseUtils.successResponse();
        try {
            AppRecruitResume appRecruitResume = recruitManager.queryRecruitResumeById(recruitJobResumeReq.getRecruitId());
            if (appRecruitResume == null) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400072);
                return resp;
            }
            int count = recruitManager.queryRecruitJobResumeCount(AppRecruitJobResume.builder().recruitId(recruitJobResumeReq.getRecruitId()).build());
            resp.setResult(count);
        } catch (Exception e) {
            logger.warn("queryRecruitJobCountByJobResume error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private RecruitJobVo convertRecruitPojo(AppRecruitJob appRecruitJob) {

        RecruitJobVo recruitJobVo = new RecruitJobVo();
        try {
            String area = null;
            if (StringUtils.isNotEmpty(appRecruitJob.getArea())) {
                area = appRecruitJob.getArea();
                appRecruitJob.setArea(null);
            }
            recruitJobVo = DTOUtils.map(appRecruitJob, RecruitJobVo.class);
            recruitJobVo.setLocation(getAddress(recruitJobVo));
            if (area != null) {
                AreaVo area1 = JSON.parseObject(area, AreaVo.class);
                recruitJobVo.setArea(area1);
            }
            if (recruitJobVo != null) {
                //需求
                recruitJobVo.setRequirement(queryRequirement(recruitJobVo));
                //没有福利信息的招聘，默认显示“急招”标签
                if (StringUtils.isEmpty(recruitJobVo.getWalfare())) {
                    recruitJobVo.setWalfare("急招");
                }
                //福利转数组和图片数组
                recruitJobVo = getWelfareArray(recruitJobVo);
                //拼接地址
                //recruitJobVo.setLocation(getAddress(recruitJobVo));
                //获取用户头像
                User user = userManager.queryUser(recruitJobVo.getPartyId());
                if (user != null) {
                    if (StringUtils.isNotEmpty(user.getShowName())) {
                        recruitJobVo.setShowName(user.getShowName());
                    } else {
                        recruitJobVo.setShowName(user.showName());
                    }
                    if (StringUtils.isNotEmpty(user.getHeadImg())) {
                        recruitJobVo.setHeadImg(user.getHeadImg());
                    } else {
                        recruitJobVo.setHeadImg(SysConfigUtil.getStr("default_user_headimg"));
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("convertRecruitPojo error", e);
        }
        return recruitJobVo;
    }


    private String queryShowName(Long partyId) {

        User user = userManager.queryUser(partyId);
        if (user != null) {
            if (StringUtils.isNotEmpty(user.getShowName())) {
                return user.getShowName();
            } else {
                return user.showName();
            }
        } else {
            return "xl" + partyId;
        }
    }

    /**
     * 招工举报
     *
     * @param id
     * @param reason
     * @param message
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.reportRecruitJobByArticleId", description = "举报招工")
    public Response<Boolean> reportRecruitJobByArticleId(Long id, String reason, String message) {

        try {
            Assert.notNull(id, "id不能为空");
            Assert.notNull(reason, "举报原因不能为空");
            Assert.notNull(message, "举报留言不能为空");
            if (reason.length() == 0) {
                throw new IllegalArgumentException("举报原因不能为空");
            }
            if (message.length() == 0) {
                throw new IllegalArgumentException("举报留言不能为空");
            }
            AppRecruitJob appRecruitJob = recruitManager.queryRecruitJobById(id);
            if (appRecruitJob == null && "Y".equals(appRecruitJob.getIsDeleted())) {// 判断动态是否存在
                return Response.ofFail("该数据已经被删除");
            }
            AppArticle appArticle = articleManager.queryArticle(appRecruitJob.getArticleId());
            if (appArticle == null) {// 判断动态是否存在
                return Response.ofFail("该数据已经被删除");
            }
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("partyId", partyId);
            paras.put("articleId", appRecruitJob.getArticleId());
            paras.put("tipType", Constant.ArticleTipType.REPORT.name());
            List<AppArticleTip> tips = articleManager.queryArticleTipList(paras);
            if (CollectionUtils.isNotEmpty(tips)) {// 已经举报过
                return Response.ofFail("请勿重复举报");
            } else {
                AppArticleTip tip = new AppArticleTip();
                tip.setPartyId(partyId);
                tip.setTipType(Constant.ArticleTipType.REPORT.name());
                tip.setArticleId(appArticle.getId());

                tip.setToPartyId(appRecruitJob.getPartyId());
                String content = convertReprotDetail(appArticle);
                tip.setContent(content);
                tip.setComments("【" + reason + "】" + message);
                articleManager.addArticleTip(tip);
            }
            return Response.ofSuccess(Boolean.TRUE);
        } catch (IllegalArgumentException e) {
            logger.info("===========参数错误：[[ {} ]]===========", e.getMessage(), e);
            return Response.ofFail("无效的参数");
        } catch (Exception e) {
            logger.error("===========异常：[[ {} ]][[ {} ]][[ {} ]][[ {} ]]===========", e.getMessage(), id, reason, message, e);
            return Response.ofFail("异常");
        }
    }

    /**
     * 生成动态拼接内容
     *
     * @param article
     * @return
     */
    private String convertReprotDetail(AppArticle article) {

        StringBuilder sb = new StringBuilder();
        if (article != null) {
            if (StringUtils.isNotEmpty(article.getArticle())) {
                sb.append(article.getArticle());
            }
            if (StringUtils.isNotEmpty(article.getArticleAudio())) {
                sb.append("[语音]");
            }
            if (StringUtils.isNotEmpty(article.getArticleImgs())) {
                int countMatches = StringUtils.countMatches(article.getArticleImgs(), "http");
                for (int i = 0; i < countMatches; i++) {
                    sb.append("[图片]");
                }
            }
        }
        String retVal = sb.toString();
        if (retVal.length() > 300) {
            retVal = retVal.substring(0, 299);
        }
        return retVal;
    }

    /**
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.getReportDetail", description = "回显招工举报")
    public Response<Map<String, String>> getReportDetail(Long id) {

        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Assert.notNull(id, "id不能为空");
            AppRecruitJob appRecruitJob = recruitManager.queryRecruitJobById(id);
            Preconditions.checkState(partyId != null, "未登录");
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("partyId", partyId);
            paras.put("articleId", appRecruitJob.getArticleId());
            paras.put("tipType", Constant.ArticleTipType.REPORT.name());
            List<AppArticleTip> tips = articleManager.queryArticleTipList(paras);
            if (tips.isEmpty()) {
                return Response.ofSuccess(null);
            }
            AppArticleTip appArticleTip = tips.get(0);
            String reason = appArticleTip.getComments();
            int fromIndex = reason.indexOf("【");
            int toIndex = reason.indexOf("】");

            HashMap<String, String> result = Maps.newHashMap();
            //取出拼接的reason type
            if (fromIndex > 0 && toIndex > 0) {
                result.put("reason", reason.substring(fromIndex + 1, toIndex));
            }
            String message = reason;
            if (toIndex < reason.length()) {
                message = reason.substring(toIndex + 1);
            }
            result.put("message", message);
            return Response.ofSuccess(result);
        } catch (IllegalArgumentException e) {
            logger.info("===========参数错误：[[ {} ]][[ {} ]]===========", id, e.getMessage());
            return Response.ofFail(e.getMessage());
        } catch (IllegalStateException e) {
            logger.info("===========状态错误：[[ {} ]][[ {} ]]===========", id, e.getMessage());
            return Response.ofFail(e.getMessage());
        } catch (Exception e) {
            logger.info("===========异常： [[ {} ]]===========", id, e);
            return Response.ofFail(e.getMessage());
        }
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.publishRecruitJobDraft", description = "新增招工草稿")
    public Response<Boolean> publishRecruitJobDraft(String recruitJobVo) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String md5 = MD5.md5(partyId + JSON.toJSONString(recruitJobVo));
            if (redisUtil.isRepeat(md5, 2)) {
                resp.setFacade(FacadeEnums.ERROR_REPEAT);
                return resp;
            }
            if (recruitJobVo == null) {
                resp.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_INPUT_INVALID);
                return resp;
            }
            Boolean flag = false;
            AppPropExtend prop = propExtendManager.selectProp(partyId, EnumKeyValue.User.class.getSimpleName(), EnumKeyValue.User.USER_RECRUITJOB_DRAFT.name());
            if (prop != null) {
                prop.setValue(recruitJobVo);
                flag = propExtendManager.update(prop);
            } else {
                flag = propExtendManager.insertExceptPropExtend(AppPropExtend.builder().relationId(partyId).deviceId(deviceId).type(EnumKeyValue.User.class.getSimpleName()).ekey(EnumKeyValue.User.USER_RECRUITJOB_DRAFT.name()).value(recruitJobVo).build());
            }
            resp.setResult(flag);
        } catch (Exception e) {
            logger.warn("publishRecruitJobDraft error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.queryRecruitJobDraft", description = "查询当前登录用户的招工草稿")
    public Response<String> queryRecruitJobDraft() {
        return responseCacheUtils.execute(() -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            AppPropExtend prop = propExtendManager.selectProp(partyId, EnumKeyValue.User.class.getSimpleName(), EnumKeyValue.User.USER_RECRUITJOB_DRAFT.name());
            if (prop != null && StringUtils.isNotEmpty(prop.getValue())) {
                return prop.getValue();
            }
            return null;
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.cleanRecruitJobDraft", description = "清除当前登录用户的招工草稿")
    public Response<Boolean> cleanRecruitJobDraft() {
        return responseCacheUtils.execute(() -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            //查询用户的招工草稿，然后清除
            AppPropExtend appPropExtend = propExtendManager.selectProp(partyId, EnumKeyValue.User.class.getSimpleName(), EnumKeyValue.User.USER_RECRUITJOB_DRAFT.name());
            if (appPropExtend != null) {
                Boolean flag = propExtendManager.update(AppPropExtend.builder().isDeleted("Y").relationId(partyId).type(EnumKeyValue.User.class.getSimpleName()).id(appPropExtend.getId()).ekey(EnumKeyValue.User.USER_RECRUITJOB_DRAFT.name()).build());
                return flag;
            }
            return false;
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.queryRecommendJob", description = "查询推荐招聘")
    public Response<List<RecruitJobVo>> queryRecommendJob(RecruitJobReq recruitJobReq) {
        return responseCacheUtils.execute(() -> {
            List<String> strings = null;
            List<String> expectCitys = null;
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            List<RecruitJobVo> list1 = new ArrayList<>();
            if (StringUtils.isNotEmpty(recruitJobReq.getJobCategory()) && StringUtils.contains(recruitJobReq.getJobCategory(), "推荐岗位")) {
                recruitJobReq.setJobCategory(null);
                //未登录，随机取5条
                if (partyId == null) {
                    List<RecruitJobVo> recruitJobVoList =queryRandRecruitJob(recruitJobReq);
                    return recruitJobVoList;
                }
                //求职意向中的岗位
                List<AppRecruitResume> recruitResumes = recruitManager.queryRecruitResumeByResume(AppRecruitResume.builder().type(RecruitEnum.ResumeType.PSERSONAL.name()).partyId(partyId).build());
                if (recruitResumes.size() > 0 && (StringUtils.isNotEmpty(recruitResumes.get(0).getExpectJob()) || StringUtils.isNotEmpty(recruitResumes.get(0).getExpectCity()))) {//查用户的期望工作
                    if (StringUtils.isNotEmpty(recruitResumes.get(0).getExpectJob())) { //期望的岗位
                        strings = Arrays.asList(recruitResumes.get(0).getExpectJob().split(","));
                    }
                    if (StringUtils.isNotEmpty(recruitResumes.get(0).getExpectCity())) {//期望的工作地址
                        expectCitys = Arrays.asList(recruitResumes.get(0).getExpectCity().replace("-", "%").split(","));
                    }
                    recruitJobReq.setJobCategorys(strings);
                    recruitJobReq.setExpectCitys(expectCitys);
                    Response<List<RecruitJobVo>> listResponse = this.queryRecruitJobList(recruitJobReq);
                    recruitJobReq.setJobCategorys(null);
                    recruitJobReq.setExpectCitys(null);
                    if (listResponse.getResult().size() > 0) {
                        list1.addAll(listResponse.getResult());
                        return list1;
                    }
                }
                //求职意向里面没有就随机5条数据
                if (list1.size() < 1) {
                    //查随机5条数据
                    List<RecruitJobVo> recruitJobVoList =queryRandRecruitJob(recruitJobReq);
                    return recruitJobVoList;
                }
            }
            Response<List<RecruitJobVo>> listResponse = this.queryRecruitJobList(recruitJobReq);
            list1.addAll(listResponse.getResult());
            return list1;
        });
    }

    /**
     * 
     * @param recruitJobReq
     * @return
     */
    private List<RecruitJobVo> queryRandRecruitJob(RecruitJobReq recruitJobReq) {
        AppRecruitJob recruitJob = DTOUtils.map(recruitJobReq, AppRecruitJob.class);
        AppDateSectionReq appDateSectionReq = new AppDateSectionReq();
        appDateSectionReq.setStartPage(1);
        appDateSectionReq.setPageSize(5);
        List<AppRecruitJob> list=recruitManager.queryRecruitJobList(recruitJob, appDateSectionReq, "RAND()", null, null, null);
        List<RecruitJobVo> recruitJobVoList = convertRecruitPojoList(list);
        return recruitJobVoList;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.queryJobResumeTipsByJobResumeId", description = "查询岗位申请进度")
    public Response<Map<String, Object>> queryJobResumeTipsByJobResumeId(Long jobResumeId) {
        return responseCacheUtils.execute(() -> {
            Map<String, Object> result = Maps.newConcurrentMap();
            AppRecruitJobResume appRecruitJobResume = recruitManager.queryjobResumeById(jobResumeId);
            List<AppRecruitJobResumeTip> appJobResumeTips = recruitManager.queryAppRecruitJobResumeTipsByJobResumeId(jobResumeId);
            List<AppRecruitJobResumeTipVo> appRecruitJobResumeTipVoList = DTOUtils.map(appJobResumeTips, AppRecruitJobResumeTipVo.class);
            if (appRecruitJobResume != null && CollectionUtils.isNotEmpty(appRecruitJobResumeTipVoList)) {
                result.put("status", appRecruitJobResume.getStatus());
                result.put("tip", RecruitEnum.EmploymentType.getMsg(appRecruitJobResume.getStatus()));
                result.put("appRecruitJobResumeTipVoList", appRecruitJobResumeTipVoList);
            }
            return result;
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.insertUserReceiptAccount", description = "保存用户收款账户信息")
    public Response<Boolean> insertUserReceiptAccount(UserRecommendedVo userRecommendedVo) {
        return responseCacheUtils.executeCacheLock("insertUserReceiptAccount", () -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            AppPropExtend appPropExtend = propExtendManager.selectProp(partyId, EnumKeyValue.User.class.getSimpleName(), EnumKeyValue.User.USER_RECEIPT_ACCOUNT.name());
            if (appPropExtend != null) {
                throw new AppException(FacadeEnums.ERROR_CHAT_400070);
            }
            appPropExtend = AppPropExtend.builder().relationId(partyId).ekey(EnumKeyValue.User.USER_RECEIPT_ACCOUNT.name()).type(EnumKeyValue.User.class.getSimpleName()).value(userRecommendedVo.toString()).deviceId(deviceId).build();
            boolean flag = propExtendManager.insert(appPropExtend);
            return flag;
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.queryUserReceiptAccount", description = "查询用户收款账户信息")
    public Response<UserRecommendedVo> queryUserReceiptAccount() {
        return responseCacheUtils.execute(() -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            AppPropExtend appPropExtend = propExtendManager.selectProp(partyId, EnumKeyValue.User.class.getSimpleName(), EnumKeyValue.User.USER_RECEIPT_ACCOUNT.name());
            UserRecommendedVo userRecommendedVo = null;
            if (appPropExtend != null) {
                Object accountVo = JSONObject.parse(appPropExtend.getValue());
                userRecommendedVo = DTOUtils.map(accountVo, UserRecommendedVo.class);
            }
            return userRecommendedVo;
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.queryUserReward", description = "查询用户佣金信息")
    public Response<Map<String, Object>> queryUserReward() {
        return responseCacheUtils.execute(() -> {
            Map<String, Object> result = Maps.newConcurrentMap();

            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Integer reward = recruitManager.queryUserReward(partyId, RecruitEnum.EmploymentType.UC.name());
            Integer deferredReward = recruitManager.queryUserReward(partyId, RecruitEnum.EmploymentType.U.name());
            result.put("partyId", partyId);
            result.put("type", "RECOMMEND");
            List<AppRecruitJob> list = recruitManager.queryJobRecommendList(result);
            if (CollectionUtils.isNotEmpty(list)) {
                result.put("isHaveRecommend", true);
            } else {
                result.put("isHaveRecommend", false);
            }
            result.put("reward", reward);
            result.put("deferredReward", deferredReward);
            return result;
        });
    }

    @Override
    public Response<List<Map<String, Object>>> queryAllCommission() {
        return responseCacheUtils.execute(() -> {
            return recruitManager.queryJobResumeList(null, RecruitEnum.EmploymentType.U.name(), false, null, null).stream()
                    .map(v -> {
                        AtomicBoolean dataStatus = new AtomicBoolean(true);
                        Map<String, Object> result = new HashMap<>();
                        result.put("id", v.getId());
                        result.put("partyId", v.getPartyId());
                        result.put("status", v.getStatus());
                        result.put("createTime", v.getCreateTime());
                        result.put("updateTime", v.getUpdateTime());
                        result.put("remark", v.getRemark());
                        result.put("commission", v.getCommission());
                        result.put("recruitId", v.getRecruitId());
                        result.put("jobId", v.getJobId());
                        Optional.ofNullable(queryResumePropExtend(v.getId(),v.getRecruitId())).ifPresent(v1 -> {
                            if (StringUtils.equals(v1.getType(), RecruitEnum.ResumeType.PSERSONAL.name())) {
                                dataStatus.set(false);
                            }
                            result.put("name", v1.getName());
                            result.put("certificatesNumber", v1.getCertificatesNumber());
                            DTOUtils.objectToMap(v1).ifPresent(v2 -> {
                                v2.remove("class");
                                if (StringUtils.isNotEmpty(v1.getDistrict())) {
                                    JSONObject jsonObject = JSON.parseObject(v1.getDistrict());
                                    v2.put("districtStitch", Optional.ofNullable(jsonObject.getJSONObject("province").getString("name")).orElse("") + Optional.ofNullable(jsonObject.getJSONObject("city").getString("name")).orElse("") + Optional.ofNullable(jsonObject.getJSONObject("county").getString("name")).orElse(""));
                                }
                                result.put("resume", v2);
                            });
                        });
                        Optional.ofNullable(queryJobPropExtend(v.getId(),v.getJobId())).ifPresent(job -> {
                            result.put("jobName", job.getJobName());
                            result.put("commissionDesc", job.getCommissionDesc());
                        });
                        Optional.ofNullable(userManager.queryUser(v.getPartyId())).ifPresent(user -> {
                            result.put("recName", user.showName());
                            result.put("nikerName", user.getNikerName());
                            result.put("recPartyId", user.getPartyId()+"");
                            result.put("recLoginName", user.getLoginName());
                            Optional.ofNullable(result.get("resume")).ifPresent(v2 -> {
                                ((Map) v2).put("showName", user.showName());
                            });
                        });
                        Optional.ofNullable(propExtendManager.selectProp(v.getPartyId(), EnumKeyValue.User.class.getSimpleName(), EnumKeyValue.User.USER_RECEIPT_ACCOUNT.name())).ifPresent(v1 -> {
                            JSONObject json = JSON.parseObject(v1.getValue());
                            result.put("userAccountName", json.getString("userAccountName"));
                            result.put("accountNumber", json.getString("accountNumber"));
                            result.put("bank", json.getString("bank"));

                        });
                        recruitManager.queryAppRecruitJobResumeTipsByJobResumeId(v.getId()).stream().filter(v1 -> StringUtils.equals(v1.getTip(), RecruitEnum.EmploymentType.U.tip))
                                .findFirst().ifPresent(v1 -> {
                            //最近录取时间
                            result.put("recruitDate", v1.getCreateTime());
                        });
                        recruitManager.queryAppRecruitJobResumeTipsByJobResumeId(v.getId()).stream().filter(v1 -> StringUtils.equals(v1.getTip(), RecruitEnum.EmploymentType.UC.tip))
                                .findFirst().ifPresent(v1 -> {
                            //佣金发放时间
                            result.put("commissionDate", v1.getCreateTime());
                        });
                        if (dataStatus.get()) {
                            return result;
                        } else {
                            return null;
                        }
                    }).filter(v -> v != null).collect(Collectors.toList());
        });
    }

    @Override
    public Response<List<Map<String, Object>>> queryAllCommissionTotal() {
        AtomicInteger ids = new AtomicInteger(1);
        return responseCacheUtils.execute(() -> {
            return recruitManager.queryRecruitResumeByResume(AppRecruitResume.builder().build()).stream().map(v -> v.getPartyId()).distinct().collect(Collectors.toSet())
                    .stream().map(v -> {
                        Map<String, Object> result = new HashMap<>();
                        result.put("id",ids.getAndAdd(1));
                        Optional.ofNullable(userManager.queryUser(v)).ifPresent(user -> {
                            result.put("recName", user.showName());
                            result.put("nikerName", user.getNikerName());
                            result.put("recPartyId", user.getPartyId());
                            result.put("recLoginName", user.getLoginName());
                        });

                        Optional.ofNullable(recruitManager.queryJobResumeList(v, RecruitEnum.EmploymentType.U.name(), false, null, null))
                                .filter(v1 -> v1.size() > 0).ifPresent(list -> {
                            result.put("recCount", list.size());
                            result.put("recCountSuccess", list.stream().filter(in -> StringUtils.contains(in.getStatus(), RecruitEnum.EmploymentType.UC.name())).count());
                            result.put("earnCommission", list.stream().filter(in -> StringUtils.contains(in.getStatus(), RecruitEnum.EmploymentType.UC.name())).map(v1 -> v1.getCommission()).reduce(Integer::sum).orElse(0));
                            result.put("stayCommission", list.stream().filter(in -> StringUtils.contains(in.getStatus(), RecruitEnum.EmploymentType.U.name())).map(v1 -> v1.getCommission()).reduce(Integer::sum).orElse(0));
                        });
                        if(result.containsKey("recCount")){
                            return result;
                        }else {
                            return null;
                        }
                    }).filter(v -> v != null).collect(Collectors.toList());
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.queryUserRecruitJurisdiction", description = "查询用户是否拥有发布带有佣金的职位的权限")
    public Response<Boolean> queryUserRecruitJurisdiction() {
        return responseCacheUtils.execute(() -> {
            Boolean flag = false;
            String loginName = sessionHelper.getSessionProp(SessionConstants.LOGIN_NAME, String.class);
            String mobile = sysParaManager.queryConfigValue("RECRUIT_PUBLISHJOB_USER_MOBILE");
            if (StringUtils.isNotEmpty(loginName)) {
                if (mobile.contains(loginName)) {
                    flag = true;
                }
            }
            return flag;
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.RecruitService.queryJobRecommendByCommission", description = "查询当前用户的推荐")
    public Response<List<RecruitJobVo>> queryJobRecommendByCommission(String isCommission, PageReq req) {
        return responseCacheUtils.execute(() -> {
            Boolean flag = null;
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (isCommission == null) {
                throw new AppException(FacadeEnums.E_C_INPUT_INVALID);
            }

            if (StringUtils.equals(isCommission, "N")) {
                flag = true;
            }
            if (StringUtils.equals(isCommission, "Y")) {
                flag = false;
            }
            List<RecruitJobVo> recruitJobResumes = recruitManager.queryJobResumeList(partyId, null, flag, null, new Page(req.getStartPage(),req.getPageSize())).stream().map(v -> {
                RecruitJobVo recruitJobVo = new RecruitJobVo();
                recruitJobVo.setResumeJobId(v.getId());
                recruitJobVo.setStatus(v.getStatus());

                AppPropExtend appPropExtend = propExtendManager.selectProp(v.getId(), EnumKeyValue.Recruit.class.getSimpleName(), EnumKeyValue.Recruit.RESUME_PROP_EXTEND.name());
                if (appPropExtend != null) {
                    AppRecruitResume appRecruitResume = JSON.parseObject(appPropExtend.getValue(), AppRecruitResume.class);
                    if (appRecruitResume.getType().equals(RecruitEnum.ResumeType.PSERSONAL.name())){
                        recruitJobVo.setRefereeName(null);
                    }else {
                        recruitJobVo.setRefereeName(appRecruitResume.getName());
                    }
                }
                Optional.ofNullable(Optional.ofNullable(recruitManager.queryRecruitJobById(v.getJobId())).orElseGet(() -> {
                    return queryJobPropExtend(v.getId(),v.getJobId());
                })).ifPresent(job -> {
                    recruitJobVo.setId(v.getJobId());
                    recruitJobVo.setPartyId(job.getPartyId());
                    recruitJobVo.setArticleId(job.getArticleId());
                    recruitJobVo.setJobName(job.getJobName());
                    recruitJobVo.setPayType(job.getPayType());
                    recruitJobVo.setPayStart(job.getPayStart());
                    recruitJobVo.setPayEnd(job.getPayEnd());

                    if (StringUtils.isEmpty(job.getWalfare())) {
                        job.setWalfare("急招");
                    }

                    //转换福利待遇
                    RecruitJobVo recruitJobVo1 = new RecruitJobVo();
                    recruitJobVo1.setWalfare(job.getWalfare());
                    recruitJobVo1 = getWelfareArray(recruitJobVo1);
                    recruitJobVo.setWalfares(recruitJobVo1.getWalfares());

                    recruitJobVo.setCompanyName(job.getCompanyName());
                    recruitJobVo.setOfficialSign(job.getOfficialSign());
                    recruitJobVo.setComments(job.getComments());
                    recruitJobVo.setDesc(job.getDesc());
                    recruitJobVo.setCommission(job.getCommission());
                    recruitJobVo.setCommissionDesc(job.getCommissionDesc());
                });
                return recruitJobVo;
                //岗位或简历被删除，若岗位名称或简历名字为空则该条记录不显示
            }).filter(v -> StringUtils.isNotEmpty(v.getJobName()) && StringUtils.isNotEmpty(v.getRefereeName())).collect(Collectors.toList());
            return recruitJobResumes;
        });
    }

    /**
     * 查询缓存的岗位
     *
     * @param id 申请id
     * @param jobId 工作id
     * @return
     */
    private AppRecruitJob queryJobPropExtend(Long id, Long jobId) {
        AppRecruitJob appRecruitJob;
        AppPropExtend appPropExtend1 = propExtendManager.selectProp(id, EnumKeyValue.Recruit.class.getSimpleName(), EnumKeyValue.Recruit.JOB_PROP_EXTEND.name());
        if (appPropExtend1 != null) {
            appRecruitJob = JSON.parseObject(appPropExtend1.getValue(), AppRecruitJob.class);
        } else {
            appRecruitJob = recruitManager.queryRecruitJobById(jobId);
        }
        return appRecruitJob;
    }

    /**
     * 查询缓存的简历
     *
     * @param id
     * @param resumeId 简历id
     * @return
     */
    private AppRecruitResume queryResumePropExtend(Long id, Long resumeId) {
        AppPropExtend appPropExtend1 = propExtendManager.selectProp(id, EnumKeyValue.Recruit.class.getSimpleName(), EnumKeyValue.Recruit.RESUME_PROP_EXTEND.name());
        AppRecruitResume appRecruitResume = null;
        if (appPropExtend1 != null) {
            appRecruitResume = JSON.parseObject(appPropExtend1.getValue(), AppRecruitResume.class);
        } else {
            appRecruitResume = recruitManager.queryRecruitResumeById(resumeId);
        }
        return appRecruitResume;
    }

    /**
     * 查询后台的招聘列表
     *
     * @param req
     * @return
     */
    @Override
    public Response<List<RecruitJobVo>> queryTOPRecruitJobList(RecruitJobReq req) {
        return responseCacheUtils.execute(() -> {
            AppRecruitJob recruitJob = DTOUtils.map(req, AppRecruitJob.class);
            AppDateSectionReq appDateSectionReq = new AppDateSectionReq();
            appDateSectionReq.setEndDay(req.getEndDay());
            appDateSectionReq.setStartDay(req.getStartDay());
            appDateSectionReq.setPageSize(req.getPageSize());
            appDateSectionReq.setStartPage(req.getStartPage());
            String orderBy = null;
            if (StringUtils.isEmpty(req.getOrderBy())) {
                orderBy = "a.RECOMMEND_SIGN desc,a.OFFICIAL_SIGN desc,a.RECOMMEND_TIME desc";
            } else {
                orderBy = req.getOrderBy();
            }
            recruitJob.setComments(null);
            List<AppRecruitJob> list = recruitManager.queryRecruitJobList(recruitJob, appDateSectionReq, orderBy, null, null, req.getIsCommission());
            List<RecruitJobVo> recruitJobVoList = list.stream().map((vo) -> {
                RecruitJobVo recruitJobVo = convertRecruitPojo(vo);
                //同步待处理数
                recruitJobVo.setStayApplyNum(syncStayApplyNum(recruitJobVo.getId()));
                //更新招聘投递数量
                int count = recruitManager.queryRecruitJobResumeCount(AppRecruitJobResume.builder().jobId(recruitJobVo.getId()).build());
                recruitManager.updateRecruitJob(AppRecruitJob.builder().id(recruitJobVo.getId()).applyNum(count).build());
                recruitJobVo.setApplyNum(count);
                return recruitJobVo;
            }).collect(Collectors.toList());
            return recruitJobVoList;
        });
    }

    /**
     * top端发布招工
     * @param recruitJobVo
     * @return
     */
    @Override
    public Response<Boolean> publishTopRecruitJob(RecruitJobVo recruitJobVo) {
        return responseCacheUtils.execute(()->{
            User user =userManager.getUserByLoginAccount(recruitJobVo.getPublishPhone());
            if(user == null){ //用户不存在
                throw new BusiException("用户不存在");
            }
            recruitJobVo.setPartyId(user.getPartyId());
            Response<Boolean> booleanResponse = this.publishRecruitJob(recruitJobVo);
            return booleanResponse.getResult();
        });
    }

    /**
     * 查询所有的求职人员 top后台导出
     * @return
     */
    @Override
    public Response<List<RecruitResumeVo>> queryRecruitResumeAll(RecruitResumeReq recruitResumeReq) {
        return responseCacheUtils.execute(()->{
            AppRecruitResume appRecruitResume = DTOUtils.map(recruitResumeReq, AppRecruitResume.class);
            Page page = new Page();
            page.setStartPage(1);
            page.setPageSize(Short.MAX_VALUE);
            String orderBy = null;
            if (StringUtils.isNotEmpty(recruitResumeReq.getOrderBy())) {
                orderBy = recruitResumeReq.getOrderBy();
            }
            List<AppRecruitResume> appRecruitResumes = recruitManager.queryResumeByParam(appRecruitResume, page, orderBy, "Y");
            List<RecruitResumeVo> list = appRecruitResumes.stream().map((vo)->{
                RecruitResumeVo recruitResumeVo = convertRecruitResume(vo);
                return  recruitResumeVo;
            }).collect(Collectors.toList());
            return list;
        });
    }

    /**
     * 查配置里面随机5个岗位
     * @return private List<String> queryRandomJobName() {
    List<String> strings = new ArrayList<>();
    String s = sysParaManager.queryConfigValue("RECRUIT_JOBTYPE_OPTION");
    JSONArray jsonArr = JSONArray.fromObject(s);//
    jsonArr.stream().forEach((v)->{
    JSONObject object = JSON.parseObject(v.toString());
    String value = object.get("value").toString();
    com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONArray.parseArray(value);
    int index = (int) (Math.random() * jsonArray.size());
    strings.add(jsonArray.get(index).toString());
    });
    return strings;
    }*/


    /**
     * 生成动态分享内容,针对微信朋友圈做不同处理
     *
     * @param recruitJobVo
     * @param articleShow
     * @return
     */
    private String shareContent(RecruitJobVo recruitJobVo, String articleShow) {

        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(recruitJobVo.getJobName())) {
            sb.append(recruitJobVo.getJobName());
        } else {
            sb.append(articleShow);
        }
        return sb.toString();
    }

    private Map<String, Object> getJobAndAdress(RecruitResumeVo recruitResumeVo) {
        Map<String, Object> result = Maps.newConcurrentMap();
        String job = "";
        String city = "";
        if (CollectionUtils.isNotEmpty(recruitResumeVo.getJobList())) {
            for (int i = 0; i < recruitResumeVo.getJobList().size(); i++) {
                if ((i + 1) == recruitResumeVo.getJobList().size()) {
                    job += recruitResumeVo.getJobList().get(i).get("first").toString() + "-" + recruitResumeVo.getJobList().get(i).get("second").toString();
                } else {
                    job += recruitResumeVo.getJobList().get(i).get("first").toString() + "-" + recruitResumeVo.getJobList().get(i).get("second").toString() + ",";
                }
            }
        }
        if (CollectionUtils.isNotEmpty(recruitResumeVo.getCityList())) {
            for (int i = 0; i < recruitResumeVo.getCityList().size(); i++) {

                if ("不限".equals(recruitResumeVo.getCityList().get(i).get("first").toString())) {
                    city += "-,";
                    continue;
                }

                if (recruitResumeVo.getCityList().get(i).get("second").toString().startsWith("全")) {
                    city += recruitResumeVo.getCityList().get(i).get("first").toString() + "-,";
                } else {
                    city += recruitResumeVo.getCityList().get(i).get("first").toString() + "-" + recruitResumeVo.getCityList().get(i).get("second").toString() + ",";
                }
            }
        }
        if (city.endsWith(",")) {
            city = city.substring(0, city.length() - 1);
        }

        result.put("job", job);
        result.put("city", city);
        return result;
    }
}
