/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import java.util.Date;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

/**
 * 
 * app首页的baner数据
 * @author zhangyong 2016年8月15日下午4:20:22
 */
public class BanerDTO extends BaseVo {

	/**  */
	private static final long serialVersionUID = -7180455823411007144L;

	private Long id;
	
	private String url;
	
	private String imageUrl;
	
	private String desc;
	
	private Date createDate;
	
	private Date updateDate;
	
	private String isDeleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
}
