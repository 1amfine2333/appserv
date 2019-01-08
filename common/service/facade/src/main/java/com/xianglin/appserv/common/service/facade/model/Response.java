/**
 *
 */
package com.xianglin.appserv.common.service.facade.model;

import com.google.common.base.Optional;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import org.apache.commons.lang3.StringUtils;

import static com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums.RETURN_EMPTY;

/**
 * 通用服务响应结果
 *
 * @author pengpeng 2016年2月18日下午4:04:56
 */
public class Response<T> extends com.xianglin.gateway.common.service.facade.model.Response<T> {

    public Response() {
        super(null);
    }

    public Response(T t) {

        super(t);
    }

    /**
     * 检查响应，静态导入后可用于检查响应结果并返回响应结果
     *
     * @param response
     * @param <T>
     * @return
     * @throws IllegalArgumentException 响应失败
     */
    public static <T> Optional<T> checkResponse(Response<T> response) {

        if (response == null) {
           return Optional.absent();
        }
        if (!response.isSuccess()) {
            throw new IllegalArgumentException(response.getTips());
        }
        T result = response.getResult();
        return Optional.fromNullable(result);
    }

    /**
     * 成功响应
     *
     * @param data
     * @return
     */
    public static <T> Response<T> ofSuccess(T data) {

        Response<T> response = new Response<>(data);
        response.setCode(1000);
        return response;

    }

    /**
     * 失败响应的工厂方法(带提示)
     *
     * @param message
     * @return
     */
    public static <T> Response<T> ofFail(String message) {

        Response response = new Response<>(RETURN_EMPTY);
        if (!StringUtils.isBlank(message)) {
            response.setTips(message);
        }
        return response;
    }

    /**
     * 失败响应的工厂方法
     *
     * @return
     */
    public static <T> Response<T> ofFail() {

        return ofFail(null);
    }


    public void setFacade(FacadeEnums facadeEnums) {

        setResonpse(facadeEnums.code, facadeEnums.msg, facadeEnums.tip);
    }

    @Deprecated
    public void setResonpse(ResponseEnum response) {

        setResonpse(response.getCode(), response.getMemo(), response.getTips());
    }


}
