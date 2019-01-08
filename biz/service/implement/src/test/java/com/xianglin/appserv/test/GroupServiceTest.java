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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglei on 2017/9/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-biz-spring.xml"})
//@Transactional
public class GroupServiceTest {

    @Autowired
    private OrganizeService organizeService;

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private PersonalService personalService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private AccountantMerchantService accountantMerchantService;

    @Autowired
    private ArticleService articleService;

    @Before
    public void initSession() {

        Session session = new MapSession();
        session.setAttribute(SessionConstants.PARTY_ID, 1000000000002324L);
        session.setAttribute(SessionConstants.CLIENT_VERSION, "3.5.3");
        sessionHelper.saveLocalSesson(session);
    }

    @Test
    public void applyTest() {

        Response<Boolean> resp = organizeService.apply(4L);
        System.out.println(resp);
    }

    @Test
    public void create() {

        System.out.print("1111111111111111111111111111111111111111111111111111111");
        OrganizeVo vo = OrganizeVo.builder().name("dszxTest").build();
        Response<Boolean> resp = organizeService.create(vo);
        System.out.println(resp);
        resp = organizeService.create(vo);
        System.out.println(resp);
    }

    @Test
    public void queryOrganize() {

        System.out.print("1111111111111111111111111111111111111111111111111111111");
        Response<List<OrganizeVo>> resp = organizeService.queryOrganize("一");
        System.out.println(resp);
    }

    @Test
    public void listApply() {

        Response<List<OrganizeApplyVo>> resp = organizeService.listApply(4L);
        System.out.println(resp);
    }

    @Test
    public void addMember() {

        System.out.print("1111111111111111111111111111111111111111111111111111111");
        MemberVo member = new MemberVo();
        member.setName("hkkn");
        member.setMobilePhone("13601656393");
        member.setGroupId(73L);
        Response<Boolean> resp = organizeService.addMember(member);
        System.out.println(resp);
    }

    @Test
    public void batchAddmember() {

        List<MemberVo> list = new ArrayList<>();
        /*MemberVo member = new  MemberVo();
        member.setName("李亚运");
        member.setMobilePhone("13122359790");
        member.setGroupId(9L);
        list.add(member);*/
        MemberVo member2 = new MemberVo();
        member2.setName("胡伟");
        member2.setMobilePhone("15824387760");
        member2.setGroupId(4L);
        list.add(member2);
        Response<Boolean> resp = organizeService.batchAddmember(list);
        System.out.println(resp);
    }

    @Test
    public void defaultOrganize() {

        Response<OrganizeVo> resp = organizeService.defaultOrganize();
        System.out.println(resp);
    }

    @Test
    public void confirmApply() {

        Response<Boolean> resp = organizeService.confirmApply(15L);
        System.out.println(resp);
    }

    @Test
    public void listOrganize() {

        Response<List<OrganizeVo>> resp = organizeService.listOrganize();
        System.out.println(resp);
    }

    @Test
    public void organizeDetail() {

        Response<OrganizeVo> resp = organizeService.organizeDetail(43L);
        System.out.println(resp);
    }

    @Test
    public void deleteMember() {

        Response<Boolean> resp = organizeService.deleteMember(1, 4L);
        System.out.println(resp);
    }

    @Test
    public void queryNoticeList() {

        PageReq req = new PageReq();
        req.setPageSize(10);
        req.setStartPage(1);
        Response<List<OrganizeNoticeVo>> resp = organizeService.queryNoticeList(9L, req);
        System.out.println(resp);
    }

    @Test
    public void publishNotice() {

        OrganizeNoticeVo vo = new OrganizeNoticeVo();
        vo.setTitle("gvbxb cfb");
        vo.setOrganzeId(9L);
        vo.setContent("bbdszbbbbbbbbbbbbbbbbzxczccccccccccccccc");
        Response<Boolean> resp = organizeService.publishNotice(vo);
        System.out.println(resp);
    }

