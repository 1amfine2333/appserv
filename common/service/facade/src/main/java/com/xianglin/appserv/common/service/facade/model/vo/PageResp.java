package com.xianglin.appserv.common.service.facade.model.vo;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/11/7
 * Time: 14:33
 */
public class PageResp extends BaseVo {
    private int curPage = 1; // 当前页

    private Integer totalCount = 0;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurPage() {

        return curPage;
    }

    public void setCurPage(int curPage) {

        this.curPage = curPage;
    }

}
