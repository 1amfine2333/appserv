/**
 *
 */
package com.xianglin.appserv.biz.shared.util;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Response 工具类
 *
 * @author pengpeng 2016年2月24日下午4:34:31
 */
public class ResponseUtils {

    private static final Logger logger = LoggerFactory.getLogger(ResponseUtils.class);

    /**
     * 使用指定的resultEnum构造Response
     *
     * @param resultEnum
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Response<T> toResponse(ResultEnum resultEnum) {
        Response<?> response = new Response<String>(null);
        response.setCode(resultEnum.getCode());
        response.setMemo(resultEnum.getMemo());
        response.setTips(resultEnum.getTips());
        return (Response<T>) response;
    }

    @SuppressWarnings("unchecked")
    public static <T> Response<T> toResponse(ResponseEnum responseEnum) {
        Response<?> response = new Response<String>(null);
        response.setCode(responseEnum.getCode());
        response.setMemo(responseEnum.getMemo());
        response.setTips(responseEnum.getTips());
        return (Response<T>) response;
    }

    /**
     * 创建一个新的Response对象，默认success
     *
     * @return
     */
    public static <T> Response<T> successResponse() {
        Response<T> response = new Response<T>(null);
        response.setCode(ResultEnum.ResultSuccess.getCode());
        return response;
    }

    public static <T> Response<T> successResponse(T t) {
        Response<T> response = new Response<T>(null);
        response.setCode(ResultEnum.ResultSuccess.getCode());
        response.setResult(t);
        return response;
    }

    /**
     * 业务执行返回
     *
     * @param exec
     * @param <T>
     *     使用responseCacheUtils替代
     * @return
     */
    @Deprecated
    public static <T> Response<T> executeResponse(BusiResponse<T> exec) {
        Response<T> resp = new Response<>(null);
        try {
            T t = exec.execute();
            if (t instanceof Response) {
                resp = (Response<T>) t;
            } else {
                resp.setResult(t);
            }
        } catch (BusiException e) {
            logger.warn("BusiException", e);
            resp.setFacade(e.getFacadeEnums());
        } catch (Exception e) {
            logger.warn("", e);
            resp.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return resp;
    }


    /**
     * 业务执行方法
     *
     * @param <T>
     */
    @FunctionalInterface
    public interface BusiResponse<T> {

        T execute() throws Exception;
    }

}
