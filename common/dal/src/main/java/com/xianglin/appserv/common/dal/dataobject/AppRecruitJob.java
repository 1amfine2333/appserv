package com.xianglin.appserv.common.dal.dataobject;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xianglin.appserv.common.util.EmojiEscapeUtil;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("app_recruit_job")
public class AppRecruitJob extends Base{

    private Long partyId;
    
    private Long articleId;

    private String jobName;

    private String payType;

    private Integer payStart;

    private Integer payEnd;
    
    private String gender;
    
    private String education;
    
    private String workingLife;
    
    private String walfare;

    private String companyName;

    private String province;

    private String city;

    private String county;

    private String area;//地址 json格式

    private String contactName;

    private String contactPhone;

    private String images;

    private Integer officialSign;

    private Integer recommendSign;

    private Date recommendTime;
    
    private Integer applyNum;

    private String desc;

    //岗位类别
    private String jobCategory;

    //是否有年龄要求，Y:是，N:否
    private String ageType;

    //年龄要求，最低
    private Integer ageStart;

    //年龄要求，最高
    private Integer ageEnd;

    //佣金
    private Integer commission;

    //佣金说明
    private String commissionDesc;

    @TableField(exist = false)
    private Long lastId;

    @TableField(exist = false)
    private String showName;

    @TableField(exist = false)
    private String status;//已投递:H 未录用:F 已录用:U

    @TableField(exist = false)
    private String refereeName;//被推荐人名称

    @TableField(exist = false)
    private Long resumeJobId;//申请记录的id

    @TableField(exist = false)
    private String key;//模糊查询公司名称和岗位名称

    private Integer stayApplyNum;//待处理应聘数

    @Builder
    public AppRecruitJob(Long id, String isDeleted, Date createTime, Date updateTime, String comments, Long partyId, Long articleId, String jobName, String payType, Integer payStart, Integer payEnd, String gender, String education, String workingLife, String walfare, String companyName, String province, String city, String county, String area, String contactName, String contactPhone, String images, Integer officialSign, Integer recommendSign, Date recommendTime, Integer applyNum, String desc, String jobCategory, String ageType, Integer ageStart, Integer ageEnd, Integer commission, String commissionDesc, Long lastId, String showName, String status, String refereeName, Long resumeJobId,Integer stayApplyNum) {
        super(id, isDeleted, createTime, updateTime, comments);
        this.partyId = partyId;
        this.articleId = articleId;
        this.jobName = jobName;
        this.payType = payType;
        this.payStart = payStart;
        this.payEnd = payEnd;
        this.gender = gender;
        this.education = education;
        this.workingLife = workingLife;
        this.walfare = walfare;
        this.companyName = companyName;
        this.province = province;
        this.city = city;
        this.county = county;
        this.area = area;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.images = images;
        this.officialSign = officialSign;
        this.recommendSign = recommendSign;
        this.recommendTime = recommendTime;
        this.applyNum = applyNum;
        this.desc = desc;
        this.jobCategory = jobCategory;
        this.ageType = ageType;
        this.ageStart = ageStart;
        this.ageEnd = ageEnd;
        this.commission = commission;
        this.commissionDesc = commissionDesc;
        this.lastId = lastId;
        this.showName = showName;
        this.status = status;
        this.refereeName = refereeName;
        this.resumeJobId = resumeJobId;
        this.stayApplyNum =stayApplyNum;
    }
}
