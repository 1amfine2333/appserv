package com.xianglin.appserv.common.dal.dataobject;

import java.util.Date;

public class AppInstall {
    private Long id;

    private Long partyId;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 应用包名
     */
    private String appPackage;
    /**
     * 应用权限
     */
    private String appPrivilege;
    /**
     * 应用安装时间
     */
    private Date appInstallDate;
    /**
     * 
     */
    private String isDeleted;
    /**
     * 应用版本
     */
    private String appVersion;
    /**
     * 开发者
     */
    private String appDev;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    private String appIcon;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPartyId() {
		return partyId;
	}

	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public String getAppPrivilege() {
		return appPrivilege;
	}

	public void setAppPrivilege(String appPrivilege) {
		this.appPrivilege = appPrivilege;
	}

	public Date getAppInstallDate() {
		return appInstallDate;
	}

	public void setAppInstallDate(Date appInstallDate) {
		this.appInstallDate = appInstallDate;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppDev() {
		return appDev;
	}

	public void setAppDev(String appDev) {
		this.appDev = appDev;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}
    

}