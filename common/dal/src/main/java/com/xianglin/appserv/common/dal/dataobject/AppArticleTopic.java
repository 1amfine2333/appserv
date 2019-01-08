package com.xianglin.appserv.common.dal.dataobject;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName("app_article_topic")
public class AppArticleTopic extends Base{

    /**
     * 话题内容
     */
    private String content;

    /**
     * 话题热度
     */
    private Integer popularity;

    /**
     * 是否置顶
     */
    private String topSing;

    @TableField(strategy = FieldStrategy.IGNORED)
    private Date topTime;

    private String creator;

    @Builder
    public AppArticleTopic(Long id, String isDeleted, Date createTime, Date updateTime, String comments, String content, Integer popularity, String topSing, Date topTime, String creator) {
        super(id, isDeleted, createTime, updateTime, comments);
        this.content = content;
        this.popularity = popularity;
        this.topSing = topSing;
        this.topTime = topTime;
        this.creator = creator;
    }
}
