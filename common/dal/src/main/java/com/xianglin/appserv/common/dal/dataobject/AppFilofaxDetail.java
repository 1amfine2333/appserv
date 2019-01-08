package com.xianglin.appserv.common.dal.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class AppFilofaxDetail {
    private Long id;

    private Long partyId;

    private Long filofaxAccount;

    private Long category;

    private String categoryMode;

    private String day;

    private BigDecimal amount;

    private String image;

    private String label;

    private String status;

    private String isDeleted;

    private Date createDate;

    private Date updateDate;

    private String comments;
    
    private BigDecimal inSum;
    
    private BigDecimal outSum;

    private String categoryName;
    
    private String accountName;

    private String accountImage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public String getCategoryMode() {
        return categoryMode;
    }

    public void setCategoryMode(String categoryMode) {
        this.categoryMode = categoryMode == null ? null : categoryMode.trim();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day == null ? null : day.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted == null ? null : isDeleted.trim();
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments == null ? null : comments.trim();
    }


	public BigDecimal getInSum() {
		return inSum;
	}

	public void setInSum(BigDecimal inSum) {
		this.inSum = inSum;
	}

	public BigDecimal getOutSum() {
		return outSum;
	}

	public void setOutSum(BigDecimal outSum) {
		this.outSum = outSum;
	}

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountImage() {
        return accountImage;
    }

    public void setAccountImage(String accountImage) {
        this.accountImage = accountImage;
    }
}
