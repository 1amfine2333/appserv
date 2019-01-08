package com.xianglin.appserv.biz.shared.impl;

import com.xianglin.appserv.biz.shared.FilofaxManager;
import com.xianglin.appserv.common.dal.daointerface.AppFilofaxAccountDAO;
import com.xianglin.appserv.common.dal.daointerface.AppFilofaxBudgetDAO;
import com.xianglin.appserv.common.dal.daointerface.AppFilofaxCategoryDAO;
import com.xianglin.appserv.common.dal.daointerface.AppFilofaxDetailDAO;
import com.xianglin.appserv.common.dal.dataobject.AppFilofaxAccount;
import com.xianglin.appserv.common.dal.dataobject.AppFilofaxBudget;
import com.xianglin.appserv.common.dal.dataobject.AppFilofaxCategory;
import com.xianglin.appserv.common.dal.dataobject.AppFilofaxDetail;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.vo.DailyDetailTotalVo;
import com.xianglin.appserv.common.service.facade.model.vo.FilofaxDetailVo;
import com.xianglin.appserv.common.util.DTOUtils;

import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.core.model.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xianglin.appserv.common.util.SysConfigUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * Created by wanglei on 2017/7/4.
 */
@Service("filofaxManager")
public class FilofaxManagerImpl implements FilofaxManager {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(FilofaxManagerImpl.class);
    @Autowired
    private AppFilofaxDetailDAO appFilofaxDetailDAO;

    @Autowired
    private AppFilofaxBudgetDAO appFilofaxBudgetDAO;

    @Autowired
    private AppFilofaxCategoryDAO appFilofaxCategoryDAO;

    @Autowired
    private AppFilofaxAccountDAO appFilofaxAccountDAO;

    @Override
    public List<AppFilofaxCategory> queryCategoryList(Map<String, Object> paras) {
        return appFilofaxCategoryDAO.selectList(paras);
    }

    @Override
    public boolean addCategory(AppFilofaxCategory category) {
        return appFilofaxCategoryDAO.insert(category) == 1;
    }

    @Override
    public BigDecimal querybudgetSum(Map<String, Object> paras) {
        return appFilofaxBudgetDAO.selectSumBudget(paras);
    }

    @Override
    public BigDecimal queryFILOFAXAMOUNTSum(Map<String, Object> paras) {
        return appFilofaxDetailDAO.selectSumFilofaxAccount(paras);
    }

