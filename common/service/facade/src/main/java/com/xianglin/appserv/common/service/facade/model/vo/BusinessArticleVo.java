package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Describe :
 * Created by xingyali on 2018/6/26 18:24.
 * Update reason :
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessArticleVo extends BaseVo {
    
    private List<BusinessVo> businessVo;
    
    private List<ArticleVo> articleVo;
}
