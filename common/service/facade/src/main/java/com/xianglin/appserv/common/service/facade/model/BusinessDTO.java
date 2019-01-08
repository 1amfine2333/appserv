/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

/**
 * 
 * 
 * @author zhangyong 2016年8月15日下午4:26:14
 */
public class BusinessDTO extends BaseVo {

	/**  */
	private static final long serialVersionUID = -949318167244766337L;


	private Long id;
	
	
	private String nodePartyId;
	private String businessType;
	
	private String businessStatus;
	
	private String h5url;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNodePartyId() {
		return nodePartyId;
	}

	public void setNodePartyId(String nodePartyId) {
		this.nodePartyId = nodePartyId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}

	public String getH5url() {
		return h5url;
	}

	public void setH5url(String h5url) {
		this.h5url = h5url;
	}
	
	
}
