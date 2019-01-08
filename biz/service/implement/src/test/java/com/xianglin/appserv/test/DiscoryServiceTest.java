package com.xianglin.appserv.test;

import com.xianglin.appserv.common.service.facade.LoanService;
import com.xianglin.appserv.common.service.facade.app.DiscoryService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.vo.MapVo;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.fala.session.MapSession;
import com.xianglin.fala.session.Session;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by wanglei on 2017/9/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-biz-spring.xml"})
//@Transactional
public class DiscoryServiceTest {

    @Autowired
    private DiscoryService service;

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
    public void recommendNews() throws IOException {
        MsgQuery req = MsgQuery.builder().msgType("").build();
        Response<List<MsgVo>> resp = service.recommendNews(req);
        System.out.println(resp);
    }

    @Test
    public void listUserNews() throws IOException {
        MsgQuery req = MsgQuery.builder().msgType("VIDEO").startPage(1).pageSize(20).build();
        Response<List<MsgVo>> resp = service.listUserNews(req);
        System.out.println(resp.getResult().size());
    }

    @Test
    public void operateNews() throws IOException {
        MsgQuery req = MsgQuery.builder().msgId(94130L).operateType(Constant.MsgOperateType.PRAISE.name()).build();
        int i = 0;
        while (++i < 4){
            Response<Boolean> resp = service.operateNews(req);
        }
//        System.out.println(resp.getResult());
    }
    
    @Test
    public void queryChannel(){
        Response<List<MapVo>> list = service.queryChannel();
        System.out.println("频道"+list);
    }

    @Test
    public void newsDetail() throws IOException {
        Response<MsgVo> resp = service.newsDetail(94130L);
        System.out.println(resp.getResult());
    }
}
