package com.xianglin.appserv.biz.shared.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xianglin.appserv.biz.shared.RecruitManager;
import com.xianglin.appserv.common.dal.daointerface.AppRecruitJobDAO;
import com.xianglin.appserv.common.dal.daointerface.AppRecruitJobResumeDAO;
import com.xianglin.appserv.common.dal.daointerface.AppRecruitJobResumeTipDAO;
import com.xianglin.appserv.common.dal.daointerface.AppRecruitResumeDAO;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.util.EmojiEscapeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Describe :
 * Created by xingyali on 2018/4/19 11:00.
 * Update reason :
 */
@Service
public class RecruitManagerImpl implements RecruitManager {

    @Autowired
    private AppRecruitJobDAO appRecruitJobDAO;

    @Autowired
    private AppRecruitResumeDAO appRecruitResumeDAO;

    @Autowired
    private AppRecruitJobResumeDAO appRecruitJobResumeDAO;

    @Autowired
    private AppRecruitJobResumeTipDAO appRecruitJobResumeTipDAO;

    @Override
    public List<AppRecruitJob> queryRecruitJobList(AppRecruitJob appRecruitJob, AppDateSectionReq appDateSectionReq, String orderBy, List<String> jobCategorys,List<String> expectCitys,String isCommission) {
        List<AppRecruitJob> recruitJobList = appRecruitJobDAO.query(appRecruitJob, appDateSectionReq, orderBy, jobCategorys,expectCitys, isCommission);
        //反转义emoji by songjialin
        recruitJobList = recruitJobList.stream()
                .map(input -> {
                    EmojiEscapeUtil.deEscapeEmojiString(input);
                    return input;
                })
                .collect(Collectors.toList());
        return recruitJobList;
    }

    @Override
    public AppRecruitJob queryRecruitJobById(Long id) {
        AppRecruitJob appRecruitJob = appRecruitJobDAO.selectById(id);
        EmojiEscapeUtil.deEscapeEmojiString(appRecruitJob);
        return appRecruitJob;
    }

    @Override
    public Boolean insertRecruitJob(AppRecruitJob appRecruitJob) {
        return appRecruitJobDAO.insert(appRecruitJob) == 1;
    }

    @Override
    public Boolean insertRecruitResume(AppRecruitResume appRecruitResume) {
        return appRecruitResumeDAO.insert(appRecruitResume) == 1;
    }

    @Override
    public List<AppRecruitResume> queryRecruitResume(Map<String, Object> param) {
        List<AppRecruitResume> recruitResumes = appRecruitResumeDAO.query(param);
        //反转义emoji by songjialin
        recruitResumes = recruitResumes.stream()
                .map(input -> {
                    EmojiEscapeUtil.deEscapeEmojiString(input);
                    return input;
                })
                .collect(Collectors.toList());
        return recruitResumes;
    }

    @Override
    public List<AppRecruitJobResume> queryRecruitJobResumeList(Map<String, Object> paras) {
        List<AppRecruitJobResume> recruitJobResumes = appRecruitJobResumeDAO.query(paras);
        //反转义emoji by songjialin
        recruitJobResumes = recruitJobResumes.stream()
                .map(input -> {
                    EmojiEscapeUtil.deEscapeEmojiString(input);
                    return input;
                })
                .collect(Collectors.toList());
        return recruitJobResumes;
    }

    @Override
    public AppRecruitResume queryRecruitResumeById(Long id) {
        AppRecruitResume appRecruitResume = appRecruitResumeDAO.selectById(id);
        EmojiEscapeUtil.deEscapeEmojiString(appRecruitResume);
        return appRecruitResume;
    }

    @Override
    public int queryRecruitJobCount(AppRecruitJob appRecruitJob, AppDateSectionReq appDateSectionReq, String isCommission) {
        return appRecruitJobDAO.queryCount(appRecruitJob, appDateSectionReq, isCommission);
    }

    @Override
    public Boolean insertRecruitJobResume(AppRecruitJobResume appRecruitJobResume) {
        return appRecruitJobResumeDAO.insert(appRecruitJobResume) == 1;
    }

    @Override
    public List<AppRecruitJob> queryJobRecommendList(Map<String, Object> param) {
        List<AppRecruitJob> jobList = appRecruitJobDAO.queryJobRecommendList(param);
        //反转义emoji by songjialin
        jobList = jobList.stream()
                .map(input -> {
                    EmojiEscapeUtil.deEscapeEmojiString(input);
                    return input;
                })
                .collect(Collectors.toList());
        return jobList;
    }

    @Override
    public Boolean updateRecruitResume(AppRecruitResume appRecruitResume) {
        if (StringUtils.isNotEmpty(appRecruitResume.getRecentJob())) {
            appRecruitResume.setRecentJob(EmojiEscapeUtil.escapeEmoji2Base64String(appRecruitResume.getRecentJob()));
            //EmojiEscapeUtil.escapeEmoji2Base64String(appRecruitResume.getRecentJob());
        }
        return appRecruitResumeDAO.updateById(appRecruitResume) == 1;
    }

    @Override
    public Boolean updateRecruitJob(AppRecruitJob appRecruitJob) {
        return appRecruitJobDAO.updateById(appRecruitJob) == 1;
    }

    @Override
    public AppRecruitJob queryRecruitJobByArticleId(Long id) {
        AppRecruitJob appRecruitJob = appRecruitJobDAO.queryRecruitJobByArticleId(id);
        EmojiEscapeUtil.deEscapeEmojiString(appRecruitJob);
        return appRecruitJob;
    }

