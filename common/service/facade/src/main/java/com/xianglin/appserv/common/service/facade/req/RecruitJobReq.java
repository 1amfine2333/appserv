package com.xianglin.appserv.common.service.facade.req;

import com.xianglin.appserv.common.service.facade.model.vo.PageResp;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * Describe :
 * Created by xingyali on 2018/4/19 11:20.
 * Update reason :
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecruitJobReq extends DateSectionReq{
    private Long id;

    private Long partyId;

    private String jobName;
    //岗位类别
    private String jobCategory;

    private String payType;

    private Integer payStart;

    private Integer payEnd;

    private String gender;

    private String education; //学历要求

    private String workingLife;//工作年限要求

    private String walfare;//福利待遇，多个以逗号分隔

    //年龄要求，最低
    private Integer ageStart;

    //年龄要求，最高
    private Integer ageEnd;

    private String companyName;

    private String province;

    private String city;

    private String county;

    private String contactName;

    private String contactPhone;

    private String images;

    private Integer officialSign;

    private Integer recommendSign;

    private Date recommendTime;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    private String desc;

    private Long lastId;
    
    private String showName;
    
    private String key;//模糊查询公司名称和岗位名称
    
    private String isCommission;//有奖推荐 
    
    private String orderBy;//排序
    
    private List<String> jobCategorys;//岗位类型集合 
    
    private List<String> expectCitys;//期望工作地址集合
    
}
