package com.xianglin.appserv.common.service.facade.req;

import java.io.Serializable;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;
import com.xianglin.appserv.common.service.facade.model.vo.PageResp;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;

/**
 * Created by wanglei on 2017/7/4.
 */
public class DateSectionReq extends PageReq  implements Serializable {

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
