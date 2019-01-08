/**
 * 
 */
package com.xianglin.appserv.biz.shared.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.service.facade.model.vo.MonthAchieveVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.MonthAchieveQuery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xianglin.appserv.biz.shared.MonthAchieveManager;
import com.xianglin.appserv.biz.shared.ProfitManager;
import com.xianglin.appserv.common.service.facade.model.ProfitDTO;
import com.xianglin.appserv.common.service.facade.model.enums.MonthAchieveType;
import com.xianglin.appserv.common.util.NumberUtil;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.fala.session.Session;
import com.xianglin.te.common.service.facade.MonthlySummaryService;
import com.xianglin.te.common.service.facade.enums.Constants;
import com.xianglin.te.common.service.facade.enums.Constants.FacadeEnums;
import com.xianglin.te.common.service.facade.req.RequestReq;
import com.xianglin.te.common.service.facade.vo.MonthlySummaryVo;
import com.xianglin.xlnodecore.common.service.facade.SalaryImportService;
import com.xianglin.xlnodecore.common.service.facade.model.SalaryDTO;

/**
 * 
 * 收益
 * @author zhangyong 2016年8月15日下午6:07:10
 */
@Service
public class ProfitManagerImpl implements ProfitManager {
	private static final Logger logger =LoggerFactory.getLogger(ProfitManagerImpl.class);
	@Autowired//电商|充值收益
	private MonthlySummaryService teMonthlySummaryService;
	@Autowired//银行收益
	private SalaryImportService salaryImportService;
	@Autowired
	private SessionHelper sessionHelper;
	
	@Autowired
	private MonthAchieveManager monthAchieveManager;
	/**
	 * @see com.xianglin.appserv.biz.shared.ProfitManager#queryProfit(Long)
	 */
	@Override
	public ProfitDTO queryProfit(Long userPartyId) {
		Long partyId = getPartyId(userPartyId);
		//1.查询电商收益
		MonthlySummaryVo vo = new MonthlySummaryVo();
		vo.setPartyId(partyId);
		vo.setProductCode(Constants.ProductCode.EC.code);
		vo.setOrderApplication( Constants.ApplicationEnums.EC_APPLICATION.code);
		RequestReq<MonthlySummaryVo> req = new RequestReq<>(vo);
		com.xianglin.te.common.service.facade.resp.Response<List<Map<String,Object>>> ecResp = teMonthlySummaryService.querySumAmountGBYearAndMonth(req);
		com.xianglin.te.common.service.facade.resp.Response<BigDecimal> ecTotalResp = teMonthlySummaryService.querySumAmount(req);
		String ecMonth = "";
		BigDecimal ecProfit = BigDecimal.ZERO;
		BigDecimal ecTotal = BigDecimal.ZERO;
		if(FacadeEnums.OK.code.equals(String.valueOf(ecResp.getCode()))){
			Map<String,Object> firstMap = ecResp.getResult().get(0);
			String year = firstMap.get("summaryYear") == null ?"0":firstMap.get("summaryYear").toString();
			String month = firstMap.get("summaryMonth") == null ?"0":firstMap.get("summaryMonth").toString();
			ecMonth = year+StringUtils.leftPad(month, 2, "0");;
			ecProfit = new BigDecimal(firstMap.get("sumAmount").toString());
		}
		if(FacadeEnums.OK.code.equals(String.valueOf(ecTotalResp.getCode()))){
			ecTotal = ecTotalResp.getResult();
		}
		
		//2.查询银行收益
		String bankmonth = "";
		BigDecimal bankProfit = BigDecimal.ZERO;
		BigDecimal bankTotal = BigDecimal.ZERO;
		com.xianglin.xlnodecore.common.service.facade.base.Response<SalaryDTO> salaryResp = salaryImportService.queryByNodeMangerPartyId(userPartyId, null);
		if(FacadeEnums.OK.code.equals(String.valueOf(salaryResp.getCode()))){
			SalaryDTO salaryDto = salaryResp.getResult();
			bankmonth = salaryDto.getMonthName();
			bankProfit = salaryDto.getAmountPay();
			bankTotal = salaryDto.getTotalIncome();
		}
	
		//封装收益对象
		ProfitDTO profitDto = new ProfitDTO();
		profitDto.setStaticType(MonthAchieveType.INCOME.name());
		profitDto.setPartyId(userPartyId);
		profitDto.setTotal(bankTotal.add(ecTotal));
		profitDto.setDataPeriod(bankmonth);
		if(ecMonth.compareTo(bankmonth) > 0){//电商佣金月份大于银行收益月份，最近月份有电商无银行
			profitDto.setDataPeriod(ecMonth);
			profitDto.setCurrentTotal(ecProfit);
			
		}else if(ecMonth.compareTo(bankmonth)<0){//电商佣金月份小于银行收益月份，最近月份有银行无电商
			profitDto.setDataPeriod(bankmonth);
			profitDto.setCurrentTotal(bankProfit);
		}else{//有电商有银行数据
			profitDto.setCurrentTotal(bankProfit.add(ecProfit));
		}
		
		//3.查询充值收益 暂无
		 
				//4.查询贷款收益 暂无
			
		return StringUtils.isEmpty(profitDto.getDataPeriod())?null:profitDto;
	}
	/**
	 * @see com.xianglin.appserv.biz.shared.ProfitManager#queryLocalProfit(java.lang.Long,String,String)
	 */
	@Override
	public ProfitDTO queryLocalProfit(Long userPartyId,String busiType,String staticType) throws Exception {
		Long partyId = getPartyId(userPartyId);
		ProfitDTO profitDTO = new ProfitDTO();
		profitDTO.setPartyId(partyId);
		profitDTO.setBusiType(busiType);
		profitDTO.setStaticType(staticType);
		Map<String,Object> param = new HashMap<>();
		param.put("partyId", partyId);
		param.put("busiType", busiType);
		param.put("staticType", staticType);
	
		List<Map<String,Object>> monthAchieveList = monthAchieveManager.querySumAmountGroup(param);
		if(!CollectionUtils.isEmpty(monthAchieveList)){
			Map<String,Object> firstMap = monthAchieveList.get(0);
			String year = firstMap.get("year") == null ?"0":firstMap.get("year").toString();
			String month = firstMap.get("month") == null ?"0":firstMap.get("month").toString();
			profitDTO.setCurrentTotal(NumberUtil.truncateBigDecimal(new BigDecimal(firstMap.get("sumAmount").toString()),2));// 兼容1.3版本以下
			profitDTO.setDataPeriod(year.concat(StringUtils.leftPad(month, 2, "0")));
		}
		
		return StringUtils.isEmpty(profitDTO.getDataPeriod())?null:profitDTO;
	}


