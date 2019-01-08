/**
 * 
 */
package com.xianglin.appserv.biz.shared.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.listener.MonthAchieveInitListner;
import com.xianglin.appserv.common.dal.dataobject.AppPush;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.UserStatus;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.UserType;
import com.xianglin.appserv.common.service.facade.model.vo.AppPushVo;

/**
 * 
 * 
 * @author wanglei 2016年8月17日下午2:20:00
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-biz-spring.xml"})
//@Transactional
public class UserManagerTest {

	@Autowired
	private UserManager service;
	
	@Test
	public void testList(){
		User user = new User();
		try { 
			user.setLoginName("1101");
			user.setPartyId(1101L);
			user.setDeviceId("9e788ce266834c2481c08949121de82f");
			user.setUserType(UserType.nodeManager.name());
			user.setStatus(UserStatus.NORMAL.name());
			service.addUpdateUser(user);
			System.out.println(12);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSaveUpdatePush(){
		AppPushVo push = new AppPushVo();
		try { 
			push.setDeviceId("213213214123211111");
			push.setPushToken("12123131322321413213");
			push.setPushType("JPUSH");
			service.saveUpdatePush(push);
			System.out.println(12);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
