package com.xianglin.appserv.biz.service.implement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.biz.shared.ActivityManager;
import com.xianglin.appserv.biz.shared.MessageManager;
import com.xianglin.appserv.biz.shared.PropExtendManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.app.DiscoryService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;

import java.text.SimpleDateFormat;
import java.util.*;

import net.sf.json.JSONArray;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wanglei on 2017/5/2.
 */
@Service("discoryService")
@ServiceInterface
public class DiscoryServiceImpl implements DiscoryService {
    private static final Logger logger = LoggerFactory.getLogger(DiscoryServiceImpl.class);
    @Autowired
    private SessionHelper sessionHelper;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ActivityManager activityManager;
    @Autowired
    private PropExtendManager propExtendManager;

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.DiscoryService.discoryDiscount", description = "发现页面优惠专享")
    public Response<String> discoryDiscount() {
        Response<String> response = ResponseUtils.successResponse();
        try {
            response.setResult(SysConfigUtil.getStr("discount_url"));
        } catch (Exception e) {
            logger.warn("discoryDiscount error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.DiscoryService.discoryActivity", description = "发现页面活动分页查询")
    public Response<List<ActivityVo>> discoryActivity(PageReq req) {
        Response<List<ActivityVo>> response = ResponseUtils.successResponse();
        try {
            Map<String, Object> paras = DTOUtils.beanToMap(req);
            List<AppActivity> list = activityManager.queryActivitys(paras);
            List<ActivityVo> result = convertPojo(list);
            response.setResult(result);
        } catch (Exception e) {
            logger.warn("discoryActivity error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.DiscoryService.discoryActivityUrl", description = "查询活动地址")
    public Response<String> discoryActivityUrl() {
        Response<String> response = ResponseUtils.successResponse();
        try {
            response.setResult(SysConfigUtil.getStr("discoryActivityUrl"));
        } catch (Exception e) {
            logger.warn("discoryDiscount error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.DiscoryService.discoryMsg", description = "发现页面新闻查询")
    public Response<List<MsgVo>> discoryMsg(MsgQuery req) {
        Response<List<MsgVo>> response = ResponseUtils.successResponse();
        try {
            List<MsgVo> list = messageManager.listNews(req);
            for (MsgVo vo : list) {
                vo.setUrl(SysConfigUtil.getStr("H5WECHAT_URL") + "/home/nodeManager/topNews.html?id=" + vo.getId());
                vo.setMessage(StringUtils.substring(StringUtils.replacePattern(vo.getMessage(), "<.*?>", ""), 0, 50));
                list.add(vo);
            }
            response.setResult(list);
        } catch (Exception e) {
            logger.warn("discoryMsg error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }

        return response;
    }

    @Override
    public Response<Boolean> submitCreditCardApply(UserCreaditApplyVo req) {
        Response<Boolean> response = ResponseUtils.successResponse();
        try {
            AppUserCreditApply appUserCreaditApply = new AppUserCreditApply();
            appUserCreaditApply.setPartyId(req.getPartyId());
            appUserCreaditApply.setApplyCardno(req.getApplyCardno());
            appUserCreaditApply.setApplyMobile(req.getApplyMobile());
            appUserCreaditApply.setApplyUsername(req.getApplyUsername());
            Boolean insertUserCreaditApply = userManager.addUserCreaditApply(appUserCreaditApply);
            response.setResult(insertUserCreaditApply);
        } catch (Exception e) {
            logger.error("submitCreditCardApply error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * 查询用户新闻
     *
     * @param req
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.DiscoryService.listUserNews", description = "查询用户新闻")
    public Response<List<MsgVo>> listUserNews(MsgQuery req) {
        Response<List<MsgVo>> response = ResponseUtils.successResponse();
        try {
            UserMsg um = UserMsg.builder().msgType(req.getMsgType()).build();
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            um.setDeviceId(deviceId);
            List<Msg> list = messageManager.listDeviceNews(um, Page.builder().startPage(req.getStartPage()).pageSize(req.getPageSize()).build());
            if (org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
                response.setResult(recommendNews(req).getResult());
            } else {
                list = decoreateNews(list, partyId);
                response.setResult(DTOUtils.map(list, MsgVo.class));
            }
        } catch (Exception e) {
            logger.warn("discoryMsg error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * 新闻推荐
     *
     * @param req 设备号
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.DiscoryService.recommendNews", description = "新闻推荐")
    public Response<List<MsgVo>> recommendNews(MsgQuery req) {
        Response<List<MsgVo>> response = ResponseUtils.successResponse();
        try {
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            String msgTag = req.getMsgType();
            if (req.getPageSize() == null) {
                req.setPageSize(10);
            }
            List<String> tags = new ArrayList<>();
            if (StringUtils.isEmpty(msgTag)) {
                List<MapVo> list = queryChannel().getResult();
                int i = 0;
                for (MapVo vo : list) {
                    tags.add(vo.getKey());
                    if (++i > 6) {
                        break;
                    }
                }
            } else {
                tags.add(msgTag);
            }
            List<Msg> list = null;
            if (tags.size() > 1 && tags.contains(Constant.MsgNewsTag.VIDEO.name())) {
                int videoSize = 2;
                tags.remove(Constant.MsgNewsTag.VIDEO.name());
                List<Msg> newsList = messageManager.recommendNews(deviceId, tags, req.getPageSize() - videoSize);
                tags.clear();
                tags.add(Constant.MsgNewsTag.VIDEO.name());
                List<Msg> videoList = messageManager.recommendNews(deviceId, tags, videoSize);
                list = ListUtils.union(videoList, newsList);
            } else {
                list = messageManager.recommendNews(deviceId, tags, req.getPageSize());
            }
            list = CollectionUtils.randomList(list);
            if (org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
                UserMsg um = UserMsg.builder().msgType(req.getMsgType()).deviceId(deviceId).build();
                list = messageManager.listDeviceNews(um, Page.builder().startPage(1).pageSize(10).build());
            }
            list = decoreateNews(list, null);
            response.setResult(DTOUtils.map(list, MsgVo.class));
        } catch (Exception e) {
            logger.warn("discoryMsg error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * 新闻状态更新
     * 包含阅读，分享，点赞等
     *
     * @param req
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.DiscoryService.operateNews", description = "新闻状态更新")
    public Response<Boolean> operateNews(MsgQuery req) {
        Response<Boolean> response = ResponseUtils.successResponse(true);
        try {
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            messageManager.updateNews(UserMsg.builder().msgId(req.getMsgId()).deviceId(deviceId).operateType(req.getOperateType()).build());
        } catch (Exception e) {
            logger.warn("discoryMsg error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.DiscoryService.queryChannel", description = "查询频道列表")
    public Response<List<MapVo>> queryChannel() {
        List<MapVo> mapVoList = new ArrayList<>();
        Response<List<MapVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
            if (partyId == null) {
                String value = SysConfigUtil.getStr("gold_channel");
                mapVoList = getMapVoList(value);
            } else {
                //查询当前登录用户的频道列表
                String value = SysConfigUtil.getStr("gold_channel");
                AppPropExtend appPropExtend = AppPropExtend.builder().relationId(partyId).type(User.class.getSimpleName()).value(value).ekey(MapVo.USER_CHANNEL).deviceId(deviceId).build();
                appPropExtend = propExtendManager.queryAndInit(appPropExtend);

                mapVoList = getMapVoList(appPropExtend.getValue());
            }
            resp.setResult(mapVoList);
        } catch (Exception e) {
            logger.warn("queryChannel error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    private List<MapVo> getMapVoList(String value) {
        List<MapVo> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(value)) {
            JSONArray jsonArray = JSONArray.fromObject(value);
            List<Map<String, Object>> list2 = (List<Map<String, Object>>) JSONArray.toCollection(jsonArray, Map.class);
            for (int i = 0; i < list2.size(); i++) {
                MapVo map = new MapVo();
                map.setKey(list2.get(i).get("key").toString());
                map.setValue(list2.get(i).get("value").toString());
                list.add(map);
            }
        }
        return list;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.DiscoryService.updateChannel", description = "修改频道列表")
    public Response<Boolean> updateChannel(List<MapVo> list) {
        Response<Boolean> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            if (list.size() > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("relationId", partyId);
                map.put("type", User.class.getSimpleName());
                map.put("ekey", MapVo.USER_CHANNEL);
                List<AppPropExtend> appPropExtendList = propExtendManager.queryChannel(map);
                if (list.size() > 0) {
                    AppPropExtend appPropExtend = AppPropExtend.builder().relationId(partyId).type(User.class.getSimpleName()).ekey(MapVo.USER_CHANNEL).build();
                    appPropExtend.setValue(list.toString());
                    appPropExtend.setId(appPropExtendList.get(0).getId());
                    Boolean update = propExtendManager.update(appPropExtend);
                    resp.setResult(update);
                }

            }
        } catch (Exception e) {
            logger.warn("updateChannel error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }

        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.DiscoryService.newsDetail", description = "新闻明显查询")
    public Response<MsgVo> newsDetail(Long id) {
        Response<MsgVo> resp = ResponseUtils.successResponse();
        try {

            Msg msg = messageManager.queryById(id);
            if (msg != null) {
                msg = convertMsg(msg);
                MsgVo vo = DTOUtils.map(msg, MsgVo.class);

                String deviceId = sessionHelper.getSessionProp(SessionConstants.DEVICE_ID, String.class);
                List<UserMsg> list = messageManager.queryUserMsg(UserMsg.builder().msgId(id).deviceId(deviceId).build(), null);
                if (list.size() == 1) {
                    vo.setPraiseTread(list.get(0).getPraiseTread());
                }
                resp.setResult(vo);
            }
        } catch (Exception e) {
            logger.warn("updateChannel error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**
     * 疲劳新闻处理
     *
     * @param list
     * @return
     */
    private List<Msg> decoreateNews(List<Msg> list, Long partyId) {
        List<Msg> result = new ArrayList<>(list.size());
        for (Msg msg : list) {
            result.add(convertMsg(msg));
        }
        return result;
    }

    /**
     * 新闻内容修饰
     *
     * @param msg
     * @return
     */
    private Msg convertMsg(Msg msg) {
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        if (StringUtils.equals(msg.getMsgTag(), Constant.MsgNewsTag.VIDEO.name())) {
            if (partyId != null) {
                msg.setUrl(SysConfigUtil.getStr("discory_video_url") + "?id=" + msg.getId() + "&partyId=" + partyId);
            } else {
                msg.setUrl(SysConfigUtil.getStr("discory_video_url") + "?id=" + msg.getId());
            }
        } else {
            if (partyId != null) {
                msg.setUrl(SysConfigUtil.getStr("H5WECHAT_URL") + "/home/nodeManager/topNews.html?id=" + msg.getId() + "&partyId=" + partyId);
            } else {
                msg.setUrl(SysConfigUtil.getStr("H5WECHAT_URL") + "/home/nodeManager/topNews.html?id=" + msg.getId());
            }

        }
        if (StringUtils.isNotEmpty(msg.getRemark())) {
            JSONObject remark = JSON.parseObject(msg.getRemark());
            if (remark.containsKey("createdate")) {
                msg.setCreateTime(new java.sql.Date(remark.getLong("createdate")));
            }
        }
        msg.setComments(StringUtils.replacePattern(msg.getMessage(), "<.*?>", ""));
        msg.setReadNum((msg.getReadNum() * 10) + new DateTime(msg.getUpdateTime()).getSecondOfMinute() % 9 + 1);//阅读量随机
        msg.setDateTime(indecateTime(msg.getCreateTime()));
        return msg;
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
            if (intervalMinutes < 60 && intervalMinutes > 1) {
                String showTime = intervalMinutes + "分钟前";
                return showTime;
            }
            if (intervalMinutes < 1) {
                String showTime = "刚刚";
                return showTime;
            }
            if (intervalMinutes < 1440 && intervalMinutes > 61) {
                //N小时前
                String showTime = (intervalMinutes / 60) + "小时前";
                return showTime;
            } else {
                String showTime = (intervalMinutes / 60) + "小时前";
                return showTime;
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            String showTime = sdf.format(createTime);
            return showTime;
        }
    }

    /**
     * 实体类List转换方法
     *
     * @param list
     * @return
     */
    private List<ActivityVo> convertPojo(List<AppActivity> list) {
        List<ActivityVo> result = new ArrayList<>(list.size());
        for (AppActivity activity : list) {
            ActivityVo vo;
            try {
                vo = DTOUtils.map(activity, ActivityVo.class);
                result.add(vo);
            } catch (Exception e) {
                logger.warn("convertPojo error", e);
            }
        }
        return result;
    }
}
