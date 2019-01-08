package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.util.List;

/**
 * Describe :
 * Created by xingyali on 2017/12/13 17:49.
 * Update reason :
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoldInviteVo extends BaseVo {

    /**
     * 邀请金币
     */
    private int inviteGold;

    /**
     * 注册金币
     */
    private int registerGold;
    
    /**
     * 已邀请人数
     */
    private int inviteCount;

    /**
     * 已获得的金币
     */
    private int getGold;

    /**
     * 已邀请用户
     */
    private List<UserVo> userVoList;
}
