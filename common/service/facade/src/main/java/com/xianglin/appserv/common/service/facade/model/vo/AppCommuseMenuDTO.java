package com.xianglin.appserv.common.service.facade.model.vo;

import java.io.Serializable;

public class AppCommuseMenuDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4838092546983092515L;

	private Long seqId;

	private String partyid;

	private String menuId;

	private String menuName;

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
		this.menuId = menuId == null ? null : menuId.trim();
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
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