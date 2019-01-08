package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.RecruitJobReq;
import com.xianglin.appserv.common.service.facade.req.RecruitJobResumeReq;
import com.xianglin.appserv.common.service.facade.req.RecruitResumeReq;

import java.util.List;
import java.util.Map;

/**
 * Describe :
 * Created by xingyali on 2018/4/19 10:57.
 * Update reason :
 */
public interface RecruitService {

    /**
     * 招工列表查询
     *
     * @param recruitJobReq
     * @return
     */
    Response<List<RecruitJobVo>> queryRecruitJobList(RecruitJobReq recruitJobReq);


    /**
     *  招工列表查询V2
     * @param recruitJobReq
     * @return
     */
    Response<List<RecruitJobV2>> queryRecruitJobList2(RecruitJobReq recruitJobReq);

    /**
     * 招工列表总数
     *
     * @param recruitJobReq
     * @return
     */
    Response<Integer> queryRecruitJobCount(RecruitJobReq recruitJobReq);

    /**
     * 查招工详情
     *
     * @param id
     * @return
     */
    Response<RecruitJobVo> queryRecruitJobById(String type, Long id);

    /**
     * 发布招工
     *
     * @param recruitJobVo
     * @return
     */
    Response<Boolean> publishRecruitJob(RecruitJobVo recruitJobVo);

    /**
     * 投递简历
     *
     * @param jobId
     * @param resumeId
     * @return
     */
    Response<Boolean> publishRecruitJobResume(Long jobId, Long resumeId);

    /**
     * 发布个人简历、推荐简历、求职意向
     *
     * @param recruitResumeVo
     * @return
     */
    Response<RecruitResumeVo> publishRecruitResume(RecruitResumeVo recruitResumeVo);

    /**
     * 根据简历id查询个人简历/推荐简历/求职意向
     *
     * @param id
     * @return
     */
    Response<RecruitResumeVo> queryRecruitResumeById(Long id);

    /**
     * 根据类型查当前用户的个人简历/推荐简历/求职意向
     *
     * @param type
     * @return
     */
    Response<List<RecruitResumeVo>> queryRecruitResumeListByType(String type, PageReq req);

    /**
     * 修改简历和被推荐人简历
     *
     * @param recruitResumeVo
     * @return
     */
    Response<Boolean> updateRecruitResume(RecruitResumeVo recruitResumeVo);

    /**
     * 返回是否显示我的招聘、我的求职、我的推荐
     */
    Response<Map<String, Object>> isShowRecruitJob();

    /**
     * 查询当前用户的求职或推荐
     *
     * @param type
     * @return
     */
    Response<List<RecruitJobVo>> queryJobRecommend(String type, PageReq req);

    /**
     * 删除招工
     *
     * @param id
     * @return
     */
    Response<Boolean> deleteRecruitJob(Long id);

    /**
     * 删除简历
     * @param id
     * @return
     */
    Response<Boolean> deleteResruitResume(Long id);

    /**
     * top 修改招工
     *
     * @param recruitJobVo
     * @return
     */
    Response<Boolean> updateRecruitJob(RecruitJobVo recruitJobVo);

    /**
     * top 根据职位的id查询应聘人员简历
     *
     * @param recruitJobResumeReq
     * @return
     */
    Response<List<RecruitResumeVo>> queryRecruitResumeListByJobResume(RecruitJobResumeReq recruitJobResumeReq);

    /**
     * top 根据职位的id查询应聘人员简历数
     *
     * @param recruitJobResumeReq
     * @return
     */
    Response<Integer> queryRecruitResumeCountByJobResume(RecruitJobResumeReq recruitJobResumeReq);

    /**
     * 修改录用状态
     *
     * @param recruitJobResumeVo
     * @return
     */
    Response<Boolean> updateJobResumeStatus(RecruitJobResumeVo recruitJobResumeVo);

    /**
     * 查询求职人员
     *
     * @param recruitResumeReq
     * @return
     */
    Response<List<RecruitResumeVo>> queryRecruitResume(RecruitResumeReq recruitResumeReq);

    /**
     * 查询求职人员数
     *
     * @param recruitResumeReq
     * @return
     */
    Response<Integer> queryRecruitResumeCount(RecruitResumeReq recruitResumeReq);

    /**
     * 根据用户的partyId查询投递记录
     *
     * @param recruitJobResumeReq
     * @return
     */
    Response<List<RecruitJobVo>> queryRecruitJobByJobResume(RecruitJobResumeReq recruitJobResumeReq);

    /**
     * 根据用户的partyId查询投递记录总数
     *
     * @param recruitJobResumeReq
     * @return
     */
    Response<Integer> queryRecruitJobCountByJobResume(RecruitJobResumeReq recruitJobResumeReq);

    /**
     * 招工举报
     *
     * @param id
     * @param reason
     * @param message
     * @return
     */
    Response<Boolean> reportRecruitJobByArticleId(Long id, String reason, String message);

    /**
     * 回显举报
     *
     * @param id
     * @return
     */
    Response<Map<String, String>> getReportDetail(Long id);

    /**
     * 新增招工草稿
     * @param recruitJobVo
     * @return
     */
    Response<Boolean> publishRecruitJobDraft(String recruitJobVo);

    /**
     * 查询当前登录用户的招工草稿
     * @return
     */
    Response<String> queryRecruitJobDraft();

    /**
     *清除当前登录用户的招工草稿
     * @return
     */
    Response<Boolean> cleanRecruitJobDraft();

    /**
     * 查询推荐招聘
     * @return
     */
    Response<List<RecruitJobVo>> queryRecommendJob(RecruitJobReq recruitJobReq);

    /**
     * 查询岗位申请进度
     * @param jobResumeId
     * @return
     */
    Response<Map<String,Object>> queryJobResumeTipsByJobResumeId(Long jobResumeId);

    /**
     * 保存用户收款账户信息
     * @param userRecommendedVo
     * @return
     */
    Response<Boolean> insertUserReceiptAccount(UserRecommendedVo userRecommendedVo);

    /**
     * 查询收款账户信息
     * @return
     */
    Response<UserRecommendedVo> queryUserReceiptAccount();

    /**
     * 查询用户获取佣金信息
     * @return
     */
    Response<Map<String,Object>> queryUserReward();

    /** 查询全部有奖推荐简历
     * for top
     * @return
     */
    Response<List<Map<String,Object>>> queryAllCommission();

    /**有奖推荐统计查询
     * @return
     */
    Response<List<Map<String,Object>>> queryAllCommissionTotal();

    /**
     * 查询用户是否拥有发布带有佣金的职位的权限
     * @return
     */
    Response<Boolean> queryUserRecruitJurisdiction();

    /**
     * 查询用户推荐列表有奖/普通
     * @param isCommission
     * @param req
     * @return
     */
    Response<List<RecruitJobVo>> queryJobRecommendByCommission(String isCommission, PageReq req);

    /**
     * 招工列表查询 top
     *
     * @param recruitJobReq
     * @return
     */
    Response<List<RecruitJobVo>> queryTOPRecruitJobList(RecruitJobReq recruitJobReq);

    /**
     * top端发布招聘
     * @param recruitJobVo
     * @return
     */
    Response<Boolean> publishTopRecruitJob(RecruitJobVo recruitJobVo);

    /**
     * 查询所有的求职人员
     * @return
     */
    Response<List<RecruitResumeVo>> queryRecruitResumeAll(RecruitResumeReq recruitResumeReq);
}
