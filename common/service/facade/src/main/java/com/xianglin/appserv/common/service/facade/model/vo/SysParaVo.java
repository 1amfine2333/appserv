package com.xianglin.appserv.common.service.facade.model.vo;

import java.util.Date;

/**
 * Created by wanglei on 2017/1/4.
 */
public class SysParaVo extends BaseVo {

	private Long seqId;
	private String code;
	private String value;
	private String description;
	private String comments;
	private Date createTime;
	private Date updateTime;
	private String valueShow;

	public String getValueShow() {
		return valueShow;
	}

	public void setValueShow(String valueShow) {
		this.valueShow = valueShow;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getSeqId() {
		return seqId;
	}

	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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
}
