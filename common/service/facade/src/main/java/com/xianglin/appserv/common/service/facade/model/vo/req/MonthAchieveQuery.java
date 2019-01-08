/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model.vo.req;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

/**
 * 
 * 
 * @author wanglei 2016年8月12日下午2:53:12
 */
public class MonthAchieveQuery extends BaseVo {
	
	/**  */
	private static final long serialVersionUID = 4487144957041494718L;

	private String staticType;
	
	private String busiType;
	
	private String month;
	
	private String year;
	
	private Integer startPage = 1;
	
	private Integer pageSize = 10;

	public String getStaticType() {
		return staticType;
	}

	public void setStaticType(String staticType) {
		this.staticType = staticType;
	}

	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Integer getStartPage() {
		return startPage;
	}

	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
