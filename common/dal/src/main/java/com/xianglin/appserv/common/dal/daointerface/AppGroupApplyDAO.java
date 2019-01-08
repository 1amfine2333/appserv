package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppGroup;
import com.xianglin.appserv.common.dal.dataobject.AppGroupApply;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 
 */
@Repository
public interface AppGroupApplyDAO extends BaseDAO<AppGroupApply> {


    List<AppGroupApply> queryGroupApply(Map<String, Object> paras);

    int queryCount(Map<String, Object> paras);

    List<AppGroupApply> query(Map<String, Object> paras);
}
