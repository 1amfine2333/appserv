/**
 *
 */
package com.xianglin.appserv.biz.service.implement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.xianglin.appserv.biz.shared.util.LoginAttrUtil;
import com.xianglin.appserv.biz.shared.util.ResponseCacheUtils;
import com.xianglin.appserv.common.dal.dataobject.AppArticleTip;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.NodeAchieveReq;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.core.model.exception.AppException;
import com.xianglin.xlnodecore.common.service.facade.BankImportService;
import com.xianglin.xlnodecore.common.service.facade.FinanceService;
import com.xianglin.xlnodecore.common.service.facade.NodeReportService;
import com.xianglin.xlnodecore.common.service.facade.NodeService;
import com.xianglin.xlnodecore.common.service.facade.base.CommonListResp;
import com.xianglin.xlnodecore.common.service.facade.model.FinanceImportDTO;
import com.xianglin.xlnodecore.common.service.facade.req.BankImportReq;
import com.xianglin.xlnodecore.common.service.facade.req.ImportReq;
import com.xianglin.xlnodecore.common.service.facade.req.NodeDailyReportReq;
import com.xianglin.xlnodecore.common.service.facade.req.NodeReq;
import com.xianglin.xlnodecore.common.service.facade.resp.BankImportListResp;
import com.xianglin.xlnodecore.common.service.facade.resp.NodeResp;
import com.xianglin.xlnodecore.common.service.facade.vo.BankImportVo;
import com.xianglin.xlnodecore.common.service.facade.vo.NodeDailyReportVo;
import com.xianglin.xlnodecore.common.service.facade.vo.NodeVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xianglin.appserv.biz.shared.BusinessManager;
import com.xianglin.appserv.biz.shared.ProfitDetailManager;
import com.xianglin.appserv.biz.shared.ProfitManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.service.facade.app.EarnPageService;
import com.xianglin.appserv.common.service.facade.model.BusiVisitDTO;
import com.xianglin.appserv.common.service.facade.model.BusinessDTO;
import com.xianglin.appserv.common.service.facade.model.EarningDTO;
import com.xianglin.appserv.common.service.facade.model.ProfitDTO;
import com.xianglin.appserv.common.service.facade.model.ProfitDetailDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.MonthAchieveType;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.req.MonthAchieveQuery;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;

/**
 * @author zhangyong 2016年8月17日上午10:14:49
 */
@ServiceInterface
public class EarnPageServiceImpl implements EarnPageService {

    private static final Logger logger = LoggerFactory.getLogger(EarnPageServiceImpl.class);
    /**
     * @see com.xianglin.appserv.common.service.facade.app.EarnPageService#getEarnHomeData(java.lang.Long)
     */
    @Autowired
    private ProfitManager profitManager;

    @Autowired
    private BusinessManager businessManager;

    @Autowired
    private ProfitDetailManager profitDetailManager;

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private BankImportService bankImportService;

    @Autowired
    private LoginAttrUtil loginAttrUtil;
    
    @Autowired
    private FinanceService financeService;
    
    @Autowired
    private NodeService nodeService;

