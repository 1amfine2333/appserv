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
public class BusinessAllVo extends BaseVo{

    /**
     * 乡邻生活
     * @return
     */
    private List<BusinessVo> xianglinLifeBusiness;

    /**
     * 用户业务
     */
    private List<BusinessVo> userBusiness;

    /**
     *便民服务
     */
    private List<BusinessVo> servicesBusiness;

    /**
     *便民查询
     */
    private List<BusinessVo> queryBusiness;

    /**
     *便民生活
     */
    private List<BusinessVo> lifeBusiness;

}
