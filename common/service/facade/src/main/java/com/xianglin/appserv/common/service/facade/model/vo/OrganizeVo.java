package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.util.List;

/**
 * Created by wanglei on 2017/9/18.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizeVo extends BaseVo{

    private Long id;

    /**
     *组织名称
     */
    private String name;

    /**
     *组织地址信息
     */
    private String district;

    /**
     *组织管理员
     */
    private Long managerPartyId;

    /**
     *管理员名字
     */
    private String managerName;

    /**
     *显示时间
     */
    private String dateTime;

    /**
     * 当前用户是否是管理员
     */
    private Boolean isManager = false;

    /**
     * 当前村管理员个数
     */
    private int managerCount;

    /**
     * 当前村人数
     */
    private int memberCount;

    /**
     * 成员
     */
    private List<MemberVo> members;

    /**
     * 公告
     */
    private List<OrganizeNoticeVo> notices;

    /**
     * 用户姓名
     */
    private String userName;

    private Boolean isAuth=false;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 状态
     * Y开通
     * N未开通
     */
    private String status;

}
