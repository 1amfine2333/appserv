package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppGroupMember;
import com.xianglin.appserv.common.dal.dataobject.AppGroupNotice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 
 */
@Repository
public interface AppGroupNoticeDAO extends BaseDAO<AppGroupNotice> {
    List<AppGroupNotice> queryGroupNotice(Map<String, Object> paras);

    List<AppGroupNotice> query(Map<String, Object> paras);
}
