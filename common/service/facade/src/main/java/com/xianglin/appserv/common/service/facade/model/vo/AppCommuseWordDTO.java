package com.xianglin.appserv.common.service.facade.model.vo;

import java.io.Serializable;

public class AppCommuseWordDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2676566007792757930L;

	private Long seqId;

	private String partyid;

	private String menuId;

	private String keyword;

	private Long useCount;

	private String dataStatus;

	public Long getSeqId() {
		return seqId;
	}

	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}

	public String getPartyid() {
		return partyid;
	}

	public void setPartyid(String partyid) {
		this.partyid = partyid == null ? null : partyid.trim();
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword == null ? null : keyword.trim();
	}

	public Long getUseCount() {
		return useCount;
	}

	public void setUseCount(Long useCount) {
		this.useCount = useCount;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus == null ? null : dataStatus.trim();
	}

}