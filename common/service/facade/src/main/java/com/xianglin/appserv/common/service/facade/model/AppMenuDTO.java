package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class AppMenuDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1858426791321413837L;

	private String mid;

	private String pid;

	private String mname;
	// 父菜单的名字
	private String pname;

	private BigDecimal mlevel;

	private BigDecimal morder;

	private String murl;

	private String imgurl;

	private String mstatus;

	private BigDecimal busitype;
	// 页面转字符描述用
	private String busitype1;

	public String getBusitype1() {
		return busitype1;
	}

	public void setBusitype1(String busitype1) {
		this.busitype1 = busitype1;
	}

	private Date createDate;

	private Date updateDate;

	private List<String> midList;

	public List<String> getMidList() {
		return midList;
	}

	public void setMidList(List<String> midList) {
		this.midList = midList;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
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

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid == null ? null : mid.trim();
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid == null ? null : pid.trim();
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname == null ? null : mname.trim();
	}

	public BigDecimal getMlevel() {
		return mlevel;
	}

	public void setMlevel(BigDecimal mlevel) {
		this.mlevel = mlevel;
	}

	public BigDecimal getMorder() {
		return morder;
	}

	public void setMorder(BigDecimal morder) {
		this.morder = morder;
	}

	public String getMurl() {
		return murl;
	}

	public void setMurl(String murl) {
		this.murl = murl == null ? null : murl.trim();
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl == null ? null : imgurl.trim();
	}

	public String getMstatus() {
		return mstatus;
	}

	public void setMstatus(String mstatus) {
		this.mstatus = mstatus == null ? null : mstatus.trim();
	}

	public BigDecimal getBusitype() {
		return busitype;
	}

	public void setBusitype(BigDecimal busitype) {
		this.busitype = busitype;
	}

}
