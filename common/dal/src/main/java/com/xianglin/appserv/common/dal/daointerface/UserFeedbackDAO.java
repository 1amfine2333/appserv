package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppArticle;
import com.xianglin.appserv.common.dal.dataobject.UserFeedback;

import java.util.List;
import java.util.Map;

public interface UserFeedbackDAO extends BaseDAO<UserFeedback> {

	/** 根据条件查询
	 * @param paras
	 * @return
	 */
	List<UserFeedback> selectList(Map<String,Object> paras);

	/** 根据条件查询数量
	 * @param paras
	 * @return
	 */
	Integer selectCount(Map<String, Object> paras);

}
