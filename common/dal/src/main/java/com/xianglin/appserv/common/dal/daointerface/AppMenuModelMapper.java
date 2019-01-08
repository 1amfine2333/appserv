package com.xianglin.appserv.common.dal.daointerface;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xianglin.appserv.common.dal.dataobject.AppMenuModel;
import com.xianglin.appserv.common.dal.dataobject.AppMenuModelExample;

public interface AppMenuModelMapper extends BaseDAO<AppMenuModel> {
	int deleteByExample(AppMenuModelExample example);

	int deleteByPrimaryKey(String mid);

	int insert(AppMenuModel record);

	int insertSelective(AppMenuModel record);

	List<AppMenuModel> selectByExample(AppMenuModelExample example);

	List<AppMenuModel> selectByPrimaryKey(String mid);

	int updateByExampleSelective(@Param("record") AppMenuModel record, @Param("example") AppMenuModelExample example);

	int updateByExample(@Param("record") AppMenuModel record, @Param("example") AppMenuModelExample example);

	int updateByPrimaryKeySelective(AppMenuModel record);

	int updateByPrimaryKey(AppMenuModel record);

	// 新添功能<!-- 根据mname查菜单信息 -->
	List<AppMenuModel> selectByMname(String mname);

	// 查询所有
	List<AppMenuModel> selectAll();

	// 根据mid查菜单列表
	List<AppMenuModel> selectAllByMid(String mid);

	// 根据mid查mname
	String selectMname(String mid);

	// 根据mid查创建时间
	Date selectCreateDate(String mid);
}