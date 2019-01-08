package com.xianglin.appserv.common.service.facade.model.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wanglei on 2017/7/4.
 */
public class FilofaxDetailVo extends BaseVo{
	private Long id;

    private Long partyId;

    private Long filofaxAccount;

    private String category;

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
    
    private String inSum;
    
    private String outSum;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
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

	public String getInSum() {
		return inSum;
	}

	public void setInSum(String inSum) {
		this.inSum = inSum;
	}

	public String getOutSum() {
		return outSum;
	}

	public void setOutSum(String outSum) {
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
