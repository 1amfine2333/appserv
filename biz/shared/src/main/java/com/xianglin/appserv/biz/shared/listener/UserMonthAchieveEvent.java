/**
 * 
 */
package com.xianglin.appserv.biz.shared.listener;

import com.xianglin.appserv.common.dal.dataobject.User;
import org.springframework.context.ApplicationEvent;

/**
 * 
 * 
 * @author wanglei 2016年8月16日上午11:01:58
 */
public class UserMonthAchieveEvent extends ApplicationEvent {

	/**  */
	private static final long serialVersionUID = -7976419333921064611L;
	
	private User user;
	
	/**
	 * @param partyId
	 */
	public UserMonthAchieveEvent (User user) {
		super(user);
		this.user = user;
	}

	public User getUser() {
		return user;
	}
	
}
