/**
 *
 */
package com.xianglin.appserv.biz.shared.listener;

import java.math.BigDecimal;
import java.util.*;

import com.xianglin.appserv.common.util.PropertiesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.xianglin.appserv.common.dal.daointerface.MonthAchieveDAO;
import com.xianglin.appserv.common.dal.dataobject.MonthAchieve;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.model.enums.MonthAchieveType;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.te.common.service.facade.MonthlySummaryService;
import com.xianglin.te.common.service.facade.enums.Constants;
import com.xianglin.te.common.service.facade.resp.Response;
import com.xianglin.te.common.service.facade.vo.MonthlySummaryVo;
import com.xianglin.xlnodecore.common.service.facade.SalaryImportService;
import com.xianglin.xlnodecore.common.service.facade.model.SalaryDTO;

/**
 * @author wanglei 2016年8月16日上午10:53:27
 */
@Component
public class MonthAchieveInitListner implements ApplicationListener<UserMonthAchieveEvent> {

    private static final Logger logger = LoggerFactory.getLogger(MonthAchieveInitListner.class);

    @Autowired
    private MonthAchieveDAO monthAchieveDAO;

    @Autowired
    private MonthlySummaryService monthlySummaryService;

    @Autowired
    private SalaryImportService salaryImportService;

    private Set<Long> partyIds = new HashSet<>();

    private Calendar today = Calendar.getInstance();

    /**
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(UserMonthAchieveEvent event) {
        User user = event.getUser();
        logger.info("init user month achieve user {}", user);
        //判断是否切日
        Calendar day = Calendar.getInstance();
        if (day.get(Calendar.DAY_OF_MONTH) != today.get(Calendar.DAY_OF_MONTH)) {
            today = day;
            partyIds.clear();
        }
        if (!"prd".equals(PropertiesUtil.getProperty("env"))) {
            initMonthAchieve(user.getPartyId());
        } else {
            if (!partyIds.contains(user.getPartyId())) {
                initMonthAchieve(user.getPartyId());
                partyIds.add(user.getPartyId());
            }
        }

    }

    private void initMonthAchieve(Long partyId) {
        //默认起始年月
        Integer startYear = 2016;
        Integer startMonth = 5;
        //截止年月
        Calendar cal = Calendar.getInstance();
        Integer endYear = cal.get(Calendar.YEAR);
        Integer endMonth = cal.get(Calendar.MONTH) + 1;

        while (startMonth.intValue() <= endMonth && startYear.intValue() <= endYear) {
            //循环同步数据
            //1 查询银行

            //2 电商和缴费
            try {
                initTeTrans(partyId, startYear, startMonth);
                initBankSalary(partyId, startYear, startMonth);
            } catch (Exception e) {
                logger.error("initMonthAchieve errir", e);
            }
            //3 查询贷款

            startMonth++;
            if (startMonth == 13) {
                startYear++;
                startMonth = 1;
            }
        }

    }

    /**
     * 初始化电商和缴费数据
     *
     * @param partyId
     * @param year
     * @param month
     * @throws Exception
     */
    private void initTeTrans(Long partyId, Integer year, Integer month) throws Exception {
        MonthAchieve ma = new MonthAchieve();
        ma.setPartyId(partyId);
        ma.setYear(year);
        ma.setMonth(month);
        ma.setStaticType(MonthAchieveType.INCOME.name());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("partyId", partyId);
        paramMap.put("summaryYear", year);
        paramMap.put("summaryMonth", month);
        Response<List<MonthlySummaryVo>> resp = monthlySummaryService.queryMonthlySummary(paramMap);
        if (CollectionUtils.isNotEmpty(resp.getResult())) {
            List<MonthlySummaryVo> result = resp.getResult();
            Map<String, BigDecimal> achieveMap = dealMonthlySummary(result);
            for (Map.Entry<String, BigDecimal> entry : achieveMap.entrySet()) {
                ma.setBusiType(entry.getKey());
                ma.setTotal(entry.getValue());
                saveUpdate(ma);
            }
           /* for (MonthlySummaryVo vo : result) {
                if (StringUtils.equals(vo.getTransactionType(), Constants.TransactionTypeEnums.TRANS_TYPE_500020.getCode())) {//充值佣金
                    ma.setBusiType(MonthAchieveType.ERCHARGE.name());
                    ma.setTotal(vo.getTransactionAmount());
                    saveUpdate(ma);
                    continue;
                } else if (StringUtils.equals(vo.getTransactionType(), Constants.TransactionTypeEnums.TRANS_TYPE_500070.getCode())) {//水电煤
                    ma.setBusiType(MonthAchieveType.PAY.name());
                    ma.setTotal(vo.getTransactionAmount());
                    saveUpdate(ma);
                    continue;
                }
                if (StringUtils.startsWith(vo.getTransactionType(), "500")) {
                    total = total.add(vo.getTransactionAmount());
                }
            }
            ma.setBusiType(MonthAchieveType.ESHOP.name());
            ma.setTotal(total);
            saveUpdate(ma);*/
        }
    }

