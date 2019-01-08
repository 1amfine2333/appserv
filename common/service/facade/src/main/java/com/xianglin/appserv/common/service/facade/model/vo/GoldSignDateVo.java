package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.util.Date;

/**
 * Describe :
 * Created by xingyali on 2017/12/12 14:27.
 * Update reason :
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoldSignDateVo  extends BaseVo {
    private String day; //天数
    
    private Integer amount;//金币
    
    private String time; //时间
    
    private String signType; //签到状态
    
    private String getGole;//是否领取金币
    
}
