package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

/**
 * Describe :
 * Created by xingyali on 2017/12/8 17:09.
 * Update reason :
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapVo extends BaseVo{

    /**
     * 用户频道
     */
    public static transient final String USER_CHANNEL = "channel";

    /**
     * 用户连续签到天数
     */
    public static transient final String USER_SIGN_DAYS = "userSignDays";

    /**
     * 用户注册成功弹框
     */
    public static transient final String USER_GOLD_REGISTER_ALERT = "goldRegisterAler";

    /**
     * 推荐用户H5注册页面二维码地址
     */
    public static transient final String RECOMMEND_USER_QR_URL = "qrCode_rec";

    /**
     * 用户的使用过的地址
     */
//    public static transient final String USER_USED_ADDRESS = "userAddress";

    /**
     * 用户注册通道
     */
    public static transient final String USER_REG_CHANNEL = "userRegChannel";

    /**
     * 用户开通的村意见
     */
    public static transient final String USER_OPINION = "userOpinion";

    /**
     * 用户设置图案密码
     */
    public static transient final String USER_PASSWORD_PATTERN = "userPwdPattern";
    
//    public static transient final String USER_RECRUITJOB_DRAFT ="recruitDraft";
    
    public static transient final String USER_QRCODE = "userQrCode";

    /**
     * 用户收款账户
     */
//    public static transient final String USER_RECEIPT_ACCOUNT = "receiptAccount";
    

    private String key;

    private String value;

}
