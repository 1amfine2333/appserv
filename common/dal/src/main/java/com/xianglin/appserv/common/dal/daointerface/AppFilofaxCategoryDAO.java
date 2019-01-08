package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppFilofaxCategory;
import com.xianglin.appserv.common.dal.dataobject.AppFilofaxDetail;

import java.util.List;
import java.util.Map;

public interface AppFilofaxCategoryDAO extends BaseDAO<AppFilofaxCategory>{

    List<AppFilofaxCategory> selectList(Map<String, Object> paras);

}
