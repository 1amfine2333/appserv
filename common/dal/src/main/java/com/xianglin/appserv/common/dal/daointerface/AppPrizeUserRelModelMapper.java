package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppPrizeUserRelModel;
import com.xianglin.appserv.common.dal.dataobject.AppPrizeUserRelModelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppPrizeUserRelModelMapper {
    int countByExample(AppPrizeUserRelModelExample example);

    int deleteByExample(AppPrizeUserRelModelExample example);

    int deleteByPrimaryKey(Long relId);

    int insert(AppPrizeUserRelModel record);

    int insertSelective(AppPrizeUserRelModel record);

    List<AppPrizeUserRelModel> selectByExample(AppPrizeUserRelModelExample example);

    AppPrizeUserRelModel selectByPrimaryKey(Long relId);

    int updateByExampleSelective(@Param("record") AppPrizeUserRelModel record, @Param("example") AppPrizeUserRelModelExample example);

    int updateByExample(@Param("record") AppPrizeUserRelModel record, @Param("example") AppPrizeUserRelModelExample example);

    int updateByPrimaryKeySelective(AppPrizeUserRelModel record);

    int updateByPrimaryKey(AppPrizeUserRelModel record);
}