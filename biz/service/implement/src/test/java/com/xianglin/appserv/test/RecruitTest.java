package com.xianglin.appserv.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xianglin.appserv.biz.shared.util.ResponseCacheUtils;
import com.xianglin.appserv.common.service.facade.app.RecruitService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.RecruitEnum;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.RecruitJobReq;
import com.xianglin.appserv.common.service.facade.req.RecruitJobResumeReq;
import com.xianglin.appserv.common.service.facade.req.RecruitResumeReq;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.fala.session.MapSession;
import com.xianglin.fala.session.Session;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Describe :
 * Created by xingyali on 2018/4/19 15:59.
 * Update reason :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-biz-spring.xml"})
public class RecruitTest {

    @Autowired
    private SessionHelper sessionHelper;
    
    @Autowired
    private RecruitService recruitService;

    @Autowired
    private ResponseCacheUtils responseCacheUtils;

    @Before
    public void initSession(){
        Session session = new MapSession();
        session.setAttribute(SessionConstants.PARTY_ID,1000000000002926L);
        sessionHelper.saveLocalSesson(session);
    }
    
    @Test
    public void queryRecruitJobList(){
        RecruitJobReq req= new RecruitJobReq();
        req.setStartPage(1);
        req.setPageSize(10);
        req.setComments("TOP");
        req.setStartDay("2018-10-01 00:00:00");
        req.setEndDay("2018-10-17 15:15:32");
        /*req.setPayStart(1000);
        req.setPayEnd(2000);*/
        
        //req.setCompanyName("上海");
        //req.setComments("TOP");
        /*req.setPayStart(8000);
        req.setPayType("A");
        req.setPayEnd(12000);*/
        //req.setShowName("幸雅丽");
        //req.setIsCommission("Y");
        //req.setJobName("保姆");
        Response<List<RecruitJobVo>> listResponse = recruitService.queryRecruitJobList(req);
        Response<Integer> integerResponse = recruitService.queryRecruitJobCount(req);
        System.out.print("招工列表查询："+listResponse);
        System.out.print("招工列表数："+integerResponse);
    }
    
    @Test
    public void queryRecruitJobList2(){
        RecruitJobReq req= new RecruitJobReq();
        Response<List<RecruitJobV2>> listResponse = recruitService.queryRecruitJobList2(req);
        System.out.print("招工列表查询："+listResponse);
    }
    
    @Test
    public void queryRecruitJobById(){
        Response<RecruitJobVo> recruitJobVoResponse = recruitService.queryRecruitJobById("RECRUIT",1114L);
        System.out.print("查招工详情："+recruitJobVoResponse);
    }
    