    @Test
    public void queryNotice() {

        Response<OrganizeNoticeVo> resp = organizeService.queryNotice(2L);
        System.out.println(resp);
    }

    @Test
    //?
    public void exit() {

        Response<Boolean> resp = organizeService.exit(9L);
        System.out.println(resp);
    }

    @Test
    public void assignManager() {

        Response<Boolean> resp = organizeService.assignManager(1000000000002373L, 300L);
        System.out.println(resp);
    }

    @Test
    public void queryPersonal() {

        System.out.print("1111111111111111111111111111111111111111111111111111111");
        Response<UserVo> resp = personalService.queryPersonal();
        System.out.println(resp);

    }

    @Test
    public void createGroup() {

        List<MemberVo> list = new ArrayList<>();
        MemberVo memberVo2 = new MemberVo();
        memberVo2.setName("公举");
        memberVo2.setMobilePhone("15515809629");
        memberVo2.setPartyId(1000000000002124L);
        list.add(memberVo2);
        MemberVo memberVo3 = new MemberVo();
        memberVo3.setName("李亚运");
        memberVo3.setMobilePhone("13122359790");
        memberVo3.setPartyId(7000013L);
        list.add(memberVo3);
        MemberVo memberVo4 = new MemberVo();
        memberVo4.setName("李绪阳");
        memberVo4.setMobilePhone("18317089341");
        memberVo4.setPartyId(1000000000002211L);
        list.add(memberVo4);
        
        /*MemberVo memberVo7 = new MemberVo();
        memberVo7.setName("你就那可口可乐看看");
        memberVo7.setMobilePhone("15026554967");
        memberVo7.setPartyId(7003658L);
        list.add(memberVo7);*/
        Response<GroupVo> resp = groupService.create(list);
        System.out.println(resp);
        resp = groupService.create(list);
        System.out.println(resp);

    }

    @Test
    public void list() {

        Response<List<GroupVo>> resp = groupService.list();
        System.out.println(resp);
    }

    @Test
    public void update() {

        GroupVo vo = new GroupVo();
        vo.setName("群聊啊啊");
        vo.setId(25L);
        Response<Boolean> resp = groupService.update(vo);
        System.out.println(resp);
    }

    @Test
    public void listMembers() {

        Response<List<MemberVo>> resp = groupService.listMembers(301);
        System.out.println(resp);
    }

    @Test
    public void exitGroup() {

        Response<Boolean> resp = groupService.exit(114);
        System.out.println(resp);

    }

    @Test
    public void deleteMember1() {

        Response<Boolean> resp = groupService.deleteMember(71, 25);
        System.out.println(resp);
    }

    @Test
    public void batchDeleteMember() {

        List<Long> memberIds = new ArrayList<>();
        memberIds.add(194L);
        memberIds.add(197L);
        Response<Boolean> resp = groupService.batchDeleteMember(memberIds, 64);
        System.out.println(resp);
    }

    @Test
    public void assignManager1() {

        Response<Boolean> resp = groupService.assignManager(1000000000002139L, 11);
        System.out.println(resp);
    }

