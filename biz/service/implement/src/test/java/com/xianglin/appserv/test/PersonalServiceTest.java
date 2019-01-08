package com.xianglin.appserv.test;

import com.alibaba.fastjson.JSON;
import com.xianglin.appserv.common.service.facade.app.PersonalService;
import com.xianglin.appserv.common.service.facade.app.UserGenealogyService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.AreaVo;
import com.xianglin.appserv.common.service.facade.model.vo.NodeVo;
import com.xianglin.appserv.common.service.facade.model.vo.PersonalVo;
import com.xianglin.appserv.common.service.facade.model.vo.UserVo;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.fala.session.MapSession;
import com.xianglin.fala.session.Session;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanglei on 2017/9/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-biz-spring.xml"})
//@Transactional
public class PersonalServiceTest {

    @Autowired
    private PersonalService service;

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private UserGenealogyService userGenealogyService;

    public static void main(String[] args) {

        try {
            Map<String, Object> resp = new HashMap();
            resp.put("JOIN_DATE", "20161230");
            Interval interval = new Interval(DateUtils.parseDate((String) resp.get("JOIN_DATE"), "yyyyMMdd").getTime(), DateUtils.parseDate("20171231", "yyyyMMdd").getTime());
            System.out.println(interval.toPeriod().getYears());
            System.out.println(interval.toPeriod().getMonths());
            System.out.println(interval.toPeriod().getDays());

            //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            //跨年的情况会出现问题哦
            //如果时间为：2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 1

            Date oDate = DateUtils.parseDate((String) resp.get("JOIN_DATE"), "yyyyMMdd");
            Date fDate = DateUtils.parseDate("20171231", "yyyyMMdd");
            /*Date fDate=sdf.parse("2015-12-31");
            Date oDate=sdf.parse("2016-01-01");*/
            /*Calendar aCalendar = Calendar.getInstance();
            aCalendar.setTime(fDate);
            int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
            aCalendar.setTime(oDate);
            int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
            int days=day2-day1;*/
            long intervalMilli = fDate.getTime() - oDate.getTime();
            //int days=(int) (intervalMilli / (24 * 60 * 60 * 1000));
            //System.out.print(days);
            System.out.print(TimeUnit.MILLISECONDS.toDays(intervalMilli));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void initSession() {

        Session session = new MapSession();
        session.setAttribute(SessionConstants.PARTY_ID, 1000000000002458L);
        sessionHelper.saveLocalSesson(session);
    }

    @Test
    public void createInvite() {

        Response<NodeVo> resp = service.queryNodeInfo();
        System.out.println(resp);
    }

    @Test
    public void queryUser() {

        Response<UserVo> resp = service.queryUser(7000013L);
        System.out.println(resp);
    }

    @Test
    public void queryQrCode() {

        Response<String> resp = service.queryQrCode();
        System.out.println(resp);
    }

    @Test
    public void unReadMsgCount() {

        Response<Integer> resp = service.unReadMsgCount();
        System.out.println(resp);
    }

    @Test
    public void queryDistrictByIdCard() {

        Response<AreaVo> response = service.queryDistrictByIdNumber("420821099207221025");
        System.out.println("身份证：" + response);
    }

    @Test
    public void createUserQRCode() {

        Response<String> resp = service.createUserQRCode("activity_cycle_code", "activity351qrcode", "gold_qr_code_img");
        System.out.println("二维码：" + resp);
    }

    @Test
    public void queryUserByPhone() {

        Response<UserVo> resp = service.queryUserByPhone("15121105561");
        System.out.println("二维码：" + resp);
    }

    @Test
    public void testQueryPersonal() throws Exception {

        Response<UserVo> userVoResponse = service.queryPersonal();
        System.out.println(JSON.toJSONString(userVoResponse, true));

    }

    @Test
    public void genTopartyId() throws Exception {

        userGenealogyService.privateCopyGenealogysId(234536464234L);

    }
    @Test
    public void copyGen() throws Exception {

        userGenealogyService.privateCopyGenealogys(3L);

    }
    
    @Test
    public void queryUserSignAndSubjectAndFollow(){
        Response<Map<String, Object>> mapResponse = service.queryUserSignAndSubjectAndFollow(1000000000002324L);
        System.out.println(mapResponse);
    }
    
    @Test
    public void queryPersonal(){
        Response<PersonalVo> response = service.personInfo();
        System.out.println("是否邀请成功："+response);
        
    }
    
    @Test
    public void queryNewArticleUser(){
        Response<List<UserVo>> listResponse = service.queryNewArticleUser();
        System.out.println("queryNewArticleUser："+listResponse);
    }
    
    @Test
    public void queryUserQRCode(){
        Response<String> stringResponse = service.queryUserQRCode();
        System.out.println("queryUserQRCode："+stringResponse);
    }

    @Test
    public void queryPersonalConfig(){
        Response<String> stringResponse = service.queryPersonalConfig("1111");
        System.out.println("queryUserQRCode："+stringResponse);
    }

    @Test
    public void updatePersonalConfig(){
        Response<String> stringResponse = service.updatePersonalConfig("1111","CLOSE");
        System.out.println("queryUserQRCode："+stringResponse);
    }
}
