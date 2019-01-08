package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleTopicVo extends BaseVo{

    private Long id;

    private String content;

    private Integer popularity;

    private String topSing;

    private Date topTime;

    private String creator;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    private String readStatus;

    private String operator;

}
