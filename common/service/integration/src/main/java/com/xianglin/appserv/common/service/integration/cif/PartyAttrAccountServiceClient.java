/**
 * 
 */
package com.xianglin.appserv.common.service.integration.cif;

import com.xianglin.cif.common.service.facade.model.Response;
import com.xianglin.cif.common.service.facade.req.PartyAttrPasswordReq;
import com.xianglin.cif.common.service.facade.resp.PartyAttrAccountResp;
import com.xianglin.cif.common.service.facade.resp.PartyAttrPasswordResp;

/**
 * 
 *
 * @author zhangyong 2016年8月26日上午10:36:20
 */
public interface PartyAttrAccountServiceClient {
	
	/**
	 * c查询是否已开户
	 * 
	 * @param partyId
	 * @return
	 */
	PartyAttrAccountResp getPartyAttrAccount(long partyId);

	/**
	 * 查询交易密码
	 * @param partyId
	 * @return
	 */
	PartyAttrPasswordResp selectTradePwd(Long partyId);

	/** 校验交易密码
	 * @param partyId
	 * @param pwd
	 * @return
	 */
	Response<Boolean> checkTradePwd(String pwd,String sessionId);

}
