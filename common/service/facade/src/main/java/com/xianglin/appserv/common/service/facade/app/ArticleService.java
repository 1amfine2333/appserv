package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.base.PageResult;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.ArticleReq;
import com.xianglin.appserv.common.service.facade.req.ArticleTipReq;
import com.xianglin.appserv.common.service.facade.req.ArticleTopicReq;

import java.util.List;

/**
 * 动态相关服务 Created by wanglei on 2017/2/13.
 */
public interface ArticleService {

    /**
     * 分页查询动态 需要判断当前用户的类型
     *
     * @param req
     * @return
     */
    Response<List<ArticleVo>> queryArticleList(PageReq req, Long partyId);

    /**
     * 动态明细
     *
     * @param articleId
     * @return
     */
    Response<ArticleVo> queryArticle(Long articleId);

    /**
     * 查询热评
     *
     * @param req
     * @param articleId
     * @return
     */
    Response<List<ArticleVo>> queryTopComments(PageReq req, Long articleId);

    /**
     * 分页查询评论或子评论 需要判断动态和主评论是否已经被删除
     *
     * @param req
     * @param articleId
     * @return
     */
    Response<List<ArticleVo>> queryCommentsList(PageReq req, Long articleId);

    /**
     * 查询个人发表的动态数
     *
     * @return
     */
    Response<Integer> queryArticleCount();

    /**
     * 发表动态
     *
     * @param vo
     * @return
     */
    Response<Long> publishArticle(ArticleVo vo);

    /**
     * 发表评论
     *
     * @param vo
     * @return
     */
    Response<Long> publishComments(ArticleVo vo);

    /**
     * 点赞和取消点赞 重新点赞为更新
     *
     * @param articleId
     * @return
     */
    Response<Long> praiseArticle(Long articleId);

    /**
     * 删除动态 需要注意楼主可以删除评论，其他用户只能删除个人发表的评论
     *
     * @param articleId
     * @return
     */
    Response<Boolean> deleteArticle(Long articleId);

    /**
     * 删除评论
     *
     * @param id
     * @return
     */
    Response<Boolean> deleteComments(Long id);

    /**
     * 查询举报类型以及举报结果
     *
     * @param id
     * @return
     */
    Response<List<String>> queryReprotList(Long id);

    /**
     * 举报
     *
     * @param id
     * @param reprotMsg 举报信息
     * @return
     */
    Response<Boolean> report(Long id, String reprotMsg);

    /**
     * 分页查询点赞和评论提醒
     *
     * @param req
     * @return
     */
    Response<List<ArticleTipVo>> queryArticleTipList(ArticleTipReq req);

    /**
     * 分页查询动态（管理后台使用）
     *
     * @param req
     * @return
     */
    CommonResp<List<ArticleVo>> queryArticleListPage(ArticleReq req);

    /**
     * 更新/删除动态（管理后台使用）
     *
     * @param vo
     * @return
     */
    Response<ArticleVo> updateArticle(ArticleVo vo);

    /**
     * 分页查询举报信息（管理后台使用）
     *
     * @param req
     * @return
     */
    CommonResp<List<ArticleTipVo>> queryArticleTipListPage(ArticleTipReq req);

    /**
     * 更新举报状态（管理后台使用）
     *
     * @param vo
     * @return
     */
    Response<ArticleTipVo> updateArticleTip(ArticleTipVo vo);

    /**
     * 查询（并确认）未读点赞数和回复数
     *
     * @return
     */
    Response<ArticleTip> queryArticleTips();

    /**
     * 查询动态分享信息
     *
     * @param articleId
     * @return
     */
    Response<WechatShareInfo> queryArticleShareInfo(Long articleId);

    /**
     * 收藏动态
     *
     * @param id
     * @return
     */
    Response<Boolean> collectArticle(Long id);

    /**
     * 删除收藏
     *
     * @param id
     * @return
     */
    Response<Boolean> removeCollectArticle(Long id);

    /**
     * 分页查询收藏的动态
     *
     * @param req
     * @return
     */
    Response<List<ArticleVo>> queryCollectArticle(PageReq req);

    /**
     * 动态列表查询V2
     *
     * @param req
     * @return
     * @since v3.0.3
     */
    Response<List<ArticleVo>> queryArticleListV2(ArticleReq req);

    /**
     * 动态分享
     *
     * @param id
     * @return
     * @since v3.0.3
     */
    Response<Boolean> shareArticle(Long id);

    /**
     * 发表动态v1
     *
     * @param vo
     * @return
     */
    Response<Boolean> publishArticleV1(ArticleVo vo);

    /**
     * 动态列表查询V3
     *
     * @param req
     * @return
     * @since v3.0.3
     */
    Response<List<ArticleVo>> queryArticleListV3(ArticleReq req);

    /**
     * 分页查询点赞和评论提醒
     *
     * @param req
     * @return
     */
    Response<List<ArticleTipV2>> queryArticleTipListV1(ArticleTipReq req);


