/**
 * 
 */
package com.xianglin.appserv;

import com.xianglin.appserv.common.util.PropertiesUtil;

import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.SMS;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

/**
 * 
 * 
 * @author zhangyong 2016年8月31日下午4:48:38
 */
public class JPushClient {

	static cn.jpush.api.JPushClient client;
	public  static void initClient(){
		
		String appkey = PropertiesUtil.getProperty("jpushAppKey");
		String secret = PropertiesUtil.getProperty("jpushMasterSecret");
		
		System.out.println("appkey="+appkey);
		System.out.println("secret="+secret);
		client = new cn.jpush.api.JPushClient(secret, appkey);
	
	}
	/**
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		initClient();
		try {
			PushPayload payload = buildPushObject_all_alias_alert();
			System.out.println(payload.toString());
			client.sendPush(payload);
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIRequestException e) {
			e.printStackTrace();
		}
	}
/*	 public static PushPayload buildPushObject_all_all_alert() {
	        return PushPayload.alertAll(ALERT);
	    }*/
	
	
	public static PushPayload buildPushObject_all_alias_alert() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias("cb22bc181b1a4167a857cfdd73764cab"))
                .setNotification(Notification.alert("8912"))
                //.setSMS(SMS.content("sms", 1000))
                .setMessage(Message.content("wishichi"))
                .build();
	}
}
