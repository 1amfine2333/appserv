package com.xianglin.appserv.common.dal.daointerface;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xianglin.appserv.common.dal.dataobject.AppArticle;
import com.xianglin.appserv.common.dal.dataobject.AppDateSectionReq;
import com.xianglin.appserv.common.dal.dataobject.AppRecruitJob;
import com.xianglin.appserv.common.dal.dataobject.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AppRecruitJobDAO extends BaseMapper<AppRecruitJob> {

    List<AppRecruitJob> query(@Param("paras")AppRecruitJob appRecruitJob, @Param("appDateSectionReq")AppDateSectionReq appDateSectionReq, @Param("orderBy")String orderBy,@Param("jobCategorys")List<String> jobCategorys,@Param("expectCitys")List<String> expectCitys,@Param("isCommission")String isCommission);

    int queryCount(@Param("paras")AppRecruitJob appRecruitJob, @Param("appDateSectionReq")AppDateSectionReq appDateSectionReq,@Param("isCommission")String isCommission);

    List<AppRecruitJob> queryJobRecommendList(Map<String,Object> param);

    AppRecruitJob queryRecruitJobByArticleId(Long id);

    int queryJobRecommendCount(Map<String,Object> param);

    int updateJobApplyCount(Long jobId);
}
