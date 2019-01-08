package com.xianglin.appserv.common.service.facade.model.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by wanglei on 2017/7/4.
 */
public class DailyDetailTotalVo extends BaseVo{
	
	private String inSum;//收入
	
	private String outSum;//支出
    
	private String day;//日期
    
    private String time;// 时间
	
	private long partyId;//用户ID
	
	private List<FilofaxDetailVo> filofaxDetailVo = new ArrayList<FilofaxDetailVo>();

	public String getInSum() {
		return inSum;
	}




	public void setInSum(String inSum) {
		this.inSum = inSum;
	}




	public String getOutSum() {
		return outSum;
	}




	public void setOutSum(String outSum) {
		this.outSum = outSum;
	}




	public String getDay() {
		return day;
	}




	public void setDay(String day) {
		this.day = day;
	}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getPartyId() {
		return partyId;
	}




	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}



	public List<FilofaxDetailVo> getFilofaxDetailVo() {
		return filofaxDetailVo;
	}




	public void setFilofaxDetailVo(List<FilofaxDetailVo> filofaxDetailVo) {
		this.filofaxDetailVo = filofaxDetailVo;
	}




	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

   
  
}
