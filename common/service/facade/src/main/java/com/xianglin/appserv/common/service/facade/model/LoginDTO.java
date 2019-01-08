/**
 * 
 */
package com.xianglin.appserv.common.service.facade.model;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录信息
 * 
 * @author hebbo 2016年8月15日上午9:22:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO extends BaseVo {

	/** serialVersionUID */
	private static final long serialVersionUID = -468122203532121903L;

	/** 站点编号 */
	private String nodeCode;

	/** 用户唯一标识 */
	private Long partyId;

	/** 用户密码 */
	private String password;

	/** app客户端唯一标识 */
	private String clientId;

	/** app客户端版本号 */
	private String clientVersion;
	
	private String smsCode;
	
	private String mobilePhone;

	/**
	 * 客户端设备deviceId
	 */
	private String deviceId;

	private String recCode;//推荐码

	/**
	 * 登录类型，区分使用验证码，密码，图案密码登录
	 */
	private String type;
}
