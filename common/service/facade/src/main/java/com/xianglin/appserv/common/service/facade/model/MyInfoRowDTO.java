package com.xianglin.appserv.common.service.facade.model;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

/**
 * 我的tab中每行数据控制
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/12/9
 * Time: 9:32
 */
public class MyInfoRowDTO  extends BaseVo{

    private static final long serialVersionUID = -1240236567391975282L;


    //个人信息
    private String userInfoRow;
    private String userInfoRowTip;

    /**
     * 资金账户
     */
    private String accountRow;
    private String accountRowTip;

    /**
     * 联系客户经理
     */
    private String contractManagerRow;
    private String contractManagerTip;

    /**
     * 邀请
     */
    private String inviteRow;
    private String inviteRowTip;

    /**
     * 检查版本
     */
    private String aboutAppRow;
    private String aboutAppTip;
    /**
     * 充值密码
     */
    private String resetPwRow;
    private String resetPwRowTip;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserInfoRow() {
        return userInfoRow;
    }

    public void setUserInfoRow(String userInfoRow) {
        this.userInfoRow = userInfoRow;
    }

    public String getUserInfoRowTip() {
        return userInfoRowTip;
    }

    public void setUserInfoRowTip(String userInfoRowTip) {
        this.userInfoRowTip = userInfoRowTip;
    }

    public String getAccountRow() {
        return accountRow;
    }

    public void setAccountRow(String accountRow) {
        this.accountRow = accountRow;
    }

    public String getAccountRowTip() {
        return accountRowTip;
    }

    public void setAccountRowTip(String accountRowTip) {
        this.accountRowTip = accountRowTip;
    }

    public String getContractManagerRow() {
        return contractManagerRow;
    }

    public void setContractManagerRow(String contractManagerRow) {
        this.contractManagerRow = contractManagerRow;
    }

    public String getContractManagerTip() {
        return contractManagerTip;
    }

    public void setContractManagerTip(String contractManagerTip) {
        this.contractManagerTip = contractManagerTip;
    }

    public String getInviteRow() {
        return inviteRow;
    }

    public void setInviteRow(String inviteRow) {
        this.inviteRow = inviteRow;
    }

    public String getInviteRowTip() {
        return inviteRowTip;
    }

    public void setInviteRowTip(String inviteRowTip) {
        this.inviteRowTip = inviteRowTip;
    }

    public String getAboutAppRow() {
        return aboutAppRow;
    }

    public void setAboutAppRow(String aboutAppRow) {
        this.aboutAppRow = aboutAppRow;
    }

    public String getAboutAppTip() {
        return aboutAppTip;
    }

    public void setAboutAppTip(String aboutAppTip) {
        this.aboutAppTip = aboutAppTip;
    }

    public String getResetPwRow() {
        return resetPwRow;
    }

    public void setResetPwRow(String resetPwRow) {
        this.resetPwRow = resetPwRow;
    }

    public String getResetPwRowTip() {
        return resetPwRowTip;
    }

    public void setResetPwRowTip(String resetPwRowTip) {
        this.resetPwRowTip = resetPwRowTip;
    }
}
