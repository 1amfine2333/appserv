package com.xianglin.appserv.biz.shared.impl;

/**
 * Created by wanglei on 2017/2/15.
 */

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xianglin.appserv.biz.shared.ArticleManager;
import com.xianglin.appserv.common.dal.daointerface.AppArticleDAO;
import com.xianglin.appserv.common.dal.daointerface.AppArticleTipDAO;
import com.xianglin.appserv.common.dal.daointerface.AppArticleTopicDAO;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.model.enums.ArticlePopularType;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.vo.ArticleTip;
import com.xianglin.appserv.common.util.*;
import jodd.util.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.assertj.core.internal.cglib.core.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author
 * @create 2017-02-15 13:54
 **/
@Service("articleManager")
public class ArticleManagerImpl implements ArticleManager {

    @Autowired
    private AppArticleDAO appArticleDAO;

    @Autowired
    private AppArticleTipDAO appArticleTipDAO;

    @Autowired
    private AppArticleTopicDAO appArticleTopicDAO;

    @Override
    public List<AppArticle> queryArticleList(Map<String, Object> paras) {
        List<AppArticle> appArticles = appArticleDAO.selectArticleList(paras);
        return appArticles;
    }

    @Override
    public Integer queryArticleCount(Map<String, Object> paras) {

        return appArticleDAO.selectArticleCount(paras);
    }

    @Override
    public AppArticle queryArticle(Long id) {

        AppArticle appArticle = appArticleDAO.selectById(id);
        //反转义emoji by songjialin
        EmojiEscapeUtil.deEscapeEmojiString(appArticle);
        return appArticle;
    }

    /**
     * 删除动态或者评论做相应的删除Tip动作
     *
     * @param article
     * @return
     */
    private Boolean updateForArticleTip(AppArticle article) {

        //转义emoji by songjialin
        EmojiEscapeUtil.escapeEmojiString(article);

        Map<String, Object> paras = DTOUtils.queryMap();
        paras.put("pageSize", 10000);
        List<Long> articleTipIds = new ArrayList<Long>();
        // 删除动态
        if (article.getArticleType().equals(Constant.ArticleType.SUBJECT.name())) {
            paras.put("articleId", article.getId());
            List<AppArticle> articleList = appArticleDAO.selectArticleList(paras);
            for (AppArticle appArticle : articleList) {
                // 不管是点赞还是评论,tip的articleId都对应一条article数据,而每条article数据的articleId都是动态id
                articleTipIds.add(appArticle.getId());
                articleTipIds.add(article.getId());
            }
        }
        // 删除主评论
        if (article.getArticleType().equals(Constant.ArticleType.COMMENT.name())) {
            paras.put("commentId", article.getId());
            List<AppArticle> articleList = appArticleDAO.selectArticleList(paras);
            articleTipIds.add(article.getId());
            for (AppArticle appArticle : articleList) {
                // 不管是点赞还是评论,tip的articleId都对应一条article数据,而每条article数据的commentId都是主评论id
                articleTipIds.add(appArticle.getId());
            }
        }
        // 删除子评论
        if (article.getArticleType().equals(Constant.ArticleType.SUBCOMMENT.name())) {
            // 删除赞提醒
            articleTipIds.add(article.getId());
            // 删除子评论提醒
            paras.put("replyId", article.getId());
            List<AppArticle> articleList = appArticleDAO.selectArticleList(paras);
            for (AppArticle appArticle : articleList) {
                articleTipIds.add(appArticle.getId());
            }
        }
        Boolean flag = true;
        if (articleTipIds.size() != 0) {
            int batchDelete = appArticleTipDAO.batchDelete(articleTipIds);
            flag = batchDelete != 0;
        }
        return flag;
    }

    @Override
    public Boolean updateArticle(AppArticle article) {

        //转义emoji by songjialin
        EmojiEscapeUtil.escapeEmojiString(article);

        Boolean flag = appArticleDAO.updateById(article) == 1;
        updateArtilceWeight(article.getId());
        return flag;
    }

