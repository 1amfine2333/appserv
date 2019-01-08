package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.ActivityShareDaily;

import java.util.List;
import java.util.Map;


public interface ActivityShareDailyDAO extends BaseDAO<ActivityShareDaily> {

    /**根据(条件)分页查询
     * @param paras
     * @return
     */
    List<ActivityShareDaily> selectList(Map<String,Object> paras);
}