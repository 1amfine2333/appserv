package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Describe :
 * Created by xingyali on 2018/4/19 11:09.
 * Update reason :
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecruitJobV2 implements Serializable {

    private static final long serialVersionUID = -5102293077715242258L;

    private Long id;

    private Long partyId; //发布人

    private Long articleId;//动态id

    private String jobName;//职位名称

    private String payType; //待遇类型：面议或薪资区间

    private Integer payStart;

    private Integer payEnd;

    private String walfare; //福利待遇，多个以逗号分隔

    private String[] walfares; //福利待遇

    private String companyName;//公司名称

    private String province;//工作所在省

    private String city;//市

    private String county;//区

    private Integer officialSign;// 是否官方标识，1，0

    private Integer recommendSign; //是否置顶表示：1，0

    private Date recommendTime; //推荐时间，用于排序

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

  
}
