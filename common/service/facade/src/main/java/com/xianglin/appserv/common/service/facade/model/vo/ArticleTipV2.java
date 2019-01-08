package com.xianglin.appserv.common.service.facade.model.vo;

/**
 * Created by wanglei on 2017/2/21.
 */

import lombok.*;

import java.util.Date;

/**
 * @author
 * @create 2017-02-21 17:42
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleTipV2 extends BaseVo {

    private Long id;

    private Long partyId;

    private String userNickName;
    
    private String showName;

    private Boolean isAuth=false;

    private String userHeadImg;

    private String tipType;
    /**
     * 微博的ID
     */
    private Long articleId;

    /**
     * 类型，视频还是微博
     */
    private String articleType;
    
    /**
     * 提醒内容
     */
    private String content;
    
    private String articleImg;

    private String articleAudio;

    private Integer articleAudioLength;

    private String readStatus;

    private String dateTime;

    private String tipStatus;

    private String dealStatus;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;
    
    private ArticleVo articleVo;
    
}
