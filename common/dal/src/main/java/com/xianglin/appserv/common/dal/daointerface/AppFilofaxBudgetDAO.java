package com.xianglin.appserv.common.dal.daointerface;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.dal.dataobject.AppFilofaxBudget;

public interface AppFilofaxBudgetDAO extends BaseDAO<AppFilofaxBudget>{

	BigDecimal selectSumBudget(Map<String, Object> paras);

	List<AppFilofaxBudget> getBudget(Map<String, Object> paras);

}
