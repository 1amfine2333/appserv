package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppUserGenealogy;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AppUserGenealogyDAO extends BaseDAO<AppUserGenealogy> {

    List<AppUserGenealogy> query(Map<String, Object> paras);

    List<AppUserGenealogy> queryUserGenealogy(Map<String, Object> paras);

    Integer deleteAllByCreator(@Param("partyId") Long toPartyId);
}