    @Test
    public void addNativeMembers() {

        List<MemberVo> list = new ArrayList<>();
        /*MemberVo memberVo = new MemberVo();
        memberVo.setName("呵呵");
        memberVo.setMobilePhone("13402160951");
        memberVo.setPartyId(1000000000002121L);
        list.add(memberVo);
        MemberVo memberVo1 = new MemberVo();
        memberVo1.setName("莫莫");
        memberVo1.setMobilePhone("18702106960");
        memberVo1.setPartyId(7000044L);
        list.add(memberVo1);
        MemberVo memberVo2 = new MemberVo();
        memberVo2.setName("公举");
        memberVo2.setMobilePhone("15515809629");
        memberVo2.setPartyId(1000000000002124L);
        list.add(memberVo2);
        MemberVo memberVo3 = new MemberVo();
        memberVo3.setName("走在乡邻的小路上");
        memberVo3.setMobilePhone("17671434117");
        memberVo3.setPartyId(1000000000002172L);
        list.add(memberVo3);
        MemberVo memberVo4 = new MemberVo();
        memberVo4.setName("你就那可口可乐看看");
        memberVo4.setMobilePhone("15026554967");
        memberVo4.setPartyId(7003658L);
        list.add(memberVo4);
        MemberVo memberVo5 = new MemberVo();
        memberVo5.setName("ccccccccccccccccccc");
        memberVo5.setMobilePhone("15156684305");
        memberVo5.setPartyId(1000000000002139L);
        list.add(memberVo5);  */
        MemberVo memberVo6 = new MemberVo();
        memberVo6.setName("曹延昌");
        memberVo6.setMobilePhone("18616763041");
        memberVo6.setPartyId(1000000000002044L);
        list.add(memberVo6);
        MemberVo memberVo7 = new MemberVo();
        memberVo7.setName("公举");
        memberVo7.setMobilePhone("15515809629");
        memberVo7.setPartyId(1000000000002124L);
        list.add(memberVo7);
        Response<List<MemberVo>> resp = groupService.addNativeMembers(list, 64);
        System.out.println(resp);
    }

    @Test
    public void getUserVo() {

        Response<UserVo> resp = personalService.queryPersonal();
        System.out.println(resp);
    }

    @Test
    public void updateUser() {

        UserVo userVo = new UserVo();
        userVo.setNikerName("xingyali");
        userVo.setHeadImg("");
        userVo.setGender("女");
        userVo.setId(151L);
        userVo.setIntroduce("dddddd");
        userVo.setBirthday("2017-09-22");
        userVo.setDistrict(JSON.parseObject("{\"province\":{\"code\":\"23\",\"name\":\"黑龙江省\"},\"city\":{\"code\":\"2309\",\"name\":\"七台河市\"},\"county\":{\"code\":\"230903\",\"name\":\"桃山区\"},\"town\":{\"code\":\"230903100\",\"name\":\"万宝河镇\"},\"village\":{\"code\":\"\",\"name\":\"\"},\"district\":null}", AreaVo.class));
        Response<UserVo> resp = personalService.updateUser(userVo);
        System.out.println(resp);
    }

    @Test
    public void createNativeMembers() {

        List<MemberVo> list = new ArrayList<>();
        /*MemberVo memberVo = new MemberVo();
        memberVo.setName("呵呵");
        memberVo.setMobilePhone("13402160951");
        list.add(memberVo);
        MemberVo memberVo1 = new MemberVo();
        memberVo1.setName("莫莫");
        memberVo1.setMobilePhone("18702106960");
        list.add(memberVo1);
        MemberVo memberVo2 = new MemberVo();
        memberVo2.setName("公举");
        memberVo2.setMobilePhone("15515809629");
        list.add(memberVo2);
        MemberVo memberVo3 = new MemberVo();
        memberVo3.setName("走在乡邻的小路上");
        memberVo3.setMobilePhone("17671434117");
        list.add(memberVo3);
        MemberVo memberVo4 = new MemberVo();
        memberVo4.setName("你就那可口可乐看看");
        memberVo4.setMobilePhone("15026554967");
        list.add(memberVo4);
        MemberVo memberVo5 = new MemberVo();
        memberVo5.setName("ccccccccccccccccccc");
        memberVo5.setMobilePhone("15156684305");
        list.add(memberVo5); 
        MemberVo memberVo6 = new MemberVo();
        memberVo6.setName("rffgvdxxccccccc");
        memberVo6.setMobilePhone("15588650613");
        list.add(memberVo6);
        MemberVo memberVo7 = new MemberVo();
        memberVo7.setName("14783");
        memberVo7.setMobilePhone("15021386706");
        list.add(memberVo7);
        MemberVo memberVo8 = new MemberVo();
        memberVo8.setName("xxx");
        memberVo8.setMobilePhone("13520155221");
        list.add(memberVo8);
        MemberVo memberVo9 = new MemberVo();
        memberVo9.setName("fvb");
        memberVo9.setMobilePhone("4555566666");
        list.add(memberVo9);*/
        MemberVo memberVo10 = new MemberVo();
        memberVo10.setName("433");
        memberVo10.setMobilePhone("1542162141");
        list.add(memberVo10);
        MemberVo memberVo11 = new MemberVo();
        memberVo11.setName("宋加林");
        memberVo11.setMobilePhone("15156684305");
        list.add(memberVo11);
        MemberVo memberVo12 = new MemberVo();
        memberVo12.setName("曹延昌");
        memberVo12.setMobilePhone("18616763041");
        list.add(memberVo12);
        Response<List<MemberVo>> resp = groupService.createNativeMembers(list);
        System.out.println(resp);
    }

