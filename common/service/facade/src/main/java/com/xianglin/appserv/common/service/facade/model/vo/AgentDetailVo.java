/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model.vo;

import java.io.Serializable;

/**
 * 站长
 * 	业绩与收益详情
 * 
 * @author leaf 2016年7月14日下午2:41:49
 */
public class AgentDetailVo extends BaseVo {
	
	
	/**  */
	private static final long serialVersionUID = 7236797152285509600L;
	
	/** 站点编号  */
	private Long nodePartyId;
	
	/** 余额  */
	private String balance;
	
	/** 银行卡数 */
	private Integer cardCount;
	
	/** 省内余额排名  */
	private Integer balanceRank;
	
	/** 省内银行卡数排名 */
	private Integer cardRank;
	
	/** 余额上升指标 */
	private Integer balanceTag;
	
	/** 银行卡数上升指标 */
	private Integer cardTag;
	
	/** 收益日期 */
	private String earningsDate;
	
	/** 当月收益 */
	private String monthOfEarnings;
	
	/** 累计收益 */
	private String totalOfEarnings;
	
	/** 献花数 */
	private Integer flowers;
	
	/** 打赏数 */
	private Integer rewards;


	private String operateUrl;
	private String profitUrl;

	private String myRankUrl;
	/**
	 * 打赏中间页，封装参数
	 */
	private String rewardParamUrl;
	/**
	 * @return the rewards
	 */
	public Integer getRewards() {
		return rewards;
	}

	/**
	 * @param rewards the rewards to set
	 */
	public void setRewards(Integer rewards) {
		this.rewards = rewards;
	}

	/**
	 * @return the flowers
	 */
	public Integer getFlowers() {
		return flowers;
	}

	/**
	 * @param flowers the flowers to set
	 */
	public void setFlowers(Integer flowers) {
		this.flowers = flowers;
	}

	/**
	 * @return the nodePartyId
	 */
	public Long getNodePartyId() {
		return nodePartyId;
	}

	/**
	 * @param nodePartyId the nodePartyId to set
	 */
	public void setNodePartyId(Long nodePartyId) {
		this.nodePartyId = nodePartyId;
	}


	/**
	 * @return the balanceRank
	 */
	public Integer getBalanceRank() {
		return balanceRank;
	}

	/**
	 * @param balanceRank the balanceRank to set
	 */
	public void setBalanceRank(Integer balanceRank) {
		this.balanceRank = balanceRank;
	}

	/**
	 * @return the cardRank
	 */
	public Integer getCardRank() {
		return cardRank;
	}

	/**
	 * @param cardRank the cardRank to set
	 */
	public void setCardRank(Integer cardRank) {
		this.cardRank = cardRank;
	}

	/**
	 * @return the balanceTag
	 */
	public Integer getBalanceTag() {
		return balanceTag;
	}

	/**
	 * @param balanceTag the balanceTag to set
	 */
	public void setBalanceTag(Integer balanceTag) {
		this.balanceTag = balanceTag;
	}

	/**
	 * @return the cardTag
	 */
	public Integer getCardTag() {
		return cardTag;
	}

	/**
	 * @param cardTag the cardTag to set
	 */
	public void setCardTag(Integer cardTag) {
		this.cardTag = cardTag;
	}
	
	/**
	 * @return the monthOfEarnings
	 */
	public String getMonthOfEarnings() {
		return monthOfEarnings;
	}

	/**
	 * @param monthOfEarnings the monthOfEarnings to set
	 */
	public void setMonthOfEarnings(String monthOfEarnings) {
		this.monthOfEarnings = monthOfEarnings;
	}

	/**
	 * @return the totalOfEarnings
	 */
	public String getTotalOfEarnings() {
		return totalOfEarnings;
	}

	/**
	 * @param totalOfEarnings the totalOfEarnings to set
	 */
	public void setTotalOfEarnings(String totalOfEarnings) {
		this.totalOfEarnings = totalOfEarnings;
	}

	/**
	 * @return the balance
	 */
	public String getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(String balance) {
		this.balance = balance;
	}

	/**
	 * @return the cardCount
	 */
	public Integer getCardCount() {
		return cardCount;
	}

	/**
	 * @param cardCount the cardCount to set
	 */
	public void setCardCount(Integer cardCount) {
		this.cardCount = cardCount;
	}

	/**
	 * @return the earningsDate
	 */
	public String getEarningsDate() {
		return earningsDate;
	}

	/**
	 * @param earningsDate the earningsDate to set
	 */
	public void setEarningsDate(String earningsDate) {
		this.earningsDate = earningsDate;
	}

	public String getOperateUrl() {
		return operateUrl;
	}

	public void setOperateUrl(String operateUrl) {
		this.operateUrl = operateUrl;
	}

	public String getProfitUrl() {
		return profitUrl;
	}

	public void setProfitUrl(String profitUrl) {
		this.profitUrl = profitUrl;
	}

	public String getMyRankUrl() {
		return myRankUrl;
	}

	public void setMyRankUrl(String myRankUrl) {
		this.myRankUrl = myRankUrl;
	}

	public String getRewardParamUrl() {
		return rewardParamUrl;
	}

	public void setRewardParamUrl(String rewardParamUrl) {
		this.rewardParamUrl = rewardParamUrl;
	}
}
