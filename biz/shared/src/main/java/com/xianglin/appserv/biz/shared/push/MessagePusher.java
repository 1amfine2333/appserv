/**
 * 
 */
package com.xianglin.appserv.biz.shared.push;

import java.util.Set;

import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;


/**
 * 消息推送服务
 * 
 * @author wanglei 2016年9月26日下午5:43:49
 */
public interface MessagePusher {

	/**
	 * 初始化
	 * 
	 */
	void init();

	/**
	 * 给单个用户推送消息
	 * 
	 * @param message
	 */
	void push(MsgVo msg,Set<String> tockens);
	
	/**
	 * 针对指定版本用户推送
	 * 
	 * @param msg
	 * @param tockens
	 * @param version
	 */
	void push(MsgVo msg,Set<String> tockens,String version);
}
