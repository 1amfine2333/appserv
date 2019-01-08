/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model.vo;

/**
 * 
 * 
 * @author wanglei 2016年10月20日下午3:37:17
 */
public class PushMsgCheckVo extends BaseVo {

	/** 消息Id */
	private Long msgId;
	
	private String deviceId;
	
	/** 消息类型，CLICK RECEIVE */
	private String pushStatus;

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(String pushStatus) {
		this.pushStatus = pushStatus;
	}
}
