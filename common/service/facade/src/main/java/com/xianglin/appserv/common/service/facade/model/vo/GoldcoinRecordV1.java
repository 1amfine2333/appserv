package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Describe :
 * Created by xingyali on 2017/12/11 15:11.
 * Update reason :
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoldcoinRecordV1 extends BaseVo {
    private Long id;


    /**
     * 请求号，最长32位，和系统号联合唯一
     */
    private String requestId;

    /**
     * 系统类型
     */
    private String system;

    /**
     * 批次号，执行批量任务时使用
     */
    private String batchId;

    /**
     * 发起方
     */
    private Long fronPartyId;

    /**
     * 接受方
     */
    private Long toPartyId;

    /**
     *金币数，支持使用负数
     */
    private Integer amount;

    /**
     * 交易类型，系统自定义
     */
    private String type;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 状态，S：成功
     */
    private String status;

    private Date createTime;

    private Date updateTime;

    private BigDecimal balance;
    
    private Integer exchange;

    private BigDecimal inComeAmount;


    private Integer gold;

    private String symbol;

    @Builder.Default
    private int startPage = 1;

    @Builder.Default
    private int pageSize = 10;

}
