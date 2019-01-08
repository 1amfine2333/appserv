package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.*;

/**
 * Describe :
 * Created by xingyali on 2017/10/23 14:59.
 * Update reason :
 */
public interface InterestService {

    /*
    * 通用秒息宝接口,需要登录
    * */
    public Response<String> currencyIntegerestLogin(String tranCode, String content);

    /*
    * 通用秒息宝接口,需要登录
    * */
    public Response<String> hCurrencyIntegerestLogin(String tranCode, String content ,String sessionId);

    /**
     * 通用秒息宝接口，不需要登录
     *
     */
    public Response<String> currencyIntegerest(String tranCode,String content);

    /**
     * 交易密码确认
     */
    public Response<Boolean> confirmPassword(String password);

    /**
     * 身份证姓名确认
     */
    public Response<Boolean> confirmUserNameAndIdNumber(String userName,String idNumber);

    /**
     * 中原银行业务通用接口(调账房提供) 需要登录
     * @param trxnCode
     * @param content
     * @return
     */
    public Response<String> busiRequestStr(String trxnCode,String content);

    /**
     * 预申请用户访问令牌信息(调账房提供) 需要登录
     * @return
     */
    public Response<Object> preApplyChannelAccessToken();

    /**
     * 查询基金产品信息（七日年化收益）
     * @param iZyFnProductQueryReqDTO
     * @return
     */
    public Response<IZyFnProductInfoRespDTO> getFnZyProductInfo(IZyFnProductQueryReqDTO iZyFnProductQueryReqDTO);

    /**
     * 中原银行理财-用户充值
     * @param iFnRechargeAndWithdrawalsReqDTO
     * @return
     */
    public Response<IBalanceOrderDTO> rechargeMoney(IFnRechargeAndWithdrawalsReqDTO iFnRechargeAndWithdrawalsReqDTO);

    /**
     * 中原银行理财-用户提现
     * @param iFnRechargeAndWithdrawalsReqDTO
     * @return
     */
    public Response<IBalanceOrderDTO> withdrawCash(IFnRechargeAndWithdrawalsReqDTO iFnRechargeAndWithdrawalsReqDTO);

    /**
     * 中原银行理财-基金申购
     * @param iFnFundPurchaseReqDTO
     * @return
     */
    public Response<IProductOrderDTO> fundPurchase(IFnFundPurchaseReqDTO iFnFundPurchaseReqDTO);

    /**
     * 中原银行理财-基金赎回
     * @param iFnFundRedeemReqDTO
     * @return
     */
    public Response<IProductOrderDTO> fundRedeem(IFnFundRedeemReqDTO iFnFundRedeemReqDTO);

}
