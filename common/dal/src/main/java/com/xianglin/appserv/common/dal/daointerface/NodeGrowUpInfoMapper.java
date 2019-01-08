package com.xianglin.appserv.common.dal.daointerface;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.xianglin.appserv.common.dal.dataobject.NodeGrowUpInfo;
import com.xianglin.appserv.common.dal.dataobject.NodeGrowUpInfoExample;

@Repository
public interface NodeGrowUpInfoMapper {
	/**
	 * 查余额超过某数值
	 * 
	 * @param state
	 * @return
	 */
	int selectCountByBalance(BigDecimal balance);

	/**
	 * 查有余额人的个数
	 * 
	 * @return
	 */
	int selectCount();

	/**
	 * 省内小站个数
	 * 
	 * @return
	 */
	int selectCountByProvince(String province);

	// 根据partyId查所有
	NodeGrowUpInfo selectByAllPartyId(String nodeManagerPartyId);

	int countByExample(NodeGrowUpInfoExample example);

	int deleteByExample(NodeGrowUpInfoExample example);

	int deleteByPrimaryKey(String id);

	int insert(NodeGrowUpInfo record);

	int insertSelective(NodeGrowUpInfo record);

	List<NodeGrowUpInfo> selectByExample(NodeGrowUpInfoExample example);

	NodeGrowUpInfo selectByPrimaryKey(String id);

	int updateByExampleSelective(@Param("record") NodeGrowUpInfo record, @Param("example") NodeGrowUpInfoExample example);

	int updateByExample(@Param("record") NodeGrowUpInfo record, @Param("example") NodeGrowUpInfoExample example);

	int updateByPrimaryKeySelective(NodeGrowUpInfo record);

	int updateByPrimaryKey(NodeGrowUpInfo record);
}