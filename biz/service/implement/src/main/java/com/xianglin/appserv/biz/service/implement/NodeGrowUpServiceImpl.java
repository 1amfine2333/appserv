package com.xianglin.appserv.biz.service.implement;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.NodeMonthReportDetailVo;
import com.xianglin.appserv.common.service.facade.model.vo.NodeMonthReportVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.daointerface.NodeGrowUpInfoMapper;
import com.xianglin.appserv.common.dal.dataobject.NodeGrowUpInfo;
import com.xianglin.appserv.common.service.facade.NodeGrowUpService;
import com.xianglin.appserv.common.service.facade.req.NodeGrowUpInfoModel;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;

/**
 * 站长成长史
 * 
 * @author liuhong
 *
 */
@Service("nodeGrowUpService")
@ServiceInterface
public class NodeGrowUpServiceImpl implements NodeGrowUpService {
	private static final Logger logger = LoggerFactory.getLogger(EarnPageServiceImpl.class);
	@Autowired
	private NodeGrowUpInfoMapper nodeGrowUpInfoMapper;
	@Autowired
	private SessionHelper sessionHelper;
	/**
	 * 查小站成长信息
	 */
	public NodeGrowUpInfoModel findAllByPartyId(Long partyId1) throws Exception {
		String partyId = Long.toString(partyId1);
		NodeGrowUpInfoModel nodeGrowUpInfoModel = new NodeGrowUpInfoModel();
		try {
			NodeGrowUpInfo nodeGrowUpInfo = nodeGrowUpInfoMapper.selectByAllPartyId(partyId);
			if (nodeGrowUpInfo != null) {
				// 小站第n天
				Date smdate = nodeGrowUpInfo.getOpenDate();
				Date bdate = new Date();

				int intervalDays = DateUtils.getIntervalDays(smdate, bdate);

				// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				// smdate = sdf.parse(sdf.format(smdate));
				// bdate = sdf.parse(sdf.format(bdate));
				// Calendar cal = Calendar.getInstance();
				// cal.setTime(smdate);
				// long time1 = cal.getTimeInMillis();
				// cal.setTime(bdate);
				// long time2 = cal.getTimeInMillis();
				// long between_days = (time2 - time1) / (1000 * 3600 * 24);
				// int time = Integer.parseInt(String.valueOf(between_days));
				nodeGrowUpInfoModel.setCreateTime(intervalDays);
				// 创建日期,全国排名
				nodeGrowUpInfoModel.setCreateDate(nodeGrowUpInfo.getOpenDate());
				nodeGrowUpInfoModel.setOrderAll(nodeGrowUpInfo.getOrderAll());
				// 第一次购物及金额
				nodeGrowUpInfoModel.setFirstProduct(nodeGrowUpInfo.getFirstProduct());
				nodeGrowUpInfoModel.setFirstOrderPrice(nodeGrowUpInfo.getFirstOrderPrice());
				// 银行状态
				nodeGrowUpInfoModel.setBankState(nodeGrowUpInfo.getBankState());

				// 全国小站个数,省,省排名//
				nodeGrowUpInfoModel.setProvince(nodeGrowUpInfo.getProvince());
				nodeGrowUpInfoModel.setOrderProvince(nodeGrowUpInfo.getOrderProvince());
				int countByProvince = nodeGrowUpInfoMapper.selectCountByProvince(nodeGrowUpInfo.getProvince());
				nodeGrowUpInfoModel.setNodeCountByProvince(countByProvince);
				// 开通银行日期,月均,余额,击败%人,描述// 月均收入
				nodeGrowUpInfoModel.setBankDate(nodeGrowUpInfo.getBankDate());
				nodeGrowUpInfoModel.setBalance(nodeGrowUpInfo.getBalance());
				// 余额击败%的人
				int selectCount = nodeGrowUpInfoMapper.selectCount();
				int balanceCount = nodeGrowUpInfoMapper.selectCountByBalance(nodeGrowUpInfo.getBalance());

				BigDecimal ch = new BigDecimal(balanceCount);
				BigDecimal result = ch.divide(new BigDecimal(selectCount), 2, BigDecimal.ROUND_HALF_UP);
				NumberFormat nf = NumberFormat.getPercentInstance();
				String percentage = nf.format(result.doubleValue());
				// Double a = new Double(balanceCount / selectCount);
				// String percentage = formatter.format(a);
				nodeGrowUpInfoModel.setPercentage(percentage);
				// 小站总个数
				nodeGrowUpInfoModel.setNodeCount(selectCount);
				BigDecimal bal = nodeGrowUpInfo.getBalance();
				BigDecimal c = new BigDecimal(10000);
				int i = bal.compareTo(c);
				// 余额描述
				try {
					if (i >= 0) {
						nodeGrowUpInfoModel.setPercentageInfo("让人羡慕 ！");
					} else {
						nodeGrowUpInfoModel.setPercentageInfo("请努力并继续加油 ！");
					}
				} catch (Exception e) {

				}
				// 电商开通时间
				nodeGrowUpInfoModel.setCommerceDate(nodeGrowUpInfo.getCommerceDate());
				nodeGrowUpInfoModel.setCommerceState(nodeGrowUpInfo.getCommerceState());
			} else {
				nodeGrowUpInfoModel = null;
			}
		} catch (Exception e) {

		}
		return nodeGrowUpInfoModel;
	}
	
