package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppPropExtend extends Base{

    private Long relationId;

    private String deviceId;

    private String type;

    private String ekey;

    private String value;

    @Builder
    public AppPropExtend(Long id, String isDeleted, Date createTime, Date updateTime, String comments, Long relationId, String deviceId, String type, String ekey, String value) {
        super(id, isDeleted, createTime, updateTime, comments);
        this.relationId = relationId;
        this.deviceId = deviceId;
        this.type = type;
        this.ekey = ekey;
        this.value = value;
    }
}
