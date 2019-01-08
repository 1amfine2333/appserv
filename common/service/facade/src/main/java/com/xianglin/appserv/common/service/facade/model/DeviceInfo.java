/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;
import lombok.*;

/**
 * 设备信息
 * 
 * @author pengpeng 2016年1月25日下午2:38:37
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfo extends BaseVo {

	/** serialVersionUID */
	private static final long serialVersionUID = -1046045783811801867L;

	/** deviceId */
	private String deviceId;

	/** 设备平台 DevicePlatform枚举 iPhone，iPad，iMac，Android，Windows等 */
	private String platform;

	/** 手机品牌 */
	private String deviceBrand;

	/** 手机机型 */
	private String deviceModel;

	/** 操作系统类型 SystemTypeEnum ANDROID,iOS,WINDOWS */
	private String systemType;

	/** 操作系统版本 如ios11 */
	private String systemVersion;

	/** 屏幕宽度（像素） */
	private Integer screenWidth;

	/** 屏幕高度（像素） */
	private Integer screenHight;

	/** WIFI的MAC地址 */
	private String wifiMac;

	/** 蓝牙的MAC地址 */
	private String bluetoothMac;
	
	private String androidId;
	
	private String cpu;

	/** IMEI串号 */
	private String imei;

	/** IMSI串号 */
	private String imsi;

	/** 总内存数量（M） */
	private Integer totalMemory;

	/** CPU核心数 */
	private Integer cpuCoreCount;

	/** CPU频率 */
	private Integer cpuFrequency;

	/** ISO设备信息 */
	private String iosInfo;
	
	/** iOSToken */
	private String iosToken;

	/**
	 * 来源渠道名
	 */
	private String channel;

	/**
	 * 设备名 如xiaom6
	 */
	private String deviceName;
}
