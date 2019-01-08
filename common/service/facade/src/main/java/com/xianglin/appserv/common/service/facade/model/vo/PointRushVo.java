package com.xianglin.appserv.common.service.facade.model.vo;

public class PointRushVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3077720066100742706L;
	
	private String pointRush;
	
	/** 点击文字对应的链接 http,native  */
	private String pushActive;
	
	/** 过期时间 */
	private long expireTime;
	
	public String getPointRush() {
		return pointRush;
	}

	public void setPointRush(String pointRush) {
		this.pointRush = pointRush;
	}

	public String getPushActive() {
		return pushActive;
	}

	public void setPushActive(String pushActive) {
		this.pushActive = pushActive;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
}
