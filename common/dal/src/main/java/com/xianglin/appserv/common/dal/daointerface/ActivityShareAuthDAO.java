package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.ActivityShareAuth;
import com.xianglin.appserv.common.dal.dataobject.ActivityShareDaily;

import java.util.List;
import java.util.Map;


public interface ActivityShareAuthDAO extends BaseDAO<ActivityShareAuth> {

    /**根据(条件)分页查询
     * @param paras
     * @return
     */
    List<ActivityShareAuth> selectList(Map<String,Object> paras);
}