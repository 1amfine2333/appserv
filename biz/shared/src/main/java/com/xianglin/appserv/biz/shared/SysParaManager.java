package com.xianglin.appserv.biz.shared;

import com.xianglin.appserv.common.dal.dataobject.SystemConfigModel;

import java.util.List;
import java.util.Map;

/**
 * Created by wanglei on 2017/1/4.
 */
public interface SysParaManager {

    /**
     * 分页查询系统参数
     * @param req
     * @return
     */
    List<SystemConfigModel> queryPara(Map<String,Object> req);

    /**
     * 更新系统参数
     * @param para
     * @return
     */
    Boolean updatePara(SystemConfigModel para);

    /**
     * 新增系统参数
     * @param para
     * @return
     */
     Boolean addPara(SystemConfigModel para);

    /**
     * 根据参数名称更新系统参数
     * @param para
     * @return
     */
    Boolean updateByName(SystemConfigModel para);

    /**查询配置信息
     * @param paraName
     * @return
     */
    String queryConfigValue(String paraName);

    /**查询全部配置信息
     * @return
     */
    List<SystemConfigModel> queryPara();
}
