package com.xianglin.appserv.common.service.facade.model.vo;

import com.xianglin.appserv.common.service.facade.req.DateSectionReq;

public class ProportionTotalVo extends DateSectionReq{
	/**
     *partyId
     */
    private Long partyId;
    
    /**
     * 
     * ACCOUNT
     * 
     * */
    private Long filofaxAccount; 
    
    /**
     *转入转出类型IN OUT
     */
    private String categoryMode;
    
    /**
     *标签
     */
    private String label;
    
    /**
     *类别
     */
    private String category;

    
    /**
     *区域类型，
     * 扩展字段，备用 
     * 类目STATUS 
     * 标签LABEL
     */
    private String sectionType;


	public Long getPartyId() {
		return partyId;
	}


	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}


	public Long getFilofaxAccount() {
		return filofaxAccount;
	}


	public void setFilofaxAccount(Long filofaxAccount) {
		this.filofaxAccount = filofaxAccount;
	}


	public String getCategoryMode() {
		return categoryMode;
	}


	public void setCategoryMode(String categoryMode) {
		this.categoryMode = categoryMode;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getSectionType() {
		return sectionType;
	}


	public void setSectionType(String sectionType) {
		this.sectionType = sectionType;
	}
    
}
