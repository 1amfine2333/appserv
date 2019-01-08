package com.xianglin.appserv.common.service.facade.model;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;
import lombok.*;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/3/7 13:51.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopSmsDTO extends BaseVo {

    /**
     * id
     */
    private Long id;

    /**
     * 发布人
     */
    private String publisher;

    /**
     * 提交时间
     */
    private String submitTime;

    /**
     * 群发对象
     */
    private String peopleType;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 新增区域编码
     */
    private String districtCode;

    /**
     * 新增区域
     */
    private String district;

    /**
     * 最后一次登录时间
     */
    private String lastLoginTime;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 发送时间
     */
    private String sendTime;

    /**
     * 成功条数
     */
    private int successCount;

    /**
     * 失败条数
     */
    private int failCount;
}
