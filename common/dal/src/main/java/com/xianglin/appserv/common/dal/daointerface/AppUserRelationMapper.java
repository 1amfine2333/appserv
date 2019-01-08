package com.xianglin.appserv.common.dal.daointerface;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xianglin.appserv.common.dal.dataobject.AppUserRelation;
import com.xianglin.appserv.common.dal.dataobject.UserInfoWrap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AppUserRelationMapper extends BaseMapper<AppUserRelation> {


}
