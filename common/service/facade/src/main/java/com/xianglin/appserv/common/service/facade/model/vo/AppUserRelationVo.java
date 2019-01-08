package com.xianglin.appserv.common.service.facade.model.vo;

import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRelationVo extends PageReq {
    private static final long serialVersionUID = -3457163818705167746L;
    private Long id;

    private Long fromPartyId;

    private Long toPartyId;

    private String relation;

    private String bothStatus;

    private Date createDate;

    private Date updateDate;

    private String isDeleted;

    private String comments;

    private String loginName;

    private Long partyId;

    private String trueName;

    private String nikerName;

    private String showName;

    private Boolean isAuth=false;

    private String headImg;

    private String introduce;

    private Integer fansNumbers;

    private Integer followNumbers;
    
    private String district;
    /**
     *
     */
    private boolean queryAll = false;
   
}
