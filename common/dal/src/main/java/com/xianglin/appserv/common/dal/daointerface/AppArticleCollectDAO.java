package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppArticle;
import com.xianglin.appserv.common.dal.dataobject.AppArticleCollect;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * app 动态收藏
 */
@Repository
public interface AppArticleCollectDAO extends BaseDAO<AppArticleCollect> {

	/** 根据条件查询
	 * @param paras
	 * @return
	 */
	List<AppArticleCollect> selectList(Map<String, Object> paras);

}