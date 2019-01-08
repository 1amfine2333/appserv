package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserGenealogy implements Serializable {
    private Long id;

    private Long parentId;

    private String name;

    private String headImg;

    private String gender;

    private String birthday;

    private String descs;

    private String district;

    private String mateName;

    private String mateBirthday;

    private String mateDesc;

    private String status;

    private Long creator;
    
    private String creatorName;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    @Builder.Default
    private List<AppUserGenealogy> subUsers = new ArrayList<>();
    
}
