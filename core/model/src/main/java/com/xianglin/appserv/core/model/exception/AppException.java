package com.xianglin.appserv.core.model.exception;/**
 * Created by wanglei on 2017/3/22.
 */

import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import org.apache.commons.lang3.StringUtils;

/**
 * app异常类
 *
 * @author wanglei
 * @create 2017-03-22 15:15
 **/
public class AppException extends BusiException{

    private FacadeEnums facade;

    public FacadeEnums getFacade () {
        return facade;
    }

    public AppException (FacadeEnums facade) {
        super(facade);
        this.facade = facade;
    }

    public AppException (FacadeEnums facade,String tip) {
        super(facade,tip);
        this.facade = facade;
        facade.setTip(tip);
    }
}
