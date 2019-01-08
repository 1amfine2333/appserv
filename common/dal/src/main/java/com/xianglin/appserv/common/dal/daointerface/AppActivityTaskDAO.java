package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppActivityTask;
import com.xianglin.appserv.common.dal.dataobject.Page;
import com.xianglin.appserv.common.dal.dataobject.UserFeedback;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AppActivityTaskDAO extends BaseDAO<AppActivityTask> {

	/** 根据条件查询
	 * @param paras
	 * @return
	 */
	List<AppActivityTask> selectList(@Param("paras") AppActivityTask para, @Param("page") Page page);

	/** 根据条件查询数量
	 * @param paras
	 * @return
	 */
	int selectCount(AppActivityTask paras);

	/** 新增(带条件)
	 * @param task
	 * @return
	 */
	int insertWithSelect(AppActivityTask task);

    int queryActivityTaskCount(Map<String, Object> paras);

    List<AppActivityTask> query(Map<String, Object> paras);
}