    @Test
    public void publishRecruitJob(){
        RecruitJobVo recruitJobVo = new RecruitJobVo();
        recruitJobVo.setCompanyName("4444");
        recruitJobVo.setContactName("aaaa");
        recruitJobVo.setContactPhone("13402160951");
        recruitJobVo.setCity("上海市");
        recruitJobVo.setProvince("上海");
        AreaVo areaVo = new AreaVo();
        DistrictVo districtVo = new DistrictVo();
        districtVo.setCode("11");
        districtVo.setName("上海");
        areaVo.setProvince(districtVo);
        recruitJobVo.setArea(areaVo);
        recruitJobVo.setJobName("招测试一名A");
        recruitJobVo.setPayStart(5000);
        recruitJobVo.setPayEnd(10000);
        recruitJobVo.setDesc("招测试一名啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
        recruitJobVo.setPayType("A");
        Response<Boolean> booleanResponse = recruitService.publishRecruitJob(recruitJobVo);
        System.out.print("发布招工："+booleanResponse);
    }
    
    @Test
    public void publishRecruitResume(){
//        String str = "[\n" +
//                "    {\n" +
//                "        first: \"餐饮酒店\",\n" +
//                "        second: \"服务员\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        first: \"餐饮酒店\",\n" +
//                "        second: \"厨师\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        first: \"商务服务\",\n" +
//                "        second: \"销售代表\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        first: \"商超零售\",\n" +
//                "        second: \"促销导购\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        first: \"生活服务\",\n" +
//                "        second: \"保安\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        first: \"美容美发\",\n" +
//                "        second: \"美甲师\"\n" +
//                "    }\n" +
//                "]\n";
//        String city = "[\n" +
//                "    {\n" +
//                "        first: \"餐饮酒店\",\n" +
//                "        second: \"全北京\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        first: \"餐饮酒店\",\n" +
//                "        second: \"厨师\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        first: \"商务服务\",\n" +
//                "        second: \"销售代表\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        first: \"商超零售\",\n" +
//                "        second: \"促销导购\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        first: \"生活服务\",\n" +
//                "        second: \"保安\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        first: \"美容美发\",\n" +
//                "        second: \"美甲师\"\n" +
//                "    }\n" +
//                "]\n";
//        ArrayList<Map<String,Object>> jobList = new Gson().fromJson(str, new TypeToken<List<Map<String,Object>>>() {}.getType());
//        ArrayList<Map<String,Object>> cityList = new Gson().fromJson(city, new TypeToken<List<Map<String,Object>>>() {}.getType());
        RecruitResumeVo recruitResumeVo = new RecruitResumeVo();
        recruitResumeVo.setType(RecruitEnum.ResumeType.PSERSONAL.name());
        recruitResumeVo.setName("姜永涛");
        recruitResumeVo.setEthnicity("汉");
        recruitResumeVo.setEducation("小学");
        recruitResumeVo.setCriminalRecord("Y");
        recruitResumeVo.setCertificatesNumber("330821199406221027");
        recruitResumeVo.setContactPhone("15264562521");
        recruitResumeVo.setGender("男");
        recruitResumeVo.setDistrict("");
        recruitResumeVo.setLeaderSign("Y");
        recruitResumeVo.setRecommendPhone("115262");
//        recruitResumeVo.setJobList(jobList);
//        recruitResumeVo.setCityList(cityList);
        Response<RecruitResumeVo> recruitResumeVoResponse = recruitService.publishRecruitResume(recruitResumeVo);
        System.out.print("发布个人简历、推荐简历、求职意向："+recruitResumeVoResponse);
    }
    
    @Test
    public void deleteRecruitJob(){
        Response<Boolean> booleanResponse = recruitService.deleteRecruitJob(1271L);
        System.out.print("删除招工："+booleanResponse);
    }
    
    @Test
    public void publishRecruitJobResume(){
         Response<Boolean> booleanResponse = recruitService.publishRecruitJobResume(1544L, 54L);
        System.out.print("投递简历："+booleanResponse);
    }
    
    @Test
    public void queryRecruitResumeListByType(){
        Response<List<RecruitResumeVo>> listResponse = recruitService.queryRecruitResumeListByType("RECOMMEND",new PageReq());
        System.out.print("根据类型查当前用户的个人简历/推荐简历/求职意向："+listResponse);
    }
    
    @Test
    public void isShowRecruitJob(){
        Response<Map<String, Object>> showRecruitJob = recruitService.isShowRecruitJob();
        System.out.print("返回是否显示我的招聘、我的求职、我的推荐："+showRecruitJob);
        
    }
    
    @Test
    public void queryJobRecommend(){
        Response<List<RecruitJobVo>> listResponse = recruitService.queryJobRecommend("RECOMMEND",new PageReq());
        System.out.print("查询当前用户的求职或推荐："+listResponse);
    }
    
    @Test
    public void queryRecruitResumeListByJobId(){
       // Response<List<RecruitResumeVo>> listResponse = recruitService.queryRecruitResumeListByJobId(1L);
        //System.out.print("根据招聘id查询投递的简历："+listResponse);
    }
    
    @Test
    public void updateJobResumeStatus(){
        RecruitJobResumeVo recruitJobResumeVo= new RecruitJobResumeVo();
        recruitJobResumeVo.setJobId(1L);
        recruitJobResumeVo.setRecruitId(1L);
        recruitJobResumeVo.setManageStatus("A");
        Response<Boolean> booleanResponse = recruitService.updateJobResumeStatus(recruitJobResumeVo);
        System.out.print("修改简历投递状态："+booleanResponse);
    }
    
    @Test
    public void queryRecruitResume(){
        RecruitResumeReq recruitResumeReq = new RecruitResumeReq();
        Response<List<RecruitResumeVo>> listResponse = recruitService.queryRecruitResume(recruitResumeReq);
        Response<Integer> integerResponse = recruitService.queryRecruitResumeCount(recruitResumeReq);
        System.out.print("查询求职人员："+listResponse.toString());
        System.out.print("查询求职人员数："+integerResponse);
    }

    public static int IdNOToAge(String IdNO){
        int leh = IdNO.length();
        String dates="";
        //int se = Integer.valueOf(IdNO.substring(leh - 1)) % 2;
        if (leh == 18) {
            dates = IdNO.substring(6, 10);
        }else{
            dates = "19"+IdNO.substring(6, 8);  
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String year=df.format(new Date());
        int u=Integer.parseInt(year)-Integer.parseInt(dates);
        return u;
       

    }

    public static void main(String[] args) {
        String expectCity="-,辽宁省-沈阳市";
        String[] image = expectCity.split(",");
        String[] smallImage = new String[image.length];
        for (int i = 0; i < image.length; i++) {
            if(image[i].equals("-")){
                smallImage[i] ="不限";
            }else{
                smallImage[i] = image[i]; 
            }
        }
        String expentCity =  StringUtils.join(smallImage,",");
        System.out.println(expentCity);
    }
    
    @Test
    public void queryRecruitJobByJobResume(){
        Response<List<RecruitJobVo>> listResponse = recruitService.queryRecruitJobByJobResume(RecruitJobResumeReq.builder().recruitId(1L).build());
        Response<Integer> integerResponse = recruitService.queryRecruitJobCountByJobResume(RecruitJobResumeReq.builder().recruitId(1L).build());
        System.out.print("年龄："+listResponse);
        System.out.print("年龄："+integerResponse);
    }
    
    @Test
    public void deleteResruitResume(){
        Response<Boolean> booleanResponse = recruitService.deleteResruitResume(2L);
        System.out.print("删除简历："+booleanResponse);
    }
    
    @Test
    public void queryRecruitResumeById(){
        Response<RecruitResumeVo> recruitResumeVoResponse = recruitService.queryRecruitResumeById(146L);
        System.out.print("查简历："+recruitResumeVoResponse);
    }
    
    @Test
    public void publishRecruitJobDraft(){
        Response<Boolean> booleanResponse = recruitService.publishRecruitJobDraft("111");
        System.out.print("新增招工草稿："+booleanResponse); 
    }
    
    @Test
    public void queryRecruitJobDraft(){
        Response<String> stringResponse = recruitService.queryRecruitJobDraft();
        System.out.print("查询当前登录用户的招工草稿："+stringResponse);
    }
    
    @Test
    public void cleanRecruitJobDraft(){
        Response<Boolean> booleanResponse = recruitService.cleanRecruitJobDraft();
        System.out.print("清除当前登录用户的招工草稿："+booleanResponse);
    }
    
    @Test
    public void queryRecommendJobName(){
        RecruitJobReq recruitJobReq = new RecruitJobReq();
        recruitJobReq.setPageSize(10);
        recruitJobReq.setStartPage(1);
        recruitJobReq.setJobCategory("推荐岗位");
        Response<List<RecruitJobVo>> listResponse = recruitService.queryRecommendJob(recruitJobReq);
        System.out.print("查询推荐岗位："+listResponse);
    }

    @Test
    public void queryJobResumeTipsByJobResumeId(){
        Response<Map<String,Object>> response = recruitService.queryJobResumeTipsByJobResumeId(182L);
        System.out.println("查询岗位申请进度：" + JSON.toJSONString(response));
    }


    @Test
    public void insertUserReceiptAccount(){
        UserRecommendedVo userRecommendedVo = new UserRecommendedVo();
        userRecommendedVo.setAccountNumber("127381623123");
        userRecommendedVo.setBank("中国银行");
        userRecommendedVo.setUserAccountName("姜永涛");
        Response<Boolean> response = recruitService.insertUserReceiptAccount(userRecommendedVo);
        System.out.println("保存用户收款账户信息" + JSON.toJSONString(response));
    }

    @Test
    public void queryUserReceiptAccount(){
        Response<UserRecommendedVo> resp = recruitService.queryUserReceiptAccount();
        System.out.println("查询用户收款账户信息" +JSON.toJSONString(resp));
    }

    @Test
    public void queryUserReward(){
        Response<Map<String,Object>> resp = recruitService.queryUserReward();
        System.out.println("查询用户佣金信息" + JSON.toJSONString(resp));
    }

    @Test
    public void queryAllCommission(){
        Response<List<Map<String,Object>>> resp = recruitService.queryAllCommission();
        System.out.println("查询用户佣金信息" + resp);
    }

    @Test
    public void queryAllCommissionTotal(){
        Response<List<Map<String,Object>>> resp = recruitService.queryAllCommissionTotal();
        System.out.println("查询用户佣金信息" + JSON.toJSONString(resp));
    }

    @Test
    public void queryUserRecruitJurisdiction(){
        Response<Boolean> response = recruitService.queryUserRecruitJurisdiction();
        System.out.println("查询用户是否拥有发布带有佣金的职位的权限"+ JSONObject.toJSONString(response));
    }

    @Test
    public void queryJobRecommendByCommission(){
        Response<List<RecruitJobVo>> listResponse = recruitService.queryJobRecommendByCommission("N",new PageReq());
        System.out.print("查询当前用户的求职或推荐："+JSON.toJSONString(listResponse));
    }

    @Test
    public void cacheRefresh(){
        responseCacheUtils.refreshCache(ResponseCacheUtils.ResponseCacheKey.RECRUIT_LIST);
    }

}
