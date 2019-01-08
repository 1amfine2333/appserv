package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppArticle;
import com.xianglin.appserv.common.dal.dataobject.AppGroup;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 
 */
@Repository
public interface AppGroupDAO extends BaseDAO<AppGroup> {
    List<AppGroup> queryGroup(Map<String, Object> paras);

    List<AppGroup> queryGroupLikeName(Map<String, Object> paras);

    int queryCount(Map<String, Object> paras);

    List<AppGroup> query(Map<String, Object> paras);

    List<AppGroup> queryGroupByParas(Map<String, Object> paras);

    int queryCountByParam(Map<String, Object> paras);
}
