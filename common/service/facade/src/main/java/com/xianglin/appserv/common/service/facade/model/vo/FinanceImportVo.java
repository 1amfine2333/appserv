package com.xianglin.appserv.common.service.facade.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Describe :
 * Created by xingyali on 2018/4/3 10:21.
 * Update reason :
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinanceImportVo extends BaseVo{
    private Long id;
    private String batchNo;
    private String fileNo;
    private String bankTypeCode;
    private String nodeCodeInBank;
    private Long nodePartyId;
    private String nodeName;
    private String branchName;
    private String subbranchName;
    private Integer cardCount;
    private BigDecimal balance;
    private BigDecimal demandBalance;
    private BigDecimal regularBalance;
    private BigDecimal monthAverage;
    private BigDecimal lastMonthAverage;
    private BigDecimal yearAverage;
    private BigDecimal lastYearAverage;
    private Date importDate;
    private String isDeleted;
    private String creator;
    private String updater;
    private Date createDate;
    private Date updateDate;
    private String comments;
}
