package com.xianglin.appserv.common.service.facade.model.vo;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;
import lombok.*;

/**
 * Created by wanglei on 2017/9/11.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantSubAmountVo extends BaseVo{

    private String shopCode;//店铺编号 

    private String orderAmount; //订单金额
    
    private String subAmount;//优惠金额
    
    private String realAmount;//实收金额
    
    private String url;//二维码跳转链接
}
