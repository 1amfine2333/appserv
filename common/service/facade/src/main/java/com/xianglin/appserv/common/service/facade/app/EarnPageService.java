/**
 * 
 */
package com.xianglin.appserv.common.service.facade.app;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.EarningDTO;
import com.xianglin.appserv.common.service.facade.model.ProfitDetailDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.BankAchieveVo;
import com.xianglin.appserv.common.service.facade.model.vo.FinanceImportVo;
import com.xianglin.appserv.common.service.facade.model.vo.NodeAchieveDetailVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.NodeAchieveReq;

/**
 * 
 * 赚钱页面服务接口
 * @author zhangyong 2016年8月17日上午10:03:43
 */
public interface EarnPageService {

	/**
	 * 赚钱首页数据
	 * 
	 * @param partyId
	 * @return
	 */
	Response<EarningDTO> getEarnHomeData(Long partyId);
	
	/**
	 * 收益|业绩 列表
	 * @param busiType
	 * @param staticType
	 * @param start
	 * @param pageSize
	 * @return
	 */
	Response<List<ProfitDetailDTO>> getProfitDetailList(String busiType,String staticType,Integer start,Integer pageSize);

	/**查询站长最新银行业绩
	 * @return
	 */
	Response<BankAchieveVo> queryLastBankAchieve();

	/**查询站长历史银行业绩
	 * @param req
	 * @return
	 */
	Response<List<BankAchieveVo>> queryBankAchieves(PageReq req);

	/**查询站点业绩明细，用于绘制客户端站点业绩报表
	 * @param req
	 * @return
	 */
	Response<List<NodeAchieveDetailVo>> queryNodeAchieveDetail(NodeAchieveReq req);

    /**
     * 获取所有的理财导入数据
     * @return
     */
	Response<List<FinanceImportVo>> getFinanceDataByNodePartyId();

	/**图表数据查询
	 * @param req
	 * @return
	 */
	Response<List<NodeAchieveDetailVo>> queryNodeAchieveDetailV2(NodeAchieveReq req);

	/**业绩查询
	 * @return
	 */
	Response<String> queryImportList(PageReq req);

    /** 查询最近业绩
     * @return
     */
    Response<String> queryLastImport();

}
