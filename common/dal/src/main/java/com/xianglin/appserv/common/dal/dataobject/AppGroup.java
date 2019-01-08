package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppGroup {
    private Long id;

    private String name;
    
    private String synName;

    private String imageUrl;
    
    private String qrCode;

    private Long managePartyId;

    private String ryGroupId;

    private String district;

    private String districtCode;

    private String groupType;

    private String createType;
    
    private Long operator;

    private String status;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    /**
     * 当前村管理员个数
     */
    private int managerCount;

    /**
     * 当前村人数
     */
    private int memberCount; 

}
