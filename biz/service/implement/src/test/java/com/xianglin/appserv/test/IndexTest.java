package com.xianglin.appserv.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.biz.shared.SysParaManager;
import com.xianglin.appserv.biz.shared.UserManager;
import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.service.facade.AppLoginService;
import com.xianglin.appserv.common.service.facade.MessageService;
import com.xianglin.appserv.common.service.facade.UserRelationService;
import com.xianglin.appserv.common.service.facade.app.*;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.req.*;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.cif.common.service.facade.AuthService;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.model.CustomRealnameauthDTO;
import com.xianglin.cif.common.service.facade.model.CustomersDTO;
import com.xianglin.fala.session.MapSession;
import com.xianglin.fala.session.Session;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Describe :
 * Created by xingyali on 2017/11/8 17:43.
 * Update reason :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-biz-spring.xml"})
public class IndexTest {

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private IndexService indexService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserGenealogyService userGenealogyService;

    @Autowired
    private OrganizeService organizeService;

    @Autowired
    private PersonalService personalService;

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomersInfoService customersInfoService;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ActivityInviteService activityInviteService;

    @Autowired
    private EarnPageService earnPageService;

    @Autowired
    private AppLoginService appLoginService;
    
    @Autowired
    private LogService logService;

    @Autowired
    private SysParaManager sysParaManager;

