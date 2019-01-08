package com.xianglin.appserv.common.service.facade.model;

import com.xianglin.appserv.common.service.facade.model.enums.LinkType;
import lombok.*;

import java.io.Serializable;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/3/5 20:42.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenealogyLinkDTO implements Serializable {

    private static final long serialVersionUID = 2875614345705756787L;

    /**
     * 把谁（被添加到家谱的人）
     */
    private Long partyId;

    /**
     * 添加到谁（家谱所有人）
     */
    private Long genealogyUser;

    /**
     * 父节点id
     */
    private Long parentId;

    /**
     * 手机号码
     */
    private String mobilePhone;

    /**
     * 验证码
     */
    private String captcha;

    /**
     * @see LinkType
     */
    private String type;
}
