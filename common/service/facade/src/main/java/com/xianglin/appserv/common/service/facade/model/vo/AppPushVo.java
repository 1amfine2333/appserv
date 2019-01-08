/**
 *
 */
package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

/**
 * 推送消息
 *
 * @author wanglei 2016年10月12日上午10:40:51
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppPushVo extends BaseVo {

	/**  */
	private static final long serialVersionUID = 6056128438244524469L;

	/** 设备号 */
	private String deviceId;

	/** 登陆用户partyId，没有可以不填 */
	private Long partyId;

	/** 推送类型 来源MsgPushType */
	private String pushType;

	/** 推送token值 */
	private String pushToken;

	/** 客户端版本号 */
	private String version;

	/** 状态 */
	private String status;

	/**
	 * 系统类型
	 */
	private String systemType;
}
