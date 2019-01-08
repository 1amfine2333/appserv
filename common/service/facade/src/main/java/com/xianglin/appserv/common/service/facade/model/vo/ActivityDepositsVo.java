package com.xianglin.appserv.common.service.facade.model.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wanglei on 2017/1/6.
 */
public class ActivityDepositsVo extends BaseVo{

    private Long id;

    private Long partyId;

    private String remainAchieve;

    private String currentAchieve;

    private String goalAchieve;

    /**
     * 进度，超过100则表示完成
     */
    private int progress;

    /**
     * 领取奖励标识
     * F:业绩未达到，不能领取奖励
     * N:业绩已达到，可以领取奖励
     * Y:已经领取过奖励
     */
    private String rewardSign;

    /**
     *当前时间
     */
    private Date currentDate = new Date();

    private long current = System.currentTimeMillis();

    /**
     *活动结束时间
     */
    private Date endDate;

    private long endTime;


    private String rewardResult;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getRemainAchieve() {
        return remainAchieve;
    }

    public void setRemainAchieve(String remainAchieve) {
        this.remainAchieve = remainAchieve;
    }

    public String getCurrentAchieve() {
        return currentAchieve;
    }

    public void setCurrentAchieve(String currentAchieve) {
        this.currentAchieve = currentAchieve;
    }

    public String getGoalAchieve() {
        return goalAchieve;
    }

    public void setGoalAchieve(String goalAchieve) {
        this.goalAchieve = goalAchieve;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getRewardSign() {
        return rewardSign;
    }

    public void setRewardSign(String rewardSign) {
        this.rewardSign = rewardSign;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRewardResult() {
        return rewardResult;
    }

    public void setRewardResult(String rewardResult) {
        this.rewardResult = rewardResult;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
