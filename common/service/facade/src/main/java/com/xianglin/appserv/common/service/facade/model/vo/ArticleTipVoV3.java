package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.util.Date;

/**
 * @author jiang yong tao
 * @date 2018/9/5  10:26
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleTipVoV3 extends BaseVo {

    private Long id;

    private Long partyId;

    private String tipType;

    private Long articleId;

    private String content;

    private Long toPartyId;

    private String tipStatus;

    private String dealStatus;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    private String readStatus;

    private String userMobile;

    private String articleType;

    private String reportUserName;

    private String toUserName;

    private String operator;

    private String punishment;
}
