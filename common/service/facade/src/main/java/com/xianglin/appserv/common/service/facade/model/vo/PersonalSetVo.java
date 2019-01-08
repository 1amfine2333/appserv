package com.xianglin.appserv.common.service.facade.model.vo;

/**个人设置信息
 * Created by wanglei on 2017/5/10.
 */
public class PersonalSetVo extends BaseVo{

    /**
     *是否设置交易密码
     */
    private boolean hasTranPassword;

    /**
     * 设置交易码地址
     */
    private String tranPasswordUrl;

    /**
     *是否设置登陆密码
     */
    private boolean hasLoginPassword;

    public boolean isHasTranPassword() {
        return hasTranPassword;
    }

    public void setHasTranPassword(boolean hasTranPassword) {
        this.hasTranPassword = hasTranPassword;
    }

    public String getTranPasswordUrl() {
        return tranPasswordUrl;
    }

    public void setTranPasswordUrl(String tranPasswordUrl) {
        this.tranPasswordUrl = tranPasswordUrl;
    }

    public boolean isHasLoginPassword() {
        return hasLoginPassword;
    }

    public void setHasLoginPassword(boolean hasLoginPassword) {
        this.hasLoginPassword = hasLoginPassword;
    }
}
