package com.xianglin.appserv.common.dal.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppBusiness {
    private Long id;

    private String districtFullName;

    private String busiCode;

    private String busiName;

    private String introduction;

    private String busiImage;
    
    private String busiIcon;

    private String htmlTitle;

    private String hrefUrl;
    
    private String busiActive;

    private String supportVersion;

    private String supportUserType;

    private Integer priorityLevel;

    private String startDate;

    private String endDate;

    private String supportOS;

    private String businessStatus;

    private String busiCategory;

    private String authSign;

    private Date createDate;

    private Date updateDate;

    private String creator;

    private String updater;

    private String isDeleted;

    private String comments;

    private String keyWords;

    private String indexStatus;

    private Long partyId;
    
    private List<Long> ids;
}
