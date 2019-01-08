package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.MerchantInfoVo;
import com.xianglin.appserv.common.service.facade.model.vo.MerchantOrderVo;
import com.xianglin.appserv.common.service.facade.model.vo.MerchantSubAmountVo;

/** 乡邻账房相关接口
 * Created by wanglei on 2017/9/8.
 */
public interface AccountantMerchantService {

    /**查询用户默认商家
     * @return
     */
    Response<MerchantInfoVo> queryDefaultMerchant();

    /**提交订单信息，
     * 参数及返回待定
     * @return
     */
    Response<MerchantOrderVo> submitPreOrder(MerchantOrderVo order);

    /**查询订单状态
     * @param orderId
     * @return
     */
    Response<MerchantOrderVo> queryOrderStatus(String orderId);

    /**
     * 查询订单的优惠金额和URL
     * 
     */
    Response<MerchantSubAmountVo>  querySubAmountAndUrl(MerchantSubAmountVo merchantSubAmountVo);

    /**提交订单信息，仅供测试使用
     * @param order
     * @return
     */
    Response<MerchantOrderVo> submitPreOrderTest(MerchantOrderVo order);

    /**查询当前用户是否已开通帐房
     * @return
     */
    Response<Boolean> queryMerchantStatus();

}
