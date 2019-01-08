/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model.vo;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author zhangyong 2016年10月13日上午10:15:39
 */
public class RedFacadeVo extends BaseVo{

	
	private String redPacketNum;
	
	private String startDate;
	
	private List<Map<String,Object>> tipsList;

	public String getRedPacketNum() {
		return redPacketNum;
	}

	public void setRedPacketNum(String redPacketNum) {
		this.redPacketNum = redPacketNum;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public List<Map<String, Object>> getTipsList() {
		return tipsList;
	}

	public void setTipsList(List<Map<String, Object>> tipsList) {
		this.tipsList = tipsList;
	}
	
	
	
}
