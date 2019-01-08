/**
 * 
 */
package com.xianglin.appserv.common.service.facade.app;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.PermanetCalendarVo;


/**
 * 
 * 
 * @author zhangyong 2016年9月29日上午11:04:40
 */
public interface PermanetCalendarService {

	Response<List<PermanetCalendarVo>> getPerCalendarList(Map<String,Object> param);
	/**
	 * 根据年月日查询
	 * 
	 * 
	 * @param YMD 年月日
	 * @return
	 */
	Response<PermanetCalendarVo> getPerCalendar(String YMD);
	
	/**
	 * 根据年月份查询
	 * 
	 * 
	 * @param YM 年和月
	 * @return
	 */
	Response<List<PermanetCalendarVo>> getMonthPerCalendar(String YM);
	
	
}
