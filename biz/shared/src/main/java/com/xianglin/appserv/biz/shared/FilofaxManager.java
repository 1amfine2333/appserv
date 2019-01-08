package com.xianglin.appserv.biz.shared;

import com.xianglin.appserv.common.dal.dataobject.AppFilofaxAccount;
import com.xianglin.appserv.common.dal.dataobject.AppFilofaxBudget;
import com.xianglin.appserv.common.dal.dataobject.AppFilofaxCategory;
import com.xianglin.appserv.common.dal.dataobject.AppFilofaxDetail;
import com.xianglin.appserv.common.service.facade.model.vo.DailyDetailTotalVo;
import com.xianglin.appserv.core.model.exception.AppException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglei on 2017/7/4.
 */
public interface FilofaxManager {

    /**类别查询
     * @param paras
     * @return
     */
    List<AppFilofaxCategory> queryCategoryList(Map<String,Object> paras);

    /**x新增类别
     * @param category
     * @return
     */
    boolean addCategory(AppFilofaxCategory category);

    /** 按条件查询预算总额
     * @param paras
     * @return
     */
	BigDecimal querybudgetSum(Map<String, Object> paras);

	/** 按条件查询明细总额
     * @param paras
     * @return
     */
	BigDecimal queryFILOFAXAMOUNTSum(Map<String, Object> paras);

	/** 按条件查询明细列表
     * @param paras
     * @return
     */
	List<DailyDetailTotalVo> queryBudgetDetail(Map<String, Object> paras);

    /** 按条件查询(初始化)账号列表
     * @param paras
     * @return
     */
    List<AppFilofaxAccount> queryAccountList(Map<String,Object> paras);

    /** 按条件查询预算
     * @param paras
     * @return
     */
	List<AppFilofaxBudget> queryBudget(Map<String, Object> paras);

	 /** 添加一条预算
     * @param paras
     * @return
     */
	Boolean addBudget(AppFilofaxBudget budget);

	/** 更新一条预算
     * @param paras
     * @return
     */
	Boolean updateBudget(AppFilofaxBudget budget);

    /**新增賬戶
     * @param account
     * @return
     */
    boolean addAccount(AppFilofaxAccount account) throws AppException;

    /**賬戶更新
     * @param account
     * @return
     */
    boolean updateAccount(AppFilofaxAccount account) throws AppException;

    List<Map<String,Object>> getTypeSum(Map<String, Object> paras);

	List<Map<String,Object>> getCategorySum(Map<String, Object> paras);

    /**根据id查询账户
     * @param id
     * @return
     */
    AppFilofaxAccount queryAccount(Long id);

    /**新增账本明细
     * @param detail
     * @return
     */
    boolean addFiloFaxDetail(AppFilofaxDetail detail);

    /**更新记账明细
     * @param detail
     * @return
     */
    boolean updateFiloFaxDetail(AppFilofaxDetail detail);


    /**按条件查询
     * @param paras
     * @return
     */
    List<AppFilofaxDetail> queryFilofaxDetail(Map<String, Object> paras);

    /**根据id进行查询
     * @param id
     * @return
     */
    AppFilofaxDetail queryFilofaxDetail(Long id);

	List<Map<String,Object>> getMonSumByYear(Map<String, Object> paras);

	/**
     * 根据条件查询收入和支出
     * 
     * */
    Map<String,Object> queryInSumAndOutSum(Map<String, Object> paras);
}
