package com.xianglin.appserv.biz.shared;

import com.xianglin.appserv.common.dal.dataobject.AppPropExtend;

import java.util.List;
import java.util.Map;

/**
 * Describe :
 * Created by xingyali on 2017/12/8 17:23.
 * Update reason :
 */
public interface PropExtendManager {

    /**
     * @param
     * @return
     */
    List<AppPropExtend> queryChannel(Map<String, Object> map);

    /**
     * @param apppropextend
     * @return
     */
    Boolean update(AppPropExtend apppropextend);

    /**
     * @param appPropExtend
     * @return
     */
    Boolean insert(AppPropExtend appPropExtend);

    /**
     * @param id
     * @return
     */
    AppPropExtend selectAppPropExtendById(Long id);

    /**
     * 根据条件查询扩展配置，若不存在则新建一个并返回
     * 必须relationId，type，ekey
     *
     * @param paras
     * @return
     */
    AppPropExtend queryAndInit(AppPropExtend paras);

    Boolean insertExceptPropExtend(AppPropExtend build);

    Boolean updateByParam(Map<String, String> param, AppPropExtend build);

    /**
     * 根据条件查询扩展配置
     * 必须relationId，type，ekey
     *
     * @param 
     * @return
     */
    AppPropExtend selectProp(Long partyId, String type, String ekey);
}
