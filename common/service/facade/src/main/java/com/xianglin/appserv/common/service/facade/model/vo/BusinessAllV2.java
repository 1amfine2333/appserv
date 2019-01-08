package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.util.List;

/**
 * Created by wanglei on 2017/5/2.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessAllV2 extends BaseVo{
    
    private String businessName;

    private List<BusinessVo> business;


}
