package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppBanner {
    private Long id;

    private String type;

    private String bannerCode;

    private String bannerName;

    private String introduction;
    
    private String imageName;

    private String bannerImage;

    private String hrefUrl;

    private String activeType; //跳转类型，区分原生：ACTIVE，h5：HTML，rn：RN

    private int duration;//展示时间，以秒为单位

    private String needLogin;//点击是否需要登陆 Y,N

    private String title;

    private String supportVersion;

    private String supportUserType;

    private Integer priorityLevel;

    private Date startDate;

    private Date endDate;

    private String supportOs;

    private String supportArea;

    private String supportUser;

    private String bannerStatus;

    private Date createDate;

    private Date updateDate;

    private String creator;

    private String updater;

    private String isDeleted;

    private String comments;

    private String content;
    
}
