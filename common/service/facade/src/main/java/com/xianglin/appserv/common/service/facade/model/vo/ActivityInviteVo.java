/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author wanglei 2016年12月13日下午2:47:09
 */
public class ActivityInviteVo extends BaseVo {

	private Long id;

    private Long partyId;
    
    private String commentName;
    
    private Integer ranking;

    private String qrCode;
    
    private Integer recCount = 0;

    private BigDecimal recAmt = BigDecimal.ZERO;
    
    private String recAmtStr = "0";

    private String status;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;
    
    private String headImg;
    
    /** 微信分享配置
     * 包含信息，title，message，url，titleImg
      */
    private Map<String, String> wxContent;
    
    /** 通用配置 */
    private Map<String, String> cfContent;

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

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public Integer getRecCount() {
		return recCount;
	}

	public void setRecCount(Integer recCount) {
		this.recCount = recCount;
	}

	public BigDecimal getRecAmt() {
		return recAmt;
	}

	public void setRecAmt(BigDecimal recAmt) {
		this.recAmt = recAmt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getRecAmtStr() {
		return recAmtStr;
	}

	public void setRecAmtStr(String recAmtStr) {
		this.recAmtStr = recAmtStr;
	}

	public Map<String, String> getWxContent() {
		return wxContent;
	}

	public void setWxContent(Map<String, String> wxContent) {
		this.wxContent = wxContent;
	}

	public Map<String, String> getCfContent() {
		return cfContent;
	}

	public void setCfContent(Map<String, String> cfContent) {
		this.cfContent = cfContent;
	}

	public String getCommentName() {
		return commentName;
	}

	public void setCommentName(String commentName) {
		this.commentName = commentName;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
}