    @Override
    public Boolean addArticle(AppArticle article) {
        //转义emoji by songjialin
        EmojiEscapeUtil.escapeEmojiString(article);
        if(StringUtils.isNotEmpty(article.getArticle())){
            if (article.getArticle().length() > 2000) {
                article.setArticle(article.getArticle().substring(0, 1999));
            }
            String value = SensitiveWordUtil.filterSensitiveWord(article.getArticle());
            if(!StringUtils.equals(value,article.getArticle())){
                article.setArticleOriginal(article.getArticle());
                article.setArticle(value);
            }
        }

        if (article.getGroupId() == null) {
            article.setGroupId(0L);
        }
        boolean result = appArticleDAO.insert(article) == 1;
        //更新权重
        updateArtilceWeight(article.getId());
        //更新话题
        if(StringUtils.equals(article.getArticleType(), Constant.ArticleType.SHORT_VIDEO.name())){
            updateTopic(article.getId());
        }
        return result;
    }

    @Override
    public List<AppArticleTip> queryArticleTipList(Map<String, Object> paras) {

        return appArticleTipDAO.selectArticleTipList(paras);
    }

    @Override
    public Integer queryArticleTipCount(Map<String, Object> paras) {

        return appArticleTipDAO.selectArticleTipCount(paras);
    }

    @Override
    public AppArticleTip queryArticleTip(Long id) {

        return appArticleTipDAO.selectByPrimaryKey(id);
    }

    @Override
    public Boolean updateArticleTip(AppArticleTip tip) {

        boolean result = appArticleTipDAO.updateByPrimaryKeySelective(tip) == 1;
//        appArticleDAO.updateTipCount(tip.getArticleId());//since v3.0.3
        //since 3.7
        updateArtilceWeight(tip.getArticleId());
        return result;
    }

    @Override
    public Boolean addArticleTip(AppArticleTip tip) {

        boolean result = appArticleTipDAO.insert(tip) == 1;
//        appArticleDAO.updateTipCount(tip.getArticleId());//since v3.0.3
        //since 3.7
        updateArtilceWeight(tip.getArticleId());
        ;
        return result;
    }

    @Override
    public Long publishComments(AppArticle vo) {
        // 需要区分评论类型，
        // 评论发布成功，需要给提示
        if (vo.getArticleId().equals(vo.getReplyId())) {
            vo.setArticleType(Constant.ArticleType.COMMENT.name());
        } else {
            vo.setArticleType(Constant.ArticleType.SUBCOMMENT.name());
        }
        if(StringUtils.isNotEmpty(vo.getArticle())){
            String value = SensitiveWordUtil.filterSensitiveWord(vo.getArticle());
            if(!StringUtils.equals(value,vo.getArticle())){
                vo.setArticleOriginal(vo.getArticle());
                vo.setArticle(value);
            }
        }
        appArticleDAO.insert(vo);

        updateCommentCount(vo);

        Long toPartyId = vo.getReplyPartyId();
        AppArticle selectByPrimaryKey = appArticleDAO.selectById(vo.getReplyId());
        if (toPartyId == null) {
            toPartyId = selectByPrimaryKey.getPartyId();
        }
        // 评论提示
        AppArticleTip tip = new AppArticleTip();
        tip.setPartyId(vo.getPartyId());
        tip.setArticleId(vo.getId());
        tip.setToPartyId(toPartyId);
        tip.setTipType(Constant.ArticleTipType.COMMENT.name());
        tip.setDealStatus(Constant.YESNO.NO.code);
        tip.setContent(vo.getArticle());
        if (tip.getPartyId().equals(tip.getToPartyId())) {
            tip.setReadStatus("Y");
        } else {
            tip.setReadStatus("N");
        }
        AppArticle appArticle = appArticleDAO.selectOne(AppArticle.builder().id(vo.getArticleId()).build());
        if(appArticle != null && appArticle.getArticleType().equals(Constant.ArticleType.SHORT_VIDEO.name())){
            tip.setArticleType(Constant.ArticleType.SHORT_VIDEO.name());
        }else if(appArticle != null && appArticle.getArticleType().equals(Constant.ArticleType.SUBJECT.name())){
            tip.setArticleType(Constant.ArticleType.SUBJECT.name()); 
        }
        appArticleTipDAO.insert(tip);
        return vo.getId();
    }

