/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model.enums;

/**
 * 月度统计类型
 * 
 * @author wanglei 2016年8月12日下午3:39:03
 */
public enum MonthAchieveType {
	
	INCOME("收益"),
	
	ACHIEVE("业绩"),
	
	/* 以上为大类别，以下为小类别 */
	
	BANK("银行"),
	
	ESHOP("电商"),
	
	LOAN("贷款"),
	
	PAY("水电煤缴费"),
	
	ERCHARGE("手机充值"),
	;
	
	private String msg;

	private MonthAchieveType(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
