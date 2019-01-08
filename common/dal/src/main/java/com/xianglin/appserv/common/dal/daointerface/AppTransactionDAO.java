package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppActivityReward;
import com.xianglin.appserv.common.dal.dataobject.AppTransaction;
import com.xianglin.appserv.common.dal.dataobject.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AppTransactionDAO extends BaseDAO<AppTransaction> {

	/** 根据条件查询
	 * @param paras
	 * @return
	 */
	List<AppTransaction> selectList(Map<String, Object> paras);

	/** 根据条件查询数量
	 * @param paras
	 * @return
	 */
	int selectCount(Map<String, Object> paras);

	/**查询订单总金额
	 * @param paras
	 * @param page
	 * @return
	 */
	BigDecimal selectTotalAmount(@Param("paras") AppTransaction paras,@Param("page") Page page);

	/**根据条件查询
	 * @param paras
	 * @param page
	 * @return
	 */
	List<AppTransaction> selectTranList(@Param("paras") AppTransaction paras,@Param("page") Page page);
}