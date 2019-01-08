package com.xianglin.appserv.biz.shared;

import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.model.vo.UserRecommendedVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.RecruitJobReq;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Describe :
 * Created by xingyali on 2018/4/19 11:00.
 * Update reason :
 */
public interface RecruitManager {
    List<AppRecruitJob> queryRecruitJobList(AppRecruitJob appRecruitJob, AppDateSectionReq appDateSectionReq, String orderBy,List<String> jobCategorys,List<String> expectCitys,String isCommission);

    AppRecruitJob queryRecruitJobById(Long id);

    Boolean insertRecruitJob(AppRecruitJob appRecruitJob);

    Boolean insertRecruitResume(AppRecruitResume appRecruitResumt);

    List<AppRecruitResume> queryRecruitResume(Map<String,Object> param);

    List<AppRecruitJobResume> queryRecruitJobResumeList(Map<String,Object> paras);

    AppRecruitResume queryRecruitResumeById(Long id);

    int queryRecruitJobCount(AppRecruitJob appRecruitJob,AppDateSectionReq appDateSectionReq,String isCommission);

    Boolean insertRecruitJobResume(AppRecruitJobResume h);

    List<AppRecruitJob> queryJobRecommendList(Map<String,Object> param);

    Boolean updateRecruitResume(AppRecruitResume appRecruitResume);

    Boolean updateRecruitJob(AppRecruitJob appRecruitJob);

    AppRecruitJob queryRecruitJobByArticleId(Long id);

    List<AppRecruitResume> queryJobResumeByParam(Map<String,Object> param);

    List<AppRecruitJobResume> queryJobResume(AppRecruitJobResume build, Page page);

    Boolean updateRecruitJobResume(AppRecruitJobResume appRecruitJobResume);

    List<AppRecruitResume> queryResumeByParam(AppRecruitResume appRecruitResume, Page page,String orderBy,String isResume);

    int queryRecruitJobResumeCount(AppRecruitJobResume build);

    int queryJobRecommendCount(Map<String,Object> param);

    int queryResumeCount(Map<String,Object> param);

    int queryResumeCountByParam(AppRecruitResume appRecruitResume,String isResume);

    /**
     * 查个人当前用户的简历数个推荐简历数
     * @param map
     * @return
     */
    Map<String,BigDecimal> queryPersonalAndRecommedResumeCount(Map<String,Object> map);

    /**
     * 更新招聘的投递数量
     * @param jobId
     */
    Boolean updateRecruitJobApplyCount(Long jobId);

    /**
     * 查简历
     * @param build
     * @return
     */
    List<AppRecruitResume> queryRecruitResumeByResume(AppRecruitResume build);

    /**
     * 保存岗位申请进度提示信息
     * @param appRecruitJobResumeTip
     * @return
     */
    int insertAppRecruitJobResumeTip(AppRecruitJobResumeTip appRecruitJobResumeTip);

    /**
     * 查询岗位申请进度提示信息
     * @param jobResumeId
     * @return
     */
    List<AppRecruitJobResumeTip> queryAppRecruitJobResumeTipsByJobResumeId(Long jobResumeId);

    /**
     * 同步待处理人数
     * @param id
     * @return
     */
    int syncStayApplyNum(Long id);

    /**
     * 查询用户佣金信息
     * @return
     */
    Integer queryUserReward(Long partyId,String status);

    /**根据条件查询用户
     * @param partyId
     * @param status 为like查询
     * @param commissionList 是否有奖励，为空则不限 null:不限， true:为空 false:不为空
     * @param orderBy
     * @param page
     * @return
     */
    List<AppRecruitJobResume> queryJobResumeList(Long partyId,String status,Boolean commissionList,String orderBy,Page page);



    /**
     * 根据id查询投递进度
     * @param id
     * @return
     */
    AppRecruitJobResume queryjobResumeById(Long id);

    /**
     * 查询岗位的投递数
     * @param build
     * @return
     */
    Integer queryJobResumeByJobResume(AppRecruitJobResume build);

    /**
     * 全部字段修改
     * @param appRecruitResume
     * @return
     */
    Boolean updateAllRecruitResumeById(AppRecruitResume appRecruitResume);
}
