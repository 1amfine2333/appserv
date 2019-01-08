/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

/**
 * 
 * 赚钱功能数据对象
 * @author zhangyong 2016年8月17日上午10:12:11
 */
public class EarningDTO extends BaseVo {

	/**  */
	private static final long serialVersionUID = -6732767864356079662L;

	private ProfitDTO profitDto;
	
	private ProfitDetailDTO profitDetailDto;

	private List<BusinessDTO> businessDtoList;

	private BusiVisitDTO busiVisisDto;
	
	public BusiVisitDTO getBusiVisisDto() {
		return busiVisisDto;
	}

	public void setBusiVisisDto(BusiVisitDTO busiVisisDto) {
		this.busiVisisDto = busiVisisDto;
	}

	public ProfitDTO getProfitDto() {
		return profitDto;
	}

	public void setProfitDto(ProfitDTO profitDto) {
		this.profitDto = profitDto;
	}

	public ProfitDetailDTO getProfitDetailDto() {
		return profitDetailDto;
	}

	public void setProfitDetailDto(ProfitDetailDTO profitDetailDto) {
		this.profitDetailDto = profitDetailDto;
	}

	public List<BusinessDTO> getBusinessDtoList() {
		return businessDtoList;
	}

	public void setBusinessDtoList(List<BusinessDTO> businessDtoList) {
		this.businessDtoList = businessDtoList;
	}
	
	
}
