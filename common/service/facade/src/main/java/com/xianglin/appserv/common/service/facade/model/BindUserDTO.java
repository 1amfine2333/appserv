package com.xianglin.appserv.common.service.facade.model;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

public class BindUserDTO extends BaseVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5059680459468488510L;
	private String mobilePhone;
	private String smsCode;

	private String newMobilePhone;
	private String newSmsCode;

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getNewMobilePhone() {
		return newMobilePhone;
	}

	public void setNewMobilePhone(String newMobilePhone) {
		this.newMobilePhone = newMobilePhone;
	}

	public String getNewSmsCode() {
		return newSmsCode;
	}

	public void setNewSmsCode(String newSmsCode) {
		this.newSmsCode = newSmsCode;
	}

}
