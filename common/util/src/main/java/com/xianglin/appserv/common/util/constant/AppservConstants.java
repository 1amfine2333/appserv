/**
 * 
 */
package com.xianglin.appserv.common.util.constant;

/**
 * 
 * 
 * @author Yao 2016年3月17日上午11:03:39
 */
public interface AppservConstants {

	/** 未执行 */
	public String STATUS_UNEXECUTED = "0";
	/** 执行中 */
	public String STATUS_EXECUTING = "1";
	/** 执行完成 */
	public String STATUS_EXECUTED = "2";
	/** 子任务一次查询记录上限值 */
	public long  LIMIT = 1000L;

	
	public enum LoginError{
		MOBILE_BIND_MORE(600000,"该手机已在多个帐号，请联系管理人员处理"),
		NOT_BIND_MOBILE(600001,"请使用乡邻小站绑定站长手机号登录"),
		INVALID_SMS_CODE(600002,"手机号和验证码不匹配"),
		INVALID_INPUT_ACCOUNT(600003,"请输入登录账号"),
		;
		public int code;
		public String desc;
		
		LoginError(int code,String desc){
			this.code = code;
			this.desc = desc;
		}
	}
}
