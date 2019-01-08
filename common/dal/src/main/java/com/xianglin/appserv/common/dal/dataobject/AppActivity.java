package com.xianglin.appserv.common.dal.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppActivity {
    private Long id;
    
    private String category;

    private String activeCode;

    private String activityName;

    private String activityDesc;

    private String activityUrl;

    private String activityImgUrl;

    private String activityDetailImgUrl;

    private String isPraise;

    private String dataStatus;
    
    private int clickNum;

    private String startDate;

    private String endDate;
    
    private String supportVersion;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    private String date;//yyyyMMdd 日期查询

}
