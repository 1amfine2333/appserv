/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;


/**
 * 
 * 
 * @author qianshanshan 2016年3月29日下午7:52:58
 */
public class PosLoginResp extends BaseResp {
	
	private static final long serialVersionUID = -1898180908791318726L;
	//网点partyID
	private Long nodePartyId;
	//网点经理partyId
	private Long nodeManagerPartyId;
	
	// 短信输入错误次数
	private Integer smsErrorCount;
	// 密码输入错误次数
	private Integer pwdErrorCount;
	// 站点名称
	private String nodeName;
	// 站长姓名
	private String nodeManagerName;
	//行政代码
	private String districtCode;
	//站点地址
	private String nodeAddress;
	/**
	 * @return the nodePartyId
	 */
	public Long getNodePartyId() {
		return nodePartyId;
	}
	/**
	 * @param nodePartyId the nodePartyId to set
	 */
	public void setNodePartyId(Long nodePartyId) {
		this.nodePartyId = nodePartyId;
	}
	/**
	 * @return the nodeManagerPartyId
	 */
	public Long getNodeManagerPartyId() {
		return nodeManagerPartyId;
	}
	/**
	 * @param nodeManagerPartyId the nodeManagerPartyId to set
	 */
	public void setNodeManagerPartyId(Long nodeManagerPartyId) {
		this.nodeManagerPartyId = nodeManagerPartyId;
	}
	/**
	 * @return the smsErrorCount
	 */
	public Integer getSmsErrorCount() {
		return smsErrorCount;
	}
	/**
	 * @param smsErrorCount the smsErrorCount to set
	 */
	public void setSmsErrorCount(Integer smsErrorCount) {
		this.smsErrorCount = smsErrorCount;
	}
	/**
	 * @return the pwdErrorCount
	 */
	public Integer getPwdErrorCount() {
		return pwdErrorCount;
	}
	/**
	 * @param pwdErrorCount the pwdErrorCount to set
	 */
	public void setPwdErrorCount(Integer pwdErrorCount) {
		this.pwdErrorCount = pwdErrorCount;
	}
	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}
	/**
	 * @param nodeName the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	/**
	 * @return the nodeManagerName
	 */
	public String getNodeManagerName() {
		return nodeManagerName;
	}
	/**
	 * @param nodeManagerName the nodeManagerName to set
	 */
	public void setNodeManagerName(String nodeManagerName) {
		this.nodeManagerName = nodeManagerName;
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
	 * @return the nodeAddress
	 */
	public String getNodeAddress() {
		return nodeAddress;
	}
	/**
	 * @param nodeAddress the nodeAddress to set
	 */
	public void setNodeAddress(String nodeAddress) {
		this.nodeAddress = nodeAddress;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PosLoginResp [nodePartyId=" + nodePartyId + ", nodeManagerPartyId=" + nodeManagerPartyId
				+ ", smsErrorCount=" + smsErrorCount + ", pwdErrorCount=" + pwdErrorCount + ", nodeName=" + nodeName
				+ ", nodeManagerName=" + nodeManagerName + ", districtCode=" + districtCode + "]";
	}
}
