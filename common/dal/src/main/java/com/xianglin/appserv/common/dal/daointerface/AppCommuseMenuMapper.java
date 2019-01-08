package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppCommuseMenu;
import com.xianglin.appserv.common.dal.dataobject.AppCommuseMenuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppCommuseMenuMapper {
    int deleteByExample(AppCommuseMenuExample example);

    int deleteByPrimaryKey(Long seqId);

    int insert(AppCommuseMenu record);

    int insertSelective(AppCommuseMenu record);

    List<AppCommuseMenu> selectByExample(AppCommuseMenuExample example);

    AppCommuseMenu selectByPrimaryKey(Long seqId);

    int updateByExampleSelective(@Param("record") AppCommuseMenu record, @Param("example") AppCommuseMenuExample example);

    int updateByExample(@Param("record") AppCommuseMenu record, @Param("example") AppCommuseMenuExample example);

    int updateByPrimaryKeySelective(AppCommuseMenu record);

    int updateByPrimaryKey(AppCommuseMenu record);
}