    @Override
    public List<AppRecruitResume> queryJobResumeByParam(Map<String, Object> param) {
        List<AppRecruitResume> recruitResumes = appRecruitResumeDAO.queryJobResumeByParam(param);
        recruitResumes = recruitResumes.stream()
                .map(input -> {
                    EmojiEscapeUtil.deEscapeEmojiString(input);
                    return input;
                })
                .collect(Collectors.toList());
        return recruitResumes;
    }

    @Override
    public List<AppRecruitJobResume> queryJobResume(AppRecruitJobResume build, Page page) {
        return appRecruitJobResumeDAO.queryJobResume(build, page);
    }

    @Override
    public Boolean updateRecruitJobResume(AppRecruitJobResume appRecruitJobResume) {
        return appRecruitJobResumeDAO.updateById(appRecruitJobResume) == 1;
    }

    @Override
    public List<AppRecruitResume> queryResumeByParam(AppRecruitResume appRecruitResume, Page page, String orderBy,String isResume) {
        List<AppRecruitResume> recruitResumes = appRecruitResumeDAO.queryResumeByParam(appRecruitResume, page, orderBy,isResume);
        recruitResumes = recruitResumes.stream()
                .map(input -> {
                    EmojiEscapeUtil.deEscapeEmojiString(input);
                    return input;
                })
                .collect(Collectors.toList());
        return recruitResumes;
    }

    @Override
    public int queryRecruitJobResumeCount(AppRecruitJobResume build) {
        return appRecruitJobResumeDAO.queryCount(build);
    }

    @Override
    public int queryJobRecommendCount(Map<String, Object> param) {
        return appRecruitJobDAO.queryJobRecommendCount(param);
    }

    @Override
    public int queryResumeCount(Map<String, Object> param) {
        return appRecruitResumeDAO.queryJobResumeCount(param);
    }

    @Override
    public int queryResumeCountByParam(AppRecruitResume appRecruitResume,String isResume) {
        return appRecruitResumeDAO.queryResumeCountByParam(appRecruitResume,isResume);
    }

    @Override
    public Map<String, BigDecimal> queryPersonalAndRecommedResumeCount(Map<String, Object> map) {
        return appRecruitResumeDAO.queryPersonalAndRecommedResumeCount(map);
    }

    @Override
    public Boolean updateRecruitJobApplyCount(Long jobId) {
        return appRecruitJobDAO.updateJobApplyCount(jobId) == 1;
    }

    /**
     * 查简历
     *
     * @param build
     * @return
     */
    @Override
    public List<AppRecruitResume> queryRecruitResumeByResume(AppRecruitResume build) {
        EntityWrapper<AppRecruitResume> ew = new EntityWrapper();
        ew.eq(build.getPartyId() != null, "PARTY_ID", build.getPartyId());
        ew.eq(StringUtils.isNoneEmpty(build.getType()), "TYPE", build.getType());
        List<AppRecruitResume> recruitResumes = appRecruitResumeDAO.selectList(ew);
        return recruitResumes;
    }

    @Override
    public int insertAppRecruitJobResumeTip(AppRecruitJobResumeTip appRecruitJobResumeTip) {
        return appRecruitJobResumeTipDAO.insert(appRecruitJobResumeTip);
    }

    @Override
    public List<AppRecruitJobResumeTip> queryAppRecruitJobResumeTipsByJobResumeId(Long jobResumeId) {
        EntityWrapper<AppRecruitJobResumeTip> ew = new EntityWrapper<>();
        ew.eq(jobResumeId != null, "JOB_RESUME_ID", jobResumeId);
        ew.orderBy("CREATE_TIME", false);
        List<AppRecruitJobResumeTip> appRecruitJobResumeTips = appRecruitJobResumeTipDAO.selectList(ew);
        return appRecruitJobResumeTips;
    }

    /**
     * 同步待处理人数
     *
     * @param id
     * @return
     */
    @Override
    public int syncStayApplyNum(Long id) {
        EntityWrapper<AppRecruitJobResume> ew = new EntityWrapper<>();
        ew.eq(id != null, "JOB_ID", id);
        ew.eq("STATUS", "H");
        Integer integer = appRecruitJobResumeDAO.selectCount(ew);
        appRecruitJobDAO.updateById(AppRecruitJob.builder().id(id).stayApplyNum(integer).build());
        return integer;
    }

    @Override
    public Integer queryUserReward(Long partyId, String status) {
        return appRecruitJobResumeDAO.queryUserReward(partyId, status);
    }

    @Override
    public List<AppRecruitJobResume> queryJobResumeList(Long partyId, String status, Boolean commissionList, String orderBy, Page page) {
        EntityWrapper<AppRecruitJobResume> ew = new EntityWrapper<>();
        ew.eq(partyId != null, "PARTY_ID", partyId);
        ew.like(StringUtils.isNotEmpty(status), "STATUS", status);
        if (commissionList != null) {
            if (commissionList) {
                ew.andNew().isNull("COMMISSION").or().eq("COMMISSION",0);
            } else {
                ew.gt("COMMISSION",0);
            }
        }
        ew.orderBy(StringUtils.isNotEmpty(orderBy), orderBy);
        ew.orderBy("ID", false);
        if (page != null) {
            return appRecruitJobResumeDAO.selectPage(new RowBounds(page.getOffset(), page.getPageSize()), ew);
        } else {
            return appRecruitJobResumeDAO.selectList(ew);
        }
    }

    @Override
    public AppRecruitJobResume queryjobResumeById(Long id) {
        return appRecruitJobResumeDAO.selectById(id);
    }

    @Override
    public Integer queryJobResumeByJobResume(AppRecruitJobResume build) {
        return appRecruitJobResumeDAO.queryCount(build);
    }

    @Override
    public Boolean updateAllRecruitResumeById(AppRecruitResume appRecruitResume) {
        return appRecruitResumeDAO.updateAllColumnById(appRecruitResume)==1;
    }
}
