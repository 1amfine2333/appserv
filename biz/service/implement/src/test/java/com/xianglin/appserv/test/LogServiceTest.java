package com.xianglin.appserv.test;

import com.alibaba.fastjson.JSON;
import com.xianglin.appserv.common.service.facade.app.*;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.ArticleTipReq;
import com.xianglin.appserv.common.service.facade.req.OranginzeReq;
import com.xianglin.appserv.common.util.RyUtil;
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

import java.util.*;

/**
 * Created by wanglei on 2017/9/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-biz-spring.xml"})
//@Transactional
public class LogServiceTest {

    @Autowired
    private LogService logService;

    @Autowired
    private SessionHelper sessionHelper;


    @Before
    public void initSession() {

        Session session = new MapSession();
        session.setAttribute(SessionConstants.PARTY_ID, 1000000000002324L);
        session.setAttribute(SessionConstants.CLIENT_VERSION, "3.5.3");
        session.setAttribute(SessionConstants.DEVICE_ID, "dfdafdagdasfdasdfsdafdfsddfdsdfdsdfd");
        sessionHelper.saveLocalSesson(session);
    }

    @Test
    public void putClientLog(){
        logService.putClientLog(ClientLogVo.builder().message("test").build());
    }

    @Test
    public void queryUserLoginList(){
        System.out.println(logService.queryUserLoginList(1000000000002987L,new Date(),null));
    }

}
