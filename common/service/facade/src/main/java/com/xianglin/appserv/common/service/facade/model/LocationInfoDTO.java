/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;

/**
 * 地理位置信息
 * 
 * @author pengpeng 2016年2月18日上午10:34:39
 */
public class LocationInfoDTO implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -2261944469557959948L;

	/** 地理位置信息描述 */
	private String position;

	/** 经度 */
	private double longitude;

	/** 纬度 */
	private double latitude;

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
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LocationInfo [position=" + position + ", longitude=" + longitude + ", latitude=" + latitude + "]";
	}

}
