package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppActivityModel;
import com.xianglin.appserv.common.dal.dataobject.AppActivityModelExample;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AppActivityModelMapper {
    int countByExample(AppActivityModelExample example);

    int deleteByExample(AppActivityModelExample example);

    int deleteByPrimaryKey(Long activeId);

    int insert(AppActivityModel record);

    int insertSelective(AppActivityModel record);

    List<AppActivityModel> selectByExample(AppActivityModelExample example);

    AppActivityModel selectByPrimaryKey(Long activeId);

    int updateByExampleSelective(@Param("record") AppActivityModel record, @Param("example") AppActivityModelExample example);

    int updateByExample(@Param("record") AppActivityModel record, @Param("example") AppActivityModelExample example);

    int updateByPrimaryKeySelective(AppActivityModel record);

    int updateByPrimaryKey(AppActivityModel record);

	List<AppActivityModel> selectByPage(Map<String, Object> paras);

}