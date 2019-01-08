package com.xianglin.appserv.common.dal.dataobject;



import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("app_client_log")
public class AppClientLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long partyId;

    private String deviceId;

    private String ip;

    private String systemType;

    private String version;

    @TableLogic
    private String isDeleted;

    private Date createTime;

    @TableField(update = "now()")
    private Date updateTime;

    private String comments;

    private String message;
}