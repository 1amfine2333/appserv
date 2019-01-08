/**
 * 
 */
package com.xianglin.appserv.biz.shared.push;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgType;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.util.DateUtils;

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
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;


/**
 * JPush 实现
 * 
 * @author wanglei 2016年9月26日下午3:17:02
 */
@Service("jpush")
public class JPushPusherImpl extends MessagePusherAdapter implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(JPushPusherImpl.class);

	private JPushClient jpushClient;
	
	@Value("#{config['jpushMasterSecret']}")
	private String jpushMasterSecret;

	@Value("#{config['jpushAppKey']}")
	private String jpushAppKey;
	
	@Value("#{config['MESSAGE_DETAIL_URL']}")
	private String messageDetial;
	
	@Value("#{config['env']}")
	private String env;
	
	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	/**
	 * @see com.xianglin.appserv.biz.shared.push.MessagePusher#init()
	 */
	@Override
	public void init() {
		if (jpushClient == null) {
			logger.debug("init jpush client jpushMasterSecret:{},jpushAppKey:{}",jpushMasterSecret,jpushAppKey);
			jpushClient = new JPushClient(jpushMasterSecret, jpushAppKey);
			logger.debug("init jpush client finish..");
		}
	}

	/**
	 * @see com.xianglin.appserv.biz.shared.push.MessagePusher#push(com.xianglin.appserv.common.service.facade.model.vo.MsgVo,java.util.Set)
	 */
	@Override
	public void push(MsgVo msg,Set<String> tockens) {
		try {
			logger.info("jpush push msg {} tockens :{}",msg,ArrayUtils.toArray(tockens));
			Map<String, String> extras = new HashMap<>();
			extras.put("type", "web");
			extras.put("url", msg.getUrl()+"");
			extras.put("partyId", msg.getPartyId()+"");
			extras.put("title", msg.getMsgTitle()+"");
			extras.put("msgType", msg.getMsgType()+"");
			extras.put("content-available", "1");
			extras.put("msgTime", System.currentTimeMillis()+"");
			extras.put("expiryTime", msg.getExpiryTime()+"");//过期时间
			extras.put("loginCheck", msg.getLoginCheck()+"");//是否需要登录
			extras.put("msgId", msg.getId()+"");
			extras.put("msgSource",msg.getMsgSource()+"");
			
			if(CollectionUtils.isNotEmpty(tockens)){
				Builder builder = null;
				builder = PushPayload.newBuilder().setMessage(Message.newBuilder().setTitle(msg.getMsgTitle()).setContentType(msg.getMsgType()).setMsgContent(StringUtils.substring(StringUtils.replacePattern(msg.getMessage(), "<.*?>", ""), 0, 125)).addExtras(extras).build());
				builder.setNotification(Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder().setAlert(StringUtils.substring(msg.getMsgTitle(), 0, 30)+"\n"+ StringUtils.substring(StringUtils.replacePattern(msg.getMessage(), "<.*?>", ""), 0, 130)).addExtras(extras).build()).build());
				builder.setPlatform(Platform.android_ios());
				builder.setAudience(Audience.alias(tockens));
				builder.setOptions(Options.newBuilder().setTimeToLive(0).build());
				logger.info("jpush send msg {}",builder.build());
				if(jpushClient == null){
					init();
				}
				PushResult pushResult = jpushClient.sendPush(builder.build());
				logger.info("jpush send msg {} result{} ",builder,pushResult);
			}
		} catch (Throwable e) {
			logger.warn("",e);
		}
	}

	@Override
	public void push(MsgVo msg, Set<String> tockens, String version) {
		logger.debug("{} jpush push msg {} tockens :{}",version,msg,ArrayUtils.toArray(tockens));
		Builder builder = null;
		try {
			if(StringUtils.equals(msg.getMsgType(), MsgType.OFFLINE.name())){
				builder = PushPayload.newBuilder().setMessage(Message.newBuilder().setTitle(msg.getMsgTitle()).setContentType(msg.getMsgType()).setMsgContent(msg.getMessage().replaceAll("<.*?>", "")).build());
			}else{
				Map<String, String> extras = new HashMap<>();
				extras.put("type", "web");
				extras.put("url", msg.getUrl());
				extras.put("partyId", msg.getPartyId()+"");
				builder = PushPayload.newBuilder().setNotification(Notification.android(StringUtils.replacePattern(msg.getMessage(), "<.*?>", ""),msg.getMsgTitle(), extras));
			}
			builder.setPlatform(Platform.android());
			builder.setAudience(Audience.alias(tockens));
			builder.setOptions(Options.newBuilder().setTimeToLive(0).build());
			PushResult result = jpushClient.sendPush(builder.build());
			logger.info("{} jpush send msg {} result {}",version,builder,result);
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}
	
	public static void main(String[] args) throws APIConnectionException, APIRequestException {
		JPushClient jpushClient = new JPushClient("7d5bd11ee1134a2390f3acee", "b14e115eb1efda301fd22cc8");
		MsgVo msg = new MsgVo();
		msg.setMsgTitle("test");
		msg.setMsgType("OFFLINE");
		msg.setMessage("test,test,test,test,test,test,test,test,test,test");
		Map<String, String> extras = new HashMap<>();
		extras.put("type1", "web");
		extras.put("url1", "21321322");
		extras.put("url111", "");
		Builder builder = PushPayload.newBuilder().setMessage(Message.newBuilder().setTitle(msg.getMsgTitle()).setContentType(msg.getMsgType()).setMsgContent(StringUtils.substring(StringUtils.replacePattern(msg.getMessage(), "<.*?>", ""), 0, 125)).addExtras(extras).build());
		builder.setNotification(Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder().setAlert(msg.getMsgTitle()).addExtras(extras).build()).build());
		builder.setPlatform(Platform.android_ios());
		builder.setOptions(Options.newBuilder().setApnsProduction(false).build());
		builder.setAudience(Audience.alias("6acd4610f84a429380367fc4231e7bbe"));
		PushPayload payload = builder.build();
		PushResult resp = jpushClient.sendPush(payload);
		System.out.println("msg: "+payload);
		System.out.println("resp: "+resp);
	}
}
