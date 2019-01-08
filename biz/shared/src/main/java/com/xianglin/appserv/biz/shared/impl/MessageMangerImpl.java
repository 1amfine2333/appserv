/**
 *
 */
package com.xianglin.appserv.biz.shared.impl;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import com.alibaba.dubbo.config.annotation.Service;
import com.xianglin.appserv.biz.shared.MessageManager;
import com.xianglin.appserv.biz.shared.push.MessagePushService;
import com.xianglin.appserv.common.dal.daointerface.AppPushDAO;
import com.xianglin.appserv.common.dal.daointerface.MsgDAO;
import com.xianglin.appserv.common.dal.daointerface.UserDAO;
import com.xianglin.appserv.common.dal.daointerface.UserMsgDAO;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgPushStatus;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgType;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.YESNO;
import com.xianglin.appserv.common.service.facade.model.enums.YesNo;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.facade.model.vo.PushMsgCheckVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.common.util.RedisUtil;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.appserv.core.model.exception.AppException;
import com.xianglin.fala.session.RedisClient;
import com.xianglin.fala.session.RedisClient.RedisOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.ShardedJedis;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wanglei 2016年8月15日上午10:40:02
 */
@Service
public class MessageMangerImpl implements MessageManager {

    private static final Logger logger = LoggerFactory.getLogger(MessageMangerImpl.class);

    @Autowired
    private MsgDAO msgDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserMsgDAO userMsgDAO;

    @Autowired
    private MessagePushService messagePushService;

    @Autowired
    private AppPushDAO appPushDAO;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 消息缓存前缀
     */
    private final String MSG_PRIFIX = "MessageManager_";

    @Override
    public List<MsgVo> list(MsgQuery req, Long userPartyId) throws Exception {
        Map<String, Object> paras = DTOUtils.beanToMap(req);
        paras.put("partyId", userPartyId);
        if (StringUtils.isNotEmpty(req.getMsgType())) {
            paras.put("msgType", req.getMsgType());
        } else {
            paras.put("msgTypes", Constant.MsgType.toList(MsgType.NEWS.name(), MsgType.ESHOP.name()));
        }
        List<Map<String, Object>> msgs = msgDAO.queryMap(paras);
        if (!CollectionUtils.isEmpty(msgs)) {
            for (Map<String, Object> map : msgs) {
                String msgUrl = map.get("url") == null ? "" : map.get("url").toString();
                if (StringUtils.isEmpty(msgUrl)) {
                    msgUrl = SysConfigUtil.getStr("MESSAGE_DETAIL_URL") + "?id=" + map.get("id") + "&type=";
                    if (StringUtils.equals(req.getMsgType(), MsgType.ARTICLE.name())) {
                        msgUrl += "other";
                    } else {
                        msgUrl += "sys";
                    }
                    map.put("url", msgUrl);
                }
            }
            return DTOUtils.map(msgs, MsgVo.class);
        }
        return new ArrayList<>();
    }

    /**
     * @throws Exception
     * @see com.xianglin.appserv.biz.shared.MessageManager#detail(java.lang.Long, java.lang.Long)
     */
    @Override
    public MsgVo detail(Long req, Long userPartyId) throws Exception {
        return DTOUtils.map(msgDAO.selectByPrimaryKey(req), MsgVo.class);
    }

    /**
     * @throws Exception
     * @see com.xianglin.appserv.biz.shared.MessageManager#saveUpdate(com.xianglin.appserv.common.service.facade.model.vo.MsgVo)
     */
    @Override
    public Long saveUpdate(MsgVo req) throws Exception {
        Msg msg = DTOUtils.map(req, Msg.class);
        if (null != req.getId()) {
            msgDAO.updateByPrimaryKeySelective(msg);
            return req.getId();
        } else {
            if (StringUtils.isEmpty(msg.getIsDeleted())) {
                msg.setIsDeleted(YESNO.NO.code);
            }
            msgDAO.insert(msg);

            String msgUrl = msg.getUrl();
            if (StringUtils.isEmpty(msgUrl)) {
                msgUrl = SysConfigUtil.getStr("MESSAGE_DETAIL_URL") + "?type=";
                if (StringUtils.equals(msg.getMsgType(), MsgType.ARTICLE.name()) || StringUtils.equals(msg.getMsgType(), MsgType.USER_GENERLOGY_TIP.name())|| StringUtils.equals(msg.getMsgType(), MsgType.CASHBONUS_TIP.name())||StringUtils.equals(msg.getMsgType(), MsgType.FEEDBACK.name())) {
                    msgUrl += "other";
                } else {
                    msgUrl += "sys";
                }
            }
            msgUrl += "&id=" + msg.getId();
            msg.setUrl(msgUrl);
            msgDAO.updateByPrimaryKeySelective(msg);
            req.setUrl(msgUrl);
            return msg.getId();
        }
    }

