/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model.exception;

import com.xianglin.appserv.common.service.facade.model.BaseResp;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;

/**
 * message exception contains code and message
 * 
 * @author Yaen 2016年6月7日上午11:35:06
 */
public class MessageException extends Exception {
	private static final long serialVersionUID = -7594144199136491498L;

	/** code */
	private int code;

	public int getCode() {
		return code;
	}

	/**
	 * constructor
	 * 
	 * @param code
	 * @param message
	 */
	public MessageException(int code, String message) {
		super(message);
		this.code = code;
	}

	/**
	 * constructor using enum
	 * 
	 * @param code
	 * @param message
	 */
	public MessageException(FacadeEnums en) {
		this(en.getCode(), en.getMsg());
	}

	/**
	 * constructor using enum
	 * 
	 * @param code
	 * @param message
	 */
	public MessageException(FacadeEnums en, String message) {
		this(en.getCode(), message);
	}

	/**
	 * constructor using resp
	 * 
	 * @param resp
	 */
	public MessageException(BaseResp resp) {
		this(resp.getCode(), resp.getMsg());
	}
}
