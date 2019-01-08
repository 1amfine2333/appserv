package com.xianglin.appserv.biz.service.implement;

import com.xianglin.appserv.biz.shared.BusinessManager;
import com.xianglin.appserv.biz.shared.util.ResponseCacheUtils;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.service.facade.app.AccountantMerchantService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.vo.MerchantInfoVo;
import com.xianglin.appserv.common.service.facade.model.vo.MerchantOrderVo;
import com.xianglin.appserv.common.service.facade.model.vo.MerchantSubAmountVo;
import com.xianglin.appserv.common.util.NumberUtil;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.merchant.common.service.facade.MerchantPayServiceFacade;
import com.xianglin.merchant.common.service.facade.MerchantServiceFacade;
import com.xianglin.merchant.common.service.facade.dto.*;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by wanglei on 2017/9/11.
 */
@Service
@ServiceInterface
public class AccountantMerchantServiceImpl implements AccountantMerchantService {

    private final Logger logger = LoggerFactory.getLogger(AccountantMerchantServiceImpl.class);

    @Autowired
    private MerchantServiceFacade merchantServiceFacade;

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private MerchantPayServiceFacade merchantPayServiceFacade;

    @Autowired
    private BusinessManager businessManager;

    @Autowired
    private ResponseCacheUtils responseCacheUtils;

