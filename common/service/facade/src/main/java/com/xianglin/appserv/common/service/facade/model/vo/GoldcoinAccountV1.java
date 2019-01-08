package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.util.Date;

/**
 * Describe :
 * Created by xingyali on 2017/12/11 16:41.
 * Update reason :
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoldcoinAccountV1 extends BaseVo {
    private Long id;

    private Long partyId;

    private String type;

    private Integer amount;

    private String status;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;
}
