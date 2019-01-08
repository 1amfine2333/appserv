package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wanglei on 2017/11/9.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGenealogyVo extends BaseVo  {

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

    private String isDeleted;

    private String status;

    private Long creator;

    private String creatorName;

    private Date createTime;

    private Date updateTime;

    private String comments;

    @Builder.Default
    private List<UserGenealogyVo> subUsers = new ArrayList<>();
}
