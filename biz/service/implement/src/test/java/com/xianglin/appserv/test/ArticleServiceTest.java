package com.xianglin.appserv.test;

import com.xianglin.appserv.biz.shared.ArticleManager;
import com.xianglin.appserv.biz.shared.SysParaManager;
import com.xianglin.appserv.common.service.facade.app.ArticleService;
import com.xianglin.appserv.common.service.facade.base.PageResult;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.ArticlePopularType;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.ArticleReq;
import com.xianglin.appserv.common.service.facade.req.ArticleTipReq;
import com.xianglin.appserv.common.service.facade.req.ArticleTopicReq;
import com.xianglin.appserv.common.util.HttpUtils;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.appserv.common.util.SysConfigUtil;
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
public class ArticleServiceTest {

    @Autowired
    private ArticleService service;

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private SysParaManager sysParaManager;

    @Autowired
    private ArticleManager articleManager;

    @Before
    public void initSession() {
        Session session = new MapSession();
        session.setAttribute(SessionConstants.PARTY_ID, 1000000000002458L);
//        session.setAttribute(SessionConstants.U|SER_TYPE,"user");
        session.setAttribute(SessionConstants.DEVICE_ID, "DLKAJFALSIEIRSAI4324KLISFA");
        sessionHelper.saveLocalSesson(session);
        sysParaManager.queryPara().forEach(config -> {
            SysConfigUtil.put(config.getCode(),config.getValue());
        });
    }

    @Test
    public void queryArticleListV2() {
        ArticleReq req = new ArticleReq();
        Response<List<ArticleVo>> resp = service.queryArticleListV2(req);
        System.out.println(resp);
    }

    public static void main(String[] args) {
        /*UserGenealogyVo vo1 = UserGenealogyVo.builder().id(1L).name("用户1").gender("男").headImg("https://appfile.xianglin.cn/file/879398").build();
        UserGenealogyVo vo2 = UserGenealogyVo.builder().id(2L).parentId(1L).name("用户1").gender("男").headImg("https://appfile.xianglin.cn/file/879398").build();
        UserGenealogyVo vo3 = UserGenealogyVo.builder().id(3L).parentId(1L).name("用户1").gender("男").headImg("https://appfile.xianglin.cn/file/879398").build();
        vo1.getSubUsers().add(vo2);
        vo1.getSubUsers().add(vo3);
        UserGenealogyVo vo4 = UserGenealogyVo.builder().id(4L).parentId(2L).name("用户1").gender("男").headImg("https://appfile.xianglin.cn/file/879398").build();
        UserGenealogyVo vo5 = UserGenealogyVo.builder().id(5L).parentId(2L).name("用户1").gender("男").headImg("https://appfile.xianglin.cn/file/879398").build();
        UserGenealogyVo vo6 = UserGenealogyVo.builder().id(6L).parentId(2L).name("用户1").gender("男").headImg("https://appfile.xianglin.cn/file/879398").build();
        vo2.getSubUsers().add(vo4);
        vo2.getSubUsers().add(vo5);
        vo2.getSubUsers().add(vo6);
        UserGenealogyVo vo7 = UserGenealogyVo.builder().id(7L).parentId(4L).name("用户1").gender("男").headImg("https://appfile.xianglin.cn/file/879398").build();
        UserGenealogyVo vo8 = UserGenealogyVo.builder().id(8L).parentId(4L).name("用户1").gender("男").headImg("https://appfile.xianglin.cn/file/879398").build();
        UserGenealogyVo vo9 = UserGenealogyVo.builder().id(9L).parentId(5L).name("用户1").gender("男").headImg("https://appfile.xianglin.cn/file/879398").build();

        vo4.getSubUsers().add(vo7);
        vo4.getSubUsers().add(vo8);
        vo5.getSubUsers().add(vo9);

        System.out.println(JSON.toJSON(vo1));*/
       String  articleAudio  =  HttpUtils.executePost2("http://172.16.6.35:8080/file/"+"convert/amr2mp3?url="+"https://appfile-test.xianglin.cn/file/443611","",10000);
        System.out.println(articleAudio);
    }
    
    @Test
    public void reportUser(){
        Response<Boolean> resp = service.reportUser(1000000000002324L,"其他");
        System.out.println("举报用户" + resp);
    }
    
    @Test
    public void queryReprotList(){
        Response<List<String>> resp =  service.queryReprotList(1000000000002324L);
        System.out.println("举报用户" + resp);
    }

    @Test
    public void praiseArticle(){
        Response<Long> resp =  service.praiseArticle(6451L);
        System.out.println("举报用户" + resp);
    }
    
    @Test
    public void queryArticleCountByType(){
        Response<Integer> response = service.queryArticleCountByType("LEARNING_PPT");
        System.out.println("举报用户" + response);
    }
    
    @Test
    public void deleteArticle(){
        Response<Boolean> booleanResponse = service.deleteArticle(7202L);
        System.out.println("举报用户" + booleanResponse);
    }
    
