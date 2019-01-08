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
public class ArticleTipVo extends BaseVo {

	private Long id;

	private Long partyId;

	private String userNickName;

	private String userHeadImg;

	private String articleAudio;

	private Integer articleAudioLength;

	private String tipType;

	/**
	 * 针对评论提醒，为用户发表的评论的id
	 */
	private Long articleId;

	/**
	 *针对评论提醒，为评论主体id（动态）
	 */
	private Long replyId;

	private String content;

	private Long toPartyId;

	private String toUserNickName;

	private String toUserHeadImg;

	private String tipStatus;

	private String dealStatus;

	private String isDeleted;

	private Date createTime;

	private Date updateTime;

	private String comments;

	private String readStatus;

	private String dateTime;
	
	private String articleImg;

	private String operator;

}
