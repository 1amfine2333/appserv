package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

/**
 * Describe :
 * Created by xingyali on 2018/2/28 16:07.
 * Update reason :
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopMsgVo extends BaseVo {
    
    private Long id;
    /**
     * 发布人
     */
    private String publisher;
    /**
     * 提交时间
     */
    private String subTime;
    /**
     * 群发对象
     */
    private String groupObject;
    /**
     * 业务类型
     */
    private String type;
    /**
     * 行政位置
     */
    private String location;
    /**
     * 最后一次登录时间
     */
    private String lastLoinTime;
    /**
     * 短信内容
     */
    private String msgContent;
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
