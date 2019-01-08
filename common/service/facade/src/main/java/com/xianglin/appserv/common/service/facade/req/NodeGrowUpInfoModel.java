package com.xianglin.appserv.common.service.facade.req;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class NodeGrowUpInfoModel implements Serializable {

	// 省内小站个数
	private int nodeCountByProvince;

	// 共几家小站
	private int nodeCount;

	public int getNodeCountByProvince() {
		return nodeCountByProvince;
	}

	public void setNodeCountByProvince(int nodeCountByProvince) {
		this.nodeCountByProvince = nodeCountByProvince;
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}

	// 余额数描述
	private String percentageInfo;

	public String getPercentageInfo() {
		return percentageInfo;
	}

	public void setPercentageInfo(String percentageInfo) {
		this.percentageInfo = percentageInfo;
	}

	// 余额超过小站人数百分比
	private String percentage;

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	// 小站创建天数
	private int createTime;

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	private String id;

	private String nodeCode;

	private String nodeName;

	private String nodeManagerPartyId;

	private String nodeManagerName;

	private String province;

	private Date createDate;

	private String orderAll;

	private String orderProvince;

	private Date openDate;

	private Date bankDate;

	private String bankState;

	private BigDecimal balance;

	private Date commerceDate;

	private String commerceState;

	private String transactionNum;

	private String firstProduct;

	private BigDecimal firstOrderPrice;

	private Date importDate;

	private String other;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode == null ? null : nodeCode.trim();
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName == null ? null : nodeName.trim();
	}

	public String getNodeManagerPartyId() {
		return nodeManagerPartyId;
	}

	public void setNodeManagerPartyId(String nodeManagerPartyId) {
		this.nodeManagerPartyId = nodeManagerPartyId == null ? null : nodeManagerPartyId.trim();
	}

	public String getNodeManagerName() {
		return nodeManagerName;
	}

	public void setNodeManagerName(String nodeManagerName) {
		this.nodeManagerName = nodeManagerName == null ? null : nodeManagerName.trim();
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province == null ? null : province.trim();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getOrderAll() {
		return orderAll;
	}

	public void setOrderAll(String orderAll) {
		this.orderAll = orderAll == null ? null : orderAll.trim();
	}

	public String getOrderProvince() {
		return orderProvince;
	}

	public void setOrderProvince(String orderProvince) {
		this.orderProvince = orderProvince == null ? null : orderProvince.trim();
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public Date getBankDate() {
		return bankDate;
	}

	public void setBankDate(Date bankDate) {
		this.bankDate = bankDate;
	}

	public String getBankState() {
		return bankState;
	}

	public void setBankState(String bankState) {
		this.bankState = bankState == null ? null : bankState.trim();
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Date getCommerceDate() {
		return commerceDate;
	}

	public void setCommerceDate(Date commerceDate) {
		this.commerceDate = commerceDate;
	}

	public String getCommerceState() {
		return commerceState;
	}

	public void setCommerceState(String commerceState) {
		this.commerceState = commerceState == null ? null : commerceState.trim();
	}

	public String getTransactionNum() {
		return transactionNum;
	}

	public void setTransactionNum(String transactionNum) {
		this.transactionNum = transactionNum == null ? null : transactionNum.trim();
	}

	public String getFirstProduct() {
		return firstProduct;
	}

	public void setFirstProduct(String firstProduct) {
		this.firstProduct = firstProduct == null ? null : firstProduct.trim();
	}

	public BigDecimal getFirstOrderPrice() {
		return firstOrderPrice;
	}

	public void setFirstOrderPrice(BigDecimal firstOrderPrice) {
		this.firstOrderPrice = firstOrderPrice;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other == null ? null : other.trim();
	}
}