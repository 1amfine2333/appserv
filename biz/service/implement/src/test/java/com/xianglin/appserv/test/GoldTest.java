package com.xianglin.appserv.test;

import com.xianglin.appserv.biz.shared.UserGenealogyManager;
import com.xianglin.appserv.common.service.facade.app.*;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.GoldcoinService;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.fala.session.MapSession;
import com.xianglin.fala.session.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Describe :
 * Created by xingyali on 2017/12/12 15:24.
 * Update reason :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-biz-spring.xml"})
public class GoldTest {
    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private InterestService interestService;

    @Autowired
    private GoldService goldService;

    @Autowired
    private PersonalService personalService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private DiscoryService discoryService;

    @Autowired
    private UserGenealogyService userGenealogyService;

    @Before
    public void initSession() {
        Session session = new MapSession();
        session.setAttribute(SessionConstants.PARTY_ID, 1000000000002330L);
        sessionHelper.saveLocalSesson(session);
    }

    @Test
    public void queryContinuitySign() {
        Response<GoldSignDayVo> resp = goldService.queryContinuitySign();
        System.out.println("查询连续签到天数" + resp);
    }

    @Test
    public void queryRecord() {
        Response<List<GoldcoinRecordV1>> resp = goldService.queryRecord(1,10);
        System.out.println("金币交易记录查询" + resp);
    }

    @Test
    public void queryRecordByLastTime() {
        Response<GoldcoinRecordV1> resp = goldService.queryRecordByLastTime();
        System.out.println("查询上一次的金币兑换记录" + resp);
    }

    @Test
    public void querySignDay() {
        Response<List<GoldSignDateVo>> resp = goldService.querySignDay();
        System.out.println("查询签到天数" + resp);
    }

    @Test
    public void sign() {
        Response<Boolean> resp = goldService.sign();
        System.out.println("签到" + resp);
    }

    @Test
    public void getGoldcoin() {
        Response<Boolean> resp = goldService.getGoldcoin("20171212");
        System.out.println("领取金币" + resp);
    }

    @Test
    public void querySignAndPunish() {
        Response<List<ActivityRewardVo>> resp = goldService.querySignAndPunish();
        System.out.println("查询日常奖励与惩罚" + resp);
    }

    @Test
    public void personInfo() {
        Response<PersonalVo> response = personalService.personInfo();
        System.out.println("我的页面金币" + response);
    }

    @Test
    public void querySignGold() {
        Response<Integer> response = goldService.querySignGold();
        System.out.println("查询签到金币奖励" + response);
    }

    @Test
    public void queryRegisterGold() {
        Response<Integer> response = goldService.queryRegisterGold();
        System.out.println("查询注册金币奖励" + response);
    }

    @Test
    public void queryGame() {
        Response<List<ActivityVo>> resp = indexService.queryGame();
        System.out.println("查询游戏列表" + resp);
    }

    @Test
    public void updateClickGame() {
        Response<Boolean> resp = indexService.updateClickGame(42L);
        System.out.println("修改游戏点击量" + resp);
    }

    @Test
    public void updateChannel() {
        List<MapVo> list = new ArrayList<>();
        MapVo mapVo14 = new MapVo();
        mapVo14.setKey("TJ");
        mapVo14.setValue("推荐");
        MapVo mapVo = new MapVo();
        mapVo.setKey("VIDEO");
        mapVo.setValue("视频");
        MapVo mapVo1 = new MapVo();
        mapVo1.setKey("JKZX");
        mapVo1.setValue("健康");
        MapVo mapVo2 = new MapVo();
        mapVo2.setKey("YLXW");
        mapVo2.setValue("热点");
        MapVo mapVo3 = new MapVo();
        mapVo3.setKey("XLXW");
        mapVo3.setValue("乡邻新闻");


        MapVo mapVo4 = new MapVo();
        mapVo4.setKey("QW");
        mapVo4.setValue("奇闻");
        MapVo mapVo5 = new MapVo();
        mapVo5.setKey("LZ");
        mapVo5.setValue("励志");
        MapVo mapVo6 = new MapVo();
        mapVo6.setKey("YE");
        mapVo6.setValue("育儿");
        MapVo mapVo7 = new MapVo();
        mapVo7.setKey("YL");
        mapVo7.setValue("娱乐");
        MapVo mapVo8 = new MapVo();
        mapVo8.setKey("JS");
        mapVo8.setValue("军事");
        MapVo mapVo9 = new MapVo();
        mapVo9.setKey("LS");
        mapVo9.setValue("历史");
        MapVo mapVo10 = new MapVo();
        mapVo10.setKey("TY");
        mapVo10.setValue("体育");
        MapVo mapVo11 = new MapVo();
        mapVo11.setKey("SN");
        mapVo11.setValue("三农");
        MapVo mapVo12 = new MapVo();
        mapVo12.setKey("XH");
        mapVo12.setValue("笑话");
        MapVo mapVo13 = new MapVo();
        mapVo13.setKey("SS");
        mapVo13.setValue("时尚");
        list.add(mapVo14);
        list.add(mapVo);
        list.add(mapVo1);
        list.add(mapVo2);
        list.add(mapVo3);
        list.add(mapVo4);
        list.add(mapVo5);
        list.add(mapVo6);
        list.add(mapVo7);
        list.add(mapVo8);
        list.add(mapVo9);
        list.add(mapVo10);
        list.add(mapVo11);
        list.add(mapVo12);
        list.add(mapVo13);
        Response<Boolean> response = discoryService.updateChannel(list);
        System.out.println("修改频道列表" + response);
    }

    @Test
    public void award() {
        Response<Integer> resp = goldService.award("READ_NEWS");
        System.out.println("发奖励" + resp);
    }

    @Test
    public void doExchange() {
        Response<Boolean> resp = goldService.doExchange("ef68a6290c1a4caaa223e1b942b01c9b");
        System.out.println("发奖励" + resp);
    }

    @Test
    public void queryGoldQRCode() {
        Response<String> resp = personalService.queryGoldQRCode();
        System.out.println("发奖励" + resp);
    }

    @Test
    public void deleteUserGenealogyVo() {
        Response<Boolean> resp = userGenealogyService.deleteUserGenealogyVo(1L);
        System.out.println("删除家谱成员" + resp);
    }

    public static void main(String[] args) {
        int i =6;
        i =Math.abs(i);
        System.out.println(i);
        BigDecimal a= new BigDecimal(2415);
        a=a.abs();
        System.out.println(a);

        String mobile = "13681467423";
        mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        System.out.println(mobile);
    }

}
