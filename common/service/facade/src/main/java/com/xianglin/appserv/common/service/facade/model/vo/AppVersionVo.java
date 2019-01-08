/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model.vo;

/**
 * 
 * 
 * @author wanglei 2016年8月26日下午4:12:50
 */
public class AppVersionVo extends BaseVo {

	/**  */
	private static final long serialVersionUID = -328710710896553561L;

	private String version;
	
	private String downloadURL;
	
	private String desc;
	
	private Integer versionCode;

	/** 更新基本 0，提示更新，1 强制更新 */
	private String updateLevel;
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUpdateLevel() {
		return updateLevel;
	}

	public void setUpdateLevel(String updateLevel) {
		this.updateLevel = updateLevel;
	}

	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
}
