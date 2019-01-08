package com.xianglin.appserv.common.service.facade.model.exception;

import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;

public class BusiException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4221989094632363073L;

	private ResponseEnum responseEnum;

	private FacadeEnums facadeEnums;

	@Deprecated
	public BusiException(ResponseEnum responseEnum) {
		super(responseEnum.getMemo());
		this.responseEnum = responseEnum;
	}

	@Deprecated
	public BusiException(ResponseEnum responseEnum,String msg) {
		super(msg);
		this.responseEnum = responseEnum;
		this.responseEnum.setMemo(msg);
		this.responseEnum.setTips(msg);
	}

	public BusiException(FacadeEnums facadeEnums) {
		super(facadeEnums.getTip());
		this.facadeEnums = facadeEnums;
	}

	public BusiException(FacadeEnums facadeEnums,String msg) {
		super(msg);
		this.facadeEnums = facadeEnums;
		this.facadeEnums.setTip(msg);
		this.facadeEnums.setMsg(msg);
	}

	public BusiException(String msg) {
		super(msg);
		this.facadeEnums = FacadeEnums.E_C_VALDATE_PROPERTY_VAL;
		this.facadeEnums.setTip(msg);
		this.facadeEnums.setMsg(msg);
	}

	public FacadeEnums getFacadeEnums() {
		return facadeEnums;
	}

	public void setFacadeEnums(FacadeEnums facadeEnums) {
		this.facadeEnums = facadeEnums;
	}

	@Deprecated
	public ResponseEnum getResponseEnum() {
		return responseEnum;
	}

	@Deprecated
	public void setResponseEnum(ResponseEnum responseEnum) {
		this.responseEnum = responseEnum;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this.facadeEnums, ToStringStyle.JSON_STYLE);
	}
}
