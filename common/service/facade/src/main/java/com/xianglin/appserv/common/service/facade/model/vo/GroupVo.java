package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.util.List;

/**
 * Created by wanglei on 2017/9/19.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupVo extends BaseVo{

    private Long id;

    /**
     *群名
     */
    private String name;

    /**
     *头像
     */
    private String imageUrl;

    /**
     * 群二维码
     */
    private String qrCode;

    /**
     * 融云群id
     */
    private String ryGroupId;

    /**
     *群类型 
     */
    private String groupType;

    /**
     * 创建类型
     */
    private String createType;

    /***
     * 群成员数
     */
    private int count;

    private List<MemberVo> members;

    /**
     *管理员
     */
    private Long managerPartyId;

    /**
     * 当前用户是否是管理员
     */
    private Boolean isManager;
    
    private String status;

    /**
     * 当前用户是否存在
     */
    private Boolean isExist;

 
}
