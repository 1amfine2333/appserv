/**
 * 
 */
package com.xianglin.appserv.common.dal.dataobject;

import java.math.BigDecimal;

/**
 * 地理位置信息DO
 * 
 * @author pengpeng 2016年2月24日下午6:05:59
 */
public class LocationInfoDO extends BaseDO {

	/** 用户唯一标识 */
	private String userId;

	/** 设备唯一标识 */
	private String deviceId;

	/** 地理位置信息描述 */
	private String position;

	/** 经度 */
	private BigDecimal longitude;

	/** 纬度 */
	private BigDecimal latitude;

	/** 12位字符长度的geohash */
	private String geohash;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the longitude
	 */
	public BigDecimal getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public BigDecimal getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the geohash
	 */
	public String getGeohash() {
		return geohash;
	}

	/**
	 * @param geohash
	 *            the geohash to set
	 */
	public void setGeohash(String geohash) {
		this.geohash = geohash;
	}

}
