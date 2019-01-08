package com.xianglin.appserv.common.service.facade.model.vo.req;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

@SuppressWarnings("serial")
public class PageReq extends BaseVo {

	private int startPage = 1; // 当前页

	@Deprecated
	private int curPage = 1;

	private int pageSize = 10; // 每页多少行

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		if(startPage <= 0){
			this.startPage  = 1;
		}else{
			this.startPage =  startPage;
		}

	}

	public int getPageSize() {

		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if(pageSize <= 0){
			this.pageSize  = 1;
		}else{
			this.pageSize =  pageSize;
		}
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

}
