package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppPrizesModel;
import com.xianglin.appserv.common.dal.dataobject.AppPrizesModelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppPrizesModelMapper {
    int countByExample(AppPrizesModelExample example);

    int deleteByExample(AppPrizesModelExample example);

    int deleteByPrimaryKey(Long prizeId);

    int insert(AppPrizesModel record);

    int insertSelective(AppPrizesModel record);

    List<AppPrizesModel> selectByExample(AppPrizesModelExample example);

    AppPrizesModel selectByPrimaryKey(Long prizeId);

    int updateByExampleSelective(@Param("record") AppPrizesModel record, @Param("example") AppPrizesModelExample example);

    int updateByExample(@Param("record") AppPrizesModel record, @Param("example") AppPrizesModelExample example);

    int updateByPrimaryKeySelective(AppPrizesModel record);

    int updateByPrimaryKey(AppPrizesModel record);
}