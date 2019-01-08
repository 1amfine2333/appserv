package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppMobile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppMobileMapper extends BaseDAO<AppMobile> {

    List<AppMobile> selectAll(AppMobile appMobile);

    AppMobile selectByMobile(AppMobile appMobile);
}