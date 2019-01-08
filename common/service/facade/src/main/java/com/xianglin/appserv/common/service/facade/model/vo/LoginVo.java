package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo extends BaseVo{

    private String loginName;

    private Long partyId;

    private String userType;

    private String ryToken;

    private String showName;

    private String headImg;

    /**
     * 是否弹出图案密码提示
     */
    private Boolean alertPasswordPattern;

    /**
     * 是否新用户注册
     */
    private Boolean isNewUser;
}