    @Autowired
    private ResponseCacheUtils responseCacheUtils;

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.EarnPageService.getEarnHomeData", description = "赚钱页面数据")
    public Response<EarningDTO> getEarnHomeData(Long partyId) {
        Response<EarningDTO> response = ResponseUtils.successResponse();
        EarningDTO earnDto = new EarningDTO();
        try {
            ProfitDTO profitDto = null;
            List<BusinessDTO> busiDtoList = businessManager.getBusinessList(null);
            earnDto.setBusinessDtoList(busiDtoList.stream().filter(v -> StringUtils.isNotEmpty(v.getBusinessType()) && StringUtils.isNotEmpty(v.getH5url())).collect(Collectors.toList()));
            String clientVersion = loginAttrUtil.getClientVersion();
            logger.info("current mobile version {}", clientVersion);
            if (StringUtils.equals(clientVersion, "1.1.0") || StringUtils.isEmpty(clientVersion)) {
                profitDto = profitManager.queryLocalProfit(partyId, null, MonthAchieveType.INCOME.name());
            } else {
                profitDto = profitManager.queryTopOne(partyId, getBusiTypes(), MonthAchieveType.INCOME.name());
            }
            earnDto.setProfitDto(profitDto);
            if (profitDto != null) {
                BigDecimal total = BigDecimal.ZERO;
                if (StringUtils.equals(clientVersion, "1.1.0") || StringUtils.isEmpty(clientVersion)) {
                    total = profitManager.queryTotalProfit(partyId, null, MonthAchieveType.INCOME.name());
                } else {
                    total = profitManager.queryTotal(partyId, getBusiTypes(), MonthAchieveType.INCOME.name());
                }
                profitDto.setTotal(NumberUtil.truncateBigDecimal(total, 2));
                String yearMonth = earnDto.getProfitDto().getDataPeriod();
                MonthAchieveQuery query = new MonthAchieveQuery();
                query.setMonth(yearMonth.substring(4));
                query.setYear(yearMonth.substring(0, 4));
                query.setStaticType(MonthAchieveType.INCOME.name());
                List<ProfitDetailDTO> list;
                list = profitDetailManager.getProfitDetailList(query, partyId);
                if (!CollectionUtils.isEmpty(list)) {
                    List<ProfitDetailDTO> detailDtos = DTOUtils.map(list, ProfitDetailDTO.class);
                    earnDto.setProfitDetailDto(detailDtos.get(0));
                }
            } else {
                logger.info("【赚钱页面数据查询无数据】");
            }

            BusiVisitDTO busiVisisDto = new BusiVisitDTO();

			
		/*	busiVisisDto.setBankManagerUrl(UrlPendingUtil.concatUrl(BaseUrl.H5WECHAT_URL_KEY, Constant.BusiVisitKey.BANK_MANAGER_URL.name()));
            busiVisisDto.setEshopManagerUrl(UrlPendingUtil.concatUrl(BaseUrl.H5WECHAT_URL_KEY, Constant.BusiVisitKey.ESHOP_MANAGER_URL.name()));
			busiVisisDto.setLoanManagerUrl(UrlPendingUtil.concatUrl(BaseUrl.H5WECHAT_URL_KEY, Constant.BusiVisitKey.LOAN_MANAGER_URL.name()));
			busiVisisDto.setLifeMangerUrl(UrlPendingUtil.concatUrl(BaseUrl.H5WECHAT_URL_KEY, Constant.BusiVisitKey.LIVE_MANAGER_URL.name()));
			busiVisisDto.setMobileChargeUrl(UrlPendingUtil.concatUrl(BaseUrl.H5WECHAT_URL_KEY,Constant.BusiVisitKey.MOBLE_MANAGER_UR.name()));
			*/
            busiVisisDto.setBankManagerUrl(SysConfigUtil.getStr(Constant.BusiVisitKey.BANK_MANAGER_URL.name()));
            busiVisisDto.setEshopManagerUrl(SysConfigUtil.getStr(Constant.BusiVisitKey.ESHOP_MANAGER_URL.name()));
            busiVisisDto.setLoanManagerUrl(SysConfigUtil.getStr(Constant.BusiVisitKey.LOAN_MANAGER_URL.name()));
            logger.info("current mobile version {}", clientVersion);
            if (StringUtils.equals(clientVersion, "1.1.0") || StringUtils.isEmpty(clientVersion)) {
                busiVisisDto.setLifeMangerUrl(SysConfigUtil.getStr(Constant.BusiVisitKey.MOBLE_MANAGER_URL.name()));
            } else {
                if (businessManager.isOpenjiaofei()) {
                    busiVisisDto.setLifeMangerUrl(SysConfigUtil.getStr(Constant.BusiVisitKey.LIFE_MANAGER_URL.name()));
                }
            }
            busiVisisDto.setMobileChargeUrl(SysConfigUtil.getStr(Constant.BusiVisitKey.MOBLE_MANAGER_URL.name()));

            earnDto.setBusiVisisDto(busiVisisDto);
            response.setResult(earnDto);
        } catch (Exception e) {
            response.setCode(ResponseEnum.DATA_ERROR.getCode());
            response.setTips(ResponseEnum.DATA_ERROR.getTips());
            response.setResult(earnDto);
            logger.warn("赚钱查询收益出现问题:", e);
            e.printStackTrace();
        }

        return response;
    }

