/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model.enums;

/**
 * 响应结果枚举
 * 
 * @author pengpeng 2016年3月14日下午4:30:05
 */
@Deprecated
public enum ResponseEnum {

	/** 二维码已过期 */
	QRCODE_TIMEOUT(10001, "QRCodeTimeout", "二维码已过期！"),

	/** 二维码token生成失败 */
	TOKEN_CREATE_FAIL(10002, "TokenCreateFail", "抱歉，暂时无法操作，请稍后再试。"),

	// RETURN_EMPTY(400000,"empty","返回为空"),

	DATA_ERROR(500000, "程序异常，请稍后重试", "抱歉，暂时无法操作，请稍后重试"),

	RED_PACKET_EMPTY(20001, "很抱歉，今日活动已经结束哦", "很抱歉，今日活动已经结束哦"), RED_PACKET_NOT_START(20002, "活动还未开始，请耐心等待哦", "活动还未开始，请耐心等待哦"), RED_PACKET_PARTICIPATE(20003, "今日您已抢过了 ", "今日您已经抢过了"),

	RED_PACKET_ERROR(20004, "抢的人实在太多了，请您再接再厉哦！", "抢的人实在太多了，请您再接再厉哦！"),

	SESSION_INVALD(500004, "用户未登录", "用户未登录"), PARAM_INVALD(111111, "参数非法", "参数非法"), BUSI_INVALD(222222, "业务异常", "业务异常"),

	DELETE_ERROR(500008, "无权限删除动态", "无权限删除动态"),

	SYSTEM_EXCEPTION(999999, "系统异常", "系统繁忙，请稍后再试"),

	ACCOUNT_ERROR(500001, "帐号出现异常", "帐号出现异常"), APPSERV_ERROR_002(500001, "活动已经过期", "活动已经过期"), BUSI_INVALD_LUCKWHEEL_NO(20005, "今天您的幸运大转盘机会已经用完了。 明天再来吧！", "今天您的幸运大转盘机会已经用完了。 明天再来吧！"), BUSI_INVALD_LUCKWHEEL_SHARE(
			20006, "今天您的幸运大转盘机会已经用完了。 分享给好友，立刻获取一次机会。", "今天您的幸运大转盘机会已经用完了。 分享给好友，立刻获取一次机会。")

	;

	/** 结果码 */
	private int code;

	/** 结果描述 */
	private String memo;

	/** 用户友好提示 */
	private String tips;

	/**
	 * @param code
	 * @param memo
	 * @param tips
	 */
	private ResponseEnum(int code, String memo, String tips) {
		this.code = code;
		this.memo = memo;
		this.tips = tips;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @return the tips
	 */
	public String getTips() {
		return tips;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

}
