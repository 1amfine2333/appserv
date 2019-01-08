package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppRequestLogDetail {
    private Long id;

    private String type;

    private String remark;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    private String content;

   
}
