package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.util.List;

/**
 * Describe :
 * Created by xingyali on 2018/9/21 11:32.
 * Update reason :
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerV2 extends BaseVo  {
    
    private String bannerName;
    
    private List<BannerVo> bannerVoList;
    
}
