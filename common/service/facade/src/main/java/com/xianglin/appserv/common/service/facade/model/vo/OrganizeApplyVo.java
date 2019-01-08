package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

/** 组织申请
 * Created by wanglei on 2017/9/18.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizeApplyVo extends BaseVo{

    private Long id; //申请ID

    private Long organzeId; //村ID

    private String userName; //姓名
    
    private String showName;//显示姓名

    private Boolean isAuth=false;

    private String userImg;//头像

    private String mobilePhone;//手机号
    
    private String district;//家乡地址
    
    private String type;//类型

    private String status; //申请状态
    
    private String auditor; //审核人

    private String remark; //备注

    private String opinion;//申请意见
    
    private String dateTime; //创建时间

}
