package com.xianglin.appserv.common.dal.dataobject;

import com.xianglin.appserv.common.util.EmojiEscapeUtil;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    private Long id;

    private String loginName;

    private String newLoginName;

    private Long partyId;

    private String userType;

    private String deviceId;

    private String sessionId;

    private String nikerName;

    private String headImg;

    @EmojiEscapeUtil.EscapeField
    private String introduce;

    private Long recPartyId;

    private String trueName;

    private String showName;

    private String password;

    private String passwordSalt;

    private String passwordPattern;
    
    private String passwordPatternStatus;

    private String descs;

    private String status;

    private String ryToken;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    private String province;//省

    private String city;//市

    private String county;//县、区

    private String town;//镇

    private String village;//村名

    private String district;//区域合集

    private String location; //所在地

    private String birthday;//生日

    private String gender;//性别
    
    public String showName(){
        if(StringUtils.isNotEmpty(this.nikerName)){
            return this.nikerName;
        }
        if(StringUtils.isNotEmpty(this.trueName)){
            return this.trueName;  
        }
        if( this.partyId!= null){
            return  "xl"+this.partyId;  
        }
        return "";
    }
}
