package com.xianglin.appserv.common.service.facade.model.vo;

/**
 * Created by wanglei on 2017/3/13.
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 * @create 2017-03-13 17:51
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatShareInfo extends BaseVo {

	private String title;

	private String titieImg;
	/**
	 * 分享到好友
	 */
	private String content;

	/**
	 * url地址
	 */
	private String url;
	/**
	 * 分享到朋友圈
	 */
	private String contentPYQ;
}
