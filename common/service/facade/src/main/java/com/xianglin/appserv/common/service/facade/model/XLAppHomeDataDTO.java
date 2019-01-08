/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.facade.model.vo.StationVo;
import com.xianglin.appserv.common.service.facade.model.vo.TotalSolarDataVo;

/**
 * 
 * 乡邻APP首页数据
 * @author zhangyong 2016年8月11日下午5:24:57
 */
public class XLAppHomeDataDTO extends BaseVo {

	
	/**  */
	private static final long serialVersionUID = -8392907053900935838L;
	private ProfitDTO profitDto;
	private List<BanerDTO> banerDtoList;
	private List<BusinessDTO> businessDtoList;
	private MessageDTO messageDto;
	private List<MessageDTO> msgDtoList;
	private MsgVo msgVo;
	private List<MsgVo> msgVoList;
	/** 银行业绩 */
	private String bankBalance;
	
	/** 银行业绩统计时间 */
	private String bankSumDate;
	/** 开卡数 */
	private Integer cardCount;
	
	/**	光伏信息 **/
	private List<StationVo> stations;
	
	/**	新装光伏列表10天之内的信息**/
	private List<StationVo> newStatiosList;
	
	private TotalSolarDataVo solarData;
	
	/** 红包信息**/
	private String amount;
	
	/** 是否已领取**/
	private String isFlag;
	
	public ProfitDTO getProfitDto() {
		return profitDto;
	}
	public void setProfitDto(ProfitDTO profitDto) {
		this.profitDto = profitDto;
	}
	public MessageDTO getMessageDto() {
		return messageDto;
	}
	public void setMessageDto(MessageDTO messageDto) {
		this.messageDto = messageDto;
	}
	public List<MessageDTO> getMsgDtoList() {
		return msgDtoList;
	}
	public void setMsgDtoList(List<MessageDTO> msgDtoList) {
		this.msgDtoList = msgDtoList;
	}
	public MsgVo getMsgVo() {
		return msgVo;
	}
	public void setMsgVo(MsgVo msgVo) {
		this.msgVo = msgVo;
	}
	public List<MsgVo> getMsgVoList() {
		return msgVoList;
	}
	public void setMsgVoList(List<MsgVo> msgVoList) {
		this.msgVoList = msgVoList;
	}
	
	public List<BanerDTO> getBanerDtoList() {
		return banerDtoList;
	}
	public void setBanerDtoList(List<BanerDTO> banerDtoList) {
		this.banerDtoList = banerDtoList;
	}
	public List<BusinessDTO> getBusinessDtoList() {
		return businessDtoList;
	}
	public void setBusinessDtoList(List<BusinessDTO> businessDtoList) {
		this.businessDtoList = businessDtoList;
	}
	public String getBankBalance() {
		return bankBalance;
	}
	public void setBankBalance(String bankBalance) {
		this.bankBalance = bankBalance;
	}
	public Integer getCardCount() {
		return cardCount;
	}
	public void setCardCount(Integer cardCount) {
		this.cardCount = cardCount;
	}
	public String getBankSumDate() {
		return bankSumDate;
	}
	public void setBankSumDate(String bankSumDate) {
		this.bankSumDate = bankSumDate;
	}
	public List<StationVo> getStations() {
		return stations;
	}
	public void setStations(List<StationVo> stations) {
		this.stations = stations;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getIsFlag() {
		return isFlag;
	}
	public void setIsFlag(String isFlag) {
		this.isFlag = isFlag;
	}
	public List<StationVo> getNewStatiosList() {
		return newStatiosList;
	}
	public void setNewStatiosList(List<StationVo> newStatiosList) {
		this.newStatiosList = newStatiosList;
	}
	public TotalSolarDataVo getSolarData() {
		return solarData;
	}
	public void setSolarData(TotalSolarDataVo solarData) {
		this.solarData = solarData;
	}
}

