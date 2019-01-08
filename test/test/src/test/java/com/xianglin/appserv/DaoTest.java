/**
 * 
 */
package com.xianglin.appserv;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xianglin.appserv.common.dal.daointerface.AppBannerDAO;
import com.xianglin.appserv.common.dal.daointerface.AppInstallDAO;
import com.xianglin.appserv.common.dal.daointerface.MonthAchieveDAO;
import com.xianglin.appserv.common.dal.daointerface.MsgDAO;
import com.xianglin.appserv.common.dal.dataobject.AppInstall;
import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.enums.MonthAchieveType;
import com.xianglin.appserv.common.service.facade.model.vo.req.MonthAchieveQuery;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;
import com.xianglin.appserv.common.util.DTOUtils;


/**
 * 
 * 
 * @author zhangyong 2016年8月18日下午1:45:09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/spring/common-dal.xml")
public class DaoTest {

	@Resource
	private MsgDAO msgDao;
	@Resource
	private MonthAchieveDAO monthDao;
//	@Autowired
	@Autowired
	private AppInstallDAO appInstallDao;
	
	@Autowired
	AppBannerDAO appBanerDao;
	
	@Test
	public void testBaner(){
		Map<String,Object>params = new HashMap<>();
		params.put("districtCode", "0091");
//		List<AppBanner> list =appBanerDao.queryAppBanerList(params);
//		System.out.println(list.size());
	}
	@Test
	public void testAppInstall(){
//		appInstallDao.deleteAppInstall("1", "test");
//		appInstallDao.queryAppInstallByDeviceId("wsf");
		
		
		List<AppInstall> list = new ArrayList<>();
		
		for (int i = 0; i <5; i++) {
			AppInstall in = new AppInstall();
			in.setAppDev("dev"+i);
			in.setAppInstallDate(new Date());
			in.setAppName("appname"+i);
			in.setAppPackage("com.package"+i);
			in.setAppVersion("1.0."+i);
			in.setDeviceId("877123ksjdfjassljldf");
			in.setIsDeleted("0");
			in.setPartyId(Long.valueOf(i));
			list.add(in);
		}
		
		appInstallDao.batchInsertAppInstall(list);
	}
	
/*	@Test
	public void testBaner() throws Exception{
	
//		bannerDao.selectByPrimaryKey(1L);
		AppBanner appbaner = new AppBanner();
		appbaner.setId(1L);
		appbaner.setIsDeleted("1");
		System.out.println(bannerDao.queryAppBanerList(null));
	}*/
	
	@org.junit.Test
	public void testMsgList() throws Exception{
		Request<MsgQuery> messageReq = new Request<>();
		MsgQuery msgQ = new MsgQuery();
		msgQ.setPageSize(2);
		msgQ.setStartPage(1);
		messageReq.setReq(msgQ);
		msgDao.queryMap(DTOUtils.beanToMap(msgQ));
	}
	@org.junit.Test
	public void testSelect() throws Exception{
		MonthAchieveQuery query = new MonthAchieveQuery();
//		query.setPageSize(1);
//		query.setStartPage(1);
		query.setMonth("06");
		query.setYear("2016");
		query.setStaticType(MonthAchieveType.INCOME.name());
		System.out.println(DTOUtils.beanToMap(query));
		monthDao.select(DTOUtils.beanToMap(query));
	}
	@org.junit.Test
	public void testSum(){
		Map<String,Object>map= new HashMap<>();
			map.put("partyId", 5500268);
//			map.put("year", 2016);
//			map.put("month", 6);
//			map.put("busiType", MonthAchieveType.ESHOP.name());
			map.put("staticType", MonthAchieveType.INCOME.name());
			System.out.println(monthDao.selectSumAmountGBYearAndMonth(map));
	}
}