    @Override
    public Boolean deleteComments(AppArticle comments) {
        // 先删除消息提醒tip
        updateForArticleTip(comments);
        // 删除的是主评论,先删除它下面的子评论
        if (comments.getArticleType().equals(Constant.ArticleType.COMMENT.name())) {
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("pageSize", comments.getReplyCount() + 10);// 多10条防止系统异常时出脏数据
            paras.put("commentId", comments.getId());
            List<AppArticle> selectArticleList = appArticleDAO.selectArticleList(paras);
            List<Long> list = new ArrayList<Long>();
            for (AppArticle appArticle : selectArticleList) {
                list.add(appArticle.getId());
            }
            if (list.size() != 0) {
                appArticleDAO.batchDelete(list);
            }
        }
        comments.setIsDeleted(Constant.YESNO.YES.code);
        appArticleDAO.updateById(comments);
        updateCommentCount(comments);
        return true;
    }

    @Override
    public List<AppArticleTip> queryUpdateArticleTip(Map<String, Object> paras) {

        List<AppArticleTip> selectArticleTipList = appArticleTipDAO.selectArticleTipList(paras);
        appArticleTipDAO.updateReadStatus((Long) paras.get("toPartyId"), (String) paras.get("tipType"));
        return selectArticleTipList;
    }

    @Override
    public List<AppArticleTip> queryArticleTipByParam(Map<String, Object> paras) {

        List<AppArticleTip> selectArticleTipList = appArticleTipDAO.queryArticleTipByParam(paras);
        appArticleTipDAO.updateReadStatus((Long) paras.get("toPartyId"), (String) paras.get("tipType"));
        return selectArticleTipList;
    }

    /**
     * 更新动态和主评论的评论数
     *
     * @param comments
     */
    private void updateCommentCount(AppArticle comments) {
        //更新动态评论数
        AppArticle article = appArticleDAO.selectById(comments.getArticleId());
        Map<String, Object> paras = new HashMap<>();
        paras.put("articleId", article.getId());
        paras.put("articleType", Constant.ArticleType.COMMENT.name());
        if (article != null) {
            int count = appArticleDAO.selectArticleCount(paras);
            article.setReplyCount(count);
            appArticleDAO.updateById(article);
            updateArtilceWeight(comments.getArticleId());
        }

        if (comments.getCommentId() != null) {// 更新主评论的回复数
            article = appArticleDAO.selectById(comments.getCommentId());
            paras.clear();
            paras.put("commentId", article.getId());
            int count = appArticleDAO.selectArticleCount(paras);
            article.setReplyCount(count);
            appArticleDAO.updateById(article);
        }
    }

    @Override
    public boolean setTopLevel4LearningPPT(Long id) {

        return appArticleDAO.setTopLevel(id) == 1;
    }

    @Override
    public boolean incrReadCounts(Long id) {

        Preconditions.checkArgument(id != null);
        return appArticleDAO.incrReadCount(id) == 1;
    }

    @Override
    public List<AppArticle> queryLearningFileList(int startPage, int pageSize) {

        return appArticleDAO.queryLearningFileList(startPage, pageSize);
    }

    @Override
    public int countLearningFile() {

        return appArticleDAO.countLearningFile();
    }

    @Override
    public List<AppArticle> queryArticleListByIds(Long[] ids) {
        return appArticleDAO.queryArticleListByIds(ids);
    }

    /**
     * 粉丝数大于20的用户在一周内发布的微博随机取6条
     *
     * @param param
     * @return
     */
    @Override
    public List<AppArticle> queryRandArticle(Map<String, Object> param) {
        return appArticleDAO.queryRandArticle(param);
    }

