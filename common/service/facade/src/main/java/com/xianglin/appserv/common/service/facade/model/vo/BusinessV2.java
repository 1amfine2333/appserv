package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Describe :
 * Created by xingyali on 2018/6/27 11:30.
 * Update reason :
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessV2 extends BaseVo  {
    
    private List<BusinessVo>  firstBusinessVos;
    
    private List<BusinessVo>  businessVos;
    
}
