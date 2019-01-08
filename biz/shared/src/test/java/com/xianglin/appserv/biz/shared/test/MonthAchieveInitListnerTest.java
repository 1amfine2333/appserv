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
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.UserStatus;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.UserType;

/**
 * 
 * 
 * @author wanglei 2016年8月22日下午1:43:07
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-biz-spring.xml"})
//@Transactional
public class MonthAchieveInitListnerTest {

	@Autowired
	private MonthAchieveInitListner service;
	
	@Test
	public void testList(){
		try {
//			service.initMonthAchieve(700000725L);
			System.out.println(12);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