    @Override
    public List<Long> queryPopularArticles(ArticlePopularType type) {
        //查询对应缓存
        DateTime startDate = null;
        DateTime endDate = null;
        switch (type){
            case TODAY:
                startDate = DateTime.now().minusDays(1);
                break;
            case YESTERDAY:
                startDate = DateTime.now().minusDays(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
                endDate = startDate.plusDays(1);
                break;
            case BEFOREYESTERDAY:
                startDate = DateTime.now().minusDays(2).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
                endDate = startDate.plusDays(1);
                break;
            case WEEK:
                startDate = DateTime.now().minusDays(7).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
                break;
            case ALL:
        }
        Date startDateTime = startDate != null?startDate.toDate():null;
        Date endDateTime = endDate != null?endDate.toDate():null;
        int limit = SysConfigUtil.getInt("ARTICLE_WEIGHT_LIMIT");
        return appArticleDAO.selectPopularArticles(startDateTime,endDateTime,limit);
    }

    @Override
    public List<AppArticleTopic> queryArticleTopic(AppArticleTopic para,String orderBy,String stopTime, Page page) {
        EntityWrapper<AppArticleTopic> ew = new EntityWrapper();
        ew.like(StringUtils.isNoneEmpty(para.getContent()),"CONTENT",para.getContent());
        ew.ge(para.getCreateTime()!=null,"CREATE_TIME",para.getCreateTime());
        ew.le(StringUtils.isNoneEmpty(stopTime),"CREATE_TIME",stopTime);
        ew.orderBy(StringUtils.isNotEmpty(orderBy),orderBy);
        ew.orderBy("TOP_TIME",false);
        ew.orderBy("ID",false);

        return appArticleTopicDAO.selectPage(new RowBounds(page.getOffset(),page.getPageSize()),ew);
    }

    @Override
    public List<AppArticle> queryArticle(AppArticle para, String orderBy, Page page) {
        EntityWrapper<AppArticle> ew = new EntityWrapper();
        ew.eq(StringUtils.isNotEmpty(para.getArticleType()),"ARTICLE_TYPE",para.getArticleType());
        ew.eq(para.getTopLevel() != null,"TOP_LEVEL",para.getTopLevel());
        ew.eq(StringUtils.isNoneEmpty(para.getRecSign()),"REC_SIGN",para.getRecSign());
        ew.lt(para.getLastId() != null && para.getLastId()>0  ,"ID",para.getLastId());
        ew.like(StringUtils.isNoneEmpty(para.getTopic()),"ARTICLE",Constant.ArticleContent.TOPIC.start +  para.getTopic() + Constant.ArticleContent.TOPIC.end);
        ew.orderBy(StringUtils.isNotEmpty(orderBy),orderBy)
                .orderBy("id",false);
        return appArticleDAO.selectPage(new RowBounds(page.getOffset(),page.getPageSize()),ew);
    }

    @Override
    public boolean updateArticleTopic(AppArticleTopic appArticleTopic) {
        return appArticleTopicDAO.updateById(appArticleTopic)==1;
    }

    @Override
    public Integer queryArticleTopicCout(AppArticleTopic para, String stopTime) {
        EntityWrapper<AppArticleTopic> ew = new EntityWrapper();
        ew.like(StringUtils.isNoneEmpty(para.getContent()),"CONTENT",para.getContent());
        ew.ge(para.getCreateTime()!=null,"CREATE_TIME",para.getCreateTime());
        ew.le(StringUtils.isNoneEmpty(stopTime),"CREATE_TIME",stopTime);
        ew.eq("IS_DELETED",Constant.Delete_Y_N.N.name());
        return appArticleTopicDAO.selectCount(ew);
    }

    @Override
    public List<AppArticleTip> selectArticleTipListByParam(Map<String, Object> paras) {
        return appArticleTipDAO.selectArticleTipListByParam(paras);
    }

    @Override
    public Integer queryArticleTipCountIsDeleted(Map<String,Object> paras) {
        return appArticleTipDAO.queryArticleTipCountIsDeleted(paras);
    }


    @Override
    public boolean addAppArticleTopic(AppArticleTopic appArticleTopic) {
        return appArticleTopicDAO.insert(appArticleTopic)==1;
    }

    /**
     * 更新点赞数，评论数，收藏数记权重值
     *
     * @param id
     */
    @Async
    public void updateArtilceWeight(Long id) {
        if(id == null){ 
            return;
        }
        appArticleDAO.updateTipCount(id);
        AppArticle article = appArticleDAO.selectById(id);
        //更新权重值
        if (article != null) {
            double A = article.getCreateTime().getTime() / 1000;
            long B = SysConfigUtil.getLong("ARTICLE_WEIGHT_B");
            long t = SysConfigUtil.getLong("ARTICLE_WEIGHT_t");

            BigDecimal kr = new BigDecimal(SysConfigUtil.getStr("ARTICLE_WEIGHT_Kr"));
            BigDecimal Kc = new BigDecimal(SysConfigUtil.getStr("ARTICLE_WEIGHT_Kc"));
            BigDecimal Kl = new BigDecimal(SysConfigUtil.getStr("ARTICLE_WEIGHT_Kl"));
            BigDecimal Kf = new BigDecimal(SysConfigUtil.getStr("ARTICLE_WEIGHT_Kf"));

            BigDecimal Z = new BigDecimal(article.getShareCount()).multiply(kr)
                    .add(new BigDecimal(article.getReplyCount()).multiply(Kc))
                    .add(new BigDecimal(article.getPraiseCount()).multiply(Kl))
                    .add(new BigDecimal(article.getCollectCount()).multiply(Kf)).add(new BigDecimal("1"));
//            if(Z.)
            BigDecimal change = article.getWeightAdjust() == null ? BigDecimal.ZERO : article.getWeightAdjust();
            BigDecimal rank = new BigDecimal(Math.log10(Z.doubleValue()) + (A - B) / t + change.doubleValue());
            rank = rank.setScale(4, BigDecimal.ROUND_DOWN);
            article.setWeight(rank);
            appArticleDAO.updateById(article);
        }
    }

    /**
     * 更新点赞数，评论数，收藏数记权重值
     *
     * @param id
     */
    @Async
    public void updateTopic(Long id) {
        if(id == null){
            return;
        }
        AppArticle article = appArticleDAO.selectById(id);
        //更新权重值
        if (article != null && StringUtils.isNotEmpty(article.getArticle())) {
            AtomicReference<String> content = new AtomicReference(StringUtils.replacePattern(article.getArticle(),"<.*?>",""));
            Arrays.stream(Optional.ofNullable(StringUtils.substringsBetween(content.get(), "#", "#")).orElse(new String[0])).map(v -> "#"+v+"#").filter(v ->{
                int result = appArticleTopicDAO.updateForSet("POPULARITY = POPULARITY +1,UPDATE_TIME = now()",new EntityWrapper<>(AppArticleTopic.builder().content(v).build()));
                return result > 0;
            }).distinct().collect(Collectors.toMap(v -> v,v -> Constant.ArticleContent.TOPIC.start + v + Constant.ArticleContent.TOPIC.end)).forEach((k,v) ->{
                content.set(StringUtils.replace(content.get(),k,v));
            });
            article.setArticle(content.get());
            appArticleDAO.updateById(article);
        }
    }

    public static void main(String[] args) {
        AppArticle article = AppArticle.builder().shareCount(0).replyCount(6).praiseCount(2).collectCount(0).build();
        double A = System.currentTimeMillis() / 1000;
        A = 1531299223L;
        long B = 1483200000L;
        long t = 45000;

        BigDecimal kr = new BigDecimal("3");
        BigDecimal Kc = new BigDecimal("1");
        BigDecimal Kl = new BigDecimal("0.5");
        BigDecimal Kf = new BigDecimal("3");

        BigDecimal Z = new BigDecimal(article.getShareCount()).multiply(kr)
                .add(new BigDecimal(article.getReplyCount()).multiply(Kc))
                .add(new BigDecimal(article.getPraiseCount()).multiply(Kl))                    
                .add(new BigDecimal(article.getCollectCount()).multiply(Kf)).add(new BigDecimal("0.1"));

        System.out.println(Z);
        BigDecimal change = article.getWeightAdjust() == null ? BigDecimal.ZERO : article.getWeightAdjust();
        BigDecimal rank = new BigDecimal(Math.log10(Z.doubleValue()) + (A - B) / t + change.doubleValue());
        rank = rank.setScale(4, BigDecimal.ROUND_DOWN);
        System.out.println(rank.toString());

        BigDecimal big = new BigDecimal("1047.6429588");
        big.setScale(4, BigDecimal.ROUND_DOWN);
        System.out.println(big.setScale(4, BigDecimal.ROUND_DOWN));


    }
}
