/**
 * 
 */
package com.xianglin.appserv.biz.shared.push;

import java.util.ArrayList;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgType;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Feedback;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Message.Builder;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

/**
 * MiPush 实现
 * 
 * @author wanglei 2016年9月26日下午3:17:02
 */
@Service("miPush")
public class MiPusherImpl extends MessagePusherAdapter implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(MiPusherImpl.class);

	private Sender sender;

	private String masterSecret = "ClqCNGtAOP2lzdG6e/F9Yg==";

	private String appId = "2882303761517540722";
	
	private String appKey = "5561754048722";
	
	private String appPackage = "com.xianglin.app";
	
	@Value("#{config['MESSAGE_DETAIL_URL']}")
	private String messageDetial;

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
		try {
			logger.debug("init miPush client masterSecret:{},appKey:{}", masterSecret, appKey);
			Constants.useOfficial();
			sender = new Sender(masterSecret);
		} catch (Throwable e) {
			logger.debug(e.getMessage());
		}
	}

	/**
	 * @see com.xianglin.appserv.biz.shared.push.MessagePusher#push(com.xianglin.appserv.common.service.facade.model.vo.MsgVo,
	 *      java.util.List)
	 */
	@Override
	public void push(MsgVo msg, Set<String> tockens) {
		try {
			Builder builder = new Message.Builder()
	                .title(msg.getMsgTitle())
	                .description(StringUtils.substring(StringUtils.replacePattern(msg.getMessage(), "<.*?>", ""), 0, 125))
	                .extra("type", "web").extra("url", msg.getUrl()).extra("partyId", msg.getPartyId() + "").extra("msgType", msg.getMsgType())
	                .extra("msgTime", System.currentTimeMillis()+"")
	                .extra("expiryTime", msg.getExpiryTime()+"").extra("loginCheck", msg.getLoginCheck()).extra("msgId", msg.getId()+"")
	                .restrictedPackageName(appPackage)
	                .notifyType(1)     // 使用默认提示音提示
	                .timeToLive(2*24*3600*1000);
//	                .passThrough(0)//通知消息
			if(StringUtils.equals(MsgType.OFFLINE.name(), msg.getMsgType())){
				builder.passThrough(0);//透传消息
			}else{
				builder.passThrough(0);//通知消息
			}
			Result resp = sender.sendToAliasNoRetry(builder.build(), new ArrayList<>(tockens));
			logger.debug("miPush send result {}", resp);
		} catch (Throwable e) {
			logger.debug(e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
//		Constants.useOfficial();
//		Sender sender = new Sender("8NkfDxYG1jTGUAhWxWS1oQ==");
//		Message message = new Message.Builder()
//	                .title("test")
//	                .description("321321").payload("dfadfdsaf")
//	                .restrictedPackageName("com.xianglin.app")
//	                .notifyType(1)     // 使用默认提示音提示
//	                .build();
//		sender.broadcastAll(message, 0);
		
		Feedback fb = new Feedback("8NkfDxYG1jTGUAhWxWS1oQ==");
		String result = fb.getInvalidRegIds(10);
		System.out.println(result);
	}
}