    /**
     * @see com.xianglin.appserv.biz.shared.MessageManager#sendMsg(com.xianglin.appserv.common.service.facade.model.vo.MsgVo)
     */
    @Override
    public Boolean sendMsg(MsgVo req) {
        try {
            Long msgId = saveUpdate(req);
            req.setId(msgId);

            UserMsg um;
            Integer startPage = 1;
            Integer pageSize = 100;
            Map<String, Object> paras = DTOUtils.queryMap();
            if (req.getPartyId() != null) {//单人消息
                paras.put("partyId", req.getPartyId());
            }
            paras.put("pageSize", pageSize);
            List<AppPush> list = appPushDAO.query(paras);
            while (list.size() > 0) {
                batchInsert(list, req);
                if (list.size() < pageSize) {
                    break;
                }
                paras.put("startPage", ++startPage);
                list = appPushDAO.query(paras);
            }
            messagePushService.push(req);
        } catch (Exception e) {
            logger.error("jpush error ", e);
        }
        return true;
    }

    @Override
    public Boolean sendMsg(MsgVo req, List<Long> partyIds) throws BusiException {
        try {
            //判断消息是否需要缓存
            logger.info("sendMsg {},partyIds:{}",req,partyIds);
            final Long msgId = saveUpdate(req);
            String type = SysConfigUtil.getStr("MSG_SYN_BUSINESS", "");
            boolean flag = Arrays.stream(type.split(",")).anyMatch(req.getMsgType()::equalsIgnoreCase);
            if (flag) {//需要缓存
                logger.info("send push msg {},{}",type,flag);
                Msg msg = DTOUtils.map(req, Msg.class);
                partyIds.forEach(partyId -> {
                    putPushMsg(partyId, msgId + "");
                });
                Timer timer = new Timer(true);
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        doSendMsg(req, partyIds, msgId);
                    }
                };
                //延迟8秒后操作
                timer.schedule(task, 1000 * 8);
            } else {
                doSendMsg(req, partyIds, msgId);
            }
        } catch (Exception e) {
            logger.warn("jpush error ", e);
        }
        return true;
    }

    private void doSendMsg(MsgVo req, List<Long> partyIds, Long msgId) {
        try {
            req.setId(msgId);
            //保存用户和消息的关系
            List<Long> noPushUser = new LinkedList<>();
            if (CollectionUtils.isNotEmpty(partyIds)) {
                userDAO.selectUser(partyIds).forEach(user -> {
                    boolean flat = redisUtil.isRepeat(MSG_PRIFIX + user.getPartyId() + msgId, 30);
                    if (!flat) {
                        userMsgDAO.insert(UserMsg.builder().partyId(user.getPartyId()).pushSign(YesNo.N.name()).deviceId(user.getDeviceId()).msgId(msgId).build());
                    }
                });
                messagePushService.push(req);
            } else {
                Integer startPage = 1;
                Integer pageSize = 100;
                Map<String, Object> paras = DTOUtils.queryMap();
                paras.put("pageSize", pageSize);
                List<AppPush> list = appPushDAO.query(paras);
                while (list.size() > 0) {
                    batchInsert(list, req);
                    paras.put("startPage", ++startPage);
                    if (list.size() < pageSize) {
                        break;
                    }
                    list = appPushDAO.query(paras);
                }
                messagePushService.push(req);
            }
        } catch (Exception e) {
            logger.warn("jpush error ", e);
        }
    }

    /**
     * 批量插入用户-消息关系
     *
     * @param list
     */
    private void batchInsert(List<AppPush> list, MsgVo req) {
        UserMsg um;
        List<UserMsg> umList = new ArrayList<>(list.size());
        for (AppPush pack : list) {
            um = new UserMsg();
            um.setPartyId(pack.getPartyId());
            um.setDeviceId(pack.getDeviceId());
            um.setMsgId(req.getId());
            umList.add(um);
        }
        userMsgDAO.batchInsert(umList);
    }

    /**
     * @see com.xianglin.appserv.biz.shared.MessageManager#read(java.lang.Long, java.lang.Long)
     */
    @Override
    public MsgVo read(Long id, Long userPartyId) throws Exception {
        if (userPartyId == null) {
            throw new Exception("read msg userParyty can not be null");
        }
        MsgVo vo = DTOUtils.map(msgDAO.selectByPrimaryKey(id), MsgVo.class);
        Map<String, Object> paras = DTOUtils.queryMap();
        paras.put("partyId", userPartyId);
        paras.put("msgId", id);
        List<UserMsg> list = userMsgDAO.select(paras);
        if (CollectionUtils.isNotEmpty(list) && vo != null) {
            UserMsg um = list.get(0);
            vo.setMsgStatus(um.getStatus());
            vo.setPraiseSign(um.getPraiseSign());
            if (StringUtils.equals(um.getStatus(), Constant.MsgStatus.UNREAD.code)) {
                um.setStatus(Constant.MsgStatus.READED.code);
                userMsgDAO.updateByPrimaryKeySelective(um);
            }
        }
        return vo;
    }

    @Override
    public int read(Long partyId, String msgType) throws Exception {
        return userMsgDAO.readMsg(partyId, msgType);
    }

    /**
     * @see com.xianglin.appserv.biz.shared.MessageManager#praise(java.lang.Long, java.lang.Long)
     */
    @Override
    public Boolean praise(Long id, Long userPartyId) throws Exception {
        UserMsg um = new UserMsg();
        um.setPartyId(userPartyId);
        um.setMsgId(id);
        um.setStatus(Constant.MsgStatus.READED.code);
        um.setPraiseSign(Constant.YESNO.YES.code);
        if (userMsgDAO.updateByPrimaryKeySelective(um) == 1) {
            Msg msg = msgDAO.selectByPrimaryKey(id);
            if (msg != null) {
                msg.setPraises(msg.getPraises() + 1);
                msgDAO.updateByPrimaryKeySelective(msg);
            }
        }
        return true;
    }

    @Override
    public Boolean updatePushMsg(PushMsgCheckVo vo) {
        UserMsg um = new UserMsg();
        um.setMsgId(vo.getMsgId());
        um.setDeviceId(vo.getDeviceId());
        um.setPushSign(YESNO.YES.code);
        if (StringUtils.equals(vo.getPushStatus(), MsgPushStatus.CLICK.name())) {
            um.setClickSign(YESNO.YES.code);
        }
        userMsgDAO.updateByPrimaryKeySelective(um);
        return true;
    }

    public static void main(String[] args) throws APIConnectionException, APIRequestException {
        JPushClient jpushClient = new JPushClient("7d5bd11ee1134a2390f3acee", "b14e115eb1efda301fd22cc8");
        Map<String, String> extras = new HashMap<>();
        extras.put("type1", "web");
        extras.put("url1", "21321322");
//		Builder builder = PushPayload.newBuilder().setNotification(Notification.android("test", "ceshi", extras))
//				.setPlatform(Platform.android());
//				.setMessage(Message.newBuilder().setTitle("test123")
//						.addExtra("type", "web")
//						.addExtra("url",
//								"https://h5-dev.xianglin.cn/home/nodeManager/mesgTypeSystem.html?id=23&type=sys")
//						.addExtras(extras)
//						.build());// 自定义消息
//		builder.setAudience(Audience.all());
        Builder builder = PushPayload.newBuilder().setMessage(Message.newBuilder().setTitle(MsgType.ALARM.name()).setContentType(MsgType.OFFLINE.name()).setMsgContent("你的乡邻app账户于{时间}在别的设备登陆，你被迫下线，如果这不是你的操作，你的密码已经泄露，请检查手机是否被植入密码").build());
        builder.setPlatform(Platform.ios());
        builder.setOptions(Options.newBuilder().setApnsProduction(false).build());
        builder.setAudience(Audience.alias("b8740fce35fe482ab39947dabb6c782d"));
        PushPayload payload = builder.build();
        PushResult resp = jpushClient.sendPush(payload);
        System.out.println("msg: " + payload);
        System.out.println("resp: " + resp);
        System.out.println(MessageFormat.format("{0}", DateUtils.formatDate(new Date(), DateUtils.TIME_FMT)));
//		content=content.replaceAll("<.*?>",""); 
//		System.out.println(HtmlUtils.);
        String a = "<a>dfdafafda</a>";
        System.out.println(StringUtils.replacePattern(a, "<.*?>", ""));
        System.out.println(a.replaceAll("<.*?>", ""));
    }

    /**
     * @see com.xianglin.appserv.biz.shared.MessageManager#listNews(com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery)
     */
    @Override
    public List<MsgVo> listNews(MsgQuery req) {
        try {
            Map<String, Object> paras = DTOUtils.beanToMap(req);
            paras.put("msgType", MsgType.NEWS.name());
            if (StringUtils.isNotEmpty(req.getMsgType())) {
                paras.put("msgTag", req.getMsgType());
            }
            List<Msg> msgs = msgDAO.query(paras);
            return DTOUtils.map(msgs, MsgVo.class);
        } catch (Exception e) {
            throw new BusiException(e.getMessage());
        }
    }


    @Override
    public int queryMsgCount(Map<String, Object> paras) {
        return msgDAO.queryCount(paras);
    }

    /**
     * 查询设备关联的消息
     *
     * @param paras
     * @return
     */
    @Override
    public List<Msg> listDeviceNews(UserMsg paras, Page page) {
        return msgDAO.queryNews(paras, page);
    }

    @Override
    public List<Msg> recommendNews(String deviceid, List<String> msgTags, int pageSize) {
        List<Msg> list = msgDAO.queryRecommenNews(deviceid, msgTags, pageSize);
        User user = userDAO.selectByDeviceId(deviceid);
        UserMsg um = UserMsg.builder().deviceId(deviceid).build();
        if (user != null) {
            um.setPartyId(user.getPartyId());
        }
        for (Msg msg : list) {
            um.setMsgId(msg.getId());
            userMsgDAO.insert(um);
        }
        return list;
    }

    /**
     * 更新新闻状态
     *
     * @param paras
     * @return
     */
    @Override
    public boolean updateNews(UserMsg paras) {
        //1，更新用户操作数
        userMsgDAO.updateMsgOperNum(paras);
        //2，更新消息总的阅读数
        msgDAO.updateOperateNum(paras.getMsgId());
        return true;
    }

    @Override
    public List<MsgVo> queryMsgByparam(Map<String, Object> paras) {
        List<MsgVo> list = null;
        try {
            List<Msg> msgs = msgDAO.queryMsgByparam(paras);
            list = DTOUtils.map(msgs, MsgVo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<UserMsg> queryUserMsg(UserMsg um, Page page) {
        return userMsgDAO.selectList(um, page);
    }

    @Override
    public Msg queryById(Long id) {
        return msgDAO.selectByPrimaryKey(id);
    }

    @Override
    public List<Msg> queryPushMsg(final Long partyId) {
        return redisClient.execute((shardedJedis) -> {
            final String key = MSG_PRIFIX + partyId;
            Set<String> members = shardedJedis.smembers(key);
            List<Msg> msgs = null;
            if (CollectionUtils.isNotEmpty(members)) {
                User user = userDAO.selectByPartyId(partyId);
                msgs = members.parallelStream().map(s -> {
                    return msgDAO.selectByPrimaryKey(Long.valueOf(s));
                }).collect(Collectors.toList());
                msgs.forEach(msg -> {
                    //标记为已经推送
                    boolean flat = redisUtil.isRepeat(MSG_PRIFIX + user.getPartyId() + msg.getId(), 30);
                    if (!flat) {
                        userMsgDAO.insert(UserMsg.builder().partyId(partyId).msgId(msg.getId()).deviceId(user.getDeviceId()).pushSign(YesNo.Y.name()).build());
                    }
                });
                //删除已拉下来数据
                shardedJedis.srem(key, members.toArray(new String[members.size()]));
            }
            return msgs;
        });


    }

    /**消息存入缓存
     * @param partyId
     * @param msgId
     */
    public void putPushMsg(final Long partyId, final String msgId) {
        redisClient.execute((shardedJedis) -> {
            final String key = MSG_PRIFIX + partyId;
            shardedJedis.sadd(key, msgId);
            shardedJedis.expire(key, 60);//缓存1分钟
            return null;
        });
    }

}
