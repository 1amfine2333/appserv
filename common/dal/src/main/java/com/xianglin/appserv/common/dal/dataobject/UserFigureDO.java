/**
 * 
 */
package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.util.Date;

/**
 * 
 * 
 * @author pengpeng 2016年2月24日下午6:15:37
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFigureDO extends BaseDO {

	/** 用户唯一标识 */
	private String userId;

	/** 用户身份角色唯一标识 */
	private String figureId;

	/** 昵称或备注名 */
	private String nickName;

	/** 头像图片url */
	private String avatarUrl;

	/** 用户加入群的时间 */
	private Date joinTime;


}
