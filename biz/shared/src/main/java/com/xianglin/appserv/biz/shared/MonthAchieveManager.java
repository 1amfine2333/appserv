/**
 * 
 */
package com.xianglin.appserv.biz.shared;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.service.facade.model.vo.MonthAchieveVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.MonthAchieveQuery;

/**
 * 
 * 
 * @author wanglei 2016年8月15日下午5:39:28
 */
public interface MonthAchieveManager {

	/**
	 * 榜单查询
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	public List<MonthAchieveVo> query(MonthAchieveQuery req) throws Exception;
	
	/**
	 * 查看榜单明细
	 * 
	 * @param id
	 * @return
	 */
	public MonthAchieveVo detail(Long id) throws Exception;
	/**
	 * 
	 * 分组汇总数据
	 * 
	 * @param param
	 * @return
	 */
	List<Map<String,Object>> querySumAmountGroup(Map<String,Object> param);
	/**
	 * 汇总数据
	 * 
	 * 
	 * @param param
	 * @return
	 */
	BigDecimal querySumAmount(Map<String,Object> param);
}
