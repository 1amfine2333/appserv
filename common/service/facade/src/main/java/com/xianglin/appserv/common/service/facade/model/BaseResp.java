package com.xianglin.appserv.common.service.facade.model;

import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.exception.MessageException;
import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

public class BaseResp extends BaseVo {

	private static final long serialVersionUID = -6892882644985304286L;

	private int code = FacadeEnums.OK.code;

	private String msg = FacadeEnums.OK.msg;

	// 错误信息
	private String memo = "";

	// 给客户端显示给用户的提示弹出框
	private String tips = FacadeEnums.OK.tip;

	// Header header;

	private String operateType;

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the tips
	 */
	public String getTips() {
		return tips;
	}

	/**
	 * @param tips
	 *            the tips to set
	 */
	public void setTips(String tips) {
		this.tips = tips;
	}

	/**
	 * @return the operateType
	 */
	public String getOperateType() {
		return operateType;
	}

	/**
	 * @param operateType
	 *            the operateType to set
	 */
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public int getCode() {

		return code;
	}

	public void setCode(int code) {

		this.code = code;
	}

	public String getMsg() {

		return msg;
	}

	public void setMsg(String msg) {

		this.msg = msg;
	}

	public void setBaseResp(FacadeEnums facade) {
		this.code = facade.getCode();
		this.msg = facade.getMsg();
		this.tips = facade.getTip();
	}

	/**
	 * check resp result, must be OK(200000), otherwise throw MessageException
	 * 
	 * @throws MessageException
	 */
	public void check() throws MessageException {
		if (this.getCode() != FacadeEnums.OK.code) {
			throw new MessageException(this);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BaseResp [code=" + code + ", msg=" + msg + ", memo=" + memo + ", tips=" + tips + ", operateType="
				+ operateType + "]";
	}
}
