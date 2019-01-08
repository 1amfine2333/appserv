package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wanglei on 2017/2/14.
 */

/**
 * @author
 * @create 2017-02-14 9:49
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleVo extends BaseVo {

    /**  */
    private static final long serialVersionUID = -4908442531642053929L;

    private Long id;

    private Long partyId;

    private Long groupId;

    private Long[] groupIds;

    private String title;

    private String nikeName;
    
    private String trueName;
    
    private String showName;

    private Boolean isAuth=false;

    private String headImg;

    private String gender;

    private String articleType;

    private String article;

    private String image;

    private String articleImgs;
    
    private String smallImage;
    
    private String smallArticleImgs;
    
    private String[] articleImageArray;
    
    private String[] smallArticleImgsArray;

    private String articleAudio;

    private Integer articleAudioLength;

    private String videoUrl;

    private Integer videoLength;

    private String shareImg;

    private String shareTitle;

    private String shareUrl;

    private String contacts;

    private String contactsPhone;

    private String articleStatus;

    private Integer replyCount;

    private Integer praiseCount;

    private Integer readCount;

    private String supportUsers;

    /**
     * 是否点赞 ，Y：点赞，N：未点赞
     *
     * @since v3.0.3
     */
    private String isPraise = "N";

    private Integer collectCount;

    // 动态是否被收藏
    private String isCollect = "N";

    private Integer shareCount;

    /**
     * 是否分享过，Y：分享，N：未分享
     */
    private String isShare = "N";

    /**
     * 是否发布到微博
     */
    private String isSubject = "N";

    private Long replyId;

    private Long articleId;

    private Long commentId;

    private Integer topLevel;

    private Integer visibleLevel;

    private String isDeleted;

    private Date createTime;

    private String dateTime;

    private Date updateTime;

    private String comments;

    private String province;

    private String city;

    private String county;

    private String town;

    private String village;

    private BigDecimal weight;

    private BigDecimal weightAdjust;

    private String district;

    private Long replyPartyId;

    private String replyNickName;
    
    private String replyShowName;

    private String replyHeadImg;

    /**
     * 用户类型 村长/村户
     */
    private String userType;

    /**
     * 关注状态
     */
    private String bothStatus;
    
    private Date topTime;//置顶时间
    
    private String recSign;//是否推荐
    
    private String orderBy;//排序

    private String topic;
    
    private String phone;//发布者的手机号

}
