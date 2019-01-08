package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;


/**
 * @author jiang yong tao
 * @date 2018/10/9  10:09
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRecommendedVo extends BaseVo {

    /********用户收款账户信息*********/
    private String userAccountName;

    private String accountNumber;

    private String bank;

}
