/**
 * 
 */
package com.xianglin.appserv.biz.shared.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xianglin.appserv.biz.shared.MonthAchieveManager;
import com.xianglin.appserv.common.dal.daointerface.MonthAchieveDAO;
import com.xianglin.appserv.common.dal.dataobject.MonthAchieve;
import com.xianglin.appserv.common.service.facade.model.vo.MonthAchieveVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.MonthAchieveQuery;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.SessionHelper;

/**
 * 
 * 
 * @author wanglei 2016年8月15日下午5:45:11
 */
@Service
public class MonthAchieveManagerImpl implements MonthAchieveManager {

	private static final Logger logger = LoggerFactory.getLogger(MonthAchieveManagerImpl.class);

	@Autowired
	private MonthAchieveDAO monthAchieveDAO;
	
	@Autowired
	private SessionHelper sessionHelper;
	/**
	 * @throws Exception 
	 * @see com.xianglin.appserv.biz.shared.MonthAchieveManager#query(com.xianglin.appserv.common.service.facade.model.vo.req.MonthAchieveQuery)
	 */
	@Override
	public List<MonthAchieveVo> query(MonthAchieveQuery req) throws Exception {
		Map<String, Object> paras = DTOUtils.beanToMap(req);
		List<MonthAchieve> list = monthAchieveDAO.select(paras);
		return DTOUtils.map(list, MonthAchieveVo.class);
	}

	/**
	 * @throws Exception 
	 * @see com.xianglin.appserv.biz.shared.MonthAchieveManager#detail(java.lang.Long)
	 */
	@Override
	public MonthAchieveVo detail(Long id) throws Exception {
		return DTOUtils.map(monthAchieveDAO.selectByPrimaryKey(id), MonthAchieveVo.class) ;
	}

	/**
	 * @see com.xianglin.appserv.biz.shared.MonthAchieveManager#querySumAmountGroup(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> querySumAmountGroup(Map<String, Object> param) {
		return monthAchieveDAO.selectSumAmountGBYearAndMonth(param);
	}

	/**
	 * @see com.xianglin.appserv.biz.shared.MonthAchieveManager#querySumAmount(java.util.Map)
	 */
	@Override
	public BigDecimal querySumAmount(Map<String, Object> param) {
		return monthAchieveDAO.selectSumAmount(param);
	}

}
