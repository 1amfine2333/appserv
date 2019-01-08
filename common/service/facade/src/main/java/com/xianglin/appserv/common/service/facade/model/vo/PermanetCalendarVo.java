package com.xianglin.appserv.common.service.facade.model.vo;

import java.util.Date;

public class PermanetCalendarVo extends BaseVo {
    /**  */
	private static final long serialVersionUID = 6303774506374699445L;
	private Long id;
    private String yearmonth;

    private String daycount;

    private String festivalinfo;

    private String lumarinfo1;

    private String lumarinfo2;

    private String dayviewyidesccontent;

    private String dayviewjidesccontent;

    private String pzbj;

    private String jsyq;

    private String xsyj;
    private String cs;

    private String wx;

    private Date createDate;

    private String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getYearmonth() {
		return yearmonth;
	}

	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}

	public String getDaycount() {
		return daycount;
	}

	public void setDaycount(String daycount) {
		this.daycount = daycount;
	}

	public String getFestivalinfo() {
		return festivalinfo;
	}

	public void setFestivalinfo(String festivalinfo) {
		this.festivalinfo = festivalinfo;
	}

	public String getLumarinfo1() {
		return lumarinfo1;
	}

	public void setLumarinfo1(String lumarinfo1) {
		this.lumarinfo1 = lumarinfo1;
	}

	public String getLumarinfo2() {
		return lumarinfo2;
	}

	public void setLumarinfo2(String lumarinfo2) {
		this.lumarinfo2 = lumarinfo2;
	}

	public String getDayviewyidesccontent() {
		return dayviewyidesccontent;
	}

	public void setDayviewyidesccontent(String dayviewyidesccontent) {
		this.dayviewyidesccontent = dayviewyidesccontent;
	}

	public String getDayviewjidesccontent() {
		return dayviewjidesccontent;
	}

	public void setDayviewjidesccontent(String dayviewjidesccontent) {
		this.dayviewjidesccontent = dayviewjidesccontent;
	}

	public String getPzbj() {
		return pzbj;
	}

	public void setPzbj(String pzbj) {
		this.pzbj = pzbj;
	}

	public String getJsyq() {
		return jsyq;
	}

	public void setJsyq(String jsyq) {
		this.jsyq = jsyq;
	}

	public String getXsyj() {
		return xsyj;
	}

	public void setXsyj(String xsyj) {
		this.xsyj = xsyj;
	}

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	public String getWx() {
		return wx;
	}

	public void setWx(String wx) {
		this.wx = wx;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
   
}