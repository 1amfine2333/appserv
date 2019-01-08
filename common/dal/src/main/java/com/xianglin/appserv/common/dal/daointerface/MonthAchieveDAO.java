package com.xianglin.appserv.common.dal.daointerface;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.dal.dataobject.MonthAchieve;

public interface MonthAchieveDAO extends BaseDAO<MonthAchieve>{
	
	public List<MonthAchieve> select(Map<String, Object> paras);
	
	/**
	 * 查询收益|业绩的统计数据
	 * 
	 * 
	 * @param params
	 * @return
	 */
	BigDecimal selectSumAmount(Map<String,Object> params);

	/**
	 * 查询收益|业绩数据
	 * 按年月分类汇总
	 * 
	 * 
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> selectSumAmountGBYearAndMonth(Map<String,Object> params);
}