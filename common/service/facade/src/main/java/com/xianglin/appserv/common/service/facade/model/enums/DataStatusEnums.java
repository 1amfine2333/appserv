package com.xianglin.appserv.common.service.facade.model.enums;

public enum DataStatusEnums {
	/*you nete down*/
	Y ("Y","有效"),
	N ("N","失效")
	
	;
	
	private String code;
	private String message;
	
	DataStatusEnums(String code,String message){
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * 根据code获取message
	 * @author gengchaogang
	 * @dateTime 2015年12月8日 下午2:20:10
	 * @param code
	 * @return
	 */
	public static String getMessageByCode(String code){
		if(code == null){
			return null;
		}
		String result = null;
		for (DataStatusEnums llRespCodeEnum : values()) {
			if (code.equals(llRespCodeEnum.code)) {
				result = llRespCodeEnum.getMessage();
				break;
			}
		}
		return result;
	}

}
