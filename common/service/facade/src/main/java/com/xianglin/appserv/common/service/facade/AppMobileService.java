package com.xianglin.appserv.common.service.facade;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.AppMobileVo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/12/14
 * Time: 10:29
 */
public interface  AppMobileService {


    Response<List<AppMobileVo>> getAppMobileList(AppMobileVo req);

    Response<AppMobileVo> getAppMobile(AppMobileVo req);

    Response<Integer> updateAppMobile(AppMobileVo req);

    Response<Integer> insertAppMobile(AppMobileVo req);

}
