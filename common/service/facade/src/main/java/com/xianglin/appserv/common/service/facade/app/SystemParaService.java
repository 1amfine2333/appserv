package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.MapVo;
import com.xianglin.appserv.common.service.facade.model.vo.SysParaVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;

import java.util.List;

/**
 * Created by wanglei on 2017/1/4.
 */
public interface SystemParaService {

    /**
     * 查询系统参数
     * @param paraName
     * @return
     */
    Response<SysParaVo> queryPara(String paraName);

    /**
     * 分页查询系统参数
     * @param req
     * @return
     */
    Response<List<SysParaVo>> queryPara(PageReq req);

    /**更新系统参数
     * @param para
     * @return
     */
    Response<Boolean> updatePara(SysParaVo para);

    /**
     * 新增系统参数
     * @param para
     * @return
     */
    Response<Boolean> addPara(SysParaVo para);

    /**批量返回app配置参数
     * @return
     */
    Response<List<MapVo>> queryAppParas();
}
