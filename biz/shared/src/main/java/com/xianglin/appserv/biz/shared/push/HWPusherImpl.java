/**
 * 
 */
package com.xianglin.appserv.biz.shared.push;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.xianglin.appserv.common.util.HttpUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.notnoop.apns.ApnsServiceBuilder;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgType;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;

/**
 * JPush 实现
 * 
 * @author wanglei 2016年9月26日下午3:17:02
 */
@Service("hwPush")
public class HWPusherImpl extends MessagePusherAdapter implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(HWPusherImpl.class);
	
	private String appKey = "10680497";
	
	private String masterSecret = "0eee42a1e0cea801778ca9d1ec295dcc";
	
	private String accessToken;
	
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
		InputStream certFile = null;
		ApnsServiceBuilder builder = null;
		try {
			logger.debug("init hwPush client masterSecret:{},appKey:{}", masterSecret, appKey);
			Map<String, String> para = new HashMap<>();
			para.put("grant_type", "client_credentials");
			para.put("client_id", "10680497");
			para.put("client_secret", "0eee42a1e0cea801778ca9d1ec295dcc");
			String resp = HttpUtils.executePost("https://login.vmall.com/oauth2/token", para);
					System.out.println(resp);
			com.alibaba.fastjson.JSONObject obj = JSON.parseObject(resp);
			accessToken = obj.getString("access_token");
			logger.debug("hwPush jpush client accessToken:{}", accessToken);
		} catch (Throwable e) {
			logger.debug(e.getMessage());
		}
	}

	/**
	 * @see com.xianglin.appserv.biz.shared.push.MessagePusher#push(com.xianglin.appserv.common.service.facade.model.vo.MsgVo, java.util.List)
	 */
	@Override
	public void push(MsgVo msg, Set<String> tockens) {
		try{
			String msgUrl = messageDetial + "?id=" + msg.getId() + "&type=";
			if (StringUtils.equals(msg.getMsgType(), MsgType.ARTICLE.name())) {
				msgUrl += "other";
			} else {
				msgUrl += "sys";
			}
			JSONObject obj = new JSONObject();
			obj.put("notification_title", msg.getMsgTitle());
			obj.put("notification_content", StringUtils.substring(StringUtils.replacePattern(msg.getMessage(), "<.*?>", ""), 0, 25));
			obj.put("type", "web");
			obj.put("url", msg.getUrl());
			obj.put("partyId", msg.getPartyId()+"");
			obj.put("title", msg.getMsgTitle());
			obj.put("msgType", msg.getMsgType());
			obj.put("doings", 1);
			obj.put("msgTime", System.currentTimeMillis());
			obj.put("expiryTime", msg.getExpiryTime()+"");//过期时间
			obj.put("loginCheck", msg.getLoginCheck());//是否需要登录
			obj.put("msgId", msg.getId()+"");
			
			Map<String, String> para = new HashMap<>();
			para.put("dev_app_id", appKey);
			para.put("push_type", "1");
			para.put("access_token", accessToken);
			para.put("message", obj.toJSONString()); 
			para.put("nsp_fmt", "JSON");
			para.put("deviceTokenList", toTokens(tockens));
			para.put("cacheMode", "1");
			para.put("msgType", "1");
			para.put("nsp_svc", "openpush.message.batch_send");
			String resp = HttpUtils.executePost("https://api.vmall.com/rest.php", para);
			logger.debug("HwPush resp {}",resp);
			if(!StringUtils.contains(resp, "success")){
				init();
			}
			logger.debug("hwPush send result {}",ArrayUtils.toArray(resp));
		}catch(Throwable e){
			logger.debug(e.getMessage());
		}
	}
	
	public static void main(String[] args) throws Exception {
		Map<String, Object> para = new HashMap<>();
		para.put("grant_type", "client_credentials");
		para.put("client_id", "10680497");
		para.put("client_secret", "0eee42a1e0cea801778ca9d1ec295dcc");
//		String resp = HttpUtils.executePost("https://login.vmall.com/oauth2/token", para);
//				System.out.println(resp);
//		com.alibaba.fastjson.JSONObject obj = JSON.parseObject(resp);
//		String key = obj.getString("access_token");
//		System.out.println(key);
		
		String token = "CFgJyXiuSIqX6U2aMtkFNsam9DnAztFuPTWM7DWkadEXQSZddCy0xYRBB084yKhL2w6D7tenWkdwu0oduaiOsw==";
//		token = "CFgIpO1S0jNQo5FfSgLaX8FpdDDr/855nTEYxAsFBWcJnx0NNZULrQQTc9MuCM5c7oGV4MrSBhZi1x6q+O2wxA==";
		para.clear();
		
		JSONObject obj = new JSONObject();
		obj.put("notification_title", "测试消息11111");
		obj.put("notification_content", "");
		obj.put("type", "web");
		obj.put("url", "https://h5-dev.xianglin.cn/home/nodeManager/mesgTypeSystem.html?type=other&id=847");
		obj.put("partyId", "7000174");
		obj.put("title", "测试消息11111");
		obj.put("msgType", "ARTICLE");
		obj.put("doings", 1);
		obj.put("msgTime", System.currentTimeMillis());
		
		
		Set<String> tockens = new HashSet<>();
		tockens.add("08667890253488802000003649000001");
//		tockens.add("08667890253488802000003649000002");
		para.put("dev_app_id", "10680497");
		para.put("push_type", "1");
		para.put("access_token", token);
//		para.put("tokens", toTokens(tockens));
//		para.put("android", obj.toJSONString());
		para.put("nsp_fmt", "JSON");
//		para.put("nsp_svc", "openpush.message.single_send");
		
//		para.put("deviceTokenList", new String[]{"08667890253488802000003649000001","08667890253488802000003649000002"});
		para.put("deviceTokenList",toTokens(tockens));
//		para.put("deviceTokenList","08667890253488802000003649000002,08667890253488802000003649000002");
//		para.put("deviceTokenList","[\"08667890253488802000003649000001\",\"08667890253488802000003649000002\"]");
//		para.put("deviceTokenList","231232131221124122132133");
//		para.put("deviceToken", "08656200222931862000003649000001");
		para.put("nsp_svc", "openpush.message.batch_send");
//		para.put("nsp_svc", "openpush.message.single_send");
		para.put("message", obj.toJSONString());
		para.put("cacheMode", "1");
		para.put("msgType", "1");
		para.put("nsp_fmt", "JSON");

		
		String resp = HttpUtils.executePost("https://api.vmall.com/rest.php", para);
		System.out.println(resp);
//		System.out.println(StringUtils.contains(resp, ":0,"));
//		System.out.println(StringUtils.substring(resp, 1, resp.length()-1));
//		Object res = JSON.parseObject(StringUtils.substring(resp, 1, resp.length()-1), HashMap.class);
//		System.out.println(res.getClass().getSimpleName());
//		if(StringUtils.equals("0", res.getString("request_id"))){
//			System.out.println(111111);
//		}
		
//		String resp = HttpUtils.executeGet("http://appfile-dev.xianglin.cn/file/2");
//		System.out.println(resp.length());
	}
	
	public static String toTokens(Set<String> tockens){
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(String s:tockens){
			sb.append("\"").append(s).append("\"").append(",");
		}
		return sb.substring(0, sb.length()-1) + "]";
	}
}
