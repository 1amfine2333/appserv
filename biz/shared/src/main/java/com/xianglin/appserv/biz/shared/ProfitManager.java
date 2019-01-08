/**
 * 
 */
package com.xianglin.appserv.biz.shared;

import java.math.BigDecimal;
import java.util.List;

import com.xianglin.appserv.common.service.facade.model.ProfitDTO;

/**
 * 
 * 
 * @author zhangyong 2016年8月15日下午6:05:51
 */
public interface ProfitManager {

	/**
	 * 此接口实时查询业务系统
	 * 查询离现在最近的一笔收益，如果无数据返回null
	 * 
	 * 查询返回的数据在服务端都必须是已经排序的，此方法没有对数据排序
	 * @param userPartyId
	 * @return
	 * @throws Exception
	 */
	ProfitDTO queryProfit(Long userPartyId) throws Exception;
	
	/**
	 * 查询本地同步后的数据
	 * 
	 * @param userPartyId
	 * @param busiType
	 * @param staticType
	 * @return
	 */
	ProfitDTO queryLocalProfit(Long userPartyId,String busiType,String staticType) throws Exception;
	/**
	 * V1.3兼容数据接口，根据参数查询，银行和贷款业务在赚钱页面不展示
	 * @param userPartyId
	 * @param busiTypes
	 * @param staticType
	 * @return
	 * @throws Exception
	 */
	ProfitDTO queryTopOne(Long userPartyId,List<String>busiTypes,String staticType) throws Exception;

	/**
	 * V1.3兼容数据接口，根据参数查询，银行和贷款业务在赚钱页面不展示
	 * @param userPartyId
	 * @param busiTypes
	 * @param staticType
     * @return
     */
	BigDecimal queryTotal(Long userPartyId,List<String>busiTypes,String staticType);
	
	/**
	 * 汇总数据查询
	 * 
	 * 
	 * @param userPartyId
	 * @param busiType
	 * @param staticType
	 * @return
	 * @throws Exception
	 */
	BigDecimal queryTotalProfit(Long userPartyId,String busiType,String staticType) throws Exception;

	/**
	 * 
	 * 收益汇总列表查询，按月汇总
	 * 
	 * @param userPartyId
	 * @param start
	 * @param pageSize
	 * @return
	 */
	List<ProfitDTO> queryProfitList(Long userPartyId,Integer start,Integer pageSize);
}
