package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

/**
 * Created by wanglei on 2017/9/19.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizeNoticeVo extends BaseVo{

    private Long id;

    private Long organzeId;

    private String title;

    private String content;

    /**
     * 消息发布人
     */
    private String userName;

    private Boolean isAuth=false; 

    /**
     * 时间
     */
    private String dateTime;

}