    @Test
    public void createMembers() {

        long a = 0;
        Response<List<MemberVo>> resp = groupService.createMembers(a);
        System.out.println(resp);
    }


    @Test
    public void queryGroup() {

        Response<GroupVo> resp = groupService.queryGroup(70);
        System.out.println(resp);
    }


    @Test
    public void queryRU() {

        String aa = RyUtil.query("9153bf5b-5076-4a53-afc0-16896c2ffedc");
        System.out.println(aa);
    }

    @Test
    public void queryGroupByRUId() {

        Response<GroupVo> resp = groupService.queryGroupByRUId("ac1f2ab6-9bf9-4f40-8a7b-85e2f4f6487b");
        System.out.println(resp);
    }

    @Test
    public void queryDefaultMerchant() {

        Response<MerchantInfoVo> response = accountantMerchantService.queryDefaultMerchant();
        System.out.println(response);
    }

    @Test
    public void dismiss() {

        Response<Boolean> response = groupService.dismiss(59);
        System.out.println(response);
    }

    @Test
    public void personInfo() {

        System.out.print("1111111111111111111111111111111111111111111111111111111");
        Response<PersonalVo> response = personalService.personInfo();
        System.out.println(response);
    }

    @Test
    public void queryArticleTipList() {

        ArticleTipReq articleTipReq = new ArticleTipReq();
        articleTipReq.setPageSize(10);
        articleTipReq.setStartPage(0);
        articleTipReq.setTipType("SHARE");
        Response<List<ArticleTipVo>> response = articleService.queryArticleTipList(articleTipReq);
        System.out.println(response);
    }

    @Test
    public void sendMessage() {

        Response<Boolean> resp = organizeService.sendMessage("13402160951");
        System.out.println(resp);
    }

    @Test
    public void deleteApply() {

        Response<Boolean> resp = organizeService.deleteApply(46L);
        System.out.println(resp);
    }
    
    @Test 
    public void isApplyVillage(){
        Response<Boolean> resp = organizeService.isApplyVillage();
        System.out.println("返回是否已经申请试点村+"+resp);
    }
    
    @Test
    public void ApplyVillage(){
        Response<Boolean> resp = organizeService.ApplyVillage();
        System.out.println("返回是否已经申请试点村+"+resp);
    }

    @Test
    public void isVillageAndGroupManager() {

        Response<VillageManagerVo> resp = organizeService.isVillageAndGroupManager();
        System.out.println("返回是否已经申请试点村+" + resp);
    }

    @Test
    public void queryOrganizeParas() {

        Response<List<OrganizeVo>> resp = organizeService.queryOrganizeParas(OranginzeReq.builder().status("Y").build());
        System.out.println("返回是否已经申请试点村+" + resp);
    }
    
    @Test
    public void updateApplyVillage(){
        OrganizeApplyVo organizeApplyVo = new OrganizeApplyVo();
        organizeApplyVo.setId(541L);
        organizeApplyVo.setStatus("Y");
        Response<Boolean> resp = organizeService.updateApplyVillage(organizeApplyVo);
        System.out.println("返回是否已经申请试点村+" + resp);
    }

}
