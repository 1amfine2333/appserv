package com.xianglin.appserv.biz.service.implement;

import com.xianglin.appserv.biz.shared.FilofaxManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.AppFilofaxAccount;
import com.xianglin.appserv.common.dal.dataobject.AppFilofaxBudget;
import com.xianglin.appserv.common.dal.dataobject.AppFilofaxCategory;
import com.xianglin.appserv.common.dal.dataobject.AppFilofaxDetail;
import com.xianglin.appserv.common.service.facade.app.FilofaxService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.req.FilofaxDetailReq;
import com.xianglin.appserv.common.util.*;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wanglei on 2017/7/4.
 */
@Service("filofaxService")
@ServiceInterface
public class FilofaxServiceImpl implements FilofaxService {

    private static final Logger logger = LoggerFactory.getLogger(FilofaxServiceImpl.class);

    @Autowired
    private FilofaxManager filofaxManager;

    @Autowired
    private SessionHelper sessionHelper;

    /**
     * 查询个人关联类别
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.queryCategoryList", description = "查询个人关联类别")
    public Response<List<FilofaxCategoryVo>> queryCategoryList(String mode) {
        Response<List<FilofaxCategoryVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            if (StringUtils.isNotEmpty(mode)) {
                paras.put("mode", mode);
            }
            List<AppFilofaxCategory> list = filofaxManager.queryCategoryList(paras);
            List<FilofaxCategoryVo> result = DTOUtils.map(list, FilofaxCategoryVo.class);
            resp.setResult(result);
        } catch (Exception e) {
            logger.warn("queryCategoryList error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 新增类别
     *
     * @param vo
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.addCategory", description = "新增类别")
    public Response<Boolean> addCategory(FilofaxCategoryVo vo) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            if (StringUtils.isNotEmpty(vo.getMode())) {
                paras.put("mode", vo.getMode());
            }
            List<AppFilofaxCategory> list = filofaxManager.queryCategoryList(paras);
            if (CollectionUtils.isNotEmpty(list)) {
                if (list.size() >= 60) {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400018);
                    return resp;
                }
                for (AppFilofaxCategory category : list) {
                    if (StringUtils.equals(category.getName(), vo.getName())) {
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400016);
                        return resp;
                    }
                }
            }

            vo.setPartyId(partyId);
            vo.setIcon(SysConfigUtil.getStr("filofax_category_icon"));
            vo.setIconPoint(SysConfigUtil.getStr("filofax_category_icon_point"));
            vo.setType(Constant.FilofaxCategory.PRIVATE.name());
            resp.setResult(filofaxManager.addCategory(DTOUtils.map(vo, AppFilofaxCategory.class)));
        } catch (Exception e) {
            logger.warn("addCategory error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 查询个人账号列表
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.queryAccountList", description = "查询个人账号列表")
    public Response<List<FilofaxAccountVo>> queryAccountList(Long acctId) {
        Response<List<FilofaxAccountVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            if (acctId != null && acctId != 0) {
                paras.put("id", acctId);
            }
            List<AppFilofaxAccount> list = filofaxManager.queryAccountList(paras);
            List<FilofaxAccountVo> result = DTOUtils.map(list, FilofaxAccountVo.class);
            resp.setResult(result);
        } catch (Exception e) {
            logger.warn("queryAccountList error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 新增账户
     *
     * @param vo
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.addAccount", description = "新增账户")
    public Response<Boolean> addAccount(FilofaxAccountVo vo) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            vo.setPartyId(partyId);
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            List<AppFilofaxAccount> list = filofaxManager.queryAccountList(paras);
            if (CollectionUtils.isNotEmpty(list)) {
                for (AppFilofaxAccount account : list) {
                    if (StringUtils.equals(account.getName(), vo.getName())) {
                        resp.setFacade(FacadeEnums.ERROR_CHAT_400017);
                        return resp;
                    }
                }
            }
            vo.setIcon(SysConfigUtil.getStr("filofax_account_icon"));
            resp.setResult(filofaxManager.addAccount(DTOUtils.map(vo, AppFilofaxAccount.class)));

        } catch (Exception e) {
            logger.warn("addCategory error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 更新账户信息
     *
     * @param vo
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.updateAccount", description = "更新账户信息")
    public Response<Boolean> updateAccount(FilofaxAccountVo vo) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            vo.setPartyId(partyId);
            resp.setResult(filofaxManager.updateAccount(DTOUtils.map(vo, AppFilofaxAccount.class)));
        } catch (Exception e) {
            logger.warn("addCategory error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 删除帐户
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.deleteAccount", description = "删除帐户")
    public Response<Boolean> deleteAccount(Long id) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            AppFilofaxAccount account = filofaxManager.queryAccount(id);
            if (account.getPartyId().equals(partyId)) {
                Map<String, Object> paras = DTOUtils.queryMap();
                paras.put("partyId", partyId);
                paras.put("filofaxAccount", id);
                List<AppFilofaxDetail> list = filofaxManager.queryFilofaxDetail(paras);
                if (CollectionUtils.isEmpty(list)) {
                    account.setIsDeleted(Constant.YESNO.YES.code);
                    resp.setResult(filofaxManager.updateAccount(account));
                } else {
                    resp.setFacade(FacadeEnums.ERROR_CHAT_400015);
                }
            }
        } catch (Exception e) {
            logger.warn("addCategory error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 记一笔，新增账号明细
     *
     * @param vo
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.addFilofaxDetail", description = "记一笔，新增账号明细")
    public Response<Boolean> addFilofaxDetail(FilofaxDetailVo vo) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            AppFilofaxDetail detail = DTOUtils.map(vo, AppFilofaxDetail.class);
            detail.setCategory(Long.valueOf(vo.getCategory()));
            if (vo.getId() != null && vo.getId() != 0) {
                resp.setResult(filofaxManager.updateFiloFaxDetail(detail));
            } else {
                Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
                detail.setPartyId(partyId);
                resp.setResult(filofaxManager.addFiloFaxDetail(detail));
            }
        } catch (Exception e) {
            logger.warn("addFilofaxDetail error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 更新明细信息
     *
     * @param vo
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.updateFilofaxDetail", description = "更新明细信息")
    public Response<Boolean> updateFilofaxDetail(FilofaxDetailVo vo) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            if (vo.getId() != null && vo.getId() != 0) {
                resp.setResult(filofaxManager.updateFiloFaxDetail(DTOUtils.map(vo, AppFilofaxDetail.class)));
            } else {
                resp = addFilofaxDetail(vo);
            }
        } catch (Exception e) {
            logger.warn("addCategory error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 删除明细
     *
     * @param id
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.deleteFilofaxDetail", description = "删除明细")
    public Response<Boolean> deleteFilofaxDetail(Long id, Long partyId) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId2 = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId != null || partyId2 != null) {
                if (partyId == null) {
                    partyId = partyId2;
                }
                AppFilofaxDetail detail = filofaxManager.queryFilofaxDetail(id);
                if (detail != null && detail.getPartyId().equals(partyId)) {
                    detail.setIsDeleted(Constant.YESNO.YES.code);
                    resp.setResult(filofaxManager.updateFiloFaxDetail(detail));
                } else {
                    resp.setFacade(FacadeEnums.DELETE_FAIL);
                }
            } else {
                resp.setFacade(FacadeEnums.DELETE_FAIL);
            }
        } catch (Exception e) {
            logger.warn("addFilofaxDetail error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 账本明细查询请求参数
     *
     * @param req
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.queryFilofaxDetailList", description = "账本明细查询请求参数")
    public Response<List<FilofaxDetailVo>> queryFilofaxDetailList(FilofaxDetailReq req) {
        Response<List<FilofaxDetailVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            List<DailyDetailTotalVo> list = filofaxManager.queryBudgetDetail(paras);
            List<FilofaxDetailVo> result = DTOUtils.map(list, FilofaxDetailVo.class);
            resp.setResult(result);
        } catch (Exception e) {
            logger.warn("queryAccountList error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 我的钱包个余额查询
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.queryAccountTotal", description = "我的钱包个余额查询")
    public Response<FilofaxAccountTotalVo> queryAccountTotal() {
        Response<FilofaxAccountTotalVo> resp = ResponseUtils.successResponse();
        try {
            FilofaxAccountTotalVo vo = new FilofaxAccountTotalVo();
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            List<AppFilofaxAccount> list = filofaxManager.queryAccountList(paras);
            BigDecimal total = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(list)) {
                List<FilofaxAccountVo> vos = DTOUtils.map(list, FilofaxAccountVo.class);
                for (FilofaxAccountVo acct : vos) {
                    if (StringUtils.equals(acct.getIsStatis(), Constant.YESNO.YES.code)) {
                        total = total.add(acct.getBalance());
                    }
                    acct.setBalanceStr(NumberUtil.amountFormat(acct.getBalance()));
                }
                vo.setAccountList(vos);
            }
            vo.setTotal(NumberUtil.amountFormat(total));
            //查询借入，借出
            paras.clear();
            paras.put("name", "借入");
            paras.put("type", Constant.FilofaxCategory.PUBLIC.name());
            List<AppFilofaxCategory> categorys = filofaxManager.queryCategoryList(paras);
            if (CollectionUtils.isNotEmpty(categorys)) {
                paras.clear();
                paras.put("partyId", partyId);
                paras.put("category", categorys.get(0).getId());
                vo.setBorrow(NumberUtil.amountFormat(filofaxManager.queryFILOFAXAMOUNTSum(paras)));
            }
            //查询借入，借出
            paras.clear();
            paras.put("name", "借出");
            paras.put("type", Constant.FilofaxCategory.PUBLIC.name());
            categorys = filofaxManager.queryCategoryList(paras);
            if (CollectionUtils.isNotEmpty(categorys)) {
                paras.clear();
                paras.put("partyId", partyId);
                paras.put("category", categorys.get(0).getId());
                vo.setLend(NumberUtil.amountFormat(filofaxManager.queryFILOFAXAMOUNTSum(paras)));
            }
            resp.setResult(vo);
        } catch (Exception e) {
            logger.warn("queryAccountList error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 日统计查询
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.queryDailyDetailTotal", description = "日统计查询")
    public Response<DailyDetailTotalVo> queryDailyDetailTotal() {
        return null;
    }

    /**
     * 预算金额及对应支出，收入查询
     * 明细页面，按条件返回预算剩余，收入，支出三个金额信息，若对应的预算不存在，则初始一个金额为0的预算
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.queryBudgetTotal", description = "预算金额及对应支出，收入查询")
    public Response<BudgetTotalVo> queryBudgetTotal(BudgetTotalVo vo) {
        Response<BudgetTotalVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            BudgetTotalVo budgetTotalVo = new BudgetTotalVo();
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            paras.put("startDay", vo.getStartDay());
            paras.put("endDay", vo.getEndDay());
            BigDecimal budgetSum = filofaxManager.querybudgetSum(paras);
            paras.put("type", "'SPECIAL'");
            Map<String, Object> inSumOutSum = filofaxManager.queryInSumAndOutSum(paras);
            if (inSumOutSum == null) {
                budgetTotalVo.setInSum("0.00");
                budgetTotalVo.setOutSum("0.00");
            } else {
                budgetTotalVo.setInSum(String.valueOf(inSumOutSum.get("inSum").toString()));
                budgetTotalVo.setOutSum(String.valueOf(inSumOutSum.get("outSum").toString()));
            }
            if (budgetSum.compareTo(BigDecimal.ZERO) == 0) {
                budgetTotalVo.setBudgetSurplus(String.valueOf(new BigDecimal("0.00")));
                budgetTotalVo.setIsBudget(Constant.YESNO.NO.code);
            } else {
                BigDecimal surplus = budgetSum.subtract(new BigDecimal(String.valueOf(budgetTotalVo.getOutSum())));
                budgetTotalVo.setBudgetSurplus(String.valueOf(surplus));
                budgetTotalVo.setIsBudget(Constant.YESNO.YES.code);
            }
            budgetTotalVo.setStartDay(vo.getStartDay());
            budgetTotalVo.setEndDay(vo.getEndDay());
            resp.setResult(budgetTotalVo);
        } catch (Exception e) {
            logger.warn("queryBudget error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 更新预算金额，
     * 若预算不存在，则新增一个
     *
     * @param vo
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.updateBudget", description = "更新预算金额")
    public Response<Boolean> updateBudget(BudgetVo vo) {
        Response<Boolean> resp = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            paras.put("startDay", vo.getStartDay());
            paras.put("endDay", vo.getEndDay());
            List<AppFilofaxBudget> appFilofaxBudgetList = filofaxManager.queryBudget(paras);
            AppFilofaxBudget appFilofaxBudget = new AppFilofaxBudget();
            appFilofaxBudget.setBudget(new BigDecimal(vo.getBudget()));
            appFilofaxBudget.setPartyId(Long.valueOf(partyId));
            appFilofaxBudget.setStartDay(vo.getStartDay());
            appFilofaxBudget.setEndDay(vo.getEndDay());
            if (null == appFilofaxBudgetList || appFilofaxBudgetList.size() == 0) {
                //新增
                Boolean addBudget = filofaxManager.addBudget(appFilofaxBudget);
                resp.setResult(addBudget);
            } else {
                //更新
                appFilofaxBudget.setId(appFilofaxBudgetList.get(0).getId());
                Boolean updateBudget = filofaxManager.updateBudget(appFilofaxBudget);
                resp.setResult(updateBudget);
            }
        } catch (Exception e) {
            logger.warn("updateBudget error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 按条件查询预算信息，若对应的预算不存在，则初始化一个
     *
     * @param vo
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.queryBudget", description = "查询预算信息")
    public Response<BudgetVo> queryBudget(BudgetVo vo) {
        Response<BudgetVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            paras.put("startDay", vo.getStartDay());
            paras.put("endDay", vo.getEndDay());
            BudgetVo budgetVo = new BudgetVo();
            BigDecimal budgetSum = filofaxManager.querybudgetSum(paras);
            if (budgetSum.compareTo(BigDecimal.ZERO) == 0) {
                budgetVo.setBudget(String.valueOf(new BigDecimal("0.00")));
                budgetVo.setStartDay(vo.getStartDay());
                budgetVo.setEndDay(vo.getEndDay());
                resp.setResult(budgetVo);
                return resp;
            }
            budgetVo.setBudget(String.valueOf(budgetSum));
            budgetVo.setStartDay(vo.getStartDay());
            budgetVo.setEndDay(vo.getEndDay());
            budgetVo.setPartyId(partyId);
            resp.setResult(budgetVo);
        } catch (Exception e) {
            logger.warn("queryBudget error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 预算页面，对应每日明细查询
     *
     * @param req@return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.queryBudgetDetail", description = "预算页面，对应每日明细查询")
    public Response<List<DailyDetailTotalVo>> queryBudgetDetail(ProportionTotalVo req) {
        Response<List<DailyDetailTotalVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                Long partyId2 = req.getPartyId();
                if (partyId2 == null) {
                    resp.setResonpse(ResponseEnum.SESSION_INVALD);
                    return resp;
                } else {
                    partyId = partyId2;
                }
            }
            Map<String, Object> paras = DTOUtils.beanToMap(req);
            paras.put("partyId", partyId);
            paras.put("startDay", req.getStartDay());
            paras.put("endDay", req.getEndDay());
            paras.put("categoryMode", req.getCategoryMode());
            paras.put("filofaxAccount", req.getFilofaxAccount());
            List<DailyDetailTotalVo> list = filofaxManager.queryBudgetDetail(paras);
            resp.setResult(list);
        } catch (Exception e) {
            logger.warn("queryBudgetDetail error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 时间区域内每日占比查询
     * 日度明细页面每日占比
     *
     * @param req
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.queryDailyProportion", description = "时间区域内每日占比查询")
    public Response<List<DailyProportionVo>> queryDailyProportion(ProportionTotalVo req) {
        Response<List<DailyProportionVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            List<DailyProportionVo> list = new ArrayList<DailyProportionVo>();
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            paras.put("startDay", req.getStartDay());
            paras.put("endDay", req.getEndDay());
            paras.put("categoryMode", Constant.FilofaxMode.OUT);
            List<AppFilofaxBudget> budgetList = filofaxManager.queryBudget(paras);
            BigDecimal budget=new BigDecimal("0");
            if (null == budgetList || budgetList.size()==0) {
                budget=BigDecimal.ZERO;
            }else{
                budget=budgetList.get(0).getBudget();
            }
            paras.put("type", "'SPECIAL'");
            List<Map<String, Object>> filofaxDetailMapList = filofaxManager.getTypeSum(paras);
            Map<String, Object> resultMap = new HashMap<>();
            for (Map<String, Object> map : filofaxDetailMapList) {
                resultMap.put(map.get("day").toString(), map.get("amount"));
            }
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            Date startDay = DateUtils.parse("yyyyMMdd", req.getStartDay());
            Date endDay = DateUtils.parse("yyyyMMdd", req.getEndDay());
            startDate.setTime(startDay);
            endDate.setTime(endDay);
            endDate.add(Calendar.DATE, 1);
            if(budget.compareTo(BigDecimal.ZERO) > 0){  //有预算
                for (; startDate.before(endDate); )
                {
                    String day = DateUtils.formatDate(startDate.getTime(), "yyyyMMdd");
                    if (resultMap.containsKey(day)) {  //有明细
                        DailyProportionVo dailyProportionVo=getDailyProportionVo(day,partyId,resultMap.get(day).toString(),budget,null);
                        list.add(dailyProportionVo);
                    }else{                //无明细
                        DailyProportionVo dailyProportionVo=getDailyProportionVo(day,partyId,"0.0000",budget,"0.0000");
                        list.add(dailyProportionVo);
                    }
                    startDate.add(Calendar.DAY_OF_YEAR, 1);
                }
            }else{   //无预算
                for (; startDate.before(endDate); )
                {
                    String day = DateUtils.formatDate(startDate.getTime(), "yyyyMMdd");
                    if (resultMap.containsKey(day)){   //有明细：21
                        DailyProportionVo dailyProportionVo=getDailyProportionVo(day,partyId,"0.0000",budget,"0.2200");
                        list.add(dailyProportionVo);

                    }else{   //无明细：0
                        DailyProportionVo dailyProportionVo=getDailyProportionVo(day,partyId,"0.0000",budget,"0.0000");
                        list.add(dailyProportionVo); 
                    }
                    startDate.add(Calendar.DAY_OF_YEAR, 1);
                }
            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.warn("queryDailyProportion error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 一定时间区域时间内按条件合计查询
     * h5页面统计使用，支持类别，标签的支出/收入统计
     *
     * @param req
     * @return
     */
    @Override
    public Response<List<TotalProportionVo>> queryTotalProportion(ProportionTotalVo req) {
        Response<List<TotalProportionVo>> resp = ResponseUtils.successResponse();
        try {
            List<TotalProportionVo> list = new ArrayList<TotalProportionVo>();
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", req.getPartyId());
            paras.put("startDay", req.getStartDay());
            paras.put("endDay", req.getEndDay());
            paras.put("categoryMode", req.getCategoryMode());
            paras.put("labelORMode", req.getSectionType());
            if (req.getFilofaxAccount() == null) {
                paras.put("type", "'SPECIAL'");
            }
            List<Map<String, Object>> filofaxDetailMapList = filofaxManager.getCategorySum(paras);
            List<AppFilofaxDetail> filofaxDetailList = convertAppFilofaxDetailPojo(filofaxDetailMapList);
            if (filofaxDetailList != null) {
                //计算收入或支出总额
                BigDecimal total = new BigDecimal("0.00");
                for (AppFilofaxDetail filofaxDetail : filofaxDetailList) {
                    total = total.add(filofaxDetail.getAmount());
                }
                //标签为其它的相加在一起
                if (req.getSectionType().equals("categoryName")) {
                    for (int i = 0; i < filofaxDetailList.size(); i++) {
                        TotalProportionVo totalProportionVo = getTotalProportionVo(req.getPartyId(),filofaxDetailList.get(i).getStatus(),filofaxDetailList.get(i).getAmount(),total,ColorUtil.getColor(i + 1),null);
                        list.add(totalProportionVo);
                    }
                }
                if (req.getSectionType().equals("LABEL")) {
                    if (filofaxDetailList.size() <= 60) {  //不大于60条数据
                        for (int i = 0; i < filofaxDetailList.size(); i++) {
                            String label = null;
                            if (StringUtils.isNotEmpty(filofaxDetailList.get(i).getStatus())) {
                                label=filofaxDetailList.get(i).getStatus();
                            } else {
                                label = "其他";
                            }
                            TotalProportionVo totalProportionVo = getTotalProportionVo(req.getPartyId(),null,filofaxDetailList.get(i).getAmount(),total,ColorUtil.getColor(i + 1),label);
                            list.add(totalProportionVo);
                        }
                    } else {      //大于60条数据
                        List<TotalProportionVo> voList = new ArrayList<TotalProportionVo>();
                        BigDecimal OtherAmount = new BigDecimal("0.00");
                        for (int i = 0; i < filofaxDetailList.size(); i++) {
                            if (StringUtils.isNotEmpty(filofaxDetailList.get(i).getStatus()) && i < 60) {
                                TotalProportionVo totalProportionVo = getTotalProportionVo(req.getPartyId(),null,filofaxDetailList.get(i).getAmount(),total,ColorUtil.getColor(i + 1),filofaxDetailList.get(i).getStatus());
                                list.add(totalProportionVo);
                            } else {
                                //其它标签
                                OtherAmount = OtherAmount.add(filofaxDetailList.get(i).getAmount());
                                String label = null;
                                if (StringUtils.isNotEmpty(filofaxDetailList.get(i).getStatus())) {
                                    label = filofaxDetailList.get(i).getStatus();
                                } else {
                                    label = "其他";
                                }
                                TotalProportionVo totalProportionVo = getTotalProportionVo(req.getPartyId(),null,filofaxDetailList.get(i).getAmount(),total,ColorUtil.getColor(i + 1),label);
                                voList.add(totalProportionVo);
                            }
                        }
                        TotalProportionVo totalProportionVo = getTotalProportionVo(req.getPartyId(),null,OtherAmount,total,null,"其他");
                        totalProportionVo.setTotalProportionVo(voList);
                        list.add(totalProportionVo);
                        //重新根据占比排序
                        for (int i = 0; i < list.size() - 1; i++) {
                            for (int j = 1; j < list.size() - i; j++) {
                                String aa = list.get(j - 1).getCategoryTotal();
                                String bb = list.get(j).getCategoryTotal();
                                BigDecimal a = new BigDecimal(aa);
                                BigDecimal b = new BigDecimal(bb);
                                TotalProportionVo c;
                                if (a.compareTo(b) < 0) {
                                    c = list.get(j - 1);
                                    list.set((j - 1), list.get(j));
                                    list.set(j, c);
                                }
                            }
                        }
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setImageColor(ColorUtil.getColor(i + 1));
                        }
                    }


                }
            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.warn("queryTotalProportion error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    private TotalProportionVo getTotalProportionVo(Long partyId, String status, BigDecimal amount, BigDecimal total, String color, String label) {
        TotalProportionVo totalProportionVo = new TotalProportionVo();
        totalProportionVo.setPartyId(partyId);
        totalProportionVo.setTotal(String.valueOf(total));
        totalProportionVo.setCategory(status);
        totalProportionVo.setImageColor(color);
        totalProportionVo.setlABEL(label);
        totalProportionVo.setCategoryTotal(String.valueOf(amount));
        BigDecimal setScale = amount.divide(total, 4, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        totalProportionVo.setPercentage(setScale + "%");
        return totalProportionVo;
    }
    
    private DailyProportionVo getDailyProportionVo(String day,Long partyId,String amount,BigDecimal budget,String proportion){
        DailyProportionVo dailyProportionVo = new DailyProportionVo();
        dailyProportionVo.setDay(day);
        dailyProportionVo.setAmount(amount);
        BigDecimal divide = BigDecimal.ZERO;
        if(new BigDecimal(amount).compareTo(BigDecimal.ZERO)>0){
            divide = new BigDecimal(amount).divide(budget, 4, BigDecimal.ROUND_HALF_EVEN);
        }
        if(divide.compareTo(BigDecimal.ZERO) > 0){
            proportion = String.valueOf(divide);
        }
        dailyProportionVo.setProportion(proportion);
        dailyProportionVo.setBudget(String.valueOf(budget));
        dailyProportionVo.setPartyId(partyId);
        return  dailyProportionVo;
    }

    private List<AppFilofaxDetail> convertAppFilofaxDetailPojo(List<Map<String, Object>> filofaxDetailMapList) {
        List<AppFilofaxDetail> result = new ArrayList<>(filofaxDetailMapList.size());
        for (Map map : filofaxDetailMapList) {
            AppFilofaxDetail vo;
            try {
                vo = DTOUtils.map(map, AppFilofaxDetail.class);
                result.add(vo);
            } catch (Exception e) {
                logger.warn("convertPojo error", e);
            }
        }
        return result;
    }

    /**
     * 年度总乱查询
     *
     * @return
     */
    @Override
    public Response<List<YearTotalProportionVo>> queryYearTotalProportion(int year, Long partyId) {
        Response<List<YearTotalProportionVo>> resp = ResponseUtils.successResponse();
        try {
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            paras.put("year", year);
            paras.put("type", "'SPECIAL'");
            List<Map<String, Object>> filofaxDetailMapList = filofaxManager.getMonSumByYear(paras);
            List<AppFilofaxDetail> filofaxDetailList = convertAppFilofaxDetailPojo(filofaxDetailMapList);
            List<YearTotalProportionVo> list = new ArrayList<YearTotalProportionVo>();
            if (filofaxDetailList != null) {
                //总结余初始化为0
                BigDecimal totalSurplus = new BigDecimal("0.00");
                for (AppFilofaxDetail filofaxDetail : filofaxDetailList) {
                    YearTotalProportionVo YearTotalProportionVo = new YearTotalProportionVo();
                    YearTotalProportionVo.setPartyId(partyId);
                    YearTotalProportionVo.setMonth(Integer.valueOf(filofaxDetail.getDay()) + "月");
                    BigDecimal intSum = filofaxDetail.getInSum();
                    YearTotalProportionVo.setInSum(String.valueOf(intSum));
                    BigDecimal outSum = filofaxDetail.getOutSum();
                    YearTotalProportionVo.setOutSum(String.valueOf(outSum));
                    BigDecimal monthSurplus = intSum.subtract(outSum);
                    YearTotalProportionVo.setMonthSurplus(String.valueOf(monthSurplus));
                    totalSurplus = monthSurplus.add(totalSurplus);
                    YearTotalProportionVo.setTotalSurplus(String.valueOf(totalSurplus));
                    list.add(YearTotalProportionVo);
                }
            }
            resp.setResult(list);
        } catch (Exception e) {
            logger.warn("queryYearTotalProportion error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }


    /**
     * h5图表详情页面
     * 一定时间区域的占比和每日明细，包含收入和支出
     *
     * @param req
     * @return
     */
    @Override
    public Response<DailyProportionVo> queryProportionAndDetail(ProportionTotalVo req) {
        Response<DailyProportionVo> resp = ResponseUtils.successResponse();
        try {
            DailyProportionVo dailyProportionVo = new DailyProportionVo();

            //查收入或支出总额
            Map<String, Object> paras = DTOUtils.beanToMap(req);
            paras.put("partyId", req.getPartyId());
            paras.put("startDay", req.getStartDay());
            paras.put("endDay", req.getEndDay());
            paras.put("categoryMode", req.getCategoryMode());
            paras.put("labelORMode", req.getSectionType());
            if (req.getFilofaxAccount() == null) {
                paras.put("type", "'SPECIAL'");
            }
            List<Map<String, Object>> filofaxDetailMapList = filofaxManager.getCategorySum(paras);
            List<AppFilofaxDetail> filofaxDetailList = convertAppFilofaxDetailPojo(filofaxDetailMapList);
            BigDecimal total = new BigDecimal("0.00");
            for (AppFilofaxDetail filofaxDetail : filofaxDetailList) {
                total = total.add(filofaxDetail.getAmount());
            }
            //按收入或支出、标签或类型总额
            paras.put("categoryName", req.getCategory());
            paras.put("label", req.getLabel());
            paras.remove("category");
            if (req.getFilofaxAccount() == null) {
                paras.put("type", "'SPECIAL'");
            }
            BigDecimal amount = filofaxManager.queryFILOFAXAMOUNTSum(paras);
            dailyProportionVo.setAmount(String.valueOf(amount));
            BigDecimal setScale = amount.divide(total, 4, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
            dailyProportionVo.setPercentage(String.valueOf(setScale) + "%");
            List<DailyDetailTotalVo> list = filofaxManager.queryBudgetDetail(paras);
            dailyProportionVo.setDailyDetailTotalVo(list);
            resp.setResult(dailyProportionVo);
        } catch (Exception e) {
            logger.warn("queryProportionAndDetail error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 现金页面根据账户ID查一定时间区域内的流入和流出
     *
     * @param req
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.FilofaxService.queryInSumAndOutSum", description = "根据账户ID查一定时间区域内的流入和流出")
    public Response<DailyDetailTotalVo> queryInSumAndOutSum(ProportionTotalVo req) {
        Response<DailyDetailTotalVo> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            Map<String, Object> paras = new HashMap<>();
            paras.put("partyId", partyId);
            if (req.getFilofaxAccount() == null) {
                paras.put("type", "'SPECIAL'");
            }
            paras.put("filofaxAccount", req.getFilofaxAccount());
            paras.put("startDay", req.getStartDay());
            paras.put("endDay", req.getEndDay());
            Map<String, Object> inSumOutSum = filofaxManager.queryInSumAndOutSum(paras);
            DailyDetailTotalVo dailyDetailTotalVo = new DailyDetailTotalVo();
            if (inSumOutSum == null) {
                dailyDetailTotalVo.setInSum("0.00");
                dailyDetailTotalVo.setOutSum("0.00");
            } else {
                dailyDetailTotalVo.setInSum(String.valueOf(inSumOutSum.get("inSum").toString()));
                dailyDetailTotalVo.setOutSum(String.valueOf(inSumOutSum.get("outSum").toString()));
            }
            resp.setResult(dailyDetailTotalVo);
        } catch (Exception e) {
            logger.warn("queryDailyProportion error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }


}