    public static void main(String[] args) {
        List<MapVo> list = new ArrayList<>();
        MapVo mapVo = new MapVo();
        mapVo.setKey("1.问：首页和发现页分别计数，到达banner位上限继续添加点击");
        mapVo.setValue("答：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        MapVo mapVo1 = new MapVo();
        mapVo1.setKey("2.问：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        mapVo1.setValue("答：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        MapVo mapVo2 = new MapVo();
        mapVo2.setKey("3.问：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        mapVo2.setValue("答：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        MapVo mapVo3 = new MapVo();
        mapVo3.setKey("4.问：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        mapVo3.setValue("答：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        MapVo mapVo4 = new MapVo();
        mapVo4.setKey("5.问：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        mapVo4.setValue("答：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        MapVo mapVo6 = new MapVo();
        mapVo6.setKey("6.问：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        mapVo6.setValue("答：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        MapVo mapVo7 = new MapVo();
        mapVo7.setKey("7.问：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        mapVo7.setValue("答：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        MapVo mapVo8 = new MapVo();
        mapVo8.setKey("8.问：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        mapVo8.setValue("答：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        MapVo mapVo9 = new MapVo();
        mapVo9.setKey("9.问：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        mapVo9.setValue("答：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        MapVo mapVo10 = new MapVo();
        mapVo10.setKey("10.问：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        mapVo10.setValue("答：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        MapVo mapVo11 = new MapVo();
        mapVo11.setKey("11.问：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        mapVo11.setValue("答：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        MapVo mapVo12 = new MapVo();
        mapVo12.setKey("12.问：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        mapVo12.setValue("答：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        MapVo mapVo13 = new MapVo();
        mapVo13.setKey("13.问：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        mapVo13.setValue("答：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        MapVo mapVo14 = new MapVo();
        mapVo14.setKey("14.问：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        mapVo14.setValue("答：每个编号仅可使用一次，编辑后点击确定提示修改成功，");
        list.add(mapVo);
        list.add(mapVo1);
        list.add(mapVo2);
        list.add(mapVo3);
        list.add(mapVo4);
        list.add(mapVo6);
        list.add(mapVo7);
        list.add(mapVo8);
        list.add(mapVo9);
        list.add(mapVo10);
        list.add(mapVo11);
        list.add(mapVo12);
        list.add(mapVo13);
        list.add(mapVo14);
        list.toString();
        String json = JSONArray.fromObject(list).toString();
        System.out.print(list.toString());


        //json转成list
       /* JSONArray jsonArray = JSONArray.fromObject(json);
        List<MapVo> list2 = (List) JSONArray.toCollection(jsonArray);
        for (int i = 0; i < list2.size(); i++) {
            MapVo map= new MapVo();
            System.out.println(list2.get(i).getKey());
            System.out.println(list2.get(i).getValue());
            //运行：abc
            //　　  123
        }
        */

        MsgVo vo = new MsgVo();
        vo.setMessage("212321321");
//        MsgVo  vo = MsgVo.builder().titleImg("123212321321").build();
        System.out.println(vo.getMessage());
    }

    public static void main1(String[] args) {
        List<String> list = new ArrayList<>();
        String a1 = "https://cdn02.xianglin.cn/0b33d989-4d80-46ff-b415-09f4b942e69e.png,张宝强,2分钟前,领取30元";
        String a2 = "https://cdn02.xianglin.cn/4744d716-d41f-464a-86ba-bfb2735b0bd0.png,马冬梅,3分钟前,领取18元";
        String a3 = "https://cdn02.xianglin.cn/2fda3d4a-ebdd-4522-862a-745ca87225e8.png,张雪莲,2分钟前,领取43元";
        String a4 = "https://cdn02.xianglin.cn/afbd31a5-c8df-44df-b82e-6a65ac149a14.png,张艳艳,3分钟前,领取12元";
        String a5 = "https://cdn02.xianglin.cn/3112485f-e5e9-4025-9edc-9ba9bdaba5fa.png,王根田,3分钟前,领取9元";
        String a6 = "https://cdn02.xianglin.cn/c3fca6f5-f200-4fe1-bdad-99582f81b11a.png,赵四虎,7分钟前,领取2元";
        String a7 = "https://cdn02.xianglin.cn/9ca0c506-78aa-448a-bc15-5f15adb8c53a.png,李志强,3分钟前,领取10元";
        String a8 = "https://cdn02.xianglin.cn/de3b1a96-3725-41d6-ba21-a86b1fd71864.png,田根源,3分钟前,领取19元";
        String a9 = "https://cdn02.xianglin.cn/9ed02019-8380-416a-a473-34b04dd61bab.png,孔凌平,3分钟前,领取7元";
        String a10 = "https://cdn02.xianglin.cn/70b98c42-c349-49fe-856e-ae47a87566e1.png,谢广平,5分钟前,领取16元";
        String a11 = "https://cdn02.xianglin.cn/2b470c1c-cf57-4acf-8f1c-e7af5c64d7ee.png,刘录凤,3分钟前,领取2元";
        String a12 = "https://cdn02.xianglin.cn/1a100cd4-1253-4ecb-bb20-86edaba69989.png,杨富贵,2分钟前,领取29元";
        String a13 = "https://cdn02.xianglin.cn/bafcbaca-98a7-48cc-9f88-caeb1ebc32b5.png,陈艳红,3分钟前,领取31元";
        String a14 = "https://cdn02.xianglin.cn/ae9e716c-1a9d-4cd1-b873-7ae36f18bbf4.png,袁世豪,3分钟前,领取4元";
        String a15 = "https://cdn02.xianglin.cn/47a7d001-aa45-433b-87d1-a59a283c5405.png,罗康,6分钟前,领取88元";
        String a16 = "https://cdn02.xianglin.cn/06ccc2c3-e69c-4367-a8e7-c3e6f25f4287.png,白山泉,3分钟前,领取15元";
        String a17 = "https://cdn02.xianglin.cn/e1fd79bc-3469-4636-9406-b761e4f99d5d.png,陈军,3分钟前,领取20元";
        String a18 = "https://cdn02.xianglin.cn/128cb7b2-1709-4798-82a4-5d52df913f04.png,邓先平,3分钟前,领取2元";
        String a19 = "https://cdn02.xianglin.cn/ffb7559f-90f6-4bd8-83f2-a88190290e4a.png,方美红,2分钟前,领取17元";
        String a20 = "https://cdn02.xianglin.cn/fb12d168-f3a7-45e6-8650-96c57bccc307.png,刘秀芬,3分钟前,领取11元";
        String a21 = "https://cdn02.xianglin.cn/0621e0bf-6282-40ab-87e2-4c5234a478e7.png,马兰兰,3分钟前,领取30元";
        String a22 = "https://cdn02.xianglin.cn/075bded8-cc72-48ae-90d9-a0da433643c0.png,赵峰,3分钟前,领取29元";
        String a23 = "https://cdn02.xianglin.cn/914952bb-5846-4b97-918f-4426a17b484b.png,吴磊,3分钟前,领取14元";
        String a24 = "https://cdn02.xianglin.cn/c5ded495-0709-4c5b-9a21-92d65ab8b131.png,贾宝泉,3分钟前,领取6元";
        String a25 = "https://cdn02.xianglin.cn/fbe0fd22-0736-4985-89ef-29c77bae585f.png,王宏强,4分钟前,领取19元";
        String a26 = "https://cdn02.xianglin.cn/97ce7e7c-7c5c-48d1-9edf-d5d1e7948697.png,曹超,3分钟前,领取26元";
        String a27 = "https://cdn02.xianglin.cn/501d7823-00d3-464b-baf0-8d48b480d472.png,宋保国,3分钟前,领取2元";
        String a28 = "https://cdn02.xianglin.cn/cdcc6d95-0d3d-4731-a248-4c60b6018d12.png,朱爱红,3分钟前,领取46元";
        String a29 = "https://cdn02.xianglin.cn/56eabf72-a200-4186-843a-55345f459f77.png,柳晓飞,3分钟前,领取33元";
        String a30 = "https://cdn02.xianglin.cn/1806891a-7f62-4e93-84d6-96edcc72aedb.png,严涛,4分钟前,领取5元";
        String a31 = "https://cdn02.xianglin.cn/344f710b-61a5-4b67-9f71-04eb2cd62d71.png,欧海青,3分钟前,领取17元";
        String a32 = "https://cdn02.xianglin.cn/64dfd8a1-573b-4afa-bad7-773987286818.png,常敏刚,3分钟前,领取9元";
        String a33 = "https://cdn02.xianglin.cn/1621e073-96a7-45ae-8726-beb9a1e1252f.png,薛康,3分钟前,领取21元";
        String a34 = "https://cdn02.xianglin.cn/9b02de1b-73c2-4025-8943-d59113082a8e.png,王会强,3分钟前,领取13元";
        String a35 = "https://cdn02.xianglin.cn/04696d5a-888c-46cf-b763-5183ce4e274e.png,张健康,2分钟前,领取2元";
        String a36 = "https://cdn02.xianglin.cn/eb200d10-41ac-4148-9cc0-88af2de55717.png,郭明明,3分钟前,领取12元";
        String a37 = "https://cdn02.xianglin.cn/6aea64a5-2b49-40bc-a641-ef2116beae00.png,胡景香,4分钟前,领取10元";
        String a38 = "https://cdn02.xianglin.cn/d1cfe5fa-bd7e-4338-83f8-6fa32236f7a4.png,李岩,3分钟前,领取23元";
        String a39 = "https://cdn02.xianglin.cn/ef45197d-f314-437d-9b98-dab7220801da.png,闫兴,3分钟前,领取4元";
        String a40 = "https://cdn02.xianglin.cn/c8c8a62c-8f16-4136-891a-80a0976240a5.png,宋慧昌,3分钟前,领取12元";
        String a41 = "https://cdn02.xianglin.cn/1309f2cd-71ae-4fb9-9fbe-dcb5f3e27d6b.png,赵勇,1分钟前,领取23元";
        String a42 = "https://cdn02.xianglin.cn/4cabe84d-5d68-4810-866e-e52aaabd7bb6.png,张博文,3分钟前,领取36元";
        String a43 = "https://cdn02.xianglin.cn/3b20f56a-84de-4e1d-8237-d84c4a706ad7.png,王录雨,1分钟前,领取10元";
        String a44 = "https://cdn02.xianglin.cn/e7c5ac77-8253-409a-9bcd-36dd18223067.png,毛泽平,3分钟前,领取5元";
        String a45 = "https://cdn02.xianglin.cn/8a7e13cf-e7ef-4535-b402-2bfa15a9a827.png,周海涛,1分钟前,领取18元";
        String a46 = "https://cdn02.xianglin.cn/754d10a0-b487-461c-9636-c93151bb102f.png,杨欢,45分钟前,领取10元";
        String a47 = "https://cdn02.xianglin.cn/aace93cf-7be2-4362-a65f-2bf6dc241bed.png,李玉龙,12分钟前,领取31元";
        String a48 = "https://cdn02.xianglin.cn/53d1267f-c60e-4897-9c57-4d1019e9f24c.png,杨山会,20分钟前,领取8元";
        String a49 = "https://cdn02.xianglin.cn/8fc965dc-98d8-4602-a35b-0398ff00e901.png,马超,45分钟前,领取22元";
        String a50 = "https://cdn02.xianglin.cn/8ae1b183-8378-4025-8257-8c8f685031d2.png,黄博强,3分钟前,领取6元";
        list.add(a1);
        list.add(a11);
        list.add(a21);
        list.add(a31);
        list.add(a41);
        list.add(a2);
        list.add(a12);
        list.add(a22);
        list.add(a32);
        list.add(a42);
        list.add(a3);
        list.add(a13);
        list.add(a23);
        list.add(a33);
        list.add(a43);
        list.add(a4);
        list.add(a14);
        list.add(a24);
        list.add(a34);
        list.add(a44);
        list.add(a5);
        list.add(a15);
        list.add(a25);
        list.add(a35);
        list.add(a45);
        list.add(a6);
        list.add(a16);
        list.add(a26);
        list.add(a36);
        list.add(a46);
        list.add(a7);
        list.add(a17);
        list.add(a27);
        list.add(a37);
        list.add(a47);
        list.add(a8);
        list.add(a18);
        list.add(a28);
        list.add(a38);
        list.add(a48);
        list.add(a9);
        list.add(a19);
        list.add(a29);
        list.add(a39);
        list.add(a49);
        list.add(a10);
        list.add(a20);
        list.add(a30);
        list.add(a40);
        list.add(a50);


        String json = JSONArray.fromObject(list).toString();
        System.out.println("list===" + json);


        System.out.println(list.get(RandomUtils.nextInt(0, list.size())));
        List<String> list1 = new ArrayList<>();


        JSONArray jsonArray = JSONArray.fromObject(json);
        List<String> list2 = (List<String>) JSONArray.toCollection(jsonArray, Map.class);
        for (int i = 0; i < list2.size(); i++) {
            list1.add(list2.get(i));
        }
        System.out.println("list===" + list1);

        int days = (int) TimeUnit.MILLISECONDS.toDays(DateTime.parse("20180208", DateTimeFormat.forPattern("yyyyMMdd")).getMillis() - DateTime.now().withHourOfDay(1).getMillis());


    }

    public static void main4(String[] args) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        String today = formatter.format(new Date());
        System.out.println(today);
        try {
            String firstDay = "20180201";
            Date a = DateUtils.parseDate(firstDay, "yyyyMMdd");
            Date b = null;
//            long days = TimeUnit.MILLISECONDS.toDays(DateUtils.parseDate(firstDay, "yyyyMMdd").getTime() - DateUtils.parseDate(DateUtil.convertDate(new Date(), "yyyyMMdd"), "yyyyMMdd").getTime());

//            System.out.println(days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void initSession() {
        Session session = new MapSession();
        session.setAttribute(SessionConstants.PARTY_ID, 1000000000002458L);
        session.setAttribute(SessionConstants.CLIENT_VERSION,"4.0.2");
        sessionHelper.saveLocalSesson(session);
        sysParaManager.queryPara().forEach(config -> {
            SysConfigUtil.put(config.getCode(),config.getValue());
        });
    }

    @Test
    public void queryDefaultVillage() {
        Response<VillageVo> response = indexService.queryDefaultVillage();
        System.out.print("默认村：" + response);
    }

    @Test
    public void publishArticleV1() {
        ArticleVo articleVo = new ArticleVo();
        Long[] groupIds = new Long[]{307L, 300L};
        articleVo.setGroupIds(groupIds);
        articleVo.setArticleType("BROADCAST");
        articleVo.setArticle("广播广播12345");
        Response<Boolean> response = articleService.publishArticleV1(articleVo);
        System.out.print("广播广播" + response);
    }

    @Test
    public void queryArticleListV3() {
        ArticleReq articleReq = new ArticleReq();
        articleReq.setGroupId(0L);
        articleReq.setPartyId(7001025L);
        articleReq.setArticleType("SUBJECT");
        Response<List<ArticleVo>> response = articleService.queryArticleListV3(articleReq);
        System.out.print("动态列表查询:" + response);
    }

    @Test
    public void query() {
        Response<UserGenealogyVo> resp = userGenealogyService.query(1L);
        System.out.print("查询家谱个人详情:" + resp);
    }

    @Test
    public void update() {
        UserGenealogyVo userGenealogyVo = UserGenealogyVo.builder().id(1L).name("张三").mateName("李四").build();
        Response<Boolean> resp = userGenealogyService.update(userGenealogyVo);
        System.out.print("修改家谱成员信息:" + resp);
    }

    @Test
    public void add() {
        UserGenealogyVo userGenealogyVo = new UserGenealogyVo();
        userGenealogyVo.setBirthday("1994-07-22");
        userGenealogyVo.setName("dvcsd");
        userGenealogyVo.setParentId(2L);
        userGenealogyVo.setId(40L);
        Response<Boolean> resp = userGenealogyService.add(userGenealogyVo);
        System.out.print("添加家谱成员:" + resp);
    }

    @Test
    public void queryUserGenealogyVo() {
        Response<List<UserGenealogyVo>> resp = userGenealogyService.queryUserGenealogyVo();
        System.out.print("查询我的家谱:" + resp);
    }

    @Test
    public void queryArticleTipListV1() {
        ArticleTipReq articleTipReq = new ArticleTipReq();
        Response<List<ArticleTipV2>> resp = articleService.queryArticleTipListV1(articleTipReq);
        System.out.print("动态提醒V1:" + resp);
    }

    @Test
    public void queryArticleTipsV1() {
        Response<Integer> resp = articleService.queryArticleTipsV1();
        System.out.print("查询点赞、评论、分享、收藏、举报数未读数:" + resp);
    }

    @Test
    public void queryOrganizeParas() {
        OranginzeReq oranginzeReq = new OranginzeReq();
        oranginzeReq.setName("");
        oranginzeReq.setIsManager(true);
        Response<List<OrganizeVo>> resp = organizeService.queryOrganizeParas(oranginzeReq);
        System.out.print("村庄:" + resp);
    }

    @Test
    public void queryMemberByParas() {
        MemberReq memberReq = new MemberReq();
        memberReq.setGroupId(301L);
        memberReq.setName("鸽");
        Response<List<MemberVo>> resp = organizeService.queryMemberByParas(memberReq);
        System.out.print("人详情:" + resp);
    }

    @Test
    public void updateVillageUserType() {
        Response<Boolean> resp = organizeService.updateVillageUserType(MemberVo.builder().id(1170L).type("MEMBER").build());
        System.out.print("修改:" + resp);
    }

    @Test
    public void queryUserFeedBackByParas() {
        UserFeedbackReq req = new UserFeedbackReq();
        //req.setContent("啦咔");
        //req.setStatus("");
        req.setStatus("N");
        Response<List<UserFeedbackVo>> resp = personalService.queryUserFeedBackByParas(req);
        System.out.print("查反馈:" + resp);
    }

    @Test
    public void updateUserFeedback() {
        UserFeedbackVo userFeedbackVo = new UserFeedbackVo();
        userFeedbackVo.setOperator("7001025");
        userFeedbackVo.setRemark("已处理");
        userFeedbackVo.setStatus("Y");
        userFeedbackVo.setId(1L);
        userFeedbackVo.setPartyId(1000000000002926L);
        Response<Boolean> resp = personalService.updateUserFeedback(userFeedbackVo);
        System.out.print("修改反馈:" + resp);
    }

    @Test
    public void queryFollowsOrFans() {
        Response<List<AppUserRelationVo>> response = userRelationService.queryFollowsOrFans(AppUserRelationVo.builder().bothStatus("FOLLOW").build());
        System.out.print("关注列表:" + response);
    }

    @Test
    public void recommendFriend() {
        Response<List<AppUserRelationVo>> response = userRelationService.recommendFriend();
        System.out.print("推荐好友:" + response);
    }

    @Test
    public void queryVillageManage() {
        Response<List<String>> resp = organizeService.queryVillageManage(301L);
        System.out.print("管理员:" + resp);
    }

    @Test
    public void listOrganizeV2() {
        Response<List<OrganizeVo>> resp = organizeService.listOrganizeV2();
        System.out.print("获取当前用户的组织V2:" + resp);
    }

    @Test
    public void recommendVideo() {
        Response<List<MsgVo>> response = messageService.recommendVideo("");
        System.out.print("推荐新闻:" + response);
    }

    @Test
    public void listMembers() {
        Response<List<MemberVo>> resp = groupService.listMembers(300L);
        System.out.print("某个村的人员:" + resp);
    }

    @Test
    public void follow() {
        AppUserRelationVo vo = new AppUserRelationVo();
        vo.setBothStatus("FOLLOW");
        vo.setToPartyId(1000000000001411L);
        Response<String> response = userRelationService.follow(vo);
        System.out.print("关注:" + response);
    }

    @Test
    public void queryVillageCountByParas() {
        OrganizeVo organizeVo = new OrganizeVo();
        organizeVo.setIsManager(false);
        organizeVo.setName("");
        Response<Integer> resp = organizeService.queryVillageCountByParas(organizeVo);
        System.out.print("村个数:" + resp);
    }

    @Test
    public void indexBusinessAll() {
        Response<BusinessAllVo> resp = indexService.indexBusinessAll("3.4.0");
        System.out.print("BusinessAll:" + resp);
    }

    @Test
    public void findFriends() {
        Response<List<AppUserRelationVo>> response = userRelationService.findFriends("134", "1", 1, 10);
        System.out.print("找朋友:" + response);
    }

    @Test
    public void deleteArticle() {
        Response<Boolean> resp = articleService.deleteArticle(4099L);
        System.out.print("删除动态:" + resp);
    }

    @Test
    public void queryArticleListPage() {
        ArticleReq req = new ArticleReq();
        CommonResp<List<ArticleVo>> resp = articleService.queryArticleListPage(req);
        System.out.print("后台动态:" + resp);
    }

    @Test
    public void queryMemberCountByParas() {
        Response<Integer> resp = organizeService.queryMemberCountByParas(MemberVo.builder().groupId(365L).name("弯").build());
        System.out.print("某个村的人数:" + resp);
    }

    @Test
    public void addRealName() {
        RealNameVo vo = new RealNameVo();
        vo.setIdNumber("420821199207221025");
        vo.setUserName("幸雅丽");
        User user = userManager.queryUser(7001025L);
        CustomersDTO customers = new CustomersDTO();
        customers.setPartyId(7001025L);
        customers.setMobilePhone(user.getLoginName());
        customers.setCustomerName(vo.getUserName());
        customers.setCredentialsType("身份证");
        customers.setCredentialsNumber(vo.getIdNumber());
        customers.setCreator(7001025L + "");
        com.xianglin.cif.common.service.facade.model.Response<CustomRealnameauthDTO> resp1 = authService.twoFactorAuth(customers, "app");
        System.out.print("实名认证:" + resp1);
    }

    @Test
    public void queryRealName() {
        com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> resp1 = customersInfoService.selectCustomsAlready2Auth(7001025L);
        System.out.print("查实名认证:" + resp1);
    }

    @Test
    public void queryUserUsedAddress() {
        Response<List<String>> resp = articleService.queryUserUsedAddress();
        System.out.println("查询用户最近发布的3个地址===" + resp);
    }

    @Test
    public void indexActivity() {
        Response<List<ActivityVo>> resp = indexService.indexActivity("3.5.0");
        System.out.println("查询活动===" + resp);
    }

    @Test
    public void queryUserAnnualReportPopup() {
        Response<Boolean> response = indexService.queryUserAnnualReportPopup();
        System.out.println("查询用户的年报账单是否弹窗===" + response);
    }

    @Test
    public void queryCycleDays() {
        int days = (int) TimeUnit.MILLISECONDS.toDays(DateTime.parse("20180131", DateTimeFormat.forPattern("yyyyMMdd")).getMillis() - DateTime.now().withHourOfDay(1).getMillis());
        System.out.println("距离天数：" + days);
        Response<Integer> response = indexService.queryCycleDays();
        System.out.println("查询用户的年报账单是否弹窗===" + response);
    }

    @Test
    public void invite() {
        Response<Boolean> response = activityInviteService.invite(ActivityInviteDetailVo.builder().activityCode("106").loginName("18729084389").recPartyId(1000000000002184L).build());
        System.out.println("是否邀请成功：" + response);
    }

    @Test
    public void clickBusiness() {
        Response<Boolean> response = indexService.clickBusiness(134L);
        System.out.println("是否邀请成功：" + response);
    }

    @Test
    public void indexBusinessV3() {
        Response<List<BusinessVo>> response = indexService.indexBusinessV3("3.5.3");
        System.out.println("是否邀请成功：" + response);
    }

    @Test
    public void queryTopBanner() {
        List<String> types = new ArrayList<>();
        types.add(Constant.BannerType.INDEX.name());
        types.add(Constant.BannerType.DISCOVER.name());
        Response<List<BannerVo>> listResponse = indexService.queryTopBanner(types);
        System.out.println("是否邀请成功：" + listResponse);
    }

    @Test
    public void insertBannerVo() {
        Response<Boolean> response = indexService.insertBannerVo(BannerVo.builder().bannerCode("300").type("INDEX").bannerName("3中秋节活动").title("3中秋节活动").content("3333中秋节活动中秋节活动中秋节活动中秋节活动中秋节活动中秋节活动中秋节活动").bannerImage("https://cdn02.xianglin.cn/3d9bae6733adc04f8a32676c841066f0-84885.jpg").priorityLevel(3).build());
        System.out.println("是否邀请成功：" + response);
    }

    @Test
    public void indexBannersV2() {
        Response<List<BannerVo>> response = indexService.indexBannersV2("3.5.4", "");
        System.out.println("是否邀请成功：" + response);
    }

    @Test
    public void earnPageService() {
        Response<List<FinanceImportVo>> financeDataByNodePartyId = earnPageService.getFinanceDataByNodePartyId();
        System.out.println("是否邀请成功：" + financeDataByNodePartyId);
    }

    @Test
    public void setPatternPassword() {
        //Response<Boolean> response = appLoginService.setPatternPassword("Y","122");
        //System.out.println("是否邀请成功："+response);
        List<MapVo> list = new ArrayList<>();
        MapVo mapVo = new MapVo();
        mapVo.setKey("金币是什么？怎么赚取？");
        mapVo.setValue("金币是乡邻APP为大家提供的小福利，做日常小任务可以获得。可以在“我的—金币任务”查看赚金币的任务列表。");
        MapVo mapVo1 = new MapVo();
        mapVo1.setKey("金币有什么用？");
        mapVo1.setValue("每周五金币会统一兑换成现金，可在“我的--余额”里查看兑换好的现金并提现，遇特殊情况未兑换请看置顶微博。");
        MapVo mapVo2 = new MapVo();
        mapVo2.setKey("为什么邀请了好友，好友有金币，自己没有？");
        mapVo2.setValue("邀请好友时，好友需要在邀请页面填写自己的手机号码，或者好友在注册时填写你的邀请码，注册成功后，自己才有金币。");
        MapVo mapVo3 = new MapVo();
        mapVo3.setKey("账户余额怎么提现？");
        mapVo3.setValue("先在“我的—设置—交易密码”设置交易密码，设置成功后按照步骤提示进行提现。");
        /*MapVo mapVo4 = new MapVo();
        mapVo4.setKey("为什么我在秒息宝里转入了金额但是没有看到收益？");
        mapVo4.setValue("转入金额太少时收益无法计算，建议大家转入100元以上。");
        MapVo mapVo6 = new MapVo();
        mapVo6.setKey("为什么办信用卡应用没有了？");
        mapVo6.setValue("系统对该应用进行维护时会暂时不可见，稍晚一天再来查看 就会有啦。");*/
        MapVo mapVo7 = new MapVo();
        mapVo7.setKey("怎么更换自己的头像？");
        mapVo7.setValue("点击 “我的”，点击自己的头像可以选择图片就可以更换了。");
        MapVo mapVo8 = new MapVo();
        mapVo8.setKey("怎么才能有更多的粉丝？");
        mapVo8.setValue("主动去关注别人，定时更新一些有趣的微博内容，会吸引更多的粉丝。");
        MapVo mapVo9 = new MapVo();
        mapVo9.setKey("为什么别人的名字后面有金光闪闪的尊贵标志，我却没有？");
        mapVo9.setValue("在“我的—实名认证”或者在“我的”点击蓝色部分点击“姓名”，通过实名认证后就会带上尊贵标志啦");
        MapVo mapVo10 = new MapVo();
        mapVo10.setKey("如何查询APP版本？");
        mapVo10.setValue("点击“我的—设置—关于乡邻”页面上的数字就是自己的APP版本。");
        MapVo mapVo11 = new MapVo();
        mapVo11.setKey("如何查看自己的APP版本是不是最新版本？");
        mapVo11.setValue("点击“我的—设置—关于乡邻—检查新版本”，就可以查看是否是新版本了。");
        MapVo mapVo12 = new MapVo();
        mapVo12.setKey("想要在乡邻优选上卖东西或者在乡邻优选上买的东西物流一直不更新怎么办？");
        mapVo12.setValue("可以拨打乡邻优选专属服务电话：0311-87235588。");
        MapVo mapVo13 = new MapVo();
        mapVo13.setKey("我是站长，但是我的名字后面没有“乡贤”标识也看不到“银行业务”入口怎么办？");
        mapVo13.setValue("是因为您注册乡邻APP使用的手机号和市场部工作人员登记录入系统的不一致，请使用市场部工作人员登记的手机号注册乡邻APP，或者直接联系市场部工作人员说明问题。");
        MapVo mapVo131 = new MapVo();
        mapVo131.setKey("我是站长，怎样在乡邻优选下单可以拿到佣金？");
        mapVo131.setValue("让村民购物下单填写地址时选择站长代收服务，并选择自己的站点即可获得平台的佣金。");
        MapVo mapVo14 = new MapVo();
        mapVo14.setKey("我是站长，哪里可以看见自己乡邻优选的收益？");
        mapVo14.setValue("点击乡邻优选,点击“会员中心-佣金明细”。");
        MapVo mapVo15 = new MapVo();
        mapVo15.setKey("我是站长，我的乡邻优选佣金什么时候到账，什么时候可以提现？");
        mapVo15.setValue("佣金会在确认收货后的七天内到APP账户，到APP账户后一个月后可提现。");
        MapVo mapVo16 = new MapVo();
        mapVo16.setKey("我的微博怎么被删除了呀？");
        mapVo16.setValue("点击“我的—微博”可以查看自己历史发过的微博，如果微博内容涉嫌违规会被后台系统检测到并删除，系统每删除一条微博会扣除对应账户100金币。");
        MapVo mapVo17 = new MapVo();
        mapVo17.setKey("我之前用别的手机号实名认证了自己的信息，现在换了手机号去实名认证时提示我已经被认证过了，该怎么办？");
        mapVo17.setValue("联系市场部人员，说明情况，市场部人员会帮您处理。");
        MapVo mapVo18 = new MapVo();
        mapVo18.setKey("如何修改手机号？");
        mapVo18.setValue("点击“我的—设置—更改手机号”先验证当前的手机号，再输入新的手机号就可以了。修改后用新的手机号登录APP。");
        MapVo mapVo19 = new MapVo();
        mapVo19.setKey("如何设置交易密码？");
        mapVo19.setValue("点击“我的—设置—交易密码”设置6位数密码就可以了，请不要将交易密码告诉别人。");
        MapVo mapVo20 = new MapVo();
        mapVo20.setKey("如何修改登录密码？");
        mapVo20.setValue("点击“我的—设置—登录密码”，先验证当前的手机号，重新设置登录密码就可以了。");
        MapVo mapVo21 = new MapVo();
        mapVo21.setKey("如何修改交易密码？");
        mapVo21.setValue("点击“设置—交易密码—修改交易密码”，先输入原来的交易密码，验证成功后再输入新的6位数交易密码。");
        list.add(mapVo);
        list.add(mapVo1);
        list.add(mapVo2);
        list.add(mapVo3);
        list.add(mapVo7);
        list.add(mapVo8);
        list.add(mapVo9);
        list.add(mapVo10);
        list.add(mapVo11);
        list.add(mapVo12);
        list.add(mapVo13);
        list.add(mapVo131);
        list.add(mapVo14);
        list.add(mapVo15);
        list.add(mapVo16);
        list.add(mapVo17);
        list.add(mapVo18);
        list.add(mapVo19);
        list.add(mapVo20);
        list.add(mapVo21);
        list.toString();
        String json = JSONArray.fromObject(list).toString();
        System.out.print("是否邀请成功：" + list.toString());


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("LIFE", "村情村貌");
        jsonObject.put("MYAPPS", "乡邻财富");
        jsonObject.put("PERSONSERVICE", "便民生活");
        jsonObject.put("PERSONQUERY", "购物娱乐");
        jsonObject.put("PERSONLIFE", "第三方服务");
        // System.out.print("是否邀请成功："+jsonObject);

        String business_name = SysConfigUtil.getStr("BUSINESS_NAME");
//        if (StringUtil.isNotBlank(business_name)) {
//            JSONObject object = JSON.parseObject(business_name);
//            System.out.print("是否邀请成功111111111111：" + object.get("PERSONLIFE").toString());
//        }
    }

    @Test
    public void indexBusinessAllV2() {
        Response<List<BusinessAllV2>> listResponse = indexService.indexBusinessAllV2("3.5.4");
        System.out.print("indexBusinessAllV2：" + listResponse);
    }

    @Test
    public void queryBannerById() {
        Response<BannerVo> bannerVoResponse = indexService.queryBannerById(18L);
        System.out.print("queryBannerById：" + bannerVoResponse);
    }
    
    @Test
    public void queryBusinessByCode() {
        Response<BusinessVo> bannerVoResponse = indexService.queryBusinessByCode("APP-035");
        System.out.print("queryBannerById：" + bannerVoResponse);
    }
    
    @Test
    public void queryStartPage(){
        Response<BannerVo> bannerVoResponse = indexService.queryStartPage();
        System.out.print("bannerVoResponse：" + bannerVoResponse);
    }
    
    @Test
    public void queryOpeartePosition(){
        Response<List<BannerVo>> response = indexService.queryOpeartePosition();
        System.out.print("queryOpeartePosition：" + response);
    }
    
    @Test
    public void queryIndexMsg(){
        Response<List<MsgVo>> response = indexService.queryIndexMsg();
        System.out.print("queryIndexMsg：" + response);
    }

    @Test
    public void queryBusinessArticleByqueryKey(){
        ArticleReq req = new ArticleReq();
        req.setType(Constant.BusinessOrArticle.ALL.name());
        req.setQueryKey("乡");
        Response<BusinessArticleVo> businessArticleVoResponse = indexService.queryBusinessArticleByqueryKey(req);
        System.out.print("queryBusinessArticleByqueryKey：" + businessArticleVoResponse);
    }
    
    @Test
    public void indexBusinessV4(){
        Response<BusinessV2> businessV2Response = indexService.indexBusinessV4("3.5.8");
        System.out.print("indexBusinessV4：" + businessV2Response);
    }
    
    @Test
    public void putSearchRecord(){
        Response<Boolean> dd = logService.putSearchRecord("dd");
        System.out.print("indexBusinessV4：" + dd);
    }
    
    @Test
    public void queryXlQuare (){
        Response<List<BannerVo>> listResponse = indexService.queryXlQuare("XL_SQ");
        System.out.print("queryXlQuare：" + listResponse);
    }

}
