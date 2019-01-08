/**
 * 
 */
package com.xianglin.appserv.biz.shared.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xianglin.appserv.biz.shared.MonthAchieveManager;
import com.xianglin.appserv.common.service.facade.model.vo.MonthAchieveVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.MonthAchieveQuery;
import com.xianglin.appserv.common.util.DTOUtils;

/**
 * 
 * 
 * @author wanglei 2016年8月17日下午2:20:00
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-biz-spring.xml"})
//@Transactional
public class MonthAchieveManagerTest {

	@Autowired
	private MonthAchieveManager service;
	
	@Test
	public void testList(){
		MonthAchieveQuery req = new MonthAchieveQuery();
		req.setPageSize(10);
		req.setStartPage(1);
		try {
			List<MonthAchieveVo> list = service.query(req);
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void detailTest(){
		try {
			MonthAchieveVo list = service.detail(1L);
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println(1);
	}
}
