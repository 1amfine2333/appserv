/**
 * 
 */
package com.xianglin.appserv.biz.service.implement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xianglin.appserv.common.dal.daointerface.PermanetCalendarDAO;
import com.xianglin.appserv.common.dal.dataobject.PermanetCalendar;
import com.xianglin.appserv.common.service.facade.app.PermanetCalendarService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.vo.PermanetCalendarVo;
import com.xianglin.appserv.common.util.DTOUtils;

/**
 * 
 * 
 * @author zhangyong 2016年9月29日上午11:11:52
 */
@Service
public class PermanetCalendarServiceImpl implements PermanetCalendarService {

	private static final Logger logger =LoggerFactory.getLogger(PermanetCalendarServiceImpl.class);
	@Autowired
	private PermanetCalendarDAO permanetCalendarDao;
	/**
	 * 
	 */
	@Override
	public Response<List<PermanetCalendarVo>> getPerCalendarList(Map<String, Object> param) {
		Response<List<PermanetCalendarVo>> resp = new Response<>(null);
		if(null == param ||param.isEmpty()){
			resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
		}else{
			List<PermanetCalendar> dbList = permanetCalendarDao.getPermanetCalendarList(param);
			if(CollectionUtils.isNotEmpty(dbList)){
				List<PermanetCalendarVo> voList;
				try {
					voList = DTOUtils.map(dbList, PermanetCalendarVo.class);
					resp.setResult(voList);
				} catch (Exception e) {
					logger.error("转换错误",e);
					resp.setFacade(FacadeEnums.E_S_DATA_ERROR);
				}
			
			}else{
				resp.setFacade(FacadeEnums.RETURN_EMPTY);
			}
		}
		
		return resp;
	}

	/**
	 * 
	 */
	@Override
	public Response<PermanetCalendarVo> getPerCalendar(String YMD) {
		Response<PermanetCalendarVo> resp = new Response<>(null);
		if(StringUtils.isEmpty(YMD)){
			resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
		}else{
			logger.info("查询日期：{}",YMD);
			
			if(StringUtils.length(YMD)!= 8 || !NumberUtils.isDigits(YMD)){
				resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
				resp.setMemo("参数格式为：yyyyMMdd,数字类型");
				resp.setTips("参数格式为：yyyyMMdd,数字类型");
				return resp;
			}
			String year = YMD.substring(0,4).concat("年");
			String month = Integer.parseInt(YMD.substring(4,6))+"月";
			String day = Integer.parseInt(YMD.substring(6, 8))+"";
			Map<String,Object> param = new HashMap<>();
			param.put("yearMonth", year+month);
			param.put("dayCount", day);
			
			List<PermanetCalendar> dbList = permanetCalendarDao.getPermanetCalendarList(param);
			if(CollectionUtils.isNotEmpty(dbList)){
				PermanetCalendar pc = dbList.get(0);
				PermanetCalendarVo vo;
				try {
					vo = DTOUtils.map(pc, PermanetCalendarVo.class);
					resp.setResult(vo);
				} catch (Exception e) {
					logger.error("转换错误",e);
					resp.setFacade(FacadeEnums.E_S_DATA_ERROR);
				}
				
			}else{
				resp.setFacade(FacadeEnums.RETURN_EMPTY);
			}
			
		}
		return resp;
	}

	/**
	 *
	 */
	@Override
	public Response<List<PermanetCalendarVo>> getMonthPerCalendar(String YM) {
		
		Response<List<PermanetCalendarVo>> resp = new Response<>(null);
		if(StringUtils.isEmpty(YM)){
			resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
		}else{
			logger.info("查询日期：{}",YM);
			
			if(StringUtils.length(YM)!= 6 || NumberUtils.isDigits(YM)){
				resp.setFacade(FacadeEnums.E_C_INPUT_INVALID);
				resp.setMemo("参数格式为：yyyyMM,数字类型");
				resp.setTips("参数格式为：yyyyMM,数字类型");
				return resp;
			}
			String year = YM.substring(0,4).concat("年");
			String month = YM.substring(4,6).concat("月");
			Map<String,Object> param = new HashMap<>();
			param.put("yearMonth", year+month);
			
			List<PermanetCalendar> dbList = permanetCalendarDao.getPermanetCalendarList(param);
			if(CollectionUtils.isNotEmpty(dbList)){
				List<PermanetCalendarVo> vo;
				try {
					vo = DTOUtils.map(dbList, PermanetCalendarVo.class);
					resp.setResult(vo);
				} catch (Exception e) {
					logger.error("数据转换错误",e);
					resp.setFacade(FacadeEnums.E_S_DATA_ERROR);
				}
			
			}else{
				resp.setFacade(FacadeEnums.RETURN_EMPTY);
			}
			
		}
		return resp;
	}

}
