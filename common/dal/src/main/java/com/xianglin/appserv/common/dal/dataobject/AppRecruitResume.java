package com.xianglin.appserv.common.dal.dataobject;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("app_recruit_resume")
public class AppRecruitResume extends Base{

    private Long partyId; //发布人

    private String type; //类型，个人简历，推荐简历，求职意向

    private Integer age; //年龄

    private String name;//姓名

    private String gender;//性别

    private String ethnicity; //名族

    private String education;   //最高学历

    private String criminalRecord;  //是否有前科，Y,N

    private String contactPhone;  //联系号码

    private String certificatesNumber; //身份证号

    private String district; //户籍地址

    private String leaderSign; //是否领队

    private String recommendPhone;  //推荐人手机号

    private String expectJob; //期望工作

    private String payType; //期望薪资方式，面议或最高最低工资

    private Integer payStart; //期望最低工资

    private Integer payEnd; //期望最高工资

    private String marriageSign;//婚姻状态

    private String location; //现居住地

    private String provinceSign;//是否接受外省工作

    private String recentJob;//最近一次投递的工作
    
    private Date recentJobTime;//最近一次投递简历时间
    
    private Integer deliveryCount; //投递次数

    private String manageStatus; //管理状态

    private String status;

    //期望工作地址，多个以逗号分隔
    private String expectCity;

    //备注
    private String remark;

    @Builder
    public AppRecruitResume(Long id, String isDeleted, Date createTime, Date updateTime, String comments, Long partyId, String type, Integer age, String name, String gender, String ethnicity, String education, String criminalRecord, String contactPhone, String certificatesNumber, String district, String leaderSign, String recommendPhone, String expectJob, String payType, Integer payStart, Integer payEnd, String marriageSign, String location, String provinceSign, String recentJob, Date recentJobTime, Integer deliveryCount, String manageStatus, String status, String expectCity, String remark) {
        super(id, isDeleted, createTime, updateTime, comments);
        this.partyId = partyId;
        this.type = type;
        this.age = age;
        this.name = name;
        this.gender = gender;
        this.ethnicity = ethnicity;
        this.education = education;
        this.criminalRecord = criminalRecord;
        this.contactPhone = contactPhone;
        this.certificatesNumber = certificatesNumber;
        this.district = district;
        this.leaderSign = leaderSign;
        this.recommendPhone = recommendPhone;
        this.expectJob = expectJob;
        this.payType = payType;
        this.payStart = payStart;
        this.payEnd = payEnd;
        this.marriageSign = marriageSign;
        this.location = location;
        this.provinceSign = provinceSign;
        this.recentJob = recentJob;
        this.recentJobTime = recentJobTime;
        this.deliveryCount = deliveryCount;
        this.manageStatus = manageStatus;
        this.status = status;
        this.expectCity = expectCity;
        this.remark = remark;
    }

}
