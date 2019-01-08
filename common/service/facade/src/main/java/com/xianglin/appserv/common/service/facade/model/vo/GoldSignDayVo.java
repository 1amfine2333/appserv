package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

/**
 * Describe :
 * Created by xingyali on 2017/12/12 14:53.
 * Update reason :
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoldSignDayVo extends BaseVo  {
    private int day;   //连续签到天数
    private Integer amount; //明天签到的金币
    private Boolean isSign;//是否签到
    private UserVo userVo;//用户信息
}
