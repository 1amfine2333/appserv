package com.xianglin.appserv.test;

import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.biz.shared.listener.ActivityInviteListner;
import com.xianglin.appserv.common.service.facade.AppLoginService;
import com.xianglin.appserv.common.service.facade.app.ArticleService;
import com.xianglin.appserv.common.service.facade.app.SystemParaService;
import com.xianglin.appserv.common.service.facade.model.DeviceInfo;
import com.xianglin.appserv.common.service.facade.model.LoginDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.ArticleVo;
import com.xianglin.appserv.common.service.facade.model.vo.LoginVo;
import com.xianglin.appserv.common.service.facade.model.vo.MapVo;
import com.xianglin.appserv.common.service.facade.req.ArticleReq;
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

import java.util.List;

/**
 * Created by wanglei on 2017/9/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-biz-spring.xml"})
//@Transactional
public class AppLoginServiceTest {

    @Autowired
    private AppLoginService service;

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ActivityInviteListner activityInviteListner;

    @Autowired
    private SystemParaService systemParaService;

    @Before
    public void initSession() {
        Session session = new MapSession();
        session.setAttribute(SessionConstants.PARTY_ID, 1000000000002121L);
        sessionHelper.saveLocalSesson(session);
    }

    @Test
    public void queryArticleListV2() {
        String deviceId = "537b12da909e42bcb93595a0406b1c42";
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setDeviceId(deviceId);
        loginDTO.setMobilePhone("18621898982");
        loginDTO.setSmsCode("123456");
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceId(deviceId);
        deviceInfo.setCpu("2");
        deviceInfo.setAndroidId("2321321321412");
        deviceInfo.setImei("2321321321");
        Response<String> resp = service.loginMobileV2(loginDTO, deviceInfo);
        System.out.println(resp);
    }

    @Test
    public void rewardV321() {
        try {
            activityInviteListner.rewardV321(userManager.queryUser(1000000000002188L));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cancellation() {
        service.cancellation("15901815152");
    }

    @Test
    public void resetMobile() {
        service.resetMobile("18344292314", "18344292315");
    }

    @Test
    public void queryAppParas() {
        Response<List<MapVo>> resp = systemParaService.queryAppParas();
        System.out.println(resp);
    }

    @Test
    public void loginV4(){
        LoginDTO loginDTO = LoginDTO.builder().mobilePhone("13122359790").smsCode("349044").deviceId("1231314213213131231").build();
        DeviceInfo deviceInfo = DeviceInfo.builder().systemType("IOS").build();
        Response<LoginVo> resp = service.loginV4(loginDTO,deviceInfo);
        System.out.println(resp);
    }
}
