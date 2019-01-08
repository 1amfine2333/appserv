package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.ActivityDeposits;
import com.xianglin.appserv.common.dal.dataobject.AppActivity;
import com.xianglin.appserv.common.dal.dataobject.AppFilofaxDetail;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * 记账本明细
 */
public interface AppFilofaxDetailDAO extends BaseDAO<AppFilofaxDetail>{
    
    /**
     * 根据条件查询明细列表
     * */
    List<AppFilofaxDetail> selectList(Map<String, Object> paras);

    /**
     * 根据条件查询金额总和
     * */
	BigDecimal selectSumFilofaxAccount(Map<String, Object> paras);

	/**
     * 
     * 根据条件查询收入和支出列表
     * */
	List<Map<String,Object>> getAmountSum(Map<String, Object> paras);
  /*  *//**
     *
     * 按条件查询每天的明细金额总和
     * *//*
	List<Map<String,Object>> getDailyTotal(Map<String, Object> paras);*/
    
	/**
     * 
     * 根据标签或类别查询明细金额总和
     * */
	List<Map<String,Object>> getCategorySum(Map<String, Object> paras);

	/**
     * 
     * 查询某年的每月收入和支出总额
     * */
	List<Map<String,Object>> getMonSumByYear(Map<String, Object> paras);

	/**
     * 
     * 按条件查询每天的明细金额总和
     * */
	List<Map<String,Object>> getTypeSum(Map<String, Object> paras);

	/**
     * 按条件查询明细列表
     * */
	List<AppFilofaxDetail> getCategoryList(Map<String, Object> map);
	
	/**
     * 按条件查询收入和支出
     * */
    Map<String, Object> queryInSumAndOutSum(Map<String, Object> paras);
}