    @Test
    public void queryArticleListByIds(){
        Long arrayDemo[] = {2196L,2197L}; 
        Response<List<ArticleVo>> listResponse = service.queryArticleListByIds(arrayDemo);
        System.out.println("举报用户" + listResponse);
    }
    
    @Test
    public void queryArticleContByParam(){
        ArticleReq articleReq = new ArticleReq();
        articleReq.setIsExcludeShareUrl(false);
        articleReq.setStartDate("2018-10-11");
        articleReq.setEndDate("2018-10-11");
        Response<List<ArticleVo>> listCommonResp = service.queryArticleListByParam(articleReq);
        Response<Integer> integerResponse = service.queryArticleContByParam(articleReq);
        System.out.println("微博" + listCommonResp);
        System.out.println("微博数" + integerResponse);
    }

    @Test
    public void publishArticleTip(){
        ArticleTipVo articleTipVo= new ArticleTipVo();
        articleTipVo.setPartyId(1000000000002701L);
        articleTipVo.setToPartyId(1000000000002458L);
        articleTipVo.setTipType(Constant.ArticleTipType.ACT.name());
        articleTipVo.setTipStatus(Constant.YESNO.YES.code);
        articleTipVo.setContent("给你投了一票");
        Response<Boolean> booleanResponse = service.publishArticleTip(articleTipVo);
        System.out.println("发布提醒" + booleanResponse);
    }
    
    @Test
    public void queryArticleTipListV1(){
        ArticleTipReq req=new ArticleTipReq();
        Response<List<ArticleTipV2>> listResponse = service.queryArticleTipListV1(req);
        System.out.println("提醒列表" + listResponse);
    }
    
    @Test
    public void queryArticle(){
        Response<ArticleVo> articleVoResponse = service.queryArticle(7511L);
        System.out.println("动态明细" + articleVoResponse);
    }
    
    @Test
    public void queryFollowArticle(){
        ArticleReq articleReq = new ArticleReq();
        articleReq.setFollowType(Constant.FollowType.FOLLOW.name());
        Response<List<ArticleVo>> listResponse = service.queryFollowArticle(articleReq);
        System.out.println("queryFollowArticle" + listResponse);
    }
    
    @Test
    public void queryRecommendArticle(){
        Response<List<ArticleVo>> listResponse = service.queryRecommendArticle();
        System.out.println("queryRecommendArticle" + listResponse);
    }

    @Test
    public void queryPopularArticles(){
        Response<List<Long>> listResponse = service.queryPopularArticles(ArticlePopularType.WEEK.name());
        System.out.println("queryPopularArticles" + listResponse);
    }

    @Test
    public void queryVideoListByArticleType(){
        PageReq pageResp = new PageReq();
        Response<List<ArticleVo>> articleVoList = service.queryCollectArticleV2(pageResp,"SUBJECT");
        System.out.println("articleVoList" + articleVoList);
    }

    @Test
    public void queryAppArticleByPageAndParam(){
        ArticleTipReq articleTopicReq = new ArticleTipReq();
        articleTopicReq.setStartPage(1);
        articleTopicReq.setPageSize(10);
        Response<PageResult<ArticleTipVoV3>> pageResultResponse = service.queryArticleTipListByParams(articleTopicReq);
        System.out.println("pageResultResponse=====================>>" + pageResultResponse.getResult().getData().toString());
    }

    @Test
    public void publishShortVideo(){
        ArticleVo vo = ArticleVo.builder().article("dlfjdl;aa#中秋节#打发多大#中秋节#发生#国庆节#打开就分开了打工#1111111").build();
        Response<Boolean> resp = service.publishShortVideo(vo);
        System.out.println(resp);
    }

    @Test
    public void queryTopicList(){
        Response<List<ArticleTopicVo>> resp = service.queryTopicList();
        System.out.println(resp);
    }
    
    @Test
    public void queryShortVideo(){
        Response<PageResult<ArticleVo>> response = service.queryShortVideo(ArticleReq.builder().build());
        System.out.println("查询top端视频列表"+response);
    }

    @Test
    public void queryArticleTopicListByPageAndParams(){
        ArticleTopicReq articleTopicReq = new ArticleTopicReq();
        Response<PageResult<ArticleTopicVo>>  response = service.queryArticleTopicListByPageAndParams(articleTopicReq);
        System.out.println("response=================================>>>>>>>>>"+response.getResult().toString());
    }
    
    @Test
    public void updateArticle(){
        Response<ArticleVo> articleVoResponse = service.updateArticle(ArticleVo.builder().id(8305L).topLevel(0).build());
        System.out.println("response=================================>>>>>>>>>"+articleVoResponse.getResult().toString());
    }

//    @Test
//    public void queryArticleTopicCout(){
//        Integer count = articleManager.queryArticleTopicCout();
//        System.out.println("count==========================>>>>>>>>"+count);
//    }
}
