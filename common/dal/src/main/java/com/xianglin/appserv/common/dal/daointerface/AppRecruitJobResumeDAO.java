package com.xianglin.appserv.common.dal.daointerface;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xianglin.appserv.common.dal.dataobject.AppRecruitJob;
import com.xianglin.appserv.common.dal.dataobject.AppRecruitJobResume;
import com.xianglin.appserv.common.dal.dataobject.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface AppRecruitJobResumeDAO extends BaseMapper<AppRecruitJobResume> {


    List<AppRecruitJobResume> query(Map<String,Object> paras);

    List<AppRecruitJobResume> queryJobResume(@Param("paras") AppRecruitJobResume build,@Param("page") Page page);

    int queryCount(@Param("paras") AppRecruitJobResume build);

    @Select("SELECT IFNULL(sum(a.COMMISSION),0) from app_recruit_job_resume a where PARTY_ID=#{partyId} and a.STATUS = #{status} and IS_DELETED = 'N'")
    Integer queryUserReward(@Param("partyId") Long partyId,@Param("status") String status);
}
