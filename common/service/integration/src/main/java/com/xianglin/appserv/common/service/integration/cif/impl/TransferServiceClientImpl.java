/**
 * 
 */
package com.xianglin.appserv.common.service.integration.cif.impl;

import com.alibaba.fastjson.JSONArray;
import com.xianglin.appserv.common.service.integration.cif.TransferServiceClient;
import com.xianglin.te.common.service.facade.enums.Constants;
import com.xianglin.te.common.service.facade.req.AccountBalChangeReq;
import com.xianglin.te.common.service.facade.req.AccountBalQueryReq;
import com.xianglin.te.common.service.facade.resp.AccountBalChangeResp;
import com.xianglin.te.common.service.facade.resp.AccountBalQueryResp;
import com.xianglin.te.common.service.facade.resp.Response;
import com.xianglin.te.common.service.facade.vo.AccTransfer;
import com.xianglin.te.common.service.facade.vo.AcctBalanceModelVo;
import com.xianglin.te.common.service.facade.vo.AcctChangeUpTe;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyong 2016年10月18日上午10:43:14
 */
@Service
public class TransferServiceClientImpl implements TransferServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(TransferServiceClientImpl.class);

	@Autowired(required =false)
	private com.xianglin.te.common.service.facade.TransferService transferService;

	@Override
	public Response<AccountBalChangeResp> accountBalChange(AccountBalChangeReq req) {

		return transferService.accountBalChange(req);
	}

    @Override
    public Response<String> abQuery(Long partyId) {
        Response<String> response = new Response<>();
        AccountBalQueryReq accountBalQueryReq = new AccountBalQueryReq();
        accountBalQueryReq.setPartyId(partyId);
        accountBalQueryReq.setMemberCode(String.valueOf(partyId));
        accountBalQueryReq.setAccountBalanceType(Constants.AccountBalTypeEnums.ACCOUNT_BAL_PRINCIPAL.code);
        AccountBalQueryResp resp = transferService.abQuery(accountBalQueryReq);
        List<AcctBalanceModelVo> list;
        if (Constants.FacadeEnums.OK.code.equals(resp.getCode()) && CollectionUtils.isNotEmpty(list = resp.getBalanceList())) {
            AcctBalanceModelVo vo = list.get(0);
            response.setBaseResp(Constants.FacadeEnums.OK);
            response.setResult(vo.getBalance());
        } else {
            response.setBaseResp(Constants.FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

	@Override
	public Response<Boolean> accountBalTransfer(AcctChangeUpTe changeVo, String orderNumber, String subOrderNumber) {
		Response<Boolean> resp = new Response<>();
		try {
			resp.setResult(false);
			AccTransfer trans = new AccTransfer();
			trans.setOrderRequest(changeVo.getOrderSeqId());
			trans.setOrderNumber(orderNumber);
			trans.setOrderSubNumber(subOrderNumber);
			List<AcctChangeUpTe> acctChangeUpTesObject = new ArrayList<>(1);
			acctChangeUpTesObject.add(changeVo);
			trans.setAcctChangeUpTesObject(acctChangeUpTesObject);
			trans.setAcctChangeUpTes(JSONArray.toJSONString(acctChangeUpTesObject));
			Response<List<AcctChangeUpTe>> list = transferService.accountBalTransfer(trans);
			if(CollectionUtils.isNotEmpty(list.getResult())){
                AcctChangeUpTe change = list.getResult().get(0);
                if(StringUtils.equals(change.getAcctHandleCode(),"000000")){//成功
                    resp.setResult(true);
                }
            }
		} catch (Exception e) {
			logger.error("accountBalTransfe  r",e);
		}
		return resp;
	}

}
