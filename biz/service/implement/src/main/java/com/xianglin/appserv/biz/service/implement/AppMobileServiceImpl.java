package com.xianglin.appserv.biz.service.implement;

import com.xianglin.appserv.common.dal.daointerface.AppMobileMapper;
import com.xianglin.appserv.common.dal.dataobject.AppMobile;
import com.xianglin.appserv.common.service.facade.AppMobileService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.vo.AppMobileVo;
import com.xianglin.appserv.common.util.DTOUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/12/14
 * Time: 10:34
 */
@Service("appMobileService")
public class AppMobileServiceImpl implements AppMobileService {

    @Autowired
    private AppMobileMapper appMobileMapper;
    @Override
    public Response<List<AppMobileVo>> getAppMobileList(AppMobileVo req) {

        Response<List<AppMobileVo>> response = new Response<>(null);
        try {
            AppMobile mobile = DTOUtils.map(req,AppMobile.class);
            if(req.getIsDeleted() == null){
                mobile.setIsDeleted("0");
            }
            List<AppMobile> list = appMobileMapper.selectAll(mobile);
            if(CollectionUtils.isNotEmpty(list)){
                response.setFacade(FacadeEnums.OK);
                response.setResult( DTOUtils.map(list,AppMobileVo.class));
            }else{
                response.setFacade(FacadeEnums.RETURN_EMPTY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    @Override
    public Response<AppMobileVo> getAppMobile(AppMobileVo req) {
        Response<AppMobileVo> response = new Response<>(null);
        try {
            if(req.getMobile() == null){
                response.setFacade(FacadeEnums.E_C_INPUT_INVALID);
                return response;
            }
            AppMobile mobile = DTOUtils.map(req,AppMobile.class);
            AppMobile am = appMobileMapper.selectByMobile(mobile);
            if(null != am){
                response.setFacade(FacadeEnums.OK);
                response.setResult( DTOUtils.map(am,AppMobileVo.class));
            }else{
                response.setFacade(FacadeEnums.RETURN_EMPTY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    @Override
    public Response<Integer> updateAppMobile(AppMobileVo req) {

        Response<Integer> response = new Response<>(null);
        try {
            int i = appMobileMapper.updateByPrimaryKeySelective(DTOUtils.map(req,AppMobile.class));
            if(i>0){
                response.setFacade(FacadeEnums.OK);
                response.setResult(i);
            }else{
                response.setFacade(FacadeEnums.UPDATE_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setFacade(FacadeEnums.UPDATE_FAIL);
        }
        return response;
    }

    @Override
    public Response<Integer> insertAppMobile(AppMobileVo req) {
        Response<Integer> response = new Response<>(null);
        try {
            int i = appMobileMapper.insert(DTOUtils.map(req,AppMobile.class));
            if(i>0){
                response.setFacade(FacadeEnums.OK);
                response.setResult(i);
            }else{
                response.setFacade(FacadeEnums.INSERT_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setFacade(FacadeEnums.INSERT_FAIL);
        }
        return response;
    }
}
