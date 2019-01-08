package com.xianglin.appserv.common.service.facade.model.vo;/**
 * Created by wanglei on 2017/4/6.
 */

import lombok.*;

/**
 * @author
 * @create 2017-04-06 16:34
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTip extends BaseVo{

    /**
     * 未读点赞数
     */
    private int praiseCount;

    /**
     * 未读回复数
     */
    private int replyCount;

    /**
     * 未读分享数
     * @return
     */
    private int shareCount;
    

}