    private Map<String, BigDecimal> dealMonthlySummary(List<MonthlySummaryVo> list) {
        Map<String, BigDecimal> map = new HashMap<>();
        for (MonthlySummaryVo monthlySummaryVo : list) {
            String transType = monthlySummaryVo.getTransactionType();
            BigDecimal amount = monthlySummaryVo.getTransactionAmount();

            // 佣金显示范围
            if ((Constants.TransactionTypeEnums.TRANS_TYPE_500010.code.equals(monthlySummaryVo.getTransactionType()))
                    || (Constants.TransactionTypeEnums.TRANS_TYPE_500011.code.equals(monthlySummaryVo.getTransactionType()))
                    || (Constants.TransactionTypeEnums.TRANS_TYPE_500020.code.equals(monthlySummaryVo.getTransactionType()))
                    || (Constants.TransactionTypeEnums.TRANS_TYPE_500071.code.equals(monthlySummaryVo.getTransactionType()))
                    || (Constants.TransactionTypeEnums.TRANS_TYPE_500070.code.equals(monthlySummaryVo.getTransactionType()))) {
                dealMap(map, transType, amount);
            }
        }
        BigDecimal salebrokerageP = convert(map.get(Constants.TransactionTypeEnums.TRANS_TYPE_500010.code));
        BigDecimal salebrokerageN = convert(map.get(Constants.TransactionTypeEnums.TRANS_TYPE_500011.code));
        BigDecimal echargebrokerage = convert(map.get(Constants.TransactionTypeEnums.TRANS_TYPE_500020.code));
        BigDecimal livebroker = null;
        //水电煤特殊处理 在水电煤不存在时，返回null，前端特殊处理，为null不显示
        BigDecimal livebrokeragePositive = map.get(Constants.TransactionTypeEnums.TRANS_TYPE_500070.code);
        BigDecimal livebrokerageNative = map.get(Constants.TransactionTypeEnums.TRANS_TYPE_500071.code);
        map.clear();
        map.put(MonthAchieveType.ESHOP.name(), salebrokerageP.add(salebrokerageN));
        map.put(MonthAchieveType.ERCHARGE.name(), echargebrokerage);
        if(livebrokeragePositive !=null && livebrokerageNative !=null){
            livebroker = livebrokeragePositive.add(livebrokerageNative);
        }else if(livebrokeragePositive!=null && livebrokerageNative == null){
            livebroker = livebrokeragePositive;
        }else if(livebrokeragePositive == null && livebrokerageNative !=null){
            livebroker = livebrokerageNative;
        }

        if(livebroker != null){
            map.put(MonthAchieveType.PAY.name(), livebroker);
        }
        return map;
    }

    private BigDecimal convert(Object o) {

        BigDecimal b = new BigDecimal(o == null ? "0" : o.toString());

        return b;
    }

    private void dealMap(Map<String, BigDecimal> map, String transactionType, BigDecimal amount) {

        if (map.containsKey(transactionType) && map.get(transactionType) != null) {
            map.put(transactionType, map.get(transactionType).add(amount));
        } else {
            map.put(transactionType, amount);
        }
    }

    /**
     * 初始化银行业务
     *
     * @param partyId
     * @param year
     * @param month
     * @throws Exception
     */
    private void initBankSalary(Long partyId, Integer year, Integer month) throws Exception {
        com.xianglin.xlnodecore.common.service.facade.base.Response<SalaryDTO> salary = salaryImportService.queryByNodeMangerPartyId(partyId, mergeDate(year, month));
        if (salary.isSucc()) {
            MonthAchieve ma = new MonthAchieve();
            ma.setPartyId(partyId);
            ma.setYear(year);
            ma.setMonth(month);
            ma.setStaticType(MonthAchieveType.INCOME.name());
            ma.setBusiType(MonthAchieveType.BANK.name());
            ma.setTotal(salary.getResult().getAmountPay());
            saveUpdate(ma);
        }
    }

    private void saveUpdate(MonthAchieve ma) throws Exception {
        Map<String, Object> paras = DTOUtils.beanToMap(ma);
        List<MonthAchieve> list = monthAchieveDAO.select(paras);
        if (CollectionUtils.isNotEmpty(list)) {
            MonthAchieve achieve = list.get(0);
            achieve.setTotal(ma.getTotal());
            monthAchieveDAO.updateByPrimaryKeySelective(achieve);
            if (list.size() > 1) {//第一条之后的数据作删除处理
                for (int i = 1; i < list.size(); i++) {
                    achieve = list.get(i);
                    monthAchieveDAO.deleteByPrimaryKey(achieve.getId());
                }
            }
        } else {
            monthAchieveDAO.insert(ma);
        }
    }

    private String mergeDate(Integer year, Integer month) {
        if (month >= 10) {
            return year + "" + month;
        } else {
            return year + "0" + month;
        }
    }

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        System.out.println(cal.get(Calendar.YEAR));
        System.out.println(cal.get(Calendar.MONTH) + 1);
    }
}
