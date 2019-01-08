package com.xianglin.appserv.common.service.facade.model.vo;

import java.util.Date;

/**
 * Created by wanglei on 2017/5/2.
 */
public class NodeMonthReportVo extends BaseVo{

    private String month;

    private String reportName;

    private String createTime;
    
    private String reportUrl;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

    
}
