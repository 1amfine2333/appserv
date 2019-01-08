/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

/**
 * 通用服务响应结果
 * 
 * @author pengpeng 2016年2月18日下午4:04:56
 */
public class Request<T> extends BaseVo {

	/**  */
	private static final long serialVersionUID = 1L;
	
	private Long userPartyId;
	
	private String requestId;
	
	private Long partyId;
	
	private T req;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public T getReq() {
		return req;
	}

	public void setReq(T req) {
		this.req = req;
	}

	public Long getUserPartyId() {
		return userPartyId;
	}

	public void setUserPartyId(Long userPartyId) {
		this.userPartyId = userPartyId;
	}

    public Long getPartyId() {
        return this.partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }
}
