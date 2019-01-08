package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

/**
 * Created by wanglei on 2017/9/18.
 */


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberVo extends BaseVo{

    private Long id;

    /***
     * 群或组织的ID
     * 
     */
    
    private Long groupId;

    /**
     *姓名
     */
    private String name;

    /**
     * 显示名称
     */
    private String showName;
    
    /**
     *手机号
     */
    private String mobilePhone;
                             
    /**
     *姓名
     */
    private String type;

    /**
     *头像地址
     */
    private String imageUrl;

    private Long partyId;

    /**
     *激活状态
     */
    private String activeStatus;

    /**
     *光柱状态
     */
    private String follwoStatus;
    
    /**
     * 是否是管理员
     * */
    private String isManager;


    /**
     * 是否实名认证
     */
    private Boolean isAuth=false;
}
