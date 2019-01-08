package com.xianglin.appserv.common.service.facade.base;

import java.io.Serializable;
import java.util.List;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/4/13 14:39.
 */

public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -6698810165358114188L;

    private int totalSize;

    private List<T> data;

    public PageResult() {

    }

    public PageResult(int totalSize, List<T> data) {

        this.totalSize = totalSize;
        this.data = data;
    }

    public int getTotalSize() {

        return totalSize;
    }

    public void setTotalSize(int totalSize) {

        this.totalSize = totalSize;
    }

    public List<T> getData() {

        return data;
    }

    public void setData(List<T> data) {

        this.data = data;
    }
}
