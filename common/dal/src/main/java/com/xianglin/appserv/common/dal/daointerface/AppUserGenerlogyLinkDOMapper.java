package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppUserGenerlogyLinkDO;
import com.xianglin.appserv.common.dal.dataobject.AppUserGenerlogyLinkDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppUserGenerlogyLinkDOMapper {
    long countByExample(AppUserGenerlogyLinkDOExample example);

    int deleteByExample(AppUserGenerlogyLinkDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AppUserGenerlogyLinkDO record);

    int insertSelective(AppUserGenerlogyLinkDO record);

    List<AppUserGenerlogyLinkDO> selectByExample(AppUserGenerlogyLinkDOExample example);

    AppUserGenerlogyLinkDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AppUserGenerlogyLinkDO record, @Param("example") AppUserGenerlogyLinkDOExample example);

    int updateByExample(@Param("record") AppUserGenerlogyLinkDO record, @Param("example") AppUserGenerlogyLinkDOExample example);

    int updateByPrimaryKeySelective(AppUserGenerlogyLinkDO record);

    int updateByPrimaryKey(AppUserGenerlogyLinkDO record);
}