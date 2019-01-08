/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 网点收益业绩数据
 * @author zhangyong 2016年8月11日下午4:36:05
 */
public class ProfitDTO implements Serializable {

	/**  */
	private static final long serialVersionUID = -8677880702907398257L;
	private Long partyId;
	private String rankType;//排名类型 国|省|市 等
	private Integer rank; //第几名
	private String busiType;//业务类型(银行|电商|贷款)
	private String staticType;//统计类型()
	private BigDecimal currentTotal;//当前统计
	private BigDecimal total;//总计
	private String dataPeriod;//数据时间
	
	public String getRankType() {
		return rankType;
	}
	public void setRankType(String rankType) {
		this.rankType = rankType;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public String getBusiType() {
		return busiType;
	}
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}
	public String getStaticType() {
		return staticType;
	}
	public void setStaticType(String staticType) {
		this.staticType = staticType;
	}
	public Long getPartyId() {
		return partyId;
	}
	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}
	public BigDecimal getCurrentTotal() {
		return currentTotal;
	}
	public void setCurrentTotal(BigDecimal currentTotal) {
		this.currentTotal = currentTotal;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public String getDataPeriod() {
		return dataPeriod;
	}
	public void setDataPeriod(String dataPeriod) {
		this.dataPeriod = dataPeriod;
	}
	@Override
	public String toString() {
		return "ProfitDTO [partyId=" + partyId + ", rankType=" + rankType + ", rank=" + rank + ", busiType=" + busiType
				+ ", staticType=" + staticType + ", currentTotal=" + currentTotal + ", total=" + total + ", dataPeriod="
				+ dataPeriod + "]";
	}
	
	
	
}
