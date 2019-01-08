package com.xianglin.appserv.common.service.facade.model.vo;

import java.util.List;

/**
 * 网点管理Response
 *
 * @author shaodongyang
 * @class com.xianglin.xlnodecore.common.service.facade.resp.XlcNodeReq
 * @date 2015年9月14日 上午11:14:30
 * @since 1.0
 */
public class BankImportVo extends BaseVo {

	private static final long serialVersionUID = -1365293259487186067L;
	private BankImportVo vo;
	private long  nodePartyId;
	//行政位置
	private String districtCode;

	private String startTime;
	private String endTime;
	
	private List<BankImportVo> bankImportList;

	/**
	 * default constructor
	 */
	public BankImportVo() {}
	/**
	 * @param vo constructor
	 */
	public BankImportVo(BankImportVo vo) {
		this.vo = vo;
	}

	public long getNodePartyId() {
		return nodePartyId;
	}

	public void setNodePartyId(long nodePartyId) {
		this.nodePartyId = nodePartyId;
	}

	public BankImportVo getVo() {

		return vo;
	}

	public void setVo(BankImportVo vo) {

		this.vo = vo;
	}

	public List<BankImportVo> getBankImportList() {
		return bankImportList;
	}

	public void setBankImportList(List<BankImportVo> bankImportList) {
		this.bankImportList = bankImportList;
	}
	/**
	 * @return the districtCode
	 */
	public String getDistrictCode() {
		return districtCode;
	}
	/**
	 * @param districtCode the districtCode to set
	 */
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