    @Override
	@ServiceMethod(alias="com.xianglin.appserv.common.service.facade.NodeGrowUpService.queryMonthReports", description = "查询站长月报列表")
	public Response<List<NodeMonthReportVo>> queryMonthReports(PageReq req, Long partyId) {
    	Response<List<NodeMonthReportVo>> response = ResponseUtils.successResponse();
    	try {
			Long userId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
			//判断partyID是否为空
			if (partyId == null || userId == null) {
			    response.setResonpse(ResponseEnum.SESSION_INVALD);
			    return response;
			}
			List<NodeMonthReportVo> list=new ArrayList<NodeMonthReportVo>();
			NodeMonthReportVo nmr13=new NodeMonthReportVo();
			nmr13.setMonth("一月");
			nmr13.setReportName("2017年1月小站经营报告");
			nmr13.setCreateTime("2017-02-01");
			nmr13.setReportUrl(SysConfigUtil.getStr("monthReportUrl"));
			list.add(nmr13);
			NodeMonthReportVo nmr12=new NodeMonthReportVo();
			nmr12.setMonth("十二月");
			nmr12.setReportName("2016年12月小站经营报告");
			nmr12.setCreateTime("2017-01-01");
			nmr12.setReportUrl(SysConfigUtil.getStr("monthReportUrl"));
			list.add(nmr12);
			NodeMonthReportVo nmr11=new NodeMonthReportVo();
			nmr11.setMonth("十一月");
			nmr11.setReportName("2016年11月小站经营报告");
			nmr11.setCreateTime("2016-12-01");
			nmr11.setReportUrl(SysConfigUtil.getStr("monthReportUrl"));
			list.add(nmr11);
			NodeMonthReportVo nmr10=new NodeMonthReportVo();
			nmr10.setMonth("十月");
			nmr10.setReportName("2016年10月小站经营报告");
			nmr10.setCreateTime("2016-11-01");
			nmr10.setReportUrl(SysConfigUtil.getStr("monthReportUrl"));
			list.add(nmr10);
			NodeMonthReportVo nmr9=new NodeMonthReportVo();
			nmr9.setMonth("九月");
			nmr9.setReportName("2016年9月小站经营报告");
			nmr9.setCreateTime("2016-10-01");
			nmr9.setReportUrl(SysConfigUtil.getStr("monthReportUrl"));
			list.add(nmr9);
			NodeMonthReportVo nmr8=new NodeMonthReportVo();
			nmr8.setMonth("八月");
			nmr8.setReportName("2016年8月小站经营报告");
			nmr8.setCreateTime("2016-09-01");
			nmr8.setReportUrl(SysConfigUtil.getStr("monthReportUrl"));
			list.add(nmr8);
			NodeMonthReportVo nmr7=new NodeMonthReportVo();
			nmr7.setMonth("七月");
			nmr7.setReportName("2016年7月小站经营报告");
			nmr7.setCreateTime("2016-08-01");
			nmr7.setReportUrl(SysConfigUtil.getStr("monthReportUrl"));
			list.add(nmr7);
			NodeMonthReportVo nmr6=new NodeMonthReportVo();
			nmr6.setMonth("六月");
			nmr6.setReportName("2016年6月小站经营报告");
			nmr6.setCreateTime("2016-07-01");
			nmr6.setReportUrl(SysConfigUtil.getStr("monthReportUrl"));
			list.add(nmr6);
			NodeMonthReportVo nmr5=new NodeMonthReportVo();
			nmr5.setMonth("五月");
			nmr5.setReportName("2016年5月小站经营报告");
			nmr5.setCreateTime("2016-06-01");
			nmr5.setReportUrl(SysConfigUtil.getStr("monthReportUrl"));
			list.add(nmr5);
			NodeMonthReportVo nmr4=new NodeMonthReportVo();
			nmr4.setMonth("四月");
			nmr4.setReportName("2016年4月小站经营报告");
			nmr4.setCreateTime("2016-05-01");
			nmr4.setReportUrl(SysConfigUtil.getStr("monthReportUrl"));
			list.add(nmr4);
			NodeMonthReportVo nmr3=new NodeMonthReportVo();
			nmr3.setMonth("三月");
			nmr3.setReportName("2016年3月小站经营报告");
			nmr3.setCreateTime("2016-04-01");
			nmr3.setReportUrl(SysConfigUtil.getStr("monthReportUrl"));
			list.add(nmr3);
			NodeMonthReportVo nmr2=new NodeMonthReportVo();
			nmr2.setMonth("二月");
			nmr2.setReportName("2016年2月小站经营报告");
			nmr2.setCreateTime("2016-03-01");
			nmr2.setReportUrl(SysConfigUtil.getStr("monthReportUrl"));
			list.add(nmr2);
			NodeMonthReportVo nmr1=new NodeMonthReportVo();
			nmr1.setMonth("一月");
			nmr1.setReportName("2016年1月小站经营报告");
			nmr1.setCreateTime("2016-02-01");
			nmr1.setReportUrl(SysConfigUtil.getStr("monthReportUrl"));
			list.add(nmr1);
			int startIndex = (req.getStartPage() - 1) * req.getPageSize();
			int endIndex = 0;
			int pageCount = list.size() % req.getPageSize() == 0 ? list.size() / req.getPageSize() : list.size() / req.getPageSize() + 1;
			if (req.getStartPage() > pageCount) {
			    logger.info("queryMonthReports null");
			} else {
			    if (req.getStartPage() != pageCount) {
			        endIndex = startIndex + 10;
			    } else {
			        endIndex = list.size();
			    }
			    response.setResult(list.subList(startIndex, endIndex));
			}
		} catch (Exception e) {
			logger.error("queryMonthReports ", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
		}
		return response;
    }

	/**
	 * 查询站长月报明细
	 *
	 * @param id
	 * @return
	 */
	@Override
	public Response<NodeMonthReportDetailVo> queryMonthReportDetail(Long id) {
		return null;
	}
	
}
