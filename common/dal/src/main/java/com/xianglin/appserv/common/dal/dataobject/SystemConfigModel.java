package com.xianglin.appserv.common.dal.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author gengchaogang
 * @dateTime 2015年12月7日 下午3:53:25
 *
 */
public class SystemConfigModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1430076943185351966L;

	private Long seqId;
	private String code;
	private String value;
	private String description;
	private String comments;
	private Date createTime;
	private Date updateTime;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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