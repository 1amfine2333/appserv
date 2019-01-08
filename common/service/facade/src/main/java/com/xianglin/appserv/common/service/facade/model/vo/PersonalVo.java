package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.math.BigDecimal;

/**
 * Created by wanglei on 2017/5/2.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalVo extends BaseVo{

    private Long partyId;

    private String nickName;
    
    private String trueName;
    
    private String showName;
    
    private String idNumber;

    private String headImg;

    private String introduce;

    private int articleCount;

    private int followCount;

    private int fansCount;

    private int collectionCount;
    
    private int shortVideoCount;

    private String accountBalance;

    private String accountUrl;

    private int couponCount;

    private String couponUrl;

    private Boolean isAuth=false;

    private Integer gold;


}
