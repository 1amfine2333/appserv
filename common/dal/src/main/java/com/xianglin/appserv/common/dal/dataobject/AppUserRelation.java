package com.xianglin.appserv.common.dal.dataobject;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.YesNo;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("app_user_relation")
public class AppUserRelation {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "FROM_PARTY_ID")
    private Long fromPartyId;

    private Long toPartyId;

    private String relation;

    private String bothStatus;

    /**
     * 备注名
     */
    private String remarkName;

    /**
     * 关注状态
     */
    private String followStatus;

    /**
     * 被关注状态
     */
    private String followedStatus;

    private Date createDate;

    @TableField(update = "now()")
    private Date updateDate;

    @TableLogic
    private String isDeleted;

    private String comments;

    /**
     * 关注状态转换
     *
     * @return
     */
    public String getUserRelation() {
        if (StringUtils.equals(followedStatus, YesNo.Y.name()) && StringUtils.equals(this.followStatus, YesNo.Y.name())) {
            return Constant.RelationStatus.BOTH.name();
        } else if (StringUtils.equals(followedStatus, YesNo.Y.name())) {
            return Constant.RelationStatus.FANS.name();
        } else if (StringUtils.equals(followStatus, YesNo.Y.name())) {
            return Constant.RelationStatus.FOLLOW.name();
        } else {
            return Constant.RelationStatus.UNFOLLOW.name();
        }
    }
}
