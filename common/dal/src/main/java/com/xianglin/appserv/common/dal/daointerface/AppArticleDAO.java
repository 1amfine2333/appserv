package com.xianglin.appserv.common.dal.daointerface;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xianglin.appserv.common.dal.dataobject.AppArticle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * app 动态
 */
@Repository
public interface AppArticleDAO extends BaseMapper<AppArticle> {

    /**
     * 根据条件查询
     *
     * @param paras
     * @return
     */
    List<AppArticle> selectArticleList(Map<String, Object> paras);

    /**
     * 根据条件查询数量
     *
     * @param paras
     * @return
     */
    Integer selectArticleCount(Map<String, Object> paras);


    /**
     * 批量删除动态
     *
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

    /**
     * 更新动态或评论点赞，收藏数，分享数
     *
     * @param id
     * @return
     */
    int updateTipCount(Long id);

    /**
     * 学习资料 加精和不加精
     *
     * @param id
     * @return
     */
    int setTopLevel(@Param("id") Long id);

    /**
     * 阅读数加一
     *
     * @param id
     * @return
     */
    int incrReadCount(@Param("id") Long id);

    List<AppArticle> queryLearningFileList(@Param("startPage") int startPage, @Param("pageSize") int pageSize);

    /**查询学习专区数
     * @return
     */
    int countLearningFile();

    /**根据id[]查询
     * @param ids
     * @return
     */
    List<AppArticle> queryArticleListByIds(Long[] ids);

    /**
     *粉丝数大于20的用户在一周内发布的微博随机取6条 
     * @param param
     * @return
     */
    List<AppArticle> queryRandArticle(Map<String,Object> param);

    /**根据热度查询微博
     * @param startDate
     * @param endDate
     * @param limit
     * @return
     */
    List<Long> selectPopularArticles(@Param("startDate") Date startDate,@Param("endDate")Date endDate,@Param("limit")int limit);

}
