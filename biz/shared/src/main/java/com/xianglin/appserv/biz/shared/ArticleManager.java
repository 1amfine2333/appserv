package com.xianglin.appserv.biz.shared;/**
 * Created by wanglei on 2017/2/15.
 */

import com.xianglin.appserv.common.dal.dataobject.AppArticle;
import com.xianglin.appserv.common.dal.dataobject.AppArticleTip;
import com.xianglin.appserv.common.dal.dataobject.AppArticleTopic;
import com.xianglin.appserv.common.dal.dataobject.Page;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.ArticlePopularType;
import com.xianglin.appserv.common.service.facade.model.vo.ArticleTopicVo;
import com.xianglin.appserv.common.service.facade.model.vo.ArticleVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 动态管理控制器
 *
 * @author
 * @create 2017-02-15 13:48
 **/
public interface ArticleManager {

    /**
     * 根据条件查询
     *
     * @param paras
     * @return
     */
    List<AppArticle> queryArticleList(Map<String, Object> paras);

    /**
     * 根据条件查询动态数量
     *
     * @param paras
     * @return
     */
    Integer queryArticleCount(Map<String, Object> paras);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    AppArticle queryArticle(Long id);

    /**
     * 更新
     *
     * @param article
     * @return
     */
    Boolean updateArticle(AppArticle article);

    /**
     * 新增
     *
     * @param article
     * @return
     */
    Boolean addArticle(AppArticle article);


    /**
     * 根据条件查询列表
     *
     * @param paras
     * @return
     */
    List<AppArticleTip> queryArticleTipList(Map<String, Object> paras);


    /**
     * 根据条件查询数据
     *
     * @param paras
     * @return
     */
    Integer queryArticleTipCount(Map<String, Object> paras);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    AppArticleTip queryArticleTip(Long id);

    /**
     * 更新
     *
     * @param tip
     * @return
     */
    Boolean updateArticleTip(AppArticleTip tip);

    /**
     * 新增
     *
     * @param tip
     * @return
     */
    Boolean addArticleTip(AppArticleTip tip);

    /**
     * 发表评论
     *
     * @param vo
     * @return
     */
    Long publishComments(AppArticle vo);

    /**
     * 删除评论
     *
     * @param comments
     * @return
     */
    Boolean deleteComments(AppArticle comments);

    /**
     * 查询并更新状态
     *
     * @param paras
     * @return
     */
    List<AppArticleTip> queryUpdateArticleTip(Map<String, Object> paras);

    /**
     * 查询消息提醒，排除已删除的原微博
     *
     * @param paras
     * @return
     */
    List<AppArticleTip> queryArticleTipByParam(Map<String, Object> paras);

    boolean setTopLevel4LearningPPT(Long id);

    boolean incrReadCounts(Long id);

    List<AppArticle> queryLearningFileList(int startPage, int pageSize);

    int countLearningFile();

    /**
     * 根据ids查动态列表
     * @param ids
     * @return
     */
    List<AppArticle> queryArticleListByIds(Long[] ids);

    /**
     * 粉丝数大于20的用户在一周内发布的微博随机取6条
     * @param param
     * @return
     */
    List<AppArticle> queryRandArticle(Map<String,Object> param);

    /**根据类型查询热门微博
     * @param type
     * @return
     */
    List<Long> queryPopularArticles(ArticlePopularType type);

    /** 根据条件查询话题
     * @param para
     * @param page
     * @return
     */
    List<AppArticleTopic> queryArticleTopic(AppArticleTopic para,String orderBy, String stopTime,Page page);

    /**
     * 新增话题
     * @param appArticleTopic
     * @return
     */
    boolean addAppArticleTopic(AppArticleTopic appArticleTopic);

    /** 根据条件查询话题
     * @param para
     * @param orderBy
     * @param page
     * @return
     */
    List<AppArticle> queryArticle(AppArticle para,String orderBy, Page page);

    /**
     * 更新话题状态
     * @param appArticleTopic
     * @return
     */
    boolean updateArticleTopic(AppArticleTopic appArticleTopic);

    /**
     * 查询话题条数
     * @return
     */
    Integer queryArticleTopicCout(AppArticleTopic para,String stopTime);


    /**
     * 查询提示消息列表包括已删除状态
     * @param paras
     * @return
     */
    List<AppArticleTip> selectArticleTipListByParam(Map<String, Object> paras);

    /**
     * 查询提示消息列表包括已删除状态的总条数
     * @param paras
     * @return
     */
    Integer queryArticleTipCountIsDeleted(Map<String,Object> paras);

}

