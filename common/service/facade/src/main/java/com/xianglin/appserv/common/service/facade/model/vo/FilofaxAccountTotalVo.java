package com.xianglin.appserv.common.service.facade.model.vo;

import java.util.List;

/**乡邻账本，我的钱包总额页面查询
 * Created by wanglei on 2017/7/4.
 */
public class FilofaxAccountTotalVo extends BaseVo{

    private String total;

    private List<FilofaxAccountVo> accountList;

    private String borrow;//借入

    private String lend;//借出

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<FilofaxAccountVo> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<FilofaxAccountVo> accountList) {
        this.accountList = accountList;
    }

    public String getBorrow() {
        return borrow;
    }

    public void setBorrow(String borrow) {
        this.borrow = borrow;
    }

    public String getLend() {
        return lend;
    }

    public void setLend(String lend) {
        this.lend = lend;
    }
}
