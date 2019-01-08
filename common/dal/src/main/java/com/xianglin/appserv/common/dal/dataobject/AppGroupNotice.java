package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppGroupNotice {
    private Long id;

    private Long groupId;

    private String title;

    private String content;

    private Long partyId;

    private String status;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

}
