package com.xianglin.appserv.core.service.enums;

import com.xianglin.appserv.common.util.SysConfigUtil;

public enum ImgBusiType {
	USER_HEAD_IMG("USER_HEAD_IMG", SysConfigUtil.getStr("IMG_USER_HEAD_PATH"), "个人头像");

	private String code;//图片上传业务码
	private String path;//图片存储路径
	private String msg;//业务说明

	private ImgBusiType(String code, String path, String msg) {
		this.code = code;
		this.path = path;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 根据CODE查询MSG
	 * 
	 * @param code
	 *            枚举编码
	 * @return 枚举值
	 */
	public static String getMsgByCode(String code) {
		for (ImgBusiType imgTypeEnum : values()) {
			if (imgTypeEnum.code.equals(code))
				return imgTypeEnum.getMsg();
		}
		return null;
	}

	public static ImgBusiType getEnumByCode(String code) {
		for (ImgBusiType imgTypeEnum : values()) {
			if (imgTypeEnum.code.equals(code))
				return imgTypeEnum;
		}
		return null;
	}
}
