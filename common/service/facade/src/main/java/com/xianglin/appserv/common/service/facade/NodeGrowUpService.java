package com.xianglin.appserv.common.service.facade;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.NodeMonthReportDetailVo;
import com.xianglin.appserv.common.service.facade.model.vo.NodeMonthReportVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.NodeGrowUpInfoModel;

import java.util.List;

/**
 * 站长成长史
 * 
 * @author liuhong
 *
 */
public interface NodeGrowUpService {
	/**站长成长史
	 * @param partyId
	 * @return
	 * @throws Exception
	 */
	NodeGrowUpInfoModel findAllByPartyId(Long partyId) throws Exception;
	/**
	 * @param req
	 * @param partyId
	 * @return
	 */
	Response<List<NodeMonthReportVo>> queryMonthReports(PageReq req, Long partyId);

	/**查询站长月报明细
	 * @param id
	 * @return
	 */
	Response<NodeMonthReportDetailVo> queryMonthReportDetail(Long id);


}
