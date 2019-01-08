package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

/**
 * Created by wanglei on 2017/7/21.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RealNameVo extends AreaVo{

    /**
     *姓名
     */
    private String userName;

    /**
     * 脱敏身份证号
     */
    private String idNumber;

    /**
     * 完整身份证号
     */
    private String idCard;

    /**
     * 实名认证状态
     */
    private String realNameStatus;

    

}
