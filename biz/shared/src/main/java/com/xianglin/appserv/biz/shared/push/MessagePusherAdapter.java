/**
 * 
 */
package com.xianglin.appserv.biz.shared.push;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xianglin.appserv.common.dal.daointerface.AppPushDAO;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;


/**
 * 消息推送服务
 * 
 * @author wanglei 2016年9月26日下午5:43:49
 */
public abstract class MessagePusherAdapter implements MessagePusher{
	
	private static final Logger logger = LoggerFactory.getLogger(MessagePusherAdapter.class);

	@Autowired
	private AppPushDAO appPushDao;

	/**
	 * 初始化
	 * 
	 */
	public abstract void init();

	/**
	 * 给单个用户推送消息
	 * 
	 * @param message
	 */
	public abstract void push(MsgVo msg,Set<String> tockens);
	
	/**
	 * 针对指定版本用户推送
	 * 
	 * @param msg
	 * @param tockens
	 * @param version
	 */
	public void push(MsgVo msg,Set<String> tockens,String version){
		logger.debug("push version:{}",version);
	};
	
	protected void deleteInvalidToken(Set<String> tokens){
		appPushDao.deleteBatch(tokens);
	}
}
