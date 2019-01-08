package com.xianglin.appserv.common.service.facade.model.vo;

import java.io.Serializable;

/**
 * @author xingyali
 * @description Created by xingyali 
 */

public class PageParam<T> implements Serializable {

    private static final long serialVersionUID = 1604192717721580125L;

    private int pageSize;

    private int curPage;

    private T param;

    public int getPageSize() {

        return pageSize;
    }

    public void setPageSize(int pageSize) {

        this.pageSize = pageSize;
    }

    public int getCurPage() {

        return curPage;
    }

    public void setCurPage(int curPage) {

        this.curPage = curPage;
    }

    public T getParam() {

        return param;
    }

    public void setParam(T param) {

        this.param = param;
    }
}
