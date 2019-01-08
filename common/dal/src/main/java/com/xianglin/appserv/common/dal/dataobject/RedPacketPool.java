package com.xianglin.appserv.common.dal.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class RedPacketPool {
    private Long id;

    private String type;
    
    private Long partyId;

	private Long sendPartyId;
    
    private String orderNumber;
    
    private String transactionStatus;
    
    private String status;

    private BigDecimal amount;
    
    private String redPacketCode;

    private String source;

    private String isDeleted;
   
    private Date effectiveDate;

    private Date expiredDate;

    private Date createDate;

    private Date updateDate;
    
    private String description;
	private String userName;
	private String accountName;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getRedPacketCode() {
		return redPacketCode;
	}

	public void setRedPacketCode(String redPacketCode) {
		this.redPacketCode = redPacketCode;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Long getPartyId() {
		return partyId;
	}

	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Override
	public String toString() {
		return "RedPacketPool{" +
				"id=" + id +
				", type='" + type + '\'' +
				", partyId=" + partyId +
				", sendPartyId=" + sendPartyId +
				", orderNumber='" + orderNumber + '\'' +
				", transactionStatus='" + transactionStatus + '\'' +
				", status='" + status + '\'' +
				", amount=" + amount +
				", redPacketCode='" + redPacketCode + '\'' +
				", source='" + source + '\'' +
				", isDeleted='" + isDeleted + '\'' +
				", effectiveDate=" + effectiveDate +
				", expiredDate=" + expiredDate +
				", createDate=" + createDate +
				", updateDate=" + updateDate +
				", description='" + description + '\'' +
				", userName='" + userName + '\'' +
				", accountName='" + accountName + '\'' +
				'}';
	}

	public Long getSendPartyId() {
		return sendPartyId;
	}

	public void setSendPartyId(Long sendPartyId) {
		this.sendPartyId = sendPartyId;
	}
}