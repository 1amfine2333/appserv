package com.xianglin.appserv.biz.service.implement;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xianglin.appserv.biz.shared.*;
import com.xianglin.appserv.biz.shared.util.ResponseCacheUtils;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.daointerface.AppUserRelationMapper;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.app.ArticleService;
import com.xianglin.appserv.common.service.facade.app.GoldService;
import com.xianglin.appserv.common.service.facade.app.PersonalService;
import com.xianglin.appserv.common.service.facade.base.PageResult;
import com.xianglin.appserv.common.service.facade.model.AppSessionConstants;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.*;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.ArticleReq;
import com.xianglin.appserv.common.service.facade.req.ArticleTipReq;
import com.xianglin.appserv.common.service.facade.req.ArticleTopicReq;
import com.xianglin.appserv.common.util.*;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.model.CustomersDTO;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Service("articleService")
@ServiceInterface
public class ArticleServiceImpl implements ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private ArticleManager articleManager;

    @Autowired
    private ActivityShareManager activityShareManager;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private CustomersInfoService customersInfoService;

    @Autowired
    private GoldService goldService;

    @Autowired
    private PropExtendManager propExtendManager;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AppUserRelationMapper appUserRelationMapper;

    @Autowired
    private UserRelationManager userRelationManager;

    @Autowired
    private RecruitManager recruitManager;

    @Autowired
    private UserRelationManager relationCoreService;

    @Autowired
    private LogManager logManager;

    @Autowired
    private ResponseCacheUtils responseCacheUtils;

    @Autowired
    private PersonalService personalService;
    
    @Autowired
    private SysParaManager sysParaManager;

    /**
     * 缩略图
     */
    private final String smallImg = "@!200_200";

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryArticleList", description = "动态查询")
    public Response<List<ArticleVo>> queryArticleList(PageReq req, Long partyId) {

        Response<List<ArticleVo>> resp = new Response<List<ArticleVo>>(null);
        try {
            String userType = sessionHelper.getSessionProp(SessionConstants.USER_TYPE, String.class);
            if (StringUtils.isEmpty(userType)) {
                userType = Constant.UserType.visitor.name();
            }
            Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.beanToMap(req);
            paras.put("partyId", partyId);
            paras.put("praisePartyId", userPartyId);
            paras.put("articleType", Constant.ArticleType.SUBJECT.name());
            List<AppArticle> list = articleManager.queryArticleList(paras);
            Long groupId = 0L;
            List<ArticleVo> result = convertPojo(list, userPartyId, groupId);
            resp.setResult(result);
            resp.setCode(1000);
        } catch (Exception e) {
            logger.warn("queryArticleList error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
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
                    /*if (StringUtils.isNotEmpty(article.getUser().getNikerName())) {
                        vo.setNikeName(article.getUser().getNikerName());
                    } else {
                        vo.setNikeName("xl" + article.getPartyId());
                    }*/
                    vo.setNikeName(article.getUser().getShowName());
                    if (StringUtils.isNotEmpty(article.getUser().getShowName())) {
                        vo.setShowName(article.getUser().getShowName());
                    } else {
                        vo.setShowName(article.getUser().showName());
                    }
                    if (StringUtils.isNotEmpty(article.getUser().getHeadImg())) {
                        vo.setHeadImg(article.getUser().getHeadImg());
                    } else {
                        vo.setHeadImg(SysConfigUtil.getStr("default_user_headimg"));
                    }
                    vo.setGender(article.getUser().getGender());
                } else {
                    vo.setShowName("xl" + article.getPartyId());
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
                result.add(vo);
            } catch (Exception e) {
                logger.warn("convertPojo error", e);
            }
        }
        return result;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryArticle", description = "动态明细")
    public Response<ArticleVo> queryArticle(Long articleId) {

        Response<ArticleVo> resp = new Response<ArticleVo>(null);
        try {
            String userType = sessionHelper.getSessionProp(SessionConstants.USER_TYPE, String.class);
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (userType == null) {
                userType = "";
            }
            if (partyId == null && articleId.equals(0L)) {  //查pcweb举报用户时使用到的
                resp.setResult(new ArticleVo());
            } else {
                Map<String, Object> paras = DTOUtils.queryMap();
                paras.put("id", articleId);
                paras.put("praisePartyId", partyId);
                //paras.put("articleType", Constant.ArticleType.SUBJECT.name());
                List<AppArticle> list = articleManager.queryArticleList(paras);
                Long groupId = 0L;
                if (list.size() > 0) {
                    if (list.get(0).getGroupId() > 0) {
                        groupId = list.get(0).getGroupId();
                    }
                    //学习资料阅读数加一
                    AppArticle appArticle = list.get(0);
                    String articleType = appArticle.getArticleType();
                    if (Constant.ArticleType.LEARNING_PPT.name().equals(articleType)) {
                        articleManager.incrReadCounts(appArticle.getId());
                        Integer readCount = appArticle.getReadCount();
                        if (readCount != null) {
                            appArticle.setReadCount(readCount + 1);
                        }
                    }
                }
                List<ArticleVo> result = convertPojo(list, partyId, groupId);
                if (result.size() > 0 && result.get(0) != null) {
                    ArticleVo vo = result.get(0);
                    // 查看是否被收藏
                    if (partyId != null) {
                        Map<String, Object> paras2 = DTOUtils.queryMap();
                        paras2.put("partyId", partyId);
                        paras2.put("articleId", vo.getId());
                        paras2.put("tipType", Constant.ArticleTipType.COLLET.name());
                        List<AppArticleTip> queryArticleTipList = articleManager.queryArticleTipList(paras2);
                        if (CollectionUtils.isNotEmpty(queryArticleTipList)) {
                            vo.setIsCollect(Constant.YESNO.YES.code);
                        } else {
                            vo.setIsCollect(Constant.YESNO.NO.code);
                        }
                    }
                    resp.setResult(checkArticleVo(vo));
                    resp.setCode(1000);
                } else {
                    resp.setCode(1000);
                }
            }
        } catch (Exception e) {
            logger.warn("queryArticle error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 学习专区控制，低版本对图片进行截取
     *
     * @param vo
     * @return
     */
    private ArticleVo checkArticleVo(ArticleVo vo) {
        if (StringUtils.equals(vo.getArticleType(), Constant.ArticleType.LEARNING_PPT.name())) {
            String currentVersion = "3.5.6";
            String version = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
            if (currentVersion.compareTo(version) > 0) {
                if (StringUtils.isNotEmpty(vo.getArticleImgs())) {
                    vo.setArticleImgs(Arrays.stream(vo.getArticleImgs().split(";")).limit(9).reduce((str1, str2) -> str1 + ";" + str2).orElse(""));
                }
            }
        }
        return vo;
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
            if (intervalMinutes < 1) {
                String showTime = "刚刚";
                return showTime;
            } else if (intervalMinutes >= 1 && intervalMinutes < 60) {
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

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryTopComments", description = "动态热评查询")
    public Response<List<ArticleVo>> queryTopComments(PageReq req, Long articleId) {

        Response<List<ArticleVo>> resp = new Response<List<ArticleVo>>(null);
        try {
            Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.beanToMap(req);
            paras.put("articleId", articleId);
            paras.put("praisePartyId", userPartyId);
            paras.put("orderBy", "FOLLOWCOUNT");
            paras.put("articleType", "COMMENT");
            List<AppArticle> list = articleManager.queryArticleList(paras);
            Long groupId = 0L;
            List<ArticleVo> result = convertPojo(list, userPartyId, groupId);
            resp.setResult(result);
            resp.setCode(1000);
        } catch (Exception e) {
            logger.warn("queryTopComments error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryCommentsList", description = "分页查询评论或子评论")
    public Response<List<ArticleVo>> queryCommentsList(PageReq req, Long articleId) {

        Response<List<ArticleVo>> resp = ResponseUtils.successResponse();
        try {
            // 查询评论
            String userType = sessionHelper.getSessionProp(SessionConstants.USER_TYPE, String.class);
            if (StringUtils.isEmpty(userType)) {
                userType = Constant.UserType.visitor.name();
            }
            AppArticle article = articleManager.queryArticle(articleId);
            if (article == null) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                return resp;
            }
            Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.beanToMap(req);

            paras.put("praisePartyId", userPartyId);
            if (article.getArticleType().equals(Constant.ArticleType.SUBJECT.name()) || article.getArticleType().equals(Constant.ArticleType.BROADCAST.name()) || article.getArticleType().equals(Constant.ArticleType.FINANCE.name()) || article.getArticleType().equals(Constant.ArticleType.GOVERNMENT.name()) || article.getArticleType().equals(Constant.ArticleType.RECRUITMENT.name()) || article.getArticleType().equals(Constant.ArticleType.LEARNING_PPT.name()) || article.getArticleType().equals(Constant.ArticleType.SHORT_VIDEO.name())) {
                paras.put("replyId", article.getId());
            } else if (article.getArticleType().equals(Constant.ArticleType.COMMENT.name())) {
                paras.put("commentId", article.getId());
                paras.put("pageSize", article.getReplyCount() + 5);
            } else {
                paras.put("commentId", article.getCommentId());
            }
            paras.put("orderBy", "CREATE_TIME");
            List<AppArticle> list = articleManager.queryArticleList(paras);
            if (CollectionUtils.isNotEmpty(list)) {
                Long groupId = 0L;
                resp.setResult(convertPojo(list, userPartyId, groupId));
            } else {
                resp.setResult(new ArrayList<ArticleVo>());
            }
        } catch (Exception e) {
            logger.warn("queryCommentsList error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryArticleCount", description = "查询当前用户发表的动态数")
    public Response<Integer> queryArticleCount() {

        Response<Integer> res = new Response<Integer>(0);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = new HashMap<String, Object>();
            paras.put("partyId", partyId);
            paras.put("articleType", Constant.ArticleType.SUBJECT.name());
            Integer queryArticleCount = articleManager.queryArticleCount(paras);
            if (queryArticleCount == null) {
                queryArticleCount = 0;
            }
            res.setResult(queryArticleCount);
            res.setCode(1000);
        } catch (Exception e) {
            logger.warn("queryArticleList error", e);
            res = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return res;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.publishArticle", description = "发表动态")
    public Response<Long> publishArticle(ArticleVo vo) {

        Response<Long> resp = ResponseUtils.successResponse();
        try {
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("comments", vo.getComments());
            List<AppArticle> list = articleManager.queryArticleList(paras);
            if (list.size() == 0 || StringUtils.isEmpty(vo.getComments())) {
                Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
                if (StringUtils.isEmpty(vo.getArticle()) && StringUtils.isEmpty(vo.getArticleAudio())
                        && StringUtils.isEmpty(vo.getArticleImgs())) {
                    resp.setFacade(FacadeEnums.INSERT_INVALID);
                    return resp;
                }
                // 判断该用户是否被禁止发表评论
                String users = SysConfigUtil.getStr("article_refuse_users");
                if (users != null && StringUtils.contains(users, partyId + "")) {
                    resp.setFacade(FacadeEnums.E_C_USER_INVALID);
                    return resp;
                }
                vo.setPartyId(partyId);
                vo.setArticleType(Constant.ArticleType.SUBJECT.name());
                AppArticle article = DTOUtils.map(vo, AppArticle.class);
                Boolean addArticle = articleManager.addArticle(article);
                if (addArticle) {
                    resp.setResult(article.getId());
                } else {
                    resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
                }
            } else {
                resp.setResult(list.get(0).getId());
            }
        } catch (Exception e) {
            logger.warn("publishArticle error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.publishComments", description = "发表评论")
    public Response<Long> publishComments(ArticleVo vo) {

        Response<Long> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("comments", vo.getComments());
            List<AppArticle> list = articleManager.queryArticleList(paras);
            if (list.size() == 0 || StringUtils.isEmpty(vo.getComments())) {
                // 判断该用户是否被禁止发表评论
                if (StringUtils.isEmpty(vo.getArticle()) && StringUtils.isEmpty(vo.getArticleAudio())) {
                    resp.setFacade(FacadeEnums.INSERT_INVALID);
                    return resp;
                }

                String users = SysConfigUtil.getStr("article_comments_refuse_users");
                if (users != null && StringUtils.contains(users, partyId + "")) {
                    resp.setFacade(FacadeEnums.E_C_USER_INVALID);
                    return resp;
                }

                AppArticle queryArticle = articleManager.queryArticle(vo.getReplyId());
                if (queryArticle == null) {// 判断动态是否存在
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                    return resp;
                }
                if (StringUtils.equals(queryArticle.getArticleType(), Constant.ArticleType.SUBJECT.name()) ||
                        StringUtils.equals(queryArticle.getArticleType(), Constant.ArticleType.BROADCAST.name()) ||
                        StringUtils.equals(queryArticle.getArticleType(), Constant.ArticleType.FINANCE.name()) ||
                        StringUtils.equals(queryArticle.getArticleType(), Constant.ArticleType.GOVERNMENT.name()) ||
                        StringUtils.equals(queryArticle.getArticleType(), Constant.ArticleType.RECRUITMENT.name()) ||
                        StringUtils.equals(queryArticle.getArticleType(), Constant.ArticleType.LEARNING_PPT.name()) ||
                        StringUtils.equals(queryArticle.getArticleType(), Constant.ArticleType.SHORT_VIDEO.name())
                        ) {
                    vo.setArticleId(queryArticle.getId());
                } else {
                    vo.setArticleId(queryArticle.getArticleId());
                }
                if (StringUtils.equals(queryArticle.getArticleType(), Constant.ArticleType.COMMENT.name())) {
                    vo.setCommentId(queryArticle.getId());
                } else {
                    vo.setCommentId(queryArticle.getCommentId());
                }
                //since 3.0.3 增加区域信息
                User user = userManager.queryUser(partyId);
                if (StringUtils.isNotEmpty(user.getProvince())) {
                    vo.setProvince(user.getProvince());
                }
                if (StringUtils.isNotEmpty(user.getCity())) {
                    vo.setCity(user.getCity());
                }
                if (StringUtils.isNotEmpty(user.getCounty())) {
                    vo.setCounty(user.getCounty());
                }
                if (StringUtils.isNotEmpty(user.getTown())) {
                    vo.setTown(user.getTown());
                }
                if (StringUtils.isNotEmpty(user.getVillage())) {
                    vo.setVillage(user.getVillage());
                }
                String userType = sessionHelper.getSessionProp(SessionConstants.USER_TYPE, String.class);
                AppArticle article = articleManager.queryArticle(vo.getArticleId());
                if (article == null) {// 判断动态是否存在
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                    return resp;
                }
                AppArticle queryArticle2 = articleManager.queryArticle(vo.getReplyId());
                vo.setPartyId(partyId);
                if (vo.getGroupId() == null) {
                    vo.setGroupId(0L);
                }
                Long id = articleManager.publishComments(DTOUtils.map(vo, AppArticle.class));

                if (StringUtils.length(vo.getArticle()) >= 15) {
                    // 判断是否已经领取过
                    addCommentTask(partyId, sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class));
                }
                resp.setResult(id);
                AppArticle appArticle = articleManager.queryArticle(id);
                if (!partyId.equals(queryArticle.getPartyId())) {
                    List<Long> partyIds = new ArrayList<>(1);
                    partyIds.add(queryArticle.getPartyId());
                    //判断是否是回复评论或是回复微博
                    //AppArticle appArticle = articleManager.queryArticle(id);
                    if (appArticle.getArticleType().equals(Constant.ArticleType.SUBCOMMENT.name())) {  //回复评论
                        // 发推送
                        if (partyIds.size() > 0) {
                            messageManager.sendMsg(MsgVo.builder().partyId(queryArticle.getPartyId()).msgTitle("乡邻app ").isSave(Constant.YESNO.YES)
                                    .message(user.getShowName() + "回复了你的评论，赶快去看看吧").msgType(Constant.MsgType.COMMENT_SUBJECT.name()).loginCheck(Constant.YESNO.NO.code).passCheck(Constant.YESNO.NO.code).expiryTime(0).isDeleted("Y").msgSource(Constant.MsgType.COMMENT_SUBJECT.name()).build(), partyIds);
                        }
                    } else {
                        String content = "微博";
                        if (article.getArticleType().equals(Constant.ArticleType.RECRUITMENT.name())) {
                            content = Constant.ArticleType.RECRUITMENT.desc;
                        }
                        if (article.getArticleType().equals(Constant.ArticleType.SHORT_VIDEO.name())) {
                            content = Constant.ArticleType.SHORT_VIDEO.desc;
                        }
                        //查发原微博的partyID
                        // 发推送
                        if (partyIds.size() > 0) {
                            messageManager.sendMsg(MsgVo.builder().partyId(queryArticle.getPartyId()).msgTitle("乡邻app ").isSave(Constant.YESNO.YES)
                                    .message(user.getShowName() + "评论你的乡邻" + content + "，赶快去看看吧").msgType(Constant.MsgType.COMMENT_SUBJECT.name()).loginCheck(Constant.YESNO.NO.code).passCheck(Constant.YESNO.NO.code).expiryTime(0).isDeleted("Y").msgSource(Constant.MsgType.COMMENT_SUBJECT.name()).build(), partyIds);
                        }
                    }
                }
            } else {
                resp.setResult(list.get(0).getId());
            }
        } catch (Exception e) {
            logger.warn("publishComments error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 完成
     *
     * @param partyId
     * @param deviceId
     */
    private void addCommentTask(Long partyId, String deviceId) {

        try {
            String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
            AppActivityTask task = AppActivityTask.builder().daily(today).partyId(partyId).activityCode(Constant.ActivityType.LUCKY.code)
                    .taskCode(Constant.ActivityTaskType.COMMENT.name()).build();
            List<AppActivityTask> tasks = activityManager.queryActivityTask(task, null);
            if (CollectionUtils.isEmpty(tasks)) {
                task = new AppActivityTask();
                task.setPartyId(partyId);
                task.setDeviceId(deviceId);
                task.setDaily(today);
                task.setActivityCode(Constant.ActivityType.LUCKY.code);
                task.setTaskCode(Constant.ActivityTaskType.COMMENT.name());
                task.setTaskDailyId(task.getPartyId() + task.getTaskCode() + task.getDaily() + "1");
                task.setTaskName(Constant.ActivityTaskType.COMMENT.desc);
                task.setTaskStatus(Constant.YESNO.YES.code);
                task.setUseStatus(Constant.YESNO.NO.code);
                activityManager.saveUpdateActivityTask(task);
            }
        } catch (Exception e) {
            logger.warn("addCommentTask", e);
        }
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.praiseArticle", description = "点赞和取消点赞(支持动态和评论)")
    public Response<Long> praiseArticle(Long articleId) {

        Response<Long> resp = ResponseUtils.successResponse();
        try {
//            String userType = sessionHelper.getSessionProp(SessionConstants.USER_TYPE, String.class);
            AppArticle article = articleManager.queryArticle(articleId);
            if (article == null) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                return resp;
            }
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("partyId", partyId);
            paras.put("articleId", articleId);
            paras.put("tipType", Constant.ArticleTipType.PRAISE.name());
            List<AppArticleTip> list = articleManager.queryArticleTipList(paras);
            AppArticleTip tip;
            if (CollectionUtils.isNotEmpty(list)) {// 已有点赞记录
                tip = list.get(0);
                if (StringUtils.equals(tip.getTipStatus(), Constant.YESNO.NO.code)) {// 点赞
                    tip.setTipStatus(Constant.YESNO.YES.code);
                } else {// 取消点赞
                    tip.setTipStatus(Constant.YESNO.NO.code);
                }
                articleManager.updateArticleTip(tip);
            } else {
                tip = new AppArticleTip();
                tip.setPartyId(partyId);
                tip.setArticleId(articleId);
                tip.setToPartyId(article.getPartyId());
                tip.setTipStatus(Constant.YESNO.YES.code);
                tip.setTipType(Constant.ArticleTipType.PRAISE.name());
                if (!partyId.equals(article.getPartyId())) {
                    String content = "微博";
                    if (article.getArticleType().equals(Constant.ArticleType.RECRUITMENT.name())) {
                        content = Constant.ArticleType.RECRUITMENT.desc;
                    }
                    if (article.getArticleType().equals(Constant.ArticleType.SHORT_VIDEO.name())) {
                        content = Constant.ArticleType.SHORT_VIDEO.desc;
                    }
                    User user = userManager.queryUser(partyId);
                    List<Long> partyIds = new ArrayList<>(1);
                    partyIds.add(article.getPartyId());
                    if (article.getArticleType().equals(Constant.ArticleType.SUBJECT.name()) || article.getArticleType().equals(Constant.ArticleType.RECRUITMENT.name()) || article.getArticleType().equals(Constant.ArticleType.SHORT_VIDEO.name())) {
                        tip.setContent("赞了你发表的" + content);
                        if (partyIds.size() > 0) {
                            messageManager.sendMsg(MsgVo.builder().partyId(article.getPartyId()).msgTitle("乡邻app ").isSave(Constant.YESNO.YES)
                                    .message(user.getShowName() + "赞了你的乡邻" + content + "，赶快去看看吧").msgType(Constant.MsgType.PRAISE_SUBJECT.name()).loginCheck(Constant.YESNO.NO.code).passCheck(Constant.YESNO.NO.code).expiryTime(0).isDeleted("Y").msgSource(Constant.MsgType.PRAISE_SUBJECT.name()).build(), partyIds);
                        }
                    } else {
                        tip.setContent("赞了你的评论");
                        if (partyIds.size() > 0) {
                            String showName = getShowName(user);
                            messageManager.sendMsg(MsgVo.builder().partyId(article.getPartyId()).msgTitle("乡邻app ").isSave(Constant.YESNO.YES)
                                    .message(showName + "赞了你的评论，赶快去看看吧").msgType(Constant.MsgType.PRAISE_COMMENT.name()).loginCheck(Constant.YESNO.NO.code).passCheck(Constant.YESNO.NO.code).expiryTime(0).isDeleted("Y").msgSource(Constant.MsgType.PRAISE_SUBJECT.name()).build(), partyIds);
                        }
                    }
                }
                if(article.getArticleId() != null){
                    AppArticle appArticle = articleManager.queryArticle(article.getArticleId());
                    if(appArticle != null){
                        tip.setArticleType(queryArticleTipType(appArticle));
                    }
                }else{
                    tip.setArticleType(article.getArticleType());  
                }
                articleManager.addArticleTip(tip);
            }

            // 判断下一步动作，更新状态
            paras.remove("partyId");
            paras.put("tipStatus", Constant.YESNO.YES.code);
            Integer praiseCount = articleManager.queryArticleTipCount(paras);
            article.setPraiseCount(praiseCount);
            articleManager.updateArticle(article);
            resp.setResult(tip.getId());
            // 判断点赞动态后是否满足集齐5个赞,动态15字
            if (StringUtils.isNotEmpty(article.getArticle())) {
                if (article.getArticleType().equals(Constant.ArticleType.SUBJECT.name())) {
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    if (fmt.format(article.getCreateTime()).toString().equals(fmt.format(new Date()).toString())) {
                        String str = article.getArticle();
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < article.getArticle().length(); i++) {
                            if ((str.charAt(i) + "").getBytes().length > 1) {
                                if (isChineseByScript(str.charAt(i))) {
                                    sb.append(str.charAt(i));
                                }
                            }
                        }
                        if (sb.length() >= 15 && article.getPraiseCount() >= 5) {
                            ActivityShareDaily queryInitShareDaily = activityShareManager.queryInitShareDaily(article
                                    .getPartyId());
                            if (queryInitShareDaily.getTaskStatus().equals("N")) {
                                queryInitShareDaily.setTaskStatus("Y");
                                activityShareManager.updateShareDaily(queryInitShareDaily);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("praiseArticle error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    // 使用UnicodeScript方法判断
    private boolean isChineseByScript(char c) {

        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        if (sc == Character.UnicodeScript.HAN) {
            return true;
        }
        return false;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.deleteArticle", description = "删除动态")
    public Response<Boolean> deleteArticle(Long articleId) {

        Response<Boolean> resp = new Response<Boolean>(false);
        try {
            // 先查询出动态信息
            AppArticle appArticle = articleManager.queryArticle(articleId);
            if (appArticle == null
                    || (!StringUtils.equalsIgnoreCase(appArticle.getArticleType(), Constant.ArticleType.SUBJECT.name())
                    && !StringUtils.equalsIgnoreCase(appArticle.getArticleType(), Constant.ArticleType.GOVERNMENT.name())
                    && !StringUtils.equalsIgnoreCase(appArticle.getArticleType(), Constant.ArticleType.FINANCE.name())
                    && !StringUtils.equalsIgnoreCase(appArticle.getArticleType(), Constant.ArticleType.BROADCAST.name())
                    && !StringUtils.equalsIgnoreCase(appArticle.getArticleType(), Constant.ArticleType.RECRUITMENT.name())
                    && !StringUtils.equalsIgnoreCase(appArticle.getArticleType(), Constant.ArticleType.LEARNING_PPT.name())
                    && !StringUtils.equalsIgnoreCase(appArticle.getArticleType(), Constant.ArticleType.SHORT_VIDEO.name())
            )) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                return resp;
            }
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            // 是否当前用户
            if (partyId.equals(appArticle.getPartyId())) {
                appArticle.setIsDeleted("Y");
                appArticle.setUpdateTime(new Date());
                // 后台逻辑删除处理
                Boolean updateArticle = articleManager.updateArticle(appArticle);
                AppRecruitJob appRecruitJob = recruitManager.queryRecruitJobByArticleId(articleId);
                if (appRecruitJob != null) {
                    recruitManager.updateRecruitJob(AppRecruitJob.builder().id(appRecruitJob.getId()).isDeleted("Y").build());
                }
                resp.setResult(updateArticle);
                resp.setCode(1000);
            } else {
                resp = ResponseUtils.toResponse(ResponseEnum.DELETE_ERROR);
            }
        } catch (Exception e) {
            logger.warn("deleteArticle error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.deleteComments", description = "删除评论")
    public Response<Boolean> deleteComments(Long id) {
        // 需要判断是否当前用户或动态发布者
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            AppArticle comments = articleManager.queryArticle(id);
            if (comments == null) {// 判断动态是否存在
                resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                return resp;
            }

            boolean flag = false;// 是否有删除权限
            if (comments.getPartyId().equals(partyId)) {// 当前用户删除
                logger.info("当前用户删除,{}", comments);
                flag = true;
            } else {// 判断是否为楼主
                AppArticle article = articleManager.queryArticle(comments.getArticleId());
                if (article.getPartyId().equals(partyId)) {
                    logger.info("楼主删除,{}", comments);
                    flag = true;
                }
            }

            if (flag) {
                resp.setResult(articleManager.deleteComments(comments));
            } else {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400006);
            }
        } catch (Exception e) {
            logger.warn("deleteComments error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryReprotList", description = "查询举报类型")
    public Response<List<String>> queryReprotList(Long id) {

        Response<List<String>> resp = ResponseUtils.successResponse();
        try {
            String result = SysConfigUtil.getStr("article_report_type");
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId != null) {
                Map<String, Object> paras = DTOUtils.queryMap();
                paras.put("partyId", partyId);
                paras.put("articleId", id);
                paras.put("tipType", Constant.ArticleTipType.REPORT.name());
                List<AppArticleTip> tips = articleManager.queryArticleTipList(paras);
                if (CollectionUtils.isNotEmpty(tips) && tips.size() == 1) {
                    result = result + ";" + tips.get(0).getComments();
                }
            }
            resp.setResult(Arrays.asList(StringUtils.split(result, ";")));
        } catch (Exception e) {
            logger.warn("queryReprotList error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.report", description = "举报")
    public Response<Boolean> report(Long id, String reprotMsg) {

        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            AppArticle appArticle = articleManager.queryArticle(id);
            if (appArticle == null) {// 判断动态是否存在
                resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                return resp;
            }
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("partyId", partyId);
            paras.put("articleId", id);
            paras.put("tipType", Constant.ArticleTipType.REPORT.name());
            List<AppArticleTip> tips = articleManager.queryArticleTipList(paras);
            if (CollectionUtils.isNotEmpty(tips)) {// 已经举报过
                resp.setResult(false);
            } else {
                AppArticleTip tip = new AppArticleTip();
                tip.setPartyId(partyId);
                tip.setTipType(Constant.ArticleTipType.REPORT.name());
                tip.setArticleId(id);
                tip.setToPartyId(appArticle.getPartyId());
                tip.setContent(convertReprotDetail(appArticle));
                tip.setArticleType(appArticle.getArticleType());
                tip.setComments(reprotMsg);
                articleManager.addArticleTip(tip);

                messageManager.sendMsg(MsgVo.builder().partyId(partyId).msgTitle("举报反馈").message("您的举报已收到，管理员查看后将做出处理，十分感谢您的积极举报！维护良好网络环境我们共同努力！").msgType(Constant.MsgType.NOTIFY.name()).build());

                resp.setResult(true);
            }
        } catch (Exception e) {
            logger.warn("report error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
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
        return sb.toString();
    }

    /**
     * 设置默认头像和昵称
     *
     * @param articleTipVo
     * @param appArticleTip
     * @return
     */
    private ArticleTipVo setNameAndImg(ArticleTipVo articleTipVo, AppArticleTip appArticleTip) {

        if (StringUtils.isNotEmpty(appArticleTip.getUser().getNikerName())) {
            articleTipVo.setUserNickName(appArticleTip.getUser().getNikerName());
        } else {
            articleTipVo.setUserNickName("xl" + appArticleTip.getPartyId());
        }
        String name = queryRealName(appArticleTip.getUser().getPartyId());
        if (name != null) {
            articleTipVo.setUserNickName(name);
        }
        if (StringUtils.isNotEmpty(appArticleTip.getUser().getHeadImg())) {
            articleTipVo.setUserHeadImg(appArticleTip.getUser().getHeadImg());
        } else {
            articleTipVo.setUserHeadImg(SysConfigUtil.getStr("default_user_headimg"));
        }
        if (StringUtils.isNotEmpty(appArticleTip.getToUser().getNikerName())) {
            articleTipVo.setToUserNickName(appArticleTip.getToUser().getNikerName());
        } else {
            articleTipVo.setToUserNickName("xl" + appArticleTip.getToUser().getPartyId());
        }
        name = queryRealName(appArticleTip.getToUser().getPartyId());
        if (name != null) {
            articleTipVo.setToUserNickName(name);
        }
        if (StringUtils.isNotEmpty(appArticleTip.getToUser().getHeadImg())) {
            articleTipVo.setToUserHeadImg(appArticleTip.getToUser().getHeadImg());
        } else {
            articleTipVo.setToUserHeadImg(SysConfigUtil.getStr("default_user_headimg"));
        }
        return articleTipVo;

    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryArticleTipList", description = "评论/赞查询")
    public Response<List<ArticleTipVo>> queryArticleTipList(ArticleTipReq req) {

        Response<List<ArticleTipVo>> resp = ResponseUtils.successResponse();
        Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        Map<String, Object> paras;
        try {
            paras = DTOUtils.beanToMap(req);
            paras.put("tipType", req.getTipType());
            paras.put("toPartyId", userPartyId);
            paras.put("tipStatus", "Y");
            paras.put("exceptPartyId", userPartyId);
            // 根据被评论人和消息类型查列表 exceptPartyId排除对自己的消息提醒
            List<AppArticleTip> list = articleManager.queryUpdateArticleTip(paras);
            List<ArticleTipVo> articleTipVolist = new ArrayList<ArticleTipVo>();
            for (AppArticleTip appArticleTip : list) {
                // 查询评论提醒
                AppArticle queryArticle = articleManager.queryArticle(appArticleTip.getArticleId());
                if (queryArticle != null && appArticleTip.getUser() != null) {
                    if (appArticleTip.getTipType().equals(Constant.ArticleTipType.COMMENT.name())) {
                        ArticleTipVo articleTipVo = DTOUtils.map(appArticleTip, ArticleTipVo.class);
                        // 加字段了无法使用工具方法
                        articleTipVo.setReplyId(articleTipVo.getArticleId());
                        articleTipVo.setArticleId(queryArticle.getArticleId());
                        articleTipVo.setArticleAudio(queryArticle.getArticleAudio());
                        articleTipVo.setArticleAudioLength(queryArticle.getArticleAudioLength());
                        articleTipVo = setNameAndImg(articleTipVo, appArticleTip);
                        articleTipVo.setDateTime(indecateTime(appArticleTip.getCreateTime()));
                        queryArticle = articleManager.queryArticle(queryArticle.getReplyId());
                        articleTipVo.setComments(convertReprotDetail(queryArticle));
                        articleTipVolist.add(articleTipVo);
                    }
                    // 查询点赞提醒
                    if (appArticleTip.getTipType().equals(Constant.ArticleTipType.PRAISE.name())) {
                        AppArticle queryArticle3 = articleManager.queryArticle(appArticleTip.getArticleId());
                        if (queryArticle3 != null) {
                            ArticleTipVo articleTipVo = DTOUtils.map(appArticleTip, ArticleTipVo.class);
                            articleTipVo.setComments(convertReprotDetail(queryArticle3));
                            articleTipVo.setReplyId(articleTipVo.getArticleId());
                            if (queryArticle3.getArticleId() != null) {
                                articleTipVo.setArticleId(queryArticle3.getArticleId());
                            } else {
                                articleTipVo.setArticleId(queryArticle3.getId());
                            }
                            articleTipVo = setNameAndImg(articleTipVo, appArticleTip);
                            articleTipVo.setDateTime(indecateTime(appArticleTip.getCreateTime()));
                            articleTipVolist.add(articleTipVo);
                        }
                    }

                    //查询分享提醒
                    if (appArticleTip.getTipType().equals(Constant.ArticleTipType.SHARE.name())) {
                        ArticleTipVo articleTipVo = DTOUtils.map(appArticleTip, ArticleTipVo.class);
                        // 加字段了无法使用工具方法
                        articleTipVo.setReplyId(articleTipVo.getArticleId());
                        articleTipVo.setArticleId(appArticleTip.getArticleId());
                        articleTipVo.setArticleAudio(queryArticle.getArticleAudio());
                        articleTipVo.setArticleAudioLength(queryArticle.getArticleAudioLength());
                        articleTipVo = setNameAndImg(articleTipVo, appArticleTip);
                        articleTipVo.setDateTime(indecateTime(appArticleTip.getCreateTime()));
                        articleTipVo.setContent("分享了你发表的微博");
                        //queryArticle = articleManager.queryArticle(queryArticle.getReplyId());
                        articleTipVo.setComments(convertReprotDetail(queryArticle));
                        articleTipVolist.add(articleTipVo);
                    }
                }
            }
            resp.setResult(articleTipVolist);
        } catch (Exception e) {
            logger.warn("queryArticleTipList error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 对象转换
     *
     * @param tips
     * @return
     */
    private List<ArticleTipVo> articleTipVoConverter(List<AppArticleTip> tips) {

        List<ArticleTipVo> result = new ArrayList<>(tips.size());
        try {
            ArticleTipVo vo = null;
            for (AppArticleTip tip : tips) {
                vo = DTOUtils.map(tip, ArticleTipVo.class);
                if (tip.getUser() != null) {
                    if (tip.getUser().getPartyId() != null) {
                        vo.setUserNickName("xl" + tip.getUser().getPartyId());
                    }
                    if (StringUtils.isNotEmpty(tip.getUser().getTrueName())) {
                        vo.setUserNickName(tip.getUser().getTrueName());
                    }
                    if (StringUtils.isNotEmpty(tip.getUser().getNikerName())) {
                        vo.setUserNickName(tip.getUser().getNikerName());
                    }
                }
                if (tip.getUser() != null && StringUtils.isNotEmpty(tip.getUser().getHeadImg())) {
                    vo.setUserHeadImg(tip.getUser().getHeadImg());
                }
                if (tip.getToUser() != null && StringUtils.isNotEmpty(tip.getToUser().getHeadImg())) {
                    vo.setToUserHeadImg(tip.getToUser().getHeadImg());
                }
                if (tip.getToUser() != null) {
                    if (tip.getToUser().getPartyId() != null) {
                        vo.setToUserNickName("xl" + tip.getToUser().getPartyId());
                    }
                    if (StringUtils.isNotEmpty(tip.getToUser().getTrueName())) {
                        vo.setToUserNickName(tip.getToUser().getTrueName());
                    }
                    if (StringUtils.isNotEmpty(tip.getToUser().getNikerName())) {
                        vo.setToUserNickName(tip.getToUser().getNikerName());
                    }
                }
                AppArticle queryArticle = articleManager.queryArticle(tip.getArticleId());
                if (queryArticle != null && StringUtils.isNotEmpty(queryArticle.getArticleAudio())) {
                    vo.setArticleAudio(queryArticle.getArticleAudio());
                }
                if (queryArticle != null && queryArticle.getArticleAudioLength() != null) {
                    vo.setArticleAudioLength(queryArticle.getArticleAudioLength());
                }
                if (queryArticle == null) {   //查pcweb举报用户时使用的
                    vo.setArticleId(0L);
                }
                /*if (queryArticle != null) {
                    vo.setComments(convertReprotDetail(queryArticle));
                } else {
                    vo.setComments(vo.getContent());
                }*/
                if (StringUtils.isNotEmpty(vo.getComments())) {
                    vo.setComments(vo.getComments());
                }
                vo.setDateTime(indecateTime(tip.getCreateTime()));
                result.add(vo);
            }
        } catch (Exception e) {
            logger.error("articleTipVoConverter error", e);
        }
        return result;
    }

    @Override

    public CommonResp<List<ArticleVo>> queryArticleListPage(ArticleReq req) {

        CommonResp<List<ArticleVo>> resp = new CommonResp<List<ArticleVo>>();
        try {
            req.setPageSize(500);
            Map<String, Object> paras = DTOUtils.beanToMap(req);
            paras.put("articleType", Constant.ArticleType.SUBJECT.name());
            if (req.getStartDate() != null && !(req.getStartDate().isEmpty())) {
                String date = req.getStartDate() + " 00:00:00";
                Date startDate = DateUtils.formatStr(date, "yyyy-MM-dd HH:mm:ss");
                paras.put("startDate", startDate);
            } else {
                paras.remove("startDate");
            }
            if (req.getEndDate() != null && !(req.getEndDate().isEmpty())) {
                String date = req.getEndDate() + " 23:59:59";
                Date endDate = DateUtils.formatStr(date, "yyyy-MM-dd HH:mm:ss");
                paras.put("endDate", endDate);
            } else {
                paras.remove("endDate");
            }
            if (req.getNickName() == null || req.getNickName().isEmpty()) {
                paras.remove("nickName");
            }
            if (req.getQueryKey() == null || req.getQueryKey().isEmpty()) {
                paras.remove("queryKey");
            }
            //paras.put("orderBy","FOLLOWCOUNT2");
            List<AppArticle> list = articleManager.queryArticleList(paras);
            for (AppArticle appArticle : list) {
                StringBuilder sb = new StringBuilder();
                if (appArticle.getArticle() != null) {
                    sb.append(appArticle.getArticle());
                }
                if (!StringUtils.isEmpty(appArticle.getArticleAudio())) {
                    sb.append("[语音]");
                }
                String imgs = appArticle.getArticleImgs();
                if (StringUtils.isNotEmpty(imgs)) {
                    int countMatches = StringUtils.countMatches(imgs, "http");
                    for (int i = 0; i < countMatches; i++) {
                        sb.append("[图片]");
                    }
                }
                String articleInfo = sb.toString();
                if (articleInfo.length() > 50) {
                    articleInfo = articleInfo.substring(0, 50) + "...";
                }
                appArticle.setArticle(articleInfo);
                /*if (appArticle.getUser() != null && appArticle.getUser().getTrueName() != null) {
                    appArticle.getUser().setNikerName(appArticle.getUser().getTrueName());
                }*/
                if (appArticle.getUser() != null && appArticle.getUser().getPartyId() != null) {
                    appArticle.getUser().setNikerName(queryRealName(appArticle.getUser().getPartyId()));
                }
            }
            List<ArticleVo> result = convertPojo(list);
            resp.setResult(result);
        } catch (Exception e) {
            logger.warn("queryArticleListPage error", e);
        }
        return resp;
    }

    @Override
    public Response<ArticleVo> updateArticle(ArticleVo vo) {
        Response<ArticleVo> resp = new Response<>(null);
        try {
            AppArticle appArticle1 = articleManager.queryArticle(vo.getId());
            if (appArticle1 == null) {  //如果微博被删除了，返回提示
                resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                return resp;
            }
            if (appArticle1 != null) {
                vo.setArticleType(appArticle1.getArticleType());
                vo.setPartyId(appArticle1.getPartyId());
                AppArticle appArticle = DTOUtils.map(vo, AppArticle.class);
                if (vo.getTopLevel() != null && vo.getTopLevel() == 9) {  //更新置顶时间
                    appArticle.setTopTime(new Date());
                }else{
                    appArticle.setTopTime(null); 
                }
                Boolean updateArticle = articleManager.updateArticle(appArticle);
                if ((vo.getTopLevel() != null && vo.getTopLevel() == 9) || (vo.getRecSign() != null && vo.getRecSign().equals(Constant.YESNO.YES.code))) { //微博或短视频上热门 或短视频别推荐,推送系统消息给用户
                    vo.setCreateTime(appArticle1.getCreateTime());
                    //推送消息并发送奖励
                    pushMsgAndReward(vo);
                }
                if (updateArticle) {
                    if (!StringUtils.equals(vo.getIsDeleted(), YesNo.Y.name())) {
                        AppArticle queryArticle = articleManager.queryArticle(vo.getId());
                        vo = DTOUtils.map(queryArticle, ArticleVo.class);
                    }
                    resp.setResult(vo);
                    resp.setCode(1000);
                } else {
                    resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
                }

                if (StringUtils.equals(vo.getIsDeleted(), YesNo.Y.name())) {
                    User user = userManager.queryUser(vo.getPartyId());
                    String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
                    String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
                    String taskCode = Constant.ActivityTaskType.DEL_SUBJECT.name();
                    if(appArticle1.getArticleType().equals(Constant.ArticleType.SHORT_VIDEO.name())){
                        taskCode = Constant.ActivityTaskType.DEL_SHORT_VIDEO.name();
                    }
                    AppActivityTask task = AppActivityTask.builder().partyId(vo.getPartyId()).deviceId(deviceId).mobilePhone(user.getLoginName()).activityCode(GoldService.ACTIVITY_PUBNISH).taskStatus(YesNo.Y.name()).taskCode(taskCode)
                            .daily(today).taskDailyId(today + System.currentTimeMillis()).useStatus(YesNo.Y.name()).taskName(YesNo.Y.name()).build();
                    task = activityManager.saveUpdateActivityTask(task);
                    activityManager.reward(vo.getPartyId(), task.getId(), null);
                }
            }
        } catch (Exception e) {
            logger.warn("updateArticle error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 微博或视频上热门货推荐 推送消息并发送奖励
     *
     * @param vo
     */
    private void pushMsgAndReward(ArticleVo vo) {
        try {
            String type = "";
            String message = "";
            String title = "";
            if (vo.getArticleType().equals(Constant.ArticleType.SUBJECT.name())) {
                type = Constant.ActivityTaskType.HOT_ARTICLE.name();
                title = "您的微博上热门了";
                int amount = queryRewardAmount(type);
                message = "恭喜您，您的微博上热门啦！奖励您" + amount + "金币，请查收。";
            } else if (vo.getArticleType().equals(Constant.ArticleType.SHORT_VIDEO.name())) {
                if (vo.getTopLevel() != null && vo.getTopLevel() == 9) { //短视频上热门
                    type = Constant.ActivityTaskType.SHORT_VODIE_HOT.name();
                    title = "您的短视频上热门了";
                    int amount = queryRewardAmount(type);
                    message = "恭喜您，您的短视频上热门啦！奖励您" + amount + "金币，请查收。";
                }
                if (vo.getRecSign() != null && vo.getRecSign().equals(YesNo.Y.name())) { //短视频被推荐
                    type = Constant.ActivityTaskType.SHORT_VODIE_REC.name();
                    title = "您的短视频被推荐";
                    int amount = queryRewardAmount(type);
                    message = "恭喜您，您的短视频被推荐啦！奖励您" + amount + "金币，请查收。";
                }
            }
            //发推送
            List<Long> partyIds = new ArrayList<>(1);
            partyIds.add(vo.getPartyId());
            User user = userManager.queryUser(vo.getPartyId());
            String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            //发奖励
            AppActivityTask task = AppActivityTask.builder().partyId(vo.getPartyId()).deviceId(deviceId).mobilePhone(user.getLoginName()).activityCode(GoldService.ACTIVITY_REWARD).taskStatus(YesNo.Y.name()).taskCode(type)
                    .daily(today).taskDailyId(vo.getCreateTime().getTime()+vo.getId()+type).useStatus(YesNo.Y.name()).taskName(YesNo.Y.name()).build();
            task = activityManager.saveUpdateActivityTask(task);
            List<AppTransaction> reward = activityManager.reward(vo.getPartyId(), task.getId(), null);
            if(CollectionUtils.isNotEmpty(reward)){
                messageManager.sendMsg(MsgVo.builder().partyId(vo.getPartyId()).msgTitle(title).isSave(Constant.YESNO.YES)
                        .message(message).msgType(type).loginCheck(Constant.YESNO.NO.code).passCheck(Constant.YESNO.NO.code).expiryTime(0).isDeleted("Y").build(), partyIds);   
            }
        } catch (Exception e) {
            logger.warn("pushMsgAndReward :"+e);
        }
    }

    private int queryRewardAmount(String type) {
        Map<String, Object> param = new HashMap<>();
        param.put("rewardCategary", type);
        param.put("activityCode", GoldService.ACTIVITY_REWARD);
        List<AppActivityReward> list = activityManager.queryActivityReward(param);
        if (list.size() > 0) {
            return list.get(0).getStartAmt().intValue();
        }
        return 0;
    }

    private double queryWeightAdjust(String t) {
        double weight = 0;
        String weightAdjust = SysConfigUtil.getStr("WEIGHT_ADJUST");
        JSONArray jsonArray = JSONArray.fromObject(weightAdjust);
        List<Map<String, Object>> list2 = (List<Map<String, Object>>) JSONArray.toCollection(jsonArray, Map.class);
        for (int i = 0; i < list2.size(); i++) {
            if (list2.get(i).get("key").toString().equals(t)) {
                weight = Double.valueOf(list2.get(i).get("value").toString());
                break;
            }
        }
        return weight;
    }

    @Override
    public CommonResp<List<ArticleTipVo>> queryArticleTipListPage(ArticleTipReq req) {

        CommonResp<List<ArticleTipVo>> resp = new CommonResp<List<ArticleTipVo>>();
        try {
            req.setPageSize(10000);
            Map<String, Object> paras = DTOUtils.beanToMap(req);
            if (req.getStartDate() != null && !(req.getStartDate().isEmpty())) {
                String date = req.getStartDate() + " 00:00:00";
                Date startDate = DateUtils.formatStr(date, "yyyy-MM-dd HH:mm:ss");
                paras.put("startDate", startDate);
            } else {
                paras.remove("startDate");
            }
            if (req.getEndDate() != null && !(req.getEndDate().isEmpty())) {
                String date = req.getEndDate() + " 23:59:59";
                Date endDate = DateUtils.formatStr(date, "yyyy-MM-dd HH:mm:ss");
                paras.put("endDate", endDate);
            } else {
                paras.remove("endDate");
            }
            List<AppArticleTip> list = articleManager.queryArticleTipList(paras);
            List<ArticleTipVo> result = articleTipVoConverter(list);
            resp.setResult(result);
        } catch (Exception e) {
            logger.warn("queryArticleTipListPage error", e);
        }
        return resp;
    }

    @Override
    public Response<ArticleTipVo> updateArticleTip(ArticleTipVo vo) {

        Response<ArticleTipVo> resp = new Response<>(null);
        try {

            AppArticleTip appArticle = DTOUtils.map(vo, AppArticleTip.class);
            Boolean updateArticleTip = articleManager.updateArticleTip(appArticle);
            if (vo.getIsDeleted()!=null){
                if ( vo.getIsDeleted().equals(Constant.Delete_Y_N.Y.name())){
                    appArticle = articleManager.queryArticleTip(vo.getId());
                    articleManager.updateArticle(AppArticle.builder().id(appArticle.getArticleId()).isDeleted(Constant.Delete_Y_N.Y.name()).build());
                }
            }
            if (updateArticleTip) {
                AppArticleTip appArticleTip = articleManager.queryArticleTip(vo.getId());
                vo = DTOUtils.map(appArticleTip, ArticleTipVo.class);
                resp.setResult(vo);
                resp.setCode(1000);
            } else {
                resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
            }
        } catch (Exception e) {
            logger.warn("updateArticleTip error", e);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryArticleTips", description = "查询未读点赞数、回复数和分享数")
    public Response<ArticleTip> queryArticleTips() {

        Response<ArticleTip> resp = ResponseUtils.successResponse();
        ArticleTip articleTip = new ArticleTip();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("toPartyId", partyId);
            paras.put("readStatus", "N");
            paras.put("tipStatus", "Y");
            paras.put("exceptPartyId", partyId);
            //查询未读分享数
            paras.put("tipType", Constant.ArticleTipType.SHARE.name());
            Integer queryShareCount = articleManager.queryArticleTipCount(paras);
            paras.put("tipType", Constant.ArticleTipType.PRAISE.name());
            // 根据partyId查当前用户的动态和评论id
            Integer queryPraiseCount = articleManager.queryArticleTipCount(paras);
            paras.put("tipType", Constant.ArticleTipType.COMMENT.name());
            Integer queryReplyCount = articleManager.queryArticleTipCount(paras);
            articleTip.setPraiseCount(queryPraiseCount);
            articleTip.setReplyCount(queryReplyCount);
            articleTip.setShareCount(queryShareCount);
            resp.setResult(articleTip);
        } catch (Exception e) {
            logger.warn("queryArticleTips error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryArticleShareInfo", description = "查询动态分享信息")
    public Response<WechatShareInfo> queryArticleShareInfo(Long articleId) {

        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        Response<WechatShareInfo> resp = ResponseUtils.successResponse();
        AppArticle queryArticle = articleManager.queryArticle(articleId);
        if (queryArticle == null) {// 判断动态是否已被删除
            resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
            return resp;
        }
        WechatShareInfo wechatShareInfo = new WechatShareInfo();
        wechatShareInfo.setTitle("来自乡邻app的分享");
        if(queryArticle.getArticleType().equals(Constant.ArticleType.SHORT_VIDEO.name())){
            wechatShareInfo.setUrl(SysConfigUtil.getStr("SHORT_VIDEO_SHARE_URL") + "?id=" + queryArticle.getId() + "&partyId=" + partyId);
        }else{
            wechatShareInfo.setUrl(SysConfigUtil.getStr("article_share_url") + "?id=" + queryArticle.getId() + "&partyId=" + partyId);  
        }
        String imgs = queryArticle.getArticleImgs();
        if (StringUtils.isNotEmpty(imgs)) {
            String[] imgsarr = imgs.split(";");
            wechatShareInfo.setTitieImg(imgsarr[0] + smallImg);
        } else {
            if (queryArticle.getArticleType().equals(Constant.ArticleType.RECRUITMENT.name())) {
                wechatShareInfo.setTitieImg(SysConfigUtil.getStr("recruitment_default_img") + smallImg);
            } else {
                // 展示乡邻app logo
                wechatShareInfo.setTitieImg(SysConfigUtil.getStr("article_share_img") + smallImg);
            }
        }
        String contentStr = "@你，快来看看我分享了什么！";
        wechatShareInfo.setContent(shareContent(queryArticle, contentStr));
        String contentStrPYQ = "你有一条新分享未查看 ";
        wechatShareInfo.setContentPYQ(shareContent(queryArticle, contentStrPYQ));
        resp.setResult(wechatShareInfo);
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.collectArticle", description = "收藏动态")
    public Response<Boolean> collectArticle(Long id) {

        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            AppArticle appArticle = articleManager.queryArticle(id);
            if (appArticle == null) {// 判断动态是否已被删除
                resp.setFacade(FacadeEnums.ERROR_CHAT_400005);
                return resp;
            }
            AppArticleTip appArticleTip = new AppArticleTip();
            appArticleTip.setArticleId(id);
            appArticleTip.setPartyId(partyId);
            appArticleTip.setArticleType(appArticle.getArticleType());
            appArticleTip.setTipType(Constant.ArticleTipType.COLLET.name());// 设置类别为收藏
            AppArticle article = appArticle;
            if (article != null && StringUtils.isNotEmpty(article.getArticle()) && article.getArticle().length() > 300) {
                appArticleTip.setContent(article.getArticle().substring(0, 299));
            } else if (article != null && StringUtils.isNotEmpty(article.getArticle()) ) {
                appArticleTip.setContent(article.getArticle());
            }
            appArticleTip.setToPartyId(article.getPartyId());
            appArticleTip.setArticleType(queryArticleTipType(article));
            Boolean addArticleTip = articleManager.addArticleTip(appArticleTip);
            resp.setResult(addArticleTip);
        } catch (Exception e) {
            logger.warn("collectArticle error", e);
            resp.setFacade(FacadeEnums.INSERT_FAIL);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.removeCollectArticle", description = "删除收藏")
    public Response<Boolean> removeCollectArticle(Long id) {

        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("articleId", id);
            paras.put("partyId", partyId);
            List<AppArticleTip> articleTipList = articleManager.queryArticleTipList(paras);
            if (CollectionUtils.isNotEmpty(articleTipList)) {
                AppArticleTip appArticleTip = articleTipList.get(0);
                appArticleTip.setIsDeleted(Constant.YESNO.YES.code);
                Boolean updateArticleTip = articleManager.updateArticleTip(appArticleTip);
                resp.setResult(updateArticleTip);
            } else {
                resp.setResult(true);
            }
        } catch (Exception e) {
            logger.warn("removeCollectArticle error", e);
            resp.setFacade(FacadeEnums.DELETE_FAIL);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryCollectArticle", description = "分页查询收藏的动态")
    public Response<List<ArticleVo>> queryCollectArticle(PageReq req) {

        Response<List<ArticleVo>> resp = ResponseUtils.successResponse();
        List<ArticleVo> list = new ArrayList<ArticleVo>();
        List<AppArticle> listAppArticle = new ArrayList<AppArticle>();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.beanToMap(req);
            paras.put("partyId", partyId);
            paras.put("tipType", Constant.ArticleTipType.COLLET.name());
            List<AppArticleTip> queryArticleTipList = articleManager.queryArticleTipList(paras);
            if (CollectionUtils.isNotEmpty(queryArticleTipList)) {
                for (AppArticleTip appArticleTip : queryArticleTipList) {
                    // 根据id查收藏的动态
                    AppArticle article = articleManager.queryArticle(appArticleTip.getArticleId());
                    if (article == null) {// 收藏的动态已被删除
                        AppArticle articleNew = new AppArticle();
                        articleNew.setUser(appArticleTip.getToUser());
                        articleNew.setId(appArticleTip.getArticleId());
                        articleNew.setArticle("抱歉,该微博已被删除");
                        articleNew.setPartyId(appArticleTip.getToPartyId());
                        articleNew.setTopLevel(0);
                        listAppArticle.add(articleNew);
                    } else {
                        // 先做是否点赞判断
                        paras.put("tipType", Constant.ArticleTipType.PRAISE.name());
                        paras.put("articleId", appArticleTip.getArticleId());
                        List<AppArticleTip> queryArticleTipList2 = articleManager.queryArticleTipList(paras);
                        if (CollectionUtils.isEmpty(queryArticleTipList2)) {
                            article.setArticleStatus(Constant.YESNO.NO.code);
                        } else {
                            article.setArticleStatus(queryArticleTipList2.get(0).getTipStatus());
                        }
                        article.setTopLevel(0);
                        listAppArticle.add(article);
                    }
                }
                Long groupId = 0L;
                list = convertPojo(listAppArticle, partyId, groupId);
            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.warn("queryCollectArticle error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }

        return resp;
    }

    /**
     * 动态雷彪查询
     *
     * @param req
     * @return
     * @since v3.0.3
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryArticleListV2", description = "动态列表查询V2")
    public Response<List<ArticleVo>> queryArticleListV2(ArticleReq req) {

        Response<List<ArticleVo>> resp = ResponseUtils.successResponse();
        try {
            String userType = sessionHelper.getSessionProp(SessionConstants.USER_TYPE, String.class);
            if (StringUtils.isEmpty(userType)) {
                userType = Constant.UserType.visitor.name();
            }
            Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("startPage", req.getStartPage());
            paras.put("pageSize", req.getPageSize());
            paras.put("partyId", req.getPartyId());
            paras.put("articleType", Constant.ArticleType.SUBJECT.name());
            if (StringUtils.isNotEmpty(req.getProvince())) {
                paras.put("province", req.getProvince());
            }
            if (StringUtils.isNotEmpty(req.getCity())) {
                paras.put("city", req.getCity());
            }
            if (StringUtils.isNotEmpty(req.getCounty())) {
                paras.put("county", req.getCounty());
            }
            if (StringUtils.isNotEmpty(req.getTown())) {
                paras.put("town", req.getTown());
            }
            if (StringUtils.isNotEmpty(req.getVillage())) {
                paras.put("village", req.getVillage());
            }
            if (StringUtils.isNotEmpty(req.getOrderBy())) {
                paras.put("orderBy", "REPLY_COUNT");
            }
            List<AppArticle> list = new LinkedList<>();
            if (req.getStartPage() <= 1) {//第一页
                paras.put("topLevel", 9);//查询系统消息
                list.addAll(articleManager.queryArticleList(paras));
            }
            paras.put("groupId", req.getGroupId());
            if (req.getGroupId() == null) {
                paras.put("groupId", 0);//公开群组id
            }
            paras.put("topLevel", 0);//查询普通消息
            Long groupId = 0L;
            if (req.getGroupId() != null && req.getGroupId() > 0) {
                groupId = req.getGroupId();
            }
            list.addAll(articleManager.queryArticleList(paras));
            List<ArticleVo> result = convertPojo(list, userPartyId, groupId);
            resp.setResult(result);
            resp.setCode(1000);
        } catch (Exception e) {
            logger.warn("queryArticleList error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * ArticleVo 对象转换
     *
     * @param list
     * @param userPartyId 当前登陆用户
     * @return
     */
    private List<ArticleVo> convertPojo(List<AppArticle> list, Long userPartyId, Long groupId) {

        List<ArticleVo> result = new ArrayList<>(list.size());
        Long managerId = null;
        if (groupId != null) {
            AppGroup appGroup = groupManager.Group(groupId);
            if (appGroup != null) {
                managerId = appGroup.getManagePartyId();
            }
        }
        if (list.size() > 0) {
            for (AppArticle article : list) {
                try {
                    ArticleVo vo;
                    vo = DTOUtils.map(article, ArticleVo.class);
                    if (StringUtils.isNotEmpty(vo.getImage())) {
                        vo.setSmallImage(vo.getImage() + smallImg);
                    }
                    if (StringUtils.isNotEmpty(vo.getArticleImgs())) {
                        vo = selectArticle(vo);
                    }

                    if (userPartyId != null) {
                        AppUserRelation relation = userRelationManager.queryRelation(userPartyId, vo.getPartyId());
                        if (relation != null && StringUtils.equals(relation.getFollowStatus(), YesNo.Y.name())) {
                            vo.setBothStatus(Constant.RelationStatus.FOLLOW.name());
                        } else {
                            vo.setBothStatus(Constant.RelationStatus.UNFOLLOW.name());
                        }
                    }
                    if (article.getCreateTime() != null) {
                        if (Objects.equals(Constant.ArticleType.LEARNING_PPT.name(), article.getArticleType())) {
                            vo.setDateTime(DateUtils.formatDateTime(DateUtils.DATETIME_NOSECOND, article.getCreateTime()));
                        } else {
                            vo.setDateTime(indecateTime(article.getCreateTime()));
                        }
                    }
                    if (article.getUser() != null) {
                        vo.setUserType(article.getUser().getUserType());
                        if (StringUtils.isNotEmpty(article.getUser().getNikerName())) {
                            vo.setNikeName(article.getUser().getNikerName());
                        } else {
                            if (managerId != null && managerId.equals(vo.getPartyId())) {
                                vo.setNikeName("xl" + article.getPartyId());
                            }

                        }
                        //实名认证信息
                        User user = userManager.queryUser(article.getUser().getPartyId());
                        if (user != null) {
                            if (StringUtils.isNotEmpty(user.getShowName())) {
                                vo.setNikeName(user.getShowName());
                            } else {
                                vo.setNikeName(getShowName(user));
                            }

                            if (StringUtils.isNotEmpty(user.getTrueName())) {
                                vo.setIsAuth(true);
                            }
                        }
                        if (StringUtils.isNotEmpty(article.getUser().getShowName())) {
                            vo.setShowName(article.getUser().getShowName());
                        } else {
                            vo.setShowName(getShowName(user));
                        }
                        if (article.getReplyPartyId() != null) {
                            User replyUser = userManager.queryUser(article.getReplyPartyId());
                            if (replyUser != null) {
                                if (StringUtils.isNotEmpty(replyUser.getShowName())) {
                                    vo.setReplyShowName(replyUser.getShowName());
                                } else {
                                    vo.setReplyShowName(getShowName(replyUser));
                                }
                            }
                        }
                        if (StringUtils.isNotEmpty(article.getUser().getHeadImg())) {
                            vo.setHeadImg(article.getUser().getHeadImg());
                        } else {
                            vo.setHeadImg(SysConfigUtil.getStr("default_user_headimg"));
                        }
                        //取发表用户的类型
                        if (groupId != null && groupId.equals(0L)) {
                            vo.setUserType(article.getUser().getUserType());
                        }
                        vo.setGender(article.getUser().getGender());
                    } else {
                        //vo.setReplyShowName("xl" + article.getReplyPartyId());
                        vo.setShowName("xl" + article.getPartyId());
                    }
                    if (article.getReplyUser() != null) {
                        if (StringUtils.isNotEmpty(article.getReplyUser().getNikerName())) {
                            vo.setReplyNickName(article.getReplyUser().getNikerName());
                        } else {
                            vo.setReplyNickName("xl" + article.getReplyPartyId());
                        }
                        //实名认证信息
                        String name = getUserName(article.getUser().getPartyId());
                        //String name = queryRealName(article.getReplyUser().getPartyId());
                        if (name != null) {
                            vo.setReplyNickName(name);
                        }
                        if (StringUtils.isNotEmpty(article.getReplyUser().getHeadImg())) {
                            vo.setReplyHeadImg(article.getReplyUser().getHeadImg());
                        } else {
                            vo.setReplyHeadImg(SysConfigUtil.getStr("default_user_headimg"));
                        }
                    }
                    // 查询性别
                    if (userPartyId != null) {
                        Map<String, Object> paras = DTOUtils.queryMap();
                        paras.put("pageSize", 100);
                        paras.put("partyId", userPartyId);
                        paras.put("articleId", article.getId());
                        List<AppArticleTip> tips = articleManager.queryArticleTipList(paras);
                        if (CollectionUtils.isNotEmpty(tips)) {
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
                    logger.warn("convertPojo error", e);
                }
            }
        }
        return result;
    }

    /**
     * 返回微博缩略图
     *
     * @param vo
     * @return
     */
    private ArticleVo selectArticle(ArticleVo vo) {
        try {
            if (StringUtils.isNotEmpty(vo.getArticleImgs())) {
                String[] articleImgs = vo.getArticleImgs().split(";");
                String[] smallArticleImge = new String[articleImgs.length];
                if (articleImgs.length > 0) {
                    for (int i = 0; i < articleImgs.length; i++) {
                        smallArticleImge[i] = articleImgs[i] + smallImg;
                    }
                }
                vo.setSmallArticleImgs(StringUtils.join(smallArticleImge, ";"));
                vo.setArticleImageArray(articleImgs);
                vo.setSmallArticleImgsArray(smallArticleImge);
            }
        } catch (Exception e) {
            logger.warn("selectArticle error", e);
        }
        return vo;
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

    /**
     * 动态分享
     *
     * @param id
     * @return
     * @since v3.0.3
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.shareArticle", description = "分页查询收藏的动态")
    public Response<Boolean> shareArticle(Long id) {

        Response<Boolean> resp = ResponseUtils.successResponse(true);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId != null) {
                AppArticleTip appArticleTip = new AppArticleTip();
                appArticleTip.setArticleId(id);
                appArticleTip.setPartyId(partyId);
                appArticleTip.setTipType(Constant.ArticleTipType.SHARE.name());// 设置类别为收藏
                AppArticle article = articleManager.queryArticle(id);
                appArticleTip.setContent(article.getArticle());
                appArticleTip.setToPartyId(article.getPartyId());
                appArticleTip.setArticleType(queryArticleTipType(article));
                Boolean addArticleTip = articleManager.addArticleTip(appArticleTip);
                resp.setResult(addArticleTip);
                //发推送
                if (!partyId.equals(article.getPartyId())) {
                    User user = userManager.queryUser(partyId);
                    List<Long> partyIds = new ArrayList<>(1);
                    partyIds.add(article.getPartyId());
                    if (partyIds.size() > 0) {
                        String content = "微博";
                        if (article.getArticleType().equals(Constant.ArticleType.RECRUITMENT.name())) {
                            content = Constant.ArticleType.RECRUITMENT.desc;
                        }
                        if (article.getArticleType().equals(Constant.ArticleType.SHORT_VIDEO.name())) {
                            content = Constant.ArticleType.SHORT_VIDEO.desc;
                        }
                        messageManager.sendMsg(MsgVo.builder().partyId(article.getPartyId()).msgTitle("乡邻app ").isSave(Constant.YESNO.YES)
                                .message(user.getShowName() + "转发了你的乡邻" + content + "，赶快去看看吧").msgType(Constant.MsgType.FORWARD_SUBJECT.name()).loginCheck(Constant.YESNO.NO.code).passCheck(Constant.YESNO.NO.code).expiryTime(0).isDeleted("Y").msgSource(Constant.MsgType.FORWARD_SUBJECT.name()).build(), partyIds);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("collectArticle error", e);
            resp.setFacade(FacadeEnums.INSERT_FAIL);
        }
        return resp;
    }

    /**
     * 返回提示类型
     * @param article
     * @return
     */
    private String queryArticleTipType(AppArticle article) {
        if(article == null){
            return null;
        }
        if(article.getArticleType().equals(Constant.ArticleType.SHORT_VIDEO.name())){
            return Constant.ArticleType.SHORT_VIDEO.name();
        }else if(article.getArticleType().equals(Constant.ArticleType.SUBJECT.name())){
            return Constant.ArticleType.SUBJECT.name();
        }
        return null;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.publishArticleV1", description = "发表微博")
    public Response<Boolean> publishArticleV1(ArticleVo vo) {
        return responseCacheUtils.execute(() -> {
            return publishAddArticle(vo);
        });
    }

    /**
     * 发布article公共方法
     *
     * @param vo
     * @return
     * @throws Exception
     */
    private Boolean publishAddArticle(ArticleVo vo) throws BusiException {
        Boolean result = true;
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
        String md5 = MD5.md5(partyId + JSON.toJSONString(vo));
        if (redisUtil.isRepeat(md5, 3)) {
            throw new BusiException(FacadeEnums.ERROR_REPEAT);
        }
        Map<String, Object> paras = DTOUtils.queryMap();
        if (StringUtils.isEmpty(vo.getArticle()) && StringUtils.isEmpty(vo.getArticleAudio())
                && StringUtils.isEmpty(vo.getArticleImgs()) && StringUtils.isEmpty(vo.getTitle()) && StringUtils.isEmpty(vo.getVideoUrl()) && StringUtils.isEmpty(vo.getShareUrl())) {
            throw new BusiException(FacadeEnums.INSERT_INVALID);
        }
        // 判断该用户是否被禁止发表评论
        String users = SysConfigUtil.getStr("article_refuse_users");
        if (users != null && StringUtils.contains(users, partyId + "")) {
            throw new BusiException(FacadeEnums.E_C_USER_INVALID);
        }
        vo.setPartyId(partyId);
        if (StringUtils.isNotEmpty(vo.getArticleType())) {
            vo.setArticleType(vo.getArticleType());
        } else {
            vo.setArticleType(Constant.ArticleType.SUBJECT.name());
        }
        Boolean addArticle = false;
        for (int i = 0; i < vo.getGroupIds().length; i++) {
            vo.setGroupId(vo.getGroupIds()[i]);
            AppArticle article = DTOUtils.map(vo, AppArticle.class);
            if (!article.getArticleType().equals(Constant.ArticleType.RECRUITMENT.name())) {
                //查询用户的家乡地址
                article = queryUserAddress(article);
            }
            article.setShareTitle(StringUtils.substring(article.getShareTitle(), 0, 100));
            addArticle = articleManager.addArticle(article);
            if (article.getArticleType().equals(Constant.ArticleType.RECRUITMENT.name())) {
                //同步发布到AppRecruitJob
                insertAppRecruitJob(article);
            }
            if (vo.getArticleType().equals(Constant.ArticleType.RECRUITMENT.name())) {
                //将发布的地址同步到扩展表
                createUserAddress(article, deviceId);
                if (vo.getIsSubject().equals(YesNo.Y.name())) {  //判断是否发布到微博
                    article.setArticleType(Constant.ArticleType.SUBJECT.name());
                    if (StringUtils.isEmpty(article.getArticleImgs())) {
                        article.setShareImg(SysConfigUtil.getStr("recruitment_default_img") + smallImg);
                    } else {
                        String[] imgs = article.getArticleImgs().split(";");
                        article.setShareImg(imgs[0]);
                    }
                    article.setShareUrl("NATIVE:RECRUITMENT:" + article.getId());
                    article.setShareTitle(StringUtils.substring(article.getArticle(), 0, 100));
                    article.setArticle("");
                    article = queryUserAddress(article);
                    result = articleManager.addArticle(article);
                }
            }

            if (vo.getArticleType().equals(Constant.ArticleType.BROADCAST.name())) {   //如果是发广播，就给该村的人发推送
                //查询这个村所有的人
                paras.clear();
                paras.put("groupId", vo.getGroupId());
                List<AppGroupMember> members = groupManager.queryAppMemberByParas(paras);
                List<Long> partyIds = new ArrayList<>(1);
                if (members.size() > 0) {
                    for (AppGroupMember member : members) {
                        if (!partyId.equals(member.getPartyId())) {
                            partyIds.add(member.getPartyId());
                        }
                    }
                }
                if (partyIds.size() > 0) {
                    messageManager.sendMsg(MsgVo.builder().partyId(partyId).msgTitle("村里有广播啦 ").isSave(Constant.YESNO.YES)
                            .message(article.getTitle()).msgType(Constant.MsgType.BROADCAST.name()).loginCheck(Constant.YESNO.NO.code).passCheck(Constant.YESNO.NO.code).expiryTime(0).isDeleted("Y").msgSource(article.getId() + "").build(), partyIds);
                }
            }
        }
        return result;
    }


    private void insertAppRecruitJob(AppArticle article) {
        try {
            AppRecruitJob appRecruitJob = new AppRecruitJob();
            if (StringUtils.isNotEmpty(article.getContactsPhone())) {
                appRecruitJob.setContactPhone(article.getContactsPhone());
            }
            if (StringUtils.isNotEmpty(article.getContacts())) {
                appRecruitJob.setContactName(article.getContacts());
            }
            appRecruitJob.setArticleId(article.getId());
            appRecruitJob.setPartyId(article.getPartyId());
            if (StringUtils.isNotEmpty(article.getArticle())) {
                appRecruitJob.setDesc(article.getArticle());
                if (article.getArticle().length() > 16) {
                    appRecruitJob.setJobName(StringUtils.substring(article.getArticle(), 0, 32));
                } else {
                    appRecruitJob.setJobName(article.getArticle());
                }
            }
            if (StringUtils.isNotEmpty(article.getArticleImgs())) {
                appRecruitJob.setImages(article.getArticleImgs());
            }
            if (StringUtils.isNotEmpty(article.getCounty())) {
                appRecruitJob.setCounty(article.getCounty());
            }
            if (StringUtils.isNotEmpty(article.getCity())) {
                appRecruitJob.setCity(article.getCity());
            }
            if (StringUtils.isNotEmpty(article.getProvince())) {
                appRecruitJob.setProvince(article.getProvince());
            }
            //列表中的薪资待遇一律默认面议
            appRecruitJob.setPayType(Constant.RecruitPayType.F.name());
            //列表中公司名称取原岗位招聘中联系人名字
            appRecruitJob.setCompanyName(article.getContacts());
            recruitManager.insertRecruitJob(appRecruitJob);
        } catch (Exception e) {
            logger.warn("insertAppRecruitJob error", e);
        }
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
            logger.warn("queryUserAddress error", e);
        }
        return article;
    }

    private void createUserAddress(AppArticle article, String deviceId) {

        if (article != null) {
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
            if (StringUtils.isNotEmpty(article.getProvince())) {
                mapVo.setValue(article.getProvince());
            } else {
                mapVo.setValue("");
            }
            if (StringUtils.isNotEmpty(article.getCity())) {
                mapVo2.setValue(article.getCity());
            } else {
                mapVo2.setValue("");
            }
            if (StringUtils.isNotEmpty(article.getCounty())) {
                mapVo3.setValue(article.getCounty());
            } else {
                mapVo3.setValue("");
            }
            if (StringUtils.isNotEmpty(article.getTown())) {
                mapVo4.setValue(article.getTown());
            } else {
                mapVo4.setValue("");
            }
            if (StringUtils.isNotEmpty(article.getVillage())) {
                mapVo5.setValue(article.getVillage());
            } else {
                mapVo5.setValue("");
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
                paras.put("relationId", article.getPartyId());
                paras.put("type", User.class.getSimpleName());
                paras.put("ekey", EnumKeyValue.User.USER_USED_ADDRESS.name());
                paras.put("value", locationJson);
                List<AppPropExtend> appPropExtends = propExtendManager.queryChannel(paras);
                if (appPropExtends.size() > 0) {     //修改时间
                    propExtendManager.update(AppPropExtend.builder().id(appPropExtends.get(0).getId()).build());
                } else {   //新增一条
                    propExtendManager.insert(AppPropExtend.builder().relationId(article.getPartyId()).ekey(EnumKeyValue.User.USER_USED_ADDRESS.name()).deviceId(deviceId).value(locationJson).type(User.class.getSimpleName()).build());
                }

            }
        }
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryArticleListV3", description = "动态列表查询V3")
    public Response<List<ArticleVo>> queryArticleListV3(ArticleReq req) {

        Response<List<ArticleVo>> resp = ResponseUtils.successResponse();
        try {
            String userType = sessionHelper.getSessionProp(SessionConstants.USER_TYPE, String.class);
            if (StringUtils.isEmpty(userType)) {
                userType = Constant.UserType.visitor.name();
            }
            Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("startPage", req.getStartPage());
            paras.put("pageSize", req.getPageSize());
            paras.put("partyId", req.getPartyId());
            paras.put("articleType", req.getArticleType());
            paras.put("lastId", req.getLastId());
            List<AppArticle> list = new LinkedList<>();
            if (req.getStartPage() <= 1 && (req.getLastId() == null || req.getLastId() <= 0)) {//第一页
                paras.put("topLevel", 9);//查询系统消息
                paras.put("pageSize", 1000);
                list.addAll(articleManager.queryArticleList(paras));
                paras.put("pageSize", 10);
            }
            if(req.getLastId() != null && req.getLastId()<=0){
                paras.remove("lastId");
            }
            paras.put("groupId", req.getGroupId());
            if (req.getGroupId() == null) {
                paras.put("groupId", 0);//公开群组id
            }
            paras.put("topLevel", 0);//查询普通消息
            if (userPartyId != null && paras.get("groupId").toString().equals("0")) {
                //查当前用户的试点村
                Map<String, Object> param = new HashMap<>();
                param.put("partyId", userPartyId);
                param.put("groupType", Constant.GroupType.V.name());
                List<AppGroupMember> list1 = groupManager.queryAppMemberByParas(param);
                Long groupId2 = 0L;
                if (list1.size() > 0) {
                    groupId2 = list1.get(0).getGroupId();
                }
                if (groupId2 > 0) {
                    String groupIds = groupId2 + "," + paras.get("groupId").toString();
                    paras.put("groupIds", groupIds);
                    paras.remove("groupId");
                }
            }
            list.addAll(articleManager.queryArticleList(paras));
            Long groupId = 0L;

            User user = userManager.queryUser(userPartyId);
            //学习专区用户预览
            String queryKey = req.getQueryKey();
            //不是top端查询则过滤，top端查询不过滤
            if (!"${XL_TOP_LEARNING_LIST}".equals(queryKey)) {
                list = list.<AppArticle>stream()
                        .map(input -> {
                            if (Constant.ArticleType.LEARNING_PPT.name().equals(input.getArticleType())) {
                                logger.info("===========进行学习资源的用户预览过滤，微博id：[[ {} ]]===========", input.getId());
                                if (!Strings.isNullOrEmpty(input.getSupportUsers())) {
                                    List<String> phones = Splitter.on(",")
                                            .omitEmptyStrings()
                                            .splitToList(input.getSupportUsers());
                                    if (user != null && phones.contains(user.getLoginName())) {
                                        return input;
                                    } else {
                                        return null;
                                    }
                                } else {
                                    return input;
                                }
                            }
                            return input;
                        }).filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }

            if (req.getGroupId() != null && req.getGroupId() > 0) {
                groupId = req.getGroupId();
            }
            List<ArticleVo> result = convertPojo(list, userPartyId, groupId);
            resp.setResult(result);
            resp.setCode(1000);
        } catch (Exception e) {
            logger.warn("queryArticleList error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryArticleTipListV1", description = "提醒列表")
    public Response<List<ArticleTipV2>> queryArticleTipListV1(ArticleTipReq req) {

        Response<List<ArticleTipV2>> resp = ResponseUtils.successResponse();
        Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        Map<String, Object> paras;
        try {
            paras = DTOUtils.beanToMap(req);
            paras.put("toPartyId", userPartyId);
            paras.put("tipStatus", "Y");
            paras.put("exceptPartyId", userPartyId);
            // 根据被评论人和消息类型查列表 exceptPartyId排除对自己的消息提醒,排除已删除的原微博
            List<AppArticleTip> list = articleManager.queryUpdateArticleTip(paras);
            List<ArticleTipV2> articleTipVolist2 = new ArrayList<ArticleTipV2>();
            for (AppArticleTip appArticleTip : list) {
                if (appArticleTip.getArticleId() != null) {
                    AppArticle queryArticle = articleManager.queryArticle(appArticleTip.getArticleId());
                    if (queryArticle != null && appArticleTip.getUser() != null) {
                        // 查询评论提醒
                        if (appArticleTip.getTipType().equals(Constant.ArticleTipType.COMMENT.name())) {
                            ArticleTipV2 articleTipV2 = DTOUtils.map(appArticleTip, ArticleTipV2.class);
                            String name = getUserName(articleTipV2.getPartyId());
                            String userImg = getUserImg(articleTipV2.getPartyId());
                            articleTipV2.setUserNickName(name);
                            //articleTipV2.setShowName(appArticleTip.getUser().getShowName());
                            if (appArticleTip.getUser() != null) {
                                articleTipV2.setShowName(getShowName(appArticleTip.getUser()));
                            } else {
                                articleTipV2.setShowName("xl" + articleTipV2.getPartyId());
                            }
                            if (StringUtils.isNotEmpty(appArticleTip.getUser().getTrueName())) {
                                articleTipV2.setIsAuth(true);
                            }
                            articleTipV2.setUserHeadImg(userImg);
                            articleTipV2.setDateTime(indecateTime(appArticleTip.getCreateTime()));
                            //articleTipV2.setComments(queryArticle.getArticle());
                            articleTipV2.setArticleAudio(queryArticle.getArticleAudio());
                            articleTipV2.setArticleAudioLength(queryArticle.getArticleAudioLength());
                            AppArticle queryArticle1 = articleManager.queryArticle(queryArticle.getArticleId());
                            if (queryArticle1 != null && queryArticle1.getIsDeleted().equals("N")) {
                                ArticleVo articleVo = DTOUtils.map(queryArticle1, ArticleVo.class);
                                articleTipV2.setArticleVo(articleVo);
                                articleTipVolist2.add(articleTipV2);
                            }
                        }
                        // 查询点赞提醒
                        if (appArticleTip.getTipType().equals(Constant.ArticleTipType.PRAISE.name())) {
                            if (queryArticle != null) {
                                ArticleTipV2 articleTipV2 = DTOUtils.map(appArticleTip, ArticleTipV2.class);
                                if (articleTipV2.getArticleId() != null) {
                                    articleTipV2.setArticleId(queryArticle.getArticleId());
                                } else {
                                    articleTipV2.setArticleId(queryArticle.getId());
                                }
                                String name = getUserName(articleTipV2.getPartyId());
                                String userImg = getUserImg(articleTipV2.getPartyId());
                                articleTipV2.setUserNickName(name);
                                if (appArticleTip.getUser() != null) {
                                    articleTipV2.setShowName(getShowName(appArticleTip.getUser()));
                                } else {
                                    articleTipV2.setShowName("xl" + articleTipV2.getPartyId());
                                }
                                if (StringUtils.isNotEmpty(appArticleTip.getUser().getTrueName())) {
                                    articleTipV2.setIsAuth(true);
                                }
                                articleTipV2.setUserHeadImg(userImg);
                                articleTipV2.setDateTime(indecateTime(appArticleTip.getCreateTime()));
                                ArticleVo articleVo = DTOUtils.map(queryArticle, ArticleVo.class);
                                articleTipV2.setArticleVo(articleVo);
                                String type = null;
                                if (articleVo.getArticleType().equals(Constant.ArticleType.BROADCAST.name())) {
                                    type = "赞了你发表的广播";
                                } else if (articleVo.getArticleType().equals(Constant.ArticleType.FINANCE.name())) {
                                    type = "赞了你发表的财务公开";
                                } else if (articleVo.getArticleType().equals(Constant.ArticleType.GOVERNMENT.name())) {
                                    type = "赞了你发表的政务公开";
                                } else if (articleVo.getArticleType().equals(Constant.ArticleType.SUBJECT.name())) {
                                    type = "赞了你发表的微博";
                                } else if (articleVo.getArticleType().equals(Constant.ArticleType.COMMENT.name()) || articleVo.getArticleType().equals(Constant.ArticleType.SUBCOMMENT.name())) {
                                    type = "赞了你的评论";
                                } else if (articleVo.getArticleType().equals(Constant.ArticleType.RECRUITMENT.name())) {
                                    type = "赞了你发表的招工";
                                } else if (articleVo.getArticleType().equals(Constant.ArticleType.SHORT_VIDEO.name())) {
                                    type = "赞了你发表的短视频";
                                }
                                articleTipV2.setContent(type);
                                if (org.apache.commons.lang.StringUtils.isNotEmpty(articleTipV2.getComments())) {
                                    articleTipV2.setComments("");
                                }
                                //查原微博的类型
                                AppArticle queryArticle1 = articleManager.queryArticle(queryArticle.getArticleId());
                                if (queryArticle1 != null) {
                                    articleTipV2.setComments(queryArticle1.getArticleType());
                                }
                                articleTipVolist2.add(articleTipV2);
                            }
                        }
                        //查询分享提醒
                        if (appArticleTip.getTipType().equals(Constant.ArticleTipType.SHARE.name())) {
                            ArticleTipV2 articleTipV2 = DTOUtils.map(appArticleTip, ArticleTipV2.class);
                            // 加字段了无法使用工具方法
                            String name = getUserName(articleTipV2.getPartyId());
                            String userImg = getUserImg(articleTipV2.getPartyId());
                            articleTipV2.setUserNickName(name);
                            if (appArticleTip.getUser() != null) {
                                articleTipV2.setShowName(getShowName(appArticleTip.getUser()));
                            } else {
                                articleTipV2.setShowName("xl" + articleTipV2.getPartyId());
                            }
                            if (appArticleTip.getUser() != null && StringUtils.isNotEmpty(appArticleTip.getUser().getTrueName())) {
                                articleTipV2.setIsAuth(true);
                            }
                            articleTipV2.setUserHeadImg(userImg);
                            articleTipV2.setArticleId(appArticleTip.getArticleId());
                            articleTipV2.setDateTime(indecateTime(appArticleTip.getCreateTime()));
                            ArticleVo articleVo = DTOUtils.map(queryArticle, ArticleVo.class);
                            articleTipV2.setArticleVo(articleVo);
                            String type = null;
                            if (articleVo.getArticleType().equals(Constant.ArticleType.BROADCAST.name())) {
                                type = "分享了你发表的广播";
                            } else if (articleVo.getArticleType().equals(Constant.ArticleType.FINANCE.name())) {
                                type = "分享了你发表的财务公开";
                            } else if (articleVo.getArticleType().equals(Constant.ArticleType.GOVERNMENT.name())) {
                                type = "分享了你发表的政务公开";
                            } else if (articleVo.getArticleType().equals(Constant.ArticleType.SUBJECT.name())) {
                                type = "分享了你发表的微博";
                            } else if (articleVo.getArticleType().equals(Constant.ArticleType.COMMENT.name())) {
                                type = "分享了你的评论";
                            } else if (articleVo.getArticleType().equals(Constant.ArticleType.RECRUITMENT.name())) {
                                type = "分享了你发表的招工";
                            } else if (articleVo.getArticleType().equals(Constant.ArticleType.SHORT_VIDEO.name())) {
                                type = "分享了你发表的短视频";
                            }
                            articleTipV2.setContent(type);
                            articleTipVolist2.add(articleTipV2);
                        }
                        /*//查询收藏提醒
                        if(appArticleTip.getTipType().equals(Constant.ArticleTipType.COLLET.name())){
                            ArticleTipV2 articleTipV2 = DTOUtils.map(appArticleTip, ArticleTipV2.class);
                            // 加字段了无法使用工具方法
                            String name = getUserName(articleTipV2.getPartyId());
                            String userImg = getUserImg(articleTipV2.getPartyId());
                            articleTipV2.setUserNickName(name);
                            articleTipV2.setUserHeadImg(userImg);
                            articleTipV2.setArticleId(appArticleTip.getArticleId());
                            articleTipV2.setDateTime(indecateTime(appArticleTip.getCreateTime()));
                            ArticleVo articleVo =  DTOUtils.map(queryArticle, ArticleVo.class);
                            articleTipV2.setArticleVo(articleVo);
                            String type = null;
                            if(articleVo.getArticleType().equals(Constant.ArticleType.BROADCAST.name())){
                                type ="收藏了你发表的广播";
                            }else if(articleVo.getArticleType().equals(Constant.ArticleType.FINANCE.name())){
                                type ="收藏了你发表的财务公开";
                            }else if(articleVo.getArticleType().equals(Constant.ArticleType.GOVERNMENT.name())){
                                type ="收藏了你发表的政务公开";
                            }else if(articleVo.getArticleType().equals(Constant.ArticleType.SUBJECT.name())){
                                type ="收藏了你发表的财务微博";
                            }
                            articleTipV2.setContent(type);
                            articleTipVolist2.add(articleTipV2);
                        }*/
                        //查询举报提醒
                        /*if(appArticleTip.getTipType().equals(Constant.ArticleTipType.REPORT.name())){
                            ArticleTipV2 articleTipV2 = DTOUtils.map(appArticleTip, ArticleTipV2.class);
                            // 加字段了无法使用工具方法
                            String name = getUserName(appArticleTip.getPartyId());
                            String userImg = getUserImg(appArticleTip.getPartyId());
                            articleTipV2.setUserNickName(name);
                            articleTipV2.setUserHeadImg(userImg);
                            articleTipV2.setArticleId(appArticleTip.getArticleId());
                            articleTipV2.setDateTime(indecateTime(appArticleTip.getCreateTime()));
                            articleTipV2.setContent("举报了你发表的微博");
                            ArticleVo articleVo =  DTOUtils.map(queryArticle, ArticleVo.class);
                            articleTipV2.setArticleVo(articleVo);
                            articleTipVolist2.add(articleTipV2);
                        }*/
                        //查询关注提醒
                    }
                } else if (appArticleTip.getTipType().equals(Constant.ArticleTipType.FOLLOW.name()) || appArticleTip.getTipType().equals(Constant.ArticleTipType.ACT.name())) {
                    ArticleTipV2 articleTipV2 = DTOUtils.map(appArticleTip, ArticleTipV2.class);
                    // 加字段了无法使用工具方法
                    String name = getUserName(appArticleTip.getPartyId());
                    String userImg = getUserImg(appArticleTip.getPartyId());
                    articleTipV2.setUserNickName(name);
                    if (appArticleTip.getUser() != null) {
                        articleTipV2.setShowName(getShowName(appArticleTip.getUser()));
                    } else {
                        articleTipV2.setShowName("xl" + articleTipV2.getPartyId());
                    }
                    if (appArticleTip.getUser() != null && StringUtils.isNotEmpty(appArticleTip.getUser().getTrueName())) {
                        articleTipV2.setIsAuth(true);
                    }
                    articleTipV2.setUserHeadImg(userImg);
                    articleTipV2.setDateTime(indecateTime(appArticleTip.getCreateTime()));
                    articleTipV2.setContent(appArticleTip.getContent());
                    articleTipVolist2.add(articleTipV2);
                }
            }
            resp.setResult(articleTipVolist2);
        } catch (Exception e) {
            logger.warn("queryArticleTipListV1 error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    private String getShowName(User user) {

        String userName = null;
        if (user.getPartyId() != null) {
            userName = "xl" + user.getPartyId();
        }
        if (StringUtils.isNotEmpty(user.getTrueName())) {
            userName = user.getTrueName();
        }
        if (StringUtils.isNotEmpty(user.getNikerName())) {
            userName = user.getNikerName();
        }
        return userName;
    }

    private String getUserName(Long partyId) {

        String userName = null;
        User user = userManager.queryUser(partyId);
        if (user != null) {
            if (StringUtils.isNotEmpty(user.getShowName())) {
                userName = user.getShowName();
            } else {
                userName = user.showName();
                /*if (user.getPartyId() != null) {
                    userName = "xl" + user.getPartyId();
                }
                if (StringUtils.isNotEmpty(user.getTrueName())) {
                    userName = user.getTrueName();
                }
                if (StringUtils.isNotEmpty(user.getNikerName())) {
                    userName = user.getNikerName();
                }*/
            }
        } else {
            return "xl" + partyId;
        }
        return userName;
    }

    private String getUserImg(Long partyId) {

        String userImg = null;
        User user = userManager.queryUser(partyId);
        if (user != null) {
            if (StringUtils.isNotEmpty(user.getHeadImg())) {
                userImg = user.getHeadImg();
            } else {
                userImg = SysConfigUtil.getStr("default_user_headimg");
            }
        }
        return userImg;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryArticleTipsV1", description = "查询提醒未读数")
    public Response<Integer> queryArticleTipsV1() {

        Response<Integer> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("toPartyId", partyId);
            paras.put("readStatus", "N");
            paras.put("tipStatus", "Y");
            paras.put("exceptPartyId", partyId);
            paras.put("exceptTipTypes", Constant.ArticleTipType.toList(Constant.ArticleTipType.COLLET.name(), Constant.ArticleTipType.REPORT.name()));
            //查询未读分享数
            Integer count = articleManager.queryArticleTipCount(paras);
            resp.setResult(count);
        } catch (Exception e) {
            logger.warn("queryArticleTips error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryUserUsedAddress", description = "查询用户最近发布的3个地址")
    public Response<List<String>> queryUserUsedAddress() {

        Response<List<String>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            //查询用户最新发布的三个地址
            List<String> list = queryUserAddressByPartyId(partyId);
            resp.setResult(list);
        } catch (Exception e) {
            logger.warn("queryUserUsedAddress error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 查询用户最新发布的三个地址
     * @param partyId
     * @return
     */
    private List<String> queryUserAddressByPartyId(Long partyId) {
            Map<String, Object> paras = new HashMap<>();
            paras.put("startPage", 1);
            paras.put("pageSize", 3);
            paras.put("relationId", partyId);
            paras.put("type", User.class.getSimpleName());
            paras.put("ekey", EnumKeyValue.User.USER_USED_ADDRESS.name());
            paras.put("orderBy", "UPDATE_TIME");
            List<AppPropExtend> appPropExtends = propExtendManager.queryChannel(paras);
            List<String> list = appPropExtends.stream().map((vo)->{
                return vo.getValue();
            }).collect(Collectors.toList());
            return list;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.reportUser", description = "举报用户")
    public Response<Boolean> reportUser(Long partyId, String reprotMsg) {

        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            User user = userManager.queryUser(partyId);
            if (user == null) {// 用户不存在
                resp.setFacade(FacadeEnums.E_C_USER_INVALID);
                return resp;
            }
            Long LoginPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (LoginPartyId == null) {// 用户没有登录
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("partyId", LoginPartyId);
            paras.put("toPartyId", partyId);
            paras.put("articleId", partyId);
            paras.put("tipType", Constant.ArticleTipType.REPORT.name());
            List<AppArticleTip> tips = articleManager.queryArticleTipList(paras);
            if (CollectionUtils.isNotEmpty(tips)) {// 已经举报过用户
                resp.setResult(false);
            } else {
                AppArticleTip tip = new AppArticleTip();
                tip.setPartyId(LoginPartyId);
                tip.setTipType(Constant.ArticleTipType.REPORT.name());
                tip.setArticleId(partyId);
                tip.setToPartyId(partyId);
                String userName = getUserName(partyId);
                String longUserName = getUserName(LoginPartyId);
                tip.setContent(longUserName + "举报了" + userName);
                tip.setComments(reprotMsg);
                articleManager.addArticleTip(tip);
                resp.setResult(true);
            }
        } catch (Exception e) {
            logger.warn("report error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /*private String convertAddress(AreaVo area) {
        List<MapVo>  mapVoList = new ArrayList<>();
        if(StringUtils.isNotEmpty(area.getProvince().getName())){
            MapVo mapVo = new MapVo();
            mapVo.setKey("province");
            mapVo.setValue(area.getProvince().getName());
            mapVoList.add(mapVo);
        }
        if(StringUtils.isNotEmpty(area.getCity().getName())){
            MapVo mapVo = new MapVo();
            mapVo.setKey("city");
            mapVo.setValue(area.getCity().getName());
            mapVoList.add(mapVo);
        }
        if(StringUtils.isNotEmpty(area.getCounty().getName())){
            MapVo mapVo = new MapVo();
            mapVo.setKey("county");
            mapVo.setValue(area.getCounty().getName());
            mapVoList.add(mapVo);
        }
        if(StringUtils.isNotEmpty(area.getTown().getName())){
            MapVo mapVo = new MapVo();
            mapVo.setKey("town");
            mapVo.setValue(area.getTown().getName());
            mapVoList.add(mapVo);
        }
        if(StringUtils.isNotEmpty(area.getVillage().getName())){
            MapVo mapVo = new MapVo();
            mapVo.setKey("village");
            mapVo.setValue(area.getVillage().getName());
            mapVoList.add(mapVo);
        }
        String locationJson = JSONArray.fromObject(mapVoList).toString();
        return locationJson;
    }*/

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryRecruitment", description = "查询招工信息")
    public Response<List<ArticleVo>> queryRecruitment(ArticleReq req) {

        Response<List<ArticleVo>> resp = ResponseUtils.successResponse();
        List<ArticleVo> list = new ArrayList<>();
        try {
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("startPage", req.getStartPage());
            paras.put("pageSize", req.getPageSize());
            paras.put("partyId", req.getPartyId());
            paras.put("articleType", req.getArticleType());
            if (StringUtils.isNotEmpty(req.getCity())) {
                paras.put("city", req.getCity());
            }
            if (StringUtils.isNotEmpty(req.getProvince())) {
                paras.put("province", req.getProvince());
            }
            if (StringUtils.isNotEmpty(req.getTown())) {
                paras.put("town", req.getTown());
            }
            if (StringUtils.isNotEmpty(req.getVillage())) {
                paras.put("village", req.getVillage());
            }
            if (StringUtils.isNotEmpty(req.getCounty())) {
                paras.put("county", req.getCounty());
            }
            paras.put("groupId", req.getGroupId());
            if (req.getGroupId() == null) {
                paras.put("groupId", 0);//公开群组id
            }
            List<AppArticle> appArticleList = articleManager.queryArticleList(paras);
            if (appArticleList.size() > 0) {
                for (AppArticle appArticle : appArticleList) {
                    ArticleVo articleVo = new ArticleVo();
                    articleVo.setId(appArticle.getId());
                    if (StringUtils.isNotEmpty(appArticle.getTitle())) {
                        articleVo.setTitle(appArticle.getTitle());
                    }
                    if (StringUtils.isNotEmpty(appArticle.getContacts())) {
                        articleVo.setContacts(appArticle.getContacts());
                    }
                    if (StringUtils.isNotEmpty(appArticle.getContactsPhone())) {
                        articleVo.setContactsPhone(appArticle.getContactsPhone());
                    }
                    if (StringUtils.isNotEmpty(appArticle.getArticle())) {
                        articleVo.setArticle(appArticle.getArticle());
                    }
                    if (StringUtils.isNotEmpty(appArticle.getArticleImgs())) {
                        articleVo.setArticleImgs(appArticle.getArticleImgs());
                    }
                    articleVo.setDateTime(DateUtils.formatDateTime("yyyy-MM-dd", appArticle.getCreateTime()));
                    String district = null;
                    if (StringUtils.isNotEmpty(appArticle.getProvince())) {
                        district = appArticle.getProvince();
                    }
                    if (StringUtils.isNotEmpty(appArticle.getCity())) {
                        district = district + appArticle.getCity();
                    }
                    if (StringUtils.isNotEmpty(appArticle.getCounty())) {
                        district = district + appArticle.getCounty();
                    }
                    if (StringUtils.isNotEmpty(appArticle.getTown())) {
                        district = district + appArticle.getTown();
                    }
                    if (StringUtils.isNotEmpty(appArticle.getVillage())) {
                        district = district + appArticle.getVillage();
                    }
                    articleVo.setDistrict(district);
                    articleVo.setPartyId(appArticle.getPartyId());
                    list.add(articleVo);
                }
            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.warn("queryRecruitment error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }

        return resp;
    }

    @Override
    public Response<LearningPPtVO> queryLearningFile(Long id) {

        LearningPPtVO result = null;
        try {
            checkArgument(id != null, "参数错误，id不能为空");
            AppArticle appArticle = articleManager.queryArticle(id);
            if (appArticle == null) {
                return Response.ofFail("参数错误，记录不存在");
            }
            result = DTOUtils.map(appArticle, LearningPPtVO.class);
            String pptFileUrl = null;
            String pptFileName = null;
            String coverImageName = null;

            HashMap<String, Object> paramsMap = Maps.newHashMap();
            paramsMap.put("relationId", appArticle.getId());
            paramsMap.put("type", AppArticle.class.getSimpleName());
            paramsMap.put("ekey", "pptFileName");
            List<AppPropExtend> pptFileNames = propExtendManager.queryChannel(paramsMap);
            if (!pptFileNames.isEmpty()) {
                pptFileName = pptFileNames.get(0).getValue();
            }
            paramsMap.put("ekey", "pptFileUrl");
            List<AppPropExtend> pptFileUrls = propExtendManager.queryChannel(paramsMap);
            if (!pptFileUrls.isEmpty()) {
                pptFileUrl = pptFileUrls.get(0).getValue();
            }
            paramsMap.put("ekey", "coverImageName");
            List<AppPropExtend> coverImageNames = propExtendManager.queryChannel(paramsMap);
            if (!coverImageNames.isEmpty()) {
                coverImageName = coverImageNames.get(0).getValue();
            }

            result.setPptFileName(pptFileName);
            result.setPptFileUrl(pptFileUrl);
            result.setImageName(coverImageName);
        } catch (IllegalArgumentException e) {
            logger.info("===========top 学习专区 查询学习资料参数错误，id ：[[ {} ]]===========", id);
            return Response.ofFail(e.getMessage());
        } catch (Exception e) {
            logger.warn("===========top 学习专区 查询学习资料详情错误，id ：[[ {} ]]===========", id, e);
            return Response.ofFail();
        }

        return Response.ofSuccess(result);
    }

    @Override
    public Response<PageResult<ArticleVo>> queryLearningFileList(ArticleReq queryParam) {

        PageResult<ArticleVo> result = new PageResult<>();
        try {
            checkArgument(queryParam != null, "参数错误，参数不能为空");
            int startPage = queryParam.getStartPage();
            int pageSize = queryParam.getPageSize();
            int count = articleManager.countLearningFile();
            List<ArticleVo> resultList = Lists.newArrayList();
            if (count != 0) {
                List<AppArticle> appArticles = articleManager.queryLearningFileList(startPage, pageSize);
                for (AppArticle appArticle : appArticles) {
                    resultList.add(DTOUtils.map(appArticle, ArticleVo.class));
                }
            }
            result.setTotalSize(count);
            result.setData(resultList);
        } catch (IllegalArgumentException e) {
            logger.info("===========top 学习专区 查询学习资料列表参数错误，id ：[[ {} ]]===========", queryParam);
            return Response.ofFail(e.getMessage());
        } catch (Exception e) {
            logger.warn("===========top 学习专区 查询学习资料列表错误，id ：[[ {} ]]===========", queryParam, e);
            return Response.ofFail();
        }
        return Response.ofSuccess(result);
    }

    @Override
    public Response<Boolean> updateLearningFile(LearningPPtVO inputVO) {

        try {
            checkArgument(inputVO != null, "参数错误，参数不能为空");
            AppArticle appArticle = DTOUtils.map(inputVO, AppArticle.class);
            Date now = new Date();
            appArticle.setUpdateTime(now);
            AppPropExtend pptFileName = AppPropExtend.builder()
                    .relationId(appArticle.getId())
                    .type(AppArticle.class.getSimpleName())
                    .ekey("pptFileName")
                    .value(inputVO.getPptFileName())
                    .build();
            AppPropExtend pptFileUrl = AppPropExtend.builder()
                    .relationId(appArticle.getId())
                    .type(AppArticle.class.getSimpleName())
                    .ekey("pptFileUrl")
                    .value(inputVO.getPptFileUrl())
                    .build();
            AppPropExtend coverImageName = AppPropExtend.builder()
                    .relationId(appArticle.getId())
                    .type(AppArticle.class.getSimpleName())
                    .ekey("coverImageName")
                    .value(inputVO.getImageName())
                    .build();
            if (inputVO.getId() == null) {
                appArticle.setCreateTime(now);
                appArticle.setArticleType(Constant.ArticleType.LEARNING_PPT.name());
                appArticle.setReadCount(0);
                Boolean articleFlag = articleManager.addArticle(appArticle);
                if (articleFlag) {
                    pptFileName.setRelationId(appArticle.getId());
                    pptFileUrl.setRelationId(appArticle.getId());
                    coverImageName.setRelationId(appArticle.getId());
                    propExtendManager.insert(pptFileName);
                    propExtendManager.insert(pptFileUrl);
                    propExtendManager.insert(coverImageName);
                }
            } else {
                articleManager.updateArticle(appArticle);
                HashMap<String, String> params = Maps.newHashMap();
                params.put("relationId", appArticle.getId().toString());
                params.put("type", AppArticle.class.getSimpleName());
                params.put("IS_DELETE", "N");
                params.put("ekey", "pptFileName");
                propExtendManager.updateByParam(params, pptFileName);
                params.put("ekey", "pptFileUrl");
                propExtendManager.updateByParam(params, pptFileUrl);
                params.put("ekey", "coverImageName");
                propExtendManager.updateByParam(params, coverImageName);
            }
        } catch (IllegalArgumentException e) {
            logger.info("===========top 学习专区 查询学习资料参数错误:[[ {} ]]===========", "参数对象为空");
            return Response.ofFail(e.getMessage());
        } catch (Exception e) {
            logger.warn("=========== top 学习专区 更新ppt学习资料错误，参数 ：[[ {} ]]===========", JSON.toJSONString(inputVO, true), e);
            return Response.ofFail("服务器异常");
        }

        return Response.ofSuccess(true);
    }

    @Override
    public Response<Boolean> deleteLearningFile(Long id) {

        Boolean flag = false;
        try {
            checkArgument(id != null, "参数错误，id不能为空");
            AppArticle appArticle = AppArticle.builder()
                    .id(id)
                    .isDeleted(Constant.YESNO.YES.code)
                    .articleType(Constant.ArticleType.LEARNING_PPT.name())
                    .updateTime(new Date())
                    .build();
            AppPropExtend build = AppPropExtend.builder()
                    .isDeleted(Constant.YESNO.YES.code)
                    .build();
            HashMap<String, String> updateParam = Maps.newHashMap();
            updateParam.put("relationId", id.toString());
            updateParam.put("type", AppArticle.class.getSimpleName());
            flag = articleManager.updateArticle(appArticle) && propExtendManager.updateByParam(updateParam, build);
        } catch (IllegalArgumentException e) {
            logger.info("===========top 学习专区 删除学习资料参数错误:[[ {} ]]===========", "参数对象为空");
            return Response.ofFail(e.getMessage());
        } catch (Exception e) {
            logger.warn("=========== top 学习专区 删除ppt学习资料异常，参数 ：[[ {} ]]===========", id, e);
            return Response.ofFail("服务器异常");
        }
        return Response.ofSuccess(flag);
    }

    /**
     * 根据类型查询微博数
     *
     * @param type
     * @return
     */
    @Override
    public Response<Integer> queryArticleCountByType(String type) {

        Response<Integer> response = ResponseUtils.successResponse(0);
        try {
            if (StringUtils.isEmpty(type)) {
                response.setFacade(FacadeEnums.E_C_INPUT_INVALID);
                return response;
            }
            Map<String, Object> param = new HashMap<>();
            param.put("articleType", type);
            Integer integer = articleManager.queryArticleCount(param);
            response.setResult(integer);
        } catch (Exception e) {
            logger.warn("queryArticleCountByType error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }


    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryArticleListByIds", description = "根据id查微博")
    public Response<List<ArticleVo>> queryArticleListByIds(Long[] ids) {
        Response<List<ArticleVo>> response = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (ids.length <= 0) {
                response.setFacade(FacadeEnums.E_C_INPUT_INVALID);
                return response;
            }
            Map<String, Object> param = new HashMap<>();
            param.put("ids", ids);
            List<AppArticle> appArticles = articleManager.queryArticleList(param);
            final List<ArticleVo> result = convertPojo(appArticles, partyId, 0L);
            //将置顶微博设置为空
//            result = result.stream().map(vo -> {
//                vo.setTopLevel(null);
//                return vo;
//            }).collect(Collectors.toList());
            List<ArticleVo> resultList = new ArrayList<>();
            for (Long id : ids) {
                resultList.add(result.stream().filter((vo) ->
                        vo.getId().equals(id)
                ).findFirst().map((vo) -> {
                    vo.setTopLevel(null);
                    return vo;
                }).orElse(null));
            }
//
//            List<ArticleVo> resultList = Arrays.stream(ids).map((id) -> {
//                return result.stream().filter((vo) ->
//                    vo.getId().equals(id)
//                ).findFirst().map((vo) -> {
//                    vo.setTopLevel(null);
//                    return vo;
//                }).orElse(null);
//            }).collect(Collectors.toList());
            response.setResult(resultList);
        } catch (Exception e) {
            logger.warn("queryArticleByIds error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryArticleContByParam", description = "根据参数查动态数")
    public Response<Integer> queryArticleContByParam(ArticleReq req) {
        Response<Integer> response = ResponseUtils.successResponse(0);
        try {
            Map<String, Object> paras = DTOUtils.beanToMap(req);
            if (req.getIsExcludeShareUrl()) {
                paras.put("excludeShareUrl", "paras");
                paras.put("groupId", 0);
            }
            paras.put("articleType", Constant.ArticleType.SUBJECT.name());
            if (req.getStartDate() != null && !(req.getStartDate().isEmpty())) {
                String date = req.getStartDate() + " 00:00:00";
                Date startDate = DateUtils.formatStr(date, "yyyy-MM-dd HH:mm:ss");
                paras.put("startDate", startDate);
            } else {
                paras.remove("startDate");
            }
            if (req.getEndDate() != null && !(req.getEndDate().isEmpty())) {
                String date = req.getEndDate() + " 23:59:59";
                Date endDate = DateUtils.formatStr(date, "yyyy-MM-dd HH:mm:ss");
                paras.put("endDate", endDate);
            } else {
                paras.remove("endDate");
            }
            if (req.getNickName() == null || req.getNickName().isEmpty()) {
                paras.remove("nickName");
            }
            if (req.getQueryKey() == null || req.getQueryKey().isEmpty()) {
                paras.remove("queryKey");
            }
            //AppArticle appArticle = DTOUtils.map(req, AppArticle.class);
            int count = articleManager.queryArticleCount(paras);
            response.setResult(count);
        } catch (Exception e) {
            logger.warn("queryArticleContByParam error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    /**
     * 发布提醒
     *
     * @param articleTipVo
     * @return
     */
    @Override
    public Response<Boolean> publishArticleTip(ArticleTipVo articleTipVo) {
        Response<Boolean> response = ResponseUtils.successResponse();
        try {
            AppArticleTip appArticleTip = DTOUtils.map(articleTipVo, AppArticleTip.class);
            Boolean flag = articleManager.addArticleTip(appArticleTip);
            response.setResult(flag);
        } catch (Exception e) {
            logger.warn("publishArticleTip error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryFollowArticle", description = "查关注微博")
    public Response<List<ArticleVo>> queryFollowArticle(ArticleReq req) {
        Response<List<ArticleVo>> response = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (StringUtils.isEmpty(req.getFollowType())) {
                response.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_INPUT_INVALID);
                return response;
            }
            Map<String, Object> paras = DTOUtils.queryMap();
            if (StringUtils.equals(req.getFollowType(), Constant.FollowType.FOLLOW.name())) {
                //查用户的关注用户
                List<Long> partyIds = queryFollowUser(partyId);
                if (partyIds.size() < 1) {
                    return response;
                }
                paras.put("partyIds", partyIds);
            }
            User user = null;
            if (StringUtils.equals(req.getFollowType(), Constant.FollowType.CITY.name()) || StringUtils.equals(req.getFollowType(), Constant.FollowType.COUNTY.name()) || StringUtils.equals(req.getFollowType(), Constant.FollowType.TOWN.name())) {
                //查用户的家乡地址
                user = userManager.queryUser(partyId);
                if (user == null) {
                    response.setFacade(FacadeEnums.ERROR_CHAT_400047);
                    return response;
                }
            }
            //根据用户所在的市查微博，判断用户是否设置了家乡地址
            if (StringUtils.equals(req.getFollowType(), Constant.FollowType.CITY.name())) {
                if (StringUtils.isEmpty(user.getCity())) {
                    response.setFacade(FacadeEnums.ERROR_CHAT_400069);
                    return response;
                }
                paras.put("province", user.getProvince());
                paras.put("city", user.getCity());
            }
            //根据用户所在的县查微博，判断用户是否设置了家乡地址
            if (StringUtils.equals(req.getFollowType(), Constant.FollowType.COUNTY.name())) {
                if (StringUtils.isEmpty(user.getCounty())) {
                    response.setFacade(FacadeEnums.ERROR_CHAT_400069);
                    return response;
                }
                paras.put("province", user.getProvince());
                paras.put("city", user.getCity());
                paras.put("county", user.getCounty());
            }
            //根据用户所在的镇查微博，
            if (StringUtils.equals(req.getFollowType(), Constant.FollowType.TOWN.name())) {
                if (StringUtils.isEmpty(user.getTown())) {
                    response.setFacade(FacadeEnums.ERROR_CHAT_400069);
                    return response;
                }
                paras.put("province", user.getProvince());
                paras.put("city", user.getCity());
                paras.put("county", user.getCounty());
                paras.put("town", user.getTown());
            }
            paras.put("startPage", req.getStartPage());
            paras.put("pageSize", req.getPageSize());
            paras.put("lastId", req.getLastId());
            paras.put("groupId", 0);
            paras.put("articleType", Constant.ArticleType.SUBJECT.name());
            paras.put("orderBy", "CREATE_TIME DESC");
            List<AppArticle> appArticles = articleManager.queryArticleList(paras);
            List<ArticleVo> result = convertPojo(appArticles, partyId, 0L);
            //将置顶微博设置为空
            result = result.stream().map(vo -> {
                vo.setTopLevel(null);
                return vo;
            }).collect(Collectors.toList());
            response.setResult(result);
        } catch (Exception e) {
            logger.warn("queryFollowArticle error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    /**
     * 查用户的关注用户的partyID
     *
     * @param partyId
     * @return
     */
    private List<Long> queryFollowUser(Long partyId) {
        return relationCoreService.queryRelationList(partyId, Constant.RelationStatus.FOLLOW.code, new Page(1, Short.MAX_VALUE)).stream().map(v -> v.getToPartyId()).collect(Collectors.toList());
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryRecommendArticle", description = "感兴趣微博推荐")
    public Response<List<ArticleVo>> queryRecommendArticle() {
        Response<List<ArticleVo>> response = ResponseUtils.successResponse();
        try {
            //查当前登录用户
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                response.setFacade(FacadeEnums.SESSION_INVALD);
                return response;
            }
            //查用户的家乡地址
            User user = userManager.queryUser(partyId);
            //排除的partyID
            List<Long> partyIds = new ArrayList<>();
            List<AppArticle> list = new LinkedList<>();
            //1、用户设置过家乡：粉丝数大于20的用户在一周内发布的微博随机取6条+全市最新发布的2条微博。一个用户最多取一条。。随机排序，剔除自己发布微博
            //2、用户未设置过家乡：粉丝数大于20的用户在一周内发布的微博随机取8条。一个用户最多取一条。随机排序，剔除自己发布微博
            //3、满足条件微博数量不够：有多少显示多少
            Map<String, Object> param = new HashMap<>();
            param.put("excludePartyId", partyId);
            param.put("startPage", 1);
            param.put("pageSize", 6);
            param.put("followCount", 20);
            param.put("rand", "rand");
            list.addAll(articleManager.queryRandArticle(param)); //粉丝数大于20的用户在一周内发布的微博随机取6条
            partyIds = list.stream().map(vo -> {
                return vo.getPartyId();
            }).collect(Collectors.toList());
            if (partyIds.size() > 0) {
                param.put("partyIds", partyIds);
            }
            param.remove("followCount");
            param.remove("rand");
            if (user != null && StringUtils.isNotEmpty(user.getCity())) {
                param.put("province", user.getProvince());
                param.put("city", user.getCity());
            }
            param.put("pageSize", 2);
            List<AppArticle> appArticles = articleManager.queryRandArticle(param);
            list.addAll(appArticles); //全市/或全栈最新发布的2条微博
            List<ArticleVo> result = convertPojo(list, partyId, 0L);
            Collections.shuffle(result);
            logger.info("queryRecommendArticle totalSize3:" + result.size());
            //将置顶微博设置为空
            result = result.stream().map(vo -> {
                vo.setTopLevel(null);
                return vo;
            }).collect(Collectors.toList());
            response.setResult(result);
        } catch (Exception e) {
            logger.warn("queryRecommendArticle error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    @Override
    public Response<List<ArticleVo>> queryArticleListV4(ArticleReq req) {
        Response<List<ArticleVo>> resp = ResponseUtils.successResponse();
        try {
            String userType = sessionHelper.getSessionProp(SessionConstants.USER_TYPE, String.class);
            if (StringUtils.isEmpty(userType)) {
                userType = Constant.UserType.visitor.name();
            }
            Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("startPage", req.getStartPage());
            paras.put("pageSize", req.getPageSize());
            paras.put("partyId", req.getPartyId());
            paras.put("queryKey", req.getQueryKey());
            paras.put("articleType", req.getArticleType());
            paras.put("lastId", req.getLastId());
            paras.put("groupId", req.getGroupId());
            paras.put("orderBy", req.getOrderBy());
            List<AppArticle> list = articleManager.queryArticleList(paras);
            Long groupId = 0L;
            if (req.getGroupId() != null && req.getGroupId() > 0) {
                groupId = req.getGroupId();
            }
            List<ArticleVo> result = convertPojo(list, userPartyId, groupId);
            resp.setResult(result);
        } catch (Exception e) {
            logger.warn("queryArticleList error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryPopularArticles", description = "查询热门微博")
    public Response<List<Long>> queryPopularArticles(String type) {
        return responseCacheUtils.executeCacheResponse(ResponseCacheUtils.ResponseCacheKey.ARTICLE_POPULAR, type, () -> {
            ArticlePopularType articleType = ArticlePopularType.getType(type);
            doArticleRecord(Constant.SearchType.SCREEN.name(), articleType.getDesc());
            return articleManager.queryPopularArticles(articleType);
        });
    }

    @Override
    public Response<String> articleAudioChangeMp3(String articleAudio) {
        Response<String> response = ResponseUtils.successResponse();
        try {
            if (StringUtils.isEmpty(articleAudio)) {
                response.setFacade(com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.E_C_INPUT_INVALID);
                return response;
            }
            String str = SysConfigUtil.getStr("APP_FILESERVER_URL");
            articleAudio = HttpUtils.executePost2(str + "convert/amr2mp3?url=" + articleAudio, "", 10000);
            logger.info("articleAudioChangeMp3:{}", articleAudio);
            response.setResult(articleAudio);
        } catch (Exception e) {
            logger.warn("articleAudioChangeMp3 error:", e);
        }
        return response;
    }

    /**
     * 查询top端视频列表
     *
     * @param articleReq
     * @return
     */
    @Override
    public Response<PageResult<ArticleVo>> queryShortVideo(ArticleReq articleReq) {
        Response<PageResult<ArticleVo>> response = ResponseUtils.successResponse();
        PageResult<ArticleVo> articleVoPageResult = new PageResult<>();
        try {
            Map<String, Object> param = DTOUtils.beanToMap(articleReq);
           // Map<String, Object> param = DTOUtils.map(articleReq, Map.class);
            if (articleReq != null && StringUtils.isEmpty(articleReq.getOrderBy())) {
                param.put("orderBy", "TOP_TIME DESC,CREATE_TIME DESC");
            }
            param.put("articleType", Constant.ArticleType.SHORT_VIDEO.name());
            List<AppArticle> appArticles = articleManager.queryArticleList(param);
            List<ArticleVo> articleVos = appArticles.stream().map(vo -> {
                ArticleVo articleVo = null;
                try {
                    articleVo = DTOUtils.map(vo, ArticleVo.class);
                    //查询用户的手机号
                    User user=userManager.queryUser(articleVo.getPartyId());
                    if(user != null){
                        articleVo.setPhone(user.getLoginName());
                        if(StringUtils.isNotEmpty(user.getShowName())){
                            articleVo.setShowName(user.getShowName());
                        }else{
                            articleVo.setShowName(user.showName());
                        }
                    }
                    
                    if(StringUtils.isNotEmpty(articleVo.getArticle())){
                        articleVo.setTopic(Arrays.stream(StringUtils.substringsBetween(articleVo.getArticle(), Constant.ArticleContent.TOPIC.start, Constant.ArticleContent.TOPIC.end)).distinct().collect(Collectors.joining(",")));   
                    }
                } catch (Exception e) {
                    logger.warn("queryShortVideo stream:" + e);
                }
                return articleVo;
            }).collect(Collectors.toList());
            Integer count = articleManager.queryArticleCount(param);
            articleVoPageResult.setData(articleVos);
            articleVoPageResult.setTotalSize(count);
            response.setResult(articleVoPageResult);
        } catch (Exception e) {
            logger.warn("queryShortVideo List:" + e);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.publishShortVideo", description = "发布短视频")
    public Response<Boolean> publishShortVideo(ArticleVo vo) {
        return responseCacheUtils.execute(() -> {
            vo.setArticleType(Constant.ArticleType.SHORT_VIDEO.name());
            vo.setGroupIds(new Long[]{0L});
            return publishAddArticle(vo);
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryCollectArticleV2", description = "根据类型分页查询收藏的短视频")
    public Response<List<ArticleVo>> queryCollectArticleV2(PageReq pageReq, String type) {
        Response<List<ArticleVo>> response = new Response<>();
        List<AppArticle> listAppArticle = Lists.newArrayList();
        List<ArticleVo> articleVoList = Lists.newArrayList();

        try {
            Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = DTOUtils.beanToMap(pageReq);
            paras.put("partyId", userPartyId);
            paras.put("tipType",Constant.ArticleTipType.COLLET.name());
            if (type.equals(Constant.ArticleType.SUBJECT.name())) {
                paras.put("articleType", Constant.ArticleType.SUBJECT.name());
            }
            if (type.equals(Constant.ArticleType.SHORT_VIDEO.name())) {
                paras.put("articleType", Constant.ArticleType.SHORT_VIDEO.name());
            }
            List<AppArticleTip> queryArticleTipList = articleManager.queryArticleTipList(paras);

            if (CollectionUtils.isNotEmpty(queryArticleTipList)) {
                queryArticleTipList.forEach(vo -> {
                    AppArticle article = articleManager.queryArticle(vo.getArticleId());
                    if (article == null) {// 收藏的动态已被删除
                        AppArticle articleNew = new AppArticle();
                        articleNew.setUser(vo.getToUser());
                        articleNew.setId(vo.getArticleId());
                        if (type.equals(Constant.ArticleType.SUBJECT.name())) {
                            articleNew.setArticle("抱歉,该微博已被删除");
                        }
                        if (type.equals(Constant.ArticleType.SHORT_VIDEO.name())) {
                            articleNew.setArticle("抱歉,该短视频已被删除");
                        }
                        articleNew.setPartyId(vo.getToPartyId());
                        articleNew.setTopLevel(0);
                        listAppArticle.add(articleNew);
                    } else {
                        // 先做是否点赞判断
                        paras.put("tipType", Constant.ArticleTipType.PRAISE.name());
                        paras.put("articleId", vo.getArticleId());
                        List<AppArticleTip> queryArticleTipList2 = articleManager.queryArticleTipList(paras);
                        if (CollectionUtils.isEmpty(queryArticleTipList2)) {
                            article.setArticleStatus(Constant.YESNO.NO.code);
                        } else {
                            article.setArticleStatus(queryArticleTipList2.get(0).getTipStatus());
                        }
                        article.setTopLevel(0);
                        article.setUser(vo.getToUser());
                        listAppArticle.add(article);
                    }
                });
                articleVoList = convertPojo(listAppArticle, userPartyId, 0L);
            }
            response.setResult(articleVoList);
        } catch (Exception e) {
            logger.warn("queryVideoListByArticleType error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryTopicList", description = "全部话题查询")
    public Response<List<ArticleTopicVo>> queryTopicList() {
        return responseCacheUtils.execute(() -> {
            return DTOUtils.map(articleManager.queryArticleTopic(new AppArticleTopic(), "TOP_TIME desc,POPULARITY desc",null, new Page(1, Short.MAX_VALUE)), ArticleTopicVo.class);
        });
    }

    @Override
    public Response<Boolean> addArticleTopic(ArticleTopicVo articleTopicVo) {
        Response<Boolean> resp = new Response<>();
        boolean flag = articleManager.addAppArticleTopic(AppArticleTopic.builder()
                .content("#" + articleTopicVo.getContent() + "#")
                .creator(articleTopicVo.getCreator())
                .popularity(0)
                .topSing("N")
                .build());
        resp.setResult(flag);
        return resp;
    }

    @Override
    public Response<PageResult<ArticleTopicVo>> queryArticleTopicListByPageAndParams(ArticleTopicReq articleTopicReq) {

        Response<PageResult<ArticleTopicVo>> resp = new Response<>();
        AppArticleTopic appArticleTopic = new AppArticleTopic();
        if (StringUtils.isNotEmpty(articleTopicReq.getContent())) {
            appArticleTopic.setContent(articleTopicReq.getContent());
        }
        if (StringUtils.isNotEmpty(articleTopicReq.getStartTime())) {
            appArticleTopic.setCreateTime(DateUtils.formatStr(articleTopicReq.getStartTime(),DateUtils.DATE_FMT));
        }
        List<AppArticleTopic> appArticleTopicList = articleManager.queryArticleTopic(appArticleTopic,articleTopicReq.getOrderBy(),articleTopicReq.getStopTime(), new Page(articleTopicReq.getStartPage(), articleTopicReq.getPageSize()));
        Integer articleCout = articleManager.queryArticleTopicCout(appArticleTopic,articleTopicReq.getStopTime());
        List<ArticleTopicVo> articleTopicVoList = null;
        try {
            articleTopicVoList = DTOUtils.map(appArticleTopicList, ArticleTopicVo.class);

            PageResult<ArticleTopicVo> pageResult = new PageResult<>();
            pageResult.setData(articleTopicVoList);
            pageResult.setTotalSize(articleCout);
            resp.setResult(pageResult);
        } catch (Exception e) {
            logger.warn("queryArticleTopicListByPageAndParams error", e);
        }
        return resp;
    }

    @Override
    public Response<Boolean> updateArticleTopic(ArticleTopicVo articleTopicVo) {
        Response<Boolean> resp = new Response<>();
        AppArticleTopic appArticleTopic = new AppArticleTopic();
        appArticleTopic.setContent(articleTopicVo.getContent());
        appArticleTopic.setPopularity(articleTopicVo.getPopularity());
        appArticleTopic.setTopSing(articleTopicVo.getTopSing());
        appArticleTopic.setId(articleTopicVo.getId());
        appArticleTopic.setTopTime(articleTopicVo.getTopTime());
        appArticleTopic.setIsDeleted(articleTopicVo.getIsDeleted());
        boolean flag = articleManager.updateArticleTopic(appArticleTopic);
        if (flag){
            resp.setCode(1000);
            resp.setResult(true);
            return resp;
        }else {
            return Response.ofFail();
        }
    }

    @Override
    public Response<PageResult<ArticleTipVoV3>> queryArticleTipListByParams(ArticleTipReq articleTipReq) {

        Response<PageResult<ArticleTipVoV3>> resp = new Response<>();
        Map<String, Object> paras = Maps.newConcurrentMap();
        List<ArticleTipVoV3> articleTipVoV3List = null;
        try {
            paras.put("startPage", articleTipReq.getStartPage());
            paras.put("pageSize", articleTipReq.getPageSize());
            paras.put("tipType", Constant.ArticleTipType.REPORT.name());
            if (StringUtils.isNotEmpty(articleTipReq.getDealStatus())) {
                paras.put("dealStatus", articleTipReq.getDealStatus());
            }
            if (StringUtils.isNotEmpty(articleTipReq.getStartDate())) {
                paras.put("startDate", articleTipReq.getStartDate());
            }
            if (StringUtils.isNotEmpty(articleTipReq.getEndDate())) {
                paras.put("endDate", articleTipReq.getEndDate());
            }
            List<AppArticleTip> articleTipList = articleManager.selectArticleTipListByParam(paras);
            Integer tipCount = articleManager.queryArticleTipCountIsDeleted(paras);
            articleTipVoV3List = DTOUtils.map(articleTipList, ArticleTipVoV3.class);

            for (ArticleTipVoV3 articleTipVoV3:articleTipVoV3List){
                UserVo toUser = personalService.queryUser(articleTipVoV3.getToPartyId()).getResult();
                UserVo reportUsesr = personalService.queryUser(articleTipVoV3.getPartyId()).getResult();
                if (toUser != null) {
                    articleTipVoV3.setUserMobile(toUser.getLoginName());
                    articleTipVoV3.setToUserName(toUser.getShowName());
                }
                if (reportUsesr !=null){
                    articleTipVoV3.setReportUserName(reportUsesr.getShowName());
                }
            }

            PageResult pageResult = new PageResult();
            pageResult.setData(articleTipVoV3List);
            pageResult.setTotalSize(tipCount);

            resp.setResult(pageResult);
        } catch (Exception e) {
            logger.warn("queryArticleTipListByParams error", e);
        }
        return resp;
    }

    /**
     * top端查询用户最近发布的3个工作地点
     * @return
     */
    @Override
    public Response<List<String>> queryTOPUserUsedAddress(String loginName) {
        return responseCacheUtils.execute(()->{
            User user=userManager.getUserByLoginAccount(loginName);
            if(user == null){ //用户不存在
               throw new BusiException("用户不存在");
            }
            List<String> strings = queryUserAddressByPartyId(user.getPartyId());
            return strings;
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryShortVideos", description = "短视频查询")
    public Response<List<ArticleVo>> queryShortVideos(String topic, Long lastId) {
        return responseCacheUtils.execute(() -> {
            if (StringUtils.isEmpty(topic)) {
                return queryArticleListV3(ArticleReq.builder().articleType(Constant.ArticleType.SHORT_VIDEO.name())
                        .lastId(lastId).build()).getResult();
            } else {
                if(lastId == null || lastId == 0){
                    List<AppArticle> list = articleManager.queryArticle(AppArticle.builder().articleType(Constant.ArticleType.SHORT_VIDEO.name()).topic(topic).lastId(lastId).build()
                            , "TOP_TIME desc,REC_SIGN desc,PRAISE_COUNT desc"
                            , new Page(1, Short.MAX_VALUE)).stream().peek(v -> {
                        v.setUser(userManager.queryUser(v.getPartyId()));
                    }).collect(Collectors.toList());
                    Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
                    return convertPojo(list, userPartyId, 0L);
                }else {
                    return new ArrayList<>();
                }
            }
        });
    }


    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.ArticleService.queryArticleListByParam", description = " 分页查询动态")
    public Response<List<ArticleVo>> queryArticleListByParam(ArticleReq req) {
        Response<List<ArticleVo>> response = ResponseUtils.successResponse();
        try {
            Map<String, Object> paras = DTOUtils.beanToMap(req);
            if (req.getIsExcludeShareUrl()) {
                paras.put("excludeShareUrl", "paras");
                paras.put("groupId", 0);
            }
            paras.put("articleType", Constant.ArticleType.SUBJECT.name());
            if (req.getStartDate() != null && !(req.getStartDate().isEmpty())) {
                String date = req.getStartDate() + " 00:00:00";
                Date startDate = DateUtils.formatStr(date, "yyyy-MM-dd HH:mm:ss");
                paras.put("startDate", startDate);
            } else {
                paras.remove("startDate");
            }
            if (req.getEndDate() != null && !(req.getEndDate().isEmpty())) {
                String date = req.getEndDate() + " 23:59:59";
                Date endDate = DateUtils.formatStr(date, "yyyy-MM-dd HH:mm:ss");
                paras.put("endDate", endDate);
            } else {
                paras.remove("endDate");
            }
            if (req.getNickName() == null || req.getNickName().isEmpty()) {
                paras.remove("nickName");
            }
            if (req.getQueryKey() == null || req.getQueryKey().isEmpty()) {
                paras.remove("queryKey");
            }
//            paras.put("orderBy", StringUtils.isEmpty(req.getOrderBy())?null:req.getOrderBy());
            paras.put("orderBy", null);
            List<AppArticle> list = articleManager.queryArticleList(paras);
            List<ArticleVo> result = null;
            if (req.getIsExcludeShareUrl()) { //电商使用
                result = convertPojo(list, null, 0L);
            } else { //仅top后台使用
                result = convertPojoTopArticle(list);
                /*result = result.parallelStream().map(vo->{
                    if(StringUtils.isNotEmpty(vo.getArticleAudio())){
                       String  str = SysConfigUtil.getStr("APP_FILESERVER_URL");
                        try {
                            String articleAudio =  HttpUtils.executeGet(str+"convert/amr2mp3?url="+vo.getArticleAudio());
                            logger.info("ArticleAudio:{}", articleAudio);
                            vo.setArticleAudio(articleAudio);
                        } catch (Exception e) {
                            logger.info("get ArticleAudio error:", e);
                        }
                    }
                    return vo;
                }).collect(Collectors.toList());*/
            }
            response.setResult(result);
        } catch (Exception e) {
            logger.warn("queryArticleListByParam error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    /**
     * //转换微博对象仅top后台使用
     *
     * @param list
     * @return
     */
    private List<ArticleVo> convertPojoTopArticle(List<AppArticle> list) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        articleVoList = list.stream()
                .map(vo -> {
                    ArticleVo articleVo = null;
                    try {
                        if (StringUtils.contains(vo.getShareUrl(), "NATIVE")) {
                            vo.setShareUrl(SysConfigUtil.getStr("article_share_url") + "?id=" + vo.getId());
                        }
                        StringBuilder sb = new StringBuilder();
                        if (StringUtils.isNotEmpty(vo.getArticle())) {
                            sb.append(vo.getArticle());
                        }
                        if (StringUtils.isNotEmpty(vo.getShareUrl())) {
                            if (StringUtils.isNotEmpty(vo.getShareUrl())) {
                                sb.append("【转发】"); //转发
                            }
                            vo.setArticleImgs(null);
                        } else {
                            if (StringUtils.isNotEmpty(vo.getArticleAudio())) {
                                sb.append("【语音】"); //语音
                            }
                            if (StringUtils.isNotEmpty(vo.getVideoUrl())) {
                                sb.append("【视频】"); //视频
                                vo.setArticleImgs(null);     //top微博当为视频的时候图片为空
                            }
                            if (StringUtils.isNotEmpty(vo.getArticleImgs())) {
                                sb.append("【图片】"); //图片  
                            }
                        }
                        vo.setComments(sb.toString());
                        articleVo = DTOUtils.map(vo, ArticleVo.class);
                        if (vo.getUser() != null) {
                            articleVo.setShowName(getUserName(vo.getPartyId()));
                        } else {
                            articleVo.setShowName("xl" + vo.getPartyId());
                        }
                        articleVo = selectArticle(articleVo);
                    } catch (Exception e) {
                        logger.warn("convertPojoTopArticle error", e);
                    }
                    return articleVo;
                })
                .collect(Collectors.toList());
        return articleVoList;
    }

    @Override
    public Response<Boolean> setTop4Article(Long id) {

        try {
            checkArgument(id != null, "参数错误，id不能为空");
            articleManager.setTopLevel4LearningPPT(id);
        } catch (IllegalArgumentException e) {
            logger.info("===========top 学习专区 加精取消加精参数错误，id ：[[ {} ]]===========", id);
            return Response.ofFail(e.getMessage());
        } catch (Exception e) {
            logger.warn("===========top 学习专区 加精取消加精操作错误，id:[[ {} ]]===========", id);
            return Response.ofFail();
        }
        return Response.ofSuccess(Boolean.TRUE);
    }

    /*private String convertAddress(AreaVo area) {
        List<MapVo>  mapVoList = new ArrayList<>();
        if(StringUtils.isNotEmpty(area.getProvince().getName())){
            MapVo mapVo = new MapVo();
            mapVo.setKey("province");
            mapVo.setValue(area.getProvince().getName());
            mapVoList.add(mapVo);
        }
        if(StringUtils.isNotEmpty(area.getCity().getName())){
            MapVo mapVo = new MapVo();
            mapVo.setKey("city");
            mapVo.setValue(area.getCity().getName());
            mapVoList.add(mapVo);
        }
        if(StringUtils.isNotEmpty(area.getCounty().getName())){
            MapVo mapVo = new MapVo();
            mapVo.setKey("county");
            mapVo.setValue(area.getCounty().getName());
            mapVoList.add(mapVo);
        }
        if(StringUtils.isNotEmpty(area.getTown().getName())){
            MapVo mapVo = new MapVo();
            mapVo.setKey("town");
            mapVo.setValue(area.getTown().getName());
            mapVoList.add(mapVo);
        }
        if(StringUtils.isNotEmpty(area.getVillage().getName())){
            MapVo mapVo = new MapVo();
            mapVo.setKey("village");
            mapVo.setValue(area.getVillage().getName());
            mapVoList.add(mapVo);
        }
        String locationJson = JSONArray.fromObject(mapVoList).toString();
        return locationJson;
    }*/

    /**
     * 生成动态分享内容,针对微信朋友圈做不同处理
     *
     * @param article
     * @param articleShow
     * @return
     */
    private String shareContent(AppArticle article, String articleShow) {

        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(article.getArticle())) {
            sb.append(article.getArticle().replace(Constant.ArticleContent.TOPIC.start,"").replace(Constant.ArticleContent.TOPIC.end,""));
        } else {
            sb.append(articleShow);
        }
        return sb.toString();
    }


    /**
     * 记录用户微博搜索，查看日志
     *
     * @param type
     * @param content
     */
    private void doArticleRecord(String type, String content) {
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
        String clentVersion = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
        String systemType = sessionHelper.getSessionProp(AppSessionConstants.SYSTEM_TYPE, String.class);
        String remoteIp = sessionHelper.getGatewayProp("remoteAddr");
        logManager.saveSearchRecord(AppClientLogSearch.builder().partyId(partyId).deviceId(deviceId).systemType(systemType).version(clentVersion).type(type).content(content).build());
    }

}