	@Override
	public ProfitDTO queryTopOne(Long userPartyId,List<String> busiTypes,String staticType) throws Exception {
		Long partyId = getPartyId(userPartyId);
		ProfitDTO profitDTO = new ProfitDTO();
		profitDTO.setPartyId(partyId);
		profitDTO.setStaticType(staticType);
		Map<String,Object> param = new HashMap<>();
		param.put("partyId", partyId);
		param.put("busiTypes", busiTypes);
		param.put("staticType", staticType);
		List<Map<String,Object>> monthAchieveList = monthAchieveManager.querySumAmountGroup(param);
		if(!CollectionUtils.isEmpty(monthAchieveList)){
			Map<String,Object> firstMap = monthAchieveList.get(0);
			String year = firstMap.get("year") == null ?"0":firstMap.get("year").toString();
			String month = firstMap.get("month") == null ?"0":firstMap.get("month").toString();
			profitDTO.setCurrentTotal(NumberUtil.truncateBigDecimal(new BigDecimal(firstMap.get("sumAmount").toString()),2));
			profitDTO.setDataPeriod(year.concat(StringUtils.leftPad(month, 2, "0")));
		}
		return StringUtils.isEmpty(profitDTO.getDataPeriod())?null:profitDTO;

	}

	@Override
	public BigDecimal queryTotalProfit(Long userPartyId,String busiType,String staticType){
		Long partyId = getPartyId(userPartyId);
		Map<String,Object> param = new HashMap<>();
		param.put("partyId", partyId);
		param.put("busiTypes", busiType);
		param.put("staticType", staticType);
		return monthAchieveManager.querySumAmount(param);
	
	}
	@Override
	public BigDecimal queryTotal(Long userPartyId,List<String>busiTypes,String staticType){
		Long partyId = getPartyId(userPartyId);
		Map<String,Object> param = new HashMap<>();
		param.put("partyId", partyId);
		param.put("busiTypes", busiTypes);
		param.put("staticType", staticType);
		return monthAchieveManager.querySumAmount(param);

	}
	public List<MonthAchieveVo> queryProfitList(Long userPartyId,String staticType){
		Long partyId = getPartyId(userPartyId);
		Map<String,Object> param = new HashMap<>();
		param.put("partyId", partyId);
		param.put("staticType", staticType);
		MonthAchieveQuery mq = new MonthAchieveQuery();
		mq.setStaticType(staticType);
		BigDecimal total = BigDecimal.ZERO;
		try {
			List<MonthAchieveVo> list =	monthAchieveManager.query(mq);
			return list;
		} catch (Exception e) {
			logger.error("查询");
		}

		return new ArrayList<>();
	}
	/**
	 * @see com.xianglin.appserv.biz.shared.ProfitManager#queryProfitList(java.lang.Long, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<ProfitDTO> queryProfitList(Long userPartyId, Integer start, Integer pageSize) {
		Long partyId = getPartyId(userPartyId);
		Map<String,Object> param = new HashMap<>();
		param.put("partyId", partyId);
		param.put("staticType", MonthAchieveType.INCOME.name());
		param.put("startPage", start == 0? 1:start);
		param.put("pageSize", pageSize);
		List<Map<String,Object>> monthAchieveList = monthAchieveManager.querySumAmountGroup(param);
		
		if(!CollectionUtils.isEmpty(monthAchieveList)){
			List<ProfitDTO> list = new ArrayList<>(monthAchieveList.size());
			for (Map<String, Object> map : monthAchieveList) {
				ProfitDTO profitDTO = new ProfitDTO();
				profitDTO.setPartyId(partyId);
				profitDTO.setStaticType(MonthAchieveType.INCOME.name());
				String year = map.get("year") == null ?"0":map.get("year").toString();
				String month = map.get("month") == null ?"0":map.get("month").toString();
				profitDTO.setCurrentTotal(NumberUtil.truncateBigDecimal(new BigDecimal(map.get("sumAmount").toString()),2));
				profitDTO.setDataPeriod(year.concat(StringUtils.leftPad(month, 2, "0")));
			}
			return list;
		}
		return new ArrayList<>();
		
	}
	
	
	private Long getPartyId(Long userPartyId){
		Session  session = sessionHelper.getSession();


		String userId = session.getAttribute(SessionConstants.PARTY_ID);

		if(userId != null){
			userPartyId = Long.valueOf(userId);
		}
		/*if(userPartyId == null){
			if (StringUtils.isBlank(userId)) {
				logger.warn("get userId from session is empty !sessionId:{} , userId :{} ", session.getId(), userId);
				return null;
			}
			userPartyId = Long.valueOf(userId);
		}*/
		return userPartyId;

	}

}
