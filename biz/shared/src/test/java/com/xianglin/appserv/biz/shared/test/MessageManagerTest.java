/**
 * 
 */
package com.xianglin.appserv.biz.shared.test;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.xianglin.appserv.biz.shared.MessageManager;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgType;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;
import com.xianglin.appserv.common.util.DTOUtils;

/**
 * 
 * 
 * @author wanglei 2016年8月17日下午2:20:00
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-biz-spring.xml"})
//@Transactional
public class MessageManagerTest {

	@Autowired
	private MessageManager service;
	
	@Test
	public void testList(){
		MsgQuery req = new MsgQuery();
		req.setPageSize(10);
		req.setStartPage(1);
		try {
			Map<String, Object> paras = DTOUtils.beanToMap(req);
			service.list(req,1000221L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
	@Test
	public void testSaveUpdate(){
		MsgVo req = new MsgVo();
		try {
			req.setMsgTitle("test"); 
			req.setMessage("test test test");
			req.setMsgType(MsgType.ALARM.name());
//			req.setPartyId(1990213L);
			service.saveUpdate(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSend(){
		MsgVo req = new MsgVo();
		try {
			req.setMsgTitle("test"); 
			req.setMessage("test test test");
			req.setPartyId(7000137L);
			req.setMsgType(MsgType.ALARM.name());
//			req.setPartyId(1990213L);
			service.sendMsg(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void detailTest(){
		try {
			MsgVo vo = service.detail(20L,1000221L);
			System.out.println(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void readTest(){
		try {
			MsgVo vo = service.read(20L,1000221L);
			System.out.println(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void praiseTest(){
		try {
			String A = "212";
			Boolean vo = service.praise(20L,1000221L);
			System.out.println(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
