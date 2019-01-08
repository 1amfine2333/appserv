package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppActivity;
import com.xianglin.appserv.common.dal.dataobject.AppArticleCollect;
import com.xianglin.appserv.common.dal.dataobject.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * app 动态收藏
 */
@Repository
public interface AppActivityDAO extends BaseDAO<AppActivity> {

	/** 根据条件查询
	 * @param paras
	 * @return
	 */
	List<AppActivity> selectList(Map<String, Object> paras);

	/**根据条件查询
	 * @param paras
	 * @return
	 */
	List<AppActivity> selecActivitytList(@Param("paras") AppActivity paras,@Param("page") Page page);

	/**根据code查询活动
	 * @param activityCode
	 * @return
	 */
	AppActivity selectByCode(String activityCode);
}