    /**
     * 查询点赞、评论、分享、收藏、举报数未读数
     *
     * @param
     * @return
     */
    Response<Integer> queryArticleTipsV1();

    /**
     * 分页查询招工信息
     *
     * @param req
     * @return
     */
    Response<List<ArticleVo>> queryRecruitment(ArticleReq req);

    /**
     * 举报用户
     *
     * @param partyId
     * @param reprotMsg 举报信息
     * @return
     */
    Response<Boolean> reportUser(Long partyId, String reprotMsg);

    /**
     * 查询用户最近发布的3个工作地点
     *
     * @return
     */
    Response<List<String>> queryUserUsedAddress();

    /**
     * 查询学习专区文件详情，用于回显
     *
     * @param id
     * @return
     */
    Response<LearningPPtVO> queryLearningFile(Long id);

    /**
     * 查询学习专区文件分页
     *
     * @param queryParam
     * @return
     */
    Response<PageResult<ArticleVo>> queryLearningFileList(ArticleReq queryParam);

    /**
     * 查询学习专区文件详情，用于回显
     *
     * @param inputVO
     * @return
     */
    Response<Boolean> updateLearningFile(LearningPPtVO inputVO);

    /**
     * 查询学习专区文件详情，用于回显
     *
     * @param id
     * @return
     */
    Response<Boolean> setTop4Article(Long id);

    /**
     * 删除学习专区文件
     *
     * @param id
     * @return
     */
    Response<Boolean> deleteLearningFile(Long id);

    /**
     * 根据类型查询微博数
     *
     * @param type
     * @return
     */
    Response<Integer> queryArticleCountByType(String type);

    /**
     * 根据id查微博
     * @param ids
     * @return
     */
    Response<List<ArticleVo>> queryArticleListByIds(Long[] ids);

    /**
     * 根据参数查动态数
     * @param req
     * @return
     */
    Response<Integer> queryArticleContByParam(ArticleReq req);

    /**
     * 发布提醒
     * @param articleTipVo
     * @return
     */
    Response<Boolean> publishArticleTip(ArticleTipVo articleTipVo);

    /**
     * 根据参数查询微博 电商与top后台通用
     * @param req
     * @return
     */
    Response<List<ArticleVo>> queryArticleListByParam(ArticleReq req);

    /**
     * 取当前用户关注/本市/本区县/本乡镇微博 4.0
     * 关注：follow
     * 本市：city
     * 县:county
     * 镇：town
     * @return
     */
    Response<List<ArticleVo>> queryFollowArticle(ArticleReq req);

    /**
     * 查推荐微博4.0
     * @return
     */
    Response<List<ArticleVo>> queryRecommendArticle();

    /**
     * 查询微博列表，不需要按照置顶排序
     * @param req
     * @return
     */
    Response<List<ArticleVo>> queryArticleListV4(ArticleReq req);

    /**根据类型查询全部热门微博
     * @param type
     * @return
     */
    Response<List<Long>> queryPopularArticles(String type);

    /**
     * 语音转成Mp3格式
     * @param articleAudio
     * @return
     */
    Response<String> articleAudioChangeMp3(String articleAudio);

    /**
     * 查询top端短视频
     * @param articleReq
     * @return
     */
    Response<PageResult<ArticleVo>> queryShortVideo(ArticleReq articleReq);

    /** 发布短视频
     * @param vo
     * @return
     */
    Response<Boolean> publishShortVideo(ArticleVo vo);

    /**
     * 根据类型分页查询
     * @param pageReq 分页参数
     * @param type 类型 微博、短视频等
     * @return
     */
    Response<List<ArticleVo>> queryCollectArticleV2(PageReq pageReq,String type);

    /**查询全部主题
     * @return
     */
    Response<List<ArticleTopicVo>> queryTopicList();

    /**短视频查询
     * @param topic
     * @param lastId
     * @return
     */
    Response<List<ArticleVo>> queryShortVideos(String topic,Long lastId);

    /**
     * 新增话题
     * @param articleTopicVo 话题信息
     * @return
     */
    Response<Boolean> addArticleTopic(ArticleTopicVo articleTopicVo);

    /**
     * 话题分页查询(关键字搜索)
     * @param articleTopicReq
     * @return
     */
    Response<PageResult<ArticleTopicVo>> queryArticleTopicListByPageAndParams(ArticleTopicReq articleTopicReq);

    /**
     * 更新话题状态
     * @param articleTopicVo
     * @return
     */
    Response<Boolean> updateArticleTopic(ArticleTopicVo articleTopicVo);

    /**
     * 查询被举报的微博和短视频
     * @param articleTipReq
     * @return
     */
    Response<PageResult<ArticleTipVoV3>> queryArticleTipListByParams(ArticleTipReq articleTipReq);


    /**
     * top端查询用户最近发布的3个工作地点
     *
     * @return
     */
    Response<List<String>> queryTOPUserUsedAddress(String loginName);
    

}

