package com.xianglin.appserv;
/**
 * 
 */


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xianglin.appserv.common.service.facade.MessageService;
import com.xianglin.appserv.common.service.facade.app.XLAppIndexPageService;

/**
 * 
 * 
 * @author zhangyong 2016年8月15日上午11:44:27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:consumer.xml"})
public class ServiceTest {

	private Logger logger = LoggerFactory.getLogger(ServiceTest.class);
	@Autowired
	private XLAppIndexPageService appIndexService;
	
	@Autowired
	private MessageService messageService;
	
	@Test
	public void appIndexServiceTest(){
		logger.info("===============");
		logger.info("{}",appIndexService.getXLAppHomeData(5190133l,1l));
		logger.info("===============");
	}
}
