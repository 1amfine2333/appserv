package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Msg {

    private Long id;

    private String msgTitle;

    private String msgType;
    
    private String msgTag;

    private String titleImg;

    private String url;
    
    private String msgSource;
    
    private String msgSourceUrl;

    private String msgSourceToken;
    
    private String loginCheck;
    
    private Integer expiryTime;
    
    private String passCheck;
    
    private String status;

    private String remark;//备注 20171115

    private String titleImgList;

    private Integer shareNum;//分享数 20171115

    private Integer treadNum;//点踩数

    private Integer readNum;
    
    private Integer praises;

    private String isDeleted;

    private String creator;

    private String updater;
    
    private String dateTime;

    private Date createTime;

    private Date updateTime;

    private String message;
    
    private String comments;
}
