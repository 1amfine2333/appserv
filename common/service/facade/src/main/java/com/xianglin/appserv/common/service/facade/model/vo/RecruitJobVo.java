package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

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
public class RecruitJobVo extends BaseVo {

    private static final long serialVersionUID = -5102293077715242258L;

    private Long id;

    private Long partyId; //发布人

    private Long articleId;//动态id

    private String jobName;//职位名称
    
    private String jobCategory;//岗位类别

    private String payType; //待遇类型：面议或薪资区间

    private Integer payStart;

    private Integer payEnd;

    private String gender;//性别要求

    private String education;//学历要求

    private String workingLife;//工作年限要求

    private String requirement;//要求

    private String walfare; //福利待遇，多个以逗号分隔

    private String[] walfares; //福利待遇

    private String companyName;//公司名称

    private String province;//工作所在省

    private String city;//市

    private String county;//区

    private AreaVo area;//地址

    private String location;//拼接地址

    private String contactName; //联系人姓名

    private String contactPhone; //联系人手机号

    private String images; //图片列表

    private String[] image; //图片列表
    
    private String[] smallImage;//缩略图片

    private Integer officialSign;// 是否官方标识，1，0

    private Integer recommendSign; //是否置顶表示：1，0

    private Date recommendTime; //推荐时间，用于排序

    private Integer applyNum;//申请数
    
    private Integer stayApplyNum;//待处理应聘数

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

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    private String desc;// 描述

    private Boolean isPublish = false; //是否是当前用户发布的招聘

    private Boolean isApply = false;//当前登录用户是否投递过简历

    private Boolean isResume = false;//当前登录用户是否有简历

    private Boolean isRecommendResume = false;//当前登录用户是否有推荐简历

    private String status;//录用状态   已投递:H   未录用:F    已录用:U

    private String refereeName;//被推荐人名称

    private Long resumeJobId;//申请记录的id

    private String showName;//发布人名称

    private String headImg;//当前发布人的头像
    
    private Integer count;//总记录数
    
    private String publishPhone;//发布人手机号

    /**
     * 是否发布到微博
     */
    private String isSubject = "N";

    private String title;

    private String titieImg;
    /**
     * 分享到好友
     */
    private String content;

    /**
     * url地址
     */
    private String url;
    /**
     * 分享到朋友圈
     */
    private String contentPYQ;
    
}
