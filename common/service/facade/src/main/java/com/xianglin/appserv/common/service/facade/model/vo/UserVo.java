package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo extends BaseVo {

    private Long id;

    private String loginName;

    private String newLoginName;

    private Long partyId;

    private String userType;

    private String deviceId;

    private String sessionId;

    private String nikerName;

    private String headImg;

    private String introduce;

    /**
     * 推荐人姓名
     */
    private String introducerName;

    private String trueName;

    private String showName;

    private Boolean isAuth=false;

    private String password;

    private String passwordSalt;

    private String passwordPattern;

    private String descs;

    private String status;

    private String ryToken;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    private String province;//省

    private String city;//市

    private String county;//县、区

    private String town;//镇

    private String village;//村名

    private AreaVo district;//家乡对象格式

    private String location; //所在地 json格式

    private AreaVo locationArea;//所在地区域明细

    private String birthday;//生日

    private String gender;//性别

    /**
     * 粉丝数
     */
    private Integer fansNumber;

    /**
     * 关注数
     */
    private Integer followsNumber;

    /**
     * 是否关注
     */
    private String bothStatus;


    /**
     *是否设置图案密码
     */
    private Boolean hasPasswordPattern;

    /**
     * 是否设置密码
     */
    private Boolean hasPassword;

    /**
     * 身份证号
     */
    private String certificatesNumber;
    

}