    private List<String> getBusiTypes() {
        List<String> busiTypes = new ArrayList<>();
        busiTypes.add(MonthAchieveType.ERCHARGE.name());
        busiTypes.add(MonthAchieveType.PAY.name());
        busiTypes.add(MonthAchieveType.ESHOP.name());
        return busiTypes;
    }

    /**
     * @see com.xianglin.appserv.common.service.facade.app.EarnPageService#getProfitDetailList(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.EarnPageService.getProfitDetailList", description = "移动app赚钱收益点击更多")
    public Response<List<ProfitDetailDTO>> getProfitDetailList(String busiType, String staticType, Integer start,
                                                               Integer pageSize) {
        Response<List<ProfitDetailDTO>> response = ResponseUtils.successResponse();
        MonthAchieveQuery query = new MonthAchieveQuery();
        query.setPageSize(pageSize);
        query.setStartPage(start);
        query.setStaticType(staticType);
        query.setBusiType(busiType);
        List<ProfitDetailDTO> list;
        list = profitDetailManager.getProfitDetailList(query, null);
        if (!CollectionUtils.isEmpty(list)) {
            response.setResult(list);
        } else {
            response.setResult(new ArrayList<ProfitDetailDTO>());
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.EarnPageService.queryLastBankAchieve", description = "查询最新银行同步业绩")
    public Response<BankAchieveVo> queryLastBankAchieve() {
        Response<BankAchieveVo> resp = ResponseUtils.successResponse();
        try {
            //使用nodePartyId
            Long nodePartyId = sessionHelper.getSessionProp(SessionConstants.node_party_id, Long.class);
            BankImportReq req = new BankImportReq();
            BankImportVo vo = new BankImportVo();
            vo.setNodePartyId(nodePartyId);
            req.setVo(vo);
            BankImportListResp nodeResp = bankImportService.getBankImportList(req);
            if (CollectionUtils.isNotEmpty(nodeResp.getVo())) {
                BankImportVo respVo = nodeResp.getVo().get(0);
                resp.setResult(converBankAchieveVo(respVo));
            } else {
                resp.setResult(new BankAchieveVo());
            }
        } catch (Exception e) {
            logger.warn("queryLastBankAchieve error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 对象转换
     *
     * @param vo
     * @return
     */
    private BankAchieveVo converBankAchieveVo(BankImportVo vo) {
        BankAchieveVo achieve = new BankAchieveVo();
        achieve.setCardCount(vo.getCardCount());
        if (vo.getYearAverage() != null) {
            achieve.setYearAverage(NumberUtil.amountFormat(vo.getYearAverage().divide(new BigDecimal("10000"))));
        } else {
            achieve.setYearAverage("0.00");
        }
        if (vo.getMonthAverage() != null) {
            achieve.setMonthAverage(NumberUtil.amountFormat(vo.getMonthAverage().divide(new BigDecimal("10000"))));
        } else {
            achieve.setMonthAverage("0.00");
        }
        if (vo.getBalance() != null) {
            achieve.setBalance(NumberUtil.amountFormat(vo.getBalance().divide(new BigDecimal("10000"))));
        } else {
            achieve.setBalance("0.00");
        }
        achieve.setImportDate(DateFormatUtils.format(vo.getImportDate(), DateUtils.DATE_FMT));
        return achieve;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.EarnPageService.queryBankAchieves", description = "查询历史银行同步业绩")
    public Response<List<BankAchieveVo>> queryBankAchieves(PageReq req) {
        Response<List<BankAchieveVo>> resp = ResponseUtils.successResponse();
        resp.setResult(new ArrayList<BankAchieveVo>());
        try {
            List<BankAchieveVo> list = new ArrayList<>();
            Long nodePartyId = sessionHelper.getSessionProp(SessionConstants.node_party_id, Long.class);
            BankImportReq nodeReq = new BankImportReq();
            BankImportVo vo = new BankImportVo();
            vo.setNodePartyId(nodePartyId);
            nodeReq.setVo(vo);
            BankImportListResp nodeResp = bankImportService.getBankImportList(nodeReq);
            if (CollectionUtils.isNotEmpty(nodeResp.getVo())) {
                for (BankImportVo v : nodeResp.getVo()) {
                    list.add(converBankAchieveVo(v));
                }
            }
            int startIndex = (req.getStartPage() - 1) * req.getPageSize();
            int endIndex = 0;
            int pageCount = list.size() % req.getPageSize() == 0 ? list.size() / req.getPageSize() : list.size() / req.getPageSize() + 1;
            if (req.getStartPage() > pageCount) {
                logger.info("queryBankAchieves null");
            } else {
                if (req.getStartPage() != pageCount) {
                    endIndex = startIndex + 10;
                } else {
                    endIndex = list.size();
                }
                resp.setResult(list.subList(startIndex, endIndex));
            }
        } catch (Exception e) {
            logger.warn("queryBankAchieves error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.EarnPageService.queryNodeAchieveDetail", description = "查询站点业绩明细查询")
    public Response<List<NodeAchieveDetailVo>> queryNodeAchieveDetail(NodeAchieveReq req) {
        Response<List<NodeAchieveDetailVo>> resp = ResponseUtils.successResponse();
        try {
            Long nodePartyId = sessionHelper.getSessionProp(SessionConstants.node_party_id, Long.class);
            BankImportReq nodeReq = new BankImportReq();
            BankImportVo vo = new BankImportVo();
            vo.setNodePartyId(nodePartyId);
            nodeReq.setVo(vo);
            BankImportListResp nodeResp = bankImportService.getBankImportList(nodeReq);
            List<NodeAchieveDetailVo> respList = null;
            if (CollectionUtils.isNotEmpty(nodeResp.getVo())) {
                respList = converImportAchieve(nodeResp.getVo(), req.getType(), req.getYear(), req.getMonth());
            } else {
                respList = new ArrayList<>();
            }
            resp.setResult(respList);
        } catch (Exception e) {
            logger.warn("queryNodeAchieveDetail error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.EarnPageService.getFinanceDataByNodePartyId", description = "获取所有的理财导入数据")
    public Response<List<FinanceImportVo>> getFinanceDataByNodePartyId() {
        Response<List<FinanceImportVo>> resp = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                resp.setResonpse(ResponseEnum.SESSION_INVALD);
                return resp;
            }
            NodeReq nodeReq = new NodeReq();
            com.xianglin.xlnodecore.common.service.facade.vo.NodeVo nodeVo = new NodeVo();
            nodeVo.setNodeManagerPartyId(partyId);
            nodeReq.setVo(nodeVo);
            NodeResp nodeResp = nodeService.queryNodeInfoByNodeManagerPartyId(nodeReq);
            if(nodeReq.getVo() != null){
                FinanceImportDTO financeImportDTO = new FinanceImportDTO();
                financeImportDTO.setNodePartyId(nodeResp.getVo().getNodePartyId());
                logger.info("getFinanceDataByNodePartyId para:{}", financeImportDTO.toString());
                com.xianglin.xlnodecore.common.service.facade.base.Response<List<FinanceImportDTO>> financeDataByNodePartyId = financeService.getFinanceDataByNodePartyId(financeImportDTO);
                logger.info("getFinanceDataByNodePartyId result:{}", financeDataByNodePartyId.toString());
                if(financeDataByNodePartyId.isSuccess() && financeDataByNodePartyId.getResult().size()>0){
                    List<FinanceImportVo> financeImportVoList = financeDataByNodePartyId.getResult().stream()
                            .map(input -> {
                                FinanceImportVo output;
                                try {
                                    output = DTOUtils.map(input, FinanceImportVo.class);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                output.setBalance(input.getBalance().setScale(2,BigDecimal.ROUND_HALF_UP));
                                output.setYearAverage(input.getYearAverage().setScale(2,BigDecimal.ROUND_HALF_UP));
                                output.setMonthAverage(input.getMonthAverage().setScale(2,BigDecimal.ROUND_HALF_UP));
                                return output;
                            })
                            .collect(Collectors.toList());
                    resp.setResult(financeImportVoList);
                }
            }
        } catch (Exception e) {
            logger.warn("getFinanceDataByNodePartyId error", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }

    /**查询当前登陆用户对应的站点partyId
     * @return
     */
    private Optional<Long> getNodePartyId(){
        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
        NodeReq nodeReq = new NodeReq();
        com.xianglin.xlnodecore.common.service.facade.vo.NodeVo nodeVo = new NodeVo();
        nodeVo.setNodeManagerPartyId(partyId);
        nodeReq.setVo(nodeVo);
        NodeResp nodeResp = nodeService.queryNodeInfoByNodeManagerPartyId(nodeReq);
        return Optional.ofNullable(Optional.ofNullable(nodeResp.getVo()).map(v -> v.getNodePartyId()).orElse(null));
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.EarnPageService.queryNodeAchieveDetailV2", description = "图表数据查询")
    public Response<List<NodeAchieveDetailVo>> queryNodeAchieveDetailV2(NodeAchieveReq req) {
        // FIXME WANGLEI
        return responseCacheUtils.execute(() -> {
            ImportReq paras = new ImportReq();
            paras.setNodePartyId(getNodePartyId().orElseThrow(() -> new BusiException("没有查到对应的站点")));
            paras.setType(req.getType());
            paras.setCategory(req.getCategory());
            paras.setYear(req.getYear());
            paras.setMonth(req.getMonth());
            logger.info("queryPerformanceByType paras:{}", JSON.toJSONString(paras));
            return bankImportService.queryPerformanceByType(paras).getResult().stream().map(v -> {
                NodeAchieveDetailVo detailVo = new NodeAchieveDetailVo();
                detailVo.setValue(v.getValue());
                detailVo.setDateTime(v.getDateTime());
                return detailVo;
            }).collect(Collectors.toList());
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.EarnPageService.queryImportList", description = "业绩查询")
    public Response<String> queryImportList(PageReq req) {
        return responseCacheUtils.execute(() -> {
            BankImportReq paras = new BankImportReq();
            paras.setNodePartyId(getNodePartyId().orElseThrow(() -> new BusiException("没有查到对应的站点")));
            paras.setPageSize(req.getPageSize());
            paras.setStartPage(req.getStartPage());
            paras.setCurPage(req.getStartPage());
            return JSON.toJSONString(bankImportService.queryImportList(paras).getResult());
        });
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.EarnPageService.queryLastImport", description = "最近业绩查询")
    public Response<String> queryLastImport() {
        return responseCacheUtils.execute(() -> {
            return JSON.toJSONString(bankImportService.queryLastImport(getNodePartyId().orElseThrow(() -> new BusiException("没有查到对应的站点"))).getResult());
        });
    }

    /**
     * 数据转换
     *
     * @param vo
     * @param type
     * @param year
     * @param month
     * @return
     */
    private List<NodeAchieveDetailVo> converImportAchieve(List<BankImportVo> vo, String type, int year, int month) {

        SortedMap<Long, BigDecimal> nodeData = new TreeMap<>();
        for (BankImportVo v : vo) {
            nodeData.put(v.getImportDate().getTime(),getImportAchieveVal(v,type));
        }
//        Calendar nextMonth = Calendar.getInstance();
//        nextMonth.set(Calendar.DAY_OF_MONTH,1);
//        nextMonth.add(Calendar.MONTH,1);
//        nodeData.put(nextMonth.getTimeInMillis(),BigDecimal.ZERO);//设置下个月初一的值为0
        nodeData.put(System.currentTimeMillis(),BigDecimal.ZERO);//设置当日天值为0

        List<NodeAchieveDetailVo> result = new ArrayList<>();
        if (nodeData.size() > 0) {
            if (month != 0) {
                Calendar firstDay = Calendar.getInstance();
                firstDay.set(Calendar.YEAR, year);
                firstDay.set(Calendar.MONTH, month - 1);
                firstDay.set(Calendar.DAY_OF_MONTH, 1);
                int first = firstDay.get(Calendar.DAY_OF_MONTH);
                firstDay.add(Calendar.MONTH, 1);
                firstDay.add(Calendar.DATE, -1);
                int last = firstDay.get(Calendar.DAY_OF_MONTH);
                for (int day = first; day <= last; day++) {
                    NodeAchieveDetailVo achieve = new NodeAchieveDetailVo();
                    Calendar d = Calendar.getInstance();
                    d.set(Calendar.YEAR, year);
                    d.set(Calendar.MONTH, month - 1);
                    d.set(Calendar.DAY_OF_MONTH, day);
                    achieve.setDateTime(d.getTimeInMillis());
                    achieve.setValue(getLastValue(nodeData,d.getTimeInMillis()));
                    result.add(achieve);
                }
            } else {//按年取数据
                for (int m = 0; m < 12; m++) {
                    Calendar d = Calendar.getInstance();
                    d.set(Calendar.YEAR, year);
                    d.set(Calendar.MONTH, m);
                    d.set(Calendar.DAY_OF_MONTH, 1);
                    d.add(Calendar.MONTH, 1);//拿到下个月第一天
                    d.add(Calendar.DAY_OF_YEAR, -1);//得到前一天

                    NodeAchieveDetailVo achieve = new NodeAchieveDetailVo();
                    achieve.setDateTime(d.getTimeInMillis());
                    achieve.setValue(getLastValue(nodeData, d.getTimeInMillis()));
                    result.add(achieve);
                }
            }
        }
        return result;
    }

    /**
     * 取最接近的数据
     *
     * @param nodeData
     * @param day 最近一天的数据
     * @return
     */
    private BigDecimal getLastValue(SortedMap<Long, BigDecimal> nodeData, long day) {
        BigDecimal data = BigDecimal.ZERO;
        if(nodeData.containsKey(day)){
            data = nodeData.get(day);
        }else{
            for(long time:nodeData.keySet()){
                if(time < day){
                    data = nodeData.get(time);
                }
                if(time > day){
                    return data;
                }
            }
        }
        return data;
    }

    /**
     * 根据类型取对应的数值
     *
     * @param v
     * @param type
     * @return
     */
    private BigDecimal getImportAchieveVal(BankImportVo v, String type) {
        try {
            switch (type) {
                case "B":
                    return NumberUtil.truncateBigDecimal(v.getBalance().divide(new BigDecimal(10000)), 2);
                case "C":
                    return new BigDecimal(v.getCardCount());
                case "M":
                    return NumberUtil.truncateBigDecimal(v.getMonthAverage().divide(new BigDecimal(10000)), 2);
                case "Y":
                    return NumberUtil.truncateBigDecimal(v.getYearAverage().divide(new BigDecimal(10000)), 2);
            }
        } catch (Exception e) {
            logger.warn("data is null ", e);
        }
        return BigDecimal.ZERO;
    }


    /**
     * 数据转换
     *
     * @param vos
     * @param type
     * @param month
     * @return
     */
    private List<NodeAchieveDetailVo> converNodeAchieve(List<NodeDailyReportVo> vos, String type, int month) {
        SortedMap<Calendar, BigDecimal> map = new TreeMap<>();
        List<NodeAchieveDetailVo> result = new ArrayList<>();
        for (NodeDailyReportVo vo : vos) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(vo.getReportDate());
            map.put(cal, getAchieveVal(vo, type));
        }
        if (month == 0) {//按年查数据，需要对数据惊喜清洗
            map = nodeAchieveFilter(map);
        }
        NodeAchieveDetailVo vo = null;
        for (Map.Entry<Calendar, BigDecimal> entry : map.entrySet()) {
            vo = new NodeAchieveDetailVo();
            vo.setValue(entry.getValue());
            vo.setDateTime(entry.getKey().getTimeInMillis());
            result.add(vo);
        }
        return result;
    }

    /**
     * 对整年数据进行过滤，取每个月最后一天数据，没有则取最后一天
     *
     * @param map
     * @return
     */
    private SortedMap<Calendar, BigDecimal> nodeAchieveFilter(SortedMap<Calendar, BigDecimal> map) {
        SortedMap<Calendar, BigDecimal> data = new TreeMap<>();
        NodeAchieveDetailVo vo = new NodeAchieveDetailVo();
        for (int i = 0; i < 12; i++) {
            Calendar cal = null;
            for (Calendar d : map.keySet()) {
                if (d.get(Calendar.MONTH) == i) {
                    if (cal == null || d.after(cal)) {
                        cal = d;
                    }
                }
            }
            data.put(cal, map.get(cal));
        }
        return data;
    }

    /**
     * 根据条件取对应余额
     *
     * @param vo
     * @param type
     * @return
     */
    private BigDecimal getAchieveVal(NodeDailyReportVo vo, String type) {
        switch (type) {
            case "B":
                return vo.getBankBalance();
            case "C":
                return new BigDecimal(vo.getBankCardCount());
            case "Y":
                return vo.getBankBalanceMonthlyAvr();
            case "M":
                return vo.getBankBalanceMonthlyAvr();
        }
        return BigDecimal.ZERO;
    }

    /**
     * 日期格式化
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    private String formatDate(int year, int month, int day) {
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("-");
        if (month < 10) {
            sb.append("0");
        }
        sb.append(month).append("-");
        if (day < 10) {
            sb.append(0);
        }
        sb.append(day);
        return sb.toString();
    }

    public static void main(String[] args) {
//        SortedMap<Calendar,BigDecimal> map = new TreeMap<>();
//
//        for(int i=0;i<15;i++){
//            int random = RandomUtils.nextInt(1000,2000000000);
//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.SECOND,random);
//            map.put(cal,new BigDecimal(random));
//        }
//        for(Map.Entry<Calendar,BigDecimal> entry:map.entrySet()){
//            System.out.println(entry.getKey().get(Calendar.MONTH));
//            System.out.println(entry.getKey().getTime()+"="+entry.getValue());
//        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2016);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 1);//拿到下个月第一天
        cal.add(Calendar.MONTH, 1);

//        cal.add(Calendar.DAY_OF_YEAR, -1);

        System.out.println(cal.toString());

        Calendar cal2 = Calendar.getInstance();

        System.out.println(cal2.getTimeInMillis() - cal.getTimeInMillis());
        long count = TimeUnit.DAYS.convert(cal2.getTimeInMillis() - cal.getTimeInMillis(), TimeUnit.MILLISECONDS);
        System.out.println(count);

        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(1);

        Calendar end = Calendar.getInstance();
        System.out.println(TimeUnit.DAYS.convert(end.getTimeInMillis() - start.getTimeInMillis(), TimeUnit.MILLISECONDS));

        end.add(Calendar.DAY_OF_YEAR, 6);
        System.out.println(TimeUnit.DAYS.convert(end.getTimeInMillis() - start.getTimeInMillis(), TimeUnit.MILLISECONDS));

    }
}
