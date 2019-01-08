package com.xianglin.appserv.common.dal.dataobject;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Base {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableLogic
    private String isDeleted;

    private Date createTime;

    @TableField(update = "now()")
    private Date updateTime;

    private String comments;
}
