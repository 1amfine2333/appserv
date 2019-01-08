package com.xianglin.appserv.common.service.facade.model.vo;

/**
 * Created by wanglei on 2017/9/12.
 */
public class MerchantInfoVo extends BaseVo{

    private String merchantCode;

    private String merchantName;

    private String merchantFullname;

    private String shopCode;

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantFullname() {
        return merchantFullname;
    }

    public void setMerchantFullname(String merchantFullname) {
        this.merchantFullname = merchantFullname;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }
}
