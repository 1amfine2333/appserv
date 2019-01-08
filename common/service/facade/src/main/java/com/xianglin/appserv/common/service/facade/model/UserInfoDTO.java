package com.xianglin.appserv.common.service.facade.model;

import java.util.Date;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

public class UserInfoDTO extends BaseVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6945158899427932763L;
	/**
	 * 图片业务类型
	 */
	private String imgBusiType;
	/**
	 * base64压缩过的图像字符串
	 */
	private String base64Img;

	private Long id;

	private String loginName;

	private Long partyId;

	private String userType;

	private String deviceId;

	private String sessionId;

	private String trueName;
	/**
	 * 昵称
	 */
	private String nikerName;
	/**
	 * 头像
	 */
	private String headImg;
	/**
	 * 一句话介绍
	 */
	private String introduce;

	private String descs;

	private String status;

	private String isDeleted;

	private Date createTime;

	private Date updateTime;
	/**
	 * 站点名称(session中有)
	 */
	private String nodeName;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 身份证号码
	 */
	private String idNumber;

	/**
	 * 粉丝数
	 */
	private Integer fansNumber;

	/**
	 * 关注数
	 */
	private Integer followsNumber;

	/**
	 * 是否关注
	 */
	private String bothStatus;
	
	/**
	 * 是否认证
	 */
	private Boolean isAuth=false;

	public String getImgBusiType() {
		return imgBusiType;
	}

	public void setImgBusiType(String imgBusiType) {
		this.imgBusiType = imgBusiType;
	}

	public String getBase64Img() {
		return base64Img;
	}

	public void setBase64Img(String base64Img) {
		this.base64Img = base64Img;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Long getPartyId() {
		return partyId;
	}

	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getNikerName() {
		return nikerName;
	}

	public void setNikerName(String nikerName) {
		this.nikerName = nikerName;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
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

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public Integer getFansNumber() {
		return fansNumber;
	}

	public void setFansNumber(Integer fansNumber) {
		this.fansNumber = fansNumber;
	}

	public Integer getFollowsNumber() {
		return followsNumber;
	}

	public void setFollowsNumber(Integer followsNumber) {
		this.followsNumber = followsNumber;
	}

	public String getBothStatus() {
		return bothStatus;
	}

	public void setBothStatus(String bothStatus) {
		this.bothStatus = bothStatus;
	}

	public Boolean getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(Boolean isAuth) {
		this.isAuth = isAuth;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public Boolean getAuth() {
		return isAuth;
	}

	public void setAuth(Boolean auth) {
		isAuth = auth;
	}
}
