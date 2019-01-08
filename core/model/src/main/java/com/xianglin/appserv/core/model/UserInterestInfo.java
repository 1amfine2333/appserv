package com.xianglin.appserv.core.model;

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

import java.util.Set;

/**用户秒息宝活动缓存对象，仅在秒息宝业务中使用
 * Created by wanglei on 2017/10/25.
 */
public class UserInterestInfo extends BaseVo{

    public static final String USER_INTEREST = "UserInterestInfo";

    /**
     *手机号
     * 使用用户登陆手机号
     */
    private String mobileNo;

    /**
     *外系统id
     * 本系统使用用户的partyId
     */
    private String openId;

    /**
     *访问令牌
     */
    private String accessToken;

    /**
     *刷新令牌
     */
    private String refreshToken;

    /**
     *加密key
     */
    private String key;

    /**
     *权限
     */
    private Set<String> scope;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Set<String> getScope() {
        return scope;
    }

    public void setScope(Set<String> scope) {
        this.scope = scope;
    }
}
