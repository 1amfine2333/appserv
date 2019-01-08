package com.xianglin.appserv.common.dal.daointerface;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xianglin.appserv.common.dal.dataobject.AppRecruitJob;
import com.xianglin.appserv.common.dal.dataobject.AppRecruitResume;
import com.xianglin.appserv.common.dal.dataobject.Page;
import com.xianglin.appserv.common.dal.dataobject.User;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AppRecruitResumeDAO extends BaseMapper<AppRecruitResume> {

    List<AppRecruitResume> query(Map<String,Object> param);

    List<AppRecruitResume> queryJobResumeByParam(Map<String,Object> param);

    List<AppRecruitResume> queryResumeByParam(@Param("paras")AppRecruitResume appRecruitResume,@Param("page")  Page page,@Param("orderBy")String orderBy,@Param("isResume")String isResume);

    int queryJobResumeCount(Map<String,Object> param);

    int queryResumeCountByParam(@Param("paras") AppRecruitResume appRecruitResume,@Param("isResume")String isResume);

    Map<String,BigDecimal> queryPersonalAndRecommedResumeCount(Map<String,Object> map);
}
