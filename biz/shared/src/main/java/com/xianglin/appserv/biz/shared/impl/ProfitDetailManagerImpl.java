/**
 * 
 */
package com.xianglin.appserv.biz.shared.impl;

import com.xianglin.appserv.biz.shared.ProfitDetailManager;
import com.xianglin.appserv.biz.shared.util.LoginAttrUtil;
import com.xianglin.appserv.common.dal.daointerface.MonthAchieveDAO;
import com.xianglin.appserv.common.dal.dataobject.MonthAchieve;
import com.xianglin.appserv.common.service.facade.model.ProfitDetailDTO;
import com.xianglin.appserv.common.service.facade.model.enums.MonthAchieveType;
import com.xianglin.appserv.common.service.facade.model.vo.req.MonthAchieveQuery;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.NumberUtil;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * 
 * @author zhangyong 2016年8月17日上午10:41:00
 */
@Service
public class ProfitDetailManagerImpl implements ProfitDetailManager {

	private static final Logger logger = LoggerFactory.getLogger(ProfitDetailManagerImpl.class);

	@Autowired
	private MonthAchieveDAO monthAchieveDAO;

	@Autowired
	private LoginAttrUtil loginAttrUtil;
	/**
	 * 
	 * @see com.xianglin.appserv.biz.shared.ProfitDetailManager#getProfitDetailList(com.xianglin.appserv.common.service.facade.model.vo.req.MonthAchieveQuery,Long)
	 */
	@Override
	public List<ProfitDetailDTO> getProfitDetailList(MonthAchieveQuery monthQuery,Long partyId) {
		List<ProfitDetailDTO> alist = new ArrayList<>();
		Map<String, Object> param;
		try {
			param = DTOUtils.beanToMap(monthQuery);
			param.put("partyId",loginAttrUtil.getPartyId());
			String clientVersion =loginAttrUtil.getSessionStr(SessionConstants.CLIENT_VERSION);

			List<MonthAchieve> list = monthAchieveDAO.select(param);

			if (!CollectionUtils.isEmpty(list)) {
				Map<String, Map<String, Object>> map = new LinkedHashMap<>();
				for (MonthAchieve monthAchieve : list) {
					String key = monthAchieve.getYear().toString().concat("-")
							.concat(StringUtils.leftPad(monthAchieve.getMonth().toString(), 2, "0"));
					dealMap(map, key, monthAchieve.getBusiType(), monthAchieve.getTotal());
				}
				
				for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
					ProfitDetailDTO detailDto = new ProfitDetailDTO();
					detailDto.setYear(entry.getKey().substring(0, 4));
					detailDto.setMonth(entry.getKey().substring(5));
					Map<String, Object> m = entry.getValue();
					if(StringUtils.equals(clientVersion,"1.1.0")|| StringUtils.isEmpty(clientVersion)){
						detailDto.setBankProfit(convert(m.get(MonthAchieveType.BANK.name())));// 银行佣金
						detailDto.setLoanProfit(convert(m.get(MonthAchieveType.LOAN.name())));// 贷款佣金
						BigDecimal echarge = convert(m.get(MonthAchieveType.ERCHARGE.name()));
						BigDecimal pay = convert(m.get(MonthAchieveType.PAY.name()));
						if(echarge !=null && pay !=null){
							detailDto.setLiveEchargeProfit(echarge.add(pay));// 生活缴费佣金(1.2.0以下版本手机充值和生活缴费在一起)
						}else if(echarge !=null && pay ==null){
							detailDto.setLiveEchargeProfit(echarge);
						}else{
							detailDto.setLiveEchargeProfit(pay);
						}

					}else{
						detailDto.setMobileEchargeProfit(convert(m.get(MonthAchieveType.ERCHARGE.name())));// 手机充值佣金
						detailDto.setLiveEchargeProfit(convert(m.get(MonthAchieveType.PAY.name())));// 生活缴费佣金
					}

					detailDto.setEshopProfit(convert(m.get(MonthAchieveType.ESHOP.name())));// 电商佣金
					detailDto.setTotalProfit(convert(detailDto.getTotal()));
					alist.add(detailDto);
				}
			}
		} catch (Exception e) {
			logger.error("查询收益统计失败", e);
		}

		return alist;
	}

	private void dealMap(Map<String, Map<String, Object>> map, String key, String transactionType, BigDecimal amount) {

		if (map.containsKey(key)) {
			Map<String, Object> transTypeAndAmount = map.get(key);
			transTypeAndAmount.put(transactionType, amount);
		} else {
			Map<String, Object> transTypeAndAmount = new HashMap<>();
			transTypeAndAmount.put(transactionType, amount);
			map.put(key, transTypeAndAmount);
		}
	}

	private BigDecimal convert(Object o) {
		if(null != o){
			BigDecimal b = new BigDecimal(o.toString());
			return	NumberUtil.truncateBigDecimal(b, 2);
		}
		return null;
	}

}
