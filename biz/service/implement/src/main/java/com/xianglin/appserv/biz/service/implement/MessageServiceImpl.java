/**
 *
 */
package com.xianglin.appserv.biz.service.implement;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.biz.shared.BusinessManager;
import com.xianglin.appserv.biz.shared.MessageManager;
import com.xianglin.appserv.biz.shared.util.ResponseCacheUtils;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.Msg;
import com.xianglin.appserv.common.service.facade.MessageService;
import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgStatus;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.facade.model.vo.PushMsgCheckVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.fala.session.Session;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wanglei 2016年8月16日下午5:43:50
 */
@ServiceInterface
@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    /**  */
    @Autowired
    private MessageManager messageManager;

    /**
     * sessionHelper
     */
    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private BusinessManager businessManager;

    @Autowired
    private ResponseCacheUtils responseCacheUtils;

    /**
     * @see com.xianglin.appserv.common.service.facade.MessageService#list(com.xianglin.appserv.common.service.facade.model.Request)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.biz.service.MessageService.list", description = "查询当前用户消息")
    public Response<List<MsgVo>> list(Request<MsgQuery> req) {
        Response<List<MsgVo>> response = ResponseUtils.successResponse();
        try {
            Session session = sessionHelper.getSession();
            Long partyId = Long.valueOf(session.getAttribute(SessionConstants.PARTY_ID));
            List<MsgVo> list = messageManager.list(req.getReq(), partyId);
            response.setResult(list);
        } catch (Exception e) {
            logger.error("list error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.MessageService#detail(com.xianglin.appserv.common.service.facade.model.Request)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.biz.service.MessageService.detail", description = "查询消息详情")
    public Response<MsgVo> detail(Request<Long> req) {
        Response<MsgVo> response = ResponseUtils.successResponse();
        try {
            Msg msg = messageManager.queryById(req.getReq());
            if (msg != null) {
                MsgVo vo = DTOUtils.map(msg, MsgVo.class);
                if (StringUtils.isNotEmpty(vo.getRemark())) {
                    JSONObject remark = JSON.parseObject(vo.getRemark());
                    if (remark.containsKey("createdate")) {
                        vo.setCreateTime(new java.sql.Date(remark.getLong("createdate")));
                    }
                }
                response.setResult(vo);
            }
        } catch (Exception e) {
            logger.error("detail error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.MessageService#saveUpdate(com.xianglin.appserv.common.service.facade.model.Request)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.biz.service.MessageService.saveUpdate", description = "更新/新增")
    public Response<Boolean> saveUpdate(Request<MsgVo> req) {
        Response<Boolean> response = ResponseUtils.successResponse(true);
        try {
            response.setResult(messageManager.saveUpdate(req.getReq()) != null);
        } catch (Exception e) {
            logger.error("detail error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
            response.setResult(false);
        }
        return response;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.MessageService#sendMsg(com.xianglin.appserv.common.service.facade.model.Request)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.MessageService.sendMsg", description = "发送消息")
    public Response<Boolean> sendMsg(Request<MsgVo> req) {
        Response<Boolean> response = ResponseUtils.successResponse(true);
        try {
            ArrayList<Long> list = new ArrayList(1);
            if (req.getUserPartyId() != null) {
                list.add(req.getUserPartyId());
            }
            response.setResult(messageManager.sendMsg(req.getReq(), list));
        } catch (Exception e) {
            logger.error("sendMsg error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
            response.setResult(false);
        }
        return response;
    }

    @Override
    public Response<Boolean> sendMsg(Request<MsgVo> req, List<Long> partyIds) {
        Response<Boolean> response = ResponseUtils.successResponse(true);
        try {
            messageManager.sendMsg(req.getReq(), partyIds);
        } catch (Exception e) {
            logger.error("sendMsg error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
            response.setResult(false);
        }
        return response;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.MessageService#read(com.xianglin.appserv.common.service.facade.model.Request)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.biz.service.MessageService.read", description = "更新消息为已读")
    public Response<MsgVo> read(Request<Long> req) {
        Response<MsgVo> response = ResponseUtils.successResponse();
        try {
            Long partyId = null;
            try {
                Session session = sessionHelper.getSession();
                partyId = Long.valueOf(session.getAttribute(SessionConstants.PARTY_ID));
            } catch (Exception e) {
            }
            if (partyId == null) {
                partyId = req.getUserPartyId();
            }
            response.setResult(messageManager.read(req.getReq(), partyId));
        } catch (Exception e) {
            logger.error("read error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.MessageService#praise(com.xianglin.appserv.common.service.facade.model.Request)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.biz.service.MessageService.praise", description = "消息点赞")
    public Response<Boolean> praise(Request<Long> req) {
        Response<Boolean> response = ResponseUtils.successResponse(true);
        try {
            Session session = sessionHelper.getSession();
            Long partyId = Long.valueOf(session.getAttribute(SessionConstants.PARTY_ID));
            response.setResult(messageManager.praise(req.getReq(), partyId));
        } catch (Exception e) {
            logger.error("praise error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
            response.setResult(false);
        }
        return response;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.MessageService#updatePushMsg(com.xianglin.appserv.common.service.facade.model.vo.PushMsgCheckVo)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.biz.service.MessageService.updatePushMsg", description = "同步推送消息状态")
    public Response<Boolean> updatePushMsg(PushMsgCheckVo req) {
        Response<Boolean> response = ResponseUtils.successResponse(true);
        try {
            response.setResult(messageManager.updatePushMsg(req));
        } catch (Exception e) {
            logger.error("praise error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
            response.setResult(false);
        }
        return response;
    }

    /**
     * 根据条件推荐6条3天内的播放量最高的新闻
     *
     * @return
     */
    @Override
    public Response<List<MsgVo>> recommendVideo(String msgTag) {
        Response<List<MsgVo>> response = ResponseUtils.successResponse();
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            String endDay = fmt.format(new Date()).toString() + " 59:59:59";
            calendar.roll(Calendar.DAY_OF_YEAR, -2);
            String startDay = fmt.format(calendar.getTime()) + " 00:00:00";
            Map<String, Object> paras = new HashMap<>();
            paras.put("startDay", startDay);
            paras.put("endDay", endDay);
            paras.put("msgTag", msgTag);
            paras.put("msgType", Constant.MsgType.NEWS.name());
            List<MsgVo> list = messageManager.queryMsgByparam(paras);
            for (MsgVo msgVo : list) {
                msgVo.setReadNum(msgVo.getReadNum() * 10 + RandomUtils.nextInt(0, 10));
                msgVo.setDateTime(indecateTime(msgVo.getCreateTime()));
            }
            response.setResult(list);
        } catch (Exception e) {
            logger.error("recommendVideo error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.biz.service.MessageService.queryPushMsg", description = "查询待推送消息")
    public Response<List<MsgVo>> queryPushMsg() {
        return responseCacheUtils.execute(() -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            return DTOUtils.map(messageManager.queryPushMsg(partyId), MsgVo.class);
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.biz.service.MessageService.queryPushStatus", description = "查询待推送轮询开关")
    public Response<Boolean> queryPushStatus() {
        return responseCacheUtils.execute(() -> {
            boolean flag = false;
            String type = SysConfigUtil.getStr("MSG_SYN_BUSINESS", "");
            Set<String> busiSet = businessManager.queryUserBusiness();
            if (CollectionUtils.isNotEmpty(busiSet)) {
                flag = Arrays.stream(type.split(",")).anyMatch(busiSet::contains);
            }
            return flag;
        });
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
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String showTime = sdf.format(createTime);
                return showTime;
            }
        }/* else if (fmt.format(yesterday).equals(fmt.format(createTime))) {// 判断如果是昨天
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String showTime = "昨天" + sdf.format(createTime);
            return showTime;
        }*/ else {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            String showTime = sdf.format(createTime);
            return showTime;
        }
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.MessageService#hasUnReadMsg(java.lang.Long)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.biz.service.MessageService.hasUnReadMsg", description = "查询用户是否有未读消息")
    public Response<Boolean> hasUnReadMsg(Long partyId) {
        Response<Boolean> response = ResponseUtils.successResponse(false);
        try {
            if (partyId == null) {
                partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            }
            MsgQuery query = MsgQuery.builder().build();
            query.setStatus(MsgStatus.UNREAD.code);
            List<MsgVo> list = messageManager.list(query, partyId);
            if (CollectionUtils.isNotEmpty(list)) {
                response.setResult(true);
            }
        } catch (Exception e) {
            logger.error("hasUnReadMsg error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

}
