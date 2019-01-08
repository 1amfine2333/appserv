package com.xianglin.appserv.common.service.facade.model;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;
import com.xianglin.appserv.common.service.facade.model.vo.PointRushVo;
import com.xianglin.appserv.common.service.facade.model.vo.StationVo;

public class PointRushDTO extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1695297618906392852L;

	/**	整点钱用户列表	**/
	private List<PointRushVo> pVosList;

	/**	光伏信息 **/
	private List<StationVo> stations;

	/**	新装光伏列表10天之内的信息**/
	private List<StationVo> newStatiosList;
	
	/**	活动开始标示 **/
	private boolean activityFlag;
	
	private String redPacketUrl;

	public List<PointRushVo> getpVosList() {
		return pVosList;
	}

	public void setpVosList(List<PointRushVo> pVosList) {
		this.pVosList = pVosList;
	}

	public boolean isActivityFlag() {
		return activityFlag;
	}

	public void setActivityFlag(boolean activityFlag) {
		this.activityFlag = activityFlag;
	}

	public List<StationVo> getStations() {
		return stations;
	}

	public void setStations(List<StationVo> stations) {
		this.stations = stations;
	}

	public List<StationVo> getNewStatiosList() {
		return newStatiosList;
	}

	public void setNewStatiosList(List<StationVo> newStatiosList) {
		this.newStatiosList = newStatiosList;
	}

	public String getRedPacketUrl() {
		return redPacketUrl;
	}

	public void setRedPacketUrl(String redPacketUrl) {
		this.redPacketUrl = redPacketUrl;
	}

	@Override
	public String toString() {
		return "PointRushDTO{" +
				", activityFlag=" + activityFlag +
				", redPacketUrl='" + redPacketUrl + '\'' +
				"} ";
	}
}
