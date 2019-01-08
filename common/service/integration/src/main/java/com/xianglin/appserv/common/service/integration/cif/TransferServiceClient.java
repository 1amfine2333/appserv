package com.xianglin.appserv.common.service.integration.cif;
/**
 *
 */

import com.xianglin.te.common.service.facade.req.AccountBalChangeReq;
import com.xianglin.te.common.service.facade.req.AccountBalQueryReq;
import com.xianglin.te.common.service.facade.resp.AccountBalChangeResp;
import com.xianglin.te.common.service.facade.resp.AccountBalQueryResp;
import com.xianglin.te.common.service.facade.resp.Response;
import com.xianglin.te.common.service.facade.vo.AcctChangeUpTe;

/**
 *
 *
 * @author zhangyong 2016年10月18日上午10:29:47
 */
public interface TransferServiceClient {

	/** 资金账户余额变更
	 * @param req
	 * @return
	 */
	Response<AccountBalChangeResp>  accountBalChange(AccountBalChangeReq req);

	/**
	 * 余额查询
	 * @param partyId
	 * @return
     */
	Response<String> abQuery(Long partyId);

	/** 资金账户转账
	 * @param changeVo
	 * @param orderNumber
	 * @param subOrderNumber
	 * @return
	 */
	Response<Boolean> accountBalTransfer(AcctChangeUpTe changeVo,String orderNumber,String subOrderNumber);
}
