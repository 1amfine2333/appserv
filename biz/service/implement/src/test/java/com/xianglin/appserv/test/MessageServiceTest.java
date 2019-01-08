package com.xianglin.appserv.test;

import com.xianglin.appserv.common.service.facade.MessageService;
import com.xianglin.appserv.common.service.facade.app.DiscoryService;
import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.fala.session.MapSession;
import com.xianglin.fala.session.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-biz-spring.xml"})
public class MessageServiceTest {

    @Autowired
    private MessageService service;

    @Autowired
    private SessionHelper sessionHelper;

    @Before
    public void initSession() {
        Session session = new MapSession();
        session.setAttribute(SessionConstants.PARTY_ID, 7001025L);
        session.setAttribute(SessionConstants.DEVICE_ID,"598e71e44dfa444c83b9ef5923c59b2a");
        sessionHelper.saveLocalSesson(session);
    }

    @Test
    public void recommendNews() throws Exception {
        Request<MsgVo> req = new Request<>();
        MsgVo msg = MsgVo.builder().msgTitle("您已成功收款0.01元").msgType("MERCHANT_PAY").msgSource("{\"orderNo\":\"QR22018050700022100345\",\"orderAmount\":\"0.01\"}")
                .loginCheck("N").message("您已成功收款0.01元").partyId(1000000000002499L).build();
        req.setReq(msg);
//        req.setPartyId(1000000000002499L);
        List<Long> partyIds = Arrays.asList(1000000000002499L,1000000000002685L);
        service.sendMsg(req,partyIds);
        TimeUnit.SECONDS.sleep(1);
        msg = MsgVo.builder().msgTitle("您已成功收款10.01元").msgType("MERCHANT_PAY").msgSource("{\"orderNo\":\"QR22018050700000000033333\",\"orderAmount\":\"10.01\"}")
                .loginCheck("N").message("您已成功收款10.01元").build();
        req.setReq(msg);
        service.sendMsg(req,partyIds);
        System.out.println(1);
    }

}
