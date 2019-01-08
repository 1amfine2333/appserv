package com.xianglin.appserv.common.service.facade.model.vo;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/11/8
 * Time: 12:26
 */
public class CommonResp<T> extends  PageResp {

    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
