package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.io.Serializable;

/**
 * Created by wanglei on 2017/7/4.
 */
public class AppDateSectionReq extends Page  implements Serializable {

    /**
     *格式YYYYMMDD
     */
    private String startDay;

    /**
     *格式YYYYMMDD
     */
    private String endDay;

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
    
    
    
}
