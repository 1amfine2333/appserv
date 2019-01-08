package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppPrizesActivityRuleModel;
import com.xianglin.appserv.common.dal.dataobject.AppPrizesActivityRuleModelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppPrizesActivityRuleModelMapper {
    int countByExample(AppPrizesActivityRuleModelExample example);

    int deleteByExample(AppPrizesActivityRuleModelExample example);

    int deleteByPrimaryKey(Long ruleId);

    int insert(AppPrizesActivityRuleModel record);

    int insertSelective(AppPrizesActivityRuleModel record);

    List<AppPrizesActivityRuleModel> selectByExample(AppPrizesActivityRuleModelExample example);

    AppPrizesActivityRuleModel selectByPrimaryKey(Long ruleId);

    int updateByExampleSelective(@Param("record") AppPrizesActivityRuleModel record, @Param("example") AppPrizesActivityRuleModelExample example);

    int updateByExample(@Param("record") AppPrizesActivityRuleModel record, @Param("example") AppPrizesActivityRuleModelExample example);

    int updateByPrimaryKeySelective(AppPrizesActivityRuleModel record);

    int updateByPrimaryKey(AppPrizesActivityRuleModel record);
}