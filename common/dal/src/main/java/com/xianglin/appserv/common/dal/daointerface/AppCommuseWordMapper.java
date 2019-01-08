package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppCommuseWord;
import com.xianglin.appserv.common.dal.dataobject.AppCommuseWordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppCommuseWordMapper {
    int deleteByExample(AppCommuseWordExample example);

    int deleteByPrimaryKey(Long seqId);

    int insert(AppCommuseWord record);

    int insertSelective(AppCommuseWord record);

    List<AppCommuseWord> selectByExample(AppCommuseWordExample example);

    AppCommuseWord selectByPrimaryKey(Long seqId);

    int updateByExampleSelective(@Param("record") AppCommuseWord record, @Param("example") AppCommuseWordExample example);

    int updateByExample(@Param("record") AppCommuseWord record, @Param("example") AppCommuseWordExample example);

    int updateByPrimaryKeySelective(AppCommuseWord record);

    int updateByPrimaryKey(AppCommuseWord record);
}