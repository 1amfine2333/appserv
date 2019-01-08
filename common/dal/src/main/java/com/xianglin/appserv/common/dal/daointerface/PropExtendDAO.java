package com.xianglin.appserv.common.dal.daointerface;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xianglin.appserv.common.dal.dataobject.AppPropExtend;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Describe :
 * Created by xingyali on 2017/12/11 9:37.
 * Update reason :
 */
public interface PropExtendDAO extends BaseMapper<AppPropExtend> {

    List<AppPropExtend> query(Map<String, Object> map);

    /**
     * 根据唯一条件查询
     *
     * @param relationId
     * @param type
     * @param ekey
     * @return
     */
    AppPropExtend selectProp(@Param("relationId") Long relationId, @Param("type") String type, @Param("ekey") String ekey);

    int insertExceptPropExtend(AppPropExtend build);

    int updateByparamMap(@Param("param") Map<String, String> param, @Param("build") AppPropExtend build);
}
