/**
 * 
 */
package com.xianglin.appserv.biz.shared.push;

import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;


/**
 * 消息推送服务
 * 
 * @author wanglei 2016年9月26日下午3:28:08
 */
public interface MessagePushService {

	/**
	 * 推送离线消息
	 * 
	 * @param message
	 */
	void push(MsgVo message);
}