    /**
     * 查询用户默认商家
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.AccountantMerchantService.queryDefaultMerchant", description = "查询用户默认商家")
    public Response<MerchantInfoVo> queryDefaultMerchant() {
        Response<MerchantInfoVo> resp = ResponseUtils.successResponse();
        try {
            Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            LoginUserDTO loginUserDTO = new LoginUserDTO();
            ResponseDTO<MerchantShopInfoDTO> mResp = merchantServiceFacade.findDefaultShopByPartyId(userPartyId + "", loginUserDTO);
            logger.info("merchantServiceFacade findDefaultMerchantByPartyId resp {}", ToStringBuilder.reflectionToString(mResp));
            if (mResp.isSuccess()) {
                MerchantInfoVo vo = new MerchantInfoVo();
                vo.setMerchantCode(mResp.getResult().getMerchantCode());
                vo.setMerchantFullname(mResp.getResult().getShopname());
                if (StringUtils.isEmpty(mResp.getResult().getShopshortname())) {
                    vo.setMerchantName(mResp.getResult().getShopname());
                } else {
                    vo.setMerchantName(mResp.getResult().getShopshortname());
                }
                vo.setShopCode(mResp.getResult().getMerchantShopCode());
                resp.setResult(vo);
            } else {
                resp.setCode(Integer.valueOf(mResp.getCode()));
                resp.setTips(mResp.getTip());
            }
        } catch (Exception e) {
            logger.error("queryDefaultMerchant error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 提交订单信息，
     * 参数及返回待定
     *
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.AccountantMerchantService.submitPreOrder", description = "提交订单信息")
    public Response<MerchantOrderVo> submitPreOrder(MerchantOrderVo order) {
        Response<MerchantOrderVo> resp = ResponseUtils.successResponse(order);
        try {
            Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            //校验订单金额是否大于99999999.9999
            BigDecimal bigDecimal = new BigDecimal("99999999.9999");
            BigDecimal orderAmout = new BigDecimal(order.getOrderAmount());
            if (orderAmout.compareTo(bigDecimal) == 1) {
                resp.setFacade(FacadeEnums.ERROR_CHAT_400041);
                resp.setMemo("订单金额不能超过99999999.9999元！");
                resp.setTips("订单金额不能超过99999999.9999元！");
                return resp;
            } else {
                RqPayOrderDTO dto = new RqPayOrderDTO();
                dto.setPartyId(userPartyId + "");
                dto.setAuthCode(order.getAuthCode());
                dto.setOrderAmount(new BigDecimal((order.getOrderAmount())));
                dto.setOrderInfo(order.getOrderInfo());
                dto.setShopCode(order.getShopCode());
                ResponseDTO<RqPayResultDTO> mResp = merchantPayServiceFacade.passivePay(dto);
                logger.info("merchantPayServiceFacade passivePay resp {}", ToStringBuilder.reflectionToString(mResp));
                if (mResp.isSuccess()) {
                    order.setOrderNo(mResp.getResult().getOrderNo());
                } else {
                    resp.setCode(Integer.valueOf(mResp.getCode()));
                    resp.setTips(mResp.getTip());
                }
            }
        } catch (Exception e) {
            logger.error("submitPreOrder error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    /**
     * 查询订单状态
     *
     * @param orderId
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.AccountantMerchantService.queryOrderStatus", description = "查询订单状态")
    public Response<MerchantOrderVo> queryOrderStatus(String orderId) {
        Response<MerchantOrderVo> response = ResponseUtils.successResponse();
        MerchantOrderVo vo = new MerchantOrderVo();
        try {
            if (StringUtils.isNotEmpty(orderId) && orderId.length() >= 4) {
                Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
                MerchantOrderParamDTO dto = new MerchantOrderParamDTO();
                dto.setOrderNo(orderId);
                ResponseDTO<MerchantRqpayOrderInfoDTO> resp = merchantServiceFacade.findMerchantOrderByNo(dto);
                logger.info("findMerchantOrderByNo resp {}", ToStringBuilder.reflectionToString(resp));
                if (resp.isSuccess()) {
                    if (resp.getResult() != null) {
                        String amount = NumberUtil.amountFormat(String.valueOf(resp.getResult().getOrderSubAmount()));
                        vo.setOrderAmount(amount);
                        vo.setOrderNo(resp.getResult().getOrderNo());
                        vo.setOrderStatus(resp.getResult().getOrderStatus());
                        LoginUserDTO loginUserDTO = new LoginUserDTO();
                        ResponseDTO<MerchantShopInfoDTO> shop = merchantServiceFacade.findDefaultShopByPartyId(userPartyId + "", loginUserDTO);
                        logger.info("findDefaultShopByPartyId resp {}", ToStringBuilder.reflectionToString(resp));
                        if (shop.isSuccess()) {
                            if (shop.getResult().getTipFlag().equals("Y") && resp.getResult().getOrderStatus().equals("02")) {    //在d店铺语音开启的情况下并且订单成功的状态下，提示：您收到xx元
                                String strsub = orderId.substring(orderId.length() - 4);
                                vo.setAlertTip("您收到" + amount + "元 订单号后四位" + strsub);
                            }
                        } else {
                            response.setCode(Integer.valueOf(shop.getCode()));
                            response.setTips(shop.getTip());
                        }
                        response.setResult(vo);
                    }
                } else {
                    response.setCode(Integer.valueOf(resp.getCode()));
                    response.setTips(resp.getTip());
                }
            }
        } catch (Exception e) {
            logger.error("queryOrderStatus error:", e);
        }

        return response;
    }


    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.AccountantMerchantService.querySubAmountAndUrl", description = "查询订单的优惠金额和URL")
    public Response<MerchantSubAmountVo> querySubAmountAndUrl(MerchantSubAmountVo merchantSubAmountVo) {
        Response<MerchantSubAmountVo> response = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                response.setResonpse(ResponseEnum.SESSION_INVALD);
                return response;
            }
            //校验订单金额是否大于99999999.9999
            BigDecimal bigDecimal = new BigDecimal("99999999.9999");
            BigDecimal orderAmout = new BigDecimal(merchantSubAmountVo.getOrderAmount());
            if (orderAmout.compareTo(bigDecimal) == 1) {
                response.setFacade(FacadeEnums.ERROR_CHAT_400042);
                response.setMemo("订单金额不能超过99999999.9999元！");
                response.setTips("订单金额不能超过99999999.9999元！");
                return response;
            } else {
                QRPayDTO qRPayDTO = new QRPayDTO();
                qRPayDTO.setOrderAmount(merchantSubAmountVo.getOrderAmount());
                qRPayDTO.setShopCode(merchantSubAmountVo.getShopCode());
                LoginUserDTO loginUserDTO = new LoginUserDTO();
                loginUserDTO.setPartyId(partyId + "");
                ResponseDTO<QRPayResultDTO> resp = merchantPayServiceFacade.qrReturnInfo(qRPayDTO, loginUserDTO);
                if (resp.isSuccess()) {
                    merchantSubAmountVo.setOrderAmount(resp.getResult().getOrderAmount());
                    merchantSubAmountVo.setSubAmount(resp.getResult().getSubAmount());
                    merchantSubAmountVo.setRealAmount(resp.getResult().getRealAmount());
                    merchantSubAmountVo.setUrl(resp.getResult().getUrl());
                    response.setResult(merchantSubAmountVo);
                } else {
                    response.setCode(Integer.valueOf(resp.getCode()));
                    response.setTips(resp.getTip());
                }
            }
        } catch (Exception e) {
            logger.error("querySubAmountAndUrl error", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.AccountantMerchantService.submitPreOrderTest", description = "提交订单信息（用于测试不上生产）", timeout = 600000)
    public Response<MerchantOrderVo> submitPreOrderTest(MerchantOrderVo order) {
        Response<MerchantOrderVo> resp = ResponseUtils.successResponse(order);
        try {
            Long userPartyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            RqPayOrderDTO dto = new RqPayOrderDTO();
            dto.setPartyId(order.getOrderNo());//orderNo 用于partyId
            dto.setAuthCode(order.getAuthCode());
            dto.setOrderAmount(new BigDecimal((order.getOrderAmount())));
            dto.setOrderInfo(order.getOrderInfo());
            dto.setShopCode(order.getShopCode());
            ResponseDTO<RqPayResultDTO> mResp = merchantPayServiceFacade.passivePay(dto);
            logger.info("merchantPayServiceFacade passivePay resp {}", ToStringBuilder.reflectionToString(mResp));
            if (mResp.isSuccess()) {
                order.setOrderNo(mResp.getResult().getOrderNo());
            } else {
                resp.setCode(Integer.valueOf(mResp.getCode()));
                resp.setTips(mResp.getTip());
            }
        } catch (Exception e) {
            logger.error("submitPreOrder error", e);
            resp = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return resp;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.AccountantMerchantService.queryMerchantStatus", description = "查询当前用户是否开通电商")
    public Response<Boolean> queryMerchantStatus() {
        return responseCacheUtils.execute(() -> {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            if (partyId == null) {
                return false;
            }
            return businessManager.queryUserBusiness().contains(Constant.BusinessType.MERCHANT.name());
        });
    }
}
