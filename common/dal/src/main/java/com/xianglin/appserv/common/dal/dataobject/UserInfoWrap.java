package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

/**
 * 数据库查询封装
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2017/1/4
 * Time: 15:47
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoWrap implements Cloneable{

    private String loginName;
    private Long partyId;
    private String nikerName;
    private String trueName;
    private String showName;
    private Boolean isAuth=false;
    private String headImg;
    private String introduce;
    private String bothStatus;
    private Integer fansNumbers;
    private Integer followNumbers;
    private String district;


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