    @Override
    public List<DailyDetailTotalVo> queryBudgetDetail(Map<String, Object> paras) {
        if(paras.get("filofaxAccount")==null){
            paras.put("type","'SPECIAL'");
        }
        List<DailyDetailTotalVo> list = new ArrayList<DailyDetailTotalVo>();
        List<Map<String,Object>> filofaxDetailMap=new ArrayList<Map<String,Object>>();
        List<AppFilofaxDetail> appFilofaxDetailList = new ArrayList<AppFilofaxDetail>();
        if (paras.get("categoryMode") == null && paras.get("categoryName") == null && paras.get("label") == null) {
            filofaxDetailMap = appFilofaxDetailDAO.getAmountSum(paras);
             //将filofaxDetailMap转换为appFilofaxDetailList
            appFilofaxDetailList=convertAppFilofaxDetailPojo(filofaxDetailMap);
            if (appFilofaxDetailList != null && appFilofaxDetailList.size() > 0) {
                List<FilofaxDetailVo> filofaxDetailVoList = convertPojo(appFilofaxDetailList);
                for (int i = 0; i < filofaxDetailVoList.size(); i++) {
                    DailyDetailTotalVo dailyDetailTotalVo = new DailyDetailTotalVo();
                    try {
                        String date = filofaxDetailVoList.get(i).getDay();
                        String day=convertDay(date);
                        dailyDetailTotalVo.setDay(day);
                        dailyDetailTotalVo.setTime(filofaxDetailVoList.get(i).getDay());
                        dailyDetailTotalVo.setOutSum(filofaxDetailVoList.get(i).getOutSum());
                        dailyDetailTotalVo.setInSum(filofaxDetailVoList.get(i).getInSum());
                        Map<String, Object> map2 = new HashMap<String, Object>();
                        map2.put("partyId", paras.get("partyId"));
                        map2.put("day", appFilofaxDetailList.get(i).getDay());
                        map2.put("filofaxAccount",paras.get("filofaxAccount"));
                        if(paras.get("filofaxAccount")==null){
                            map2.put("type","'SPECIAL'");
                        }
                        dailyDetailTotalVo.setFilofaxDetailVo(this.childNode(map2));
                        list.add(dailyDetailTotalVo);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            filofaxDetailMap = appFilofaxDetailDAO.getTypeSum(paras);
            appFilofaxDetailList=convertAppFilofaxDetailPojo(filofaxDetailMap);
            if (appFilofaxDetailList != null && appFilofaxDetailList.size() > 0) {
                List<FilofaxDetailVo> filofaxDetailVoList = convertPojo(appFilofaxDetailList);
                for (FilofaxDetailVo vo : filofaxDetailVoList) {
                    DailyDetailTotalVo dailyDetailTotalVo = new DailyDetailTotalVo();
                    if (paras.get("categoryMode").equals("IN")) {
                        dailyDetailTotalVo.setInSum(String.valueOf(vo.getAmount()));
                    } else if (paras.get("categoryMode").equals("OUT")) {
                        dailyDetailTotalVo.setOutSum(String.valueOf(vo.getAmount()));
                    }
                    String date = vo.getDay();
                    String day=convertDay(date);
                    dailyDetailTotalVo.setDay(day);
                    dailyDetailTotalVo.setTime(date);
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("partyId", paras.get("partyId"));
                    map2.put("day", vo.getDay());
                    map2.put("categoryName", paras.get("categoryName"));
                    map2.put("type","'SPECIAL'");
                    map2.put("label", paras.get("label"));
                    map2.put("categoryMode",paras.get("categoryMode"));
                    dailyDetailTotalVo.setFilofaxDetailVo(this.childNode(map2));
                    list.add(dailyDetailTotalVo);
                }
            }
        }

        return list;
    }

    private List<AppFilofaxDetail> convertAppFilofaxDetailPojo(List<Map<String, Object>> filofaxDetailMap) {
        List<AppFilofaxDetail> result = new ArrayList<>(filofaxDetailMap.size());
        for (Map filofaxDetail : filofaxDetailMap) {
            AppFilofaxDetail vo;
            try {
                vo = DTOUtils.map(filofaxDetail, AppFilofaxDetail.class);
                result.add(vo);
            } catch (Exception e) {
                logger.error("convertPojo error", e);
            }
        }
        return result;
    }

    private String convertDay(String date) {
        //获取今天的日期
        String today = DateUtils.getToday();
        //获取昨天的日期
        String yesterday = DateUtils.getYesterday();
        if (date.equals(today)) {
            String week = DateUtils.getWeek(today);
            date="今天" + " " + week;
        } else if (date.equals(yesterday)) {
            String week = DateUtils.getWeek(yesterday);
            date="昨天" + " " + week;
        } else if (StringUtils.isNotEmpty(date)) {
            String week = DateUtils.getWeek(date);
            String mon = date.substring(4, 6);
            String day = date.substring(6, 8);
            date=Integer.valueOf(mon) + "月" + Integer.valueOf(day) + "日" + " " + week;
        }
        return date;
    }


    private List<FilofaxDetailVo> childNode(Map<String, Object> map) {
        List<FilofaxDetailVo> set = new ArrayList<FilofaxDetailVo>();
        List<AppFilofaxDetail> appFilofaxDetailList = new ArrayList<AppFilofaxDetail>();
        appFilofaxDetailList = appFilofaxDetailDAO.getCategoryList(map);
        List<FilofaxDetailVo> filofaxDetailVoList = convertPojo(appFilofaxDetailList);
        for (int i = 0; i < filofaxDetailVoList.size(); i++) {
            FilofaxDetailVo r = filofaxDetailVoList.get(i);
            set.add(r);
        }
        return set;
    }

    /**
     * 实体类List转换方法
     *
     * @param list
     * @return
     */
    private List<FilofaxDetailVo> convertPojo(List<AppFilofaxDetail> list) {
        List<FilofaxDetailVo> result = new ArrayList<>(list.size());
        for (AppFilofaxDetail filofaxDetail : list) {
            FilofaxDetailVo vo;
            try {
                vo = DTOUtils.map(filofaxDetail, FilofaxDetailVo.class);
                vo.setCategory(filofaxDetail.getCategory()+"");
                if(filofaxDetail.getInSum() != null){
                    vo.setInSum(filofaxDetail.getInSum().toString());  
                }
                if(filofaxDetail.getOutSum() != null){
                    vo.setOutSum(filofaxDetail.getOutSum().toString());  
                }
                result.add(vo);
            } catch (Exception e) {
                logger.error("convertPojo error", e);
            }
        }
        return result;
    }

    @Override
    public List<AppFilofaxAccount> queryAccountList(Map<String, Object> paras) {
        List<AppFilofaxAccount> list = appFilofaxAccountDAO.selectList(paras);
        if (CollectionUtils.isEmpty(list) && !paras.containsKey("id")) {//初始化基础账户
            String[] accts = SysConfigUtil.getStr("filofax_account_default").split("-");
            if (accts != null && accts.length > 0) {
                AppFilofaxAccount account = new AppFilofaxAccount();
                account.setBalance(BigDecimal.ZERO);
                account.setPartyId((Long) paras.get("partyId"));
                account.setIsStatis(Constant.YESNO.YES.code);
                String[] strs;
                for (String acct : accts) {
                    strs = acct.split("=");
                    account.setName(strs[0]);
                    account.setIcon(strs[1]);
                    appFilofaxAccountDAO.insert(account);
                }
            }
            list = appFilofaxAccountDAO.selectList(paras);
        }
        return list;
    }

    @Override
    public List<AppFilofaxBudget> queryBudget(Map<String, Object> paras) {
        return appFilofaxBudgetDAO.getBudget(paras);
    }

    @Override
    public Boolean addBudget(AppFilofaxBudget budget) {
        return appFilofaxBudgetDAO.insert(budget) == 1;
    }

    @Override
    public Boolean updateBudget(AppFilofaxBudget budget) {
        return appFilofaxBudgetDAO.updateByPrimaryKeySelective(budget) == 1;
    }

    @Override
    public boolean addAccount(AppFilofaxAccount account) throws AppException {
        boolean result = appFilofaxAccountDAO.insert(account) == 1;
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            changeAccountDetail(account.getId(), account.getPartyId(), Constant.FilofaxMode.IN.name(), "余额调整", account.getBalance());
        }
        return result;
    }

    @Override
    public boolean updateAccount(AppFilofaxAccount account) throws AppException {
        AppFilofaxAccount before = appFilofaxAccountDAO.selectByPrimaryKey(account.getId());
        if (account.getBalance().compareTo(before.getBalance()) > 0) {//余额调整 增加
            changeAccountDetail(account.getId(), account.getPartyId(), Constant.FilofaxMode.IN.name(), "余额调整", account.getBalance().subtract(before.getBalance()));
        } else if (account.getBalance().compareTo(before.getBalance()) < 0) {//余额调整 減少
            changeAccountDetail(account.getId(), account.getPartyId(), Constant.FilofaxMode.OUT.name(), "余额调整", before.getBalance().subtract(account.getBalance()));
        }
        return appFilofaxAccountDAO.updateAccountBalance(account) == 1;
    }


    private void changeAccountDetail(Long acctId, Long partyId, String mode, String category, BigDecimal amt) throws AppException {
        AppFilofaxDetail detail = new AppFilofaxDetail();
        detail.setPartyId(partyId);
        detail.setAmount(amt);
        detail.setCategoryMode(mode);
        detail.setFilofaxAccount(acctId);
        detail.setDay(DateUtils.formatDate(new Date(), "yyyyMMdd"));
        //查詢category
        Map<String, Object> paras = DTOUtils.queryMap();
        paras.put("name", "余额调整");
        paras.put("mode", mode);
        paras.put("type", Constant.FilofaxCategory.SPECIAL.name());
        List<AppFilofaxCategory> list = appFilofaxCategoryDAO.selectList(paras);
        if (list.size() == 1) {
            detail.setCategory(list.get(0).getId());
            appFilofaxDetailDAO.insert(detail);
        } else {
            throw new AppException(FacadeEnums.ERROR_CHAT_400014);
        }
    }

    @Override
    public List<Map<String,Object>> getTypeSum(Map<String, Object> paras) {
        return appFilofaxDetailDAO.getTypeSum(paras);
    }

    @Override
    public List<Map<String,Object>> getCategorySum(Map<String, Object> paras) {
        return appFilofaxDetailDAO.getCategorySum(paras);
    }

    @Override
    public AppFilofaxAccount queryAccount(Long id) {
        return appFilofaxAccountDAO.selectByPrimaryKey(id);
    }

    @Override
    public boolean addFiloFaxDetail(AppFilofaxDetail detail) {
        boolean result = appFilofaxDetailDAO.insert(detail) == 1;
        appFilofaxAccountDAO.updateAccountBalance(appFilofaxAccountDAO.selectByPrimaryKey(detail.getFilofaxAccount()));
        return result;
    }

    @Override
    public boolean updateFiloFaxDetail(AppFilofaxDetail detail) {
        boolean result = appFilofaxDetailDAO.updateByPrimaryKeySelective(detail) == 1;
        appFilofaxAccountDAO.updateAccountBalance(appFilofaxAccountDAO.selectByPrimaryKey(detail.getFilofaxAccount()));
        return result;
    }

    /**
     * 按条件查询
     *
     * @param paras
     * @return
     */
    @Override
    public List<AppFilofaxDetail> queryFilofaxDetail(Map<String, Object> paras) {
        return appFilofaxDetailDAO.selectList(paras);
    }

    @Override
    public AppFilofaxDetail queryFilofaxDetail(Long id) {
        return appFilofaxDetailDAO.selectByPrimaryKey(id);
    }

    @Override
    public List<Map<String,Object>> getMonSumByYear(Map<String, Object> paras) {
        return appFilofaxDetailDAO.getMonSumByYear(paras);
    }

    @Override
    public Map<String, Object> queryInSumAndOutSum(Map<String, Object> paras) {
        return appFilofaxDetailDAO.queryInSumAndOutSum(paras);
    }

}
