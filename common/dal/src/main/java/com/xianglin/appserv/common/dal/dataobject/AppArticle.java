package com.xianglin.appserv.common.dal.dataobject;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.xianglin.appserv.common.util.EmojiEscapeUtil;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName("app_article")
public class AppArticle extends Base{

    private Long partyId;

    private Long groupId;

    private String articleType;

    private String title;

    private String image;

    private String article;

    /**
     * 过滤前内容
     */
    private String articleOriginal;

    private String articleImgs;

    private String articleAudio;

    private Integer articleAudioLength;

    private String videoUrl;

    private Integer videoLength;

    private String shareImg;

    private String shareTitle;

    private String shareUrl;

    @TableField(exist = false)
    private String contacts;

    @TableField(exist = false)
    private String contactsPhone;

    private String articleStatus;

    private Integer replyCount;

    private Integer praiseCount;

    private Integer collectCount;

    private Integer shareCount;

    private Long replyId;

    private Long commentId;

    private Long articleId;

    private Long replyPartyId;

    private Integer topLevel;

    private Integer visibleLevel;

    private String province;

    private String city;

    private String county;

    private String town;

    private String village;
    
    private BigDecimal weight;
    
    private BigDecimal weightAdjust;

    private Integer readCount;

    private String supportUsers;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private User replyUser;

    @TableField(strategy = FieldStrategy.IGNORED)
    private Date topTime;//置顶时间

    private String recSign;//是否推荐

    @TableField(exist = false)
    private Long lastId;

    //主题关键字
    @TableField(exist = false)
    private String topic;

    @Builder
    public AppArticle(Long id, String isDeleted, Date createTime, Date updateTime, String comments, Long partyId, Long groupId, String articleType, String title, String image, String article, String articleOriginal, String articleImgs, String articleAudio, Integer articleAudioLength, String videoUrl, Integer videoLength, String shareImg, String shareTitle, String shareUrl, String contacts, String contactsPhone, String articleStatus, Integer replyCount, Integer praiseCount, Integer collectCount, Integer shareCount, Long replyId, Long commentId, Long articleId, Long replyPartyId, Integer topLevel, Integer visibleLevel, String province, String city, String county, String town, String village, BigDecimal weight, BigDecimal weightAdjust, Integer readCount, String supportUsers, User user, User replyUser, Date topTime, String recSign, String topic,Long lastId) {
        super(id, isDeleted, createTime, updateTime, comments);
        this.partyId = partyId;
        this.groupId = groupId;
        this.articleType = articleType;
        this.title = title;
        this.image = image;
        this.article = article;
        this.articleOriginal = articleOriginal;
        this.articleImgs = articleImgs;
        this.articleAudio = articleAudio;
        this.articleAudioLength = articleAudioLength;
        this.videoUrl = videoUrl;
        this.videoLength = videoLength;
        this.shareImg = shareImg;
        this.shareTitle = shareTitle;
        this.shareUrl = shareUrl;
        this.contacts = contacts;
        this.contactsPhone = contactsPhone;
        this.articleStatus = articleStatus;
        this.replyCount = replyCount;
        this.praiseCount = praiseCount;
        this.collectCount = collectCount;
        this.shareCount = shareCount;
        this.replyId = replyId;
        this.commentId = commentId;
        this.articleId = articleId;
        this.replyPartyId = replyPartyId;
        this.topLevel = topLevel;
        this.visibleLevel = visibleLevel;
        this.province = province;
        this.city = city;
        this.county = county;
        this.town = town;
        this.village = village;
        this.weight = weight;
        this.weightAdjust = weightAdjust;
        this.readCount = readCount;
        this.supportUsers = supportUsers;
        this.user = user;
        this.replyUser = replyUser;
        this.topTime = topTime;
        this.recSign = recSign;
        this.topic = topic;
        this.lastId = lastId;
    }

    /**
     * 生成动态拼接内容
     *
     * @return
     */
    public String convert2ReprotDetail() {

        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(getArticle())) {
            sb.append(getArticle());
        }
        if (StringUtils.isNotEmpty(getArticleAudio())) {
            sb.append("[语音]");
        }
        if (StringUtils.isNotEmpty(getArticleImgs())) {
            int countMatches = StringUtils.countMatches(getArticleImgs(), "http");
            for (int i = 0; i < countMatches; i++) {
                sb.append("[图片]");
            }
        }
        return sb.toString();
    }
}
