package com.xianglin.appserv.common.service.facade.model.vo;

import javafx.beans.property.adapter.ReadOnlyJavaBeanBooleanProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Describe :
 * Created by xingyali on 2018/4/19 11:09.
 * Update reason :
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecruitResumeVo extends BaseVo {

    private static final long serialVersionUID = -5102293077715242258L;

    private Long id;

    private Long partyId;

    private String type;
    
    private String name;

    private String gender;

    private Integer age;

    private String ethnicity;

    private String education;

    private String criminalRecord;

    private String contactPhone;

    private String certificatesNumber;

    private String district;//户籍地址
    
    private String districtStitch;//拼接后的格式；

    private String leaderSign;

    private String recommendPhone;

    private String expectJob;

    private String payType;

    private Integer payStart;

    private Integer payEnd;

    private String marriageSign;

    private String location;  // 现居住地json格式
    
    private String locationStitch; //现居住地拼接后的格式
    
    private String provinceSign;

    private String recentJob;//最近一次投递的工作

    private Date recentJobTime;//最近一次投递简历时间

    private int deliveryCount; //投递次数

    private String manageStatus;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    private String status;//录用状态

    private String showName;//发布人的用户昵称

    private Boolean isApply;//是否投递过简历

    private Boolean isIntention;//是否填写求职意向

    private String expectCity; //期望工作地址，多个以逗号分隔

    private String remark;  //备注

    private List<Map<String,Object>> jobList;

    private List<Map<String,Object>> cityList;
}
