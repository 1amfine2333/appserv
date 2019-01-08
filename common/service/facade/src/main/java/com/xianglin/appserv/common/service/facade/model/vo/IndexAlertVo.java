package com.xianglin.appserv.common.service.facade.model.vo;

/**
 * Created by wanglei on 2017/5/2.
 */
public class IndexAlertVo extends BaseVo {

	/**
	 * 红包雨活动提示框
	 */
	private String shareAlert;

	/**
	 * 乡邻介绍查询
	 */
	private String companyIntroduce;

	/**
	 * 红包雨活动分享提示框
	 */
	private WechatShareInfo shareInfo;

	/**
	 * 用户注册奖励金币数
	 * 为0则不显示
	 */
	private Integer registerGoldcoin;

	/**
	 *当日签到金币奖励
	 * 为0则不提示
	 */
	private Integer signGoldcoin;

	public String getCompanyIntroduce() {
		return companyIntroduce;
	}

	public void setCompanyIntroduce(String companyIntroduce) {
		this.companyIntroduce = companyIntroduce;
	}

	public String getShareAlert() {
		return shareAlert;
	}

	public void setShareAlert(String shareAlert) {
		this.shareAlert = shareAlert;
	}

	public WechatShareInfo getShareInfo() {
		return shareInfo;
	}

	public void setShareInfo(WechatShareInfo shareInfo) {
		this.shareInfo = shareInfo;
	}

	public Integer getRegisterGoldcoin() {
		return registerGoldcoin;
	}

	public void setRegisterGoldcoin(Integer registerGoldcoin) {
		this.registerGoldcoin = registerGoldcoin;
	}

	public Integer getSignGoldcoin() {
		return signGoldcoin;
	}

	public void setSignGoldcoin(Integer signGoldcoin) {
		this.signGoldcoin = signGoldcoin;
	}
}
