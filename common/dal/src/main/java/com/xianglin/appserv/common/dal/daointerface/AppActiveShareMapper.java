package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppActiveShare;
import com.xianglin.appserv.common.dal.dataobject.AppActiveShareExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppActiveShareMapper {
    int countByExample(AppActiveShareExample example);

    int deleteByExample(AppActiveShareExample example);

    int deleteByPrimaryKey(Long shareId);

    int insert(AppActiveShare record);

    int insertSelective(AppActiveShare record);

    List<AppActiveShare> selectByExample(AppActiveShareExample example);

    AppActiveShare selectByPrimaryKey(Long shareId);

    int updateByExampleSelective(@Param("record") AppActiveShare record, @Param("example") AppActiveShareExample example);

    int updateByExample(@Param("record") AppActiveShare record, @Param("example") AppActiveShareExample example);

    int updateByPrimaryKeySelective(AppActiveShare record);

    int updateByPrimaryKey(AppActiveShare record);
}