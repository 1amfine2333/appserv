package com.xianglin.appserv.common.dal.daointerface;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xianglin.appserv.common.dal.dataobject.AppClientLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 客户端日志管理
 */
@Repository
public interface AppClientLogDAO extends BaseMapper<AppClientLog> {

}
